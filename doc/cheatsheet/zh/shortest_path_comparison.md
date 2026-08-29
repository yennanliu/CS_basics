# Shortest Path Algorithms — 什麼時候用哪一個

> **範圍** — **只做選型判斷** — 哪種題型該用哪個最短路徑演算法，以及直覺選法在哪裡會錯。不放完整實作。
> **另見**：[Dijkstra.md](./Dijkstra.md)；[Bellman-Ford.md](./Bellman-Ford.md)；[Floyd-Warshall.md](./Floyd-Warshall.md)；[bfs.md](./bfs.md) — 無權重／0-1 權重；[graph.md](./graph.md) — 圖的其他所有主題。

## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## 快速決策表

| 問題 | 答案 → 演算法 |
|----------|-------------------|
| 權重非負、單一起點？ | **Dijkstra** O((V+E) log V) |
| 允許負權重、單一起點？ | **Bellman-Ford** O(V·E) |
| 需要偵測負環？ | **Bellman-Ford** O(V·E) |
| 最多 K 條邊／K 次轉乘？ | **Bellman-Ford**（跑 K 輪） |
| 全點對最短路徑？ | **Floyd-Warshall** O(V³) |
| 遞移閉包（可達性）？ | **Floyd-Warshall**（布林版） |
| 無權重圖？ | **BFS** O(V+E) |
| 權重只有 0/1 的格子圖？ | **0-1 BFS**（雙端佇列）O(V+E) |
| DAG？ | **拓撲排序 + 鬆弛** O(V+E) |
| 稠密圖、單一起點？ | **Dijkstra** 搭配陣列 O(V²) |
| 稀疏圖、單一起點？ | **Dijkstra** 搭配堆積 O((V+E) log V) |

## 並排比較

| 性質 | BFS | Dijkstra | Bellman-Ford | Floyd-Warshall |
|----------|-----|----------|--------------|----------------|
| **類型** | 單一起點 | 單一起點 | 單一起點 | 全點對 |
| **時間** | O(V+E) | O((V+E) log V) | O(V·E) | O(V³) |
| **空間** | O(V) | O(V) | O(V) | O(V²) |
| **負權重** | 不行 | 不行 | 可以 | 可以 |
| **偵測負環** | 不行 | 不行 | 可以 | 可以 |
| **圖的型態** | 無權重 | 有權重（≥0） | 任意 | 任意 |
| **做法** | 佇列 | 貪婪 + 堆積 | 鬆弛 ×(V-1) | DP |
| **實作難度** | 簡單 | 中等 | 簡單 | 簡單 |

## 決策流程圖

```text
Start: What's the shortest path problem?
│
├── Unweighted graph?
│   └── YES → BFS  O(V+E)
│
├── Single source or all pairs?
│   ├── ALL PAIRS
│   │   ├── V ≤ 400? → Floyd-Warshall  O(V³)
│   │   └── V > 400?  → Run Dijkstra from each vertex  O(V·(V+E) log V)
│   │
│   └── SINGLE SOURCE
│       ├── DAG? → Topo sort + relax  O(V+E)
│       ├── Negative weights?
│       │   ├── NO  → Dijkstra  O((V+E) log V)
│       │   └── YES → Bellman-Ford  O(V·E)
│       └── At most K edges?
│           └── Bellman-Ford with K iterations
│
└── Need negative cycle detection?
    ├── Single check → Bellman-Ford (V-th iteration)
    └── All pairs → Floyd-Warshall (check diagonal)
```

## 常見錯誤與陷阱

### 1. 對負權重用 Dijkstra
```text
Graph:  A --1--> B --(-5)--> C
        A --3--> C

Dijkstra visits C via A→C (cost 3), marks C as done.
Misses A→B→C (cost 1+(-5) = -4).  ← WRONG ANSWER

Fix: Use Bellman-Ford.
```

