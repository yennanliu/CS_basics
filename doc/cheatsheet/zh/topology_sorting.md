# 拓撲排序 - 完整指南

> **範圍** — 為 DAG 排序 — Kahn 的 BFS 解法、DFS 後序、環的偵測，以及建構在它們之上的排程類題目。
> **另見** — [topology_sorting_examples.md](./topology_sorting_examples.md) — 這些模板背後的八道完整解題；[diff_toposort_quickunion.md](./diff_toposort_quickunion.md) — 拓撲排序 vs 併查集 — 什麼題該用哪個工具；[union_find.md](./union_find.md) — 無向圖的連通性；[graph.md](./graph.md) — 一般的圖論素材。

## LeetCode 題目清單

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## 總覽

拓撲排序是把有向無環圖（DAG）中的頂點排成一個線性順序，使得對每一條有向邊 (u, v)，頂點 u 都排在 v 之前。

### 關鍵特性
- **只適用 DAG**：僅在有向無環圖上成立
- **答案不唯一**：可能存在很多組合法的拓撲順序
- **解決相依關係**：處理有先修／相依關係的問題
- **應用場景**：任務排程、建置系統、課程規劃、相依性解析

### 複雜度分析
| 做法 | 時間複雜度 | 空間複雜度 | 適用情境 |
|----------|----------------|------------------|----------|
| DFS（Kahn 演算法） | O(V + E) | O(V) | 通用、偵測環 |
| BFS（入度） | O(V + E) | O(V) | 找出所有順序、逐層處理 |
| 找樹的重心 | O(V + E) | O(V) | 無向樹，找中心／最小化高度 |
| 列舉所有拓撲排序 | O(V! × (V + E)) | O(V) | 小圖、所有排列 |

### 參考資料
- [techbridge : topological-sort](https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/)
- [DFS-based topological sort](https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html)
- [topological_sort.py](https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/topological_sort.py)
- [TopologicalSort.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSort.java)
- [NumberOfProvinces.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfProvinces.java)（連通分量／併查集）
- [MinimumHeightTrees.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BFS/MinimumHeightTrees.java)（找樹的重心）

## 題型分類

### 1. 課程排程
牽涉先修關係與課程順序的題目。
- **模式**：建出相依圖、檢查是否有環、找出合法順序
- **代表題目**：LC 207、210、630、1462

### 2. 任務排程
牽涉任務相依與平行執行的題目。
- **模式**：求最短時間、平行處理的層數
- **代表題目**：LC 1136、2050、1857

### 3. 字典序排序
要求字典序最小／最大之拓撲順序的題目。
- **模式**：用優先佇列決定順序、外星文字典
- **代表題目**：LC 269、953、1203

### 4. 建置順序與相依性
牽涉建置系統與套件相依的題目。
- **模式**：偵測環、找出建置順序、處理群組
- **代表題目**：LC 444、802、851

### 5. 圖的分層
牽涉在 DAG 上逐層處理的題目。
- **模式**：帶層數的 BFS、DAG 上的最長路徑
- **代表題目**：LC 2192、2115、1857

### 6. 環的偵測與安全狀態
聚焦在偵測環與找出安全節點的題目。
- **模式**：三色 DFS、辨識安全狀態
- **代表題目**：LC 802、207、1059

### 7. 連通分量（併查集／DFS）
牽涉在無向圖中找連通分量的題目。
- **模式**：帶路徑壓縮的併查集、用 DFS/BFS 遍歷計數分量
- **代表題目**：LC 547、200、323、684

### 8. 找樹的重心
牽涉尋找無向樹之中心／重心的題目。
- **模式**：逐層剝除葉節點，類似無向樹版的拓撲排序
- **代表題目**：LC 310、樹的直徑、樹的中心

## 核心模板

### 模板 1：BFS（Kahn 演算法）⭐⭐⭐⭐⭐
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

### 模板 2：DFS（三色標記）⭐⭐⭐⭐

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

### 模板 3：DFS（用堆疊）
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

### 模板 4：字典序
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

### 模板 5：所有拓撲順序
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

### 模板 6：平行任務排程
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

### 模板 7：找樹的重心（無向樹的葉節點剝除法）⭐⭐⭐
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

### 模板 8：併查集（連通分量）
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

