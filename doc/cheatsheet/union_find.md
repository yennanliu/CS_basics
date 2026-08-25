# Union Find

> **Scope** — Disjoint set union — connectivity, component counting, cycle detection in **undirected** graphs, with path compression and union by rank.
> **See also**: [diff_toposort_quickunion.md](./diff_toposort_quickunion.md) — union-find vs toposort — which tool for which problem; [topology_sorting.md](./topology_sorting.md) — directed ordering; [graph.md](./graph.md) — general graph material.

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

### 0-4) Pattern-Specific Code Examples

#### Pattern 1: Basic Connectivity - Cycle Detection
**Problem: LC 261 - Graph Valid Tree**
```java
public boolean validTree(int n, int[][] edges) {
    // A tree must have exactly n-1 edges
    if (edges.length != n - 1) return false;

    UnionFind uf = new UnionFind(n);
    for (int[] edge : edges) {
        if (!uf.union(edge[0], edge[1])) {
            return false; // Cycle detected
        }
    }
    return true;
}
```

#### Pattern 2: Component Counting
**Problem: LC 323 - Number of Connected Components**
```java
public int countComponents(int n, int[][] edges) {
    UnionFind uf = new UnionFind(n);
    int components = n;

    for (int[] edge : edges) {
        if (uf.union(edge[0], edge[1])) {
            components--;
        }
    }
    return components;
}
```

#### Pattern 3: Find Redundant Edge (Cycle Detection)
**Problem: LC 684 - Redundant Connection**
```java
public int[] findRedundantConnection(int[][] edges) {
    UnionFind uf = new UnionFind(edges.length + 1);

    for (int[] edge : edges) {
        int x = edge[0], y = edge[1];
        if (!uf.union(x, y)) {
            return edge; // This edge creates a cycle
        }
    }
    return null;
}
```

#### Pattern 4: 2D Grid Connectivity
**Problem: LC 200 - Number of Islands**
```java
public int numIslands(char[][] grid) {
    int rows = grid.length, cols = grid[0].length;
    UnionFind uf = new UnionFind(rows * cols);
    int islands = 0;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            if (grid[r][c] == '1') {
                islands++;
                int idx = r * cols + c;

                // Check 4 directions
                int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                        && grid[nr][nc] == '1') {
                        int nidx = nr * cols + nc;
                        if (uf.union(idx, nidx)) {
                            islands--;
                        }
                    }
                }
            }
        }
    }
    return islands;
}
```

#### Pattern 5: Weighted Union Find (with Ratios)
**Problem: LC 399 - Evaluate Division**
```java
class WeightedUnionFind {
    Map<String, String> parent;
    Map<String, Double> ratio; // ratio[x] = x / parent[x]

    public WeightedUnionFind() {
        parent = new HashMap<>();
        ratio = new HashMap<>();
    }

    public String find(String x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x);
            ratio.put(x, 1.0);
        }
        if (!x.equals(parent.get(x))) {
            String originalParent = parent.get(x);
            parent.put(x, find(originalParent));
            ratio.put(x, ratio.get(x) * ratio.get(originalParent));
        }
        return parent.get(x);
    }

    public void union(String x, String y, double value) {
        String rootX = find(x);
        String rootY = find(y);
        if (!rootX.equals(rootY)) {
            parent.put(rootX, rootY);
            ratio.put(rootX, value * ratio.get(y) / ratio.get(x));
        }
    }

    public double query(String x, String y) {
        if (!parent.containsKey(x) || !parent.containsKey(y)) {
            return -1.0;
        }
        String rootX = find(x);
        String rootY = find(y);
        if (!rootX.equals(rootY)) return -1.0;
        return ratio.get(x) / ratio.get(y);
    }
}

public double[] calcEquation(List<List<String>> equations,
                              double[] values,
                              List<List<String>> queries) {
    WeightedUnionFind uf = new WeightedUnionFind();

    for (int i = 0; i < equations.size(); i++) {
        String a = equations.get(i).get(0);
        String b = equations.get(i).get(1);
        uf.union(a, b, values[i]);
    }

    double[] results = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {
        String c = queries.get(i).get(0);
        String d = queries.get(i).get(1);
        results[i] = uf.query(c, d);
    }
    return results;
}
```

#### Pattern 6: BFS + Union-Find Climb (Tree LCA)
**Problem: LC 865 - Smallest Subtree with all the Deepest Nodes**
- **Core Idea**: Use BFS to find all deepest-level nodes and build a parent map, then repeatedly union each node with its parent and move upward until all converge to a single root — that root is the LCA
- **When to Use**: Finding LCA of multiple target nodes in a tree when you prefer iterative bottom-up convergence over recursive post-order
- **Steps**:
  1. BFS to build `parent` map and identify `deepestNodes` (last level in BFS)
  2. Put all deepest nodes in a `Set`
  3. While set size > 1: replace each node with its parent (they converge upward)
  4. The single remaining node is the LCA
- **Key Insight**: This is essentially "climb from leaves to root" — all deepest nodes walk upward in lockstep until they meet at one ancestor
- **Trade-off vs DFS**: More intuitive for iterative thinkers; DFS post-order approach (return `(node, depth)` pair) is more concise and commonly used
- **Similar Problems**: LC 236 (LCA), LC 1123 (same as 865), LC 1644, LC 1650

```java
// LC 865 - BFS + Parent-Climb approach (simplified Union-Find concept)
// time = O(N), space = O(N)
public TreeNode subtreeWithAllDeepest(TreeNode root) {
    Map<TreeNode, TreeNode> parent = new HashMap<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    parent.put(root, null);
    List<TreeNode> level = new ArrayList<>();

    // Step 1: BFS to find deepest level + build parent map
    while (!q.isEmpty()) {
        int size = q.size();
        level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode cur = q.poll();
            level.add(cur);
            if (cur.left != null) { parent.put(cur.left, cur); q.offer(cur.left); }
            if (cur.right != null) { parent.put(cur.right, cur); q.offer(cur.right); }
        }
    }

    // Step 2: Climb upward until all deepest nodes converge
    Set<TreeNode> set = new HashSet<>(level);
    while (set.size() > 1) {
        Set<TreeNode> next = new HashSet<>();
        for (TreeNode node : set) next.add(parent.get(node));
        set = next;
    }
    return set.iterator().next();
}
```

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

