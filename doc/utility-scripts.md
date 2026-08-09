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

## Other Scripts

| Script | Purpose |
|--------|---------|
| `get_again_problems.sh` | Extract problems to review again |
| `get_company_LC.sh` | Get company-specific LeetCode problems |
| `get_lc_per_rating.py` | Filter problems by difficulty rating |
| `get_must_problems.sh` | Extract must-do problems |
| `get_review_list.py` | Generate review lists |
| `list_leetcode_solutions_by_type.sh` | List solutions by algorithm type |
