# Reading SQL `EXPLAIN` Plans

> **Scope** — reading query plans as a data engineering interview probes it: what `EXPLAIN`, `EXPLAIN ANALYZE` and `BUFFERS` report, how to walk the plan tree, scan nodes, estimated vs actual rows, the three join algorithms as they appear in a plan, sort and hash spills, buffer and I/O counters, a worked slow-query diagnosis, and how the same reading carries over to Spark SQL. Answered for PostgreSQL 16–18. Writing the SQL itself, index design and warehouse levers are [sql_for_data.md](./sql_for_data.md)'s; Spark's execution model and tuning are [spark.md](./spark.md)'s.
> **See also**: [`./sql_for_data.md`](./sql_for_data.md) — §10 Query Performance, the short version of this doc; [`./spark.md`](./spark.md) — shuffles, AQE and skew behind a Spark plan; [`../db/faq_DB_DW.md`](../db/faq_DB_DW.md) — MySQL's `EXPLAIN` `type` column.

SQL says **what** data you want; the plan is how the database decided to **get** it. `EXPLAIN`
is how you read that decision, and `EXPLAIN ANALYZE` is how you check it against what actually
happened. Every plan below is PostgreSQL unless the section says otherwise, and every plan is
trimmed to the lines that matter.

---

## 0) The Short Answer ⭐⭐⭐⭐⭐

**"I run `EXPLAIN (ANALYZE, BUFFERS)`, read the tree bottom-up, and look first for the node
where estimated rows and actual rows diverge — a bad estimate is the root cause of most bad
plans. Then I check the scans for data read and thrown away, the joins for the wrong
algorithm, and sorts or hashes for spills to disk. I change one thing at a time and re-run."**

A plan answers six questions:

1. Why did the optimiser pick this strategy?
2. Which node spends the most time?
3. Is the query reading data it then throws away?
4. Is a join using the wrong algorithm?
5. Are the optimiser's row estimates right?
6. Where, therefore, is the real bottleneck?

---

## 1) What `EXPLAIN` Shows ⭐⭐⭐⭐⭐

### 1-1) From SQL to plan

**The optimiser, not the SQL, decides the scan, the join order, the join algorithm and whether
to sort — and it decides from statistics, not from the data.**

```text
SQL text
  -> Parser / analyser   syntax, tables, columns, types
  -> Rewriter            views, rules
  -> Planner/optimiser   enumerates scans, join orders, join methods; costs each
  -> Plan                a tree of operators (nodes)
  -> Executor            runs the tree, returns rows
```

The cost model is fed by table **statistics** (row counts, distinct values, most common values,
histograms — collected by `ANALYZE` / autovacuum), the available **indexes**, and cost settings
such as `seq_page_cost` and `random_page_cost`. When the statistics are wrong, the plan is
chosen for data that does not exist.

### 1-2) `EXPLAIN` vs `EXPLAIN ANALYZE` vs `BUFFERS` ⭐⭐⭐⭐⭐

**`EXPLAIN` predicts; `EXPLAIN ANALYZE` runs the query and reports what happened; `BUFFERS`
adds how many 8 KB pages each node touched.**

| Command | Runs the query? | Shows |
|---|---|---|
| `EXPLAIN` | no | the chosen plan with estimated cost, rows and width |
| `EXPLAIN ANALYZE` | **yes** | the plan plus actual time, actual rows and loops per node |
| `EXPLAIN (ANALYZE, BUFFERS)` | **yes** | plus shared / local / temp page counts per node |
| `EXPLAIN (FORMAT JSON)` | no (unless `ANALYZE`) | the same plan as JSON, for plan-visualiser tools |

- PostgreSQL 18 includes `BUFFERS` automatically whenever `ANALYZE` is given.
- Use plain `EXPLAIN` when the query is too slow or too dangerous to run; use `ANALYZE` for
  everything else, because only it shows where the estimates went wrong.

### 1-3) Reading one node: cost, rows, width

```sql
EXPLAIN
SELECT *
FROM   orders
WHERE  order_date >= DATE '2026-10-01';
```

```text
Seq Scan on orders  (cost=0.00..18000.00 rows=500000 width=64)
  Filter: (order_date >= '2026-10-01'::date)
```

