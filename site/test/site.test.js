const test = require('node:test');
const assert = require('node:assert/strict');
const { setupDOM, teardownDOM, click } = require('./helpers');

setupDOM();
const CSSite = require('../site.js');

test.afterEach(() => { setupDOM(); });
test.after(() => { teardownDOM(); });

// ── Table wrapping ────────────────────────────────────────────────────────

test('wrapTables puts a scroll container around each bare table', () => {
  setupDOM('<table id="t"><tr><td>a</td></tr></table>');
  CSSite.wrapTables(document);

  const table = document.getElementById('t');
  assert.equal(table.parentElement.className, 'table-wrap');
});

test('wrapTables is idempotent', () => {
  setupDOM('<table><tr><td>a</td></tr></table>');
  CSSite.wrapTables(document);
  CSSite.wrapTables(document);

  assert.equal(document.querySelectorAll('.table-wrap').length, 1);
});

test('wrapTables handles every table on the page', () => {
  setupDOM('<table></table><div><table></table></div><table></table>');
  CSSite.wrapTables(document);

  assert.equal(document.querySelectorAll('.table-wrap').length, 3);
});

test('wrapTables keeps the table in its original position', () => {
  setupDOM('<div id="host"><p>before</p><table id="t"></table><p>after</p></div>');
  CSSite.wrapTables(document);

  const kids = [...document.getElementById('host').children].map((el) => el.tagName + '.' + el.className);
  assert.deepEqual(kids, ['P.', 'DIV.table-wrap', 'P.']);
});

// ── Reading progress ──────────────────────────────────────────────────────

test('readingProgress reports how far down the document is scrolled', () => {
  assert.equal(CSSite.readingProgress({ scrollHeight: 2000, clientHeight: 1000, scrollTop: 0 }), 0);
  assert.equal(CSSite.readingProgress({ scrollHeight: 2000, clientHeight: 1000, scrollTop: 500 }), 50);
  assert.equal(CSSite.readingProgress({ scrollHeight: 2000, clientHeight: 1000, scrollTop: 1000 }), 100);
});

test('readingProgress reports 0 rather than dividing by zero on a short page', () => {
  assert.equal(CSSite.readingProgress({ scrollHeight: 800, clientHeight: 800, scrollTop: 0 }), 0);
});

test('initReadingProgress is a no-op when the page has no progress bar', () => {
  setupDOM('<p>no bar</p>');
  assert.doesNotThrow(() => CSSite.initReadingProgress());
});

test('initReadingProgress sizes the bar on scroll', () => {
  setupDOM('<div id="reading-progress"></div>');
  CSSite.initReadingProgress();

  const doc = document.documentElement;
  Object.defineProperty(doc, 'scrollHeight', { value: 2000, configurable: true });
  Object.defineProperty(doc, 'clientHeight', { value: 1000, configurable: true });
  doc.scrollTop = 250;
  window.dispatchEvent(new window.Event('scroll'));

  assert.equal(document.getElementById('reading-progress').style.width, '25%');
});

// ── Copy button ───────────────────────────────────────────────────────────

test('copyCode copies the sibling <pre> and flashes confirmation on the button', async () => {
  setupDOM(`<div class="code-block-wrapper">
      <button class="copy-btn">copy</button>
      <pre>print("hi")</pre>
    </div>`);

  const written = [];
  global.navigator.clipboard = { writeText: (t) => { written.push(t); return Promise.resolve(); } };

  const btn = document.querySelector('.copy-btn');
  // jsdom does not implement innerText, which is what the browser reads.
  Object.defineProperty(document.querySelector('pre'), 'innerText', { value: 'print("hi")' });

  CSSite.copyCode(btn);
  await Promise.resolve();

  assert.deepEqual(written, ['print("hi")']);
  assert.equal(btn.textContent, 'copied');
  assert.equal(btn.classList.contains('copied'), true);
});

