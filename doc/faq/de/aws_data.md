# AWS Data Services: Glue, EMR, Iceberg, Athena, RDS, Lambda and SQS

> **Scope** — the AWS data stack as a data engineering interview probes it: how S3, the Glue Data Catalog, Glue ETL, EMR, Athena and RDS fit together, choosing between the compute services, Glue jobs and bookmarks, EMR cluster anatomy and EMR Serverless, Apache Iceberg on S3 with the Glue catalog, Athena's cost model, partitions and Iceberg DML, getting data out of RDS (JDBC, snapshot export, DMS CDC, zero-ETL), event-driven ingestion with S3, SQS and Lambda (Lambda vs Spark, timeouts, visibility timeout, duplicates and idempotency, DLQs, consumer concurrency), S3 partitioning and file sizes, AWS CDK, pipeline monitoring and cost, security, and a worked RDS-to-lakehouse design. The engine-neutral comparison of Iceberg, Delta and Hudi is [batch_pipeline.md](./batch_pipeline.md)'s; Spark internals and tuning are [spark.md](./spark.md)'s; Redshift has its own older notes.
> **See also**: [`./batch_pipeline.md`](./batch_pipeline.md) — lake vs warehouse vs lakehouse, table formats, idempotency and backfills; [`./spark.md`](./spark.md) — the engine under Glue and EMR; [`./kafka.md`](./kafka.md) — CDC with Debezium and Kafka Connect; [`../db/faq_redshift.md`](../db/faq_redshift.md) — the Redshift notes; [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist.

Most AWS data platforms are the same five pieces: **files in S3**, **a catalog that says which
files are a table** (Glue Data Catalog), **a table format that makes those files transactional**
(Iceberg), **engines that read and write them** (Glue, EMR, Athena), and **operational databases
the data comes from** (RDS / Aurora). Around them sit the event-driven glue — **S3 events, SQS
and Lambda** — and the code that deploys and watches it all (**CDK, CloudWatch**). The interview
rarely asks you to recite a service; it asks you to pick one and defend the trade-off.

---

## 0) The Short Answer ⭐⭐⭐⭐⭐

**"Storage is S3, the Glue Data Catalog is the shared metastore, and tables are Iceberg so I get
atomic commits, `MERGE` and time travel on S3. Source data comes out of RDS by CDC with DMS (or
zero-ETL into Redshift). I transform with Glue for small-to-medium serverless Spark jobs and EMR
— usually EMR Serverless — when I need control over versions, tuning or cost at scale. Analysts
query with Athena, whose bill is bytes scanned, so the tables are Parquet, partitioned on the
filter column and compacted."**

```text
   RDS / Aurora (OLTP)                    other sources (APIs, files, Kafka/MSK)
        |  DMS CDC  /  snapshot export  /  Glue JDBC
        v
   S3  raw (bronze)  --Glue job or EMR Spark-->  S3 Iceberg silver / gold
                                                      |
                     Glue Data Catalog  <-------------+  (one metastore for every engine)
                       |        |        |
                    Athena     EMR    Redshift Spectrum / QuickSight
```

---

## 1) The AWS Data Map ⭐⭐⭐⭐⭐

### 1-1) Which service does what

**Each service owns one layer; the Glue Data Catalog is what lets them share tables.**

| Layer | Service | One-line role |
|---|---|---|
| Storage | **S3** | durable, cheap object storage; the lake itself |
| Metastore | **Glue Data Catalog** | Hive-metastore-compatible table definitions shared by Athena, EMR, Glue, Redshift Spectrum |
| Table format | **Apache Iceberg** (or Delta / Hudi) | snapshots, atomic commits, row-level changes over Parquet in S3 |
| Serverless ETL | **AWS Glue jobs** | managed Spark (and Python shell) jobs, billed per DPU-hour |
| Big-data clusters | **Amazon EMR** | Spark, Hive, Trino, Flink, HBase on EC2, on EKS, or Serverless |
| Interactive SQL | **Amazon Athena** | serverless Trino-based SQL over the catalog, billed per data scanned |
| Operational DB | **Amazon RDS / Aurora** | managed PostgreSQL, MySQL, etc. — the source, not the warehouse |
| Migration / CDC | **AWS DMS** | full load + change data capture out of RDS into S3, Kinesis or MSK |
| Governance | **Lake Formation** | table-, column-, row- and cell-level permissions on catalog tables |
| Warehouse | **Redshift** | MPP columnar warehouse for heavy, repeated BI workloads |
| Messaging | **Amazon SQS** | managed queue that decouples producers from consumers; at-least-once delivery |
| Event compute | **AWS Lambda** | short, event-driven functions — validate, route, trigger — up to 15 minutes each |
| Infrastructure as code | **AWS CDK** | TypeScript / Python that synthesises CloudFormation |
| Observability | **Amazon CloudWatch** | metrics, logs and alarms for every service above |

### 1-2) Choosing the compute ⭐⭐⭐⭐⭐

**Ask three questions: is it SQL or code, is it ad hoc or scheduled, and how much control (and
how much data) is there?**

| | Glue job | EMR on EC2 | EMR Serverless | Athena | Redshift |
|---|---|---|---|---|---|
| Interface | PySpark / Scala Spark, Python shell | any Hadoop-stack app | Spark, Hive | SQL | SQL |
| Infra to manage | none | cluster, nodes, AMIs | none (an application) | none | cluster or serverless workgroup |
| Startup | tens of seconds | minutes for a new cluster | seconds with pre-initialised capacity | none | none |
| Tuning control | limited (worker type, count, Spark conf) | full | most Spark conf | little | sort / dist keys, WLM |
| Billing | DPU-hours, per second | EC2 + EMR uplift; Spot possible | vCPU and GB per second | data scanned, or provisioned capacity | node-hours or RPU-hours |
| Sweet spot | scheduled ETL of small to medium size, crawlers, catalog work | large, long or highly tuned jobs; non-Spark apps; cheapest at scale with Spot | spiky Spark batch without cluster ops | ad hoc exploration, light transforms (CTAS) | dashboards and heavy repeated joins |

The answer interviewers like: **"Athena for exploration, Glue or EMR Serverless for the
pipelines, EMR on EC2 only when I need the control or the Spot savings, and Redshift only when
a BI workload is heavy and repeated enough to pay for a warehouse."** Lambda is not in this
table because it is not a place to run the transform: it reacts to events and starts the job
that does ([§7-1](#7-1-why-lambda-and-spark-not-one-of-them-)).

---

## 2) AWS Glue ⭐⭐⭐⭐⭐

### 2-1) What Glue is

**Glue is three products under one name: a Data Catalog (the metastore), crawlers (schema
inference), and serverless Spark ETL jobs — plus Data Quality rules and workflows around them.**

| Part | What it does |
|---|---|
| **Data Catalog** | databases, tables, partitions, schemas; one per account per region |
| **Crawlers** | scan S3 / JDBC, infer the schema and partitions, write them to the catalog |
| **ETL jobs** | Spark (Glue 5.0 runs Spark 3.5), streaming Spark, or plain Python shell |
| **Job bookmarks** | remember what a job already processed, for incremental runs |
| **Data Quality** | DQDL rules (`IsComplete "id"`, `RowCount > 0`) evaluated in a job or on a table |
| **Workflows / triggers** | simple orchestration; most teams use Step Functions or MWAA (Airflow) instead |

### 2-2) The Glue Data Catalog ⭐⭐⭐⭐⭐

**The catalog is a managed Hive metastore: a table is a name, a schema, a format and an S3
location. Every AWS engine reads the same definition, which is why it is the centre of the
stack.**

- **Hive-style table**: the catalog holds the schema and **every partition** (`dt=2026-11-09`
  → an S3 prefix). Planning a query lists partitions from the catalog, then files from S3.
- **Iceberg table**: the catalog holds only a **pointer** (`metadata_location`) to the table's
  current Iceberg metadata file. Partitions and files live in Iceberg's own metadata, so the
  catalog does not grow with them.
- **Permissions**: IAM decides who can call the catalog API; **Lake Formation** adds
  table-, column-, row- and cell-level grants on top, enforced by Athena, EMR, Glue and Redshift.
- **Sharing**: one catalog per region per account; cross-account access through Lake Formation
  or resource policies.

### 2-3) Crawlers, and when not to use them ⭐⭐⭐⭐

**A crawler infers a schema from the files; that is convenient for exploration and risky for a
production pipeline, where the schema should be declared, not guessed.**

| Problem | How it bites | Fix |
|---|---|---|
| **Schema drift** | one bad file flips a column's type or adds a column, breaking readers | declare the table in DDL / IaC; validate in the job |
| **Table explosion** | inconsistent layouts make the crawler create one table per folder | consistent prefixes; crawler grouping settings; or no crawler |
| **Partition lag** | new partitions are invisible until the crawler runs again | register partitions in the job, use partition projection, or use Iceberg |
| **Cost and time** | re-scanning a large prefix every run | incremental crawls (S3 event mode), or none |

Ways to register new Hive partitions, cheapest last:

1. Run the crawler.
2. `MSCK REPAIR TABLE t` — lists the whole prefix; slow on large tables.
3. `ALTER TABLE t ADD PARTITION (dt='2026-11-09')` from the job that wrote it.
4. **Partition projection** in Athena — no partitions stored at all (see [§5-3](#5-3-partitions-repair-add-or-project-)).
5. **Iceberg** — the commit itself records the new files; nothing to register.

### 2-4) A Glue ETL job ⭐⭐⭐⭐

**A Glue job is a Spark application with Glue's context around it: `GlueContext` for catalog
reads and writes, `DynamicFrame` for messy schemas, and `Job` for bookmarks.**

```python
# python — a Glue 5.0 job: read the raw CDC table, MERGE it into an Iceberg table
import sys
from awsglue.utils import getResolvedOptions
from awsglue.context import GlueContext
from awsglue.job import Job
from pyspark.context import SparkContext

args = getResolvedOptions(sys.argv, ["JOB_NAME"])
glue_context = GlueContext(SparkContext())
spark = glue_context.spark_session
job = Job(glue_context)
job.init(args["JOB_NAME"], args)

# transformation_ctx names this source's bookmark state; bookmarks themselves are switched
# on by the job argument --job-bookmark-option job-bookmark-enable (off = full reread)
raw = glue_context.create_dynamic_frame.from_catalog(
    database="raw",
    table_name="orders_cdc",
    transformation_ctx="raw_orders_cdc",
)
changes = raw.toDF()
changes.createOrReplaceTempView("changes")

# keep the latest change per key, then upsert. Op = 'D' is a DMS delete; Op is NULL on
# full-load rows unless the task sets IncludeOpForFullLoad. change_seq is DMS's source
# change sequence (the AR_H_CHANGE_SEQ header, added by a transformation rule): unlike
# updated_at it is unique and in commit order, so a delete never ties with the update before it
spark.sql("""
    MERGE INTO glue_catalog.silver.orders AS t
    USING (
        SELECT * FROM (
            SELECT *, ROW_NUMBER() OVER (PARTITION BY order_id ORDER BY change_seq DESC) AS rn
            FROM changes
        ) WHERE rn = 1
    ) AS s
    ON t.order_id = s.order_id
    -- a stale replay (s.change_seq <= t.change_seq) matches no clause, so the row is left alone
    WHEN MATCHED AND s.change_seq > t.change_seq AND s.Op = 'D' THEN DELETE
    WHEN MATCHED AND s.change_seq > t.change_seq THEN UPDATE SET
        t.status = s.status, t.amount = s.amount, t.order_ts = s.order_ts,
        t.updated_at = s.updated_at, t.change_seq = s.change_seq
    WHEN NOT MATCHED AND (s.Op IS NULL OR s.Op <> 'D') THEN
        INSERT (order_id, customer_id, status, amount, order_ts, updated_at, change_seq)
        VALUES (s.order_id, s.customer_id, s.status, s.amount, s.order_ts, s.updated_at, s.change_seq)
""")

job.commit()  # the bookmark advances only here, after the write succeeded
```

The job enables Iceberg with the job parameter `--datalake-formats iceberg` plus the catalog
settings in [§4-6](#4-6-wiring-spark-to-iceberg-and-the-glue-catalog), and bookmarks with
`--job-bookmark-option job-bookmark-enable`.

- **Ordering CDC events**: order by the source's change sequence (DMS `AR_H_CHANGE_SEQ`, a
  Debezium LSN), never by `updated_at` — two events can share a timestamp, and a delete may not
  carry one. Keeping `change_seq` on the target row lets the `MERGE` ignore an older event that
  is replayed later. A hard delete removes that version, so if an older update for a deleted key
  can still arrive in a later batch, keep a tombstone (soft delete) instead.

- **`DynamicFrame` vs `DataFrame`**: a `DynamicFrame` tolerates a column with mixed types
  (a *choice* type, resolved with `resolveChoice`) and plugs into bookmarks and catalog sinks.
  Convert with `toDF()` for anything SQL-shaped; the DataFrame API is faster and better known.
- **Sizing**: one **DPU** is 4 vCPU and 16 GB. Worker types `G.1X` (1 DPU), `G.2X` (2 DPU)
  and larger; `G.025X` for small streaming jobs. Auto Scaling releases idle workers.
- **Flex execution** runs non-urgent jobs on spare capacity at a lower rate, with a less
  predictable start time.
- **Python shell jobs** run plain Python on a fraction of a DPU — right for an API call or a
  small file, wrong for anything that needs Spark.

### 2-5) Job bookmarks and idempotency ⭐⭐⭐⭐⭐

**A bookmark records what the last successful run processed — S3 objects by modification time,
or JDBC rows by a monotonically increasing key — so the next run reads only new input.**

- It needs the job argument `--job-bookmark-option job-bookmark-enable` (the default is
  disabled), plus `job.init`, a `transformation_ctx` on each source, and `job.commit()` at the
  end. Missing any of the four silently turns the job back into a full reload.
- It is **at-least-once**: a run that writes and then fails before `commit()` will reprocess
  the same input. So the write must be idempotent — `MERGE` on a key, or overwrite the
  partition for the run's logical date — never a blind append.
- For JDBC it keys on the primary key by default (`jobBookmarkKeys` to choose another); a key
  that is not monotonic, or rows updated in place, are missed. That is a reason to prefer CDC.
- **Backfills**: reset or pause the bookmark, or run a separate job parameterised by date.
  A bookmark answers "what is new?", not "rebuild November".

### 2-6) Glue pitfalls ⭐⭐⭐⭐

| Symptom | Usual cause | Fix |
|---|---|---|
| job slow, most workers idle | one huge or unsplittable file (`.gz` CSV) | splittable formats; repartition after read |
| thousands of tiny output files | many partitions × many tasks | repartition by the partition column before writing; compact Iceberg |
| `OutOfMemory` on the driver | `collect()`, or listing millions of S3 objects | stay distributed; `groupFiles` / `groupSize` for many small inputs |
| JDBC read takes hours on one task | single-connection read | `hashfield` / `hashpartitions` (or Spark's `partitionColumn` / `numPartitions`), read from a replica |
| reruns duplicate data | append + bookmark without idempotent write | `MERGE` or partition overwrite |

---

## 3) Amazon EMR ⭐⭐⭐⭐

### 3-1) What EMR is, and its three deployment options

**EMR is managed open-source big data — Spark, Hive, Trino, Flink, HBase, Hudi, Iceberg — with
AWS-tuned builds and S3 integration. You choose where it runs.**

| Option | You manage | Good for |
|---|---|---|
| **EMR on EC2** | the cluster: instance types, node groups, scaling, Spot | long-running or heavily tuned clusters, non-Spark apps (HBase, Flink), the lowest cost at scale |
| **EMR on EKS** | an EKS cluster that EMR submits Spark jobs to | teams already standardised on Kubernetes |
| **EMR Serverless** | an *application* (release + type); AWS manages capacity | Spark or Hive batch with spiky load and no ops team |

### 3-2) Cluster anatomy on EC2 ⭐⭐⭐⭐⭐

**Primary node coordinates; core nodes run tasks *and* hold HDFS; task nodes only run tasks.
So Spot belongs on task nodes, and the data belongs in S3.**

| Node | Runs | Losing one |
|---|---|---|
| **Primary** (master) | YARN ResourceManager, HDFS NameNode, Spark history | the cluster fails (unless multi-primary) |
| **Core** | YARN NodeManager **and** HDFS DataNode | HDFS blocks and running tasks lost — keep core on On-Demand |
| **Task** | YARN NodeManager only | only its running tasks are retried — ideal for **Spot** |

- **Transient clusters**: create per job, run steps, `--auto-terminate`. Nothing survives the
  cluster except S3, which is the point.
- **Long-running clusters**: for interactive work (Trino, notebooks) or HBase; use managed
  scaling and auto-termination on idle.
- **Instance fleets** let EMR pick from several instance types and mix On-Demand and Spot,
  which makes Spot interruptions far rarer than with one type.
- **EMRFS** is how EMR reads and writes `s3://`; the **EMRFS S3-optimised committer** avoids
  the rename-based commit that is slow and unsafe on S3.

### 3-3) Submitting work ⭐⭐⭐

```bash
# bash — a transient EMR on EC2 cluster that runs one Spark step and terminates
aws emr create-cluster \
  --name nightly-orders \
  --release-label emr-7.5.0 \
  --applications Name=Spark \
  --use-default-roles \
  --instance-groups \
      InstanceGroupType=MASTER,InstanceCount=1,InstanceType=m5.xlarge \
      InstanceGroupType=CORE,InstanceCount=2,InstanceType=r5.2xlarge \
      InstanceGroupType=TASK,InstanceCount=6,InstanceType=r5.2xlarge,BidPrice=OnDemandPrice \
  --steps 'Type=Spark,Name=build-orders,ActionOnFailure=TERMINATE_CLUSTER,Args=[--deploy-mode,cluster,s3://my-bucket/jobs/build_orders.py,--run-date,2026-11-09]' \
  --auto-terminate

# bash — the same job on EMR Serverless: no cluster, just an application id and a role
aws emr-serverless start-job-run \
  --application-id "$APP_ID" \
  --execution-role-arn "$JOB_ROLE_ARN" \
  --job-driver '{
    "sparkSubmit": {
      "entryPoint": "s3://my-bucket/jobs/build_orders.py",
      "entryPointArguments": ["--run-date", "2026-11-09"],
      "sparkSubmitParameters": "--conf spark.executor.memory=8g --conf spark.executor.cores=4"
    }
  }'
```

In production these calls come from an orchestrator — Airflow (MWAA) operators or Step
Functions' EMR integrations — which also owns retries and backfills.

### 3-4) Glue or EMR? ⭐⭐⭐⭐⭐

**Glue when the job is ordinary Spark and you value zero operations; EMR when you need a
specific version, a non-Spark engine, deep tuning, or Spot pricing on large, steady volume.**

| Question | Points to Glue | Points to EMR |
|---|---|---|
| Ops capacity | none to spare | a platform team exists |
| Engine | Spark / Python | Spark plus Hive, Trino, Flink, HBase |
| Version control | Glue's Spark version is fine | need a specific Spark / Iceberg / library version |
| Tuning | defaults are enough | custom executors, shuffle service, node types |
| Volume and cost | small to medium, irregular | large and steady — Spot task nodes win |
| Startup | seconds to tens of seconds | Serverless with pre-initialised capacity, or a warm cluster |

### 3-5) EMR pitfalls ⭐⭐⭐⭐

- **Spot on core nodes** → HDFS blocks vanish with the node; jobs fail in confusing ways. Spot
  on task nodes only, and keep outputs in S3.
- **Data in HDFS on a transient cluster** → gone at termination. HDFS is scratch space.
- **Idle long-running clusters** → the biggest EMR bill line. Set auto-termination on idle.
- **Over-sized executors** → long GC pauses; under-sized → overhead. Start from
  `maximizeResourceAllocation` or explicit 4–5 cores per executor and tune from the Spark UI.
- **S3 throttling** (`503 Slow Down`) when thousands of tasks hit one prefix → spread the keys,
  write fewer, larger files.

---

## 4) Apache Iceberg on AWS ⭐⭐⭐⭐⭐

### 4-1) Why Iceberg on S3

**A Hive table on S3 is a directory of files plus a list of partitions in the catalog — no
atomic commit, slow listing, and a partition list that grows without bound. Iceberg replaces the
directory with a tree of metadata files and commits by swapping one pointer.**

| | Hive-style table on S3 | Iceberg table on S3 |
|---|---|---|
| Which files are the table | whatever is under the prefix now | exactly the files listed by the current snapshot |
| Commit | files appear one by one; readers can see half a job | atomic swap of `metadata_location` |
| Planning | list partitions from the catalog, then list S3 | read manifests; file statistics prune without listing |
| Row-level changes | rewrite whole partitions by hand | `MERGE`, `UPDATE`, `DELETE` |
| Schema change | by position or name; renames break files | column ids; add, drop, rename, reorder safely |
| Partitioning | explicit columns every query must filter on | **hidden** transforms; partition evolution |
| History | none | snapshots, time travel, rollback |

The engine-neutral comparison with Delta Lake and Hudi is
[batch_pipeline.md §7-3](./batch_pipeline.md#7-3-table-formats-iceberg-delta-lake-hudi).

### 4-2) The metadata tree and the commit ⭐⭐⭐⭐⭐

**Every write produces new data files, new manifests and a new metadata file; the commit is a
compare-and-swap of the catalog's pointer from the old metadata file to the new one.**

```text
Glue Data Catalog   table silver.orders
  metadata_location ──► s3://.../metadata/00042-....metadata.json   (schema, partition spec, snapshots)
                              │ current-snapshot-id
                              ▼
                         snap-....avro   manifest list  (one per snapshot)
                              │
                 ┌────────────┴────────────┐
                 ▼                         ▼
          manifest A.avro             manifest B.avro    (data / delete files + column min/max)
                 │                         │
           data files (Parquet)      data files, delete files
```

- **Optimistic concurrency**: a writer reads version N, writes its files, and asks the catalog
  to move the pointer from N to N+1. If another writer got there first, the commit fails and
  the writer retries against the new version — re-validating that its change still applies.
- **Conflicts that bite**: two jobs `MERGE`-ing the same partitions, or compaction rewriting
  files a streaming writer is deleting from. Schedule compaction away from heavy writes, or
  scope it to cold partitions.
- **Readers** never lock anything: a query pins one snapshot for its whole run.

### 4-3) Hidden partitioning and partition evolution ⭐⭐⭐⭐

**Iceberg partitions by a transform of a column, so queries filter on the real column
(`order_ts`) and still prune; changing the scheme later does not rewrite old data.**

```sql
-- sql — Spark SQL on the Glue catalog
CREATE TABLE glue_catalog.silver.orders (
    order_id     BIGINT,
    customer_id  BIGINT,
    status       STRING,
    amount       DECIMAL(12, 2),
    order_ts     TIMESTAMP,
    updated_at   TIMESTAMP,
    change_seq   STRING          -- DMS AR_H_CHANGE_SEQ: fixed-width, so it sorts as a string
)
USING iceberg
PARTITIONED BY (days(order_ts))
TBLPROPERTIES (
    'format-version'    = '2',
    'write.merge.mode'  = 'merge-on-read',
    'write.delete.mode' = 'merge-on-read',
    'write.update.mode' = 'merge-on-read'
);

-- a reader filters on the column itself; Iceberg maps it to day partitions
SELECT SUM(amount) FROM glue_catalog.silver.orders
WHERE order_ts >= TIMESTAMP '2026-11-01 00:00:00';

-- volume grew: bucket new data by customer as well; old files keep their day-only layout
ALTER TABLE glue_catalog.silver.orders ADD PARTITION FIELD bucket(16, customer_id);
```

### 4-4) Copy-on-write vs merge-on-read ⭐⭐⭐⭐

**Copy-on-write rewrites every data file a change touches — slow writes, fast reads.
Merge-on-read writes small delete files and merges them at read time — fast writes, reads
slow down until compaction.**

| Workload | Mode |
|---|---|
| nightly batch, read-heavy dashboards | copy-on-write (the default) |
| frequent CDC upserts, streaming `MERGE` | merge-on-read, plus scheduled compaction |

Format version 2 is required for row-level deletes; Athena reads and writes Iceberg v2.

### 4-5) Table maintenance ⭐⭐⭐⭐⭐

**An Iceberg table that is never maintained gets slower and more expensive every day: small
files, piled-up delete files, thousands of snapshots, and orphaned files from failed writes.**

| Task | Spark procedure | Athena | Why |
|---|---|---|---|
| Compact small files, apply deletes | `rewrite_data_files` | `OPTIMIZE t REWRITE DATA USING BIN_PACK` | fewer opens, faster reads |
| Expire old snapshots | `expire_snapshots` | `VACUUM t` (with `vacuum_max_snapshot_age_seconds`) | metadata and storage stop growing |
| Delete orphan files | `remove_orphan_files` | `VACUUM t` | files from failed writes cost money forever |
| Compact manifests | `rewrite_manifests` | — | faster planning |

```sql
-- sql — Spark procedures on the Glue catalog
CALL glue_catalog.system.rewrite_data_files(
    table => 'silver.orders',
    strategy => 'binpack',
    where => 'order_ts < current_date()'          -- leave the hot partition to the writers
);
CALL glue_catalog.system.expire_snapshots(
    table => 'silver.orders',
    older_than => TIMESTAMP '2026-11-03 00:00:00',
    retain_last => 20
);
CALL glue_catalog.system.remove_orphan_files(table => 'silver.orders');
```

Managed alternatives: the **Glue Data Catalog table optimizers** run compaction, snapshot
retention and orphan-file deletion for Iceberg tables automatically; **S3 Tables** (table
buckets) store Iceberg tables with maintenance built into the bucket. Both trade control for
not owning the schedule.

A **GDPR delete** takes more than `DELETE`. The row becomes invisible, but its bytes survive in
three places: in the *live* data file when the delete was merge-on-read (only a delete file
masks it), in older snapshots reachable by time travel, and in copies outside the table. So:
`DELETE`, then **force** a rewrite of the affected files so the current snapshot no longer holds
the row — the defaults skip a data file with too few delete files, which is exactly the
one-row erasure case. In Spark, pass `options => map('delete-file-threshold', '1')` to
`rewrite_data_files`; in Athena, set the table property `optimize_rewrite_delete_file_threshold`
to `1` before `OPTIMIZE`. **Verify** that no delete files remain in the affected partitions (the
`delete_files` metadata table) before going on. Then expire the snapshots that still hold the
row, then purge the other copies — the raw CDC landing zone, and noncurrent S3 object versions
if bucket versioning is on.

### 4-6) Wiring Spark to Iceberg and the Glue catalog

```text
--datalake-formats iceberg                       (Glue job parameter; EMR: the Iceberg classification)
--conf spark.sql.extensions=org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions
--conf spark.sql.catalog.glue_catalog=org.apache.iceberg.spark.SparkCatalog
--conf spark.sql.catalog.glue_catalog.catalog-impl=org.apache.iceberg.aws.glue.GlueCatalog
--conf spark.sql.catalog.glue_catalog.io-impl=org.apache.iceberg.aws.s3.S3FileIO
--conf spark.sql.catalog.glue_catalog.warehouse=s3://my-bucket/warehouse/
```

The extensions enable `MERGE` and the `CALL` procedures; `GlueCatalog` makes the Glue Data
Catalog the pointer store, so the table Spark writes is the table Athena reads.

---

## 5) Amazon Athena ⭐⭐⭐⭐⭐

### 5-1) What Athena is

**Athena is serverless SQL over S3: a Trino-based engine (engine version 3) that reads table
definitions from the Glue Data Catalog. You pay for the bytes each query scans, so every
performance lever is also a cost lever.**

- **Pricing**: per TB scanned (with a 10 MB minimum per query), or **provisioned capacity**
  (reserved DPUs) for steady, predictable load.
- **Workgroups** separate teams: result location, encryption, engine version, and a
  **per-query data-scanned limit** that cancels a runaway query.
- **Formats**: Parquet, ORC, Avro, JSON, CSV; Iceberg tables with full DML; Hudi and Delta
  read support.
- **Federated query** reaches RDS, DynamoDB and others through Lambda connectors — fine for a
  lookup, too slow for a bulk extract.
- **Athena for Apache Spark** runs notebooks on the same service; separate from the SQL engine.

### 5-2) The cost model is the performance model ⭐⭐⭐⭐⭐

**Reduce bytes scanned: columnar formats, compression, partition pruning, fewer columns, and
right-sized files.**

| Lever | Why it cuts the scan |
|---|---|
| **Parquet / ORC** instead of CSV / JSON | reads only the selected columns; min/max statistics skip row groups |
| **Compression** (Snappy, ZSTD) | fewer bytes on disk = fewer bytes billed |
| **Partition on the filter column** | `WHERE dt = ...` reads one prefix, not the table |
| **Select only needed columns** | `SELECT *` on Parquet bills every column |
| **Files of ~128 MB–1 GB** | thousands of tiny files cost per-file overhead and S3 requests |
| **Sort / cluster by the next filter** | row-group statistics then skip most of each file |
| **CTAS to convert** | one `CREATE TABLE AS ... WITH (format = 'PARQUET')` turns raw JSON into a cheap table |

```sql
-- sql — Athena: convert a raw JSON table into partitioned, compressed Parquet once
CREATE TABLE analytics.events_parquet
WITH (
    format = 'PARQUET',
    write_compression = 'ZSTD',
    external_location = 's3://my-bucket/analytics/events_parquet/',
    partitioned_by = ARRAY['dt']
) AS
SELECT event_id, user_id, event_type, event_ts, dt   -- partition column last
FROM raw.events_json
WHERE dt >= '2026-11-01';
```

### 5-3) Partitions: repair, add, or project ⭐⭐⭐⭐

**For a Hive-style table, partition projection computes the partitions from a rule instead of
storing them, so there is nothing to register and nothing to forget.**

```sql
-- sql — Athena: a daily-partitioned log table with partition projection
CREATE EXTERNAL TABLE raw.app_logs (
    request_id  STRING,
    user_id     BIGINT,
    status      INT,
    latency_ms  INT
)
PARTITIONED BY (dt STRING)
STORED AS PARQUET
LOCATION 's3://my-bucket/raw/app_logs/'
TBLPROPERTIES (
    'projection.enabled'          = 'true',
    'projection.dt.type'          = 'date',
    'projection.dt.format'        = 'yyyy-MM-dd',
    'projection.dt.range'         = '2024-01-01,NOW',
    'projection.dt.interval'      = '1',
    'projection.dt.interval.unit' = 'DAYS',
    'storage.location.template'   = 's3://my-bucket/raw/app_logs/dt=${dt}/'
);
```

| Method | Cost | Trap |
|---|---|---|
| `MSCK REPAIR TABLE` | lists the whole prefix | slow and can time out on large tables; only finds `key=value` folders |
| `ALTER TABLE ADD PARTITION` | one catalog call | the writer must remember to make it |
| **partition projection** | none | a query **without** a filter on `dt` enumerates the whole range |
| **Iceberg** | none | — partitions live in table metadata |

### 5-4) Iceberg in Athena ⭐⭐⭐⭐⭐

**Athena creates, reads and writes Iceberg tables with `MERGE`, `UPDATE`, `DELETE`, time
travel and maintenance commands — enough to run a small lakehouse with SQL alone.**

```sql
-- sql — Athena engine v3
CREATE TABLE silver.customers (
    customer_id  BIGINT,
    email        STRING,
    country      STRING,
    updated_at   TIMESTAMP
)
PARTITIONED BY (country)
LOCATION 's3://my-bucket/warehouse/silver/customers/'
TBLPROPERTIES ('table_type' = 'ICEBERG', 'format' = 'parquet');

MERGE INTO silver.customers AS t
USING staging.customer_changes AS s
    ON t.customer_id = s.customer_id
WHEN MATCHED AND s.op = 'D' THEN DELETE
WHEN MATCHED THEN UPDATE SET email = s.email, country = s.country, updated_at = s.updated_at
WHEN NOT MATCHED AND (s.op IS NULL OR s.op <> 'D')     -- a replayed delete must not reinsert
    THEN INSERT (customer_id, email, country, updated_at)
         VALUES (s.customer_id, s.email, s.country, s.updated_at);

-- time travel, and the metadata tables
SELECT COUNT(*) FROM silver.customers FOR TIMESTAMP AS OF (current_timestamp - interval '1' day);
SELECT snapshot_id, committed_at, operation FROM "silver"."customers$snapshots";

-- maintenance
OPTIMIZE silver.customers REWRITE DATA USING BIN_PACK;
VACUUM silver.customers;
```

### 5-5) Athena, Redshift or EMR Trino? ⭐⭐⭐⭐

| Need | Pick | Why |
|---|---|---|
| ad hoc questions on lake data | **Athena** | no infrastructure; pay per query |
| dashboards hitting the same joins all day | **Redshift** (or materialised gold tables) | caching, sort keys, concurrency scaling; per-scan billing adds up |
| heavy interactive SQL with custom connectors or tuning | **Trino on EMR** | full control over the cluster and plugins |
| warehouse joins with lake data | **Redshift Spectrum** | query S3 tables from the warehouse through the same catalog |

### 5-6) Athena pitfalls ⭐⭐⭐⭐

| Error or symptom | Cause | Fix |
|---|---|---|
| bill spikes on a "small" query | `SELECT *`, no partition filter, or CSV | columns, filters, Parquet; a workgroup scan limit |
| `HIVE_PARTITION_SCHEMA_MISMATCH` | a partition's schema differs from the table's | fix the partition or rebuild it; declare schemas rather than crawl |
| query slow over many tiny files | streaming or frequent small writes | compaction (`OPTIMIZE`, Spark `rewrite_data_files`) |
| query fails after 30 minutes | the DML timeout quota | break the job up, or move it to Glue / EMR |
| `HIVE_BAD_DATA` / null columns | file type differs from the declared type | validate before publishing; quarantine bad files |
| `503 Slow Down` | too many requests to one S3 prefix | fewer, larger files; spread prefixes |

---

## 6) Amazon RDS and Aurora ⭐⭐⭐⭐

### 6-1) What RDS is, from a data engineer's seat

**RDS is managed OLTP — PostgreSQL, MySQL, MariaDB, Oracle, SQL Server, Db2 — with backups,
patching and failover handled. Aurora is AWS's own engine (MySQL- and PostgreSQL-compatible)
with storage replicated six ways across three AZs. For a data engineer it is the source of
truth to extract from, not a place to run analytics.**

- Analytical scans on the primary compete with the application's transactions — extract from
  a **read replica**, a snapshot export, or the change log instead.
- Backups are automated (point-in-time recovery inside the retention window) plus manual
  snapshots; **snapshot export to S3** writes the tables as Parquet.

### 6-2) Multi-AZ vs read replicas ⭐⭐⭐⭐⭐

**Multi-AZ is for availability; read replicas are for read scale. Interviewers ask this one
because people mix them up.**

| | Multi-AZ (instance) | Read replica | Aurora replica |
|---|---|---|---|
| Purpose | survive an AZ or instance failure | offload reads, reporting, extraction | both: reads, and failover target |
| Replication | synchronous to a standby | asynchronous — can lag | shared cluster storage, typically low lag |
| Readable | standby is **not** readable (a Multi-AZ DB *cluster* has two readable standbys) | yes | yes |
| Failover | automatic, DNS endpoint flips | manual promotion | automatic |
| Cross-region | no | yes | Aurora Global Database |

### 6-3) Getting data out of RDS ⭐⭐⭐⭐⭐

**Pick by freshness and by whether you need deletes: a JDBC pull for small tables, snapshot
export for a one-off or nightly copy, log-based CDC for anything large or that must see
deletes, zero-ETL when the destination is Redshift.**

| Method | Freshness | Load on source | Captures deletes | Notes |
|---|---|---|---|---|
| **Glue / Spark JDBC pull** (full or `updated_at` watermark) | batch | queries the DB — use a replica | no (soft deletes only) | parallelise with a partition column; watermark misses rows whose `updated_at` is not maintained |
| **Snapshot export to S3** | per snapshot | none | as of the snapshot | Parquet output; good for an initial load or nightly full copy |
| **AWS DMS full load + CDC** | seconds to minutes | reads the binlog / WAL | **yes** | writes to S3 (Parquet / CSV with an `Op` column), Kinesis or MSK |
| **Debezium on MSK Connect** | seconds | reads the binlog / WAL | **yes** | Kafka-native; see [kafka.md](./kafka.md) |
| **Zero-ETL integration** | near real time | managed | **yes** | Aurora / RDS → Redshift with no pipeline to run; Redshift only |

```text
DMS CDC into S3, landed then merged:

  RDS (binlog / WAL) ──DMS task──► s3://raw/orders/  (Op=I|U|D, commit ts, row image)
                                       │  Glue / EMR job on a schedule
                                       ▼
                         MERGE latest change per key ──► Iceberg silver.orders
```

### 6-4) CDC prerequisites and the replication-slot trap ⭐⭐⭐⭐⭐

**CDC reads the database's own change log, so the log has to be on, in the right format, and
kept long enough — and a stalled consumer on PostgreSQL can fill the disk.**

- **MySQL / Aurora MySQL**: automated backups on (that enables the binlog), `binlog_format =
  ROW`, and enough binlog retention for the consumer to catch up after an outage:
  `CALL mysql.rds_set_configuration('binlog retention hours', 24);`
- **PostgreSQL**: `rds.logical_replication = 1` in the parameter group (reboot), then the
  CDC tool creates a **logical replication slot**.
- **The trap**: a replication slot keeps every WAL segment its consumer has not confirmed. If
  the DMS task or Debezium connector stops, WAL piles up until storage is full and the database
  stops accepting writes. Alarm on it, and drop slots that belong to dead consumers.

```sql
-- sql — PostgreSQL: how much WAL is each slot holding back?
SELECT slot_name,
       active,
       pg_size_pretty(pg_wal_lsn_diff(pg_current_wal_lsn(), restart_lsn)) AS retained_wal
FROM   pg_replication_slots;
```

CloudWatch's `TransactionLogsDiskUsage` and `OldestReplicationSlotLag` are the metrics to alarm on.

### 6-5) Operating RDS as a source ⭐⭐⭐

- **Connections**: hundreds of Spark executors or Lambda invocations can exhaust
  `max_connections`. Cap JDBC parallelism; put **RDS Proxy** in front of Lambda.
- **Credentials**: Secrets Manager (with rotation) or IAM database authentication — never a
  password in job parameters.
- **Network**: RDS lives in private subnets; a Glue job needs a **connection** in the same VPC,
  and S3 access from there needs a VPC endpoint or NAT.
- **Scaling**: vertical (instance class), storage autoscaling, read replicas; Aurora Serverless
  v2 scales capacity in fine steps for spiky load.

---

## 7) Event-Driven Ingestion: S3, SQS and Lambda ⭐⭐⭐⭐⭐

**A common AWS shape for log and file ingestion: objects land in S3, an event per object goes
to SQS, a Lambda consumer validates it and hands the heavy work to Spark, and the results go
back to S3. Interviewers probe the edges: why the work is split that way, what Lambda cannot
do, and what happens when a message arrives twice, fails forever, or arrives ten thousand at a
time.**

```text
producers (cluster agents, apps)
    |  PUT  s3://lake/landing/...                 ~1 TB/day of raw logs
    v
S3 landing  --event notification-->  SQS queue  --after maxReceiveCount-->  SQS DLQ
                                         |
                                         |  event source mapping: batch, window, max concurrency
                                         v
                                 Lambda consumer   validate, dedupe, record the batch
                                         |
                                         |  submit (one job per batch / window, not per message)
                                         v
                          Spark on EMR / EMR Serverless / Glue   parse, transform, aggregate
                                         |
                                         v
                    S3 curated (Parquet / Iceberg, partitioned)  -->  Athena, reports
```

### 7-1) Why Lambda *and* Spark, not one of them ⭐⭐⭐⭐⭐

**"They are not alternatives; they solve different shapes of work. Lambda runs short,
event-driven tasks — validate a message, read metadata, route it, start the next stage. Spark
runs the large distributed work — parse, join, shuffle and aggregate a terabyte across many
executors. Each one is wasteful or impossible at the other's job."**

| Workload | Fits | Why |
|---|---|---|
| React to one object landing; check its metadata and schema | **Lambda** | runs only when an event arrives; scales with the queue; nothing idle to pay for |
| Route, enrich or reject a small record | **Lambda** | seconds of work, no cluster to start |
| Parse and transform 1 TB of logs | **Spark** | partitions spread across executors; no 15-minute ceiling |
| Joins, `groupBy`, window functions over the whole day | **Spark** | needs a shuffle — data moved between machines — which Lambda has no notion of |
| Daily rollups and report tables | **Spark** (or Athena CTAS when it is plain SQL) | scans the whole partition once |

- **Everything on Spark**: a cluster (or a Serverless application's startup) for every small
  event adds latency and idle cost, and a long-running cluster sized for the daily peak sits
  mostly empty.
- **Everything on Lambda**: the 15-minute limit, the 10 GB memory cap and stateless,
  share-nothing invocations make a distributed join or aggregation impossible without
  rebuilding a worse Spark by hand.

**Senior point:** the split is also a *failure boundary*. A bad message fails one Lambda
invocation and goes to the DLQ; it does not fail a two-hour Spark job.

### 7-2) Lambda vs long-running compute ⭐⭐⭐⭐

**Compare them on run time, resources, cost model and operations — then match each to the
workload rather than preferring "serverless" for its own sake.**

| | AWS Lambda | Long-running compute (ECS / Fargate, EC2, EMR) |
|---|---|---|
| Run time | up to **15 minutes** per invocation | hours or days |
| Resources | 128 MB – 10,240 MB memory; CPU scales with memory (up to 6 vCPU); `/tmp` up to 10 GB | any instance size; local disks; many nodes |
| Scaling | per event, automatic, to the concurrency limit | auto scaling groups, cluster scaling — slower, but you control it |
| State | none between invocations | in-memory state, caches, shuffle |
| Cost model | per request + GB-seconds of run time; nothing when idle | per instance-second while running, busy or not |
| Cost at steady high load | can exceed a well-used instance | cheaper per unit of work when kept busy; Spot on top |
| Operations | no servers, patching or capacity planning | AMIs or images, scaling policies, cluster lifecycle |
| Cold start | yes — milliseconds to seconds | none once warm |

| Option | Pick it for |
|---|---|
| **Lambda** | short, spiky, event-driven work |
| **ECS / Fargate** | a container that runs longer than 15 minutes, or holds a connection, with no cluster to run |
| **EMR (EC2 or Serverless) / Glue** | distributed Spark — see [§1-2](#1-2-choosing-the-compute-) and [§3-4](#3-4-glue-or-emr-) |
| **AWS Batch** | a large number of independent, container-sized jobs |

**Senior point:** Lambda's fast scaling is not automatically good. Ten thousand messages at
once can mean a thousand concurrent invocations hammering a database, an API or the Spark job
submitter — see [§7-7](#7-7-controlling-consumer-concurrency-).

### 7-3) Lambda's timeout and memory limits ⭐⭐⭐⭐

**"I design the work to fit the limits rather than raising them until they stop failing: keep
each invocation small, stream instead of loading, and hand anything big or long to Spark."**

**Timeout (900 seconds at most):**

- **Size the batch to the time.** The event source mapping's batch size and batching window
  decide how much one invocation gets; size them so a batch finishes in a minute or two, well
  inside the timeout.
- **Split the work into stages.** Lambda validates and records the batch, then starts the
  Spark job and returns; it never waits for a terabyte to be processed. A multi-step flow with
  waits belongs to **Step Functions** or the orchestrator, not to a Lambda polling in a loop.
- **Know what a timeout does to the message.** A timed-out invocation is a failure: the batch
  becomes visible again and counts towards `maxReceiveCount`, so a job that always times out
  ends in the DLQ.

**Memory (128 MB – 10,240 MB):**

- **Memory buys CPU.** Lambda allocates CPU in proportion to memory — 1,769 MB is one full
  vCPU — so raising memory often makes a CPU-bound function faster *and* cheaper per run. Measure
  it rather than guessing: cost per invocation, or per GB processed, is the number to compare.
- **Stream, do not load.** Read an S3 object with a streaming body, line by line, instead of
  `read()`-ing a 2 GB file into memory.
- **`/tmp` is configurable up to 10 GB** for spill space, but a function that needs it is
  usually a sign the work belongs on Spark.
- **Reuse clients across invocations** — create `boto3` clients and connections outside the
  handler, so a warm environment does not rebuild them.

**Watch:** `Duration` against the timeout, `Errors`, `Throttles` and `ConcurrentExecutions` in
CloudWatch, and *Max Memory Used* from each invocation's `REPORT` log line (or Lambda Insights).
A function whose duration creeps towards its timeout as data grows is telling you it has
outgrown Lambda.

### 7-4) Consuming SQS with Lambda ⭐⭐⭐⭐⭐

**SQS decouples the producers from the processing. Lambda's event source mapping polls the
queue for you, invokes the function with a batch, and deletes the batch on success. Four
settings decide whether it works: the message shape, the visibility timeout, the batch, and
partial-batch failure reporting.**

**1. Message shape — send a pointer, not the payload.** A message is capped at 1 MiB, so it
carries the S3 location and the identity of the work, not the log lines:

```json
{
  "event_id": "evt-20261110-cluster-a-000001",
  "dataset": "cluster_logs",
  "source": "cluster-a",
  "s3_bucket": "data-lake",
  "s3_key": "landing/date=2026-11-10/cluster-a/part-0001.json.gz",
  "event_time": "2026-11-10T10:00:00Z"
}
```

S3 event notifications have their own shape (a `Records` array with `bucket`, `key`,
`versionId` and a `sequencer`); either way the consumer reads the object from S3.

**2. Visibility timeout.** A received message is hidden from other consumers, not deleted. If
the function succeeds, the mapping deletes it; if not, it reappears when the visibility timeout
ends and is retried. Set it to **at least six times the function timeout, plus the batching
window** (`MaximumBatchingWindowInSeconds`) — AWS's guidance — so a slow or throttled batch does
not reappear and run twice while the first attempt is still going. With a 120-second function
and a 10-second window, that is 6 × 120 + 10 = 730 seconds.

**3. Batch size and window.** One invocation can take many messages:

| Setting | Effect | Trade-off |
|---|---|---|
| `BatchSize` | up to 10,000 for a standard queue (10 for FIFO) | bigger batch → fewer invocations, but more to redo on failure and more time per run |
| `MaximumBatchingWindowInSeconds` | wait up to N seconds (max 300) to fill a batch | fewer, fuller invocations at the cost of latency |
| invocation payload | 6 MB per invocation | caps the batch however large `BatchSize` is |

**4. Partial batch failures.** Without them, one bad message fails the whole batch and all
of it is retried — re-running the messages that succeeded. With `ReportBatchItemFailures`
switched on, the function returns the ids of only the messages that failed:

```python
# python — an SQS-triggered handler that retries only the messages that failed
def handler(event, context):
    failures = []
    for record in event["Records"]:
        try:
            process_record(record)       # §7-5: parses the body; must be safe to run twice
        except Exception:
            # only this message returns to the queue; the rest of the batch is deleted
            failures.append({"itemIdentifier": record["messageId"]})
    return {"batchItemFailures": failures}
```

**Watch:** `ApproximateNumberOfMessagesVisible` (backlog), `ApproximateAgeOfOldestMessage`
(how far behind the consumer is — the one to alarm on), `NumberOfMessagesReceived` vs
`NumberOfMessagesDeleted`, and the function's `Errors` and `Duration`. A growing age means the
consumer, or something downstream of it, cannot keep up.

### 7-5) Duplicate messages and idempotency ⭐⭐⭐⭐⭐

**"A standard queue delivers at least once, and S3 event notifications can repeat too, so I
assume every message can arrive twice. Delivery is not exactly-once; I make the *processing*
idempotent — running it twice leaves the same result as running it once."**

Duplicates come from the queue itself, from a visibility timeout that ran out, from a batch
that was retried, from a producer that retried a send, and from a DLQ replay. Three layers
handle them:

**1. A stable business key.** Dedupe on the producer's `event_id` (or, for S3 events, bucket +
key + `versionId` / `sequencer`) — **not** SQS's `messageId`, which is new each time a producer
re-sends the same event. For S3 events that only works if every input object lands under a
**unique key that is never overwritten** (a UUID or the producer's sequence in the name): an
overwrite under the same key looks like a duplicate of the first object, and the new data is
skipped — or, read late, the first event processes the second object's bytes.

**2. A claim ledger with a conditional write.** "Check, then process" is a race: two consumers
can both see "not done". A DynamoDB conditional write lets exactly one of them claim the event,
and a lease lets a retry take over from a consumer that died mid-run:

```python
# python — claim an event exactly once with a DynamoDB conditional write
import json
import time

import boto3
from botocore.exceptions import ClientError

dynamodb = boto3.client("dynamodb")
TABLE = "ingest_claims"           # partition key: event_id; TTL attribute: expires_at
LEASE_SECONDS = 3 * 60            # a little over the function timeout (2 min), so a dead
                                  # holder's claim frees up well before the retries run out
TTL_SECONDS = 14 * 24 * 3600      # longer than queue + DLQ retention, so a replay is still caught


def try_claim(event_id):
    """Return 'CLAIMED', 'DONE' (a duplicate) or 'BUSY' (another consumer holds it)."""
    now = int(time.time())
    try:
        dynamodb.put_item(
            TableName=TABLE,
            Item={
                "event_id": {"S": event_id},
                "status": {"S": "IN_PROGRESS"},
                "lease_until": {"N": str(now + LEASE_SECONDS)},
                "expires_at": {"N": str(now + TTL_SECONDS)},
            },
            # a new event, or one whose previous holder died and let its lease run out
            ConditionExpression="attribute_not_exists(event_id) OR "
                                "(#s = :in_progress AND lease_until < :now)",
            ExpressionAttributeNames={"#s": "status"},
            ExpressionAttributeValues={":in_progress": {"S": "IN_PROGRESS"},
                                       ":now": {"N": str(now)}},
            ReturnValuesOnConditionCheckFailure="ALL_OLD",
        )
        return "CLAIMED"
    except ClientError as e:
        if e.response["Error"]["Code"] != "ConditionalCheckFailedException":
            raise
        existing = e.response.get("Item", {})
        return "DONE" if existing.get("status", {}).get("S") == "COMPLETED" else "BUSY"


def process_record(record):
    body = json.loads(record["body"])
    event_id = body["event_id"]
    state = try_claim(event_id)
    if state == "DONE":
        return                                    # duplicate delivery: nothing to do
    if state == "BUSY":
        raise RuntimeError(f"{event_id} is being processed elsewhere")   # retry later
    try:
        run_pipeline(body)                        # the real work, itself idempotent (layer 3);
                                                  # defined elsewhere in the function
    except Exception:
        # release the claim so the retry can take it at once
        dynamodb.delete_item(TableName=TABLE, Key={"event_id": {"S": event_id}})
        raise
    dynamodb.update_item(
        TableName=TABLE,
        Key={"event_id": {"S": event_id}},
        UpdateExpression="SET #s = :done",
        ExpressionAttributeNames={"#s": "status"},
        ExpressionAttributeValues={":done": {"S": "COMPLETED"}},
    )
```

(Powertools for AWS Lambda ships this pattern as its `idempotency` utility.)

**When `run_pipeline` only submits a Spark job, `COMPLETED` means *submitted*, not *processed*.**
The message is deleted once the submission is recorded, so the job's own failure must not depend
on the message to be retried. Store the job's run id on the claim (`job_run_id`), and let the
orchestrator — Step Functions, Airflow, or the job's own retry policy — watch that run to the end,
retry it, and alarm when it fails for good. Make the submission itself idempotent too: a
deterministic job name or client token derived from the event (or batch) id, so a duplicate
submit finds the run that already exists.

**3. An idempotent sink.** The ledger cuts duplicates; it cannot remove them, because the work
can succeed and the process die before `COMPLETED` is written. So the output must be safe to
write twice:

| Sink | Idempotent write |
|---|---|
| S3 files | an output key derived from a **stable** identity — the input object's key, or the event id — so a retry overwrites what the first attempt wrote. A retry can regroup messages into a different batch, so a path named after the batch is deterministic only if the batch's membership is pinned first: write a batch manifest (batch id → event ids) before any output, and have a retry replay that manifest. Either way a reader must never see half of it |
| Iceberg / Delta table | `MERGE` on the business key, or replace the batch's partition in one commit ([§4-2](#4-2-the-metadata-tree-and-the-commit-)) |
| Database | upsert on a unique key |

**FIFO queues are not the fix by themselves.** They deduplicate on a `MessageDeduplicationId`
within a **five-minute** window and keep order within a `MessageGroupId`, at lower throughput.
A replay from the DLQ an hour later is outside the window — business-level idempotency is
still needed.

**Senior point:** separate *delivery* from *effect*. The queue decides how often a message
arrives; the pipeline decides that the data comes out the same however often it does.

### 7-6) Dead-letter queues and replay ⭐⭐⭐⭐

**A DLQ moves a message that keeps failing out of the main queue, so one poison message does
not burn retries and capacity forever. It isolates the failure; it does not fix it — the
alarm, the triage and the replay are the other half.**

```text
SQS main queue ──► Lambda consumer
     ▲                 │
     │ failed:         ├── success → message deleted
     │ visible again   │
     └─────────────────┘
          after maxReceiveCount receives
                 │
                 ▼
             SQS DLQ ──► alarm ──► triage ──► fix ──► redrive to the main queue
```

- **Redrive policy** on the main queue: the DLQ's ARN and `maxReceiveCount`. AWS recommends
  **at least 5** for a Lambda consumer, because a throttled or timed-out batch also counts as a
  receive — set it too low and healthy messages land in the DLQ during a spike.
- **Retention**: in a standard queue a message keeps its *original* enqueue time when it moves
  to the DLQ, so give the DLQ a **longer** retention than the source (up to 14 days) — or
  messages expire there before anyone has looked.
- **Alarm** on the DLQ's `ApproximateNumberOfMessagesVisible > 0`. A DLQ nobody watches is a
  slower way of dropping data.
- **Triage by cause:**

| Cause | Action |
|---|---|
| transient — a network blip, a downstream outage, throttling | redrive once the dependency is healthy |
| malformed message, missing field | fix the producer; redrive or discard after recording why |
| missing or late data | data-quality investigation |
| a bug in the consumer | fix, deploy, then redrive |

- **Replay**: SQS's DLQ redrive (`StartMessageMoveTask`, or *Start DLQ redrive* in the
  console) moves messages back to the source queue at a controlled rate. A replay is a
  duplicate by definition — which is why [§7-5](#7-5-duplicate-messages-and-idempotency-)
  comes first.

### 7-7) Controlling consumer concurrency ⭐⭐⭐⭐

**"I size concurrency to the slowest thing downstream, not to what Lambda can do. If the Spark
cluster can take twenty job submissions a minute, five hundred concurrent consumers only make
the queue someone else's problem."**

Unbounded consumers exhaust database connections, trip API rate limits, submit too many Spark
jobs and spend money fast. The controls, from the queue outwards:

| Control | Limits | Note |
|---|---|---|
| **Maximum concurrency** on the SQS event source mapping | invocations driven by *this queue* | the right knob for SQS: the mapping stops polling at the cap, so excess messages simply wait in the queue |
| **Reserved concurrency** on the function | *every* invocation of the function, from any source | also guarantees the function capacity in the account; but used alone with SQS, pollers still fetch messages, invocations get throttled, and throttled messages count towards `maxReceiveCount` — healthy messages drift into the DLQ |
| **Batch size / window** | messages per invocation | concurrency × batch size is the messages in flight — 50 × 10 = 500 here, before duration and arrival rate decide the real throughput |
| **Downstream** | the shared resource itself | RDS Proxy for connections, a job-submission queue or semaphore, one Spark job per batch or time window instead of per message, retries with exponential backoff and jitter |

If both caps are set, keep the mapping's maximum concurrency **below** the function's reserved
concurrency, so the mapping, not throttling, is what holds the line.

**Watch:** `ConcurrentExecutions` and `Throttles` on the function, queue depth and age of the
oldest message, and the downstream's own latency and submission rate. Rising queue age with flat
concurrency means the cap is binding — raise it only if the downstream has headroom.

---

## 8) Laying Out Data in S3 ⭐⭐⭐⭐

### 8-1) Hive-style partitioning and partition pruning ⭐⭐⭐⭐⭐

**Partitioning is for the readers: a key in the path lets Spark, Athena and Glue skip every
object outside the filter. Choose the keys from the queries that will run, in the order they
filter.**

```text
s3://data-lake/curated/cluster_logs/
    dt=2026-11-10/
        cluster_id=cluster-a/
            part-00000-....snappy.parquet      (~128 MB – 1 GB each)
            part-00001-....snappy.parquet
        cluster_id=cluster-b/
            part-00000-....snappy.parquet
    dt=2026-11-11/
        ...
```

```sql
-- sql — Athena / Spark SQL: the dt filter prunes every other day before a byte is read
SELECT cluster_id,
       COUNT(*) AS error_count
FROM   curated.cluster_logs
WHERE  dt = '2026-11-10'
  AND  log_level = 'ERROR'
GROUP  BY cluster_id;
```

- **Date first**, because almost every log query is bounded in time. One `dt=yyyy-MM-dd` key
  is easier to filter on than `year=/month=/day=` (a range across a month boundary is one
  comparison, not three).
- **A second key only if queries filter on it and its cardinality is low** — `cluster_id`
  with tens of values, yes; `request_id` or `user_id`, never.
- **Hours** only when the data is big enough that an hour still makes large files, and queries
  ask for hours.
- Register partitions with **partition projection** or use **Iceberg** — see
  [§5-3](#5-3-partitions-repair-add-or-project-) and
  [§4-3](#4-3-hidden-partitioning-and-partition-evolution-).

**Senior point:** S3 is object storage, not HDFS — a partition is just a key prefix. Pruning
helps because the *engine* never lists or opens the skipped objects. Prefixes matter to S3
itself only for request rates (about 3,500 writes and 5,500 reads per second per prefix), which
is a different reason to spread keys.

### 8-2) Cardinality, file size and the small-file problem ⭐⭐⭐⭐

**Every partition key multiplies the number of directories; too many, and each holds a few
tiny files. Small files cost on every read: more listings, more GET requests, more Spark tasks
and more Parquet footers to open, for the same bytes.**

| Lever | Practice |
|---|---|
| Format | **Parquet** (or ORC), compressed with Snappy or ZSTD — column pruning, predicate pushdown on row-group statistics, smaller scans |
| Target file size | roughly **128 MB – 1 GB** per file |
| Partitions | low-cardinality keys only; check the count per partition and the size per file |
| Writing from Spark | `repartition` by the partition columns before `partitionBy`, so each task writes one file per partition instead of one per partition *per task*; `maxRecordsPerFile` as a ceiling |
| Already small | a scheduled compaction job — or Iceberg's `rewrite_data_files` ([§4-5](#4-5-table-maintenance-)) |
| Skew | one huge partition (a noisy cluster) and many tiny ones → salt or split the hot key, check the partition-size distribution |
| Old data | S3 Lifecycle: raw to Standard-IA after 30 days, Glacier tiers after that, expire what the retention policy allows |

```scala
// scala — Spark: rebuild whole (dt, cluster_id) partitions, one sizeable file per task each
logs
  .repartition(col("dt"), col("cluster_id"))
  .write
  .mode("overwrite")
  .option("partitionOverwriteMode", "dynamic")   // replace only the partitions present in logs
  .option("maxRecordsPerFile", 5000000)
  .partitionBy("dt", "cluster_id")
  .parquet("s3://data-lake/curated/cluster_logs/")
```

- **Dynamic overwrite replaces every partition the write touches, whole.** Without it,
  `overwrite` deletes the entire table first. With it, a re-run of one day is idempotent and
  leaves the other days alone — **but only if `logs` holds the complete partition**. A later
  partial batch for the same `(dt, cluster_id)` would wipe the rows earlier batches wrote. So
  this shape is for a job that rebuilds whole partitions (the daily job reading the full day);
  incremental batches append under batch-scoped keys ([§7-5](#7-5-duplicate-messages-and-idempotency-))
  or `MERGE` into Iceberg.
- **Check the committer.** On EMR, confirm the release's EMRFS S3-optimised committer supports
  dynamic partition overwrite; where it does not, Spark falls back to a slower, rename-based
  commit on S3.
- **Hot keys cap the parallelism.** `repartition` by `(dt, cluster_id)` sends each pair to one
  task, so a few huge pairs (a noisy cluster) are each written by a single task while the rest of
  the cluster idles. Add a salt to the expression for those keys — e.g.
  `repartition(col("dt"), col("cluster_id"), pmod(hash(col("request_id")), lit(8)))` — to split
  them across tasks (a deterministic salt, not `rand()`, so a retried task regroups the same rows), and let compaction merge the extra files later.
- **Not atomic on a plain S3 table** — a reader can see the partition mid-rewrite; Iceberg makes
  it one commit.

---

## 9) Operating the Pipeline: CDK, Monitoring and Cost ⭐⭐⭐⭐

### 9-1) Infrastructure as code with AWS CDK ⭐⭐⭐⭐

**"Every bucket, queue, function, role and alarm is TypeScript in the repo, reviewed like code
and deployed through the same pipeline to every environment. Nothing is clicked in the
console, so dev, staging and prod differ only in the parameters we chose."**

- **CDK** synthesises **CloudFormation**: `cdk synth` renders the template, `cdk diff` shows
  what a change will do to the deployed stack, `cdk deploy` applies it.
- **Constructs** are the unit of reuse — a `QueueWithDlq` or `IngestPipeline` construct
  that bundles the queue, the DLQ, the consumer, its permissions and its alarms, so the next
  pipeline cannot forget the DLQ.
- **Environments** are stack parameters — names, concurrency caps, alarm thresholds, sizes —
  not copies of the code.

```typescript
// typescript — AWS CDK v2: landing bucket → queue (+DLQ) → capped, batch-aware Lambda consumer
import { Duration, RemovalPolicy, Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import * as cloudwatch from 'aws-cdk-lib/aws-cloudwatch';
import * as lambda from 'aws-cdk-lib/aws-lambda';
import { SqsEventSource } from 'aws-cdk-lib/aws-lambda-event-sources';
import * as s3 from 'aws-cdk-lib/aws-s3';
import * as s3n from 'aws-cdk-lib/aws-s3-notifications';
import * as sqs from 'aws-cdk-lib/aws-sqs';

export interface LogIngestProps extends StackProps {
  maxConcurrency: number;          // per environment: small in dev, sized to the downstream in prod
  maxQueueAgeSeconds: number;      // the freshness SLA, as an alarm threshold
}

export class LogIngestStack extends Stack {
  constructor(scope: Construct, id: string, props: LogIngestProps) {
    super(scope, id, props);

    const lake = new s3.Bucket(this, 'Lake', {
      encryption: s3.BucketEncryption.S3_MANAGED,
      blockPublicAccess: s3.BlockPublicAccess.BLOCK_ALL,
      enforceSSL: true,
      removalPolicy: RemovalPolicy.RETAIN,           // deleting the stack never deletes the data
    });

    const functionTimeout = Duration.minutes(2);

    const dlq = new sqs.Queue(this, 'IngestDlq', {
      retentionPeriod: Duration.days(14),            // longer than the source: time to fix and replay
    });
    const queue = new sqs.Queue(this, 'IngestQueue', {
      visibilityTimeout: Duration.seconds(730),      // 6 x the function timeout + the batching window
      retentionPeriod: Duration.days(4),
      deadLetterQueue: { queue: dlq, maxReceiveCount: 5 },
    });
    lake.addEventNotification(
      s3.EventType.OBJECT_CREATED,
      new s3n.SqsDestination(queue),
      { prefix: 'landing/' },                       // producers write unique, never-overwritten keys
    );

    const consumer = new lambda.Function(this, 'Consumer', {
      runtime: lambda.Runtime.PYTHON_3_12,
      architecture: lambda.Architecture.ARM_64,
      handler: 'consumer.handler',
      code: lambda.Code.fromAsset('lambda/consumer'),
      timeout: functionTimeout,
      memorySize: 1769,                              // one full vCPU
      environment: { LAKE_BUCKET: lake.bucketName },
    });
    lake.grantRead(consumer, 'landing/*');           // least privilege: only what it reads

    consumer.addEventSource(new SqsEventSource(queue, {
      batchSize: 50,
      maxBatchingWindow: Duration.seconds(10),
      reportBatchItemFailures: true,
      maxConcurrency: props.maxConcurrency,
    }));

    new cloudwatch.Alarm(this, 'DlqNotEmpty', {
      metric: dlq.metricApproximateNumberOfMessagesVisible(),
      threshold: 0,
      comparisonOperator: cloudwatch.ComparisonOperator.GREATER_THAN_THRESHOLD,
      evaluationPeriods: 1,
    });
    new cloudwatch.Alarm(this, 'ConsumerFallingBehind', {
      metric: queue.metricApproximateAgeOfOldestMessage(),
      threshold: props.maxQueueAgeSeconds,
      evaluationPeriods: 3,
    });
  }
}
```

**Deployment:** pull request → unit tests (`aws-cdk-lib/assertions` can assert, say, that every
queue has a redrive policy) → `cdk synth` and `cdk diff` posted on the review → deploy to
staging → validate → deploy to prod.

**Senior points:**

- **An infrastructure deploy can destroy data.** Changing a resource's logical id or a
  replacement-only property makes CloudFormation create a new one and delete the old — a
  bucket or table goes with it. `RemovalPolicy.RETAIN` on stateful resources, and read the
  `cdk diff` for `[-]` and *replace* before approving.
- **A rollback restores the template, not the data**, and a stack stuck in
  `UPDATE_ROLLBACK_FAILED` is its own incident.
- **Least privilege comes cheap** with `grant*` methods; **no secrets in code** — Secrets
  Manager or SSM Parameter Store; **tags** on every resource (`Tags.of(app).add(...)`) so cost
  can be allocated.

### 9-2) Monitoring pipeline health ⭐⭐⭐⭐⭐

**"Green AWS metrics do not mean the data is right. I watch the infrastructure, but I alarm on
the data: did it arrive on time, is it complete, and does it look right."**

| Layer | Watch | It tells you |
|---|---|---|
| Ingestion | objects and bytes landed per hour, per source | a source went quiet, or doubled |
| Queue | `ApproximateAgeOfOldestMessage`, visible messages, DLQ depth | the consumer is falling behind, or messages are failing |
| Lambda | `Errors`, `Throttles`, `Duration` vs timeout, `ConcurrentExecutions` | the consumer is failing, capped, or outgrowing Lambda |
| Spark | job duration, failed stages and executors, shuffle read/write, spill, task-time skew | the job is slowing down, and why |
| Freshness | the latest complete partition, time of the last successful run | the data is late |
| Completeness | row counts against the source or yesterday, every expected source present | the data is partial |
| Quality | null rate on key fields, duplicate keys, parse-failure rate, schema drift | the data is wrong |

- **Alarm on the SLA, not only on errors.** "Yesterday's logs are queryable by 08:00" is the
  contract. If the job finishes at 08:45 with every service healthy, the pipeline has failed
  its users — so alarm when the 08:00 partition is not complete, not when a job errors.
- **End-to-end latency**: stamp `ingested_at` and `processed_at` on the records, and track the
  p95 / p99 of the gap.
- **Fewer, better alarms.** Page on SLA risk and the DLQ; send the rest to a dashboard. An
  alarm nobody acts on trains people to ignore the one that matters.
- **Gate the publish** on data-quality checks (Glue Data Quality, Deequ, Great Expectations), so
  a bad day is held back rather than published and then explained.

### 9-3) Watching the cost ⭐⭐⭐⭐

**"At a terabyte a day, the bill is part of the design. I tag everything so it can be
attributed, track cost per GB processed, and know which line each optimisation moves."**

| Service | What drives the bill | The lever |
|---|---|---|
| S3 | stored GB, request count, transfer | Parquet + compression; fewer, bigger files; Lifecycle to IA / Glacier; expire what you may |
| Lambda | requests + GB-seconds | batching, so fewer invocations; right-size memory; arm64 (Graviton) |
| SQS | API requests (each up to 64 KB is one) | batched receive and delete — the event source mapping does this |
| Spark on EMR / Glue | instance- or DPU-hours | right-size executors, fix skew and shuffle, Spot on EMR task nodes, transient clusters or Serverless instead of idle ones |
| CloudWatch | log ingestion GB, retention, custom metrics | log level in prod, a retention period on every log group, no per-record logging |
| Athena | bytes scanned | [§5-2](#5-2-the-cost-model-is-the-performance-model-) |

- **Attribution:** **cost allocation tags** (activated in Billing) on every resource from the
  CDK app; **Cost Explorer** by tag; **AWS Budgets** alerts; **Cost Anomaly Detection** for the
  runaway loop or the request storm nobody expected.
- **Unit cost** — cost per GB ingested, per day processed — is the number that stays
  meaningful as volume grows. A total that doubles because volume doubled is fine; a cost per
  GB that doubles is not.

---

## 10) Security and Governance ⭐⭐⭐⭐

**Least-privilege IAM roles per job, encryption with KMS everywhere, private networking, and
Lake Formation for who-sees-which-column.**

| Concern | AWS answer |
|---|---|
| Job identity | one IAM role per Glue job / EMR Serverless application, scoped to its prefixes and tables |
| Fine-grained access | Lake Formation grants on databases, tables, columns, rows (data filters); LF-tags for scale |
| Encryption at rest | SSE-KMS on S3; KMS on RDS, Glue catalog and job bookmarks; Athena result encryption |
| Encryption in transit | TLS to RDS; S3 over HTTPS |
| Network | VPC endpoints for S3, Glue, Athena; RDS in private subnets |
| Secrets | Secrets Manager, referenced by the Glue connection |
| Audit | CloudTrail for API calls; Lake Formation and S3 access logs for data access |
| PII deletes | Iceberg `DELETE`, rewrite the affected files, then snapshot expiry; purge raw copies and old S3 versions ([§4-5](#4-5-table-maintenance-)) |

---

## 11) Worked Design: RDS Orders to an S3 Lakehouse ⭐⭐⭐⭐⭐

**Question**: *"Orders live in an Aurora PostgreSQL database. Analysts want yesterday's revenue
by country in Athena every morning, and finance wants corrections and cancellations reflected.
Design it on AWS."*

```text
Aurora PostgreSQL  ──DMS (full load, then CDC)──►  S3 raw/orders/   (Parquet, Op + change_seq)
                                                         │
        Step Functions / MWAA, hourly                    ▼
          └─► Glue job: dedupe latest per key, MERGE ──► Iceberg silver.orders  (merge-on-read)
          └─► Glue job (daily): aggregate ─────────────► Iceberg gold.daily_revenue
          └─► Data Quality rules gate the publish
          └─► nightly: rewrite_data_files + expire_snapshots
                                                         │
                       Glue Data Catalog ◄───────────────┘
                              │
                     Athena workgroup (scan limit) ──► QuickSight
```

| Decision | Reason |
|---|---|
| **DMS CDC**, not a JDBC pull on `updated_at` | sees hard deletes and cancellations; no load on the primary |
| **`IncludeOpForFullLoad=true` + a change-sequence column** | full-load rows get an `Op` like CDC rows (otherwise it is NULL, which the `MERGE` must treat as an insert); the sequence orders events per key |
| **Raw kept as landed** | replayable: a bug in silver is fixed by re-running from raw |
| **Iceberg silver with `MERGE`** | row-level corrections; atomic publish; time travel for "what did the report say yesterday?" |
| **Merge-on-read + nightly compaction** | hourly upserts stay cheap; reads stay fast |
| **Glue jobs**, not EMR | modest volume, no platform team; move to EMR Serverless if cost or tuning demands it |
| **Idempotent `MERGE` keyed on `order_id`** | a retried run produces the same table |
| **Partition gold by date** | the morning query reads one day |
| **Workgroup scan limit** | one careless `SELECT *` cannot become the month's bill |

Failure modes worth naming unprompted:

- **DMS task stops** → the PostgreSQL slot retains WAL ([§6-4](#6-4-cdc-prerequisites-and-the-replication-slot-trap-)); alarm on lag and disk.
- **Out-of-order or replayed changes** → dedupe by the source change sequence / LSN, not by
  `updated_at` or arrival time, and skip an event older than the row's stored sequence.
- **Schema change upstream** → DMS carries added columns; the `MERGE` lists columns explicitly,
  and Iceberg evolves the table by column id.
- **Compaction vs `MERGE` conflict** → compaction scoped to partitions older than today.

---

## 12) Interview Toolkit ⭐⭐⭐⭐⭐

### 12-1) Quick Q → A

- **Q: What is the Glue Data Catalog?** — A managed Hive metastore shared by Athena, EMR, Glue
  and Redshift Spectrum; for Iceberg it stores only the pointer to the current metadata file.
- **Q: Crawler or DDL?** — Crawlers for exploration; declared schemas (DDL / IaC) in production,
  because a guessed schema drifts.
- **Q: How do Glue bookmarks work, and what can go wrong?** — They store what the last
  successful run processed; they need `transformation_ctx` and `job.commit()`, and they are
  at-least-once, so the write must be idempotent.
- **Q: Glue or EMR?** — Glue for ordinary Spark with no ops; EMR for version control, non-Spark
  engines, deep tuning, or Spot savings at scale.
- **Q: Where do Spot instances go on EMR?** — Task nodes. Core nodes hold HDFS.
- **Q: How does an Iceberg commit stay atomic on S3?** — New files, new metadata, then a
  compare-and-swap of the catalog pointer; a losing writer retries.
- **Q: Copy-on-write or merge-on-read?** — CoW for read-heavy batch; MoR for frequent upserts,
  with compaction.
- **Q: How do you cut an Athena bill?** — Parquet, compression, partition on the filter column,
  select fewer columns, compact small files, set a workgroup scan limit.
- **Q: Multi-AZ vs read replica?** — Synchronous standby for failover vs asynchronous copy for
  reads.
- **Q: How do you extract from RDS without hurting it?** — CDC (DMS / Debezium) from the log,
  or read from a replica / snapshot export; never full scans of the primary.
- **Q: What is the replication-slot trap?** — A stopped CDC consumer makes PostgreSQL retain WAL
  until storage fills.
- **Q: Why not do everything in Lambda?** — 15 minutes, 10 GB, no shared state, no shuffle.
  Lambda reacts to events and starts the job; Spark does the distributed work.
- **Q: How long should the SQS visibility timeout be?** — At least six times the function
  timeout plus the batching window, so a slow batch does not reappear and run twice.
- **Q: How do you handle duplicate SQS messages?** — Assume them: a stable business key, a
  claim with a DynamoDB conditional write, and an idempotent sink (`MERGE`, deterministic
  paths).
- **Q: What does `ReportBatchItemFailures` do?** — Retries only the failed messages of a
  batch instead of the whole batch.
- **Q: What `maxReceiveCount` for a Lambda consumer?** — At least 5, because throttles and
  timeouts count as receives; and a DLQ retention longer than the source queue's.
- **Q: Reserved concurrency or maximum concurrency?** — For an SQS trigger, the event source
  mapping's maximum concurrency: excess messages wait in the queue instead of being throttled
  into the DLQ.
- **Q: How do you partition logs in S3?** — Date first, then a low-cardinality key the queries
  filter on; Parquet files of 128 MB – 1 GB; compact the small ones.

### 12-2) "Our Athena queries are slow and the bill doubled. What do you do?"

> "I would start from what Athena bills for, bytes scanned, because the cost and the latency
> have the same cause.
>
> First I look at the query history for the top queries by data scanned and check their shape:
> `SELECT *`, missing partition filters, or a filter on a column the table is not partitioned by.
>
> Then the tables underneath: are they CSV or JSON rather than Parquet, uncompressed, or made of
> thousands of small files from a streaming writer?
>
> The fixes, in order of payoff: convert hot tables to partitioned, compressed Parquet with CTAS
> or an ETL job, move them to Iceberg if they take updates, compact them on a schedule, and teach
> the heavy dashboards to read a pre-aggregated gold table.
>
> Then I put guard rails in: a workgroup per team with a per-query scan limit, and an alarm on
> scanned bytes. If a dashboard still runs the same heavy join all day, that workload belongs in
> Redshift."

### 12-3) "The pipeline took two hours at 1 TB a day; at 2 TB it takes five. Why?"

> "Twice the data should take roughly twice the time, so the extra hour is the interesting part
> — something stopped scaling linearly. I would walk the pipeline in order and look for where
> the time went.
>
> First, the input: did the volume really double evenly, or did one source or one cluster grow,
> which would point at skew? Then the queue: is the age of the oldest message climbing, and is
> the consumer hitting its concurrency cap or throttling?
>
> Then the Spark job, in the Spark UI: which stage grew. A shuffle stage that grew more than the
> input, a handful of tasks running far longer than the median, or spill to disk all say skew or
> under-sized partitions; thousands of tiny input files say a small-file problem upstream; and a
> long wait before the job starts says the cluster had no capacity.
>
> Then the dependencies: a slower database or API downstream holds everything behind it.
>
> The fix follows the cause — more partitions or a salted key for skew, compaction for small
> files, a higher concurrency cap only if the downstream has room, or more executors when the
> work genuinely scales out. And I would add the metric that would have caught it earlier:
> duration per GB, alarmed on, rather than total duration."

### 12-4) Answering "how did you …?" from your own pipeline

These questions are about *your* system, and an interviewer probes what you say. Five rules:

1. **Why before how.** Open with the decision and the constraint that drove it, not a list of
   services.
2. **Name the trade-off.** "Lambda was cheaper to run and simpler to operate, but it could
   not do the aggregation, so that went to Spark."
3. **Use real numbers, honestly.** 1 TB/day is a daily average — about 12 MB/s spread evenly —
   not the peak; know which one you are quoting, and what the peak was.
4. **Show production thinking unprompted:** retries, idempotency, the DLQ, monitoring, recovery,
   cost.
5. **Separate what you built from what you know.** To "how did you implement it?", describe
   only what you did. Alternatives go under *"if I were to redesign it today, I would …"* — that
   shows judgement without claiming work you did not do.

If there is time for only three of them, rehearse **why Lambda and not Spark** ([§7-1](#7-1-why-lambda-and-spark-not-one-of-them-)),
**duplicate messages** ([§7-5](#7-5-duplicate-messages-and-idempotency-)) and **S3
partitioning** ([§8-1](#8-1-hive-style-partitioning-and-partition-pruning-)): each one also
opens into a system-design discussion. Say each answer out loud in English in one to two
minutes, and prepare the follow-up underneath it.

### 12-5) Service cheat table

| Say this | When asked about |
|---|---|
| "one catalog, many engines" | why the Glue Data Catalog matters |
| "declared, not crawled" | production schemas |
| "bookmarks are at-least-once" | Glue incremental loads |
| "Spot on task nodes, data in S3" | EMR cost and reliability |
| "commit = pointer swap" | Iceberg atomicity on S3 |
| "maintain it or it rots" | Iceberg compaction and snapshot expiry |
| "bytes scanned is the bill" | Athena performance and cost |
| "availability vs read scale" | Multi-AZ vs read replicas |
| "CDC from the log, not queries" | extracting from RDS |
| "Lambda triggers, Spark computes" | Lambda vs Spark |
| "at-least-once in, idempotent out" | duplicate SQS messages |
| "visibility ≥ 6 × function timeout + batching window" | SQS + Lambda retries |
| "size concurrency to the slowest downstream" | consumer concurrency |
| "a DLQ isolates, it does not fix" | dead-letter queues |
| "date first, low cardinality, big files" | S3 partitioning |
| "alarm on the SLA, not only on errors" | pipeline monitoring |
