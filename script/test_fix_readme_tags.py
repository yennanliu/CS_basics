#!/usr/bin/env python3
"""Tests for script/fix_readme_tags.py's pure helpers (no network, no README write).

    python3 script/test_fix_readme_tags.py
"""

import os
import sys
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import fix_readme_tags as f  # noqa: E402


class NormaliseCell(unittest.TestCase):
    def test_bare_company_is_backticked(self):
        self.assertEqual(f.normalise_cell(" dp, google"), (" dp, `google`", True))

    def test_alias_next_to_its_canonical_tag_is_dropped(self):
        # LC 1676 ended up with `microsoft` twice: `m$` was rewritten in place
        self.assertEqual(f.normalise_cell(" `microsoft`, m$"), (" `microsoft`", True))
        self.assertEqual(f.normalise_cell(" `google`, M$, `microsoft`, tree"),
                         (" `google`, `microsoft`, tree", True))

    def test_split_span_does_not_duplicate(self):
        cell, changed = f.normalise_cell(" `microsoft`, `tree, M$`")
        self.assertTrue(changed)
        self.assertEqual(cell.count("microsoft"), 1)

    def test_clean_cell_is_unchanged(self):
        self.assertEqual(f.normalise_cell(" `amazon`, `google`"), (" `amazon`, `google`", False))


class TypeTags(unittest.TestCase):
    def test_mixed_case_key_is_found_from_a_lowercased_tag(self):
        self.assertEqual(f.TYPE_TAG_KEYS.get(f.tag_key("BST")), "BST")


class LiveReadme(unittest.TestCase):
    def test_no_row_lists_a_company_twice(self):
        with open(os.path.join(f.ROOT, "README.md"), encoding="utf-8") as fh:
            lines = fh.read().split("\n")
        dupes = []
        for idx, lc, _, cells in f.iter_rows(lines):
            comps = [c for c in (f.canonical_company(p) for p in f.split_tags(cells[-2])) if c]
            if len(comps) != len(set(comps)):
                dupes.append(lc)
        self.assertEqual(dupes, [])


if __name__ == "__main__":
    unittest.main()
