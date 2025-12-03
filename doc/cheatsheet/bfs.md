# BFS (Breadth-First Search)

## Overview
Breadth-First Search is a graph traversal algorithm that explores nodes level by level, visiting all nodes at the current depth before moving to nodes at the next depth.

### Key Properties
- **Complete**: Always finds a solution if one exists
- **Optimal**: Finds shortest path in unweighted graphs
- **Space Complex**: O(b^d) where b=branching factor, d=depth
- **Time Complex**: O(V + E) for graphs, O(n) for trees

### Core Characteristics
- Uses **Queue** data structure (FIFO - First In, First Out)
- Guarantees **shortest path** in unweighted graphs
- Explores nodes **level by level** (breadth first, then depth)
- Memory intensive compared to DFS

## Fundamental Concepts

### 1. Node States (for cycle detection)
- **State 0**: Not visited (white)
- **State 1**: Currently being processed (gray) 
- **State 2**: Completely processed (black)

### 2. BFS vs DFS Comparison
| Aspect | BFS | DFS |
|--------|-----|-----|
| Data Structure | Queue | Stack |
| Memory | O(w) width | O(h) height |
| Shortest Path | ✅ Yes | ❌ No |
| Complete | ✅ Yes | ❌ No (infinite spaces) |

## Implementation Patterns

### Pattern 1: Basic Tree BFS
```python
from collections import deque

def bfs_tree(root):
    if not root:
        return []
    
    queue = deque([root])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node.val)
        
        if node.left:
            queue.append(node.left)
        if node.right:
            queue.append(node.right)
    
    return result
```

### Pattern 2: Level-by-Level BFS
```python
def bfs_levels(root):
    if not root:
        return []
    
    queue = deque([root])
    levels = []
    
    while queue:
        level_size = len(queue)
        current_level = []
        
        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        levels.append(current_level)
    
    return levels
```

### Pattern 3: Graph BFS with Visited Set
```python
def bfs_graph(start, graph):
    queue = deque([start])
    visited = set([start])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node)
        
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
    
    return result
```

### Pattern 4: Multi-Source BFS
```python
def multi_source_bfs(grid, sources):
    """Start BFS from multiple sources simultaneously"""
    queue = deque(sources)  # All sources at once
    visited = set(sources)
    
    while queue:
        x, y = queue.popleft()
        
        for dx, dy in [(0,1), (0,-1), (1,0), (-1,0)]:
            nx, ny = x + dx, y + dy
            
            if (0 <= nx < len(grid) and 0 <= ny < len(grid[0]) 
                and (nx, ny) not in visited):
                visited.add((nx, ny))
                queue.append((nx, ny))
```

### Pattern 5: BFS with Path Tracking
```python
def bfs_with_path(start, target):
    queue = deque([(start, [start])])
    visited = {start}

    while queue:
        node, path = queue.popleft()

        if node == target:
            return path

        for neighbor in get_neighbors(node):
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append((neighbor, path + [neighbor]))

    return None
```

### Pattern 6: Sort + Repeated BFS (Sequential Shortest Paths)
```java
/**
 * Pattern: Sort targets by priority, then repeatedly call BFS to find shortest paths
 * Use case: Visit multiple targets in specific order, minimize total travel distance
 * Key insight: BFS guarantees shortest path between each pair of consecutive targets
 *
 * Time: O(k × m × n) where k = number of targets, m×n = grid size
 * Space: O(m × n) for visited array in each BFS call
 */
public int sortAndBFS(List<List<Integer>> grid) {
    int rows = grid.size();
    int cols = grid.get(0).size();

    // Step 1: Collect all targets and sort by priority (e.g., value)
    List<int[]> targets = new ArrayList<>();
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid.get(r).get(c) > 1) {
                // Store [value, row, col]
                targets.add(new int[]{grid.get(r).get(c), r, c});
            }
        }
    }

    // Sort by value (ascending) - defines visit order
    targets.sort(Comparator.comparingInt(a -> a[0]));

    // Step 2: Sequentially visit each target using BFS
    int totalSteps = 0;
    int startR = 0, startC = 0; // Starting position

    for (int[] target : targets) {
        int targetR = target[1];
        int targetC = target[2];

        // Find shortest path from current position to next target
        int steps = bfs(grid, startR, startC, targetR, targetC);

        if (steps == -1) {
            return -1; // Target unreachable
        }

        totalSteps += steps;

        // Update starting position for next iteration
        startR = targetR;
        startC = targetC;
    }

    return totalSteps;
}

/**
 * Standard BFS to find shortest path in grid
 * Returns minimum steps from (sr, sc) to (tr, tc), or -1 if unreachable
 */
private int bfs(List<List<Integer>> grid, int sr, int sc, int tr, int tc) {
    if (sr == tr && sc == tc) return 0;

    int rows = grid.size();
    int cols = grid.get(0).size();

    Queue<int[]> queue = new LinkedList<>();
    queue.offer(new int[]{sr, sc});

    boolean[][] visited = new boolean[rows][cols];
    visited[sr][sc] = true;

    int[][] dirs = {{0,1}, {0,-1}, {1,0}, {-1,0}};
    int steps = 0;

    while (!queue.isEmpty()) {
        int size = queue.size();
        steps++;

        for (int i = 0; i < size; i++) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // Check bounds and obstacles
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols
                    || visited[nr][nc] || grid.get(nr).get(nc) == 0) {
                    continue;
                }

                // Found target
                if (nr == tr && nc == tc) {
                    return steps;
                }

                visited[nr][nc] = true;
                queue.offer(new int[]{nr, nc});
            }
        }
    }

    return -1; // Unreachable
}
```

