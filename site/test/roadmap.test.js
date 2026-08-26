const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { JSDOM } = require('jsdom');
const { setupDOM, teardownDOM, click } = require('./helpers');

// roadmap.js reads `document`/`localStorage` off the global scope at call time,
// so a DOM has to exist before it is required.
setupDOM();
const CSRoadmap = require('../roadmap.js');

const PAGE = path.join(__dirname, '..', 'pages', 'lc-roadmap.html');

// ── Fixture ───────────────────────────────────────────────────────────────

function record(id, extra) {
  return Object.assign({
    title: 'P' + id, url: 'https://leetcode.com/problems/p' + id + '/',
    difficulty: 'Easy', solutions: { Java: 'https://example.test/' + id + '.java' }
  }, extra);
}

/**
 * a → b, a → c, (b, c) → d.
 *
 * `d` also carries P1, which `a` owns: the shared problem is what the
 * distinct-counting and cross-topic rules hang on. The `tagged` list is
 * deliberately lopsided — it skips topic `c` entirely and reaches P9, a problem
 * with no repo solution — so the empty-topic and missing-solution paths are
 * exercised.
 */
function fixture() {
  return {
    meta: { title: 'Study Roadmap', intro: 'intro text' },
    defaultList: 'roadmap',
    lists: [
      { id: 'roadmap', label: 'Roadmap picks', blurb: 'the path', curated: true, shown: 4 },
      { id: 'tagged', label: 'Tagged', blurb: 'a tag list', curated: false, shown: 3 }
    ],
    problems: {
      1: record(1), 2: record(2, { difficulty: 'Hard' }), 3: record(3), 4: record(4),
      9: record(9, { solutions: {} })
    },
    stats: { topics: 4, problems: 5, rows: 3 },
    nodes: [
      { id: 'a', title: 'A', blurb: 'root', row: 0, col: 0, rowSize: 1, prereqs: [],
        sheets: [{ slug: 'array', title: 'Array', url: 'cheatsheets/array.html' }],
        lists: { roadmap: ['1', '2'], tagged: ['1', '9'] } },
      { id: 'b', title: 'B', blurb: '', row: 1, col: 0, rowSize: 2, prereqs: ['a'],
        sheets: [], lists: { roadmap: ['3'], tagged: ['3'] } },
      { id: 'c', title: 'C', blurb: '', row: 1, col: 1, rowSize: 2, prereqs: ['a'],
        sheets: [], lists: { roadmap: ['4'], tagged: [] } },
      { id: 'd', title: 'D', blurb: '', row: 2, col: 0, rowSize: 1, prereqs: ['b', 'c'],
        sheets: [], lists: { roadmap: ['1'], tagged: [] } }
    ]
  };
}

function viewOf(listId) { return CSRoadmap.view(fixture(), listId); }

function solvedSet(ids) {
  const set = Object.create(null);
  ids.forEach((id) => { set[String(id)] = true; });
  return set;
}

// ── view ──────────────────────────────────────────────────────────────────

test('view resolves a list, and falls back to the default for an unknown one', () => {
  assert.equal(CSRoadmap.view(fixture(), 'tagged').list, 'tagged');
  assert.equal(CSRoadmap.view(fixture(), 'no-such-list').list, 'roadmap');
  assert.equal(CSRoadmap.view(fixture(), null).list, 'roadmap');
});

// Only the curated path has a teaching order, so only it locks topics.
test('view marks the curated list and nothing else', () => {
  assert.equal(CSRoadmap.view(fixture(), 'roadmap').curated, true);
  assert.equal(CSRoadmap.view(fixture(), 'tagged').curated, false);
});

// ── Counting, per list ────────────────────────────────────────────────────

