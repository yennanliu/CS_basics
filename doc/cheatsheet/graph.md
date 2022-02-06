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
                cur = q.pop(0)
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