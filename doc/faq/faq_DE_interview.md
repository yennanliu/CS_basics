# Data Engineering Interview FAQ

> **Scope** — the high-level map of a data engineer interview and the last-minute checklist: for each topic, the handful of points you must be able to say out loud, and a link to the deep dive under `de/` that holds the mechanism, the code and the follow-up questions.
> **See also**: [`de/spark.md`](./de/spark.md), [`de/kafka.md`](./de/kafka.md), [`de/hadoop.md`](./de/hadoop.md), [`de/data_modeling.md`](./de/data_modeling.md), [`de/sql_for_data.md`](./de/sql_for_data.md), [`de/batch_pipeline.md`](./de/batch_pipeline.md), [`de/schema_design.md`](./de/schema_design.md), [`de/stream_pipeline.md`](./de/stream_pipeline.md), [`de/flink.md`](./de/flink.md), [`de/sql_vs_nosql.md`](./de/sql_vs_nosql.md), [`de/aws_data.md`](./de/aws_data.md), [`de/data_performance.md`](./de/data_performance.md) and [`de/analytics.md`](./de/analytics.md) — one deep dive per topic.

Read this file the night before. If any line below is something you cannot explain in two
minutes, open that topic's deep dive.

---

## 1) The Topic Map ⭐⭐⭐⭐⭐

| Topic | Weight | What the interviewer is really probing | Deep dive |
|---|---|---|---|
| Apache Spark | ⭐⭐⭐⭐⭐ | do you know where the time goes — shuffles, skew, memory | [`de/spark.md`](./de/spark.md) |
| Kafka | ⭐⭐⭐⭐⭐ | ordering, durability and delivery guarantees, and their cost | [`de/kafka.md`](./de/kafka.md) |
| Hadoop ecosystem | ⭐⭐⭐ | the foundations, and what replaced each piece | [`de/hadoop.md`](./de/hadoop.md) |
| Data modeling | ⭐⭐⭐⭐⭐ | grain, star schemas and history (SCD) | [`de/data_modeling.md`](./de/data_modeling.md) |
| SQL for data | ⭐⭐⭐⭐⭐ | window functions and the classic patterns, written live | [`de/sql_for_data.md`](./de/sql_for_data.md) |
| Batch pipelines | ⭐⭐⭐⭐ | idempotency, backfills, data quality | [`de/batch_pipeline.md`](./de/batch_pipeline.md) |
| Schema design | ⭐⭐⭐⭐ | designing tables from access patterns, and evolving them safely | [`de/schema_design.md`](./de/schema_design.md) |
| Stream pipelines | ⭐⭐⭐⭐ | event time, watermarks, state and exactly-once | [`de/stream_pipeline.md`](./de/stream_pipeline.md) |
| Apache Flink | ⭐⭐⭐⭐ | the engine under most streaming answers — state backends, checkpoints, exactly-once with Kafka | [`de/flink.md`](./de/flink.md) |
| SQL vs NoSQL | ⭐⭐⭐⭐ | choosing a store and defending the trade-off | [`de/sql_vs_nosql.md`](./de/sql_vs_nosql.md) |
| AWS data services | ⭐⭐⭐⭐ | Glue vs EMR vs Athena, Iceberg on S3, getting data out of RDS, S3 → SQS → Lambda ingestion, CDK and monitoring | [`de/aws_data.md`](./de/aws_data.md) |
| Storage and processing performance | ⭐⭐⭐⭐⭐ | read less, move less, avoid OOM in Spark / databases / Trino, sort and compact data, read vs write trade-offs, SQL rewrites | [`de/data_performance.md`](./de/data_performance.md) |
| Analytics | ⭐⭐⭐⭐ | precise metric definitions, non-additive metrics, A/B test data, root-causing a metric change | [`de/analytics.md`](./de/analytics.md) |

---

## 2) Apache Spark ⭐⭐⭐⭐⭐

- **Architecture** — one driver plans and schedules; executors run one task per partition; a cluster manager (YARN, Kubernetes) hands out the resources.
- **Lazy evaluation** — transformations build a plan; an action runs it as a job, cut into stages at every shuffle.
- **Narrow vs wide** — `map` / `filter` stay in a stage; `groupBy` / `join` / `distinct` shuffle data over the network, and the shuffle is where the time goes.
- **DataFrames over RDDs** — the schema lets Catalyst optimise the plan and Tungsten store rows compactly; a Python UDF blocks both.
- **Joins** — broadcast hash join when one side is small, sort-merge join otherwise.
- **Skew** — one task runs far longer than the rest; fix with AQE skew join, broadcasting, isolating the hot key, or salting.
- **Partitions** — `repartition` shuffles, `coalesce` only merges; aim for partitions of roughly 100–200 MB, and avoid writing thousands of small files.
- **Failures** — driver OOM is usually a `collect()`; executor OOM is usually skew or too few shuffle partitions; lost partitions are recomputed from lineage.

