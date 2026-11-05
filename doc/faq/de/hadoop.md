# Hadoop Ecosystem Interview Deep-Dive

> **Scope** — the Hadoop ecosystem as a data engineer interview probes it: HDFS internals, MapReduce end to end, YARN, Hive, HBase, ZooKeeper, the satellite tools, file formats, and how the stack moved to object storage and the lakehouse. Spark itself is out of scope here.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`spark.md`](./spark.md) — the engine that replaced MapReduce; [`batch_pipeline.md`](./batch_pipeline.md) — file formats and table formats in a pipeline; [`sql_vs_nosql.md`](./sql_vs_nosql.md) — where HBase sits among NoSQL stores; the older notes [`../spark/faq_hadoop.md`](../spark/faq_hadoop.md) and [`../spark/faq_mapreduce.md`](../spark/faq_mapreduce.md).

Answers target **Apache Hadoop 3.x** and **Hive 3/4**. Each section leads with the sentence you
would say out loud, then the mechanism, then the trade-off an interviewer pushes on next.

---

## 1) The Big Picture ⭐⭐⭐

### 1-1) What Hadoop is, and its three layers

**Hadoop is a framework for storing and processing very large datasets on clusters of
commodity machines: HDFS stores the data, YARN allocates the cluster's resources, and
MapReduce (or Tez, Spark) computes on it.**

| Layer | Component | Job |
|---|---|---|
| Storage | **HDFS** | distributed, replicated file system |
| Resource management | **YARN** | schedules CPU and memory across applications |
| Compute | **MapReduce** (also Tez, Spark, Flink on YARN) | runs the processing |
| Common | **Hadoop Common** | shared libraries, RPC, configuration, security |

```text
        Hive   Pig   HBase   Spark   Tez   Flink   Oozie
   ─────────────────────────────────────────────────────
                YARN (ResourceManager / NodeManagers)
   ─────────────────────────────────────────────────────
                HDFS (NameNode / DataNodes)
   ─────────────────────────────────────────────────────
                commodity servers, local disks, racks
```

### 1-2) The design principles behind it

**Hadoop assumes hardware fails all the time, so it replicates data and retries work, and it
moves computation to the data instead of moving data to the computation.**

- **Failure is normal** — a disk or node failure is handled by replication and task retries,
  not by expensive hardware.
- **Data locality** — the scheduler prefers to run a task on the node (or at least the rack)
  that holds its block, because the network is the scarce resource.
- **Write once, read many** — files are appended, never updated in place, which keeps
  replication and consistency simple.
