# Bellman-Ford Algorithm

> **範圍** — 容得下**負權重**、而且能偵測負環的單源最短路徑演算法，另外還有限制跳數（k hop）的變形。
> **另見**：[shortest_path_comparison.md](./shortest_path_comparison.md) — 該挑哪個演算法；[Dijkstra.md](./Dijkstra.md) — 權重全非負時更快；[Floyd-Warshall.md](./Floyd-Warshall.md) — 全點對最短路徑。

## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 總覽
**Bellman-Ford 演算法**是一個能處理負邊權的單源最短路徑演算法。跟 Dijkstra（戴克斯特拉）不同，它可以偵測負環；只要圖中沒有負環，它保證找得到最短路徑。

### 關鍵性質
- **時間複雜度**：O(V·E)，V 是頂點數、E 是邊數
- **空間複雜度**：O(V)，用於距離陣列
- **核心想法**：把所有邊鬆弛 V-1 次
- **什麼時候用**：有負權重的單源最短路徑
- **特色**：`可以偵測`負環

### 核心特徵
- **實作簡單**：就是兩層巢狀迴圈掃邊
- **吃得下負權重**：跟 Dijkstra 不同，負邊也能處理
- **能偵測環**：可以找出圖中的負環
- **以鬆弛為基礎**：反覆鬆弛邊，直到收斂
- **保證最佳**：只要沒有負環，找到的就是最短路徑

