# Apache Kafka for Data Engineers

> **Scope** — Kafka as a data engineer is interviewed on it: the storage log, replication, the KRaft controller, producer and consumer internals, delivery semantics and transactions, retention and compaction, sizing, Kafka Connect and CDC, Schema Registry, security, operations and failure scenarios; stream-processing semantics (windows, watermarks, state) belong to the stream pipeline doc.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`./stream_pipeline.md`](./stream_pipeline.md) — Kafka Streams, Flink and end-to-end exactly-once; [`./schema_design.md`](./schema_design.md) — schema evolution in general; [`../kafka/faq_kafka.md`](../kafka/faq_kafka.md) — the older Kafka notes with diagrams.

Answers target **Apache Kafka 3.x–4.x**. Where a default or a feature changed between
versions, the version is named.

---

## 1) What Kafka Is ⭐⭐⭐⭐⭐

### 1-1) The one-sentence answer

**Kafka is a distributed, replicated, append-only commit log: producers append records to
partitions, consumers read them at their own offset, and records stay for the retention
period whether or not anyone has read them.**

That one sentence explains most of Kafka's behaviour:

- **append-only log** → writes are sequential, so they are fast, and every record has a
  stable position (the offset);
- **consumers own their offset** → many independent readers, each at its own pace, and any
  of them can rewind and replay;
- **retention, not deletion on read** → Kafka is a buffer between systems *and* a short-term
  source of truth you can rebuild state from.

### 1-2) Vocabulary

| Term | Meaning |
|---|---|
| **Record** | key (optional), value, headers, timestamp |
| **Topic** | a named stream of records |
| **Partition** | an ordered, immutable log; the unit of parallelism **and** of ordering |
| **Offset** | a record's position inside its partition, a monotonically increasing 64-bit integer |
| **Broker** | one Kafka server; a cluster is several |
| **Replica** | a copy of a partition on a broker; one is the **leader**, the others **followers** |
| **ISR** | in-sync replicas — the leader plus followers that are caught up |
| **Controller** | the component that manages metadata and leader election (KRaft) |
| **Consumer group** | a set of consumers that share a topic's partitions between them |

```text
Topic "orders", 3 partitions, replication factor 3

          Broker 1          Broker 2          Broker 3
P0     [Leader]          [Follower]        [Follower]
P1     [Follower]        [Leader]          [Follower]
P2     [Follower]        [Follower]        [Leader]

P0 log:  | 0 | 1 | 2 | 3 | 4 | 5 | ...  → append only
                         ▲ consumer group A has committed offset 3
```

### 1-3) Why it is fast

**Kafka is fast because it does very little per record: sequential appends, the OS page
cache instead of its own cache, zero-copy transfer to consumers, and batching everywhere.**

- **Sequential I/O** — appends to the end of a segment file; no random writes.
- **Page cache** — the broker relies on the OS to cache recently written data, so consumers
  that keep up are usually served from memory.
- **Zero-copy** — on plaintext connections the broker can use `sendfile` to move bytes from
  the page cache to the socket without copying them through user space (TLS prevents this,
  because the bytes must be encrypted).
- **Batching + compression** — producers send record batches; the batch is stored and
  shipped as-is, compressed once by the producer and decompressed by the consumer.
- **Partitioning** — load spreads across brokers and disks, and consumers scale out per
  partition.

---

## 2) Storage Internals ⭐⭐⭐⭐

### 2-1) Partitions, segments and indexes

**A partition is a directory of segment files; only the newest (active) segment is written,
and retention deletes or compacts whole older segments.**

```text
/var/lib/kafka/orders-0/
  00000000000000000000.log        ← records (batches), offsets 0 .. 368,771
  00000000000000000000.index      ← sparse offset → byte position
  00000000000000000000.timeindex  ← sparse timestamp → offset
  00000000000000368772.log        ← active segment, named by its base offset
  00000000000000368772.index
  00000000000000368772.timeindex
  leader-epoch-checkpoint
```

- A segment rolls when it reaches `segment.bytes` (default 1 GiB) or `segment.ms` (default 7 days).
- The **offset index** is sparse: to read offset *n*, binary-search the index for the
  nearest entry ≤ *n*, then scan forward in the `.log` file.
- The **time index** answers `offsetsForTimes` — "give me the first offset at or after
  this timestamp", which is how you replay from a point in time.
- Retention never deletes the active segment, so a low-traffic topic can hold data past
  `retention.ms` until its segment rolls.

