# Dijkstra's Algorithm

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

### **Category 2: Shortest Path with Constraints**
- **Description**: Shortest path with additional restrictions (stops, cost limits)
- **Examples**: LC 787 (Cheapest Flights K Stops), LC 1928 (Minimum Cost K Waypoints)
- **Pattern**: Modified Dijkstra with state tracking

### **Category 3: Grid-based Shortest Path**
- **Description**: Finding optimal paths in 2D grids
- **Examples**: LC 1631 (Path Min Effort), LC 778 (Swim in Rising Water)
- **Pattern**: Dijkstra on implicit graph (grid cells as nodes)

### **Category 4: Multi-Source Shortest Path**
- **Description**: Multiple starting points to find shortest paths
- **Examples**: LC 2812 (Find Safest Path), LC 1162 (As Far from Land)
- **Pattern**: Initialize multiple sources or use super source

### **Category 5: Time-Dependent Shortest Path**
- **Description**: Path costs change based on time or sequence
- **Examples**: LC 2045 (Second Minimum Time), LC 882 (Reachable Nodes)
- **Pattern**: Track time/state in priority queue



## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | State Tracked | When to Use |
|---------------|----------|---------------|-------------|
| **Basic Dijkstra** | Standard shortest path | (distance, node) | No constraints |
| **Constrained Path** | Path with limits | (cost, node, constraint) | K stops, budget |
| **Grid Dijkstra** | 2D grid navigation | (cost, x, y) | Matrix problems |
| **Multi-Source** | Multiple starts | (dist, node, source) | Multiple origins |
| **Time-Variant** | Time-dependent | (time, node, state) | Dynamic costs |

### Universal Dijkstra Template
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

### Template 2: Dijkstra with Constraints
```python
def dijkstra_constrained(n, edges, src, dst, k):
    """Shortest path with at most k stops/constraints"""
    graph = collections.defaultdict(list)
    for u, v, w in edges:
        graph[u].append((v, w))
    
    # (cost, node, stops_left)
    pq = [(0, src, k + 1)]
    # Track best stops count for each node
    best = {}
    
    while pq:
        cost, u, stops = heapq.heappop(pq)
        
        if u == dst:
            return cost
        
        # Prune if we've seen this node with more stops
        if u in best and best[u] >= stops:
            continue
        best[u] = stops
        
        if stops > 0:
            for v, w in graph[u]:
                heapq.heappush(pq, (cost + w, v, stops - 1))
    
    return -1
```

### Template 3: Grid-based Dijkstra
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

### Template 4: Multi-Source Dijkstra
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

## LC Examples

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

### 2-1) Network Delay Time

```python
# LC 743 Network Delay Time
# V0 
# IDEA : Dijkstra
class Solution:
    def networkDelayTime(self, times, N, K):
        K -= 1
        nodes = collections.defaultdict(list)
        for u, v, w in times:
            nodes[u - 1].append((v - 1, w))
        dist = [float('inf')] * N
        dist[K] = 0
        done = set()
        for _ in range(N):
            smallest = min((d, i) for (i, d) in enumerate(dist) if i not in done)[1]
            for v, w in nodes[smallest]:
                if v not in done and dist[smallest] + w < dist[v]:
                    dist[v] = dist[smallest] + w
            done.add(smallest)
        return -1 if float('inf') in dist else max(dist)
```

### 2-2) Cheapest Flights Within K Stops

```python
# LC 787 Cheapest Flights Within K Stops
# V1
# IDEA : Dijkstra
# https://leetcode.com/problems/cheapest-flights-within-k-stops/discuss/267200/Python-Dijkstra
# IDEA
# -> To implement Dijkstra, we need a priority queue to pop out the lowest weight node for next search. In this case, the weight would be the accumulated flight cost. So my node takes a form of (cost, src, k). cost is the accumulated cost, src is the current node's location, k is stop times we left as we only have at most K stops. I also convert edges to an adjacent list based graph g.
# -> Use a vis array to maintain visited nodes to avoid loop. vis[x] record the remaining steps to reach x with the lowest cost. If vis[x] >= k, then no need to visit that case (start from x with k steps left) as a better solution has been visited before (more remaining step and lower cost as heappopped beforehand). And we should initialize vis[x] to 0 to ensure visit always stop at a negative k.
# -> Once k is used up (k == 0) or vis[x] >= k, we no longer push that node x to our queue. Once a popped cost is our destination, we get our lowest valid cost.
# -> For Dijkstra, there is not need to maintain a best cost for each node since it's kind of greedy search. It always chooses the lowest cost node for next search. So the previous searched node always has a lower cost and has no chance to be updated. The first time we pop our destination from our queue, we have found the lowest cost to our destination.
import collections
import math
class Solution:
    def findCheapestPrice(self, n, flights, src, dst, K):
        graph = collections.defaultdict(dict)
        for s, d, w in flights:
            graph[s][d] = w
        pq = [(0, src, K+1)]
        vis = [0] * n
        while pq:
            w, x, k = heapq.heappop(pq)
            if x == dst:
                return w
            if vis[x] >= k:
                continue
            vis[x] = k
            for y, dw in graph[x].items():
                heapq.heappush(pq, (w+dw, y, k-1))
        return -1
```

### 2-3) Path With Minimum Effort

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

### 2-4) Path with Maximum Probability

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

### 2-5) Number of Ways to Arrive at Destination

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

### 2-6) Swim in Rising Water

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

### 2-7) Trapping Rain Water II

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

### 2-8) Minimum Obstacle Removal to Reach Corner

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



## Decision Framework

### Pattern Selection Strategy

```
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

```
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