## LC Examples

### 2-1) Redundant Connection (LC 684) — Union-Find Cycle Detection
> Add edges one by one; if two nodes are already connected, this edge is redundant.

```java
// LC 684 - Redundant Connection
// IDEA: Union-Find — detect cycle; redundant edge connects already-connected nodes
// time = O(N * α(N)), space = O(N)
public int[] findRedundantConnection(int[][] edges) {
    int n = edges.length;
    int[] parent = new int[n + 1];
    for (int i = 0; i <= n; i++) parent[i] = i;
    for (int[] edge : edges) {
        if (find(parent, edge[0]) == find(parent, edge[1])) return edge;
        union(parent, edge[0], edge[1]);
    }
    return new int[]{};
}
private int find(int[] parent, int x) {
    if (parent[x] != x) parent[x] = find(parent, parent[x]); // path compression
    return parent[x];
}
private void union(int[] parent, int x, int y) {
    parent[find(parent, x)] = find(parent, y);
}
```

```python
# LC 684 - Redundant Connection
# IDEA: Union-Find (dict-based, union by rank) — process edges; return first edge that forms a cycle
# time = O(N * α(N)), space = O(N)

class Solution(object):
    def findRedundantConnection(self, edges):
        uf = MyUF()
        for a, b in edges:
            if not uf.union(a, b):
                return [a, b]
        return []

class MyUF(object):
    def __init__(self):
        self.parent = {}
        self.rank = {}

    def get_parent(self, x):
        if x not in self.parent:       # lazy init: node becomes its own root on first seen
            self.parent[x] = x
            self.rank[x] = 0
        if self.parent[x] != x:
            self.parent[x] = self.get_parent(self.parent[x])  # path compression
        return self.parent[x]

    def union(self, a, b):
        rootA, rootB = self.get_parent(a), self.get_parent(b)
        if rootA == rootB:
            return False               # cycle detected — this edge is redundant
        if self.rank[rootA] < self.rank[rootB]:
            self.parent[rootA] = rootB
        elif self.rank[rootA] > self.rank[rootB]:
            self.parent[rootB] = rootA
        else:
            self.parent[rootB] = rootA
            self.rank[rootA] += 1
        return True
```

```python
# LC 684 - Redundant Connection (array-based variant, matches Java approach)
# IDEA: Union-Find (1-indexed array, path compression only) — simpler when nodes are 1..n
# time = O(N * α(N)), space = O(N)

class Solution(object):
    def findRedundantConnection(self, edges):
        n = len(edges)
        uf = UF(n)
        for u, v in edges:
            if not uf.union(u, v):
                return [u, v]
        return []

class UF(object):
    def __init__(self, n):
        self.parents = list(range(n + 1))   # 1-indexed; parents[i] = i initially

    def find(self, a):
        if self.parents[a] != a:
            self.parents[a] = self.find(self.parents[a])  # path compression
        return self.parents[a]

    def union(self, a, b):
        root_a, root_b = self.find(a), self.find(b)
        if root_a == root_b:
            return False               # already connected → cycle
        self.parents[root_a] = root_b
        return True
```

### 2-2) Number of Provinces (LC 547) — Count Connected Components
> Count the number of distinct roots after unioning all direct friendships.

```java
// LC 547 - Number of Provinces
// IDEA: Union-Find — count distinct components (roots)
// time = O(N^2 * α(N)), space = O(N)
public int findCircleNum(int[][] isConnected) {
    int n = isConnected.length;
    int[] parent = new int[n];
    for (int i = 0; i < n; i++) parent[i] = i;
    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++)
            if (isConnected[i][j] == 1) union(parent, i, j);
    int count = 0;
    for (int i = 0; i < n; i++) if (find(parent, i) == i) count++;
    return count;
}
private int find(int[] parent, int x) {
    if (parent[x] != x) parent[x] = find(parent, parent[x]);
    return parent[x];
}
private void union(int[] parent, int x, int y) {
    parent[find(parent, x)] = find(parent, y);
}
```

```python
# python
# LC 547 - Number of Provinces
# IDEA: Union-Find — union every direct friendship; `components` counter = answer
# time = O(N^2 * α(N)), space = O(N)
class Solution(object):
    def findCircleNum(self, isConnected):
        n = len(isConnected)
        uf = UnionFind(n)
        for i in range(n):
            for j in range(i + 1, n):
                if isConnected[i][j] == 1:
                    uf.union(i, j)
        return uf.components   # each successful union decrements the counter

# reuses the `UnionFind` (union by size) class from section 0-3
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.size = [1] * n
        self.components = n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        rx, ry = self.find(x), self.find(y)
        if rx == ry:
            return False
        if self.size[rx] < self.size[ry]:
            rx, ry = ry, rx
        self.parent[ry] = rx
        self.size[rx] += self.size[ry]
        self.components -= 1
        return True
```

### 2-3) Accounts Merge (LC 721) — Union-Find on Emails
> Union emails belonging to the same person; group by root; sort and format.

```java
// LC 721 - Accounts Merge
// IDEA: Union-Find — union all emails in same account; group by root
// time = O(N * M * α(N*M)), space = O(N*M)
public List<List<String>> accountsMerge(List<List<String>> accounts) {
    Map<String, String> parent = new HashMap<>();
    Map<String, String> emailToName = new HashMap<>();
    // init
    for (List<String> acc : accounts)
        for (int i = 1; i < acc.size(); i++) {
            parent.put(acc.get(i), acc.get(i));
            emailToName.put(acc.get(i), acc.get(0));
        }
    // union
    for (List<String> acc : accounts)
        for (int i = 2; i < acc.size(); i++)
            union(parent, acc.get(1), acc.get(i));
    // group by root
    Map<String, TreeSet<String>> groups = new HashMap<>();
    for (String email : parent.keySet())
        groups.computeIfAbsent(find(parent, email), k -> new TreeSet<>()).add(email);
    List<List<String>> result = new ArrayList<>();
    for (Map.Entry<String, TreeSet<String>> entry : groups.entrySet()) {
        List<String> list = new ArrayList<>();
        list.add(emailToName.get(entry.getKey()));
        list.addAll(entry.getValue());
        result.add(list);
    }
    return result;
}
private String find(Map<String, String> parent, String x) {
    if (!parent.get(x).equals(x)) parent.put(x, find(parent, parent.get(x)));
    return parent.get(x);
}
private void union(Map<String, String> parent, String x, String y) {
    parent.put(find(parent, x), find(parent, y));
}
```