### 2. LC 787（Cheapest Flights K Stops）該用 Bellman-Ford 還是 Dijkstra
```text
Dijkstra alone doesn't work — K stop constraint means a longer
path might be cheaper. Need modified Bellman-Ford with K iterations,
or modified Dijkstra with state (node, stops_remaining).
```

### 3. Floyd-Warshall 的迴圈順序很要命
```java
// CORRECT: k (intermediate) must be outermost loop
for (int k = 0; k < V; k++)        // intermediate vertex
    for (int i = 0; i < V; i++)     // source
        for (int j = 0; j < V; j++) // destination
            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);

// WRONG: i or j as outermost loop gives incorrect results
```

### 4. 格子圖上該用 Dijkstra 還是 DP
```text
LC 64 (Min Path Sum): only move right/down → DAG → use DP (simpler)
LC 1631 (Min Effort):  move 4 directions → cycles possible → use Dijkstra
LC 778 (Swim in Rising Water): 4 directions → Dijkstra or binary search + BFS

Rule: If movement is restricted to one direction (no cycles) → DP.
      If movement allows backtracking/cycles → Dijkstra.
```

## 變形：0-1 BFS

當邊的權重只有 0 或 1：

```python
# Time: O(V+E), Space: O(V)
from collections import deque

def bfs01(graph, src, n):
    dist = [float('inf')] * n
    dist[src] = 0
    dq = deque([src])

    while dq:
        u = dq.popleft()
        for v, w in graph[u]:  # w is 0 or 1
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                if w == 0:
                    dq.appendleft(v)  # 0-weight → front
                else:
                    dq.append(v)      # 1-weight → back
    return dist
```

**經典題：** LC 1368（Min Cost to Make at Least One Valid Path）— 在格子圖上跑 0-1 BFS

## LC 範例

| # | 題目 | 演算法 | 為什麼選它？ |
|---|---------|-----------|---------------|
| 743 | Network Delay Time | Dijkstra | 非負權重、單一起點 |
| 787 | Cheapest Flights K Stops | Bellman-Ford（跑 K 輪） | 有 K 條邊的限制 |
| 1334 | Find City Smallest Neighbors | Floyd-Warshall | 全點對 + 門檻值 |
| 1631 | Path with Min Effort | Dijkstra | 格子圖、四方向、非負 |
| 778 | Swim in Rising Water | Dijkstra / BS+BFS | 格子圖、最小化路徑上的最大值 |
| 1368 | Min Cost Valid Path | 0-1 BFS | 權重只有 0/1 |
| 1462 | Course Schedule IV | Floyd-Warshall | 遞移閉包 |
| 862 | Shortest Subarray Sum ≥ K | 根本不是最短路徑！ | 前綴和 + 單調雙端佇列 |
| 64 | Minimum Path Sum | DP（不是 Dijkstra） | DAG — 只能往右／往下 |
| 505 | The Maze II | Dijkstra | 有權重（滾動距離）、非負 |

## 題目 → 演算法決策表（延伸版） ⭐⭐⭐⭐⭐

先看 **關鍵訊號** 那一欄 — 那是題目敘述裡逼你做出選擇的那句話。

