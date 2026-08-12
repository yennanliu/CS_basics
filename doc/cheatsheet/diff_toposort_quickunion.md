# Quick Comparison: Topological Sort vs Quick Union

> **Scope** — **Decision doc only** — topological sort vs union-find: what each answers, where the naive choice is wrong, and problems solvable by both.
> **See also**: [topology_sorting.md](./topology_sorting.md); [union_find.md](./union_find.md); [graph.md](./graph.md).

| Feature | Topological Sort | Quick Union (Disjoint Set Union) |
|:---|:---|:---|
| Purpose | **Order nodes** respecting dependency (**DAGs** only) | **Find connected components** and **detect cycles** (**undirected graphs**) |
| Works on | **Directed graphs (DAGs)** | **Undirected graphs** |
| Detects cycle? | ✅ (if cannot topologically sort, cycle detected) | ✅ (when two nodes already share a parent, cycle detected) |
| Handles direction of edges? | ✅ (Direction matters: `u ➔ v`) | ❌ (Ignores direction: just connected or not) |
| Output | Ordered list of nodes (`[u, v, w]`) | Connected components or cycle detection |
| Common Use Cases | Course scheduling, build systems, dependency resolution | Kruskal's MST, dynamic connectivity, union-find problems |
| Time Complexity | O(V + E) | Nearly O(1) per operation (amortized with path compression) |
| Space Complexity | O(V + E) | O(V) |

---

