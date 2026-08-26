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

function problem(id, extra) {
  return Object.assign({
    id: String(id), title: 'P' + id, url: 'https://leetcode.com/problems/p' + id + '/',
    difficulty: 'Easy', solutions: { Java: 'https://example.test/' + id + '.java' }
  }, extra);
}

// a → b, a → c, (b, c) → d. `d` also lists P1, which `a` owns: the shared
// problem is what the distinct-counting and cross-topic rules hang on.
function fixture() {
  return {
    meta: { title: 'Study Roadmap', intro: 'intro text' },
    stats: { topics: 4, problems: 4, problemSlots: 5, rows: 3 },
    nodes: [
      { id: 'a', title: 'A', blurb: 'root', row: 0, col: 0, rowSize: 1, prereqs: [],
        sheets: [{ slug: 'array', title: 'Array', url: 'cheatsheets/array.html' }],
        problems: [problem(1), problem(2, { difficulty: 'Hard' })] },
      { id: 'b', title: 'B', blurb: '', row: 1, col: 0, rowSize: 2, prereqs: ['a'],
        sheets: [], problems: [problem(3)] },
      { id: 'c', title: 'C', blurb: '', row: 1, col: 1, rowSize: 2, prereqs: ['a'],
        sheets: [], problems: [problem(4, { solutions: {} })] },
      { id: 'd', title: 'D', blurb: '', row: 2, col: 0, rowSize: 1, prereqs: ['b', 'c'],
        sheets: [], problems: [problem(1)] }
    ]
  };
}

function solvedSet(ids) {
  const set = Object.create(null);
  ids.forEach((id) => { set[String(id)] = true; });
  return set;
}

// ── Counting ──────────────────────────────────────────────────────────────

test('statsFor counts only the problems the topic itself lists', () => {
  const roadmap = fixture();
  assert.deepEqual(CSRoadmap.statsFor(roadmap.nodes[0], solvedSet([1])), { done: 1, total: 2 });
  assert.deepEqual(CSRoadmap.statsFor(roadmap.nodes[1], solvedSet([1])), { done: 0, total: 1 });
});

test('isDone needs every problem, and is false for an empty topic', () => {
  const roadmap = fixture();
  assert.equal(CSRoadmap.isDone(roadmap.nodes[0], solvedSet([1])), false);
  assert.equal(CSRoadmap.isDone(roadmap.nodes[0], solvedSet([1, 2])), true);
  assert.equal(CSRoadmap.isDone({ problems: [] }, solvedSet([])), false);
});

// The headline figure would otherwise read 5 when there are only 4 problems.
test('distinctSolved counts a problem shared by two topics once', () => {
  const roadmap = fixture();
  assert.equal(CSRoadmap.distinctSolved(roadmap.nodes, solvedSet([1])), 1);
  assert.equal(CSRoadmap.distinctSolved(roadmap.nodes, solvedSet([1, 2, 3, 4])), 4);
});

test('distinctSolved ignores ticks for problems no topic lists', () => {
  assert.equal(CSRoadmap.distinctSolved(fixture().nodes, solvedSet([999])), 0);
});

// ── Unlocking ─────────────────────────────────────────────────────────────

test('isUnlocked opens a topic only once every prereq is finished', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const b = byId.b, d = byId.d;

  assert.equal(CSRoadmap.isUnlocked(byId.a, byId, solvedSet([])), true, 'the root is always open');
  assert.equal(CSRoadmap.isUnlocked(b, byId, solvedSet([1])), false, 'A is only half done');
  assert.equal(CSRoadmap.isUnlocked(b, byId, solvedSet([1, 2])), true);
  // D needs BOTH B and C, so finishing one branch is not enough.
  assert.equal(CSRoadmap.isUnlocked(d, byId, solvedSet([1, 2, 3])), false);
  assert.equal(CSRoadmap.isUnlocked(d, byId, solvedSet([1, 2, 3, 4])), true);
});

test('unmetPrereqs names the topics still in the way, in authored order', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.d, byId, solvedSet([1, 2, 3])), ['c']);
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.d, byId, solvedSet([1, 2])), ['b', 'c']);
  assert.deepEqual(CSRoadmap.unmetPrereqs(byId.a, byId, solvedSet([])), []);
});

// build-roadmap.js fails the build on an unknown prereq, so if one ever reaches
// the page it is better to leave the branch reachable than to strand it.
test('an unknown prereq id does not lock a topic forever', () => {
  const node = { id: 'x', title: 'X', prereqs: ['ghost'], problems: [problem(9)] };
  assert.equal(CSRoadmap.isUnlocked(node, { x: node }, solvedSet([])), true);
});

