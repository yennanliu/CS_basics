const test = require('node:test');
const assert = require('node:assert/strict');

const I = require('../i18n.js');

// ── splitBlocks / joinBlocks ──────────────────────────────────────────────
// The reason code is never stored twice: it is lifted out before a translation
// is ever written, and put back verbatim at compose time.

test('splitBlocks lifts each fence out and leaves one marker in its place', () => {
  const { prose, blocks } = I.splitBlocks('# T\n\n```java\nint x = 1;\n```\n\nafter\n');
  assert.equal(prose, '# T\n\n<!--CODE-->\n\nafter\n');
  assert.deepEqual(blocks, ['```java\nint x = 1;\n```']);
});

test('splitBlocks honours CommonMark fence lengths, so a nested fence stays inside', () => {
  const md = '````text\n```\ninner\n```\n````\n';
  const { prose, blocks } = I.splitBlocks(md);
  assert.equal(prose, '<!--CODE-->\n');
  assert.equal(blocks.length, 1);
  assert.ok(blocks[0].includes('inner'));
});

test('splitBlocks does not treat an info string containing the fence char as an opener', () => {
  const { blocks } = I.splitBlocks('``` ``` ```\n');
  assert.equal(blocks.length, 0);
});

test('splitBlocks closes an unterminated fence at end of file rather than hanging', () => {
  const { prose, blocks } = I.splitBlocks('```java\nint x = 1;\n');
  assert.equal(prose, '<!--CODE-->');
  assert.equal(blocks.length, 1);
});

test('joinBlocks reverses splitBlocks exactly', () => {
  const md = '# T\n\n```java\nx\n```\n\ntext\n\n```python\ny\n```\n';
  const { prose, blocks } = I.splitBlocks(md);
  assert.equal(I.joinBlocks(prose, blocks), md);
});

// A translated section that lost a marker would otherwise silently drop code
// from the page — the one integrity failure the store still has to catch.
test('joinBlocks throws when the text carries the wrong number of markers', () => {
  assert.throws(() => I.joinBlocks('<!--CODE-->', ['a', 'b']), /2/);
  assert.throws(() => I.joinBlocks('<!--CODE-->\n<!--CODE-->', ['a']), /markers do not match/);
});

// ── splitSections ─────────────────────────────────────────────────────────

test('splitSections cuts at each heading and rejoins to the original exactly', () => {
  const prose = 'intro\n\n## A\n\nbody a\n\n### B\n\nbody b\n';
  const secs = I.splitSections(prose);
  assert.equal(secs.length, 3);
  assert.ok(secs[1].startsWith('## A'));
  assert.equal(secs.join('\n'), prose);
});

test('splitSections needs a space after the hashes, so a bare # is prose', () => {
  assert.equal(I.splitSections('text\n#hashtag\nmore').length, 1);
});

test('splitSections keeps a leading chunk that precedes the first heading', () => {
  const secs = I.splitSections('front matter\n\n# Title\n');
  assert.equal(secs.length, 2);
  assert.equal(secs[0], 'front matter\n');
});

// ── keyOf ─────────────────────────────────────────────────────────────────

test('keyOf ignores surrounding whitespace, so re-indenting does not orphan a translation', () => {
  assert.equal(I.keyOf('## A\n\nbody'), I.keyOf('\n## A\n\nbody\n\n'));
});

// Layout comes from the English sheet at compose time, never from the store, so
// hashing past cosmetic whitespace cannot change what renders — it only stops a
// stray trailing space or an extra blank line from throwing away a good
// translation. These are the two edits that used to orphan one.
test('keyOf ignores trailing spaces and extra blank lines inside a section', () => {
  const base = '## A\n\nfirst\n\nsecond';
  assert.equal(I.keyOf(base), I.keyOf('## A  \n\nfirst   \n\nsecond'));
  assert.equal(I.keyOf(base), I.keyOf('## A\n\n\n\nfirst\n\n\nsecond'));
});

test('keyOf still separates sections that differ in their words', () => {
  assert.notEqual(I.keyOf('## A\n\nfirst'), I.keyOf('## A\n\nfirsts'));
  // A blank line is cosmetic; a *missing* line is not.
  assert.notEqual(I.keyOf('## A\n\nfirst\n\nsecond'), I.keyOf('## A\n\nfirst'));
});