### 2-2) Record batches and timestamps

**Records are stored in batches, and the batch — not the record — carries the producer id,
epoch, sequence number and compression codec.**

- `CreateTime` (default) — the timestamp the producer set; `LogAppendTime` — the broker
  overwrites it with append time (`message.timestamp.type`).
- Event-time processing downstream uses this timestamp unless the payload carries its own.
- `max.message.bytes` (topic) / `message.max.bytes` (broker) cap a batch's size — about
  1 MB by default. Large payloads belong in object storage, with a pointer in Kafka.

### 2-3) Tiered storage

**Tiered storage (KIP-405, production-ready in 3.9) moves older segments to object storage,
so retention is no longer bounded by local disks.**

- Local disk keeps recent segments (`local.retention.ms`); the remote tier keeps the rest
  (`retention.ms`).
- Brokers become lighter to replace and rebalance, because less data sits on them.
- Reads of old data come from the remote tier, with higher latency than the page cache.

---

## 3) Replication ⭐⭐⭐⭐⭐

### 3-1) Leaders, followers and the ISR

**Each partition has one leader that serves all writes (and by default all reads);
followers fetch from it like consumers, and the followers that keep up form the ISR.**

- A follower drops out of the ISR when it has not caught up for
  `replica.lag.time.max.ms` (default 30 s in 3.x).
- `min.insync.replicas` (commonly set to 2 with replication factor 3) is the minimum ISR
  size for an `acks=all` write to succeed; below it, the producer gets
  `NotEnoughReplicas` instead of a silently under-replicated write.
- **Follower fetching** (KIP-392, `replica.selector.class`) lets consumers read from the
  nearest replica, e.g. in the same availability zone, to cut cross-zone traffic.

### 3-2) LEO, high watermark and leader epoch

**Consumers can only read up to the high watermark — the offset every ISR member has —
so a record is visible only after it is replicated, and a leader change cannot expose a
record that might be lost.**

| Term | Meaning |
|---|---|
| **LEO** (log end offset) | the next offset a replica will write |
| **High watermark (HW)** | the smallest LEO across the ISR; records below it are committed |
| **Leader epoch** | a counter bumped on each leader change; stored with offsets |

```text
Leader    LEO = 10   | 0 .. 9 |
Follower1 LEO = 10   | 0 .. 9 |
Follower2 LEO =  8   | 0 .. 7 |      ISR = {L, F1, F2}
                                    HW = min(10, 10, 8) = 8
Consumers see offsets 0 .. 7.
```

- After a leader change, a follower uses the **leader epoch** to find where its log diverges
  from the new leader and truncates to that point (KIP-101). This replaced truncating to the
  high watermark, which could lose or diverge data in some double-failure cases.

### 3-3) Unclean leader election

**If every ISR member is down, Kafka can either wait for one to return (consistent, the
default) or promote an out-of-sync replica (available, but committed records are lost).**

- `unclean.leader.election.enable=false` is the default — choose consistency.
- Turning it on is a CAP decision: the partition comes back sooner, and some acknowledged
  records may be gone. Acceptable for metrics, not for payments.

### 3-4) What `acks=all` actually guarantees

**With `acks=all`, the leader acknowledges only after every current ISR member has the
record; with `min.insync.replicas=2` that means at least two brokers have it.**

The assumptions to state out loud in an interview:

- replication factor ≥ 3, `min.insync.replicas=2`, unclean leader election off;
- replicas on different racks / zones (`broker.rack`), so one failure domain cannot take
  all of them;
- the write is lost only if every in-sync copy is lost together. Kafka acknowledges writes
  that are in the page cache of the replicas, not `fsync`ed; durability comes from
  replication across machines, not from a per-write flush.

---

## 4) The Controller and KRaft ⭐⭐⭐

### 4-1) What the controller does

**The controller owns cluster metadata — topics, partitions, replica assignment, ISR, the
current leader of each partition — and elects a new leader when a broker fails.**

### 4-2) ZooKeeper vs KRaft

**KRaft replaces ZooKeeper with a Raft quorum of controller nodes that store metadata in an
internal Kafka log; it is production-ready since 3.3, and Kafka 4.0 removed ZooKeeper.**

| | ZooKeeper mode | KRaft mode |
|---|---|---|
| Metadata store | an external ZooKeeper ensemble | the `__cluster_metadata` log, replicated by Raft |
| Controller | one broker elected through ZooKeeper | a quorum of controllers (3 or 5) |
| Failover | the new controller reloads all metadata from ZooKeeper | standby controllers already have the log |
| Operations | two systems to run and secure | one |
| Status | removed in 4.0 | the only mode in 4.0 |

