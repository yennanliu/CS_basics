---
layout: cheatsheet
title: "Topological Sorting"
description: "Linear ordering algorithm for directed acyclic graphs (DAGs)"
category: "Algorithm"
difficulty: "Medium"
tags: ["topological-sort", "dag", "dependency", "ordering"]
patterns:
  - Dependency resolution
  - DAG processing
  - Linear ordering
---

# Topological Sorting - Complete Guide

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

### 7. Tree Centroid Finding
Problems involving finding the center/centroid of undirected trees.
- **Pattern**: Leaf trimming layer by layer, similar to topological sort for undirected trees
- **Key Problems**: LC 310, Tree diameter, Tree center

## Core Templates

### Template 1: BFS (Kahn's Algorithm)
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

# Java version
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

### Template 2: DFS (Three-Color)

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

# Java version
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

# Java version
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

# Java version
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

### Template 7: Tree Centroid Finding (Leaf Trimming for Undirected Trees)
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

# Java version
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
| [2192. All Ancestors of a Node in a Directed Acyclic Graph](https://leetcode.com/problems/all-ancestors-of-a-node-in-a-directed-acyclic-graph/) | Medium | Graph Layering | DFS/BFS |

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

#### Cycle Detection
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Safe States | 802 | Reverse graph + outdegree |
| All Paths Safe | 1059 | DFS with path tracking |
| Detect Any Cycle | 207 | Three-color DFS |

#### Tree Centroid Finding
| Pattern | Problems | Key Insight |
|---------|----------|-------------|
| Find Tree Centers | 310 | Leaf trimming layer by layer |
| Minimum Height Trees | 310 | BFS from leaves, stop at 1-2 nodes |
| Tree Diameter Related | 310 | Centroid is at diameter midpoint |

## Decision Framework

```
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
├── Need cycle detection only?
│   │
│   ├── YES → Use Template 2 (Three-Color DFS)
│   │
│   └── NO → Continue
│
└── DEFAULT → Use Template 1 (BFS Kahn's Algorithm)
```

## 2) LC Example

### 2-2) Course Schedule II

```java
// java
// LC 210
public class CourseSchedule2 {

    // V0
    // IDEA : TOPOLOGICAL SORT (fixed by gpt)
    // ref : https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSortV2.java
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if (numCourses == 1) {
            return new int[]{% raw %}{0}{% endraw %};
        }

        // topologic ordering
        List<Integer> ordering = topologicalSort(numCourses, prerequisites);
        //System.out.println(">>> ordering = " + ordering);
        if (ordering == null){
            return new int[]{% raw %}{}{% endraw %};
        }
        int[] res = new int[numCourses];
        for (int x = 0; x < ordering.size(); x++) {
            int val = ordering.get(x);
            //System.out.println(val);
            res[x] = val;
        }

        return res;
    }

    public List<Integer> topologicalSort(int numNodes, int[][] edges) {
        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            graph.get(from).add(to);
            inDegree[to]++;
        }

        // Step 2: Initialize a queue with nodes that have in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            /**
             * NOTE !!!
             *
             *  we add ALL nodes with degree = 0 to queue at init step
             */
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        // Step 3: Process the nodes in topological order
        while (!queue.isEmpty()) {
            /**
             * NOTE !!!
             *
             *  ONLY "degree = 0"  nodes CAN be added to queue
             *
             *  -> so we can add whatever node from queue to final result (topologicalOrder)
             */
            int current = queue.poll();
            topologicalOrder.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor] -= 1;
                /**
                 * NOTE !!!
                 *
                 *  if a node "degree = 0"  means this node can be ACCESSED now,
                 *
                 *  -> so we need to add it to the queue (for adding to topologicalOrder in the following while loop iteration)
                 */
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // If topologicalOrder does not contain all nodes, there was a cycle in the graph
        if (topologicalOrder.size() != numNodes) {
            //throw new IllegalArgumentException("The graph has a cycle, so topological sort is not possible.");
            return null;
        }

        /** NOTE !!! reverse ordering */
        Collections.reverse(topologicalOrder);
        return topologicalOrder;
    }

```

```python
# LC 210 Course Schedule II
# V0
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
from collections import defaultdict
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return [x for x in range(numCourses)]
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g, res):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!

                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g, res):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            """
            NOTE : the main difference between LC 207, 210

            -> we append idx to res (our ans)
            """
            res.append(idx)
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        res = []
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g, res):
                return []
        return res

# V0'
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
import collections
class Solution:
    def findOrder(self, numCourses, prerequisites):
        # build graph
        _graph = collections.defaultdict(list)
        for i in range(len(prerequisites)):
            _graph[prerequisites[i][0]].append(prerequisites[i][1])

        visited = [0] * numCourses
        res = []
        for i in range(numCourses):
            if not self.dfs(_graph, visited, i, res):
                return []
        print ("res = " + str(res))
        return res

    # 0 : unknown, 1 :visiting, 2 : visited    
    def dfs(self, _graph, visited, i, res):
        if visited[i] == 1:
            return False
        if visited[i] == 2:
            return True
        visited[i] = 1
        for item in _graph[i]:
            if not self.dfs(_graph, visited, item, res):
                return False
        visited[i] = 2
        res.append(i)
        return True

# V0'
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: List[int]
        """
        graph = collections.defaultdict(list)
        for u, v in prerequisites:
            graph[u].append(v)
        # 0 = Unknown, 1 = visiting, 2 = visited
        visited = [0] * numCourses
        path = []
        for i in range(numCourses):
            ### NOTE : if not a valid "prerequisites", then will return NULL list
            if not self.dfs(graph, visited, i, path):
                return []
        return path
    
    def dfs(self, graph, visited, i, path):
        # 0 = Unknown, 1 = visiting, 2 = visited
        if visited[i] == 1: return False
        if visited[i] == 2: return True
        visited[i] = 1
        for j in graph[i]:
            if not self.dfs(graph, visited, j, path):
                ### NOTE : the quit condition
                return False
        visited[i] = 2
        path.append(i)
        return True
```

### 2-2) Course Schedule

```java
// java
// LC 207
// same as LC 210
```

```python
# LC 207 Course Schedule
# NOTE : there are also bracktrack, dfs approachs for this problem
# V0
# IDEA : LC Course Schedule II 
from collections import defaultdict
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return [x for x in range(numCourses)]
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g, res):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!
                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g, res):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            """
            NOTE : the main difference between LC 207, 210
            -> we append idx to res (our ans)
            """
            res.append(idx)
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        res = []
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g, res):
                return False #[]
        return len(res) > 0

# V1
# IDEA : Topological Sort
# https://leetcode.com/problems/course-schedule/solution/
class GNode(object):
    """  data structure represent a vertex in the graph."""
    def __init__(self):
        self.inDegrees = 0
        self.outNodes = []

class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        from collections import defaultdict, deque
        # key: index of node; value: GNode
        graph = defaultdict(GNode)

        totalDeps = 0
        for relation in prerequisites:
            nextCourse, prevCourse = relation[0], relation[1]
            graph[prevCourse].outNodes.append(nextCourse)
            graph[nextCourse].inDegrees += 1
            totalDeps += 1

        # we start from courses that have no prerequisites.
        # we could use either set, stack or queue to keep track of courses with no dependence.
        nodepCourses = deque()
        for index, node in graph.items():
            if node.inDegrees == 0:
                nodepCourses.append(index)

        removedEdges = 0
        while nodepCourses:
            # pop out course without dependency
            course = nodepCourses.pop()

            # remove its outgoing edges one by one
            for nextCourse in graph[course].outNodes:
                graph[nextCourse].inDegrees -= 1
                removedEdges += 1
                # while removing edges, we might discover new courses with prerequisites removed, i.e. new courses without prerequisites.
                if graph[nextCourse].inDegrees == 0:
                    nodepCourses.append(nextCourse)

        if removedEdges == totalDeps:
            return True
        else:
            # if there are still some edges left, then there exist some cycles
            # Due to the dead-lock (dependencies), we cannot remove the cyclic edges
            return False

# V0
# IDEA : DFS + topological sort 
# dfs
from collections import defaultdict
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return True
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!

                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        #print ("g = " + str(p))
        # dfs
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g):
                return False
        return True

# V0
# IDEA : DFS + topological sort 
import collections
class Solution:
    def canFinish(self, numCourses, prerequisites):
        _graph = collections.defaultdict(list)
        for i in range(len(prerequisites)):
            _graph[prerequisites[i][0]].append(prerequisites[i][1])

        visited = [0] * numCourses
        for i in range(numCourses):
            if not self.dfs(_graph, visited, i):
                return False
        return True

    # 0 : unknown, 1 :visiting, 2 : visited    
    def dfs(self, _graph, visited, i):
        if visited[i] == 1:
            return False
        if visited[i] == 2:
            return True
        visited[i] = 1
        for item in _graph[i]:
            if not self.dfs(_graph, visited, item):
                return False
        visited[i] = 2
        return True

# V0'
# IDEA : BFS + topological sort 
from collections import defaultdict, deque
class Solution:
    def canFinish(self, numCourses, prerequisites):
        degree = defaultdict(int)   
        graph = defaultdict(set)
        q = deque()
        
        # init the courses with 0 deg
        for i in range(numCourses):
            degree[i] = 0
        
        # add 1 to degree of course that needs prereq
        # build edge from prerequisite to child course (directed graph)
        for pair in prerequisites:
            degree[pair[0]] += 1
            graph[pair[1]].add(pair[0])
        
        # start bfs queue with all classes that dont have a prerequisite
        for key, val in degree.items():
            if val == 0:
                q.append(key)
                
        stack = []
        
        while q:
            curr = q.popleft()
            stack.append(curr)
            for child in graph[curr]:
                degree[child] -= 1
                if degree[child] == 0:
                    q.append(child)
        
        return len(stack) == numCourses
```

### 2-3) Alien Dictionary

```java
// java
// LC 269

 // V0
    // IDEA: TOPOLOGICAL SORT (neetcode, comments created by gpt)
    // TOPOLOGICAL SORT : `degrees`, map, BFS
    public String foreignDictionary(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        // NOTE !!! we use `map` as degrees storage
        Map<Character, Integer> indegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        }

        /**
         *   NOTE !!! below
         *
         *   -> build the character `ordering`
         *
         *  Loop Over Adjacent Word Pairs
         *
         *
         *
         * for (int i = 0; i < words.length - 1; i++) {
         *     String w1 = words[i];
         *     String w2 = words[i + 1];
         *
         * We are comparing each pair of consecutive
         * words in the list (words[i] and words[i+1]).
         *
         * This is important because the alien language is
         * sorted — and order relationships only exist between adjacent words.
         *
         */
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            /**
             *  NOTE !!! below
             *
             *
             * int minLen = Math.min(w1.length(), w2.length());
             * if (w1.length() > w2.length() &&
             *     w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
             *     return "";
             * }
             *
             *
             * ->  This checks for a prefix violation:
             * If w1 is longer than w2, and w2 is a prefix of w1, that’s `invalid`.
             *
             * Example:
             *
             *   words = ["apple", "app"]
             *
             *
             * Here, app comes after apple,
             * which is wrong because in a lexicographically sorted language,
             * a shorter prefix should come before the longer word.
             *
             * -> Hence, we return "" to signal an invalid dictionary order.
             *
             */
            int minLen = Math.min(w1.length(), w2.length());
            // handle `ordering` edge case
            // e.g. words = ["apple", "app"]
            if (w1.length() > w2.length() &&
                    w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
                return "";
            }

            /**
             *  NOTE !!! below
             *
             *
             *  This loop compares characters at each position j in w1 and w2.
             *  The first place where they differ defines the ordering.
             *
             *
             *  Example :
             *
             *    w1 = "wrt"
             *    w2 = "wrf"
             *
             *
             *
             *  At index 2, 't' and 'f' differ → so we know:
             * 't' < 'f' → Add a directed edge: t → f
             *
             * adj.get(w1.charAt(j)).add(w2.charAt(j)): Adds this edge in the adjacency list.
             *
             * indegree.put(...): Increments in-degree of the target node.
             *
             *
             * NOTE !!!
             *
             * -> Then we break — we don’t look at further characters
             *     -> because they don’t affect the order.
             *
             *
             */
            // compare the `first different character within w1, w2`
            // The first place where they differ defines the ordering.
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    if (!adj.get(w1.charAt(j)).contains(w2.charAt(j))) {
                        adj.get(w1.charAt(j)).add(w2.charAt(j));
                        indegree.put(w2.charAt(j),
                                indegree.get(w2.charAt(j)) + 1);
                    }
                    break;
                }
            }
        }

        Queue<Character> q = new LinkedList<>();
        for (char c : indegree.keySet()) {
            if (indegree.get(c) == 0) {
                q.offer(c);
            }
        }

        StringBuilder res = new StringBuilder();

        while (!q.isEmpty()) {
            char char_ = q.poll();
            res.append(char_);
            for (char neighbor : adj.get(char_)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    q.offer(neighbor);
                }
            }
        }

        if (res.length() != indegree.size()) {
            return "";
        }

        return res.toString();
    }

```

```python
# python
# 269 alien-dictionary
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, zero_in_degree_queue, in_degree, out_degree = [], collections.deque(), {}, {}
        nodes = sets.Set()
        for word in words:
            for c in word:
                nodes.add(c)
         
        for i in range(1, len(words)):
            if len(words[i-1]) > len(words[i]) and \
                words[i-1][:len(words[i])] == words[i]:
                    return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)
         
        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)
         
        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)
             
            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)
             
                del out_degree[precedence]
         
        if out_degree:
            return ""
 
        return "".join(result)
 
 
    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in range(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = sets.Set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = sets.Set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break　　
```

### 2-4) Sequence Reconstruction

```java
// java
// LC 444
    // V1
    // https://www.youtube.com/watch?v=FHY1q1h9gq0
    // https://www.jiakaobo.com/leetcode/444.%20Sequence%20Reconstruction.html
    Map<Integer, Set<Integer>> map;
    Map<Integer, Integer> indegree;

    public boolean sequenceReconstruction_1(int[] nums, List<List<Integer>> sequences) {
        map = new HashMap<>();
        indegree = new HashMap<>();

        for(List<Integer> seq: sequences) {
            if(seq.size() == 1) {
                addNode(seq.get(0));
            } else {
                for(int i = 0; i < seq.size() - 1; i++) {
                    addNode(seq.get(i));
                    addNode(seq.get(i + 1));

                    // 加入子节点, 子节点增加一个入度
                    // [1,2] => 1 -> 2
                    // 1: [2]
                    int curr = seq.get(i);
                    int next = seq.get(i + 1);
                    if(map.get(curr).add(next)) {
                        indegree.put(next, indegree.get(next) + 1);
                    }
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for(int key : indegree.keySet()) {
            if(indegree.get(key) == 0){
                queue.offer(key);
            }
        }

        int index = 0;
        while(!queue.isEmpty()) {
            // 如果只有唯一解, 那么queue的大小永远都是1
            if(queue.size() != 1) return false;

            int curr = queue.poll();
            if(curr != nums[index++]) return false;

            for(int next: map.get(curr)) {
                indegree.put(next, indegree.get(next) - 1);
                if(indegree.get(next) == 0) {
                    queue.offer(next);
                }
            }
        }

        return index == nums.length;
    }

    private void addNode(int node) {
        if(!map.containsKey(node)) {
            map.put(node, new HashSet<>());
            indegree.put(node, 0);
        }
    }
```

### 2-6) Parallel Courses
```python
# LC 1136
def minimumSemesters(n, relations):
    """
    Find minimum number of semesters to take all courses.
    Time: O(V + E), Space: O(V)
    """
    from collections import defaultdict, deque
    
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)
    
    for pre, next_course in relations:
        graph[pre].append(next_course)
        in_degree[next_course] += 1
    
    queue = deque([i for i in range(1, n + 1) if in_degree[i] == 0])
    semesters = 0
    studied = 0
    
    while queue:
        # Process all courses in current semester
        semesters += 1
        next_queue = []
        
        for _ in range(len(queue)):
            course = queue.popleft()
            studied += 1
            
            for next_course in graph[course]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    next_queue.append(next_course)
        
        queue.extend(next_queue)
    
    return semesters if studied == n else -1
```

### 2-5) Find Eventual Safe States
```python
# LC 802
def eventualSafeNodes(graph):
    """
    Find all safe nodes (nodes from which all paths lead to terminal).
    Time: O(V + E), Space: O(V)
    """
    n = len(graph)
    # Reverse the graph
    reverse_graph = [[] for _ in range(n)]
    out_degree = [0] * n
    
    for i in range(n):
        for j in graph[i]:
            reverse_graph[j].append(i)
        out_degree[i] = len(graph[i])
    
    # Start with terminal nodes (out-degree = 0)
    from collections import deque
    queue = deque([i for i in range(n) if out_degree[i] == 0])
    safe = []
    
    while queue:
        node = queue.popleft()
        safe.append(node)
        
        for prev in reverse_graph[node]:
            out_degree[prev] -= 1
            if out_degree[prev] == 0:
                queue.append(prev)
    
    return sorted(safe)
```

### 2-7) Minimum Height Trees (LC 310)

```java
// java
// LC 310
// IDEA: Tree Centroid Finding - Leaf Trimming (Topological Sort for Undirected Trees)
public class MinimumHeightTrees {

    /**
     * Core Concept: Tree Centroid Finding via Leaf Trimming
     *
     * This is a special application of topological sort to UNDIRECTED TREES:
     *
     * Key Differences from Standard Topological Sort:
     * 1. Works on UNDIRECTED trees (not DAGs)
     * 2. Uses degree (not in-degree) - count all edges
     * 3. Leaves are nodes with degree = 1 (not in-degree = 0)
     * 4. Goal: Find centroid(s), not linear ordering
     * 5. Result: 1 or 2 nodes (tree centers), not all nodes
     *
     * Algorithm (Leaf Trimming):
     * 1. Build adjacency list + track degrees for undirected edges
     * 2. Put all leaves (degree = 1) into queue
     * 3. Remove leaves layer by layer (like peeling an onion)
     * 4. When neighbors' degree becomes 1, they become new leaves
     * 5. Stop when ≤ 2 nodes remain - these are the centroids
     *
     * Why it works:
     * - The centroid(s) of a tree minimize the maximum distance to any leaf
     * - By removing outer layers, we converge to the center
     * - A tree can have at most 2 centroids (if diameter is even: 2, if odd: 1)
     *
     * Time: O(N) - Each node and edge processed once
     * Space: O(N) - Adjacency list and queue storage
     */
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        // Edge case: single node tree
        if (n == 1) {
            return Collections.singletonList(0);
        }

        // Step 1: Build adjacency list and track degrees
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        int[] degree = new int[n];

        // For undirected tree: add edges in both directions
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
            degree[u]++;
            degree[v]++;
        }

        // Step 2: Initialize queue with all leaf nodes (degree = 1)
        Queue<Integer> leaves = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                leaves.offer(i);
            }
        }

        // Step 3: Trim leaves layer by layer
        int remaining = n;

        // Continue until only 1 or 2 nodes remain
        while (remaining > 2) {
            int leafCount = leaves.size();
            remaining -= leafCount;

            // Process all leaves in current layer
            for (int i = 0; i < leafCount; i++) {
                int leaf = leaves.poll();

                // Update degrees of neighbors
                for (int neighbor : graph.get(leaf)) {
                    degree[neighbor]--;

                    // If neighbor becomes a leaf, add to queue for next layer
                    if (degree[neighbor] == 1) {
                        leaves.offer(neighbor);
                    }
                }
            }
        }

        // Step 4: The remaining nodes (1 or 2) are the centroids
        return new ArrayList<>(leaves);
    }
}
```

```python
# python
# LC 310
# V0 - Tree Centroid Finding (Leaf Trimming for Undirected Trees)
from collections import deque

class Solution:
    def findMinHeightTrees(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        Find tree centroids using leaf trimming.

        Key Pattern: Similar to Kahn's Algorithm but for undirected trees
        - Use degree (not in-degree)
        - Leaves are nodes with degree = 1
        - Trim layers until 1-2 nodes remain

        Time: O(N), Space: O(N)
        """
        # Edge case
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

        # Initialize queue with all leaves (degree = 1)
        leaves = deque([i for i in range(n) if degree[i] == 1])

        # Trim leaves layer by layer
        remaining = n
        while remaining > 2:
            leaf_count = len(leaves)
            remaining -= leaf_count

            for _ in range(leaf_count):
                leaf = leaves.popleft()

                # Decrease degree of neighbors
                for neighbor in graph[leaf]:
                    degree[neighbor] -= 1
                    # If neighbor becomes leaf, add to queue
                    if degree[neighbor] == 1:
                        leaves.append(neighbor)

        # Return remaining centroids (1 or 2 nodes)
        return list(leaves)


# V1 - Alternative: Using Set for adjacency (faster removal)
class Solution:
    def findMinHeightTrees(self, n: int, edges: List[List[int]]) -> List[int]:
        if n == 1:
            return [0]

        # Build graph with sets for O(1) removal
        graph = [set() for _ in range(n)]

        for u, v in edges:
            graph[u].add(v)
            graph[v].add(u)

        # Find initial leaves
        leaves = [i for i in range(n) if len(graph[i]) == 1]

        # Trim leaves until 1-2 nodes remain
        while n > 2:
            n -= len(leaves)
            new_leaves = []

            for leaf in leaves:
                # Get the only neighbor
                neighbor = graph[leaf].pop()
                # Remove leaf from neighbor's adjacency set
                graph[neighbor].remove(leaf)

                # If neighbor becomes leaf, add to next layer
                if len(graph[neighbor]) == 1:
                    new_leaves.append(neighbor)

            leaves = new_leaves

        return leaves
```

**Core Logic Explanation:**

1. **Why Leaf Trimming Works:**
   - Tree centroids minimize the height when used as roots
   - By removing outer layers (leaves), we converge to the center
   - Like peeling an onion from outside to inside

2. **Key Differences from Standard Topological Sort:**
   - **Graph Type**: Undirected tree vs Directed Acyclic Graph (DAG)
   - **Degree Tracking**: Total degree vs in-degree
   - **Leaf Definition**: degree = 1 vs in-degree = 0
   - **Goal**: Find center(s) vs Find linear ordering
   - **Result**: 1-2 nodes vs All nodes in order

3. **Why At Most 2 Centroids:**
   - If tree diameter is even → 2 center nodes
   - If tree diameter is odd → 1 center node
   - These nodes minimize the maximum distance to any leaf

**Similar LC Problems:**
- LC 310: Minimum Height Trees (this problem)
- Tree diameter problems
- Finding tree radius
- Graph center finding
- Balanced tree problems

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