# DevOps Engineering FAQ

## Table of Contents

- [Estimating Kubernetes Resources from QPS/Traffic](#estimating-kubernetes-resources-from-qpstraffic)

---

## Estimating Kubernetes Resources from QPS/Traffic

Estimating Kubernetes (K8s) resources from QPS/traffic isn't an exact formula—it's a structured **modeling + measurement + iteration** process. As a DevOps engineer, you're basically translating **workload characteristics -> resource consumption -> K8s requests/limits**.

---

### 1) Start with a Mental Model

At a high level:

```
Total CPU    ≈ QPS × CPU cost per request
Total Memory ≈ baseline + (concurrency × memory per request)
```

So you need to answer 3 key questions:

1. How expensive is **one request**?
2. How many requests happen **at the same time** (concurrency)?
3. What's the **baseline overhead** of the app?

---

### 2) Measure Per-Request Cost (the MOST Important Step)

You cannot guess this accurately—you must benchmark.

#### Run a load test (e.g. k6, Locust, JMeter)

Simulate realistic traffic and measure:

- CPU usage at stable QPS
- Memory usage
- Latency (p50, p95, p99)

#### Derive

**CPU per request:**

```
CPU per request = total CPU used / QPS
```

Example:

- 2 CPU cores used at 200 QPS

```
CPU per request = 2 / 200 = 0.01 cores = 10 millicores
```

**Memory per request:**

Memory is NOT per request linearly—it depends on concurrency.

```
Memory per request ≈ (peak memory - baseline memory) / concurrent requests
```

---

### 3) Estimate Concurrency from QPS

Use **Little's Law**:

```
Concurrency = QPS × Avg Response Time
```

Example:

- QPS = 500
- Avg latency = 200ms = 0.2s

```
Concurrency = 500 × 0.2 = 100 requests in-flight
```

---

### 4) Calculate Total Resource Needs

#### CPU

```
Total CPU = QPS × CPU per request
```

Add headroom (important):

```
Final CPU = Total CPU × 1.5 ~ 2
```

#### Memory

```
Total Memory = Baseline + (Concurrency × Memory per request)
```

Add buffer (GC, spikes):

```
Final Memory = Total Memory × 1.3 ~ 1.5
```

---

### 5) Translate into Pod Sizing

Now decide:

- **Option A:** Scale vertically (bigger pods)
- **Option B:** Scale horizontally (more pods, preferred)

#### Per-pod sizing strategy

Pick a **target QPS per pod**:

Example:

- Total QPS = 1000
- Want ~100 QPS per pod
- -> 10 pods

**Per-pod CPU:**

```
CPU per pod = QPS per pod × CPU per request
```

**Per-pod Memory:**

```
Memory per pod = baseline + (concurrency per pod × memory per request)
```

---

### 6) Set Kubernetes Requests & Limits

In Kubernetes:

- **requests** = guaranteed resources (scheduler uses this)
- **limits** = max allowed

#### CPU

- requests = ~70-80% of expected usage
- limits = ~1.5-2x requests

#### Memory

- requests = close to actual usage
- limits = slightly above (OOM is dangerous)

---

### 7) Example End-to-End

#### Given

- QPS = 1000
- Avg latency = 100ms (0.1s)
- CPU per request = 5 millicores
- Baseline memory = 200MB
- Memory per request = 1MB

#### Step 1: Concurrency

```
Concurrency = 1000 × 0.1 = 100
```

#### Step 2: Total CPU

```
CPU = 1000 × 5m = 5000m = 5 cores
With buffer -> 7-10 cores
```

#### Step 3: Total Memory

```
Memory = 200MB + (100 × 1MB) = 300MB
With buffer -> ~400MB
```

#### Step 4: Pod split (10 pods)

Per pod:

- QPS = 100
- CPU = 0.5 core
- Memory ≈ 40MB + baseline slice

#### Step 5: K8s config

```yaml
resources:
  requests:
    cpu: "500m"
    memory: "64Mi"
  limits:
    cpu: "1000m"
    memory: "128Mi"
```

---

### 8) Real-World Factors

#### Traffic Shape

- Burst vs steady
- Peak vs average (always design for peak)

#### Workload Type

Different apps behave VERY differently:

| Type                         | CPU      | Memory   |
| ---------------------------- | -------- | -------- |
| CPU-bound (encoding, crypto) | high     | low      |
| IO-bound (API calls)         | low      | moderate |
| JVM apps                     | moderate | high     |
| Node.js                      | low      | moderate |

#### GC & Runtime Overhead

- JVM -> large memory buffer needed
- Go -> more predictable
- Python -> spikes possible

#### External Dependencies

If your app waits on DB, Redis, or external APIs:

- CPU per request is LOW but concurrency is HIGH

---

### 9) Use Autoscaling (Critical)

#### HPA (Horizontal Pod Autoscaler)

Scale based on:

- CPU (default)
- Or better -> custom metrics:
  - QPS
  - Latency
  - Queue length

#### Rule of thumb

- CPU target: ~60-70%
- Keep headroom for spikes

---

### 10) Practical Workflow (What Good Teams Do)

1. Benchmark (load test)
2. Derive per-request cost
3. Estimate peak QPS
4. Calculate baseline resources
5. Deploy with conservative buffer
6. Observe (Prometheus/Grafana)
7. Tune requests/limits
8. Add HPA

---

### 11) Golden Shortcuts (When You Lack Data)

If no benchmark yet:

- Start with:
  - CPU: 100-500m per pod
  - Memory: 128-512Mi
- Load test ASAP
- Adjust aggressively
