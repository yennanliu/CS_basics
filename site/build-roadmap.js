#!/usr/bin/env node

/**
 * Build the study-roadmap data.
 *
 *   data/roadmap.json  (hand-authored topic graph)
 * + README.md          (the repo's master problem index)
 * → _site/data/roadmap.json  (enriched, laid out, ready to render)
 *
 * The roadmap is a DAG: each node is a topic, each `prereqs` entry is an edge,
 * and `row` is the layer the topic is drawn on. Problem titles, difficulty and
 * links to the solutions in this repo are resolved from README.md at build time
 * so that they live in exactly one place — data/roadmap.json carries only the
 * LeetCode ids.
 *
 * Every inconsistency is a hard failure rather than a silently-dropped node: a
 * typo'd cheatsheet slug or a problem id that is not in README would otherwise
 * ship as an empty box on the page.
 *
 * Run via site/build.sh; exported helpers are unit-tested in site/test.
 */

const fs = require('fs');
const path = require('path');

const GH_BLOB = 'https://github.com/yennanliu/CS_basics/blob/master';
const DIFFICULTIES = ['Easy', 'Medium', 'Hard'];

// `[Label](href)`, tolerating the stray whitespace the README has in places —
// `[Swim in Rising Water]( https://…)` and `[Java ](./path)` both occur, and a
// strict pattern silently drops those rows.
const MD_LINK = /\[([^\]]+)\]\(\s*([^)\s]+)/;
const MD_LINK_ALL = new RegExp(MD_LINK.source, 'g');

// ── README problem index ─────────────────────────────────────────────────────

/**
 * Parses the LeetCode tables in README.md into a Map of id → problem.
 *
 * Row shape: `| <id> | [Title](lc-url) | [Java](path), [Python](path) | time |
 * space | Difficulty | tags | status |`. The id column is zero-padded in places
 * ("026"), so it is normalised to its integer form.
 *
 * A handful of rows are malformed (a stray `\` in the solutions column, a
 * missing difficulty). Those still yield a usable title, so they are kept with
 * whatever fields did parse rather than dropped — only a row with no linked
 * title at all is skipped, since without it there is nothing to show.
 *
 * The same id can appear under more than one `##` section with different
 * language columns. The first row wins for title/difficulty, and the solution
 * links are unioned across every row, so a problem does not lose its Scala link
 * just because the Array table listed only Java.
 */