test('copyCode does nothing when the clipboard API is unavailable', () => {
  setupDOM('<div class="code-block-wrapper"><button class="copy-btn">copy</button><pre>x</pre></div>');
  global.navigator.clipboard = undefined;

  const btn = document.querySelector('.copy-btn');
  assert.doesNotThrow(() => CSSite.copyCode(btn));
  assert.equal(btn.textContent, 'copy');
});

// ── Wiring ────────────────────────────────────────────────────────────────

test('init wires tables and the progress bar together', () => {
  setupDOM('<div id="reading-progress"></div><table></table>');
  CSSite.init();

  assert.equal(document.querySelectorAll('.table-wrap').length, 1);
});

test('loading site.js exposes copyCode as a global', () => {
  // The generated pages emit onclick="copyCode(this)", which resolves against
  // the global scope rather than the module — so loading the file has to
  // install it there. Re-required to model a fresh page load.
  setupDOM();
  assert.equal(typeof global.self.copyCode, 'undefined');

  delete require.cache[require.resolve('../site.js')];
  require('../site.js');

  assert.equal(typeof global.self.copyCode, 'function');
});

// ── TOC ───────────────────────────────────────────────────────────────────

// Mirrors what build-site.js emits: a <details> rail whose links point at
// heading ids in the body.
function tocPage(sections) {
  const items = sections.map(s =>
    `<li class="toc-item toc-l2"><a href="#${s.id}">${s.id}</a>` +
    (s.subs || []).map(sub =>
      `<ul class="toc-sublist toc-sublist-1"><li class="toc-item toc-l3"><a href="#${sub}">${sub}</a></li></ul>`
    ).join('') +
    '</li>'
  ).join('');
  const body = sections.map(s =>
    `<h2 id="${s.id}">${s.id}</h2>` + (s.subs || []).map(sub => `<h3 id="${sub}">${sub}</h3>`).join('')
  ).join('');
  return '<div class="page-layout"><aside class="toc-rail"><details class="toc" open data-toc>' +
    `<summary class="toc-summary">On this page</summary><nav class="toc-nav"><ul class="toc-list">${items}</ul></nav>` +
    `</details></aside><div class="page-main"><div class="cheatsheet-content">${body}</div></div></div>`;
}

test('initTOC is a no-op on a page with no TOC', () => {
  setupDOM('<p>no toc here</p>');
  assert.equal(CSSite.initTOC(document), null);
});

test('initTOC leaves the TOC open on a wide viewport', () => {
  const dom = setupDOM(tocPage([{ id: 'one' }, { id: 'two' }]));
  dom.window.innerWidth = 1400;
  CSSite.initTOC(document);

  assert.equal(document.querySelector('.toc').open, true);
});

test('initTOC collapses the TOC on a narrow viewport so it does not push the doc down', () => {
  const dom = setupDOM(tocPage([{ id: 'one' }, { id: 'two' }]));
  dom.window.innerWidth = 480;
  CSSite.initTOC(document);

  assert.equal(document.querySelector('.toc').open, false);
});

test('initTOC marks the first section active so a collapsed rail is never empty', () => {
  setupDOM(tocPage([{ id: 'one' }, { id: 'two' }]));
  CSSite.initTOC(document);

  const active = document.querySelectorAll('.toc-item.is-active');
  assert.equal(active.length, 1);
  assert.equal(active[0].querySelector('a').getAttribute('href'), '#one');
});

test('initTOC ignores TOC links whose heading is missing from the page', () => {
  setupDOM(tocPage([{ id: 'one' }]).replace('</ul></nav>',
    '<li class="toc-item toc-l2"><a href="#ghost">ghost</a></li></ul></nav>'));

  const result = CSSite.initTOC(document);
  assert.equal(result.sections, 1);
});