| Field | Meaning |
|---|---|
| `Seq Scan on orders` | the node type and the table it reads |
| `cost=0.00..18000.00` | **startup cost .. total cost**: the cost before the first row, and to return all rows |
| `rows=500000` | estimated rows this node **outputs** (after its filter) |
| `width=64` | estimated average bytes per output row |
| `Filter:` | a condition checked on every row the node reads — rows that fail are discarded |

**`cost` is not milliseconds.** It is in arbitrary units where reading one page sequentially
costs `seq_page_cost` = 1.0. It is useful only for comparing plans for the same query on the
same server. A parent's cost includes its children's.

### 1-4) What `ANALYZE` adds: actual time, rows, loops

```sql
EXPLAIN ANALYZE
SELECT *
FROM   orders
WHERE  order_date >= DATE '2026-10-01';
```

```text
Seq Scan on orders  (cost=0.00..18000.00 rows=500000 width=64)
                    (actual time=0.050..850.000 rows=1200000 loops=1)
  Filter: (order_date >= '2026-10-01'::date)
  Rows Removed by Filter: 8800000
Planning Time: 0.300 ms
Execution Time: 920.000 ms
```

| Field | Meaning |
|---|---|
| `actual time=0.050..850.000` | ms to the **first** row .. ms to the **last** row, per loop |
| `rows=1200000` | rows actually output, **per loop** |
| `loops=1` | how many times this node was executed |
| `Rows Removed by Filter` | rows read and thrown away — high means the scan read much more than it kept |
| `Planning Time` | time to build the plan |
| `Execution Time` | time to run it, excluding sending rows to the client |

Here the estimate was 500,000 rows and the truth was 1,200,000. **Compare estimated with actual
rows before looking at time**: the time tells you where it hurts, the row mismatch tells you
why.

**`loops` matters.** When `loops > 1`, `actual time` and `rows` are **averages per loop**.
Multiply by `loops` for the node's total: `actual time=0.010..0.020 rows=1 loops=1000000` is
about 20 seconds and a million rows, not 0.02 ms and one row.

### 1-5) `EXPLAIN ANALYZE` runs the statement

**`EXPLAIN ANALYZE DELETE …` deletes the rows.** To measure a write without keeping it, wrap it
in a transaction and roll back:

```sql
BEGIN;

EXPLAIN ANALYZE
DELETE FROM orders
WHERE  order_date < DATE '2020-01-01';

ROLLBACK;
```

- The rollback undoes the rows, but not every side effect: sequence values consumed stay
  consumed, and anything a trigger sent outside the database has already happened.
- The `DELETE` still takes its row locks until the rollback, so do not do this on a busy
  production table.

---

## 2) Reading the Plan Tree ⭐⭐⭐⭐⭐

### 2-1) Bottom-up: each child feeds its parent

**A plan is a tree: the most-indented nodes read tables, the nodes above them filter, join,
sort and aggregate, and the top node returns the result. Read it from the leaves up.**

```sql
SELECT u.country, SUM(o.amount)
FROM   orders o
JOIN   users  u ON u.user_id = o.user_id
WHERE  o.order_date >= DATE '2026-10-01'
GROUP  BY u.country;
```

```text
HashAggregate                                   4. group by country, SUM(amount)
  Group Key: u.country
  ->  Hash Join                                 3. match orders to users on user_id
        Hash Cond: (o.user_id = u.user_id)
        ->  Seq Scan on orders o                1. read orders, keep October onward
              Filter: (order_date >= '2026-10-01'::date)
        ->  Hash                                2. build a hash table from users
              ->  Seq Scan on users u
```

- An `->` line is a child; its indentation says which parent it feeds.
- Two children of one node are not necessarily run at the same time: a `Hash Join` builds its
  `Hash` child completely first, then streams the other child through it.
- Lines without `->` (`Filter:`, `Hash Cond:`, `Group Key:`) are details of the node above
  them, not nodes.
- On a large table you will often also see `Gather` with `Parallel Seq Scan` children and a
  `Workers Launched:` line — PostgreSQL running one node across several worker processes.

### 2-2) The node types you will meet