function parseReadmeProblems(markdown) {
  const problems = new Map();
  let section = null;

  for (const line of markdown.split('\n')) {
    const h2 = line.match(/^## +(.+?) *$/);
    if (h2) { section = h2[1]; continue; }
    if (!line.startsWith('|')) continue;

    const cells = line.split('|').map(cell => cell.trim());
    // cells[0] is the empty string before the leading pipe.
    const rawId = cells[1];
    if (!rawId || !/^\d+$/.test(rawId)) continue;

    const titleCell = cells[2] || '';
    const titleMatch = titleCell.match(MD_LINK);
    if (!titleMatch) continue;

    const id = String(parseInt(rawId, 10));
    const difficulty = DIFFICULTIES.includes(cells[6]) ? cells[6] : 'Unknown';
    const solutions = parseSolutionLinks(cells[3] || '');

    const existing = problems.get(id);
    if (existing) {
      Object.assign(existing.solutions, solutions, existing.solutions);
      continue;
    }
    problems.set(id, {
      id,
      title: titleMatch[1].trim(),
      url: titleMatch[2],
      difficulty,
      section,
      solutions
    });
  }
  return problems;
}

/**
 * `[Python](./leetcode_python/x.py), [Java](./y.java)` → `{Python: <gh url>, …}`.
 * Repo-relative paths become absolute GitHub blob URLs; anything already
 * absolute is passed through untouched.
 */
function parseSolutionLinks(cell) {
  const solutions = {};
  for (const match of cell.matchAll(MD_LINK_ALL)) {
    const lang = match[1].trim();
    const href = match[2].trim();
    if (!lang || !href) continue;
    solutions[lang] = /^https?:\/\//.test(href)
      ? href
      : `${GH_BLOB}/${href.replace(/^\.\//, '')}`;
  }
  return solutions;
}

// ── Cheatsheet titles ────────────────────────────────────────────────────────

/**
 * slug → the label to show on a roadmap node's cheatsheet chip.
 *
 * Same precedence the site index uses: the `title` override in
 * data/cheatsheet_meta.json wins, otherwise the sheet's own H1. Falling back to
 * the slug would leak filenames like "2_pointers" onto the page.
 */
function buildSheetTitles(dir, meta) {
  const overrides = (meta && meta.sheets) || {};
  const titles = new Map();
  for (const file of fs.readdirSync(dir)) {
    if (!file.endsWith('.md') || file === '00_template.md') continue;
    const slug = path.basename(file, '.md');
    const override = overrides[slug] && overrides[slug].title;
    if (override) { titles.set(slug, override); continue; }
    const h1 = fs.readFileSync(path.join(dir, file), 'utf8').match(/^# +(.+?) *$/m);
    titles.set(slug, h1 ? h1[1].trim() : slug);
  }
  return titles;
}

// ── Validation ───────────────────────────────────────────────────────────────

/**
 * Returns every structural problem with the authored graph, as a list of
 * human-readable strings. Collecting them all beats throwing on the first one:
 * a new node usually trips several checks at once, and fixing them one build
 * at a time is miserable.
 */
function validateGraph(nodes, { problems, sheetSlugs }) {
  const errors = [];
  const byId = new Map();

  for (const node of nodes) {
    if (!node.id) { errors.push(`a node is missing an "id" (title: ${node.title || '?'})`); continue; }
    if (byId.has(node.id)) errors.push(`duplicate node id "${node.id}"`);
    byId.set(node.id, node);
    if (!node.title) errors.push(`node "${node.id}" is missing a "title"`);
    if (!Number.isInteger(node.row) || node.row < 0) {
      errors.push(`node "${node.id}" needs an integer "row" >= 0 (got ${JSON.stringify(node.row)})`);
    }
    if (!Array.isArray(node.problems) || node.problems.length === 0) {
      errors.push(`node "${node.id}" lists no problems`);
    }
  }

  for (const node of nodes) {
    const prereqs = node.prereqs || [];
    for (const prereq of prereqs) {
      const parent = byId.get(prereq);
      if (!parent) { errors.push(`node "${node.id}" lists unknown prereq "${prereq}"`); continue; }
      // The row order is what makes the drawing readable: every edge must point
      // downward, so a topic never sits level with or above something it needs.
      if (Number.isInteger(parent.row) && Number.isInteger(node.row) && parent.row >= node.row) {
        errors.push(
          `node "${node.id}" (row ${node.row}) must sit below its prereq "${prereq}" (row ${parent.row})`
        );
      }
    }
    if (new Set(prereqs).size !== prereqs.length) {
      errors.push(`node "${node.id}" repeats a prereq`);
    }

    for (const slug of node.sheets || []) {
      if (!sheetSlugs.has(slug)) errors.push(`node "${node.id}" links unknown cheatsheet "${slug}"`);
    }

    const seen = new Set();
    for (const raw of node.problems || []) {
      const id = String(raw);
      if (seen.has(id)) errors.push(`node "${node.id}" repeats problem #${id}`);
      seen.add(id);
      if (!problems.has(id)) {
        errors.push(`node "${node.id}" lists problem #${id}, which is not in README.md`);
      }
    }
  }

  for (const cycle of findCycles(nodes)) {
    errors.push(`prereq cycle: ${cycle.join(' → ')}`);
  }
  for (const [node, prereq, via] of findRedundantEdges(nodes)) {
    errors.push(
      `node "${node}" lists prereq "${prereq}", which it already reaches through "${via}" — ` +
      'drop the direct edge (it draws a line across the graph that says nothing new)'
    );
  }
  return errors;
}

/**
 * Edges that the rest of the graph already implies, as [node, prereq, via].
 *
 * A roadmap only reads as a roadmap if it is a transitive reduction. "Design a
 * X after Linked List" is true but redundant once Design already sits behind
 * Heap → Trees → Linked List, and drawing it costs a line spanning three rows
 * for no information. Keeping the authored graph reduced is what keeps the
 * picture legible as topics are added.
 */
function findRedundantEdges(nodes) {
  const prereqsOf = new Map(nodes.map(n => [n.id, (n.prereqs || []).filter(p => p !== n.id)]));

  // Ancestors reachable *without* using the direct edge under test.
  function reaches(from, target, skip, seen = new Set()) {
    for (const next of prereqsOf.get(from) || []) {
      if (from === skip[0] && next === skip[1]) continue;
      if (next === target) return true;
      if (seen.has(next)) continue;
      seen.add(next);
      if (reaches(next, target, skip, seen)) return true;
    }
    return false;
  }

  const redundant = [];
  for (const node of nodes) {
    for (const prereq of prereqsOf.get(node.id) || []) {
      if (!prereqsOf.has(prereq)) continue; // unknown — already reported
      for (const other of prereqsOf.get(node.id)) {
        if (other === prereq || !prereqsOf.has(other)) continue;
        if (reaches(other, prereq, [node.id, prereq])) {
          redundant.push([node.id, prereq, other]);
          break;
        }
      }
    }
  }
  return redundant;
}

/**
 * Depth-first cycle detection over the prereq edges. The row check above
 * already rules cycles out for a well-formed graph, but it is skipped when a
 * row is missing or non-integer — and an un-caught cycle would hang the
 * downstream unlock computation on the page.
 */
function findCycles(nodes) {
  const edges = new Map(nodes.map(n => [n.id, (n.prereqs || []).filter(p => n.id !== p)]));
  const selfLoops = nodes.filter(n => (n.prereqs || []).includes(n.id)).map(n => [n.id, n.id]);
  const state = new Map();
  const cycles = [];
  const stack = [];

  function visit(id) {
    if (state.get(id) === 'done') return;
    if (state.get(id) === 'open') {
      cycles.push([...stack.slice(stack.indexOf(id)), id]);
      return;
    }
    if (!edges.has(id)) return; // unknown prereq — already reported
    state.set(id, 'open');
    stack.push(id);
    for (const next of edges.get(id)) visit(next);
    stack.pop();
    state.set(id, 'done');
  }

  for (const node of nodes) visit(node.id);
  return [...selfLoops, ...cycles];
}

// ── Assembly ─────────────────────────────────────────────────────────────────

/**
 * Resolves each node's problem ids against the README index and stamps on the
 * layout fields the page needs: `row` (authored) plus `col`/`rowSize`, which
 * place the node horizontally within its row in authored order.
 */
function buildRoadmap(roadmap, problems, sheetTitles = new Map()) {
  const rowCounts = new Map();
  for (const node of roadmap.nodes) {
    rowCounts.set(node.row, (rowCounts.get(node.row) || 0) + 1);
  }

  const seenInRow = new Map();
  const nodes = roadmap.nodes.map(node => {
    const col = seenInRow.get(node.row) || 0;
    seenInRow.set(node.row, col + 1);
    return {
      id: node.id,
      title: node.title,
      blurb: node.blurb || '',
      row: node.row,
      col,
      rowSize: rowCounts.get(node.row),
      prereqs: node.prereqs || [],
      sheets: (node.sheets || []).map(slug => ({
        slug,
        title: sheetTitles.get(slug) || slug,
        url: `cheatsheets/${slug}.html`
      })),
      problems: node.problems.map(raw => {
        const p = problems.get(String(raw));
        return {
          id: p.id,
          title: p.title,
          url: p.url,
          difficulty: p.difficulty,
          solutions: p.solutions
        };
      })
    };
  });

  const allIds = new Set();
  nodes.forEach(n => n.problems.forEach(p => allIds.add(p.id)));

  return {
    meta: roadmap.meta || {},
    nodes,
    stats: {
      topics: nodes.length,
      problems: allIds.size,
      // Counted distinctly: a problem that two topics both list (LC 323 sits in
      // both Graphs and Union Find) is one problem to solve, not two.
      problemSlots: nodes.reduce((sum, n) => sum + n.problems.length, 0),
      rows: rowCounts.size
    }
  };
}

// ── Entry point ──────────────────────────────────────────────────────────────

function main() {
  const roadmap = JSON.parse(fs.readFileSync('data/roadmap.json', 'utf8'));
  const problems = parseReadmeProblems(fs.readFileSync('README.md', 'utf8'));
  console.log(`Indexed ${problems.size} problems from README.md`);

  const cheatsheetMeta = JSON.parse(fs.readFileSync('data/cheatsheet_meta.json', 'utf8'));
  const sheetTitles = buildSheetTitles('doc/cheatsheet', cheatsheetMeta);

  const errors = validateGraph(roadmap.nodes, { problems, sheetSlugs: new Set(sheetTitles.keys()) });
  if (errors.length) {
    throw new Error(`data/roadmap.json is inconsistent:\n  - ${errors.join('\n  - ')}`);
  }

  const built = buildRoadmap(roadmap, problems, sheetTitles);
  fs.mkdirSync('_site/data', { recursive: true });
  fs.writeFileSync('_site/data/roadmap.json', JSON.stringify(built));
  console.log(
    // Fully qualified on purpose: `data/roadmap.json` is the hand-authored
    // input, so logging that path for the output reads as "your source file
    // was overwritten".
    `✓ Created _site/data/roadmap.json (${built.stats.topics} topics over ${built.stats.rows} rows, ` +
    `${built.stats.problems} distinct problems in ${built.stats.problemSlots} slots)`
  );
}

if (require.main === module) {
  try {
    main();
  } catch (err) {
    console.error(`❌ ${err.message}`);
    process.exit(1);
  }
}

module.exports = {
  GH_BLOB,
  parseReadmeProblems,
  parseSolutionLinks,
  buildSheetTitles,
  validateGraph,
  findCycles,
  findRedundantEdges,
  buildRoadmap
};
