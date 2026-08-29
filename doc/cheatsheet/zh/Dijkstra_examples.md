# Dijkstra — 實作範例

> **範圍** — [Dijkstra.md](./Dijkstra.md) 背後的解題實作庫：十一題、兩種語言，依「搜尋狀態長什麼樣」分組，因為那正是決定你需要 `dist[]`、需要第二個狀態維度、還是一個單純 `visited[]` 的關鍵。
> **另見**：[Dijkstra.md](./Dijkstra.md) — 母文件：五個模板、兩個判斷問題與演算法比較；[Bellman-Ford.md](./Bellman-Ford.md) — 邊權可以是負的時候；[Floyd-Warshall.md](./Floyd-Warshall.md) — 全點對最短路徑；[shortest_path_comparison.md](./shortest_path_comparison.md) — 三者之間怎麼挑；[bfs.md](./bfs.md) — 無權重的情況與 0-1 BFS；[heap.md](./heap.md) — 撐起這一切的優先佇列。

## LeetCode 題目清單

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph](https://leetcode.com/problem-list/graph/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## 總覽

這裡是 [Dijkstra.md](./Dijkstra.md) 的長尾。母文件收模板，以及決定用哪個模板的那兩個問題；這份文件收*實際套用*這些模板的題目。

### 關鍵性質
- **複雜度**：除非該解法另外註明，一律是用二元堆積的 O(E log V)；0-1 BFS 那幾題是 O(V + E)
- **核心想法**：演算法本身從來沒變 — 變的是「一個節點」代表什麼，底下的分組就是按照狀態離「單純一個節點」有多遠來排的
- **什麼時候用**：當母文件那兩個問題已經告訴你，這題根本上就是 Dijkstra 的形狀之後


## 經典單源最短路徑

### 1) Network Delay Time — LC 743

> 從來源 k 跑 Dijkstra（戴克斯特拉）；答案是所有最短距離的最大值，若有任何節點到不了就回傳 -1。

```java
// LC 743 - Network Delay Time
// IDEA: Dijkstra from source k; max shortest dist = time for signal to reach all nodes
// time = O((V+E) log V), space = O(V+E)
public int networkDelayTime(int[][] times, int n, int k) {
    Map<Integer, List<int[]>> graph = new HashMap<>();
    for (int[] t : times) graph.computeIfAbsent(t[0], x -> new ArrayList<>()).add(new int[]{t[1], t[2]});
    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, k});
    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int d = cur[0], u = cur[1];
        if (d > dist[u]) continue;
        for (int[] e : graph.getOrDefault(u, new ArrayList<>())) {
            if (dist[u] + e[1] < dist[e[0]]) { dist[e[0]] = dist[u] + e[1]; pq.offer(new int[]{dist[e[0]], e[0]}); }
        }
    }
    int max = 0;
    for (int i = 1; i <= n; i++) { if (dist[i] == Integer.MAX_VALUE) return -1; max = Math.max(max, dist[i]); }
    return max;
}
```

```python
# LC 743 - Network Delay Time
# IDEA: Dijkstra (min-heap PQ + BFS)
# time = O((V+E) log V), space = O(V+E)
import heapq
from collections import defaultdict

class Solution(object):
    def networkDelayTime(self, times, n, k):
        graph = defaultdict(list)
        for u, v, w in times:
            graph[u].append((v, w))

        heap = [(0, k)]   # (accumulated_time, node); start at source k with cost 0
        dist = {}         # dist[node] = finalized shortest time; acts as visited set

        while heap:
            time, node = heapq.heappop(heap)

            if node in dist:   # already finalized — skip stale entry
                continue

            dist[node] = time  # first pop = shortest time (min-heap guarantee)

            for nei, w in graph[node]:
                if nei not in dist:
                    # KEY INSIGHT: push (time + w, nei) — the ACCUMULATED path cost.
                    # Because path cost is carried inside the heap entry itself,
                    # we never need to separately check if nei is reachable;
                    # the cost already reflects the full path from source.
                    #
                    # heapq.heappop always extracts the MINIMUM accumulated cost next,
                    # so the first time we pop a node its distance is globally optimal
                    # — that is the Dijkstra guarantee (min-heap + BFS).
                    heapq.heappush(heap, (time + w, nei))

        return max(dist.values()) if len(dist) == n else -1
```

```python
# LC 743 - Network Delay Time (visited-set variant — equivalent, slightly more explicit)
# IDEA: Dijkstra — same guarantee, uses an explicit visited set instead of dist-dict
import heapq
from collections import defaultdict

class Solution(object):
    def networkDelayTime(self, times, n, k):
        graph = defaultdict(list)
        for u, v, w in times:
            graph[u].append((v, w))

        min_heap = [(0, k)]
        visited = set()

        while min_heap:
            time, node = heapq.heappop(min_heap)
            if node in visited:
                continue
            visited.add(node)

            if len(visited) == n:   # all nodes finalized — answer is current time
                return time

            for neighbor, weight in graph[node]:
                if neighbor not in visited:
                    heapq.heappush(min_heap, (time + weight, neighbor))

        return -1
```

### 2) Path with Maximum Probability — LC 1514 — 以機率為 key 的最大堆積


> 用最大堆積版的 Dijkstra，把邊上的機率相乘；起點是 1.0，目標是讓抵達機率最大。

**核心想法 — 為什麼需要 `best[]`（也就是 `max_prob[]`）：**
```text
Suppose:
  0 --0.5--> 1
   \         ^
    \       /
    0.9   0.8
      \   /
        2

From node 0 directly: 0 -> 1 = 0.5

Later we discover: 0 -> 2 -> 1 = 0.9 × 0.8 = 0.72   ← better!

If we don't store the best probability found so far per node, we'll either:
  - reprocess the same node many unnecessary times, or
  - miss a better path entirely (if we stop at the first probability found).
```
這其實就是標準 Dijkstra 那套 `dist[node]` 剪枝，只是反過來：不是檢查 `dist[u] + w < dist[v]`（讓總和最小），而是檢查 `prob[u] * edge_prob > prob[v]`（讓乘積最大）。要用**最大堆積**（把機率取負，因為 `heapq` 預設是最小堆積），至於堆積裡那些過期／已被更好的值取代的項目，用 `if prob < best[node]: continue` 跳過。

```java
// java
// LC 1514
// IDEA: Modified Dijkstra (max-heap, multiply probabilities instead of adding distances)
// NOTE: Use MAX heap since we want maximum probability
// NOTE: Initialize probabilities to -1 (unreachable), source to 1
class Solution {
    public double maxProbability(int n, int[][] edges, double[] succProb, int start, int end) {
        List<double[]>[] graph = new LinkedList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new LinkedList<>();
        }
        for (int i = 0; i < edges.length; i++) {
            graph[edges[i][0]].add(new double[]{edges[i][1], succProb[i]});
            graph[edges[i][1]].add(new double[]{edges[i][0], succProb[i]});
        }

        double[] proTo = new double[n];
        Arrays.fill(proTo, -1);
        proTo[start] = 1;

        // NOTE: MAX heap (compare b vs a)
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(b[1], a[1]));
        pq.offer(new double[]{start, 1});

        while (!pq.isEmpty()) {
            double[] cur = pq.poll();
            int curId = (int) cur[0];
            double curProb = cur[1];

            if (curId == end) return curProb;
            if (proTo[curId] > curProb) continue;

            for (double[] next : graph[curId]) {
                int nextId = (int) next[0];
                double newProb = proTo[curId] * next[1];
                if (newProb > proTo[nextId]) {
                    proTo[nextId] = newProb;
                    pq.offer(new double[]{nextId, newProb});
                }
            }
        }
        return 0;
    }
}
```

```python
# python
# LC 1514
# IDEA: Modified Dijkstra with max-heap (negate probability for max behavior)
import heapq
import collections

class Solution:
    def maxProbability(self, n, edges, succProb, start_node, end_node):
        graph = collections.defaultdict(list)
        for i, (u, v) in enumerate(edges):
            graph[u].append((v, succProb[i]))
            graph[v].append((u, succProb[i]))

        # Max-heap: negate probability since heapq is min-heap
        pq = [(-1.0, start_node)]
        dist = [0.0] * n
        dist[start_node] = 1.0

        while pq:
            neg_prob, u = heapq.heappop(pq)
            prob = -neg_prob

            if u == end_node:
                return prob
            if prob < dist[u]:
                continue

            for v, w in graph[u]:
                new_prob = prob * w
                if new_prob > dist[v]:
                    dist[v] = new_prob
                    heapq.heappush(pq, (-new_prob, v))

        return 0.0
```

**其他解法（LC 1514 不用優先佇列也解得掉）：**

因為邊權（機率）都非負，而且我們是求乘積最大而非總和最小，這題也能用 **Bellman-Ford** 和 **SPFA** 解 — 面試官若追問 Dijkstra 以外的做法，這兩個就派上用場。

```python
# V2-1: Bellman-Ford — relax all edges (both directions since undirected) up to n-1 times
# time = O(V * E), space = O(V)
class Solution:
    def maxProbability(self, n, edges, succProb, start, end):
        max_prob = [0] * n
        max_prob[start] = 1

        for i in range(n - 1):
            has_update = 0
            for j in range(len(edges)):
                u, v = edges[j]
                path_prob = succProb[j]
                if max_prob[u] * path_prob > max_prob[v]:
                    max_prob[v] = max_prob[u] * path_prob
                    has_update = 1
                if max_prob[v] * path_prob > max_prob[u]:
                    max_prob[u] = max_prob[v] * path_prob
                    has_update = 1
            # early exit: no larger probability found this round -> converged
            if not has_update:
                break

        return max_prob[end]
```

```python
# V2-2: SPFA (Shortest Path Faster Algorithm) — queue-based Bellman-Ford variant
# time = O(V * E) worst case, often much faster in practice, space = O(V + E)
class Solution:
    def maxProbability(self, n, edges, succProb, start, end):
        graph = defaultdict(list)
        for i, (a, b) in enumerate(edges):
            graph[a].append([b, succProb[i]])
            graph[b].append([a, succProb[i]])

        max_prob = [0.0] * n
        max_prob[start] = 1.0

        queue = deque([start])
        while queue:
            cur_node = queue.popleft()
            for nxt_node, path_prob in graph[cur_node]:
                # only enqueue nxt_node if this path IMPROVES its probability
                if max_prob[cur_node] * path_prob > max_prob[nxt_node]:
                    max_prob[nxt_node] = max_prob[cur_node] * path_prob
                    queue.append(nxt_node)

        return max_prob[end]
```

| 解法 | 時間 | 空間 | 備註 |
|----------|------|-------|-------|
| **Dijkstra（最大堆積）** | O((V+E) log V) | O(V+E) | 一般情況最佳選擇；`end` 一被 pop 出來就能提早結束 |
| **Bellman-Ford** | O(V·E) | O(V) | 單純的巢狀迴圈，不用堆積；不准用 PQ 時的好備案 |
| **SPFA** | 最壞 O(V·E)，一般情況更快 | O(V+E) | 用佇列取代堆積；跟 0-1 BFS 同一個想法，只是套在帶權鬆弛上 |

### 3) Number of Ways to Arrive at Destination — LC 1976 — Dijkstra + 路徑計數


> 標準 Dijkstra；在記錄最短距離的同時，順便記錄每個節點的最短路徑條數。

```java
// java
// LC 1976
// IDEA: Dijkstra + count paths
// NOTE: Track both shortest distance AND number of ways to reach each node
class Solution {
    public int countPaths(int n, int[][] roads) {
        int MOD = 1_000_000_007;
        List<long[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();

        for (int[] r : roads) {
            graph[r[0]].add(new long[]{r[1], r[2]});
            graph[r[1]].add(new long[]{r[0], r[2]});
        }

        long[] dist = new long[n];
        long[] ways = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;
        ways[0] = 1;

        // (distance, node)
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.offer(new long[]{0, 0});

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long d = cur[0];
            int u = (int) cur[1];

            if (d > dist[u]) continue;

            for (long[] next : graph[u]) {
                int v = (int) next[0];
                long w = next[1];

                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    ways[v] = ways[u];
                    pq.offer(new long[]{dist[v], v});
                } else if (dist[u] + w == dist[v]) {
                    ways[v] = (ways[v] + ways[u]) % MOD;
                }
            }
        }

        return (int) (ways[n - 1] % MOD);
    }
}
```

```python
# python
# LC 1976
# IDEA: Dijkstra + count shortest paths
import heapq
import collections

class Solution:
    def countPaths(self, n, roads):
        MOD = 10**9 + 7
        graph = collections.defaultdict(list)
        for u, v, w in roads:
            graph[u].append((v, w))
            graph[v].append((u, w))

        dist = [float('inf')] * n
        ways = [0] * n
        dist[0] = 0
        ways[0] = 1
        pq = [(0, 0)]  # (distance, node)

        while pq:
            d, u = heapq.heappop(pq)
            if d > dist[u]:
                continue
            for v, w in graph[u]:
                if dist[u] + w < dist[v]:
                    dist[v] = dist[u] + w
                    ways[v] = ways[u]
                    heapq.heappush(pq, (dist[v], v))
                elif dist[u] + w == dist[v]:
                    ways[v] = (ways[v] + ways[u]) % MOD

        return ways[n - 1] % MOD
```

## 帶狀態限制的 Dijkstra

### 4) Cheapest Flights Within K Stops — LC 787 — 二維狀態 ⭐⭐⭐⭐


> ⚠️ 這**不是**標準 Dijkstra。K 站中轉這個限制，替狀態多加了一個維度。
> 在這裡用標準的 `dist[node]` 剪枝是**錯的** — 用不同中轉次數抵達同一個節點，是不同的合法狀態。

**核心想法：**
- 狀態：`(cost, node, stops_used)` — stops_used 也是狀態身分的一部分
- 剪枝：用 `best[(node, stops)] <= cost` 取代 `dist[node] <= cost`
- 為什麼：用 1 次中轉花 900 抵達節點 A，跟用 2 次中轉花 100 抵達節點 A，兩者**都**合法；丟掉任何一個都會算出錯的答案

```python
# LC 787 - Cheapest Flights Within K Stops
# IDEA: Constrained Dijkstra — 2D state (node, stops)
# time = O(E * K * log(E * K)), space = O(E * K)
import heapq
from collections import defaultdict

class Solution(object):
    def findCheapestPrice(self, n, flights, src, dst, K):
        graph = defaultdict(list)
        for s, e, c in flights:
            graph[s].append((e, c))

        # (cost, node, stops_used)
        heap = [(0, src, 0)]

        # KEY: best[(node, stops)] = min cost to reach node using exactly 'stops' edges
        # This is a 2D map, NOT a 1D dist[] array
        # Reason: same node at different stop counts are DIFFERENT states
        best = {}

        while heap:
            cost, node, stops = heapq.heappop(heap)

            # First pop of destination is optimal (min-heap guarantee)
            if node == dst:
                return cost

            # Constraint exceeded — prune
            if stops > K:
                continue

            # 2D pruning: skip if (node, stops) already seen cheaper
            if (node, stops) in best and best[(node, stops)] <= cost:
                continue
            best[(node, stops)] = cost

            for nei, price in graph[node]:
                heapq.heappush(heap, (cost + price, nei, stops + 1))

        return -1
```

```java
// LC 787 - Cheapest Flights Within K Stops
// IDEA: Constrained Dijkstra with 2D state (node, stops)
// time = O(E * K * log(E * K)), space = O(E * K)
public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
    Map<Integer, List<int[]>> graph = new HashMap<>();
    for (int[] f : flights)
        graph.computeIfAbsent(f[0], x -> new ArrayList<>()).add(new int[]{f[1], f[2]});

    // [cost, node, stops_used]
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, src, 0});

    // best[node][stops] = min cost to reach node using exactly 'stops' edges
    // 2D array replaces the 1D dist[] used in standard Dijkstra
    int[][] best = new int[n][k + 2];
    for (int[] row : best) Arrays.fill(row, Integer.MAX_VALUE);
    best[src][0] = 0;

    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int cost = cur[0], u = cur[1], stops = cur[2];
        if (u == dst) return cost;
        if (stops > k) continue;
        for (int[] e : graph.getOrDefault(u, new ArrayList<>())) {
            int newCost = cost + e[1];
            if (newCost < best[e[0]][stops + 1]) {
                best[e[0]][stops + 1] = newCost;
                pq.offer(new int[]{newCost, e[0], stops + 1});
            }
        }
    }
    return -1;
}
```

**為什麼 `dist[node]` 剪枝會失效（具體追蹤）：**
```text
n=4, flights: 0→1(100), 0→2(500), 1→2(100), 2→3(10), 1→3(800), src=0, dst=3, K=2

Standard dist[] approach:
  Pop (0, node=0, stops=0) → expand neighbors
  Pop (100, node=1, stops=1) → dist[1]=100, expand neighbors
  Push (900, node=3, stops=2) and (200, node=2, stops=2)
  Pop (200, node=2, stops=2) → dist[2]=200, expand neighbors
  Push (210, node=3, stops=3)  ← stops=3 > K=2, pruned!
  Pop (500, node=2, stops=1) → dist[2]=200 < 500 → SKIP ← standard pruning fires
  ...but we needed to reach node=2 at stops=1 to then reach node=3 at stops=2!

With best[(node, stops)]:
  best[(1,1)]=100, best[(2,2)]=200, best[(2,1)]=500 are all DIFFERENT states
  Path 0→2→3 = 500+10=510 at stops=2 is correctly explored
  Answer: 510 (not 210 since that needs 3 stops)
```

## 網格

### 5) Path With Minimum Effort — LC 1631 — 網格上的極小化極大 ⭐⭐⭐⭐


> 讓路徑上最大的絕對差值最小；用最小堆積，以 effort 當優先權 key。

```java
// java
// LC 1631

// V0-1
// IDEA: Dijkstra's ALGO ( fixed by gpt) : min PQ + BFS
public int minimumEffortPath_0_1(int[][] heights) {
    if (heights == null || heights.length == 0)
        return 0;

    int rows = heights.length;
    int cols = heights[0].length;

    // Min-heap: [effort, x, y]
    PriorityQueue<int[]> minPQ = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
    minPQ.offer(new int[] { 0, 0, 0 }); // effort, x, y

    boolean[][] visited = new boolean[rows][cols];
    int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    while (!minPQ.isEmpty()) {
        int[] cur = minPQ.poll();
        int effort = cur[0], x = cur[1], y = cur[2];

        if (x == rows - 1 && y == cols - 1) {
            return effort;
        }

  /**  NOTE !!!  need `visited, to NOT revisited visited cells (`Dijkstra algo`)
   *
   *   Reason:
   *
   *
   *   Great question — and you’re absolutely right to raise this.
   *
   * ✅ Short Answer:
   *
   * Yes, in Dijkstra’s algorithm for the “minimum effort path” problem,
   * we still need a visited check — but only after the shortest
   * effort to a cell has been finalized.
   *
   * That is:
   *    •   Once we’ve popped a cell (x, y) from the priority queue,
   *        the effort it took to reach it is `guaranteed` to be `minimal`,
   *        due to how the min-heap works.
   *
   *    •   After that point, there’s `NO need` to `revisit` that cell —
   *        any future path that reaches (x, y) will have equal or greater effort,
   *        and can be safely ignored.
   *
   * This is different from classic BFS where all edges are equal weight —
   * but in Dijkstra, this greedy behavior is valid and optimal.
   *
   * ⸻
   *
   * 🤔 Why Not Revisit?
   *
   * Let’s break it down:
   *
   * In Dijkstra:
   *    •   The min-heap (priority queue) guarantees that we always expand the least effort path so far.
   *    •   If a cell is reached for the first time, it’s the best effort you’ll ever see to reach it.
   *    •   If you allow revisiting, you’ll reprocess worse paths and slow down the algorithm.
   *
   * ⸻
   *
   * 📌 Exception:
   *
   * If you were doing plain BFS with no heap, or non-Dijkstra variants,
   * you’d need to revisit when a better cost is found later (like in Bellman-Ford).
   * But with Dijkstra and a correct min-heap structure,
   * no revisits are necessary after finalization.
   *
   * ⸻
   *
   * ✅ Key Rule:
   *
   * In Dijkstra:
   * Once you pop a node (x, y) from the min-heap and mark it visited,
   * you do not need to revisit it — its shortest (or in this case, minimum effort) path is finalized.
   *
   */
  if (visited[x][y]) {
            continue;
        }

        visited[x][y] = true;

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (nx >= 0 && ny >= 0 && nx < rows && ny < cols && !visited[nx][ny]) {
                int newEffort = Math.max(effort, Math.abs(heights[nx][ny] - heights[x][y]));
                minPQ.offer(new int[] { newEffort, nx, ny });
            }
        }
    }

    return -1; // Should never reach here if input is valid
}
```


#### LC 1631 的幾種 Dijkstra 寫法

##### **寫法 1：用 dist[][] 陣列（推薦）**
```java
// dist[r][c] stores minimum cost found so far to reach (r,c)
public int minimumEffortPath(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
    pq.offer(new int[]{0, 0, 0}); // {row, col, effort}
    dist[0][0] = 0;
    
    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    
    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int r = cur[0], c = cur[1], effort = cur[2];
        
        // Destination check
        if (r == m-1 && c == n-1) return effort;
        
        // Skip if already found better path
        if (effort > dist[r][c]) continue;
        
        // Explore neighbors
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                int nextEffort = Math.max(effort, Math.abs(heights[nr][nc] - heights[r][c]));
                if (nextEffort < dist[nr][nc]) {
                    dist[nr][nc] = nextEffort;
                    pq.offer(new int[]{nr, nc, nextEffort});
                }
            }
        }
    }
    return -1;
}
```
**為什麼可行**：`dist[][]` 的那句 `if (effort > dist[r][c]) continue;` 會自動略過任何比目前最佳解更差的路徑。

##### **寫法 2：用 visited[] 陣列**
```java
// visited[] marks cells whose minimum effort is finalized
public int minimumEffortPath_visited(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    boolean[][] visited = new boolean[m][n];
    
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
    pq.offer(new int[]{0, 0, 0}); // {effort, row, col}
    
    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    
    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int effort = cur[0], r = cur[1], c = cur[2];
        
        if (r == m-1 && c == n-1) return effort;
        
        // Once visited, we have minimum effort (thanks to min-heap)
        if (visited[r][c]) continue;
        visited[r][c] = true;
        
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && !visited[nr][nc]) {
                int nextEffort = Math.max(effort, Math.abs(heights[nr][nc] - heights[r][c]));
                pq.offer(new int[]{nextEffort, nr, nc});
            }
        }
    }
    return -1;
}
```
**為什麼 visited 可行**：最小堆積保證某個格子第一次被 pop 出來時，帶的就是最佳 effort，所以標記成 visited 就不會重複處理。

##### **兩種寫法比較**
| 解法 | 空間 | 邏輯 | 適用時機 |
|----------|-------|-------|----------|
| **dist[][]** | 額外 O(m×n) | 跟目前已知最佳值比較 | 會多次更新的時候 |
| **visited[]** | 額外 O(m×n) | 標記為已定案 | 邏輯較單純、提早結束較快 |


#### LC 1631 的其他解法

##### **解法 3：二分搜尋 + DFS**
```java
// Binary search on effort + DFS to check if reachable
public int minimumEffortPath_binarySearch(int[][] heights) {
    int lo = 0, hi = 1_000_000;
    
    while (lo < hi) {
        int mid = (lo + hi) / 2;
        if (canReach(heights, mid)) {
            hi = mid;
        } else {
            lo = mid + 1;
        }
    }
    return lo;
}

