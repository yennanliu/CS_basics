const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { JSDOM } = require('jsdom');

const CSProblems = require('../problems-filter.js');

// The filter on problems.html — the page that carries the whole README because
// nothing else can (GitHub stops rendering markdown at 512 KB; README passed
// 1.1 MB).
//
// e2e-check.js runs this same script against the *built* page and its real
// 3,340 rows, which is the test that matters for "does it still find things".
// What it cannot do is state the rules: which column is read for what, why a
// bare number is matched differently from a word, and which heading survives a
// query that empties its section. Those are pinned here, against a document
// small enough to read.

const SCRIPT = fs.readFileSync(path.join(__dirname, '..', 'problems-filter.js'), 'utf8');

// The four table shapes README actually renders, in miniature. 57 of its 60
// tables are the 8-column one; the odd three are why columns are read by name.
const PAGE = `
<div class="content">
  <div class="pf" id="problem-filter" data-pf-chrome hidden>
    <input type="search" id="q">
    <span id="pf-count"></span>
    <button class="pf-chip" data-facet="difficulty" data-value="hard" aria-pressed="false">Hard</button>
    <button class="pf-chip" data-facet="status" data-value="again" aria-pressed="false">AGAIN</button>
    <button class="pf-chip" data-facet="must" aria-pressed="false">MUST</button>
    <button class="pf-clear" id="pf-clear" hidden>clear</button>
  </div>
  <p class="pf-empty" id="pf-empty" data-pf-chrome hidden>No rows match.</p>

  <h1>CS_BASICS</h1>
  <p>Intro prose that is not a problem row.</p>

  <h2>Sliding Window</h2>
  <div class="table-wrap"><table>
    <thead><tr><th>#</th><th>Title</th><th>Solution</th><th>Time</th><th>Space</th><th>Difficulty</th><th>Note</th><th>Status</th></tr></thead>
    <tbody>
      <tr><td>0239</td><td>Sliding Window Maximum</td><td>Py</td><td>O(n)</td><td>O(k)</td><td>Hard</td><td>deque, MUST, google</td><td>AGAIN*** (2)</td></tr>
      <tr><td>0003</td><td>Longest Substring Without Repeating</td><td>Py</td><td>O(n)</td><td>O(n)</td><td>Medium</td><td>hash, blind75</td><td>OK* (4)</td></tr>
    </tbody>
  </table></div>
  <pre>a code fence between the tables</pre>

  <h2>Newly Added</h2>
  <h3>Heap</h3>
  <div class="table-wrap"><table>
    <thead><tr><th>#</th><th>Title</th><th>Solution</th><th>Time</th><th>Space</th><th>Difficulty</th><th>Note</th><th>Status</th></tr></thead>
    <tbody>
      <tr><td>0023</td><td>Merge k Sorted Lists</td><td>Java</td><td>O(n log k)</td><td>O(k)</td><td>Hard</td><td>heap</td><td></td></tr>
      <tr><td>1046</td><td>Last Stone Weight</td><td>Py</td><td>O(n log n)</td><td>O(n)</td><td>Easy</td><td>heap</td><td>AGAIN (not start)</td></tr>
    </tbody>
  </table></div>

  <h2>Data Structure</h2>
  <div class="table-wrap"><table>
    <thead><tr><th>#</th><th>Title</th><th>Solution</th><th>Use case</th><th>Comment</th><th>Status</th></tr></thead>
    <tbody>
      <tr><td></td><td>Trie</td><td>Py</td><td>prefix search</td><td>MUST</td><td>AGAIN*</td></tr>
    </tbody>
  </table></div>
</div>`;

function dom(html = PAGE, url = 'https://example.test/problems.html') {
  return new JSDOM(`<!DOCTYPE html><html><body>${html}</body></html>`, { url });
}

const scope = d => d.window.document.querySelector('.content');
const rowsOf = sections => sections.reduce((all, s) => all.concat(s.rows), []);

// ── Reading the tables ───────────────────────────────────────────────────────

