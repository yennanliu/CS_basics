# Batch Data Pipelines

> **Scope** — everything a data engineer interview asks about batch pipelines: the reference architecture, ETL vs ELT, lake vs warehouse vs lakehouse, ingestion patterns, idempotency and backfills, late data, `MERGE`, orchestration with Airflow, dbt, data quality, file and table formats, cost, failure handling, PII, and a worked design question. Table and partition design lives in the schema design doc; streaming lives in the stream pipeline doc.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`./schema_design.md`](./schema_design.md), [`./data_modeling.md`](./data_modeling.md), [`./spark.md`](./spark.md), [`./stream_pipeline.md`](./stream_pipeline.md), [`./sql_for_data.md`](./sql_for_data.md); the older [`../faq_Airflow.md`](../faq_Airflow.md) notes.

Every answer leads with the sentence you would say out loud, then the mechanism, then the
trade-off an interviewer pushes on next.

---

## 1) The Reference Architecture ⭐⭐⭐⭐⭐

### 1-1) The five stages of a batch pipeline

**A batch pipeline moves bounded chunks of data on a schedule through five stages: ingest
from sources, land the raw copy, clean and conform it, model it for consumers, and serve it
— with orchestration, quality checks and metadata running across all five.**

```text
 Sources            Ingest             Storage layers                       Serve
┌──────────┐     ┌──────────┐     ┌────────┐   ┌─────────┐   ┌────────┐   ┌───────────┐
│ OLTP DBs │──┐  │ full /   │     │ Bronze │   │ Silver  │   │ Gold   │   │ BI / SQL  │
│ SaaS APIs│──┼─►│ incr. /  │────►│ raw,   │──►│ cleaned │──►│ marts, │──►│ ML feats  │
│ files    │──┤  │ CDC /    │     │ as-is  │   │ deduped │   │ stars, │   │ reverse   │
│ events   │──┘  │ file drop│     │ append │   │ typed   │   │ aggs   │   │ ETL, APIs │
└──────────┘     └──────────┘     └────────┘   └─────────┘   └────────┘   └───────────┘
        └────── orchestration (Airflow / Dagster) · data quality · catalog & lineage ──────┘
```

| Stage | Owns | Typical tools |
|---|---|---|
| **Ingest** | getting the data out of the source without hurting it | Fivetran / Airbyte, Debezium, Spark JDBC, custom Python |
| **Land (raw)** | an immutable copy exactly as received | object storage (S3, GCS, ADLS), a raw schema in the warehouse |
| **Transform** | cleaning, deduping, typing, conforming, business logic | SQL + dbt, Spark |
| **Model** | facts, dimensions, aggregates for consumers | dbt, warehouse SQL |
| **Serve** | dashboards, extracts, feature tables, reverse ETL | BI tools, feature stores, Census / Hightouch |

The interview point: **keep the raw copy**. Every later layer can then be rebuilt from it,
which is what turns a transform bug into a rerun instead of a data loss.

### 1-2) ETL vs ELT

**ETL transforms in a separate engine before loading; ELT loads the raw data first and
transforms it inside the warehouse — the modern default because warehouse compute scales
on demand and raw data lets you reprocess.**

| | ETL | ELT |
|---|---|---|
| Transform runs in | a dedicated engine (Spark, Informatica, Python) | the warehouse / lakehouse (SQL, dbt) |
| Raw data in the target | usually not | yes |
| Reprocessing | re-extract from the source | rerun SQL over the raw layer |
| Strength | heavy non-SQL logic; scrub PII before it lands | speed of change, analyst-owned SQL, lineage |
| Weakness | logic lives outside the warehouse, slower to change | raw sensitive data lands in the warehouse; compute cost |

Most real stacks are a mix: ELT for the bulk of analytics, an ETL step where the data must
be transformed before it is allowed to land (PII, very large unstructured inputs).

### 1-3) Data lake vs warehouse vs lakehouse

**A warehouse is managed, schema-on-write SQL storage; a lake is cheap files in object
storage read schema-on-read; a lakehouse is a lake with a table format on top that adds
ACID transactions, schema enforcement and fast SQL.**

| | Data warehouse | Data lake | Lakehouse |
|---|---|---|---|
| Storage | proprietary, managed | open files in object storage | open files + table format metadata |
| Schema | on write | on read | on write, with evolution |
| Transactions | yes | no (a directory of files) | yes (Iceberg, Delta, Hudi) |
| Data types | structured, semi-structured | anything | anything |
| Engines | the warehouse's own | many (Spark, Trino, Presto) | many, on the same tables |
| Examples | Snowflake, BigQuery, Redshift | S3 + Hive tables | Databricks Delta, Iceberg on S3 with Trino / Spark |

The failure mode of a plain lake is the **data swamp**: no transactions, so a half-written
job is visible to readers, and no schema, so nobody trusts the files.

### 1-4) The medallion layers and what each owns

**Bronze is the raw copy, silver is the cleaned and conformed truth, gold is shaped for one
use — and each layer has a different owner and a different rule about what may change.**

