# Utility Scripts

Scripts live in `script/`. Run from the project root.

## categorize_lc_by_type.py

Categorizes LeetCode problems from `data/progress.md` by type (Array, DP, Graph, etc.) based on directory structure in `leetcode_python/` and `leetcode_java/`.

```bash
# Default: 2025-2026
python3 script/categorize_lc_by_type.py

# Specify year range
python3 script/categorize_lc_by_type.py 2024 2025
```

Output: `data/LC_Practice_{year_start}_{year_end}_By_Category.md` — table of contents, problem counts per category, summary statistics. Categorizes ~80%+ of problems.

## add_lc_problem_lists.py

Maintains the `## LeetCode Problem Lists` section in every mapped cheatsheet under `doc/cheatsheet/`, linking each doc to the matching LeetCode topic problem lists (`https://leetcode.com/problem-list/<tag>/`).

```bash
# Apply (idempotent — rewrites the block in place, never duplicates it)
python3 script/add_lc_problem_lists.py

# Preview which files would change
python3 script/add_lc_problem_lists.py --dry-run

# Check every slug/label against LeetCode (network)
python3 script/add_lc_problem_lists.py --verify
```

The doc → tag mapping lives in `DOC_TAGS` inside the script; edit it there rather than hand-editing the generated sections. Docs with no meaningful LC topic tag (language tricks, complexity theory, pattern indexes) are listed in `SKIP`.

`--verify` uses LeetCode's public GraphQL endpoint (`topicTag(slug:)`) because leetcode.com returns HTTP 403 to non-browser clients for HTML pages. It also confirms each link label matches LeetCode's canonical tag name.

## fix_readme_tags.py

Normalises and completes the tags in `README.md`'s **Note** column — the leading bold
type tag, the company tags, and the curated-list tags.

```bash
# Rewrite README.md (idempotent — a second run changes nothing)
python3 script/fix_readme_tags.py

# What would change, without writing
python3 script/fix_readme_tags.py --report

# Exit 1 if README.md has drifted (for CI or a pre-commit check)
python3 script/fix_readme_tags.py --check
```

What it does, in order:

1. **Repairs a stray backtick.** An odd backtick count inverts the code-span parity for
   the rest of the cell and hides every tag after it from any parser.
2. **Canonicalises company tags.** `M$`, `MS`, `msft` → `` `microsoft` ``; `amz` →
   `` `amazon` ``; `meta`/`facebook` → `` `fb` ``; `GS`/`Goldman Sachs` →
   `` `goldman sachs` ``. This is what makes `script/get_company_LC.sh microsoft` find
   all 555 rows instead of the 2 that happened to be spelled out.
3. **Bolds a missing type tag**, when the row's first tag is already a known pattern.
4. **Appends the problem's real LeetCode topics** when the bold tag names none of them.
   The bold tag itself is never rewritten: it says which section's technique the row is
   filed under (LC 84 sits under `## Greedy` on purpose), so the fix is to add
   `` `monotonic stack` `` beside it, not to overwrite it. Skipped when the row already
   names the topic in its own words ("Ascending Stack", "mono stack").
5. **Tags the curated list the problem sits on** — `` `blind75` ``, `` `neetcode150` ``,
   `` `neetcode250` ``, `` `top100liked` ``. The three NeetCode lists nest, so only the
   narrowest is written: `blind75` already implies the other two. `top100liked` is
   LeetCode's own list and cuts across them (17 of its 100 are outside NeetCode 250), so
   it is an independent tag. `neetcodeAll` is not tagged — at 972 problems it is the
   whole catalogue and would mark 607 rows without ranking any of them.

   A row's list tags are made to **equal** what the data says, not merely to include
   it — the lists move, and a step that only ever added tags would leave a problem
   promoted from NeetCode 250 to 150 claiming both rungs for ever. A tag that is
   already correct is left where it sits rather than removed and re-appended, which is
   what keeps the step idempotent.

   The hand-written labels this replaces (`Curated Top 75`, `LC top 100 like`) are
   **deleted, not renamed**: they sat on a fraction of the rows they belonged on, and 2
   of the 75 rows saying "Curated Top 75" were not on Blind 75 at all, so renaming in
   place would have preserved the error.
6. **Adds missing company tags** for the nine companies the README tracks widely: google,
   amazon, fb, apple, netflix, microsoft, uber, linkedin, bloomberg — the first five
   being FAANG. Widening this set pushes some rows past twenty tags, at which point the
   column stops being readable — the long tail (airbnb, twitter, garena, shopee…) is
   canonicalised where it already appears but never added to a new row.

