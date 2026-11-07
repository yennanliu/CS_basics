# Apache Flink — Data Engineering Interview Deep Dive

> **Scope** — Apache Flink as a data engineering interview probes the engine itself: the runtime (JobManager, TaskManagers, slots, chaining), deployment, the DataStream and Table / SQL APIs, watermarks and timers in code, state backends, checkpoint and savepoint configuration, exactly-once with Kafka, Flink SQL joins and CDC, backpressure, memory, operations and the common failures — answered for Flink 1.20, with Flink 2.x differences called out. Engine-neutral streaming theory (why stream, window types, late-data strategies, the engine comparison) is [`stream_pipeline.md`](./stream_pipeline.md)'s.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the high-level map and last-minute checklist across all DE topics; [`./stream_pipeline.md`](./stream_pipeline.md) — time semantics, windows, state and exactly-once as concepts; [`./kafka.md`](./kafka.md) — the log Flink usually reads from and writes to; [`./spark.md`](./spark.md) — the batch engine it is most often compared with; [`../flink/faq_flink.md`](../flink/faq_flink.md) — the older Flink notes with diagrams.

Every answer leads with the sentence you would say out loud, then the mechanism, then the
trade-off the interviewer pushes on next. Configuration names are Flink 1.20's; where 1.20
renamed a key, the old name still works in 1.x and is noted.

---

## 1) What Flink Is, and Where It Fits ⭐⭐⭐⭐

### 1-1) The one-paragraph answer

**Flink is a distributed stream processor: it runs a dataflow of operators that process
events one at a time, keeps per-key state next to the operators, and makes that state
fault-tolerant with periodic distributed snapshots called checkpoints.**

- **Streaming first** — a batch job is a *bounded* stream; the same runtime runs both.
- **Stateful** — state is a first-class, partitioned, checkpointed part of the job, and can
  grow to terabytes on the RocksDB backend.
- **Event time** — watermarks, event-time timers and late-data handling are built in, so
  results depend on when events happened, not when they arrived.
- **Exactly-once state** — after a failure, every operator's state is restored to one
  consistent checkpoint and the sources rewind to the matching offsets.

### 1-2) Flink vs Spark

**Flink processes record by record with millisecond latency and large managed state; Spark
Structured Streaming runs a micro-batch every trigger and is the better fit when the team
already runs Spark for batch.**

| | Flink | Spark Structured Streaming |
|---|---|---|
| Execution | long-running operators, record at a time | a small batch job per trigger |
| Typical latency | milliseconds | ~100 ms to seconds (continuous mode is experimental) |
| State | keyed state on heap or RocksDB, rescalable, savepoints | state store per micro-batch (RocksDB provider available) |
| Event time | watermarks per source partition, timers, side outputs | watermarks for aggregations and joins; no user timers outside `flatMapGroupsWithState` / `transformWithState` |
| Custom logic | `KeyedProcessFunction` with state and timers | arbitrary stateful operators, more limited |
| Batch | supported, but Spark is the stronger batch engine | the strongest batch engine of the two |
| Upgrade with state | savepoint → new jar → restore | checkpoint compatibility depends on query changes |