> **Variation — LC 839 Similar String Groups**: same "union then group by root" shape, but the edges are *not given*. All strings are anagrams, so run the O(N² · L) pairwise check — `union(i, j)` iff `s[i] == s[j]` or they differ at **exactly 2** positions — then the answer is the component count.

### 2-4) Graph Valid Tree (LC 261) — Union-Find
> Tree has exactly N-1 edges and no cycle; union each edge, return false on same-component edge.

```java
// LC 261 - Graph Valid Tree
// IDEA: Union-Find — N-1 edges + no cycle = valid tree
// time = O(N * α(N)), space = O(N)
public boolean validTree(int n, int[][] edges) {
    if (edges.length != n - 1) return false;
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    for (int[] e : edges) {
        if (find(p, e[0]) == find(p, e[1])) return false;
        p[find(p, e[0])] = find(p, e[1]);
    }
    return true;
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 261 - Graph Valid Tree
# IDEA: Union-Find — a valid tree has exactly N-1 edges AND no cycle
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def validTree(self, n, edges):
        if len(edges) != n - 1:      # tree must have exactly n-1 edges
            return False
        parent = list(range(n))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        for a, b in edges:
            ra, rb = find(a), find(b)
            if ra == rb:             # both endpoints already connected => cycle
                return False
            parent[ra] = rb
        return True
```

### 2-5) Number of Connected Components in Undirected Graph (LC 323) — Union-Find
> Union each edge; count remaining distinct roots as connected components.

```java
// LC 323 - Number of Connected Components in Undirected Graph
// IDEA: Union-Find — count distinct roots after unioning all edges
// time = O(N * α(N)), space = O(N)
public int countComponents(int n, int[][] edges) {
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    int components = n;
    for (int[] e : edges) {
        int a = find(p, e[0]), b = find(p, e[1]);
        if (a != b) { p[a] = b; components--; }
    }
    return components;
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 323 - Number of Connected Components in Undirected Graph
# IDEA: Union-Find — start with n components, decrement on each successful union
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def countComponents(self, n, edges):
        parent = list(range(n))
        components = n

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        for a, b in edges:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                components -= 1               # two components merged into one
        return components
```

### 2-6) Surrounded Regions (LC 130) — Union-Find with Virtual Border Node
> Union all border 'O' cells to a virtual node; any 'O' not connected gets flipped to 'X'.

```java
// LC 130 - Surrounded Regions
// IDEA: Union-Find — connect border O cells to virtual node; flip disconnected O cells
// time = O(M*N), space = O(M*N)
public void solve(char[][] board) {
    int m = board.length, n = board[0].length, virtual = m * n;
    int[] p = new int[virtual + 1];
    for (int i = 0; i <= virtual; i++) p[i] = i;
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) if (board[i][j] == 'O') {
        int id = i * n + j;
        if (i == 0 || i == m-1 || j == 0 || j == n-1) union(p, id, virtual);
        else for (int[] d : dirs) {
            int ni = i+d[0], nj = j+d[1];
            if (board[ni][nj] == 'O') union(p, id, ni*n+nj);
        }
    }
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++)
        if (board[i][j] == 'O' && find(p, i*n+j) != find(p, virtual)) board[i][j] = 'X';
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```

> **Variation — LC 959 Regions Cut By Slashes**: the DSU node is *sub-cell*, not cell. Split every cell into 4 triangles (`0`=top, `1`=right, `2`=bottom, `3`=left, id = `4*(r*n+c)+k`). Inside a cell: `'/'` → union(0,3) & union(1,2); `'\'` → union(0,1) & union(2,3); `' '` → union all four. Across cells: union this cell's `1` with the right neighbor's `3`, and this cell's `2` with the bottom neighbor's `0`. Answer = component count.

> **Variation — LC 1559 Detect Cycles in 2D Grid**: only union each cell with its **right** and **down** neighbor when the letters match; if `find(a) == find(b)` before the union, a cycle exists (a grid cycle is automatically length ≥ 4). Same "union fails ⇒ cycle" test as LC 684, applied on a grid.

### 2-7) Smallest String with Swaps (LC 1202) — Union-Find + Sorting
> Union swap pairs; sort characters within each component; place sorted chars back.

```java
// LC 1202 - Smallest String with Swaps
// IDEA: Union-Find — group indices; sort chars in each group and reassign
// time = O(N log N), space = O(N)
public String smallestStringWithSwaps(String s, List<List<Integer>> pairs) {
    int n = s.length();
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    for (List<Integer> pair : pairs) union(p, pair.get(0), pair.get(1));
    Map<Integer, List<Integer>> groups = new HashMap<>();
    for (int i = 0; i < n; i++) groups.computeIfAbsent(find(p, i), k -> new ArrayList<>()).add(i);
    char[] res = s.toCharArray();
    for (List<Integer> idx : groups.values()) {
        char[] chars = new char[idx.size()];
        for (int i = 0; i < idx.size(); i++) chars[i] = s.charAt(idx.get(i));
        Arrays.sort(chars);
        Collections.sort(idx);
        for (int i = 0; i < idx.size(); i++) res[idx.get(i)] = chars[i];
    }
    return new String(res);
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```

### 2-8) Most Stones Removed with Same Row or Column (LC 947) — Union-Find
> Union stones in the same row or column; answer = stones − number of components.

```java
// LC 947 - Most Stones Removed with Same Row or Column
// IDEA: Union-Find — stones sharing row/column are in same component; remove all but one
// time = O(N^2 * α(N)), space = O(N)
public int removeStones(int[][] stones) {
    int n = stones.length;
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    for (int i = 0; i < n; i++)
        for (int j = i+1; j < n; j++)
            if (stones[i][0] == stones[j][0] || stones[i][1] == stones[j][1])
                union(p, i, j);
    Set<Integer> roots = new HashSet<>();
    for (int i = 0; i < n; i++) roots.add(find(p, i));
    return n - roots.size();
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```

