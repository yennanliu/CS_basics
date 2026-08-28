#!/usr/bin/env python3
"""
Normalise and complete the `Note` column tags in README.md.

The Note column carries two kinds of tag that this script owns:

  **type tag**   the leading bold pattern label (`**array**`, `**dp**`, ...).
                 It says which section's technique the row is filed under, so it
                 is deliberately NOT rewritten here even when LeetCode files the
                 problem elsewhere. What the script does is *complete* it: when
                 none of LeetCode's official topics back the bold label, the
                 real topics are appended as backticked tags so the row still
                 says what the problem actually is.

  company tag    `google`, `amazon`, `fb`, ... Spellings had drifted (`M$`,
                 `MS`, `msft` and `microsoft` all meant Microsoft, which broke
                 `script/get_company_LC.sh` and the roadmap's Google list). The
                 script canonicalises every one of them and adds the missing
                 ones for the eight companies the README already tracks widely.

Two vendored caches feed it, so a normal run needs neither the network nor the
PDFs:

  data/lc_topic_tags.json      LeetCode's official topicTags + difficulty,
                               from the public GraphQL API
  data/company_lc_tags.json    company -> LC ids, from the company-frequency
                               PDFs under doc/

Usage:
    python3 script/fix_readme_tags.py                 # rewrite README.md
    python3 script/fix_readme_tags.py --check         # exit 1 if out of date
    python3 script/fix_readme_tags.py --report        # what would change, no write
    python3 script/fix_readme_tags.py --refresh-topics     # re-fetch from LeetCode
    python3 script/fix_readme_tags.py --refresh-companies  # re-parse the PDFs
"""

import argparse
import json
import os
import re
import subprocess
import sys
import time
import urllib.request
from collections import Counter
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
README = ROOT / "README.md"
TOPIC_CACHE = ROOT / "data" / "lc_topic_tags.json"
COMPANY_CACHE = ROOT / "data" / "company_lc_tags.json"

# ── Vocabulary ───────────────────────────────────────────────────────────────

# The eight companies the README already tags on hundreds of rows. Missing tags
# are filled in for these only: widening the set past this pushes some rows over
# twenty tags, at which point the column stops being readable.
CORE_COMPANIES = ["google", "amazon", "fb", "apple", "microsoft", "uber",
                  "linkedin", "bloomberg"]

# Every spelling seen in the README (and the obvious neighbours), lowercased,
# mapped to the one form kept. `fb` beats `facebook`/`meta` because it is what
# 221 rows already used.
COMPANY_ALIASES = {
    "google": "google", "goog": "google",
    "amazon": "amazon", "amz": "amazon", "aws": "amazon",
    "fb": "fb", "facebook": "fb", "meta": "fb",
    "apple": "apple", "appl": "apple",
    "microsoft": "microsoft", "msft": "microsoft", "m$": "microsoft",
    "ms": "microsoft", "micorsoft": "microsoft",
    "uber": "uber",
    "linkedin": "linkedin", "linked in": "linkedin",
    "bloomberg": "bloomberg", "bb": "bloomberg",
    # hand-added long tail: canonicalised in place, never added to a new row
    "goldman sachs": "goldman sachs", "gs": "goldman sachs",
    "goldman": "goldman sachs", "goldmansachs": "goldman sachs",
    "airbnb": "airbnb", "twitter": "twitter", "yahoo": "yahoo",
    "yelp": "yelp", "dropbox": "dropbox", "indeed": "indeed",
    "square": "square", "lyft": "lyft", "karat": "karat", "tesla": "tesla",
    "paypal": "paypal", "garena": "garena", "shopee": "shopee",
    "snapchat": "snapchat", "snap": "snapchat", "adobe": "adobe",
    "oracle": "oracle", "pinterest": "pinterest", "salesforce": "salesforce",
    "ebay": "ebay", "nvidia": "nvidia", "netflix": "netflix",
    "citadel": "citadel", "two sigma": "two sigma", "palantir": "palantir",
    "bytedance": "bytedance", "doordash": "doordash", "expedia": "expedia",
    "walmart": "walmart", "visa": "visa", "vmware": "vmware",
    "intuit": "intuit", "cisco": "cisco", "ibm": "ibm", "intel": "intel",
    "samsung": "samsung", "sap": "sap", "atlassian": "atlassian",
    "flipkart": "flipkart", "zillow": "zillow", "quora": "quora",
    "baidu": "baidu", "alibaba": "alibaba", "tencent": "tencent",
    "databricks": "databricks", "robinhood": "robinhood", "roblox": "roblox",
    "splunk": "splunk", "qualtrics": "qualtrics", "zenefits": "zenefits",
}

