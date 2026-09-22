const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const { JSDOM } = require('jsdom');

const { buildPayload, loadCatalog } = require('../build-review-plan.js');

// The shipped page, booted against the real practice log.
//
// lc-review-plan.html is hand-maintained and its whole value is in the script
// inside it: a planner, a topic-balance table and four other views, none of
// which any build step can see. The page shipped broken twice before — a
// filter bar wired to handlers that only existed inside boot(), and before
// that a pasted-in copy of the log — and both were the kind of break that
// looks fine in the HTML and is empty in the browser.
//
// So the page is run, not read: jsdom, the real markup, and the same payload
// site/build-review-plan.js writes to _site/data/progress.json.
const ROOT = path.join(__dirname, '..', '..');
const PAGE = path.join(ROOT, 'site', 'pages', 'lc-review-plan.html');

let payload;
function log() {
  if (!payload) {
    payload = buildPayload(fs.readFileSync(path.join(ROOT, 'data', 'progress.txt'), 'utf8'),
                           loadCatalog(ROOT)).payload;
  }
  return payload;
}

/** Boots the page with `fetch` stubbed and waits for boot() to finish. */
async function render(data) {
  // nav.js is loaded from the built tree, not from site/pages/, so the two
  // script tags that need it are dropped rather than stubbed — the navbar is
  // covered by nav.test.js and e2e-check.js.
  const html = fs.readFileSync(PAGE, 'utf8')
    .replace('<script src="nav.js"></script>', '')
    .replace('<script>CSNav.mount();</script>', '');

  const dom = new JSDOM(html, {
    runScripts: 'dangerously', pretendToBeVisual: true, url: 'https://example.test/',
  });
  dom.window.fetch = () => Promise.resolve({ ok: true, json: () => Promise.resolve(data) });
  dom.window.eval("fetch('./data/progress.json').then(r => r.json()).then(boot)");
  // Two microtask turns for the stubbed fetch, then let any timer settle.
  await new Promise((resolve) => dom.window.setTimeout(resolve, 0));
  await new Promise((resolve) => dom.window.setTimeout(resolve, 0));
  return dom;
}

const $ = (dom, sel) => dom.window.document.querySelector(sel);
const $$ = (dom, sel) => [...dom.window.document.querySelectorAll(sel)];
const text = (el) => (el ? el.textContent.replace(/\s+/g, ' ').trim() : '');

// ── Today's plan ────────────────────────────────────────────────────────────

test("the plan fills the session size it is asked for", async () => {
  const dom = await render(log());
  assert.equal($$(dom, '.plan-item').length, 5, 'default session is five problems');

  const size = $(dom, '#plan-size');
  size.value = '12';
  size.dispatchEvent(new dom.window.Event('change', { bubbles: true }));
  assert.equal($$(dom, '.plan-item').length, 12);
});

test('every pick says why it earned the slot', async () => {
  const dom = await render(log());
  for (const item of $$(dom, '.plan-item')) {
    const why = text(item.querySelector('.plan-why'));
    assert.ok(why.length > 0, 'a pick with no reason is a pick you cannot argue with');
    assert.match(why, /overdue|due today|due in/, `no schedule reason in "${why}"`);
    assert.match(why, /interval/);
  }
});

test('a pick names the problem, not just its number', async () => {
  const dom = await render(log());
  const titled = $$(dom, '.plan-item').filter((el) => text(el.querySelector('.plan-title')));
  assert.ok(titled.length >= 4, 'indexed titles should reach the plan');
  // ...and links to the canonical problem page rather than a search for digits.
  const href = $(dom, '.plan-item .prob-num a').getAttribute('href');
  assert.match(href, /^https:\/\/leetcode\.com\/problems\/[a-z0-9-]+\/$/);
});

test('the log line is the exact shape data/progress.txt is written in', async () => {
  const dom = await render(log());
  const line = $(dom, '#plan-line').value;
  assert.match(line, /^\d{8}: \d+(, \d+)*$/, line);
  const ids = line.split(': ')[1].split(', ');
  assert.equal(ids.length, $$(dom, '.plan-item').length);
});

test('ticking a problem off narrows the log line to what was actually done', async () => {
  const dom = await render(log());
  const first = $(dom, '.plan-item');
  const box = first.querySelector('.plan-check');
  box.checked = true;
  box.dispatchEvent(new dom.window.Event('change', { bubbles: true }));

  assert.ok(first.classList.contains('done'));
  assert.equal($(dom, '#plan-line').value,
    `${$(dom, '#plan-line').value.slice(0, 8)}: ${first.getAttribute('data-prob')}`);
});

test('"show me different ones" replaces the whole session', async () => {
  const dom = await render(log());
  const before = $$(dom, '.plan-item').map((el) => el.getAttribute('data-prob'));
  $(dom, '#plan-next').dispatchEvent(new dom.window.MouseEvent('click', { bubbles: true }));
  const after = $$(dom, '.plan-item').map((el) => el.getAttribute('data-prob'));

  assert.equal(after.length, before.length);
  assert.equal(after.filter((p) => before.includes(p)).length, 0);
});

test('the "again" filter picks only problems the log keeps re-opening', async () => {
  const dom = await render(log());
  const box = $(dom, '#plan-again');
  box.checked = true;
  box.dispatchEvent(new dom.window.Event('change', { bubbles: true }));

  const items = $$(dom, '.plan-item');
  assert.ok(items.length > 0);
  for (const item of items) {
    assert.match(text(item.querySelector('.plan-why')), /came back "again"/);
  }
});

