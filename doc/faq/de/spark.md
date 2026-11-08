# Apache Spark — Data Engineering Interview Deep Dive

> **Scope** — everything about Apache Spark a data engineering interview probes: the execution model, the APIs and the optimiser, shuffles, partitioning, joins and skew, caching, memory and serialisation, PySpark specifics, writes, Structured Streaming and its watermarks, tuning, debugging and the common failures — answered for Spark 3.5, with Spark 4.0 differences called out.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the high-level map and last-minute checklist across all DE topics; [`./stream_pipeline.md`](./stream_pipeline.md) — streaming in depth; [`./batch_pipeline.md`](./batch_pipeline.md) — where a Spark job sits in a pipeline; [`./hadoop.md`](./hadoop.md) — HDFS, YARN and MapReduce; [`../spark/faq_spark_hadoop.md`](../spark/faq_spark_hadoop.md) — the question bank: short spoken answers, troubleshooting walk-throughs, pipeline-design prompts and a study plan.

Every answer leads with the sentence you would say out loud, then the mechanism, then the
trade-off the interviewer pushes on next. Configuration names and defaults are Spark 3.5's
unless a section says otherwise.

---

## 0) How to Use This Doc with the Question Bank

**This is the deep dive: the mechanism behind each answer. The question bank,
[`faq_spark_hadoop.md`](../spark/faq_spark_hadoop.md), holds the short spoken answers, the
drills and the study plan, and its [review order](../spark/faq_spark_hadoop.md#0-2-review-order)
lines the two files up step by step.**

- Follow that review order, not §1 → §16. This file's sections follow how Spark is built,
  which is not the order an interview probes it.
- Come here when a question-bank answer does not survive "why?". Each of its sections links to
  the matching section here.
- §10 (PySpark) and §11 (writes and table formats) have no counterpart in the question bank.
  Read them for a data-engineering loop.
- §16-1's quick-fire answers are a recap. The full mock bank, with difficulty levels, is the
  question bank's [§14-2](../spark/faq_spark_hadoop.md#14-2-the-question-bank-by-difficulty).

---

## 1) What Spark Is, and Where It Fits ⭐⭐⭐⭐

### 1-1) The one-paragraph answer

**Spark is a distributed, in-memory-first compute engine: you describe a computation over a
dataset, Spark turns it into a plan of parallel tasks, and runs those tasks on a cluster,
recomputing lost pieces from their lineage.**

- **Unified engine** — batch (Spark SQL / DataFrames), streaming (Structured Streaming),
  machine learning (MLlib) and graph processing share one engine and one API surface.
- **Storage-agnostic** — reads and writes HDFS, S3 / GCS / ADLS, JDBC, Kafka, Delta, Iceberg,
  Hudi, Parquet, ORC, Avro, JSON, CSV.
- **Languages** — Scala, Java, Python (PySpark), SQL and R; Spark Connect (3.4+) adds a thin
  client that talks to a remote driver over gRPC.

### 1-2) Spark vs MapReduce

**Spark keeps intermediate results in memory and plans a whole DAG of stages; MapReduce
writes to HDFS between every job, so multi-step and iterative work is much slower on it.**

| | MapReduce | Spark |
|---|---|---|
| Intermediate data | written to HDFS between jobs | in memory / local disk between stages |
| Plan | one map + one reduce per job | a DAG of many stages, optimised as a whole |
| API | low-level map / reduce | DataFrames, SQL, RDDs |
| Iterative jobs (ML, graph) | re-read input every iteration | cache once, iterate in memory |
| Fault tolerance | re-run a failed task from HDFS input | recompute from lineage |
| Streaming | none | Structured Streaming |

Spark still shuffles through local disk, so "in-memory" does not mean "never touches disk".

### 1-3) Spark vs the alternatives

| Engine | Pick it when |
|---|---|
| **Spark** | large batch ETL, mixed SQL + code, ML pipelines, one engine for batch and micro-batch streaming |
| **Flink** | low-latency, record-at-a-time streaming with large state |
| **Trino / Presto** | interactive SQL over a lake, no long-running ETL |
| **Cloud warehouse (BigQuery, Snowflake)** | SQL-only transforms, no cluster to run |
| **DuckDB / Polars / pandas** | data that fits on one machine — usually faster and far simpler |

The honest interview line: **"if it fits on one machine, don't use Spark."**

---

## 2) Architecture and the Execution Model ⭐⭐⭐⭐⭐

### 2-1) Driver, cluster manager, executors

**One driver plans the job, many executors run it in parallel, and a cluster manager hands
out the machines.**

| Component | Role |
|---|---|
| **Driver** | runs your `main()`, holds the `SparkSession`, builds the DAG, schedules tasks, collects results |
| **Cluster manager** | allocates resources — YARN, Kubernetes, Standalone (Mesos is deprecated) |
| **Executor** | a JVM on a worker node; runs tasks in threads, caches data, writes shuffle files |
| **Task** | the unit of work: one task processes **one partition** of one stage |

```text
            ┌──────────── Driver ────────────┐
            │ SparkSession → DAG → Stages     │
            │ DAGScheduler / TaskScheduler    │
            └──────┬──────────────────┬───────┘
                   │ request resources │
            ┌──────▼──────┐            │ send tasks
            │Cluster Mgr  │            │
            │YARN / K8s   │            │
            └──────┬──────┘            │
        ┌──────────┴───────┬───────────▼──────┐
   ┌────▼─────┐      ┌─────▼────┐       ┌─────▼────┐
   │Executor 1│      │Executor 2│  ...  │Executor N│
   │task task │      │task task │       │task task │
   │ cache    │      │ cache    │       │ cache    │
   └──────────┘      └──────────┘       └──────────┘
```

- Concurrent task slots = executors × `spark.executor.cores`. A 200-partition stage on 50
  slots (say 10 executors × 5 cores) runs in 4 waves; actual throughput still depends on the
  workload and I/O.
- The driver is a single point of failure for the application: if it dies, the job dies
  (YARN can retry the whole application with `spark.yarn.maxAppAttempts`).

### 2-2) Deploy modes and `spark-submit`

**In `client` mode the driver runs where you submitted from; in `cluster` mode it runs inside
the cluster — production jobs use `cluster`, so a laptop disconnect cannot kill them.**

```bash
spark-submit \
  --master yarn \
  --deploy-mode cluster \
  --num-executors 10 \
  --executor-cores 5 \
  --executor-memory 16g \
  --driver-memory 4g \
  --conf spark.sql.shuffle.partitions=400 \
  --conf spark.sql.adaptive.enabled=true \
  etl_daily_orders.py --run-date 2026-11-01
```

| Mode | Driver runs on | Use for |
|---|---|---|
| `client` | the submitting machine | notebooks, interactive shells, debugging |
| `cluster` | a node inside the cluster | scheduled production jobs |
| `local[*]` | one JVM, one thread per core | unit tests, small experiments |

### 2-3) Job → stage → task, and the two schedulers

**An action creates a job; the DAGScheduler cuts the job into stages at shuffle boundaries;
the TaskScheduler sends one task per partition of each stage to an executor.**

```text
Job   = everything one action needs
Stage = a run of narrow transformations between two shuffles
Task  = one partition of one stage, run on one core

df.read ─ filter ─ withColumn │ shuffle │ groupBy().agg ─ write
└──────── Stage 0 ───────────┘         └──── Stage 1 ─────┘
  200 input partitions → 200 tasks        shuffle partitions → N tasks
```

- **DAGScheduler** — builds stages, tracks which shuffle outputs exist, resubmits a stage
  whose shuffle files were lost.
- **TaskScheduler** — places tasks, preferring **data locality** (`PROCESS_LOCAL` →
  `NODE_LOCAL` → `RACK_LOCAL` → `ANY`, waiting up to `spark.locality.wait`, default 3s,
  at each level), and retries a failed task up to `spark.task.maxFailures` (default 4).
- Stages with no dependency on each other can run at the same time.

### 2-4) Fault tolerance: lineage, retries and speculation

**Spark does not replicate computed data; it remembers how each partition was built and
recomputes a lost one from its parents.**

- **Lineage** — every RDD / DataFrame knows its parent and the function applied. Lose an
  executor and its cached partitions are recomputed; lose its shuffle files and the map
  stage that produced them is re-run (unless an external shuffle service still serves them).
- **Task retry** — a failed task is retried on another executor; after
  `spark.task.maxFailures` failures the stage, and the job, fails.
- **Speculation** (`spark.speculation=true`) — launches a duplicate of an unusually slow
  task and takes whichever finishes first. It helps with a bad node; it does nothing for
  skew, because the duplicate has the same oversized partition. It also means side effects
  inside tasks can run twice.
- **Checkpointing** (`df.checkpoint()` with `spark.sparkContext.setCheckpointDir(...)`)
  writes data to reliable storage and cuts the lineage — useful for long iterative plans.

### 2-5) Dynamic allocation

**Dynamic allocation adds executors when tasks queue up and releases idle ones, so one job
does not hold a fixed fleet for its whole run.**

- `spark.dynamicAllocation.enabled=true`, bounded by `minExecutors` / `maxExecutors`.
- Releasing an executor would lose its shuffle files, so it needs either an **external
  shuffle service** (YARN) or `spark.dynamicAllocation.shuffleTracking.enabled=true`
  (keeps executors holding live shuffle data, the usual choice on Kubernetes).
- Trade-off: better cluster utilisation against slower ramp-up and executors churned between stages.

---

## 3) APIs: RDD, DataFrame, Dataset ⭐⭐⭐⭐

### 3-1) The comparison

**Use DataFrames: they carry a schema, so Catalyst can optimise them and Tungsten can store
them compactly; an RDD is a collection of opaque objects run through functions Spark cannot
see into.**

| | RDD | DataFrame | Dataset |
|---|---|---|---|
| Schema | none | yes (rows of named, typed columns) | yes, typed objects (`Dataset[T]`) |
| Optimiser | none | Catalyst | Catalyst (lambdas are opaque to it) |
| Memory format | JVM objects | Tungsten binary rows | Tungsten binary rows + encoders |
| Languages | all | all | Scala / Java only |
| Type errors caught | runtime | runtime (column names) | compile time |
| Use when | custom low-level control, unstructured data | almost always | Scala code that wants typed objects |

In Python and SQL a DataFrame is all there is; in Scala, `DataFrame` is literally
`Dataset[Row]`.

### 3-2) Transformations vs actions, and lazy evaluation

**Transformations are lazy and only add to a plan; an action runs the plan — which is what
lets Spark optimise the whole thing before doing any work.**

| | Transformation | Action |
|---|---|---|
| Examples | `select`, `filter`, `withColumn`, `join`, `groupBy().agg`, `repartition` | `count`, `collect`, `show`, `take`, `write.save`, `foreach` |
| Returns | a new DataFrame | a value to the driver, or data written out |
| Executes? | no | yes — submits one or more jobs |

```python
# python — nothing runs until the last line
from pyspark.sql import functions as F

orders = spark.read.parquet("s3://lake/orders/")            # reads only the footer / schema
big = orders.filter(F.col("amount") > 100)                  # lazy
by_day = big.groupBy("order_date").agg(F.sum("amount"))     # lazy
by_day.write.mode("overwrite").parquet("s3://lake/agg/")    # action: the job runs here
```

Consequence people trip on: calling two actions on the same un-cached DataFrame computes it
**twice** from the source.

### 3-3) Narrow vs wide dependencies

**A narrow transformation needs only one input partition per output partition and stays in
the same stage; a wide one needs data from many partitions, so it forces a shuffle and starts
a new stage.**

| Narrow (no shuffle) | Wide (shuffle) |
|---|---|
| `select`, `filter`, `withColumn`, `map` | `groupBy`, `distinct`, `orderBy` |
| `union` | `join` (unless broadcast or co-partitioned) |
| `coalesce` (merges partitions) | `repartition` |
| broadcast hash join (big side) | window functions with `partitionBy` |

### 3-4) RDD-era questions that still get asked

**`reduceByKey` combines on the map side before the shuffle; `groupByKey` ships every value
across the network and then groups — so `reduceByKey` (or a DataFrame `groupBy().agg`) is the
right choice for an aggregation.**

```python
# python — word count, the RDD way
lines = spark.sparkContext.textFile("s3://lake/raw/books/*.txt")
counts = (lines.flatMap(lambda line: line.split())
               .map(lambda word: (word, 1))
               .reduceByKey(lambda a, b: a + b))      # map-side combine, then shuffle
top = counts.takeOrdered(10, key=lambda kv: -kv[1])
```

- `map` vs `flatMap` — one output per input vs zero-or-more outputs per input.
- `mapPartitions` — runs once per partition; open one DB connection per partition, not per row.
- **Broadcast variable** — a read-only value shipped once per executor (a lookup dict).
- **Accumulator** — a write-only counter aggregated on the driver. Updates inside
  **actions** are applied exactly once; updates inside **transformations** can be applied
  more than once when a task or stage is re-run, so do not use them for exact counts there.

---

## 4) Catalyst, Tungsten and AQE ⭐⭐⭐⭐⭐

### 4-1) Catalyst: how a query becomes a plan

**Catalyst turns your DataFrame or SQL into a logical plan, rewrites it with rules, picks a
physical plan, and generates code for it.**

```text
SQL / DataFrame
   │ parse
   ▼
Unresolved logical plan ── analysis (catalog: tables, columns, types) ──► Analysed plan
   │ rule-based optimisation
   ▼
Optimised logical plan   (predicate pushdown, column pruning, constant folding,
   │                      join reordering with CBO stats, subquery rewrites)
   │ physical planning (strategies: which join, which aggregate)
   ▼
Physical plan  ──► whole-stage code generation ──► RDD of InternalRow, run on executors
```

- **Predicate pushdown** — a `filter` moves down to the scan, so Parquet / ORC skip row
  groups by their min / max statistics, and JDBC sources push it into the `WHERE`.
- **Column pruning** — only the referenced columns are read from a columnar file.
- **Partition pruning** — a filter on a partition column skips whole directories;
  **dynamic partition pruning** (3.0+) does it at runtime from the other side of a join.
- **CBO** (`spark.sql.cbo.enabled`) uses table statistics from `ANALYZE TABLE ... COMPUTE
  STATISTICS` for join ordering; without stats, Spark estimates from file sizes.

### 4-2) Tungsten: the execution engine

**Tungsten stores rows in a compact binary format, manages memory itself, and fuses
operators into generated Java code, so a stage runs like one hand-written loop.**

- **UnsafeRow** — binary row format, off the JVM object model: less memory, almost no GC pressure.
- **Whole-stage code generation** — `filter → project → aggregate` become one generated
  function; in `explain()` output these operators are marked with `*(n)`.
- **Cache-aware sorting and hashing** for aggregations and joins.
- What defeats it: Python UDFs, Scala lambdas on Datasets, and very wide schemas
  (code generation is skipped above `spark.sql.codegen.maxFields`).

### 4-3) Adaptive Query Execution (AQE)

**AQE re-optimises the plan between stages using the real sizes of the shuffle output, instead
of trusting estimates made before the job started. It is on by default since Spark 3.2.**

| AQE feature | What it does | Key config |
|---|---|---|
| **Coalesce shuffle partitions** | merges many small post-shuffle partitions into fewer of a target size | `spark.sql.adaptive.coalescePartitions.enabled`, `advisoryPartitionSizeInBytes` (64 MB) |
| **Switch join strategy** | turns a sort-merge join into a broadcast join when one side turns out small | `spark.sql.adaptive.autoBroadcastJoinThreshold` |
| **Skew join** | splits an oversized partition into several tasks, replicating the other side's matching partition | `spark.sql.adaptive.skewJoin.enabled`, `skewedPartitionFactor`, `skewedPartitionThresholdInBytes` |

- AQE only acts at shuffle boundaries — it cannot fix a plan with no exchange.
- By default a partition counts as skewed when it is larger than both
  `skewedPartitionFactor` × the median partition size and `skewedPartitionThresholdInBytes`
  (256 MB).

### 4-4) Reading a physical plan

**`df.explain("formatted")` shows the physical plan: read it bottom-up, and look for the scan
filters, every `Exchange` (a shuffle), and the join operator.**

```python
# python
(spark.table("sales").filter("sale_date = '2026-11-01'")
      .join(spark.table("stores"), "store_id")
      .groupBy("region").sum("amount")
      .explain(mode="formatted"))
```

| Plan node | Means |
|---|---|
| `FileScan parquet ... PushedFilters: [...]`, `PartitionFilters: [...]` | pushdown and partition pruning worked |
| `Exchange hashpartitioning(key, 200)` | a shuffle on `key` into 200 partitions |
| `BroadcastExchange` + `BroadcastHashJoin` | the small side is broadcast; no shuffle of the big side |
| `SortMergeJoin` with a `Sort` and an `Exchange` under each side | both sides shuffle and sort |
| `HashAggregate` (partial) → `Exchange` → `HashAggregate` (final) | two-phase aggregation with map-side combine |
| `AdaptiveSparkPlan isFinalPlan=false` | AQE will revise this plan at runtime — check the SQL tab for the final one |
| `*(2)` prefix | the operator is inside whole-stage codegen stage 2 |

`explain` modes: `simple`, `extended` (all four plans), `codegen`, `cost`, `formatted`.

---

## 5) Shuffles ⭐⭐⭐⭐⭐

### 5-1) What a shuffle actually does

**A shuffle redistributes rows so that all rows with the same key land in the same partition:
every map task writes its output to local disk sorted by target partition, and every reduce
task fetches its slice from every map task over the network.**

```text
Map side (stage N)                        Reduce side (stage N+1)
task 0: rows → partition by hash(key)     task 0 fetches block 0 from every map output
        → sort / spill → data file        task 1 fetches block 1 from every map output
          + index file (one per task)     ...
task 1: ...                               then aggregate / join the fetched rows
        M map tasks × R reduce partitions = up to M × R blocks fetched
```

- Spark uses the **sort-based shuffle**: one data file and one index file per map task.
- Cost: serialisation, disk write, network transfer, disk read, deserialisation — usually the
  most expensive part of a job, and where most spills and OOMs happen. [§9-5](#9-5-a-shuffle-byte-by-byte) follows one row
  through it.
- **External shuffle service** keeps serving shuffle files after the executor that wrote
  them is gone (needed for dynamic allocation on YARN).

### 5-2) How to avoid or shrink a shuffle

- **Filter and project early** so less data reaches the exchange (Catalyst does much of
  this; a UDF in the way stops it).
- **Broadcast** the small side of a join.
- **Pre-aggregate** — `groupBy().agg` already combines on the map side; avoid
  `collect_list` then aggregate.
- **Bucket** tables written repeatedly joined on the same key (section 6-4).
- **Do not repartition "just in case"** — every `repartition` is a full shuffle.
- Compress shuffle data (`spark.shuffle.compress`, on by default) and serialise with Kryo
  for RDD workloads. Kryo does not change a DataFrame shuffle ([§9-4](#9-4-the-serialisers-java-kryo-and-tungstens-binary-rows)).

### 5-3) `spark.sql.shuffle.partitions`

**It sets how many partitions every DataFrame shuffle produces — 200 by default, which is
wrong for both tiny and huge jobs.**

| Symptom | Likely cause | Fix |
|---|---|---|
| thousands of tasks finishing in milliseconds | too many partitions for the data | lower it, or let AQE coalesce |
| a few tasks spilling GBs to disk | too few partitions | raise it so each is ~100–200 MB |
| one task far slower than the rest | skew, not the count | section 7-3 |

Rule of thumb: total shuffle size ÷ ~128 MB, and at least 2–3× the number of cores. With AQE
on, set it generously high and let coalescing merge the small partitions.

---

## 6) Partitioning and File Layout ⭐⭐⭐⭐⭐

### 6-1) Partitions in memory

**A partition is the chunk of data one task processes; the number of partitions is the
parallelism of a stage.**

- **On read**, file sources split input into partitions of up to
  `spark.sql.files.maxPartitionBytes` (default 128 MB); a non-splittable file (gzip CSV) is
  one partition no matter its size.
- **After a shuffle**, the count is `spark.sql.shuffle.partitions` (or AQE's coalesced count).
- `df.rdd.getNumPartitions()` tells you the current count.

### 6-2) `repartition` vs `coalesce`

**`repartition(n)` does a full shuffle and can go up or down; `coalesce(n)` only merges
existing partitions without a shuffle, so it can only go down.**

| | `repartition(n)` / `repartition(n, col)` | `coalesce(n)` |
|---|---|---|
| Shuffle | yes | no |
| Result | evenly sized partitions (round-robin or hash by `col`) | possibly uneven |
| Use for | more parallelism; co-locating a key before a partitioned write | fewer output files at the end of a job |
| Trap | costs a full shuffle | `coalesce(1)` collapses the whole upstream stage onto one task |

`repartitionByRange(n, col)` range-partitions by sampling — useful before writing sorted output.

### 6-3) `partitionBy` on write, and the small-files problem

**`df.write.partitionBy("dt")` is a directory layout for pruning on read, not the same thing
as in-memory partitions — and every in-memory partition can write one file into every
directory value it holds.**

```python
# python — one tidy set of files per date: shuffle by the partition column first
(df.repartition("event_date")
   .write.mode("overwrite")
   .partitionBy("event_date")
   .parquet("s3://lake/events/"))
```

- Without the `repartition`, 200 tasks × 30 dates can write 6,000 small files.
- Small files hurt every reader: one task per file, footer reads, slow object-store listings,
  NameNode memory on HDFS.
- Fixes: `coalesce` / `repartition` before the write, AQE coalescing, a compaction job
  (Delta `OPTIMIZE`, Iceberg `rewrite_data_files`). `maxRecordsPerFile` only caps a file's
  size; it never merges files.
- Partition by a **low-cardinality** column queries filter on (a date). Partitioning by
  `user_id` creates millions of directories.

### 6-4) Bucketing

**Bucketing hashes rows into a fixed number of files by a key at write time, so a later join
or aggregation on that key can skip the shuffle.**

```python
# python — bucketing requires saveAsTable (it is recorded in the metastore)
(orders.write.bucketBy(64, "customer_id").sortBy("customer_id")
       .mode("overwrite").saveAsTable("analytics.orders_bucketed"))
```

- Both sides need the same bucket column and compatible bucket counts for the shuffle to be skipped.
- Spark's bucketing is not compatible with Hive's bucketing layout.
- Lakehouse formats offer alternatives: Delta Z-order / liquid clustering, Iceberg sort
  orders and hidden partitioning.

---

## 7) Joins and Data Skew ⭐⭐⭐⭐⭐

### 7-1) The join strategies

**Spark picks a broadcast hash join when one side is small enough, and a sort-merge join
otherwise; everything else is a special case.**

| Strategy | When | Shuffle | Notes |
|---|---|---|---|
| **Broadcast hash join** | one side under `spark.sql.autoBroadcastJoinThreshold` (10 MB) or hinted | none on the big side | small side collected to the driver, sent to every executor |
| **Shuffle hash join** | one side much smaller per partition, but too big to broadcast | both | builds a hash table per partition; can OOM |
| **Sort-merge join** | both sides large, equi-join — the default | both | sorts each side; spills gracefully |
| **Broadcast nested loop join** | non-equi join with a small side | none | O(n × m) |
| **Cartesian product** | non-equi join, both sides large | both | almost always a bug |

```python
# python — hint the strategy
from pyspark.sql.functions import broadcast

joined = facts.join(broadcast(dim_store), "store_id")
```

```sql
-- Spark SQL join hints (3.0+): BROADCAST, MERGE, SHUFFLE_HASH, SHUFFLE_REPLICATE_NL
SELECT /*+ BROADCAST(s) */ f.*, s.region
FROM   sales f
JOIN   stores s ON f.store_id = s.store_id;
```

Broadcasting something too large moves the OOM from the executors to the driver — the
threshold exists for a reason.

### 7-2) Join types and their traps

| Join | Returns | Trap |
|---|---|---|
| `inner` | matching rows | duplicate keys on both sides multiply rows |
| `left` / `right` / `full` | all rows of one / both sides | a later filter on the nullable side turns it back into an inner join |
| `left_semi` | left rows that have a match (left columns only) | the efficient `EXISTS` |
| `left_anti` | left rows with no match | the efficient, `NULL`-safe `NOT EXISTS` |
| `cross` | every pair | explode in row count |

- `NULL` keys never match in an equi-join; use `eqNullSafe` (`<=>` in SQL) if they must.
- Joining on columns with the same name via a list (`join(df2, "id")`) avoids the ambiguous
  duplicate `id` column you get with `df1.id == df2.id`.

### 7-3) Data skew: spotting it and fixing it

**Skew is a few keys holding most of the rows, so one task runs for an hour while the rest
finish in a minute — total job time is set by the slowest task.**

How to spot it: in the Spark UI stage page, the max task duration and shuffle-read size are
many times the median, and one task is still running long after the others.

How to fix it, in the order you would try:

1. **AQE skew join** — `spark.sql.adaptive.skewJoin.enabled=true` (default with AQE) splits
   oversized partitions of a sort-merge join automatically.
2. **Broadcast** the small side, so the big side never shuffles by the key.
3. **Handle the hot key separately** — it is often `NULL`, `-1`, `"unknown"` or a test
   account: filter it out, join the rest normally, process the hot key with a broadcast, `union`.
4. **Salting** — append a random suffix to the key on the big side and replicate the small
   side once per suffix.
5. **Two-stage aggregation** for a skewed `groupBy` — aggregate by `(key, salt)`, then by `key`.

```python
# python — salting a skewed join
from pyspark.sql import functions as F

SALT = 16
big_salted = big.withColumn("salt", (F.rand() * SALT).cast("int"))
small_salted = small.crossJoin(spark.range(SALT).withColumnRenamed("id", "salt"))
joined = big_salted.join(small_salted, ["key", "salt"]).drop("salt")
```

```python
# python — two-stage aggregation for a skewed groupBy
partial = (events.withColumn("salt", (F.rand() * 16).cast("int"))
                 .groupBy("key", "salt").agg(F.sum("amount").alias("part")))
result = partial.groupBy("key").agg(F.sum("part").alias("amount"))
```

Salting only works for aggregations that can be split and recombined (sum, count, max) —
not for an exact `countDistinct` without extra care.

---

## 8) Caching and Persistence ⭐⭐⭐⭐

### 8-1) When to cache

**Cache a DataFrame only when more than one action reuses it and recomputing it is expensive;
otherwise caching costs memory and buys nothing.**

- Good: a cleaned dataset used by several aggregations; an iterative ML loop; a dimension
  joined many times in one job.
- Bad: a DataFrame used once; a huge dataset that evicts everything else; caching "for speed"
  before a single write.
- `cache()` is lazy — the data is materialised by the next action; `count()` is the usual
  way to force it. Always `unpersist()` when done.

### 8-2) Storage levels

| Level | Stored as | Falls back to |
|---|---|---|
| `MEMORY_ONLY` | deserialised objects in memory | recompute missing partitions |
| `MEMORY_AND_DISK` | in memory, spilling to local disk | disk |
| `MEMORY_ONLY_SER` / `MEMORY_AND_DISK_SER` (Scala / Java) | serialised bytes — smaller, more CPU | recompute / disk |
| `DISK_ONLY` | local disk | — |
| `OFF_HEAP` | off-heap memory | needs `spark.memory.offHeap.enabled` |
| `..._2` variants | the same, replicated on two executors | faster recovery, twice the memory |

- `RDD.cache()` = `MEMORY_ONLY`; `DataFrame.cache()` = `MEMORY_AND_DISK` (stored in
  Tungsten's columnar in-memory format).
- Cached data lives on executors — losing one recomputes its partitions from lineage.

### 8-3) Cache vs checkpoint vs writing to storage

| | `cache` / `persist` | `checkpoint` | write to a table |
|---|---|---|---|
| Lineage kept | yes | cut | cut (a new source) |
| Survives the app | no | the files do, but they are for this app | yes |
| Where | executor memory / local disk | reliable storage (HDFS / S3) | lake / warehouse |
| Use for | reuse within one job | very long lineage in iterative jobs | handing data to another job |

---

## 9) Memory, Serialisation and Resource Sizing ⭐⭐⭐⭐

### 9-1) Unified memory management

**Executor heap is split into a reserved slice, user memory, and a unified region that
execution (shuffles, joins, sorts, aggregations) and storage (cache, broadcasts) share —
execution can evict cached blocks, storage cannot evict execution.**

```text
Executor container (YARN / K8s pod)
├── spark.executor.memoryOverhead     off-heap: JVM metaspace, thread stacks, NIO buffers,
│                                     Python workers (default max(384 MB, 10% of executor memory))
└── spark.executor.memory (JVM heap)
    ├── Reserved                      300 MB
    ├── User memory                   (1 − spark.memory.fraction) of the rest — your objects, UDF data
    └── Unified memory                spark.memory.fraction = 0.6 of (heap − 300 MB)
        ├── Storage                   spark.memory.storageFraction = 0.5 is protected from eviction
        └── Execution                 can borrow all free storage, and evict cache above the protected part
```

- When execution memory runs out, operators **spill** to disk (slow but survives); an OOM
  happens when something cannot spill — a huge single record, a shuffle-hash build side, a
  Python worker, or overhead exhausted.
- The container is killed by YARN / Kubernetes when heap + overhead exceed its limit —
  "Container killed for exceeding memory limits" means **overhead**, not heap.

### 9-2) Sizing executors

**Use mid-sized executors — around 4–5 cores and enough memory for each core's share of a
partition — rather than one huge executor per node or one core per executor.**

| Choice | Problem |
|---|---|
| very large executors (all cores, 100+ GB heap) | long GC pauses, HDFS client throughput limits per JVM |
| tiny executors (1 core) | no sharing of broadcasts / cache, JVM overhead per core |
| ~4–5 cores, memory ≈ cores × (partition working set) + overhead | the common starting point |

- Leave room on each node for the OS and the NodeManager / kubelet.
- PySpark: Python workers live outside the heap, so raise `spark.executor.memoryOverhead`
  or set `spark.executor.pyspark.memory` explicitly.
- Driver memory matters only for what comes back to it: `collect`, `toPandas`, broadcasts,
  and very large plans / file listings.

### 9-3) Serialisation and deserialisation: what and where

**Serialisation turns an in-memory object into bytes that can cross a process boundary or sit
on disk; deserialisation rebuilds the object from those bytes. Spark does it every time data
leaves an executor's heap — shipping tasks, shuffles, serialised caching, broadcasts, results
and the Python boundary — so every one of those paths pays CPU for it.**

An object on the JVM heap is a graph of headers and pointers into one process's memory. Another
executor, another machine or a file cannot follow those pointers, so the data has to be
flattened into a self-contained byte stream first.

```text
executor A heap               on the wire / on disk             executor B heap
("U1", 100)   ── serialise ──▶  0a 02 55 31 64 …  ── deserialise ──▶  ("U1", 100)
object graph: headers,          flat, self-contained,               a new object graph,
pointers, boxed fields          compressible                        rebuilt field by field
```

| Where | Direction | What is serialised | With what |
|---|---|---|---|
| Task launch | driver → executor | the task: your function and everything it captures | always Java serialisation, whatever `spark.serializer` says |
| Shuffle | map task → local disk → reduce task | every row that crosses the exchange | DataFrame: `UnsafeRowSerializer`; RDD: `spark.serializer` |
| Serialised caching | heap ↔ storage memory / disk | the cached partitions | RDD `*_SER` levels: `spark.serializer`; `DataFrame.cache()`: compressed columnar batches built by Spark SQL |
| Broadcast | driver → executors, in blocks | the broadcast value | `spark.serializer`, then compressed |
| Results | executor → driver | `collect()` / `take()` output | `spark.serializer`, capped by `spark.driver.maxResultSize` |
| Python boundary | JVM ↔ Python worker | rows into and out of Python UDFs and RDD functions | pickle (cloudpickle for functions), Arrow for pandas UDFs — §10 |

**Closures are serialised too**, so a lambda that captures a non-serialisable object (a DB
connection, a logger, the enclosing class) fails with `Task not serializable` before a single
task runs. Create the object on the executor instead, and make the write idempotent: a retried
or speculative task runs `insert` again ([§15-3](#15-3-exactly-once-is-your-job-in-batch-too)).

```scala
// scala — fails: `conn` is opened on the driver and captured by the closure
val conn = DriverManager.getConnection(url)
rdd.foreach(row => insert(conn, row))            // Task not serializable

// works: the connection is opened on the executor, once per partition
rdd.foreachPartition { rows =>
  val conn = DriverManager.getConnection(url)
  try rows.foreach(row => insert(conn, row)) finally conn.close()
}
```

### 9-4) The serialisers: Java, Kryo and Tungsten's binary rows

**`spark.serializer` chooses between Java serialisation (the default) and Kryo for RDD records,
broadcasts and results. DataFrame rows go through neither: Tungsten keeps them as `UnsafeRow`
bytes from the start, so a SQL shuffle has nothing to convert.**

| | Java (`JavaSerializer`) | Kryo (`KryoSerializer`) | Tungsten (`UnsafeRow`) |
|---|---|---|---|
| Used for | the default for RDD data, broadcasts and results; always for closures | RDD data, broadcasts and results once `spark.serializer` names it | DataFrame and SQL rows in shuffles, sorts and aggregations |
| Encoding | class metadata plus every field, through `java.io.Serializable` | compact fields; a registered class is written as a small integer id | a null bitmap and fixed-width value slots, variable-length data at the end — already bytes |
| Speed and size | slowest and largest | Spark's tuning guide: "often as much as 10x" faster and more compact | no object conversion on the shuffle path |
| Needs | the class implements `Serializable` | classes registered for the compact form; a buffer big enough for the largest object | a schema, which is why only DataFrames and Datasets get it |

`spark.serializer` is a core setting: set it before the `SparkContext` starts (builder,
`spark-submit --conf` or `spark-defaults.conf`). On Spark 3, `spark.conf.set("spark.serializer", …)`
on a running session is rejected.

```python
# python
spark = (SparkSession.builder
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .config("spark.kryo.classesToRegister", "com.acme.Click,com.acme.Session")
    .config("spark.kryo.registrationRequired", "true")  # fail fast on an unregistered class
    .config("spark.kryoserializer.buffer.max", "256m")  # default 64m
    .getOrCreate())
```

**Traps**

- **Kryo does not speed up a DataFrame shuffle.** SQL exchanges use `UnsafeRowSerializer`
  whatever `spark.serializer` says. Kryo pays off for RDD code and broadcasts of JVM objects.
- **An unregistered class still works, slowly.** Unless `registrationRequired` is on, Kryo writes
  the full class name with every object, which is the overhead you switched to Kryo to remove.
- **`Buffer overflow. Available: 0, required: …`** means one object outgrew
  `spark.kryoserializer.buffer.max`; raise it (it must stay under 2048m).
- **Typed Dataset lambdas convert every row.** `ds.map(o => …)` deserialises each `UnsafeRow`
  into a JVM object and serialises the result back — `DeserializeToObject` and
  `SerializeFromObject` in the plan. Column expressions skip both.
- **Spark already uses Kryo internally** to shuffle RDDs of primitive types, arrays of them and
  strings (since Spark 2.0), so switching helps most with RDDs of your own classes.

### 9-5) A shuffle, byte by byte

**A row that crosses a shuffle is serialised on the map side, buffered, compressed and written
to a local shuffle file, fetched over the network by the reduce task, then decompressed and
deserialised — CPU twice, disk twice and the network once, for every row.**

```python
# python
orders = spark.createDataFrame(
    [("U1", 100), ("U2", 200), ("U1", 300), ("U3", 400), ("U2", 500), ("U1", 600)],
    ["user_id", "amount"])
orders.groupBy("user_id").sum("amount").show()   # U1 1000, U2 700, U3 400
```

```text
illustrative layout: 3 map tasks, 3 shuffle partitions

map side: partial sums first                 reduce side: hash(user_id) mod 3
task 0: (U1,100) (U2,200) → U1:100 U2:200    p0 ◀ U1:100 U1:300 U1:600 → U1 1000
task 1: (U1,300) (U3,400) → U1:300 U3:400    p1 ◀ U2:200 U2:500        → U2  700
task 2: (U2,500) (U1,600) → U2:500 U1:600    p2 ◀ U3:400               → U3  400

map task                                     reduce task
1. compute the row, pick its partition id    5. fetch its block from every map output:
2. serialise it into the writer's buffers      network, or a local read when co-located
3. group the bytes by partition id: with        (up to spark.reducer.maxSizeInFlight = 48m)
   ≤ 200 partitions, as here, the bypass     6. decompress and deserialise
   writer streams each to its own file, no   7. aggregate, join or sort
   sort; the sort writers sort and spill
   sorted runs when memory runs out
4. end with one data file + one index file,
   compressed with lz4, through a 32k
   spark.shuffle.file.buffer per stream
```

The diagram's 3 × 3 layout is for illustration. A real run splits the list by the session's
default parallelism, shuffles into `spark.sql.shuffle.partitions` (200) partitions, and AQE may
coalesce those. Both 3 and 200 are within the bypass threshold, so the writer is the same.

The partial aggregate (`HashAggregate` before the `Exchange`, [§4-4](#4-4-reading-a-physical-plan)) is what shrinks the
shuffle. It is an operator of its own, so the shuffle under it has no map-side combine. Here
every map task holds distinct keys, so nothing combines; with millions of rows per key it
sends one partial sum per key per task instead of every row.

How the map side sorts depends on which writer Spark picks:

| Writer | Chosen when | What it sorts |
|---|---|---|
| `BypassMergeSortShuffleWriter` | no map-side combine in the shuffle, and at most `spark.shuffle.sort.bypassMergeThreshold` (200) reduce partitions | nothing: one file per reduce partition, concatenated at the end |
| `UnsafeShuffleWriter` (serialised shuffle) | no map-side combine, a serialiser that can relocate serialised records (Kryo, `UnsafeRowSerializer`; not Java), fewer than 16,777,216 partitions | 8-byte pointers ordered by partition id; records are never deserialised to be sorted |
| `SortShuffleWriter` | anything else, e.g. `reduceByKey` with map-side combine | deserialised objects, by partition id; when combining, also by key hash so equal keys merge — not a key order |

The serialised shuffle sorts by **partition id only**, not by key. Ordering by key, for a
sort-merge join, is a separate `Sort` on the reduce side.

| Cost | Where it is paid | Spark UI stage metric |
|---|---|---|
| CPU | serialise and compress (map), decompress and deserialise (reduce), sort | task time not explained by I/O, GC time |
| Disk | shuffle write, spills, reading the files back | Shuffle Write Size, Spill (Memory / Disk) |
| Network | remote block fetches | Shuffle Remote Reads, Shuffle Read Blocked Time |
| Memory | sort buffers, in-flight fetches, aggregation hash maps | Spill first, then OOM |

The shuffle files stay on the map executor's local disk. The external shuffle service keeps
serving them after that executor is removed ([§5-1](#5-1-what-a-shuffle-actually-does)); shuffle tracking instead keeps the
executor alive while its output is still needed ([§2-5](#2-5-dynamic-allocation)). Without either, a lost output
is recomputed by rerunning its map tasks.

### 9-6) Cutting serialisation cost

**First move fewer bytes, then make each byte cheaper: shrink what reaches the exchange, keep
rows in DataFrame operators so they stay `UnsafeRow`, and only then tune the serialiser and
buffers.**

| Lever | Why it helps | More |
|---|---|---|
| Filter and project before the exchange | fewer rows and columns to serialise, write and fetch | [§5-2](#5-2-how-to-avoid-or-shrink-a-shuffle) |
| Broadcast the small side of a join | the big side never crosses a shuffle | [§7-1](#7-1-the-join-strategies) |
| Built-in functions over UDFs and typed lambdas | no `UnsafeRow` ↔ object conversion, no pickling to Python | [§10-2](#10-2-udfs-vs-pandas-udfs-vs-built-ins) |
| Kryo with registered classes | smaller, faster RDD records and broadcasts | [§9-4](#9-4-the-serialisers-java-kryo-and-tungstens-binary-rows) |
| Serialised RDD caching (`MEMORY_ONLY_SER`) | far less heap and GC, for CPU on every read | [§8-2](#8-2-storage-levels) |
| Small closures; broadcast large lookup tables | a 200 MB map captured by a JVM closure is part of the stage's task binary, which every task deserialises; a large PySpark command is broadcast separately, and the Python workers still unpickle it | [§9-3](#9-3-serialisation-and-deserialisation-what-and-where) |
| `spark.shuffle.file.buffer` from 32k to 64k–1m | fewer, larger writes when shuffle write time dominates | below |

- Catalyst already pushes a `filter` written after a `join` below it, so reordering the code
  often changes nothing. Check `explain("formatted")` before claiming a win.
- `spark.shuffle.file.buffer` is per output stream, not per executor, and the bypass writer
  opens one stream per reduce partition. Many streams × a large buffer is real memory, so
  raise it only when shuffle write time is the bottleneck, and watch GC.
- A high **Task Deserialization Time**, or the driver warning *"Broadcasting large task binary"*,
  points at an oversized task: the warning measures the stage's serialised RDD and function, and
  the metric also counts loading dependencies and a task's own partition data. Look for a large
  captured value first, and broadcast it if that is the cause. A different warning,
  *"Stage N contains a task of very large size"*, is about a single serialised task, usually
  one carrying local data from `parallelize`.
- In an interview, name the bottleneck from the Spark UI — serialisation CPU, fetch wait, spill
  or network — before naming a fix. Adding executors helps none of the four by itself.

---

## 10) PySpark Specifics ⭐⭐⭐⭐

### 10-1) How PySpark runs

**PySpark is a Python API over the JVM engine: DataFrame operations are planned and run in
the JVM, and only Python UDFs and RDD lambdas actually run Python code — in separate Python
worker processes on each executor.**

```text
Python driver ──Py4J──► JVM driver (Catalyst, scheduler)
                              │ tasks
                              ▼
                        Executor JVM ──(rows, pickled or Arrow)──► Python worker(s)
                                                                    run your UDF / lambda
```

So a pipeline written only with built-in functions runs at JVM speed from Python.

### 10-2) UDFs vs pandas UDFs vs built-ins

**Prefer built-in functions; if you need custom logic, use a vectorised `pandas_udf`; a plain
Python UDF is the slowest option because it moves rows one at a time.**

| | Built-in (`F.*`) | Python UDF (`@udf`) | Pandas UDF (`@pandas_udf`) |
|---|---|---|---|
| Runs in | JVM, code-generated | Python worker, row by row | Python worker, Arrow batches |
| Serialisation | none | pickle per row | Arrow columnar batches |
| Optimiser visibility | full | none (a black box) | none (a black box) |
| Typical use | everything it can express | quick prototype | custom numeric / string logic at scale |

```python
# python — Spark 3.x pandas UDF with type hints
import pandas as pd
from pyspark.sql.functions import pandas_udf

@pandas_udf("double")
def fahrenheit_to_celsius(f: pd.Series) -> pd.Series:
    return (f - 32) * 5.0 / 9.0

df = df.withColumn("temp_c", fahrenheit_to_celsius("temp_f"))
```

- A UDF also blocks predicate pushdown through it.
- Grouped logic: `groupBy(...).applyInPandas(func, schema)` hands each group to `func` as one
  pandas DataFrame — a large group must fit in one worker's memory.
- Spark 3.5 also has Arrow-optimised Python UDFs (`useArrow=True`), which batch the transfer.

### 10-3) Arrow, `toPandas` and `collect`

- `spark.sql.execution.arrow.pyspark.enabled=true` makes `toPandas()` and
  `createDataFrame(pandas_df)` transfer columnar batches instead of pickled rows.
- `toPandas()` and `collect()` bring **all** the data to the driver — fine for a summary,
  an OOM for a table. Use `limit()`, aggregate first, or write to storage.
- `pyspark.pandas` (formerly Koalas) gives a pandas-like API that executes on Spark.

---

## 11) Spark SQL, Writes and Table Formats ⭐⭐⭐⭐

### 11-1) Spark SQL and the catalog

**Spark SQL and the DataFrame API compile to the same plans; choose by readability, not speed.**

```sql
-- Spark SQL: register and query a view
CREATE OR REPLACE TEMP VIEW orders AS
SELECT * FROM parquet.`s3://lake/orders/`;

SELECT customer_id,
       SUM(amount)                                                        AS total,
       ROW_NUMBER() OVER (PARTITION BY country ORDER BY SUM(amount) DESC) AS rank_in_country
FROM   orders
WHERE  order_date >= DATE '2026-10-01'
GROUP  BY customer_id, country;
```

- Temp views live in one session; global temp views in `global_temp` across sessions in one app;
  tables live in the catalog (Hive Metastore, AWS Glue, Unity Catalog, an Iceberg REST catalog).
- Spark 4.0 turns ANSI SQL mode on by default (`spark.sql.ansi.enabled=true`): invalid casts
  and integer overflow raise errors instead of returning `NULL` — a common upgrade surprise.

### 11-2) Save modes and dynamic partition overwrite

**`overwrite` on a partitioned table replaces the whole table by default; set
`partitionOverwriteMode=dynamic` so it replaces only the partitions present in the data you
write — the setting that makes a daily job safely re-runnable.**

| Save mode | If the target exists |
|---|---|
| `errorifexists` / `error` (default) | fail |
| `append` | add rows — re-running duplicates them |
| `overwrite` | replace (the whole table, or only the written partitions in dynamic mode) |
| `ignore` | do nothing |

```python
# python — idempotent daily load: replaces only event_date = run_date
spark.conf.set("spark.sql.sources.partitionOverwriteMode", "dynamic")

(daily_df.write.mode("overwrite")
         .partitionBy("event_date")
         .parquet("s3://lake/events/"))
```

- `INSERT OVERWRITE TABLE t PARTITION (dt = '2026-11-01') SELECT ...` is the SQL equivalent
  for Hive-format tables.
- Plain Parquet directories have no transactions: a failed overwrite can leave a partition
  half-written, and readers can see it mid-write. Table formats fix this.

### 11-3) Delta Lake / Iceberg / Hudi with Spark

**A table format adds a transaction log over Parquet files, which gives Spark atomic commits,
`MERGE`, time travel and schema evolution on object storage.**

```sql
-- Spark SQL on Delta Lake: upsert a batch of changes
MERGE INTO dim_customer AS t
USING updates AS s
ON    t.customer_id = s.customer_id
WHEN MATCHED THEN UPDATE SET *
WHEN NOT MATCHED THEN INSERT *;
```

| Capability | Plain Parquet | Delta / Iceberg / Hudi |
|---|---|---|
| Atomic overwrite / append | no | yes |
| `UPDATE` / `DELETE` / `MERGE` | rewrite by hand | yes |
| Concurrent writers | unsafe | optimistic concurrency |
| Time travel | no | `VERSION AS OF` / snapshots |
| Small-file compaction | your own job | `OPTIMIZE` / `rewrite_data_files` / Hudi compaction |

See [`./batch_pipeline.md`](./batch_pipeline.md) for how they fit a pipeline and
[`./schema_design.md`](./schema_design.md) for layout and evolution.

### 11-4) Reading data well

- Supply a **schema** for JSON / CSV instead of `inferSchema` — inference costs an extra pass
  and can guess wrong types.
- `mode` for malformed records: `PERMISSIVE` (default, puts bad rows in
  `_corrupt_record`), `DROPMALFORMED`, `FAILFAST`.
- JDBC reads are single-threaded unless you set `partitionColumn`, `lowerBound`,
  `upperBound` and `numPartitions` — and those bounds only decide the stride, they do not
  filter rows.
- `mergeSchema` on Parquet reads reconciles differing file schemas, at a cost; avoid it as a default.

---

## 12) Structured Streaming and Watermarks ⭐⭐⭐⭐

### 12-1) The model

**Structured Streaming treats a stream as an unbounded table and runs your DataFrame query
on it incrementally, by default as a series of micro-batches, with progress tracked in a
checkpoint directory.**

```python
# python — Kafka → windowed counts → Delta, Spark 3.5
from pyspark.sql import functions as F

events = (spark.readStream.format("kafka")
               .option("kafka.bootstrap.servers", "broker:9092")
               .option("subscribe", "clicks")
               .load()
               .select(F.from_json(F.col("value").cast("string"), click_schema).alias("e"))
               .select("e.*"))

counts = (events.withWatermark("event_time", "10 minutes")
                .groupBy(F.window("event_time", "5 minutes"), "page_id")
                .count())

# append: each window is written once, after the watermark passes it
(counts.writeStream.outputMode("append")
       .option("checkpointLocation", "s3://lake/_chk/click_counts")
       .format("delta")
       .start("s3://lake/tables/click_counts"))
```

| Concept | Options |
|---|---|
| Output mode | `append` (only final rows), `update` (changed rows), `complete` (whole result each batch) |
| Trigger | default (next batch when the last ends), `processingTime="1 minute"`, `availableNow=True` (process what exists, then stop — 3.3+) |
| Fault tolerance | offsets + state in `checkpointLocation`; with a replayable source and an idempotent / transactional sink the result is exactly-once |

- Not every sink takes every output mode — the Delta sink takes `append` and `complete`;
  for update semantics use `foreachBatch` with a `MERGE`.
- Changing a query's stateful logic can make an existing checkpoint unusable.

How Spark's watermark works is §12-2 to §12-6. The engine-neutral concepts (window types,
late-data strategies, state backends) and the comparison with Flink and Kafka Streams are in
[`./stream_pipeline.md`](./stream_pipeline.md).

### 12-2) Watermarks: what one is and how Spark computes it

**A watermark is Spark's running estimate of how far event time has progressed: the latest
event time it has seen, minus the lateness you said you would tolerate. It decides two
things — which late rows can still update a result, and which state is old enough to delete.**

Every record carries two times. **Event time** is when the thing happened (a column in the
record); **processing time** is when Spark read it. They drift apart whenever a Kafka consumer
lags, a network retries, or a phone goes offline:

| Order | Event time | Read by Spark at |
|---|---|---|
| A | 10:00 | 10:01 |
| B | 10:02 | 10:03 |
| C | 10:01 | 10:08 — arrives after B and D, happened before both |
| D | 10:04 | 10:05 |

Counting by processing time would put C in the wrong window. Counting by event time is right,
but then Spark cannot know when a window is complete — C proves an old window can still change.
`withWatermark` is the answer to "how long do we wait":

```python
# python — tolerate events up to 10 minutes behind the latest one seen
counts = (orders.withWatermark("event_time", "10 minutes")
                .groupBy(F.window("event_time", "10 minutes"))
                .count())
```

The rule, in Spark 3.5's micro-batch engine:

- **watermark = max(event time seen so far) − delay**, where the maximum is taken across
  **every partition** of the input.
- It is computed **at the end of a micro-batch and used by the next one**. Since Spark 3.4 a
  stateful operator holds two values per batch: it filters late input against the
  **previous** batch's watermark and evicts state against the **current** one. So in the
  batch where the watermark first moves, rows the new value would call late are still let in.
- It **only moves forward**. An old event never pulls it back.
- It moves only when a batch sees a newer event time. If the whole stream goes quiet, the
  watermark stops, and the last windows are not finalised until data arrives again.

| Max event time seen | Delay | Watermark |
|---|---|---|
| 10:10 | 10 min | 10:00 |
| 10:15 | 10 min | 10:05 |
| 10:25 | 10 min | 10:15 |

`withWatermark` has to name the event-time column the stateful operator uses (the column
`window()` is built on, or a grouping / join / dedup column), and it must come before that
operator. On a batch DataFrame it is a no-op.

### 12-3) Late data: what is kept, what is dropped

**Spark guarantees that a row less late than the delay — event time > the max seen minus the
delay — is never dropped because of the watermark. A row at or beyond that may be dropped or may
still be counted; the watermark is a threshold for state, not a strict cut-off for input.**

Take 10-minute tumbling windows in `append` or `update` mode (in `complete` mode nothing is
evicted, so nothing is dropped either), a 10-minute delay, and a watermark that has been at 10:15
since the previous batch, so the late-row filter and eviction both use 10:15. Three late rows
arrive:

| Row | Event time | Its window | Window end vs watermark (10:15) | What happens |
|---|---|---|---|---|
| A | 10:18 | 10:10–10:20 | 10:20, ahead | counted — the window is still open |
| B | 10:12 | 10:10–10:20 | 10:20, ahead | counted, although 10:12 is behind the watermark: its window's state still exists |
| C | 10:05 | 10:00–10:10 | 10:10, passed | dropped — the window was finalised and its state deleted |

B is the row that catches people out. "Older than the watermark means dropped" is not what
happens in a window aggregation: what decides it is whether the state the row would update is
still there. Spark 3.5 filters a row at a stateful operator when that state is already behind
the watermark (for a window, when the window's end is), and counts it in the progress metric
`numRowsDroppedByWatermark`. C is dropped only once the filter has caught up: in the one batch
where the watermark first reaches 10:15, the filter still uses the previous value, so C gets in
and is emitted with its window as that batch evicts it. The documented contract is only the
guarantee above, so do not build on the exact edge.

The dropped rows are not kept anywhere, and a `foreachBatch` sink after the aggregation sees
only aggregated rows, never the ones the operator discarded. If they matter, keep the raw input
from before the stateful operator — land the stream in a raw table as well — and reconcile the
windows from it in a later batch job. Comparing each row's `event_time` with the watermark is
not a substitute: it would call B late. Classifying rows inside the stream means reproducing
the operator's own rule, the window's end against the previous batch's watermark.

### 12-4) What a watermark cleans up, and the output modes

**A stateful query keeps every open window, unmatched join row or seen dedup key in the state
store. Without a watermark nothing ever becomes old enough to delete, so state grows for the
life of the query. With one, Spark evicts state once the watermark passes it.**

```text
no watermark — every window ever opened stays in the state store
  [10:00–10:10] [10:10–10:20] [10:20–10:30] [10:30–10:40] [10:40–10:50] …

watermark at 10:15 — a window whose end is at or behind 10:15 is final; emit it, then evict it
  [10:00–10:10]  →  emitted (append) and removed
  [10:10–10:20] [10:20–10:30] [10:30–10:40] …  kept: a late row can still update them
```

Unbounded state shows up as a checkpoint that keeps growing, slower state reads and writes
every batch, batch duration creeping up over days, and finally an executor OOM — the reason
the RocksDB state store exists ([`faq_spark_hadoop.md` §11-4](../spark/faq_spark_hadoop.md#11-4-what-errors-happen-in-spark-streaming-and-how-do-you-fix-them)).
A watermark bounds the state of one stateful operator; it does not cap executor memory, and it
cannot help an operator that has no event-time column to judge by. `dropDuplicates` is the usual
case: its state is bounded only when the event-time column is part of the key, which dedupes
only retries that carry the same timestamp. To dedupe on an ID alone with bounded state, use
Spark 3.5's `dropDuplicatesWithinWatermark`.

The output mode decides when a window's row reaches the sink, and whether the watermark can
delete anything:

| Mode | Emits a window | State evicted by the watermark? | Use when |
|---|---|---|---|
| `append` | once, when the watermark passes its end — so results lag by the window plus the delay | yes | the sink cannot take updates: files, a Delta append, a daily report |
| `update` | every batch in which its count changed, including a late row's correction | yes | a dashboard or an upsert sink (`foreachBatch` + `MERGE`) |
| `complete` | every window, every batch | **no** — the whole result has to be kept | small result tables only; state grows without bound |

An aggregation in `append` mode needs a watermark — Spark rejects the query without one,
because it could never prove a window is final. In `update` mode a late row inside the delay
changes `10:00–10:10 = 100` to `101` and the sink receives the new row; in `append` mode the
sink would have received `101` once, but later.

### 12-5) Stream–stream joins and multiple watermarks

**A stream–stream join keeps both sides in state, waiting for a match. Spark can only delete a
buffered row once it knows no future row on the other side can join it — which takes a
watermark on each side and an event-time range in the join condition.**

```python
# python — a payment must arrive within an hour of its order
orders   = orders_stream.withWatermark("order_time", "10 minutes")
payments = payments_stream.withWatermark("payment_time", "10 minutes")

paid = orders.join(
    payments,
    F.expr("""
        order_id = payment_order_id
        AND payment_time >= order_time
        AND payment_time <= order_time + INTERVAL 1 HOUR
    """),
    "inner",
)
```

- **Inner join** — the watermarks and the time range are optional, but without them an order
  that never gets a payment stays in state forever.
- **Outer join** — a left outer join needs a watermark on the **right** side and the time
  range (the other way round for a right outer join); a watermark on the left is optional, and
  only needed to clean up the left side's state too. A left row with no match is only emitted, with
  `NULL`s, once the watermark proves no match can come, so it arrives late by the time range
  plus the delay.
- A watermark on each side with no time range in the condition does not bound the state: Spark
  still cannot tell how long an order may wait for its payment.

When a query reads more than one stream (a join or a `union`), each input has its own
watermark, and the query uses one global watermark chosen by
`spark.sql.streaming.multipleWatermarkPolicy`:

| Input | Max event time | Delay | Its watermark |
|---|---|---|---|
| orders | 10:30 | 10 min | 10:20 |
| payments | 10:20 | 10 min | 10:10 |
| global, `min` (default) | | | **10:10** |
| global, `max` | | | 10:20 |

| Policy | Gains | Costs |
|---|---|---|
| `min` (default) | the slower stream's late rows are still kept | state lives longer, set by the slowest input |
| `max` | state evicted sooner, smaller state store | rows from the slower stream can be dropped |

Keep `min` unless losing the slow stream's late data is acceptable. The policy is recorded in
the checkpoint, so a restarted query keeps the one it started with.

### 12-6) Watermarks in production: choosing the delay and watching it

**Choose the delay from the measured lateness distribution and what the business can lose,
not from a round number; then watch the watermark and the state size every batch, because a
stuck watermark and growing state are the same incident seen from two ends.**

A design prompt, and the reasoning an interviewer wants: *success rate per payment method
every 5 minutes; Kafka adds ~30 s; 99% of events arrive within 3 minutes; some mobile clients
sync up to 15 minutes late; state must stay bounded.*

```python
# python
success_rate = (payments.withWatermark("event_time", "15 minutes")
                        .groupBy(F.window("event_time", "5 minutes"), "payment_method")
                        .agg(F.count("*").alias("total"),
                             F.sum(F.when(F.col("status") == "ok", 1).otherwise(0)).alias("ok"))
                        .withColumn("success_rate", F.col("ok") / F.col("total")))
```

| Delay | Effect |
|---|---|
| too short (3 min) | the ~1% of events more than 3 minutes late, many of them from the late-syncing mobile clients, are at risk of being dropped (a row whose window is still open is still counted), which biases the success rate against mobile |
| 15 min | keeps the known late tail; each window is final, in `append`, ~20 minutes after it opens |
| too long (6 h) | results wait hours in `append`, and up to 6 h of windows stay in state |

Then the questions that settle it: is 15 minutes an observed p99.9 or a guess? Do some devices
sync hours later? Does the business accept an approximate number now and a corrected one later
— `update` mode, or a batch job that recomputes yesterday from the lake? Where do rows behind
the watermark go?

**Watch it** — every `StreamingQueryProgress` carries the watermark and the state operators:

```python
# python
p = query.lastProgress
p["eventTime"]["watermark"]                    # e.g. "2026-11-08T10:15:00.000Z"
p["stateOperators"][0]["numRowsTotal"]         # rows held in state
p["stateOperators"][0]["numRowsRemoved"]       # evicted this batch
p["stateOperators"][0]["numRowsDroppedByWatermark"]  # late input discarded
p["stateOperators"][0]["memoryUsedBytes"]
p["durationMs"]["triggerExecution"]            # batch duration
```

| Symptom | Likely cause | What to check |
|---|---|---|
| State rows and batch duration rise for days | no watermark, `complete` mode, or a join with no time range | the plan and the output mode |
| Watermark stuck, nothing emitted in `append` | no new data at all, so no newer max event time | input rate per source; whether this is quiet hours |
| Watermark leaps ahead, `numRowsDroppedByWatermark` spikes | a few rows with **future** timestamps (a bad device clock) set the max | the `eventTime.max` field; filter or clamp `event_time > current_timestamp()` before `withWatermark`. That stops the next jump but cannot undo this one — the advanced watermark is in the checkpoint — so recover with a new checkpoint (or a replay of the source) and reconcile the output written meanwhile |
| Results missing late events | delay shorter than the real lateness | the event-time vs read-time gap distribution |

**One Spark-specific trap.** Spark's watermark comes from the maximum across all partitions,
so an idle Kafka partition does not hold it back. Flink takes the minimum across source
instances, where an idle partition does freeze the watermark
([`stream_pipeline.md` §3-3](./stream_pipeline.md#3-3-idle-sources-and-watermark-alignment)).
The Spark failure runs the other way: one partition far ahead — a fast producer, a backfill,
a skewed clock — advances the watermark for everyone, and the slower partitions' rows turn
up late and get dropped.

---

## 13) Tuning Checklist ⭐⭐⭐⭐⭐

### 13-1) In the order you would check

**Find the slow stage first, then ask whether it reads too much, shuffles too much, or has
one task doing all the work — tune configs last.**

1. **Read less** — partition pruning on the filter column, column pruning (no `SELECT *`),
   pushdown not blocked by a UDF, columnar formats.
2. **Shuffle less** — broadcast small dimensions, drop needless `repartition` / `distinct`,
   aggregate before joining.
3. **Fix skew** — AQE skew join, isolate hot keys, salt.
4. **Right-size partitions** — ~100–200 MB each, enough tasks for every core; AQE on.
5. **Avoid Python in the hot path** — built-ins over UDFs, `pandas_udf` over `udf`.
6. **Cache what is reused**, unpersist after.
7. **Fix file layout** — compact small files, sensible `partitionBy`, bucketing / clustering
   for repeated joins.
8. **Then size resources** — executor cores / memory / overhead, dynamic allocation.

### 13-2) The configs worth knowing by name

| Config | Default (3.5) | What it controls |
|---|---|---|
| `spark.sql.shuffle.partitions` | 200 | partitions after a DataFrame shuffle |
| `spark.sql.adaptive.enabled` | true | AQE |
| `spark.sql.autoBroadcastJoinThreshold` | 10 MB | max size to broadcast a join side (`-1` disables) |
| `spark.sql.files.maxPartitionBytes` | 128 MB | input split size for file reads |
| `spark.sql.sources.partitionOverwriteMode` | static | `dynamic` overwrites only the written partitions |
| `spark.executor.memory` / `.cores` | 1g / depends on the cluster manager | executor heap and task slots |
| `spark.executor.memoryOverhead` | max(384 MB, 10%) | off-heap headroom (Python, NIO, metaspace) |
| `spark.memory.fraction` | 0.6 | unified execution + storage share of the heap |
| `spark.dynamicAllocation.enabled` | false | scale executors with demand |
| `spark.speculation` | false | re-launch slow tasks |
| `spark.serializer` | Java serialiser | set to Kryo for RDD workloads |

---

## 14) Debugging with the Spark UI ⭐⭐⭐⭐

### 14-1) Where to look

**Jobs tab → the slow job; Stages tab → the slow stage and its task-time distribution; SQL tab
→ the physical plan with real row counts; Executors tab → GC time, spills and failures.**

| Tab | Tells you |
|---|---|
| **Jobs** | which action is slow, how many stages it had |
| **Stages** | task duration min / median / max, shuffle read / write, spill (memory and disk), input size |
| **SQL / DataFrame** | the executed plan with rows output per operator, broadcast vs sort-merge, AQE's final plan |
| **Storage** | what is cached, how much is in memory vs on disk |
| **Executors** | GC time, failed tasks, shuffle totals, lost executors |
| **Environment** | the configs actually in effect |

The Spark History Server shows the same UI for finished applications.

### 14-2) Reading the symptoms

| You see | It usually means |
|---|---|
| max task time ≫ median, one task's shuffle read ≫ others | skew |
| large "Spill (Disk)" on many tasks | partitions too big for execution memory |
| high GC time (> ~10% of task time) | too much data per executor JVM, or caching deserialised objects |
| a stage with one task | `coalesce(1)`, a window with no `partitionBy`, a non-splittable file, a global sort result |
| a stage re-running "FetchFailed" | lost executor / shuffle files — memory kill, preemption, a bad node |
| many very short tasks | too many partitions, or many tiny files |
| long gap before the first task | driver-side work: file listing, schema inference, planning a huge plan |

---

## 15) Common Failures and Fixes ⭐⭐⭐⭐⭐

### 15-1) Out of memory

| Error | Where | Usual cause | Fix |
|---|---|---|---|
| `java.lang.OutOfMemoryError` on the driver | driver | `collect()` / `toPandas()`, an oversized broadcast, a plan over millions of files | write out instead; `limit`; lower the broadcast threshold; compact files |
| `OutOfMemoryError` / heap space on an executor | executor | a skewed partition, a shuffle hash join build side, a huge row (`collect_list`) | fix skew; more shuffle partitions; sort-merge instead |
| "Container killed by YARN for exceeding memory limits" | container | off-heap use: Python workers, NIO buffers | raise `spark.executor.memoryOverhead` |
| `ExecutorLostFailure` / `FetchFailedException` | shuffle | an executor holding shuffle files died | find why it died (usually one of the above) |

### 15-2) Other errors worth recognising

- **`Task not serializable`** — a closure captured a non-serialisable object; build it inside
  `mapPartitions` / `foreachPartition`, or mark it as a broadcast.
- **`AnalysisException: cannot resolve column`** — typo, a column dropped upstream, or an
  ambiguous column after a self-join (alias both sides).
- **Job "hangs" at 199/200 tasks** — the last task is the skewed one.
- **Timeouts on broadcast** (`spark.sql.broadcastTimeout`, 300s) — the "small" side is not
  small, or is slow to compute; materialise it or stop broadcasting it.
- **Different results on re-run** — non-deterministic code (`rand()` without a seed,
  `monotonically_increasing_id`, `first()` without an order) recomputed after a failure.
- **Out of disk** — shuffle and spill files fill local disks; give executors more local
  storage or shuffle less.

### 15-3) Exactly-once is your job in batch too

**A Spark batch job is retried at task, stage and application level, so its writes must be
idempotent — re-running must replace, not add.**

- Writes: dynamic partition overwrite or `MERGE` keyed on a natural key, not `append`.
- Side effects in tasks (HTTP calls, emails, writing to an external DB in `foreach`) can run
  more than once on retry or speculation — make them idempotent or move them out of the job.
- Output committers: on object stores, use the S3A committers or a table format rather than
  rename-based commits, which are not atomic on S3.

---

## 16) Frequently Asked Questions ⭐⭐⭐⭐⭐

### 16-1) Quick-fire answers

- **What triggers a stage boundary?** A wide dependency — a shuffle.
- **How many tasks does a stage have?** One per partition of that stage.
- **`cache()` vs `persist()`?** `cache()` is `persist()` at the default level
  (`MEMORY_AND_DISK` for DataFrames, `MEMORY_ONLY` for RDDs).
- **`repartition` vs `coalesce`?** Full shuffle, any count vs merge without a shuffle, only fewer.
- **`groupByKey` vs `reduceByKey`?** `reduceByKey` combines on the map side first; prefer it.
- **Why is my job slow after one stage finished fast?** Usually skew or a shuffle that is too
  large for its partition count.
- **What does AQE do?** Re-plans at shuffle boundaries: coalesces partitions, switches to
  broadcast joins, splits skewed partitions.
- **What is predicate pushdown?** Moving a filter into the scan, so the source skips data.
- **Broadcast join limit?** `spark.sql.autoBroadcastJoinThreshold`, 10 MB by default; the
  small side must fit in driver and executor memory.
- **How does Spark recover a lost partition?** Recomputes it from lineage.
- **Narrow vs wide?** Narrow: one parent partition per child partition. Wide: many, so a shuffle.
- **Serialisation vs deserialisation?** Object → bytes to ship or store; bytes → object to
  compute on. Spark does both on every shuffle, broadcast, serialised cache and task launch.
- **Does Kryo speed up a DataFrame shuffle?** No. SQL exchanges serialise `UnsafeRow`s with their
  own serialiser; Kryo helps RDD records and broadcasts.
- **What causes `Task not serializable`?** A closure captured something that cannot be
  serialised. Create it on the executor, inside `foreachPartition`.
- **Why avoid Python UDFs?** Row-by-row serialisation to a Python process, and the optimiser
  cannot see inside them.
- **`client` vs `cluster` deploy mode?** Where the driver runs: your machine vs inside the cluster.
- **What is a DAG in Spark?** The graph of RDD / DataFrame dependencies the scheduler cuts into stages.
- **Accumulators — any caveat?** Updates in transformations can be counted more than once on retry.
- **Static vs dynamic partition overwrite?** Static replaces the whole table; dynamic only the
  partitions present in the written data.
- **What is a shuffle spill?** Execution memory filled up, so the operator wrote sorted runs to
  local disk and merged them later.
- **Window function with no `partitionBy`?** All rows move to one partition — one task does everything.
- **How do you pick the number of partitions?** Total data ÷ ~128 MB, at least 2–3× the cores,
  then let AQE coalesce.
- **What is a watermark?** Max event time seen minus the allowed delay. Windows and join rows
  behind it are final, and their state is deleted.
- **Does a 10-minute watermark drop everything older than 10 minutes?** No. Data within the
  delay is never dropped; data later than it *may* be — a row whose window is still open is
  counted.
- **Watermark in `complete` mode?** It cleans nothing: the whole result is kept.
- **Two input streams, which watermark?** The minimum of the two by default
  (`spark.sql.streaming.multipleWatermarkPolicy`).
- **Spark on Kubernetes vs YARN?** Same engine; K8s runs each executor as a pod, has no
  external shuffle service by default, so dynamic allocation uses shuffle tracking.

### 16-2) Design question: "Process 10 TB of daily clickstream"

**State the shape of the answer first: read only the day's partition, clean and dedupe,
join small dimensions by broadcast, aggregate, and write partitioned, compacted output with a
re-runnable overwrite.**

1. **Input** — clicks land in `s3://lake/raw/clicks/dt=YYYY-MM-DD/` as Parquet (or convert
   JSON to Parquet in a first step with an explicit schema).
2. **Read** — filter on `dt = run_date`, select only the needed columns.
3. **Clean** — parse, drop malformed rows to a quarantine path, dedupe on `event_id`
   (`dropDuplicates(["event_id"])` within the day).
4. **Enrich** — broadcast-join small dimensions (pages, campaigns); sort-merge join users if large.
5. **Aggregate** — sessions and daily metrics with `groupBy` and window functions partitioned by `user_id`.
6. **Write** — Delta / Iceberg (or Parquet with dynamic partition overwrite), partitioned by
   `dt`, `repartition` by `dt` (plus a clustering key) to control file sizes.
7. **Operate** — AQE on, shuffle partitions sized for ~10 TB (≈ several thousand),
   data-quality checks before publishing, orchestrated with a logical date for backfills.
