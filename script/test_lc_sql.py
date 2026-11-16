#!/usr/bin/env python3
"""Tests for script/lc_sql.py — the lookup, backlog and smoke test /lc-sql runs."""
import io
import os
import sys
import tempfile
import unittest

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, HERE)
import lc_sql as s  # noqa: E402

FILE = """/*

1934. Confirmation Rate
Medium

Table: Signups

+----------------+----------+
| Column Name    | Type     |
+----------------+----------+
| user_id        | int      |
| code           | varchar  |
+----------------+----------+

Table: Confirmations

+----------------+----------+
| Column Name    | Type     |
+----------------+----------+
| user_id        | int      |
| action         | ENUM     |
+----------------+----------+

Example 1:

Input:
Signups table:
+---------+------+
| user_id | code |
+---------+------+
| 3       | 051  |
| 7       | 052  |
| 2       | null |
+---------+------+

Confirmations table:
+---------+-----------+
| user_id | action    |
+---------+-----------+
| 3       | timeout   |
| 7       | confirmed |
| 2       | confirmed |
| 2       | timeout   |
+---------+-----------+

Output:
+---------+-------------------+
| user_id | confirmation_rate |
+---------+-------------------+
| 3       | 0.00              |
| 7       | 1.00              |
| 2       | 0.50              |
+---------+-------------------+

*/

# V0
# IDEA : LEFT JOIN + AVG OVER A 0/1 FLAG
# time = O(n + m), space = O(n)
SELECT s.user_id,
       ROUND(IFNULL(SUM(c.action = 'confirmed') / COUNT(c.user_id), 0), 2) AS confirmation_rate
FROM Signups s
LEFT JOIN Confirmations c ON s.user_id = c.user_id
GROUP BY s.user_id;

# V1
# IDEA : WRONG ON PURPOSE
SELECT user_id, 0 AS confirmation_rate FROM Signups;
"""


def write(text):
    fd, path = tempfile.mkstemp(suffix=".sql")
    with os.fdopen(fd, "w") as f:
        f.write(text)
    return path


class Parse(unittest.TestCase):
    def test_reads_inputs_result_and_schemas(self):
        tables, expected, schemas = s.parse_example(s.header_block(FILE))
        self.assertEqual(list(tables), ["Signups", "Confirmations"])
        self.assertEqual(expected[0], ["user_id", "confirmation_rate"])
        self.assertEqual(len(expected[1]), 3)
        self.assertEqual(schemas["signups"]["code"], "varchar")

    def test_schema_grids_are_not_loaded_as_tables(self):
        tables, _, _ = s.parse_example(s.header_block(FILE))
        for header, _rows in tables.values():
            self.assertNotIn("Column Name", header)

    def test_versions_in_file_order_without_comments(self):
        vs = s.versions(FILE)
        self.assertEqual([m for m, _ in vs], ["V0", "V1"])
        self.assertNotIn("IDEA", vs[0][1])

    def test_varchar_keeps_leading_zero(self):
        self.assertEqual(s.cell_value("051", "varchar"), "051")
        self.assertEqual(s.cell_value("51", "int"), 51)
        self.assertIsNone(s.cell_value("null"))


class Dialect(unittest.TestCase):
    def test_division_is_decimal_like_mysql(self):
        self.assertEqual(s.mysql_division("a / b"), "a  * 1.0 / b")
        self.assertEqual(s.mysql_division("'2020/01'"), "'2020/01'")

    def test_mysql_functions(self):
        conn = s.connect({}, {})
        row = conn.execute("SELECT IF(1 > 0, 'y', 'n'), DATEDIFF('2020-01-03', '2020-01-01'), "
                           "YEAR('2020-05-01'), DATE_FORMAT('2020-05-01', '%Y-%m'), "
                           "LEFT('abc', 2), CONCAT('a', 1)").fetchone()
        self.assertEqual(row, ("y", 2, 2020, "2020-05", "ab", "a1"))


class Run(unittest.TestCase):
    def test_pass_and_fail_are_told_apart(self):
        path = write(FILE)
        try:
            out = io.StringIO()
            self.assertEqual(s.run(path, out=out), 0, out.getvalue())
            out = io.StringIO()
            self.assertEqual(s.run(path, every=True, out=out), 1)
            self.assertIn("PASS V0", out.getvalue())
            self.assertIn("FAIL V1", out.getvalue())
        finally:
            os.unlink(path)

    def test_trailing_comment_after_semicolon_is_not_a_statement(self):
        path = write(FILE.replace("GROUP BY s.user_id;", "GROUP BY s.user_id;   -- note"))
        try:
            out = io.StringIO()
            self.assertEqual(s.run(path, out=out), 0, out.getvalue())
        finally:
            os.unlink(path)

    def test_no_example_is_an_error_not_a_pass(self):
        path = write("# V0\nSELECT 1;\n")
        try:
            self.assertEqual(s.run(path, out=io.StringIO()), 2)
        finally:
            os.unlink(path)

    def test_sqlite_gap_is_a_skip(self):
        path = write(FILE.replace("ROUND(", "GREATEST_UNKNOWN_FN("))
        try:
            out = io.StringIO()
            self.assertEqual(s.run(path, out=out), 0)
            self.assertIn("SKIP V0", out.getvalue())
        finally:
            os.unlink(path)

    def test_real_files_still_pass(self):
        # a few committed files with a full example; a parser change that stops
        # reading them fails here rather than in the middle of /lc-sql
        for slug in ("recyclable-and-low-fat-products", "confirmation-rate",
                     "find-the-subtasks-that-did-not-execute",
                     "countries-you-can-safely-invest-in", "find-customer-referee"):
            out = io.StringIO()
            path = os.path.join(ROOT, "leetcode_SQL", slug + ".sql")
            self.assertEqual(s.run(path, every=True, out=out), 0, slug + "\n" + out.getvalue())


class Lookup(unittest.TestCase):
    def test_audit_list_is_read(self):
        probs = s.sql_problems()
        self.assertGreater(len(probs), 100)        # a glob that stops matching fails here
        self.assertEqual(probs[570]["slug"], "managers-with-at-least-5-direct-reports")
        for rec in probs.values():
            self.assertTrue(rec["skip_reason"].startswith("SQL"))

    def test_readme_rows_come_from_the_sql_table_only(self):
        rows = s.readme_sql_rows(s.read_readme())
        self.assertIn(175, rows)                   # Combine Two Tables
        self.assertNotIn(1, rows)                  # Two Sum is not a SQL row
        for row in rows.values():
            self.assertIn("leetcode_SQL/", row)

    def test_lookup_known_and_unknown(self):
        out = io.StringIO()
        self.assertEqual(s.lookup(570, out=out), 0)
        self.assertIn("leetcode_SQL/managers-with-at-least-5-direct-reports.sql", out.getvalue())
        out = io.StringIO()
        self.assertEqual(s.lookup(1, out=out), 1)
        self.assertIn("never from the title", out.getvalue())

    def test_todo_excludes_filed_rows(self):
        out = io.StringIO()
        s.todo(out=out)
        listed = {int(l.split()[0]) for l in out.getvalue().splitlines()[1:]}
        self.assertFalse(listed & set(s.readme_sql_rows(s.read_readme())))


if __name__ == "__main__":
    unittest.main()