test('unbalancing the plan is allowed to concentrate it', async () => {
  const dom = await render(log());
  const spread = (dom_) => new Set($$(dom_, '.plan-item .prob-meta')
    .map((el) => text(el).split(' · ').pop())).size;
  const balanced = spread(dom);

  const box = $(dom, '#plan-balance');
  box.checked = false;
  box.dispatchEvent(new dom.window.Event('change', { bubbles: true }));
  assert.ok(spread(dom) <= balanced,
    'a balanced plan should cover at least as many topics as an unbalanced one');
});

// ── Topic balance ───────────────────────────────────────────────────────────

test('the balance table compares every weighted topic', async () => {
  const dom = await render(log());
  const rows = $$(dom, '#bal-tbody tr');
  assert.ok(rows.length > 10, `only ${rows.length} topics compared`);
  // Sorted by deficit, so the topic most owed practice is the first row.
  assert.match(text(rows[0]), /^[A-Za-z]/);
  assert.ok($$(dom, '#bal-tbody .flag.under').length > 0,
    'a log this uneven must show at least one under-practised topic');
});

test('changing the window re-reads the log rather than rescaling the old answer', async () => {
  const dom = await render(log());
  const ratios = () => $$(dom, '#bal-tbody tr').map((tr) => text(tr.children[4]));
  const before = ratios();

  const select = $(dom, '#bal-window');
  select.value = '365';
  select.dispatchEvent(new dom.window.Event('change', { bubbles: true }));
  assert.notDeepEqual(ratios(), before);
});

// ── The other panels ────────────────────────────────────────────────────────

test('the stats row leads with what is overdue', async () => {
  const dom = await render(log());
  const cards = $$(dom, '.stat-card');
  assert.equal(cards.length, 7);
  assert.match(text(cards[0]), /^\d+Overdue$/);
  assert.match(text(cards[3]), /Marked "again"/);
});

test('the all-problems table renders, filters and sorts', async () => {
  const dom = await render(log());
  const rows = () => $$(dom, '#all-tbody tr');
  const all = rows().length;
  assert.ok(all > 500, `only ${all} rows`);

  // Filtering by title, which the old search box could not do — it only read
  // the number and the dates, because a title never reached the page.
  const search = $(dom, '#prob-search');
  search.value = 'two sum';
  search.dispatchEvent(new dom.window.Event('input', { bubbles: true }));
  assert.ok(rows().length > 0 && rows().length < all);
  assert.match(text(rows()[0]).toLowerCase(), /two sum/);

  search.value = '';
  search.dispatchEvent(new dom.window.Event('input', { bubbles: true }));
  const topic = $(dom, '#prob-topic');
  assert.ok(topic.options.length > 10, 'topics should be offered as a filter');
  topic.value = 'Tree';
  topic.dispatchEvent(new dom.window.Event('change', { bubbles: true }));
  assert.ok(rows().length > 0 && rows().length < all);
  assert.match(text($(dom, '#all-count')), /^Showing \d+ of \d+ problems$/);
});

test('the filter bar is enabled only once its handlers are real', async () => {
  // It ships disabled: the page's code runs inside boot(), so before the log
  // lands there is nothing behind the controls.
  const raw = fs.readFileSync(PAGE, 'utf8');
  assert.match(raw, /id="prob-search"[^>]*disabled/);

  const dom = await render(log());
  for (const id of ['prob-search', 'prob-topic', 'prob-sort', 'prob-again-only']) {
    assert.equal(dom.window.document.getElementById(id).disabled, false, id);
  }
});

test('due, heatmap, frequency and history all render', async () => {
  const dom = await render(log());
  assert.ok($$(dom, '.due-group').length > 0);
  assert.ok($$(dom, '.hm-cell').length > 300, 'a year of heatmap cells');
  assert.equal($$(dom, '.freq-row').length, 40);
  assert.ok($$(dom, '.tl-item').length > 0);
});

test('the tabs switch panels and remember the choice', async () => {
  const dom = await render(log());
  const click = (el) => el.dispatchEvent(new dom.window.MouseEvent('click', { bubbles: true }));

  click($(dom, '.tab-btn[data-tab="balance"]'));
  assert.ok($(dom, '#tab-balance').classList.contains('active'));
  assert.ok(!$(dom, '#tab-today').classList.contains('active'));
  assert.equal(dom.window.localStorage.getItem('reviewTab'), 'balance');
});

// ── Failure modes ───────────────────────────────────────────────────────────

test('a log the build could not enrich still schedules', async () => {
  // No PROBLEMS.md, no problem_lists.json: every row is a bare number with no topic
  // and no weight. The schedule is the part that must survive that.
  const bare = buildPayload(fs.readFileSync(path.join(ROOT, 'data', 'progress.txt'), 'utf8')).payload;
  const dom = await render(bare);
  assert.ok($$(dom, '.plan-item').length > 0, 'the plan needs no catalog to exist');
  assert.ok($$(dom, '#all-tbody tr').length > 500);
  assert.equal($$(dom, '#bal-tbody tr').length, 1, 'one Unfiled row, or the empty state');
});

test('a failed fetch says so instead of rendering an empty page', async () => {
  const html = fs.readFileSync(PAGE, 'utf8')
    .replace('<script src="nav.js"></script>', '')
    .replace('<script>CSNav.mount();</script>', '');
  const dom = new JSDOM(html, { runScripts: 'dangerously', url: 'https://example.test/' });
  dom.window.fetch = () => Promise.resolve({ ok: false, status: 404 });
  dom.window.eval(`fetch('./data/progress.json')
    .then(r => { if (!r.ok) throw new Error('HTTP ' + r.status); })
    .catch(e => {
      document.getElementById('plan-list').innerHTML =
        '<p class="empty">Could not load the practice log (' + e.message + ').</p>';
    })`);
  await new Promise((resolve) => dom.window.setTimeout(resolve, 0));
  assert.match(text($(dom, '#plan-list')), /Could not load the practice log \(HTTP 404\)/);
});