# The bold type tag -> the LeetCode topic slugs that would justify it. A bold
# tag with no overlap here is the signal that the row needs its real topics
# appended.
TYPE_TAG_TOPICS = {
    "array": {"array", "matrix"},
    "string": {"string"},
    "hash table": {"hash-table"},
    "set": {"hash-table", "ordered-set"},
    "dp": {"dynamic-programming"},
    "math": {"math", "number-theory", "combinatorics", "geometry",
             "probability-and-statistics"},
    "tree": {"tree", "binary-tree", "binary-search-tree", "trie"},
    "BST": {"binary-search-tree"},
    "greedy": {"greedy"},
    "binary search": {"binary-search"},
    "dfs": {"depth-first-search"},
    "bfs": {"breadth-first-search"},
    "design": {"design"},
    "stack": {"stack", "monotonic-stack"},
    "queue": {"queue", "monotonic-queue"},
    "two pointers": {"two-pointers"},
    "backtracking": {"backtracking"},
    "sort": {"sorting"},
    "recursion": {"recursion", "divide-and-conquer"},
    "bit manipulation": {"bit-manipulation"},
    "heap": {"heap-priority-queue"},
    "linked list": {"linked-list"},
    "graph": {"graph", "union-find", "topological-sort", "shortest-path",
              "minimum-spanning-tree", "strongly-connected-component",
              "eulerian-circuit", "biconnected-component"},
    "sliding window": {"sliding-window"},
    "prefix sum": {"prefix-sum"},
    "geometry": {"geometry"},
    "simulation": {"simulation"},
    "scan line": {"line-sweep", "sweep-line"},
    # not a LeetCode topic; the repo's own label for the 56/57/253 family
    "interval": {"sorting", "array", "greedy", "line-sweep", "sweep-line",
                 "heap-priority-queue"},
    "concurrency": {"concurrency"},
    "sql": {"database"},
    "shell": {"shell"},
}

# LeetCode topic slug -> the label this README writes it as.
#
# Deliberately not exhaustive. A slug missing here is never appended, which is
# how `suffix-automaton`, `range-minimum-maximum-query` and the rest of
# LeetCode's long tail stay off an interview-prep row: they name a proof
# technique, not something you would revise from.
TOPIC_LABELS = {
    # arrays & strings
    "array": "array", "string": "string", "hash-table": "hash table",
    "matrix": "matrix", "two-pointers": "two pointers",
    "sliding-window": "sliding window", "prefix-sum": "prefix sum",
    "counting": "counting", "string-matching": "string matching",
    "rolling-hash": "rolling hash",
    "knuth-morris-pratt-algorithm": "kmp", "manacher": "manacher",
    # search & sort
    "binary-search": "binary search", "sorting": "sort",
    "merge-sort": "merge sort", "quickselect": "quickselect",
    "divide-and-conquer": "divide and conquer", "ordered-set": "ordered set",
    # recursion family
    "recursion": "recursion", "backtracking": "backtracking",
    "dynamic-programming": "dp", "memoization": "memoization",
    "dp-on-trees": "dp on trees", "bitmask": "bitmask",
    "game-theory": "game theory", "minimax-algorithm": "minimax",
    "greedy": "greedy", "math": "math", "number-theory": "number theory",
    "combinatorics": "combinatorics", "bit-manipulation": "bit manipulation",
    "geometry": "geometry", "simulation": "simulation",
    # linear structures
    "stack": "stack", "monotonic-stack": "monotonic stack", "queue": "queue",
    "monotonic-queue": "monotonic queue", "heap-priority-queue": "heap",
    "linked-list": "linked list", "design": "design",
    "data-stream": "data stream", "interactive": "interactive",
    # trees
    "tree": "tree", "binary-tree": "binary tree",
    "binary-search-tree": "bst", "trie": "trie",
    "segment-tree": "segment tree",
    "binary-indexed-tree": "binary indexed tree",
    "lowest-common-ancestor": "lca",
    # graphs
    "depth-first-search": "dfs", "breadth-first-search": "bfs",
    "graph": "graph", "union-find": "union find",
    "topological-sort": "topological sort", "shortest-path": "shortest path",
    "dijkstra": "dijkstra", "minimum-spanning-tree": "minimum spanning tree",
    "line-sweep": "scan line", "sweep-line": "scan line",
    # non-algorithm sections
    "database": "sql", "concurrency": "concurrency", "shell": "shell",
}

