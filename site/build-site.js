const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const MarkdownIt = require('markdown-it');
const markdownItAnchor = require('markdown-it-anchor');
const hljs = require('highlight.js');

function getLastModified(filePath) {
  try {
    const date = execSync(`git log -1 --format="%ai" -- "${filePath}"`, { encoding: 'utf8' }).trim();
    if (!date) return null;
    return new Date(date).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
  } catch (e) { return null; }
}

// Custom slug function shared by markdown-it-anchor and TOC generation
function slugify(text) {
  return text.toLowerCase().replace(/<[^>]*>/g, '').replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
}

function buildTimeHighlight(str, lang) {
  const code = lang && hljs.getLanguage(lang)
    ? hljs.highlight(str, { language: lang, ignoreIllegals: true }).value
    : MarkdownIt().utils.escapeHtml(str);
  const lines = code.split('\n');
  if (lines[lines.length - 1] === '') lines.pop();
  const numbered = lines.map((line, i) =>
    `<span class="ln-row"><span class="ln-num">${i + 1}</span><span class="ln-code">${line}</span></span>`
  ).join('\n');
  const cls = lang ? ` class="hljs language-${lang}"` : ' class="hljs"';
  return `<pre${lang ? ` data-lang="${lang}"` : ''}><code${cls}>${numbered}\n</code></pre>`;
}

const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: buildTimeHighlight
}).use(markdownItAnchor, {
  slugify: slugify,
  permalink: markdownItAnchor.permalink && markdownItAnchor.permalink.headerLink
    ? markdownItAnchor.permalink.headerLink({ safariReaderFix: true })
    : true,
  // Fallback options for older API (before v9 permalink object)
  permalinkBefore: true,
  permalinkSymbol: '#'
});

// Function to process links - convert cheatsheet links to internal, keep GitHub code links as external
function processLinks(html) {
  // Convert cheatsheet GitHub links to internal links
  html = html.replace(
    /https:\/\/github\.com\/yennanliu\/CS_basics\/blob\/master\/doc\/cheatsheet\/([^")\s]+\.md)/g,
    (match, filename) => {
      const baseName = filename.replace('.md', '');
      return `${baseName}.html`;
    }
  );

  // Convert GitHub blob image URLs to local paths
  // e.g. https://github.com/yennanliu/CS_basics/blob/master/doc/pic/heap.png → doc/pic/heap.png
  // Handles both src="..." and src ="..." (with optional space)
  html = html.replace(
    /src\s*=\s*"https:\/\/github\.com\/yennanliu\/CS_basics\/blob\/master\/doc\/pic\/([^"]+)"/g,
    'src="doc/pic/$1"'
  );

  // Convert relative ../pic/ paths (used by some cheatsheet files) to doc/pic/
  // e.g. ../pic/binary_search_pattern.png → doc/pic/binary_search_pattern.png
  html = html.replace(
    /src\s*=\s*"\.\.\/pic\/([^"]+)"/g,
    'src="doc/pic/$1"'
  );

  // Convert relative code links (./leetcode_python/..., ./data_structure/..., etc.)
  // to absolute GitHub URLs so they don't resolve to the deployed site
  html = html.replace(
    /href="\.\/([^"]+)"/g,
    (match, relativePath) => {
      // Check if it's a cheatsheet .md link that should stay internal
      if (relativePath.startsWith('doc/cheatsheet/') && relativePath.endsWith('.md')) {
        const baseName = relativePath.replace('doc/cheatsheet/', '').replace('.md', '');
        return `href="${baseName}.html"`;
      }
      // All other relative paths → GitHub
      return `href="https://github.com/yennanliu/CS_basics/blob/master/${relativePath}"`;
    }
  );

  return html;
}

// Read README
const readme = fs.readFileSync('README.md', 'utf8');
const content = processLinks(md.render(readme));

// Read Resource.md if exists
let resourceContent = '';
if (fs.existsSync('doc/Resource.md')) {
  const resource = fs.readFileSync('doc/Resource.md', 'utf8');
  resourceContent = processLinks(md.render(resource));
}

