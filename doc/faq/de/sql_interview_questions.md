# Senior Data Engineer SQL Interview Questions

> **Scope** — a question bank for the SQL round of a senior data engineering interview: every question is asked the way an interviewer asks it, then answered — the sentence to say out loud, the SQL, each follow-up, the cost at scale and the production trade-off. It covers joins at scale, deduplication, window functions, analytical aggregation, gaps and islands, sessions, recursive CTEs, incremental loads with `MERGE`, SCD Type 2, query optimisation and the Spark job with a few slow tasks. The mechanisms behind the answers live in the deep dives it links to; this file is the drill.
> **See also**: [`./sql_for_data.md`](./sql_for_data.md) — joins, NULLs, windows and the interview patterns explained from first principles; [`./sql_explain.md`](./sql_explain.md) — reading the plan that a "why is it slow?" answer starts from; [`./data_performance.md`](./data_performance.md) — storage layout, pruning and SQL rewrites across engines; [`./spark.md`](./spark.md) — shuffles, AQE and skew; [`./data_modeling.md`](./data_modeling.md) — facts, dimensions and SCD; [`./batch_pipeline.md`](./batch_pipeline.md) — idempotency, backfills and late data.

A senior SQL round is not a syntax test. Every question has a second half — *"and if the
table is 1 TB?"*, *"why is it slow?"*, *"how do you know the result is right?"*, *"what
happens when the job reruns?"* — and that second half is what separates the levels. Each
answer below therefore runs **spoken answer → SQL → follow-ups → cost → production
trade-off**.

Every query is **PostgreSQL** and also runs on DuckDB, unless its first line names another
dialect.

---

## 0) The Sample Tables

Every query below runs against these tables. A section that needs another table creates it
in its own block.

```sql
-- PostgreSQL
CREATE TABLE customers (
  customer_id  INT PRIMARY KEY,
  name         TEXT NOT NULL,
  country      TEXT
);

CREATE TABLE orders (
  order_id     INT PRIMARY KEY,
  customer_id  INT,                     -- NULL for a guest checkout
  order_date   DATE NOT NULL,
  amount       NUMERIC(10, 2) NOT NULL,
  status       TEXT NOT NULL            -- 'paid', 'refunded'
);

CREATE TABLE events (
  event_id     INT NOT NULL,            -- the same event can arrive twice
  user_id      INT NOT NULL,
  event_type   TEXT NOT NULL,           -- 'visit', 'purchase'
  event_time   TIMESTAMP NOT NULL,
  updated_at   TIMESTAMP NOT NULL,
  ingest_seq   BIGINT NOT NULL          -- monotonic, assigned on arrival
);

CREATE TABLE employees (
  employee_id   INT PRIMARY KEY,
  name          TEXT NOT NULL,
  department_id INT NOT NULL,
  manager_id    INT,                    -- NULL for the CEO
  salary        NUMERIC(10, 2) NOT NULL
);

CREATE TABLE daily_revenue (order_date DATE PRIMARY KEY, revenue NUMERIC(12, 2) NOT NULL);
CREATE TABLE user_logins   (user_id INT NOT NULL, login_date DATE NOT NULL);

INSERT INTO customers VALUES (1, 'Ann', 'TW'), (2, 'Bo', 'JP'), (3, 'Cy', 'TW'), (4, 'Di', 'US');

INSERT INTO orders VALUES
  (101, 1,    DATE '2026-11-01', 30, 'paid'),
  (102, 1,    DATE '2026-11-02', 20, 'paid'),
  (103, 2,    DATE '2026-11-02', 50, 'refunded'),
  (104, NULL, DATE '2026-11-03', 10, 'paid');

INSERT INTO events VALUES
  (1, 10, 'visit',    TIMESTAMP '2026-11-01 09:00', TIMESTAMP '2026-11-01 09:00', 1),
  (1, 10, 'visit',    TIMESTAMP '2026-11-01 09:00', TIMESTAMP '2026-11-01 09:05', 2),
  (2, 10, 'purchase', TIMESTAMP '2026-11-01 09:20', TIMESTAMP '2026-11-01 09:20', 3),
  (3, 11, 'visit',    TIMESTAMP '2026-11-01 10:00', TIMESTAMP '2026-11-01 10:00', 4),
  (4, 10, 'visit',    TIMESTAMP '2026-11-01 11:00', TIMESTAMP '2026-11-01 11:00', 5),
  (5, 12, 'purchase', TIMESTAMP '2026-11-02 08:00', TIMESTAMP '2026-11-02 08:00', 6),
  (6, 11, 'visit',    TIMESTAMP '2026-11-02 09:00', TIMESTAMP '2026-11-02 09:00', 7);

INSERT INTO employees VALUES
  (1, 'Kai', 1, NULL, 300), (2, 'Lin', 1, 1, 200), (3, 'Max', 1, 1, 200),
  (4, 'Ned', 1, 2,    150), (5, 'Oli', 1, 2, 120), (6, 'Pat', 2, 1, 180),
  (7, 'Quy', 2, 6,     90);

INSERT INTO daily_revenue VALUES               -- 11-04 and 11-07 are missing
  (DATE '2026-11-01', 100), (DATE '2026-11-02', 120), (DATE '2026-11-03', 90),
  (DATE '2026-11-05', 110), (DATE '2026-11-06', 130), (DATE '2026-11-08', 70),
  (DATE '2026-11-09', 150), (DATE '2026-11-10', 140);

INSERT INTO user_logins VALUES                 -- user 10 logged in twice on 11-02
  (10, DATE '2026-11-01'), (10, DATE '2026-11-02'), (10, DATE '2026-11-02'),
  (10, DATE '2026-11-03'), (10, DATE '2026-11-05'),
  (11, DATE '2026-11-01'), (11, DATE '2026-11-03'), (11, DATE '2026-11-04'),
  (11, DATE '2026-11-05'), (11, DATE '2026-11-06');
```

---

## 1) How to Answer a SQL Question ⭐⭐⭐⭐⭐

**"I state the grain of the result, ask about ties, NULLs and late data, write the query as
named CTEs, check it on a tiny example out loud, and then say what it costs at full scale and
what happens when it reruns."**

| Step | What you say | Why it scores |
|---|---|---|
| 1. Grain | "One row per customer per day." | Most wrong answers are a wrong grain — a join that fans out, a count at the wrong level. |
| 2. Edges | "Can two rows share `updated_at`? Can `customer_id` be NULL? Does a day mean UTC?" | Shows you have seen production data, not just textbook tables. |
| 3. CTEs | One step per CTE, each named for what it holds. | The interviewer can follow, and you can debug one step at a time. |
| 4. Tiny check | Two keys, one tie, one missing day — walked through by hand. | Catches the off-by-one before the interviewer does. |
| 5. Cost | "This shuffles 1 B rows by `event_id`; the window sorts each partition." | The senior half of the answer. |
| 6. Rerun | "It is a `MERGE` on the key, so a rerun rewrites the same rows." | Idempotency is what a production job is judged on. |

The four questions to rehearse on **every** drill, because the interviewer will ask at least
one of them:

1. **Scale** — what changes when the table is 1 TB, or the hot key has 10 % of the rows?
2. **Speed** — where does the time go, and how would you prove it? (`EXPLAIN`, the Spark UI.)
3. **Correctness** — how do you know the answer is right? (Row counts, sums, uniqueness checks.)
4. **Reruns** — does running it twice produce duplicates?

Behind those questions the interviewer is scoring five things, and a senior answer touches each
one without being asked:

- **Correctness** — does the result match the business definition, ties and NULLs included?
- **Performance** — at full size, does it cause a large scan, shuffle or spill?
- **Scalability** — does it still work at terabytes, and as the data keeps growing?
- **Reliability** — are retries, duplicates and late-arriving data handled?
- **Trade-offs** — what does the optimisation cost in memory, money or maintenance?

---

## 2) The Topic Map ⭐⭐⭐⭐

| Topic | Must know | What a senior answer adds | Here | Deep dive |
|---|---|---|---|---|
| Query optimisation | `EXPLAIN`, the execution plan | locating the bottleneck node, not guessing | §9 | [`sql_explain.md`](./sql_explain.md) |
| Joins | hash, sort-merge, broadcast | big-to-big joins, skew, fan-out | §3 | [`sql_for_data.md`](./sql_for_data.md) §2 |
| Window functions | `ROW_NUMBER`, `RANK`, `LAG`, `LEAD` | dedupe, running metrics, sessions | §4, §5, §7 | [`sql_for_data.md`](./sql_for_data.md) §5 |
| Aggregation | `GROUP BY`, `HAVING`, conditional aggregation | aggregate before joining, non-additive metrics | §6 | [`sql_for_data.md`](./sql_for_data.md) §4 |
| CTEs and subqueries | CTE, correlated subquery, recursion | readability vs what the optimiser does with it | §8, §9 | [`sql_for_data.md`](./sql_for_data.md) §6 |
| Data quality | NULLs, duplicates, missing records | reconciliation as an assertion that fails the job | §7, §10 | [`batch_pipeline.md`](./batch_pipeline.md) |
| Incremental processing | watermark, CDC, `MERGE` | idempotency, late data, many versions per key | §10 | [`batch_pipeline.md`](./batch_pipeline.md) |
| Data modeling | fact / dimension, SCD 1 / 2 | history that a rerun cannot corrupt | §10 | [`data_modeling.md`](./data_modeling.md) |

---

## 3) Joins at Scale ⭐⭐⭐⭐⭐

### 3-1) Q1 — Join 1 B orders to 100 M customers for each customer's total

> `orders` has 1 billion rows, `customers` 100 million. Compute each customer's total order
> amount.

**"Aggregate `orders` to one row per customer first, then join — the join then sees at most
100 M rows on each side instead of 1 B on one. And if the report only needs `customer_id`, I
do not join at all."**

```sql
-- aggregate first, join second
WITH order_totals AS (
  SELECT customer_id, SUM(amount) AS total_amount, COUNT(*) AS order_count
  FROM   orders
  WHERE  customer_id IS NOT NULL
  GROUP  BY customer_id
)
SELECT c.customer_id, c.name,
       COALESCE(t.total_amount, 0) AS total_amount,
       COALESCE(t.order_count, 0)  AS order_count
FROM   customers c
LEFT   JOIN order_totals t ON t.customer_id = c.customer_id;
```

Why it is faster: `SUM` is **decomposable**, so each worker pre-aggregates its own slice
(a partial aggregate) before the shuffle and only one row per customer per worker crosses the
network. Joining first ships all 1 B order rows to wherever their customer lives.

**Follow-up — how would you design it?** Restate the grain (one row per customer), decide
whether customers with no orders appear (they do, with `0` — hence `LEFT JOIN` from
`customers` and `COALESCE`), decide what to do with guest orders (`customer_id IS NULL` — they
belong to nobody, so they are filtered and reported as a separate total), and say which
statuses count (`refunded` probably should not; ask).

**Follow-up — the join is slow. What do you do?**

1. Read the plan or the Spark UI before changing anything (§9-1): is the time in the scan,
   the shuffle, the sort, or one task?
2. **Read less**: filter by date partition, select only the columns needed — `orders` is
   columnar, so `customer_id, amount` is a fraction of the bytes of `SELECT *`.
3. **Move less**: aggregate before the join (above); broadcast the small side if one exists.
4. **Fix skew** if one task runs far longer than its siblings (next follow-up).
5. **Avoid the shuffle entirely** for a join run every day: bucket or cluster both tables on
   `customer_id` with the same bucket count, so matching keys are already co-located.

