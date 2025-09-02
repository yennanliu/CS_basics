# Graph Algorithms

## Overview
**Graph algorithms** are techniques for solving problems on graph data structures consisting of vertices (nodes) and edges (connections between nodes).

### Key Properties
- **Time Complexity**: O(V + E) for traversal, varies for other algorithms
- **Space Complexity**: O(V) for adjacency list, O(V²) for matrix
- **Core Idea**: Model relationships and connections between entities
- **When to Use**: Network problems, dependencies, paths, connectivity
- **Key Algorithms**: BFS, DFS, Dijkstra, Union-Find, Topological Sort

### Core Characteristics
- **Directed vs Undirected**: One-way or two-way edges
- **Weighted vs Unweighted**: Edges with or without costs
- **Cyclic vs Acyclic**: Contains cycles or not
- **Connected vs Disconnected**: All nodes reachable or not

### Graph Representations
- **Adjacency List**: Space O(V + E), efficient for sparse graphs
- **Adjacency Matrix**: Space O(V²), efficient for dense graphs
- **Edge List**: Space O(E), simple but less efficient

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_processing_problem.png"></p>

## Problem Categories

### **Category 1: Graph Traversal**
- **Description**: Explore all nodes using BFS or DFS
- **Examples**: LC 200 (Number of Islands), LC 133 (Clone Graph)
- **Pattern**: Visit all connected components

### **Category 2: Shortest Path**
- **Description**: Find minimum distance between nodes
- **Examples**: LC 743 (Network Delay), LC 787 (Cheapest Flights)
- **Pattern**: Dijkstra, Bellman-Ford, Floyd-Warshall

### **Category 3: Union-Find (DSU)**
- **Description**: Detect cycles, find connected components
- **Examples**: LC 684 (Redundant Connection), LC 721 (Accounts Merge)
- **Pattern**: Union by rank, path compression

### **Category 4: Topological Sort**
- **Description**: Order nodes with dependencies
- **Examples**: LC 207 (Course Schedule), LC 210 (Course Schedule II)
- **Pattern**: DFS or Kahn's algorithm (BFS)

### **Category 5: Bipartite Graphs**
- **Description**: Check if graph can be colored with 2 colors
- **Examples**: LC 785 (Is Graph Bipartite), LC 886 (Possible Bipartition)
- **Pattern**: BFS/DFS with coloring

### **Category 6: Minimum Spanning Tree**
- **Description**: Connect all nodes with minimum cost
- **Examples**: LC 1135 (Connecting Cities), LC 1584 (Min Cost Connect Points)
- **Pattern**: Kruskal's or Prim's algorithm

## Templates & Algorithms

### Template Comparison Table
| Template Type | Use Case | Time Complexity | When to Use |
|---------------|----------|-----------------|-------------|
| **BFS** | Level-order, shortest path | O(V + E) | Unweighted shortest path |
| **DFS** | All paths, cycle detection | O(V + E) | Explore all possibilities |
| **Union-Find** | Connected components | O(α(n)) | Dynamic connectivity |
| **Dijkstra** | Weighted shortest path | O((V+E)logV) | Non-negative weights |
| **Topological** | Dependencies | O(V + E) | DAG ordering |

### Universal Graph Template
```python
def graph_algorithm(n, edges):
    # Build adjacency list
    graph = collections.defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)  # For undirected
    
    # Track visited nodes
    visited = set()
    
    # Process each component
    result = 0
    for node in range(n):
        if node not in visited:
            # Process component
            process_component(node, graph, visited)
            result += 1
    
    return result
```

### Template 1: BFS Traversal
```python
def bfs_template(graph, start):
    """Breadth-first search template"""
    from collections import deque
    
    visited = set([start])
    queue = deque([start])
    level = 0
    
    while queue:
        # Process level by level
        size = len(queue)
        for _ in range(size):
            node = queue.popleft()
            
            # Process node
            for neighbor in graph[node]:
                if neighbor not in visited:
                    visited.add(neighbor)
                    queue.append(neighbor)
        level += 1
    
    return level
```

### Template 2: DFS Traversal
```python
def dfs_template(graph, start):
    """Depth-first search template"""
    visited = set()
    path = []
    
    def dfs(node):
        visited.add(node)
        path.append(node)
        
        for neighbor in graph[node]:
            if neighbor not in visited:
                dfs(neighbor)
        
        # Backtrack if needed
        # path.pop()
    
    dfs(start)
    return visited
```