- Controllers can run on dedicated nodes (recommended for large clusters) or combined with
  brokers (`process.roles=broker,controller`, for small ones).
- Brokers fetch metadata changes from the controller log instead of being pushed updates,
  which is what makes very large partition counts practical.

---

## 5) Producers ⭐⭐⭐⭐⭐

### 5-1) The send path

**`send()` is asynchronous: the record is serialised, assigned a partition and appended to an
in-memory batch; a background sender thread ships ready batches to each partition's leader.**

```text
send(record)
  → serializer (key, value)
  → partitioner           → partition p
  → RecordAccumulator     → batch for (topic, p)       buffer.memory
  → Sender thread         → ProduceRequest to leader   batch.size / linger.ms
  → broker appends, replicates per acks
  → callback / Future completes (metadata or exception)
```

- `batch.size` (bytes per partition batch, default 16 KB) and `linger.ms` (how long to wait
  to fill a batch; default 5 ms since 4.0, 0 before) trade latency for throughput.
- `buffer.memory` full → `send()` blocks up to `max.block.ms`, then throws.
- `compression.type` — `lz4` or `zstd` are the common picks; compression works per batch, so
  bigger batches compress better.

### 5-2) Partitioning

**The key decides the partition: the same key always goes to the same partition, which is
what gives per-key ordering.**

- With a key: `murmur2(keyBytes) % numPartitions`.
- Without a key: the **sticky partitioner** (KIP-480, default since 2.4) fills one batch for a
  partition before moving to the next, which gives bigger batches than round-robin.
- **Adding partitions remaps keys** — key *k* may move to a new partition, and its ordering
  across the change is lost. Over-provision partitions at creation instead.
- A custom `Partitioner` is for genuinely skewed keys (route a hot tenant to several
  partitions), and costs that tenant its ordering.

### 5-3) `acks`, retries and ordering

| `acks` | Leader replies after | Can lose an acknowledged write when |
|---|---|---|
| `0` | nothing — fire and forget | any failure |
| `1` | the leader wrote it | the leader dies before a follower copies it |
| `all` / `-1` | every current ISR member wrote it | every in-sync copy is lost together (assumptions in 3-4) |

- `retries` defaults to effectively infinite; `delivery.timeout.ms` (default 2 min) is the
  real bound on how long a send may keep trying.
- Retries can reorder records when several requests are in flight. With idempotence on,
  ordering is preserved for `max.in.flight.requests.per.connection ≤ 5`.

### 5-4) The idempotent producer

**Idempotence gives each producer a producer id and each batch a per-partition sequence
number, so the broker discards a retried batch it has already written — no duplicates from
retries, and order is kept.**

- `enable.idempotence=true` is the default since 3.0 (it requires `acks=all`).
- It covers **one producer session**: a restarted producer gets a new producer id, so it
  does not deduplicate across restarts. That is what `transactional.id` adds (section 7).
- It does not deduplicate an application that calls `send()` twice for the same business
  event — that needs a business key and an idempotent consumer.

```java
// java — a durable, idempotent producer (kafka-clients 3.x)
Properties props = new Properties();
props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker1:9092,broker2:9092");
props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
props.put(ProducerConfig.ACKS_CONFIG, "all");
props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");

try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
    ProducerRecord<String, String> record =
        new ProducerRecord<>("orders", "customer-42", "{\"order_id\": 1001}");
    producer.send(record, (metadata, exception) -> {
        if (exception != null) {
            // retries are exhausted or the error is not retriable: log, alert, dead-letter
            System.err.println("send failed: " + exception);
        }
    });
}   // close() flushes outstanding batches
```

---

## 6) Consumers ⭐⭐⭐⭐⭐

### 6-1) The poll loop

**A consumer is a pull loop: `poll()` fetches records from the partitions it is assigned,
the application processes them, and the consumer commits the offsets it has finished.**

