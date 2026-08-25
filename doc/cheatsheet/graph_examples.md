# Graph Worked Examples

> **Scope** — The worked-solution archive for [graph.md](./graph.md): one canonical solution per problem for the grid, clone, connectivity, ratio-graph, implicit-DAG and DSU-by-attribute problems, with no template or theory material of its own.
> **See also**: [graph.md](./graph.md) — representation, traversal, connectivity and cycle detection, and the template each solution below instantiates; [graph_advanced.md](./graph_advanced.md) — Tarjan, Euler, max flow and the bipartite extras; [dfs_examples.md](./dfs_examples.md) and [bfs_examples.md](./bfs_examples.md) — several of the same problems solved from the traversal sheets' angle; [union_find.md](./union_find.md) — the DSU sheet's own treatment of LC 323 / 947 / 1319.

## LeetCode Problem Lists

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Union Find](https://leetcode.com/problem-list/union-find/)

## Overview

Each entry names the [graph.md](./graph.md) template it instantiates, so this file stays an
archive rather than a second teaching document.

| # | Problem | LC | Template it instantiates |
|---|---|---|---|
| 2-1 | Number of Islands | 200 | Template 2 — DFS flood fill on a grid |
| 2-2 | Max Area of Island | 695 | Template 2 — DFS returning a count |
| 2-3 | Closest Leaf in a Binary Tree | 742 | tree → undirected graph, then BFS (Template 1) |
| 2-4 | Number of Connected Components | 323 | Template 5 — components by repeated traversal |
| 2-5 | Clone Graph | 133 | Template 1 / 2 with a `{original: copy}` map |
| 2-6 | Bus Routes | 815 | Template 1 — BFS on an abstract (route-level) graph |
| 2-7 | Course Schedule | 207 | Template 5 — directed cycle detection |
| 2-8 | Find Eventual Safe States | 802 | Template 5 — cycle detection with memoised states |
| 2-9 | Evaluate Division | 399 | implicit weighted graph + DFS product |
| 2-10 | Longest Increasing Path in a Matrix | 329 | implicit DAG + memoised DFS |
| 2-11 | Most Stones Removed | 947 | Template 3 — DSU on a shared attribute |
| 2-12 | Possible Bipartition | 886 | Template 6 — 2-colouring a conflict graph |

## LC Examples

### 2-1) Number of Islands — LC 200

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
    dfs(grid, r - 1, c);
    dfs(grid, r + 1, c);
    dfs(grid, r, c - 1);
    dfs(grid, r, c + 1);
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
                dfs(grid, r, c);
            }
        }
    }

    return num_islands;
}

```

### 2-2) Max Area of Island — LC 695

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

### 2-3) Closest Leaf in a Binary Tree — LC 742
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

### 2-4) Number of Connected Components in an Undirected Graph — LC 323
```python
# LC 323 Number of Connected Components in an Undirected Graph
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

### 2-5) Clone Graph — LC 133 ⭐⭐⭐⭐
```python
# LC 133. Clone Graph

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

### 2-6) Bus Routes — LC 815
```python
# LC 815. Bus Routes
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

### 2-7) Course Schedule — LC 207
```java
// java
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

### 2-8) Find Eventual Safe States — LC 802
```java
// java
// LC 802

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

### 2-9) Evaluate Division — LC 399

**Key Idea**: When the input is a list of *relations* (`a / b = 2.0`), the graph is **implicit** — the nodes are strings you discover from the input. Store the weight in **both** directions (`w` and `1/w`) and multiply weights along the DFS path; a query is just "is there a path, and what is its product?".

