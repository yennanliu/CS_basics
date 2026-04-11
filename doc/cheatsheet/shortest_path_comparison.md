# Shortest Path Algorithms — When to Use Which

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

## See Also
- [Dijkstra Cheatsheet](./Dijkstra.md)
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md)
- [Floyd-Warshall Cheatsheet](./Floyd-Warshall.md)
- [BFS Cheatsheet](./bfs.md)
