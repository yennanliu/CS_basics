#!/usr/bin/env node
/**
 * 繁體中文 translations — authoring CLI.
 *
 *   node script/zh.js status [--write]     coverage per doc (--write: refresh the trackers)
 *   node script/zh.js todo [id ...]        print the sections still needing a translation
 *   node script/zh.js sync [--prune] [id ...]
 *                                          reorder to match English, park what it dropped
 *                                          (--prune: forget the parked entries)
 *
 * A document's id is its store path under i18n/zh, without the .md — `heap` for a
 * cheatsheet, `faq/java/jvm` for an FAQ. An id prefix stands for everything under
 * it, so `faq` is the whole FAQ tree and `faq/java` one directory of it; a bare
 * corpus name (`cheatsheet`, `faq`) works too. No id at all means every document.
 *
 * There is no `merge` and no `verify`: the site composes the Chinese document
 * from the English markdown plus its i18n/zh overlay at build time, so there is
 * no second copy of the code to drift out of step. See site/i18n.js.
 *
 * The workflow for a doc whose English changed:
 *
 *   node script/zh.js sync heap        # park the translations the edit invalidated
 *   node script/zh.js todo heap        # the sections whose English no longer matches
 *   …adapt each parked translation…    # keeping every <!--CODE--> line it carries
 *   …write them back as live entries…
 *   node script/zh.js sync heap        # tidy, and drop the parked copies you used
 */
'use strict';

const fs = require('fs');
const path = require('path');
const I = require('../site/i18n.js');

const ROOT = path.join(__dirname, '..');
const abs = p => path.join(ROOT, p);

/** Every translatable document, across every tree site/i18n.js knows about. */
const allDocs = () => I.docs();

/**
 * Turn CLI arguments into documents. An argument is a corpus name, an exact id,
 * or an id prefix — which is what lets one command take a whole tree
 * (`faq`), one directory of it (`faq/java`) or a single doc.
 */
function resolve(args) {
  const docs = allDocs();
  if (!args.length) return docs;
  const picked = new Map();
  const unknown = [];
  for (const arg of args) {
    const id = arg.replace(/\.md$/, '').replace(/\/$/, '');
    const hit = docs.filter(d =>
      d.id === id || d.corpus === id || d.id.startsWith(`${id}/`));
    if (!hit.length) unknown.push(arg);
    for (const d of hit) picked.set(d.id, d);
  }
  if (unknown.length) {
    console.error(`error: no such document: ${unknown.join(', ')}`);
    process.exit(1);
  }
  return docs.filter(d => picked.has(d.id));
}

/** A document's live translations, or an empty map when it has none yet. */
const readStore = doc =>
  fs.existsSync(abs(doc.store)) ? I.parseStore(fs.readFileSync(abs(doc.store), 'utf8')) : new Map();

/**
 * One row per document. `done` counts sections the store has an entry for — not
 * sections whose Chinese differs from the English.
 *
 * That distinction matters. 238 cheatsheet sections are an LC-titled heading over
 * a code block, with no prose at all: `### Trapping Rain Water — LC 42`. House
 * rule keeps LC titles in English, so their correct translation *is* the English
 * text, and a differs-from-English count could never mark them done — leaving a
 * permanent 95% ceiling and a `todo` list that was 93% work nobody should do.
 * An entry means somebody looked at the section, which is the thing worth
 * counting.
 */
function survey(docs) {
  return docs.map(doc => {
    const en = fs.readFileSync(abs(doc.en), 'utf8');
    const store = readStore(doc);
    const rows = I.survey(en, store);
    const done = rows.filter(r => r.zh !== undefined).length;
    // An entry the English no longer has: left behind by an edit upstream.
    const keys = new Set(rows.map(r => r.key));
    const orphans = [...store.keys()].filter(k => !keys.has(k)).length;
    return { doc, rows, total: rows.length, done, orphans };
  });
}

/** Total a field over survey rows. */
const sum = (rows, pick) => rows.reduce((n, r) => n + pick(r), 0);
/** A percentage that is 0 rather than NaN for an empty corpus. */
const pct = (done, total) => (total ? (100 * done) / total : 0);

/**
 * Coverage: one headline figure, then a block per tree.
 *
 * `--write` refreshes each tree's generated progress doc instead of listing the
 * documents that still need work.
 */
