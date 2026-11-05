# Stream Data Pipelines — Interview Deep-Dive

> **Scope** — everything a data engineer interview asks about stream processing: when to stream, reference architectures, time semantics and watermarks, windows and late data, state and checkpoints, exactly-once end to end, stream joins, backpressure and scaling, the engines (Flink, Spark Structured Streaming, Kafka Streams, Beam), real-time serving and operations. Kafka broker internals are not here — they belong to `kafka.md`.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`./kafka.md`](./kafka.md) — the log underneath most streams; [`./batch_pipeline.md`](./batch_pipeline.md) — the batch side of the same pipeline; [`./spark.md`](./spark.md) — Spark internals; [`../flink/faq_flink.md`](../flink/faq_flink.md) and [`../stream/faq_stream.md`](../stream/faq_stream.md) — older notes.

Each section leads with the sentence you would say out loud, then the mechanism, then the
trade-off the interviewer pushes on next. Code targets **Flink 1.19+ / 2.x**, **Spark 3.5+**
and **Kafka Streams 3.x**; where a version matters it is called out.

---

## 1) Batch vs Stream ⭐⭐⭐⭐

### 1-1) When streaming is worth it

**Stream only when someone acts on the result faster than a batch could deliver it —
streaming buys latency and pays for it with always-on infrastructure and harder correctness.**

| | Batch | Stream |
|---|---|---|
| Input | bounded (a day, an hour) | unbounded |
| Latency | minutes–hours | milliseconds–seconds |
| Completeness | all data present when the job runs | data arrives late and out of order |
| State | rebuilt each run | kept for the life of the job |
| Reprocessing | rerun the partition | replay the log through a new job |
| Failure handling | retry the task | checkpoints, restarts, duplicate handling |
| Cost | pay per run | pay for always-on, stateful workers |

Good reasons to stream: fraud and abuse detection, alerting and monitoring, live
operational dashboards, personalisation features, CDC replication between systems.
Weak reasons: "the daily report should be real-time" when nobody looks at it more than once
a day — an hourly batch is cheaper and simpler.

**Interview tell**: ask "who reads this, and how stale can it be?" before choosing.

### 1-2) Micro-batch vs record-at-a-time

**Micro-batch engines collect a small batch of records and run a tiny batch job on it;
record-at-a-time engines push each record through the operators as it arrives.**

| | Micro-batch | Record-at-a-time |
|---|---|---|
| Examples | Spark Structured Streaming (default trigger) | Flink, Kafka Streams, Dataflow |
| Typical latency | ~100 ms to seconds, bounded below by the trigger | milliseconds |
| Throughput | high — batch overhead amortised | high |
| Fault tolerance | replay the failed micro-batch | distributed snapshots (checkpoints) |
| Mental model | a batch query run repeatedly | a dataflow graph that never stops |

Spark also has a **continuous processing** mode, but it is still marked experimental and
supports only map-like operations — do not offer it as the low-latency answer.

### 1-3) Lambda vs Kappa architecture

**Lambda runs a batch layer for accuracy and a speed layer for freshness and merges them;
Kappa runs only the stream and reprocesses by replaying the log.**

```text
Lambda                                      Kappa
                ┌─► batch layer ─┐                     ┌─► stream job v1 ─► serving
source ─► log ──┤                ├─► serve   source ─► log
                └─► speed layer ─┘                     └─► stream job v2 (replay) ─► serving
```

| | Lambda | Kappa |
|---|---|---|
| Codebases | two (batch + stream), must agree | one |
| Reprocessing | rerun the batch layer | replay the log into a new job version, switch over |
| Needs | batch storage + stream infra | long log retention (or tiered storage / an archive) |
| Weakness | logic drift between the two paths | replaying months of data through a stream job is slow |

Modern practice is Kappa-like: one engine that runs the same logic in batch and stream
(Flink, Spark, Beam), the raw stream archived in object storage or a lakehouse table, and
batch backfills reading that archive with the same code.

---

## 2) Reference Architectures ⭐⭐⭐⭐

### 2-1) The canonical pipeline

**Sources write events into a durable log, a stateful processor turns them into results,
and sinks serve those results to dashboards, services or the lakehouse.**

```text
 Apps / devices ─► producers ─┐
                              ├─► Kafka (raw topics) ─► Stream processor ─┬─► Kafka (derived topics)
 OLTP DB ─► CDC (Debezium) ───┘         │               (Flink / Spark /   ├─► Real-time OLAP (Pinot, Druid, ClickHouse)
                                        │                Kafka Streams)    ├─► Serving store (Redis, Cassandra, DynamoDB)
                                        │               state + checkpoints└─► Lakehouse table (Iceberg / Delta / Hudi)
                                        └─► archive to object storage (replay, batch backfill)
```

The pieces and why each exists:

- **Durable log** (Kafka, Kinesis, Pub/Sub) — decouples producers from consumers and makes
  the source **replayable**, which every recovery and reprocessing story depends on.
- **Schema Registry** — producers and consumers agree on the event shape; see `schema_design.md`.
- **Stream processor** — filtering, enrichment, aggregation, joins; holds state.
- **Derived topics** — results other teams consume, so one job's output is another's input.
- **Sinks** — chosen by access pattern: point lookups → a key-value store, slice-and-dice
  aggregates → real-time OLAP, history and analytics → a lakehouse table.

### 2-2) CDC as a source

**Change data capture reads the database's own change log (MySQL binlog, PostgreSQL WAL)
and turns every insert, update and delete into an event, without querying the tables.**

