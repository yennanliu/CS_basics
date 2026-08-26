const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const os = require('node:os');

const lib = require('../build-roadmap.js');

const ROOT = path.join(__dirname, '..', '..');

// ── parseReadmeProblems ───────────────────────────────────────────────────

const README_SAMPLE = [
  '## Array',
  '',
  '| # | Problem | Solution | Time | Space | Difficulty | Tag | Note |',
  '|---|---------|----------|------|-------|------------|-----|------|',
  '| 026 | [Remove Duplicates](https://leetcode.com/problems/remove-duplicates/) | [Python](./leetcode_python/Array/rd.py), [Java](./leetcode_java/RD.java) | _O(n)_ | _O(1)_ | Easy | **array** | AGAIN |',
  '| 121 | [Best Time to Buy](https://leetcode.com/problems/best-time/) | [Java](./leetcode_java/BT.java) | _O(n)_ | _O(1)_ | Easy | **array** |  |',
  '',
  '## Graph',
  '',
  '| 026 | [Remove Duplicates](https://leetcode.com/problems/remove-duplicates/) | [Scala](./leetcode_scala/rd.scala) | _O(n)_ | _O(1)_ | Medium | **graph** |  |',
].join('\n');

test('parseReadmeProblems normalises the zero-padded id column', () => {
  const problems = lib.parseReadmeProblems(README_SAMPLE);
  assert.ok(problems.has('26'), 'id 026 should be indexed as "26"');
  assert.ok(!problems.has('026'));
  assert.equal(problems.get('26').title, 'Remove Duplicates');
});

test('parseReadmeProblems turns repo-relative solution paths into GitHub blob URLs', () => {
  const solutions = lib.parseReadmeProblems(README_SAMPLE).get('121').solutions;
  assert.deepEqual(solutions, { Java: `${lib.GH_BLOB}/leetcode_java/BT.java` });
});

// A problem listed under two sections lists different languages in each. Losing
// one of them would silently drop a solution link the repo actually has.
test('parseReadmeProblems unions solution links across duplicate rows', () => {
  const problem = lib.parseReadmeProblems(README_SAMPLE).get('26');
  assert.deepEqual(Object.keys(problem.solutions).sort(), ['Java', 'Python', 'Scala']);
  // First row wins for the scalar fields, so the topic tables cannot fight over them.
  assert.equal(problem.difficulty, 'Easy');
  assert.equal(problem.section, 'Array');
});

// README has both `[Swim in Rising Water]( https://…)` and `[Java ](./path)`.
// A strict link pattern drops those rows, and the roadmap build then fails on a
// problem id that is demonstrably in the file.
test('parseReadmeProblems tolerates stray whitespace inside markdown links', () => {
  const problems = lib.parseReadmeProblems(
    '| 778 | [Swim in Rising Water]( https://leetcode.com/problems/swim/) ' +
    '| [Java ](./leetcode_java/Swim.java) | _O(n)_ | _O(n)_ | Medium | **graph** |  |'
  );
  const problem = problems.get('778');
  assert.equal(problem.url, 'https://leetcode.com/problems/swim/');
  assert.deepEqual(problem.solutions, { Java: `${lib.GH_BLOB}/leetcode_java/Swim.java` });
});

test('parseReadmeProblems keeps a row whose difficulty column is malformed', () => {
  const problems = lib.parseReadmeProblems(
    '| 1242 | [Web Crawler](https://leetcode.com/problems/web-crawler/) | + \\ |  |  |  |  |  |'
  );
  assert.equal(problems.get('1242').difficulty, 'Unknown');
});

test('parseReadmeProblems skips separator rows and rows with no linked title', () => {
  const problems = lib.parseReadmeProblems([
    '| # | Problem |',
    '|---|---------|',
    '| 42 | Trapping Rain Water |',
  ].join('\n'));
  assert.equal(problems.size, 0);
});

// ── parseSolutionLinks ────────────────────────────────────────────────────

test('parseSolutionLinks passes absolute URLs through unchanged', () => {
  assert.deepEqual(
    lib.parseSolutionLinks('[C++](https://example.test/a.cpp)'),
    { 'C++': 'https://example.test/a.cpp' }
  );
});

test('parseSolutionLinks returns an empty map for an empty cell', () => {
  assert.deepEqual(lib.parseSolutionLinks(''), {});
});

// ── validateGraph ─────────────────────────────────────────────────────────

function ctx() {
  return {
    problems: new Map([['1', { id: '1' }], ['2', { id: '2' }]]),
    sheetSlugs: new Set(['array', 'heap'])
  };
}

const OK_NODES = [
  { id: 'a', title: 'A', row: 0, prereqs: [], sheets: ['array'], problems: [1] },
  { id: 'b', title: 'B', row: 1, prereqs: ['a'], sheets: ['heap'], problems: [2] }
];

test('validateGraph accepts a well-formed graph', () => {
  assert.deepEqual(lib.validateGraph(OK_NODES, ctx()), []);
});

