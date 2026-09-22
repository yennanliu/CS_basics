#!/usr/bin/env node
/**
 * data/progress.txt  ->  _site/data/progress.json
 *
 * The practice log is the source of truth for the spaced-repetition page. It
 * used to be pasted into site/pages/lc-review-plan.html as a `const RAW`
 * template literal, which meant the review schedule froze on the day someone
 * last remembered to re-paste it — the shipped copy stopped at 2026-05-02 while
 * the log ran four months further.
 *
 * The log is hand-written and its shape is loose, so the parser is deliberately
 * forgiving (see parseProgress). What it does NOT do is silently drop lines: a
 * line it cannot place is reported as a warning and counted, so a format drift
 * shows up in the build output instead of quietly shrinking the schedule.
 *
 * Status annotations — `139(again!!)`, `494(ok*)`, `34(todo)` — are the part the
 * old RAW threw away entirely. They are the whole signal for which problems are
 * not graduating, so they are normalised and carried through to the page.
 */
const fs = require('fs');
const path = require('path');
const { parseProblemIndex, PROBLEM_INDEX, GH_BLOB } = require('./build-roadmap');

const SOURCE = 'data/progress.txt';
const LISTS = 'data/problem_lists.json';
const OUT = '_site/data/progress.json';

// ── Status vocabulary ────────────────────────────────────────────────────────
//
// The log's annotations are free text written at speed: "(again)", "(again!!)",
// "(again, with univeral algo)", "(ok*, o(1) space!!)", "(dp again)". Rather
// than enumerate spellings, classify on the first keyword that appears — and
// check `again` before `ok`, because "(ok, but again)" and "(ok* again)" are
// both a problem that still needs another pass.
//
// `emphasis` counts the trailing bangs: `again!!!` is a problem that has fought
// back three times and deserves to sort above a bare `again`.
const STATUSES = ['again', 'todo', 'ok'];

function classify(note) {
  if (!note) return { status: 'none', emphasis: 0 };
  const text = note.toLowerCase();
  let status = 'other';
  for (const candidate of STATUSES) {
    if (text.includes(candidate)) { status = candidate; break; }
  }
  // Bangs anywhere in the note, not just at the end: "(again!!, 3 get_dist)".
  const bangs = (note.match(/!/g) || []).length;
  return { status, emphasis: bangs };
}

// ── Parser ───────────────────────────────────────────────────────────────────
//
// Accepted shapes, all present in the real log:
//
//   20260831: 997(todo), 907(again!!)      normal
//   20260701  1740(again),399              date and payload separated by spaces
//   20260819: 70(ok*, o(1) space!!),198(   a paren left open across a newline
//   ok*),139(again* 1d dp)                 …and closed on the next one
//   ,53(again),62                          a bare continuation of the day above
//   ------ review                          a separator, ignored
//   ...,topo_sort,weekly_331               non-numeric entries, ignored
//
// Splitting on commas has to respect paren depth, or "(again, 2 pointers)"
// becomes two entries and "2 pointers" turns into problem #2.
//
// A period separates too. It is a typo for a comma rather than a convention —
// "39(again*).79(again*)" — but it is the only period the log uses outside an
// annotation, and without this LC 79 disappears into LC 39's note.
function splitTopLevel(payload) {
  const parts = [];
  let buf = '';
  let depth = 0;
  for (const ch of payload) {
    if (ch === '(') depth++;
    else if (ch === ')') depth = Math.max(0, depth - 1);
    if ((ch === ',' || ch === '.') && depth === 0) { parts.push(buf); buf = ''; continue; }
    buf += ch;
  }
  parts.push(buf);
  return parts;
}

function depthAfter(text, depth) {
  for (const ch of text) {
    if (ch === '(') depth++;
    else if (ch === ')') depth = Math.max(0, depth - 1);
  }
  return depth;
}