## LeetCode Problem Lists

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Union-Find](https://leetcode.com/problem-list/union-find/)

## 🏩 Conceptual Difference

| | Topological Sort | Quick Union |
|:-|:-|:-|
| Respect dependency order | ✅ | ❌ |
| Is everything connected? | ❌ | ✅ |
| Detects directed cycles? | ✅ | ❌ |

---

## ⚙️ Algorithm Core Ideas

### ➔ Topological Sort (for DAGs)
- Think **assembly line**:  
  - Can't assemble a car until frame is ready.
- Process nodes with no incoming edges first.
- If you ever get stuck (nodes still left but no "zero indegree" node), **cycle detected**.

Two common ways:
- **BFS** with indegree array.
- **DFS** with recursion + post-order.

---

### ➔ Quick Union (Disjoint Set Union)
- Think **friend groups**:
  - If Alice knows Bob, Bob knows Charlie → one group.
- Each node points to a *parent*.
- If two nodes already have the same root → **cycle detected** (for undirected graphs).
- Heavily optimized with:
  - **Path Compression** (flatten tree during find)
  - **Union by Rank/Size** (attach smaller tree under larger one)

---

## 🚀 Visual Example

Imagine the same input:

```text
Courses: 0 -> 1 -> 2
```

| | Topological Sort | Quick Union |
|:-|:-|:-|
| What happens? | Outputs [0, 1, 2] (order matters) | Simply groups them together (connection matters, not order) |
| Why? | Because 0 must finish before 1, and 1 before 2 | Only cares that they're connected |
| Cycle detection? | If a back edge exists (e.g., 2 ➔ 0) → **cycle** | If trying to connect two nodes already connected → **cycle** |

---

## 🧪 Analogy

- **Topological Sort** is like **building a skyscraper**:  
  - Must finish floors **bottom-up** respecting the order.

- **Quick Union** is like **finding clusters of friends**:  
  - Doesn't matter who talked first, just find connected groups.

---

## 📜 Summary

| Question | Answer |
|:---|:---|
| Are they solving the same problem? | ❌ |
| Do both detect cycles? | ✅ (in different contexts) |
| Which one should I use for Course Schedule (directed graph)? | **Topological Sort** ✅ |
| Which one is faster per operation? | **Quick Union** (amortized ~O(1)) ✅ |
| Which one handles dependency ordering? | **Topological Sort** ✅ |

---

## ✅ Final Mental Model

| | Topological Sort | Quick Union |
|:-|:-|:-|
| Graph Type | Directed | Undirected |
| Goal | Respect order, detect cycles | Connect components, detect undirected cycles |
| Typical Problems | Scheduling, compilation order | Kruskal’s MST, dynamic connectivity |

---

## 🧭 Decision Table — Which Tool for Which LC Problem ⭐⭐⭐⭐⭐

**How to read it**: the deciding question is always the same two-parter —
1. Are edges **directed** (`u` must come before `v`)? → **Topological Sort**
2. Do you only need **"are these in the same blob?"** with **no order**? → **Union-Find**

### **Pick Topological Sort**

| LC | Problem | Why topo sort |
|:---|:---|:---|
| 210 | Course Schedule II | Directed prerequisites **and** an actual order must be printed |
| 802 | Find Eventual Safe States | Directed; "safe" = cannot reach a cycle → peel **out-degree 0** on the reversed graph |
| 851 | Loud and Rich | `richer` is a directed partial order → DAG + memo/topo DP |
| 1462 | Course Schedule IV | Directed prerequisites + reachability queries (topo order then closure) |
| 2115 | Find All Possible Recipes from Given Supplies | A recipe unlocks only after **all** ingredients exist → classic indegree countdown |
| 1857 | Largest Color Value in a Directed Graph | Topo order + DP over 26 color counters; cycle → return `-1` |
| 1203 | Sort Items by Groups Respecting Dependencies | Two nested topo sorts (groups, then items inside a group) |
| 310 | Minimum Height Trees | Undirected, but the answer is a **peeling order** (strip degree-1 leaves layer by layer) — union-find cannot produce it |
| 1591 | Strange Printer II | Colors must be printed in a dependency order → build DAG over colors |

### **Pick Union-Find**

| LC | Problem | Why union-find |
|:---|:---|:---|
| 547 | Number of Provinces | Undirected, "how many blobs" — no ordering at all |
| 684 | Redundant Connection | Undirected; the first edge whose endpoints already share a root closes the cycle |
| 721 | Accounts Merge | Transitive merge keyed by email; direction meaningless |
| 990 | Satisfiability of Equality Equations | `==` is an equivalence relation → union all `==` first, then verify every `!=` |
| 947 | Most Stones Removed with Same Row or Column | Union by **row-key / col-key**, not by grid adjacency |
| 839 | Similar String Groups | Pairwise-similarity components |
| 1319 | Number of Operations to Make Network Connected | Answer = `components - 1`, feasible iff `edges >= n-1` |
| 1971 | Find if Path Exists in Graph | Pure undirected reachability |
| 1584 | Min Cost to Connect All Points | Kruskal MST = sort edges + union-find |
| 1489 | Find Critical and Pseudo-Critical Edges in MST | Re-run Kruskal with an edge forced in / forced out |
| 2092 | Find All People With Secret | Group per timestamp, then **undo** (reset parents) for people who missed out |
| 1697 | Checking Existence of Edge Length Limited Paths | Offline: sort queries + edges by weight, union incrementally |
| 1202 | Smallest String With Swaps | Swappable indices form components → sort chars inside each component |
| 1722 | Minimize Hamming Distance After Swap Operations | Same idea: multiset compare inside each swap component |
| 1559 | Detect Cycles in 2D Grid | Undirected cycle in a grid; union only **right + down** neighbors of the same char to avoid double-counting |
| 1632 | Rank Transform of a Matrix | **Both**: union-find groups equal values in a row/col, topo-style ordering assigns ranks |

### **Pick Neither (the tag is a trap)**

| LC | Problem | Actually use |
|:---|:---|:---|
| 200 | Number of Islands | DFS/BFS flood fill — union-find is correct but heavier. UF only earns its keep when islands are **added/merged incrementally** (see 827 Making A Large Island) |
| 128 | Longest Consecutive Sequence | Hash-set expansion, O(n). Union-find is a valid but over-engineered alternative |
| 329 | Longest Increasing Path in a Matrix | Memoized DFS. It is tagged topological-sort because the strictly-increasing rule makes an implicit DAG, but you never need a materialized order; union-find is **impossible** (path *length* is directional) |
| 785 | Is Graph Bipartite? | BFS/DFS 2-coloring. Union-find works only via the "union each node with its neighbors' enemies" trick (same trick as 886 Possible Bipartition) |
| 130 | Surrounded Regions / 1254 Number of Closed Islands | DFS from the border is simpler; the UF version needs an artificial virtual "outside" node |

---

## ⚠️ Naive-Choice Traps ⭐⭐⭐⭐

- **"It's a graph cycle → union-find"** — only if **undirected**. LC 802 (Find Eventual Safe States) and LC 210 (Course Schedule II) are directed; union-find literally cannot see `u ➔ v` vs `v ➔ u`.
- **LC 684 → LC 685 (Redundant Connection II)** — the classic bait. 684 is undirected, plain union-find. 685 is **directed**, so a node can have **two parents** without any cycle existing. Plain union-find gives the wrong edge; see the template below.
- **LC 310 (Minimum Height Trees)** — an undirected tree pattern-matches to union-find, but union-find reports *components*, not *centroids*. The fix is a **topological-sort-shaped** algorithm on an undirected graph: repeatedly remove degree-1 nodes.
- **LC 947 (Most Stones Removed)** — unioning grid-adjacent stones is wrong. Two stones are related when they share **a row or a column**, however far apart.
- **LC 1361 (Validate Binary Tree Nodes)** — genuinely solvable **both** ways; know both (template below).
- **Rule of thumb**: if the required output is a **sequence**, union-find is out. If the required output is a **count / yes-no about togetherness**, topo sort is overkill.

---

## 🔀 Same Problem, Both Tools — LC 1361 Validate Binary Tree Nodes ⭐⭐⭐⭐

Given `n` nodes with `leftChild[i]` / `rightChild[i]` (`-1` = none), decide if they form **exactly one** valid binary tree.
Both tools work because the three failure modes are: a node with **2 parents**, a **cycle**, or a **forest**.

```java
// java
// time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
// IDEA: union-find view -> a tree is "n nodes, no node with 2 parents,
//       no edge that closes a cycle, and exactly 1 component at the end"
// LC 1361 - Validate Binary Tree Nodes  (approach 1: union-find)
class SolutionUF {
    int[] parent;

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];   // path compression
            x = parent[x];
        }
        return x;
    }

    public boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;

        int[] indeg = new int[n];
        int components = n;

        for (int i = 0; i < n; i++) {
            for (int child : new int[]{ leftChild[i], rightChild[i] }) {
                if (child == -1) continue;
                if (++indeg[child] > 1) return false;   // 2 parents -> not a tree
                int ru = find(i), rv = find(child);
                if (ru == rv) return false;             // edge closes a cycle
                parent[rv] = ru;
                components--;
            }
        }
        return components == 1;                          // forest -> false
    }
}
```

```java
// java
// time = O(n), space = O(n)
// IDEA: topological / indegree view -> exactly one indegree-0 root,
//       then a BFS from that root must reach all n nodes
// LC 1361 - Validate Binary Tree Nodes  (approach 2: indegree + BFS)
class SolutionTopo {
    public boolean validateBinaryTreeNodes(int n, int[] leftChild, int[] rightChild) {
        int[] indeg = new int[n];
        for (int i = 0; i < n; i++) {
            if (leftChild[i]  != -1 && ++indeg[leftChild[i]]  > 1) return false;
            if (rightChild[i] != -1 && ++indeg[rightChild[i]] > 1) return false;
        }

        int root = -1;
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) {
                if (root != -1) return false;   // 2+ roots -> forest
                root = i;
            }
        }
        if (root == -1) return false;           // no root -> cycle

        Deque<Integer> q = new ArrayDeque<>();
        q.offer(root);
        int seen = 0;
        while (!q.isEmpty()) {
            int cur = q.poll();
            seen++;
            if (leftChild[cur]  != -1) q.offer(leftChild[cur]);
            if (rightChild[cur] != -1) q.offer(rightChild[cur]);
        }
        return seen == n;                        // unreached nodes -> cycle / forest
    }
}
```

```python
# python
# time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
# IDEA: union-find view -> reject a 2nd parent, reject a cycle-closing edge,
#       then require exactly 1 remaining component
# LC 1361 - Validate Binary Tree Nodes  (approach 1: union-find)
class SolutionUF:
    def validateBinaryTreeNodes(self, n, leftChild, rightChild):
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]   # path compression
                x = parent[x]
            return x

        indeg = [0] * n
        components = n

        for i in range(n):
            for child in (leftChild[i], rightChild[i]):
                if child == -1:
                    continue
                indeg[child] += 1
                if indeg[child] > 1:
                    return False                # 2 parents -> not a tree
                ru, rv = find(i), find(child)
                if ru == rv:
                    return False                # edge closes a cycle
                parent[rv] = ru
                components -= 1

        return components == 1                  # forest -> False
```

```python
# python
# time = O(n), space = O(n)
# IDEA: topological / indegree view -> exactly one indegree-0 root,
#       then BFS from it must visit all n nodes
# LC 1361 - Validate Binary Tree Nodes  (approach 2: indegree + BFS)
from collections import deque

class SolutionTopo:
    def validateBinaryTreeNodes(self, n, leftChild, rightChild):
        indeg = [0] * n
        for i in range(n):
            for child in (leftChild[i], rightChild[i]):
                if child == -1:
                    continue
                indeg[child] += 1
                if indeg[child] > 1:
                    return False

        root = -1
        for i in range(n):
            if indeg[i] == 0:
                if root != -1:
                    return False       # 2+ roots -> forest
                root = i
        if root == -1:
            return False               # no root -> cycle

        q, seen = deque([root]), 0
        while q:
            cur = q.popleft()
            seen += 1
            for child in (leftChild[cur], rightChild[cur]):
                if child != -1:
                    q.append(child)
        return seen == n
```

**Takeaway**: on a *directed* input, the indegree (topo) view is the more natural one — it names the failure modes directly. Union-find still works here only because a binary tree is also a valid *undirected* tree once you add the "at most one parent" guard.

---

## 🧩 When Union-Find Alone Is Wrong — LC 685 Redundant Connection II ⭐⭐⭐⭐

**LC 684** (undirected): plain union-find, return the first edge whose endpoints already share a root. ✅
**LC 685** (directed): union-find alone is **wrong**, because a rooted tree also requires *every node has exactly one parent*. Two failure modes exist and can co-occur:

| Case | Symptom | Answer |
|:---|:---|:---|
| A | Some node has **2 parents**, no cycle | the **later** of the two parent-edges |
| B | **Cycle** only, every node has 1 parent | the edge that closes the cycle |
| C | **Both** (the cycle passes through the 2-parent node) | the **earlier** parent-edge that sits on the cycle |

```java
// java
// time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
// IDEA: 1) scan for a node with 2 parents -> remember cand1 (1st edge) & cand2 (2nd edge)
//       2) union all edges EXCEPT cand2. A cycle now means cand2 was innocent:
//          answer is cand1 (if it exists) else the cycle edge. No cycle -> cand2.
// LC 685 - Redundant Connection II
class Solution {
    private int find(int[] p, int x) {
        while (p[x] != x) {
            p[x] = p[p[x]];
            x = p[x];
        }
        return x;
    }

    public int[] findRedundantDirectedConnection(int[][] edges) {
        int n = edges.length;
        int[] parentOf = new int[n + 1];        // parent recorded in the INPUT
        int[] cand1 = null, cand2 = null;

        for (int[] e : edges) {
            int u = e[0], v = e[1];
            if (parentOf[v] != 0) {             // v already had a parent
                cand1 = new int[]{ parentOf[v], v };
                cand2 = e;
            } else {
                parentOf[v] = u;
            }
        }

        int[] p = new int[n + 1];
        for (int i = 0; i <= n; i++) p[i] = i;

        for (int[] e : edges) {
            if (e == cand2) continue;           // tentatively drop the 2nd parent edge
            int ru = find(p, e[0]), rv = find(p, e[1]);
            if (ru == rv) return (cand1 == null) ? e : cand1;   // case B / case C
            p[rv] = ru;
        }
        return cand2;                            // case A
    }
}
```

```python
# python
# time = O(n log n) (path halving only; O(n * α(n)) needs union by size/rank too), space = O(n)
# IDEA: 1) find a node with 2 parents -> cand1 (1st edge), cand2 (2nd edge)
#       2) union everything except cand2; a cycle means cand2 was innocent
#          -> answer cand1 if it exists else the cycle edge; no cycle -> cand2
# LC 685 - Redundant Connection II
class Solution:
    def findRedundantDirectedConnection(self, edges):
        n = len(edges)
        parent_of = [0] * (n + 1)          # parent recorded in the INPUT
        cand1 = cand2 = None

        for u, v in edges:
            if parent_of[v]:               # v already had a parent
                cand1, cand2 = [parent_of[v], v], [u, v]
            else:
                parent_of[v] = u

        p = list(range(n + 1))

        def find(x):
            while p[x] != x:
                p[x] = p[p[x]]
                x = p[x]
            return x

        for e in edges:
            if e == cand2:                 # tentatively drop the 2nd parent edge
                continue
            ru, rv = find(e[0]), find(e[1])
            if ru == rv:                   # case B / case C
                return e if cand1 is None else cand1
            p[rv] = ru
        return cand2                       # case A
```

**Why this is the canonical "wrong tool" lesson**: union-find answers *"are these connected?"*. It never answers *"does this node have too many parents?"* — that is an **indegree** question. Directed problems almost always need the indegree bookkeeping on top.

---

## 📚 Go Deeper

This page is a **decision aid** only. For full templates, variations and problem walk-throughs:

- [`topology_sorting.md`](./topology_sorting.md) — BFS (Kahn) + DFS templates, lexicographic order, cycle reporting
- [`union_find.md`](./union_find.md) — path compression, union by rank/size, virtual nodes, weighted/rollback DSU
- [`graph.md`](./graph.md) — general traversal, bipartite checking, MST, shortest paths

---

