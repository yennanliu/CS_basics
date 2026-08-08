# Site Review — Improvement Opportunities

A review of the GitHub Pages site (`yennanliu.github.io/CS_basics` → `yennj12.js.org/CS_basics`),
covering the build scripts (`site/build-site.js`, `site/build-leetcode.js`), `site/style.css`,
and the Pages deploy workflow.

**Reviewed:** 2026-08-08 · **Status:** findings only, no code changed.

---

## Table of Contents

1. [Page weight](#1-page-weight--the-biggest-problem)
2. [The four LeetCode pages](#2-the-four-leetcode-pages-are-a-separate-disconnected-site)
3. [Build & deploy correctness](#3-builddeploy-correctness)
4. [SEO & sharing](#4-seo--sharing)
5. [Search](#5-search)
6. [Navigation & layout](#6-navigation--layout)
7. [Accessibility](#7-accessibility)
8. [Small polish items](#8-small-polish-items)
9. [Top 3 priorities](#top-3-priorities)

---

## 1) Page weight — the biggest problem

| Page | Served size |
|---|---|
| `index.html` (homepage) | **630 KB** |
| `cheatsheets/dp.html` | **554 KB** |
| `data/lc-problems.json` | **433 KB** (fetched on every `search.html` load) |

The homepage is `README.md` (371 KB of markdown) rendered wholesale into one HTML file. On mobile
that is a multi-second parse before anything is interactive, and the reading-progress bar becomes
meaningless. Several cheatsheets are 100–250 KB of markdown each and land in the same bucket:

```
243179  doc/cheatsheet/dp.md
194805  doc/cheatsheet/tree.md
194022  doc/cheatsheet/lc_category.md
190846  doc/cheatsheet/dfs.md
140823  doc/cheatsheet/bfs.md
```

**Ideas**

- Give the homepage a real landing page (hero + counts + category cards + "recently updated") and
  move the README dump behind its own route.
- Split the mega-cheatsheets by `## ` section into sub-pages, with the current page as a hub.
- Lazy-load `lc-problems.json` on first keystroke rather than at page load.

---

## 2) The four LeetCode pages are a separate, disconnected site

`lc-explorer`, `lc-similar`, `lc-random-picker`, and `lc-review-plan` are hand-maintained static
files checked into `_site/` — [`site/build-leetcode.js:10-12`](../site/build-leetcode.js) says so
explicitly. Consequences:

- **Their nav is missing `cheatsheets`, `patterns`, `faqs`, and `visualizer`.** Land on
  `lc-explorer` from a cheatsheet and you can only get back via Home — a genuine navigation trap.

  | Page | Nav links present |
  |---|---|
  | generated pages | home, search, cheatsheets, patterns, faqs, lc-explorer, lc-similar, lc-random-picker, lc-review-plan, visualizer |
  | the four `lc-*` pages | home, search, lc-explorer, lc-similar, lc-random-picker, lc-review-plan |

- **No theme-toggle button** on any of the four. They read `localStorage.theme` but give the user
  no way to change it, so a light-mode reader who arrives here first is stuck in dark.
- They do not link `style.css`; each carries a large inline `<style>` block
  (448 / 198 / 222 / 260 lines ≈ 1,100 lines duplicated). Any nav or palette change means editing
  five places.

**Idea:** have `build-site.js` inject the shared nav/footer/theme script into these pages at build
time, even if their bodies stay hand-written.

---

## 3) Build/deploy correctness

- **`_site/` is committed** (347 tracked files). Re-running `node site/build-site.js` right now
  dirties **73 tracked files** and produces one page that is not committed at all
  (`_site/faqs/backend_llm_tool_idempotency.html`). Generated output in git means noisy diffs and a
  checked-in copy that is already stale. Only the four hand-written LC pages need to live in the repo.
- **The Pages workflow's `paths-ignore` excludes `leetcode_python/**` and `leetcode_java/**`** — but
  `build-leetcode.js` scans exactly those directories to cross-link each problem to its solution
  file. Adding a new solution never triggers a rebuild, so the explorer's Java/Py links go stale
  until some unrelated doc change happens to land.
- **`https://yennanliu.github.io/CS_basics/` 301s to `http://yennj12.js.org/...`** — plain HTTP,
  even though HTTPS works fine on that host. Every visitor takes an insecure hop plus an extra
  redirect.
- **The 404 page's "Go Home" points to `href="/"`**, which is the js.org domain root, not
  `/CS_basics/`. It also drops the navbar entirely.

---

## 4) SEO & sharing

- `sitemap.xml` and `robots.txt` both **404**. ~350 pages, no sitemap.
- **Identical `<meta name="description">` on every page** — the same string for the DP cheatsheet,
  the Kafka FAQ, and the homepage.
- **No `og:` / `twitter:` tags and no `<link rel="canonical">`.** Links shared to Slack, Twitter, or
  LinkedIn render as bare URLs, and the github.io ↔ js.org duplication has nothing telling search
  engines which is canonical.

---

## 5) Search

`search.html` is decent but thin:

- The index stores **only titles and the first 40 headings** — no body text. Searching "Kadane" or
  "monotonic" only hits if the term is in a heading. Prose and code are invisible.
- **AND-only, substring matching** (`score()` requires every token present). No fuzzy matching, no
  typo tolerance, no result snippets or match highlighting — you get a title and have to guess why
  it matched.
- No `/` or `⌘K` shortcut to focus the box, no recent searches, no per-type filter.
- The **cheatsheets and FAQs index pages have no filter box at all** — 75 and 43 cards respectively,
  scroll-only. A ~20-line client-side filter would help a lot.

---

## 6) Navigation & layout

- **Prev/next is global alphabetical**, so `binary_search` → `binary_tree` → `bit_manipulation`
  jumps across unrelated topics. Scoping it to the card's own category would read much better.
- **The TOC is not sticky** and only covers `h2`/`h3`. On a 550 KB DP page you scroll back to the top
  every time you want to jump. A sticky sidebar TOC with scroll-spy is the single biggest UX win for
  the long pages.
- **10 nav items + toggle + GitHub link** is a lot for the ≤1024px breakpoint before the hamburger
  kicks in at 768px. The four `lc-*` entries could collapse into one "leetcode" dropdown.
- No breadcrumbs on `patterns.html` (cheatsheet and FAQ pages have them).
- No "back to top" button on the long pages.

---

## 7) Accessibility

- **No skip-to-content link.**
- The hamburger button has `aria-label` but **no `aria-expanded`** state, so screen readers cannot
  tell whether the drawer is open.
- **8 of 9 homepage images have no `alt`** — all the Big-O charts. None use `loading="lazy"` either.
- **The site defaults to dark and never checks `prefers-color-scheme`** (`style.css` has zero
  occurrences). A light-OS user gets dark until they find the toggle.
- Copy buttons have no `aria-label`; the status change ("Copy" → "copied") is not announced.

---

## 8) Small polish items

- **Copy button label drifts**: rendered as `Copy`, but the JS resets it to lowercase `copy` after
  2s — so it silently changes case the first time you use it.
- **The favicon is a `$` glyph with `fill='white'`** — invisible against a light browser tab bar.
- The scroll listener for the progress bar is not `{passive: true}` and fires on every scroll event
  without rAF throttling.
- `lc-explorer` shows no result count / "showing N of M" feedback after filtering.
- No "last updated" or content-freshness signal on the index pages (individual pages have it).

---

## Top 3 priorities

1. **A real landing page + split the mega-cheatsheets** — fixes [§1](#1-page-weight--the-biggest-problem).
2. **Inject the shared nav into the four LC pages** — fixes [§2](#2-the-four-leetcode-pages-are-a-separate-disconnected-site)'s
   navigation trap and theme gap.
3. **Stop committing `_site/` and fix the `paths-ignore` staleness** — fixes [§3](#3-builddeploy-correctness).