| # | 題目 | 敘述裡的關鍵訊號 | 演算法 | 為什麼只能這樣做 |
|---|---------|-------------------------|-----------|-----------------|
| 847 | Shortest Path Visiting All Nodes | 「走訪**每一個**節點」、`n ≤ 12` | **對 `(node, mask)` 做 BFS** | 無權重 → BFS，但單純的 `seen[node]` 是錯的：同一個節點會需要帶著不同的已訪集合再進來一次 |
| 1129 | Shortest Path with Alternating Colors | 邊的顏色必須**交替** | **對 `(node, lastColor)` 做 BFS** | 無權重 → BFS；這個限制是多出來的**狀態**，不是多出來的權重 |
| 1514 | Path with Maximum Probability | 權重落在 `[0,1]`，要**最大化**乘積 | **Dijkstra 搭配最大堆積** | 乘積沿著路徑只會越來越小（權重 ≤ 1）→ 貪婪依然成立；嚴格來說取 `-log(p)` 就變回一般的最小成本 Dijkstra |
| 1976 | Number of Ways to Arrive at Destination | **計算**最短路徑有幾條 | **Dijkstra + `ways[]`** | 一趟搞定：嚴格變短時 `ways[v] = ways[u]`，打平時 `ways[v] += ways[u]`（對 1e9+7 取模） |
| 1928 | Minimum Cost to Reach Destination in Time | 在**時間預算**內最小化**費用** | **對 `(node, timeUsed)` 做 Dijkstra**，或 `dp[t][node]` | 兩個彼此獨立的資源 — 只對費用做 Dijkstra 是**錯的**（見下） |
| 399 | Evaluate Division | 有權重的邊，查詢是「**任一條**路徑的值」 | **DFS/BFS 邊走邊乘**（或帶權併查集） | 權重本身自洽 ⇒ 每條路徑答案都一樣，根本沒有東西要最小化 — 不需要鬆弛 |
| 1971 | Find if Path Exists in Graph | **只問可達性**，沒問距離 | **BFS / DFS / 併查集** | 敘述裡沒有成本 → 搬出最短路徑那套完全是做白工 |
| 1697 | Checking Existence of Edge Length Limited Paths | 「路徑上**每一條邊**都 < limit」 | **查詢排序 + 併查集（離線）** | 這是瓶頸（最大邊）限制，不是可加總的成本 → 鬆弛式 `dist[u]+w` 根本套不上 |
| 1584 | Min Cost to Connect All Points | 「把**所有**點連起來，總花費最小」 | **MST（Prim / Kruskal）** | 要的是最便宜的*樹*，不是最便宜的*路徑* — Prim 長得像 Dijkstra，但鬆弛方式不同（見下） |
| 329 | Longest Increasing Path in a Matrix | **最長**路徑 | **在隱式 DAG 上做記憶化 DFS** | 最長路徑沒有對應的貪婪／鬆弛做法；是「嚴格遞增」這條規則讓圖變成無環的 |
| 1857 | Largest Color Value in a Directed Graph | 路徑上某個顏色出現最多次 | **拓撲排序 + DP**（有環 ⇒ `-1`） | 同上，但這是一般的有向圖 — 做最長路徑 DP 之前必須先偵測環 |

## 直覺選法錯在哪 ⭐⭐⭐⭐⭐

### A. Dijkstra 拿錯了純量 — LC 1928

```text
Minimize FEE, but total TIME must stay ≤ maxTime.

Cheapest-fee route may blow the time budget; the fastest route
may be expensive. Neither scalar alone dominates the other.

Popping by fee and marking node "done" is WRONG: the same node
reached later (higher fee) but FASTER can still lead to the answer.

Fix: make time part of the state.
     Keep bestTime[node]; only expand a state whose time strictly
     improves on every previously expanded time at that node.
```

跟 **LC 787**（費用 vs 轉乘次數）是同一種形狀 — 只要題目同時給了**兩個預算**，其中一個就該放進狀態裡。

### B. `seen[node]` 太粗 — LC 847 / 1129 / 787

```text
Plain BFS/Dijkstra assumes: "first arrival at a node is final".
That breaks the moment the answer depends on HOW you arrived.

LC 847  : depends on which nodes are already visited  → state = (node, mask)
LC 1129 : depends on the color of the last edge used  → state = (node, lastColor)
LC 787  : depends on how many stops are left          → state = (node, stops)

Rule: seen/dist must be keyed by the FULL state, not by node.
      State count blows up the complexity by the size of the extra dimension.
```

### C. Prim vs Dijkstra — 只差一項 — LC 1584

```java
// Dijkstra  (cheapest PATH from source)
if (dist[u] + w < dist[v]) dist[v] = dist[u] + w;

// Prim / MST (cheapest TREE covering everything)   ← LC 1584
if (w < key[v]) key[v] = w;                  // no dist[u] term!
```