### 模板 9：在**隱式** DAG 上的拓撲排序 + DP ⭐⭐⭐⭐⭐

**核心想法**：圖從來不是以邊列表給你的——它是由一條比較規則*隱含*出來的
（`grid[a] < grid[b]` ⇒ 存在邊 `a → b`）。邊走邊算入度／出度，再用 Kahn 的做法逐層剝除。
**BFS 的層數**就是 DAG 中的最長路徑（因為邊的規則是嚴格遞增的，所以不可能有環）。

**何時使用**：格子或矩陣上的「最長遞增路徑／鏈」，或任何需要依相依順序處理節點、又不想承擔遞迴深度風險的 DAG 上 DP。

**與模板 1 的差異**：這裡從**匯點**（出度 0 = 區域極大值）而非源點開始剝，這樣每一層剛好就是 DP 的一步。從源點剝也可以——把比較方向反過來即可。

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

**注意**：經典的替代做法是 DFS + 記憶化（`dp[i][j] = 1 + max(dp[neighbour])`），同樣是
O(m·n)。當格子大到遞迴可能爆堆疊，或面試官明確要求用拓撲排序時，優先選 Kahn 版本。

**同樣的形狀，但圖是顯式的**：LC 1857 (Largest Color Value in a Directed Graph) — 在 Kahn 的流程中帶著一個
`count[node][26]` 的 DP 陣列，而不是單一的層數計數器；若佇列在所有節點都被取出之前就空了，代表有環 → 回傳 `-1`。

---

### 模板 10：入度特徵（答案直接從度數讀出來）

**核心想法**：有些「圖」題根本不需要遍歷——答案完全由**入度／出度的計數**決定。認出這一點能把一題 Medium 變成三行程式。

**關鍵洞見（LC 1557）**：在 **DAG** 上，入度為 0 的節點只能從它自己開始才會被走到，而其餘每個節點都能從某個入度 0 的節點到達。所以入度 0 的節點集合既必要又充分——因此就是唯一的最小答案。

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

#### 變化 A — 度數*特徵*比對：LC 997 Find the Town Judge

**變化點**：不是找入度 0，而是找一個精確的指紋——`inDegree = n - 1` **且**
`outDegree = 0`。把兩者摺進單一的 `score = inDegree - outDegree` 陣列，再掃描找 `n - 1`。

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

#### 變化 B — 度數 + 一次可達性檢查：LC 1361 Validate Binary Tree Nodes

**變化點**：光靠度數還不夠。一棵合法的二元樹需要**三個**條件——
(1) 每個節點入度 ≤ 1、(2) 恰好一個節點入度為 0（根）、
(3) 全部 `n` 個節點都能從該根到達。只有條件 1+2 而沒有 3 的話，仍然可能出現一棵看似合法的樹外加一個**分離的環**，那正是這題的陷阱情境。

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

## 題目分類

