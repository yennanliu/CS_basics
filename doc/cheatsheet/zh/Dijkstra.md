# Dijkstra's Algorithm

> **範圍** — 用優先佇列解**非負權重**的單源最短路徑。
> **另見**：[Dijkstra_examples.md](./Dijkstra_examples.md) — 撐起這些模板的十一題詳解；[shortest_path_comparison.md](./shortest_path_comparison.md) — 該挑哪個演算法；[Bellman-Ford.md](./Bellman-Ford.md) — 權重可能為負時用它；[Floyd-Warshall.md](./Floyd-Warshall.md) — 需要全點對時用它；[heap.md](./heap.md) — 底下那個優先佇列。

## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 總覽
**Dijkstra（戴克斯特拉）演算法**是一個貪婪演算法，專門解邊權 `NON-NEGATIVE`（非負）圖上的單源最短路徑問題。它會算出起點（source）到圖中所有其他節點的最短路徑。

### 關鍵性質
- **時間複雜度**：用二元堆積是 O((V + E) log V)，用陣列是 O(V²)
- **空間複雜度**：O(V)，存距離陣列跟走訪集合
- **核心想法**：每次貪婪地挑出「還沒定案、距離最小」的節點
- **什麼時候用**：非負權重的單源最短路徑
- **限制**：`不能`處理`負`邊權

### 核心特徵
- **貪婪演算法**：永遠先挑距離最小的節點
- **優先佇列**：用最小堆積（min-heap）快速取出最小值
- **鬆弛（Relaxation）**：找到更短的路徑就更新距離
- **定案（Finalization）**：一個節點一旦被走訪過，它的距離就是最佳解

### 參考資料
- [Dijkstra's Algorithm Visualization](https://www.cs.usfca.edu/~galles/visualization/Dijkstra.html)
- [CP Algorithms - Dijkstra](https://cp-algorithms.com/graph/dijkstra.html)
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md) - 負權重處理的對照
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md) - 全點對最短路徑的對照


## 題型分類

### **分類 1：經典最短路徑**
- **說明**：標準的單源最短路徑題
- **例子**：LC 743（Network Delay）、LC 1514（Path with Max Probability）
- **模式**：直接套 Dijkstra

### **分類 2：帶限制的最短路徑** ⚠️ Dijkstra 變形
- **說明**：最短路徑多了一個限制維度（轉機次數、障礙物、鑰匙、時間）
- **例子**：LC 787（Cheapest Flights K Stops）、LC 1293（Shortest Path K Obstacle Removal）、LC 864（Get All Keys）、LC 1928（Minimum Cost K Waypoints）
- **模式**：二維狀態 Dijkstra — 狀態是 `(cost, node, constraint)`，不是 `(cost, node)`
- **為什麼算變形**：同一個節點在不同限制值下是**不同的狀態**。標準的 `visited[node]` 或 `dist[node]` 剪枝在這裡是**錯的** — 它會丟掉那些「用不同剩餘額度抵達同一節點」的合法路徑。
- **剪枝規則**：`best[(node, constraint)] <= cost`（二維 best map，不是一維 dist 陣列）

### **分類 3：格子上的最短路徑**
- **說明**：在二維格子裡找最佳路徑
- **例子**：LC 64（Minimum Path Sum）、LC 1631（Path Min Effort）、LC 778（Swim in Rising Water）
- **模式**：在隱式圖上跑 Dijkstra（把格子當節點）
- **⚠️ 特別注意**：LC 64 可以純 DP 解，不必動用 Dijkstra（見下）

### **分類 4：多源最短路徑**
- **說明**：從多個起點出發找最短路徑
- **例子**：LC 2812（Find Safest Path）、LC 1162（As Far from Land）
- **模式**：一開始就把多個源頭全丟進去，或建一個超級源點

### **分類 5：與時間相關的最短路徑**
- **說明**：路徑成本會隨時間或順序改變
- **例子**：LC 2045（Second Minimum Time）、LC 882（Reachable Nodes）
- **模式**：在優先佇列裡一起追蹤時間／狀態


## 該不該用 Dijkstra：兩個問題

這份表以前有九個小節在用各種說法吵 LC 1631。其實都可以收斂成兩個問題 —— 任何最短路徑題都能拿來問一遍 —— 再加一個值得知道的替代視角。

### 1) 成本是可加的嗎？格子能不能排出順序？— LC 64 vs LC 1631 ⭐⭐⭐⭐


**問題**：跑 Dijkstra 真的需要 `dist[r][c]`（記錄到每格的最小成本）嗎？還是純 DP 就夠了？

**答案**：看**移動方向**：

#### **LC 64: Minimum Path Sum** ✅ 純 DP 就夠
```text
Movement: RIGHT only ↓ or DOWN only →
```
- **DP 為什麼行得通**：`(i,j)` 這格只可能從 `(i-1,j)` 或 `(i,j-1)` 過來
- **不需要 dist[][]**：每格照拓撲順序剛好算一次
- **不會回頭**：一格算完之後，不可能再冒出「更好的路徑」
- **解法**：單純的二維 DP，或壓成 O(min(m,n)) 空間的一維 DP