private boolean canReach(int[][] h, int limit) {
    int m = h.length, n = h[0].length;
    boolean[][] visited = new boolean[m][n];
    return dfs(h, 0, 0, limit, visited);
}

private boolean dfs(int[][] h, int r, int c, int limit, boolean[][] visited) {
    if (r < 0 || r >= h.length || c < 0 || c >= h[0].length || visited[r][c])
        return false;
    
    visited[r][c] = true;
    if (r == h.length-1 && c == h[0].length-1) return true;
    
    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    for (int[] d : dirs) {
        int nr = r + d[0], nc = c + d[1];
        if (nr >= 0 && nr < h.length && nc >= 0 && nc < h[0].length) {
            if (Math.abs(h[nr][nc] - h[r][c]) <= limit && dfs(h, nr, nc, limit, visited)) {
                return true;
            }
        }
    }
    return false;
}
```
**時間**：O((V+E) × log(maxH)) | **空間**：O(V)

##### **解法 4：併查集（Kruskal 演算法）**
```java
// Build graph as edges, sort by weight, union until src-dest connected
public int minimumEffortPath_unionFind(int[][] heights) {
    int m = heights.length, n = heights[0].length;
    List<int[]> edges = new ArrayList<>();
    
    // Build all edges
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (i > 0) // edge down
                edges.add(new int[]{i*n+j, (i-1)*n+j, Math.abs(heights[i][j]-heights[i-1][j])});
            if (j > 0) // edge right
                edges.add(new int[]{i*n+j, i*n+j-1, Math.abs(heights[i][j]-heights[i][j-1])});
        }
    }
    
    // Sort edges by effort (Kruskal's principle)
    edges.sort((a, b) -> a[2] - b[2]);
    
    UnionFind uf = new UnionFind(m * n);
    int src = 0, dst = m*n - 1;
    
    for (int[] edge : edges) {
        uf.union(edge[0], edge[1]);
        if (uf.find(src) == uf.find(dst)) {
            return edge[2]; // Return effort when src-dst first connected
        }
    }
    return 0;
}