| Node | What it does | What to check |
|---|---|---|
| `Seq Scan` | reads every page of the table | how many rows `Filter` removes |
| `Index Scan` | walks an index, then fetches each matching row from the table | how many rows; random I/O per row |
| `Index Only Scan` | answers from the index alone | `Heap Fetches` — rows it still had to visit the table for |
| `Bitmap Index Scan` + `Bitmap Heap Scan` | collects matching row locations, then reads their pages in order | `Heap Blocks: lossy` |
| `Nested Loop` | for each outer row, runs the inner side | `loops` on the inner side |
| `Hash Join` + `Hash` | hashes one side, probes it with the other | `Batches` on the `Hash` node |
| `Merge Join` | merges two inputs sorted on the join key | the `Sort` nodes beneath it |
| `Sort` | sorts its input | `Sort Method` — `external merge` means it spilled |
| `HashAggregate` | groups with a hash table | `Batches` / `Disk Usage` |
| `GroupAggregate` | groups input that is already sorted | the `Sort` beneath it |
| `Limit` | stops after n rows | whether the child can actually stop early |
| `Gather` / `Gather Merge` | collects rows from parallel workers | `Workers Launched` vs `Workers Planned` |

---

## 3) Scans ⭐⭐⭐⭐

Assume `orders` has 10 million rows and an index on `order_date`:

```sql
CREATE INDEX idx_orders_date ON orders (order_date);
```

### 3-1) Seq Scan

**A Seq Scan reads every page and tests every row. It is the right choice whenever the query
needs a large share of the table — seeing one is not a bug in itself.**

```text
Seq Scan on orders  (cost=0.00..180000.00 rows=100000 width=64)
  Filter: (order_date >= '2026-10-01'::date)
```

It is the cheapest plan when:

- the query needs a large fraction of the rows (reading 9 of 10 million rows through an index
  costs far more than reading the table once in order);
- the table is small enough to fit in a few pages;
- no index matches the predicate, or the matching index is not selective.

It is a problem when a large table is scanned to return a handful of rows — a high
`Rows Removed by Filter` against a small output is the signal.

### 3-2) Index Scan and Index Only Scan

**An Index Scan finds matching entries in the index, then fetches each row from the table. It
wins when the predicate is selective — few rows out of many.**

```text
Index Scan using idx_orders_date on orders  (cost=0.43..8500.00 rows=10000 width=64)
  Index Cond: (order_date >= '2026-11-01'::date)
```

- `Index Cond` is applied inside the index; a `Filter` line under an index scan is checked
  after the row is fetched, so rows it removes were fetched for nothing.
- Each row fetch can be a random page read, which is why the index loses as the matching share
  grows.
- An **Index Only Scan** skips the table when every needed column is in the index (for example
  through `INCLUDE`). It still has to visit the table for pages the visibility map does not mark
  all-visible; `Heap Fetches` counts those, and a recent `VACUUM` brings it down.

### 3-3) Bitmap Heap Scan

**A bitmap scan is the middle ground: it collects every matching row location from the index
first, then reads the table pages in physical order, each page once.**

```text
Bitmap Heap Scan on orders
  Recheck Cond: (order_date >= '2026-10-01'::date)
  Heap Blocks: exact=41000
  ->  Bitmap Index Scan on idx_orders_date
        Index Cond: (order_date >= '2026-10-01'::date)
```

- It suits predicates that match too many rows for an Index Scan but too few for a Seq Scan,
  and it can combine several indexes (`BitmapAnd` / `BitmapOr`).
- If the bitmap outgrows `work_mem` it becomes **lossy** — it remembers whole pages instead of
  rows — and every row on those pages is re-tested against `Recheck Cond`. `Heap Blocks:
  lossy=…` in `ANALYZE` output shows it happened.

### 3-4) "There is an index — why a Seq Scan?" ⭐⭐⭐⭐⭐

**Either the planner thinks the Seq Scan is cheaper (often correctly), or the index cannot be
used for this predicate.**

| Cause | Example | Fix |
|---|---|---|
| the query needs a large share of the table | 20% of `orders` matches | none — the Seq Scan is right; measure before forcing the index |
| the table is small | a 50-row lookup table | none |
| stale statistics make the match look large | after a bulk load | `ANALYZE orders;` |
| a function or arithmetic on the column | `WHERE DATE(order_ts) = …`, `WHERE amount * 2 > …` | rewrite as a range on the bare column, or create an expression index |
| a type or collation mismatch | comparing a `text` column to a number; `LIKE 'abc%'` under a non-C collation | match the types; use a `text_pattern_ops` index |
| the predicate is not on the composite index's leading column | index `(user_id, order_date)`, filter only on `order_date` | an index that leads with the filtered column |
| a leading wildcard | `WHERE email LIKE '%@gmail.com'` | a trigram (`pg_trgm`) index |

