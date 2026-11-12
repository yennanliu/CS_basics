# AWS Data Services: Choosing Between Them

> **Scope** — the service-selection round of a senior data engineering interview on AWS: which service owns which layer, and how to choose and defend one against its look-alikes — S3 vs EBS vs EFS, RDS vs DynamoDB vs Redshift, Kinesis Data Streams vs Firehose vs MSK, SQS vs SNS vs EventBridge vs Kinesis, Glue vs EMR vs Lambda, Athena vs Redshift, Step Functions vs MWAA vs EventBridge, the Glue Data Catalog vs Lake Formation and the layers that decide data access, what to monitor and what drives cost per layer, three classic architecture scenarios, and an answer structure for "why did you choose X?". The depth on Glue, EMR, Iceberg, Athena, RDS extraction, S3 + SQS + Lambda ingestion and S3 layout is [aws_data.md](./aws_data.md)'s; Kafka internals are [kafka.md](./kafka.md)'s; streaming semantics are [stream_pipeline.md](./stream_pipeline.md)'s.
> **See also**: [`./aws_data.md`](./aws_data.md) — the mechanism behind every Glue, EMR, Athena, Iceberg, RDS, SQS and Lambda answer here; [`./kafka.md`](./kafka.md) — what MSK runs; [`./stream_pipeline.md`](./stream_pipeline.md) — event time, watermarks, exactly-once; [`./sql_vs_nosql.md`](./sql_vs_nosql.md) — choosing a store, engine-neutral; [`../db/faq_redshift.md`](../db/faq_redshift.md) — the Redshift notes; [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist.

AWS has two or three services for almost every job, and the interview question is never "what
is Kinesis?" — it is **"why Kinesis and not Firehose, or MSK?"** So this file is organised by
decision, not by service: each section is one family of look-alikes, the table that separates
them, the tell that picks one, and the wrong thing people say about them. Learn the service names
last; learn the **trade-off** each one makes first.

---

## 0) The Short Answer ⭐⭐⭐⭐⭐

**"S3 is the lake; everything else is a choice about how data gets in, how it is transformed,
how it is queried and how it is run. I pick by the requirement — latency, volume, who consumes
it, how much control the team needs, and what it costs to operate — and I say what I would use
instead if that requirement changed."**

When asked *"why did you choose service X?"*, answer in six beats:

1. **Requirement** — the volume, latency, throughput and SLA that constrained it.
2. **Choice** — what X does that the requirement needs.
3. **Alternative** — the look-alike, and the tell that ruled it out.
4. **Trade-offs** — cost, operations, performance, reliability.
5. **Failure handling** — retries, duplicates, data loss, replay.
6. **Monitoring** — the metric that says the pipeline is healthy.

> "I would choose AWS Glue for a standard batch ETL workload because it gives managed Spark
> execution and integrates with S3 and the Glue Data Catalog, and compared with EMR it removes
> cluster management. If I needed control over the Spark runtime, dependencies or cluster
> configuration — or Spot pricing at a scale where it matters — I would move it to EMR. I would
> benchmark both on a representative day of data, comparing run time, resources and total cost,
> rather than assume either is faster."

---

## 1) The Map: Which Layer Each Service Owns ⭐⭐⭐⭐⭐

```text
 Sources      RDS / Aurora · DynamoDB · SaaS / APIs · application logs · Kafka
                 |
 Ingestion    Kinesis Data Streams · Firehose · MSK · DMS · SQS · AppFlow · Transfer Family
                 |
 Storage      S3 data lake:  raw (bronze)  ->  cleaned (silver)  ->  curated (gold)
                 |
 Processing   Glue ETL · EMR (Spark, Flink, Trino) · Lambda · Athena CTAS
                 |
 Serving      Redshift · Athena · OpenSearch · QuickSight · DynamoDB (lookups)

 Cross-cutting
   orchestration  Step Functions · MWAA (Airflow) · EventBridge (schedules, events)
   catalog        Glue Data Catalog              governance  Lake Formation
   monitoring     CloudWatch · CloudTrail        security    IAM · KMS · Secrets Manager · VPC
```

Every section below is one row of that picture with more than one candidate in it. The layer
that has only one answer — **S3 as the lake** — is the one to say first, and the one
[aws_data.md §8](./aws_data.md#8-laying-out-data-in-s3-) covers in depth.

---

## 2) Storage and Databases ⭐⭐⭐⭐⭐

### 2-1) S3 vs EBS vs EFS vs FSx ⭐⭐⭐⭐

**"S3 is object storage for the lake; EBS is a disk for one instance; EFS is a shared file
system; FSx is a specific file system I need for a specific reason."**

| Service | Kind | Scope | Pick it for | Not for |
|---|---|---|---|---|
| **S3** | object storage, HTTP API | regional, 11 nines durability | the data lake, raw files, Spark input and output, backups, exchange | a POSIX file system, in-place edits, a database |
| **EBS** | block storage | one Availability Zone, attached to an instance | EC2 / EMR node disks, a database's volume, shuffle and spill space | sharing between many instances, long-term lake storage |
| **EFS** | NFS file system | regional, many instances at once | shared files between EC2 / containers / Lambda | high-volume analytics data (S3 is far cheaper) |
| **FSx** | managed Lustre, Windows, NetApp ONTAP, OpenZFS | per file system | HPC / ML scratch (FSx for Lustre can link to an S3 bucket), lift-and-shift of an existing file system | anything plain S3 already does |

What S3 is, and is not:

- **Objects are immutable.** You replace an object, you do not edit it — which is why table
  formats like Iceberg write new files and swap a pointer ([aws_data.md §4-2](./aws_data.md#4-2-the-metadata-tree-and-the-commit-)).
- **Strongly consistent** for reads after writes and for listings (since Dec 2020), so the old
  "S3 is eventually consistent" caveat is out of date.
- **Small files are the classic failure.** Every file is a request to list, open and read, so a
  table of a million 10 KB files costs more and runs slower than the same data in a hundred
  large Parquet files; and request rates are per prefix (thousands of reads and writes per second
  each), so a single hot prefix throttles ([aws_data.md §8-2](./aws_data.md#8-2-cardinality-file-size-and-the-small-file-problem-)).
- **Storage classes and Lifecycle** move cold data to Infrequent Access and Glacier tiers;
  **Intelligent-Tiering** does it per object when the access pattern is unknown.

### 2-2) OLTP vs NoSQL vs OLAP ⭐⭐⭐⭐⭐

**Name the workload first; the service follows.**

| Service | Type | Fits | Limits |
|---|---|---|---|
| **RDS** | managed relational (PostgreSQL, MySQL, …) | transactions, relational data, a pipeline's source | one writer; not built for large scans |
| **Aurora** | cloud-native relational, storage shared across AZs | high-availability OLTP, many read replicas, zero-ETL to Redshift | cost; still OLTP |
| **DynamoDB** | key-value / document, serverless | high concurrency, single-digit-ms lookups by a known key | access patterns must be designed up front; no joins; scans are expensive |
| **Redshift** | MPP columnar warehouse | BI, large fact-to-dimension joins, repeated heavy analytics | not an OLTP store; row-at-a-time writes are slow |
| **ElastiCache** | in-memory (Redis / Valkey, Memcached) | caching, sessions, counters, leaderboards | memory cost; a cache, not the system of record |

### 2-3) RDS vs DynamoDB ⭐⭐⭐⭐⭐

| | RDS / Aurora | DynamoDB |
|---|---|---|
| Data model | tables, foreign keys, SQL | items addressed by partition key (+ sort key) |
| Schema | fixed, migrated | per item; only keys are fixed |
| Queries | anything SQL can express, including joins | get / query by key, plus secondary indexes; no joins |
| Scaling | vertical, plus read replicas | horizontal, managed, on demand or provisioned |
| Transactions | full ACID | ACID transactions across a bounded set of items |
| Change stream | binlog / WAL, read by DMS or Debezium | DynamoDB Streams, or Kinesis Data Streams for DynamoDB |
| Into the lake | CDC with DMS, snapshot export | export to S3 (needs point-in-time recovery), or the stream |

**The design answer splits the data by access pattern**, rather than putting everything in the
store that scales best:

- accounts and payments — **RDS / Aurora**, because they need transactions and joins;
- sessions and profile lookups by user id — **DynamoDB**, because the key is known and the
  latency matters;
- the full event history — **S3**, because it is large and append-only;
- analytics over all of it — **Athena** or **Redshift**.

> **Trap.** "DynamoDB scales, so put everything there." A query you did not design a key or
> index for becomes a full-table scan, and an ad hoc question becomes an export job. The data
> model and the access patterns decide, not the scaling story.

---

## 3) Streaming Ingestion: Kinesis Data Streams, Firehose or MSK ⭐⭐⭐⭐⭐

### 3-1) What each one is

- **Kinesis Data Streams (KDS)** — a durable, ordered **stream**. Producers write records with a
  partition key, records are ordered per **shard**, and consumers read at their own pace and can
  **re-read** within the retention period (24 hours by default, extendable to 365 days).
- **Amazon Data Firehose** — a managed **delivery** service. It buffers incoming records and
  writes them to a destination — S3, Redshift, OpenSearch, Splunk, HTTP endpoints, Iceberg
  tables — optionally transforming them with a Lambda and converting them to Parquet / ORC on
  the way. You write no consumer, and you cannot rewind it.
- **Amazon MSK** — managed **Apache Kafka** (provisioned brokers or MSK Serverless). Topics,
  partitions, consumer groups, Kafka Connect (MSK Connect), and the whole Kafka client
  ecosystem ([kafka.md](./kafka.md)).

### 3-2) The comparison ⭐⭐⭐⭐⭐

| | Kinesis Data Streams | Data Firehose | MSK |
|---|---|---|---|
| Role | stream platform | delivery to a sink | managed Kafka |
| Unit of scale | shard: 1 MB/s or 1,000 records/s in, 2 MB/s out | none to manage | broker size and count, partitions |
| Capacity modes | provisioned shards or on-demand | automatic | provisioned or serverless |
| Consumers | many; shared 2 MB/s per shard, or enhanced fan-out for 2 MB/s each | one destination per stream | many consumer groups |
| Replay | yes, within retention | no — it delivers and forgets | yes, within topic retention |
| Ordering | per shard (partition key) | not a guarantee to rely on | per partition |
| Transform | in your consumer (Lambda, Flink, Spark) | Lambda per batch, format conversion, dynamic partitioning | in your consumer, Kafka Streams, Flink |
| Operations | low to medium (shard sizing in provisioned mode) | lowest | medium to high (brokers, partitions, upgrades) |
| Sweet spot | real-time processing on AWS with several consumers | logs and events landed in S3 / Redshift / OpenSearch | an existing Kafka estate or the Kafka ecosystem |

### 3-3) How to choose ⭐⭐⭐⭐⭐

- **Only landing the data in S3 (or Redshift, OpenSearch)?** → **Firehose.** It batches, can
  write Parquet, partitions by a field in the record, and needs no code to run.
- **Several consumers, your own processing, replay?** → **Kinesis Data Streams**, read by Flink
  (Amazon Managed Service for Apache Flink), Spark Structured Streaming or Lambda. Add Firehose
  as one more consumer when you also want the raw stream in S3.
- **Already on Kafka, or need Kafka Connect, Kafka's semantics, or portability off AWS?** →
  **MSK.**

The latency question: KDS consumers see records in well under a second (around 200 ms with
shared-throughput polling, lower with enhanced fan-out). Firehose's latency is its **buffer**:
it flushes when the buffer reaches a size (1–128 MB) or an interval (0–900 s) — by default
5 MB or 300 s for S3 — so S3 objects arrive in seconds to minutes, not milliseconds.

**Traps:**

- *"Firehose is a cheaper Kafka."* It is a delivery pipe, not an event platform: no consumer
  groups, no offsets, no replay. When the destination fails it retries: for S3 until the
  source's retention runs out — 24 hours for Direct PUT, after which the data is lost — and for
  Redshift, OpenSearch, Splunk or HTTP for a configured duration, then backs the failed records
  up to an error prefix in S3. So alarm on delivery, not only on the error prefix.
- *"Kinesis and Kafka are the same thing."* Both are partitioned logs with many consumers and
  replay, but the scaling unit differs (a shard has fixed throughput; a Kafka partition is
  bounded by its broker), and so do retention limits, consumer APIs and the ecosystem.
- **Hot shard.** A low-cardinality partition key (a country, a constant) puts most records on
  one shard, which throttles (`WriteProvisionedThroughputExceeded`) while the others sit idle —
  the streaming version of data skew.
- **Duplicates.** Producer retries and consumer restarts both re-deliver, on all three; the sink
  has to be idempotent ([aws_data.md §7-5](./aws_data.md#7-5-duplicate-messages-and-idempotency-)).

---

## 4) Queues and Events: SQS, SNS, EventBridge or Kinesis ⭐⭐⭐⭐

### 4-1) The four shapes ⭐⭐⭐⭐⭐

**A queue hands each message to one worker; a topic copies it to every subscriber; an event bus
routes it by content; a stream keeps it, in order, for anyone to read.**

| Need | Service | Why |
|---|---|---|
| background jobs, buffering, decoupling a producer from a slow consumer | **SQS** | each message to one consumer, retries, DLQ |
| fan-out: one message to several subscribers | **SNS** (often SNS → several SQS queues) | push to many endpoints |
| routing events between services and SaaS by their content, schedules | **EventBridge** | rules match on fields and send to targets |
| high-volume ordered events, several readers, replay | **Kinesis Data Streams / MSK** | a retained, partitioned log |

The tells: *"each job processed once by one of N workers"* is SQS; *"every event re-readable by
a new consumer next month"* is a stream; *"when an S3 object lands or a Glue job fails, start X"*
is EventBridge.

### 4-2) SQS details the interviewer probes ⭐⭐⭐⭐

- **Standard queue** — nearly unlimited throughput, **at-least-once**, best-effort ordering:
  duplicates and reordering happen.
- **FIFO queue** — ordering **per message group**, deduplication within a 5-minute window, and
  lower throughput limits. Ordering across groups is not promised; one stuck group does not
  block the others.
- **Visibility timeout** — after a consumer receives a message it is hidden for this long; not
  deleted in time, it reappears and runs again. Size it to the processing time
  ([aws_data.md §7-4](./aws_data.md#7-4-consuming-sqs-with-lambda-)).
- **DLQ** — after `maxReceiveCount` receives, the message moves to a dead-letter queue, so one
  bad message stops blocking the rest; redrive it back once fixed
  ([aws_data.md §7-6](./aws_data.md#7-6-dead-letter-queues-and-replay-)).
- **Retention** — up to 14 days; a queue is a buffer, not an archive.

### 4-3) The other ingestion services ⭐⭐⭐

| Service | Does | Typical use |
|---|---|---|
| **AWS DMS** | full load + CDC from a database | RDS → S3, database → database migrations ([aws_data.md §6-3](./aws_data.md#6-3-getting-data-out-of-rds-)) |
| **Amazon AppFlow** | managed SaaS connectors | Salesforce, Zendesk, Google Analytics → S3 / Redshift |
| **AWS Transfer Family** | managed SFTP / FTPS / FTP into S3 | partners who deliver files |
| **AWS DataSync** | bulk copy between storage systems | on-premises NFS / HDFS → S3 |
| **API Gateway** | managed HTTP front door | events posted by external clients, usually into Kinesis, SQS or Lambda |
| **Snowball** | physical transfer device | petabytes the network cannot move in time |

---

## 5) Processing: Glue, EMR or Lambda ⭐⭐⭐⭐⭐

### 5-1) The three in one table

**"Glue to run Spark without a cluster, EMR to control the cluster, Lambda to react to events —
not to process the data."**

| | Glue ETL | EMR (EC2, EKS or Serverless) | Lambda |
|---|---|---|---|
| Role | serverless Spark / Python ETL | managed big-data frameworks: Spark, Hive, Trino, Flink, HBase | event-driven functions |
| Control | worker type and count, Spark conf | versions, instance types, configuration, bootstrap actions | memory, timeout |
| Large data | yes | yes | no: 15 minutes, 10 GB memory, no shuffle |
| Operations | low | medium to high on EC2; low on Serverless | lowest |
| Long or always-on work | per job, within its timeout | yes — long-running clusters, streaming | no |
| Billing | DPU-hours, per second | EC2 + EMR uplift (Spot possible), or vCPU / GB-seconds | requests + GB-seconds |

The full matrix, with Athena and Redshift in it, is [aws_data.md §1-2](./aws_data.md#1-2-choosing-the-compute-);
the Glue-or-EMR decision is [§3-4](./aws_data.md#3-4-glue-or-emr-); why Lambda starts the Spark
job rather than replacing it is [§7-1](./aws_data.md#7-1-why-lambda-and-spark-not-one-of-them-).

### 5-2) Choosing ⭐⭐⭐⭐⭐

| Situation | Pick |
|---|---|
| a standard daily Spark ETL | Glue, or EMR Serverless |
| a TB-scale Spark job | either — benchmark it on a representative day |
| a custom Spark build, non-Spark engines, unusual libraries | EMR |
| no cluster to manage, and Data Catalog integration | Glue |
| a long-running cluster, streaming, or an interactive Trino / Hive | EMR on EC2 |
| a short ETL with a loose start-time SLA | Glue Flex (spare capacity, cheaper, may start later) |
| per-file validation, routing, starting a job | Lambda |

**Trap: "Glue is slower" or "EMR is cheaper", stated as fact.** Speed and cost depend on the data
volume, the worker or instance types, job shape, startup time and how well the job is tuned.
What is true in general: EMR exposes more knobs and Spot pricing, which wins at sustained large
scale *if* someone tunes and operates it; Glue's per-DPU price buys away that operations work.
Say what you would measure, not which one wins.

### 5-3) The Spark follow-ups ⭐⭐⭐⭐

Choosing Glue or EMR moves the conversation to the engine underneath. Be ready for: reducing
shuffle, handling skew, sizing executors, avoiding small files, choosing the partition count,
reading the Spark UI for the slow stage, and making a failed job safe to rerun. All of them are
[spark.md](./spark.md)'s and [data_performance.md](./data_performance.md)'s; the rerun is
[batch_pipeline.md](./batch_pipeline.md)'s.

---

## 6) Querying: Athena or Redshift ⭐⭐⭐⭐⭐

### 6-1) The comparison

**"Athena queries the lake where it lies and bills for what it scans; Redshift is a warehouse
I load data into, so repeated heavy queries are fast and predictable."**

| | Athena | Redshift |
|---|---|---|
| Storage | files in S3, through the Glue Data Catalog | managed columnar storage (RA3), plus S3 through Spectrum |
| Infrastructure | serverless | provisioned clusters or Redshift Serverless |
| Billing | per TB scanned (or provisioned capacity) | node-hours, or RPU-hours on Serverless |
| Performance comes from | file format, partitions, file size, columns selected | distribution, sort keys, materialized views, workload management |
| Concurrency | per-account query limits | WLM queues, concurrency scaling |
| Sweet spot | ad hoc questions, exploring the lake, light CTAS transforms | dashboards, repeated large fact-to-dimension joins, a stable SLA |

**Choose Athena** when the data is already Parquet in S3, the queries are exploratory or
occasional, and nobody wants to run a warehouse. **Choose Redshift** when a BI workload runs the
same heavy joins all day, needs predictable latency under concurrency, or needs workload
management. A good answer often uses both: **Athena over the lake, Redshift for the gold
tables the dashboards hit** — and Redshift Spectrum to join the two.

### 6-2) Tuning each ⭐⭐⭐⭐

**Athena** — the bill and the latency have the same cause, bytes scanned
([aws_data.md §5-2](./aws_data.md#5-2-the-cost-model-is-the-performance-model-)):

- columnar, compressed files (Parquet / ORC);
- partition on the column queries filter by, so partitions are pruned;
- name the columns — no `SELECT *`;
- compact small files; use Iceberg when the table takes updates;
- a workgroup per team with a per-query scan limit.

**Redshift** — the layout decides how much data moves between nodes ([faq_redshift.md](../db/faq_redshift.md)):

- **distribution style**: `KEY` on the join column of two large tables, so matching rows sit on
  the same slice; `ALL` for small dimensions; `EVEN` when there is no good key; `AUTO` to let
  Redshift choose. A skewed `KEY` piles work on one slice;
- **sort key** on the column ranges are filtered by (usually a date), so blocks are skipped;
- **materialized views** for the aggregates dashboards repeat;
- **workload management** queues so an analyst's ad hoc query cannot starve the dashboards;
- `EXPLAIN` and the system views to find the step that redistributes or broadcasts data.

### 6-3) The rest of the analytics shelf ⭐⭐⭐

| Service | Does | Fits |
|---|---|---|
| **Redshift Spectrum** | Redshift SQL over S3 tables in the catalog | joining warehouse tables with lake data |
| **OpenSearch** | search index and log analytics | full-text search, log exploration, near-real-time dashboards |
| **QuickSight** | BI and dashboards | business-facing reports over Athena / Redshift |
| **Trino on EMR** | distributed SQL you operate | very large interactive SQL, custom connectors |

---

## 7) Orchestration: Step Functions, MWAA or EventBridge ⭐⭐⭐⭐

### 7-1) The comparison

**"Step Functions for a workflow of AWS service calls; MWAA when it is a data pipeline with
schedules, dependencies and backfills; EventBridge to start things when something happens."**

| | Step Functions | MWAA (managed Airflow) | EventBridge |
|---|---|---|---|
| Model | state machine (JSON / visual) | Python DAGs | event rules and schedules |
| Strong at | calling AWS services, branching, retries with backoff, parallel and map steps | dependencies across many tasks, schedules, `catchup` and backfills, sensors, a large operator library | reacting to events, cron-style schedules |
| Retries | per state, declarative | per task, in the operator | per target, with a DLQ |
| Waits for a job | built in (`.sync` integrations for Glue, EMR, Athena, ECS) | operators and sensors | no — it only starts the target |
| Operations | none, pay per transition | an always-on environment, Airflow upgrades | none |
| Fits | serverless pipelines made of AWS services | a team already on Airflow, hundreds of DAGs | the trigger in front of either |

### 7-2) Choosing ⭐⭐⭐⭐

- **A few AWS steps with branching and retries** (start a Glue job, wait, run Athena, notify) →
  **Step Functions.** Standard workflows run for up to a year and execute each step once per
  attempt — but a `Retry` policy re-runs a failed task, so the task's side effect (a write, a
  job start) must still be idempotent. Express workflows are for short, high-volume event
  processing.
- **Many interdependent pipelines, schedules, backfills of past dates, cross-system tasks** →
  **MWAA.** Backfilling a date range is what Airflow is built around; the price is an
  environment that runs, and costs, around the clock.
- **"Run when the file lands" or "every day at 02:00"** → **EventBridge** (rule or Scheduler)
  starting a state machine, a Glue job or a Lambda. It does not track a multi-step workflow.

Glue's own **workflows and triggers** also chain Glue jobs and crawlers; they are fine when every
step is Glue, and limiting the moment one is not.

---

## 8) Catalog, Governance and Access ⭐⭐⭐⭐⭐

### 8-1) The services

| Service | Owns | Value |
|---|---|---|
| **Glue Data Catalog** | metadata: databases, tables, schemas, partitions | one table definition for Athena, EMR, Glue and Redshift Spectrum |
| **Glue Crawler** | inferring a schema from files | exploration; declare schemas in production ([aws_data.md §2-3](./aws_data.md#2-3-crawlers-and-when-not-to-use-them-)) |
| **Lake Formation** | permissions on catalog resources | grants on databases, tables, columns, rows and cells; tag-based access (LF-tags) |
| **IAM** | identities and policies | which user, role or service may call which API |
| **KMS** | encryption keys | encryption at rest, and who may decrypt |
| **Secrets Manager** | credentials | database passwords and API keys out of code, with rotation |

**Catalog vs Lake Formation in one line:** the Data Catalog says **what the data is**; Lake
Formation says **who may see which part of it** — for the engines integrated with it (Athena,
Redshift Spectrum, EMR, Glue).

### 8-2) Why "I gave it IAM access" is not enough ⭐⭐⭐⭐⭐

A read from the lake succeeds only if **no** layer it crosses denies it **and** the grants that apply allow it:

```text
 caller's IAM policy        may this role call s3:GetObject / glue:GetTable / athena:...?
 S3 bucket policy           cross-account: must allow this principal; same account: an IAM allow
                            is enough, so the bucket policy restricts only by explicit Deny
 KMS key policy             may this role kms:Decrypt with the key the object was written with?
 Lake Formation grant       does the role hold SELECT on this table / column / row?
 Glue catalog policy        is the catalog resource shared with this account?
 VPC endpoint policy        does the endpoint the request goes through allow it?
 Organizations SCP          does the account's guardrail allow the action at all?
```

An explicit deny anywhere wins. In the same account, S3 grants access when **either** the
identity policy **or** the bucket policy allows it; across accounts **both** must. The usual
debugging miss is **KMS**: the role can list and get the object, and the read still fails with
an access error because it may not decrypt.

### 8-3) "How do you secure an S3 data lake?" ⭐⭐⭐⭐⭐

1. **IAM roles** per job and service, least privilege, scoped to their prefixes and tables.
2. **Bucket policies** with explicit `Deny` statements — for principals outside the expected
   set, requests without TLS, and requests not through the VPC endpoint. An allow-list alone does
   not restrict a same-account role whose IAM policy already grants access.
3. **Block Public Access** on, at the account level.
4. **SSE-KMS** for sensitive data, with key policies that name who may decrypt.
5. **Lake Formation** for table-, column- and row-level access, with LF-tags at scale.
6. **CloudTrail** (plus S3 data events) to audit who did what.
7. **Masking or tokenisation** of PII before it reaches the wide-access layers.

**Encryption is not access control.** Encryption protects the bytes if storage leaks;
IAM, bucket policies and Lake Formation decide who may read them. An interviewer listens for
whether you keep the two apart.

---

## 9) Monitoring and Reliability ⭐⭐⭐⭐

### 9-1) The services

| Service | Does | Data-engineering use |
|---|---|---|
| **CloudWatch** | metrics, logs, alarms, dashboards | job durations, queue age, iterator age, SLA alarms |
| **CloudTrail** | API audit log | who changed a resource or read a dataset |
| **X-Ray** | distributed tracing | following a request through Lambda and API Gateway |
| **SNS** | notifications | paging on a failed job or a breached alarm |
| **SQS DLQ** | failed-message storage | isolating poison messages for inspection and redrive |
| **AWS Config** | resource configuration history | compliance rules, detecting a bucket made public |

### 9-2) The metric per layer ⭐⭐⭐⭐⭐

| Layer | Watch | What it tells you |
|---|---|---|
| Kinesis | `GetRecords.IteratorAgeMilliseconds`, `WriteProvisionedThroughputExceeded` | consumers falling behind; producers throttled (a hot shard) |
| Firehose | `DeliveryToS3.DataFreshness`, delivery success | the age of the oldest record not yet delivered |
| SQS | `ApproximateAgeOfOldestMessage`, `ApproximateNumberOfMessagesVisible`, DLQ depth | a backlog building; messages failing for good |
| Spark (Glue / EMR) | job duration, stage and task time, shuffle, spill, GC | skew, under-sized partitions, memory pressure |
| S3 | object count, size, request errors (4xx / 5xx, `503 Slow Down`) | small files, throttled prefixes |
| Redshift | query duration, queue wait, disk use | WLM saturation, a bad distribution key |
| The pipeline | success rate, **data freshness**, row counts against expectation | whether the SLA was actually met |

**Alarm on the SLA, not only on errors**: a pipeline that silently processes nothing raises no
error. Freshness and volume checks catch it. The longer version is
[aws_data.md §9-2](./aws_data.md#9-2-monitoring-pipeline-health-).

---

## 10) Cost per Layer ⭐⭐⭐⭐

| Layer | What the bill follows | The lever |
|---|---|---|
| S3 | GB stored, requests, transfer | Lifecycle to colder tiers, Intelligent-Tiering, compression, fewer bigger files |
| Athena | bytes scanned | Parquet, partition pruning, no `SELECT *`, workgroup scan limits |
| Glue | DPU-hours | right-size workers, Flex for non-urgent jobs, no unnecessary runs, incremental loads |
| EMR | instance-hours | Spot on task nodes, managed scaling, transient clusters or Serverless instead of idle ones |
| Redshift | node- or RPU-hours | right-size, pause or Serverless for spiky use, WLM, materialized views |
| Kinesis | shard-hours (provisioned) or data volume (on-demand) | size shards to the real peak; on-demand for unpredictable traffic |
| Lambda | requests + GB-seconds | batch records per invocation, right-size memory, Graviton |
| The pipeline | recomputation | incremental processing instead of full reloads |

**The senior version of "use Spot instances."** Saying it is not the answer; the follow-ups are:
which work can be interrupted, where the checkpoint is, how a lost task retries, whether
reclaimed capacity stretches the run past the SLA, and whether the saving is worth that risk.
Cost, reliability and performance get traded together, never one at a time. More levers are in
[aws_data.md §9-3](./aws_data.md#9-3-watching-the-cost-).

---

## 11) Three Architecture Scenarios ⭐⭐⭐⭐⭐

### 11-1) A 1 TB daily batch ⭐⭐⭐⭐⭐

```text
 RDS / APIs / CSV / logs
        |
 S3 raw (bronze)  --Glue or EMR Spark-->  S3 silver  -->  S3 gold (Iceberg, Parquet)
                                                               |
                                               Athena (ad hoc)   Redshift (dashboards)
 orchestration: Step Functions or MWAA      monitoring: CloudWatch alarms on freshness
```

**Why:** S3 stores raw and processed data cheaply and keeps raw for reprocessing; Spark handles
the distributed transform; Parquet means a query reads only the columns it needs; partitions mean
it reads only the dates it needs; Redshift gives dashboards a stable SLA; Athena gives everyone
else the lake.

**The senior follow-ups — how do you make it faster and safe?**

- process **incrementally** (CDC, bookmarks, a watermark) instead of rescanning everything;
- partition on what queries filter by, and do not over-partition;
- compact small files;
- tune the Spark job's shuffle, joins and partition count;
- make reruns idempotent — `MERGE` on a key, or overwrite whole partitions — so a retry never
  duplicates rows;
- data-quality checks before publishing, a retry policy, and a backfill path.

The full worked version, RDS orders to an Iceberg lakehouse, is
[aws_data.md §11](./aws_data.md#11-worked-design-rds-orders-to-an-s3-lakehouse-).

### 11-2) Real-time application logs ⭐⭐⭐⭐

```text
 application logs / CloudWatch Logs
        |
 Kinesis Data Streams
        |---- Flink / Spark Structured Streaming ----> alerts, aggregates
        |---- Firehose ----> S3 (history, Parquet)  ----> Athena
        `---- Firehose ----> OpenSearch (log search)
```

**Why KDS:** several consumers read the same records, the stream can be replayed within its
retention, and the processing is real time. **If the only requirement is "logs into S3", Firehose
alone is simpler** — no stream, no Spark.

**"How do you handle duplicate events?"**

- give each event a stable `event_id` at the source;
- make processing idempotent, and deduplicate on `event_id` within a bounded window where needed;
- checkpoint the consumer's position so a restart resumes rather than starts over;
- remember that two sinks share no distributed transaction — writing to S3 and to OpenSearch can
  succeed for one and fail for the other, so each must tolerate a replay
  ([stream_pipeline.md](./stream_pipeline.md)).

**"And late-arriving data?"** — event-time windows with a watermark in the streaming job; in the
batch layer, a lookback that reprocesses the last few partitions.

### 11-3) RDS into the lake ⭐⭐⭐⭐⭐

```text
 Amazon RDS / Aurora  --DMS: full load, then CDC-->  S3 raw  --Glue / EMR MERGE-->  Iceberg  -->  Athena / Redshift
```

**Why DMS:** it does the initial full load and then streams changes from the database log, so the
source is never rescanned. The follow-ups, each answered in
[aws_data.md §6](./aws_data.md#6-amazon-rds-and-aurora-):

- the **initial snapshot** and the moment CDC starts from, so nothing falls in the gap;
- **deletes** — DMS marks them with `Op = 'D'`; the `MERGE` must apply them;
- **ordering** — apply changes by the source's change sequence, not by a timestamp that can tie;
- **duplicates** — the merge on the primary key makes a re-delivered change harmless;
- **schema evolution** — a new source column must not break the job;
- **replication lag** — alarm on it; on PostgreSQL a stalled consumer also fills the disk with
  WAL ([aws_data.md §6-4](./aws_data.md#6-4-cdc-prerequisites-and-the-replication-slot-trap-)).

---

## 12) Interview Toolkit ⭐⭐⭐⭐⭐

### 12-1) Service-selection table

| Need | First choice | Alternative, and when |
|---|---|---|
| data lake | S3 | — |
| batch ETL | Glue / EMR Serverless | Lambda for small per-file work |
| large or highly tuned Spark | EMR | Glue |
| real-time event stream | Kinesis Data Streams | MSK when on Kafka |
| logs → S3 | Firehose | KDS + a consumer when you also process them |
| database CDC | DMS | Debezium on MSK Connect |
| message queue | SQS | — |
| fan-out to subscribers | SNS | EventBridge for content routing |
| event routing / schedules | EventBridge | — |
| serverless SQL on the lake | Athena | Redshift Spectrum |
| data warehouse | Redshift | — |
| workflow orchestration | Step Functions / MWAA | EventBridge to trigger |
| metadata catalog | Glue Data Catalog | — |
| lake governance | Lake Formation | IAM + bucket policies, coarse-grained |
| low-latency key lookups | DynamoDB | ElastiCache in front |
| BI dashboards | QuickSight | — |
| search / log analytics | OpenSearch | — |
| monitoring | CloudWatch | — |
| encryption keys / secrets | KMS / Secrets Manager | — |

### 12-2) The fifteen questions to rehearse

| # | Question | The point to make |
|---|---|---|
| 1 | S3 or Redshift? | lake vs warehouse: cheap, open files vs fast, managed queries — usually both ([§6-1](#6-1-the-comparison)) |
| 2 | Glue or EMR? | serverless vs control; benchmark, do not assert ([§5-2](#5-2-choosing-)) |
| 3 | Kinesis or Firehose? | stream you process vs delivery you configure ([§3-3](#3-3-how-to-choose-)) |
| 4 | Kinesis or MSK? | AWS-native shards vs the Kafka ecosystem ([§3-2](#3-2-the-comparison-)) |
| 5 | SQS or Kinesis? | a message for one worker vs a retained log for many readers ([§4-1](#4-1-the-four-shapes-)) |
| 6 | Athena or Redshift? | ad hoc on S3, billed per scan vs a warehouse for repeated heavy queries ([§6-1](#6-1-the-comparison)) |
| 7 | Design a TB-scale batch pipeline | S3 layers, Spark, Parquet, partitions, idempotent reruns ([§11-1](#11-1-a-1-tb-daily-batch-)) |
| 8 | Design a real-time pipeline | KDS, a stream processor, checkpoints, two sinks ([§11-2](#11-2-real-time-application-logs-)) |
| 9 | Duplicate events? | stable ids, idempotent sinks, dedup windows ([aws_data.md §7-5](./aws_data.md#7-5-duplicate-messages-and-idempotency-)) |
| 10 | Late-arriving data? | watermarks in the stream, lookback in the batch ([stream_pipeline.md](./stream_pipeline.md)) |
| 11 | Failure recovery? | retries with backoff, checkpoints, DLQs, idempotent reruns |
| 12 | Cutting pipeline cost? | compute, storage and scan, each with its lever ([§10](#10-cost-per-layer-)) |
| 13 | Monitoring a Spark job? | Spark UI for the slow stage, CloudWatch for duration and freshness ([§9-2](#9-2-the-metric-per-layer-)) |
| 14 | Securing the lake? | IAM, bucket policy, KMS, Lake Formation, audit ([§8-3](#8-3-how-do-you-secure-an-s3-data-lake-)) |
| 15 | Incremental ETL? | CDC or a watermark in, `MERGE` out ([batch_pipeline.md](./batch_pipeline.md)) |

### 12-3) Things people say that are wrong

| Said | Correct it to |
|---|---|
| "S3 is eventually consistent" | strongly consistent for reads and lists since Dec 2020 |
| "Firehose writes every 5 MB or 15 minutes" | buffer size 1–128 MB and interval 0–900 s are configurable; the S3 default is 5 MB or 300 s |
| "Firehose can replace Kafka" | it delivers to a sink; no consumer groups, offsets or replay |
| "Glue is always more expensive / slower than EMR" | it depends on volume, sizing and tuning; measure both |
| "DynamoDB scales, so use it for everything" | only for access patterns designed into its keys |
| "IAM allows it, so the job can read it" | bucket policy, KMS, Lake Formation and SCPs all have to agree |
| "It's encrypted, so it's secure" | encryption protects the bytes; access control decides who reads them |
| "Use Spot to save money" | only for interruptible work with checkpoints and SLA slack |

### 12-4) Last-review checklist

**Storage and databases**

- [ ] S3 vs EBS vs EFS
- [ ] S3 partitioning and the small-file problem
- [ ] RDS vs DynamoDB vs Redshift

**Ingestion**

- [ ] Kinesis Data Streams vs Firehose vs MSK
- [ ] SQS vs SNS vs EventBridge vs Kinesis
- [ ] DMS full load vs CDC

**Processing**

- [ ] Glue vs EMR vs Lambda
- [ ] Spark driver, executors, shuffle and skew
- [ ] Parquet, compression and partitioning

**Analytics**

- [ ] Athena vs Redshift
- [ ] Redshift distribution and sort keys
- [ ] `EXPLAIN` and query tuning

**Architecture and operations**

- [ ] Step Functions vs MWAA
- [ ] Glue Data Catalog vs Lake Formation
- [ ] IAM, KMS and Secrets Manager
- [ ] CloudWatch, retries, DLQs and idempotency
- [ ] cost per layer

**Study order:** S3, Glue, EMR, Spark, Kinesis, Redshift and Athena first — they carry most of
an AWS system-design answer — then MWAA, Step Functions, DMS and Lake Formation.
