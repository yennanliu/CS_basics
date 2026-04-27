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
  // ../pic/ relative image paths → doc/pic/
  html = html.replace(
    /src\s*=\s*"\.\.\/pic\/([^"]+)"/g,
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
    'Interview Prep': ['java_trick', 'python_trick', 'lc_pattern', 'lc_category', 'code_interview', 'diff_toposort_quickunion']
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
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${title} - CS_basics</title>
  <meta name="description" content="Computer Science fundamentals: algorithms, data structures, system design, and LeetCode solutions">
  <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>CS</text></svg>">
  <link rel="stylesheet" href="${basePath}style.css">
  <link rel="stylesheet" href="${basePath}vendor/fonts.css">
  <link rel="stylesheet" href="${basePath}vendor/highlight/atom-one-dark.min.css">
  <script>
  (function() {
    var saved = localStorage.getItem('theme');
    if (saved === 'dark' || (!saved && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      document.documentElement.setAttribute('data-theme', 'dark');
    }
  })();
  </script>
  <script>
  function copyCode(btn) {
    var pre = btn.closest('.code-block-wrapper').querySelector('pre');
    var text = pre ? pre.innerText : '';
    navigator.clipboard.writeText(text).then(function() {
      btn.textContent = 'Copied!';
      btn.classList.add('copied');
      setTimeout(function() { btn.textContent = 'Copy'; btn.classList.remove('copied'); }, 2000);
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
      var isDark = document.documentElement.getAttribute('data-theme') === 'dark';
      toggle.textContent = isDark ? '☀️' : '🌙';
      toggle.addEventListener('click', function() {
        var dark = document.documentElement.getAttribute('data-theme') === 'dark';
        if (dark) {
          document.documentElement.removeAttribute('data-theme');
          localStorage.setItem('theme', 'light');
          toggle.textContent = '🌙';
        } else {
          document.documentElement.setAttribute('data-theme', 'dark');
          localStorage.setItem('theme', 'dark');
          toggle.textContent = '☀️';
        }
      });
    }
  });</script>
</head>
<body>
  <div class="progress-container"><div class="progress-bar" id="reading-progress"></div></div>
  <nav class="navbar">
    <div class="container">
      <div class="nav-brand">
        <span class="nav-icon">💻</span>
        <span class="nav-title">CS_basics</span>
      </div>
      <button class="nav-toggle" onclick="document.querySelector('.nav-links').classList.toggle('open')" aria-label="Toggle menu">
        <span></span><span></span><span></span>
      </button>
      <div class="nav-links">
        <a href="${basePath}index.html" class="${currentPage === 'home' ? 'active' : ''}">Home</a>
        <a href="${basePath}cheatsheets.html" class="${currentPage === 'cheatsheets' ? 'active' : ''}">Cheat Sheets</a>
        <a href="${basePath}patterns.html" class="${currentPage === 'patterns' ? 'active' : ''}">Pattern Recognition</a>
        <a href="${basePath}faqs.html" class="${currentPage === 'faqs' ? 'active' : ''}">FAQs</a>
        <a href="${basePath}resources.html" class="${currentPage === 'resources' ? 'active' : ''}">Resources</a>
        <a href="${basePath}lc-explorer.html" class="${currentPage === 'lc-explorer' ? 'active' : ''}">LC Explorer</a>
        <a href="${basePath}algo_demo/index.html" class="${currentPage === 'visualizer' ? 'active' : ''}">Visualizer</a>
        <button id="theme-toggle" class="theme-toggle" aria-label="Toggle dark mode">&#127769;</button>
        <a href="https://github.com/yennanliu/CS_basics" target="_blank" class="github-link" aria-label="GitHub">
          <svg width="20" height="20" viewBox="0 0 16 16" fill="currentColor">
            <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"/>
          </svg>
        </a>
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
      <p>CS_basics - Computer Science Fundamentals & Interview Preparation</p>
      <p>
        <a href="https://github.com/yennanliu/CS_basics">GitHub</a> •
        <a href="https://github.com/yennanliu/CS_basics/tree/master/doc">Documentation</a> •
        <a href="https://github.com/yennanliu/CS_basics/issues">Report Issues</a>
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
}

console.log('✓ Website built successfully!');