class UnionFind {
    int[] parent, rank;
    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }
    
    int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }
    
    void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;
        if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
    }
}
```
**時間**：O((V+E) log(V+E)) = O(m×n × log(m×n)) | **空間**：O(m×n)


#### LC 1631 該挑哪個解法
| 解法 | 優點 | 缺點 | 什麼時候用 |
|----------|------|------|-----------|
| **Dijkstra + dist[][]** | 最直覺、最標準 | 多花空間 | 想要經典的 Dijkstra 寫法 |
| **Dijkstra + visited[]** | 提早結束更單純 | 彈性較低 | 只需要求出最小 effort |
| **二分搜尋 + DFS** | 某些情況記憶體用得較少 | 較慢（要反覆 DFS） | 記憶體是關鍵限制 |
| **併查集** | 從圖的角度看很優雅 | 實作較複雜 | 練併查集 |


### 6) Swim in Rising Water — LC 778 — 網格上的極小化極大


> 最小堆積，優先權 = 到目前為止看過的最大高度；答案 = 抵達右下角的時間。

```java
// java
// LC 778
// IDEA: Dijkstra (min PQ + BFS on grid)
// NOTE: Track max elevation along path (not sum of weights)
public int swimInWater(int[][] grid) {
    int n = grid.length;
    PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
    boolean[][] visited = new boolean[n][n];

    minHeap.offer(new int[]{grid[0][0], 0, 0});
    visited[0][0] = true;

    int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    int res = 0;

    while (!minHeap.isEmpty()) {
        int[] cur = minHeap.poll();
        int elevation = cur[0], x = cur[1], y = cur[2];

        // NOTE: track MAX elevation along path
        res = Math.max(res, elevation);

        if (x == n - 1 && y == n - 1) return res;

        for (int[] d : directions) {
            int nx = x + d[0], ny = y + d[1];
            if (nx >= 0 && ny >= 0 && nx < n && ny < n && !visited[ny][nx]) {
                visited[ny][nx] = true;
                minHeap.offer(new int[]{grid[ny][nx], nx, ny});
            }
        }
    }
    return -1;
}
```

### 7) Minimum Path Sum — LC 64 — DAG 網格，DP 才是最佳解


> 只能往右／往下走，讓路徑上的總和最小。Dijkstra **可以動**，但這個網格是個 **DAG**，所以單純的 DP 嚴格來說更好。這題很適合拿來看清楚 Dijkstra 到底幫你買到了什麼 — 以及沒買到什麼。
> 參考：`leetcode_python/Dynamic_Programming/minimum-path-sum.py`（V0-1 / V0-2 = Dijkstra，V1 / V2 = DP）

#### **1) 核心想法**

```text
Dijkstra = BFS + Priority Queue (min-heap)

