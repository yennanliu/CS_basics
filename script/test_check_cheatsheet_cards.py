#!/usr/bin/env python3
"""Tests for check_cheatsheet_cards.py.

A checker that quietly stops matching passes every section in the repo, so each
rule is asserted against a bad section as well as a good one. The good sections
are the two sample cards in doc/cheatsheet/00_template.md itself: if the template
and the checker disagree, this fails, and one of them has to move.

    python3 script/test_check_cheatsheet_cards.py
"""
import os
import re
import sys
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import check_cheatsheet_cards as c  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def template_samples():
    """The ````markdown sample blocks in 00_template.md, in order."""
    with open(os.path.join(ROOT, "doc/cheatsheet/00_template.md"), encoding="utf-8") as f:
        text = f.read()
    return re.findall(r"^````markdown\n(.*?)^````\s*$", text, re.M | re.S)


def run(text, path="doc/cheatsheet/demo.md", changed=None, new_sheet=False):
    lines = text.split("\n")
    whole = changed is None
    return c.check_file(path, lines, changed or set(), new_sheet, whole=whole)


PATTERN = """### Template 1: Complement Lookup — LC 1

**Recognise** — "two numbers that add up to target", unsorted.

**Key point** — `seen` holds every number before index i.

```python
# python
# IDEA: look up target - x before storing x
# time = O(n), space = O(n)
def two_sum(nums, target):
    seen = {}
    for i, x in enumerate(nums):
        if target - x in seen:
            return [seen[target - x], i]
        seen[x] = i
```

**Complexity**
- **Time O(n)**: one pass; each lookup is O(1).

**Traps**
- `[3, 3]`, target 6: storing before looking pairs 3 with itself.

**Not this if** — sorted input: two pointers, O(1) space (LC 167).

**Drill** — LC 1 ⭐ · LC 167 · LC 15
"""

PROBLEM = """### Two Sum II — LC 167

> **Key point** — sorted, so move the pointer on the side that is too big or too small.

**Reduces to**: [Template 1](#template-1-complement-lookup--lc-1), with two pointers instead of a map.

```python
# python
# IDEA: shrink from whichever end overshoots
# time = O(n), space = O(1)
def two_sum_sorted(nums, target):
    l, r = 0, len(nums) - 1
    while nums[l] + nums[r] != target:
        if nums[l] + nums[r] < target:
            l += 1
        else:
            r -= 1
    return [l + 1, r + 1]
```
"""


class TemplateSamples(unittest.TestCase):
    def test_template_has_both_samples(self):
        self.assertGreaterEqual(len(template_samples()), 2)

    def test_template_pattern_card_passes(self):
        self.assertEqual(run(template_samples()[0]), [])

    def test_template_problem_card_passes(self):
        self.assertEqual(run(template_samples()[1]), [])


class PatternCard(unittest.TestCase):
    def test_good(self):
        self.assertEqual(run(PATTERN), [])

    def test_heading_needs_name_and_lc(self):
        out = run(PATTERN.replace("Template 1: Complement Lookup — LC 1", "Template 1: Complement Lookup"))
        self.assertTrue(any("pattern card heading" in p for p in out), out)

    def test_missing_key_point(self):
        out = run(PATTERN.replace("**Key point**", "**Idea**"))
        self.assertTrue(any("no **Key point**" in p for p in out), out)

    def test_missing_complexity(self):
        out = run(PATTERN.replace("**Complexity**", "Complexity"))
        self.assertTrue(any("no **Complexity**" in p for p in out), out)

    def test_not_this_if_before_key_point_is_out_of_order(self):
        moved = PATTERN.replace("**Not this if** — sorted input: two pointers, O(1) space (LC 167).\n", "")
        moved = moved.replace("**Key point**", "**Not this if** — sorted.\n\n**Key point**")
        out = run(moved)
        self.assertTrue(any("out of order" in p for p in out), out)

    def test_java_before_python(self):
        java = "```java\n// java\n// IDEA: x\n// time = O(n), space = O(n)\nint f() { return 0; }\n```\n\n"
        out = run(PATTERN.replace("```python", java + "```python", 1))
        self.assertTrue(any("Python first" in p for p in out), out)

    def test_block_without_time(self):
        out = run(PATTERN.replace("# time = O(n), space = O(n)\n", ""))
        self.assertTrue(any("time = O" in p for p in out), out)

    def test_no_template_in_examples_sheet(self):
        out = run(PATTERN, path="doc/cheatsheet/hash_map_examples.md")
        self.assertTrue(any("holds no templates" in p for p in out), out)


class ProblemCard(unittest.TestCase):
    def test_good(self):
        self.assertEqual(run(PROBLEM), [])

    def test_code_first_fails(self):
        bad = "### Two Sum II — LC 167\n\n```python\n# IDEA: x\n# time = O(n)\na = 1\nb = 2\n```\n"
        out = run(bad)
        self.assertTrue(any("must open with" in p for p in out), out)

    def test_missing_reduces_to(self):
        out = run(PROBLEM.replace("**Reduces to**", "Reduces to"))
        self.assertTrue(any("Reduces to" in p for p in out), out)

    def test_other_examples_section(self):
        out = run("## Other Examples\n\ntext\n", path="doc/cheatsheet/hash_map_examples.md")
        self.assertTrue(any("Other Examples" in p for p in out), out)