test('columns are read by header name, not by position', () => {
  const d = dom();
  const tables = d.window.document.querySelectorAll('table');
  assert.deepEqual(CSProblems.columnMap(tables[0]),
    { num: 0, difficulty: 5, status: 7, note: 6 });
  // The 6-column "Data Structure" table has no Difficulty and keeps its notes
  // under "Comment". A fixed index would read "Use case" as the difficulty.
  assert.deepEqual(CSProblems.columnMap(tables[2]),
    { num: 0, difficulty: -1, status: 5, note: 4 });
});

test('a row carries its number, difficulty and status', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const lc239 = rows.find(r => r.num === '239');
  assert.equal(lc239.difficulty, 'hard');
  assert.equal(lc239.status, 'again');
  // README zero-pads; the padding is stripped so a query can match exactly.
  assert.equal(rows.find(r => r.text.includes('merge k sorted')).num, '23');
});

test('status is the word, not the star run or the parenthetical', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const by = title => rows.find(r => r.text.includes(title)).status;
  assert.equal(by('sliding window maximum'), 'again');   // AGAIN*** (2)
  assert.equal(by('longest substring'), 'ok');           // OK* (4)
  assert.equal(by('last stone'), 'again');               // AGAIN (not start)
  assert.equal(by('merge k sorted'), 'todo');            // empty cell
});

test('MUST counts from either the Note or the Status column', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  // LC 239 carries it in Note; the Trie row carries it in Comment.
  assert.equal(rows.find(r => r.num === '239').must, true);
  assert.equal(rows.find(r => r.text.includes('trie')).must, true);
  assert.equal(rows.find(r => r.num === '3').must, false);
});

test('a row answers to its whole heading trail', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const heap = rows.find(r => r.num === '1046');
  // "### Heap" under "## Newly Added" — both words reach the row, so a topic
  // query returns the section rather than only the titles containing the word.
  assert.match(heap.text, /newly added/);
  assert.match(heap.text, /heap/);
});

test('the filter bar is not part of the document it filters', () => {
  const sections = CSProblems.indexContent(scope(dom()));
  const all = sections.flatMap(s => s.blocks);
  assert.ok(!all.some(b => b.id === 'problem-filter'),
    'the bar would hide itself on the first keystroke');
  assert.ok(!all.some(b => b.id === 'pf-empty'));
});

// ── Matching ─────────────────────────────────────────────────────────────────

function state(over = {}) {
  return Object.assign(CSProblems.emptyState(), over);
}

test('a bare number matches that problem number exactly', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const hits = rows.filter(r => CSProblems.matchRow(r, state({ terms: ['23'] })));
  assert.equal(hits.length, 1);
  assert.equal(hits[0].num, '23');
  // Not LC 239, whose row text contains "23" twice over — that is the Ctrl-F
  // behaviour this box exists to replace.
  assert.ok(!hits.some(r => r.num === '239'));
});

test('a word matches anywhere in the row', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const hit = r => CSProblems.matchRow(r, state({ terms: ['deque'] }));
  assert.equal(rows.filter(hit).length, 1);
  assert.equal(rows.filter(r => CSProblems.matchRow(r, state({ terms: ['py'] }))).length, 4);
});

test('multiple words all have to match', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const both = state({ terms: ['heap', 'easy'] });
  assert.deepEqual(rows.filter(r => CSProblems.matchRow(r, both)).map(r => r.num), ['1046']);
});

test('facets are OR within a group and AND across groups', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  const either = state({ difficulty: ['hard', 'easy'] });
  assert.deepEqual(rows.filter(r => CSProblems.matchRow(r, either)).map(r => r.num).sort(),
    ['1046', '23', '239']);
  const both = state({ difficulty: ['hard'], status: ['again'] });
  assert.deepEqual(rows.filter(r => CSProblems.matchRow(r, both)).map(r => r.num), ['239']);
});

test('MUST is its own toggle', () => {
  const rows = rowsOf(CSProblems.indexContent(scope(dom())));
  assert.equal(rows.filter(r => CSProblems.matchRow(r, state({ must: true }))).length, 2);
});

// ── Applying to the page ─────────────────────────────────────────────────────

const shown = d => Array.from(d.window.document.querySelectorAll('.content tbody tr'))
  .filter(tr => !tr.classList.contains('pf-off'));
const hidden = (d, sel) => Array.from(d.window.document.querySelectorAll(sel))
  .filter(el => el.classList.contains('pf-off'));