**Follow-up — how do you tell whether there is data skew?** Two places agree when it is skew:

```sql
-- the key distribution: is a handful of keys carrying most of the rows?
SELECT customer_id, COUNT(*) AS n
FROM   orders
GROUP  BY customer_id
ORDER  BY n DESC
LIMIT  20;
```

and in the Spark UI, the stage's task summary shows **max duration and shuffle read far above
the median** (for example 20 minutes against 2). A `NULL` or a sentinel such as `-1` or
`'unknown'` at the top of that list is the classic cause: every guest order hashes to the same
partition.

**Follow-up — one table fits in memory. What changes?** Then **broadcast** it: every executor
gets a full copy and the big table is joined in place, with no shuffle of the big side at all.
Spark does it automatically below `spark.sql.autoBroadcastJoinThreshold` (10 MB by default),
and AQE switches to it at runtime when a filtered side turns out small; you can force it with
a hint. 100 M customers will not fit — but `customers WHERE country = 'TW'` might.

| Join strategy | When the engine uses it |
|---|---|
| Broadcast hash join | one side is small enough to copy to every executor |
| Sort-merge join | both sides are large; Spark's default for an equi-join of two big tables |
| Shuffle hash join | both sides are shuffled, and each partition of the smaller side fits in memory as a hash table |

Here both tables are large, so the default is a sort-merge join; do not assume a broadcast fits
until the filtered size says so.

```sql
-- Spark SQL
SELECT /*+ BROADCAST(c) */ c.customer_id, SUM(o.amount) AS total_amount
FROM   orders o
JOIN   customers c ON c.customer_id = o.customer_id
WHERE  c.country = 'TW'
GROUP  BY c.customer_id;
```

**Follow-up — how do you stop the join from multiplying rows?** A join returns more rows than
its left side when the right side has **several rows per key** — fan-out. The usual culprit
on a dimension is history: an SCD Type 2 `customers` table has one row per *version*, so
every order joins to every version of its customer. Check the uniqueness you are assuming, and
reconcile before and after:

```sql
-- the assumption: one row per customer_id on the right
SELECT customer_id, COUNT(*) FROM customers GROUP BY customer_id HAVING COUNT(*) > 1;

-- the reconciliation: the join must not change the total
SELECT (SELECT SUM(amount) FROM orders WHERE customer_id IS NOT NULL) AS before_join,
       (SELECT SUM(o.amount) FROM orders o JOIN customers c USING (customer_id)) AS after_join;
```

On a versioned dimension, join to the current row (`AND c.is_current`) or to the version valid
at the order's time (`AND o.order_date >= c.valid_from AND (o.order_date < c.valid_to OR
c.valid_to IS NULL)`).

### 3-2) Q3 — Customers who never ordered

**"An anti-join: `NOT EXISTS` is my default, because it is correct whatever the NULLs and
every major optimiser runs it as an anti-join. `LEFT JOIN … IS NULL` is equivalent. `NOT IN`
is the one to avoid."**

```sql
-- NOT EXISTS: the default
SELECT c.customer_id
FROM   customers c
WHERE  NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.customer_id);

-- LEFT JOIN ... IS NULL: the same result
SELECT c.customer_id
FROM   customers c
LEFT   JOIN orders o ON o.customer_id = c.customer_id
WHERE  o.customer_id IS NULL;

-- NOT IN: returns NO rows, because orders.customer_id contains a NULL
SELECT c.customer_id
FROM   customers c
WHERE  c.customer_id NOT IN (SELECT customer_id FROM orders);
```

The first two return customers 3 and 4; the third returns nothing.

**Follow-up — `LEFT JOIN … IS NULL` vs `NOT EXISTS`?** Same answer, same plan in PostgreSQL,
Spark, BigQuery and Snowflake (an anti-join). Two practical differences: `NOT EXISTS` stops at
the first match and cannot fan out, while the `LEFT JOIN` form only works if you test a column
that is **non-nullable on the right** — test a nullable one and real matches look like misses.

**Follow-up — why does `NOT IN` break on a NULL?** `x NOT IN (a, b, NULL)` means
`x <> a AND x <> b AND x <> NULL`, and `x <> NULL` is `UNKNOWN`, so the whole condition is
never `TRUE` and no row passes. It is also slower: because of that NULL rule PostgreSQL cannot
turn `NOT IN` into an anti-join and runs it as a filter against a hashed subplan.

**Follow-up — which is faster on your database, and how would you check?** Do not argue from
memory; run `EXPLAIN ANALYZE` on both and compare the plan shape and actual time. In
PostgreSQL both good forms show a `Hash Anti Join`; `NOT IN` shows `Filter: (NOT (hashed
SubPlan 1))`. In Spark, `EXPLAIN FORMATTED` shows `LeftAnti` for both good forms, and a
`BroadcastNestedLoopJoin` for a `NOT IN` against a nullable column — the slowest join Spark has.

### 3-3) Exploding joins and the Cartesian product

**"A join with a missing or non-unique key multiplies rows; on a cluster that is a shuffle
that never finishes or an executor that runs out of memory. I make the right side unique on
the join key before joining, and I check the row count after."**

| Cause | What happens | Fix |
|---|---|---|
| Join condition forgotten | every row × every row (a Cartesian product) | an explicit `ON`; Spark refuses an implicit cross join unless asked |
| Many-to-many key | rows multiply by the duplicates on each side | aggregate or `DISTINCT` one side to the join key first |
| Versioned dimension | each fact matches every version | join on `is_current` or the validity interval |
| Join on a low-cardinality column | e.g. `country` — every pair in a country | join on the real key; aggregate before joining |

```sql
-- many-to-many made one-to-many: reduce the right side to one row per key first
WITH latest_status AS (
  SELECT DISTINCT ON (customer_id) customer_id, status
  FROM   orders
  WHERE  customer_id IS NOT NULL
  ORDER  BY customer_id, order_date DESC, order_id DESC
)
SELECT c.customer_id, s.status
FROM   customers c
LEFT   JOIN latest_status s USING (customer_id);
```

---

## 4) Deduplication ⭐⭐⭐⭐⭐

### 4-1) Q2 — Keep the latest version of each event

> Each event has `event_id`, `user_id`, `event_time` and `updated_at`. The same `event_id` can
> arrive more than once; keep the latest version.

**"Number each `event_id`'s rows newest first and keep number 1 — with a tie-breaker in the
`ORDER BY` so the choice is deterministic."**

```sql
WITH ranked AS (
  SELECT *,
         ROW_NUMBER() OVER (
           PARTITION BY event_id
           ORDER BY updated_at DESC, ingest_seq DESC   -- the tie-breaker
         ) AS rn
  FROM   events
)
SELECT event_id, user_id, event_type, event_time, updated_at
FROM   ranked
WHERE  rn = 1;
```

BigQuery, Snowflake, DuckDB and Spark 4.2+ can filter on the window directly with
`QUALIFY rn = 1`, which saves the CTE; Spark 4.1 and earlier need the CTE or a subquery.

**Follow-up — two rows share `updated_at`. How is the result deterministic?** Without a
tie-breaker `ROW_NUMBER` picks either row, and a rerun on a different partitioning may pick the
other — the same input then yields two different tables. Add a column that really orders the
versions: a source sequence number, a CDC log position (LSN, binlog offset), a Kafka
offset, or an ingestion sequence as here. As a last resort, a hash of the row makes it
repeatable, though not meaningful.

**Follow-up — `ROW_NUMBER()` vs `DISTINCT`?** `DISTINCT` removes rows that are identical in
**every** column. The two versions of event 1 differ in `updated_at`, so `DISTINCT` keeps
both. `ROW_NUMBER` dedupes **by key** and lets you choose which row wins. `DISTINCT` is right
only for exact duplicates — the same record delivered twice.

**Follow-up — 1 B rows. What performance problems?** The window needs every row of an
`event_id` together, so it **shuffles the whole table by `event_id`** and **sorts** each
partition — the most expensive two steps in the plan, and a sort that may spill to disk.
Problems to name and their fixes:

- **Skew** on a hot or NULL `event_id` — one task gets millions of rows. Filter or handle the
  NULLs separately.
- **Spill** — more shuffle partitions so each sort fits in memory.
- **A cheaper operator** — an aggregate needs no sort. Spark, DuckDB and BigQuery have an
  "arg max" aggregate that returns the row with the largest ordering value:

```sql
-- DuckDB (Spark: max_by(struct(*), struct(updated_at, ingest_seq)))
SELECT event_id,
       arg_max(user_id,    (updated_at, ingest_seq)) AS user_id,
       arg_max(event_type, (updated_at, ingest_seq)) AS event_type
FROM   events
GROUP  BY event_id;
```

- **Do not dedupe the whole history every run** — see the next follow-up.

**Follow-up — the data keeps arriving. How do you dedupe incrementally?** Dedupe only the new
batch, then `MERGE` it into a target that is already unique, replacing a row only when the
incoming version is newer:

```sql
CREATE TABLE events_clean AS
SELECT event_id, user_id, event_type, event_time, updated_at, ingest_seq
FROM   events WHERE FALSE;                          -- an empty copy, unique on event_id

MERGE INTO events_clean t
USING (
  SELECT * FROM (
    SELECT *, ROW_NUMBER() OVER (PARTITION BY event_id
                                 ORDER BY updated_at DESC, ingest_seq DESC) AS rn
    FROM   events
    WHERE  ingest_seq > 0                            -- the new batch: above the last watermark
  ) b WHERE rn = 1
) s
ON t.event_id = s.event_id
WHEN MATCHED AND (s.updated_at, s.ingest_seq) > (t.updated_at, t.ingest_seq) THEN
  UPDATE SET user_id = s.user_id, event_type = s.event_type, event_time = s.event_time,
             updated_at = s.updated_at, ingest_seq = s.ingest_seq
WHEN NOT MATCHED THEN
  INSERT (event_id, user_id, event_type, event_time, updated_at, ingest_seq)
  VALUES (s.event_id, s.user_id, s.event_type, s.event_time, s.updated_at, s.ingest_seq);
```

The batch must be deduplicated **before** the `MERGE`: two source rows matching one target row
is an error in PostgreSQL, Spark / Delta and Snowflake (*"cannot affect row a second time"*).
To keep the `MERGE` cheap on a big target you can add the batch's event-date range to the `ON`
clause, so only the touched partitions are read — **but only under a contract that an event's
date never changes**, so every existing row for the batch's keys is inside that range. Without
it, a correction that moves an event to another date finds no match in the pruned range and the
`MERGE` inserts a second row for the same `event_id`. If dates can change, match on `event_id`
alone and get the speed from the target's layout instead — clustering or Z-ordering on
`event_id` so the key lookup skips files. §10-1 covers the watermark.

### 4-2) Deduplicate with a status priority

> Retries leave several rows per `order_id`. Keep the most *correct* one: `SUCCESS` over
> `PENDING` over `FAILED`, and the latest within a status.

**"The same `ROW_NUMBER`, with the business priority encoded as a `CASE` in the `ORDER BY`
ahead of the timestamp."**

```sql
CREATE TABLE raw_orders (order_id INT, status TEXT, amount NUMERIC(10, 2), updated_at TIMESTAMP);
INSERT INTO raw_orders VALUES
  (1, 'PENDING', 30, TIMESTAMP '2026-11-01 10:00'),
  (1, 'SUCCESS', 30, TIMESTAMP '2026-11-01 10:01'),
  (1, 'FAILED',  30, TIMESTAMP '2026-11-01 10:05'),   -- a late retry failure: must not win
  (2, 'FAILED',  50, TIMESTAMP '2026-11-01 11:00'),
  (2, 'PENDING', 50, TIMESTAMP '2026-11-01 11:02');