- **Debezium** on Kafka Connect is the common answer; each table becomes a topic keyed by
  the primary key, with `before` / `after` images and an `op` field (`c`, `u`, `d`, `r` for snapshot).
- It starts with an initial **snapshot**, then streams changes from the log position it recorded.
- Deletes arrive as a delete event followed by a **tombstone** (null value), which lets a
  compacted topic drop the key.
- **Outbox pattern** — the service writes its business row and an `outbox` row in one local
  transaction; CDC publishes the outbox. It avoids the dual-write problem (DB commit
  succeeds, Kafka publish fails, or the reverse).

### 2-3) Choosing the sink

| Need | Sink | Why |
|---|---|---|
| Point lookup by key (feature store, user profile) | Redis, Cassandra, DynamoDB | low-latency reads by key; upsert results |
| Sub-second slice-and-dice dashboards | Pinot, Druid, ClickHouse | columnar, ingest directly from Kafka |
| History, analytics, ML training | Iceberg / Delta / Hudi table | cheap storage, SQL engines, time travel |
| Search over events | Elasticsearch / OpenSearch | inverted index |
| Other stream jobs | a Kafka topic | replayable, fan-out |

The sink decides the delivery guarantee you can offer (section 6): an upsert-by-key sink
makes at-least-once look exactly-once; an append-only sink does not.

---

## 3) Time Semantics and Watermarks ⭐⭐⭐⭐⭐

### 3-1) Event time, ingestion time, processing time

**Event time is when the thing happened; processing time is when the engine sees it.
Correct aggregations use event time, because processing time changes every time you replay.**

| Time | Set by | Deterministic on replay? | Use for |
|---|---|---|---|
| **Event time** | the producer, inside the record | yes | business metrics, windows, joins |
| **Ingestion time** | the broker / source when it arrives | no | when events carry no timestamp |
| **Processing time** | the operator's wall clock | no | timeouts, monitoring, "last N seconds of traffic" |

The gap between event time and processing time (**skew**) is never constant: mobile
devices go offline, a partition lags, a producer retries. That is the whole reason
watermarks exist.

### 3-2) Watermarks

**A watermark is a marker in the stream that says "no more events with a timestamp ≤ T
are expected"; event-time windows close, and timers fire, when the watermark passes them.**

The usual strategy is **bounded out-of-orderness**: watermark = highest event time seen − a
fixed bound. Each parallel source instance emits its own watermark, and an operator with
several inputs uses the **minimum** of them.

```java
// java — Flink 1.19+: watermarks for a Kafka source
WatermarkStrategy<Ride> watermarks = WatermarkStrategy
        .<Ride>forBoundedOutOfOrderness(Duration.ofSeconds(10))   // tolerate 10 s of disorder
        .withTimestampAssigner((ride, recordTs) -> ride.getEventTimeMillis())
        .withIdleness(Duration.ofMinutes(1));                     // see 3-3

DataStream<Ride> rides = env.fromSource(kafkaSource, watermarks, "rides");
```

| Bound too small | Bound too large |
|---|---|
| more events arrive behind the watermark and count as late | windows close later, so results are delayed |
| lower latency, less state | more complete results, more state held |

A watermark is a **heuristic**, not a guarantee: it trades completeness for latency, and
whatever arrives behind it needs a late-data policy (section 4-3).

### 3-3) Idle sources and watermark alignment

**Because an operator takes the minimum watermark of its inputs, one partition that stops
sending data freezes the watermark for the whole job — and every window stops closing.**

- **Idleness** — `withIdleness(...)` marks a split idle after a quiet period, so it is
  excluded from the minimum until data arrives again.
- **Watermark alignment** (Flink 1.15+, `withWatermarkAlignment`) — the opposite problem: one
  source far *ahead* of the others makes downstream operators buffer its data in state. Alignment
  pauses the sources that are too far ahead.
- A topic with more partitions than active keys, or a backfill reading one partition faster
  than another, triggers both problems; check the per-subtask watermark in the Flink UI.

---

## 4) Windows, Triggers and Late Data ⭐⭐⭐⭐⭐

### 4-1) Window types

**A window turns an infinite stream into finite chunks you can aggregate; the type decides
whether chunks are fixed, overlapping or defined by gaps in activity.**

| Window | Definition | Each event lands in | Example |
|---|---|---|---|
| **Tumbling** | fixed size, no overlap | exactly one window | rides per city per minute |
| **Sliding / hopping** | fixed size, advancing by a slide < size | size ÷ slide windows | 10-minute moving count every minute |
| **Session** | closes after a gap of inactivity, per key | one session (sessions merge) | a user's visit until 30 minutes idle |
| **Global** | one window for all time | the single window | needs a custom trigger to ever emit |
| **Cumulative** (Flink SQL `CUMULATE`) | grows in steps within a max size | each step up to the max | today's revenue so far, updated every minute |

```java
// java — Flink 1.19+: one-minute tumbling event-time windows per city
rides.keyBy(Ride::getCity)
     .window(TumblingEventTimeWindows.of(Duration.ofMinutes(1)))
     .aggregate(new CountAndSumFare())          // incremental: keeps one accumulator per window
     .sinkTo(resultSink);
```

Prefer an incremental `aggregate` / `reduce` over a `process` window function that buffers
every event: the state per window drops from all its events to one accumulator.

### 4-2) Triggers: when a window emits

**A trigger decides when a window's result is emitted; the default event-time trigger fires
once, when the watermark passes the window end.**

- **Early firings** — emit speculative results before the window closes (every 10 s of
  processing time), so a dashboard is not blank for a whole hour-long window.
