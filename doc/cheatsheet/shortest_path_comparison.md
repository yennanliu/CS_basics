# Shortest Path Algorithms — When to Use Which

## LeetCode Problem Lists

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## Quick Decision Table

| Question | Answer → Algorithm |
|----------|-------------------|
| Non-negative weights, single source? | **Dijkstra** O((V+E) log V) |
| Negative weights allowed, single source? | **Bellman-Ford** O(V·E) |
| Need to detect negative cycles? | **Bellman-Ford** O(V·E) |
| At most K edges/stops? | **Bellman-Ford** (K iterations) |
| All-pairs shortest path? | **Floyd-Warshall** O(V³) |
| Transitive closure (reachability)? | **Floyd-Warshall** (boolean) |
| Unweighted graph? | **BFS** O(V+E) |
| Grid with 0/1 weights? | **0-1 BFS** (deque) O(V+E) |
| DAG? | **Topological Sort + relax** O(V+E) |
| Dense graph, single source? | **Dijkstra** with array O(V²) |
| Sparse graph, single source? | **Dijkstra** with heap O((V+E) log V) |

## Side-by-Side Comparison

| Property | BFS | Dijkstra | Bellman-Ford | Floyd-Warshall |
|----------|-----|----------|--------------|----------------|
| **Type** | Single-source | Single-source | Single-source | All-pairs |
| **Time** | O(V+E) | O((V+E) log V) | O(V·E) | O(V³) |
| **Space** | O(V) | O(V) | O(V) | O(V²) |
| **Negative weights** | No | No | Yes | Yes |
| **Negative cycle detect** | No | No | Yes | Yes |
| **Graph type** | Unweighted | Weighted (≥0) | Any | Any |
| **Approach** | Queue | Greedy + heap | Relaxation ×(V-1) | DP |
| **Implementation** | Simple | Medium | Simple | Simple |

## Decision Flowchart

```
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

## Common Mistakes & Gotchas

### 1. Using Dijkstra with negative weights
```
Graph:  A --1--> B --(-5)--> C
        A --3--> C

Dijkstra visits C via A→C (cost 3), marks C as done.
Misses A→B→C (cost 1+(-5) = -4).  ← WRONG ANSWER

Fix: Use Bellman-Ford.
```

### 2. Bellman-Ford vs Dijkstra for LC 787 (Cheapest Flights K Stops)
```
Dijkstra alone doesn't work — K stop constraint means a longer
path might be cheaper. Need modified Bellman-Ford with K iterations,
or modified Dijkstra with state (node, stops_remaining).
```

### 3. Floyd-Warshall loop order matters
```java
// CORRECT: k (intermediate) must be outermost loop
for (int k = 0; k < V; k++)        // intermediate vertex
    for (int i = 0; i < V; i++)     // source
        for (int j = 0; j < V; j++) // destination
            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);

// WRONG: i or j as outermost loop gives incorrect results
```

### 4. When to use Dijkstra vs DP on grids
```
LC 64 (Min Path Sum): only move right/down → DAG → use DP (simpler)
LC 1631 (Min Effort):  move 4 directions → cycles possible → use Dijkstra
LC 778 (Swim in Rising Water): 4 directions → Dijkstra or binary search + BFS

Rule: If movement is restricted to one direction (no cycles) → DP.
      If movement allows backtracking/cycles → Dijkstra.
```

## Variant: 0-1 BFS

For graphs where edge weights are only 0 or 1:

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

**Classic LC:** LC 1368 (Min Cost to Make at Least One Valid Path) — 0-1 BFS on grid

## LC Example

| # | Problem | Algorithm | Why This One? |
|---|---------|-----------|---------------|
| 743 | Network Delay Time | Dijkstra | Non-negative, single source |
| 787 | Cheapest Flights K Stops | Bellman-Ford (K iters) | K-edge constraint |
| 1334 | Find City Smallest Neighbors | Floyd-Warshall | All-pairs + threshold |
| 1631 | Path with Min Effort | Dijkstra | Grid, 4-directional, non-negative |
| 778 | Swim in Rising Water | Dijkstra / BS+BFS | Grid, min-max path |
| 1368 | Min Cost Valid Path | 0-1 BFS | 0/1 weights |
| 1462 | Course Schedule IV | Floyd-Warshall | Transitive closure |
| 862 | Shortest Subarray Sum ≥ K | Not shortest path! | Prefix sum + mono deque |
| 64 | Minimum Path Sum | DP (not Dijkstra) | DAG — right/down only |
| 505 | The Maze II | Dijkstra | Weighted (roll distance), non-negative |

## Problem → Algorithm Decision Table (Extended) ⭐⭐⭐⭐⭐

Read the **Signal** column first — it is the phrase in the problem statement that forces the choice.

| # | Problem | Signal in the statement | Algorithm | Why it's forced |
|---|---------|-------------------------|-----------|-----------------|
| 847 | Shortest Path Visiting All Nodes | "visit **every** node", `n ≤ 12` | **BFS over `(node, mask)`** | Unweighted → BFS, but plain `seen[node]` is wrong: a node must be re-entered with a different visited set |
| 1129 | Shortest Path with Alternating Colors | edge colors must **alternate** | **BFS over `(node, lastColor)`** | Unweighted → BFS; the constraint is extra **state**, not extra weight |
| 1514 | Path with Maximum Probability | weights in `[0,1]`, **maximize** the product | **Dijkstra with max-heap** | Products only shrink along a path (weights ≤ 1) → greedy still valid; formally `-log(p)` turns it into a normal min-cost Dijkstra |
| 1976 | Number of Ways to Arrive at Destination | **count** the shortest paths | **Dijkstra + `ways[]`** | One pass: on strict improve `ways[v] = ways[u]`, on tie `ways[v] += ways[u]` (mod 1e9+7) |
| 1928 | Minimum Cost to Reach Destination in Time | minimize **fee** subject to a **time budget** | **Dijkstra over `(node, timeUsed)`** or `dp[t][node]` | Two independent resources — plain Dijkstra on cost alone is **WRONG** (see below) |
| 399 | Evaluate Division | weighted edges, query = "value of **any** path" | **DFS/BFS with running product** (or weighted union-find) | Weights are consistent ⇒ every path gives the same answer, so there is nothing to minimize — no relaxation needed |
| 1971 | Find if Path Exists in Graph | **reachability only**, no distance asked | **BFS / DFS / union-find** | No cost in the statement → any shortest-path machinery is wasted work |
| 1697 | Checking Existence of Edge Length Limited Paths | "path where **every edge** < limit" | **Sort queries + union-find (offline)** | Bottleneck (max-edge) constraint, not an additive cost → relaxation `dist[u]+w` does not apply |
| 1584 | Min Cost to Connect All Points | "connect **all** points, cheapest total" | **MST (Prim / Kruskal)** | Wants a cheapest *tree*, not a cheapest *path* — Prim looks like Dijkstra but relaxes differently (see below) |
| 329 | Longest Increasing Path in a Matrix | **LONGEST** path | **Memoized DFS on implicit DAG** | Longest path has no greedy/relaxation analogue; the strict-increase rule is what makes the graph acyclic |
| 1857 | Largest Color Value in a Directed Graph | longest color count along a path | **Topo sort + DP** (cycle ⇒ `-1`) | Same as above, but a general digraph — must detect a cycle before any longest-path DP |

## When the Naive Choice Is Wrong ⭐⭐⭐⭐⭐

### A. Dijkstra on the wrong scalar — LC 1928

```
Minimize FEE, but total TIME must stay ≤ maxTime.

