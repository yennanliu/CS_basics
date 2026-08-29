# 系統設計類的實作模式

> **範圍** — 那些會被當成「寫程式題」來考的分散式系統模式：一致性雜湊、token bucket 與 leaky bucket 限流、以及各種負載平衡演算法，附上可直接跑的實作，以及面試官一定會追問的取捨。
> **另見**：[design.md](./design.md) — 這些內容原本所屬的 LC「設計一個 X」總表；[design_examples.md](./design_examples.md) — 二十題完整寫過的 LC 設計題；[../../system_design/](../../system_design/) — 架構關卡的教材，那邊這些模式是方塊圖而不是程式碼；[hash_map.md](./hash_map.md) — 環背後的雜湊基礎。

## 為什麼這些內容要獨立成一份檔案

它們不是 LeetCode 題目，也不算架構關卡的白板題。位置剛好卡在中間：電話關或現場的 coding 關會要你在四十分鐘內*實作*一個出來，而且併發與邊界情況都要處理好。這讓它們算程式題 —— 但也讓 [design.md](./design.md) 一次橫跨兩個主題，這次拆檔就是要修掉這件事。

### 關鍵性質
- **複雜度**：各模式分別列出；環的查找是 O(log V)（V 為虛擬節點數），兩種限流器都是每個請求 O(1)
- **核心想法**：每一個都是用記憶體或精度，換一個樸素實作會失去的性質 —— 節點變動時的均勻分佈、突發流量的容忍度，或平滑的輸出速率
- **什麼時候用**：題目問的是「實作 X」，而不是「畫出 X」


## 1) 一致性雜湊（Consistent Hashing）


**概念：**
- 把資料／負載分散到多個節點上，而且能水平擴充
- 節點增減時，盡量減少要重新雜湊的資料量
- 用在：分散式快取、負載平衡、CDN
- **主要好處**：增減節點時只有 k/n 的 key 需要重新對應（不是全部）

**虛擬節點：**
- 每個實體節點在環上放多個虛擬節點
- 負載分佈更均勻，容錯也更好
- 常見設定：每個實體節點 100-200 個虛擬節點

**實作：**

```python
# V0 - Consistent Hashing with Virtual Nodes
import hashlib
import bisect
from typing import List, Optional

class ConsistentHashing:
    """
    Consistent Hashing Implementation with Virtual Nodes

    Time Complexity:
    - add_node: O(V log N) where V = virtual_nodes, N = total nodes
    - remove_node: O(V log N)
    - get_node: O(log N) binary search

    Space: O(V × P) where P = physical nodes
    """

    def __init__(self, virtual_nodes: int = 150):
        """
        virtual_nodes: number of virtual nodes per physical node
        """
        self.virtual_nodes = virtual_nodes
        self.ring = []  # Sorted list of (hash, node_id)
        self.nodes = set()  # Physical nodes

    def _hash(self, key: str) -> int:
        """Generate hash value for key"""
        return int(hashlib.md5(key.encode()).hexdigest(), 16)

    def add_node(self, node_id: str) -> None:
        """
        Add physical node with virtual nodes
        Time: O(V log N)
        """
        if node_id in self.nodes:
            return

        self.nodes.add(node_id)

        # Add virtual nodes to ring
        for i in range(self.virtual_nodes):
            virtual_key = f"{node_id}:vnode{i}"
            hash_value = self._hash(virtual_key)
            bisect.insort(self.ring, (hash_value, node_id))

    def remove_node(self, node_id: str) -> None:
        """
        Remove physical node and its virtual nodes
        Time: O(V × N) for removal
        """
        if node_id not in self.nodes:
            return

        self.nodes.remove(node_id)

        # Remove all virtual nodes
        self.ring = [(h, nid) for h, nid in self.ring if nid != node_id]

    def get_node(self, key: str) -> Optional[str]:
        """
        Find node responsible for key
        Time: O(log N) - binary search on ring
        """
        if not self.ring:
            return None

        hash_value = self._hash(key)

        # Binary search for first node >= hash_value
        idx = bisect.bisect_right(self.ring, (hash_value, ''))

        # Wrap around if at end
        if idx == len(self.ring):
            idx = 0

        return self.ring[idx][1]

    def get_nodes(self, key: str, count: int = 1) -> List[str]:
        """
        Get multiple nodes for replication
        Time: O(log N + count)
        """
        if not self.ring or count <= 0:
            return []

        hash_value = self._hash(key)
        idx = bisect.bisect_right(self.ring, (hash_value, ''))

        result = []
        seen = set()

        # Traverse ring clockwise
        for i in range(len(self.ring)):
            pos = (idx + i) % len(self.ring)
            node_id = self.ring[pos][1]

            if node_id not in seen:
                result.append(node_id)
                seen.add(node_id)

                if len(result) == count:
                    break

        return result

    def get_distribution(self) -> dict:
        """Get key distribution stats (for testing)"""
        # Sample 10000 keys and count distribution
        distribution = {node: 0 for node in self.nodes}

        for i in range(10000):
            key = f"key_{i}"
            node = self.get_node(key)
            if node:
                distribution[node] += 1

        return distribution
```

