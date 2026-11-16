---
name: lc-sql
description: File a LeetCode **SQL** (Database) solution into this repo the way the existing ones are filed — find the problem's real slug offline, write `leetcode_SQL/<slug>.sql` in the house layout (a /* */ header holding the schema and the example's input and result tables, then V0 with an IDEA block and a time/space line; the standard, readable MySQL query with each clause on its own line, CTEs over nested subqueries, and the NULL handling commented; every other good approach filed as V1, V2, …), smoke-test it against the header's own example with script/lc_sql.py, and insert the row into README's `## SQL` table. Use when asked to "add LC <number>" for a database problem, to file a SQL query just solved, to turn a pasted MySQL draft into a committed solution, or to work through the SQL problems not filed yet. Triggers - "/lc-sql 584", "add LC 1934 sql", "file this MySQL solution", "which SQL problems are missing?".
allowed-tools: Read, Glob, Grep, Bash, Write, Edit
---

# Add a LeetCode SQL solution

Turn an LC number (plus, usually, a query the user already wrote) into a committed-quality
MySQL solution file **and** its README row, in the shape of the 166 files under
`leetcode_SQL/` and the 165-row `## SQL` table in README.

**Invocation**: `/lc-sql <LC number>` — e.g. `/lc-sql 584`. A query pasted under the
command is used as `V0`. `/lc-sql todo` lists the SQL problems not filed yet (step 1).

There is no pattern dir to pick: every SQL solution lives flat in `leetcode_SQL/`, and every
row goes in the one `## SQL` table. Ask only if the number itself is missing.

**Wrong skill?** This one owns `leetcode_SQL/` and the `## SQL` table only. A **Pandas**
solution to a database problem is Python and goes to
[`/lc-python`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-python/SKILL.md);
an algorithm problem in Java goes to
[`/lc-java`](https://github.com/yennanliu/CS_basics/blob/master/.claude/skills/lc-java/SKILL.md).
If the user pasted a Pandas `DataFrame` function, say so and switch rather than translating
it to SQL.

## Prime directives

1. **The slug comes from a record, never from the title.** LC 580 is
   `count-student-number-in-departments`; the file here is `count-student-numbers-in-departments.sql`,
   and LC 1988's README link reads `find-cutoff-score-for-each-schooly`. A guessed slug is a
   wrong file name and a link that 404s. `script/lc_sql.py lookup` reads the canonical one
   offline (step 1).
2. **The header carries the example, or the file cannot be tested.** 48 of the 166 files
   have no example tables in their `/* */` header, so nothing can check them and nobody has.
   A new file copies the schema, the example's input tables **and** its result table as
   ASCII grids — they are what step 5 runs the query against.
3. **The user's query is the solution.** When a draft is pasted, fix its bugs and keep its
   idea as `V0`. Keeping the *idea* does not mean keeping the *spelling*: a one-line query is
   reformatted to the readable form of directive 6, with the same logic underneath.
4. **Untested is unfinished.** Run `script/lc_sql.py smoke` on the file before reporting
   done. A query that only "looks right" is how `WHERE referee_id != 2` ships — it silently
   drops every `NULL` row.
5. **LeetCode grades MySQL 8; the smoke test runs SQLite.** The helper bridges the common
   gap (decimal `/`, `IF`, `DATEDIFF`, `YEAR`, `DATE_FORMAT`, `CONCAT`, `LEFT`), and reports
   anything else SQLite cannot run as `SKIP`, not `PASS`. A `SKIP` is reported to the user as
   *not run locally*, never as tested.
6. **Readable beats clever.** These files are read back months later by someone who has to
   *explain* the query. Every block is the **standard approach** for the problem — a join,
   a `GROUP BY` / `HAVING`, a window function, a CTE — written one clause per line, with the
   NULL behaviour and every non-obvious step commented (step 3 lists the rules).
7. **Every good approach is worth filing.** A self-join *and* a window function, `NOT IN`
   *and* `LEFT JOIN … IS NULL`, a correlated subquery *and* a `GROUP BY` — each that runs
   in LC's limits and is worth knowing is its own version (step 4).
8. **One table, in order.** README has exactly one `## SQL` table (the imported
   `## Newly Added (kamyu104 gap)` set has none — the audit skipped SQL), sorted by LC number.
   A row goes in that table at its numeric position, never appended at the end.

## The steps

### 1. Find the problem's record

```bash
python3 script/lc_sql.py lookup 570            # -> slug, title, difficulty, 🔒, file path, README status
python3 script/lc_sql.py todo --limit 20       # the SQL problems README's ## SQL table lacks
```

`lookup` reads the kamyu104 coverage audit's SQL list (`doc/lc-coverage-kamyu/skipped_*.json`,
156 problems not filed yet) and README's `## SQL` table, both offline. Three outcomes:

- **In the audit** — the slug, title, difficulty and premium flag printed are the
  canonical ones. Use them as given.
- **Already filed** — README has the row; the job is improving the existing file, not adding
  a second one. Read it before touching it.
- **Unknown** — a problem newer than the audit. The slug comes from the problem URL the user
  gave, or the problem page; ask for the URL rather than deriving it from the title.

`todo` is the backlog. With no number given, offer its first few, Easy before Medium.

### 2. Read a neighbour before writing

```bash
ls leetcode_SQL/ | head -20
python3 script/lc_sql.py smoke leetcode_SQL/confirmation-rate.sql
```

Open a file whose header has example grids (`confirmation-rate.sql`,
`find-the-subtasks-that-did-not-execute.sql`) and match its header. The older files open
with `<Title> Problem` / `LeetCode Problem <n>.` and copy V1/V2 from blogs with their URL —
that is history, not the shape to copy. The layout below is.

### 3. Write `leetcode_SQL/<slug>.sql`

```sql
/*

584. Find Customer Referee
Easy

Table: Customer

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
| referee_id  | int     |
+-------------+---------+
id is the primary key column for this table.
Each row indicates the id of a customer, their name, and the id of the customer who referred them.

Find the names of the customer that are not referred by the customer with id = 2.

Return the result table in any order.

Example 1:

Input:
Customer table:
+----+------+------------+
| id | name | referee_id |
+----+------+------------+
| 1  | Will | null       |
| 3  | Alex | 2          |
+----+------+------------+
(trimmed here to two rows; the file carries all six)

Output:
+------+
| name |
+------+
| Will |
+------+

*/

# V0
# IDEA : FILTER WITH AN EXPLICIT NULL BRANCH
#
#   referee_id != 2 is NULL (not TRUE) when referee_id is NULL, and WHERE keeps
#   only TRUE rows -- so a customer nobody referred would be dropped. The
#   IS NULL branch keeps them.
#
# time = O(n), space = O(n)    -- n = rows in Customer; one scan, output up to n rows
SELECT name
FROM Customer
WHERE referee_id != 2
   OR referee_id IS NULL;   -- NOTE !!! without this line every NULL row is lost
```

Rules that are not negotiable:

- The `/* */` header is **first**: `<number>. <Exact Title>`, the difficulty, every table's
  `Table: <Name>` + `Column Name | Type` grid with the key sentences under it, the task, then
  `Example 1:` with `<Name> table:` grids for the inputs and an `Output:` grid. Copy the
  grids exactly — `null` stays `null`, every column the result has stays.
- Leave out leetcode.com page furniture (`Solved`, `Topics`, `Companies`, `Hint`, the
  premium lock, `SQL Schema` / `Pandas Schema` tabs). Premium is recorded in README's 🔒.
- `# V0` is the canonical query; `# IDEA : ...` in caps, the indented *why*, then
  `# time = O(...), space = O(...)` naming what `n` (and `m`) count. Comment lines start
  with `#`, like the markers; `--` is for a short note at the end of a line.
- Each version ends with `;`.