# At most this many official topics are appended to one row.
MAX_TOPICS_ADDED = 3

# Sections whose rows LeetCode does not tag with algorithm topics.
NON_ALGO_SECTIONS = {"SQL", "Shell Script", "Concurrency"}

# ── README parsing ───────────────────────────────────────────────────────────

ROW_ID = re.compile(r"^\s*(\d+)")


def split_row(line):
    """A table row's cells, or None when the line is not a problem row."""
    if not line.strip().startswith("|"):
        return None
    cells = [c.strip() for c in line.strip().strip("|").split("|")]
    if len(cells) < 3 or not ROW_ID.match(cells[0]):
        return None
    return cells


def iter_rows(lines):
    """(index, lc_id, section, cells) for every problem row, in file order."""
    h2 = h3 = None
    for i, line in enumerate(lines):
        if line.startswith("## "):
            h2, h3 = line[3:].strip(), None
        elif line.startswith("### "):
            h3 = line[4:].strip()
        cells = split_row(line)
        if cells is None:
            continue
        yield i, int(ROW_ID.match(cells[0]).group(1)), h2, h3, cells


def split_tags(cell):
    """
    Split the Note cell on commas that sit outside a `backtick span`.

    Backticks matter: one row carries `` `tree, M$` `` — a tag pair that lost
    its separating backticks — and several carry prose like
    `` `#416 Partition Equal Subset Sum, #473 Matchsticks to Square` ``, whose
    comma must NOT split and whose "Square" must never be read as the company.
    """
    parts, buf, in_code = [], [], False
    for ch in cell:
        if ch == "`":
            in_code = not in_code
            buf.append(ch)
        elif ch == "," and not in_code:
            parts.append("".join(buf))
            buf = []
        else:
            buf.append(ch)
    parts.append("".join(buf))
    return parts


def tag_key(part):
    """The bare tag inside a part, or '' when the part is prose/markup."""
    t = part.strip()
    t = re.sub(r"^\*\*(.*)\*\*$", r"\1", t).strip()
    t = t.strip("`").strip()
    return t.lower()


def canonical_company(part):
    """The canonical company for a whole tag, or None."""
    key = tag_key(part)
    if not key or len(key) > 20:
        return None
    return COMPANY_ALIASES.get(key)


def balance_backticks(cell):
    """
    Repair a tag that lost its opening backtick.

    One row reads ``sliding window`, counter`` — a single stray backtick, which
    inverts the code-span parity for the whole rest of the cell and hides every
    tag after it from the parser (that row kept re-collecting company tags on
    each run because of it). Only cells with an odd backtick count are touched,
    and only the part holding the lone backtick.
    """
    if cell.count("`") % 2 == 0:
        return cell, False
    parts = cell.split(",")
    for i, part in enumerate(parts):
        if part.count("`") != 1:
            continue
        lead = re.match(r"^(\s*)", part).group(1)
        body = part.strip()
        if body.endswith("`"):
            parts[i] = "%s`%s" % (lead, body)
        elif body.startswith("`"):
            parts[i] = "%s%s`" % (lead, body)
        else:
            continue
        return ",".join(parts), True
    return cell, False


def normalise_cell(cell):
    """
    Rewrite every company tag in the cell to `` `canonical` ``, in place.

    Splitting the badly-quoted `` `tree, M$` `` is safe only when *both* halves
    are known tags, which is what keeps the prose spans intact.
    """
    out, changed = [], False
    for part in split_tags(cell):
        lead = re.match(r"^(\s*)", part).group(1)
        body = part.strip()
        inner = body.strip("`").strip()
        # rescue a backtick span that swallowed a comma between two real tags
        if (body.startswith("`") and body.endswith("`") and "," in inner
                and all(canonical_company(p) or tag_key(p) in TYPE_TAG_TOPICS
                        for p in inner.split(","))):
            for p in inner.split(","):
                comp = canonical_company(p)
                out.append(" `%s`" % (comp if comp else tag_key(p)))
            changed = True
            continue
        comp = canonical_company(part)
        if comp:
            new = "%s`%s`" % (lead, comp)
            if new != part:
                changed = True
            out.append(new)
        else:
            out.append(part)
    return ",".join(out), changed