WITH prioritized AS (
  SELECT *,
         ROW_NUMBER() OVER (
           PARTITION BY order_id
           ORDER BY CASE status WHEN 'SUCCESS' THEN 1
                                WHEN 'PENDING' THEN 2
                                ELSE 3 END,
                    updated_at DESC
         ) AS rn
  FROM   raw_orders
)
SELECT order_id, status, amount, updated_at FROM prioritized WHERE rn = 1;
```

Order 1 keeps `SUCCESS` although a `FAILED` row is newer; order 2 keeps `PENDING`. Say why the
rule exists — a payment that succeeded once is paid, whatever a later retry reported — because
the interviewer is checking that you ask for the business rule rather than defaulting to
"latest wins". Keep the priority in a small mapping table rather than a `CASE` when more than
one query needs it.

---

## 5) Window Functions ⭐⭐⭐⭐⭐

### 5-1) Q4 — The second-highest salary in each department

**"`DENSE_RANK` by salary descending within the department and keep rank 2 — after asking
whether 'second highest' means the second *distinct salary* or the second *person*."**

```sql
WITH ranked AS (
  SELECT department_id, employee_id, name, salary,
         DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS salary_rank
  FROM   employees
)
SELECT department_id, employee_id, name, salary
FROM   ranked
WHERE  salary_rank = 2;
```

Department 1 (salaries 300, 200, 200, 150, 120) returns Lin **and** Max at 200; department 2
returns Quy at 90.

**Follow-up — `ROW_NUMBER` vs `RANK` vs `DENSE_RANK`?** They differ only on ties.

| Salary | `ROW_NUMBER` | `RANK` | `DENSE_RANK` |
|---|---|---|---|
| 300 | 1 | 1 | 1 |
| 200 | 2 | 2 | 2 |
| 200 | 3 | 2 | 2 |
| 150 | 4 | 4 | 3 |

`ROW_NUMBER` gives unique numbers and breaks ties arbitrarily (use it to pick exactly one row);
`RANK` gives ties the same rank and then **skips** (Olympic ranking); `DENSE_RANK` gives ties
the same rank and **does not skip** (use it for "the n-th distinct value").

**Follow-up — two people share the highest salary. What is the second highest?** It depends on
the question, which is why you ask:

- **The second distinct salary** — `DENSE_RANK = 2`. With two people at 300 and one at 200,
  the answer is 200.
- **The person in second place** — `ROW_NUMBER = 2` returns one of the two people at 300,
  picked by the tie-breaker (add `employee_id` to the `ORDER BY` so the pick is stable),
  while `RANK = 2` returns nothing, because both are rank 1 and the next rank is 3.

Also say what happens to a department with a single salary: no row. If the report needs a row
with `NULL` (as LeetCode 176 does), select from the departments and `LEFT JOIN` the rank-2 row.

**Follow-up — the top three earners in each department?** `DENSE_RANK() <= 3`, so ties at the
boundary are all included (LeetCode 185). Say what that means: the top three **salary levels**,
which can be more than three people:

```sql
SELECT department_id, name, salary
FROM (
  SELECT department_id, name, salary,
         DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS r
  FROM   employees
) t
WHERE  r <= 3
ORDER  BY department_id, salary DESC;
```

Use `ROW_NUMBER() <= 3` instead if the report must have **exactly** three rows per
department, and say which tie-breaker decides who is cut.

### 5-2) Q5 — A 7-day rolling average of revenue

**"A window frame over the last seven days — but `ROWS BETWEEN 6 PRECEDING` counts rows, not
days, so with missing dates it averages over more than a week. I fill the dates from a
calendar first, or use a `RANGE` frame on the date."**

```sql
-- the version usually written first: correct only if every date has a row
SELECT order_date, revenue,
       AVG(revenue) OVER (ORDER BY order_date
                          ROWS BETWEEN 6 PRECEDING AND CURRENT ROW) AS rolling_avg_rows
FROM   daily_revenue;
```

On 11-08 that frame covers 11-01 to 11-08 — **eight** calendar days, because 11-04 and 11-07
have no row.

**Follow-up — `ROWS` vs `RANGE`?** `ROWS` counts physical rows around the current one.
`RANGE` takes every row whose **ordering value** is within the offset of the current row's, so
`RANGE BETWEEN INTERVAL '6 days' PRECEDING AND CURRENT ROW` is exactly the last seven calendar
days however many rows that is. `RANGE` also treats **peers** — rows with an equal ordering
value — as one unit, which is why the default frame (`RANGE … CURRENT ROW`) gives tied rows the
same running total. Spark SQL's `RANGE` needs a numeric ordering column, so order by a day
number there.

**Follow-up — dates are missing. Is the result still a 7-day average?** No — on two counts. The
`ROWS` frame spans more than seven days, and even a correct `RANGE` frame averages over the days
that **exist**, so a missing day is skipped rather than counted as zero. Which is right is a
business question: a missing day in a revenue table is usually a day with zero sales *or* a
pipeline failure, and you must know which before averaging (§7-3).

**Follow-up — fill the dates first, then average.** If the data starts as `orders`, aggregate
it to **one row per date** first (`SUM(amount) … GROUP BY order_date`). Then generate a date
spine, `LEFT JOIN` the daily rows onto it, and decide what a missing day is worth — zero only if
a missing day genuinely means no revenue:

```sql
WITH calendar AS (
  SELECT CAST(d AS DATE) AS order_date
  FROM   generate_series(DATE '2026-11-01', DATE '2026-11-10', INTERVAL '1 day') AS g(d)
),
filled AS (
  SELECT c.order_date, COALESCE(r.revenue, 0) AS revenue    -- a missing day counts as zero
  FROM   calendar c
  LEFT   JOIN daily_revenue r ON r.order_date = c.order_date
)
SELECT order_date, revenue,
       AVG(revenue) OVER w AS rolling_avg_7d,
       COUNT(*)     OVER w AS days_in_window       -- < 7 for the first six days
FROM   filled
WINDOW w AS (ORDER BY order_date ROWS BETWEEN 6 PRECEDING AND CURRENT ROW);
```

Now `ROWS` and days agree. The first six rows still average fewer than seven days; report them
as `NULL` (`CASE WHEN days_in_window = 7 THEN … END`) if a partial window would mislead. Without
the spine, the equivalent is the `RANGE` frame:

```sql
SELECT order_date, revenue,
       AVG(revenue) OVER (ORDER BY order_date
                          RANGE BETWEEN INTERVAL '6 days' PRECEDING AND CURRENT ROW)
         AS rolling_avg_days_present
FROM   daily_revenue;
```

— which averages the days present, not seven, so it is only right when a missing day means
"unknown" rather than "zero".

---

## 6) Aggregation and Analytical SQL ⭐⭐⭐⭐

### 6-1) Q6 — Daily visitors, purchasers and conversion rate

**"Conditional distinct counts in one pass over the table, divided with `NULLIF` so a day with
no visitors returns `NULL` instead of an error — and then I check the definition, because 'a
purchaser on a day' is not the same as 'a visitor who converted'."**

```sql
SELECT CAST(event_time AS DATE) AS event_date,
       COUNT(DISTINCT CASE WHEN event_type = 'visit'    THEN user_id END) AS visitors,
       COUNT(DISTINCT CASE WHEN event_type = 'purchase' THEN user_id END) AS purchasers,
       1.0 * COUNT(DISTINCT CASE WHEN event_type = 'purchase' THEN user_id END)
           / NULLIF(COUNT(DISTINCT CASE WHEN event_type = 'visit' THEN user_id END), 0)
         AS conversion_rate
FROM   events
GROUP  BY CAST(event_time AS DATE)
ORDER  BY event_date;
```

The `CASE` without an `ELSE` yields `NULL` for other event types, and `COUNT` skips NULLs —
that is what makes one scan produce both counts.

**Follow-up — why `NULLIF`?** `NULLIF(x, 0)` returns `NULL` when `x` is 0, and anything
divided by `NULL` is `NULL`. Without it a day with no visits raises *division by zero* and
kills the whole query. The `1.0 *` matters too: an integer divided by an integer truncates in
PostgreSQL, Spark's `div` and SQL Server, so 1 / 2 would be 0.

**Follow-up — `COUNT(*)` vs `COUNT(column)` vs `COUNT(DISTINCT column)`?**

| Expression | Counts | On `(10, 10, NULL)` |
|---|---|---|
| `COUNT(*)` | rows | 3 |
| `COUNT(user_id)` | non-NULL values | 2 |
| `COUNT(DISTINCT user_id)` | distinct non-NULL values | 1 |

**Follow-up — a user visits on one day and buys on another. Does the metric fit?** No. On
11-02 user 12 purchased without a visit that day, so the rate for 11-02 counts a purchaser who
is not in the denominator. With one user who only purchased and another who only visited, the
query reports 1 / 1 = 100 % although nobody converted — and a daily rate can even exceed 100 %.
Agree the definition first, then write it so the numerator is a subset of the denominator. For
*"visited and purchased on the same day"*, aggregate to one row per user per day with flags:

```sql
WITH user_daily AS (
  SELECT CAST(event_time AS DATE) AS event_date, user_id,
         MAX(CASE WHEN event_type = 'visit'    THEN 1 ELSE 0 END) AS visited,
         MAX(CASE WHEN event_type = 'purchase' THEN 1 ELSE 0 END) AS purchased
  FROM   events
  GROUP  BY CAST(event_time AS DATE), user_id
)
SELECT event_date,
       SUM(visited)                                              AS visitors,
       SUM(CASE WHEN visited = 1 AND purchased = 1 THEN 1 ELSE 0 END) AS converted,
       1.0 * SUM(CASE WHEN visited = 1 AND purchased = 1 THEN 1 ELSE 0 END)
           / NULLIF(SUM(visited), 0)                             AS conversion_rate
FROM   user_daily
GROUP  BY event_date
ORDER  BY event_date;
```

For *"of the users who visited on day d, the share who purchased within 24 hours"* — which also
counts a purchase just after midnight — anchor on the first visit:

```sql
WITH visits AS (
  SELECT user_id, CAST(event_time AS DATE) AS visit_date, MIN(event_time) AS first_visit
  FROM   events WHERE event_type = 'visit'
  GROUP  BY user_id, CAST(event_time AS DATE)
)
SELECT v.visit_date,
       COUNT(*)                     AS visitors,
       COUNT(p.user_id)             AS converted,
       1.0 * COUNT(p.user_id) / COUNT(*) AS conversion_rate