**Java 實作：**

```java
// Java - Consistent Hashing with Virtual Nodes
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

class ConsistentHashing {
    private final int virtualNodes;
    private final TreeMap<Long, String> ring; // Hash -> Node
    private final Set<String> nodes;
    private final MessageDigest md;

    /**
     * Constructor
     * time = O(1)
     * space = O(1)
     */
    public ConsistentHashing(int virtualNodes) {
        this.virtualNodes = virtualNodes;
        this.ring = new TreeMap<>();
        this.nodes = new HashSet<>();
        try {
            this.md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private long hash(String key) {
        md.reset();
        byte[] digest = md.digest(key.getBytes());
        long hash = 0;
        for (int i = 0; i < 8; i++) {
            hash = (hash << 8) | (digest[i] & 0xFF);
        }
        return hash;
    }

    /**
     * Add node with virtual nodes
     * time = O(V log N)
     * space = O(V)
     */
    public void addNode(String nodeId) {
        if (nodes.contains(nodeId)) return;

        nodes.add(nodeId);

        // Add virtual nodes
        for (int i = 0; i < virtualNodes; i++) {
            String virtualKey = nodeId + ":vnode" + i;
            long hash = hash(virtualKey);
            ring.put(hash, nodeId);
        }
    }

    /**
     * Remove node and its virtual nodes
     * time = O(V log N)
     * space = O(1)
     */
    public void removeNode(String nodeId) {
        if (!nodes.contains(nodeId)) return;

        nodes.remove(nodeId);

        // Remove all virtual nodes
        ring.entrySet().removeIf(entry -> entry.getValue().equals(nodeId));
    }

    /**
     * Find node for key
     * time = O(log N)
     * space = O(1)
     */
    public String getNode(String key) {
        if (ring.isEmpty()) return null;

        long hash = hash(key);

        // Find first entry >= hash (ceiling)
        Map.Entry<Long, String> entry = ring.ceilingEntry(hash);

        // Wrap around if null
        if (entry == null) {
            entry = ring.firstEntry();
        }

        return entry.getValue();
    }

    /**
     * Get multiple nodes for replication
     * time = O(log N + count)
     * space = O(count)
     */
    public List<String> getNodes(String key, int count) {
        if (ring.isEmpty() || count <= 0) return new ArrayList<>();

        long hash = hash(key);
        List<String> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        // Start from ceiling entry
        Long startKey = ring.ceilingKey(hash);
        if (startKey == null) startKey = ring.firstKey();

        // Traverse ring
        Iterator<Map.Entry<Long, String>> iter = ring.tailMap(startKey).entrySet().iterator();

        while (seen.size() < count) {
            if (!iter.hasNext()) {
                iter = ring.entrySet().iterator();
            }

            String nodeId = iter.next().getValue();
            if (!seen.contains(nodeId)) {
                result.add(nodeId);
                seen.add(nodeId);
            }
        }

        return result;
    }
}
```

**使用範例：**

```python
# Create consistent hashing with 150 virtual nodes per physical node
ch = ConsistentHashing(virtual_nodes=150)

# Add nodes
ch.add_node("node1")
ch.add_node("node2")
ch.add_node("node3")

# Get node for key
node = ch.get_node("user_12345")  # Returns node responsible for this key

# Get multiple nodes for replication
replicas = ch.get_nodes("user_12345", count=3)  # Returns [node1, node2, node3]

# Remove node - only keys on this node need remapping
ch.remove_node("node2")

# Check distribution
distribution = ch.get_distribution()
# {'node1': 5012, 'node2': 4988} - roughly even distribution
```