```python
# python — confluent-kafka 2.x, at-least-once: process first, then commit
from confluent_kafka import Consumer

consumer = Consumer({
    "bootstrap.servers": "broker1:9092",
    "group.id": "billing",
    "enable.auto.commit": False,
    "auto.offset.reset": "earliest",       # only used when the group has no committed offset
})
consumer.subscribe(["orders"])

try:
    while True:
        msg = consumer.poll(timeout=1.0)
        if msg is None:
            continue
        if msg.error():
            print("consumer error:", msg.error())
            continue
        process(msg.value())                         # must be idempotent: a crash replays it
        consumer.commit(message=msg, asynchronous=False)
finally:
    consumer.close()                                 # leaves the group cleanly
```

- `max.poll.records` caps a batch; `max.poll.interval.ms` (default 5 min) is the longest the
  application may go between polls before the consumer is considered dead and its partitions
  are reassigned. Slow processing → raise it, or process fewer records per poll.
- Heartbeats run on a background thread (`session.timeout.ms`, default 45 s since 3.0).
- Committing after every message is slow; production code commits per batch or every N
  seconds.

### 6-2) Consumer groups and assignment

**Within a group, each partition is consumed by exactly one consumer, so the partition count
caps the group's parallelism; different groups each get every record.**

```text
Topic with 4 partitions

Group "billing" (2 consumers)   Group "analytics" (5 consumers)
  C1 ← P0, P1                      C1 ← P0   C2 ← P1
  C2 ← P2, P3                      C3 ← P2   C4 ← P3   C5 idle
```

| Assignor | Behaviour |
|---|---|
| `RangeAssignor` | contiguous ranges per topic; can be uneven across topics |
| `RoundRobinAssignor` | spreads partitions evenly; moves many on change |
| `StickyAssignor` | even, and keeps existing assignments where possible |
| `CooperativeStickyAssignor` | sticky **and** incremental — only moved partitions are revoked |

### 6-3) Rebalancing

**A rebalance reassigns partitions when membership changes; under the classic eager
protocol every consumer stops and gives up all its partitions, which is the "stop-the-world"
pause interviewers ask about.**

Triggers: a consumer joins, leaves, crashes (misses the session timeout), or exceeds
`max.poll.interval.ms`; the subscribed topics change partitions.

| Protocol | What moves | Note |
|---|---|---|
| Eager (classic) | every partition is revoked, then reassigned | the whole group pauses |
| Cooperative incremental (2.4+) | only partitions that change owner | several short rounds, no full stop |
| Static membership (`group.instance.id`, 2.3+) | nothing on a quick restart | the member keeps its id across restarts within the session timeout |
| KIP-848 consumer protocol (GA in 4.0) | computed by the broker-side group coordinator | no group-wide barrier; consumers reconcile individually |

- Use static membership for rolling deploys on Kubernetes, so a pod restart does not reshuffle
  the whole group.
- Handle `on_revoke` / `onPartitionsRevoked`: commit what is processed and flush state before
  the partition moves, or the next owner reprocesses it.

### 6-4) Offset management

**Committed offsets live in the compacted internal topic `__consumer_offsets`, keyed by group,
topic and partition; the committed value is the next offset to read.**

| Strategy | Order | Crash result |
|---|---|---|
| Commit before processing | commit → process | at-most-once (records lost) |
| Commit after processing | process → commit | at-least-once (records replayed) |
| Auto-commit (`enable.auto.commit=true`) | commits on a timer inside `poll()` | at-least-once if processing finishes before the next poll, otherwise can skip |
| Offsets stored with the output | one DB transaction writes result + offset | effectively once for that sink |

- `auto.offset.reset` (`earliest` / `latest` / `none`) applies only when there is no
  committed offset or it has expired (`offsets.retention.minutes`, default 7 days).
- Reset or replay a group with the CLI (the group must be inactive):

```bash
# replay "billing" on topic orders from a point in time (dry run first, then --execute)
kafka-consumer-groups.sh --bootstrap-server broker1:9092 \
  --group billing --topic orders \
  --reset-offsets --to-datetime 2026-11-01T00:00:00.000 --dry-run

# see lag per partition
kafka-consumer-groups.sh --bootstrap-server broker1:9092 --describe --group billing
```

### 6-5) Consumer lag

**Lag = log end offset − committed offset, per partition; it is the first metric to alert on,
because it is how a slow or dead consumer shows up.**

- Alert on lag that **keeps growing**, or on time-based lag (how old the oldest unprocessed
  record is), not on a fixed count — a busy topic always has some lag.
- Fixes, in order: find the slow step (a DB call, a large message); add consumers up to the
  partition count; batch writes to the sink; add partitions (which remaps keys — 5-2).

---

## 7) Delivery Semantics and Transactions ⭐⭐⭐⭐⭐

