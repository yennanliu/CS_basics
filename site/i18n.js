/* ─────────────────────────────────────────────────────────────────────────
   CS_basics — 繁體中文 cheatsheets, composed at build time.

   There is one markdown tree, the English one. A translation is a *sparse
   overlay* of translated sections in i18n/zh/<slug>.md, and the full Chinese
   document is composed from the two whenever the site is built.

   Two properties fall out of that, and both used to need machinery to enforce:

   - **Code is never duplicated.** A sheet is ~70% fenced code by weight, and
     that code must read identically in both languages. `splitBlocks` lifts every
     fence out before anything is stored, so the store holds prose only and
     `joinBlocks` puts the originals back. Code cannot drift because there is
     only one copy of it.
   - **The two documents always have the same shape.** Headings, and their order,
     come from the English sheet, so a translation cannot add or drop one.

   The translation unit is a **section**: a heading plus the prose beneath it, up
   to the next heading (median 249 bytes). Each is keyed by a hash of its English
   text, so editing one section invalidates that section's translation and leaves
   the rest of the sheet current. Whole-file tracking used to charge a ~1,000-line
   re-translation for a 45-line edit.

   A section with no entry in the store falls back to its English text, so a
   half-translated sheet renders as a Chinese page with English gaps rather than
   failing. That is also why `compose` needs no notion of "untranslated".
   ───────────────────────────────────────────────────────────────────────── */
'use strict';

const crypto = require('node:crypto');

/** A lifted-out code block. Unnumbered: order alone puts them back. */
const CODE = '<!--CODE-->';

const FENCE_RE = /^(\s*)(`{3,}|~{3,})(.*)$/;
const KEY_RE = /^<!-- ([0-9a-f]{12}) -->$/;

/**
 * Split a sheet into prose (every fence collapsed to a one-line CODE marker)
 * and the ordered code blocks that were lifted out.
 *
 * Fence matching follows CommonMark: the closing fence uses the same character
 * and is at least as long as the opening one.
 */
function splitBlocks(text) {
  const prose = [];
  const blocks = [];
  const lines = text.split('\n');
  let i = 0;
  while (i < lines.length) {
    const m = FENCE_RE.exec(lines[i]);
    // An info string containing the fence character is not an opener (``` ``` ```).
    if (!m || m[3].includes(m[2][0])) {
      prose.push(lines[i++]);
      continue;
    }
    const fence = m[2];
    const block = [lines[i++]];
    const close = new RegExp('^\\s*' + fence[0] + '{' + fence.length + ',}\\s*$');
    while (i < lines.length && !close.test(lines[i])) block.push(lines[i++]);
    if (i < lines.length) block.push(lines[i++]);
    prose.push(CODE);
    blocks.push(block.join('\n'));
  }
  return { prose: prose.join('\n'), blocks };
}

/** Substitute each CODE marker for its block, in order. */
function joinBlocks(prose, blocks) {
  let n = 0;
  const out = prose.split('\n').map(line => (line.trim() === CODE ? blocks[n++] : line));
  if (n !== blocks.length) {
    throw new Error(
      `code markers do not match: the text carries ${n}, the sheet has ${blocks.length}. ` +
      'A translated section must keep every <!--CODE--> line it was given.'
    );
  }
  return out.join('\n');
}

/**
 * Cut prose into sections at each heading. Concatenating the result with '\n'
 * reproduces the input exactly, which is what lets `compose` swap in a
 * translation without disturbing the layout around it.
 */
function splitSections(prose) {
  const sections = [];
  let cur = [];
  for (const line of prose.split('\n')) {
    if (/^#{1,6} /.test(line) && cur.length) {
      sections.push(cur.join('\n'));
      cur = [];
    }
    cur.push(line);
  }
  sections.push(cur.join('\n'));
  return sections;
}

/** A section's body and the blank lines that trail it, which layout depends on. */
function splitTrailer(section) {
  const body = section.replace(/\n+$/, '');
  return [body, section.slice(body.length)];
}

/**
 * The store key for a section: a hash of its English body.
 *
 * 12 hex digits over ~39 sections per sheet — a collision needs two *different*
 * English sections in one file to hash alike, which is ~1e-10. Two *identical*
 * sections sharing a key is deliberate: they get the same translation.
 */
function keyOf(body) {
  return crypto.createHash('sha1').update(body.trim(), 'utf8').digest('hex').slice(0, 12);
}

/** Parse a store file into key → translated body. */
function parseStore(md) {
  const store = new Map();
  let key = null;
  let buf = [];
  const flush = () => {
    if (key) store.set(key, buf.join('\n').replace(/\n+$/, ''));
    buf = [];
  };
  for (const line of md.split('\n')) {
    const m = KEY_RE.exec(line);
    if (m) {
      flush();
      key = m[1];
    } else if (key) {
      buf.push(line);
    }
  }
  flush();
  return store;
}

/** Render key → body back to a store file, in the order given. */
function formatStore(entries) {
  return entries.map(([key, body]) => `<!-- ${key} -->\n${body}\n`).join('\n');
}

/**
 * Build the 繁體中文 document for one sheet.
 *
 * `store` is a Map from `parseStore`; anything it does not carry stays English.
 */
function compose(enText, store) {
  const { prose, blocks } = splitBlocks(enText);
  const composed = splitSections(prose).map(section => {
    const [body, trailer] = splitTrailer(section);
    const zh = store.get(keyOf(body));
    return zh === undefined ? section : zh + trailer;
  });
  return joinBlocks(composed.join('\n'), blocks);
}

/**
 * One row per section of an English sheet: its key, its English body, and the
 * translation if the store has one. Drives both `sync` and the coverage report.
 */
function survey(enText, store) {
  const { prose } = splitBlocks(enText);
  return splitSections(prose).map(section => {
    const [body] = splitTrailer(section);
    const key = keyOf(body);
    return { key, en: body, zh: store.get(key) };
  }).filter(row => row.en.trim() !== '');
}

module.exports = {
  CODE,
  splitBlocks,
  joinBlocks,
  splitSections,
  splitTrailer,
  keyOf,
  parseStore,
  formatStore,
  compose,
  survey,
};