function cmdStatus(docs, write) {
  const rows = survey(docs);
  const orphans = sum(rows, r => r.orphans);
  console.log(
    `${sum(rows, r => r.done)}/${sum(rows, r => r.total)} sections translated ` +
    `(${pct(sum(rows, r => r.done), sum(rows, r => r.total)).toFixed(0)}%) ` +
    `across ${rows.filter(r => r.done > 0).length}/${rows.length} documents` +
    (orphans ? `, ${orphans} orphaned entries — run: node script/zh.js sync` : '')
  );

  for (const c of I.CORPORA) {
    const mine = rows.filter(r => r.doc.corpus === c.name);
    if (!mine.length) continue;
    const done = sum(mine, r => r.done);
    const total = sum(mine, r => r.total);
    console.log(
      `\n${c.enDir} — ${done}/${total} sections (${pct(done, total).toFixed(0)}%), ` +
      `${mine.filter(r => r.done > 0).length}/${mine.length} documents`
    );
    if (write) {
      // A tracker describes a whole tree, so it is written from the whole tree even
      // when the command names one document. Writing `mine` meant
      // `status faq/java --write` rewrote doc/faq-zh-progress.md with that one
      // directory's 14 rows and dropped the other 35 documents.
      const all = survey(I.docs(c.name));
      fs.writeFileSync(abs(c.tracker), tracker(c, all));
      console.log(`  ✓ wrote ${c.tracker} (${all.length} documents)`);
      continue;
    }
    for (const r of mine) {
      if (r.done === r.total) continue;
      console.log(`  ${r.doc.id}: ${r.done}/${r.total}` + (r.orphans ? ` (${r.orphans} orphaned)` : ''));
    }
  }
}

/**
 * Print the sections with no translation, each under the key it must be stored
 * against — the output is meant to be pasted into the store file and filled in.
 */
function cmdTodo(docs) {
  let n = 0;
  let parked = 0;
  for (const { doc, rows } of survey(docs)) {
    const missing = rows.filter(r => r.zh === undefined);
    if (!missing.length) continue;
    const stale = fs.existsSync(abs(doc.store))
      ? I.parseStale(fs.readFileSync(abs(doc.store), 'utf8')).size : 0;
    parked += stale;
    console.log(`\n## ${doc.id} — ${missing.length} section(s)` +
      (stale ? `, ${stale} parked translation(s) in ${doc.store} to adapt` : '') + '\n');
    for (const r of missing) console.log(`<!-- ${r.key} -->\n${r.en}\n`);
    n += missing.length;
  }
  console.error(
    n ? `${n} section(s) need a translation` +
        (parked ? `; ${parked} parked translation(s) are there to start from` : '')
      : 'nothing to translate'
  );
}

/**
 * Reconcile a store with its English document: reorder the live entries to match,
 * park anything the English no longer has, and revive anything it has again.
 *
 * Parking rather than deleting is the point. An English edit is usually small,
 * and the Chinese it invalidates is usually still most of the way there — so the
 * old text stays in the file for whoever writes the replacement. `--prune` is the
 * only thing that throws it away, and you have to ask for it.
 */
function cmdSync(docs, prune) {
  let changed = 0;
  let parked = 0;
  let revived = 0;
  for (const { doc, rows } of survey(docs)) {
    if (!fs.existsSync(abs(doc.store))) continue;
    const raw = fs.readFileSync(abs(doc.store), 'utf8');
    const live = I.parseStore(raw);
    const stale = I.parseStale(raw);

    const kept = [];
    const seen = new Set();
    for (const r of rows) {
      if (seen.has(r.key)) continue;
      // Reverting an English section brings its parked translation back: the text
      // is the same again, so the key is too.
      const body = live.get(r.key) ?? stale.get(r.key);
      if (body === undefined) continue;
      if (!live.has(r.key)) revived++;
      kept.push([r.key, body]);
      seen.add(r.key);
    }

    const keep = prune ? [] : [...stale].filter(([k]) => !seen.has(k));
    for (const [k, v] of live) if (!seen.has(k)) { keep.push([k, v]); parked++; }

    const next = I.formatStore(kept, keep);
    if (next === raw) continue;
    fs.writeFileSync(abs(doc.store), next);
    console.log(`✓ ${doc.id}: ${kept.length} live, ${keep.length} parked`);
    changed++;
  }
  if (!changed) {
    console.log('every store file is already in sync');
    return;
  }
  console.log(
    `synced ${changed} store file(s)` +
    (parked ? `, ${parked} translation(s) parked for reuse` : '') +
    (revived ? `, ${revived} revived` : '') +
    (prune ? ' — pruned parked entries' : '')
  );
}

