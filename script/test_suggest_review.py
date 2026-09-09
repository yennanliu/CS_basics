#!/usr/bin/env python3
"""
Unit tests for script/suggest_review.py.

    python3 script/test_suggest_review.py           # all of it
    python3 script/test_suggest_review.py -v
    python3 script/test_suggest_review.py ParseProgress
    python3 script/suggest_review.py --self-test    # the same suite, quietly

Three of the planner's four inputs are hand-written files whose shape nobody
controls — README rows, the practice log, commit subjects. The failure mode
there is not a crash, it is a parser that quietly reads fewer rows than there
are and hands back a plausible but shrunken plan. So the fixtures below are not
invented shapes: every one of them is a line that is really in those files, kept
here so a format drift fails a test instead of silently shrinking the plan.

The unit tests run against those fixtures rather than the real files, so they do
not move whenever a row does. `LiveFiles` at the bottom is what holds the real
ones — including the cross-check that this script and `extract_must_lc.py` still
agree on what a `MUST` row is. That mirrors how site/test/*.test.js is split.
"""
import os
import sys
import textwrap
import unittest
from collections import Counter

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import suggest_review as sr  # noqa: E402

REPO = sr.REPO


def write(tmpdir, name, text):
    path = os.path.join(tmpdir, name)
    with open(path, "w", encoding="utf-8") as fh:
        fh.write(text)
    return path


class TempFileCase(unittest.TestCase):
    def setUp(self):
        import tempfile
        self._tmp = tempfile.TemporaryDirectory()
        self.tmpdir = self._tmp.name
        self.addCleanup(self._tmp.cleanup)


# ── README ──────────────────────────────────────────────────────────────────
README = """
## Resource

| 999 | above the first LC section | | | | Easy | | OK |

## Array

|  #  | Title | Solution | Time | Space | Difficulty | Note | Status |
|-----|-------|----------|------|-------|------------|------|--------|
| 48 | [Rotate Image](https://leetcode.com/problems/rotate-image/) | \
[Python](./leetcode_python/Array/rotate-image.py) | _O(n^2)_ | _O(1)_ | Medium | \
**array**, `google`, `blind75` | AGAIN*** (5) (MUST) |
| 118 | [Pascal's Triangle](https://leetcode.com/problems/pascals-triangle/) | \
[Python](./leetcode_python/Array/pascals-triangle.py) | _O(n^2)_ | _O(1)_ | Easy | \
**array**, the row must be built from the one above, `amazon` | OK** |
| 547 | [Friend Circles](https://leetcode.com/problems/number-of-provinces/) | \
[Python](./leetcode_python/DFS/friend-circles.py) | _O(n^2)_ | _O(n)_ | Medium | \
union find, `google` | AGAIN* |
| 254 | Plain Title With No Link | | _O(n)_ | _O(1)_ | Hard | `fb` | |

## Graph

|  #  | Title | Solution | Time | Space | Difficulty | Note | Status |
|-----|-------|----------|------|-------|------------|------|--------|
| 547 | [Number of Provinces](https://leetcode.com/problems/number-of-provinces/) | \
[Java](./leetcode_java/x/NumberOfProvinces.java) | _O(n^2)_ | _O(n)_ | Medium | \
**graph**, MUST, `fb` | AGAIN***** (2) |
"""


