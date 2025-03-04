# Union Find
> Check "connection status" for nodes in graphs

- How many connections ?
- Whether 2 nodes are "connected" (with same parent)
- Is a tree has `cycle` ?
    - is graph a valid tree
- Ref
    - [Union find basic](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/UnionFind%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3.md)
    - [Union find use cases](https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/UnionFind%E7%AE%97%E6%B3%95%E5%BA%94%E7%94%A8.md)

## 0) Concept
- Implementation 
    - build 3 API:
        - `Union`
        - `connected`
        - find : once "route compression" is applied, then union and connected API will be `O(1)` time complexity
    - parent : array, record "parent node"
    - size : array, record "weight of each node"
- Use case
    - DFS replacement
    - graph find "connection"

### 0-1) Types

- Equation calculation
    - LC 399

### 0-2) Pattern

## 1) General form
```java
// java

// LC 684
public class UnionFind3 {
  // Union-find data structure
  int[] parents;
  int[] size;
  int n;

  // Constructor to initialize the union-find data structure
  public UnionFind3(int[][] edges) {
    HashSet<Integer> set = new HashSet<>();
    for (int[] x : edges) {
      set.add(x[0]);
      set.add(x[1]);
    }
    this.n = set.size();

    // Initialize parent and size arrays
    this.parents = new int[n + 1]; // Using 1-based indexing
    this.size = new int[n + 1];
    for (int i = 1; i <= n; i++) {
      this.parents[i] = i;
      this.size[i] = 1;
    }
  }

  // Find the root of the set containing 'x' with path compression
  public int getParent(int x) {
    /**
     * NOTE !!!
     *
     *  we use `!=` logic below to simplify code
     */
    if (x != this.parents[x]) {
      // Path compression: recursively find the parent and update the current node's
      // parent
      /**
       *  NOTE !!!
       *
       *  we should update parent as `getParent(this.parents[x])`,
       *  e.g. -> use `this.parents[x]` as parameter, send into getParent method,
       *       -> then assign result to this.parents[x]
       *
       */
      this.parents[x] = getParent(this.parents[x]);
    }
    return this.parents[x];
  }

  // Union the sets containing x and y, return false if they are already connected
  public boolean union(int x, int y) {
    int rootX = getParent(x);
    int rootY = getParent(y);

    // If they are already in the same set, a cycle is detected
    if (rootX == rootY) {
      return false;
    }

    // Union by size: attach the smaller tree to the root of the larger tree
    if (size[rootX] < size[rootY]) {
      parents[rootX] = rootY;
      size[rootY] += size[rootX];
    } else {
      parents[rootY] = rootX;
      size[rootX] += size[rootY];
    }
    return true;
  }
}

```

```java
// Java
// LC 261
    class UF {
        int[] parents; // Parent array

        public UF(int n) {
            this.parents = new int[n];
            for (int i = 0; i < n; i++) {
                this.parents[i] = i; // Each node is its own parent initially
            }
        }

        // Union operation
        public boolean union(int a, int b) {
            int aParent = find(a);
            int bParent = find(b);

      /**
       *  NOTE !!!
       *
       *   If find(a) == find(b), it means they are already `CONNECTED`,
       *   -> `and adding the edge` would form a `CYCLE` (e.g. if we do `parents[aParent] = bParent`)
       *   -> so return `false` to prevent it.
       *
       *   -> e.g.  if `aParent == bParent`
       *   -> means node a, b ALREADY connected each other
       *   -> if we still go ahead and connect them in the other way
       *   -> will form a CYCLE, which is NOT a VALID TREE
       *
       *    e.g.
       *
       *   before
       *        a - c - b
       *
       *
       *  after
       *       a - c - b
       *       | ----- |      (will form a cycle)
       *
       */
      /**
       *  Example
       *
       *   # Detecting a Cycle in a Graph
       *
       * ## Example: Cycle Detection
       *
       * Given a graph with `n = 5` nodes and the following edges:
       *
       * ```
       * edges = [[0,1], [1,2], [2,3], [1,3]]
       * ```
       *
       * ### Step-by-Step Process
       *
       * 1. **Initial State**
       *    - Every node is its own parent:
       *    ```
       *    parents = [0, 1, 2, 3, 4]
       *    ```
       *
       * 2. **Processing Edges**
       *
       *    - **Edge [0,1]**
       *      - `find(0) = 0`, `find(1) = 1` → Different sets, merge:
       *      ```
       *      parents = [1, 1, 2, 3, 4]
       *      ```
       *
       *    - **Edge [1,2]**
       *      - `find(1) = 1`, `find(2) = 2` → Merge
       *      ```
       *      parents = [1, 2, 2, 3, 4]
       *      ```
       *
       *    - **Edge [2,3]**
       *      - `find(2) = 2`, `find(3) = 3` → Merge
       *      ```
       *      parents = [1, 2, 3, 3, 4]
       *      ```
       *
       *    - **Edge [1,3]**
       *      - `find(1) = 3`, `find(3) = 3` → Same parent (Cycle detected!)
       *      ```
       *      Cycle detected! Returning false.
       *      ```
       *
       *  ### **Result**
       *    A **cycle is detected** in the graph, so the function returns **false**.
       */
      if (aParent == bParent) {
                return false; // Cycle detected
            }

            // Simple union without rank
            parents[aParent] = bParent;
            return true;
        }

        // Find with path compression
        public int find(int a) {
            if (parents[a] != a) {
                parents[a] = find(parents[a]); // Path compression
            }
            return parents[a];
        }
    }
```

