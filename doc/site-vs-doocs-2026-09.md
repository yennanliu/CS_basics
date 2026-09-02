# Site comparison: leetcode.doocs.org vs. this site (Sep 2026)

> **Scope** — what [leetcode.doocs.org](https://leetcode.doocs.org/en/) does that this
> site does not, and which of those are worth building here. Not a redesign proposal;
> a ranked list with the effort and the wiring each item would need.

## The two sites are different products

Read the gap list with this in mind, or you will copy the wrong things.

**doocs/leetcode is a solution encyclopedia.** ~3,700 problems, one page each, every
page carrying LeetCode's problem statement plus solutions in 8–14 languages
(Python3, Java, C++, Go, TypeScript, Rust, JS, C#, PHP, Swift, Kotlin…). It is built
with MkDocs Material, maintained by a crowd, and optimised for *lookup*: you arrive
from a search engine with a problem number and leave with code.

**This site is one person's training system.** 1,388 indexed problems, 129
cheatsheets, 43 FAQs, 37 visualizers, a prerequisite-ordered roadmap, a
spaced-repetition review plan compiled from a real practice log, a complexity quiz,
and OK/AGAIN status per problem. It is optimised for *practice scheduling*: you
arrive not knowing what to work on and leave with a queue.

Things this site has that doocs has no equivalent for, and should not trade away:

| | |
|---|---|
| Study roadmap | Prereq DAG, validated as a transitive reduction at build time |
| Review plan | Spaced repetition compiled from `data/progress.txt`, the single source |
| Complexity quiz | Snippet → time/space, graded by a real complexity parser |
| Visualizers | 37 step-through algorithm demos |
| Pattern recognition | Problem-statement keyword → technique table |
| Company + status tags | `google`/`fb`/…, `blind75`/`neetcode150`, OK/AGAIN with repeat counts |
| zh overlay i18n | Per-section overlay, not a forked tree |

So the question is never "should we become doocs". It is: doocs is much better at
*one* thing — being the place a specific problem's solution lives — and that is the
one thing this site currently does not do at all.

---

## Feature matrix

| Feature | doocs | here | Gap |
|---|---|---|---|
| Page per problem, stable URL | ✅ `/lc/1/` | ❌ | **the big one** |
| Solution code visible on the site | ✅ inline | ❌ links out to GitHub blobs | **big** |
| Multi-language solution tabs | ✅ 8–14 langs | ❌ | medium |
| Official topic tags + difficulty per problem | ✅ frontmatter → badges | ⚠️ have the data, not the page | medium |
| Tag index pages with their own URL | ✅ `/tags/#tag:array` | ⚠️ filters inside a JS page only | medium |
| Full-text search with section anchors + highlighted excerpts | ✅ | ⚠️ title/heading/summary match only | **big** |
| Prev/next through the whole sequence | ✅ | ⚠️ within cheatsheets only | small |
| Named solution approaches ("Solution 1: Hash Table") | ✅ | ⚠️ `// IDEA:` comments | small |
| Complexity typeset as maths | ✅ KaTeX | ⚠️ code comments | small |
| Contest archive | ✅ full section | ⚠️ 14 contest dirs in repo, **zero on the site** | medium |
| Comments per page | ✅ giscus | ❌ | see caveat |
| "Was this page helpful?" | ✅ | ❌ | low |
| Edit-on-GitHub link | ✅ | ✅ | — |
| Last-updated / contributors | ✅ both | ✅ last-updated | n/a (solo repo) |
| Dark/light toggle, code copy, TOC | ✅ | ✅ | — |
| Language switch EN/中文 | ✅ whole site | ⚠️ cheatsheets only | low |
| Book-mapped indexes (LCCI/LCOF/LCP) | ✅ | ❌ (have Blind75/NeetCode/Grind instead) | skip |
| Per-language formatting CI | ✅ 5 linters | ❌ | medium (repo, not site) |
| Roadmap / review plan / quiz / visualizers | ❌ | ✅ | — |

---

## Tier 1 — worth building

### 1. A page per problem

**What.** `lc/0015.html` (or `lc/15/`): title, difficulty badge, official topic tags,
this repo's Java and Python solutions rendered inline with the existing copy button,
the README `Note` column, the OK/AGAIN status with its repeat count, and outbound
links to leetcode.com and the GitHub blob.

**Why it matters here.** Today the site *indexes* 1,388 problems across
`problems.html`, `lc-explorer.html`, `search.html`, `lc-similar.html`,
`lc-random-picker.html` and the roadmap — and not one of them shows a single line of
solution code. Every code path exits to GitHub. That is the whole loop broken at the
last step: the review plan tells you to redo LC 31, and then hands you off to a raw
blob view with no note, no tags, no complexity, no back-link.

It is also the only structural change that makes the *other* tools better rather than
adding a sixth one. The roadmap, the review plan, the explorer, similar-problems and
the quiz all currently dead-end at a GitHub URL; they would all link to the same page
instead.

**Inputs already exist — none of this needs new content.**

| Source | Gives |
|---|---|
| `README.md` (1,469 table rows → 1,388 distinct problems) | number, title, LC URL, solution paths, time/space, difficulty, notes, status |
| `data/lc_topic_tags.json` (1,388 problems) | LeetCode's official topicTags + difficulty |
| `data/company_lc_tags.json` | which companies ask it |
| `data/problem_lists.json` | Blind75 / NeetCode 150/250 / Top 100 Liked membership |
| `leetcode_python/**` (2,869 files), `leetcode_java/**` (1,626), `leetcode_SQL/**` (166), `leetcode_scala/**` (7) | the code |
| `data/progress.txt` | when it was last practised, and how it went |

**Effort.** A `site/build-problems.js` of roughly the shape of `build-roadmap.js`
(~400 lines), plus a template. `build-leetcode.js` already has the
filename→problem normalisation (`normalizeKey`, `buildSolutionMap`) that the
code-file lookup needs; lift it into `build-lib.js` rather than writing it twice.

**Watch out for.**

- **Do not copy LeetCode's problem statement.** doocs pastes LeetCode's own
  description HTML onto every page. That is LeetCode's copyrighted text, and the
  reason doocs can be pressured about it. Link out for the statement; own the
  solution, the note and the complexity. This costs nothing pedagogically — you
  already have the problem open when you practise.
- **Scale, but less than you would fear.** Measured on this machine today: a full
  `SKIP_FONTS=1 bash site/build.sh` is **3.2 s** and produces 474 files / 352
  sitemap URLs; `node site/e2e-check.js _site` is **0.25 s** for 59 checks over
  every one of those pages. Adding ~1,400 problem pages takes the sitemap to ~1,750
  and, extrapolating linearly, e2e-check to a bit over a second. Neither is a
  reason not to do it — but `prune-images.js` and `finalize-pages.js` also walk the
  whole tree, so re-measure once it lands rather than assuming.
- **A problem with no local solution file** should still get a page (the note and
  tags are useful) but must not render an empty code block — `build-leetcode.js`
  already has the "no solution found" branch to copy.

### 2. Solution code, with language tabs

**What.** A tabbed block — Java | Python | SQL | Scala — used on the problem pages and
reusable in cheatsheets, where the Java/Python pairs are currently two stacked fences.

**Why.** doocs's tabs are the single most-used control on the site. Here the payoff
is smaller (2 languages, not 14) but real: a cheatsheet section with a Java and a
Python block costs a screen and a half of scrolling; tabbed it costs one.

**Effort.** Small — a `<div class="tabs">` + ~30 lines in `site.js`, alongside
`copyCode`. Must degrade to stacked blocks with JS off, since `e2e-check.js` will not
let a page depend on an external script and the existing blocks are already
progressive.

**Do not** chase doocs's language count. Go/Rust/C++ solutions for 1,400 problems is
content this repo does not have and does not need for a Google L3 bar.

### 3. Real full-text search

**What.** Ours matches title + category + summary + heading text and returns whole
pages. Material's returns **`page › section`** hits with a highlighted excerpt around
the match, deep-linked to the anchor, and opens on `/`.

**Why.** With 129 cheatsheets averaging several hundred lines, "which sheet mentions
Boyer-Moore" is currently unanswerable on the site — the term is in the body, and the
body is not indexed. That is the query a study site exists to answer.

**Effort.** Medium. `build-site.js` already collects headings per page for the TOC;
extend `data/search-index.json` from one record per page to one per H2/H3 with its
body text, then add snippet extraction, `<mark>` highlighting and the `/` shortcut.
Index size needs watching — cap the stored body per section.

**Good news:** `e2e-check.js` already lifts `score()` verbatim out of the built
`search.html` and runs it against the built index, so the regression harness for this
change exists before the change does.

### 4. Tag pages with their own URL

**What.** `tags/two-pointers.html`: the problems carrying the tag, the cheatsheets
that own the pattern, the roadmap node it sits on, and the OK/AGAIN split for it.

**Why.** doocs gives every tag a URL. Here, tag filtering lives inside
`lc-explorer.html`'s JS — so a tag cannot be linked from a cheatsheet, cannot be
bookmarked, cannot be found by a search engine, and cannot appear in site search.
`lc-explorer.html` does have `shareFilters()`, which is the right instinct pointed at
a query string instead of a page.

Also the natural home for a number the site cannot currently show: *for this pattern,
how many of my attempts are still AGAIN?* That is the roadmap's missing feedback loop.

**Effort.** Small-to-medium once problem pages exist — same generator, grouped the
other way. ~40 tags from `lc_topic_tags.json`.

---

## Tier 2 — worth considering

### 5. Surface the contest work

`leetcode_python/lc_weekly/` (12 contests), `lc_biweekly/` (2), and
`data/lc_weekly/contest_index.md` are all in the repo and none of it reaches the site.
doocs has a whole Contest section. One index page plus the problem pages from Tier 1
would cover it. Cheap, and it is content already paid for.

Skip doocs's Guardian/Knight rating-cutoff table — it is vanity data that goes stale.

### 6. Named approaches and typeset complexity

doocs writes `### Solution 1: Hash Table` followed by prose ending in "Time complexity
is $O(n)$, space $O(n)$". This repo states complexity as a `// time = O(...)` comment
inside the code, per the style guide.

The comment convention is fine and should stay. What is worth stealing is **naming the
approach** — "Solution 1: Hash Table", "Solution 2: Sorting + Two Pointers". A named
approach is recallable under interview pressure in a way that "the second code block"
is not, and it gives the problem page a table of contents for free.

If you want the maths typeset, pre-render KaTeX at build time. Do not ship a KaTeX
`<script>` tag — see the caveat below.

### 7. Per-language formatting gates in CI

Not a site feature, but the clearest thing doocs does better as a *repository*: five
formatting workflows (`black`, `clang-format`, `gofmt`, `rustfmt`, `prettier`) plus a
PR checker and labeler. This repo has 2,869 Python and 1,626 Java files under no
formatter gate at all — which is the same soil the recent "clear 20 SyntaxWarnings"
and "unmangle the ASCII tree diagrams" commits grew out of.

Gate **changed files only**: `black --check` and `google-java-format --dry-run
--set-exit-if-changed` over the diff. Reformatting 4,500 files in one commit would
destroy the git history that `build-site.js` reads for every page's last-updated date.

### 8. Related problems on the problem page

`lc-similar.html` is a 1,893-line d3 visualization of the similarity graph — genuinely
better than anything doocs has, and in the wrong place. The moment you want "what else
is like this" is while you are looking at a problem, not on a separate page you have
to remember exists. A four-item "related" list on each problem page, with the graph
still there for browsing.

---

## Tier 3 — do not copy

| | Why not |
|---|---|
| LeetCode problem statements | LeetCode's copyrighted text. Link out. |
| Solutions in 8–14 languages | Content this repo does not have; irrelevant to the L3 goal. |
| LCOF / LCCI / LCP / LCS book sections | Chinese-market interview books. Blind75 / NeetCode / Grind / Top-100-Liked already serve this purpose here, and are already wired into the roadmap's list picker. |
| Contest rating & badge tables | Stale on arrival. |
| Sponsor block, Open Collective, contributor avatars | Solo repo. |
| MkDocs Material migration | It is where most of doocs's polish comes from — instant loading, search, tabs, feedback widget, all free. It is also a full rewrite that throws away the roadmap, review plan, quiz and the zh overlay, all of which are custom generators. Steal the features, not the framework. |

### The comments caveat

doocs sets `comments: true` on every page and renders giscus (GitHub Discussions), plus
a "Was this page helpful?" widget. Both are genuinely useful for a solo maintainer —
a free correction channel with no backend.

Both are also **external `<script>` tags**, which `e2e-check.js` bans site-wide. That
rule is not incidental; it is why this site has exactly one third-party runtime
dependency (d3, and that one got vendored into `package-lock.json` precisely because
an unpinned CDN script on `lc-similar.html` made the page unusable when the request
failed).

So: either accept a deliberate, documented exception in `e2e-check.js` scoped to
giscus on cheatsheet pages, or skip it. Do not weaken the rule generally.

---

## Suggested order

1. **Problem pages** (Tier 1.1) — unblocks 1.2, 1.4, 2.5, 2.8 and gives every existing
   tool somewhere to link.
2. **Full-text search** (1.3) — independent of everything else, biggest daily payoff
   for the smallest diff.
3. **Language tabs** (1.2) and **tag pages** (1.4) — both fall out of 1.1.
4. **Contest index** (2.5), **related-on-page** (2.8) — cheap once 1.1 lands.
5. **Formatting CI** (2.7) — orthogonal, do it whenever.

Named approaches (2.6) is a writing convention, not a build change; fold it into the
cheatsheet style guide and apply it as sheets get touched.

---

## Sources

- <https://leetcode.doocs.org/en/> — the site
- <https://github.com/doocs/leetcode> — `README_EN.md` (Topics / Focused Training),
  `solution/README_EN.md` (index columns), `solution/0000-0099/0001.Two Sum/README_EN.md`
  (per-problem page shape: frontmatter `difficulty`/`tags`/`edit_url`, `<!-- tabs:start -->`),
  `solution/CONTEST_README_EN.md`, `.github/workflows/` (the five formatting gates)
- This repo: `site/build-*.js`, `site/e2e-check.js`, `site/pages/*.html`, `CLAUDE.md`