**Concrete Example: LC 675 - Cut Off Trees for Golf Event**
```
Problem: Cut trees in forest from shortest to tallest, return minimum steps
Grid: [[1,2,3],    Trees: (0,1)=2, (0,2)=3, (1,2)=4, (2,0)=7, (2,1)=6, (2,2)=5
       [0,0,4],    Sorted: 2→3→4→5→6→7
       [7,6,5]]

Path: (0,0) →[1 step]→ (0,1) cut 2
      (0,1) →[2 steps]→ (0,2) cut 3
      (0,2) →[1 step]→ (1,2) cut 4
      (1,2) →[1 step]→ (2,2) cut 5
      (2,2) →[1 step]→ (2,1) cut 6
      (2,1) →[1 step]→ (2,0) cut 7
Total: 1+2+1+1+1+1 = 7 steps (Note: Problem statement has different expected output)

Key insight: Must cut in sorted order, BFS finds shortest path between each pair
```

**Pattern Characteristics:**
- **Sort Phase**: O(k log k) where k = number of targets
- **BFS Phase**: O(k) iterations, each BFS is O(m×n) for grid
- **Total Time**: O(k log k + k×m×n) ≈ O(k×m×n) when k << m×n
- **Space**: O(m×n) for visited array (created fresh each BFS)

**When to Use This Pattern:**
- Must visit targets in specific order (sorted by value, priority, etc.)
- Need shortest path between consecutive targets
- Targets are sparse in the space
- Cannot use dynamic programming due to order constraints

**Key Variations:**
1. **Different Sort Criteria**: Sort by distance, value, custom priority
2. **Modified Grid**: Update grid after visiting target (set to 1, remove obstacle)
3. **Early Termination**: Return immediately if any target unreachable
4. **Optimization**: Use A* instead of BFS for large grids

**Similar Problems:**
- LC 675: Cut Off Trees for Golf Event (sort trees by height)
- LC 1293: Shortest Path with Obstacles Elimination (BFS with state)
- LC 864: Shortest Path to Get All Keys (BFS with key collection state)
- LC 1091: Shortest Path in Binary Matrix (basic BFS shortest path)
- LC 317: Shortest Distance from All Buildings (multi-source BFS)

## Problem Categories

### 1. Tree Traversal Problems
- **Level Order Traversal**: LC 102, 107, 103
- **Binary Tree Paths**: LC 257, 1022
- **Right Side View**: LC 199
- **Vertical Order**: LC 314

### 2. Shortest Path Problems
- **Unweighted Graphs**: LC 127 (Word Ladder)
- **Grid Navigation**: LC 1730 (Shortest Path to Food), LC 1091 (Shortest Path in Binary Matrix)
- **Multi-source**: LC 542 (01 Matrix), LC 1162 (As Far from Land), LC 317 (Shortest Distance from All Buildings)
- **Sequential Targets**: LC 675 (Cut Off Trees for Golf Event - Sort + Repeated BFS)
- **State-Based BFS**: LC 864 (Shortest Path to Get All Keys), LC 1293 (Shortest Path with Obstacles Elimination)

### 3. Graph Structure Problems
- **Cycle Detection**: LC 207 (Course Schedule)
- **Connected Components**: LC 200 (Number of Islands)
- **Graph Validation**: LC 261 (Graph Valid Tree)
- **Clone Graph**: LC 133

### 4. Matrix/Grid Problems
- **Surrounded Regions**: LC 130
- **Walls and Gates**: LC 286
- **Maze Problems**: LC 490

## Time & Space Complexity

### BFS Time Complexity Analysis

BFS time complexity depends on the graph representation:

#### 🔹 Graph Representations

**Adjacency List (most common in practice):**
- Each vertex is enqueued/dequeued once → O(V)
- Each edge is explored at most once → O(E)
- ✅ **Total = O(V + E)**

**Adjacency Matrix:**
- Checking all neighbors of a vertex costs O(V)
- Doing this for all vertices costs O(V²)
- ✅ **Total = O(V²)**

#### Detailed Breakdown by Data Structure

**Tree BFS**
- **Time**: O(n) - visit each node once
- **Space**: O(w) - maximum width of tree
- **Explanation**: Each node visited exactly once, queue stores at most one level

