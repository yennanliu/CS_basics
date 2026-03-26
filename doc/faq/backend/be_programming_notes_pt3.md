# Backend Programming Notes for BE Role (Part 3)

> Focus: Advanced Concurrency, Locking Mechanisms, High Traffic Patterns

## Table of Contents

1. [Advanced Concurrency](#advanced-concurrency)
   - [Fork/Join Framework](#1-forkjoin-framework)
   - [Lock-Free Programming](#2-lock-free-programming)
   - [CAS and ABA Problem](#3-cas-and-aba-problem)
   - [Memory Barriers & Happens-Before](#4-memory-barriers--happens-before)
2. [Advanced Locking](#advanced-locking)
   - [Lock Striping](#1-lock-striping)
   - [Distributed Lock Algorithms](#2-distributed-lock-algorithms)
   - [Database Locking](#3-database-locking)
   - [Spin Lock vs Mutex](#4-spin-lock-vs-mutex)
3. [High Traffic Patterns](#high-traffic-patterns)
   - [Request Coalescing](#1-request-coalescing)
   - [Load Shedding](#2-load-shedding)
   - [Bulkhead Pattern](#3-bulkhead-pattern)
   - [Hot Key Problem](#4-hot-key-problem)
   - [Connection Pool Tuning](#5-connection-pool-tuning)
   - [Consistent Hashing](#6-consistent-hashing)
4. [Real-World Scenarios](#real-world-scenarios)
5. [Common Interview Questions](#common-interview-questions)

---

## Advanced Concurrency

### 1. Fork/Join Framework

```java
import java.util.concurrent.*;

/**
 * Fork/Join: Divide-and-conquer parallelism with work stealing.
 * Ideal for recursive algorithms that can be split into subtasks.
 */
public class ForkJoinDemo {

    private static final ForkJoinPool pool = ForkJoinPool.commonPool();

    // Example: Parallel sum of array
    static class SumTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000;
        private final long[] array;
        private final int start, end;

        SumTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            int length = end - start;

            // Base case: compute directly
            if (length <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            // Recursive case: split and fork
            int mid = start + length / 2;
            SumTask leftTask = new SumTask(array, start, mid);
            SumTask rightTask = new SumTask(array, mid, end);

            // Fork left task (async)
            leftTask.fork();

            // Compute right task in current thread
            long rightResult = rightTask.compute();

            // Join left task result
            long leftResult = leftTask.join();

            return leftResult + rightResult;
        }
    }

    // Example: Parallel merge sort
    static class MergeSortTask extends RecursiveAction {
        private static final int THRESHOLD = 1000;
        private final int[] array;
        private final int start, end;
        private final int[] temp;

        MergeSortTask(int[] array, int start, int end, int[] temp) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.temp = temp;
        }

        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                // Use sequential sort for small arrays
                java.util.Arrays.sort(array, start, end);
                return;
            }

            int mid = start + (end - start) / 2;

            // Fork both subtasks
            MergeSortTask left = new MergeSortTask(array, start, mid, temp);
            MergeSortTask right = new MergeSortTask(array, mid, end, temp);

            invokeAll(left, right);

            // Merge results
            merge(array, start, mid, end, temp);
        }

        private void merge(int[] arr, int start, int mid, int end, int[] temp) {
            System.arraycopy(arr, start, temp, start, end - start);

            int i = start, j = mid, k = start;
            while (i < mid && j < end) {
                arr[k++] = temp[i] <= temp[j] ? temp[i++] : temp[j++];
            }
            while (i < mid) arr[k++] = temp[i++];
        }
    }

    public static void main(String[] args) {
        long[] array = new long[1_000_000];
        for (int i = 0; i < array.length; i++) array[i] = i;

        long sum = pool.invoke(new SumTask(array, 0, array.length));
        System.out.println("Sum: " + sum);
    }
}
```

### 2. Lock-Free Programming

```java
import java.util.concurrent.atomic.*;

/**
 * Lock-free stack using CAS (Compare-And-Swap).
 * No locks, but uses atomic operations for thread safety.
 */
public class LockFreeStack<T> {

    private final AtomicReference<Node<T>> top = new AtomicReference<>();

    private static class Node<T> {
        final T value;
        final Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    public void push(T value) {
        Node<T> newNode = new Node<>(value, null);

        while (true) {
            Node<T> currentTop = top.get();
            newNode = new Node<>(value, currentTop);

            // CAS: if top is still currentTop, set it to newNode
            if (top.compareAndSet(currentTop, newNode)) {
                return; // Success
            }
            // Else: another thread modified top, retry
        }
    }

    public T pop() {
        while (true) {
            Node<T> currentTop = top.get();

            if (currentTop == null) {
                return null; // Stack is empty
            }

            Node<T> newTop = currentTop.next;

            // CAS: if top is still currentTop, set it to newTop
            if (top.compareAndSet(currentTop, newTop)) {
                return currentTop.value; // Success
            }
            // Else: retry
        }
    }

    public T peek() {
        Node<T> currentTop = top.get();
        return currentTop != null ? currentTop.value : null;
    }

    public boolean isEmpty() {
        return top.get() == null;
    }
}

/**
 * Lock-free counter using CAS with backoff.
 */
public class LockFreeCounter {

    private final AtomicLong value = new AtomicLong(0);

    public long incrementAndGet() {
        while (true) {
            long current = value.get();
            long next = current + 1;

            if (value.compareAndSet(current, next)) {
                return next;
            }
            // Backoff on contention
            Thread.onSpinWait();
        }
    }

    // Better: use built-in atomic increment
    public long incrementAndGetSimple() {
        return value.incrementAndGet();
    }

    public long get() {
        return value.get();
    }
}
```

### 3. CAS and ABA Problem

```java
import java.util.concurrent.atomic.*;

/**
 * ABA Problem: Value changes A -> B -> A, CAS succeeds but state changed.
 * Solution: Use AtomicStampedReference (version number).
 */
public class ABADemo {

    // PROBLEM: ABA issue with AtomicReference
    static class BrokenStack<T> {
        private final AtomicReference<Node<T>> top = new AtomicReference<>();

        static class Node<T> {
            T value;
            Node<T> next;
            Node(T v, Node<T> n) { value = v; next = n; }
        }

        /*
         * ABA Scenario:
         * 1. Thread A reads top = [A -> B -> C]
         * 2. Thread A is suspended
         * 3. Thread B pops A, pops B, pushes D, pushes A
         * 4. top = [A -> D -> C]  (A is reused but different state!)
         * 5. Thread A resumes, CAS(A, B) succeeds (A == A)
         * 6. But now top = [B] with B pointing to freed memory or wrong node
         */
    }

    // SOLUTION: AtomicStampedReference with version
    static class SafeStack<T> {
        private final AtomicStampedReference<Node<T>> top =
            new AtomicStampedReference<>(null, 0);

        static class Node<T> {
            T value;
            Node<T> next;
            Node(T v, Node<T> n) { value = v; next = n; }
        }

        public void push(T value) {
            int[] stampHolder = new int[1];

            while (true) {
                Node<T> currentTop = top.get(stampHolder);
                int currentStamp = stampHolder[0];

                Node<T> newNode = new Node<>(value, currentTop);

                // CAS with stamp - detects ABA
                if (top.compareAndSet(currentTop, newNode, currentStamp, currentStamp + 1)) {
                    return;
                }
            }
        }

        public T pop() {
            int[] stampHolder = new int[1];

            while (true) {
                Node<T> currentTop = top.get(stampHolder);
                int currentStamp = stampHolder[0];

                if (currentTop == null) {
                    return null;
                }

                Node<T> newTop = currentTop.next;

                // CAS with stamp - detects ABA
                if (top.compareAndSet(currentTop, newTop, currentStamp, currentStamp + 1)) {
                    return currentTop.value;
                }
            }
        }
    }

    // Alternative: AtomicMarkableReference for simpler mark/unmark
    static class MarkableDemo {
        private final AtomicMarkableReference<String> ref =
            new AtomicMarkableReference<>("initial", false);

        public void markAsDeleted() {
            while (true) {
                String current = ref.getReference();
                if (ref.compareAndSet(current, current, false, true)) {
                    return;
                }
            }
        }

        public boolean isMarked() {
            return ref.isMarked();
        }
    }
}
```

### 4. Memory Barriers & Happens-Before

```java
/**
 * Java Memory Model (JMM) guarantees with happens-before relationships.
 */
public class MemoryBarrierDemo {

    // WRONG: No visibility guarantee
    static class BrokenVisibility {
        private boolean flag = false;
        private int value = 0;

        public void writer() {
            value = 42;
            flag = true;  // Might be reordered before value = 42
        }

        public void reader() {
            if (flag) {
                System.out.println(value);  // Might print 0!
            }
        }
    }

    // CORRECT: volatile establishes happens-before
    static class VolatileVisibility {
        private volatile boolean flag = false;
        private int value = 0;

        public void writer() {
            value = 42;
            flag = true;  // volatile write - memory barrier
        }

        public void reader() {
            if (flag) {  // volatile read - memory barrier
                System.out.println(value);  // Guaranteed to print 42
            }
        }
    }

    // CORRECT: synchronized establishes happens-before
    static class SynchronizedVisibility {
        private boolean flag = false;
        private int value = 0;
        private final Object lock = new Object();

        public void writer() {
            synchronized (lock) {
                value = 42;
                flag = true;
            }  // Release lock - memory barrier
        }

        public void reader() {
            synchronized (lock) {  // Acquire lock - memory barrier
                if (flag) {
                    System.out.println(value);  // Guaranteed to print 42
                }
            }
        }
    }

    /**
     * Happens-Before Rules:
     * 1. Program order: Each action in a thread happens-before every subsequent action
     * 2. Monitor lock: unlock() happens-before subsequent lock()
     * 3. Volatile: write happens-before subsequent read
     * 4. Thread start: Thread.start() happens-before any action in started thread
     * 5. Thread join: All actions in thread happen-before join() returns
     * 6. Transitivity: If A happens-before B and B happens-before C, then A happens-before C
     */
}
```

---

## Advanced Locking

### 1. Lock Striping

```java
import java.util.concurrent.locks.*;

/**
 * Lock striping: Divide data into segments, each with its own lock.
 * Reduces contention compared to single global lock.
 */
public class StripedMap<K, V> {

    private static final int DEFAULT_STRIPES = 16;

    private final int numStripes;
    private final Node<K, V>[] buckets;
    private final ReentrantLock[] locks;

    @SuppressWarnings("unchecked")
    public StripedMap(int numBuckets, int numStripes) {
        this.numStripes = numStripes;
        this.buckets = new Node[numBuckets];
        this.locks = new ReentrantLock[numStripes];

        for (int i = 0; i < numStripes; i++) {
            locks[i] = new ReentrantLock();
        }
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    private int getStripeIndex(K key) {
        return Math.abs(key.hashCode() % numStripes);
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        int stripeIndex = getStripeIndex(key);

        locks[stripeIndex].lock();
        try {
            Node<K, V> node = buckets[bucketIndex];
            while (node != null) {
                if (node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }
            return null;
        } finally {
            locks[stripeIndex].unlock();
        }
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        int stripeIndex = getStripeIndex(key);

        locks[stripeIndex].lock();
        try {
            Node<K, V> node = buckets[bucketIndex];

            // Check if key exists
            while (node != null) {
                if (node.key.equals(key)) {
                    node.value = value;
                    return;
                }
                node = node.next;
            }

            // Add new node
            buckets[bucketIndex] = new Node<>(key, value, buckets[bucketIndex]);
        } finally {
            locks[stripeIndex].unlock();
        }
    }

    // For operations requiring all locks (resize, clear, etc.)
    public void clear() {
        for (int i = 0; i < numStripes; i++) {
            locks[i].lock();
        }
        try {
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = null;
            }
        } finally {
            for (int i = numStripes - 1; i >= 0; i--) {
                locks[i].unlock();
            }
        }
    }
}
```

### 2. Distributed Lock Algorithms

#### Redlock (Redis)

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;
import java.util.*;

/**
 * Redlock: Distributed lock using multiple Redis instances.
 * Requires majority (N/2 + 1) of nodes to agree.
 */
public class RedlockDistributedLock {

    private final List<Jedis> redisNodes;
    private final int quorum;
    private final long lockTtlMs;
    private final long retryDelayMs;
    private final int maxRetries;

    public RedlockDistributedLock(List<Jedis> redisNodes, long lockTtlMs) {
        this.redisNodes = redisNodes;
        this.quorum = redisNodes.size() / 2 + 1;  // Majority
        this.lockTtlMs = lockTtlMs;
        this.retryDelayMs = 200;
        this.maxRetries = 3;
    }

    public LockResult tryLock(String resource) {
        String lockValue = UUID.randomUUID().toString();
        String lockKey = "lock:" + resource;

        for (int retry = 0; retry < maxRetries; retry++) {
            long startTime = System.currentTimeMillis();
            int acquiredCount = 0;
            List<Jedis> acquiredNodes = new ArrayList<>();

            // Try to acquire lock on all nodes
            for (Jedis node : redisNodes) {
                try {
                    String result = node.set(
                        lockKey, lockValue,
                        SetParams.setParams().nx().px(lockTtlMs)
                    );
                    if ("OK".equals(result)) {
                        acquiredCount++;
                        acquiredNodes.add(node);
                    }
                } catch (Exception e) {
                    // Node unavailable - continue
                }
            }

            // Calculate elapsed time
            long elapsedMs = System.currentTimeMillis() - startTime;

            // Check if we acquired quorum within valid time
            // (lock must be valid for at least some minimum time)
            long validityTime = lockTtlMs - elapsedMs - 2; // drift factor

            if (acquiredCount >= quorum && validityTime > 0) {
                return new LockResult(true, lockValue, validityTime);
            }

            // Failed - release any acquired locks
            for (Jedis node : acquiredNodes) {
                releaseLock(node, lockKey, lockValue);
            }

            // Wait before retry
            try {
                Thread.sleep(retryDelayMs + (long)(Math.random() * retryDelayMs));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new LockResult(false, null, 0);
            }
        }

        return new LockResult(false, null, 0);
    }

    public void unlock(String resource, String lockValue) {
        String lockKey = "lock:" + resource;

        for (Jedis node : redisNodes) {
            try {
                releaseLock(node, lockKey, lockValue);
            } catch (Exception e) {
                // Continue releasing on other nodes
            }
        }
    }

    private void releaseLock(Jedis node, String lockKey, String lockValue) {
        // Lua script: only delete if value matches
        String script =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end";

        node.eval(script,
            Collections.singletonList(lockKey),
            Collections.singletonList(lockValue));
    }

    public static class LockResult {
        public final boolean acquired;
        public final String lockValue;
        public final long validityTimeMs;

        LockResult(boolean acquired, String lockValue, long validityTimeMs) {
            this.acquired = acquired;
            this.lockValue = lockValue;
            this.validityTimeMs = validityTimeMs;
        }
    }
}
```

#### ZooKeeper Lock

```java
import org.apache.zookeeper.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Distributed lock using ZooKeeper ephemeral sequential nodes.
 * Fair lock: Requests are processed in order.
 */
public class ZooKeeperDistributedLock {

    private final ZooKeeper zk;
    private final String lockPath;
    private String currentLockNode;

    public ZooKeeperDistributedLock(ZooKeeper zk, String lockPath) {
        this.zk = zk;
        this.lockPath = lockPath;
    }

    public void lock() throws Exception {
        // Create ephemeral sequential node
        currentLockNode = zk.create(
            lockPath + "/lock_",
            new byte[0],
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL_SEQUENTIAL
        );

        while (true) {
            // Get all children and sort
            List<String> children = zk.getChildren(lockPath, false);
            Collections.sort(children);

            String currentNodeName = currentLockNode.substring(lockPath.length() + 1);
            int currentIndex = children.indexOf(currentNodeName);

            if (currentIndex == 0) {
                // We have the lock!
                return;
            }

            // Watch the previous node
            String prevNode = children.get(currentIndex - 1);
            CountDownLatch latch = new CountDownLatch(1);

            Stat stat = zk.exists(lockPath + "/" + prevNode, event -> {
                if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                    latch.countDown();
                }
            });

            if (stat != null) {
                // Wait for previous node to be deleted
                latch.await();
            }
            // Else: previous node already deleted, retry loop
        }
    }

    public void unlock() throws Exception {
        if (currentLockNode != null) {
            zk.delete(currentLockNode, -1);
            currentLockNode = null;
        }
    }

    // Try lock with timeout
    public boolean tryLock(long timeoutMs) throws Exception {
        long deadline = System.currentTimeMillis() + timeoutMs;

        currentLockNode = zk.create(
            lockPath + "/lock_",
            new byte[0],
            ZooDefs.Ids.OPEN_ACL_UNSAFE,
            CreateMode.EPHEMERAL_SEQUENTIAL
        );

        while (System.currentTimeMillis() < deadline) {
            List<String> children = zk.getChildren(lockPath, false);
            Collections.sort(children);

            String currentNodeName = currentLockNode.substring(lockPath.length() + 1);
            int currentIndex = children.indexOf(currentNodeName);

            if (currentIndex == 0) {
                return true;
            }

            String prevNode = children.get(currentIndex - 1);
            CountDownLatch latch = new CountDownLatch(1);

            Stat stat = zk.exists(lockPath + "/" + prevNode, event -> {
                latch.countDown();
            });

            if (stat != null) {
                long remainingMs = deadline - System.currentTimeMillis();
                if (remainingMs > 0) {
                    latch.await(remainingMs, java.util.concurrent.TimeUnit.MILLISECONDS);
                }
            }
        }

        // Timeout - cleanup
        unlock();
        return false;
    }
}
```

### 3. Database Locking

```java
/**
 * Database locking strategies.
 */
public class DatabaseLockingDemo {

    // Pessimistic Locking: SELECT FOR UPDATE
    public Order getOrderWithLock(Connection conn, long orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ? FOR UPDATE";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapToOrder(rs);
            }
            return null;
        }
        // Lock released when transaction commits/rollbacks
    }

    // Pessimistic with NOWAIT (fail immediately if locked)
    public Order getOrderWithLockNoWait(Connection conn, long orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ? FOR UPDATE NOWAIT";
        // Throws exception if row is locked
        // ...
        return null;
    }

    // Pessimistic with SKIP LOCKED (skip locked rows)
    public List<Order> getUnlockedOrders(Connection conn, int limit) throws SQLException {
        String sql = "SELECT * FROM orders WHERE status = 'PENDING' " +
                     "FOR UPDATE SKIP LOCKED LIMIT ?";
        // Returns only unlocked rows - useful for job processing
        // ...
        return null;
    }

    // Optimistic Locking: Version column
    public boolean updateOrderOptimistic(Connection conn, Order order) throws SQLException {
        String sql = "UPDATE orders SET status = ?, version = version + 1 " +
                     "WHERE id = ? AND version = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getStatus());
            stmt.setLong(2, order.getId());
            stmt.setInt(3, order.getVersion());

            int updated = stmt.executeUpdate();

            if (updated == 0) {
                // Version mismatch - another transaction modified it
                throw new OptimisticLockException("Order was modified by another transaction");
            }
            return true;
        }
    }

    // Advisory Lock (PostgreSQL)
    public void withAdvisoryLock(Connection conn, long lockId, Runnable action)
            throws SQLException {

        try (PreparedStatement lock = conn.prepareStatement("SELECT pg_advisory_lock(?)")) {
            lock.setLong(1, lockId);
            lock.execute();

            try {
                action.run();
            } finally {
                try (PreparedStatement unlock = conn.prepareStatement(
                        "SELECT pg_advisory_unlock(?)")) {
                    unlock.setLong(1, lockId);
                    unlock.execute();
                }
            }
        }
    }

    // Advisory Lock with timeout (PostgreSQL)
    public boolean tryAdvisoryLock(Connection conn, long lockId, long timeoutMs)
            throws SQLException {

        String sql = "SELECT pg_try_advisory_lock(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, lockId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getBoolean(1);
        }
    }

    private Order mapToOrder(ResultSet rs) throws SQLException {
        return new Order(); // mapping logic
    }

    static class OptimisticLockException extends RuntimeException {
        OptimisticLockException(String msg) { super(msg); }
    }
}
```

### 4. Spin Lock vs Mutex

```java
import java.util.concurrent.atomic.*;

/**
 * Spin Lock: Busy-wait loop, good for short critical sections.
 */
public class SpinLock {

    private final AtomicBoolean locked = new AtomicBoolean(false);

    // Basic spin lock
    public void lock() {
        while (!locked.compareAndSet(false, true)) {
            // Busy wait
        }
    }

    // Spin lock with exponential backoff
    public void lockWithBackoff() {
        int backoff = 1;
        int maxBackoff = 1000;

        while (!locked.compareAndSet(false, true)) {
            // Backoff
            for (int i = 0; i < backoff; i++) {
                Thread.onSpinWait();
            }
            backoff = Math.min(backoff * 2, maxBackoff);
        }
    }

    // Spin lock with yield
    public void lockWithYield() {
        while (!locked.compareAndSet(false, true)) {
            Thread.yield();  // Give up CPU time slice
        }
    }

    public void unlock() {
        locked.set(false);
    }

    public boolean tryLock() {
        return locked.compareAndSet(false, true);
    }
}

/**
 * Ticket Lock: Fair spin lock with FIFO ordering.
 */
public class TicketLock {

    private final AtomicLong ticketCounter = new AtomicLong(0);
    private final AtomicLong nowServing = new AtomicLong(0);

    public void lock() {
        long myTicket = ticketCounter.getAndIncrement();

        while (nowServing.get() != myTicket) {
            Thread.onSpinWait();
        }
    }

    public void unlock() {
        nowServing.incrementAndGet();
    }
}

/**
 * CLH Lock: Queue-based fair lock with better cache behavior.
 */
public class CLHLock {

    private final AtomicReference<Node> tail = new AtomicReference<>(new Node());
    private final ThreadLocal<Node> myNode = ThreadLocal.withInitial(Node::new);
    private final ThreadLocal<Node> myPred = new ThreadLocal<>();

    private static class Node {
        volatile boolean locked = false;
    }

    public void lock() {
        Node node = myNode.get();
        node.locked = true;

        // Add to queue
        Node pred = tail.getAndSet(node);
        myPred.set(pred);

        // Spin on predecessor's lock
        while (pred.locked) {
            Thread.onSpinWait();
        }
    }

    public void unlock() {
        Node node = myNode.get();
        node.locked = false;

        // Reuse predecessor's node
        myNode.set(myPred.get());
    }
}
```

**Comparison:**

| Aspect | Spin Lock | Mutex |
|--------|-----------|-------|
| Wait strategy | Busy-wait (CPU spinning) | Block (OS context switch) |
| Best for | Very short critical sections | Long critical sections |
| CPU usage | High when contended | Low when waiting |
| Latency | Low (no context switch) | Higher (context switch) |
| Fairness | Usually unfair | Can be fair |
| Use case | Low-level, real-time | General purpose |

---

## High Traffic Patterns

### 1. Request Coalescing

```java
import java.util.concurrent.*;

/**
 * Request Coalescing: Multiple identical requests share one backend call.
 * Prevents thundering herd problem.
 */
public class RequestCoalescer<K, V> {

    private final ConcurrentHashMap<K, CompletableFuture<V>> inFlightRequests =
        new ConcurrentHashMap<>();

    private final Function<K, V> fetchFunction;
    private final ExecutorService executor;

    public RequestCoalescer(Function<K, V> fetchFunction, ExecutorService executor) {
        this.fetchFunction = fetchFunction;
        this.executor = executor;
    }

    public CompletableFuture<V> get(K key) {
        // Try to join existing request or create new one
        return inFlightRequests.computeIfAbsent(key, k -> {
            CompletableFuture<V> future = CompletableFuture.supplyAsync(
                () -> fetchFunction.apply(k),
                executor
            );

            // Remove from map when complete
            future.whenComplete((result, ex) -> inFlightRequests.remove(k));

            return future;
        });
    }

    // With timeout for stale entries
    public CompletableFuture<V> getWithTimeout(K key, long timeoutMs) {
        return get(key).orTimeout(timeoutMs, TimeUnit.MILLISECONDS);
    }
}

/**
 * Batch Coalescer: Collect multiple requests into single batch call.
 */
public class BatchCoalescer<K, V> {

    private final int batchSize;
    private final long maxWaitMs;
    private final Function<List<K>, Map<K, V>> batchFunction;

    private final ConcurrentHashMap<K, CompletableFuture<V>> pendingRequests =
        new ConcurrentHashMap<>();
    private final BlockingQueue<K> requestQueue = new LinkedBlockingQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public BatchCoalescer(int batchSize, long maxWaitMs,
                          Function<List<K>, Map<K, V>> batchFunction) {
        this.batchSize = batchSize;
        this.maxWaitMs = maxWaitMs;
        this.batchFunction = batchFunction;

        startBatchProcessor();
    }

    public CompletableFuture<V> get(K key) {
        return pendingRequests.computeIfAbsent(key, k -> {
            requestQueue.offer(k);
            return new CompletableFuture<>();
        });
    }

    private void startBatchProcessor() {
        scheduler.scheduleWithFixedDelay(() -> {
            List<K> batch = new ArrayList<>();
            requestQueue.drainTo(batch, batchSize);

            if (!batch.isEmpty()) {
                try {
                    Map<K, V> results = batchFunction.apply(batch);

                    for (K key : batch) {
                        CompletableFuture<V> future = pendingRequests.remove(key);
                        if (future != null) {
                            V value = results.get(key);
                            if (value != null) {
                                future.complete(value);
                            } else {
                                future.completeExceptionally(
                                    new RuntimeException("Not found: " + key));
                            }
                        }
                    }
                } catch (Exception e) {
                    for (K key : batch) {
                        CompletableFuture<V> future = pendingRequests.remove(key);
                        if (future != null) {
                            future.completeExceptionally(e);
                        }
                    }
                }
            }
        }, 0, maxWaitMs, TimeUnit.MILLISECONDS);
    }
}
```

### 2. Load Shedding

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Load Shedding: Reject excess requests to maintain service quality.
 */
public class LoadShedder {

    private final int maxConcurrent;
    private final AtomicInteger currentLoad = new AtomicInteger(0);
    private final double shedThreshold;  // Start shedding at this percentage

    public LoadShedder(int maxConcurrent, double shedThreshold) {
        this.maxConcurrent = maxConcurrent;
        this.shedThreshold = shedThreshold;
    }

    public <T> T execute(Callable<T> task) throws Exception {
        int current = currentLoad.get();

        // Hard limit
        if (current >= maxConcurrent) {
            throw new LoadSheddingException("System overloaded");
        }

        // Probabilistic shedding near threshold
        double loadPercentage = (double) current / maxConcurrent;
        if (loadPercentage > shedThreshold) {
            double shedProbability = (loadPercentage - shedThreshold) / (1 - shedThreshold);
            if (Math.random() < shedProbability) {
                throw new LoadSheddingException("Request shed due to high load");
            }
        }

        currentLoad.incrementAndGet();
        try {
            return task.call();
        } finally {
            currentLoad.decrementAndGet();
        }
    }

    public static class LoadSheddingException extends RuntimeException {
        LoadSheddingException(String msg) { super(msg); }
    }
}

/**
 * Adaptive Load Shedder: Adjusts based on latency.
 */
public class AdaptiveLoadShedder {

    private final int maxConcurrent;
    private final long targetLatencyMs;
    private final AtomicInteger currentLoad = new AtomicInteger(0);
    private final AtomicLong avgLatencyMs = new AtomicLong(0);

    public AdaptiveLoadShedder(int maxConcurrent, long targetLatencyMs) {
        this.maxConcurrent = maxConcurrent;
        this.targetLatencyMs = targetLatencyMs;
    }

    public <T> T execute(Callable<T> task) throws Exception {
        // Shed if latency is high, regardless of concurrent count
        if (avgLatencyMs.get() > targetLatencyMs * 2 && currentLoad.get() > maxConcurrent / 2) {
            throw new LoadSheddingException("Shedding due to high latency");
        }

        if (currentLoad.get() >= maxConcurrent) {
            throw new LoadSheddingException("Max concurrent reached");
        }

        currentLoad.incrementAndGet();
        long start = System.currentTimeMillis();

        try {
            return task.call();
        } finally {
            long latency = System.currentTimeMillis() - start;
            updateLatency(latency);
            currentLoad.decrementAndGet();
        }
    }

    private void updateLatency(long latency) {
        // Exponential moving average
        avgLatencyMs.updateAndGet(avg -> (avg * 9 + latency) / 10);
    }

    public static class LoadSheddingException extends RuntimeException {
        LoadSheddingException(String msg) { super(msg); }
    }
}
```

### 3. Bulkhead Pattern

```java
import java.util.concurrent.*;

/**
 * Bulkhead: Isolate failures to prevent cascading.
 * Each service/operation has its own resource pool.
 */
public class BulkheadManager {

    private final ConcurrentHashMap<String, Bulkhead> bulkheads = new ConcurrentHashMap<>();

    public Bulkhead getBulkhead(String name, int maxConcurrent, int queueSize) {
        return bulkheads.computeIfAbsent(name,
            n -> new Bulkhead(n, maxConcurrent, queueSize));
    }

    public static class Bulkhead {
        private final String name;
        private final Semaphore semaphore;
        private final ExecutorService executor;
        private final AtomicLong rejectedCount = new AtomicLong(0);

        public Bulkhead(String name, int maxConcurrent, int queueSize) {
            this.name = name;
            this.semaphore = new Semaphore(maxConcurrent);
            this.executor = new ThreadPoolExecutor(
                maxConcurrent, maxConcurrent,
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize),
                new ThreadPoolExecutor.AbortPolicy()
            );
        }

        public <T> T execute(Callable<T> task, long timeoutMs) throws Exception {
            if (!semaphore.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS)) {
                rejectedCount.incrementAndGet();
                throw new BulkheadFullException("Bulkhead " + name + " is full");
            }

            try {
                Future<T> future = executor.submit(task);
                return future.get(timeoutMs, TimeUnit.MILLISECONDS);
            } finally {
                semaphore.release();
            }
        }

        public CompletableFuture<Void> executeAsync(Runnable task) {
            if (!semaphore.tryAcquire()) {
                rejectedCount.incrementAndGet();
                CompletableFuture<Void> failed = new CompletableFuture<>();
                failed.completeExceptionally(
                    new BulkheadFullException("Bulkhead " + name + " is full"));
                return failed;
            }

            return CompletableFuture.runAsync(task, executor)
                .whenComplete((result, ex) -> semaphore.release());
        }

        public long getRejectedCount() {
            return rejectedCount.get();
        }
    }

    public static class BulkheadFullException extends RuntimeException {
        BulkheadFullException(String msg) { super(msg); }
    }
}

// Usage example
public class BulkheadUsageDemo {

    private final BulkheadManager bulkheadManager = new BulkheadManager();

    // Separate bulkheads for different downstream services
    private final BulkheadManager.Bulkhead paymentBulkhead =
        bulkheadManager.getBulkhead("payment", 10, 50);

    private final BulkheadManager.Bulkhead inventoryBulkhead =
        bulkheadManager.getBulkhead("inventory", 20, 100);

    public PaymentResult processPayment(PaymentRequest request) throws Exception {
        return paymentBulkhead.execute(() -> {
            // Call payment service
            return callPaymentService(request);
        }, 5000);
    }

    public InventoryResult checkInventory(String productId) throws Exception {
        return inventoryBulkhead.execute(() -> {
            // Call inventory service
            return callInventoryService(productId);
        }, 3000);
    }

    private PaymentResult callPaymentService(PaymentRequest req) { return null; }
    private InventoryResult callInventoryService(String id) { return null; }
}
```

### 4. Hot Key Problem

```java
import java.util.concurrent.*;

/**
 * Hot Key Problem: Few keys receive disproportionate traffic.
 * Solutions: Local cache, key splitting, rate limiting per key.
 */
public class HotKeyHandler {

    // Solution 1: Local cache for hot keys
    private final ConcurrentHashMap<String, CacheEntry> localCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicLong> keyAccessCount = new ConcurrentHashMap<>();
    private final long hotKeyThreshold;
    private final long localCacheTtlMs;

    public HotKeyHandler(long hotKeyThreshold, long localCacheTtlMs) {
        this.hotKeyThreshold = hotKeyThreshold;
        this.localCacheTtlMs = localCacheTtlMs;
    }

    public String get(String key) {
        // Track access
        long count = keyAccessCount
            .computeIfAbsent(key, k -> new AtomicLong(0))
            .incrementAndGet();

        // Check local cache first for hot keys
        CacheEntry entry = localCache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.value;
        }

        // Fetch from remote cache/database
        String value = fetchFromRemote(key);

        // Cache locally if hot
        if (count > hotKeyThreshold) {
            localCache.put(key, new CacheEntry(value, localCacheTtlMs));
        }

        return value;
    }

    private String fetchFromRemote(String key) {
        // Fetch from Redis/Database
        return "value_" + key;
    }

    static class CacheEntry {
        final String value;
        final long expiresAt;

        CacheEntry(String value, long ttlMs) {
            this.value = value;
            this.expiresAt = System.currentTimeMillis() + ttlMs;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiresAt;
        }
    }
}

/**
 * Solution 2: Key splitting for hot keys.
 * Split hot key into multiple keys to distribute load.
 */
public class HotKeySplitter {

    private final int numSplits;
    private final Set<String> hotKeys;
    private final Random random = new Random();

    public HotKeySplitter(int numSplits, Set<String> hotKeys) {
        this.numSplits = numSplits;
        this.hotKeys = hotKeys;
    }

    public String getEffectiveKey(String key) {
        if (hotKeys.contains(key)) {
            // Split hot key: key -> key_0, key_1, ..., key_N
            int split = random.nextInt(numSplits);
            return key + "_" + split;
        }
        return key;
    }

    public void set(String key, String value) {
        if (hotKeys.contains(key)) {
            // Write to all splits
            for (int i = 0; i < numSplits; i++) {
                String splitKey = key + "_" + i;
                setInCache(splitKey, value);
            }
        } else {
            setInCache(key, value);
        }
    }

    public String get(String key) {
        String effectiveKey = getEffectiveKey(key);
        return getFromCache(effectiveKey);
    }

    private void setInCache(String key, String value) { /* ... */ }
    private String getFromCache(String key) { return null; }
}

/**
 * Solution 3: Rate limiting per key.
 */
public class PerKeyRateLimiter {

    private final ConcurrentHashMap<String, TokenBucket> keyLimiters = new ConcurrentHashMap<>();
    private final int tokensPerSecond;
    private final int bucketSize;

    public PerKeyRateLimiter(int tokensPerSecond, int bucketSize) {
        this.tokensPerSecond = tokensPerSecond;
        this.bucketSize = bucketSize;
    }

    public boolean tryAcquire(String key) {
        TokenBucket bucket = keyLimiters.computeIfAbsent(key,
            k -> new TokenBucket(tokensPerSecond, bucketSize));
        return bucket.tryAcquire();
    }

    // Cleanup old entries periodically
    public void cleanup() {
        long now = System.currentTimeMillis();
        keyLimiters.entrySet().removeIf(
            entry -> now - entry.getValue().lastAccess > 60_000);
    }

    static class TokenBucket {
        private final int refillRate;
        private final int capacity;
        private double tokens;
        private long lastRefillTime;
        volatile long lastAccess;

        TokenBucket(int refillRate, int capacity) {
            this.refillRate = refillRate;
            this.capacity = capacity;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean tryAcquire() {
            lastAccess = System.currentTimeMillis();
            refill();

            if (tokens >= 1) {
                tokens--;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double elapsed = (now - lastRefillTime) / 1000.0;
            tokens = Math.min(capacity, tokens + elapsed * refillRate);
            lastRefillTime = now;
        }
    }
}
```

### 5. Connection Pool Tuning

```java
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP connection pool tuning for high traffic.
 */
public class ConnectionPoolConfig {

    public HikariDataSource createOptimizedPool() {
        HikariConfig config = new HikariConfig();

        // Basic settings
        config.setJdbcUrl("jdbc:mysql://localhost:3306/mydb");
        config.setUsername("user");
        config.setPassword("password");

        // Pool sizing
        // Formula: connections = (core_count * 2) + effective_spindle_count
        // For SSD: connections = (core_count * 2) + 1
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);

        // Connection timeout (how long to wait for connection from pool)
        config.setConnectionTimeout(30_000);  // 30 seconds

        // Idle timeout (how long connection can sit idle before being removed)
        config.setIdleTimeout(600_000);  // 10 minutes

        // Max lifetime (max lifetime of connection in pool)
        config.setMaxLifetime(1_800_000);  // 30 minutes

        // Validation
        config.setConnectionTestQuery("SELECT 1");
        config.setValidationTimeout(5_000);  // 5 seconds

        // Leak detection
        config.setLeakDetectionThreshold(60_000);  // 1 minute

        // Performance optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        return new HikariDataSource(config);
    }

    /**
     * Monitor connection pool health.
     */
    public void monitorPool(HikariDataSource ds) {
        System.out.println("Active connections: " + ds.getHikariPoolMXBean().getActiveConnections());
        System.out.println("Idle connections: " + ds.getHikariPoolMXBean().getIdleConnections());
        System.out.println("Total connections: " + ds.getHikariPoolMXBean().getTotalConnections());
        System.out.println("Threads waiting: " + ds.getHikariPoolMXBean().getThreadsAwaitingConnection());
    }
}
```

### 6. Consistent Hashing

```java
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.security.MessageDigest;

/**
 * Consistent Hashing: Distribute load across nodes with minimal redistribution.
 */
public class ConsistentHash<T> {

    private final int virtualNodes;
    private final ConcurrentSkipListMap<Long, T> ring = new ConcurrentSkipListMap<>();
    private final MessageDigest md;

    public ConsistentHash(int virtualNodes) {
        this.virtualNodes = virtualNodes;
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addNode(T node) {
        for (int i = 0; i < virtualNodes; i++) {
            long hash = hash(node.toString() + "#" + i);
            ring.put(hash, node);
        }
    }

    public void removeNode(T node) {
        for (int i = 0; i < virtualNodes; i++) {
            long hash = hash(node.toString() + "#" + i);
            ring.remove(hash);
        }
    }

    public T getNode(String key) {
        if (ring.isEmpty()) {
            return null;
        }

        long hash = hash(key);

        // Find first node with hash >= key hash
        Map.Entry<Long, T> entry = ring.ceilingEntry(hash);

        // Wrap around if necessary
        if (entry == null) {
            entry = ring.firstEntry();
        }

        return entry.getValue();
    }

    // Get N nodes for replication
    public List<T> getNodes(String key, int count) {
        if (ring.isEmpty()) {
            return Collections.emptyList();
        }

        Set<T> nodes = new LinkedHashSet<>();
        long hash = hash(key);

        // Get nodes starting from the key's position
        SortedMap<Long, T> tailMap = ring.tailMap(hash);
        for (T node : tailMap.values()) {
            nodes.add(node);
            if (nodes.size() >= count) break;
        }

        // Wrap around if needed
        if (nodes.size() < count) {
            for (T node : ring.values()) {
                nodes.add(node);
                if (nodes.size() >= count) break;
            }
        }

        return new ArrayList<>(nodes);
    }

    private long hash(String key) {
        md.reset();
        byte[] digest = md.digest(key.getBytes());

        // Use first 8 bytes for long hash
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            hash = (hash << 8) | (digest[i] & 0xFF);
        }
        return hash;
    }
}

// Usage
public class ConsistentHashDemo {

    public void demo() {
        ConsistentHash<String> hash = new ConsistentHash<>(150);  // 150 virtual nodes

        // Add cache servers
        hash.addNode("cache-1.example.com");
        hash.addNode("cache-2.example.com");
        hash.addNode("cache-3.example.com");

        // Get server for a key
        String server = hash.getNode("user:123");
        System.out.println("Key 'user:123' maps to: " + server);

        // Simulate node failure - only ~1/N keys need remapping
        hash.removeNode("cache-2.example.com");

        // Get replicas for redundancy
        List<String> replicas = hash.getNodes("user:123", 2);
        System.out.println("Replicas: " + replicas);
    }
}
```

---

## Real-World Scenarios

### Scenario 1: High-Concurrency Counter (Like YouTube Views)

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * Problem: Millions of view increments per second.
 * Solution: Local counters + periodic flush to database.
 */
public class HighConcurrencyCounter {

    // LongAdder is more efficient than AtomicLong under high contention
    private final ConcurrentHashMap<String, LongAdder> localCounters = new ConcurrentHashMap<>();
    private final ScheduledExecutorService flusher = Executors.newSingleThreadScheduledExecutor();
    private final DatabaseService db;

    public HighConcurrencyCounter(DatabaseService db) {
        this.db = db;

        // Flush every 5 seconds
        flusher.scheduleAtFixedRate(this::flushToDatabase, 5, 5, TimeUnit.SECONDS);
    }

    public void increment(String key) {
        localCounters.computeIfAbsent(key, k -> new LongAdder()).increment();
    }

    public void incrementBy(String key, long delta) {
        localCounters.computeIfAbsent(key, k -> new LongAdder()).add(delta);
    }

    public long getApproximateCount(String key) {
        LongAdder adder = localCounters.get(key);
        long local = adder != null ? adder.sum() : 0;
        long persisted = db.getCount(key);
        return local + persisted;
    }

    private void flushToDatabase() {
        // Swap out current counters
        Map<String, Long> toFlush = new HashMap<>();

        localCounters.forEach((key, adder) -> {
            long count = adder.sumThenReset();
            if (count > 0) {
                toFlush.put(key, count);
            }
        });

        if (!toFlush.isEmpty()) {
            // Batch update to database
            db.batchIncrement(toFlush);
        }
    }

    public void shutdown() {
        flusher.shutdown();
        flushToDatabase();  // Final flush
    }

    interface DatabaseService {
        long getCount(String key);
        void batchIncrement(Map<String, Long> increments);
    }
}
```

### Scenario 2: Distributed Rate Limiter

```java
import redis.clients.jedis.Jedis;
import java.util.*;

/**
 * Problem: Rate limit across multiple server instances.
 * Solution: Sliding window with Redis.
 */
public class DistributedRateLimiter {

    private final Jedis redis;
    private final int maxRequests;
    private final long windowSizeMs;

    public DistributedRateLimiter(Jedis redis, int maxRequests, long windowSizeMs) {
        this.redis = redis;
        this.maxRequests = maxRequests;
        this.windowSizeMs = windowSizeMs;
    }

    // Sliding window using sorted set
    public boolean tryAcquire(String userId) {
        String key = "ratelimit:" + userId;
        long now = System.currentTimeMillis();
        long windowStart = now - windowSizeMs;

        // Lua script for atomic operation
        String script =
            "local key = KEYS[1]\n" +
            "local now = tonumber(ARGV[1])\n" +
            "local window_start = tonumber(ARGV[2])\n" +
            "local max_requests = tonumber(ARGV[3])\n" +
            "local window_size = tonumber(ARGV[4])\n" +
            "\n" +
            "-- Remove old entries\n" +
            "redis.call('ZREMRANGEBYSCORE', key, '-inf', window_start)\n" +
            "\n" +
            "-- Count current requests\n" +
            "local count = redis.call('ZCARD', key)\n" +
            "\n" +
            "if count < max_requests then\n" +
            "    -- Add new request\n" +
            "    redis.call('ZADD', key, now, now .. ':' .. math.random())\n" +
            "    redis.call('PEXPIRE', key, window_size)\n" +
            "    return 1\n" +
            "else\n" +
            "    return 0\n" +
            "end";

        Long result = (Long) redis.eval(
            script,
            Collections.singletonList(key),
            Arrays.asList(
                String.valueOf(now),
                String.valueOf(windowStart),
                String.valueOf(maxRequests),
                String.valueOf(windowSizeMs)
            )
        );

        return result == 1;
    }

    // Get remaining requests
    public int getRemainingRequests(String userId) {
        String key = "ratelimit:" + userId;
        long windowStart = System.currentTimeMillis() - windowSizeMs;

        redis.zremrangeByScore(key, "-inf", String.valueOf(windowStart));
        long count = redis.zcard(key);

        return Math.max(0, maxRequests - (int) count);
    }
}
```

### Scenario 3: Eventual Consistency with Conflict Resolution

```java
import java.util.*;

/**
 * Problem: Multiple services may update same data concurrently.
 * Solution: Last-write-wins with vector clocks for conflict detection.
 */
public class EventualConsistencyStore {

    private final ConcurrentHashMap<String, VersionedValue> store = new ConcurrentHashMap<>();

    static class VectorClock {
        private final Map<String, Long> clock = new ConcurrentHashMap<>();

        public void increment(String nodeId) {
            clock.merge(nodeId, 1L, Long::sum);
        }

        public void merge(VectorClock other) {
            for (Map.Entry<String, Long> entry : other.clock.entrySet()) {
                clock.merge(entry.getKey(), entry.getValue(), Math::max);
            }
        }

        public boolean happensBefore(VectorClock other) {
            boolean atLeastOneLess = false;

            for (String node : getAllNodes(other)) {
                long thisValue = clock.getOrDefault(node, 0L);
                long otherValue = other.clock.getOrDefault(node, 0L);

                if (thisValue > otherValue) {
                    return false;  // Not strictly before
                }
                if (thisValue < otherValue) {
                    atLeastOneLess = true;
                }
            }

            return atLeastOneLess;
        }

        public boolean isConcurrent(VectorClock other) {
            return !happensBefore(other) && !other.happensBefore(this);
        }

        private Set<String> getAllNodes(VectorClock other) {
            Set<String> nodes = new HashSet<>(clock.keySet());
            nodes.addAll(other.clock.keySet());
            return nodes;
        }

        public VectorClock copy() {
            VectorClock copy = new VectorClock();
            copy.clock.putAll(this.clock);
            return copy;
        }
    }

    static class VersionedValue {
        final String value;
        final VectorClock clock;
        final long timestamp;

        VersionedValue(String value, VectorClock clock) {
            this.value = value;
            this.clock = clock;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public void put(String key, String value, String nodeId) {
        store.compute(key, (k, existing) -> {
            VectorClock newClock;

            if (existing == null) {
                newClock = new VectorClock();
            } else {
                newClock = existing.clock.copy();
            }

            newClock.increment(nodeId);
            return new VersionedValue(value, newClock);
        });
    }

    public String get(String key) {
        VersionedValue v = store.get(key);
        return v != null ? v.value : null;
    }

    // Handle incoming replication
    public ConflictResult replicate(String key, String value, VectorClock incomingClock) {
        return store.compute(key, (k, existing) -> {
            if (existing == null) {
                return new VersionedValue(value, incomingClock);
            }

            if (incomingClock.happensBefore(existing.clock)) {
                // Incoming is older - keep existing
                return existing;
            }

            if (existing.clock.happensBefore(incomingClock)) {
                // Incoming is newer - accept it
                return new VersionedValue(value, incomingClock);
            }

            // Concurrent - conflict! Use LWW (last-write-wins) or merge
            VersionedValue winner = existing.timestamp > 0 ? existing :
                new VersionedValue(value, incomingClock);

            // Merge clocks
            winner.clock.merge(incomingClock);

            return winner;
        }) != null ? ConflictResult.ACCEPTED : ConflictResult.REJECTED;
    }

    enum ConflictResult { ACCEPTED, REJECTED, CONFLICT }
}
```

---

## Common Interview Questions

### Q1: How would you implement a thread-safe LRU cache?

```java
import java.util.*;
import java.util.concurrent.locks.*;

public class ConcurrentLRUCache<K, V> {

    private final int capacity;
    private final Map<K, Node<K, V>> map;
    private final Node<K, V> head, tail;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public ConcurrentLRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.head = new Node<>(null, null);
        this.tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            Node<K, V> node = map.get(key);
            if (node == null) return null;

            // Need write lock to move to front
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                moveToFront(node);
                return node.value;
            } finally {
                lock.writeLock().unlock();
                lock.readLock().lock();
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            Node<K, V> node = map.get(key);

            if (node != null) {
                node.value = value;
                moveToFront(node);
            } else {
                node = new Node<>(key, value);
                map.put(key, node);
                addToFront(node);

                if (map.size() > capacity) {
                    Node<K, V> lru = tail.prev;
                    remove(lru);
                    map.remove(lru.key);
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void moveToFront(Node<K, V> node) {
        remove(node);
        addToFront(node);
    }

    private void remove(Node<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    private void addToFront(Node<K, V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
}
```

### Q2: Explain deadlock vs livelock vs starvation.

| Condition | Deadlock | Livelock | Starvation |
|-----------|----------|----------|------------|
| Definition | Threads blocked waiting for each other | Threads active but no progress | Thread never gets resources |
| State | Blocked | Running | Waiting |
| Resource usage | Holding resources | Releasing/acquiring | Never acquiring |
| Recovery | Needs intervention | May resolve itself | May resolve with fairness |
| Example | A waits for B, B waits for A | Both back off and retry simultaneously | Low priority thread never scheduled |

```java
// Deadlock example
Object lock1 = new Object();
Object lock2 = new Object();

// Thread 1: synchronized(lock1) { synchronized(lock2) { } }
// Thread 2: synchronized(lock2) { synchronized(lock1) { } }

// Livelock example: Two people in corridor both moving same direction
AtomicBoolean resourceAvailable = new AtomicBoolean(true);
// Thread 1: while(!canProceed) { releaseResource(); waitRandom(); tryAcquire(); }
// Thread 2: Same pattern - both keep backing off

// Starvation: Non-fair lock with many high-priority threads
ReentrantLock unfairLock = new ReentrantLock(false);  // unfair
// Low priority thread may never acquire lock
```

### Q3: How to prevent database connection pool exhaustion?

1. **Proper pool sizing**: `connections = (core_count * 2) + effective_spindle_count`
2. **Connection timeout**: Fail fast instead of waiting indefinitely
3. **Leak detection**: Log connections held too long
4. **Statement timeout**: Kill long-running queries
5. **Circuit breaker**: Stop trying when database is overwhelmed
6. **Read replicas**: Distribute read load
7. **Caching**: Reduce database calls

### Q4: When to use pessimistic vs optimistic locking?

| Scenario | Use Pessimistic | Use Optimistic |
|----------|-----------------|----------------|
| Conflict probability | High | Low |
| Read:Write ratio | Write-heavy | Read-heavy |
| Transaction duration | Short | Can be long |
| Retry cost | High | Low |
| Example | Bank transfer | Profile update |

### Q5: How does ConcurrentHashMap achieve thread safety without locking entire map?

- **Segment locking** (Java 7): Map divided into segments, each with own lock
- **CAS + synchronized** (Java 8+): Lock-free reads, fine-grained locking for writes
- **Node-level locking**: Only lock the bucket being modified
- **Volatile reads**: Reads see latest writes without locking

---

## Summary: Quick Reference

| Problem | Solution |
|---------|----------|
| CPU-intensive parallel work | Fork/Join Framework |
| Lock-free data structures | CAS with AtomicReference |
| ABA problem | AtomicStampedReference |
| Reduce lock contention | Lock striping |
| Distributed coordination | ZooKeeper / Redlock |
| Thundering herd | Request coalescing |
| System overload | Load shedding |
| Cascading failures | Bulkhead pattern |
| Hot keys | Local cache + key splitting |
| Node failures | Consistent hashing |
| Concurrent modification | Optimistic locking (version) |
| Exclusive access | Pessimistic locking (SELECT FOR UPDATE) |

---

## References

- [Java Concurrency in Practice - Brian Goetz](https://jcip.net/)
- [The Art of Multiprocessor Programming](https://www.amazon.com/Art-Multiprocessor-Programming-Revised-Reprint/dp/0123973376)
- [Martin Kleppmann - Designing Data-Intensive Applications](https://dataintensive.net/)
- [Redis Distributed Locks (Redlock)](https://redis.io/topics/distlock)
- [HikariCP - Connection Pool](https://github.com/brettwooldridge/HikariCP)