### 7-1) The three semantics

**At-least-once is the default you design for; exactly-once within Kafka needs an idempotent
producer plus transactions; exactly-once into an external system needs a sink that is
idempotent or transactional.**

| Semantics | How | Failure result |
|---|---|---|
| At-most-once | commit offsets, then process | records lost |
| At-least-once | process, then commit | records duplicated |
| Exactly-once (Kafka → Kafka) | transactions + `isolation.level=read_committed` | neither |
| Effectively-once (Kafka → DB) | at-least-once + idempotent upsert, or offsets in the same DB transaction | neither, for that sink |

### 7-2) How transactions work

**A transactional producer writes to several partitions — including the consumer offsets topic —
and commits or aborts them atomically; `read_committed` consumers never see aborted records.**

- `transactional.id` — a stable id per producer instance; on restart it **fences** the old
  instance (bumps the epoch), so a zombie cannot write.
- The **transaction coordinator** records state in the internal `__transaction_state` topic
  and writes commit / abort **markers** into every partition the transaction touched.
- `read_committed` consumers read only up to the **last stable offset** — the first offset of
  any still-open transaction — so an open transaction delays downstream reads.

```java
// java — consume-transform-produce, exactly-once inside Kafka (kafka-clients 3.x)
producer.initTransactions();
while (true) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
    if (records.isEmpty()) continue;
    producer.beginTransaction();
    try {
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        for (ConsumerRecord<String, String> r : records) {
            producer.send(new ProducerRecord<>("orders-enriched", r.key(), enrich(r.value())));
            offsets.put(new TopicPartition(r.topic(), r.partition()),
                        new OffsetAndMetadata(r.offset() + 1));
        }
        // the input offsets are committed inside the same transaction as the output
        producer.sendOffsetsToTransaction(offsets, consumer.groupMetadata());
        producer.commitTransaction();
    } catch (ProducerFencedException e) {
        producer.close();          // another instance owns this transactional.id
        break;
    } catch (KafkaException e) {
        producer.abortTransaction();
        // rewind the consumer to the last committed offsets before retrying the batch
    }
}
```

### 7-3) Where exactly-once stops

- It covers **Kafka in, Kafka out**. A side effect inside the loop — an HTTP call, an email —
  can still happen twice.
- Writing to a database: upsert on a deterministic key (`order_id`), or store the consumed
  offset in the same DB transaction as the result and seek to it on startup.
- Kafka Streams (`processing.guarantee=exactly_once_v2`) and Flink's Kafka sink build on these
  transactions — see [`./stream_pipeline.md`](./stream_pipeline.md).

---

## 8) Retention and Log Compaction ⭐⭐⭐⭐

### 8-1) Delete retention

**`cleanup.policy=delete` drops whole closed segments once they are older than `retention.ms`
(default 7 days) or the partition exceeds `retention.bytes` (default unlimited).**

- Retention is per partition and per segment — data is deleted in segment-sized chunks.
- Size the disk as `ingest rate × retention × replication factor`, plus headroom.

### 8-2) Log compaction

**`cleanup.policy=compact` keeps at least the latest record for every key, so the topic is a
changelog you can rebuild a table from; a record with a `null` value (a tombstone) deletes
the key.**

```text
before:  k1=a  k2=b  k1=c  k3=d  k2=null  k1=e
after:   k3=d  k1=e                          (k2 removed once delete.retention.ms passes)
```

- The log cleaner compacts only the inactive segments; the active head is not compacted yet.
- Offsets are kept — compaction leaves gaps, and consumers must not assume consecutive offsets.
- Tombstones stay for `delete.retention.ms` (default 1 day) so slow consumers can see the delete.
- `cleanup.policy=compact,delete` combines both: latest per key, but nothing older than retention.
- Used by `__consumer_offsets`, Kafka Streams changelogs, CDC topics, and "current state per
  entity" topics.

### 8-3) Ordering guarantees

**Kafka guarantees order only within a partition; to keep an entity's events in order, key
them by the entity id.**

- Global order → one partition, and no parallelism.
- Order survives retries with idempotence on and `max.in.flight.requests.per.connection ≤ 5`.
- Order breaks when partitions are added (keys remap) or when a consumer processes one
  partition's records concurrently on several threads.

---

## 9) Topic and Partition Sizing ⭐⭐⭐⭐

### 9-1) How many partitions

**Pick enough partitions for the consumer parallelism and throughput you need at peak, with
headroom, because adding partitions later remaps keys.**