The function-on-column case is the one interviewers expect. This predicate hides `order_ts`
inside a function, so a plain index on `order_ts` cannot serve it:

```sql
SELECT * FROM orders
WHERE  DATE(order_ts) = DATE '2026-11-10';
```

The same filter as a half-open range on the bare column can use the index:

```sql
SELECT * FROM orders
WHERE  order_ts >= TIMESTAMP '2026-11-10 00:00:00'
  AND  order_ts <  TIMESTAMP '2026-11-11 00:00:00';
```

To test whether an index *could* help, `SET enable_seqscan = off;` in your own session and
compare `EXPLAIN ANALYZE` output — a diagnostic only, never a production setting.

---

## 4) Estimated vs Actual Rows ⭐⭐⭐⭐⭐

### 4-1) Why the estimate is the first thing to check

**The optimiser chooses join order and join algorithm from estimated row counts. When an
estimate is off by orders of magnitude, every decision above that node was made for the wrong
data.**

```text
Hash Join  (cost=1000.00..50000.00 rows=10000 width=40)
           (actual time=10.000..500.000 rows=2000000 loops=1)
```

The planner expected 10,000 rows and got 2,000,000 — 200 times more. Typical consequences:

- it picks a **Nested Loop** because the outer side looks tiny, and the inner side then runs
  two million times;
- it sizes a hash table for 10,000 rows, which then spills to disk;
- it puts the "small" input on the wrong side of a join.

A quick way to rank nodes is the **error factor**: the larger of estimated and actual rows
divided by the smaller (counting 0 as 1). Compare the numbers as printed: the estimate and the
actual count are **both per loop**, so an inner node estimated at `rows=1` that shows
`rows=1 loops=1000` is estimated correctly. Multiply by `loops` only to get the node's total
work. Factors under about 10× are normal; 100× or more on a node with expensive parents is
worth investigating.

One exception before blaming statistics: a node that was **stopped early** reports fewer actual
rows than estimated by design — the child of a `Limit`, or a Merge Join input left unread once
the other input ran out. That mismatch is not a bad estimate. Otherwise, always look at the
**lowest** node with a big mismatch — the errors above it are usually inherited from it.

### 4-2) Causes and fixes

| Cause | What happened | Fix |
|---|---|---|
| **stale statistics** | a bulk load or delete since the last `ANALYZE` | `ANALYZE orders;` — and check autovacuum is keeping up |
| **skewed values the sample missed** | one value holds 30% of rows | raise the column's sample: `ALTER TABLE orders ALTER COLUMN status SET STATISTICS 1000;` then `ANALYZE` |
| **correlated columns** | `country = 'TW' AND city = 'Taipei'`: the planner multiplies the two selectivities as if independent and underestimates | extended statistics (below) |
| **an expression the planner cannot estimate** | `WHERE lower(email) = …`, a function returning a set | an expression index (it gets its own statistics) |
| **a parameter the planner cannot see** | a prepared statement reusing a generic plan built without the value | compare with a literal value; check `plan_cache_mode` |

Extended statistics teach the planner that two columns move together:

```sql
CREATE STATISTICS stats_users_country_city (dependencies, mcv)
ON country, city
FROM users;

ANALYZE users;
```

---

## 5) Join Algorithms in a Plan ⭐⭐⭐⭐

### 5-1) Nested Loop

**For each row of the outer (first) child, run the inner (second) child. Fast when the outer
side is small and the inner side is an index lookup; disastrous when the outer side is large.**

```text
Nested Loop  (actual rows=1000 loops=1)
  ->  Seq Scan on orders  (actual rows=1000 loops=1)
  ->  Index Scan using users_pkey on users  (actual rows=1 loops=1000)
        Index Cond: (user_id = orders.user_id)
```

- The inner Index Scan ran 1,000 times (`loops=1000`). At 1,000 outer rows that is cheap; at
  10 million it is 10 million index probes.
- It is the only algorithm that handles any join condition, including inequalities and
  `LIKE`, so a non-equi join forces it.
- A large `loops` on the inner side beneath a small estimated outer side is the classic
  symptom of a bad estimate (§4).

### 5-2) Hash Join

**Build a hash table from one input (the `Hash` child), then stream the other input through it
and probe for matches. The default for large equi-joins.**

```text
Hash Join
  Hash Cond: (orders.user_id = users.user_id)
  ->  Seq Scan on orders            probe side: streamed
  ->  Hash                          build side: fully read first
        Buckets: 262144  Batches: 1  Memory Usage: 18000kB
        ->  Seq Scan on users
```

