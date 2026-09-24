#!/usr/bin/env python3
"""
The gate for README.md's problem index and the practice log — what
site/e2e-check.js is for _site/ and script/check_skills.py is for .claude/skills/.

    python3 script/check_readme.py                  # PASS/FAIL lines, exit 1 on any FAIL
    python3 script/check_readme.py --verbose        # name every offending row
    python3 script/check_readme.py --strict         # ignore the baseline: every known problem fails too
    python3 script/check_readme.py --update-baseline    # accept the current state as the new floor

Why it exists. README is the only complete copy of the index, and nothing read
it back: the Sep 2026 review found 42 solution links pointing at files that do
not exist (typos — `letcode_python/`, `.py.py` — and files that moved), 21 ids
filed in both of README's two table sets, 420 spellings of the status cell, and
an impossible date (`20260229`) in data/progress.txt that every reader had been
parsing as a string. e2e-check.js cannot see any of it: the solution links are
GitHub URLs by the time the page is built.

What it checks, per rule:

  rows         every row has an LC number and a linked title
  links        every relative solution link resolves to a file in the repo
  duplicates   no id appears in both the main `##` tables and the imported
               `## Newly Added` set (a problem may sit in two *main* sections —
               LC 547 is under both DFS and Graph — that is reported, not failed)
  imported     every row under `## Newly Added` carries the status `imported`
               and no main-table row does — the cell is what lets the site
               count practised rows apart from imported drafts, and a row whose
               cell disagrees with its heading was filed under the wrong one
  status       every main-table status cell parses as
                 <OK|AGAIN|not start> <stars> (<note>)…  — the shape three
               scripts already read; the notes stay free text
  dates        every `YYYYMMDD` line in data/progress.txt is a real date
  unlinked     solution files no row links to — REPORTED, never failed, because
               contest problems are routinely filed before their README row

The baseline. The gate fails on *regressions*: anything not in
data/readme_check_baseline.json. When it landed the baseline excused 31 dead
links, 17 status cells, 10 duplicates and one date; the Sep 2026 burn-down
repointed the 30 `C++/` and `Python/` links at the kamyu104 repo the rows were
imported from (every target verified to exist there), fixed the one typo,
rewrote the 17 cells into the grammar, corrected the date, and then merged
the 10 duplicates into their main rows — so the file is empty, and stays the
place a finding goes when it genuinely cannot be fixed yet. The baseline holds each finding's identity —
the row's LC number with the exact offending string, never a line number — and
every entry excuses exactly one finding. So a fix shrinks it visibly, a new
problem cannot hide behind an old one, and a baselined value that reappears on
another row (`AGAIN !!! (2)` on LC 300 when only LC 139 was excused) fails like
any other regression. `--update-baseline` rewrites it; `--strict` ignores it.

Row shape: `| # | Title | Solution | Time | Space | Difficulty | Note | Status |`
(read from the ends, as site/build-roadmap.js and script/suggest_review.py do,
because three tables have a different middle).
"""
import argparse
import json
import os
import re
import sys
import time

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
README = os.path.join(ROOT, "README.md")
PROGRESS = os.path.join(ROOT, "data", "progress.txt")
BASELINE = os.path.join(ROOT, "data", "readme_check_baseline.json")

IMPORTED_HEADING = "Newly Added"
IMPORTED_STATUS = "imported"  # the status cell of every row in the imported set
ROW_RE = re.compile(r"^\|\s*(\d{1,4})\s*\|")
LINK_RE = re.compile(r"\[([^\]]*)\]\(\s*([^)\s]+)\s*\)")

# The status grammar. Three scripts read this cell — suggest_review.py takes the
# word (`STATUS_WORD`) and counts the stars, extract_must_lc.py reads MUST in
# any casing, eval_lc_readiness.py builds the cost curve from the star run — so
# the parts they read are pinned and the notes are left as free text:
#   AGAIN*** (3)              OK* (2) (but again)        AGAIN (not start)
#   again************ (4)(MUST)                          not start
# A cell may also be empty (the imported set, and 30 main rows), or a bare
# paren note. What it may not be is anything that puts prose where the word
# goes, because the word is what the planners key on.
STATUS_RE = re.compile(
    r"^\s*(?:(?:OK|AGAIN|Again|again|ok|Not start|not start)\s*\*{0,}\s*)?"
    r"(?:\(\s*[^()]*\)\s*)*\*?\s*$"
)