// Function to generate table of contents
// Extracts the id already placed on headings by markdown-it-anchor
function generateTOC(htmlContent) {
  const headingRegex = /<h([23])\s[^>]*?id="([^"]*)"[^>]*>(.*?)<\/h\1>/g;
  const headings = [];
  let match;

  while ((match = headingRegex.exec(htmlContent)) !== null) {
    const level = match[1];
    const id = match[2];  // use the actual id from the rendered heading
    const text = match[3].replace(/<[^>]*>/g, '').replace(/^[\s#]+/, ''); // Remove HTML tags and leading #/spaces from permalink
    headings.push({ level, text, id });
  }

  if (headings.length < 3) return ''; // Don't show TOC for short documents

  let toc = '<div class="toc"><h2>Table of Contents</h2><ul>';
  headings.forEach(({ level, text, id }) => {
    const indent = level === '3' ? ' class="toc-sub"' : '';
    toc += `<li${indent}><a href="#${id}">${text}</a></li>`;
  });
  toc += '</ul></div>';

  return toc;
}

// Ensure all h2/h3/h4 headings have an id attribute
// (markdown-it-anchor handles most, this catches any from raw HTML blocks)
function ensureHeadingIds(htmlContent) {
  return htmlContent.replace(/<h([2-4])(?![^>]*\bid=)([^>]*)>(.*?)<\/h\1>/g, (match, level, attrs, text) => {
    const id = slugify(text);
    return `<h${level}${attrs} id="${id}">${text}</h${level}>`;
  });
}

// Process all cheatsheet files
const cheatsheetDir = 'doc/cheatsheet';
const cheatsheets = [];

if (fs.existsSync(cheatsheetDir)) {
  const files = fs.readdirSync(cheatsheetDir)
    .filter(f => f.endsWith('.md') && f !== 'README.md' && f !== '00_template.md')
    .sort();

  // Group cheatsheets by category
  const categories = {
    'Core Data Structures': ['array', 'linked_list', 'tree', 'binary_tree', 'bst', 'graph', 'stack', 'queue', 'heap', 'hash_map', 'hashing', 'set', 'trie', 'Collection'],
    'Search & Sort': ['binary_search', 'dfs', 'bfs', 'sort', 'topology_sorting'],
    'Algorithm Patterns': ['2_pointers', 'sliding_window', 'backtrack', 'dp', 'greedy', 'recursion', 'palindrome', 'scanning_line', 'n_sum', 'add_x_sum', 'kadane', 'divide_and_conquer'],
    'Advanced Topics': ['union_find', 'segment_tree', 'binary_indexed_tree', 'monotonic_stack', 'prefix_sum', 'difference_array', 'advanced_simulation', 'streaming_algorithms'],
    'Graph Algorithms': ['Dijkstra', 'Bellman-Ford', 'Floyd-Warshall', 'diff_toposort', 'topology'],
    'Complexity & Math': ['complexity_cheatsheet', 'math', 'bit_manipulation'],
    'Strings & Patterns': ['string', 'kmp', 'rolling_hash'],
    'Specialized': ['matrix', 'intervals', 'design', 'iterator', 'stock_trading'],
    'Interview Prep': ['java_trick', 'python_trick', 'lc_pattern', 'lc_category', 'code_interview', 'diff_toposort_quickunion', 'Collection']
  };

  files.forEach(file => {
    const filePath = path.join(cheatsheetDir, file);
    const rawContent = fs.readFileSync(filePath, 'utf8');
    const baseName = path.basename(file, '.md');
    const title = baseName.replace(/_/g, ' ')
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');

    // Render and process links
    let htmlContent = md.render(rawContent);
    htmlContent = processLinks(htmlContent);
    htmlContent = ensureHeadingIds(htmlContent);

    // Generate table of contents
    const toc = generateTOC(htmlContent);

    // Wrap content with navigation and better structure
    const lastMod = getLastModified(filePath);
    const wrappedContent = `
      <nav class="breadcrumbs"><a href="../index.html">Home</a> <span class="sep">›</span> <a href="../cheatsheets.html">Cheat Sheets</a> <span class="sep">›</span> <span class="current">${title}</span></nav>
      <div class="cheatsheet-header">
        <h1>${title}</h1>
        ${lastMod ? `<span class="last-updated">Last updated: ${lastMod}</span>` : ''}
      </div>
      ${toc}
      <div class="cheatsheet-content">
        ${htmlContent}
      </div>
      <div class="cheatsheet-footer">
        <a href="../cheatsheets.html" class="back-link">← Back to Cheat Sheets</a>
        <a href="https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/${file}" class="github-edit" target="_blank">
          Edit on GitHub →
        </a>
      </div>
    `;

    cheatsheets.push({
      file: baseName,
      title: title,
      content: wrappedContent,
      category: null
    });
  });

  // Assign categories
  cheatsheets.forEach(sheet => {
    for (const [category, keywords] of Object.entries(categories)) {
      if (keywords.some(kw => sheet.file.includes(kw) || sheet.file === kw)) {
        sheet.category = category;
        break;
      }
    }
    if (!sheet.category) {
      sheet.category = 'Other';
    }
  });
}

// Process all FAQ files
const faqDir = 'doc/faq';
const faqs = [];

function walkDir(dir) {
  const entries = fs.readdirSync(dir, { withFileTypes: true });
  const files = [];
  for (const entry of entries) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      files.push(...walkDir(fullPath));
    } else if (entry.name.endsWith('.md')) {
      files.push(fullPath);
    }
  }
  return files;
}

