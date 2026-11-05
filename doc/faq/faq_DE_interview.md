# Data Engineering Interview FAQ

> **Scope** — the core knowledge a data engineer interview probes, one section per topic: Spark, Kafka, the Hadoop ecosystem, data modeling, analytical SQL, batch and stream pipelines, schema design, and SQL vs NoSQL — each answered the way you would say it out loud, then the mechanism, then the architecture.
> **See also**: [`spark/faq_spark_hadoop.md`](./spark/faq_spark_hadoop.md), [`kafka/faq_kafka.md`](./kafka/faq_kafka.md), [`flink/faq_flink.md`](./flink/faq_flink.md), [`sql/faq_sql.md`](./sql/faq_sql.md), [`faq_data_model.md`](./faq_data_model.md) and [`db/faq_DB_DW.md`](./db/faq_DB_DW.md) — the longer notes on each tool; this file is the interview map across them.

Each section leads with the one-sentence answer, then the mechanism, then the trade-off an
interviewer pushes on next. The stars mark how often the topic comes up in a DE loop.

---

## 1) Apache Spark ⭐⭐⭐⭐⭐

### 1-1) Architecture: driver, cluster manager, executors

**Spark is a distributed compute engine: one driver plans the job, many executors run it in
parallel, and a cluster manager hands out the machines.**

| Component | Role |
|---|---|
| **Driver** | Runs your `main()`, holds the `SparkSession`, builds the DAG, schedules tasks, collects results |
| **Cluster manager** | Allocates resources — YARN, Kubernetes, Standalone (Mesos is deprecated) |
| **Executor** | A JVM on a worker node; runs tasks, caches data, writes shuffle files |
| **Task** | The unit of work: one task processes **one partition** |

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

- **Deploy modes**: `client` (driver on the submitting machine — good for notebooks) vs
  `cluster` (driver inside the cluster — what production jobs use).
- Parallelism = number of partitions; throughput = cores × executors. A 200-partition stage
  on 50 cores runs in 4 waves.

### 1-2) Lazy evaluation, the DAG, stages and shuffles

**Transformations are lazy and only build a plan; an action triggers execution, and the
plan is cut into stages at every shuffle.**

| | Transformation | Action |
|---|---|---|
| Examples | `select`, `filter`, `map`, `join`, `groupBy` | `count`, `collect`, `show`, `write`, `take` |
| Returns | a new DataFrame / RDD | a value to the driver, or data to storage |
| Runs? | no — adds to the plan | yes — submits a **job** |

- **Narrow dependency** — each output partition depends on one input partition
  (`map`, `filter`, `union`). No data moves; Spark pipelines them into one stage.
- **Wide dependency** — an output partition needs data from many inputs (`groupBy`,
  `join`, `distinct`, `repartition`). This is a **shuffle**: write to local disk, transfer
  over the network, read back. It ends a stage.

```text
Job  = one action
Stage = the work between two shuffles
Task  = one partition of one stage

read ─ filter ─ map │ shuffle │ groupBy-agg ─ write
└──── Stage 0 ─────┘         └──── Stage 1 ──────┘
```

Why lazy evaluation matters: Catalyst sees the whole plan before running it, so it can push
filters down to the scan, prune columns, and pick a join strategy.

### 1-3) RDD vs DataFrame vs Dataset, Catalyst and Tungsten

**Use DataFrames: they carry a schema, so Catalyst can optimise them and Tungsten can store
them compactly; RDDs are opaque functions Spark cannot see into.**

| | RDD | DataFrame | Dataset |
|---|---|---|---|
| Schema | none | yes (rows of columns) | yes, typed (`Dataset[T]`) |
| Optimiser | no | Catalyst | Catalyst |
| Languages | all | all | Scala / Java only |
| Use when | custom low-level control, unstructured data | almost always | compile-time types in Scala |

- **Catalyst** — the query optimiser: parsed plan → analysed → optimised logical plan
  (predicate pushdown, constant folding, column pruning) → physical plans, choose one by cost.
- **Tungsten** — the execution engine: off-heap binary row format, whole-stage code
  generation (fuses operators into one generated Java function).
- A Python UDF breaks both: rows are serialised out to a Python worker and back. Prefer
  built-in functions; if you need a UDF, use a vectorised `pandas_udf` (Arrow batches).

```python
# python
from pyspark.sql import functions as F

df = spark.read.parquet("s3://bucket/events/")
daily = (df.filter(F.col("event_date") >= "2026-01-01")     # pushed down to the scan
           .groupBy("event_date", "country")                 # wide -> shuffle
           .agg(F.countDistinct("user_id").alias("dau")))
daily.explain(mode="formatted")                              # read the physical plan
```

### 1-4) Partitioning: `repartition` vs `coalesce`, and the small-files problem

**`repartition(n)` does a full shuffle and can go up or down; `coalesce(n)` only merges
existing partitions without a shuffle, so it can only go down.**

| | `repartition(n)` / `repartition(col)` | `coalesce(n)` |
|---|---|---|
| Shuffle | yes | no |
| Result | evenly sized partitions | possibly uneven |
| Use for | increase parallelism, co-locate by key before a write | reduce output files at the end |

- `spark.sql.shuffle.partitions` (default **200**) sets the partition count after a shuffle —
  too low on a TB job gives huge spilling tasks, too high on a small job gives tiny tasks.
  **AQE** (`spark.sql.adaptive.enabled`, on by default since 3.2) coalesces them at runtime.
- **Rule of thumb**: aim for ~100–200 MB per partition, and 2–4 tasks per core.
- **Small files** — writing 10,000 tiny files slows every later reader (one task per file,
  metadata overhead on HDFS / S3 listing). Fix with `coalesce` / `repartition` before the
  write, `maxRecordsPerFile`, or a compaction job (Delta `OPTIMIZE`, Iceberg `rewrite_data_files`).