- **Late firings** — emit an updated result for each late event within the allowed lateness.
- Every firing after the first is a **refinement** of the same window, so the sink must be
  able to accept it: overwrite by `(key, window_start)`, or consume a retraction stream.
- Beam names the three questions explicitly: *what* is computed (the transform), *where* in
  event time (the window), *when* in processing time (the trigger), and *how* refinements
  relate (accumulating vs discarding panes).

### 4-3) Late data strategies

**A late event is one that arrives after the watermark has passed its window; you can drop
it, route it aside, or update the result you already emitted.**

| Strategy | How | Cost |
|---|---|---|
| **Drop** | the default once a window is closed | silent undercount; at least count what you drop |
| **Allowed lateness** | keep window state for an extra period, re-fire on each late event | more state; downstream sees updates |
| **Side output** | send late events to a separate stream / table | a batch job reconciles them later |
| **Upsert / retract downstream** | emit results keyed by window; later results overwrite | sink must support upserts |
| **Correct in batch** | the stream gives a fast estimate, a batch job over the archive gives the final number | two paths, like Lambda |

```java
// java — Flink: keep windows 5 extra minutes, send anything later to a side output
final OutputTag<Ride> lateRides = new OutputTag<Ride>("late-rides") {};

SingleOutputStreamOperator<CityCount> counts = rides
        .keyBy(Ride::getCity)
        .window(TumblingEventTimeWindows.of(Duration.ofMinutes(1)))
        .allowedLateness(Duration.ofMinutes(5))
        .sideOutputLateData(lateRides)
        .aggregate(new CountAndSumFare());

DataStream<Ride> tooLate = counts.getSideOutput(lateRides);   // e.g. write to a reconciliation table
```

---

## 5) State ⭐⭐⭐⭐

### 5-1) Keyed state vs operator state

**Keyed state is partitioned by the stream's key and only visible to the record with that
key; operator state belongs to one parallel instance — the Kafka offsets a source has read
are the classic example.**

| | Keyed state | Operator state |
|---|---|---|
| Scope | one value per key, after `keyBy` | one value per parallel subtask |
| Types (Flink) | `ValueState`, `ListState`, `MapState`, `ReducingState`, `AggregatingState` | `ListState`, union list state, broadcast state |
| Redistributed on rescale | by key group (section 8-2) | by splitting / unioning the lists |
| Example | running total per user, last-seen timestamp per device | source offsets, buffered sink transactions |

```java
// java — Flink keyed state: alert when a card's running spend exceeds a limit
public class SpendTracker extends KeyedProcessFunction<String, Payment, Alert> {
    private transient ValueState<Double> total;

    @Override
    public void open(OpenContext ctx) {              // Flink 1.19+; older versions: open(Configuration)
        StateTtlConfig ttl = StateTtlConfig.newBuilder(Duration.ofHours(24)).build();
        ValueStateDescriptor<Double> desc = new ValueStateDescriptor<>("total", Double.class);
        desc.enableTimeToLive(ttl);                  // bound state: idle cards expire
        total = getRuntimeContext().getState(desc);
    }

    @Override
    public void processElement(Payment p, Context ctx, Collector<Alert> out) throws Exception {
        double current = total.value() == null ? 0.0 : total.value();
        current += p.getAmount();
        total.update(current);
        if (current > 10_000.0) {
            out.collect(new Alert(p.getCardId(), current));
        }
    }
}
```

### 5-2) State backends

**The state backend decides where live state sits and how it is snapshotted: on the JVM heap
for speed, or in embedded RocksDB on local disk for state larger than memory.**

| Backend | Lives in | Good for | Watch out |
|---|---|---|---|
| Flink `HashMapStateBackend` | JVM heap, as objects | small state, lowest latency | GC pauses, state limited by heap |
| Flink `EmbeddedRocksDBStateBackend` | RocksDB on local disk | large state, **incremental** checkpoints | serialisation on every access; tune RocksDB memory |
| Flink 2.0 ForSt | disaggregated, remote storage | very large state, faster rescaling | newer — check maturity for your version |
| Spark default state store | executor memory, versioned files in the checkpoint dir | small–medium state | memory pressure with many keys |
| Spark RocksDB state store (3.2+) | RocksDB on local disk | large state | set `spark.sql.streaming.stateStore.providerClass` |
| Kafka Streams | local RocksDB + a changelog topic | state per application instance | restore time = replaying the changelog |

The checkpoint itself goes to durable storage (S3, HDFS, GCS) whichever backend holds the
live state.

### 5-3) State TTL and state size

**Stream state grows forever unless something removes it: a window closing, a watermark
passing, or an explicit TTL.**

- **Unbounded keys** — a `GROUP BY user_id` with no window keeps one entry per user ever seen.
  Set a TTL (`StateTtlConfig`, Flink SQL `table.exec.state.ttl`) or window the aggregation.
- **Stream-stream joins** without a time bound keep both sides forever (section 7-1).
- **Dedup sets** need a horizon: "drop duplicates seen within 24 hours", not "ever".
- Symptoms of state growth: checkpoint size and duration rising steadily, RocksDB disk
  filling, recovery getting slower each week.
- TTL is a correctness decision, not only an ops one: an expired key behaves as if it were
  new, so a user returning after the TTL is counted as new.

---

## 6) Fault Tolerance and Exactly-Once ⭐⭐⭐⭐⭐

### 6-1) Checkpoints: distributed snapshots

**A checkpoint is a consistent snapshot of every operator's state plus the source offsets
it corresponds to; on failure the job restores the last checkpoint and replays the source
from those offsets.**