test('statsFor counts only what the topic contributes to the current list', () => {
  const roadmap = fixture();
  assert.deepEqual(CSRoadmap.statsFor(roadmap.nodes[0], viewOf('roadmap'), solvedSet([1])), { done: 1, total: 2 });
  assert.deepEqual(CSRoadmap.statsFor(roadmap.nodes[0], viewOf('tagged'), solvedSet([1])), { done: 1, total: 2 });
  assert.deepEqual(CSRoadmap.statsFor(roadmap.nodes[2], viewOf('tagged'), solvedSet([4])), { done: 0, total: 0 });
});

test('isEmpty separates "nothing on this list" from "nothing done yet"', () => {
  const roadmap = fixture();
  assert.equal(CSRoadmap.isEmpty(roadmap.nodes[2], viewOf('roadmap')), false);
  assert.equal(CSRoadmap.isEmpty(roadmap.nodes[2], viewOf('tagged')), true);
});

test('isDone needs every problem, and an empty topic is never done', () => {
  const roadmap = fixture();
  assert.equal(CSRoadmap.isDone(roadmap.nodes[0], viewOf('roadmap'), solvedSet([1])), false);
  assert.equal(CSRoadmap.isDone(roadmap.nodes[0], viewOf('roadmap'), solvedSet([1, 2])), true);
  // Same topic, different list: P1 + P9 rather than P1 + P2.
  assert.equal(CSRoadmap.isDone(roadmap.nodes[0], viewOf('tagged'), solvedSet([1, 2])), false);
  assert.equal(CSRoadmap.isDone(roadmap.nodes[2], viewOf('tagged'), solvedSet([])), false);
});

// The headline figure would otherwise read 5 when there are only 4 problems.
test('distinctSolved counts a problem shared by two topics once', () => {
  const roadmap = fixture();
  assert.equal(CSRoadmap.distinctSolved(roadmap.nodes, viewOf('roadmap'), solvedSet([1])), 1);
  assert.equal(CSRoadmap.distinctSolved(roadmap.nodes, viewOf('roadmap'), solvedSet([1, 2, 3, 4])), 4);
});

test('distinctSolved ignores ticks for problems the current list does not hold', () => {
  const roadmap = fixture();
  // P2 and P4 are on the roadmap list but not on `tagged`.
  assert.equal(CSRoadmap.distinctSolved(roadmap.nodes, viewOf('tagged'), solvedSet([2, 4])), 0);
  assert.equal(CSRoadmap.distinctSolved(roadmap.nodes, viewOf('tagged'), solvedSet([1, 9])), 2);
});

// ── Unlocking ─────────────────────────────────────────────────────────────

test('isUnlocked opens a topic only once every prereq is finished', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const view = viewOf('roadmap');

  assert.equal(CSRoadmap.isUnlocked(byId.a, view, byId, solvedSet([])), true, 'the root is always open');
  assert.equal(CSRoadmap.isUnlocked(byId.b, view, byId, solvedSet([1])), false, 'A is only half done');
  assert.equal(CSRoadmap.isUnlocked(byId.b, view, byId, solvedSet([1, 2])), true);
  // D needs BOTH B and C, so finishing one branch is not enough.
  assert.equal(CSRoadmap.isUnlocked(byId.d, view, byId, solvedSet([1, 2, 3])), false);
  assert.equal(CSRoadmap.isUnlocked(byId.d, view, byId, solvedSet([1, 2, 3, 4])), true);
});

// An imported list is a catalogue, not a path — locking every topic behind a
// prerequisite nobody has finished would render the whole graph as blocked.
test('a non-curated list locks nothing', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const view = viewOf('tagged');
  assert.equal(CSRoadmap.isUnlocked(byId.d, view, byId, solvedSet([])), true);
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.d, view, byId, solvedSet([])), []);
  assert.equal(CSRoadmap.lockLabel(byId.d, view, byId, solvedSet([])), '');
});

test('unmetPrereqs names the topics still in the way, in authored order', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const view = viewOf('roadmap');
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.d, view, byId, solvedSet([1, 2, 3])), ['c']);
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.d, view, byId, solvedSet([1, 2])), ['b', 'c']);
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.a, view, byId, solvedSet([])), []);
});