class ParseReadme(TempFileCase):
    def setUp(self):
        super().setUp()
        self.rows = sr.parse_readme(write(self.tmpdir, "README.md", README))

    def test_rows_above_the_first_lc_section_are_not_problems(self):
        # Everything above `## Array` is intro prose — the Note tags table in
        # the real README is pipe-delimited too.
        self.assertNotIn(999, self.rows)

    def test_each_lc_number_appears_once(self):
        self.assertEqual(sorted(self.rows), [48, 118, 254, 547])

    def test_must_in_the_status_cell_is_a_marker(self):
        self.assertTrue(self.rows[48]["must"])

    def test_must_as_prose_in_the_note_is_not_a_marker(self):
        # "the row must be built from the one above" is not a priority flag.
        self.assertFalse(self.rows[118]["must"])

    def test_an_all_caps_must_token_in_the_note_is_a_marker(self):
        self.assertTrue(self.rows[547]["must"])

    def test_a_duplicate_row_folds_into_the_first_sighting(self):
        # LC 547 is `Friend Circles` with no marker under DFS and `Number of
        # Provinces` with MUST under Graph. Keeping only the first row's flags
        # loses the marker, so the strongest claim about a problem wins.
        row = self.rows[547]
        self.assertEqual(row["section"], "Array")
        self.assertEqual(row["also_in"], ["Graph"])
        self.assertTrue(row["must"])
        self.assertEqual(row["passes"], 5)
        self.assertLessEqual({"google", "fb"}, row["tags"])
        self.assertEqual(len(row["paths"]), 2)

    def test_the_star_run_counts_review_passes(self):
        self.assertEqual(self.rows[48]["passes"], 3)

    def test_title_and_url_come_off_the_markdown_link(self):
        self.assertEqual(self.rows[48]["title"], "Rotate Image")
        self.assertTrue(self.rows[48]["url"].endswith("/rotate-image/"))

    def test_a_row_with_no_link_keeps_its_plain_title(self):
        self.assertEqual(self.rows[254]["title"], "Plain Title With No Link")
        self.assertEqual(self.rows[254]["url"], "")

    def test_difficulty_is_read_by_value_not_by_position(self):
        # The real table has ragged complexity cells ("ctor: O(1), next: O(1)
        # amortized"), so the column index is not dependable.
        self.assertEqual(self.rows[118]["difficulty"], "Easy")
        self.assertEqual(self.rows[254]["difficulty"], "Hard")

    def test_status_word_and_an_empty_status_cell(self):
        self.assertEqual(self.rows[48]["status"], "AGAIN")
        self.assertEqual(self.rows[118]["status"], "OK")
        self.assertIsNone(self.rows[254]["status"])

    def test_only_repo_relative_solution_paths_are_kept(self):
        # The problem's own leetcode.com link lives in the title cell, but a
        # row occasionally carries an external link in the solution cell too.
        self.assertEqual(self.rows[48]["paths"],
                         ["leetcode_python/Array/rotate-image.py"])

    def test_separator_and_prose_rows_are_skipped(self):
        rows = sr.parse_readme(write(self.tmpdir, "sep.md", textwrap.dedent("""
            ## Array

            |  #  | Title | Solution | Time | Space | Difficulty | Note | Status |
            |-----|-------|----------|------|-------|------------|------|--------|
            ||`Linear`| | | | | | |
            | 1 | [Two Sum](https://leetcode.com/problems/two-sum/) | x | _O(n)_ | _O(n)_ | Easy | `blind75` | OK |
        """)))
        self.assertEqual(sorted(rows), [1])


# ── data/progress.txt ───────────────────────────────────────────────────────
# Every shape here is one the real log contains.
PROGRESS = """20260909: 4(todo), 34(again!!) | DP: 44(todo)

20260908: 70(ok*, o(1) space!!),198(
ok*),139(again* 1d dp)

20260907: top 100 (backtrack): 51(todo) | (LC must) 438(again), 2289(todo: mono stack + dp)
20260906: 39(again*).79(again*), topo_sort, weekly_331
,53(again)

------ review

20260905  1740(ok)
"""


