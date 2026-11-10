# AWS Data Services: Glue, EMR, Iceberg, Athena and RDS

> **Scope** — the AWS data stack as a data engineering interview probes it: how S3, the Glue Data Catalog, Glue ETL, EMR, Athena and RDS fit together, choosing between the compute services, Glue jobs and bookmarks, EMR cluster anatomy and EMR Serverless, Apache Iceberg on S3 with the Glue catalog, Athena's cost model, partitions and Iceberg DML, getting data out of RDS (JDBC, snapshot export, DMS CDC, zero-ETL), security, and a worked RDS-to-lakehouse design. The engine-neutral comparison of Iceberg, Delta and Hudi is [batch_pipeline.md](./batch_pipeline.md)'s; Spark internals and tuning are [spark.md](./spark.md)'s; Redshift has its own older notes.
> **See also**: [`./batch_pipeline.md`](./batch_pipeline.md) — lake vs warehouse vs lakehouse, table formats, idempotency and backfills; [`./spark.md`](./spark.md) — the engine under Glue and EMR; [`./kafka.md`](./kafka.md) — CDC with Debezium and Kafka Connect; [`../db/faq_redshift.md`](../db/faq_redshift.md) — the Redshift notes; [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist.

Most AWS data platforms are the same five pieces: **files in S3**, **a catalog that says which
files are a table** (Glue Data Catalog), **a table format that makes those files transactional**
(Iceberg), **engines that read and write them** (Glue, EMR, Athena), and **operational databases
the data comes from** (RDS / Aurora). The interview rarely asks you to recite a service; it asks
you to pick one and defend the trade-off.

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
a BI workload is heavy and repeated enough to pay for a warehouse."**

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

# transformation_ctx is the bookmark key: without it, every run re-reads everything
raw = glue_context.create_dynamic_frame.from_catalog(
    database="raw",
    table_name="orders_cdc",
    transformation_ctx="raw_orders_cdc",
)
changes = raw.toDF()
changes.createOrReplaceTempView("changes")

# keep the latest change per key, then upsert (Op = 'D' is a delete from DMS)
spark.sql("""
    MERGE INTO glue_catalog.silver.orders AS t
    USING (
        SELECT * FROM (
            SELECT *, ROW_NUMBER() OVER (PARTITION BY order_id ORDER BY updated_at DESC) AS rn
            FROM changes
        ) WHERE rn = 1
    ) AS s
    ON t.order_id = s.order_id
    WHEN MATCHED AND s.Op = 'D' THEN DELETE
    WHEN MATCHED THEN UPDATE SET t.status = s.status, t.amount = s.amount, t.updated_at = s.updated_at
    WHEN NOT MATCHED AND s.Op <> 'D' THEN
        INSERT (order_id, customer_id, status, amount, updated_at)
        VALUES (s.order_id, s.customer_id, s.status, s.amount, s.updated_at)
""")

job.commit()  # the bookmark advances only here, after the write succeeded
```

The job enables Iceberg with the job parameter `--datalake-formats iceberg` plus the catalog
settings in [§4-6](#4-6-wiring-spark-to-iceberg-and-the-glue-catalog).

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

- It needs `job.init`, a `transformation_ctx` on each source, and `job.commit()` at the end.
  Missing any of the three silently turns the job back into a full reload.
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
    updated_at   TIMESTAMP
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

Expiring snapshots is also how a **GDPR delete** finally leaves storage: `DELETE` makes the row
invisible, but the old files stay readable through time travel until they are expired.

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
WHEN NOT MATCHED THEN INSERT (customer_id, email, country, updated_at)
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

## 7) Security and Governance ⭐⭐⭐⭐

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
| PII deletes | Iceberg `DELETE` + snapshot expiry + orphan cleanup ([§4-5](#4-5-table-maintenance-)) |

---

## 8) Worked Design: RDS Orders to an S3 Lakehouse ⭐⭐⭐⭐⭐

**Question**: *"Orders live in an Aurora PostgreSQL database. Analysts want yesterday's revenue
by country in Athena every morning, and finance wants corrections and cancellations reflected.
Design it on AWS."*

```text
Aurora PostgreSQL  ──DMS (full load, then CDC)──►  S3 raw/orders/   (Parquet, Op column)
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
| **Raw kept as landed** | replayable: a bug in silver is fixed by re-running from raw |
| **Iceberg silver with `MERGE`** | row-level corrections; atomic publish; time travel for "what did the report say yesterday?" |
| **Merge-on-read + nightly compaction** | hourly upserts stay cheap; reads stay fast |
| **Glue jobs**, not EMR | modest volume, no platform team; move to EMR Serverless if cost or tuning demands it |
| **Idempotent `MERGE` keyed on `order_id`** | a retried run produces the same table |
| **Partition gold by date** | the morning query reads one day |
| **Workgroup scan limit** | one careless `SELECT *` cannot become the month's bill |

Failure modes worth naming unprompted:

- **DMS task stops** → the PostgreSQL slot retains WAL ([§6-4](#6-4-cdc-prerequisites-and-the-replication-slot-trap-)); alarm on lag and disk.
- **Out-of-order changes** → dedupe by the source commit timestamp / LSN, not by arrival time.
- **Schema change upstream** → DMS carries added columns; the `MERGE` lists columns explicitly,
  and Iceberg evolves the table by column id.
- **Compaction vs `MERGE` conflict** → compaction scoped to partitions older than today.

---

## 9) Interview Toolkit ⭐⭐⭐⭐⭐

### 9-1) Quick Q → A

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

### 9-2) "Our Athena queries are slow and the bill doubled. What do you do?"

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

### 9-3) Service cheat table

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
