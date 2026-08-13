# Floyd-Warshall Algorithm

> **Scope** — **All-pairs** shortest path by DP over intermediate vertices — O(V³), dense graphs, transitive closure.
> **See also**: [shortest_path_comparison.md](./shortest_path_comparison.md) — which algorithm to reach for; [Dijkstra.md](./Dijkstra.md) — single source, sparse graph; [Bellman-Ford.md](./Bellman-Ford.md) — single source, negative weights.

## LeetCode Problem Lists

- [Shortest Path](https://leetcode.com/problem-list/shortest-path/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Dynamic Programming](https://leetcode.com/problem-list/dynamic-programming/)

## Overview
**Floyd-Warshall algorithm** is a dynamic programming algorithm that solves the all-pairs shortest path problem. It finds the shortest paths between all pairs of vertices in a weighted graph, even with negative edge weights (but no negative cycles).

### Key Properties
- **Time Complexity**: O(V³) where V is the number of vertices
- **Space Complexity**: O(V²) for distance matrix
- **Core Idea**: Dynamic programming with intermediate vertices
- **When to Use**: All-pairs shortest path, can handle negative weights
- **Limitation**: `Cannot` handle `negative cycles` (detects them but doesn't work with them)

### Core Characteristics
- **Dynamic Programming**: Builds solution incrementally using intermediate vertices
- **Matrix-Based**: Uses adjacency matrix representation
- **Simple Implementation**: Three nested loops
- **Versatile**: Works with negative weights, detects negative cycles
- **Path Reconstruction**: Can track paths with predecessor matrix

### References
- [Floyd-Warshall Visualization](https://www.cs.usfca.edu/~galles/visualization/Floyd.html)
- [CP Algorithms - Floyd-Warshall](https://cp-algorithms.com/graph/all-pair-shortest-path-floyd-warshall.html)
- [Dijkstra Cheatsheet](./Dijkstra.md) - For single-source comparison
- [Bellman-Ford Cheatsheet](./Bellman-Ford.md) - For single-source with negative weights


## Problem Categories

### **Category 1: Classic All-Pairs Shortest Path**
- **Description**: Find shortest paths between all pairs of vertices
- **Examples**: LC 1334 (Find City with Smallest Number), LC 1462 (Course Schedule IV)
- **Pattern**: Direct application of Floyd-Warshall

### **Category 2: Transitive Closure**
- **Description**: Determine reachability between all pairs of vertices
- **Examples**: LC 1462 (Course Schedule IV), Graph connectivity problems
- **Pattern**: Boolean version of Floyd-Warshall

### **Category 3: Negative Cycle Detection**
- **Description**: Detect if graph contains negative cycles
- **Examples**: Arbitrage detection, negative weight cycles
- **Pattern**: Check diagonal after Floyd-Warshall

### **Category 4: Minimax/Maximin Path**
- **Description**: Find path that minimizes maximum edge or maximizes minimum edge
- **Examples**: LC 1334 (threshold problems), bottleneck shortest path
- **Pattern**: Modified Floyd-Warshall with different operation

### **Category 5: Graph Diameter and Metrics**
- **Description**: Find longest shortest path, graph center, radius
- **Examples**: Network diameter, graph eccentricity
- **Pattern**: Post-process Floyd-Warshall results


## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Operation | When to Use |
|---------------|----------|-----------|-------------|
| **Basic Floyd-Warshall** | All-pairs shortest path | min(dist[i][j], dist[i][k]+dist[k][j]) | Standard shortest paths |
| **Transitive Closure** | Reachability | dist[i][j] OR (dist[i][k] AND dist[k][j]) | Boolean connectivity |
| **Minimax Path** | Bottleneck path | min(dist[i][j], max(dist[i][k], dist[k][j])) | Capacity/bandwidth |
| **Path Reconstruction** | Track actual paths | Predecessor matrix | Need actual path |
| **Negative Cycle** | Detect cycles | Check dist[i][i] < 0 | Arbitrage, cycle detection |

### Template 1: Basic Floyd-Warshall
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

### Template 2: Floyd-Warshall with Path Reconstruction
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

### Template 3: Transitive Closure (Reachability)
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

### Template 4: Negative Cycle Detection
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

### Template 5: Minimax Path (Bottleneck)
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

### Template 6: Space-Optimized Version
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

## Algorithm Comparison

### Floyd-Warshall vs Dijkstra vs Bellman-Ford

| Feature | Floyd-Warshall | Dijkstra | Bellman-Ford |
|---------|----------------|----------|--------------|
| **Problem Type** | All-pairs shortest path | Single-source shortest path | Single-source shortest path |
| **Time Complexity** | O(V³) | O((V+E) log V) | O(V·E) |
| **Space Complexity** | O(V²) | O(V) | O(V) |
| **Negative Weights** | ✅ Yes | ❌ No | ✅ Yes |
| **Negative Cycles** | Detects | N/A | Detects |
| **Implementation** | Very simple (3 loops) | Moderate (priority queue) | Simple (2 loops) |
| **Graph Type** | Dense graphs preferred | Sparse graphs preferred | Any |
| **Output** | All-pairs distances | Single-source distances | Single-source distances |
| **Best Use Case** | Small graphs, all-pairs | Large sparse graphs | Negative weights, cycle detection |

### When to Use Which Algorithm

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

### Practical Comparison Table

| Scenario | Best Algorithm | Reason |
|----------|----------------|--------|
| Small complete graph, all-pairs | Floyd-Warshall | O(V³) is acceptable, simple code |
| Large sparse graph, single-source | Dijkstra | O((V+E) log V) much faster |
| Negative weights, single-source | Bellman-Ford | Only algorithm that handles it |
| Transitive closure | Floyd-Warshall | Natural DP formulation |
| Grid shortest path | Dijkstra | Graph is implicit, sparse |
| Network diameter | Floyd-Warshall | Need all-pairs anyway |
| Path with constraints | Dijkstra (modified) | Flexible state tracking |
| Arbitrage detection | Floyd-Warshall | Need cycle detection, all-pairs |

### Complexity Comparison Examples

For a graph with V=1000 vertices and E=5000 edges:

| Algorithm | Operations | Relative Speed |
|-----------|------------|----------------|
| Floyd-Warshall | 1,000,000,000 | Baseline (slowest) |
| Dijkstra (V times) | ~50,000 × log(1000) × 1000 | ~20x faster |
| Dijkstra (single) | ~5,000 × log(1000) | ~20,000x faster |
| Bellman-Ford | 1000 × 5000 = 5,000,000 | ~200x faster |


## LC Examples

### 2-1) Find the City With the Smallest Number of Neighbors (LC 1334) — Floyd-Warshall All-Pairs
> Run Floyd-Warshall; for each city count reachable cities within threshold; return city with fewest (largest index on tie).

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

### 2-2) Course Schedule IV (LC 1462) — Floyd-Warshall Transitive Closure
> Use boolean reachability matrix; reachable[i][j] = true if i is prerequisite of j (direct or indirect).

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

### 2-3) Network Delay Time Alternative Solution (LC 743) — Floyd-Warshall All-Pairs
> Compute all-pairs distances; answer is max dist from source k (overkill vs Dijkstra but correct).

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

### 2-4) Graph Connectivity With Threshold (LC 1627) — Floyd-Warshall Connectivity
> Build edges for all GCD > threshold; use Floyd-Warshall transitive closure to answer queries.

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

### 2-5) Shortest Path Visiting All Nodes (LC 847) — BFS + Bitmask (Floyd-Warshall Preprocessing)
> BFS with state (node, visitedMask); precompute pairwise distances with Floyd-Warshall if needed.

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

### 2-6) Cheapest Flights Within K Stops (LC 787) — Min-Plus Matrix Power ⭐⭐⭐⭐⭐
> The `k-i-j` loop-order rule made concrete. **Plain Floyd-Warshall is WRONG here**: its outer `k` is an *intermediate vertex*, not a *hop count*, so once the triple loop finishes, `dist[src][dst]` is the unrestricted shortest path with no record of how many edges it used. The Floyd-Warshall *family* fix is to change what the third loop means — move `k` inward and you get **min-plus matrix multiplication**, whose exponent counts edges.

| | Loop order | What `k` means | Result |
|---|---|---|---|
| **Floyd-Warshall** | `k` **outermost**, then `i`, `j` | set of intermediate vertices allowed so far | unrestricted all-pairs shortest path (edge count unbounded) |
| **Min-plus product** | `i`, then `k`, then `j` (`k` inner) | the single joining vertex of two halves | `C = A ⊗ B`: A's hop budget **plus** B's hop budget |

**Key Idea**: define `(A ⊗ B)[i][j] = min over k of (A[i][k] + B[k][j])` — ordinary matrix multiplication with `(+, ×)` swapped for `(min, +)`. This product is **associative**, so it can be exponentiated by repeated squaring.

Let `M[i][j]` = cheapest single flight `i → j`, and set **`M[i][i] = 0`** — that "stay put" self-loop is what turns *exactly t edges* into *at most t edges*. Then `M^t[i][j]` = cheapest `i → j` route using **at most `t` flights**. LC 787 permits `K` stops = `K + 1` flights, so the answer is `(M^(K+1))[src][dst]`.

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

**⚠️ Overflow guard**: use `Integer.MAX_VALUE / 3` (not `MAX_VALUE`) as `INF` in Java — `minPlus` adds two entries that may both be `INF`, and `MAX_VALUE + MAX_VALUE` wraps to a negative "shortest" path.

**Interview reality check**: for LC 787's actual constraints (`n ≤ 100`, `k ≤ 100`) the layered O(K·E) relaxation is simpler and faster — see [Bellman-Ford](./Bellman-Ford.md) §2-1 and [Dijkstra](./Dijkstra.md) §2-2 for the two solutions you would actually write. Reach for min-plus exponentiation only when the **hop budget `K` is enormous (10⁹) while `V` stays small**, where `log K` beats any per-hop loop. Mentioning it as the follow-up ("what if K were a billion?") is the interview payoff.

### Sizing Check: Is `n` Small Enough for O(n³)?

Read the constraints before committing to Floyd-Warshall — the cube is unforgiving.

| `n` | `n³` | Verdict |
|-----|------|---------|
| ≤ 100 | 10⁶ | Trivially fine (LC 787, LC 1462 sit here) |
| ≤ 200 | 8 × 10⁶ | Fine (LC 1334 caps at 100) |
| ≤ 500 | 1.25 × 10⁸ | Borderline — OK in Java/C++, risky in Python |
| ≤ 1000 | 10⁹ | Too slow — run Dijkstra from each source instead |
| > 1000 | ≥ 10⁹ | Not an all-pairs problem; re-read the question |

**Rule of thumb**: if the constraint on the vertex count is stated as `n ≤ a few hundred` **and** the problem asks about many different `(u, v)` pairs, the setter is almost certainly inviting Floyd-Warshall.


## Problems by Pattern

### **All-Pairs Shortest Path Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Find the City With Smallest Number | 1334 | Direct Floyd-Warshall | Medium |
| Network Delay Time | 743 | Overkill but works | Medium |
| Minimum Weighted Subgraph | 2203 | Three sources | Hard |
| Shortest Path in Undirected Graph | 1976 | All-pairs distances | Medium |

### **Transitive Closure Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Course Schedule IV | 1462 | Boolean Floyd-Warshall | Medium |
| Graph Connectivity | 1627 | Reachability matrix | Hard |
| Evaluate Division | 399 | Weighted transitive closure | Medium |

### **Minimax/Maximin Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Path With Minimum Effort | 1631 | Modified Floyd-Warshall | Medium |
| Swim in Rising Water | 778 | Minimax path | Hard |
| Minimum Score of a Path | 2492 | Modified operation | Medium |

### **Graph Metrics Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Graph Diameter | N/A | Max of all-pairs | Medium |
| Center of Star Graph | 1791 | Post-process distances | Easy |
| Tree Diameter | 1522 | All-pairs in tree | Medium |


## Decision Framework

### When to Use Floyd-Warshall

✅ **Use Floyd-Warshall when:**
- Need all-pairs shortest paths
- Graph is small (V ≤ 400-500)
- Need transitive closure
- Need to detect negative cycles
- Graph is dense (E ≈ V²)
- Implementation simplicity is priority
- Need to answer multiple queries about different pairs

❌ **Don't use Floyd-Warshall when:**
- Only need single-source shortest path (use Dijkstra/Bellman-Ford)
- Graph is very large (V > 1000)
- Graph is sparse (use Dijkstra V times)
- Memory is constrained (O(V²) space)
- Need fastest path finding (Dijkstra is faster for single-source)

### Implementation Checklist

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


## Summary & Quick Reference

### Time/Space Complexity

| Aspect | Complexity | Notes |
|--------|------------|-------|
| Time | O(V³) | Three nested loops |
| Space | O(V²) | Distance matrix |
| Preprocessing | O(E) | Build adjacency matrix |
| Query Time | O(1) | After preprocessing |

### Key Code Patterns

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

### Common Variations

| Variation | Modification | Use Case |
|-----------|--------------|----------|
| **Standard** | min(dist[i][j], dist[i][k]+dist[k][j]) | Shortest paths |
| **Longest Path** | max(dist[i][j], dist[i][k]+dist[k][j]) | Critical paths |
| **Minimax** | min(dist[i][j], max(dist[i][k], dist[k][j])) | Bottleneck paths |
| **Maximin** | max(dist[i][j], min(dist[i][k], dist[k][j])) | Widest paths |
| **Boolean** | OR/AND operations | Reachability |

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Wrong loop order (k must be outermost)
- Forgetting to initialize diagonal to 0
- Not handling undirected graphs (both directions)
- Checking for negative cycles incorrectly
- Using Floyd-Warshall for single-source on large graphs

**✅ Best Practices:**
- Always use k as outer loop (intermediate vertex)
- Initialize dist[i][i] = 0 before adding edges
- For undirected graphs, add both directions
- Check diagonal for negative values to detect cycles
- Consider space optimization if only final distances needed
- Use Dijkstra if only single-source is needed

### Interview Tips

1. **Identify the problem type**: Clarify if single-source or all-pairs
2. **Mention time/space complexity**: O(V³) time, O(V²) space upfront
3. **Compare with alternatives**: Discuss when Dijkstra/Bellman-Ford better
4. **Edge cases**: Disconnected components, negative cycles, self-loops
5. **Optimization opportunities**: Can we use Dijkstra V times instead?

### When to Mention Floyd-Warshall in Interview

- "We need all-pairs shortest paths" → Floyd-Warshall
- "Graph is small (< 500 vertices)" → Floyd-Warshall feasible
- "Need transitive closure" → Floyd-Warshall is natural
- "Can handle negative weights?" → Yes, unlike Dijkstra
- "What about larger graphs?" → Run Dijkstra V times or use Johnson's

### Related Algorithms

- **[Dijkstra](./Dijkstra.md)**: Single-source, faster for sparse graphs, no negative weights
- **[Bellman-Ford](./Bellman-Ford.md)**: Single-source, handles negative weights, slower
- **Johnson's Algorithm**: All-pairs using reweighting + Dijkstra, O(V²logV + VE)
- **Warshall's Algorithm**: Boolean version for transitive closure
- **Path Matrix Multiplication**: Alternative O(V³logV) approach