test('validateGraph rejects an edge that does not point downward', () => {
  const nodes = [
    { id: 'a', title: 'A', row: 1, prereqs: [], problems: [1] },
    { id: 'b', title: 'B', row: 1, prereqs: ['a'], problems: [2] }
  ];
  const errors = lib.validateGraph(nodes, ctx());
  assert.equal(errors.length, 1);
  assert.match(errors[0], /must sit below its prereq "a"/);
});

test('validateGraph reports unknown prereqs, sheets and problem ids', () => {
  const nodes = [{ id: 'a', title: 'A', row: 0, prereqs: ['ghost'], sheets: ['nope'], problems: [999] }];
  const errors = lib.validateGraph(nodes, ctx());
  assert.equal(errors.length, 3);
  assert.ok(errors.some(e => /unknown prereq "ghost"/.test(e)));
  assert.ok(errors.some(e => /unknown cheatsheet "nope"/.test(e)));
  assert.ok(errors.some(e => /#999, which is not in README/.test(e)));
});

test('validateGraph reports duplicates rather than quietly de-duplicating them', () => {
  const nodes = [
    { id: 'a', title: 'A', row: 0, prereqs: [], problems: [1, 1] },
    { id: 'a', title: 'A again', row: 1, prereqs: ['a', 'a'], problems: [2] }
  ];
  const errors = lib.validateGraph(nodes, ctx());
  assert.ok(errors.some(e => /duplicate node id "a"/.test(e)));
  assert.ok(errors.some(e => /repeats problem #1/.test(e)));
  assert.ok(errors.some(e => /repeats a prereq/.test(e)));
});

test('validateGraph rejects a node with no problems and no row', () => {
  const errors = lib.validateGraph([{ id: 'a', title: 'A', problems: [] }], ctx());
  assert.ok(errors.some(e => /needs an integer "row"/.test(e)));
  assert.ok(errors.some(e => /lists no problems/.test(e)));
});

// ── findCycles ────────────────────────────────────────────────────────────

// Rows normally make a cycle impossible, but the row check is skipped when a
// row is missing — and a cycle would hang the page's unlock computation.
test('findCycles catches a loop that the row check cannot see', () => {
  const cycles = lib.findCycles([
    { id: 'a', prereqs: ['c'] },
    { id: 'b', prereqs: ['a'] },
    { id: 'c', prereqs: ['b'] }
  ]);
  assert.equal(cycles.length, 1);
  assert.deepEqual(cycles[0], ['a', 'c', 'b', 'a']);
});

test('findCycles catches a node that lists itself', () => {
  assert.deepEqual(lib.findCycles([{ id: 'a', prereqs: ['a'] }]), [['a', 'a']]);
});

test('findCycles returns nothing for a diamond', () => {
  assert.deepEqual(lib.findCycles([
    { id: 'a', prereqs: [] },
    { id: 'b', prereqs: ['a'] },
    { id: 'c', prereqs: ['a'] },
    { id: 'd', prereqs: ['b', 'c'] }
  ]), []);
});

// ── findRedundantEdges ────────────────────────────────────────────────────

// The roadmap has to stay a transitive reduction or the picture turns to
// spaghetti: "design after linked-list" was true but already implied by
// design → heap → trees → linked-list, and drawing it cost a three-row line.
test('findRedundantEdges flags an edge the graph already implies', () => {
  const redundant = lib.findRedundantEdges([
    { id: 'a', prereqs: [] },
    { id: 'b', prereqs: ['a'] },
    { id: 'c', prereqs: ['b', 'a'] }
  ]);
  assert.deepEqual(redundant, [['c', 'a', 'b']]);
});

test('findRedundantEdges leaves a genuine diamond alone', () => {
  assert.deepEqual(lib.findRedundantEdges([
    { id: 'a', prereqs: [] },
    { id: 'b', prereqs: ['a'] },
    { id: 'c', prereqs: ['a'] },
    { id: 'd', prereqs: ['b', 'c'] }
  ]), []);
});

test('findRedundantEdges sees through a longer chain', () => {
  const redundant = lib.findRedundantEdges([
    { id: 'a', prereqs: [] },
    { id: 'b', prereqs: ['a'] },
    { id: 'c', prereqs: ['b'] },
    { id: 'd', prereqs: ['c', 'a'] }
  ]);
  assert.deepEqual(redundant, [['d', 'a', 'c']]);
});

test('validateGraph surfaces a redundant edge as an error', () => {
  const errors = lib.validateGraph([
    { id: 'a', title: 'A', row: 0, prereqs: [], problems: [1] },
    { id: 'b', title: 'B', row: 1, prereqs: ['a'], problems: [2] },
    { id: 'c', title: 'C', row: 2, prereqs: ['b', 'a'], problems: [1] }
  ], ctx());
  assert.equal(errors.length, 1);
  assert.match(errors[0], /already reaches through "b"/);
});

// ── buildRoadmap ──────────────────────────────────────────────────────────

test('buildRoadmap resolves problems and stamps the layout position', () => {
  const problems = new Map([
    ['1', { id: '1', title: 'Two Sum', url: 'u1', difficulty: 'Easy', solutions: { Java: 'j' }, section: 'Array' }],
    ['2', { id: '2', title: 'Add Two', url: 'u2', difficulty: 'Medium', solutions: {}, section: 'Linked list' }]
  ]);
  const built = lib.buildRoadmap(
    { meta: { title: 'T' }, nodes: [
      { id: 'a', title: 'A', row: 0, prereqs: [], sheets: ['array'], problems: [1] },
      { id: 'b', title: 'B', row: 1, prereqs: ['a'], sheets: [], problems: [1, 2] },
      { id: 'c', title: 'C', row: 1, prereqs: ['a'], sheets: [], problems: [2] }
    ] },
    problems,
    new Map([['array', 'Array']])
  );

  assert.equal(built.meta.title, 'T');
  assert.deepEqual(built.nodes.map(n => [n.row, n.col, n.rowSize]), [[0, 0, 1], [1, 0, 2], [1, 1, 2]]);
  assert.deepEqual(built.nodes[0].sheets, [{ slug: 'array', title: 'Array', url: 'cheatsheets/array.html' }]);
  // The `section` field is README bookkeeping and has no business shipping.
  assert.deepEqual(Object.keys(built.nodes[0].problems[0]).sort(),
    ['difficulty', 'id', 'solutions', 'title', 'url']);
});

// LC 323 is listed under both Graphs and Union Find. It is one problem to
// solve, so the headline count must not double it.
test('buildRoadmap counts a shared problem once, and its slots twice', () => {
  const problems = new Map([['1', { id: '1', title: 'x', url: 'u', difficulty: 'Easy', solutions: {} }]]);
  const built = lib.buildRoadmap({ nodes: [
    { id: 'a', title: 'A', row: 0, problems: [1] },
    { id: 'b', title: 'B', row: 1, prereqs: ['a'], problems: [1] }
  ] }, problems);
  assert.equal(built.stats.problems, 1);
  assert.equal(built.stats.problemSlots, 2);
  assert.equal(built.stats.topics, 2);
  assert.equal(built.stats.rows, 2);
});

// ── buildSheetTitles ──────────────────────────────────────────────────────

test('buildSheetTitles prefers the meta override, then the H1, and skips the template', () => {
  const dir = fs.mkdtempSync(path.join(os.tmpdir(), 'sheets-'));
  fs.writeFileSync(path.join(dir, '2_pointers.md'), '# Two Pointers\n\ntext\n');
  fs.writeFileSync(path.join(dir, 'n_sum.md'), '# N Sum\n');
  fs.writeFileSync(path.join(dir, '00_template.md'), '# Template\n');
  fs.writeFileSync(path.join(dir, 'notes.txt'), 'ignored');

  const titles = lib.buildSheetTitles(dir, { sheets: { n_sum: { title: 'N Sum (2Sum → kSum)' } } });
  assert.equal(titles.get('2_pointers'), 'Two Pointers');
  assert.equal(titles.get('n_sum'), 'N Sum (2Sum → kSum)');
  assert.equal(titles.size, 2, 'the template and non-markdown files are not sheets');
});

// ── The real data ─────────────────────────────────────────────────────────

// data/roadmap.json is hand-authored, so the checks above only pay off if they
// actually run against it. This is the same gate site/build-roadmap.js applies.
test('the checked-in roadmap passes every validation against the real README', () => {
  const roadmap = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/roadmap.json'), 'utf8'));
  const problems = lib.parseReadmeProblems(fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8'));
  const sheetTitles = lib.buildSheetTitles(
    path.join(ROOT, 'doc/cheatsheet'),
    JSON.parse(fs.readFileSync(path.join(ROOT, 'data/cheatsheet_meta.json'), 'utf8'))
  );
  const errors = lib.validateGraph(roadmap.nodes, {
    problems,
    sheetSlugs: new Set(sheetTitles.keys())
  });
  assert.deepEqual(errors, []);

  // Every roadmap problem should link to a solution in this repo — that link is
  // the whole reason the roadmap reads from README instead of a curated list.
  const built = lib.buildRoadmap(roadmap, problems, sheetTitles);
  const unlinked = [];
  built.nodes.forEach(node => node.problems.forEach(p => {
    if (!Object.keys(p.solutions).length) unlinked.push(`${node.id} #${p.id}`);
  }));
  assert.deepEqual(unlinked, []);
});

// The page reads `roadmap.nodes[].prereqs` to decide what is unlocked, so a
// dangling root would leave a whole branch permanently locked.
test('the checked-in roadmap has exactly one root', () => {
  const roadmap = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/roadmap.json'), 'utf8'));
  const roots = roadmap.nodes.filter(n => !(n.prereqs || []).length);
  assert.deepEqual(roots.map(n => n.id), ['arrays-hashing']);
});
