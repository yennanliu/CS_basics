---
name: lc-zh-translate
description: Translate cheatsheet or FAQ sections into 繁體中文 as a sparse overlay under i18n/zh/, using script/zh.js's sync → todo → write → sync → status loop. Keeps every <!--CODE--> marker in order, takes structure and heading order from the English document, leaves link targets and API names in English, and adapts the entries a sync parked rather than starting them from nothing. Use when asked to translate a cheatsheet or FAQ, to work the translation backlog, to fill in the untranslated sections of a sheet, or to refresh the progress docs. Triggers - "translate dp_loop_order", "/lc-zh-translate heap", "work the zh backlog", "what is still untranslated?", "refresh the translation progress".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Translate into 繁體中文

Fill in translated sections of the sparse overlay under `i18n/zh/`, which the site composes
with the English document at build time.

**Invocation**: `/lc-zh-translate <id> [id ...]` — e.g. `/lc-zh-translate heap`,
`/lc-zh-translate faq/java`. **An id prefix stands for everything under it**, so one command
can take a whole tree (`faq`), a directory (`faq/java`) or a file (`faq/java/jvm`).

With no id, report the backlog (`node script/zh.js status`) and ask which to take. Do not
pick one unprompted — a translation campaign is the user's call about where to spend effort.

## What an overlay is, and why

There is **one markdown tree per family — the English one**. A translation is a *sparse
overlay of translated sections* mirroring the English path under `i18n/zh/`, and
`site/i18n.js` composes the two into a full Chinese document at build time.

Roughly 70% of a cheatsheet (two fifths of an FAQ) is fenced code that must read identically
in both languages, and every English sheet was edited in the last six months. A parallel
tree would store that code twice and track staleness per *file*, so a 45-line edit would
invalidate a 1,000-line translation. The overlay stores **prose only, keyed per section**
(median 249 bytes), so an edit invalidates only the section it touched.

`CORPORA` in `site/i18n.js` is the only place the family table lives — `script/zh.js`,
`build-site.js` and `site/test/i18n.corpus.test.js` all read it.

| | English tree | Overlay | Progress (generated) |
|---|---|---|---|
| Cheatsheets | `doc/cheatsheet/<slug>.md` | `i18n/zh/<slug>.md` | `doc/cheatsheet-zh-progress.md` |
| FAQs | `doc/faq/<path>.md` | `i18n/zh/faq/<path>.md` | `doc/faq-zh-progress.md` |

## Prime directives

1. **Every `<!--CODE-->` marker is kept, in order.** Each fence is lifted out to a one-line
   marker before storage and spliced back at compose time. A translated section that drops
   one, adds one, or reorders them makes `compose` **throw**. Count them before and after.
2. **Structure comes from the English document.** Headings and their order are the English
   document's, so a translation cannot add or drop a section and the two can never disagree
   about shape. Translate the heading *text* only.
3. **Links keep their English targets.** `[見 §3](#two-pointers)` still names the English
   slug. The build pairs the two documents' headings by position, retargets every fragment,
   and then asserts no link is left dangling — so a "helpfully" translated anchor breaks it.
4. **API, class and command names stay in English.** `ConcurrentHashMap`,
   `SELECT … FOR UPDATE`, `kafka-topics.sh`, `heapq.heappush`. They are what you type and
   what an interviewer will say. LC problem *titles* stay English too — house rule.
5. **Adapt the parked entry; do not start from nothing.** `sync` parks an invalidated
   translation under `<!-- stale: key -->` in the same file precisely because the English
   edit is usually small and the Chinese usually still most of the way there.

## The workflow

```bash
node script/zh.js status                  # the backlog, before anything
node script/zh.js sync <id>               # park the translations the edit invalidated
node script/zh.js todo <id>               # the sections needing a translation, keys included
#   ... write each one back into i18n/zh/<id>.md as a live `<!-- key -->` entry ...
node script/zh.js sync <id>               # tidy, and drop the parked copies you used
node script/zh.js status --write          # refresh both progress docs
```

`sync` **parks** rather than deletes. `compose` ignores parked entries, so one can never
reach a page. Reverting the English revives it on the next `sync` — same text, same key.
**Only `sync --prune` throws parked entries away**, so do not reach for it to tidy up.

## The steps

### 1. See the backlog

```bash
node script/zh.js status
```

