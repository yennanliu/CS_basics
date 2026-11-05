#!/usr/bin/env python3
"""Tests for check_cheatsheets.py.

A ratchet whose rule quietly stops matching reports "no rule got worse" forever,
so every rule is asserted against a bad fixture and a good one, and the real tree
is asserted to still produce findings for the rules the baseline says are live.

    python3 script/test_check_cheatsheets.py
"""
import json
import os
import sys
import tempfile
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import check_cheatsheets as c  # noqa: E402

F = "```"


def counts(name, text, tier=5):
    return c.count_file(name, text, tier)


class LineRules(unittest.TestCase):
    def test_note_wall(self):
        self.assertEqual(counts("x.md", "a\n# NOTE !!! here\n").get("note-wall"), 1)
        self.assertNotIn("note-wall", counts("x.md", "# NOTE: fine\n"))

    def test_ipython(self):
        self.assertEqual(counts("x.md", F + "python\nIn [3]: x = 1\nOut[3]: 1\n" + F + "\n").get("ipython"), 2)
        self.assertNotIn("ipython", counts("x.md", "Inside [3] the list\n"))

    def test_gpt_and_blog(self):
        got = counts("x.md", "fixed by GPT\nsee https://blog.csdn.net/x\n")
        self.assertEqual((got.get("gpt"), got.get("blog-link")), (1, 1))
        self.assertNotIn("gpt", counts("x.md", "chatgptx is one word\n"))


class Headings(unittest.TestCase):
    def test_emoji_but_not_the_star_run(self):
        self.assertEqual(counts("x.md", "## 🔥 Hot\n").get("heading-emoji"), 1)
        self.assertNotIn("heading-emoji", counts("x.md", "## Template 1: Window — LC 3 ⭐⭐⭐\n"))

    def test_code_first_example(self):
        bad = "### Two Sum — LC 1\n\n" + F + "python\nx = 1\n" + F + "\n"
        good = "### Two Sum — LC 1\n> **Key point** — complement.\n\n" + F + "python\nx = 1\n" + F + "\n"
        self.assertEqual(counts("x_examples.md", bad).get("code-first-example"), 1)
        self.assertNotIn("code-first-example", counts("x_examples.md", good))
        # an h2 is a section, not a worked example
        self.assertNotIn("code-first-example", counts("x.md", "## LC 1\n" + F + "python\nx\n" + F + "\n"))

    def test_heading_inside_code_is_not_a_heading(self):
        text = F + "python\n# LC 1 two sum\n## 🔥 not a heading\n" + F + "\n"
        self.assertEqual(counts("x.md", text), {"no-recognition": 1, "no-drill-path": 1})