### Template 3: Union-Find (DSU)
```python
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
        self.count = n
    
    def find(self, x):
        """Find with path compression"""
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """Union by rank"""
        px, py = self.find(x), self.find(y)
        if px == py:
            return False
        
        if self.rank[px] < self.rank[py]:
            px, py = py, px
        self.parent[py] = px
        if self.rank[px] == self.rank[py]:
            self.rank[px] += 1
        
        self.count -= 1
        return True
    
    def connected(self, x, y):
        return self.find(x) == self.find(y)
```

### Template 4: Topological Sort (DFS)
```python
def topological_sort_dfs(n, edges):
    """Topological sort using DFS"""
    graph = collections.defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
    
    # 0: unvisited, 1: visiting, 2: visited
    state = [0] * n
    result = []
    
    def dfs(node):
        if state[node] == 1:  # Cycle detected
            return False
        if state[node] == 2:  # Already processed
            return True
        
        state[node] = 1  # Mark as visiting
        for neighbor in graph[node]:
            if not dfs(neighbor):
                return False
        
        state[node] = 2  # Mark as visited
        result.append(node)
        return True
    
    for i in range(n):
        if state[i] == 0:
            if not dfs(i):
                return []  # Cycle exists
    
    return result[::-1]
```

### Template 5: Topological Sort (BFS/Kahn's)
```python
def topological_sort_bfs(n, edges):
    """Kahn's algorithm for topological sort"""
    from collections import deque
    
    graph = collections.defaultdict(list)
    indegree = [0] * n
    
    for u, v in edges:
        graph[u].append(v)
        indegree[v] += 1
    
    queue = deque([i for i in range(n) if indegree[i] == 0])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node)
        
        for neighbor in graph[node]:
            indegree[neighbor] -= 1
            if indegree[neighbor] == 0:
                queue.append(neighbor)
    
    return result if len(result) == n else []
```

### Template 6: Bipartite Check
```python
def is_bipartite(graph):
    """Check if graph is bipartite using BFS"""
    n = len(graph)
    colors = [-1] * n
    
    for start in range(n):
        if colors[start] != -1:
            continue
        
        queue = deque([start])
        colors[start] = 0
        
        while queue:
            node = queue.popleft()
            for neighbor in graph[node]:
                if colors[neighbor] == -1:
                    colors[neighbor] = 1 - colors[node]
                    queue.append(neighbor)
                elif colors[neighbor] == colors[node]:
                    return False
    
    return True
```

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_rep1.png"></p>

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_rep2.png"></p>


## Problems by Pattern

### **Graph Traversal Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Number of Islands | 200 | DFS/BFS on grid | Medium |
| Max Area of Island | 695 | DFS with counting | Medium |
| Clone Graph | 133 | BFS/DFS with map | Medium |
| Pacific Atlantic Water | 417 | Multi-source DFS | Medium |
| Word Ladder | 127 | BFS shortest path | Hard |
| Surrounded Regions | 130 | DFS from boundary | Medium |

### **Shortest Path Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Network Delay Time | 743 | Dijkstra | Medium |
| Cheapest Flights K Stops | 787 | Modified Dijkstra | Medium |
| Path with Min Effort | 1631 | Dijkstra on grid | Medium |
| Bus Routes | 815 | BFS on routes | Hard |
| Shortest Path Binary Matrix | 1091 | BFS | Medium |

### **Union-Find Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Number of Connected Components | 323 | Basic Union-Find | Medium |
| Redundant Connection | 684 | Detect cycle | Medium |
| Accounts Merge | 721 | Union-Find with map | Medium |
| Number of Provinces | 547 | Union-Find or DFS | Medium |
| Satisfiability of Equality | 990 | Union-Find | Medium |

### **Topological Sort Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Course Schedule | 207 | Cycle detection | Medium |
| Course Schedule II | 210 | Topological order | Medium |
| Alien Dictionary | 269 | Build graph + sort | Hard |
| Minimum Height Trees | 310 | Leaf removal | Medium |
| Parallel Courses | 1136 | Level-wise BFS | Medium |

### **Bipartite Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Is Graph Bipartite | 785 | BFS coloring | Medium |
| Possible Bipartition | 886 | DFS coloring | Medium |

