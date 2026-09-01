const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');

const { parseProgress, classify, splitTopLevel, aggregate, mergeDays, buildPayload } =
  require('../build-review-plan.js');

const ROOT = path.join(__dirname, '..', '..');

// ── The shapes the real log actually contains ───────────────────────────────
//
// data/progress.txt is written by hand at the end of a practice session, so its
// format is loose in ways a strict parser would silently drop. Each case below
// is a line that exists in the real file.

test('parses the ordinary "date: ids" line', () => {
  const { days } = parseProgress('20260831: 997(todo), 907(again!!), 104(ok)');
  assert.equal(days.length, 1);
  assert.equal(days[0].date, '20260831');
  assert.deepEqual(days[0].items.map(i => i.id), [997, 907, 104]);
  assert.deepEqual(days[0].items.map(i => i.status), ['todo', 'again', 'ok']);
});

test('accepts a date separated from its payload by spaces rather than a colon', () => {
  const { days } = parseProgress('20260701  1740(again),399,907(again)');
  assert.deepEqual(days[0].items.map(i => i.id), [1740, 399, 907]);
});

test('a "|" splits a day into sessions and is not a problem separator', () => {
  const { days } = parseProgress('20260820: 416(again) | 300(again),70(ok)');
  assert.deepEqual(days[0].items.map(i => i.id), [416, 300, 70]);
});

test('an annotation containing a comma stays one entry', () => {
  const { days } = parseProgress('20260610: 15(again, 2 pointers),424(again?)');
  assert.deepEqual(days[0].items.map(i => i.id), [15, 424]);
  assert.equal(days[0].items[0].note, 'again, 2 pointers');
});

test('an annotation containing digits does not become a problem number', () => {
  const { days } = parseProgress('20260819: 70(ok*, o(1) space!!)');
  assert.deepEqual(days[0].items.map(i => i.id), [70]);
});

test('a nested paren does not truncate the annotation', () => {
  const { days } = parseProgress('20260819: 70(ok*, o(1) space!!)');
  assert.equal(days[0].items[0].note, 'ok*, o(1) space!!');
  assert.equal(days[0].items[0].emphasis, 2);
});

test('a paren left open across a newline keeps the entry together', () => {
  // The real file wraps mid-annotation: "...,152(\nok*),139(again*)".
  const { days } = parseProgress('20260819: 91(again),152(\nok*),139(again* 1d dp)');
  assert.deepEqual(days[0].items.map(i => i.id), [91, 152, 139]);
  assert.equal(days[0].items[1].status, 'ok');
});

test('a bare continuation line belongs to the day above it', () => {
  const { days } = parseProgress('20260610: 271,238(again)\n,53(again),62');
  assert.equal(days.length, 1);
  assert.deepEqual(days[0].items.map(i => i.id), [271, 238, 53, 62]);
});

test('a period between entries is treated as the comma it was meant to be', () => {
  // "39(again*).79(again*)" — without this, LC 79 vanishes into 39's note.
  const { days } = parseProgress('20260831: 39(again*).79(again*)');
  assert.deepEqual(days[0].items.map(i => i.id), [39, 79]);
});

test('separator lines end the current day rather than joining it', () => {
  const { days } = parseProgress('20260222: 1920\n\n------ review\n20260221: 62');
  assert.deepEqual(days.map(d => d.date), ['20260222', '20260221']);
  assert.deepEqual(days[0].items.map(i => i.id), [1920]);
});

test('named drills are skipped — they have no LeetCode number to schedule', () => {
  const { days } = parseProgress('20260613: 53,weekly_331,topo_sort,55');
  assert.deepEqual(days[0].items.map(i => i.id), [53, 55]);
});

test('a date that is not a calendar date is reported, not silently kept', () => {
  const { days, warnings } = parseProgress('20261399: 53');
  assert.equal(days.length, 0);
  assert.equal(warnings.length, 1);
  assert.match(warnings[0], /not a calendar date/);
});

test('a wrapped annotation still continues onto the next line', () => {
  // The real log does this at progress.txt:19-20 — the guard below must not
  // break it.
  const { days, warnings } = parseProgress('20260819: 322(again),152(\nok*),62(ok)');
  assert.equal(days.length, 1);
  assert.deepEqual(days[0].items.map(i => i.id), [322, 152, 62]);
  assert.equal(days[0].items[1].status, 'ok');
  assert.deepEqual(warnings, []);
});

test('an unclosed "(" does not swallow the next day, and is reported', () => {
  // A missing ")" is a plausible typo in a hand-written file. It used to hold
  // the annotation open, so every following date line was appended to the note
  // and those days disappeared from the schedule without a warning.
  const { days, warnings } = parseProgress('20260819: 152(ok*\n20260820: 62(ok),91(again)');
  assert.deepEqual(days.map(d => d.date), ['20260819', '20260820']);
  assert.deepEqual(days[0].items.map(i => i.id), [152]);
  assert.deepEqual(days[1].items.map(i => i.id), [62, 91]);
  assert.equal(warnings.length, 1);
  assert.match(warnings[0], /unclosed/);
});

// ── Status classification ───────────────────────────────────────────────────