堆積骨架一模一樣，鬆弛方式不同。「把**所有**節點連起來」→ MST；「從 **A 走到 B**」→ 最短路徑。

### D. 要最大化而不是最小化 — LC 1514

```text
Dijkstra's greedy argument needs the path metric to be monotone
NON-IMPROVING as the path grows.

Sum of non-negative weights  : only grows  → min-heap Dijkstra  ✅
Product of probabilities ≤ 1 : only shrinks → max-heap Dijkstra ✅ (LC 1514)
Product of weights > 1       : grows unboundedly → Dijkstra ❌
```

## 模板：狀態擴增的搜尋 ⭐⭐⭐⭐⭐

**關鍵想法**：演算法不變（無權重就 BFS，有權重就 Dijkstra），把*狀態*加寬。
其餘的東西 — `seen`、`dist`、佇列裡塞的資料 — 全部改用加寬後的狀態當鍵值。

### 變形 1 — 對 `(node, bitmask)` 做 BFS — LC 847

```java
// java
// time = O(n * 2^n), space = O(n * 2^n)
// IDEA: unweighted -> BFS, but the answer depends on WHICH nodes are already
//       visited, so the state is (node, visitedMask), not just node.
//       Start from every node at once (path may begin anywhere).
// LC 847 - Shortest Path Visiting All Nodes
public int shortestPathLength(int[][] graph) {
    int n = graph.length, full = (1 << n) - 1;
    boolean[][] seen = new boolean[n][1 << n];        // keyed by FULL state
    Deque<int[]> q = new ArrayDeque<>();
    for (int i = 0; i < n; i++) {                     // multi-source start
        q.offerLast(new int[]{i, 1 << i});
        seen[i][1 << i] = true;
    }
    int steps = 0;
    while (!q.isEmpty()) {
        for (int sz = q.size(); sz > 0; sz--) {       // level by level
            int[] cur = q.pollFirst();
            if (cur[1] == full) return steps;
            for (int nxt : graph[cur[0]]) {
                int mask = cur[1] | (1 << nxt);
                if (!seen[nxt][mask]) {
                    seen[nxt][mask] = true;
                    q.offerLast(new int[]{nxt, mask});
                }
            }
        }
        steps++;
    }
    return 0;
}
```

```python
# python
# time = O(n * 2^n), space = O(n * 2^n)
# IDEA: same as above - BFS whose visited set is keyed by (node, mask)
# LC 847 - Shortest Path Visiting All Nodes
from collections import deque

def shortestPathLength(graph):
    n = len(graph)
    full = (1 << n) - 1
    dq = deque((i, 1 << i, 0) for i in range(n))      # (node, mask, dist)
    seen = {(i, 1 << i) for i in range(n)}
    while dq:
        node, mask, d = dq.popleft()
        if mask == full:
            return d
        for nxt in graph[node]:
            nm = mask | (1 << nxt)
            if (nxt, nm) not in seen:
                seen.add((nxt, nm))
                dq.append((nxt, nm, d + 1))
    return 0
```

### 變形 2 — 對 `(node, resourceUsed)` 做 Dijkstra — LC 1928

```java
// java
// time = O(maxTime * E * log(maxTime * E)), space = O(maxTime * E)
// IDEA: two budgets (fee to minimize, time capped by maxTime). Pop by FEE, so
//       the first pop of node n-1 is the answer. A node may be expanded many
//       times - but only when it arrives with a STRICTLY better time.
// LC 1928 - Minimum Cost to Reach Destination in Time
public int minCost(int maxTime, int[][] edges, int[] passingFees) {
    int n = passingFees.length;
    List<int[]>[] g = new List[n];
    for (int i = 0; i < n; i++) g[i] = new ArrayList<>();
    for (int[] e : edges) {
        g[e[0]].add(new int[]{e[1], e[2]});
        g[e[1]].add(new int[]{e[0], e[2]});
    }
    int[] bestTime = new int[n];
    Arrays.fill(bestTime, Integer.MAX_VALUE);
    // (cost, time, node) - ordered by cost
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{passingFees[0], 0, 0});
    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int cost = cur[0], time = cur[1], node = cur[2];
        if (node == n - 1) return cost;               // min cost, popped by cost
        if (time >= bestTime[node]) continue;         // pruning = the "state" check
        bestTime[node] = time;
        for (int[] nb : g[node]) {
            int nt = time + nb[1];
            if (nt <= maxTime) pq.offer(new int[]{cost + passingFees[nb[0]], nt, nb[0]});
        }
    }
    return -1;
}
```