test('initTOC collapses sub-sections only once the TOC is dense', () => {
  const sparse = [{ id: 'a', subs: ['a1', 'a2'] }, { id: 'b', subs: ['b1'] }];
  setupDOM(tocPage(sparse));
  CSSite.initTOC(document);
  assert.equal(document.querySelector('.toc-list').classList.contains('toc-dense'), false);

  const dense = Array.from({ length: 10 }, (_, i) => ({
    id: `s${i}`,
    subs: Array.from({ length: 4 }, (_, j) => `s${i}-${j}`)
  }));
  setupDOM(tocPage(dense));
  CSSite.initTOC(document);

  const list = document.querySelector('.toc-list');
  assert.equal(list.classList.contains('toc-dense'), true);
  // Exactly one group is expanded — the one holding the active section.
  assert.equal(list.querySelectorAll('.toc-l2.is-open').length, 1);
  assert.equal(list.querySelector('.toc-l2.is-open a').getAttribute('href'), '#s0');
});

test('init wires the TOC alongside tables and the progress bar', () => {
  setupDOM(`<div id="reading-progress"></div>${tocPage([{ id: 'one' }, { id: 'two' }])}`);
  CSSite.init();

  assert.equal(document.querySelectorAll('.toc-item.is-active').length, 1);
});

// ── Cheatsheet index filter ───────────────────────────────────────────────

// Mirrors the index markup: a filter bar, then one section per category.
function indexPage(cards) {
  var byCat = {};
  cards.forEach(function (c) { (byCat[c.category] = byCat[c.category] || []).push(c); });
  var sections = Object.keys(byCat).map(function (cat) {
    return `<section class="cat-section" data-category="${cat}"><h3 class="cat-heading">${cat}</h3>` +
      '<div class="sheet-grid">' +
      byCat[cat].map(c =>
        `<article class="cheatsheet-card sheet-card tier-${c.tier}" data-tier="${c.tier}" data-search="${c.search}">` +
        `<h4 class="card-title">${c.title}</h4></article>`
      ).join('') +
      '</div></section>';
  }).join('');
  return '<section class="tier-key">key</section><section class="start-here">ladder</section>' +
    '<div class="index-filter" data-sheet-filter>' +
    '<input class="filter-input" type="search">' +
    '<div class="filter-tiers">' +
    '<button class="filter-chip is-on" data-min-tier="0">All</button>' +
    '<button class="filter-chip" data-min-tier="4">4+</button>' +
    '<button class="filter-chip" data-min-tier="5">5</button>' +
    '</div>' +
    `<p class="filter-status" data-total="${cards.length}"></p></div>` +
    sections +
    '<p class="filter-empty" hidden>Nothing. <button class="filter-reset">Clear it</button></p>';
}

const FILTER_CARDS = [
  { title: 'Array', tier: 5, category: 'Arrays', search: 'array arrays in-place rewriting' },
  { title: 'Sliding Window', tier: 5, category: 'Arrays', search: 'sliding window expand contract' },
  { title: 'Difference Array', tier: 3, category: 'Arrays', search: 'difference array range update' },
  { title: 'Dijkstra', tier: 4, category: 'Graphs', search: 'dijkstra shortest path weights' }
];

const shown = () => [...document.querySelectorAll('.sheet-card:not([hidden])')]
  .map(c => c.querySelector('.card-title').textContent);

test('initSheetFilter is a no-op on a page without the filter bar', () => {
  setupDOM('<p>not the index</p>');
  assert.equal(CSSite.initSheetFilter(document), null);
});

test('initSheetFilter shows every sheet and the plain total before any input', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  assert.equal(shown().length, 4);
  assert.equal(document.querySelector('.filter-status').textContent, '4 sheets');
});

test('initSheetFilter matches the description and category, not just the title', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  const input = document.querySelector('.filter-input');
  input.value = 'shortest';
  input.dispatchEvent(new window.Event('input'));

  assert.deepEqual(shown(), ['Dijkstra']);
  assert.equal(document.querySelector('.filter-status').textContent, '1 of 4 sheets');
});

test('initSheetFilter requires every term to match, so terms narrow the result', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  const input = document.querySelector('.filter-input');
  input.value = 'array range';
  input.dispatchEvent(new window.Event('input'));

  assert.deepEqual(shown(), ['Difference Array']);
});

