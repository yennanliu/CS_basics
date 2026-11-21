# Data Engineering System Design

> **Scope** — the system-design round of a data engineering interview: how the round is run and scored, back-of-the-envelope sizing for data volumes, the building blocks of a data platform, the concepts every design leans on, the trade-offs to argue out loud, and nine classic design problems worked from requirements to failure modes. The mechanism behind each building block lives in its deep dive, linked from the section that uses it.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`./batch_pipeline.md`](./batch_pipeline.md) and [`./stream_pipeline.md`](./stream_pipeline.md) — the two pipeline deep dives, each with its own worked design; [`./kafka.md`](./kafka.md), [`./spark.md`](./spark.md), [`./flink.md`](./flink.md) — the engines; [`./data_modeling.md`](./data_modeling.md) and [`./schema_design.md`](./schema_design.md) — the model and the tables; [`./sql_vs_nosql.md`](./sql_vs_nosql.md) — picking the serving store; [`./aws_service_selection.md`](./aws_service_selection.md) — the same designs on AWS.

Every answer leads with the sentence you would say out loud, then the mechanism, then the
trade-off the interviewer pushes on next. The aim is not to name every tool: it is to show
that each box in the diagram is there because a requirement put it there.

---

## 1) How the Round Works ⭐⭐⭐⭐⭐

### 1-1) What the interviewer scores

**A data system design round scores whether you turn a vague ask into requirements, pick a
design those requirements justify, and can defend its correctness when data is late, wrong or
replayed — not whether you know the most tools.**

| Signal | What a strong answer shows | What a weak one shows |
|---|---|---|
| **Requirements** | asks about volume, freshness, consumers and correctness before drawing | draws Kafka → Spark → Snowflake in the first minute |
| **Design** | each component traced to a requirement; the simplest design that meets them | a tool for every box, "because it scales" |
| **Correctness** | idempotency, late data, dedupe, backfills, schema change | assumes every event arrives once, on time, in order |
| **Trade-offs** | names the option not taken and what would flip the choice | one option, presented as the only one |
| **Operations** | monitoring, data quality, cost, on-call, reprocessing | stops when the diagram is drawn |
| **Communication** | drives the session, checks in, manages time | waits to be asked each next step |

The difference between a backend design round and a data one: a backend round is mostly about
**serving requests** (latency, availability, read/write path). A data round is mostly about
**moving and reshaping data correctly over time** — the hard questions are what happens on a
rerun, a late event, a schema change, or a bug found three weeks later.

### 1-2) The 45-minute plan

**Spend the first quarter on requirements and numbers, the middle half on the design and its
correctness, and the last quarter on scaling, failure and trade-offs — and say the plan out
loud so the interviewer can redirect you.**

```text
 0-8  min  Clarify     functional ask, consumers, freshness SLA, volume, correctness bar
 8-12 min  Estimate    events/s at peak, bytes/day, storage per year, partitions
12-25 min  High level  sources → ingest → storage layers → processing → serving (+ control plane)
25-35 min  Deep dive   data model + grain, idempotency, late data, dedupe, backfill
35-42 min  Scale/fail  hot keys, 10× growth, a region or a job down, bad data upstream
42-45 min  Wrap up     trade-offs made, what you would do with more time, how you'd monitor it
```