Three vendored caches feed it, so a normal run needs neither the network nor the PDFs:

| File | Source | Refresh with |
|------|--------|--------------|
| `data/lc_topic_tags.json` | LeetCode's public GraphQL API — official `topicTags` + difficulty for the 1388 problems the README lists | `--refresh-topics` |
| `data/company_lc_tags.json` | the company-frequency PDFs under `doc/` (via `pdftotext`), unioned with `doc/google_leetcode_problems_by_tags.md` for Google | `--refresh-companies` |
| `data/problem_lists.json` | Blind 75 / NeetCode 150 / 250 / Top 100 Liked membership, shared with the site's roadmap filter | `script/fetch_problem_lists.py` |

All three are **vendored, not built** — the site build never touches the network.
Refresh them by hand. Sharing `data/problem_lists.json` with `site/build-roadmap.js` is
the point: the README and the roadmap's list picker answer "is this on Blind 75?" from
one file, so they cannot disagree.

The Google column feeds the site: `site/build-roadmap.js` reads `google` out of this
column for the Study Roadmap's "Google-tagged" list, which the completed tags took from
263 problems to 914.

### Refreshing the company cache safely

The PDFs come in three print layouts, and `doc/leetcode_company_V6` puts a **single**
space after the problem number where the others put several — a `\s{2,}` gap alone reads
5% of that file and silently produces a cache that looks fine. `--refresh-companies`
therefore validates before it writes, and every failure is fatal:

- `pdftotext` runs with `check=True`, so a corrupt or encrypted PDF aborts the refresh
  instead of contributing an empty page.
- Each PDF states its own row count (`You have solved 24 / 1115 problems.`). Parsing under
  half of it raises; parsing under all of it prints a note, because the `V1` captures are
  genuinely short prints (55–86%) rather than bad parses.
- A refresh that ends with no problems for one of the nine companies raises rather than
  writing the cache — a silently short parse would not produce a visibly broken file, it
  would quietly *delete* that company's README tags on the next run.

`doc/leetcode_company_V4` is not read: it is a prose interview guide with no problem
table.

## scrape_lc_discuss_company.py

Scrapes recently-asked LeetCode problems for a company from LeetCode's **public Discuss forum** and writes a markdown report (default [`doc/g_recent_asked.md`](./g_recent_asked.md)).

```bash
# Full run for Google -> doc/g_recent_asked.md  (slow: ~2.5s/request, ~1h for ~275 posts)
python3 script/scrape_lc_discuss_company.py

# Another company -> doc/meta_recent_asked.md
python3 script/scrape_lc_discuss_company.py --tag meta

# Rebuild the report from cache, no network at all
python3 script/scrape_lc_discuss_company.py --build-only

# Quick sample while testing
python3 script/scrape_lc_discuss_company.py --tag amazon --max-pages 2
```

Output: ranked table of referenced LC problems (number, link, difficulty, tags, thread count, match strength, last-seen date, whether the repo already solves it), evidence quotes linking back to each source thread, and the raw feed of interview-flavoured posts.

**This is not LeetCode's official company list.** `companyTag` is Premium-gated and returns `null` anonymously, so this is self-reported interview experience — treat the counts as weak signal. Note also that the legacy discuss API (`categoryTopicList`, category `interview-question`) still responds but is **frozen at 2025-03-04**; live data lives behind the `ugcArticle*` fields the script uses.

Downloads are cached one-file-per-post under `data/.lc_discuss_cache/<tag>/` (gitignored), so interrupted runs resume instead of re-fetching. Delete that directory for a clean pull.

