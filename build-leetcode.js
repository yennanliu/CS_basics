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
    .problem-header {
      display: grid;
      grid-template-columns: 4rem 1fr auto 8rem auto;
      gap: 0.5rem;
      align-items: center;
      padding: 1rem;
      background: var(--bg);
      border-bottom: 2px solid var(--border);
      font-weight: 600;
      font-size: 0.85rem;
      color: var(--text-light);
    }
    .problem-row {
      display: grid;
      grid-template-columns: 4rem 1fr auto 8rem auto;
      gap: 0.5rem;
      align-items: center;
      padding: 1rem;
      border-bottom: 1px solid var(--border);
      background: var(--surface);
      transition: background 0.15s;
    }
    .problem-title-wrapper {
      display: flex;
      flex-direction: column;
      gap: 0.3rem;
    }
    .problem-tags {
      display: flex;
      flex-wrap: wrap;
      gap: 0.3rem;
    }
    .tag-badge {
      font-size: 0.65rem;
      padding: 0.2rem 0.4rem;
      background: var(--bg);
      border: 1px solid var(--border);
      border-radius: 3px;
      color: var(--text-light);
      white-space: nowrap;
    }
    .problem-row:hover {
      background: var(--bg);
    }
    .problem-actions {
      display: flex;
      gap: 0.3rem;
    }
    .icon-btn {
      background: none;
      border: 1px solid var(--border);
      border-radius: 4px;
      padding: 0.3rem 0.6rem;
      cursor: pointer;
      color: var(--text-light);
      font-size: 0.8rem;
      transition: all 0.2s;
    }
    .icon-btn:hover {
      border-color: var(--text);
      color: var(--text);
    }
    .problem-row:last-child {
      border-bottom: none;
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
    .help-text {
      font-size: 0.8rem;
      color: var(--text-light);
      padding: 0.75rem 1.5rem;
      background: var(--bg);
      border-top: 1px solid var(--border);
    }
    .help-text kbd {
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 3px;
      padding: 0.2rem 0.4rem;
      font-family: monospace;
      font-size: 0.75rem;
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
      align-items: center;
    }
    .results-info {
      margin-left: auto;
      font-size: 0.9rem;
      color: var(--text-light);
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
        grid-template-columns: 3rem 1fr auto;
        gap: 0.3rem;
      }
      .problem-row > :nth-child(3),
      .problem-row > :nth-child(4) {
        display: none;
      }
      .problem-actions {
        gap: 0.2rem;
      }
      .icon-btn {
        padding: 0.2rem 0.4rem;
        font-size: 0.7rem;
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
      <button class="button button-secondary" onclick="exportCSV()">Export CSV</button>
      <button class="button button-secondary" onclick="shareFilters()">Share Filters</button>
      <button class="button button-secondary" onclick="copyListMarkdown()">Copy List</button>
      <div class="results-info" id="resultsInfo"></div>
    </div>

    <div class="controls">
      <div class="control-group">
        <label class="control-label">Search</label>
        <input type="text" id="searchInput" placeholder="Problem name or number..." oninput="debounceSearch()">
      </div>

      <div class="control-group">
        <label class="control-label">Difficulty</label>
        <div class="tag-filters" id="difficultyFilters"></div>
      </div>

      <div class="control-group">
        <label class="control-label">Acceptance</label>
        <div class="tag-filters" id="acceptanceFilters"></div>
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

    <div class="help-text">
      <strong>Shortcuts:</strong>
      <kbd>Esc</kbd> clear filters •
      <kbd>r</kbd> random problem •
      click titles to open on LeetCode
    </div>
  </div>

  <script>
    let allProblems = [];
    let allTags = [];
    let searchTimeout;
    const state = {
      searchQuery: '',
      difficulties: new Set(),
      tags: new Set(),
      acceptanceMin: 0,
      acceptanceMax: 100
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

        const accContainer = document.getElementById('acceptanceFilters');
        const accRanges = [
          { label: '0-30%', min: 0, max: 30 },
          { label: '30-60%', min: 30, max: 60 },
          { label: '60-100%', min: 60, max: 100 }
        ];
        accRanges.forEach(range => {
          const btn = document.createElement('button');
          btn.className = 'tag-btn';
          btn.textContent = range.label;
          btn.onclick = () => toggleAcceptance(range.min, range.max, btn);
          accContainer.appendChild(btn);
        });

        const tagSelect = document.getElementById('tagSelect');
        allTags.forEach(tag => {
          const option = document.createElement('option');
          option.value = tag.name;
          option.textContent = \`\${tag.name} (\${tag.count})\`;
          tagSelect.appendChild(option);
        });

        restoreFiltersFromURL();
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

    function toggleAcceptance(min, max, btn) {
      btn.classList.toggle('active');
      if (btn.classList.contains('active')) {
        state.acceptanceMin = min;
        state.acceptanceMax = max;
      } else {
        state.acceptanceMin = 0;
        state.acceptanceMax = 100;
      }
      // Only allow one acceptance filter at a time
      document.getElementById('acceptanceFilters').querySelectorAll('.tag-btn').forEach(b => {
        if (b !== btn) b.classList.remove('active');
      });
      filterProblems();
    }

    function debounceSearch() {
      clearTimeout(searchTimeout);
      searchTimeout = setTimeout(() => {
        filterProblems();
      }, 300);
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

        const matchesAcceptance = p.acceptance >= state.acceptanceMin &&
          p.acceptance <= state.acceptanceMax;

        return matchesSearch && matchesDiff && matchesTags && matchesAcceptance;
      });

      renderProblems(results);
    }

    function renderProblems(problems) {
      const container = document.getElementById('problemsContainer');
      const resultInfo = document.getElementById('resultsInfo');

      if (problems.length === 0) {
        const message = state.searchQuery || state.difficulties.size > 0 || state.tags.size > 0
          ? 'No problems match your filters. Try adjusting them.'
          : 'No problems found';
        container.innerHTML = \`<div class="empty">\${message}</div>\`;
        resultInfo.textContent = '';
        return;
      }

      resultInfo.textContent = \`Showing \${problems.length} of \${allProblems.length} problems\`;

      const header = \`<div class="problem-header">
        <div>#</div>
        <div>Problem</div>
        <div></div>
        <div>Acceptance</div>
        <div></div>
      </div>\`;

      const rows = problems.map(p => {
        const tagsHtml = p.tags.length > 0 ? \`
          <div class="problem-tags">
            \${p.tags.slice(0, 3).map(tag => \`<span class="tag-badge">\${tag}</span>\`).join('')}
            \${p.tags.length > 3 ? \`<span class="tag-badge">+\${p.tags.length - 3}</span>\` : ''}
          </div>
        \` : '';
        return \`
          <div class="problem-row">
            <div class="problem-id">#\${p.id}</div>
            <div class="problem-title-wrapper">
              <div class="problem-title" onclick="openProblem('\${p.title}')" style="cursor: pointer;">\${p.title}</div>
              \${tagsHtml}
            </div>
            <div class="difficulty \${p.difficulty}">\${p.difficulty}</div>
            <div class="problem-acceptance">\${p.acceptance}%</div>
            <div class="problem-actions">
              <button class="icon-btn" title="Open on LeetCode" onclick="openProblem('\${p.title}')">LC</button>
              <button class="icon-btn" title="Search GitHub" onclick="searchGitHub('\${p.id}', '\${p.title}')">GH</button>
            </div>
          </div>
        \`;
      }).join('');

      container.innerHTML = header + rows;
    }

    function openProblem(title) {
      const slug = title.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '');
      window.open(\`https://leetcode.com/problems/\${slug}/\`, '_blank');
    }

    function searchGitHub(id, title) {
      const query = encodeURIComponent(id);
      window.open(\`https://github.com/yennanliu/CS_basics/search?q=\${query}\`, '_blank');
    }

    function exportCSV() {
      const query = document.getElementById('searchInput').value.toLowerCase();
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

      const csv = ['ID,Title,Difficulty,Acceptance,Tags'].concat(
        results.map(p =>
          \`\${p.id},"\${p.title}",\${p.difficulty},\${p.acceptance}%,"\${p.tags.join(', ')}"\`
        )
      ).join('\\n');

      const blob = new Blob([csv], { type: 'text/csv' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'leetcode-problems.csv';
      a.click();
      URL.revokeObjectURL(url);
    }

    function shareFilters() {
      const params = new URLSearchParams();
      if (document.getElementById('searchInput').value) {
        params.set('search', document.getElementById('searchInput').value);
      }
      if (state.difficulties.size > 0) {
        params.set('difficulty', Array.from(state.difficulties).join(','));
      }
      if (state.tags.size > 0) {
        params.set('tags', Array.from(state.tags).join(','));
      }
      if (state.acceptanceMin !== 0 || state.acceptanceMax !== 100) {
        params.set('acceptance', \`\${state.acceptanceMin}-\${state.acceptanceMax}\`);
      }

      if (params.toString()) {
        const url = \`\${window.location.href.split('?')[0]}?\${params.toString()}\`;
        navigator.clipboard.writeText(url).then(() => {
          alert('Filter URL copied to clipboard!');
        }).catch(() => {
          prompt('Copy this filter URL:', url);
        });
      } else {
        alert('No filters to share');
      }
    }

    function copyListMarkdown() {
      const query = document.getElementById('searchInput').value.toLowerCase();
      let results = allProblems.filter(p => {
        const matchesSearch = !query ||
          p.title.toLowerCase().includes(query) ||
          p.id.includes(query);
        const matchesDiff = state.difficulties.size === 0 ||
          state.difficulties.has(p.difficulty);
        const matchesTags = state.tags.size === 0 ||
          p.tags.some(t => state.tags.has(t));
        const matchesAcceptance = p.acceptance >= state.acceptanceMin &&
          p.acceptance <= state.acceptanceMax;
        return matchesSearch && matchesDiff && matchesTags && matchesAcceptance;
      });

      const markdown = \`# LeetCode Problems (\${results.length})

\${results.map(p =>
        \`- [\${p.id}. \${p.title}](https://leetcode.com/problems/\${p.title.toLowerCase().replace(/[^a-z0-9]+/g, '-').replace(/^-|-$/g, '')}) [\${p.difficulty}] (\${p.acceptance}%)\`
      ).join('\\n')}\`;

      navigator.clipboard.writeText(markdown).then(() => {
        alert(\`Copied \${results.length} problems to clipboard!\`);
      }).catch(() => {
        prompt('Copy this list:', markdown);
      });
    }

    function restoreFiltersFromURL() {
      const params = new URLSearchParams(window.location.search);

      if (params.has('search')) {
        document.getElementById('searchInput').value = params.get('search');
        state.searchQuery = params.get('search').toLowerCase();
      }

      if (params.has('difficulty')) {
        const diffs = params.get('difficulty').split(',');
        diffs.forEach(diff => {
          state.difficulties.add(diff);
          document.querySelectorAll('#difficultyFilters .tag-btn').forEach(btn => {
            if (btn.textContent === diff) btn.classList.add('active');
          });
        });
      }

      if (params.has('acceptance')) {
        const [min, max] = params.get('acceptance').split('-').map(Number);
        state.acceptanceMin = min;
        state.acceptanceMax = max;
        document.querySelectorAll('#acceptanceFilters .tag-btn').forEach(btn => {
          if (btn.textContent.includes(min) || btn.textContent.includes(max)) {
            btn.classList.add('active');
          }
        });
      }

      if (params.has('tags')) {
        const tags = params.get('tags').split(',');
        tags.forEach(tag => {
          state.tags.add(tag);
        });
        updateSelectedTags();
      }

      if (params.toString()) {
        filterProblems();
      }
    }

    // Keyboard shortcuts
    document.addEventListener('keydown', e => {
      if (e.key === 'Escape') {
        clearFilters();
      } else if (e.key === 'r' && !document.activeElement.matches('input')) {
        const filtered = allProblems.filter(p => {
          const matchesSearch = !state.searchQuery ||
            p.title.toLowerCase().includes(state.searchQuery) ||
            p.id.includes(state.searchQuery);
          const matchesDiff = state.difficulties.size === 0 ||
            state.difficulties.has(p.difficulty);
          const matchesTags = state.tags.size === 0 ||
            p.tags.some(t => state.tags.has(t));
          return matchesSearch && matchesDiff && matchesTags;
        });
        if (filtered.length > 0) {
          const random = filtered[Math.floor(Math.random() * filtered.length)];
          openProblem(random.title);
        }
      }
    });

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
