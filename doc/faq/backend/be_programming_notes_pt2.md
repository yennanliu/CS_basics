# Backend Programming Notes for BE Role (Part 2)

> Focus: Thread Pools, Async Programming, Caching, Message Queues, Distributed Transactions

## Table of Contents

1. [Thread Pool & Executor Framework](#thread-pool--executor-framework)
2. [CompletableFuture & Async Programming](#completablefuture--async-programming)
3. [Caching Strategies](#caching-strategies)
4. [Message Queue Patterns](#message-queue-patterns)
5. [Distributed Transactions](#distributed-transactions)
6. [Coordination Utilities](#coordination-utilities)
7. [Graceful Shutdown](#graceful-shutdown)
8. [Backpressure Handling](#backpressure-handling)
9. [Common Interview Questions](#common-interview-questions)

---

## Thread Pool & Executor Framework

### 1. ThreadPoolExecutor Deep Dive

```java
import java.util.concurrent.*;

public class ThreadPoolDemo {

    /**
     * ThreadPoolExecutor parameters:
     * - corePoolSize: minimum threads always kept alive
     * - maximumPoolSize: maximum threads allowed
     * - keepAliveTime: idle time before non-core threads terminate
     * - workQueue: queue to hold tasks before execution
     * - threadFactory: creates new threads
     * - handler: rejection policy when queue is full
     */
    public static ThreadPoolExecutor createCustomPool() {
        return new ThreadPoolExecutor(
            4,                              // corePoolSize
            8,                              // maximumPoolSize
            60L, TimeUnit.SECONDS,          // keepAliveTime
            new LinkedBlockingQueue<>(100), // workQueue with capacity
            new CustomThreadFactory("worker"),
            new ThreadPoolExecutor.CallerRunsPolicy() // rejection policy
        );
    }

    static class CustomThreadFactory implements ThreadFactory {
        private final String namePrefix;
        private final AtomicInteger counter = new AtomicInteger(0);

        CustomThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + "-" + counter.incrementAndGet());
            t.setDaemon(false);
            t.setUncaughtExceptionHandler((thread, ex) -> {
                System.err.println("Thread " + thread.getName() + " failed: " + ex.getMessage());
            });
            return t;
        }
    }
}
```

### 2. Rejection Policies Comparison

| Policy | Behavior | Use Case |
|--------|----------|----------|
| `AbortPolicy` | Throws `RejectedExecutionException` | Fail fast, alert monitoring |
| `CallerRunsPolicy` | Caller thread executes the task | Backpressure, slow down producer |
| `DiscardPolicy` | Silently discards task | Non-critical tasks |
| `DiscardOldestPolicy` | Discards oldest queued task | Prefer newest data |

```java
// Custom rejection handler with logging and metrics
public class LoggingRejectionHandler implements RejectedExecutionHandler {

    private final AtomicLong rejectedCount = new AtomicLong(0);

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        rejectedCount.incrementAndGet();

        // Log details
        System.err.println("Task rejected. Pool size: " + executor.getPoolSize() +
                          ", Active: " + executor.getActiveCount() +
                          ", Queue size: " + executor.getQueue().size());

        // Option 1: Block and wait
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RejectedExecutionException("Interrupted while waiting", e);
        }

        // Option 2: Execute in caller thread (like CallerRunsPolicy)
        // if (!executor.isShutdown()) {
        //     r.run();
        // }
    }

    public long getRejectedCount() {
        return rejectedCount.get();
    }
}
```

### 3. Choosing Queue Type

| Queue Type | Behavior | Use Case |
|------------|----------|----------|
| `LinkedBlockingQueue` | Unbounded or bounded FIFO | General purpose |
| `ArrayBlockingQueue` | Bounded, array-backed | Memory-constrained |
| `SynchronousQueue` | No capacity, direct handoff | Maximize threads first |
| `PriorityBlockingQueue` | Priority ordering | Task prioritization |

```java
// SynchronousQueue: Creates new thread for each task (up to max)
// Good for short-lived tasks with unpredictable load
ThreadPoolExecutor cachedLikePool = new ThreadPoolExecutor(
    0, Integer.MAX_VALUE,
    60L, TimeUnit.SECONDS,
    new SynchronousQueue<>()
);

// Bounded queue: Predictable memory usage
ThreadPoolExecutor boundedPool = new ThreadPoolExecutor(
    4, 8,
    60L, TimeUnit.SECONDS,
    new ArrayBlockingQueue<>(100)
);
```

### 4. Monitor Thread Pool Health

```java
public class ThreadPoolMonitor {

    private final ScheduledExecutorService monitor = Executors.newSingleThreadScheduledExecutor();

    public void startMonitoring(ThreadPoolExecutor pool, long intervalMs) {
        monitor.scheduleAtFixedRate(() -> {
            System.out.println("=== Thread Pool Stats ===");
            System.out.println("Pool Size: " + pool.getPoolSize());
            System.out.println("Active Threads: " + pool.getActiveCount());
            System.out.println("Queued Tasks: " + pool.getQueue().size());
            System.out.println("Completed Tasks: " + pool.getCompletedTaskCount());
            System.out.println("Core Pool Size: " + pool.getCorePoolSize());
            System.out.println("Max Pool Size: " + pool.getMaximumPoolSize());
        }, 0, intervalMs, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        monitor.shutdown();
    }
}
```

---

## CompletableFuture & Async Programming

### 1. Basic CompletableFuture Patterns

```java
import java.util.concurrent.*;

public class CompletableFutureDemo {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    // Basic async execution
    public CompletableFuture<String> fetchDataAsync(String id) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate API call
            sleep(100);
            return "Data for " + id;
        }, executor);
    }

    // Chain transformations
    public CompletableFuture<Integer> processData(String id) {
        return fetchDataAsync(id)
            .thenApply(data -> data.toUpperCase())      // Transform
            .thenApply(data -> data.length());           // Transform again
    }

    // Chain async operations
    public CompletableFuture<String> fetchAndEnrich(String id) {
        return fetchDataAsync(id)
            .thenCompose(data -> enrichDataAsync(data)); // Async chain
    }

    // Combine two independent futures
    public CompletableFuture<String> fetchBothAndCombine(String id1, String id2) {
        CompletableFuture<String> future1 = fetchDataAsync(id1);
        CompletableFuture<String> future2 = fetchDataAsync(id2);

        return future1.thenCombine(future2, (data1, data2) -> {
            return data1 + " + " + data2;
        });
    }

    private CompletableFuture<String> enrichDataAsync(String data) {
        return CompletableFuture.supplyAsync(() -> "Enriched: " + data, executor);
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
```

### 2. Error Handling

```java
public class CompletableFutureErrorHandling {

    // Handle exceptions
    public CompletableFuture<String> fetchWithFallback(String id) {
        return fetchDataAsync(id)
            .exceptionally(ex -> {
                System.err.println("Error: " + ex.getMessage());
                return "Default Value";  // Fallback
            });
    }

    // Handle both success and failure
    public CompletableFuture<String> fetchWithHandle(String id) {
        return fetchDataAsync(id)
            .handle((result, ex) -> {
                if (ex != null) {
                    return "Error: " + ex.getMessage();
                }
                return "Success: " + result;
            });
    }

    // Recover with another async operation
    public CompletableFuture<String> fetchWithAsyncFallback(String id) {
        return fetchDataAsync(id)
            .exceptionallyCompose(ex -> {
                System.err.println("Primary failed, trying backup...");
                return fetchFromBackupAsync(id);
            });
    }

    // Timeout handling
    public CompletableFuture<String> fetchWithTimeout(String id) {
        return fetchDataAsync(id)
            .orTimeout(5, TimeUnit.SECONDS)
            .exceptionally(ex -> {
                if (ex instanceof TimeoutException) {
                    return "Timeout - using cached value";
                }
                throw new CompletionException(ex);
            });
    }

    private CompletableFuture<String> fetchDataAsync(String id) {
        return CompletableFuture.supplyAsync(() -> "Data: " + id);
    }

    private CompletableFuture<String> fetchFromBackupAsync(String id) {
        return CompletableFuture.supplyAsync(() -> "Backup Data: " + id);
    }
}
```

### 3. Parallel Execution Patterns

```java
public class ParallelExecutionPatterns {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    // Wait for all futures to complete
    public CompletableFuture<List<String>> fetchAll(List<String> ids) {
        List<CompletableFuture<String>> futures = ids.stream()
            .map(this::fetchDataAsync)
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    // Return first completed result
    public CompletableFuture<String> fetchFirst(List<String> ids) {
        List<CompletableFuture<String>> futures = ids.stream()
            .map(this::fetchDataAsync)
            .collect(Collectors.toList());

        return CompletableFuture.anyOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(result -> (String) result);
    }

    // Collect results as they complete (with timeout per item)
    public List<String> fetchAllWithIndividualTimeout(List<String> ids, long timeoutMs) {
        List<CompletableFuture<String>> futures = ids.stream()
            .map(id -> fetchDataAsync(id)
                .orTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .exceptionally(ex -> "Failed: " + id))
            .collect(Collectors.toList());

        return futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());
    }

    // Limit concurrency with Semaphore
    public CompletableFuture<List<String>> fetchWithConcurrencyLimit(
            List<String> ids, int maxConcurrent) {

        Semaphore semaphore = new Semaphore(maxConcurrent);

        List<CompletableFuture<String>> futures = ids.stream()
            .map(id -> CompletableFuture.supplyAsync(() -> {
                try {
                    semaphore.acquire();
                    try {
                        return fetchDataSync(id);
                    } finally {
                        semaphore.release();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new CompletionException(e);
                }
            }, executor))
            .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }

    private CompletableFuture<String> fetchDataAsync(String id) {
        return CompletableFuture.supplyAsync(() -> fetchDataSync(id), executor);
    }

    private String fetchDataSync(String id) {
        // Simulate work
        return "Data: " + id;
    }
}
```

---

## Caching Strategies

### 1. Cache-Aside Pattern (Lazy Loading)

```java
public class CacheAsidePattern {

    private final Cache<String, User> cache;
    private final UserRepository repository;

    public CacheAsidePattern(Cache<String, User> cache, UserRepository repository) {
        this.cache = cache;
        this.repository = repository;
    }

    public User getUser(String userId) {
        // 1. Check cache first
        User user = cache.get(userId);

        if (user != null) {
            return user;  // Cache hit
        }

        // 2. Cache miss - load from database
        user = repository.findById(userId);

        if (user != null) {
            // 3. Store in cache
            cache.put(userId, user);
        }

        return user;
    }

    public void updateUser(User user) {
        // 1. Update database first
        repository.save(user);

        // 2. Invalidate cache (or update)
        cache.invalidate(user.getId());

        // Alternative: Update cache
        // cache.put(user.getId(), user);
    }
}
```

### 2. Write-Through Pattern

```java
public class WriteThroughPattern {

    private final Cache<String, User> cache;
    private final UserRepository repository;

    public User getUser(String userId) {
        // Always read from cache (cache is source of truth for reads)
        User user = cache.get(userId);

        if (user == null) {
            user = repository.findById(userId);
            if (user != null) {
                cache.put(userId, user);
            }
        }

        return user;
    }

    public void saveUser(User user) {
        // Write to both cache and database synchronously
        repository.save(user);
        cache.put(user.getId(), user);
    }
}
```

### 3. Write-Behind (Write-Back) Pattern

```java
public class WriteBehindPattern {

    private final Cache<String, User> cache;
    private final UserRepository repository;
    private final BlockingQueue<User> writeQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService writer = Executors.newSingleThreadScheduledExecutor();

    public WriteBehindPattern(Cache<String, User> cache, UserRepository repository) {
        this.cache = cache;
        this.repository = repository;

        // Start background writer
        writer.scheduleWithFixedDelay(this::flushWrites, 100, 100, TimeUnit.MILLISECONDS);
    }

    public void saveUser(User user) {
        // 1. Write to cache immediately
        cache.put(user.getId(), user);

        // 2. Queue for async database write
        writeQueue.offer(user);
    }

    private void flushWrites() {
        List<User> batch = new ArrayList<>();
        writeQueue.drainTo(batch, 100);  // Batch up to 100 items

        if (!batch.isEmpty()) {
            try {
                repository.saveAll(batch);  // Batch write to DB
            } catch (Exception e) {
                // Re-queue failed items
                writeQueue.addAll(batch);
            }
        }
    }

    public void shutdown() {
        writer.shutdown();
        flushWrites();  // Final flush
    }
}
```

### 4. Cache with Distributed Lock (Prevent Stampede)

```java
public class CacheWithLock {

    private final Cache<String, User> cache;
    private final UserRepository repository;
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public User getUser(String userId) {
        // 1. Check cache
        User user = cache.get(userId);
        if (user != null) {
            return user;
        }

        // 2. Get lock for this specific key
        ReentrantLock lock = locks.computeIfAbsent(userId, k -> new ReentrantLock());

        lock.lock();
        try {
            // 3. Double-check cache after acquiring lock
            user = cache.get(userId);
            if (user != null) {
                return user;
            }

            // 4. Load from database
            user = repository.findById(userId);
            if (user != null) {
                cache.put(userId, user);
            }

            return user;
        } finally {
            lock.unlock();
        }
    }
}
```

### 5. Refresh-Ahead Pattern

```java
public class RefreshAheadCache {

    private final Cache<String, CacheEntry<User>> cache;
    private final UserRepository repository;
    private final ExecutorService refreshExecutor = Executors.newFixedThreadPool(2);

    private static final long TTL_MS = 60_000;           // 60 seconds TTL
    private static final long REFRESH_THRESHOLD = 45_000; // Refresh at 75% of TTL

    static class CacheEntry<T> {
        final T value;
        final long createdAt;
        volatile boolean refreshing = false;

        CacheEntry(T value) {
            this.value = value;
            this.createdAt = System.currentTimeMillis();
        }

        boolean shouldRefresh() {
            return System.currentTimeMillis() - createdAt > REFRESH_THRESHOLD;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - createdAt > TTL_MS;
        }
    }

    public User getUser(String userId) {
        CacheEntry<User> entry = cache.get(userId);

        if (entry == null || entry.isExpired()) {
            // Cache miss or expired - synchronous load
            return loadAndCache(userId);
        }

        // Trigger async refresh if approaching expiry
        if (entry.shouldRefresh() && !entry.refreshing) {
            entry.refreshing = true;
            refreshExecutor.submit(() -> {
                try {
                    loadAndCache(userId);
                } catch (Exception e) {
                    entry.refreshing = false;
                }
            });
        }

        return entry.value;
    }

    private User loadAndCache(String userId) {
        User user = repository.findById(userId);
        if (user != null) {
            cache.put(userId, new CacheEntry<>(user));
        }
        return user;
    }
}
```

---

## Message Queue Patterns

### 1. At-Least-Once Delivery with Idempotency

```java
public class AtLeastOnceConsumer {

    private final MessageQueue queue;
    private final IdempotencyStore idempotencyStore;
    private final OrderService orderService;

    public void processMessages() {
        while (true) {
            Message message = queue.receive();

            try {
                // Check if already processed
                if (idempotencyStore.isProcessed(message.getId())) {
                    queue.acknowledge(message);
                    continue;
                }

                // Process the message
                orderService.processOrder(message.getPayload());

                // Mark as processed BEFORE acknowledging
                idempotencyStore.markProcessed(message.getId());

                // Acknowledge to queue
                queue.acknowledge(message);

            } catch (Exception e) {
                // Don't acknowledge - message will be redelivered
                queue.nack(message);
            }
        }
    }
}
```

### 2. Transactional Outbox Pattern

```java
/**
 * Ensures message is sent if and only if database transaction commits.
 * Solves dual-write problem.
 */
public class TransactionalOutbox {

    private final DataSource dataSource;
    private final ScheduledExecutorService publisher = Executors.newSingleThreadScheduledExecutor();

    public TransactionalOutbox() {
        // Poll and publish outbox messages
        publisher.scheduleWithFixedDelay(this::publishPendingMessages, 100, 100, TimeUnit.MILLISECONDS);
    }

    @Transactional
    public void createOrder(Order order) {
        // 1. Save order to database
        saveOrder(order);

        // 2. Save event to outbox table (same transaction)
        OutboxEvent event = new OutboxEvent(
            UUID.randomUUID().toString(),
            "order.created",
            toJson(order),
            "PENDING"
        );
        saveOutboxEvent(event);

        // Transaction commits both or neither
    }

    private void publishPendingMessages() {
        List<OutboxEvent> pending = fetchPendingEvents(100);

        for (OutboxEvent event : pending) {
            try {
                // Publish to message queue
                messageQueue.publish(event.getTopic(), event.getPayload());

                // Mark as published
                markEventPublished(event.getId());

            } catch (Exception e) {
                // Will retry on next poll
                markEventFailed(event.getId());
            }
        }
    }

    // Database operations
    private void saveOrder(Order order) { /* ... */ }
    private void saveOutboxEvent(OutboxEvent event) { /* ... */ }
    private List<OutboxEvent> fetchPendingEvents(int limit) { /* ... */ return List.of(); }
    private void markEventPublished(String id) { /* ... */ }
    private void markEventFailed(String id) { /* ... */ }
}
```

### 3. Dead Letter Queue Handler

```java
public class DeadLetterQueueHandler {

    private final MessageQueue mainQueue;
    private final MessageQueue dlq;
    private final int maxRetries = 3;

    public void processWithDLQ() {
        while (true) {
            Message message = mainQueue.receive();

            try {
                processMessage(message);
                mainQueue.acknowledge(message);

            } catch (RetryableException e) {
                int retryCount = message.getRetryCount();

                if (retryCount < maxRetries) {
                    // Requeue with incremented retry count
                    message.setRetryCount(retryCount + 1);
                    message.setNextRetryTime(calculateBackoff(retryCount));
                    mainQueue.requeue(message);
                } else {
                    // Move to DLQ
                    dlq.publish(message);
                    mainQueue.acknowledge(message);
                }

            } catch (NonRetryableException e) {
                // Immediately move to DLQ
                dlq.publish(message);
                mainQueue.acknowledge(message);
            }
        }
    }

    private long calculateBackoff(int retryCount) {
        // Exponential backoff: 1s, 2s, 4s, 8s...
        return System.currentTimeMillis() + (1000L * (1 << retryCount));
    }

    private void processMessage(Message message) throws RetryableException, NonRetryableException {
        // Business logic
    }
}
```

### 4. Consumer Group with Partition Assignment

```java
public class PartitionedConsumer {

    private final int consumerId;
    private final int totalConsumers;
    private final MessageQueue queue;

    public PartitionedConsumer(int consumerId, int totalConsumers) {
        this.consumerId = consumerId;
        this.totalConsumers = totalConsumers;
    }

    public void consume() {
        while (true) {
            Message message = queue.receive();

            // Check if this message belongs to this consumer
            int partition = getPartition(message.getKey());

            if (partition == consumerId) {
                processMessage(message);
                queue.acknowledge(message);
            } else {
                // Requeue for correct consumer (or skip if using proper partitioning)
                queue.requeue(message);
            }
        }
    }

    private int getPartition(String key) {
        // Consistent hash to determine partition
        return Math.abs(key.hashCode() % totalConsumers);
    }

    private void processMessage(Message message) {
        // Process message
    }
}
```

---

## Distributed Transactions

### 1. Saga Pattern (Choreography)

```java
/**
 * Each service publishes events, others react.
 * Compensating transactions for rollback.
 */
public class OrderSagaChoreography {

    // Order Service
    public void createOrder(Order order) {
        saveOrder(order, "PENDING");
        publishEvent(new OrderCreatedEvent(order));
    }

    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        updateOrderStatus(event.getOrderId(), "PAYMENT_COMPLETED");
        publishEvent(new OrderPaidEvent(event.getOrderId()));
    }

    public void handlePaymentFailed(PaymentFailedEvent event) {
        // Compensating transaction
        updateOrderStatus(event.getOrderId(), "CANCELLED");
    }

    // Payment Service
    public void handleOrderCreated(OrderCreatedEvent event) {
        try {
            processPayment(event.getOrder());
            publishEvent(new PaymentCompletedEvent(event.getOrderId()));
        } catch (Exception e) {
            publishEvent(new PaymentFailedEvent(event.getOrderId(), e.getMessage()));
        }
    }

    // Inventory Service
    public void handleOrderPaid(OrderPaidEvent event) {
        try {
            reserveInventory(event.getOrderId());
            publishEvent(new InventoryReservedEvent(event.getOrderId()));
        } catch (Exception e) {
            // Compensate: refund payment
            publishEvent(new InventoryFailedEvent(event.getOrderId()));
        }
    }
}
```

### 2. Saga Pattern (Orchestrator)

```java
/**
 * Central orchestrator controls the saga flow.
 * Easier to understand and debug.
 */
public class OrderSagaOrchestrator {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;
    private final ShippingService shippingService;

    public OrderResult executeOrderSaga(OrderRequest request) {
        String sagaId = UUID.randomUUID().toString();
        SagaState state = new SagaState(sagaId);

        try {
            // Step 1: Create order
            Order order = orderService.createOrder(request);
            state.setOrderId(order.getId());
            state.addCompensation(() -> orderService.cancelOrder(order.getId()));

            // Step 2: Process payment
            PaymentResult payment = paymentService.processPayment(order);
            state.setPaymentId(payment.getId());
            state.addCompensation(() -> paymentService.refund(payment.getId()));

            // Step 3: Reserve inventory
            InventoryReservation reservation = inventoryService.reserve(order);
            state.setReservationId(reservation.getId());
            state.addCompensation(() -> inventoryService.release(reservation.getId()));

            // Step 4: Schedule shipping
            Shipment shipment = shippingService.schedule(order);
            state.setShipmentId(shipment.getId());

            // All steps succeeded
            orderService.confirmOrder(order.getId());
            return OrderResult.success(order.getId());

        } catch (Exception e) {
            // Execute compensations in reverse order
            state.compensate();
            return OrderResult.failure(e.getMessage());
        }
    }

    static class SagaState {
        private final String sagaId;
        private final Deque<Runnable> compensations = new ArrayDeque<>();
        private String orderId;
        private String paymentId;
        private String reservationId;
        private String shipmentId;

        SagaState(String sagaId) {
            this.sagaId = sagaId;
        }

        void addCompensation(Runnable compensation) {
            compensations.push(compensation);
        }

        void compensate() {
            while (!compensations.isEmpty()) {
                try {
                    compensations.pop().run();
                } catch (Exception e) {
                    // Log and continue with other compensations
                    System.err.println("Compensation failed: " + e.getMessage());
                }
            }
        }

        // Setters omitted for brevity
        void setOrderId(String id) { this.orderId = id; }
        void setPaymentId(String id) { this.paymentId = id; }
        void setReservationId(String id) { this.reservationId = id; }
        void setShipmentId(String id) { this.shipmentId = id; }
    }
}
```

### 3. Two-Phase Commit (2PC) Simulation

```java
/**
 * Coordinator-based 2PC implementation.
 * Note: Rarely used in practice due to blocking nature.
 */
public class TwoPhaseCommitCoordinator {

    private final List<Participant> participants;

    public boolean executeTransaction(TransactionContext context) {
        String txId = UUID.randomUUID().toString();

        // Phase 1: Prepare
        boolean allPrepared = true;
        for (Participant participant : participants) {
            try {
                boolean prepared = participant.prepare(txId, context);
                if (!prepared) {
                    allPrepared = false;
                    break;
                }
            } catch (Exception e) {
                allPrepared = false;
                break;
            }
        }

        // Phase 2: Commit or Rollback
        if (allPrepared) {
            for (Participant participant : participants) {
                participant.commit(txId);
            }
            return true;
        } else {
            for (Participant participant : participants) {
                participant.rollback(txId);
            }
            return false;
        }
    }

    interface Participant {
        boolean prepare(String txId, TransactionContext context);
        void commit(String txId);
        void rollback(String txId);
    }
}
```

---

## Coordination Utilities

### 1. CountDownLatch - Wait for N Events

```java
public class CountDownLatchDemo {

    /**
     * Wait for multiple services to initialize before starting main application.
     */
    public void initializeServices() throws InterruptedException {
        int serviceCount = 3;
        CountDownLatch latch = new CountDownLatch(serviceCount);

        // Start services in parallel
        new Thread(() -> {
            initDatabase();
            latch.countDown();
        }).start();

        new Thread(() -> {
            initCache();
            latch.countDown();
        }).start();

        new Thread(() -> {
            initMessageQueue();
            latch.countDown();
        }).start();

        // Wait for all services
        boolean completed = latch.await(30, TimeUnit.SECONDS);

        if (completed) {
            System.out.println("All services initialized!");
        } else {
            throw new RuntimeException("Service initialization timeout");
        }
    }

    private void initDatabase() { sleep(1000); }
    private void initCache() { sleep(500); }
    private void initMessageQueue() { sleep(800); }
    private void sleep(long ms) { try { Thread.sleep(ms); } catch (InterruptedException e) {} }
}
```

### 2. CyclicBarrier - Synchronization Point

```java
public class CyclicBarrierDemo {

    /**
     * All threads wait at barrier, then proceed together.
     * Useful for parallel algorithms with phases.
     */
    public void parallelMatrixComputation() {
        int threadCount = 4;
        CyclicBarrier barrier = new CyclicBarrier(threadCount, () -> {
            System.out.println("All threads completed phase, merging results...");
        });

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    // Phase 1: Compute partial result
                    System.out.println("Thread " + threadId + " computing...");
                    Thread.sleep(100 * threadId);

                    // Wait for all threads
                    barrier.await();

                    // Phase 2: All threads proceed together
                    System.out.println("Thread " + threadId + " proceeding to phase 2");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

### 3. Semaphore - Limit Concurrent Access

```java
public class SemaphoreDemo {

    /**
     * Connection pool with limited connections.
     */
    private final Semaphore connectionSemaphore;
    private final Queue<Connection> connectionPool;

    public SemaphoreDemo(int maxConnections) {
        this.connectionSemaphore = new Semaphore(maxConnections);
        this.connectionPool = new ConcurrentLinkedQueue<>();

        // Pre-create connections
        for (int i = 0; i < maxConnections; i++) {
            connectionPool.offer(createConnection());
        }
    }

    public Connection acquireConnection(long timeout, TimeUnit unit)
            throws InterruptedException, TimeoutException {

        if (!connectionSemaphore.tryAcquire(timeout, unit)) {
            throw new TimeoutException("Could not acquire connection");
        }

        Connection conn = connectionPool.poll();
        if (conn == null || !conn.isValid()) {
            conn = createConnection();
        }
        return conn;
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.offer(conn);
            connectionSemaphore.release();
        }
    }

    private Connection createConnection() {
        return new Connection();
    }
}
```

### 4. Phaser - Flexible Barrier

```java
public class PhaserDemo {

    /**
     * Dynamic number of parties, multiple phases.
     */
    public void dynamicParallelProcessing() {
        Phaser phaser = new Phaser(1); // Register self

        for (int i = 0; i < 3; i++) {
            final int taskId = i;
            phaser.register(); // Register new party

            new Thread(() -> {
                for (int phase = 0; phase < 3; phase++) {
                    System.out.println("Task " + taskId + " executing phase " + phase);

                    // Wait for others to complete this phase
                    phaser.arriveAndAwaitAdvance();
                }

                // Deregister when done
                phaser.arriveAndDeregister();
            }).start();
        }

        // Wait for all tasks to complete
        while (!phaser.isTerminated()) {
            phaser.arriveAndAwaitAdvance();
        }
    }
}
```

---

## Graceful Shutdown

### 1. ExecutorService Graceful Shutdown

```java
public class GracefulShutdownDemo {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);
    private volatile boolean shuttingDown = false;

    public void shutdown() {
        shuttingDown = true;

        // Stop accepting new tasks
        executor.shutdown();

        try {
            // Wait for existing tasks to complete
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Forcing shutdown...");

                // Force shutdown
                List<Runnable> pendingTasks = executor.shutdownNow();
                System.err.println("Pending tasks: " + pendingTasks.size());

                // Wait again
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public void submitTask(Runnable task) {
        if (shuttingDown) {
            throw new RejectedExecutionException("Service is shutting down");
        }
        executor.submit(task);
    }
}
```

### 2. Spring Boot Graceful Shutdown

```java
@Component
public class GracefulShutdownHandler implements ApplicationListener<ContextClosedEvent> {

    private final ExecutorService executor;
    private final AtomicInteger activeRequests = new AtomicInteger(0);

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        System.out.println("Shutdown initiated, waiting for active requests...");

        // Wait for active requests to complete
        int maxWaitSeconds = 30;
        int waited = 0;

        while (activeRequests.get() > 0 && waited < maxWaitSeconds) {
            try {
                Thread.sleep(1000);
                waited++;
                System.out.println("Active requests: " + activeRequests.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Shutdown executor
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Shutdown complete");
    }

    public void trackRequest() {
        activeRequests.incrementAndGet();
    }

    public void completeRequest() {
        activeRequests.decrementAndGet();
    }
}
```

---

## Backpressure Handling

### 1. Blocking with Bounded Queue

```java
public class BackpressureWithBoundedQueue {

    // Bounded queue - producer blocks when full
    private final BlockingQueue<Task> queue = new ArrayBlockingQueue<>(100);

    public void produce(Task task) throws InterruptedException {
        // Blocks if queue is full - natural backpressure
        queue.put(task);
    }

    public void produceWithTimeout(Task task, long timeout, TimeUnit unit)
            throws InterruptedException {

        if (!queue.offer(task, timeout, unit)) {
            throw new RejectedExecutionException("Queue full, try again later");
        }
    }

    public void consume() throws InterruptedException {
        Task task = queue.take(); // Blocks if empty
        process(task);
    }

    private void process(Task task) { }
}
```

### 2. Rate-Based Backpressure

```java
public class RateBasedBackpressure {

    private final RateLimiter rateLimiter;
    private final BlockingQueue<Task> queue;
    private final ExecutorService executor;

    public RateBasedBackpressure(int maxRatePerSecond, int queueCapacity) {
        this.rateLimiter = new TokenBucketRateLimiter(maxRatePerSecond, maxRatePerSecond);
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.executor = Executors.newFixedThreadPool(4);

        startConsumer();
    }

    public boolean submit(Task task) {
        // Check rate limit
        if (!rateLimiter.tryAcquire()) {
            return false; // Rate limited
        }

        // Check queue capacity
        return queue.offer(task);
    }

    private void startConsumer() {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Task task = queue.take();
                    process(task);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private void process(Task task) { }
}
```

### 3. Reactive Backpressure (Conceptual)

```java
/**
 * Reactive Streams backpressure with request-based flow control.
 */
public class ReactiveBackpressureExample {

    // Subscriber requests N items at a time
    public void demonstrateBackpressure() {
        // Conceptual - using Project Reactor/RxJava syntax

        /*
        Flux.range(1, 1000)
            .onBackpressureBuffer(100)      // Buffer up to 100
            .onBackpressureDrop(i -> log("Dropped: " + i))  // Drop if buffer full
            .publishOn(Schedulers.parallel())
            .subscribe(new BaseSubscriber<Integer>() {
                @Override
                protected void hookOnSubscribe(Subscription s) {
                    request(10); // Request initial 10 items
                }

                @Override
                protected void hookOnNext(Integer value) {
                    process(value);
                    request(1); // Request next item after processing
                }
            });
        */
    }
}
```

---

## Common Interview Questions

### Q1: How would you handle a thundering herd problem?

**Answer:**
1. **Cache stampede prevention**: Use distributed lock or `computeIfAbsent`
2. **Staggered cache expiration**: Add jitter to TTL
3. **Request coalescing**: Multiple identical requests share one backend call
4. **Circuit breaker**: Prevent cascading failures

```java
// Request coalescing example
ConcurrentHashMap<String, CompletableFuture<Data>> inFlightRequests = new ConcurrentHashMap<>();

public CompletableFuture<Data> getData(String key) {
    return inFlightRequests.computeIfAbsent(key, k -> {
        return fetchFromBackend(k)
            .whenComplete((result, ex) -> inFlightRequests.remove(k));
    });
}
```

### Q2: Difference between Saga Choreography vs Orchestration?

| Aspect | Choreography | Orchestration |
|--------|--------------|---------------|
| Coordination | Decentralized (events) | Centralized (orchestrator) |
| Coupling | Loose | Tighter to orchestrator |
| Complexity | Complex event flows | Simpler flow control |
| Debugging | Harder (distributed) | Easier (single point) |
| Single point of failure | No | Yes (orchestrator) |

### Q3: How to ensure exactly-once processing?

**Answer:** True exactly-once is hard. Use idempotency:
1. **Idempotent operations**: Design operations to be safely repeatable
2. **Idempotency key**: Track processed request IDs
3. **Transactional outbox**: Atomically update DB and outbox

```java
// At-least-once delivery + idempotent processing = effectively exactly-once
public void processMessage(Message msg) {
    if (isAlreadyProcessed(msg.getId())) {
        return; // Skip duplicate
    }

    // Process idempotently (use upsert, conditional updates)
    processIdempotently(msg);

    markAsProcessed(msg.getId());
}
```

### Q4: When to use CountDownLatch vs CyclicBarrier?

| Feature | CountDownLatch | CyclicBarrier |
|---------|----------------|---------------|
| Reusable | No (single use) | Yes (can reset) |
| Use case | Wait for N events | Sync N threads repeatedly |
| Action | Count down to zero | Wait at barrier |
| Example | Wait for services to start | Parallel algorithm phases |

### Q5: How to prevent memory leaks in thread pools?

1. **ThreadLocal cleanup**: Always remove ThreadLocal values
2. **Bounded queues**: Use bounded work queues
3. **Proper shutdown**: Call `shutdown()` and `awaitTermination()`
4. **Reject policy**: Handle rejected tasks properly
5. **Monitor queue size**: Alert on growing queues

```java
// ThreadLocal cleanup
public void processInPool(Task task) {
    executor.submit(() -> {
        try {
            threadLocalContext.set(createContext());
            process(task);
        } finally {
            threadLocalContext.remove(); // IMPORTANT!
        }
    });
}
```

---

## Summary: Pattern Selection Guide

| Problem | Pattern |
|---------|---------|
| Parallel independent tasks | `CompletableFuture.allOf()` |
| First result wins | `CompletableFuture.anyOf()` |
| Limit concurrency | `Semaphore` |
| Wait for events | `CountDownLatch` |
| Sync at phases | `CyclicBarrier` |
| Dynamic coordination | `Phaser` |
| Distributed transactions | Saga (Orchestrator) |
| Event-driven transactions | Saga (Choreography) |
| Cache miss handling | Cache-Aside + Lock |
| Write performance | Write-Behind |
| Message reliability | At-least-once + Idempotency |
| Failed message handling | Dead Letter Queue |

---

## References

- [Java Concurrency in Practice](https://jcip.net/)
- [Martin Fowler - CQRS](https://martinfowler.com/bliki/CQRS.html)
- [Microservices Patterns - Chris Richardson](https://microservices.io/patterns/)
- [AWS: Saga Pattern](https://docs.aws.amazon.com/prescriptive-guidance/latest/modernization-data-persistence/saga-pattern.html)