Useful flags: `--delay` (default 2.5s — below ~2s trips LeetCode's WAF, which returns HTML 403s rather than JSON), `--no-comments` (much faster, but comments are usually where the actual questions are), `--refresh-index` (re-download the LC problem index).

The script's module docstring records the reverse-engineered schema, since introspection is disabled. The sharpest trap: `ugcArticleDiscussionArticle(topicId:)` takes `ID` while `topicComments(topicId:)` takes `Int!` — the same argument name with two different types.

## find_missing_java.py

Lists LeetCode problems that have a Python solution under `leetcode_python/` but no Java one. Drives the Java-backfill batches.

```bash
python3 script/find_missing_java.py                    # summary + per-category counts
python3 script/find_missing_java.py --list             # every missing problem
python3 script/find_missing_java.py --category Array   # one Python category
python3 script/find_missing_java.py --json out.json    # machine-readable, for batch work
python3 script/find_missing_java.py --false-gaps       # what a slug-only scan gets wrong
python3 script/find_missing_java.py --unidentified     # Python files with no parseable LC number
```

Comparing `leetcode_python/<Cat>/<slug>.py` filenames against the `leetcode.com/problems/<slug>` comment in each Java file **over-reports by ~40 problems**, because one problem gets spelled several ways: LeetCode renames slugs (LC 211 is `add-and-search-word-data-structure-design` on the Python side, `design-add-and-search-words-data-structure` on the Java side), some Python files are underscored (`coin_change_2`), and ~60 Java files carry no url comment at all.

So the join uses three signals, strongest first: the **LC number** parsed from the `123. Title` line both sides carry, then any **url slug** in the body, then the **title** normalised to a class-name key. A problem counts as covered if any signal hits.

Two traps the parser has to dodge, both of which silently inflate coverage:

- `1. Populate the graph map` in a mid-function comment is not an LC id. The id scan stops at the first type declaration, and a candidate title must look like a title (short noun phrase, no operators or quotes).
- `check with LC 542` is a cross-reference, not a solution. A `// LC n` tag counts only when it sits alone on its line and a problem url follows within a few lines — the shape `LCWeekly/*.java` uses to hold a whole contest.

## eval_lc_readiness.py

Scores a LeetCode profile against a Google SWE coding bar and prints a terminal report.
Pulls the public LeetCode GraphQL API (no auth, no premium) and cross-references it with
`README.md`'s status column.

```bash
python3 script/eval_lc_readiness.py                    # fetch + report, L3 bar
python3 script/eval_lc_readiness.py --level L4         # or L3 / L4 / L5
python3 script/eval_lc_readiness.py --user someone
python3 script/eval_lc_readiness.py --offline          # replay the cache, no network
python3 script/eval_lc_readiness.py --json out.json    # evaluated report as JSON
```

`--level` picks which bar to score against, and it moves the grade a lot — the level
changes what "enough" means, not what gets asked:

| Level | Solved / medium / hard | Contest rating | Notes |
|-------|------------------------|----------------|-------|
| `L3` (default) | 300 / 200 / 60 | 1650 | entry; no system design round, medium fluency is the bar |
| `L4` | 500 / 300 / 150 | 1800 | hard-flavoured mediums common, so the hard tier must be familiar |
| `L5` | 600 / 350 / 200 | 1900 | adds a system design round this data cannot see |

Topic targets scale with the level too (`topic_scale`: 0.70 / 1.00 / 1.15).

Fetched JSON is cached under `.lc_cache/` (gitignored), so `--offline` replays the last
fetch for free. Four axes, weighted into one grade:

| Axis | What it measures | Source |
|------|------------------|--------|
| Volume | solved counts and the Easy/Medium/Hard mix vs target | LC `submitStats` |
| Breadth | per-topic solved vs a target for each topic Google asks, weighted by how often it comes up | LC `tagProblemCounts` |
| Mastery | share of README rows marked `OK` rather than `AGAIN` | README status column |
| Signal | contest rating, contests attended, active days — the only speed-under-pressure proxy available | LC contest + calendar |

Two views carry most of the diagnostic value:

- **Cost curve** — mean review passes per problem, per README section. A high mean means
  the topic keeps costing re-learns even after it is "solved"; ranking sections by it
  separates *never seen* from *never stuck*.
- **Chronic blind spots** — rows still marked `AGAIN` after 12+ passes. More passes have
  already failed to fix these, so they need a different intervention, not another rep.

**Full guide**: [`doc/lc-readiness-guide.md`](./lc-readiness-guide.md) — every flag, what
each section of the output means, and which numbers to act on versus ignore.

Two caveats the output flags on its own:

- LeetCode's skill-stats endpoint reports only a curated tag set and silently omits three
  of the topics scored here. Heap/PQ and Prefix Sum fall back to a regex over the README
  rows and print with a `~` — a lower bound, since it counts only what this repo tracks.
  BST has no usable fallback (its README notes are full of "check with BST"
  cross-references, so any regex over-counts), so it prints `n/a` and is left out of the
  breadth score entirely — an unmeasured topic is not a proven gap.
- The `OK`/`AGAIN` marker behaves as a permanent review-queue tag, not a mastery verdict
  (92% of Hard rows sit at `AGAIN`, some after 20+ passes). So read the Mastery axis as a
  floor, and trust the *relative* cost curve over the absolute `OK` share.

## zh.js

Drives the 繁體中文 translations. There is one markdown tree — the English one —
and `i18n/zh/<slug>.md` holds a sparse overlay of translated *sections*, each
keyed by a hash of its English text. The site composes the two into a full
Chinese document at build time, so code is never stored twice and cannot drift.

```bash
node script/zh.js status [--write]          # coverage; --write refreshes the tracker doc
node script/zh.js todo [slug ...]           # the sections still needing a translation
node script/zh.js sync [--prune] [slug ...] # reorder to match English, park what it dropped
```

With no slugs, every sheet is processed. Edit an English section and its
translation simply goes missing — `todo` prints it, and the page falls back to
English until it is filled in.

`sync` parks an invalidated translation under `<!-- stale: key -->` in the same
file rather than deleting it, so the replacement starts from the old Chinese
instead of from nothing; reverting the English revives it. `--prune` is the only
thing that discards parked entries. See
[`cheatsheet-zh-progress.md`](cheatsheet-zh-progress.md) for the current state and
CLAUDE.md for the translation conventions.

## check_skills.py

The gate for the agent skills under `.claude/skills/`, run in CI by
`.github/workflows/skills-check.yml`. Nothing else looks at a skill: it is
markdown, so no compiler, linter or test sees it, and both of the ways one breaks
are invisible in a diff.

```bash
python3 script/check_skills.py             # structure + wiring
python3 script/check_skills.py --install   # ...and exercise both documented installs
python3 script/check_skills.py --verbose   # list every path it resolved
```

Three groups, matching the three things that rot independently:

| Group | Checks |
|-------|--------|
| **structure** | the `---` fenced frontmatter parses as `key: value`; `name` is kebab-case and matches the directory; `description` exists, fits in 1024 chars and is a sentence rather than a name echo; the body is really there; code fences balance |
| **wiring** | every `.claude/skills/...` path named by `CLAUDE.md`, an `INSTALL.md` or `site/pages/skills.html` still resolves; no reference file is orphaned; no absolute `/Users/...` path is baked in |
| **install** | the `cp -r` into `~/.claude/skills` and the zip the Claude app uploads are both performed into a temp directory, then re-checked — the only way to prove a skill works with none of this repo around it |

The wiring group is the one that earns the file. `skills.html` links each
reference file by name as an **absolute** `github.com` URL, so
[`site/e2e-check.js`](../site/e2e-check.js) cannot resolve them and never will —
renaming `references/patterns.md` leaves the skill perfectly valid and the
published page pointing at a 404. This catches that.

The first run of it found a live bug: `add-time-space/SKILL.md` had shipped
without its `---` fences, so its advertised description was the literal string
`name: add-time-space`.

## suggest_review.py

**On the site**: [`suggest-review.html`](../site/pages/suggest-review.html) — the
same material as a page, with the flags grouped by what they do and four real
runs side by side. Reached from the navbar's **more → suggest** entry. Editing
this doc does not update that page; keep the two in step by hand.

Suggests what to practise next, chosen to keep the practice **balanced** rather
than merely important. The problem it solves is bias: a good week on DP turns
into three weeks on DP while linked list, design and slide window quietly go a
month untouched — and a flat "most important, least recently seen" ranking makes
that worse, because the biggest sections hold the most important problems and
would fill the whole list.

```bash
python3 script/suggest_review.py                      # the balanced plan
python3 script/suggest_review.py --top 20 --per-category 3
python3 script/suggest_review.py --only must          # MUST rows only
python3 script/suggest_review.py --only top100liked
python3 script/suggest_review.py --section "Binary Search" --top 10
python3 script/suggest_review.py --no-balance         # plain importance rank
python3 script/suggest_review.py --markdown doc/review_suggestions.md
python3 script/suggest_review.py --self-test          # run the unit tests
```

### What it reads

Nothing is invented; all three signals are already in the repo.

| Source | What it contributes |
|--------|---------------------|
| `README.md` | The problem universe **and** this repo's own judgement of what matters: the `MUST` marker, the curated-list tags (`blind75` / `neetcode150` / `neetcode250` / `top100liked`), the company tags, and the status column's `OK`/`AGAIN` plus its `*` run of review passes. |
| git history | When each problem was last *worked on* — the commit that touched its solution file, or named its LC number in the subject. |
| [`data/progress.txt`](../data/progress.txt) | When it was last *practised*, which is not the same thing: a re-read that produced no commit still counts. |

[`doc/must_lc_list.md`](must_lc_list.md) is generated from the same README rows by
`extract_must_lc.py`, so reading README covers it — and `--self-test` asserts the
two scripts agree on which rows carry `MUST`, so the rule cannot drift apart.

### How a problem is chosen

**score = importance × staleness × category balance**

- **Importance** — `MUST` +5.0, `top100liked` +2.5, `blind75` +2.5 (the NeetCode
  lists nest, so only the narrowest one scores: 150 → +1.5, 250 → +0.7),
  `google` +1.5 (the stated target is a Google loop), other company tags +0.2
  each capped at +1.0, Medium +0.5 / Hard +0.3, and +0.2 per recorded pass on an
  `AGAIN` row up to +2.4. That last one reads the pass count as *difficulty*,
  not as progress — the `AGAIN` marker never graduates in this repo, see
  [`lc-readiness-guide.md`](lc-readiness-guide.md).
- **Staleness** — `1 − 0.5 ^ (days / half-life)`, half-life 21 days. Touched
  today scores 0, never touched saturates near 1.
- **Category balance** — each README section's share of total importance
  compared against its share of *recent attention*. A section getting none of
  the practice it is owed nearly doubles its problems' scores; one getting twice
  its share is cut to 0.4.

Picks are then spent **round-robin across categories in deficit order**, capped
at `--per-category` (default 2), so no single hot topic can own the list. The
cap and the score floor are a preference for breadth, not a quota: once every
eligible category has had its share, the rest of `--top` is filled by score.

### Reading the output

Read the **balance table first** and the picks second — the table is the
finding, the picks are one way to act on it.

```text
== Balance: attention vs importance ==

  category                       n  importance     attention         ratio  last
  Array                        139  ######...... #...........    0.19 UNDER  today
  Linked list                   24  ##.......... ............    0.00 UNDER  36d
  ...
  Dynamic Programming           94  #####....... ############    2.20 over   today
```

`ratio` is the share of recent attention divided by the share of importance:
`1.00` is a fair share, `UNDER` (< 0.5) is the bias to fix, `over` (> 1.8) is
where the practice has been pooling.

### Two things it deliberately ignores

- **Bulk commits.** A commit touching more than `--bulk-limit` (default 6)
  solution files is an import or a sweeping refactor, not a study session. This
  repo has commits adding 393 generated Java files at once; counted naively they
  made every one of those problems look practised on the same day and put 563
  problems inside a 30-day window that actually saw 188. Their file paths are
  dropped; an LC number written into the subject by hand still counts.
- **Non-practice sections.** SQL, Shell Script, Concurrency and the unverified
  `Newly Added (kamyu104 gap)` drafts are left out of the balance maths so they
  cannot distort a category's share. `--all-sections` puts them back.

### Tests

```bash
python3 script/test_suggest_review.py           # stdlib unittest, no dependencies
python3 script/test_suggest_review.py -v
python3 script/test_suggest_review.py ParseProgress   # one class
python3 script/suggest_review.py --self-test    # the same suite, quietly
```

Three of the four inputs are hand-written files whose shape nobody controls, so
the failure mode is not a crash — it is a parser that quietly reads fewer rows
than there are and returns a plausible but shrunken plan. Every fixture in
[`test_suggest_review.py`](../script/test_suggest_review.py) is therefore a line
that is really in those files: a `MUST` in the status cell vs. the word "must" in
prose, a duplicate README row for one LC, an annotation containing a comma, a
line wrapped mid-annotation, a `DP:` label, a stray period between entries, a
`git log` whose commit adds twenty files at once.

The split mirrors `site/test/*.test.js`: the unit tests run against those
fixtures so they do not move whenever a row does, and a `LiveFiles` class holds
the real `README.md` and `data/progress.txt` — including the cross-check that
`suggest_review.py` and `extract_must_lc.py` still agree on what a `MUST` row is,
and that `EXCLUDED_SECTIONS` still names sections that exist (a renamed one would
silently start competing for review slots).

`--self-test` loads and runs that same module, so there is one copy of the
assertions; it exists because the planner is most often run as a lone script and
"does it still read the files correctly" should be one command away.

Writing them turned up one live difference worth knowing:
`site/build-review-plan.js` does not strip a run's label, so
`| DP: 44(todo), 10(todo)` loses LC 44 — 8 entries in the current log, always the
first problem after a label. This script strips them; the site build is
untouched.

## Other Scripts

| Script | Purpose |
|--------|---------|
| `get_again_problems.sh` | Extract problems to review again |
| `get_company_LC.sh` | Get company-specific LeetCode problems |
| `get_lc_per_rating.py` | Filter problems by difficulty rating |
| `get_must_problems.sh` | Extract must-do problems |
| `get_review_list.py` | Fibonacci-interval spaced-repetition dates from `data/progress.txt` (see `suggest_review.py` above for the importance/balance view) |
| `list_leetcode_solutions_by_type.sh` | List solutions by algorithm type |