- `partitionBy("dt")` on write is a **directory layout** for pruning, not the same thing as
  in-memory partitions — and partitioning by a high-cardinality column explodes file counts.

### 1-5) Joins and data skew ⭐⭐⭐⭐⭐

**Spark picks broadcast hash join when one side is small, sort-merge join otherwise; skew
means a few keys hold most of the rows, so one task runs for an hour while the rest finish
in a minute.**

| Strategy | When | Shuffle |
|---|---|---|
| **Broadcast hash join** | one side < `spark.sql.autoBroadcastJoinThreshold` (10 MB default) or hinted | no — small side copied to every executor |
| **Sort-merge join** | both sides large, equi-join (the default) | both sides |
| **Shuffle hash join** | one side much smaller but too big to broadcast | both sides |
| **Broadcast nested loop** | non-equi join | — and slow; avoid |

How to spot skew: in the Spark UI, one stage's max task time is 10× the median, and its
shuffle-read size is far larger than the others.

How to fix it, in the order you would try:

1. **AQE skew join** — `spark.sql.adaptive.skewJoin.enabled=true` splits oversized partitions automatically.
2. **Broadcast** the small side, so the big side never shuffles.
3. **Filter or handle the hot key separately** — often it is `NULL` or a test user; join the rest normally and `union`.
4. **Salting** — append a random suffix to the skewed key on the big side, replicate the small side once per suffix.

```python
# python — salting a skewed join
SALT = 16
big   = big.withColumn("salt", (F.rand() * SALT).cast("int"))
small = small.crossJoin(spark.range(SALT).withColumnRenamed("id", "salt"))
joined = big.join(small, ["key", "salt"]).drop("salt")
```

### 1-6) Memory, caching and the common failures

**Executor memory is split into execution (shuffles, joins, sorts) and storage (cache)
which borrow from each other; most OOMs are a `collect()` on the driver or one skewed
partition on an executor.**

- `cache()` = `persist(MEMORY_AND_DISK)` for DataFrames. Cache only what is **reused** by
  more than one action, and `unpersist()` afterwards. Caching is itself lazy.
- **Driver OOM**: `collect()` / `toPandas()` on a big result, or broadcasting something too
  large. Write to storage instead, or `take(n)`.
- **Executor OOM / spill**: skewed partition, too few shuffle partitions, too-large
  broadcast. Raise `spark.sql.shuffle.partitions`, fix skew, increase `executor.memoryOverhead`
  (Python workers and off-heap live there).
- **Checkpointing** truncates a long lineage (iterative jobs) by writing to reliable storage.
- Fault tolerance comes from **lineage**: a lost partition is recomputed from its parents,
  not restored from a replica.

---

## 2) Apache Kafka ⭐⭐⭐⭐⭐

### 2-1) Architecture: brokers, topics, partitions, replicas

**Kafka is a distributed, replicated, append-only commit log: producers append to
partitions, consumers read them at their own offset, and the data stays for the retention
period whether or not anyone read it.**

| Term | Meaning |
|---|---|
| **Broker** | One Kafka server; a cluster is several |
| **Topic** | A named stream of records |
| **Partition** | An ordered, immutable log; the unit of parallelism **and** of ordering |
| **Offset** | A record's position inside its partition |
| **Replica** | A copy of a partition on another broker; one is **leader**, the rest **followers** |
| **ISR** | In-sync replicas — followers caught up with the leader |
| **Controller** | Manages leadership and metadata — **KRaft** (Raft quorum inside Kafka) since 3.x; ZooKeeper is removed in 4.0 |

```text
Topic "orders", 3 partitions, replication factor 3

          Broker 1          Broker 2          Broker 3
P0     [Leader]          [Follower]        [Follower]
P1     [Follower]        [Leader]          [Follower]
P2     [Follower]        [Follower]        [Leader]

P0 log:  | 0 | 1 | 2 | 3 | 4 | 5 | ...  → append only
                         ▲ consumer group A at offset 3
```

Why it is fast: sequential disk writes, the OS page cache, zero-copy `sendfile` to
consumers, and batching + compression end to end.

### 2-2) Producers: keys, `acks`, idempotence

**The key picks the partition, so the same key always lands in the same partition and stays
in order; `acks=all` with `min.insync.replicas=2` is what makes a write durable.**

| `acks` | Wait for | Risk |
|---|---|---|
| `0` | nothing | lost on any failure |
| `1` | leader only | lost if the leader dies before followers copy it |
| `all` (`-1`) | every ISR member | none, given `min.insync.replicas ≥ 2` |

- Partition = `hash(key) % numPartitions`; no key → sticky partitioning across batches.
  **Adding partitions later remaps keys** and breaks per-key ordering.
- `enable.idempotence=true` (the default since 3.0) gives each producer an id and per-partition
  sequence numbers, so a retry cannot write a duplicate.
- Throughput levers: `batch.size`, `linger.ms`, `compression.type` (`lz4` / `zstd`).

### 2-3) Consumer groups, offsets and rebalancing

**Each partition is read by exactly one consumer in a group, so the partition count caps the
group's parallelism; the consumer commits its offset to know where to resume.**

```text
Topic with 4 partitions

Group "billing" (2 consumers)   Group "analytics" (4 consumers)
  C1 ← P0, P1                      C1 ← P0   C2 ← P1
  C2 ← P2, P3                      C3 ← P2   C4 ← P3
(Each group gets every message; a 5th consumer in "analytics" sits idle.)
```

- Offsets are stored in the internal `__consumer_offsets` topic.
- **Commit after processing** → at-least-once (a crash replays). **Commit before** →
  at-most-once (a crash loses). Auto-commit is a timer, so it can be either.
- **Rebalance** — when a consumer joins, leaves or misses `max.poll.interval.ms`, partitions
  are reassigned. The eager protocol stops the whole group; cooperative-sticky assignment
  (and the KIP-848 protocol in 4.0) moves only what must move.
