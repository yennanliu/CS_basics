# Advanced Graph Algorithms

> **Scope** — The graph techniques a first interview pass should skip: Tarjan's low-link family (strongly connected components, bridges, articulation points), Euler paths and circuits, and the bipartite extras — parity Union-Find, maximum matching (and the max-flow view of it) and greedy k-colouring.
> **See also**: [graph.md](./graph.md) — representation, traversal, connectivity, cycle detection and 2-colouring, plus the chooser table that routes here; [graph_examples.md](./graph_examples.md) — the classic problems on graph.md's templates; [dfs_advanced.md](./dfs_advanced.md) — the rare DFS tricks that are not graph theory (two-grid validation, trie DFS, depth-indexed stacks); [union_find.md](./union_find.md) — the DSU primitives Template 3 builds on.

## LeetCode Problem Lists

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Strongly Connected Component](https://leetcode.com/problem-list/strongly-connected-component/)
- [Eulerian Circuit](https://leetcode.com/problem-list/eulerian-circuit/)
- [Biconnected Component](https://leetcode.com/problem-list/biconnected-component/)

## Overview

Everything here is a **low-link DFS**, an **edge-consuming DFS**, or an **augmenting-path
search**. They share one shape: a plain traversal plus one extra piece of state — `low[]`, a
consumed adjacency list, or a match / residual-capacity array — that turns the traversal into a
proof. An L3 loop rarely asks for any of them; when it does, it is almost always bridges
(LC 1192) or the itinerary (LC 332), and the statement names the structure outright.

### Prerequisites
- [graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-): DFS
  with per-node state. Tarjan adds a discovery timestamp and a `low[]` value to the same DFS.
- [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-): BFS / DFS
  2-colouring. Templates 3–5 below are what to do when that is not enough.
- [graph.md § Template 3](./graph.md#template-3-union-find-with-path-compression--lc-684-):
  union-find with path compression, which Template 3 doubles into a parity DSU.
- [dfs.md § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-): the recursive
  graph DFS every template here starts from.

## Recognition

| Signal in the statement | Constraint cue | Reach for | Not this if |
|---|---|---|---|
| "critical connections", "which edge, if removed, disconnects the network" | V ≤ 10⁵, E ≤ 10⁵ | [Template 1](#template-1-tarjans-low-link-dfs--scc-bridges-articulation-points--lc-1192-) — bridge iff `low[child] > disc[parent]` | only "connected or not" after one removal → re-run [graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-) |
| "which server / cell, if removed, splits the network" | V ≤ 10⁵ | [Template 1's articulation-point variant](#variant-articulation-points-cut-vertices) | the grid answer is always ≤ 2 (LC 1568) → try removing each cell |
| "mutually reachable", "strongly connected" | directed, V ≤ 10⁵ | [Template 1's SCC variant](#variant-strongly-connected-components-directed) | undirected → that is just a connected component |
| "use every ticket / edge exactly once" | E ≤ 300 | [Template 2](#template-2-euler-path--circuit-hierholzer--lc-332-) — Hierholzer | visit every **node** once → Hamiltonian path, bitmask DP |
| "shortest string that contains every combination" | k^n ≤ 4,096 | [Cracking the Safe — LC 753](#cracking-the-safe--lc-753) — de Bruijn graph + Euler circuit | — |
| "two groups, these pairs must be apart", pairs arrive online | n ≤ 2,000 | [Template 3](#template-3-parity-union-find-bipartite-check--lc-886) — parity DSU | all pairs known up front → BFS 2-colouring, [graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) |
| "largest number of pairs", "assign each X to at most one Y" | sides ≤ 200 | [Template 4](#template-4-bipartite-matching-by-augmenting-paths-kuhn--lc-1820) — Kuhn | a cost on each pairing → min-cost assignment (Hungarian / DP over masks) |
| "maximum throughput", "cheapest set of links to cut" | V ≤ a few hundred | [Template 4's max-flow generalisation](#generalisation-max-flow--min-cut-edmonds-karp) | — |
| "k colours, no two neighbours equal", every degree < k | V ≤ 10⁴ | [Template 5](#template-5-greedy-k-colouring--lc-1042) — greedy | k = 2 → bipartite check; small degree bound absent → backtracking |

## Low-Link DFS

### Template 1: Tarjan's Low-Link DFS — SCC, Bridges, Articulation Points — LC 1192 ⭐⭐⭐⭐

**Recognise** — "critical connection", "which edge, if removed, disconnects the graph", "cut
vertex". Undirected, V up to 10⁵: removing each edge and re-checking connectivity is
O(E·(V + E)), too slow; one DFS is O(V + E).

**Key point** — `disc[u]` is when the DFS first reached `u`; `low[u]` is the smallest `disc`
that `u`'s subtree can reach using tree edges plus **one** back edge. A tree edge `u → v` is a
bridge exactly when `low[v] > disc[u]`: nothing in `v`'s subtree climbs back to `u` or above, so
cutting `u–v` isolates it.

```python
# python
# LC 1192 - Critical Connections in a Network
# IDEA: Tarjan low-link; tree edge u -> v is a bridge iff low[v] > disc[u]
# time = O(V + E), space = O(V + E)
import sys

def criticalConnections(n, connections):
    sys.setrecursionlimit(max(1000, 2 * n + 10))   # the DFS can be n deep
    graph = [[] for _ in range(n)]
    for a, b in connections:
        graph[a].append(b)
        graph[b].append(a)

    disc = [-1] * n          # discovery time, -1 = unvisited
    low = [0] * n            # lowest disc reachable from u's subtree via one back edge
    timer = [0]
    bridges = []

    def dfs(u, parent):
        disc[u] = low[u] = timer[0]
        timer[0] += 1
        for v in graph[u]:
            if v == parent:
                continue                        # do not walk straight back up the tree edge
            if disc[v] == -1:                   # tree edge
                dfs(v, u)
                low[u] = min(low[u], low[v])    # pull the child's reach up
                if low[v] > disc[u]:            # v's subtree cannot reach u or above
                    bridges.append([u, v])
            else:                               # back edge
                low[u] = min(low[u], disc[v])

    for start in range(n):                      # the loop also covers a disconnected graph
        if disc[start] == -1:
            dfs(start, -1)
    return bridges
```

```java
// java
// LC 1192 - Critical Connections in a Network
// IDEA: Tarjan low-link; tree edge u -> v is a bridge iff low[v] > disc[u]
// time = O(V + E), space = O(V + E)
class Solution {
    private List<List<Integer>> graph;
    private int[] disc;
    private int[] low;
    private int timer;
    private List<List<Integer>> bridges;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (List<Integer> e : connections) {
            graph.get(e.get(0)).add(e.get(1));
            graph.get(e.get(1)).add(e.get(0));
        }
        disc = new int[n];
        low = new int[n];
        Arrays.fill(disc, -1);
        timer = 0;
        bridges = new ArrayList<>();
        for (int start = 0; start < n; start++) {
            if (disc[start] == -1) {
                dfs(start, -1);
            }
        }
        return bridges;
    }

    private void dfs(int u, int parent) {
        disc[u] = low[u] = timer++;
        for (int v : graph.get(u)) {
            if (v == parent) {
                continue;
            }
            if (disc[v] == -1) {
                dfs(v, u);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u]) {
                    bridges.add(Arrays.asList(u, v));
                }
            } else {
                low[u] = Math.min(low[u], disc[v]);   // back edge: disc[v], not low[v]
            }
        }
    }
}
```

**Complexity**
- **Time O(V + E)**: `dfs` runs once per vertex (guarded by `disc[v] == -1`), and its
  `for v in graph[u]` loop reads each adjacency entry once, 2E in total.
- **Space O(V + E)**: the adjacency list, plus `disc` / `low` and a recursion stack up to V deep.

**Dry run** — `n = 4`, edges `[[0,1],[1,2],[2,0],[1,3]]`:

```text
step                  disc  low (after)              note
dfs(0)                0:0   low0=0
  dfs(1) from 0       1:1   low1=1
    dfs(2) from 1     2:2   2 sees 0 (visited)  -> low2 = min(2, disc0=0) = 0
    back in 1                low1 = min(1, low2=0) = 0;  low2=0 > disc1=1? no
    dfs(3) from 1     3:3   3 sees only its parent      low3 = 3
    back in 1                low3=3 > disc1=1  -> bridge [1, 3]
  back in 0                  low0 = 0;  low1=0 > disc0=0? no
result: [[1, 3]]
```

**Traps**
- On a back edge take `disc[v]`, not `low[v]`. For bridges both happen to work, but for
  articulation points `low[v]` carries the ancestor's own reach further up and hides a cut
  vertex.
- `v == parent` assumes no parallel edges (true on LC 1192). With a doubled edge `u–v`, skip by
  **edge id** instead, or the duplicate is wrongly reported as a bridge.
- Python's default recursion limit is 1,000; a 10⁵-node path needs the `setrecursionlimit`
  line, or an explicit stack.

**Not this if** — the question is only "is the graph still connected after removing this one
edge": one traversal per query
([graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-)) is
simpler when there are few queries. Edges arrive over time and only connectivity matters →
union-find.

**Drill** — LC 1192 ⭐ · LC 1568 *(articulation points on a grid; the answer is at most 2)* ·
LC 2360 *(cycles in a functional graph, the SCC idea without Tarjan)*

#### Variant: strongly connected components (directed)

The same DFS on a **directed** graph, with a stack of the vertices on the current DFS path.
When `low[v] == disc[v]` after `v`'s children finish, `v` is the root of an SCC: pop the stack
down to `v`. A back edge counts only if its target is still on the stack (a finished SCC is
closed).

```python
# python
# Tarjan SCC
# IDEA: v roots an SCC when low[v] == disc[v]; pop the path stack down to v
# time = O(V + E), space = O(V)
def tarjan_scc(n, graph):
    disc = [-1] * n
    low = [0] * n
    on_stack = [False] * n
    stack = []
    sccs = []
    timer = [0]

    def dfs(v):
        disc[v] = low[v] = timer[0]
        timer[0] += 1
        stack.append(v)
        on_stack[v] = True
        for u in graph[v]:
            if disc[u] == -1:
                dfs(u)
                low[v] = min(low[v], low[u])
            elif on_stack[u]:                   # back edge into the open SCC
                low[v] = min(low[v], disc[u])
        if low[v] == disc[v]:                   # v is the root of an SCC
            component = []
            while True:
                u = stack.pop()
                on_stack[u] = False
                component.append(u)
                if u == v:
                    break
            sccs.append(component)

    for v in range(n):
        if disc[v] == -1:
            dfs(v)
    return sccs

# 0 -> 1 -> 2 -> 0, 2 -> 3, 3 -> 4 -> 5 -> 3  =>  [[5, 4, 3], [2, 1, 0]]
```

**Kosaraju** is the two-pass alternative: DFS once to record finish order, then DFS the
**reversed** graph in reverse finish order; each tree of the second pass is an SCC. Same
O(V + E), easier to get right under pressure, needs the reversed graph in memory.

#### Variant: articulation points (cut vertices)

A non-root `u` is a cut vertex when some child `v` has `low[v] >= disc[u]` (note `>=`: the child
may reach `u` itself, but nothing above it). The DFS root is a cut vertex exactly when it has two
or more DFS children.

```python
# python
# Tarjan articulation points
# IDEA: non-root u is a cut vertex iff some child has low[child] >= disc[u]; root iff >= 2 children
# time = O(V + E), space = O(V + E)
def articulation_points(n, edges):
    graph = [[] for _ in range(n)]
    for a, b in edges:
        graph[a].append(b)
        graph[b].append(a)
    disc = [-1] * n
    low = [0] * n
    cut = set()
    timer = [0]

    def dfs(u, parent):
        disc[u] = low[u] = timer[0]
        timer[0] += 1
        children = 0
        for v in graph[u]:
            if v == parent:
                continue
            if disc[v] == -1:
                children += 1
                dfs(v, u)
                low[u] = min(low[u], low[v])
                if parent != -1 and low[v] >= disc[u]:
                    cut.add(u)
            else:
                low[u] = min(low[u], disc[v])
        if parent == -1 and children >= 2:
            cut.add(u)

    for start in range(n):
        if disc[start] == -1:
            dfs(start, -1)
    return sorted(cut)

# edges [[0,1],[1,2],[2,0],[1,3],[3,4]]  =>  [1, 3]
```

| | Bridge `u–v` | Articulation point `u` | SCC root `v` |
|---|---|---|---|
| Graph | undirected | undirected | directed |
| Test | `low[v] > disc[u]` | `low[v] >= disc[u]` (root: ≥ 2 children) | `low[v] == disc[v]` |

## Edge-Covering Walks

### Template 2: Euler Path / Circuit (Hierholzer) — LC 332 ⭐⭐⭐⭐

**Recognise** — "use every ticket / every edge exactly once", "reconstruct the itinerary". The
graph has a path that uses each **edge** once (not each node). E ≤ 300 on LC 332, but plain
backtracking over edge orders is exponential in the worst case; Hierholzer is linear.

**Key point** — walk forward consuming edges until you are stuck; the node you are stuck at can
only be the **end** of what remains, so it is appended to the route then, in post-order. Reversing
the post-order gives the walk. Edges are consumed, nodes are never marked: an airport is
legitimately revisited.

| Graph | Euler circuit exists iff | Euler path exists iff |
|---|---|---|
| Undirected | every degree is even | 0 or 2 vertices of odd degree |
| Directed | `in == out` everywhere | one `out − in = 1` (start), one `in − out = 1` (end), rest equal |

```python
# python
# LC 332 - Reconstruct Itinerary
# IDEA: Hierholzer; walk consuming edges, append a node when it is stuck, reverse at the end
# time = O(E log E), space = O(E)
from collections import defaultdict

def findItinerary(tickets):
    graph = defaultdict(list)
    # sort descending so list.pop() from the tail yields the smallest airport first
    for src, dst in sorted(tickets, reverse=True):
        graph[src].append(dst)

    route = []
    stack = ["JFK"]
    while stack:
        airport = stack[-1]
        if graph[airport]:
            stack.append(graph[airport].pop())   # consume one ticket, walk forward
        else:
            route.append(stack.pop())            # stuck: this airport is final, post-order
    return route[::-1]
```

```java
// java
// LC 332 - Reconstruct Itinerary
// IDEA: Hierholzer; walk consuming edges, append a node when it is stuck, reverse at the end
// time = O(E log E), space = O(E)
public List<String> findItinerary(List<List<String>> tickets) {
    Map<String, PriorityQueue<String>> graph = new HashMap<>();
    for (List<String> t : tickets) {
        graph.computeIfAbsent(t.get(0), k -> new PriorityQueue<>()).add(t.get(1));
    }
    LinkedList<String> route = new LinkedList<>();
    Deque<String> stack = new ArrayDeque<>();
    stack.push("JFK");
    while (!stack.isEmpty()) {
        PriorityQueue<String> next = graph.get(stack.peek());
        if (next != null && !next.isEmpty()) {
            stack.push(next.poll());          // consume the smallest ticket
        } else {
            route.addFirst(stack.pop());      // post-order, reversed as it is built
        }
    }
    return route;
}
```

**Complexity**
- **Time O(E log E)**: the `sorted(tickets, ...)` call (the heap in Java); after that every
  ticket is pushed once and popped once by the `while stack` loop.
- **Space O(E)**: the adjacency lists, the stack and the route each hold at most E + 1 entries.

**Dry run** — tickets `JFK→SFO, JFK→ATL, SFO→ATL, ATL→JFK, ATL→SFO`:

```text
stack                        action
[JFK]                        take ATL (smallest)
[JFK ATL]                    take JFK
[JFK ATL JFK]                take SFO
[JFK ATL JFK SFO]            take ATL
[JFK ATL JFK SFO ATL]        take SFO
[JFK ATL JFK SFO ATL SFO]    SFO stuck -> route [SFO]; then ATL, SFO, JFK, ATL, JFK pop in turn
route reversed: JFK ATL JFK SFO ATL SFO
```

**Traps**
- A `visited` set of airports breaks it: `JFK → ATL → JFK` revisits JFK on purpose.
- Greedy "always take the smallest" without post-order fails on `JFK→KUL, JFK→NRT, NRT→JFK`:
  KUL is a dead end, so it must come **last**. Post-order puts it there.
- The route is built backwards; forgetting the final reverse (or `addFirst`) is the classic bug.

**Not this if** — every **node** must be visited once (Hamiltonian path, LC 847): that is
NP-hard in general and solved with bitmask BFS / DP on small n, not Hierholzer.

**Drill** — LC 332 ⭐ · LC 753 *(de Bruijn graph, below)* · LC 2097 *(valid arrangement of
pairs: find the start from the degree imbalance first)*

### Cracking the Safe — LC 753

> **Key point** — a node is the last `n − 1` digits typed, and each of the `k` digits is an edge
> to the next node, so the `k^n` edges are exactly the `k^n` passwords. Every node has
> `in = out = k`, so an Euler circuit exists, and walking it types every password exactly once:
> the shortest answer has length `k^n + n − 1`.

**Reduces to**: [Template 2](#template-2-euler-path--circuit-hierholzer--lc-332-) — Hierholzer
on an implicit graph, where an edge is consumed by taking the node's next unused digit.

```python
# python
# LC 753 - Cracking the Safe
# IDEA: Hierholzer on the de Bruijn graph; node = last n-1 digits, edge = one appended digit
# time = O(n * k^n), space = O(n * k^n)
def crackSafe(n, k):
    start = "0" * (n - 1)
    next_digit = {}               # node -> the next digit (edge) it has not used yet
    stack = [(start, "")]         # (node, digit of the edge that led here)
    digits = []
    while stack:
        node, came_by = stack[-1]
        d = next_digit.get(node, 0)
        if d < k:
            next_digit[node] = d + 1                   # consume edge `d` out of `node`
            stack.append(((node + str(d))[1:], str(d)))
        else:
            stack.pop()                                # stuck: emit in post-order
            digits.append(came_by)
    return start + "".join(reversed(digits))
```

**Traps**
- The recursive form (`dfs(edge[1:])` inside the digit loop) recurses once per password, up to
  4,096 deep, past Python's default limit of 1,000. The explicit stack above has no limit.
- Do not forget the `start` prefix: the walk emits edges (digits), and the first password needs
  `n − 1` digits typed before it.

## Matching, Flow and Colouring

### Template 3: Parity Union-Find (Bipartite Check) — LC 886

**Recognise** — "split into two groups so that each dislike pair is apart", where the pairs are
given as a list or arrive one at a time. n ≤ 2,000, pairs ≤ 10⁴.

**Key point** — give every person `x` a twin `x + n` meaning "the other group". A pair `(a, b)`
says `a` sits with `b`'s twin and `b` with `a`'s twin. The answer is impossible the moment a
pair's two people already share a set: some chain of "opposite" constraints with an even length
put them together, so this pair closes an odd cycle.

```python
# python
# LC 886 - Possible Bipartition
# IDEA: parity DSU; union a with twin(b) and b with twin(a); a and b already joined -> odd cycle
# time = O((n + m) * α(n)), space = O(n)
def possibleBipartition(n, dislikes):
    parent = list(range(2 * (n + 1)))      # people 1..n, twins n+1..2n+1

    def find(x):
        while parent[x] != x:
            parent[x] = parent[parent[x]]  # path halving
            x = parent[x]
        return x

    def union(x, y):
        parent[find(x)] = find(y)

    for a, b in dislikes:
        if find(a) == find(b):             # forced into the same group already
            return False
        union(a, b + n + 1)                # a joins b's opposite
        union(b, a + n + 1)                # b joins a's opposite
    return True
```

```java
// java
// LC 886 - Possible Bipartition
// IDEA: parity DSU; union a with twin(b) and b with twin(a); a and b already joined -> odd cycle
// time = O((n + m) * α(n)), space = O(n)
class Solution {
    private int[] parent;

    public boolean possibleBipartition(int n, int[][] dislikes) {
        parent = new int[2 * (n + 1)];                    // people 1..n, twins n+1..2n+1
        for (int i = 0; i < parent.length; i++) parent[i] = i;
        for (int[] d : dislikes) {
            int a = d[0], b = d[1];
            if (find(a) == find(b)) return false;         // forced into the same group already
            union(a, b + n + 1);                          // a joins b's opposite
            union(b, a + n + 1);                          // b joins a's opposite
        }
        return true;
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];                // path halving
            x = parent[x];
        }
        return x;
    }

    private void union(int x, int y) {
        parent[find(x)] = find(y);
    }
}
```

**Complexity**
- **Time O((n + m)·α(n))**: each of the m pairs does two `find`s and two `union`s; path halving
  keeps each near constant.
- **Space O(n)**: the `parent` array of 2(n + 1) entries.

**Traps**
- The check must come **before** the unions of the same pair; checking after always finds
  `a` and `b`'s twin together and never the conflict.
- People are 1-indexed on LC 886: size the array `2 * (n + 1)`, not `2 * n`.

**Not this if** — every pair is known before you answer: BFS 2-colouring
([graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)) is the
answer an interviewer expects, and it can also return the two groups. The full BFS solution of
LC 886 is in [graph_examples.md](./graph_examples.md).

**Drill** — LC 886 ⭐ · LC 785 · LC 990 *(equalities first, then inequalities: plain DSU, no
twins)*

### Template 4: Bipartite Matching by Augmenting Paths (Kuhn) — LC 1820

**Recognise** — "the maximum number of pairs", "each boy invites at most one girl and each girl
accepts at most one". Two sides, an allowed-pair matrix, each side ≤ 200.

**Key point** — try to match each left vertex in turn. Left vertex `u` gets right vertex `v` if
`v` is free, or if `v`'s current partner can be **moved** to another right vertex — a recursive
attempt that is an augmenting path. A matching is maximum exactly when no augmenting path is
left, so one attempt per left vertex is enough.

```python
# python
# LC 1820 - Maximum Number of Accepted Invitations
# IDEA: Kuhn; for each boy, DFS for an augmenting path (free girl, or re-seat her partner)
# time = O(m * m * n), space = O(n)
def maximumInvitations(grid):
    m, n = len(grid), len(grid[0])
    partner = [-1] * n                     # partner[girl] = boy, or -1

    def try_match(boy, tried):
        for girl in range(n):
            if grid[boy][girl] and not tried[girl]:
                tried[girl] = True         # each girl is tried once per augmenting search
                if partner[girl] == -1 or try_match(partner[girl], tried):
                    partner[girl] = boy
                    return True
        return False

    matched = 0
    for boy in range(m):
        if try_match(boy, [False] * n):
            matched += 1
    return matched
```

```java
// java
// LC 1820 - Maximum Number of Accepted Invitations
// IDEA: Kuhn; for each boy, DFS for an augmenting path (free girl, or re-seat her partner)
// time = O(m * m * n), space = O(n)
public int maximumInvitations(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] partner = new int[n];
    Arrays.fill(partner, -1);
    int matched = 0;
    for (int boy = 0; boy < m; boy++) {
        if (tryMatch(grid, boy, new boolean[n], partner)) {
            matched++;
        }
    }
    return matched;
}

private boolean tryMatch(int[][] grid, int boy, boolean[] tried, int[] partner) {
    for (int girl = 0; girl < grid[0].length; girl++) {
        if (grid[boy][girl] == 1 && !tried[girl]) {
            tried[girl] = true;
            if (partner[girl] == -1 || tryMatch(grid, partner[girl], tried, partner)) {
                partner[girl] = boy;
                return true;
            }
        }
    }
    return false;
}
```

**Complexity**
- **Time O(m·m·n)**: m calls of `try_match` from the outer loop; inside one, `tried` lets each
  girl be entered once, and each entry scans her boy's row of n cells — O(m·n) per search.
- **Space O(n)**: `partner` and `tried`, plus a recursion stack at most m deep.

**Traps**
- `tried` must be **fresh for each boy**. Reusing it across boys blocks girls that a later
  augmenting path needs; not having it at all loops forever on a cycle of re-seatings.
- Greedy "give each boy his first free girl" is not maximum: `[[1,1],[1,0]]` greedy gives boy 0
  girl 0 and leaves boy 1 alone; Kuhn re-seats boy 0 on girl 1.

**Not this if** — the pairs have **costs** and you want the cheapest perfect assignment
(LC 1066, LC 1947): that is DP over bitmasks for n ≤ 10, not unweighted matching.

**Drill** — LC 1820 ⭐ · LC 1349 *(max independent set on a bipartite seat graph = seats −
matching)* · LC 2123 *(minimum operations, König's theorem)*

#### Generalisation: max flow / min cut (Edmonds-Karp)

Matching is max flow with every capacity 1: source → each left vertex → allowed pairs → each
right vertex → sink. General capacities need Edmonds-Karp: BFS for the shortest augmenting path
in the residual graph, push its bottleneck, and add the reverse capacity so a later path can
undo it. By the max-flow min-cut theorem, the flow value is also the cheapest cut.

```python
# python
# Edmonds-Karp max flow on a capacity matrix
# IDEA: BFS the residual graph for a shortest augmenting path; push its bottleneck; repeat
# time = O(V * E^2), space = O(V^2)
from collections import deque

def max_flow(capacity, source, sink):
    n = len(capacity)
    residual = [row[:] for row in capacity]
    flow = 0
    while True:
        parent = [-1] * n
        parent[source] = source
        queue = deque([source])
        while queue and parent[sink] == -1:
            u = queue.popleft()
            for v in range(n):
                if parent[v] == -1 and residual[u][v] > 0:
                    parent[v] = u
                    queue.append(v)
        if parent[sink] == -1:                 # no augmenting path left
            return flow
        bottleneck = float('inf')
        v = sink
        while v != source:
            bottleneck = min(bottleneck, residual[parent[v]][v])
            v = parent[v]
        v = sink
        while v != source:
            residual[parent[v]][v] -= bottleneck
            residual[v][parent[v]] += bottleneck   # lets a later path cancel this push
            v = parent[v]
        flow += bottleneck
```

### Template 5: Greedy k-Colouring — LC 1042

**Recognise** — "assign one of k colours / types so that no two connected nodes share one",
with a stated bound "every node has at most k − 1 neighbours". LC 1042: degree ≤ 3, 4 types.

**Key point** — a node with fewer than k neighbours always has a free colour, whatever its
neighbours already hold. So colouring in any order, each node taking the first colour no
neighbour uses, never gets stuck: no search, no backtracking.

```python
# python
# LC 1042 - Flower Planting With No Adjacent
# IDEA: degree <= 3 and 4 colours, so a free colour always exists; take the first one
# time = O(V + E), space = O(V + E)
def gardenNoAdj(n, paths):
    graph = [[] for _ in range(n + 1)]
    for a, b in paths:
        graph[a].append(b)
        graph[b].append(a)

    colour = [0] * (n + 1)                 # 0 = not coloured yet; gardens are 1..n
    for garden in range(1, n + 1):
        used = set()
        for neighbour in graph[garden]:
            used.add(colour[neighbour])
        for c in (1, 2, 3, 4):
            if c not in used:
                colour[garden] = c
                break
    return colour[1:]
```

```java
// java
// LC 1042 - Flower Planting With No Adjacent
// IDEA: degree <= 3 and 4 colours, so a free colour always exists; take the first one
// time = O(V + E), space = O(V + E)
public int[] gardenNoAdj(int n, int[][] paths) {
    List<List<Integer>> graph = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
        graph.add(new ArrayList<>());
    }
    for (int[] p : paths) {
        graph.get(p[0]).add(p[1]);
        graph.get(p[1]).add(p[0]);
    }
    int[] colour = new int[n];               // colour[g - 1]; 0 = not coloured yet
    for (int garden = 1; garden <= n; garden++) {
        boolean[] used = new boolean[5];
        for (int neighbour : graph.get(garden)) {
            used[colour[neighbour - 1]] = true;
        }
        for (int c = 1; c <= 4; c++) {
            if (!used[c]) {
                colour[garden - 1] = c;
                break;
            }
        }
    }
    return colour;
}
```

**Complexity**
- **Time O(V + E)**: each garden reads its neighbour list once in `for neighbour in
  graph[garden]`; the colour scan is at most 4 steps.
- **Space O(V + E)**: the adjacency list.

**Traps**
- Gardens are 1-indexed; the answer list is 0-indexed. Off by one here returns a colouring of
  the wrong garden.
- Without the degree guarantee greedy can fail (a 4-cycle coloured in a bad order with 2
  colours); then it is backtracking, or the bipartite check when k = 2.

**Not this if** — k = 2: a colour choice now **forces** each neighbour, which is BFS
2-colouring ([graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)).

**Drill** — LC 1042 ⭐ · LC 785 *(k = 2: propagation, not greedy)*

## When Not to Use

| Technique | What usually suffices instead | When the simpler card is enough |
|---|---|---|
| Tarjan bridges / cut vertices | remove the edge or vertex and re-run a traversal ([graph.md § Template 1](./graph.md#template-1-connected-components-by-traversal--lc-547-)) | one or two candidate removals, or a grid where the answer is at most 2 (LC 1568) |
| Tarjan / Kosaraju SCC | three-state DFS ([graph.md § Template 5](./graph.md#template-5-three-state-dfs-cycle-detection--lc-802-)) or Kahn ([graph.md § Template 4](./graph.md#template-4-topological-sort-kahns-bfs--lc-207-)) | the question is only "is there a cycle" or "which nodes are on no cycle", not which nodes share one |
| Hierholzer | DFS + backtracking over edges | E is tiny and the interviewer wants any valid order, not the lexicographically smallest |
| Parity union-find | BFS 2-colouring ([graph.md § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-)) | all pairs are given up front, which is every LeetCode bipartite problem |
| Kuhn matching / max flow | greedy, or DP over bitmasks | one side has ≤ 10 members (bitmask DP), or the structure forces a unique choice |
| Greedy k-colouring | backtracking colouring | no degree bound is given: greedy is then not guaranteed to finish |

## Summary

| Algorithm | Time | Space | Note |
|---|---|---|---|
| Tarjan (bridges / cut vertices / SCC) | O(V + E) | O(V + E) | one DFS, `disc[]` + `low[]` |
| Kosaraju SCC | O(V + E) | O(V + E) | two DFS passes, needs the reversed graph |
| Hierholzer | O(E log E) | O(E) | O(E) when the edge order does not matter |
| Parity union-find | O((n + m)·α(n)) | O(n) | `2n` nodes: `x` and its twin |
| Kuhn matching | O(V·E) | O(V) | one augmenting search per left vertex |
| Edmonds-Karp | O(V·E²) | O(V²) | min cut = max flow |
| Greedy k-colouring | O(V + E) | O(V + E) | only when every degree < k |

**Pitfalls unique to this sheet**
- Tarjan reads `disc[v]` on a back edge; the bridge test is `>`, the cut-vertex test `>=`.
- Hierholzer consumes **edges** and never marks nodes; the route comes out reversed.
- Kuhn needs a fresh `tried` array for each left vertex.
- Deep recursion: Python needs `setrecursionlimit` or an explicit stack for Templates 1 and 2.

### Related Topics
- **[graph.md](./graph.md)**: representation, traversal, cycle detection and 2-colouring — read that first
- **[union_find.md](./union_find.md)**: the DSU primitives Template 3 doubles
- **[topology_sorting.md](./topology_sorting.md)**: condensing SCCs yields a DAG to sort
- **[minimum_spanning_tree.md](./minimum_spanning_tree.md)**: Kruskal (sorted edges + union-find)
  and Prim (heap or dense array). LC 1135, LC 1584.
