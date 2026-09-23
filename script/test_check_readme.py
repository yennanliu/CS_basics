#!/usr/bin/env python3
"""
Unit tests for script/check_readme.py.

    python3 script/test_check_readme.py
    python3 script/test_check_readme.py -v

Each rule is asserted against a bad fixture as well as a good one: a gate that
only ever sees valid input cannot tell you it still works. `Live` at the bottom
runs the real README against the committed baseline, so the checked-in state is
always a passing one.
"""
import io
import json
import os
import sys
import tempfile
import textwrap
import unittest
from contextlib import redirect_stdout

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import check_readme as cr  # noqa: E402


HEADER = textwrap.dedent("""\
    ## Array

    |  #  | Title | Solution | Time | Space | Difficulty | Note | Status |
    |-----|-------|----------|------|-------|------------|------|--------|
    """)


def row(num, title="T", sol="[Python](./leetcode_python/Array/a.py)", status="OK", note="**array**"):
    return "| %04d | [%s](https://leetcode.com/problems/x/) | %s | _O(n)_ | _O(1)_ | Easy | %s | %s |\n" % (
        num, title, sol, note, status)


class Fixture:
    """A throwaway repo: a README, a log, and the solution files it points at."""

    def __init__(self, readme, progress="20260101: 1(ok)\n", files=("leetcode_python/Array/a.py",)):
        self.dir = tempfile.mkdtemp()
        for f in files:
            full = os.path.join(self.dir, f)
            os.makedirs(os.path.dirname(full), exist_ok=True)
            open(full, "w").close()
        self.readme = readme
        self.progress = progress

    def findings(self):
        return cr.findings(self.readme, self.progress, root=self.dir)

    def cleanup(self):
        import shutil
        shutil.rmtree(self.dir)


def run(f, baseline=None, strict=False):
    """-> (failed count, output) of the report over findings `f`."""
    rep = cr.Report()
    buf = io.StringIO()
    with redirect_stdout(buf):
        cr.run(rep, f, baseline or {}, strict=strict)
    return rep.failed, buf.getvalue()


class Rows(unittest.TestCase):
    def test_reads_the_ends_of_the_row_and_the_two_table_sets(self):
        text = HEADER + row(1, status="AGAIN* (2)") + "\n## Newly Added (kamyu104 gap)\n\n### Array\n\n" + \
            "|  #  | Title | Solution | Time | Space | Difficulty | Note | Status |\n|--|--|--|--|--|--|--|--|\n" + \
            row(2, status="")
        rows = cr.parse_rows(text)
        self.assertEqual([(r["id"], r["imported"], r["section"]) for r in rows],
                         [(1, False, "Array"), (2, True, "Array")])
        self.assertEqual(rows[0]["status"], "AGAIN* (2)")
        self.assertEqual(rows[0]["solutions"], [("Python", "./leetcode_python/Array/a.py")])

    def test_a_row_without_a_linked_title_is_a_bad_row(self):
        fx = Fixture(HEADER + row(1) + "| 0002 | Plain title | [Python](./leetcode_python/Array/a.py) | _O(n)_ | _O(1)_ | Easy | x | OK |\n")
        try:
            f = fx.findings()
            self.assertEqual([r["id"] for r in f["bad_rows"]], [2])
            failed, _ = run(f)
            self.assertGreaterEqual(failed, 1)
        finally:
            fx.cleanup()


class Links(unittest.TestCase):
    def test_a_dead_relative_link_fails_and_a_url_is_not_checked(self):
        fx = Fixture(HEADER + row(1, sol="[Python](./leetcode_python/Array/missing.py), [C++](https://example.com/x.cpp)"))
        try:
            f = fx.findings()
            self.assertEqual([d["target"] for d in f["dead_links"]], ["./leetcode_python/Array/missing.py"])
            failed, out = run(f)
            self.assertIn("FAIL  every relative solution link resolves", out)
        finally:
            fx.cleanup()

    def test_a_resolving_link_passes_and_counts_the_file_as_linked(self):
        fx = Fixture(HEADER + row(1), files=("leetcode_python/Array/a.py", "leetcode_python/Array/orphan.py"))
        try:
            f = fx.findings()
            self.assertEqual(f["dead_links"], [])
            self.assertEqual(f["linked"], 1)
            self.assertEqual(f["unlinked"]["python"], ["leetcode_python/Array/orphan.py"])
            failed, out = run(f)
            self.assertIn("PASS  every relative solution link resolves", out)
            # unlinked files are reported, never failed
            self.assertIn("python: 1 files no README row links to", out)
        finally:
            fx.cleanup()

    def test_the_baseline_tolerates_a_known_dead_link_but_not_a_new_one(self):
        fx = Fixture(HEADER + row(1, sol="[C++](./C++/old.cpp), [Python](./leetcode_python/Array/new-typo.py)"))
        try:
            f = fx.findings()
            base = {"dead_links": ["./C++/old.cpp"]}
            failed, out = run(f, base)
            self.assertIn("FAIL  every relative solution link resolves", out)
            self.assertIn("new-typo.py", out)
            self.assertIn("1 baselined", out)
            # with only the known one, it passes; --strict fails it again
            fx.readme = HEADER + row(1, sol="[C++](./C++/old.cpp)")
            f = fx.findings()
            self.assertIn("PASS", run(f, base)[1].split("solution links")[1].split("\n")[1])
            self.assertIn("FAIL", run(f, base, strict=True)[1].split("solution links")[1].split("\n")[1])
        finally:
            fx.cleanup()