FROM   visits v
LEFT   JOIN LATERAL (
  SELECT e.user_id FROM events e
  WHERE  e.user_id = v.user_id AND e.event_type = 'purchase'
    AND  e.event_time >= v.first_visit
    AND  e.event_time <  v.first_visit + INTERVAL '24 hours'
  LIMIT  1
) p ON TRUE
GROUP  BY v.visit_date
ORDER  BY v.visit_date;
```

Also ask which time zone "a day" is in; `CAST(event_time AS DATE)` on a UTC timestamp is a UTC
day.

**Follow-up — `COUNT(DISTINCT)` is expensive on a big table. How do you cut the cost?**

1. **Approximate it**: `approx_count_distinct` (Spark, BigQuery `APPROX_COUNT_DISTINCT`,
   Snowflake `APPROX_COUNT_DISTINCT`) uses HyperLogLog — a few KB of state per group and an
   error around 1–2 %. Fine for a dashboard, not for billing.
2. **Pre-aggregate to the user-day grain** once — one row per `(date, user_id)` with
   `visited` / `purchased` flags — and count rows from that table. The distinct work happens
   once, in the pipeline, not in every dashboard query.
3. **Store sketches** (HLL) per day so weekly and monthly uniques are a sketch union, not a
   rescan. Distinct counts are **not additive**: the sum of seven DAUs is not WAU.
4. **One distinct per query** — several `COUNT(DISTINCT …)` over different columns in one
   query expand the data once per column in Spark (the `Expand` operator). Split them, or
   pre-aggregate.

### 6-2) Cohort retention

> Of the users who signed up on day d, what share was active on day d + 1, d + 7 and d + 30?

**"Build the cohort (first day per user), build distinct active days, join them on the user,
count actives per cohort and day offset — and divide by the cohort size computed on its own,
because a cohort's size must not depend on who came back."**

```sql
CREATE TABLE users (user_id INT PRIMARY KEY, signup_ts TIMESTAMP NOT NULL);
INSERT INTO users VALUES
  (10, TIMESTAMP '2026-11-01 08:00'), (11, TIMESTAMP '2026-11-01 09:30'),
  (12, TIMESTAMP '2026-11-02 07:00'), (13, TIMESTAMP '2026-11-01 12:00');

WITH cohort AS (
  SELECT user_id, CAST(signup_ts AS DATE) AS cohort_date FROM users
),
cohort_size AS (
  SELECT cohort_date, COUNT(*) AS cohort_users FROM cohort GROUP BY cohort_date
),
activity AS (
  SELECT DISTINCT user_id, CAST(event_time AS DATE) AS activity_date FROM events
),
retained AS (
  SELECT c.cohort_date,
         a.activity_date - c.cohort_date AS day_number,
         COUNT(DISTINCT a.user_id)        AS active_users
  FROM   cohort c
  JOIN   activity a ON a.user_id = c.user_id AND a.activity_date >= c.cohort_date
  GROUP  BY c.cohort_date, a.activity_date - c.cohort_date
),
offsets AS (                                   -- the day offsets the report asks for
  SELECT day_number FROM (VALUES (0), (1), (7), (30)) AS o(day_number)
),
eligible AS (                                  -- only offsets a cohort is old enough to have
  SELECT s.cohort_date, s.cohort_users, o.day_number
  FROM   cohort_size s
  CROSS  JOIN offsets o
  WHERE  s.cohort_date + o.day_number <= (SELECT MAX(activity_date) FROM activity)
)
SELECT e.cohort_date, e.day_number, e.cohort_users,
       COALESCE(r.active_users, 0)                                     AS active_users,
       ROUND(100.0 * COALESCE(r.active_users, 0) / e.cohort_users, 1) AS retention_pct
FROM   eligible e
LEFT   JOIN retained r ON r.cohort_date = e.cohort_date AND r.day_number = e.day_number
ORDER  BY e.cohort_date, e.day_number;
```

The 11-01 cohort has three users (10, 11, 13); on day 1 only user 11 is active, so day-1
retention is 33.3 %. The `eligible` step is what makes a mature cohort with nobody back on day
7 show **0 %** instead of no row at all — `retained` only has the cohort-day pairs where someone
was active — and it leaves out offsets a cohort is too young to have reached.

**The bug this version avoids.** A common write-up computes `COUNT(DISTINCT c.user_id)` and
`COUNT(DISTINCT a.user_id)` in the same `GROUP BY cohort_date, day_number` after a `LEFT JOIN`
with `WHERE a.activity_date >= c.cohort_date`. Two things go wrong: the `WHERE` on the right
table turns the `LEFT JOIN` into an inner join, so users who never came back vanish, and inside
each `day_number` group the "cohort size" is just the users active that day — so retention is
100 % on every row. Compute the denominator once per cohort, separately.

Production note: the signup date comes from a `users` table rather than `MIN(event_time)`,
which shifts when old events are backfilled; and "as of" is the last day the activity data is
complete, not today. The sample treats `MAX(activity_date)` as complete; a production job
replaces that expression with a trusted completeness watermark — the last day whose load
finished — or a partly loaded day reports retention that is too low.

### 6-3) DAU, WAU and MAU

**"DAU is distinct users per day. WAU and MAU are distinct users over a trailing window, which
is not the sum of DAUs — so I count distinct users over a date range, or union sketches."**

```sql
WITH user_days AS (
  SELECT DISTINCT user_id, CAST(event_time AS DATE) AS d FROM events
),
calendar AS (
  SELECT DISTINCT d FROM user_days
)
SELECT c.d,
       COUNT(DISTINCT CASE WHEN u.d = c.d                       THEN u.user_id END) AS dau,
       COUNT(DISTINCT CASE WHEN u.d >  c.d - 7                  THEN u.user_id END) AS wau,
       COUNT(DISTINCT u.user_id)                                                    AS mau
FROM   calendar c
JOIN   user_days u ON u.d > c.d - 30 AND u.d <= c.d       -- a trailing 30-day window
GROUP  BY c.d
ORDER  BY c.d;
```

The range join makes every user-day appear in up to 30 output days, so at scale this is
computed incrementally — one partition per day from a stored user-day table — or from daily
HLL sketches, where `MAU(d)` is the union of the last 30 sketches. `DAU / MAU` is the
"stickiness" ratio.

### 6-4) Each user's first and most recent purchase

**"`MIN` and `MAX` of the purchase date grouped by user. If the question wants the whole first
row — amount, product — it becomes a `ROW_NUMBER` or `arg_min` question."**

```sql
SELECT customer_id,
       MIN(order_date) AS first_order_date,
       MAX(order_date) AS last_order_date,
       COUNT(*)        AS orders,
       MAX(order_date) - MIN(order_date) AS active_span_days
FROM   orders
WHERE  status = 'paid' AND customer_id IS NOT NULL
GROUP  BY customer_id;
```

Two edges to name: a refunded first order (filter on status, or not — ask), and a tie on the
first date when you need the whole row (break it on `order_id`).

### 6-5) Daily rows into a monthly report

**"Conditional aggregation: one `SUM(CASE …)` per output column. It is the portable pivot, and
it is one scan."**

```sql
SELECT customer_id,
       SUM(CASE WHEN status = 'paid'     THEN amount ELSE 0 END) AS paid_amount,
       SUM(CASE WHEN status = 'refunded' THEN amount ELSE 0 END) AS refunded_amount,
       COUNT(*) FILTER (WHERE status = 'paid')                   AS paid_orders
FROM   orders
WHERE  order_date >= DATE '2026-11-01' AND order_date < DATE '2026-12-01'
  AND  customer_id IS NOT NULL
GROUP  BY customer_id;

-- one row per month, one column per status
SELECT DATE_TRUNC('month', order_date) AS month,
       SUM(amount) FILTER (WHERE status = 'paid')     AS paid,
       SUM(amount) FILTER (WHERE status = 'refunded') AS refunded
FROM   orders
GROUP  BY DATE_TRUNC('month', order_date)
ORDER  BY month;
```

`FILTER (WHERE …)` is the standard spelling (PostgreSQL, DuckDB, SQLite); `SUM(CASE …)` works
everywhere. Spark, Snowflake and BigQuery also have a `PIVOT` clause, but its column list must
be known when the query is written — a new status needs a code change either way.

---

## 7) Gaps, Islands and Sessions ⭐⭐⭐⭐⭐

### 7-1) Users who logged in on three or more consecutive days

**"Gaps and islands: within each user, subtract the row number from the date. Consecutive dates
minus consecutive numbers give the same value, so that difference is the island's id. Dedupe
the dates first, or a second login on the same day breaks the run."**

```sql
WITH login_days AS (
  SELECT DISTINCT user_id, login_date FROM user_logins      -- one row per user per day
),
numbered AS (
  SELECT user_id, login_date,
         login_date - CAST(ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) AS INT)
           AS island_id
  FROM   login_days
)
SELECT user_id,
       MIN(login_date) AS streak_start,
       MAX(login_date) AS streak_end,
       COUNT(*)        AS streak_days
FROM   numbered
GROUP  BY user_id, island_id
HAVING COUNT(*) >= 3
ORDER  BY user_id, streak_start;
```

User 10 has a streak 11-01 → 11-03; user 11 has 11-03 → 11-06.

```text
user 11   login_date   row_number   login_date - row_number
          11-01        1            10-31   island A
          11-03        2            11-01   island B
          11-04        3            11-01   island B
          11-05        4            11-01   island B
          11-06        5            11-01   island B   -> 4 days