- **Consumer lag** = latest offset − committed offset; the first metric to alert on.

### 2-4) Delivery semantics and exactly-once ⭐⭐⭐⭐⭐

**At-least-once is the default you design for; exactly-once inside Kafka needs idempotent
producers plus transactions, and exactly-once to an external system needs an idempotent or
transactional sink.**

| Semantics | How | Failure result |
|---|---|---|
| At-most-once | commit, then process | message lost |
| At-least-once | process, then commit | message duplicated |
| Exactly-once | idempotent producer + transactions + `isolation.level=read_committed` | neither |

- Kafka transactions make "read from A → write to B → commit offset" atomic. That is what
  Kafka Streams' `processing.guarantee=exactly_once_v2` and Flink's Kafka sink use.
- Writing to a database: make the write **idempotent** — upsert on a natural key, or store
  the offset in the same DB transaction as the data.

### 2-5) Retention, log compaction and ordering

**Delete retention drops old segments by time or size; compaction keeps only the latest
value per key — a changelog you can rebuild a table from.**

- `cleanup.policy=delete` — `retention.ms` (default 7 days) / `retention.bytes`.
- `cleanup.policy=compact` — keeps the last record per key; a `null` value (tombstone) deletes
  the key. Used for CDC tables, `__consumer_offsets`, Kafka Streams state changelogs.
- **Ordering** is guaranteed only **within a partition**. Need global order → one partition
  (no parallelism). Need per-entity order → key by the entity id.
- With retries, `max.in.flight.requests.per.connection ≤ 5` plus idempotence keeps order.

### 2-6) Kafka vs a traditional message queue

**A queue deletes a message once it is consumed; Kafka keeps the log, so many independent
consumers can read it and any of them can replay it.**

| | Kafka | RabbitMQ / SQS |
|---|---|---|
| Model | partitioned log, pull | queue, push (Rabbit) / pull (SQS) |
| After consumption | kept until retention | deleted |
| Replay | yes — reset the offset | no |
| Ordering | per partition | per queue (FIFO variants) |
| Strength | high-throughput event streams, fan-out, CDC | task queues, per-message routing, low volume |

---

## 3) The Hadoop Ecosystem ⭐⭐⭐

### 3-1) HDFS: NameNode, DataNodes, blocks

**HDFS splits files into large blocks (128 MB by default), stores three replicas of each on
different DataNodes, and keeps all the metadata in one NameNode's memory.**

| Component | Holds |
|---|---|
| **NameNode** | namespace and block → DataNode map, in memory; the edit log and fsimage on disk |
| **DataNode** | the blocks themselves; sends heartbeats and block reports |
| **Standby NameNode** | HA failover, kept in sync through JournalNodes |

- **Rack awareness**: replica 1 local, replicas 2 and 3 on another rack — survives a rack failure.
- Write-once, read-many: append is allowed, random update is not.
- **Small files hurt** because every file costs NameNode memory (~150 bytes per object), not
  because of disk space.
- **Data locality**: schedule the computation on the node that holds the block, ship code not data.

### 3-2) MapReduce: map, shuffle, reduce

**Map turns each input split into key-value pairs, the shuffle groups them by key across
the network, and reduce aggregates each key — every step writes to disk, which is why Spark
replaced it.**

```text
input splits → MAP → (k, v) → combiner → partition by hash(k) → SORT/SHUFFLE → REDUCE → output
"a b a"        (a,1)(b,1)(a,1)   (a,2)(b,1)                     a:[2,3] b:[1]    a=5 b=1
```

- **Combiner** = a map-side reduce; cuts shuffle volume (valid only for associative ops like sum).
- **Spark vs MapReduce**: Spark keeps intermediate data in memory across stages and builds a
  whole DAG; MapReduce writes to HDFS between every job. Spark is typically 10–100× faster on
  iterative work.

### 3-3) YARN: the resource manager

**YARN separates resource management from computation, so MapReduce, Spark, Flink and Hive
can share one cluster.**

| Component | Role |
|---|---|
| **ResourceManager** | cluster-wide scheduler (Capacity / Fair scheduler, queues) |
| **NodeManager** | per node; launches and monitors containers |
| **ApplicationMaster** | one per application; negotiates containers and tracks its tasks |
| **Container** | a slice of CPU + memory on one node |

### 3-4) The ecosystem map, and what replaced it

**Know what each tool did and what a modern stack uses instead — interviewers ask both.**

| Tool | What it is | Modern equivalent |
|---|---|---|
| **Hive** | SQL on HDFS, compiled to MR / Tez / Spark; the **Hive Metastore** | Spark SQL, Trino, cloud warehouses; the Metastore lives on (or Glue / Unity Catalog) |
| **HBase** | wide-column, random read/write on HDFS (Bigtable model) | Bigtable, Cassandra, DynamoDB |
| **Pig** | dataflow scripting on MR | Spark |
| **Sqoop** | bulk RDBMS ↔ HDFS copy | Spark JDBC, Debezium CDC, managed connectors |
| **Flume** | log collection into HDFS | Kafka + Kafka Connect |
| **Oozie** | workflow scheduler | Airflow, Dagster |
| **ZooKeeper** | coordination, leader election | still used; Kafka moved to KRaft |
| **Impala / Presto** | MPP SQL on HDFS | Trino, Starburst |
| **HDFS** | distributed storage | object storage (S3, GCS, ADLS) — compute and storage separated |

The big shift: **storage and compute are now separate** (object store + elastic compute),
and open table formats (Iceberg, Delta, Hudi) add the ACID tables HDFS directories never had.

---

## 4) Data Modeling ⭐⭐⭐⭐⭐

### 4-1) OLTP vs OLAP, normalised vs denormalised

**OLTP models are normalised so each fact is written once and updates stay consistent; OLAP
models are denormalised so reads scan few tables and few joins.**

