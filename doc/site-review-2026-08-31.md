# Site Review — 2026-08-31

A second pass over the GitHub Pages site, three weeks after
[`site-review.md`](./site-review.md). That review was findings-only; this one
records what was **found and fixed**, and what the fixes changed about how the
build works.

**Reviewed:** 2026-08-31 · **Status:** all findings below are closed.

The trigger was a check on PR #52 (`feat/site-improvements`). That branch turned
out to be 603 commits behind `master` and its proposals were already merged — but
two of its ideas had never landed, and chasing them turned up a wider set of
problems.

---

## Table of Contents

1. [The review plan was four months stale](#1-the-review-plan-was-four-months-stale)
2. [~120 dead links inside the cheatsheets](#2-120-dead-links-inside-the-cheatsheets)
3. [Pages missing the shared chrome](#3-pages-missing-the-shared-chrome)
4. [Wide tables were unreachable on mobile](#4-wide-tables-were-unreachable-on-mobile)
5. [Two thirds of the image payload was never referenced](#5-two-thirds-of-the-image-payload-was-never-referenced)
6. [The homepage was a README dump](#6-the-homepage-was-a-readme-dump)
7. [SEO and sharing](#7-seo-and-sharing)
8. [One external CDN dependency](#8-one-external-cdn-dependency)
9. [Five copies of the palette](#9-five-copies-of-the-palette)
10. [Accessibility](#10-accessibility)
11. [What CI checks now](#what-ci-checks-now)
12. [Status of the 2026-08-08 review](#status-of-the-2026-08-08-review)

---

## 1) The review plan was four months stale

`site/pages/lc-review-plan.html:365` held the practice log as a `const RAW`
template literal — a paste of `data/progress.txt`, frozen at **2026-05-02** while
the log itself ran to **2026-08-31**. Nothing in `build.sh` regenerated it, so the
spaced-repetition schedule was missing about 120 days of practice. The paste also
stripped every status annotation: `139(again!!)` and `139(ok)` arrived at the page
as the same bare `139`.

**Fixed** by `site/build-review-plan.js`, which compiles the log to
`_site/data/progress.json` on every build; the page fetches it. The annotations
survive, so the page can say which problems keep coming back:

- a **Marked "again"** stat card, and an `again` chip on every row that has one;
- within a due date, the problems with the most `again` marks sort first;
- an **only "again"** filter and a **Most "again"** sort on the All Problems table.

The parser is deliberately forgiving, because the log is written by hand at the
end of a session. `site/test/build-review-plan.test.js` pins each shape it has to
survive against the real file — wrapped lines, `|` between sessions, `(again, 2
pointers)`, `70(ok*, o(1) space!!)`, `39(again*).79(again*)`, a bare `,53(again)`
continuation, separator rules, and the 24 dates the log lists twice.

Numbers after the change: **801 practice days, 784 problems, 2,849 attempts, 281
problems still marked `again`**.

## 2) ~120 dead links inside the cheatsheets

Link rewriting was pattern matching on the href text: a `./x.md` inside a
cheatsheet became `x.html` and everything else became a GitHub URL. That covered
the `./`-prefixed sibling link the style guide asks for, and nothing else — so
every link written as a bare `design.md` (14×), `hash_map.md` (10×),
`python_trick.md` (8×), a cross-tree `../faq/java/faq_OOP.md`, or a stale
`kadane_algo.md` shipped as a dead end. 31 pages, ~120 links.

CI could not see any of it: its link scan covered six hardcoded root pages, and it
warned rather than failed.

**Fixed** by resolving against a registry of the pages the build actually
produces. A `.md` link is a local page exactly when its target is built, and a
GitHub URL otherwise — see *Where a `.md` link goes* in `CLAUDE.md`. A typo now
lands on GitHub, where it 404s visibly, instead of becoming a dead link inside the
site.

Also fixed: `doc/cheatsheet/set.md` referenced `doc/pic/set_operations.png`, an
image that has never existed in the repo, and `doc/faq/flink/faq_flink.md`
referenced `checkpoint3.png` where the file is `Checkpoint3.png` — invisible on a
case-insensitive laptop, a broken image on the case-sensitive server.

## 3) Pages missing the shared chrome

Five tool pages had no `<footer>`. `404.html` had no navbar, no footer, no theme
attribute, an absolute `/CS_basics/style.css` that could not be previewed locally,
and a "Go Home" button pointing at `/` — which on `yennanliu.github.io` is
somebody else's site.

The 404 has a second problem the others do not: GitHub Pages serves it for a
request to **any** unmatched path, and the browser keeps that path. A request for
`/CS_basics/cheatsheets/typo.html` resolved `nav.js` against
`/CS_basics/cheatsheets/`, so the page loaded with no navbar and a
`CSNav.mount()` that threw.

**Fixed**: the tool pages take the shared footer; the 404 was rewritten with a
`<base>` computed before the first asset tag, which pins every relative href on the
page to the site root at any depth.

## 4) Wide tables were unreachable on mobile

`style.css` defines `.table-wrap { overflow-x: auto }` — and nothing ever emitted
it. Combined with `table { min-width: 400px }` and `body { overflow-x: hidden }`,
a table wider than the viewport was not merely squashed on a phone: its right-hand
columns were **clipped, with no way to scroll to them**. The README page alone
renders 55 tables, several of them eight columns wide with a tag column full of
company names.

**Fixed** in the markdown pipeline; the CSS was already there. `e2e-check.js`
fails if a generated table ships unwrapped.

## 5) Two thirds of the image payload was never referenced

`build.sh` copies `doc/pic` wholesale, because the pages that reference it do not
exist yet. Of the 82 MB that landed in `_site`, **62 MB was referenced by no page
at all** — 116 files including four single images over 2.5 MB each. It shipped to
every CDN edge on every deploy.

No image anywhere carried `loading="lazy"` or intrinsic dimensions, so a page like
`cheatsheets/binary_tree.html` blocked on 2.2 MB of diagrams before it could
finish painting, then jumped around as they arrived.

**Fixed**: `site/prune-images.js` deletes the unreferenced images from `_site`
after the build (`doc/pic` in the repo is untouched — those files are still
correct for anyone reading the markdown on GitHub), and the pipeline adds
`loading="lazy"`, `decoding="async"` and width/height read from the file header.

`_site` went from **118 MB to 51 MB**.

## 6) The homepage was a README dump

`index.html` was `README.md` rendered straight through: **915 KB**, 55 tables,
1,512 problem rows, no sign that the site has a search, a roadmap, a review plan
or 36 visualizers. It also inherited eight broken links, because README's relative
repo paths were not rewritten.

**Fixed**: a landing page at `index.html` (**6.6 KB**) — hero, counts, one card
per tool — with README keeping its own page at `problems.html`, linked from the
landing page and the nav.

Every number on it is read from the source files at build time. A typed "1,300+"
is a number that goes stale the first week nobody re-checks it.

## 7) SEO and sharing

No `sitemap.xml`, no `robots.txt`, no `og:`/`twitter:` tags, no
`<link rel="canonical">`. All 352 pages shared one identical
`<meta name="description">` — the same sentence for the DP cheatsheet, the Kafka
FAQ and the homepage. The 130 zh/en pairs had no `hreflang`, so a crawler had to
guess they were translations rather than duplicates.

**Fixed**: per-page descriptions come from the summary each page family already
computed for its index card (the Scope line for a cheatsheet, the lead paragraph
for an FAQ) — 347 distinct descriptions across 352 pages. Canonical, Open Graph,
Twitter and hreflang tags are emitted by the template, and
`site/finalize-pages.js` fills in the hand-written and copied pages afterwards,
then writes the sitemap from the whole tree.

## 8) One external CDN dependency

`lc-similar.html` pulled `https://d3js.org/d3.v7.min.js` — the site's only
third-party runtime dependency, unpinned, without an integrity hash, on a page
that is unusable if the request fails. Everything else (fonts, highlight.js) is
vendored and the build never touches the network.

**Fixed**: `d3@7.9.0` is a pinned devDependency, vendored into `_site/vendor/` by
`build.sh`. `e2e-check.js` fails on any external `<script>`.

## 9) Five copies of the palette

`lc-explorer`, `lc-similar`, `lc-review-plan`, `lc-random-picker` and
`lc-complexity-quiz` each carried their own `:root` / `[data-theme="dark"]` block.
They had already drifted — `--border` was `#1f1f1f` on three and `#222222` on a
fourth. This was also the root cause of the missing footers: the footer lived in
the generated template none of them use.

**Fixed** by `site/lc-page.css`, the *overlap* only — palette, footer. They still
cannot take `style.css` wholesale, which sets a body font, a container width and a
full table treatment their bespoke layouts do not want.

## 10) Accessibility

No skip-to-content link anywhere. No `prefers-reduced-motion` guard, despite
`scroll-behavior: smooth`, the reading-progress bar, the nav drawer and the theme
transition. No keyboard route to search.

**Fixed** in `nav.js`/`nav.css`, which every page family loads: the navbar renders
a skip link as its first child, and `mount()` labels whatever holds the page
content as `#main` so the link has a target. `/` and `⌘K`/`Ctrl-K` reach search
from any page, and stay out of the way while the reader is typing.

---

## What CI checks now

The ~280-line validator that lived as a heredoc inside `validate-pages.yml` is
now `site/e2e-check.js` — runnable locally, reviewable, and run by **both**
workflows, so a push that skips the validate workflow's path filter still cannot
publish a broken tree.

The important difference is coverage. The old one checked a hardcoded list of six
root pages; every problem in §2, §3 and §4 above was on a page that was not on it.
The new one walks all 352, and errors where the old one warned.

It also tests the shipped artefact rather than a copy of its logic: search's
`score()` is lifted verbatim out of the built `search.html` and run against the
built index.

**Result: 59 checks, 0 failures.** Add a site-wide rule there, not in a workflow.

## Status of the 2026-08-08 review

| [Earlier finding](./site-review.md) | Status |
|---|---|
| §1 Page weight — landing page | Fixed here — 915 KB → 6.6 KB |
| §1 Split the mega-cheatsheets | Fixed earlier on `master` |
| §2 LC pages: nav, theme toggle | Fixed earlier on `master` (`nav.js`) |
| §2 LC pages: duplicated palette | Fixed here (`lc-page.css`) |
| §3 `_site/` committed | Fixed earlier on `master` |
| §3 `paths-ignore` staleness | Fixed earlier on `master` |
| §3 404 page: "Go Home", no navbar | Fixed here |
| §4 sitemap / robots | Fixed here |
| §4 identical descriptions | Fixed here |
| §4 `og:` / `twitter:` / canonical | Fixed here |
| §5 `/` or `⌘K` shortcut | Fixed here |
| §5 index filter box | Fixed earlier on `master` |
| §6 prev/next scoped to category | Fixed earlier on `master` |
| §7 skip-to-content link | Fixed here |
| §7 `aria-expanded` on the hamburger | Fixed earlier on `master` |
| §7 homepage images: `alt`, `loading` | Fixed here |

**Still open**, and worth a later pass:

- **Search indexes titles and headings only** (§5) — prose and code are invisible,
  matching is AND-only substring with no snippets or highlighting.
- **`search.html` fetches 900 KB of JSON on load** (§1) — it could wait for the
  first keystroke.
- **The TOC is not sticky** (§6), and there is no back-to-top on the long pages.
- **The site defaults to dark and never reads `prefers-color-scheme`** (§7).
- **`https://` → `http://yennj12.js.org` redirect** (§3) — a DNS/Pages setting,
  not a build one.
- **Copy button label case drift and a non-passive scroll listener** (§8).