// build-roadmap.js fails the build on an unknown prereq, so if one ever reaches
// the page it is better to leave the branch reachable than to strand it.
test('an unknown prereq id does not lock a topic forever', () => {
  const node = { id: 'x', title: 'X', prereqs: ['ghost'], lists: { roadmap: ['9'] } };
  const view = viewOf('roadmap');
  assert.equal(CSRoadmap.isUnlocked(node, view, { x: node }, solvedSet([])), true);
});

test('lockLabel names the unmet prereqs and is empty once they are met', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const view = viewOf('roadmap');
  assert.equal(CSRoadmap.lockLabel(byId.d, view, byId, solvedSet([1, 2])), 'Finish first: B, C');
  assert.equal(CSRoadmap.lockLabel(byId.d, view, byId, solvedSet([1, 2, 3, 4])), '');
  assert.equal(CSRoadmap.lockLabel(byId.a, view, byId, solvedSet([])), '');
});

// ── nextUp ────────────────────────────────────────────────────────────────

test('nextUp starts at the root and names its first unsolved problem', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const next = CSRoadmap.nextUp(roadmap.nodes, viewOf('roadmap'), byId, solvedSet([1]));
  assert.equal(next.node.id, 'a');
  assert.equal(next.problem.id, '2');
});

test('nextUp prefers the shallowest open topic and skips locked ones', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  // A done, B and C open at row 1: the left-most (authored first) wins. D is
  // still locked, so it must not be suggested even though it is unfinished.
  const next = CSRoadmap.nextUp(roadmap.nodes, viewOf('roadmap'), byId, solvedSet([1, 2]));
  assert.equal(next.node.id, 'b');
});

test('nextUp skips a topic the current list has nothing in', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  // On `tagged`, C and D are empty, so finishing A leaves only B.
  const next = CSRoadmap.nextUp(roadmap.nodes, viewOf('tagged'), byId, solvedSet([1, 9]));
  assert.equal(next.node.id, 'b');
});

test('nextUp returns null when everything on the list is solved', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  assert.equal(CSRoadmap.nextUp(roadmap.nodes, viewOf('roadmap'), byId, solvedSet([1, 2, 3, 4])), null);
  assert.equal(CSRoadmap.nextUp(roadmap.nodes, viewOf('tagged'), byId, solvedSet([1, 9, 3])), null);
});

// ── Markup ────────────────────────────────────────────────────────────────

function parse(html) {
  return new JSDOM(`<!DOCTYPE html><html><body>${html}</body></html>`).window.document;
}

test('graphHTML emits one .row per row, in ascending order', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const doc = parse(CSRoadmap.graphHTML(roadmap.nodes, viewOf('roadmap'), byId, solvedSet([])));
  const rows = [...doc.querySelectorAll('.row')];
  assert.deepEqual(rows.map((r) => r.getAttribute('data-row')), ['0', '1', '2']);
  assert.equal(rows[1].querySelectorAll('.node').length, 2);
});

test('nodeHTML marks a finished topic done and an unmet one locked', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const view = viewOf('roadmap');

  const fresh = parse(CSRoadmap.nodeHTML(byId.b, view, byId, solvedSet([]))).querySelector('.node');
  assert.ok(fresh.classList.contains('locked'));
  assert.ok(!fresh.classList.contains('done'));
  assert.equal(fresh.getAttribute('title'), 'B — 0 of 1 solved. Finish first: A');

  const open = parse(CSRoadmap.nodeHTML(byId.b, view, byId, solvedSet([1, 2, 3]))).querySelector('.node');
  assert.ok(open.classList.contains('done'));
  assert.ok(!open.classList.contains('locked'));
  assert.equal(open.getAttribute('title'), 'B — 1 of 1 solved');
  assert.equal(open.querySelector('.node-count').textContent, '1/1');
});