The interview line: **"Flink when latency, event time or state size is the hard part;
Spark when the hard part is batch volume and the streaming is a few aggregations on the
side."** The four-engine table (with Kafka Streams and Beam) is
[`stream_pipeline.md` §9-1](./stream_pipeline.md#9-1-comparison).

### 1-3) When not to use Flink

- **Minute-level freshness is enough** — a scheduled batch job or a micro-batch is cheaper to
  run and to debug.
- **Kafka in, Kafka out, small state** — Kafka Streams runs inside the service, with no
  cluster to operate.
- **No one to run it** — a Flink job is a long-running distributed system: checkpoints,
  savepoints, upgrades and on-call. Use a managed service (Amazon Managed Service for Apache
  Flink, Confluent Cloud for Apache Flink, Ververica) or a simpler tool.

---

## 2) Architecture and the Runtime ⭐⭐⭐⭐⭐

### 2-1) JobManager, TaskManagers and slots

**The JobManager plans and coordinates the job, TaskManagers run it, and each TaskManager
offers a fixed number of task slots — a slot is the unit a parallel slice of the job is
scheduled into.**

| Component | Role |
|---|---|
| **Client** | compiles your `main()` into a JobGraph and submits it (in application mode this happens on the JobManager) |
| **JobManager** | one per job or cluster: the *Dispatcher* (accepts jobs, serves the Web UI), the *ResourceManager* (asks YARN / Kubernetes for TaskManagers), and one *JobMaster* per job (schedules tasks, triggers checkpoints, handles failover) |
| **TaskManager** | a JVM worker; runs subtasks in threads, holds their state, exchanges data with other TaskManagers over the network |
| **Task slot** | a fixed share of a TaskManager's managed memory; CPU is shared between slots |
| **Subtask** | one parallel instance of a (chained) operator; runs in one thread |

```text
                ┌─────────────── JobManager ───────────────┐
                │ Dispatcher · ResourceManager · JobMaster │
                │ checkpoint coordinator · scheduler       │
                └───────┬──────────────────────┬───────────┘
       deploy subtasks  │                      │  trigger checkpoints
            ┌───────────▼──────────┐   ┌───────▼──────────────┐
            │ TaskManager 1        │   │ TaskManager 2        │
            │ ┌──────┐ ┌──────┐    │   │ ┌──────┐ ┌──────┐    │
            │ │slot 1│ │slot 2│    │   │ │slot 1│ │slot 2│    │
            │ └──────┘ └──────┘    │   │ └──────┘ └──────┘    │
            │ state · net buffers  │◄─►│ state · net buffers  │
            └──────────────────────┘   └──────────────────────┘
```

- `taskmanager.numberOfTaskSlots` (default 1) sets slots per TaskManager; a common choice is
  one slot per CPU core.
- The JobManager is the single coordinator; production runs it with **high availability**
  (ZooKeeper or Kubernetes HA services), so a new JobManager takes over and restores the job
  from the latest completed checkpoint.

### 2-2) From program to running tasks

**Your code becomes a StreamGraph, which is optimised into a JobGraph by chaining
operators, which the JobManager expands into an ExecutionGraph with one vertex per
parallel subtask.**

```text
StreamGraph      every operator in your program
    │ chain operators that can run in one thread
JobGraph         what is submitted: chained vertices + data exchanges
    │ expand each vertex by its parallelism
ExecutionGraph   one execution vertex per subtask, kept by the JobMaster
    │ deploy
physical tasks   subtasks running in TaskManager slots
```

**Operator chaining** fuses neighbouring operators into one task when the connection is
one-to-one (`forward`) and the parallelism matches — `source → map → filter` becomes one
thread, with no serialisation or network hop between them. A `keyBy`, `rebalance` or a
parallelism change breaks the chain. `disableChaining()` / `startNewChain()` exist to split a
heavy operator out so the Web UI shows which one is slow.

### 2-3) Parallelism, slots and slot sharing

**By default, one slot can hold one subtask of *every* operator in the job, so a job needs as
many slots as its highest operator parallelism — not the sum.**

```text
job: source(p=4) → keyBy → window(p=4) → sink(p=2)

slot sharing on (default):          4 slots
  slot 1: source-1, window-1, sink-1
  slot 2: source-2, window-2, sink-2
  slot 3: source-3, window-3
  slot 4: source-4, window-4
```

- Parallelism is set per operator (`.setParallelism(n)`), per job (`-p`, `parallelism.default`)
  or not at all (default 1). Per-operator wins.
- **Match the source to the Kafka partitions**: a source with parallelism above the partition
  count leaves subtasks idle — and an idle subtask holds back the watermark (§4-2).
- **Max parallelism** (`pipeline.max-parallelism`) is the number of *key groups* — the
  upper bound you can ever rescale to. Set it once, before the first production deploy (§5-4).

### 2-4) Deployment modes

**Production jobs run in application mode — one cluster per job, `main()` run on the
JobManager — usually on Kubernetes through the Flink Kubernetes Operator.**

| Mode | Cluster lifetime | Isolation | Use for |
|---|---|---|---|
| **Application** | one cluster per application, torn down when it ends | full | production jobs |
| **Session** | long-running; many jobs share its TaskManagers | none — one bad job can take the others down | SQL gateway, notebooks, many small jobs |
| **Per-job** | one cluster per job, `main()` on the client | full | deprecated in 1.15, removed in 2.0 — use application mode |

Resource providers: Kubernetes (native, or the Operator), YARN, or standalone. With the
Operator a job is a Kubernetes resource:

```yaml
apiVersion: flink.apache.org/v1beta1
kind: FlinkDeployment
metadata:
  name: order-agg
spec:
  image: registry.example.com/order-agg:1.4.0
  flinkVersion: v1_20
  serviceAccount: flink
  flinkConfiguration:
    taskmanager.numberOfTaskSlots: "4"
    state.backend.type: rocksdb
    execution.checkpointing.interval: "60s"
    execution.checkpointing.incremental: "true"
    execution.checkpointing.dir: s3://flink/checkpoints/order-agg
  jobManager:
    resource: { memory: "2048m", cpu: 1 }
  taskManager:
    resource: { memory: "8192m", cpu: 4 }
  job:
    jarURI: local:///opt/flink/usrlib/order-agg.jar
    parallelism: 16
    upgradeMode: savepoint     # on a spec change: stop with a savepoint, redeploy, restore
```

---

## 3) The APIs ⭐⭐⭐⭐

### 3-1) The comparison

**Use SQL / Table API by default; drop to the DataStream API when you need custom state,
timers or control the planner cannot express.**

| API | Level | Use when |
|---|---|---|
| **Flink SQL** | declarative | filters, joins, windowed and running aggregations, CDC pipelines; analysts can own it |
| **Table API** | declarative, in Java / Python | the same plans as SQL, built in code |
| **DataStream API** | operators, state, timers | custom per-key logic: fraud rules, sessionisation with special rules, async enrichment |
| **ProcessFunction** | the lowest DataStream level | direct access to state, event-time timers and side outputs |
| **DataSet API** | batch only | legacy: deprecated in 1.18, **removed in 2.0** — batch runs on DataStream or SQL in batch mode |

- **PyFlink** wraps the Table API and the DataStream API; Python UDFs run in a separate Python
  process, so prefer built-in functions in hot paths, as with PySpark.
- **Scala**: the Scala APIs were removed in 2.0; Scala users call the Java API.
- The APIs mix: a `Table` converts to a `DataStream` and back (`toChangelogStream`,
  `fromChangelogStream`).

### 3-2) A minimal DataStream job

**A job is: environment → source with a watermark strategy → transformations → sink →
`execute()`. Give every stateful operator a `uid`.**

```java
// java
// Per-customer order totals over 1-minute event-time windows, Kafka → Kafka.
StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
env.enableCheckpointing(60_000);   // every 60 s; exactly-once is the default mode

KafkaSource<Order> source = KafkaSource.<Order>builder()
    .setBootstrapServers("kafka:9092")
    .setTopics("orders")
    .setGroupId("order-agg")
    .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.EARLIEST))
    .setValueOnlyDeserializer(new OrderDeserializer())
    .build();

WatermarkStrategy<Order> watermarks = WatermarkStrategy
    .<Order>forBoundedOutOfOrderness(Duration.ofSeconds(10))   // events up to 10 s late are on time
    .withTimestampAssigner((order, kafkaTs) -> order.eventTimeMillis)
    .withIdleness(Duration.ofMinutes(1));                       // an empty partition stops holding the watermark

env.fromSource(source, watermarks, "orders").uid("orders-source")
    .keyBy(order -> order.customerId)
    .window(TumblingEventTimeWindows.of(Duration.ofMinutes(1)))
    .aggregate(new SumAmount())          // incremental: one accumulator per key and window
    .uid("customer-1m-total")
    .sinkTo(totalsSink).uid("totals-sink");

env.execute("order-agg");
```

- `aggregate` / `reduce` keep one accumulator per window; `process` on a window buffers every
  element until the window fires — use it only when the logic needs all of them.
- `Duration`-based window and lateness methods are 1.19+; earlier versions take `Time.minutes(1)`.

### 3-3) `KeyedProcessFunction` and timers

**A `KeyedProcessFunction` sees each event for one key with that key's state, and can set
timers that call it back at an event time or processing time — that is how you express
"if X has not happened within N minutes".**

```java
// java
// Alert when an order is not paid within 15 minutes of being created (event time).
public class UnpaidOrderAlert extends KeyedProcessFunction<String, OrderEvent, String> {
    private transient ValueState<Long> deadline;   // when the pending timer fires; null = none

    @Override
    public void open(OpenContext ctx) {            // 1.19+; open(Configuration) before, removed in 2.0
        deadline = getRuntimeContext().getState(
            new ValueStateDescriptor<>("deadline", Long.class));
    }

    @Override
    public void processElement(OrderEvent e, Context ctx, Collector<String> out) throws Exception {
        if (e.type == EventType.CREATED) {
            long fireAt = e.eventTimeMillis + Duration.ofMinutes(15).toMillis();
            deadline.update(fireAt);
            ctx.timerService().registerEventTimeTimer(fireAt);
        } else if (e.type == EventType.PAID && deadline.value() != null) {
            ctx.timerService().deleteEventTimeTimer(deadline.value());
            deadline.clear();                      // paid in time: nothing to alert
        }
    }

    @Override
    public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
        out.collect("order " + ctx.getCurrentKey() + " unpaid after 15 minutes");
        deadline.clear();
    }
}
```

- An **event-time** timer fires when the watermark passes it, so it replays correctly from a
  checkpoint or a backfill; a **processing-time** timer fires on the wall clock.
- Timers are keyed, deduplicated per key and timestamp, and stored in the checkpoint.
- Traps: `PAID` arriving before `CREATED` (out of order) leaves an alert that should never
  fire — store the early `PAID` in state too; and a key that never gets its closing event keeps
  its state forever unless a timer or a TTL clears it.

### 3-4) Async I/O and side outputs

**Enrich from an external service with async I/O, never a blocking call in `map`; route
records that do not belong in the main output to a side output instead of dropping them.**

```java
// java
// Up to 100 lookups in flight per subtask; a lookup slower than 500 ms times out.
DataStream<EnrichedOrder> enriched = AsyncDataStream.unorderedWait(
    orders, new CustomerLookup(), 500, TimeUnit.MILLISECONDS, 100);

// Late events go to a side output instead of being silently dropped.
OutputTag<Order> lateTag = new OutputTag<Order>("late-orders") {};
SingleOutputStreamOperator<Total> totals = orders
    .keyBy(order -> order.customerId)
    .window(TumblingEventTimeWindows.of(Duration.ofMinutes(1)))
    .allowedLateness(Duration.ofMinutes(5))
    .sideOutputLateData(lateTag)
    .aggregate(new SumAmount());
DataStream<Order> lateOrders = totals.getSideOutput(lateTag);
```

- A blocking call in `map` makes the whole subtask wait on the network — throughput collapses
  and backpressure spreads upstream.
- `unorderedWait` emits results as they complete (order kept only across watermarks);
  `orderedWait` keeps input order at the cost of latency.
- Async I/O state is checkpointed: in-flight requests are re-sent after a restore, so the
  lookup must be safe to repeat.

---

## 4) Watermarks and Late Data in Flink ⭐⭐⭐⭐⭐

The concepts — event vs processing time, what a watermark promises, the late-data strategies —
are [`stream_pipeline.md` §3](./stream_pipeline.md#3-time-semantics-and-watermarks-)
and [§4-3](./stream_pipeline.md#4-3-late-data-strategies). This section is how Flink implements them.

### 4-1) Where watermarks come from

**Watermarks are generated in the source, per source split (per Kafka partition), and an
operator's watermark is the *minimum* of its inputs' watermarks.**

- `forBoundedOutOfOrderness(d)` emits `max event time seen − d − 1 ms`;
  `forMonotonousTimestamps()` is the special case `d = 0`.
- They are emitted periodically, every `pipeline.auto-watermark-interval` (default 200 ms),
  not per record.
- Generate watermarks **in the source** (`fromSource(..., strategy, ...)`), not with
  `assignTimestampsAndWatermarks` after it: per-partition watermarks are only possible inside
  the source, and a later assigner sees partitions already interleaved.
- In SQL, the same thing is a column declaration:
  `WATERMARK FOR order_time AS order_time - INTERVAL '10' SECOND`.

### 4-2) Idle partitions and watermark alignment

**One partition with no data holds every downstream watermark back, and one partition far
ahead of the rest makes state grow — `withIdleness` fixes the first, watermark alignment the
second.**

| Problem | Symptom | Fix |
|---|---|---|
| An empty partition, or source parallelism above the partition count | windows never fire; the watermark is stuck at `Long.MIN_VALUE` in the Web UI | `.withIdleness(Duration.ofMinutes(1))`; in SQL `table.exec.source.idle-timeout` |
| One partition replaying a backlog while others are live | window state grows, then the job OOMs on catch-up | `.withWatermarkAlignment("orders", Duration.ofSeconds(20))` pauses splits that run too far ahead |

Idleness has a cost: when the idle partition wakes up, its first events may already be late.

### 4-3) Late events in the DataStream API

**A window fires when the watermark passes its end; `allowedLateness` keeps it open for
updates, and anything later still goes to a side output.**

| Event arrives… | What happens |
|---|---|
| before the watermark passes the window end | counted normally |
| after, but within `allowedLateness` | the window fires **again** with an updated result — the sink must accept updates |
| after `allowedLateness` | dropped, unless `sideOutputLateData` routes it out (§3-4) |

`allowedLateness` keeps every window's state for that much longer, so it costs memory in
proportion to the key count.

---

## 5) State and State Backends ⭐⭐⭐⭐⭐

### 5-1) Kinds of state

**Keyed state is scoped to the current key after a `keyBy` and is what almost all jobs use;
operator state belongs to a parallel subtask, and broadcast state is the same copy on every
subtask.**

| State | Scope | Typical use |
|---|---|---|
| `ValueState<T>` | key | last value, a flag, a deadline |
| `ListState<T>` | key | buffered events |
| `MapState<K, V>` | key | per-key lookup; on RocksDB each entry is its own row, so it scales better than a map inside `ValueState` |
| `ReducingState` / `AggregatingState` | key | running aggregate |
| Operator `ListState` | subtask | source offsets, buffered sink records |
| Broadcast state | every subtask, same contents | rules or config pushed to all instances |

Keyed vs operator state and the broadcast pattern in depth are
[`stream_pipeline.md` §5-1](./stream_pipeline.md#5-1-keyed-state-vs-operator-state) and
[§7-4](./stream_pipeline.md#7-4-broadcast-state).

### 5-2) HashMap vs RocksDB

**HashMap keeps state as Java objects on the heap — fastest, but bounded by memory; RocksDB
keeps it serialised on local disk — slower per access, but sized by disk and the only choice
for incremental checkpoints.**

| | `hashmap` (default) | `rocksdb` |
|---|---|---|
| Where state lives | JVM heap, as objects | local disk (RocksDB), managed memory as its cache |
| Access cost | an object lookup | serialise + native call on every read and write |
| Size limit | the heap; large heaps mean long GC pauses | the local disk — terabytes |
| Incremental checkpoints | no — full snapshot every time | yes: only new SST files are uploaded |
| Pick when | state of a few GB per TaskManager | large state, or a checkpoint that takes too long |

- Set with `state.backend.type: hashmap | rocksdb`. Where checkpoints go is separate:
  `execution.checkpointing.dir` (`state.checkpoints.dir` before 1.20), always durable storage
  (S3, GCS, HDFS) in production.
- On RocksDB, keep state **values** small and use `MapState` over a large `ValueState<Map>` —
  every access deserialises the whole value.
- Flink 2.0 adds **ForSt**, an experimental disaggregated backend that keeps state in remote
  storage, so a TaskManager's local disk stops being the limit.

### 5-3) Keeping state bounded: TTL

**Every piece of keyed state needs a way to die — a timer that clears it, or a TTL — or the
job's state grows with every key it has ever seen.**

```java
// java
StateTtlConfig ttl = StateTtlConfig
    .newBuilder(Duration.ofHours(24))                               // 1.x before 1.19: Time.hours(24)
    .setUpdateType(StateTtlConfig.UpdateType.OnCreateAndWrite)      // reading does not extend it
    .setStateVisibility(StateTtlConfig.StateVisibility.NeverReturnExpired)
    .build();

ValueStateDescriptor<Long> lastSeen = new ValueStateDescriptor<>("last-seen", Long.class);
lastSeen.enableTimeToLive(ttl);
```

- TTL is **processing time** only, so a backfill does not expire state the way live traffic does.
- Expired entries are removed lazily on access, during a full snapshot, or by RocksDB compaction
  — memory does not drop the moment TTL passes.
- In SQL, `table.exec.state.ttl` (or the `STATE_TTL` hint, 1.18+, per operator) bounds join and
  aggregate state; without it a regular join keeps both sides forever (§8-3).

### 5-4) Key groups and rescaling

**Keyed state is partitioned into a fixed number of key groups — the max parallelism — and
rescaling moves whole key groups between subtasks; that is why max parallelism can never be
changed without losing state.**

```text
max parallelism 128  →  128 key groups;  key → hash(key) % 128

parallelism 4:   subtask 0: groups 0-31   subtask 1: 32-63   subtask 2: 64-95   subtask 3: 96-127
rescale to 8:    each subtask takes 16 groups — state moves by whole group, no key is rehashed
```

- The default max parallelism is derived from the first deploy's parallelism (at least 128,
  at most 32768), so a job first deployed at a different parallelism can get a different value.
  Set `pipeline.max-parallelism` explicitly.
- Parallelism above max parallelism is impossible; max parallelism far above parallelism adds a
  little overhead per key group. A power of two at least a few times the expected peak is typical.

---

## 6) Checkpoints, Savepoints and Recovery ⭐⭐⭐⭐⭐

### 6-1) What a checkpoint is

**A checkpoint is a consistent snapshot of every operator's state plus the source offsets,
taken without stopping the job: the JobManager injects barriers at the sources, and each
operator snapshots its state when the barrier passes.**

On failure Flink restarts the affected tasks, restores state from the last **completed**
checkpoint and rewinds the sources to its offsets — so state reflects every record exactly
once, even though some records are *processed* twice. The barrier mechanism, aligned vs
unaligned checkpoints, and checkpoint vs savepoint are
[`stream_pipeline.md` §6-1 to §6-3](./stream_pipeline.md#6-1-checkpoints-distributed-snapshots).

For the Kafka source, **the offsets that matter are the ones in the checkpoint**. Flink commits
offsets back to Kafka only when a checkpoint completes, and only so that lag dashboards work;
recovery never reads them.

### 6-2) The checkpoint settings worth knowing by name

| Key (1.20) | Default | What to set and why |
|---|---|---|
| `execution.checkpointing.interval` | unset — checkpointing **off** | 30 s – 5 min; it bounds replay on failure and, with transactional sinks, output latency |
| `execution.checkpointing.mode` | `EXACTLY_ONCE` | `AT_LEAST_ONCE` skips barrier alignment — lower latency, duplicates in state after a failure |
| `execution.checkpointing.timeout` | 10 min | raise for large state; a timed-out checkpoint counts as failed |
| `execution.checkpointing.min-pause` | 0 | a pause after each checkpoint so the job is not checkpointing all the time |
| `execution.checkpointing.tolerable-failed-checkpoints` | unset (0) | allow a few, so one slow upload does not restart the job |
| `execution.checkpointing.unaligned.enabled` | false | on, when backpressure makes aligned checkpoints time out |
| `execution.checkpointing.incremental` | false | on with RocksDB (`state.backend.incremental` before 1.20) |
| `execution.checkpointing.externalized-checkpoint-retention` | delete on cancel | `RETAIN_ON_CANCELLATION`, so a cancelled job can still restore |

### 6-3) Savepoints and upgrading a job

**A savepoint is a checkpoint you trigger and own: stop the job with one, deploy the new jar,
start it from the savepoint — state carries over if every stateful operator kept its `uid`.**

```bash
# Stop with a savepoint (drains in-flight data cleanly), then resume the new version from it.
./bin/flink stop --savepointPath s3://flink/savepoints/order-agg <jobId>
./bin/flink run -d -s s3://flink/savepoints/order-agg/savepoint-3f2a1b-6c8d1e0f9a7b target/order-agg-1.5.jar
```

| Change | Restores? |
|---|---|
| New parallelism (≤ max parallelism) | yes — key groups are redistributed |
| New operator added | yes — it starts with empty state |
| Stateful operator removed | only with `--allowNonRestoredState` (`-n`), which drops its state |
| Operator without an explicit `uid`, after any change to the graph | **no** — its generated id changed, so its state is orphaned |
| A field added to a POJO / Avro state type | yes — both support schema evolution |
| A Kryo-serialised state type changed | usually no — avoid generic types in state |
| Max parallelism changed | no |

The rule to say out loud: **"`uid()` on every stateful operator, from the first deploy."**
Savepoints are portable across backends (canonical format); checkpoints are not guaranteed to be.

### 6-4) Restarts, failover regions and HA

**When a task fails, Flink restarts only the tasks connected to it by pipelined data — the
failover region — and restores them from the last checkpoint, retrying under a restart
strategy.**

| Setting | Default | Note |
|---|---|---|
| `restart-strategy.type` | `exponential-delay` when checkpointing is on (since 1.19); `none` when off | `fixed-delay` / `failure-rate` also exist; `none` fails the job on the first error |
| `jobmanager.execution.failover-strategy` | `region` | a streaming job with `keyBy` everywhere is usually one region, so in practice the whole job restarts |
| `high-availability.type` | none | `kubernetes` or `zookeeper` in production, with `high-availability.storageDir` on durable storage |

A restart loop — the job restoring, failing on the same record, restoring again — shows as a
climbing `numRestarts` metric. That needs a code fix (or a dead-letter output), not more retries.

---

## 7) Exactly-Once End to End with Kafka ⭐⭐⭐⭐⭐

### 7-1) What Flink guarantees, and what the sink adds

**Flink's checkpoints make *state* exactly-once; output is exactly-once only if the sink is
idempotent or commits transactionally on the checkpoint.**

| Sink behaviour | Result after a failure | Example |
|---|---|---|
| plain append | duplicates — records since the last checkpoint are written again | Kafka sink with `AT_LEAST_ONCE`, a plain JDBC insert |
| idempotent (upsert by key) | effectively once — the replay overwrites the same rows | JDBC upsert, Elasticsearch with a document id, Cassandra |
| transactional, two-phase commit | exactly once — output is committed only when the checkpoint completes | Kafka sink with `EXACTLY_ONCE`, the file sink |

How two-phase commit lines up with checkpoints is
[`stream_pipeline.md` §6-5](./stream_pipeline.md#6-5-two-phase-commit-sinks).

### 7-2) The Kafka sink, configured for exactly-once

```java
// java
KafkaSink<String> totalsSink = KafkaSink.<String>builder()
    .setBootstrapServers("kafka:9092")
    .setRecordSerializer(KafkaRecordSerializationSchema.builder()
        .setTopic("order-totals")
        .setValueSerializationSchema(new SimpleStringSchema())
        .build())
    .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
    .setTransactionalIdPrefix("order-agg")          // unique per job on the cluster
    .setProperty("transaction.timeout.ms", "900000") // 15 min: must not exceed the broker's max
    .build();
```

The traps, each of which an interviewer will ask about:

- **Transaction timeout.** The sink's default `transaction.timeout.ms` is 1 hour; the broker's
  `transaction.max.timeout.ms` defaults to 15 minutes, so the job fails at start until one of
  the two is changed. The timeout must also exceed the longest expected outage plus checkpoint
  interval, or the broker aborts a transaction Flink still means to commit — data loss.
- **Readers must use `isolation.level=read_committed`.** A default (`read_uncommitted`) consumer
  sees records from aborted transactions — the duplicates you paid to avoid.
- **Latency equals the checkpoint interval.** Downstream readers see a transaction's records
  only when it commits, at the end of a checkpoint; a 1-minute interval means output arrives
  in 1-minute steps.
- **Unique `transactionalIdPrefix` per job.** Two jobs sharing a prefix fence each other's
  producers.

### 7-3) Other sinks

- **File sink (`FileSink`)** — writes in-progress part files and moves them to finished on
  checkpoint completion; with bulk formats (Parquet) it rolls a file on every checkpoint, so a
  short interval creates many small files.
- **Lakehouse tables** — the Iceberg, Paimon, Delta and Hudi Flink sinks commit a table snapshot
  per checkpoint; same trade-off between freshness and file count.
- **JDBC** — use an upsert statement keyed on the primary key, so a replay overwrites rather
  than duplicates; the JDBC sink also has an XA exactly-once mode, rarely worth its operational
  cost.

---

## 8) Flink SQL and the Table API ⭐⭐⭐⭐

### 8-1) Dynamic tables in one paragraph

**A Flink SQL query over a stream is a continuous query over a table that keeps changing; its
result is another changing table, emitted as a changelog.**

Append-only results (filters, windowed aggregations) can go to any sink; updating results
(a `GROUP BY` without a window, a regular join) emit updates and retractions and need a sink
with a primary key. The changelog row kinds (`+I`, `-U`, `+U`, `-D`) are
[`stream_pipeline.md` §9-2](./stream_pipeline.md#9-2-flink-datastream-and-table--sql), and a
windowed aggregation in SQL is [§10-1](./stream_pipeline.md#10-1-a-windowed-aggregation-in-flink-sql).

### 8-2) A running total, Kafka to upsert-Kafka

```sql
CREATE TABLE orders (
  order_id     STRING,
  customer_id  STRING,
  amount       DECIMAL(10, 2),
  order_time   TIMESTAMP(3),
  WATERMARK FOR order_time AS order_time - INTERVAL '10' SECOND
) WITH (
  'connector' = 'kafka',
  'topic' = 'orders',
  'properties.bootstrap.servers' = 'kafka:9092',
  'properties.group.id' = 'customer-totals',
  'scan.startup.mode' = 'group-offsets',
  'properties.auto.offset.reset' = 'earliest',
  'format' = 'json'
);

-- An updating result needs a sink with a primary key.
CREATE TABLE customer_totals (
  customer_id  STRING,
  order_count  BIGINT,
  total_amount DECIMAL(18, 2),
  PRIMARY KEY (customer_id) NOT ENFORCED
) WITH (
  'connector' = 'upsert-kafka',
  'topic' = 'customer-totals',
  'properties.bootstrap.servers' = 'kafka:9092',
  'key.format' = 'json',
  'value.format' = 'json'
);

INSERT INTO customer_totals
SELECT customer_id, COUNT(*), SUM(amount)
FROM orders
GROUP BY customer_id;
```

- The aggregate keeps one row of state per customer, forever, unless `table.exec.state.ttl` is
  set — and with a TTL, a customer idle longer than it restarts from zero.
- `upsert-kafka` writes the key as the Kafka message key and a delete as a tombstone, so a
  compacted topic holds the latest total per customer.
- The planner shows the changelog mode of every operator in `EXPLAIN CHANGELOG_MODE SELECT …`.

### 8-3) The four joins

**Pick the join by how long each side has to be kept: a regular join keeps both sides
forever; the other three bound the state.**

| Join | Syntax | State kept | Use for |
|---|---|---|---|
| **Regular** | `a JOIN b ON a.k = b.k` | both sides, forever (unless TTL) | small, bounded inputs; otherwise set a TTL |
| **Interval** | `… AND b.ts BETWEEN a.ts - INTERVAL '1' HOUR AND a.ts` | only the time range; cleaned by watermark | two event streams that happen close together: impression → click |
| **Temporal** | `JOIN rates FOR SYSTEM_TIME AS OF o.order_time` | the versions of the table still needed | enrich with the value *as of* the event: currency rate, price |
| **Lookup** | `JOIN customers FOR SYSTEM_TIME AS OF o.proc_time` | none in Flink (optional cache) | enrich from an external DB on each record; the DB must take the load |

The stream-join concepts are [`stream_pipeline.md` §7](./stream_pipeline.md#7-stream-joins-and-enrichment-).

### 8-4) Flink CDC

**Flink CDC reads a database's change log (MySQL binlog, PostgreSQL logical replication)
directly as a changelog stream — snapshot first, then the log — with no Debezium and no
Kafka in between.**

- The source takes a consistent initial snapshot in parallel chunks without locking the table,
  then switches to the log at the exact position the snapshot ended.
- A CDC table is an updating table, so it joins and aggregates like any other, and lands in an
  upsert sink — a common pattern is MySQL → Flink CDC → Paimon / Iceberg / Hudi.
- Flink CDC 3.x adds YAML pipelines that sync whole databases, including schema changes.
- With Kafka in the middle instead (Debezium → Kafka → Flink), read the topic with the
  `debezium-json` format — [`kafka.md`](./kafka.md) covers that side.

### 8-5) Tuning a SQL aggregation

| Setting | Effect |
|---|---|
| `table.exec.mini-batch.enabled` + `allow-latency` + `size` | buffers input and updates state once per batch — far fewer state accesses on RocksDB |
| `table.optimizer.agg-phase-strategy: TWO_PHASE` | local pre-aggregation before the shuffle; needs mini-batch; fixes a hot key in `GROUP BY` |
| `table.optimizer.distinct-agg.split.enabled` | splits `COUNT(DISTINCT …)` into two levels to spread a skewed distinct key |
| `table.exec.state.ttl` | bounds aggregate and join state |

---

## 9) Backpressure and Performance ⭐⭐⭐⭐

### 9-1) How data moves, and why backpressure is safe

**Subtasks exchange data in network buffers with credit-based flow control: a sender sends
only as many buffers as the receiver has announced room for, so a slow operator slows its
upstream down instead of running out of memory.**

- Backpressure in Flink is a symptom, not a failure: the slowest operator sets the throughput
  of the whole pipeline, and Kafka lag grows.
- It hurts checkpoints: barriers queue behind buffered data, so **aligned** checkpoints slow
  down or time out. Unaligned checkpoints (barriers overtake the buffers) and **buffer
  debloating** (`taskmanager.network.memory.buffer-debloat.enabled`, which shrinks in-flight
  data to about one second's worth) are the two fixes.

### 9-2) Finding the bottleneck

**In the Web UI, find the first operator that is *busy* while everything upstream of it is
*backpressured* — that operator is the bottleneck.**

| Metric (per subtask, ms per second) | Meaning |
|---|---|
| `busyTimeMsPerSecond` near 1000 | this operator is the slow one |
| `backPressuredTimeMsPerSecond` high | it is waiting on something downstream |
| `idleTimeMsPerSecond` high | it has nothing to do — starved upstream, or an idle partition |

Then ask why it is busy: one subtask busy while the others idle is **skew** (a hot key —
[`stream_pipeline.md` §8-3](./stream_pipeline.md#8-3-hot-keys)); all subtasks busy is too little
parallelism or expensive code — the Web UI's **flame graphs** (`rest.flamegraph.enabled`) show
which method; a busy sink is the external system's limit.

### 9-3) Tuning, in the order you would check

1. **Is it skew?** Compare records per subtask. Fix with pre-aggregation (two-phase, mini-batch)
   or a salted key, then a second aggregation.
2. **Is it a blocking call?** Replace synchronous lookups with async I/O (§3-4) or a cached
   lookup join.
3. **Is it state access?** On RocksDB, enable mini-batch in SQL, use `MapState`, keep values small,
   give RocksDB more managed memory.
4. **Is it serialisation?** Kryo fallback for a generic type is slow — use POJOs, Avro or
   registered types; `pipeline.generic-types: false` makes a Kryo fallback fail at start.
5. **Is it parallelism?** Raise it — up to the Kafka partition count for the source, up to max
   parallelism for keyed operators.
6. **Is it checkpointing?** Long or failing checkpoints: incremental RocksDB, unaligned
   checkpoints, a longer interval, `min-pause`.

---

## 10) Memory Model and Sizing ⭐⭐⭐

### 10-1) TaskManager memory

**Set the total (`taskmanager.memory.process.size`) and let Flink divide it; the pieces worth
knowing are the heap, managed memory (RocksDB's cache), and network buffers.**

| Component | Sized by | Used for |
|---|---|---|
| JVM heap (framework + task) | what remains | user code objects, `hashmap` state |
| **Managed memory** | `taskmanager.memory.managed.fraction` (0.4) | RocksDB block cache and write buffers, batch sorts and joins |
| **Network memory** | `taskmanager.memory.network.fraction` (0.1, between 64 MB and 1 GB) | buffers between subtasks |
| JVM metaspace | `taskmanager.memory.jvm-metaspace.size` (256 MB) | loaded classes |
| JVM overhead | `taskmanager.memory.jvm-overhead.fraction` (0.1) | threads, native libraries |

- With `hashmap` state, managed memory is mostly unused — lower the fraction and give the heap
  the room.
- On Kubernetes, a container killed with no Java `OutOfMemoryError` usually means RocksDB or
  another native allocation went past the limit: raise JVM overhead, or check
  `state.backend.rocksdb.memory.managed` (true by default) is still on.

### 10-2) Sizing from the workload

```text
Peak input       50,000 events/s, 1 KB each
One subtask      ~5,000 events/s through the heaviest operator (measure it; this is an assumption)
Parallelism      50,000 / 5,000 = 10, plus headroom for catch-up after an outage → 16
Kafka partitions ≥ 16, so every source subtask has a partition
Slots            16 → 4 TaskManagers × 4 slots
Max parallelism  128 — room to grow 8× without losing state
State            keys × bytes per key; above a few GB per TaskManager → RocksDB + incremental checkpoints
```

---

## 11) Operations ⭐⭐⭐⭐

### 11-1) What to monitor

| Metric | Why |
|---|---|
| Kafka consumer lag (`records-lag-max`, or lag from the committed offsets) | the first sign the job cannot keep up |
| `lastCheckpointDuration`, `lastCheckpointSize`, `numberOfFailedCheckpoints` | a checkpoint that grows or fails predicts a slow, painful recovery |
| `numRestarts` / uptime | a restart loop |
| `currentOutputWatermark` vs wall clock | event-time lag; a frozen watermark means an idle or stuck source |
| busy / backpressured time per operator | where the bottleneck is (§9-2) |
| heap, GC time, container memory | the OOM before it happens |

### 11-2) Scaling a running job

- **Manually** — stop with a savepoint, start from it with a new parallelism (≤ max parallelism).
- **Adaptive scheduler** (`jobmanager.scheduler: adaptive`) — the job uses whatever slots are
  available; with **reactive mode** in standalone application clusters, adding TaskManagers
  scales the job up.
- **Autoscaler** in the Kubernetes Operator (`job.autoscaler.enabled: "true"`) — measures each
  operator's busy time and target rate, then rescales per operator; Flink 1.18+ can apply the new
  parallelism in place, without a full redeploy.
- Rescaling cannot outrun the Kafka partition count for the source.

### 11-3) Reprocessing

To replay history after a bug fix: start a **new** job from the earliest offsets (or a
timestamp) writing to a new output, let it catch up, switch readers to it, then stop the old
one. Restoring the old savepoint with fixed code only fixes events still ahead of it. The
pattern is [`stream_pipeline.md` §12-3](./stream_pipeline.md#12-3-deploying-a-new-version-and-reprocessing).

---

## 12) Common Failures and Fixes ⭐⭐⭐⭐⭐

| Symptom | Likely cause | Fix |
|---|---|---|
| Windows never fire, output is empty | the watermark is not advancing: an idle partition, source parallelism above the partition count, or timestamps in seconds instead of ms | `withIdleness`; match source parallelism to partitions; check the timestamp assigner |
| Checkpoints time out under load | backpressure delays barrier alignment; or state too large for full snapshots | unaligned checkpoints, buffer debloating, RocksDB incremental, fix the bottleneck |
| State grows without bound | a regular SQL join or `GROUP BY` without TTL; keyed state never cleared; keys with unbounded cardinality | `table.exec.state.ttl`, timers or `StateTtlConfig`, interval / temporal joins |
| Job fails at start with a Kafka `InvalidTxnTimeoutException` | the sink's 1 h transaction timeout exceeds the broker's 15 min maximum | set `transaction.timeout.ms` ≤ the broker's `transaction.max.timeout.ms` |
| Duplicates downstream despite `EXACTLY_ONCE` | consumers read with `read_uncommitted`; or a non-transactional sink | `isolation.level=read_committed`; an idempotent upsert sink |
| Savepoint restore fails: "cannot map operator state" | an operator's generated id changed because it had no `uid` | set `uid`s; restore once with `--allowNonRestoredState` if losing that state is acceptable |
| Restart loop on one bad record | deserialisation or a code exception on a poison message | catch and route to a dead-letter side output; a deserialiser that returns null for bad input |
| TaskManager killed by Kubernetes, no `OutOfMemoryError` in the log | native memory (RocksDB) above the container limit | raise JVM overhead / process size; keep RocksDB on managed memory |
| One subtask at 100% busy, the rest idle | a hot key | two-phase aggregation, salting, mini-batch |
| `ClassNotFoundException` / `NoSuchMethodError` at runtime | connector or dependency version clash with Flink's classpath | shade the jar; match connector versions to the Flink version |

---

## 13) Frequently Asked Questions ⭐⭐⭐⭐⭐

### 13-1) Quick-fire answers

| Question | Answer to say out loud |
|---|---|
| What makes Flink "true streaming"? | operators run continuously and pass each record on as soon as it is processed; no micro-batches |
| How does Flink get exactly-once? | checkpoints snapshot all state plus source offsets consistently; on failure everything rewinds to that snapshot; a transactional or idempotent sink extends it to the output |
| Checkpoint vs savepoint? | a checkpoint is automatic, owned by Flink, for recovery; a savepoint is triggered by you, owned by you, for upgrades, rescaling and migration |
| What is a task slot? | a fixed share of a TaskManager's managed memory that one parallel slice of the job runs in; with slot sharing, a job needs as many slots as its highest parallelism |
| What is operator chaining? | fusing one-to-one operators with the same parallelism into one thread, removing serialisation between them |
| HashMap or RocksDB? | HashMap for small state and low latency; RocksDB for large state and incremental checkpoints |
| Why set a `uid` on every operator? | savepoint state is matched to operators by id; a generated id changes when the graph changes, orphaning the state |
| What is max parallelism? | the number of key groups — the ceiling for rescaling, fixed for the life of the state |
| Why does an idle Kafka partition stop my windows? | an operator's watermark is the minimum of its inputs; an empty partition never advances, so neither does the minimum |
| How do you handle late data? | bounded out-of-orderness in the watermark, `allowedLateness` for updates, a side output for the rest |
| What is backpressure and how do you find its cause? | a slow operator slowing its upstream through credit-based flow control; find the busy operator just downstream of the backpressured ones |
| Regular vs interval vs temporal vs lookup join? | regular keeps both sides forever; interval keeps a time range; temporal joins against a versioned table as of event time; lookup queries an external store per record |
| Why is my Kafka output delayed by a minute? | an exactly-once sink commits per checkpoint; output latency equals the checkpoint interval |
| What happens when a TaskManager dies? | the JobManager restarts the affected failover region on other slots and restores from the last completed checkpoint; sources rewind to its offsets |
| What changed in Flink 2.0? | DataSet API, Scala APIs, per-job mode and the legacy `SourceFunction` / `SinkFunction` removed; `config.yaml` replaces `flink-conf.yaml`; ForSt disaggregated state introduced |
| Flink or Spark Structured Streaming? | Flink for millisecond latency, complex event-time logic and large state; Spark when the team already runs Spark and seconds of latency are fine |

### 13-2) Design question: "Detect card fraud in real time"

**Prompt** — card transactions arrive on Kafka at 20,000 per second; flag a card that makes
more than 5 transactions in 1 minute, or two transactions more than 500 km apart within 10
minutes; alerts must reach a downstream service within seconds.

```text
Kafka: transactions (64 partitions, key = card_id)
   │
Flink job (application mode, Kubernetes Operator, parallelism 32, max parallelism 256)
   ├─ KafkaSource, watermark = event time − 5 s, idleness 1 min
   ├─ keyBy(card_id)
   ├─ KeyedProcessFunction
   │     state: recent transactions (ListState, pruned by event-time timer after 10 min)
   │     rule 1: count in the last 1 min > 5
   │     rule 2: distance / time to the previous transaction too large
   ├─ broadcast state: rule thresholds, updated from a config topic without a redeploy
   └─ sinks: alerts → Kafka (EXACTLY_ONCE, checkpoint every 10 s)
             late events → side output → lake table for audit
   │
Fraud service consumes alerts with read_committed
```

Points to make, in roughly the order an interviewer pushes:

- **Why `KeyedProcessFunction`, not a window** — rule 2 compares each transaction with the
  previous one, which a fixed window cannot express; the 10-minute timer keeps state bounded.
- **Latency vs exactly-once** — the alert topic only sees committed records, so the checkpoint
  interval (10 s) is the latency floor. If seconds are too slow, use `AT_LEAST_ONCE` and
  deduplicate alerts by transaction id downstream.
- **State size** — active cards × a few recent transactions: small per key, but many keys →
  RocksDB with incremental checkpoints.
- **Hot keys** — card ids are well distributed; a merchant-level rule would not be, and would
  need pre-aggregation.
- **Changing rules** — broadcast state, so thresholds change without a savepoint and redeploy;
  a change to the *logic* is a savepoint upgrade (§6-3).
- **Failure** — a TaskManager loss restores from the last checkpoint and replays at most 10 s of
  input; idempotent alert ids make any duplicate harmless.