class Duplicates(unittest.TestCase):
    def test_an_id_in_both_table_sets_fails_and_two_main_sections_is_allowed(self):
        text = HEADER + row(1) + row(5) + "\n## Graph\n\n|a|b|c|d|e|f|g|h|\n|-|-|-|-|-|-|-|-|\n" + row(5) + \
            "\n## Newly Added (kamyu104 gap)\n\n### Array\n\n|a|b|c|d|e|f|g|h|\n|-|-|-|-|-|-|-|-|\n" + row(1, status="")
        fx = Fixture(text)
        try:
            f = fx.findings()
            self.assertEqual(f["cross_duplicates"], [1])
            self.assertEqual(f["main_duplicates"], [5])
            failed, out = run(f)
            self.assertIn("FAIL  no id is filed in both", out)
            self.assertEqual(run(f, {"cross_duplicates": [1]})[1].count("FAIL"), 0)
        finally:
            fx.cleanup()


class Status(unittest.TestCase):
    GOOD = ["OK", "AGAIN", "again", "Again (1)", "OK* (2)", "AGAIN*** (3)", "AGAIN* (4)(MUST)",
            "AGAIN (not start)", "AGAIN* (not start*) (2)", "OK* (2) (but again, MUST)", "not start",
            "Not start* (1) (good basic)", "AGAIN (7)*", "", "(not start)", "AGAIN * (2)",
            "again************ (4)(MUST)", "OK*  (2)"]
    BAD = ["AGAIN(1)***** not start", "AGAIN*** (1)s", "OK**** (2) (but again !!!", "done", "AGAIN (1) again",
           "OK - see 128", "AGAIN**1"]

    def test_the_shapes_three_scripts_read_all_parse(self):
        for cell in self.GOOD:
            self.assertTrue(cr.STATUS_RE.match(cell), repr(cell))

    def test_prose_where_the_word_goes_does_not(self):
        for cell in self.BAD:
            self.assertFalse(cr.STATUS_RE.match(cell), repr(cell))

    def test_only_main_table_cells_are_checked(self):
        text = HEADER + row(1, status="AGAIN*** (1)s") + \
            "\n## Newly Added (kamyu104 gap)\n\n### Array\n\n|a|b|c|d|e|f|g|h|\n|-|-|-|-|-|-|-|-|\n" + row(2, status="garbage here")
        fx = Fixture(text)
        try:
            f = fx.findings()
            self.assertEqual([r["id"] for r in f["bad_status"]], [1])
            failed, out = run(f)
            self.assertIn("FAIL  every main-table status cell parses", out)
            self.assertEqual(run(f, {"bad_status": ["AGAIN*** (1)s"]})[1].count("FAIL"), 0)
        finally:
            fx.cleanup()


class Dates(unittest.TestCase):
    def test_an_impossible_date_fails_unless_baselined(self):
        fx = Fixture(HEADER + row(1), progress="20260228: 1(ok)\n20260229: 2(again)\n  wrapped, 3\n20260301: 4\n")
        try:
            f = fx.findings()
            self.assertEqual(f["dates"], 3)
            self.assertEqual(f["bad_dates"], [(2, "20260229")])
            self.assertIn("FAIL  every date header", run(f)[1])
            self.assertEqual(run(f, {"bad_dates": ["20260229"]})[1].count("FAIL"), 0)
            self.assertIn("FAIL  every date header", run(f, {"bad_dates": ["20260229"]}, strict=True)[1])
        finally:
            fx.cleanup()


class Baseline(unittest.TestCase):
    def test_baseline_records_strings_not_line_numbers_and_round_trips(self):
        fx = Fixture(HEADER + row(1, sol="[C++](./C++/x.cpp)", status="weird cell"),
                     progress="20260229: 1\n")
        try:
            f = fx.findings()
            base = cr.baseline_of(f)
            self.assertEqual(base["dead_links"], ["./C++/x.cpp"])
            self.assertEqual(base["bad_status"], ["weird cell"])
            self.assertEqual(base["bad_dates"], ["20260229"])
            data_only = {k: v for k, v in base.items() if k != "_comment"}
            self.assertNotIn("line", json.dumps(data_only))
            self.assertEqual(run(f, base)[0], 0)
        finally:
            fx.cleanup()


class Live(unittest.TestCase):
    """The committed README, log and baseline must pass together."""

    def test_the_checked_in_state_passes_against_its_baseline(self):
        with open(cr.README, encoding="utf-8") as fh:
            readme = fh.read()
        with open(cr.PROGRESS, encoding="utf-8") as fh:
            progress = fh.read()
        f = cr.findings(readme, progress)
        self.assertGreater(len(f["rows"]), 3000)
        self.assertGreater(f["linked"], 4000)
        failed, out = run(f, cr.load_baseline(cr.BASELINE))
        self.assertEqual(failed, 0, out)

    def test_the_baseline_is_not_stale(self):
        """A fix that removes a known problem should shrink the baseline too,
        or the file quietly tolerates a problem that no longer exists — and
        the same string, reintroduced, would slip through."""
        with open(cr.README, encoding="utf-8") as fh:
            readme = fh.read()
        with open(cr.PROGRESS, encoding="utf-8") as fh:
            progress = fh.read()
        current = cr.baseline_of(cr.findings(readme, progress))
        committed = cr.load_baseline(cr.BASELINE)
        for key in ("dead_links", "cross_duplicates", "bad_status", "bad_dates"):
            self.assertEqual(sorted(committed.get(key, [])), sorted(current[key]),
                             "%s: run python3 script/check_readme.py --update-baseline" % key)


if __name__ == "__main__":
    unittest.main()
