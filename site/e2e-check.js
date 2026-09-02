#!/usr/bin/env node
/**
 * Post-build smoke test for the generated site.
 *
 *   bash site/build.sh && node site/e2e-check.js _site
 *
 * The point of this file is coverage of *every* page. validate-pages.yml checks
 * a hardcoded list of six root pages, which is why a whole class of breakage
 * stayed invisible for months: ~120 dead `.md` links across 31 cheatsheets, five
 * tool pages with no footer, and a 404 page with no navbar and an absolute
 * stylesheet href. None of those pages were on the list.
 *
 * So the rules here are stated once and applied to the whole tree, and a
 * violation is an error rather than a warning — a broken link that only prints a
 * warning is a broken link that ships.
 *
 * Where it can, it tests the shipped artefact rather than a reimplementation of
 * it: search's score() is lifted verbatim out of the built search.html and run
 * against the built index, so a scoring change that breaks queries fails here
 * even though this file never duplicates the scoring rules.
 */
const fs = require('fs');
const path = require('path');

const SITE = process.argv[2] || '_site';
let pass = 0, fail = 0;

const ok = (name, cond, detail = '') => {
  cond ? pass++ : fail++;
  console.log(`  ${cond ? 'PASS' : 'FAIL'}  ${name}${detail ? '  — ' + detail : ''}`);
};

const read = f => fs.readFileSync(f, 'utf8');
const json = f => JSON.parse(read(f));

function walk(dir, acc = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) walk(full, acc);
    else if (entry.name.endsWith('.html')) acc.push(full);
  }
  return acc;
}

const pages = walk(SITE);
const rel = p => path.relative(SITE, p);

// ── 0. The build produced everything it is supposed to ───────────────────────
console.log('\n== required artefacts ==');
const REQUIRED = [
  'index.html', 'problems.html', 'resources.html', 'cheatsheets.html', 'faqs.html',
  'patterns.html', 'search.html', 'lc-roadmap.html', 'skills.html', '404.html',
  'style.css', 'nav.css', 'nav.js', 'lc-page.css', 'site.js', 'roadmap.js', 'complexity.js',
  'vendor/d3.min.js', 'vendor/highlight/atom-one-dark.min.css',
  'data/roadmap.json', 'data/complexity-quiz.json', 'data/lc-problems.json',
  'data/search-index.json', 'data/progress.json'
];
const missingFiles = REQUIRED.filter(f => {
  const p = path.join(SITE, f);
  return !fs.existsSync(p) || fs.statSync(p).size === 0;
});
ok('every required file is present and non-empty', missingFiles.length === 0, missingFiles.join(', '));

for (const [dir, min] of [['cheatsheets', 100], ['faqs', 20], ['algo_demo', 20], ['doc/pic', 50]]) {
  const full = path.join(SITE, dir);
  const count = fs.existsSync(full) ? fs.readdirSync(full).length : 0;
  ok(`${dir}/ is populated`, count >= min, `${count} entries`);
}

// ── 1. Every page carries the shared chrome ──────────────────────────────────
console.log('\n== page structure (all pages) ==');
ok('pages were generated', pages.length > 100, `${pages.length} pages`);

const REQUIRED_MARKUP = [
  ['<!DOCTYPE html>', h => h.includes('<!DOCTYPE html>')],
  ['<meta charset="UTF-8">', h => h.includes('<meta charset="UTF-8">')],
  ['viewport meta', h => h.includes('name="viewport"')],
  ['<title>', h => /<title>[^<]+<\/title>/.test(h)],
  ['navbar mount', h => h.includes('id="site-nav"') && h.includes('CSNav.mount()')],
  ['nav.js', h => /<script src="[^"]*nav\.js"><\/script>/.test(h)],
  ['nav.css', h => /<link rel="stylesheet" href="[^"]*nav\.css">/.test(h)],
  ['<footer>', h => h.includes('<footer')]
];

const sources = new Map(pages.map(p => [p, read(p)]));

for (const [label, test] of REQUIRED_MARKUP) {
  const bad = pages.filter(p => !test(sources.get(p)));
  ok(`all ${pages.length} pages have ${label}`, bad.length === 0,
     bad.length ? `${bad.length} missing, e.g. ${bad.slice(0, 3).map(rel).join(', ')}` : '');
}

// A page title of "undefined" or a bare " — CS_basics" means the title fell
// through every fallback the builder has.
const badTitles = pages.filter(p => /<title>\s*(undefined|—|-)?\s*(CS_basics)?\s*<\/title>/.test(sources.get(p)));
ok('no empty or undefined titles', badTitles.length === 0, badTitles.slice(0, 3).map(rel).join(', '));