It prints per-document coverage, the orphan count, and the documents at 0%. Take what the
user asked for; if they asked for "the backlog", start with a document at 0% rather than
picking off single sections across many files — a document that composes fully in Chinese is
worth more than the same effort spread thin.

### 2. Park what the English edits invalidated

```bash
node script/zh.js sync <id>
```

Run this **before** `todo`, so parked entries are available to adapt (directive 5).

### 3. List what is owed, with its keys

```bash
node script/zh.js todo <id>
```

Each section comes with its `<!-- key -->` — a hash of the English text. The key is the
join; it is not editable, and a translation filed under a wrong key is a translation the
compose step will never find.

### 4. Write each section into `i18n/zh/<id>.md`

```markdown
<!-- 4e9c8ca0cfb6 -->
# DP 迴圈順序與相依性

> **範圍** — 為什麼自底向上 DP 的迴圈*巢狀*與*方向*…

<!--CODE-->

接下來這段說明…

<!--CODE-->
```

- the key comment, then the translated prose, markers in place (directive 1);
- the Scope line becomes `> **範圍** — …` (the build reads either spelling for the card
  description). An FAQ has no Scope line, so its card is summarised from the *composed
  Chinese* — translate the lead paragraph and the card follows;
- **no `category` / `tier` / `kind`** in a translation. The build reads them off the English
  document, so the two indexes can never disagree;
- a ⭐ run on a heading is part of the heading — keep it exactly, since the site strips it
  into a star badge and the anchor depends on it.

**A missing section falls back to English**, so a half-translated document renders as a
Chinese page with English gaps rather than failing. That is what makes translating one
section at a time safe. Do not fill a gap with the English text to "complete" it — a section
counts as translated when the store has an entry, and 238 cheatsheet sections are an
LC-titled heading over a code block whose correct translation *is* the English text. Those
are real entries; a copied paragraph of English prose is not.

### 5. Tidy and refresh

```bash
node script/zh.js sync <id>          # drops the parked copies you adapted
node script/zh.js status --write     # regenerates both progress docs
```

The progress docs are **generated** — never hand-edit `doc/cheatsheet-zh-progress.md` or
`doc/faq-zh-progress.md`.

### 6. Gate

```bash
SKIP_FONTS=1 bash site/build.sh
node site/e2e-check.js _site
npm test --prefix site
```

The build is where a dropped `<!--CODE-->` marker surfaces (`compose` throws) and where a
retargeted link that went dangling is caught. Then look at the composed page — the 中文 / EN
button in the navbar swaps between counterparts, and `cheatsheets.zh.html` /
`faqs.zh.html` are the ways in.

### 7. Report

Close with the document, sections translated over sections owed, how many parked entries
were adapted versus written fresh, the new coverage line from `status`, and anything left
deliberately in English.

## Do not

- ❌ drop, add or reorder a `<!--CODE-->` marker (directive 1)
- ❌ add or remove a section, or reorder headings (directive 2)
- ❌ translate a link's anchor target (directive 3)
- ❌ translate an API, class or command name, or an LC problem title (directive 4)
- ❌ run `sync --prune` to tidy — it discards work that was kept on purpose
- ❌ hand-edit either progress doc; they are generated by `status --write`
- ❌ paste English prose into a section to make the percentage move
- ❌ start a campaign the user did not ask for
- ❌ commit or push unless asked

## Worked example

`/lc-zh-translate dp_loop_order` — one of the four sheets still at 0%:

| Step | What it produced |
|---|---|
| 1 | `status` → `dp_loop_order: 0/33`, one of four sheets untouched (`patience_sorting` 0/24, `math_logic_puzzles` 0/14, `memory_constrained_algorithms` 0/14) |
| 2 | `sync` → nothing parked; the document had no overlay at all |
| 3 | `todo` → 33 sections with their keys, starting at `4e9c8ca0cfb6` (the H1 and its Scope line) |
| 4 | wrote all 33 into the overlay file the id maps to (`i18n/zh/` + the id + `.md`); the ⭐⭐⭐⭐⭐ run on *0-1) The rule, in one line* kept verbatim; `dp.md#template-1b-prefix-partition-dp---lc-139` left as the English anchor |
| 5 | `sync` clean; `status --write` → cheatsheets 5171/5318 |
| 6 | build clean, `compose` accepted every marker, e2e 80/80 |
| 7 | flagged: the 5 LC-titled headings over code blocks are entries whose Chinese *is* the English title, per house rule |
