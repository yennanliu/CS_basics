const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const MarkdownIt = require('markdown-it');
const markdownItAnchor = require('markdown-it-anchor');
const hljs = require('highlight.js');
const {
  slugify, TIER_LABELS, prioBadge, PRIO_BADGE_RE, headingText,
  annotatePriorityHeadings, PRIORITY_LEGEND, generateTOC, extractHeadings,
  headingIds, anchorMap, retargetAnchors,
  ensureHeadingIds, groupByCategory, buildPrevNext, buildIndexGrid,
  buildCheatsheetIndex, splitLeadingH1, buildPageContent, extractScope,
  titleCaseFromFile, summariseDoc
} = require('./build-lib');
const { compose, parseStore, docs: zhDocs, orphanStores } = require('./i18n');

// A commit that only retouches a header or fixes a link is not a content update.
// Without this floor, one repo-wide formatting pass stamps today's date on every
// page and the "Updated" line stops meaning anything — which is exactly what the
// bulk Scope-line commit did to 39 of the 74 cheatsheets.
const SUBSTANTIVE_LINE_CHANGES = 10;

// Build a map of filePath → last-modified date string in one git log call.
// Prefers the newest *substantive* commit and falls back to the newest commit of
// any size, so a file whose only history is a small edit still gets a date.
function buildLastModifiedMap(filePaths) {
  const substantive = new Map();
  const anySize = new Map();
  try {
    const raw = execSync(
      `git log --numstat --format="COMMIT %ai" -- ${filePaths.map(f => `"${f}"`).join(' ')}`,
      { encoding: 'utf8', maxBuffer: 64 * 1024 * 1024 }
    );
    let currentDate = null;
    for (const line of raw.split('\n')) {
      if (line.startsWith('COMMIT ')) {
        currentDate = new Date(line.slice(7).trim())
          .toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
        continue;
      }
      if (!line.trim() || !currentDate) continue;
      // numstat: "<added>\t<deleted>\t<path>"; binary files report "-".
      const parts = line.split('\t');
      if (parts.length < 3) continue;
      const file = parts[2].trim();
      const added = Number(parts[0]) || 0;
      const deleted = Number(parts[1]) || 0;
      if (!anySize.has(file)) anySize.set(file, currentDate);
      if (added + deleted >= SUBSTANTIVE_LINE_CHANGES && !substantive.has(file)) {
        substantive.set(file, currentDate);
      }
    }
  } catch (_) {}
  const map = new Map(anySize);
  for (const [file, date] of substantive) map.set(file, date);
  return map;
}


const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: function(str, lang) {
    if (lang && hljs.getLanguage(lang)) {
      try {
        const highlighted = hljs.highlight(str, { language: lang, ignoreIllegals: true }).value;
        return `<pre data-lang="${lang}"><code class="hljs language-${lang}">${highlighted}</code></pre>`;
      } catch (_) {}
    }
    return `<pre><code class="hljs">${md.utils.escapeHtml(str)}</code></pre>`;
  }
}).use(markdownItAnchor, {
  slugify: slugify,
  permalink: markdownItAnchor.permalink && markdownItAnchor.permalink.headerLink
    ? markdownItAnchor.permalink.headerLink({ safariReaderFix: true })
    : true,
  // Fallback for older markdown-it-anchor API (before v9)
  permalinkBefore: true,
  permalinkSymbol: '#'
});

function wrapCodeBlocks(html) {
  return html.replace(
    /<pre( data-lang="([^"]+)")?>((<code[\s\S]*?<\/code>))<\/pre>/g,
    (_, dataLangAttr, lang, inner) => {
      const labelSpan = lang
        ? `<span class="code-lang-label">${lang}</span>`
        : '<span></span>';
      const preAttr = lang ? ` data-lang="${lang}"` : '';
      return `<div class="code-block-wrapper">` +
        `<div class="code-block-header">${labelSpan}` +
        `<button class="copy-btn" onclick="copyCode(this)">Copy</button></div>` +
        `<pre${preAttr}>${inner}</pre></div>`;
    }
  );
}

// ── Markdown link resolution ─────────────────────────────────────────────────
//
// Every .md link in the source tree has to become either a page on this site or
// a GitHub URL, and which one it is depends on whether that markdown file gets
// built. `mdToPage` is that answer, filled in by registerPages() before anything
// renders: repo-relative source path → site-root-relative output URL.
//
// This used to be pattern matching on the href text — `./x.md` inside a
// cheatsheet became `x.html`, everything else became a GitHub URL. That handled
// the ./-prefixed sibling link the style guide asks for and nothing else, so the
// ~120 links written as a bare `design.md`, a cross-tree `../faq/java/faq_OOP.md`
// or a stale `kadane_algo.md` all shipped as dead ends. Resolving against a real
// map of built pages means a link is a local page exactly when the target is one.
const mdToPage = new Map();

function registerPage(srcPath, url) {
  mdToPage.set(path.posix.normalize(srcPath), url);
}

// Where a link's target sits relative to the page doing the linking. Cheatsheet
// siblings keep coming out as a bare "bst.html", which the zh pass relies on to
// spot a link it should point at the translated page instead.
function pageHref(outDir, url) {
  const rel = outDir ? path.posix.relative(outDir, url) : url;
  return rel || url;
}

const GITHUB_BLOB = 'https://github.com/yennanliu/CS_basics/blob/master/';