// ── 2. Links resolve ─────────────────────────────────────────────────────────
console.log('\n== internal links (all pages) ==');

// Hrefs inside a page's own <script> are template strings that are only real
// once the page runs, so they are not files on disk and cannot be checked here.
function stripScripts(html) {
  return html.replace(/<script\b[^>]*>[\s\S]*?<\/script>/gi, '');
}

const brokenLinks = [];
const absoluteLinks = [];
for (const p of pages) {
  const html = stripScripts(sources.get(p));
  for (const m of html.matchAll(/href="([^"]+)"/g)) {
    const href = m[1];
    if (/^([a-z][a-z0-9+.-]*:|\/\/|#)/i.test(href)) continue;
    // A root-relative href breaks the moment the site is served from a project
    // subpath, which is exactly how GitHub Pages serves this one.
    if (href.startsWith('/')) { absoluteLinks.push(`${rel(p)} → ${href}`); continue; }
    const target = path.resolve(path.dirname(p), decodeURIComponent(href.split('#')[0]));
    if (!fs.existsSync(target)) brokenLinks.push(`${rel(p)} → ${href}`);
  }
}
ok('no broken internal links', brokenLinks.length === 0,
   brokenLinks.length ? `${brokenLinks.length}, e.g. ${brokenLinks.slice(0, 4).join(' | ')}` : '');
ok('no root-relative links', absoluteLinks.length === 0,
   absoluteLinks.length ? `${absoluteLinks.length}, e.g. ${absoluteLinks.slice(0, 4).join(' | ')}` : '');

// A `.md` href that survived the build is a link into the source tree that the
// reader cannot follow — the failure mode this check was written for.
const mdLinks = [];
for (const p of pages) {
  for (const m of stripScripts(sources.get(p)).matchAll(/href="([^"]*\.md(?:#[^"]*)?)"/g)) {
    if (!/^https?:/i.test(m[1])) mdLinks.push(`${rel(p)} → ${m[1]}`);
  }
}
ok('no unresolved .md links', mdLinks.length === 0,
   mdLinks.length ? `${mdLinks.length}, e.g. ${mdLinks.slice(0, 4).join(' | ')}` : '');

console.log('\n== images (all pages) ==');
const brokenImages = [];
const eagerImages = [];
for (const p of pages) {
  const html = stripScripts(sources.get(p));
  for (const m of html.matchAll(/<img\b[^>]*>/gi)) {
    const tag = m[0];
    const src = (tag.match(/src\s*=\s*"([^"]+)"/) || [])[1];
    if (!src || /^(https?:|data:|\/\/)/i.test(src)) continue;
    if (!fs.existsSync(path.resolve(path.dirname(p), decodeURIComponent(src)))) {
      brokenImages.push(`${rel(p)} → ${src}`);
    }
    // Doc pages carry multi-megabyte diagrams far below the fold; without this
    // the browser fetches every one of them before first paint.
    if (!/loading\s*=\s*"lazy"/i.test(tag)) eagerImages.push(`${rel(p)} → ${src}`);
  }
}
ok('no broken local images', brokenImages.length === 0,
   brokenImages.length ? `${brokenImages.length}: ${brokenImages.slice(0, 4).join(' | ')}` : '');

// A github.com/.../blob/ URL serves an HTML page, not an image, so one that
// survived the build renders as a broken image on every page carrying it.
const unconverted = [];
for (const p of pages) {
  const html = stripScripts(sources.get(p));
  for (const m of html.matchAll(/<img\b[^>]*src\s*=\s*"(https:\/\/github\.com\/[^"]*\/blob\/[^"]*)"/gi)) {
    unconverted.push(`${rel(p)} → ${m[1]}`);
  }
  for (const m of html.matchAll(/<img\b[^>]*src\s*=\s*"((?:\.\.\/)*pic\/[^"]*)"/gi)) {
    unconverted.push(`${rel(p)} → ${m[1]}`);
  }
}
ok('no unconverted image paths', unconverted.length === 0,
   unconverted.length ? `${unconverted.length}, e.g. ${unconverted.slice(0, 3).join(' | ')}` : '');
ok('local images are lazy-loaded', eagerImages.length === 0,
   eagerImages.length ? `${eagerImages.length} eager, e.g. ${eagerImages.slice(0, 3).join(' | ')}` : '');

// ── 3. Wide content cannot escape the page ───────────────────────────────────
// style.css hides body overflow-x, so an unwrapped table wider than the viewport
// is not merely ugly on a phone — its right-hand columns are unreachable.
console.log('\n== responsive containment ==');
// Only the markdown-generated pages: the hand-written tools lay out their own
// tables inside their own scroll containers (.all-table-wrap and friends), which
// this cannot see and should not second-guess.
const GENERATED = /^(index|patterns|resources|faqs|cheatsheets(\.zh)?)\.html$|^(cheatsheets|faqs)\//;
const unwrappedTables = [];
for (const p of pages.filter(p => GENERATED.test(rel(p)))) {
  const html = stripScripts(sources.get(p));
  const tables = (html.match(/<table/g) || []).length;
  const wrapped = (html.match(/<div class="table-wrap">\s*<table/g) || []).length;
  if (tables > wrapped) unwrappedTables.push(`${rel(p)} (${tables - wrapped} of ${tables})`);
}
ok('every generated table is scroll-wrapped', unwrappedTables.length === 0,
   unwrappedTables.length ? `${unwrappedTables.length} pages, e.g. ${unwrappedTables.slice(0, 3).join(', ')}` : '');

// ── 4. Search: the real score() against the real index ───────────────────────
console.log('\n== search (shipped score() + shipped index) ==');
const searchHtml = read(`${SITE}/search.html`);
const index = json(`${SITE}/data/search-index.json`);
const lc = json(`${SITE}/data/lc-problems.json`);

const scoreSrc = (searchHtml.match(/function score\(rec, tokens\)[\s\S]*?\n    \}/) || [])[0];
ok('score() lifted out of the shipped page', Boolean(scoreSrc));