- Needs an equality condition. Linear in both inputs.
- The planner puts the side it estimates to be smaller under `Hash`. If that side is bigger
  than expected, the hash table outgrows memory and splits into **batches** that go to disk
  (`Batches` > 1, §6).
- The hash table's memory limit is `work_mem × hash_mem_multiplier` (2.0 by default since
  PostgreSQL 15).

### 5-3) Merge Join

**Sort both inputs on the join key, then walk them together like the merge step of merge sort.
Cheap when the inputs already arrive sorted; expensive when two large inputs must be sorted
first.**

```text
Merge Join
  Merge Cond: (orders.user_id = users.user_id)
  ->  Sort
        Sort Key: orders.user_id
        ->  Seq Scan on orders
  ->  Index Scan using users_pkey on users       already in user_id order
```

- An index on the join key (as on `users` here), or an earlier sort, removes the `Sort` on that
  side.
- It does not need memory for a hash table, so it can win when neither side fits in memory —
  but the `Sort` nodes beneath it can spill instead.

### 5-4) Choosing between them

| Join | How it works | Fits | Watch for |
|---|---|---|---|
| **Nested Loop** | outer row → probe inner | small outer side + index on inner key; any join condition | inner `loops` far above the estimate |
| **Hash Join** | hash one side, probe with the other | large equi-joins, no useful order | `Batches` > 1 on the `Hash` node |
| **Merge Join** | merge two sorted inputs | inputs already sorted (index, earlier sort); very large inputs | expensive `Sort` children, `external merge` |

No algorithm is always best. A Hash Join is not "better" than a Nested Loop: for 50 orders and
an indexed `users`, the Nested Loop does 50 lookups while the Hash Join reads all of `users`.
Judge by the actual rows, not by the node name.

---

## 6) Spills: Sort and Hash Out of Memory ⭐⭐⭐⭐

When a sort or hash needs more memory than its limit, PostgreSQL writes the overflow to
temporary files — a **spill to disk**. The limit is `work_mem` (4 MB by default) per sort or
hash operation, times `hash_mem_multiplier` for hashes.

### 6-1) Sort spill

```sql
SELECT * FROM orders ORDER BY amount DESC;
```

```text
Sort  (actual time=4200.000..5100.000 rows=10000000 loops=1)
  Sort Key: amount DESC
  Sort Method: external merge  Disk: 204800kB
```

- `external merge  Disk: 204800kB` — the sort did not fit, so it wrote about 200 MB of sorted
  runs to disk and merged them.
- `quicksort  Memory: …` means it fit. `top-N heapsort` means a `LIMIT` let it keep only the
  top rows — often the real fix is adding the `LIMIT` the caller wanted all along.

### 6-2) Hash spill

```sql
SELECT customer_id, SUM(amount)
FROM   orders
GROUP  BY customer_id;
```

```text
HashAggregate
  Group Key: customer_id
  Batches: 16  Memory Usage: 8241kB  Disk Usage: 204800kB
```

The same signal on a Hash Join's build side:

```text
Hash
  Buckets: 65536  Batches: 8  Memory Usage: 4096kB
```

`Batches: 1` means everything fit in memory. `Batches` > 1 means the input was split and some
of it went through temp files. It is a lead, not a verdict — weigh it against the node's
actual time and the `temp read` / `temp written` counts.

### 6-3) Fixing a spill

| Fix | Why it helps |
|---|---|
| filter earlier, select fewer columns | less data reaches the sort or hash, and narrower rows fit more per MB |
| drop an `ORDER BY` nobody needs; add the `LIMIT` they do | removes the sort, or turns it into a top-N heapsort |
| fix the estimate (§4) | the planner may choose a different join or size the hash correctly |
| `SET LOCAL work_mem = '256MB';` inside the query's transaction | more memory for this query only; reverts at commit or rollback |
| pre-aggregate, or process in batches | each step handles less data |

Raise `work_mem` **per transaction**, not globally. A plain `SET` lasts for the rest of the
session — on a pooled connection it leaks into whoever borrows that connection next — so use
`SET LOCAL`, or `RESET work_mem` afterwards. And the limit applies to **each** sort and hash
node in **each** query, and parallel workers each get their own. A query with five hash nodes
and four workers can use twenty times the setting. And if the real cause is reading a billion
rows you do not need, more memory just makes the wrong plan finish slightly sooner.