- **Throughput over latency** — built for large sequential scans, not for millisecond lookups
  (that is HBase's job).
- **Scale out** — add nodes to add both storage and compute.

---

## 2) HDFS ⭐⭐⭐⭐⭐

### 2-1) Architecture: NameNode, DataNodes, blocks

**HDFS splits each file into large blocks (128 MB by default), stores each block on several
DataNodes (three replicas by default), and keeps the whole namespace in one NameNode's
memory.**

| Component | Holds / does |
|---|---|
| **NameNode** | the namespace (directories, files, permissions) and the file → blocks map, in memory; persists the namespace as fsimage + edit log |
| **DataNode** | stores blocks as local files; sends heartbeats (every 3 s by default) and periodic block reports |
| **Secondary NameNode** | merges the edit log into the fsimage (checkpointing) in a non-HA cluster — **not** a hot standby |
| **Standby NameNode** | in an HA cluster, a live replica of the namespace that can take over, and does the checkpointing |
| **JournalNode** | in HA, stores the shared edit log (a quorum of usually 3 or 5) |

- The NameNode does **not** persist block locations; it rebuilds the block → DataNode map
  from block reports at startup. Until enough blocks are reported it stays in **safe mode**
  (read-only).
- Clients talk to the NameNode only for metadata; the data itself flows directly between
  clients and DataNodes, so the NameNode is not a data bottleneck.
- A DataNode that misses heartbeats for about 10 minutes is declared dead, and its blocks are
  re-replicated from the surviving copies.

### 2-2) fsimage, edit log and checkpointing

**The fsimage is a snapshot of the namespace and the edit log records every change since; a
checkpoint merges the two into a new fsimage so a restart does not replay days of edits.**

```text
 NameNode memory  ◄── load at startup ──  fsimage_N  +  edits_N..M  (replayed)
        │
        │ every change (create, rename, delete) is appended to the edit log first
        ▼
   edits_inprogress
        │
        ▼  checkpoint (Secondary NN or Standby NN)
   fsimage_N + edits  ──merge──►  fsimage_M  ──uploaded back to the NameNode
```

- Checkpoints are triggered by time (`dfs.namenode.checkpoint.period`, 1 hour by default) or
  by the number of uncheckpointed transactions (`dfs.namenode.checkpoint.txns`).
- Without checkpointing the edit log grows without bound and a NameNode restart can take hours.

### 2-3) High availability: JournalNodes and ZKFC

**HDFS HA runs an active and a standby NameNode that share the edit log through a quorum of
JournalNodes; a ZooKeeper Failover Controller on each NameNode detects failure and promotes
the standby.**

| Piece | Role |
|---|---|
| **Active NameNode** | serves all client requests, writes edits to the JournalNodes |
| **Standby NameNode** | tails the edits from the JournalNodes, keeps its namespace current, receives block reports too |
| **JournalNodes (QJM)** | an edit is committed once a majority acknowledges it; 2f + 1 nodes tolerate f failures |
| **ZKFC** | health-checks its local NameNode, holds an ephemeral lock in ZooKeeper, triggers failover |
| **Fencing** | makes sure the old active cannot keep writing — the QJM only accepts writes from the newest epoch |

```text
             ZooKeeper ensemble (lock: who is active)
               ▲                          ▲
             ZKFC                        ZKFC
               │                          │
   Active NameNode ──edits──► JournalNode ×3 ──tail──► Standby NameNode
               ▲                                          ▲
               └──────── block reports from every DataNode ┘
```

- The **split-brain** risk — two NameNodes both believing they are active — is what fencing
  and the QJM epoch number prevent.
- Hadoop 3 supports more than one standby NameNode.

### 2-4) Federation and router-based federation

**Federation scales the namespace horizontally: several independent NameNodes each own part
of the namespace, while all DataNodes store blocks for all of them.**

- Each NameNode manages a **namespace volume** and its own **block pool**; DataNodes register
  with every NameNode.
- HA and federation are orthogonal: HA removes the single point of failure, federation removes
  the single-NameNode memory limit. Large clusters use both.
- Clients see one tree through **ViewFs** (a client-side mount table) or **Router-based
  Federation** (a server-side router layer, Hadoop 2.9+).

### 2-5) The write pipeline

**A client asks the NameNode where to write, then streams the block through a pipeline of
DataNodes — each one stores the data and forwards it to the next — and acknowledgements flow
back up the chain.**

```text
client ──create()──► NameNode   (checks permissions, records the file)
client ◄── DataNodes for block 1: [DN1, DN4, DN5]

client ─packet─► DN1 ─packet─► DN4 ─packet─► DN5
client ◄──ack─── DN1 ◄──ack─── DN4 ◄──ack─── DN5

block full → ask NameNode for the next block's DataNodes → repeat
close()   → NameNode marks the file complete once the minimum replication is reached
```

- Data is sent in **packets** (64 KB by default) with checksums; the client keeps a data
  queue and an ack queue so a packet is only discarded after the whole pipeline acked it.
- **If a DataNode fails mid-write**, the pipeline is closed, unacked packets go back to the
  front of the queue, the failed node is removed, and writing continues on the remaining
  nodes. The NameNode notices the block is under-replicated and re-replicates it later.
- A file being written is visible to readers only up to the last completed block unless the
  writer calls `hflush()` / `hsync()`.

### 2-6) The read path

**A client gets the block locations from the NameNode, sorted by network distance, then reads
each block directly from the closest DataNode, verifying checksums as it goes.**

- Locality order: same node → same rack → another rack.
- On a checksum failure or a dead DataNode, the client reads the next replica and reports the
  corrupt block, which the NameNode then re-replicates from a good copy.
- **Short-circuit local reads** let a client on the same machine read the block file
  directly, bypassing the DataNode's TCP path.

### 2-7) Replication, rack awareness and erasure coding

**Default placement puts the first replica on the writer's node, the second on a node in a
different rack, and the third on another node in that second rack — so one rack can fail
without losing data, while only one copy crosses racks.**

| | 3× replication | Erasure coding (RS-6-3) |
|---|---|---|
| Storage overhead | 200% (3× raw) | 50% (1.5× raw) |
| Tolerates | 2 lost copies | 3 lost cells out of 9 |
| Write / read cost | cheap | CPU for encoding; reconstruction reads from 6 nodes |
| Data locality | yes | mostly lost — a block is striped across nodes |
| Use for | hot data, small files | cold / archival data |

```bash
# Hadoop 3: apply an erasure-coding policy to a cold directory
hdfs ec -enablePolicy -policy RS-6-3-1024k
hdfs ec -setPolicy   -path /data/archive -policy RS-6-3-1024k
hdfs ec -getPolicy   -path /data/archive

# change replication for an existing path
hdfs dfs -setrep -w 2 /data/tmp
```

- Rack awareness needs a topology script (`net.topology.script.file.name`); without one,
  every node is in `/default-rack`.
- The **balancer** (`hdfs balancer`) evens out disk usage across DataNodes after nodes are added.

### 2-8) The small-files problem ⭐⭐⭐⭐

**Every file, directory and block is an object in NameNode memory (commonly estimated at
~150 bytes each), so millions of small files exhaust the NameNode long before the disks fill
— and each small file also costs a map task.**

| Fix | How |
|---|---|
| Write bigger files | batch or buffer upstream; coalesce in Spark before writing |
| Compaction job | periodically rewrite a partition's small files into a few large ones |
| Container formats | pack records into SequenceFile / Avro / Parquet / ORC |
| **HAR** (Hadoop Archive) | `hadoop archive` bundles files into one archive; reduces NameNode objects, but reads are slower and archives are immutable |
| `CombineFileInputFormat` | lets one map task read many small files (helps compute, not NameNode memory) |
| Table format | Iceberg / Delta / Hudi compaction (`rewrite_data_files`, `OPTIMIZE`) |

### 2-9) Commands worth knowing

```bash
hdfs dfs -ls -h /data/events
hdfs dfs -put local.csv /data/raw/
hdfs dfs -du -s -h /data/events            # space used (before replication is counted in the first column)
hdfs fsck /data/events -files -blocks -locations   # block health and where each block lives
hdfs dfsadmin -report                      # capacity and live / dead DataNodes
hdfs dfsadmin -safemode get
hdfs haadmin -getServiceState nn1          # active or standby
```

---

## 3) MapReduce ⭐⭐⭐⭐

### 3-1) The flow end to end

**MapReduce reads input splits, maps each record to key-value pairs, sorts and shuffles them
so every value for a key reaches the same reducer, and reduces each key to the output — with
the intermediate data written to local disk between phases.**

```text
HDFS input
  │  InputFormat → splits (≈ one per block)
  ▼
MAP tasks        record → (k, v)          e.g. "a b a" → (a,1) (b,1) (a,1)
  │  in-memory buffer → partition → sort → [combiner] → spill to local disk → merge
  ▼
SHUFFLE          each reducer fetches its partition from every map output (HTTP)
  ▼
SORT / MERGE     reducer merges the fetched runs, grouped by key
  ▼
REDUCE tasks     (k, [v1, v2, ...]) → output   e.g. (a, [1,1]) → (a, 2)
  ▼
HDFS output      part-r-00000, part-r-00001, ...
```

### 3-2) Input splits vs HDFS blocks

**A block is how HDFS physically stores data; an input split is how MapReduce logically
divides the input among map tasks — usually one split per block, but they are not the same
thing.**

- Split size = `max(minSize, min(maxSize, blockSize))`, so by default a split equals a block.
- A record that crosses a block boundary is still read whole: the `RecordReader` for a text
  file skips the partial first line and reads past the end of its split to finish the last one.
- Number of map tasks = number of splits. Number of reduce tasks is configured
  (`mapreduce.job.reduces`, default 1).
- Non-splittable input (a single gzip file) gives **one** map task no matter how big it is.

### 3-3) Map side: buffer, partitioner, sort, spill, combiner

**Map output goes into a circular in-memory buffer; when it fills past a threshold it is
partitioned by reducer, sorted by key, optionally combined, and spilled to local disk, and
the spills are merged into one sorted file per map task.**

| Step | Detail | Config |
|---|---|---|
| Buffer | circular buffer per map task | `mapreduce.task.io.sort.mb` (100 MB default) |
| Spill threshold | a background thread spills when the buffer is this full | `mapreduce.map.sort.spill.percent` (0.80) |
| Partition | which reducer gets each key — `HashPartitioner` by default | custom `Partitioner` class |
| Sort | by key, within each partition | `SortComparator` |
| Combiner | a map-side reduce on each spill, cuts shuffle volume | `job.setCombinerClass(...)` |
| Merge | spills merged into one partitioned, sorted output file | `mapreduce.task.io.sort.factor` |

- The default partitioner is `(key.hashCode() & Integer.MAX_VALUE) % numReduceTasks`; a skewed
  key sends all its values to one reducer.
- A **combiner** must be safe to run zero, one or many times — fine for sum, count, max;
  wrong for average (combine `(sum, count)` pairs instead).

### 3-4) Shuffle and reduce side

**Each reducer copies its partition from every finished map task, merges the sorted runs, and
calls `reduce()` once per key with an iterator over that key's values.**

- **Copy phase** — reducers start fetching as soon as maps finish (controlled by
  `mapreduce.job.reduce.slowstart.completedmaps`), over HTTP from the NodeManager's shuffle
  handler.
- **Merge phase** — fetched segments are merged in memory and on disk into one sorted stream.
- **Reduce phase** — values for a key arrive together; output goes to HDFS through the
  `OutputFormat`, one file per reducer.
- **Secondary sort** — to get values sorted too, use a composite key, a partitioner and a
  grouping comparator on the natural key.

### 3-5) Word count in Java

```java
// java — Hadoop 3, org.apache.hadoop.mapreduce API
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

  // Map: (byte offset, line) -> (word, 1) for every word in the line
  public static class TokenMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private static final IntWritable ONE = new IntWritable(1);
    private final Text word = new Text();

    @Override
    protected void map(LongWritable offset, Text line, Context context)
        throws IOException, InterruptedException {
      for (String token : line.toString().split("\\s+")) {
        if (token.isEmpty()) {
          continue;
        }
        word.set(token);
        context.write(word, ONE);
      }
    }
  }

  // Reduce: (word, [1, 1, ...]) -> (word, total). Also used as the combiner.
  public static class SumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private final IntWritable total = new IntWritable();

    @Override
    protected void reduce(Text word, Iterable<IntWritable> counts, Context context)
        throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable count : counts) {
        sum += count.get();
      }
      total.set(sum);
      context.write(word, total);
    }
  }

  public static void main(String[] args) throws Exception {
    Job job = Job.getInstance(new Configuration(), "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenMapper.class);
    job.setCombinerClass(SumReducer.class);   // sum is associative, so this is safe
    job.setReducerClass(SumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));   // must not exist yet
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
```

```bash
hadoop jar wordcount.jar WordCount /data/books /out/wordcount
hdfs dfs -cat /out/wordcount/part-r-00000 | head
```

### 3-6) Fault tolerance, speculative execution and counters

**A failed task is retried on another node, a slow one can be raced by a speculative copy, and
the job only fails when a task exhausts its attempts.**

- **Task failure** — retried up to `mapreduce.map.maxattempts` / `mapreduce.reduce.maxattempts`
  (4 by default). Map output lives on local disk, so a lost node means its completed map tasks
  re-run too.
- **ApplicationMaster failure** — YARN restarts the AM (`mapreduce.am.max-attempts`), which can
  recover completed tasks from the job history.
- **Speculative execution** — when a task runs much slower than its peers, a duplicate attempt
  starts elsewhere; the first to finish wins and the other is killed. It helps with a bad
  node, **not with data skew** (the duplicate has the same oversized input), and it should be
  off for tasks with side effects.
- **Counters** — built-in (records in/out, spilled records, bytes read) and custom
  (`context.getCounter("quality", "bad_rows").increment(1)`); the first place to look when a
  job is slow or wrong.

### 3-7) Join patterns in MapReduce

| Pattern | How | When |
|---|---|---|
| **Reduce-side join** | mappers tag each record with its source; reducer joins on the key | both inputs large; always works; full shuffle |
| **Map-side (replicated) join** | small table shipped to every mapper via the distributed cache (`job.addCacheFile`), loaded into a hash map | one side fits in memory; no shuffle |
| **Sorted merge join** | both inputs pre-sorted and partitioned identically, merged in the map | inputs already bucketed the same way |

These are the same strategies Spark calls sort-merge join and broadcast hash join — see
[`spark.md`](./spark.md).

---

## 4) YARN ⭐⭐⭐⭐

### 4-1) Components

**YARN splits resource management from application logic: one ResourceManager arbitrates the
cluster, a NodeManager on each node runs containers, and each application brings its own
ApplicationMaster.**

| Component | Role |
|---|---|
| **ResourceManager** | the cluster authority: a **Scheduler** (allocates resources) and an **ApplicationsManager** (accepts jobs, starts and restarts AMs) |
| **NodeManager** | per node; launches containers, enforces their limits, reports node health and usage |
| **ApplicationMaster** | per application; negotiates containers from the RM and tells NMs what to run |
| **Container** | an allocation of memory and vcores on one node |
| **Timeline / History server** | keeps logs and metrics after an application ends |

- In Hadoop 1, the **JobTracker** did both scheduling and job tracking and only ran MapReduce;
  YARN (Hadoop 2) removed that bottleneck and let Spark, Tez and Flink share the cluster.
- The RM can run in HA (active / standby through ZooKeeper).

### 4-2) The life of an application

```text
1. client  ──submit app──►  ResourceManager
2. RM      ──allocate container #0──► NodeManager A  → launches the ApplicationMaster
3. AM      ──register; request N containers (with locality hints)──► RM
4. RM      ──grant containers on NM B, NM C──► AM
5. AM      ──launch tasks──► NM B, NM C       (tasks report progress to the AM)
6. AM      ──unregister──► RM; containers released; logs aggregated to HDFS
```

- Container requests carry **locality** preferences (node → rack → any); the scheduler may
  delay briefly to get a local container.
- Key sizes: `yarn.nodemanager.resource.memory-mb` / `cpu-vcores` (what a node offers),
  `yarn.scheduler.minimum-allocation-mb` / `maximum-allocation-mb` (container bounds).
- A container exceeding its memory limit is killed by the NodeManager — the classic
  "Container killed by YARN for exceeding memory limits" error in Spark, fixed by raising the
  executor memory overhead.

### 4-3) Schedulers

| Scheduler | Model | Use |
|---|---|---|
| **FIFO** | one queue, first come first served | testing only |
| **Capacity** (Apache Hadoop's default) | hierarchical queues, each with a guaranteed share; idle capacity can be borrowed; preemption reclaims it | multi-team clusters with SLAs |
| **Fair** | applications share resources evenly over time; weighted queues | interactive mixed workloads |

- **Node labels** pin queues to subsets of nodes (GPU nodes, high-memory nodes).
- **Preemption** kills containers in an over-capacity queue so another queue gets its guarantee.

---

## 5) Hive ⭐⭐⭐⭐

### 5-1) Architecture and the Metastore

**Hive puts SQL on top of files in HDFS or object storage: it compiles HiveQL into Tez (or
MapReduce) jobs, and its Metastore holds the table definitions that the rest of the ecosystem
also reads.**

| Component | Role |
|---|---|
| **HiveServer2** | JDBC / ODBC endpoint; clients connect with `beeline` |
| **Driver / compiler** | parses HiveQL, plans, optimises (Calcite CBO), produces a DAG |
| **Execution engine** | **Tez** (the default since Hive 2/3); MapReduce is deprecated; Hive-on-Spark was removed in Hive 4 |
| **Metastore (HMS)** | table schemas, partitions, locations, SerDes — stored in an RDBMS (MySQL / PostgreSQL), served over Thrift |
| **LLAP** | optional long-lived daemons with caching, for interactive queries |

- **Schema-on-read** — Hive does not validate data on load; the SerDe interprets the files
  when they are queried.
- The **Metastore outlived Hive as a query engine**: Spark SQL, Trino, Impala and Flink all use
  it as their catalog, and cloud catalogs (AWS Glue) speak its API.

### 5-2) Managed vs external tables

**Dropping a managed table deletes its data; dropping an external table deletes only the
metadata — use external tables for data that other tools also own.**

| | Managed (internal) | External |
|---|---|---|
| Data location | the Hive warehouse directory | any path you give in `LOCATION` |
| `DROP TABLE` | deletes metadata **and** data | deletes metadata only |
| ACID | in Hive 3 with ACID enabled, managed ORC tables are transactional | not transactional |
| Use for | tables Hive fully owns | raw landing zones, data shared with Spark / other jobs |

### 5-3) Partitions and buckets

**Partitions split a table into directories by a column's value so queries can skip them;
buckets hash rows into a fixed number of files by a column so joins and sampling can use
that layout.**

| | Partitioning | Bucketing |
|---|---|---|
| Layout | a directory per value: `.../dt=2026-11-01/` | N files per partition: `hash(col) % N` |
| Pick | low-cardinality, always-filtered columns (date) | high-cardinality join keys (`user_id`) |
| Gain | partition pruning | bucket map join, sort-merge bucket join, sampling |
| Trap | too many partitions → small files + Metastore load | the bucket count is fixed once data is written |

- **Static partition** — the value is named in the statement. **Dynamic partition** — taken
  from the last columns of the `SELECT` (`hive.exec.dynamic.partition.mode=nonstrict`).
- Files written to a partition directory by another tool are invisible until the partition is
  registered: `MSCK REPAIR TABLE t` or `ALTER TABLE t ADD PARTITION`.

### 5-4) HiveQL example

```sql
-- HiveQL (Hive 3): an external raw table and a managed, partitioned, bucketed ORC table
CREATE EXTERNAL TABLE IF NOT EXISTS raw_events (
  event_id   STRING,
  user_id    BIGINT,
  event_type STRING,
  event_ts   TIMESTAMP
)
PARTITIONED BY (dt STRING)
STORED AS PARQUET
LOCATION 'hdfs:///data/raw/events';

MSCK REPAIR TABLE raw_events;          -- register partitions already on storage

CREATE TABLE IF NOT EXISTS events_by_user (
  event_id   STRING,
  user_id    BIGINT,
  event_type STRING,
  event_ts   TIMESTAMP
)
PARTITIONED BY (dt STRING)
CLUSTERED BY (user_id) INTO 32 BUCKETS
STORED AS ORC;

SET hive.exec.dynamic.partition.mode=nonstrict;

-- dynamic partition insert: dt comes from the last SELECT column
INSERT OVERWRITE TABLE events_by_user PARTITION (dt)
SELECT event_id, user_id, event_type, event_ts, dt
FROM   raw_events
WHERE  dt >= '2026-11-01';
```

### 5-5) Making Hive queries fast

- **Partition pruning** — filter on the partition column directly.
- **Columnar formats** — ORC / Parquet with predicate pushdown and column pruning.
- **Map join** — `hive.auto.convert.join=true` broadcasts a small table instead of shuffling.
- **Vectorization** — `hive.vectorized.execution.enabled=true` processes batches of rows.
- **CBO** — `ANALYZE TABLE t COMPUTE STATISTICS FOR COLUMNS` so the Calcite optimiser can
  pick join order.
- **Skew** — `hive.optimize.skewjoin=true`, or handle the hot key separately.
- **Tez + LLAP** for interactive latency.

---

## 6) HBase ⭐⭐⭐

### 6-1) Data model

**HBase is a distributed, sorted map — row key → column family → column qualifier →
timestamp → value — built on HDFS for random, real-time reads and writes by key.**

```text
row key          column family "info"              column family "stats"
───────────────  ───────────────────────────────   ──────────────────────
user#00042       info:name = "Ada"   (ts 1730...)   stats:logins = 17
                 info:city = "Taipei"
user#00043       info:name = "Lin"                  stats:logins = 3
```

- Rows are **sorted by row key**, which makes range scans by prefix cheap.
- Column families are declared up front and stored separately; qualifiers are free-form, and
  empty cells cost nothing (sparse).
- Each cell keeps several **versions** by timestamp.
- **Atomic per row**; no multi-row transactions, no secondary indexes, no joins, no SQL
  (Apache Phoenix adds a SQL layer).

### 6-2) Architecture

| Component | Role |
|---|---|
| **Region** | a contiguous range of row keys of one table; split when it grows too large |
| **RegionServer** | serves a set of regions: reads, writes, flushes, compactions |
| **HMaster** | assigns regions to RegionServers, handles DDL and load balancing (not on the data path) |
| **ZooKeeper** | tracks live servers and the active master |
| **`hbase:meta`** | the table that maps row-key ranges to regions and servers |

A client finds `hbase:meta` through ZooKeeper, looks up the region for its key, caches that
location, and then talks to the RegionServer directly.

### 6-3) Write and read path

**A write goes to the write-ahead log and then to an in-memory MemStore, which is flushed to
immutable HFiles on HDFS; a read merges the MemStore, the block cache and the HFiles.**

```text
WRITE: client ─put─► RegionServer ─► WAL (HDFS, for recovery) ─► MemStore (sorted, in memory)
                                                             └─ full → flush → HFile on HDFS
READ:  MemStore  +  BlockCache  +  HFiles (Bloom filters skip files without the key) → merged
COMPACTION: minor = merge a few small HFiles; major = rewrite all HFiles of a store, drop deletes
```

- It is an **LSM-tree** design: fast sequential writes, reads that may touch several files —
  the same family as Cassandra and RocksDB.
- A delete writes a **tombstone**; the data is removed at major compaction.

### 6-4) Row key design ⭐⭐⭐⭐

**The row key is the only index, so design it from the access pattern — and never make it
monotonically increasing, or every write lands on the same region.**

| Problem | Fix |
|---|---|
| Timestamp or sequence prefix → one hot region | **salt** (`hash(id) % N` prefix) or **hash** the key |
| Need latest-first scans | append a reversed timestamp: `Long.MAX_VALUE - ts` |
| Need to scan by user | put `user_id` first: `<salt>#<user_id>#<reversed_ts>` |
| Long keys waste space | keys are stored in every cell — keep them short |
| Region hotspots at table creation | **pre-split** the table on the expected key ranges |

Salting spreads writes but turns a prefix scan into N parallel scans — that is the trade-off
to name.

---

## 7) ZooKeeper and the Rest of the Ecosystem ⭐⭐⭐

### 7-1) ZooKeeper

**ZooKeeper is a small, strongly consistent, replicated store for coordination data — leader
election, locks, configuration and membership — not for application data.**

- An **ensemble** of 2f + 1 servers tolerates f failures; writes go through the leader and are
  committed by a majority using the **ZAB** protocol.
- Data lives in **znodes**: persistent, **ephemeral** (deleted when the client's session dies —
  the basis of failure detection), and **sequential** (a counter suffix — the basis of locks
  and leader election).
- **Watches** notify a client once when a znode changes.
- Used by HDFS HA (ZKFC), YARN RM HA, HBase, Hive locks, and Kafka before KRaft.

### 7-2) Ingestion, workflow and query tools

| Tool | What it does | Status / modern equivalent |
|---|---|---|
| **Sqoop** | bulk import/export between RDBMS and HDFS / Hive, as map-only jobs | retired to the Apache Attic in 2021 → Spark JDBC, Debezium CDC, managed connectors |
| **Flume** | agent-based log collection into HDFS (source → channel → sink) | Kafka + Kafka Connect, Fluent Bit |
| **Oozie** | XML workflow (DAG) and coordinator (time / data triggers) scheduler | Airflow, Dagster |
| **Pig** | Pig Latin dataflow scripts compiled to MR / Tez | Spark |
| **Impala** | MPP SQL engine with long-running daemons, reads the Hive Metastore | Trino, Spark SQL, cloud warehouses |
| **Presto / Trino** | distributed SQL over many sources, including Hive tables | still current |
| **Ranger / Atlas** | fine-grained authorisation / metadata lineage | cloud IAM + catalogs (Unity Catalog, Lake Formation) |
| **Kerberos** | cluster authentication | still how secure Hadoop clusters authenticate |

---

## 8) File Formats and Compression ⭐⭐⭐⭐

**Use a columnar format (ORC or Parquet) for analytics, a row format (Avro) for ingestion and
messages, and pick compression knowing whether the file stays splittable.**

| Format | Layout | Schema | Splittable | Best for |
|---|---|---|---|---|
| Text / CSV | row | none | yes (uncompressed) | interchange, debugging |
| **SequenceFile** | row, binary key-value | Writable types | yes | MapReduce intermediates, packing small files |
| **Avro** | row, binary | embedded JSON schema | yes | ingestion, Kafka, schema evolution |
| **ORC** | columnar, stripes | embedded | yes | Hive, ACID tables |
| **Parquet** | columnar, row groups | embedded | yes | Spark, Trino, most engines |

| Codec | Splittable on its own | Note |
|---|---|---|
| gzip | no | good ratio; a big `.gz` file is one map task |
| bzip2 | yes | best ratio, slow |
| Snappy / LZ4 | no | fast; fine **inside** a container format (Parquet / ORC / Avro / SequenceFile), which stay splittable |
| LZO | with an index | needs a separate indexing step |
| Zstandard | no (raw) | strong ratio at good speed; common inside Parquet / ORC |

Columnar wins for analytics because a query reads only the columns it selects, similar values
compress well together, and min/max statistics per row group or stripe let the reader skip
data entirely.

---

## 9) From Hadoop to Cloud and the Lakehouse ⭐⭐⭐⭐

### 9-1) Separating storage and compute

**Modern stacks replace HDFS with object storage and long-lived clusters with elastic compute,
so storage and compute scale and are paid for independently.**

| | HDFS on a Hadoop cluster | Object storage (S3, GCS, ADLS) |
|---|---|---|
| Scaling | add nodes = storage + compute together | storage unlimited; compute separate |
| Cost | always-on nodes, 3× replication | pay per GB stored; durability managed by the provider |
| Locality | data-local tasks | none — data always comes over the network |
| Rename | atomic metadata operation | S3: copy + delete, not atomic (GCS / ADLS Gen2 differ) |
| Listing | fast, from NameNode memory | slower, paged API calls |

- Because an S3 rename is not atomic, the classic "write to a temp dir, rename on commit"
  output committer is slow and unsafe there; Hadoop's **S3A committers** (magic / staging)
  and table formats solve it.
- **Managed Hadoop** (EMR, Dataproc, HDInsight) runs YARN, Spark and Hive on demand over
  object storage.
- **Open table formats** — Iceberg, Delta Lake, Hudi — add a transaction log over Parquet
  files, giving ACID commits, `MERGE`, schema evolution, time travel and compaction. Together
  with a catalog (Hive Metastore, Glue, Unity, Polaris) that is the **lakehouse**.

### 9-2) Hadoop MapReduce vs Spark

**Spark replaced MapReduce as the compute engine because it keeps intermediate data in memory
across a whole DAG instead of writing to disk between every step — but Spark still commonly
runs on YARN and reads from HDFS.**

| | MapReduce | Spark |
|---|---|---|
| Model | two fixed phases per job; chain jobs for more | arbitrary DAG of stages |
| Intermediate data | local disk between map and reduce, HDFS between jobs | memory where possible, disk on spill / shuffle |
| Iterative work (ML, graph) | one job per iteration, re-reads input | caches the dataset across iterations |
| APIs | Java map / reduce | SQL, DataFrames, Python, Scala, streaming, MLlib |
| Fault tolerance | re-run failed tasks; outputs on disk | lineage recomputation |
| Still fits | very large, simple, disk-bound batch jobs on existing clusters | almost everything else |

---

## 10) Interview Q → A ⭐⭐⭐⭐

- **Why is the HDFS block so large?** To make seek time small relative to transfer time, and
  to keep the number of blocks — and NameNode memory — low.
- **What happens if the NameNode fails?** Without HA, HDFS is unavailable until it restarts
  from fsimage + edits. With HA, the ZKFC fails over to the standby, which already has the
  namespace and block locations.
- **Is the Secondary NameNode a backup?** No. It only checkpoints the edit log into the
  fsimage; it cannot serve clients.
- **How does HDFS detect corrupted data?** Checksums per chunk on write and read, plus a
  periodic DataNode block scanner; a corrupt replica is replaced from a good one.
- **Block vs input split?** Physical storage unit vs the logical unit of one map task; usually
  aligned, not the same.
- **Why can't a combiner compute an average directly?** It may run any number of times;
  average of averages is wrong. Emit `(sum, count)` and divide in the reducer.
- **How do you handle a skewed key in MapReduce?** A custom partitioner, salting the key in a
  first job and merging in a second, or a map-side join if the other side is small.
  Speculative execution does not help.
- **Managed vs external Hive table?** Drop deletes data vs only metadata.
- **Why are small files bad?** NameNode memory per object, plus one map task per file.
- **Where would you use HBase over Hive?** Random reads and writes by key with low latency
  (profiles, counters, time series by entity) vs large analytical scans.
- **What does YARN add over Hadoop 1?** It splits the JobTracker into a ResourceManager and
  per-application ApplicationMasters, so the cluster scales further and runs engines other
  than MapReduce.
- **Is Hadoop dead?** HDFS clusters and MapReduce are shrinking, but its ideas and parts are
  everywhere: YARN still runs Spark, the Hive Metastore is still the common catalog, Parquet
  and ORC came from that ecosystem, and object storage plus table formats are its successor.
