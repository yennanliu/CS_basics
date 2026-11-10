# Data Storage and Processing Performance FAQ

> **Scope** — the performance questions a data engineering interview asks across engines rather than about one: a method for any "make it faster" question, optimising how data is stored, optimising how it is processed, avoiding out-of-memory failures in Spark, relational databases, MPP query engines and single-node tools, re-ordering and merging data (sorting, clustering, Z-order, compaction, `MERGE`, external merge sort, LSM compaction), read vs write performance on storage systems, and the SQL rewrites that make a query faster. Each engine's internals stay in its own deep dive; this file is the cross-engine view and links to them.
> **See also**: [`./spark.md`](./spark.md) — shuffles, skew, memory and Spark's own failure table; [`./sql_explain.md`](./sql_explain.md) — reading the plan that tells you where the time went; [`./sql_for_data.md`](./sql_for_data.md) — indexes and warehouse levers in §10; [`./batch_pipeline.md`](./batch_pipeline.md) — file formats, table formats and compaction in a pipeline; [`./sql_vs_nosql.md`](./sql_vs_nosql.md) — B-tree vs LSM storage engines; [`../faq_performance_tune.md`](../faq_performance_tune.md) — application-level profiling.

Almost every data performance question has the same answer underneath: **read less, move
less, hold less in memory, and write in the shape the reader needs.** The sections below are
that sentence applied to storage, processing, memory, ordering and the read/write path.

---

## 0) The Short Answer ⭐⭐⭐⭐⭐

**"First I measure where the time goes — the query plan or the Spark UI, not a guess. Then I
reduce work in this order: read less data (columnar formats, partition pruning, clustering so
min/max statistics skip files), move less data (filter and aggregate before a join, broadcast
the small side, fix skew), hold less in memory (stream instead of collecting, enough
partitions, let the engine spill), and write less (incremental `MERGE` instead of full
rebuilds, files of a few hundred MB). Only then do I add hardware."**

