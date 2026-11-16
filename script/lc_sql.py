#!/usr/bin/env python3
"""The helper /lc-sql runs: find a SQL problem's real slug, list the ones not
filed yet, and smoke-test a leetcode_SQL/<slug>.sql file against its example.

    python3 script/lc_sql.py lookup 1934          # number -> slug, title, difficulty, 🔒
    python3 script/lc_sql.py todo                 # SQL problems README's ## SQL table lacks
    python3 script/lc_sql.py smoke leetcode_SQL/confirmation-rate.sql
    python3 script/lc_sql.py smoke leetcode_SQL/<slug>.sql --all       # every version
    python3 script/lc_sql.py smoke leetcode_SQL/<slug>.sql --ordered   # row order matters

lookup / todo are offline. They read the SQL problems the kamyu104 coverage
audit set aside (doc/lc-coverage-kamyu/skipped_*.json, skip_reason "SQL
problem"), which carry the canonical leetcode.com slug, title, difficulty and
premium flag, and the ## SQL table in README.md. A number the audit does not
know is reported as unknown, never guessed: the slug then comes from the
problem URL.

smoke: every file under leetcode_SQL/ opens with a /* ... */ header that copies
the problem's example: the input tables and the expected result, drawn as
ASCII grids. This reads those grids, loads the inputs into an in-memory SQLite
database, runs the file's `# V0` query (or every version with --all), and
compares what comes back with the expected result. It is the SQL counterpart
of the `python3 -c` smoke test /lc-python runs: an untested query is
unfinished.

LeetCode grades SQL on MySQL; this runs SQLite, because nothing else is
guaranteed to be installed. The common MySQL functions (IF, DATEDIFF, YEAR,
DATE_FORMAT, CONCAT, ...) are registered as SQLite functions, and anything
SQLite still cannot run is reported as SKIP with the reason rather than as a
failure. `/` is rewritten to MySQL's decimal division (SQLite truncates
int / int), and `@var :=` session variables, which SQLite does not have, are
warned about.

smoke exits 0 when every version run matched (or was skipped), 1 when one
returned a different result, 2 when the file's example could not be read.
lookup exits 1 on a number neither the audit nor README knows.
"""
import argparse
import datetime
import glob
import json
import os
import re
import sqlite3
import sys

GRID_EDGE = re.compile(r"^\s*\+[-+]+\+\s*$")
GRID_ROW = re.compile(r"^\s*\|.*\|\s*$")
VERSION = re.compile(r"^#\s*(V\d+(?:-\d+)?)\b", re.M)
RESULT_LABEL = re.compile(r"^(result( table)?|output|expected( output)?)\s*:?$", re.I)
SCHEMA_LABEL = re.compile(r"^table\s*:\s*(\w+)", re.I)
INPUT_LABEL = re.compile(r"^(?:input\s*:\s*)?(\w+)(?:\s+table)?\s*:?$", re.I)


# ── reading the header ──────────────────────────────────────────────────────

def header_block(text):
    """The /* ... */ comment the file opens with — the problem statement."""
    m = re.search(r"/\*(.*?)\*/", text, re.S)
    return m.group(1) if m else ""


def grids(block):
    """Yield (label, header, rows) for every ASCII grid, labelled by the nearest
    non-blank line above it."""
    lines = block.splitlines()
    i = 0
    while i < len(lines):
        if not GRID_EDGE.match(lines[i]):
            i += 1
            continue
        # the label is the closest non-blank line above the grid
        j = i - 1
        while j >= 0 and not lines[j].strip():
            j -= 1
        label = lines[j].strip() if j >= 0 else ""
        # a grid is: edge, header row, edge, data rows..., edge
        body = []
        i += 1
        while i < len(lines) and (GRID_EDGE.match(lines[i]) or GRID_ROW.match(lines[i])):
            if GRID_ROW.match(lines[i]):
                body.append([cell.strip() for cell in lines[i].strip().strip("|").split("|")])
            i += 1
        if body:
            yield label, body[0], body[1:]


def parse_example(block):
    """Split the header's grids into input tables and the expected result.

    Returns (tables, expected, schemas): tables maps name -> (header, rows),
    expected is (header, rows) or None, and schemas maps a lower-cased table
    name -> {lower-cased column: declared type}, read from the
    "Column Name | Type" grids that describe each table."""
    tables, expected, schemas = {}, None, {}
    for label, header, rows in grids(block):
        if [h.lower() for h in header[:2]] == ["column name", "type"]:
            m = SCHEMA_LABEL.match(label)
            if m:
                schemas[m.group(1).lower()] = {r[0].lower(): r[1].lower()
                                               for r in rows if len(r) >= 2}
            continue
        if RESULT_LABEL.match(label):
            if expected is None:          # only the first example is used
                expected = (header, rows)
            continue
        m = INPUT_LABEL.match(label)
        # only the first example's inputs are loaded
        if m and expected is None and m.group(1).lower() not in tables_lower(tables):
            tables[m.group(1)] = (header, rows)
    return tables, expected, schemas


