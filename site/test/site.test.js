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