class ParseProgress(TempFileCase):
    def setUp(self):
        super().setUp()
        self.dates, self.notes, self.warnings = sr.parse_progress(
            write(self.tmpdir, "progress.txt", PROGRESS))

    def test_session_separators_do_not_hide_problems(self):
        # "a | b | c" groups a day into sessions and carries no meaning here.
        self.assertIn(44, self.dates)

    def test_an_annotation_containing_a_comma_stays_one_entry(self):
        # Splitting naively turns "70(ok*, o(1) space!!)" into an entry for
        # LC 70 and one for "o(1) space", and the latter reads as LC 1.
        self.assertIn(70, self.dates)
        self.assertNotIn(1, self.dates)

    def test_an_annotation_wrapped_across_a_newline_is_rejoined(self):
        # "...,198(\nok*),139(again* 1d dp)" — a line ending inside an
        # annotation is held back until its parens balance.
        self.assertIn(198, self.dates)
        self.assertIn(139, self.dates)

    def test_a_period_between_entries_splits_them(self):
        # "39(again*).79(again*)" is a typo for a comma, but it is the log's
        # only period outside an annotation; without the rule LC 79 disappears.
        self.assertIn(39, self.dates)
        self.assertIn(79, self.dates)

    def test_a_bare_continuation_line_joins_the_day_above(self):
        self.assertIn(53, self.dates)

    def test_a_labelled_run_does_not_swallow_its_first_problem(self):
        self.assertIn(44, self.dates)     # "| DP: 44(todo)"
        self.assertIn(51, self.dates)     # "top 100 (backtrack): 51(todo)"
        self.assertIn(438, self.dates)    # "(LC must) 438(again)"

    def test_a_label_of_its_own_is_never_read_as_a_problem(self):
        self.assertNotIn(100, self.dates)  # from "top 100 (backtrack)"

    def test_a_colon_inside_an_annotation_is_not_a_label(self):
        self.assertIn(2289, self.dates)
        self.assertEqual(self.notes[2289]["todo"], 1)

    def test_named_drills_are_not_lc_numbers(self):
        # topo_sort, weekly_331, lazy_bst_in_order are practice but not
        # LeetCode numbers, so they cannot join a per-problem schedule.
        self.assertNotIn(331, self.dates)
        self.assertNotIn(3, self.dates)

    def test_a_separator_line_ends_the_day(self):
        self.assertIn(1740, self.dates)

    def test_again_beats_ok_when_a_note_says_both(self):
        self.assertEqual(self.notes[34]["again"], 1)
        self.assertEqual(self.notes[70]["ok"], 1)

    def test_dates_are_newest_first(self):
        multi = sr.parse_progress(write(self.tmpdir, "two.txt",
                                        "20260901: 1\n\n20260909: 1\n"))[0]
        self.assertEqual(multi[1], sorted(multi[1], reverse=True))

    def test_the_fixture_parses_without_warnings(self):
        self.assertEqual(self.warnings, [])

    def test_an_impossible_date_is_reported_not_dropped_silently(self):
        # 2026 is not a leap year; the real log has a 20260229.
        _, _, warnings = sr.parse_progress(
            write(self.tmpdir, "bad.txt", "20260229: 1(ok)\n"))
        self.assertEqual(len(warnings), 1)
        self.assertIn("20260229", warnings[0])

    def test_a_dated_line_wins_over_an_unclosed_annotation(self):
        # One missing ")" used to swallow every following day into the note.
        # An LC number is at most four digits, so an eight-digit line is a date.
        dates, _, warnings = sr.parse_progress(write(self.tmpdir, "open.txt",
                                                     "20260908: 1(ok\n20260909: 2(ok)\n"))
        self.assertIn(2, dates)
        self.assertEqual(len(warnings), 1)
        self.assertIn("unclosed", warnings[0])

    def test_content_before_any_date_is_reported(self):
        _, _, warnings = sr.parse_progress(
            write(self.tmpdir, "orphan.txt", "12(ok)\n"))
        self.assertTrue(any("before any date" in w for w in warnings))

    def test_a_missing_log_is_a_warning_not_a_crash(self):
        dates, notes, warnings = sr.parse_progress(
            os.path.join(self.tmpdir, "nope.txt"))
        self.assertEqual((dates, notes), ({}, {}))
        self.assertEqual(len(warnings), 1)


class StripLabel(unittest.TestCase):
    def test_shapes(self):
        cases = [
            ("DP: 44(todo)", "44(todo)"),
            ("top 100 (backtrack): 51(todo)", "51(todo)"),
            ("(LC must) 438(again)", "438(again)"),
            ("2289(todo: mono stack + dp)", "2289(todo: mono stack + dp)"),
            ("139(again!!)", "139(again!!)"),
        ]
        for raw, want in cases:
            with self.subTest(raw=raw):
                self.assertEqual(sr._strip_label(raw), want)