Treat each cell (r, c) as a graph node.
Edges: (r,c) -> (r,c+1) and (r,c) -> (r+1,c), edge weight = grid[next cell]
Answer = shortest path from (0,0) to (m-1,n-1)
```

- **成本模型是可加的**（`new_cost = curr_cost + grid[nr][nc]`）而且**所有權重 ≥ 0** → Dijkstra 成立。
- **貪婪保證**：最小堆積永遠會 pop 出全域最便宜的邊界格子，所以**終點第一次被 pop 出來時就是答案** — 直接回傳，不用把堆積清空。
- **`cost_grid[r][c]`**（也就是「dist」陣列）= *目前為止*抵達 `(r,c)` 的最佳成本。它身兼**兩個**角色：
  1. **鬆弛過濾器** — 只有 `new_cost < cost_grid[nr][nc]` 才把鄰居推進堆積。
  2. **隱式的 `visited`** — `if curr_cost > cost_grid[r][c]: continue` 會丟掉堆積裡的過期項目，所以不需要另外開 `visited[][]`。
- **但是**：移動方向只有右／下 → 這個網格是**有天然拓撲順序的 DAG**（按列由左到右）。每個格子只可能從 `(r-1,c)` / `(r,c-1)` 走過來，而這兩格都在它*之前*就算好了。所以**一個格子算完之後再也不會被改進** — Dijkstra 那個堆積的存在意義，在這裡完全被浪費掉。
- **結論**：DP 的 `O(m*n)` 打敗 Dijkstra 的 `O(m*n*log(m*n))`。只有當題目改成四方向移動、或成本不可加時，才需要 Dijkstra。

```text
grid = [[1,3,1],          DP table (min sum to reach each cell)
        [1,5,1],     -->  [1, 4, 5]
        [4,2,1]]          [2, 7, 6]
                          [6, 8, 7]   -> answer = 7  (1→3→1→1→1)
