# Note for System design

A checklist/outline for the system-design interview. Use the framework in
section 0 to structure any question; the later sections are the building blocks
and numbers to pull from as needed.

## 0) The Interview Framework (drive the discussion in this order)

```text
1. Requirements        → functional + non-functional; scope it down
2. Estimation          → scale: users, QPS, storage, bandwidth
3. API design          → the key endpoints / contracts
4. Data model          → entities, schema, SQL vs NoSQL
5. High-level design    → boxes & arrows; happy path end-to-end
6. Deep dives          → 1-2 components in depth (the interesting parts)
7. Bottlenecks & scale → find limits, add caching / sharding / queues
```

### Step 1 — Requirements
- **Functional**: what must it do? (e.g. post, follow, search, upload).
- **Non-functional**: scale, latency, availability, consistency, durability.
- **Explicitly scope out** things to keep the problem tractable. Confirm with
  the interviewer before designing.

### Step 2 — Estimation (back-of-envelope)
- Users → DAU → **QPS** (peak ≈ 2-3x average).
- **Read:write ratio** (many systems are ~100:1 read-heavy → cache/replicas).
- Storage per record x records/day x retention.
- Bandwidth = payload size x QPS.

### Step 3 — API
- Choose style: REST / RPC / GraphQL.
- Define the few core endpoints with request/response and pagination.

### Step 4 — Data Model
- Identify entities and relationships.
- SQL vs NoSQL (see section 5). Note indexes and access patterns.

### Step 5 — High-Level Design
- Draw client → LB → app servers → cache/DB → async workers.
- Walk one request end-to-end before optimizing.

### Step 6 — Deep Dive
- Pick the component the interviewer cares about (e.g. the feed, the ID
  generator, the search index) and design it in detail.

### Step 7 — Bottlenecks
- Single points of failure, hot spots, the DB, the network.
- Apply: caching, replication, sharding, queues, CDN, rate limiting.

---

## 1) Concept (before starting)

- Scalability (part 1)
	- Vertical scaling
	- Horizontal scaling
	- Caching
	- Load balancing
	- Database replication
	- Database partitioning

- Scalability (part 2)
	- Clones
	- Databases
	- Caches
	- Asynchronism

## 2) Performance vs scalability
- **Performance**: it's fast for a single user.
- **Scalability**: it stays fast as load (users/data) grows.
- A system can be performant but not scalable (degrades under load), or scalable
  but not fast (handles load but each request is slow).

## 3) Latency vs throughput
- **Latency**: time to handle one request (measure p50/p95/**p99**, not average).
- **Throughput**: requests handled per unit time (QPS/TPS).
- Aim for max throughput at acceptable latency. Batching raises throughput but
  can raise latency.

## 4) Availability vs consistency

### CAP theorem
In a network **partition (P)** you must choose:
- **CP** — consistency over availability (reject/stall to stay correct; e.g. many
  traditional RDBMS, coordination services).
- **AP** — availability over consistency (serve possibly stale data; e.g. many
  eventually-consistent key-value stores).
- With no partition you can have both C and A. Partitions are unavoidable in
  distributed systems, so CAP is really a P-time trade-off.

### Consistency models
| Model | Guarantee |
|-------|-----------|
| **Strong** | Every read sees the latest write |
| **Eventual** | Replicas converge given no new writes (temporarily stale) |
| **Causal** | Causally related ops seen in order |
| **Read-your-writes** | You always see your own updates |

### PACELC
Extends CAP: if Partition → choose A or C; **Else** (normal ops) → choose
**Latency** or **Consistency**.

### Availability math
- "Nines": 99.9% ≈ 8.7h/yr down; 99.99% ≈ 52min/yr; 99.999% ≈ 5min/yr.
- Components **in series** multiply (both must be up); **in parallel/redundant**
  improve availability.

---

## 5) Building Blocks

### Load Balancer
- Distributes traffic across servers; enables horizontal scaling + failover.
- Algorithms: round-robin, least-connections, IP/consistent hashing.
- L4 (transport) vs L7 (application, content-aware).
- Health checks remove dead nodes. Beware it becoming a SPOF (run redundant LBs).

### Cache
- Layers: client → CDN → app in-memory → distributed cache → DB cache.
- Patterns: **cache-aside** (lazy load), read-through, write-through,
  write-behind.
- Eviction: **LRU** (common), LFU, TTL.
- Pitfalls: invalidation ("hardest problem"), stale reads, **thundering herd**
  on hot-key expiry.

### Database — Replication
- **Leader-follower (primary-replica)**: writes to leader, reads from replicas
  → scales reads, adds redundancy. Replica lag → eventual consistency on reads.
- **Multi-leader / leader-less**: higher write availability, conflict resolution
  needed.

### Database — Sharding / Partitioning
- Split data across nodes to scale writes & storage.
- **Strategies**: range-based, hash-based, **consistent hashing** (minimizes
  reshuffling when nodes change), directory-based.
- Challenges: **hot spots** (uneven keys), cross-shard joins/transactions,
  rebalancing. Choose a shard key with even distribution and matching access
  patterns.

### SQL vs NoSQL
| | SQL (relational) | NoSQL |
|---|------------------|-------|
| Schema | Fixed, normalized | Flexible |
| Scaling | Vertical (harder to shard) | Horizontal by design |
| Transactions | Strong ACID | Often BASE / limited |
| Best for | Complex queries, relations, integrity | High write volume, scale, flexible/denormalized data |
- NoSQL flavors: key-value, document, wide-column, graph.

### Message Queue / Async
- Decouples producers from consumers; smooths spikes; enables retries.
- Use for work that can be async (emails, notifications, media processing).
- Delivery semantics: at-most-once / **at-least-once** (needs idempotent
  consumers) / exactly-once (hard).
- Watch: ordering, backpressure, dead-letter queues, consumer lag.

### CDN
- Caches static (and some dynamic) content at edge locations near users →
  lower latency, offloads origin. Push vs pull. Set TTLs / cache-control.

### Other pieces to mention when relevant
- **API Gateway / reverse proxy**: routing, auth, rate limiting, TLS termination.
- **Rate limiting**: token bucket / leaky bucket / sliding window.
- **Search index** (inverted index) for full-text search.
- **Blob/object storage** for large files (store URLs in the DB, not blobs).
- **Coordination service** for leader election / config.
- **Monitoring, logging, tracing** for observability.

---

## 6) Common Numbers to Know

Latency ballparks (order of magnitude):
| Operation | Time |
|-----------|------|
| L1 cache reference | ~1 ns |
| Main memory reference | ~100 ns |
| SSD random read | ~100 µs |
| Round trip within datacenter | ~0.5 ms |
| Disk (HDD) seek | ~10 ms |
| Packet round trip across continents | ~150 ms |

Rules of thumb:
- Memory is fast, disk is slow, network is slower — minimize round trips.
- Reads >> writes in most consumer systems → cache and replicate reads.
- ~86,400 seconds/day → 1M requests/day ≈ ~12 QPS average.
- Peak QPS ≈ 2-3x average.

---

## 7) Lambda VS Kappa
- Lambda 
	- Stream layer + Batch layer + Service layer
	- Query = λ (Complete data) = λ (live streaming data) * λ (Stored data)
	- Event Sourcing : save whole events 

- Kappa 
	- Query = K (New Data) = K (Live streaming data)
- https://towardsdatascience.com/a-brief-introduction-to-two-data-processing-architectures-lambda-and-kappa-for-big-data-4f35c28005bb
- https://blog.csdn.net/brucesea/article/details/45937875