def tables_lower(tables):
    return {name.lower() for name in tables}


TEXT_TYPES = re.compile(r"char|text|enum|date|time")


def cell_value(text, declared=""):
    """A grid cell as a Python value: null -> None, numbers -> int/float.

    A column declared varchar / enum / date keeps its text, so a country code
    `051` is not loaded as the number 51."""
    if text.lower() in ("null", ""):
        return None
    if TEXT_TYPES.search(declared) or re.fullmatch(r"-?0\d+", text):
        return text
    if re.fullmatch(r"-?\d+", text):
        return int(text)
    if re.fullmatch(r"-?\d+\.\d+", text):
        return float(text)
    return text


# ── the queries ─────────────────────────────────────────────────────────────

def versions(text):
    """[(marker, sql)] for every `# V..` block after the header, in file order."""
    end = text.find("*/")
    body = text[end + 2:] if end != -1 else text
    marks = list(VERSION.finditer(body))
    out = []
    for k, m in enumerate(marks):
        stop = marks[k + 1].start() if k + 1 < len(marks) else len(body)
        chunk = body[m.end():stop]
        # MySQL's `#` comments are not SQLite comments; drop them
        sql = "\n".join(line for line in chunk.splitlines()
                        if not line.lstrip().startswith("#"))
        sql = re.sub(r"/\*.*?\*/", "", sql, flags=re.S)   # block comments too
        if sql.strip():
            out.append((m.group(1), sql.strip()))
    return out


def warnings_for(sql):
    found = []
    if re.search(r"@\w+\s*:=", sql):
        found.append("uses @var := session variables, which SQLite does not have")
    return found


def mysql_division(sql):
    """Rewrite `a / b` as `a * 1.0 / b`, outside string literals.

    MySQL's `/` always returns a decimal (1 / 2 = 0.5000); SQLite's truncates
    when both sides are integers (1 / 2 = 0). `* 1.0` shares `/`'s precedence
    and associates left, so `x - a / b * c` keeps its meaning."""
    out, quote = [], None
    for ch in sql:
        if quote:
            if ch == quote:
                quote = None
        elif ch in "'\"`":
            quote = ch
        elif ch == "/":
            out.append(" * 1.0 ")
        out.append(ch)
    return "".join(out)


def split_statements(sql):
    """Split on `;` outside string literals, dropping `-- ` line comments.

    A `;` inside 'a;b' or inside a trailing `-- note; more` is not a statement
    break, and a fragment that is only a comment is not a statement."""
    statements, current, quote, i = [], [], None, 0
    while i < len(sql):
        ch = sql[i]
        if quote:
            current.append(ch)
            if ch == quote:
                quote = None
        elif ch in "'\"`":
            quote = ch
            current.append(ch)
        elif sql.startswith("--", i):
            # skip to the end of the line; keep the newline
            end = sql.find("\n", i)
            i = len(sql) if end == -1 else end
            continue
        elif ch == ";":
            statements.append("".join(current))
            current = []
        else:
            current.append(ch)
        i += 1
    statements.append("".join(current))
    return [st.strip() for st in statements if st.strip()]


# MySQL functions SQLite lacks, close enough for an example-sized smoke test.
def _date(v):
    return datetime.date.fromisoformat(str(v)[:10]) if v is not None else None


MYSQL_FORMAT = {"%Y": "%Y", "%y": "%y", "%m": "%m", "%c": "%-m", "%d": "%d",
                "%e": "%-d", "%M": "%B", "%b": "%b", "%W": "%A", "%a": "%a",
                "%H": "%H", "%i": "%M", "%s": "%S", "%T": "%H:%M:%S"}


def _date_format(v, fmt):
    if v is None:
        return None
    d = datetime.datetime.fromisoformat(str(v))
    out = re.sub(r"%[a-zA-Z]", lambda m: d.strftime(MYSQL_FORMAT.get(m.group(0), m.group(0))), fmt)
    return out