test('an inactive filter leaves the document alone', () => {
  const d = dom();
  const sections = CSProblems.indexContent(scope(d));
  const result = CSProblems.apply(sections, state());
  assert.equal(result.active, false);
  assert.equal(result.shown, 5);
  assert.equal(shown(d).length, 5);
  assert.equal(hidden(d, '.content *').length, 0, 'nothing is hidden when nothing is filtered');
});

test('a query hides the rows, the headings and the prose it empties', () => {
  const d = dom();
  const sections = CSProblems.indexContent(scope(d));
  CSProblems.apply(sections, state({ terms: ['deque'] }));

  assert.deepEqual(shown(d).map(tr => tr.children[0].textContent), ['0239']);
  const doc = d.window.document;
  const headings = Array.from(doc.querySelectorAll('h2'));
  assert.equal(headings.find(h => h.textContent === 'Sliding Window').classList.contains('pf-off'), false);
  assert.equal(headings.find(h => h.textContent === 'Data Structure').classList.contains('pf-off'), true);
  // The intro paragraph and the code fence are context for reading the index
  // top to bottom, not answers to a query.
  assert.equal(doc.querySelector('.content > p:not([data-pf-chrome])').classList.contains('pf-off'), true);
  assert.equal(doc.querySelector('pre').classList.contains('pf-off'), true);
});

test('a parent heading survives on its children', () => {
  const d = dom();
  const sections = CSProblems.indexContent(scope(d));
  CSProblems.apply(sections, state({ terms: ['stone'] }));
  const doc = d.window.document;
  // "## Newly Added" owns no rows itself. Hiding it would file its sub-tables
  // under nothing and make them read as part of the main index.
  assert.equal(doc.querySelector('h2:nth-of-type(2)').textContent, 'Newly Added');
  assert.equal(hidden(d, 'h2').map(h => h.textContent).includes('Newly Added'), false);
  assert.equal(hidden(d, 'h3').length, 0, 'the section that did match stays');
});

test('clearing the query restores every element it hid', () => {
  const d = dom();
  const sections = CSProblems.indexContent(scope(d));
  CSProblems.apply(sections, state({ terms: ['deque'] }));
  assert.ok(hidden(d, '.content *').length > 0);
  CSProblems.apply(sections, state());
  assert.equal(hidden(d, '.content *').length, 0);
});

test('the stripe follows the rows still showing', () => {
  const d = dom();
  const sections = CSProblems.indexContent(scope(d));
  const alt = () => shown(d).map(tr => tr.classList.contains('pf-alt'));

  // Unfiltered, the class reproduces nth-child(even) — per table, so the
  // stripe restarts with each <tbody> rather than running across the page.
  CSProblems.apply(sections, state());
  assert.deepEqual(alt(), [false, true, false, true, false]);

  // Hiding the first row of a table must promote the second, or the table
  // renders one stripe and no alternation.
  CSProblems.apply(sections, state({ status: ['ok', 'todo'] }));
  assert.deepEqual(shown(d).map(tr => tr.children[0].textContent), ['0003', '0023']);
  assert.deepEqual(alt(), [false, false]);
});

// ── URL round-trip ───────────────────────────────────────────────────────────

test('a filtered view survives in the url', () => {
  assert.equal(CSProblems.writeQuery(
    state({ difficulty: ['hard'], status: ['again'], must: true }), 'graph'),
    '?q=graph&difficulty=hard&status=again&must=1');
  const back = CSProblems.readQuery('?q=graph&difficulty=hard&status=again&must=1');
  assert.deepEqual(back, {
    query: 'graph', terms: ['graph'], difficulty: ['hard'], status: ['again'], must: true
  });
  assert.equal(CSProblems.writeQuery(state(), ''), '', 'an empty filter leaves the url clean');
});

// ── Booting ──────────────────────────────────────────────────────────────────

test('boot reveals the bar and wires the box', () => {
  const d = dom();
  const doc = d.window.document;
  CSProblems.boot(doc);

  assert.equal(doc.getElementById('problem-filter').hidden, false);
  assert.equal(doc.getElementById('pf-count').textContent, '5 rows');
  // Set only once the script is live, so the CSS stripe stands without it.
  assert.ok(doc.body.classList.contains('pf-page'));

  const input = doc.getElementById('q');
  input.value = 'heap';
  input.dispatchEvent(new d.window.Event('input', { bubbles: true }));
  assert.equal(shown(d).length, 2);
  assert.equal(doc.getElementById('pf-count').textContent, '2 of 5 rows');
  assert.equal(doc.getElementById('pf-clear').hidden, false);
});