function parseProgress(raw) {
  const days = [];
  const warnings = [];
  let current = null;
  // A payload is only complete once its parens balance. The log wraps
  // mid-annotation — "...,152(\nok*),139(again*)" — so a line ending inside an
  // annotation is held back and the next line is appended to it, rather than the
  // two being parsed separately and LC 152's verdict falling on the floor.
  let pending = null;

  const flush = () => {
    if (pending === null || !current) { pending = null; return; }
    // "a | b | c" groups a day into sessions; they carry no meaning here.
    for (const chunk of splitTopLevel(pending.replace(/\|/g, ','))) {
      const entry = chunk.trim();
      if (!entry) continue;
      // Greedy to the last ")", so a nested one survives: "70(ok*, o(1) space!!)"
      // is one annotation, not a note that stops at the first close paren.
      const m = entry.match(/^(\d+)\s*(?:\((.*)\))?/);
      // Named drills — topo_sort, weekly_331, lazy_bst_in_order — are practice
      // but not LeetCode numbers, so they cannot join a per-problem schedule.
      if (!m) continue;
      const { status, emphasis } = classify(m[2] || '');
      current.items.push({
        id: Number(m[1]),
        status,
        emphasis,
        note: (m[2] || '').trim() || null
      });
    }
    pending = null;
  };

  const lines = raw.split('\n');
  for (let i = 0; i < lines.length; i++) {
    const trimmed = lines[i].trim();
    const midAnnotation = pending !== null && depthAfter(pending, 0) > 0;

    if (!trimmed) { flush(); continue; }
    // Separator rules the log uses to break up eras.
    if (/^[-=]{2,}/.test(trimmed)) { flush(); current = null; continue; }

    const header = trimmed.match(/^(\d{8})\s*[:.]?\s*(.*)$/);
    // A dated line wins over a still-open annotation. It used to lose, so one
    // missing ")" swallowed every following day into the note until a blank line
    // flushed — silently, which is the opposite of the rule that a line the
    // parser cannot place gets reported. An LC number is at most four digits, so
    // an eight-digit line is a date, never the tail of a wrapped annotation.
    if (header && midAnnotation) {
      warnings.push(`line ${i + 1}: previous entry left "(" unclosed, closed here`);
    }
    if (header) {
      flush();
      const date = header[1];
      if (!/^\d{4}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$/.test(date)) {
        warnings.push(`line ${i + 1}: "${date}" is not a calendar date, skipped`);
        current = null;
        continue;
      }
      current = { date, items: [] };
      days.push(current);
      pending = header[2];
    } else if (current) {
      // A wrapped annotation continues the held-back line; a bare
      // ",53(again),62" is the same day carried onto a second line.
      pending = midAnnotation ? pending + trimmed : ((pending || '') + ',' + trimmed);
    } else {
      warnings.push(`line ${i + 1}: no date seen yet, skipped: ${trimmed.slice(0, 40)}`);
      continue;
    }

    // Hold the line only while an annotation is still open.
    if (depthAfter(pending, 0) === 0) flush();
  }
  flush();

  return { days: days.filter(d => d.items.length > 0), warnings };
}

// ── Aggregation ──────────────────────────────────────────────────────────────

// "again" outranks "todo" outranks "ok" when one day records a problem twice.
function rank(h) {
  return (h.status === 'again' ? 3 : h.status === 'todo' ? 2 : h.status === 'ok' ? 1 : 0) * 10 + h.emphasis;
}

// One entry per problem, newest last, so the page can compute an interval from
// the repetition count without re-walking every day.
function aggregate(days) {
  const byProblem = new Map();
  for (const day of days) {
    for (const item of day.items) {
      if (!byProblem.has(item.id)) byProblem.set(item.id, []);
      byProblem.get(item.id).push({ date: day.date, status: item.status, emphasis: item.emphasis });
    }
  }

  return [...byProblem.entries()]
    .map(([id, history]) => {
      // Deduplicate a problem listed twice on the same day (the log does this
      // when a session is split by "|"), keeping the strongest signal.
      const seen = new Map();
      for (const h of history) {
        const prev = seen.get(h.date);
        if (!prev || rank(h) > rank(prev)) seen.set(h.date, h);
      }
      const dedup = [...seen.values()].sort((a, b) => a.date.localeCompare(b.date));
      const latest = dedup[dedup.length - 1];
      return {
        id,
        dates: dedup.map(h => h.date),
        statuses: dedup.map(h => h.status),
        status: latest.status,
        emphasis: Math.max(...dedup.map(h => h.emphasis)),
        // How many times it has come back marked "again" — the count that says a
        // problem is not graduating, however many times it has been attempted.
        againCount: dedup.filter(h => h.status === 'again').length
      };
    })
    .sort((a, b) => a.id - b.id);
}

// The log lists 24 dates twice — the file is appended to in eras, and an era
// sometimes re-opens a date the previous one already had. They are one day's
// practice, so they are merged into one entry; leaving them separate made the
// page report 826 "practice days" over 801 distinct dates.
function mergeDays(days) {
  const byDate = new Map();
  for (const day of days) {
    const existing = byDate.get(day.date);
    if (existing) existing.items.push(...day.items);
    else byDate.set(day.date, { date: day.date, items: [...day.items] });
  }
  return [...byDate.values()].sort((a, b) => a.date.localeCompare(b.date));
}