```text
partitions ≥ max( target_throughput / producer_throughput_per_partition,
                  target_throughput / consumer_throughput_per_partition,
                  peak number of consumers in the largest group )
```

- Measure the per-partition throughput on your own hardware and payloads; it depends on
  message size, compression, `acks` and the consumer's processing cost.
- Too many partitions cost: more open files and replication traffic, longer leader elections,
  more memory in producers (one batch buffer per partition).
- Too few: a hard ceiling on consumer parallelism.

### 9-2) Replication factor and placement

- `replication.factor=3` and `min.insync.replicas=2` is the common production default:
  tolerates one broker down for writes, two for reads.
- Set `broker.rack` so replicas are spread across racks / availability zones.
- Use `kafka-reassign-partitions.sh` (or Cruise Control) to move partitions when adding brokers
  — a new broker takes no existing partitions on its own.

### 9-3) Key design

- Choose a key with **many values and even traffic** — `user_id`, `order_id`.
- A hot key (one huge tenant) overloads one partition; split it with a sub-key and accept the
  loss of ordering for that tenant, or give it its own topic.
- A `null` key spreads records but gives no ordering.

---

## 10) Kafka Connect and CDC ⭐⭐⭐⭐

### 10-1) Connect architecture

**Kafka Connect is a framework for moving data between Kafka and other systems with
configuration instead of code: source connectors write into Kafka, sink connectors read from
it.**

| Concept | Meaning |
|---|---|
| **Worker** | a JVM process running connectors; distributed mode forms a group |
| **Connector** | the job definition (e.g. JDBC source, S3 sink) |
| **Task** | the unit of parallelism a connector splits into |
| **Converter** | serialises records — JSON, Avro, Protobuf (with Schema Registry) |
| **SMT** | single message transform — rename, mask, route a field |
| **DLQ** | `errors.tolerance=all` + `errors.deadletterqueue.topic.name` for bad records in sinks |

- Distributed mode stores configs, offsets and status in Kafka topics, and rebalances tasks
  when a worker dies.

### 10-2) CDC with Debezium

**Log-based CDC reads the database's replication log (MySQL binlog, PostgreSQL WAL) and turns
every insert, update and delete into an event, without querying the tables.**

```text
PostgreSQL WAL ─► Debezium source connector ─► topic dbserver.public.orders
                                                   key   = primary key
                                                   value = {before, after, op, ts_ms, source}
                                               ─► sink (warehouse, lakehouse, search index)
```

- **Snapshot then stream**: an initial snapshot of the table, then changes from the log position
  where the snapshot ended.
- Keyed by primary key → all changes to a row are in one partition, in order; compact the topic
  to keep the latest state.
- Catches **deletes** (a timestamp-based query cannot), and puts less load on the source than
  polling.
- Cost: the replication slot (PostgreSQL) holds WAL until Debezium reads it, so a stopped
  connector can fill the database's disk — monitor it.

### 10-3) The outbox pattern

**To publish an event and update a database atomically, write the event to an `outbox` table
in the same transaction, and let CDC publish it — never write to the DB and to Kafka as two
separate steps.**

- Two separate writes fail independently: the row commits and the event is lost, or the reverse.
- Debezium's outbox event router turns outbox rows into events on the right topic.

---

## 11) Schema Registry and Compatibility ⭐⭐⭐⭐

### 11-1) Why a registry

**A schema registry stores versioned schemas for each topic and lets producers send a small
schema id instead of the schema, while it rejects changes that would break consumers.**

```text
[ magic byte 0 ][ 4-byte schema id ][ Avro / Protobuf / JSON payload ]
```

- The subject name defaults to `<topic>-key` / `<topic>-value` (`TopicNameStrategy`).
- Consumers fetch the writer's schema by id and resolve it against their own reader schema.

### 11-2) Compatibility modes

| Mode | Guarantees | Allowed change (Avro) |
|---|---|---|
| `BACKWARD` (default) | new consumers can read data written with the previous schema | delete a field; add a field **with a default** |
| `FORWARD` | old consumers can read data written with the new schema | add a field; delete a field that has a default |
| `FULL` | both | add or delete only fields with defaults |
| `*_TRANSITIVE` | the same, against **all** previous versions, not only the last | |
| `NONE` | nothing checked | anything |

- Upgrade order follows the mode: `BACKWARD` → upgrade consumers first; `FORWARD` → producers
  first.
