#!/usr/bin/env python3
"""
Audit LC Examples sections in all cheatsheets for format compliance.

Target format (from monotonic_stack.md):
  ### 2-1) Problem Name (LC N) — Short Description
  > Core idea one-liner.

  ```java
  // LC N - Problem Name
  // IDEA: ...
  // time = O(N), space = O(N)
  <java code>
  ```

Usage:
  python3 script/check_lc_format.py [--verbose]
"""

import re
import sys
import os
from pathlib import Path

CHEATSHEET_DIR = Path(__file__).parent.parent / "doc" / "cheatsheet"

GOOD_HEADER = re.compile(r'^### \d+-\d+\) .+ \(LC \d+\) — .+')
# Template/comparison entries don't need an LC number
TEMPLATE_KEYWORDS = {"template", "comparison", " vs ", "custom", "theoretical"}
SECTION_HEADER = re.compile(r'^### (.+)')


def parse_lc_examples(content: str):
    """Extract entries from ## LC Examples section."""
    lc_start = content.find("## LC Examples")
    if lc_start == -1:
        return []

    section = content[lc_start:]
    lines = section.split("\n")

    entries = []
    current_entry = None
    in_code = False
    code_lang = None

    first_line = True
    for line in lines:
        # Stop at next ## section (but not the opening ## LC Examples itself)
        if not first_line and line.startswith("## "):
            break
        first_line = False

        # Track code fences
        if line.startswith("```"):
            if not in_code:
                in_code = True
                lang = line[3:].strip()
                code_lang = lang if lang else "unknown"
                if current_entry is not None:
                    current_entry["code_langs"].append(code_lang)
                    current_entry["code_blocks"].append([])
            else:
                in_code = False
                code_lang = None
            continue

        if in_code:
            if current_entry is not None and current_entry["code_blocks"]:
                current_entry["code_blocks"][-1].append(line)
            continue

        # Check for ### header
        m = SECTION_HEADER.match(line)
        if m:
            if current_entry is not None:
                entries.append(current_entry)
            current_entry = {
                "header": line,
                "blockquote": None,
                "code_langs": [],
                "code_blocks": [],
            }
            continue

        # Blockquote line (> ...)
        if line.startswith("> ") and current_entry is not None and current_entry["blockquote"] is None:
            current_entry["blockquote"] = line[2:].strip()

    if current_entry is not None:
        entries.append(current_entry)

    return entries


def classify_entry(entry):
    """Return list of issues with an entry."""
    issues = []
    header = entry["header"]

    # 1. Check header format (allow template/comparison entries without LC number)
    header_lower = header.lower()
    if any(kw in header_lower for kw in TEMPLATE_KEYWORDS):
        return issues  # template/comparison entries are fine without LC number
    if not GOOD_HEADER.match(header):
        has_lc = bool(re.search(r'\(LC \d+\)', header))
        has_num = bool(re.match(r'### \d+-\d+\)', header))
        has_desc = bool(re.search(r' — .+', header))

        if not has_num:
            issues.append("bad-numbering")
        if not has_lc:
            issues.append("missing-lc-number")
        if not has_desc:
            issues.append("missing-description")

    # 2. Check blockquote
    if entry["blockquote"] is None:
        issues.append("missing-blockquote")

    # 3. Check code
    langs = entry["code_langs"]
    if not langs:
        issues.append("no-code")
    elif "java" not in langs:
        issues.append("python-only")
    # python alongside java is acceptable

    return issues


def extract_idea_from_java(code_lines):
    """Try to extract IDEA from java code comments."""
    for line in code_lines:
        m = re.match(r'\s*// IDEA:\s*(.+)', line)
        if m:
            return m.group(1).strip()
    return None


def main():
    verbose = "--verbose" in sys.argv or "-v" in sys.argv

    files = sorted(CHEATSHEET_DIR.glob("*.md"))

    results = {}
    for fpath in files:
        if fpath.name == "00_template.md":
            continue
        content = fpath.read_text()
        if "## LC Examples" not in content:
            continue

        entries = parse_lc_examples(content)
        file_issues = []
        for e in entries:
            issues = classify_entry(e)
            if issues:
                file_issues.append({"header": e["header"], "issues": issues})

        if file_issues:
            results[fpath.name] = file_issues

    # Summary
    print("=" * 60)
    print("LC Examples Format Audit")
    print("=" * 60)

    python_only_files = []
    fixable_files = []

    for fname, issues_list in sorted(results.items()):
        all_issues = [i for entry in issues_list for i in entry["issues"]]
        has_python_only = "python-only" in all_issues or "no-code" in all_issues

        if has_python_only:
            python_only_files.append(fname)
        else:
            fixable_files.append(fname)

    print(f"\n[NEEDS JAVA SOLUTIONS] ({len(python_only_files)} files)")
    print("These files have python-only examples and need java solutions written:")
    for f in python_only_files:
        n = len(results[f])
        print(f"  {f}: {n} entries need work")

    print(f"\n[FIXABLE BY SCRIPT] ({len(fixable_files)} files)")
    print("These files only need header/blockquote formatting fixes:")
    for fname in fixable_files:
        print(f"\n  {fname}:")
        for entry in results[fname]:
            print(f"    {entry['header'][:70]}")
            print(f"      issues: {', '.join(entry['issues'])}")

    print(f"\n[SUMMARY]")
    total_bad = sum(len(v) for v in results.values())
    print(f"  Total files with issues: {len(results)}")
    print(f"  Total entries with issues: {total_bad}")
    print(f"  Fixable by script: {sum(len(results[f]) for f in fixable_files)} entries in {len(fixable_files)} files")
    print(f"  Needs java solutions: {sum(len(results[f]) for f in python_only_files)} entries in {len(python_only_files)} files")

    if verbose:
        print("\n[DETAIL — ALL ISSUES]")
        for fname, issues_list in sorted(results.items()):
            print(f"\n  {fname}")
            for entry in issues_list:
                print(f"    {entry['header'][:70]}")
                print(f"      {entry['issues']}")


if __name__ == "__main__":
    main()