// ── The generated progress docs ──────────────────────────────────────────────
//
// One per tree, written side by side from the same survey, because the thing a
// reader wants is "how much of *this* tree is translated" and a single combined
// table of 182 rows answers that for neither.

const TRACKER_INTRO = {
  cheatsheet: `The cheatsheets under [\`doc/cheatsheet/\`](./cheatsheet/) are the only
markdown tree.`,
  faq: `The interview FAQs under [\`doc/faq/\`](./faq/) are the only markdown tree.`,
};

// How much of a tree is code, which is the reason the overlay stores none of it.
const TRACKER_CODE_NOTE = {
  cheatsheet: `Roughly 70% of these sheets is fenced code, and that code must read identically
in both languages — so it is never stored twice:`,
  faq: `Two fifths of these documents is fenced code — Java, SQL, shell, config — and it
must read identically in both languages, so it is never stored twice:`,
};

const TRACKER_LIMITS = {
  cheatsheet: `- **The star legend and the priority tooltips** inside a sheet are still English;
  they come from \`site/build-lib.js\`, not from the markdown.
- **LC problem titles stay in English** — they are proper names, and keeping them
  is what makes a problem findable on LeetCode itself.`,
  faq: `- **API, class and command names stay in English** — \`ConcurrentHashMap\`,
  \`SELECT ... FOR UPDATE\`, \`kafka-topics.sh\`. They are the strings you type and
  the terms an interviewer will use.
- **A few FAQs were written in Chinese to begin with**
  ([\`後端面試題總整理.md\`](./faq/backend/後端面試題總整理.md) among them). Their
  sections still need an entry each to count as translated, and that entry is
  usually the text already there.`,
};

/** The generated preamble of one tree's progress doc. */
function trackerHead(c) {
  return `# 繁體中文 ${c.label}s — Translation Progress

${TRACKER_INTRO[c.name]}
A translation is a *sparse overlay* of translated sections in
\`${c.storeDir}/<id>.md\`, and the site composes the two into a full Chinese
document at build time — see the *Traditional Chinese docs* section of
[CLAUDE.md](../CLAUDE.md).

**This file is generated. Do not edit it by hand:**

\`\`\`bash
node script/zh.js status --write
\`\`\`

## How a translation is stored

${TRACKER_CODE_NOTE[c.name]}

\`\`\`text
${c.enDir}/<id>.md
   │  every fence lifts out to a one-line <!--CODE--> marker
   │  the prose is cut into sections at each heading
   ▼
${c.storeDir}/<id>.md
   │  <!-- hash --> + the translated section
   │  compose — English structure, translated prose, original code
   ▼
_site/${c.name === 'faq' ? 'faqs' : 'cheatsheets'}/<page>.zh.html
\`\`\`

Each section is keyed by a hash of **its English text**. Edit one section of an
English document and only that section's translation goes missing; the rest of
the file stays current. A section with no entry falls back to English, so a
half-translated document renders with English gaps rather than failing.

## Known limitations

${TRACKER_LIMITS[c.name]}

`;
}

/** One tree's progress doc: the preamble, then a row per document. */
function tracker(c, rows) {
  const done = sum(rows, r => r.done);
  const total = sum(rows, r => r.total);
  const prefix = c.name === 'cheatsheet' ? '' : `${c.name}/`;
  const out = [trackerHead(c)];
  out.push(`## Status — ${done} / ${total} sections (${pct(done, total).toFixed(0)}%)`, '');
  out.push(`| ${c.label} | Sections | 繁體中文 |`, '|---|---:|:---:|');
  for (const r of rows) {
    const store = `../${r.doc.store}`;
    const state =
      r.done === 0 ? '—'
        : r.done === r.total ? `[✅](${store})`
          : `[${r.done}/${r.total}](${store})`;
    const name = r.doc.id.slice(prefix.length);
    out.push(`| [${name}](./${path.posix.relative('doc', r.doc.en)}) | ${r.total} | ${state} |`);
  }
  out.push('');
  return out.join('\n');
}

/** Parse `<command> [--flags] [id ...]` and dispatch. */
function main() {
  const [cmd, ...rest] = process.argv.slice(2);
  const write = rest.includes('--write');
  const docs = resolve(rest.filter(a => !a.startsWith('--')));
  if (cmd === 'status') cmdStatus(docs, write);
  else if (cmd === 'todo') cmdTodo(docs);
  else if (cmd === 'sync') cmdSync(docs, rest.includes('--prune'));
  else {
    console.error('usage: node script/zh.js status [--write] | todo [id ...] | ' +
                  'sync [--prune] [id ...]');
    process.exit(1);
  }
}

main();