Flink implements a variant of the **Chandy-Lamport** algorithm (asynchronous barrier
snapshotting):

```text
 source ──[e5][e4][ B n ][e3][e2]──► operator ──► sink

1. The checkpoint coordinator injects barrier n into every source; each source records its offsets.
2. Barriers flow with the data. An operator that has received barrier n on all inputs snapshots
   its state (asynchronously) and forwards the barrier.
3. When every sink has acknowledged barrier n, checkpoint n is complete.
4. Failure → restart all tasks from checkpoint n: restore state, rewind sources to n's offsets.
```

Everything before barrier n is in the snapshot; everything after it is replayed. That is
why the state is consistent: no event is counted in the state twice or lost from it.

```java
// java — Flink: checkpoint every minute, exactly-once state semantics
env.enableCheckpointing(60_000, CheckpointingMode.EXACTLY_ONCE);
env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30_000);
env.getCheckpointConfig().setCheckpointTimeout(10 * 60_000);
```

### 6-2) Aligned vs unaligned checkpoints

**With aligned checkpoints an operator with several inputs waits for the barrier on every
input before snapshotting; under backpressure that wait can stall the checkpoint for a long
time, which unaligned checkpoints avoid.**

| | Aligned | Unaligned (Flink 1.11+) |
|---|---|---|
| Mechanism | block inputs that delivered the barrier until all inputs deliver it | barrier overtakes buffered data; in-flight buffers go into the checkpoint |
| Under backpressure | checkpoint duration grows with the backlog | checkpoint completes quickly |
| Checkpoint size | state only | state + in-flight data |
| Modes | exactly-once and at-least-once | exactly-once only |

At-least-once checkpointing skips alignment entirely: a record that arrives after its input's
barrier may be processed and then replayed, so it can be counted twice.

### 6-3) Checkpoints vs savepoints

**Checkpoints are automatic and exist to recover from failure; savepoints are triggered by
you and exist to stop, upgrade, rescale or migrate a job without losing state.**

| | Checkpoint | Savepoint |
|---|---|---|
| Triggered by | Flink, periodically | the operator (`flink savepoint`, `flink stop --savepointPath`) |
| Purpose | failure recovery | planned upgrades, rescaling, Flink version upgrades, A/B of job versions |
| Format | may be incremental, backend-specific | canonical (portable across backends) or native |
| Lifecycle | owned by Flink, old ones cleaned up | owned by you, kept until you delete it |

Give every stateful operator a stable `.uid("...")`: savepoint state is matched to operators
by id, and an auto-generated id changes when the job graph changes.

### 6-4) Exactly-once end to end ⭐⭐⭐⭐⭐

**"Exactly-once" in a stream engine means each event affects the *state* exactly once;
getting it in the *output* needs three things: a replayable source, consistent checkpoints,
and a sink that is either transactional or idempotent.**

| Piece | Requirement | Examples |
|---|---|---|
| Source | can rewind to a recorded position | Kafka offsets, Kinesis sequence numbers, files |
| Engine | state + offsets snapshotted atomically | Flink checkpoints, Spark checkpoint + WAL of offsets |
| Sink | replayed output is not duplicated | Kafka transactions, two-phase commit, Delta / Iceberg commits per batch, upsert by key |

- **Transactional sink** — output is staged and only made visible when the checkpoint
  commits. Downstream readers must read committed data only (Kafka `isolation.level=read_committed`).
  Cost: output is visible only once per checkpoint, so latency ≥ checkpoint interval.
- **Idempotent sink** — replays write the same key with the same value (upsert on
  `(key, window_start)`, deterministic ids). Lower latency, but readers may briefly see a
  result that is later rewritten with the same value.
- Side effects outside the sink (sending an email, calling an API) are never covered —
  make them idempotent with a dedupe key.
- Without a transactional or idempotent sink, the honest description is **at-least-once**.

### 6-5) Two-phase commit sinks

**A two-phase-commit sink opens a transaction per checkpoint, pre-commits when the barrier
arrives, and commits only after the whole checkpoint succeeds.**

```text
barrier n arrives at sink ──► pre-commit txn n (flush, keep it open) ──► start txn n+1
checkpoint n completes     ──► commit txn n          (now visible to read_committed readers)
failure before completion  ──► abort txn n, restore checkpoint n-1, replay
```

```java
// java — Flink KafkaSink with exactly-once delivery
KafkaSink<String> sink = KafkaSink.<String>builder()
        .setBootstrapServers("broker:9092")
        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                .setTopic("ride-counts")
                .setValueSerializationSchema(new SimpleStringSchema())
                .build())
        .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
        .setTransactionalIdPrefix("ride-counts-job")      // must be unique per job
        .setProperty("transaction.timeout.ms", "900000")  // ≤ broker transaction.max.timeout.ms
        .build();
```

Traps: the producer's `transaction.timeout.ms` must cover the longest checkpoint plus the
restart time, or the broker aborts a pre-committed transaction and its data is lost; and it
may not exceed the broker's `transaction.max.timeout.ms` (15 minutes by default).

---

## 7) Stream Joins and Enrichment ⭐⭐⭐⭐

### 7-1) Stream-stream joins

**Joining two unbounded streams needs a time bound — "the payment arrives within 15 minutes
of the order" — so the engine knows when it can forget a row it is buffering.**

| Join | Condition | State held |
|---|---|---|
| **Interval join** | `b.ts BETWEEN a.ts - x AND a.ts + y` | each side for the interval, cleaned by the watermark |
| **Window join** | both events in the same window | per window |
| **Regular (unbounded) join** | key equality only | both sides forever — needs a TTL |