| Layer | Contains | Rules |
|---|---|---|
| **Bronze / raw** | source records as received, plus ingestion metadata (`_ingested_at`, `_source`, `_batch_id`) | append-only; never edited; retained long enough to rebuild silver |
| **Silver / cleaned** | deduped, typed, validated, conformed entities and events | one row per entity version or event; quality-gated; PII handled |
| **Gold / marts** | facts, dimensions, aggregates, feature tables | modeled per consumer; business definitions live here |

Same idea, older names: staging → integration → presentation. The names matter less than
the contract: **each layer is rebuildable from the one below**.

---

## 2) Ingestion Patterns ⭐⭐⭐⭐⭐

### 2-1) Full, incremental, or CDC

**Full reload for small tables, incremental by a watermark column when the source has a
reliable `updated_at`, and log-based CDC when you need deletes, low source load, or
near-real-time.**

| Pattern | How | Catches deletes | Source load | Watch out for |
|---|---|---|---|---|
| **Full snapshot** | `SELECT *` each run, replace the target | yes (by absence) | high | only viable for small or slow-changing tables |
| **Incremental (watermark)** | `WHERE updated_at > :last_watermark` | no | low–medium | needs an indexed, reliable `updated_at` |
| **Append-only** | `WHERE id > :last_id` | n/a | low | only for immutable rows (logs, events) |
| **Log-based CDC** | read the binlog / WAL (Debezium, DMS, Datastream) | yes | lowest | more infrastructure; schema changes flow through |
| **Snapshot diff** | compare today's full extract with yesterday's | yes | high | expensive; a fallback when nothing else works |

### 2-2) Incremental loads done right

**Pull by a watermark with an overlap window, then dedupe on the primary key — because
rows commit "in the past" and the exact watermark boundary will drop some of them.**

- Store the watermark (the max `updated_at` actually loaded) in a state table, not in a
  variable that resets on rerun.
- **Overlap**: query `updated_at > watermark - overlap`. A transaction that started before
  the last run but committed after it carries an `updated_at` older than the watermark.
- **Dedupe** in silver: latest version per key by `updated_at` (and a tiebreaker such as a
  sequence number or the CDC log position).
- Clock skew between application servers makes `updated_at` approximate; the overlap
  absorbs it.
- Hard deletes never show up in an `updated_at` query. Either get soft-delete flags from the
  source team or use CDC.

```sql
-- PostgreSQL source extract: incremental with a 10-minute overlap
SELECT *
FROM   orders
WHERE  updated_at >  :last_watermark - INTERVAL '10 minutes'
  AND  updated_at <= :run_upper_bound          -- fixed upper bound: reruns read the same rows
ORDER  BY updated_at;
```

The fixed upper bound matters: without it, a rerun at a later time reads more rows than the
original run, and the job is no longer deterministic.

### 2-3) Change data capture with Debezium

**Debezium reads the database's replication log and emits one event per row change —
insert, update, delete — into Kafka, so the warehouse can replay the source's history
without querying its tables.**

```text
MySQL binlog / Postgres WAL ──► Debezium (Kafka Connect source) ──► Kafka topic per table
                                                                        │
                                       sink: Spark / Flink / Kafka Connect ─► bronze (append)
                                                                        │
                                                         MERGE into silver (latest per key)
```

- Each event carries `before`, `after`, `op` (`c`, `u`, `d`, `r` for snapshot reads) and
  source metadata such as the log position and the transaction id.
- An **initial snapshot** loads existing rows, then streaming continues from the log
  position the snapshot recorded, so nothing is missed in between.
- Key the topic by primary key so all changes to a row stay in order in one partition.
- Deletes arrive as a `d` event, followed by a tombstone (`null` value) for compacted topics.
- Trade-offs: needs log access (binlog in `ROW` format, a Postgres replication slot), and an
  abandoned replication slot makes Postgres retain WAL until the disk fills.

### 2-4) Files and APIs

**File drops need a landing contract — naming, completeness signal, schema — and API pulls
need pagination, rate-limit handling and a checkpoint so a failure resumes rather than
restarts.**

Files (SFTP, partner S3 buckets):

- Agree on a naming convention with a date and a sequence number, and a **completeness
  signal** — a `_SUCCESS` / manifest file, or a row count in a trailer — so you never load a
  half-uploaded file.
- Copy to your own raw area immediately and process from there; the partner's bucket is not
  your archive.
- Record each file you loaded (name, checksum, row count) so a re-delivered file is detected.

APIs (SaaS sources):

- **Pagination**: prefer cursor-based (`next_cursor`) over offset-based, because offsets skip
  or repeat records when data changes during the pull.
- **Rate limits**: respect `429` and `Retry-After`, back off exponentially with jitter.
- **Incremental**: filter by an `updated_since` parameter where the API offers one.
- **Checkpoint** the last cursor so a failure resumes from it.

```python
# python — cursor-paginated pull with retry on 429 / 5xx (requests 2.x)
import random
import time

import requests


def fetch_all(url, params, max_retries=5):
    cursor = None
    while True:
        query = dict(params, cursor=cursor) if cursor else dict(params)
        for attempt in range(max_retries):
            resp = requests.get(url, params=query, timeout=30)
            if resp.status_code == 429 or resp.status_code >= 500:
                retry_after = resp.headers.get("Retry-After")
                wait = float(retry_after) if retry_after else 2 ** attempt + random.random()
                time.sleep(wait)
                continue
            resp.raise_for_status()
            break
        else:
            raise RuntimeError(f"giving up after {max_retries} attempts: {url}")

        body = resp.json()
        yield from body["data"]
        cursor = body.get("next_cursor")
        if not cursor:
            return
```

