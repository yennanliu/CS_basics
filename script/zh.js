#!/usr/bin/env node
/**
 * 繁體中文 cheatsheet translations — authoring CLI.
 *
 *   node script/zh.js status [--write]   coverage per sheet (--write: refresh the tracker)
 *   node script/zh.js todo [slug ...]    print the sections still needing a translation
 *   node script/zh.js sync [slug ...]    drop orphaned entries, reorder to match English
 *
 * There is no `merge` and no `verify`: the site composes the Chinese document
 * from the English sheet plus i18n/zh/<slug>.md at build time, so there is no
 * second copy of the code to drift out of step. See site/i18n.js.
 *
 * The workflow for a sheet whose English changed:
 *
 *   node script/zh.js todo heap        # the sections whose English no longer matches
 *   …translate each one…               # keeping every <!--CODE--> line it carries
 *   …append them to i18n/zh/heap.md…
 *   node script/zh.js sync heap        # tidy: drop what the English no longer has
 */
'use strict';

const fs = require('fs');
const path = require('path');
const I = require('../site/i18n.js');

const ROOT = path.join(__dirname, '..');
const EN_DIR = path.join(ROOT, 'doc', 'cheatsheet');
const STORE_DIR = path.join(ROOT, 'i18n', 'zh');
const TRACKER = path.join(ROOT, 'doc', 'cheatsheet-zh-progress.md');

// Not cheatsheets: the template, and the index files build-site.js skips.
const SKIP = new Set(['00_template', 'README']);

const allSlugs = () =>
  fs.readdirSync(EN_DIR)
    .filter(f => f.endsWith('.md') && !SKIP.has(f.slice(0, -3)))
    .map(f => f.slice(0, -3))
    .sort();

function resolve(slugs) {
  if (!slugs.length) return allSlugs();
  const known = new Set(allSlugs());
  const unknown = slugs.filter(s => !known.has(s));
  if (unknown.length) {
    console.error(`error: no such cheatsheet: ${unknown.join(', ')}`);
    process.exit(1);
  }
  return slugs;
}

const storePath = slug => path.join(STORE_DIR, `${slug}.md`);
const readStore = slug =>
  fs.existsSync(storePath(slug)) ? I.parseStore(fs.readFileSync(storePath(slug), 'utf8')) : new Map();

/**
 * One row per sheet. `done` counts sections whose stored translation differs
 * from the English — the only definition that cannot go stale, since it is read
 * off the text itself rather than off a marker somebody has to remember to clear.
 */
function survey(slugs) {
  return slugs.map(slug => {
    const en = fs.readFileSync(path.join(EN_DIR, `${slug}.md`), 'utf8');
    const store = readStore(slug);
    const rows = I.survey(en, store);
    const done = rows.filter(r => r.zh !== undefined && r.zh !== r.en).length;
    // An entry the English no longer has: left behind by an edit upstream.
    const keys = new Set(rows.map(r => r.key));
    const orphans = [...store.keys()].filter(k => !keys.has(k)).length;
    return { slug, rows, total: rows.length, done, orphans };
  });
}

function cmdStatus(slugs, write) {
  const rows = survey(slugs);
  const total = rows.reduce((n, r) => n + r.total, 0);
  const done = rows.reduce((n, r) => n + r.done, 0);
  const orphans = rows.reduce((n, r) => n + r.orphans, 0);
  const sheets = rows.filter(r => r.done > 0).length;
  const pct = total ? (100 * done) / total : 0;

  console.log(
    `${done}/${total} sections translated (${pct.toFixed(0)}%) across ${sheets}/${rows.length} sheets` +
    (orphans ? `, ${orphans} orphaned entries — run: node script/zh.js sync` : '')
  );
  if (!write) {
    for (const r of rows) {
      if (r.done === r.total) continue;
      console.log(`  ${r.slug}: ${r.done}/${r.total}` + (r.orphans ? ` (${r.orphans} orphaned)` : ''));
    }
    return;
  }
  fs.writeFileSync(TRACKER, tracker(rows, done, total, pct));
  console.log(`✓ wrote ${path.relative(ROOT, TRACKER)}`);
}

