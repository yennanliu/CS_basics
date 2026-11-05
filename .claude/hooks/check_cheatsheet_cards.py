#!/usr/bin/env python3
"""PostToolUse hook: run the cheatsheet card gate right after a cheatsheet edit.

CI (.github/workflows/cheatsheet-cards.yml) only catches a section that is not
card-shaped after it is pushed. This hook runs the same checker the moment an
Edit / Write / MultiEdit touches doc/cheatsheet/<sheet>.md, and hands any
problem in that file back to Claude (exit 2 + stderr), so the section is fixed
in the same turn it was written.

It reads the tool call as JSON on stdin and stays silent (exit 0) for every
other file, for 00_template.md, and when the checker cannot run (no git, no
origin/master): the CI job is still the gate, this is the early warning.
"""
import json
import os
import subprocess
import sys


def main():
    try:
        call = json.load(sys.stdin)
    except ValueError:
        return 0
    tool_input = call.get("tool_input") or {}
    tool_response = call.get("tool_response") or {}
    path = tool_input.get("file_path") or tool_response.get("filePath") or ""
    if not path.endswith(".md"):
        return 0

    # The repo the edited file lives in, so a worktree checks its own tree.
    directory = os.path.dirname(os.path.abspath(path))
    if not os.path.isdir(directory):
        return 0
    top = subprocess.run(["git", "-C", directory, "rev-parse", "--show-toplevel"],
                         capture_output=True, text=True)
    if top.returncode != 0:
        return 0
    root = top.stdout.strip()
    rel = os.path.relpath(os.path.realpath(path), os.path.realpath(root))
    parts = rel.split(os.sep)
    if parts[:2] != ["doc", "cheatsheet"] or len(parts) != 3 or parts[2] == "00_template.md":
        return 0

    checker = os.path.join(root, "script", "check_cheatsheet_cards.py")
    if not os.path.exists(checker):
        return 0
    run = subprocess.run([sys.executable, checker], cwd=root, capture_output=True, text=True)
    if run.returncode == 2:          # base ref missing: leave it to CI
        return 0
    mine = [line for line in run.stdout.splitlines() if line.startswith(rel + ":")]
    if not mine:
        return 0

    print(f"check_cheatsheet_cards.py: {len(mine)} problem(s) in {rel} — "
          f"fix the section, not the checker (shapes: doc/cheatsheet/00_template.md):",
          file=sys.stderr)
    for line in mine:
        print("  " + line, file=sys.stderr)
    return 2


if __name__ == "__main__":
    sys.exit(main())