**相關的 LeetCode 題目：**
- LC 535 - Encode and Decode TinyURL（以雜湊做對應）
- 系統設計面試：設計短網址服務、設計分散式快取

---

## 2) 限流器 — Token Bucket


**概念：**
- 用 token bucket 演算法控制請求速率
- **Token**：以固定速率補充（refill_rate）
- **Bucket**：token 的容量上限
- **Request**：消耗 1 個 token，不夠就拒絕
- **使用情境**：API 限流、流量整形、QoS

**Token Bucket vs Leaky Bucket：**
- Token Bucket：允許突發流量，上限是桶子的容量
- Leaky Bucket：輸出速率平滑，不允許突發

**實作：**

```python
# LC 359 - Logger Rate Limiter (simplified variant)
# V0 - Token Bucket Rate Limiter
import time
from threading import Lock

class TokenBucketRateLimiter:
    """
    Token Bucket Rate Limiter

    Parameters:
    - capacity: max tokens in bucket (burst size)
    - refill_rate: tokens added per second

    Time Complexity:
    - allow_request: O(1)

    Space: O(1) per user/key
    """

    def __init__(self, capacity: int, refill_rate: float):
        """
        capacity: max tokens (burst size)
        refill_rate: tokens added per second
        """
        self.capacity = capacity
        self.refill_rate = refill_rate
        self.tokens = capacity  # Start with full bucket
        self.last_refill = time.time()
        self.lock = Lock()

    def _refill(self):
        """Refill tokens based on elapsed time"""
        now = time.time()
        elapsed = now - self.last_refill

        # Add tokens based on elapsed time
        tokens_to_add = elapsed * self.refill_rate
        self.tokens = min(self.capacity, self.tokens + tokens_to_add)
        self.last_refill = now

    def allow_request(self, tokens: int = 1) -> bool:
        """
        Try to consume tokens for request
        Returns: True if allowed, False if rate limited
        Time: O(1)
        """
        with self.lock:
            self._refill()

            if self.tokens >= tokens:
                self.tokens -= tokens
                return True

            return False

    def get_available_tokens(self) -> float:
        """Get current available tokens (for monitoring)"""
        with self.lock:
            self._refill()
            return self.tokens


# Multi-user Rate Limiter
class MultiUserRateLimiter:
    """
    Rate limiter for multiple users
    Each user has independent token bucket
    """

    def __init__(self, capacity: int, refill_rate: float):
        self.capacity = capacity
        self.refill_rate = refill_rate
        self.users = {}  # user_id -> (tokens, last_refill)
        self.lock = Lock()

    def allow_request(self, user_id: str, tokens: int = 1) -> bool:
        """
        Check if user can make request
        Time: O(1)
        Space: O(U) where U = number of users
        """
        with self.lock:
            now = time.time()

            if user_id not in self.users:
                # New user: initialize with full bucket
                self.users[user_id] = [self.capacity, now]

            user_tokens, last_refill = self.users[user_id]

            # Refill tokens
            elapsed = now - last_refill
            tokens_to_add = elapsed * self.refill_rate
            user_tokens = min(self.capacity, user_tokens + tokens_to_add)

            # Try to consume
            if user_tokens >= tokens:
                user_tokens -= tokens
                self.users[user_id] = [user_tokens, now]
                return True

            # Update last_refill even if rejected
            self.users[user_id] = [user_tokens, now]
            return False
```

**Java 實作：**