test('nodeHTML fades a topic the list has nothing in, and shows a dash', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const node = parse(CSRoadmap.nodeHTML(byId.c, viewOf('tagged'), byId, solvedSet([]))).querySelector('.node');
  assert.ok(node.classList.contains('empty'));
  assert.ok(!node.classList.contains('done'), 'zero of zero is not "done"');
  assert.equal(node.querySelector('.node-count').textContent, '—');
  assert.equal(node.getAttribute('title'), 'C — nothing on this list');
});

test('problemHTML links every language the repo has', () => {
  const withJava = parse(CSRoadmap.problemHTML(
    { id: '1', title: 'P', url: 'u', difficulty: 'Easy',
      solutions: { Java: 'https://x.test/a.java', Python: 'https://x.test/a.py' } },
    solvedSet([])
  ));
  assert.deepEqual([...withJava.querySelectorAll('.prob-links a')].map((a) => a.textContent), ['Ja', 'Py']);
});

// An imported list reaches past what this repo has solved. A silent gap reads
// as a rendering bug, so the row says so instead.
test('problemHTML marks a problem this repo has no solution for', () => {
  const bare = parse(CSRoadmap.problemHTML(
    { id: '9', title: 'P9', url: 'u', difficulty: 'Easy', solutions: {} }, solvedSet([])
  ));
  assert.equal(bare.querySelectorAll('.prob-links a').length, 0);
  assert.equal(bare.querySelector('.prob-gap').getAttribute('title'), 'No solution in this repo yet');
});

test('problemHTML reflects the solved tick in both the class and the checkbox', () => {
  const doc = parse(CSRoadmap.problemHTML(
    { id: '7', title: 'P7', url: 'u', difficulty: 'Easy', solutions: {} }, solvedSet([7])
  ));
  assert.ok(doc.querySelector('.prob').classList.contains('solved'));
  assert.equal(doc.querySelector('input').checked, true);
});

test('problemHTML escapes a title that would otherwise inject markup', () => {
  const doc = parse(CSRoadmap.problemHTML(
    { id: '1', title: '<img src=x onerror=alert(1)>', url: 'u', difficulty: 'Easy', solutions: {} },
    solvedSet([])
  ));
  assert.equal(doc.querySelectorAll('img').length, 0);
  assert.equal(doc.querySelector('.prob-title').textContent, '<img src=x onerror=alert(1)>');
});

test('drawerBodyHTML ticks the prereqs that are met and links the cheatsheets', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const view = viewOf('roadmap');

  const rootDoc = parse(CSRoadmap.drawerBodyHTML(byId.a, view, byId, solvedSet([])));
  assert.equal(rootDoc.querySelectorAll('.chip.met').length, 0, 'the root has no prereq section');
  assert.equal(rootDoc.querySelector('a.chip').getAttribute('href'), 'cheatsheets/array.html');
  assert.equal(rootDoc.querySelectorAll('.prob').length, 2);

  const dDoc = parse(CSRoadmap.drawerBodyHTML(byId.d, view, byId, solvedSet([1, 2, 3])));
  const chips = [...dDoc.querySelectorAll('[data-open]')];
  assert.deepEqual(chips.map((c) => c.getAttribute('data-open')), ['b', 'c']);
  assert.deepEqual(chips.map((c) => c.classList.contains('met')), [true, false]);
});

test('drawerBodyHTML drops the prereq section on a list that is not a path', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const doc = parse(CSRoadmap.drawerBodyHTML(byId.d, viewOf('tagged'), byId, solvedSet([])));
  assert.equal(doc.querySelectorAll('[data-open]').length, 0);
});

test('drawerBodyHTML explains an empty topic instead of showing a bare heading', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const doc = parse(CSRoadmap.drawerBodyHTML(byId.c, viewOf('tagged'), byId, solvedSet([])));
  assert.equal(doc.querySelectorAll('.prob').length, 0);
  assert.match(doc.querySelector('.drawer-empty').textContent, /Tagged has nothing filed under this topic/);
});