function buildPayload(raw, catalog) {
  const parsed = parseProgress(raw);
  const warnings = parsed.warnings;
  const days = mergeDays(parsed.days);
  const problems = attachCatalog(aggregate(days), catalog);
  return {
    payload: {
      generated: new Date().toISOString().slice(0, 10),
      source: SOURCE,
      repo: GH_BLOB,
      days,
      problems,
      // The whole indexed universe per topic, so the page can say which topic is
      // getting less practice than its weight deserves — a claim about the
      // topic, which the practised rows alone cannot support.
      sections: catalog ? catalog.sections : [],
      stats: {
        days: days.length,
        problems: problems.length,
        attempts: days.reduce((n, d) => n + d.items.length, 0),
        again: problems.filter(p => p.againCount > 0).length,
        titled: problems.filter(p => p.title).length,
        firstDate: days.length ? days[0].date : null,
        lastDate: days.length ? days[days.length - 1].date : null
      }
    },
    warnings
  };
}

// ── Catalog ──────────────────────────────────────────────────────────────────
//
// The log records a bare LeetCode number and nothing else, which is all a
// schedule needs and nowhere near enough to act on: "#1094 is 12 days overdue"
// tells you neither what the problem is, which pattern it drills, nor whether
// it is worth the slot. Every one of those facts is already in the repo — the
// indexed row (title, difficulty, section, MUST, the solutions committed here)
// and data/problem_lists.json (Blind 75 / NeetCode / Top 100 Liked) — so the
// page is handed them rather than turning a number into a leetcode.com search.
//
// The weights are `script/suggest_review.py`'s, so the page's balance table and
// the CLI planner answer the same question the same way. The two signals that
// script reads and this one cannot are the non-Google company tags and git
// history; neither changes which section is starving, which is what the table
// is for.
const W = {
  must: 5.0,
  top100liked: 2.5,
  blind75: 2.5,
  neetcode150: 1.5,
  neetcode250: 0.7,
  google: 1.5,
  difficulty: { Medium: 0.5, Hard: 0.3, Easy: 0.0 }
};

// Why a problem is worth a pass at all, in points. The NeetCode lists nest, so
// only the narrowest one scores — a Blind 75 problem is on all four.
function importance(meta) {
  let score = 0;
  const on = new Set(meta.lists || []);
  if (meta.must) score += W.must;
  if (on.has('top100liked')) score += W.top100liked;
  if (on.has('blind75')) score += W.blind75;
  else if (on.has('neetcode150')) score += W.neetcode150;
  else if (on.has('neetcode250')) score += W.neetcode250;
  if (meta.google) score += W.google;
  score += W.difficulty[meta.difficulty] || 0;
  return Math.round(score * 100) / 100;
}

// A problem the log names but the index does not carry still has to be schedulable
// and still has to land somewhere on the balance table, so it gets a section of
// its own rather than being dropped or silently folded into a real one.
const UNFILED = 'Unfiled';

/**
 * Everything the page needs about a problem that the practice log cannot know.
 * Returns { byId, sections } — sections carries the importance of the WHOLE
 * indexed universe per topic, not only the problems that have been practised,
 * because "this topic is starving" is a claim about the topic, not about the
 * rows that happen to be in the log.
 */