---

## 3) Idempotency, Determinism and Backfills ⭐⭐⭐⭐⭐

### 3-1) What idempotent means for a pipeline

**A task is idempotent when running it once or ten times for the same logical date leaves
the target in the same state — the property that makes retries, reruns and backfills safe.**

Three ingredients:

1. **Deterministic input** — the task reads a fixed slice defined by its logical date / data
   interval, never "everything since `now()`".
2. **Replace, do not append** — the task owns its output slice and overwrites it
   (partition overwrite, `MERGE`, delete-then-insert in one transaction).
3. **No hidden side effects** — emails, API calls and counters are separate tasks with their
   own dedupe key.

| Write pattern | Idempotent? | Why |
|---|---|---|
| `INSERT INTO ... SELECT` | no | a rerun duplicates the rows |
| `INSERT OVERWRITE ... PARTITION (dt = ...)` | yes | the partition is replaced |
| `DELETE WHERE dt = ...` + `INSERT`, one transaction | yes | same effect, for engines without overwrite |
| `MERGE` on a natural key | yes | matched rows update, others insert |
| `CREATE OR REPLACE TABLE ... AS SELECT` | yes | the whole table is rebuilt |

```sql
-- Spark SQL / Hive: idempotent daily partition load
INSERT OVERWRITE TABLE silver.orders PARTITION (dt = '2026-11-01')
SELECT order_id, customer_id, status, amount, updated_at
FROM   bronze.orders_raw
WHERE  dt = '2026-11-01';
```

In Spark, set `spark.sql.sources.partitionOverwriteMode=dynamic` before a DataFrame
`mode("overwrite")` write with `partitionBy`, or it replaces **every** partition of the table,
not just the ones the job wrote.

### 3-2) Determinism traps

**Anything that reads the wall clock, a random number, or "the latest" version of an input
makes a rerun produce a different answer.**

- `CURRENT_DATE` / `now()` in a transform → use the run's logical date parameter.
- Reading the dimension "as of now" while backfilling last year → join on the version valid
  at the event time (SCD2), or snapshot the dimension per run.
- `ROW_NUMBER()` without a total order (ties on `updated_at`) → add a tiebreaker column.
- Unordered `LIMIT`, sampling without a seed, UUIDs generated in the transform for keys →
  derive keys from the data (a hash of the natural key).
- Source data that changes after the fact → read from the immutable bronze copy, not the live
  source, when reprocessing.

### 3-3) Backfills and reprocessing

**A backfill reruns the pipeline for past logical dates; it is safe only when every task is
idempotent and parameterised by its date, and it should be throttled so it does not starve
the daily run.**

- In Airflow 2.x: `airflow dags backfill -s 2026-01-01 -e 2026-03-31 my_dag` (Airflow 3 moved
  this to `airflow backfill create`), or clear past task
  instances and let the scheduler re-run them; `catchup=True` back-fills missed intervals
  automatically when a DAG is first deployed.
- Limit concurrency (`max_active_runs`, pools) so a 90-day backfill does not saturate the
  warehouse.
- Run oldest first when later partitions depend on earlier ones (running totals, SCD2).
- **Schema changes + backfill**: deploy the new code, backfill into a new table or
  partitions, validate counts and aggregates against the old output, then swap.
- Full rebuild vs incremental backfill: when the logic change touches every row (a new
  business definition), rebuilding the table from bronze is often simpler and safer than
  patching partitions.

### 3-4) Late-arriving data and late-arriving dimensions

**Late facts land in a partition that already ran, so either reprocess a trailing window
every day or partition by arrival date; late dimensions mean a fact arrives before its
dimension row, so point it at a placeholder and fix it when the dimension arrives.**

Late facts:

| Strategy | How | Trade-off |
|---|---|---|
| **Rolling reprocess window** | every run rebuilds the last N days of event-date partitions | simple, correct within N days; costs N× compute |
| **Partition by ingestion date** | load by arrival; consumers filter by event date | never rewrites; queries by event date scan more |
| **Incremental `MERGE`** | upsert late rows into the right event-date partition | efficient; needs a key and a table format with `MERGE` |

Late-arriving dimensions ("early facts"):

- The fact references `customer_id = 42`, but `dim_customer` has no row for 42 yet.
- Insert an **inferred member** — a placeholder dimension row with the natural key and
  `unknown` attributes — and point the fact at its surrogate key.
- When the real dimension row arrives, update the inferred row in place (Type 1 on the
  placeholder), so facts already pointing at it pick up the attributes.
- The alternative — a single `-1 Unknown` row — loses the link and needs the facts re-keyed later.

### 3-5) `MERGE` / upsert patterns

**`MERGE` matches incoming rows to the target on a key and decides per row whether to
update, insert or delete — the standard way to apply an incremental batch or a CDC batch
idempotently.**