```java
// Pure DP - NO dist[][] needed
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    dp[0][0] = grid[0][0];
    
    // First column: only from above
    for (int i = 1; i < m; i++)
        dp[i][0] = dp[i-1][0] + grid[i][0];
    
    // First row: only from left
    for (int j = 1; j < n; j++)
        dp[0][j] = dp[0][j-1] + grid[0][j];
    
    // Fill rest
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            dp[i][j] = grid[i][j] + Math.min(dp[i-1][j], dp[i][j-1]);
    
    return dp[m-1][n-1];
}
// Time: O(m*n), Space: O(min(m,n))
```

#### **LC 1631: Path With Minimum Effort** ⚠️ 需要 Dijkstra + dist[][]
```text
Movement: UP, DOWN, LEFT, RIGHT (all 4 directions)
```
- **為什麼需要 Dijkstra**：同一格可能從很多條路徑抵達，而且更好的路徑可能晚一點才出現
- **dist[][] 不能省**：它記錄每格「目前找到的最佳成本」
- **會回頭重訪**：四個方向都能走的時候，你可能用更好的成本再次踩到同一格
- **解法**：Dijkstra 搭配 dist[][] + PriorityQueue

```java
// Dijkstra + dist[][] - NECESSARY for 4-directional movement
public int minimumEffortPath(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    
    // dist[r][c] = minimum effort found so far to reach (r,c)
    int[][] dist = new int[m][n];
    for (int[] row : dist)
        Arrays.fill(row, Integer.MAX_VALUE);
    
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
    pq.offer(new int[]{0, 0, 0});
    dist[0][0] = 0;
    
    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    
    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int r = cur[0], c = cur[1], cost = cur[2];
        
        // Already processed with better cost
        if (cost > dist[r][c]) continue;
        
        if (r == m-1 && c == n-1) return cost;
        
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                int newCost = Math.max(cost, Math.abs(heights[nr][nc] - heights[r][c]));
                if (newCost < dist[nr][nc]) {
                    dist[nr][nc] = newCost;
                    pq.offer(new int[]{nr, nc, newCost});
                }
            }
        }
    }
    return -1;
}
// Time: O(m*n*log(m*n)), Space: O(m*n)
```

#### 對照表 — LC 64 vs LC 1631

| 題目 | 移動方向 | 成本模型 | 最佳解法 | 需要 dist[][]？ | 需要 visited？ |
|---------|----------|-----------|----------------|--------|---------|
| **LC 64** | 右 + 下 | 加總 | **二維 DP** | ❌ 不用 | ❌ 不用 |
| **LC 1631** | 四方向 | 差值取最大 | **Dijkstra** | ✅ 要 | ✅ 要（靠 dist 檢查） |
| **LC 1263** | 四方向 | 加總成本 | **Dijkstra** | ✅ 要 | ✅ 要（靠 dist 檢查） |


有兩個推論值得白紙黑字寫下來，因為這正是兩種最常見的翻車方式：

**為什麼 DP 救不了 LC 1631。**
**答**：問題出在**移動方向**：
- **LC 64**：只能往右／往下 → 存在拓撲順序 → DP 可行 ✅
- **LC 1631**：上下左右都能走 → 有環 → DP 失效 ❌

四方向移動會產生循環依賴：
```text
(1,1) → (1,2) → (2,2) → (2,1) → (1,1)
```
DP 要求依賴關係構成 DAG（無環），所以這題**只能用 Dijkstra 或二分搜尋**。

**為什麼「cost」和「effort」不是同一種量。**
**答**：不同題量的是不同東西：
- **Cost（LC 64、1263）**：路徑上所有值的總和 = `cost += value`
- **Effort（LC 1631）**：相鄰格子差值的最大值 = `effort = max(effort, |diff|)`

Cost 可加，effort 不可加。就是這個不可加性讓 DP 掛掉。


### 2) 你需要 `dist[]`，還是 `visited[]` 就夠？ ⭐⭐⭐⭐

```text
dist[r][c] = "What's the MINIMUM cost I've found SO FAR to reach (r,c)?"
```
- **初始化**：`dist[r][c] = Integer.MAX_VALUE`（未知）
- **更新**：從 PQ 彈出某格、成本為 C 時，檢查 `if (C > dist[r][c]) continue;`
  - 條件成立表示我們早就找到更好的路徑 → 跳過不處理
  - 這等於**自動避免重複處理**，不需要另外開 visited 陣列
- **什麼時候非要不可**：同一格有多條路徑可達 → 需要 Dijkstra 的鬆弛機制


**答**：不用，兩者擇一：
- **選項 A：dist[][]** → 處理前檢查 `if (newCost < dist[r][c])`
- **選項 B：visited[]** → 從 PQ 第一次彈出後就標記已走訪

兩種都能避免重複處理同一格。哪個你看得順眼就用哪個 —— 但它們**不是**永遠可以互換。下面會說明什麼時候 `best[]`／`dist[]` 是真的**必要**，什麼時候可以簡化成「直接拿下一條候選路徑跟目前（剛彈出）這條比」。



Dijkstra 在看候選邊 `cur_node -> nxt_node` 時，可以做兩種完全不同的比較：

