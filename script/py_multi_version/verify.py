#!/usr/bin/env python3
"""Verify worktree LC python files for the '>=3 solution versions' task.

Scope rules that matter:
 - a block whose body is only a docstring / `pass` / `return` placeholder is NOT an
   implementation, so it neither counts toward the 3 nor needs complexity comments
 - whitespace / py2 / newline checks apply ONLY to lines this branch ADDED, because
   pre-existing formatting debt is task 2's job, not task 3's
"""
import os, re, sys, ast, hashlib, subprocess
from collections import Counter, defaultdict

import os
# Resolve everything from this file's location so the tooling works from any checkout.
HERE = os.path.dirname(os.path.abspath(__file__))
REPO = os.path.dirname(os.path.dirname(HERE))
# PMV_WORKTREE lets this run from ANY checkout while reporting on the work branch's
# worktree; without it we audit our own tree.
REPO = os.environ.get("PMV_WORKTREE", REPO)
WT = os.path.join(REPO, "leetcode_python")
BASE = os.environ.get("PMV_BASE", "master")

MARK = re.compile(r"^#\s*V\d")
TIME = re.compile(r"#.*\btime\b\s*[:=]", re.I)
SPACE = re.compile(r"#.*\bspace\b\s*[:=]", re.I)
CODEISH = re.compile(r"^\s*(class\s+\w|def\s+\w|from\s+\w|import\s+\w)")
PY2 = re.compile(r"\bxrange\b|\.iteritems\(|^\s*print\s+[^(=]")

_added = None
def added_lines():
    """map repo-relative path -> list of added line texts, vs BASE incl. working tree"""
    global _added
    if _added is not None: return _added
    _added = defaultdict(list)
    raw = subprocess.run(f"git diff -U0 {BASE} -- leetcode_python", shell=True, cwd=REPO,
                         capture_output=True, text=True).stdout
    cur = None
    for line in raw.split("\n"):
        if line.startswith("+++ b/"): cur = line[6:]
        elif line.startswith("+") and not line.startswith("+++") and cur:
            _added[cur].append(line[1:])
    return _added

def strip_str(t):
    t = re.sub(r'"""[\s\S]*?"""', "", t)
    t = re.sub(r"'''[\s\S]*?'''", "", t)
    return t

PLACEHOLDER = {"pass", "return", "return None", "return 0", "return []", "return ''"}

def check(rel):
    p = os.path.join(WT, rel)
    raw = open(p, encoding="utf-8", errors="replace").read()
    lines = raw.split("\n")
    idx = [i for i, l in enumerate(lines) if MARK.match(l)]
    impl = 0; stubs = 0; miss = []; sigs = []
    for n, i in enumerate(idx):
        end = idx[n+1] if n+1 < len(idx) else len(lines)
        seg = lines[i+1:end]
        stripped = strip_str("\n".join(seg)).split("\n")
        if not any(CODEISH.match(l) for l in stripped):
            continue
        body = [re.sub(r"\s+", " ", l.strip()) for l in stripped
                if l.strip() and not l.strip().startswith("#")]
        # placeholder-only block: signature lines plus a bare pass/return
        meat = [b for b in body if not re.match(r"^(class|def)\b", b)]
        if not meat or all(m in PLACEHOLDER for m in meat):
            stubs += 1
            continue
        impl += 1
        # Scan everything before the first real code line, plus a few lines after it.
        # Headers here run 30+ lines and often embed a """note""" block *between*
        # comment lines, so stopping at the first non-comment line would miss the
        # complexity comment that follows the note.
        first_code = next((k for k, l in enumerate(seg) if CODEISH.match(l)), len(seg))
        near = "\n".join(seg[:first_code + 5])
        if not (TIME.search(near) and SPACE.search(near)):
            miss.append(lines[i].strip())
        sigs.append(hashlib.md5("\n".join(body).encode()).hexdigest())

    probs = []
    if impl < 3: probs.append(f"only {impl} real impl (plus {stubs} placeholder)")
    if miss: probs.append(f"missing time/space: {', '.join(miss)}")
    dup = sum(v-1 for v in Counter(sigs).values() if v > 1)
    if dup: probs.append(f"{dup} duplicate block(s)")
    try:
        ast.parse(raw)
    except SyntaxError as e:
        probs.append(f"SYNTAX ERROR line {e.lineno}: {e.msg}")
    if raw and not raw.endswith("\n"): probs.append("no trailing newline")

    # formatting checks: added lines only
    add = added_lines().get(f"leetcode_python/{rel}", [])
    if not add: probs.append("NO CHANGES vs base")
    bad_ws = [l for l in add if re.search(r"[ \t]+$", l)]
    if bad_ws: probs.append(f"{len(bad_ws)} added line(s) with trailing whitespace")
    if any("\t" in l for l in add): probs.append("added line contains tab")
    py2 = [l for l in add if PY2.search(l)]
    if py2: probs.append(f"{len(py2)} added line(s) with py2 construct")
    return impl, probs

if __name__ == "__main__":
    targets = []
    for a in sys.argv[1:]:
        if a.endswith(".txt"):
            for line in open(a):
                line = line.split("\t")[0].strip()
                if line: targets.append(line)
        else:
            targets.append(a)
    ok = 0; bad = []
    for t in targets:
        try:
            impl, probs = check(t)
        except FileNotFoundError:
            bad.append((t, ["FILE NOT FOUND"])); continue
        if probs: bad.append((t, probs))
        else: ok += 1
    print(f"PASS {ok}/{len(targets)}")
    for t, probs in bad:
        print(f"  FAIL {t}")
        for pr in probs: print(f"        - {pr}")
    sys.exit(1 if bad else 0)