```sql
-- Delta Lake / Databricks SQL:
-- apply one batch of CDC changes, latest change per key wins
MERGE INTO silver.customers AS t
USING (
  SELECT *
  FROM (
    SELECT *,
           ROW_NUMBER() OVER (PARTITION BY customer_id
                              ORDER BY change_lsn DESC) AS rn
    FROM   bronze.customers_cdc
    WHERE  batch_id = '2026-11-01'
  ) AS ranked
  WHERE rn = 1
) AS s
ON t.customer_id = s.customer_id
WHEN MATCHED AND s.op = 'd' THEN DELETE
WHEN MATCHED THEN UPDATE SET
  t.email      = s.email,
  t.status     = s.status,
  t.updated_at = s.updated_at
WHEN NOT MATCHED AND s.op <> 'd' THEN INSERT (customer_id, email, status, updated_at)
  VALUES (s.customer_id, s.email, s.status, s.updated_at);
```

- **Dedupe the source first**: `MERGE` fails (or is non-deterministic) when several source rows
  match the same target row.
- Restrict the target scan with a partition predicate in the `ON` clause where possible —
  an unrestricted `MERGE` rewrites files across the whole table.
- Snowflake and BigQuery have `MERGE` with the same shape, but details differ (BigQuery's
  `SET` takes unqualified column names); check the dialect.
- For SCD2, `MERGE` alone cannot both close the old row and insert the new version from one
  source row; use the staged-union trick or two statements in one transaction (see
  [`./data_modeling.md`](./data_modeling.md)).

---

## 4) Orchestration ⭐⭐⭐⭐⭐

### 4-1) What an orchestrator owns

**The orchestrator owns when tasks run, in what order, with what retries, and for which
logical date — it should not own the transformation logic or move the data itself.**

| Owns | Does not own |
|---|---|
| dependencies between tasks and between pipelines | the SQL / Spark code (call it, do not embed it) |
| schedules and data intervals | large data passing between tasks |
| retries, timeouts, alerting | business definitions |
| backfills and reruns | storage |
| concurrency limits on shared resources | |

### 4-2) Airflow concepts interviewers ask about

**An Airflow DAG is Python code that declares tasks and their dependencies; the scheduler
creates one DAG run per data interval and runs each task when its upstream tasks succeed.**

| Concept | What it is |
|---|---|
| **DAG** | the graph of tasks, with a schedule, start date and default args |
| **Operator** | a task template — `BashOperator`, `PythonOperator`, `SQLExecuteQueryOperator`, provider operators |
| **TaskFlow** | `@task`-decorated Python functions; return values pass through XCom automatically |
| **Sensor** | a task that waits for a condition (a file, a partition, another DAG); use `mode="reschedule"` or deferrable sensors so it does not hold a worker slot |
| **XCom** | small metadata passed between tasks — ids, paths, counts; **not** data sets |
| **Logical date / data interval** | the interval a run is *for*; a daily run for 2026-11-01 starts after that interval ends |
| **Catchup** | when `True`, the scheduler creates runs for every missed interval since `start_date` |
| **Retries** | `retries`, `retry_delay`, `retry_exponential_backoff` per task |
| **Pools** | a named slot limit shared across DAGs, to cap load on one database |
| **SLAs** | `sla=` per task triggers a miss callback in Airflow 2.x; removed in Airflow 3.0; deadline alerts are the successor |
| **Datasets / Assets** | data-aware scheduling: a DAG runs when an upstream task updates a dataset (Airflow 2.4+; renamed Assets in 3.0) |
| **Dynamic task mapping** | `.expand()` creates one task instance per element of a list at run time (Airflow 2.3+) |

The classic trap: a daily DAG's run for logical date `2026-11-01` covers the interval
`[2026-11-01, 2026-11-02)` and **starts after** it closes, on 2026-11-02. Queries should use
`data_interval_start` / `data_interval_end`, not the wall clock.

### 4-3) A small Airflow 2.x DAG

```python
# python — Airflow 2.4+ (TaskFlow API, dynamic task mapping)
from datetime import datetime, timedelta

from airflow.decorators import dag, task

TABLES = ["orders", "customers", "payments"]


@dag(
    schedule="@daily",
    start_date=datetime(2026, 1, 1),
    catchup=False,
    max_active_runs=1,
    default_args={
        "retries": 3,
        "retry_delay": timedelta(minutes=5),
        "retry_exponential_backoff": True,
    },
    tags=["ingest"],
)
def daily_ingest():

    @task
    def list_tables():
        return TABLES

    @task(pool="oltp_replica")          # cap concurrent queries against the source
    def extract(table, data_interval_start=None, data_interval_end=None):
        # Airflow injects the context values named in the signature
        start, end = data_interval_start.isoformat(), data_interval_end.isoformat()
        path = f"s3://lake/bronze/{table}/dt={data_interval_start.date()}/"
        # ... SELECT ... WHERE updated_at >= start AND updated_at < end, write to path ...
        return path

    @task
    def load_silver(paths):
        # ... MERGE each bronze path into silver ...
        return len(paths)

    @task
    def check_counts(n_tables):
        if n_tables != len(TABLES):
            raise ValueError(f"expected {len(TABLES)} tables, loaded {n_tables}")

    paths = extract.expand(table=list_tables())   # one mapped task per table
    check_counts(load_silver(paths))


daily_ingest()
```

