# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Repository Overview

CS_basics is a comprehensive computer science fundamentals repository containing algorithmic problems, data structures, system design patterns, and interview preparation materials. The codebase spans multiple programming languages and focuses on LeetCode problems, system design, and CS concepts.

## Directory Structure

- `leetcode_java/` - Java implementations of LeetCode problems (~508 files)
  - Maven project with JUnit testing setup
  - Organized into: AlgorithmJava, DataStructure, dev, LeetCodeJava packages
- `leetcode_python/` - Python implementations of LeetCode problems (~826 files)
  - Organized by algorithm patterns (Array, Backtracking, Binary_Search, etc.)
- `leetcode_SQL/` - SQL query solutions (~166 files)
- `leetcode_scala/` - Scala implementations
- `algorithm/` - Algorithm implementations across multiple languages (C, Java, JS, Python, SQL)
- `data_structure/` - Data structure implementations (Java, JS, Python, Scala)
- `system_design/` - System design patterns, templates, and case studies
- `doc/` - Documentation, cheat sheets, interview resources, and study materials
- `ref_code/` - Reference code examples
- `script/` - Utility scripts
- `site/` - GitHub Pages build tooling
  - `build.sh` - **The** build recipe: builds the whole `_site/` tree. Both workflows call it
  - `build-site.js` - Builds HTML pages from markdown docs
  - `build-leetcode.js` - Generates LeetCode JSON data for the LC Explorer
  - `build-roadmap.js` - Resolves [`data/roadmap.json`](data/roadmap.json) against `README.md` and [`data/problem_lists.json`](data/problem_lists.json) into the Study Roadmap's data; fails the build on a bad topic id, cheatsheet slug, LC number or list mapping
  - `build-quiz.js` - Resolves [`data/complexity_quiz.json`](data/complexity_quiz.json) against `README.md` into the Complexity Quiz's data; fails the build on a duplicate id, an LC number README does not know, or an answer the grader cannot parse
  - `build-review-plan.js` - Compiles [`data/progress.txt`](data/progress.txt) into the Review Plan's data. **The practice log is the only copy** — see [Review plan data](#the-review-plans-data) below
  - `finalize-pages.js` / `prune-images.js` - The two finishing passes; they run last because they need the whole `_site/` tree (see [Finishing passes](#the-two-finishing-passes))
  - `e2e-check.js` - Post-build validation of every generated page. Both workflows run it; run it locally too
  - `pages/` - Hand-maintained static pages (LC Explorer/Similar/Review-Plan/Random-Picker/Roadmap/Complexity-Quiz, Skills, 404)
  - `nav.js` / `roadmap.js` / `complexity.js` - Browser scripts copied to `_site/`; unit-tested under `site/test/`
  - `style.css` - Stylesheet for the generated doc pages
  - `nav.css` - Navbar, skip link and the `prefers-reduced-motion` opt-out. Loaded by **every** page family
  - `lc-page.css` - Shared palette and footer for the hand-written pages in `pages/`, which do not load `style.css`
  - `package.json` / `package-lock.json` - Node.js dependencies (markdown-it, highlight.js, d3)

### The site is built by CI — never commit `_site/`

`_site/` is generated output and is **gitignored**. `.github/workflows/deploy-pages.yml`
runs `site/build.sh` on every push to `master` and GitHub Pages serves that artifact
(`build_type: workflow`), so the built HTML never needs to be in a commit.

**To change the site, edit source only**: the markdown under `doc/`, the static pages in
`site/pages/`, or the build tooling in `site/`.

To preview locally, run the same recipe CI runs — not `build-site.js` on its own, which
produces an incomplete tree:

```bash
npm ci --prefix site          # first time only
bash site/build.sh            # or: npm run build --prefix site
SKIP_FONTS=1 bash site/build.sh   # offline / skip the web font download
node site/e2e-check.js _site      # the same gate CI runs — run it before you push
python3 -m http.server -d _site 8000
```

`build.sh` starts with `rm -rf _site`, so a deleted or renamed doc can never leave an
orphan page behind.

### `e2e-check.js` is the contract for the built site

Both workflows run it and fail on any error, so it is the place a site-wide rule
belongs. It walks **every** page, not a list of a few — the rules it enforces
(doctype, charset, viewport, title, navbar, footer, canonical URL, a per-page
description, no broken or root-relative link, no unresolved `.md` link, no eager
or missing image, every table scroll-wrapped, no external `<script>`) each exist
because something quietly shipped broken without them.

It also exercises the real artefacts rather than a copy of their logic: search's
`score()` is lifted verbatim out of the built `search.html` and run against the
built index, so a scoring change that breaks queries fails here.

**Add a rule here, not in a workflow.** These checks used to live as a ~280-line
heredoc inside `validate-pages.yml`, where they could not be run locally, could
not be tested, and only warned.

### The review plan's data

[`data/progress.txt`](data/progress.txt) — the daily practice log — is the single
source for `lc-review-plan.html`. `build-review-plan.js` compiles it to
`_site/data/progress.json` on every build and the page fetches that.

**Never paste practice data into the page.** It was a `const RAW` template literal
until Aug 2026, which meant the review schedule froze on the day someone last
remembered to re-paste it; the shipped copy had been four months behind the log.
An `e2e-check.js` rule now fails the build if that literal comes back.

The log's format is hand-written and loose — wrapped lines, `|` between sessions,
annotations containing commas and nested parens. `site/test/build-review-plan.test.js`
pins each shape against the real file, so a new one that the parser cannot read
fails a test instead of silently shrinking the schedule.

The annotations are the point: `139(again!!)` and `139(ok)` are different rows.
`again` beats `ok` when a note says both (`"ok, but again"`), and the bang count
sorts `again!!!` above a bare `again`.

### The two finishing passes

`finalize-pages.js` and `prune-images.js` run after everything else in `build.sh`,
and the order matters. The site is produced by five generators **plus a plain `cp`**
of `site/pages/` and `algo_demo/`, so no generator can see the finished tree:

- `finalize-pages.js` gives the copied pages the canonical / Open Graph / Twitter
  tags that `htmlTemplate` already gives the generated ones, then writes
  `sitemap.xml` and `robots.txt` from the whole tree. It only fills gaps — a page
  that already has a canonical URL is left alone.
- `prune-images.js` deletes the `_site/doc/pic` images no page references.
  `build.sh` has to copy the directory wholesale (the pages that reference it do
  not exist yet), and two thirds of it — 68 MB — belongs to the markdown as read
  on GitHub, not to the site. It matches case-insensitively on purpose, so a
  reference whose case is wrong is reported by `e2e-check.js` as a broken link
  rather than quietly deleted.

`doc/pic/` in the repo is never touched by either.

### Where a `.md` link goes

`build-site.js` keeps a registry of every markdown file that becomes a page
(`mdToPage`), filled in before anything renders. A `.md` link resolves against it:
**a local page if the target is built, a GitHub URL if it is not.** That holds for
every spelling — `./heap.md`, a bare `design.md`, a cross-tree
`../faq/java/faq_OOP.md`, or a hand-written `github.com/.../doc/cheatsheet/x.md`.

Two consequences worth knowing:

- A link to `00_template.md` or a cheatsheet `README.md` correctly becomes a
  GitHub URL — neither is built.
- A typo'd target (`kadane_algo.md` for `kadane_algorithm.md`) lands on GitHub,
  where it 404s visibly, rather than becoming a dead link inside the site.

### The landing page and the problem index

`index.html` is a landing page built by `build-site.js`; README lives at
`problems.html`. Every count on the landing page — problems, cheatsheets, FAQs,
visualizers, roadmap topics, quiz questions, OK vs AGAIN — is read from the source
files at build time. **Do not hardcode one**; a typed number is one that goes stale
the first week nobody re-checks it.

## Build and Test Commands

### Java (leetcode_java/)
```bash
# Build (if Maven is available)
cd leetcode_java
mvn compile

# Run tests (if Maven is available)
mvn test

# Run specific test class
mvn test -Dtest=ClassName
```

Note: Maven may not be available in all environments. The project uses Java 8 compatibility with JUnit 5 for testing.

### Python
No specific build requirements. Python files can be executed directly:
```bash
python3 path/to/solution.py
```

## Code Organization Patterns

### LeetCode Problems
- **Java**: Problems are organized into packages by algorithm type (AlgorithmJava, DataStructure, LeetCodeJava)
- **Python**: Problems are organized into directories by algorithm patterns (Array, Backtracking, Binary_Search, etc.)
- **SQL**: Query solutions organized by problem number/type

### System Design
- Template-based approach with `00_template.md` as the base structure
- Real-world case studies (Netflix, Twitter, Uber, etc.)
- Design patterns organized by system type

## Key Resources and References

The repository extensively references:
- LeetCode problem classifications and patterns
- Algorithm complexity charts and Big-O references
- Interview preparation materials (Blind 75, Grind 75, Grind 169)
- System design fundamentals and case studies

## Utility Scripts

See [`doc/utility-scripts.md`](doc/utility-scripts.md) for full usage of all scripts in `script/`.

## Development Notes

- Code follows language-specific conventions
- Problems often include multiple solution approaches
- System design includes both theoretical concepts and practical implementations
- Documentation emphasizes interview preparation and pattern recognition
- Use `data/progress.md` to track daily practice progress with AI-suggested related problems

---

## Cheatsheet Style Guide

Cheatsheets live in `doc/cheatsheet/`. [`doc/cheatsheet/00_template.md`](doc/cheatsheet/00_template.md) is the authoritative structure — read it before creating or restructuring a cheatsheet.

### Which skeleton

Two skeletons are in use. **Pick by size, not by topic**, and never mix them in one file:

| | **Skeleton A — short doc** | **Skeleton B — reference doc** |
|---|---|---|
| Use when | < ~800 lines; one technique | > ~800 lines; a data structure or pattern family |
| Shape | `0) Concept` → `1) General form` → `2) LC Example` | `Overview` → `Problem Categories` → `Templates & Algorithms` → `LC Examples` → `Problems by Pattern` → `Pattern Selection Strategy` → `Summary` |
| Examples | `kadane_algorithm.md`, `n_sum.md`, `iterator.md` | `heap.md`, `dp.md`, `bfs.md`, `graph.md` |

If a Skeleton A doc grows past ~800 lines, convert it to B rather than appending B's sections to the end.

### Mandatory header

Every cheatsheet opens with the H1, then a **Scope** block, then `## LeetCode Problem Lists`:

```markdown
# <Topic Name>

> **Scope** — <what this file owns, and what it deliberately does not>.
> **See also**: [other.md](./other.md) — <why you'd go there>.
```

Both lines are **required on every file**, not only when topics overlap:

- The H1 is what the site shows as the page title and on the index card — write it as a name a reader would recognise, not as a filename echo.
- The Scope line is what stops two files from silently growing into the same doc, **and** it is lifted verbatim into the sheet's card description on `cheatsheets.html`. One sentence, plain prose, no lead-in.

### Registering a new cheatsheet

A new `doc/cheatsheet/*.md` must also get an entry in [`data/cheatsheet_meta.json`](data/cheatsheet_meta.json) — the build fails otherwise, on purpose, so nothing lands in an unsorted bucket:

```json
"my_topic": { "category": "Arrays & Strings", "tier": 4, "title": "Optional override" }
```

- `category` — must be one of the entries in that file's `categoryOrder`.
- `tier` — FAANG interview weight: `5` must-know, `4` high value, `3` worth knowing, `2` niche. This drives the stars on the card, the card's ordering within its category, and the emphasis stripe.
- `title` — only when the H1 is too long or too literal for a card.
- `kind` — `"stub"` for a redirect file, `"reference"` for an imported index. Omit for a normal sheet.
- Add it to `startHere` only if it belongs in the beginner reading ladder.

### Adding a topic to the Study Roadmap

The roadmap page (`lc-roadmap.html`) is driven entirely by [`data/roadmap.json`](data/roadmap.json) — one entry per topic:

```json
{
  "id": "monotonic-stack",
  "title": "Monotonic Stack",
  "row": 2,
  "prereqs": ["stack"],
  "sheets": ["monotonic_stack", "monotonic_queue"],
  "blurb": "One sentence on what the topic buys you.",
  "problems": [496, 503, 85, 901, 907]
}
```

Titles, difficulty and links to this repo's solutions come from `README.md` at build time — never repeat them here. `site/build-roadmap.js` fails the build if:

- a `problems` id is not in a README table, or a `sheets` slug is not a file in `doc/cheatsheet/`;
- `row` is not strictly greater than every prereq's `row` (edges must point downward);
- the prereqs contain a cycle, or an edge the graph **already implies** — the roadmap has to stay a transitive reduction, or the drawing turns into spaghetti.

Within a row, topics are drawn in the order they appear in the file, so put a topic near the column its prerequisite sits in to keep the edges short.

### The roadmap's list picker

The page shows one problem set at a time. `roadmap` is the curated path above; the rest are well-known lists filed onto the same topics. All of them are declared in `data/roadmap.json`:

- **`lists`** — the picker's entries. `from` says where membership comes from: `curated` (the ids on the nodes), `list:<flag>` (a flag in [`data/problem_lists.json`](data/problem_lists.json)), or `readme:<field>` (`google` / `must`, read straight out of README's tag and status columns).
- **`topicSources`** — each source files problems under its own taxonomy (NeetCode's `Arrays & Hashing`, LeetCode's plan group `Hashing`, README's `## Array` heading). These maps put them on roadmap topics; `null` means *deliberately* off the roadmap (SQL, shell, JavaScript-only exercises). A list's `topicFrom` names which taxonomies to try, in order, so a coarse group falls through to a finer one.

Only the curated list has a teaching order, so only it renders locks and prerequisites.

### Adding a complexity-quiz question

The quiz page (`lc-complexity-quiz.html`) draws from [`data/complexity_quiz.json`](data/complexity_quiz.json) — one entry per snippet:

```json
{
  "id": "two-sum-hash",
  "lc": 1,
  "topic": "Arrays & Hashing",
  "vars": "n = len(nums)",
  "code": ["def twoSum(nums, target):", "    ..."],
  "time": "O(n)",
  "space": "O(n)",
  "why": "One sentence on where each bound comes from.",
  "trap": "The wrong answer people actually give, and why it is wrong."
}
```

Titles, difficulty and the link to this repo's solution come from `README.md` at build time — never repeat them here. Set `title` and `difficulty` yourself only for an entry with `"lc": null` (a pure algorithm or Python drill). `site/build-quiz.js` fails the build if:

- an `id` repeats, or an `lc` number is not in a README table;
- an entry with an `lc` number sets its own `title` or `difficulty`, or one without an `lc` number omits them;
- an `accept` field is not an array (a bare string survives validation and then breaks the page's feedback);
- any answer — `time`, `space`, or an `accept` alternative — does not parse as a complexity expression. Answers are normalised by `site/complexity.js`, whose identifiers are **single letters**, so write `O(n * a)` with a `vars` line rather than `O(n * amount)`.

`accept` is for answers that are genuinely defensible (`O(h)` vs `O(n)` for a tree's recursion stack), not for spelling variants — `O(n log n)`, `nlogn` and `N·logN` already grade the same.

`data/problem_lists.json` is **vendored, not built** — Blind 75 / NeetCode 150 / 250 / All are extracted from the neetcode.io app bundle, and Top 100 Liked from LeetCode's GraphQL. Refresh it by hand; the site build never touches the network:

```bash
python3 script/fetch_problem_lists.py           # rewrite the file
python3 script/fetch_problem_lists.py --check   # exit 1 if it is stale
```

The build fails on a taxonomy key that is missing, points at an unknown topic, or is mapped but unused — so a renamed upstream category cannot silently drop a whole group of problems. Watch the per-list "shown of" tally it prints.

### Formatting Rules

- **Bold** key terms: `**Pattern**`, `**Key Idea**`, `**Recurrence**`
- Category headers: `#### **Category Name**`
- Code blocks: **always** tag the language — `java`, `python`, or `text` for ASCII traces, diagrams and program output. Never a bare ` ``` `.
- Complexity: inside code as first comment — `// time = O(...), space = O(...)`
- Images: `<p align="center"><img src="../pic/filename.png"></p>`
- Priority markers: a trailing `⭐`…`⭐⭐⭐⭐⭐` run on a heading marks how interview-critical that section is (5 = memorise it). The site strips the run out of the heading, renders it as a star badge, weights the heading's left rule by it, and surfaces 4★/5★ sections in the page's table of contents — so put the run **on the heading**, not in the prose under it. Leave ordinary background sections unmarked; if everything is starred, nothing is.
- Heading levels never skip (`h2` → `h3`, never `h2` → `h4`)
- State each LC number **once** per heading — not `... (LC 347) — LC 347`
- Complexity is stated **once** in the header: either the `## Time Complexity` table *or* a Key Properties bullet, never both

### Code Conventions

- Open with `// IDEA: brief description` (Java) or `# IDEA: ...` (Python)
- Provide both Java and Python implementations when applicable
- Label each block: `// java` / `# python`
- Include `// LC <number> - Problem Name` above the class/function
- **One canonical solution per problem.** A second variant needs a stated reason (different complexity, different language idiom, distinct trick) — not just a different spelling of the same loop.

### Anti-patterns (these caused the Aug 2026 cleanup — see [`doc/cheatsheet-review-2026-08.md`](doc/cheatsheet-review-2026-08.md))

- ❌ An `LC Examples` section appended to the end that re-solves problems already solved by templates above
- ❌ Catch-all sections (`Missing Google Patterns`) instead of filing new material under the pattern it belongs to
- ❌ Duplicate heading text under the same parent (`Summary`, `Core Idea`) — qualify them
- ❌ Splitting one topic across two files without a Scope line saying which owns what

### Common Section Patterns

| Pattern | Use |
|---------|-----|
| Quick Decision Table | At section start — maps goal → template → examples |
| Template Comparison Table | Side-by-side comparison of loop conditions / update rules |
| Similar Problems Table | Group related LC numbers with key differences |
| Visual Trace | ASCII walkthrough of algorithm steps (tag it ` ```text `) |
| Decision Matrix | `Minimize vs Maximize`, `Memoization vs Tabulation`, etc. |

### Overview Section (for larger docs)

```markdown
### Key Properties
- **Complexity**: see the [Time Complexity](#time-complexity) table above
- **Core Idea**: ...
- **When to Use**: ...

### References
- [Name](url)
```

---

## Traditional Chinese cheatsheets

There is **one markdown tree**, the English one. A translation is a *sparse
overlay* of translated sections in `i18n/zh/<slug>.md`, and the site composes the
two into a full Chinese document at build time (`site/i18n.js`). The navbar shows
a **中文 / EN** button that swaps between counterparts; `cheatsheets.html` ↔
`cheatsheets.zh.html` is the way in. Progress lives in
[`doc/cheatsheet-zh-progress.md`](doc/cheatsheet-zh-progress.md) (generated).

**Why an overlay and not a second tree.** Roughly 70% of a sheet is fenced code
that must read identically in both languages, and every English sheet was edited
in the last six months. A parallel tree stored that code twice and tracked
staleness per *file*, so a 45-line edit invalidated a 1,000-line translation. The
overlay stores prose only, keyed per *section* (median 249 bytes), so an edit
invalidates only the section it touched.

**Rules for a translation**

- **Only prose is stored.** Every fence is lifted out to a one-line `<!--CODE-->`
  marker before storage and spliced back at compose time. A translated section
  must keep every marker it was given, in order — `compose` throws otherwise.
- **Structure comes from the English sheet.** Headings and their order are the
  English document's, so a translation cannot add or drop one and the two can
  never disagree about shape. Translate the heading *text* only.
- **A missing section falls back to English**, so a half-translated sheet renders
  as a Chinese page with English gaps rather than failing.
- **Links keep their English targets.** `[見 §3](#two-pointers)` still names the
  English slug; the build pairs the two documents' headings by position, retargets
  every fragment, and then asserts that no cheatsheet link is left dangling.
- No `category` / `tier` / `kind` in a translation: the build reads them off the
  English sheet, so the two indexes can never disagree.
- The Scope line becomes `> **範圍** — …` (the build reads either spelling for the
  card description).
- The zh index's category names, blurbs, tier labels and "start here" reasons come
  from the `zh` block in [`data/cheatsheet_meta.json`](data/cheatsheet_meta.json);
  anything missing there falls back to English rather than failing the build.
  The page's own sentences are `INDEX_TEXT` in `site/build-lib.js`.

**Workflow** — after an English sheet is edited, its changed sections simply go
missing from the store:

```bash
node script/zh.js sync heap           # park the translations the edit invalidated
node script/zh.js todo heap           # the sections needing a translation, keys included
#   adapt each parked translation, keeping every <!--CODE--> line, and write it back
#   into i18n/zh/heap.md as a live `<!-- key -->` entry
node script/zh.js sync heap           # tidy, and drop the parked copies you used
node script/zh.js status --write      # refresh the progress doc
```

`sync` **parks** rather than deletes: a translation whose English section changed
is kept in the same file under `<!-- stale: key -->`, because the edit is usually
small and the Chinese usually still most of the way there. `compose` ignores
parked entries, so one can never reach a page. Revert the English and its
translation revives on the next `sync` — same text, same key. Only
`sync --prune` throws parked entries away.

A section counts as translated when the store **has an entry** for it, not when
its Chinese differs from the English: 238 sections are an LC-titled heading over a
code block, and house rule keeps LC titles in English, so their correct
translation *is* the English text.

---

## Adding Time/Space Complexity Javadoc Comments

For the full guide, see [`doc/add-time-space-guide.md`](doc/add-time-space-guide.md). Quick start: `/add-time-space <DirectoryName>`.

---

## Evaluating Interview Readiness

`script/eval_lc_readiness.py` scores a LeetCode profile against a Google SWE coding bar
(`--level L3` by default) using the public GraphQL API plus `README.md`'s status column.

For the full guide — flags, how to read each section, and which numbers to act on — see
[`doc/lc-readiness-guide.md`](doc/lc-readiness-guide.md). Quick start:

```bash
python3 script/eval_lc_readiness.py
```

---

## Coaching a coding interview

`.claude/skills/lc-interview-coach/` is the interviewer-side counterpart to the readiness
script: it reviews a solution against the four FAANG signals (communication, problem solving,
coding, verification), teaches the pattern from first principles rather than handing over a
template, dry-runs code as a state table, and names the one line that sets the complexity.

`SKILL.md` is the whole coach; `references/` holds the rubric anchors, the per-pattern
invariants, and the in-the-room talk track. It is plain markdown with no dependencies —
[`INSTALL.md`](.claude/skills/lc-interview-coach/INSTALL.md) covers installing it on Codex,
Gemini and other agents from the same source.

`site/pages/skills.html` is its page on the site — intro, per-agent install, quick start —
reached from the navbar's **more → coach** entry and from the landing page's card. It is a
hand-maintained page like the LC tools, so `build.sh` copies it and `finalize-pages.js` gives
it the canonical and Open Graph tags. Editing the skill does **not** update that page; keep
the two in step by hand.

```text
/lc-interview-coach review leetcode_python/Sliding_Window/sliding_window_maximum.py
/lc-interview-coach mock interview me on LC 239
```
