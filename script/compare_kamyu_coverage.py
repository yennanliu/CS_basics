#!/usr/bin/env python3
"""Compare kamyu104/LeetCode-Solutions problem lists against CS_basics coverage.

Emits one checkpoint markdown file per source list (batch), so partial
progress survives an interrupted run.
"""
import json
import os
import re
import sys

KAMYU = sys.argv[1]
OURS = sys.argv[2]
OUT = sys.argv[3]

BATCHES = [
    ("0001-1000", "0001-1000.md"),
    ("1001-2000", "1001-2000.md"),
    ("2001-3000", "2001-3000.md"),
    ("3001-latest", "README.md"),
]

ROW = re.compile(
    r"^\s*(\d{3,4})\s*\|\s*\[([^\]]+)\]\(https://leetcode\.com/problems/([a-z0-9\-]+)/?\)(.*)$"
)
DIFF = re.compile(r"\b(Easy|Medium|Hard)\b")


def parse_batch(path):
    """number -> {title, slug, difficulty}; first occurrence wins (rows repeat per tag)."""
    out = {}
    with open(path, encoding="utf-8") as f:
        for line in f:
            m = ROW.match(line)
            if not m:
                continue
            num = int(m.group(1))
            if num in out:
                continue
            d = DIFF.search(m.group(4))
            out[num] = {
                "num": num,
                "title": m.group(2).replace("🔒", "").strip(),
                "slug": m.group(3),
                "difficulty": d.group(1) if d else "?",
                "premium": "🔒" in line,
            }
    return out


# ---------- our coverage ----------

CAMEL = re.compile(r"(?<=[a-z0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")


def camel_to_slug(name):
    return CAMEL.sub("-", name).lower()


def normalize(s):
    """Collapse to a comparable key: alphanumerics only."""
    return re.sub(r"[^a-z0-9]", "", s.lower())


def collect_ours(root):
    slugs = set()
    numbers = set()
    exts = {".py", ".java", ".sql", ".scala", ".js", ".cpp", ".c", ".go", ".ts"}
    skip_dirs = {".git", "__pycache__", "node_modules", "target", ".claude", "site"}
    num_pat = re.compile(r"\bLC[ _#\-]*(\d{1,4})\b")
    url_pat = re.compile(r"leetcode\.com/problems/([a-z0-9\-]+)")

    for dirpath, dirnames, filenames in os.walk(root):
        dirnames[:] = [d for d in dirnames if d not in skip_dirs]
        for fn in filenames:
            stem, ext = os.path.splitext(fn)
            if ext not in exts:
                continue
            # filename -> slug key
            if re.search(r"[A-Z]", stem) and "-" not in stem:
                slugs.add(normalize(camel_to_slug(stem)))
            slugs.add(normalize(stem))
            # content -> LC numbers + problem URLs
            fp = os.path.join(dirpath, fn)
            try:
                with open(fp, encoding="utf-8", errors="ignore") as f:
                    text = f.read()
            except OSError:
                continue
            for m in num_pat.finditer(text):
                numbers.add(int(m.group(1)))
            for m in url_pat.finditer(text):
                slugs.add(normalize(m.group(1)))
    return slugs, numbers


def main():
    os.makedirs(OUT, exist_ok=True)
    print("scanning our repo ...", flush=True)
    our_slugs, our_numbers = collect_ours(OURS)
    print(f"  our slugs: {len(our_slugs)}  our LC numbers: {len(our_numbers)}", flush=True)

    summary = []
    for label, fname in BATCHES:
        path = os.path.join(KAMYU, fname)
        probs = parse_batch(path)
        missing, have = [], []
        for num in sorted(probs):
            p = probs[num]
            key = normalize(p["slug"])
            covered = key in our_slugs or num in our_numbers
            (have if covered else missing).append(p)

        # checkpoint: markdown + json per batch
        md = [f"# Missing from CS_basics — kamyu104 list `{label}`", ""]
        md.append(f"- Problems in list: **{len(probs)}**")
        md.append(f"- Already in our project: **{len(have)}**")
        md.append(f"- **To add: {len(missing)}**")
        md.append("")
        md.append("| # | Title | Difficulty | Slug |")
        md.append("|---|-------|------------|------|")
        for p in missing:
            lock = " 🔒" if p["premium"] else ""
            md.append(
                f"| {p['num']} | [{p['title']}](https://leetcode.com/problems/{p['slug']}/){lock} "
                f"| {p['difficulty']} | `{p['slug']}` |"
            )
        with open(os.path.join(OUT, f"missing_{label}.md"), "w", encoding="utf-8") as f:
            f.write("\n".join(md) + "\n")
        with open(os.path.join(OUT, f"missing_{label}.json"), "w", encoding="utf-8") as f:
            json.dump(missing, f, indent=1)

        summary.append((label, len(probs), len(have), len(missing)))
        print(f"[checkpoint] {label}: total={len(probs)} have={len(have)} missing={len(missing)}",
              flush=True)

    with open(os.path.join(OUT, "_summary.json"), "w", encoding="utf-8") as f:
        json.dump(summary, f, indent=1)


if __name__ == "__main__":
    main()
