# Union Find

> **Scope** — Disjoint set union — connectivity, component counting, cycle detection in **undirected** graphs, with path compression and union by rank.
> **See also**: [union_find_examples.md](./union_find_examples.md) — the nineteen worked problems behind these patterns; [diff_toposort_quickunion.md](./diff_toposort_quickunion.md) — union-find vs toposort — which tool for which problem; [topology_sorting.md](./topology_sorting.md) — directed ordering; [graph.md](./graph.md) — general graph material.

- Efficiently determines connectivity between nodes in dynamic graphs
- When to use it: Dynamic connectivity queries, cycle detection, MST algorithms, grouping elements
- Key LeetCode problems: Graph Valid Tree, Number of Islands, Accounts Merge, Friend Circles
- Data structures: Parent array, size/rank array for optimization
- States: Connected components, parent-child relationships

**Time Complexity:** Nearly O(1) per operation with optimizations

## LeetCode Problem Lists

- [Union-Find](https://leetcode.com/problem-list/union-find/)
- [Graph Theory](https://leetcode.com/problem-list/graph/)

## 0) Concept

### 0-0) Union-Find Variants

#### Quick Find vs Quick Union

**Quick Find:**
- **Find**: O(1) - Direct array lookup
- **Union**: O(n) - Update all elements in component
- **Use Case**: When find operations greatly outnumber union operations
- **Implementation**: Store component ID directly for each element

```java
// Quick Find Implementation
class QuickFind {
    private int[] id;
    private int count; // number of components

    public QuickFind(int n) {
        id = new int[n];
        count = n;
        for (int i = 0; i < n; i++) {
            id[i] = i; // Each element is its own component
        }
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public int find(int p) {
        return id[p]; // Direct lookup
    }

    /**
     * time = O(N)
     * space = O(1)
     */
    public void union(int p, int q) {
        int pID = find(p);
        int qID = find(q);

        if (pID == qID) return;

        // Change all entries with id[p] to id[q]
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pID) {
                id[i] = qID;
            }
        }
        count--;
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }
}
```

**Quick Union (with optimizations):**
- **Find**: O(α(n)) ≈ O(1) with path compression
- **Union**: O(α(n)) ≈ O(1) with union by rank/size
- **Use Case**: General purpose, balanced find/union operations
- **Implementation**: Store parent pointers, build tree structure

**Comparison:**

| Operation | Quick Find | Quick Union | Quick Union + Optimizations |
|-----------|------------|-------------|---------------------------|
| Initialize | O(n) | O(n) | O(n) |
| Find | O(1) | O(n) worst | O(α(n)) ≈ O(1) |
| Union | O(n) | O(n) worst | O(α(n)) ≈ O(1) |
| Space | O(n) | O(n) | O(n) |
| Best For | Many finds | Balanced | General purpose |

**When to Use Quick Find:**
- Very rare union operations
- Real-time find queries required
- Small datasets where O(n) union is acceptable

**When to Use Quick Union (Optimized):**
- Balanced mix of find and union operations
- Large datasets (millions of elements)
- Most practical applications (recommended)

### 0-1) Key Optimizations
Union Find achieves nearly O(1) performance through two critical optimizations:

**Path Compression**: Applied in `find()` operation
- Makes each visited node point directly to the root
- Flattens the tree structure during traversal
- Recursive: `parent[x] = find(parent[x])`
- Amortizes future lookups to O(1)

**Union by Rank/Size**: Applied in `union()` operation
- Always attach smaller tree to larger tree's root
- Keeps tree height balanced (logarithmic)
- Prevents degenerate linear chains
- Can track either tree height (rank) or size (count)

Without these optimizations, operations degrade to O(n). With both, time complexity becomes O(α(n)) where α is the inverse Ackermann function (effectively constant).

### 0-2) Types
- **Basic Connectivity**: Check if nodes are connected, count components
- **Cycle Detection**: Determine if adding edge creates cycle
- **Dynamic MST**: Kruskal's algorithm for minimum spanning trees
- **Weighted Union Find**: Handle ratios/weights between nodes (LC 399)
- **Grid Problems**: 2D grid connectivity (Number of Islands variants)

### 0-3) Algorithm Pattern / Template

**Core Operations:**
- `find(x)`: Get root parent of x with path compression
- `union(x, y)`: Connect two nodes, return false if already connected
- `connected(x, y)`: Check if two nodes are in same component

**Template (Union by Size):**
```java
class UnionFind {
    int[] parent, size;
    int components;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    // Path Compression: flatten tree by making nodes point directly to root
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Compress path during recursion
        }
        return parent[x];
    }

    // Union by Size: attach smaller tree to larger tree
    public boolean union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return false; // Already connected

        // Always attach smaller size to larger size
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
        components--;
        return true;
    }
}
```

**Alternative Template (Union by Rank):**
```java
class UnionFind {
    int[] parent, rank;
    int components;

    public UnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0; // Initial rank is 0
        }
    }

    // Path Compression: recursively flatten tree structure
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Make x point directly to root
        }
        return parent[x];
    }

    // Union by Rank: attach lower rank tree to higher rank tree
    public void union(int x, int y) {
        int rootX = find(x), rootY = find(y);

        // Already in the same component
        if (rootX == rootY) return;

        // Attach smaller rank tree to larger rank tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY; // X's tree becomes child of Y
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX; // Y's tree becomes child of X
        } else {
            // Equal ranks: attach either way, increment rank of new root
            parent[rootX] = rootY;
            rank[rootY]++;
        }
        components--;
    }
}
```

**Python Template (Union by Size — clean class):**
```python
# python
# IDEA: path compression (in find) + union by size (attach smaller tree to larger)
# time = O(α(N)) ≈ O(1) amortized per op, space = O(N)
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))   # each node is its own root initially
        self.size = [1] * n            # size[root] = # nodes in that component
        self.components = n            # running count of components

    # Path Compression: point every visited node directly at the root
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])   # compress on the way back
        return self.parent[x]

    # Union by Size: attach the smaller tree under the larger one
    def union(self, x, y):
        root_x, root_y = self.find(x), self.find(y)
        if root_x == root_y:
            return False               # already connected (adding this edge => cycle)
        if self.size[root_x] < self.size[root_y]:
            root_x, root_y = root_y, root_x   # ensure root_x is the larger tree
        self.parent[root_y] = root_x
        self.size[root_x] += self.size[root_y]
        self.components -= 1
        return True

    def connected(self, x, y):
        return self.find(x) == self.find(y)
```

**Python Template (Union by Rank — alternative):**
```python
# python
# IDEA: path compression + union by rank (approx tree height); rank++ only on equal ranks
# time = O(α(N)) ≈ O(1) amortized per op, space = O(N)
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n            # rank ~ upper bound on tree height
        self.components = n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])   # path compression
        return self.parent[x]

    def union(self, x, y):
        root_x, root_y = self.find(x), self.find(y)
        if root_x == root_y:
            return False
        # attach the lower-rank tree under the higher-rank tree
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1     # equal ranks: pick one root, bump its rank
        self.components -= 1
        return True
```

> **Python ASCII trace — path compression during `find(3)`** on chain `3 → 2 → 1 → 0 (root)`:
>
> ```text
> parent = [0, 0, 1, 2]        # index:  0  1  2  3
>
>   Before find(3):            Recursion unwinds, each frame rewires parent[x] = root:
>
>     0 (root)                   find(3) → find(2) → find(1) → find(0) returns 0
>     |                          ↑ on the way back:
>     1                            parent[1] = 0
>     |                            parent[2] = 0
>     2                            parent[3] = 0
>     |
>     3
>
>   After find(3):             parent = [0, 0, 0, 0]
>
>          0 (root)            # tree flattened: every node now points straight to root 0
>        / | \                 # any later find() on 1/2/3 is O(1)
>       1  2  3
> ```

**Key Differences: Size vs Rank**
- **Union by Size**: Tracks actual count of nodes in each tree
  - Useful when you need component sizes
  - Updates size after every union
- **Union by Rank**: Tracks approximate tree height (upper bound)
  - More space efficient (rank grows slowly)
  - Rank only increases when merging equal-rank trees
  - With path compression, rank != actual height

**Edge Cases:**
- Single node graphs
- Already connected nodes
- Invalid indices

### 0-4) The Six Union-Find Patterns

Every problem below is the same template with a different answer to *"what is a node, and when
do two of them merge?"* That question is the whole difficulty; the DSU itself never changes.

| # | Pattern | What a node is | When you merge | Worked at |
|---|---|---|---|---|
| 1 | **Cycle detection** | a vertex | on every edge — a `union` that returns `false` *is* the cycle | [1) LC 684](./union_find_examples.md#1-redundant-connection--lc-684), [5) LC 261](./union_find_examples.md#5-graph-valid-tree--lc-261) |
| 2 | **Component counting** | a vertex | on every edge; decrement a counter when the union succeeds | [6) LC 323](./union_find_examples.md#6-number-of-connected-components-in-an-undirected-graph--lc-323), [4) LC 547](./union_find_examples.md#4-number-of-provinces--lc-547) |
| 3 | **Redundant edge** | a vertex | as above, but *return* the first edge whose union fails | [1) LC 684](./union_find_examples.md#1-redundant-connection--lc-684), [2) LC 685](./union_find_examples.md#2-redundant-connection-ii--lc-685--dsu-on-a-directed-graph-) |
| 4 | **2D grid connectivity** | a cell, flattened to `row * cols + col` | with the right/down neighbour, so each pair is considered once | [10) LC 200](./union_find_examples.md#10-number-of-islands--lc-200--grid--1d-via-row--cols--col), [11) LC 130](./union_find_examples.md#11-surrounded-regions--lc-130--a-virtual-border-node) |
| 5 | **Weighted union-find** | a variable | carrying a *ratio* to the parent, multiplied through on path compression | [13) LC 399](./union_find_examples.md#13-evaluate-division--lc-399--weighted-union-find-with-ratios-) |
| 6 | **BFS + union-find climb** | a tree node | with its parent, repeatedly, until every target converges on one root | [19) LC 865](./union_find_examples.md#19-smallest-subtree-with-all-the-deepest-nodes--lc-865--bfs--union-find-climb) — same as LC 1123; compare LC 236, LC 1644 and LC 1650, which are the recursive-LCA framing of it |

Two structural variants change *when* you run the algorithm rather than what it merges:

- **Sorted-edge (Kruskal-style)** — process edges cheapest-first and stop as soon as the two
  endpoints you care about connect. [14) LC 1631](./union_find_examples.md#14-path-with-minimum-effort--lc-1631--sorted-edge-kruskal-style).
- **Offline reverse** — union-find cannot *split*, so when the problem removes things, replay
  it backwards and add them instead. [15) LC 803](./union_find_examples.md#15-bricks-falling-when-hit--lc-803--offline-reverse-union-find-).


## 1) Example Problems with Code References

### Basic Connectivity & Component Counting
- **LC 200** – Number of Islands: Count connected components in 2D grid
  - Java: `leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfIslands.java:493`
  - Pattern: Grid to 1D conversion (`row * cols + col`), Union Find with 4-directional checks

- **LC 261** – Graph Valid Tree: Check if n-1 edges form exactly one component
  - Java: `leetcode_java/src/main/java/LeetCodeJava/BFS/GraphValidTree.java:36`
  - Pattern: Cycle detection, exactly n-1 edges validation

- **LC 323** – Number of Connected Components: Basic component counting
  - Java: `leetcode_java/src/main/java/LeetCodeJava/Graph/NumberOfConnectedComponentsUndirectedGraph.java:49`
  - Pattern: Track component count, decrement on successful union

### Cycle Detection & Redundancy
- **LC 684** – Redundant Connection: Find edge that creates cycle in tree
  - Java: `leetcode_java/src/main/java/LeetCodeJava/Tree/RedundantConnection.java:50`
  - Pattern: Return first edge where `union()` fails (cycle detected)

### Weighted Union Find
- **LC 399** – Evaluate Division: Weighted UF with ratios for equation solving
  - Java: `leetcode_java/src/main/java/LeetCodeJava/DFS/EvaluateDivision.java:421`
  - Pattern: Store ratios, path compression with ratio multiplication

### Advanced Applications
- **LC 130** – Surrounded Regions: Use dummy node for boundary connected regions
- **LC 547** – Friend Circles: Find groups in friendship matrix
- **LC 721** – Accounts Merge: Group accounts by shared emails
- **LC 865** – Smallest Subtree with all Deepest Nodes: BFS + parent-climb to find LCA of deepest nodes
- **LC 886** – Possible Bipartition: Detect bipartite graph conflicts
- **LC 1135** – Connecting Cities: MST with Kruskal's algorithm
- **LC 1319** – Network Connections: Minimum operations to connect all nodes
- **LC 2316** – Count Unreachable Pairs of Nodes: Component sizes + running-remainder cross-pair count (see §2-13)

### Sorted-Edge / Offline Union-Find
- **LC 1631** – Path With Minimum Effort: Kruskal sweep, minimize the max edge (see §2-14)
- **LC 778** – Swim in Rising Water: same sweep with the weight on the cell (see §2-14)
- **LC 1697** – Checking Existence of Edge Length Limited Paths: offline queries sorted by limit (see §2-14)
- **LC 803** – Bricks Falling When Hit: offline **reverse** DSU + virtual roof node (see §2-15)

### Size-Aware & Directed Variants
- **LC 827** – Making A Large Island: size bookkeeping + distinct-neighbour-root flip (see §2-16)
- **LC 685** – Redundant Connection II: directed graph, two-candidate elimination (see §2-17)
- **LC 1971** – Find if Path Exists in Graph: baseline "union all edges, then one connectivity query"

## 2) Diagrams

### Basic Union Operations
```text
Initial: [0] [1] [2] [3] [4]

After union(0,1): [0,1] [2] [3] [4]
                   1
                  /
                 0

After union(2,3): [0,1] [2,3] [4]
                   1     3
                  /     /
                 0     2
```

### Path Compression Visualization
```text
Before find(1):           After find(1):
     4 (root)                4 (root)
     |                      /|\
     3                     1 2 3
     |
     2
     |
     1

Call find(1):
- 1 → 2 → 3 → 4 (traversal)
- During return, compress: parent[1] = 4, parent[2] = 4, parent[3] = 4
- Result: All nodes point directly to root
```

### Union by Rank Example
```text
Initial state:
  0     1     2     3
rank: 0 0 0 0

union(0, 1):         union(2, 3):
    0                    2
   /                    /
  1                    3
rank[0] = 1         rank[2] = 1

union(0, 2):
    0 (rank=2)
   / \
  1   2
     /
    3

Why rank increased:
- rank[0] = 1, rank[2] = 1 (equal)
- Attach 2 to 0, increment rank[0] to 2
```

### Path Compression in Action
```text
Scenario: find(A) in chain A→B→C→D→E (root)

Step 1: Recursive calls
  find(A) calls find(B)
    find(B) calls find(C)
      find(C) calls find(D)
        find(D) calls find(E)
          find(E) returns E

Step 2: Path compression during return
  parent[D] = E
  parent[C] = E  ← Compression happens here
  parent[B] = E  ← Skip intermediate nodes
  parent[A] = E  ← Direct link to root

Result:
Before:  A → B → C → D → E
After:   A → E
         B → E
         C → E
         D → E
```

## 3) Tips & Pitfalls

**Common Mistakes:**
1. **Forgetting Path Compression**: Results in O(n) time instead of nearly O(1)
   ```java
   // WRONG: No path compression
   public int find(int x) {
       while (parent[x] != x) x = parent[x];
       return x;
   }

   // CORRECT: With path compression
   public int find(int x) {
       if (parent[x] != x) {
           parent[x] = find(parent[x]); // Flatten on return
       }
       return parent[x];
   }
   ```

2. **Not Tracking Component Count**: Missing decrement in union operation
   ```java
   // WRONG: Forgot to decrement
   public void union(int x, int y) {
       parent[find(x)] = find(y);
   }

   // CORRECT: Track components
   public void union(int x, int y) {
       int rootX = find(x), rootY = find(y);
       if (rootX != rootY) {
           parent[rootX] = rootY;
           components--; // Important!
       }
   }
   ```

3. **Index Confusion**: Mixing 0-based and 1-based indexing
4. **Cycle Detection Timing**: Checking after union instead of before
5. **Wrong Parent Update**: Updating node instead of root in union
   ```java
   // WRONG: Update x directly
   parent[x] = parent[y];

   // CORRECT: Update roots
   int rootX = find(x), rootY = find(y);
   parent[rootX] = rootY;
   ```

6. **Confusing Rank with Size**:
   - Rank = approximate tree height (only increases when merging equal ranks)
   - Size = actual node count (always increases by merged size)

**How to Optimize:**
- **Always use path compression** in find operation
- **Union by size/rank** to keep trees balanced
- **Track component count** for quick queries
- **Use iterative find** to avoid recursion overhead

**Space vs Time Tradeoffs:**
- Basic UF: O(n) space, O(n) time per operation
- With optimizations: O(n) space, O(α(n)) ≈ O(1) time per operation
- α(n) is inverse Ackermann function, effectively constant for practical inputs

**Key Patterns:**
1. **Cycle Detection**: `if (find(x) == find(y)) return false; // cycle`
2. **Component Counting**: Track and decrement count on successful unions
3. **2D Grid to 1D**: Use `row * cols + col` for coordinate conversion
4. **Dummy Nodes**: Connect boundary elements to virtual node for easier processing
5. **Weighted Relationships**: Store ratios/distances for equation-like problems

**When NOT to use Union Find:**
- Static graphs where DFS/BFS suffice
- **A `parent[]` array that is already a tree** (e.g. `parent[0] = -1`, LC 4015 Weighted Sum of a
  Tree). It *looks* like a DSU array, but with no `union()` there is nothing to merge — just memoize
  `depth[x] = depth[parent[x]] + 1` and climb. Same walk as `find()` with path compression, none of
  the bookkeeping. See [dfs_advanced.md → Template 11](./dfs_advanced.md#template-11-parent-array-tree--memoized-upward-depth--lc-4015).
- Need shortest paths (use Dijkstra/Floyd-Warshall)
- Directed graph strongly connected components (use Tarjan's)
- Small graphs where simple adjacency checks work


## Worked Examples

Nineteen problems live in **[union_find_examples.md](./union_find_examples.md)**, grouped by
what a node represents rather than by problem number:

| Group | Problems |
|---|---|
| [Cycle detection & redundancy](./union_find_examples.md#cycle-detection--redundancy) | LC 684, 685, 990 |
| [Component counting & connectivity](./union_find_examples.md#component-counting--connectivity) | LC 547, 261, 323, 1319, 2316, 128 |
| [Grids](./union_find_examples.md#grids) | LC 200, 130, 827 |
| [Weighted, sorted-edge & offline variants](./union_find_examples.md#weighted-sorted-edge--offline-variants) | LC 399, 1631, 803 |
| [Union-find on other structures](./union_find_examples.md#union-find-on-other-structures) | LC 721, 1202, 947, 865 |
