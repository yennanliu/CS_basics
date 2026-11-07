#!/usr/bin/env python3
"""Tests for script/release_notes.py.  Run: python3 script/test_release_notes.py"""
import datetime as dt
import os
import sys
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import release_notes as rn  # noqa: E402


class VersionTest(unittest.TestCase):
    DAY = dt.date(2026, 11, 7)

    def test_date_tag(self):
        self.assertEqual(rn.next_version(self.DAY, []), "V20261107")

    def test_same_day_gets_a_suffix(self):
        self.assertEqual(rn.next_version(self.DAY, ["V20261107"]), "V20261107.2")
        self.assertEqual(rn.next_version(self.DAY, ["V20261107", "V20261107.2"]), "V20261107.3")


class SummariseTest(unittest.TestCase):
    def test_areas_and_new_solutions(self):
        counts, sols = rn.summarise("\n".join([
            "A\tleetcode_python/Hash_table/foo.py",
            "M\tleetcode_python/Hash_table/bar.py",
            "A\tleetcode_java/src/main/java/LeetCodeJava/Array/Foo.java",
            "M\tdoc/cheatsheet/heap.md",
            "M\tdoc/readme.md",
            "D\tcourses/x.md",
        ]))
        self.assertEqual(counts["Python solutions"], [2, 1])
        self.assertEqual(counts["Java solutions"], [1, 1])
        self.assertEqual(counts["Cheatsheets"], [1, 0])   # the more specific prefix wins
        self.assertEqual(counts["Other docs"], [1, 0])
        self.assertEqual(counts["Other"], [1, 0])
        self.assertEqual(sols["Python"], ["leetcode_python/Hash_table/foo.py"])
        self.assertEqual(len(sols["Java"]), 1)

    def test_practice_days_counts_only_added_date_lines(self):
        diff = "+20261107: 93(again!!!)\n+\n-20261101: 1(ok)\n+ wrapped 20261105 text\n+20261106: 721\n"
        self.assertEqual(rn.practice_days(diff), 2)

    def test_render_mentions_the_essentials(self):
        counts, sols = rn.summarise("A\tleetcode_python/Array/a.py")
        body = rn.render("V20261107", "V20260902", 12, counts, sols, 3, "2026-09-02")
        self.assertIn("## CS_basics V20261107", body)
        self.assertIn("compare/V20260902...V20261107", body)
        self.assertIn("| Python solutions | 1 | 1 |", body)
        self.assertIn("**3** day(s)", body)
        self.assertIn("blob/V20261107/leetcode_python/Array/a.py", body)


class RealRepoTest(unittest.TestCase):
    def test_notes_against_the_real_history(self):
        if "V20260902" not in rn.tags():
            self.skipTest("release tags not fetched")
        body = rn.notes("V99999999", since="V20260902")
        self.assertIn("since `V20260902`", body)
        self.assertRegex(body, r"\*\*\d+\*\* commits")


if __name__ == "__main__":
    unittest.main()
