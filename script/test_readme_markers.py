#!/usr/bin/env python3
"""Tests for script/readme_markers.py and the shared MUST rule it reads by.

    python3 script/test_readme_markers.py
"""

import os
import sys
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import readme_markers as rm  # noqa: E402
import suggest_review as sr  # noqa: E402

HEADER = "| # | Title | Solution | Time | Space | Difficulty | Note | Status |"
RULE = "|---|---|---|---|---|---|---|---|"


def row(lc, note="", status=""):
    return "| %04d | [P%d](https://leetcode.com/problems/p%d/) | [Python](./x.py) | O(n) | O(1) | Easy | %s | %s |" % (
        lc, lc, lc, note, status)


README = "\n".join([
    "# Repo",
    "Prose that says AGAIN and MUST, which grep used to pick up.",
    "## Array",
    HEADER, RULE,
    row(1, "`google`", "AGAIN** (MUST)"),
    row(2, "the window must be non-decreasing", "OK*"),
    row(3, "MUST, two pointers", "OK"),
    row(4, "try again later", ""),
    row(5, "", "AGAIN*"),
    "## Newly Added (kamyu104 gap)",
    "### Array",
    HEADER, RULE,
    row(6, "EVERY ROW MUST EQUAL ROW 0", "imported"),
    row(7, "AGAIN", "imported"),
])


class IsMust(unittest.TestCase):
    def test_status_cell_any_casing(self):
        self.assertTrue(sr.is_must("", "AGAIN* (must)"))

    def test_note_needs_an_all_caps_token(self):
        self.assertTrue(sr.is_must("MUST, dp", "OK"))
        self.assertFalse(sr.is_must("the window must be non-decreasing", "OK"))

    def test_imported_row_never(self):
        self.assertFalse(sr.is_must("EVERY ROW MUST EQUAL", "imported"))


class MarkedRows(unittest.TestCase):
    def ids(self, marker):
        return [int(line.split("|")[1]) for _, line in rm.marked_rows(README, marker)]

    def test_again_reads_the_status_word_only(self):
        # not the prose line, not "try again" in a note, not the imported index
        self.assertEqual(self.ids("again"), [1, 5])

    def test_must_uses_the_shared_rule(self):
        self.assertEqual(self.ids("must"), [1, 3])

    def test_rows_are_written_verbatim_with_their_line(self):
        (line_no, line), = [r for r in rm.marked_rows(README, "must") if "P3]" in r[1]]
        self.assertEqual(README.split("\n")[line_no - 1], line)

    def test_render_header_counts_rows(self):
        text = rm.render(rm.marked_rows(README, "again"), "again")
        self.assertIn("# LeetCode Problems with AGAIN marker", text)
        self.assertIn("# Total count: 2", text)
        self.assertTrue(text.endswith(row(5, "", "AGAIN*") + "\n"))


class LiveReadme(unittest.TestCase):
    """The checked-in README: the lists agree with the planner's parser."""

    @classmethod
    def setUpClass(cls):
        with open(os.path.join(rm.ROOT, "README.md"), encoding="utf-8") as f:
            cls.text = f.read()
        cls.problems = sr.parse_readme(os.path.join(rm.ROOT, "README.md"))

    def test_must_ids_match_parse_readme(self):
        listed = {int(line.split("|")[1]) for _, line in rm.marked_rows(self.text, "must")}
        self.assertEqual(listed, {n for n, p in self.problems.items() if p["must"]})

    def test_again_list_is_not_empty(self):
        self.assertGreater(len(rm.marked_rows(self.text, "again")), 100)


if __name__ == "__main__":
    unittest.main()
