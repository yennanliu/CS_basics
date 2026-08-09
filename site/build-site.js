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

// ── Navigation: single source of truth ───────────────────────────────────────
// Both the generated pages and the hand-written LC pages under site/pages/
// render their navbar from this list, so a link added here shows up everywhere.
const NAV_ITEMS = [
  { key: 'home',            href: 'index.html',            label: 'home' },
  { key: 'search',          href: 'search.html',           label: 'search' },
  { key: 'cheatsheets',     href: 'cheatsheets.html',      label: 'cheatsheets' },
  { key: 'patterns',        href: 'patterns.html',         label: 'patterns' },
  { key: 'faqs',            href: 'faqs.html',             label: 'faqs' },
  { key: 'lc-explorer',     href: 'lc-explorer.html',      label: 'lc-explorer' },
  { key: 'lc-similar',      href: 'lc-similar.html',       label: 'similar' },
  { key: 'lc-random-picker',href: 'lc-random-picker.html', label: 'random' },
  { key: 'lc-review-plan',  href: 'lc-review-plan.html',   label: 'review' },
  { key: 'visualizer',      href: 'algo_demo/index.html',  label: 'visualizer' },
];

function buildNavLinks(currentPage, basePath) {
  return NAV_ITEMS.map(item =>
    `<a href="${basePath}${item.href}" class="${currentPage === item.key ? 'active' : ''}">${item.label}</a>`
  ).join('\n        ');
}

// Shared across generated pages and the hand-written pages in site/pages/.
function buildFooter() {
  return `  <footer>
    <div class="container">
      <p>CS_basics — computer science fundamentals &amp; interview preparation</p>
      <p>
        <a href="https://github.com/yennanliu/CS_basics">github</a> |
        <a href="https://github.com/yennanliu/CS_basics/tree/master/doc">docs</a> |
        <a href="https://github.com/yennanliu/CS_basics/issues">issues</a>
      </p>
    </div>
  </footer>`;
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

function processLinks(html) {
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
      return `href="https://github.com/yennanliu/CS_basics/blob/master/${relativePath}"`;
    }
  );
  return html;
}

function renderContent(rawContent) {
  return wrapCodeBlocks(processLinks(md.render(rawContent)));
}