**Readability rules** (directive 6) — they apply to every version, `V0` included:

- **One clause per line**, keywords in capitals: `SELECT` / `FROM` / `JOIN … ON` / `WHERE` /
  `GROUP BY` / `HAVING` / `ORDER BY`, a continuation indented under its clause.
- **Explicit `JOIN … ON`**, never a comma join with the condition in `WHERE`. Say `LEFT JOIN`
  when unmatched rows must survive, and comment why.
- **A CTE per logical step** (`WITH dept_max AS (…)`) instead of subqueries nested three deep.
  The CTE name says what rows it holds.
- **Aliases that read.** A table alias is its initial (`e`, `d`) or, in a self-join, its role
  (`emp`, `mgr`) — never `t1` / `t2` / `a` / `b`. Every derived column gets `AS`, spelled
  exactly as the expected output's column, because LeetCode compares column names.
- **No sugar that hides the logic**: `SUM(CASE WHEN action = 'confirmed' THEN 1 ELSE 0 END)`
  rather than `SUM(action = 'confirmed')`; window functions (`RANK() OVER (…)`, `LAG`) rather
  than `@var :=` session variables; named columns rather than `GROUP BY 1` / `ORDER BY 2`;
  no `SELECT *` in an answer, no `ORDER BY NULL`.
- **Say what happens to NULL** wherever a comparison can meet one: `!=` and `=` drop NULL
  rows; `NOT IN (subquery)` returns nothing if the subquery yields a NULL; `COUNT(col)` skips
  NULLs and `COUNT(*)` does not; a `WHERE` on the right table of a `LEFT JOIN` turns it into
  an inner join. A comment is required on the line that relies on one of these.
- **Ties and order are deliberate.** `RANK` vs `DENSE_RANK` vs `ROW_NUMBER` is a choice — say
  which tie behaviour the problem asks for. Add `ORDER BY` only when the problem asks for an
  order, and then run step 5 with `--ordered`.

### 4. File every good approach as its own version

| Marker | What it holds |
|---|---|
| `# V0` | The canonical query — the user's own idea when a draft was given, otherwise the most standard, easiest-to-explain one. |
| `# V0-1` | A variation on `V0`'s idea — same technique, a cleaner or cheaper form (a CTE instead of a repeated subquery). |
| `# V1`, `# V2`, … | A **different approach** — a window function after a self-join, `NOT EXISTS` after `LEFT JOIN … IS NULL`, `GROUP BY` + `HAVING` after a correlated subquery. Most standard first. |

- Each version has its own `IDEA`, *why* and `time = …, space = …` line.
- A rewrite with the same plan — `IFNULL` for `COALESCE`, `<>` for `!=` — is not a version.
  Most SQL problems end up with one to three.
- A version that needs MySQL-only syntax is fine; step 5 will report it as `SKIP`, and the
  report says so.

### 5. Smoke-test against the header's example

```bash
python3 script/lc_sql.py smoke leetcode_SQL/<slug>.sql --all
python3 script/lc_sql.py smoke leetcode_SQL/<slug>.sql --all --ordered   # only when the problem asks for an order
```

It loads the header's input grids into SQLite, runs every version, and compares each result
with the `Output:` grid — column names, and rows as a multiset (numbers by value, so `0.5`
matches `0.50`). Every version must print `PASS` or `SKIP`:

- `FAIL` — the query is wrong, or the grids were copied wrong. Fix whichever it is; never edit
  the expected grid to match the query.
- `SKIP` — SQLite cannot run it. Say *not run locally* in the report; if `V0` itself is a
  `SKIP`, see whether a portable spelling exists before accepting it.
- `ERROR … no example to test` — the header is missing its grids (directive 2). Add them.

Then add a second case by hand when the example misses an edge the problem allows — an
all-NULL column, a tie, an empty match — by copying the file to the scratchpad, editing its
grids, and running `smoke` on the copy. Do not commit the copy.

### 6. Add the README row