function buildCatalog(indexMarkdown, listsJson) {
  const byId = new Map();
  const listed = new Map();
  for (const entry of (listsJson && listsJson.problems) || []) {
    listed.set(String(parseInt(entry.id, 10)), entry);
  }

  const sections = new Map();
  const bump = (name, imp) => {
    const row = sections.get(name) || { name, n: 0, importance: 0 };
    row.n += 1;
    row.importance = Math.round((row.importance + imp) * 100) / 100;
    sections.set(name, row);
  };

  for (const [id, p] of parseProblemIndex(indexMarkdown)) {
    const extra = listed.get(id);
    const meta = {
      title: p.title,
      difficulty: p.difficulty === 'Unknown' ? null : p.difficulty,
      section: p.section || UNFILED,
      slug: extra ? extra.slug : slugFromUrl(p.url),
      must: p.must || undefined,
      google: p.google || undefined,
      lists: extra && extra.lists && extra.lists.length ? extra.lists : undefined,
      solutions: relativeSolutions(p.solutions)
    };
    meta.importance = importance(meta);
    byId.set(id, meta);
    bump(meta.section, meta.importance);
  }

  // A curated-list problem the index has never carried is still a real problem
  // with a real title, and the log does contain a few — so it goes in `byId`
  // and its row on the page reads properly.
  //
  // It does NOT get a section weight. There are 363 of these, and counting them
  // made `Unfiled` the third-largest topic on the balance table: a topic the
  // repo has never claimed, permanently owed practice it was never going to
  // get. The table compares the topics the index organises; a logged problem with
  // no indexed row still gets its own `Unfiled` line, added by the page, at zero
  // weight.
  for (const [id, entry] of listed) {
    if (byId.has(id)) continue;
    const meta = {
      title: entry.title,
      difficulty: entry.difficulty || null,
      section: UNFILED,
      slug: entry.slug,
      lists: entry.lists && entry.lists.length ? entry.lists : undefined
    };
    meta.importance = importance(meta);
    byId.set(id, meta);
  }

  return {
    byId,
    sections: [...sections.values()].sort((a, b) => b.importance - a.importance)
  };
}

// Solution links are shipped relative to the repo root and rebuilt against
// `repo` on the page: the absolute form repeated the same 52-character GitHub
// prefix up to three times for each of 796 problems, which is a fifth of the
// file for no information.
function relativeSolutions(solutions) {
  const out = {};
  for (const [lang, href] of Object.entries(solutions || {})) {
    out[lang] = href.startsWith(GH_BLOB + '/') ? href.slice(GH_BLOB.length + 1) : href;
  }
  return Object.keys(out).length ? out : undefined;
}

// The index links to the canonical problem page, so the slug is already there for
// every row that has one — deriving it from the title would guess wrong exactly
// where guessing is expensive (LC 4038's title and its slug disagree).
function slugFromUrl(url) {
  const m = /leetcode\.com\/problems\/([^/?#]+)/.exec(url || '');
  return m ? m[1] : null;
}

// Fold the catalog into the aggregated problems. A problem with no entry keeps
// exactly the shape it had before, so the page's schedule never depends on it.
function attachCatalog(problems, catalog) {
  if (!catalog) return problems;
  for (const problem of problems) {
    const meta = catalog.byId.get(String(problem.id));
    if (!meta) { problem.section = UNFILED; problem.importance = 0; continue; }
    Object.assign(problem, meta);
  }
  return problems;
}

// `root` is the repo root. The build runs from there like every other script
// here, so it defaults to the working directory; the tests run from site/ and
// pass it explicitly rather than chdir-ing underneath the rest of the suite.
function loadCatalog(root) {
  const at = name => (root ? path.join(root, name) : name);
  if (!fs.existsSync(at(PROBLEM_INDEX))) return null;
  const lists = fs.existsSync(at(LISTS)) ? JSON.parse(fs.readFileSync(at(LISTS), 'utf8')) : null;
  return buildCatalog(fs.readFileSync(at(PROBLEM_INDEX), 'utf8'), lists);
}

// ── Build ────────────────────────────────────────────────────────────────────

function main() {
if (!fs.existsSync(SOURCE)) {
  console.error(`${SOURCE} is missing — the review plan has no data to build from.`);
  process.exit(1);
}

const catalog = loadCatalog();
const { payload, warnings } = buildPayload(fs.readFileSync(SOURCE, 'utf8'), catalog);

if (payload.stats.days === 0) {
  console.error(`${SOURCE} produced no practice days — refusing to ship an empty review plan.`);
  process.exit(1);
}

fs.mkdirSync('_site/data', { recursive: true });
fs.writeFileSync(OUT, JSON.stringify(payload));

console.log(`✓ Created ${OUT} (${payload.stats.days} practice days, ` +
  `${payload.stats.problems} problems, ${payload.stats.again} still marked "again")`);
console.log(`    ${payload.stats.firstDate} → ${payload.stats.lastDate}`);
console.log(`    ${payload.stats.titled} of ${payload.stats.problems} carry a title, ` +
  `difficulty and topic from ${PROBLEM_INDEX}; ${payload.sections.length} topics weighted`);
for (const w of warnings) console.warn(`    warning: ${w}`);
}

if (require.main === module) main();

module.exports = {
  parseProgress, classify, splitTopLevel, aggregate, mergeDays, buildPayload,
  importance, buildCatalog, attachCatalog, loadCatalog, slugFromUrl,
  relativeSolutions, UNFILED, W
};