# Shorthand the README already uses for a topic, so an appended tag does not
# restate what the row says in its own words.
TOPIC_SYNONYMS = {
    "mono stack": "monotonic stack", "monostack": "monotonic stack",
    "ascending stack": "monotonic stack", "decreasing stack": "monotonic stack",
    "mono queue": "monotonic queue", "monoqueue": "monotonic queue",
    "2 pointers": "two pointers", "2 pointer": "two pointers",
    "two pointer": "two pointers", "2 ptr": "two pointers",
    "pre-sum": "prefix sum", "presum": "prefix sum", "pre sum": "prefix sum",
    "hashmap": "hash table", "hash map": "hash table",
    "hashtable": "hash table", "hashset": "hash table", "hash set": "hash table",
    "dict": "hash table", "counter": "hash table",
    "linkedlist": "linked list", "linked-list": "linked list",
    "binary search tree": "bst",
    "dynamic programming": "dp", "dynamic-programming": "dp",
    "priority queue": "heap", "pq": "heap", "priorityqueue": "heap",
    "union-find": "union find", "unionfind": "union find", "uf": "union find",
    "topological": "topological sort", "topo sort": "topological sort",
    "sliding-window": "sliding window", "window": "sliding window",
    "backtrack": "backtracking", "sorting": "sort",
    "bit manipulate": "bit manipulation",
}


def present_tags(cell):
    """
    Every topic the cell already names, lowercased and canonicalised.

    Whole tags are not enough: rows write topics into prose too ("top down,
    bottom up dfs", "Ascending Stack"), and appending `dfs` next to those reads
    as noise. So each part also contributes its words and its two- and
    three-word shingles.
    """
    seen = set()

    # naming the specialised structure already covers the general one
    IMPLIES = {"monotonic stack": "stack", "monotonic queue": "queue",
               "dp on trees": "dp", "binary tree": "tree", "bst": "tree",
               "merge sort": "sort", "kmp": "string matching",
               "manacher": "string matching", "dijkstra": "shortest path"}

    def add(text):
        text = text.strip()
        if not text:
            return
        text = TOPIC_SYNONYMS.get(text, text)
        seen.add(text)
        if text in IMPLIES:
            seen.add(IMPLIES[text])

    for part in split_tags(cell):
        add(tag_key(part))
        # up to trigrams, so "Divide-and-Conquer" and "monotonic stack" count
        words = re.findall(r"[a-z]+", part.lower())
        for i in range(len(words)):
            for n in (1, 2, 3):
                if i + n <= len(words):
                    add(" ".join(words[i:i + n]))
    return seen


def replace_note_cell(line, new_cell):
    """Put `new_cell` back as the second-to-last cell of the row."""
    prefix = line[:len(line) - len(line.lstrip())]
    body = line.strip()
    lead_pipe = body.startswith("|")
    trail_pipe = body.endswith("|")
    cells = body.strip("|").split("|")
    cells[-2] = " %s " % new_cell.strip()
    rebuilt = "|".join(cells)
    return "%s%s%s%s" % (prefix, "|" if lead_pipe else "", rebuilt,
                         "|" if trail_pipe else "")


TOPIC_COMMENT = (
    "LeetCode's official topicTags and difficulty for every problem README.md "
    "lists. Fetched from the public GraphQL API by "
    "script/fix_readme_tags.py --refresh-topics and read back by that script to "
    "complete the README's type tags. Vendored, not built — refresh by hand."
)

COMPANY_COMMENT = (
    "Which LeetCode problems each company is known to ask, parsed from the "
    "company-frequency PDFs under doc/ (and, for Google, unioned with "
    "doc/google_leetcode_problems_by_tags.md) by "
    "script/fix_readme_tags.py --refresh-companies. Only the eight companies "
    "README.md tags widely are kept. Vendored, not built — refresh by hand."
)


def load_cache(path):
    """The cache without its `_comment` provenance key."""
    return {k: v for k, v in json.loads(path.read_text()).items()
            if k != "_comment"}


def write_cache(path, comment, mapping):
    """
    One line per key, in the style of data/problem_lists.json.

    A refresh then shows up as a handful of changed lines rather than one
    reflowed blob, which is the only way a vendored cache stays reviewable.
    """
    lines = [" %s: %s," % (json.dumps("_comment"), json.dumps(comment))]
    for key, value in mapping.items():
        lines.append(" %s: %s," % (json.dumps(key),
                                   json.dumps(value, separators=(", ", ": "))))
    lines[-1] = lines[-1].rstrip(",")
    path.write_text("{\n%s\n}\n" % "\n".join(lines))


# ── Cache builders ───────────────────────────────────────────────────────────