Cheapest-fee route may blow the time budget; the fastest route
may be expensive. Neither scalar alone dominates the other.

Popping by fee and marking node "done" is WRONG: the same node
reached later (higher fee) but FASTER can still lead to the answer.

Fix: make time part of the state.
     Keep bestTime[node]; only expand a state whose time strictly
     improves on every previously expanded time at that node.
```

Same shape as **LC 787** (fee vs. stops) — whenever a problem carries **two budgets**, one of them belongs in the state.

### B. `seen[node]` is too coarse — LC 847 / 1129 / 787

```
Plain BFS/Dijkstra assumes: "first arrival at a node is final".
That breaks the moment the answer depends on HOW you arrived.

LC 847  : depends on which nodes are already visited  → state = (node, mask)
LC 1129 : depends on the color of the last edge used  → state = (node, lastColor)
LC 787  : depends on how many stops are left          → state = (node, stops)

Rule: seen/dist must be keyed by the FULL state, not by node.
      State count blows up the complexity by the size of the extra dimension.
```

### C. Prim vs. Dijkstra — one term apart — LC 1584

```java
// Dijkstra  (cheapest PATH from source)
if (dist[u] + w < dist[v]) dist[v] = dist[u] + w;

// Prim / MST (cheapest TREE covering everything)   ← LC 1584
if (w < key[v]) key[v] = w;                  // no dist[u] term!
```

Identical heap scaffolding, different relaxation. "Connect **all** nodes" → MST; "get **from A to B**" → shortest path.

### D. Maximize instead of minimize — LC 1514

```
Dijkstra's greedy argument needs the path metric to be monotone
NON-IMPROVING as the path grows.

Sum of non-negative weights  : only grows  → min-heap Dijkstra  ✅
Product of probabilities ≤ 1 : only shrinks → max-heap Dijkstra ✅ (LC 1514)
Product of weights > 1       : grows unboundedly → Dijkstra ❌
```

## Template: State-Augmented Search ⭐⭐⭐⭐⭐

**Key Idea**: keep the algorithm (BFS if unweighted, Dijkstra if weighted) and widen the *state*.
Everything else — `seen`, `dist`, the queue payload — is keyed by the widened state.

### Variant 1 — BFS over `(node, bitmask)` — LC 847

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

### Variant 2 — Dijkstra over `(node, resourceUsed)` — LC 1928

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

> **Alternative for LC 1928**: layered DP `dp[t][node] = min fee to reach node in exactly time t`,
> relaxed over `t = 1..maxTime` — the Bellman-Ford framing, `O(maxTime · E)`, no heap.
> Same idea as LC 787's "K iterations": the bounded resource becomes the DP dimension.

## 0-1 BFS — Java version (LC 1368)

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

## More Reference

- **LC 1311** Get Watched Videos by Your Friends — BFS **levels** (exactly `k` hops), then sort by frequency; a level query, not a distance query.
- **LC 1466** Reorder Routes to Make All Paths Lead to the City Zero — traverse from `0` ignoring direction, count edges pointing the wrong way (0/1 edge cost, but a tree ⇒ plain DFS is enough).
- **LC 1489** Find Critical and Pseudo-Critical Edges in MST — MST family, not shortest path; see the LC 1584 note above.

## See Also
- [Dijkstra Cheatsheet](./Dijkstra.md)
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md)
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md)
- [BFS Cheatsheet](./bfs.md)