| | OLTP | OLAP |
|---|---|---|
| Workload | many small reads/writes by key | few large scans and aggregations |
| Model | 3NF, normalised | star schema, denormalised |
| Storage | row-oriented | column-oriented |
| Examples | MySQL, PostgreSQL | BigQuery, Snowflake, Redshift, ClickHouse |
| Optimise for | write correctness, concurrency | read throughput |

Normal forms in one line each:

- **1NF** — atomic values, no repeating groups.
- **2NF** — 1NF, and no non-key column depends on part of a composite key.
- **3NF** — 2NF, and no non-key column depends on another non-key column (no transitive dependency).

### 4-2) Dimensional modeling: facts, dimensions, grain ⭐⭐⭐⭐⭐

**Declare the grain first — what one fact row means — then the dimensions that describe it
and the measures it holds.** Kimball's four steps: pick the business process → declare the
grain → identify the dimensions → identify the facts.

- **Fact table** — measurements of a business event (`amount`, `quantity`), foreign keys to
  dimensions, very tall and narrow.
- **Dimension table** — descriptive context (who, what, where, when), wide and short.
- **Star schema** — the fact joins directly to denormalised dimensions. Faster, simpler; the default.
- **Snowflake schema** — dimensions normalised into sub-dimensions. Less redundancy, more joins.

```text
                 dim_date
                    │
 dim_customer ── fact_order_line ── dim_product
                    │
                 dim_store

grain: one row per product per order
```

Measure additivity: **additive** (sales amount — sum across every dimension),
**semi-additive** (account balance — sum across accounts, not across time),
**non-additive** (ratios — store numerator and denominator instead).

### 4-3) Fact table types

| Type | One row per | Example |
|---|---|---|
| **Transaction** | event | one order line |
| **Periodic snapshot** | entity per period | daily account balance |
| **Accumulating snapshot** | process instance, updated as it moves | order: placed → paid → shipped → delivered dates |
| **Factless** | an event or coverage with no measure | student attended class |

### 4-4) Slowly changing dimensions ⭐⭐⭐⭐⭐

**SCD Type 2 keeps history by closing the old row and inserting a new one, so a fact joins
to the version of the dimension that was true when it happened.**

| Type | Behaviour | History |
|---|---|---|
| 0 | never change | original only |
| 1 | overwrite | none |
| 2 | new row + `valid_from` / `valid_to` / `is_current` | full |
| 3 | add a `previous_value` column | one step |
| 4 | current table + separate history table | full, split |
| 6 | 1 + 2 + 3 combined | full + current value on every row |

```sql
-- SCD2: close the changed rows, then insert the new versions
UPDATE dim_customer d
SET    valid_to = CURRENT_DATE - 1, is_current = FALSE
FROM   stg_customer s
WHERE  d.customer_id = s.customer_id
  AND  d.is_current
  AND  d.address <> s.address;

INSERT INTO dim_customer (customer_id, address, valid_from, valid_to, is_current)
SELECT s.customer_id, s.address, CURRENT_DATE, DATE '9999-12-31', TRUE
FROM   stg_customer s
LEFT JOIN dim_customer d
       ON d.customer_id = s.customer_id AND d.is_current
WHERE  d.customer_id IS NULL;      -- new customers and the ones just closed
```

The fact table stores the dimension's **surrogate key** (`customer_sk`), not `customer_id`,
which is what pins each fact to one version.

### 4-5) Kimball vs Inmon vs Data Vault vs One Big Table

| Approach | Idea | Strength | Weakness |
|---|---|---|---|
| **Kimball** | bottom-up: conformed star schemas per process | fast to deliver, business-friendly | conformance needs discipline |
| **Inmon** | top-down: a 3NF enterprise warehouse, marts built from it | one integrated source of truth | slow to build |
| **Data Vault** | hubs (keys), links (relationships), satellites (attributes over time) | auditable, absorbs source change | many joins; needs a mart layer on top |
| **One Big Table** | fully flattened, wide table | fastest to query on columnar engines | duplicated data, hard to change |

A common modern answer: Data Vault or staging in the raw/integration layer, Kimball stars
in the marts, and a wide table for one hot dashboard.

---

## 5) SQL for Data ⭐⭐⭐⭐⭐

### 5-1) Logical execution order

**SQL runs `FROM` first and `SELECT` almost last — which is why you cannot filter on a
window function in `WHERE` or use a `SELECT` alias in `WHERE`.**

```text
1 FROM / JOIN  →  2 WHERE  →  3 GROUP BY  →  4 HAVING
→ 5 window functions  →  6 SELECT  →  7 DISTINCT  →  8 ORDER BY  →  9 LIMIT
```

- `WHERE` filters rows before grouping; `HAVING` filters groups after it.
- To filter on a window result, wrap it in a CTE / subquery (or use `QUALIFY` in
  Snowflake, BigQuery, Databricks).

### 5-2) Window functions ⭐⭐⭐⭐⭐

**A window function computes over a set of related rows without collapsing them — `GROUP BY`
returns one row per group, a window keeps every row.**

| Function | Use |
|---|---|
| `ROW_NUMBER()` | unique 1, 2, 3 — dedupe, top-1 per group |
| `RANK()` | ties share a rank, then skip (1, 1, 3) |
| `DENSE_RANK()` | ties share, no gap (1, 1, 2) |
| `LAG()` / `LEAD()` | previous / next row's value — day-over-day change |
| `SUM() OVER (... ROWS BETWEEN ...)` | running total, moving average |
| `FIRST_VALUE()` / `LAST_VALUE()` | first / last in the frame (mind the default frame) |
| `NTILE(n)` | bucket into n groups — percentiles |

```sql
SELECT user_id,
       order_date,
       amount,
       SUM(amount) OVER (PARTITION BY user_id ORDER BY order_date
                         ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS running_total,
       AVG(amount) OVER (PARTITION BY user_id ORDER BY order_date
                         ROWS BETWEEN 6 PRECEDING AND CURRENT ROW)         AS avg_7_orders,
       amount - LAG(amount) OVER (PARTITION BY user_id ORDER BY order_date) AS diff_prev
FROM   orders;
```

