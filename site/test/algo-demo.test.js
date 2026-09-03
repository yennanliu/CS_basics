const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { JSDOM } = require('jsdom');

// algo_demo/common.js is a plain browser script with no module wrapper, so it
// is tested the way e2e-check.js tests search's score(): the shipped file is
// evaluated as-is, and the assertions run against what the pages actually load.
// Nothing here reimplements the logger.
const COMMON = fs.readFileSync(
  path.join(__dirname, '..', '..', 'algo_demo', 'common.js'), 'utf8');
const STYLE = fs.readFileSync(
  path.join(__dirname, '..', '..', 'algo_demo', 'style.css'), 'utf8');

// The markup every page carries around its log, so the panel chrome the logger
// drives (the counter, the empty state) is exercised too.
const PANEL = `
  <section class="viz-trace">
    <div class="viz-trace-head">
      <h3>Steps</h3>
      <span class="viz-trace-count" data-trace-count>0 steps</span>
    </div>
    <div class="viz-trace-body">
      <div class="viz-log" id="log"></div>
      <p class="viz-trace-empty" data-trace-empty><span>Press Run</span></p>
    </div>
  </section>`;

function boot() {
  const dom = new JSDOM(`<!DOCTYPE html><html><body>${PANEL}</body></html>`,
                        { url: 'https://example.test/', runScripts: 'dangerously' });
  dom.window.eval(COMMON);
  return dom;
}

const rows = (dom) => [...dom.window.document.querySelectorAll('#log > .step')];
const text = (el) => el.textContent.replace(/\s+/g, ' ').trim();
const body = (el) => text(el.querySelector('.step-main') || el);

// ── Step trace: the three shapes the pages write ──────────────────────────

test('a plain message becomes a numbered step', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('L=4 R=24 sum=28');
  log.log('L=4 R=22 sum=26');

  const r = rows(dom);
  assert.equal(r.length, 2);
  assert.ok(r[0].classList.contains('step-item'));
  assert.deepEqual(r.map((s) => s.querySelector('.step-n').textContent), ['1', '2']);
});

test('an indented message is the reason for the step above, not a step of its own', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('L=4 R=24 sum=28');
  log.log('&nbsp;&nbsp;sum &gt; target, move R left');

  const r = rows(dom);
  assert.equal(r.length, 1, 'the note must not open a second row');
  const note = r[0].querySelector('.step-note');
  assert.ok(note);
  assert.equal(text(note), 'sum > target, move R left');
  // ...and it must not consume a step number.
  assert.equal(r[0].querySelector('.step-n').textContent, '1');
});

test('an indented message with nothing above it still gets a row', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('&nbsp;&nbsp;orphaned detail');

  const r = rows(dom);
  assert.equal(r.length, 1);
  assert.equal(body(r[0]), 'orphaned detail');
});

test('--- x --- is a phase heading and is not numbered', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('--- Iteration 2 ---');
  log.log('relax 1->2');

  const r = rows(dom);
  assert.ok(r[0].classList.contains('step-phase'));
  assert.equal(body(r[0]), 'Iteration 2');
  assert.equal(r[1].querySelector('.step-n').textContent, '1',
               'a phase heading must not spend a step number');
});

test('a note after a phase heading starts a step rather than attaching to nothing', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('first');
  log.log('--- Iteration 2 ---');
  log.log('&nbsp;&nbsp;detail');

  const r = rows(dom);
  assert.equal(r.length, 3);
  assert.ok(r[2].classList.contains('step-item'));
  assert.equal(r[0].querySelectorAll('.step-note').length, 0,
               'the note belongs to the new phase, not to the step before it');
});

test('an empty message is dropped', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('');
  log.log('   ');
  log.log(null);
  assert.equal(rows(dom).length, 0);
});

// ── Outcomes vs. inline emphasis ──────────────────────────────────────────

test('a message that is entirely one highlight span is the outcome', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('<span class="highlight">Found! 4 + 24 = 28</span>');
  assert.ok(rows(dom)[0].classList.contains('is-key'));
});

test('a highlight span used inline is emphasis, not an outcome', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  // Dijkstra writes every processed node this way; marking them all "key"
  // would flag nine rows in ten.
  log.log('Process node <span class="highlight">1</span> dist=7');

  const row = rows(dom)[0];
  assert.ok(!row.classList.contains('is-key'));
  assert.ok(row.querySelector('.highlight'), 'the inline emphasis still renders');
});

test('an indented outcome marks the note, not a new step', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('slide to [2..5]');
  log.log('&nbsp;&nbsp;<span class="highlight">New max=31</span>');

  const note = rows(dom)[0].querySelector('.step-note');
  assert.ok(note.classList.contains('is-key'));
});

// ── Decoration ────────────────────────────────────────────────────────────

