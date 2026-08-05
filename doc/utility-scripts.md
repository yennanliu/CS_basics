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

Maintains the `## LeetCode Problem Lists` section in every cheatsheet under `doc/cheatsheet/`, linking each doc to the matching LeetCode topic problem lists (`https://leetcode.com/problem-list/<tag>/`).

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

## Other Scripts

| Script | Purpose |
|--------|---------|
| `get_again_problems.sh` | Extract problems to review again |
| `get_company_LC.sh` | Get company-specific LeetCode problems |
| `get_lc_per_rating.py` | Filter problems by difficulty rating |
| `get_must_problems.sh` | Extract must-do problems |
| `get_review_list.py` | Generate review lists |
| `list_leetcode_solutions_by_type.sh` | List solutions by algorithm type |
