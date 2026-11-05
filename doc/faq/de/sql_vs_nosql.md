# SQL vs NoSQL — Data Engineering Interview Deep-Dive

> **Scope** — relational databases and the NoSQL families side by side: transactions and isolation, storage engines, replication and sharding, CAP and consistency models, one representative store per family, query-first modeling, NewSQL, and how to choose a store in a design question. Warehouse modeling lives in the data modeling deep-dive, not here.
> **See also**: [`../faq_DE_interview.md`](../faq_DE_interview.md) — the overview and last-minute checklist; [`./data_modeling.md`](./data_modeling.md), [`./schema_design.md`](./schema_design.md), [`./sql_for_data.md`](./sql_for_data.md), [`./kafka.md`](./kafka.md), [`./stream_pipeline.md`](./stream_pipeline.md), [`./batch_pipeline.md`](./batch_pipeline.md), [`./spark.md`](./spark.md), [`./hadoop.md`](./hadoop.md) — the other deep-dives; [`../db/faq_DB_DW.md`](../db/faq_DB_DW.md), [`../redis/redis_info.md`](../redis/redis_info.md) and [`../sql/faq_mysql.md`](../sql/faq_mysql.md) — older notes on specific engines.

Every answer leads with the sentence you would say out loud, then the mechanism, then the
trade-off an interviewer pushes on next.

---

## 1) The One-Minute Answer ⭐⭐⭐⭐⭐

### 1-1) The comparison table

**SQL databases give you a fixed schema, joins and multi-row ACID transactions on a design
that is single-primary or tightly coordinated; NoSQL databases trade some of that for
horizontal scale, a flexible schema, and a data model shaped around a known access pattern.**

| | SQL (relational) | NoSQL |
|---|---|---|
| Data model | tables, rows, relations | key-value, document, wide-column, graph, search |
| Schema | schema-on-write, enforced by the database | flexible; enforced by the application (or optional validators) |
| Query | SQL, ad-hoc joins and aggregations | an API or query language per store; joins usually done in the app |
| Transactions | ACID across many rows and tables | often single-item or single-partition; several now offer multi-item transactions |
| Scale | vertical first; read replicas; sharding is manual or bolted on | horizontal by partition key, built in |
| Consistency | strong on the primary | tunable, often eventual by default |
| Design starts from | the entities and their relations | the queries the application will run |
| Examples | PostgreSQL, MySQL, Oracle, SQL Server | Redis, DynamoDB, MongoDB, Cassandra, Neo4j, Elasticsearch |

**NewSQL** (Spanner, CockroachDB, TiDB) keeps SQL and ACID and adds horizontal scale, at the
cost of consensus latency on every write.

### 1-2) What the interviewer is really asking

"SQL or NoSQL?" is rarely a quiz about definitions. It checks whether you can **name the
requirement that decides it**:

- **Invariants across rows** (balance never negative, seat sold once) → relational transactions.
- **Unknown or ad-hoc queries** (analysts, reporting) → SQL.
- **Write volume or data size beyond one machine**, with **known queries** → a partitioned NoSQL store.
- **Latency at the edge / global distribution** → a multi-region store, with its consistency trade-off named.
- **Shape of the data** — graph traversal, full-text search, time series — → the store built for it.

A strong answer ends with "and here is what I give up".

---

## 2) Relational Fundamentals ⭐⭐⭐⭐⭐

### 2-1) ACID in depth

**ACID is the contract a transaction gives you: all-or-nothing, constraints hold, concurrent
transactions do not see each other's partial work, and a commit survives a crash.**

| Letter | Guarantee | How the engine provides it |
|---|---|---|
| **Atomicity** | every statement in the transaction applies, or none does | undo information / rollback; the commit record in the log is the switch |
| **Consistency** | declared constraints (PK, FK, CHECK, UNIQUE) hold after commit | constraint checks; the *application* still owns business rules not declared |
| **Isolation** | concurrent transactions behave as if run in some order, to the chosen level | locks and/or MVCC snapshots |
| **Durability** | a committed transaction is not lost on a crash | the write-ahead log is flushed (`fsync`) before the commit returns |

The "C" is the weakest letter: it means *the database will not break the rules you
declared*, not that your data is correct.

### 2-2) Isolation levels and anomalies ⭐⭐⭐⭐⭐

**Isolation levels are defined by which anomalies they allow; the stronger the level, the
fewer anomalies and the more blocking or aborts.**

| Anomaly | What happens |
|---|---|
| **Dirty read** | read another transaction's uncommitted write |
| **Non-repeatable read** | the same row read twice returns different values |
| **Phantom read** | the same range query returns a different set of rows |
| **Lost update** | two read-modify-write cycles overwrite each other |
| **Write skew** | two transactions read overlapping data, write disjoint rows, and together break an invariant |

| Level | Dirty read | Non-repeatable | Phantom | Notes |
|---|---|---|---|---|
| Read Uncommitted | possible | possible | possible | rarely used; PostgreSQL treats it as Read Committed |
| **Read Committed** | no | possible | possible | PostgreSQL and Oracle default |
| **Repeatable Read** | no | no | possible per the SQL standard | MySQL InnoDB default; InnoDB's next-key locks block phantoms for locking reads, and PostgreSQL's snapshot prevents them |
| **Serializable** | no | no | no | also prevents write skew; PostgreSQL uses SSI (aborts on conflict) |

