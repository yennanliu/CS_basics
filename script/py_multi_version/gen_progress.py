#!/usr/bin/env python3
"""Regenerate doc/branch-progress.md from real repo state. Single source of truth."""
import json, os, subprocess, sys, datetime
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import verify

SP = os.path.dirname(os.path.abspath(__file__))
state = json.load(open(os.path.join(SP, "state.json")))
BATCHES = os.path.join(SP, "batches")
REPO = os.path.dirname(os.path.dirname(SP))

def sh(cmd, cwd):
    return subprocess.run(cmd, shell=True, cwd=cwd, capture_output=True, text=True).stdout.strip()

def batch_files(n):
    p = os.path.join(BATCHES, f"batch_{n:03d}.txt")
    return [l.split("\t")[0].strip() for l in open(p) if l.strip()] if os.path.exists(p) else []

L = []
w = L.append
w("# Branch progress log")
w("")
w("Running log of the automated clean-up of `leetcode_python/`, one section per branch.")
w("Generated from real repo state by `script/py_multi_version/gen_progress.py` — do not hand-edit.")
w("")
w(f"_Snapshot: {datetime.datetime.now().strftime('%Y-%m-%d %H:%M')}_")
w("")
w("## Where this work came from")
w("")
w("Audit of `leetcode_python/`: **2,868 files**, 7,847 `# V<n>` markers, **6,967** of them")
w("holding a real implementation. Three tasks fell out of it:")
w("")
w("| # | Task | Size found by audit | Branch |")
w("|---|------|---------------------|--------|")
w("| 1 | Add missing `# time =` / `# space =` | 373 blocks in 142 files | not started |")
w("| 2 | Fix code format | 90 files with py2/tab defects; 366 duplicate blocks in 264 files; 375 files with no `V0`; ~9,300 whitespace fixes | not started |")
w("| 3 | Every LC needs >=3 solution versions | **2,176** files short (1,913 have 1, 258 have 2, 5 have none) | `feat/py-multi-version` |")
w("")
w("Task 2's dedup pass must run **after** task 3, or the two passes fight over the")
w("same version markers.")
w("")
w("A note on the count: a first pass put task 3 at 2,168 files. That counter treated a")
w("`pass`-only block as an implementation. `audit.py` does not, which is the stricter and")
w("correct reading, and it moves the number to 2,176. Re-run `audit.py --report` for the")
w("live figure — it always measures the tree rather than trusting this table.")
w("")

for br, info in state["branches"].items():
    wt = info["worktree"]; base = info["base"]
    # retarget the verifier at this branch's worktree
    verify.REPO = wt
    verify.WT = os.path.join(wt, "leetcode_python")
    verify.BASE = base
    verify._added = None
    check = verify.check
    w(f"## `{br}`")
    w("")
    w(f"**Task**: {info['task']}")
    w("")
    w(f"- Base: `{base}`  |  Worktree: `{wt}`")
    w("- Tooling: `script/py_multi_version/` (`SPEC.md` agent contract, `audit.py`, `verify.py`, `gen_progress.py`, `batches/`)")
    w(f"- HEAD: `{sh('git log -1 --format=%h\\ %s', wt)}`")
    w(f"- Commits ahead of `{base}`: {sh(f'git rev-list --count {base}..HEAD', wt) or 0}")
    w("")

    committed = set(sh(f"git diff --name-only {base}..HEAD -- leetcode_python", wt).split("\n")) - {""}
    rows = []; tot_ok = tot_files = 0
    for n in info["dispatched"]:
        files = batch_files(n)
        if not files: continue
        okc = failc = 0; probs_all = []
        for f in files:
            try:
                _, probs = check(f)
            except FileNotFoundError:
                probs = ["missing"]
            if probs:
                failc += 1
                if n in info.get("reported", []):
                    probs_all.append(f"`{f.split('/')[-1]}`: {probs[0]}")
            else: okc += 1
        ncomm = sum(1 for f in files if f"leetcode_python/{f}" in committed)
        reported = n in info.get("reported", [])
        if not reported:
            st = "in flight"
        elif ncomm == len(files):
            st = "committed"
        elif failc == 0:
            st = "verified"
        else:
            st = "needs rework"
        rows.append((n, len(files), okc, failc, ncomm, st, probs_all))
        tot_ok += okc; tot_files += len(files)

    w("### Batches dispatched")
    w("")
    w("| Batch | Files | Verified OK | Needs rework | Committed | Status |")
    w("|-------|-------|-------------|--------------|-----------|--------|")
    for r in rows:
        w(f"| {r[0]:03d} | {r[1]} | {r[2]} | {r[3]} | {r[4]} | {r[5]} |")
    w("")
    outstanding = [p for r in rows for p in r[6]]
    w(f"Batches still running are marked *in flight*; their file counts are partial.")
    w("")
    if outstanding:
        w("Outstanding problems on batches that have already reported:")
        w("")
        for p in outstanding: w(f"- {p}")
        w("")

    total = info["total_files"]
    w("The batch work-list was cut from the original 2,168-file audit, so it holds 345")
    w("batches. It gets recut from `audit.py` once the in-flight wave lands, which both")
    w("picks up the 8 files the stricter count added and drops the files already finished.")
    w("")
    w("### Overall")
    w("")
    w(f"- In scope: **{total}** files across **{info['total_batches']}** batches")
    w(f"- Dispatched: **{tot_files}**  |  Verified complete: **{tot_ok}** "
      f"({100.0*tot_ok/total:.1f}% of scope)  |  Remaining: **{total - tot_ok}**")
    w(f"- Files changed in commits so far: **{len(committed)}**")
    w(f"- Changed but uncommitted: **{sh('git status --porcelain -- leetcode_python | wc -l', wt).strip()}**")
    w("")
    w("### Definition of done")
    w("")
    w("Enforced mechanically by `script/py_multi_version/verify.py`; a file passes only if:")
    w("")
    w("- at least **3 blocks with real code** — a `pass`/bare-`return` placeholder block")
    w("  does not count toward the 3")
    w("- every real block carries both `# time =` and `# space =` in its header")
    w("- no two blocks are identical after comment/whitespace normalisation (this is what")
    w("  stops the pass from re-creating the 366 duplicates task 2 has to clean up)")
    w("- the file parses under `ast.parse`")
    w("- **added** lines carry no tabs, no trailing whitespace, no py2 constructs, and the")
    w("  file ends in exactly one newline (pre-existing debt is deliberately out of scope")
    w("  here — it belongs to task 2)")
    w("")
    w("Each new block also gets the house 4-line header, and every solution is run against")
    w("the problem's docstring examples before it counts. Existing blocks are left")
    w("byte-for-byte unchanged — the pass is purely additive.")
    w("")

open(os.path.join(REPO, "doc", "branch-progress.md"), "w").write("\n".join(L) + "\n")
print("wrote doc/branch-progress.md")