```sql
-- Flink SQL: match each order to its payment within 15 minutes (interval join)
SELECT o.order_id, o.amount, p.payment_id, p.pay_time
FROM   orders o, payments p
WHERE  o.order_id = p.order_id
  AND  p.pay_time BETWEEN o.order_time AND o.order_time + INTERVAL '15' MINUTE;
```

Outer joins add a question: when do you emit "order with no payment"? Only after the
watermark passes the end of the interval, so an outer join's unmatched rows are delayed by
the interval plus the watermark bound. Spark requires watermarks on both sides and a time
condition for outer stream-stream joins for the same reason.

### 7-2) Stream-table enrichment

**Enrichment adds dimension attributes (user tier, merchant category) to each event; you
either look them up in an external store or keep the table as state inside the job.**

| Approach | How | Trade-off |
|---|---|---|
| **Lookup per event** | query a DB / cache for each record (Flink async I/O, lookup join) | simple; external latency and load, use async I/O and a cache |
| **Table as state** | consume the table's CDC stream into keyed state, join locally | fast, no external calls; state holds the whole table |
| **Broadcast** | small table sent to every parallel instance (7-4) | fastest; only for small tables |
| **Kafka Streams KStream-KTable join** | KTable materialised from a compacted topic | same as table-as-state, partitioned by key |

Lookup joins see the table **as of processing time**, so a replay enriches old events with
today's attributes; a temporal join (7-3) fixes that.

### 7-3) Temporal joins

**A temporal join joins each event to the version of the table that was valid at the
event's time — the streaming equivalent of an SCD Type 2 lookup.**

```sql
-- Flink SQL: price each order at the exchange rate valid when it was placed
-- currency_rates must be a versioned table: a PRIMARY KEY and an event-time attribute with a watermark
SELECT o.order_id,
       o.amount * r.rate AS amount_usd
FROM   orders AS o
JOIN   currency_rates FOR SYSTEM_TIME AS OF o.order_time AS r
  ON   o.currency = r.currency;
```

Replays give the same answer, because the lookup is keyed on event time rather than on the
moment the join ran.

### 7-4) Broadcast state

**Broadcast state sends a small, slowly changing stream — rules, thresholds, feature flags
— to every parallel instance, so each one can apply it to its share of the main stream.**

- Typical use: a fraud job whose rules are updated at runtime without a redeploy.
- Every instance holds a full copy, so it only fits small data.
- The broadcast side must be processed identically on every instance; per-key logic stays on
  the keyed side.

### 7-5) Deduplication

**Producers retry and sources replay, so duplicates are normal; dedupe by a unique event id
within a bounded horizon.**

```sql
-- Flink SQL: keep the first event per event_id (the planner recognises this pattern)
SELECT event_id, user_id, event_time
FROM (
  SELECT *,
         ROW_NUMBER() OVER (PARTITION BY event_id ORDER BY event_time ASC) AS rn
  FROM   clicks
)
WHERE rn = 1;
```

```python
# python — Spark 3.5+: drop duplicates seen within the watermark delay
deduped = (events.withWatermark("event_time", "1 hour")
                 .dropDuplicatesWithinWatermark(["event_id"]))
```

- The id must be assigned **at the producer** — an id generated after ingestion is
  different on every replay.
- Bound the state: a state TTL (Flink SQL) or the watermark (Spark) decides how long ids are
  remembered. A duplicate arriving after that horizon passes through.

---

## 8) Backpressure, Scaling and Hot Keys ⭐⭐⭐

### 8-1) Backpressure

**Backpressure is a slow operator or sink slowing everything upstream of it, instead of
buffering until it runs out of memory.**

- **Flink** — bounded network buffers: when a downstream task's buffers are full, the
  upstream task cannot send and slows down, all the way back to the source. The web UI shows
  per-task backpressure and busy time.
- **Spark** — each micro-batch takes a bounded amount: `maxOffsetsPerTrigger` (Kafka) or
  `maxFilesPerTrigger` (files).
- **Kafka Streams** — consumers simply poll slower.
- In every case the backlog ends up as **consumer lag** in Kafka, which is where it belongs:
  the log absorbs the burst.

How to diagnose: find the **first** operator that is busy while the next is not — that one is
the bottleneck. Common causes: a slow external lookup (use async I/O), a skewed key, a sink
with too few partitions, or large state in RocksDB without enough memory.

### 8-2) Scaling and rescaling

**Parallelism is capped by the source partitions; keyed state is split into key groups, and
rescaling reassigns whole key groups to the new set of instances.**

- A Kafka source has at most one active reader per partition, so parallelism above the
  partition count leaves source instances idle (downstream operators can still be wider).
- Flink hashes each key into one of `maxParallelism` **key groups**; each subtask owns a
  contiguous range. Rescaling = take a savepoint, restart with a new parallelism, and each
  subtask loads its new ranges.
- `maxParallelism` fixes the number of key groups and cannot be changed for a job's keyed
  state without rewriting it (State Processor API). Set it deliberately before the first
  production deploy.
- Autoscaling: the Flink Kubernetes Operator's autoscaler, Dataflow's autoscaling, Kafka
  Streams scaling by adding instances (partitions are reassigned).

### 8-3) Hot keys

**A key that carries a large share of the traffic pins one subtask at 100% while the others
idle, and no amount of extra parallelism helps.**