// ── nextUp ────────────────────────────────────────────────────────────────

test('nextUp starts at the root and names its first unsolved problem', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const next = CSRoadmap.nextUp(roadmap.nodes, byId, solvedSet([1]));
  assert.equal(next.node.id, 'a');
  assert.equal(next.problem.id, '2');
});

test('nextUp prefers the shallowest open topic and skips locked ones', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  // A done, B and C open at row 1: the left-most (authored first) wins. D is
  // still locked, so it must not be suggested even though it is unfinished.
  const next = CSRoadmap.nextUp(roadmap.nodes, byId, solvedSet([1, 2]));
  assert.equal(next.node.id, 'b');
});

test('nextUp returns null when everything is solved', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  assert.equal(CSRoadmap.nextUp(roadmap.nodes, byId, solvedSet([1, 2, 3, 4])), null);
});

// ── Markup ────────────────────────────────────────────────────────────────

function parse(html) {
  return new JSDOM(`<!DOCTYPE html><html><body>${html}</body></html>`).window.document;
}

test('graphHTML emits one .row per row, in ascending order', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  const doc = parse(CSRoadmap.graphHTML(roadmap.nodes, byId, solvedSet([])));
  const rows = [...doc.querySelectorAll('.row')];
  assert.deepEqual(rows.map((r) => r.getAttribute('data-row')), ['0', '1', '2']);
  assert.equal(rows[1].querySelectorAll('.node').length, 2);
});

test('nodeHTML marks a finished topic done and an unmet one locked', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);

  const fresh = parse(CSRoadmap.nodeHTML(byId.b, byId, solvedSet([]))).querySelector('.node');
  assert.ok(fresh.classList.contains('locked'));
  assert.ok(!fresh.classList.contains('done'));
  assert.equal(fresh.getAttribute('title'), 'B — 0 of 1 solved. Finish first: A');

  const open = parse(CSRoadmap.nodeHTML(byId.b, byId, solvedSet([1, 2, 3]))).querySelector('.node');
  assert.ok(open.classList.contains('done'));
  assert.ok(!open.classList.contains('locked'));
  assert.equal(open.getAttribute('title'), 'B — 1 of 1 solved');
  assert.equal(open.querySelector('.node-count').textContent, '1/1');
});

// The label used to be printed on every box; 29 repetitions of "after Arrays &
// Hashing" buried the graph, so it moved into the tooltip and the drawer.
test('lockLabel names the unmet prereqs and is empty once they are met', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);
  assert.equal(CSRoadmap.lockLabel(byId.d, byId, solvedSet([1, 2])), 'Finish first: B, C');
  assert.equal(CSRoadmap.lockLabel(byId.d, byId, solvedSet([1, 2, 3, 4])), '');
  assert.equal(CSRoadmap.lockLabel(byId.a, byId, solvedSet([])), '');
});

test('problemHTML links every language the repo has, and nothing when it has none', () => {
  const withJava = parse(CSRoadmap.problemHTML(
    problem(1, { solutions: { Java: 'https://x.test/a.java', Python: 'https://x.test/a.py' } }),
    solvedSet([])
  ));
  assert.deepEqual([...withJava.querySelectorAll('.prob-links a')].map((a) => a.textContent), ['Ja', 'Py']);

  const bare = parse(CSRoadmap.problemHTML(problem(1, { solutions: {} }), solvedSet([])));
  assert.equal(bare.querySelectorAll('.prob-links a').length, 0);
});

test('problemHTML reflects the solved tick in both the class and the checkbox', () => {
  const doc = parse(CSRoadmap.problemHTML(problem(7), solvedSet([7])));
  assert.ok(doc.querySelector('.prob').classList.contains('solved'));
  assert.equal(doc.querySelector('input').checked, true);
});

test('problemHTML escapes a title that would otherwise inject markup', () => {
  const doc = parse(CSRoadmap.problemHTML(
    problem(1, { title: '<img src=x onerror=alert(1)>' }), solvedSet([])
  ));
  assert.equal(doc.querySelectorAll('img').length, 0);
  assert.equal(doc.querySelector('.prob-title').textContent, '<img src=x onerror=alert(1)>');
});

