#!/usr/bin/env python3
"""Refresh data/problem_lists.json — the curated LeetCode lists the roadmap can filter by.

Why this exists
---------------
`site/build-roadmap.js` lets the roadmap page show one curated list at a time
(Blind 75, NeetCode 150 / 250 / All, LeetCode's Top 100 Liked). Two of those
memberships are not derivable from anything in this repo:

  * NeetCode — the open-source solutions repo vendored at `ref_code/leetcode-main`
    carries only `blind75` and `neetcode150`, over 450 problems. There is no
    `neetcode250` flag in it and no published API. The complete table is embedded
    in the neetcode.io Angular bundle, which is where this script reads it from.

  * Top 100 Liked — `doc/important/lc_top100_liked.txt` is a 93-row snapshot of
    the *old* problem-list version. The current study plan has exactly 100
    problems and its own topic grouping, both of which LeetCode's public GraphQL
    endpoint will hand over.

Both are written into one small, reviewable JSON file under `data/`, so **the
site build never touches the network**. Run this by hand when the lists change.

The other two lists the roadmap offers — `google` and `must` — come straight out
of README.md at build time and are not in this file.

Usage:
    python3 script/fetch_problem_lists.py            # rewrite data/problem_lists.json
    python3 script/fetch_problem_lists.py --stdout   # print, do not write
    python3 script/fetch_problem_lists.py --check    # exit 1 if the file is stale

Both fetches assert on what they extracted. If a count comes back wrong the
upstream format has moved, and the script fails loudly rather than writing a
truncated list that would quietly shrink the site.
"""

import argparse
import json
import os
import re
import sys
import urllib.request

USER_AGENT = "Mozilla/5.0 (compatible; CS_basics; +https://github.com/yennanliu/CS_basics)"

# ── NeetCode ────────────────────────────────────────────────────────────────

NEETCODE_PRACTICE = "https://neetcode.io/practice"
# The bundle filename carries a content hash that changes on every deploy, so it
# is discovered from the page rather than hard-coded.
BUNDLE_RE = re.compile(r'src="(main\.[0-9a-f]+\.js)"')
# One problem record in the minified bundle. These are plain JS object literals;
# the fields are read with regexes rather than by evaluating the bundle, because
# running scraped code to extract a list of problem names is not a trade worth
# making.
RECORD_RE = re.compile(r'\{problem:"((?:[^"\\]|\\.)*)",pattern:"([^"]*)",link:"([^"]*)"[^}]*\}')
NEETCODE_FLAGS = ["blind75", "neetcode150", "neetcode250"]
NEETCODE_EXPECTED = {"blind75": 75, "neetcode150": 150, "neetcode250": 250}
NEETCODE_MIN_TOTAL = 900

# ── LeetCode ────────────────────────────────────────────────────────────────

LEETCODE_GRAPHQL = "https://leetcode.com/graphql/"
LEETCODE_PLAN = "top-100-liked"
LEETCODE_QUERY = """
query($slug: String!) {
  studyPlanV2Detail(planSlug: $slug) {
    name
    planSubGroups {
      name
      questions { questionFrontendId title titleSlug difficulty }
    }
  }
}
"""
LEETCODE_EXPECTED = 100


def get(url, payload=None):
    headers = {"User-Agent": USER_AGENT}
    data = None
    if payload is not None:
        headers["Content-Type"] = "application/json"
        data = json.dumps(payload).encode()
    request = urllib.request.Request(url, data=data, headers=headers)
    with urllib.request.urlopen(request, timeout=60) as response:
        return response.read().decode("utf-8", "replace")


def field(blob, name):
    match = re.search(name + r':"((?:[^"\\]|\\.)*)"', blob)
    return match.group(1) if match else None


def fetch_neetcode():
    """-> (bundle name, {lc id: {title, difficulty, group, lists}})."""
    html = get(NEETCODE_PRACTICE)
    bundle_name = BUNDLE_RE.search(html)
    if not bundle_name:
        raise SystemExit(f"could not find the main bundle in {NEETCODE_PRACTICE}")
    bundle = get(f"https://neetcode.io/{bundle_name.group(1)}")

    problems = {}
    for match in RECORD_RE.finditer(bundle):
        blob = match.group(0)
        code = field(blob, "code")
        # "0217-contains-duplicate" and "135-candy" both occur.
        digits = re.match(r"(\d+)-", code or "")
        if not digits:
            continue
        problem_id = str(int(digits.group(1)))
        # The bundle repeats a record wherever a view embeds it. First wins;
        # the copies were identical in every build observed.
        if problem_id in problems:
            continue
        lists = [flag for flag in NEETCODE_FLAGS if re.search(flag + r":!0", blob)]
        problems[problem_id] = {
            "title": match.group(1).encode().decode("unicode_escape"),
            "difficulty": (field(blob, "difficulty") or "Unknown").title(),
            # Needed for problems this repo has no README row for — without a
            # slug there is no way to link them back to leetcode.com.
            "slug": match.group(3).strip("/"),
            "group": match.group(2),
            # Everything in the bundle is on the "all" list by definition; the
            # flags name the smaller lists it is also on.
            "lists": lists + ["neetcodeAll"],
        }

    if len(problems) < NEETCODE_MIN_TOTAL:
        raise SystemExit(f"neetcode: only {len(problems)} problems (expected >= {NEETCODE_MIN_TOTAL})")
    for flag, expected in NEETCODE_EXPECTED.items():
        found = sum(1 for p in problems.values() if flag in p["lists"])
        if found != expected:
            raise SystemExit(f"neetcode {flag}: extracted {found}, expected {expected}")
    return bundle_name.group(1), problems