**Trap**: with `ORDER BY` and no frame, the default is `RANGE BETWEEN UNBOUNDED PRECEDING AND
CURRENT ROW` — rows with the same `order_date` are summed together, and `LAST_VALUE` returns
the current row, not the last one.

### 5-3) The patterns that get asked

**Top-N per group** and **dedupe** — rank inside the partition, keep rank 1:

```sql
-- latest record per user (dedupe a CDC / event table)
WITH ranked AS (
  SELECT *, ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY updated_at DESC) AS rn
  FROM   users_raw
)
SELECT * FROM ranked WHERE rn = 1;
```

**Gaps and islands** — consecutive login streaks: date minus row number is constant inside a streak.

```sql
WITH d AS (
  SELECT DISTINCT user_id, login_date FROM logins
), g AS (
  SELECT user_id, login_date,
         login_date - CAST(ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY login_date) AS INT) AS grp
  FROM   d
)
SELECT user_id, MIN(login_date) AS streak_start, COUNT(*) AS streak_days
FROM   g
GROUP  BY user_id, grp
HAVING COUNT(*) >= 3;
```

**Retention / funnel** — conditional aggregation:

```sql
SELECT signup_date,
       COUNT(DISTINCT u.user_id)                                                    AS signups,
       COUNT(DISTINCT CASE WHEN a.activity_date = u.signup_date + 1 THEN a.user_id END) AS d1_retained
FROM   users u
LEFT JOIN activity a ON a.user_id = u.user_id
GROUP  BY signup_date;
```

Also expect: median / percentile (`PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY x)`), pivot
with `SUM(CASE WHEN ...)`, sessionisation (`LAG` + a 30-minute gap flag + running `SUM`).

### 5-4) Join pitfalls: NULLs, fan-out, anti-joins

- **Fan-out** — joining a fact to a table with several rows per key multiplies the rows, so
  `SUM` double-counts. Aggregate first, then join; check the grain on both sides.
- **`NULL` keys never match** in an equi-join (`NULL = NULL` is unknown).
- **`NOT IN` with a `NULL`** in the subquery returns **no rows**. Use `NOT EXISTS` or
  `LEFT JOIN ... WHERE b.id IS NULL`.
- **A `WHERE` on the right table turns a `LEFT JOIN` into an inner join** — put the condition
  in `ON` instead.
- `COUNT(*)` counts rows; `COUNT(col)` skips `NULL`s; `COUNT(DISTINCT col)` is expensive at
  scale — `APPROX_COUNT_DISTINCT` (HyperLogLog) when ±2% is fine.

```sql
-- customers with no orders: anti-join, NULL-safe
SELECT c.*
FROM   customers c
WHERE  NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.customer_id);
```

### 5-5) Query performance on a warehouse

**On a columnar warehouse, cost is bytes scanned: select only the columns you need, filter on
the partition column, and let the engine skip files.**

- **Partition pruning** — filter directly on the partition column (`dt = '2026-11-01'`); wrapping
  it in a function (`DATE(ts) = ...`) can defeat pruning.
- **Clustering / sort keys** let the engine skip blocks using min/max statistics (zone maps).
- `SELECT *` on a columnar store reads every column — the most expensive habit.
- Read `EXPLAIN`: look for full scans, the join strategy, and the stage with the largest rows-out.
- Pre-aggregate hot queries into a summary table or materialised view.

---

## 6) Batch Data Pipelines ⭐⭐⭐⭐

### 6-1) ETL vs ELT, and the layered architecture

**ETL transforms before loading; ELT loads raw data into the warehouse first and transforms
there with SQL — the modern default, because warehouse compute is cheap and raw data lets you
reprocess.**

| | ETL | ELT |
|---|---|---|
| Transform in | a separate engine (Spark, Informatica) | the warehouse / lakehouse (SQL, dbt) |
| Raw data kept | often not | yes |
| Fits | heavy non-SQL logic, PII scrubbing before load | most analytics today |

```text
Sources ──► Ingest ──► Bronze / raw ──► Silver / cleaned ──► Gold / marts ──► BI, ML
(OLTP, API,  (batch,     as-is,          deduped, typed,      business-level
 files, CDC)  CDC)        append-only     conformed            aggregates, stars
                    └──────── orchestrated by Airflow / Dagster, tested by dbt / GE ───────┘
```

The medallion (bronze / silver / gold) layers are the same idea as staging → integration →
presentation: keep the raw copy, so a bug in a transform is fixed by re-running it.

### 6-2) Idempotency and backfills ⭐⭐⭐⭐⭐

**A pipeline is idempotent when running it twice for the same input gives the same result —
the property that makes retries and backfills safe.**

- **Overwrite a partition, do not append**: each run owns `dt = {{ ds }}` and replaces it
  (`INSERT OVERWRITE`, Spark dynamic partition overwrite, `MERGE`).
- **Parametrise by the logical date**, never `now()` — a backfill for March must read March.
- `MERGE` / upsert on a natural key for tables that are not partitioned by date.
- Make side effects (emails, API calls) separate tasks, keyed so they cannot fire twice.

```sql
-- idempotent daily load: rerunning 2026-11-01 replaces it rather than duplicating it
INSERT OVERWRITE TABLE fact_orders PARTITION (dt = '2026-11-01')
SELECT order_id, customer_sk, amount
FROM   stg_orders
WHERE  order_date = '2026-11-01';
```

### 6-3) Incremental loads and CDC

| Method | How | Catches deletes? | Cost |
|---|---|---|---|
| **Full reload** | copy the whole table | yes | high; fine for small tables |
| **Incremental by timestamp** | `WHERE updated_at > last_watermark` | no | needs a reliable `updated_at` |
| **CDC (log-based)** | read the DB's binlog / WAL (Debezium → Kafka) | yes | lowest load on the source, more infra |