class LineAndHeadingRules(unittest.TestCase):
    def test_note_wall_on_added_line(self):
        text = "## Overview\n\n# NOTE !!! careful\n"
        self.assertTrue(run(text, changed={2}))
        self.assertEqual(run(text, changed=set()), [])   # untouched line is not judged

    def test_emoji_heading(self):
        out = run("## 🔥 Overview\n\ntext\n")
        self.assertTrue(any("emoji" in p for p in out), out)

    def test_star_run_is_allowed(self):
        self.assertEqual(run("## Overview ⭐⭐⭐\n\ntext\n"), [])

    def test_numbering_only_in_new_sheet(self):
        text = "## 1-1) Basics\n\ntext\n"
        self.assertTrue(run(text, new_sheet=True))
        self.assertEqual(run(text, new_sheet=False), [])

    def test_rename_is_read_from_name_status(self):
        out = ("R098\tdoc/cheatsheet/old_name.md\tdoc/cheatsheet/new_name.md\n"
               "M\tdoc/cheatsheet/other.md\n"
               "C100\tdoc/cheatsheet/stub.md\tdoc/cheatsheet/moved.md\n")
        self.assertEqual(c.parse_renames(out),
                         {"doc/cheatsheet/new_name.md": "doc/cheatsheet/old_name.md",
                          "doc/cheatsheet/moved.md": "doc/cheatsheet/stub.md"})

    def _repo(self, stub):
        """A throwaway repo: a numbered sheet, then renamed (with or without a stub
        left at the old name) and edited in one heading."""
        import subprocess, tempfile
        d = tempfile.mkdtemp()
        g = lambda *a: subprocess.run(["git", *a], cwd=d, check=True, capture_output=True, text=True)
        g("init", "-q"); g("config", "user.email", "t@t"); g("config", "user.name", "t")
        os.makedirs(os.path.join(d, "doc/cheatsheet"))
        old = "# Old\n\n## 0-1) Concept\n\nprose\n\n## 0-2) More\n\nprose\n"
        with open(os.path.join(d, "doc/cheatsheet/old_name.md"), "w") as f:
            f.write(old)
        g("add", "-A"); g("commit", "-qm", "base")
        base = g("rev-parse", "HEAD").stdout.strip()
        g("mv", "doc/cheatsheet/old_name.md", "doc/cheatsheet/new_name.md")
        with open(os.path.join(d, "doc/cheatsheet/new_name.md"), "a") as f:
            f.write("\n### Template 1: Bad\n\ncode first\n")       # the one new section
        if stub:
            with open(os.path.join(d, "doc/cheatsheet/old_name.md"), "w") as f:
                f.write("# Old\n\nMoved to [new_name.md](./new_name.md).\n")
        g("add", "-A"); g("commit", "-qm", "rename")
        return d, base

    def _judge(self, d, base):
        import io, contextlib
        root = c.ROOT
        c.ROOT = d
        try:
            out = io.StringIO()
            with contextlib.redirect_stdout(out), contextlib.redirect_stderr(io.StringIO()):
                c.main(["--base", base])
            return out.getvalue()
        finally:
            c.ROOT = root

    def test_rename_judges_only_what_changed(self):
        for stub in (False, True):                     # a plain rename, and a copy + stub
            with self.subTest(stub=stub):
                d, base = self._repo(stub)
                root = c.ROOT
                c.ROOT = d
                try:
                    changed = c.changed_lines(base, "doc/cheatsheet/new_name.md", "doc/cheatsheet/old_name.md")
                finally:
                    c.ROOT = root
                lines = open(os.path.join(d, "doc/cheatsheet/new_name.md")).read().split("\n")
                self.assertTrue(changed)
                self.assertFalse(any("0-1)" in lines[i] or "0-2)" in lines[i] for i in changed))
                out = self._judge(d, base)
                self.assertIn("Template 1: Bad", out)         # the new section is judged
                self.assertNotIn("numbering", out)             # the old 0-1) headings are not

    def test_unchanged_old_section_is_not_judged(self):
        old = "### Template 1: Old\n\ncode first\n"
        self.assertEqual(run(old, changed={2}), [])      # only its body changed
        self.assertTrue(run(old, changed={0}))           # its heading changed: rewritten


class BaseRef(unittest.TestCase):
    def test_no_default_base_is_not_an_error(self):
        self.assertEqual(c.resolve_base(None, candidates=("no/such-ref-1", "no-such-ref-2")), (None, None))

    def test_explicit_base_that_does_not_resolve_is_reported(self):
        self.assertEqual(c.resolve_base("no/such-ref"), (None, "no/such-ref"))

    def test_explicit_head_resolves(self):
        sha, ref = c.resolve_base("HEAD")
        self.assertEqual(ref, "HEAD")
        self.assertRegex(sha, r"^[0-9a-f]{40}$")


class Fences(unittest.TestCase):
    def test_heading_inside_a_long_fence_is_ignored(self):
        text = "````markdown\n```python\nx\n```\n### Template 9: Bad\n````\n"
        self.assertEqual(c.scan(text.split("\n")), [])


class RealTree(unittest.TestCase):
    def test_finds_cards_in_the_repo(self):
        """Guard against a heading regex that silently matches nothing."""
        def kinds(name):
            with open(os.path.join(ROOT, "doc/cheatsheet", name), encoding="utf-8") as f:
                return [c.card_kind(t) for _, _, t in c.scan(f.read().split("\n"))]
        self.assertGreater(kinds("sliding_window.md").count("pattern"), 0)
        self.assertGreater(kinds("tree_examples.md").count("problem"), 0)


if __name__ == "__main__":
    unittest.main(verbosity=1)
