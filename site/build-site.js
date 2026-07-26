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

function buildPageContent(title, htmlContent, toc, lastMod, indexHref, indexLabel, githubHref) {
  return `
      <nav class="breadcrumbs"><a href="../index.html">Home</a> <span class="sep">›</span> <a href="../${indexHref}">${indexLabel}</a> <span class="sep">›</span> <span class="current">${title}</span></nav>
      <div class="cheatsheet-header">
        <h1>${title}</h1>
        ${lastMod ? `<span class="last-updated">Last updated: ${lastMod}</span>` : ''}
      </div>
      ${toc}
      <div class="cheatsheet-content">
        ${htmlContent}
      </div>
      <div class="cheatsheet-footer">
        <a href="../${indexHref}" class="back-link">← Back to ${indexLabel}</a>
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
const cheatsheets = [];

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

    let htmlContent = renderContent(fs.readFileSync(filePath, 'utf8'));
    htmlContent = ensureHeadingIds(htmlContent);

    let category = 'Other';
    for (const [cat, keywords] of Object.entries(categories)) {
      if (keywords.some(kw => baseName.includes(kw) || baseName === kw)) { category = cat; break; }
    }

    searchRecords.push({
      title,
      url: `cheatsheets/${baseName}.html`,
      category,
      type: 'Cheatsheet',
      headings: extractHeadings(htmlContent).slice(0, 40)
    });

    cheatsheets.push({
      file: baseName,
      title,
      category,
      content: buildPageContent(
        title, htmlContent, generateTOC(htmlContent),
        lastModMap.get(filePath) || null,
        'cheatsheets.html', 'Cheat Sheets',
        `https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/${file}`
      )
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
        <a href="${basePath}index.html" class="${currentPage === 'home' ? 'active' : ''}">home</a>
        <a href="${basePath}search.html" class="${currentPage === 'search' ? 'active' : ''}">search</a>
        <a href="${basePath}cheatsheets.html" class="${currentPage === 'cheatsheets' ? 'active' : ''}">cheatsheets</a>
        <a href="${basePath}patterns.html" class="${currentPage === 'patterns' ? 'active' : ''}">patterns</a>
        <a href="${basePath}faqs.html" class="${currentPage === 'faqs' ? 'active' : ''}">faqs</a>
        <a href="${basePath}lc-explorer.html" class="${currentPage === 'lc-explorer' ? 'active' : ''}">lc-explorer</a>
        <a href="${basePath}lc-similar.html" class="${currentPage === 'lc-similar' ? 'active' : ''}">similar</a>
        <a href="${basePath}lc-random-picker.html" class="${currentPage === 'lc-random-picker' ? 'active' : ''}">random</a>
        <a href="${basePath}lc-review-plan.html" class="${currentPage === 'lc-review-plan' ? 'active' : ''}">review</a>
        <a href="${basePath}algo_demo/index.html" class="${currentPage === 'visualizer' ? 'active' : ''}">visualizer</a>
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

const cheatsheetCategoryOrder = ['Core Data Structures', 'Search & Sort', 'Algorithm Patterns', 'Advanced Topics', 'Graph Algorithms', 'Complexity & Math', 'Strings & Patterns', 'Specialized', 'Interview Prep', 'Other'];
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

console.log('✓ Website built successfully!');