| 題目 | 難度 | 分類 | 關鍵技巧 |
|---------|------------|----------|---------------|
| [207. Course Schedule](https://leetcode.com/problems/course-schedule/) | Medium | 課程排程 | 偵測環 |
| [210. Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | Medium | 課程排程 | BFS/DFS |
| [269. Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) | Hard | 字典序 | 字元順序 |
| [310. Minimum Height Trees](https://leetcode.com/problems/minimum-height-trees/) | Medium | 樹的重心 | 葉節點剝除 |
| [444. Sequence Reconstruction](https://leetcode.com/problems/sequence-reconstruction/) | Medium | 建置順序 | 唯一順序 |
| [630. Course Schedule III](https://leetcode.com/problems/course-schedule-iii/) | Hard | 課程排程 | 貪婪 + 堆積(heap) |
| [802. Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/) | Medium | 偵測環 | 反向圖 |
| [851. Loud and Rich](https://leetcode.com/problems/loud-and-rich/) | Medium | 圖的分層 | DFS + 記憶化 |
| [953. Verifying an Alien Dictionary](https://leetcode.com/problems/verifying-an-alien-dictionary/) | Easy | 字典序 | 驗證順序 |
| [1059. All Paths from Source Lead to Destination](https://leetcode.com/problems/all-paths-from-source-lead-to-destination/) | Medium | 偵測環 | DFS |
| [1136. Parallel Courses](https://leetcode.com/problems/parallel-courses/) | Medium | 任務排程 | 分層 BFS |
| [1203. Sort Items by Groups Respecting Dependencies](https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/) | Hard | 建置順序 | 雙層拓撲排序 |
| [1462. Course Schedule IV](https://leetcode.com/problems/course-schedule-iv/) | Medium | 課程排程 | 遞移閉包 |
| [1857. Largest Color Value in a Directed Graph](https://leetcode.com/problems/largest-color-value-in-a-directed-graph/) | Hard | 圖的分層 | DAG 上的 DP |
| [2050. Parallel Courses III](https://leetcode.com/problems/parallel-courses-iii/) | Hard | 任務排程 | 時間計算 |
| [2115. Find All Possible Recipes from Given Supplies](https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/) | Medium | 建置順序 | 改造版 BFS |
| [547. Number of Provinces](https://leetcode.com/problems/number-of-provinces/) | Medium | 連通分量 | 併查集／DFS |
| [2192. All Ancestors of a Node in a Directed Acyclic Graph](https://leetcode.com/problems/all-ancestors-of-a-node-in-a-directed-acyclic-graph/) | Medium | 圖的分層 | DFS/BFS |
| [329. Longest Increasing Path in a Matrix](https://leetcode.com/problems/longest-increasing-path-in-a-matrix/) | Hard | 圖的分層 | 隱式 DAG + Kahn（模板 9） |
| [1557. Minimum Number of Vertices to Reach All Nodes](https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/) | Medium | 圖的分層 | 入度 0 的集合（模板 10） |
| [997. Find the Town Judge](https://leetcode.com/problems/find-the-town-judge/) | Easy | 度數計算 | 入／出度特徵（模板 10-A） |
| [1361. Validate Binary Tree Nodes](https://leetcode.com/problems/validate-binary-tree-nodes/) | Medium | 偵測環 | 入度 + 可達性（模板 10-B） |

### 依分類整理的題型

#### 課程排程類
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 基本的環偵測 | 207 | 檢查是否為 DAG |
| 找出合法順序 | 210 | 回傳拓撲順序 |
| 帶時間限制 | 630 | 貪婪 + 優先佇列 |
| 查詢先修關係 | 1462 | Floyd-Warshall／DFS |

#### 任務排程類
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 最短時間 | 1136、2050 | 逐層 BFS |
| 平行執行 | 1136 | 計算層數 |
| 帶執行時長 | 2050 | 在完成時間上做 DP |

#### 字典序排序
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 字元順序 | 269 | 由相鄰比較建圖 |
| 驗證順序 | 953 | 檢查一致性 |
| 自訂比較器 | 269 | 從範例中萃取規則 |

#### 建置順序與相依性
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 唯一重建 | 444 | 佇列大小恆為 1 |
| 食譜相依 | 2115 | 處理初始原料 |
| 群組相依 | 1203 | 兩層拓撲排序 |

#### 圖的分層
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 找出所有祖先 | 2192 | 遍歷反向圖 |
| 可達的最富有者 | 851 | DFS + 記憶化 |
| 路徑值最大化 | 1857 | DAG 上的 DP |
| 隱式 DAG 上的最長路徑 | 329 | 邊由 `a < b` 隱含；層數 = 路徑長度 |

#### 度數計算（不需遍歷）
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 最小起始集合 | 1557 | 在 DAG 上答案就是入度 0 的節點 |
| 節點指紋 | 997 | 法官 = 入度 `n-1` 且出度 `0` |
| 驗證樹的形狀 | 1361 | 入度 ≤ 1 + 唯一的根 + 根能到達全部 n 個節點 |

#### 環的偵測
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 安全狀態 | 802 | 反向圖 + 出度 |
| 所有路徑皆安全 | 1059 | DFS 並追蹤路徑 |
| 偵測任意環 | 207 | 三色 DFS |

#### 連通分量（併查集／DFS）
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 計算分量數 | 547、323 | 併查集或 DFS 遍歷 |
| 找出多餘的邊 | 684 | 併查集偵測環 |
| 島嶼數量 | 200 | 在格子上做 DFS/BFS |

#### 找樹的重心
| 模式 | 題目 | 關鍵洞見 |
|---------|----------|-------------|
| 找出樹的中心 | 310 | 逐層剝除葉節點（由外向內的多源 BFS） |
| 最小高度樹 | 310 | 從葉節點開始 BFS，剩 1-2 個節點時停止 |
| 與樹直徑相關 | 310、1245 | 重心位在直徑的中點 |
| 剪葉／收集硬幣 | 2603 | 剝掉葉節點以移除不必要的節點 |
| 樹中距離總和 | 834 | 換根 DP，與重心概念相關 |

## 決策框架

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

> 上面的 `UnionFind` 類別就是模板；LC 547 用三種方式套用它——
> 併查集、DFS 與 BFS——見
> [topology_sorting_examples.md](./topology_sorting_examples.md#8-number-of-provinces--lc-547)。


## 完整解題範例

八道題目，依它們所編碼的相依關係形狀分組，收錄在
**[topology_sorting_examples.md](./topology_sorting_examples.md)**：

| 分組 | 題目 | 練到的模板 |
|---|---|---|
| [課程排程與排序](./topology_sorting_examples.md#course-scheduling--ordering) | LC 210、207、269、444 | Kahn（T1）、三色 DFS（T2）、字典序（T4） |
| [分層與平行排程](./topology_sorting_examples.md#layering--parallel-scheduling) | LC 1136 | 平行任務排程（T6） |
| [環的偵測與安全狀態](./topology_sorting_examples.md#cycle-detection--safe-states) | LC 802 | 三色 DFS（T2） |
| [無向圖 — 連通分量與重心](./topology_sorting_examples.md#undirected-graphs--components--centroids) | LC 310、547 | 葉節點剝除（T7）、併查集（T8） |


## 總結與面試提示

### 常見陷阱
1. **忘了偵測環**：永遠要檢查結果的大小是否等於節點數
2. **邊的方向搞反**：記住邊是從先修指向相依者
3. **沒處理不連通的部分**：所有尚未拜訪的節點都要處理
4. **入度初始化錯誤**：確保所有節點都被納入
5. **漏掉邊界情況**：空圖、單一節點、自環
6. **混淆度數與入度**：無向樹用總度數，DAG 用入度
7. **停止條件錯誤**：找樹的重心時要在剩下 ≤2 個節點時停（不是等佇列空）

### 關鍵洞見
1. **BFS vs DFS**：要找出一個順序時 BFS 比較簡單，要列出所有順序則用 DFS
2. **追蹤入度**：入度為 0 的節點才可以被處理
3. **三色 DFS**：白（未拜訪）、灰（拜訪中）、黑（已完成）
4. **反向圖**：對安全狀態這類題目很有用
5. **逐層處理**：用於平行執行與求最短時間
6. **找樹的重心**：無向樹要用度數（不是入度），逐層剝除葉節點直到剩下 1-2 個節點
7. **無向 vs 有向**：無向樹需要雙向邊並追蹤度數，DAG 則使用入度

### 面試作法
1. **釐清需求**：
   - 圖是否保證為 DAG？
   - 需要所有順序還是只要一個？
   - 對順序有沒有特別偏好？

2. **選擇演算法**：
   - 預設用 BFS（Kahn）因為最單純
   - 遞迴類問題用 DFS
   - 需要字典序時用優先佇列

3. **處理邊界情況**：
   - 空圖
   - 單一節點
   - 不連通的部分
   - 圖中有環

4. **必要時再最佳化**：
   - 偵測到環就提早終止
   - 用原地修改節省空間
   - 用更好的資料結構縮短時間

### 時間／空間複雜度總表
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| 建圖 | O(E) | O(V + E) | 鄰接表 |
| 計算入度 | O(E) | O(V) | 陣列或雜湊表 |
| BFS/DFS 遍歷 | O(V + E) | O(V) | 每個節點／邊只走一次 |
| 偵測環 | O(V + E) | O(V) | 三色標記 |
| 列舉所有順序 | O(V! × E) | O(V) | 所有排列，指數級 |

### 相關概念
- **強連通分量**：Tarjan／Kosaraju 演算法
- **DAG 上的最短路徑**：拓撲排序 + 鬆弛
- **要徑法（Critical Path Method）**：專案排程
- **相依性解析**：套件管理器、建置系統
- **資料流分析**：編譯器最佳化
