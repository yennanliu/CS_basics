# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/88363919
# IDEA : DFS 
"""
# Definition for a Node.
class Node(object):
    def __init__(self, val, neighbors):
        self.val = val
        self.neighbors = neighbors
"""
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

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/88363919
# IDEA : BFS 
"""
# Definition for a Node.
class Node(object):
    def __init__(self, val, neighbors):
        self.val = val
        self.neighbors = neighbors
"""
class Solution(object):
    def cloneGraph(self, node):
        """
        :type node: Node
        :rtype: Node
        """
        que = collections.deque()
        hashd = dict()
        que.append(node)
        node_copy = Node(node.val, [])
        hashd[node] = node_copy
        while que:
            t = que.popleft()
            if not t: continue
            for n in t.neighbors:
                if n not in hashd:
                    hashd[n] = Node(n.val, [])
                    que.append(n)
                hashd[t].neighbors.append(hashd[n])
        return node_copy

# V1''
# https://www.jiuzhang.com/solution/clone-graph/#tag-highlight-lang-python
# IDEA : DFS 
"""
Definition for a undirected graph node
class UndirectedGraphNode:
    def __init__(self, x):
        self.label = x
        self.neighbors = []
"""
class Solution:

    def __init__(self):
        self.dict = {}
        
    """
    @param: node: A undirected graph node
    @return: A undirected graph node
    """
    def cloneGraph(self, node):
        if node is None:
            return None
            
        if node.label in self.dict:
            return self.dict[node.label]
            
        root = UndirectedGraphNode(node.label)
        self.dict[node.label] = root
        for item in node.neighbors:
            root.neighbors.append(self.cloneGraph(item))
        return root

# V1''' 
# https://www.jiuzhang.com/solution/clone-graph/#tag-highlight-lang-python
# IDEA : BFS 
class Solution:
    def cloneGraph(self, node):
        root = node
        if node is None:
            return node
            
        # use bfs algorithm to traverse the graph and get all nodes.
        nodes = self.getNodes(node)
        
        # copy nodes, store the old->new mapping information in a hash map
        mapping = {}
        for node in nodes:
            mapping[node] = UndirectedGraphNode(node.label)
        
        # copy neighbors(edges)
        for node in nodes:
            new_node = mapping[node]
            for neighbor in node.neighbors:
                new_neighbor = mapping[neighbor]
                new_node.neighbors.append(new_neighbor)
        
        return mapping[root]
        
    def getNodes(self, node):
        q = collections.deque([node])
        result = set([node])
        while q:
            head = q.popleft()
            for neighbor in head.neighbors:
                if neighbor not in result:
                    result.add(neighbor)
                    q.append(neighbor)
        return result

# V2 
# Time:  O(n)
# Space: O(n)
class UndirectedGraphNode(object):
    def __init__(self, x):
        self.label = x
        self.neighbors = []

class Solution(object):
    # @param node, a undirected graph node
    # @return a undirected graph node
    def cloneGraph(self, node):
        if node is None:
            return None
        cloned_node = UndirectedGraphNode(node.label)
        cloned, queue = {node:cloned_node}, [node]

        while queue:
            current = queue.pop()
            for neighbor in current.neighbors:
                if neighbor not in cloned:
                    queue.append(neighbor)
                    cloned_neighbor = UndirectedGraphNode(neighbor.label)
                    cloned[neighbor] = cloned_neighbor
                cloned[current].neighbors.append(cloned[neighbor])
        return cloned[node]