#!/usr/bin/env node
/**
 * The last step of site/build.sh: give every page in _site/ the head metadata it
 * needs, then index the whole tree.
 *
 * Runs after everything else has been written and copied, which is the point.
 * The site is built by four generators plus a plain `cp` of the hand-written
 * pages, so no single one of them can see the finished tree — the sitemap
 * assembled inside build-site.js would list the 300 pages it wrote and miss the
 * 43 visualizers and 6 tools that arrive later. Walking _site/ at the end cannot
 * miss a page, because a page that exists is a page it finds.
 *
 * Generated pages already carry a real per-page description, canonical URL and
 * hreflang pair from htmlTemplate; those are left exactly as they are. This only
 * fills gaps, so the hand-written pages get the same treatment without anyone
 * having to remember to paste eight meta tags into the next one.
 */
const fs = require('fs');
const path = require('path');

const { SITE_ORIGIN: ORIGIN, escAttr, unescAttr, trimDescription } = require('./build-lib');

const SITE = process.argv[2] || '_site';

// Pages that exist for the browser, not for a reader arriving from a search
// result. 404 is served for every unmatched path, so indexing it would put a
// dead end in the results for every query it happened to match.
const NOINDEX = new Set(['404.html']);

function walk(dir, acc = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const full = path.join(dir, entry.name);
    if (entry.isDirectory()) walk(full, acc);
    else if (entry.name.endsWith('.html')) acc.push(full);
  }
  return acc;
}

// "Dijkstra - Algorithm Visualizer" and "Heap — CS_basics" both want to become
// "Dijkstra" / "Heap" for og:title — the site name is already in og:site_name.
function pageTitle(html) {
  const m = html.match(/<title>([\s\S]*?)<\/title>/);
  if (!m) return 'CS_basics';
  return m[1].replace(/\s*[—-]\s*(CS[_ ]basics|Algorithm Visualizer|CS Basics)\s*$/i, '').trim() || 'CS_basics';
}

function existingDescription(html) {
  const m = html.match(/<meta name="description" content="([^"]*)"/);
  return m ? m[1] : null;
}

// A visualizer page's own lead paragraph beats anything this file could invent.
function leadParagraph(html) {
  const body = html.replace(/<script\b[^>]*>[\s\S]*?<\/script>/gi, '');
  for (const m of body.matchAll(/<p(?:\s[^>]*)?>([\s\S]*?)<\/p>/gi)) {
    const text = m[1].replace(/<[^>]+>/g, '').replace(/\s+/g, ' ').trim();
    if (text.length >= 40) return trimDescription(text);
  }
  return null;
}

// data/release.json is written by .github/workflows/release.yml when a release
// is cut, so the footer of every page names the release the site was built from.
// Stamped here rather than in each generator because the footer is written in
// three places (htmlTemplate, the hand-written pages, the visualizers) and only
// this pass sees all of them. No file — a fork, an old checkout — means no stamp.
const RELEASE_FILE = path.join(__dirname, '..', 'data', 'release.json');
const release = fs.existsSync(RELEASE_FILE) ? JSON.parse(fs.readFileSync(RELEASE_FILE, 'utf8')) : null;
const stamp = release && release.version
  ? `<p class="site-version">release <a href="https://github.com/yennanliu/CS_basics/releases/tag/${encodeURIComponent(release.version)}">${escAttr(release.version)}</a>` +
    (release.date ? ` · ${escAttr(release.date)}` : '') + '</p>'
  : null;

// ── Head metadata ────────────────────────────────────────────────────────────
//
// Fills the gaps on a page with no canonical URL (the copied pages); returns the
// page unchanged when it already has one, or null when it has no <title> to
// anchor the tags after.
function withHeadMetadata(html, url) {
  if (html.includes('rel="canonical"')) return html;

  const title = unescAttr(pageTitle(html));
  const existing = existingDescription(html);
  const description = unescAttr(existing || leadParagraph(html)) ||
    `${title} — an interactive walkthrough from the CS_basics algorithm visualizer.`;

  const tags = [
    `  <link rel="canonical" href="${ORIGIN}${url}">`,
    existing ? null : `  <meta name="description" content="${escAttr(description)}">`,
    NOINDEX.has(url) ? '  <meta name="robots" content="noindex">' : null,
    `  <meta property="og:type" content="article">`,
    `  <meta property="og:site_name" content="CS_basics">`,
    `  <meta property="og:title" content="${escAttr(title)}">`,
    `  <meta property="og:description" content="${escAttr(description)}">`,
    `  <meta property="og:url" content="${ORIGIN}${url}">`,
    `  <meta name="twitter:card" content="summary">`,
    `  <meta name="twitter:title" content="${escAttr(title)}">`,
    `  <meta name="twitter:description" content="${escAttr(description)}">`
  ].filter(Boolean).join('\n');

  // After </title> so the tags land inside <head> whatever else the page has.
  if (!/<\/title>/.test(html)) return null;
  return html.replace('</title>', `</title>\n${tags}`);
}

// ── Release stamp ────────────────────────────────────────────────────────────
function withReleaseStamp(html) {
  if (!stamp || !html.includes('</footer>') || html.includes('class="site-version"')) return html;
  // Inside the footer's .container when it has one, so it lines up with the rest.
  return /<\/div>\s*<\/footer>/.test(html)
    ? html.replace(/(\s*)<\/div>(\s*)<\/footer>/, `$1  ${stamp}$1</div>$2</footer>`)
    : html.replace('</footer>', `${stamp}</footer>`);
}

// One pass: each page is read once and written at most once.
const pages = walk(SITE).sort();
let patched = 0;
let stamped = 0;

for (const file of pages) {
  const url = path.relative(SITE, file).split(path.sep).join('/');
  const original = fs.readFileSync(file, 'utf8');
  let html = withHeadMetadata(original, url);
  if (html === null) {
    console.warn(`    warning: ${url} has no <title>, skipped`);
    html = original;
  } else if (html !== original) {
    patched++;
  }
  const withStamp = withReleaseStamp(html);
  if (withStamp !== html) stamped++;
  if (withStamp !== original) fs.writeFileSync(file, withStamp);
}

console.log(`✓ Head metadata: ${patched} page(s) filled in, ${pages.length - patched} already complete`);

if (stamp) {
  fs.mkdirSync(path.join(SITE, 'data'), { recursive: true });
  fs.writeFileSync(path.join(SITE, 'data', 'release.json'), JSON.stringify(release, null, 2) + '\n');
  console.log(`✓ Release stamp: ${release.version} on ${stamped} page footer(s)`);
} else {
  console.log('→ No data/release.json, footers left unstamped');
}

// ── Sitemap ──────────────────────────────────────────────────────────────────

const indexable = pages
  .map(f => path.relative(SITE, f).split(path.sep).join('/'))
  .filter(url => !NOINDEX.has(url));

const today = new Date().toISOString().slice(0, 10);
fs.writeFileSync(path.join(SITE, 'sitemap.xml'),
  '<?xml version="1.0" encoding="UTF-8"?>\n' +
  '<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n' +
  indexable.map(url => `  <url><loc>${ORIGIN}${url}</loc><lastmod>${today}</lastmod></url>`).join('\n') +
  '\n</urlset>\n');
console.log(`✓ Created sitemap.xml (${indexable.length} urls)`);

fs.writeFileSync(path.join(SITE, 'robots.txt'),
  'User-agent: *\n' +
  'Allow: /\n' +
  '\n' +
  `Sitemap: ${ORIGIN}sitemap.xml\n`);
console.log('✓ Created robots.txt');