```python
# python
# time = O(maxTime * E * log(maxTime * E)), space = O(maxTime * E)
# IDEA: Dijkstra keyed by (node, time) - expand a node again only if it is
#       reached strictly faster than any earlier expansion of that node.
# LC 1928 - Minimum Cost to Reach Destination in Time
import heapq

def minCost(maxTime, edges, passingFees):
    n = len(passingFees)
    g = [[] for _ in range(n)]
    for u, v, t in edges:
        g[u].append((v, t))
        g[v].append((u, t))
    best_time = [float('inf')] * n
    pq = [(passingFees[0], 0, 0)]                     # (cost, time, node)
    while pq:
        cost, time, node = heapq.heappop(pq)
        if node == n - 1:
            return cost
        if time >= best_time[node]:
            continue
        best_time[node] = time
        for nxt, t in g[node]:
            if time + t <= maxTime:
                heapq.heappush(pq, (cost + passingFees[nxt], time + t, nxt))
    return -1
```

> **LC 1928 的另一種寫法**：分層 DP `dp[t][node] = 恰好在時間 t 抵達 node 的最小費用`，
> 沿著 `t = 1..maxTime` 一路鬆弛 — 這就是 Bellman-Ford 的框架，`O(maxTime · E)`，不用堆積。
> 跟 LC 787 的「跑 K 輪」是同一招：有上界的那個資源直接變成 DP 的一個維度。

## 0-1 BFS — Java 版（LC 1368）

```java
// java
// time = O(m*n), space = O(m*n)
// IDEA: changing a cell's arrow costs 1, following it costs 0 -> weights are
//       only {0,1}, so a deque replaces the heap: 0-edges to the FRONT,
//       1-edges to the BACK. The deque stays sorted by distance.
// LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
public int minCost(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dir = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};   // grid value 1,2,3,4
    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    Deque<int[]> dq = new ArrayDeque<>();
    dist[0][0] = 0;
    dq.offerFirst(new int[]{0, 0});
    while (!dq.isEmpty()) {
        int[] cur = dq.pollFirst();
        int r = cur[0], c = cur[1];
        for (int k = 0; k < 4; k++) {
            int nr = r + dir[k][0], nc = c + dir[k][1];
            if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;
            int w = (grid[r][c] == k + 1) ? 0 : 1;      // arrow already points there?
            if (dist[r][c] + w < dist[nr][nc]) {
                dist[nr][nc] = dist[r][c] + w;
                if (w == 0) dq.offerFirst(new int[]{nr, nc});
                else        dq.offerLast(new int[]{nr, nc});
            }
        }
    }
    return dist[m - 1][n - 1];
}
```

## 延伸參考

- **LC 1311** Get Watched Videos by Your Friends — BFS 的**層**（剛好 `k` 步），再依出現次數排序；這是層數查詢，不是距離查詢。
- **LC 1466** Reorder Routes to Make All Paths Lead to the City Zero — 從 `0` 出發、無視方向走訪，數有幾條邊指錯方向（等於 0/1 邊權，但因為是樹 ⇒ 單純 DFS 就夠）。
- **LC 1489** Find Critical and Pseudo-Critical Edges in MST — 屬於 MST 家族，不是最短路徑；參考上面 LC 1584 那一列。

## 另見
- [Dijkstra Cheatsheet](./Dijkstra.md)
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md)
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md)
- [BFS Cheatsheet](./bfs.md)
