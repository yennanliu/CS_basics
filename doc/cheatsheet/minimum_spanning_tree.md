# Minimum Spanning Tree (MST)

> **Scope** — Minimum spanning trees on undirected weighted graphs: the cut property, Kruskal (sorted edges + Union-Find) and Prim (heap or dense array), each traced step by step with its variants, and the problem shapes built on them — complete geometric graphs, a virtual source node, critical edges and bottleneck trees.
> **See also**: [union_find.md](./union_find.md) — the DSU that Kruskal runs on; [heap.md](./heap.md) — the priority queue under Prim; [shortest_path_comparison.md](./shortest_path_comparison.md#c-prim-vs-dijkstra--one-term-apart--lc-1584) — Prim vs Dijkstra, one term apart; [graph.md](./graph.md) — representation, traversal and the graph-problem catalogue that routes here.

- **Core idea**: connect **all** `n` nodes with exactly `n - 1` edges and the smallest total weight — greedily take the cheapest edge that does not close a cycle
- **When to use it**: "connect every point / city / house", "minimum cost so that all are connected", "exactly one simple path between any two points" — a cheapest *tree*, not a cheapest *path*
- **Key LeetCode problems**: LC 1584, LC 1135, LC 1168, LC 1489, LC 3600
- **Data structures**: Union-Find + sorted edge list (Kruskal), or min-heap + visited set (Prim)
- **Typical states**: Kruskal — `parent[]` and the count of edges taken; Prim — `in_tree` and the cheapest known edge into each outside node

**Time Complexity:** Kruskal `O(E log E)` (the sort); Prim with a heap `O(E log V)`; Prim with a plain array `O(V^2)`
**Space Complexity:** `O(V + E)` for the graph, `O(V)` for the DSU or the visited set

## LeetCode Problem Lists

- [Minimum Spanning Tree](https://leetcode.com/problem-list/minimum-spanning-tree/)
- [Union Find](https://leetcode.com/problem-list/union-find/)
- [Graph](https://leetcode.com/problem-list/graph/)

## 0) Concept

### 0-0) Core Principle

A **spanning tree** of a connected undirected graph with `n` nodes is a subset of its edges that
reaches every node with **no cycle** — so it always has **exactly `n - 1` edges**, and there is
exactly one simple path between any two nodes. The **minimum** spanning tree is the one whose
edge weights add up to the least.

**Why greedy works — the two properties:**

- **Cut property**: split the nodes into any two groups. The cheapest edge crossing that split
  belongs to *some* MST. Prim is this property applied over and over (the split is "in the tree"
  vs "not yet").
- **Cycle property**: the most expensive edge on any cycle is never needed. Kruskal is this
  property applied over and over (an edge whose ends are already connected would close a cycle,
  and it is the most expensive one on that cycle, because every earlier edge was cheaper).

**Important Insight:**
- An MST minimises the **sum** of the tree's edges. It does **not** minimise the distance
  between any two particular nodes — that is shortest path (Dijkstra), a different problem.
- An MST also minimises the **largest** edge in the tree (it is a *minimum bottleneck* spanning
  tree), which is why "minimise the maximum edge" problems are Kruskal sweeps in disguise.
- If the graph is disconnected there is no spanning tree: Kruskal ends with fewer than `n - 1`
  edges, Prim ends with fewer than `n` nodes visited. Return `-1` when the problem asks.

### 0-1) Types

1. **Classic weighted edge list** — edges are given; run Kruskal as-is (LC 1135)
2. **Complete geometric graph** — every pair of points is an edge, weight = a distance (LC 1584)
3. **Virtual source node** — "build a station at a node *or* connect to one": add node `0` whose edge to node `i` costs the station price (LC 1168)
4. **Edge classification** — which edges are in every MST / some MST: rebuild the MST with one edge excluded or forced (LC 1489)
5. **Bottleneck tree** — maximise the weakest edge / minimise the strongest edge of a spanning tree: sort-and-union, or binary search + DSU feasibility (LC 3600, cf. LC 1631)

### 0-2) Kruskal or Prim? ⭐⭐⭐⭐

| The graph you are handed | Reach for | Why |
|---|---|---|
| An edge list, `E` close to `V` (sparse) | **Kruskal** | Edges already exist; sort them once, `O(E log E)` |
| An adjacency list, or "start anywhere and grow" | **Prim + heap** | Same scaffolding as Dijkstra, `O(E log V)` |
| Points on a plane — **every pair** is an edge (dense, `E ≈ V²/2`) | **Prim + array** | `O(V²)`, no edge list, `O(V)` memory — the optimum, since every pair must be looked at |
| You need to exclude / force a specific edge | **Kruskal** | Easy to skip or pre-union one edge, then rerun |
| Edges arrive online, or you stop as soon as two nodes connect | **Kruskal** | Its DSU answers "connected yet?" after every edge |

In an interview, Kruskal is usually the faster one to write correctly; Prim is the one to name
when the graph is dense, and the dense-array Prim is the one that actually beats the others there.

---

## 1) General form

### 1-1) Kruskal — sorted edges + Union-Find ⭐⭐⭐⭐⭐

**Steps:**
1. Sort every edge by weight, cheapest first.
2. For each edge `(w, a, b)`: if `a` and `b` are already in the same component, skip it (it would close a cycle); otherwise union them and add `w`.
3. Stop once `n - 1` edges are taken. Fewer than `n - 1` at the end → the graph is disconnected.

```python
# python
# IDEA: Kruskal — sort edges, take each one that joins two different components
# time = O(E log E), space = O(V + E)
def kruskal(n, edges):
    """n nodes 0..n-1, edges = [(weight, a, b), ...]; return MST cost or -1."""
    parent = list(range(n))
    rank = [0] * n

    def find(x):
        # path halving: point x at its grandparent while walking up
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    def union(a, b):
        root_a, root_b = find(a), find(b)
        if root_a == root_b:
            return False                # already connected -> this edge closes a cycle
        if rank[root_a] < rank[root_b]:
            root_a, root_b = root_b, root_a
        parent[root_b] = root_a         # hang the shorter tree under the taller one
        if rank[root_a] == rank[root_b]:
            rank[root_a] += 1
        return True

    total_cost = 0
    edges_used = 0
    for weight, a, b in sorted(edges):
        if union(a, b):
            total_cost += weight
            edges_used += 1
            if edges_used == n - 1:     # a tree on n nodes has exactly n - 1 edges
                return total_cost
    return total_cost if n <= 1 else -1  # ran out of edges: disconnected
```

```java
// java
// IDEA: Kruskal — sort edges, take each one that joins two different components
// time = O(E log E), space = O(V + E)
public int kruskal(int n, int[][] edges) {    // edges[i] = {weight, a, b}
    int[] parent = new int[n];
    int[] rank = new int[n];
    for (int i = 0; i < n; i++) {
        parent[i] = i;
    }

    int[][] sorted = edges.clone();
    Arrays.sort(sorted, (x, y) -> Integer.compare(x[0], y[0]));

    int totalCost = 0;
    int edgesUsed = 0;
    for (int[] edge : sorted) {
        int rootA = find(parent, edge[1]);
        int rootB = find(parent, edge[2]);
        if (rootA == rootB) {
            continue;                       // would close a cycle
        }
        if (rank[rootA] < rank[rootB]) {
            int tmp = rootA; rootA = rootB; rootB = tmp;
        }
        parent[rootB] = rootA;
        if (rank[rootA] == rank[rootB]) {
            rank[rootA]++;
        }
        totalCost += edge[0];
        edgesUsed++;
        if (edgesUsed == n - 1) {
            return totalCost;
        }
    }
    return n <= 1 ? 0 : -1;                 // disconnected
}

private int find(int[] parent, int x) {
    while (parent[x] != x) {
        parent[x] = parent[parent[x]];      // path halving
        x = parent[x];
    }
    return x;
}
```

#### Kruskal step by step — one graph, every edge in order

The same graph is traced through Prim in [1-2](#1-2-prim--grow-one-tree-with-a-min-heap-), so the
two orders can be compared — both end at the same tree.

```text
graph G, 5 nodes:  0-1 (2)  1-2 (3)  2-4 (4)  1-4 (5)  0-3 (6)  1-3 (8)  3-4 (9)

    0 ---2--- 1 ---3--- 2
    |       /   \      /
    6     8       5   4
    |   /           \/
    3 -------9------ 4

sorted edge   find(a) == find(b)?   action      components after               total
0-1 (2)       no                    take        {0,1} {2} {3} {4}                2
1-2 (3)       no                    take        {0,1,2} {3} {4}                  5
2-4 (4)       no                    take        {0,1,2,4} {3}                    9
1-4 (5)       YES — 1 and 4 joined  skip        (would close cycle 1-2-4)
0-3 (6)       no                    take        {0,1,2,3,4}                     15
              4 edges = n - 1  ->  stop; 1-3 (8) and 3-4 (9) are never looked at

MST = {0-1, 1-2, 2-4, 0-3}, cost 15
```

**Where the time goes:** the sort is `O(E log E)`, which is the same as `O(E log V)` because
`E ≤ V²`. The `E` find/union calls cost `O(E · α(V))` with path compression and union by rank —
effectively linear. So Kruskal is *a sort plus a linear scan*; if the edges arrive already
sorted (or the weights are small integers you can bucket), it is near-linear.

#### Kruskal variants — what changes in the template

| Problem says | Change to [1-1](#1-1-kruskal--sorted-edges--union-find-) | LC |
|---|---|---|
| **Maximum** spanning tree / maximise total | sort **descending** (or negate the weights) | 1102 (as a bottleneck) |
| The graph may be disconnected; cost per component | do not return `-1`; the result is a **minimum spanning forest** with `n - edges_used` trees | — |
| "Minimise the largest edge on a path from `s` to `t`" | stop the sweep the moment `find(s) == find(t)`; the current weight is the answer | 1631, 1102 |
| Many "connected using edges `< limit`?" queries | sort the **queries** by limit too, and sweep both together (offline) | 1697 — [union_find_examples.md §13](./union_find_examples.md#13-path-with-minimum-effort--lc-1631--sorted-edge-kruskal-style) |
| Some edges are mandatory | union them (and add their cost) **before** the sweep | 1489, 3600 |
| "Build here **or** connect" | add a virtual node `0` with one edge per node | 1168 — [2-3](#2-3-optimize-water-distribution-in-a-village--lc-1168-) |
| Return the tree, not only its cost | collect each taken edge | below |

**Reason for a second snippet: it returns the tree's edges and covers the maximum variant**, which
the cost-only template cannot.

```python
# python
# IDEA: Kruskal that returns the chosen edges; maximum=True builds a MAXIMUM spanning tree
# time = O(E log E), space = O(V + E)
def kruskal_tree(n, edges, maximum=False):
    """edges = [(weight, a, b), ...]; return (total, tree_edges).
    len(tree_edges) < n - 1 means the graph is disconnected (the result is a forest)."""
    parent = list(range(n))

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]
            x = parent[x]
        return x

    total = 0
    tree_edges = []
    for weight, a, b in sorted(edges, reverse=maximum):
        root_a, root_b = find(a), find(b)
        if root_a == root_b:
            continue                    # closes a cycle
        parent[root_a] = root_b
        total += weight
        tree_edges.append((a, b, weight))
        if len(tree_edges) == n - 1:
            break
    return total, tree_edges
```

### 1-2) Prim — grow one tree with a min-heap ⭐⭐⭐⭐⭐

**Steps:**
1. Push `(0, start)` — joining the first node costs nothing.
2. Pop the cheapest `(cost, node)`. If `node` is already in the tree, skip it — it was joined more cheaply earlier.
3. Otherwise add it to the tree, add `cost`, and push `(w, neighbour)` for every edge to a neighbour still outside.
4. Stop once all `n` nodes are in. An empty heap before that → disconnected.

```python
# python
# IDEA: Prim — the cheapest edge leaving the tree is always safe (cut property)
# time = O(E log V), space = O(V + E)
import heapq

def prim(n, adj):
    """adj[u] = [(weight, v), ...] for an undirected graph; return MST cost or -1."""
    in_tree = [False] * n
    min_heap = [(0, 0)]                 # (cost to join the tree, node)
    total_cost = 0
    nodes_joined = 0

    while min_heap and nodes_joined < n:
        cost, node = heapq.heappop(min_heap)
        if in_tree[node]:
            continue                    # a stale, more expensive offer for this node
        in_tree[node] = True
        total_cost += cost
        nodes_joined += 1
        for weight, neighbour in adj[node]:
            if not in_tree[neighbour]:
                heapq.heappush(min_heap, (weight, neighbour))

    return total_cost if nodes_joined == n else -1
```

```java
// java
// IDEA: Prim — the cheapest edge leaving the tree is always safe (cut property)
// time = O(E log V), space = O(V + E)
public int prim(int n, List<List<int[]>> adj) {  // adj.get(u) = [{weight, v}, ...]
    boolean[] inTree = new boolean[n];
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));
    minHeap.offer(new int[]{0, 0});              // {cost to join the tree, node}
    int totalCost = 0;
    int nodesJoined = 0;

    while (!minHeap.isEmpty() && nodesJoined < n) {
        int[] top = minHeap.poll();
        int cost = top[0];
        int node = top[1];
        if (inTree[node]) {
            continue;                            // stale offer
        }
        inTree[node] = true;
        totalCost += cost;
        nodesJoined++;
        for (int[] edge : adj.get(node)) {
            if (!inTree[edge[1]]) {
                minHeap.offer(new int[]{edge[0], edge[1]});
            }
        }
    }
    return nodesJoined == n ? totalCost : -1;
}
```

#### Prim step by step — the same graph G

The graph from [Kruskal step by step](#kruskal-step-by-step--one-graph-every-edge-in-order),
grown from node `0`. Prim never looks at the global edge order — only at the cheapest edge
**leaving the current tree**:

```text
pop        in tree?   action                 push                         heap after              total
(0, 0)     no         join 0                 (2,1) (6,3)                  (2,1) (6,3)                 0
(2, 1)     no         join 1 via 0-1         (3,2) (5,4) (8,3)            (3,2) (5,4) (6,3) (8,3)     2
(3, 2)     no         join 2 via 1-2         (4,4)                        (4,4) (5,4) (6,3) (8,3)     5
(4, 4)     no         join 4 via 2-4         (9,3)                        (5,4) (6,3) (8,3) (9,3)     9
(5, 4)     YES        stale — skip                                        (6,3) (8,3) (9,3)
(6, 3)     no         join 3 via 0-3                                                                 15
all 5 nodes joined -> stop

MST = {0-1, 1-2, 2-4, 0-3}, cost 15   — the same tree Kruskal built, found in a different order
```

The stale `(5, 4)` is the lazy version's cost: a node is pushed once per neighbour that offers
it, and only the first pop counts.

#### Lazy, eager and dense Prim — and recovering the tree

| Version | Heap holds | Time | Use when |
|---|---|---|---|
| **Lazy** (the template above) | every offered edge; stale ones skipped on pop | `O(E log E)` = `O(E log V)` | the default — Python's `heapq` has no decrease-key |
| **Eager** (indexed heap) | one entry per outside node, its key lowered in place | `O(E log V)`, heap size `≤ V` | Java / C++ with an indexed priority queue; rarely worth it in an interview |
| **Dense** (array, no heap) | nothing — `min_dist[]` scanned each round | `O(V²)` | complete or near-complete graphs — [2-1](#2-1-min-cost-to-connect-all-points--lc-1584-) |

To return the tree rather than its cost, carry the node an edge came **from** in the heap entry
— that is Prim's `parent[]`. **Reason for a second snippet: it returns the edges**, which the
template's `(cost, node)` entries cannot.

```python
# python
# IDEA: Prim that records which tree node each node was joined from
# time = O(E log V), space = O(V + E)
import heapq

def prim_tree(n, adj, start=0):
    """adj[u] = [(weight, v), ...]; return (total, tree_edges), or None if disconnected."""
    in_tree = [False] * n
    min_heap = [(0, start, -1)]         # (edge weight, node, the tree node it joins from)
    total = 0
    tree_edges = []

    while min_heap:
        weight, node, came_from = heapq.heappop(min_heap)
        if in_tree[node]:
            continue
        in_tree[node] = True
        total += weight
        if came_from != -1:
            tree_edges.append((came_from, node, weight))
        for next_weight, neighbour in adj[node]:
            if not in_tree[neighbour]:
                heapq.heappush(min_heap, (next_weight, neighbour, node))

    return (total, tree_edges) if len(tree_edges) == n - 1 else None
```

### 1-3) Prim vs Dijkstra — one term apart

The two loops are the same shape — heap of `(key, node)`, skip a node popped twice, push its
neighbours. Only the key differs:

```text
Dijkstra : push (dist[u] + w, v)   key = whole PATH cost from the source   -> cheapest path A -> B
Prim     : push (w, v)             key = the single EDGE into the tree     -> cheapest tree over ALL
```

Writing `cost + weight` in Prim turns it into Dijkstra and returns the wrong answer on LC 1584
(the shortest-path tree is not the minimum spanning tree). See
[shortest_path_comparison.md](./shortest_path_comparison.md#c-prim-vs-dijkstra--one-term-apart--lc-1584).

### 1-4) Pitfalls

- **Counting cost on a stale pop.** In lazy Prim a node is pushed once per neighbour; only its
  **first** pop is the cheapest. Check `in_tree` *before* adding the cost.
- **Treating directed edges as undirected (or vice versa).** MST is for **undirected** graphs —
  add both directions to Prim's adjacency list. A directed "minimum arborescence" is a different
  algorithm (Chu–Liu/Edmonds) and does not come up in interviews.
- **Forgetting the disconnected case.** Check `edges_used == n - 1` (Kruskal) or
  `nodes_joined == n` (Prim) before returning a cost.
- **1-indexed nodes.** LC 1135 and LC 1168 number nodes `1..n`; size `parent` as `n + 1`.
- **Building an edge list for a complete graph.** For `n = 1000` points that is ~500,000 edges
  and a sort; the dense Prim in [2-1](#2-1-min-cost-to-connect-all-points--lc-1584-) needs no
  edge list at all.

---

## 2) LC Example

### 2-1) Min Cost to Connect All Points — LC 1584 ⭐⭐⭐⭐⭐

**Problem:** connect all `points` on a plane; joining two points costs their Manhattan distance
`|xi - xj| + |yi - yj|`. Return the minimum total cost.

**Key Idea:** "exactly one simple path between any two points" means a spanning **tree**, and
the cheapest one is the MST of the **complete** graph — every point is a node, every pair is an
edge. The edges are never given; compute each one when it is needed.

**Trace** — `points = [[0,0],[2,2],[3,10],[5,2],[7,0]]`:

```text
start from point 0 (cost 0)
  tree {0}            cheapest way out: 0-1 = 4        take 1   total  4
  tree {0,1}          cheapest way out: 1-3 = 3        take 3   total  7
  tree {0,1,3}        cheapest way out: 3-4 = 4        take 4   total 11
  tree {0,1,3,4}      cheapest way out: 1-2 = 9        take 2   total 20
  all 5 points in the tree -> 20
```

```python
# python
# LC 1584 - Min Cost to Connect All Points
# IDEA: Prim + min-heap on the implicit complete graph
# time = O(n^2 log n), space = O(n^2)
import heapq

class Solution:
    def minCostConnectPoints(self, points):
        n = len(points)
        in_tree = set()
        min_heap = [(0, 0)]             # (cost to join this point, point index)
        total_cost = 0

        while len(in_tree) < n:
            cost, point = heapq.heappop(min_heap)
            if point in in_tree:
                continue                # only the FIRST (cheapest) pop joins the tree
            in_tree.add(point)
            total_cost += cost

            x1, y1 = points[point]
            for other in range(n):
                if other not in in_tree:
                    x2, y2 = points[other]
                    heapq.heappush(min_heap, (abs(x1 - x2) + abs(y1 - y2), other))

        return total_cost
```

```java
// java
// LC 1584 - Min Cost to Connect All Points
// IDEA: Prim + min-heap on the implicit complete graph
// time = O(n^2 log n), space = O(n^2)
public int minCostConnectPoints(int[][] points) {
    int n = points.length;
    boolean[] inTree = new boolean[n];
    PriorityQueue<int[]> minHeap = new PriorityQueue<>((x, y) -> Integer.compare(x[0], y[0]));
    minHeap.offer(new int[]{0, 0});              // {cost to join, point index}
    int totalCost = 0;
    int joined = 0;

    while (joined < n) {
        int[] top = minHeap.poll();
        int point = top[1];
        if (inTree[point]) {
            continue;
        }
        inTree[point] = true;
        totalCost += top[0];
        joined++;
        for (int other = 0; other < n; other++) {
            if (!inTree[other]) {
                int dist = Math.abs(points[point][0] - points[other][0])
                         + Math.abs(points[point][1] - points[other][1]);
                minHeap.offer(new int[]{dist, other});
            }
        }
    }
    return totalCost;
}
```

#### Dense Prim — `O(n²)`, no heap

**Reason for a second solution: a better bound.** The graph is complete, so a heap only adds a
`log n` and `O(n²)` memory. Keep `min_dist[i]` = the cheapest edge from the tree to point `i`,
and scan for the smallest each round: `n` rounds × `O(n)` scan = `O(n²)` time, `O(n)` space —
optimal, since every pair has to be looked at once. Name it after the heap version.

```python
# python
# LC 1584 - Min Cost to Connect All Points
# IDEA: Prim with a min_dist array instead of a heap (dense graph)
# time = O(n^2), space = O(n)
class Solution:
    def minCostConnectPoints(self, points):
        n = len(points)
        INF = float("inf")
        min_dist = [INF] * n            # cheapest edge from the tree to each point
        in_tree = [False] * n
        min_dist[0] = 0
        total_cost = 0

        for _ in range(n):
            # the outside point closest to the tree
            closest = -1
            for i in range(n):
                if not in_tree[i] and (closest == -1 or min_dist[i] < min_dist[closest]):
                    closest = i
            in_tree[closest] = True
            total_cost += min_dist[closest]

            # the new tree point may offer a cheaper edge to the points still outside
            x1, y1 = points[closest]
            for i in range(n):
                if not in_tree[i]:
                    distance = abs(x1 - points[i][0]) + abs(y1 - points[i][1])
                    if distance < min_dist[i]:
                        min_dist[i] = distance

        return total_cost
```

Kruskal also passes — generate all `n(n-1)/2` pairs and feed them to
[1-1](#1-1-kruskal--sorted-edges--union-find-) — but it is the slowest of the three here,
because it sorts every pair.

### 2-2) Connecting Cities With Minimum Cost — LC 1135

**Problem:** `n` cities `1..n`, `connections[i] = [a, b, cost]`. Return the minimum cost to
connect every city, or `-1` if that is impossible.

**Key Idea:** this is [1-1](#1-1-kruskal--sorted-edges--union-find-) with no disguise — an edge
list, a sparse graph, and the disconnected case as the answer `-1`. The only change is the
1-indexed cities: shift them to `0..n-1` (or size the DSU `n + 1`).

```python
# python
# LC 1135 - Connecting Cities With Minimum Cost
# IDEA: Kruskal (template 1-1), cities shifted to 0-indexed
# time = O(E log E), space = O(n + E)
class Solution:
    def minimumCost(self, n, connections):
        edges = [(cost, a - 1, b - 1) for a, b, cost in connections]
        return kruskal(n, edges)        # -1 when fewer than n - 1 edges join
```

### 2-3) Optimize Water Distribution in a Village — LC 1168 ⭐⭐⭐⭐

**Problem:** each house `i` either builds a well for `wells[i-1]` or is piped to another house.
Return the minimum total cost to supply water to every house.

**Key Idea — the virtual node.** "Build a well at house `i`" is the same as "lay a pipe from a
water source `0` to house `i`" with cost `wells[i-1]`. Add that node and those `n` edges, and the
answer is the MST of the `n + 1` node graph. Every "pay a fixed price per node *or* connect it to
a neighbour" problem reduces this way.

```text
n = 3, wells = [1,2,2], pipes = [[1,2,1],[2,3,1]]

virtual edges: 0-1 (1)  0-2 (2)  0-3 (2)
pipe edges   : 1-2 (1)  2-3 (1)

Kruskal: 0-1 (1) take, 1-2 (1) take, 2-3 (1) take -> 3 edges for 4 nodes -> cost 3
```

```python
# python
# LC 1168 - Optimize Water Distribution in a Village
# IDEA: Kruskal + virtual node 0 (a well == a pipe from node 0)
# time = O((n + m) log (n + m)), space = O(n + m)
class Solution:
    def minCostToSupplyWater(self, n, wells, pipes):
        edges = [(cost, 0, house + 1) for house, cost in enumerate(wells)]
        for a, b, cost in pipes:
            edges.append((cost, a, b))
        return kruskal(n + 1, edges)    # n houses + the water source
```

### 2-4) Find Critical and Pseudo-Critical Edges in MST — LC 1489

**Problem:** classify each edge: **critical** if every MST contains it, **pseudo-critical** if
some MST contains it but not all.

**Key Idea — rebuild the MST around one edge.** Compute the MST weight `best` once. Then for
each edge `e`:

- **exclude** `e` and rebuild: if the graph disconnects or the weight rises above `best`, `e` is
  **critical**;
- otherwise **force** `e` first (union it, count its weight) and rebuild: if the weight still
  equals `best`, `e` is **pseudo-critical**.

`E + 1` Kruskal runs over edges sorted once: `O(E² · α(V))`, fine for `E ≤ 200`.

```python
# python
# LC 1489 - Find Critical and Pseudo-Critical Edges in Minimum Spanning Tree
# IDEA: Kruskal with one edge excluded / forced, compared with the true MST weight
# time = O(E^2 * α(V)), space = O(V + E)
class Solution:
    def findCriticalAndPseudoCriticalEdges(self, n, edges):
        # keep the original index: the answer is reported by it
        order = sorted(range(len(edges)), key=lambda i: edges[i][2])

        def mst_weight(skip, force):
            parent = list(range(n))

            def find(x):
                while parent[x] != x:
                    parent[x] = parent[parent[x]]
                    x = parent[x]
                return x

            weight = 0
            used = 0
            if force != -1:
                a, b, w = edges[force]
                parent[find(a)] = find(b)
                weight += w
                used += 1
            for i in order:
                if i == skip:
                    continue
                a, b, w = edges[i]
                root_a, root_b = find(a), find(b)
                if root_a != root_b:
                    parent[root_a] = root_b
                    weight += w
                    used += 1
            return weight if used == n - 1 else float("inf")

        best = mst_weight(-1, -1)
        critical, pseudo = [], []
        for i in range(len(edges)):
            if mst_weight(i, -1) > best:
                critical.append(i)
            elif mst_weight(-1, i) == best:
                pseudo.append(i)
        return [critical, pseudo]
```

### 2-5) Similar Problems

| LC | Problem | Shape | Note |
|---|---|---|---|
| 1584 | Min Cost to Connect All Points | complete geometric graph | Prim; dense array Prim is `O(n²)` — [2-1](#2-1-min-cost-to-connect-all-points--lc-1584-) |
| 1135 | Connecting Cities With Minimum Cost | edge list | Kruskal as-is, `-1` if disconnected — [2-2](#2-2-connecting-cities-with-minimum-cost--lc-1135) |
| 1168 | Optimize Water Distribution in a Village | virtual source node | well = edge from node `0` — [2-3](#2-3-optimize-water-distribution-in-a-village--lc-1168-) |
| 1489 | Find Critical and Pseudo-Critical Edges in MST | edge classification | exclude / force one edge — [2-4](#2-4-find-critical-and-pseudo-critical-edges-in-mst--lc-1489) |
| 3600 | Maximize Spanning Tree Stability with Upgrades | bottleneck tree | binary search the stability, DSU checks a spanning tree exists with every edge `≥` it |
| 1102 | Path With Maximum Minimum Value | bottleneck path, **maximum** | Kruskal sorted descending, stop when the corners join — [Kruskal variants](#kruskal-variants--what-changes-in-the-template) |
| 1697 | Checking Existence of Edge Length Limited Paths | offline queries | sort queries and edges, sweep together — [union_find_examples.md §13](./union_find_examples.md#13-path-with-minimum-effort--lc-1631--sorted-edge-kruskal-style) |
| 1631 | Path With Minimum Effort | bottleneck path | a Kruskal sweep stopped when source and target join — [union_find_examples.md](./union_find_examples.md#13-path-with-minimum-effort--lc-1631--sorted-edge-kruskal-style) |
| 1167 | Minimum Cost to Connect Sticks | **not** MST | merging cost re-counts merged lengths — Huffman, a heap greedy ([heap.md](./heap.md)) |

**Decision tree:**

```text
"connect ALL nodes, minimum total cost"?
├── no: "cheapest route from A to B"            -> shortest path (Dijkstra.md)
└── yes: an MST
    ├── edges given as a list                   -> Kruskal (1-1)
    ├── every pair is an edge (points)          -> Prim, dense array for O(n^2) (2-1)
    ├── "build here OR connect to a neighbour"  -> add a virtual node 0, then Kruskal (2-3)
    ├── "which edges are in every / some MST"   -> Kruskal with one edge excluded / forced (2-4)
    └── "maximise the min / minimise the max"   -> sort + union until connected, or
                                                   binary search + DSU feasibility (2-5)
```