```python
#--------------------------
# UNION FIND V1 (basic)
#--------------------------
class UnionFind:

    def __init__(self, n):
        self.count = n
        self.parent = [None] * n
        self.size = [None] * n

        for i in range(len(n)):
            self.parent[i] = i
            self.size = 1

    def union(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        if rootP == rootQ:
            return
        #self.parent[rootQ] = rootP  # this is OK as well
        self.parent[rootP] = rootQ
        self.count -= 1

    def connected(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        return rootP == rootQ

    def find(self, x):
        while self.parent[x] != x:
            x = parent[x]
        return x

    def count(self):
        return self.count
```

```python
#-------------------
# UNION FIND V2
#-------------------
class UnionFind(object):
    def __init__(self, n):
        self.set = range(n)
        self.count = n
 
    def find_set(self, x):
       if self.set[x] != x:
           self.set[x] = self.find_set(self.set[x])  # path compression.
       return self.set[x]
 
    def union_set(self, x, y):
        x_root, y_root = map(self.find_set, (x, y))
        if x_root != y_root:
            self.set[min(x_root, y_root)] = max(x_root, y_root)
            self.count -= 1
            
class Solution(object):
    def countComponents(self, n, edges):
        union_find = UnionFind(n)
        for i, j in edges:
            union_find.union_set(i, j)
        return union_find.count
```

### 1-1) Basic OP

## 2) LC Example

### 2-1) Surrounded Regions
```python
# LC 130 Surrounded Regions
# NOTE : there is also bfs, dfs approaches
# V1
# IDEA : UNION FIND
# https://leetcode.com/problems/surrounded-regions/discuss/1764075/Python-or-Union-Find
class DSU:
    def __init__(self, n):
        self.root = list(range(n))
        self.rank = [0]*n
    
    def find(self, x):
        if self.root[x] != x:
            self.root[x] = self.find(self.root[x])
        return self.root[x]
    
    def union(self, x, y):
        rx, ry = self.find(x), self.find(y)
        
        if rx == ry:
            return False
        
        if self.rank[rx] == self.rank[ry]:
            self.rank[ry] += 1
        
        if self.rank[rx]  < self.rank[ry]:
            self.root[rx] = ry
        else:
            self.root[ry] = rx
        
        return True
        
class Solution:
    def solve(self, board):
        """
        Do not return anything, modify board in-place instead.
        """
        
        #use union find to group the Os
        #the Os on the edge has higher rank
        #in the end, check each O cell, if the root of the cell is not in the edge, flip to x
        
        m, n = len(board), len(board[0])
        dsu = DSU(m*n)
        edges = set()
        for i in range(m):
            for j in range(n):
                if i == 0 or i == m - 1 or j == 0 or j == n - 1:
                    dsu.rank[n*i + j] = float("inf")
                    edges.add(n*i + j)
        
        for i in range(m):
            for j in range(n):
                if board[i][j] == "X": continue
                for ni, nj in [(i + 1, j), (i - 1, j), (i, j + 1), (i, j - 1)]:
                    if 0 <= ni < m and 0 <= nj < n and board[ni][nj] == "O":
                        dsu.union(n*i + j, n*ni + nj)
        
        
        for i in range(m):
            for j in range(n):
                if dsu.find(n*i + j) not in edges:
                    board[i][j] = "X"
        
        return board

# V1
# IDEA : UNION FIND
# https://leetcode.com/problems/surrounded-regions/discuss/1371795/python3-%2B-Union-Find
class Solution:
    def solve(self, board):
        """
        Do not return anything, modify board in-place instead.
        """
        f = {} #dic index : root
        
        def find(x):
            f.setdefault(x, x)
            if f[x] != x:
                f[x] = find(f[x])
            return f[x]
        
        def union(x, y):
            f[find(y)] = find(x)
      
        if not board or not board[0]:
            return
        row = len(board)
        col = len(board[0])
        dummy = row * col
        #dummy is Point O colletion dont' need to be changed(need to be remained)
        for i in range(row):
            for j in range(col):
                if board[i][j] == "O":
                    if i == 0 or i == row - 1 or j == 0 or j == col - 1:
                        union(i * col + j, dummy)#need to be remained O
                    else:
                        for x, y in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                            if board[i + x][j + y] == "O":#union all connected O
                                union(i * col + j, (i + x) * col + (j + y))
        for i in range(row):
            for j in range(col):
                if find(dummy) == find(i * col + j):#Point O colletion dont' need to be changed 
                    board[i][j] = "O"
                else:
                    board[i][j] = "X"
```

