# Graph Worked Examples

> **Scope** — The problem set for [graph.md](./graph.md): one problem card per problem, grouped under the graph.md template each one reduces to, with no template or theory material of its own.
> **See also**: [graph.md](./graph.md) — representation, traversal, connectivity and cycle detection, and the template each card below reduces to; [graph_advanced.md](./graph_advanced.md) — Tarjan, Euler, max flow and the bipartite extras; [dfs_examples.md](./dfs_examples.md) and [bfs_examples.md](./bfs_examples.md) — the grid and tree problems this sheet points to; [union_find_examples.md](./union_find_examples.md) — the DSU sheet's own problem set.

## LeetCode Problem Lists

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Depth-First Search](https://leetcode.com/problem-list/depth-first-search/)
- [Breadth-First Search](https://leetcode.com/problem-list/breadth-first-search/)
- [Union Find](https://leetcode.com/problem-list/union-find/)

## Overview

The classic graph problems that are not grids, trees or dependency orders, each as a problem
card under the [graph.md](./graph.md) template it reduces to. [graph.md](./graph.md) holds the
templates; this sheet is the problems. A problem that is worked in full somewhere else has a
pointer row in the Index instead of a second copy.

## Index

| LC | Problem | Reduces to | Difficulty |
|---|---|---|---|
| 323 | [Number of Connected Components in an Undirected Graph](#number-of-connected-components-in-an-undirected-graph--lc-323) | [graph § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) — components by traversal | Medium |
| 133 | [Clone Graph](#clone-graph--lc-133-) | [graph § Template 2](./graph.md#template-2-dfs-reachability--lc-841-) — DFS reachability | Medium |
| 947 | [Most Stones Removed with Same Row or Column](#most-stones-removed-with-same-row-or-column--lc-947) | [graph § Template 3](./graph.md#template-3-union-find-with-path-compression--lc-684-) — union-find | Medium |
| 886 | [Possible Bipartition](#possible-bipartition--lc-886) | [graph § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) — 2-colouring | Medium |

**Worked in another sheet** — one home per problem, so these are pointers:

| LC | Problem | Worked in |
|---|---|---|
| 200 | Number of Islands | [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) |
| 695 | Max Area of Island | [dfs_examples.md](./dfs_examples.md) |
| 742 | Closest Leaf in a Binary Tree | [bfs_examples.md](./bfs_examples.md) |
| 1319 | Number of Operations to Make Network Connected | [union_find_examples.md](./union_find_examples.md) |
| 207 | Course Schedule | [graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-) |
| 802 | Find Eventual Safe States | [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-) |
| 399 | Evaluate Division | [dfs.md § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) |
| 329 | Longest Increasing Path in a Matrix | [topology_sorting.md § Template 9](./topology_sorting.md#template-9-topological-sort--dp-on-an-implicit-dag-) |
| 815 | Bus Routes | [bfs_advanced.md](./bfs_advanced.md) |

## Connected Components Examples

### Number of Connected Components in an Undirected Graph — LC 323

> **Key point** — every traversal launched from a node not yet visited covers exactly one
> component, so the answer is the number of launches. The graph arrives as an edge list, so
> build the adjacency list in **both** directions first.

**Reduces to**: [graph § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) — the same launch-and-count loop, over an edge list instead of an adjacency matrix.

```python
# python
# LC 323 - Number of Connected Components in an Undirected Graph
# IDEA: build an undirected adjacency list; each DFS launched from an unvisited node is one component
# time = O(V + E), space = O(V + E)
from collections import defaultdict

def countComponents(n, edges):
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)          # undirected: both directions
    visited = set()
    count = 0
    for start in range(n):
        if start in visited:
            continue
        count += 1                  # an unvisited node opens a new component
        visited.add(start)
        stack = [start]
        while stack:
            node = stack.pop()
            for nxt in graph[node]:
                if nxt not in visited:
                    visited.add(nxt)
                    stack.append(nxt)
    return count

# countComponents(5, [[0,1],[1,2],[3,4]]) -> 2
```

**Traps**
- An isolated node has no edge, so it never appears in `graph`: loop over `range(n)`, not over
  the adjacency list's keys, or `n = 3, edges = []` returns 0 instead of 3.
- The union-find version (decrement a counter on every successful union) is equally good and is
  worked in [union_find_examples.md](./union_find_examples.md).

## DFS Reachability Examples

### Clone Graph — LC 133 ⭐⭐⭐⭐

> **Key point** — the `{original: copy}` map **is** the visited set. Register a node's copy
> *before* recursing into its neighbours; then a cycle that comes back to it finds the copy
> already there instead of recursing forever.

**Reduces to**: [graph § Template 2](./graph.md#template-2-dfs-reachability--lc-841-) — DFS reachability from one node, where "visit" means "create the copy".

```python
# python
# LC 133 - Clone Graph
# IDEA: DFS; copies = {original: copy} doubles as visited; wire each copy's neighbours from the recursion
# time = O(V + E), space = O(V)
class Node:
    def __init__(self, val=0, neighbors=None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []

def cloneGraph(node):
    copies = {}                         # original -> its copy
    def dfs(cur):
        if cur in copies:
            return copies[cur]
        copy = Node(cur.val)
        copies[cur] = copy              # register BEFORE recursing, or a cycle recurses forever
        for nxt in cur.neighbors:
            copy.neighbors.append(dfs(nxt))
        return copy
    return dfs(node) if node else None
```

**Traps**
- The empty graph: `node` is `None`, and the answer is `None`, not a new node.
- Key the map by the **node object**, not `node.val`: values are unique in LC 133, but a
  follow-up that drops that guarantee breaks a value-keyed map silently.

## Union-Find Examples

### Most Stones Removed with Same Row or Column — LC 947

> **Key point** — a component of `k` stones can always be reduced to one stone, so the answer
> is `n − components`. The edges are not given: two stones are connected when they **share a
> row or a column**. Make the row and the column themselves the DSU nodes and treat each stone
> as the edge `row r — column c`. That avoids comparing all O(n²) pairs.

**Reduces to**: [graph § Template 3](./graph.md#template-3-union-find-with-path-compression--lc-684-) — union-find, where the nodes are attribute values rather than the items.

```python
# python
# LC 947 - Most Stones Removed with Same Row or Column
# IDEA: union ("row", r) with ("col", c) for each stone; answer = stones - components
# time = O(n log n) with path halving alone, space = O(n)
def removeStones(stones):
    parent = {}
    def find(x):
        parent.setdefault(x, x)
        while parent[x] != x:
            parent[x] = parent[parent[x]]      # path halving
            x = parent[x]
        return x
    def union(a, b):
        ra, rb = find(a), find(b)
        if ra != rb:
            parent[ra] = rb
    for r, c in stones:
        union(("row", r), ("col", c))          # the stone IS the edge row r -- column c
    roots = {find(("row", r)) for r, c in stones}
    return len(stones) - len(roots)

# [[0,0],[0,1],[1,0],[1,2],[2,1],[2,2]] -> 5   (one component of 6 stones)
# [[0,0],[0,2],[1,1],[2,0],[2,2]]       -> 3   (components of 4 and 1 stones)
```

```java
// java
// LC 947 - Most Stones Removed with Same Row or Column
// IDEA: union the stone's row with the stone's column (~col keeps the two id spaces apart)
// time = O(n log n) with path halving alone, space = O(n)
import java.util.*;

class Solution {
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
        for (int[] s : stones) {
            union(s[0], ~s[1]);          // row id = r, column id = ~c (negative, never a row)
        }
        Set<Integer> roots = new HashSet<>();
        for (int[] s : stones) {
            roots.add(find(s[0]));
        }
        return stones.length - roots.size();
    }
}
```

**Traps**
- Rows and columns are both small integers, so `union(r, c)` merges row 1 with column 1. Keep
  the namespaces apart: a tagged tuple, `~c`, or `c + 10001`.
- Count **roots**, not successful unions. A component contributes exactly one survivor whatever
  its size.
- The pairwise version (union every two stones sharing a row or column) is correct but O(n²);
  it is the one in [union_find_examples.md](./union_find_examples.md).

**Similar** — LC 721 (an email is the shared attribute) · LC 990 (a variable is the shared attribute)

## Bipartite Check Examples

### Possible Bipartition — LC 886

> **Key point** — "split into two groups so no two people who dislike each other share a group"
> is exactly "is the dislike graph bipartite?". Build the graph from the pairs first, then
> 2-colour every component: an edge between two equal colours is an odd cycle, and no split exists.

**Reduces to**: [graph § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) — the same BFS 2-colouring as LC 785, after building the graph from an edge list with 1-indexed people. The code is Template 6's, run on the dislike graph; the parity union-find form is [graph_advanced § Template 3](./graph_advanced.md#template-3-parity-union-find-bipartite-check--lc-886).

**Traps**
- The dislike graph is usually disconnected: launch from **every** unpainted person, or a
  conflict in a second component is never seen.
- People are numbered `1..n`, so size the colour array `n + 1`.

**Similar** — LC 785 (graph given as an adjacency list) · LC 1042 (four colours: greedy, not 2-colouring)
