# Dijkstra — Worked Examples

> **Scope** — The worked-solution archive behind [Dijkstra.md](./Dijkstra.md): eleven problems in both languages, grouped by the shape of the search state, since that is what decides whether you need `dist[]`, a second state dimension, or a plain `visited[]`.
> **See also**: [Dijkstra.md](./Dijkstra.md) — the parent sheet: the five templates, the two decision questions and the algorithm comparison; [Bellman-Ford.md](./Bellman-Ford.md) — when edges can be negative; [Floyd-Warshall.md](./Floyd-Warshall.md) — all pairs; [shortest_path_comparison.md](./shortest_path_comparison.md) — picking between the three; [bfs.md](./bfs.md) — the unweighted case and 0-1 BFS; [heap.md](./heap.md) — the priority queue underneath all of it.

## LeetCode Problem Lists

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph](https://leetcode.com/problem-list/graph/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Overview

This is the long tail of [Dijkstra.md](./Dijkstra.md). The parent keeps the templates and the two
questions that pick between them; this file keeps the problems that *apply* them.

### Key Properties
- **Complexity**: O(E log V) with a binary heap unless a solution says otherwise; the 0-1 BFS problems are O(V + E)
- **Core Idea**: the algorithm never changes — what changes is what a "node" is, and the groups below are ordered by how far the state drifts from "just a node"
- **When to Use**: after the parent's two questions have told you the problem is Dijkstra-shaped at all


## Classic Single-Source Shortest Path

### 1) Network Delay Time — LC 743

> Dijkstra from source k; answer is max of all shortest distances, or -1 if any unreachable.

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

### 2) Path with Maximum Probability — LC 1514 — max-heap on probability


> Max-heap Dijkstra multiplying edge probabilities; start at 1.0, maximize reach-probability.

**Core Idea — why we need `best[]` (a.k.a `max_prob[]`):**
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
This mirrors the `dist[node]` pruning of standard Dijkstra, just inverted: instead of `dist[u] + w < dist[v]` (minimize sum), we check `prob[u] * edge_prob > prob[v]` (maximize product). Use a **max-heap** (negate the probability, since `heapq` is a min-heap by default), and stale/negative-updated heap entries are skipped via `if prob < best[node]: continue`.

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

**Alternative Approaches (LC 1514 also solvable without a priority queue):**

Since edge weights (probabilities) are non-negative and we're maximizing a product instead of minimizing a sum, this problem also admits **Bellman-Ford** and **SPFA** solutions — useful if the interviewer asks for approaches beyond Dijkstra.

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

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| **Dijkstra (max-heap)** | O((V+E) log V) | O(V+E) | Best general choice; early-exits once `end` is popped |
| **Bellman-Ford** | O(V·E) | O(V) | Simple nested loops, no heap; good fallback if PQ not allowed |
| **SPFA** | O(V·E) worst, faster typical | O(V+E) | Queue instead of heap; same idea as 0-1 BFS but for weighted relax |

### 3) Number of Ways to Arrive at Destination — LC 1976 — Dijkstra + path counting


> Standard Dijkstra; track count of shortest paths at each node alongside minimum distance.

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

## Constrained-State Dijkstra

### 4) Cheapest Flights Within K Stops — LC 787 — 2D state ⭐⭐⭐⭐


> ⚠️ This is NOT standard Dijkstra. The constraint (K stops) adds a second dimension to the state.
> Standard `dist[node]` pruning is WRONG here — same node reached with different stops = different valid states.

**Core Idea:**
- State: `(cost, node, stops_used)` — stops_used is part of the identity
- Pruning: `best[(node, stops)] <= cost` replaces `dist[node] <= cost`
- Why: Node A reached in 1 stop at cost 900 vs 2 stops at cost 100 are BOTH valid; discarding either gives wrong answer

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

**Why `dist[node]` pruning fails (concrete trace):**
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

## Grids

### 5) Path With Minimum Effort — LC 1631 — min-max on a grid ⭐⭐⭐⭐


> Minimize the maximum absolute difference along path; use min-heap with effort as priority key.

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


#### Dijkstra Implementation Variants for LC 1631

##### **Variant 1: Using dist[][] Array (Recommended)**
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
**Why it works**: The `dist[][]` check `if (effort > dist[r][c]) continue;` automatically skips any path that's worse than the best we've found.

##### **Variant 2: Using visited[] Array**
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
**Why visited works**: The min-heap guarantees that the first time we pop a cell is with optimal effort, so marking it visited prevents reprocessing.

##### **Variant Comparison**
| Approach | Space | Logic | Best For |
|----------|-------|-------|----------|
| **dist[][]** | Extra O(m×n) | Compare against best known | When updating multiple times |
| **visited[]** | Extra O(m×n) | Mark as finalized | Simpler logic, faster exit |


#### Alternative Approaches for LC 1631

##### **Approach 3: Binary Search + DFS**
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
**Time**: O((V+E) × log(maxH)) | **Space**: O(V)

##### **Approach 4: Union Find (Kruskal's Algorithm)**
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
**Time**: O((V+E) log(V+E)) = O(m×n × log(m×n)) | **Space**: O(m×n)


#### Approach Selection for LC 1631
| Approach | Pros | Cons | Best When |
|----------|------|------|-----------|
| **Dijkstra + dist[][]** | Most intuitive, standard | Extra space | Want classic Dijkstra pattern |
| **Dijkstra + visited[]** | Simpler early termination | Less flexible | Just need minimum effort |
| **Binary Search + DFS** | Uses less memory in some cases | Slower (repeated DFS) | Memory is critical |
| **Union Find** | Elegant graph perspective | Complex to implement | Learning Union Find |


### 6) Swim in Rising Water — LC 778 — min-max on a grid


> Min-heap where priority = max elevation seen so far; answer = time to reach bottom-right.

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

### 7) Minimum Path Sum — LC 64 — a DAG grid where DP is optimal


> Move only RIGHT/DOWN, minimize the sum along the path. Dijkstra **works**, but the grid is a **DAG** so plain DP is strictly better. Great problem for seeing what Dijkstra buys you — and what it doesn't.
> Ref: `leetcode_python/Dynamic_Programming/minimum-path-sum.py` (V0-1 / V0-2 = Dijkstra, V1 / V2 = DP)

#### **1) Core Idea**

```text
Dijkstra = BFS + Priority Queue (min-heap)

Treat each cell (r, c) as a graph node.
Edges: (r,c) -> (r,c+1) and (r,c) -> (r+1,c), edge weight = grid[next cell]
Answer = shortest path from (0,0) to (m-1,n-1)
```

- **Cost model is ADDITIVE** (`new_cost = curr_cost + grid[nr][nc]`) and **all weights ≥ 0** → Dijkstra is valid.
- **Greedy guarantee**: the min-heap always pops the globally cheapest frontier cell, so the **first pop of the destination is the answer** — return immediately, no need to drain the heap.
- **`cost_grid[r][c]`** ("dist" array) = best cost found *so far* to reach `(r,c)`. It plays **two** roles:
  1. **Relaxation filter** — only push a neighbor if `new_cost < cost_grid[nr][nc]`.
  2. **Implicit `visited`** — `if curr_cost > cost_grid[r][c]: continue` drops stale heap entries, so no separate `visited[][]` is needed.
- **But**: movement is only RIGHT/DOWN → the grid is a **DAG with a natural topological order** (row-major). Each cell can only be reached from `(r-1,c)` / `(r,c-1)`, both computed *before* it. So **a cell is never improved after it's computed** — the whole point of Dijkstra's heap is wasted here.
- **Verdict**: DP `O(m*n)` beats Dijkstra `O(m*n*log(m*n))`. Use Dijkstra only if the problem adds 4-directional movement or a non-additive cost.

```text
grid = [[1,3,1],          DP table (min sum to reach each cell)
        [1,5,1],     -->  [1, 4, 5]
        [4,2,1]]          [2, 7, 6]
                          [6, 8, 7]   -> answer = 7  (1→3→1→1→1)
```

#### **2) Pattern**

**Pattern name**: *Grid Shortest Path with additive non-negative weights* → `heap of (cost, r, c)` + `dist[][]`

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

**Pattern checklist** (reusable for any additive-cost grid problem):

| Step | Code | Why |
|------|------|-----|
| 1. Heap key first | `pq = [[cost, r, c]]` | `heapq` compares element 0 → must be the cost |
| 2. `dist[][]` init | `[[inf] * n for _ in range(m)]` | tracks best-so-far; also acts as `visited` |
| 3. Seed source | `cost_grid[0][0] = grid[0][0]` | start cell's own value counts in LC 64 |
| 4. Early return on pop | `if (r,c) == dest: return cost` | greedy guarantee → first pop is optimal |
| 5. Skip stale | `if cost > cost_grid[r][c]: continue` | replaces explicit `visited[][]` |
| 6. Relax | `if new_cost < cost_grid[nr][nc]: push` | prevents heap blow-up |

**Preferred DP solution** (same problem, no heap — `O(m*n)` time, `O(1)` extra space):

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

**⚠️ Gotchas seen in the python file**

- **PQ ordering**: cost must be the *first* tuple element, otherwise the heap sorts by row/col.
- **`(x, y)` vs `(r, c)` mix-up**: V0-2 pushes `(new_cost, nx, ny)` where `x` = column, `y` = row, so indexing is `grid[ny][nx]` and the destination check is `x == n-1 and y == m-1`. Pick ONE convention (`r, c` with `grid[r][c]` is safer) and stick to it.
- **2D init**: use `[[inf] * n for _ in range(m)]`, never `[[inf] * n] * m` (all rows alias the same list).
- **Don't add `grid[0][0]` twice**: seed the heap with `grid[0][0]` as its cost, and never re-add it when relaxing.

#### **3) Similar LC Problems**

| LC # | Title | Movement | Cost Model | Best Approach | Why |
|------|-------|----------|-----------|----------------|-----|
| **64** | Minimum Path Sum | ↓→ only | additive sum | **DP** (Dijkstra works) | DAG → topological order exists |
| **62** | Unique Paths | ↓→ only | counting | **DP** | count, not minimize — no heap concept |
| **63** | Unique Paths II | ↓→ only | counting + blocks | **DP** | same as 62 with obstacle cells = 0 |
| **120** | Triangle | ↓ / ↓-right | additive sum | **DP** | triangle is also a DAG |
| **931** | Minimum Falling Path Sum | ↓ 3-way | additive sum | **DP** | still a DAG (row by row) |
| **1289** | Min Falling Path Sum II | ↓ any col | additive sum | **DP + min/2nd-min** | DAG + per-row optimization |
| **174** | Dungeon Game | ↓→ only | additive **but** needs ≥1 HP | **DP backwards** | forward greedy fails → DP from end |
| **1631** | Path With Minimum Effort | 4-dir | `max(diff)` | **Dijkstra** | cycles + non-additive → DP impossible |
| **778** | Swim in Rising Water | 4-dir | `max(height)` | **Dijkstra** | cycles + minimax cost |
| **1091** | Shortest Path in Binary Matrix | 8-dir | unit cost | **BFS** | all weights equal → plain BFS is enough |
| **1293** | Shortest Path with Obstacle Elim. | 4-dir | unit cost + k budget | **BFS + state** | `(r, c, k)` 3D state |
| **2290** | Minimum Obstacle Removal | 4-dir | cost 0 or 1 | **0-1 BFS / Dijkstra** | deque beats heap for 0/1 weights |
| **1368** | Min Cost to Make Valid Path | 4-dir | cost 0 or 1 | **0-1 BFS / Dijkstra** | same 0/1 weight trick |

**Decision rule derived from this family:**

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


> Cost = 1 for obstacle, 0 for empty cell; use 0-1 BFS (deque) or Dijkstra to minimize total.

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


> The doc references LC 1368 in the tables above; this is the worked implementation of the **deque** form of 0-1 BFS
> (LC 2290 in [8) LC 2290](#8-minimum-obstacle-removal-to-reach-corner--lc-2290--0-1-bfs) shows the priority-queue form).

**Key Idea**: every cell has **one free outgoing edge** (weight `0`) — the direction its arrow points — and **three
paid outgoing edges** (weight `1`, the cost of re-pointing the arrow). With only two distinct weights you don't need a
heap: a **deque** keeps the frontier sorted if you `pushFront` weight-`0` relaxations and `pushBack` weight-`1` ones.

**Why it works**: the deque holds at most two distinct distance values at any time (`d` and `d+1`). Pushing a
weight-`0` neighbour to the front keeps it in the `d` block, pushing a weight-`1` neighbour to the back puts it in the
`d+1` block — exactly the ordering a priority queue would produce, at `O(1)` per operation instead of `O(log V)`.

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

**0-1 BFS vs Dijkstra — when to swap the heap for a deque**

| | Dijkstra (heap) | 0-1 BFS (deque) |
|---|---|---|
| Edge weights | any non-negative | **only 0 and 1** |
| Frontier structure | min-heap | double-ended queue |
| Complexity | `O(E log V)` | `O(V + E)` |
| Push rule | `heappush(pq, (d, node))` | `w == 0 -> appendleft`, `w == 1 -> append` |
| LC examples | 743, 1631, 778 | **1368**, 2290 |

> ⚠️ **Trap**: 0-1 BFS is *only* valid when weights are exactly `{0, 1}`. With weights `{0, 1, 2}` the deque holds
> three distance blocks and the ordering invariant breaks — fall back to Dijkstra.

---

## Multi-Source and Implicit Graphs

### 10) Trapping Rain Water II — LC 407 — multi-source from the boundary


> Process boundary cells with min-heap; water trapped = max(boundary height) - cell height.

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

### 11) Best-First Search on an Implicit Graph — LC 373


> **Pattern**: Dijkstra with the graph never materialized. The "nodes" are index tuples, the "edges" are
> *successor rules*, and the "distance" is the value itself. Because every successor is `>=` its parent, the key is
> **monotonically non-decreasing** — which is exactly the condition that makes Dijkstra's "first pop is final" hold.

**Key Idea**: replace `for neighbor in graph[u]` with `for successor in nextStates(u)`. Everything else — the min-heap,
the `visited`/`seen` de-dup, the pop-smallest loop — is unchanged Dijkstra.

```text
Dijkstra                          Best-first on implicit graph
---------                         ---------------------------
dist[] table                      the popped value IS the distance
graph[u] adjacency list           nextStates(u) rule
visited / dist-skip               seen set on the STATE tuple
pop min -> finalized              pop min -> k-th smallest overall
```

**Worked example — LC 373 Find K Pairs with Smallest Sums.**
State = `(i, j)` index pair; successors of `(i, j)` are `(i+1, j)` and `(i, j+1)`; key = `nums1[i] + nums2[j]`.
Both arrays are sorted, so any successor's sum `>=` its parent's — the monotone key Dijkstra requires.

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

#### **Variation A — LC 378 Kth Smallest Element in a Sorted Matrix**

> Twist: same `(r, c)` grid walk, but the key is `matrix[r][c]` directly and we want only the `k`-th pop, not the list.

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

> Note: LC 378 also has a `O(n log(max-min))` **binary-search-on-value** solution that beats this when `k ~ n^2`.
> The heap version is the one worth remembering here because it is *literally the same code shape* as LC 373.

#### **Variation B — LC 264 Ugly Number II**

> Twist: the state is the **value itself** (not an index tuple), and successors are `v*2, v*3, v*5`.
> Shows that "implicit graph" doesn't have to mean a grid — any monotone successor rule works.

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

#### **Family summary**

| LC # | State | Successor rule | Key (the "distance") |
|------|-------|----------------|----------------------|
| **373** | `(i, j)` index pair | `(i+1, j)`, `(i, j+1)` | `nums1[i] + nums2[j]` |
| **378** | `(r, c)` cell | `(r+1, c)`, `(r, c+1)` | `matrix[r][c]` |
| **264** | value `v` | `2v`, `3v`, `5v` | `v` |

**Checklist for spotting this pattern in an interview**

1. Asked for the **k-th smallest / k smallest** of a set that is too large to enumerate.
2. Every successor's key is `>=` the current key (**monotone** — no "negative edges").
3. Multiple parents can generate the same state → you **must** de-dup with a `seen` set on the state, otherwise the
   heap blows up with duplicates (the exact same role `dist[]`/`visited` plays in Dijkstra).

---