### 2-2) Graph Valid Tree
```python
# LC 261 Graph Valid Tree
```

```java
// java
// LC 261 Graph Valid Tree
    // V0'
    // IDEA : QUICK FIND (gpt)
    public boolean validTree_0(int n, int[][] edges) {
        if (n == 0) {
            return false;
        }

        /**
         * Step 1) Initialize root array where each node is its own parent
         *
         *  NOTE !!!
         *   we init an array with n length (NOT from edges)
         */
        int[] root = new int[n];
        for (int i = 0; i < n; i++) {
            root[i] = i;
        }

        /**
         * Step 2) update relation (union find)
         */
        // Process each edge
        for (int[] edge : edges) {

            /**
             *  NOTE !!!
             *
             *    find node "parent" with 2 perspective
             *     1) from 1st element (e.g. edge[0])
             *     2) from 2nd element (e.g. edge[1])
             *
             *    so, if parent1 == parent2
             *     -> means there is a circular (because they have same parent, so nodes must "connect itself back" at some point),
             *     -> so input is NOT a valid tree
             */
            int root1 = find(root, edge[0]); // parent1
            int root2 = find(root, edge[1]); // parent2

            // If the roots are the same, there's a cycle
            if (root1 == root2) {
                /** NOTE !!!  if a cycle, return false directly */
                return false;
            } else {
                // Union the sets
                /** NOTE !!!  if not a cycle, then "compress" the route, e.g. make node as the other node's parent */
                root[root1] = root2;
            }
        }

        /** Check if the number of edges is exactly n - 1 */
        return edges.length == n - 1; // NOTE !!! this check
    }

    // Find function with path compression
    private int find(int[] root, int e) {
        if (root[e] == e) {
            return e;
        } else {
            root[e] = find(root, root[e]); // Path compression
            return root[e];
        }
    }
```

### 2-3) Possible Bipartition
```python
# LC 886 Possible Bipartition
```

### 2-4) Evaluate Division

```java
// java
// LC 399

// V4
// IDEA: UNION FIND (gpt)
class UnionFind {
    private Map<String, String> parent;
    private Map<String, Double> ratio;

    public UnionFind() {
        this.parent = new HashMap<>();
        this.ratio = new HashMap<>();
    }

    // Finds the root of a node and applies path compression
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

    // Union two nodes with the given value
    public void union(String x, String y, double value) {
        String rootX = find(x);
        String rootY = find(y);

        if (!rootX.equals(rootY)) {
            parent.put(rootX, rootY);
            ratio.put(rootX, value * ratio.get(y) / ratio.get(x));
        }
    }

    // Get the ratio between two nodes if they are connected
    public double isConnected(String x, String y) {
        if (!parent.containsKey(x) || !parent.containsKey(y)) {
            return -1.0;
        }

        String rootX = find(x);
        String rootY = find(y);

        if (!rootX.equals(rootY)) {
            return -1.0;
        }

        return ratio.get(x) / ratio.get(y);
    }
}

public double[] calcEquation_4(List<List<String>> equations, double[] values, List<List<String>> queries) {
    UnionFind uf = new UnionFind();

    // Build the union-find structure
    for (int i = 0; i < equations.size(); i++) {
        String a = equations.get(i).get(0);
        String b = equations.get(i).get(1);
        double value = values[i];
        uf.union(a, b, value);
    }

    // Process the queries
    double[] results = new double[queries.size()];
    for (int i = 0; i < queries.size(); i++) {
        String c = queries.get(i).get(0);
        String d = queries.get(i).get(1);
        results[i] = uf.isConnected(c, d);
    }

    return results;
}
```