> **Variation — LC 765 Couples Holding Hands**: union by **couple id** instead of person id — for each seat pair `(2i, 2i+1)` do `union(row[2i]/2, row[2i+1]/2)`. Answer = `n_couples − components` (a component of size `k` needs `k−1` swaps). Same "components → answer" arithmetic as LC 947.

### 2-9) Satisfiability of Equality Equations (LC 990) — Union-Find
> Process '==' edges first; then check '!=' pairs for contradiction.

```java
// LC 990 - Satisfiability of Equality Equations
// IDEA: Union-Find — union on ==, validate != pairs for contradiction
// time = O(N), space = O(26)
public boolean equationsPossible(String[] equations) {
    int[] p = new int[26];
    for (int i = 0; i < 26; i++) p[i] = i;
    for (String eq : equations)
        if (eq.charAt(1) == '=') union(p, eq.charAt(0)-'a', eq.charAt(3)-'a');
    for (String eq : equations)
        if (eq.charAt(1) == '!' && find(p, eq.charAt(0)-'a') == find(p, eq.charAt(3)-'a'))
            return false;
    return true;
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
private void union(int[] p, int x, int y) { p[find(p,x)] = find(p,y); }
```

```python
# python
# LC 990 - Satisfiability of Equality Equations
# IDEA: Union-Find — union all '==' pairs first, then verify no '!=' pair shares a root
# time = O(N), space = O(26)
class Solution(object):
    def equationsPossible(self, equations):
        parent = list(range(26))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        # pass 1: union every equality
        for eq in equations:
            if eq[1] == '=':
                parent[find(ord(eq[0]) - 97)] = find(ord(eq[3]) - 97)
        # pass 2: any inequality inside one component => contradiction
        for eq in equations:
            if eq[1] == '!' and find(ord(eq[0]) - 97) == find(ord(eq[3]) - 97):
                return False
        return True
```

### 2-10) Number of Operations to Make Network Connected (LC 1319) — Union-Find
> Need at least N-1 edges; count components; extra edges reconnect disconnected components.

```java
// LC 1319 - Number of Operations to Make Network Connected
// IDEA: Union-Find — count components; need (components-1) extra cables
// time = O(N * α(N)), space = O(N)
public int makeConnected(int n, int[][] connections) {
    if (connections.length < n - 1) return -1;
    int[] p = new int[n];
    for (int i = 0; i < n; i++) p[i] = i;
    int components = n;
    for (int[] c : connections) {
        int a = find(p, c[0]), b = find(p, c[1]);
        if (a != b) { p[a] = b; components--; }
    }
    return components - 1;
}
private int find(int[] p, int x) { return p[x]==x ? x : (p[x]=find(p,p[x])); }
```

```python
# python
# LC 1319 - Number of Operations to Make Network Connected
# IDEA: Union-Find — need >= n-1 cables; answer = (components - 1) redundant cables reused
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def makeConnected(self, n, connections):
        if len(connections) < n - 1:     # not enough cables to ever connect n nodes
            return -1
        parent = list(range(n))
        components = n

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])   # path compression
            return parent[x]

        for a, b in connections:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
                components -= 1
        # (components - 1) cables are needed to join the remaining components
        return components - 1
```

> **Variation — LC 1579 Remove Max Number of Edges to Keep Graph Fully Traversable**: run **two parallel DSUs** (Alice's and Bob's). Process type-3 (shared) edges **first**, unioning into both; then type-1 into Alice only, type-2 into Bob only. Count every edge whose `union()` returned `false` (redundant) — that count is the answer, but return `-1` unless both DSUs end with exactly 1 component.

> **Variation — LC 2076 Process Restricted Friend Requests**: *check before committing the union*. For each request `(a,b)`: if already connected → accept; otherwise scan all restrictions `(x,y)` and reject if `find(x)/find(y)` match `find(a)/find(b)` in either order; only union when no restriction is violated. O(Q · R · α(N)).

### 2-11) Longest Consecutive Sequence (LC 128) — HashSet O(N)
> For each number, only start counting if (num-1) is absent — marks sequence start.

```java
// LC 128 - Longest Consecutive Sequence
// IDEA: HashSet — only extend sequences from their start element
// time = O(N), space = O(N)
public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int n : nums) set.add(n);
    int longest = 0;
    for (int n : set) {
        if (!set.contains(n - 1)) {   // sequence start
            int len = 1;
            while (set.contains(n + len)) len++;
            longest = Math.max(longest, len);
        }
    }
    return longest;
}
```

### 2-12) Smallest Subtree with all the Deepest Nodes (LC 865) — BFS + Union-Find Climb
> BFS finds deepest nodes and parent map; then all deepest nodes "climb" upward via parents until they converge to the LCA. This is the same as LC 1123.

```java
// LC 865 - Smallest Subtree with all the Deepest Nodes
// IDEA: BFS to find deepest level + build parent map, then climb upward until convergence
// time = O(N), space = O(N)
public TreeNode subtreeWithAllDeepest(TreeNode root) {
    Map<TreeNode, TreeNode> parent = new HashMap<>();
    Queue<TreeNode> q = new LinkedList<>();
    q.offer(root);
    parent.put(root, null);
    List<TreeNode> level = new ArrayList<>();

    // BFS: build parent map, track each level (last level = deepest)
    while (!q.isEmpty()) {
        int size = q.size();
        level = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TreeNode cur = q.poll();
            level.add(cur);
            if (cur.left != null) { parent.put(cur.left, cur); q.offer(cur.left); }
            if (cur.right != null) { parent.put(cur.right, cur); q.offer(cur.right); }
        }
    }

    // Climb: replace each node with its parent until all converge
    Set<TreeNode> set = new HashSet<>(level);
    while (set.size() > 1) {
        Set<TreeNode> next = new HashSet<>();
        for (TreeNode node : set) next.add(parent.get(node));
        set = next;
    }
    return set.iterator().next();
}
```