| Question as asked | The one-line answer | Section |
|---|---|---|
| "How do you optimise data storage?" | columnar + compressed, right-sized files, partition on the common filter, sort within it | [§2](#2-optimising-storage-) |
| "How do you optimise processing?" | prune and push down, shuffle less, compute incrementally | [§3](#3-optimising-processing-) |
| "How do you avoid OOM in a query engine?" | never materialise something unbounded in one place — one key, one node, one client | [§4](#4-avoiding-out-of-memory-failures-) |
| "How do you re-order or merge data?" | sort / cluster on write, compact small files, `MERGE` on a deduplicated source | [§5](#5-re-ordering-and-merging-data-) |
| "How do you speed up reads or writes on a database?" | every read optimisation is a write cost — name the trade, then batch writes and index reads | [§6](#6-read-and-write-performance-on-storage-systems-) |
| "How do you make this SQL faster?" | sargable filters, aggregate before join, `EXISTS`, keyset pagination | [§7](#7-sql-rewrites-that-make-queries-faster-) |

---

## 1) A Method for Any Performance Question ⭐⭐⭐⭐⭐

### 1-1) The four places time goes

**A data job spends its time in four places — reading, moving, computing and writing — and
the fix depends on which one dominates, so the first step is always to find out.**

| Phase | Symptom | Where you see it |
|---|---|---|
| **Read** (I/O) | scan time dominates; bytes read ≫ bytes needed | `BUFFERS` in PostgreSQL, "bytes scanned" in BigQuery / Athena, input size per stage in the Spark UI |
| **Move** (shuffle, network) | one stage with huge shuffle read/write; one slow task | Spark UI stage table, `Exchange` nodes in a plan |
| **Compute** | CPU-bound tasks; a Python UDF; a sort or hash spilling | task time vs GC time, `Sort Method: external merge` |
| **Write** | many small files; slow commit; index maintenance on every row | file count per partition, write stage time, WAL volume |

Ask before you tune:

1. **What is the target?** "The daily job must finish by 06:00" or "p95 under 2 s" — a number.
2. **What changed?** Data volume, a new join, a new skewed key, a config, an engine upgrade.
3. **Which phase dominates?** Read the plan or the UI; the slowest stage or node is the work.
4. **Is it one task or all of them?** One slow task is skew; all slow is volume or resources.

### 1-2) The levers in order of payoff

**Cheap structural changes beat expensive tuning: removing work is worth more than doing the
same work faster, and adding hardware is last because it hides the problem and costs money
every day.**

| Order | Lever | Typical gain | Example |
|---|---|---|---|
| 1 | **Do not do the work** | 10–1000× | incremental instead of full rebuild; drop an unused column from the extract |
| 2 | **Read less** | 10–100× | partition pruning, column pruning, clustering for data skipping |
| 3 | **Move less** | 2–20× | broadcast the dimension, aggregate before the join, fix skew |
| 4 | **Compute smarter** | 2–10× | built-ins instead of Python UDFs, approximate distinct counts |
| 5 | **Write better** | 2–10× for the next reader | right-sized files, sorted output, compaction |
| 6 | **Tune the engine** | 1.2–3× | shuffle partitions, memory fractions, `work_mem` |
| 7 | **Scale out or up** | linear at best | more executors, bigger warehouse |

---

## 2) Optimising Storage ⭐⭐⭐⭐⭐

### 2-1) Format, compression and encoding

**For analytics, store columnar (Parquet or ORC) with a fast codec: a query reads only the
columns it names, similar values in a column compress well, and per-chunk statistics let the
reader skip data without opening it.**

| Choice | Use it for | Why |
|---|---|---|
| **Parquet / ORC** (columnar) | analytical tables, the lake | column pruning, statistics, encodings |
| **Avro** (row, schema-ful) | Kafka messages, landing raw records | whole-record writes, schema evolution |
| **CSV / JSON** | interchange at the edge only | no types, no statistics, slow to parse — convert on arrival |

| Codec | Trade-off |
|---|---|
| **Snappy** / **LZ4** | fast to decompress, moderate ratio — the usual default |
| **ZSTD** | better ratio than Snappy at similar decode speed — a good default for cold and warm data |
| **GZIP** | good ratio, slow, and a gzip **CSV** is not splittable — one task per file |

Inside a Parquet column chunk the writer also **encodes** before compressing: **dictionary**
encoding for low-cardinality strings, **run-length** encoding for repeated values, **delta**
encoding for sorted integers and timestamps. Sorting the data (see [§5](#5-re-ordering-and-merging-data-))
makes all three work better, which is why sorted files are often 2–5× smaller.

### 2-2) File and partition sizing

**Aim for files of roughly 128 MB to 1 GB and partitions that hold at least a few files'
worth of data; too many small files cost more in opens and metadata than the data itself.**

| Unit | Target | Too small means | Too large means |
|---|---|---|---|
| **File** | 128 MB – 1 GB | one task and one open per tiny file; slow listing and planning | little parallelism within the file |
| **Parquet row group** | ~128 MB | weak statistics, more seeks | coarse skipping |
| **Table partition** | ≥ 1 GB, a few hundred to a few thousand partitions | small files and huge metadata | little pruning |

- **Partition by a low-cardinality column that almost every query filters on** — usually the
  date. Never by `user_id` or another high-cardinality key.
- Within a partition, **sort or cluster** by the next most-filtered column, so min/max
  statistics can skip files (see [`./schema_design.md`](./schema_design.md#4-3-bucketing-z-order-and-liquid-clustering)).
- A table format (Iceberg, Delta) keeps the file list in metadata, so planning does not have to
  list a directory — but it does not stop small files being slow to read.

### 2-3) Data types and width

**Every byte in a row is read, shuffled, cached and stored again in every copy, so the
narrowest correct type is a free speed-up.**

- Integer surrogate keys join faster and compress better than long string keys.
- Store dates and timestamps as `DATE` / `TIMESTAMP`, not strings — they are smaller, compare
  correctly and allow partition pruning.
- `DECIMAL(p, s)` for money; `DOUBLE` only where rounding error is acceptable.
- Extract the JSON fields queries actually use into typed columns; a `STRING` holding JSON is
  re-parsed on every query and has no statistics.
- Drop columns nobody reads from wide raw tables when promoting to silver / gold.

### 2-4) Lifecycle: hot, warm, cold

**Not all data deserves the same storage: keep the recent, frequently-read data fast, move the
rest to cheaper tiers, and replace old raw detail with aggregates when nobody queries it.**

| Tier | Holds | Storage |
|---|---|---|
| Hot | the last days or weeks; dashboards hit it | warehouse native storage, SSD, cache |
| Warm | months; ad hoc analysis | object storage in Parquet, standard class |
| Cold | years; audit and rare backfills | infrequent-access or archive class |

- Expire old table snapshots (`VACUUM` in Delta, `expire_snapshots` in Iceberg) — otherwise
  every rewrite keeps the old files forever.
- Put a TTL on bronze / raw landing data once silver is trusted and replayable.
- Roll old detail into daily aggregates when the detail is no longer queried.

---

## 3) Optimising Processing ⭐⭐⭐⭐⭐

### 3-1) Read less: pruning and pushdown

**The fastest byte is the one never read: let the engine skip partitions, files, row groups
and columns before any data reaches a task.**

| Technique | What it skips | Needs |
|---|---|---|
| **Projection pushdown** (column pruning) | columns not referenced | a columnar format; no `SELECT *` |
| **Partition pruning** | whole partitions | a filter on the partition column itself |
| **Predicate pushdown** | row groups / files whose min/max exclude the value | statistics; data sorted or clustered on the column |
| **Bloom filters** | files that cannot contain an equality value | a bloom filter written for that column |
| **Dynamic partition pruning** | fact partitions with no matching dimension rows | a join on the partition column to a filtered dimension |

Check that pushdown happened instead of assuming it:

```python
# PySpark — the plan should list the filter under PushedFilters / PartitionFilters
orders = spark.read.parquet("s3://lake/silver/orders/")
recent = (orders
          .where("order_date >= '2026-11-01' AND country = 'TW'")
          .select("order_id", "user_id", "amount"))
recent.explain("formatted")
# FileScan parquet ... PartitionFilters: [isnotnull(order_date), (order_date >= 2026-11-01)]
#                      PushedFilters:    [IsNotNull(country), EqualTo(country,TW)]
#                      ReadSchema:       struct<order_id:bigint,user_id:bigint,amount:decimal(12,2)>
```

Pushdown is lost by a function around the column (`WHERE to_date(ts) = …` on a column
partitioned by `ts`'s date), by a cast that changes the type, and by a Python UDF in the
filter.

### 3-2) Move less: shuffles and joins

**A shuffle writes every row to disk and sends it over the network, so the cheapest join is
the one that shuffles the fewest bytes.**

- **Filter and aggregate before the join**, not after — see the rewrite in [§7-2](#7-2-worked-rewrite-aggregate-before-the-join).
- **Broadcast the small side** so the large side never moves (Spark's
  `spark.sql.autoBroadcastJoinThreshold`, 10 MB by default; Trino's broadcast distribution).
- **Co-locate** tables that are always joined on the same key — bucketing in Spark / Hive,
  distribution keys in Redshift — so the join needs no exchange.
- **Fix skew** — one hot key makes one task do most of the work; see
  [`./spark.md`](./spark.md#7-3-data-skew-spotting-it-and-fixing-it).
- Avoid `DISTINCT` and `UNION` (which deduplicates) when the data is already unique —
  each is a hidden shuffle.

### 3-3) Compute less: incremental and precomputed

**Recomputing a year of history to add one day is the most common waste in a batch
platform; process what changed and precompute what is read often.**

| Technique | Replaces | Caveat |
|---|---|---|
| **Incremental load + `MERGE`** | nightly full rebuild | needs a reliable change marker or CDC |
| **Partition overwrite** of the days that changed | rebuilding the table | the job must be idempotent per partition |
| **Materialised views / summary tables** | dashboards scanning detail | refresh cost and staleness |
| **Approximate aggregates** (`approx_count_distinct`, HLL sketches) | exact `COUNT(DISTINCT)` | ~1–2% error, stated up front |
| **Built-in functions** | Python UDFs | a UDF blocks code generation and costs serialisation per row |

### 3-4) Write less

- Rewrite only the partitions or files that changed; a table format's `MERGE` touches only the
  files holding matching rows.
- Prefer **merge-on-read** for frequent small updates, **copy-on-write** for read-heavy tables
  updated in large batches (see [§5-4](#5-4-merging-datasets-merge-copy-on-write-and-merge-on-read)).
- Batch many small writes into fewer, larger ones — per-row inserts into a warehouse or a lake
  produce a file or a commit each.

---

## 4) Avoiding Out-of-Memory Failures ⭐⭐⭐⭐⭐

### 4-1) Why engines run out of memory

**An engine runs out of memory when something unbounded is materialised in a bounded place:
one key's rows in one task, a whole result on one node, a hash table built from the wrong
side, or too many queries each taking their share at once.**

The five shapes, in every engine:

| Shape | Example |
|---|---|
| **Everything to one place** | `collect()` to the Spark driver; a client fetching 50 M rows; a final result through a coordinator |
| **One key too big** | skew — a `NULL` or test-account key with 30% of the rows lands in one task |
| **Wrong build side** | a hash join builds its in-memory table from the large input |
| **Row explosion** | a fan-out join, `explode`, `collect_list` producing a huge row |
| **Too much concurrency** | each of 200 connections allowed several `work_mem` buffers |

What the engine does when its buffer fills is the other half of the answer:

| Engine | When a sort / hash / aggregation buffer fills |
|---|---|
| **Spark** | spills sort, aggregation and shuffle buffers to local disk; still fails on a single oversized partition, a huge broadcast, a driver `collect`, or off-heap growth |
| **PostgreSQL / MySQL** | spills sorts and hashes to temp files (slower, but completes); the whole server can still be killed by the OS if per-query memory × connections exceeds RAM |
| **Trino / Presto / Athena** | keeps operators in memory and **fails the query** at the memory limit unless spilling is configured — sizing the join is your job |
| **pandas** | no spilling: the whole DataFrame must fit in RAM, usually several times the file size |
| **DuckDB / Polars (streaming)** | spill or stream larger-than-memory sorts, joins and aggregations |

### 4-2) Spark

**Driver OOM is almost always something pulled to the driver; executor OOM is almost always
one partition too big — skew, too few shuffle partitions, or an explosion.**

Prevention, in the order to check:

1. **Never `collect()` or `toPandas()` a large result** — write it out; use `limit` or
   `toLocalIterator()` when the driver must see rows.
2. **Enough shuffle partitions** that each holds ~100–200 MB; enable AQE so it can coalesce
   and split them.
3. **Fix skew** before adding memory — AQE skew join, isolate or salt the hot key.
4. **Keep the broadcast honest** — the threshold is measured on an estimate; a "small" side
   that is really 2 GB kills the driver and every executor.
5. **Cap row growth** — check join fan-out, and avoid `collect_list` / `collect_set` over an
   unbounded group.
6. **Off-heap** — "Container killed … exceeding memory limits" means Python workers, Arrow or
   native buffers outgrew `spark.executor.memoryOverhead`, not the heap.

```python
# Write the big result out instead of collecting it
result.write.mode("overwrite").parquet("s3://lake/tmp/report/")

# If the driver really needs rows, iterate one partition at a time
for row in result.toLocalIterator():
    handle(row)

# Bound what reaches the driver; fail fast instead of OOM-ing (default 1g).
# It is read when the SparkContext starts — spark.conf.set at runtime does not change it:
#   spark-submit --conf spark.driver.maxResultSize=1g ...
#   SparkSession.builder.config("spark.driver.maxResultSize", "1g").getOrCreate()
```

The full error → cause → fix table is [`./spark.md` §15-1](./spark.md#15-1-out-of-memory),
and the memory regions behind it are [§9-1](./spark.md#9-1-unified-memory-management).

### 4-3) Relational databases (PostgreSQL, MySQL)

**A database rarely dies of one query; it dies of per-operation memory multiplied by
concurrency — and the client that fetched the whole result set often dies first.**

Server side:

- PostgreSQL's **`work_mem`** (default 4 MB) is per sort or hash **node**, per query, per
  connection — hash nodes may use `work_mem × hash_mem_multiplier` (default 2.0). A query with
  five sorts and hashes on 200 connections can ask for far more than `work_mem × 200`.
  Raise it **per session or per role** for the heavy report, not globally.
- Under the limit, a sort or hash **spills** (`Sort Method: external merge`, `Batches: 8`) —
  slower, not fatal. Fixing a spill is [`./sql_explain.md` §6-3](./sql_explain.md#6-3-fixing-a-spill).
- Use a **connection pooler** (PgBouncer) so concurrency is bounded by the pool, not by the
  number of application threads.
- MySQL's equivalents are `sort_buffer_size` and `join_buffer_size` (per session),
  `tmp_table_size` / `max_heap_table_size` (an in-memory temp table beyond that goes to disk).

Client side — the common one in data pipelines:

```python
# psycopg2: a *named* cursor is a server-side cursor; rows arrive in batches of itersize
import psycopg2

conn = psycopg2.connect(dsn)
with conn.cursor(name="export_orders") as cur:   # the name makes it server-side
    cur.itersize = 10_000
    cur.execute("SELECT order_id, user_id, amount FROM orders WHERE order_date >= %s",
                ("2026-11-01",))
    for row in cur:                              # never holds more than one batch
        write(row)
```

- JDBC to PostgreSQL honours `setFetchSize(n)` only with **autocommit off** and a
  forward-only result set; otherwise the driver buffers every row.
- MySQL Connector/J streams with `setFetchSize(Integer.MIN_VALUE)`, or with
  `useCursorFetch=true` and a positive fetch size.
- Avoid a 100 000-item `IN (...)` list built in the application — load the keys into a temporary
  table and join.

### 4-4) MPP query engines (Trino, Presto, Athena)

**Trino holds a hash join's build side in memory and fails the query at the limit, so the
answer is to make the build side small: put the smaller table on the right, give the
optimiser statistics, and reduce before joining.**

- **Build side** — Trino builds the hash table from the **right** side of a join. With table
  statistics the cost-based optimiser reorders joins; without them it keeps your order, so
  write the large fact on the left.
- **Distribution** — a **broadcast** join copies the build side to every worker (fast, but each
  worker holds all of it); a **partitioned** join hashes both sides (each worker holds a
  slice). A large build side must be partitioned.
- **High-cardinality `GROUP BY`** and **`COUNT(DISTINCT)` over many columns** hold large hash
  tables — pre-aggregate or use `approx_distinct`.
- Do not pull a huge result to the client: `CREATE TABLE AS SELECT` (CTAS) or `INSERT INTO`
  writes it in parallel from the workers.
- Athena's **"Query exhausted resources at this scale factor"** is the same failure; the fixes
  are the same, plus partitioning so less data is read at all.

### 4-5) Single-node tools (pandas, DuckDB, Polars)

**pandas needs the whole dataset in RAM, often several times the file size; read only the
columns and rows you need, in chunks, or switch to an engine that streams.**

```python
import pandas as pd

# 1) only the columns you need, with compact dtypes
cols = ["user_id", "country", "amount"]
dtypes = {"user_id": "int64", "country": "category", "amount": "float64"}

# 2) in chunks: aggregate each, then combine the small partial results
partials = []
for chunk in pd.read_csv("orders.csv", usecols=cols, dtype=dtypes, chunksize=1_000_000):
    partials.append(chunk.groupby("country", observed=True)["amount"].sum())
revenue_by_country = pd.concat(partials).groupby(level=0).sum()
```

- **DuckDB** runs sorts, joins and aggregations larger than memory by spilling to a temp
  directory: `SET memory_limit = '4GB'; SET temp_directory = '/tmp/duckdb';`, and it queries
  Parquet in place without loading it.
- **Polars**' lazy API (`scan_parquet` … `collect(engine="streaming")`) processes in batches.
- The chunked pattern only works for **decomposable** aggregates (sum, count, min, max); a
  median or a distinct count needs a different approach (sketches, or a real engine).

### 4-6) The prevention checklist

- [ ] Nothing unbounded goes to one place — driver, client or coordinator.
- [ ] Partitions are sized (~100–200 MB in Spark), not left at a default.
- [ ] The hottest key's share is known; skew is fixed before memory is raised.
- [ ] Every join's build / broadcast side is genuinely small.
- [ ] Joins are checked for fan-out (`COUNT(*)` before and after).
- [ ] Per-query memory × concurrency fits the machine (`work_mem`, Trino query limits, pool size).
- [ ] Clients stream with a fetch size or server-side cursor.
- [ ] More memory is the last fix, and it is recorded *why* it was needed.

---

## 5) Re-ordering and Merging Data ⭐⭐⭐⭐⭐

### 5-1) Why order on disk matters

**Sorting data by the columns queries filter on makes each file's min/max range narrow, so
the reader can skip most files; it also improves compression and lets a join or aggregation
skip its own sort.**

```text
Unsorted by country — every file's range covers everything; nothing can be skipped
  file 1: country min=AR max=US      file 2: min=BR max=US      file 3: min=AU max=VN

Sorted by country — a filter on country = 'TW' opens one file
  file 1: country min=AR max=FR      file 2: min=GB max=PL      file 3: min=TW max=VN
```

Benefits of sorted data:

- **Data skipping** — min/max, zone maps and bloom filters prune more.
- **Compression** — runs of equal values feed run-length and dictionary encoding.
- **Sort-free operators** — a merge join or streaming aggregation can use the existing order.

The cost is a sort on write, which is a shuffle — paid once by the writer instead of on every
read by every reader.

### 5-2) Sort, cluster or Z-order

**A linear sort serves one leading column well; Z-order and Hilbert curves (and liquid
clustering) interleave several columns so filters on any of them skip some data — at the cost
of skipping less on the first column than a pure sort would.**

| Layout | Good for | Weak at |
|---|---|---|
| **Linear sort** `(a, b)` | filters on `a`, or on `a` and `b` | filters on `b` alone |
| **Z-order / Hilbert** `(a, b)` | filters on `a` **or** `b` | very high column counts — gains fade beyond 3–4 columns |
| **Liquid clustering** (Delta) / **sort order** (Iceberg) | layout that can change without rewriting the table | still needs periodic maintenance |
| **Bucketing** | joins and aggregations on the bucket key | skipping on other columns |

```sql
-- Delta Lake: compact and co-locate by two filter columns
OPTIMIZE events WHERE event_date >= '2026-11-01' ZORDER BY (user_id, event_type);

-- Iceberg (Spark procedure): rewrite files sorted by a Z-order of two columns
CALL lake.system.rewrite_data_files(
  table      => 'analytics.events',
  strategy   => 'sort',
  sort_order => 'zorder(user_id, event_type)'
);
```

```python
# Spark: sort within each output file without a global sort (no single-reducer bottleneck)
from pyspark.sql import functions as F

FILES_PER_DATE = 8                            # size so each file lands at ~128 MB – 1 GB
(events
   .repartition("event_date",                 # a few writers per date, not one: a big day
                F.pmod(F.hash("user_id"), F.lit(FILES_PER_DATE)))   # is split across 8 tasks
   .sortWithinPartitions("country", "user_id")
   .write.partitionBy("event_date")
   .mode("overwrite")
   .parquet("s3://lake/silver/events/"))
```

`orderBy` is a **global** sort (range-partitioned shuffle); `sortWithinPartitions` sorts each
partition locally, which is all that data skipping needs.

### 5-3) Compaction: merging small files

**Compaction rewrites many small files into fewer large ones; bin-packing is cheap and only
fixes the file count, while a sort compaction also restores the layout that makes skipping
work.**

| Strategy | What it does | When |
|---|---|---|
| **Bin-pack** | concatenates small files up to the target size | streaming or micro-batch sinks; frequent |
| **Sort / Z-order** | rewrites and re-sorts | read-heavy tables; less often, on recent partitions |

- Compact **recent partitions** on a schedule; old partitions are already compact.
- Compaction is a commit like any other: with optimistic concurrency it can conflict with a
  writer touching the same files — schedule it away from heavy writes, or compact partitions
  the writer has finished.
- Expire snapshots afterwards, or the small files are still stored.

The pipeline view of small files and their causes is
[`./batch_pipeline.md` §7-4](./batch_pipeline.md#7-4-the-small-files-problem-and-compaction).

### 5-4) Merging datasets: `MERGE`, copy-on-write and merge-on-read

**`MERGE` upserts a batch of changes into a table; in a lakehouse, copy-on-write rewrites
every file a change touches (slow writes, fast reads), and merge-on-read writes small delete
or delta files that readers apply on the fly (fast writes, slower reads until compacted).**

| | Copy-on-write | Merge-on-read |
|---|---|---|
| Write cost | rewrite each touched file | append delete / delta files |
| Read cost | none extra | apply deletes at read time |
| Good for | batch updates, read-heavy tables | frequent small updates, CDC streams |
| Maintenance | — | periodic compaction to fold deltas in |

The trap: **the source must have at most one row per key.** If two source rows match one
target row, `MERGE` raises an error (PostgreSQL: *"MERGE command cannot affect row a second
time"*; Delta and Spark: a "multiple source rows matched" error). Deduplicate first:

```sql
MERGE INTO dim_customer AS t
USING (
  SELECT customer_id, email, country, updated_at
  FROM (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY updated_at DESC) AS rn
    FROM   staging_customer_changes
    WHERE  updated_at IS NOT NULL                -- DESC puts NULLs first: quarantine them instead
  ) ranked
  WHERE rn = 1                                   -- one row per key: the latest change
) AS s
ON t.customer_id = s.customer_id
WHEN MATCHED AND s.updated_at > t.updated_at THEN
  UPDATE SET email = s.email, country = s.country, updated_at = s.updated_at
WHEN NOT MATCHED THEN
  INSERT (customer_id, email, country, updated_at)
  VALUES (s.customer_id, s.email, s.country, s.updated_at);
```

- The `s.updated_at > t.updated_at` guard makes a replayed or out-of-order batch harmless. It
  needs a non-null timestamp on both sides — a `NULL` never compares greater — which is why
  the source drops rows without one (route them to a quarantine table, not the bin).
- Add the **partition column to the `ON` clause** when you can (`AND t.event_date = s.event_date`)
  so the engine prunes target files instead of scanning the whole table — but only when that
  column **never changes** for a key. If a row's `event_date` can move, the old row no longer
  matches and `WHEN NOT MATCHED` inserts a duplicate.

### 5-5) Merging sorted data at scale: external merge sort

**When data is bigger than memory, an engine sorts it in chunks that fit, writes each sorted
run to disk, then streams a k-way merge of the runs with a min-heap — the same algorithm
behind sort spills, sort-merge joins and LSM compaction.**

```text
Phase 1 — run generation           Phase 2 — k-way merge
  read M bytes → sort → run 1        heap holds the head of each run
  read M bytes → sort → run 2        pop the smallest, emit it, push that run's next row
  ...                                memory = one buffer per run, not the data
  read M bytes → sort → run k
```

```python
import heapq

# Each run is already sorted; heapq.merge streams them with a heap of size k
run_1 = [1, 4, 9]
run_2 = [2, 3, 10]
run_3 = [5, 6, 7]

merged = list(heapq.merge(run_1, run_2, run_3))
print(merged)   # [1, 2, 3, 4, 5, 6, 7, 9, 10]
```

- Cost: **O(N log k)** comparisons for N rows over k runs, and each row read and written about
  twice — that is why a spill is slower but still finishes.
- If there are more runs than buffers fit, the engine merges in several passes; more memory
  means fewer, longer runs and fewer passes.

### 5-6) LSM compaction: merging inside a storage engine

**An LSM-tree store (RocksDB, Cassandra, HBase) turns writes into sequential appends and
merges sorted files in the background; the compaction strategy chooses which cost you pay.**

| Strategy | Write amplification | Read and space amplification | Fits |
|---|---|---|---|
| **Size-tiered** (Cassandra's default) | low | higher — a key may sit in many files | write-heavy, append-mostly |
| **Leveled** (RocksDB's default) | higher | low — about one file per level to check | read-heavy, update-heavy |
| **Time-window** | low | low for time-ordered data with TTL | time series, metrics |

Background on B-tree vs LSM and the three amplifications is
[`./sql_vs_nosql.md` §3](./sql_vs_nosql.md#3-2-read-write-and-space-amplification).

---

## 6) Read and Write Performance on Storage Systems ⭐⭐⭐⭐⭐

### 6-1) The trade-off you always name

**Every structure that makes reads faster — an index, a sort order, a materialised copy —
has to be maintained on every write, so the first sentence of the answer is which side the
workload needs.**

| Read optimisation | What it costs writes |
|---|---|
| Another index | one more structure updated per insert / update; more WAL |
| Sorted or clustered layout | a sort on write, or later re-clustering |
| Denormalised / materialised copy | every change written twice; refresh lag |
| Leveled compaction | more background rewriting |
| Read replicas | replication lag; the primary still takes every write |

This is the **RUM** trade-off — read overhead, update overhead and memory / space overhead:
you can lower any two, not all three.

### 6-2) Faster writes

**Writes get faster when you send fewer, larger batches, maintain fewer structures per row,
and write sequentially rather than randomly.**

| Technique | Why it helps |
|---|---|
| **Bulk load** (`COPY`, `LOAD DATA`) instead of row-by-row `INSERT` | one statement, one parse, minimal round trips |
| **Batch** inserts and commit every N rows | one fsync per commit instead of per row |
| **Drop or defer secondary indexes** during a large backfill, rebuild after | building an index once is cheaper than maintaining it per row |
| **Fewer indexes** on write-heavy tables | each index is a write per row |
| **Sequential keys** (`BIGINT` identity, UUIDv7) instead of random UUIDv4 in a B-tree | inserts append to the right-most page instead of splitting random pages |
| **Partition** large tables | smaller indexes; old partitions dropped instead of `DELETE`d |
| **LSM-based store** for very high write rates | writes are sequential appends |
| **Asynchronous commit** (`synchronous_commit = off`) where losing the last moments is acceptable | no wait for the WAL flush |

```python
# PostgreSQL: COPY streams rows in one statement — typically far faster than INSERT per row
import io
import psycopg2

rows = [(1, "TW", 120.50), (2, "JP", 80.00), (3, "US", 42.10)]
buf = io.StringIO("".join(f"{oid}\t{country}\t{amount}\n" for oid, country, amount in rows))

with psycopg2.connect(dsn) as conn, conn.cursor() as cur:
    cur.copy_expert("COPY orders_stage (order_id, country, amount) FROM STDIN", buf)
```

### 6-3) Faster reads

**Reads get faster when the data a query needs is found without scanning, is already in
memory, or was precomputed into the shape the query asks for.**

| Technique | OLTP | Analytics |
|---|---|---|
| **Indexes** — composite, covering, partial | ✅ primary lever | rarely (no B-trees in most warehouses) |
| **Sort / cluster for data skipping** | — | ✅ primary lever |
| **Caching** — buffer pool, Redis, BI cache | ✅ | ✅ for dashboards |
| **Read replicas** | ✅ scale reads, offload extracts | — |
| **Materialised views / summary tables** | sometimes | ✅ |
| **Partitioning** | pruning + smaller indexes | pruning |
| **Columnar storage** | — | ✅ |

Index rules are in [`./sql_for_data.md` §10-1](./sql_for_data.md#10-1-indexes-in-an-oltp-database);
reading whether the index was used is [`./sql_explain.md`](./sql_explain.md).

### 6-4) Object stores and the lake

**On S3-style storage the cost is per request and per file, not per seek, so performance
comes from fewer, larger files, metadata that avoids listing, and keys spread across
prefixes.**

- S3 sustains at least **3,500 writes and 5,500 reads per second per prefix**; a hot single
  prefix throttles (`503 Slow Down`) — spread keys across prefixes (partitions already do).
- **Listing is slow and paged**; a table format (Iceberg, Delta) reads the file list from
  metadata instead.
- **Rename is a copy** on object storage — commit protocols that rename a temporary directory
  are slow and unsafe; table formats commit by writing a metadata pointer instead.
- A Parquet reader fetches the **footer first**, then only the column chunks it needs, with
  ranged GETs — so wide files with few columns read are cheap, tiny files are not.

### 6-5) Hot spots and contention

**A system is as fast as its busiest partition, lock or key, so spread writes evenly and
avoid sending every update to the same place.**

- **Monotonic keys in distributed stores** (a timestamp as HBase / Bigtable row key prefix,
  a sequential DynamoDB partition key) send every new write to one node — prefix with a hash
  or a bucket number.
- **Hot counters** (`UPDATE stats SET views = views + 1 WHERE id = 1`) serialise on one row's
  lock — shard the counter into N rows and sum on read, or aggregate in a stream first.
- **Long transactions** in PostgreSQL hold back vacuum, so dead rows accumulate and reads slow
  down — keep batch transactions short and chunked (see [§7-4](#7-4-large-updates-and-deletes-in-chunks)).

---

## 7) SQL Rewrites That Make Queries Faster ⭐⭐⭐⭐⭐

### 7-1) The rewrite table

**Most slow SQL is fixed by giving the optimiser a predicate it can use, reducing rows before
an expensive step, or asking for less than the query currently asks for.**

| Slow form | Faster form | Why |
|---|---|---|
| `WHERE DATE(created_at) = '2026-11-01'` | `WHERE created_at >= '2026-11-01' AND created_at < '2026-11-02'` | sargable: the index or partition pruning can be used |
| `WHERE id IN (SELECT …)` that returns many duplicates, or a `JOIN` then `DISTINCT` | `WHERE EXISTS (SELECT 1 …)` | a semi-join stops at the first match and never multiplies rows |
| `WHERE id NOT IN (SELECT col …)` | `WHERE NOT EXISTS (…)` | planned as an anti-join — but **not equivalent** when `col` has `NULL`s: `NOT IN` then returns no rows, `NOT EXISTS` returns the unmatched ones (usually what was meant) |
| join detail, then `GROUP BY` | `GROUP BY` the detail first, then join | fewer rows reach the join |
| `UNION` | `UNION ALL` when the parts cannot overlap | no deduplicating sort or hash |
| `SELECT *` | the columns you use | columnar engines read only those; covering indexes become possible |
| `ORDER BY … OFFSET 100000 LIMIT 50` | keyset: `WHERE (created_at, id) < (?, ?) ORDER BY … LIMIT 50` | `OFFSET` reads and throws away every skipped row |
| `WHERE a = ? OR b = ?` across two indexed columns | two indexed queries combined with `UNION ALL` (excluding overlap) | each branch uses its own index |
| correlated subquery per row | a join or a window function | one pass instead of one lookup per row |
| self-join for "previous row" | `LAG()` | one sort instead of a join |
| exact `COUNT(DISTINCT user_id)` on a dashboard | `APPROX_COUNT_DISTINCT` | a small sketch instead of a full distinct set |

Then **check with `EXPLAIN`** — a rewrite that the optimiser already did for you buys nothing,
and PostgreSQL 12+ already inlines a non-recursive CTE referenced once (unless marked
`MATERIALIZED`).

### 7-2) Worked rewrite: aggregate before the join

**"Revenue per country" over a billion order rows does not need a billion rows to reach the
join — it needs one row per user.**

```sql
-- Before: every order row is joined to users, then grouped
SELECT u.country, SUM(o.amount) AS revenue
FROM   orders o
JOIN   users  u ON u.user_id = o.user_id
WHERE  o.order_date >= DATE '2026-10-01'
GROUP  BY u.country;

-- After: shrink orders to one row per user first, then join the much smaller result
WITH user_revenue AS (
  SELECT user_id, SUM(amount) AS revenue
  FROM   orders
  WHERE  order_date >= DATE '2026-10-01'
  GROUP  BY user_id
)
SELECT u.country, SUM(r.revenue) AS revenue
FROM   user_revenue r
JOIN   users        u ON u.user_id = r.user_id
GROUP  BY u.country;
```

It returns the same answer because `SUM` is decomposable — a sum of per-user sums is the
total. It does **not** work for `COUNT(DISTINCT …)` across the join or for averages computed
as an average of averages (see [`./analytics.md` §2-3](./analytics.md#2-3-ratio-metrics-and-the-averaging-trap)).

### 7-3) Worked rewrite: keyset pagination

```sql
-- Before: page 2001 reads 100 050 rows and returns 50
SELECT order_id, created_at, amount
FROM   orders
ORDER  BY created_at DESC, order_id DESC
OFFSET 100000 LIMIT 50;

-- After: remember the last row of the previous page; an index on (created_at, order_id) seeks to it
SELECT order_id, created_at, amount
FROM   orders
WHERE  (created_at, order_id) < (TIMESTAMP '2026-10-03 08:15:00', 918273)
ORDER  BY created_at DESC, order_id DESC
LIMIT  50;
```

The tie-breaker (`order_id`) makes the order total, so no row is skipped or repeated when
two rows share a timestamp. The sort keys must be `NOT NULL`: a row comparison with a `NULL`
is unknown, so a page ending on a `NULL` `created_at` returns an empty next page — declare the
columns `NOT NULL` or `COALESCE` them in both the `ORDER BY` and the predicate. The cost: no
"jump to page 2001" — only next / previous.

### 7-4) Large updates and deletes in chunks

**One `DELETE` of 200 million rows holds locks for its whole duration, writes all of its WAL
at once and leaves a vacuum backlog; deleting in bounded chunks keeps each transaction short.**

```sql
-- PostgreSQL: delete in batches of 10 000 by primary key; repeat until it affects 0 rows.
-- (Not by ctid: on a partitioned table two partitions can share a ctid.)
DELETE FROM events
WHERE  event_id IN (
  SELECT event_id
  FROM   events
  WHERE  event_date < DATE '2025-01-01'
  LIMIT  10000
);
```

- Better still on a partitioned table: **`DROP` or detach the old partition** — metadata only,
  no row-by-row delete.
- In a lakehouse, a `DELETE` rewrites files (copy-on-write) or writes delete files
  (merge-on-read); scope it with a partition filter, then compact.

---

## 8) Quick Q → A ⭐⭐⭐⭐

| Question | Answer |
|---|---|
| Parquet or Avro for the lake? | Parquet for analytical tables; Avro for messages and raw row-wise landing |
| Why are many small files slow? | each costs an open, a metadata entry and often a task; reading 10 000 × 1 MB files is far slower than 10 × 1 GB |
| `repartition` or `coalesce` before a write? | `coalesce` to reduce file count cheaply; `repartition` by the partition column when files per partition must be few and balanced |
| Spark driver OOM — first thing to look for? | a `collect()` / `toPandas()`, an oversized broadcast, or planning over millions of files |
| Executor OOM — first thing? | the max task input in the stage vs the median: skew, or too few shuffle partitions |
| PostgreSQL query is slow and the plan says `external merge` | the sort spilled: raise `work_mem` for that session, or sort fewer rows / use an index that already orders them |
| Trino query fails with a memory limit | make the build (right) side smaller, collect statistics, use a partitioned join, pre-aggregate |
| How do you speed up a bulk load? | `COPY`, large batches, drop and rebuild secondary indexes, sequential keys |
| Index makes reads fast — why not index everything? | each index slows every write and takes space; index for the queries you run |
| Z-order or sort? | sort when one column dominates the filters; Z-order when two or three are used about equally |
| Copy-on-write or merge-on-read? | CoW for read-heavy, batch-updated tables; MoR for frequent small updates, then compact |
| Why does `MERGE` fail with "affect row a second time"? | the source had two rows for one key — deduplicate with `ROW_NUMBER()` first |
| `OFFSET` pagination is slow on deep pages | switch to keyset pagination on an indexed, unique sort key |
| How does a database sort data larger than memory? | external merge sort: sorted runs on disk, then a k-way heap merge |