What makes this production-shaped: each mapped `extract` reads a fixed data interval
(deterministic), writes to a date-partitioned path (idempotent on rerun), passes only paths
through XCom, and the source is protected by a pool.

### 4-4) Airflow vs Dagster vs Prefect

| | Airflow | Dagster | Prefect |
|---|---|---|---|
| Unit | task in a DAG | **software-defined asset** (the table it produces) | flow and task (plain Python) |
| Scheduling | time-based, plus datasets / assets | asset-driven, partitions first-class | time / event-based, dynamic at run time |
| Strength | ecosystem, providers, the default in most companies | lineage and partitions built in, local testing | low ceremony, dynamic workflows |
| Weakness | DAG-parsing overhead; data-awareness was added later | smaller ecosystem | smaller ecosystem for heavy data platforms |
| Managed | MWAA, Cloud Composer, Astronomer | Dagster+ | Prefect Cloud |

A good interview answer names the trade-off: Airflow if the team already runs it and the
work is task-shaped; Dagster when you want the orchestrator to understand tables and
partitions.

---

## 5) dbt ⭐⭐⭐⭐

### 5-1) What dbt does

**dbt turns SQL `SELECT` statements into tables and views in the warehouse, in dependency
order, with tests and documentation — it is the "T" of ELT and does not move data in or out.**

- A **model** is one `.sql` file containing a `SELECT`; `{{ ref('stg_orders') }}` references
  another model and builds the dependency graph.
- `{{ source('app', 'orders') }}` declares raw tables, so freshness can be checked against them.
- `dbt build` runs models, tests, seeds and snapshots in DAG order.
- Lineage and docs come from the graph: `dbt docs generate`.

### 5-2) Materializations

| Materialization | Builds | Use for |
|---|---|---|
| `view` | a view | light staging models, always fresh |
| `table` | a table rebuilt every run | small-to-medium marts |
| `incremental` | inserts / merges only new rows | large fact tables |
| `ephemeral` | a CTE inlined into downstream models | reusable logic without a table |
| `materialized_view` | a warehouse materialized view (adapter-dependent) | low-latency aggregates |

```sql
-- models/marts/fct_orders.sql — dbt incremental model (dbt-core 1.x)
{{ config(
    materialized = 'incremental',
    unique_key   = 'order_id',
    incremental_strategy = 'merge'
) }}

SELECT order_id,
       customer_id,
       status,
       amount,
       updated_at
FROM   {{ ref('stg_orders') }}

{% if is_incremental() %}
  -- only rows changed since the last load, with a 1-day overlap for late updates
WHERE  updated_at > (SELECT MAX(updated_at) - INTERVAL '1 day' FROM {{ this }})
{% endif %}
```

`INTERVAL '1 day'` is PostgreSQL / Snowflake syntax; on BigQuery write
`TIMESTAMP_SUB(MAX(updated_at), INTERVAL 1 DAY)`. A schema change in an incremental model
needs `on_schema_change` or a `--full-refresh`.

### 5-3) Tests, snapshots and contracts

- **Generic tests** in YAML: `unique`, `not_null`, `accepted_values`, `relationships`.
- **Singular tests**: a SQL file that returns the failing rows; zero rows means pass.
- Packages such as `dbt_utils` and `dbt_expectations` add row-count, recency and range tests.
- **Snapshots** implement SCD Type 2 over a source table (`timestamp` or `check` strategy),
  adding `dbt_valid_from` / `dbt_valid_to`.
- **Model contracts** (`contract: {enforced: true}`) make the build fail if a model's
  columns or types drift from the declared schema.
- `dbt source freshness` alerts when a source's `loaded_at_field` is older than a threshold.

```yaml
# models/marts/schema.yml
version: 2
models:
  - name: fct_orders
    columns:
      - name: order_id
        tests: [unique, not_null]
      - name: status
        tests:
          - accepted_values:
              values: ['placed', 'paid', 'shipped', 'cancelled']
      - name: customer_id
        tests:
          - relationships:
              to: ref('dim_customer')
              field: customer_id
```

---

## 6) Data Quality and Observability ⭐⭐⭐⭐⭐

### 6-1) The checks and where they run

**Check data at every layer boundary and stop the pipeline before bad data reaches a
consumer — a failed check that halts a load is cheaper than a wrong number in a board deck.**

| Dimension | Check | Example |
|---|---|---|
| **Schema** | columns and types as expected | a source renamed `amount` to `total` |
| **Completeness** | not-null keys; row count vs history | today's orders are 40% below the 4-week same-weekday average |
| **Uniqueness** | primary key has no duplicates | a retried ingest doubled a partition |
| **Validity** | ranges, enums, formats | `amount < 0`, an unknown `status` |
| **Consistency** | referential integrity, cross-table totals | facts pointing at missing dimension keys |
| **Freshness** | latest partition / `max(updated_at)` within the SLA | the upstream export did not land |
| **Accuracy** | reconciliation with the source of truth | daily revenue vs the payments system |

Where they run:

- **Ingest**: schema, row counts, file completeness — fail fast.
- **Silver**: uniqueness, nulls, validity — quarantine bad rows.
- **Gold**: business rules, reconciliation, anomaly checks — block publication.