```

- **Why dedupe**: user 10's two logins on 11-02 would take row numbers 2 and 3, the
  differences then split, and a three-day streak is reported as two short ones. `DENSE_RANK`
  instead of `ROW_NUMBER` fixes it too.
- **Dialects**: date minus integer is PostgreSQL / DuckDB; MySQL needs `DATE_SUB(login_date,
  INTERVAL rn DAY)`, Spark `date_sub(login_date, rn)`.
- **The `LAG` alternative** — flag a row as a new island when it is not the day after the
  previous one, then a running `SUM` of the flags is the island id. It generalises to gaps of
  any size ("within two days counts as consecutive"), which the row-number trick does not.
- LeetCode 601 (*Human Traffic of Stadium*) is this question on ids instead of dates.

### 7-2) Sessionisation: a new session after 30 minutes of inactivity

**"`LAG` gives each event the previous event's time; a gap over 30 minutes (or no previous
event) flags a new session; a running `SUM` of the flags numbers the sessions."**

```sql
WITH ordered AS (
  SELECT user_id, event_time,
         LAG(event_time) OVER (PARTITION BY user_id ORDER BY event_time) AS prev_time
  FROM   events
),
flagged AS (
  SELECT user_id, event_time,
         CASE WHEN prev_time IS NULL
                OR event_time > prev_time + INTERVAL '30 minutes' THEN 1 ELSE 0 END
           AS new_session
  FROM   ordered
)
SELECT user_id, event_time,
       SUM(new_session) OVER (PARTITION BY user_id ORDER BY event_time
                              ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS session_no
FROM   flagged
ORDER  BY user_id, event_time;
```

User 10's events at 09:00, 09:00, 09:20 are session 1; 11:00 is session 2.

- **Dedupe first** — the duplicated event 1 is in session 1 twice here; in production run the
  §4-1 dedupe before sessionising.
- **Ties on `event_time`** — add a tie-breaker to both `ORDER BY`s, or two events at the same
  instant can be numbered differently on two runs.
- **A global session id** — `user_id || '-' || session_no`, or a hash of the user and the
  session's first event time, which stays stable when later events arrive.
- **At scale** this shuffles by `user_id` and sorts each user's events; a bot with millions of
  events is the skewed key. **Across batch boundaries** a session can span midnight, so either
  reprocess a lookback window or carry each user's last event time forward in state — which is
  what a streaming session window does.
- **Dialect** — `INTERVAL '30 minutes'` in PostgreSQL and DuckDB; Spark accepts
  `INTERVAL 30 MINUTES`, BigQuery uses `TIMESTAMP_DIFF(event_time, prev_time, MINUTE) > 30`.

### 7-3) Which dates are missing from a daily table?

**"Generate a calendar for the expected range and anti-join the table against it. Then the
real question: is a missing date zero revenue, or a pipeline that did not run?"**

```sql
WITH calendar AS (
  SELECT CAST(d AS DATE) AS dt
  FROM   generate_series(DATE '2026-11-01', DATE '2026-11-10', INTERVAL '1 day') AS g(d)
)
SELECT c.dt AS missing_date
FROM   calendar c
WHERE  NOT EXISTS (SELECT 1 FROM daily_revenue r WHERE r.order_date = c.dt)
ORDER  BY c.dt;
```

Returns 11-04 and 11-07.

**Follow-up — where does the calendar come from?** `generate_series` in PostgreSQL and
DuckDB, `sequence()` + `explode()` in Spark, `GENERATE_DATE_ARRAY` + `UNNEST` in BigQuery,
`GENERATOR` in Snowflake. Most warehouses keep a permanent `dim_date` table — one row per day
for decades, with fiscal periods and holidays — and joining that is simpler than generating.

**Follow-up — the source is partitioned Parquet. How do you avoid a full scan?** Do not read the
data at all: the question is which **partitions** exist, and that is metadata. `SHOW PARTITIONS
sales` in Spark / Hive, the table format's metadata (`sales.partitions` in Iceberg, the Delta
log), or an object-store listing of `dt=…/` prefixes. Compare that list with the calendar. If
you must read data, select only the partition column so the engine answers from file metadata.

**Follow-up — zero revenue or pipeline failure?** The table cannot say, which is why the check
needs a second source: the load log (did the job for 11-04 succeed?), the source system's row
count for the day, or the fact that every other region had sales that day. Write the pipeline
so a successful day **always** produces a row — zero included — and then a missing row can only
mean a failure.

**Follow-up — make it a data quality check.** Run it as an assertion at the end of the load
that **fails the job** (an Airflow task, a dbt test, a Great Expectations or Soda check) rather
than a query someone remembers to run:

```sql
-- returns rows only when something is wrong; the orchestrator fails the task if any come back
WITH calendar AS (
  SELECT CAST(d AS DATE) AS dt
  FROM   generate_series(DATE '2026-11-01', DATE '2026-11-10', INTERVAL '1 day') AS g(d)
)
SELECT 'missing_date' AS check_name, CAST(dt AS TEXT) AS detail
FROM   calendar c
WHERE  NOT EXISTS (SELECT 1 FROM daily_revenue r WHERE r.order_date = c.dt)
UNION ALL
SELECT 'negative_revenue', CAST(order_date AS TEXT)
FROM   daily_revenue WHERE revenue < 0;
```

---

## 8) Recursive CTEs: Hierarchies ⭐⭐⭐

### 8-1) Everyone who reports to a manager, directly or indirectly

**"A recursive CTE: the anchor is the manager, the recursive member joins employees whose
manager is already in the result, and it stops when a pass adds no rows. I guard it against
cycles, because a bad row in the source makes it run forever."**

```sql
WITH RECURSIVE reports AS (
  -- anchor: the starting manager
  SELECT employee_id, manager_id, name, 0 AS depth, ARRAY[employee_id] AS path
  FROM   employees
  WHERE  employee_id = 2

  UNION ALL

  -- recursive member: the direct reports of everyone found so far
  SELECT e.employee_id, e.manager_id, e.name, r.depth + 1, r.path || ARRAY[e.employee_id]
  FROM   employees e
  JOIN   reports  r ON e.manager_id = r.employee_id
  WHERE  e.employee_id <> ALL (r.path)          -- cycle guard: never revisit a node
    AND  r.depth < 20                           -- depth guard: a backstop
)
SELECT employee_id, name, depth, path
FROM   reports
WHERE  depth > 0
ORDER  BY depth, employee_id;
```

Lin (2) manages Ned (4) and Oli (5).

- **How it runs** — the anchor produces the first working set; each pass joins the working set
  to `employees` and the new rows become the next working set; it ends when a pass returns
  nothing. `UNION ALL`, not `UNION`, unless you want duplicates removed every pass.
- **Cycles** — if A manages B and B manages A, the recursion never ends. The `path` array
  stops it at the first repeat; PostgreSQL 14+ also has the built-in `CYCLE employee_id SET
  is_cycle USING path` clause. A depth limit alone stops it too, but silently returns wrong
  rows, so use it only as a backstop.
- **The other direction** — the chain *up* to the CEO is the same query with the join flipped
  (`e.employee_id = r.manager_id`).
- **Where it runs** — PostgreSQL, MySQL 8, SQL Server, Snowflake, BigQuery, DuckDB, and Spark
  from 4.1 onward (earlier Spark has no recursive CTE, so the loop is written in the driver,
  one join per level). The engines share the recursion, not the dialect: the cycle guard above
  uses PostgreSQL's `ARRAY[…]`, `||` and `<> ALL`, so on Spark, BigQuery or Snowflake rewrite
  it with that engine's array functions (`array_contains`, `ARRAY_CONCAT`, …). A deep, wide hierarchy queried often is better flattened once into a
  closure table `(ancestor, descendant, depth)` so the question becomes a plain join.

---

## 9) Query Optimisation ⭐⭐⭐⭐⭐

### 9-1) Q9 — A query takes 30 minutes. How do you investigate?

**"Measure before changing anything. I read the actual plan — `EXPLAIN ANALYZE` in a database,
the Spark UI's SQL tab and stage view in Spark — and find the one node or stage where the time
goes. Then it is one of five things: reading too much, shuffling too much, spilling, skew, or a
bad estimate that picked the wrong plan."**

| Symptom in the plan or UI | Diagnosis | Fix |
|---|---|---|
| Scan reads far more rows / bytes than the result needs | no pruning, `SELECT *`, filter not pushed down | filter on the partition column, select columns, make the predicate sargable |
| Large shuffle read / write on a join or aggregate stage | moving the big table | aggregate first, broadcast the small side, bucket both sides |
| `Sort Method: external merge`, `Batches: 8`, Spark "spill (disk)" | operator out of memory | more memory per operator, more shuffle partitions, less data into it |
| A few tasks run 10× longer than the median | skew on a hot key | AQE skew join, isolate the hot key, salt |
| Estimated rows far from actual rows | stale or missing statistics | `ANALYZE`, extended statistics, rewrite the predicate |
| Nested loop over a large input | wrong join algorithm, usually from a bad estimate | fix the estimate; an index on the inner side |

Then **change one thing, re-run, and compare** the same metrics — and say it was the plan that
proved the fix, not the stopwatch. Also check the boring causes first: lock waits (the query
is not slow, it is blocked), a cold cache, a cluster that was shared with another job.
[`sql_explain.md`](./sql_explain.md) walks a full diagnosis node by node.

### 9-2) Q10 — Partition pruning vs predicate pushdown?

**"Both make the engine read less, at different levels. Partition pruning skips whole
partitions — directories or files — using the partition key, before any file is opened.
Predicate pushdown hands the filter to the storage layer so it skips row groups or rows inside
the files it does open."**

| | Partition pruning | Predicate pushdown |
|---|---|---|
| Works on | the partition column (`dt=2026-11-01/`) | any column the format keeps statistics for |
| Skips | whole partitions, from metadata alone | Parquet / ORC row groups by min / max, rows at the source (a JDBC `WHERE`) |
| Needs | a filter on the partition column itself | a simple comparison the reader understands |
| Broken by | a function on the column: `WHERE YEAR(dt) = 2026` | a UDF, a cast the reader cannot push, poorly sorted data (every row group spans every value) |

```sql
-- prunes: a range on the raw column
SELECT SUM(amount) FROM orders
WHERE  order_date >= DATE '2026-11-01' AND order_date < DATE '2026-12-01';

-- does not prune on most engines: the column is wrapped in a function
SELECT SUM(amount) FROM orders
WHERE  EXTRACT(YEAR FROM order_date) = 2026 AND EXTRACT(MONTH FROM order_date) = 11;
```

The second form also stops a database from using an index on `order_date` — it is not
**sargable**. Check either in the plan: Spark's scan node lists `PartitionFilters` and
`PushedFilters`; PostgreSQL shows the partitions it kept and whether the filter is an
`Index Cond` or a `Filter`.

### 9-3) Q11 — Why can `SELECT *` hurt performance?

**"On a columnar format it reads every column when you need three — the I/O is per column, so
the cost is proportional to the columns selected. It also moves more data through every
shuffle, uses more memory, prevents index-only scans, and breaks when the schema changes."**

- **I/O** — Parquet, ORC, BigQuery and Snowflake store each column separately; **column
  pruning** reads only the ones referenced. BigQuery bills by bytes scanned, so `SELECT *` is
  literally the bill.
- **Network and memory** — every extra column is carried through each shuffle, sort buffer and
  hash table, which is where spills come from.
- **Indexes** — a row store can answer from an index alone (an index-only scan) only if every
  selected column is in the index.
- **Correctness** — a column added upstream changes the result's shape; an `INSERT … SELECT *`
  breaks or, worse, silently shifts columns.

Fine in an exploratory `LIMIT 10` on a database; never in a pipeline or a view.

### 9-4) Q12 — When would you use a CTE instead of a subquery?

**"For readability almost always — a CTE names each step, can be referenced twice, and is the
only way to write recursion. For performance it is usually the same, because modern
optimisers inline a CTE like a subquery; the exceptions are what I would mention."**

| Engine | What happens to a CTE |
|---|---|
| PostgreSQL 12+ | inlined if referenced once; materialised if referenced twice, unless `NOT MATERIALIZED` |
| PostgreSQL ≤ 11 | always materialised — an *optimisation fence*: filters outside were not pushed in |
| Spark SQL | inlined; a CTE referenced twice is **computed twice** unless the plan reuses the exchange |
| BigQuery, Snowflake | generally inlined; a CTE used several times may be evaluated each time |

So: use CTEs for clarity; when an expensive CTE is referenced several times, check the plan and
materialise it on purpose — a temp table, `MATERIALIZED`, or `df.cache()` in Spark. A
**correlated** subquery is a different matter (§9-10).

### 9-5) Q13 — How do you optimise a query with many joins?

**"Shrink each input before it joins, join the most selective tables first so intermediate
results stay small, make sure no join fans out, and let the optimiser order the rest — which
needs good statistics."**

1. **Filter early** — push every filter into the CTE or subquery that reads the table, not
   after the last join. Optimisers push most of them down; not across an outer join or a
   window function.
2. **Aggregate early** — aggregate a fact to the grain the result needs before joining
   dimensions (§3-1).
3. **Selective joins first** — a join that cuts 1 B rows to 1 M should run before one that keeps
   them all. The optimiser orders joins from **cardinality estimates**, so stale statistics give
   a bad order; refresh them (`ANALYZE`, `ANALYZE TABLE … COMPUTE STATISTICS` in Spark, which
   the cost-based optimiser needs).
4. **No fan-out** — every dimension unique on its join key (§3-3).
5. **Broadcast the small dimensions** — in Spark, star joins with several small dimensions
   each broadcast, so the fact is shuffled once or not at all.
6. **Check the plan** — the join algorithm, the order, and the row count after each join.

### 9-6) Q14 — What causes data skew in a `GROUP BY` or `JOIN`?

**"Hash partitioning sends every row with the same key to the same task, so one hot key — a
NULL, a default value, one huge customer — puts most of the data on one task while the rest
finish and wait."**

| Cause | Example | Fix |
|---|---|---|
| NULL or sentinel keys | `customer_id IS NULL` for every guest order | filter them, or join them separately |
| Genuinely hot keys | one marketplace seller with 30 % of orders | AQE skew join; split the hot keys out and broadcast-join them; salt |
| Bad partitioning | too few shuffle partitions, or `repartition(col)` on a low-cardinality column | more partitions; AQE coalescing |
| Skewed input files | one huge gzip file (not splittable) | splittable formats; repartition after read |

For a `GROUP BY`, skew is often harmless because of **partial aggregation** — each task
pre-aggregates the hot key locally before the shuffle. It bites when the aggregate is not
decomposable (`COUNT(DISTINCT)`, `collect_list`, percentile), and on joins.

**Salting**, the manual fix: append a random number `0..N-1` to the hot key on the big side,
replicate the small side's matching rows `N` times with each salt, and join on `(key, salt)`.
The hot key's rows now spread over `N` tasks, at the cost of `N` copies of the small side's rows.

```sql
-- Spark SQL: salt the big side, explode the small side
WITH salted_orders AS (
  SELECT *, CAST(FLOOR(RAND() * 8) AS INT) AS salt FROM orders
),
salted_customers AS (
  SELECT c.*, s.salt FROM customers c
  CROSS JOIN (SELECT EXPLODE(SEQUENCE(0, 7)) AS salt) s
)
SELECT o.customer_id, SUM(o.amount) AS total_amount
FROM   salted_orders o
JOIN   salted_customers c ON c.customer_id = o.customer_id AND c.salt = o.salt
GROUP  BY o.customer_id;
```

In Spark 3, try AQE first (`spark.sql.adaptive.skewJoin.enabled`, on by default), which splits
oversized shuffle partitions at runtime without changing the query.

### 9-7) Q15 — How do indexes work, and when do they not help?

**"A B-tree index keeps the column's values sorted with a pointer to each row, so a lookup or a
narrow range is `O(log n)` plus the rows matched instead of a full scan. It stops helping when
the query matches a large share of the table, when the predicate cannot use it, and it always
costs on writes."**

An index does **not** help when:

- **The filter is not selective** — matching more than a few percent of rows, random index
  reads cost more than one sequential scan, and the planner correctly picks the scan.
- **The predicate is not sargable** — a function or cast on the column (`LOWER(email) = …`,
  `created_at::date = …`), a leading wildcard (`LIKE '%son'`), or an implicit type conversion.
  An expression index (`ON (LOWER(email))`) fixes the first.
- **The column is not the index's leading column** — an index on `(country, city)` does not
  serve `WHERE city = …`.
- **The table is tiny**, or the statistics are stale and the planner thinks it is.
- **The workload is analytical** — scanning billions of rows on a columnar warehouse. Those
  engines have no B-tree indexes; their equivalents are partitioning, clustering / sort keys and
  zone maps (min / max per block).

The costs: every `INSERT`, `UPDATE` and `DELETE` maintains every index, indexes take disk and
memory, and a bulk load is often faster with indexes dropped and rebuilt afterwards. A
**covering** index (`INCLUDE (amount)`) makes an index-only scan possible.

### 9-8) Q16 — A query scans several terabytes. How do you make it cheaper?

**"Read less first: prune partitions, prune columns, push filters into the files. Then store
it better: columnar files of a sensible size, sorted or clustered on the filter columns. Then
compute less: pre-aggregate what is queried every day."**

| Lever | What it does | Example |
|---|---|---|
| Partition pruning | skips whole days / regions | `WHERE dt BETWEEN …` on a `dt`-partitioned table |
| Column pruning | reads only the referenced columns | no `SELECT *` |
| Clustering / sorting / Z-order | makes min / max statistics selective so row groups are skipped | Delta `ZORDER BY (customer_id)`, BigQuery clustering, Snowflake clustering keys |
| Columnar format + compression | fewer bytes per value | Parquet / ORC with Zstd or Snappy instead of CSV / JSON |
| Healthy file sizes | fewer files to open and plan | compact many small files to ~128 MB–1 GB |
| Pre-aggregation | the dashboard reads a summary, not the facts | a daily aggregate table or a materialised view |
| Approximation | sketches instead of exact distincts | `approx_count_distinct` |
| Incremental processing | each run reads only the new partitions | §10-1 |

Then **prove it**: compare bytes scanned (BigQuery's job info, Snowflake's query profile,
Spark's scan metrics) before and after. [`data_performance.md`](./data_performance.md) is the
long version.

### 9-9) A Spark SQL job: most tasks take 2 minutes, a few take 20

> A Spark SQL job runs 40 minutes. Most tasks in a stage finish in 2 minutes, a few take 20,
> and one stage's shuffle read is unusually large. Skew, a bad partition count, or the wrong
> join strategy — how do you tell?

**"The task-time distribution tells me. A few slow tasks with far more shuffle read than the
median is skew. Every task slow, or a stage with very few tasks, is a partition count problem.
A sort-merge join where one side is small enough to broadcast is a strategy problem. I check
in that order, and only then pick the fix."**

1. **Stage and task view** in the Spark UI: the task summary's **max vs median** duration.
   A long tail (20 min against 2) points at skew or a straggler; uniform slowness does not.
2. **Compare per-task shuffle read, records and spill.** If the slow tasks also read 10× the
   records, it is **data skew**. If they read the same as the others, it is a **straggler** —
   a bad node, GC pauses (check GC time), or a slow disk — and speculation
   (`spark.speculation`) helps.
3. **Find the hot keys** of that stage's join or group key:
   `SELECT key, COUNT(*) FROM t GROUP BY key ORDER BY 2 DESC LIMIT 20`. NULLs and sentinels
   first.
4. **Read the physical plan** (`EXPLAIN FORMATTED`, or the SQL tab): is it `SortMergeJoin` or
   `BroadcastHashJoin`? Did AQE kick in (`AQEShuffleRead`, `skew=true`)? What are the input
   sizes on each side?
5. **Decide**:

| Evidence | Diagnosis | Fix |
|---|---|---|
| Few tasks with most of the records; a hot key | data skew | AQE skew join; filter or separately handle NULL keys; broadcast; salting |
| Tasks all large and spilling; e.g. 200 partitions for 2 TB | too few shuffle partitions | raise `spark.sql.shuffle.partitions`, or let AQE coalesce from a high number; aim for ~100–200 MB per partition |
| Thousands of tiny tasks, scheduling overhead dominates | too many partitions | AQE coalescing; fewer partitions |
| A sort-merge join with one small side | wrong join strategy | broadcast it (hint, or raise the threshold), refresh statistics so the optimiser sees the size |
| Slow tasks read no more than the others | stragglers | check executor GC and the node; enable speculation |

Interview advice that the question invites: if you have done this on a real job, say which of
these you actually did and what it measured before and after, and keep that separate from the
fixes you would *also* consider.

### 9-10) Two anti-patterns to name: correlated subqueries and functions on partition columns

**Correlated subquery** — a subquery that references the outer row and is, logically, run once
per row:

```sql
-- anti-pattern: a per-row subquery in the SELECT list
SELECT c.customer_id,
       (SELECT SUM(o.amount) FROM orders o WHERE o.customer_id = c.customer_id) AS total
FROM   customers c;

-- rewrite: one aggregate, one join
SELECT c.customer_id, t.total
FROM   customers c
LEFT   JOIN (SELECT customer_id, SUM(amount) AS total
             FROM orders GROUP BY customer_id) t USING (customer_id);
```

Many optimisers **decorrelate** the simple cases into a join automatically (Spark, Snowflake,
DuckDB, SQL Server); PostgreSQL does it for `EXISTS` / `IN` but runs a scalar subquery in the
`SELECT` list as a per-row SubPlan. The rewrite is never slower and does not depend on the
optimiser being clever — and a "previous value per row" correlated subquery is better as
`LAG`.

**A function on a partition or indexed column** — `WHERE YEAR(created_at) = 2026` cannot prune
partitions or use an index; `WHERE created_at >= '2026-01-01' AND created_at < '2027-01-01'`
can (§9-2). The half-open range is also correct for timestamps, where `BETWEEN … AND
'2026-12-31'` loses the last day after midnight.

---

## 10) Incremental ETL and Data Quality ⭐⭐⭐⭐⭐

### 10-1) Q7 — Process only what changed since the last run

> Every day, read the rows added or updated in a source table since the previous run.

**"A high-water mark: store the largest change marker the last successful run processed, read
rows above it — minus a lookback for late arrivals — dedupe to one version per key, and `MERGE`
into the target. The `MERGE` makes a rerun harmless, and the watermark only advances after the
target commits."**

```sql
CREATE TABLE etl_watermark (job TEXT PRIMARY KEY, high_water TIMESTAMP NOT NULL);
INSERT INTO etl_watermark VALUES ('orders_sync', TIMESTAMP '2026-11-01 00:00');

CREATE TABLE src_orders (order_id INT, status TEXT, amount NUMERIC(10, 2),
                         updated_at TIMESTAMP NOT NULL,
                         change_seq BIGINT    NOT NULL);   -- change_seq: the source's order
INSERT INTO src_orders VALUES
  (1, 'paid',     30, TIMESTAMP '2026-11-01 10:00', 1),
  (1, 'refunded', 30, TIMESTAMP '2026-11-01 15:00', 3),  -- two versions of one key in the batch
  (2, 'paid',     50, TIMESTAMP '2026-11-01 11:00', 2),
  (2, 'paid',     55, TIMESTAMP '2026-11-01 11:00', 4);  -- a correction with the same timestamp

CREATE TABLE dim_orders (order_id INT PRIMARY KEY, status TEXT, amount NUMERIC(10, 2),
                         updated_at TIMESTAMP NOT NULL, change_seq BIGINT NOT NULL);

BEGIN;

-- 1. snapshot the batch once: above the watermark (minus a lookback), one version per key
DROP TABLE IF EXISTS stg_orders;                       -- safe to rerun in the same session
CREATE TEMP TABLE stg_orders AS
SELECT order_id, status, amount, updated_at, change_seq
FROM (
  SELECT s.*, ROW_NUMBER() OVER (PARTITION BY order_id
                                 ORDER BY updated_at DESC, change_seq DESC) AS rn
  FROM   src_orders s
  WHERE  s.updated_at > (SELECT high_water - INTERVAL '1 hour'
                         FROM etl_watermark WHERE job = 'orders_sync')
) b
WHERE rn = 1;

-- 2. replace only with a newer version, so replaying an old batch changes nothing
MERGE INTO dim_orders t
USING stg_orders s
ON t.order_id = s.order_id
WHEN MATCHED AND (s.updated_at, s.change_seq) > (t.updated_at, t.change_seq) THEN
  UPDATE SET status = s.status, amount = s.amount,
             updated_at = s.updated_at, change_seq = s.change_seq
WHEN NOT MATCHED THEN
  INSERT (order_id, status, amount, updated_at, change_seq)
  VALUES (s.order_id, s.status, s.amount, s.updated_at, s.change_seq);

-- 3. advance the watermark to the newest row of the batch that was merged
UPDATE etl_watermark
SET    high_water = GREATEST(high_water,
                             COALESCE((SELECT MAX(updated_at) FROM stg_orders), high_water))
WHERE  job = 'orders_sync';                            -- never moves backwards

DROP TABLE stg_orders;
COMMIT;
```

Order 1 ends `refunded`; order 2 ends at 55, because the version order is `(updated_at,
change_seq)` — a timestamp alone would pick either 11:00 row, and a strict `updated_at >` would
reject the same-timestamp correction on the next run.

Both markers are `NOT NULL`. A `NULL` `change_seq` would sort **first** under `DESC` in
PostgreSQL and make the tuple comparison `UNKNOWN`, so a same-timestamp correction would be
silently rejected — a source that can emit NULL markers fails validation in staging rather than
reaching the `MERGE` or the watermark.

**The watermark comes from the staging table, not from a second read of `src_orders`.** Under
PostgreSQL's default `READ COMMITTED`, each statement sees a new snapshot even inside one
transaction: a row committed to the source between the `MERGE` and the `UPDATE` would push the
watermark past a row the `MERGE` never read, and the next run would skip it for good. Staging
the batch pins one snapshot for both. On a lakehouse, where the target and the watermark live
in different systems, the guarantee comes from the `MERGE` being idempotent instead.

The production shape of the same job:

```text
source table / CDC stream
  -> staging table              the raw batch, tagged with a batch_id
  -> dedupe + validate          one version per key; reject rows that fail checks
  -> MERGE INTO target          idempotent: a replayed batch rewrites the same rows
  -> commit watermark + audit   only now advance the watermark; record batch_id,
                                rows read / inserted / updated, processing time
```

The audit row is what makes a later reconciliation possible (§10-3). And `MERGE` alone is not
exactly-once for the whole pipeline: it still needs a deduplicated source, transactional writes,
no two jobs merging into the same table at once, and the watermark updated after the commit.

**Follow-up — how do you define the watermark?** As a **change marker from the source**, never
the job's own clock: the source's `updated_at`, or better a monotonic sequence — a CDC log
position (LSN, binlog offset), a Kafka offset, an auto-increment id for append-only tables.
Store it in a control table keyed by job, set it to the maximum value **actually read**, and
make the comparison strict (`>`) only when the marker is unique; with a timestamp, many rows can
share the boundary value, so read `>=` (or use a lookback) and let the `MERGE` absorb the
re-read rows. Two classic traps: `updated_at` set at the **start** of a long source transaction
commits *after* rows with later timestamps, so a strict `>` skips it; and a column the
application forgets to update on some code path means those changes are never seen — which is
why log-based CDC beats a timestamp column.

**Follow-up — data arrives late. What then?** Pick by how late, and say the trade-off:

| Approach | How | Cost |
|---|---|---|
| Lookback window | read from `watermark − 1 hour` (or − 3 days) every run | re-reads rows; harmless because the `MERGE` is idempotent |
| Reprocess event-time partitions | recompute the last N days' partitions every run | simple; costs N days of compute daily |
| Route late rows to their partition | a late row updates the partition of its event date | later reports change; downstream needs to know |
| Cut-off | after N days a period is closed; later rows go to an adjustment | stable reports, explicitly approximate |

A lookback only covers lateness up to its length. Anything later must come back through a
periodic reconciliation against the source, or through CDC, which does not depend on
timestamps at all.

Distinguish **arrival time** (when the row reached us, which drives the watermark) from
**event time** (when it happened, which decides the partition and the metric).

**Follow-up — the job fails and reruns. How do you avoid duplicates?** Make every write
**idempotent**: `MERGE` on the business key, or `INSERT OVERWRITE` / delete-and-insert of
whole partitions — never a blind `INSERT`. Advance the watermark **after** the write commits,
in the same transaction when possible; if the job dies between the two, the rerun reads the
same batch and the `MERGE` rewrites the same rows. Then verify: a uniqueness check on the key
after each run (§10-3).

**Follow-up — several updates to one key in the same batch?** Keep the latest by the source's
ordering (`ROW_NUMBER() … ORDER BY updated_at DESC, seq DESC`) **before** the `MERGE`, as above
— otherwise the `MERGE` fails with two source rows for one target row, or, on an engine that
allows it, applies them in an arbitrary order. If the target needs every version (a history
table, SCD 2), keep them all and order them instead (§10-2).

**Follow-up — write the `MERGE`.** The statement above, plus a delete branch when the source
is CDC with an operation column: `WHEN MATCHED AND s.op = 'D' THEN DELETE`. Keep the `ON`
condition on the key alone and put the "is newer" test in the `WHEN` clause; on a partitioned
lakehouse table add the partition range to `ON` so the `MERGE` reads only the touched
partitions.

### 10-2) Q8 — SCD Type 2: keep every address a customer has had

**"Each change closes the current row — sets `valid_to` and `is_current = false` — and inserts
a new current row. Both statements go in one transaction, both only touch customers whose
tracked attributes really changed, and a unique constraint on the current row makes a
double-run impossible rather than unlikely."**

**Follow-up — Type 1 vs Type 2?**

| | SCD Type 1 | SCD Type 2 |
|---|---|---|
| On change | overwrite the attribute | close the old row, insert a new version |
| History | lost | every version, with `valid_from` / `valid_to` |
| Rows per key | one | one per version, exactly one current |
| Key | the natural key | a surrogate key per version (facts reference the version) |
| Use for | corrections, attributes nobody reports on historically | attributes that change what a past fact means: address, segment, price tier |

Type 3 keeps one "previous value" column; real tables usually mix types per column.

```sql
CREATE TABLE dim_customer (
  customer_sk  INT PRIMARY KEY,
  customer_id  INT NOT NULL,
  address      TEXT NOT NULL,
  valid_from   DATE NOT NULL,
  valid_to     DATE,                    -- NULL while current; exclusive end otherwise
  is_current   BOOLEAN NOT NULL
);
INSERT INTO dim_customer VALUES
  (1, 101, 'Taipei', DATE '2025-01-01', NULL, TRUE),
  (2, 102, 'Osaka',  DATE '2025-03-01', NULL, TRUE);

INSERT INTO dim_customer VALUES
  (3, 104, 'Busan',  DATE '2025-05-01', NULL, TRUE);

CREATE TABLE stg_customer (customer_id INT, address TEXT, change_date DATE, change_seq BIGINT);
INSERT INTO stg_customer VALUES
  (101, 'Kyoto',   DATE '2025-07-01', 1),  -- moved twice on one day:
  (101, 'Tokyo',   DATE '2025-07-01', 2),  --   the day's last change wins
  (101, 'Nagoya',  DATE '2025-07-03', 3),  -- and again two days later: a version of its own
  (102, 'Osaka',   DATE '2025-07-01', 4),  -- unchanged
  (103, 'Seoul',   DATE '2025-07-01', 5),  -- new customer
  (104, 'Incheon', DATE '2025-04-01', 6);  -- late: older than the current Busan row

-- changes too old to apply here wait for the history rebuild (kept across runs)
CREATE TABLE dim_customer_late (customer_id INT, address TEXT, change_date DATE);

BEGIN;
DROP TABLE IF EXISTS stg_daily;                -- safe to rerun in the same session
DROP TABLE IF EXISTS stg_chain;

-- 0. one change per customer per day: the day's last
CREATE TEMP TABLE stg_daily AS
SELECT customer_id, address, change_date
FROM (
  SELECT s.*, ROW_NUMBER() OVER (PARTITION BY customer_id, change_date
                                 ORDER BY change_seq DESC) AS rn
  FROM   stg_customer s
) t
WHERE rn = 1;

-- 1. route late changes: dated on or before the current row's start and different from the
--    version valid on that day (a replayed change matches it and is skipped)
INSERT INTO dim_customer_late
SELECT s.customer_id, s.address, s.change_date
FROM   stg_daily s
JOIN   dim_customer cur ON cur.customer_id = s.customer_id AND cur.is_current
LEFT   JOIN dim_customer h ON h.customer_id = s.customer_id
                          AND h.valid_from <= s.change_date
                          AND (s.change_date < h.valid_to OR h.valid_to IS NULL)
WHERE  s.change_date <= cur.valid_from
  AND  h.address IS DISTINCT FROM s.address
  AND  NOT EXISTS (SELECT 1 FROM dim_customer_late l
                   WHERE l.customer_id = s.customer_id AND l.change_date = s.change_date
                     AND l.address = s.address);

-- 2. the new versions: changes after the current row's start, minus no-ops, each one ending
--    where the next begins
CREATE TEMP TABLE stg_chain AS
WITH newer AS (
  SELECT s.customer_id, s.address, s.change_date,
         COALESCE(LAG(s.address) OVER (PARTITION BY s.customer_id ORDER BY s.change_date),
                  cur.address) AS prev_address
  FROM   stg_daily s
  LEFT   JOIN dim_customer cur ON cur.customer_id = s.customer_id AND cur.is_current
  WHERE  cur.customer_id IS NULL OR s.change_date > cur.valid_from
),
changed AS (
  SELECT * FROM newer WHERE address IS DISTINCT FROM prev_address
)
SELECT customer_id, address, change_date AS valid_from,
       LEAD(change_date) OVER (PARTITION BY customer_id ORDER BY change_date) AS valid_to
FROM   changed;

-- 3. close each changed customer's current row where its first new version starts
UPDATE dim_customer d
SET    valid_to = c.first_from, is_current = FALSE
FROM   (SELECT customer_id, MIN(valid_from) AS first_from
        FROM stg_chain GROUP BY customer_id) c
WHERE  d.customer_id = c.customer_id
  AND  d.is_current;

-- 4. insert the chain; only its last version is current
INSERT INTO dim_customer
SELECT (SELECT COALESCE(MAX(customer_sk), 0) FROM dim_customer)
         + ROW_NUMBER() OVER (ORDER BY customer_id, valid_from),
       customer_id, address, valid_from, valid_to, valid_to IS NULL
FROM   stg_chain;

DROP TABLE stg_daily;
DROP TABLE stg_chain;
COMMIT;
```

After the run customer 101 has Taipei closed on 07-01, Tokyo from 07-01 to 07-03 (Kyoto lost to
Tokyo on the same day) and Nagoya current from 07-03; 102 is untouched, 103 is new, and 104's
Incheon change waits in `dim_customer_late` for the history rebuild while Busan stays current.
`BEGIN` / `COMMIT` matter: in autocommit mode the close would commit on its own, and a failed
insert would leave the customer with no current row. (Use a sequence or identity column for
`customer_sk` in a real table; the `MAX + ROW_NUMBER` keeps the example portable.)

The invariant — at most one current row per customer — is enforced by the database, not by the
care taken above:

```sql
-- PostgreSQL only: a partial unique index (DuckDB, Spark and most warehouses have none)
CREATE UNIQUE INDEX dim_customer_one_current ON dim_customer (customer_id) WHERE is_current;
```

**Follow-up — how does the new version arrive?** Exactly as above: step 2 turns the batch
into versions — each change compared with the one before it (or the current row) so a no-op is
dropped, and each version ending where the next begins — step 3 closes the current row at the
first new version, and step 4 inserts them all. A brand-new customer has no current row, so it
only gets step 4. The order matters — close first, then insert. On Delta or
Snowflake the same thing is often written as **one `MERGE`** whose source is the staging rows
unioned with a copy of each changed row under a `NULL` merge key, so the changed customer both
matches (close) and does not match (insert) in a single atomic statement.

**Follow-up — a rerun must not create two current rows.** Three layers:

1. **The statements are idempotent.** Rerun with the same staging data: every change is now
   on or before its customer's current start, and equal to the version valid that day, so step 1
   routes nothing new and step 2 builds an empty chain — steps 3 and 4 do nothing.
2. **One transaction** around both statements, so a failure between them cannot leave a closed
   row with no successor.
3. **A constraint that makes the bad state impossible** — the partial unique index above.
   Warehouses without one run the validation checks below after the load and fail the job.

Compare with `IS DISTINCT FROM` (null-safe) — `address <> s.address` is `UNKNOWN` when either
side is NULL, so a change to or from NULL is silently missed. With many tracked columns, compare
a **hash of the tracked columns** rather than each column.

**Follow-up — how do you validate the history?** Three checks, each returning rows only when the
dimension is broken:

```sql
-- 1. exactly one current row per customer: catches none as well as two
SELECT customer_id, COUNT(*) FILTER (WHERE is_current) AS current_rows
FROM   dim_customer
GROUP  BY customer_id
HAVING COUNT(*) FILTER (WHERE is_current) <> 1;

-- 2. the current row is open; a closed row ends after it starts
SELECT * FROM dim_customer
WHERE  (is_current AND valid_to IS NOT NULL)
   OR  (NOT is_current AND (valid_to IS NULL OR valid_to <= valid_from));

-- 3. no gap or overlap: each version ends exactly where the next one starts
SELECT customer_id, valid_from, valid_to, next_from
FROM (
  SELECT customer_id, valid_from, valid_to,
         LEAD(valid_from) OVER (PARTITION BY customer_id ORDER BY valid_from) AS next_from
  FROM   dim_customer
) t
WHERE  next_from IS NOT NULL AND valid_to IS DISTINCT FROM next_from;
```

**Follow-up — several changes on the same day?** Decide the grain with the business first:

- **Day grain** — what the example does: step 0 keeps the day's last change per customer, so
  Kyoto loses to Tokyo, while changes on **different** days stay separate versions (Tokyo, then
  Nagoya). Deduping the batch to one row per customer instead would silently stretch Tokyo
  over Nagoya's days.
- **Every change** — the same load with `valid_from` / `valid_to` as timestamps and step 0
  partitioned by the change's timestamp (a sequence breaks ties), so every change is a version.
- **A late change** older than the current version must be **inserted into the middle** of the
  history: split the version it falls in, which rewrites `valid_to` on its predecessor. That is
  why step 1 parks it in `dim_customer_late` instead of applying it — comparing it with the
  version **valid on its date**, not the current one, so a late change back to today's address
  is still caught. A batch job that rebuilds those keys' intervals from the full change log with
  `LEAD` handles it cleanly ([`sql_for_data.md`](./sql_for_data.md) §7-14).

Use **half-open intervals** — `valid_from <= t AND (t < valid_to OR valid_to IS NULL)` — with
`valid_to` equal to the next version's `valid_from`, rather than "the day before". The
question's example table closes Taipei on 2025-06-30 (inclusive); that works for dates, but with
timestamps an inclusive end leaves a gap, so say which convention you use. A far-future
`valid_to` (`9999-12-31`) instead of `NULL` lets the condition be a plain `BETWEEN`-style range.

### 10-3) Reconciliation: proving a load is correct

**"After every load I compare the target with the source on counts, sums and keys, and run the
assertions that describe the table — unique key, no NULL keys, values in range. Each one
returns rows only when something is wrong, so the orchestrator can fail the run."**

```sql
-- every query returns zero rows on a healthy load
-- 1. one row per key
SELECT order_id, COUNT(*) FROM dim_orders GROUP BY order_id HAVING COUNT(*) > 1;

-- 2. every source key arrived
SELECT DISTINCT s.order_id FROM src_orders s
WHERE  NOT EXISTS (SELECT 1 FROM dim_orders d WHERE d.order_id = s.order_id);

-- 3. totals agree with the source's latest versions
WITH latest AS (
  SELECT DISTINCT ON (order_id) order_id, amount
  FROM   src_orders ORDER BY order_id, updated_at DESC, change_seq DESC
)
SELECT (SELECT SUM(amount) FROM latest) AS source_total,
       (SELECT SUM(amount) FROM dim_orders) AS target_total
WHERE  (SELECT SUM(amount) FROM latest) IS DISTINCT FROM (SELECT SUM(amount) FROM dim_orders);
```

The usual set: row counts per partition against the source, sums of the money columns, key
uniqueness and referential integrity (every fact's key in its dimension), freshness (the
newest timestamp is recent), and volume against the trailing average (a day at 10 % of normal
is a failure even when every check above passes). [`batch_pipeline.md`](./batch_pipeline.md)
and [`sql_for_data.md`](./sql_for_data.md) §7-16 have more.

---

## 11) The Performance Cheat Sheet ⭐⭐⭐⭐

| Technique | Solves | Watch out for |
|---|---|---|
| Partition pruning | scanning partitions the query does not need | the filter must be on the partition column, unwrapped |
| Predicate pushdown | reading rows / row groups that fail the filter | depends on the format, the reader and how the data is sorted |
| Column pruning | reading columns nobody selected | `SELECT *` defeats it |
| Broadcast join | shuffling a big table to join a small one | the small side must fit in every executor's memory |
| Sort-merge join | joining two large tables | a shuffle and a sort of both sides; sensitive to skew |
| Bucketing | re-shuffling the same join every day | both sides bucketed on the key with compatible bucket counts; engine-specific |
| Materialised view | recomputing the same aggregate per query | refresh cost and staleness |
| Approximate aggregation | the cost of exact distinct counts | the answer is approximate (~1–2 %); not for money |

Three pairs that come up as one-line questions:

- **`WHERE` vs `HAVING`** — `WHERE` filters rows before grouping and cannot use an aggregate;
  `HAVING` filters groups after. A condition on a plain column belongs in `WHERE`, where it
  also cuts the data before the aggregate.
- **`UNION` vs `UNION ALL`** — `UNION` removes duplicates, which costs a sort or hash of the
  whole result; `UNION ALL` keeps every row. Default to `UNION ALL` and dedupe on purpose.
- **`ROWS` vs `RANGE`** — `ROWS` frames by physical row count; `RANGE` by the ordering value,
  treating equal values as one peer group (§5-2).

---

## 12) The Practice Plan ⭐⭐⭐

**Priority order** for a senior DE loop:

1. **SQL coding** — window functions, dedupe, sessions, rolling aggregates, multi-table joins.
2. **SQL optimisation** — reading a plan, join strategy, partition pruning, skew and shuffle.
3. **Data engineering SQL** — incremental loads, CDC and `MERGE`, SCD 1 / 2, data quality and
   reconciliation.
4. **Database fundamentals** — indexes, transactions and ACID, isolation levels,
   normalisation vs denormalisation ([`sql_vs_nosql.md`](./sql_vs_nosql.md)).

**The ten drills to be able to write cold**, each answered above:

| # | Drill | Technique | Answered in |
|---|---|---|---|
| 1 | Each user's latest event | `ROW_NUMBER` + tie-breaker | §4-1 |
| 2 | Users with three consecutive login days | gaps and islands | §7-1 |
| 3 | Top three salaries per department | `DENSE_RANK` | §5-1 |
| 4 | Daily revenue with a 7-day moving average | window frame + date spine | §5-2 |
| 5 | Rows with no matching record | `NOT EXISTS` | §3-2 |
| 6 | DAU / WAU / MAU | `COUNT(DISTINCT)` over ranges | §6-3 |
| 7 | Duplicate transactions, keep the latest | dedupe | §4-1, §4-2 |
| 8 | Each user's first and latest purchase | `MIN` / `MAX` | §6-4 |
| 9 | Sessions with a 30-minute timeout | `LAG` + running `SUM` | §7-2 |
| 10 | Daily rows into a monthly report | conditional aggregation | §6-5 |

For each one, rehearse the four questions from §1 out loud: *1 TB? why slow? how do you know
it is right? what if it reruns?*

**Q1–Q8 at a glance** — what each question tests, and the words a senior answer uses:

| Question | Tests | Senior keywords |
|---|---|---|
| Q1 Large join (§3-1) | join optimisation | aggregate first, shuffle, skew, broadcast |
| Q2 Deduplication (§4-1) | keeping the latest version | `ROW_NUMBER`, deterministic tie-breaker |
| Q3 Anti-join (§3-2) | finding what is missing | `NOT EXISTS`, the `NOT IN` NULL trap |
| Q4 Ranking (§5-1) | ranking and ties | `RANK`, `DENSE_RANK`, distinct levels vs people |
| Q5 Rolling average (§5-2) | window aggregation | `ROWS` vs `RANGE`, calendar |
| Q6 Conversion rate (§6-1) | conditional aggregation | `COUNT(DISTINCT)`, one cohort for both parts |
| Q7 Incremental ETL (§10-1) | loading only the changes | CDC, watermark, `MERGE`, idempotency |
| Q8 SCD Type 2 (§10-2) | keeping history | validity intervals, one current row, reruns |

If time is short, master **Q1, Q2, Q5, Q7 and Q8** first: together they cover large-scale
Spark work, ETL dedupe, window functions, production pipelines and history modeling. For each,
be ready to say why you chose the pattern, how correctness is guaranteed, where the bottleneck
is at a billion rows, whether the result survives a retry or late data, and how you would verify
it — the plan, a reconciliation, a metric.

**Where to practise**:

- [LeetCode SQL 50](https://leetcode.com/studyplan/top-sql-50/) for the fundamentals, then the
  Hard database problems: [262 Trips and Users](https://leetcode.com/problems/trips-and-users/),
  [601 Human Traffic of Stadium](https://leetcode.com/problems/human-traffic-of-stadium/),
  [185 Department Top Three Salaries](https://leetcode.com/problems/department-top-three-salaries/).
- [DataLemur](https://datalemur.com/) and [StrataScratch](https://www.stratascratch.com/) for
  questions asked at named companies, closer to real product data.
- The [Mode SQL tutorial](https://mode.com/sql-tutorial/) to review analytical SQL and window
  functions.

---

## 13) Saying It in English: Q1–Q8 ⭐⭐⭐⭐

The answer to each question, at the length you would say it out loud before the follow-ups
start. Practise them as written; the sections above hold the detail behind every sentence.

**Q1 — large table join**

> I would first reduce the input by filtering and pre-aggregating the fact table to one row per
> customer. Then I would read the execution plan to see the join strategy and the shuffle. With
> two large tables I expect a sort-merge join; I would check the join key for skew and use
> adaptive query execution or salting if one key dominates.

**Q2 — deduplication**

> I would use `ROW_NUMBER` partitioned by `event_id`, ordered by `updated_at` descending, and
> keep row number one. To make the result deterministic I add a stable tie-breaker such as a
> source sequence. At a billion rows the cost is the shuffle and sort by `event_id`, so for
> continuous data I dedupe each new batch and `MERGE` it into an already-unique table.

**Q3 — customers without orders**

> I would use `NOT EXISTS`, or a left anti join, because it states the requirement directly and
> is correct when the subquery has NULLs. I avoid `NOT IN`: one NULL in the subquery makes it
> return no rows. And I would compare the execution plans rather than assume one form is faster.

**Q4 — second-highest salary**

> I would use `DENSE_RANK` partitioned by department, ordered by salary descending, and keep rank
> two. That handles ties when the requirement is the second-highest distinct salary. I would
> first confirm whether the business means the second distinct salary level or the employee in
> second place, because ties give different answers.

**Q5 — rolling average**

> I would aggregate to one row per date, join it to a calendar table to fill missing dates —
> using zero only if a missing date really means zero revenue — and then apply a seven-row
> window over the complete series. That avoids confusing seven records with seven calendar days.

**Q6 — conversion rate**

> I would use conditional aggregation with `COUNT(DISTINCT user_id)` per event type and `NULLIF`
> to avoid dividing by zero. More importantly, I would clarify the definition: if a conversion
> means a user who visited and purchased on the same day, I aggregate to the user-day level
> first so that the numerator and the denominator are the same cohort.

**Q7 — incremental processing**

> I would read changes above a watermark taken from a reliable change timestamp or CDC offset,
> with a lookback for late data. I load them into staging, deduplicate and validate them, then
> `MERGE` into the target so a retry rewrites the same rows. The watermark advances only after
> the write commits, and every batch records its row counts for reconciliation.

**Q8 — SCD Type 2**

> When a tracked attribute changes, I expire the current record by setting its end date and
> inserting a new current version, both in one transaction. I handle duplicate source events,
> late-arriving updates and retries, and I validate that every customer has exactly one current
> record and that the validity periods neither overlap nor leave gaps.