SOLUTION_TREES = {
    "python": ("leetcode_python", ".py"),
    "java": (os.path.join("leetcode_java", "src", "main", "java", "LeetCodeJava"), ".java"),
}


class Report:
    """PASS/FAIL lines and a tally, in the shape the other gates print."""

    def __init__(self, verbose=False):
        self.passed = 0
        self.failed = 0
        self.verbose = verbose

    def section(self, title):
        print("\n== %s ==" % title)

    def check(self, name, cond, detail=""):
        if cond:
            self.passed += 1
        else:
            self.failed += 1
        print("  %s  %s%s" % ("PASS" if cond else "FAIL", name, "  — " + detail if detail else ""))
        return cond

    def info(self, text):
        if self.verbose:
            print("  INFO  " + text)


# ── Parsing ─────────────────────────────────────────────────────────────────
def parse_rows(text):
    """README text -> [row], one per table row that starts with an LC number.

    Each row: line (1-based), id (int), section, imported (bool), title,
    title_link, solutions [(label, target)], note, status.
    """
    rows = []
    section = None
    imported = False
    for i, line in enumerate(text.split("\n"), 1):
        if line.startswith("## "):
            section = line[3:].strip()
            imported = IMPORTED_HEADING in section
            continue
        if line.startswith("### ") and imported:
            section = line[4:].strip()
            continue
        m = ROW_RE.match(line)
        if not m:
            continue
        cells = [c.strip() for c in line.strip().strip("|").split("|")]
        if len(cells) < 4:
            continue
        title_m = LINK_RE.search(cells[1])
        rows.append({
            "line": i,
            "id": int(m.group(1)),
            "section": section or "",
            "imported": imported,
            "title": (title_m.group(1) if title_m else cells[1]).strip(),
            "title_link": title_m.group(2) if title_m else "",
            "solutions": [(lab.strip(), target) for lab, target in LINK_RE.findall(cells[2])],
            "note": cells[-2] if len(cells) >= 8 else "",
            "status": cells[-1] if len(cells) >= 8 else "",
        })
    return rows


def local_path(target):
    """A relative solution link -> repo-relative path, or None for a URL."""
    if re.match(r"^[a-z][a-z0-9+.-]*:", target):
        return None
    return re.sub(r"^(\./)+", "", target.split("#")[0])


def resolves(root, rel):
    """True when `rel` names a regular file inside `root`.

    Not `os.path.exists`: that accepts a directory (`./leetcode_python/` is not a
    solution, and `..` walks up to the root itself and "exists") and follows a
    `../` out of the repo. A solution link has to land on a file we ship.
    """
    root_real = os.path.realpath(root)
    candidate = os.path.realpath(os.path.join(root_real, rel))
    return (os.path.commonpath((root_real, candidate)) == root_real
            and candidate != root_real
            and os.path.isfile(candidate))


def walk(root, tree, ext):
    """Files under `root/tree` with `ext`, as root-relative paths."""
    out = []
    for dirpath, _dirs, files in os.walk(os.path.join(root, tree)):
        for f in files:
            if f.endswith(ext):
                out.append(os.path.relpath(os.path.join(dirpath, f), root))
    return out


def log_dates(text):
    """-> [(line, 'YYYYMMDD', ok)] for every date header in the log."""
    out = []
    for i, raw in enumerate(text.split("\n"), 1):
        m = re.match(r"^(\d{8})", raw.strip())
        if not m:
            continue
        try:
            time.strptime(m.group(1), "%Y%m%d")
            out.append((i, m.group(1), True))
        except ValueError:
            out.append((i, m.group(1), False))
    return out


