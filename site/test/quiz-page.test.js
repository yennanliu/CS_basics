const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { JSDOM } = require('jsdom');

// The Complexity Quiz, booted in both languages against the real bank.
//
// lc-complexity-quiz.html and lc-complexity-quiz.zh.html are two thin shells
// around one script, site/lc-complexity-quiz.js, which picks its strings and
// its bank by <html lang>. So the shells are run rather than read: the real
// markup, the real complexity.js and lc-complexity-quiz.js inlined in place of
// their <script src> tags, and the bank built from data/ by build-quiz.js and
// served through a stubbed fetch.
const SITE = path.join(__dirname, '..');
const ROOT = path.join(SITE, '..');
const PAGES = {
  en: path.join(SITE, 'pages', 'lc-complexity-quiz.html'),
  zh: path.join(SITE, 'pages', 'lc-complexity-quiz.zh.html'),
};

const { buildQuiz, buildZh } = require('../build-quiz.js');
const { parseReadmeProblems } = require('../build-roadmap.js');
const readJson = (f) => JSON.parse(fs.readFileSync(path.join(ROOT, f), 'utf8'));
const BUILT = buildQuiz(readJson('data/complexity_quiz.json'),
  parseReadmeProblems(fs.readFileSync(path.join(ROOT, 'README.md'), 'utf8')));
const BANKS = {
  './data/complexity-quiz.json': BUILT,
  './data/complexity-quiz.zh.json': buildZh(BUILT, readJson('data/complexity_quiz.zh.json')),
};

// `stored` seeds localStorage, which jsdom gives each instance fresh.
async function boot(lang, only, url, stored) {
  const inline = (file) => '<script>' + fs.readFileSync(path.join(SITE, file), 'utf8') + '</script>';
  const html = fs.readFileSync(PAGES[lang], 'utf8')
    .replace('<script src="nav.js"></script>', '')
    .replace('<script>CSNav.mount();</script>', '')
    .replace('<script src="complexity.js"></script>', () => inline('complexity.js'))
    .replace('<script src="lc-complexity-quiz.js"></script>', () => inline('lc-complexity-quiz.js'));
  const fetched = [];
  const dom = new JSDOM(html, {
    runScripts: 'dangerously',
    pretendToBeVisual: true,
    url: url || 'https://example.test/',
    beforeParse(window) {
      if (stored) window.localStorage.setItem('complexity-quiz-prefs', JSON.stringify(stored));
      window.fetch = (url) => {
        fetched.push(url);
        const bank = BANKS[url];
        // Narrow the bank to one question so the test knows what it is answering.
        const data = only ? Object.assign({}, bank, { questions: bank.questions.filter((q) => q.id === only) }) : bank;
        return Promise.resolve({ json: () => Promise.resolve(JSON.parse(JSON.stringify(data))) });
      };
    },
  });
  for (let i = 0; i < 20 && dom.window.document.getElementById('setup').hidden; i++) {
    await new Promise((r) => setTimeout(r, 5));
  }
  dom.fetched = fetched;
  return dom;
}

const $ = (dom, sel) => dom.window.document.querySelector(sel);
const $$ = (dom, sel) => [...dom.window.document.querySelectorAll(sel)];
const text = (el) => (el ? el.textContent.replace(/\s+/g, ' ').trim() : '');
const click = (dom, sel) => $(dom, sel).dispatchEvent(new dom.window.Event('click', { bubbles: true }));

function answer(dom, time, space) {
  $(dom, '#timeInput').value = time;
  $(dom, '#spaceInput').value = space;
  click(dom, '#checkBtn');
}