function resolveDocLink(href, srcDir, outDir) {
  const [target, hash = ''] = href.split(/(#.*)$/, 2);
  if (!target.endsWith('.md')) return null;
  const abs = path.posix.normalize(path.posix.join(srcDir, target));
  // A link that climbs out of the repo is not ours to rewrite.
  if (abs.startsWith('../')) return null;
  const page = mdToPage.get(abs);
  return page ? pageHref(outDir, page) + hash : GITHUB_BLOB + abs;
}

function processLinks(html, srcDir = '.', outDir = '') {
  // A hand-written absolute GitHub link to a cheatsheet is really an internal
  // link. It only becomes one if that sheet is actually built — otherwise it
  // stays a GitHub URL rather than turning into a page that does not exist,
  // which is how `kadane_algo.md` used to ship as a dead `kadane_algo.html`.
  html = html.replace(
    /https:\/\/github\.com\/yennanliu\/CS_basics\/blob\/master\/(doc\/[^")\s]+\.md)/g,
    (full, srcPath) => {
      const page = mdToPage.get(path.posix.normalize(srcPath));
      return page ? pageHref(outDir, page) : full;
    }
  );
  // GitHub blob image URLs → local paths (handles optional space before =)
  html = html.replace(
    /src\s*=\s*"https:\/\/github\.com\/yennanliu\/CS_basics\/blob\/master\/doc\/pic\/([^"]+)"/g,
    'src="doc/pic/$1"'
  );
  // Relative image paths (../pic/, ../../pic/, deeper) → doc/pic/
  // Nested FAQ files (e.g. doc/faq/java/) reference images as ../../pic/.
  html = html.replace(
    /src\s*=\s*"(?:\.\.\/)+pic\/([^"]+)"/g,
    'src="doc/pic/$1"'
  );
  // Every remaining relative link: a built page if the target is one, a GitHub
  // URL otherwise. Anything already absolute (http, mailto, #anchor) is skipped.
  html = html.replace(
    /href="([^"]+)"/g,
    (full, href) => {
      if (/^([a-z][a-z0-9+.-]*:|\/\/|#)/i.test(href)) return full;
      const resolved = resolveDocLink(href.replace(/^\.\//, ''), srcDir, outDir);
      if (resolved) return `href="${resolved}"`;
      // Not markdown — a source file, a directory, an asset. Those live in the
      // repo, not on this site, so they point at GitHub.
      if (href.endsWith('.html') || href.startsWith('doc/pic/')) return full;
      const abs = path.posix.normalize(path.posix.join(srcDir, href.replace(/^\.\//, '')));
      if (abs.startsWith('../')) return full;
      return `href="${GITHUB_BLOB}${abs}"`;
    }
  );
  return html;
}

// style.css hides the body's horizontal overflow, and `table { min-width: 400px }`
// keeps a table at least that wide. On a phone that combination does not merely
// squash a wide table — it clips it, with no way to scroll to the columns on the
// right. README alone renders 55 tables, several of them eight columns wide with
// a tag column full of company names.
//
// The `.table-wrap` scroll container this needs has been in style.css all along;
// nothing ever emitted it. That is what this does.
// Runs after wrapCodeBlocks, so a <table> shown *inside* a fenced example is
// already escaped to &lt;table&gt; and cannot match.
function wrapTables(html) {
  return html.replace(/<table\b[^>]*>/g, m => `<div class="table-wrap">${m}`)
             .replace(/<\/table>/g, '</table></div>');
}

// Doc pages carry diagrams that are megabytes each and usually far below the
// fold — binary_tree.html alone pulls 2.2 MB of them. Without this the browser
// blocks on every one before it can finish painting the text.
//
// The dimensions are read off the file so the space is reserved up front:
// lazy-loading images with no intrinsic size just trades a slow load for a page
// that jumps around as each one arrives.
function lazyLoadImages(html) {
  return html.replace(/<img\b([^>]*)>/gi, (full, attrs) => {
    if (/loading\s*=/i.test(attrs)) return full;
    const src = (attrs.match(/src\s*=\s*"([^"]+)"/) || [])[1];
    if (!src || /^(https?:|data:|\/\/)/i.test(src)) return full;
    const size = !/\b(width|height)\s*=/i.test(attrs) ? imageSize(src) : null;
    const dims = size ? ` width="${size.width}" height="${size.height}"` : '';
    return `<img${attrs} loading="lazy" decoding="async"${dims}>`;
  });
}

// Intrinsic pixel size straight out of the file header — a PNG's IHDR chunk or a
// JPEG's SOFn marker. Cheaper than a dependency for the two formats doc/pic uses,
// and a format it cannot read simply goes without the attributes.
const imageSizeCache = new Map();
function imageSize(src) {
  // Rendered hrefs are site-root-relative ("doc/pic/x.png"); the files are in
  // the repo at the same path under doc/.
  const file = path.join('.', src.replace(/^(\.\.\/)+/, ''));
  if (imageSizeCache.has(file)) return imageSizeCache.get(file);
  let result = null;
  try {
    const buf = fs.readFileSync(file);
    if (buf.length > 24 && buf.readUInt32BE(0) === 0x89504e47) {
      result = { width: buf.readUInt32BE(16), height: buf.readUInt32BE(20) };
    } else if (buf.length > 4 && buf[0] === 0xff && buf[1] === 0xd8) {
      let i = 2;
      while (i + 9 < buf.length) {
        if (buf[i] !== 0xff) { i++; continue; }
        const marker = buf[i + 1];
        // SOFn carries the frame dimensions; SOF4/8/12 are not frame headers.
        if (marker >= 0xc0 && marker <= 0xcf && ![0xc4, 0xc8, 0xcc].includes(marker)) {
          result = { height: buf.readUInt16BE(i + 5), width: buf.readUInt16BE(i + 7) };
          break;
        }
        i += 2 + buf.readUInt16BE(i + 2);
      }
    }
  } catch (_) { /* missing or unreadable — ship the img without dimensions */ }
  if (result && (!result.width || !result.height)) result = null;
  imageSizeCache.set(file, result);
  return result;
}

// `srcDir` is the directory of the markdown being rendered, `outDir` the
// directory of the page it becomes — both needed to turn a link written
// relative to the source into one that works from the output.
function renderContent(rawContent, srcDir = '.', outDir = '') {
  return lazyLoadImages(wrapTables(wrapCodeBlocks(processLinks(md.render(rawContent), srcDir, outDir))));
}


// Records accumulated across the build for the client-side global search index.
const searchRecords = [];


// ── Page registry ────────────────────────────────────────────────────────────
//
// Which markdown files become pages, decided once and up front, because a link
// in the very first document rendered can point at the very last one built.

const cheatsheetDir = 'doc/cheatsheet';
const faqDir = 'doc/faq';

// 00_template.md is the authoring skeleton and README.md the directory listing;
// neither is a page, so a link to one correctly falls through to GitHub.
const cheatsheetFiles = fs.existsSync(cheatsheetDir)
  ? fs.readdirSync(cheatsheetDir)
      .filter(f => f.endsWith('.md') && f !== 'README.md' && f !== '00_template.md')
      .sort()
  : [];

const faqFiles = fs.existsSync(faqDir) ? walkDir(faqDir).sort() : [];

// doc/faq/java/faq_OOP.md → faqs/java_faq_OOP.html: the subdirectory is folded
// into the filename because every FAQ page is written to one flat directory.
function faqPageName(filePath) {
  const relativePath = path.relative(faqDir, filePath);
  const baseName = path.basename(filePath, '.md');
  const subDir = path.dirname(relativePath);
  return subDir === '.' ? baseName : `${subDir}_${baseName}`.replace(/\//g, '_');
}

// README renders to problems.html; index.html is the hand-built landing page, so
// a link to README.md has to resolve to the problem index, not to the front door.
registerPage('README.md', 'problems.html');
if (fs.existsSync('doc/Resource.md')) registerPage('doc/Resource.md', 'resources.html');
if (fs.existsSync('doc/pattern_recognition.md')) registerPage('doc/pattern_recognition.md', 'patterns.html');
// Registered before README renders, so the `[card](doc/derivation_cards.md#…)`
// links in its Note column land on the local page rather than on GitHub.
if (fs.existsSync('doc/derivation_cards.md')) registerPage('doc/derivation_cards.md', 'derivation-cards.html');
for (const file of cheatsheetFiles) {
  registerPage(`${cheatsheetDir}/${file}`, `cheatsheets/${path.basename(file, '.md')}.html`);
}
for (const filePath of faqFiles) {
  registerPage(filePath, `faqs/${faqPageName(filePath)}.html`);
}

// ── Data collection ─────────────────────────────────────────────────────────

const readme = fs.readFileSync('README.md', 'utf8');
const content = renderContent(readme, '.', '');

let resourceContent = '';
if (fs.existsSync('doc/Resource.md')) {
  resourceContent = renderContent(fs.readFileSync('doc/Resource.md', 'utf8'), 'doc', '');
}

// ── Cheatsheets ──────────────────────────────────────────────────────────────

const cheatsheets = [];

// Category, FAANG-interview tier and title overrides live in one reviewable file
// rather than in keyword heuristics here — substring matching used to file
// difference_array under "arrays" and diff_toposort_quickunion under "sort".
const cheatsheetMeta = JSON.parse(fs.readFileSync('data/cheatsheet_meta.json', 'utf8'));


if (cheatsheetFiles.length > 0) {
  const files = cheatsheetFiles;

  const filePaths = files.map(f => path.join(cheatsheetDir, f));
  const lastModMap = buildLastModifiedMap(filePaths);

  // Fail the build rather than silently dumping a new cheatsheet into "Other".
  const unmapped = files.map(f => path.basename(f, '.md')).filter(b => !cheatsheetMeta.sheets[b]);
  if (unmapped.length) {
    throw new Error(
      `data/cheatsheet_meta.json is missing entries for: ${unmapped.join(', ')}\n` +
      'Add a { category, tier } entry for each (see the _comment field in that file).'
    );
  }
  const stale = Object.keys(cheatsheetMeta.sheets)
    .filter(b => !files.includes(`${b}.md`));
  if (stale.length) {
    throw new Error(`data/cheatsheet_meta.json lists sheets with no .md file: ${stale.join(', ')}`);
  }

  // The header contract from doc/cheatsheet/00_template.md. Registering a sheet
  // was already enforced; the H1 and the Scope line were not, so a new file could
  // ship a card with a filename title and no description.
  const headerProblems = [];
  for (const file of files) {
    const raw = fs.readFileSync(path.join(cheatsheetDir, file), 'utf8');
    const lines = raw.split('\n');
    if (!lines[0].startsWith('# ')) headerProblems.push(`${file}: first line must be the H1 ("# Topic Name")`);
    if (!lines.slice(0, 12).some(l => l.startsWith('> **Scope**'))) {
      headerProblems.push(`${file}: missing the "> **Scope** — …" line in its first 12 lines`);
    }
  }
  if (headerProblems.length) {
    throw new Error(
      'Cheatsheet header contract violated (see doc/cheatsheet/00_template.md):\n  ' +
      headerProblems.join('\n  ')
    );
  }

  for (const file of files) {
    const filePath = path.join(cheatsheetDir, file);
    const baseName = path.basename(file, '.md');
    const sheetMeta = cheatsheetMeta.sheets[baseName];
    const raw = fs.readFileSync(filePath, 'utf8');

    let htmlContent = renderContent(raw, cheatsheetDir, 'cheatsheets');
    htmlContent = ensureHeadingIds(htmlContent);
    // Captured before the H1 is split off, so the translated sheet — measured at
    // the same point — lines up heading-for-heading with it.
    const enHeadingIds = headingIds(htmlContent);
    const { title: h1Title, titleId, html: bodyHtml } = splitLeadingH1(htmlContent);
    const { html: annotated, hasPriority } = annotatePriorityHeadings(bodyHtml);
    htmlContent = annotated;

    // Preference order: explicit override → the file's own H1 → the filename.
    const title = sheetMeta.title || h1Title || titleCaseFromFile(baseName);
    const category = sheetMeta.category;
    const tier = sheetMeta.tier;
    const kind = sheetMeta.kind || 'sheet';
    const description = extractScope(raw);

    searchRecords.push({
      title,
      url: `cheatsheets/${baseName}.html`,
      category,
      type: 'Cheatsheet',
      // Tier travels with the record so search can rank must-know sheets first
      // and show the same stars the index does.
      tier,
      summary: description,
      headings: extractHeadings(htmlContent).slice(0, 40)
    });

    const kindNote = kind === 'stub'
      ? '<span class="kind-chip kind-stub">redirect</span>'
      : kind === 'reference'
        ? '<span class="kind-chip kind-reference">imported reference</span>'
        : '';

    cheatsheets.push({
      file: baseName,
      // The markdown this page was rendered from. It is how the 繁體中文 pass
      // pairs a page with its overlay, which lives at the same path under i18n/zh.
      src: filePath,
      title,
      category,
      tier,
      kind,
      description,
      enHeadingIds,
      content: buildPageContent({
        title,
        htmlContent,
        toc: generateTOC(htmlContent),
        lastMod: lastModMap.get(filePath) || null,
        indexHref: 'cheatsheets.html',
        indexLabel: 'Cheat Sheets',
        githubHref: `https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/${file}`,
        titleId,
        meta: `<span class="cat-chip">${category}</span>` +
          `<span class="tier-chip tier-${tier}">${prioBadge(tier)}` +
          `<span class="tier-label">${cheatsheetMeta.tierLabels[String(tier)].label}</span></span>` +
          kindNote,
        legend: hasPriority ? PRIORITY_LEGEND : ''
      })
    });
  }

  // Order pages by category, then by interview weight — this also drives the
  // prev/next links, which used to jump between unrelated topics alphabetically.
  const catRank = new Map(cheatsheetMeta.categoryOrder.map((c, i) => [c, i]));
  cheatsheets.sort((a, b) =>
    (catRank.get(a.category) - catRank.get(b.category)) ||
    (b.tier - a.tier) ||
    a.title.localeCompare(b.title)
  );
}

// ── Traditional Chinese docs ─────────────────────────────────────────────────
//
// There is one markdown tree per family, the English one. A translation is a
// sparse overlay of translated *sections* in i18n/zh, keyed by a hash of the
// English text, and the Chinese document is composed here: English structure,
// translated prose, the original code blocks. Nothing is stored twice, so
// nothing can drift — see site/i18n.js, which also owns the table of which
// trees are translated and where each one's overlay lives.
//
// A section with no entry falls back to English, so a half-translated document
// is a Chinese page with English gaps. Everything the index shows about a doc —
// category, tier, kind — is the English page's, never restated in the
// translation, so the two indexes can never disagree.
//
// A translation is optional. A doc without one simply gets no 中文 button, which
// is why the toggle can never link into a 404.

const ZH_LABELS = {
  home: '首頁',
  updated: '更新於',
  backTo: label => `返回${label}`,
  edit: '在 GitHub 上編輯'
};
const ZH_TOC_LABELS = { contents: '目錄', sections: n => `${n} 個章節` };

// A translation nobody can reach: its English document was renamed or deleted.
// Failing here is the point — the alternative is a store file quietly going
// unread until someone wonders why their Chinese page never changed.
const zhOrphans = orphanStores();
if (zhOrphans.length) {
  throw new Error(
    `i18n/zh has translations with no English document:\n  ${zhOrphans.join('\n  ')}\n` +
    'Every overlay must mirror the path of the markdown it translates ' +
    '(see the CORPORA table in site/i18n.js).'
  );
}

/**
 * Compose the 繁體中文 page for every doc of one family that has a translation.
 *
 * `pages` is that family's English page list, in the order it was built, so the
 * translated pages thread the same prev/next ladder. Each entry carries `file`
 * (its page name), `src` (the markdown it was rendered from) and `enHeadingIds`
 * (captured before its H1 was split off, so the two documents line up
 * heading-for-heading). The two families differ in nothing else, so the chips
 * under the title and the one-line description arrive as callbacks.
 */
function composeZhPages({ corpus, pages, outDir, indexHref, indexLabel, type, describe, meta }) {
  // Keyed on a normalised path: `docs()` builds its side with path.posix.join and a
  // page records whatever its family walked the tree with, so `./doc/faq/x.md` and
  // `doc/faq/x.md` are the same document and must not miss each other here. A miss
  // is silent — the page simply ships without its translation.
  const key = p => path.posix.normalize(p.split(path.sep).join('/'));
  const byEn = new Map(zhDocs(corpus).map(doc => [key(doc.en), doc]));
  const translated = pages
    .map(page => ({ page, doc: byEn.get(key(page.src)) }))
    .filter(({ doc }) => doc && fs.existsSync(doc.store));
  if (!translated.length) return [];

  // Known up front so a sibling link inside a translation (./bst.md) can resolve
  // to the translated page rather than bouncing the reader back into English.
  const names = new Set(translated.map(({ page }) => page.file));
  // A composed page changes when either side does, so it is dated by whichever
  // was touched last — the translation, or the English document under it.
  const lastMod = buildLastModifiedMap(translated.flatMap(({ doc }) => [doc.en, doc.store]));
  /** The later of two date strings, tolerating either being absent. */
  const laterOf = (a, b) => (a && b ? (new Date(a) >= new Date(b) ? a : b) : a || b || null);

  // Two passes, because a link inside one translation can point at a *section of
  // another one* — so every page has to be rendered before any of them can have
  // its anchors retargeted.
  const drafts = translated.map(({ page, doc }) => {
    const raw = compose(
      fs.readFileSync(doc.en, 'utf8'),
      parseStore(fs.readFileSync(doc.store, 'utf8'))
    );
    const html = ensureHeadingIds(renderContent(raw, path.dirname(doc.en), outDir));
    return { page, doc, raw, html, anchors: anchorMap(page.enHeadingIds, headingIds(html)) };
  });
  const anchorMaps = new Map(drafts.map(d => [d.page.file, d.anchors]));

  return drafts.map(({ page, doc, raw, html }) => {
    let htmlContent = html.replace(
      /href="([^"#]+)(\.html)(#[^"]*)?"/g,
      (full, name, ext, hash) => (names.has(name) ? `href="${name}.zh.html${hash || ''}"` : full)
    );
    // A hand-written `[見 §3](#two-pointers)` still names the *English* heading
    // slug, which does not exist on this page. Point it at the translated
    // heading in the same position — here, or in a sibling translation.
    htmlContent = retargetAnchors(htmlContent, linked => {
      if (!linked) return anchorMaps.get(page.file);
      const sibling = linked.match(/^([^/]+)\.zh\.html$/);
      return sibling ? anchorMaps.get(sibling[1]) : null;
    });
    const { title: h1Title, titleId, html: bodyHtml } = splitLeadingH1(htmlContent);
    const { html: annotated, hasPriority } = annotatePriorityHeadings(bodyHtml);
    htmlContent = annotated;
    const title = h1Title || page.title;
    const headings = extractHeadings(htmlContent);
    const description = describe(raw, headings) || page.description;

    return {
      ...page,
      title,
      description,
      // Kept so the caller can write the search record without re-deriving it.
      headings: headings.slice(0, 40),
      content: buildPageContent({
        title,
        htmlContent,
        toc: generateTOC(htmlContent, ZH_TOC_LABELS),
        lastMod: laterOf(lastMod.get(doc.store), lastMod.get(doc.en)),
        indexHref,
        indexLabel,
        githubHref: `https://github.com/yennanliu/CS_basics/blob/master/${doc.store}`,
        titleId,
        labels: ZH_LABELS,
        meta: meta(page),
        legend: hasPriority ? PRIORITY_LEGEND : ''
      })
    };
  });
}

const zhSheets = composeZhPages({
  corpus: 'cheatsheet',
  pages: cheatsheets,
  outDir: 'cheatsheets',
  indexHref: 'cheatsheets.zh.html',
  indexLabel: '速查表',
  describe: raw => extractScope(raw),
  meta: sheet => `<span class="cat-chip">${sheet.category}</span>` +
    `<span class="tier-chip tier-${sheet.tier}">${prioBadge(sheet.tier)}` +
    `<span class="tier-label">${cheatsheetMeta.tierLabels[String(sheet.tier)].label}</span></span>`
});

for (const sheet of zhSheets) {
  searchRecords.push({
    title: sheet.title,
    url: `cheatsheets/${sheet.file}.zh.html`,
    category: sheet.category,
    type: 'Cheatsheet (中文)',
    tier: sheet.tier,
    summary: sheet.description,
    headings: sheet.headings
  });
}

// ── FAQs ─────────────────────────────────────────────────────────────────────

const faqs = [];

function walkDir(dir) {
  const entries = fs.readdirSync(dir, { withFileTypes: true });
  const files = [];
  for (const entry of entries) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) files.push(...walkDir(fullPath));
    else if (entry.name.endsWith('.md')) files.push(fullPath);
  }
  return files;
}

if (faqFiles.length > 0) {
  const lastModMap = buildLastModifiedMap(faqFiles);

  const faqCategoryMap = {
    'java': 'Java', 'backend': 'Backend', 'db': 'Database',
    'redis': 'Redis', 'kafka': 'Kafka', 'spark': 'Spark & Hadoop',
    'flink': 'Flink', 'stream': 'Streaming', 'sql': 'SQL'
  };

  for (const filePath of faqFiles) {
    const relativePath = path.relative(faqDir, filePath);
    const baseName = path.basename(filePath, '.md');
    const subDir = path.dirname(relativePath);
    // Same name the page registry recorded, so the two cannot drift apart.
    const uniqueName = faqPageName(filePath);
    const title = baseName.replace(/_/g, ' ').split(' ')
      .map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');

    let category = 'General';
    if (subDir !== '.') {
      const topDir = subDir.split('/')[0];
      category = faqCategoryMap[topDir] || topDir.charAt(0).toUpperCase() + topDir.slice(1);
    }

    const raw = fs.readFileSync(filePath, 'utf8');
    let htmlContent = renderContent(raw, path.dirname(filePath), 'faqs');
    htmlContent = ensureHeadingIds(htmlContent);
    // Captured before the H1 is split off, so the translated page — measured at
    // the same point — lines up heading-for-heading with it.
    const enHeadingIds = headingIds(htmlContent);
    const { title: h1Title, titleId, html: bodyHtml } = splitLeadingH1(htmlContent);
    const { html: annotated, hasPriority } = annotatePriorityHeadings(bodyHtml);
    htmlContent = annotated;
    const pageTitle = h1Title || title;
    const docHeadings = extractHeadings(htmlContent);
    // summariseDoc falls back in order: the Scope line if the FAQ has one (17
    // of 49 do), else the lead paragraph, else the sections the doc covers.
    // The earlier comment here claimed FAQs never carry a Scope line, which is
    // what led a skill to tell authors to drop it.
    const description = summariseDoc(raw, docHeadings);

    searchRecords.push({
      title: pageTitle,
      url: `faqs/${uniqueName}.html`,
      category,
      type: 'FAQ',
      headings: docHeadings.slice(0, 40)
    });

    faqs.push({
      file: uniqueName,
      src: filePath,
      enHeadingIds,
      // The card used to show a filename-derived title ("Jvm") while the page
      // showed the H1 ("JVM FAQ"). One title, from the file itself.
      title: pageTitle,
      category,
      description,
      content: buildPageContent({
        title: pageTitle,
        htmlContent,
        toc: generateTOC(htmlContent),
        lastMod: lastModMap.get(filePath) || null,
        indexHref: 'faqs.html',
        indexLabel: 'FAQs',
        githubHref: `https://github.com/yennanliu/CS_basics/blob/master/${filePath}`,
        titleId,
        meta: `<span class="cat-chip">${category}</span>`,
        legend: hasPriority ? PRIORITY_LEGEND : ''
      })
    });
  }
}

// ── Traditional Chinese FAQs ─────────────────────────────────────────────────
//
// The same overlay and the same composer as the cheatsheets. An FAQ differs only
// in usually having no Scope line, so its card description falls back to the
// *composed* Chinese rather than lifted out of a line the author wrote.

// The FAQ index's own words, and the categories build-site.js derives from the
// directory names. Only the ones that are English are translated: Java, Redis,
// Kafka and Flink are product names in both languages.
const FAQ_ZH = {
  h1: 'FAQ - 常見面試問答',
  intro: '涵蓋 Java、後端、資料庫、串流系統等主題的面試問答整理。',
  tip: '<strong>💡 提示：</strong>這些問答是為技術面試準備時的快速查閱而寫的。',
  source: '在 <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/faq">GitHub</a> 上瀏覽全部 FAQ。',
  categories: {
    General: '綜合', Java: 'Java', Backend: '後端', Database: '資料庫', SQL: 'SQL',
    Redis: 'Redis', Kafka: 'Kafka', 'Spark & Hadoop': 'Spark 與 Hadoop',
    Flink: 'Flink', Streaming: '串流處理'
  }
};

/** An FAQ category's Chinese name, falling back to the English product name. */
const zhFaqCategory = category => FAQ_ZH.categories[category] || category;

const zhFaqs = composeZhPages({
  corpus: 'faq',
  pages: faqs,
  outDir: 'faqs',
  indexHref: 'faqs.zh.html',
  indexLabel: '常見問答',
  describe: (raw, headings) => summariseDoc(raw, headings),
  meta: faq => `<span class="cat-chip">${zhFaqCategory(faq.category)}</span>`
});

for (const faq of zhFaqs) {
  searchRecords.push({
    title: faq.title,
    url: `faqs/${faq.file}.zh.html`,
    category: faq.category,
    type: 'FAQ (中文)',
    headings: faq.headings
  });
}

// ── HTML template ─────────────────────────────────────────────────────────────

// Where the site is served from. Needed for the absolute URLs that canonical,
// hreflang, Open Graph and the sitemap all require — a relative one is not valid
// in any of them.
const SITE_ORIGIN = 'https://yennanliu.github.io/CS_basics/';

const DEFAULT_DESCRIPTION =
  'Computer Science fundamentals: algorithms, data structures, system design, and LeetCode solutions';

function escAttr(value) {
  return String(value)
    .replace(/&/g, '&amp;').replace(/</g, '&lt;')
    .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

// A title reaches htmlTemplate already entity-escaped when it came from rendered
// markdown ("Hashing &amp; Counting") and raw when a caller passed a literal
// ("Problem Index"). Decoding first makes escAttr idempotent over both, which is
// what stops og:title shipping "Hashing &amp;amp; Counting".
function unescAttr(value) {
  // Null-safe: a page that passes no description must reach metaDescription as
  // "" and get the default, not the string "undefined".
  if (value === null || value === undefined) return '';
  return String(value)
    .replace(/&lt;/g, '<').replace(/&gt;/g, '>')
    .replace(/&quot;/g, '"').replace(/&#0?39;|&apos;/g, "'")
    .replace(/&amp;/g, '&');
}

// A description is a sentence about *this* page or it is noise. Every page family
// already computes one for its index card — the Scope line for a cheatsheet, the
// lead paragraph for an FAQ — it just never reached the <head>, so all 352 pages
// shipped the same generic sentence and told a search engine nothing.
function metaDescription(text) {
  const clean = String(text || '').replace(/\s+/g, ' ').trim();
  if (!clean) return DEFAULT_DESCRIPTION;
  return clean.length > 300 ? clean.slice(0, 297).replace(/\s+\S*$/, '') + '…' : clean;
}

// `opts.lang` / `opts.langAlt` mark a page that exists in two languages; nav.js
// turns them into the 中文/EN button. Pages without a translation pass neither
// and render exactly the markup they did before.
//
// `opts.url` is the page's own path from the site root. It drives the canonical
// link, the hreflang pair and the sitemap entry, so a page that omits it is
// simply left out of all three rather than claiming a wrong address.
const htmlTemplate = (title, bodyContent, currentPage = 'home', basePath = '', opts = {}) => {
  const url = opts.url || '';
  const absolute = url ? SITE_ORIGIN + url : '';
  // Decode before truncating, so a 300-char cut can never land inside an entity.
  const description = metaDescription(unescAttr(opts.description));
  const pageTitle = unescAttr(title);

  // The zh and en sheets are translations of each other, not duplicates. Saying
  // so — in both directions, plus x-default — is what stops a crawler picking
  // one and dropping the other.
  const alternates = url && opts.langAlt
    ? [
        `\n  <link rel="alternate" hreflang="${opts.lang === 'zh' ? 'zh-Hant' : 'en'}" href="${SITE_ORIGIN}${url}">`,
        `\n  <link rel="alternate" hreflang="${opts.lang === 'zh' ? 'en' : 'zh-Hant'}" href="${SITE_ORIGIN}${path.posix.join(path.posix.dirname(url), opts.langAlt)}">`,
        `\n  <link rel="alternate" hreflang="x-default" href="${SITE_ORIGIN}${opts.lang === 'zh' ? path.posix.join(path.posix.dirname(url), opts.langAlt) : url}">`
      ].join('')
    : '';

  return `
<!DOCTYPE html>
<html lang="${opts.lang === 'zh' ? 'zh-Hant' : 'en'}" data-theme="dark">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">
  <meta name="theme-color" content="#000000">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="mobile-web-app-capable" content="yes">
  <title>${escAttr(pageTitle)} — CS_basics</title>
  <meta name="description" content="${escAttr(description)}">
  <link rel="canonical" href="${absolute || SITE_ORIGIN}">${alternates}
  <meta property="og:type" content="article">
  <meta property="og:site_name" content="CS_basics">
  <meta property="og:title" content="${escAttr(pageTitle)}">
  <meta property="og:description" content="${escAttr(description)}">
  <meta property="og:url" content="${absolute || SITE_ORIGIN}">
  <meta property="og:locale" content="${opts.lang === 'zh' ? 'zh_TW' : 'en_US'}">
  <meta name="twitter:card" content="summary">
  <meta name="twitter:title" content="${escAttr(pageTitle)}">
  <meta name="twitter:description" content="${escAttr(description)}">
  <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90' fill='white'>$</text></svg>">
  <link rel="stylesheet" href="${basePath}nav.css">
  <link rel="stylesheet" href="${basePath}style.css">
  <link rel="stylesheet" href="${basePath}vendor/highlight/atom-one-dark.min.css">
  <!-- Blocking on purpose: nav.js restores the stored theme before first paint. -->
  <script src="${basePath}nav.js"></script>
  <script src="${basePath}site.js"></script>
</head>
<body>
  <div class="progress-container"><div class="progress-bar" id="reading-progress"></div></div>
  <div id="site-nav" data-page="${currentPage}" data-base="${basePath}"${
    opts.langAlt ? ` data-lang="${opts.lang || 'en'}" data-lang-alt="${opts.langAlt}"` : ''
  }></div>
  <script>CSNav.mount();</script>

  <main class="container">
    <div class="content">
      ${bodyContent}
    </div>
  </main>

  <footer>
    <div class="container">
      <p>CS_basics — computer science fundamentals &amp; interview preparation</p>
      <p>
        <a href="https://github.com/yennanliu/CS_basics">github</a> |
        <a href="https://github.com/yennanliu/CS_basics/tree/master/doc">docs</a> |
        <a href="https://github.com/yennanliu/CS_basics/issues">issues</a>
      </p>
    </div>
  </footer>
</body>
</html>
`;
};

// ── Landing page ─────────────────────────────────────────────────────────────
//
// index.html used to be README rendered straight through: a 915 KB page of 55
// tables and 1,512 problem rows, which is the right shape for a repository
// listing and the wrong one for a front door. It showed a first-time visitor a
// wall of LeetCode numbers and no sign that the site has a search, a roadmap, a
// spaced-repetition plan or 37 algorithm visualizers.
//
// So the README keeps its page — it is the problem index, and it is genuinely
// useful — but at problems.html, with a landing page in front of it.

const { parseReadmeProblems } = require('./build-roadmap');
const readmeProblems = parseReadmeProblems(readme);

// README's last column is a hand-kept verdict — "OK******* (7)", "AGAIN**** (3)".
// parseReadmeProblems does not carry it (the roadmap has no use for it), so it is
// read here: "still marked AGAIN" is the one number on this page worth acting on.
function readmeStatusCounts(markdown) {
  const counts = { ok: 0, again: 0, todo: 0 };
  for (const line of markdown.split('\n')) {
    if (!line.startsWith('|')) continue;
    const cells = line.split('|').map(c => c.trim());
    // A data row is "| # | Title | … | Status |", so cells[1] is the number.
    if (cells.length < 4 || !/^\d+$/.test(cells[1])) continue;
    const status = cells[cells.length - 2].toUpperCase();
    if (status.includes('AGAIN')) counts.again++;
    else if (status.includes('OK')) counts.ok++;
    else if (status) counts.todo++;
  }
  return counts;
}
const statusCounts = readmeStatusCounts(readme);

// Counted, never typed: a hardcoded "1,300+" is a number that goes stale the
// first week nobody remembers it is there.
const stats = [
  [readmeProblems.size.toLocaleString('en-US'), 'LeetCode problems indexed'],
  [cheatsheets.length, 'cheatsheets'],
  [faqs.length, 'interview FAQs'],
  [fs.existsSync('algo_demo')
    ? fs.readdirSync('algo_demo').filter(f => f.endsWith('.html') && f !== 'index.html').length
    : 0, 'algorithm visualizers']
];

// Counts inside a blurb come from the same files the pages themselves are built
// from. A sentence saying "29 topics" is a sentence that will be wrong the next
// time someone adds one.
const countIn = (file, pick) => {
  try { return pick(JSON.parse(fs.readFileSync(file, 'utf8'))); } catch (_) { return null; }
};
const roadmapTopics = countIn('data/roadmap.json', d => (d.nodes || []).length);
const quizQuestions = countIn('data/complexity_quiz.json', d => (d.questions || []).length);
const visualizerCount = stats[3][0];

// The pitch for each tool is what it does for you, not what it is. "Explore
// problems by tag" beats "LC Explorer" to someone who has never seen either.
//
// Grouped rather than listed. Eleven cards in one grid is a menu you read all
// of before choosing any of it; the three groups below answer the question a
// first visit actually arrives with — am I here to learn something, to practise
// it, or to look something up?
const ENTRY_GROUPS = [
  ['Learn a pattern', 'Ordered so that one thing leads to the next, rather than by problem number.', [
    ['lc-roadmap.html', 'Study roadmap',
     `A dependency-ordered path through ${roadmapTopics ? `${roadmapTopics} topics` : 'the topics'} — what to learn next, and what it needs first.`],
    ['cheatsheets.html', 'Cheat sheets',
     'Every pattern, with templates in Java and Python, ranked by how often interviews ask for it.'],
    ['patterns.html', 'Pattern recognition',
     'Read a problem statement, name the technique. The keyword-to-pattern table.'],
    ['algo_demo/index.html', 'Visualizers',
     `Step through Dijkstra, KMP, knapsack and ${visualizerCount - 3} more, one frame at a time.`]
  ]],
  ['Practise it', 'Pick something to solve, then keep what you solved from going stale.', [
    ['lc-explorer.html', 'Problem explorer',
     'All indexed problems, filtered by tag, difficulty and acceptance rate, linked to the solutions here.'],
    ['lc-review-plan.html', 'Review plan',
     'Spaced repetition over the practice log — what is overdue, what keeps coming back, and a session picked for you.'],
    ['lc-complexity-quiz.html', 'Complexity quiz',
     `Read a snippet, name its time and space.${quizQuestions ? ` ${quizQuestions} questions,` : ''} each with the trap it sets.`],
    ['lc-random-picker.html', 'Random picker',
     'One problem, drawn from the list and difficulty you choose — for when choosing is the thing stopping you.']
  ]],
  ['Look something up', 'The index the rest of it is built from.', [
    ['problems.html', 'Problem index',
     'The full README table — every problem, its solutions, its tags and its status.'],
    ['lc-similar.html', 'Similar problems',
     'The graph of which problems share a technique, so a solved one points at its siblings.'],
    ['search.html', 'Search',
     'Every cheatsheet, FAQ and problem note, full-text. Press / from anywhere on the site.'],
    ['resources.html', 'Resources',
     'The books, courses and references the notes here were built from.']
  ]]
];

// ── Agent skills ─────────────────────────────────────────────────────────────
//
// The repo ships agent skills as well as notes, and nothing on the front page
// said so — the coach was one card reading "Interview coach" in a grid of
// eleven, which is indistinguishable from another web page of the site. They
// are a different kind of thing: markdown you install into your own agent, and
// they work on your code rather than on this site's.
const skillCount = fs.existsSync('.claude/skills')
  ? fs.readdirSync('.claude/skills', { withFileTypes: true }).filter(e => e.isDirectory()).length
  : 0;

// Ten cards in one flat grid is a list, not a band — so they are declared as
// three labelled groups, the same way ENTRY_GROUPS splits the main cards. The
// split is by what the skill touches: your solutions, your practice record, or
// this site.
const AGENT_SKILLS = [
  ['File what you solved', [
    ['lc-python.html', '/lc-python', 'File a Python solution',
     'Turns a solved problem into a committed one — the problem\'s real slug, the house file layout, ' +
     'a smoke test against the docstring\'s own examples, and the README row inserted in number order.'],
    ['lc-java.html', '/lc-java', 'File a Java solution',
     'The Java counterpart — the package its pattern owns, markers that match the method names, ' +
     'a compile and a run, and the [Java] link added to the row the problem already has.'],
    ['lc-cheatsheet.html', '/lc-cheatsheet', 'Update a cheatsheet',
     'Files what a problem taught you into the sheet that owns the topic — as an example, a variation ' +
     'on a template, a new pattern or a new sheet — then follows the anchors and translations it moved.']
  ]],
  ['Track the practice', [
    ['skills.html', '/lc-coach', 'Interview coach',
     'Scores a solution the way an interviewer does — the six-point verdict (SH/H/LH/LNH/NH/SNH), ' +
     'the debrief packet from their side of the table, the one line that sets the complexity, and what to drill next.'],
    ['lc-log.html', '/lc-log', 'Log the session',
     'Appends today to the practice log in the shape the review plan\'s parser actually reads, ' +
     'so no problem number is silently dropped and the annotations survive.'],
    ['lc-again.html', '/lc-again', 'Graduate an AGAIN',
     'Moves the README status cell after a re-solve — promoting only what was genuinely re-derived ' +
     'unaided, and keeping the star run that records what the problem cost.'],
    ['l3-core.html', '/l3-core', 'The L3 core set',
     'Blind 75 plus the NeetCode 150 problems marked MUST, held fixed so the number means something — ' +
     'where each one stands in the practice log, the ok share, and the next five to drill.']
  ]],
  ['Maintain the site', [
    ['lc-algo-demo.html', '/lc-algo-demo', 'Add a visualizer',
     'Writes the 37th visualizer against the contract the other 36 obey — the shared palette, the ' +
     'canvas wrapper, the structured step trace, and the repaint a theme switch depends on.'],
    ['lc-site-data.html', '/lc-site-data', 'Roadmap &amp; quiz data',
     'Adds a roadmap topic or a quiz question so the build-time validators accept it first time — ' +
     'no implied edge, no duplicate id, and answers that parse as complexity expressions.'],
    ['lc-zh-translate.html', '/lc-zh-translate', 'Translate to 中文',
     'Works the 繁體中文 overlay — prose only, keyed per section, every code fence stored once and ' +
     'spliced back at compose time.'],
    ['lc-faq-add.html', '/lc-faq-add', 'File an FAQ answer',
     'Files an interview question into the FAQ its Scope line owns, under the section it belongs to — ' +
     'and writes the Chinese half in the same change.']
  ]]
];

const landingContent = `
  <div class="hero">
    <h1>CS_basics</h1>
    <p class="hero-lede">Algorithms, data structures and system design, worked through in Java, Python and SQL — the notes and solutions behind one engineer's interview preparation.</p>
    <div class="hero-actions">
      <a class="hero-btn hero-btn-primary" href="lc-roadmap.html">Start with the roadmap</a>
      <a class="hero-btn" href="lc-review-plan.html">What to review today</a>
      <a class="hero-btn" href="search.html">Search everything</a>
    </div>
  </div>

  <div class="stat-strip">
    ${stats.map(([n, label]) =>
      `<div class="stat-cell"><span class="stat-n">${n}</span><span class="stat-l">${label}</span></div>`
    ).join('')}
  </div>

  ${ENTRY_GROUPS.map(([heading, note, cards]) => `
  <section class="entry-section">
    <h2>${heading}</h2>
    <p class="section-note">${note}</p>
    <div class="entry-grid">
      ${cards.map(([href, title, blurb]) => `
      <a class="entry-card" href="${href}">
        <span class="entry-title">${title}</span>
        <span class="entry-blurb">${blurb}</span>
      </a>`).join('')}
    </div>
  </section>`).join('')}

  <section class="skills-band">
    <h2>Agent skills</h2>
    <p class="section-note">
      The repo ships ${skillCount ? `${skillCount} skills for coding agents` : 'skills for coding agents'} under
      <code>.claude/skills/</code> — plain markdown, no dependencies, installed into your own agent rather than run here.
      These have pages of their own.
    </p>
    ${AGENT_SKILLS.map(([label, cards]) => `
    <h3 class="skill-group">${label}</h3>
    <div class="skill-grid">
      ${cards.map(([href, command, title, blurb]) => `
      <a class="skill-card" href="${href}">
        <span class="skill-cmd">${command}</span>
        <span class="skill-title">${title}</span>
        <span class="skill-blurb">${blurb}</span>
      </a>`).join('')}
    </div>`).join('')}
    <p class="section-note skill-install">Install one into Claude Code, Codex, Gemini or any agent that reads a markdown instruction file:</p>
    <pre class="skill-install-code"><code>git clone --depth 1 https://github.com/yennanliu/CS_basics.git /tmp/cs_basics
cp -r /tmp/cs_basics/.claude/skills/lc-coach ~/.claude/skills/</code></pre>
    <p class="section-note">
      Same idea without an agent: <a href="suggest-review.html">suggest-review</a> is a standard-library Python
      script that measures which topics the practice is quietly skipping, then spends its picks on the ones that are owed them.
    </p>
  </section>

  <h2>Complexity, at a glance</h2>
  <p class="section-note">The reference charts, kept on the front page because they are the thing most often looked up. Source: <a href="https://www.bigocheatsheet.com/">bigocheatsheet.com</a>.</p>
  <div class="ref-figures">
    ${[
      ['bigO_complexity_chart.png', 'Big-O complexity chart: operation count against input size for constant, logarithmic, linear, linearithmic, quadratic and exponential growth.'],
      ['common_ds_op_cost.png', 'Table of average and worst-case time complexity for access, search, insertion and deletion across the common data structures.'],
      ['sort_algorithm_complexity.png', 'Table of best, average and worst-case time and space complexity for the common sorting algorithms.']
    ]
      .filter(([f]) => fs.existsSync(path.join('doc/pic', f)))
      .map(([f, alt]) => {
        const size = imageSize(`doc/pic/${f}`);
        const dims = size ? ` width="${size.width}" height="${size.height}"` : '';
        return `<figure><img src="doc/pic/${f}" alt="${escAttr(alt)}" loading="lazy" decoding="async"${dims}></figure>`;
      }).join('')}
  </div>

  <p class="section-note">
    ${statusCounts.ok + statusCounts.again > 0
      ? `Of the problems attempted so far, ${statusCounts.ok.toLocaleString('en-US')} are marked <strong>OK</strong> and ${statusCounts.again.toLocaleString('en-US')} are still marked <strong>AGAIN</strong> — the <a href="lc-review-plan.html">review plan</a> schedules the second group. `
      : ''}Everything here is built from the markdown in
    <a href="https://github.com/yennanliu/CS_basics">the repository</a> — corrections welcome.
  </p>
`;

fs.writeFileSync('_site/index.html', htmlTemplate('Home', landingContent, 'home', '', {
  url: 'index.html',
  description: `Algorithms, data structures, system design and ${readmeProblems.size} LeetCode solutions in Java, Python and SQL — with ${cheatsheets.length} cheatsheets, a study roadmap and a spaced-repetition review plan.`
}));
console.log(`✓ Created index.html (landing page, ${readmeProblems.size} problems indexed)`);

fs.writeFileSync('_site/problems.html', htmlTemplate('Problem Index', content, 'problems', '', {
  url: 'problems.html',
  description: `All ${readmeProblems.size} LeetCode problems in this repo, by topic, with links to the Java, Python and SQL solutions and the tags each one carries.`
}));
console.log('✓ Created problems.html (the README index)');

if (resourceContent) {
  fs.writeFileSync('_site/resources.html', htmlTemplate('Resources', resourceContent, 'resources', '', {
    url: 'resources.html',
    description: 'Books, courses, problem lists and reference sites collected while preparing for algorithm and system design interviews.'
  }));
  console.log('✓ Created resources.html');
}

// The two indexes are each other's counterpart: the 中文/EN button in the navbar
// swaps between them, which is also the only way into the translations from the
// rest of the site. Built only when translations exist, so a repo with none is
// byte-identical to before.
const bilingualIndex = zhSheets.length > 0;

fs.writeFileSync('_site/cheatsheets.html', htmlTemplate(
  'Cheat Sheets', buildCheatsheetIndex(cheatsheets, cheatsheetMeta), 'cheatsheets', '',
  Object.assign({
    url: 'cheatsheets.html',
    description: `${cheatsheets.length} cheatsheets covering every interview algorithm pattern, ranked by how often it comes up.`
  }, bilingualIndex ? { lang: 'en', langAlt: 'cheatsheets.zh.html' } : {})
));
console.log('✓ Created cheatsheets.html index');

if (bilingualIndex) {
  fs.writeFileSync('_site/cheatsheets.zh.html', htmlTemplate(
    '速查表', buildCheatsheetIndex(zhSheets, cheatsheetMeta, 'zh'), 'cheatsheets', '',
    { lang: 'zh', langAlt: 'cheatsheets.html', url: 'cheatsheets.zh.html',
      description: `${zhSheets.length} 份演算法面試速查表的繁體中文版，依考題出現頻率排序。` }
  ));
  console.log(`✓ Created cheatsheets.zh.html index (${zhSheets.length} translated sheets)`);
}

const translated = new Set(zhSheets.map(s => s.file));

if (cheatsheets.length > 0) {
  fs.mkdirSync('_site/cheatsheets', { recursive: true });
  cheatsheets.forEach((sheet, idx) => {
    let fixedContent = sheet.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    fixedContent += buildPrevNext(cheatsheets, idx);
    fs.writeFileSync(`_site/cheatsheets/${sheet.file}.html`, htmlTemplate(
      sheet.title, fixedContent, 'cheatsheets', '../',
      Object.assign({
        url: `cheatsheets/${sheet.file}.html`,
        description: sheet.description
      }, translated.has(sheet.file) ? { lang: 'en', langAlt: `${sheet.file}.zh.html` } : {})
    ));
  });
  console.log(`✓ Created ${cheatsheets.length} individual cheatsheet pages`);
}

if (zhSheets.length > 0) {
  // prev/next has to thread the .zh pages, so it gets a list whose `file` carries
  // the suffix — the sheets themselves stay keyed by the bare slug so the index
  // and the toggle can pair the two languages up.
  const zhPages = zhSheets.map(s => ({ ...s, file: `${s.file}.zh` }));
  zhPages.forEach((sheet, idx) => {
    let fixedContent = sheet.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    fixedContent += buildPrevNext(zhPages, idx);
    fs.writeFileSync(`_site/cheatsheets/${sheet.file}.html`, htmlTemplate(
      sheet.title, fixedContent, 'cheatsheets', '../',
      { lang: 'zh', langAlt: `${zhSheets[idx].file}.html`,
        url: `cheatsheets/${sheet.file}.html`, description: sheet.description }
    ));
  });
  console.log(`✓ Created ${zhSheets.length} 繁體中文 cheatsheet pages`);
}

const knownFaqCategoryOrder = ['General', 'Java', 'Backend', 'Database', 'SQL', 'Redis', 'Kafka', 'Spark & Hadoop', 'Flink', 'Streaming'];
const faqGrouped = groupByCategory(faqs);
const faqCategoryOrder = [
  ...knownFaqCategoryOrder,
  ...Object.keys(faqGrouped).filter(cat => !knownFaqCategoryOrder.includes(cat))
];

/** The tip panel the index closes with, in whichever language the index is in. */
const faqIndexFoot = t =>
  `\n<div style="margin-top: 3rem; padding: 1.5rem; background: var(--bg-secondary); border-radius: 8px;">
  <p>${t.tip}</p>
  <p>${t.source}</p>
</div>`;

const faqIndexContent = '<h1>FAQ - Frequently Asked Questions</h1>' +
  '<p class="intro">Interview preparation FAQs covering Java, Backend, Database, Streaming, and more.</p>' +
  buildIndexGrid(faqGrouped, faqCategoryOrder, 'faqs') +
  faqIndexFoot({
    tip: '<strong>💡 Tip:</strong> These FAQs are designed for quick reference during technical interview preparation.',
    source: 'View all FAQs on <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/faq">GitHub</a>.'
  });

// Built only when translations exist, so a repo with none ships exactly the page
// it did before — the same rule the cheatsheet index follows.
const bilingualFaqIndex = zhFaqs.length > 0;

fs.writeFileSync('_site/faqs.html', htmlTemplate('FAQs', faqIndexContent, 'faqs', '', Object.assign({
  url: 'faqs.html',
  description: `${faqs.length} interview FAQs on Java, backend, databases, SQL and streaming systems.`
}, bilingualFaqIndex ? { lang: 'en', langAlt: 'faqs.zh.html' } : {})));
console.log('✓ Created faqs.html index');

if (bilingualFaqIndex) {
  const zhFaqGrouped = groupByCategory(zhFaqs);
  const zhFaqIndexContent = `<h1>${FAQ_ZH.h1}</h1>` +
    `<p class="intro">${FAQ_ZH.intro}</p>` +
    buildIndexGrid(zhFaqGrouped, faqCategoryOrder, 'faqs', { lang: 'zh', catName: zhFaqCategory }) +
    faqIndexFoot(FAQ_ZH);
  fs.writeFileSync('_site/faqs.zh.html', htmlTemplate(FAQ_ZH.h1, zhFaqIndexContent, 'faqs', '', {
    lang: 'zh', langAlt: 'faqs.html', url: 'faqs.zh.html',
    description: `${zhFaqs.length} 篇技術面試問答的繁體中文版，涵蓋 Java、後端、資料庫與串流系統。`
  }));
  console.log(`✓ Created faqs.zh.html index (${zhFaqs.length} translated FAQs)`);
}

const translatedFaqs = new Set(zhFaqs.map(f => f.file));

if (faqs.length > 0) {
  fs.mkdirSync('_site/faqs', { recursive: true });
  faqs.forEach((faq, idx) => {
    let fixedContent = faq.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    fixedContent += buildPrevNext(faqs, idx);
    fs.writeFileSync(`_site/faqs/${faq.file}.html`, htmlTemplate(faq.title, fixedContent, 'faqs', '../', Object.assign({
      url: `faqs/${faq.file}.html`, description: faq.description
    }, translatedFaqs.has(faq.file) ? { lang: 'en', langAlt: `${faq.file}.zh.html` } : {})));
  });
  console.log(`✓ Created ${faqs.length} individual FAQ pages`);
}

if (zhFaqs.length > 0) {
  // prev/next has to thread the .zh pages, so it gets a list whose `file` carries
  // the suffix — the FAQs themselves stay keyed by the bare page name so the index
  // and the toggle can pair the two languages up.
  const zhPages = zhFaqs.map(f => ({ ...f, file: `${f.file}.zh` }));
  zhPages.forEach((faq, idx) => {
    let fixedContent = faq.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    fixedContent += buildPrevNext(zhPages, idx);
    fs.writeFileSync(`_site/faqs/${faq.file}.html`, htmlTemplate(faq.title, fixedContent, 'faqs', '../', {
      lang: 'zh', langAlt: `${zhFaqs[idx].file}.html`,
      url: `faqs/${faq.file}.html`, description: faq.description
    }));
  });
  console.log(`✓ Created ${zhFaqs.length} 繁體中文 FAQ pages`);
}

if (fs.existsSync('doc/pattern_recognition.md')) {
  let patternHtml = renderContent(fs.readFileSync('doc/pattern_recognition.md', 'utf8'), 'doc', '');
  patternHtml = ensureHeadingIds(patternHtml);
  const patternContent = `
    <div class="cheatsheet-header">
      <h1>Pattern Recognition Guide</h1>
      <p>Map problem keywords to algorithm patterns — the fastest way to crack coding interviews.</p>
    </div>
    ${generateTOC(patternHtml)}
    <div class="cheatsheet-content">${patternHtml}</div>
  `;
  fs.writeFileSync('_site/patterns.html', htmlTemplate('Pattern Recognition', patternContent, 'patterns', '', {
    url: 'patterns.html',
    description: 'Map a problem statement to the algorithm pattern it wants — the keyword-to-technique table, with the LeetCode problems that drill each one.'
  }));
  console.log('✓ Created patterns.html');

  searchRecords.push({
    title: 'Pattern Recognition Guide',
    url: 'patterns.html',
    category: 'Guide',
    type: 'Guide',
    headings: extractHeadings(patternHtml).slice(0, 60)
  });
}

// One card per chronic problem — the invariant, the line that sets the
// complexity, the edges — for the forty problems the log and README's pass
// counts agree cost the most. README's Note column links each row to its card,
// so this is built as a page and not left as a GitHub-only markdown file.
if (fs.existsSync('doc/derivation_cards.md')) {
  let cardsHtml = renderContent(fs.readFileSync('doc/derivation_cards.md', 'utf8'), 'doc', '');
  cardsHtml = ensureHeadingIds(cardsHtml);
  const { html: cardsBody } = splitLeadingH1(cardsHtml);
  const cardsContent = `
    <div class="cheatsheet-header">
      <h1>Derivation Cards</h1>
      <p>Forty chronic problems, one card each: the invariant, the one line that sets the complexity, the edges that keep failing — reviewed instead of re-solved.</p>
    </div>
    ${generateTOC(cardsBody)}
    <div class="cheatsheet-content">${cardsBody}</div>
  `;
  fs.writeFileSync('_site/derivation-cards.html', htmlTemplate('Derivation Cards', cardsContent, 'cards', '', {
    url: 'derivation-cards.html',
    description: 'One derivation card per chronic LeetCode problem — the invariant, the contract of the recursion or loop state, the line that sets the complexity, and the edges — for the forty problems this repo has re-solved the most.'
  }));
  console.log('✓ Created derivation-cards.html');

  searchRecords.push({
    title: 'Derivation Cards',
    url: 'derivation-cards.html',
    category: 'Guide',
    type: 'Guide',
    headings: extractHeadings(cardsBody).slice(0, 60)
  });
}

// ── Global search index + search page ─────────────────────────────────────────

fs.mkdirSync('_site/data', { recursive: true });
fs.writeFileSync('_site/data/search-index.json', JSON.stringify({ records: searchRecords }));
console.log(`✓ Created data/search-index.json (${searchRecords.length} doc records)`);

const searchBody = `
  <div class="cheatsheet-header">
    <h1>Search</h1>
    <p>Search across cheatsheets, FAQs, guides, and LeetCode problems. Press <kbd>/</kbd> or <kbd>⌘K</kbd> from any page to get here.</p>
  </div>
  <input type="text" id="q" placeholder="Search topics, patterns, problems…" autofocus
    style="width:100%;padding:0.8rem 1rem;font-size:1.05rem;border:1px solid var(--border);border-radius:8px;background:var(--bg-secondary);color:var(--text);margin-bottom:0.5rem;">
  <p id="searchMeta" style="color:var(--text-light);font-size:0.9rem;margin:0.25rem 0 1.5rem;">Loading index…</p>
  <div id="results"></div>
  <script>
  (function () {
    var docs = [], problems = [], ready = 0;
    var q = document.getElementById('q');
    var meta = document.getElementById('searchMeta');
    var results = document.getElementById('results');

    function esc(s){ return String(s).replace(/[&<>"]/g, function(c){ return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;'}[c]; }); }

    Promise.all([
      fetch('data/search-index.json').then(function(r){ return r.json(); }).catch(function(){ return {records:[]}; }),
      fetch('data/lc-problems.json').then(function(r){ return r.json(); }).catch(function(){ return {problems:[]}; })
    ]).then(function (res) {
      docs = (res[0].records || []).map(function (d) {
        return { kind:'doc', title:d.title, url:d.url, category:d.category, type:d.type,
                 tier:d.tier || 0, summary:d.summary || '',
                 hay:(d.title + ' ' + (d.category||'') + ' ' + (d.summary||'') + ' ' + (d.headings||[]).join(' ')).toLowerCase() };
      });
      problems = (res[1].problems || []).map(function (p) {
        return { kind:'lc', id:p.id, title:p.title, difficulty:p.difficulty, tags:p.tags||[],
                 solutions:p.solutions||null,
                 hay:('#' + p.id + ' ' + p.title + ' ' + (p.tags||[]).join(' ') + ' leetcode').toLowerCase() };
      });
      meta.textContent = docs.length + ' docs · ' + problems.length + ' problems indexed. Type to search.';
      var url = new URLSearchParams(location.search);
      if (url.get('q')) { q.value = url.get('q'); run(); }
    });

    function score(rec, tokens) {
      var t = rec.title.toLowerCase();
      for (var i = 0; i < tokens.length; i++) { if (rec.hay.indexOf(tokens[i]) === -1) return -1; }
      var s = 0;
      for (var j = 0; j < tokens.length; j++) { if (t.indexOf(tokens[j]) !== -1) s += 10; if (t === tokens[j]) s += 20; }
      return s;
    }

    function lcSlug(title){ return title.toLowerCase().replace(/\\(.*$/,'').replace(/[^a-z0-9]+/g,'-').replace(/^-|-$/g,''); }

    function run() {
      var raw = q.value.trim().toLowerCase();
      if (!raw) { results.innerHTML = ''; meta.textContent = docs.length + ' docs · ' + problems.length + ' problems indexed. Type to search.'; return; }
      var tokens = raw.split(/\\s+/).filter(Boolean);

      // Equally-relevant hits are broken by interview weight, so a must-know
      // cheatsheet outranks a niche one on the same query.
      var docHits = docs.map(function(d){ return {r:d,s:score(d,tokens)}; }).filter(function(x){ return x.s >= 0; })
        .sort(function(a,b){ return (b.s - a.s) || (b.r.tier - a.r.tier); }).slice(0, 60);
      var lcHits = problems.map(function(p){ return {r:p,s:score(p,tokens)}; }).filter(function(x){ return x.s >= 0; })
        .sort(function(a,b){ return b.s - a.s; }).slice(0, 60);

      meta.textContent = docHits.length + ' doc results · ' + lcHits.length + ' problem results';
      var html = '';

      if (docHits.length) {
        html += '<h2>Docs &amp; Cheatsheets</h2><div class="cheatsheet-grid sheet-grid">';
        docHits.forEach(function(x){
          var d = x.r;
          var stars = d.tier ? '<span class="prio prio-' + d.tier + '"><span class="prio-stars" aria-hidden="true">' +
            Array(d.tier + 1).join('\u2605') + Array(6 - d.tier).join('\u2606') + '</span></span>' : '';
          html += '<article class="cheatsheet-card sheet-card' + (d.tier ? ' tier-' + d.tier : '') + '">' +
            '<div class="card-top"><h3 class="card-title"><a href="' + esc(d.url) + '">' + esc(d.title) + '</a></h3>' + stars + '</div>' +
            (d.summary ? '<p class="card-desc">' + esc(d.summary) + '</p>' : '') +
            '<p class="card-meta">' + esc(d.type) + (d.category ? ' · ' + esc(d.category) : '') + '</p></article>';
        });
        html += '</div>';
      }

      if (lcHits.length) {
        html += '<h2 style="margin-top:2rem;">LeetCode Problems</h2><div style="display:flex;flex-direction:column;gap:0.4rem;">';
        lcHits.forEach(function(x){
          var p = x.r;
          var links = '<a href="https://leetcode.com/problems/' + lcSlug(p.title) + '/" target="_blank" rel="noopener">LC</a>';
          if (p.solutions && p.solutions.java) links += ' · <a href="' + esc(p.solutions.java) + '" target="_blank" rel="noopener">Java</a>';
          if (p.solutions && p.solutions.python) links += ' · <a href="' + esc(p.solutions.python) + '" target="_blank" rel="noopener">Py</a>';
          html += '<div style="display:flex;gap:0.75rem;align-items:baseline;padding:0.5rem 0.75rem;background:var(--bg-secondary);border-radius:6px;">' +
            '<span style="font-family:monospace;color:var(--text-light);">#' + esc(p.id) + '</span>' +
            '<span style="flex:1;">' + esc(p.title) + '</span>' +
            '<span style="font-size:0.8rem;color:var(--text-light);">' + esc(p.difficulty||'') + '</span>' +
            '<span style="font-size:0.85rem;white-space:nowrap;">' + links + '</span></div>';
        });
        html += '</div>';
      }

      if (!docHits.length && !lcHits.length) html = '<p class="empty" style="color:var(--text-light);">No results for “' + esc(raw) + '”.</p>';
      results.innerHTML = html;
    }

    var timer;
    q.addEventListener('input', function(){ clearTimeout(timer); timer = setTimeout(run, 120); });
  })();
  </script>
`;
fs.writeFileSync('_site/search.html', htmlTemplate('Search', searchBody, 'search', '', {
  url: 'search.html',
  description: 'Search every cheatsheet, FAQ, guide and LeetCode problem in the repo.'
}));
console.log('✓ Created search.html');

// The sitemap and robots.txt are written by site/finalize-pages.js, which runs
// after the hand-written pages have been copied in and so can see the whole tree.

// ── Post-build self-check ────────────────────────────────────────────────────
// The priority badge carries a screen-reader sentence. It once leaked into TOC
// labels and search records because both read heading *text* with a blanket
// tag-strip; headingText() drops the badge first. Assert it stays dropped.
{
  const offenders = [];
  const scan = (dir) => {
    if (!fs.existsSync(dir)) return;
    for (const f of fs.readdirSync(dir).filter(n => n.endsWith('.html'))) {
      const html = fs.readFileSync(path.join(dir, f), 'utf8');
      // Inside a TOC link or a search record the sentence has no business appearing.
      const inToc = /<a href="#[^"]*">[^<]*Priority \d of 5 —/.test(html);
      if (inToc) offenders.push(path.join(dir, f));
    }
  };
  scan('_site/cheatsheets');
  scan('_site/faqs');
  const index = fs.existsSync('_site/data/search-index.json')
    ? fs.readFileSync('_site/data/search-index.json', 'utf8') : '';
  if (/Priority \d of 5 —/.test(index)) offenders.push('_site/data/search-index.json');
  if (offenders.length) {
    throw new Error(
      'Priority-badge text leaked into heading labels — route the text through ' +
      `headingText(): ${offenders.slice(0, 5).join(', ')}${offenders.length > 5 ? ` (+${offenders.length - 5})` : ''}`
    );
  }
}

// Every in-page anchor must land somewhere. A translated sheet is where this
// goes wrong silently: its links still carry the English heading slugs, which
// retargetAnchors rewrites — but only while the two documents keep the same
// heading shape. Assert the result rather than trusting it.
{
  const dir = '_site/cheatsheets';
  const idsOf = new Map();
  const pages = fs.existsSync(dir) ? fs.readdirSync(dir).filter(f => f.endsWith('.html')) : [];
  for (const page of pages) {
    const html = fs.readFileSync(path.join(dir, page), 'utf8');
    idsOf.set(page, new Set([...html.matchAll(/\bid="([^"]*)"/g)].map(m => m[1])));
  }

  const dangling = [];
  for (const page of pages) {
    const html = fs.readFileSync(path.join(dir, page), 'utf8');
    for (const [, href] of html.matchAll(/href="([^"]*#[^"]*)"/g)) {
      const hash = href.indexOf('#');
      const target = href.slice(0, hash) || page;
      // A fragment travels percent-encoded but is matched decoded, so compare decoded.
      let fragment;
      try { fragment = decodeURIComponent(href.slice(hash + 1)); } catch (_) { continue; }
      if (!fragment || !idsOf.has(target)) continue;
      if (!idsOf.get(target).has(fragment)) dangling.push(`${page} → ${href}`);
    }
  }
  if (dangling.length) {
    throw new Error(
      `${dangling.length} cheatsheet link(s) point at an anchor that does not exist:\n  ` +
      dangling.slice(0, 8).join('\n  ') +
      (dangling.length > 8 ? `\n  (+${dangling.length - 8} more)` : '') +
      '\nOn a .zh page this usually means the translation added or dropped a heading, ' +
      'so the positional anchor map was abandoned (see anchorMap in build-lib.js).'
    );
  }
}

console.log('✓ Website built successfully!');