test('classify reads the log\'s vocabulary', () => {
  assert.equal(classify('again').status, 'again');
  assert.equal(classify('again!!').status, 'again');
  assert.equal(classify('todo, dp').status, 'todo');
  assert.equal(classify('ok*').status, 'ok');
  assert.equal(classify('OK').status, 'ok');
  assert.equal(classify('').status, 'none');
  assert.equal(classify('to note').status, 'other');
});

test('"again" wins over "ok" when a note says both', () => {
  // "(ok, but again)" and "(ok* again)" are problems that still need a pass.
  assert.equal(classify('ok, but again').status, 'again');
  assert.equal(classify('ok* again').status, 'again');
});

test('emphasis counts the bangs, so again!!! sorts above again', () => {
  assert.equal(classify('again').emphasis, 0);
  assert.equal(classify('again!!!').emphasis, 3);
  assert.equal(classify('again!!, 3 get_dist').emphasis, 2);
});

// ── splitTopLevel ───────────────────────────────────────────────────────────

test('splitTopLevel respects paren depth', () => {
  assert.deepEqual(
    splitTopLevel('1(a, b),2(c)').map(s => s.trim()),
    ['1(a, b)', '2(c)']
  );
});

// ── Aggregation ─────────────────────────────────────────────────────────────

test('a problem practised twice in one day counts once, keeping the worse verdict', () => {
  // The log does this when a session is split by "|".
  const { days } = parseProgress('20260820: 494(ok) | 494(again!!)');
  const [problem] = aggregate(days);
  assert.deepEqual(problem.dates, ['20260820']);
  assert.equal(problem.status, 'again');
  assert.equal(problem.emphasis, 2);
});

test('againCount is how often a problem came back, not how often it was seen', () => {
  const { days } = parseProgress('20260101: 322(ok)\n20260102: 322(again)\n20260103: 322(again!!)');
  const [problem] = aggregate(days);
  assert.equal(problem.dates.length, 3);
  assert.equal(problem.againCount, 2);
  assert.equal(problem.status, 'again');
});

test('a date the log lists twice is one practice day, not two', () => {
  // The file is appended to in eras, and an era sometimes re-opens a date an
  // earlier one already had — 24 of them do.
  const { days } = parseProgress('20260101: 1,2\n\n------\n20260101: 3');
  assert.equal(days.length, 2, 'the parser sees them as written');
  const merged = mergeDays(days);
  assert.equal(merged.length, 1);
  assert.deepEqual(merged[0].items.map(i => i.id), [1, 2, 3]);
});

test('the real log has no duplicate dates left after merging', () => {
  const raw = fs.readFileSync(path.join(ROOT, 'data', 'progress.txt'), 'utf8');
  const { payload } = buildPayload(raw);
  const dates = payload.days.map(d => d.date);
  assert.equal(new Set(dates).size, dates.length);
  assert.equal(payload.stats.days, dates.length);
});

test('history is ordered oldest first, whatever order the log was written in', () => {
  const { days } = parseProgress('20260103: 1\n20260101: 1\n20260102: 1');
  const [problem] = aggregate(days);
  assert.deepEqual(problem.dates, ['20260101', '20260102', '20260103']);
});

// ── The real log ────────────────────────────────────────────────────────────
//
// The cases above pin the parser's rules; this pins it to the file it exists to
// read, so a future edit to progress.txt that the parser cannot handle fails
// here rather than quietly shrinking the review schedule.

test('the real data/progress.txt parses cleanly', () => {
  const raw = fs.readFileSync(path.join(ROOT, 'data', 'progress.txt'), 'utf8');
  const { payload, warnings } = buildPayload(raw);

  assert.deepEqual(warnings, [], 'progress.txt should parse with no warnings');
  assert.ok(payload.stats.days > 500, `only ${payload.stats.days} practice days parsed`);
  assert.ok(payload.stats.problems > 500, `only ${payload.stats.problems} problems parsed`);
  assert.ok(payload.stats.again > 0, 'no "again" annotations survived the parse');

  // Every day is a real date, in order, and holds at least one problem.
  let previous = '';
  for (const day of payload.days) {
    assert.match(day.date, /^\d{8}$/);
    assert.ok(day.date >= previous, `days out of order at ${day.date}`);
    assert.ok(day.items.length > 0, `${day.date} has no problems`);
    previous = day.date;
  }

  // LeetCode numbers, not fragments of an annotation that leaked through.
  for (const problem of payload.problems) {
    assert.ok(Number.isInteger(problem.id) && problem.id > 0 && problem.id < 10000,
      `implausible problem id ${problem.id}`);
    assert.ok(problem.dates.length > 0);
    assert.ok(problem.againCount <= problem.dates.length);
  }
});

test('the page reads the built log rather than a pasted-in copy', () => {
  const html = fs.readFileSync(path.join(ROOT, 'site', 'pages', 'lc-review-plan.html'), 'utf8');
  assert.ok(html.includes("fetch('./data/progress.json')"),
    'lc-review-plan.html should fetch the generated log');
  assert.ok(!html.includes('const RAW = `'),
    'lc-review-plan.html should not carry a hardcoded copy of the practice log');
});