class CodeRules(unittest.TestCase):
    def test_java_sub_comparator(self):
        bad = F + "java\nArrays.sort(a, (x, y) -> x[0] != y[0] ? Integer.compare(x[0], y[0]) : y[1] - x[1]);\n" + F
        good = F + "java\nArrays.sort(a, (x, y) -> Integer.compare(x[0], y[0]));\nmap.merge(k, 1, (o, n) -> o + n);\n" + F
        self.assertEqual(counts("x.md", bad).get("java-sub-comparator"), 1)
        self.assertNotIn("java-sub-comparator", counts("x.md", good))
        # only inside java fences
        self.assertNotIn("java-sub-comparator", counts("x.md", "(a, b) -> a - b\n"))

    def test_java_sub_comparator_next_to_integer_compare(self):
        code = "Arrays.sort(x, (a, b) -> { return a[0] - b[0] + Integer.compare(a[1], b[1]); });"
        self.assertEqual(counts("x.md", F + "java\n" + code + "\n" + F + "\n").get("java-sub-comparator"), 1)

    def test_java_sub_comparator_every_shape(self):
        bad = {
            "no spaces": "Arrays.sort(x, (a, b) -> a[0]-b[0]);",
            "typed params": "Arrays.sort(x, (int[] p, int[] q) -> p[1]-q[1]);",
            "block lambda, one line": "list.sort((a, b) -> { return b - a; });",
            "block lambda, several lines": "pq = new PriorityQueue<>((a, b) -> {\n    if (a[0] != b[0]) return a[0] - b[0];\n    return 0;\n});",
            "expression on the next line": "pq = new PriorityQueue<>((a, b) ->\n    b[1] - a[1]);",
            "anonymous Comparator": "new Comparator<Integer>() {\n  public int compare(Integer o1, Integer o2) {\n    return o1 - o2;\n  }\n}",
        }
        for name, code in bad.items():
            with self.subTest(name):
                self.assertEqual(counts("x.md", F + "java\n" + code + "\n" + F + "\n").get("java-sub-comparator"), 1)
        good = [
            "list.sort((a, b) -> {\n  int d = a - 1;\n  return Integer.compare(a, b);\n});",
            "IntStream.range(0, n).map(i -> n - i);",
            "map.merge(k, 1, (o, n) -> o + n);",
        ]
        for code in good:
            with self.subTest(code):
                self.assertNotIn("java-sub-comparator", counts("x.md", F + "java\n" + code + "\n" + F + "\n"))

    def test_py_float_mid(self):
        bad = F + "python\nmid = (left + right) / 2\n" + F
        good = F + "python\nmid = (left + right) // 2\n# mid = (l + r) / 2 is wrong\n" + F
        self.assertEqual(counts("x.md", bad).get("py-float-mid"), 1)
        self.assertNotIn("py-float-mid", counts("x.md", good))
        self.assertNotIn("py-float-mid", counts("x.md", F + "java\nint mid = (l + r) / 2;\n" + F))


class SheetRules(unittest.TestCase):
    def test_examples_after_summary(self):
        self.assertEqual(counts("x.md", "## Summary\n## LC Examples\n").get("examples-after-summary"), 1)
        self.assertNotIn("examples-after-summary", counts("x.md", "## LC Examples\n## Summary\n"))

    def test_numbered_summary_and_drill_path_level(self):
        self.assertEqual(counts("x.md", "## 7) Summary\n## 8) LC Examples\n", tier=3).get("examples-after-summary"), 1)
        # a Drill Path must be its own level-two section
        self.assertEqual(counts("x.md", "## Recognition\n### Drill Path\n").get("no-drill-path"), 1)
        self.assertNotIn("no-drill-path", counts("x.md", "## Recognition\n## 3) Drill Path\n"))

    def test_core_sheet_needs_recognition_and_drill_path(self):
        self.assertEqual(counts("x.md", "## Overview\n"), {"no-recognition": 1, "no-drill-path": 1})
        self.assertEqual(counts("x.md", "## Recognition\n## Drill Path\n"), {})
        self.assertEqual(counts("x.md", "## 0) Concept\n### When to use\n## 3) Drill Path\n"), {})
        # below tier 4, and satellites, are not held to it
        self.assertEqual(counts("x.md", "## Overview\n", tier=3), {})
        self.assertEqual(counts("x_advanced.md", "## When Not to Use\n"), {})

    def test_examples_sheet(self):
        self.assertEqual(counts("x_examples.md", "## Overview\n### Template 1: Window — LC 3\n"),
                         {"examples-no-index": 1, "examples-template": 1})
        self.assertEqual(counts("x_examples.md", "## Index\n## Window Examples\n"), {})

    def test_advanced_sheet(self):
        self.assertEqual(counts("x_advanced.md", "## Overview\n"), {"advanced-no-when-not": 1})