```java
// Java - Token Bucket Rate Limiter
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

class TokenBucketRateLimiter {
    private final int capacity;
    private final double refillRate; // tokens per second
    private double tokens;
    private long lastRefillTime;

    /**
     * Constructor
     * capacity: max tokens (burst size)
     * refillRate: tokens added per second
     */
    public TokenBucketRateLimiter(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    private synchronized void refill() {
        long now = System.currentTimeMillis();
        double elapsed = (now - lastRefillTime) / 1000.0; // Convert to seconds

        double tokensToAdd = elapsed * refillRate;
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTime = now;
    }

    /**
     * Try to consume tokens
     * time = O(1)
     * space = O(1)
     */
    public synchronized boolean allowRequest(int requestTokens) {
        refill();

        if (tokens >= requestTokens) {
            tokens -= requestTokens;
            return true;
        }

        return false;
    }

    public synchronized double getAvailableTokens() {
        refill();
        return tokens;
    }
}

// Multi-user Rate Limiter
class MultiUserRateLimiter {
    private final int capacity;
    private final double refillRate;
    private final Map<String, UserBucket> users;

    private static class UserBucket {
        double tokens;
        long lastRefillTime;

        UserBucket(double tokens, long time) {
            this.tokens = tokens;
            this.lastRefillTime = time;
        }
    }

    public MultiUserRateLimiter(int capacity, double refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.users = new ConcurrentHashMap<>();
    }

    /**
     * Check if user can make request
     * time = O(1)
     * space = O(U) where U = users
     */
    public boolean allowRequest(String userId, int requestTokens) {
        long now = System.currentTimeMillis();

        users.putIfAbsent(userId, new UserBucket(capacity, now));

        UserBucket bucket = users.get(userId);

        synchronized (bucket) {
            // Refill
            double elapsed = (now - bucket.lastRefillTime) / 1000.0;
            double tokensToAdd = elapsed * refillRate;
            bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
            bucket.lastRefillTime = now;

            // Try consume
            if (bucket.tokens >= requestTokens) {
                bucket.tokens -= requestTokens;
                return true;
            }

            return false;
        }
    }
}
```

**LC 359 - Logger Rate Limiter（簡化版）**

```python
# LC 359 - Logger Rate Limiter
class Logger:
    """
    Simplified rate limiter: only 1 message per key every 10 seconds
    This is a special case of token bucket where:
    - capacity = 1
    - refill_rate = 1 token per 10 seconds
    """

    def __init__(self):
        self.msg_dict = {}  # message -> last_timestamp

    def shouldPrintMessage(self, timestamp: int, message: str) -> bool:
        """
        time = O(1)
        space = O(M) where M = unique messages
        """
        if message not in self.msg_dict:
            self.msg_dict[message] = timestamp
            return True

        if timestamp - self.msg_dict[message] >= 10:
            self.msg_dict[message] = timestamp
            return True

        return False
```

**使用範例：**

```python
# Create rate limiter: 10 requests per second, burst up to 20
limiter = TokenBucketRateLimiter(capacity=20, refill_rate=10)

# Handle requests
for i in range(25):
    if limiter.allow_request():
        print(f"Request {i}: ALLOWED")
    else:
        print(f"Request {i}: RATE LIMITED")
    time.sleep(0.05)  # 50ms between requests

# Output:
# Requests 0-19: ALLOWED (burst uses all tokens)
# Requests 20-24: RATE LIMITED (no tokens left)
# After 1 second: 10 new tokens available
```

---

## 3) 限流器 — Leaky Bucket


**概念：**
- 以佇列為基礎的限流，輸出速率固定
- **Queue**：存放進來的請求（有容量上限）
- **Leak**：以固定速率處理請求
- **Overflow**：佇列滿了就拒絕請求
- **和 Token Bucket 的差別**：會把突發流量抹平，不會立刻處理整批突發

**實作：**