- Watermark on `updated_at` with a small overlap window, then dedupe — clocks and long
  transactions commit rows "in the past".
- Hard deletes are invisible to timestamp queries; use CDC or soft-delete flags.

### 6-4) Orchestration

**The orchestrator owns dependencies, schedules, retries and backfills — not the
transformation logic.** Airflow is the usual answer: a DAG of tasks, scheduled by logical date.

- Keep tasks **atomic and idempotent**; pass data through storage, not through XCom.
- Use **sensors** (or dataset / asset-driven scheduling) to wait for upstream data instead
  of guessing a cron time.
- Retries with backoff, SLAs / alerts, `catchup` for backfills, pools to cap concurrency on a
  shared resource.
- Alternatives: Dagster (asset-centric), Prefect, cloud-native (Step Functions, Composer is
  managed Airflow). See [`faq_Airflow.md`](./faq_Airflow.md).

### 6-5) Data quality

**Test data like code: check it at each layer boundary and stop the pipeline before bad data
reaches a dashboard.**

| Check | Example |
|---|---|
| Schema | expected columns and types |
| Completeness | not-null on keys; row count within ±X% of last week |
| Uniqueness | primary key has no duplicates |
| Validity | `amount >= 0`, status in an allowed set |
| Referential | every `customer_sk` exists in `dim_customer` |
| Freshness | the latest partition landed within the SLA |

Tools: dbt tests, Great Expectations, Soda, Deequ. Quarantine bad rows instead of dropping
them silently, and record **data contracts** with the producing team.

### 6-6) File and table formats ⭐⭐⭐⭐

**Parquet is the analytics default: columnar, compressed, with per-row-group statistics that
let a query skip data. A table format like Iceberg or Delta adds ACID transactions and time
travel on top of those files.**

| Format | Layout | Strength |
|---|---|---|
| CSV / JSON | row, text | human-readable; slow, no types |
| **Avro** | row, binary, schema embedded | streaming and ingestion; best schema evolution |
| **Parquet** | columnar | analytics scans, compression, predicate pushdown |
| **ORC** | columnar | the Hive ecosystem, similar to Parquet |

Table formats (**Iceberg, Delta Lake, Hudi**) keep a transaction log / metadata tree over
Parquet files, which gives: atomic commits, `MERGE` / `UPDATE` / `DELETE`, schema evolution,
time travel, hidden partitioning (Iceberg), and compaction of small files. That is what turns
a data lake into a **lakehouse**.

---

## 7) Schema Design ⭐⭐⭐⭐

### 7-1) Start from the queries and the grain

**Schema design answers three questions before any DDL: what one row means, how the data
will be queried, and how it will change.**

A worked answer to "design the tables for a ride-sharing app's analytics":

1. **Processes** — trip requested, trip completed, payment.
2. **Grain** — `fact_trip`: one row per completed trip.
3. **Dimensions** — rider, driver (SCD2: rating, city change), date, location (zone).
4. **Measures** — fare, distance, duration, surge multiplier.
5. **Access** — "trips and revenue per city per day" → partition by `trip_date`, cluster by `city_id`.

```sql
CREATE TABLE fact_trip (
  trip_id          BIGINT        NOT NULL,      -- degenerate dimension
  trip_date        DATE          NOT NULL,      -- partition column
  rider_sk         BIGINT        NOT NULL,
  driver_sk        BIGINT        NOT NULL,
  pickup_zone_sk   INT           NOT NULL,
  dropoff_zone_sk  INT           NOT NULL,
  requested_at     TIMESTAMP     NOT NULL,      -- store in UTC
  completed_at     TIMESTAMP,
  distance_km      DECIMAL(8,2),
  fare_amount      DECIMAL(12,2),               -- money: DECIMAL, never FLOAT
  currency_code    CHAR(3)
)
PARTITIONED BY (trip_date)
CLUSTERED BY (pickup_zone_sk);
```

### 7-2) Keys: natural vs surrogate

| | Natural key | Surrogate key |
|---|---|---|
| What | a business identifier (`email`, `order_no`) | a meaningless generated id (`customer_sk`) |
| Pro | meaningful, no lookup | stable, small, enables SCD2 versions, joins across sources |
| Con | can change, can collide across source systems | needs a lookup / generation step |

In a warehouse dimension, keep both: the surrogate key as the primary key, the natural key
for lookups and dedupe. In distributed systems, generate ids without a central counter —
UUIDv7 / ULID (time-ordered) or a hash of the natural key.

### 7-3) Partitioning, clustering and bucketing

**Partition by what nearly every query filters on, with low enough cardinality that each
partition stays large — usually a date.**

| Technique | What it does | Pick |
|---|---|---|
| **Partitioning** | separate directories / segments per value; pruning skips them | date; aim for partitions of ≥ ~1 GB |
| **Clustering / sort key** | orders data within files; min/max stats skip blocks | the next most-filtered columns (`user_id`, `country`) |
| **Bucketing** | hash rows into N files by key | join keys, so joins skip the shuffle |
| **Z-order / liquid clustering** | multi-column locality | several filter columns at once |

Traps: partitioning by `user_id` (millions of tiny partitions); partitioning by a local-time
date on UTC data; a partition column no query filters on.

### 7-4) Schema evolution

**Additive changes are safe; renames, type narrowing and removing a required field break
someone downstream — so every change is checked against a compatibility rule.**

| Change | Usually safe? |
|---|---|
| Add a nullable column / a field with a default | yes |
| Widen a type (`INT` → `BIGINT`) | usually |
| Rename a column | no — add new, backfill, deprecate old |
| Drop a column | breaks readers that use it |
| Narrow a type / change meaning | no |

