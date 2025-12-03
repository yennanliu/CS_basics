# Union Find

- Efficiently determines connectivity between nodes in dynamic graphs
- When to use it: Dynamic connectivity queries, cycle detection, MST algorithms, grouping elements
- Key LeetCode problems: Graph Valid Tree, Number of Islands, Accounts Merge, Friend Circles
- Data structures: Parent array, size/rank array for optimization
- States: Connected components, parent-child relationships

**Time Complexity:** Nearly O(1) per operation with optimizations

## 0) Concept

### 0-0) Key Optimizations
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

### 0-1) Types
- **Basic Connectivity**: Check if nodes are connected, count components
- **Cycle Detection**: Determine if adding edge creates cycle
- **Dynamic MST**: Kruskal's algorithm for minimum spanning trees
- **Weighted Union Find**: Handle ratios/weights between nodes (LC 399)
- **Grid Problems**: 2D grid connectivity (Number of Islands variants)

### 0-2) Algorithm Pattern / Template

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

### 0-3) Pattern-Specific Code Examples

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

## 1) Example Problems with Code References

#### Basic Connectivity & Component Counting
- **LC 200** – Number of Islands: Count connected components in 2D grid
  - Java: `leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfIslands.java:493`
  - Pattern: Grid to 1D conversion (`row * cols + col`), Union Find with 4-directional checks

- **LC 261** – Graph Valid Tree: Check if n-1 edges form exactly one component
  - Java: `leetcode_java/src/main/java/LeetCodeJava/BFS/GraphValidTree.java:36`
  - Pattern: Cycle detection, exactly n-1 edges validation

- **LC 323** – Number of Connected Components: Basic component counting
  - Java: `leetcode_java/src/main/java/LeetCodeJava/Graph/NumberOfConnectedComponentsUndirectedGraph.java:49`
  - Pattern: Track component count, decrement on successful union

#### Cycle Detection & Redundancy
- **LC 684** – Redundant Connection: Find edge that creates cycle in tree
  - Java: `leetcode_java/src/main/java/LeetCodeJava/Tree/RedundantConnection.java:50`
  - Pattern: Return first edge where `union()` fails (cycle detected)

#### Weighted Union Find
- **LC 399** – Evaluate Division: Weighted UF with ratios for equation solving
  - Java: `leetcode_java/src/main/java/LeetCodeJava/DFS/EvaluateDivision.java:421`
  - Pattern: Store ratios, path compression with ratio multiplication

#### Advanced Applications
- **LC 130** – Surrounded Regions: Use dummy node for boundary connected regions
- **LC 547** – Friend Circles: Find groups in friendship matrix
- **LC 721** – Accounts Merge: Group accounts by shared emails
- **LC 886** – Possible Bipartition: Detect bipartite graph conflicts
- **LC 1135** – Connecting Cities: MST with Kruskal's algorithm
- **LC 1319** – Network Connections: Minimum operations to connect all nodes

## 2) Diagrams

### Basic Union Operations
```
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
```
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
```
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
```
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
- Need shortest paths (use Dijkstra/Floyd-Warshall)
- Directed graph strongly connected components (use Tarjan's)
- Small graphs where simple adjacency checks work