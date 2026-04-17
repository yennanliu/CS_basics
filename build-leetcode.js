#!/usr/bin/env node

/**
 * Build LeetCode Explorer
 * - Parses doc/google_leetcode_problems_by_tags.md
 * - Generates _site/data/lc-problems.json (pre-formatted data)
 * - Generates _site/lc-explorer.html (simple, fast UI)
 */

const fs = require('fs');
const path = require('path');

// Parse markdown file
function parseProblems() {
  const content = fs.readFileSync('doc/google_leetcode_problems_by_tags.md', 'utf8');
  const lines = content.split('\n');

  const problems = new Map(); // id -> problem
  const tagMap = new Map();   // tag -> count

  let currentTag = null;
  let currentDiff = null;

  for (const line of lines) {
    if (line.startsWith('## ')) {
      currentTag = line.slice(3).trim();
      continue;
    }

    if (line.startsWith('### ')) {
      currentDiff = line.slice(4).trim();
      continue;
    }

    // Parse problem entries: - #66 - Plus One - Array, Math - 42.5%
    if (line.startsWith('- #')) {
      const match = line.match(/^- #(\d+)\s*-\s*(.+?)\s*-\s*(.+?)\s*-\s*([\d.]+)%/);
      if (match) {
        const [, idStr, title, tagsRaw, accStr] = match;
        const id = idStr;
        const tags = tagsRaw.split(',').map(t => t.trim()).filter(Boolean);
        const acceptance = parseFloat(accStr);

        if (!problems.has(id)) {
          problems.set(id, {
            id,
            title: title.trim(),
            tags,
            difficulty: currentDiff || 'Unknown',
            acceptance
          });
        }

        // Count tags
        tags.forEach(tag => {
          tagMap.set(tag, (tagMap.get(tag) || 0) + 1);
        });
      }
    }
  }

  return { problems, tagMap };
}

// Generate lc-explorer.html
function generateLcExplorerHtml() {
  return `<!DOCTYPE html>
<html lang="en" data-theme="light">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>LeetCode Explorer - CS Basics</title>
  <meta name="description" content="Explore LeetCode problems by difficulty and tags">
  <link rel="icon" href="data:image/svg+xml,<svg xmlns=%22http://www.w3.org/2000/svg%22 viewBox=%220 0 100 100%22><text y=%22.9em%22 font-size=%2290%22>💻</text></svg>">
  <style>
    * { box-sizing: border-box; }
    :root {
      --bg: #fafafa;
      --surface: #fff;
      --text: #1a1a2e;
      --text-light: #666;
      --border: #e0e0e0;
      --radius: 8px;
    }
    [data-theme="dark"] {
      --bg: #0f0f1a;
      --surface: #1a1a2e;
      --text: #e0e0e0;
      --text-light: #aaa;
      --border: #2a2a3e;
    }
    body {
      margin: 0;
      padding: 0;
      font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', system-ui, sans-serif;
      background: var(--bg);
      color: var(--text);
      transition: background 0.3s, color 0.3s;
    }
    .navbar {
      background: var(--surface);
      border-bottom: 1px solid var(--border);
      padding: 1rem 2rem;
      position: sticky;
      top: 0;
      z-index: 100;
    }
    .nav-content {
      max-width: 1200px;
      margin: 0 auto;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .logo {
      font-size: 1.2rem;
      font-weight: 700;
      color: var(--text);
      text-decoration: none;
    }
    .theme-btn {
      background: none;
      border: none;
      font-size: 1.2rem;
      cursor: pointer;
      padding: 0.5rem;
    }
    .container {
      max-width: 1200px;
      margin: 2rem auto;
      padding: 0 2rem;
    }
    .header {
      text-align: center;
      margin-bottom: 2rem;
    }
    .header h1 {
      margin: 0 0 0.5rem;
      font-size: 2rem;
    }
    .header p {
      color: var(--text-light);
      margin: 0 0 1rem;
    }
    .stats {
      display: flex;
      justify-content: center;
      gap: 2rem;
      margin: 1rem 0;
      flex-wrap: wrap;
    }
    .stat {
      padding: 0.75rem 1.5rem;
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: var(--radius);
      text-align: center;
    }
    .stat-value {
      font-size: 1.8rem;
      font-weight: 700;
      color: var(--text);
    }
    .stat-label {
      font-size: 0.85rem;
      color: var(--text-light);
      margin-top: 0.25rem;
    }
    .controls {
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
      gap: 1rem;
      margin-bottom: 2rem;
      background: var(--surface);
      padding: 1.5rem;
      border: 1px solid var(--border);
      border-radius: var(--radius);
      align-items: flex-end;
    }
    .control-group {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }
    .control-label {
      font-size: 0.9rem;
      font-weight: 600;
      color: var(--text);
    }
    input[type="text"], select {
      padding: 0.6rem;
      border: 1px solid var(--border);
      border-radius: var(--radius);
      background: var(--bg);
      color: var(--text);
      font-family: inherit;
      font-size: 0.95rem;
      cursor: pointer;
    }
    .tag-filters {
      display: flex;
      flex-wrap: wrap;
      gap: 0.4rem;
      margin-top: 0.5rem;
    }
    .tag-btn {
      padding: 0.3rem 0.8rem;
      border: 1px solid var(--border);
      background: var(--bg);
      color: var(--text);
      border-radius: 16px;
      cursor: pointer;
      font-size: 0.8rem;
      transition: all 0.2s;
      white-space: nowrap;
    }
    .tag-btn:hover {
      border-color: var(--text);
    }
    .tag-btn.active {
      background: var(--text);
      color: var(--bg);
      border-color: var(--text);
      font-weight: 600;
    }
    .selected-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 0.4rem;
      margin-top: 0.5rem;
    }
    .tag-chip {
      display: inline-flex;
      align-items: center;
      gap: 0.4rem;
      padding: 0.3rem 0.7rem;
      background: var(--text);
      color: var(--bg);
      border-radius: 16px;
      font-size: 0.8rem;
    }
    .tag-chip button {
      background: none;
      border: none;
      color: inherit;
      cursor: pointer;
      font-size: 1rem;
      padding: 0;
      display: flex;
      align-items: center;
    }
    .tag-chip button:hover {
      opacity: 0.8;
    }
    .problems-list {
      display: flex;
      flex-direction: column;
      gap: 0;
      border: 1px solid var(--border);
      border-radius: var(--radius);
      overflow: hidden;
    }
    .problem-row {
      display: grid;
      grid-template-columns: 4rem 1fr auto 8rem;
      gap: 1rem;
      align-items: center;
      padding: 1rem;
      border-bottom: 1px solid var(--border);
      background: var(--surface);
      cursor: pointer;
      transition: background 0.15s;
    }
    .problem-row:last-child {
      border-bottom: none;
    }
    .problem-row:hover {
      background: var(--bg);
    }
    .problem-id {
      font-family: monospace;
      font-weight: 600;
      color: var(--text-light);
      font-size: 0.9rem;
    }
    .problem-title {
      font-weight: 500;
    }
    .difficulty {
      padding: 0.3rem 0.8rem;
      border-radius: 4px;
      font-size: 0.8rem;
      font-weight: 600;
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
    .difficulty.Easy { background: #1e7e34; color: #fff; }
    .difficulty.Medium { background: #b45309; color: #fff; }
    .difficulty.Hard { background: #b91c1c; color: #fff; }
    [data-theme="dark"] .difficulty.Easy { background: #166534; }
    [data-theme="dark"] .difficulty.Medium { background: #92400e; }
    [data-theme="dark"] .difficulty.Hard { background: #991b1b; }
    .problem-acceptance {
      font-size: 0.85rem;
      color: var(--text-light);
      text-align: right;
      min-width: 60px;
    }
    .empty {
      text-align: center;
      padding: 3rem;
      color: var(--text-light);
    }
    .button {
      padding: 0.6rem 1.5rem;
      background: var(--text);
      color: var(--bg);
      border: none;
      border-radius: var(--radius);
      font-weight: 600;
      cursor: pointer;
      font-family: inherit;
      transition: opacity 0.2s;
    }
    .button:hover {
      opacity: 0.9;
    }
    .button-secondary {
      background: var(--surface);
      color: var(--text);
      border: 1px solid var(--border);
    }
    .button-group {
      display: flex;
      gap: 1rem;
      flex-wrap: wrap;
      margin-bottom: 1.5rem;
    }
    .loading {
      text-align: center;
      padding: 2rem;
      color: var(--text-light);
    }
    @media (max-width: 768px) {
      .controls {
        grid-template-columns: 1fr;
      }
      .problem-row {
        grid-template-columns: 3rem 1fr;
        gap: 0.5rem;
      }
      .problem-row > :nth-child(3),
      .problem-row > :nth-child(4) {
        display: none;
      }
      .header h1 {
        font-size: 1.5rem;
      }
      .stats {
        gap: 1rem;
      }
    }
  </style>
</head>
<body>
  <nav class="navbar">
    <div class="nav-content">
      <a href="index.html" class="logo">CS Basics</a>
      <button class="theme-btn" onclick="toggleTheme()" aria-label="Toggle dark mode">🌙</button>
    </div>
  </nav>

  <div class="container">
    <div class="header">
      <h1>LeetCode Explorer</h1>
      <p>Browse and filter LeetCode problems</p>
      <div class="stats" id="stats"></div>
    </div>

    <div class="button-group">
      <button class="button button-secondary" onclick="clearFilters()">Clear Filters</button>
    </div>

    <div class="controls">
      <div class="control-group">
        <label class="control-label">Search</label>
        <input type="text" id="searchInput" placeholder="Problem name or number..." oninput="filterProblems()">
      </div>

      <div class="control-group">
        <label class="control-label">Difficulty</label>
        <div class="tag-filters" id="difficultyFilters"></div>
      </div>

      <div class="control-group">
        <label class="control-label">Tags</label>
        <select id="tagSelect" onchange="addTag()">
          <option value="">+ Add tag</option>
        </select>
      </div>
    </div>

    <div id="selectedTagsContainer" style="margin-bottom: 1.5rem;"></div>

    <div id="problemsContainer" class="problems-list">
      <div class="loading">Loading problems...</div>
    </div>
  </div>

  <script>
    let allProblems = [];
    let allTags = [];
    const state = {
      searchQuery: '',
      difficulties: new Set(),
      tags: new Set()
    };

    async function loadData() {
      try {
        const response = await fetch('./data/lc-problems.json');
        const data = await response.json();
        allProblems = data.problems;
        allTags = data.tags;

        document.getElementById('stats').innerHTML = \`
          <div class="stat">
            <div class="stat-value">\${data.stats.totalProblems}</div>
            <div class="stat-label">Problems</div>
          </div>
          <div class="stat">
            <div class="stat-value">\${data.stats.totalTags}</div>
            <div class="stat-label">Tags</div>
          </div>
        \`;

        const diffContainer = document.getElementById('difficultyFilters');
        ['Easy', 'Medium', 'Hard'].forEach(diff => {
          const btn = document.createElement('button');
          btn.className = 'tag-btn';
          btn.textContent = diff;
          btn.onclick = () => toggleDifficulty(diff, btn);
          diffContainer.appendChild(btn);
        });

        const tagSelect = document.getElementById('tagSelect');
        allTags.forEach(tag => {
          const option = document.createElement('option');
          option.value = tag.name;
          option.textContent = \`\${tag.name} (\${tag.count})\`;
          tagSelect.appendChild(option);
        });

        filterProblems();
      } catch (err) {
        console.error('Error loading data:', err);
        document.getElementById('problemsContainer').innerHTML = '<div class="empty">Error loading problems</div>';
      }
    }

    function toggleDifficulty(diff, btn) {
      btn.classList.toggle('active');
      if (btn.classList.contains('active')) {
        state.difficulties.add(diff);
      } else {
        state.difficulties.delete(diff);
      }
      filterProblems();
    }

    function addTag() {
      const tagSelect = document.getElementById('tagSelect');
      const tag = tagSelect.value;
      if (tag) {
        state.tags.add(tag);
        tagSelect.value = '';
        updateSelectedTags();
        filterProblems();
      }
    }

    function removeTag(tag) {
      state.tags.delete(tag);
      updateSelectedTags();
      filterProblems();
    }

    function updateSelectedTags() {
      const container = document.getElementById('selectedTagsContainer');
      if (state.tags.size === 0) {
        container.innerHTML = '';
        return;
      }

      container.innerHTML = '<div class="selected-tags">' +
        Array.from(state.tags).map(tag => \`
          <div class="tag-chip">
            \${tag}
            <button type="button" onclick="removeTag('\${tag}')">&times;</button>
          </div>
        \`).join('') +
        '</div>';
    }

    function clearFilters() {
      state.searchQuery = '';
      state.difficulties.clear();
      state.tags.clear();
      document.getElementById('searchInput').value = '';
      document.getElementById('tagSelect').value = '';
      document.querySelectorAll('.tag-btn').forEach(btn => btn.classList.remove('active'));
      updateSelectedTags();
      filterProblems();
    }

    function filterProblems() {
      const query = document.getElementById('searchInput').value.toLowerCase();
      state.searchQuery = query;

      let results = allProblems.filter(p => {
        const matchesSearch = !query ||
          p.title.toLowerCase().includes(query) ||
          p.id.includes(query);

        const matchesDiff = state.difficulties.size === 0 ||
          state.difficulties.has(p.difficulty);

        const matchesTags = state.tags.size === 0 ||
          p.tags.some(t => state.tags.has(t));

        return matchesSearch && matchesDiff && matchesTags;
      });

      renderProblems(results);
    }

    function renderProblems(problems) {
      const container = document.getElementById('problemsContainer');

      if (problems.length === 0) {
        container.innerHTML = '<div class="empty">No problems found</div>';
        return;
      }

      container.innerHTML = problems.map(p => \`
        <div class="problem-row" onclick="openProblem('\${p.title}')">
          <div class="problem-id">#\${p.id}</div>
          <div class="problem-title">\${p.title}</div>
          <div class="difficulty \${p.difficulty}">\${p.difficulty}</div>
          <div class="problem-acceptance">\${p.acceptance}%</div>
        </div>
      \`).join('');
    }

    function openProblem(title) {
      const slug = title.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
      window.open(\`https://leetcode.com/problems/\${slug}/\`, '_blank');
    }

    function toggleTheme() {
      const html = document.documentElement;
      const current = html.getAttribute('data-theme') || 'light';
      const next = current === 'light' ? 'dark' : 'light';
      html.setAttribute('data-theme', next);
      localStorage.setItem('theme', next);
    }

    const savedTheme = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', savedTheme);

    loadData();
  </script>
</body>
</html>`;
}

// Main execution
try {
  console.log('Parsing doc/google_leetcode_problems_by_tags.md...');
  const { problems, tagMap } = parseProblems();
  console.log(`✓ Parsed ${problems.size} problems across ${tagMap.size} tags`);

  // Create output directory
  if (!fs.existsSync('_site')) fs.mkdirSync('_site', { recursive: true });
  if (!fs.existsSync('_site/data')) fs.mkdirSync('_site/data', { recursive: true });

  // Generate and save JSON data
  console.log('Generating problems JSON...');
  const problemsArray = Array.from(problems.values());
  const tagsArray = Array.from(tagMap.entries())
    .map(([name, count]) => ({ name, count }))
    .sort((a, b) => b.count - a.count);

  const jsonData = {
    problems: problemsArray,
    tags: tagsArray,
    stats: {
      totalProblems: problems.size,
      totalTags: tagMap.size
    }
  };

  console.log('Writing _site/data/lc-problems.json...');
  fs.writeFileSync('_site/data/lc-problems.json', JSON.stringify(jsonData, null, 2));
  console.log(`✓ Created _site/data/lc-problems.json`);

  // Generate lc-explorer.html
  console.log('Generating lc-explorer.html...');
  const html = generateLcExplorerHtml();
  fs.writeFileSync('_site/lc-explorer.html', html);
  console.log(`✓ Created _site/lc-explorer.html`);

  console.log('\n✅ Build complete!');
  console.log(`   Open: _site/lc-explorer.html`);

} catch (err) {
  console.error('❌ Error:', err.message);
  process.exit(1);
}