GRAPHQL = """query ($limit: Int!, $skip: Int!) {
  problemsetQuestionList: questionList(categorySlug:"", limit:$limit, skip:$skip, filters:{}) {
    total: totalNum
    questions: data { questionFrontendId difficulty topicTags { slug } }
  }
}"""


def refresh_topics():
    """Re-fetch LeetCode's official topicTags into data/lc_topic_tags.json."""
    out, skip = {}, 0
    while True:
        payload = json.dumps({"query": GRAPHQL,
                              "variables": {"limit": 500, "skip": skip}}).encode()
        req = urllib.request.Request(
            "https://leetcode.com/graphql/", data=payload,
            headers={"Content-Type": "application/json",
                     "User-Agent": "Mozilla/5.0",
                     "Referer": "https://leetcode.com/problemset/"})
        with urllib.request.urlopen(req, timeout=60) as resp:
            data = json.load(resp)["data"]["problemsetQuestionList"]
        if not data["questions"]:
            break
        for q in data["questions"]:
            out[q["questionFrontendId"]] = {
                "difficulty": q["difficulty"],
                "topics": [t["slug"] for t in q["topicTags"]],
            }
        skip += len(data["questions"])
        print("  %d/%d" % (skip, data["total"]), file=sys.stderr)
        if skip >= data["total"]:
            break
        time.sleep(0.4)
    # keep only what the README asks about, so the cache stays reviewable
    wanted = {str(lc) for _, lc, _, _, _ in iter_rows(README.read_text().split("\n"))}
    trimmed = {k: out[k] for k in sorted(wanted & set(out), key=int)}
    write_cache(TOPIC_CACHE, TOPIC_COMMENT, trimmed)
    print("wrote %s (%d problems)" % (TOPIC_CACHE, len(trimmed)), file=sys.stderr)


# `  388    Longest Absolute File Path (/problems/…)   40.3%   Medium`
PDF_ROW = re.compile(r"^\s*(\d{1,4})\s{2,}(\S.*?)\s*$")
PDF_DIRS = ["doc/Leetcode_company_frequency-master", "doc/leetcode_company_V1",
            "doc/leetcode_company_V4", "doc/leetcode_company_V5",
            "doc/leetcode_company_V6"]


def pdf_company(filename):
    stem = os.path.splitext(os.path.basename(filename))[0]
    stem = re.sub(r"\s*-\s*LeetCode.*$", "", stem, flags=re.I)
    stem = re.sub(r"\s*\d+\s*(year|months?)\s*$", "", stem, flags=re.I)
    stem = re.sub(r"\s+alltime$", "", stem, flags=re.I)
    stem = re.sub(r"^lc[-_]", "", stem.strip().lower())
    stem = re.sub(r"_all$", "", stem)
    return COMPANY_ALIASES.get(stem)


def refresh_companies():
    """Re-parse the company-frequency PDFs into data/company_lc_tags.json."""
    out = {}
    for rel in PDF_DIRS:
        directory = ROOT / rel
        if not directory.is_dir():
            continue
        for name in sorted(os.listdir(directory)):
            if not name.lower().endswith(".pdf"):
                continue
            company = pdf_company(name)
            if company not in CORE_COMPANIES:
                continue
            text = subprocess.run(["pdftotext", "-layout", str(directory / name), "-"],
                                  capture_output=True, text=True, timeout=180).stdout
            for line in text.split("\n"):
                m = PDF_ROW.match(line)
                if not m:
                    continue
                lc, rest = int(m.group(1)), m.group(2)
                if not 1 <= lc <= 3500 or not re.search(r"[A-Za-z]{3}", rest):
                    continue
                if re.match(r"^(Easy|Medium|Hard|\d+\.\d+%)", rest):
                    continue
                out.setdefault(company, set()).add(lc)
    # doc/google_leetcode_problems_by_tags.md predates the PDFs and covers ~450
    # Google problems they miss; the union is what the site's Google list wants
    gdoc = ROOT / "doc" / "google_leetcode_problems_by_tags.md"
    if gdoc.exists():
        extra = {int(n) for n in re.findall(r"^- #(\d+) ", gdoc.read_text(), re.M)}
        out.setdefault("google", set()).update(extra)
    write_cache(COMPANY_CACHE, COMPANY_COMMENT,
                {k: sorted(out[k]) for k in sorted(out)})
    for company in sorted(out, key=lambda c: -len(out[c])):
        print("  %-10s %d" % (company, len(out[company])), file=sys.stderr)
    print("wrote %s" % COMPANY_CACHE, file=sys.stderr)