**Graph BFS (Adjacency List)**
- **Time**: O(V + E) - visit each vertex and edge once
- **Space**: O(V) - queue and visited set
- **Explanation**:
  - Vertex processing: Each vertex enqueued/dequeued once = O(V)
  - Edge processing: Each edge examined once = O(E)
  - Queue space: At most O(V) vertices
  - Visited set: O(V) vertices

**Graph BFS (Adjacency Matrix)**
- **Time**: O(V²) - check all possible edges
- **Space**: O(V) - queue and visited set
- **Explanation**:
  - For each vertex, check all V possible neighbors
  - Total vertices × neighbors per vertex = V × V = O(V²)

**Grid BFS**
- **Time**: O(m × n) - visit each cell once
- **Space**: O(m × n) - worst case queue size
- **Explanation**:
  - Each cell visited at most once
  - Queue can contain at most all cells in worst case
  - Grid is essentially a graph with m×n vertices and 4-directional edges

#### Performance Comparison Table

| Graph Type | Representation | Time Complexity | Space Complexity | Best For |
|------------|----------------|-----------------|------------------|----------|
| **Sparse Graph** | Adjacency List | O(V + E) | O(V) | E << V² |
| **Dense Graph** | Adjacency Matrix | O(V²) | O(V²) | E ≈ V² |
| **Tree** | Parent-Child Links | O(n) | O(w) | Hierarchical data |
| **Grid** | 2D Array | O(m × n) | O(m × n) | Spatial problems |

#### Why O(V + E) for Adjacency List?

```python
# Detailed analysis of BFS with adjacency list
def bfs_analysis(graph, start):
    queue = deque([start])        # O(1)
    visited = {start}             # O(1)

    while queue:                  # Executes at most V times
        vertex = queue.popleft()  # O(1) - each vertex dequeued once

        # This inner loop runs exactly deg(vertex) times
        for neighbor in graph[vertex]:  # Total across all vertices = E
            if neighbor not in visited:  # O(1) with set
                visited.add(neighbor)    # O(1) - each vertex added once
                queue.append(neighbor)   # O(1) - each vertex enqueued once

    # Analysis:
    # - Outer while loop: O(V) iterations
    # - Inner for loop: Sum of deg(v) for all v = 2E (undirected) or E (directed)
    # - Each operation inside: O(1)
    # Total: O(V + E)
```

## Common Mistakes & Tips

### ❌ Common Mistakes
1. Using `queue.pop()` instead of `queue.popleft()` with list
2. Not handling visited set in graphs (infinite loops)
3. Forgetting level-by-level processing when needed
4. Incorrect boundary checking in grid problems

### ✅ Best Practices
1. Use `collections.deque` for better performance
2. Always use visited set for graph problems
3. Check boundaries before adding to queue in grid problems
4. Consider multi-source BFS for optimization
5. Track level/distance when needed for shortest path

## Advanced Techniques

### Bidirectional BFS
```python
def bidirectional_bfs(start, end):
    """Meet in the middle - faster for long paths"""
    if start == end:
        return 0
    
    forward = {start: 0}
    backward = {end: 0}
    queue_forward = deque([start])
    queue_backward = deque([end])
    
    while queue_forward or queue_backward:
        # Expand smaller frontier
        if len(forward) <= len(backward):
            if expand_level(queue_forward, forward, backward):
                return True
        else:
            if expand_level(queue_backward, backward, forward):
                return True
    
    return False
```

### BFS with Priority (Dijkstra-like)
```python
import heapq

def weighted_bfs(start, end, graph):
    """BFS variant for weighted graphs"""
    heap = [(0, start)]
    distances = {start: 0}
    
    while heap:
        dist, node = heapq.heappop(heap)
        
        if node == end:
            return dist
            
        if dist > distances.get(node, float('inf')):
            continue
            
        for neighbor, weight in graph[node]:
            new_dist = dist + weight
            if new_dist < distances.get(neighbor, float('inf')):
                distances[neighbor] = new_dist
                heapq.heappush(heap, (new_dist, neighbor))
    
    return -1
```

## Quick Reference

### When to Use BFS
- Finding shortest path in unweighted graphs
- Level-order tree traversal
- Finding connected components
- Checking if graph is bipartite
- Web crawling (breadth-first exploration)

### When NOT to Use BFS
- Deep trees/graphs with limited memory
- Only need to find ANY path (not shortest)
- Weighted graphs (use Dijkstra instead)
- Need to explore all paths (use DFS)

### Key LeetCode Problems
| Difficulty | Problem | Key Concept |
|------------|---------|-------------|
| Easy | LC 102 | Level-order traversal |
| Medium | LC 127 | Shortest path transformation |
| Medium | LC 200 | Connected components |
| Medium | LC 542 | Multi-source BFS |
| Hard | LC 317 | Multi-source optimization |
| Hard | LC 675 | Sort + Repeated BFS (sequential targets) |
| Hard | LC 864 | BFS with state (key collection) |
| Hard | LC 1293 | BFS with state (obstacle elimination) |