class SplitTopLevel(unittest.TestCase):
    def test_commas_inside_parens_do_not_split(self):
        self.assertEqual([c.strip() for c in sr._split_top_level("70(ok*, o(1) space!!),139")],
                         ["70(ok*, o(1) space!!)", "139"])

    def test_a_period_at_depth_zero_splits(self):
        self.assertEqual([c.strip() for c in sr._split_top_level("39(again*).79(ok)")],
                         ["39(again*)", "79(ok)"])


class Classify(unittest.TestCase):
    def test_again_is_checked_before_ok(self):
        self.assertEqual(sr._classify("ok, but again"), "again")
        self.assertEqual(sr._classify("ok*"), "ok")
        self.assertEqual(sr._classify("todo: mono stack"), "todo")
        self.assertEqual(sr._classify(""), "none")
        self.assertEqual(sr._classify("with univeral algo"), "other")


# ── git ─────────────────────────────────────────────────────────────────────
class SubjectLc(unittest.TestCase):
    @staticmethod
    def named(subject):
        return {int(a or b) for a, b in sr.SUBJECT_LC.findall(subject)}

    def test_a_language_suffix_names_a_problem(self):
        self.assertEqual(self.named("update 131 py"), {131})
        self.assertEqual(self.named("update 1353 py, progress"), {1353})

    def test_an_explicit_lc_prefix_names_a_problem(self):
        self.assertEqual(
            self.named("update backtrack cheatsheet: expand LC 131 (Template 8)"),
            {131})

    def test_an_lc_range_is_a_bulk_import_not_a_problem(self):
        self.assertEqual(
            self.named("add 291 solutions for LC 1118-2000 coverage gap"), set())

    def test_a_bare_count_is_not_a_problem(self):
        self.assertEqual(self.named("cover the 13 uncovered classics"), set())
        self.assertEqual(self.named("update PQ cheatsheet"), set())


class GitTouchHistory(unittest.TestCase):
    """The bulk-commit rule, against a canned `git log` rather than the repo's."""

    UNIVERSE = {
        "leetcode_python/Array/rotate-image.py": {48},
        "leetcode_python/Stack/daily-temperatures.py": {739},
    }

    def fake_log(self, text):
        """Stand in for `git log --name-only --pretty=format:'\\x01%ct\\x01%s'`."""
        import subprocess
        from unittest import mock

        completed = subprocess.CompletedProcess([], 0, stdout=text, stderr="")
        return mock.patch.object(subprocess, "run", return_value=completed)

    def test_a_normal_commit_attributes_its_files(self):
        log = "\x01100\x01update 48 py\nleetcode_python/Array/rotate-image.py\n"
        with self.fake_log(log):
            touches, bulk = sr.git_touch_history("/repo", self.UNIVERSE)
        self.assertEqual(sorted(touches), [48])
        self.assertEqual(bulk, 0)

    def test_a_file_the_readme_does_not_claim_is_ignored(self):
        log = "\x01100\x01update ws\nleetcode_java/src/main/java/dev/Workspace18.java\n"
        with self.fake_log(log):
            touches, _ = sr.git_touch_history("/repo", self.UNIVERSE)
        self.assertEqual(touches, {})

    def test_a_bulk_commit_is_skipped(self):
        # 393 generated Java files in one commit made every one of those
        # problems look practised on the same day.
        universe = {"f%d.py" % i: {i} for i in range(20)}
        log = "\x01100\x01feat: add 20 solutions\n" + "".join(
            "f%d.py\n" % i for i in range(20))
        with self.fake_log(log):
            touches, bulk = sr.git_touch_history("/repo", universe)
        self.assertEqual(touches, {})
        self.assertEqual(bulk, 1)

    def test_the_bulk_limit_is_a_boundary_not_a_cliff(self):
        universe = {"f%d.py" % i: {i} for i in range(20)}
        log_of = lambda n: ("\x01100\x01batch\n"
                            + "".join("f%d.py\n" % i for i in range(n)))
        with self.fake_log(log_of(6)):
            self.assertEqual(len(sr.git_touch_history("/repo", universe)[0]), 6)
        with self.fake_log(log_of(7)):
            self.assertEqual(sr.git_touch_history("/repo", universe)[0], {})

    def test_a_hand_written_lc_number_survives_a_bulk_commit(self):
        universe = {"f%d.py" % i: {i} for i in range(20)}
        log = "\x01100\x01update 131 py plus a sweep\n" + "".join(
            "f%d.py\n" % i for i in range(20))
        with self.fake_log(log):
            touches, bulk = sr.git_touch_history("/repo", universe)
        self.assertEqual(sorted(touches), [131])
        self.assertEqual(bulk, 1)

    def test_touches_come_back_newest_first(self):
        log = ("\x01300\x01update 48 py\nleetcode_python/Array/rotate-image.py\n"
               "\x01100\x01update 48 py\nleetcode_python/Array/rotate-image.py\n")
        with self.fake_log(log):
            touches, _ = sr.git_touch_history("/repo", self.UNIVERSE)
        self.assertEqual(touches[48], [300, 300, 100, 100])

    def test_a_git_failure_degrades_to_the_practice_log(self):
        import subprocess
        from unittest import mock

        with mock.patch.object(subprocess, "run",
                               side_effect=OSError("git not found")):
            with mock.patch.object(sys, "stderr", open(os.devnull, "w")):
                self.assertEqual(sr.git_touch_history("/repo", self.UNIVERSE), ({}, 0))