There is one `## SQL` table; find its range and the rows either side of the new number:

```bash
grep -n "^## SQL\|^## Shell Script" README.md        # the table runs between these two lines
awk '/^## SQL/{s=1;next} /^## /{s=0} s && /^\| [0-9]/{print NR": "substr($0,1,60)}' README.md | tail -3
```

Insert at the row's numeric position, zero-padded to four digits like its neighbours:

```text
| 0584 | [Find Customer Referee](https://leetcode.com/problems/find-customer-referee/) | [MySQL](./leetcode_SQL/find-customer-referee.sql) | _O(n)_ | _O(n)_ | Easy | **sql**, 🔒, `IS NULL` branch, NULL != 2 is not TRUE | AGAIN(1) |
```

- Solution cell: a `MySQL` link to the file, the label every SQL row uses.
- Note cell: `**sql**` first, then 🔒 when `lookup` said premium, then the technique worth
  grepping for later (`window func`, `self join`, `recursive cte`, `left join where null`),
  then company tags in backticks if known.
- Time / Space: the same bounds as the file's `time =` line.
- Status: `AGAIN(1)` for a first pass. Never change a status the user has set.

Then prove the row is right — the number sorts, and the link resolves:

```bash
python3 script/lc_sql.py lookup 175             # -> README: already filed -> | 0175 | ...
python3 script/check_readme.py                  # every link resolves, every status cell parses
```

### 7. Report what was assumed

Close with the file, the README line number, the `smoke` output per version (`PASS` /
`SKIP` with its reason), and every inference: a slug taken from a URL because the audit did
not know the number, a statement written from memory of a premium problem, a version left as
`SKIP`.

## Do not

- ❌ guess the slug from the title — `lookup` it, or take it from the URL (step 1)
- ❌ write a header without the example's input and `Output:` grids — the file becomes
  untestable (directive 2)
- ❌ hand back a query `smoke` has not run, or report a `SKIP` as tested (step 5)
- ❌ edit the expected `Output:` grid to make a query pass
- ❌ comma joins, `t1` / `t2` aliases, `SELECT *`, positional `GROUP BY 1`, `@var :=`
  session variables, or a NULL comparison with no comment (step 3)
- ❌ pad with a second spelling of the same query (step 4)
- ❌ append the row at the end of `## SQL`, or add a second row for a problem already filed
  (step 6)
- ❌ copy a blog's solution with its URL as a version, the way the 2021 files did
- ❌ touch `data/progress.txt` — the practice log is the user's own record and gets its own
  commit
- ❌ commit or push unless asked

## Worked example

`/lc-sql 584`, starting from the one-line draft `select name from Customer where referee_id != 2`.
The result is [`leetcode_SQL/find-customer-referee.sql`](https://github.com/yennanliu/CS_basics/blob/master/leetcode_SQL/find-customer-referee.sql).

| Step | What it produced |
|---|---|
| 1 | `lookup 584` → `find-customer-referee`, Easy, premium: yes (🔒), no README row yet |
| 2 | matched the header of `confirmation-rate.sql`: schema grid, task, `Customer table:` and `Output:` grids |
| 3 | kept the draft's idea (filter on `referee_id`) as `V0`, reformatted to one clause per line, added the `OR referee_id IS NULL` branch, and commented why `!= 2` drops NULL rows |
| 4 | added `V1`: `NOT IN (SELECT id … WHERE referee_id = 2)`, a set difference, and commented why it is NULL-safe here (`id` is the primary key) |
| 5 | the draft alone: `FAIL V0: rows [('Zack',)], expected [['Will'], ['Jane'], ['Bill'], ['Zack']]`; after the fix `PASS V0`, `PASS V1` |
| 6 | inserted between the `0580` and `0595` rows; `lookup 584` now reports it filed; `check_readme.py`: `6 passed, 0 failed` |
| 7 | flagged: the statement was written from the problem page's wording because it is premium, so the user should check it against their copy |