### 6-2) Fail, warn, or quarantine

| Severity | Action | Use for |
|---|---|---|
| **Error** | fail the task, block downstream | broken keys, schema drift, empty load |
| **Warn** | alert, continue | small volume dips, a few invalid rows |
| **Quarantine** | write bad rows to a side table, continue with the rest | row-level validity failures |

**Write-audit-publish (WAP)**: write the new data to a staging branch or table, run the
checks, and only then publish (swap the table, fast-forward the branch — Iceberg branches,
Nessie, or a `CREATE OR REPLACE` swap). Consumers never see unchecked data.

### 6-3) Data contracts and observability

- A **data contract** is an agreement with the producing team: schema, semantics, SLAs,
  ownership, and how changes are announced. Enforced in CI on the producer side (schema
  registry, dbt model contracts), it turns "the source changed" from an outage into a failed
  pull request.
- **Observability** = monitoring the data, not only the jobs: freshness, volume, schema and
  distribution metrics per table, with anomaly detection on their history (Monte Carlo,
  Elementary, Soda, or homegrown metrics tables).
- **Lineage** (OpenLineage, dbt docs, a catalog such as DataHub or Unity Catalog) answers the
  two incident questions: *what broke this table* and *who is affected*.

---

## 7) File Formats and Table Formats ⭐⭐⭐⭐⭐

### 7-1) Row vs columnar formats

**Use Avro (row-based, schema embedded) for ingestion and messages, Parquet (columnar,
compressed, with statistics) for analytics, and never CSV or JSON past the raw layer.**

| Format | Layout | Schema | Splittable | Best for |
|---|---|---|---|---|
| CSV | row, text | none | yes, uncompressed; not when gzipped | exchange with humans and legacy systems |
| JSON lines | row, text | none | yes, uncompressed | APIs, semi-structured raw data |
| **Avro** | row, binary | embedded, with evolution rules | yes | ingestion, Kafka messages, write-heavy |
| **Parquet** | columnar | embedded | yes | analytical scans |
| **ORC** | columnar | embedded | yes | the Hive ecosystem |

Columnar wins for analytics because a query reads only the columns it touches, values of
one column compress well together, and per-chunk statistics let the reader skip data.

### 7-2) Parquet internals

**A Parquet file is split into row groups, each row group holds one column chunk per
column, each chunk is split into pages, and the footer stores the schema plus min/max
statistics that let a reader skip row groups.**

```text
Parquet file
├── Row group 0  (default target size in parquet-mr: 128 MB)
│   ├── Column chunk: user_id   → pages (dictionary page + data pages)
│   ├── Column chunk: country   → pages
│   └── Column chunk: amount    → pages
├── Row group 1
│   └── ...
└── Footer: schema, row-group metadata, per-chunk min / max / null count
```

- **Encodings**: dictionary encoding for low-cardinality columns (`country` becomes small
  integer ids), run-length and bit-packing on top, then a codec (Snappy, Zstd, Gzip).
- **Predicate pushdown**: `WHERE amount > 1000` skips any row group whose `amount` max is
  ≤ 1000. It only helps when the data is **sorted or clustered** by that column — on random
  data every row group's min/max spans everything.
- **Column pruning**: only the selected columns' chunks are read.
- **Nested data** (structs, lists) is stored with repetition and definition levels (the Dremel
  encoding), so nested fields are columnar too.

### 7-3) Table formats: Iceberg, Delta Lake, Hudi

**A table format keeps a metadata layer over Parquet files that records exactly which files
make up each version of the table — that is what gives a lake atomic commits, `MERGE`,
schema evolution, and time travel.**

What every table format adds over "a directory of Parquet files":

- **Atomic commits** — readers see the old version or the new one, never a half-written job.
- **Row-level `UPDATE` / `DELETE` / `MERGE`**.
- **Schema evolution** with column ids or mapping, so renames do not break files.
- **Time travel** — query or roll back to a previous snapshot.
- **File-level statistics in metadata** — planning skips files without listing directories.
- **Compaction** and snapshot expiry as maintenance operations.

| | Apache Iceberg | Delta Lake | Apache Hudi |
|---|---|---|---|
| Metadata | metadata files → manifest lists → manifests | `_delta_log` JSON commits + Parquet checkpoints | timeline of commits in `.hoodie` |
| Partitioning | **hidden partitioning** with transforms (`days(ts)`, `bucket(16, id)`); partition evolution | Hive-style columns; liquid clustering as an alternative | Hive-style |
| Row-level changes | copy-on-write or merge-on-read (delete files) | copy-on-write; deletion vectors | copy-on-write or merge-on-read, built for upserts |
| Engine breadth | very broad (Spark, Flink, Trino, Snowflake, BigQuery, Athena) | broadest on Spark / Databricks; others via connectors | Spark and Flink first |
| Typical pick | multi-engine lakehouse | Databricks shops | CDC-heavy upsert workloads |

**Copy-on-write vs merge-on-read**: copy-on-write rewrites the affected files on every update
(fast reads, slow writes); merge-on-read writes small delete / log files and merges them at
read time (fast writes, reads slow down until compaction).

