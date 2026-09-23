#!/usr/bin/env python3
"""
Unit tests for script/l3_core.py.

    python3 script/test_l3_core.py
    python3 script/test_l3_core.py -v

Synthetic README, list and log fixtures for the rules; the real files at the
bottom for the vacuous-pass guards — the set must be non-empty and every id in
data/l3_core.json must still be what the rule produces, or `refresh --check`
in CI would be the first to know.
"""
import json
import os
import sys
import tempfile
import time
import unittest

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import l3_core as lc  # noqa: E402


def ts(day):
    return time.mktime(time.strptime(day, "%Y%m%d"))


def problem(num, section="Array", must=False, status=None, passes=0, tags=()):
    return {"lc": num, "title": "P%d" % num, "url": "", "difficulty": "Medium",
            "section": section, "also_in": [], "note": "", "status": status,
            "passes": passes, "must": must, "tags": set(tags), "paths": []}


class Rule(unittest.TestCase):
    def test_blind75_always_in_and_neetcode150_only_with_must(self):
        problems = {1: problem(1), 2: problem(2, must=True), 3: problem(3), 4: problem(4, must=True)}
        lists = {1: {"blind75", "neetcode150"}, 2: {"neetcode150"}, 3: {"neetcode150"},
                 4: {"neetcode250"}}
        self.assertEqual(lc.core_ids(problems, lists), [1, 2])

    def test_a_problem_readme_does_not_index_is_left_out(self):
        problems = {1: problem(1)}
        lists = {1: {"blind75"}, 99: {"blind75"}}
        self.assertEqual(lc.core_ids(problems, lists), [1])

    def test_write_and_load_round_trip(self):
        with tempfile.TemporaryDirectory() as d:
            path = os.path.join(d, "l3_core.json")
            lc.write_core([3, 1, 2], path)
            data = lc.load_core(path)
            self.assertEqual(data["ids"], [3, 1, 2])
            self.assertEqual(data["rule"], lc.RULE)
            self.assertEqual(data["count"], 3)


LOG = """\
20260101: 139(again), 300, topo_sort
20260102  322(todo), others: 678(ok)
20260103: 139(ok), 300(again!!), 300(ok, but again)
20260104: DP: 44(again), 10(ok)
"""