### 參考資料
- [Bellman-Ford Visualization](https://www.cs.usfca.edu/~galles/visualization/BellmanFord.html)
- [CP Algorithms - Bellman-Ford](https://cp-algorithms.com/graph/bellman_ford.html)
- [Dijkstra Cheatsheet](./Dijkstra.md) - 非負權重時的對照
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md) - 全點對的對照


## 題型分類

### **分類 1：帶負權重的經典最短路徑**
- **說明**：有負邊的標準單源最短路徑
- **例子**：LC 787（Cheapest Flights K Stops）、貨幣換算
- **模式**：直接套 Bellman-Ford

### **分類 2：偵測負環**
- **說明**：判斷圖中是否含負環
- **例子**：LC 1334（套利偵測）、貨幣套利
- **模式**：多跑第 V 輪，看還有沒有更新

### **分類 3：帶限制的最短路徑**
- **說明**：最多只能用 K 條邊／K 次跳躍的最短路徑
- **例子**：LC 787（K 站中轉）、LC 1928（連線次數受限）
- **模式**：把迭代次數設上限的 Bellman-Ford

### **分類 4：貨幣兌換與套利**
- **說明**：把權重取對數，藉此偵測套利機會
- **例子**：外匯交易、價格套利
- **模式**：取對數轉換 + 偵測負環

### **分類 5：帶成本的網路路由**
- **說明**：在可能有負成本（折扣）的情況下找最便宜的路徑
- **例子**：有折扣的配送、有回饋的路由
- **模式**：標準 Bellman-Ford 加上成本追蹤


## 模板與演算法

### 模板比較表
| 模板類型 | 適用情境 | 迭代次數 | 什麼時候用 |
|---------------|----------|------------|-------------|
| **基本 Bellman-Ford** | 標準最短路徑 | V-1 | 有負權重 |
| **加上負環偵測** | 偵測負環 | V | 套利、驗證 |
| **限制 K 條邊** | 邊數受限的路徑 | K | 跳數／中轉次數受限 |
| **SPFA（佇列最佳化）** | 平均情況更快 | 不固定 | 稀疏圖 |
| **路徑重建** | 記錄實際路徑 | V-1 | 需要路徑細節 |

### 模板 1：基本 Bellman-Ford
```python
def bellman_ford(n, edges, src):
    """
    Find shortest paths from src to all vertices
    n: number of vertices (0-indexed)
    edges: list of (u, v, weight)
    src: source vertex
    Returns: distance array or None if negative cycle exists
    """
    # Initialize distances
    dist = [float('inf')] * n
    dist[src] = 0

    # Relax all edges V-1 times
    for i in range(n - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True

        # Early termination: no updates in this iteration
        if not updated:
            break

    # Check for negative cycles (V-th iteration)
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return None  # Negative cycle detected

    return dist
```

### 模板 2：帶負環偵測的 Bellman-Ford
```python
def bellman_ford_with_cycle_detection(n, edges, src):
    """
    Bellman-Ford with detailed negative cycle detection
    Returns: (distances, has_negative_cycle, cycle_nodes)
    """
    dist = [float('inf')] * n
    dist[src] = 0
    parent = [-1] * n

    # Relax edges V-1 times
    for i in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u

    # Check for negative cycle and track affected nodes
    negative_cycle_node = -1
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            dist[v] = dist[u] + w
            parent[v] = u
            negative_cycle_node = v

    if negative_cycle_node == -1:
        return dist, False, []

    # Extract negative cycle
    cycle = []
    visited = set()
    current = negative_cycle_node

    # Walk back to find cycle
    for _ in range(n):
        current = parent[current]

    # Now current is definitely in the cycle
    cycle_start = current
    while True:
        cycle.append(current)
        current = parent[current]
        if current == cycle_start:
            cycle.append(current)
            break

    cycle.reverse()
    return dist, True, cycle
```

### 模板 3：限制 K 條邊的 Bellman-Ford
```python
def bellman_ford_k_edges(n, edges, src, dst, k):
    """
    Find shortest path using at most k edges
    Useful for problems like "Cheapest Flights Within K Stops"
    """
    # dist[i] = shortest distance to vertex i
    dist = [float('inf')] * n
    dist[src] = 0

    # Relax edges exactly k times (at most k edges)
    for iteration in range(k):
        # Use temporary array to avoid using updated values in same iteration
        temp_dist = dist.copy()

        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < temp_dist[v]:
                temp_dist[v] = dist[u] + w

        dist = temp_dist

        # Early termination
        if iteration > 0 and dist[dst] == prev_dist:
            break
        prev_dist = dist[dst]

    return dist[dst] if dist[dst] != float('inf') else -1
```

### 模板 4：SPFA（Shortest Path Faster Algorithm）
```python
from collections import deque

def spfa(n, edges, src):
    """
    Queue-optimized Bellman-Ford (SPFA)
    Average case: O(E), Worst case: O(V·E)
    Only relax edges from vertices that were updated
    """
    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v, w in edges:
        graph[u].append((v, w))

    dist = [float('inf')] * n
    dist[src] = 0

    # Queue of vertices to process
    queue = deque([src])
    in_queue = [False] * n
    in_queue[src] = True

    # Count times each vertex is added to queue
    count = [0] * n
    count[src] = 1

    while queue:
        u = queue.popleft()
        in_queue[u] = False

        for v, w in graph[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

                if not in_queue[v]:
                    queue.append(v)
                    in_queue[v] = True
                    count[v] += 1

                    # Negative cycle detection
                    if count[v] >= n:
                        return None  # Negative cycle

    return dist
```

### 模板 5：帶路徑重建的 Bellman-Ford
```python
def bellman_ford_with_path(n, edges, src, dst):
    """
    Find shortest path and reconstruct the actual path
    Returns: (distance, path)
    """
    dist = [float('inf')] * n
    dist[src] = 0
    parent = [-1] * n

    # Relax edges V-1 times
    for i in range(n - 1):
        updated = False
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u
                updated = True

        if not updated:
            break

    # Check for negative cycle
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return float('inf'), []  # Negative cycle

    # Reconstruct path
    if dist[dst] == float('inf'):
        return float('inf'), []

    path = []
    current = dst
    while current != -1:
        path.append(current)
        current = parent[current]

    path.reverse()
    return dist[dst], path
```

### 模板 6：貨幣套利偵測
```python
import math

def detect_arbitrage(n, exchange_rates):
    """
    Detect arbitrage opportunity in currency exchange
    exchange_rates[i][j] = rate from currency i to currency j
    Returns: True if arbitrage exists, False otherwise
    """
    # Convert to logarithmic weights
    # If product of rates > 1, arbitrage exists
    # Take negative log to use Bellman-Ford for negative cycles
    edges = []
    for u in range(n):
        for v in range(n):
            if u != v and exchange_rates[u][v] > 0:
                # Negative log to convert product to sum
                weight = -math.log(exchange_rates[u][v])
                edges.append((u, v, weight))

    # Run Bellman-Ford from any source
    dist = [float('inf')] * n
    dist[0] = 0

    # Relax edges V-1 times
    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # Check for negative cycle (arbitrage)
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return True  # Arbitrage detected

    return False
```


## 演算法比較

### Bellman-Ford vs Dijkstra vs Floyd-Warshall

| 特性 | Bellman-Ford | Dijkstra | Floyd-Warshall |
|---------|--------------|----------|----------------|
| **問題類型** | 單源 | 單源 | 全點對 |
| **時間複雜度** | O(V·E) | O((V+E) log V) | O(V³) |
| **空間複雜度** | O(V) | O(V) | O(V²) |
| **負權重** | ✅ 可以 | ❌ 不行 | ✅ 可以 |
| **負環** | ✅ 可偵測 | N/A | ✅ 可偵測 |
| **實作難度** | 簡單（2 層迴圈） | 中等（要優先佇列） | 非常簡單（3 層迴圈） |
| **提早結束** | ✅ 可以 | ✅ 抵達目標即可停 | ❌ 必須跑完 |
| **平均效能** | 稠密圖上很慢 | 稀疏圖上很快 | 小圖上不錯 |
| **最適合的圖** | 任何圖（尤其有負邊時） | 稀疏、非負 | 稠密、小型 |
| **最佳化手段** | SPFA 變形 | 加啟發式的 A* | 沒有實用的 |

### 各演算法的適用時機

```text
Shortest Path Algorithm Selection:

1. Does graph have negative edge weights?
   ├── YES → Continue to 2
   │   ├── Single-source? → Use Bellman-Ford
   │   ├── All-pairs? → Use Floyd-Warshall
   │   └── Need cycle detection? → Use Bellman-Ford
   │
   └── NO → Continue to 3

2. Single-source or all-pairs?
   ├── Single-source → Use Dijkstra
   │   └── Very sparse graph? → Consider SPFA
   │
   └── All-pairs → Continue to 4

3. What's the graph size?
   ├── Small (V ≤ 400) → Floyd-Warshall
   └── Large → Run Dijkstra V times

4. Special considerations:
   ├── Need to detect negative cycles? → Bellman-Ford or Floyd-Warshall
   ├── Edge count constraint (K edges)? → Modified Bellman-Ford
   ├── Unweighted graph? → BFS
   └── Tree structure? → DFS/BFS
```

### 效能比較

**例子：V=1000 個頂點的圖**

| 圖的稠密度 | 邊數 | Bellman-Ford | Dijkstra | SPFA（平均） |
|---------------|-------|--------------|----------|------------|
| 稀疏 | 2,000 | 2,000,000 次操作 | ~20,000 次操作 ⚡ | ~40,000 次操作 |
| 中等 | 10,000 | 10,000,000 次操作 | ~100,000 次操作 ⚡ | ~200,000 次操作 |
| 稠密 | 100,000 | 100,000,000 次操作 | ~1,000,000 次操作 ⚡ | ~2,000,000 次操作 |

**註**：在非負權重的情況下，Dijkstra 通常比 Bellman-Ford 快上 50-100 倍。

### 演算法挑選範例

| 情境 | 最佳演算法 | 為什麼 |
|----------|----------------|-----|
| GPS 導航（道路網） | **Dijkstra** | 非負權重、稀疏圖 |
| 帶手續費的貨幣兌換 | **Bellman-Ford** | 可能出現負權重 |
| 套利偵測 | **Bellman-Ford** | 需要偵測負環 |
| 網路延遲時間 | **Dijkstra** | 非負、單源 |
| 課程先修關係（全點對） | **Floyd-Warshall** | 小圖、遞移閉包 |
| 最多 K 次中轉的航班 | **Bellman-Ford（跑 K 輪）** | 有邊數限制 |
| 網際網路路由（OSPF） | **Dijkstra** | 成本非負 |
| 外匯交易機會 | **Bellman-Ford** | 偵測套利環 |


## LC 範例

### 2-1) Cheapest Flights Within K Stops (LC 787) — 鬆弛 K 輪的 Bellman-Ford
> 最多鬆弛 K+1 輪；每輪都複製一份 dist 陣列，避免用到同一輪剛更新的邊。

```java
// LC 787 - Cheapest Flights Within K Stops
// IDEA: Bellman-Ford — relax edges k+1 times; copy dist array each iteration
// time = O(K * E), space = O(N)
public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    int[] dist = new int[n];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[src] = 0;
    for (int i = 0; i <= k; i++) {
        int[] tmp = Arrays.copyOf(dist, n);
        for (int[] f : flights) {
            if (dist[f[0]] != Integer.MAX_VALUE)
                tmp[f[1]] = Math.min(tmp[f[1]], dist[f[0]] + f[2]);
        }
        dist = tmp;
    }
    return dist[dst] == Integer.MAX_VALUE ? -1 : dist[dst];
}
```

```python
# LC 787 - Cheapest Flights Within K Stops
# Classic Bellman-Ford with K edges constraint

def findCheapestPrice(n, flights, src, dst, k):
    """
    Find cheapest price with at most k stops (k+1 edges)
    """
    # Initialize distances
    prices = [float('inf')] * n
    prices[src] = 0

    # Relax edges at most k+1 times (k stops = k+1 edges)
    for i in range(k + 1):
        # Use temp array to ensure we only use distances from previous iteration
        temp_prices = prices.copy()

        for u, v, price in flights:
            if prices[u] != float('inf'):
                temp_prices[v] = min(temp_prices[v], prices[u] + price)

        prices = temp_prices

    return prices[dst] if prices[dst] != float('inf') else -1


# Alternative: SPFA with stops tracking
def findCheapestPrice_SPFA(n, flights, src, dst, k):
    """
    SPFA variant with stops tracking
    """
    from collections import deque

    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v, price in flights:
        graph[u].append((v, price))

    # (cost, node, stops)
    queue = deque([(0, src, 0)])
    # best_cost[node] = minimum cost to reach node
    best = {src: 0}

    min_cost = float('inf')

    while queue:
        cost, node, stops = queue.popleft()

        # Reached destination
        if node == dst:
            min_cost = min(min_cost, cost)
            continue

        # Exceeded stops limit
        if stops > k:
            continue

        # Explore neighbors
        for neighbor, price in graph[node]:
            new_cost = cost + price

            # Pruning: only continue if this is a better path
            if new_cost < best.get(neighbor, float('inf')) or new_cost < min_cost:
                best[neighbor] = new_cost
                queue.append((new_cost, neighbor, stops + 1))

    return min_cost if min_cost != float('inf') else -1
```

#### **變形：分層 DP 觀點 `dp[t][v]` — 「最多 K 條邊」vs「剛好 K 條邊」** ⭐⭐⭐⭐⭐

> 鬆弛完全一樣，只是把輪次索引攤開成一個 DP 維度。**關鍵差異**：保留前一列的繼承（`dp[t] = dp[t-1]`）代表**最多** K 條邊；不保留就代表**剛好** K 條邊。

**核心想法**：Bellman-Ford *本來就是*一個以「用了幾條邊」為維度的 DP。這也是為什麼限制跳數的那題（LC 787）同時出現在本文開頭連的 `shortest-path` 和 `dynamic-programming` 兩個標籤頁上。

**遞迴式**：
```text
dp[t][v] = min( dp[t-1][v],                                  <-- carry-over => "at most t edges"
                min over each edge (u -> v, w) of dp[t-1][u] + w )

dp[0][src] = 0, dp[0][*] = INF
answer     = dp[k+1][dst]        # k stops == k+1 edges
```

把 `t` 明確寫出來，剛好回答了面試官幾乎一定會追問的兩個問題：
- *「滾動版為什麼要複製陣列？」* → 第 `t` 輪只能讀第 `t-1` 輪；沒有複製的話，一輪之內就可能串起 2 條以上的邊，悄悄超出跳數上限。
- *「如果路徑必須**剛好**用 K 條邊呢？」* → 每一列從 `INF` 開始，而不是複製前一列，這樣比較短的解就活不下來。

```java
// java
// LC 787 - Cheapest Flights Within K Stops
// IDEA: layered DP form of Bellman-Ford — dp[t][v] = cheapest cost to reach v using at most t edges
// time = O(K * E), space = O(K * N) for the full table (O(N) if you keep only 2 rows)
public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    final int INF = Integer.MAX_VALUE / 2;          // /2 so dp[u] + w never overflows
    int[][] dp = new int[k + 2][n];
    Arrays.fill(dp[0], INF);
    dp[0][src] = 0;

    for (int t = 1; t <= k + 1; t++) {
        dp[t] = dp[t - 1].clone();                  // carry-over => "AT MOST t edges"
        for (int[] f : flights) {
            if (dp[t - 1][f[0]] < INF)
                dp[t][f[1]] = Math.min(dp[t][f[1]], dp[t - 1][f[0]] + f[2]);
        }
    }
    return dp[k + 1][dst] >= INF ? -1 : dp[k + 1][dst];
}

// Variant: shortest path using EXACTLY k edges — just drop the carry-over row
// time = O(K * E), space = O(N)
public int cheapestExactlyKEdges(int n, int[][] edges, int src, int dst, int k) {
    final int INF = Integer.MAX_VALUE / 2;
    int[] prev = new int[n];
    Arrays.fill(prev, INF);
    prev[src] = 0;
    for (int t = 0; t < k; t++) {
        int[] cur = new int[n];
        Arrays.fill(cur, INF);                      // NO carry-over => "EXACTLY k edges"
        for (int[] e : edges) {
            if (prev[e[0]] < INF)
                cur[e[1]] = Math.min(cur[e[1]], prev[e[0]] + e[2]);
        }
        prev = cur;
    }
    return prev[dst] >= INF ? -1 : prev[dst];
}
```

```python
# python
# LC 787 - Cheapest Flights Within K Stops
# IDEA: layered DP form of Bellman-Ford — dp[t][v] = cheapest cost to reach v using at most t edges
# time = O(K * E), space = O(K * N) for the full table (O(N) if you keep only 2 rows)
def findCheapestPrice(n, flights, src, dst, k):
    INF = float('inf')
    dp = [[INF] * n for _ in range(k + 2)]
    dp[0][src] = 0

    for t in range(1, k + 2):
        dp[t] = dp[t - 1][:]                    # carry-over => "AT MOST t edges"
        for u, v, w in flights:
            if dp[t - 1][u] != INF:
                dp[t][v] = min(dp[t][v], dp[t - 1][u] + w)

    return -1 if dp[k + 1][dst] == INF else dp[k + 1][dst]


# Variant: shortest path using EXACTLY k edges — just drop the carry-over row
# time = O(K * E), space = O(N)
def cheapest_exactly_k_edges(n, edges, src, dst, k):
    INF = float('inf')
    prev = [INF] * n
    prev[src] = 0
    for _ in range(k):
        cur = [INF] * n                         # NO carry-over => "EXACTLY k edges"
        for u, v, w in edges:
            if prev[u] != INF:
                cur[v] = min(cur[v], prev[u] + w)
        prev = cur
    return -1 if prev[dst] == INF else prev[dst]
```

| 你要的 | 每列的初始化 | 答案 |
|------|--------------------|------|
| **最多 K 條邊** | `dp[t] = dp[t-1].clone()` / `dp[t-1][:]` | `dp[K][dst]` |
| **剛好 K 條邊** | `dp[t] = [INF] * n` | `dp[K][dst]` |
| **邊數不限** | 跑 `V-1` 輪，原地更新即可 | `dp[V-1][dst]` |

**🚫 陷阱**：在 Java 裡 `INF` 要用 `Integer.MAX_VALUE / 2`（不要用 `Integer.MAX_VALUE`），否則 `dp[u] + w` 會溢位成負數，導致每次鬆弛都「成功」。

### 2-2) Network Delay Time (LC 743) — 鬆弛 N-1 輪的 Bellman-Ford
> 把所有邊鬆弛 N-1 輪；訊號傳到所有節點的最短時間 = dist 陣列的最大值。

```java
// LC 743 - Network Delay Time
// IDEA: Bellman-Ford — relax all edges N-1 times; answer = max dist, -1 if any unreachable
// time = O(N * E), space = O(N)
public int networkDelayTime(int[][] times, int n, int k) {
    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;
    for (int i = 0; i < n - 1; i++)
        for (int[] t : times)
            if (dist[t[0]] != Integer.MAX_VALUE)
                dist[t[1]] = Math.min(dist[t[1]], dist[t[0]] + t[2]);
    int max = 0;
    for (int i = 1; i <= n; i++) {
        if (dist[i] == Integer.MAX_VALUE) return -1;
        max = Math.max(max, dist[i]);
    }
    return max;
}
```

```python
# LC 743 - Network Delay Time
# Can use Bellman-Ford but Dijkstra is more efficient

def networkDelayTime(times, n, k):
    """
    Bellman-Ford approach (Dijkstra is better for this problem)
    """
    # Initialize distances (1-indexed)
    dist = [float('inf')] * (n + 1)
    dist[k] = 0

    # Relax edges n-1 times
    for _ in range(n - 1):
        updated = False
        for u, v, w in times:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                updated = True

        if not updated:
            break

    # Find maximum delay
    max_delay = max(dist[1:])
    return max_delay if max_delay != float('inf') else -1
```

### 2-3) 貨幣套利偵測 — 用對數轉換的自訂 Bellman-Ford

```python
# Detect arbitrage opportunity in currency exchange
import math

def detect_arbitrage_opportunity(rates):
    """
    rates: 2D array where rates[i][j] = exchange rate from currency i to j
    Returns: True if arbitrage exists

    Example:
    rates = [
        [1, 0.5, 2.0],    # Currency 0: 1->1, 0.5->1, 2.0->2
        [2.0, 1, 0.25],   # Currency 1
        [0.5, 4.0, 1]     # Currency 2
    ]
    If 0 -> 1 -> 2 -> 0 gives > 1, arbitrage exists
    """
    n = len(rates)

    # Convert to negative log weights
    edges = []
    for i in range(n):
        for j in range(n):
            if i != j and rates[i][j] > 0:
                # -log(rate): if product > 1, sum of -logs < 0
                weight = -math.log(rates[i][j])
                edges.append((i, j, weight))

    # Bellman-Ford from any starting currency
    dist = [float('inf')] * n
    dist[0] = 0

    # Relax edges n-1 times
    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # Check for negative cycle (arbitrage)
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return True

    return False


# Example usage
def find_arbitrage_path(rates):
    """
    Not only detect but find the arbitrage path
    """
    n = len(rates)
    edges = []

    for i in range(n):
        for j in range(n):
            if i != j and rates[i][j] > 0:
                weight = -math.log(rates[i][j])
                edges.append((i, j, weight))

    dist = [0] * n  # Start with 0 (log(1) = 0)
    parent = [-1] * n

    # Relax edges
    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                parent[v] = u

    # Find node in negative cycle
    cycle_node = -1
    for u, v, w in edges:
        if dist[u] + w < dist[v]:
            cycle_node = v
            parent[v] = u
            break

    if cycle_node == -1:
        return None  # No arbitrage

    # Extract cycle
    visited = set()
    current = cycle_node
    while current not in visited:
        visited.add(current)
        current = parent[current]

    # Build cycle path
    path = [current]
    node = parent[current]
    while node != current:
        path.append(node)
        node = parent[node]
    path.reverse()

    return path
```

### 2-4) Minimum Cost to Reach Destination — 自訂的 Bellman-Ford 變形 — LC 1928

```python
def min_cost_with_discounts(n, roads, src, dst, discounts):
    """
    Find minimum cost where some roads have discounts (negative weights)
    roads: [(u, v, cost)]
    discounts: {(u, v): discount_amount}
    """
    # Apply discounts to create edges with potentially negative weights
    edges = []
    for u, v, cost in roads:
        actual_cost = cost - discounts.get((u, v), 0)
        edges.append((u, v, actual_cost))
        # If undirected
        actual_cost_rev = cost - discounts.get((v, u), 0)
        edges.append((v, u, actual_cost_rev))

    # Bellman-Ford
    dist = [float('inf')] * n
    dist[src] = 0

    for _ in range(n - 1):
        for u, v, w in edges:
            if dist[u] != float('inf') and dist[u] + w < dist[v]:
                dist[v] = dist[u] + w

    # Check for negative cycle
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            return "Infinite arbitrage possible"

    return dist[dst] if dist[dst] != float('inf') else -1
```

### 2-5) 時光旅行問題 — 理論上的負環偵測

```python
def shortest_path_with_time_machine(n, edges, src, dst):
    """
    Some edges go back in time (negative weight)
    Need to detect if infinite time travel loop exists

    edges: [(u, v, time_delta)]  # negative = go back in time
    """
    dist = [float('inf')] * n
    dist[src] = 0
    parent = [-1] * n

    # Bellman-Ford
    for _ in range(n - 1):
        for u, v, time in edges:
            if dist[u] != float('inf') and dist[u] + time < dist[v]:
                dist[v] = dist[u] + time
                parent[v] = u

    # Check if destination is affected by negative cycle
    affected = set()
    for u, v, time in edges:
        if dist[u] != float('inf') and dist[u] + time < dist[v]:
            # Mark all reachable nodes from v as affected
            affected.add(v)
            # BFS/DFS to find all reachable from v
            queue = [v]
            visited = {v}
            while queue:
                node = queue.pop(0)
                for u2, v2, _ in edges:
                    if u2 == node and v2 not in visited:
                        visited.add(v2)
                        queue.append(v2)
            affected.update(visited)

    if dst in affected:
        return "Can arrive arbitrarily early (time loop)"

    return dist[dst] if dist[dst] != float('inf') else "Unreachable"
```


## 依模式分類的題目

### **帶負權重的經典最短路徑**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Cheapest Flights Within K Stops | 787 | K 條邊的 Bellman-Ford | Medium |
| Network Delay Time | 743 | 基本 Bellman-Ford（Dijkstra 更好） | Medium |
| Minimum Cost to Reach Destination | 1928 | 帶限制的路徑 | Hard |
| Path with Maximum Probability | 1514 | 改寫權重 | Medium |

### **負環偵測**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Currency Arbitrage | N/A | 對數轉換 | Hard |
| Detect Cycle in Graph | N/A | 檢查第 V 輪 | Medium |
| Find Negative Weight Cycle | N/A | 記錄父節點指標 | Hard |

### **帶限制的路徑問題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Cheapest Flights K Stops | 787 | 限制迭代輪數 | Medium |
| Maximum Probability Path | 1514 | 改寫的 Bellman-Ford | Medium |
| Minimum Cost K Edges | N/A | 跑 K 輪 | Medium |

### **含負權重的圖上度量**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Evaluate Division | 399 | 帶權圖 | Medium |
| Accounts Merge | 721 | 併查集更好 | Medium |


## 決策框架

### 什麼時候該用 Bellman-Ford

✅ **這些情況用 Bellman-Ford：**
- 圖裡有負邊權
- 需要偵測負環
- 路徑最多只能用 K 條邊
- 貨幣兌換或套利問題
- 實作簡單比速度重要
- 在分散式系統上跑（可以平行化）
- 圖的結構常常變動（比較好更新）

❌ **這些情況別用 Bellman-Ford：**
- 權重全都非負（改用 Dijkstra）
- 小圖上要全點對最短路徑（改用 Floyd-Warshall）
- 圖非常大又稠密（太慢）
- 對即時效能要求高、又沒有負權重
- 圖沒有權重（改用 BFS）

### 這題真的是 Bellman-Ford 嗎？（標籤分流）

本文開頭連的 `graph` / `dynamic-programming` / `shortest-path` 清單裡有好幾百題，但**真正非用 Bellman-Ford 不可的 LC 題目非常少**。下面三個徵兆全部成立，你才該動用它：

1. **邊上有權重。** 沒有權重 → 單純的 BFS（[bfs.md](./bfs.md)）。
2. **權重可能是負的，或者路徑的邊數／跳數有上限。** 非負*而且*沒上限 → Dijkstra 嚴格來說更好（[Dijkstra.md](./Dijkstra.md)、[shortest_path_comparison.md](./shortest_path_comparison.md)）。
3. **要求的答案是最短／最便宜路徑，或是環的可行性判斷** — 不是計數、不是子序列、不是排序、也不是可達性。

如果只有第 3 點成立，你八成看的是另一個模式。以下是那幾個標籤頁上常見的高頻「長得很像」的題目：

| LC | 題目 | 為什麼看起來像 Bellman-Ford | 其實是什麼 |
|----|-------|--------------------------------|---------------------|
| 45 | Jump Game II | 「最少跳幾次」讀起來像限制邊數的最短路徑 | 所有權重都是 1 → 貪婪／BFS 分層只要 **O(n)**，而 BF 是 O(V·E) — [greedy.md](./greedy.md) |
| 279 | Perfect Squares | 隱式圖上的最少步數 | 無權重 BFS 或零錢兌換型 DP — [dp.md](./dp.md) |
| 207 | Course Schedule | 圖 +「偵測環」 | 拓撲排序（Kahn / DFS 著色）— [topology_sorting.md](./topology_sorting.md) |
| 1192 | Critical Connections in a Network | 以邊為主的圖掃描 | Tarjan 找橋（DFS low-link）— [graph.md](./graph.md) |
| 785 | Is Graph Bipartite? | 對所有邊做全域一致性檢查 | BFS/DFS 二著色 — [bfs.md](./bfs.md) |
| 133 | Clone Graph | 完整走訪整張圖 | DFS/BFS 加一個 visited map |
| 947 | Most Stones Removed with Same Row or Column | 隱式圖上的連通性 | 併查集 — [union_find.md](./union_find.md) |
| 332 | Reconstruct Itinerary | 「找出穿過整張圖的路徑」 | Hierholzer 尤拉路徑 — [graph.md](./graph.md) |
| 753 | Cracking the Safe | 覆蓋整張圖的最短字串 | de Bruijn 序列／尤拉迴路 |
| 53 | Maximum Subarray | 一路「鬆弛目前最佳解」的 DP | Kadane — [kadane_algorithm.md](./kadane_algorithm.md) |

**面試金句** — LC 45 是最值得準備的對比：它*確實*是一個最少邊數的最短路徑問題，所以 Bellman-Ford 是對的，但因為每條邊權重都是 1，BFS 分層／貪婪掃描只要 O(n) 就解掉。把兩個都講出來、然後挑便宜的那個，展現的是你「做了選擇」而不是「用預設值」。

### 實作檢查清單

```python
# Bellman-Ford Implementation Checklist:

# 1. Initialize distances
dist = [float('inf')] * n
dist[src] = 0

# 2. Relax all edges V-1 times
for iteration in range(n - 1):
    updated = False
    for u, v, w in edges:
        if dist[u] != float('inf') and dist[u] + w < dist[v]:
            dist[v] = dist[u] + w
            updated = True

    # Early termination optimization
    if not updated:
        break

# 3. Check for negative cycles (V-th iteration)
has_negative_cycle = False
for u, v, w in edges:
    if dist[u] != float('inf') and dist[u] + w < dist[v]:
        has_negative_cycle = True
        break

# 4. Handle results
if has_negative_cycle:
    return None  # or handle appropriately
return dist
```

### 常見最佳化

1. **提早結束**
   ```python
   # If no updates in an iteration, done early
   if not updated:
       break
   ```

2. **SPFA（用佇列）**
   ```python
   # Only process vertices that had updates
   # Average O(E), worst O(V·E)
   ```

3. **雙向搜尋**
   ```python
   # Run from both src and dst simultaneously
   # Can reduce iterations by half
   ```

4. **限制迭代輪數**
   ```python
   # For K-edge constraint, stop after K iterations
   for i in range(min(k, n-1)):
   ```


## 總結與速查

### 時間／空間複雜度

| 面向 | 複雜度 | 備註 |
|--------|------------|-------|
| 時間（標準） | O(V·E) | 對 E 條邊跑 V-1 輪 |
| 時間（SPFA 平均） | O(E) | 佇列最佳化後的平均情況 |
| 時間（SPFA 最壞） | O(V·E) | 退化回標準版 |
| 空間 | O(V) | 距離陣列 + 父節點陣列 |
| 環偵測 | +O(E) | 多跑的第 V 輪 |

### 演算法核心結構

```text
# Standard Bellman-Ford Pattern
dist[src] = 0
for _ in range(V - 1):                    # V-1 iterations
    for each edge (u, v, w):              # All edges
        if dist[u] + w < dist[v]:         # Relaxation
            dist[v] = dist[u] + w

# Negative cycle check
for each edge (u, v, w):
    if dist[u] + w < dist[v]:
        return "Negative cycle exists"
```

### 跟其他演算法的關鍵差異

| 面向 | Bellman-Ford | Dijkstra | Floyd-Warshall |
|--------|--------------|----------|----------------|
| **邊的鬆弛** | 所有邊，跑 V-1 次 | 只處理距離最短的節點 | 透過中介點處理所有點對 |
| **資料結構** | 單純的陣列 | 優先佇列 | 二維矩陣 |
| **順序重要嗎** | 不重要（所有邊都鬆弛） | 重要（貪婪挑選） | 重要（k 迴圈要放最外層） |
| **可平行化** | ✅ 可以（同一輪之內） | ❌ 不行（本質是序列的） | ✅ 可以（要改寫） |

### 常見模式與技巧

#### **模式 1：負環偵測**
```python
# After V-1 iterations, one more check
for u, v, w in edges:
    if dist[u] + w < dist[v]:
        return "Has negative cycle"
```

#### **模式 2：K 條邊的限制**
```python
# Use temp array to ensure using previous iteration values
for _ in range(k):
    temp = dist.copy()
    for u, v, w in edges:
        temp[v] = min(temp[v], dist[u] + w)
    dist = temp
```

#### **模式 3：路徑重建**
```python
parent = [-1] * n
# During relaxation:
if dist[u] + w < dist[v]:
    dist[v] = dist[u] + w
    parent[v] = u

# Reconstruct path
path = []
while current != -1:
    path.append(current)
    current = parent[current]
path.reverse()
```

#### **模式 4：套利偵測**
```python
# Convert multiplicative to additive
weight = -math.log(exchange_rate)
# Negative cycle = arbitrage opportunity
```

#### **模式 5：提早結束**
```python
for _ in range(n - 1):
    updated = False
    for u, v, w in edges:
        if relax(u, v, w):
            updated = True
    if not updated:
        break  # No more updates possible
```

### 常見錯誤與提示

**🚫 常見錯誤：**
- 明明 Dijkstra 就能解卻用 Bellman-Ford（白白浪費時間）
- 鬆弛前忘記檢查 `dist[u] != inf`
- 做 K 條邊限制的題目時沒有用暫存陣列
- 迭代輪數搞錯（標準版應該是 V-1）
- 沒處理不連通的分量
- 忘了加上提早結束的最佳化

**✅ 最佳實務：**
- 先確認負權重是不是真的存在
- 稀疏圖上用 SPFA 換取更好的平均效能
- 實作提早結束來提升效率
- K 條邊的題目用暫存陣列，避免錯誤的更新
- 比較時小心處理無限大的值
- 沒有負權重就先考慮 Dijkstra
- 需要全點對時，先跟 Floyd-Warshall 的成本比一比

### 面試提示

1. **什麼時候該把 Bellman-Ford 講出來**：
   - 「有負邊權嗎？」→ 有的話就提 Bellman-Ford
   - 「需要偵測負環嗎？」→ 那答案就是 Bellman-Ford
   - 「路徑最多 K 條邊？」→ 改寫過的 Bellman-Ford

2. **複雜度的討論**：
   - 一開始就把 O(V·E) 講清楚
   - 主動說明非負權重下它比 Dijkstra 慢
   - 稀疏圖上把 SPFA 當成最佳化手段提出來

3. **實作上的說明**：
   - 比 Dijkstra 好寫（不用優先佇列）
   - 要加限制（K 條邊）很容易改
   - 多跑一輪就能偵測負環

4. **其他解法**：
   - 沒有負權重 → 「Dijkstra 會更快」
   - 需要全點對 → 「Floyd-Warshall 可能更單純」
   - 圖非常大 → 「建議用 SPFA 最佳化」

5. **值得討論的邊界情況**：
   - 不連通的分量
   - 負環（怎麼處理）
   - 某些頂點從起點根本到不了
   - 同一對頂點之間有多條邊

### 相關演算法

- **[Dijkstra](./Dijkstra.md)**：更快的單源解法，但不吃負權重
- **[Floyd-Warshall](./Floyd-Warshall.md)**：全點對，可處理負權重
- **SPFA**：用佇列最佳化的 Bellman-Ford 變形
- **Johnson's Algorithm**：重新配權 + Dijkstra，用於全點對
- **Yen's Algorithm**：前 K 短路徑
- **Eppstein's Algorithm**：前 K 短路徑（更快）

### 快速決策矩陣

| 你的情況 | 選擇 |
|----------------|--------|
| 單源、沒有負權重 | **Dijkstra** ⚡ |
| 單源、有負權重 | **Bellman-Ford** ✅ |
| 需要偵測負環 | **Bellman-Ford** ✅ |
| 最多 K 條邊／K 次跳躍 | **Bellman-Ford（跑 K 輪）** ✅ |
| 全點對、小圖 | **Floyd-Warshall** |
| 全點對、大圖 | **跑 V 次 Dijkstra** 或 **Johnson's** |
| 無權重的圖 | **BFS** ⚡⚡ |
| 貨幣套利 | **Bellman-Ford** ✅ |
| 即時導航 | **Dijkstra** 或 **A*** ⚡ |