```java
// java
// LC 399 - Evaluate Division
// IDEA: build a bidirectional weighted graph (a->b = v, b->a = 1/v),
//       then DFS accumulating the product. -1.0 = unreachable / unknown var.
// time = O(Q * (V + E)), space = O(V + E)
import java.util.*;

public class Solution {
    public double[] calcEquation(List<List<String>> equations, double[] values,
                                 List<List<String>> queries) {
        // 1) build adjacency: node -> (neighbor -> weight)
        Map<String, Map<String, Double>> g = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            g.computeIfAbsent(a, x -> new HashMap<>()).put(b, values[i]);
            g.computeIfAbsent(b, x -> new HashMap<>()).put(a, 1.0 / values[i]);
        }

        // 2) answer each query with an independent DFS
        double[] res = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String a = queries.get(i).get(0);
            String b = queries.get(i).get(1);
            // unknown variable -> -1.0 (note: "x/x" is NOT 1.0 if x is unseen)
            if (!g.containsKey(a) || !g.containsKey(b)) {
                res[i] = -1.0;
            } else {
                res[i] = dfs(g, a, b, 1.0, new HashSet<>());
            }
        }
        return res;
    }

    private double dfs(Map<String, Map<String, Double>> g, String cur, String target,
                       double acc, Set<String> visited) {
        if (cur.equals(target)) {
            return acc;                      // covers "a/a" = 1.0 when a exists
        }
        visited.add(cur);
        for (Map.Entry<String, Double> e : g.get(cur).entrySet()) {
            if (visited.contains(e.getKey())) {
                continue;
            }
            double r = dfs(g, e.getKey(), target, acc * e.getValue(), visited);
            if (r != -1.0) {
                return r;
            }
        }
        return -1.0;
    }
}
```

```python
# python
# LC 399 - Evaluate Division
# IDEA: bidirectional weighted graph + DFS multiplying edge weights
# time = O(Q * (V + E)), space = O(V + E)
from collections import defaultdict

class Solution(object):
    def calcEquation(self, equations, values, queries):
        g = defaultdict(dict)
        for (a, b), v in zip(equations, values):
            g[a][b] = v
            g[b][a] = 1.0 / v

        def dfs(cur, target, acc, visited):
            if cur == target:
                return acc                 # handles "a/a" = 1.0
            visited.add(cur)
            for nxt, w in g[cur].items():
                if nxt in visited:
                    continue
                r = dfs(nxt, target, acc * w, visited)
                if r != -1.0:
                    return r
            return -1.0

        res = []
        for a, b in queries:
            # unknown variable => -1.0, even for "x/x"
            if a not in g or b not in g:
                res.append(-1.0)
            else:
                res.append(dfs(a, b, 1.0, set()))
        return res

# equations = [["a","b"],["b","c"]], values = [2.0, 3.0]
# queries   = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
# -> [6.0, 0.5, -1.0, 1.0, -1.0]
```

**Gotchas**:
- `a / a` is `1.0` only if `a` appeared in the equations; an unseen variable is always `-1.0`.
- The multiplicative weight makes this a **weighted union-find** problem too (store `weight[x] = value of x / value of parent[x]`), which is the O(1)-per-query variant.

**Interview signal**: "given ratios / conversions / exchange rates, answer queries" → weighted graph DFS (or weighted DSU).

---

### 2-10) Longest Increasing Path in a Matrix — LC 329

**Key Idea**: A grid where you may only move to a **strictly larger** value is a **DAG** (no cycles are possible, because values strictly increase). On a DAG you can memoize: `dp[cell] = longest increasing path starting here`. Without the "strictly increasing" guarantee this would need cycle handling — this is exactly the DFS-vs-DP boundary interviewers probe.

**Why no `visited` set is needed**: the strict inequality already prevents revisiting a cell on the current path, so the memo array doubles as both cache and visited marker.

```java
// java
// LC 329 - Longest Increasing Path in a Matrix
// IDEA: the "move only to a bigger value" rule makes the grid a DAG,
//       so plain DFS + memo (top-down DP) works; each cell is computed once.
// time = O(m * n), space = O(m * n)
public class Solution {
    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int m = matrix.length, n = matrix[0].length, best = 0;
        int[][] memo = new int[m][n];   // 0 = not computed yet

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                best = Math.max(best, dfs(matrix, i, j, memo));
            }
        }
        return best;
    }

    private int dfs(int[][] mat, int i, int j, int[][] memo) {
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        int best = 1;                    // the cell itself
        for (int[] d : DIRS) {
            int x = i + d[0], y = j + d[1];
            if (x >= 0 && x < mat.length && y >= 0 && y < mat[0].length
                    && mat[x][y] > mat[i][j]) {          // strictly increasing => DAG edge
                best = Math.max(best, 1 + dfs(mat, x, y, memo));
            }
        }
        memo[i][j] = best;
        return best;
    }
}
```