```python
# V0 - Leaky Bucket Rate Limiter
import time
from collections import deque
from threading import Lock

class LeakyBucketRateLimiter:
    """
    Leaky Bucket Rate Limiter

    Parameters:
    - capacity: max queue size
    - leak_rate: requests processed per second

    Time Complexity:
    - allow_request: O(1) amortized

    Space: O(capacity)
    """

    def __init__(self, capacity: int, leak_rate: float):
        """
        capacity: max requests in queue
        leak_rate: requests processed per second
        """
        self.capacity = capacity
        self.leak_rate = leak_rate
        self.queue = deque()
        self.last_leak = time.time()
        self.lock = Lock()

    def _leak(self):
        """Process (leak) requests at constant rate"""
        now = time.time()
        elapsed = now - self.last_leak

        # Calculate how many requests should have been processed
        requests_to_leak = int(elapsed * self.leak_rate)

        if requests_to_leak > 0:
            # Remove processed requests from queue
            for _ in range(min(requests_to_leak, len(self.queue))):
                self.queue.popleft()

            self.last_leak = now

    def allow_request(self) -> bool:
        """
        Try to add request to queue
        Returns: True if accepted, False if rejected
        Time: O(1) amortized
        """
        with self.lock:
            self._leak()

            if len(self.queue) < self.capacity:
                self.queue.append(time.time())
                return True

            return False

    def get_queue_size(self) -> int:
        """Get current queue size"""
        with self.lock:
            self._leak()
            return len(self.queue)


# Multi-user Leaky Bucket
class MultiUserLeakyBucket:
    """
    Leaky bucket rate limiter for multiple users
    """

    def __init__(self, capacity: int, leak_rate: float):
        self.capacity = capacity
        self.leak_rate = leak_rate
        self.users = {}  # user_id -> (queue, last_leak_time)
        self.lock = Lock()

    def allow_request(self, user_id: str) -> bool:
        """
        Check if user request can be added to queue
        Time: O(1) amortized
        Space: O(U × C) where U=users, C=capacity
        """
        with self.lock:
            now = time.time()

            if user_id not in self.users:
                self.users[user_id] = (deque(), now)

            queue, last_leak = self.users[user_id]

            # Leak requests
            elapsed = now - last_leak
            requests_to_leak = int(elapsed * self.leak_rate)

            if requests_to_leak > 0:
                for _ in range(min(requests_to_leak, len(queue))):
                    queue.popleft()
                last_leak = now

            # Try to add request
            if len(queue) < self.capacity:
                queue.append(now)
                self.users[user_id] = (queue, last_leak)
                return True

            self.users[user_id] = (queue, last_leak)
            return False
```

**Java 實作：**

```java
// Java - Leaky Bucket Rate Limiter
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class LeakyBucketRateLimiter {
    private final int capacity;
    private final double leakRate; // requests per second
    private final Queue<Long> queue;
    private long lastLeakTime;

    public LeakyBucketRateLimiter(int capacity, double leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.queue = new LinkedList<>();
        this.lastLeakTime = System.currentTimeMillis();
    }

    private synchronized void leak() {
        long now = System.currentTimeMillis();
        double elapsed = (now - lastLeakTime) / 1000.0;

        int requestsToLeak = (int) (elapsed * leakRate);

        if (requestsToLeak > 0) {
            for (int i = 0; i < Math.min(requestsToLeak, queue.size()); i++) {
                queue.poll();
            }
            lastLeakTime = now;
        }
    }

    /**
     * Try to add request to queue
     * time = O(1) amortized
     * space = O(capacity)
     */
    public synchronized boolean allowRequest() {
        leak();

        if (queue.size() < capacity) {
            queue.offer(System.currentTimeMillis());
            return true;
        }

        return false;
    }

    public synchronized int getQueueSize() {
        leak();
        return queue.size();
    }
}
```

**比較：Token Bucket vs Leaky Bucket**

| 面向 | Token Bucket | Leaky Bucket |
|---------|-------------|--------------|
| 突發處理 | 允許突發，上限為容量 | 把突發抹平 |
| 輸出速率 | 浮動（最高到容量） | 固定（漏出速率） |
| 適用情境 | API 限流、流量本身就會爆量 | 流量整形、要求輸出平滑 |
| 複雜度 | 每個請求 O(1) | 攤還 O(1) |
| 記憶體 | 每個使用者 O(1) | 每個使用者 O(capacity) |

**相關 LeetCode：**
- LC 359 - Logger Rate Limiter
- LC 362 - Design Hit Counter
- LC 635 - Design Log Storage System

---

## 4) 負載平衡演算法


**概念：**
- 把請求分散到多台伺服器
- **目標**：分佈均勻、高可用、低延遲
- **種類**：Round Robin、Weighted Round Robin、Least Connections、一致性雜湊

**實作：**