# ── Scoring ─────────────────────────────────────────────────────────────────
def problem(**overrides):
    base = {
        "lc": 1, "title": "T", "url": "", "difficulty": "Medium",
        "section": "Array", "also_in": [], "note": "", "status": None,
        "passes": 0, "must": False, "tags": set(), "paths": [],
    }
    base.update(overrides)
    return base


class Importance(unittest.TestCase):
    def score(self, **kw):
        return sr.importance(problem(**kw), kw.pop("_lists", {}))

    def test_must_is_the_strongest_single_marker(self):
        must, _ = sr.importance(problem(must=True, difficulty=""), {})
        top100, _ = sr.importance(problem(difficulty=""), {1: {"top100liked"}})
        self.assertEqual(must, sr.W_MUST)
        self.assertGreater(must, top100)

    def test_the_neetcode_lists_nest_so_only_the_narrowest_scores(self):
        both, reasons = sr.importance(
            problem(difficulty=""), {1: {"blind75", "neetcode150", "neetcode250"}})
        self.assertEqual(both, sr.W_BLIND75)
        self.assertEqual(reasons, ["blind75"])

    def test_top100liked_stands_on_its_own(self):
        # It is LeetCode's list and cuts across all three NeetCode lists.
        score, reasons = sr.importance(
            problem(difficulty=""), {1: {"blind75", "top100liked"}})
        self.assertEqual(score, sr.W_BLIND75 + sr.W_TOP100)
        self.assertIn("top100liked", reasons)

    def test_a_readme_list_tag_counts_when_problem_lists_json_is_missing(self):
        score, _ = sr.importance(problem(difficulty="", tags={"neetcode150"}), {})
        self.assertEqual(score, sr.W_NEETCODE150)

    def test_company_tags_are_capped(self):
        score, _ = sr.importance(
            problem(difficulty="", tags=set(sr.COMPANIES)), {})
        self.assertEqual(score, sr.W_COMPANY_CAP)

    def test_google_scores_on_its_own(self):
        score, reasons = sr.importance(problem(difficulty="", tags={"google"}), {})
        self.assertEqual(score, sr.W_GOOGLE)
        self.assertIn("google", reasons)

    def test_review_passes_count_only_against_an_again_row(self):
        # The AGAIN marker never graduates in this repo, so the pass count is
        # read as difficulty rather than as progress — but an OK row's passes
        # really are progress and must not be scored as a gap.
        again, _ = sr.importance(
            problem(difficulty="", status="AGAIN", passes=5), {})
        ok, _ = sr.importance(problem(difficulty="", status="OK", passes=5), {})
        self.assertEqual(again, 5 * sr.W_PASS)
        self.assertEqual(ok, 0.0)

    def test_the_pass_bump_is_capped(self):
        score, _ = sr.importance(
            problem(difficulty="", status="AGAIN", passes=40), {})
        self.assertEqual(score, sr.W_PASS_CAP)

    def test_medium_outweighs_hard(self):
        # A coding round is made of mediums.
        medium, _ = sr.importance(problem(difficulty="Medium"), {})
        hard, _ = sr.importance(problem(difficulty="Hard"), {})
        easy, _ = sr.importance(problem(difficulty="Easy"), {})
        self.assertGreater(medium, hard)
        self.assertGreater(hard, easy)

    def test_an_unmarked_easy_problem_scores_nothing(self):
        self.assertEqual(sr.importance(problem(difficulty="Easy"), {})[0], 0.0)