```python
# python
# LC 329 - Longest Increasing Path in a Matrix
# IDEA: implicit DAG (edges only go to strictly larger values) + memoized DFS
# time = O(m * n), space = O(m * n)
class Solution(object):
    def longestIncreasingPath(self, matrix):
        if not matrix or not matrix[0]:
            return 0
        m, n = len(matrix), len(matrix[0])
        memo = [[0] * n for _ in range(m)]

        def dfs(i, j):
            if memo[i][j]:
                return memo[i][j]
            best = 1
            for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                x, y = i + di, j + dj
                if 0 <= x < m and 0 <= y < n and matrix[x][y] > matrix[i][j]:
                    best = max(best, 1 + dfs(x, y))
            memo[i][j] = best
            return best

        return max(dfs(i, j) for i in range(m) for j in range(n))

# [[9,9,4],[6,6,8],[2,1,1]] -> 4   (1 -> 2 -> 6 -> 9)
```

**Alternative (topological / peeling)**: treat cells with out-degree 0 as sinks and run Kahn's algorithm on the reverse DAG; the number of BFS levels is the answer. Same O(m·n), no recursion depth risk.

**Interview signal**: "longest path" is NP-hard in general graphs but **linear on a DAG** — always say out loud why the graph is acyclic before claiming O(V+E).

---

### 2-11) Most Stones Removed with Same Row or Column — LC 947

**Key Idea**: Sometimes edges are not given — two items are connected because they **share an attribute** (same row, same column, same email, same equation variable). Naively comparing all pairs is O(n²). Instead, **make the attribute itself a DSU node** and union `item ↔ attribute`. Items sharing an attribute land in the same component transitively, in near-linear time.

**Namespace trick**: rows and columns are both integers, so they must not collide. Use `~c` (or `c + OFFSET`, or a tuple/string key) for columns.

**LC 947 insight**: within one connected component of `k` stones you can always remove `k - 1` of them (peel them off in reverse-DFS order, leaving one behind), so the answer is `n - (number of components)`.

```java
// java
// LC 947 - Most Stones Removed with Same Row or Column
// IDEA: union stone's row with stone's column (~col avoids id collision).
//       answer = n - #components. No O(n^2) pairwise comparison needed.
// time = O(n log n) (path halving only; O(n * alpha(n)) needs union by size/rank too), space = O(n)
import java.util.*;

public class Solution {
    private Map<Integer, Integer> parent = new HashMap<>();

    private int find(int x) {
        parent.putIfAbsent(x, x);
        while (parent.get(x) != x) {
            parent.put(x, parent.get(parent.get(x)));   // path halving
            x = parent.get(x);
        }
        return x;
    }

    private void union(int a, int b) {
        int ra = find(a), rb = find(b);
        if (ra != rb) {
            parent.put(ra, rb);
        }
    }

    public int removeStones(int[][] stones) {
        parent = new HashMap<>();
        // key trick: row id = r, column id = ~c  (negative, cannot clash with rows)
        for (int[] s : stones) {
            union(s[0], ~s[1]);
        }
        Set<Integer> roots = new HashSet<>();
        for (int[] s : stones) {
            roots.add(find(s[0]));
        }
        return stones.length - roots.size();
    }
}
```

```python
# python
# LC 947 - Most Stones Removed with Same Row or Column
# IDEA: DSU over (row, col) attribute nodes; answer = n - #components
# time = O(n log n) (path halving only; O(n * alpha(n)) needs union by size/rank too), space = O(n)
class Solution(object):
    def removeStones(self, stones):
        parent = {}

        def find(x):
            parent.setdefault(x, x)
            while parent[x] != x:
                parent[x] = parent[parent[x]]     # path halving
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb

        # tagged keys keep the two namespaces apart
        for r, c in stones:
            union(("row", r), ("col", c))

        roots = {find(("row", r)) for r, c in stones}
        return len(stones) - len(roots)

# [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]] -> 5  (1 component of 6 stones)
# [[0,0],[0,2],[1,1],[2,0],[2,2]]       -> 3  (2 components: 4 + 1 stones)
```