```python
# V0 - Load Balancer Implementations
from typing import List, Optional
import time
from threading import Lock

class RoundRobinLoadBalancer:
    """
    Round Robin Load Balancer
    Distributes requests sequentially across servers

    Time: O(1) per request
    Space: O(N) where N = number of servers
    """

    def __init__(self, servers: List[str]):
        self.servers = servers
        self.current = 0
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get next server in round-robin fashion
        Time: O(1)
        """
        if not self.servers:
            return None

        with self.lock:
            server = self.servers[self.current]
            self.current = (self.current + 1) % len(self.servers)
            return server

    def add_server(self, server: str):
        """Add new server to pool"""
        with self.lock:
            if server not in self.servers:
                self.servers.append(server)

    def remove_server(self, server: str):
        """Remove server from pool"""
        with self.lock:
            if server in self.servers:
                self.servers.remove(server)
                # Adjust current index if needed
                if self.current >= len(self.servers) and self.servers:
                    self.current = 0


class WeightedRoundRobinLoadBalancer:
    """
    Weighted Round Robin Load Balancer
    Servers with higher weight get more requests

    Example: weights = [5, 1, 1]
    -> Server 0 gets 5/7 of requests
    -> Server 1 and 2 each get 1/7 of requests

    Time: O(1) per request
    Space: O(sum of weights)
    """

    def __init__(self, servers: List[str], weights: List[int]):
        """
        servers: list of server IDs
        weights: corresponding weights (must be positive)
        """
        if len(servers) != len(weights):
            raise ValueError("Servers and weights must have same length")

        self.servers = []
        # Expand servers based on weights
        for server, weight in zip(servers, weights):
            self.servers.extend([server] * weight)

        self.current = 0
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get next server based on weights
        Time: O(1)
        """
        if not self.servers:
            return None

        with self.lock:
            server = self.servers[self.current]
            self.current = (self.current + 1) % len(self.servers)
            return server


class LeastConnectionsLoadBalancer:
    """
    Least Connections Load Balancer
    Routes to server with fewest active connections

    Time: O(N) per request (find min)
    Space: O(N)

    Better for long-lived connections (WebSockets, DB connections)
    """

    def __init__(self, servers: List[str]):
        self.connections = {server: 0 for server in servers}
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get server with least connections
        Time: O(N) where N = servers
        """
        if not self.connections:
            return None

        with self.lock:
            # Find server with minimum connections
            server = min(self.connections, key=self.connections.get)
            self.connections[server] += 1
            return server

    def release_connection(self, server: str):
        """
        Release connection from server
        Call this when request completes
        """
        with self.lock:
            if server in self.connections:
                self.connections[server] = max(0, self.connections[server] - 1)

    def add_server(self, server: str):
        """Add new server"""
        with self.lock:
            if server not in self.connections:
                self.connections[server] = 0

    def remove_server(self, server: str):
        """Remove server"""
        with self.lock:
            if server in self.connections:
                del self.connections[server]


class WeightedLeastConnectionsLoadBalancer:
    """
    Weighted Least Connections
    Considers both connection count and server capacity

    Score = connections / weight (lower is better)

    Time: O(N) per request
    Space: O(N)
    """

    def __init__(self, servers: List[str], weights: List[int]):
        self.servers = servers
        self.weights = {server: weight for server, weight in zip(servers, weights)}
        self.connections = {server: 0 for server in servers}
        self.lock = Lock()

    def get_server(self) -> Optional[str]:
        """
        Get server with lowest connections/weight ratio
        Time: O(N)
        """
        if not self.servers:
            return None

        with self.lock:
            # Calculate score for each server
            best_server = None
            best_score = float('inf')

            for server in self.servers:
                score = self.connections[server] / self.weights[server]
                if score < best_score:
                    best_score = score
                    best_server = server

            self.connections[best_server] += 1
            return best_server

    def release_connection(self, server: str):
        """Release connection"""
        with self.lock:
            if server in self.connections:
                self.connections[server] = max(0, self.connections[server] - 1)
```

**Java 實作：**

