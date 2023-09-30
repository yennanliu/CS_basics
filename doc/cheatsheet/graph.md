# Grpah 

## 0) Concept  

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_processing_problem.png"></p>

### 0-1) Types

- Types
    - Quick union
    - [Union Find](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/union_find.md)
    - [Topology sorting](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/topology_sorting.md)
        - LC 207, LC 210
    - Graph Bipartite
    - [Dijkstra](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/Dijkstra.md)
    - DirectedEdge
    - Directed acyclic graph (DAG)
    - Route relaxation
    - Route compression

- Algorithm
    - bfs
    - dfs
    - brute force
    - graph Algorithm
        - Dijkstra
        - Topology sorting
        - Union Find

- Data structure
    - defaultdict
    - TreeNode
    - array
    - string
    - dict
    - set

- Represent form (as below)
    - matrix 
    - dict + list

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_rep1.png"></p>

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_rep2.png"></p>

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP
- Graph API (client)
    - connect
    - check_if_connected
    - shortest_path
    - longest_path
    - is_cycle


#### 1-1-1) Number of Distinct Islands
    - LC 694
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
        dfs_1(grid, r - 1, c);
        dfs_1(grid, r + 1, c);
        dfs_1(grid, r, c - 1);
        dfs_1(grid, r, c + 1);
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
                    dfs_1(grid, r, c);
                }
            }
        }

        return num_islands;
    }

    ```


#### 1-1-2) Max Area of Island
    - LC 695
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

## 2) LC Example

### 2-1) Closest Leaf in a Binary Tree
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

### 2-2) Number of Connected Components in an Undirected Graph
```python
# LC 323 Number of Connected Components in an Undirected Graph
# V0
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

### 2-3) Clone Graph
```python
# LC 133. Clone Graph

# V0
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

# V0
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


### 2-4) Bus Routes
```python
# LC 815. Bus Routes
# V0
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