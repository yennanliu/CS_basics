# Backend Programming Notes for BE Role

> Focus: High Traffic, Concurrency, Lock Patterns, and Practical Implementation

## Table of Contents

1. [High Traffic Patterns](#high-traffic-patterns)
   - [Idempotent Request Processing](#1-idempotent-request-processing)
   - [Rate Limiting](#2-rate-limiting)
   - [Circuit Breaker](#3-circuit-breaker)
   - [Request Deduplication with Distributed Lock](#4-request-deduplication-with-distributed-lock)
2. [Concurrency Patterns](#concurrency-patterns)
   - [ConcurrentHashMap Operations](#concurrenthashmap-operations)
   - [Atomic Operations](#atomic-operations)
   - [Lock Strategies](#lock-strategies)
3. [API Design Patterns](#api-design-patterns)
4. [Java Design Patterns for Backend](#java-design-patterns-for-backend)

---

## High Traffic Patterns

### 1. Idempotent Request Processing

**Problem**: Design a mechanism that prevents duplicate processing of requests with the same `requestId` within a configurable time window.

#### Initial Design (V0 - Interview Whiteboard)

```java
/**
 * Design a mechanism that prevents duplicate processing of requests
 * with the same requestId within a configurable time window
 *
 * Key considerations:
 * - Is duplicate? -> check requestId
 * - Configurable time window
 * - Thread safety
 * - Lock timeout
 */

int TIME_WINDOW = 10;
int LOCK_TIME_OUT = 60; // 60 sec

class Request {
    String reqId;
    String msg;
    long timestamp; // unix time
}

class Resp {
    String status;
}

public class IdempotentRequestProcessor {

    Map<String, Request> processedReq;

    IdempotentRequestProcessor() {
        this.map = new HashMap();  // NOT thread-safe!
    }

    public Resp processRequest(Request req) {
        // Problem: Race condition here!
        if (this.map.contains(req.reqId)) {
            if (map.get(req.reqId).timestamp < req.time - TIME_WINDOW) {
                return new Resp("duplicated req in time window");
            }
        }

        this.put(req.reqId, req);
        return new Resp("req is received");
    }
}
```

**Issues with V0:**
1. `HashMap` is NOT thread-safe
2. Check-then-act is NOT atomic (race condition)
3. No cleanup mechanism for expired entries
4. Lock timeout not implemented

---

#### Solution V1: ConcurrentHashMap with Atomic Check

```java
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.time.Instant;

@Data
@AllArgsConstructor
class Request {
    private String reqId;
    private String msg;
    private long timestamp; // Unix timestamp in seconds
}

@Data
@AllArgsConstructor
class Resp {
    private String status;
    private String message;
}

public class IdempotentRequestProcessor {

    // Configurable window in seconds
    private static final int TIME_WINDOW = 10;

    // Stores RequestId -> Timestamp of first successful receipt
    private final Map<String, Long> processedReqs = new ConcurrentHashMap<>();

    public Resp processRequest(Request req) {
        long currentTime = Instant.now().getEpochSecond();

        // Check if duplicate within window
        Long existingTimestamp = processedReqs.get(req.getReqId());

        if (existingTimestamp != null) {
            if (currentTime - existingTimestamp < TIME_WINDOW) {
                return new Resp("REJECTED", "Duplicate request within time window.");
            }
            // Outside window - treat as fresh request
        }

        // Store the request timestamp
        processedReqs.put(req.getReqId(), currentTime);

        return executeBusinessLogic(req);
    }

    private Resp executeBusinessLogic(Request req) {
        try {
            // Simulate processing
            return new Resp("SUCCESS", "Request processed: " + req.getReqId());
        } catch (Exception e) {
            // If fails, remove from map so user can retry
            processedReqs.remove(req.getReqId());
            return new Resp("ERROR", "Processing failed.");
        }
    }

    // TODO: Add cleanup task (ScheduledExecutor) to prevent infinite growth
}
```

**Improvement but still has issue:**
- `get()` then `put()` is NOT atomic - still has race condition!

---

#### Solution V2: Using `compute()` for True Atomicity

```java
import java.util.concurrent.*;

class Request {
    String reqId;
    String msg;
    long timestamp; // epoch seconds

    public Request(String reqId, String msg, long timestamp) {
        this.reqId = reqId;
        this.msg = msg;
        this.timestamp = timestamp;
    }
}

class Resp {
    String status;

    public Resp(String status) {
        this.status = status;
    }
}

public class IdempotentRequestProcessor {

    private final long TIME_WINDOW_SECONDS = 10;

    // reqId -> last processed timestamp
    private final ConcurrentHashMap<String, Long> processedReq = new ConcurrentHashMap<>();

    public Resp processRequest(Request req) {
        long now = req.timestamp;

        // Atomic check + update using compute()
        Long[] resultHolder = new Long[1];
        boolean[] isDuplicate = new boolean[1];

        processedReq.compute(req.reqId, (key, oldTimestamp) -> {
            if (oldTimestamp == null) {
                // First time seeing this request
                resultHolder[0] = now;
                isDuplicate[0] = false;
                return now;
            }

            if (now - oldTimestamp <= TIME_WINDOW_SECONDS) {
                // Duplicate within window - keep old timestamp
                isDuplicate[0] = true;
                return oldTimestamp;
            }

            // Outside window - update timestamp
            resultHolder[0] = now;
            isDuplicate[0] = false;
            return now;
        });

        if (isDuplicate[0]) {
            return new Resp("duplicate request");
        }

        // Process actual request
        return process(req);
    }

    private Resp process(Request req) {
        try {
            // Simulate business logic
            return new Resp("processed: " + req.reqId);
        } catch (Exception e) {
            return new Resp("failed");
        }
    }
}
```

---

#### Solution V3: Production-Ready with Cleanup

```java
import java.util.concurrent.*;
import java.time.Instant;

public class IdempotentRequestProcessor {

    private final long timeWindowSeconds;
    private final ConcurrentHashMap<String, Long> processedReqs = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupExecutor;

    public IdempotentRequestProcessor(long timeWindowSeconds, long cleanupIntervalSeconds) {
        this.timeWindowSeconds = timeWindowSeconds;

        // Schedule periodic cleanup to prevent memory leak
        this.cleanupExecutor = Executors.newSingleThreadScheduledExecutor();
        cleanupExecutor.scheduleAtFixedRate(
            this::cleanupExpiredEntries,
            cleanupIntervalSeconds,
            cleanupIntervalSeconds,
            TimeUnit.SECONDS
        );
    }

    public Resp processRequest(Request req) {
        long now = Instant.now().getEpochSecond();

        boolean[] isDuplicate = {false};

        processedReqs.compute(req.getReqId(), (key, oldTimestamp) -> {
            if (oldTimestamp == null) {
                return now;
            }
            if (now - oldTimestamp <= timeWindowSeconds) {
                isDuplicate[0] = true;
                return oldTimestamp;
            }
            return now;
        });

        if (isDuplicate[0]) {
            return new Resp("REJECTED", "Duplicate request");
        }

        return executeBusinessLogic(req);
    }

    private void cleanupExpiredEntries() {
        long now = Instant.now().getEpochSecond();
        processedReqs.entrySet().removeIf(
            entry -> now - entry.getValue() > timeWindowSeconds * 2
        );
    }

    public void shutdown() {
        cleanupExecutor.shutdown();
    }

    private Resp executeBusinessLogic(Request req) {
        // Business logic here
        return new Resp("SUCCESS", "Processed: " + req.getReqId());
    }
}
```

---

#### Solution V4: Distributed Version with Redis

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

public class DistributedIdempotentProcessor {

    private final Jedis redis;
    private final long timeWindowSeconds;
    private static final String KEY_PREFIX = "idempotent:";

    public DistributedIdempotentProcessor(Jedis redis, long timeWindowSeconds) {
        this.redis = redis;
        this.timeWindowSeconds = timeWindowSeconds;
    }

    public Resp processRequest(Request req) {
        String key = KEY_PREFIX + req.getReqId();

        // SETNX with expiration - atomic operation
        String result = redis.set(
            key,
            String.valueOf(System.currentTimeMillis()),
            SetParams.setParams().nx().ex(timeWindowSeconds)
        );

        if (result == null) {
            // Key already exists - duplicate request
            return new Resp("REJECTED", "Duplicate request");
        }

        // First time - process the request
        try {
            return executeBusinessLogic(req);
        } catch (Exception e) {
            // On failure, delete key to allow retry
            redis.del(key);
            return new Resp("ERROR", "Processing failed");
        }
    }

    private Resp executeBusinessLogic(Request req) {
        return new Resp("SUCCESS", "Processed: " + req.getReqId());
    }
}
```

---

### Key Takeaways for Idempotent Processing

| Aspect | Single Instance | Distributed |
|--------|-----------------|-------------|
| Storage | `ConcurrentHashMap` | Redis / Database |
| Atomicity | `compute()` / `computeIfAbsent()` | `SETNX` with TTL |
| Cleanup | `ScheduledExecutorService` | Redis TTL auto-expiry |
| Failure Handling | Remove from map on error | Delete key on error |

---

### 2. Rate Limiting

#### Token Bucket Implementation

```java
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class TokenBucketRateLimiter {

    private final long maxTokens;
    private final long refillRate; // tokens per second
    private final AtomicReference<State> state;

    private static class State {
        final long tokens;
        final long lastRefillTime;

        State(long tokens, long lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }
    }

    public TokenBucketRateLimiter(long maxTokens, long refillRate) {
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.state = new AtomicReference<>(new State(maxTokens, System.nanoTime()));
    }

    public boolean tryAcquire() {
        while (true) {
            State currentState = state.get();
            long now = System.nanoTime();
            long elapsedNanos = now - currentState.lastRefillTime;

            // Calculate tokens to add
            long tokensToAdd = (elapsedNanos * refillRate) / 1_000_000_000L;
            long newTokens = Math.min(maxTokens, currentState.tokens + tokensToAdd);

            if (newTokens < 1) {
                return false; // No tokens available
            }

            // Try to consume one token
            State newState = new State(newTokens - 1, now);
            if (state.compareAndSet(currentState, newState)) {
                return true;
            }
            // CAS failed - retry
        }
    }
}
```

#### Sliding Window Rate Limiter

```java
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.Deque;

public class SlidingWindowRateLimiter {

    private final int maxRequests;
    private final long windowSizeMs;
    private final Deque<Long> requestTimestamps = new ConcurrentLinkedDeque<>();

    public SlidingWindowRateLimiter(int maxRequests, long windowSizeMs) {
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long windowStart = now - windowSizeMs;

        // Remove expired timestamps
        while (!requestTimestamps.isEmpty() && requestTimestamps.peekFirst() < windowStart) {
            requestTimestamps.pollFirst();
        }

        if (requestTimestamps.size() < maxRequests) {
            requestTimestamps.addLast(now);
            return true;
        }

        return false;
    }
}
```

---

### 3. Circuit Breaker

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CircuitBreaker {

    private enum State { CLOSED, OPEN, HALF_OPEN }

    private volatile State state = State.CLOSED;
    private final AtomicInteger failureCount = new AtomicInteger(0);
    private final AtomicLong lastFailureTime = new AtomicLong(0);

    private final int failureThreshold;
    private final long resetTimeoutMs;
    private final int halfOpenMaxCalls;
    private final AtomicInteger halfOpenCalls = new AtomicInteger(0);

    public CircuitBreaker(int failureThreshold, long resetTimeoutMs, int halfOpenMaxCalls) {
        this.failureThreshold = failureThreshold;
        this.resetTimeoutMs = resetTimeoutMs;
        this.halfOpenMaxCalls = halfOpenMaxCalls;
    }

    public boolean allowRequest() {
        switch (state) {
            case CLOSED:
                return true;

            case OPEN:
                if (System.currentTimeMillis() - lastFailureTime.get() > resetTimeoutMs) {
                    state = State.HALF_OPEN;
                    halfOpenCalls.set(0);
                    return true;
                }
                return false;

            case HALF_OPEN:
                return halfOpenCalls.incrementAndGet() <= halfOpenMaxCalls;

            default:
                return false;
        }
    }

    public void recordSuccess() {
        if (state == State.HALF_OPEN) {
            state = State.CLOSED;
        }
        failureCount.set(0);
    }

    public void recordFailure() {
        lastFailureTime.set(System.currentTimeMillis());

        if (state == State.HALF_OPEN) {
            state = State.OPEN;
            return;
        }

        if (failureCount.incrementAndGet() >= failureThreshold) {
            state = State.OPEN;
        }
    }

    public <T> T execute(SupplierWithException<T> action) throws Exception {
        if (!allowRequest()) {
            throw new CircuitBreakerOpenException("Circuit breaker is OPEN");
        }

        try {
            T result = action.get();
            recordSuccess();
            return result;
        } catch (Exception e) {
            recordFailure();
            throw e;
        }
    }

    @FunctionalInterface
    public interface SupplierWithException<T> {
        T get() throws Exception;
    }

    public static class CircuitBreakerOpenException extends RuntimeException {
        public CircuitBreakerOpenException(String message) {
            super(message);
        }
    }
}
```

---

### 4. Request Deduplication with Distributed Lock

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import java.util.UUID;

public class DistributedLockWithDedup {

    private final Jedis redis;
    private final String lockPrefix = "lock:";
    private final String processedPrefix = "processed:";

    public DistributedLockWithDedup(Jedis redis) {
        this.redis = redis;
    }

    /**
     * Process request with distributed lock and deduplication
     */
    public <T> T processWithLock(String requestId, long lockTimeoutSec,
                                  long dedupWindowSec, RequestProcessor<T> processor) {

        // 1. Check if already processed
        if (redis.exists(processedPrefix + requestId)) {
            throw new DuplicateRequestException("Request already processed");
        }

        // 2. Acquire distributed lock
        String lockKey = lockPrefix + requestId;
        String lockValue = UUID.randomUUID().toString();

        String acquired = redis.set(
            lockKey, lockValue,
            SetParams.setParams().nx().ex(lockTimeoutSec)
        );

        if (acquired == null) {
            throw new LockNotAcquiredException("Could not acquire lock");
        }

        try {
            // 3. Double-check after acquiring lock
            if (redis.exists(processedPrefix + requestId)) {
                throw new DuplicateRequestException("Request already processed");
            }

            // 4. Process the request
            T result = processor.process();

            // 5. Mark as processed
            redis.setex(processedPrefix + requestId, dedupWindowSec, "1");

            return result;

        } finally {
            // 6. Release lock (only if we own it)
            releaseLock(lockKey, lockValue);
        }
    }

    /**
     * Lua script for safe lock release (only release if we own the lock)
     */
    private void releaseLock(String lockKey, String lockValue) {
        String script =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end";

        redis.eval(script,
            java.util.Collections.singletonList(lockKey),
            java.util.Collections.singletonList(lockValue)
        );
    }

    @FunctionalInterface
    public interface RequestProcessor<T> {
        T process();
    }

    public static class DuplicateRequestException extends RuntimeException {
        public DuplicateRequestException(String message) { super(message); }
    }

    public static class LockNotAcquiredException extends RuntimeException {
        public LockNotAcquiredException(String message) { super(message); }
    }
}
```

---

## Concurrency Patterns

### ConcurrentHashMap Operations

| Method | Behavior | Use Case |
|--------|----------|----------|
| `putIfAbsent(k, v)` | Insert only if key absent | Simple dedup |
| `computeIfAbsent(k, func)` | Compute value lazily if absent | Lazy initialization |
| `compute(k, func)` | Always compute (atomic read-modify-write) | Conditional updates |
| `merge(k, v, func)` | Merge with existing value | Counters, aggregation |

```java
// Example: Atomic counter increment
ConcurrentHashMap<String, AtomicLong> counters = new ConcurrentHashMap<>();

// Safe increment
counters.computeIfAbsent("hits", k -> new AtomicLong(0)).incrementAndGet();

// Conditional update
counters.compute("hits", (k, v) -> {
    if (v == null) return new AtomicLong(1);
    v.incrementAndGet();
    return v;
});
```

---

### Atomic Operations

```java
import java.util.concurrent.atomic.*;

// AtomicInteger/Long for simple counters
AtomicLong counter = new AtomicLong(0);
counter.incrementAndGet();
counter.compareAndSet(expected, newValue);

// AtomicReference for object references
AtomicReference<State> stateRef = new AtomicReference<>(initialState);
stateRef.compareAndSet(oldState, newState);

// LongAdder for high-contention counters (better than AtomicLong)
LongAdder adder = new LongAdder();
adder.increment();
adder.sum(); // Get total
```

---

### Lock Strategies

#### 1. ReentrantLock with Timeout

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

ReentrantLock lock = new ReentrantLock();

public void processWithTimeout() throws InterruptedException {
    if (lock.tryLock(5, TimeUnit.SECONDS)) {
        try {
            // Critical section
        } finally {
            lock.unlock();
        }
    } else {
        throw new RuntimeException("Could not acquire lock");
    }
}
```

#### 2. ReadWriteLock for Read-Heavy Workloads

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

ReadWriteLock rwLock = new ReentrantReadWriteLock();

public Data read() {
    rwLock.readLock().lock();
    try {
        return data;
    } finally {
        rwLock.readLock().unlock();
    }
}

public void write(Data newData) {
    rwLock.writeLock().lock();
    try {
        this.data = newData;
    } finally {
        rwLock.writeLock().unlock();
    }
}
```

#### 3. StampedLock for Optimistic Read

```java
import java.util.concurrent.locks.StampedLock;

StampedLock lock = new StampedLock();

public Data optimisticRead() {
    long stamp = lock.tryOptimisticRead();
    Data localData = this.data;

    // Check if data was modified during read
    if (!lock.validate(stamp)) {
        // Fall back to regular read lock
        stamp = lock.readLock();
        try {
            localData = this.data;
        } finally {
            lock.unlockRead(stamp);
        }
    }
    return localData;
}
```

---

## API Design Patterns

### Idempotency Key Pattern

```java
@PostMapping("/orders")
public ResponseEntity<Order> createOrder(
    @RequestHeader("Idempotency-Key") String idempotencyKey,
    @RequestBody CreateOrderRequest request) {

    // Check if already processed
    Optional<Order> existing = orderService.findByIdempotencyKey(idempotencyKey);
    if (existing.isPresent()) {
        return ResponseEntity.ok(existing.get());
    }

    // Process and store with idempotency key
    Order order = orderService.createOrder(request, idempotencyKey);
    return ResponseEntity.status(HttpStatus.CREATED).body(order);
}
```

### Retry with Exponential Backoff

```java
public <T> T executeWithRetry(Supplier<T> action, int maxRetries) {
    int attempt = 0;
    long backoffMs = 100;

    while (true) {
        try {
            return action.get();
        } catch (Exception e) {
            attempt++;
            if (attempt >= maxRetries) {
                throw e;
            }

            try {
                Thread.sleep(backoffMs + (long)(Math.random() * backoffMs));
                backoffMs *= 2; // Exponential backoff
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ie);
            }
        }
    }
}
```

---

## Java Design Patterns for Backend

### 1. Singleton (Thread-Safe)

```java
// Double-checked locking with volatile
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}

// Better: Enum Singleton
public enum SingletonEnum {
    INSTANCE;

    public void doSomething() { }
}

// Better: Static holder (lazy + thread-safe)
public class SingletonHolder {
    private SingletonHolder() {}

    private static class Holder {
        static final SingletonHolder INSTANCE = new SingletonHolder();
    }

    public static SingletonHolder getInstance() {
        return Holder.INSTANCE;
    }
}
```

### 2. Producer-Consumer with BlockingQueue

```java
import java.util.concurrent.*;

public class ProducerConsumer {

    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>(100);
    private final ExecutorService consumers = Executors.newFixedThreadPool(4);

    public void start() {
        for (int i = 0; i < 4; i++) {
            consumers.submit(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Task task = queue.take(); // Blocks if empty
                        task.process();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
        }
    }

    public void submit(Task task) throws InterruptedException {
        queue.put(task); // Blocks if full
    }

    public void shutdown() {
        consumers.shutdownNow();
    }
}
```

### 3. Object Pool Pattern

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {

    private final BlockingQueue<Connection> pool;
    private final int maxSize;

    public ConnectionPool(int maxSize) {
        this.maxSize = maxSize;
        this.pool = new LinkedBlockingQueue<>(maxSize);

        // Pre-create connections
        for (int i = 0; i < maxSize; i++) {
            pool.offer(createConnection());
        }
    }

    public Connection acquire() throws InterruptedException {
        return pool.take();
    }

    public void release(Connection conn) {
        if (conn != null && !conn.isClosed()) {
            pool.offer(conn);
        } else {
            // Replace bad connection
            pool.offer(createConnection());
        }
    }

    private Connection createConnection() {
        // Create new connection
        return new Connection();
    }
}
```

---

## Common Interview Questions

### Q1: Why use `ConcurrentHashMap.compute()` instead of get+put?

**Answer**: `get()` followed by `put()` is NOT atomic. Between these two operations, another thread could modify the map. `compute()` performs the entire read-modify-write as a single atomic operation.

### Q2: Difference between `synchronized` and `ReentrantLock`?

| Feature | synchronized | ReentrantLock |
|---------|--------------|---------------|
| Lock timeout | No | Yes (`tryLock`) |
| Interruptible | No | Yes |
| Fairness | No | Configurable |
| Multiple conditions | No | Yes |
| Try lock | No | Yes |

### Q3: When to use `volatile`?

Use `volatile` when:
- Variable is accessed by multiple threads
- Only simple read/write operations (no read-modify-write)
- No compound operations needed

NOT sufficient for: `counter++` (read-modify-write needs AtomicInteger)

### Q4: How to prevent deadlock?

1. **Lock ordering**: Always acquire locks in the same order
2. **Lock timeout**: Use `tryLock()` with timeout
3. **Avoid nested locks**: Minimize lock scope
4. **Use higher-level constructs**: `ConcurrentHashMap`, `BlockingQueue`

---

## Summary: Choosing the Right Tool

| Scenario | Solution |
|----------|----------|
| Simple counter | `AtomicLong` or `LongAdder` |
| Conditional map update | `ConcurrentHashMap.compute()` |
| Read-heavy cache | `ReadWriteLock` or `StampedLock` |
| Request deduplication (single node) | `ConcurrentHashMap` + TTL cleanup |
| Request deduplication (distributed) | Redis `SETNX` with TTL |
| Rate limiting | Token Bucket / Sliding Window |
| Fault tolerance | Circuit Breaker |
| Producer-Consumer | `BlockingQueue` |

---

## References

- [Java Concurrency Guide](https://javaguide.cn/java/concurrent/)
- [Redis Distributed Locks](https://redis.io/topics/distlock)
- [Martin Fowler - Circuit Breaker](https://martinfowler.com/bliki/CircuitBreaker.html)
