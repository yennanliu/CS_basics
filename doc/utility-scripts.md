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

## scrape_lc_discuss_company.py

Scrapes recently-asked interview questions for a company from **four public sources**, maps every mention back to a LeetCode problem, and writes one markdown report (default [`doc/g_recent_asked.md`](./g_recent_asked.md)).

| Source | Endpoint | What it gives | Signal |
|--------|----------|---------------|--------|
| `leetcode` | `leetcode.com/graphql` Discuss API | threads + bodies + comments | high — posters cite `LC <n>` and links |
| `reddit` | `search.rss` over r/leetcode, r/cscareerquestions, r/csMajors, r/ExperiencedDevs | posts (full selftext) + comment threads | high — the densest source of recent reports |
| `blind` | `teamblind.com/search/<query>` HTML | search cards + full post bodies (no comments) | medium — lots of process chatter, few problem ids |
| `hn` | `hn.algolia.com` search API | stories + comments | low — breadth and noise |

```bash
# Full run for Google, all sources -> doc/g_recent_asked.md  (slow: hours, mostly comments)
python3 script/scrape_lc_discuss_company.py

# Another company -> doc/meta_recent_asked.md
python3 script/scrape_lc_discuss_company.py --tag meta

# Just the fast sources
python3 script/scrape_lc_discuss_company.py --sources blind,hn

# Rebuild the report from cache, no network at all
python3 script/scrape_lc_discuss_company.py --build-only

# Quick sample while testing
python3 script/scrape_lc_discuss_company.py --tag amazon --max-pages 2
```

Output: ranked table of referenced LC problems (number, link, difficulty, tags, thread count, which sources, match strength, last-seen date, whether the repo already solves it), evidence quotes linking back to each source post, and a per-source raw feed of interview-flavoured posts.

**This is not LeetCode's official company list.** `companyTag` is Premium-gated and returns `null` anonymously, so all of this is self-reported interview experience — treat the counts as weak signal. Note also that the legacy discuss API (`categoryTopicList`, category `interview-question`) still responds but is **frozen at 2025-03-04**; live data lives behind the `ugcArticle*` fields the script uses.

Downloads are cached one-file-per-post under `data/.lc_discuss_cache/<tag>/` (gitignored) — LeetCode at the root, other sources in a subdirectory each — so interrupted runs resume instead of re-fetching. Comment fetching (the slow stage) goes newest-first, so a run you cut short still has the freshest threads. Delete that directory for a clean pull.

Useful flags: `--sources` (subset of `leetcode,reddit,blind,hn`), `--delay` (overrides the per-source defaults — 2.5s LeetCode, 8s Reddit, 2s Blind, 1s HN; going faster gets you blocked, not throttled), `--reddit-subs`, `--no-comments` (much faster, but comments are usually where the actual questions are), `--refresh-index` (re-download the LC problem index).

Adding a source is one function: return `record()`s, honour `ctx.build_only`, register it in `SOURCES`. Extraction, ranking and the report never look at where a post came from.

The module docstring records what each site does *not* document. The sharpest traps: LeetCode's `ugcArticleDiscussionArticle(topicId:)` takes `ID` while `topicComments(topicId:)` takes `Int!` (same argument name, two types); Reddit 403s `.json` for anonymous clients but still serves the `.rss` twin of the same path; Blind ignores `?page` entirely, so breadth comes from asking several queries.

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

## Other Scripts

| Script | Purpose |
|--------|---------|
| `get_again_problems.sh` | Extract problems to review again |
| `get_company_LC.sh` | Get company-specific LeetCode problems |
| `get_lc_per_rating.py` | Filter problems by difficulty rating |
| `get_must_problems.sh` | Extract must-do problems |
| `get_review_list.py` | Generate review lists |
| `list_leetcode_solutions_by_type.sh` | List solutions by algorithm type |