class LatestVerdicts(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.NamedTemporaryFile("w", suffix=".txt", delete=False)
        self.tmp.write(LOG)
        self.tmp.close()
        self.state, self.warnings = lc.latest_verdicts(self.tmp.name)

    def tearDown(self):
        os.unlink(self.tmp.name)

    def test_the_latest_annotation_wins(self):
        self.assertEqual(self.state[139]["verdict"], "ok")
        self.assertEqual(self.state[139]["attempts"], 2)
        self.assertEqual(self.state[139]["ts"], ts("20260103"))

    def test_a_bare_number_is_no_verdict_and_again_beats_ok_in_one_note(self):
        # 300 is bare on day 1, then "again!!" then "(ok, but again)" — still again
        self.assertEqual(self.state[300]["verdict"], "again")
        self.assertEqual(self.state[300]["attempts"], 3)

    def test_todo_is_no_verdict(self):
        self.assertEqual(self.state[322]["verdict"], "none")

    def test_labels_are_stripped_the_way_suggest_review_strips_them(self):
        self.assertEqual(self.state[678]["verdict"], "ok")   # "others: 678(ok)"
        self.assertEqual(self.state[44]["verdict"], "again")  # "DP: 44(again)"
        self.assertNotIn("topo_sort", self.state)

    def test_first_and_last_dates(self):
        self.assertEqual(self.state[300]["first_ts"], ts("20260101"))
        self.assertEqual(self.state[10]["ts"], ts("20260104"))

    def test_an_unreadable_date_is_a_warning_not_a_crash(self):
        with tempfile.NamedTemporaryFile("w", suffix=".txt", delete=False) as f:
            f.write("20260229: 1(ok)\n20260301: 2(ok)\n")
        try:
            state, warnings = lc.latest_verdicts(f.name)
        finally:
            os.unlink(f.name)
        self.assertEqual(len(warnings), 1)
        self.assertIn("20260229", warnings[0])
        self.assertNotIn(1, state)          # nothing to date it by
        self.assertEqual(state[2]["verdict"], "ok")


class Session(unittest.TestCase):
    def rows(self):
        now = ts("20260301")
        problems = {
            1: problem(1, "Array"), 2: problem(2, "Array"), 3: problem(3, "Tree"),
            4: problem(4, "Tree"), 5: problem(5, "Graph"), 6: problem(6, "Graph"),
            7: problem(7, "Stack"),
        }
        log = {
            1: {"verdict": "ok", "ts": now - 2 * lc.DAY, "attempts": 3, "first_ts": now},     # fresh ok
            2: {"verdict": "again", "ts": now - 10 * lc.DAY, "attempts": 9, "first_ts": now},  # again
            3: {"verdict": "none", "ts": now - 20 * lc.DAY, "attempts": 2, "first_ts": now},   # no verdict
            4: {"verdict": "ok", "ts": now - 45 * lc.DAY, "attempts": 1, "first_ts": now},     # stale ok
            5: {"verdict": "again", "ts": now - 1 * lc.DAY, "attempts": 2, "first_ts": now},   # too recent
            # 6 never logged
            7: {"verdict": "again", "ts": now - 40 * lc.DAY, "attempts": 4, "first_ts": now},
        }
        return lc.build_rows([1, 2, 3, 4, 5, 6, 7], problems, log, now=now)

    def test_priority_never_then_unverdicted_then_again_then_stale_ok(self):
        picked = [r["lc"] for r in lc.pick(self.rows(), 10)]
        self.assertEqual(picked[0], 6)                  # never in the log
        self.assertEqual(picked[1], 3)                  # attempted, no verdict
        self.assertEqual(set(picked[2:4]), {2, 7})      # the agains
        self.assertEqual(picked[4], 4)                  # the stale ok
        self.assertNotIn(1, picked)                     # a fresh ok is not offered
        self.assertNotIn(5, picked)                     # logged yesterday

    def test_round_robin_spreads_a_session_over_sections(self):
        picked = lc.pick(self.rows(), 3)
        self.assertEqual(len({r["section"] for r in picked}), 3)

    def test_summary_counts_and_chronic(self):
        s = lc.summarise(self.rows())
        self.assertEqual((s["size"], s["ok"], s["again"], s["none"], s["never"]), (7, 2, 3, 1, 1))
        self.assertEqual(s["unverdicted"], 2)
        self.assertEqual(s["chronic"], [2])            # again with 8+ attempts
        self.assertEqual(s["by_section"]["Tree"]["total"], 2)


class Live(unittest.TestCase):
    """The real files. Guards against the rule quietly producing nothing, and
    against data/l3_core.json drifting from the rule it claims to encode."""

    def test_rule_produces_a_set_and_the_file_matches_it(self):
        import suggest_review as sr
        problems = sr.parse_readme(lc.README)
        lists = sr.load_problem_lists(lc.LISTS)
        ids = lc.core_ids(problems, lists)
        self.assertGreater(len(ids), 75)
        self.assertLess(len(ids), 150)
        data = lc.load_core()
        self.assertIsNotNone(data, "data/l3_core.json is missing — run: python3 script/l3_core.py refresh")
        self.assertEqual(data["ids"], ids, "data/l3_core.json is stale — run: python3 script/l3_core.py refresh")

    def test_the_log_parses_to_verdicts(self):
        state, warnings = lc.latest_verdicts()
        self.assertGreater(len(state), 500)
        self.assertTrue(all(v["verdict"] in ("ok", "again", "none") for v in state.values()))


if __name__ == "__main__":
    unittest.main()