### 2-13) Count Unreachable Pairs of Nodes (LC 2316) — Union-Find + Running-Remainder Pair Count
> Union all edges → group nodes into components → count pairs of nodes that live in **different** components (they are unreachable from each other).

Python ref: `leetcode_python/Depth-First-Search/count-unreachable-pairs-of-nodes-in-an-undirected-graph.py`

**Key Idea:** two nodes are *unreachable* ⟺ they belong to **different connected components**. So the answer = number of cross-component node pairs = `Σ (s_i · s_j)` over all pairs of components `i < j`, where `s_i` is the size of component `i`.

#### ⭐ The `remain` trick — O(k) cross-pair counting (no nested loop)

Naively you might:
- compute `C(n, 2)` (all pairs) then subtract intra-component pairs `Σ C(s_i, 2)`, **or**
- double-loop over every pair of components `s_i · s_j` → O(k²).

Instead, keep a running `remain` = "nodes not yet consumed" and accumulate in **one pass**:

```python
res = 0
remain = n
for s in size.values():
    remain -= s          # remain now = total nodes in the *remaining* components
    res += s * remain     # pair this component's s nodes with every node still ahead
return res
```

**Why it works** (avoids double-counting):

For component `i` (processed in order), after `remain -= s_i`, `remain = n − (s_1 + … + s_i) = Σ_{j>i} s_j`.
So each step adds `s_i · Σ_{j>i} s_j`. Summing over all `i`:

```text
Σ_i  s_i · (Σ_{j>i} s_j)  =  Σ_{i < j} s_i · s_j
```

which is exactly every cross-component pair, counted **once**. Subtracting first (`remain -= s` *before* multiplying) is what excludes the component's pairing with itself and prevents `(i, j)` / `(j, i)` duplicates.

**Visual trace** (example graph `n = 7, edges = [[0,2],[0,5],[2,4],[1,6],[5,4]]` → components of size `4, 2, 1`, expected `14`):

```text
remain = 7
s=4 → remain = 3 → res += 4*3 = 12   (res=12)
s=2 → remain = 1 → res += 2*1 = 2    (res=14)
s=1 → remain = 0 → res += 1*0 = 0    (res=14)  ✅
```
> The per-step values depend on iteration order, but the **total is invariant** (= `Σ_{i<j} s_i·s_j`).

#### Full solution

```python
# LC 2316 - Count Unreachable Pairs of Nodes in an Undirected Graph
# IDEA: Union-Find → component sizes → running-remainder cross-pair count
# time = O((N + E) * α(N)), space = O(N)
class MyUF:
    def __init__(self, n):
        self.parents = list(range(n))

    def get_parent(self, x):
        if self.parents[x] != x:
            self.parents[x] = self.get_parent(self.parents[x])  # path compression
        return self.parents[x]

    def union(self, x, y):
        px, py = self.get_parent(x), self.get_parent(y)
        if px != py:
            self.parents[py] = px

class Solution(object):
    def countPairs(self, n, edges):
        uf = MyUF(n)
        for x, y in edges:
            uf.union(x, y)

        # root -> component size (store the COUNT, not the node list)
        size = {}
        for i in range(n):
            root = uf.get_parent(i)
            size[root] = size.get(root, 0) + 1

        res, remain = 0, n
        for s in size.values():
            remain -= s          # remaining nodes ahead of this component
            res += s * remain     # cross-component pairs, counted once
        return res
```

```java
// LC 2316 - Count Unreachable Pairs of Nodes in an Undirected Graph
// IDEA: Union-Find → component sizes → running-remainder cross-pair count
// time = O((N + E) * α(N)), space = O(N)
public long countPairs(int n, int[][] edges) {
    int[] parent = new int[n], size = new int[n];
    for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
    for (int[] e : edges) union(parent, size, e[0], e[1]);

    long res = 0, remain = n;              // use long: pairs can exceed int range
    for (int i = 0; i < n; i++) {
        if (find(parent, i) == i) {        // i is a root → this component's size is size[i]
            remain -= size[i];
            res += (long) size[i] * remain;
        }
    }
    return res;
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
private void union(int[] p, int[] sz, int x, int y) {
    int rx = find(p, x), ry = find(p, y);
    if (rx == ry) return;
    if (sz[rx] < sz[ry]) { int t = rx; rx = ry; ry = t; }
    p[ry] = rx; sz[rx] += sz[ry];          // union by size keeps size[root] correct
}
```

**Gotchas:**
- **Store the count, not the nodes.** You only ever need `s_i`, so `size[root] += 1` beats collecting node lists → O(N) space, not O(N) per component.
- **Subtract before multiplying** (`remain -= s` then `res += s * remain`) — reversing the two lines would count each component against itself.
- **Watch overflow (Java).** With `n` up to `10^5`, cross-pairs approach `~5·10^9` > `Integer.MAX_VALUE`; use `long`.
- **Same trick, general use:** counting cross-group pairs given group sizes `[s_1..s_k]` is always `Σ_{i<j} s_i·s_j`, computable in one O(k) pass this way — handy far beyond Union-Find.

### 2-14) Path With Minimum Effort (LC 1631) — Sorted-Edge (Kruskal-style) Union-Find ⭐⭐⭐⭐⭐

> **Pattern:** "minimize the **maximum** edge on a path" / "smallest threshold that connects A and B".
> Sort all edges ascending, union them one by one, and stop the moment `find(src) == find(dst)`. The weight of the edge that just closed the connection **is** the answer — no binary search, no Dijkstra needed.

**Key Idea:** the DSU is a *monotone* connectivity oracle. Adding edges in increasing weight order means "the graph restricted to weights ≤ w"; the first `w` at which source and target join is by definition the minimum possible bottleneck. This is exactly Kruskal's MST sweep, stopped early.

**When to use:** the cost of a path is `max(edge)` (not `sum(edge)`), or the query is "are A and B connected using only edges ≤ limit".