class Staleness(unittest.TestCase):
    def test_touched_today_is_zero(self):
        self.assertEqual(sr.staleness(0, sr.DEFAULT_HALF_LIFE), 0.0)

    def test_one_half_life_is_half_stale(self):
        self.assertAlmostEqual(sr.staleness(21, 21), 0.5)

    def test_it_rises_with_days_and_is_bounded_by_one(self):
        self.assertLess(sr.staleness(7, 21), sr.staleness(60, 21))
        self.assertLess(sr.staleness(365, 21), 1.0)
        # Far enough out the remaining freshness underflows to zero, so the
        # curve saturates at exactly 1.0 rather than merely approaching it.
        self.assertEqual(sr.staleness(10000, 21), 1.0)

    def test_never_touched_saturates(self):
        self.assertGreater(sr.staleness(None, 21), 0.99)

    def test_a_future_timestamp_does_not_go_negative(self):
        self.assertEqual(sr.staleness(-5, 21), 0.0)


class CategoryBalance(unittest.TestCase):
    def rows(self, now):
        return [
            {"section": "Hot", "importance": 10.0, "last_ts": now,
             "_events": [now, now - sr.DAY]},
            {"section": "Cold", "importance": 10.0, "last_ts": now - 90 * sr.DAY,
             "_events": [now - 90 * sr.DAY]},
        ]

    def test_equal_importance_unequal_attention(self):
        now = 1_700_000_000.0
        cats = sr.category_balance(self.rows(now), now, window_days=30)
        self.assertAlmostEqual(cats["Hot"]["importance_share"], 0.5)
        self.assertAlmostEqual(cats["Cold"]["importance_share"], 0.5)
        # The cold category's only event is outside the window.
        self.assertEqual(cats["Cold"]["attention_share"], 0.0)
        self.assertEqual(cats["Hot"]["attention_share"], 1.0)

    def test_the_multiplier_rewards_neglect_and_penalises_pooling(self):
        now = 1_700_000_000.0
        cats = sr.category_balance(self.rows(now), now, window_days=30)
        self.assertEqual(cats["Cold"]["multiplier"], 1.0 + sr.BALANCE_GAIN)
        self.assertEqual(cats["Hot"]["multiplier"], sr.BALANCE_FLOOR)

    def test_the_multiplier_stays_within_its_bounds(self):
        now = 1_700_000_000.0
        rows = [{"section": "S%d" % i, "importance": float(i + 1),
                 "last_ts": now, "_events": [now] * (i * 7)} for i in range(6)]
        for cat in sr.category_balance(rows, now, 30).values():
            self.assertGreaterEqual(cat["multiplier"], sr.BALANCE_FLOOR)
            self.assertLessEqual(cat["multiplier"], 1.0 + sr.BALANCE_GAIN)

    def test_attention_is_recency_weighted_inside_the_window(self):
        now = 1_700_000_000.0
        rows = [
            {"section": "Yesterday", "importance": 1.0, "last_ts": now,
             "_events": [now - sr.DAY]},
            {"section": "ThreeWeeksAgo", "importance": 1.0, "last_ts": now,
             "_events": [now - 21 * sr.DAY]},
        ]
        cats = sr.category_balance(rows, now, window_days=30)
        self.assertGreater(cats["Yesterday"]["attention_share"],
                           cats["ThreeWeeksAgo"]["attention_share"])

    def test_a_quiet_repo_does_not_divide_by_zero(self):
        now = 1_700_000_000.0
        rows = [{"section": "A", "importance": 0.0, "last_ts": None, "_events": []}]
        cats = sr.category_balance(rows, now, 30)
        self.assertEqual(cats["A"]["ratio"], 0.0)