test('initSheetFilter hides a category once none of its sheets match', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  const input = document.querySelector('.filter-input');
  input.value = 'dijkstra';
  input.dispatchEvent(new window.Event('input'));

  const visible = [...document.querySelectorAll('.cat-section:not([hidden])')]
    .map(s => s.getAttribute('data-category'));
  assert.deepEqual(visible, ['Graphs']);
});

test('initSheetFilter narrows to the priority a chip asks for', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  click(document.querySelector('[data-min-tier="4"]'));
  assert.deepEqual(shown().sort(), ['Array', 'Dijkstra', 'Sliding Window']);

  click(document.querySelector('[data-min-tier="5"]'));
  assert.deepEqual(shown().sort(), ['Array', 'Sliding Window']);
});

test('initSheetFilter marks the active chip and only that one', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  click(document.querySelector('[data-min-tier="5"]'));
  const on = [...document.querySelectorAll('.filter-chip.is-on')];
  assert.equal(on.length, 1);
  assert.equal(on[0].getAttribute('data-min-tier'), '5');
});

test('initSheetFilter combines the text query with the priority chip', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  const input = document.querySelector('.filter-input');
  input.value = 'array';
  input.dispatchEvent(new window.Event('input'));
  click(document.querySelector('[data-min-tier="5"]'));

  assert.deepEqual(shown(), ['Array']);
});

test('initSheetFilter tucks away the ladder and the key while a filter is active', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);
  const key = document.querySelector('.tier-key');
  const ladder = document.querySelector('.start-here');
  assert.equal(key.hidden, false);

  const input = document.querySelector('.filter-input');
  input.value = 'heap';
  input.dispatchEvent(new window.Event('input'));
  assert.equal(key.hidden, true);
  assert.equal(ladder.hidden, true);

  input.value = '';
  input.dispatchEvent(new window.Event('input'));
  assert.equal(key.hidden, false, 'clearing the query brings the ladder back');
});

test('initSheetFilter explains an empty result instead of showing a blank page', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  const input = document.querySelector('.filter-input');
  input.value = 'quantum';
  input.dispatchEvent(new window.Event('input'));

  assert.equal(shown().length, 0);
  assert.equal(document.querySelector('.filter-empty').hidden, false);
  assert.equal(document.querySelector('.filter-status').textContent, '0 of 4 sheets');
});

test('the clear button resets both the query and the priority chip', () => {
  setupDOM(indexPage(FILTER_CARDS));
  CSSite.initSheetFilter(document);

  const input = document.querySelector('.filter-input');
  input.value = 'quantum';
  input.dispatchEvent(new window.Event('input'));
  click(document.querySelector('[data-min-tier="5"]'));
  click(document.querySelector('.filter-reset'));

  assert.equal(input.value, '');
  assert.equal(shown().length, 4);
  assert.equal(document.querySelector('.filter-chip.is-on').getAttribute('data-min-tier'), '0');
});

test('init wires the index filter alongside the rest', () => {
  setupDOM(`<div id="reading-progress"></div>${indexPage(FILTER_CARDS)}`);
  CSSite.init();

  assert.equal(document.querySelector('.filter-status').textContent, '4 sheets');
});

test('initSheetFilter rewrites a category count to match what is on screen', () => {
  setupDOM(indexPage(FILTER_CARDS).replace(
    '<h3 class="cat-heading">Arrays</h3>',
    '<h3 class="cat-heading">Arrays<span class="cat-count">3 sheets</span></h3>'
  ));
  CSSite.initSheetFilter(document);
  const count = document.querySelector('.cat-count');
  assert.equal(count.textContent, '3 sheets');

  const input = document.querySelector('.filter-input');
  input.value = 'window';
  input.dispatchEvent(new window.Event('input'));
  assert.equal(count.textContent, '1 of 3 sheets');

  input.value = '';
  input.dispatchEvent(new window.Event('input'));
  assert.equal(count.textContent, '3 sheets', 'clearing the filter restores the full count');
});