```

#### **2) 模式**

**模式名稱**：*可加且非負權重的網格最短路徑* → `heap of (cost, r, c)` + `dist[][]`

```python
# python
# LC 64 - Minimum Path Sum
# IDEA: Dijkstra (min-heap PQ + BFS) — general grid-shortest-path template
# time = O(m*n*log(m*n)), space = O(m*n)
import heapq

class Solution(object):
    def minPathSum(self, grid):
        if not grid or not grid[0]:
            return 0

        m, n = len(grid), len(grid[0])

        # NOTE !!! heap compares the 1st element -> cost MUST come first
        # pq entry: [cost_so_far, row, col]
        pq = [[grid[0][0], 0, 0]]

        # NOTE !!! cost_grid[r][c] = best cost found so far to reach (r,c)
        # syntax: [[val] * n for _ in range(m)]  (NOT [[val] * n] * m -> shared refs!)
        cost_grid = [[float('inf')] * n for _ in range(m)]
        cost_grid[0][0] = grid[0][0]

        moves = [[0, 1], [1, 0]]   # CAN ONLY move right, down

        while pq:
            curr_cost, r, c = heapq.heappop(pq)

            # first pop of destination == shortest path (min-heap guarantee)
            if r == m - 1 and c == n - 1:
                return curr_cost

            # NOTE !!! stale entry -> a better path to (r,c) was already found
            if curr_cost > cost_grid[r][c]:
                continue

            for dr, dc in moves:
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n:
                    new_cost = curr_cost + grid[nr][nc]
                    # relaxation: only push if strictly cheaper
                    if new_cost < cost_grid[nr][nc]:
                        cost_grid[nr][nc] = new_cost
                        heapq.heappush(pq, [new_cost, nr, nc])

        return -1
```

**模式檢查清單**（任何可加成本的網格題都能重用）：

| 步驟 | 程式碼 | 為什麼 |
|------|------|-----|
| 1. 堆積 key 放第一位 | `pq = [[cost, r, c]]` | `heapq` 比較的是第 0 個元素 → 必須是 cost |
| 2. 初始化 `dist[][]` | `[[inf] * n for _ in range(m)]` | 記錄目前最佳值；同時扮演 `visited` |
| 3. 放入起點 | `cost_grid[0][0] = grid[0][0]` | LC 64 裡起點格子本身的值也要算 |
| 4. pop 到終點就回傳 | `if (r,c) == dest: return cost` | 貪婪保證 → 第一次 pop 就是最佳 |
| 5. 跳過過期項目 | `if cost > cost_grid[r][c]: continue` | 取代顯式的 `visited[][]` |
| 6. 鬆弛 | `if new_cost < cost_grid[nr][nc]: push` | 避免堆積爆掉 |

**更推薦的 DP 解**（同一題，不用堆積 — `O(m*n)` 時間、`O(1)` 額外空間）：

```python
# python
# LC 64 - Minimum Path Sum
# IDEA: DP in-place -> dp[i][j] += min(dp[i-1][j], dp[i][j-1])
# time = O(m*n), space = O(1)
class Solution:
    def minPathSum(self, grid):
        if not grid:
            return None
        m, n = len(grid), len(grid[0])

        for i in range(1, m):      # 1st column: reachable ONLY from above (↓)
            grid[i][0] += grid[i-1][0]

        for j in range(1, n):      # 1st row: reachable ONLY from left (→)
            grid[0][j] += grid[0][j-1]

        for i in range(1, m):
            for j in range(1, n):
                grid[i][j] += min(grid[i-1][j], grid[i][j-1])

        return grid[-1][-1]