### 7-4) The small-files problem and compaction

**Many small files slow every reader — each file costs an open, a metadata lookup and
often a task — so batch jobs should write files of a few hundred MB and compaction should
merge what streaming or frequent small batches leave behind.**

Causes:

- Too many output partitions × too many tasks (each task writes one file per partition it touches).
- Frequent micro-batches or streaming sinks.
- Over-partitioning by a high-cardinality column.

Fixes:

- Reduce writers before the write: `coalesce(n)`, or `repartition` by the partition column
  so each partition is written by few tasks.
- Spark AQE coalesces shuffle partitions automatically (`spark.sql.adaptive.enabled`).
- Table maintenance: Delta `OPTIMIZE` (with `ZORDER BY` or liquid clustering), Iceberg
  `rewrite_data_files` procedure, Hudi clustering / compaction.
- Expire old snapshots (`VACUUM`, `expire_snapshots`) so storage and metadata do not grow forever.

---

## 8) Partitioning, Cost and Performance ⭐⭐⭐⭐

### 8-1) Partition for the reader

**Partition by the column nearly every query filters on — usually the event or load date —
and keep each partition large; the full rules are in [`./schema_design.md`](./schema_design.md).**

- Date partitioning gives you both pruning for readers and a natural unit of idempotent
  overwrite for writers.
- Cluster / sort within partitions by the next most-filtered column so min/max statistics work.
- Over-partitioning (by `user_id`, by hour on a small table) creates small files and slow planning.

### 8-2) Where batch cost goes

| Cost driver | Lever |
|---|---|
| Bytes scanned (BigQuery on-demand, Athena) | column pruning, partition filters, clustering, pre-aggregates |
| Compute time (Snowflake warehouses, Databricks clusters) | right-size and auto-suspend; incremental instead of full rebuilds |
| Rebuilding unchanged data | incremental models, `MERGE` only changed partitions |
| Shuffles and skew in Spark | broadcast joins, fixing skew (see [`./spark.md`](./spark.md)) |
| Storage of old snapshots and duplicates | snapshot expiry, retention policies on bronze |
| Egress | keep compute in the same region as storage |

The usual high-impact change in an interview answer: move a nightly full rebuild of a large
fact table to an incremental `MERGE` of the partitions that changed.

### 8-3) Performance checklist

- Push filters and projections down to the source extract; do not `SELECT *` from production.
- Extract from a **read replica** or a CDC stream, not the primary.
- Join on integer surrogate keys rather than long strings where the model allows.
- Pre-aggregate in gold for dashboards; do not let BI tools scan silver.
- Measure before tuning: query profile / `EXPLAIN`, Spark UI stage times, and the slowest
  task in the DAG's critical path.

---

## 9) Failure Handling and Reruns ⭐⭐⭐⭐

### 9-1) Designing for failure

**Assume every task will fail halfway at some point; make each one atomic and idempotent so
the fix is always "rerun the task".**

- **Atomic outputs**: write to a temporary location or staging table, then swap or commit
  (table-format commit, `ALTER TABLE ... EXCHANGE PARTITION`, `CREATE OR REPLACE`). A
  half-finished write is then never visible.
- **Retries** with exponential backoff for transient errors (network, throttling); no retry
  for deterministic errors (a schema mismatch fails the same way every time).
- **Timeouts** on every task, so a hung query does not block the run forever.
- **Checkpoints** for long extracts (the last cursor, the last file) so a retry resumes.
- **Dependencies on data, not time**: sensors or dataset triggers, so a late upstream delays
  the run rather than producing an empty partition.

### 9-2) The incident runbook

1. **Detect** — a failed task, a failed quality check, or a freshness alert.
2. **Contain** — stop downstream publication; mark affected tables as stale for consumers.
3. **Diagnose** — lineage tells you which upstream changed; compare the failing partition
   with the last good one (row counts, schema, distinct keys).
4. **Fix** — code fix, source re-delivery, or a config change.
5. **Rerun / backfill** the affected intervals, oldest first; idempotency makes this safe.
6. **Verify** with the same checks, then publish.
7. **Prevent** — add the check that would have caught it earlier.

### 9-3) Common failure causes

| Cause | Symptom | Prevention |
|---|---|---|
| Upstream schema change | load or cast errors | schema checks at ingest, data contracts |
| Late upstream data | empty or partial partition | sensors, completeness checks, row-count thresholds |
| Duplicate ingestion | inflated metrics | idempotent writes, uniqueness tests |
| Skewed or grown data | job runs out of memory or misses its SLA | volume monitoring, skew handling, autoscaling |
| Credential / permission change | auth failures | secrets manager, alerting on auth errors |
| Non-deterministic logic | numbers change on rerun | logical-date parameters, total ordering |

---

## 10) Security, PII and Deletes ⭐⭐⭐

### 10-1) Handling PII

**Classify PII at ingestion, keep it in as few tables as possible, and give most users only
masked or tokenised versions.**

- **Classify** columns (email, phone, address, government ids) with tags in the catalog.
- **Minimise**: drop PII the use case does not need before silver.
- **Pseudonymise**: replace identifiers with a keyed hash (HMAC with a secret) or a token from
  a vault, so joins still work but the raw value is not exposed. A plain unsalted hash of an
  email is reversible by dictionary attack.