for (const lang of ['en', 'zh']) {
  test(`${lang}: boots, fetches its own bank and fills the setup screen`, async () => {
    const dom = await boot(lang);
    assert.deepEqual(dom.fetched, [lang === 'zh' ? './data/complexity-quiz.zh.json' : './data/complexity-quiz.json']);
    assert.ok(!$(dom, '#setup').hidden);
    // The classic set is the default; Focus lists only its topics.
    const classic = BUILT.questions.filter((q) => q.pack === 'classic');
    assert.equal($(dom, '#packSelect').value, 'classic');
    assert.equal($$(dom, '#topicSelect option').length, new Set(classic.map((q) => q.topic)).size + 1);
    assert.ok(text($(dom, '#poolText')).includes(String(classic.length)));
    assert.doesNotMatch($(dom, '.container').innerHTML, /undefined/);
  });

  test(`${lang}: a graded question highlights the lines that set each bound`, async () => {
    const dom = await boot(lang, 'two-sum-hash');
    click(dom, '#startBtn');
    const q = BUILT.questions.find((x) => x.id === 'two-sum-hash');
    assert.equal($$(dom, '#qCode .ln').length, q.code.split('\n').length, 'one numbered span per line');
    assert.ok(!$(dom, '#qCode').classList.contains('graded'), 'nothing is given away before checking');

    answer(dom, 'O(n)', 'O(1)');
    assert.ok($(dom, '#qCode').classList.contains('graded'));
    const hotT = $$(dom, '#qCode .ln.hot-t').map((el) => Number(el.dataset.n));
    const hotS = $$(dom, '#qCode .ln.hot-s').map((el) => Number(el.dataset.n));
    assert.deepEqual(hotT, q.cost.time.lines);
    assert.deepEqual(hotS, q.cost.space.lines);

    assert.ok($(dom, '.dim.t').classList.contains('right'));
    assert.ok($(dom, '.dim.s').classList.contains('wrong'));
    assert.match(text($(dom, '.dim.s .diag')), lang === 'zh' ? /太低.*保留/ : /too low.*holds/, 'O(1) for O(n) is an under-estimate, worded for space');
    assert.doesNotMatch($(dom, '#verdict').innerHTML, /undefined/);
  });

  test(`${lang}: a dominated term and a dropped paren still count`, async () => {
    const dom = await boot(lang, 'two-sum-hash');
    click(dom, '#startBtn');
    answer(dom, 'O(n + log n', 'O(n) + O(1)');
    assert.ok(!$(dom, '#verdict').hidden, 'both answers were read and graded');
    assert.ok($(dom, '.dim.t').classList.contains('right'));
    assert.ok($(dom, '.dim.s').classList.contains('right'));
  });

  test(`${lang}: gibberish is not graded`, async () => {
    const dom = await boot(lang, 'two-sum-hash');
    click(dom, '#startBtn');
    answer(dom, 'n squared-ish', 'O(n + 1)');
    assert.ok($(dom, '#verdict').hidden, 'nothing was graded');
    assert.ok(text($(dom, '#timeMsg')).length > 0, 'the time box says why');
    assert.equal(text($(dom, '#spaceMsg')), '', 'the readable box has no message');
    $(dom, '#timeInput').value = 'O(n + n)';
    click(dom, '#checkBtn');
    assert.ok(!$(dom, '#verdict').hidden);
    assert.ok($(dom, '.dim.t').classList.contains('right'));
    assert.ok($(dom, '.dim.s').classList.contains('right'));
  });

  test(`${lang}: a symbol the question never defines is named`, async () => {
    const dom = await boot(lang, 'two-sum-hash');
    click(dom, '#startBtn');
    answer(dom, 'O(k)', 'O(n)');
    assert.match(text($(dom, '.dim.t .diag')), /k/);
    assert.match(text($(dom, '.dim.t .diag')), /n/);
  });
}

// ── Question sets ────────────────────────────────────────────────────────

const inPack = (pack) => BUILT.questions.filter((q) => pack === 'all' || q.pack === pack);
const change = (dom, sel, value) => {
  $(dom, sel).value = value;
  $(dom, sel).dispatchEvent(new dom.window.Event('change', { bubbles: true }));
};

test('the bank carries both sets, and the essentials never repeat a classic problem', () => {
  assert.deepEqual(BUILT.packs, ['classic', 'essentials']);
  assert.ok(inPack('essentials').length >= 40, 'the essentials set is a real set, not a token');
  // A problem in both sets would be asked twice under "Both sets".
  const classicLc = new Set(inPack('classic').map((q) => q.lc).filter(Boolean));
  const repeats = inPack('essentials').filter((q) => classicLc.has(q.lc)).map((q) => q.lc);
  assert.deepEqual(repeats, []);
});

for (const lang of ['en', 'zh']) {
  test(`${lang}: the Question set dropdown offers each set and both, with counts`, async () => {
    const dom = await boot(lang);
    const options = $$(dom, '#packSelect option');
    assert.deepEqual(options.map((o) => o.value), ['classic', 'essentials', 'all']);
    options.forEach((o) => assert.ok(text(o).includes(String(inPack(o.value).length)), text(o)));
    assert.ok(!$(dom, '#packRow').hidden);
  });
}

test('switching to essentials narrows the pool and the Focus list', async () => {
  const dom = await boot('en');
  change(dom, '#topicSelect', 'Python Pitfalls');       // a topic essentials does not have
  change(dom, '#packSelect', 'essentials');
  const essentials = inPack('essentials');
  assert.equal($(dom, '#topicSelect').value, 'all', 'a topic the set lacks falls back to everything');
  assert.equal($$(dom, '#topicSelect option').length, new Set(essentials.map((q) => q.topic)).size + 1);
  assert.ok(text($(dom, '#poolText')).startsWith(essentials.length + ' questions'));

  change(dom, '#topicSelect', 'Stack');
  const stack = essentials.filter((q) => q.topic === 'Stack').length;
  assert.ok(text($(dom, '#poolText')).startsWith(stack + ' questions'));

  click(dom, '[data-count="all"]');
  click(dom, '#startBtn');
  assert.ok(text($(dom, '#qCounter')).includes(String(stack)), text($(dom, '#qCounter')));
  assert.equal(text($(dom, '#qTopic')), 'Stack');
});

