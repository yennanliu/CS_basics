#!/usr/bin/env python3
"""
Write the body of a GitHub Release for CS_basics, and pick its version.

Called by .github/workflows/release.yml; runnable locally to preview a release:

    python3 script/release_notes.py version            # next tag: V<YYYYMMDD>[.N]
    python3 script/release_notes.py notes V20261107    # the post, since the last tag
    python3 script/release_notes.py notes V20261107 --since V20260902

The tag scheme is the one this repo already used by hand (V20260902, V20260502):
the UTC date, upper-case V. A second release on the same day becomes V20261107.2.

The post is computed from git, never typed: commits since the previous release,
what changed per top-level tree, the solution files that are new, and how many
days the practice log (data/progress.txt) gained. GitHub's --generate-notes then
appends the merged PRs underneath — most of this repo lands as direct pushes, so
the PR list alone would describe a fraction of a release.
"""
import argparse
import datetime as dt
import re
import subprocess
import sys

REPO = "yennanliu/CS_basics"
SITE = "https://yennanliu.github.io/CS_basics/"

# Top-level trees worth a row of their own, in the order the post lists them.
# Anything else is folded into "other".
AREAS = [
    ("leetcode_python", "Python solutions"),
    ("leetcode_java", "Java solutions"),
    ("leetcode_SQL", "SQL solutions"),
    ("doc/cheatsheet", "Cheatsheets"),
    ("doc/faq", "FAQs"),
    ("doc/behavioral", "Behavioral guides"),
    ("doc", "Other docs"),
    ("i18n", "繁體中文 translations"),
    ("system_design", "System design"),
    ("algo_demo", "Algorithm visualizers"),
    ("site", "Site tooling"),
    ("data", "Site data"),
    (".claude/skills", "Agent skills"),
    ("script", "Scripts"),
    (".github", "CI"),
]

DATE_LINE = re.compile(r"^\d{8}:")


def git(*args):
    return subprocess.run(["git", *args], check=True, capture_output=True, text=True).stdout


def tags():
    """Release tags (V<YYYYMMDD>[.N]), oldest first."""
    found = [t for t in git("tag", "--list").split() if re.fullmatch(r"V\d{8}(\.\d+)?", t)]

    def key(t):
        day, _, n = t[1:].partition(".")
        return (int(day), int(n or 1))

    return sorted(found, key=key)


def next_version(today=None, existing=None):
    day = (today or dt.datetime.now(dt.timezone.utc).date()).strftime("%Y%m%d")
    existing = set(tags() if existing is None else existing)
    version, n = f"V{day}", 1
    while version in existing:
        n += 1
        version = f"V{day}.{n}"
    return version


def area_of(path):
    for prefix, label in AREAS:
        if path == prefix or path.startswith(prefix + "/"):
            return label
    return "Other"


def summarise(name_status):
    """Fold `git diff --name-status` output into per-area (changed, added) counts
    plus the list of new solution files."""
    counts = {label: [0, 0] for _, label in AREAS}
    counts["Other"] = [0, 0]
    new_solutions = {"Python": [], "Java": []}
    for line in name_status.splitlines():
        parts = line.split("\t")
        if len(parts) < 2:
            continue
        status, path = parts[0], parts[-1]
        label = area_of(path)
        counts[label][0] += 1
        if status.startswith("A"):
            counts[label][1] += 1
            if path.startswith("leetcode_python/") and path.endswith(".py"):
                new_solutions["Python"].append(path)
            elif path.startswith("leetcode_java/src/main/java/LeetCodeJava/") and path.endswith(".java"):
                new_solutions["Java"].append(path)
    return counts, new_solutions


def practice_days(progress_diff):
    return sum(1 for line in progress_diff.splitlines()
               if line.startswith("+") and DATE_LINE.match(line[1:]))


def render(version, since, commits, counts, new_solutions, days, since_date=None):
    out = [f"## CS_basics {version}", ""]
    out.append(f"🌐 Site: {SITE}  ")
    if since:
        compare = f"https://github.com/{REPO}/compare/{since}...{version}"
        when = f" ({since_date})" if since_date else ""
        out.append(f"📦 **{commits}** commits since `{since}`{when} — [full diff]({compare})")
    else:
        out.append(f"📦 **{commits}** commits")
    out.append("")

    out += ["### What changed", "", "| Area | Files changed | New files |", "|---|---:|---:|"]
    rows = [(label, c, a) for label, (c, a) in counts.items() if c]
    if rows:
        out += [f"| {label} | {c} | {a} |" for label, c, a in rows]
    else:
        out.append("| — | 0 | 0 |")
    out.append("")

    out += ["### Practice", "", f"- **{days}** day(s) added to the practice log (`data/progress.txt`)"]
    for lang, files in new_solutions.items():
        if not files:
            continue
        out.append(f"- **{len(files)}** new {lang} solution file(s)")
        shown = files[:15]
        out += [f"  - [`{f.rsplit('/', 1)[-1]}`](https://github.com/{REPO}/blob/{version}/{f})" for f in shown]
        if len(files) > len(shown):
            out.append(f"  - … and {len(files) - len(shown)} more")
    out.append("")
    return "\n".join(out)


def notes(version, since=None, head="HEAD"):
    if since is None:
        previous = [t for t in tags() if t != version]
        since = previous[-1] if previous else None
    rng = f"{since}..{head}" if since else head
    commits = int(git("rev-list", "--count", rng).strip())
    name_status = git("-c", "diff.renameLimit=0", "diff", "--name-status", "-M", rng) if since else ""
    progress = git("diff", "-U0", rng, "--", "data/progress.txt") if since else ""
    since_date = git("log", "-1", "--format=%cs", since).strip() if since else None
    counts, new_solutions = summarise(name_status)
    return render(version, since, commits, counts, new_solutions, practice_days(progress), since_date)


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    sub = ap.add_subparsers(dest="cmd", required=True)
    sub.add_parser("version", help="print the next release tag")
    n = sub.add_parser("notes", help="print the release post as markdown")
    n.add_argument("version")
    n.add_argument("--since", help="previous tag (default: the latest V<date> tag)")
    n.add_argument("--head", default="HEAD")
    args = ap.parse_args(argv)
    if args.cmd == "version":
        print(next_version())
    else:
        print(notes(args.version, args.since, args.head))
    return 0


if __name__ == "__main__":
    sys.exit(main())