---

## 7) `BUFFERS` and I/O ⭐⭐⭐

### 7-1) The counters

```sql
EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM orders
WHERE  order_date >= DATE '2026-10-01';
```

```text
Seq Scan on orders  (actual time=0.050..850.000 rows=1200000 loops=1)
  Buffers: shared hit=12000 read=80000
```

Each count is in pages, normally 8 KB each, and a parent's counts include its children's.

| Counter | Meaning |
|---|---|
| `shared hit` | page was already in PostgreSQL's shared buffer cache |
| `shared read` | page had to be read in — from the OS page cache or from disk |
| `shared dirtied` | pages this node modified |
| `shared written` | pages this node had to write out to free buffer space |
| `temp read` / `temp written` | pages of temporary files — sort and hash spills |
| `local …` | the same, for temporary tables |

Here the scan touched 92,000 pages (about 720 MB); 12,000 were cached and 80,000 were read in.

### 7-2) Reading them, and their limits

- **High `shared read`** — the query pulls a lot of data in. Ask whether it scans more than it
  needs: a missing index, no partition pruning, a filter applied too late.
- **High `shared hit`** — the data was cached. Fast now, but a cold cache (after a restart, on a
  replica) will behave like the `read` case.
- **High `temp read` / `temp written`** — a sort or hash spilled (§6).
- **Pages touched is a steadier measure than time**: it does not change with cache warmth, so it
  is the better number to compare before and after a fix.

**`shared read` is not a count of disk reads**: a page missing from shared buffers may still be
in the operating system's page cache. To see time actually spent waiting on I/O, turn on
`track_io_timing` (a superuser setting, or set it in `postgresql.conf`); `EXPLAIN (ANALYZE,
BUFFERS)` then prints an `I/O Timings:` line per node:

```sql
SET track_io_timing = on;

EXPLAIN (ANALYZE, BUFFERS)
SELECT * FROM orders
WHERE  order_date >= DATE '2026-10-01';
```

---

## 8) Worked Example: A Slow Revenue Query ⭐⭐⭐⭐⭐

`orders` has 100 million rows (20 GB), `users` has 10 million (2 GB). This report takes 120
seconds:

```sql
SELECT u.country, SUM(o.amount) AS revenue
FROM   orders o
JOIN   users  u ON o.user_id = u.user_id
WHERE  o.order_date >= DATE '2026-10-01'
GROUP  BY u.country;
```

### 8-1) The plan

`EXPLAIN (ANALYZE, BUFFERS)` gives, simplified (a real plan would also run the scans in
parallel):

```text
HashAggregate  (actual time=118000..118005 rows=20 loops=1)
  Group Key: u.country
  ->  Hash Join  (actual time=30000..110000 rows=20000000 loops=1)
        Hash Cond: (o.user_id = u.user_id)
        ->  Seq Scan on orders o  (actual rows=20000000 loops=1)
              Filter: (order_date >= '2026-10-01'::date)
              Rows Removed by Filter: 80000000
        ->  Hash  (actual rows=10000000 loops=1)
              Buckets: 262144  Batches: 32  Memory Usage: 8192kB
              ->  Seq Scan on users u  (actual rows=10000000 loops=1)
```

### 8-2) Finding the bottleneck

Read bottom-up:

1. **`Seq Scan on orders`** — all 100M rows read, 80M discarded by the filter. Most of the
   I/O is spent on rows the query does not want.
2. **`Hash` on users** — 10M rows in `Batches: 32`: the build side is far bigger than the
   memory limit, so most of it went through temp files.
3. **`Hash Join`** — 20M rows out, starting at 30 s (the time to build the hash) and running to
   110 s.
4. **`HashAggregate`** — 20M rows in, 20 rows out, 8 s. Not the problem.

The join and the scan beneath it are where the time goes.

### 8-3) Candidate fixes

**A — read less of `orders`.** If date-bounded queries are common, partition `orders` by
`order_date` so the planner prunes old partitions, or add an index. With 20% of rows matching,
an index on `order_date` may well lose to the Seq Scan — measure it. A covering index
`(order_date) INCLUDE (user_id, amount)` makes an Index Only Scan possible.

**B — shrink the hash build.** The planner hashes `users` because it is the smaller input; it
only needs `user_id` and `country`, and raising `work_mem` for this transaction (`SET LOCAL`) may bring
`Batches` down to 1.