class Ratchet(unittest.TestCase):
    def test_compare(self):
        base = {r: {} for r in c.RULES}
        base["note-wall"] = {"a.md": 3, "b.md": 1}
        cur = {r: {} for r in c.RULES}
        cur["note-wall"] = {"a.md": 2, "c.md": 1}
        worse, better = c.compare(cur, base)
        self.assertEqual(worse, [("note-wall", "c.md", 0, 1)])          # a new file is held to 0
        self.assertEqual(sorted(better), [("note-wall", "a.md", 3, 2), ("note-wall", "b.md", 1, 0)])

    def test_main_fails_on_a_regression_and_passes_after_update(self):
        with tempfile.TemporaryDirectory() as d:
            sheets = os.path.join(d, "s")
            os.mkdir(sheets)
            meta = os.path.join(d, "meta.json")
            base = os.path.join(d, "base.json")
            with open(meta, "w") as fh:
                json.dump({"sheets": {"x": {"tier": 2}}}, fh)
            with open(os.path.join(sheets, "x.md"), "w") as fh:
                fh.write("# NOTE !!! one\n")
            argv = ["--sheets", sheets, "--meta", meta, "--baseline", base]
            self.assertEqual(c.main(argv), 1)                      # no baseline: held to 0
            self.assertEqual(c.main(argv + ["--update-baseline"]), 0)
            self.assertEqual(c.main(argv), 0)
            with open(os.path.join(sheets, "x.md"), "a") as fh:
                fh.write("# NOTE !!! two\n")
            self.assertEqual(c.main(argv), 1)


class UpdateBaseline(unittest.TestCase):
    def test_update_refuses_a_count_that_rose(self):
        with tempfile.TemporaryDirectory() as d:
            sheets = os.path.join(d, "s"); os.mkdir(sheets)
            meta = os.path.join(d, "meta.json"); base = os.path.join(d, "base.json")
            with open(meta, "w") as fh:
                json.dump({"sheets": {}}, fh)
            with open(os.path.join(sheets, "a.md"), "w") as fh:
                fh.write("# NOTE !!! a\n# NOTE !!! a\n")
            with open(os.path.join(sheets, "b.md"), "w") as fh:
                fh.write("# NOTE !!! b\n")
            argv = ["--sheets", sheets, "--meta", meta, "--baseline", base]
            self.assertEqual(c.main(argv + ["--update-baseline"]), 0)        # first baseline
            with open(os.path.join(sheets, "a.md"), "w") as fh:
                fh.write("# NOTE !!! a\n")                                    # a fell ...
            with open(os.path.join(sheets, "b.md"), "w") as fh:
                fh.write("# NOTE !!! b\n# NOTE !!! b\n")                     # ... b rose
            self.assertEqual(c.main(argv + ["--update-baseline"]), 1)
            self.assertEqual(c.load_baseline(base)["note-wall"], {"a.md": 2, "b.md": 1})


class RealTree(unittest.TestCase):
    def test_tree_matches_baseline(self):
        worse, _ = c.compare(c.survey(), c.load_baseline(c.BASELINE))
        self.assertEqual(worse, [], "the tree regressed against data/cheatsheet_lint_baseline.json")

    def test_rules_still_match_something(self):
        # A rule that drops to zero overnight more likely broke than got fixed —
        # unless the baseline agrees it is zero. So every rule the baseline still
        # holds findings for must still find them, and at least one rule must.
        cur = c.survey()
        base = c.load_baseline(c.BASELINE)
        live = [r for r in c.RULES if sum(base[r].values())]
        self.assertTrue(live, "the baseline holds no findings at all")
        for rule in live:
            self.assertGreater(sum(cur[rule].values()), 0, rule)
        # and the matcher itself still fires on a known-bad line
        self.assertEqual(c.count_file("x.md", "# NOTE !!! x\n", 0).get("note-wall"), 1)

    def test_baseline_names_every_rule(self):
        with open(c.BASELINE, encoding="utf-8") as fh:
            data = json.load(fh)
        self.assertEqual(set(data) - {"_comment"}, set(c.RULES))


if __name__ == "__main__":
    unittest.main(verbosity=1)