# ── The rewrite ──────────────────────────────────────────────────────────────

def rewrite(lines, topics, companies):
    """Return (new_lines, stats). Pure: `lines` is not mutated."""
    lines = list(lines)
    stats = Counter()
    company_ids = {c: set(v) for c, v in companies.items()}

    for idx, lc, h2, h3, cells in list(iter_rows(lines)):
        cell = cells[-2]
        original = cell

        # 0) a stray backtick would hide every tag after it from the parser
        cell, fixed = balance_backticks(cell)
        if fixed:
            stats["backticks_repaired"] += 1

        # 1) one spelling per company
        cell, changed = normalise_cell(cell)
        if changed:
            stats["company_normalised"] += 1

        # 2) a bold type tag on every row
        bold = re.findall(r"\*\*(.+?)\*\*", cell)
        if not bold:
            parts = split_tags(cell)
            first = tag_key(parts[0])
            if first in TYPE_TAG_TOPICS or first.lower() in TYPE_TAG_TOPICS:
                label = first if first in TYPE_TAG_TOPICS else first.lower()
                parts[0] = "**%s**" % label
                cell = ",".join(parts)
                bold = [label]
                stats["type_tag_added"] += 1

        entry = topics.get(str(lc))

        # 3) the problem's real topics, when the bold tag names none of them
        if entry and bold and h2 not in NON_ALGO_SECTIONS:
            tag = bold[0]
            backing = TYPE_TAG_TOPICS.get(tag) or TYPE_TAG_TOPICS.get(tag.lower())
            if backing is not None and not (backing & set(entry["topics"])):
                have = present_tags(cell)
                # LeetCode lists a problem's topics primary-first, so the
                # window is fixed at the first MAX_TOPICS_ADDED of them. Fixing
                # the window (rather than filling up to it) is what keeps the
                # script idempotent: a rerun sees the same three labels already
                # present and adds nothing.
                known = [TOPIC_LABELS[s] for s in entry["topics"]
                         if s in TOPIC_LABELS][:MAX_TOPICS_ADDED]
                add = [label for label in known if label not in have]
                if add:
                    cell = cell.rstrip().rstrip(",") + "".join(
                        ", `%s`" % a for a in add)
                    stats["topics_added"] += 1
                    stats["topic_tags_added"] += len(add)

        # 4) the core-eight company tags the row is missing
        have = {tag_key(p) for p in split_tags(cell)}
        add = [c for c in CORE_COMPANIES
               if lc in company_ids.get(c, ()) and c not in have]
        if add:
            cell = cell.rstrip().rstrip(",") + "".join(", `%s`" % c for c in add)
            stats["company_rows_touched"] += 1
            stats["company_tags_added"] += len(add)

        if cell != original:
            lines[idx] = replace_note_cell(lines[idx], cell)
            stats["rows_changed"] += 1

    return lines, stats


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--check", action="store_true",
                    help="exit 1 if README.md is not already normalised")
    ap.add_argument("--report", action="store_true",
                    help="print the per-row diff summary without writing")
    ap.add_argument("--refresh-topics", action="store_true",
                    help="re-fetch topic tags from LeetCode (needs network)")
    ap.add_argument("--refresh-companies", action="store_true",
                    help="re-parse the company PDFs (needs pdftotext)")
    args = ap.parse_args()

    if args.refresh_topics:
        refresh_topics()
    if args.refresh_companies:
        refresh_companies()
    if args.refresh_topics or args.refresh_companies:
        if not (args.check or args.report):
            return 0

    for cache in (TOPIC_CACHE, COMPANY_CACHE):
        if not cache.exists():
            print("missing cache %s — run with --refresh-topics "
                  "--refresh-companies" % cache, file=sys.stderr)
            return 2

    topics = load_cache(TOPIC_CACHE)
    companies = load_cache(COMPANY_CACHE)
    text = README.read_text()
    lines = text.split("\n")
    new_lines, stats = rewrite(lines, topics, companies)

    for key in ("rows_changed", "backticks_repaired", "company_normalised",
                "type_tag_added", "topics_added", "topic_tags_added",
                "company_rows_touched", "company_tags_added"):
        print("%-22s %d" % (key, stats[key]))

    if args.report:
        for old, new in zip(lines, new_lines):
            if old != new:
                print("\n- %s\n+ %s" % (old[:220], new[:220]))
        return 0
    if args.check:
        return 1 if stats["rows_changed"] else 0
    README.write_text("\n".join(new_lines))
    return 0


if __name__ == "__main__":
    sys.exit(main())