test('a chip toggles, and says so to a screen reader', () => {
  const d = dom();
  const doc = d.window.document;
  CSProblems.boot(doc);
  const chip = doc.querySelector('[data-facet="difficulty"]');
  const click = () => chip.dispatchEvent(new d.window.MouseEvent('click', { bubbles: true }));

  click();
  assert.equal(chip.getAttribute('aria-pressed'), 'true');
  assert.deepEqual(shown(d).map(tr => tr.children[0].textContent), ['0239', '0023']);
  click();
  assert.equal(chip.getAttribute('aria-pressed'), 'false');
  assert.equal(shown(d).length, 5);
});

test('no match says so rather than showing a blank page', () => {
  const d = dom();
  const doc = d.window.document;
  CSProblems.boot(doc);
  const input = doc.getElementById('q');
  input.value = 'zzzqqq';
  input.dispatchEvent(new d.window.Event('input', { bubbles: true }));
  assert.equal(shown(d).length, 0);
  assert.equal(doc.getElementById('pf-empty').hidden, false);
});

test('clear resets the box, the chips and the page', () => {
  const d = dom();
  const doc = d.window.document;
  CSProblems.boot(doc);
  const input = doc.getElementById('q');
  input.value = 'heap';
  input.dispatchEvent(new d.window.Event('input', { bubbles: true }));
  doc.querySelector('[data-facet="must"]').dispatchEvent(new d.window.MouseEvent('click', { bubbles: true }));

  doc.getElementById('pf-clear').dispatchEvent(new d.window.MouseEvent('click', { bubbles: true }));
  assert.equal(input.value, '');
  assert.equal(shown(d).length, 5);
  assert.equal(doc.getElementById('pf-clear').hidden, true);
  assert.ok(Array.from(doc.querySelectorAll('[data-facet]'))
    .every(c => c.getAttribute('aria-pressed') === 'false'));
});

test('a url with a filter in it arrives filtered', () => {
  const d = dom(PAGE, 'https://example.test/problems.html?q=heap&difficulty=hard');
  const doc = d.window.document;
  CSProblems.boot(doc);
  assert.equal(doc.getElementById('q').value, 'heap');
  assert.deepEqual(shown(d).map(tr => tr.children[0].textContent), ['0023']);
  assert.equal(doc.querySelector('[data-facet="difficulty"]').getAttribute('aria-pressed'), 'true');
});

test('escape empties the box before it gives up focus', () => {
  const d = dom();
  const doc = d.window.document;
  CSProblems.boot(doc);
  const input = doc.getElementById('q');
  input.value = 'heap';
  input.dispatchEvent(new d.window.Event('input', { bubbles: true }));
  input.dispatchEvent(new d.window.KeyboardEvent('keydown', { key: 'Escape', bubbles: true }));
  assert.equal(input.value, '');
  assert.equal(shown(d).length, 5);
});

test('the page with no filter bar is left alone', () => {
  const d = dom('<div class="content"><h2>Doc</h2><p>Body</p></div>');
  assert.equal(CSProblems.boot(d.window.document), null);
});

// The deferred script boots itself; e2e-check.js calls boot() directly because
// jsdom has not fired DOMContentLoaded by the time it runs, so this is the one
// place the self-start is actually exercised.
test('the script starts itself on DOMContentLoaded', async () => {
  const d = new JSDOM(
    `<!DOCTYPE html><html><head><script>${SCRIPT}</script></head><body>${PAGE}</body></html>`,
    { url: 'https://example.test/problems.html', runScripts: 'dangerously' }
  );
  await new Promise(resolve => {
    if (d.window.document.readyState === 'loading') {
      d.window.addEventListener('DOMContentLoaded', resolve);
    } else { resolve(); }
  });
  assert.equal(d.window.document.getElementById('problem-filter').hidden, false);
  assert.equal(d.window.document.getElementById('pf-count').textContent, '5 rows');
});