| Fix | How | When |
|---|---|---|
| **Two-phase aggregation** | add a random salt to the key, pre-aggregate per `(key, salt)`, then aggregate per key | aggregations that are associative (count, sum, min, max) |
| **Local pre-aggregation** | combine within each subtask before the shuffle (Flink SQL `table.exec.mini-batch.*` + two-phase agg) | high-volume group-by |
| **Split the hot key out** | route the known hot keys to a dedicated path | a few known keys (a test account, a big tenant) |
| **Change the key** | key by a finer grain (`city, zone`) and roll up downstream | the grain allows it |

---

## 9) The Engines ⭐⭐⭐⭐

### 9-1) Comparison

| | Apache Flink | Spark Structured Streaming | Kafka Streams | Apache Beam / Dataflow |
|---|---|---|---|---|
| Model | record-at-a-time dataflow | micro-batch (incremental query) | record-at-a-time library | unified model; the runner executes it |
| Deploy | Flink cluster (K8s operator, YARN, managed services) | Spark cluster / Databricks | a library inside your Java app | Dataflow, or a Flink / Spark runner |
| State | first-class, very large, savepoints | state store per micro-batch | local RocksDB + changelog topics | managed by the runner |
| Event time | full: watermarks, timers, late data | watermarks for aggregations and joins | grace periods, stream time | full: windows, triggers, accumulation |
| Sources / sinks | many connectors | Spark data sources | Kafka in, Kafka out | many IOs |
| Pick when | low latency, complex event time, large state | team runs Spark; one engine for batch + stream | Kafka-centric microservices, no cluster to run | GCP, or portability across runners |

### 9-2) Flink: DataStream and Table / SQL

**Flink has two main APIs over the same runtime: the DataStream API for full control of
state and timers, and the Table API / SQL for declarative pipelines the planner optimises.**

- **DataStream** — `keyBy`, windows, `KeyedProcessFunction` with state and event-time
  timers, side outputs, async I/O. Use it for custom logic (fraud rules, complex event processing).
- **Table API / SQL** — dynamic tables: a stream is a table that keeps changing, and a query
  over it produces a changelog (`+I` insert, `-U` / `+U` update before/after, `-D` delete).
- An **append-only** result (windowed aggregation, filter) can go to any sink; an
  **updating** result (non-windowed `GROUP BY`) needs an upsert sink with a primary key
  (`upsert-kafka`, JDBC with a key, Paimon / Hudi).
- **CEP library** — pattern matching over event sequences (`MATCH_RECOGNIZE` in SQL).

### 9-3) Spark Structured Streaming

**Structured Streaming treats a stream as an unbounded table and runs your batch DataFrame
query incrementally, one micro-batch per trigger.**

| Output mode | Emits | Allowed with |
|---|---|---|
| **Append** | only rows that will never change | non-aggregating queries; aggregations **with** a watermark (emitted when the window closes) |
| **Update** | rows that changed since the last trigger | aggregations (on a plain query it behaves like append); not every sink supports it |
| **Complete** | the whole result table every trigger | aggregations only; result must fit in memory |

| Sink | Output modes | Note |
|---|---|---|
| File (Parquet, etc.) | append | exactly-once via the sink's metadata log |
| Delta Lake | append, complete | **not** update; use `foreachBatch` + `MERGE` for updates |
| Kafka | append, update, complete | at-least-once |
| `foreachBatch` | any | your code per micro-batch; make it idempotent with the batch id |
| Console / memory | any | debugging only |

```python
# python — Spark 3.5+: one-minute ride counts per city, written to Delta
from pyspark.sql import functions as F

rides = (spark.readStream.format("kafka")
              .option("kafka.bootstrap.servers", "broker:9092")
              .option("subscribe", "rides")
              .option("maxOffsetsPerTrigger", 500000)              # bound each micro-batch
              .load()
              .select(F.from_json(F.col("value").cast("string"), ride_schema).alias("r"))
              .select("r.*"))

counts = (rides.withWatermark("event_time", "10 minutes")
               .groupBy(F.window("event_time", "1 minute"), "city")
               .agg(F.count("*").alias("rides"), F.sum("fare").alias("revenue")))

# append: each window is written once, after the watermark passes its end
(counts.writeStream.outputMode("append")
       .format("delta")
       .option("checkpointLocation", "s3://bucket/chk/rides_per_city")
       .trigger(processingTime="1 minute")
       .start("s3://bucket/tables/rides_per_city"))
```

```python
# python — updating results into Delta: foreachBatch + MERGE, idempotent per batch
from delta.tables import DeltaTable

def upsert(batch_df, batch_id):
    target = DeltaTable.forPath(spark, "s3://bucket/tables/rides_per_city_live")
    (target.alias("t")
           .merge(batch_df.alias("s"),
                  "t.city = s.city AND t.window = s.window")
           .whenMatchedUpdateAll()
           .whenNotMatchedInsertAll()
           .execute())

(counts.writeStream.outputMode("update")
       .foreachBatch(upsert)
       .option("checkpointLocation", "s3://bucket/chk/rides_per_city_live")
       .start())
```

- The **checkpoint location** holds offsets, commit log and state; deleting it, or changing
  the query in an incompatible way (adding a stateful operator, changing the grouping keys),
  means restarting from scratch.
- `trigger(availableNow=True)` (3.3+) processes everything available and stops — the
  streaming code run as an incremental batch job.

### 9-4) Kafka Streams: KStream and KTable

**Kafka Streams is a library, not a cluster: your application reads from Kafka, keeps state
in local RocksDB backed by changelog topics, and writes back to Kafka.**