```

```java
// java
// LC 64 - Minimum Path Sum
// IDEA: DP 1D rolling row
// time = O(m*n), space = O(n)
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    dp[0] = grid[0][0];
    for (int j = 1; j < n; j++) dp[j] = dp[j-1] + grid[0][j];

    for (int i = 1; i < m; i++) {
        dp[0] += grid[i][0];                       // only from above
        for (int j = 1; j < n; j++)
            dp[j] = Math.min(dp[j], dp[j-1]) + grid[i][j];  // dp[j]=above, dp[j-1]=left
    }
    return dp[n-1];
}
```

**⚠️ 那份 python 檔裡看得到的坑**

- **PQ 的排序**：cost 必須是 tuple 的*第一個*元素，否則堆積會照 row/col 排序。
- **`(x, y)` 跟 `(r, c)` 搞混**：V0-2 推的是 `(new_cost, nx, ny)`，其中 `x` 是行、`y` 是列，所以索引寫成 `grid[ny][nx]`，終點判斷是 `x == n-1 and y == m-1`。挑一種慣例（`r, c` 搭 `grid[r][c]` 比較安全）然後從頭用到尾。
- **二維初始化**：要寫 `[[inf] * n for _ in range(m)]`，絕對不要寫 `[[inf] * n] * m`（所有列都指向同一個 list）。
- **別把 `grid[0][0]` 加兩次**：把 `grid[0][0]` 當成起點的 cost 塞進堆積後，鬆弛時就不要再加一次。

#### **3) 相似的 LC 題目**

| LC # | 題目 | 移動方向 | 成本模型 | 最佳解法 | 為什麼 |
|------|-------|----------|-----------|----------------|-----|
| **64** | Minimum Path Sum | 只有 ↓→ | 可加總和 | **DP**（Dijkstra 也可以） | DAG → 存在拓撲順序 |
| **62** | Unique Paths | 只有 ↓→ | 計數 | **DP** | 是計數不是最小化 — 根本沒有堆積的概念 |
| **63** | Unique Paths II | 只有 ↓→ | 計數 + 障礙 | **DP** | 跟 62 一樣，障礙格算 0 |
| **120** | Triangle | ↓ / ↓ 右 | 可加總和 | **DP** | 三角形同樣是 DAG |
| **931** | Minimum Falling Path Sum | ↓ 三個方向 | 可加總和 | **DP** | 仍是 DAG（一列一列來） |
| **1289** | Min Falling Path Sum II | ↓ 任一行 | 可加總和 | **DP + 最小／次小值** | DAG + 每列的最佳化 |
| **174** | Dungeon Game | 只有 ↓→ | 可加，**但**血量要 ≥1 | **反向 DP** | 正向貪婪會失效 → 從終點往回推 |
| **1631** | Path With Minimum Effort | 四方向 | `max(diff)` | **Dijkstra** | 有環 + 不可加 → DP 做不到 |
| **778** | Swim in Rising Water | 四方向 | `max(height)` | **Dijkstra** | 有環 + 極小化極大成本 |
| **1091** | Shortest Path in Binary Matrix | 八方向 | 單位成本 | **BFS** | 權重全相等 → 單純 BFS 就夠 |
| **1293** | Shortest Path with Obstacle Elim. | 四方向 | 單位成本 + k 額度 | **BFS + 狀態** | `(r, c, k)` 三維狀態 |
| **2290** | Minimum Obstacle Removal | 四方向 | 成本 0 或 1 | **0-1 BFS / Dijkstra** | 0/1 權重下雙端佇列勝過堆積 |
| **1368** | Min Cost to Make Valid Path | 四方向 | 成本 0 或 1 | **0-1 BFS / Dijkstra** | 同樣的 0/1 權重技巧 |

**從這個題型家族歸納出來的判斷規則：**

```text
Grid path problem?
├── Movement is monotonic (↓→ only, no cycles)?
│   └── YES -> DP (topological order is free)          e.g. 64, 62, 120, 931
└── NO (4-dir / 8-dir -> cycles possible)
    ├── All edge weights EQUAL (unit)?  -> BFS         e.g. 1091, 1293
    ├── Weights are only 0 or 1?        -> 0-1 BFS     e.g. 2290, 1368
    └── Arbitrary non-negative weights
        or minimax cost (max of steps)? -> Dijkstra    e.g. 1631, 778, 1631-like
```



### 8) Minimum Obstacle Removal to Reach Corner — LC 2290 — 0-1 BFS


> 障礙格成本 1、空格成本 0；用 0-1 BFS（雙端佇列）或 Dijkstra 讓總成本最小。

```java
// java
// LC 2290

// V0-1
// IDEA: Dijkstra's Algorithm (fixed by gpt)
/**
 *  NOTE !!!
 *
 * ✅ Summary:
 *  •   Single cost var won’t work → need dist[][] to track per-cell minimum cost.
 *  •   No explicit visited needed → the dist[][] + early skip (if (cost > dist[y][x]) continue) handles that.
 *
 */
public int minimumObstacles(int[][] grid) {
    if (grid == null || grid.length == 0 || grid[0].length == 0) {
        return 0;
    }

    int m = grid.length; // rows
    int n = grid[0].length; // cols

    /**
     *   NOTE !!!
     *
     *    we need a 2D array to save the cost when BFS loop over the grid
     *    (CAN'T just use a single var (cost))
     *
     * ---
     *
     * 1. Why keep a dist[][] array instead of a single cost variable?
     *
     *
     *  •   The minimum cost to reach a cell (x,y) is not unique across the grid.
     *  •   For example, you might reach (2,2) with cost 3 via one path, but later find a better path with cost 2.
     *  •   If you only had a single global cost variable, you couldn’t distinguish the costs of different cells — you’d lose information.
     *
     * That’s why:
     *  •   dist[y][x] keeps track of the best cost found so far for each specific cell.
     *  •   Dijkstra works by always expanding the lowest-cost node next, and updating neighbors only if we find a cheaper path.
     *
     * Without dist[][], you’d either:
     *  •   Revisit nodes unnecessarily (potential infinite loops), or
     *  •   Miss better paths (return wrong result).
     */
    // distance[y][x] = min obstacles to reach (y,x)
    int[][] dist = new int[m][n];
    for (int[] row : dist) {
        Arrays.fill(row, Integer.MAX_VALUE);
    }
    dist[0][0] = 0;

    // PQ stores [cost, x, y]
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
    pq.offer(new int[] { 0, 0, 0 }); // start at (0,0) with cost=0

    int[][] moves = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    while (!pq.isEmpty()) {
        int[] cur = pq.poll();
        int cost = cur[0], x = cur[1], y = cur[2];

        // Reached destination
        if (x == n - 1 && y == m - 1) {
            return cost;
        }

        // Skip if we already found better
        /**
         *  NOTE !!!
         *
         *   why DON'T need to maintain a `visited` var
         *   to prevent repeating visit ?
         *
         *  -----
         *
         *   2. Why no explicit visited array?
         *
         * This is subtle. In Dijkstra:
         *  •   A node is considered “visited” (finalized) once it’s dequeued from the priority queue with its minimum cost.
         *  •   Because of the if (cost > dist[y][x]) continue; check, we automatically ignore revisits that don’t improve cost.
         *
         *
         *  So, the role of visited is effectively played by:
         *
         *      ```
         *      if (cost > dist[y][x]) continue;
         *      ```
         *
         *   This guarantees:
         *  •   The first time you pop a cell with its minimum cost, you expand it.
         *  •   If another path later tries to reach the same cell with a higher cost, it gets ignored.
         *
         * 👉 That’s why visited isn’t needed in Dijkstra — the dist[][] array + priority queue ensure correctness.
         *
         */
        if (cost > dist[y][x])
            continue;

        for (int[] mv : moves) {
            int nx = x + mv[0];
            int ny = y + mv[1];

            if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                int newCost = cost + grid[ny][nx];
                if (newCost < dist[ny][nx]) {
                    dist[ny][nx] = newCost;
                    pq.offer(new int[] { newCost, nx, ny });
                }
            }
        }
    }

    return -1; // should never happen
}
```

### 9) Minimum Cost to Make at Least One Valid Path in a Grid — LC 1368 — 0-1 BFS


> 上面幾張表都提到 LC 1368；這裡是 0-1 BFS **雙端佇列**版的完整實作
> （[8) LC 2290](#8-minimum-obstacle-removal-to-reach-corner--lc-2290--0-1-bfs) 示範的是優先佇列版）。

**核心想法**：每個格子都有**一條免費的出邊**（權重 `0`）— 就是箭頭指的那個方向 — 以及**三條要付錢的出邊**（權重 `1`，也就是把箭頭轉向的成本）。只有兩種相異權重時你不需要堆積：把權重 `0` 的鬆弛結果 `pushFront`、權重 `1` 的 `pushBack`，**雙端佇列**就會自己維持排序。

**為什麼可行**：雙端佇列在任何時刻最多只裝兩種距離值（`d` 和 `d+1`）。把權重 `0` 的鄰居推到前面，它就留在 `d` 這一段；把權重 `1` 的鄰居推到後面，它就落在 `d+1` 那一段 — 這正是優先佇列會產生的順序，但每次操作只要 `O(1)` 而不是 `O(log V)`。

```text
Deque contents:  [ d d d d | d+1 d+1 d+1 ]
                   ^front                ^back
   weight-0 edge -> appendleft  (stays in the d block)
   weight-1 edge -> append      (joins the d+1 block)