test('drawerBodyHTML heads the problem section with the list and its tally', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const doc = parse(CSRoadmap.drawerBodyHTML(byId.a, viewOf('tagged'), byId, solvedSet([1])));
  assert.equal(doc.querySelectorAll('.drawer-section h3')[1].textContent, 'Tagged — 1 / 2');
});

test('listOptionsHTML labels each list with its size and selects the current one', () => {
  const doc = parse('<select>' + CSRoadmap.listOptionsHTML(fixture().lists, 'tagged') + '</select>');
  const options = [...doc.querySelectorAll('option')];
  assert.deepEqual(options.map((o) => o.textContent), ['Roadmap picks (4)', 'Tagged (3)']);
  assert.deepEqual(options.map((o) => o.selected), [false, true]);
});

test('edgePath leaves the parent and enters the child vertically', () => {
  assert.equal(CSRoadmap.edgePath(10, 20, 50, 120), 'M10 20 C10 70 50 70 50 120');
});

// ── Storage ───────────────────────────────────────────────────────────────

test('readSolved survives a missing, malformed or non-array value', () => {
  setupDOM();
  assert.deepEqual(CSRoadmap.readSolved(), Object.create(null));

  localStorage.setItem(CSRoadmap.STORE_KEY, 'not json');
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()), []);

  localStorage.setItem(CSRoadmap.STORE_KEY, '{"1":true}');
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()), []);
});

test('writeSolved round-trips through readSolved as string keys', () => {
  setupDOM();
  CSRoadmap.writeSolved(solvedSet([1, 322]));
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()).sort(), ['1', '322']);
});

// ── The page, end to end ──────────────────────────────────────────────────

/**
 * Loads the real lc-roadmap.html into jsdom, mounts the navbar, and hands the
 * fixture to CSRoadmap.render — the same path the browser takes minus the
 * fetch. This is what catches an id in the markup drifting away from the id
 * roadmap.js looks up.
 */
function renderPage(roadmap, url, stored) {
  const html = fs.readFileSync(PAGE, 'utf8');
  const dom = new JSDOM(html, { url: url || 'https://example.test/lc-roadmap.html' });
  for (const key of ['window', 'document', 'localStorage', 'CustomEvent', 'Event', 'navigator', 'self', 'history', 'location']) {
    global[key] = key === 'self' ? dom.window : dom.window[key];
  }
  // Each JSDOM carries its own localStorage, so anything a returning visitor
  // would have stored has to be seeded into *this* document, not the last one.
  Object.entries(stored || {}).forEach(([key, value]) => dom.window.localStorage.setItem(key, value));
  require('../nav.js').mount();
  CSRoadmap.render(roadmap || fixture());
  return dom.window.document;
}

test.after(() => { teardownDOM(); });

test('the page renders every topic and reveals the summary', () => {
  const doc = renderPage();
  assert.equal(doc.querySelectorAll('.node').length, 4);
  assert.equal(doc.getElementById('loading').hidden, true);
  assert.equal(doc.getElementById('summary').hidden, false);
  assert.equal(doc.getElementById('statProblems').textContent, '0 / 4');
  assert.equal(doc.getElementById('statTopics').textContent, '0 / 4');
  assert.equal(doc.getElementById('pageIntro').textContent, 'intro text');
});

test('the navbar marks the roadmap as the active page', () => {
  const doc = renderPage();
  const active = doc.querySelector('.nav-links a.active');
  assert.equal(active.getAttribute('href'), 'lc-roadmap.html');
});

test('closing the drawer hands focus back to the topic that opened it', () => {
  const doc = renderPage();
  const box = doc.querySelector('.node[data-id="a"]');
  click(box);
  assert.equal(doc.activeElement, doc.getElementById('drawerClose'));

  click(doc.getElementById('overlay'));
  assert.equal(doc.activeElement, box);
});

test('hopping between prereqs returns focus to the topic you started from', () => {
  const doc = renderPage();
  const box = doc.querySelector('.node[data-id="d"]');
  click(box);
  click(doc.getElementById('drawerBody').querySelector('[data-open="b"]'));
  click(doc.getElementById('drawerClose'));
  assert.equal(doc.activeElement, box);
});