if (scoreSrc) {
  const score = new Function(`${scoreSrc}; return score;`)();
  const docs = index.records.map(d => ({
    title: d.title, url: d.url,
    hay: (d.title + ' ' + (d.category || '') + ' ' + (d.summary || '') + ' ' + (d.headings || []).join(' ')).toLowerCase()
  }));
  const query = q => docs
    .map(d => ({ d, s: score(d, q.toLowerCase().split(/\s+/)) }))
    .filter(x => x.s >= 0)
    .sort((a, b) => b.s - a.s)
    .map(x => x.d);

  for (const q of ['dp', 'knapsack', 'binary search', 'kadane', 'dijkstra', 'monotonic stack']) {
    const hits = query(q);
    ok(`query "${q}" returns hits`, hits.length > 0, hits.length ? `${hits.length}, top: ${hits[0].title}` : '');
  }
  ok('nonsense query returns nothing', query('zzzqqq').length === 0);
}

const missingIndexed = index.records.filter(r => !fs.existsSync(path.join(SITE, r.url.split('#')[0])));
ok('every search-index url resolves', missingIndexed.length === 0,
   `${index.records.length} records, ${missingIndexed.length} missing`);

// ── 5. LC explorer data ──────────────────────────────────────────────────────
console.log('\n== lc data ==');
ok('problems present', lc.problems.length > 1000, `${lc.problems.length} problems`);
ok('tags present', lc.tags.length > 100, `${lc.tags.length} tags`);
ok('stats consistent', lc.stats.totalProblems === lc.problems.length);
const withSolutions = lc.problems.filter(p => p.solutions);
ok('solution cross-links present', withSolutions.length > 500, `${withSolutions.length} link to repo source`);
ok('solution urls are absolute github', withSolutions.every(p =>
  Object.values(p.solutions).every(u => u.startsWith('https://github.com/yennanliu/CS_basics/blob/master/'))));

// ── 6. Review plan is built from the log, not pasted into the page ───────────
console.log('\n== review plan ==');
const progress = json(`${SITE}/data/progress.json`);
const reviewHtml = read(`${SITE}/lc-review-plan.html`);
ok('page fetches the generated log', reviewHtml.includes("fetch('./data/progress.json')"));
ok('page has no pasted-in practice data', !reviewHtml.includes('const RAW = `'));
ok('log has practice days', progress.stats.days > 100, `${progress.stats.days} days`);
ok('log carries status annotations', progress.stats.again > 0,
   `${progress.stats.again} problems marked "again"`);