```

```java
// java
// LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
// IDEA: 0-1 BFS — free edge in the sign direction, cost-1 edge otherwise; deque replaces the heap
// time = O(m*n), space = O(m*n)
public int minCost(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    // sign 1..4 -> (dr, dc): 1=right, 2=left, 3=down, 4=up
    int[][] dirs = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };

    int[][] dist = new int[m][n];
    for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
    dist[0][0] = 0;

    Deque<int[]> dq = new ArrayDeque<>();   // {cost, r, c}
    dq.offerFirst(new int[] {0, 0, 0});

    while (!dq.isEmpty()) {
        int[] cur = dq.pollFirst();
        int cost = cur[0], r = cur[1], c = cur[2];

        if (cost > dist[r][c]) continue;            // stale entry, same role as in Dijkstra
        if (r == m - 1 && c == n - 1) return cost;  // first pop of target = final answer

        for (int sign = 1; sign <= 4; sign++) {
            int nr = r + dirs[sign - 1][0];
            int nc = c + dirs[sign - 1][1];
            if (nr < 0 || nr >= m || nc < 0 || nc >= n) continue;

            int w = (grid[r][c] == sign) ? 0 : 1;   // free iff we follow this cell's arrow
            if (cost + w < dist[nr][nc]) {
                dist[nr][nc] = cost + w;
                // NOTE !!! weight-0 -> FRONT, weight-1 -> BACK. This is the whole trick.
                if (w == 0) dq.offerFirst(new int[] {cost + w, nr, nc});
                else        dq.offerLast(new int[] {cost + w, nr, nc});
            }
        }
    }
    return dist[m - 1][n - 1];
}
```

```python
# python
# LC 1368 - Minimum Cost to Make at Least One Valid Path in a Grid
# IDEA: 0-1 BFS — free edge in the sign direction, cost-1 edge otherwise; deque replaces the heap
# time = O(m*n), space = O(m*n)
from collections import deque

def minCost(grid):
    m, n = len(grid), len(grid[0])
    # sign value -> (dr, dc): 1=right, 2=left, 3=down, 4=up
    DIRS = {1: (0, 1), 2: (0, -1), 3: (1, 0), 4: (-1, 0)}

    INF = float('inf')
    dist = [[INF] * n for _ in range(m)]
    dist[0][0] = 0

    dq = deque([(0, 0, 0)])          # (cost, r, c)
    while dq:
        cost, r, c = dq.popleft()
        if cost > dist[r][c]:        # stale entry
            continue
        if r == m - 1 and c == n - 1:
            return cost

        for sign, (dr, dc) in DIRS.items():
            nr, nc = r + dr, c + dc
            if 0 <= nr < m and 0 <= nc < n:
                w = 0 if grid[r][c] == sign else 1
                if cost + w < dist[nr][nc]:
                    dist[nr][nc] = cost + w
                    # NOTE !!! weight-0 -> appendleft, weight-1 -> append
                    if w == 0:
                        dq.appendleft((cost + w, nr, nc))
                    else:
                        dq.append((cost + w, nr, nc))

    return dist[m - 1][n - 1]
```

**0-1 BFS vs Dijkstra — 什麼時候該把堆積換成雙端佇列**

| | Dijkstra（堆積） | 0-1 BFS（雙端佇列） |
|---|---|---|
| 邊權 | 任何非負值 | **只能是 0 和 1** |
| 邊界結構 | 最小堆積 | 雙端佇列 |
| 複雜度 | `O(E log V)` | `O(V + E)` |
| 推入規則 | `heappush(pq, (d, node))` | `w == 0 -> appendleft`，`w == 1 -> append` |
| LC 例題 | 743, 1631, 778 | **1368**, 2290 |

> ⚠️ **陷阱**：0-1 BFS *只有*在權重剛好是 `{0, 1}` 時才成立。權重變成 `{0, 1, 2}` 時，雙端佇列裡就會出現三段距離，
> 順序不變量直接破功 — 這時退回去用 Dijkstra。

---

## 多源與隱式圖

### 10) Trapping Rain Water II — LC 407 — 從邊界出發的多源搜尋


> 用最小堆積從邊界格子開始處理；某格接住的水 = max(邊界高度) - 該格高度。

```java
// java
// LC 407
// IDEA: Multi-source Dijkstra (PQ from boundary inward)
// NOTE: Start from all boundary cells, expand inward with min-heap
// NOTE: Water trapped at a cell = max(0, boundary_height - cell_height)
public int trapRainWater(int[][] heightMap) {
    if (heightMap == null || heightMap.length < 3 || heightMap[0].length < 3)
        return 0;

    int rows = heightMap.length, cols = heightMap[0].length;
    boolean[][] visited = new boolean[rows][cols];
    // PQ: [height, row, col]
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

    // Push all border cells
    for (int c = 0; c < cols; c++) {
        pq.offer(new int[]{heightMap[0][c], 0, c});
        pq.offer(new int[]{heightMap[rows - 1][c], rows - 1, c});
        visited[0][c] = true;
        visited[rows - 1][c] = true;
    }
    for (int r = 1; r < rows - 1; r++) {
        pq.offer(new int[]{heightMap[r][0], r, 0});
        pq.offer(new int[]{heightMap[r][cols - 1], r, cols - 1});
        visited[r][0] = true;
        visited[r][cols - 1] = true;
    }

    int totalWater = 0;
    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    while (!pq.isEmpty()) {
        int[] cell = pq.poll();
        for (int[] d : dirs) {
            int nr = cell[1] + d[0], nc = cell[2] + d[1];
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || visited[nr][nc])
                continue;
            visited[nr][nc] = true;
            int h = heightMap[nr][nc];
            if (h < cell[0]) {
                totalWater += cell[0] - h;
                pq.offer(new int[]{cell[0], nr, nc}); // raise to boundary level
            } else {
                pq.offer(new int[]{h, nr, nc});
            }
        }
    }
    return totalWater;
}
```

### 11) 隱式圖上的最佳優先搜尋 — LC 373


> **模式**：圖從頭到尾都沒有被建出來的 Dijkstra。「節點」是索引 tuple、「邊」是*後繼規則*、
> 「距離」就是值本身。因為每個後繼狀態都 `>=` 它的父狀態，key 是**單調非遞減**的 — 而這正是
> Dijkstra「第一次 pop 就定案」成立的條件。

**核心想法**：把 `for neighbor in graph[u]` 換成 `for successor in nextStates(u)`。其他部分 — 最小堆積、
用 `visited`/`seen` 去重、每次 pop 最小值的迴圈 — 都是原封不動的 Dijkstra。

```text
Dijkstra                          Best-first on implicit graph
---------                         ---------------------------
dist[] table                      the popped value IS the distance
graph[u] adjacency list           nextStates(u) rule
visited / dist-skip               seen set on the STATE tuple
pop min -> finalized              pop min -> k-th smallest overall
```

**實作範例 — LC 373 Find K Pairs with Smallest Sums。**
狀態 = `(i, j)` 索引對；`(i, j)` 的後繼是 `(i+1, j)` 和 `(i, j+1)`；key = `nums1[i] + nums2[j]`。
兩個陣列都已排序，所以任何後繼的總和都 `>=` 父狀態的總和 — 剛好滿足 Dijkstra 要的單調 key。

```java
// java
// LC 373 - Find K Pairs with Smallest Sums
// IDEA: best-first (Dijkstra-style) search over the implicit (i, j) index grid
// time = O(k log k), space = O(k)
public List<List<Integer>> kSmallestPairs(int[] nums1, int[] nums2, int k) {
    List<List<Integer>> res = new ArrayList<>();
    if (nums1.length == 0 || nums2.length == 0) return res;

    // {sum, i, j} — sum is FIRST so the heap orders by it
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    Set<Long> seen = new HashSet<>();          // de-dup on the STATE, not on the value
    pq.offer(new int[] {nums1[0] + nums2[0], 0, 0});
    seen.add(0L);

    while (!pq.isEmpty() && res.size() < k) {
        int[] cur = pq.poll();
        int i = cur[1], j = cur[2];
        res.add(Arrays.asList(nums1[i], nums2[j]));

        int[][] next = { {i + 1, j}, {i, j + 1} };   // the "adjacency rule"
        for (int[] nx : next) {
            int ni = nx[0], nj = nx[1];
            if (ni < nums1.length && nj < nums2.length
                    && seen.add((long) ni * nums2.length + nj)) {   // encode (ni,nj) as one key
                pq.offer(new int[] {nums1[ni] + nums2[nj], ni, nj});
            }
        }
    }
    return res;
}
```

```python
# python
# LC 373 - Find K Pairs with Smallest Sums
# IDEA: best-first (Dijkstra-style) search over the implicit (i, j) index grid
# time = O(k log k), space = O(k)
import heapq