### **Advanced Graph Problems**
| Problem | LC # | Key Technique | Difficulty |
|---------|------|---------------|------------|
| Critical Connections | 1192 | Tarjan's algorithm | Hard |
| Find Eventual Safe States | 802 | Cycle detection | Medium |
| Reconstruct Itinerary | 332 | Hierholzer's algorithm | Hard |
| Minimum Spanning Tree | 1135 | Kruskal/Prim | Medium |

#### 1-1-1) Number of Islands

- LC 200

```java
// java
void dfs(char[][] grid, int r, int c){
    int nr = grid.length;
    int nc = grid[0].length;

    if (r < 0 || c < 0 || r >= nr || c >= nc || grid[r][c] == '0') {
        return;
    }

    grid[r][c] = '0';

    /** NOTE here !!!*/
    dfs_1(grid, r - 1, c);
    dfs_1(grid, r + 1, c);
    dfs_1(grid, r, c - 1);
    dfs_1(grid, r, c + 1);
}

public int numIslands_1(char[][] grid) {
    if (grid == null || grid.length == 0) {
        return 0;
    }

    int nr = grid.length;
    int nc = grid[0].length;
    int num_islands = 0;

    for (int r = 0; r < nr; ++r) {
        for (int c = 0; c < nc; ++c) {
            if (grid[r][c] == '1') {
                ++num_islands;
                dfs_1(grid, r, c);
            }
        }
    }

    return num_islands;
}

```


#### 1-1-2) Max Area of Island

- LC 695

```java
// java
int[][] grid;
boolean[][] seen;

public int area(int r, int c) {
    if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length ||
            seen[r][c] || grid[r][c] == 0)
        return 0;
    seen[r][c] = true;

    /** NOTE !!!*/
    return (1 + area(r+1, c) + area(r-1, c)
            + area(r, c-1) + area(r, c+1));
}

public int maxAreaOfIsland_1(int[][] grid) {
    this.grid = grid;
    seen = new boolean[grid.length][grid[0].length];
    int ans = 0;
    for (int r = 0; r < grid.length; r++) {
        for (int c = 0; c < grid[0].length; c++) {
            ans = Math.max(ans, area(r, c));
        }
    }
    return ans;
}
```

## 2) LC Example

### 2-1) Closest Leaf in a Binary Tree
```python 
# 742 Closest Leaf in a Binary Tree
import collections
class Solution:
    # search via DFS
    def findClosestLeaf(self, root, k):
        self.start = None
        self.buildGraph(root, None, k)
        q, visited = [root], set()
        #q, visited = [self.start], set() # need to validate this
        self.graph = collections.defaultdict(list)
        while q:
            for i in range(len(q)):
                cur = q.pop(0) # this is dfs
                # add cur to visited, NOT to visit this node again
                visited.add(cur)
                ### NOTICE HERE 
                # if not cur.left and not cur.right: means this is the leaf (HAS NO ANY left/right node) of the tree
                # so the first value of this is what we want, just return cur.val as answer directly
                if not cur.left and not cur.right:
                    # return the answer
                    return cur.val
                # if not find the leaf, then go through all neighbors of current node, and search again
                for node in self.graph:
                    if node not in visited: # need to check if "if node not in visited" or "if node in visited"
                        q.append(node)

    # build graph via DFS
    # node : current node
    # parent : parent of current node
    def buildGraph(self, node, parent, k):
        if not node:
            return
        # if node.val == k, THEN GET THE start point FROM current "node",
        # then build graph based on above
        if node.val == k:
            self.start = node
        if parent:
            self.graph[node].append(parent)
            self.graph[parent].append(node)
        self.buildGraph(node.left, node, k)
        self.buildGraph(node.right, node, k)

```

### 2-2) Number of Connected Components in an Undirected Graph
```python
# LC 323 Number of Connected Components in an Undirected Graph
# V0
# IDEA : DFS
class Solution:
    def countComponents(self, n, edges):
        def helper(u):
            if u in pair:
                for v in pair[u]:
                    if v not in visited:
                        visited.add(v)
                        helper(v)
            
        pair = collections.defaultdict(set)
        for u,v in edges:
            pair[u].add(v)
            pair[v].add(u)
        count = 0
        visited = set()
        for i in range(n):
            if i not in visited:
                helper(i)
                count+=1
        return count
```