def fetch_leetcode_top100():
    """-> {lc id: {title, difficulty, group, lists}} for the Top 100 Liked plan."""
    raw = get(LEETCODE_GRAPHQL, {"query": LEETCODE_QUERY, "variables": {"slug": LEETCODE_PLAN}})
    payload = json.loads(raw)
    if payload.get("errors"):
        raise SystemExit(f"leetcode: {payload['errors']}")
    detail = (payload.get("data") or {}).get("studyPlanV2Detail")
    if not detail:
        raise SystemExit(f"leetcode: no study plan '{LEETCODE_PLAN}' in the response")

    problems = {}
    for group in detail["planSubGroups"]:
        for question in group["questions"]:
            problems[str(int(question["questionFrontendId"]))] = {
                "title": question["title"],
                "difficulty": question["difficulty"].title(),
                "slug": question["titleSlug"],
                "group": group["name"],
                "lists": ["top100liked"],
            }
    if len(problems) != LEETCODE_EXPECTED:
        raise SystemExit(f"leetcode: {len(problems)} problems, expected {LEETCODE_EXPECTED}")
    return problems


# ── Merge ───────────────────────────────────────────────────────────────────

def build():
    bundle, neetcode = fetch_neetcode()
    leetcode = fetch_leetcode_top100()

    merged = {}
    # `group` is per-taxonomy: NeetCode files Two Sum under "Arrays & Hashing"
    # while LeetCode's plan files it under "Hashing". The roadmap maps each
    # taxonomy separately, so both are kept rather than one overwriting the other.
    for source, records in (("neetcode", neetcode), ("leetcodePlan", leetcode)):
        for problem_id, record in records.items():
            entry = merged.setdefault(problem_id, {
                "id": problem_id, "title": record["title"], "slug": record["slug"],
                "difficulty": record["difficulty"], "groups": {}, "lists": []
            })
            entry["groups"][source] = record["group"]
            entry["lists"] = sorted(set(entry["lists"]) | set(record["lists"]))

    problems = sorted(merged.values(), key=lambda r: int(r["id"]))
    counts = {}
    for entry in problems:
        for name in entry["lists"]:
            counts[name] = counts.get(name, 0) + 1

    return {
        "_comment": (
            "Curated LeetCode lists the roadmap page can filter by. Generated by "
            "script/fetch_problem_lists.py and read by site/build-roadmap.js and "
            "script/fix_readme_tags.py (which writes them as README Note tags) — "
            "regenerate by hand, never at build time. `groups` gives each source's "
            "own topic name, which data/roadmap.json maps onto roadmap topics. The "
            "`google` and `must` lists are not here; they come from README.md."
        ),
        "sources": {
            "neetcode": {"url": NEETCODE_PRACTICE, "bundle": bundle},
            "leetcodePlan": {"url": f"https://leetcode.com/studyplan/{LEETCODE_PLAN}/"},
        },
        "counts": dict(sorted(counts.items())),
        "problems": problems,
    }


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--stdout", action="store_true", help="print instead of writing")
    parser.add_argument("--check", action="store_true", help="exit 1 if the file is stale")
    args = parser.parse_args()

    repo_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    out_path = os.path.join(repo_root, "data", "problem_lists.json")

    data = build()
    text = json.dumps(data, indent=1, ensure_ascii=False) + "\n"

    if args.stdout:
        sys.stdout.write(text)
        return

    if args.check:
        # The neetcode bundle hash changes on every deploy even when the lists
        # do not, so staleness is judged on the problems, not the whole file.
        if not os.path.exists(out_path):
            sys.exit(f"{out_path} is missing")
        with open(out_path, encoding="utf-8") as handle:
            if json.load(handle).get("problems") != data["problems"]:
                sys.exit(f"{out_path} is stale — re-run without --check")
        print(f"{out_path} is up to date ({data['counts']})")
        return

    with open(out_path, "w", encoding="utf-8") as handle:
        handle.write(text)
    print(f"wrote {out_path}: {data['counts']}")


if __name__ == "__main__":
    main()
