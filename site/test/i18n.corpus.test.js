/* Runs site/i18n.js against every real translated document, not toy input.
 *
 * The unit tests in i18n.test.js pin the behaviour of each function; these pin
 * the thing that actually matters — that every translated document in the repo,
 * cheatsheet or FAQ, still composes into a well-formed page. Composition is the only step between the store and
 * what ships, and it runs at build time, so a bad store entry would otherwise
 * surface as a broken deploy rather than a failing test.
 *
 * Deliberately NOT checked here: entries the English no longer has. Those are
 * the normal state of a document whose English was edited and whose translation has
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

// Every document that actually has a translation, across every tree i18n.js
// knows about. A doc with no store file is an untranslated one, not a failure.
const translated = I.docs().filter(d => fs.existsSync(path.join(ROOT, d.store)));

const read = p => fs.readFileSync(path.join(ROOT, p), 'utf8');
const headings = md =>
  md.split('\n').filter(l => /^#{1,6} /.test(l)).map(l => l.match(/^#+/)[0].length);

test('there is a corpus to check', () => {
  assert.ok(translated.length > 0, 'i18n/zh is empty — the store did not survive');
});

test('every store file mirrors an English document', () => {
  assert.deepEqual(I.orphanStores(), [], 'store files with no English document');
});

test('every document composes without throwing', () => {
  const failed = [];
  for (const doc of translated) {
    try {
      I.compose(read(doc.en), I.parseStore(read(doc.store)));
    } catch (err) {
      failed.push(`${doc.id}: ${err.message}`);
    }
  }
  assert.deepEqual(failed, []);
});

// The build pairs the two documents' headings by position to remap in-page
// anchors, and asserts no link is left dangling. Composition takes structure from
// the English sheet, so this cannot drift — which is exactly why it is worth
// pinning: it is the property that replaced a whole class of hand-checking.
test('a composed document has the same heading shape as its English original', () => {
  const bad = [];
  for (const doc of translated) {
    const en = read(doc.en);
    const zh = I.compose(en, I.parseStore(read(doc.store)));
    const a = headings(en);
    const b = headings(zh);
    if (a.length !== b.length || a.some((lvl, i) => lvl !== b[i])) {
      bad.push(`${doc.id}: ${a.length} headings in English, ${b.length} composed`);
    }
  }
  assert.deepEqual(bad, []);
});

// Code lives in exactly one place, so the composed page must carry every block
// the English sheet has — no more, no fewer, and no marker left showing.
test('a composed document carries the English code blocks, and leaks no marker', () => {
  const bad = [];
  for (const doc of translated) {
    const en = read(doc.en);
    const zh = I.compose(en, I.parseStore(read(doc.store)));
    const enBlocks = I.splitBlocks(en).blocks;
    const zhBlocks = I.splitBlocks(zh).blocks;
    if (enBlocks.length !== zhBlocks.length) {
      bad.push(`${doc.id}: ${enBlocks.length} blocks in English, ${zhBlocks.length} composed`);
    } else if (enBlocks.some((b, i) => b !== zhBlocks[i])) {
      bad.push(`${doc.id}: a code block differs from the English document`);
    }
    if (zh.includes(I.CODE)) bad.push(`${doc.id}: a <!--CODE--> marker reached the page`);
  }
  assert.deepEqual(bad, []);
});

test('every stored translation keeps the code markers its English section had', () => {
  const count = s => (s.match(/<!--CODE-->/g) || []).length;
  const bad = [];
  for (const doc of translated) {
    const store = I.parseStore(read(doc.store));
    for (const row of I.survey(read(doc.en), store)) {
      if (row.zh !== undefined && count(row.zh) !== count(row.en)) {
        bad.push(`${doc.id} ${row.key}: ${count(row.en)} markers in English, ${count(row.zh)} stored`);
      }
    }
  }
  assert.deepEqual(bad, []);
});