### 2-3) Clone Graph
```python
# LC 133. Clone Graph

# V0
# IDEA : BFS
class Solution(object):
    def cloneGraph(self, node):
        if not node:
            return
        q = [node]
        """
        NOTE !!! : we init res as Node(node.val, [])
          -> since Node has structure as below :

          class Node:
            def __init__(self, val = 0, neighbors = None):
                self.val = val
                self.neighbors = neighbors if neighbors is not None else []
        """
        res = Node(node.val, [])
        """
        NOTE !!! : we use dict as visited,
                   and we use node as visited dict key 
        """
        visited = dict()
        visited[node] = res
        while q:
            #t = q.pop(0) # this works as well
            t = q.pop(-1)
            if not t:
                continue
            for n in t.neighbors:
                if n not in visited:
                    """
                    NOTE !!! : we need to 
                         -> use n as visited key
                         -> use Node(n.val, []) as visited value
                    """
                    visited[n] = Node(n.val, [])
                    q.append(n)
                """
                NOTE !!! 
                    -> we need to append visited[n] to visited[t].neighbors
                """
                visited[t].neighbors.append(visited[n])
        return res

# V0
# IDEA : DFS
# NOTE :
#  -> 1) we init node via : node_copy = Node(node.val, [])
#  -> 2) we copy graph via dict
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: Node
        :rtype: Node
        """
        node_copy = self.dfs(node, dict())
        return node_copy
    
    def dfs(self, node, hashd):
        if not node: return None
        if node in hashd: return hashd[node]
        node_copy = Node(node.val, [])
        hashd[node] = node_copy
        for n in node.neighbors:
            n_copy = self.dfs(n, hashd)
            if n_copy:
                node_copy.neighbors.append(n_copy)
        return node_copy
```


### 2-4) Bus Routes
```python
# LC 815. Bus Routes
# V0
# IDEA : BFS + GRAPH
class Solution(object):
    def numBusesToDestination(self, routes, S, T):
        # edge case:
        if S == T:
            return 0
        to_routes = collections.defaultdict(set)
        for i, route in enumerate(routes):
            for j in route:
                to_routes[j].add(i)
        bfs = [(S, 0)]
        seen = set([S])
        for stop, bus in bfs:
            if stop == T:
                return bus
            for i in to_routes[stop]:
                for j in routes[i]:
                    if j not in seen:
                        bfs.append((j, bus + 1))
                        seen.add(j)
                routes[i] = []  # seen route
        return -1
```

### 2-5) Course Schedule
```java
// java
// V0
// IDEA : DFS (fix by gpt) (NOTE : there is also TOPOLOGICAL SORT solution)
// NOTE !!! instead of maintain status (0,1,2), below video offers a simpler approach
//      -> e.g. use a set, recording the current visiting course, if ANY duplicated (already in set) course being met,
//      -> means "cyclic", so return false directly
// https://www.youtube.com/watch?v=EgI5nU9etnU
public boolean canFinish(int numCourses, int[][] prerequisites) {
    // Initialize adjacency list for storing prerequisites
    /**
     *  NOTE !!!
     *
     *  init prerequisites map
     *  {course : [prerequisites_array]}
     *  below init map with null array as first step
     */
    Map<Integer, List<Integer>> preMap = new HashMap<>();
    for (int i = 0; i < numCourses; i++) {
        preMap.put(i, new ArrayList<>());
    }

    // Populate the adjacency list with prerequisites
    /**
     *  NOTE !!!
     *
     *  update prerequisites map
     *  {course : [prerequisites_array]}
     *  so we go through prerequisites,
     *  then append each course's prerequisites to preMap
     */
    for (int[] pair : prerequisites) {
        int crs = pair[0];
        int pre = pair[1];
        preMap.get(crs).add(pre);
    }

    /** NOTE !!!
     *
     *  init below set for checking if there is "cyclic" case
     */
    // Set for tracking courses during the current DFS path
    Set<Integer> visiting = new HashSet<>();

    // Recursive DFS function
    for (int c = 0; c < numCourses; c++) {
        if (!dfs(c, preMap, visiting)) {
            return false;
        }
    }
    return true;
}

private boolean dfs(int crs, Map<Integer, List<Integer>> preMap, Set<Integer> visiting) {
    /** NOTE !!!
     *
     *  if visiting contains current course,
     *  means there is a "cyclic",
     *  (e.g. : needs to take course a, then can take course b, and needs to take course b, then can take course a)
     *  so return false directly
     */
    if (visiting.contains(crs)) {
        return false;
    }
    /**
     *  NOTE !!!
     *
     *  if such course has NO preRequisite,
     *  return true directly
     */
    if (preMap.get(crs).isEmpty()) {
        return true;
    }

    /**
     *  NOTE !!!
     *
     *  add current course to set (Set<Integer> visiting)
     */
    visiting.add(crs);
    for (int pre : preMap.get(crs)) {
        if (!dfs(pre, preMap, visiting)) {
            return false;
        }
    }
    /**
     *  NOTE !!!
     *
     *  remove current course from set,
     *  since already finish visiting
     *
     *  e.g. undo changes
     */
    visiting.remove(crs);
    preMap.get(crs).clear(); // Clear prerequisites as the course is confirmed to be processed
    return true;
}
```

