#!/usr/bin/env node

/**
 * Ensure navigation pages exist in _site directory
 * Run after build-leetcode.js to generate missing HTML pages
 */

const fs = require('fs');
const path = require('path');

// Ensure _site directory exists
if (!fs.existsSync('_site')) {
  fs.mkdirSync('_site', { recursive: true });
}

const pageTemplate = (title, currentPage) => {
  const pages = [
    { file: 'index.html', label: 'Home', id: 'home' },
    { file: 'cheatsheets.html', label: 'Cheat Sheets', id: 'cheatsheets' },
    { file: 'patterns.html', label: 'Patterns', id: 'patterns' },
    { file: 'faqs.html', label: 'FAQs', id: 'faqs' },
    { file: 'resources.html', label: 'Resources', id: 'resources' },
    { file: 'leetcode.html', label: 'LC Explorer', id: 'leetcode' }
  ];

  const navLinks = pages
    .map(p => {
      const isActive = p.id === currentPage ? ' class="active"' : '';
      return `        <a href="${p.file}"${isActive}>${p.label}</a>`;
    })
    .join('\n');

  return `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${title} - CS Basics</title>
  <link rel="icon" href="data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><text y='.9em' font-size='90'>CS</text></svg>">
  <style>
    * { box-sizing: border-box; }
    body { margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif; line-height: 1.6; color: #333; background: #f5f5f5; }
    nav { background: white; border-bottom: 1px solid #ddd; padding: 1rem 2rem; position: sticky; top: 0; z-index: 100; }
    .nav-container { max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center; gap: 2rem; }
    .logo { font-size: 1.3rem; font-weight: 700; text-decoration: none; color: #333; }
    nav a { color: #666; text-decoration: none; padding: 0.5rem 0.75rem; border-bottom: 2px solid transparent; transition: all 0.2s; margin-right: 0.5rem; display: inline-block; }
    nav a:hover, nav a.active { color: #333; border-bottom-color: #333; }
    .container { max-width: 1200px; margin: 2rem auto; padding: 2rem; background: white; border-radius: 8px; min-height: 60vh; }
    h1 { margin-top: 0; }
    footer { text-align: center; padding: 2rem; color: #666; font-size: 0.9rem; margin-top: 4rem; border-top: 1px solid #ddd; }
    a { color: #0066cc; }
    a:hover { text-decoration: underline; }
    .github-link { margin-left: 1rem; }
  </style>
</head>
<body>
  <nav>
    <div class="nav-container">
      <a href="index.html" class="logo">CS Basics</a>
      <div>
${navLinks}
        <a href="https://github.com/yennanliu/CS_basics" target="_blank" class="github-link">GitHub</a>
      </div>
    </div>
  </nav>
  <div class="container">
    <h1>${title}</h1>
    <p><a href="https://github.com/yennanliu/CS_basics">View on GitHub →</a></p>
  </div>
  <footer>
    <p>&copy; 2026 CS Basics. <a href="https://github.com/yennanliu/CS_basics">Open source on GitHub</a>.</p>
  </footer>
</body>
</html>`;
};

const pages = [
  { file: 'index.html', title: 'Home', id: 'home' },
  { file: 'cheatsheets.html', title: 'Cheat Sheets', id: 'cheatsheets' },
  { file: 'patterns.html', title: 'Pattern Recognition', id: 'patterns' },
  { file: 'faqs.html', title: 'Frequently Asked Questions', id: 'faqs' },
  { file: 'resources.html', title: 'Resources', id: 'resources' }
];

let created = 0;
pages.forEach(p => {
  const filePath = path.join('_site', p.file);
  if (!fs.existsSync(filePath)) {
    const html = pageTemplate(p.title, p.id);
    fs.writeFileSync(filePath, html);
    console.log(`✓ Created ${p.file}`);
    created++;
  }
});

if (created === 0) {
  console.log('✓ All navigation pages already exist');
} else {
  console.log(`\n✓ Generated ${created} navigation page(s)`);
}