| Abstraction | Is | Example |
|---|---|---|
| **KStream** | an event stream — every record is a new fact | clicks, payments |
| **KTable** | a changelog — the latest value per key | user profile, account balance |
| **GlobalKTable** | a KTable fully replicated to every instance | small reference data; join without co-partitioning |

```java
// java — Kafka Streams 3.x: rides per city per minute, 30 s grace for late events
StreamsBuilder builder = new StreamsBuilder();
KStream<String, Ride> rides = builder.stream("rides", Consumed.with(Serdes.String(), rideSerde));

KTable<Windowed<String>, Long> perCity = rides
        .groupBy((key, ride) -> ride.getCity(), Grouped.with(Serdes.String(), rideSerde))
        .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(30)))
        .count();

perCity.toStream()
       .map((windowedCity, count) -> KeyValue.pair(
               windowedCity.key() + "@" + windowedCity.window().start(), count))
       .to("rides-per-city", Produced.with(Serdes.String(), Serdes.Long()));
```

- Joins require **co-partitioning**: both topics with the same key and the same partition
  count; re-keying (`groupBy`, `selectKey`) creates an internal repartition topic.
- `processing.guarantee=exactly_once_v2` uses Kafka transactions for read-process-write.
- Scale out by starting more instances of the app; partitions are rebalanced across them, and
  a moved task restores its state from the changelog (standby replicas shorten that).

### 9-5) Apache Beam and Dataflow

**Beam is a programming model — PCollections, windows, triggers, watermarks — that runs on a
chosen runner; Google Cloud Dataflow is the managed runner most people mean.**

- One pipeline definition for batch and stream; the runner (Dataflow, Flink, Spark) executes it.
- Its model is the clearest statement of the what / where / when / how questions (section 4-2).
- Dataflow adds autoscaling, dynamic work rebalancing and a managed shuffle / streaming engine.
- Trade-off: portability and a managed service, against fewer knobs and runner-specific gaps.

---

## 10) Streaming SQL ⭐⭐⭐

### 10-1) A windowed aggregation in Flink SQL

**Streaming SQL declares the source with its watermark, then runs an ordinary-looking query
whose windows come from table-valued functions.**

```sql
-- Flink SQL 1.19+
CREATE TABLE rides (
  ride_id     STRING,
  city        STRING,
  fare        DECIMAL(10, 2),
  event_time  TIMESTAMP(3),
  WATERMARK FOR event_time AS event_time - INTERVAL '10' SECOND
) WITH (
  'connector' = 'kafka',
  'topic' = 'rides',
  'properties.bootstrap.servers' = 'broker:9092',
  'properties.group.id' = 'rides-per-city',
  'scan.startup.mode' = 'earliest-offset',
  'format' = 'json'
);

CREATE TABLE rides_per_city (
  window_start  TIMESTAMP(3),
  window_end    TIMESTAMP(3),
  city          STRING,
  rides         BIGINT,
  revenue       DECIMAL(38, 2)
) WITH (
  'connector' = 'kafka',
  'topic' = 'rides-per-city',
  'properties.bootstrap.servers' = 'broker:9092',
  'format' = 'json'
);

INSERT INTO rides_per_city
SELECT window_start, window_end, city, COUNT(*) AS rides, SUM(fare) AS revenue
FROM   TABLE(TUMBLE(TABLE rides, DESCRIPTOR(event_time), INTERVAL '1' MINUTES))
GROUP  BY window_start, window_end, city;
```

- `TUMBLE`, `HOP`, `CUMULATE` and `SESSION` (1.19+ as a TVF) are the window functions.
- A windowed aggregation is **append-only**, so a plain `kafka` sink works; the same query
  without the window would produce updates and need `upsert-kafka` with a primary key.

---

## 11) Serving Real-Time Results ⭐⭐⭐

### 11-1) Real-time OLAP stores

**Real-time OLAP stores ingest straight from Kafka and answer aggregate queries in
sub-second time, which is what a live dashboard over raw events needs.**

| Store | Strength | Notes |
|---|---|---|
| **Apache Druid** | time-partitioned segments, rollup at ingestion | native Kafka / Kinesis ingestion; strong for time-series slice-and-dice |
| **Apache Pinot** | low-latency user-facing analytics, upserts | star-tree index; built at LinkedIn, used at Uber |
| **ClickHouse** | very fast scans on `MergeTree` tables | Kafka table engine + materialised views; also a general analytics DB |
| **StarRocks / Doris** | MPP SQL with real-time ingestion and joins | MySQL-compatible protocol |

Pre-aggregate in the stream job when the query pattern is fixed (a dashboard tile); ingest
raw events into the OLAP store when users slice by arbitrary dimensions.

### 11-2) Streaming into lakehouse tables

**Streaming into Iceberg, Delta or Hudi gives the stream's data a durable, queryable home
with ACID commits — at the cost of minute-level freshness and a small-files problem.**

- Each commit (a Flink checkpoint, a Spark micro-batch) adds files; frequent commits mean many
  small files, so schedule **compaction** (Delta `OPTIMIZE`, Iceberg `rewrite_data_files`) and
  snapshot expiry.
- **Upserts / CDC** — Hudi merge-on-read, Iceberg v2 row-level deletes, Delta `MERGE`, and
  Apache Paimon (built for Flink streaming upserts) apply changes by key.
- The commit interval sets freshness: a one-minute checkpoint means data is queryable roughly
  a minute after it arrives.
- This is what lets a Kappa-style system answer historical questions without a separate
  batch copy.

---

## 12) Operations ⭐⭐⭐⭐

### 12-1) What to monitor