**C — aggregate before joining.** Collapse 20M orders to one row per user first, so the join
handles at most one row per active user:

```sql
WITH user_revenue AS (
    SELECT user_id, SUM(amount) AS revenue
    FROM   orders
    WHERE  order_date >= DATE '2026-10-01'
    GROUP  BY user_id
)
SELECT u.country, SUM(r.revenue) AS revenue
FROM   user_revenue r
JOIN   users u ON r.user_id = u.user_id
GROUP  BY u.country;
```

This helps when users place many orders each and adds a second aggregation when they do not.
The plan, not intuition, decides.

### 8-4) Proving the fix

Change one thing, re-run `EXPLAIN (ANALYZE, BUFFERS)`, and compare the plans:

| Measure | Before | Goal |
|---|---|---|
| Execution Time | 120 s | lower |
| rows read from `orders` | 100M | lower |
| `Hash` batches | 32 | 1, or fewer |
| `temp written` | high | near zero |
| result | 20 rows | **identical**, checked separately |

The result must match exactly, and `EXPLAIN ANALYZE` cannot prove it: it throws the query's
output away, so its row count shows only that both versions return 20 rows, not that the 20
revenue figures agree. Run both queries against the same data snapshot (one `REPEATABLE READ`
transaction, or a frozen copy) and diff the result rows — `EXCEPT` in both directions should
return nothing. A faster query with a different answer is a bug.

---

## 9) Spark SQL and Warehouse Plans ⭐⭐⭐⭐

### 9-1) `EXPLAIN FORMATTED` in Spark

**The reading is the same — bottom-up, look at scans and joins — but the expensive thing in a
distributed plan is the `Exchange`: a shuffle of data across the network.**

```python
df.explain("formatted")
```

```sql
EXPLAIN FORMATTED
SELECT customer_id, SUM(amount)
FROM   orders
GROUP  BY customer_id;
```

| Operator | Meaning |
|---|---|
| `Scan` (`FileScan parquet …`) | reads files; check `PartitionFilters` (pruning) and `PushedFilters` |
| `Filter` / `Project` | row filter / column selection |
| `Exchange` | **a shuffle** — repartitions rows across executors |
| `BroadcastExchange` | ships a small table to every executor |
| `HashAggregate` / `SortAggregate` | aggregation; usually a partial one before the `Exchange` and a final one after |
| `BroadcastHashJoin` | join against a broadcast copy of the small side, no shuffle of the large side |
| `SortMergeJoin` | both sides shuffled and sorted on the key, then merged |
| `AdaptiveSparkPlan` | the root when Adaptive Query Execution (AQE) is on |

**With AQE on, `explain` shows the initial plan** (`AdaptiveSparkPlan isFinalPlan=false`). AQE
can switch a join to broadcast, coalesce partitions or split skewed ones at runtime, so the
plan that actually ran is in the Spark UI's SQL tab.

### 9-2) SortMergeJoin vs BroadcastHashJoin

```text
AdaptiveSparkPlan isFinalPlan=false
+- SortMergeJoin [customer_id], [customer_id], Inner
   :- Sort [customer_id ASC]
   :  +- Exchange hashpartitioning(customer_id, 200)
   :     +- Scan orders
   +- Sort [customer_id ASC]
      +- Exchange hashpartitioning(customer_id, 200)
         +- Scan customers
```

Both sides are scanned, **shuffled** into 200 partitions by `customer_id`
(`spark.sql.shuffle.partitions`), sorted, then merged. Two `Exchange` nodes mean both tables
cross the network.

```text
BroadcastHashJoin [customer_id], [customer_id], Inner, BuildRight
:- Scan orders
+- BroadcastExchange HashedRelationBroadcastMode(...)
   +- Scan customers
```

`customers` is copied to every executor and `orders` is joined where it already sits — the fact
table is never shuffled. Spark broadcasts automatically below
`spark.sql.autoBroadcastJoinThreshold` (10 MB by default), and the broadcast copy costs driver
and executor memory, so a "small" table that is not small causes out-of-memory errors.

### 9-3) PostgreSQL vs Spark

| | PostgreSQL | Spark |
|---|---|---|
| runs on | one server (with parallel workers) | a cluster of executors |
| scans | Seq / Index / Bitmap | file scans with partition pruning and pushed filters |
| joins | Nested Loop / Hash / Merge | Broadcast Hash / Sort-Merge / Shuffle Hash |
| the expensive movement | disk I/O, temp files | **shuffle** over the network |
| memory pressure | `work_mem`, spills | executor memory, spills |
| actual numbers | `EXPLAIN ANALYZE` | Spark UI: SQL metrics, stages, tasks |