Write skew, the classic example: two on-call doctors each check "is someone else on call?",
see yes, and both go off call. Snapshot isolation allows it; Serializable or an explicit
`SELECT ... FOR UPDATE` on the rows the decision read prevents it.

```sql
-- PostgreSQL: prevent a lost update with a row lock
BEGIN;
SELECT balance FROM accounts WHERE id = 42 FOR UPDATE;   -- others now wait on this row
UPDATE accounts SET balance = balance - 100 WHERE id = 42;
COMMIT;

-- or make the update itself atomic, no read needed
UPDATE accounts SET balance = balance - 100 WHERE id = 42 AND balance >= 100;
```

### 2-3) MVCC and locking

**MVCC keeps several versions of a row so readers see a consistent snapshot without
blocking writers; locks are still used so two writers cannot change the same row at once.**

- **PostgreSQL** — every row version carries the creating and deleting transaction ids
  (`xmin` / `xmax`). An `UPDATE` writes a new version; old ones are reclaimed by `VACUUM`.
  Long-running transactions keep old versions alive and cause table bloat.
- **MySQL InnoDB** — updates in place and keeps old versions in the **undo log**; readers
  rebuild the snapshot from it. A long transaction makes the undo history grow.
- **Readers do not block writers, writers do not block readers.** Writers on the same row
  still block each other (row locks).
- **Pessimistic locking** — `SELECT ... FOR UPDATE`; good when conflicts are frequent.
- **Optimistic locking** — a `version` column checked on write
  (`UPDATE ... WHERE id = ? AND version = ?`); good when conflicts are rare. The same pattern
  appears in NoSQL as conditional writes.
- **Deadlocks** — the engine detects the cycle and aborts one transaction; the app must retry.
  Taking locks in a consistent order avoids most of them.

### 2-4) The write-ahead log