test('the chosen set is remembered, and ?set= overrides it without saving', async () => {
  let dom = await boot('en');
  change(dom, '#packSelect', 'essentials');
  assert.equal(JSON.parse(dom.window.localStorage.getItem('complexity-quiz-prefs')).pack, 'essentials');

  dom = await boot('en', null, 'https://example.test/lc-complexity-quiz.html?set=essentials',
    { count: 10, topic: 'all', pack: 'classic' });
  assert.equal($(dom, '#packSelect').value, 'essentials');
  assert.ok(text($(dom, '#poolText')).startsWith(inPack('essentials').length + ' questions'));
  click(dom, '[data-count="5"]');
  assert.equal(JSON.parse(dom.window.localStorage.getItem('complexity-quiz-prefs')).pack, 'classic',
    'saving another preference keeps the stored set, not the linked one');

  dom = await boot('en', null, 'https://example.test/lc-complexity-quiz.html?set=all');
  assert.equal($(dom, '#packSelect').value, 'all');

  dom = await boot('en', null, 'https://example.test/lc-complexity-quiz.html?set=nonsense');
  assert.equal($(dom, '#packSelect').value, 'classic', 'an unknown ?set= is ignored');
});

test('a Focus the linked set lacks is not erased from what is saved', async () => {
  const dom = await boot('en', null, 'https://example.test/lc-complexity-quiz.html?set=essentials',
    { count: 10, topic: 'Python Pitfalls', pack: 'classic' });   // a classic-only topic
  assert.equal($(dom, '#topicSelect').value, 'all', 'the visit falls back to everything');
  click(dom, '[data-count="5"]');
  const saved = JSON.parse(dom.window.localStorage.getItem('complexity-quiz-prefs'));
  assert.equal(saved.topic, 'Python Pitfalls');
  assert.equal(saved.pack, 'classic');
});

test('distance-k stops once the frontier is empty, so a huge k costs nothing', () => {
  const q = BUILT.questions.find((x) => x.id === 'nodes-distance-k');
  assert.match(q.code, /if not frontier:\n\s+break/);
  assert.equal(q.time, 'O(n)');
});

test('zh: the page writes Chinese prose and topic names, but grades the same answers', async () => {
  const dom = await boot('zh', 'two-sum-hash');
  assert.match(text($(dom, '#topicSelect')), /陣列與雜湊/);
  click(dom, '#startBtn');
  assert.match(text($(dom, '#qTopic')), /陣列與雜湊/);
  answer(dom, 'O(n)', 'O(n)');
  assert.match(text($(dom, '.dim.t .reason')), /[一-鿿]/);
  assert.match(text($(dom, '.dim.t .field')), /時間/);
});

test('the report lists each missed bound with its reason', async () => {
  const dom = await boot('en', 'two-sum-hash');
  click(dom, '#startBtn');
  answer(dom, 'O(n^2)', 'O(n)');
  click(dom, '#nextBtn');
  assert.ok(!$(dom, '#report').hidden);
  assert.equal(text($(dom, '#scoreBig')), '1 / 2');
  assert.equal($$(dom, '#missedList .missed-detail').length, 1, 'only the missed bound');
  assert.match(text($(dom, '#missedList')), /time is O\(n\)/);
});

test('lineRange folds runs of consecutive lines', async () => {
  const dom = await boot('en');
  assert.equal(dom.window.CSQuiz.lineRange([9, 3, 4, 5]), '3–5, 9');
  assert.equal(dom.window.CSQuiz.lineRange([2]), '2');
});

test('the two shells keep the same element ids', () => {
  const ids = (file) => [...fs.readFileSync(file, 'utf8').matchAll(/\bid="([^"]+)"/g)].map((m) => m[1]).sort();
  assert.deepEqual(ids(PAGES.zh), ids(PAGES.en));
});

test('both string tables carry the same keys', async () => {
  const dom = await boot('en');
  const { STRINGS } = dom.window.CSQuiz;
  const keysOf = (s) => {
    const flat = [];
    for (const [k, v] of Object.entries(s)) {
      if (v && typeof v === 'object' && !Array.isArray(v)) for (const kk of Object.keys(v)) flat.push(k + '.' + kk);
      else flat.push(k);
    }
    return flat.sort();
  };
  assert.deepEqual(keysOf(STRINGS.zh), keysOf(STRINGS.en));
});