// `state` outlives a render. A second render that inherited the previous
// document's open topic silently kept a focus target pointing at a dead node.
test('a fresh render starts with the drawer shut', () => {
  const first = renderPage();
  click(first.querySelector('.node[data-id="a"]'));
  assert.ok(first.getElementById('drawer').classList.contains('open'));

  const second = renderPage();
  assert.ok(!second.getElementById('drawer').classList.contains('open'));
  const box = second.querySelector('.node[data-id="b"]');
  click(box);
  click(second.getElementById('drawerClose'));
  assert.equal(second.activeElement, box, 'focus returns inside the current document');
});

test('the overlay closes the drawer and clears the hash', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('overlay'));
  assert.ok(!doc.getElementById('drawer').classList.contains('open'));
  assert.equal(global.location.hash, '');
});

test('every prereq becomes one edge, tagged with the pair it joins', () => {
  const doc = renderPage();
  const edges = [...doc.querySelectorAll('#edges path')]
    .map((p) => [p.getAttribute('data-from'), p.getAttribute('data-to')]);
  assert.deepEqual(edges, [['a', 'b'], ['a', 'c'], ['b', 'd'], ['c', 'd']]);
});

test('hovering a topic lifts only the edges that touch it', () => {
  const doc = renderPage();
  const svg = doc.getElementById('edges');

  CSRoadmap.highlightEdges('b');
  assert.ok(svg.classList.contains('focused'));
  assert.deepEqual(
    [...svg.querySelectorAll('path')].map((p) => p.classList.contains('hot')),
    [true, false, true, false]
  );

  CSRoadmap.highlightEdges(null);
  assert.ok(!svg.classList.contains('focused'));
  assert.equal(svg.querySelectorAll('path.hot').length, 0);
});

// The whole point of keying progress by problem id: A and D share P1.
test('ticking a shared problem updates every topic that lists it', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="a"]'));

  const box = doc.getElementById('drawerBody').querySelector('input[data-check="1"]');
  box.checked = true;
  box.dispatchEvent(new global.window.Event('change', { bubbles: true }));

  assert.equal(doc.querySelector('.node[data-id="a"] .node-count').textContent, '1/2');
  assert.equal(doc.querySelector('.node[data-id="d"] .node-count').textContent, '1/1');
  assert.ok(doc.querySelector('.node[data-id="d"]').classList.contains('done'));
  assert.equal(doc.getElementById('statProblems').textContent, '1 / 4');
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()), ['1'], 'and it is persisted');
});

test('"tick all" completes a topic and unlocks what came after it', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-bulk="all"]'));

  assert.ok(doc.querySelector('.node[data-id="a"]').classList.contains('done'));
  assert.ok(!doc.querySelector('.node[data-id="b"]').classList.contains('locked'));
  assert.ok(doc.querySelector('.node[data-id="d"]').classList.contains('locked'));

  click(doc.getElementById('drawerBody').querySelector('[data-bulk="none"]'));
  assert.ok(!doc.querySelector('.node[data-id="a"]').classList.contains('done'));
  assert.ok(doc.querySelector('.node[data-id="b"]').classList.contains('locked'));
});

test('the next-up hint follows progress and jumps to the topic', () => {
  const doc = renderPage();
  const hint = doc.getElementById('nextUp');
  assert.equal(hint.hidden, false);
  assert.equal(hint.querySelector('[data-open]').getAttribute('data-open'), 'a');
  assert.match(hint.querySelector('.where').textContent, /#1 P1/);

  click(hint.querySelector('[data-open]'));
  assert.equal(doc.getElementById('drawerTitle').textContent, 'A');
});

test('a prereq chip in the drawer navigates to that prereq', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="d"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-open="b"]'));
  assert.equal(doc.getElementById('drawerTitle').textContent, 'B');
});