**The write-ahead log (WAL, MySQL's redo log) records every change before the data pages are
written, so a crash is recovered by replaying the log — and the same log feeds replication
and change data capture.**

```text
client COMMIT
   │
   ▼
append change records to WAL ──fsync──► "commit OK" returned
   │
   ▼ (later, in the background)
dirty data pages flushed to the table files (checkpoint)

crash → restart → replay WAL from the last checkpoint → committed work restored
```

- Sequential log appends are cheap; random page writes are deferred and batched.
- **Replication** ships the log: PostgreSQL streaming replication sends WAL; MySQL sends its
  **binlog** (a separate logical log).
- **CDC** tools (Debezium) read the same log — PostgreSQL logical decoding, the MySQL
  binlog — to publish row changes to Kafka without querying the tables.

---

## 3) Storage Engines: B-tree vs LSM-tree ⭐⭐⭐⭐

### 3-1) The two designs

**B-trees update pages in place and are read-optimised; LSM-trees buffer writes in memory
and flush sorted immutable files, which makes writes cheap and pushes the cost to reads and
background compaction.**

| | B-tree | LSM-tree |
|---|---|---|
| Write path | find the page, modify it in place (plus the WAL) | append to the WAL + an in-memory **memtable**; flush to a sorted **SSTable** |
| Read path | one root-to-leaf walk | check the memtable, then SSTables newest to oldest; **Bloom filters** skip files |
| Background work | page splits, vacuum / purge | **compaction** merges SSTables and drops overwritten / deleted keys |
| Good at | point reads, range reads, mixed OLTP | high write throughput, sequential I/O |
| Used by | PostgreSQL, MySQL InnoDB, SQL Server, Oracle | Cassandra, RocksDB, LevelDB, HBase, ScyllaDB; DynamoDB's internals are not public |

```text
LSM write path
write ─► WAL (durability)
     └─► memtable (sorted, in memory) ── full ──► flush ──► SSTable L0
                                                         compaction ▼
                                                     SSTable L1 ... Ln (sorted, larger)
read: memtable → newest SSTable → ... (Bloom filter says "definitely not here" to skip)
```

### 3-2) Read, write and space amplification

**Every storage engine trades three costs against each other, and an interview answer that
names them sounds like experience.**

| Amplification | Meaning | B-tree | LSM-tree |
|---|---|---|---|
| **Write** | bytes written to disk per byte of user data | a whole page per small change | the same row rewritten at each compaction level |
| **Read** | disk reads per lookup | ~ tree height | several SSTables per lookup, reduced by Bloom filters and caching |
| **Space** | disk used vs live data | fragmentation, half-full pages | obsolete versions and tombstones until compaction |

Compaction strategy is the LSM tuning knob: **size-tiered** (lower write amplification,
more space) vs **leveled** (lower read and space amplification, more write amplification).
Cassandra exposes both, plus time-window compaction for time series.

---

## 4) Scaling a Relational Database ⭐⭐⭐⭐⭐

### 4-1) Replication: synchronous, asynchronous, read replicas

**Replication copies the primary's log to replicas: synchronous replication waits for a
replica before confirming a commit (no loss, more latency), asynchronous does not (fast, but
a failover can lose the last commits).**

| Mode | Commit waits for | On primary failure |
|---|---|---|
| **Asynchronous** | the primary only | recent commits may be missing on the promoted replica |
| **Semi-synchronous** (MySQL) | at least one replica received the change | little or no loss; falls back to async on timeout |
| **Synchronous** (PostgreSQL `synchronous_commit` + `synchronous_standby_names`) | the named replica(s) confirmed | no loss; a slow replica slows every commit |

- **Read replicas** scale reads, not writes — every write still goes to one primary.
- **Replication lag** breaks read-your-writes: a user updates a profile, the next page reads
  a replica and shows the old value. Fixes: read your own recent writes from the primary,
  pin a session to the primary for a few seconds, or wait for the replica to reach the
  write's log position.
- **Failover** promotes a replica (Patroni, Orchestrator, or a managed service such as RDS
  Multi-AZ); clients reconnect through a stable endpoint.

### 4-2) Partitioning vs sharding

**Partitioning splits one table into pieces inside one database; sharding splits the data
across separate database servers. Partitioning helps pruning and maintenance; sharding is
what scales writes.**

| | Partitioning | Sharding |
|---|---|---|
| Where | one server | many servers |
| Transparent to queries | yes — the planner prunes partitions | no — something must route each query |
| Cross-piece transactions | normal | need distributed transactions, or are avoided |
| Example | PostgreSQL `PARTITION BY RANGE (created_at)` | Vitess for MySQL, Citus for PostgreSQL, application-level routing |

```sql
-- PostgreSQL declarative partitioning
CREATE TABLE events (
  event_id    BIGINT       NOT NULL,
  created_at  TIMESTAMPTZ  NOT NULL,
  payload     JSONB
) PARTITION BY RANGE (created_at);

CREATE TABLE events_2026_11 PARTITION OF events
  FOR VALUES FROM ('2026-11-01') TO ('2026-12-01');
-- dropping a month is DROP TABLE events_2026_10 — no slow DELETE
```

### 4-3) Sharding strategies and their traps

| Strategy | How | Good | Trap |
|---|---|---|---|
| **Range** | key ranges per shard (`A–F`, `G–M`) | range scans stay on one shard | hot spots on sequential keys (timestamps, auto-increment ids) |
| **Hash** | `hash(key) mod N` | even spread | range scans hit every shard; changing N moves most keys |
| **Consistent hashing** | keys and nodes on a ring, with virtual nodes | adding a node moves only ~1/N of the keys | still no range locality |
| **Directory / lookup** | a table maps key → shard | flexible, supports moving tenants | the directory is a dependency to keep available |
| **Geo / tenant** | shard by region or customer | data residency, tenant isolation | one large tenant becomes a hot shard |

The shard key decides everything: pick one that every hot query includes, that spreads
load, and that keeps the rows of one transaction together (for example `tenant_id`).
**Cross-shard joins and transactions are the cost** — design so the common path never needs them.

### 4-4) Connection pooling

**A database connection is expensive — a PostgreSQL connection is a whole backend
process — so applications share a small pool of them instead of opening one per request.**

- App-side pools (HikariCP, SQLAlchemy's pool) reuse connections inside one service.
- A proxy pool (PgBouncer, RDS Proxy) shares connections across many app instances —
  essential with serverless or many pods.
- PgBouncer **transaction pooling** gives a connection back after each transaction, so
  session state (session-level `SET`, advisory locks, and — in older versions — prepared
  statements) does not carry over.
- More connections is not more throughput: past the core count, contention grows.

---

## 5) Distributed Systems Theory ⭐⭐⭐⭐⭐

### 5-1) CAP and PACELC

**CAP says that during a network partition a distributed store must choose between
consistency (refuse some requests) and availability (answer, possibly with stale data);
PACELC adds that even without a partition, it trades latency against consistency.**

- **C** in CAP means **linearizability**, not ACID's C.
- Partitions are not optional in a real network, so the choice is **CP or AP** during one.
- **PACELC** — if **P**artition, choose **A** or **C**; **E**lse, choose **L**atency or **C**onsistency.

| Store | PACELC | Why |
|---|---|---|
| Cassandra | PA/EL (tunable) | any replica answers; stronger levels per query |
| DynamoDB | PA/EL by default | eventually consistent reads unless `ConsistentRead=true` |
| HBase | PC/EC | one RegionServer serves each region |
| Spanner, CockroachDB | PC/EC | consensus on every write |

| Store | CAP lean | Note |
|---|---|---|
| Cassandra | AP | tunable per query: `QUORUM` reads + writes (R + W > N) give strong reads |
| DynamoDB | AP | eventual reads by default; `ConsistentRead=true` gives a strong read on the table or an LSI, never on a GSI |
| MongoDB, HBase | CP | single primary per shard / region |
| Spanner | CP | TrueTime; externally consistent |

### 5-2) Consistency models

**Consistency models are a ladder: the stronger the model, the more the system behaves like
a single copy, and the more coordination — latency — it costs.**

| Model | Guarantee | Example |
|---|---|---|
| **Linearizable** (strong) | every read sees the latest completed write, in real-time order | Spanner, etcd / ZooKeeper reads through the leader |
| **Sequential** | all clients see operations in the same order, not necessarily real time | — mostly a theory rung |
| **Causal** | operations that are causally related are seen in order; concurrent ones may differ | MongoDB causally consistent sessions |
| **Read-your-writes** | a client always sees its own writes | session stickiness, reading from the primary |
| **Monotonic reads** | a client never sees time go backwards | pin a client to one replica |
| **Eventual** | if writes stop, replicas converge | DynamoDB default reads, Cassandra `ONE` |

Interview tip: most product requirements are satisfied by **read-your-writes + monotonic
reads**, which are far cheaper than linearizability.

### 5-3) Quorums: R + W > N

**In a Dynamo-style store with N replicas, a write waits for W acknowledgements and a read
asks R replicas; if R + W > N, every read set overlaps the latest write set, so the read can
return the newest value.**

```text
N = 3 replicas
W = 2, R = 2  → R + W = 4 > 3   overlap guaranteed → strong-ish reads
W = 1, R = 1  → R + W = 2 ≤ 3   fastest, eventual
W = 3, R = 1  → writes fail if any replica is down; reads are cheap

Cassandra consistency levels: ONE, TWO, QUORUM, LOCAL_QUORUM, EACH_QUORUM, ALL
QUORUM = floor(RF / 2) + 1  (RF 3 → 2)
```

- Overlap is not full linearizability: concurrent writes, sloppy quorums and hinted handoff
  can still surface surprises. Conflicts are resolved by **last-write-wins** timestamps
  (Cassandra) or by version vectors / application merge (the original Dynamo paper).
- **Read repair** and **anti-entropy** (Merkle-tree repair, `nodetool repair`) bring stale
  replicas back in line.
- `LOCAL_QUORUM` keeps quorum inside one datacenter so cross-region latency is not paid on every request.

### 5-4) Consensus: Raft and Paxos in one breath

**Consensus lets a group of nodes agree on one ordered log even when some fail: a leader
proposes each entry and it commits once a majority has stored it.**

- **Raft** — leader election by term + majority vote; the leader replicates log entries;
  an entry is committed when a majority acknowledges. A 3-node group survives 1 failure, a
  5-node group 2.
- **Paxos / Multi-Paxos** — the older, equivalent family; Spanner uses Paxos per split.
- Used by: etcd and Consul (Raft), ZooKeeper (ZAB, a similar protocol), Kafka KRaft,
  CockroachDB and TiKV (Raft per range / region).
- The cost: each write needs a majority round trip, so a group spread across regions pays
  cross-region latency on every commit.

---

## 6) The NoSQL Families in Depth ⭐⭐⭐⭐⭐

### 6-1) Overview

| Family | Model | Good at | Examples |
|---|---|---|---|
| **Key-value** | `key → value` | caching, sessions, counters, O(1) lookups | Redis, DynamoDB, Memcached |
| **Document** | `key → JSON-like document` | nested entities read and written whole | MongoDB, Couchbase, Firestore |
| **Wide-column** | partition key → sorted rows of columns | huge write throughput, time-ordered data | Cassandra, HBase, Bigtable, ScyllaDB |
| **Graph** | nodes + relationships | many-hop relationship queries | Neo4j, Amazon Neptune |
| **Search** | inverted index | full-text, faceted search, log analytics | Elasticsearch, OpenSearch |
| **Time-series** | time-partitioned measurements | metrics, IoT | InfluxDB, TimescaleDB, Prometheus |

### 6-2) Key-value: Redis

**Redis is an in-memory data-structure server: every command is sub-millisecond because the
data lives in RAM and commands execute one at a time on a single thread.**

| Structure | Use | Commands |
|---|---|---|
| String | cache, counter, lock | `GET`, `SET key val EX 60 NX`, `INCR` |
| Hash | an object's fields | `HSET`, `HGET`, `HINCRBY` |
| List | queue, recent items | `LPUSH`, `RPOP`, `LRANGE` |
| Set | unique members, tags | `SADD`, `SISMEMBER`, `SINTER` |
| Sorted set | leaderboard, priority, sliding-window rate limit | `ZADD`, `ZRANGE ... REV`, `ZRANGEBYSCORE` |
| Stream | append-only log with consumer groups | `XADD`, `XREADGROUP`, `XACK` |
| HyperLogLog | approximate distinct count | `PFADD`, `PFCOUNT` |

- **Persistence**: **RDB** snapshots (compact, may lose the writes since the last snapshot)
  and **AOF** append-only file (`appendfsync everysec` loses at most about a second). Both can be on.
- **Eviction** when `maxmemory` is reached: `allkeys-lru`, `allkeys-lfu`, `volatile-ttl`, `noeviction`, ...
- **Scale**: replicas + **Sentinel** for failover; **Redis Cluster** shards across 16,384 hash slots.
- **Transactions**: `MULTI` / `EXEC` queue commands and run them without interleaving, but
  there is no rollback; Lua scripts run atomically. Replication is asynchronous, so an
  acknowledged write can be lost on failover.
- Not the system of record for data you cannot lose — use it as a cache or for derived state.

```bash
# cache-aside with a TTL, and a leaderboard
SET user:42:profile '{"name":"Ana"}' EX 300
ZADD leaderboard 1500 user:42
ZINCRBY leaderboard 25 user:42
ZRANGE leaderboard 0 9 REV WITHSCORES
```

See [`../redis/redis_info.md`](../redis/redis_info.md) for more Redis detail.

### 6-3) Key-value / document: DynamoDB ⭐⭐⭐⭐⭐

**DynamoDB is a managed, partitioned key-value and document store: you choose a partition
key (and optionally a sort key), and every efficient access goes through those keys or a
secondary index.**

- **Primary key** — a partition key alone, or partition key + sort key. Items with the same
  partition key form an **item collection**, sorted by the sort key, queryable by range
  (`begins_with`, `between`).
- **Query vs Scan** — `Query` reads one partition key's items; `Scan` reads the whole table
  and is the thing to avoid on the hot path.
- **Secondary indexes**:

| | Local secondary index (LSI) | Global secondary index (GSI) |
|---|---|---|
| Key | same partition key, different sort key | any partition / sort key |
| Created | only at table creation | any time |
| Consistency | eventual or strong (`ConsistentRead=true`) | eventual only |
| Capacity | shares the table's | its own |
| Limit | item collection ≤ 10 GB per partition key value | — |

- **Consistency** — reads are eventually consistent by default; `ConsistentRead=true` gives a
  strongly consistent read on the table or an LSI, at twice the read-capacity cost.
- **Capacity modes** — **on-demand** (pay per request) or **provisioned** (RCUs / WCUs,
  optionally with auto scaling). Throttling shows up as `ProvisionedThroughputExceededException`.
- **Hot partitions** — a single partition key value has a throughput ceiling; spread hot keys
  (for example by appending a suffix) and avoid low-cardinality partition keys.
- **Transactions** — `TransactWriteItems` / `TransactGetItems` make up to 100 actions atomic
  across items and tables.
- **Conditional writes** (`ConditionExpression`) give optimistic locking and "create if not exists".
- **TTL** expires items; **DynamoDB Streams** publishes item changes for CDC.

### 6-4) DynamoDB single-table design

**Single-table design stores several entity types in one table with generic `PK` / `SK`
attributes, so one `Query` returns an entity and its related items together.**

```text
Table: app        (PK = partition key, SK = sort key)

PK              SK                        attributes
CUSTOMER#42     PROFILE                   name, email
CUSTOMER#42     ORDER#2026-11-01#9001     total, status
CUSTOMER#42     ORDER#2026-11-03#9002     total, status
ORDER#9001      ITEM#1                    sku, qty, price
ORDER#9001      ITEM#2                    sku, qty, price

GSI1:  GSI1PK = status, GSI1SK = order date   → "all PENDING orders by date"

Query PK = CUSTOMER#42                              → profile + all orders
Query PK = CUSTOMER#42 AND begins_with(SK, ORDER#)  → orders only, sorted by date
Query PK = ORDER#9001                               → line items
```

- List every access pattern first; each must map to a key condition on the table or a GSI.
- The trade-off: new access patterns can need a new GSI or a backfill, and ad-hoc analytics
  belongs in a warehouse fed by DynamoDB Streams or an export to S3.

### 6-5) Document: MongoDB

**MongoDB stores BSON documents in collections; you embed data that is read together and
reference data that is shared or grows without bound.**

```javascript
// orders collection — line items embedded, customer referenced
{
  _id: ObjectId("6720b1f4c2a1e5d3f0a1b2c3"),
  customerId: ObjectId("6720a0e1b9f4d2c1e0a9b8c7"),
  status: "PAID",
  createdAt: ISODate("2026-11-01T10:15:00Z"),
  items: [
    { sku: "A-100", qty: 2, price: NumberDecimal("19.99") },
    { sku: "B-200", qty: 1, price: NumberDecimal("5.00") }
  ],
  shipping: { city: "Taipei", zip: "100" }
}
// db.orders.createIndex({ customerId: 1, createdAt: -1 })
```

| Embed when | Reference when |
|---|---|
| read together, owned by the parent (order → line items) | shared by many parents (product, customer) |
| bounded size | unbounded growth (comments, events) |
| updated together | updated independently and often |

- **Documents are limited to 16 MB** — an unbounded embedded array is the classic mistake.
- **Replica set** — one primary, secondaries replicate the oplog; automatic election on failure.
  `writeConcern: "majority"` and `readConcern: "majority"` give durability and avoid reading
  rolled-back writes; `readPreference` decides whether secondaries serve reads.
- **Sharding** — a shard key (ranged or hashed) splits a collection into chunks across shards,
  routed by `mongos`. A monotonically increasing shard key (like `createdAt`) sends every insert to one shard.
- **Multi-document ACID transactions** exist (replica sets since 4.0, sharded clusters since 4.2),
  but they cost more; a schema that needs them everywhere probably wanted a relational DB.
- **Aggregation pipeline** (`$match`, `$group`, `$lookup`) covers analytics-style queries;
  `$lookup` is a join, but not one to rely on at scale on the hot path.
- Schema validation is available with `$jsonSchema` validators.

### 6-6) Wide-column: Cassandra ⭐⭐⭐⭐

**Cassandra is a masterless, partitioned, replicated wide-column store built for high write
throughput: the partition key picks the nodes, the clustering key sorts rows inside the
partition, and every query must name the partition key.**

- **Ring** — consistent hashing (Murmur3 tokens, virtual nodes); every node is equal, no
  single primary. The replication factor (RF) sets how many nodes hold each partition;
  `NetworkTopologyStrategy` places replicas per datacenter.
- **Write path** — commit log + memtable → SSTable flush → compaction (an LSM-tree, see 3-1).
  Writes are fast and cheap; an update is just a newer write.
- **Tunable consistency** per query: `ONE`, `QUORUM`, `LOCAL_QUORUM`, `ALL` (see 5-3).
- **Tombstones** — a delete writes a marker that lives for `gc_grace_seconds` (10 days by
  default) so repairs can propagate it. Queue-like "insert then delete" patterns pile up
  tombstones and make reads slow or fail.
- **Lightweight transactions** — `INSERT ... IF NOT EXISTS`, `UPDATE ... IF col = ?` use Paxos;
  they are slow, so keep them off the hot path.
- **No joins, no ad-hoc filters** — a `WHERE` on a non-key column needs `ALLOW FILTERING`
  (a scan) or a secondary index; the normal answer is another table.
- **Partition size** — keep partitions bounded (a time bucket in the key), or one key
  becomes a hot, huge partition.

### 6-7) Graph: Neo4j

**A graph database stores relationships as first-class records, so a many-hop traversal
follows pointers instead of running a join per hop.**

```text
// Cypher: friends-of-friends who like the same band, excluding existing friends
MATCH (me:Person {id: 42})-[:FRIEND]->(:Person)-[:FRIEND]->(fof:Person),
      (fof)-[:LIKES]->(b:Band)<-[:LIKES]-(me)
WHERE NOT (me)-[:FRIEND]->(fof) AND fof <> me
RETURN fof.name, count(b) AS shared
ORDER BY shared DESC
LIMIT 10;
```

- Good at: recommendations, fraud rings, network / dependency analysis, knowledge graphs.
- Weak at: bulk aggregations over every node, and very large graphs that must be sharded.
- A recursive CTE in SQL can do 2–3 hops; a graph DB pays off when depth is variable or deep.

### 6-8) Search: Elasticsearch

**Elasticsearch is a distributed search engine on Lucene: an inverted index maps each term
to the documents containing it, so full-text queries with relevance scoring are fast.**

```text
docs:  1 "kafka consumer lag"     2 "spark consumer"     3 "kafka streams"

inverted index (after analysis: lowercase, tokenize)
term       → postings
consumer   → [1, 2]
kafka      → [1, 3]
lag        → [1]
spark      → [2]
streams    → [3]

query "kafka consumer" → union / intersect postings → score with BM25 → 1 ranks first
```

- An **index** is split into **primary shards** (fixed at creation) with **replicas**.
- **Near-real-time**: a document becomes searchable after a refresh (1 second by default).
- Mapping types matter: `text` (analysed, full-text) vs `keyword` (exact, aggregations).
- Not a primary database: no multi-document transactions, and a mapping change usually
  means reindexing. Feed it from the system of record through CDC.

---

## 7) Query-First Modeling ⭐⭐⭐⭐⭐

### 7-1) Entity-first vs query-first

**A relational schema is designed from the entities and normalised; a NoSQL schema is
designed from the queries and denormalised, with one table (or index) per access pattern and
the partition key chosen so each query hits one partition.**

| Step | Relational | NoSQL (Cassandra / DynamoDB) |
|---|---|---|
| 1 | list entities and relations | list every query, with its filters and sort order |
| 2 | normalise to 3NF | pick a partition key per query that spreads load |
| 3 | add indexes for slow queries | pick a clustering / sort key for ordering and ranges |
| 4 | any new query is just SQL | a new query may need a new table or index |
| Writes | one place per fact | the same fact written to several tables |

### 7-2) A worked Cassandra example

**Task: a chat app needs "latest messages in a room" and "messages a user sent".**

```sql
-- Cassandra (CQL): one table per query
CREATE TABLE messages_by_room (
  room_id     UUID,
  bucket      TEXT,          -- e.g. '2026-11' — caps partition size
  sent_at     TIMEUUID,
  sender_id   UUID,
  body        TEXT,
  PRIMARY KEY ((room_id, bucket), sent_at)
) WITH CLUSTERING ORDER BY (sent_at DESC);

CREATE TABLE messages_by_sender (
  sender_id   UUID,
  bucket      TEXT,
  sent_at     TIMEUUID,
  room_id     UUID,
  body        TEXT,
  PRIMARY KEY ((sender_id, bucket), sent_at)
) WITH CLUSTERING ORDER BY (sent_at DESC);

-- the hot query names the whole partition key
SELECT sender_id, body, sent_at
FROM   messages_by_room
WHERE  room_id = 9f1c2a7e-0b3d-4c5e-8f6a-1b2c3d4e5f60 AND bucket = '2026-11'
LIMIT  50;
```

- **Partition key** `(room_id, bucket)` — spreads load and bounds each partition by month.
- **Clustering key** `sent_at DESC` — newest first, so "latest 50" is one sequential read.
- Writing a message means writing both tables (a logged `BATCH` keeps them together,
  at some cost) — denormalisation is the price of one-partition reads.

---

## 8) NewSQL and Analytical Stores ⭐⭐⭐⭐

### 8-1) NewSQL: Spanner, CockroachDB, TiDB

**NewSQL databases shard data into ranges, replicate each range with consensus, and run
distributed transactions on top — SQL and ACID with horizontal scale.**

| | Google Spanner | CockroachDB | TiDB |
|---|---|---|---|
| Wire protocol / dialect | GoogleSQL and a PostgreSQL dialect | PostgreSQL-compatible | MySQL-compatible |
| Replication | Paxos per split | Raft per range | Raft per region (TiKV) |
| Time / ordering | TrueTime (GPS + atomic clocks, bounded uncertainty) | hybrid logical clocks | a central timestamp oracle (PD) |
| Default isolation | external consistency (strict serializability) | Serializable | snapshot isolation (called Repeatable Read) |
| Analytics | — | — | TiFlash columnar replicas (HTAP) |

- The cost: every write is a consensus round, so multi-region writes pay cross-region latency.
- Choose one when you need SQL + ACID **and** have outgrown one primary, or need multi-region
  writes with strong consistency.

### 8-2) OLTP stores vs OLAP stores

**OLTP stores serve many small keyed reads and writes with row storage; OLAP stores serve
large scans and aggregations with columnar storage — the data engineer usually moves data
from the first to the second.**

| | OLTP (PostgreSQL, MySQL, DynamoDB) | OLAP (BigQuery, Snowflake, Redshift, ClickHouse) |
|---|---|---|
| Storage | row-oriented | column-oriented, heavily compressed |
| Query | point lookups by key, small ranges | scans over billions of rows, few columns |
| Concurrency | thousands of short transactions | tens to hundreds of heavy queries |
| Updates | constant, row-level | batch loads, merges, mostly append |
| Indexes | B-tree on keys | partition pruning, sort keys, zone maps |

Running analytics on the OLTP primary steals its I/O and can lock or bloat tables; replicate
into a warehouse instead (see [`../db/faq_DB_DW.md`](../db/faq_DB_DW.md)).

### 8-3) Polyglot persistence and CDC between stores

**Most real systems use several stores, each for what it is best at, and keep them in sync
by streaming the system of record's changes — not by having the application write to all of them.**

```text
                  ┌────────────► Redis (cache, invalidated on change)
PostgreSQL ──WAL──► Debezium ──► Kafka ──► Elasticsearch (search index)
(system of record)                    ├──► Snowflake / BigQuery (analytics)
                                      └──► other services (events)
```

- **Dual writes** (the app writes to the DB and then to the search index) drift on any
  partial failure. Prefer CDC, or the **transactional outbox**: write the event to an
  `outbox` table in the same DB transaction, and stream that table.
- Downstream stores are eventually consistent with the source; design reads that tolerate lag.
- One store is the **source of truth**; everything else can be rebuilt from it.

---

## 9) Choosing a Store ⭐⭐⭐⭐⭐

### 9-1) The decision table

| Need | Pick |
|---|---|
| Money, inventory, multi-row invariants | SQL (PostgreSQL / MySQL) |
| Ad-hoc analytics and joins | SQL warehouse (BigQuery, Snowflake) |
| Very high write throughput, known queries, multi-region | wide-column (Cassandra) / DynamoDB |
| Low-latency cache, counters, rate limits | Redis |
| Flexible nested documents | MongoDB / Firestore |
| Relationship traversal | graph DB |
| Full-text and faceted search | Elasticsearch / OpenSearch |
| Metrics and time series | a time-series DB, or Cassandra with time buckets |
| Global scale **and** SQL + ACID | Spanner / CockroachDB |

The default answer: **"start with PostgreSQL unless a requirement rules it out"** — then name
the requirement (write volume, latency, schema variability, global distribution) and the store
that satisfies it.

### 9-2) Questions to ask before choosing

1. **What are the queries?** Known and few → NoSQL is viable. Ad-hoc → SQL.
2. **What invariants span rows?** Any → transactions; prefer relational or NewSQL.
3. **What are the read and write rates, and the data size in a year?** Fits one primary
   (with replicas)? Then relational is simpler.
4. **What latency, and from where?** Single region vs global users.
5. **What consistency does each read need?** Strong for money, read-your-writes for a profile,
   eventual for a feed.
6. **Who operates it?** Managed (DynamoDB, Cloud SQL, Atlas) vs self-hosted (Cassandra) is
   often the deciding cost.

---

## 10) Worked Design Questions ⭐⭐⭐⭐

### 10-1) Chat messages

**Cassandra (or another wide-column store such as Bigtable / ScyllaDB), partitioned by
conversation and time bucket.**

- Huge write volume, append-mostly, read as "latest N in a conversation" — a clustering-key range read.
- Partition `(conversation_id, bucket)`, cluster by `message_id` (a time-based id) descending.
- Unread counts and presence in Redis; message search in Elasticsearch via CDC.
- Trade-off: no ad-hoc queries; each new access pattern is a new table.

### 10-2) Shopping cart and checkout

**The cart can live in a key-value store; the checkout must be relational.**

- **Cart**: DynamoDB or Redis keyed by `user_id` — a single-key read/write, high availability
  matters more than strict consistency, and losing a cart is annoying, not catastrophic.
- **Order + payment + inventory**: PostgreSQL / MySQL in one transaction, with a unique
  constraint or conditional update so stock cannot go negative and an order is not charged twice.
- The handoff: on checkout, read the cart, then run the relational transaction with an
  **idempotency key** so a retried request does not create a second order.

### 10-3) User profiles

**PostgreSQL first; a document store if the profile shape varies a lot per user.**

- Read-heavy, keyed by `user_id`, modest size — a relational table with read replicas and
  a cache in front is enough for a very large user base.
- PostgreSQL `JSONB` handles optional, varying attributes without leaving SQL.
- Read-your-writes matters (users notice a stale profile): read from the primary after an
  update, or invalidate the cache on write.
- At global scale with multi-region writes: DynamoDB global tables or Spanner — and name the
  consistency each gives.

### 10-4) Time-series metrics

**A time-series database (Prometheus for monitoring, InfluxDB / TimescaleDB for app metrics),
or Cassandra / Bigtable with time-bucketed partitions at very large scale.**

- Append-only, written in time order, read as ranges and downsampled aggregates.
- Partition by `(metric, series, time bucket)`; keep raw data for days, roll up to
  minute / hour aggregates for longer retention.
- Long-term analytics go to columnar files (Parquet) or ClickHouse.
- Trap: high-cardinality labels (a `user_id` label on every metric) explode the series count.

---

## 11) Common Misconceptions ⭐⭐⭐⭐

### 11-1) "NoSQL has no schema"

**NoSQL has no schema *enforced by the database*; the schema still exists — in the application
code that reads the data — so it is schema-on-read, not schema-less.**

- Every reader must handle every version of the document ever written.
- Schema changes become application migrations: write both shapes, backfill, then stop
  reading the old one.
- MongoDB `$jsonSchema` validators and Cassandra's typed CQL tables do enforce structure.

### 11-2) "NoSQL cannot do transactions"

**Many NoSQL stores do support transactions now — with narrower scope or higher cost than a
relational database.**

| Store | Transactions |
|---|---|
| MongoDB | multi-document ACID transactions (replica sets 4.0+, sharded clusters 4.2+) |
| DynamoDB | `TransactWriteItems` / `TransactGetItems`, up to 100 items across tables |
| Cassandra | lightweight transactions (compare-and-set on one partition, via Paxos) |
| Redis | `MULTI` / `EXEC` (no rollback) and atomic Lua scripts |

### 11-3) Other misconceptions worth correcting

- **"SQL does not scale."** Relational databases scale reads with replicas and writes with
  sharding (Vitess, Citus) or NewSQL; most products never outgrow one well-tuned primary.
- **"NoSQL is always faster."** It is fast for the access pattern it was modelled for; an
  unplanned query can mean a full scan.
- **"Eventual consistency means the data is wrong."** It means replicas may lag briefly;
  with the right reads (quorum, `ConsistentRead`, primary reads) you can get strong reads
  where they matter.
- **"CAP means pick two of three."** Partition tolerance is not optional; the choice is what
  to do *during* a partition.

---

## 12) Quick Q → A

### 12-1) Rapid-fire questions

- **Q: Why can't you just add read replicas to scale writes?** — Every write still goes to the
  single primary; replicas only add read capacity. Writes scale by sharding.
- **Q: What makes a good partition key?** — High cardinality, even access, and present in
  every hot query; it should keep one request's data in one partition.
- **Q: Why are deletes expensive in Cassandra?** — A delete is a tombstone write that reads
  must skip until compaction after `gc_grace_seconds`.
- **Q: When is a GSI read stale?** — Always potentially: GSIs are updated asynchronously and
  support only eventually consistent reads.
- **Q: How do you stop two users booking the same seat?** — A unique constraint or
  `UPDATE ... WHERE status = 'free'` in a relational DB; a conditional write
  (`attribute_not_exists`) in DynamoDB.
- **Q: B-tree or LSM for a write-heavy log of events?** — LSM: sequential writes, compaction
  in the background; reads are the cost.
- **Q: What is replication lag and how do you hide it from a user?** — Replicas apply the
  primary's log late; route a user's reads to the primary right after they write.
- **Q: How do you keep a search index in sync with the database?** — CDC from the database
  log (Debezium → Kafka → indexer), not dual writes.
- **Q: What does `R + W > N` buy you?** — Read and write sets overlap, so a read can see the
  latest acknowledged write; it does not by itself give linearizability.
- **Q: Where do NewSQL databases pay for their guarantees?** — Consensus latency on every
  write, worst across regions.