### 2-5) Friend Circles
```python
# LC 547 Friend Circles
# NOTE !! there are also BFS, DFS approaches
class UnionFind(object):
    def __init__(self, n):
        self.n = n
        self.parent = [-1]*n
        for i in range(n):
            self.parent[i] = i
    
    def find(self, i):
    #Path Compression
        if self.parent[i] != i:
            self.parent[i] = self.find(self.parent[i])
        return self.parent[i]
    
    def union(self, x, y):
        xroot = self.find(x)
        yroot = self.find(y)
        if xroot != yroot:
            self.parent[yroot]= xroot
    
    def get_count(self):
        total = set()
        print(self.parent)
        for i in range(self.n):
            total.add(self.find(i))
        print("total", total)
        return len(total)


class Solution:
    def findCircleNum(self, M):
        """
        :type M: List[List[int]]
        :rtype: int
        """
        #Union-Find Solution
        n = len(M[0])
        uf = UnionFind(n)
        
        for r in range(len(M)):
            for c in range(len(M[0])):
                if M[r][c] == 1:
                    uf.union(r,c)
        
        return uf.get_count()
```

### 2-6) Array Nesting
```python
# LC 565. Array Nesting
# V0
# IDEA : UNION FIND
class Solution(object):
    def arrayNesting(self, nums):
        def search(idx):
            cnt = 0
            while nums[idx] >= 0:
                cnt += 1
                next = nums[idx]
                nums[idx] = -1
                idx = next
            return cnt
        ans = 0
        for x in range(len(nums)):
            if nums[x] >= 0:
                ans = max(ans, search(x))
        return ans
```

### 2-7) Sentence Similarity II
```python
# LC 737. Sentence Similarity II
# NOTE : there is also DFS, BFS approaches
# V0'''
# IDEA : UNION FIND
class Solution(object):
    def areSentencesSimilarTwo(self, words1, words2, pairs):
        if len(words1) != len(words2):
            return False
        
        parent = dict()
        
        def add(x):
            if x not in parent:
                parent[x] = x
                
        def find(x):
            if x == parent[x]:
                return x
            parent[x] = find(parent[x])
            return parent[x]
        
        def union(x, y):
            parentX = find(x)
            parentY = find(y)
            if parentX == parentY:
                return
            parent[parentY] = parentX
            
        for a, b in pairs:
            add(a)
            add(b)
            union(a, b)
            
        # print parent
        for word1, word2 in zip(words1, words2):
            # print word1, word2
            if word1 == word2:
                continue
            if word1 not in parent or word2 not in parent:
                return False
            if find(word1) != find(word2):
                return False
        return True
```

### 2-8) Number of Connected Components in an Undirected Graph
```python
# LC 323 Number of Connected Components in an Undirected Graph
# NOTE : there is ALSO dfs, bfs approaches
# V0
# IDEA : UNION FIND
# union find basic algorithm
class UnionFind:

    def __init__(self, n):
        self.n = n
        self.parent = [x for x in range(n)]

    def union(self, x, y):
        #print (">>> union : x = {}, y = {}".format(x, y))
        parentX = self.find(x)
        parentY = self.find(y)
        """
        NOTE this !!!
            -> if parentX == parentY, we DO NOTHING
        """
        if parentX == parentY:
            return
        self.parent[parentX] = parentY
        self.n -= 1 

    def find(self, x):
        while x != self.parent[x]:
            x = self.parent[x]
        return x

    def connected(self, x, y):
        parentX = self.find(x)
        parentY = self.find(y)
        return parentX == parentY

    def count(self):
        return self.n

class Solution:
    def countComponents(self, n, edges):
        """
        build union find

            step 1) init class (uf)
            step 2) union all (a, b) in edges
            step 3) return uf.count
        """
        uf = UnionFind(n)
        for a, b in edges:
            uf.union(a, b)
        return uf.count()
```

### 2-9) Connecting Cities With Minimum Cost
```python
# LC 1135. Connecting Cities With Minimum Cost
# note : there is also prime, Kruskal approaches
# V1
# IDEA : UNION FIND
# https://leetcode.com/problems/connecting-cities-with-minimum-cost/discuss/831263/Python-very-Concise-Union-Find
class Solution:
    def minimumCost(self, N: int, connections: List[List[int]]) -> int:
        parents = [x for x in range(N)]
        
        def find(x):
            if parents[x] != x: parents[x] = find(parents[x])
            return parents[x]
        
        def union(u, v):
            if find(u) == find(v): return False
            parents[find(v)] = find(u)
            return True

        connections.sort(key = lambda x: x[2])
        ans = 0
        for u, v, val in connections:
            if union(u-1, v-1): ans += val
        return ans if len(set(find(x) for x in parents)) == 1 else -1
```