- Renaming a field is a delete plus an add; use an alias (Avro) or a new field and deprecate the old.
- More on schema evolution in general: [`./schema_design.md`](./schema_design.md).

---

## 12) Kafka Streams in One Section ⭐⭐⭐

**Kafka Streams is a Java library — not a cluster — that reads topics, processes them with
local state, and writes topics; it scales by running more instances of your application.**

- `KStream` (every event) vs `KTable` (latest value per key, backed by a compacted changelog).
- State lives in local RocksDB, backed up to changelog topics; a new instance restores it from
  there (standby replicas make that faster).
- Parallelism = input partitions; each partition is a task.
- Exactly-once with `processing.guarantee=exactly_once_v2`.
- Windows, joins and how it compares to Flink and Spark: [`./stream_pipeline.md`](./stream_pipeline.md).

---

## 13) Security ⭐⭐

**Encrypt with TLS, authenticate with SASL or mTLS, and authorise with ACLs — per principal,
per topic, per operation.**

| Layer | Options |
|---|---|
| Encryption in transit | TLS listeners (disables zero-copy `sendfile`) |
| Authentication | mTLS, SASL/SCRAM, SASL/OAUTHBEARER, SASL/GSSAPI (Kerberos) |
| Authorisation | ACLs: `Read` / `Write` / `Describe` / `Create` on topics, groups, transactional ids |
| At rest | disk encryption at the OS / cloud level; Kafka does not encrypt segments itself |

```bash
# allow the billing service to consume orders with its group
kafka-acls.sh --bootstrap-server broker1:9093 --command-config admin.properties \
  --add --allow-principal User:billing \
  --operation Read --topic orders --group billing
```

---

## 14) Operations and Monitoring ⭐⭐⭐⭐

### 14-1) The metrics that matter

| Metric | Healthy | Means when bad |
|---|---|---|
| `UnderReplicatedPartitions` | 0 | a follower is behind or a broker is down |
| `UnderMinIsrPartitionCount` | 0 | `acks=all` writes to those partitions are failing |
| `OfflinePartitionsCount` | 0 | no leader — partitions unavailable |
| `ActiveControllerCount` | exactly 1 across the cluster | controller problem |
| Consumer lag (per group) | stable | consumers too slow or stopped |
| Request latency (produce / fetch p99) | stable | disk, network or GC pressure |
| Disk usage | below the retention plan | retention or tiering misconfigured |

### 14-2) Routine operations

- **Rolling restart** — one broker at a time; wait for `UnderReplicatedPartitions` to return
  to 0 before the next.
- **Adding brokers** — reassign partitions to them; throttle replication
  (`--throttle`) so the move does not starve client traffic.
- **Preferred leader election** — after restarts, move leadership back so load is even
  (`auto.leader.rebalance.enable` does this periodically).
- **Topic config changes** — `kafka-configs.sh --alter`; no restart needed.

---

## 15) Failure Scenarios ⭐⭐⭐⭐⭐

**Interviewers ask "what happens when …" — answer with what the cluster does, what the client
sees, and whether data can be lost.**

| Scenario | What happens | Data loss? |
|---|---|---|
| A follower broker dies | it leaves the ISR; leaders keep serving | no |
| A leader broker dies | the controller elects a new leader from the ISR; clients refresh metadata and retry | no, with `acks=all` and an ISR of 2+ |
| ISR shrinks below `min.insync.replicas` | `acks=all` writes fail with `NotEnoughReplicas`; reads continue | no — writes are refused instead |
| All ISR members die | the partition is offline until one returns | no, unless unclean election is enabled |
| A producer times out on send | it retries; idempotence prevents duplicates within the session | no; the app sees an error only after `delivery.timeout.ms` |
| A consumer crashes mid-batch | its partitions move; the new owner resumes from the last commit | no, but the uncommitted records are reprocessed |
| A consumer is too slow (`max.poll.interval.ms`) | it is kicked out; rebalance; its commit may fail | no, but duplicates and rebalance storms |
| A poison message | the consumer fails on it repeatedly | no, but the partition is stuck — catch it and dead-letter it |
| Disk full on a broker | the broker shuts down its affected log directory | depends on replicas elsewhere |
| A network partition isolates a leader | the controller elects a new leader; the old one is fenced by the leader epoch | no acknowledged `acks=all` writes |

---

## 16) Kafka vs the Alternatives ⭐⭐⭐⭐

### 16-1) Kafka vs a message queue

