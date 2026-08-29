/* Runs site/i18n.js against the real cheatsheets, not toy input.
 *
 * The unit tests in i18n.test.js pin the behaviour of each function; these pin
 * the thing that actually matters — that every sheet in the repo still composes
 * into a well-formed page. Composition is the only step between the store and
 * what ships, and it runs at build time, so a bad store entry would otherwise
 * surface as a broken deploy rather than a failing test.
 *
 * Deliberately NOT checked here: entries the English no longer has. Those are
 * the normal state of a sheet whose English was edited and whose translation has
 * not caught up, and failing CI on them would mean an English-only edit could not
 * land until someone re-translated. `node script/zh.js status` reports them, and
 * `sync` clears them.
 */
const test = require('node:test');
const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');

const I = require('../i18n.js');

const ROOT = path.join(__dirname, '..', '..');
const EN_DIR = path.join(ROOT, 'doc', 'cheatsheet');
const STORE_DIR = path.join(ROOT, 'i18n', 'zh');

const slugs = fs.existsSync(STORE_DIR)
  ? fs.readdirSync(STORE_DIR).filter(f => f.endsWith('.md')).map(f => f.slice(0, -3)).sort()
  : [];

const read = p => fs.readFileSync(p, 'utf8');
const enPath = slug => path.join(EN_DIR, `${slug}.md`);
const headings = md =>
  md.split('\n').filter(l => /^#{1,6} /.test(l)).map(l => l.match(/^#+/)[0].length);

test('there is a corpus to check', () => {
  assert.ok(slugs.length > 0, 'i18n/zh is empty — the store did not survive');
});

test('every store file mirrors an English sheet', () => {
  const orphans = slugs.filter(s => !fs.existsSync(enPath(s)));
  assert.deepEqual(orphans, [], 'store files with no English sheet');
});

test('every sheet composes without throwing', () => {
  const failed = [];
  for (const slug of slugs) {
    try {
      I.compose(read(enPath(slug)), I.parseStore(read(path.join(STORE_DIR, `${slug}.md`))));
    } catch (err) {
      failed.push(`${slug}: ${err.message}`);
    }
  }
  assert.deepEqual(failed, []);
});

// The build pairs the two documents' headings by position to remap in-page
// anchors, and asserts no link is left dangling. Composition takes structure from
// the English sheet, so this cannot drift — which is exactly why it is worth
// pinning: it is the property that replaced a whole class of hand-checking.
test('a composed sheet has the same heading shape as its English original', () => {
  const bad = [];
  for (const slug of slugs) {
    const en = read(enPath(slug));
    const zh = I.compose(en, I.parseStore(read(path.join(STORE_DIR, `${slug}.md`))));
    const a = headings(en);
    const b = headings(zh);
    if (a.length !== b.length || a.some((lvl, i) => lvl !== b[i])) {
      bad.push(`${slug}: ${a.length} headings in English, ${b.length} composed`);
    }
  }
  assert.deepEqual(bad, []);
});

// Code lives in exactly one place, so the composed page must carry every block
// the English sheet has — no more, no fewer, and no marker left showing.
test('a composed sheet carries the English code blocks, and leaks no marker', () => {
  const bad = [];
  for (const slug of slugs) {
    const en = read(enPath(slug));
    const zh = I.compose(en, I.parseStore(read(path.join(STORE_DIR, `${slug}.md`))));
    const enBlocks = I.splitBlocks(en).blocks;
    const zhBlocks = I.splitBlocks(zh).blocks;
    if (enBlocks.length !== zhBlocks.length) {
      bad.push(`${slug}: ${enBlocks.length} blocks in English, ${zhBlocks.length} composed`);
    } else if (enBlocks.some((b, i) => b !== zhBlocks[i])) {
      bad.push(`${slug}: a code block differs from the English sheet`);
    }
    if (zh.includes(I.CODE)) bad.push(`${slug}: a <!--CODE--> marker reached the page`);
  }
  assert.deepEqual(bad, []);
});

test('every stored translation keeps the code markers its English section had', () => {
  const count = s => (s.match(/<!--CODE-->/g) || []).length;
  const bad = [];
  for (const slug of slugs) {
    const store = I.parseStore(read(path.join(STORE_DIR, `${slug}.md`)));
    for (const row of I.survey(read(enPath(slug)), store)) {
      if (row.zh !== undefined && count(row.zh) !== count(row.en)) {
        bad.push(`${slug} ${row.key}: ${count(row.en)} markers in English, ${count(row.zh)} stored`);
      }
    }
  }
  assert.deepEqual(bad, []);
});