```java
// Java - Round Robin Load Balancer
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

class RoundRobinLoadBalancer {
    private final List<String> servers;
    private final AtomicInteger current;

    public RoundRobinLoadBalancer(List<String> servers) {
        this.servers = new ArrayList<>(servers);
        this.current = new AtomicInteger(0);
    }

    /**
     * Get next server
     * time = O(1)
     * space = O(N)
     */
    public String getServer() {
        if (servers.isEmpty()) return null;

        int index = current.getAndIncrement() % servers.size();
        return servers.get(index);
    }

    public synchronized void addServer(String server) {
        if (!servers.contains(server)) {
            servers.add(server);
        }
    }

    public synchronized void removeServer(String server) {
        servers.remove(server);
    }
}

// Java - Least Connections Load Balancer
class LeastConnectionsLoadBalancer {
    private final Map<String, Integer> connections;

    public LeastConnectionsLoadBalancer(List<String> servers) {
        this.connections = new HashMap<>();
        for (String server : servers) {
            connections.put(server, 0);
        }
    }

    /**
     * Get server with least connections
     * time = O(N)
     * space = O(N)
     */
    public synchronized String getServer() {
        if (connections.isEmpty()) return null;

        return connections.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(entry -> {
                String server = entry.getKey();
                connections.put(server, entry.getValue() + 1);
                return server;
            })
            .orElse(null);
    }

    public synchronized void releaseConnection(String server) {
        connections.computeIfPresent(server, (k, v) -> Math.max(0, v - 1));
    }
}
```

**使用範例：**

```python
# Round Robin
rr_lb = RoundRobinLoadBalancer(["server1", "server2", "server3"])
for _ in range(6):
    print(rr_lb.get_server())
# Output: server1, server2, server3, server1, server2, server3

# Weighted Round Robin
wrr_lb = WeightedRoundRobinLoadBalancer(
    servers=["server1", "server2", "server3"],
    weights=[5, 2, 1]  # server1 gets 5/8 of traffic
)

# Least Connections
lc_lb = LeastConnectionsLoadBalancer(["server1", "server2", "server3"])
server = lc_lb.get_server()  # Returns server with fewest connections
# ... process request ...
lc_lb.release_connection(server)  # Release when done
```

**負載平衡演算法比較：**

| 演算法 | 時間 | 空間 | 最適合 | 優點 | 缺點 |
|-----------|------|-------|----------|------|------|
| Round Robin | O(1) | O(N) | 短請求 | 簡單、公平 | 完全不看伺服器負載 |
| Weighted RR | O(1) | O(W) | 各機器規格不一 | 有考慮機器容量 | 權重是寫死的 |
| Least Connections | O(N) | O(N) | 長連線 | 會動態依負載調整 | 額外開銷較高 |
| Consistent Hashing | O(log N) | O(VN) | 快取、sticky session | 重新對應的量最小 | 實作較複雜 |

**相關 LeetCode：**
- 系統設計：設計分散式快取、設計網路爬蟲
- LC 535 - Encode and Decode TinyURL（挑選存放 URL 的伺服器）

---

## 5) 面試技巧


**可以講的重點：**

1. **一致性雜湊：**
   - 「節點增減時，資料搬移量最小」
   - 「虛擬節點讓負載分佈更平均」
   - 「Cassandra、DynamoDB、Memcached 都在用」

2. **限流：**
   - **Token Bucket**：「允許可控的突發，很適合 API」
   - **Leaky Bucket**：「輸出平滑，適合流量整形」
   - 「選哪個，取決於能不能接受突發流量」

3. **負載平衡：**
   - **Round Robin**：「簡單，機器規格一致時很好用」
   - **Weighted**：「伺服器容量不同時用這個」
   - **Least Connections**：「長連線的情況比較適合」
   - **Consistent Hashing**：「有狀態的服務（例如快取）用這個」

**常見的面試問題：**

1. 「設計一個 API 的限流器」
   → 要支援突發就用 token bucket，分散式狀態放 Redis

2. 「你會怎麼把負載分散到多台伺服器？」
   → 先講 Round Robin，再依需求帶出其他選項

3. 「設計一個分散式快取」
   → 用一致性雜湊分配 key，順便聊複本

4. 「負載平衡器要怎麼處理伺服器掛掉？」
   → 健康檢查、自動移除、重試機制

**實作上的提醒：**

- **執行緒安全**：併發存取要用鎖或原子操作
- **分散式系統**：共享狀態放 Redis／Memcached
- **監控**：追蹤指標（請求速率、伺服器負載、錯誤率）
- **健康檢查**：定期確認伺服器還活著
- **優雅降級**：出問題時不要整個服務一起倒