| | **類型 1：跟 `best[]` 比** | **類型 2：下一條路徑跟剛彈出的 cur 路徑比** |
|---|---|---|
| **比的是什麼** | `candidate_value` 對上 `best[nxt_node]`（該節點／狀態**歷來記錄過**的最佳值） | 什麼都沒存 —— `candidate_value` 直接由 `cur_node` 已定案的值推出來，沒有查表這回事 |
| **檢查發生在什麼時候** | **推進堆積之前**（鬆弛步驟） | **彈出之後** —— 只有一個 `visited[node]` 布林關卡，完全不比值 |
| **為什麼需要／為什麼行得通** | 整趟跑下來，同一個節點（或節點＋限制狀態）可能被用不同的值抵達很多次；你得記住目前最好的那個，才知道新路徑是不是真的更好 | Dijkstra 的最小堆積不變量保證**一個節點第一次被彈出時就已經是全域最佳**，所以彈出後根本沒東西好比 —— 之後那些比較差的重複項，visited 檢查一律跳過 |
| **會跨越多次更新持續存在嗎？** | ✅ 會 —— 一個節點定案前，`best[node]` 可能被覆寫好幾次 | ❌ 不會 —— 一個節點只寫一次（`visited[node] = true`），之後再也不動 |
| **什麼時候非用不可…** | 狀態多了一個維度（`(node, constraint)` —— 同一個節點在好幾個不同限制值下都算「合法抵達」），或者你需要偵測**平手**（例如數路徑條數），或者你想在推進堆積前就把不會變好的候選擋掉 | 狀態就是每個節點一個純量、邊權非負、沒有額外限制維度 —— 也就是最單純的單源情況 |
| **用錯了會不會默默出錯** | 不會（永遠正確，只是多吃點記憶體） | ❌ 在**帶限制**的題目（LC 787 那類）只用 `visited[]` 是**錯的** —— 它會抹掉那個額外維度，丟掉合法路徑（具體推演見 [LC 787](./Dijkstra_examples.md#4-cheapest-flights-within-k-stops--lc-787--2d-state-)） |

**一句話講完核心概念：**
> `best[]` 回答的是：*「這個候選，比我對這個狀態看過的任何東西都好嗎？」* —— 只要一個狀態可以合法地用不同的值重訪，就需要它。
> 只用 `visited[]` 回答的是：*「這個狀態已經定案了嗎？」* —— 只有在堆積的彈出順序保證（第一次彈出＝最佳）完全涵蓋整個狀態時才夠，也就是沒有額外限制維度的情況。

**經典 Dijkstra 題目分類：**

| 類型 | LC # | 題目 | 原因 |
|------|------|---------|-----|
| **類型 1 — 需要 best[]/dist[]** | 743 | Network Delay Time | 標準單源，推進堆積前先鬆弛 |
| | 1514 | Path with Maximum Probability | `best[]`／`max_prob[]` 記錄每個節點目前的最大乘積 |
| | 1976 | Number of Ways to Arrive at Destination | 同時需要 `dist[]` **跟** `ways[]` —— 必須抓到精確的平手（`==`），沒有存下來的值就辦不到 |
| | 787 | Cheapest Flights Within K Stops | **必須**用二維 `best[(node, stops)]` —— 只用 `visited[node]` 可證明是錯的（見 [LC 787](./Dijkstra_examples.md#4-cheapest-flights-within-k-stops--lc-787--2d-state-) 的推演） |
| | 1293 / 864 / 2093 | 帶限制狀態的 Dijkstra 變形 | 跟 787 同理 —— 多一個限制維度就代表一個節點會有多個合法的定案狀態 |
| **類型 2 — 只用 visited[] 就夠** | 1631 | Path With Minimum Effort | 變形 2（`visited[][]`）—— 每格狀態是純量，第一次彈出就是最佳 effort |
| | 778 | Swim in Rising Water | `visited[][]` 標記格子已定案；下一條路徑的值 = `max(目前路徑值, 下一格高度)` |
| | 743 | Network Delay Time（另一種寫法） | [LC 743](./Dijkstra_examples.md#1-network-delay-time--lc-743) 展示的「visited 集合版本」—— 跟 `dist[]` 等價，只是改成彈出後檢查而非推進前檢查 |
| | 2290 | Minimum Obstacle Removal | 權重只有 0/1 → 用雙端佇列跑 0-1 BFS + `visited[]` 也行，不需要值表 |

**經驗法則**：如果「光靠 `node` 就能完整描述我在搜尋中的位置嗎？」這個問題答案是**是**，那只用 `visited[]` 就安全。一旦答案變成「不行，我還得知道自己用掉了幾次轉機／幾把鑰匙／清了幾個障礙」，就必須升級成以 `(node, constraint)` 為鍵的 `best[]`／`dist[]`。


**答**：對！`if (cost > dist[r][c]) continue;` 這個檢查**就是**你的 visited 機制：
- 第一次彈出 (r,c)：`cost == dist[r][c]` → 處理
- 之後再彈出 (r,c)：`cost > dist[r][c]` → 跳過（等同「已走訪」）

所以你不用多開一個陣列，就享有 visited[] 的語意。

---


### 3) 什麼時候併查集是更好的視角

**答**：以下情況用併查集：
- 你習慣自己把邊列表建出來
- 你想把問題看成圖的連通性問題
- 你在練 Kruskal 演算法

兩者時間複雜度一樣是 O(m×n×log(m×n))，但格子題通常 Dijkstra 比較直覺。

## 模板與演算法

### 模板對照表
| 模板類型 | 適用情境 | 追蹤的狀態 | 什麼時候用 |
|---------------|----------|---------------|-------------|
| **基本 Dijkstra** | 標準最短路徑 | (distance, node) | 沒有額外限制 |
| **帶限制路徑** | 有上限的路徑 | (cost, node, constraint) | K 次轉機、預算 |
| **格子 Dijkstra** | 二維格子移動 | (cost, x, y) | 矩陣題 |
| **多源** | 多個起點 | (dist, node, source) | 多個源頭 |
| **時間變動** | 成本隨時間變 | (time, node, state) | 動態成本 |

### 通用 Dijkstra 模板 ⭐⭐⭐⭐⭐
```python
import heapq
import collections

def dijkstra(n, edges, src, dst):
    # Build adjacency list
    graph = collections.defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
    
    # Min heap: (distance, node)
    pq = [(0, src)]
    # Distance array
    dist = [float('inf')] * n
    dist[src] = 0
    # Visited set (optional but recommended)
    visited = set()
    
    while pq:
        d, u = heapq.heappop(pq)
        
        # Skip if already processed with better distance
        if u in visited:
            continue
        visited.add(u)
        
        # Found destination
        if u == dst:
            return d
        
        # Relax edges
        for v, w in graph[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                heapq.heappush(pq, (dist[v], v))
    
    return dist[dst] if dist[dst] != float('inf') else -1
```

### 模板 1：基本 Dijkstra
```python
def dijkstra_basic(n, edges, src):
    """Find shortest paths from src to all nodes"""
    graph = collections.defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
    
    dist = [float('inf')] * n
    dist[src] = 0
    pq = [(0, src)]  # (distance, node)
    
    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]:  # Already processed
            continue
        
        for v, w in graph[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                heapq.heappush(pq, (dist[v], v))
    
    return dist
```

### 模板 2：帶限制的 Dijkstra（二維狀態變形） ⭐⭐⭐⭐

**核心想法 —— 為什麼這不是標準 Dijkstra：**

| | 標準 Dijkstra | 帶限制的 Dijkstra |
|---|---|---|
| **狀態** | `(cost, node)` | `(cost, node, constraint)` |
| **狀態空間** | 一維 —— 每個節點一筆 | 二維 —— 每組 `(node, constraint)` 一筆 |
| **剪枝** | `dist[node] <= cost` | `best[(node, stops)] <= cost` |
| **第一次彈出的不變量** | `node` 第一次彈出＝全域最佳 | `(node, stops)` 第一次彈出＝該 stops 值下的最佳 |
| **visited[node] 有用嗎？** | ✅ 有 | ❌ 沒有 —— 同一節點在不同轉機次數下都合法 |

**為什麼 `visited[node]`／`dist[node]` 會壞掉：**
```text
Example: src=0, dst=3, K=2
  Path A: 0→1→3  cost=900  stops=1  ← fewer stops but more expensive
  Path B: 0→1→2→3  cost=210  stops=2  ← more stops but cheaper

Standard dist[1] would be finalized at first pop (cost=100).
Path B would try to re-expand node 1 at stops=1, but dist[1] check blocks it.
→ Wrong: path B never explored, answer is incorrect.

With best[(node, stops)]: (node=1, stops=0) and (node=1, stops=1) are DIFFERENT states.
Both get explored independently. Correct answer found.
```

```python
def dijkstra_constrained(n, edges, src, dst, k):
    graph = collections.defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))

    # (cost, node, stops_used)
    pq = [(0, src, 0)]

    # KEY: 2D best-map — best[(node, stops)] = min cost to reach node using exactly 'stops' edges
    # This replaces the 1D dist[] array used in standard Dijkstra
    best = {}

    while pq:
        cost, u, stops = heapq.heappop(pq)

        # First pop of (u, dst) is optimal (min-heap guarantee for this state)
        if u == dst:
            return cost

        # Constraint exceeded — prune this branch
        if stops > k:
            continue

        # 2D pruning: skip if we've already reached (node, stops) cheaper
        if (u, stops) in best and best[(u, stops)] <= cost:
            continue
        best[(u, stops)] = cost

        for v, w in graph[u]:
            heapq.heappush(pq, (cost + w, v, stops + 1))

    return -1
```

**通用的帶限制 Dijkstra 骨架：**
```python
# Replace 'stops' with whatever constraint your problem has:
# - stops remaining (LC 787)
# - obstacles budget (LC 1293)
# - keys bitmask (LC 864)
# - discount count (LC 2093)

pq = [(0, src, initial_constraint)]
best = {}   # best[(node, constraint)] = min cost

while pq:
    cost, node, constraint = heapq.heappop(pq)
    if node == dst: return cost
    if constraint is exhausted: continue
    if (node, constraint) in best and best[(node, constraint)] <= cost: continue
    best[(node, constraint)] = cost
    for nei, w in graph[node]:
        heapq.heappush(pq, (cost + w, nei, updated_constraint))
```

**相似題目（同樣的二維狀態模式）：**
| LC # | 題目 | 限制維度 | 狀態 |
|------|---------|---------------------|-------|
| **787** | Cheapest Flights K Stops | 已用轉機次數 (0..K) | `(node, stops)` |
| **1293** | Shortest Path K Obstacle Removal | 已清障礙數 (0..K) | `(node, obstacles)` |
| **864** | Shortest Path to Get All Keys | 已收集的鑰匙（bitmask） | `(node, keys)` |
| **2093** | Minimum Cost to Reach City With Discounts | 已用折扣次數 (0..K) | `(node, discounts)` |
| **1928** | Min Cost to Reach Destination in Time | 剩餘時間 | `(node, time)` |

### 模板 3：格子上的 Dijkstra ⭐⭐⭐⭐
```python
def dijkstra_grid(grid):
    """Find minimum cost path in 2D grid"""
    rows, cols = len(grid), len(grid[0])
    
    # Min heap: (cost, row, col)
    pq = [(0, 0, 0)]
    # Distance matrix
    dist = [[float('inf')] * cols for _ in range(rows)]
    dist[0][0] = 0
    
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    
    while pq:
        cost, r, c = heapq.heappop(pq)
        
        if r == rows - 1 and c == cols - 1:
            return cost
        
        if cost > dist[r][c]:
            continue
        
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols:
                # Calculate new cost (problem-specific)
                new_cost = max(cost, abs(grid[nr][nc] - grid[r][c]))
                
                if new_cost < dist[nr][nc]:
                    dist[nr][nc] = new_cost
                    heapq.heappush(pq, (new_cost, nr, nc))
    
    return -1
```

### 模板 4：多源 Dijkstra ⭐⭐⭐
```python
def dijkstra_multi_source(n, edges, sources):
    """Shortest paths from multiple sources"""
    graph = collections.defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
    
    dist = [float('inf')] * n
    pq = []
    
    # Initialize all sources
    for src in sources:
        dist[src] = 0
        heapq.heappush(pq, (0, src))
    
    while pq:
        d, u = heapq.heappop(pq)
        if d > dist[u]:
            continue
        
        for v, w in graph[u]:
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                heapq.heappush(pq, (dist[v], v))
    
    return dist
```

### 模板 5：雙向 Dijkstra
```python
def dijkstra_bidirectional(n, edges, src, dst):
    """Optimize by searching from both ends"""
    graph = collections.defaultdict(list)
    reverse = collections.defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
        reverse[v].append((u, w))
    
    def dijkstra_helper(start, adj, other_dist):
        dist = [float('inf')] * n
        dist[start] = 0
        pq = [(0, start)]
        visited = set()
        min_path = float('inf')
        
        while pq:
            d, u = heapq.heappop(pq)
            if u in visited:
                continue
            visited.add(u)
            
            # Check if we can form a complete path
            if other_dist[u] != float('inf'):
                min_path = min(min_path, d + other_dist[u])
            
            for v, w in adj[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w
                    heapq.heappush(pq, (dist[v], v))
        
        return dist, min_path
    
    # Run both directions
    dist_fwd, path1 = dijkstra_helper(src, graph, [float('inf')] * n)
    dist_bwd, path2 = dijkstra_helper(dst, reverse, dist_fwd)
    
    return min(path1, path2, dist_fwd[dst])
```

## 依模式分類的題目

### **經典最短路徑題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Network Delay Time | 743 | 基本 Dijkstra | Medium |
| Path with Maximum Probability | 1514 | 最大堆積變形 | Medium |
| Find the City With Smallest Number | 1334 | 全點對最短路徑 | Medium |
| Minimum Weighted Subgraph | 2203 | 三個源頭跑 Dijkstra | Hard |
| Number of Ways to Arrive | 1976 | 數最短路徑條數 | Medium |
| Shortest Path in Binary Matrix | 1091 | 格子 Dijkstra | Medium |

### **帶限制的路徑題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Cheapest Flights Within K Stops | 787 | 狀態追蹤 | Medium |
| Minimum Cost to Reach City | 1928 | K 個中繼點 | Hard |
| Shortest Path to Get All Keys | 864 | 狀態 bitmask | Hard |
| Escape a Large Maze | 1036 | 有上限的 BFS/Dijkstra | Hard |
| Minimum Obstacle Removal | 2290 | 0-1 BFS 變形 | Hard |

### **格子題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Minimum Path Sum | 64 | DAG 格子（DP 較佳，Dijkstra 也能解） | Medium |
| Path With Minimum Effort | 1631 | 格子 Dijkstra | Medium |
| Swim in Rising Water | 778 | 最小時間路徑 | Hard |
| Minimum Cost to Make Valid Path | 1368 | 改造過的成本 | Hard |
| Shortest Path in a Grid | 1293 | K 個障礙 | Hard |
| Trap Rain Water II | 407 | 優先佇列 | Hard |

### **多源題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Find Safest Path in Grid | 2812 | 多源初始化 | Medium |
| As Far from Land as Possible | 1162 | 多源 BFS | Medium |
| Shortest Distance from All Buildings | 317 | 跑多次 Dijkstra | Hard |
| Minimum Height Trees | 310 | 找中心點 | Medium |

### **與時間／狀態相關的題**
| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Second Minimum Time to Destination | 2045 | 追蹤兩個值 | Hard |
| Reachable Nodes In Subdivided Graph | 882 | 邊的細分 | Hard |
| Minimum Time to Visit All Points | 2065 | 狀態追蹤 | Hard |
| The Maze III | 499 | 字典序路徑 | Hard |


## 詳解範例

十一道題放在 **[Dijkstra_examples.md](./Dijkstra_examples.md)**，依「狀態長什麼樣」分組 —— 因為決定實作方式的就是這件事：

| 分組 | 題目 | 狀態由什麼構成 |
|---|---|---|
| [經典單源](./Dijkstra_examples.md#classic-single-source-shortest-path) | LC 743, 1514, 1976 | 每個節點一個純量 |
| [帶限制的狀態](./Dijkstra_examples.md#constrained-state-dijkstra) | LC 787 | `(node, budget)` —— 多一個維度 |
| [格子](./Dijkstra_examples.md#grids) | LC 1631, 778, 64, 2290, 1368 | 一個格子，再加上成本是可加還是取 running max |
| [多源與隱式圖](./Dijkstra_examples.md#multi-source-and-implicit-graphs) | LC 407, 373 | 圖從來不是以邊列表給你的 |


## 決策框架

### 模式選擇策略

```text
Dijkstra Algorithm Selection Flowchart:

1. Is it a shortest path problem?
   ├── NO → Consider other algorithms (DFS, BFS, DP)
   └── YES → Continue to 2

2. Are all edge weights non-negative?
   ├── NO → Use Bellman-Ford or SPFA
   └── YES → Continue to 3

3. Single source or multiple sources?
   ├── Multiple → Use Multi-Source Dijkstra (Template 4)
   └── Single → Continue to 4

4. Is it on a graph or grid?
   ├── Grid → Use Grid-based Dijkstra (Template 3)
   └── Graph → Continue to 5

5. Any constraints (K stops, budget, time)?
   ├── YES → Use Constrained Dijkstra (Template 2)
   └── NO → Use Basic Dijkstra (Template 1)

6. Need optimization for large graphs?
   ├── YES → Consider Bidirectional Dijkstra (Template 5)
   └── NO → Use selected template from above
```

### 什麼時候用 Dijkstra、什麼時候用 BFS

| 判準 | Dijkstra | BFS |
|----------|----------|-----|
| **邊權** | 非負且不一樣 | 全部相同（無權重）或 0/1 |
| **資料結構** | 優先佇列（最小堆積） | 佇列（`LinkedList`） |
| **時間複雜度** | O((V + E) log V) | O(V + E) |
| **第一次抵達＝最短？** | ❌ 不是（必須靠 PQ 鬆弛） | ✅ 是（層數＝距離） |
| **「最小成本／權重」** | ✅ 用 Dijkstra | ❌ 會算錯 |
| **「最少步數／移動次數」** | ❌ 殺雞用牛刀 | ✅ 用 BFS |
| **成本不一的格子** | ✅ 在隱式圖上跑 Dijkstra | ❌ |
| **成本一致的格子** | ❌ 多餘的開銷 | ✅ BFS |

**決策規則**：每條邊成本都一樣（或都是 1）就用 BFS —— 更簡單而且是 O(V+E)。一旦邊有不同的非負權重，改用 Dijkstra。

**常見陷阱**：像 LC 279 Perfect Squares 或 LC 752 Open the Lock 這種所有邊成本都是 1 的題目，還硬要用 Dijkstra（PQ）—— 單純 BFS 就夠了，而且更快。

**0-1 BFS 特例**：如果邊權只有 0 或 1，用**雙端佇列** —— 權重 0 的邊往前推、權重 1 的往後推。複雜度跟 BFS 一樣是 O(V+E)，又能正確處理兩種權重。例子：LC 2290 Minimum Obstacle Removal。

### 什麼時候用 Dijkstra、什麼時候用其他演算法

| 情境 | 用 Dijkstra | 用替代方案 | 替代演算法 |
|----------|--------------|-----------------|---------------------|
| 有負權重 | ❌ | ✅ | Bellman-Ford |
| 無權重圖 | ❌ | ✅ | BFS |
| 全點對最短路徑 | ❌ | ✅ | Floyd-Warshall |
| 單源、非負 | ✅ | ❌ | - |
| 需要還原路徑 | ✅ | - | 記錄父節點 |
| 稠密圖 | ⚠️ | 可考慮 | Bellman-Ford |
| 稀疏圖 | ✅ | ❌ | - |

## 演算法比較：Dijkstra vs Floyd-Warshall vs Bellman-Ford

### 完整對照表

| 特性 | Dijkstra | Floyd-Warshall | Bellman-Ford |
|---------|----------|----------------|--------------|
| **問題類型** | 單源最短路徑 | 全點對最短路徑 | 單源最短路徑 |
| **時間複雜度** | 用堆積是 O((V+E) log V) | O(V³) | O(V·E) |
| **空間複雜度** | O(V) | O(V²) | O(V) |
| **負權重** | ❌ 不行 | ✅ 可以 | ✅ 可以 |
| **負環** | N/A | 偵測得到 | 偵測得到 |
| **實作難度** | 中等（要優先佇列） | 非常簡單（三層迴圈） | 簡單（兩層迴圈） |
| **資料結構** | 最小堆積／優先佇列 | 二維矩陣 | 邊列表 + 距離陣列 |
| **適合的圖** | 稀疏圖最合適 | 稠密圖最合適 | 都能用 |
| **輸出** | 從單一源頭出發的距離 | 全點對距離 | 從單一源頭出發的距離 |
| **可提早結束** | ✅ 到終點就能停 | ❌ 必須跑完 | ❌ 必須跑 V-1 輪 |
| **最佳使用情境** | 大型稀疏圖、單源 | 小型完全圖、全點對 | 負權重、環偵測 |
| **最不利的圖** | 稠密圖 | 超大圖 | 邊很多的稠密圖 |

### 各演算法的使用時機

```text
Shortest Path Algorithm Selection:

1. What type of problem?
   ├── All-pairs shortest path? → Continue to 2
   │   ├── Small graph (V ≤ 400)? → Use Floyd-Warshall
   │   └── Large graph? → Run Dijkstra V times (or Johnson's algorithm)
   │
   └── Single-source shortest path? → Continue to 3

2. Are edge weights non-negative?
   ├── YES → Use Dijkstra (most efficient)
   │   ├── Sparse graph? → Dijkstra with binary heap: O((V+E) log V)
   │   └── Dense graph? → Consider array-based: O(V²)
   │
   └── NO (has negative weights) → Use Bellman-Ford
       └── Need cycle detection? → Bellman-Ford explicitly detects

3. Special cases:
   ├── Unweighted graph? → Use BFS: O(V+E)
   ├── Tree structure? → Use DFS/BFS: O(V)
   ├── Grid-based? → Dijkstra on implicit graph
   └── Transitive closure? → Floyd-Warshall (boolean variant)
```

### 實務比較範例

**範例 1：社群網路（1000 位使用者、5000 條好友關係）**
- **單源（求某位使用者到其他人的距離）：**
  - Dijkstra：約 5000 × log(1000) ≈ 50,000 次操作 ⚡ **最佳選擇**
  - Bellman-Ford：1000 × 5000 = 5,000,000 次操作
  - Floyd-Warshall：1000³ = 1,000,000,000 次操作

- **全點對（所有使用者兩兩之間的距離）：**
  - Dijkstra × V：50,000 × 1000 = 50,000,000 次操作 ⚡ **最佳選擇**
  - Floyd-Warshall：1,000,000,000 次操作（但程式碼比較簡單）

**範例 2：小型完全圖（50 個節點、兩兩相連）**
- **全點對最短路徑：**
  - Floyd-Warshall：50³ = 125,000 次操作 ⚡ **最佳選擇**（最簡單）
  - Dijkstra × V：約 2500 × log(50) × 50 = 約 500,000 次操作

**範例 3：貨幣兌換與套利偵測**
- **偵測負環（套利機會）：**
  - Bellman-Ford：O(V·E) ⚡ **最佳選擇**（直接偵測得到）
  - Floyd-Warshall：O(V³)，檢查對角線（順便解全點對）
  - Dijkstra：❌ 處理不了負權重

### 效能實測參考

| 圖規模 | 邊數 | Dijkstra（單源） | Dijkstra（全點對） | Floyd-Warshall | Bellman-Ford |
|------------|-------|-------------------|----------------------|----------------|--------------|
| V=100, 稀疏 | 500 | 0.01ms | 1ms | 10ms ⚡ | 5ms |
| V=100, 稠密 | 5000 | 0.1ms | 10ms ⚡ | 10ms | 50ms |
| V=500, 稀疏 | 2500 | 0.05ms | 25ms ⚡ | 1.25s | 125ms |
| V=500, 稠密 | 125K | 2ms | 1s | 1.25s ⚡ | 6.25s |
| V=1000, 稀疏 | 5000 | 0.1ms | 100ms ⚡ | 10s | 500ms |

*（時間為概略值，假設實作有經過最佳化）*

### 演算法選擇矩陣

| 你的情況 | 建議演算法 | 原因 |
|----------------|----------------------|-----|
| 在路網中找 A 到 B 的最短路徑 | **Dijkstra** | 單源、非負、可提早結束 |
| 找小型網路（≤300 節點）的中心 | **Floyd-Warshall** | 要全點對、圖小、程式碼簡單 |
| 有即時路況的城市路徑規劃（動態成本） | **Dijkstra**（重跑） | 即時更新、單源 |
| 檢查先修關係鏈存不存在 | **Floyd-Warshall** | 遞移閉包、圖小 |
| 貨幣套利偵測 | **Bellman-Ford** | 需要偵測負環 |
| 社群網路 —— 幾度分隔 | **BFS**（無權重時） | 無權重、單源 |
| 最小生成樹 | **Prim's/Kruskal's** | 根本是另一個問題 |
| 格子上的遊戲尋路 | **Dijkstra** 或 **A*** | 稀疏格子、有啟發函數可用 |

## 總結與速查

### 複雜度速查
| 實作方式 | 時間複雜度 | 空間複雜度 | 備註 |
|----------------|-----------------|------------------|-------|
| 陣列版 | O(V²) | O(V) | 稠密圖適用 |
| 二元堆積 | O((V+E)logV) | O(V) | 最常見 |
| 費氏堆積 | O(E + VlogV) | O(V) | 理論最佳 |
| 格子版 | O(RC log(RC)) | O(RC) | R=列數、C=行數 |

### 模板速查
| 模板 | 最適合 | 關鍵程式碼模式 |
|----------|----------|------------------|
| 基本 | 標準最短路徑 | `heapq.heappop(pq)` → 鬆弛邊 |
| 帶限制 | K 次轉機、預算上限 | 追蹤狀態：`(cost, node, constraint)` |
| 格子 | 二維矩陣題 | 四方向移動 |
| 多源 | 多個起點 | 一開始就把所有源頭放進去 |
| 雙向 | 大型圖 | 從兩端同時搜 |

### 常見模式與小技巧

#### **優先佇列的狀態**
```python
# Basic state
(distance, node)

# With constraints
(cost, node, stops_remaining)

# Grid problems
(cost, row, col)

# With path tracking
(distance, node, path)
```

#### **走訪集合的最佳化**
```python
# Option 1: Check after pop (recommended)
if node in visited:
    continue
visited.add(node)

# Option 2: Check distance
if d > dist[node]:
    continue
```

#### **路徑還原**
```python
parent = {}
# During relaxation:
parent[v] = u

# Reconstruct path:
path = []
while node != source:
    path.append(node)
    node = parent[node]
path.reverse()
```

### 解題步驟
1. **辨認圖的結構**：邊是明給的，還是隱式的（格子）？
2. **檢查限制**：權重非負嗎？是單源嗎？
3. **選模板**：基本、帶限制、格子，還是多源？
4. **定義狀態**：優先佇列裡要追蹤什麼？
5. **實作鬆弛**：距離要怎麼更新？
6. **處理終止條件**：什麼時候停？要回傳什麼值？

---

## 相似 LeetCode 題目參考

### 格子類題目
| LC # | 標題 | 移動方向 | 關鍵特性 | 主要解法 | 其他解法 | 需要 dist[][]？ |
|------|-------|----------|-------------|----------|---------|---------|
| **64** | Minimum Path Sum | 只有 ↓→ | 可加成本 | **二維 DP** | 一維 DP、Dijkstra（殺雞用牛刀） | ❌ 不用 |
| **1631** | Path With Minimum Effort | 四方向 | 單步最大差值（不可加） | **Dijkstra** | 二分搜尋、併查集 | ✅ 要 |
| **778** | Swim in Rising Water | 四方向 | 格子值取最大 | **Dijkstra** | 併查集 | ✅ 要 |
| **1263** | Minimum Moves to Move Box | 四方向 | 推箱子機制 | **Dijkstra + 狀態** | - | ✅ 要 |
| **882** | Reachable Nodes In Subdivided Graph | 圖 | 節點細分 | **Dijkstra** | - | ✅ 要 |

**LC 1631 深入解析：**
- **現有解法**：四種主要作法（Dijkstra dist[]、Dijkstra visited、二分搜尋、併查集）
- **最常見**：Dijkstra 搭配 `dist[][]` 或 `visited[]` 陣列
- **關鍵洞見**：成本模型是 `Math.max(effort, step_diff)`，不是加總 —— 這讓 DP 不可行
- **參考檔**：`leetcode_java/src/main/java/LeetCodeJava/Graph/PathWithMinimumEffort.java`（V0-V4.3）

### 經典最短路徑題目
| LC # | 標題 | 類型 | 關鍵特性 |
|------|-------|------|-------------|
| **743** | Network Delay Time | 圖 | 廣播延遲 |
| **787** | Cheapest Flights K Stops | 圖 | K 次轉機限制 |
| **1514** | Path with Maximum Probability | 圖 | 最大化機率 |
| **1928** | Minimum Cost to Reach Destination | 加權圖 | K 個中繼點 |

### 多源最短路徑
| LC # | 標題 | 關鍵特性 |
|------|-------|-------------|
| **1162** | As Far from Land as Possible | 多源 BFS-Dijkstra |
| **2812** | Find the Safest Path | 格子上的多源 |
| **2290** | Minimum Obstacle Removal | 0-1 BFS 變形 |

### 主要實作檔案
- **Java 參考**：`leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/MinimumPathSum.java`
  - V0：Dijkstra 搭配 dist[][]（能過，但殺雞用牛刀）
  - V0-0-1、V1、V2：純 DP 作法（LC 64 的最佳解）
- **Python 參考**：`leetcode_python/Dynamic_Programming/minimum-path-sum.py`
  - V0-1、V0-2：Dijkstra（最小堆積 + `cost_grid[][]`）—— 見 [7) LC 64](./Dijkstra_examples.md#7-minimum-path-sum--lc-64--a-dag-grid-where-dp-is-optimal)
  - V1、V2：原地 DP `O(1)` 空間／一維滾動列（LC 64 的最佳解）
  
---

### 常見錯誤與提醒

**🚫 常見錯誤：**
- 忘了檢查是否已走訪
- 對負權重硬套 Dijkstra
- 沒用優先佇列（用了普通佇列）
- 帶限制的題目狀態比較寫錯
- 沒處理不連通的分量

**✅ 最佳實務：**
- 優先佇列一律用最小堆積
- 記錄走訪過的節點，避免重複處理
- 除了源點，其他距離一律初始化成無限大
- 想清楚該用距離陣列還是走訪集合
- 處理好邊界情況（空圖、無路可達）

### 面試提示
1. **先問清楚限制**：一定要問有沒有負權重
2. **主動講複雜度**：一開始就把時間／空間複雜度說出來
3. **解釋鬆弛**：更新距離是整個演算法的核心概念
4. **提到替代方案**：說明什麼時候 BFS 或 Bellman-Ford 更適合
5. **需要就再最佳化**：大型圖可以聊聊雙向搜尋

### 相關主題
- **BFS**：無權重的最短路徑
- **[Bellman-Ford](./Bellman-Ford.md)**：能處理負權重（詳細比較見上）
- **[Floyd-Warshall](./Floyd-Warshall.md)**：全點對最短路徑（詳細比較見上）
- **A* 演算法**：由啟發函數引導的搜尋
- **SPFA**：以佇列最佳化的 Bellman-Ford 變形
- **Johnson's Algorithm**：用重新賦權技巧解全點對