# ── Picking ─────────────────────────────────────────────────────────────────
def cat(section, deficit, n=3):
    return {"section": section, "importance_share": 0.5, "attention_share": 0.5,
            "ratio": 1.0, "deficit": deficit, "multiplier": 1.0,
            "last_ts": None, "n": n}


class Pick(unittest.TestCase):
    def setUp(self):
        self.rows = ([{"lc": i, "section": "Hot", "score": 100 - i} for i in range(4)]
                     + [{"lc": 10 + i, "section": "Cold", "score": 50 - i}
                        for i in range(3)])
        self.cats = {"Hot": cat("Hot", -0.4), "Cold": cat("Cold", 0.4)}

    def pick(self, **kw):
        kw.setdefault("min_score_frac", 0.0)
        kw.setdefault("balanced", True)
        kw.setdefault("per_category", 2)
        return sr.pick(self.rows, self.cats, **kw)

    def test_the_most_neglected_category_goes_first(self):
        picks = self.pick(top=4)
        self.assertEqual(picks[0]["section"], "Cold")

    def test_it_alternates_rather_than_draining_one_category(self):
        picks = self.pick(top=4)
        self.assertEqual([p["section"] for p in picks],
                         ["Cold", "Hot", "Cold", "Hot"])

    def test_within_a_category_the_best_score_goes_first(self):
        picks = self.pick(top=4)
        cold = [p["lc"] for p in picks if p["section"] == "Cold"]
        self.assertEqual(cold, sorted(cold))   # ids ascend as scores descend

    def test_no_category_exceeds_per_category_while_others_are_waiting(self):
        picks = self.pick(top=4, per_category=2)
        self.assertEqual(Counter(p["section"] for p in picks)["Hot"], 2)

    def test_the_cap_is_a_preference_not_a_quota(self):
        # Returning two problems for `--section X --top 5` reads as a bug.
        self.assertEqual(len(self.pick(top=6, per_category=2)), 6)

    def test_it_never_returns_more_than_asked(self):
        self.assertEqual(len(self.pick(top=3)), 3)

    def test_it_stops_when_the_pool_runs_out(self):
        self.assertEqual(len(self.pick(top=99)), len(self.rows))

    def test_a_weak_category_is_passed_over(self):
        # Breadth is the point, but not at the price of a slot spent on a
        # problem nothing recommends.
        picks = self.pick(top=3, min_score_frac=0.9)
        self.assertTrue(all(p["section"] == "Hot" for p in picks))

    def test_a_zero_score_is_never_picked(self):
        rows = [{"lc": 1, "section": "Hot", "score": 0.0}]
        self.assertEqual(sr.pick(rows, self.cats, 5, 2, True, 0.0), [])

    def test_no_balance_is_straight_score_order(self):
        picks = sr.pick(self.rows, self.cats, 3, 1, False)
        self.assertEqual([p["lc"] for p in picks], [0, 1, 2])

    def test_a_problem_is_never_picked_twice(self):
        picks = self.pick(top=99)
        self.assertEqual(len({id(p) for p in picks}), len(picks))