| Metric | Tells you | Alert when |
|---|---|---|
| **Consumer lag** (records / time) | the job is falling behind its source | lag grows steadily |
| **End-to-end latency** (event time → sink) | freshness as users see it | above the SLA |
| **Watermark delay** (now − watermark) | how far event time is behind | growing, or frozen (idle source) |
| **Checkpoint duration / size / failures** | health of fault tolerance | failures, or duration near the interval |
| **Backpressure / busy time** per operator | where the bottleneck is | sustained high |
| **Late records dropped** | completeness lost to the watermark | above the expected rate |
| **Restarts** | crash loops | any restart storm |
| **State size** | unbounded growth | steady growth with stable traffic |

### 12-2) Failure scenarios

| Failure | What happens | Mitigation |
|---|---|---|
| Task / node dies | restart from the last checkpoint, replay since then | checkpoints; spare slots |
| Poison message (bad schema, null) | the job crashes on it, restarts, crashes again | catch errors, send to a **dead-letter queue**, keep running |
| Sink down | backpressure, lag grows, checkpoints may time out | retries with backoff; Kafka absorbs the backlog; alert on lag |
| Source retention expires during an outage | data you never read is deleted | retention longer than the worst outage; archive to object storage |
| Checkpoint storage unavailable | checkpoints fail; a later failure replays more | tolerate N failed checkpoints, alert |
| Bad deploy writes wrong results | wrong data downstream | savepoint before deploy; replay from it or from older offsets into a new output |
| Clock skew on producers | event times in the future push the watermark forward, everything else becomes late | clamp or reject timestamps too far ahead |

### 12-3) Deploying a new version and reprocessing

- **Compatible change** — stop with a savepoint, deploy, restore from the savepoint. Stable
  operator uids make this possible.
- **Incompatible change** (new aggregation key, new state schema) — run the new job alongside
  the old one from an earlier offset into a **new output** table / topic, compare, then switch
  readers and retire the old job. This is the Kappa reprocessing move.
- **Backfill** — replay from Kafka if retention covers it; otherwise run the same logic as a
  batch job over the archived raw data (Flink and Spark both run the code in batch mode).

---

## 13) Worked Design: Real-Time Rides per City ⭐⭐⭐⭐

**Question**: "Build a dashboard that shows rides started and revenue per city, updated
within a minute, for the last 24 hours."

**Clarify first**: event volume, freshness SLA, whether numbers may be revised by late
events, how far back users look, and whether they filter by other dimensions.

```text
 rider / driver apps ─► API ─► Kafka "ride_events" (key = ride_id, Avro + Schema Registry)
                                     │
                                     ▼
                     Flink job (event time, 30 s bounded out-of-orderness)
                       1. dedupe by event_id (24 h state TTL)
                       2. enrich with city from zone (broadcast state, small table)
                       3. 1-minute tumbling window per city: count, sum(fare)
                       4. allowed lateness 10 min → updated results keyed by (city, window_start)
                                     │
                ┌────────────────────┼──────────────────────────┐
                ▼                    ▼                          ▼
   upsert-kafka "city_minute"   Pinot table (upsert on      Iceberg table (raw events,
   (other consumers)             city + window_start)        hourly compaction) → batch
                                     │                        reconciliation and history
                                     ▼
                               dashboard (queries Pinot)
```

Decisions to say out loud:

1. **Event time** with a watermark, because app events arrive late from poor connections.
2. **Results keyed by `(city, window_start)`** and upserted, so late events revise a minute
   instead of creating a duplicate row; the dashboard always reads the latest value.
3. **At-least-once + idempotent upserts** to Pinot, rather than paying the transaction
   latency of exactly-once Kafka output; duplicates from replays overwrite the same key.
4. **Dedupe by a producer-assigned `event_id`**, since mobile clients retry.
5. **Raw events to Iceberg** for history and a nightly batch reconciliation that corrects
   anything that arrived after the allowed lateness.
6. **Scaling**: Kafka partitions sized for peak throughput; key the window by city, and if one
   city dominates, key by `(city, zone)` and roll up in the query.
7. **Operations**: alert on consumer lag, watermark delay and checkpoint failures; dead-letter
   malformed events.

---

## 14) Quick Q&A ⭐⭐⭐

**Q: Is Kafka a stream processor?**
Kafka is the log; Kafka Streams and ksqlDB are processors that run on top of it. The broker
itself only stores and serves records.

**Q: Why not use processing time — it is simpler?**
Because the result depends on when the job ran: a replay or a lagging partition changes the
counts. Processing time is fine for operational signals ("errors in the last minute of
traffic"), not for business metrics.

**Q: What happens to a window if one Kafka partition receives no data?**
Its watermark does not advance, the operator's minimum watermark stalls, and windows stop
closing — unless the source is marked idle.

**Q: Does exactly-once mean each event is processed once?**
No. Events may be processed more than once after a failure; exactly-once means the state and
the committed output reflect each event once.

**Q: How do you change the aggregation logic of a running stateful job?**
If the state is compatible, savepoint and restore. If not, start the new version from an
earlier offset into a new output, validate, and switch readers over.

**Q: Micro-batch or continuous for a 2-second SLA?**
Either can meet it; micro-batch with a short trigger is often enough. Pick record-at-a-time
when the SLA is sub-second or the logic needs per-event timers.

**Q: How do you test a streaming job?**
Unit-test functions with test harnesses (Flink `KeyedOneInputStreamOperatorTestHarness`,
Kafka Streams `TopologyTestDriver`), feed out-of-order and late events on purpose, and run an
integration test against an embedded or containerised Kafka.
