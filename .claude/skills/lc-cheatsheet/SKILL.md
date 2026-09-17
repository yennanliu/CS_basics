---
name: lc-cheatsheet
description: File a LeetCode problem into the cheatsheets under doc/cheatsheet/ the way the existing sections are written — find the sheet whose Scope line owns the topic, file it under the pattern it belongs to (a worked example, a variation on an existing template, a new pattern section, or a new sheet), smoke-test every snippet, fix the heading anchors the edit moved, and re-translate the 中文 sections it invalidated. Use when asked to add or update a cheatsheet, to document a pattern just learned, to add an LC example or a template variation to a sheet, or to start a new cheatsheet. Triggers - "add LC 1650 to binary_tree as a variation", "/lc-cheatsheet 239 into sliding_window", "document the monotonic stack pattern", "update the LCA part of the binary tree cheatsheet".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add a LeetCode problem to a cheatsheet

Turn a problem (or a pattern just learned) into a cheatsheet section in the shape the other
~130 sheets under `doc/cheatsheet/` already use — filed under the pattern that owns it, with
every snippet run, every moved anchor followed, and the 繁體中文 overlay kept level.

**Invocation**: `/lc-cheatsheet <LC number> [into <sheet-slug>] [as <mode>]`

```text
/lc-cheatsheet 1650 into binary_tree as variation
/lc-cheatsheet 239 into sliding_window          # mode inferred -> example
/lc-cheatsheet 496 as pattern                   # sheet inferred from the technique
/lc-cheatsheet monotonic_queue as sheet         # a whole new file
add LC 1650 to the LCA part of binary_tree.md   # the plain-English form works too
```

| `as <mode>` | Use when | Lands as |
|---|---|---|
| `example` *(default)* | the sheet has the template; this problem is another instance of it | a worked LC example **under that template's section** |
| `variation` | the sheet's template does not quite solve it — one precondition differs | a `#### <X> Variant — <what changed> (LC n)` under the template it varies |
| `pattern` | the sheet has no template for this technique at all | a new pattern/template section, plus its row in the sheet's category table |
| `sheet` | no sheet owns the topic | a new `doc/cheatsheet/<slug>.md` **and** its `data/cheatsheet_meta.json` entry |

Missing arguments are inferred, not asked about: the sheet from the technique the solution
uses and from `README.md`'s topic heading for that LC number, the mode from what the sheet
already contains (step 2). Ask only when two sheets both plausibly own it and their Scope
lines do not settle it.

**Precedence, when `into <slug>` and the Scope lines disagree.** Both are honoured, and they
are honoured differently — the named sheet is never ignored, and the Scope line still decides
where the depth lives:

| | |
|---|---|
| `into <slug>` | **always edited.** It is where you said you were working, so it gets the entry point: the row in its own table, the code a reader of *that* sheet needs, and the link onward |
| the Scope owner | **gets the full section**, if it is a different sheet. Depth belongs to the file whose Scope line claims the topic, or the two sheets grow into one document |

So `/lc-cheatsheet 1650 into binary_tree` edits `binary_tree.md` **and**
`tree_lca_distance.md`, and says so in the report. Nothing is silently redirected; a sheet you
named is never left untouched. When the named sheet *is* the Scope owner — the common case —
the two collapse into one edit.

## Prime directives

1. **The Scope line decides which sheet, not the topic name.** Every sheet opens with
   `> **Scope** — <what this file owns, and what it deliberately does not>`. Read the
   candidates' Scope lines before writing a line. That block exists precisely to stop two
   files growing into the same document.
2. **File it under the pattern it belongs to.** Never append an `LC Examples` section to the
   end that re-solves what a template above already solves, and never open a catch-all
   (`Missing Google Patterns`). Both are the Aug 2026 cleanup this repo already paid for —
   see `doc/cheatsheet-review-2026-08.md`.
3. **Untested code is not a cheatsheet entry.** Run every snippet added against the
   problem's own examples (step 6). A sheet is read as authoritative; a wrong snippet in one
   is worse than no snippet.
4. **A moved anchor is a broken site.** Changing a heading's text — including adding or
   changing its `⭐` run — moves its id. Every link aimed at it moves in the **same commit**
   (step 5).
5. **An English edit parks its Chinese translation.** The 中文 overlay is keyed per section,
   so an edited section silently falls back to English. Re-key it (step 7); do not leave the
   gap.

## The steps

### 1. Find the sheet that owns it

