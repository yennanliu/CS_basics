#!/usr/bin/env python3
"""Offline tests for script/lc_discussion.py — the parts that decide what a
summary is built from. Nothing here touches the network."""
import io
import json
import os
import sys
import tempfile
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


def hit(url, title="Google L3 onsite", snippet=None, date=None):
    """A saved search result. Most real ones carry only url and title."""
    h = {"url": url, "title": title}
    if snippet:
        h["snippet"] = snippet
    if date:
        h["date"] = date
    return h


class SourceOf(unittest.TestCase):
    def test_known_sites(self):
        self.assertEqual(d.source_of("https://www.teamblind.com/post/google-l3-abc"), "Blind")
        self.assertEqual(d.source_of("https://www.glassdoor.co.uk/Interview/x.htm"), "Glassdoor")
        self.assertEqual(d.source_of("https://leetcode.com/discuss/post/1/x/"), "LC")

    def test_other_sites_and_lookalikes(self):
        self.assertIsNone(d.source_of("https://www.reddit.com/r/leetcode/x"))
        self.assertIsNone(d.source_of("https://notteamblind.com/post/x"))
        self.assertIsNone(d.source_of("https://teamblind.example.com/post/x"))
        self.assertIsNone(d.source_of("not a url"))

    def test_canonical_merges_spellings(self):
        self.assertEqual(d.canonical("https://www.teamblind.com/post/a/?utm=x#c"),
                         d.canonical("http://teamblind.com/post/a"))


class ShortlistSnippets(unittest.TestCase):
    def test_drops_off_site_duplicates_and_missing_terms(self):
        hits = [hit("https://www.teamblind.com/post/a"),
                hit("https://teamblind.com/post/a/?ref=1"),
                hit("https://www.reddit.com/r/x", title="Google L3"),
                hit("https://www.glassdoor.com/Interview/q.htm", title="Meta E4")]
        kept = d.shortlist_snippets(hits, ["google", "l3"], now=NOW)
        self.assertEqual([h["source"] for h in kept], ["Blind"])

    def test_days_window_keeps_undated_last(self):
        hits = [hit("https://teamblind.com/post/old", date="2026-01-01"),
                hit("https://teamblind.com/post/undated"),
                hit("https://teamblind.com/post/new", date="2026-09-30")]
        kept = d.shortlist_snippets(hits, ["google"], days=30, now=NOW)
        self.assertEqual([h["url"].rsplit("/", 1)[1] for h in kept], ["new", "undated"])

    def test_named_numbers_come_from_the_title_only(self):
        hits = [hit("https://teamblind.com/post/a", title="Google LC 1235",
                    snippet="then leetcode 2812 as follow-up")]
        self.assertEqual(d.shortlist_snippets(hits, ["google"], now=NOW)[0]["named"], [1235])

    def test_snippet_still_counts_for_the_keyword_filter(self):
        hits = [hit("https://teamblind.com/post/a", title="Phone screen", snippet="google l3")]
        self.assertEqual(len(d.shortlist_snippets(hits, ["google", "l3"], now=NOW)), 1)

    def test_a_dropped_copy_does_not_hide_a_qualifying_one(self):
        hits = [hit("https://teamblind.com/post/a", title="Meta E4"),
                hit("https://teamblind.com/post/a", date="2026-01-01"),
                hit("https://www.teamblind.com/post/a/", date="2026-09-30")]
        kept = d.shortlist_snippets(hits, ["google"], days=30, now=NOW)
        self.assertEqual([h["date"] for h in kept], ["2026-09-30"])

    def test_command_reads_the_saved_file(self):
        hits = [hit("https://teamblind.com/post/a", title="Google LC 1235"),
                hit("https://www.glassdoor.com/Interview/q.htm"),
                hit("https://example.com/x")]
        with tempfile.NamedTemporaryFile("w", suffix=".json", delete=False) as f:
            json.dump(hits, f)
        self.addCleanup(os.remove, f.name)
        out = io.StringIO()
        with redirect_stdout(out):
            d.main(["snippets", f.name, "google"])
        text = out.getvalue()
        self.assertIn("2 of 3 results kept (Blind 1, Glassdoor 1); 1 from other sites dropped", text)
        self.assertIn("named: 1235", text)


class LoadHits(unittest.TestCase):
    def write(self, content):
        with tempfile.NamedTemporaryFile("w", suffix=".json", delete=False) as f:
            f.write(content)
        self.addCleanup(os.remove, f.name)
        return f.name

    def test_missing_file_bad_json_and_non_list_stop_with_a_message(self):
        for path in ("/nonexistent/hits.json", self.write("{not json"), self.write('{"url": "x"}')):
            with self.assertRaises(SystemExit) as cm:
                d.load_hits(path)
            self.assertIn("snippets:", str(cm.exception))

    def test_malformed_entries_skipped_and_bad_dates_dropped(self):
        path = self.write(json.dumps([
            {"url": "https://teamblind.com/post/a", "title": "Google", "date": "last week"},
            {"title": "no url"}, "a string", {"url": 42},
            {"url": "https://teamblind.com/post/c", "title": 42},
            {"url": "https://teamblind.com/post/d", "title": "Google", "snippet": ["x"]},
            {"url": "https://teamblind.com/post/e", "title": "Google", "date": "2026-13-01"},
            {"url": "https://teamblind.com/post/f", "title": None},
            {"url": "https://teamblind.com/post/b", "title": "Google", "date": "2026-09-30"}]))
        hits, skipped, undated = d.load_hits(path)
        self.assertEqual((len(hits), skipped, undated), (4, 5, 2))
        self.assertNotIn("date", hits[0])
        self.assertNotIn("date", hits[1])

    def test_loaded_hits_never_crash_the_filter(self):
        path = self.write(json.dumps([
            {"url": "https://teamblind.com/post/a", "title": 7, "date": "2026-02-30"},
            {"url": "https://teamblind.com/post/b", "title": "Google", "date": "2026-02-30"},
            {"url": "https://teamblind.com/post/c", "title": None, "snippet": "google"}]))
        hits, _, _ = d.load_hits(path)
        kept = d.shortlist_snippets(hits, ["google"], days=30, now=NOW)
        self.assertEqual([h["url"][-1] for h in kept], ["b", "c"])

    def test_command_reports_the_counts_and_an_empty_source(self):
        path = self.write(json.dumps([{"url": "https://teamblind.com/post/a", "title": "Google"},
                                      {"title": "no url"}]))
        out = io.StringIO()
        with redirect_stdout(out):
            d.main(["snippets", path, "google"])
        text = out.getvalue()
        self.assertIn("1 of 2 results kept (Blind 1)", text)
        self.assertIn("1 malformed entries skipped", text)
        self.assertIn("no Glassdoor results kept", text)

    def test_empty_list_is_not_an_error(self):
        out = io.StringIO()
        with redirect_stdout(out):
            d.main(["snippets", self.write("[]"), "google"])
        self.assertIn("0 of 0 results kept (none)", out.getvalue())


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