// The log's own recency is *not* asserted. A gap in practice is the author's
// business, and failing a deploy over it would be the tool telling the user off.
// What made the old page stale was the copy in the HTML, and that is what the
// two checks above rule out: the data cannot lag the log any more, because there
// is only one copy of it.
const lastLogged = progress.stats.lastDate;
const ageDays = Math.round(
  (Date.now() - new Date(`${lastLogged.slice(0, 4)}-${lastLogged.slice(4, 6)}-${lastLogged.slice(6, 8)}`)) / 86400000
);
console.log(`  INFO  newest log entry ${lastLogged} (${ageDays}d ago), ` +
  `${progress.stats.attempts} attempts over ${progress.stats.days} days`);

// ── 7. Discoverability ───────────────────────────────────────────────────────
console.log('\n== seo / metadata ==');
for (const f of ['sitemap.xml', 'robots.txt', '404.html']) {
  ok(`${f} exists`, fs.existsSync(path.join(SITE, f)));
}
const sitemap = fs.existsSync(`${SITE}/sitemap.xml`) ? read(`${SITE}/sitemap.xml`) : '';
const sitemapUrls = [...sitemap.matchAll(/<loc>([^<]+)<\/loc>/g)].map(m => m[1]);
ok('sitemap lists the site', sitemapUrls.length > 100, `${sitemapUrls.length} urls`);

const descriptions = new Set();
for (const p of pages) {
  const m = sources.get(p).match(/<meta name="description" content="([^"]*)"/);
  if (m) descriptions.add(m[1]);
}
// One shared description across 300+ pages tells a search engine nothing about
// any of them.
ok('descriptions are per-page', descriptions.size > pages.length / 2,
   `${descriptions.size} distinct across ${pages.length} pages`);

const missingCanonical = pages.filter(p => !sources.get(p).includes('rel="canonical"'));
ok('every page declares a canonical url', missingCanonical.length === 0,
   missingCanonical.length ? `${missingCanonical.length} missing` : '');

// The zh sheets are translations, not duplicates; without hreflang a crawler
// has to guess that.
const zhPages = pages.filter(p => p.endsWith('.zh.html'));
const missingHreflang = zhPages.filter(p => !sources.get(p).includes('hreflang'));
ok('translated pages declare hreflang', zhPages.length > 0 && missingHreflang.length === 0,
   `${zhPages.length} zh pages, ${missingHreflang.length} missing`);

// ── 8. Third-party code ──────────────────────────────────────────────────────
console.log('\n== external resources ==');
const external = [];
for (const p of pages) {
  for (const m of sources.get(p).matchAll(/<script[^>]+src="(https?:\/\/[^"]+)"/g)) {
    external.push(`${rel(p)} → ${m[1]}`);
  }
}
// Everything else the site needs is vendored, deliberately: the build never
// touches the network and neither should a reader's browser.
ok('no external script tags', external.length === 0, external.join(' | '));

// ── 9. Navigation reaches every tool ─────────────────────────────────────────
console.log('\n== navigation ==');
const CSNav = require(path.resolve(__dirname, 'nav.js'));
const entries = [...CSNav.PRIMARY, ...CSNav.MORE];
ok('nav declares entries', entries.length > 0, `${entries.length} entries`);
const deadNav = entries.filter(e => !e.external && !fs.existsSync(path.join(SITE, e.href)));
ok('every nav entry resolves to a page', deadNav.length === 0, deadNav.map(e => e.id).join(', '));

// The skip link ships inside the navbar markup rather than in each page's HTML,
// so it is asserted where it is defined — one check that covers all four page
// families, instead of 352 identical ones that could not see it anyway.
const navMarkup = CSNav.navHTML({ currentPage: 'home' });
ok('navbar renders', navMarkup.includes('class="navbar"') && navMarkup.includes('nav-links'));
ok('navbar leads with a skip link',
   navMarkup.startsWith('<a class="skip-link" href="#main">'));
ok('nav.css styles the skip link', read(`${SITE}/nav.css`).includes('.skip-link'));
ok('nav.css honours prefers-reduced-motion',
   read(`${SITE}/nav.css`).includes('prefers-reduced-motion'));

// One shared palette for the hand-written tool pages, rather than five copies
// that had already drifted apart.
console.log('\n== tool pages ==');
const TOOL_PAGES = ['lc-explorer', 'lc-similar', 'lc-review-plan', 'lc-random-picker', 'lc-complexity-quiz', 'skills'];
for (const name of TOOL_PAGES) {
  const html = sources.get(path.join(SITE, `${name}.html`));
  ok(`${name} uses the shared palette`,
     Boolean(html) && html.includes('lc-page.css') && !/:root\s*\{[^}]*--bg\s*:/.test(html));
}

console.log(`\n${pass} passed, ${fail} failed\n`);
process.exit(fail ? 1 : 0);