```bash
grep -rn "LC 1650\|1650\b" doc/cheatsheet/ | head -20        # who already mentions it
grep -n "^> \*\*Scope\*\*" doc/cheatsheet/{binary_tree,tree_lca_distance}.md
grep -n "^## \|1650" README.md | grep -B 2 1650               # README's topic heading
```

Read out of that:

- **Which sheet's Scope line claims the topic.** `binary_tree.md` owns "which direction DFS
  state flows, plus the structural templates"; `tree_lca_distance.md` owns "LCA, distance and
  path problems". An LCA variation's depth belongs to the second. A sheet named by `into`
  that is not the owner still gets its entry point (the precedence table above) — here
  `binary_tree.md`'s Template 6 gained the variations table, the LC 1650 code and the link
  onward.
- **Whether the problem is already there.** If it is, the job is `variation` or a correction,
  never a second copy — one canonical solution per problem.
- **Whether a mention is wrong.** A sheet claiming LC 1650 is solved by the LC 236 post-order
  template is a bug in the sheet: 1650 has no `root` to pass it. Fix what you find.

### 2. Pick the mode from what the sheet already has

```text
sheet has a template that solves it as-is      -> example
sheet has a template, one precondition differs -> variation   (say WHICH precondition)
sheet has no template for the technique        -> pattern
no sheet's Scope line claims the topic         -> sheet
```

A `variation` is the most common and the most valuable: it is the mode that teaches *which*
template to reach for, which is the thing a reader actually gets wrong under time pressure.

### 3. Read the neighbouring section before writing

```bash
grep -n "^### \|^#### " doc/cheatsheet/<slug>.md | head -40   # the sheet's own skeleton
sed -n '<start>,<end>p' doc/cheatsheet/<slug>.md              # the sibling section, whole
```

The sibling section **is** the template: its heading depth, whether it labels fences
`# python` / `// java`, whether complexity sits in a table or a comment, how it writes a
pitfall list. Match it. `doc/cheatsheet/00_template.md` is the authority when the sheet has
drifted or when the mode is `sheet`.

For mode `sheet`, pick the skeleton by **size, not topic** (CLAUDE.md § Which skeleton):
Skeleton A (`0) Concept` → `1) General form` → `2) LC Example`) under ~800 lines, Skeleton B
(`Overview` → `Problem Categories` → `Templates & Algorithms` → …) above it. Never mix them.

### 4. Write the section

House rules that are not negotiable:

- **Every fence is tagged** — ` ```python `, ` ```java `, ` ```text ` for traces, diagrams and
  program output. Never a bare ` ``` `.
- **`# IDEA: <one line>`** opens a snippet; complexity is the first comment,
  `# time = O(...), space = O(...)`, and `// LC <n> - <Problem Name>` sits above the class.
- **State each LC number once** per heading — not `... (LC 1650) — LC 1650`.
- **Heading levels never skip** (`h3` → `h4`, never `h3` → `h5`).
- **A `⭐`…`⭐⭐⭐⭐⭐` run goes on the heading**, not in the prose under it, and only where the
  section earns it. If everything is starred, nothing is.
- **One canonical solution per problem.** A second variant needs a stated reason — different
  complexity, a distinct trick, a different language idiom — not a different spelling of the
  same loop.

Then make it **reachable**, which is the half that gets forgotten:

| Mode | Also update, in the same sheet |
|---|---|
| `example` | the pattern's similar-problems / problem-list table |
| `variation` | the parent template's variations table, and the `Problem Categories` row if it now names a shape the variation does not use |
| `pattern` | `Problem Categories`, `Pattern Selection Strategy` and the decision tree at the end |
| `sheet` | the H1, the Scope line, `## LeetCode Problem Lists`, **and** a `data/cheatsheet_meta.json` entry (`category` from that file's `categoryOrder`, `tier` 2–5) — the build fails without it, on purpose |

A pointer from a second sheet is one table row plus a link, not a copy of the section.

### 5. Follow the anchors the edit moved

Heading ids come from `site/build-lib.js`'s `slugify`, which keeps the space before a `⭐`
run — so a star run leaves a **trailing `-`** on the id. Never hand-write an anchor:

```bash
node -e "console.log(require('./site/build-lib.js').slugify('The LCA family — pick the template by **what you are handed** ⭐⭐⭐⭐⭐'))"
#   -> the-lca-family--pick-the-template-by-what-you-are-handed-

grep -rn "#<the-old-anchor>" doc/ site/pages/       # every link that has to move with it
```

`e2e-check.js`'s dangling-`#fragment` rule is what catches a missed one, so run it (step 8)
rather than trusting the grep.

### 6. Smoke-test every snippet

```bash
python3 - <<'PY'
# build the structure the problem gives you (parent pointers, a BST, a linked list...),
# then run each snippet added, plus the edges, and assert the problem's own examples
PY
```

Variants in the same section must agree with each other. If a snippet disagrees with the
problem statement's example, the snippet is wrong — say so rather than adjusting the example.

### 7. Re-key the 中文 overlay

There is one markdown tree, the English one; `i18n/zh/<slug>.md` is a sparse overlay keyed per
section, so the sections just edited go missing from it:

```bash
node script/zh.js sync <slug>       # park the translations the edit invalidated
node script/zh.js todo <slug>       # the sections needing one, keys included
#   adapt each parked translation into a live `<!-- key -->` entry in i18n/zh/<slug>.md,
#   keeping every <!--CODE--> marker it was given, in order
node script/zh.js sync <slug>       # tidy; delete the parked copies you used
node script/zh.js status --write    # refresh the generated progress docs
```

Compare `todo` counts before and after the edit: the sheet must not end up with **more**
untranslated sections than it started with. Keep LC titles in English — that is the house
rule, so an LC-titled heading's correct translation is the English text.

### 8. Verify the way CI does

```bash
python3 script/check_skills.py            # only if a skill or its page changed
SKIP_FONTS=1 bash site/build.sh           # the whole _site tree, offline
node site/e2e-check.js _site              # the gate: anchors, links, descriptions, tables
npm test --prefix site                    # the unit tests over the built artefacts
```

A build failure on a new sheet is usually the missing `cheatsheet_meta.json` entry; an
`e2e-check` failure after a heading edit is usually step 5.

### 9. Report what was assumed

Close with: **every sheet edited** and the heading it landed under in each — naming both when
the precedence rule split the work, so the reader never has to go looking for where a section
went — the mode and **why that mode**, the snippets run and their output, every anchor that
moved, the zh sections re-translated, and each inference (the sheet chosen when two could have
owned it, a complexity stated for a snippet whose bound was not in the source).

## Do not

- ❌ append an `LC Examples` section that re-solves what the templates above already solve
- ❌ open a catch-all section instead of filing under the pattern it belongs to
- ❌ add a second solution for a problem already in the sheet without a stated reason
- ❌ leave a fence untagged, or duplicate complexity in both a table and a bullet
- ❌ hand-write a heading anchor (step 5)
- ❌ copy a section into a second sheet — cross-reference it
- ❌ paste practice data or touch `data/progress.txt` — that log is the user's own record
- ❌ commit or push unless asked

## Worked example

`/lc-cheatsheet 1650 into binary_tree as variation`, from
`leetcode_python/Depth-First-Search/lowest-common-ancestor-of-a-binary-tree-iii.py`:

| Step | What it produced |
|---|---|
| 1 | **both sheets**, per the precedence rule: `binary_tree.md` was named, so it gets the entry point (Template 6's variations table, the LC 1650 code, the link onward); `tree_lca_distance.md`'s Scope line owns LCA, so the full section lands there. Found a bug on the way: the LC 236 template's header comment claimed it also solved 1650, which has no `root` |
| 2 | `variation` — 1650 breaks one precondition of the post-order template (no root, `parent` pointers instead) |
| 3 | matched the sibling `#### LCA Variant — Smallest Subtree with All Deepest Nodes` — `##### **1. Core Idea**`, a `text` trace, a pitfalls list |
| 4 | wrote the family picker table (236/235/1644/1650/1676), the set-of-ancestors and two-pointer templates, the step trace, the pitfalls; split the `Problem Categories` LCA row, since 1650 and 235 do not use the post-order template it named |
| 5 | `slugify` gave `#lca-variant--parent-pointers-no-root-lc-1650-` for the new cross-reference — trailing `-` from the star run |
| 6 | ran all five snippets against the LC examples plus a cross-tree pair (both pointers hit `null` together → `None`) |
| 7 | the edit parked 8 zh sections in a sheet that was 100% translated; re-keyed all 8, back to 0 todo |
| 8 | build, `e2e-check` (76 checks) and 418 unit tests green |
| 9 | reported both sheets and the heading in each, and flagged: `tree_examples.md` had LC 1676 labelled "LCA III … in forest after deletion"; it is LCA IV, N target nodes — fixed in the same pass |