test('name=value is split so the changing value can be picked out', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('dp[2][3] = 10');

  const row = rows(dom)[0];
  assert.equal(row.querySelector('.k').textContent, 'dp[2][3]');
  assert.equal(row.querySelector('.v').textContent, '10');
});

test('decoration never disturbs markup the page wrote', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('get(<span class="highlight">7</span>) = 42');

  const row = rows(dom)[0];
  assert.equal(row.querySelectorAll('.highlight').length, 1);
  assert.equal(body(row), 'get(7) = 42');
});

test('an angle bracket in a message stays text and does not become a tag', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('L=4 R=24 sum=28');
  log.log('&nbsp;&nbsp;sum &gt; target &rarr; move R left');

  const note = rows(dom)[0].querySelector('.step-note');
  assert.equal(text(note), 'sum > target → move R left');
  assert.equal(note.querySelectorAll('*').length, note.querySelectorAll('span, i, b').length);
});

// ── State ─────────────────────────────────────────────────────────────────

test('only the newest step carries the latest marker', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('one');
  log.log('two');
  log.log('three');

  const latest = dom.window.document.querySelectorAll('#log .is-latest');
  assert.equal(latest.length, 1);
  assert.equal(body(latest[0]), 'three');
});

test('the panel counter and empty state follow the trace', () => {
  const dom = boot();
  const doc = dom.window.document;
  const count = doc.querySelector('[data-trace-count]');
  const empty = doc.querySelector('[data-trace-empty]');
  const log = dom.window.createLogger('log');

  log.log('one');
  assert.equal(count.textContent, '1 step');
  assert.equal(empty.hidden, true);

  log.log('two');
  assert.equal(count.textContent, '2 steps');

  log.clear();
  assert.equal(count.textContent, '0 steps');
  assert.equal(empty.hidden, false, 'the prompt comes back on reset');
  assert.equal(rows(dom).length, 0);
});

test('a phase heading takes the empty-state prompt down', () => {
  const dom = boot();
  const empty = dom.window.document.querySelector('[data-trace-empty]');
  const log = dom.window.createLogger('log');
  // bellman-ford and floyd-warshall both open their run with one of these.
  log.log('--- Iteration 1 ---');
  assert.equal(empty.hidden, true, 'the prompt would print over the heading');
});

test('clear() restarts the numbering', () => {
  const dom = boot();
  const log = dom.window.createLogger('log');
  log.log('one');
  log.clear();
  log.log('one again');
  assert.equal(rows(dom)[0].querySelector('.step-n').textContent, '1');
});

// The CSS is the other half of the contract: a row class the stylesheet does
// not know about renders as an undifferentiated line.
test('every row class the logger emits is styled', () => {
  for (const sel of ['.viz-log .step-item', '.viz-log .step-note', '.viz-log .step-phase',
                     '.viz-log .step-n', '.viz-log .step-main', '.viz-log .k', '.viz-log .v',
                     '.step-item.is-latest', '.step-item.is-key']) {
    assert.ok(STYLE.includes(sel), `${sel} is unstyled`);
  }
});

// ── repaintable ───────────────────────────────────────────────────────────

test('repaintable replays the last frame instead of resetting it', () => {
  const dom = boot();
  const seen = [];
  let draw = dom.window.VIZ.repaintable((...args) => seen.push(args));

  draw(0, 14);
  draw(3, 9, 6);
  draw.repaint();

  assert.deepEqual(seen, [[0, 14], [3, 9, 6], [3, 9, 6]]);
});

test('repaint before any draw is a no-op call, not a crash', () => {
  const dom = boot();
  let calls = 0;
  const draw = dom.window.VIZ.repaintable(() => { calls++; });
  draw.repaint();
  assert.equal(calls, 1);
});

test('repaint drops the resize Event a listener would otherwise pass in', () => {
  const dom = boot();
  const seen = [];
  const draw = dom.window.VIZ.repaintable((...args) => seen.push(args));
  draw(1, 2);
  // This is the shape the pages register: addEventListener hands the handler an
  // Event, and repaint must ignore it in favour of the recorded arguments.
  dom.window.addEventListener('resize', draw.repaint);
  dom.window.dispatchEvent(new dom.window.Event('resize'));
  assert.deepEqual(seen[1], [1, 2]);
});

// ── Palette helpers ───────────────────────────────────────────────────────

test('VIZ.on picks ink that stays readable on the fill under it', () => {
  const dom = boot();
  assert.equal(dom.window.VIZ.on('#ffffff'), '#000000');
  assert.equal(dom.window.VIZ.on('#000000'), '#ffffff');
  assert.equal(dom.window.VIZ.on('rgb(26, 127, 55)'), '#ffffff');
});

test('VIZ.font asks for the visualizer face, not the platform default', () => {
  const dom = boot();
  assert.match(dom.window.VIZ.font(12), /^12px .*monospace$/);
  assert.match(dom.window.VIZ.font(12, 'bold'), /^bold 12px /);
});
