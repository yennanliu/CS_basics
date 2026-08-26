# Topological Sorting - Complete Guide

> **Scope** — Ordering a DAG — Kahn's BFS, DFS post-order, cycle detection, and the scheduling problems built on them.
> **See also**: [topology_sorting_examples.md](./topology_sorting_examples.md) — the eight worked problems behind these templates; [diff_toposort_quickunion.md](./diff_toposort_quickunion.md) — toposort vs union-find — which tool for which problem; [union_find.md](./union_find.md) — undirected connectivity; [graph.md](./graph.md) — general graph material.

## LeetCode Problem Lists

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## Overview

Topological sorting is a linear ordering of vertices in a Directed Acyclic Graph (DAG) such that for every directed edge (u, v), vertex u comes before v in the ordering.

### Key Characteristics
- **DAG Only**: Works only on Directed Acyclic Graphs
- **Multiple Valid Orders**: Many valid topological orders may exist
- **Dependency Resolution**: Solves problems with prerequisite/dependency relationships
- **Applications**: Task scheduling, build systems, course planning, dependency resolution

### Complexity Analysis
| Approach | Time Complexity | Space Complexity | Use Case |
|----------|----------------|------------------|----------|
| DFS (Kahn's Algorithm) | O(V + E) | O(V) | General purpose, cycle detection |
| BFS (In-degree) | O(V + E) | O(V) | Finding all orderings, level-by-level |
| Tree Centroid Finding | O(V + E) | O(V) | Undirected trees, find center/minimize height |
| All Topological Sorts | O(V! × (V + E)) | O(V) | Small graphs, all permutations |

### References
- [techbridge : topological-sort](https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/)
- [DFS-based topological sort](https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html)
- [topological_sort.py](https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/topological_sort.py)
- [TopologicalSort.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSort.java)
- [NumberOfProvinces.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfProvinces.java) (Connected Components / Union Find)
- [MinimumHeightTrees.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BFS/MinimumHeightTrees.java) (Tree Centroid Finding)

## Problem Categories

### 1. Course Scheduling
Problems involving prerequisite relationships and course ordering.
- **Pattern**: Build dependency graph, check for cycles, find valid ordering
- **Key Problems**: LC 207, 210, 630, 1462

### 2. Task Scheduling
Problems involving task dependencies and parallel execution.
- **Pattern**: Find minimum time, parallel processing levels
- **Key Problems**: LC 1136, 2050, 1857

### 3. Lexicographical Ordering
Problems requiring smallest/largest lexicographical topological order.
- **Pattern**: Priority queue for ordering, alien dictionary
- **Key Problems**: LC 269, 953, 1203

### 4. Build Order & Dependencies
Problems involving build systems and package dependencies.
- **Pattern**: Detect cycles, find build order, handle groups
- **Key Problems**: LC 444, 802, 851

### 5. Graph Layering
Problems involving level-by-level processing in DAGs.
- **Pattern**: BFS with levels, longest path in DAG
- **Key Problems**: LC 2192, 2115, 1857

### 6. Cycle Detection & Safe States
Problems focused on detecting cycles and finding safe nodes.
- **Pattern**: Three-color DFS, safe states identification
- **Key Problems**: LC 802, 207, 1059

### 7. Connected Components (Union Find / DFS)
Problems involving finding connected components in undirected graphs.
- **Pattern**: Union Find with path compression, DFS/BFS traversal to count components
- **Key Problems**: LC 547, 200, 323, 684

### 8. Tree Centroid Finding
Problems involving finding the center/centroid of undirected trees.
- **Pattern**: Leaf trimming layer by layer, similar to topological sort for undirected trees
- **Key Problems**: LC 310, Tree diameter, Tree center

## Core Templates

### Template 1: BFS (Kahn's Algorithm) ⭐⭐⭐⭐⭐
```python
def topologicalSort_BFS(numNodes, edges):
    """
    BFS-based topological sort using in-degree tracking.
    Time: O(V + E), Space: O(V)
    """
    from collections import defaultdict, deque
    
    # Build graph and calculate in-degrees
    graph = defaultdict(list)
    in_degree = [0] * numNodes
    
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    # Initialize queue with nodes having no dependencies
    queue = deque([i for i in range(numNodes) if in_degree[i] == 0])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node)
        
        # Process neighbors
        for neighbor in graph[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)
    
    # Check for cycles
    return result if len(result) == numNodes else []
```

```java
// Java version
public List<Integer> topologicalSort_BFS(int numNodes, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    int[] inDegree = new int[numNodes];
    
    // Build graph
    for (int i = 0; i < numNodes; i++) {
        graph.put(i, new ArrayList<>());
    }
    
    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
        inDegree[edge[1]]++;
    }
    
    // BFS
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < numNodes; i++) {
        if (inDegree[i] == 0) queue.offer(i);
    }
    
    List<Integer> result = new ArrayList<>();
    while (!queue.isEmpty()) {
        int node = queue.poll();
        result.add(node);
        
        for (int neighbor : graph.get(node)) {
            if (--inDegree[neighbor] == 0) {
                queue.offer(neighbor);
            }
        }
    }
    
    return result.size() == numNodes ? result : new ArrayList<>();
}
```

### Template 2: DFS (Three-Color) ⭐⭐⭐⭐

```python
def topologicalSort_DFS(numNodes, edges):
    """
    DFS-based topological sort with three-color marking.
    Time: O(V + E), Space: O(V)
    """
    from collections import defaultdict
    
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
    
    # 0: white (unvisited), 1: gray (visiting), 2: black (visited)
    color = [0] * numNodes
    result = []
    has_cycle = False
    
    def dfs(node):
        nonlocal has_cycle
        if color[node] == 1:  # Gray = cycle detected
            has_cycle = True
            return
        if color[node] == 2:  # Black = already processed
            return
        
        color[node] = 1  # Mark as visiting
        for neighbor in graph[node]:
            dfs(neighbor)
        color[node] = 2  # Mark as visited
        result.append(node)  # Add to result in reverse order
    
    for i in range(numNodes):
        if color[i] == 0:
            dfs(i)
    
    return [] if has_cycle else result[::-1]
```

```java
// Java version
public List<Integer> topologicalSort_DFS(int numNodes, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    for (int i = 0; i < numNodes; i++) {
        graph.put(i, new ArrayList<>());
    }
    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
    }
    
    int[] color = new int[numNodes]; // 0: white, 1: gray, 2: black
    List<Integer> result = new ArrayList<>();
    boolean[] hasCycle = {false};
    
    for (int i = 0; i < numNodes; i++) {
        if (color[i] == 0) {
            dfs(i, graph, color, result, hasCycle);
        }
    }
    
    if (hasCycle[0]) return new ArrayList<>();
    Collections.reverse(result);
    return result;
}

private void dfs(int node, Map<Integer, List<Integer>> graph, 
                 int[] color, List<Integer> result, boolean[] hasCycle) {
    if (color[node] == 1) {
        hasCycle[0] = true;
        return;
    }
    if (color[node] == 2) return;
    
    color[node] = 1;
    for (int neighbor : graph.get(node)) {
        dfs(neighbor, graph, color, result, hasCycle);
    }
    color[node] = 2;
    result.add(node);
}
```

### Template 3: DFS (Stack-based)
```python
# V0
# IDEA : implement topologicalSortUtil, topologicalSort, and addEdge methods
# step 1) maintain a stack, save "ordering" nodes in it (and return in final step)
# step 2) init visited as [False]*self.V  (all nodes are NOT visited yet)
# step 3) iterate over all vertices in graph, if not visited, then run topologicalSortUtil
# step 4) return result (stack)
from collections import defaultdict
class Graph:
    def __init__(self, vertices):
        self.graph = defaultdict(list)
        self.V = vertices

    # for build graph
    def addEdge(self, u, v):
        self.graph[u].append(v)

    def topologicalSortUtil(self, v, visited, stack):
        visited[v] = True

        ### NOTE this !!! (self.graph[v])
        for k in self.graph[v]:
            if visited[k] == False:
                self.topologicalSortUtil(k, visited, stack)
        # stack.insert(0,v) # instead of insert v to idx = 0, we can still append v to stack and reverse it and return (e.g. return stack[::-1])
        """
        ### NOTE !! stack.append(v) is wrong, we SHOULD use  stack.insert(0,v)
        """
        stack.insert(0,v)

    def topologicalSort(self):
        visited = [False] * self.V
        stack = []
        ### NOTE this !!! (range(self.V))
        for v in range(self.V):
            # call tologicalSortUtil only if visited[v] == False (the vertice is not visited yet)
            if visited[v] == False:
                self.topologicalSortUtil(v, visited, stack)
        # return the result in inverse order
        return stack[::-1]

### TEST
{"A": 0, "B":1, "C":2, "D": 3}
v = 4
g = Graph(v)
g.addEdge(0, 1)
g.addEdge(0, 2)
g.addEdge(2, 3)
g.addEdge(3, 1)

print (g.graph)

# ans should be TableB, TableD, TableC, TableA.
r = g.topologicalSort()
print (r)
```

### Template 4: Lexicographical Order
```python
def topologicalSort_Lexicographical(numNodes, edges):
    """
    Find smallest lexicographical topological order.
    Time: O((V + E) log V), Space: O(V)
    """
    from collections import defaultdict
    import heapq
    
    graph = defaultdict(list)
    in_degree = [0] * numNodes
    
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    # Use min-heap for smallest lexicographical order
    heap = [i for i in range(numNodes) if in_degree[i] == 0]
    heapq.heapify(heap)
    result = []
    
    while heap:
        node = heapq.heappop(heap)
        result.append(node)
        
        for neighbor in graph[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                heapq.heappush(heap, neighbor)
    
    return result if len(result) == numNodes else []
```

```java
// Java version
public List<Integer> topologicalSort_Lexicographical(int numNodes, int[][] edges) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    int[] inDegree = new int[numNodes];
    
    for (int i = 0; i < numNodes; i++) {
        graph.put(i, new ArrayList<>());
    }
    
    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
        inDegree[edge[1]]++;
    }
    
    // Min-heap for lexicographical order
    PriorityQueue<Integer> pq = new PriorityQueue<>();
    for (int i = 0; i < numNodes; i++) {
        if (inDegree[i] == 0) pq.offer(i);
    }
    
    List<Integer> result = new ArrayList<>();
    while (!pq.isEmpty()) {
        int node = pq.poll();
        result.add(node);
        
        for (int neighbor : graph.get(node)) {
            if (--inDegree[neighbor] == 0) {
                pq.offer(neighbor);
            }
        }
    }
    
    return result.size() == numNodes ? result : new ArrayList<>();
}
```

### Template 5: All Topological Orders
```python
def allTopologicalSorts(graph, in_degree, path, result, visited):
    """
    Find all possible topological orderings.
    Time: O(V! × (V + E)), Space: O(V)
    """
    if len(path) == len(graph):
        result.append(path[:])
        return
    
    for node in range(len(graph)):
        if in_degree[node] == 0 and not visited[node]:
            # Choose node
            visited[node] = True
            path.append(node)
            
            # Update in-degrees
            for neighbor in graph[node]:
                in_degree[neighbor] -= 1
            
            # Recurse
            allTopologicalSorts(graph, in_degree, path, result, visited)
            
            # Backtrack
            for neighbor in graph[node]:
                in_degree[neighbor] += 1
            path.pop()
            visited[node] = False

# Usage
def findAllOrders(numNodes, edges):
    from collections import defaultdict
    
    graph = defaultdict(list)
    in_degree = [0] * numNodes
    
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    result = []
    visited = [False] * numNodes
    allTopologicalSorts(graph, in_degree, [], result, visited)
    return result
```

### Template 6: Parallel Task Scheduling
```python
def parallelTaskScheduling(numTasks, edges, times):
    """
    Find minimum time to complete all tasks with parallel execution.
    Time: O(V + E), Space: O(V)
    """
    from collections import defaultdict, deque
    
    graph = defaultdict(list)
    in_degree = [0] * numTasks
    
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    # Track completion time for each task
    completion_time = [0] * numTasks
    queue = deque()
    
    # Initialize with tasks having no dependencies
    for i in range(numTasks):
        if in_degree[i] == 0:
            queue.append(i)
            completion_time[i] = times[i]
    
    while queue:
        task = queue.popleft()
        
        for next_task in graph[task]:
            # Update completion time
            completion_time[next_task] = max(
                completion_time[next_task],
                completion_time[task] + times[next_task]
            )
            
            in_degree[next_task] -= 1
            if in_degree[next_task] == 0:
                queue.append(next_task)
    
    return max(completion_time)
```

```java
// Java version
public int parallelTaskScheduling(int numTasks, int[][] edges, int[] times) {
    Map<Integer, List<Integer>> graph = new HashMap<>();
    int[] inDegree = new int[numTasks];
    int[] completionTime = new int[numTasks];
    
    for (int i = 0; i < numTasks; i++) {
        graph.put(i, new ArrayList<>());
    }
    
    for (int[] edge : edges) {
        graph.get(edge[0]).add(edge[1]);
        inDegree[edge[1]]++;
    }
    
    Queue<Integer> queue = new LinkedList<>();
    for (int i = 0; i < numTasks; i++) {
        if (inDegree[i] == 0) {
            queue.offer(i);
            completionTime[i] = times[i];
        }
    }
    
    while (!queue.isEmpty()) {
        int task = queue.poll();
        
        for (int nextTask : graph.get(task)) {
            completionTime[nextTask] = Math.max(
                completionTime[nextTask],
                completionTime[task] + times[nextTask]
            );
            
            if (--inDegree[nextTask] == 0) {
                queue.offer(nextTask);
            }
        }
    }
    
    int maxTime = 0;
    for (int time : completionTime) {
        maxTime = Math.max(maxTime, time);
    }
    return maxTime;
}
```

### Template 7: Tree Centroid Finding (Leaf Trimming for Undirected Trees) ⭐⭐⭐
```python
def findMinHeightTrees(n, edges):
    """
    Find tree centroids using leaf trimming (similar to Kahn's Algorithm for undirected trees).
    Time: O(V + E), Space: O(V)

    Key Insight:
    - For undirected trees, leaves are nodes with degree = 1
    - Remove leaves layer by layer until 1-2 nodes remain
    - These remaining nodes are the centroids (MHT roots)
    - Different from standard topological sort: works on undirected trees, not DAGs
    """
    from collections import deque

    # Edge case: single node
    if n == 1:
        return [0]

    # Build adjacency list and track degrees
    graph = [[] for _ in range(n)]
    degree = [0] * n

    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)
        degree[u] += 1
        degree[v] += 1

    # Initialize queue with all leaf nodes (degree = 1)
    leaves = deque([i for i in range(n) if degree[i] == 1])

    # Trim leaves layer by layer
    remaining = n
    while remaining > 2:
        leaf_count = len(leaves)
        remaining -= leaf_count

        for _ in range(leaf_count):
            leaf = leaves.popleft()

            # Process neighbors of current leaf
            for neighbor in graph[leaf]:
                degree[neighbor] -= 1
                # If neighbor becomes a leaf, add to queue
                if degree[neighbor] == 1:
                    leaves.append(neighbor)

    # The remaining nodes (1 or 2) are the centroids
    return list(leaves)
```

```java
// Java version
public List<Integer> findMinHeightTrees(int n, int[][] edges) {
    // Edge case: single node
    if (n == 1) {
        return Collections.singletonList(0);
    }

    // Build adjacency list and track degrees
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        graph.add(new ArrayList<>());
    }

    int[] degree = new int[n];

    for (int[] edge : edges) {
        int u = edge[0], v = edge[1];
        graph.get(u).add(v);
        graph.get(v).add(u);
        degree[u]++;
        degree[v]++;
    }

    // Initialize queue with all leaf nodes (degree = 1)
    Queue<Integer> leaves = new LinkedList<>();
    for (int i = 0; i < n; i++) {
        if (degree[i] == 1) {
            leaves.offer(i);
        }
    }

    // Trim leaves layer by layer
    int remaining = n;
    while (remaining > 2) {
        int leafCount = leaves.size();
        remaining -= leafCount;

        for (int i = 0; i < leafCount; i++) {
            int leaf = leaves.poll();

            // Process neighbors of current leaf
            for (int neighbor : graph.get(leaf)) {
                degree[neighbor]--;
                // If neighbor becomes a leaf, add to queue
                if (degree[neighbor] == 1) {
                    leaves.offer(neighbor);
                }
            }
        }
    }

    // The remaining nodes (1 or 2) are the centroids
    return new ArrayList<>(leaves);
}
```

### Template 8: Union Find (Connected Components)
```python
class UnionFind:
    """
    Union Find with path compression and union by rank.
    Time: O(α(N)) per operation (nearly O(1)), Space: O(N)
    """
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
        self.count = n  # number of connected components

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, x, y):
        px, py = self.find(x), self.find(y)
        if px == py:
            return
        # union by rank
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        self.count -= 1

```

```java
// Java version
class UnionFind {
    int[] parent;
    int[] rank;
    int count;

    UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;
        // union by rank
        if (rank[px] < rank[py]) {
            parent[px] = py;
        } else if (rank[px] > rank[py]) {
            parent[py] = px;
        } else {
            parent[py] = px;
            rank[px]++;
        }
        count--;
    }
}

```

### Template 9: Topological Sort + DP on an **Implicit** DAG ⭐⭐⭐⭐⭐

**Key Idea**: the graph is never given as an edge list — it is *implied* by a comparison rule
(`grid[a] < grid[b]` ⇒ edge `a → b`). Build in/out-degrees on the fly, then peel with Kahn's.
The **number of BFS layers** is the longest path in the DAG (no cycles are possible because the
edge rule is strictly increasing).

**When to use**: "longest increasing path / chain" on a grid or matrix, or any DP on a DAG where
you need nodes processed in dependency order without recursion depth risk.

**The twist vs Template 1**: peel from the **sinks** (out-degree 0 = local maxima) instead of the
sources, so each layer is one step of the DP. Peeling from sources works too — just reverse the
comparison.

```java
// java
// LC 329 - Longest Increasing Path in a Matrix
// IDEA: implicit DAG (cell -> strictly larger neighbour) + Kahn's peeling from sinks;
//       answer = number of peeling layers = longest path length
public class Solution {
    // time = O(m * n), space = O(m * n)
    private static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
        int m = matrix.length, n = matrix[0].length;

        // step 1) out-degree = how many strictly-larger neighbours a cell points to
        int[][] outdeg = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int[] d : DIRS) {
                    int x = i + d[0], y = j + d[1];
                    if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] > matrix[i][j]) {
                        outdeg[i][j]++;
                    }
                }
            }
        }

        // step 2) seed the queue with the sinks (local maxima)
        Deque<int[]> q = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (outdeg[i][j] == 0) q.offer(new int[]{i, j});
            }
        }

        // step 3) peel layer by layer; each layer = +1 on the longest path
        int length = 0;
        while (!q.isEmpty()) {
            length++;
            for (int sz = q.size(); sz > 0; sz--) {
                int[] cur = q.poll();
                int i = cur[0], j = cur[1];
                for (int[] d : DIRS) {
                    int x = i + d[0], y = j + d[1];
                    // walk backwards along the implicit edge (smaller neighbour)
                    if (x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] < matrix[i][j]) {
                        if (--outdeg[x][y] == 0) q.offer(new int[]{x, y});
                    }
                }
            }
        }
        return length;
    }
}
```

```python
# python
# LC 329 - Longest Increasing Path in a Matrix
# IDEA: implicit DAG (cell -> strictly larger neighbour) + Kahn's peeling from sinks
from collections import deque

def longestIncreasingPath(matrix):
    # time = O(m * n), space = O(m * n)
    if not matrix or not matrix[0]:
        return 0

    m, n = len(matrix), len(matrix[0])
    dirs = ((1, 0), (-1, 0), (0, 1), (0, -1))

    # step 1) out-degree = number of strictly-larger neighbours
    outdeg = [[0] * n for _ in range(m)]
    for i in range(m):
        for j in range(n):
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < m and 0 <= y < n and matrix[x][y] > matrix[i][j]:
                    outdeg[i][j] += 1

    # step 2) seed with sinks (local maxima)
    q = deque((i, j) for i in range(m) for j in range(n) if outdeg[i][j] == 0)

    # step 3) peel; layer count == longest increasing path
    length = 0
    while q:
        length += 1
        for _ in range(len(q)):
            i, j = q.popleft()
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < m and 0 <= y < n and matrix[x][y] < matrix[i][j]:
                    outdeg[x][y] -= 1
                    if outdeg[x][y] == 0:
                        q.append((x, y))
    return length
```

**Note**: the classic alternative is DFS + memo (`dp[i][j] = 1 + max(dp[neighbour])`), also
O(m·n). Prefer the Kahn's version when the grid is large enough that recursion could blow the
stack, or when the interviewer explicitly asks for topological sort.

**Same shape, explicit graph**: LC 1857 (Largest Color Value in a Directed Graph) — carry a
`count[node][26]` DP array through Kahn's instead of a single layer counter; if the queue drains
before all nodes are popped, there is a cycle → return `-1`.

---

### Template 10: In-Degree Signature (answer read straight off the degrees)

**Key Idea**: some "graph" problems never need the traversal at all — the answer is fully
determined by **in-degree / out-degree counts**. Recognising this turns a Medium into 3 lines.

**Key Insight (LC 1557)**: on a **DAG**, a node with in-degree 0 can only be reached by starting
there, and every other node is reachable from some in-degree-0 node. So the set of in-degree-0
nodes is both necessary and sufficient — and therefore the unique minimum answer.

```java
// java
// LC 1557 - Minimum Number of Vertices to Reach All Nodes
// IDEA: on a DAG, the minimal start set == exactly the nodes with in-degree 0
public class Solution {
    // time = O(V + E), space = O(V)
    public List<Integer> findSmallestSetOfVertices(int n, List<List<Integer>> edges) {
        boolean[] hasIncoming = new boolean[n];
        for (List<Integer> e : edges) {
            hasIncoming[e.get(1)] = true;
        }

        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!hasIncoming[i]) res.add(i);
        }
        return res;
    }
}
```

```python
# python
# LC 1557 - Minimum Number of Vertices to Reach All Nodes
# IDEA: on a DAG, the minimal start set == exactly the nodes with in-degree 0
def findSmallestSetOfVertices(n, edges):
    # time = O(V + E), space = O(V)
    has_incoming = [False] * n
    for _, v in edges:
        has_incoming[v] = True
    return [i for i in range(n) if not has_incoming[i]]
```

#### Variation A — degree *signature* lookup: LC 997 Find the Town Judge

**Twist**: instead of in-degree 0, look for one exact fingerprint — `inDegree = n - 1` **and**
`outDegree = 0`. Fold both into a single `score = inDegree - outDegree` array and scan for `n - 1`.

```java
// java
// LC 997 - Find the Town Judge
// IDEA: judge == in-degree (n-1) and out-degree 0  =>  score = in - out == n - 1
public class Solution {
    // time = O(V + E), space = O(V)
    public int findJudge(int n, int[][] trust) {
        int[] score = new int[n + 1];
        for (int[] t : trust) {
            score[t[0]]--;   // t[0] trusts someone -> out-degree
            score[t[1]]++;   // t[1] is trusted     -> in-degree
        }
        for (int i = 1; i <= n; i++) {
            if (score[i] == n - 1) return i;
        }
        return -1;
    }
}
```

```python
# python
# LC 997 - Find the Town Judge
def findJudge(n, trust):
    # time = O(V + E), space = O(V)
    score = [0] * (n + 1)
    for a, b in trust:
        score[a] -= 1
        score[b] += 1
    for i in range(1, n + 1):
        if score[i] == n - 1:
            return i
    return -1
```

#### Variation B — degrees + one reachability pass: LC 1361 Validate Binary Tree Nodes

**Twist**: degrees alone are not enough. A valid binary tree needs **three** conditions —
(1) every node has in-degree ≤ 1, (2) exactly one node has in-degree 0 (the root), and
(3) all `n` nodes are reachable from that root. Conditions 1+2 without 3 still allow a valid-looking
tree plus a **detached cycle**, which is exactly the trap case.

```java
// java
// LC 1361 - Validate Binary Tree Nodes
// IDEA: in-degree <= 1 for all + exactly one in-degree-0 root + root reaches all n nodes
public class Solution {
    // time = O(N), space = O(N)
    public boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        // (1) no node may have two parents
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) {
            for (int c : new int[]{leftChild[i], rightChild[i]}) {
                if (c != -1 && ++indeg[c] > 1) return false;
            }
        }

        // (2) exactly one root
        int root = -1;
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                if (root != -1) return false;   // 2+ roots => forest
                root = i;
            }
        }
        if (root == -1) return false;           // 0 roots => everything is in a cycle

        // (3) the root must reach every node (else a detached cycle exists)
        int seen = 1;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            for (int c : new int[]{leftChild[node], rightChild[node]}) {
                if (c != -1) {
                    seen++;
                    stack.push(c);
                }
            }
        }
        return seen == n;
    }
}
```

```python
# python
# LC 1361 - Validate Binary Tree Nodes
def validateBinaryTreeNodes(n, leftChild, rightChild):
    # time = O(N), space = O(N)
    # (1) no node may have two parents
    indeg = [0] * n
    for i in range(n):
        for c in (leftChild[i], rightChild[i]):
            if c != -1:
                indeg[c] += 1
                if indeg[c] > 1:
                    return False

    # (2) exactly one root
    roots = [i for i in range(n) if indeg[i] == 0]
    if len(roots) != 1:
        return False

    # (3) root must reach every node
    seen, stack = 1, [roots[0]]
    while stack:
        node = stack.pop()
        for c in (leftChild[node], rightChild[node]):
            if c != -1:
                seen += 1
                stack.append(c)
    return seen == n
```

## Problem Classification

| Problem | Difficulty | Category | Key Technique |
|---------|------------|----------|---------------|
| [207. Course Schedule](https://leetcode.com/problems/course-schedule/) | Medium | Course Scheduling | Cycle Detection |
| [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | Medium | Course Scheduling | BFS/DFS |
| [269. Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) | Hard | Lexicographical | Character Ordering |
| [310. Minimum Height Trees](https://leetcode.com/problems/minimum-height-trees/) | Medium | Tree Centroid | Leaf Trimming |
| [444. Sequence Reconstruction](https://leetcode.com/problems/sequence-reconstruction/) | Medium | Build Order | Unique Ordering |
| [630. Course Schedule III](https://leetcode.com/problems/course-schedule-iii/) | Hard | Course Scheduling | Greedy + Heap |
| [802. Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/) | Medium | Cycle Detection | Reverse Graph |
| [851. Loud and Rich](https://leetcode.com/problems/loud-and-rich/) | Medium | Graph Layering | DFS + Memoization |
| [953. Verifying an Alien Dictionary](https://leetcode.com/problems/verifying-an-alien-dictionary/) | Easy | Lexicographical | Order Validation |
| [1059. All Paths from Source Lead to Destination](https://leetcode.com/problems/all-paths-from-source-lead-to-destination/) | Medium | Cycle Detection | DFS |
| [1136. Parallel Courses](https://leetcode.com/problems/parallel-courses/) | Medium | Task Scheduling | Level BFS |
| [1203. Sort Items by Groups Respecting Dependencies](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | Hard | Build Order | Double Topological |
| [1462. Course Schedule IV](https://leetcode.com/problems/course-schedule-iv/) | Medium | Course Scheduling | Transitive Closure |
| [1857. Largest Color Value in a Directed Graph](https://leetcode.com/problems/largest-color-value-in-a-directed-graph/) | Hard | Graph Layering | DP on DAG |
| [2050. Parallel Courses III](https://leetcode.com/problems/parallel-courses-iii/) | Hard | Task Scheduling | Time Calculation |
| [2115. Find All Possible Recipes from Given Supplies](https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/) | Medium | Build Order | Modified BFS |
| [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/) | Medium | Connected Components | Union Find / DFS |
| [2192. All Ancestors of a Node in a Directed Acyclic Graph](https://leetcode.com/problems/all-ancestors-of-a-node-in-a-directed-acyclic-graph/) | Medium | Graph Layering | DFS/BFS |
| [329. Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/) | Hard | Graph Layering | Implicit DAG + Kahn's (Template 9) |
| [1557. Minimum Number of Vertices to Reach All Nodes](https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/) | Medium | Graph Layering | In-degree 0 set (Template 10) |
| [997. Find the Town Judge](https://leetcode.com/problems/find-the-town-judge/) | Easy | Degree Counting | In/out-degree signature (Template 10-A) |
| [1361. Validate Binary Tree Nodes](https://leetcode.com/problems/validate-binary-tree-nodes/) | Medium | Cycle Detection | In-degree + reachability (Template 10-B) |

### Problem Patterns by Category

#### Course Scheduling Problems
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Basic Cycle Detection | 207 | Check if DAG exists |
| Find Valid Order | 210 | Return topological order |
| With Time Constraints | 630 | Greedy + priority queue |
| Query Prerequisites | 1462 | Floyd-Warshall/DFS |

#### Task Scheduling Problems
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Minimum Time | 1136, 2050 | Level-wise BFS |
| Parallel Execution | 1136 | Count levels |
| With Durations | 2050 | DP on completion times |

#### Lexicographical Ordering
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Character Order | 269 | Build graph from comparisons |
| Verify Order | 953 | Check consistency |
| Custom Comparator | 269 | Extract rules from examples |

#### Build Order & Dependencies
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Unique Reconstruction | 444 | Queue size always 1 |
| Recipe Dependencies | 2115 | Handle initial supplies |
| Group Dependencies | 1203 | Two-level topological sort |

#### Graph Layering
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Find Ancestors | 2192 | Reverse graph traversal |
| Richest Reachable | 851 | DFS with memoization |
| Max Path Value | 1857 | DP on DAG |
| Longest Path on Implicit DAG | 329 | Edges implied by `a < b`; layer count = path length |

#### Degree Counting (no traversal needed)
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Minimal Start Set | 1557 | On a DAG the answer is exactly the in-degree-0 nodes |
| Node Fingerprint | 997 | Judge = in-degree `n-1` and out-degree `0` |
| Validate Tree Shape | 1361 | in-degree ≤ 1 + one root + root reaches all n |

#### Cycle Detection
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Safe States | 802 | Reverse graph + outdegree |
| All Paths Safe | 1059 | DFS with path tracking |
| Detect Any Cycle | 207 | Three-color DFS |

#### Connected Components (Union Find / DFS)
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Count Components | 547, 323 | Union Find or DFS traversal |
| Detect Redundant Edge | 684 | Union Find cycle detection |
| Number of Islands | 200 | DFS/BFS on grid |

#### Tree Centroid Finding
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Find Tree Centers | 310 | Leaf trimming layer by layer (multi-source BFS inward) |
| Minimum Height Trees | 310 | BFS from leaves, stop at 1-2 nodes |
| Tree Diameter Related | 310, 1245 | Centroid is at diameter midpoint |
| Leaf Pruning / Coin Collection | 2603 | Trim leaves to remove unnecessary nodes |
| Sum of Distances in Tree | 834 | Rerooting DP, related to centroid concept |

## Decision Framework

```text
START: Topological Sort Problem
│
├── Working with undirected tree?
│   │
│   ├── YES → Finding tree center/centroid?
│   │   │
│   │   ├── YES → Use Template 7 (Tree Centroid Finding)
│   │   │
│   │   └── NO → Continue
│   │
│   └── NO → Continue
│
├── Need all valid orderings?
│   │
│   ├── YES → Use Template 5 (Backtracking)
│   │
│   └── NO → Continue
│
├── Need lexicographical order?
│   │
│   ├── YES → Use Template 4 (Priority Queue)
│   │
│   └── NO → Continue
│
├── Need parallel execution time?
│   │
│   ├── YES → Use Template 6 (Level BFS)
│   │
│   └── NO → Continue
│
├── Need to count connected components?
│   │
│   ├── YES → Use Template 8 (Union Find)
│   │
│   └── NO → Continue
│
├── Need cycle detection only?
│   │
│   ├── YES → Use Template 2 (Three-Color DFS)
│   │
│   └── NO → Continue
│
└── DEFAULT → Use Template 1 (BFS Kahn's Algorithm)
```

> The `UnionFind` class above is the template; LC 547 applies it three ways —
> union-find, DFS and BFS — in
> [topology_sorting_examples.md](./topology_sorting_examples.md#8-number-of-provinces--lc-547).


## Worked Examples

Eight problems, grouped by the shape of the dependency they encode, live in
**[topology_sorting_examples.md](./topology_sorting_examples.md)**:

| Group | Problems | The template they exercise |
|---|---|---|
| [Course scheduling & ordering](./topology_sorting_examples.md#course-scheduling--ordering) | LC 210, 207, 269, 444 | Kahn's (T1), three-colour DFS (T2), lexicographical (T4) |
| [Layering & parallel scheduling](./topology_sorting_examples.md#layering--parallel-scheduling) | LC 1136 | parallel task scheduling (T6) |
| [Cycle detection & safe states](./topology_sorting_examples.md#cycle-detection--safe-states) | LC 802 | three-colour DFS (T2) |
| [Undirected graphs — components & centroids](./topology_sorting_examples.md#undirected-graphs--components--centroids) | LC 310, 547 | leaf trimming (T7), union find (T8) |


## Summary & Interview Tips

### Common Pitfalls
1. **Forgetting Cycle Detection**: Always check if result size equals number of nodes
2. **Wrong Edge Direction**: Remember edges go from prerequisite to dependent
3. **Not Handling Disconnected Components**: Process all unvisited nodes
4. **Incorrect In-degree Initialization**: Ensure all nodes are included
5. **Missing Edge Cases**: Empty graph, single node, self-loops
6. **Confusing Degree vs In-degree**: For undirected trees use total degree, for DAGs use in-degree
7. **Wrong Stopping Condition**: For tree centroids, stop at ≤2 nodes (not when queue is empty)

### Key Insights
1. **BFS vs DFS**: BFS is easier for finding one order, DFS for all orders
2. **In-degree Tracking**: Nodes with 0 in-degree can be processed
3. **Three-Color DFS**: White (unvisited), Gray (visiting), Black (visited)
4. **Reverse Graph**: Useful for problems like safe states
5. **Level Processing**: For parallel execution and minimum time
6. **Tree Centroid Finding**: For undirected trees, use degree (not in-degree), trim leaves layer by layer until 1-2 nodes remain
7. **Undirected vs Directed**: Undirected trees need bidirectional edges and degree tracking, while DAGs use in-degree

### Interview Approach
1. **Clarify Requirements**:
   - Is the graph guaranteed to be a DAG?
   - Do we need all orderings or just one?
   - Are there any ordering preferences?

2. **Choose Algorithm**:
   - Default to BFS (Kahn's) for simplicity
   - Use DFS for recursive problems
   - Use priority queue for lexicographical order

3. **Handle Edge Cases**:
   - Empty graph
   - Single node
   - Disconnected components
   - Cycles in graph

4. **Optimize if Needed**:
   - Early termination for cycle detection
   - Space optimization with in-place modifications
   - Time optimization with better data structures

### Time/Space Complexity Summary
| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Build Graph | O(E) | O(V + E) | Adjacency list |
| Calculate In-degrees | O(E) | O(V) | Array or map |
| BFS/DFS Traversal | O(V + E) | O(V) | Visit each node/edge once |
| Cycle Detection | O(V + E) | O(V) | Three-color marking |
| All Orderings | O(V! × E) | O(V) | Exponential for all permutations |

### Related Concepts
- **Strongly Connected Components**: Tarjan's/Kosaraju's algorithm
- **Shortest Path in DAG**: Topological sort + relaxation
- **Critical Path Method**: Project scheduling
- **Dependency Resolution**: Package managers, build systems
- **Dataflow Analysis**: Compiler optimization
