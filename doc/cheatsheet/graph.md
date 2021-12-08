# Grpah 

## 0) Concept  

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/graph_processing_problem.png"></p>

### 0-1) Types

- Types
    - Quick union
    - Union Find
    - Topology sorting
    - Graph Bipartite
    - Dijkstra algorithm
    - DirectedEdge
    - Directed acyclic graph (DAG)
    - Route relaxation

- Algorithm
    - bfs
    - dfs
    - brute force
    - graph Algorithm

- Data structure
    - TreeNode
    - array
    - string
    - dict
    - set

### 0-2) Pattern
```python
def GraphPattern(node1, node2, n):
    def dfs(node):
        # dfs logic
    def bfs(node):
        # bfs logic
    def union_find(node):
    # union_find logic
    import collections.defaultdict
    # build the graph
    graph = defaultdict(set)
    for n1, n2 in zip(node1, node2):
        graph[n1].append(n2)
        graph[n2].append(n1)

    # go through the graph with one of the algorithms above
    visited = []
    for i in range(n):
        if conditions:
            # do sth
            visited.append(node)
        else:
            # do sth

```

```python
class Graph(object):

    def __init__(self):
        self.numberOfNodes = 0
        self.adjacentList = {}

    def addVertex(self, node):
        self.adjacentList[node] = []
        self.numberOfNodes += 1 

    def addEdge(self, node1, node2):
        self.adjacentList[node1].append(node2)
        self.adjacentList[node2].append(node1)

    def showConnections(self):
        allNodes = self.adjacentList.keys()
        for node in allNodes:
            nodeConnections = self.adjacentList[node]
            connections = ""
            for vertex in nodeConnections:
                connections += vertex + " "
            print (node + "-->" + connections)         
```

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

### 2-2) alien-dictionary
```python
# 269 alien-dictionary
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, zero_in_degree_queue, in_degree, out_degree = [], collections.deque(), {}, {}
        nodes = sets.Set()
        for word in words:
            for c in word:
                nodes.add(c)
         
        for i in range(1, len(words)):
            if len(words[i-1]) > len(words[i]) and \
                words[i-1][:len(words[i])] == words[i]:
                    return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)
         
        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)
         
        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)
             
            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)
             
                del out_degree[precedence]
         
        if out_degree:
            return ""
 
        return "".join(result)
 
 
    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in range(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = sets.Set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = sets.Set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break　　
```