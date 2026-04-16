#!/usr/bin/env node

/**
 * Build LeetCode Explorer page from doc/google_leetcode_problems_by_tags.md
 * Generates _site/leetcode.html with embedded data and interactive features
 */

const fs = require('fs');
const path = require('path');

// Parse markdown file
function parseProblems() {
  const content = fs.readFileSync('doc/google_leetcode_problems_by_tags.md', 'utf8');
  const lines = content.split('\n');

  const problems = new Map(); // id -> {id, title, tags, difficulty, acceptance}
  const tagMap = new Map(); // tag -> {Easy: [ids], Medium: [ids], Hard: [ids]}

  let currentTag = null;
  let currentDiff = null;

  for (const line of lines) {
    // Tag sections: ## Array
    if (line.startsWith('## ')) {
      currentTag = line.slice(3).trim();
      if (currentTag && !tagMap.has(currentTag)) {
        tagMap.set(currentTag, { Easy: [], Medium: [], Hard: [] });
      }
      continue;
    }

    // Difficulty subsections: ### Easy
    if (line.startsWith('### ')) {
      currentDiff = line.slice(4).trim();
      continue;
    }

    // Problem entries: - #66 - Plus One - Array, Math - 42.5%
    if (line.startsWith('- #')) {
      const match = line.match(/^- #(\d+)\s*-\s*(.+?)\s*-\s*(.+?)\s*-\s*([\d.]+)%/);
      if (match) {
        const [, idStr, title, tagsRaw, accStr] = match;
        const id = idStr;
        const tags = tagsRaw.split(',').map(t => t.trim()).filter(Boolean);
        const acceptance = parseFloat(accStr);

        // Store problem
        if (!problems.has(id)) {
          problems.set(id, {
            id,
            title: title.trim(),
            tags,
            difficulty: currentDiff || 'Unknown',
            acceptance
          });
        }

        // Add to tagMap
        if (currentTag && currentDiff && tagMap.has(currentTag)) {
          const diffKey = currentDiff; // 'Easy', 'Medium', 'Hard'
          if (!tagMap.get(currentTag)[diffKey]) {
            tagMap.get(currentTag)[diffKey] = [];
          }
          tagMap.get(currentTag)[diffKey].push(id);
        }
      }
    }
  }

  return { problems, tagMap };
}

// Compute tag co-occurrence matrix
function buildCoMatrix(problems) {
  const coMatrix = {}; // tagA -> { tagB: count }

  for (const problem of problems.values()) {
    const uniqueTags = [...new Set(problem.tags)];
    for (let i = 0; i < uniqueTags.length; i++) {
      for (let j = i + 1; j < uniqueTags.length; j++) {
        const [a, b] = [uniqueTags[i], uniqueTags[j]].sort();
        if (!coMatrix[a]) coMatrix[a] = {};
        coMatrix[a][b] = (coMatrix[a][b] || 0) + 1;
      }
    }
  }

  return coMatrix;
}

// Minimal htmlTemplate matching the existing deploy-pages.yml structure
function htmlTemplate(title, body) {
  const basePath = './';
  const currentPage = 'leetcode';

  return `<!DOCTYPE html>
<html lang="en" data-theme="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${title} - CS Basics</title>
  <meta name="description" content="Interactive LeetCode problems explorer for interview preparation">
  <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>💻</text></svg>">
  ${generateCSS()}
  <script src="https://cdnjs.cloudflare.com/ajax/libs/d3/7.8.5/d3.min.js"></script>
</head>
<body>
  <nav class="navbar">
    <div class="nav-container">
      <a href="${basePath}index.html" class="logo">CS_basics</a>
      <button class="nav-toggle" onclick="document.querySelector('.nav-links').classList.toggle('open')" aria-label="Toggle menu">
        <span></span><span></span><span></span>
      </button>
      <div class="nav-links">
        <a href="${basePath}index.html" class="">Home</a>
        <div class="nav-study-group">
          <span class="nav-study-label">Study</span>
          <a href="${basePath}cheatsheets.html" class="">Cheat Sheets</a>
          <a href="${basePath}patterns.html" class="">Pattern Recognition</a>
          <a href="${basePath}leetcode.html" class="active">LC Explorer</a>
          <a href="${basePath}faqs.html" class="">FAQs</a>
          <a href="${basePath}resources.html" class="">Resources</a>
        </div>
        <a href="${basePath}algo_demo/index.html" class="">Visualizer</a>
        <button id="theme-toggle" class="theme-toggle" aria-label="Toggle dark mode" onclick="toggleTheme()">🌙</button>
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
      ${body}
    </div>
  </main>

  <footer class="footer">
    <p>&copy; 2024 CS Basics. Open source on <a href="https://github.com/yennanliu/CS_basics">GitHub</a>.</p>
  </footer>

  <script>
    function toggleTheme() {
      const html = document.documentElement;
      const current = html.getAttribute('data-theme') || 'light';
      const next = current === 'light' ? 'dark' : 'light';
      html.setAttribute('data-theme', next);
      localStorage.setItem('theme', next);
    }

    // Restore theme from localStorage
    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);
  </script>
</body>
</html>`;
}

// Generate complete CSS
function generateCSS() {
  return `<style>
/* Design tokens */
:root {
  --bg-color: #fafafa;
  --surface: #fff;
  --text-color: #1a1a2e;
  --text-light: #555;
  --border-color: #e0e0e0;
  --accent-color: #555;
  --shadow: 0 2px 8px rgba(0,0,0,.08);
  --shadow-md: 0 4px 12px rgba(0,0,0,.12);
  --radius: 8px;
  --font: 'Inter', system-ui, -apple-system, sans-serif;
  --mono: 'JetBrains Mono', 'Fira Code', monospace;
}

[data-theme="dark"] {
  --bg-color: #0f0f1a;
  --surface: #1a1a2e;
  --text-color: #e0e0e0;
  --text-light: #aaa;
  --border-color: #2a2a3e;
  --accent-color: #666;
  --shadow: 0 2px 8px rgba(0,0,0,.3);
}

/* Global */
* { box-sizing: border-box; }
html, body { margin: 0; padding: 0; }
body {
  font-family: var(--font);
  background: var(--bg-color);
  color: var(--text-color);
  line-height: 1.6;
  transition: background .3s, color .3s;
}

a { color: var(--text-color); text-decoration: none; }
a:hover { text-decoration: underline; }

/* Navbar */
.navbar {
  background: var(--surface);
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 100;
  box-shadow: var(--shadow);
}

.nav-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 0.75rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 1.1rem;
  font-weight: 700;
  margin-right: 2rem;
  color: var(--text-color);
}

.nav-links {
  display: flex;
  gap: 0;
  align-items: center;
  flex-wrap: wrap;
}

.nav-links a, .nav-study-label, .theme-toggle {
  padding: 0.5rem 0.75rem;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.nav-links a {
  color: var(--text-light);
  border-bottom: 2px solid transparent;
  cursor: pointer;
}

.nav-links a:hover, .nav-links a.active {
  color: var(--text-color);
  border-bottom-color: var(--text-color);
}

.nav-study-group {
  display: flex;
  align-items: center;
  gap: 0;
  border-left: 1px solid var(--border-color);
  border-right: 1px solid var(--border-color);
  padding: 0 0.5rem;
}

.nav-study-label {
  font-size: 0.75rem;
  color: var(--text-light);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.github-link { margin-left: 1rem; }
.github-link svg { transition: opacity .2s; }
.github-link:hover svg { opacity: 0.7; }

.nav-toggle, .theme-toggle {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.5rem;
  color: var(--text-color);
}

@media (max-width: 768px) {
  .nav-toggle { display: block; }
  .nav-links { display: none; flex-direction: column; width: 100%; }
  .nav-links.open { display: flex; }
  .nav-study-group { border: none; padding: 0; }
}

/* Container */
.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 2rem;
}

.content { min-height: 60vh; }

/* LC Page Styles */
.lc-hero {
  text-align: center;
  margin-bottom: 3rem;
  padding: 2rem 0;
}

.lc-hero h1 {
  font-size: 2.5rem;
  margin: 0 0 0.5rem;
  color: var(--text-color);
}

.lc-hero p {
  font-size: 1.1rem;
  color: var(--text-light);
  margin: 0 0 1.5rem;
}

.lc-stats-bar {
  display: flex;
  justify-content: center;
  gap: 2rem;
  flex-wrap: wrap;
}

.stat {
  display: inline-block;
  padding: 0.75rem 1.5rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-size: 0.9rem;
  color: var(--text-light);
}

.stat strong {
  display: block;
  font-size: 1.4rem;
  color: var(--text-color);
  margin-top: 0.25rem;
}

/* Tabs */
.lc-tabs {
  display: flex;
  gap: 0;
  border-bottom: 2px solid var(--border-color);
  margin-bottom: 2rem;
  overflow-x: auto;
}

.lc-tab {
  background: none;
  border: none;
  padding: 0.75rem 1.5rem;
  font-family: var(--font);
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--text-light);
  cursor: pointer;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  transition: all 0.2s;
  white-space: nowrap;
}

.lc-tab.active, .lc-tab:hover {
  color: var(--text-color);
  border-bottom-color: var(--text-color);
}

/* Views */
.lc-view {
  display: none;
}

.lc-view.active {
  display: block;
}

/* Filter View */
.lc-filter-controls {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 1.5rem;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  position: sticky;
  top: 60px;
  z-index: 50;
}

.lc-filter-count-badge {
  display: inline-block;
  background: var(--text-color);
  color: var(--bg-color);
  border-radius: 12px;
  padding: 0.15rem 0.5rem;
  font-size: 0.75rem;
  font-weight: 600;
  margin-left: 0.5rem;
}

.lc-tag-search {
  margin-bottom: 0.75rem;
}

.lc-tag-search input {
  width: 100%;
  padding: 0.4rem 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-family: var(--font);
  font-size: 0.85rem;
  background: var(--bg-color);
  color: var(--text-color);
}

.lc-acc-range-slider {
  display: flex;
  gap: 1rem;
  align-items: center;
  margin-top: 1rem;
  font-size: 0.85rem;
}

.lc-acc-range-slider input[type="range"] {
  flex: 1;
}

.lc-col-header {
  cursor: pointer;
  user-select: none;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  font-weight: 600;
}

.lc-col-header:hover {
  color: var(--text-color);
}

.lc-col-header .sort-indicator {
  font-size: 0.7rem;
  display: inline-block;
}

.lc-pagination {
  display: flex;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 1.5rem;
  flex-wrap: wrap;
}

.lc-pagination button {
  padding: 0.4rem 0.8rem;
  border: 1px solid var(--border-color);
  background: var(--surface);
  color: var(--text-color);
  border-radius: var(--radius);
  cursor: pointer;
  font-family: var(--font);
  font-size: 0.85rem;
  transition: all 0.2s;
}

.lc-pagination button:hover {
  background: var(--border-color);
}

.lc-pagination button.active {
  background: var(--text-color);
  color: var(--bg-color);
}

.lc-search-box input {
  width: 100%;
  padding: 0.6rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-family: var(--font);
  font-size: 0.95rem;
  background: var(--bg-color);
  color: var(--text-color);
}

.lc-diff-filters, .lc-tag-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.lc-btn {
  padding: 0.4rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: 20px;
  background: var(--bg-color);
  color: var(--text-color);
  cursor: pointer;
  font-family: var(--font);
  font-size: 0.85rem;
  transition: all 0.2s;
}

.lc-btn:hover {
  background: var(--border-color);
}

.lc-btn.active {
  background: var(--text-color);
  color: var(--bg-color);
  border-color: var(--text-color);
}

.lc-btn.favorite {
  position: relative;
}

.lc-btn .tag-count {
  display: inline-block;
  background: var(--text-color);
  color: var(--bg-color);
  border-radius: 10px;
  padding: 0 0.3rem;
  font-size: 0.65rem;
  font-weight: 600;
  margin-left: 0.3rem;
}

.lc-btn.active .tag-count {
  background: var(--bg-color);
  color: var(--text-color);
}

.favorite-star {
  position: absolute;
  top: -4px;
  right: -4px;
  font-size: 0.9rem;
  cursor: pointer;
  opacity: 0.5;
  transition: opacity 0.2s;
}

.favorite-star.active {
  opacity: 1;
}

/* Difficulty badges */
.diff-badge {
  display: inline-block;
  padding: 0.2rem 0.6rem;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.diff-badge.easy { background: #1e7e34; color: #fff; }
.diff-badge.medium { background: #b45309; color: #fff; }
.diff-badge.hard { background: #b91c1c; color: #fff; }

[data-theme="dark"] .diff-badge.easy { background: #166534; }
[data-theme="dark"] .diff-badge.medium { background: #92400e; }
[data-theme="dark"] .diff-badge.hard { background: #991b1b; }

/* Problem List */
.lc-problem-list {
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  overflow: hidden;
}

.lc-problem-row {
  display: grid;
  grid-template-columns: 3.5rem 1fr auto 12rem 3rem;
  gap: 0.75rem;
  align-items: center;
  padding: 0.8rem 1rem;
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
  transition: background 0.15s;
  font-size: 0.9rem;
  background: var(--surface);
}

.lc-problem-row:hover {
  background: var(--bg-color);
}

.lc-problem-row:last-child {
  border-bottom: none;
}

.prob-id {
  color: var(--text-light);
  font-family: var(--mono);
  font-weight: 600;
}

.prob-tags {
  color: var(--text-light);
  font-size: 0.8rem;
}

.prob-acc {
  color: var(--text-light);
  font-size: 0.8rem;
  text-align: right;
}

/* Pattern View */
.lc-pattern-accordion {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.lc-pattern-item {
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  overflow: hidden;
}

.lc-pattern-header {
  padding: 1rem;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--bg-color);
  border-bottom: 1px solid var(--border-color);
  user-select: none;
}

.lc-pattern-header:hover {
  background: var(--border-color);
}

.lc-pattern-header.open {
  border-bottom: none;
}

.lc-pattern-content {
  display: none;
  padding: 1rem;
  max-height: 500px;
  overflow-y: auto;
}

.lc-pattern-content.open {
  display: block;
}

.lc-co-tag {
  margin-bottom: 0.75rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.lc-co-bar {
  height: 20px;
  background: var(--border-color);
  border-radius: 4px;
  overflow: hidden;
  flex: 1;
  max-width: 200px;
}

.lc-co-bar-fill {
  height: 100%;
  background: var(--text-color);
  transition: width 0.3s;
}

/* Mind Map */
.mindmap-container {
  width: 100%;
  height: 600px;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background: var(--bg-color);
  position: relative;
}

.mindmap-controls {
  margin-bottom: 1rem;
  padding: 1rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  display: flex;
  gap: 1.5rem;
  flex-wrap: wrap;
  align-items: center;
}

.mindmap-controls label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
}

.mindmap-controls input[type="range"] {
  width: 150px;
}

.mindmap-legend {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  font-size: 0.8rem;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
}

.legend-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

/* Random Picker */
.lc-picker-container {
  max-width: 600px;
  margin: 0 auto;
}

/* Batch picker list */
.lc-batch-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.lc-batch-row {
  display: grid;
  grid-template-columns: 3.5rem 1fr auto 10rem 3rem auto;
  gap: 0.75rem;
  align-items: center;
  padding: 0.85rem 1rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-size: 0.9rem;
  transition: background 0.15s;
}

.lc-batch-row:hover { background: var(--bg-color); }

.lc-batch-row .batch-done-btn {
  padding: 0.3rem 0.7rem;
  font-size: 0.8rem;
  background: var(--bg-color);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  cursor: pointer;
  color: var(--text-color);
  font-family: var(--font);
  white-space: nowrap;
}

.lc-batch-row .batch-done-btn:hover { background: var(--border-color); }
.lc-batch-row.done { opacity: 0.45; }

/* Similar Problems */
.lc-similar-container { max-width: 700px; margin: 0 auto; }

.lc-similar-search {
  display: flex;
  gap: 0.75rem;
  align-items: center;
  flex-wrap: wrap;
  padding: 1.25rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  margin-bottom: 1.5rem;
}

.lc-similar-search input[type="number"] {
  width: 110px;
  padding: 0.55rem 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background: var(--bg-color);
  color: var(--text-color);
  font-family: var(--font);
  font-size: 0.95rem;
}

.lc-similar-search input[type="text"] {
  flex: 1;
  min-width: 160px;
  padding: 0.55rem 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background: var(--bg-color);
  color: var(--text-color);
  font-family: var(--font);
  font-size: 0.95rem;
}

.lc-source-card {
  padding: 1rem 1.25rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-left: 4px solid var(--text-color);
  border-radius: var(--radius);
  margin-bottom: 1.25rem;
  font-size: 0.9rem;
}

.lc-similar-results {
  display: flex;
  flex-direction: column;
  gap: 0.6rem;
}

.lc-similar-row {
  display: grid;
  grid-template-columns: 3.5rem 1fr auto 9rem 3rem auto;
  gap: 0.75rem;
  align-items: center;
  padding: 0.8rem 1rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-size: 0.9rem;
  cursor: pointer;
  transition: background 0.15s;
}

.lc-similar-row:hover { background: var(--bg-color); }

.lc-sim-score {
  font-size: 0.75rem;
  color: var(--text-light);
  font-family: var(--mono);
  white-space: nowrap;
}

.lc-similar-empty {
  text-align: center;
  padding: 3rem;
  color: var(--text-light);
  font-size: 0.95rem;
}

.lc-similar-sort {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.lc-tag-match {
  display: inline-block;
  padding: 0.2rem 0.5rem;
  background: var(--bg-color);
  border-radius: 4px;
  font-size: 0.7rem;
  color: var(--text-light);
  margin-right: 0.3rem;
}

.lc-tag-match.matched {
  background: var(--border-color);
  font-weight: 600;
}

.lc-export-controls {
  display: flex;
  gap: 0.75rem;
  flex-wrap: wrap;
  margin-bottom: 1.5rem;
}

.lc-export-btn {
  padding: 0.5rem 1rem;
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  color: var(--text-color);
  cursor: pointer;
  font-family: var(--font);
  font-size: 0.85rem;
  transition: all 0.2s;
}

.lc-export-btn:hover {
  background: var(--border-color);
}

.lc-picker-exclude {
  display: flex;
  gap: 0.75rem;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}

.lc-picker-exclude select {
  padding: 0.4rem 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background: var(--surface);
  color: var(--text-color);
  font-family: var(--font);
  font-size: 0.85rem;
}

.loading-spinner {
  display: inline-block;
  width: 16px;
  height: 16px;
  border: 2px solid var(--border-color);
  border-top-color: var(--text-color);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@media (max-width: 768px) {
  .lc-batch-row {
    grid-template-columns: 3rem 1fr;
    gap: 0.5rem;
  }
  .lc-batch-row > *:nth-child(3),
  .lc-batch-row > *:nth-child(4),
  .lc-batch-row > *:nth-child(5) { display: none; }

  .lc-similar-row {
    grid-template-columns: 3rem 1fr;
    gap: 0.5rem;
  }
  .lc-similar-row > *:nth-child(3),
  .lc-similar-row > *:nth-child(4),
  .lc-similar-row > *:nth-child(5) { display: none; }
}

.lc-picker-controls {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  align-items: center;
}

.lc-picker-controls select {
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  background: var(--surface);
  color: var(--text-color);
  font-family: var(--font);
}

.lc-picker-card {
  background: var(--surface);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  padding: 2.5rem;
  text-align: center;
  box-shadow: var(--shadow-md);
  margin-bottom: 1.5rem;
}

.lc-picker-card .prob-number {
  font-size: 0.95rem;
  color: var(--text-light);
  font-family: var(--mono);
}

.lc-picker-card .prob-name {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0.75rem 0;
  color: var(--text-color);
}

.lc-picker-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 1.5rem;
  flex-wrap: wrap;
}

.btn-primary {
  padding: 0.6rem 1.5rem;
  background: var(--text-color);
  color: var(--bg-color);
  border: none;
  border-radius: var(--radius);
  font-weight: 600;
  cursor: pointer;
  font-family: var(--font);
  transition: opacity 0.2s;
}

.btn-primary:hover {
  opacity: 0.9;
}

.btn-secondary {
  padding: 0.6rem 1.5rem;
  background: var(--surface);
  color: var(--text-color);
  border: 1px solid var(--border-color);
  border-radius: var(--radius);
  font-weight: 500;
  cursor: pointer;
  font-family: var(--font);
  transition: all 0.2s;
}

.btn-secondary:hover {
  background: var(--border-color);
}

.lc-progress-section {
  margin-top: 2rem;
}

.lc-progress-bar {
  width: 100%;
  height: 8px;
  background: var(--border-color);
  border-radius: 4px;
  overflow: hidden;
  margin-top: 0.5rem;
}

.lc-progress-fill {
  height: 100%;
  background: var(--text-color);
  transition: width 0.3s;
}

.lc-recent-problems {
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--border-color);
}

.lc-recent-problems a {
  display: inline-block;
  margin-right: 0.5rem;
  margin-bottom: 0.5rem;
  padding: 0.25rem 0.6rem;
  background: var(--bg-color);
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-size: 0.85rem;
  transition: all 0.2s;
}

.lc-recent-problems a:hover {
  background: var(--border-color);
  text-decoration: none;
}

/* Footer */
.footer {
  text-align: center;
  padding: 2rem;
  border-top: 1px solid var(--border-color);
  color: var(--text-light);
  font-size: 0.85rem;
  margin-top: 3rem;
}

.footer a {
  color: var(--text-light);
}

@media (max-width: 768px) {
  .lc-filter-controls {
    grid-template-columns: 1fr;
  }

  .lc-problem-row {
    grid-template-columns: 3rem 1fr;
    gap: 0.5rem;
  }

  .lc-problem-row > *:nth-child(3),
  .lc-problem-row > *:nth-child(4),
  .lc-problem-row > *:nth-child(5) {
    display: none;
  }

  .lc-hero h1 {
    font-size: 1.8rem;
  }

  .lc-stats-bar {
    gap: 1rem;
  }

  .stat {
    padding: 0.5rem 1rem;
    font-size: 0.8rem;
  }
}
</style>`;
}

// Generate the main page content/body HTML
function generatePageBody(problems, tagMap, coMatrix) {
  const easyCount = [...problems.values()].filter(p => p.difficulty === 'Easy').length;
  const mediumCount = [...problems.values()].filter(p => p.difficulty === 'Medium').length;
  const hardCount = [...problems.values()].filter(p => p.difficulty === 'Hard').length;

  const problemsJSON = JSON.stringify(Object.fromEntries(problems), null, 2);
  const tagMapJSON = JSON.stringify(
    Object.fromEntries(
      Array.from(tagMap.entries()).map(([tag, diffs]) => [
        tag,
        {
          Easy: diffs.Easy || [],
          Medium: diffs.Medium || [],
          Hard: diffs.Hard || []
        }
      ])
    ),
    null,
    2
  );
  const coMatrixJSON = JSON.stringify(coMatrix, null, 2);

  return `<div id="lc-page">
  <div class="lc-hero">
    <h1>LeetCode Explorer</h1>
    <p>Interactive tool for exploring ${problems.size} Google LeetCode problems across ${tagMap.size} categories</p>
    <div class="lc-stats-bar">
      <span class="stat easy">Easy <strong>${easyCount}</strong></span>
      <span class="stat medium">Medium <strong>${mediumCount}</strong></span>
      <span class="stat hard">Hard <strong>${hardCount}</strong></span>
      <span class="stat">Tags <strong>${tagMap.size}</strong></span>
    </div>
  </div>

  <div class="lc-tabs" role="tablist">
    <button class="lc-tab active" data-view="filter" onclick="switchView('filter')">Filter & Browse</button>
    <button class="lc-tab" data-view="patterns" onclick="switchView('patterns')">Pattern Similarity</button>
    <button class="lc-tab" data-view="mindmap" onclick="switchView('mindmap'); initMindMap()">Tag Mind Map</button>
    <button class="lc-tab" data-view="picker" onclick="switchView('picker')">Random Picker</button>
    <button class="lc-tab" data-view="similar" onclick="switchView('similar')">Similar Problems</button>
  </div>

  <!-- Filter & Browse View -->
  <div id="view-filter" class="lc-view active">
    <div class="lc-filter-controls">
      <div>
        <div class="lc-search-box">
          <input type="text" id="lc-search" placeholder="Search by name or number..." onkeyup="applyFilters()">
        </div>
        <div style="margin-top: 1rem;">
          <div style="margin-bottom: 0.75rem; color: var(--text-light); font-size: 0.85rem;">Difficulty:</div>
          <div class="lc-diff-filters">
            <button class="lc-btn" data-diff="Easy" onclick="toggleDifficulty('Easy')">Easy</button>
            <button class="lc-btn" data-diff="Medium" onclick="toggleDifficulty('Medium')">Medium</button>
            <button class="lc-btn" data-diff="Hard" onclick="toggleDifficulty('Hard')">Hard</button>
          </div>
        </div>
        <div style="margin-bottom: 0.75rem; color: var(--text-light); font-size: 0.85rem; margin-top: 1rem;">Acceptance Rate:</div>
        <div class="lc-acc-range-slider">
          <input type="range" id="acc-min" min="0" max="100" value="0" onchange="applyFilters()" style="flex: 1;">
          <span id="acc-min-val" style="min-width: 35px; font-size: 0.8rem;">0%</span>
        </div>
        <div class="lc-acc-range-slider">
          <input type="range" id="acc-max" min="0" max="100" value="100" onchange="applyFilters()" style="flex: 1;">
          <span id="acc-max-val" style="min-width: 35px; font-size: 0.8rem;">100%</span>
        </div>
        <div style="margin-bottom: 0.75rem; color: var(--text-light); font-size: 0.85rem; margin-top: 1rem;">Tags:</div>
        <div class="lc-tag-search">
          <input type="text" id="lc-tag-search" placeholder="Filter tags..." onkeyup="filterTagPills()">
        </div>
        <div class="lc-tag-filters" id="lc-tag-pills"></div>
        <button class="lc-btn" style="width: 100%; margin-top: 1rem;" onclick="clearFilters()">Clear All Filters <span class="lc-filter-count-badge" id="filter-count-badge" style="display:none;">0</span></button>
      </div>
      <div style="display: flex; flex-direction: column; gap: 1rem;">
        <div>
          <label style="display: block; margin-bottom: 0.5rem; color: var(--text-light); font-size: 0.85rem;">Sort:</label>
          <select onchange="applyFilters()" id="lc-sort">
            <option value="id">By Number</option>
            <option value="acceptance">By Acceptance %</option>
            <option value="difficulty">By Difficulty</option>
          </select>
        </div>
        <div style="padding: 0.75rem; background: var(--bg-color); border-radius: var(--radius); font-size: 0.85rem;">
          <strong id="lc-result-count">0</strong> problems
        </div>
      </div>
    </div>
    <div class="lc-problem-list" id="lc-problem-list"></div>
  </div>

  <!-- Pattern Similarity View -->
  <div id="view-patterns" class="lc-view">
    <div class="lc-pattern-accordion" id="lc-pattern-accordion"></div>
  </div>

  <!-- Mind Map View -->
  <div id="view-mindmap" class="lc-view">
    <div class="mindmap-controls">
      <label>Min connections: <input type="range" id="mindmap-threshold" min="5" max="50" value="15" onchange="updateMindMap()"></label>
      <button class="lc-btn" onclick="resetMindMapZoom()">Reset Zoom</button>
      <div class="mindmap-legend" id="mindmap-legend"></div>
    </div>
    <div class="mindmap-container" id="mindmap-container"></div>
  </div>

  <!-- Random Picker View -->
  <div id="view-picker" class="lc-view">
    <div class="lc-picker-container">
      <div class="lc-picker-controls">
        <div>Difficulty:</div>
        <label><input type="checkbox" value="Easy" id="pick-diff-easy" onchange="renderPickerCard()" checked> Easy</label>
        <label><input type="checkbox" value="Medium" id="pick-diff-medium" onchange="renderPickerCard()" checked> Medium</label>
        <label><input type="checkbox" value="Hard" id="pick-diff-hard" onchange="renderPickerCard()" checked> Hard</label>
      </div>
      <div class="lc-picker-controls" style="gap: 1.25rem;">
        <label>Tag: <select id="picker-tag-select" onchange="renderPickerCard()">
          <option value="">All Tags</option>
        </select></label>
        <label style="display:flex; align-items:center; gap:0.5rem;">
          Count:
          <input type="number" id="picker-gen-count" min="1" max="20" value="1"
            style="width:60px; padding:0.4rem 0.5rem; border:1px solid var(--border-color); border-radius:var(--radius); background:var(--bg-color); color:var(--text-color); font-family:var(--font);"
            onchange="renderPickerCard()">
        </label>
        <button class="btn-primary" onclick="renderPickerCard()">Pick</button>
        <button class="btn-secondary" onclick="recommendNext()">Recommend Next</button>
      </div>
      <div class="lc-picker-exclude" id="picker-exclude-tags" style="display:none;">
        <label>Exclude tags:
          <select id="picker-exclude-select" multiple style="min-width:200px; height:60px;">
          </select>
        </label>
      </div>
      <div class="lc-export-controls">
        <button class="lc-export-btn" onclick="exportBatchList()">📥 Export List</button>
        <button class="lc-export-btn" onclick="copyBatchList()">📋 Copy to Clipboard</button>
      </div>
      <div id="lc-picker-card-container"></div>
      <div class="lc-progress-section">
        <div style="font-size: 0.9rem; color: var(--text-light);">Progress: <strong id="picker-count">0</strong> / <span id="picker-total">0</span> seen</div>
        <div class="lc-progress-bar">
          <div class="lc-progress-fill" id="picker-progress" style="width: 0%"></div>
        </div>
        <button class="lc-btn" style="width: 100%; margin-top: 1rem;" onclick="clearPickerHistory()">Clear History</button>
      </div>
      <div class="lc-recent-problems">
        <div style="font-size: 0.85rem; color: var(--text-light); margin-bottom: 0.5rem;">Recently picked:</div>
        <div id="picker-recent"></div>
      </div>
    </div>
  </div>

  <!-- Similar Problems View -->
  <div id="view-similar" class="lc-view">
    <div class="lc-similar-container">
      <div class="lc-similar-search">
        <input type="text" id="similar-problem-input" placeholder="Enter problem # or name (e.g. 10 or Two Sum)..."
          onkeydown="if(event.key==='Enter') findSimilar()">
        <label style="display:flex; align-items:center; gap:0.5rem; white-space:nowrap;">
          Show:
          <input type="number" id="similar-count-input" min="1" max="30" value="5"
            style="width:60px; padding:0.4rem 0.5rem; border:1px solid var(--border-color); border-radius:var(--radius); background:var(--bg-color); color:var(--text-color); font-family:var(--font);">
          results
        </label>
        <button class="btn-primary" onclick="findSimilar()">Find Similar</button>
      </div>
      <div class="lc-similar-sort" id="similar-sort-controls" style="display:none;">
        <button class="lc-btn" data-sort="score" onclick="setSimilarSort('score')">By Similarity</button>
        <button class="lc-btn" data-sort="difficulty" onclick="setSimilarSort('difficulty')">By Difficulty</button>
        <button class="lc-btn" data-sort="acceptance" onclick="setSimilarSort('acceptance')">By Acceptance</button>
      </div>
      <div id="similar-results-area">
        <div class="lc-similar-empty">Enter a problem number or name above to find similar problems.</div>
      </div>
    </div>
  </div>
</div>

<script>
const PROBLEMS_DATA = {
  problems: ${problemsJSON},
  tagMap: ${tagMapJSON},
  coMatrix: ${coMatrixJSON}
};

const TAG_GROUPS = {
  'Array': 0, 'Matrix': 0, 'String': 0,
  'Dynamic Programming': 1, 'Memoization': 1, 'Divide and Conquer': 1,
  'Depth-First Search': 2, 'Breadth-First Search': 2, 'Graph': 2,
  'Tree': 2, 'Binary Tree': 2, 'Binary Search Tree': 2, 'Trie': 2,
  'Binary Search': 3, 'Sorting': 3, 'Two Pointers': 3, 'Sliding Window': 3,
  'Hash Table': 4, 'Design': 4, 'Union Find': 4,
  'Stack': 5, 'Queue': 5, 'Heap (Priority Queue)': 5, 'Monotonic Stack': 5,
  'Math': 6, 'Bit Manipulation': 6, 'Combinatorics': 6,
  'Greedy': 7, 'Backtracking': 7, 'Recursion': 7,
};

function getTagGroup(tag) { return TAG_GROUPS[tag] ?? 7; }

// State
const state = {
  activeView: 'filter',
  filterDiffs: new Set(),
  filterTags: new Set(),
  filterAccMin: 0,
  filterAccMax: 100,
  searchQuery: '',
  searchDebounceTimer: null,
  sortBy: 'id',
  sortDir: 'asc',
  currentPage: 0,
  pageSize: 50,
  pickerDiffs: new Set(['Easy', 'Medium', 'Hard']),
  pickerTag: null,
  pickerExcludeTags: new Set(),
  favoriteTags: new Set(JSON.parse(localStorage.getItem('lc-fav-tags') || '[]')),
  mindmapInitialized: false,
  similarSortBy: 'score'
};

// Tab switching
function switchView(view) {
  state.activeView = view;
  document.querySelectorAll('.lc-view').forEach(v => v.classList.remove('active'));
  document.getElementById('view-' + view).classList.add('active');
  document.querySelectorAll('.lc-tab').forEach(t => t.classList.remove('active'));
  event.target.classList.add('active');
}

// Filter view functions
function initFilterView() {
  const tagContainer = document.getElementById('lc-tag-pills');
  const allTags = [...new Set(Object.values(PROBLEMS_DATA.problems).flatMap(p => p.tags))].sort();

  // Calculate tag counts and difficulty ratings
  const tagStats = {};
  allTags.forEach(tag => {
    const problems = Object.values(PROBLEMS_DATA.problems).filter(p => p.tags.includes(tag));
    const count = problems.length;
    const avgDiff = (problems.filter(p => p.difficulty === 'Easy').length * 1 +
                     problems.filter(p => p.difficulty === 'Medium').length * 2 +
                     problems.filter(p => p.difficulty === 'Hard').length * 3) / count;
    tagStats[tag] = { count, avgDiff };
  });

  // Sort with favorites first
  const favTags = allTags.filter(t => state.favoriteTags.has(t));
  const otherTags = allTags.filter(t => !state.favoriteTags.has(t));
  const sortedTags = [...favTags, ...otherTags];

  sortedTags.forEach(tag => {
    const container = document.createElement('div');
    container.style.position = 'relative';
    container.style.display = 'inline-block';

    const btn = document.createElement('button');
    btn.className = 'lc-btn';
    btn.dataset.tag = tag;
    btn.innerHTML = \`\${tag}<span class="tag-count">\${tagStats[tag].count}</span>\`;
    btn.onclick = (e) => {
      if (e.target.classList.contains('favorite-star')) return;
      btn.classList.toggle('active');
      if (btn.classList.contains('active')) {
        state.filterTags.add(tag);
      } else {
        state.filterTags.delete(tag);
      }
      applyFilters();
    };

    const star = document.createElement('span');
    star.className = 'favorite-star';
    star.textContent = '★';
    if (state.favoriteTags.has(tag)) star.classList.add('active');
    star.onclick = (e) => {
      e.stopPropagation();
      if (state.favoriteTags.has(tag)) {
        state.favoriteTags.delete(tag);
        star.classList.remove('active');
      } else {
        state.favoriteTags.add(tag);
        star.classList.add('active');
      }
      localStorage.setItem('lc-fav-tags', JSON.stringify([...state.favoriteTags]));
    };

    container.appendChild(btn);
    container.appendChild(star);
    tagContainer.appendChild(container);
  });

  // Initialize picker tag select and exclude select
  const pickerTagSelect = document.getElementById('picker-tag-select');
  const pickerExcludeSelect = document.getElementById('picker-exclude-select');
  allTags.forEach(tag => {
    const option = document.createElement('option');
    option.value = tag;
    option.textContent = tag;
    pickerTagSelect.appendChild(option);

    const excludeOption = document.createElement('option');
    excludeOption.value = tag;
    excludeOption.textContent = tag;
    pickerExcludeSelect.appendChild(excludeOption);
  });

  // Initialize patterns view
  initPatternView();

  // Pick initial problem for picker
  renderPickerCard();

  applyFilters();
}

function toggleDifficulty(diff) {
  const btn = document.querySelector(\`[data-diff="\${diff}"]\`);
  btn.classList.toggle('active');
  if (btn.classList.contains('active')) {
    state.filterDiffs.add(diff);
  } else {
    state.filterDiffs.delete(diff);
  }
  applyFilters();
}

function clearFilters() {
  state.filterDiffs.clear();
  state.filterTags.clear();
  document.getElementById('lc-search').value = '';
  document.getElementById('lc-tag-search').value = '';
  state.searchQuery = '';
  document.getElementById('acc-min').value = 0;
  document.getElementById('acc-max').value = 100;
  document.querySelectorAll('.lc-btn').forEach(b => b.classList.remove('active'));
  document.querySelectorAll('[data-tag]').forEach(b => b.style.display = '');
  applyFilters();
}

function filterTagPills() {
  const query = document.getElementById('lc-tag-search').value.toLowerCase();
  document.querySelectorAll('[data-tag]').forEach(btn => {
    const tag = btn.dataset.tag;
    btn.style.display = tag.toLowerCase().includes(query) ? '' : 'none';
  });
}

function sortProblems(field) {
  // This will be called by column headers, we can implement client-side sorting
  const query = document.getElementById('lc-search').value.toLowerCase();
  let results = Object.values(PROBLEMS_DATA.problems).filter(p => {
    const diffMatch = state.filterDiffs.size === 0 || state.filterDiffs.has(p.difficulty);
    const tagMatch = state.filterTags.size === 0 || p.tags.some(t => state.filterTags.has(t));
    const textMatch = !query || p.title.toLowerCase().includes(query) || p.id.includes(query);
    const accMatch = p.acceptance >= state.filterAccMin && p.acceptance <= state.filterAccMax;
    return diffMatch && tagMatch && textMatch && accMatch;
  });

  results.sort((a, b) => {
    if (field === 'id') return parseInt(a.id) - parseInt(b.id);
    if (field === 'title') return a.title.localeCompare(b.title);
    if (field === 'difficulty') return ['Easy', 'Medium', 'Hard'].indexOf(a.difficulty) - ['Easy', 'Medium', 'Hard'].indexOf(b.difficulty);
    if (field === 'acceptance') return b.acceptance - a.acceptance;
    return 0;
  });
  state.currentPage = 0;
  renderProblemList(results);
}

function updateFilterBadge() {
  const count = state.filterDiffs.size + state.filterTags.size + (state.filterAccMin > 0 ? 1 : 0) + (state.filterAccMax < 100 ? 1 : 0);
  const badge = document.getElementById('filter-count-badge');
  if (count > 0) {
    badge.textContent = count;
    badge.style.display = 'inline-block';
  } else {
    badge.style.display = 'none';
  }
}

function applyFilters() {
  const query = document.getElementById('lc-search').value.toLowerCase();
  state.searchQuery = query;

  // Update acceptance range state
  state.filterAccMin = parseInt(document.getElementById('acc-min').value);
  state.filterAccMax = parseInt(document.getElementById('acc-max').value);
  document.getElementById('acc-min-val').textContent = state.filterAccMin + '%';
  document.getElementById('acc-max-val').textContent = state.filterAccMax + '%';

  let results = Object.values(PROBLEMS_DATA.problems).filter(p => {
    const diffMatch = state.filterDiffs.size === 0 || state.filterDiffs.has(p.difficulty);
    const tagMatch = state.filterTags.size === 0 || p.tags.some(t => state.filterTags.has(t));
    const textMatch = !query || p.title.toLowerCase().includes(query) || p.id.includes(query);
    const accMatch = p.acceptance >= state.filterAccMin && p.acceptance <= state.filterAccMax;
    return diffMatch && tagMatch && textMatch && accMatch;
  });

  const sortBy = document.getElementById('lc-sort').value;
  results.sort((a, b) => {
    if (sortBy === 'acceptance') return b.acceptance - a.acceptance;
    if (sortBy === 'difficulty') return ['Easy', 'Medium', 'Hard'].indexOf(a.difficulty) - ['Easy', 'Medium', 'Hard'].indexOf(b.difficulty);
    return parseInt(a.id) - parseInt(b.id);
  });

  state.currentPage = 0;
  renderProblemList(results);
  updateFilterBadge();
}

function renderProblemList(problems) {
  const container = document.getElementById('lc-problem-list');
  container.innerHTML = '';
  document.getElementById('lc-result-count').textContent = problems.length;

  if (problems.length === 0) {
    container.innerHTML = '<div style="text-align:center; padding:3rem; color:var(--text-light);">No problems match your filters.</div>';
    return;
  }

  // Pagination
  const pageSize = state.pageSize;
  const totalPages = Math.ceil(problems.length / pageSize);
  const start = state.currentPage * pageSize;
  const end = Math.min(start + pageSize, problems.length);
  const paginated = problems.slice(start, end);

  // Render header
  const header = document.createElement('div');
  header.className = 'lc-problem-row';
  header.style.background = 'var(--bg-color)';
  header.style.fontWeight = '600';
  header.style.cursor = 'default';
  header.innerHTML = \`
    <span class="lc-col-header" onclick="sortProblems('id')">#</span>
    <span class="lc-col-header" onclick="sortProblems('title')">Title</span>
    <span class="lc-col-header" onclick="sortProblems('difficulty')">Difficulty</span>
    <span class="lc-col-header" onclick="sortProblems('tags')">Tags</span>
    <span class="lc-col-header" onclick="sortProblems('acceptance')">Acceptance %</span>
  \`;
  container.appendChild(header);

  // Render problems
  paginated.forEach(p => {
    const row = document.createElement('div');
    row.className = 'lc-problem-row';
    row.onclick = () => window.open(generateLCURL(p.title), '_blank');

    row.innerHTML = \`
      <span class="prob-id">#\${p.id}</span>
      <span class="prob-title">\${p.title}</span>
      <span class="diff-badge \${p.difficulty.toLowerCase()}">\${p.difficulty}</span>
      <span class="prob-tags">\${p.tags.join(', ')}</span>
      <span class="prob-acc">\${p.acceptance}%</span>
    \`;
    container.appendChild(row);
  });

  // Render pagination
  if (totalPages > 1) {
    const paginationDiv = document.createElement('div');
    paginationDiv.className = 'lc-pagination';
    for (let i = 0; i < totalPages; i++) {
      const btn = document.createElement('button');
      btn.textContent = i + 1;
      btn.onclick = () => {
        state.currentPage = i;
        renderProblemList(problems);
      };
      if (i === state.currentPage) btn.classList.add('active');
      paginationDiv.appendChild(btn);
    }
    container.appendChild(paginationDiv);
  }
}

function generateLCURL(title) {
  const slug = title.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
  return \`https://leetcode.com/problems/\${slug}/\`;
}

// Pattern view
function initPatternView() {
  const container = document.getElementById('lc-pattern-accordion');
  const allTags = [...new Set(Object.values(PROBLEMS_DATA.problems).flatMap(p => p.tags))].sort();

  allTags.forEach(tag => {
    if (!PROBLEMS_DATA.tagMap[tag]) return;
    const count = (PROBLEMS_DATA.tagMap[tag].Easy || []).length +
                  (PROBLEMS_DATA.tagMap[tag].Medium || []).length +
                  (PROBLEMS_DATA.tagMap[tag].Hard || []).length;

    const item = document.createElement('div');
    item.className = 'lc-pattern-item';

    const header = document.createElement('div');
    header.className = 'lc-pattern-header';
    header.innerHTML = \`
      <span><strong>\${tag}</strong> (\${count} problems)</span>
      <span>▼</span>
    \`;
    header.onclick = () => {
      header.classList.toggle('open');
      content.classList.toggle('open');
    };

    const content = document.createElement('div');
    content.className = 'lc-pattern-content';

    // Build co-occurrence data for this tag
    const coData = {};
    Object.values(PROBLEMS_DATA.problems).forEach(p => {
      if (p.tags.includes(tag)) {
        p.tags.forEach(t => {
          if (t !== tag) {
            coData[t] = (coData[t] || 0) + 1;
          }
        });
      }
    });

    const sorted = Object.entries(coData).sort((a, b) => b[1] - a[1]).slice(0, 5);
    const maxCount = sorted[0]?.[1] || 1;

    sorted.forEach(([coTag, count]) => {
      const percent = (count / maxCount) * 100;
      const line = document.createElement('div');
      line.className = 'lc-co-tag';
      line.innerHTML = \`
        <span style="min-width: 150px;">\${coTag}</span>
        <div class="lc-co-bar">
          <div class="lc-co-bar-fill" style="width: \${percent}%"></div>
        </div>
        <span>(\${count})</span>
      \`;
      content.appendChild(line);
    });

    item.appendChild(header);
    item.appendChild(content);
    container.appendChild(item);
  });
}

// Mind Map view
let mindmapChart = null;

function initMindMap() {
  if (state.mindmapInitialized) return;
  state.mindmapInitialized = true;

  const threshold = parseInt(document.getElementById('mindmap-threshold').value);
  renderMindMap(threshold);
}

function updateMindMap() {
  const threshold = parseInt(document.getElementById('mindmap-threshold').value);
  if (mindmapChart) {
    document.getElementById('mindmap-container').innerHTML = '';
  }
  renderMindMap(threshold);
}

function resetMindMapZoom() {
  if (!mindmapChart) return;
  mindmapChart.transition().duration(750).call(d3.zoom().transform, d3.zoomIdentity.translate(50, 50));
}

function renderMindMap(threshold) {
  // Build nodes and links
  const nodes = Object.entries(PROBLEMS_DATA.tagMap).map(([tag, diffs]) => ({
    id: tag,
    count: (diffs.Easy || []).length + (diffs.Medium || []).length + (diffs.Hard || []).length,
    group: getTagGroup(tag)
  }));

  const nodeIds = new Set(nodes.map(n => n.id));
  const links = [];
  Object.entries(PROBLEMS_DATA.coMatrix).forEach(([a, bMap]) => {
    Object.entries(bMap).forEach(([b, count]) => {
      if (count >= threshold && nodeIds.has(a) && nodeIds.has(b)) {
        links.push({ source: a, target: b, value: count });
      }
    });
  });

  const width = document.getElementById('mindmap-container').clientWidth;
  const height = 600;

  const svg = d3.select('#mindmap-container').append('svg')
    .attr('width', width)
    .attr('height', height)
    .call(d3.zoom().on('zoom', e => g.attr('transform', e.transform)));

  mindmapChart = svg;

  const g = svg.append('g').attr('transform', 'translate(50,50)');

  const sim = d3.forceSimulation(nodes)
    .force('link', d3.forceLink(links).id(d => d.id).distance(d => 200 / Math.log(d.value + 1)))
    .force('charge', d3.forceManyBody().strength(-120))
    .force('center', d3.forceCenter(width / 2 - 50, height / 2 - 50))
    .force('collision', d3.forceCollide(d => Math.sqrt(d.count) * 3 + 8));

  const link = g.append('g').selectAll('line').data(links).join('line')
    .attr('stroke', 'var(--border-color)')
    .attr('stroke-opacity', 0.5)
    .attr('stroke-width', d => Math.log(d.value) * 0.5 + 0.5);

  const color = d3.scaleOrdinal(d3.schemeTableau10);

  const node = g.append('g').selectAll('g').data(nodes).join('g')
    .call(d3.drag()
      .on('start', e => { if (!e.active) sim.alphaTarget(0.3).restart(); e.subject.fx = e.subject.x; e.subject.fy = e.subject.y; })
      .on('drag', e => { e.subject.fx = e.x; e.subject.fy = e.y; })
      .on('end', e => { if (!e.active) sim.alphaTarget(0); e.subject.fx = null; e.subject.fy = null; })
    );

  node.append('circle')
    .attr('r', d => Math.sqrt(d.count) * 3)
    .attr('fill', d => color(d.group))
    .attr('stroke', 'white')
    .attr('stroke-width', 1.5)
    .style('cursor', 'pointer')
    .on('click', (e, d) => {
      document.getElementById('view-filter').classList.add('active');
      document.getElementById('view-patterns').classList.remove('active');
      document.getElementById('view-mindmap').classList.remove('active');
      document.getElementById('view-picker').classList.remove('active');

      state.filterTags.clear();
      state.filterTags.add(d.id);

      document.querySelectorAll('.lc-btn[data-tag]').forEach(b => {
        b.classList.toggle('active', b.dataset.tag === d.id);
      });

      applyFilters();
    });

  node.append('text')
    .attr('dy', '0.35em')
    .attr('text-anchor', 'middle')
    .style('font-size', d => d.count > 200 ? '10px' : '8px')
    .style('pointer-events', 'none')
    .style('fill', 'white')
    .style('font-weight', 'bold')
    .text(d => d.id.length > 12 ? d.id.slice(0, 11) + '…' : d.id);

  sim.on('tick', () => {
    link.attr('x1', d => d.source.x).attr('y1', d => d.source.y)
        .attr('x2', d => d.target.x).attr('y2', d => d.target.y);
    node.attr('transform', d => \`translate(\${d.x},\${d.y})\`);
  });

  // Legend
  const legend = document.getElementById('mindmap-legend');
  legend.innerHTML = '';
  const groupNames = ['Data Structures', 'DP/Greedy', 'Graphs', 'Search', 'Hash', 'Stack/Queue', 'Math', 'Other'];
  groupNames.forEach((name, i) => {
    const item = document.createElement('div');
    item.className = 'legend-item';
    item.innerHTML = \`
      <div class="legend-color" style="background: \${color(i)}"></div>
      <span>\${name}</span>
    \`;
    legend.appendChild(item);
  });
}

// Picker view — supports single card (count=1) and batch list (count>1)
function getPickerPool() {
  const diffs = [...document.getElementById('lc-picker-card-container').parentElement.querySelectorAll('input[type="checkbox"]:checked')]
    .map(cb => cb.value);
  const tag = document.getElementById('picker-tag-select').value || null;
  const excludeSelect = document.getElementById('picker-exclude-select');
  const excludeTags = excludeSelect ? [...excludeSelect.selectedOptions].map(o => o.value) : [];
  const seen = new Set(JSON.parse(localStorage.getItem('lc-seen-problems') || '[]'));

  let pool = Object.values(PROBLEMS_DATA.problems).filter(p => {
    const diffOk = diffs.length === 0 || diffs.includes(p.difficulty);
    const tagOk = !tag || p.tags.includes(tag);
    const excludeOk = !excludeTags.some(t => p.tags.includes(t));
    return diffOk && tagOk && excludeOk && !seen.has(p.id);
  });

  if (pool.length === 0) {
    pool = Object.values(PROBLEMS_DATA.problems).filter(p => {
      const diffOk = diffs.length === 0 || diffs.includes(p.difficulty);
      const tagOk = !tag || p.tags.includes(tag);
      const excludeOk = !excludeTags.some(t => p.tags.includes(t));
      return diffOk && tagOk && excludeOk;
    });
  }
  return pool;
}

function pickRandom(pool, n) {
  const shuffled = [...pool].sort(() => Math.random() - 0.5);
  return shuffled.slice(0, Math.min(n, pool.length));
}

function renderPickerCard() {
  const count = Math.max(1, parseInt(document.getElementById('picker-gen-count').value) || 1);
  const pool = getPickerPool();
  const picked = pickRandom(pool, count);
  const container = document.getElementById('lc-picker-card-container');

  if (count === 1) {
    const p = picked[0];
    container.innerHTML = \`
      <div class="lc-picker-card">
        <div class="prob-number">#\${p.id}</div>
        <div class="prob-name">\${p.title}</div>
        <div style="margin: 1rem 0;">
          <span class="diff-badge \${p.difficulty.toLowerCase()}">\${p.difficulty}</span>
        </div>
        <div style="font-size: 0.85rem; color: var(--text-light); margin: 0.75rem 0;">
          \${p.tags.join(' • ')}
        </div>
        <div style="font-size: 0.8rem; color: var(--text-light);">Acceptance: \${p.acceptance}%</div>
        <div class="lc-picker-actions">
          <button class="btn-primary" onclick="markPickerDone(\${p.id})">Mark Done & Next</button>
          <button class="btn-secondary" onclick="renderPickerCard()">Skip</button>
          <button class="btn-secondary" onclick="window.open('\${generateLCURL(p.title)}', '_blank')">Open on LeetCode ↗</button>
        </div>
      </div>
    \`;
  } else {
    // Batch list mode
    const listHtml = picked.map(p => \`
      <div class="lc-batch-row" id="batch-row-\${p.id}">
        <span class="prob-id">#\${p.id}</span>
        <span class="prob-title" style="cursor:pointer" onclick="window.open('\${generateLCURL(p.title)}','_blank')">\${p.title}</span>
        <span class="diff-badge \${p.difficulty.toLowerCase()}">\${p.difficulty}</span>
        <span class="prob-tags">\${p.tags.join(', ')}</span>
        <span class="prob-acc">\${p.acceptance}%</span>
        <button class="batch-done-btn" onclick="markBatchDone('\${p.id}')">✓ Done</button>
      </div>
    \`).join('');
    container.innerHTML = \`
      <div class="lc-batch-list">\${listHtml}</div>
      <div style="display:flex; gap:0.75rem; flex-wrap:wrap; margin-bottom:1.5rem;">
        <button class="btn-primary" onclick="markAllBatchDone(['\${picked.map(p=>p.id).join("','")}'])">Mark All Done & Refresh</button>
        <button class="btn-secondary" onclick="renderPickerCard()">Re-pick</button>
      </div>
    \`;
  }

  updatePickerProgress();
  updateRecentPickerList();
}

function markPickerDone(id) {
  const seen = new Set(JSON.parse(localStorage.getItem('lc-seen-problems') || '[]'));
  seen.add(String(id));
  localStorage.setItem('lc-seen-problems', JSON.stringify([...seen]));
  renderPickerCard();
}

function markBatchDone(id) {
  const seen = new Set(JSON.parse(localStorage.getItem('lc-seen-problems') || '[]'));
  seen.add(String(id));
  localStorage.setItem('lc-seen-problems', JSON.stringify([...seen]));
  const row = document.getElementById('batch-row-' + id);
  if (row) row.classList.add('done');
  updatePickerProgress();
  updateRecentPickerList();
}

function markAllBatchDone(ids) {
  const seen = new Set(JSON.parse(localStorage.getItem('lc-seen-problems') || '[]'));
  ids.forEach(id => seen.add(String(id)));
  localStorage.setItem('lc-seen-problems', JSON.stringify([...seen]));
  renderPickerCard();
}

function updatePickerProgress() {
  const seen = new Set(JSON.parse(localStorage.getItem('lc-seen-problems') || '[]'));
  const total = Object.keys(PROBLEMS_DATA.problems).length;
  document.getElementById('picker-count').textContent = seen.size;
  document.getElementById('picker-total').textContent = total;
  document.getElementById('picker-progress').style.width = ((seen.size / total) * 100) + '%';
}

function updateRecentPickerList() {
  const seen = JSON.parse(localStorage.getItem('lc-seen-problems') || '[]');
  const recent = seen.slice(-8).reverse();
  const container = document.getElementById('picker-recent');
  container.innerHTML = '';

  recent.forEach(id => {
    const p = PROBLEMS_DATA.problems[id];
    if (p) {
      const link = document.createElement('a');
      link.href = generateLCURL(p.title);
      link.target = '_blank';
      link.textContent = \`#\${p.id} \${p.title}\`;
      container.appendChild(link);
    }
  });
}

function clearPickerHistory() {
  if (confirm('Clear all progress?')) {
    localStorage.removeItem('lc-seen-problems');
    updatePickerProgress();
    updateRecentPickerList();
    renderPickerCard();
  }
}

// Similar Problems — Jaccard similarity on tags
function computeSimilarity(tagsA, tagsB) {
  const setA = new Set(tagsA);
  const setB = new Set(tagsB);
  const intersection = tagsA.filter(t => setB.has(t)).length;
  const union = new Set([...tagsA, ...tagsB]).size;
  return union === 0 ? 0 : intersection / union;
}

function findSimilar() {
  const input = document.getElementById('similar-problem-input').value.trim();
  const n = Math.max(1, parseInt(document.getElementById('similar-count-input').value) || 5);
  const area = document.getElementById('similar-results-area');

  if (!input) {
    area.innerHTML = '<div class="lc-similar-empty">Please enter a problem number or name.</div>';
    return;
  }

  // Find source problem by id or by title substring
  const allProblems = Object.values(PROBLEMS_DATA.problems);
  let source = PROBLEMS_DATA.problems[input] ||
    allProblems.find(p => p.id === input) ||
    allProblems.find(p => p.title.toLowerCase() === input.toLowerCase()) ||
    allProblems.find(p => p.title.toLowerCase().includes(input.toLowerCase()));

  if (!source) {
    area.innerHTML = \`<div class="lc-similar-empty">Problem not found for "<strong>\${input}</strong>". Try a problem number like <em>10</em> or a title keyword.</div>\`;
    return;
  }

  // Score all other problems
  const scored = allProblems
    .filter(p => p.id !== source.id)
    .map(p => ({
      problem: p,
      score: computeSimilarity(source.tags, p.tags)
    }))
    .filter(x => x.score > 0)
    .sort((a, b) => b.score - a.score)
    .slice(0, n);

  // Source card
  const sourceHtml = \`
    <div class="lc-source-card">
      <div style="font-size:0.8rem; color:var(--text-light); margin-bottom:0.4rem;">Source problem</div>
      <div style="display:flex; align-items:center; gap:0.75rem; flex-wrap:wrap;">
        <span class="prob-id" style="font-family:var(--mono); font-weight:600; color:var(--text-light);">#\${source.id}</span>
        <strong>\${source.title}</strong>
        <span class="diff-badge \${source.difficulty.toLowerCase()}">\${source.difficulty}</span>
        <span style="font-size:0.8rem; color:var(--text-light);">\${source.acceptance}%</span>
        <a href="\${generateLCURL(source.title)}" target="_blank" style="font-size:0.85rem; margin-left:auto;">Open ↗</a>
      </div>
      <div style="font-size:0.8rem; color:var(--text-light); margin-top:0.5rem;">\${source.tags.join(' • ')}</div>
    </div>
  \`;

  if (scored.length === 0) {
    area.innerHTML = sourceHtml + '<div class="lc-similar-empty">No similar problems found with overlapping tags.</div>';
    return;
  }

  const rowsHtml = scored.map(({ problem: p, score }) => {
    const sourceTagsSet = new Set(source.tags);
    const matchedTags = p.tags.filter(t => sourceTagsSet.has(t));
    const tagBreakdown = p.tags.map(t => \`<span class="lc-tag-match \${matchedTags.includes(t) ? 'matched' : ''}">\${t}</span>\`).join('');

    return \`
      <div class="lc-similar-row" style="flex-direction: column; align-items: flex-start;">
        <div style="display: flex; gap: 0.75rem; align-items: center; width: 100%; flex-wrap: wrap;">
          <span class="prob-id">#\${p.id}</span>
          <span class="prob-title" style="cursor:pointer; flex:1;" onclick="event.stopPropagation(); window.open('\${generateLCURL(p.title)}','_blank')">\${p.title}</span>
          <span class="diff-badge \${p.difficulty.toLowerCase()}">\${p.difficulty}</span>
          <span class="prob-acc">\${p.acceptance}%</span>
          <span class="lc-sim-score">\${Math.round(score * 100)}% match</span>
        </div>
        <div style="width: 100%; font-size: 0.8rem; margin-top: 0.5rem;">\${tagBreakdown}</div>
      </div>
    \`;
  }).join('');

  // Sort results if needed
  if (state.similarSortBy === 'difficulty') {
    scored.sort((a, b) => ['Easy', 'Medium', 'Hard'].indexOf(a.problem.difficulty) - ['Easy', 'Medium', 'Hard'].indexOf(b.problem.difficulty));
  } else if (state.similarSortBy === 'acceptance') {
    scored.sort((a, b) => b.problem.acceptance - a.problem.acceptance);
  }

  area.innerHTML = sourceHtml +
    \`<div class="lc-similar-sort" style="display:flex; gap:0.5rem; margin-bottom:1rem;">
      <button class="lc-btn \${state.similarSortBy === 'score' ? 'active' : ''}" onclick="setSimilarSort('score')">By Similarity</button>
      <button class="lc-btn \${state.similarSortBy === 'difficulty' ? 'active' : ''}" onclick="setSimilarSort('difficulty')">By Difficulty</button>
      <button class="lc-btn \${state.similarSortBy === 'acceptance' ? 'active' : ''}" onclick="setSimilarSort('acceptance')">By Acceptance</button>
    </div>\` +
    \`<div style="font-size:0.85rem; color:var(--text-light); margin-bottom:0.75rem;">Top \${scored.length} similar problems (by tag overlap):</div>\` +
    \`<div class="lc-similar-results">\${rowsHtml}</div>\`;
}

function setSimilarSort(sortBy) {
  state.similarSortBy = sortBy;
  findSimilar();
}

function recommendNext() {
  const seen = new Set(JSON.parse(localStorage.getItem('lc-seen-problems') || '[]'));
  const allProblems = Object.values(PROBLEMS_DATA.problems);

  // Get unsolved problems by difficulty progression
  const easyUnsolved = allProblems.filter(p => p.difficulty === 'Easy' && !seen.has(p.id));
  const mediumUnsolved = allProblems.filter(p => p.difficulty === 'Medium' && !seen.has(p.id));
  const hardUnsolved = allProblems.filter(p => p.difficulty === 'Hard' && !seen.has(p.id));

  const seeCount = parseInt(document.getElementById('picker-gen-count').value) || 1;
  let recommended = [];

  if (easyUnsolved.length > 0) {
    recommended = easyUnsolved.sort(() => Math.random() - 0.5).slice(0, seeCount);
  } else if (mediumUnsolved.length > 0) {
    recommended = mediumUnsolved.sort(() => Math.random() - 0.5).slice(0, seeCount);
  } else if (hardUnsolved.length > 0) {
    recommended = hardUnsolved.sort(() => Math.random() - 0.5).slice(0, seeCount);
  }

  const container = document.getElementById('lc-picker-card-container');
  if (recommended.length === 0) {
    container.innerHTML = '<div style="text-align:center; padding:2rem; color:var(--text-light);">🎉 All problems solved!</div>';
    return;
  }

  if (seeCount === 1) {
    const p = recommended[0];
    container.innerHTML = \`
      <div class="lc-picker-card">
        <div class="prob-number">#\${p.id}</div>
        <div class="prob-name">\${p.title}</div>
        <div style="margin: 1rem 0;">
          <span class="diff-badge \${p.difficulty.toLowerCase()}">\${p.difficulty}</span>
        </div>
        <div style="font-size: 0.85rem; color: var(--text-light); margin: 0.75rem 0;">
          \${p.tags.join(' • ')}
        </div>
        <div class="lc-picker-actions">
          <button class="btn-primary" onclick="markPickerDone(\${p.id})">Mark Done & Next</button>
          <button class="btn-secondary" onclick="window.open('\${generateLCURL(p.title)}', '_blank')">Open on LeetCode ↗</button>
        </div>
      </div>
    \`;
  } else {
    const listHtml = recommended.map(p => \`
      <div class="lc-batch-row" id="batch-row-\${p.id}">
        <span class="prob-id">#\${p.id}</span>
        <span class="prob-title" style="cursor:pointer" onclick="window.open('\${generateLCURL(p.title)}','_blank')">\${p.title}</span>
        <span class="diff-badge \${p.difficulty.toLowerCase()}">\${p.difficulty}</span>
        <span class="prob-tags">\${p.tags.join(', ')}</span>
        <span class="prob-acc">\${p.acceptance}%</span>
        <button class="batch-done-btn" onclick="markBatchDone('\${p.id}')">✓ Done</button>
      </div>
    \`).join('');
    container.innerHTML = \`
      <div class="lc-batch-list">\${listHtml}</div>
      <div style="display:flex; gap:0.75rem; flex-wrap:wrap; margin-bottom:1.5rem;">
        <button class="btn-primary" onclick="markAllBatchDone(['\${recommended.map(p=>p.id).join("','")}'])">Mark All Done & Refresh</button>
      </div>
    \`;
  }
  updatePickerProgress();
}

function exportBatchList() {
  const count = Math.max(1, parseInt(document.getElementById('picker-gen-count').value) || 1);
  const pool = getPickerPool();
  const picked = pickRandom(pool, count);

  let csv = 'ID,Title,Difficulty,Tags,Acceptance\\n';
  picked.forEach(p => {
    const tags = p.tags.join(';');
    csv += \`\${p.id},"\${p.title}",\${p.difficulty},"\${tags}",\${p.acceptance}\\n\`;
  });

  const blob = new Blob([csv], { type: 'text/csv' });
  const url = window.URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = 'lc-problems.csv';
  a.click();
}

function copyBatchList() {
  const count = Math.max(1, parseInt(document.getElementById('picker-gen-count').value) || 1);
  const pool = getPickerPool();
  const picked = pickRandom(pool, count);

  let text = 'LeetCode Problems\\n' + '='.repeat(50) + '\\n';
  picked.forEach(p => {
    text += \`#\${p.id}: \${p.title} [\${p.difficulty}]\\n\`;
    text += \`Tags: \${p.tags.join(', ')}\\n\`;
    text += \`Acceptance: \${p.acceptance}%\\n\\n\`;
  });

  navigator.clipboard.writeText(text).then(() => {
    alert('Copied to clipboard!');
  });
}

// Boot
initFilterView();
</script>`;
}

// Main execution
try {
  console.log('Parsing doc/google_leetcode_problems_by_tags.md...');
  const { problems, tagMap } = parseProblems();
  console.log(`✓ Parsed ${problems.size} problems across ${tagMap.size} tags`);

  console.log('Building co-occurrence matrix...');
  const coMatrix = buildCoMatrix(problems);
  console.log('✓ Built co-occurrence index');

  console.log('Generating HTML page...');
  const body = generatePageBody(problems, tagMap, coMatrix);
  const html = htmlTemplate('LeetCode Explorer', body);

  console.log('Writing _site/leetcode.html...');
  if (!fs.existsSync('_site')) fs.mkdirSync('_site', { recursive: true });
  fs.writeFileSync('_site/leetcode.html', html);
  console.log('✓ Created _site/leetcode.html');

} catch (err) {
  console.error('Error building LeetCode page:', err.message);
  process.exit(1);
}
