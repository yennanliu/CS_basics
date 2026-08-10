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