- **Schema Registry** (Avro / Protobuf / JSON Schema) enforces `BACKWARD` (new readers read
  old data — the default), `FORWARD`, or `FULL` compatibility on each topic.
- Iceberg / Delta track columns by id, so renames and reorders are metadata-only.
- Agree changes through a **data contract**, and version breaking ones (`orders_v2`).

### 7-5) Data types and conventions

- Money → `DECIMAL(p, s)`, never `FLOAT` (rounding).
- Timestamps → UTC, `TIMESTAMP WITH TIME ZONE` or an explicit `_utc` suffix; keep the
  local-time date only as a derived column.
- `NOT NULL` on keys and required fields — it documents intent and catches bad loads.
- Enums → a lookup dimension or a constrained string, not magic integers.
- Audit columns on every table: `created_at`, `updated_at`, `_ingested_at`, `_source`, `_batch_id`.
- Consistent naming: `snake_case`, `fact_` / `dim_` prefixes, `_id` for natural, `_sk` for surrogate keys.
- **Semi-structured** data (JSON) → land it raw in a `VARIANT` / `JSON` / `STRUCT` column,
  and promote the fields queries actually use to typed columns.

---

## 8) Stream Data Pipelines ⭐⭐⭐⭐

### 8-1) Batch vs stream, Lambda vs Kappa

**Batch processes a bounded dataset on a schedule; streaming processes an unbounded one
continuously. Choose streaming only when the latency is worth the operational cost.**

| | Batch | Stream |
|---|---|---|
| Data | bounded | unbounded |
| Latency | minutes–hours | ms–seconds |
| Correctness | simple — all data is there | late and out-of-order data |
| Reprocessing | rerun the partition | replay from the log |
| Cost to run | lower | always-on, stateful |

- **Lambda architecture** — a batch layer (accurate) plus a speed layer (fast), merged at
  serving time. Two codebases computing the same thing.
- **Kappa architecture** — streaming only; reprocess by replaying the Kafka log through a new
  job version. One codebase; relies on long retention.
- Today, engines that run the same code in batch and stream (Spark, Flink, Beam) plus
  lakehouse tables have made most new designs Kappa-like.

```text
App / DB ─► CDC / producers ─► Kafka ─► Flink / Spark Streaming ─► sinks
                                 │          (state + checkpoints)     ├─ OLAP store (Pinot, Druid, ClickHouse)
                                 │                                    ├─ lakehouse table (Iceberg / Delta)
                                 └─► raw archive in object storage    └─ alerts / serving DB
```

### 8-2) Event time, watermarks and windows ⭐⭐⭐⭐⭐

**Aggregate by event time — when it happened — not processing time; a watermark is the
engine's estimate that no event older than *T* is still coming, and it decides when a
window can close.**

| Window | Shape | Example |
|---|---|---|
| **Tumbling** | fixed size, no overlap | clicks per minute |
| **Sliding / hopping** | fixed size, overlapping | 10-minute average every 1 minute |
| **Session** | gap-based, per key | a user's activity until 30 minutes idle |

- Watermark = max event time seen − allowed lateness. A record behind the watermark is
  **late**: drop it, send it to a side output, or update an already-emitted result.
- A larger lateness allowance → more complete results, more state, higher latency.

```python
# python — Spark Structured Streaming, 5-minute event-time windows
from pyspark.sql import functions as F

events = (spark.readStream.format("kafka")
               .option("kafka.bootstrap.servers", "broker:9092")
               .option("subscribe", "clicks").load()
               .select(F.from_json(F.col("value").cast("string"), schema).alias("e"))
               .select("e.*"))

counts = (events.withWatermark("event_time", "10 minutes")
                .groupBy(F.window("event_time", "5 minutes"), "page_id")
                .count())

(counts.writeStream.outputMode("update")
       .option("checkpointLocation", "s3://bucket/chk/clicks")
       .format("delta").start("s3://bucket/tables/click_counts"))
```

### 8-3) State, checkpoints and exactly-once end to end ⭐⭐⭐⭐

**Exactly-once end to end needs three parts: a replayable source, engine state that is
checkpointed together with the source offsets, and a sink that is idempotent or
transactional.**

- **State** — running aggregates, join buffers, dedupe sets; kept in a state backend
  (RocksDB) and bounded with TTLs or watermarks, or it grows forever.
- **Checkpoint** — a consistent snapshot of state + source offsets (Flink: Chandy-Lamport
  barriers). On failure, restore the snapshot and replay from those offsets.
- **Sink** — Kafka transactions, two-phase commit (Flink `TwoPhaseCommitSinkFunction`), a
  transactional table commit (Delta / Iceberg), or an upsert keyed by a deterministic id.
- Without that, the honest answer is **at-least-once + idempotent writes**, which is what most
  production pipelines run.

### 8-4) Spark Structured Streaming vs Flink vs Kafka Streams

| | Spark Structured Streaming | Apache Flink | Kafka Streams |
|---|---|---|---|
| Model | micro-batch (continuous mode is experimental) | true record-at-a-time | record-at-a-time library |
| Latency | ~100 ms–seconds | milliseconds | milliseconds |
| State | state store (RocksDB available) | first-class, very large state, savepoints | local RocksDB + changelog topics |
| Deploy | a Spark cluster | a Flink cluster | inside your app — no cluster |
| Pick when | the team already runs Spark; unified batch + stream | low latency, complex event time, big state | Kafka-in, Kafka-out microservices |

### 8-5) Backpressure, late data and failure handling

- **Backpressure** — a slow sink slows the operators upstream instead of buffering until
  OOM (Flink propagates it through network buffers; Spark bounds intake with
  `maxOffsetsPerTrigger`). Kafka absorbs the burst as lag.
- **Poison messages** — catch deserialisation and validation errors, send them to a
  **dead-letter queue** with the error, keep the pipeline running.
- **Reprocessing** — keep the raw stream (long retention or an archive in object storage) so
  a fixed job can replay it.