function generateTOC(htmlContent) {
  const headingRegex = /<h([23])\s[^>]*?id="([^"]*)"[^>]*>(.*?)<\/h\1>/g;
  const headings = [];
  let match;
  while ((match = headingRegex.exec(htmlContent)) !== null) {
    headings.push({
      level: match[1],
      id: match[2],
      text: match[3].replace(/<[^>]*>/g, '').replace(/^[\s#]+/, '')
    });
  }
  if (headings.length < 3) return '';
  let toc = '<div class="toc"><h2>Table of Contents</h2><ul>';
  for (const { level, text, id } of headings) {
    toc += `<li${level === '3' ? ' class="toc-sub"' : ''}><a href="#${id}">${text}</a></li>`;
  }
  return toc + '</ul></div>';
}

function extractHeadings(htmlContent) {
  const headingRegex = /<h([1-4])\s[^>]*?>(.*?)<\/h\1>/g;
  const headings = [];
  let match;
  while ((match = headingRegex.exec(htmlContent)) !== null) {
    const text = match[2].replace(/<[^>]*>/g, '').replace(/^[\s#]+/, '').trim();
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
    html += `<h2 id="${slugify(category)}">${category}</h2><div class="cheatsheet-grid">`;
    for (const item of grouped[category]) {
      const meta = item.partCount
        ? `<span class="card-meta">${item.partCount} parts</span>`
        : '';
      html += `\n        <div class="cheatsheet-card" data-title="${item.title.toLowerCase()}">` +
        `<h3><a href="${subFolder}/${item.file}.html">${item.title}</a></h3>` +
        `<p><a href="${subFolder}/${item.file}.html" class="read-more">Read more →</a>${meta}</p>` +
        `</div>`;
    }
    html += '</div>';
  }
  return html;
}

// ── Splitting oversized cheatsheets ──────────────────────────────────────────
// Some cheatsheets are 100–250 KB of markdown, which renders to a half-megabyte
// HTML page. Those get split into a hub page (kept at the original URL, so
// inbound links still work) plus one page per group of sections.

// Markdown expands roughly 2–4× into highlighted HTML, so these limits are set
// well below the page sizes we actually want to land on.
const SPLIT_THRESHOLD = 45_000;  // markdown bytes above which a sheet is split
const MIN_PARTS       = 3;       // a split must yield at least this many pieces
const MAX_PART        = 30_000;  // a section bigger than this is split again, deeper
const MERGE_MAX       = 25_000;  // consecutive sections are packed up to this size

// Headings are frequently `## [Title](url)` or `### \`fn()\` notes`; strip the
// inline markup so both the label and the slug stay readable.
function cleanHeadingText(text) {
  return text
    .replace(/#+\s*$/, '')
    .replace(/`([^`]*)`/g, '$1')
    .replace(/!?\[([^\]]*)\]\([^)]*\)/g, '$1')
    .replace(/[*_]{1,3}([^*_]+)[*_]{1,3}/g, '$1')
    .replace(/<[^>]*>/g, '')
    .replace(/\s+/g, ' ')
    .trim();
}

// Split raw markdown on headings of `level`, ignoring headings inside fenced
// code blocks — Python comments start with `#` and would otherwise split a page
// in the middle of a snippet.
function splitMarkdownByHeading(raw, level) {
  const prefix = '#'.repeat(level) + ' ';
  const intro = [];
  const parts = [];
  let current = null;
  let inFence = false;

  for (const line of raw.split('\n')) {
    if (/^\s*(```|~~~)/.test(line)) inFence = !inFence;
    if (!inFence && line.startsWith(prefix)) {
      if (current) parts.push(current);
      current = { title: cleanHeadingText(line.slice(prefix.length)), lines: [line] };
    } else if (current) {
      current.lines.push(line);
    } else {
      intro.push(line);
    }
  }
  if (current) parts.push(current);

  return {
    intro: intro.join('\n').trim(),
    parts: parts.map(p => ({ title: p.title || 'Untitled', md: p.lines.join('\n') }))
  };
}

// A section that is still oversized on its own gets broken up at the next
// heading level down, recursively. Without this, `dp.md`'s single 90 KB
// "Comprehensive Pattern Analysis" section would stay a 200 KB page.
function explodePart(part, nextLevel) {
  if (Buffer.byteLength(part.md) <= MAX_PART) return [part];
  for (let level = nextLevel; level <= 6; level++) {
    const sub = splitMarkdownByHeading(part.md, level);
    if (sub.parts.length < 2) continue;
    // Children keep their own heading text — the parent is already implied by
    // the hub page and the breadcrumb, and concatenating produces unreadably
    // long titles for deeply nested sections.
    const out = [];
    if (sub.intro.trim()) out.push({ title: part.title, md: sub.intro });
    for (const child of sub.parts) {
      out.push(...explodePart(child, level + 1));
    }
    return out;
  }
  return [part];  // nothing left to split on
}

// Pick the shallowest heading level that actually carves the file up. Most
// cheatsheets split cleanly on h2; a few (lc_category, python_trick) keep
// everything under a single h2 and only break apart at h3/h4.
function chooseSplit(raw) {
  for (const level of [2, 3, 4]) {
    const result = splitMarkdownByHeading(raw, level);
    if (result.parts.length >= MIN_PARTS) {
      const parts = result.parts.flatMap(p => explodePart(p, level + 1));
      return { level, intro: result.intro, parts };
    }
  }
  return null;
}

// Pack consecutive sections into pages, so a file with 68 tiny h3 sections
// doesn't become 68 near-empty pages. A single section over the limit always
// gets its own page rather than being dropped.
function mergeParts(parts) {
  const chunks = [];
  let buffer = null;
  for (const part of parts) {
    const size = Buffer.byteLength(part.md);
    if (buffer && Buffer.byteLength(buffer.md) + size > MERGE_MAX) {
      chunks.push(buffer);
      buffer = null;
    }
    if (!buffer) {
      buffer = { title: part.title, sections: [part.title], md: part.md };
    } else {
      buffer.sections.push(part.title);
      buffer.md += '\n' + part.md;
    }
  }
  if (buffer) chunks.push(buffer);
  return chunks;
}

// `processLinks` emits `doc/pic/...` for images (relative to the site root) and
// bare `name.html` for internal cheatsheet links (relative to cheatsheets/).
// Those two live at different depths, so they need separate prefixes.
function reanchor(html, rootUp, siblingUp) {
  return html
    .replace(/src\s*=\s*"doc\//g, `src="${rootUp}doc/`)
    .replace(/href="([a-z0-9_.+-]+\.html)"/gi, `href="${siblingUp}$1"`);
}

function buildPageContent(title, htmlContent, toc, lastMod, indexHref, indexLabel, githubHref, opts = {}) {
  const up = '../'.repeat(opts.depth || 1);
  const parentCrumb = opts.parent
    ? ` <span class="sep">›</span> <a href="${opts.parent.href}">${opts.parent.title}</a>`
    : '';
  const backHref = opts.backHref || `${up}${indexHref}`;
  const backLabel = opts.backLabel || indexLabel;
  return `
      <nav class="breadcrumbs"><a href="${up}index.html">Home</a> <span class="sep">›</span> <a href="${up}${indexHref}">${indexLabel}</a>${parentCrumb} <span class="sep">›</span> <span class="current">${title}</span></nav>
      <div class="cheatsheet-header">
        <h1>${title}</h1>
        ${lastMod ? `<span class="last-updated">Last updated: ${lastMod}</span>` : ''}
      </div>
      ${toc}
      <div class="cheatsheet-content">
        ${htmlContent}
      </div>
      <div class="cheatsheet-footer">
        <a href="${backHref}" class="back-link">← Back to ${backLabel}</a>
        <a href="${githubHref}" class="github-edit" target="_blank">Edit on GitHub →</a>
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
const cheatsheets = [];      // index cards + top-level pages (hub page for split sheets)
const cheatsheetParts = [];  // sub-pages of split sheets, written to cheatsheets/<sheet>/

if (fs.existsSync(cheatsheetDir)) {
  const files = fs.readdirSync(cheatsheetDir)
    .filter(f => f.endsWith('.md') && f !== 'README.md' && f !== '00_template.md')
    .sort();

  const filePaths = files.map(f => path.join(cheatsheetDir, f));
  const lastModMap = buildLastModifiedMap(filePaths);

  const categories = {
    'Core Data Structures': ['array', 'linked_list', 'tree', 'binary_tree', 'bst', 'graph', 'stack', 'queue', 'heap', 'hash_map', 'hashing', 'set', 'trie', 'Collection'],
    'Search & Sort': ['binary_search', 'dfs', 'bfs', 'sort', 'topology_sorting'],
    'Algorithm Patterns': ['2_pointers', 'sliding_window', 'backtrack', 'dp', 'greedy', 'recursion', 'palindrome', 'scanning_line', 'n_sum', 'add_x_sum', 'kadane', 'divide_and_conquer'],
    'Advanced Topics': ['union_find', 'segment_tree', 'binary_indexed_tree', 'monotonic_stack', 'prefix_sum', 'difference_array', 'advanced_simulation', 'streaming_algorithms'],
    'Graph Algorithms': ['Dijkstra', 'Bellman-Ford', 'Floyd-Warshall', 'diff_toposort', 'topology'],
    'Complexity & Math': ['complexity_cheatsheet', 'math', 'bit_manipulation'],
    'Strings & Patterns': ['string', 'kmp', 'rolling_hash'],
    'Specialized': ['matrix', 'intervals', 'design', 'iterator', 'stock_trading'],
    'Interview Prep': ['java_trick', 'python_trick', 'python_gotchas', 'gotchas', 'lc_pattern', 'lc_category', 'code_interview', 'diff_toposort_quickunion', 'concurrency']
  };

  for (const file of files) {
    const filePath = path.join(cheatsheetDir, file);
    const baseName = path.basename(file, '.md');
    const title = baseName.replace(/_/g, ' ').split(' ')
      .map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(' ');
    const rawMd = fs.readFileSync(filePath, 'utf8');
    const githubHref = `https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/${file}`;
    const lastMod = lastModMap.get(filePath) || null;

    let category = 'Other';
    for (const [cat, keywords] of Object.entries(categories)) {
      if (keywords.some(kw => baseName.includes(kw) || baseName === kw)) { category = cat; break; }
    }

    const split = Buffer.byteLength(rawMd) > SPLIT_THRESHOLD ? chooseSplit(rawMd) : null;

    if (!split) {
      let htmlContent = ensureHeadingIds(renderContent(rawMd));

      searchRecords.push({
        title, url: `cheatsheets/${baseName}.html`, category, type: 'Cheatsheet',
        headings: extractHeadings(htmlContent).slice(0, 40)
      });

      cheatsheets.push({
        file: baseName, title, category, src: filePath,
        content: buildPageContent(
          title, htmlContent, generateTOC(htmlContent), lastMod,
          'cheatsheets.html', 'Cheat Sheets', githubHref
        )
      });
      continue;
    }

    // ── Oversized sheet: hub page + one page per chunk of sections ──
    const usedSlugs = new Set();
    const chunks = mergeParts(split.parts).map((chunk, i) => {
      const base = (slugify(chunk.title).slice(0, 60).replace(/-+$/, '')) || `part-${i + 1}`;
      let slug = base;
      for (let n = 2; usedSlugs.has(slug); n++) slug = `${base}-${n}`;
      usedSlugs.add(slug);
      return { ...chunk, slug };
    });

    const introHtml = split.intro ? ensureHeadingIds(renderContent(split.intro)) : '';
    const sectionList = chunks.map((chunk, i) => {
      // The first section is already the link text — list only what follows it.
      const rest = chunk.sections.slice(1);
      const sub = rest.length
        ? `<p class="part-sections">${rest.map(s => md.utils.escapeHtml(s)).join(' · ')}</p>`
        : '';
      return `<li><a href="${baseName}/${chunk.slug}.html"><span class="part-num">${String(i + 1).padStart(2, '0')}</span> ${md.utils.escapeHtml(chunk.title)}</a>${sub}</li>`;
    }).join('\n');

    const hubHtml =
      `<p class="split-note">This cheatsheet is long, so it is split across ${chunks.length} pages.</p>` +
      introHtml +
      `<h2 id="sections">Sections</h2><ol class="part-list">${sectionList}</ol>`;

    searchRecords.push({
      title, url: `cheatsheets/${baseName}.html`, category, type: 'Cheatsheet',
      headings: chunks.flatMap(c => c.sections).slice(0, 40)
    });

    cheatsheets.push({
      file: baseName, title, category, src: filePath, isHub: true, partCount: chunks.length,
      content: buildPageContent(title, hubHtml, '', lastMod,
        'cheatsheets.html', 'Cheat Sheets', githubHref)
    });

    chunks.forEach((chunk, i) => {
      let partHtml = ensureHeadingIds(renderContent(chunk.md));
      // Part pages sit at cheatsheets/<sheet>/<slug>.html: two levels below the
      // site root, one level below its sibling cheatsheets.
      partHtml = reanchor(partHtml, '../../', '../');

      const prev = i > 0 ? chunks[i - 1] : null;
      const next = i < chunks.length - 1 ? chunks[i + 1] : null;
      const partNav = '<nav class="prev-next">' +
        (prev ? `<a href="${prev.slug}.html" class="prev-link">← ${md.utils.escapeHtml(prev.title)}</a>` : '<span></span>') +
        (next ? `<a href="${next.slug}.html" class="next-link">${md.utils.escapeHtml(next.title)} →</a>` : '<span></span>') +
        '</nav>';

      searchRecords.push({
        title: `${title} · ${chunk.title}`,
        url: `cheatsheets/${baseName}/${chunk.slug}.html`,
        category, type: 'Cheatsheet',
        headings: extractHeadings(partHtml).slice(0, 40)
      });

      cheatsheetParts.push({
        sheet: baseName,
        file: chunk.slug,
        title: `${title} · ${chunk.title}`,
        content: buildPageContent(
          chunk.title, partHtml, generateTOC(partHtml), lastMod,
          'cheatsheets.html', 'Cheat Sheets', githubHref,
          {
            depth: 2,
            parent: { href: `../${baseName}.html`, title },
            backHref: `../${baseName}.html`,
            backLabel: title
          }
        ) + partNav
      });
    });
  }
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

    searchRecords.push({
      title,
      url: `faqs/${uniqueName}.html`,
      category,
      type: 'FAQ',
      headings: extractHeadings(htmlContent).slice(0, 40)
    });

    faqs.push({
      file: uniqueName,
      title,
      category,
      src: filePath,
      content: buildPageContent(
        title, htmlContent, generateTOC(htmlContent),
        lastModMap.get(filePath) || null,
        'faqs.html', 'FAQs',
        `https://github.com/yennanliu/CS_basics/blob/master/${filePath}`
      )
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
  <link rel="stylesheet" href="${basePath}style.css">
  <link rel="stylesheet" href="${basePath}vendor/highlight/atom-one-dark.min.css">
  <script>
  (function() {
    var saved = localStorage.getItem('theme') || 'dark';
    document.documentElement.setAttribute('data-theme', saved);
  })();
  </script>
  <script>
  function copyCode(btn) {
    var pre = btn.closest('.code-block-wrapper').querySelector('pre');
    var text = pre ? pre.innerText : '';
    navigator.clipboard.writeText(text).then(function() {
      btn.textContent = 'copied';
      btn.classList.add('copied');
      setTimeout(function() { btn.textContent = 'copy'; btn.classList.remove('copied'); }, 2000);
    });
  }
  </script>
  <script>document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('table').forEach(function(table) {
      if (!table.parentElement.classList.contains('table-wrap')) {
        var wrapper = document.createElement('div');
        wrapper.className = 'table-wrap';
        table.parentNode.insertBefore(wrapper, table);
        wrapper.appendChild(table);
      }
    });
    var progressBar = document.getElementById('reading-progress');
    if (progressBar) {
      window.addEventListener('scroll', function() {
        var winScroll = document.documentElement.scrollTop;
        var height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
        progressBar.style.width = height > 0 ? (winScroll / height * 100) + '%' : '0%';
      });
    }
    var toggle = document.getElementById('theme-toggle');
    if (toggle) {
      var updateLabel = function() {
        toggle.textContent = document.documentElement.getAttribute('data-theme') === 'dark' ? '☀ light' : '● dark';
      };
      updateLabel();
      toggle.addEventListener('click', function() {
        var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
        var next = isDark ? 'light' : 'dark';
        document.documentElement.setAttribute('data-theme', next);
        localStorage.setItem('theme', next);
        updateLabel();
      });
    }
  });</script>
</head>
<body>
  <div class="progress-container"><div class="progress-bar" id="reading-progress"></div></div>
  <nav class="navbar">
    <div class="container">
      <a href="${basePath}index.html" class="nav-brand">
        <span class="nav-title">CS_basics</span>
      </a>
      <button class="nav-toggle" onclick="document.querySelector('.nav-links').classList.toggle('open')" aria-label="Toggle menu">
        <span></span><span></span><span></span>
      </button>
      <div class="nav-links">
        ${buildNavLinks(currentPage, basePath)}
        <button id="theme-toggle" class="theme-toggle" aria-label="Toggle theme">☀ light</button>
        <a href="https://github.com/yennanliu/CS_basics" target="_blank" class="github-link" aria-label="GitHub">github</a>
      </div>
    </div>
  </nav>

  <main class="container">
    <div class="content">
      ${bodyContent}
    </div>
  </main>

${buildFooter()}
</body>
</html>
`;

// ── Write output ─────────────────────────────────────────────────────────────

const cheatsheetCategoryOrder = ['Core Data Structures', 'Search & Sort', 'Algorithm Patterns', 'Advanced Topics', 'Graph Algorithms', 'Complexity & Math', 'Strings & Patterns', 'Specialized', 'Interview Prep', 'Other'];

// ── Landing page ─────────────────────────────────────────────────────────────
// The README is 370 KB of markdown; rendering it as the homepage produced a
// 630 KB entry point. It now lives at overview.html and the homepage is a real
// index into the site.

function countFiles(dir, ext) {
  let n = 0;
  const walkCount = d => {
    if (!fs.existsSync(d)) return;
    for (const entry of fs.readdirSync(d, { withFileTypes: true })) {
      const full = path.join(d, entry.name);
      if (entry.isDirectory()) walkCount(full);
      else if (entry.name.endsWith(ext)) n++;
    }
  };
  walkCount(dir);
  return n;
}

function countLcProblems() {
  const src = 'doc/google_leetcode_problems_by_tags.md';
  if (!fs.existsSync(src)) return 0;
  const ids = new Set();
  for (const line of fs.readFileSync(src, 'utf8').split('\n')) {
    const m = line.match(/^- #(\d+)\s/);
    if (m) ids.add(m[1]);
  }
  return ids.size;
}

// Newest-first ordering across every cheatsheet and FAQ source file. The map
// from buildLastModifiedMap preserves git's reverse-chronological order.
const allDocs = [
  ...cheatsheets.map(d => ({ ...d, dir: 'cheatsheets' })),
  ...faqs.map(d => ({ ...d, dir: 'faqs' })),
].filter(d => d.src);
const recentOrder = buildLastModifiedMap(allDocs.map(d => d.src));
const docBySrc = new Map(allDocs.map(d => [d.src, d]));
const recentlyUpdated = [];
for (const [src, date] of recentOrder) {
  const doc = docBySrc.get(src);
  if (doc) recentlyUpdated.push({ ...doc, date });
  if (recentlyUpdated.length >= 8) break;
}

const visualizerCount = fs.existsSync('algo_demo')
  ? fs.readdirSync('algo_demo').filter(f => f.endsWith('.html') && f !== 'index.html').length
  : 0;

const stats = [
  { n: cheatsheets.length,                    label: 'cheatsheets' },
  { n: faqs.length,                           label: 'interview faqs' },
  { n: countLcProblems(),                     label: 'lc problems indexed' },
  { n: countFiles('leetcode_python', '.py'),  label: 'python solutions' },
  { n: countFiles('leetcode_java', '.java'),  label: 'java solutions' },
  { n: visualizerCount,                       label: 'algo visualizers' },
];

const startHere = [
  { href: 'patterns.html',          title: 'Pattern recognition', desc: 'Map problem keywords to the algorithm that solves them.' },
  { href: 'cheatsheets.html',       title: 'Cheat sheets',        desc: `${cheatsheets.length} templates and patterns, grouped by topic.` },
  { href: 'lc-explorer.html',       title: 'LeetCode explorer',   desc: 'Filter problems by tag, difficulty, and acceptance rate.' },
  { href: 'lc-similar.html',        title: 'Similar problems',    desc: 'Find problems that share a pattern with one you just solved.' },
  { href: 'lc-review-plan.html',    title: 'Review plan',         desc: 'Spaced-repetition schedule for problems you have solved.' },
  { href: 'algo_demo/index.html',   title: 'Visualizers',         desc: `${visualizerCount} step-through animations of core algorithms.` },
  { href: 'faqs.html',              title: 'Interview FAQs',      desc: 'Java, backend, databases, Kafka, Spark, and streaming.' },
  { href: 'overview.html',          title: 'Full README',         desc: 'The complete problem tracker and resource list.' },
];

const categoryCounts = groupByCategory(cheatsheets);

const landingContent = `
  <section class="hero">
    <h1 class="hero-title">CS_basics</h1>
    <p class="hero-tagline">Computer science fundamentals for interview prep — algorithm cheat sheets,
      data structures, system design, and worked LeetCode solutions in Python, Java, Scala, and SQL.</p>
    <div class="hero-actions">
      <a href="cheatsheets.html" class="btn btn-primary">Browse cheat sheets</a>
      <a href="search.html" class="btn">Search everything</a>
      <a href="patterns.html" class="btn">Pattern guide</a>
    </div>
  </section>

  <div class="stat-grid">
    ${stats.map(s => `<div class="stat"><span class="stat-n">${s.n}</span><span class="stat-label">${s.label}</span></div>`).join('\n    ')}
  </div>

  <h2 id="start-here">Start here</h2>
  <div class="cheatsheet-grid">
    ${startHere.map(c => `<div class="cheatsheet-card">
      <h3><a href="${c.href}">${c.title}</a></h3>
      <p>${c.desc}</p>
    </div>`).join('\n    ')}
  </div>

  <h2 id="by-topic">Cheat sheets by topic</h2>
  <ul class="cat-list">
    ${cheatsheetCategoryOrder
      .filter(cat => categoryCounts[cat] && categoryCounts[cat].length)
      .map(cat => `<li><a href="cheatsheets.html#${slugify(cat)}">${cat}</a> <span class="cat-count">${categoryCounts[cat].length}</span></li>`)
      .join('\n    ')}
  </ul>

  ${recentlyUpdated.length ? `<h2 id="recent">Recently updated</h2>
  <ul class="recent-list">
    ${recentlyUpdated.map(d =>
      `<li><a href="${d.dir}/${d.file}.html">${d.title}</a> <span class="recent-date">${d.date}</span></li>`
    ).join('\n    ')}
  </ul>` : ''}

  <h2 id="repo">In the repository</h2>
  <p>Everything on this site is generated from markdown in
    <a href="https://github.com/yennanliu/CS_basics">yennanliu/CS_basics</a>. The repo also holds the
    solution source itself — <a href="https://github.com/yennanliu/CS_basics/tree/master/leetcode_python">Python</a>,
    <a href="https://github.com/yennanliu/CS_basics/tree/master/leetcode_java">Java</a>,
    <a href="https://github.com/yennanliu/CS_basics/tree/master/leetcode_SQL">SQL</a>,
    <a href="https://github.com/yennanliu/CS_basics/tree/master/leetcode_scala">Scala</a> —
    plus <a href="https://github.com/yennanliu/CS_basics/tree/master/system_design">system design</a> notes.</p>
`;

fs.writeFileSync('_site/index.html', htmlTemplate('Home', landingContent, 'home'));
console.log('✓ Created index.html (landing page)');

fs.writeFileSync('_site/overview.html', htmlTemplate('Overview', content, 'overview'));
console.log(`✓ Created overview.html (README, ${(Buffer.byteLength(content) / 1024).toFixed(0)} KB)`);

searchRecords.push({
  title: 'Overview (full README)', url: 'overview.html',
  category: 'Guide', type: 'Guide', headings: extractHeadings(content).slice(0, 60)
});

if (resourceContent) {
  fs.writeFileSync('_site/resources.html', htmlTemplate('Resources', resourceContent, 'resources'));
  console.log('✓ Created resources.html');
}

const cheatsheetGrouped = groupByCategory(cheatsheets);

let cheatsheetIndexContent = '<h1>Algorithm & Data Structure Cheat Sheets</h1>' +
  '<p class="intro">Comprehensive collection of algorithm patterns, data structures, and problem-solving techniques.</p>' +
  buildIndexGrid(cheatsheetGrouped, cheatsheetCategoryOrder, 'cheatsheets') +
  `\n<div style="margin-top: 3rem; padding: 1.5rem; background: var(--bg-secondary); border-radius: 8px;">
  <p><strong>💡 Tip:</strong> These cheatsheets are designed for quick reference during coding interviews and problem-solving.</p>
  <p>View all cheatsheets on <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">GitHub</a>.</p>
</div>`;

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

if (cheatsheetParts.length > 0) {
  for (const part of cheatsheetParts) {
    fs.mkdirSync(`_site/cheatsheets/${part.sheet}`, { recursive: true });
    fs.writeFileSync(
      `_site/cheatsheets/${part.sheet}/${part.file}.html`,
      htmlTemplate(part.title, part.content, 'cheatsheets', '../../')
    );
  }
  const splitSheets = new Set(cheatsheetParts.map(p => p.sheet));
  console.log(`✓ Split ${splitSheets.size} oversized cheatsheets into ${cheatsheetParts.length} sub-pages`);
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

// ── Hand-written pages (site/pages/*.html) ───────────────────────────────────
// The LeetCode tools are standalone apps with their own markup, so they are not
// generated from markdown. They are still copied through this build so the
// navbar can be injected from NAV_ITEMS — previously their hand-maintained nav
// had drifted and was missing cheatsheets / patterns / faqs / visualizer,
// leaving no way back to the rest of the site.

// Rendered in the LC pages' own <ul><li> idiom so their inline CSS still
// applies. `home` is omitted: the .logo already links to index.html.
function buildLcNavList(currentPage) {
  const items = NAV_ITEMS
    .filter(item => item.key !== 'home')
    .map(item => `        <li><a href="${item.href}"${currentPage === item.key ? ' class="active"' : ''}>${item.label}</a></li>`)
    .join('\n');
  return `<ul class="nav-links">\n${items}\n      </ul>`;
}

// Nine nav items no longer fit on one row at every width. The footer styles are
// scoped here too: these pages don't link style.css, and they name their muted
// colour --text-light rather than --text-muted.
const LC_INJECTED_CSS = `
    <style>
      .nav-content { flex-wrap: wrap; gap: 8px 16px; }
      .nav-links { flex-wrap: wrap; }
      footer {
        background: var(--surface); border-top: 1px solid var(--border);
        padding: 24px 0; margin-top: 32px; text-align: center; color: var(--text-light);
      }
      footer .container { max-width: 1200px; margin: 0 auto; padding: 0 16px; }
      footer p { margin: 4px 0; font-size: 13px; }
      footer a { color: var(--text-light); margin: 0 8px; text-decoration: none; }
      footer a:hover { color: var(--text); text-decoration: underline; }
    </style>`;

const pagesDir = 'site/pages';
if (fs.existsSync(pagesDir)) {
  const pageFiles = fs.readdirSync(pagesDir).filter(f => f.endsWith('.html')).sort();
  let injected = 0;

  for (const file of pageFiles) {
    const currentPage = path.basename(file, '.html');
    let html = fs.readFileSync(path.join(pagesDir, file), 'utf8');

    const navList = buildLcNavList(currentPage);
    const replaced = html.replace(/<ul class="nav-links">[\s\S]*?<\/ul>/, navList);
    if (replaced === html) {
      console.warn(`  ! ${file}: no <ul class="nav-links"> found — nav not injected`);
    } else {
      html = replaced;
      injected++;
    }

    html = html.replace('</head>', `${LC_INJECTED_CSS}\n</head>`);

    // These pages had no footer at all, so every other page on the site ended
    // with links back to the repo and these four didn't.
    if (!html.includes('<footer>')) {
      const withFooter = html.replace(/<\/body>/, `${buildFooter()}\n</body>`);
      if (withFooter === html) {
        console.warn(`  ! ${file}: no </body> found — footer not injected`);
      }
      html = withFooter;
    }

    fs.writeFileSync(path.join('_site', file), html);
  }
  console.log(`✓ Copied ${pageFiles.length} hand-written pages (shared nav + footer injected into ${injected})`);
}

console.log('✓ Website built successfully!');