test('drawerBodyHTML ticks the prereqs that are met and links the cheatsheets', () => {
  const roadmap = fixture();
  const byId = CSRoadmap.indexNodes(roadmap.nodes);

  const rootDoc = parse(CSRoadmap.drawerBodyHTML(byId.a, byId, solvedSet([])));
  assert.equal(rootDoc.querySelectorAll('.chip.met').length, 0, 'the root has no prereq section');
  assert.equal(rootDoc.querySelector('a.chip').getAttribute('href'), 'cheatsheets/array.html');
  assert.equal(rootDoc.querySelectorAll('.prob').length, 2);

  const dDoc = parse(CSRoadmap.drawerBodyHTML(byId.d, byId, solvedSet([1, 2, 3])));
  const chips = [...dDoc.querySelectorAll('[data-open]')];
  assert.deepEqual(chips.map((c) => c.getAttribute('data-open')), ['b', 'c']);
  assert.deepEqual(chips.map((c) => c.classList.contains('met')), [true, false]);
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
function renderPage(roadmap) {
  const html = fs.readFileSync(PAGE, 'utf8');
  const dom = new JSDOM(html, { url: 'https://example.test/lc-roadmap.html' });
  for (const key of ['window', 'document', 'localStorage', 'CustomEvent', 'Event', 'navigator', 'self', 'history', 'location']) {
    global[key] = key === 'self' ? dom.window : dom.window[key];
  }
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

test('clicking a topic opens the drawer with that topic loaded', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="a"]'));
  assert.ok(doc.getElementById('drawer').classList.contains('open'));
  assert.equal(doc.getElementById('drawer').getAttribute('aria-hidden'), 'false');
  assert.equal(doc.getElementById('drawerTitle').textContent, 'A');
  assert.equal(doc.getElementById('drawerBody').querySelectorAll('.prob').length, 2);
  assert.equal(global.location.hash, '#a', 'the open topic is shareable via the URL');
});

test('the overlay closes the drawer and clears the hash', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('overlay'));
  assert.ok(!doc.getElementById('drawer').classList.contains('open'));
  assert.equal(global.location.hash, '');
});

// The whole point of keying progress by problem id: A and D share LC 1.
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
  assert.equal(doc.querySelector('.node[data-id="b"]').getAttribute('title'), 'B — 0 of 1 solved');
  // D still needs B and C, so it stays locked.
  assert.ok(doc.querySelector('.node[data-id="d"]').classList.contains('locked'));

  click(doc.getElementById('drawerBody').querySelector('[data-bulk="none"]'));
  assert.ok(!doc.querySelector('.node[data-id="a"]').classList.contains('done'));
  assert.ok(doc.querySelector('.node[data-id="b"]').classList.contains('locked'));
  assert.match(doc.querySelector('.node[data-id="b"]').getAttribute('title'), /Finish first: A$/);
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

test('an edge out of a finished topic is drawn as live', () => {
  const doc = renderPage();
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-bulk="all"]'));
  const live = [...doc.querySelectorAll('#edges path')]
    .filter((p) => p.classList.contains('live'))
    .map((p) => p.getAttribute('data-to'));
  assert.deepEqual(live, ['b', 'c']);
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
  global.window.confirm = () => true;
  click(doc.querySelector('.node[data-id="a"]'));
  click(doc.getElementById('drawerBody').querySelector('[data-bulk="all"]'));
  assert.equal(doc.getElementById('statProblems').textContent, '2 / 4');

  click(doc.getElementById('resetBtn'));
  assert.equal(doc.getElementById('statProblems').textContent, '0 / 4');
  assert.deepEqual(Object.keys(CSRoadmap.readSolved()), []);
});

test('a topic named in the URL hash opens on load', () => {
  const html = fs.readFileSync(PAGE, 'utf8');
  const dom = new JSDOM(html, { url: 'https://example.test/lc-roadmap.html#c' });
  for (const key of ['window', 'document', 'localStorage', 'CustomEvent', 'Event', 'navigator', 'self', 'history', 'location']) {
    global[key] = key === 'self' ? dom.window : dom.window[key];
  }
  require('../nav.js').mount();
  CSRoadmap.render(fixture());
  assert.equal(dom.window.document.getElementById('drawerTitle').textContent, 'C');
});

// ── The page's markup contract ────────────────────────────────────────────

// render() reaches for these by id. A rename in the HTML that misses roadmap.js
// would throw at load, and the page would show a permanent "Loading…".
test('lc-roadmap.html carries every element roadmap.js looks up', () => {
  const doc = new JSDOM(fs.readFileSync(PAGE, 'utf8')).window.document;
  for (const id of ['pageTitle', 'pageIntro', 'summary', 'statProblems', 'statTopics',
                    'summaryFill', 'summaryLabel', 'resetBtn', 'nextUp', 'graph', 'edges',
                    'loading', 'note', 'overlay', 'drawer', 'drawerClose', 'drawerTitle',
                    'drawerBlurb', 'drawerBody']) {
    assert.ok(doc.getElementById(id), `#${id} is missing from lc-roadmap.html`);
  }
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