def kSmallestPairs(nums1, nums2, k):
    if not nums1 or not nums2:
        return []

    res = []
    seen = {(0, 0)}                                  # de-dup on the STATE tuple
    pq = [(nums1[0] + nums2[0], 0, 0)]               # (sum, i, j) — key first

    while pq and len(res) < k:
        s, i, j = heapq.heappop(pq)
        res.append([nums1[i], nums2[j]])

        for ni, nj in ((i + 1, j), (i, j + 1)):      # the "adjacency rule"
            if ni < len(nums1) and nj < len(nums2) and (ni, nj) not in seen:
                seen.add((ni, nj))
                heapq.heappush(pq, (nums1[ni] + nums2[nj], ni, nj))

    return res
```

#### **變形 A — LC 378 Kth Smallest Element in a Sorted Matrix**

> 變化點：一樣是在 `(r, c)` 網格上走，但 key 直接就是 `matrix[r][c]`，而且我們只要第 `k` 次 pop 的結果，不用整個清單。

```python
# python
# LC 378 - Kth Smallest Element in a Sorted Matrix
# IDEA: same implicit-grid best-first search; the k-th pop is the answer
# time = O(k log k), space = O(k)
import heapq

def kthSmallest(matrix, k):
    n = len(matrix)
    pq = [(matrix[0][0], 0, 0)]
    seen = {(0, 0)}

    val = matrix[0][0]
    for _ in range(k):
        val, r, c = heapq.heappop(pq)
        for nr, nc in ((r + 1, c), (r, c + 1)):
            if nr < n and nc < n and (nr, nc) not in seen:
                seen.add((nr, nc))
                heapq.heappush(pq, (matrix[nr][nc], nr, nc))
    return val
```

```java
// java
// LC 378 - Kth Smallest Element in a Sorted Matrix
// IDEA: same implicit-grid best-first search; the k-th pop is the answer
// time = O(k log k), space = O(n^2) for the seen matrix
public int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;
    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
    boolean[][] seen = new boolean[n][n];
    pq.offer(new int[] {matrix[0][0], 0, 0});
    seen[0][0] = true;

    int val = matrix[0][0];
    for (int cnt = 0; cnt < k; cnt++) {
        int[] cur = pq.poll();
        val = cur[0];
        int r = cur[1], c = cur[2];
        if (r + 1 < n && !seen[r + 1][c]) { seen[r + 1][c] = true; pq.offer(new int[] {matrix[r + 1][c], r + 1, c}); }
        if (c + 1 < n && !seen[r][c + 1]) { seen[r][c + 1] = true; pq.offer(new int[] {matrix[r][c + 1], r, c + 1}); }
    }
    return val;
}
```

> 註：LC 378 還有一個 `O(n log(max-min))` 的**對答案二分搜尋**解法，當 `k ~ n^2` 時比這裡快。
> 但堆積版才是這裡值得記住的，因為它*根本就是跟 LC 373 一模一樣的程式形狀*。

#### **變形 B — LC 264 Ugly Number II**

> 變化點：狀態就是**值本身**（不是索引 tuple），後繼是 `v*2, v*3, v*5`。
> 這說明「隱式圖」不一定是網格 — 任何單調的後繼規則都行。

```python
# python
# LC 264 - Ugly Number II
# IDEA: best-first search where state = value, successors = v*2 / v*3 / v*5
# time = O(n log n), space = O(n)
import heapq

def nthUglyNumber(n):
    pq = [1]
    seen = {1}
    val = 1
    for _ in range(n):
        val = heapq.heappop(pq)
        for f in (2, 3, 5):
            if val * f not in seen:
                seen.add(val * f)
                heapq.heappush(pq, val * f)
    return val
```

```java
// java
// LC 264 - Ugly Number II
// IDEA: best-first search where state = value, successors = v*2 / v*3 / v*5
// time = O(n log n), space = O(n)
public int nthUglyNumber(int n) {
    PriorityQueue<Long> pq = new PriorityQueue<>();
    Set<Long> seen = new HashSet<>();
    pq.offer(1L);
    seen.add(1L);

    long val = 1L;
    int[] factors = {2, 3, 5};
    for (int i = 0; i < n; i++) {
        val = pq.poll();
        for (int f : factors) {
            long nxt = val * f;          // NOTE !!! use long — v*5 can overflow int mid-search
            if (seen.add(nxt)) pq.offer(nxt);
        }
    }
    return (int) val;
}
```

#### **家族總結**

| LC # | 狀態 | 後繼規則 | key（也就是「距離」） |
|------|-------|----------------|----------------------|
| **373** | `(i, j)` 索引對 | `(i+1, j)`, `(i, j+1)` | `nums1[i] + nums2[j]` |
| **378** | `(r, c)` 格子 | `(r+1, c)`, `(r, c+1)` | `matrix[r][c]` |
| **264** | 值 `v` | `2v`, `3v`, `5v` | `v` |

**面試中怎麼認出這個模式**

1. 題目要一個大到無法全部列舉的集合裡的**第 k 小／前 k 小**。
2. 每個後繼的 key 都 `>=` 目前的 key（**單調** — 沒有「負權邊」）。
3. 多個父狀態可能生出同一個狀態 → 你**必須**用 `seen` 集合對狀態去重，否則堆積會被重複項目撐爆
   （這正是 `dist[]`／`visited` 在 Dijkstra 裡扮演的角色）。

---