MYSQL_FUNCS = {
    ("IF", 3): lambda c, a, b: a if c else b,
    ("DATEDIFF", 2): lambda a, b: None if a is None or b is None else (_date(a) - _date(b)).days,
    ("YEAR", 1): lambda v: _date(v).year if v is not None else None,
    ("MONTH", 1): lambda v: _date(v).month if v is not None else None,
    ("DAY", 1): lambda v: _date(v).day if v is not None else None,
    ("DATE_FORMAT", 2): _date_format,
    ("LEFT", 2): lambda s, n: None if s is None else str(s)[:n],
    ("RIGHT", 2): lambda s, n: None if s is None else (str(s)[-n:] if n else ""),
    ("CONCAT", -1): lambda *a: None if any(x is None for x in a) else "".join(str(x) for x in a),
    ("POWER", 2): lambda a, b: None if a is None or b is None else a ** b,
    ("SQRT", 1): lambda a: None if a is None else a ** 0.5,
}


def sqlite_type(declared):
    """The SQLite affinity for a MySQL column type; '' when the schema is silent."""
    if re.search(r"int|bool|bit", declared):
        return "INTEGER"
    if re.search(r"decimal|numeric", declared):
        # NUMERIC, not REAL: decimal(20,0) holds integers past 2**53 exactly
        return "NUMERIC"
    if re.search(r"float|double|real", declared):
        return "REAL"
    if TEXT_TYPES.search(declared):
        return "TEXT"
    return ""


def connect(tables, schemas):
    conn = sqlite3.connect(":memory:")
    for (name, nargs), fn in MYSQL_FUNCS.items():
        conn.create_function(name, nargs, fn)
    for name, (header, rows) in tables.items():
        types = schemas.get(name.lower(), {})
        declared = [types.get(h.lower(), "") for h in header]
        # declared types give SQLite the affinity MySQL's coercion implies, so
        # `id = '2'` matches an INT column as it does on LeetCode
        cols = ", ".join('"%s" %s' % (h, sqlite_type(t)) for h, t in zip(header, declared))
        conn.execute('CREATE TABLE "%s" (%s)' % (name, cols))
        marks = ", ".join("?" for _ in header)
        conn.executemany('INSERT INTO "%s" VALUES (%s)' % (name, marks),
                         [[cell_value(c, t) for c, t in zip(r, declared)] for r in rows])
    return conn


# ── comparing ───────────────────────────────────────────────────────────────

def norm(v, from_grid=False):
    """Compare numbers by value (0.5 == "0.50"), text exactly, NULL as NULL.

    Only a cell of the expected grid can spell NULL as `null`: a query that
    returns the *string* 'null' or '' has not returned NULL, so for the actual
    rows only SQLite's own None is NULL. Integers stay exact; only a
    fractional value is rounded, to absorb ROUND(…, 2) printing."""
    if from_grid:
        v = cell_value(v)
    elif isinstance(v, str) and re.fullmatch(r"-?\d+(\.\d+)?", v) and not re.fullmatch(r"-?0\d+", v):
        v = cell_value(v)          # a numeric string compares by value, like the grid's
    if v is None:
        return ("null",)
    if isinstance(v, float) and v.is_integer():
        v = int(v)
    if isinstance(v, int):
        return ("num", v)
    if isinstance(v, float):
        return ("num", round(v, 5))
    return ("str", str(v))


def compare(got_cols, got_rows, exp_cols, exp_rows, ordered):
    if [c.lower() for c in got_cols] != [c.lower() for c in exp_cols]:
        return "columns %s, expected %s" % (got_cols, exp_cols)
    got = [tuple(norm(v) for v in r) for r in got_rows]
    exp = [tuple(norm(v, from_grid=True) for v in r) for r in exp_rows]
    if not ordered:
        got, exp = sorted(got), sorted(exp)
    if got != exp:
        return "rows %s, expected %s" % (got_rows, [[cell_value(c) for c in r] for r in exp_rows])
    return None


def run(path, every=False, ordered=False, out=sys.stdout):
    with open(path, encoding="utf-8") as f:
        text = f.read()
    tables, expected, schemas = parse_example(header_block(text))
    if not tables or expected is None:
        print("ERROR %s: no example to test — the header needs the input tables "
              "and a 'Result table:' / 'Output:' grid" % path, file=out)
        return 2
    blocks = versions(text)
    if not blocks:
        print("ERROR %s: no `# V0` block after the header" % path, file=out)
        return 2
    if not every:
        blocks = blocks[:1]
    print("tables: %s; expected %d row(s)" % (", ".join(tables), len(expected[1])), file=out)
    status = 0
    for marker, sql in blocks:
        for w in warnings_for(sql):
            print("  warn %s: %s" % (marker, w), file=out)
        conn = connect(tables, schemas)
        try:
            cur = conn.cursor()
            # a version may hold several statements (an UPDATE, then a SELECT);
            # the last one that returns rows is the answer. A trailing
            # `-- note` after the final `;` is not a statement.
            cols, rows = [], []
            for stmt in split_statements(sql):
                cur.execute(mysql_division(stmt))
                if cur.description:
                    cols = [d[0] for d in cur.description]
                    rows = cur.fetchall()
        except sqlite3.Error as e:
            print("SKIP %s: SQLite cannot run it (%s) — verify on LeetCode" % (marker, e), file=out)
            continue
        finally:
            conn.close()
        problem = compare(cols, rows, expected[0], expected[1], ordered)
        if problem:
            print("FAIL %s: %s" % (marker, problem), file=out)
            status = 1
        else:
            print("PASS %s: %d row(s) match" % (marker, len(rows)), file=out)
    return status


