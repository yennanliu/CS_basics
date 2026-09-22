/**
 * site/pages/ must not carry the same CSS or JS twice.
 *
 * The nine agent-skill pages each had a pasted copy of one 225-line <style>
 * block and one 162-line <script>: eight byte-identical, and lc-cheatsheet's
 * already 32 lines adrift — a page that looked almost, but not quite, like its
 * siblings, with no way to tell which of the two was the intended design. That
 * is 2,700 lines and 133 KB of the repo, and it is why a one-line change to the
 * shared chrome meant editing nine files and getting it right nine times.
 *
 * They load site/skill-page.css and site/skill-page.js now. This is the rule
 * that keeps it that way: the moment two pages inline the same substantial
 * block again, it belongs in a shared file, and this fails naming both.
 *
 * The apps (lc-similar, lc-review-plan, lc-explorer, lc-complexity-quiz,
 * lc-random-picker, lc-roadmap, suggest-review) are deliberately untouched —
 * their inline scripts ARE the page, and no two of them are alike.
 */
const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const crypto = require('node:crypto');

const ROOT = path.join(__dirname, '..', '..');
const PAGES_DIR = path.join(ROOT, 'site', 'pages');

// Below this a repeated block is boilerplate (a one-line CSNav.mount(), a
// three-line reset) and hoisting it would cost more than it saves.
const SHARED_AT = 20;

const pages = fs.readdirSync(PAGES_DIR).filter((f) => f.endsWith('.html')).sort();
const read = (f) => fs.readFileSync(path.join(PAGES_DIR, f), 'utf8');

function blocks(html, kind) {
  const re = kind === 'style'
    ? /<style[^>]*>([\s\S]*?)<\/style>/g
    : /<script(?![^>]*\ssrc=)[^>]*>([\s\S]*?)<\/script>/g;
  return [...html.matchAll(re)].map((m) => m[1]).filter((b) => b.split('\n').length > SHARED_AT);
}

for (const kind of ['style', 'script']) {
  test(`no inline <${kind}> block over ${SHARED_AT} lines is repeated across pages`, () => {
    const owners = new Map();
    for (const page of pages) {
      for (const block of blocks(read(page), kind)) {
        const key = crypto.createHash('sha1').update(block).digest('hex');
        if (!owners.has(key)) owners.set(key, { lines: block.split('\n').length, pages: [] });
        owners.get(key).pages.push(page);
      }
    }
    const duplicated = [...owners.values()].filter((o) => o.pages.length > 1);
    assert.deepEqual(
      duplicated.map((o) => `${o.lines} lines in ${o.pages.join(', ')}`),
      [],
      `hoist the repeated block into site/skill-page.${kind === 'style' ? 'css' : 'js'} ` +
      '(and add it to build.sh) rather than keeping a second copy'
    );
  });
}

// The pair is only a pair if both halves ship. skill-page.css leaves .reveal
// visible so a page with no JS is never stuck blank; skill-page.js is what
// hides it. A page with the CSS and not the JS shows every section at once.
test('every page loading skill-page.css also loads skill-page.js', () => {
  const mismatched = pages.filter((page) => {
    const html = read(page);
    return html.includes('skill-page.css') !== html.includes('skill-page.js');
  });
  assert.deepEqual(mismatched, []);
});

test('the shared files exist and build.sh copies them', () => {
  for (const f of ['site/skill-page.css', 'site/skill-page.js']) {
    assert.ok(fs.existsSync(path.join(ROOT, f)), `${f} is missing`);
  }
  const buildSh = fs.readFileSync(path.join(ROOT, 'site', 'build.sh'), 'utf8');
  assert.match(buildSh, /skill-page\.css/, 'build.sh does not copy skill-page.css to _site/');
  assert.match(buildSh, /skill-page\.js/, 'build.sh does not copy skill-page.js to _site/');
});