The six steps in [`../faq_DE_interview.md`](../faq_DE_interview.md#11-answering-an-architecture-question-)
are the same plan in short form; this file is the long form.

### 1-3) Clarifying questions that change the design

**Ask the questions whose answer would change a box in the diagram, and skip the ones that
would not.** Each row below flips a real decision:

| Question | If the answer is… | …the design becomes |
|---|---|---|
| How fresh must the output be? | next morning | batch, daily partitions, cheapest compute |
| | under a minute | streaming engine, a real-time OLAP or KV serving store |
| Who consumes it? | analysts in BI | warehouse / lakehouse tables, dimensional model |
| | a product API at 10k QPS | precomputed results in a KV store or cache |
| | ML training | point-in-time correct history, not just the latest value |
| Can numbers be revised later? | yes | upsert keyed results, a late-data window, a reconciliation job |
| | no (billing, finance) | exactly-once or reconcile-before-publish, an audit trail |
| What is the source? | an OLTP database | CDC (log-based) rather than `SELECT` on the primary |
| | mobile / web clients | an ingestion API, client-side `event_id`, heavy dedupe |
| | third-party files / APIs | landing zone, schema validation, quarantine |
| Do rows get deleted / is there PII? | yes | log-based CDC to see deletes; a table format that supports row deletes; tokenisation |
| How much history? | 30 days | simple retention, cheap |
| | 7 years, auditable | immutable raw layer, tiered storage, time travel |

State assumptions when the interviewer will not give numbers: *"I'll assume 100 M daily users
and 50 events each — tell me if that is off by an order of magnitude."*

---

## 2) Back-of-the-Envelope for Data ⭐⭐⭐⭐⭐

### 2-1) Numbers to know

**Data estimates need only four conversions — per day to per second, peak over average, raw
to compressed, and throughput to partitions — and each is an order of magnitude, not a
benchmark.**

| Rule of thumb | Value | Use it for |
|---|---|---|
| Seconds in a day | 86,400 ≈ **10⁵** | 1 M events/day ≈ 12/s; 1 B/day ≈ 12 k/s |
| Peak / average | **2–5×** (diurnal), more for launches or sales | size ingest and stream jobs for peak, batch for the total |
| A JSON event | **0.5–1 KB** | bytes/day = events/day × size |
| Columnar compression vs raw JSON | **~3–10×** (Parquet + zstd / snappy) | lake storage per year |
| Planning throughput per Kafka partition | **~5–10 MB/s** written | partition count = peak MB/s ÷ that, then round up for consumer parallelism |
| S3 request rate per prefix | 3,500 writes / 5,500 reads per second | why many small files and one hot prefix hurt |
| Target file / Spark partition size | **128 MB – 1 GB** files; ~128–256 MB per task | compaction targets; `shuffle.partitions` sizing |
| HyperLogLog | ~12 KB for ~0.8% error, any cardinality | distinct counts (§6-4) |

Say where a number comes from (*"a common planning figure is…"*). An interviewer forgives a
number that is 2× off; they do not forgive an estimate that never turns into a decision.

### 2-2) A worked estimate

**Turn every number into a decision: partitions, cluster size, retention, cost.**

"Clickstream for 100 M DAU, 50 events per user per day, ~1 KB each":

```text
events/day   = 100 M × 50                 = 5 B/day
avg rate     = 5 B / 10^5 s               ≈ 50 k events/s   (more precisely ~58 k)
peak rate    = 3 × avg                    ≈ 175 k events/s
ingest bytes = 175 k × 1 KB               ≈ 175 MB/s at peak
raw per day  = 5 B × 1 KB                 = 5 TB/day (JSON)
lake per day = 5 TB ÷ ~5 (Parquet)        ≈ 1 TB/day   → ~365 TB/year before compaction / TTL
kafka parts  = 175 MB/s ÷ ~8 MB/s         ≈ 22 → 48 (consumer parallelism + 2× headroom)
kafka disk   = 5 TB/day × 7 days × RF 3   ≈ 105 TB raw; ~35 TB with producer compression
```

Decisions that fall out: **48 partitions**, keyed by `user_id` so a session stays ordered;
**7-day** Kafka retention because the lake is the long-term replay source; **daily partitions
with hourly sub-partitions** in the lake because a day is 1 TB; a **TTL or tiering** policy,
because 365 TB/year of raw events is the line item finance will ask about.

---

## 3) Building Blocks ⭐⭐⭐⭐⭐

### 3-1) The layered reference architecture

**Almost every data design is the same five layers — sources, ingestion, storage, processing,
serving — with a control plane across them for orchestration, metadata, quality and access.**

```text
  SOURCES          INGEST              STORAGE                 PROCESS             SERVE
 ┌─────────┐   ┌──────────────┐   ┌───────────────────┐   ┌──────────────┐   ┌────────────────┐
 │ OLTP DB │──►│ CDC (Debezium)│─┐ │ log: Kafka        │   │ stream: Flink│──►│ OLAP: Pinot /  │
 │ apps    │──►│ event API     │─┼►│ lake: S3 + Iceberg│◄─►│ batch: Spark │   │ Druid / ClickH │
 │ SaaS    │──►│ connectors    │─┘ │  bronze/silver/gold│   │ SQL: dbt     │──►│ warehouse + BI │
 │ files   │──►│ file landing  │   │ warehouse         │   └──────────────┘──►│ KV / cache /API│
 └─────────┘   └──────────────┘   └───────────────────┘                      │ feature store  │
                                                                              └────────────────┘
  CONTROL PLANE: orchestrator (Airflow) · catalog + lineage · schema registry · data quality ·
                 access control / PII · monitoring + cost
```

The sentence to say: **"Raw data lands immutable first; every downstream layer can be rebuilt
from it."** That one property is what makes bugs, backfills and new consumers cheap. Layer
details are in [`./batch_pipeline.md`](./batch_pipeline.md#1-the-reference-architecture-)
and [`./stream_pipeline.md`](./stream_pipeline.md#2-reference-architectures-).

### 3-2) Storage: pick by access pattern

**Choose storage by who reads it and how — scan-heavy analytics, point lookups, low-latency
aggregates, or replayable events — never by what is fashionable.**

| Store | Shape of access | Latency | Examples | Not for |
|---|---|---|---|---|
| **Event log** | ordered append, replay by offset | ms | Kafka, Kinesis, Pulsar | ad-hoc queries, long history (cost) |
| **Object store + table format** | big scans, cheap forever storage, `MERGE`, time travel | s–min | S3/GCS + Iceberg / Delta / Hudi | point lookups, sub-second dashboards |
| **Cloud warehouse** | SQL analytics, joins, BI concurrency | s | BigQuery, Snowflake, Redshift | high-QPS product reads, streaming upserts at scale |
| **Real-time OLAP** | aggregations on fresh data, high concurrency | 10–100 ms | Pinot, Druid, ClickHouse | complex joins, being the system of record |
| **KV / wide-column** | get by key at high QPS | ms | DynamoDB, Cassandra, Bigtable, Redis | scans and aggregations |
| **Search** | text, filters, facets | 10–100 ms | Elasticsearch / OpenSearch | system of record, joins |

Most real designs use **two or three** of these: the lake as the system of record, plus the
store that matches the serving pattern. The family-by-family comparison is in
[`./sql_vs_nosql.md`](./sql_vs_nosql.md).

### 3-3) Processing: batch, micro-batch, streaming

**Pick the processing mode from the freshness requirement, then the engine from the team and
the platform — Spark, Flink and SQL-in-the-warehouse all solve most problems.**

| Mode | Freshness | Typical engine | Strength | Cost |
|---|---|---|---|---|
| **Batch** | hours–day | Spark, warehouse SQL + dbt | simple, cheap, easy to backfill | stale |
| **Micro-batch** | seconds–minutes | Spark Structured Streaming | batch code reused; good throughput | latency floor = trigger interval |
| **Streaming** | ms–seconds | Flink, Kafka Streams | lowest latency, rich event-time and state | state, watermarks, harder ops |
| **In-store** | as fresh as ingest | materialized views, OLAP ingestion rollups | no job to run | limited logic, vendor-specific |

Engine internals: [`./spark.md`](./spark.md), [`./flink.md`](./flink.md); when streaming is
worth it: [`./stream_pipeline.md`](./stream_pipeline.md#1-1-when-streaming-is-worth-it).

### 3-4) Serving: design for the reader

**The serving layer is chosen by the reader's query, concurrency and latency — and the
pipeline should precompute exactly as much as that reader needs and no more.**

| Reader | Pattern | Serve from |
|---|---|---|
| Analyst, ad-hoc | arbitrary SQL over history | warehouse / lakehouse gold tables |
| Internal dashboard | fixed slices, refresh every few minutes | warehouse + BI extract, or real-time OLAP |
| User-facing analytics (e.g. "your post's views") | thousands of QPS, per-entity, fresh | real-time OLAP or precomputed KV |
| Product feature (recommendations, limits) | one key, < 10 ms | KV store / cache written by the pipeline |
| ML training / inference | point-in-time history / latest value | feature store: offline + online (§6-5) |
| Other systems | events or files | Kafka topic, reverse ETL, data share |

The trade-off is **precompute vs query time**: precomputing makes reads fast and cheap but
fixes the slices you can ask for; querying raw keeps flexibility but costs per query. See §5-5.

### 3-5) The control plane

**What separates a pipeline from a platform is the control plane: orchestration, a catalog
with lineage, a schema registry, quality checks and access control — mention it in every
design, because it is where operations live.**

| Piece | Owns | Examples |
|---|---|---|
| **Orchestrator** | schedules, dependencies, retries, backfills | Airflow, Dagster, Prefect |
| **Catalog + lineage** | what tables exist, who owns them, what feeds what | Glue / Unity / Hive Metastore, DataHub, OpenLineage |
| **Schema registry** | event schemas and their compatibility rules | Confluent / Glue Schema Registry |
| **Quality** | freshness, volume, nulls, uniqueness, reconciliation | dbt tests, Great Expectations, Soda |
| **Access / PII** | who sees which column, masking, deletes | Lake Formation, Ranger, column tags |

Orchestration and quality in depth: [`./batch_pipeline.md`](./batch_pipeline.md#4-orchestration-).

---

## 4) Core Concepts to Know ⭐⭐⭐⭐⭐

### 4-1) Delivery semantics and idempotency

**Distributed pipelines deliver at-least-once by default, so correctness comes from making
every write idempotent — "exactly-once" in practice means at-least-once delivery plus an
idempotent or transactional sink.**

| Semantics | Means | Cost | Use when |
|---|---|---|---|
| **At-most-once** | may lose, never duplicates | cheapest | metrics where a gap is fine |
| **At-least-once** | never loses, may duplicate | cheap | default; pair with dedupe or upsert |
| **Exactly-once (effectively)** | each event affects the result once | transactions, latency, coupling | money, counts that are billed |

The idempotent-write patterns, in the order to reach for them:

1. **Overwrite a partition** by its logical date — a rerun replaces, never appends.
2. **Upsert / `MERGE` on a natural key** — a duplicate overwrites itself.
3. **Dedupe on a producer-assigned `event_id`** within a bounded window.
4. **Transactional sink** (Kafka transactions, two-phase commit) when the above cannot work.

```sql
-- batch: a rerun of 2026-11-17 replaces that day, it never doubles it
INSERT OVERWRITE TABLE gold.fct_events PARTITION (dt = '2026-11-17')
SELECT event_id, user_id, event_type, event_ts
FROM (
  SELECT *, ROW_NUMBER() OVER (PARTITION BY event_id ORDER BY ingest_ts) AS rn
  FROM silver.events
  WHERE dt = '2026-11-17'
) deduped
WHERE rn = 1;
```

End-to-end exactly-once in a stream: [`./stream_pipeline.md`](./stream_pipeline.md#6-4-exactly-once-end-to-end-).

### 4-2) Event time, late data and results that change

**Aggregate by when the event happened, not when it arrived — and then decide, out loud, how
long you wait for late events and what happens to the ones that come after that.**

- **Event time** is what the business means by "clicks at 10:00"; processing time is an
  artefact of your pipeline's lag. A replay in processing time gives a different answer.
- A **watermark** says "events older than T are probably all here"; it trades latency for
  completeness.
- Three choices for events after the watermark: **drop and count them**, **update the result**
  (an upsert keyed by window), or **route them to a side output** and fix them in a
  reconciliation batch.
- The design consequence: **results are revisable**, so the serving store must accept upserts
  keyed by `(dimension, window_start)`, and consumers must accept that yesterday's number can
  move.

Mechanism: [`./stream_pipeline.md`](./stream_pipeline.md#3-time-semantics-and-watermarks-).

### 4-3) Partitioning, keys and skew

**Partition for the reader, key for ordering and parallelism, and plan for the hot key before
the interviewer asks — because real traffic is never uniform.**

| Where | Partition / key by | Why | The trap |
|---|---|---|---|
| Kafka topic | the entity whose order matters (`user_id`, `order_id`) | per-key order; parallel consumers | one celebrity key saturates one partition |
| Lake table | the column readers filter on, usually event date | partition pruning | too fine (per-user) → millions of tiny files |
| Stream job | the aggregation key | state is local per key | skewed key → one slow subtask stalls the watermark |
| Spark job | the join / group key | shuffle co-location | one key holds 40% of rows → one task runs for hours |

Hot-key fixes: **salt** the key and aggregate in two stages (`key#0..N` → `key`), **split the
hot key out** and handle it separately, **pre-aggregate locally** before the shuffle, or
**broadcast** the small side of a join. Skew in Spark: [`./spark.md`](./spark.md); hot keys in
streams: [`./stream_pipeline.md`](./stream_pipeline.md#8-3-hot-keys).

### 4-4) Schemas, evolution and contracts

**Producers change schemas without telling you; the design must make an incompatible change
fail loudly at the producer, and a compatible one flow through without a deploy.**

- **Schema registry** with a compatibility mode: **backward** (new readers read old data) is
  the usual default for event topics, so consumers upgrade first.
- **Safe changes**: add an optional field with a default. **Breaking**: rename, change a type,
  remove a required field — do those as a new field or a new topic version.
- **Lake tables**: table formats (Iceberg, Delta) evolve columns by ID, so add / rename is a
  metadata change; never rely on column position.
- **Data contracts**: the producing team owns a schema, its semantics and its SLA, checked in
  CI; bad records go to a **dead-letter / quarantine** table, not into silver.

Schema rules in depth: [`./schema_design.md`](./schema_design.md).

### 4-5) Backfill and reprocessing are requirements

**Every pipeline will be rerun — for a bug, a new column, a late source — so design for the
backfill on day one: deterministic jobs, an immutable raw layer, and outputs keyed by the
logical date rather than by "now".**

- Jobs read a **fixed data interval** passed in, never `now()` or "latest", so
  `run(2026-10-01)` gives the same answer today and next year.
- The **raw layer is immutable and long-retained**; every derived layer can be dropped and
  rebuilt from it.
- **Backfill without hurting today**: run it on separate capacity, cap concurrency, write to a
  shadow table and swap, and publish downstream only after validation.
- For streams: **replay from Kafka** within retention, or from the lake beyond it (the Kappa
  answer); a new job version runs side by side and switches over when caught up.

Mechanism: [`./batch_pipeline.md`](./batch_pipeline.md#3-idempotency-determinism-and-backfills-).

### 4-6) Data quality and observability

**A pipeline that runs green and writes wrong numbers is the worst failure in data
engineering, so the design checks the data, not only the job — and blocks publication on the
checks that matter.**

| Check | Catches | Typical rule |
|---|---|---|
| **Freshness** | a stuck source or job | max(event_ts) within SLA |
| **Volume** | a silently partial load | row count within ±X% of the same weekday |
| **Schema** | an upstream change | contract / registry compatibility |
| **Validity** | bad values | not null, ranges, accepted values |
| **Uniqueness** | duplicates from retries | unique on the natural key |
| **Reconciliation** | logic bugs, lost data | totals match the source of truth |

Then decide per check: **fail** (block publish), **warn**, or **quarantine** the bad rows.
Quality in depth: [`./batch_pipeline.md`](./batch_pipeline.md#6-data-quality-and-observability-).

### 4-7) Keeping two systems consistent

**Never write to a database and to Kafka from the same request and hope both succeed — use
CDC from the database's log, or a transactional outbox, so there is one source of truth and
the second write is derived from it.**

```text
 dual write (wrong):  service ─► DB  ✓
                      service ─► Kafka ✗  (crash between) → the two disagree forever

 outbox (right):      service ─► one DB transaction: UPDATE orders + INSERT outbox_events
                      CDC / relay reads outbox_events ─► Kafka   (at-least-once, consumers dedupe)
```

CDC with Debezium: [`./batch_pipeline.md`](./batch_pipeline.md#2-3-change-data-capture-with-debezium);
read/write split: [`../java/cqrs.md`](../java/cqrs.md).

---

## 5) The Trade-offs ⭐⭐⭐⭐⭐

### 5-1) Batch vs stream

**Default to batch and stream only what a requirement forces: streaming buys freshness and
costs state, watermarks, always-on compute and harder operations.**

| | Batch | Stream |
|---|---|---|
| Freshness | hours | seconds |
| Correctness | complete inputs, easy to reason about | late data, revisable results |
| Cost | pay while running | always on, sized for peak |
| Backfill | rerun a date | replay a log, run versions side by side |
| Ops | retries, a DAG | checkpoints, lag, state size, rescaling |

What flips it to streaming: a user-facing or operational decision that loses value in minutes
— fraud, alerting, live dashboards, inventory. "The business would like it faster" is a
reason to try hourly batch first.

### 5-2) Lambda vs Kappa

**Lambda runs a batch and a speed layer and merges them, paying for two codebases; Kappa
runs one streaming codebase and reprocesses by replaying the log — Kappa is the modern
default when the engine and retention allow it.**

The honest middle ground most companies run: **one streaming path for fresh results, plus a
nightly batch reconciliation over the lake** that corrects what arrived too late — two jobs,
but the batch one is simple SQL, not a second copy of the logic. Deep dive:
[`./stream_pipeline.md`](./stream_pipeline.md#1-3-lambda-vs-kappa-architecture).

### 5-3) Where data lives and where it is transformed

**ELT into a lakehouse or warehouse is the modern default; the trade-off is cost and lock-in
against simplicity, and the lake vs warehouse line is now mostly about who else reads it.**

| Choice | Pick A when | Pick B when |
|---|---|---|
| **ETL vs ELT** | heavy transforms, PII must be removed before load | warehouse compute is elastic; keep raw for reprocessing (default) |
| **Lake(house) vs warehouse** | many engines (Spark, Flink, ML) read the same data; huge cheap storage | mostly SQL + BI; a small team that wants zero ops |
| **Open table format vs proprietary** | avoid lock-in, multi-engine | one vendor's features and performance matter more |

Lake vs warehouse vs lakehouse in depth: [`./batch_pipeline.md`](./batch_pipeline.md#1-3-data-lake-vs-warehouse-vs-lakehouse).

### 5-4) Exactly-once vs at-least-once plus idempotent sink

**Prefer at-least-once delivery into an idempotent sink: it gives the same final result as
exactly-once with less latency and coupling; pay for transactions only where an append-only
sink makes duplicates unfixable.**

The idempotent sink only makes **re-emitting a result** safe. It cannot undo a duplicate
**input** that was already added into a count, and duplicate inputs come from two places:

- **The job's own recovery** replays the *same offsets*. Keeping the aggregate in the
  engine's checkpointed state covers this, because a restore rolls state and offsets back
  together.
- **Producer retries** write the same `event_id` at *different offsets*. A checkpoint sees
  two records and counts both, so these need **`event_id` dedupe** before the increment.

Most pipelines face both, so an aggregate needs both: checkpointed state *and* dedupe.

| | Exactly-once (transactional) | At-least-once + idempotent |
|---|---|---|
| Latency | output visible on checkpoint / commit | immediate |
| Coupling | sink must support transactions | sink must support upsert or dedupe |
| Failure | aborted transactions, longer recovery | brief duplicates, overwritten by key |
| Use for | append-only outputs others consume as a log | KV, OLAP upsert tables, `MERGE` into a lake |

### 5-5) Precompute vs compute at query time

**Precompute what is asked often with fixed slices; keep the raw detail for what is asked
rarely or unpredictably.**

| | Precompute (rollups, materialized tables) | Query time (raw + a fast engine) |
|---|---|---|
| Read latency | ms | seconds |
| Flexibility | only the dimensions you rolled up | any slice |
| Storage | multiplies with dimension combinations | raw once |
| Change | a new dimension = a backfill | a new query |

The usual answer is both: **raw detail in the lake, a few rollups for the dashboards and the
product**, and a rule that a rollup is added when a query pattern proves hot.

### 5-6) Ingestion: CDC vs query, push vs pull

**Log-based CDC is the right default for database sources — it sees deletes and every
intermediate change without loading the primary; query-based pulls are the fallback when you
cannot get at the log.**

| | Log-based CDC | Query-based (`updated_at > :watermark`) |
|---|---|---|
| Hard deletes | captured | missed (needs a periodic full diff) |
| Intermediate updates | all | only the latest at pull time |
| Load on source | reads the log | queries the tables |
| Setup | replication slot / binlog access, connector ops | one SQL query |
| Risk | slot left behind fills the source disk | clock skew and same-timestamp rows missed |

For client events, **push** (apps send to an ingestion API → Kafka) is the only option; make
the client stamp an `event_id` and an event time, and expect retries, duplicates and clock
skew.

### 5-7) Freshness, correctness, cost — pick the trade explicitly

**You can make a pipeline fresher, more correct or cheaper, and each one costs the others —
so say which one this design favours and why.**

```text
            freshness
               /\
              /  \        fresher  → streaming, always-on compute, revisable results
             /    \       correct  → wait for late data, reconcile, exactly-once
            /______\      cheaper  → batch, coarser partitions, fewer copies
     correctness   cost
```

Example sentence: *"Billing favours correctness, so it publishes after a reconciliation even
if that costs an hour; the live dashboard favours freshness and is allowed to be revised."*

### 5-8) Build vs buy

**Buy (managed) the undifferentiated plumbing — connectors, Kafka, the warehouse — and build
where your data or logic is the product; the deciding question is who will be on call for it.**

| Lean managed when | Lean self-managed when |
|---|---|
| a small team, standard sources | the scale makes managed pricing the largest cost |
| speed to first pipeline matters | you need a feature or a version the service lacks |
| compliance is covered by the vendor | data residency or network rules forbid it |

---

## 6) Classic Problems ⭐⭐⭐⭐⭐

Each problem follows the same shape: **clarify → numbers → design → the decisions to say out
loud → follow-ups**. Two classics are worked in full elsewhere and are not repeated here:

- *"Daily pipeline from an OLTP database to a reporting warehouse"* —
  [`./batch_pipeline.md`](./batch_pipeline.md#11-1-design-a-daily-pipeline-from-an-oltp-database-to-a-reporting-warehouse)
- *"Real-time rides and revenue per city"* —
  [`./stream_pipeline.md`](./stream_pipeline.md#13-worked-design-real-time-rides-per-city-)

### 6-1) Clickstream and product analytics

**Prompt**: "Collect events from our web and mobile apps and give product teams DAU, funnels
and retention, with a dashboard fresh within 15 minutes."

**Clarify**: event volume (use §2-2: 5 B/day), which metrics are fixed vs ad-hoc, how late
mobile events arrive (offline phones: hours to days), PII in events, retention needs.

```text
 apps (SDK: event_id, event_ts, user/device id, batching + retry)
   ─► collector API (stateless, validate against schema, enrich ip→geo, drop PII)
   ─► Kafka "events.raw" (key = user_id, 48 partitions, 7 d)
        ├─► Flink: dedupe event_id (TTL 72 h ≥ SDK retry window) ─► Kafka "events.clean" (key = event_id)
        │        ─► Pinot (upsert on event_id; 15-min dashboards)
        └─► Kafka Connect / Flink sink ─► S3 bronze (Iceberg, dt/hour)
                 ─► Spark/dbt hourly: silver (deduped, sessionized) ─► gold (DAU, funnels, retention)
                 ─► warehouse / BI for ad-hoc; invalid events ─► quarantine table
```

Decisions to say out loud:

1. **Client-side `event_id` and event time** — mobile retries and offline queues make
   duplicates and lateness the norm, not the exception.
2. **A retry contract the dedupe can cover**: the SDK gives up on an event after 72 h and the
   collector rejects older ones (counted, not silent), so a 72 h dedupe TTL sees every retry.
   Pinot's upsert on `event_id` is the second guard: a duplicate that still slips through
   overwrites its row instead of adding one, so the live numbers are right on their own and do
   not wait for the batch path to correct them.
3. **Schema validation at the collector**, with a dead-letter topic, so one bad SDK release
   cannot corrupt silver.
4. **Two serving paths**: Pinot for the fixed fresh dashboards, the lake + warehouse for
   ad-hoc funnels and retention over months.
5. **Sessionization in batch** (30-minute inactivity gap) where late events can be included;
   streaming session windows only if a product needs live sessions.
6. **DAU from distinct users per day**: exact in batch, HyperLogLog sketches for fast rollups
   across arbitrary date ranges (§6-4).

Follow-ups: *events arrive 3 days late* → event-date partitions with a reprocess window,
dashboards labelled provisional for the last N days; *GDPR delete* → delete by user everywhere the
data landed: row-level deletes in the lake tables, then **expire the old snapshots** so time
travel no longer holds the rows; the same delete in derived warehouse tables and Pinot; Kafka
retention short enough to age out; backups whose retention ends inside the legal deadline; and
a deletion log that proves each step ran. The log only records the deletes; it does not stop a
**replay** — a job restored from an older checkpoint, or a backfill from Kafka or bronze — from
writing the user back. So the deleted IDs also go into a durable **deletion filter**
(a suppression table) that ingestion, every replay and every backfill check before they
write to the lake or Pinot.

### 6-2) Ad click aggregation for billing

**Prompt**: "Count ad clicks per ad per minute; advertisers are billed on it and see it on a
dashboard. ~10 B clicks/day."

**Clarify**: billing correctness vs dashboard freshness (different bars), click fraud /
dedupe rules, how long a click can be late, query patterns (per ad, per campaign, per minute).

```text
 ad servers ─► click log ─► Kafka "clicks" (key = ad_id)
   ├─► Flink (event time, watermark 1 min, allowed lateness 10 min)
   │     1. dedupe click_id; drop clicks failing fraud rules → side output
   │     2. tumbling 1-min window per ad_id: count
   │     3. upsert (ad_id, minute) ─► OLAP store ─► advertiser dashboard (near real time)
   └─► raw clicks ─► S3 / Iceberg (immutable)
          ─► hourly + daily batch recount ─► reconcile vs streaming totals
          ─► billing table (published only after reconciliation) ─► invoicing
```

Decisions to say out loud:

1. **Two numbers with two bars**: the streaming count is fast and *provisional*; the batch
   recount over the immutable raw log is the *billing* number. Exactly-once in the stream
   is nice; reconciliation is what makes the bill defensible.
2. **Dedupe on `click_id`** (and fraud filters) before counting, with the dropped clicks
   kept for audit.
3. **Upsert keyed by `(ad_id, minute)`** so late clicks revise the minute instead of adding
   rows.
4. **Hot ads**: a viral ad saturates one key — pre-aggregate per partition, or salt
   `ad_id#n` and merge.
5. **Aggregate at one-minute grain**, roll up to hour and day in the store; queries for a
   campaign sum minutes rather than scanning clicks.

Follow-ups: *reconciliation finds a 0.5% mismatch* → the batch number wins for billing,
alert, and investigate (late clicks past the allowed lateness are the usual cause); *the Flink
job is down for an hour* → it restores from the checkpoint and replays from Kafka offsets; the
dashboard is stale, the bill is unaffected.

### 6-3) Top-K trending

**Prompt**: "Show the top 100 trending hashtags (or songs, or products) in the last hour,
updated every minute."

**Clarify**: exact or approximate (approximate is almost always fine), window (sliding 1 h
vs tumbling), global or per region, how many distinct items (millions).

```text
 events ─► Kafka (key = item_id)
   ─► stage 1: per-partition count per (item, 1-min bucket)      ← parallel, keyed
   ─► stage 2: merge buckets into a sliding 60-min count per item
   ─► stage 3: keep a top-K heap per slide; emit top 100         ← one small operator
   ─► KV store "trending:{region}" (overwritten each minute) ─► API / cache
```

Decisions to say out loud:

1. **Two-stage aggregation**: count in parallel by key, then a single small operator merges
   only the candidates — never send every event to one node.
2. **Sliding window as 60 one-minute buckets**: add the newest bucket and subtract the oldest,
   instead of recounting an hour every minute.
3. **Memory**: with millions of items, exact counts may be too large. Keep a **Count-Min
   Sketch per one-minute bucket** for approximate counts in fixed memory (it only ever
   over-counts). A sketch cannot list its items, so candidates need their own structure, and
   **each minute's top-N is the wrong one**: a tag with 20 events in every minute can lose
   every minute to a different 21-event tag and still win the hour, without ever appearing in
   a minute's list. Use a summary with a coverage guarantee instead: a **Misra-Gries summary
   of k counters per minute**. These summaries are mergeable, and the merge of the 60 that are
   live keeps the guarantee — every item with more than W / (k + 1) of the hour's W events
   is in it. Re-score those candidates against the live sketches and take the top 100. An item
   below that share can still be missed, so choose k for it (k = 10,000 covers anything over
   0.01% of the hour); the official ranking comes from an exact hourly batch recount. When a
   bucket expires, its sketch and its summary leave together, so a leader with no new events
   falls out instead of staying stuck in a heap.
4. **"Trending" is not "most popular"**: rank by velocity against a baseline (this hour vs
   the same hour last week), or the top list never changes.

A Count-Min Sketch, the structure interviewers ask to see:

```python
import hashlib


class CountMinSketch(object):
    # IDEA: d hash rows of w counters; an item's estimate is the min over its d counters,
    #       which can only over-count (collisions add, never subtract).
    # time = O(d) per add / query, space = O(w * d) regardless of how many items
    def __init__(self, width, depth):
        self.width = width
        self.depth = depth
        self.table = [[0] * width for _ in range(depth)]

    def _index(self, item, row):
        digest = hashlib.sha256(("%d:%s" % (row, item)).encode()).hexdigest()
        return int(digest, 16) % self.width

    def add(self, item, count=1):
        for row in range(self.depth):
            self.table[row][self._index(item, row)] += count

    def estimate(self, item):
        estimates = []
        for row in range(self.depth):
            estimates.append(self.table[row][self._index(item, row)])
        return min(estimates)


cms = CountMinSketch(width=1000, depth=5)
for tag in ["#ai"] * 50 + ["#nba"] * 20 + ["#rain"] * 3:
    cms.add(tag)
print(cms.estimate("#ai"), cms.estimate("#nba"), cms.estimate("#rain"))  # 50 20 3 (≥ true)
```

Sizing: width ≈ e / ε and depth ≈ ln(1 / δ) give an error of at most ε × (total count) with
probability 1 − δ.

### 6-4) Distinct counts at scale (DAU, unique visitors)

**Prompt**: "Unique visitors per page per day, and for any date range the user picks."

**Clarify**: exact or approximate, which ranges (day, week, arbitrary), cardinality (billions
of visitor-page pairs).

**The answer to lead with**: **distinct counts do not add** — daily uniques cannot be summed
into weekly uniques — so either keep the raw IDs (exact, expensive) or keep a **mergeable
sketch** per day and merge sketches for any range.

| Approach | Exact? | Storage | Range query |
|---|---|---|---|
| `COUNT(DISTINCT user_id)` over raw events | yes | all events | rescan the range |
| Per-day set of IDs | yes | grows with users | union sets — heavy |
| **HyperLogLog per (page, day)** | ~0.8% error | ~12 KB each, fixed | merge the sketches — fast |
| Bitmap (Roaring) per (page, day) on dense integer IDs | yes | compact for dense IDs | OR the bitmaps |

```sql
-- BigQuery: store a sketch per page per day, merge for any range
CREATE OR REPLACE TABLE gold.page_uv_sketch AS
SELECT page_id, DATE(event_ts) AS dt, HLL_COUNT.INIT(user_id) AS uv_sketch
FROM silver.page_views
GROUP BY page_id, dt;

SELECT page_id, HLL_COUNT.MERGE(uv_sketch) AS uv_approx
FROM gold.page_uv_sketch
WHERE dt BETWEEN '2026-11-01' AND '2026-11-17'
GROUP BY page_id;
```

Follow-up: *finance needs exact* → exact counts from raw in a nightly batch for the few
official numbers; sketches for everything interactive.

### 6-5) Feature store with point-in-time correctness

**Prompt**: "ML teams compute the same features in notebooks and in production and they
disagree. Design a feature platform."

**Clarify**: online latency (single-digit ms?), freshness of features (daily vs streaming),
how many models and entities, training data volume.

```text
 sources ─► batch features (Spark/dbt, daily)    ┐
         ─► stream features (Flink, e.g. txn_count_10m) ┤
                                                  ├─► OFFLINE store: full history, (entity, feature_ts)
                                                  │      └─► point-in-time join ─► training sets
                                                  └─► ONLINE store: latest value per entity (KV)
                                                         └─► model server, < 10 ms
   feature registry: one definition per feature, owner, freshness SLA, lineage
```

Decisions to say out loud:

1. **One definition, two stores**: the same feature code writes both the offline history and
   the online latest value, which is what kills train/serve skew.
2. **Point-in-time correctness**: a training row at time T may only see feature values
   computed **at or before T** — otherwise the model trains on the future (leakage) and is
   great offline, poor in production.
3. **Online store is a KV**, keyed by entity, with the freshness of each feature monitored.

```sql
-- point-in-time join: for each label, the latest feature value at or before the label time
SELECT label_id, user_id, label_ts, label, txn_count_7d
FROM (
  SELECT l.label_id, l.user_id, l.label_ts, l.label, f.txn_count_7d,
         ROW_NUMBER() OVER (
           PARTITION BY l.label_id
           ORDER BY f.feature_ts DESC
         ) AS rn
  FROM labels l
  LEFT JOIN user_features f
    ON f.user_id = l.user_id
   AND f.feature_ts <= l.label_ts   -- never a value from the future
) t
WHERE rn = 1;
```

### 6-6) Real-time fraud detection

**Prompt**: "Score every card transaction for fraud before it is approved."

**Clarify**: the latency budget (the approval path allows tens of ms), rules vs ML, what
features need history (velocity: transactions in the last 10 min per card), what happens if
scoring is down.

```text
 payment service ──sync──► scoring service (rules + model, p99 < 50 ms)
                               ▲ reads features
                     online KV (card velocity, device history, merchant risk)
                               ▲ writes features continuously
 payment events ─► Kafka ─► Flink (keyed by card_id: counts / sums over 10 m, 1 h, 24 h)
                         └─► lake ─► offline features + labels (chargebacks, weeks later) ─► retrain
```

Decisions to say out loud:

1. **The decision is synchronous, the features are asynchronous** — the stream job keeps
   per-card aggregates warm in a KV, so scoring is one lookup, not a computation.
2. **Fail open or fail closed** is a business decision: if scoring times out, approve below an
   amount and decline or step-up above it.
3. **Labels arrive weeks later** (chargebacks), so training joins features to labels
   point-in-time (§6-5).
4. **Feedback loop**: blocked transactions never get a label — keep a small holdout or
   reviewed sample so the model can still learn.

### 6-7) A company data platform (lakehouse)

**Prompt**: "We have 200 Postgres databases, Salesforce, event streams and a mess of cron
scripts. Design our data platform."

**Clarify**: consumers (BI, ML, finance, product), team size, cloud, compliance (PII, data
residency), what hurts most today.

```text
 sources: Postgres ×200 ─► Debezium/Kafka Connect ─┐
          SaaS (Salesforce) ─► managed connectors ─┼─► S3 bronze (Iceberg, append-only)
          events ─► Kafka ────────────────────────┘        │
                                                  dbt / Spark ─► silver (conformed, deduped, typed)
                                                           ─► gold (marts per domain: finance, product…)
 query: Trino / Athena + warehouse for BI; Spark for ML; one catalog for every engine
 control plane: Airflow · catalog + lineage · data contracts in CI · quality gates ·
                column-level access + PII tags · cost per team
```

Decisions to say out loud:

1. **One open table format and one catalog**, so every engine reads the same tables — no copy
   per tool.
2. **Medallion layers with ownership**: the platform owns bronze, domain teams own their gold
   marts (a light data-mesh model), and contracts guard the boundary.
3. **Self-serve with guardrails**: templates for a new source and a new mart, so 200 databases
   do not mean 200 bespoke pipelines.
4. **Governance by default**: PII tagged at ingestion, masked in silver, deletes propagated by
   key.
5. **Phase it**: the most painful consumer first (usually finance reporting), then migrate cron
   scripts one by one, each with a reconciliation against the old output.

### 6-8) Migrating an on-prem Hadoop stack to the cloud

**Prompt**: "Move 2 PB of HDFS, 3,000 Hive / Spark jobs and their consumers to the cloud
without breaking the business."

**Decisions to say out loud**:

1. **Inventory and lineage first**: which jobs feed which consumers; many jobs turn out dead.
2. **Storage first, compute second**: copy data to object storage (bulk transfer + incremental
   sync), register it in a catalog, point new compute at it.
3. **Migrate by consumer-facing domain, not by job**, and **dual-run** each domain: old and
   new pipelines in parallel, outputs compared automatically, cut over when they match for N
   days.
4. **Re-platform, then optimise**: lift Spark/Hive SQL as-is onto managed Spark first; convert
   to a table format, tune partitioning and file sizes after the cut-over, not during.
5. **Rollback plan per domain** and a freeze on new features in the old stack.

Follow-ups: *HDFS small files* → compact during the copy; *Hive `INSERT OVERWRITE` semantics
on object storage* → a table format makes the overwrite atomic, which plain S3 directories are
not. The Hadoop side in depth: [`./hadoop.md`](./hadoop.md).

### 6-9) Deduplication in a batch pipeline

**Prompt**: "We land ~1 TB/day of order data from several sources — an orders API, database
CDC and partner files — into S3, and load it into Redshift. Design how duplicates are handled."

**Clarify**: what counts as a duplicate (the same event delivered twice, or two versions of the
same order?), which column means "latest" (`updated_at` from the source, a CDC sequence number,
or arrival time), how late a record can arrive, whether the sources share an ID for the same
business event, and whether consumers want every change or only the current state.

**Numbers**: 1 TB/day at ~1 KB/row is ~1 B rows/day, ~12 MB/s on average. A `ROW_NUMBER()`
over one day is one shuffle of ~1 TB — fine on a mid-size Spark cluster. A dedupe that rescans
the whole history every day is not: a year of it is 365 TB per run.

**The answer to lead with**: **a duplicate is not one thing.** The same order can show up twice
because one event was delivered twice, or because the order really changed twice. Those are two
problems with two keys, so the design dedupes in **two layers**, and it also stops the
pipeline's own retries from adding a third kind.

```text
 order 1001 as it arrives
 event_id  order_id  amount  updated_at  ingest_ts
 e1        1001      100     10:00       10:01     ┐ same event delivered twice
 e1        1001      100     10:00       10:07     ┘   → event-level dedupe keeps one
 e2        1001      150     10:05       10:06     ┐ a real update to the same order
 e3        1001      180     10:10       10:11     ┘   → entity-level dedupe keeps e3 as current
```

| Column | Means | Used for |
|---|---|---|
| `event_id` | one business event, assigned at the source | event-level dedupe |
| `order_id` | the business key | entity-level dedupe, `MERGE` key |
| `updated_at` (or CDC `lsn`) | when the source changed the row | picks the latest version |
| `ingest_ts` | when the row reached us | tie-breaker only, never "latest" |
| `batch_id` / `run_id` | one run of the pipeline | making a rerun idempotent |

**Where duplicates come from**: source systems that emit twice, at-least-once delivery
retries, a job that wrote half its output and was retried, overlapping incremental pulls
(a lookback by design), late data that repeats rows already loaded, and two sources that both
send the same business event.

**The toolkit** — each method answers one of these, and none answers all of them:

| Method | Removes | Cost | Trap |
|---|---|---|---|
| `DISTINCT` / `dropDuplicates()` | rows identical in every column | one shuffle | `dropDuplicates(["order_id"])` keeps an **arbitrary** row, not the latest |
| `ROW_NUMBER()` over a key | all but one row per key, by a rule | shuffle + sort | no tie-breaker → a rerun can keep a different row |
| Hash of the columns | same content under different IDs | CPU per row | `concat_ws` skips NULLs, so `('a', NULL, 'b')` and `('a', 'b', NULL)` hash the same |
| Lookback window | duplicates and updates arriving up to N days late | rereads N days | anything later than N is missed silently |
| `MERGE` / upsert | a key already in the target | rewrites the files it touches | several source rows per key → error or a random winner |
| Batch ID + atomic write | a retried run's second copy | metadata, a transaction | check-then-write races with a concurrent run |
| Primary key / unique constraint | a second row per key, at write time | index maintenance | Redshift, Snowflake and BigQuery **accept** a primary key but **do not enforce** it |
| Partition-level dedupe | duplicates inside one partition | reads one partition | misses duplicates whose copies sit in different partitions |

```text
 orders API / CDC / partner files
   ─► ingestion: stamp source_id, ingest_ts, batch_id; keep or derive event_id
   ─► S3 bronze (immutable, partitioned by ingest date) + batch manifest
   ─► Spark, daily, over bronze rows ingested in the last N days:
        1. validate schema; NULL or malformed keys → quarantine (never into a dedupe group)
        2. normalise keys, timestamps, decimals, string case
        3. event-level:  ROW_NUMBER() OVER (PARTITION BY event_id ORDER BY ingest_ts)        = 1
        4. entity-level: ROW_NUMBER() OVER (PARTITION BY order_id
                                            ORDER BY updated_at DESC, lsn DESC, event_id DESC) = 1
   ─► silver.order_events  (one row per event; event_date partitions rewritten)
   ─► silver.orders        (one row per order; MERGE, newer version wins)
   ─► Redshift gold: load staging table, then DELETE + INSERT by key in one transaction
   ─► checks before publish: key uniqueness, input vs output counts, duplicate rate
```

Decisions to say out loud:

1. **Event-level dedupe on `event_id`, partitioned by event date.** Every copy of an event
   carries the same event time, so all of them fall into the **same** `event_date` partition.
   That makes "rewrite every event date this run touched, deduped" complete for this layer, as
   long as the copies arrive within the lookback: union the existing partition with the new
   arrivals, keep one row per `event_id`, overwrite the partition. Partition by ingest date
   instead and two copies of one event land in two partitions that never meet.

   ```sql
   -- Iceberg / Delta (Spark SQL), dynamic partition overwrite: rewrites only the
   -- event_date partitions this run's arrivals touch. A table format is what lets the
   -- statement read the partitions it overwrites; plain Parquet on S3 refuses.
   INSERT OVERWRITE TABLE silver.order_events PARTITION (event_date)
   SELECT event_id, order_id, amount, status, updated_at, ingest_ts, event_date
   FROM (
     SELECT *,
            ROW_NUMBER() OVER (PARTITION BY event_id ORDER BY ingest_ts) AS rn
     FROM (
       SELECT * FROM silver.order_events
       WHERE event_date IN (SELECT DISTINCT event_date FROM staged_arrivals)
       UNION ALL
       SELECT * FROM staged_arrivals
     ) AS candidates
   ) AS ranked
   WHERE rn = 1;
   ```

2. **Entity-level dedupe on `order_id` is a `MERGE`, never a partition overwrite.** Versions
   of one order have different `updated_at` values, so they land in different partitions, and
   the partition key is not the dedupe key. Keep one row per order in the batch first, then
   let the newer version win:

   ```sql
   -- Delta Lake / Iceberg (Spark SQL)
   MERGE INTO silver.orders AS t
   USING (
     SELECT *
     FROM (
       SELECT *,
              ROW_NUMBER() OVER (PARTITION BY order_id
                                 ORDER BY updated_at DESC, lsn DESC, event_id DESC) AS rn
       FROM staged_arrivals
     ) AS ranked
     WHERE rn = 1
   ) AS s
   ON t.order_id = s.order_id
   WHEN MATCHED AND (s.updated_at > t.updated_at
                     OR (s.updated_at = t.updated_at AND s.lsn > t.lsn)) THEN UPDATE SET
     t.amount     = s.amount,
     t.status     = s.status,
     t.updated_at = s.updated_at,
     t.lsn        = s.lsn
   WHEN NOT MATCHED THEN INSERT (order_id, amount, status, updated_at, lsn)
     VALUES (s.order_id, s.amount, s.status, s.updated_at, s.lsn);
   ```

   The guard on `WHEN MATCHED` is what makes the `MERGE` safe to rerun **and** safe against a
   late, older version: replaying the batch changes nothing, and a version that arrives after a
   newer one is ignored instead of rolling the order back.

3. **A deterministic order.** `ORDER BY updated_at DESC` alone leaves ties to chance, so a rerun
   can pick a different row and the output stops being reproducible. Break ties with a source
   sequence (`lsn`), then a stable ID. `ingest_ts` is a tie-breaker of last resort, never the
   meaning of "latest": a retry arrives later than the original without being newer.

4. **NULL keys are quarantined, not deduped.** `PARTITION BY order_id` puts every NULL into one
   group, so a million unrelated rows collapse into one — a silent data loss, and the same group
   is the skewed task that runs for an hour. Route them out at validation, with a count.

5. **The hash is for content, not identity.** When two sources send the same business event
   under different IDs, hash the normalised business columns and dedupe on that. Normalise
   first (trim, case, timezone, decimal scale), and make NULL explicit
   (`coalesce(col, '\u0000')`), or equal records hash differently and different records hash
   the same. SHA-256 makes an accidental collision negligible; the normalisation is what
   actually goes wrong.

6. **The lookback is a contract with the sources.** Rereading the last 3 days catches anything
   up to 3 days late — and nothing older. Measure the real lateness distribution, set N above
   its tail, **count** the arrivals older than N instead of dropping them, and send those through
   a targeted reprocess of the partitions they belong to.

7. **A rerun must not be a fourth source of duplicates.** Make every write replace rather than
   append: partition overwrite for events, `MERGE` for entities, and for Redshift a staging
   load followed by `DELETE` + `INSERT` keyed on the batch or the business key **inside one
   transaction**, so a retried load replaces its first attempt instead of doubling it. A
   `processed_batches` table is only useful when its row is written in the same transaction as
   the data; "check the table, then write" races with a concurrent run.

   ```sql
   -- Redshift: a retried run of batch 2026-11-21 replaces itself
   BEGIN;
   DELETE FROM gold.orders
   USING staging.orders_20261121 AS s
   WHERE gold.orders.order_id = s.order_id;
   INSERT INTO gold.orders SELECT * FROM staging.orders_20261121;  -- staging: one row per order
   DELETE FROM ops.processed_batches WHERE batch_id = '2026-11-21';
   INSERT INTO ops.processed_batches (batch_id, processed_at)
   VALUES ('2026-11-21', GETDATE());
   END;
   ```

   Redshift will not reject a second row with the same primary key — the constraint is only a
   hint to the planner — so uniqueness has to come from the load pattern, and a check proves it.

8. **Publish only after the checks pass.** Write gold through a staging table or a table-format
   commit, then gate on: zero duplicate keys in the output, input count = output count +
   duplicates removed + quarantined, and a duplicate rate within its normal band. A rate that
   jumps from 0.3% to 30% is an upstream retry storm or a broken key, and the dedupe hiding it
   would be the bug.

| Metric | Why |
|---|---|
| Input / output row count | the reconciliation identity above |
| Duplicates removed, per layer | event-level and entity-level move for different reasons |
| Duplicate rate = (input − output) ÷ input | the trend that catches a retry storm |
| Arrivals older than the lookback | whether N is still long enough |
| Quarantined rows | NULL keys, malformed records |

Follow-ups:

- *The dataset grows to 10 TB/day* → keep the dedupe incremental (only touched partitions),
  bucket or cluster silver by `order_id` so the `MERGE` rewrites fewer files, prune the target
  with a partition predicate in the `ON` clause, and consider merge-on-read for the
  high-update table (cheaper writes, compaction later) against copy-on-write (cheaper reads).
- *One key has millions of rows* (a test account, a default value) → it is usually a bad key,
  not a real entity: quarantine it. If it is real, pre-aggregate per partition or salt the key
  for the event-level step, and let AQE split the skewed partition.
- *Two jobs process the same key at once* → serialise writers per table in the orchestrator;
  Delta and Iceberg reject the second of two conflicting commits, which the job then retries
  against the new snapshot.
- *How do you prove it is correct?* → a uniqueness test on the output key, a count
  reconciliation per run, and a periodic full-history uniqueness scan to catch what the lookback
  missed.
- *Records are updates, not duplicates* → that is the entity layer: keep every version in
  `silver.order_events` (history, SCD2 if needed) and only the latest in `silver.orders`.

The building blocks in depth: incremental pulls and their overlap window in
[`./batch_pipeline.md`](./batch_pipeline.md#2-2-incremental-loads-done-right), `MERGE` in
[`./batch_pipeline.md`](./batch_pipeline.md#3-5-merge--upsert-patterns), and the streaming
counterpart — dedupe with state and a TTL — in §6-1.

---

## 7) Failure Modes and Operations ⭐⭐⭐⭐

### 7-1) What breaks, and the design answer

**Name the failure before the interviewer does, and give the design's answer to it — that is
the most reliable senior signal in the round.**

| Failure | Symptom | Design answer |
|---|---|---|
| Upstream schema change | job fails, or worse, nulls flow through | registry compatibility, contracts in CI, quarantine |
| Duplicate events (retries, replays) | inflated counts | `event_id` dedupe, idempotent upsert / overwrite |
| Late or out-of-order data | yesterday's number moves | event time, watermark, reprocess window, reconciliation |
| Hot key / skew | one task or partition lags | salting, two-stage aggregation, split the key |
| Partial load | a day with 60% of rows, green job | volume checks block publish |
| Stream job crash | lag grows, dashboard stale | checkpoints, restart from offsets, lag alerting |
| Source DB overloaded by extraction | production incident | CDC from a replica / the log, rate limits |
| Small files | slow queries, high request costs | compaction, target file size, coarser partitions |
| Bad deploy writes wrong data | wrong numbers downstream | raw layer + backfill, time travel to roll back, shadow runs |
| Cost runaway | bill doubles | partition pruning, lifecycle / TTL, per-team cost attribution |

### 7-2) SLAs and what to monitor

**Define the SLA from the consumer's side — "the finance mart is complete and correct by
06:00" — and monitor the data against it, not just the jobs.**

| Layer | Monitor | Alert when |
|---|---|---|
| Ingestion | consumer lag, connector status, DLQ rate | lag > freshness budget; DLQ spikes |
| Stream jobs | checkpoint duration / failures, watermark delay, backpressure | checkpoints failing; watermark stalls |
| Batch jobs | duration vs history, retries, SLA miss prediction | will miss the deadline at current pace |
| Data | freshness, volume, nulls, uniqueness, reconciliation | any blocking check fails |
| Cost | spend per pipeline / team, bytes scanned | above budget or trend |

Operations runbooks: [`./batch_pipeline.md`](./batch_pipeline.md#9-failure-handling-and-reruns-)
and [`./stream_pipeline.md`](./stream_pipeline.md#12-operations-).

---

## 8) Senior Signals and Pitfalls ⭐⭐⭐⭐

### 8-1) What a senior answer sounds like

**A senior answer is shorter on tools and longer on why: it states the requirement each choice
serves, the option it rejected, and what would make it change its mind.**

- *"I'd start with hourly batch; if the 15-minute SLA is firm, I'd move only the dashboard
  path to streaming and keep the rest batch."*
- *"Delivery is at-least-once, so the sink is an upsert on `(ad_id, minute)` — duplicates
  overwrite themselves."*
- *"Raw is immutable, so a logic bug is a backfill, not a data loss."*
- *"Billing reads the reconciled batch number; the streaming one is labelled provisional."*
- *"If one key is 30% of traffic, I'd salt it and merge in a second stage."*
- *"The thing I'd watch in production is watermark delay, because that's what makes the
  dashboard silently stale."*

### 8-2) Pitfalls that sink the round

- ❌ Drawing the architecture before asking about freshness, volume and consumers.
- ❌ Streaming everything because it sounds modern, with no requirement that needs it.
- ❌ Assuming events arrive once, in order and on time.
- ❌ No answer for "how do you backfill this?" or "what if this job ran twice?".
- ❌ One store for every reader — a warehouse behind a 10 k QPS product API, or Kafka as the
  long-term archive.
- ❌ Estimates that never turn into a partition count, a cluster size or a cost.
- ❌ Ignoring data quality, PII and deletes until the interviewer brings them up.
- ❌ Listing tools without saying what each one is for.

---

## 9) Last-Minute Checklist

One line per idea — if you can say each of these out loud, the round is covered.

- **Plan** — clarify (freshness, volume, consumers, correctness) → estimate → layers → deep dive on correctness → failure + scale → trade-offs
- **Numbers** — 10⁵ s/day, peak 2–5×, ~1 KB/event, Parquet ~5×, partitions = peak MB/s ÷ ~5–10 MB/s
- **Architecture** — sources → ingest → immutable raw → silver → gold → serving store per reader, control plane across
- **Correctness** — at-least-once + idempotent sink, `event_id` dedupe, event time + watermark, revisable keyed results
- **Backfill** — deterministic jobs on a fixed interval, overwrite by logical date, replay from raw
- **Consistency** — CDC or outbox, never dual write
- **Trade-offs** — batch vs stream, Lambda vs Kappa, ELT + lakehouse, precompute vs query time, CDC vs query pull, freshness / correctness / cost
- **Classics** — clickstream, ad clicks for billing (stream provisional + batch reconciled), top-K (two-stage + Count-Min), distinct counts (HLL merge), feature store (point-in-time), fraud (sync score + async features), platform, migration (dual-run), batch dedupe (`event_id` then business key, guarded `MERGE`)
- **Operations** — data-side SLAs, lag / watermark / checkpoint / freshness / volume alerts, quarantine, cost per team