```java
// java
// LC 1631 - Path With Minimum Effort
// IDEA: sorted-edge Union-Find (Kruskal sweep) — add edges cheapest-first; the edge that
//       first connects (0,0) with (m-1,n-1) is the minimum possible bottleneck
// time = O(M*N*log(M*N)), space = O(M*N)
public int minimumEffortPath(int[][] heights) {
    int m = heights.length, n = heights[0].length;

    // build one edge per adjacent cell pair: {weight, cellA, cellB}
    List<int[]> edges = new ArrayList<>();
    for (int r = 0; r < m; r++) {
        for (int c = 0; c < n; c++) {
            int id = r * n + c;
            if (r + 1 < m) edges.add(new int[]{Math.abs(heights[r][c] - heights[r + 1][c]), id, id + n});
            if (c + 1 < n) edges.add(new int[]{Math.abs(heights[r][c] - heights[r][c + 1]), id, id + 1});
        }
    }
    edges.sort((a, b) -> a[0] - b[0]);          // cheapest first

    int[] p = new int[m * n];
    for (int i = 0; i < m * n; i++) p[i] = i;

    for (int[] e : edges) {
        int ra = find(p, e[1]), rb = find(p, e[2]);
        if (ra != rb) p[ra] = rb;
        if (find(p, 0) == find(p, m * n - 1)) return e[0];   // just connected → this weight is the answer
    }
    return 0;                                   // single cell (no edges) → effort 0
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 1631 - Path With Minimum Effort
# IDEA: sorted-edge Union-Find (Kruskal sweep) — the first edge that joins start & end is the bottleneck
# time = O(M*N*log(M*N)), space = O(M*N)
class Solution(object):
    def minimumEffortPath(self, heights):
        m, n = len(heights), len(heights[0])
        parent = list(range(m * n))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])      # path compression
            return parent[x]

        edges = []                               # (weight, cellA, cellB)
        for r in range(m):
            for c in range(n):
                idx = r * n + c
                if r + 1 < m:
                    edges.append((abs(heights[r][c] - heights[r + 1][c]), idx, idx + n))
                if c + 1 < n:
                    edges.append((abs(heights[r][c] - heights[r][c + 1]), idx, idx + 1))
        edges.sort()                             # cheapest first

        for w, a, b in edges:
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb
            if find(0) == find(m * n - 1):       # start & end now connected using weights <= w
                return w
        return 0                                 # 1x1 grid
```

**Variations of this template**

> **LC 778 Swim in Rising Water** — the weight lives on the **cell**, not the edge. Heights are a permutation of `0..n*n-1`, so precompute `pos[height] = cellId`, then for `t = 0, 1, 2, ...` activate cell `pos[t]` and union it with the neighbours that are *already active*; return the first `t` where `find(0) == find(n*n-1)`. Same monotone sweep, O(N²·α) with no sort needed.

```java
// java
// LC 778 - Swim in Rising Water
// IDEA: same monotone sweep as LC 1631, but activate CELLS in increasing elevation
// time = O(N^2 * α(N^2)), space = O(N^2)
public int swimInWater(int[][] grid) {
    int n = grid.length, total = n * n;
    int[] pos = new int[total];                       // elevation -> cell id (heights are a permutation)
    for (int r = 0; r < n; r++)
        for (int c = 0; c < n; c++)
            pos[grid[r][c]] = r * n + c;

    int[] p = new int[total];
    for (int i = 0; i < total; i++) p[i] = i;
    boolean[] active = new boolean[total];
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

    for (int t = 0; t < total; t++) {
        int id = pos[t], r = id / n, c = id % n;
        active[id] = true;                            // water level t reaches this cell
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr < 0 || nr >= n || nc < 0 || nc >= n) continue;
            int nid = nr * n + nc;
            if (!active[nid]) continue;               // only merge with already-flooded cells
            int ra = find(p, id), rb = find(p, nid);
            if (ra != rb) p[ra] = rb;
        }
        if (find(p, 0) == find(p, total - 1)) return t;
    }
    return total - 1;
}
```

```python
# python
# LC 778 - Swim in Rising Water
# IDEA: activate cells in increasing elevation; answer = first time start & end are connected
# time = O(N^2 * α(N^2)), space = O(N^2)
class Solution(object):
    def swimInWater(self, grid):
        n = len(grid)
        total = n * n
        pos = [0] * total
        for r in range(n):
            for c in range(n):
                pos[grid[r][c]] = r * n + c

        parent = list(range(total))

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        active = [False] * total
        for t in range(total):
            idx = pos[t]
            r, c = divmod(idx, n)
            active[idx] = True
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < n and 0 <= nc < n and active[nr * n + nc]:
                    ra, rb = find(idx), find(nr * n + nc)
                    if ra != rb:
                        parent[ra] = rb
            if find(0) == find(total - 1):
                return t
        return total - 1
```

> **LC 1697 Checking Existence of Edge Length Limited Paths** — the **offline query** form of the same sweep: sort edges by weight AND sort the queries by `limit`, then walk both with one pointer, unioning every edge with `weight < limit` before answering `find(p) == find(q)`. Remember to restore the original query order when returning the answers.

### 2-15) Bricks Falling When Hit (LC 803) — Offline **Reverse** Union-Find ⭐⭐⭐⭐

> **Pattern:** the problem *deletes* things, but DSU can only *merge*. Fix: run time backwards — delete everything first, then **add the deletions back one at a time**.

**Key Idea:** a brick is stable iff it is connected to a virtual **roof** node (row 0). Erasing a brick is un-DSU-able, so:
1. Apply **all** hits up front (set those cells to 0).
2. Build the DSU on what survives, unioning row-0 bricks into the roof node `m*n`.
3. Walk the hits **in reverse**, re-adding each brick. The roof component's size jump `after − before − 1` (minus the restored brick itself) is exactly the number of bricks that fell for that hit.

**Requires union by size** so `size[find(roof)]` is meaningful.

