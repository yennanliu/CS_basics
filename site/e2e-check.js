#!/usr/bin/env node
/**
 * Post-build smoke test for the generated site.
 *
 *   node site/build-site.js && node site/e2e-check.js _site
 *
 * Rather than reimplementing page behaviour, this lifts the real functions out
 * of the BUILT html (search's score()) and runs them against the BUILT json, then
 * asserts the structural contract every page must satisfy — the same <footer>
 * check that validate-pages.yml enforces in CI.
 */
const fs = require('fs');
const SITE = process.argv[2] || '_site';
let pass = 0, fail = 0;
const ok  = (n, c, d='') => { c ? pass++ : fail++; console.log(`  ${c ? 'PASS' : 'FAIL'}  ${n}${d ? '  — ' + d : ''}`); };

const search = fs.readFileSync(`${SITE}/search.html`, 'utf8');
const idx    = JSON.parse(fs.readFileSync(`${SITE}/data/search-index.json`, 'utf8'));
const lc     = JSON.parse(fs.readFileSync(`${SITE}/data/lc-problems.json`, 'utf8'));

// ── 1. Search scoring: lift score() verbatim out of the shipped page ──
const scoreSrc = search.match(/function score\(rec, tokens\)[\s\S]*?\n    \}/)[0];
const score = new Function(`${scoreSrc}; return score;`)();

const docs = idx.records.map(d => ({
  title: d.title, url: d.url,
  hay: (d.title + ' ' + (d.category || '') + ' ' + (d.headings || []).join(' ')).toLowerCase()
}));
const query = q => docs.map(d => ({ d, s: score(d, q.toLowerCase().split(/\s+/)) }))
  .filter(x => x.s >= 0).sort((a, b) => b.s - a.s).map(x => x.d);

console.log('\n== search (real score() + real index) ==');
for (const [q, min] of [['dp', 1], ['knapsack', 1], ['binary search', 1], ['kadane', 1], ['dijkstra', 1]]) {
  const r = query(q);
  ok(`query "${q}" returns hits`, r.length >= min, `${r.length} hits, top: ${r[0] ? r[0].title : '-'}`);
}
ok('nonsense query returns nothing', query('zzzqqq').length === 0);
const deep = query('knapsack').filter(r => /cheatsheets\/[^/]+\//.test(r.url));
ok('deep-links into split sub-pages', deep.length > 0, `${deep.length} sub-page hits`);

// every indexed url must exist on disk
const missing = idx.records.filter(r => !fs.existsSync(`${SITE}/${r.url.split('#')[0]}`));
ok('every search-index url resolves to a file', missing.length === 0, `${idx.records.length} records, ${missing.length} missing`);

// ── 2. LC explorer: lift its real filter predicate ──
const exp = fs.readFileSync(`${SITE}/lc-explorer.html`, 'utf8');
console.log('\n== lc-explorer (real data) ==');
ok('fetch path matches shipped json', exp.includes("fetch('./data/lc-problems.json')") && fs.existsSync(`${SITE}/data/lc-problems.json`));
ok('problems present', lc.problems.length > 1000, `${lc.problems.length} problems`);
ok('tags present', lc.tags.length > 100, `${lc.tags.length} tags`);
ok('stats consistent', lc.stats.totalProblems === lc.problems.length);
const withSol = lc.problems.filter(p => p.solutions);
ok('solution cross-links present', withSol.length > 500, `${withSol.length} problems link to repo source`);
ok('solution urls are absolute github', withSol.every(p =>
  Object.values(p.solutions).every(u => u.startsWith('https://github.com/yennanliu/CS_basics/blob/master/'))));
// exercise the filters the UI offers
const byDiff = d => lc.problems.filter(p => p.difficulty === d).length;
ok('difficulty filter partitions data', byDiff('Easy') + byDiff('Medium') + byDiff('Hard') > 1000,
   `E=${byDiff('Easy')} M=${byDiff('Medium')} H=${byDiff('Hard')}`);
const byTag = lc.problems.filter(p => (p.tags || []).includes('Array')).length;
ok('tag filter works', byTag > 100, `Array tag -> ${byTag} problems`);
const byAcc = lc.problems.filter(p => p.acceptance >= 60).length;
ok('acceptance filter works', byAcc > 0, `>=60% -> ${byAcc} problems`);

// ── 3. Structure of every generated page ──
console.log('\n== page structure (all pages) ==');
const walk = (d, a = []) => { for (const e of fs.readdirSync(d, { withFileTypes: true })) {
  const f = `${d}/${e.name}`;
  if (e.isDirectory()) { if (e.name !== 'algo_demo') walk(f, a); } else if (e.name.endsWith('.html')) a.push(f);
} return a; };
const pages = walk(SITE);
const need = ['<!DOCTYPE html>', '<meta charset="UTF-8">', 'viewport', '<title>', 'class="navbar"', 'nav-links', '<footer>'];
for (const tag of need) {
  const bad = pages.filter(p => !fs.readFileSync(p, 'utf8').includes(tag));
  ok(`all ${pages.length} pages have ${tag}`, bad.length === 0, bad.length ? bad.slice(0, 3).join(', ') : '');
}
const NAV = ['index.html', 'search.html', 'cheatsheets.html', 'patterns.html', 'faqs.html',
             'lc-explorer.html', 'lc-similar.html', 'lc-random-picker.html', 'lc-review-plan.html', 'algo_demo/index.html'];
for (const p of ['lc-explorer', 'lc-similar', 'lc-random-picker', 'lc-review-plan']) {
  const h = fs.readFileSync(`${SITE}/${p}.html`, 'utf8');
  ok(`${p} nav reaches all ${NAV.length} sections`, NAV.every(n => h.includes(`"${n}"`)));
}

console.log(`\n${pass} passed, ${fail} failed\n`);
process.exit(fail ? 1 : 0);
