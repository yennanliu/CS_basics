# Dijkstra's Algorithm

> **Scope** — Single-source shortest path with **non-negative** weights, via a priority queue.
> **See also**: [Dijkstra_examples.md](./Dijkstra_examples.md) — the eleven worked problems behind these templates; [shortest_path_comparison.md](./shortest_path_comparison.md) — which algorithm to reach for; [Bellman-Ford.md](./Bellman-Ford.md) — when weights can be negative; [Floyd-Warshall.md](./Floyd-Warshall.md) — when you need all pairs; [heap.md](./heap.md) — the priority queue underneath.

## LeetCode Problem Lists

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Heap (Priority Queue)](https://leetcode.com/problem-list/heap-priority-queue/)

## Overview
**Dijkstra's algorithm** is a greedy algorithm that solves the single-source shortest path problem for a graph with `NON-NEGATIVE` edge weights. It finds the shortest path from a starting node (source) to all other nodes in the graph.

### Key Properties
- **Time Complexity**: O((V + E) log V) with binary heap, O(V²) with array
- **Space Complexity**: O(V) for distance array and visited set
- **Core Idea**: Greedily select the unvisited node with minimum distance
- **When to Use**: Single-source shortest path with non-negative weights
- **Limitation**: `Cannot` handle `negative` edge weights

### Core Characteristics
- **Greedy Algorithm**: Always selects the minimum distance node
- **Priority Queue**: Uses min-heap for efficient minimum extraction
- **Relaxation**: Updates distances when shorter paths are found
- **Finalization**: Once visited, a node's distance is optimal

### References
- [Dijkstra's Algorithm Visualization](https://www.cs.usfca.edu/~galles/visualization/Dijkstra.html)
- [CP Algorithms - Dijkstra](https://cp-algorithms.com/graph/dijkstra.html)
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md) - For negative weight handling comparison
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md) - For all-pairs shortest path comparison


## Problem Categories

### **Category 1: Classic Shortest Path**
- **Description**: Standard single-source shortest path problems
- **Examples**: LC 743 (Network Delay), LC 1514 (Path with Max Probability)
- **Pattern**: Direct application of Dijkstra's algorithm

### **Category 2: Shortest Path with Constraints** ⚠️ Dijkstra VARIANT
- **Description**: Shortest path with an extra constraint dimension (stops, obstacles, keys, time)
- **Examples**: LC 787 (Cheapest Flights K Stops), LC 1293 (Shortest Path K Obstacle Removal), LC 864 (Get All Keys), LC 1928 (Minimum Cost K Waypoints)
- **Pattern**: 2D-state Dijkstra — state is `(cost, node, constraint)` instead of `(cost, node)`
- **Why it's a variant**: Same node at different constraint values = **different states**. Standard `visited[node]` or `dist[node]` pruning is WRONG here — it would discard valid paths that reach the same node with a different remaining budget.
- **Pruning rule**: `best[(node, constraint)] <= cost` (2D best-map, not 1D dist array)

### **Category 3: Grid-based Shortest Path**
- **Description**: Finding optimal paths in 2D grids
- **Examples**: LC 64 (Minimum Path Sum), LC 1631 (Path Min Effort), LC 778 (Swim in Rising Water)
- **Pattern**: Dijkstra on implicit graph (grid cells as nodes)
- **⚠️ Special Note**: LC 64 can use pure DP instead of Dijkstra (see below)

### **Category 4: Multi-Source Shortest Path**
- **Description**: Multiple starting points to find shortest paths
- **Examples**: LC 2812 (Find Safest Path), LC 1162 (As Far from Land)
- **Pattern**: Initialize multiple sources or use super source

### **Category 5: Time-Dependent Shortest Path**
- **Description**: Path costs change based on time or sequence
- **Examples**: LC 2045 (Second Minimum Time), LC 882 (Reachable Nodes)
- **Pattern**: Track time/state in priority queue


## Choosing Dijkstra: the two questions

Nine of this sheet's sections used to argue about LC 1631 in one form or another. They
reduce to two questions you can ask of any shortest-path problem, plus one alternative
framing worth knowing.

### 1) Is the cost additive, and can you order the cells? — LC 64 vs LC 1631 ⭐⭐⭐⭐


**Question**: Do we really need `dist[r][c]` (tracking minimum cost to reach each cell) for Dijkstra? Or is pure DP enough?

**Answer**: It depends on **movement directions**:

#### **LC 64: Minimum Path Sum** ✅ Pure DP is Sufficient
```text
Movement: RIGHT only ↓ or DOWN only →
```
- **Why DP works**: You can only reach cell `(i,j)` from `(i-1,j)` or `(i,j-1)`
- **No need for dist[][]**: Each cell is computed exactly once in topological order
- **No revisits**: You can never find a "better path" after already computing a cell
- **Solution**: Simple 2D DP or O(min(m,n)) space 1D DP

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

#### **LC 1631: Path With Minimum Effort** ⚠️ Dijkstra + dist[][] Needed
```text
Movement: UP, DOWN, LEFT, RIGHT (all 4 directions)
```
- **Why Dijkstra needed**: You might reach a cell from multiple paths, and later find a better path
- **dist[][] is essential**: Tracks "best cost found so far" for each cell
- **Revisits possible**: When moving in all 4 directions, you can revisit cells with better costs
- **Solution**: Dijkstra with dist[][] + PriorityQueue

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

#### Summary Table — LC 64 vs LC 1631

| Problem | Movement | Cost Model | Best Approach | Need dist[][]? | Need visited? |
|---------|----------|-----------|----------------|--------|---------|
| **LC 64** | Right + Down | Additive sum | **2D DP** | ❌ No | ❌ No |
| **LC 1631** | 4-directions | Max of diffs | **Dijkstra** | ✅ Yes | ✅ Yes (via dist check) |
| **LC 1263** | 4-directions | Additive cost | **Dijkstra** | ✅ Yes | ✅ Yes (via dist check) |


Two consequences worth stating explicitly, because they are the two ways this goes wrong:

**Why DP cannot rescue LC 1631.**
**A**: Because of **movement direction**:
- **LC 64**: Only move RIGHT/DOWN → Topological order exists → DP works ✅
- **LC 1631**: Can move UP/DOWN/LEFT/RIGHT → Cycles exist → DP fails ❌

With 4-directional movement, you can have circular dependencies:
```text
(1,1) → (1,2) → (2,2) → (2,1) → (1,1)
```
DP requires dependencies to form a DAG (no cycles), so **Dijkstra or Binary Search required**.

**Why "cost" and "effort" are not the same quantity.**
**A**: They measure different things in different problems:
- **Cost (LC 64, 1263)**: Sum of all values along path = `cost += value`
- **Effort (LC 1631)**: Max difference between consecutive cells = `effort = max(effort, |diff|)`

Cost is additive; effort is not. This non-additivity is why DP fails.


### 2) Do you need `dist[]`, or is `visited[]` enough? ⭐⭐⭐⭐

```text
dist[r][c] = "What's the MINIMUM cost I've found SO FAR to reach (r,c)?"
```
- **Initialize**: `dist[r][c] = Integer.MAX_VALUE` (unknown)
- **Update**: When PQ pops a cell with cost C, check `if (C > dist[r][c]) continue;`
  - If true, we already found a better path → skip processing
  - This **automatically prevents reprocessing** without explicit visited array
- **Essential when**: Multiple paths can reach the same cell → Dijkstra refinement needed


**A**: No, you use ONE or the OTHER:
- **Option A: dist[][]** → Check `if (newCost < dist[r][c])` before processing
- **Option B: visited[]** → Mark as visited after first pop from PQ

Both prevent reprocessing the same cell. Pick whichever feels clearer — but they are **not** always interchangeable. See below for when `best[]`/`dist[]` is actually *required* vs. when you can simplify to comparing the next candidate path directly against the current (just-popped) path.



There are two different comparisons a Dijkstra implementation can make when it looks at a candidate edge `cur_node -> nxt_node`:

| | **Type 1: compare vs. `best[]`** | **Type 2: compare next path vs. cur (popped) path** |
|---|---|---|
| **What is compared** | `candidate_value` vs. `best[nxt_node]` (the best value *ever recorded* for that node/state) | Nothing stored — `candidate_value` is derived straight from `cur_node`'s already-finalized value; no lookup table |
| **When the check happens** | **Before pushing** to the heap (relaxation step) | **After popping** — a `visited[node]` boolean gate, no value comparison at all |
| **Why it's needed / why it works** | The same node (or node+constraint state) can be reached many times over the run with different values; you must remember the best one seen so far to know if a new path is actually an improvement | Dijkstra's min-heap invariant guarantees the **first pop of a node is already globally optimal**, so once popped there is nothing left to compare against — any later, worse duplicate is simply skipped by the visited check |
| **Persists across multiple updates?** | ✅ Yes — `best[node]` can be overwritten several times before the node is finalized | ❌ No — a node is written once (`visited[node] = true`) and never touched again |
| **Required when...** | State has an extra dimension (`(node, constraint)` — same node can be legitimately "reached" at several different constraint values, each valid), OR you need to detect *ties* (e.g. counting paths), OR you want to prune the heap early by rejecting non-improving pushes | State is a single scalar per node, non-negative edge weights, no extra constraint dimension — the plain single-source case |
| **Fails silently if misused** | N/A (always correct, just uses more memory) | ❌ Using `visited[]`-only on a **constrained** problem (LC 787-style) is **WRONG** — it wipes out the extra dimension and discards valid paths (see [LC 787](./Dijkstra_examples.md#4-cheapest-flights-within-k-stops--lc-787--2d-state-) for the concrete trace) |

**Key concept in one line:**
> `best[]` answers *"is this candidate better than anything I've seen before for this state?"* — needed whenever a state can be legitimately revisited with a different value.
> `visited[]`-only answers *"has this state already been finalized?"* — sufficient only when the heap's pop-order guarantee (first pop = optimal) fully covers the state, i.e. no extra constraint dimension.

**Classic Dijkstra problems by type:**

| Type | LC # | Problem | Why |
|------|------|---------|-----|
| **Type 1 — needs best[]/dist[]** | 743 | Network Delay Time | Standard single-source relaxation before push |
| | 1514 | Path with Maximum Probability | `best[]`/`max_prob[]` tracks max product seen so far per node |
| | 1976 | Number of Ways to Arrive at Destination | Needs `dist[]` **and** `ways[]` — must detect exact ties (`==`), impossible without a persisted value |
| | 787 | Cheapest Flights Within K Stops | **Must** be 2D `best[(node, stops)]` — `visited[node]`-only is provably wrong (see the [LC 787](./Dijkstra_examples.md#4-cheapest-flights-within-k-stops--lc-787--2d-state-) trace) |
| | 1293 / 864 / 2093 | Constrained-state Dijkstra variants | Same reason as 787 — extra constraint dimension means a node has multiple valid finalized states |
| **Type 2 — visited[]-only suffices** | 1631 | Path With Minimum Effort | Variant 2 (`visited[][]`) — scalar per-cell state, first pop = optimal effort |
| | 778 | Swim in Rising Water | `visited[][]` marks cells finalized; next path value = `max(cur path value, next cell height)` |
| | 743 | Network Delay Time (alt. impl.) | The "visited-set variant" shown in [LC 743](./Dijkstra_examples.md#1-network-delay-time--lc-743) — equivalent to `dist[]`, just checked after pop instead of before push |
| | 2290 | Minimum Obstacle Removal | Weights are only 0/1 → 0-1 BFS with a deque + `visited[]` also works, no value table needed |

**Rule of thumb**: if you can answer "is `node` alone a complete description of where you are in the search?" with **yes**, `visited[]`-only is safe. The moment the answer becomes "no — I also need to know how many stops/keys/obstacles I've used," you must upgrade to a `best[]`/`dist[]` map keyed by `(node, constraint)`.


**A**: Yes! The check `if (cost > dist[r][c]) continue;` **IS** your visited mechanism:
- First time we pop (r,c): `cost == dist[r][c]` → process
- Later pops to (r,c): `cost > dist[r][c]` → skip (it's like "already visited")

So you get the benefit of visited[] semantics without an extra array.

---


### 3) When Union Find is the better framing

**A**: Use Union Find when:
- You're comfortable building explicit edge list
- You want to see the problem as a graph connectivity problem
- You're practicing Kruskal's algorithm

Both have same time complexity O(m×n×log(m×n)), but Dijkstra is usually more intuitive for grid problems.

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | State Tracked | When to Use |
|---------------|----------|---------------|-------------|
| **Basic Dijkstra** | Standard shortest path | (distance, node) | No constraints |
| **Constrained Path** | Path with limits | (cost, node, constraint) | K stops, budget |
| **Grid Dijkstra** | 2D grid navigation | (cost, x, y) | Matrix problems |
| **Multi-Source** | Multiple starts | (dist, node, source) | Multiple origins |
| **Time-Variant** | Time-dependent | (time, node, state) | Dynamic costs |

### Universal Dijkstra Template ⭐⭐⭐⭐⭐
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

### Template 1: Basic Dijkstra
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

### Template 2: Dijkstra with Constraints (2D-State Variant) ⭐⭐⭐⭐

**Core idea — why this is NOT standard Dijkstra:**

| | Standard Dijkstra | Constrained Dijkstra |
|---|---|---|
| **State** | `(cost, node)` | `(cost, node, constraint)` |
| **State space** | 1D — one entry per node | 2D — one entry per `(node, constraint)` pair |
| **Pruning** | `dist[node] <= cost` | `best[(node, stops)] <= cost` |
| **First-pop invariant** | First pop of `node` = globally optimal | First pop of `(node, stops)` = optimal for that stops value |
| **visited[node] works?** | ✅ Yes | ❌ No — same node valid at different stop counts |

**Why `visited[node]` / `dist[node]` breaks:**
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

**General constrained Dijkstra skeleton:**
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

**Similar Problems (same 2D-state pattern):**
| LC # | Problem | Constraint Dimension | State |
|------|---------|---------------------|-------|
| **787** | Cheapest Flights K Stops | stops used (0..K) | `(node, stops)` |
| **1293** | Shortest Path K Obstacle Removal | obstacles removed (0..K) | `(node, obstacles)` |
| **864** | Shortest Path to Get All Keys | keys collected (bitmask) | `(node, keys)` |
| **2093** | Minimum Cost to Reach City With Discounts | discounts used (0..K) | `(node, discounts)` |
| **1928** | Min Cost to Reach Destination in Time | time remaining | `(node, time)` |

### Template 3: Grid-based Dijkstra ⭐⭐⭐⭐
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

### Template 4: Multi-Source Dijkstra ⭐⭐⭐
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

### Template 5: Bidirectional Dijkstra
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

## Problems by Pattern

### **Classic Shortest Path Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Network Delay Time | 743 | Basic Dijkstra | Medium |
| Path with Maximum Probability | 1514 | Max-heap variant | Medium |
| Find the City With Smallest Number | 1334 | All-pairs shortest path | Medium |
| Minimum Weighted Subgraph | 2203 | Three sources Dijkstra | Hard |
| Number of Ways to Arrive | 1976 | Count shortest paths | Medium |
| Shortest Path in Binary Matrix | 1091 | Grid Dijkstra | Medium |

### **Constrained Path Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Cheapest Flights Within K Stops | 787 | State tracking | Medium |
| Minimum Cost to Reach City | 1928 | K waypoints | Hard |
| Shortest Path to Get All Keys | 864 | State bitmask | Hard |
| Escape a Large Maze | 1036 | Limited BFS/Dijkstra | Hard |
| Minimum Obstacle Removal | 2290 | 0-1 BFS variant | Hard |

### **Grid-based Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Minimum Path Sum | 64 | DAG grid (DP preferred, Dijkstra works) | Medium |
| Path With Minimum Effort | 1631 | Grid Dijkstra | Medium |
| Swim in Rising Water | 778 | Min time path | Hard |
| Minimum Cost to Make Valid Path | 1368 | Modified costs | Hard |
| Shortest Path in a Grid | 1293 | K obstacles | Hard |
| Trap Rain Water II | 407 | Priority queue | Hard |

### **Multi-Source Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Find Safest Path in Grid | 2812 | Multi-source init | Medium |
| As Far from Land as Possible | 1162 | Multi-source BFS | Medium |
| Shortest Distance from All Buildings | 317 | Multiple Dijkstra | Hard |
| Minimum Height Trees | 310 | Center finding | Medium |

### **Time/State Dependent Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Second Minimum Time to Destination | 2045 | Track two values | Hard |
| Reachable Nodes In Subdivided Graph | 882 | Edge subdivision | Hard |
| Minimum Time to Visit All Points | 2065 | State tracking | Hard |
| The Maze III | 499 | Lexicographic path | Hard |


## Worked Examples

Eleven problems live in **[Dijkstra_examples.md](./Dijkstra_examples.md)**, grouped by what the
state looks like — which is the thing that decides the implementation:

| Group | Problems | What makes the state |
|---|---|---|
| [Classic single-source](./Dijkstra_examples.md#classic-single-source-shortest-path) | LC 743, 1514, 1976 | one scalar per node |
| [Constrained state](./Dijkstra_examples.md#constrained-state-dijkstra) | LC 787 | `(node, budget)` — a second dimension |
| [Grids](./Dijkstra_examples.md#grids) | LC 1631, 778, 64, 2290, 1368 | a cell, plus whether the cost is additive or a running max |
| [Multi-source & implicit graphs](./Dijkstra_examples.md#multi-source-and-implicit-graphs) | LC 407, 373 | the graph is never given as an edge list |


## Decision Framework

### Pattern Selection Strategy

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

### When to Use Dijkstra vs BFS

| Criteria | Dijkstra | BFS |
|----------|----------|-----|
| **Edge weights** | Non-negative, varying | All equal (unweighted) or 0/1 |
| **Data structure** | Priority Queue (min-heap) | Queue (`LinkedList`) |
| **Time complexity** | O((V + E) log V) | O(V + E) |
| **First visit = shortest?** | ❌ No (must relax via PQ) | ✅ Yes (level = distance) |
| **"Minimum cost/weight"** | ✅ Use Dijkstra | ❌ Wrong answer |
| **"Minimum steps/moves"** | ❌ Overkill | ✅ Use BFS |
| **Grid with varying costs** | ✅ Dijkstra on implicit graph | ❌ |
| **Grid with uniform cost** | ❌ Unnecessary overhead | ✅ BFS |

**Decision rule**: If every edge has the same cost (or cost is 1), use BFS — it's simpler and O(V+E). The moment edges have different non-negative weights, use Dijkstra.

**Common trap**: Using Dijkstra (PQ) for problems like LC 279 Perfect Squares or LC 752 Open the Lock where all edges cost 1 — plain BFS is sufficient and faster.

**0-1 BFS special case**: If edges are weighted 0 or 1 only, use a **deque** — push weight-0 edges to front, weight-1 edges to back. O(V+E) like BFS, handles two weights correctly. Example: LC 2290 Minimum Obstacle Removal.

### When to Use Dijkstra vs Other Algorithms

| Scenario | Use Dijkstra | Use Alternative | Alternative Algorithm |
|----------|--------------|-----------------|---------------------|
| Negative weights | ❌ | ✅ | Bellman-Ford |
| Unweighted graph | ❌ | ✅ | BFS |
| All-pairs shortest path | ❌ | ✅ | Floyd-Warshall |
| Single source, non-negative | ✅ | ❌ | - |
| Need path reconstruction | ✅ | - | Track parent nodes |
| Dense graphs | ⚠️ | Consider | Bellman-Ford |
| Sparse graphs | ✅ | ❌ | - |

## Algorithm Comparison: Dijkstra vs Floyd-Warshall vs Bellman-Ford

### Comprehensive Comparison Table

| Feature | Dijkstra | Floyd-Warshall | Bellman-Ford |
|---------|----------|----------------|--------------|
| **Problem Type** | Single-source shortest path | All-pairs shortest path | Single-source shortest path |
| **Time Complexity** | O((V+E) log V) with heap | O(V³) | O(V·E) |
| **Space Complexity** | O(V) | O(V²) | O(V) |
| **Negative Weights** | ❌ No | ✅ Yes | ✅ Yes |
| **Negative Cycles** | N/A | Detects | Detects |
| **Implementation** | Moderate (priority queue) | Very simple (3 loops) | Simple (2 loops) |
| **Data Structure** | Min-heap/Priority Queue | 2D matrix | Edge list + distance array |
| **Graph Type** | Best for sparse graphs | Best for dense graphs | Works with any |
| **Output** | Distances from one source | All-pairs distances | Distances from one source |
| **Early Termination** | ✅ Can stop at target | ❌ Must complete | ❌ Must run V-1 iterations |
| **Best Use Case** | Large sparse graphs, single-source | Small complete graphs, all-pairs | Negative weights, cycle detection |
| **Worst Case Graph** | Dense graphs | Very large graphs | Dense graphs with many edges |

### When to Use Each Algorithm

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

### Practical Comparison Examples

**Example 1: Social Network (1000 users, 5000 friendships)**
- **Single-source (find distances from one user):**
  - Dijkstra: ~5000 × log(1000) ≈ 50,000 operations ⚡ **Best choice**
  - Bellman-Ford: 1000 × 5000 = 5,000,000 operations
  - Floyd-Warshall: 1000³ = 1,000,000,000 operations

- **All-pairs (distances between all users):**
  - Dijkstra × V: 50,000 × 1000 = 50,000,000 operations ⚡ **Best choice**
  - Floyd-Warshall: 1,000,000,000 operations (simpler code)

**Example 2: Small Complete Graph (50 nodes, fully connected)**
- **All-pairs shortest paths:**
  - Floyd-Warshall: 50³ = 125,000 operations ⚡ **Best choice** (simplest)
  - Dijkstra × V: ~2500 × log(50) × 50 = ~500,000 operations

**Example 3: Currency Exchange with Arbitrage Detection**
- **Detect negative cycles (arbitrage opportunities):**
  - Bellman-Ford: O(V·E) ⚡ **Best choice** (explicitly detects)
  - Floyd-Warshall: O(V³), checks diagonal (works for all-pairs)
  - Dijkstra: ❌ Cannot handle negative weights

### Performance Benchmarks

| Graph Size | Edges | Dijkstra (single) | Dijkstra (all-pairs) | Floyd-Warshall | Bellman-Ford |
|------------|-------|-------------------|----------------------|----------------|--------------|
| V=100, Sparse | 500 | 0.01ms | 1ms | 10ms ⚡ | 5ms |
| V=100, Dense | 5000 | 0.1ms | 10ms ⚡ | 10ms | 50ms |
| V=500, Sparse | 2500 | 0.05ms | 25ms ⚡ | 1.25s | 125ms |
| V=500, Dense | 125K | 2ms | 1s | 1.25s ⚡ | 6.25s |
| V=1000, Sparse | 5000 | 0.1ms | 100ms ⚡ | 10s | 500ms |

*(Times are approximate, assuming optimized implementations)*

### Algorithm Selection Matrix

| Your Situation | Recommended Algorithm | Why |
|----------------|----------------------|-----|
| Need shortest path from A to B in road network | **Dijkstra** | Single-source, non-negative, can stop early |
| Find center of small network (≤300 nodes) | **Floyd-Warshall** | Need all-pairs, small graph, simple code |
| Route planning in city with traffic (dynamic costs) | **Dijkstra** (re-run) | Real-time updates, single-source |
| Check if prerequisite chain exists | **Floyd-Warshall** | Transitive closure, small graph |
| Currency arbitrage detection | **Bellman-Ford** | Negative cycle detection needed |
| Social network - degrees of separation | **BFS** (if unweighted) | Unweighted, single-source |
| Minimum spanning tree | **Prim's/Kruskal's** | Different problem entirely |
| Game pathfinding on grid | **Dijkstra** or **A*** | Sparse grid, heuristic available |

## Summary & Quick Reference

### Complexity Quick Reference
| Implementation | Time Complexity | Space Complexity | Notes |
|----------------|-----------------|------------------|-------|
| Array-based | O(V²) | O(V) | Good for dense graphs |
| Binary Heap | O((V+E)logV) | O(V) | Most common |
| Fibonacci Heap | O(E + VlogV) | O(V) | Theoretical best |
| Grid-based | O(RC log(RC)) | O(RC) | R=rows, C=cols |

### Template Quick Reference
| Template | Best For | Key Code Pattern |
|----------|----------|------------------|
| Basic | Standard shortest path | `heapq.heappop(pq)` → relax edges |
| Constrained | K-stops, budget limits | Track state: `(cost, node, constraint)` |
| Grid | 2D matrix problems | 4-directional movement |
| Multi-Source | Multiple starting points | Initialize all sources |
| Bidirectional | Large graphs | Search from both ends |

### Common Patterns & Tricks

#### **Priority Queue State**
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

#### **Visited Set Optimization**
```python
# Option 1: Check after pop (recommended)
if node in visited:
    continue
visited.add(node)

# Option 2: Check distance
if d > dist[node]:
    continue
```

#### **Path Reconstruction**
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

### Problem-Solving Steps
1. **Identify graph structure**: Explicit edges or implicit (grid)?
2. **Check constraints**: Non-negative weights? Single source?
3. **Choose template**: Basic, constrained, grid, or multi-source?
4. **Define state**: What needs tracking in priority queue?
5. **Implement relaxation**: How to update distances?
6. **Handle termination**: When to stop? Return what value?

---

## Similar LeetCode Problems Reference

### Grid-Based Problems
| LC # | Title | Movement | Key Feature | Primary Approach | Alt Approaches | dist[][] Needed? |
|------|-------|----------|-------------|----------|---------|---------|
| **64** | Minimum Path Sum | ↓→ only | Additive cost | **2D DP** | 1D DP, Dijkstra (overkill) | ❌ No |
| **1631** | Path With Minimum Effort | 4-dir | Max step diff (non-additive) | **Dijkstra** | Binary Search, Union Find | ✅ Yes |
| **778** | Swim in Rising Water | 4-dir | Max grid value | **Dijkstra** | Union Find | ✅ Yes |
| **1263** | Minimum Moves to Move Box | 4-dir | Push box mechanics | **Dijkstra + state** | - | ✅ Yes |
| **882** | Reachable Nodes In Subdivided Graph | Graph | Node subdivision | **Dijkstra** | - | ✅ Yes |

**LC 1631 Deep Dive:**
- **Solutions Available**: 4 major approaches (Dijkstra dist[], Dijkstra visited, Binary Search, Union Find)
- **Most Common**: Dijkstra with `dist[][]` array or `visited[]` array
- **Key Insight**: The cost model is `Math.max(effort, step_diff)`, not additive—this makes DP impossible
- **Reference**: `leetcode_java/src/main/java/LeetCodeJava/Graph/PathWithMinimumEffort.java` (V0-V4.3)

### Classic Shortest Path Problems
| LC # | Title | Type | Key Feature |
|------|-------|------|-------------|
| **743** | Network Delay Time | Graph | Broadcast delays |
| **787** | Cheapest Flights K Stops | Graph | K-stop constraint |
| **1514** | Path with Maximum Probability | Graph | Maximize probability |
| **1928** | Minimum Cost to Reach Destination | Weighted Graph | K waypoints |

### Multi-Source Shortest Path
| LC # | Title | Key Feature |
|------|-------|-------------|
| **1162** | As Far from Land as Possible | Multi-source BFS-Dijkstra |
| **2812** | Find the Safest Path | Grid-based multi-source |
| **2290** | Minimum Obstacle Removal | 0-1 BFS variant |

### Key Implementation Files
- **Java Reference**: `leetcode_java/src/main/java/LeetCodeJava/DynamicProgramming/MinimumPathSum.java`
  - V0: Dijkstra with dist[][] (works but overkill)
  - V0-0-1, V1, V2: Pure DP approaches (optimal for LC 64)
- **Python Reference**: `leetcode_python/Dynamic_Programming/minimum-path-sum.py`
  - V0-1, V0-2: Dijkstra (min-heap + `cost_grid[][]`) — see [7) LC 64](./Dijkstra_examples.md#7-minimum-path-sum--lc-64--a-dag-grid-where-dp-is-optimal)
  - V1, V2: DP in-place `O(1)` space / 1D rolling row (optimal for LC 64)
  
---

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Forgetting to check if already visited
- Using Dijkstra with negative weights
- Not using priority queue (using regular queue)
- Incorrect state comparison in constrained problems
- Not handling disconnected components

**✅ Best Practices:**
- Always use min-heap for priority queue
- Track visited nodes to avoid reprocessing
- Initialize distances to infinity except source
- Consider using distance array vs visited set
- Handle edge cases (empty graph, no path)

### Interview Tips
1. **Clarify constraints**: Always ask about negative weights
2. **State complexity**: Mention time/space complexity upfront
3. **Explain relaxation**: Core concept of updating distances
4. **Consider alternatives**: Mention when BFS or Bellman-Ford better
5. **Optimize if needed**: Discuss bidirectional search for large graphs

### Related Topics
- **BFS**: Unweighted shortest path
- **[Bellman-Ford](./Bellman-Ford.md)**: Handles negative weights (see detailed comparison above)
- **[Floyd-Warshall](./Floyd-Warshall.md)**: All-pairs shortest path (see detailed comparison above)
- **A* Algorithm**: Heuristic-guided search
- **SPFA**: Queue-optimized Bellman-Ford variant
- **Johnson's Algorithm**: All-pairs with reweighting technique