```java
// java
// LC 803 - Bricks Falling When Hit
// IDEA: offline REVERSE union-find — erase every hit first, then undo them one by one;
//       bricks that fall on hit i == bricks that re-attach to the roof when hit i is undone
// time = O(M*N*α(M*N) + K*α(M*N)), space = O(M*N)
int[] p, sz;
public int[] hitBricks(int[][] grid, int[][] hits) {
    int m = grid.length, n = grid[0].length, roof = m * n;

    int[][] g = new int[m][];
    for (int i = 0; i < m; i++) g[i] = grid[i].clone();
    for (int[] h : hits) g[h[0]][h[1]] = 0;            // step 1: apply ALL hits up front

    p = new int[m * n + 1];
    sz = new int[m * n + 1];
    for (int i = 0; i <= m * n; i++) { p[i] = i; sz[i] = 1; }

    // step 2: build DSU on the surviving bricks (up/left neighbours suffice for a full scan)
    for (int r = 0; r < m; r++)
        for (int c = 0; c < n; c++)
            if (g[r][c] == 1) {
                if (r == 0) union(r * n + c, roof);
                if (r > 0 && g[r - 1][c] == 1) union(r * n + c, (r - 1) * n + c);
                if (c > 0 && g[r][c - 1] == 1) union(r * n + c, r * n + c - 1);
            }

    // step 3: undo hits in reverse order
    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    int[] res = new int[hits.length];
    for (int i = hits.length - 1; i >= 0; i--) {
        int r = hits[i][0], c = hits[i][1];
        if (grid[r][c] == 0) continue;                 // no brick was there → nothing falls
        int before = sz[find(roof)];
        g[r][c] = 1;                                   // put the brick back
        if (r == 0) union(r * n + c, roof);
        for (int[] d : dirs) {
            int nr = r + d[0], nc = c + d[1];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && g[nr][nc] == 1) union(r * n + c, nr * n + nc);
        }
        int after = sz[find(roof)];
        res[i] = Math.max(0, after - before - 1);      // -1: the restored brick itself never "fell"
    }
    return res;
}
private int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
private void union(int a, int b) {                     // union by SIZE — sz[root] must stay exact
    int ra = find(a), rb = find(b);
    if (ra == rb) return;
    if (sz[ra] < sz[rb]) { int t = ra; ra = rb; rb = t; }
    p[rb] = ra;
    sz[ra] += sz[rb];
}
```

```python
# python
# LC 803 - Bricks Falling When Hit
# IDEA: offline reverse union-find + virtual roof node + union by size
# time = O(M*N*α(M*N) + K*α(M*N)), space = O(M*N)
class Solution(object):
    def hitBricks(self, grid, hits):
        m, n = len(grid), len(grid[0])
        roof = m * n
        g = [row[:] for row in grid]
        for r, c in hits:
            g[r][c] = 0                          # apply ALL hits first

        parent = list(range(m * n + 1))
        size = [1] * (m * n + 1)

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return
            if size[ra] < size[rb]:
                ra, rb = rb, ra
            parent[rb] = ra
            size[ra] += size[rb]                 # union by size keeps size[root] exact

        for r in range(m):
            for c in range(n):
                if g[r][c] == 1:
                    if r == 0:
                        union(r * n + c, roof)   # row 0 hangs from the roof
                    if r > 0 and g[r - 1][c] == 1:
                        union(r * n + c, (r - 1) * n + c)
                    if c > 0 and g[r][c - 1] == 1:
                        union(r * n + c, r * n + c - 1)

        res = [0] * len(hits)
        for i in range(len(hits) - 1, -1, -1):   # undo hits backwards
            r, c = hits[i]
            if grid[r][c] == 0:
                continue                         # hit on empty cell → 0
            before = size[find(roof)]
            g[r][c] = 1
            if r == 0:
                union(r * n + c, roof)
            for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nr, nc = r + dr, c + dc
                if 0 <= nr < m and 0 <= nc < n and g[nr][nc] == 1:
                    union(r * n + c, nr * n + nc)
            after = size[find(roof)]
            res[i] = max(0, after - before - 1)  # max(0, ...) also handles duplicate hits
        return res
```

**Gotchas:**
- A hit on a cell that was already `0` in the *original* grid contributes `0` — check `grid[r][c]`, not `g[r][c]`.
- Duplicate hits on the same cell: the second (in reverse: first) restore is a no-op, and `max(0, after - before - 1)` correctly yields `0`.
- Reverse DSU only works when the deletions are known **offline** (all given up front).

### 2-16) Making A Large Island (LC 827) — Size-Aware DSU + Candidate Flip ⭐⭐⭐⭐

> **Pattern:** "connect components once, then evaluate every candidate merge in O(1)".
> Label all islands with a size-tracking DSU in one pass, then for each `0` cell sum the sizes of its **distinct neighbouring roots** (+1 for the flipped cell itself).

**Key Idea:** the `Set<root>` deduplication is the whole trick — two of the four neighbours may belong to the *same* island, and adding its size twice is the classic wrong answer.

```java
// java
// LC 827 - Making A Large Island
// IDEA: union all 1-cells with size bookkeeping, then for each 0-cell sum the DISTINCT
//       neighbouring component sizes + 1
// time = O(N^2 * α(N^2)), space = O(N^2)
int[] p, sz;
public int largestIsland(int[][] grid) {
    int n = grid.length, total = n * n;
    p = new int[total];
    sz = new int[total];
    for (int i = 0; i < total; i++) { p[i] = i; sz[i] = 1; }

    // pass 1: merge adjacent land cells (right + down is enough for a full scan)
    for (int r = 0; r < n; r++)
        for (int c = 0; c < n; c++)
            if (grid[r][c] == 1) {
                if (r + 1 < n && grid[r + 1][c] == 1) union(r * n + c, (r + 1) * n + c);
                if (c + 1 < n && grid[r][c + 1] == 1) union(r * n + c, r * n + c + 1);
            }

    int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
    int best = 0;
    // pass 2: try flipping every 0; also cover the "grid is all 1s" case
    for (int r = 0; r < n; r++) {
        for (int c = 0; c < n; c++) {
            if (grid[r][c] == 1) {
                best = Math.max(best, sz[find(r * n + c)]);   // no flip needed
                continue;
            }
            Set<Integer> roots = new HashSet<>();             // dedupe: neighbours may share an island
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && grid[nr][nc] == 1)
                    roots.add(find(nr * n + nc));
            }
            int totalSize = 1;                                // the flipped cell itself
            for (int root : roots) totalSize += sz[root];
            best = Math.max(best, totalSize);
        }
    }
    return best;
}
private int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
private void union(int a, int b) {
    int ra = find(a), rb = find(b);
    if (ra == rb) return;
    if (sz[ra] < sz[rb]) { int t = ra; ra = rb; rb = t; }
    p[rb] = ra;
    sz[ra] += sz[rb];
}
```

