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

// ── The MUST / google markers ─────────────────────────────────────────────

const ROW = (tags, status) =>
  `| 42 | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) ` +
  `| [Java](./J.java) | _O(n)_ | _O(1)_ | Hard | ${tags} | ${status} |`;

test('the status column marks MUST in any casing', () => {
  for (const status of ['MUST', 'must', '(MUST again)', 'AGAIN*** (5) (MUST)']) {
    assert.equal(lib.parseReadmeProblems(ROW('**array**', status)).get('42').must, true, status);
  }
  assert.equal(lib.parseReadmeProblems(ROW('**array**', 'AGAIN (2)')).get('42').must, false);
});

test('the tags column marks MUST only as a standalone all-caps token', () => {
  assert.equal(lib.parseReadmeProblems(ROW('**array**, MUST, `fb`', '')).get('42').must, true);
  assert.equal(lib.parseReadmeProblems(ROW('**array**, MUSTARD', '')).get('42').must, false);
});

/**
 * 155 README rows leave the trailing status cell blank. Taking "the last
 * non-empty cell" as the status walked back onto the tags cell, where the
 * case-insensitive status rule then classified ordinary prose as a marker —
 * silently widening the MUST list and defeating the strict tag-token rule.
 * `script/extract_must_lc.py` had the same slip and was fixed alongside.
 */
test('an empty status cell does not turn tag prose into a MUST marker', () => {
  const row = ROW('**array**, the window must be non-decreasing', '');
  assert.equal(lib.parseReadmeProblems(row).get('42').must, false);
});

test('the trailing columns are read from the end, so a stray pipe cannot shift them', () => {
  // README carries one row with an extra pipe in the solutions column.
  const row = '| 42 | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) ' +
    '| [Java](./J.java) | + \\ | extra | _O(n)_ | _O(1)_ | Hard | **array**, `google` | MUST |';
  const problem = lib.parseReadmeProblems(row).get('42');
  assert.equal(problem.must, true);
  assert.equal(problem.google, true);
});

test('the google marker is a company tag, not the word inside prose', () => {
  assert.equal(lib.parseReadmeProblems(ROW('**array**, `google`', '')).get('42').google, true);
  assert.equal(lib.parseReadmeProblems(ROW('**array**, google, `fb`', '')).get('42').google, true);
  assert.equal(lib.parseReadmeProblems(ROW('**array**, googler', '')).get('42').google, false);
});