#### Variation: count components + spare edges — LC 1319

*Twist*: instead of "how many can I remove", the question is "how many **redundant** edges do I have, and are there enough to link the components".

```python
# python
# LC 1319 - Number of Operations to Make Network Connected
# IDEA: a redundant cable is an edge whose endpoints are already connected.
#       need >= n-1 cables total; then answer = (#components - 1).
# time = O(E log n) (path halving only; O(E * alpha(n)) needs union by size/rank too), space = O(n)
class Solution(object):
    def makeConnected(self, n, connections):
        if len(connections) < n - 1:
            return -1                      # impossible: a tree needs n-1 edges

        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        comps = n
        for a, b in connections:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                comps -= 1                 # a useful edge merges 2 components
        return comps - 1                   # k components need k-1 cables to join
```

```java
// java
// LC 1319 - Number of Operations to Make Network Connected
// time = O(E log n) (path halving only; O(E * alpha(n)) needs union by size/rank too), space = O(n)
public int makeConnected(int n, int[][] connections) {
    if (connections.length < n - 1) {
        return -1;
    }
    int[] p = new int[n];
    for (int i = 0; i < n; i++) {
        p[i] = i;
    }
    int comps = n;
    for (int[] c : connections) {
        int ra = find(p, c[0]), rb = find(p, c[1]);
        if (ra != rb) {
            p[ra] = rb;
            comps--;
        }
    }
    return comps - 1;
}

private int find(int[] p, int x) {
    while (p[x] != x) {
        p[x] = p[p[x]];
        x = p[x];
    }
    return x;
}
```

**Interview signal**: "connected because they share X" (row/column, email, account, variable) → make X a DSU node instead of building O(n²) edges. Same trick powers LC 721 Accounts Merge and LC 990 Satisfiability of Equality Equations.

---

### 2-12) Possible Bipartition — LC 886

*Twist*: the graph is not handed to you as edges over an existing node set — build the conflict graph from the `dislikes` pairs first, then run the same 2-colouring DFS as LC 785 (Template 6 in [graph.md](./graph.md)).

```python
def possibleBipartition(self, n, dislikes):
    """LC 886 - Build graph from dislike relationships"""
    from collections import defaultdict

    # Build adjacency list from dislikes
    graph = defaultdict(list)
    for u, v in dislikes:
        graph[u].append(v)
        graph[v].append(u)

    colors = {}

    def dfs(node, color):
        colors[node] = color
        for neighbor in graph[node]:
            if neighbor in colors:
                if colors[neighbor] == colors[node]:
                    return False
            else:
                if not dfs(neighbor, 1 - color):
                    return False
        return True

    for i in range(1, n + 1):
        if i not in colors:
            if not dfs(i, 0):
                return False
    return True
```

## Summary

- **Grid problems (LC 200, 695)** are the same DFS with a different return type: `void` when
  you only count components, `int` when you aggregate over the component.
- **Tree → graph (LC 742)** — add a parent edge for every child edge, and the leaf search
  becomes an ordinary BFS outward from the target node.
- **Clone / copy (LC 133)** — the `{original: copy}` map *is* the visited set. Create the copy
  when you first see a node; wire its neighbours when you pop it.
- **Abstract graphs (LC 815)** — pick the right node type. Nodes are *routes*, not stops,
  which turns a hard problem into a two-level BFS.
- **Cycle detection (LC 207, 802)** — a boolean `visited` is not enough; you must distinguish
  "on the current path" from "finished, and known safe".
- **Implicit graphs (LC 399, 329, 947)** — the edges are never handed to you. Discover nodes
  from the input (strings, cells, row/column ids) and build as you read.
- **DSU by attribute (LC 947, 1319)** — making the shared attribute a node avoids the O(n²)
  pairwise edge build.