```python
# python
# LC 827 - Making A Large Island
# IDEA: size-tracking DSU over land cells, then evaluate each 0-flip via distinct neighbour roots
# time = O(N^2 * α(N^2)), space = O(N^2)
class Solution(object):
    def largestIsland(self, grid):
        n = len(grid)
        parent = list(range(n * n))
        size = [1] * (n * n)

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra == rb:
                return
            if size[ra] < size[rb]:
                ra, rb = rb, ra
            parent[rb] = ra
            size[ra] += size[rb]

        for r in range(n):
            for c in range(n):
                if grid[r][c] == 1:
                    if r + 1 < n and grid[r + 1][c] == 1:
                        union(r * n + c, (r + 1) * n + c)
                    if c + 1 < n and grid[r][c + 1] == 1:
                        union(r * n + c, r * n + c + 1)

        best = 0
        for r in range(n):
            for c in range(n):
                if grid[r][c] == 1:
                    best = max(best, size[find(r * n + c)])   # handles the all-1s grid
                    continue
                roots = set()                                  # MUST dedupe by root
                for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    nr, nc = r + dr, c + dc
                    if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] == 1:
                        roots.add(find(nr * n + nc))
                best = max(best, 1 + sum(size[root] for root in roots))
        return best
```

**Gotchas:**
- `size[x]` is only meaningful when `x` is a **root** — always index it with `find(...)`.
- Don't forget the all-land case (no `0` to flip): seeding `best` from existing component sizes covers it.

### 2-17) Redundant Connection II (LC 685) — DSU on a **Directed** Graph ⭐⭐⭐⭐

> **Twist on §2-1 (LC 684):** in a *directed* rooted tree the broken invariant can be either (a) a node with **two parents**, or (b) a **cycle**, or both. Plain "union fails ⇒ answer" is no longer enough.

**Key Idea — two-candidate elimination:**
1. Scan edges keeping `parent[v]`. If some `v` already has a parent, record `cand1 = (parent[v], v)` (earlier edge) and remember the index of the **later** edge `cand2`.
2. Re-run a plain DSU over all edges **skipping `cand2`**.
   - Cycle found and there was **no** two-parent node → return the edge that closed the cycle.
   - Cycle found and a two-parent node exists → `cand2` was innocent; return `cand1`.
   - No cycle → return `cand2`.

```java
// java
// LC 685 - Redundant Connection II
// IDEA: directed DSU — locate the two edges into a 2-parent node, drop the later one and
//       re-test with union-find; whether a cycle remains tells you which candidate to remove
// time = O(N * α(N)), space = O(N)
public int[] findRedundantDirectedConnection(int[][] edges) {
    int n = edges.length;
    int[] parent = new int[n + 1];      // parent[v] = u for edge u->v (0 = none yet)
    int[] cand1 = null;
    int dup = -1;                       // index of the LATER of the two edges into the same node

    for (int i = 0; i < n; i++) {
        int u = edges[i][0], v = edges[i][1];
        if (parent[v] != 0) {
            cand1 = new int[]{parent[v], v};   // the earlier in-edge
            dup = i;                            // the later in-edge (this one)
        } else {
            parent[v] = u;
        }
    }

    int[] p = new int[n + 1];
    int[] sz = new int[n + 1];
    for (int i = 0; i <= n; i++) { p[i] = i; sz[i] = 1; }

    for (int i = 0; i < n; i++) {
        if (i == dup) continue;                 // pretend the later in-edge doesn't exist
        int ru = find(p, edges[i][0]), rv = find(p, edges[i][1]);
        if (ru == rv) {
            // a cycle survives without cand2
            return cand1 == null ? edges[i]     // no 2-parent node → this edge closes the cycle
                                 : cand1;       // 2-parent node → the EARLIER edge is the culprit
        }
        // union by size — a plain `p[rv] = ru` can build an O(N) parent chain here
        if (sz[ru] < sz[rv]) { int t = ru; ru = rv; rv = t; }
        p[rv] = ru;
        sz[ru] += sz[rv];
    }
    return edges[dup];                          // no cycle → removing the later in-edge fixes it
}
private int find(int[] p, int x) { return p[x] == x ? x : (p[x] = find(p, p[x])); }
```

```python
# python
# LC 685 - Redundant Connection II
# IDEA: find the 2-parent node's two in-edges, remove the later one, then DSU-test for a cycle
# time = O(N * α(N)), space = O(N)
class Solution(object):
    def findRedundantDirectedConnection(self, edges):
        n = len(edges)
        par = [0] * (n + 1)          # par[v] = u for edge u->v
        cand1, dup = None, -1

        for i, (u, v) in enumerate(edges):
            if par[v] != 0:
                cand1 = [par[v], v]  # earlier in-edge
                dup = i              # later in-edge
            else:
                par[v] = u

        parent = list(range(n + 1))
        size = [1] * (n + 1)

        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])
            return parent[x]

        for i, (u, v) in enumerate(edges):
            if i == dup:
                continue             # skip the later in-edge
            ru, rv = find(u), find(v)
            if ru == rv:             # cycle survives without it
                return edges[i] if cand1 is None else cand1
            # union by size — plain `parent[rv] = ru` can build an O(N) chain,
            # which blows Python's recursion limit inside find() when N = 1000
            if size[ru] < size[rv]:
                ru, rv = rv, ru
            parent[rv] = ru
            size[ru] += size[rv]

        return edges[dup]            # no cycle → the later in-edge is redundant
```

**Comparison — LC 684 vs LC 685**

| | LC 684 (undirected) | LC 685 (directed) |
|---|---|---|
| Invariant broken | exactly one cycle | 2-parent node **or** cycle (or both) |
| Algorithm | union edges; first failure is the answer | find the 2 in-edges, drop the later, re-test with DSU |
| Answer when both cases apply | n/a | the **earlier** in-edge (`cand1`) |
| Passes over edges | 1 | 2 |