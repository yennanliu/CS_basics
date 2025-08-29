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

## Problem Categories

### 1. Tree Traversal Problems
- **Level Order Traversal**: LC 102, 107, 103
- **Binary Tree Paths**: LC 257, 1022
- **Right Side View**: LC 199
- **Vertical Order**: LC 314

### 2. Shortest Path Problems
- **Unweighted Graphs**: LC 127 (Word Ladder)
- **Grid Navigation**: LC 1730 (Shortest Path to Food)
- **Multi-source**: LC 542 (01 Matrix), LC 1162 (As Far from Land)

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

### Tree BFS
- **Time**: O(n) - visit each node once
- **Space**: O(w) - maximum width of tree

### Graph BFS  
- **Time**: O(V + E) - visit each vertex and edge once
- **Space**: O(V) - queue and visited set

### Grid BFS
- **Time**: O(m × n) - visit each cell once
- **Space**: O(m × n) - worst case queue size

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