#!/usr/bin/env node
const fs = require('fs');
const path = require('path');

// Create _site/data directory
if (!fs.existsSync('_site/data')) {
  fs.mkdirSync('_site/data', { recursive: true });
}

// Copy doc/pic if it exists
if (fs.existsSync('doc/pic')) {
  const copyDir = (src, dest) => {
    if (!fs.existsSync(dest)) fs.mkdirSync(dest, { recursive: true });
    fs.readdirSync(src).forEach(file => {
      const srcPath = path.join(src, file);
      const destPath = path.join(dest, file);
      fs.statSync(srcPath).isDirectory()
        ? copyDir(srcPath, destPath)
        : fs.copyFileSync(srcPath, destPath);
    });
  };
  copyDir('doc/pic', '_site/doc/pic');
  console.log('✓ Copied doc/pic');
}

// Create a template function for pages
const createPageTemplate = (title, content, currentPage = 'home', basePath = '') => {
  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${title} - CS Basics</title>
  <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>CS</text></svg>">
  <style>
    * { box-sizing: border-box; }
    body { 
      margin: 0; 
      padding: 0; 
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
      line-height: 1.6;
      color: #333;
      background: #f5f5f5;
    }
    nav {
      background: white;
      border-bottom: 1px solid #ddd;
      padding: 1rem 2rem;
      position: sticky;
      top: 0;
      z-index: 100;
    }
    .nav-container {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
      gap: 2rem;
    }
    .logo {
      font-size: 1.3rem;
      font-weight: 700;
      text-decoration: none;
      color: #333;
    }
    .nav-links {
      display: flex;
      gap: 1.5rem;
      list-style: none;
      margin: 0;
      padding: 0;
    }
    .nav-links a {
      color: #666;
      text-decoration: none;
      padding: 0.5rem 0.75rem;
      border-bottom: 2px solid transparent;
      transition: all 0.2s;
    }
    .nav-links a:hover,
    .nav-links a.active {
      color: #333;
      border-bottom-color: #333;
    }
    .container {
      max-width: 1200px;
      margin: 2rem auto;
      padding: 0 2rem;
      background: white;
      padding: 2rem;
      border-radius: 8px;
      min-height: 60vh;
    }
    h1 { margin-top: 0; }
    footer {
      text-align: center;
      padding: 2rem;
      color: #666;
      font-size: 0.9rem;
      margin-top: 4rem;
      border-top: 1px solid #ddd;
    }
  </style>
</head>
<body>
  <nav>
    <div class="nav-container">
      <a href="${basePath}index.html" class="logo">CS Basics</a>
      <div class="nav-links">
        <a href="${basePath}index.html" class="${currentPage === 'home' ? 'active' : ''}">Home</a>
        <a href="${basePath}cheatsheets.html" class="${currentPage === 'cheatsheets' ? 'active' : ''}">Cheat Sheets</a>
        <a href="${basePath}patterns.html" class="${currentPage === 'patterns' ? 'active' : ''}">Patterns</a>
        <a href="${basePath}faqs.html" class="${currentPage === 'faqs' ? 'active' : ''}">FAQs</a>
        <a href="${basePath}resources.html" class="${currentPage === 'resources' ? 'active' : ''}">Resources</a>
        <a href="${basePath}lc-explorer.html" class="${currentPage === 'lc-explorer' ? 'active' : ''}">LC Explorer</a>
        <a href="https://github.com/yennanliu/CS_basics" target="_blank">GitHub</a>
      </div>
    </div>
  </nav>
  <div class="container">
    ${content}
  </div>
  <footer>
    <p>&copy; 2026 CS Basics. <a href="https://github.com/yennanliu/CS_basics" style="color: #666;">Open source on GitHub</a>.</p>
  </footer>
</body>
</html>`;
};

// Create home page
const homeContent = `
<h1>CS Basics</h1>
<p>Welcome to CS Basics - your comprehensive computer science fundamentals repository.</p>
<h2>Quick Links</h2>
<ul>
  <li><a href="lc-explorer.html">LeetCode Explorer</a> - Interactive explorer for LeetCode problems</li>
  <li><a href="cheatsheets.html">Cheat Sheets</a> - Quick reference guides</li>
  <li><a href="patterns.html">Pattern Recognition</a> - Algorithm patterns and techniques</li>
  <li><a href="faqs.html">FAQs</a> - Frequently asked questions</li>
  <li><a href="resources.html">Resources</a> - Study materials and links</li>
</ul>
<h2>Featured Sections</h2>
<ul>
  <li><strong>LeetCode Problems</strong> - Java, Python, SQL implementations</li>
  <li><strong>System Design</strong> - Patterns and case studies</li>
  <li><strong>Data Structures</strong> - Implementations and analysis</li>
  <li><strong>Algorithms</strong> - Classic algorithms and techniques</li>
</ul>
<p><a href="https://github.com/yennanliu/CS_basics" target="_blank">View on GitHub →</a></p>
`;

// Create other placeholder pages
const pages = [
  { file: 'index.html', title: 'Home', current: 'home', content: homeContent },
  { file: 'cheatsheets.html', title: 'Cheat Sheets', current: 'cheatsheets', content: '<h1>Cheat Sheets</h1><p>Quick reference guides for algorithms and data structures. <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/cheatsheet">View all on GitHub</a>.</p>' },
  { file: 'patterns.html', title: 'Pattern Recognition', current: 'patterns', content: '<h1>Pattern Recognition</h1><p>Common algorithm patterns and techniques. <a href="https://github.com/yennanliu/CS_basics/blob/master/doc/pattern_recognition.md">View full guide</a>.</p>' },
  { file: 'faqs.html', title: 'FAQs', current: 'faqs', content: '<h1>Frequently Asked Questions</h1><p>Common questions about interview preparation and problem solving. <a href="https://github.com/yennanliu/CS_basics/tree/master/doc/faq">View all FAQs</a>.</p>' },
  { file: 'resources.html', title: 'Resources', current: 'resources', content: '<h1>Resources</h1><p>Curated list of study materials and learning resources. <a href="https://github.com/yennanliu/CS_basics">View on GitHub</a>.</p>' }
];

pages.forEach(page => {
  const html = createPageTemplate(page.title, page.content, page.current);
  fs.writeFileSync(`_site/${page.file}`, html);
  console.log(`✓ Created ${page.file}`);
});

console.log('\n✅ Site build complete!');
