const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const MarkdownIt = require('markdown-it');
const markdownItAnchor = require('markdown-it-anchor');
const hljs = require('highlight.js');
const {
  slugify, TIER_LABELS, prioBadge, PRIO_BADGE_RE, headingText,
  annotatePriorityHeadings, PRIORITY_LEGEND, generateTOC, extractHeadings,
  ensureHeadingIds, groupByCategory, buildPrevNext, buildIndexGrid,
  buildCheatsheetIndex, splitLeadingH1, buildPageContent, extractScope,
  titleCaseFromFile, summariseDoc
} = require('./build-lib');

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


// Records accumulated across the build for the client-side global search index.
const searchRecords = [];


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

    const raw = fs.readFileSync(filePath, 'utf8');
    let htmlContent = renderContent(raw);
    htmlContent = ensureHeadingIds(htmlContent);
    const { title: h1Title, titleId, html: bodyHtml } = splitLeadingH1(htmlContent);
    const { html: annotated, hasPriority } = annotatePriorityHeadings(bodyHtml);
    htmlContent = annotated;
    const pageTitle = h1Title || title;
    const docHeadings = extractHeadings(htmlContent);
    // FAQs have no Scope line, so the card description comes from the lead
    // paragraph, or failing that from the sections the doc covers.
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