function cmdTodo(slugs) {
  let n = 0;
  for (const { slug, rows } of survey(slugs)) {
    const missing = rows.filter(r => r.zh === undefined);
    if (!missing.length) continue;
    console.log(`\n## ${slug} — ${missing.length} section(s)\n`);
    for (const r of missing) console.log(`<!-- ${r.key} -->\n${r.en}\n`);
    n += missing.length;
  }
  console.error(n ? `${n} section(s) need a translation` : 'nothing to translate');
}

function cmdSync(slugs) {
  let changed = 0;
  for (const { slug, rows } of survey(slugs)) {
    if (!fs.existsSync(storePath(slug))) continue;
    const store = readStore(slug);
    // English order, English membership: an entry whose key is gone goes with it.
    const kept = [];
    const seen = new Set();
    for (const r of rows) {
      if (store.has(r.key) && !seen.has(r.key)) {
        kept.push([r.key, store.get(r.key)]);
        seen.add(r.key);
      }
    }
    const next = I.formatStore(kept);
    if (next === fs.readFileSync(storePath(slug), 'utf8')) continue;
    fs.writeFileSync(storePath(slug), next);
    console.log(`✓ ${slug}: ${kept.length} entries, ${store.size - kept.length} dropped`);
    changed++;
  }
  console.log(changed ? `synced ${changed} store file(s)` : 'every store file is already in sync');
}

const TRACKER_HEAD = `# 繁體中文 Cheatsheets — Translation Progress

The cheatsheets under [\`doc/cheatsheet/\`](./cheatsheet/) are the only markdown
tree. A translation is a *sparse overlay* of translated sections in
\`i18n/zh/<slug>.md\`, and the site composes the two into a full Chinese document
at build time — see the *Traditional Chinese cheatsheets* section of
[CLAUDE.md](../CLAUDE.md).

**This file is generated. Do not edit it by hand:**

\`\`\`bash
node script/zh.js status --write
\`\`\`

## How a translation is stored

Roughly 70% of these sheets is fenced code, and that code must read identically
in both languages — so it is never stored twice:

\`\`\`text
doc/cheatsheet/<slug>.md          the only markdown tree
   │  every fence lifts out to a one-line <!--CODE--> marker
   │  the prose is cut into sections at each heading
   ▼
i18n/zh/<slug>.md                 <!-- hash --> + the translated section
   │  compose — English structure, translated prose, original code
   ▼
_site/cheatsheets/<slug>.zh.html
\`\`\`

Each section is keyed by a hash of **its English text**. Edit one section of an
English sheet and only that section's translation goes missing; the rest of the
sheet stays current. A section with no entry falls back to English, so a
half-translated sheet renders with English gaps rather than failing.

## Known limitations

- **The star legend and the priority tooltips** inside a sheet are still English;
  they come from \`site/build-lib.js\`, not from the markdown.
- **LC problem titles stay in English** — they are proper names, and keeping them
  is what makes a problem findable on LeetCode itself.

`;

function tracker(rows, done, total, pct) {
  const out = [TRACKER_HEAD];
  out.push(`## Status — ${done} / ${total} sections (${pct.toFixed(0)}%)`, '');
  out.push('| Sheet | Sections | 繁體中文 |', '|---|---:|:---:|');
  for (const r of rows) {
    const state =
      r.done === 0 ? '—'
        : r.done === r.total ? `[✅](../i18n/zh/${r.slug}.md)`
          : `[${r.done}/${r.total}](../i18n/zh/${r.slug}.md)`;
    out.push(`| [${r.slug}](./cheatsheet/${r.slug}.md) | ${r.total} | ${state} |`);
  }
  out.push('');
  return out.join('\n');
}

function main() {
  const [cmd, ...rest] = process.argv.slice(2);
  const write = rest.includes('--write');
  const slugs = resolve(rest.filter(a => !a.startsWith('--')));
  if (cmd === 'status') cmdStatus(slugs, write);
  else if (cmd === 'todo') cmdTodo(slugs);
  else if (cmd === 'sync') cmdSync(slugs);
  else {
    console.error('usage: node script/zh.js status [--write] | todo [slug ...] | sync [slug ...]');
    process.exit(1);
  }
}

main();
