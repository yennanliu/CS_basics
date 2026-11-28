#!/usr/bin/env python3
"""Offline tests for script/lc_graphql.py — urlopen is patched, nothing is fetched.

    python3 script/test_lc_graphql.py
"""

import io
import json
import os
import sys
import unittest
import urllib.error
from unittest import mock

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import lc_graphql as g  # noqa: E402


class FakeResponse(io.BytesIO):
    def __enter__(self):
        return self

    def __exit__(self, *exc):
        return False


def ok(payload):
    return FakeResponse(json.dumps(payload).encode())


def http_error(code, body=b"no"):
    return urllib.error.HTTPError(g.GRAPHQL_URL, code, "x", {}, io.BytesIO(body))


class Check(unittest.TestCase):
    def test_returns_data(self):
        self.assertEqual(g.check({"data": {"a": 1}}), {"a": 1})

    def test_a_200_with_errors_is_a_failure(self):
        with self.assertRaisesRegex(g.GraphQLError, "bad field"):
            g.check({"errors": [{"message": "bad field"}], "data": None})

    def test_no_data(self):
        with self.assertRaises(g.GraphQLError):
            g.check({"data": None})

    def test_not_an_object(self):
        with self.assertRaises(g.GraphQLError):
            g.check(["html?"])


class Post(unittest.TestCase):
    def test_success_sends_the_query_and_referer(self):
        with mock.patch("urllib.request.urlopen", return_value=ok({"data": {"x": 1}})) as u:
            self.assertEqual(g.post("q", {"v": 1}, referer="https://leetcode.com/u/a/"), {"x": 1})
        req = u.call_args[0][0]
        self.assertEqual(json.loads(req.data), {"query": "q", "variables": {"v": 1}})
        self.assertEqual(req.get_header("Referer"), "https://leetcode.com/u/a/")

    def test_400_is_never_retried(self):
        with mock.patch("urllib.request.urlopen", side_effect=http_error(400)) as u:
            with self.assertRaisesRegex(g.GraphQLError, "HTTP 400"):
                g.post("q", tries=3, quiet=True)
        self.assertEqual(u.call_count, 1)

    def test_retries_then_succeeds_with_backoff(self):
        waits = []
        with mock.patch("urllib.request.urlopen",
                        side_effect=[http_error(429), urllib.error.URLError("down"), ok({"data": {}})]), \
                mock.patch("time.sleep", side_effect=waits.append):
            self.assertEqual(g.post("q", tries=3, quiet=True,
                                    backoff=lambda attempt, exc: attempt + 10), {})
        self.assertEqual(waits, [10, 11])

    def test_spent_tries_reraise_the_network_error(self):
        with mock.patch("urllib.request.urlopen", side_effect=urllib.error.URLError("down")), \
                mock.patch("time.sleep") as sleep:
            with self.assertRaises(urllib.error.URLError):
                g.post("q", tries=2, quiet=True, backoff=lambda a, e: 1)
        self.assertEqual(sleep.call_count, 1)      # no sleep after the last try

    def test_graphql_error_is_not_retried(self):
        with mock.patch("urllib.request.urlopen",
                        return_value=ok({"errors": [{"message": "nope"}]})) as u:
            with self.assertRaises(g.GraphQLError):
                g.post("q", tries=3, quiet=True)
        self.assertEqual(u.call_count, 1)


class Callers(unittest.TestCase):
    """The adapters keep each caller's old contract."""

    def test_scraper_gql_returns_none_on_a_rejected_query(self):
        import scrape_lc_discuss_company as s
        with mock.patch("urllib.request.urlopen", return_value=ok({"errors": [{"message": "x"}]})), \
                mock.patch("sys.stderr", new=io.StringIO()):
            self.assertIsNone(s.gql("q", {}, delay=0))

    def test_eval_gql_returns_the_whole_payload(self):
        import eval_lc_readiness as e
        with mock.patch("urllib.request.urlopen", return_value=ok({"data": {"m": 1}})):
            self.assertEqual(e.gql("q", {}, "someone"), {"data": {"m": 1}})
        self.assertIs(e.GraphQLError, g.GraphQLError)

    def test_fetch_tag_none_on_failure(self):
        import add_lc_problem_lists as a
        with mock.patch("urllib.request.urlopen", side_effect=urllib.error.URLError("down")), \
                mock.patch("sys.stderr", new=io.StringIO()):
            self.assertIsNone(a.fetch_tag("array"))


if __name__ == "__main__":
    unittest.main()