In Spark, the plan says what will happen; the Spark UI says how it went. Check there for
shuffle read and write sizes, spill, the **task duration distribution** (one task far slower
than the median means skew) and broadcast sizes.

### 9-4) Other engines

| Engine | Plan | Actual execution |
|---|---|---|
| MySQL | `EXPLAIN` (the `type` column: `ALL` is a full scan) | `EXPLAIN ANALYZE` (8.0.18+) |
| BigQuery | — | the job's *Execution details*: stages, bytes, slot time |
| Snowflake | `EXPLAIN` | *Query Profile*: operators, bytes scanned, partitions pruned, spill |
| Trino | `EXPLAIN` | `EXPLAIN ANALYZE` |

In a warehouse, look for partitions scanned out of total (pruning), bytes spilled, and the stage
that shuffles the most data.

---

## 10) Interview Toolkit ⭐⭐⭐⭐⭐

### 10-1) A debugging order

1. **Check the query is correct first.** Join keys, grain, NULLs, duplicates. Never trade
   correctness for speed.
2. **Get the real plan.** PostgreSQL: `EXPLAIN (ANALYZE, BUFFERS)`. Spark: `EXPLAIN FORMATTED`
   plus the Spark UI.
3. **Scans.** Is it reading far more than it keeps? Is pruning or an index working?
4. **Estimated vs actual rows.** Find the lowest node with a large mismatch; check statistics,
   skew, correlated columns.
5. **Joins and aggregation.** A Nested Loop with huge inner `loops`? A Hash with many batches?
   In Spark, unnecessary shuffles?
6. **Memory and I/O.** `Sort Method`, `Batches`, temp pages, buffers; in Spark, spill and
   shuffle sizes.
7. **One change at a time, then re-measure** — time, pages read, and that the result is
   unchanged.

### 10-2) Keyword cheat table

| You see | It means | Look next at |
|---|---|---|
| `Seq Scan` + big `Rows Removed by Filter` | read much, kept little | index, sargability, partitioning |
| `Index Scan` returning many rows | many random reads | whether a Seq or Bitmap scan would be cheaper |
| `Heap Fetches` high on Index Only Scan | visibility map out of date | `VACUUM` |
| estimated ≪ actual rows | bad cardinality estimate | `ANALYZE`, statistics target, extended statistics |
| `Nested Loop`, inner `loops` in the millions | wrong join for this size | the estimate on the outer side |
| `Hash` with `Batches` > 1 | hash build spilled | build-side size, `work_mem` |
| `Sort Method: external merge` | sort spilled | sort input size, `work_mem`, a missing `LIMIT` |
| `temp written` high | spill to temp files | the sort or hash above it |
| `shared read` high | lots of data pulled in | scan volume |
| `Exchange` (Spark) | shuffle | join strategy, partitioning, skew |
| `SortMergeJoin` (Spark) | both sides shuffled | could one side be broadcast? |
| `isFinalPlan=false` (Spark) | the pre-AQE plan | the final plan in the Spark UI |

### 10-3) "Your query takes 10 minutes. What do you do?"

A model answer, in the order an interviewer wants to hear it:

> "I would start from the execution plan rather than changing configuration.
>
> First, I run `EXPLAIN ANALYZE` and compare estimated with actual row counts on each node,
> because a large mismatch usually explains a bad join choice or a spill.
>
> Second, I look at the scans: is the query reading far more data than it returns, is an index
> or partition pruning being used, are filters pushed down?
>
> Third, I check the join strategies — a nested loop running millions of inner iterations, a
> hash join whose build side spills, or expensive sorts feeding a merge join.
>
> I also check memory and I/O: buffer reads, temp file usage, sort and hash spills.
>
> On Spark I would add shuffle read and write sizes, the task duration distribution for skew,
> and the final adaptive plan in the Spark UI.
>
> Then I change one thing at a time, re-measure, and confirm the result is identical."

**The one-line version:** `EXPLAIN` gives you a hypothesis about where the time goes;
`EXPLAIN ANALYZE` and the runtime metrics confirm or kill it. The goal is lower end-to-end time
and cost with the same answer — not a plan that merely looks better.