if (fs.existsSync(faqDir)) {
  const faqFiles = walkDir(faqDir).sort();

  // Category mapping based on subdirectory
  const faqCategoryMap = {
    'java': 'Java',
    'backend': 'Backend',
    'db': 'Database',
    'redis': 'Redis',
    'kafka': 'Kafka',
    'spark': 'Spark & Hadoop',
    'flink': 'Flink',
    'stream': 'Streaming',
    'sql': 'SQL'
  };

  faqFiles.forEach(filePath => {
    const rawContent = fs.readFileSync(filePath, 'utf8');
    const relativePath = path.relative(faqDir, filePath);
    const baseName = path.basename(filePath, '.md');
    const subDir = path.dirname(relativePath);
    const uniqueName = subDir === '.' ? baseName : `${subDir}_${baseName}`.replace(/\//g, '_');
    const title = baseName.replace(/_/g, ' ')
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');

    // Determine category
    let category = 'General';
    if (subDir !== '.') {
      const topDir = subDir.split('/')[0];
      category = faqCategoryMap[topDir] || topDir.charAt(0).toUpperCase() + topDir.slice(1);
    }

    // Render and process links
    let htmlContent = md.render(rawContent);
    htmlContent = processLinks(htmlContent);
    htmlContent = ensureHeadingIds(htmlContent);

    const toc = generateTOC(htmlContent);

    const lastMod = getLastModified(filePath);
    const wrappedContent = `
      <nav class="breadcrumbs"><a href="../index.html">Home</a> <span class="sep">›</span> <a href="../faqs.html">FAQs</a> <span class="sep">›</span> <span class="current">${title}</span></nav>
      <div class="cheatsheet-header">
        <h1>${title}</h1>
        ${lastMod ? `<span class="last-updated">Last updated: ${lastMod}</span>` : ''}
      </div>
      ${toc}
      <div class="cheatsheet-content">
        ${htmlContent}
      </div>
      <div class="cheatsheet-footer">
        <a href="../faqs.html" class="back-link">← Back to FAQs</a>
        <a href="https://github.com/yennanliu/CS_basics/blob/master/${filePath}" class="github-edit" target="_blank">
          Edit on GitHub →
        </a>
      </div>
    `;

    faqs.push({
      file: uniqueName,
      title: title,
      content: wrappedContent,
      category: category
    });
  });
}

// Create cheatsheet index
let cheatsheetIndexContent = '<h1>Algorithm & Data Structure Cheat Sheets</h1>';
cheatsheetIndexContent += '<p class="intro">Comprehensive collection of algorithm patterns, data structures, and problem-solving techniques.</p>';

// Group by category
const grouped = {};
cheatsheets.forEach(sheet => {
  if (!grouped[sheet.category]) {
    grouped[sheet.category] = [];
  }
  grouped[sheet.category].push(sheet);
});

// Generate index with categories
const categoryOrder = ['Core Data Structures', 'Search & Sort', 'Algorithm Patterns', 'Advanced Topics', 'Graph Algorithms', 'Complexity & Math', 'Strings & Patterns', 'Specialized', 'Interview Prep', 'Other'];

categoryOrder.forEach(category => {
  if (grouped[category] && grouped[category].length > 0) {
    cheatsheetIndexContent += `<h2>${category}</h2><div class="cheatsheet-grid">`;
    grouped[category].forEach(sheet => {
      cheatsheetIndexContent += `
        <div class="cheatsheet-card">
          <h3><a href="cheatsheets/${sheet.file}.html">${sheet.title}</a></h3>
          <p><a href="cheatsheets/${sheet.file}.html" class="read-more">Read more →</a></p>
        </div>`;
    });
    cheatsheetIndexContent += '</div>';
  }
});

cheatsheetIndexContent += `
<div style="margin-top: 3rem; padding: 1.5rem; background: var(--bg-secondary); border-radius: 8px;">
  <p><strong>💡 Tip:</strong> These cheatsheets are designed for quick reference during coding interviews and problem-solving.</p>
  <p>View all cheatsheets on <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">GitHub</a>.</p>
</div>`;

// Create HTML template
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
  // Apply theme before render to prevent flash
  (function() {
    var saved = localStorage.getItem('theme');
    if (saved === 'dark' || (!saved && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
      document.documentElement.setAttribute('data-theme', 'dark');
    }
  })();
  </script>
  <script>document.addEventListener('DOMContentLoaded', function() {
    // Code block wrappers + copy buttons (highlighting done at build time)
    try {
      document.querySelectorAll('pre').forEach(function(pre) {
        var codeEl = pre.querySelector('code');
        var lang = pre.getAttribute('data-lang') || '';
        var wrapper = document.createElement('div');
        wrapper.className = 'code-block-wrapper';
        pre.parentNode.insertBefore(wrapper, pre);
        var header = document.createElement('div');
        header.className = 'code-block-header';
        if (lang) {
          var label = document.createElement('span');
          label.className = 'code-lang-label';
          label.textContent = lang;
          header.appendChild(label);
        } else {
          header.appendChild(document.createElement('span'));
        }
        var btn = document.createElement('button');
        btn.className = 'copy-btn';
        btn.textContent = 'Copy';
        btn.addEventListener('click', function() {
          var text = codeEl
            ? Array.from(codeEl.querySelectorAll('.ln-code')).map(function(el) { return el.textContent; }).join('\n')
            : pre.textContent;
          navigator.clipboard.writeText(text).then(function() {
            btn.textContent = 'Copied!';
            btn.classList.add('copied');
            setTimeout(function() { btn.textContent = 'Copy'; btn.classList.remove('copied'); }, 2000);
          });
        });
        header.appendChild(btn);
        wrapper.appendChild(header);
        wrapper.appendChild(pre);
      });
    } catch(e) { console.error('Code block setup error:', e); }
    document.querySelectorAll('table').forEach(function(table) {
      if (!table.parentElement.classList.contains('table-wrap')) {
        var wrapper = document.createElement('div');
        wrapper.className = 'table-wrap';
        table.parentNode.insertBefore(wrapper, table);
        wrapper.appendChild(table);
      }
    });
    // Reading progress bar
    try {
      var progressBar = document.getElementById('reading-progress');
      if (progressBar) {
        window.addEventListener('scroll', function() {
          var winScroll = document.documentElement.scrollTop;
          var height = document.documentElement.scrollHeight - document.documentElement.clientHeight;
          progressBar.style.width = height > 0 ? (winScroll / height * 100) + '%' : '0%';
        });
      }
    } catch(e) {}
    // Dark mode toggle — isolated so errors elsewhere never prevent this from working
    try {
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
    } catch(e) { console.error('Theme toggle error:', e); }
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

// Write index.html
fs.writeFileSync('_site/index.html', htmlTemplate('Home', content, 'home'));
console.log('✓ Created index.html');

// Write resources.html
if (resourceContent) {
  fs.writeFileSync('_site/resources.html', htmlTemplate('Resources', resourceContent, 'resources'));
  console.log('✓ Created resources.html');
}

// Write cheatsheets index
fs.writeFileSync('_site/cheatsheets.html', htmlTemplate('Cheat Sheets', cheatsheetIndexContent, 'cheatsheets'));
console.log('✓ Created cheatsheets.html index');

// Create cheatsheets directory and write individual pages
if (cheatsheets.length > 0) {
  fs.mkdirSync('_site/cheatsheets', { recursive: true });

  cheatsheets.forEach((sheet, idx) => {
    // Fix image paths for sub-pages (doc/pic -> ../doc/pic)
    // Handles both src="doc/" and src ="doc/" (with optional space before =)
    let fixedContent = sheet.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    // Add prev/next navigation
    const prev = idx > 0 ? cheatsheets[idx - 1] : null;
    const next = idx < cheatsheets.length - 1 ? cheatsheets[idx + 1] : null;
    let prevNext = '<nav class="prev-next">';
    prevNext += prev ? `<a href="${prev.file}.html" class="prev-link">← ${prev.title}</a>` : '<span></span>';
    prevNext += next ? `<a href="${next.file}.html" class="next-link">${next.title} →</a>` : '<span></span>';
    prevNext += '</nav>';
    fixedContent += prevNext;
    const cheatsheetHtml = htmlTemplate(sheet.title, fixedContent, 'cheatsheets', '../');
    fs.writeFileSync(`_site/cheatsheets/${sheet.file}.html`, cheatsheetHtml);
  });

  console.log(`✓ Created ${cheatsheets.length} individual cheatsheet pages`);
}

// Build FAQ index page
let faqIndexContent = '<h1>FAQ - Frequently Asked Questions</h1>';
faqIndexContent += '<p class="intro">Interview preparation FAQs covering Java, Backend, Database, Streaming, and more.</p>';

const faqGrouped = {};
faqs.forEach(faq => {
  if (!faqGrouped[faq.category]) {
    faqGrouped[faq.category] = [];
  }
  faqGrouped[faq.category].push(faq);
});

const faqCategoryOrder = ['General', 'Java', 'Backend', 'Database', 'SQL', 'Redis', 'Kafka', 'Spark & Hadoop', 'Flink', 'Streaming'];

// Add any categories not in the predefined order
Object.keys(faqGrouped).forEach(cat => {
  if (!faqCategoryOrder.includes(cat)) {
    faqCategoryOrder.push(cat);
  }
});

faqCategoryOrder.forEach(category => {
  if (faqGrouped[category] && faqGrouped[category].length > 0) {
    faqIndexContent += `<h2>${category}</h2><div class="cheatsheet-grid">`;
    faqGrouped[category].forEach(faq => {
      faqIndexContent += `
        <div class="cheatsheet-card">
          <h3><a href="faqs/${faq.file}.html">${faq.title}</a></h3>
          <p><a href="faqs/${faq.file}.html" class="read-more">Read more →</a></p>
        </div>`;
    });
    faqIndexContent += '</div>';
  }
});

faqIndexContent += `
<div style="margin-top: 3rem; padding: 1.5rem; background: var(--bg-secondary); border-radius: 8px;">
  <p><strong>💡 Tip:</strong> These FAQs are designed for quick reference during technical interview preparation.</p>
  <p>View all FAQs on <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/faq">GitHub</a>.</p>
</div>`;

// Write FAQ index
fs.writeFileSync('_site/faqs.html', htmlTemplate('FAQs', faqIndexContent, 'faqs'));
console.log('✓ Created faqs.html index');

// Write individual FAQ pages
if (faqs.length > 0) {
  fs.mkdirSync('_site/faqs', { recursive: true });

  faqs.forEach((faq, idx) => {
    // Fix image paths for sub-pages (doc/pic -> ../doc/pic)
    // Handles both src="doc/" and src ="doc/" (with optional space before =)
    let fixedContent = faq.content.replace(/src\s*=\s*"doc\//g, 'src="../doc/');
    // Add prev/next navigation
    const prev = idx > 0 ? faqs[idx - 1] : null;
    const next = idx < faqs.length - 1 ? faqs[idx + 1] : null;
    let prevNext = '<nav class="prev-next">';
    prevNext += prev ? `<a href="${prev.file}.html" class="prev-link">← ${prev.title}</a>` : '<span></span>';
    prevNext += next ? `<a href="${next.file}.html" class="next-link">${next.title} →</a>` : '<span></span>';
    prevNext += '</nav>';
    fixedContent += prevNext;
    const faqHtml = htmlTemplate(faq.title, fixedContent, 'faqs', '../');
    fs.writeFileSync(`_site/faqs/${faq.file}.html`, faqHtml);
  });

  console.log(`✓ Created ${faqs.length} individual FAQ pages`);
}

// Build Pattern Recognition page
if (fs.existsSync('doc/pattern_recognition.md')) {
  const patternRaw = fs.readFileSync('doc/pattern_recognition.md', 'utf8');
  let patternHtml = md.render(patternRaw);
  patternHtml = processLinks(patternHtml);
  patternHtml = ensureHeadingIds(patternHtml);
  const patternToc = generateTOC(patternHtml);

  // Fix cheatsheet links to be relative to root
  patternHtml = patternHtml.replace(/href="cheatsheets\//g, 'href="cheatsheets/');

  const patternContent = `
    <div class="cheatsheet-header">
      <h1>Pattern Recognition Guide</h1>
      <p>Map problem keywords to algorithm patterns — the fastest way to crack coding interviews.</p>
    </div>
    ${patternToc}
    <div class="cheatsheet-content">${patternHtml}</div>
  `;
  fs.writeFileSync('_site/patterns.html', htmlTemplate('Pattern Recognition', patternContent, 'patterns'));
  console.log('✓ Created patterns.html');
}

console.log('✓ Website built successfully!');