# ── Rendering ───────────────────────────────────────────────────────────────
class Formatting(unittest.TestCase):
    def test_fmt_days(self):
        self.assertEqual(sr.fmt_days(None), "never")
        self.assertEqual(sr.fmt_days(0.4), "today")
        self.assertEqual(sr.fmt_days(1.6), "2d")

    def test_bar_is_clamped_to_its_width(self):
        self.assertEqual(sr.bar(0.0, 4), "....")
        self.assertEqual(sr.bar(1.0, 4), "####")
        self.assertEqual(len(sr.bar(9.0, 4)), 4)
        self.assertEqual(len(sr.bar(-1.0, 4)), 4)

    def test_truncate_keeps_the_width(self):
        self.assertEqual(sr.truncate("abc", 5), "abc")
        self.assertEqual(len(sr.truncate("abcdefgh", 5)), 5)


# ── The real files ──────────────────────────────────────────────────────────
class LiveFiles(unittest.TestCase):
    """Held against the repo's own README and practice log, the way
    site/test/i18n.corpus.test.js is held against the real cheatsheets."""

    README = os.path.join(REPO, "README.md")
    PROGRESS = os.path.join(REPO, "data", "progress.txt")

    @classmethod
    def setUpClass(cls):
        cls.rows = sr.parse_readme(cls.README)

    def test_readme_still_parses_as_a_table_of_problems(self):
        self.assertGreater(len(self.rows), 1000)

    def test_every_row_has_a_section_and_a_difficulty(self):
        self.assertFalse([lc for lc, r in self.rows.items() if not r["section"]])
        missing = [lc for lc, r in self.rows.items() if not r["difficulty"]]
        self.assertLess(len(missing), 10, "unparsed difficulty in %s" % missing[:10])

    def test_must_agrees_with_extract_must_lc(self):
        # The one rule shared with another script. extract_must_lc.py owns it
        # and generates doc/must_lc_list.md from it, so the two readings must
        # agree exactly or one of the two docs is lying.
        import extract_must_lc

        theirs = {num for num, _, _, _, _ in extract_must_lc.parse(self.README)}
        mine = {lc for lc, r in self.rows.items() if r["must"]}
        self.assertEqual(mine, theirs)

    def test_the_sections_the_planner_excludes_still_exist(self):
        # A renamed section would silently stop being excluded, and SQL rows
        # would start competing for review slots.
        sections = {r["section"] for r in self.rows.values()}
        self.assertLessEqual(sr.EXCLUDED_SECTIONS, sections)

    def test_the_practice_log_still_yields_problems(self):
        dates, _, _ = sr.parse_progress(self.PROGRESS)
        self.assertGreater(len(dates), 400)

    def test_the_practice_log_parses_almost_cleanly(self):
        # One known bad date (20260229 — 2026 is not a leap year) is in the log.
        _, _, warnings = sr.parse_progress(self.PROGRESS)
        self.assertLessEqual(len(warnings), 2, warnings)

    def test_solution_paths_resolve_to_files_on_disk(self):
        # These paths are how a commit is attributed to a problem; a stale one
        # is a problem whose git history silently reads as empty.
        missing = [p for r in self.rows.values() for p in r["paths"]
                   if not os.path.exists(os.path.join(REPO, p))]
        self.assertLess(len(missing), len(self.rows) * 0.05,
                        "%d dead solution links, e.g. %s" % (len(missing), missing[:5]))

    def test_the_planner_runs_end_to_end(self):
        import io
        import contextlib

        out = io.StringIO()
        with contextlib.redirect_stdout(out):
            code = sr.main(["--top", "5"])
        self.assertEqual(code, 0)
        text = out.getvalue()
        self.assertIn("Balance: attention vs importance", text)
        self.assertIn("Suggested review", text)

    def test_an_impossible_filter_exits_nonzero_rather_than_printing_nothing(self):
        import io
        import contextlib

        err = io.StringIO()
        with contextlib.redirect_stderr(err), contextlib.redirect_stdout(io.StringIO()):
            code = sr.main(["--section", "No Such Section"])
        self.assertEqual(code, 1)
        self.assertIn("no problems match", err.getvalue())


if __name__ == "__main__":
    unittest.main(verbosity=2)
