# Performance Tuning FAQ

Practical, language-neutral notes on how to find and fix performance problems,
with Java-specific tips at the end.

## Overview

### Golden Rules
- **Measure, don't guess.** Profile first; intuition about bottlenecks is usually wrong.
- **Optimize the hot path.** 90% of time is spent in a small % of code (Amdahl's Law).
- **Fix the biggest bottleneck first**, then re-measure — the bottleneck moves.
- **Set a target.** "Fast enough" is a number (e.g. p99 < 200ms), not a feeling.
- **Beware micro-optimizations** that hurt readability for negligible gain.

### When to Tune
Only after you have (1) a reproducible workload, (2) a metric, and (3) a baseline.

---

## 1) Profiling Approach

A repeatable loop:

```text
1. Reproduce   → capture a realistic, repeatable workload
2. Measure     → get a baseline (latency, throughput, CPU, memory)
3. Locate      → profile to find the hot spot
4. Fix         → change ONE thing
5. Re-measure  → confirm improvement, watch for regressions elsewhere
6. Repeat      → until target is met
```

### Types of Profiling
| Type | Answers | Tools (examples) |
|------|---------|------------------|
| CPU / sampling | Where is time spent? | `perf`, async-profiler, `py-spy` |
| Allocation | What creates garbage? | JFR, memory profilers |
| Wall-clock / tracing | Where does a request wait? | distributed tracing, spans |
| Heap dump | What holds memory? | heap analyzers |

### Latency Percentiles
Never optimize on averages alone. Track **p50 / p95 / p99 / p99.9**.
Tail latency (p99) usually drives user-perceived slowness and SLA breaches.

---

## 2) Common Bottlenecks

### CPU-bound
- Symptoms: high CPU %, low wait time.
- Causes: inefficient algorithms (`O(n^2)` on large `n`), excessive
  serialization/regex, tight loops, unnecessary recomputation.
- Fixes: better algorithm/data structure, memoization/caching, batch work,
  parallelism (only if cores are idle).

### Memory-bound
- Symptoms: high GC time, swapping, OOM.
- Causes: leaks (unbounded caches/collections), large object graphs, boxing.
- Fixes: cap cache size, stream instead of loading all in memory, reuse buffers,
  fix leaks (objects unintentionally kept reachable).

### I/O-bound (disk / file)
- Symptoms: low CPU, high wait time.
- Fixes: buffer reads/writes, sequential over random access, larger batches,
  async/non-blocking I/O, compression to trade CPU for I/O.

### Database-bound
- Usually the #1 real-world bottleneck. See section 4.

### Network-bound
- Symptoms: latency dominated by round trips.
- Causes: chatty APIs (N+1 calls), large payloads, no connection reuse.
- Fixes: batch requests, reduce round trips, connection pooling/keep-alive,
  compression, move computation closer to data, CDN for static assets.

---

## 3) Caching

Caching trades memory/staleness for speed. Add it once you know reads dominate
and data is reused.

### Layers (client → server)
- Browser / client cache
- CDN / edge cache (static + cacheable responses)
- Application in-memory cache (fastest, per-instance)
- Distributed cache (shared across instances)
- Database buffer/query cache

### Key Decisions
- **Eviction policy**: LRU (most common), LFU, TTL-based.
- **Invalidation**: the hard part. Options: TTL expiry, write-through,
  write-behind, explicit invalidation on write.
- **Cache-aside pattern** (most common):
  ```text
  read: check cache → miss → read DB → populate cache → return
  write: write DB → invalidate/update cache
  ```

### Pitfalls
- **Stampede / thundering herd**: many misses hit the DB at once when a hot key
  expires. Mitigate with locking, request coalescing, or jittered TTLs.
- **Stale data**: pick a tolerable staleness window explicitly.

---

## 4) Database Query & Index Tuning

### Find the slow query
- Use the DB's `EXPLAIN` / `EXPLAIN ANALYZE` to read the query plan.
- Look for **full table scans** where an index seek is expected.

### Indexing
- Add indexes on columns used in `WHERE`, `JOIN`, `ORDER BY`.
- **Composite index** column order matters — leftmost prefix rule.
- **Covering index**: include all selected columns so the DB never touches the table.
- Trade-off: indexes speed reads but slow writes and use space. Don't over-index.

### Common fixes
| Problem | Fix |
|---------|-----|
| N+1 queries | Batch / join / eager fetch |
| `SELECT *` | Select only needed columns |
| Missing index | Add appropriate index |
| Large offset pagination | Keyset ("seek") pagination |
| Lock contention | Shorter transactions, right isolation level |
| Repeated heavy reads | Cache, read replicas |

- Use **connection pooling** — connection setup is expensive.
- Denormalize or precompute (materialized views) for read-heavy analytics.

---

## 5) JVM Tuning Basics (Java)

Reach for JVM flags only after code and DB are tuned.

### Heap
- `-Xms` / `-Xmx`: set initial and max heap; set **equal** in servers to avoid
  resize pauses.
- Too small → frequent GC / OOM. Too large → long pauses, wasted RAM.

### Garbage Collector
- **G1** (default on modern JDKs): balanced, region-based, good general choice.
- **ZGC / Shenandoah**: low-pause collectors for large heaps / latency-sensitive apps.
- Goal: minimize **GC pause time** and **GC overhead %**.

### Observe before tuning
- Enable GC logging and **JDK Flight Recorder (JFR)** + Mission Control.
- Watch: allocation rate, pause times, promotion to old gen, heap after full GC.

### Java code-level tips
- Prefer `StringBuilder` over `+` in loops.
- Presize collections (`new ArrayList<>(expectedSize)`) to avoid resizes.
- Avoid autoboxing in hot loops (use primitives / primitive streams).
- Reuse expensive objects (thread-safe ones): `Pattern`, `DateTimeFormatter`.
- Prefer streaming/lazy processing over materializing huge collections.

---

## 6) Big-O of the Hot Path

The single biggest algorithmic win is usually reducing complexity class:

| From | To | How |
|------|----|----|
| `O(n^2)` nested scan | `O(n)` | Hash map lookup instead of inner loop |
| `O(n)` repeated lookups | `O(1)` | Use a `Set`/`Map` instead of a `List` |
| `O(n log n)` re-sorting | `O(1)` amortized | Keep data sorted / use a heap |
| Repeated recomputation | `O(1)` per call | Memoize / cache |

Checklist for a hot path:
1. What is the input size `n`, and how does it grow?
2. What is the current complexity? Is there needless nested iteration?
3. Is the right data structure used (hash for lookup, heap for top-k, etc.)?
4. Is work repeated that could be cached or precomputed?
5. Can the work be done incrementally instead of from scratch each time?

> Rule of thumb: fixing an `O(n^2)` → `O(n)` on large input beats any amount of
> constant-factor JVM tuning.