test('reset clears every tick and the stored value', () => {
  const doc = renderPage();
  // roadmap.js calls the bare `confirm`, which resolves against the Node
  // global here — not `window`. Stubbing the wrong one leaves the guard
  // short-circuited and this test silently skips the dialog branch.
  let asked = 0;
  global.confirm = () => { asked++; return true; };
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-bulk="all"]'));
  assert.equal(doc.getElementById('statProblems').textContent, '2 / 4');

  click(doc.getElementById('resetBtn'));
  assert.equal(asked, 1, 'reset must ask before wiping progress');
  assert.equal(doc.getElementById('statProblems').textContent, '0 / 4');
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()), []);
});

test('declining the confirm leaves progress untouched', () => {
  const doc = renderPage();
  global.confirm = () => false;
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-bulk="all"]'));

  click(doc.getElementById('resetBtn'));
  assert.equal(doc.getElementById('statProblems').textContent, '2 / 4');
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()).sort(), ['1', '2']);
});

// Reset on an untouched board should not pop a dialog asking to clear nothing.
test('reset does not ask when there is nothing to clear', () => {
  const doc = renderPage();
  let asked = 0;
  global.confirm = () => { asked++; return true; };
  click(doc.getElementById('resetBtn'));
  assert.equal(asked, 0);
});

test('a topic named in the URL hash opens on load', () => {
  const doc = renderPage(null, 'https://example.test/lc-roadmap.html#c');
  assert.equal(doc.getElementById('drawerTitle').textContent, 'C');
});

// ── Switching lists ───────────────────────────────────────────────────────

function switchTo(doc, listId) {
  const select = doc.getElementById('listSelect');
  select.value = listId;
  select.dispatchEvent(new global.window.Event('change', { bubbles: true }));
}

test('the picker offers every list, defaulting to the configured one', () => {
  const doc = renderPage();
  const options = [...doc.querySelectorAll('#listSelect option')];
  assert.deepEqual(options.map((o) => o.value), ['roadmap', 'tagged']);
  assert.equal(doc.getElementById('listSelect').value, 'roadmap');
  assert.equal(doc.getElementById('listBlurb').textContent, 'the path');
});

test('switching lists re-counts every topic without touching the graph', () => {
  const doc = renderPage();
  assert.equal(doc.querySelector('.node[data-id="c"] .node-count').textContent, '0/1');

  switchTo(doc, 'tagged');
  assert.equal(doc.getElementById('statProblems').textContent, '0 / 3');
  assert.equal(doc.getElementById('listBlurb').textContent, 'a tag list');
  assert.match(doc.getElementById('summaryLabel').textContent, /of Tagged solved$/);
  // C and D hold nothing on this list; A now shows P1 + P9 rather than P1 + P2.
  assert.equal(doc.querySelector('.node[data-id="a"] .node-count').textContent, '0/2');
  assert.equal(doc.querySelector('.node[data-id="c"] .node-count').textContent, '—');
  assert.ok(doc.querySelector('.node[data-id="c"]').classList.contains('empty'));
  // Same four boxes and four edges — the list is a lens, not a different graph.
  assert.equal(doc.querySelectorAll('.node').length, 4);
  assert.equal(doc.querySelectorAll('#edges path').length, 4);
});

// The lock is a property of the curated teaching order. An imported list has no
// order, so every topic must read as available rather than blocked.
test('switching to a non-curated list clears the locks', () => {
  const doc = renderPage();
  assert.ok(doc.querySelector('.node[data-id="d"]').classList.contains('locked'));
  switchTo(doc, 'tagged');
  assert.equal(doc.querySelectorAll('.node.locked').length, 0);
});

test('a tick made on one list still counts on another', () => {
  const doc = renderPage();
  switchTo(doc, 'tagged');
  click(doc.querySelector('.node[data-id="a"]'));
  const box = doc.getElementById('drawerBody').querySelector('input[data-check="1"]');
  box.checked = true;
  box.dispatchEvent(new global.window.Event('change', { bubbles: true }));

  assert.equal(doc.getElementById('statProblems').textContent, '1 / 3');
  switchTo(doc, 'roadmap');
  assert.equal(doc.getElementById('statProblems').textContent, '1 / 4');
  assert.equal(doc.querySelector('.node[data-id="d"] .node-count').textContent, '1/1');
});