**A queue deletes a message once it is acknowledged; Kafka keeps the log, so many independent
consumers can read it and any of them can replay it.**

| | Kafka | RabbitMQ | Amazon SQS |
|---|---|---|---|
| Model | partitioned log, pull | broker queues and exchanges, push | managed queue, pull |
| After consumption | kept until retention | removed when acked | removed when deleted |
| Replay | yes — reset the offset | no (streams plugin aside) | no |
| Ordering | per partition | per queue | FIFO queues per message group |
| Per-message routing / priority | no | yes | limited |
| Strength | event streams, fan-out, CDC, replay | task queues, routing, RPC-style work | serverless, zero-ops queueing |

### 16-2) Kafka vs Pulsar vs Kinesis

| | Kafka | Apache Pulsar | Amazon Kinesis Data Streams |
|---|---|---|---|
| Storage | brokers own partitions (plus tiered storage) | stateless brokers + BookKeeper storage | managed shards |
| Scaling unit | partition | partition / segment | shard |
| Multi-tenancy, geo-replication | via tooling (MirrorMaker 2, Cluster Linking) | built in | cross-region via your own replication |
| Queue semantics | consumer groups only (share groups, KIP-932, are arriving in 4.x) | streaming **and** queue subscriptions | no |
| Ops | self-managed or managed (MSK, Confluent Cloud) | self-managed or managed | fully managed, AWS only |
| Retention | configurable, unbounded with tiering | configurable, tiered | 24 h default, up to 365 days |

The usual interview answer: Kafka for its ecosystem (Connect, Streams, Flink, Debezium) and
replay; Kinesis when the team is all-in on AWS and wants no operations; Pulsar when
multi-tenancy and geo-replication are first-class requirements.

---

## 17) Quick Q → A ⭐⭐⭐⭐

- **Q: Can Kafka lose data?** — Yes, if `acks` is below `all`, if `min.insync.replicas` is 1,
  if unclean leader election is on, or if every replica fails together. With RF 3,
  `min.insync.replicas=2`, `acks=all` and unclean election off, an acknowledged write survives
  any single broker failure.
- **Q: How do you guarantee order?** — Same key → same partition; one partition is ordered.
  Keep idempotence on so retries do not reorder.
- **Q: Why can't I have more consumers than partitions?** — A partition is assigned to one
  consumer per group, so extra consumers sit idle. Add partitions, or use another group.
- **Q: Exactly-once — really?** — Yes for Kafka-to-Kafka with transactions and
  `read_committed`. For anything else, at-least-once plus an idempotent sink.
- **Q: Push or pull?** — Pull. Consumers control their rate, batch naturally, and can replay.
- **Q: How do you replay data?** — Reset the group's offsets (`--to-datetime`, `--to-earliest`),
  or start a new group, within the retention window.
- **Q: What is a rebalance storm and how do you stop it?** — Consumers repeatedly exceeding
  `max.poll.interval.ms` or restarting; fix slow processing, lower `max.poll.records`, use
  cooperative-sticky assignment and static membership.
- **Q: How big can a message be?** — About 1 MB by default; keep it small, and put large
  payloads in object storage with a pointer in the record.
- **Q: Where are consumer offsets stored?** — In the compacted `__consumer_offsets` topic
  (in ZooKeeper only in very old clients).
- **Q: How do you move data from a database into Kafka without dual writes?** — CDC with
  Debezium, and the outbox pattern for events the application emits.

---

## 18) Last-Minute Checklist

- **Model** — partitioned, replicated, append-only log; consumers own their offsets; retention, not delete-on-read.
- **Storage** — segments, sparse offset and time indexes, tiered storage for long retention.
- **Replication** — leader / followers / ISR, high watermark, leader epoch, unclean election off.
- **Producer** — key → partition, `acks=all` + `min.insync.replicas=2`, idempotence, batching and compression.
- **Consumer** — poll loop, `max.poll.interval.ms`, groups and assignors, cooperative and static membership, KIP-848.
- **Semantics** — commit after processing = at-least-once; transactions + `read_committed` = exactly-once inside Kafka.
- **Retention** — delete vs compact, tombstones, order only within a partition.
- **Sizing** — partitions for peak parallelism, RF 3, rack-aware, adding partitions remaps keys.
- **Ecosystem** — Connect, Debezium CDC, outbox, Schema Registry compatibility modes.
- **Ops** — under-replicated partitions, under-min-ISR, offline partitions, consumer lag.
