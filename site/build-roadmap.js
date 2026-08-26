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
 * The same id can appear under more than one section with different language
 * columns. The first row wins for title/difficulty/section, and the solution
 * links are unioned across every row, so a problem does not lose its Scala link
 * just because the Array table listed only Java.
 *
 * `google` and `must` are likewise unioned across rows rather than read off the
 * first one. LC 322 is listed twice and only the second row carries its MUST
 * marker; testing the deduplicated representative would drop it, and the count
 * would then disagree with `script/extract_must_lc.py`.
 */
function parseReadmeProblems(markdown) {
  const problems = new Map();
  let h2 = null;
  let h3 = null;

  for (const line of markdown.split('\n')) {
    const headingTwo = line.match(/^## +(.+?) *$/);
    if (headingTwo) { h2 = headingTwo[1]; h3 = null; continue; }
    // "Newly Added (kamyu104 gap)" holds its tables under `###` subsections.
    // Without this every one of them reads as a single catch-all section that
    // maps to no topic.
    const headingThree = line.match(/^### +(.+?) *$/);
    if (headingThree) { h3 = headingThree[1]; continue; }
    if (!line.startsWith('|')) continue;

    const cells = line.split('|').map(cell => cell.trim());
    // cells[0] is the empty string before the leading pipe.
    const rawId = cells[1];
    if (!rawId || !/^\d+$/.test(rawId)) continue;

    const titleCell = cells[2] || '';
    const titleMatch = titleCell.match(MD_LINK);
    if (!titleMatch) continue;

    const id = String(parseInt(rawId, 10));
    const solutions = parseSolutionLinks(cells[3] || '');
    const tags = cells[7] || '';
    const google = GOOGLE_TAG.test(tags);
    const must = isMustRow(cells);

    const existing = problems.get(id);
    if (existing) {
      Object.assign(existing.solutions, solutions, existing.solutions);
      existing.google = existing.google || google;
      existing.must = existing.must || must;
      continue;
    }
    problems.set(id, {
      id,
      title: titleMatch[1].trim(),
      url: titleMatch[2],
      difficulty: DIFFICULTIES.includes(cells[6]) ? cells[6] : 'Unknown',
      section: h3 || h2,
      solutions,
      google,
      must
    });
  }
  return problems;
}

// A company tag, not the word inside prose. Backticked (`google`) and bare
// (google) both occur in the tags column.
const GOOGLE_TAG = /(?<![a-z])google(?![a-z])/i;

// The MUST marker, matching `script/extract_must_lc.py` so the roadmap's list
// and `doc/must_lc_list.md` cannot drift apart:
//   - trailing status column: any casing counts ("MUST", "(MUST again)")
//   - tags column: only a standalone ALL-CAPS token, so prose like
//     "window must be non-decreasing" is not a flag
const MUST_ANY_CASE = /must/i;
const MUST_TAG_TOKEN = /(?<![A-Za-z])MUST(?![A-Za-z])/;

function isMustRow(cells) {
  const populated = cells.filter(cell => cell !== '');
  const status = populated[populated.length - 1] || '';
  return MUST_ANY_CASE.test(status) || MUST_TAG_TOKEN.test(cells[7] || '');
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
 * Checks the list registry and the three taxonomy → topic maps.
 *
 * A taxonomy key that nobody mapped is an error rather than a silent drop: it
 * means a whole group of problems — every "Sliding Window" problem, say — would
 * quietly disappear from the page with nothing to show that it had.
 */
function validateLists(roadmap, { listed, readme }) {
  const errors = [];
  const lists = roadmap.lists || [];
  const nodeIds = new Set(roadmap.nodes.map(node => node.id));
  const sources = roadmap.topicSources || {};

  if (!lists.length) errors.push('data/roadmap.json has no "lists"');
  const seen = new Set();
  for (const list of lists) {
    if (!list.id) { errors.push('a list is missing an "id"'); continue; }
    if (seen.has(list.id)) errors.push(`duplicate list id "${list.id}"`);
    seen.add(list.id);
    if (!list.label) errors.push(`list "${list.id}" is missing a "label"`);
    if (!/^(curated|list:.+|readme:.+)$/.test(list.from || '')) {
      errors.push(`list "${list.id}" has an unrecognised "from": ${JSON.stringify(list.from)}`);
    }
    if (list.from === 'curated') continue;
    if (!(list.topicFrom || []).length) {
      errors.push(`list "${list.id}" needs a "topicFrom" naming which taxonomies place its problems`);
    }
    for (const taxonomy of list.topicFrom || []) {
      if (!sources[taxonomy]) errors.push(`list "${list.id}" uses unknown taxonomy "${taxonomy}"`);
    }
    const flag = (list.from.match(/^list:(.+)$/) || [])[1];
    if (flag && !listed.some(p => p.lists.includes(flag))) {
      errors.push(`list "${list.id}" selects "${flag}", which no problem in data/problem_lists.json carries`);
    }
  }
  if (roadmap.defaultList && !seen.has(roadmap.defaultList)) {
    errors.push(`defaultList "${roadmap.defaultList}" is not one of the lists`);
  }

  // Every key each source actually uses must appear in its map, mapped to a
  // real topic or explicitly to null ("deliberately not on the roadmap").
  const used = { readme: new Set(), neetcode: new Set(), leetcodePlan: new Set() };
  readme.forEach(problem => { if (problem.section) used.readme.add(problem.section); });
  listed.forEach(problem => {
    Object.entries(problem.groups || {}).forEach(([taxonomy, group]) => {
      if (used[taxonomy]) used[taxonomy].add(group);
    });
  });

  for (const [taxonomy, keys] of Object.entries(used)) {
    const map = sources[taxonomy];
    if (!map) { errors.push(`topicSources is missing "${taxonomy}"`); continue; }
    for (const key of [...keys].sort()) {
      if (!(key in map)) {
        errors.push(`topicSources.${taxonomy} does not map "${key}" — add a topic id, or null to exclude it`);
      } else if (map[key] !== null && !nodeIds.has(map[key])) {
        errors.push(`topicSources.${taxonomy}["${key}"] points at unknown topic "${map[key]}"`);
      }
    }
    for (const key of Object.keys(map)) {
      if (!keys.has(key)) errors.push(`topicSources.${taxonomy} maps "${key}", which no problem uses`);
    }
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

// ── Curated lists ────────────────────────────────────────────────────────────

const DIFFICULTY_RANK = { Easy: 0, Medium: 1, Hard: 2, Unknown: 3 };

/**
 * Which problems belong to `list`, as a Set of LeetCode ids.
 *
 * Three kinds of membership, named by the list's `from` field:
 *   `curated`        — the ids hand-authored on the nodes themselves
 *   `list:<flag>`    — a flag in data/problem_lists.json (Blind 75, NeetCode …)
 *   `readme:<field>` — a marker in README.md's own tables (google, must)
 */
function membersOf(list, { roadmap, listed, readme }) {
  if (list.from === 'curated') {
    const ids = new Set();
    roadmap.nodes.forEach(node => node.problems.forEach(id => ids.add(String(id))));
    return ids;
  }
  const listMatch = list.from.match(/^list:(.+)$/);
  if (listMatch) {
    return new Set(listed.filter(p => p.lists.includes(listMatch[1])).map(p => p.id));
  }
  const readmeMatch = list.from.match(/^readme:(.+)$/);
  if (readmeMatch) {
    const field = readmeMatch[1];
    return new Set([...readme.values()].filter(p => p[field]).map(p => p.id));
  }
  throw new Error(`list "${list.id}" has an unrecognised "from": ${list.from}`);
}

/**
 * Which roadmap topic a problem belongs to, or null.
 *
 * Every source files problems under its own taxonomy — NeetCode's "Arrays &
 * Hashing", LeetCode's plan group "Hashing", README's `## Array` heading — and
 * data/roadmap.json maps each of those onto a topic. A list names the
 * taxonomies to try, in order, so LeetCode's catch-all "Misc" group (which maps
 * to nothing) falls through to NeetCode's finer classification rather than
 * dropping ten problems on the floor.
 */
function resolveTopic(id, order, { listedById, readme, topicSources }) {
  for (const taxonomy of order) {
    const key = taxonomy === 'readme'
      ? (readme.get(id) || {}).section
      : ((listedById.get(id) || {}).groups || {})[taxonomy];
    if (key == null) continue;
    const topic = topicSources[taxonomy][key];
    if (topic) return topic;
  }
  return null;
}

/**
 * Distributes every list's problems across the topics, as
 * `{ listId: { topicId: [ids] } }` plus the per-list tally.
 *
 * Problems that no taxonomy can place are counted as `dropped` rather than
 * silently vanishing: a list whose problems mostly land nowhere is a broken
 * mapping, and the build log has to say so.
 */
function buildLists(roadmap, context) {
  const { listedById, readme } = context;
  const nodeIds = new Set(roadmap.nodes.map(node => node.id));
  const byList = {};
  const summary = [];

  for (const list of roadmap.lists) {
    const members = membersOf(list, context);
    const buckets = {};
    let dropped = 0;

    if (list.from === 'curated') {
      // The curated list keeps its authored order: it is a teaching sequence,
      // not a catalogue, and sorting it by difficulty would destroy that.
      roadmap.nodes.forEach(node => { buckets[node.id] = node.problems.map(String); });
    } else {
      for (const id of members) {
        const topic = resolveTopic(id, list.topicFrom || [], context);
        if (!topic || !nodeIds.has(topic)) { dropped++; continue; }
        (buckets[topic] = buckets[topic] || []).push(id);
      }
      // Imported lists have no inherent order, so present them easiest first —
      // the order you would actually work through them in.
      for (const ids of Object.values(buckets)) ids.sort(byDifficultyThenId(context));
    }

    byList[list.id] = buckets;
    summary.push({
      id: list.id,
      label: list.label,
      blurb: list.blurb || '',
      // Only the curated list is a path. The imported ones are catalogues with
      // no prerequisite order, so the page drops the lock styling for them
      // rather than showing every topic as blocked.
      curated: list.from === 'curated',
      total: members.size,
      placed: members.size - dropped,
      dropped
    });
  }
  return { byList, summary };
}

function byDifficultyThenId({ listedById, readme }) {
  const rank = id => {
    const source = readme.get(id) || listedById.get(id) || {};
    return DIFFICULTY_RANK[source.difficulty] != null ? DIFFICULTY_RANK[source.difficulty] : 3;
  };
  return (a, b) => (rank(a) - rank(b)) || (Number(a) - Number(b));
}

/**
 * The shared problem dictionary the page renders from.
 *
 * README is preferred for every field, because only it knows which solutions
 * this repo actually has. A problem that appears on an imported list but has no
 * README row still gets a title, a difficulty and a LeetCode link from the list
 * data — it simply has no `solutions`, which the page shows as a gap rather
 * than hiding.
 */
function buildProblemDictionary(ids, { readme, listedById }) {
  const dictionary = {};
  for (const id of [...ids].sort((a, b) => Number(a) - Number(b))) {
    const local = readme.get(id);
    const listed = listedById.get(id);
    if (local) {
      dictionary[id] = {
        title: local.title,
        url: local.url,
        difficulty: local.difficulty,
        solutions: local.solutions
      };
    } else {
      dictionary[id] = {
        title: listed.title,
        url: `https://leetcode.com/problems/${listed.slug}/`,
        difficulty: listed.difficulty,
        solutions: {}
      };
    }
  }
  return dictionary;
}

// ── Assembly ─────────────────────────────────────────────────────────────────

/**
 * Resolves each node's problem ids against the README index and stamps on the
 * layout fields the page needs: `row` (authored) plus `col`/`rowSize`, which
 * place the node horizontally within its row in authored order.
 */
function buildRoadmap(roadmap, problems, sheetTitles = new Map(), listed = []) {
  const listedById = new Map(listed.map(p => [p.id, p]));
  const context = {
    roadmap,
    readme: problems,
    listed,
    listedById,
    topicSources: roadmap.topicSources || {}
  };

  const { byList, summary } = buildLists(roadmap, context);

  const rowCounts = new Map();
  for (const node of roadmap.nodes) {
    rowCounts.set(node.row, (rowCounts.get(node.row) || 0) + 1);
  }

  const seenInRow = new Map();
  const nodes = roadmap.nodes.map(node => {
    const col = seenInRow.get(node.row) || 0;
    seenInRow.set(node.row, col + 1);
    const lists = {};
    for (const list of roadmap.lists) lists[list.id] = (byList[list.id] || {})[node.id] || [];
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
      // Problem ids only. The records live once in the top-level dictionary —
      // the same problem sits on up to seven lists, and repeating its title,
      // difficulty and solution links each time would triple the payload.
      lists
    };
  });

  const referenced = new Set();
  nodes.forEach(node => Object.values(node.lists).forEach(ids => ids.forEach(id => referenced.add(id))));

  // Distinct counts throughout: a problem two topics both list (LC 323 sits in
  // both Graphs and Union Find) is one problem to solve, not two.
  const distinct = list => {
    const ids = new Set();
    nodes.forEach(node => node.lists[list.id].forEach(id => ids.add(id)));
    return ids.size;
  };
  const lists = summary.map(entry => Object.assign(entry, {
    shown: distinct({ id: entry.id }),
    slots: nodes.reduce((sum, node) => sum + node.lists[entry.id].length, 0)
  }));

  return {
    meta: roadmap.meta || {},
    defaultList: roadmap.defaultList || roadmap.lists[0].id,
    lists,
    problems: buildProblemDictionary(referenced, context),
    nodes,
    stats: {
      topics: nodes.length,
      problems: referenced.size,
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

  // Curated lists that cannot be derived from this repo (Blind 75, the NeetCode
  // lists, LeetCode's Top 100 Liked). Vendored by script/fetch_problem_lists.py
  // so the build stays offline.
  const listed = JSON.parse(fs.readFileSync('data/problem_lists.json', 'utf8')).problems;
  console.log(`Loaded ${listed.length} problems from data/problem_lists.json`);

  const errors = [
    ...validateGraph(roadmap.nodes, { problems, sheetSlugs: new Set(sheetTitles.keys()) }),
    ...validateLists(roadmap, { listed, readme: problems })
  ];
  if (errors.length) {
    throw new Error(`data/roadmap.json is inconsistent:\n  - ${errors.join('\n  - ')}`);
  }

  const built = buildRoadmap(roadmap, problems, sheetTitles, listed);
  fs.mkdirSync('_site/data', { recursive: true });
  fs.writeFileSync('_site/data/roadmap.json', JSON.stringify(built));
  console.log(
    // Fully qualified on purpose: `data/roadmap.json` is the hand-authored
    // input, so logging that path for the output reads as "your source file
    // was overwritten".
    `✓ Created _site/data/roadmap.json (${built.stats.topics} topics over ${built.stats.rows} rows, ` +
    `${built.stats.problems} distinct problems)`
  );
  for (const list of built.lists) {
    // A list whose problems mostly land nowhere is a broken mapping, and the
    // only place that shows is here.
    const note = list.dropped ? `, ${list.dropped} unplaceable` : '';
    console.log(`    ${list.label.padEnd(18)} ${String(list.shown).padStart(4)} shown` +
      ` of ${list.total}${note}`);
  }
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
  validateLists,
  findCycles,
  findRedundantEdges,
  membersOf,
  resolveTopic,
  buildLists,
  buildProblemDictionary,
  buildRoadmap
};