# ── The findings ────────────────────────────────────────────────────────────
def findings(readme_text, progress_text, root=ROOT):
    """Everything wrong, as plain data, so the baseline and the report agree."""
    rows = parse_rows(readme_text)

    bad_rows = [r for r in rows if not r["title_link"]]

    dead = []
    linked = set()
    for r in rows:
        for label, target in r["solutions"]:
            rel = local_path(target)
            if rel is None:
                continue
            if resolves(root, rel):
                linked.add(rel)
            else:
                dead.append({"line": r["line"], "id": r["id"], "label": label, "target": target})

    main_ids = {}
    imported_ids = {}
    for r in rows:
        (imported_ids if r["imported"] else main_ids).setdefault(r["id"], []).append(r)
    cross = sorted(i for i in main_ids if i in imported_ids)
    within_main = sorted(i for i, rs in main_ids.items() if len(rs) > 1)

    bad_status = [r for r in rows if not r["imported"] and not STATUS_RE.match(r["status"])]
    misfiled = [r for r in rows if r["imported"] != (r["status"] == IMPORTED_STATUS)]

    dates = log_dates(progress_text)
    bad_dates = [(i, d) for i, d, ok in dates if not ok]

    unlinked = {}
    for name, (tree, ext) in SOLUTION_TREES.items():
        files = walk(root, tree, ext) if os.path.isdir(os.path.join(root, tree)) else []
        unlinked[name] = sorted(f for f in files if f not in linked)

    return {
        "rows": rows,
        "bad_rows": bad_rows,
        "dead_links": dead,
        "cross_duplicates": cross,
        "main_duplicates": within_main,
        "bad_status": bad_status,
        "misfiled": misfiled,
        "bad_dates": bad_dates,
        "dates": len(dates),
        "unlinked": unlinked,
        "linked": len(linked),
    }


# ── The baseline ────────────────────────────────────────────────────────────
# One entry per finding, and each entry excuses exactly one. The identity is the
# row's LC number plus the exact offending string — never a line number, which
# moves on every edit — so the same dead target or unparseable cell turning up
# on a *second* row is a new finding, not a baselined one. The date list is a
# multiset for the same reason: two `20260229` headers need two entries.
BASELINE_KEYS = ("dead_links", "cross_duplicates", "bad_status", "bad_dates")


def identity(key, finding):
    """The baseline entry for one finding, as JSON-shaped data."""
    if key == "dead_links":
        return {"id": finding["id"], "target": finding["target"]}
    if key == "bad_status":
        return {"id": finding["id"], "status": finding["status"]}
    if key == "bad_dates":
        return finding[1]
    return finding  # cross_duplicates: the id itself


def baseline_of(f):
    """The findings that may stay wrong, one entry each."""
    out = {
        "_comment": "Known problems in README.md / data/progress.txt that script/check_readme.py "
                    "tolerates. One entry per finding — the row's LC number and the exact offending "
                    "string, never a line number — and each entry excuses exactly one, so the gate "
                    "fails on anything NOT listed here, a fix shrinks this file, and a known-bad value "
                    "reappearing on another row fails. Regenerate with "
                    "`python3 script/check_readme.py --update-baseline`; see the script's docstring.",
    }
    for key in BASELINE_KEYS:
        out[key] = sorted((identity(key, x) for x in f[key]), key=json.dumps)
    return out


def load_baseline(path):
    if not os.path.exists(path):
        return {key: [] for key in BASELINE_KEYS}
    with open(path, encoding="utf-8") as fh:
        return json.load(fh)


def allower(base, strict=False):
    """-> allow(key, finding): True once per matching baseline entry.

    Entries are consumed, so a value listed once excuses one finding and the
    second occurrence is reported. `--strict` excuses nothing.
    """
    pool = {key: list(base.get(key, [])) for key in BASELINE_KEYS}

    def allow(key, finding):
        if strict:
            return False
        try:
            pool[key].remove(identity(key, finding))
        except ValueError:
            return False
        return True

    return allow


