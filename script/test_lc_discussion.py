#!/usr/bin/env python3
"""Offline tests for script/lc_discussion.py — the parts that decide what a
summary is built from. Nothing here touches the network."""
import io
import os
import sys
import time
import unittest
from contextlib import redirect_stdout

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import lc_discussion as d  # noqa: E402


def node(topic, title, created, summary="", ups=0):
    return {"topicId": topic, "slug": "s-%d" % topic, "title": title, "summary": summary,
            "createdAt": created + "T00:00:00+00:00",
            "reactions": [{"count": ups, "reactionType": "UPVOTE"},
                          {"count": 9, "reactionType": "THUMBS_DOWN"}]}


NOW = time.mktime(time.strptime("2026-10-02", "%Y-%m-%d"))


class Shortlist(unittest.TestCase):
    def test_every_term_must_appear(self):
        nodes = [node(1, "Google L3 onsite", "2026-09-30"),
                 node(2, "Crack AI/ML jobs", "2026-09-30", summary="google mentioned once"),
                 node(3, "Meta E4", "2026-09-30")]
        kept = d.shortlist(nodes, ["google", "l3"], now=NOW)
        self.assertEqual([n["topicId"] for n in kept], [1])

    def test_loose_keeps_partial_matches(self):
        nodes = [node(1, "Google L3", "2026-09-30"), node(2, "google AI", "2026-09-30")]
        self.assertEqual(len(d.shortlist(nodes, ["google l3"], loose=True, now=NOW)), 2)

    def test_days_window_and_dedupe_and_order(self):
        nodes = [node(1, "google a", "2026-09-01"), node(2, "google b", "2026-09-30"),
                 node(2, "google b", "2026-09-30"), node(3, "google c", "2026-01-01")]
        kept = d.shortlist(nodes, ["google"], days=60, now=NOW)
        self.assertEqual([n["topicId"] for n in kept], [2, 1])

    def test_upvotes_count_only_upvotes(self):
        self.assertEqual(d.upvotes(node(1, "x", "2026-09-30", ups=60)), 60)

    def test_post_url(self):
        self.assertEqual(d.post_url(node(8543506, "x", "2026-09-27")),
                         "https://leetcode.com/discuss/post/8543506/s-8543506/")


class NamedProblems(unittest.TestCase):
    def test_links_and_numbers(self):
        text = ("Same as https://leetcode.com/problems/maximum-profit-in-job-scheduling/description/ "
                "and LC 359, also leetcode 1101; not 2026 the year")
        slugs, nums = d.named_problems(text)
        self.assertEqual(slugs, ["maximum-profit-in-job-scheduling"])
        self.assertEqual(nums, [359, 1101])


class Xref(unittest.TestCase):
    def test_reads_the_real_index_and_log(self):
        out = io.StringIO()
        with redirect_stdout(out):
            d.main(["xref", "1", "999999"])
        text = out.getvalue()
        self.assertIn("Two Sum", text)
        self.assertIn("(not in README)", text)


if __name__ == "__main__":
    unittest.main()
