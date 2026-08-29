# Floyd-Warshall Algorithm

> **範圍** — 用「中繼頂點」做 DP 求**全點對**最短路徑 — O(V³)、稠密圖、遞移閉包。
> **另見**：[shortest_path_comparison.md](./shortest_path_comparison.md) — 該挑哪個演算法；[Dijkstra.md](./Dijkstra.md) — 單源、稀疏圖；[Bellman-Ford.md](./Bellman-Ford.md) — 單源、負權重。

## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## 總覽
**Floyd-Warshall 演算法**是一個用動態規劃解全點對最短路徑的方法。它會算出加權圖裡所有頂點兩兩之間的最短路徑，就算有負權重的邊也行（但不能有負環）。

### 關鍵性質
- **時間複雜度**：O(V³)，V 是頂點數
- **空間複雜度**：O(V²)，用來存距離矩陣
- **核心想法**：以中繼頂點為維度的動態規劃
- **什麼時候用**：全點對最短路徑，而且可以處理負權重
- **限制**：`Cannot` 處理 `negative cycles`（偵測得到，但算不出正確答案）

### 核心特徵
- **動態規劃**：一個一個放入中繼頂點，逐步把解建起來
- **矩陣為基礎**：使用相鄰矩陣表示法
- **實作簡單**：三層巢狀迴圈
- **用途廣**：支援負權重、可偵測負環
- **路徑重建**：搭配前驅矩陣就能還原路徑

