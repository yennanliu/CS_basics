# Advanced Graph Algorithms

> **Scope** — The graph techniques a first interview pass should skip: Tarjan's low-link family (strongly connected components, bridges, articulation points), Euler paths and circuits, max flow / min cut, and the bipartite extras — Union-Find detection, maximum matching and greedy k-colouring.
> **See also**: [graph.md](./graph.md) — representation, traversal, connectivity and cycle detection, plus the chooser table that routes here; [graph_examples.md](./graph_examples.md) — the worked-solution archive; [dfs_advanced.md](./dfs_advanced.md) — the DFS sheet's own Hierholzer and Tarjan-bridge templates; [union_find.md](./union_find.md) — the DSU primitives these templates build on.

## LeetCode Problem Lists

- [Graph Theory](https://leetcode.com/problem-list/graph/)
- [Strongly Connected Component](https://leetcode.com/problem-list/strongly-connected-component/)
- [Eulerian Circuit](https://leetcode.com/problem-list/eulerian-circuit/)
- [Biconnected Component](https://leetcode.com/problem-list/biconnected-component/)

## Overview

Everything here is a **low-link DFS**, an **edge-consuming DFS**, or an **augmenting-path
BFS**. They share one shape: a plain traversal plus one extra array — `low[]`, a consumed
adjacency list, or a residual-capacity matrix — that turns the traversal into a proof.

### Key Properties
- **Complexity**: see the [Complexity Quick Reference](#complexity-quick-reference) table in the summary
- **Core Idea**: one DFS/BFS pass carrying enough bookkeeping to answer a structural question
- **When to Use**: the problem asks which *edge* or *vertex* is critical, asks you to use
  every edge exactly once, or asks for a capacity / matching bound

## Problem Categories

### **Category 1: Critical Structure (Low-Link)**
- **Description**: which vertices or edges hold the graph together
- **Examples**: LC 1192 (Critical Connections), LC 1568 (Minimum Days to Disconnect Island)
- **Pattern**: Tarjan `disc[]` / `low[]` DFS

### **Category 2: Strong Connectivity**
- **Description**: maximal mutually-reachable sets in a directed graph
- **Examples**: LC 685 (Redundant Connection II), LC 1557 (Minimum Vertices to Reach All Nodes)
- **Pattern**: Tarjan SCC or Kosaraju two-pass

### **Category 3: Edge-Covering Walks**
- **Description**: use every edge exactly once
- **Examples**: LC 332 (Reconstruct Itinerary), LC 753 (Cracking the Safe)
- **Pattern**: Hierholzer — post-order emission, then reverse

### **Category 4: Flow and Matching**
- **Description**: capacity bounds, assignment, minimum cut
- **Examples**: LC 1349 (Maximum Students Taking Exam), LC 1595 (Minimum Cost to Connect Two Groups)
- **Pattern**: Ford-Fulkerson / Edmonds-Karp, Kuhn's matching

### **Category 5: Colouring Beyond Two Colours**
- **Description**: assign `k` labels with no adjacent clash
- **Examples**: LC 1042 (Flower Planting With No Adjacent)
- **Pattern**: greedy when `k > max_degree`, otherwise backtracking

## Templates & Algorithms

### Template Comparison Table

| Template | Question it answers | Time | Graph |
|---|---|---|---|
| Tarjan SCC | which vertices are mutually reachable? | O(V + E) | directed |
| Tarjan bridges | which edge is critical? | O(V + E) | undirected |
| Tarjan articulation points | which vertex is critical? | O(V + E) | undirected |
| Hierholzer | can I walk every edge exactly once? | O(E) | either |
| Union-Find bipartite | is there an odd cycle? | O(E·α(V)) | undirected |
| Kuhn's matching | largest pairing between two sides? | O(V·E) | bipartite |
| Edmonds-Karp | maximum flow / minimum cut? | O(V·E²) | directed, weighted |
| Greedy k-colouring | `k` labels, no adjacent clash? | O(V + E) | undirected, `k > max_deg` |

### Template 1: Tarjan's Low-Link DFS — SCC, Bridges, Articulation Points — LC 1192 ⭐⭐⭐⭐

**Overview:**
Tarjan's algorithm is a DFS-based technique for finding critical graph structures:
1. **Strongly Connected Components (SCC)** - Maximal sets of mutually reachable vertices (directed graphs)
2. **Bridges** - Edges whose removal disconnects the graph (undirected graphs)
3. **Articulation Points (Cut Vertices)** - Vertices whose removal disconnects the graph (undirected graphs)

**Core Concept:**
Uses DFS with two key arrays:
- `disc[v]`: Discovery time of vertex v (when first visited)
- `low[v]`: Lowest discovery time reachable from v's subtree

**Time Complexity**: O(V + E) - single DFS traversal
**Space Complexity**: O(V) - recursion stack + arrays

---

#### 1.1) Strongly Connected Components (SCC)

**Definition**: In a directed graph, an SCC is a maximal set of vertices where every vertex is reachable from every other vertex in the set.

**Key Insight:**
- Use a stack to track vertices in current DFS path
- When `low[v] == disc[v]`, v is the root of an SCC
- Pop all vertices from stack until v to get complete SCC

**Algorithm Steps:**
1. Initialize `disc[]`, `low[]`, and stack
2. DFS from each unvisited vertex
3. For each vertex v:
   - Set `disc[v] = low[v] = timer++`
   - Push v onto stack
   - For each neighbor u:
     - If unvisited: DFS(u), update `low[v] = min(low[v], low[u])`
     - If u on stack: update `low[v] = min(low[v], disc[u])`
   - If `low[v] == disc[v]`: pop stack until v to form SCC

##### Python Implementation

```python
# Tarjan's Algorithm for SCC
def tarjan_scc(n, graph):
    """
    Find all strongly connected components using Tarjan's algorithm.

    Args:
        n: number of vertices (0 to n-1)
        graph: adjacency list (directed graph)

    Returns:
        List of SCCs, where each SCC is a list of vertices

    Time: O(V + E)
    Space: O(V)
    """
    disc = [-1] * n  # Discovery times
    low = [-1] * n   # Lowest reachable
    on_stack = [False] * n
    stack = []
    sccs = []
    timer = [0]  # Use list for mutability

    def dfs(v):
        # Initialize discovery time and low value
        disc[v] = low[v] = timer[0]
        timer[0] += 1
        stack.append(v)
        on_stack[v] = True

        # Explore neighbors
        for u in graph[v]:
            if disc[u] == -1:
                # Unvisited neighbor
                dfs(u)
                low[v] = min(low[v], low[u])
            elif on_stack[u]:
                # Back edge to vertex on stack
                low[v] = min(low[v], disc[u])

        # If v is a root of SCC, pop the SCC
        if low[v] == disc[v]:
            scc = []
            while True:
                u = stack.pop()
                on_stack[u] = False
                scc.append(u)
                if u == v:
                    break
            sccs.append(scc)

    # Run DFS from each unvisited vertex
    for i in range(n):
        if disc[i] == -1:
            dfs(i)

    return sccs

# Example:
# graph = {0: [1], 1: [2], 2: [0, 3], 3: [4], 4: [5], 5: [3]}
#    0 → 1 → 2
#    ↑       ↓
#    └───────┘    3 ⇄ 4 → 5
#                     ↑   ↓
#                     └───┘
# SCCs: [[0, 2, 1], [3, 5, 4]]
```

##### Java Implementation

```java
// Tarjan's SCC Algorithm
/**
 * LC 1192 - Critical Connections in a Network (related)
 *
 * time = O(V + E)
 * space = O(V)
 */
class TarjanSCC {
    private int timer = 0;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccs;

    public List<List<Integer>> findSCCs(int n, List<List<Integer>> graph) {
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccs = new ArrayList<>();

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        // DFS from each unvisited vertex
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                dfs(i, graph);
            }
        }

        return sccs;
    }

    private void dfs(int v, List<List<Integer>> graph) {
        // Initialize
        disc[v] = low[v] = timer++;
        stack.push(v);
        onStack[v] = true;

        // Explore neighbors
        for (int u : graph.get(v)) {
            if (disc[u] == -1) {
                // Unvisited
                dfs(u, graph);
                low[v] = Math.min(low[v], low[u]);
            } else if (onStack[u]) {
                // Back edge
                low[v] = Math.min(low[v], disc[u]);
            }
        }

        // Root of SCC found
        if (low[v] == disc[v]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int u = stack.pop();
                onStack[u] = false;
                scc.add(u);
                if (u == v) break;
            }
            sccs.add(scc);
        }
    }
}
```

---

#### 1.2) Finding Bridges (Critical Connections)

**Definition**: A bridge is an edge whose removal increases the number of connected components (disconnects the graph).

**Key Insight:**
- Edge (u, v) is a bridge if `low[v] > disc[u]`
- Means v cannot reach any vertex discovered before u without using edge (u, v)

**Algorithm Steps:**
1. Run DFS with `disc[]` and `low[]`
2. For each edge (u, v) in DFS tree:
   - If `low[v] > disc[u]`: (u, v) is a bridge

##### Python Implementation

```python
# Tarjan's Algorithm for Bridges
def find_bridges(n, edges):
    """
    Find all bridges (critical connections) in an undirected graph.

    Args:
        n: number of vertices
        edges: list of [u, v] edges

    Returns:
        List of bridges (critical edges)

    Time: O(V + E)
    Space: O(V + E)
    """
    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)

    disc = [-1] * n
    low = [-1] * n
    bridges = []
    timer = [0]

    def dfs(v, parent):
        disc[v] = low[v] = timer[0]
        timer[0] += 1

        for u in graph[v]:
            if u == parent:
                # Skip edge to parent (undirected graph)
                continue

            if disc[u] == -1:
                # Unvisited neighbor
                dfs(u, v)
                low[v] = min(low[v], low[u])

                # Check if (v, u) is a bridge
                if low[u] > disc[v]:
                    bridges.append([v, u])
            else:
                # Back edge
                low[v] = min(low[v], disc[u])

    # Run DFS from each component
    for i in range(n):
        if disc[i] == -1:
            dfs(i, -1)

    return bridges

# Example:
# n = 4, edges = [[0,1],[1,2],[2,0],[1,3]]
#
#    0 --- 1 --- 3
#     \   /
#      \ /
#       2
#
# Bridge: [1, 3] (removing this disconnects 3 from rest)
```

##### Java Implementation

```java
// LC 1192 - Critical Connections in a Network
/**
 * time = O(V + E)
 * space = O(V + E)
 */
class Solution {
    private int timer = 0;
    private int[] disc;
    private int[] low;
    private List<List<Integer>> bridges;

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (List<Integer> conn : connections) {
            int u = conn.get(0);
            int v = conn.get(1);
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        disc = new int[n];
        low = new int[n];
        bridges = new ArrayList<>();
        Arrays.fill(disc, -1);

        // DFS from vertex 0 (graph is connected in this problem)
        dfs(0, -1, graph);

        return bridges;
    }

    private void dfs(int v, int parent, List<List<Integer>> graph) {
        disc[v] = low[v] = timer++;

        for (int u : graph.get(v)) {
            if (u == parent) continue;  // Skip parent edge

            if (disc[u] == -1) {
                // Unvisited
                dfs(u, v, graph);
                low[v] = Math.min(low[v], low[u]);

                // Check for bridge
                if (low[u] > disc[v]) {
                    bridges.add(Arrays.asList(v, u));
                }
            } else {
                // Back edge
                low[v] = Math.min(low[v], disc[u]);
            }
        }
    }
}
```

---

#### 1.3) Finding Articulation Points (Cut Vertices)

**Definition**: An articulation point is a vertex whose removal increases the number of connected components.

**Key Insight:**
- Vertex u is an articulation point if:
  - **Root of DFS tree**: has 2+ children
  - **Non-root**: has a child v where `low[v] >= disc[u]`

**Algorithm Steps:**
1. Run DFS with `disc[]` and `low[]`
2. For each vertex u:
   - If root: count children, articulation point if ≥ 2
   - If non-root: check if any child v has `low[v] >= disc[u]`

##### Python Implementation

```python
# Tarjan's Algorithm for Articulation Points
def find_articulation_points(n, edges):
    """
    Find all articulation points (cut vertices).

    Args:
        n: number of vertices
        edges: list of [u, v] edges

    Returns:
        Set of articulation points

    Time: O(V + E)
    Space: O(V + E)
    """
    # Build adjacency list
    graph = [[] for _ in range(n)]
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)

    disc = [-1] * n
    low = [-1] * n
    ap = set()  # Articulation points
    timer = [0]

    def dfs(v, parent):
        children = 0
        disc[v] = low[v] = timer[0]
        timer[0] += 1

        for u in graph[v]:
            if u == parent:
                continue

            if disc[u] == -1:
                # Unvisited child
                children += 1
                dfs(u, v)
                low[v] = min(low[v], low[u])

                # Check if v is articulation point
                # Case 1: Root with 2+ children
                if parent == -1 and children > 1:
                    ap.add(v)

                # Case 2: Non-root with child that can't reach ancestor
                if parent != -1 and low[u] >= disc[v]:
                    ap.add(v)
            else:
                # Back edge
                low[v] = min(low[v], disc[u])

    # Run DFS from each component
    for i in range(n):
        if disc[i] == -1:
            dfs(i, -1)

    return list(ap)

# Example:
# n = 5, edges = [[0,1],[1,2],[2,0],[1,3],[3,4]]
#
#    0 --- 1 --- 3 --- 4
#     \   /
#      \ /
#       2
#
# Articulation points: [1, 3]
# (Removing 1 disconnects {0,2} from {3,4})
# (Removing 3 disconnects 4 from rest)
```

##### Java Implementation

```java
// Articulation Points Algorithm
/**
 * time = O(V + E)
 * space = O(V + E)
 */
class ArticulationPoints {
    private int timer = 0;
    private int[] disc;
    private int[] low;
    private Set<Integer> ap;

    public List<Integer> findArticulationPoints(int n, int[][] edges) {
        // Build adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        disc = new int[n];
        low = new int[n];
        ap = new HashSet<>();
        Arrays.fill(disc, -1);

        // DFS from each component
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) {
                dfs(i, -1, graph);
            }
        }

        return new ArrayList<>(ap);
    }

    private void dfs(int v, int parent, List<List<Integer>> graph) {
        int children = 0;
        disc[v] = low[v] = timer++;

        for (int u : graph.get(v)) {
            if (u == parent) continue;

            if (disc[u] == -1) {
                children++;
                dfs(u, v, graph);
                low[v] = Math.min(low[v], low[u]);

                // Root with 2+ children
                if (parent == -1 && children > 1) {
                    ap.add(v);
                }

                // Non-root with blocking child
                if (parent != -1 && low[u] >= disc[v]) {
                    ap.add(v);
                }
            } else {
                // Back edge
                low[v] = Math.min(low[v], disc[u]);
            }
        }
    }
}
```

---

#### 1.4) Visual Example: Tarjan's Algorithm Walkthrough

```text
Graph (Directed):
    0 → 1 → 2
    ↑       ↓
    └───────┘     3 ⇄ 4
                  ↓   ↑
                  5 ──┘

DFS Traversal:

Step 1: Start at 0
  disc[0] = low[0] = 0
  stack = [0]

Step 2: Visit 1 from 0
  disc[1] = low[1] = 1
  stack = [0, 1]

Step 3: Visit 2 from 1
  disc[2] = low[2] = 2
  stack = [0, 1, 2]

Step 4: Back edge 2→0 (0 already on stack)
  low[2] = min(2, disc[0]) = 0
  Backtrack to 1: low[1] = min(1, low[2]) = 0
  Backtrack to 0: low[0] = min(0, low[1]) = 0

Step 5: At 0, low[0] == disc[0] → SCC found!
  Pop stack: [2, 1, 0]
  SCC #1: {0, 1, 2}

Step 6: Start at 3
  disc[3] = low[3] = 3
  stack = [3]

Step 7: Visit 4 from 3
  disc[4] = low[4] = 4
  stack = [3, 4]

Step 8: Visit 5 from 4
  disc[5] = low[5] = 5
  stack = [3, 4, 5]

Step 9: Edge 5→3 (back edge)
  low[5] = min(5, disc[3]) = 3
  Backtrack to 4: low[4] = min(4, low[5]) = 3
  Backtrack to 3: low[3] = min(3, low[4]) = 3

Step 10: At 3, low[3] == disc[3] → SCC found!
  Pop stack: [5, 4, 3]
  SCC #2: {3, 4, 5}

Final SCCs: [{0,1,2}, {3,4,5}]
```

---

#### 1.5) Classic LeetCode Problems

| Problem | LC# | Variant | Difficulty | Key Insight |
|---------|-----|---------|------------|-------------|
| **Critical Connections in Network** | **1192** | **Bridges** | **Hard** | Find all bridges using Tarjan |
| Number of Provinces | 547 | Basic connectivity | Medium | Count connected components |
| Redundant Connection | 684 | Cycle detection | Medium | Find edge creating cycle |
| Redundant Connection II | 685 | Directed graph | Hard | SCC + cycle in directed graph |
| Minimum Number of Vertices | 1557 | SCC sources | Medium | Find vertices with no incoming |

---

#### 1.6) Comparison: Tarjan vs Kosaraju for SCC

| Aspect | Tarjan's Algorithm | Kosaraju's Algorithm |
|--------|-------------------|---------------------|
| **Passes** | Single DFS | Two DFS passes |
| **Time** | O(V + E) | O(V + E) |
| **Space** | O(V) stack | O(V) + transpose graph |
| **Complexity** | More complex (one pass) | Simpler (two passes) |
| **Extra Space** | Stack for SCC | Reversed graph |
| **Preference** | More efficient (one pass) | Easier to understand |

---

#### 1.7) Interview Tips

**1. Recognition Patterns:**
```text
"critical connections" → Bridges (Tarjan)
"strongly connected" → SCC (Tarjan or Kosaraju)
"cut vertices" → Articulation points (Tarjan)
"remove vertex/edge disconnects graph" → Articulation/Bridge
```

**2. Key Differences:**
```text
SCC: Directed graph, maximal mutually reachable sets
Bridges: Undirected graph, critical edges
Articulation Points: Undirected graph, critical vertices

low[v] == disc[v] → Root of SCC (directed)
low[v] > disc[u] → (u,v) is bridge (undirected)
low[v] >= disc[u] → u is articulation point (undirected)
```

**3. Common Mistakes:**
- Forgetting to skip parent edge in undirected graphs
- Wrong condition for articulation point (root vs non-root)
- Not using `on_stack` array for SCC (leads to incorrect SCCs)
- Confusing `disc[u]` vs `low[u]` in back edge updates

**4. Template to Memorize:**
```python
def tarjan_template(v, parent=-1):
    disc[v] = low[v] = timer
    timer += 1

    for u in graph[v]:
        if u == parent:  # Undirected graphs only
            continue

        if disc[u] == -1:
            # Tree edge
            dfs(u, v)
            low[v] = min(low[v], low[u])
            # Check condition here (bridge, AP, etc.)
        else:
            # Back edge
            low[v] = min(low[v], disc[u])  # Or check on_stack for SCC
```

**5. Talking Points:**
- "Tarjan's uses single DFS with discovery times"
- "low[v] tracks earliest reachable vertex from v's subtree"
- "Bridges/APs indicate critical graph structure"
- "SCCs represent maximal strongly connected regions"

---

#### 1.8) Articulation Point vs Bridge — the one-line difference

| | Articulation Point | Bridge |
|--|-------------------|--------|
| What | Vertex whose removal disconnects graph | Edge whose removal disconnects graph |
| Condition | `low[v] >= disc[u]` (for non-root) | `low[v] > disc[u]` |
| LC | 1192 (Critical Connections = bridges) | 1192 |

---

### Template 2: Euler Path / Circuit (Hierholzer) — LC 753 ⭐⭐⭐

**Key Idea**: An **Euler circuit** uses every *edge* exactly once (contrast: Hamiltonian path uses every *vertex* once). Hierholzer's algorithm is a DFS that appends a node/edge to the output **after** all its outgoing edges are exhausted, then reverses.

**Existence conditions**:

| Graph | Euler circuit | Euler path |
|-------|---------------|------------|
| Undirected | every vertex has even degree | exactly 0 or 2 odd-degree vertices |
| Directed | `in == out` for every vertex | one vertex `out-in==1` (start), one `in-out==1` (end) |

**Modeling trick for LC 753**: don't search all `k^n` strings. Build a **de Bruijn graph** — node = last `n-1` digits, edge = appending one digit (there are `k` per node, so every node has `in == out == k` → an Euler circuit always exists). Walking the circuit emits a string in which **every** length-`n` password appears exactly once.

```java
// java
// LC 753 - Cracking the Safe
// IDEA: de Bruijn graph + Hierholzer Euler circuit.
//       node = (n-1)-digit prefix, edge = one appended digit.
// time = O(k^n), space = O(k^n)
import java.util.*;

public class Solution {
    private Set<String> seen;      // visited EDGES (the n-digit strings)
    private StringBuilder sb;

    public String crackSafe(int n, int k) {
        seen = new HashSet<>();
        sb = new StringBuilder();

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n - 1; i++) {
            s.append('0');
        }
        String start = s.toString();

        dfs(start, k);
        // post-order emission => append the starting node back at the end
        return sb.toString() + start;
    }

    private void dfs(String node, int k) {
        for (int d = 0; d < k; d++) {
            String edge = node + d;          // the n-digit password = an edge
            if (seen.add(edge)) {            // add() returns false if already used
                dfs(edge.substring(1), k);   // move to next node = drop first digit
                sb.append(d);                // emit AFTER exhausting the subtree
            }
        }
    }
}
```

```python
# python
# LC 753 - Cracking the Safe
# IDEA: de Bruijn graph + Hierholzer Euler circuit
# time = O(k^n), space = O(k^n)
class Solution(object):
    def crackSafe(self, n, k):
        seen = set()      # visited EDGES (n-digit strings)
        out = []

        def dfs(node):
            for d in map(str, range(k)):
                edge = node + d
                if edge not in seen:
                    seen.add(edge)
                    dfs(edge[1:])     # next node = drop the first digit
                    out.append(d)     # emit AFTER the subtree is exhausted

        start = "0" * (n - 1)
        dfs(start)
        return "".join(out) + start

# crackSafe(2, 2) -> "01100"  (contains 00, 01, 10, 11)
# length is always k^n + n - 1
```

**Iterative Hierholzer (same idea, no recursion — used by LC 332 Reconstruct Itinerary)**:
```python
# python
# time = O(E log E) with sorting, space = O(E)
def euler_path(graph, start):
    """graph: node -> list of next nodes (mutable, consumed as we walk)"""
    stack, route = [start], []
    while stack:
        while graph[stack[-1]]:
            stack.append(graph[stack[-1]].pop())   # walk until stuck
        route.append(stack.pop())                  # stuck => this node is final
    return route[::-1]
```

**Interview signal**: "use every edge / every transition exactly once", "shortest string containing all combinations" → Euler, not Hamiltonian / not brute force.

---

### Template 3: Bipartite Detection with Union-Find

*Twist on 2-colouring*: instead of propagating colours, give every vertex a **twin** `v + n` standing for "the other side". An edge `(u, v)` then means *u and v must not share a set*, enforced by unioning `u` with `v`'s twin and vice versa; a conflict shows up the moment `u` and `v` are already connected. Worth reaching for when edges arrive online, where BFS/DFS colouring would have to restart from scratch.

```python
def is_bipartite_union_find(n, edges):
    """Check bipartite using Union-Find for conflict detection"""

    class UnionFind:
        def __init__(self, n):
            self.parent = list(range(2 * n))  # 2n for opposite groups

        def find(self, x):
            if self.parent[x] != x:
                self.parent[x] = self.find(self.parent[x])
            return self.parent[x]

        def union(self, x, y):
            px, py = self.find(x), self.find(y)
            if px != py:
                self.parent[px] = py

        def connected(self, x, y):
            return self.find(x) == self.find(y)

    uf = UnionFind(n)

    # For each edge (u,v), union u with opposite of v, and v with opposite of u
    for u, v in edges:
        if uf.connected(u, v):  # Same group conflict
            return False

        # u should be in opposite group of v
        uf.union(u, v + n)  # u with opposite of v
        uf.union(v, u + n)  # v with opposite of u

    return True
```

### Template 4: Maximum Bipartite Matching (Kuhn's Algorithm)

**1. Maximum Bipartite Matching**
```python
def max_bipartite_matching(graph, n, m):
    """Find maximum matching in bipartite graph"""
    match = [-1] * m

    def dfs(u, visited):
        for v in graph[u]:
            if not visited[v]:
                visited[v] = True
                if match[v] == -1 or dfs(match[v], visited):
                    match[v] = u
                    return True
        return False

    result = 0
    for u in range(n):
        visited = [False] * m
        if dfs(u, visited):
            result += 1

    return result
```

### Template 5: Bipartite Validation with Custom Conflict Rules

**2. Bipartite Graph Validation with Custom Logic**
```python
def validate_bipartite_assignment(assignments, conflicts):
    """
    Validate if assignment is bipartite given conflict pairs
    assignments: list of items to assign
    conflicts: list of (item1, item2) that cannot be in same group
    """
    from collections import defaultdict

    graph = defaultdict(list)
    for u, v in conflicts:
        graph[u].append(v)
        graph[v].append(u)

    colors = {}

    def can_color(item, color):
        if item in colors:
            return colors[item] == color

        colors[item] = color
        for conflict_item in graph[item]:
            if not can_color(conflict_item, 1 - color):
                return False
        return True

    for item in assignments:
        if item not in colors:
            if not can_color(item, 0):
                return False, {}

    # Return partition
    group_a = [item for item, color in colors.items() if color == 0]
    group_b = [item for item, color in colors.items() if color == 1]

    return True, {"Group A": group_a, "Group B": group_b}
```

### Template 6: Greedy k-Colouring (when 2 colours are not enough) — LC 1042 ⭐⭐⭐

*Twist on bipartite*: with `k` colors and a guarantee that every vertex has degree `< k`, no search/backtracking is needed at all — just walk the vertices in order and pick any color not used by an already-colored neighbour. LC 1042 guarantees degree ≤ 3 with 4 colors available, so a greedy pass always succeeds.

```python
# python
# LC 1042 - Flower Planting With No Adjacent
# IDEA: degree <= 3 and 4 colors available => a free color ALWAYS exists.
#       greedy single pass, no bipartite check / no backtracking needed.
# time = O(V + E), space = O(V + E)
from collections import defaultdict

class Solution(object):
    def gardenNoAdj(self, n, paths):
        g = defaultdict(list)
        for a, b in paths:
            g[a].append(b)
            g[b].append(a)

        res = [0] * n                      # res[i-1] = flower type of garden i
        for i in range(1, n + 1):
            used = {res[j - 1] for j in g[i]}   # 0 = "not yet colored"
            res[i - 1] = next(c for c in (1, 2, 3, 4) if c not in used)
        return res
```

```java
// java
// LC 1042 - Flower Planting With No Adjacent
// time = O(V + E), space = O(V + E)
public int[] gardenNoAdj(int n, int[][] paths) {
    List<List<Integer>> g = new ArrayList<>();
    for (int i = 0; i <= n; i++) {
        g.add(new ArrayList<>());
    }
    for (int[] p : paths) {
        g.get(p[0]).add(p[1]);
        g.get(p[1]).add(p[0]);
    }

    int[] res = new int[n];
    for (int i = 1; i <= n; i++) {
        boolean[] used = new boolean[5];
        for (int nb : g.get(i)) {
            used[res[nb - 1]] = true;      // res = 0 for uncolored, harmless
        }
        for (int c = 1; c <= 4; c++) {
            if (!used[c]) { res[i - 1] = c; break; }
        }
    }
    return res;
}
```

**Key distinction**: 2-coloring (bipartite) needs BFS/DFS propagation because a color choice **forces** the neighbours. With `k > max_degree` colors, choices never conflict, so greedy is optimal — say this out loud instead of reaching for backtracking.

### Template 7: Max Flow / Min Cut — Ford-Fulkerson (Edmonds-Karp)

**Min Cut = Max Flow** (by max-flow min-cut theorem).

```python
from collections import defaultdict, deque

def max_flow(graph, source, sink, n):
    """graph[u][v] = capacity. Returns max flow from source to sink."""
    def bfs(source, sink, parent):
        visited = set([source])
        queue = deque([source])
        while queue:
            u = queue.popleft()
            for v in range(n):
                if v not in visited and graph[u][v] > 0:
                    visited.add(v)
                    parent[v] = u
                    if v == sink: return True
                    queue.append(v)
        return False

    flow = 0
    while True:
        parent = [-1] * n
        if not bfs(source, sink, parent):
            break
        # Find min capacity along the path
        path_flow = float('inf')
        s = sink
        while s != source:
            u = parent[s]
            path_flow = min(path_flow, graph[u][s])
            s = parent[s]
        # Update capacities
        s = sink
        while s != source:
            u = parent[s]
            graph[u][s] -= path_flow
            graph[s][u] += path_flow
            s = parent[s]
        flow += path_flow
    return flow
```

**Time**: O(VE²) Edmonds-Karp. **Use for**: network capacity, matching, crew scheduling.

## Summary & Quick Reference

### Complexity Quick Reference

| Algorithm | Time | Space | Notes |
|---|---|---|---|
| Tarjan (SCC / bridges / APs) | O(V + E) | O(V) | single DFS, `disc[]` + `low[]` |
| Kosaraju SCC | O(V + E) | O(V + E) | two DFS passes, needs the transpose graph |
| Hierholzer Euler path | O(E) | O(E) | O(E log E) when edges must be sorted (LC 332) |
| Union-Find bipartite | O(E·α(V)) | O(V) | `2n` nodes: `v` and its twin `v + n` |
| Kuhn's bipartite matching | O(V·E) | O(V) | one augmenting DFS per left vertex |
| Edmonds-Karp max flow | O(V·E²) | O(V²) | BFS augmenting paths; min cut = max flow |
| Greedy k-colouring | O(V + E) | O(V + E) | only valid when `k > max_degree` |

### Interview Signal → Template

| Signal | Template |
|---|---|
| "critical connections", "removing one cable splits the network" | Tarjan bridges |
| "cut vertices", "removing a server splits the cluster" | Tarjan articulation points |
| "strongly connected", "mutually reachable" | Tarjan SCC (or Kosaraju) |
| "use every edge / every transition exactly once" | Hierholzer Euler |
| "shortest string containing every combination" | de Bruijn graph + Euler circuit |
| "two groups, these pairs cannot share one", edges arriving online | Union-Find bipartite |
| "largest set of pairings" | Kuhn's matching |
| "maximum throughput", "cheapest set of links to cut" | Edmonds-Karp |
| "k labels, every vertex has fewer than k neighbours" | greedy k-colouring |

### Related Topics
- **[graph.md](./graph.md)**: representation, traversal and cycle detection — read that first
- **[union_find.md](./union_find.md)**: the DSU primitives Templates 3 and 4 assume
- **[dfs_advanced.md](./dfs_advanced.md)**: the DFS sheet's own Hierholzer and Tarjan-bridge templates
- **[topology_sorting.md](./topology_sorting.md)**: condensing SCCs yields a DAG to sort
- **Minimum spanning tree**: Kruskal is [union_find.md](./union_find.md) plus a sorted edge
  list; Prim is [heap.md](./heap.md) plus a visited set. LC 1135, LC 1584.