- **Mask** at query time with column-level policies (Snowflake masking policies, BigQuery
  policy tags, Unity Catalog column masks) and row-level security.
- Encrypt at rest and in transit; restrict raw-layer access to the pipeline's service accounts.

### 10-2) Right-to-be-forgotten deletes

**A GDPR / CCPA erasure request must reach every copy — raw, silver, gold, snapshots and
backups — which is why immutable raw layers need a deletion strategy designed in, not added later.**

- Keep a **deletion request table**; a scheduled job deletes the subject's rows in every table
  keyed on the subject id (`DELETE` / `MERGE` on Iceberg / Delta / Hudi, or the warehouse).
- Table formats keep old files for time travel: run snapshot expiry and `VACUUM` afterwards,
  or the deleted rows are still in storage.
- **Crypto-shredding**: encrypt each subject's PII with a per-subject key and delete the key —
  useful where rewriting immutable data or backups is impractical.
- Prefer keeping PII out of append-only raw files that are hard to rewrite; land it in a
  table format from the start.

---

## 11) Worked Design Question ⭐⭐⭐⭐⭐

### 11-1) "Design a daily pipeline from an OLTP database to a reporting warehouse"

**Clarify first, then walk the stages left to right, and spend the most time on
correctness: incremental extraction, idempotent loads, history, and quality gates.**

Clarifying questions to ask:

- Which tables, how big, how fast do they change? Do rows get hard-deleted?
- Freshness requirement — daily by 06:00, or hourly?
- Do reports need history (what did the customer's tier look like last March)?
- Can we read the primary, a replica, or the binlog / WAL?
- Who consumes the output — BI dashboards, finance reconciliation, ML?

A design for "orders, customers, products; ~50 M orders; daily by 06:00; hard deletes exist;
history needed for customers":

```text
 Postgres primary ─► read replica ─► Debezium ─► Kafka ─► S3 bronze (Parquet, dt=load_date)
                                                                │
 Airflow daily DAG (data interval = yesterday) ─────────────────┤
   1. sensor: bronze partition complete (Connect offsets / row-count manifest)
   2. dbt staging: dedupe latest change per key, cast types           → silver (Iceberg)
   3. dbt snapshot: dim_customer SCD2                                  → silver
   4. dbt incremental: fct_orders MERGE on order_id, 3-day late window → gold
   5. dbt tests + reconciliation vs source counts (block on error)
   6. publish: swap gold views / notify; freshness metric recorded
                                                                │
                                                 BI (Looker / Tableau) on gold
```

Why each choice:

| Decision | Reason |
|---|---|
| CDC from a replica, not `SELECT` on the primary | catches hard deletes; no load on the production database |
| Bronze append-only, partitioned by load date | immutable replay source; idempotent per load date |
| Silver on Iceberg / Delta | `MERGE` and deletes on the lake; time travel for debugging |
| SCD2 for customers | the history requirement |
| 3-day reprocess window on facts | late updates (refunds, status changes) land in the right event-date partition |
| Tests block publication | consumers never see a broken day |
| Fixed data interval in every task | backfills and reruns give the same result |

Follow-ups to be ready for: *what if CDC is not allowed* (incremental by `updated_at` with
overlap, plus a weekly full snapshot diff to catch deletes); *what if volume grows 100×*
(partition the extract, Spark for heavy transforms, clustering, incremental everything);
*what if the business needs hourly* (same design on an hourly interval, or move to streaming
— see [`./stream_pipeline.md`](./stream_pipeline.md)).

---

## 12) Quick Q → A ⭐⭐⭐⭐

### 12-1) Questions that come up in the room

- **Q: How do you make a pipeline idempotent?** — Parameterise every task by its logical date,
  read a fixed slice, and overwrite or `MERGE` its output instead of appending.
- **Q: How do you handle late-arriving data?** — Reprocess a trailing window of event-date
  partitions, or partition by arrival date, or `MERGE` late rows into place; pick by how late
  data gets and what it costs to rewrite.
- **Q: ETL or ELT?** — ELT by default for analytics; ETL where data must be transformed before
  it may land (PII) or the transform is not SQL-shaped.
- **Q: How do you detect deletes in an incremental load?** — You cannot with `updated_at`; use
  CDC, soft-delete flags, or a periodic full-key snapshot diff.
- **Q: Why Parquet?** — Columnar: reads only needed columns, compresses well, and min/max
  statistics let readers skip row groups.
- **Q: What does a table format add?** — Atomic commits, row-level changes, schema evolution,
  time travel and file-level statistics on top of Parquet files.
- **Q: What passes through XCom?** — Small metadata such as paths and counts; data goes
  through storage.
- **Q: A job is slow. Where do you look first?** — The critical path in the DAG, then the
  slowest stage or query profile: skew, full scans that should prune, or a full rebuild that
  could be incremental.
- **Q: How do you backfill a year safely?** — Idempotent tasks, throttled concurrency, oldest
  first, into a new table or partitions, validate against the old output, then swap.
- **Q: How do you stop bad data reaching dashboards?** — Quality checks at each layer, blocking
  on errors, and write-audit-publish so only checked data is published.