test('keyOf changes when the English text changes', () => {
  assert.notEqual(I.keyOf('## A\n\nbody'), I.keyOf('## A\n\nbody edited'));
});

test('keyOf is 12 hex digits', () => {
  assert.match(I.keyOf('anything'), /^[0-9a-f]{12}$/);
});

// ── parseStore / formatStore ──────────────────────────────────────────────

test('formatStore and parseStore round-trip', () => {
  const entries = [['a1b2c3d4e5f6', '## 總覽\n\n內容'], ['0123456789ab', '### 關鍵性質']];
  const store = I.parseStore(I.formatStore(entries));
  assert.deepEqual([...store], entries);
});

test('parseStore ignores anything before the first key, so a file can carry a preamble', () => {
  const store = I.parseStore('notes to a translator\n\n<!-- a1b2c3d4e5f6 -->\n## 總覽\n');
  assert.deepEqual([...store.keys()], ['a1b2c3d4e5f6']);
});

test('parseStore rejects a malformed key line instead of storing it', () => {
  // Wrong length and non-hex both fail the key pattern, so the line is body text.
  assert.equal(I.parseStore('<!-- nothex89abc -->\nx').size, 0);
  assert.equal(I.parseStore('<!-- a1b2 -->\nx').size, 0);
});

// ── compose ───────────────────────────────────────────────────────────────

const EN = '# Heap\n\n> **Scope** — heaps.\n\n## Overview\n\nA heap is a tree.\n\n```java\nint x;\n```\n';

test('compose returns the English sheet unchanged when the store is empty', () => {
  assert.equal(I.compose(EN, new Map()), EN);
});

test('compose swaps in a translated section and leaves the rest English', () => {
  const store = new Map([[I.keyOf('## Overview\n\nA heap is a tree.\n\n<!--CODE-->'),
                          '## 總覽\n\n堆積是一棵樹。\n\n<!--CODE-->']]);
  const out = I.compose(EN, store);
  assert.ok(out.includes('## 總覽'), 'the translated section is used');
  assert.ok(out.includes('堆積是一棵樹。'));
  assert.ok(out.includes('# Heap'), 'an untranslated section falls back to English');
  assert.ok(out.includes('```java\nint x;\n```'), 'the code block comes back verbatim');
  assert.ok(!out.includes('<!--CODE-->'), 'no marker survives into the page');
});

// Layout is the English sheet's, so blank-line drift in a translation cannot
// reflow the page.
test('compose takes the blank lines between sections from the English sheet', () => {
  const en = '# T\n\n\n## A\n\nbody\n';
  const store = new Map([[I.keyOf('## A\n\nbody'), '## 甲\n\n內文']]);
  assert.equal(I.compose(en, store), '# T\n\n\n## 甲\n\n內文\n');
});

test('compose gives two identical English sections the same translation', () => {
  const en = '## Summary\n\nsame\n\n## Summary\n\nsame\n';
  const store = new Map([[I.keyOf('## Summary\n\nsame'), '## 總結\n\n一樣']]);
  assert.equal(I.compose(en, store).match(/## 總結/g).length, 2);
});

test('compose throws when a stored section dropped a code marker', () => {
  const store = new Map([[I.keyOf('## Overview\n\nA heap is a tree.\n\n<!--CODE-->'),
                          '## 總覽\n\n堆積是一棵樹。']]);
  assert.throws(() => I.compose(EN, store), /markers do not match/);
});

// ── survey ────────────────────────────────────────────────────────────────

test('survey reports each section key and whether the store carries it', () => {
  const key = I.keyOf('## Overview\n\nA heap is a tree.\n\n<!--CODE-->');
  const rows = I.survey(EN, new Map([[key, '## 總覽']]));
  // Two sections: the H1 carries the Scope line, which is not itself a heading.
  assert.equal(rows.length, 2);
  assert.equal(rows.find(r => r.key === key).zh, '## 總覽');
  assert.equal(rows.find(r => r.en.startsWith('# Heap')).zh, undefined);
});

test('survey drops empty sections, which are not translatable units', () => {
  assert.equal(I.survey('\n\n\n', new Map()).length, 0);
});