- **Monitor**: consumer lag, end-to-end latency, checkpoint duration and failures, watermark
  delay, records dropped as late.

---

## 9) SQL vs NoSQL ⭐⭐⭐⭐

### 9-1) The comparison

**SQL databases give you a fixed schema, joins and ACID transactions on a mostly single-node
or tightly coordinated design; NoSQL databases trade some of that for horizontal scale, a
flexible schema, and a data model shaped around one access pattern.**

| | SQL (relational) | NoSQL |
|---|---|---|
| Data model | tables, rows, relations | key-value, document, wide-column, graph |
| Schema | schema-on-write, enforced | flexible / schema-on-read |
| Query | SQL, ad-hoc joins | API per store; joins usually done in the app |
| Transactions | ACID, multi-row | often single-item / single-partition; some offer more |
| Scale | vertical first; read replicas, sharding is hard | horizontal by partition key |
| Consistency | strong | tunable, often eventual |
| Examples | PostgreSQL, MySQL, Oracle | Redis, DynamoDB, MongoDB, Cassandra, Neo4j |

**NewSQL** (Spanner, CockroachDB, TiDB) gives SQL and ACID with horizontal scale, at the
cost of cross-region write latency.

### 9-2) ACID vs BASE, CAP and PACELC

- **ACID** — **A**tomicity (all or nothing), **C**onsistency (constraints hold),
  **I**solation (concurrent transactions do not interfere), **D**urability (committed
  survives a crash).
- **BASE** — **B**asically **A**vailable, **S**oft state, **E**ventually consistent.
- **CAP** — during a network **P**artition, a distributed store must choose **C**onsistency
  (refuse some requests) or **A**vailability (answer, possibly stale). Partitions happen, so
  the real choice is CP vs AP.
- **PACELC** — and **E**lse, without a partition, it trades **L**atency against
  **C**onsistency. DynamoDB / Cassandra: PA/EL. Spanner: PC/EC.

| Store | CAP lean | Note |
|---|---|---|
| Cassandra, DynamoDB | AP | tunable: `QUORUM` reads + writes (R + W > N) give strong reads |
| MongoDB, HBase | CP | single primary per shard / region |
| Spanner | CP | TrueTime; externally consistent |

### 9-3) The NoSQL families

| Family | Model | Good at | Examples |
|---|---|---|---|
| **Key-value** | `key → blob` | caching, sessions, counters, O(1) lookups | Redis, DynamoDB, Memcached |
| **Document** | `key → JSON document` | nested, varying entities read whole | MongoDB, Couchbase, Firestore |
| **Wide-column** | partition key → sorted rows of columns | huge write throughput, time series | Cassandra, HBase, Bigtable, ScyllaDB |
| **Graph** | nodes + edges | many-hop relationship queries | Neo4j, Neptune |
| **Search** | inverted index | full-text, faceted search, logs | Elasticsearch, OpenSearch |
| **Time-series** | time-partitioned metrics | metrics, IoT | InfluxDB, TimescaleDB, Prometheus |

### 9-4) Modeling in NoSQL: query-first

**A relational schema is designed from the entities; a NoSQL schema is designed from the
queries — one table per access pattern, denormalised, with the partition key chosen so a
query hits one partition.**

```sql
-- Cassandra (CQL): "latest messages in a chat room"
CREATE TABLE messages_by_room (
  room_id     UUID,
  bucket      TEXT,          -- e.g. '2026-11' — caps partition size
  sent_at     TIMEUUID,
  sender_id   UUID,
  body        TEXT,
  PRIMARY KEY ((room_id, bucket), sent_at)
) WITH CLUSTERING ORDER BY (sent_at DESC);
```

- **Partition key** — spreads load; a key with a few huge values creates **hot partitions**.
- **Clustering key** — sort order inside a partition, enables range scans.
- A second query pattern ("messages by user") gets a **second table**, written alongside.
- DynamoDB single-table design follows the same rule with `PK` / `SK` and GSIs.

### 9-5) How to choose

| Need | Pick |
|---|---|
| Money, inventory, multi-row invariants | SQL (PostgreSQL / MySQL) |
| Ad-hoc analytics and joins | SQL warehouse (BigQuery, Snowflake) |
| Very high write throughput, known queries, multi-region | wide-column (Cassandra) / DynamoDB |
| Low-latency cache or counters | Redis |
| Flexible nested documents | MongoDB / Firestore |
| Relationship traversal | graph DB |
| Global scale **and** SQL + ACID | Spanner / CockroachDB |

The interview answer: **"start with PostgreSQL unless a requirement rules it out"** — then
name the requirement (write volume, latency, schema variability, global distribution) and the
store that satisfies it. Most real systems are **polyglot**: an OLTP database, a cache, a
search index, a warehouse, joined by CDC through Kafka.

---

## 10) Last-Minute Checklist

One line per topic — if you can say each of these out loud, the section is solid.

- **Spark** — driver/executor, narrow vs wide, why a shuffle ends a stage, broadcast vs sort-merge, fixing skew
- **Kafka** — partition = order + parallelism, acks/ISR, consumer groups, at-least-once vs exactly-once
- **Hadoop** — HDFS blocks + NameNode, MapReduce shuffle, YARN, what replaced each tool
- **Modeling** — grain first, star vs snowflake, fact table types, SCD2 with surrogate keys
- **SQL** — execution order, ROW_NUMBER/RANK/LAG, top-N per group, gaps and islands, NOT IN + NULL
- **Batch** — ELT + medallion, idempotent partition overwrite, CDC, orchestration, data-quality checks
- **Schema** — queries + grain, surrogate keys, partition by date, compatible evolution, DECIMAL + UTC
- **Stream** — event time + watermarks, window types, checkpoints, exactly-once end to end
- **SQL vs NoSQL** — ACID vs BASE, CAP/PACELC, the families, query-first modeling, "start with Postgres"