test('"tick all" ticks the current list, not the curated one', () => {
  const doc = renderPage();
  switchTo(doc, 'tagged');
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-bulk="all"]'));
  // A holds P1 + P9 on `tagged`; P2 belongs to the roadmap list and must be untouched.
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()).sort(), ['1', '9']);
});

// Each JSDOM gets its own localStorage, so the round trip is checked in two
// halves rather than by re-rendering: choosing writes the key, and a render
// that finds the key opens that list.
test('choosing a list stores it', () => {
  const doc = renderPage();
  switchTo(doc, 'tagged');
  assert.equal(localStorage.getItem(CSRoadmap.LIST_KEY), 'tagged');
});

test('a stored list is what opens on load, not the default', () => {
  const doc = renderPage(null, null, { [CSRoadmap.LIST_KEY]: 'tagged' });
  assert.equal(doc.getElementById('listSelect').value, 'tagged');
  assert.equal(doc.getElementById('statProblems').textContent, '0 / 3');
  assert.equal(doc.getElementById('listBlurb').textContent, 'a tag list');
});

// A list can be renamed or dropped from data/roadmap.json between visits, and a
// stored id that no longer exists must not blank the page.
test('a stored list that no longer exists falls back to the default', () => {
  const doc = renderPage(null, null, { [CSRoadmap.LIST_KEY]: 'deleted-list' });
  assert.equal(doc.getElementById('listSelect').value, 'roadmap');
  assert.equal(doc.querySelectorAll('.node').length, 4);
});

// ── The page's markup contract ────────────────────────────────────────────

// render() reaches for these by id. A rename in the HTML that misses roadmap.js
// would throw at load, and the page would show a permanent "Loading…".
test('lc-roadmap.html carries every element roadmap.js looks up', () => {
  const doc = new JSDOM(fs.readFileSync(PAGE, 'utf8')).window.document;
  for (const id of ['pageTitle', 'pageIntro', 'summary', 'statProblems', 'statTopics',
                    'summaryFill', 'summaryLabel', 'listSelect', 'listBlurb', 'resetBtn',
                    'nextUp', 'graph', 'edges', 'loading', 'note', 'overlay', 'drawer',
                    'drawerClose', 'drawerTitle', 'drawerBlurb', 'drawerBody']) {
    assert.ok(doc.getElementById(id), `#${id} is missing from lc-roadmap.html`);
  }
});

// validate-pages.yml runs its HTML-structure check over every entry in its
// requiredFiles list, and that check demands a footer. This page is on the
// list, so a missing footer fails CI rather than merely looking odd.
test('lc-roadmap.html carries the site footer CI requires', () => {
  const html = fs.readFileSync(PAGE, 'utf8');
  assert.ok(html.includes('<footer>'), 'validate-pages.yml greps for a literal <footer>');
  const doc = new JSDOM(html).window.document;
  const links = [...doc.querySelectorAll('footer a')].map((a) => a.getAttribute('href'));
  assert.deepEqual(links, [
    'https://github.com/yennanliu/CS_basics',
    'https://github.com/yennanliu/CS_basics/tree/master/doc',
    'https://github.com/yennanliu/CS_basics/issues'
  ]);
});

test('lc-roadmap.html loads roadmap.js and tells the navbar which page it is', () => {
  const html = fs.readFileSync(PAGE, 'utf8');
  assert.match(html, /<script src="roadmap\.js"><\/script>/);
  assert.match(html, /data-page="lc-roadmap"/);
  const CSNav = require('../nav.js');
  assert.ok(
    CSNav.PRIMARY.concat(CSNav.MORE).some((item) => item.id === 'lc-roadmap'),
    'the navbar has no lc-roadmap entry, so the page would be unreachable'
  );
});