## Decision Framework

### Pattern Selection Strategy

```
Graph Algorithm Selection Flowchart:

1. What is the problem asking for?
   ├── Find shortest path → Continue to 2
   ├── Check connectivity → Use Union-Find or DFS/BFS
   ├── Order with dependencies → Use Topological Sort
   ├── Detect cycles → Use DFS with states or Union-Find
   └── Traverse all nodes → Use BFS or DFS

2. For shortest path problems:
   ├── Unweighted graph → Use BFS
   ├── Non-negative weights → Use Dijkstra
   ├── Negative weights → Use Bellman-Ford
   └── All pairs → Use Floyd-Warshall

3. For connectivity problems:
   ├── Static graph → Use DFS/BFS once
   ├── Dynamic connections → Use Union-Find
   └── Count components → Use either approach

4. For traversal problems:
   ├── Level-by-level → Use BFS
   ├── Path finding → Use DFS with backtracking
   └── State space search → Use BFS for optimal

5. Is the graph special?
   ├── Tree → Simpler DFS/BFS
   ├── DAG → Topological sort possible
   ├── Bipartite → Two-coloring
   └── Grid → Treat as implicit graph
```

### Algorithm Selection Guide

| Problem Type | Best Algorithm | Time | When to Use |
|-------------|---------------|------|-------------|
| Single-source shortest (unweighted) | BFS | O(V+E) | Simple shortest path |
| Single-source shortest (weighted) | Dijkstra | O((V+E)logV) | Non-negative weights |
| All-pairs shortest | Floyd-Warshall | O(V³) | Dense graphs |
| Cycle detection | DFS | O(V+E) | Directed graphs |
| Connected components | Union-Find | O(α(n)) | Dynamic connectivity |
| Topological order | Kahn's/DFS | O(V+E) | Task scheduling |
| Minimum spanning tree | Kruskal/Prim | O(ElogE) | Network design |

## Summary & Quick Reference

### Complexity Quick Reference
| Algorithm | Time Complexity | Space Complexity | Notes |
|-----------|-----------------|------------------|-------|
| BFS/DFS | O(V + E) | O(V) | Standard traversal |
| Dijkstra | O((V+E)logV) | O(V) | With binary heap |
| Bellman-Ford | O(VE) | O(V) | Handles negative weights |
| Floyd-Warshall | O(V³) | O(V²) | All pairs |
| Union-Find | O(α(n)) | O(V) | Near constant |
| Topological Sort | O(V + E) | O(V) | Linear time |

### Graph Building Patterns

#### **Adjacency List**
```python
# For edges list
graph = defaultdict(list)
for u, v in edges:
    graph[u].append(v)
    graph[v].append(u)  # Undirected

# For weighted edges
graph = defaultdict(list)
for u, v, w in edges:
    graph[u].append((v, w))
```

#### **Adjacency Matrix**
```python
# For unweighted
graph = [[0] * n for _ in range(n)]
for u, v in edges:
    graph[u][v] = 1
    graph[v][u] = 1  # Undirected

# For weighted
graph = [[float('inf')] * n for _ in range(n)]
for u, v, w in edges:
    graph[u][v] = w
```

### Common Patterns & Tricks

#### **Visited Tracking**
```python
# Set for simple visited
visited = set()

# Array for state tracking
# 0: unvisited, 1: visiting, 2: visited
state = [0] * n

# Dictionary for path reconstruction
parent = {}
```

