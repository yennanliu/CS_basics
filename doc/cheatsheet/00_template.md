# Cheatsheet Template

Two skeletons are in use. **Pick by size, not by topic.**

| | **Skeleton A — short doc** | **Skeleton B — reference doc** |
|---|---|---|
| Use when | < ~800 lines; one technique, few patterns | > ~800 lines; a data structure or a whole pattern family |
| Examples | `kadane_algorithm.md`, `n_sum.md`, `iterator.md`, `stock_trading.md` | `heap.md`, `dp.md`, `bfs.md`, `graph.md`, `binary_search.md` |
| Shape | `0) Concept` → `1) General form` → `2) LC Example` | `Overview` → `Problem Categories` → `Templates & Algorithms` → `LC Examples` → `Problems by Pattern` → `Pattern Selection Strategy` → `Summary` |

Do **not** mix the two in one file. If a Skeleton A doc grows past ~800 lines, convert it to B rather than bolting B's sections onto the end.

---

## Every cheatsheet starts with this header

```markdown
# <Topic Name>

> **Scope** — <one sentence: what this file owns, and what it deliberately does not>.
> **See also**: [other.md](./other.md) — <why you'd go there>; [third.md](./third.md) — <why>.

## LeetCode Problem Lists

<!-- One bullet per relevant LC topic tag, most relevant first.
     Slugs must be real LC topic tags — verify with:
       python3 script/add_lc_problem_lists.py --verify -->
- [Topic Name](https://leetcode.com/problem-list/topic-slug/)
```

Both lines are **mandatory on every file** — `node site/build-site.js` fails without them:

- The **H1** is the page title on the site and the card title on `cheatsheets.html`. Write a name a reader would recognise ("Dynamic Programming (DP)"), not a filename echo ("Dp").
- The **Scope** line keeps two files from silently growing into the same doc, *and* is lifted verbatim into the sheet's card description on the index. One sentence of plain prose, no lead-in.
- The **See also** line is what stops a reader landing on the wrong file of an overlapping family (`tree` vs `tree2` vs `binary_tree` vs `bst`).

---

## Then register the file

Every `doc/cheatsheet/*.md` needs an entry in [`data/cheatsheet_meta.json`](../../data/cheatsheet_meta.json). The build **throws** if one is missing, so nothing can land in an unsorted bucket:

```json
"my_topic": { "category": "Arrays & Strings", "tier": 4, "title": "Optional override" }
```

| Field | Meaning |
|---|---|
| `category` | Must be one of that file's `categoryOrder` entries. |
| `tier` | FAANG interview weight: `5` must-know, `4` high value, `3` worth knowing, `2` niche. Drives the card's stars, its ordering inside the category, and its emphasis stripe. |
| `title` | Only when the H1 is too long or too literal for a card. |
| `kind` | `"stub"` for a redirect file, `"reference"` for an imported index. Omit for a normal sheet. |

Add it to `startHere` only if it belongs in the beginner reading ladder.

---

## Mark the interview-critical sections

A trailing `⭐` run on a heading says how interview-critical that section is. The site strips the run out of the heading text, renders it as a star badge, weights the heading's left rule by it, and pulls 4★/5★ `h4`s into the page's table of contents.

```markdown
### Template 4: 0/1 Knapsack — LC 416 ⭐⭐⭐⭐⭐
### Template 7: Pairwise D&C Merge (k-way) — LC 23 ⭐⭐⭐⭐
```

- Put the run **on the heading**, not in the prose beneath it — only headings are picked up.
- `⭐⭐⭐⭐⭐` = write it from memory; `⭐⭐⭐⭐` = expect it; `⭐⭐⭐` = a known variant.
- A tier-4 or tier-5 sheet should mark roughly **3–8 sections**. Leave background and reference sections unmarked — if everything is starred, nothing is.

---

## Skeleton A — short doc

```markdown
## 0) Concept

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) <Problem Name> — LC <number>
```

---

## Skeleton B — reference doc

```markdown
## Time Complexity          <!-- data-structure docs only; omit for algorithm docs -->

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| <Name>         | O(...)   | O(...)   | O(...)   | O(...)   |

> One-line caveat: what the table hides (amortised? average vs worst? space?).
> This note is the ONLY place complexity is stated in the header — do not repeat
> it as a bullet under Key Properties.

## Overview

<one paragraph>

### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
- **Core Idea**: ...
- **When to Use**: ...

### References
- [Name](url)

## Problem Categories

#### **Pattern 1: <Name>** — LC <number>
- **Description**: ...
- **Examples**: LC ..., LC ...
- **Pattern**: ...

## Templates & Algorithms

### Template Comparison Table

| Template | Use Case | Complexity | When to Use |
|---|---|---|---|

### Template 1: <Name> — LC <number>

## LC Examples

### 2-1) <Problem Name> — LC <number>

## Problems by Pattern

## Pattern Selection Strategy

## Summary & Quick Reference
```

### Section rules for Skeleton B

- **`LC Examples` is not an append-log.** Before adding a problem there, check whether a template above already solves it *in the same language*. If it does, extend the template instead — do not restate the problem.
- **No `Missing Google Patterns`-style catch-all sections.** New material goes under the pattern it belongs to. If it fits nowhere, it needs its own named `##` section.
- Every `##` heading is unique within the file, and every `###` under a shared parent is unique too. If you need "Summary" twice, qualify both.

---

## Formatting rules

- **Bold** key terms: `**Pattern**`, `**Key Idea**`, `**Recurrence**`
- Category headers: `#### **Category Name**`
- Code blocks: **always** tag the language — `java`, `python`, or `text` for ASCII traces, diagrams and program output. Never leave a bare ` ``` `.
- Complexity: inside code as the first comment — `// time = O(...), space = O(...)`
- Images: `<p align="center"><img src="../pic/filename.png"></p>`
- Priority markers: `⭐⭐⭐⭐⭐` for critical/frequently-tested patterns
- Heading levels never skip: an `h2` is followed by `h3`, not `h4`
- State each LC number **once** per heading — `### 2-1) Top K Frequent Elements — LC 347`, not `### 2-1) Top K Frequent Elements (LC 347) — LC 347`

## Code conventions

- Open with `// IDEA: brief description` (Java) or `# IDEA: ...` (Python)
- Provide both Java and Python implementations when applicable
- Label each block: `// java` / `# python`
- Include `// LC <number> - Problem Name` above the class/function
- **One canonical solution per problem.** A second variant needs a reason stated in a comment (different complexity, different language idiom, teaches a distinct trick) — not just a different spelling of the same loop.

## Common section patterns

| Pattern | Use |
|---------|-----|
| Quick Decision Table | At section start — maps goal → template → examples |
| Template Comparison Table | Side-by-side comparison of loop conditions / update rules |
| Similar Problems Table | Group related LC numbers with key differences |
| Visual Trace | ASCII walkthrough of algorithm steps on a concrete example (tag it ` ```text `) |
| Decision Matrix | `Minimize vs Maximize`, `Memoization vs Tabulation`, etc. |