# ── lookup / todo ───────────────────────────────────────────────────────────

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
AUDIT_GLOB = os.path.join(ROOT, "doc", "lc-coverage-kamyu", "skipped_*.json")


def sql_problems(pattern=AUDIT_GLOB):
    """{number: record} for every SQL problem the coverage audit set aside."""
    out = {}
    for path in sorted(glob.glob(pattern)):
        with open(path, encoding="utf-8") as f:
            for rec in json.load(f):
                if rec.get("skip_reason", "").startswith("SQL"):
                    out[rec["num"]] = rec
    return out


def readme_sql_rows(readme_text):
    """{number: row} for the rows under README's main `## SQL` heading."""
    m = re.search(r"^## SQL\s*$(.*?)^## ", readme_text, re.M | re.S)
    rows = {}
    for line in (m.group(1) if m else "").splitlines():
        n = re.match(r"^\|\s*(\d+)\s*\|", line)
        if n:
            rows[int(n.group(1))] = line
    return rows


def read_readme():
    with open(os.path.join(ROOT, "README.md"), encoding="utf-8") as f:
        return f.read()


def lookup(number, out=sys.stdout):
    rec = sql_problems().get(number)
    row = readme_sql_rows(read_readme()).get(number)
    if rec is None and row:
        # filed already: README's link is the slug the file was named for
        slug = re.search(r"leetcode\.com/problems/([^/)]+)", row)
        print("%d: filed; slug in README's link: %s" % (number, slug.group(1) if slug else "?"),
              file=out)
    elif rec is None:
        print("%d: not in the audit's SQL list — take the slug from the problem URL, "
              "never from the title" % number, file=out)
    else:
        print("%d. %s\nslug: %s\ndifficulty: %s\npremium: %s"
              % (number, rec["title"], rec["slug"], rec["difficulty"],
                 "yes (🔒)" if rec["premium"] else "no"), file=out)
        if not row:
            print("file: leetcode_SQL/%s.sql" % rec["slug"], file=out)
    if row:
        # the file README links is the one to edit, whatever the audit's slug says
        filed = re.search(r"\(\./(leetcode_SQL/[^)]+)\)", row)
        print("README: already filed -> %s (edit that file; do not add a second row)"
              % (filed.group(1) if filed else row[:120]), file=out)
    else:
        print("README: no row under ## SQL yet", file=out)
    return 0 if rec is not None or row else 1


def todo(limit=0, out=sys.stdout):
    have = readme_sql_rows(read_readme())
    missing = [r for n, r in sorted(sql_problems().items()) if n not in have]
    print("%d SQL problems in the audit, %d not under ## SQL" % (len(sql_problems()), len(missing)),
          file=out)
    for rec in missing[:limit or None]:
        print("%5d  %-6s %s  %s" % (rec["num"], rec["difficulty"],
                                    "🔒" if rec["premium"] else "  ", rec["slug"]), file=out)
    return 0


def main(argv=None):
    p = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    sub = p.add_subparsers(dest="cmd", required=True)
    lk = sub.add_parser("lookup", help="number -> slug, title, difficulty, premium")
    lk.add_argument("number", type=int)
    td = sub.add_parser("todo", help="SQL problems README's ## SQL table lacks")
    td.add_argument("--limit", type=int, default=0, help="print only the first N")
    sm = sub.add_parser("smoke", help="run a file's queries against its own example")
    sm.add_argument("path", help="a leetcode_SQL/<slug>.sql file")
    sm.add_argument("--all", action="store_true", help="run every # V.. block, not only V0")
    sm.add_argument("--ordered", action="store_true",
                    help="compare row order too (the problem asks for an ORDER BY)")
    a = p.parse_args(argv)
    if a.cmd == "lookup":
        return lookup(a.number)
    if a.cmd == "todo":
        return todo(a.limit)
    return run(a.path, every=a.all, ordered=a.ordered)


if __name__ == "__main__":
    sys.exit(main())