#### **Grid as Graph**
```python
# 4-directional movement
directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

# 8-directional movement
directions = [(0, 1), (1, 0), (0, -1), (-1, 0),
              (1, 1), (1, -1), (-1, 1), (-1, -1)]

# Check bounds
def is_valid(r, c, rows, cols):
    return 0 <= r < rows and 0 <= c < cols
```

#### **Cycle Detection Patterns**
```python
# Directed graph - DFS with states
def has_cycle_directed(graph):
    # 0: unvisited, 1: visiting, 2: visited
    state = [0] * n
    
    def dfs(node):
        if state[node] == 1:  # Back edge
            return True
        if state[node] == 2:
            return False
        
        state[node] = 1
        for neighbor in graph[node]:
            if dfs(neighbor):
                return True
        state[node] = 2
        return False

# Undirected graph - Union-Find
def has_cycle_undirected(edges):
    uf = UnionFind(n)
    for u, v in edges:
        if not uf.union(u, v):
            return True  # Already connected
    return False
```

### Problem-Solving Steps
1. **Identify graph type**: Directed/undirected, weighted/unweighted
2. **Choose representation**: Adjacency list vs matrix
3. **Select algorithm**: Based on problem requirements
4. **Handle edge cases**: Empty graph, disconnected components
5. **Track state properly**: Visited nodes, paths, distances
6. **Optimize if needed**: Space or time improvements

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- Not handling disconnected components
- Incorrect visited state management
- Missing cycle detection in recursive DFS
- Wrong graph representation choice
- Not considering edge cases (self-loops, multiple edges)

**✅ Best Practices:**
- Use adjacency list for sparse graphs
- Clear visited tracking strategy
- Handle both directed and undirected cases
- Consider using Union-Find for dynamic connectivity
- Test with disconnected components

### Interview Tips
1. **Clarify graph properties**: Directed? Weighted? Connected?
2. **Draw small examples**: Visualize the problem
3. **Choose right representation**: List vs matrix
4. **State complexities**: Time and space upfront
5. **Handle edge cases**: Empty, single node, cycles
6. **Optimize incrementally**: Start simple, then improve

### Related Topics
- **Trees**: Special case of graphs (connected, acyclic)
- **Dynamic Programming**: DP on graphs (paths, trees)
- **Greedy Algorithms**: MST algorithms
- **Heap/Priority Queue**: Used in Dijkstra, Prim's
- **Recursion/Backtracking**: DFS implementation

### 2-6) Find Eventual Safe States
```java
// java
// LC 802

// V1-0
// IDEA : DFS
// KEY : check if there is a "cycle" on a node
// https://www.youtube.com/watch?v=v5Ni_3bHjzk
// https://zxi.mytechroad.com/blog/graph/leetcode-802-find-eventual-safe-states/
public List<Integer> eventualSafeNodes(int[][] graph) {
    // init
    int n = graph.length;
    State[] states = new State[n];
    for (int i = 0; i < n; i++) {
        states[i] = State.UNKNOWN;
    }

    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < n; i++) {
        // if node is with SAFE state, add to result
        if (dfs(graph, i, states) == State.SAFE) {
            result.add(i);
        }
    }
    return result;
}

private enum State {
    UNKNOWN, VISITING, SAFE, UNSAFE
}

private State dfs(int[][] graph, int node, State[] states) {
    /**
     * NOTE !!!
     *  if a node with "VISITING" state,
     *  but is visited again (within the other iteration)
     *  -> there must be a cycle
     *  -> this node is UNSAFE
     */
    if (states[node] == State.VISITING) {
        return states[node] = State.UNSAFE;
    }
    /**
     * NOTE !!!
     *  if a node is not with "UNKNOWN" state,
     *  -> update its state
     */
    if (states[node] != State.UNKNOWN) {
        return states[node];
    }

    /**
     * NOTE !!!
     *  update node state as VISITING
     */
    states[node] = State.VISITING;
    for (int next : graph[node]) {
        /**
         * NOTE !!!
         *   for every sub node, if any one them
         *   has UNSAFE state,
         *   -> set and return node state as UNSAFE directly
         */
        if (dfs(graph, next, states) == State.UNSAFE) {
            return states[node] = State.UNSAFE;
        }
    }

    /**
     * NOTE !!!
     *   if can pass all above checks
     *   -> this is node has SAFE state
     */
    return states[node] = State.SAFE;
}
```