Deep dive: [`de/spark.md`](./de/spark.md).

---

## 3) Apache Kafka ⭐⭐⭐⭐⭐

- **Model** — a partitioned, replicated, append-only log; consumers track their own offset, and data stays until retention expires.
- **Partition** — the unit of parallelism and of ordering; order is guaranteed only within a partition, so key by the entity whose order matters.
- **Durability** — `acks=all` with `min.insync.replicas=2` and replication factor 3 survives a single broker failure; `acks=1` can lose a write on leader failover.
- **Consumer groups** — one consumer per partition per group, so partition count caps parallelism; rebalances reassign partitions.
- **Delivery** — commit after processing gives at-least-once; exactly-once needs idempotent producers, transactions and `read_committed`, plus an idempotent or transactional sink outside Kafka.
- **Retention vs compaction** — delete old segments by time / size, or keep the latest value per key as a changelog.
- **Lag** — latest offset minus committed offset: the first metric to alert on.

Deep dive: [`de/kafka.md`](./de/kafka.md).

---

## 4) The Hadoop Ecosystem ⭐⭐⭐

- **HDFS** — large blocks (128 MB by default), three replicas with rack awareness, all metadata in the NameNode's memory — which is why small files hurt.
- **MapReduce** — map → shuffle and sort by key → reduce, writing to disk between every job; Spark replaced it by keeping data in memory across a DAG.
- **YARN** — ResourceManager, NodeManagers and one ApplicationMaster per application; lets Spark, Hive and others share a cluster.
- **Hive** — SQL over files plus the Metastore, which outlived Hive's engine as the catalog many engines still read.
- **HBase** — a Bigtable-style wide-column store on HDFS; row key design decides performance.
- **What replaced it** — object storage instead of HDFS, Spark / Trino instead of MapReduce / Hive, Kafka instead of Flume, Airflow instead of Oozie, and table formats (Iceberg, Delta, Hudi) for ACID on the lake.

Deep dive: [`de/hadoop.md`](./de/hadoop.md).

---

## 5) Data Modeling ⭐⭐⭐⭐⭐

- **OLTP vs OLAP** — normalise for correct writes, denormalise for fast reads.
- **Grain first** — say what one fact row means before naming a single column.
- **Star schema** — a fact table of measures and foreign keys, surrounded by denormalised dimensions; snowflake normalises the dimensions.
- **Fact types** — transaction, periodic snapshot, accumulating snapshot, factless.
- **Additivity** — additive, semi-additive (balances: not across time), non-additive (ratios: store the parts).
- **SCD Type 2** — close the old row and insert a new version with validity dates; facts store the surrogate key, which pins them to the version that was true.
- **Approaches** — Kimball (conformed stars), Inmon (3NF warehouse first), Data Vault (hubs, links, satellites), one big table.

Deep dive: [`de/data_modeling.md`](./de/data_modeling.md).

---

## 6) SQL for Data ⭐⭐⭐⭐⭐

- **Execution order** — `FROM` → `WHERE` → `GROUP BY` → `HAVING` → window → `SELECT` → `ORDER BY` → `LIMIT`; that is why a window result cannot be filtered in `WHERE`.
- **Window functions** — `ROW_NUMBER` / `RANK` / `DENSE_RANK`, `LAG` / `LEAD`, running `SUM`; know the default frame when `ORDER BY` is present.
- **The patterns** — top-N per group, dedupe, gaps and islands, running totals, retention, funnels, sessionisation, median.
- **NULL traps** — `NULL` never equals anything; `NOT IN` with a `NULL` returns no rows; a `WHERE` on the right table turns a `LEFT JOIN` into an inner join.
- **Fan-out** — joining to a many-rows-per-key table double-counts; aggregate first, check the grain.
- **Warehouse cost** — bytes scanned: no `SELECT *`, filter on the partition column, let clustering skip blocks.

Deep dive: [`de/sql_for_data.md`](./de/sql_for_data.md).

---

## 7) Batch Data Pipelines ⭐⭐⭐⭐

