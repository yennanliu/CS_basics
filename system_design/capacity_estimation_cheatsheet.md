# Capacity Estimation Cheatsheet

A general-purpose math framework for system design interviews. Apply this pattern to any read/write-heavy system.

---

## 1. Pattern: Think in Tiers

Most systems have two fundamentally different tiers with different cost profiles:

| Tier | Examples | Bottleneck |
| :--- | :--- | :--- |
| **Web / API Tier** | REST servers, GraphQL, Auth | Network I/O, lightweight |
| **Execution / Worker Tier** | Judge runner, ML inference, Video transcoder | CPU / Memory, expensive |

> **Key insight:** Separate your estimates by tier. Lumping them together leads to under/over-provisioning.

---

## 2. Flow: How to Walk Through Estimation

```
1. Define the Unit of Work
   └─ What is 1 "request"? (submission, video upload, message, etc.)

2. State Assumptions
   └─ Processing time, payload size, safety buffer (default: 20%)

3. Traffic Estimation (QPS)
   └─ Daily volume → Avg QPS → Peak QPS

4. Compute Estimation (Workers / Servers)
   └─ Concurrency = Peak QPS × Processing Time
   └─ Servers = Concurrency × Buffer / vCPUs per server

5. Memory Estimation (RAM)
   └─ Active containers + Cache (Redis)

6. Storage Estimation (Disk)
   └─ Per-record size × Daily volume × Retention period

7. Bandwidth Estimation
   └─ Inbound: QPS × payload size
   └─ Outbound: QPS × response size

8. Summarize in a table
   └─ Per component: Resource type + Estimate
```

---

## 3. Essential Tricks: Math & Resource Calculation

### 3.1 Standard Assumptions to State Upfront

| Parameter | Typical Value | Notes |
| :--- | :--- | :--- |
| Processing time | 1–5 sec | Depends on workload |
| Overhead (startup, cleanup) | 0.5–1 sec | Add to processing time |
| Safety buffer | **20%** | Always add |
| Peak-to-average ratio | **10×** | Standard multiplier |
| vCPUs per server | 16–64 | c6g.4xlarge = 16 vCPU |
| Bytes in a KB/MB/GB | 10³ / 10⁶ / 10⁹ | Use powers of 10 for speed |

---

### 3.2 QPS Calculation

```
Avg QPS  = Daily Requests / 86,400 sec
Peak QPS = Avg QPS × 10   (10× spike factor)
```

**Stage reference table (memorize this):**

| Daily Volume | Avg QPS | Peak QPS (10×) |
| :--- | :--- | :--- |
| 10K | ~0.1 | ~1 |
| 1M | ~12 | ~120 |
| 10M | ~120 | ~1,200 |
| 100M | ~1,200 | ~12,000 |

---

### 3.3 Worker / Server Calculation (CPU-bound)

For any **CPU-bound** execution tier:

```
Concurrency (W) = Peak QPS × T_total
vCPUs needed    = W × 1.2 (buffer)
Servers         = vCPUs needed / vCPUs per server
```

**Example (1M submissions/day, T_total = 3s):**
```
Peak QPS    = 120
W           = 120 × 3 = 360 concurrent workers
vCPUs       = 360 × 1.2 = 432
Servers     = 432 / 16 ≈ 27 servers (c6g.4xlarge)
```

---

### 3.4 Memory Calculation

**Active workers (RAM):**
```
Total RAM = Concurrency × Memory per container
```

**Cache (Redis):**
```
Cache RAM = # objects × avg object size × overhead factor (10×)
```

**Example:**
```
360 containers × 256 MB = ~92 GB active RAM
5,000 problems × 100 KB × 10 = ~5 GB → use 16 GB Redis node
```

---

### 3.5 Storage Calculation

```
Record size  = sum of all fields (bytes)
Daily write  = Record size × Daily volume
Annual       = Daily write × 365
```

**Example (submission record):**
```
id(8B) + user_id(8B) + problem_id(4B) + code(5KB) + status(1B) + ts(8B) ≈ 6 KB
1M/day × 6 KB = 6 GB/day → 6 × 365 ≈ 2.2 TB/year
```

**Companion storage (object store / S3):**
```
Static assets = # items × avg item size   (usually tiny, e.g., 5K problems × 1MB = 5 GB)
```
> Note: Static blob storage is cheap; flag **IOPS** as the real bottleneck, not capacity.

---

### 3.6 Bandwidth Calculation

```
Inbound  = Peak QPS × avg request payload
Outbound = Peak QPS × avg response payload
```

**Quick conversion:** 1 MB/s = 8 Mbps

**Example (120 submissions/sec):**
```
Inbound  = 120 × 5 KB  = 600 KB/s ≈ 5 Mbps   (negligible)
Outbound = 120 × 2 KB  = 240 KB/s ≈ 2 Mbps   (negligible)
```
> Bandwidth only becomes interesting at **100M+ DAU** or **video/streaming** workloads.

---

## 4. Summary Table Template

Copy-paste this at the end of any estimation:

| Component | Resource Type | Estimate |
| :--- | :--- | :--- |
| **API Servers** | Compute | 3–5 instances (general purpose) |
| **Worker / Execution Tier** | Compute | N instances (compute optimized) |
| **Database** | Storage | X TB / year |
| **Cache (Redis)** | Memory | X GB |
| **Message Queue** | Throughput | X messages / sec |
| **Object Store (S3)** | Storage | X GB (static) |

---

## 5. Pro Tips for the Interview

1. **Separate tiers early.** Web servers are cheap; execution workers are expensive. Interviewers want to see you know the difference.

2. **Always add 20% buffer.** Say it out loud — it signals production awareness.

3. **Peak = 10× average.** Use this universally unless the problem hints otherwise (e.g., a contest platform might be 50×).

4. **CPU-bound vs I/O-bound matters:**
   - CPU-bound → fewer, beefier compute-optimized instances.
   - I/O-bound → more instances, focus on connection pooling and async.

5. **Multi-region costs.** If users are global, mention cross-region latency (~200ms) and data transfer costs. Suggest co-locating workers near the DB.

6. **Flag IOPS separately from capacity** for disk-heavy workloads (e.g., test case loading). NVMe/SSD local cache per worker is the standard answer.

7. **Round generously.** In interviews, 432 vCPUs → "about 30 servers" is fine. Precision beyond 1 sig fig wastes time.