### 參考資料
- [Floyd-Warshall Visualization](https://www.cs.usfca.edu/~galles/visualization/Floyd.html)
- [CP Algorithms - Floyd-Warshall](https://cp-algorithms.com/graph/all-pair-shortest-path-floyd-warshall.html)
- [Dijkstra Cheatsheet](./Dijkstra.md) - 拿來跟單源版本比較
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md) - 單源且允許負權重的版本


## 題型分類

### **類型 1：經典全點對最短路徑**
- **說明**：求出所有頂點兩兩之間的最短路徑
- **例題**：LC 1334（Find City with Smallest Number）、LC 1462（Course Schedule IV）
- **模式**：Floyd-Warshall 直接套用

### **類型 2：遞移閉包**
- **說明**：判斷任兩個頂點之間能不能到達
- **例題**：LC 1462（Course Schedule IV）、各種圖連通性問題
- **模式**：Floyd-Warshall 的布林版本

### **類型 3：負環偵測**
- **說明**：判斷圖裡有沒有負環
- **例題**：套利偵測、負權重環
- **模式**：跑完 Floyd-Warshall 之後檢查對角線

### **類型 4：Minimax／Maximin 路徑**
- **說明**：找出「最大邊最小」或「最小邊最大」的路徑
- **例題**：LC 1334（門檻類問題）、瓶頸最短路徑
- **模式**：把 Floyd-Warshall 的運算換掉

### **類型 5：圖的直徑與各種度量**
- **說明**：求最長的最短路徑、圖心、半徑
- **例題**：網路直徑、圖的離心率
- **模式**：對 Floyd-Warshall 的結果做後處理


## 模板與演算法

### 模板比較表
| 模板類型 | 使用情境 | 運算 | 什麼時候用 |
|---------------|----------|-----------|-------------|
| **基本 Floyd-Warshall** | 全點對最短路徑 | min(dist[i][j], dist[i][k]+dist[k][j]) | 一般最短路徑 |
| **遞移閉包** | 可達性 | dist[i][j] OR (dist[i][k] AND dist[k][j]) | 布林連通性 |
| **Minimax 路徑** | 瓶頸路徑 | min(dist[i][j], max(dist[i][k], dist[k][j])) | 容量／頻寬 |
| **路徑重建** | 追出實際路徑 | 前驅矩陣 | 需要真正的路徑 |
| **負環** | 偵測環 | 檢查 dist[i][i] < 0 | 套利、環偵測 |

### 模板 1：基本 Floyd-Warshall
```python
def floyd_warshall(n, edges):
    """
    Find shortest paths between all pairs of vertices
    n: number of vertices (0-indexed)
    edges: list of (u, v, weight)
    Returns: distance matrix
    """
    # Initialize distance matrix
    dist = [[float('inf')] * n for _ in range(n)]

    # Distance from vertex to itself is 0
    for i in range(n):
        dist[i][i] = 0

    # Add edges
    for u, v, w in edges:
        dist[u][v] = w
        # For undirected graph, add reverse edge:
        # dist[v][u] = w

    # Floyd-Warshall: try all intermediate vertices
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dist[i][k] + dist[k][j] < dist[i][j]:
                    dist[i][j] = dist[i][k] + dist[k][j]

    return dist
```

### 模板 2：Floyd-Warshall + 路徑重建
```python
def floyd_warshall_with_path(n, edges):
    """
    Find shortest paths and reconstruct actual paths
    Returns: (distance matrix, next vertex matrix)
    """
    dist = [[float('inf')] * n for _ in range(n)]
    next_vertex = [[None] * n for _ in range(n)]

    # Initialize
    for i in range(n):
        dist[i][i] = 0
        next_vertex[i][i] = i

    for u, v, w in edges:
        dist[u][v] = w
        next_vertex[u][v] = v

    # Floyd-Warshall
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dist[i][k] + dist[k][j] < dist[i][j]:
                    dist[i][j] = dist[i][k] + dist[k][j]
                    next_vertex[i][j] = next_vertex[i][k]

    return dist, next_vertex

def reconstruct_path(next_vertex, u, v):
    """Reconstruct path from u to v"""
    if next_vertex[u][v] is None:
        return []

    path = [u]
    while u != v:
        u = next_vertex[u][v]
        path.append(u)
    return path
```

### 模板 3：遞移閉包（可達性）
```python
def transitive_closure(n, edges):
    """
    Determine if there's a path between every pair of vertices
    Returns: boolean reachability matrix
    """
    reach = [[False] * n for _ in range(n)]

    # Initialize: vertex can reach itself
    for i in range(n):
        reach[i][i] = True

    # Mark direct edges
    for u, v in edges:
        reach[u][v] = True

    # Floyd-Warshall for reachability
    for k in range(n):
        for i in range(n):
            for j in range(n):
                reach[i][j] = reach[i][j] or (reach[i][k] and reach[k][j])

    return reach
```

### 模板 4：負環偵測
```python
def detect_negative_cycle(n, edges):
    """
    Detect if graph contains negative cycle
    Returns: (has_negative_cycle, distance_matrix)
    """
    dist = [[float('inf')] * n for _ in range(n)]

    for i in range(n):
        dist[i][i] = 0

    for u, v, w in edges:
        dist[u][v] = w

    # Floyd-Warshall
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if dist[i][k] + dist[k][j] < dist[i][j]:
                    dist[i][j] = dist[i][k] + dist[k][j]

    # Check diagonal for negative values
    has_negative_cycle = any(dist[i][i] < 0 for i in range(n))

    return has_negative_cycle, dist
```

### 模板 5：Minimax 路徑（瓶頸）
```python
def floyd_warshall_minimax(n, edges):
    """
    Find path that minimizes the maximum edge weight
    Useful for capacity/bandwidth problems
    """
    # Initialize with infinity (no path)
    dist = [[float('inf')] * n for _ in range(n)]

    for i in range(n):
        dist[i][i] = 0

    for u, v, w in edges:
        dist[u][v] = w

    # Floyd-Warshall with minimax operation
    for k in range(n):
        for i in range(n):
            for j in range(n):
                # Minimize the maximum edge on path
                dist[i][j] = min(dist[i][j], max(dist[i][k], dist[k][j]))

    return dist
```

### 模板 6：省空間版本
```python
def floyd_warshall_optimized(n, edges):
    """
    Space-optimized: use single matrix (in-place update)
    """
    dist = [[float('inf')] * n for _ in range(n)]

    for i in range(n):
        dist[i][i] = 0

    for u, v, w in edges:
        dist[u][v] = min(dist[u][v], w)  # Handle multiple edges

    # In-place updates are safe due to intermediate vertex property
    for k in range(n):
        for i in range(n):
            for j in range(n):
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

    return dist
```

## 演算法比較

### Floyd-Warshall vs Dijkstra vs Bellman-Ford

| 特性 | Floyd-Warshall | Dijkstra | Bellman-Ford |
|---------|----------------|----------|--------------|
| **問題類型** | 全點對最短路徑 | 單源最短路徑 | 單源最短路徑 |
| **時間複雜度** | O(V³) | O((V+E) log V) | O(V·E) |
| **空間複雜度** | O(V²) | O(V) | O(V) |
| **負權重** | ✅ 可以 | ❌ 不行 | ✅ 可以 |
| **負環** | 偵測得到 | N/A | 偵測得到 |
| **實作難度** | 非常簡單（3 層迴圈） | 中等（要用優先佇列） | 簡單（2 層迴圈） |
| **適合的圖** | 偏好稠密圖 | 偏好稀疏圖 | 都可以 |
| **輸出** | 全點對距離 | 單源距離 | 單源距離 |
| **最佳使用情境** | 小圖、要全點對 | 大型稀疏圖 | 負權重、環偵測 |

### 什麼情況該用哪個演算法

```text
Algorithm Selection Flowchart:

1. Need all-pairs shortest paths?
   ├── YES → Consider Floyd-Warshall
   │   ├── Small graph (V ≤ 400)? → Use Floyd-Warshall
   │   └── Large graph? → Run Dijkstra/Bellman-Ford V times
   └── NO → Single-source problem → Continue to 2

2. Are there negative edge weights?
   ├── YES → Use Bellman-Ford (or SPFA)
   └── NO → Use Dijkstra

3. Is graph dense (E ≈ V²)?
   ├── YES → Consider Floyd-Warshall for all-pairs
   └── NO → Dijkstra is more efficient
```

### 實務比較表

| 情境 | 最佳演算法 | 理由 |
|----------|----------------|--------|
| 小型完全圖、要全點對 | Floyd-Warshall | O(V³) 可以接受，程式碼又短 |
| 大型稀疏圖、單源 | Dijkstra | O((V+E) log V) 快非常多 |
| 有負權重、單源 | Bellman-Ford | 只有它處理得了 |
| 遞移閉包 | Floyd-Warshall | DP 寫法最自然 |
| 格子上的最短路徑 | Dijkstra | 圖是隱式的，而且稀疏 |
| 網路直徑 | Floyd-Warshall | 反正本來就需要全點對 |
| 帶限制條件的路徑 | Dijkstra（改造版） | 狀態要怎麼帶都很彈性 |
| 套利偵測 | Floyd-Warshall | 需要環偵測，而且要全點對 |

### 複雜度實例比較

以 V=1000 個頂點、E=5000 條邊的圖為例：

| 演算法 | 運算次數 | 相對速度 |
|-----------|------------|----------------|
| Floyd-Warshall | 1,000,000,000 | 基準（最慢） |
| Dijkstra（跑 V 次） | ~50,000 × log(1000) × 1000 | 快約 20 倍 |
| Dijkstra（單次） | ~5,000 × log(1000) | 快約 20,000 倍 |
| Bellman-Ford | 1000 × 5000 = 5,000,000 | 快約 200 倍 |


## LC 範例

### 2-1) Find the City With the Smallest Number of Neighbors (LC 1334) — Floyd-Warshall 全點對
> 先跑 Floyd-Warshall；再對每座城市算出門檻內可達的城市數；回傳最少的那座（平手取索引較大者）。

```java
// LC 1334 - Find the City With the Smallest Number of Neighbors at a Threshold Distance
// IDEA: Floyd-Warshall all-pairs shortest path; count reachable per city within threshold
// time = O(N^3), space = O(N^2)
public int findTheCity(int n, int[][] edges, int distanceThreshold) {
    int[][] dist = new int[n][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE / 2);
    for (int i = 0; i < n; i++) dist[i][i] = 0;
    for (int[] e : edges) { dist[e[0]][e[1]] = e[2]; dist[e[1]][e[0]] = e[2]; }
    for (int k = 0; k < n; k++)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
    int ans = -1, minCount = n;
    for (int i = 0; i < n; i++) {
        int count = 0;
        for (int j = 0; j < n; j++) if (i != j && dist[i][j] <= distanceThreshold) count++;
        if (count <= minCount) { minCount = count; ans = i; }
    }
    return ans;
}
```

```python
# LC 1334 - Find the City With the Smallest Number of Neighbors at a Threshold Distance
# Classic Floyd-Warshall application

def findTheCity(n, edges, distanceThreshold):
    """
    Find city with smallest number of reachable cities within threshold
    """
    # Initialize distance matrix
    dist = [[float('inf')] * n for _ in range(n)]

    for i in range(n):
        dist[i][i] = 0

    for u, v, w in edges:
        dist[u][v] = w
        dist[v][u] = w  # Undirected graph

    # Floyd-Warshall
    for k in range(n):
        for i in range(n):
            for j in range(n):
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

    # Count reachable cities for each city
    min_reachable = float('inf')
    result_city = -1

    for i in range(n):
        reachable = sum(1 for j in range(n) if i != j and dist[i][j] <= distanceThreshold)
        if reachable <= min_reachable:
            min_reachable = reachable
            result_city = i

    return result_city
```

### 2-2) Course Schedule IV (LC 1462) — Floyd-Warshall 遞移閉包
> 用布林可達性矩陣；如果 i 是 j 的先修（直接或間接），reachable[i][j] = true。

```java
// LC 1462 - Course Schedule IV
// IDEA: Floyd-Warshall transitive closure; reachable[i][j] = i is prerequisite of j
// time = O(N^3), space = O(N^2)
public List<Boolean> checkIfPrerequisite(int numCourses, int[][] prerequisites, int[][] queries) {
    boolean[][] reach = new boolean[numCourses][numCourses];
    for (int[] p : prerequisites) reach[p[0]][p[1]] = true;
    for (int k = 0; k < numCourses; k++)
        for (int i = 0; i < numCourses; i++)
            for (int j = 0; j < numCourses; j++)
                if (reach[i][k] && reach[k][j]) reach[i][j] = true;
    List<Boolean> ans = new ArrayList<>();
    for (int[] q : queries) ans.add(reach[q[0]][q[1]]);
    return ans;
}
```

```python
# LC 1462 - Course Schedule IV
# Transitive closure problem

def checkIfPrerequisite(numCourses, prerequisites, queries):
    """
    Determine if course A is a prerequisite of course B (direct or indirect)
    """
    n = numCourses
    # is_prereq[i][j] = True if i is prerequisite of j
    is_prereq = [[False] * n for _ in range(n)]

    # Mark direct prerequisites
    for pre, course in prerequisites:
        is_prereq[pre][course] = True

    # Floyd-Warshall for transitive closure
    for k in range(n):
        for i in range(n):
            for j in range(n):
                if is_prereq[i][k] and is_prereq[k][j]:
                    is_prereq[i][j] = True

    # Answer queries
    return [is_prereq[u][v] for u, v in queries]
```

### 2-3) Network Delay Time Alternative Solution (LC 743) — Floyd-Warshall 全點對
> 算出全點對距離；答案就是從來源 k 出發的最大距離（比起 Dijkstra 是殺雞用牛刀，但答案正確）。

```java
// LC 743 - Network Delay Time (Floyd-Warshall approach)
// IDEA: All-pairs Floyd-Warshall; answer = max dist[k-1][i] for all i
// time = O(N^3), space = O(N^2)
public int networkDelayTime(int[][] times, int n, int k) {
    int[][] dist = new int[n][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE / 2);
    for (int i = 0; i < n; i++) dist[i][i] = 0;
    for (int[] t : times) dist[t[0]-1][t[1]-1] = t[2];
    for (int mid = 0; mid < n; mid++)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                dist[i][j] = Math.min(dist[i][j], dist[i][mid] + dist[mid][j]);
    int max = 0;
    for (int i = 0; i < n; i++) {
        if (dist[k-1][i] == Integer.MAX_VALUE / 2) return -1;
        max = Math.max(max, dist[k-1][i]);
    }
    return max;
}
```

```python
# LC 743 - Network Delay Time
# Can use Floyd-Warshall but Dijkstra is more efficient

def networkDelayTime(times, n, k):
    """
    Floyd-Warshall approach (overkill for single-source)
    """
    dist = [[float('inf')] * n for _ in range(n)]

    for i in range(n):
        dist[i][i] = 0

    for u, v, w in times:
        dist[u-1][v-1] = w  # Convert to 0-indexed

    # Floyd-Warshall
    for mid in range(n):
        for i in range(n):
            for j in range(n):
                dist[i][j] = min(dist[i][j], dist[i][mid] + dist[mid][j])

    # Find max distance from source k-1
    k_idx = k - 1
    max_dist = max(dist[k_idx])

    return max_dist if max_dist != float('inf') else -1
```

### 2-4) Graph Connectivity With Threshold (LC 1627) — Floyd-Warshall 連通性
> 對所有 GCD > threshold 的組合建邊；再用 Floyd-Warshall 遞移閉包回答詢問。

```java
// LC 1627 - Graph Connectivity With Threshold
// IDEA: Connect multiples of each gcd > threshold; Floyd-Warshall for connectivity queries
// time = O(N^2 log N + N^3 + Q), space = O(N^2)
public List<Boolean> areConnected(int n, int threshold, int[][] queries) {
    boolean[][] conn = new boolean[n + 1][n + 1];
    for (int i = 0; i <= n; i++) conn[i][i] = true;
    for (int g = threshold + 1; g <= n; g++)
        for (int mul = 2 * g; mul <= n; mul += g)
            conn[g][mul] = conn[mul][g] = true;
    for (int k = 1; k <= n; k++)
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                if (conn[i][k] && conn[k][j]) conn[i][j] = true;
    List<Boolean> ans = new ArrayList<>();
    for (int[] q : queries) ans.add(conn[q[0]][q[1]]);
    return ans;
}
```

```python
# LC 1627 - Graph Connectivity With Threshold
# Union-Find is better, but Floyd-Warshall works

def areConnected(n, threshold, queries):
    """
    Determine if cities are connected via intermediate cities > threshold
    """
    # Build graph: cities connected if gcd > threshold
    edges = []
    for gcd_val in range(threshold + 1, n + 1):
        # All multiples of gcd_val are connected
        multiples = list(range(gcd_val, n + 1, gcd_val))
        for i in range(len(multiples) - 1):
            edges.append((multiples[i], multiples[i + 1]))

    # Floyd-Warshall for connectivity
    connected = [[False] * (n + 1) for _ in range(n + 1)]

    for i in range(n + 1):
        connected[i][i] = True

    for u, v in edges:
        connected[u][v] = connected[v][u] = True

    for k in range(1, n + 1):
        for i in range(1, n + 1):
            for j in range(1, n + 1):
                if connected[i][k] and connected[k][j]:
                    connected[i][j] = True

    return [connected[u][v] for u, v in queries]
```

### 2-5) Shortest Path Visiting All Nodes (LC 847) — BFS + 位元遮罩（Floyd-Warshall 預處理）
> 以 (node, visitedMask) 為狀態做 BFS；有需要的話先用 Floyd-Warshall 預先算好兩兩距離。

```java
// LC 847 - Shortest Path Visiting All Nodes
// IDEA: BFS with bitmask state (node, visited); all nodes are valid starts
// time = O(2^N * N), space = O(2^N * N)
public int shortestPathLength(int[][] graph) {
    int n = graph.length, full = (1 << n) - 1;
    Queue<int[]> q = new LinkedList<>();
    boolean[][] visited = new boolean[n][1 << n];
    for (int i = 0; i < n; i++) { q.offer(new int[]{i, 1 << i, 0}); visited[i][1 << i] = true; }
    while (!q.isEmpty()) {
        int[] cur = q.poll();
        int node = cur[0], mask = cur[1], dist = cur[2];
        if (mask == full) return dist;
        for (int next : graph[node]) {
            int nextMask = mask | (1 << next);
            if (!visited[next][nextMask]) { visited[next][nextMask] = true; q.offer(new int[]{next, nextMask, dist + 1}); }
        }
    }
    return -1;
}
```

```python
# LC 847 - Shortest Path Visiting All Nodes
# Use BFS with bitmask, but Floyd-Warshall for preprocessing

def shortestPathLength(graph):
    """
    Floyd-Warshall to precompute all-pairs distances,
    then use DP/BFS to find shortest path visiting all nodes
    """
    n = len(graph)

    # Build distance matrix using Floyd-Warshall
    dist = [[float('inf')] * n for _ in range(n)]
    for i in range(n):
        dist[i][i] = 0
        for j in graph[i]:
            dist[i][j] = 1

    for k in range(n):
        for i in range(n):
            for j in range(n):
                dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

    # Now use BFS with bitmask (actual solution)
    # ... (rest of solution uses dist matrix)

    # Simplified return for template
    return 0
```

### 2-6) Cheapest Flights Within K Stops (LC 787) — Min-Plus 矩陣次方 ⭐⭐⭐⭐⭐
> `k-i-j` 迴圈順序的規則，在這題被講得最具體。**這題直接套 Floyd-Warshall 是錯的**：它最外層的 `k` 是*中繼頂點*，不是*跳數*，所以三層迴圈跑完之後，`dist[src][dst]` 是完全不受限的最短路徑，根本沒記錄用了幾條邊。Floyd-Warshall *家族*的解法是改變第三層迴圈的意義 — 把 `k` 移到內層，就變成 **min-plus 矩陣乘法**，而它的次方數剛好就在數邊。

| | 迴圈順序 | `k` 代表什麼 | 結果 |
|---|---|---|---|
| **Floyd-Warshall** | `k` 在**最外層**，接著 `i`、`j` | 目前允許使用的中繼頂點集合 | 不受限的全點對最短路徑（邊數沒有上限） |
| **Min-plus 乘積** | `i`，接著 `k`，最後 `j`（`k` 在內層） | 把兩半接起來的那一個銜接頂點 | `C = A ⊗ B`：A 的跳數預算**加上** B 的跳數預算 |

**核心想法**：定義 `(A ⊗ B)[i][j] = min over k of (A[i][k] + B[k][j])` — 就是普通的矩陣乘法，把 `(+, ×)` 換成 `(min, +)`。這個乘積具**結合律**，所以可以用反覆平方法做次方。

令 `M[i][j]` = `i → j` 最便宜的單一航班，並且設定 **`M[i][i] = 0`** — 這個「原地不動」的自環，正是把*剛好 t 條邊*變成*最多 t 條邊*的關鍵。於是 `M^t[i][j]` = `i → j` 最多用 **`t` 個航班**的最低票價。LC 787 允許 `K` 次轉機 = `K + 1` 個航班，所以答案是 `(M^(K+1))[src][dst]`。

```java
// LC 787 - Cheapest Flights Within K Stops
// IDEA: min-plus matrix power. M[i][i]=0 makes "exactly t edges" into "at most t edges",
//       so answer = (M ^ (K+1))[src][dst]. Note k is the INNER loop here, unlike Floyd-Warshall.
// time = O(V^3 log K), space = O(V^2)
static final int INF = Integer.MAX_VALUE / 3;   // /3 so INF + INF never overflows

// C[i][j] = min over k of (A[i][k] + B[k][j])
private int[][] minPlus(int[][] A, int[][] B) {
    int n = A.length;
    int[][] C = new int[n][n];
    for (int[] row : C) Arrays.fill(row, INF);
    for (int i = 0; i < n; i++)
        for (int k = 0; k < n; k++) {          // k is INNER, not outer -> counts edges, not vertices
            if (A[i][k] >= INF) continue;      // prune: no route i -> k yet
            for (int j = 0; j < n; j++)
                C[i][j] = Math.min(C[i][j], A[i][k] + B[k][j]);
        }
    return C;
}

public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    // M = one-flight cost matrix; diagonal 0 == "take fewer flights than allowed"
    int[][] M = new int[n][n];
    for (int[] row : M) Arrays.fill(row, INF);
    for (int i = 0; i < n; i++) M[i][i] = 0;
    for (int[] f : flights) M[f[0]][f[1]] = Math.min(M[f[0]][f[1]], f[2]);

    // res = identity of the min-plus semiring (0 on diagonal, INF elsewhere)
    int[][] res = new int[n][n];
    for (int i = 0; i < n; i++) { Arrays.fill(res[i], INF); res[i][i] = 0; }

    int e = k + 1;                              // K stops == K+1 flights
    while (e > 0) {
        if ((e & 1) == 1) res = minPlus(res, M);
        M = minPlus(M, M);
        e >>= 1;
    }
    return res[src][dst] >= INF ? -1 : res[src][dst];
}
```

```python
# LC 787 - Cheapest Flights Within K Stops
# IDEA: min-plus matrix power. M[i][i]=0 makes "exactly t edges" into "at most t edges",
#       so answer = (M ** (K+1))[src][dst]. Note k is the INNER loop here, unlike Floyd-Warshall.
# time = O(V^3 log K), space = O(V^2)
INF = float('inf')

def min_plus(A, B, n):
    """C[i][j] = min over k of (A[i][k] + B[k][j])  -- (min, +) instead of (+, *)"""
    C = [[INF] * n for _ in range(n)]
    for i in range(n):
        Ai, Ci = A[i], C[i]
        for k in range(n):              # k is INNER, not outer -> counts edges, not vertices
            a = Ai[k]
            if a == INF:                # prune: no route i -> k yet
                continue
            Bk = B[k]
            for j in range(n):
                if a + Bk[j] < Ci[j]:
                    Ci[j] = a + Bk[j]
    return C

def findCheapestPrice(n, flights, src, dst, k):
    # M = one-flight cost matrix; diagonal 0 == "take fewer flights than allowed"
    M = [[INF] * n for _ in range(n)]
    for i in range(n):
        M[i][i] = 0
    for u, v, w in flights:
        M[u][v] = min(M[u][v], w)

    # res = identity of the min-plus semiring (0 on diagonal, INF elsewhere)
    res = [[0 if i == j else INF for j in range(n)] for i in range(n)]

    e = k + 1                            # K stops == K+1 flights
    while e:
        if e & 1:
            res = min_plus(res, M, n)
        M = min_plus(M, M, n)
        e >>= 1

    return -1 if res[src][dst] == INF else res[src][dst]
```

**⚠️ 溢位防護**：在 Java 裡 `INF` 要用 `Integer.MAX_VALUE / 3`（不要用 `MAX_VALUE`）— `minPlus` 會把兩個都可能是 `INF` 的項加起來，而 `MAX_VALUE + MAX_VALUE` 會繞回負數，變成一條假的「最短」路徑。

**面試現實**：以 LC 787 實際的限制（`n ≤ 100`、`k ≤ 100`）來說，分層的 O(K·E) 鬆弛更簡單也更快 — 你真正會寫的那兩種解法，請看 [Bellman-Ford](./Bellman-Ford.md) §2-1 和 [Dijkstra](./Dijkstra.md) §2-2。只有在**跳數預算 `K` 大到誇張（10⁹）而 `V` 又很小**的時候，才輪到 min-plus 次方上場，因為這時 `log K` 打得贏任何逐跳的迴圈。面試的加分點，是把它當成追問（「那如果 K 是十億呢？」）的答案講出來。

### 尺寸檢查：`n` 夠小到能跑 O(n³) 嗎？

決定用 Floyd-Warshall 之前先看限制 — 三次方是不留情面的。

| `n` | `n³` | 判定 |
|-----|------|---------|
| ≤ 100 | 10⁶ | 完全沒問題（LC 787、LC 1462 都在這一格） |
| ≤ 200 | 8 × 10⁶ | 沒問題（LC 1334 上限是 100） |
| ≤ 500 | 1.25 × 10⁸ | 邊緣 — Java/C++ 可以，Python 很危險 |
| ≤ 1000 | 10⁹ | 太慢了 — 改成從每個來源各跑一次 Dijkstra |
| > 1000 | ≥ 10⁹ | 這根本不是全點對問題，回去重讀題目 |

**經驗法則**：如果頂點數的限制寫的是 `n ≤ 幾百`，**而且**題目問的是很多組不同的 `(u, v)`，那出題者幾乎可以確定就是在請你用 Floyd-Warshall。


## 依模式分類的題目

### **全點對最短路徑類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Find the City With Smallest Number | 1334 | 直接套 Floyd-Warshall | Medium |
| Network Delay Time | 743 | 殺雞用牛刀，但可行 | Medium |
| Minimum Weighted Subgraph | 2203 | 三個來源 | Hard |
| Shortest Path in Undirected Graph | 1976 | 全點對距離 | Medium |

### **遞移閉包類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Course Schedule IV | 1462 | 布林版 Floyd-Warshall | Medium |
| Graph Connectivity | 1627 | 可達性矩陣 | Hard |
| Evaluate Division | 399 | 帶權重的遞移閉包 | Medium |

### **Minimax／Maximin 類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Path With Minimum Effort | 1631 | 改造版 Floyd-Warshall | Medium |
| Swim in Rising Water | 778 | Minimax 路徑 | Hard |
| Minimum Score of a Path | 2492 | 換掉運算子 | Medium |

### **圖度量類**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Graph Diameter | N/A | 全點對取最大 | Medium |
| Center of Star Graph | 1791 | 對距離做後處理 | Easy |
| Tree Diameter | 1522 | 樹上的全點對 | Medium |


## 決策框架

### 什麼時候該用 Floyd-Warshall

✅ **這些情況用 Floyd-Warshall：**
- 需要全點對最短路徑
- 圖很小（V ≤ 400-500）
- 需要遞移閉包
- 需要偵測負環
- 圖很稠密（E ≈ V²）
- 實作簡單度優先
- 需要回答很多組不同點對的詢問

❌ **這些情況別用 Floyd-Warshall：**
- 只需要單源最短路徑（用 Dijkstra／Bellman-Ford）
- 圖非常大（V > 1000）
- 圖很稀疏（改成跑 V 次 Dijkstra）
- 記憶體吃緊（要 O(V²) 空間）
- 追求最快的尋路（單源的話 Dijkstra 更快）

### 實作檢查清單

```python
# Floyd-Warshall Implementation Checklist:

# 1. Initialize distance matrix
dist = [[float('inf')] * n for _ in range(n)]
for i in range(n): dist[i][i] = 0

# 2. Add edges
for u, v, w in edges: dist[u][v] = w

# 3. Three nested loops (ORDER MATTERS: k must be outer)
for k in range(n):           # Intermediate vertex
    for i in range(n):       # Source
        for j in range(n):   # Destination
            dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

# 4. Check for negative cycles (optional)
has_neg_cycle = any(dist[i][i] < 0 for i in range(n))

# 5. Handle disconnected components
# dist[i][j] == float('inf') means no path
```


## 總結與速查

### 時間／空間複雜度

| 面向 | 複雜度 | 說明 |
|--------|------------|-------|
| 時間 | O(V³) | 三層巢狀迴圈 |
| 空間 | O(V²) | 距離矩陣 |
| 前處理 | O(E) | 建相鄰矩陣 |
| 查詢時間 | O(1) | 前處理做完之後 |

### 關鍵程式碼模式

```python
# Pattern 1: Basic shortest path
dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

# Pattern 2: Transitive closure (reachability)
reach[i][j] = reach[i][j] or (reach[i][k] and reach[k][j])

# Pattern 3: Minimax (bottleneck)
dist[i][j] = min(dist[i][j], max(dist[i][k], dist[k][j]))

# Pattern 4: Maximum capacity
capacity[i][j] = max(capacity[i][j], min(capacity[i][k], capacity[k][j]))

# Pattern 5: Negative cycle detection
has_neg_cycle = any(dist[i][i] < 0 for i in range(n))
```

### 常見變形

| 變形 | 改法 | 使用情境 |
|-----------|--------------|----------|
| **標準版** | min(dist[i][j], dist[i][k]+dist[k][j]) | 最短路徑 |
| **最長路徑** | max(dist[i][j], dist[i][k]+dist[k][j]) | 關鍵路徑 |
| **Minimax** | min(dist[i][j], max(dist[i][k], dist[k][j])) | 瓶頸路徑 |
| **Maximin** | max(dist[i][j], min(dist[i][k], dist[k][j])) | 最寬路徑 |
| **布林版** | OR/AND 運算 | 可達性 |

### 常見錯誤與提醒

**🚫 常見錯誤：**
- 迴圈順序寫錯（k 必須在最外層）
- 忘記把對角線初始化成 0
- 沒處理無向圖（兩個方向都要建）
- 負環的檢查方式不對
- 在大圖上拿 Floyd-Warshall 解單源問題

**✅ 最佳實務：**
- k 永遠放最外層（它是中繼頂點）
- 加邊之前先把 dist[i][i] 設成 0
- 無向圖記得兩個方向都加
- 檢查對角線有沒有負值來偵測環
- 如果只需要最後的距離，可以考慮省空間版本
- 只要單源的話就用 Dijkstra

### 面試提醒

1. **先辨認題型**：問清楚是單源還是全點對
2. **主動講複雜度**：一開始就講 O(V³) 時間、O(V²) 空間
3. **跟其他選項比較**：說明什麼時候 Dijkstra／Bellman-Ford 更好
4. **邊界情況**：不連通的分量、負環、自環
5. **可以優化的地方**：改成跑 V 次 Dijkstra 會不會更好？

### 面試中什麼時候該提 Floyd-Warshall

- 「我們需要全點對最短路徑」→ Floyd-Warshall
- 「圖很小（< 500 個頂點）」→ Floyd-Warshall 可行
- 「需要遞移閉包」→ Floyd-Warshall 最自然
- 「可以處理負權重嗎？」→ 可以，這點跟 Dijkstra 不同
- 「那更大的圖呢？」→ 跑 V 次 Dijkstra，或改用 Johnson 演算法

### 相關演算法

- **[Dijkstra](./Dijkstra.md)**：單源，稀疏圖上更快，不支援負權重
- **[Bellman-Ford](./Bellman-Ford.md)**：單源，支援負權重，比較慢
- **Johnson's Algorithm**：用重新加權 + Dijkstra 做全點對，O(V²logV + VE)
- **Warshall's Algorithm**：遞移閉包的布林版本
- **Path Matrix Multiplication**：另一種 O(V³logV) 的做法