- **ELT** — land raw data, transform inside the warehouse; keep the raw layer so a bug is fixed by re-running.
- **Layers** — bronze (raw) → silver (cleaned, conformed) → gold (business marts).
- **Idempotency** — each run overwrites the partition for its logical date, so retries and backfills are safe; never use `now()` in the logic.
- **Incremental loads** — a timestamp watermark misses hard deletes; log-based CDC catches them.
- **Orchestration** — Airflow owns dependencies, schedules, retries and backfills, not the transformation logic.
- **Data quality** — schema, not-null, uniqueness, referential and freshness checks at every layer boundary.
- **Formats** — Parquet for analytics, Avro for ingestion; Iceberg / Delta / Hudi add ACID, `MERGE` and time travel.

Deep dive: [`de/batch_pipeline.md`](./de/batch_pipeline.md).

---

## 8) Schema Design ⭐⭐⭐⭐

- **Method** — requirements → access patterns → grain → keys → physical layout → how it will change.
- **Keys** — a surrogate key as the primary key, the natural key kept for lookups; time-ordered ids (UUIDv7, ULID) in distributed systems.
- **Layout** — partition by what almost every query filters on (usually a date), cluster or sort by the next filter columns, bucket by join keys.
- **Evolution** — adding a nullable column is safe; renames, drops and type narrowing break readers; enforce compatibility with a schema registry.
- **Types** — `DECIMAL` for money, UTC timestamps, `NOT NULL` on keys, audit columns on every table.

Deep dive: [`de/schema_design.md`](./de/schema_design.md).

---

## 9) Stream Data Pipelines ⭐⭐⭐⭐

- **When to stream** — only when the latency is worth an always-on, stateful system; otherwise batch.
- **Lambda vs Kappa** — two codebases (batch + speed) vs one streaming codebase that reprocesses by replaying the log.
- **Event time** — aggregate by when it happened; a watermark estimates how late data can arrive and decides when a window closes.
- **Windows** — tumbling, sliding, session; late data is dropped, side-output, or used to update a result.
- **Exactly-once end to end** — a replayable source, state checkpointed together with source offsets, and a transactional or idempotent sink.
- **Engines** — Flink for low latency and large state, Spark Structured Streaming for teams already on Spark, Kafka Streams inside a service.
- **Operations** — backpressure, dead-letter queues, consumer lag, checkpoint health.

Deep dive: [`de/stream_pipeline.md`](./de/stream_pipeline.md); the Flink engine itself — runtime, state backends, checkpoint settings, the Kafka sink — is [`de/flink.md`](./de/flink.md).

---

## 10) SQL vs NoSQL ⭐⭐⭐⭐

- **The trade** — SQL gives schema, joins and ACID; NoSQL trades some of that for horizontal scale and a model shaped around one access pattern.
- **ACID vs BASE** — strong transactional guarantees vs basically available, eventually consistent.
- **CAP / PACELC** — under a partition, choose consistency or availability; otherwise, latency or consistency.
- **Families** — key-value (Redis, DynamoDB), document (MongoDB), wide-column (Cassandra), graph (Neo4j), search (Elasticsearch).
- **Query-first modeling** — in NoSQL, one table per access pattern, with a partition key that spreads load and avoids hot partitions.
- **The default answer** — start with PostgreSQL unless a named requirement rules it out, then name the store that meets it.

Deep dive: [`de/sql_vs_nosql.md`](./de/sql_vs_nosql.md).

---

## 11) Answering an Architecture Question ⭐⭐⭐⭐

Most DE loops include one "design a pipeline" question. The same six steps work for all of them:

1. **Clarify** — sources, volume, latency requirement, consumers, correctness requirement.
2. **Batch or stream** — decide from the latency requirement, and say why.
3. **Draw the flow** — sources → ingestion → storage layers → processing → serving.
4. **Model the data** — the grain, the fact and dimension tables, the partitioning.
5. **Make it reliable** — idempotency, backfills, late data, exactly-once or at-least-once with dedupe.
6. **Operate it** — data quality checks, monitoring, alerting, cost.

---

## 12) Last-Minute Checklist

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
- **AWS** — one Glue catalog for every engine, Glue vs EMR, Iceberg commit = pointer swap, Athena bills bytes scanned, CDC out of RDS + the replication-slot trap
- **Performance** — measure first; prune + push down; aggregate before join; nothing unbounded to one node; sort / Z-order + compact; `MERGE` on a deduplicated source; every index is a write cost
- **Analytics** — define numerator / denominator / window / filters; ratio of sums (of additive parts), not average of ratios; distinct counts don't add; SRM check; "is the data right?" before any story