// LC 322 is listed twice and only the second row carries its MUST marker.
// Testing the deduplicated representative would drop it.
test('google and must are unioned across every row for an id', () => {
  const problems = lib.parseReadmeProblems([
    ROW('**array**', ''),
    ROW('**array**, MUST, `google`', '')
  ].join('\n'));
  assert.equal(problems.get('42').must, true);
  assert.equal(problems.get('42').google, true);
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

// A three-topic graph plus one imported list, wired the way data/roadmap.json
// is: the curated ids sit on the nodes, and the imported list is placed by
// taxonomy. #9 exists only on the imported list, so it also covers a problem
// this repo has no README row for.
function scenario() {
  return {
    roadmap: {
      meta: { title: 'T' },
      defaultList: 'roadmap',
      lists: [
        { id: 'roadmap', label: 'Roadmap picks', from: 'curated' },
        { id: 'tagged', label: 'Tagged', from: 'list:tagged', topicFrom: ['neetcode', 'readme'] },
        { id: 'must', label: 'MUST', from: 'readme:must', topicFrom: ['readme'] }
      ],
      topicSources: {
        neetcode: { 'Arrays & Hashing': 'a', 'Linked List': 'b', JavaScript: null },
        leetcodePlan: {},
        readme: { Array: 'a', 'Linked list': 'b', SQL: null }
      },
      nodes: [
        { id: 'a', title: 'A', row: 0, prereqs: [], sheets: ['array'], problems: [1] },
        { id: 'b', title: 'B', row: 1, prereqs: ['a'], sheets: [], problems: [1, 2] },
        { id: 'c', title: 'C', row: 1, prereqs: ['a'], sheets: [], problems: [2] }
      ]
    },
    readme: new Map([
      ['1', { id: '1', title: 'Two Sum', url: 'u1', difficulty: 'Easy', solutions: { Java: 'j' }, section: 'Array', must: true }],
      ['2', { id: '2', title: 'Add Two', url: 'u2', difficulty: 'Medium', solutions: {}, section: 'Linked list', must: false }],
      ['7', { id: '7', title: 'Query', url: 'u7', difficulty: 'Hard', solutions: {}, section: 'SQL', must: true }]
    ]),
    listed: [
      { id: '1', title: 'Two Sum', slug: 'two-sum', difficulty: 'Easy', groups: { neetcode: 'Arrays & Hashing' }, lists: ['tagged'] },
      { id: '9', title: 'Nine', slug: 'nine', difficulty: 'Hard', groups: { neetcode: 'Linked List' }, lists: ['tagged'] },
      { id: '8', title: 'Eight', slug: 'eight', difficulty: 'Easy', groups: { neetcode: 'JavaScript' }, lists: ['tagged'] }
    ]
  };
}

function buildScenario() {
  const { roadmap, readme, listed } = scenario();
  return lib.buildRoadmap(roadmap, readme, new Map([['array', 'Array']]), listed);
}

test('buildRoadmap stamps the layout position and resolves sheet titles', () => {
  const built = buildScenario();
  assert.equal(built.meta.title, 'T');
  assert.equal(built.defaultList, 'roadmap');
  assert.deepEqual(built.nodes.map(n => [n.row, n.col, n.rowSize]), [[0, 0, 1], [1, 0, 2], [1, 1, 2]]);
  assert.deepEqual(built.nodes[0].sheets, [{ slug: 'array', title: 'Array', url: 'cheatsheets/array.html' }]);
});

// The same problem sits on up to seven lists. Repeating its title, difficulty
// and solution links on each would triple the payload, so nodes carry ids and
// the records live once at the top level.
test('buildRoadmap emits ids on the nodes and the records once', () => {
  const built = buildScenario();
  assert.deepEqual(built.nodes[0].lists.roadmap, ['1']);
  assert.deepEqual(Object.keys(built.problems).sort((x, y) => Number(x) - Number(y)), ['1', '2', '9']);
  // `section` and `must` are README bookkeeping and have no business shipping.
  assert.deepEqual(Object.keys(built.problems['1']).sort(),
    ['difficulty', 'solutions', 'title', 'url']);
});

// LC 323 is listed under both Graphs and Union Find. It is one problem to
// solve, so the headline count must not double it.
test('buildRoadmap counts a shared problem once and its slots twice', () => {
  const built = buildScenario();
  const roadmapList = built.lists.find(l => l.id === 'roadmap');
  // The curated list holds #1 twice (topics a and b) and #2 twice (b and c).
  assert.equal(roadmapList.shown, 2);
  assert.equal(roadmapList.slots, 4);
  assert.equal(built.stats.topics, 3);
  assert.equal(built.stats.rows, 2);
});

test('buildRoadmap keeps the curated order but sorts an imported list by difficulty', () => {
  const { roadmap, readme, listed } = scenario();
  roadmap.nodes[1].problems = [2, 1];
  listed.push({ id: '3', title: 'Three', slug: 'three', difficulty: 'Easy',
                groups: { neetcode: 'Linked List' }, lists: ['tagged'] });
  const built = lib.buildRoadmap(roadmap, readme, new Map(), listed);

  assert.deepEqual(built.nodes[1].lists.roadmap, ['2', '1'], 'teaching order survives');
  // Topic b gathers #9 (Hard) and #3 (Easy) from the imported list.
  assert.deepEqual(built.nodes[1].lists.tagged, ['3', '9'], 'easiest first');
});

// A problem on an imported list that this repo has never solved still has to
// render — with a LeetCode link built from the list's own slug.
test('buildRoadmap falls back to the list data for a problem README lacks', () => {
  const built = buildScenario();
  assert.deepEqual(built.problems['9'], {
    title: 'Nine',
    url: 'https://leetcode.com/problems/nine/',
    difficulty: 'Hard',
    solutions: {}
  });
});

test('buildRoadmap reports what a list could not place', () => {
  const built = buildScenario();
  const tagged = built.lists.find(l => l.id === 'tagged');
  // #8 is a JavaScript-only exercise, mapped to null on purpose.
  assert.equal(tagged.total, 3);
  assert.equal(tagged.placed, 2);
  assert.equal(tagged.dropped, 1);
  assert.equal(tagged.curated, false);
});

// The curated list is the only one with a teaching order, and the page keys its
// lock rendering off exactly this flag.
test('buildRoadmap marks only the curated list as curated', () => {
  const built = buildScenario();
  assert.deepEqual(built.lists.map(l => [l.id, l.curated]),
    [['roadmap', true], ['tagged', false], ['must', false]]);
});

// SQL maps to null, so a MUST-marked SQL problem is dropped rather than landing
// in whatever topic happened to be first.
test('buildRoadmap drops a problem whose section is deliberately unmapped', () => {
  const built = buildScenario();
  const must = built.lists.find(l => l.id === 'must');
  assert.equal(must.total, 2, '#1 and #7 carry the marker');
  assert.equal(must.shown, 1);
  assert.equal(must.dropped, 1);
  assert.ok(!built.problems['7'], 'and it never reaches the page');
});

// ── membersOf / resolveTopic ──────────────────────────────────────────────

test('membersOf reads each kind of list from the right place', () => {
  const { roadmap, readme, listed } = scenario();
  const context = { roadmap, readme, listed };
  assert.deepEqual([...lib.membersOf(roadmap.lists[0], context)].sort(), ['1', '2']);
  assert.deepEqual([...lib.membersOf(roadmap.lists[1], context)].sort(), ['1', '8', '9']);
  assert.deepEqual([...lib.membersOf(roadmap.lists[2], context)].sort(), ['1', '7']);
});

test('membersOf refuses a "from" it does not understand', () => {
  const { roadmap, readme, listed } = scenario();
  assert.throws(
    () => lib.membersOf({ id: 'x', from: 'nonsense' }, { roadmap, readme, listed }),
    /unrecognised "from"/
  );
});

// LeetCode files ten of its Top 100 under a catch-all "Misc" group that maps to
// nothing. Falling through to NeetCode's finer classification is what stops
// them being dropped.
test('resolveTopic falls through to the next taxonomy when one does not map', () => {
  const { roadmap, readme, listed } = scenario();
  const context = {
    readme,
    listedById: new Map(listed.map(p => [p.id, p])),
    topicSources: Object.assign({}, roadmap.topicSources, { leetcodePlan: { Misc: null } })
  };
  context.listedById.get('9').groups.leetcodePlan = 'Misc';
  assert.equal(lib.resolveTopic('9', ['leetcodePlan', 'neetcode'], context), 'b');
  assert.equal(lib.resolveTopic('9', ['leetcodePlan'], context), null);
});

test('resolveTopic returns null when nothing places the problem', () => {
  const { roadmap, readme, listed } = scenario();
  const context = {
    readme, listedById: new Map(listed.map(p => [p.id, p])), topicSources: roadmap.topicSources
  };
  assert.equal(lib.resolveTopic('8', ['neetcode', 'readme'], context), null);
});

// ── validateLists ─────────────────────────────────────────────────────────

function listContext(overrides) {
  const { roadmap, readme, listed } = scenario();
  return { roadmap: Object.assign(roadmap, overrides || {}), listed, readme };
}

test('validateLists accepts the scenario as authored', () => {
  const { roadmap, listed, readme } = listContext();
  assert.deepEqual(lib.validateLists(roadmap, { listed, readme }), []);
});

// A taxonomy key nobody mapped would silently drop every problem filed under
// it — a whole "Sliding Window" group vanishing with nothing to show for it.
test('validateLists rejects a taxonomy key that nothing maps', () => {
  const { roadmap, listed, readme } = listContext();
  delete roadmap.topicSources.neetcode['Linked List'];
  const errors = lib.validateLists(roadmap, { listed, readme });
  assert.equal(errors.length, 1);
  assert.match(errors[0], /does not map "Linked List"/);
});

test('validateLists rejects a mapping that points at an unknown topic', () => {
  const { roadmap, listed, readme } = listContext();
  roadmap.topicSources.readme.Array = 'ghost-topic';
  const errors = lib.validateLists(roadmap, { listed, readme });
  assert.equal(errors.length, 1);
  assert.match(errors[0], /points at unknown topic "ghost-topic"/);
});

// A stale entry is the tell that an upstream category was renamed, which is
// exactly when the other half of the pair goes missing too.
test('validateLists rejects a mapping nothing uses', () => {
  const { roadmap, listed, readme } = listContext();
  roadmap.topicSources.neetcode['Old Name'] = 'a';
  const errors = lib.validateLists(roadmap, { listed, readme });
  assert.equal(errors.length, 1);
  assert.match(errors[0], /maps "Old Name", which no problem uses/);
});

test('validateLists rejects a list whose flag no problem carries', () => {
  const { roadmap, listed, readme } = listContext();
  roadmap.lists[1].from = 'list:neverPublished';
  const errors = lib.validateLists(roadmap, { listed, readme });
  assert.ok(errors.some(e => /selects "neverPublished"/.test(e)));
});

test('validateLists rejects duplicate ids, bad "from" values and a stray default', () => {
  const { listed, readme } = listContext();
  const roadmap = {
    defaultList: 'nowhere',
    nodes: [{ id: 'a' }],
    topicSources: { neetcode: {}, leetcodePlan: {}, readme: {} },
    lists: [
      { id: 'dup', label: 'One', from: 'curated' },
      { id: 'dup', label: 'Two', from: 'sideways' },
      { id: 'noTopics', label: 'Three', from: 'readme:must' }
    ]
  };
  const errors = lib.validateLists(roadmap, { listed: [], readme: new Map() });
  assert.ok(errors.some(e => /duplicate list id "dup"/.test(e)));
  assert.ok(errors.some(e => /unrecognised "from"/.test(e)));
  assert.ok(errors.some(e => /needs a "topicFrom"/.test(e)));
  assert.ok(errors.some(e => /defaultList "nowhere"/.test(e)));
  assert.ok(listed && readme);
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
function realInputs() {
  const roadmap = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/roadmap.json'), 'utf8'));
  const problems = lib.parseReadmeProblems(fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8'));
  const sheetTitles = lib.buildSheetTitles(
    path.join(ROOT, 'doc/cheatsheet'),
    JSON.parse(fs.readFileSync(path.join(ROOT, 'data/cheatsheet_meta.json'), 'utf8'))
  );
  const listed = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/problem_lists.json'), 'utf8')).problems;
  return { roadmap, problems, sheetTitles, listed };
}

test('the checked-in roadmap passes every validation against the real inputs', () => {
  const { roadmap, problems, sheetTitles, listed } = realInputs();
  assert.deepEqual(lib.validateGraph(roadmap.nodes, {
    problems,
    sheetSlugs: new Set(sheetTitles.keys())
  }), []);
  assert.deepEqual(lib.validateLists(roadmap, { listed, readme: problems }), []);
});

test('every problem on the curated path links to a solution in this repo', () => {
  const { roadmap, problems, sheetTitles, listed } = realInputs();
  const built = lib.buildRoadmap(roadmap, problems, sheetTitles, listed);
  // That link is the whole reason the curated list is drawn from README rather
  // than from an imported set — the imported ones reach further and are
  // allowed to include problems this repo has not solved.
  const unlinked = [];
  built.nodes.forEach(node => node.lists.roadmap.forEach(id => {
    if (!Object.keys(built.problems[id].solutions).length) unlinked.push(`${node.id} #${id}`);
  }));
  assert.deepEqual(unlinked, []);
});

// The whole point of the picker. A list that placed almost nothing means its
// taxonomy mapping is broken, and the page would show a graph of empty boxes.
test('every list places nearly all of its problems onto topics', () => {
  const { roadmap, problems, sheetTitles, listed } = realInputs();
  const built = lib.buildRoadmap(roadmap, problems, sheetTitles, listed);

  const thin = built.lists.filter(list => list.shown < list.total * 0.95);
  assert.deepEqual(thin.map(l => `${l.id}: ${l.shown}/${l.total}`), []);

  // Every list must reach the page with something in it.
  assert.deepEqual(built.lists.filter(l => !l.shown).map(l => l.id), []);
});

/**
 * The MUST marker is the repo's own, and `script/extract_must_lc.py` owns its
 * definition. parseReadmeProblems reimplements that rule in JS, so this pins
 * the two together against `doc/must_lc_list.md`, which the script generates.
 *
 * Checked in one direction only. The doc is a checked-in snapshot that goes
 * stale whenever README gains a marker and nobody re-runs the script — it is
 * currently one problem behind, which says nothing about this build. What must
 * never happen is the reverse: a problem the script found that the roadmap
 * misses would mean the JS rule had quietly narrowed.
 */
test('the MUST list contains everything script/extract_must_lc.py found', () => {
  const doc = path.join(ROOT, 'doc/must_lc_list.md');
  if (!fs.existsSync(doc)) return; // the generated doc is optional
  const fromDoc = [...fs.readFileSync(doc, 'utf8').matchAll(/^\| *(\d+) *\|/gm)]
    .map(m => String(Number(m[1])));
  assert.ok(fromDoc.length > 100, 'the doc parsed to something usable');

  const { problems } = realInputs();
  const fromBuild = new Set([...problems.values()].filter(p => p.must).map(p => p.id));
  assert.deepEqual(fromDoc.filter(id => !fromBuild.has(id)), [],
    'the JS MUST rule has drifted narrower than script/extract_must_lc.py');
});

// The page reads `roadmap.nodes[].prereqs` to decide what is unlocked, so a
// dangling root would leave a whole branch permanently locked.
test('the checked-in roadmap has exactly one root', () => {
  const roadmap = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/roadmap.json'), 'utf8'));
  const roots = roadmap.nodes.filter(n => !(n.prereqs || []).length);
  assert.deepEqual(roots.map(n => n.id), ['arrays-hashing']);
});