# ── The report ──────────────────────────────────────────────────────────────
def run(rep, f, base, strict=False):
    allow = allower(base, strict)

    rep.section("rows")
    rep.check("every row has an LC number and a linked title", not f["bad_rows"],
              "%d rows" % len(f["rows"]) if not f["bad_rows"]
              else "; ".join("line %d" % r["line"] for r in f["bad_rows"][:5]))

    rep.section("solution links")
    new_dead = [d for d in f["dead_links"] if not allow("dead_links", d)]
    known_dead = len(f["dead_links"]) - len(new_dead)
    rep.check("every relative solution link resolves", not new_dead,
              "%d resolve, %d dead%s" % (f["linked"], len(f["dead_links"]),
                                         ", %d baselined" % known_dead if known_dead else ""))
    for d in (new_dead if not rep.verbose else f["dead_links"]):
        rep_line = "line %d  LC %d  [%s](%s)" % (d["line"], d["id"], d["label"], d["target"])
        if d in new_dead:
            print("        " + rep_line)
        else:
            rep.info("baselined: " + rep_line)

    rep.section("duplicates")
    new_cross = [i for i in f["cross_duplicates"] if not allow("cross_duplicates", i)]
    rep.check("no id is filed in both the main tables and the imported set", not new_cross,
              "%d shared%s" % (len(f["cross_duplicates"]),
                               ", %d baselined" % (len(f["cross_duplicates"]) - len(new_cross))
                               if len(f["cross_duplicates"]) - len(new_cross) else ""))
    if new_cross:
        print("        " + ", ".join(str(i) for i in new_cross))
    rep.info("filed in two main sections (allowed): %s" % ", ".join(str(i) for i in f["main_duplicates"]))

    rep.section("imported set")
    # No baseline for this one: the set was stamped in one pass, so any row
    # whose cell disagrees with its heading is a new misfiling, not old debt.
    rep.check("every imported row says `imported` and no main row does", not f["misfiled"],
              "%d imported rows, %d main rows" % (
                  sum(1 for r in f["rows"] if r["imported"]),
                  sum(1 for r in f["rows"] if not r["imported"]))
              if not f["misfiled"] else "%d misfiled" % len(f["misfiled"]))
    for r in f["misfiled"][:10]:
        print("        line %d  LC %d  %s, status %r" % (
            r["line"], r["id"], "under Newly Added" if r["imported"] else "in a main table", r["status"]))

    rep.section("status column")
    new_status = [r for r in f["bad_status"] if not allow("bad_status", r)]
    rep.check("every main-table status cell parses (word, stars, notes)", not new_status,
              "%d cells checked, %d unparseable%s" % (
                  sum(1 for r in f["rows"] if not r["imported"]), len(f["bad_status"]),
                  ", %d baselined" % (len(f["bad_status"]) - len(new_status))
                  if len(f["bad_status"]) - len(new_status) else ""))
    for r in new_status[:10]:
        print("        line %d  LC %d  %r" % (r["line"], r["id"], r["status"]))

    rep.section("practice log")
    new_dates = [x for x in f["bad_dates"] if not allow("bad_dates", x)]
    rep.check("every date header in data/progress.txt is a real date", not new_dates,
              "%d dates, %d impossible%s" % (f["dates"], len(f["bad_dates"]),
                                             ", %d baselined" % (len(f["bad_dates"]) - len(new_dates))
                                             if len(f["bad_dates"]) - len(new_dates) else ""))
    for i, d in new_dates:
        print("        line %d  %s" % (i, d))

    rep.section("unlinked solution files (reported, not failed)")
    for name, files in f["unlinked"].items():
        print("  INFO  %s: %d files no README row links to" % (name, len(files)))
        for path in files[:5 if not rep.verbose else len(files)]:
            rep.info("  " + path)


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    ap.add_argument("--verbose", "-v", action="store_true", help="name every offending row, baselined ones included")
    ap.add_argument("--strict", action="store_true", help="ignore the baseline")
    ap.add_argument("--update-baseline", action="store_true", help="write the current findings as the baseline")
    ap.add_argument("--baseline", default=BASELINE, help=argparse.SUPPRESS)
    ap.add_argument("--readme", default=README, help=argparse.SUPPRESS)
    ap.add_argument("--progress", default=PROGRESS, help=argparse.SUPPRESS)
    args = ap.parse_args(argv)

    with open(args.readme, encoding="utf-8") as fh:
        readme_text = fh.read()
    progress_text = ""
    if os.path.exists(args.progress):
        with open(args.progress, encoding="utf-8") as fh:
            progress_text = fh.read()

    f = findings(readme_text, progress_text)

    if args.update_baseline:
        base = baseline_of(f)
        with open(args.baseline, "w", encoding="utf-8") as fh:
            json.dump(base, fh, indent=2)
            fh.write("\n")
        print("wrote %s: %d dead links, %d cross duplicates, %d status cells, %d dates" % (
            (os.path.relpath(args.baseline, ROOT),) + tuple(len(base[k]) for k in BASELINE_KEYS)))
        return 0

    rep = Report(verbose=args.verbose)
    run(rep, f, load_baseline(args.baseline), strict=args.strict)
    print("\n%d passed, %d failed" % (rep.passed, rep.failed))
    return 1 if rep.failed else 0


if __name__ == "__main__":
    sys.exit(main())
