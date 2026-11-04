const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { JSDOM } = require('jsdom');

// The Big-O comparison, booted in both languages.
//
// lc-big-o.html and lc-big-o.zh.html are two thin shells around one script,
// site/lc-big-o.js, which picks its strings by <html lang>. So the shells are
// run rather than read: the real markup, the real complexity.js and the real
// lc-big-o.js, inlined in place of their <script src> tags (nav.js is dropped —
// the navbar is covered by nav.test.js and e2e-check.js).
const SITE = path.join(__dirname, '..');
const PAGES = {
  en: path.join(SITE, 'pages', 'lc-big-o.html'),
  zh: path.join(SITE, 'pages', 'lc-big-o.zh.html'),
};

function boot(lang) {
  const inline = (file) => '<script>' + fs.readFileSync(path.join(SITE, file), 'utf8') + '</script>';
  const html = fs.readFileSync(PAGES[lang], 'utf8')
    .replace('<script src="nav.js"></script>', '')
    .replace('<script>CSNav.mount();</script>', '')
    .replace('<script src="complexity.js"></script>', () => inline('complexity.js'))
    .replace('<script src="lc-big-o.js"></script>', () => inline('lc-big-o.js'));
  return new JSDOM(html, { runScripts: 'dangerously', pretendToBeVisual: true, url: 'https://example.test/' });
}

const $ = (dom, sel) => dom.window.document.querySelector(sel);
const $$ = (dom, sel) => [...dom.window.document.querySelectorAll(sel)];
const text = (el) => (el ? el.textContent.replace(/\s+/g, ' ').trim() : '');

function plot(dom, value) {
  $(dom, '#addInput').value = value;
  $(dom, '#addForm').dispatchEvent(new dom.window.Event('submit', { cancelable: true }));
}

for (const lang of ['en', 'zh']) {
  test(`${lang}: the tool renders the ladder, chart, tables and a verdict`, () => {
    const dom = boot(lang);
    assert.equal($$(dom, '#ladder .curve-chip').length, 9);
    assert.ok($$(dom, '#chart path').length >= 9, 'one path per visible class');
    assert.equal($$(dom, '#classTable tbody tr').length, 9);
    assert.ok($$(dom, '#opsTable tbody tr').length >= 9);
    assert.ok(text($(dom, '#cmpOut')).length > 0, 'compare() ran on load');
    // A missing key renders as "undefined" somewhere — never ship that.
    assert.doesNotMatch($(dom, '.container').innerHTML, /undefined/);
  });

  test(`${lang}: an empty Plot is a neutral hint, not a red error`, () => {
    const dom = boot(lang);
    plot(dom, 'n^2 log n');
    assert.equal($$(dom, '#customList .curve-chip').length, 1);
    plot(dom, '   ');
    const msg = $(dom, '#addMsg');
    assert.ok(!msg.classList.contains('err'), 'empty input is not an error');
    assert.ok(text(msg).length > 0, 'it still says what to do');
    assert.equal($$(dom, '#customList .curve-chip').length, 1, 'nothing was removed or added');
    assert.equal(dom.window.document.activeElement, $(dom, '#addInput'), 'focus goes to the box');
  });

  test(`${lang}: a bad expression is an error, and typing clears it`, () => {
    const dom = boot(lang);
    plot(dom, 'n^^');
    assert.ok($(dom, '#addMsg').classList.contains('err'));
    $(dom, '#addInput').dispatchEvent(new dom.window.Event('input'));
    assert.equal(text($(dom, '#addMsg')), '');
  });
}

test('zh: the script writes Chinese, not the English strings', () => {
  const en = boot('en'), zh = boot('zh');
  assert.notEqual(text($(zh, '#chartDesc')), text($(en, '#chartDesc')));
  assert.match(text($(zh, '#classTable thead')), /類別/);
  assert.match(text($(zh, '#cmpOut')), /[一-鿿]/);
});

test('the two shells keep the same element ids', () => {
  const ids = (file) => [...fs.readFileSync(file, 'utf8').matchAll(/\bid="([^"]+)"/g)].map((m) => m[1]).sort();
  assert.deepEqual(ids(PAGES.zh), ids(PAGES.en));
});

test('both string tables carry the same keys', () => {
  const src = fs.readFileSync(path.join(SITE, 'lc-big-o.js'), 'utf8');
  // STRINGS lives inside the IIFE, so its literal is lifted out and evaluated.
  const keysOf = (lang) => {
    const d = new JSDOM('<html lang="' + lang + '"><body></body></html>', { runScripts: 'outside-only' });
    const literal = src.slice(src.indexOf('var STRINGS = '), src.indexOf('var LANG ='));
    d.window.eval(literal + ';window.__S = STRINGS;');
    const s = d.window.__S[lang];
    const flat = [];
    for (const [k, v] of Object.entries(s)) {
      if (v && typeof v === 'object') for (const kk of Object.keys(v)) flat.push(k + '.' + kk);
      else flat.push(k);
    }
    return flat.sort();
  };
  assert.deepEqual(keysOf('zh'), keysOf('en'));
});
