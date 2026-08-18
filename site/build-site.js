const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const MarkdownIt = require('markdown-it');
const markdownItAnchor = require('markdown-it-anchor');
const hljs = require('highlight.js');

// Build a map of filePath → last-modified date string in one git log call
function buildLastModifiedMap(filePaths) {
  const map = new Map();
  try {
    const raw = execSync(
      `git log --name-only --format="COMMIT %ai" -- ${filePaths.map(f => `"${f}"`).join(' ')}`,
      { encoding: 'utf8' }
    );
    let currentDate = null;
    for (const line of raw.split('\n')) {
      if (line.startsWith('COMMIT ')) {
        currentDate = new Date(line.slice(7).trim())
          .toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
      } else if (line.trim() && currentDate && !map.has(line.trim())) {
        map.set(line.trim(), currentDate);
      }
    }
  } catch (_) {}
  return map;
}

function slugify(text) {
  return text.toLowerCase().replace(/<[^>]*>/g, '').replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
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

function processLinks(html, siblingMdToHtml = false) {
  html = html.replace(
    /https:\/\/github\.com\/yennanliu\/CS_basics\/blob\/master\/doc\/cheatsheet\/([^")\s]+\.md)/g,
    (_, filename) => filename.replace('.md', '') + '.html'
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
  // Relative code links → absolute GitHub URLs, except internal cheatsheet .md links
  html = html.replace(
    /href="\.\/([^"]+)"/g,
    (_, relativePath) => {
      if (relativePath.startsWith('doc/cheatsheet/') && relativePath.endsWith('.md')) {
        return `href="${relativePath.replace('doc/cheatsheet/', '').replace('.md', '.html')}"`;
      }
      // Sibling link inside a cheatsheet, e.g. ./bst.md or ./heap.md#overview
      // → resolve to the built page (which lives in the same output dir)
      if (siblingMdToHtml && !relativePath.includes('/')) {
        const m = relativePath.match(/^([^#]+)\.md(#.*)?$/);
        if (m) return `href="${m[1]}.html${m[2] || ''}"`;
      }
      return `href="https://github.com/yennanliu/CS_basics/blob/master/${relativePath}"`;
    }
  );
  return html;
}

function renderContent(rawContent, siblingMdToHtml = false) {
  return wrapCodeBlocks(processLinks(md.render(rawContent), siblingMdToHtml));
}

// ── Priority (⭐) markers ─────────────────────────────────────────────────────
// Cheatsheets mark interview-critical sections with a trailing ⭐…⭐⭐⭐⭐⭐ run
// (see the style guide in CLAUDE.md). Raw emoji in a heading is easy to miss and
// impossible to filter, so the run is lifted out of the heading text into a
// badge and recorded as data-prio, which the TOC then reuses.
const TIER_LABELS = {
  5: 'Must know — expect it in almost every loop',
  4: 'High value — a gap here costs you rounds',
  3: 'Worth knowing — usually a variant of a must-know pattern',
  2: 'Niche — read once, revisit only if a company is known to ask',
  1: 'Nice to have'
};

function prioBadge(level, extraClass = '') {
  const n = Math.max(1, Math.min(5, level));
  const stars = '★'.repeat(n) + '☆'.repeat(5 - n);
  return `<span class="prio prio-${n}${extraClass ? ' ' + extraClass : ''}" title="${TIER_LABELS[n]}">` +
    `<span class="prio-stars" aria-hidden="true">${stars}</span>` +
    `<span class="sr-only">Priority ${n} of 5 — ${TIER_LABELS[n]}</span></span>`;
}

// Matches exactly what prioBadge() emits. Anything reading a heading's *text*
// (the TOC, the search index) has to drop the badge first, or the stars and the
// screen-reader sentence end up in the label.
const PRIO_BADGE_RE = /<span class="prio prio-\d[^"]*" title="[^"]*"><span class="prio-stars" aria-hidden="true">[^<]*<\/span><span class="sr-only">[^<]*<\/span><\/span>/g;

function headingText(inner) {
  return inner
    .replace(PRIO_BADGE_RE, '')
    .replace(/<[^>]*>/g, '')
    .replace(/^[\s#]+/, '')
    .trim();
}

// Rewrites ⭐ runs in h2–h4 into a badge. Heading ids are left untouched (they
// were slugified from the star-bearing text, so existing deep links keep working).
function annotatePriorityHeadings(htmlContent) {
  let maxLevel = 0;
  const html = htmlContent.replace(/<h([2-4])([^>]*)>([\s\S]*?)<\/h\1>/g, (full, level, attrs, inner) => {
    const stars = inner.match(/⭐{1,5}/);
    if (!stars) return full;
    const n = Math.min(5, stars[0].length);
    if (n > maxLevel) maxLevel = n;
    const cleaned = inner.replace(/⭐{1,5}/g, '').replace(/\s{2,}/g, ' ').trim();
    return `<h${level}${attrs} data-prio="${n}">${cleaned}${prioBadge(n, 'prio-heading')}</h${level}>`;
  });
  return { html, hasPriority: maxLevel > 0 };
}

const PRIORITY_LEGEND =
  '<div class="prio-legend"><span class="prio-legend-label">Section priority</span>' +
  [5, 4, 3, 2].map(n =>
    `<span class="prio-legend-item">${prioBadge(n)}<span class="prio-legend-text">${TIER_LABELS[n].split(' — ')[0]}</span></span>`
  ).join('') +
  '<span class="prio-legend-note">Marked on the sections that carry it — unmarked sections are background/reference.</span></div>';

// Nested TOC: h2 → h3, plus any h4 that carries a priority marker (those are the
// per-pattern templates people actually navigate to). Rendered as a sticky rail
// on wide screens and a collapsed <details> panel on narrow ones.
function generateTOC(htmlContent) {
  const headingRegex = /<h([234])(\s[^>]*)>([\s\S]*?)<\/h\1>/g;
  const headings = [];
  let match;
  while ((match = headingRegex.exec(htmlContent)) !== null) {
    const attrs = match[2];
    const idMatch = attrs.match(/id="([^"]*)"/);
    if (!idMatch) continue;
    const prioMatch = attrs.match(/data-prio="(\d)"/);
    const level = Number(match[1]);
    const prio = prioMatch ? Number(prioMatch[1]) : 0;
    // h4 only earns a TOC slot when it is flagged as interview-critical.
    if (level === 4 && prio < 4) continue;
    headings.push({
      level,
      prio,
      id: idMatch[1],
      text: headingText(match[3])
    });
  }
  if (headings.length < 3) return '';

  const entry = h =>
    `<li class="toc-item toc-l${h.level}${h.prio >= 4 ? ' toc-hot' : ''}">` +
    `<a href="#${h.id}">${h.text}` +
    (h.prio ? `<span class="toc-prio prio-${h.prio}" title="${TIER_LABELS[h.prio]}" aria-hidden="true">${'★'.repeat(h.prio)}</span>` : '') +
    '</a>';

  let toc = '';
  let openDepth = 0; // how many nested <ul> are currently open
  for (const h of headings) {
    const depth = h.level - 2; // 0 for h2, 1 for h3, 2 for h4
    while (openDepth > depth) { toc += '</li></ul>'; openDepth--; }
    if (openDepth < depth) {
      toc += `<ul class="toc-sublist toc-sublist-${depth}">`;
      openDepth = depth;
    } else if (toc) {
      toc += '</li>';
    }
    toc += entry(h);
  }
  while (openDepth > 0) { toc += '</li></ul>'; openDepth--; }
  if (toc) toc += '</li>';

  const sections = headings.filter(h => h.level === 2).length;
  return '<aside class="toc-rail">' +
    '<details class="toc" open data-toc>' +
    `<summary class="toc-summary"><span class="toc-summary-label">Contents</span>` +
    `<span class="toc-count">${sections} section${sections === 1 ? '' : 's'}</span></summary>` +
    `<nav class="toc-nav" aria-label="On this page"><ul class="toc-list">${toc}</ul></nav>` +
    '</details></aside>';
}

function extractHeadings(htmlContent) {
  const headingRegex = /<h([1-4])\s[^>]*?>(.*?)<\/h\1>/g;
  const headings = [];
  let match;
  while ((match = headingRegex.exec(htmlContent)) !== null) {
    const text = headingText(match[2]);
    if (text) headings.push(text);
  }
  return headings;
}

// Records accumulated across the build for the client-side global search index.
const searchRecords = [];

function ensureHeadingIds(htmlContent) {
  return htmlContent.replace(/<h([2-4])(?![^>]*\bid=)([^>]*)>(.*?)<\/h\1>/g, (_, level, attrs, text) =>
    `<h${level}${attrs} id="${slugify(text)}">${text}</h${level}>`
  );
}

function groupByCategory(items) {
  const grouped = {};
  for (const item of items) {
    if (!grouped[item.category]) grouped[item.category] = [];
    grouped[item.category].push(item);
  }
  return grouped;
}

function buildPrevNext(items, idx) {
  const prev = idx > 0 ? items[idx - 1] : null;
  const next = idx < items.length - 1 ? items[idx + 1] : null;
  return '<nav class="prev-next">' +
    (prev ? `<a href="${prev.file}.html" class="prev-link">← ${prev.title}</a>` : '<span></span>') +
    (next ? `<a href="${next.file}.html" class="next-link">${next.title} →</a>` : '<span></span>') +
    '</nav>';
}

function buildIndexGrid(grouped, categoryOrder, subFolder) {
  let html = '';
  for (const category of categoryOrder) {
    if (!grouped[category] || grouped[category].length === 0) continue;
    html += `<h2>${category}</h2><div class="cheatsheet-grid">`;
    for (const item of grouped[category]) {
      html += `\n        <div class="cheatsheet-card">` +
        `<h3><a href="${subFolder}/${item.file}.html">${item.title}</a></h3>` +
        `<p><a href="${subFolder}/${item.file}.html" class="read-more">Read more →</a></p>` +
        `</div>`;
    }
    html += '</div>';
  }
  return html;
}

// The cheatsheet index: a curated "start here" ladder, then every sheet grouped
// by category and ordered by interview weight, each carrying its Scope line as a
// description so the reader can tell 74 cards apart.
function buildCheatsheetIndex(sheets, meta) {
  const byFile = new Map(sheets.map(s => [s.file, s]));
  const tierLabel = tier => meta.tierLabels[String(tier)].label;

  const startHere = meta.startHere.map(s => ({ ...byFile.get(s.file), why: s.why })).filter(s => s.file);
  let html = '<h1>Algorithm &amp; Data Structure Cheat Sheets</h1>' +
    `<p class="intro">${sheets.length} sheets, grouped by topic and ranked by how often the pattern ` +
    'actually shows up in a FAANG software-engineering loop. Read the ' +
    '<a href="#start-here">Start here</a> ladder first; the catalogue below is for lookup.</p>';

  html += '<section class="tier-key" aria-label="What the star ratings mean">' +
    '<h2 class="key-heading">What the stars mean</h2><ul class="tier-key-list">' +
    [5, 4, 3, 2].map(t =>
      `<li class="tier-key-item">${prioBadge(t)}<span class="tier-key-label">${tierLabel(t)}</span>` +
      `<span class="tier-key-note">${meta.tierLabels[String(t)].note}</span></li>`
    ).join('') +
    '</ul><p class="tier-key-foot">The same stars appear on individual sections inside each sheet, so you can ' +
    'skim a 4,000-line doc and still see which templates are the ones to memorise.</p></section>';

  html += '<section class="start-here" id="start-here"><h2>Start here</h2>' +
    `<p class="cat-blurb">${startHere.length} sheets in reading order. Together they cover the large majority ` +
    'of what a coding round will actually ask.</p><ol class="start-list">';
  for (const s of startHere) {
    html += `<li class="start-item"><a class="start-title" href="cheatsheets/${s.file}.html">${s.title}</a>` +
      `${prioBadge(s.tier, 'prio-compact')}<span class="start-why">${s.why}</span></li>`;
  }
  html += '</ol></section>';

  html += '<h2 class="catalogue-heading" id="catalogue">Full catalogue</h2>';

  const grouped = groupByCategory(sheets);
  for (const category of meta.categoryOrder) {
    const items = grouped[category];
    if (!items || !items.length) continue;
    const anchor = slugify(category);
    html += `<h3 class="cat-heading" id="${anchor}">${category}` +
      `<span class="cat-count">${items.length} sheet${items.length === 1 ? '' : 's'}</span></h3>`;
    if (meta.categoryBlurbs[category]) {
      html += `<p class="cat-blurb">${meta.categoryBlurbs[category]}</p>`;
    }
    html += '<div class="cheatsheet-grid sheet-grid">';
    for (const item of items) {
      const kindChip = item.kind === 'stub'
        ? '<span class="kind-chip kind-stub">redirect</span>'
        : item.kind === 'reference'
          ? '<span class="kind-chip kind-reference">imported reference</span>'
          : '';
      html += `\n        <article class="cheatsheet-card sheet-card tier-${item.tier}">` +
        '<div class="card-top">' +
        `<h4 class="card-title"><a href="cheatsheets/${item.file}.html">${item.title}</a></h4>` +
        `${prioBadge(item.tier, 'prio-compact')}</div>` +
        (item.description ? `<p class="card-desc">${item.description}</p>` : '') +
        (kindChip ? `<p class="card-tags">${kindChip}</p>` : '') +
        '</article>';
    }
    html += '</div>';
  }

  html += '<div class="index-foot">' +
    '<p><strong>How to use this:</strong> pick the sheet, read its Scope line to confirm it owns your problem, ' +
    'then jump straight to the starred sections. Every sheet links to its neighbours rather than repeating them.</p>' +
    '<p>Source: <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">doc/cheatsheet on GitHub</a> — ' +
    'ratings and grouping live in <a href="https://github.com/yennanliu/CS_basics/blob/master/data/cheatsheet_meta.json">data/cheatsheet_meta.json</a>.</p>' +
    '</div>';
  return html;
}

// The markdown H1 is the real, hand-written title; the page header used to show a
// filename-derived one *above* it at a smaller size. Pull the H1 out of the body
// so the page has exactly one, at the top, in the header.
function splitLeadingH1(htmlContent) {
  const match = htmlContent.match(/^\s*<h1([^>]*)>([\s\S]*?)<\/h1>/);
  if (!match) return { title: null, titleId: null, html: htmlContent };
  const title = match[2].replace(/<[^>]*>/g, '').replace(/^[\s#]+/, '').trim();
  const idMatch = match[1].match(/id="([^"]*)"/);
  return {
    title: title || null,
    // Kept so links to the doc's top-level anchor still resolve.
    titleId: idMatch ? idMatch[1] : null,
    html: htmlContent.slice(match[0].length)
  };
}

function buildPageContent({
  title, htmlContent, toc, lastMod, indexHref, indexLabel, githubHref,
  meta = '', legend = '', titleId = null
}) {
  return `
      <nav class="breadcrumbs"><a href="../index.html">Home</a> <span class="sep">›</span> <a href="../${indexHref}">${indexLabel}</a> <span class="sep">›</span> <span class="current">${title}</span></nav>
      <div class="page-layout">
        ${toc}
        <div class="page-main">
          <div class="cheatsheet-header">
            <h1${titleId ? ` id="${titleId}"` : ''}>${title}</h1>
            <div class="header-meta">
              ${meta}
              ${lastMod ? `<span class="last-updated">Updated ${lastMod}</span>` : ''}
            </div>
          </div>
          ${legend}
          <div class="cheatsheet-content">
            ${htmlContent}
          </div>
          <div class="cheatsheet-footer">
            <a href="../${indexHref}" class="back-link">← Back to ${indexLabel}</a>
            <a href="${githubHref}" class="github-edit" target="_blank">Edit on GitHub →</a>
          </div>
        </div>
      </div>
    `;
}

// ── Data collection ─────────────────────────────────────────────────────────

const readme = fs.readFileSync('README.md', 'utf8');
const content = renderContent(readme);

let resourceContent = '';
if (fs.existsSync('doc/Resource.md')) {
  resourceContent = renderContent(fs.readFileSync('doc/Resource.md', 'utf8'));
}

// ── Cheatsheets ──────────────────────────────────────────────────────────────

const cheatsheetDir = 'doc/cheatsheet';
const cheatsheets = [];

// Category, FAANG-interview tier and title overrides live in one reviewable file
// rather than in keyword heuristics here — substring matching used to file
// difference_array under "arrays" and diff_toposort_quickunion under "sort".
const cheatsheetMeta = JSON.parse(fs.readFileSync('data/cheatsheet_meta.json', 'utf8'));

// Pulls the `> **Scope** — …` line out of a cheatsheet for use as its card
// description. Markdown emphasis and links are flattened to plain text.
function extractScope(rawMarkdown) {
  const line = rawMarkdown.split('\n').slice(0, 12).find(l => l.startsWith('> **Scope**'));
  if (!line) return null;
  return line
    .replace(/^>\s*\*\*Scope\*\*\s*—?\s*/, '')
    .replace(/\[([^\]]+)\]\([^)]*\)/g, '$1')   // links → their text
    .replace(/`([^`]*)`/g, '$1')
    .replace(/\*\*([^*]*)\*\*/g, '$1')
    .replace(/\*([^*]*)\*/g, '$1')
    .trim();
}

function titleCaseFromFile(baseName) {
  return baseName.replace(/_/g, ' ').split(' ')
    .map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');
}

if (fs.existsSync(cheatsheetDir)) {
  const files = fs.readdirSync(cheatsheetDir)
    .filter(f => f.endsWith('.md') && f !== 'README.md' && f !== '00_template.md')
    .sort();

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

  for (const file of files) {
    const filePath = path.join(cheatsheetDir, file);
    const baseName = path.basename(file, '.md');
    const sheetMeta = cheatsheetMeta.sheets[baseName];
    const raw = fs.readFileSync(filePath, 'utf8');

    let htmlContent = renderContent(raw, true);
    htmlContent = ensureHeadingIds(htmlContent);
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
      headings: extractHeadings(htmlContent).slice(0, 40)
    });

    const kindNote = kind === 'stub'
      ? '<span class="kind-chip kind-stub">redirect</span>'
      : kind === 'reference'
        ? '<span class="kind-chip kind-reference">imported reference</span>'
        : '';

    cheatsheets.push({
      file: baseName,
      title,
      category,
      tier,
      kind,
      description,
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

// ── FAQs ─────────────────────────────────────────────────────────────────────

const faqDir = 'doc/faq';
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

if (fs.existsSync(faqDir)) {
  const faqFiles = walkDir(faqDir).sort();
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
    const uniqueName = subDir === '.' ? baseName : `${subDir}_${baseName}`.replace(/\//g, '_');
    const title = baseName.replace(/_/g, ' ').split(' ')
      .map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');

    let category = 'General';
    if (subDir !== '.') {
      const topDir = subDir.split('/')[0];
      category = faqCategoryMap[topDir] || topDir.charAt(0).toUpperCase() + topDir.slice(1);
    }

    let htmlContent = renderContent(fs.readFileSync(filePath, 'utf8'));
    htmlContent = ensureHeadingIds(htmlContent);
    const { title: h1Title, titleId, html: bodyHtml } = splitLeadingH1(htmlContent);
    const { html: annotated, hasPriority } = annotatePriorityHeadings(bodyHtml);
    htmlContent = annotated;
    const pageTitle = h1Title || title;

    searchRecords.push({
      title: pageTitle,
      url: `faqs/${uniqueName}.html`,
      category,
      type: 'FAQ',
      headings: extractHeadings(htmlContent).slice(0, 40)
    });

    faqs.push({
      file: uniqueName,
      title,
      category,
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

// ── HTML template ─────────────────────────────────────────────────────────────

const htmlTemplate = (title, bodyContent, currentPage = 'home', basePath = '') => `
<!DOCTYPE html>
<html lang="en" data-theme="dark">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, viewport-fit=cover">
  <meta name="theme-color" content="#000000">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="mobile-web-app-capable" content="yes">
  <title>${title} — CS_basics</title>
  <meta name="description" content="Computer Science fundamentals: algorithms, data structures, system design, and LeetCode solutions">
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
  <div id="site-nav" data-page="${currentPage}" data-base="${basePath}"></div>
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

// ── Write output ─────────────────────────────────────────────────────────────

fs.writeFileSync('_site/index.html', htmlTemplate('Home', content, 'home'));
console.log('✓ Created index.html');

if (resourceContent) {
  fs.writeFileSync('_site/resources.html', htmlTemplate('Resources', resourceContent, 'resources'));
  console.log('✓ Created resources.html');
}

const cheatsheetIndexContent = buildCheatsheetIndex(cheatsheets, cheatsheetMeta);

fs.writeFileSync('_site/cheatsheets.html', htmlTemplate('Cheat Sheets', cheatsheetIndexContent, 'cheatsheets'));
console.log('✓ Created cheatsheets.html index');

if (cheatsheets.length > 0) {
  fs.mkdirSync('_site/cheatsheets', { recursive: true });
  cheatsheets.forEach((sheet, idx) => {
    let fixedContent = sheet.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    fixedContent += buildPrevNext(cheatsheets, idx);
    fs.writeFileSync(`_site/cheatsheets/${sheet.file}.html`, htmlTemplate(sheet.title, fixedContent, 'cheatsheets', '../'));
  });
  console.log(`✓ Created ${cheatsheets.length} individual cheatsheet pages`);
}

const knownFaqCategoryOrder = ['General', 'Java', 'Backend', 'Database', 'SQL', 'Redis', 'Kafka', 'Spark & Hadoop', 'Flink', 'Streaming'];
const faqGrouped = groupByCategory(faqs);
const faqCategoryOrder = [
  ...knownFaqCategoryOrder,
  ...Object.keys(faqGrouped).filter(cat => !knownFaqCategoryOrder.includes(cat))
];

let faqIndexContent = '<h1>FAQ - Frequently Asked Questions</h1>' +
  '<p class="intro">Interview preparation FAQs covering Java, Backend, Database, Streaming, and more.</p>' +
  buildIndexGrid(faqGrouped, faqCategoryOrder, 'faqs') +
  `\n<div style="margin-top: 3rem; padding: 1.5rem; background: var(--bg-secondary); border-radius: 8px;">
  <p><strong>💡 Tip:</strong> These FAQs are designed for quick reference during technical interview preparation.</p>
  <p>View all FAQs on <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/faq">GitHub</a>.</p>
</div>`;

fs.writeFileSync('_site/faqs.html', htmlTemplate('FAQs', faqIndexContent, 'faqs'));
console.log('✓ Created faqs.html index');

if (faqs.length > 0) {
  fs.mkdirSync('_site/faqs', { recursive: true });
  faqs.forEach((faq, idx) => {
    let fixedContent = faq.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    fixedContent += buildPrevNext(faqs, idx);
    fs.writeFileSync(`_site/faqs/${faq.file}.html`, htmlTemplate(faq.title, fixedContent, 'faqs', '../'));
  });
  console.log(`✓ Created ${faqs.length} individual FAQ pages`);
}

if (fs.existsSync('doc/pattern_recognition.md')) {
  let patternHtml = renderContent(fs.readFileSync('doc/pattern_recognition.md', 'utf8'));
  patternHtml = ensureHeadingIds(patternHtml);
  const patternContent = `
    <div class="cheatsheet-header">
      <h1>Pattern Recognition Guide</h1>
      <p>Map problem keywords to algorithm patterns — the fastest way to crack coding interviews.</p>
    </div>
    ${generateTOC(patternHtml)}
    <div class="cheatsheet-content">${patternHtml}</div>
  `;
  fs.writeFileSync('_site/patterns.html', htmlTemplate('Pattern Recognition', patternContent, 'patterns'));
  console.log('✓ Created patterns.html');

  searchRecords.push({
    title: 'Pattern Recognition Guide',
    url: 'patterns.html',
    category: 'Guide',
    type: 'Guide',
    headings: extractHeadings(patternHtml).slice(0, 60)
  });
}

// ── Global search index + search page ─────────────────────────────────────────

fs.mkdirSync('_site/data', { recursive: true });
fs.writeFileSync('_site/data/search-index.json', JSON.stringify({ records: searchRecords }));
console.log(`✓ Created data/search-index.json (${searchRecords.length} doc records)`);

const searchBody = `
  <div class="cheatsheet-header">
    <h1>Search</h1>
    <p>Search across cheatsheets, FAQs, guides, and LeetCode problems.</p>
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
                 hay:(d.title + ' ' + (d.category||'') + ' ' + (d.headings||[]).join(' ')).toLowerCase() };
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

      var docHits = docs.map(function(d){ return {r:d,s:score(d,tokens)}; }).filter(function(x){ return x.s >= 0; })
        .sort(function(a,b){ return b.s - a.s; }).slice(0, 60);
      var lcHits = problems.map(function(p){ return {r:p,s:score(p,tokens)}; }).filter(function(x){ return x.s >= 0; })
        .sort(function(a,b){ return b.s - a.s; }).slice(0, 60);

      meta.textContent = docHits.length + ' doc results · ' + lcHits.length + ' problem results';
      var html = '';

      if (docHits.length) {
        html += '<h2>Docs &amp; Cheatsheets</h2><div class="cheatsheet-grid">';
        docHits.forEach(function(x){
          var d = x.r;
          html += '<div class="cheatsheet-card"><h3><a href="' + esc(d.url) + '">' + esc(d.title) + '</a></h3>' +
            '<p style="color:var(--text-light);font-size:0.85rem;margin:0;">' + esc(d.type) + (d.category ? ' · ' + esc(d.category) : '') + '</p></div>';
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
fs.writeFileSync('_site/search.html', htmlTemplate('Search', searchBody, 'search'));
console.log('✓ Created search.html');

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

console.log('✓ Website built successfully!');
