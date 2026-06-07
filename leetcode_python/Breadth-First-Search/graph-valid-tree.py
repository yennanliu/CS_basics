"""

261. Graph Valid Tree
Given n nodes labeled from 0 to n-1 and a list of undirected edges (each edge is a pair of nodes), write a function to check whether these edges make up a valid tree.

Example 1:

Input: n = 5, and edges = [[0,1], [0,2], [0,3], [1,4]]
Output: true
Example 2:

Input: n = 5, and edges = [[0,1], [1,2], [2,3], [1,3], [1,4]]
Output: false
Note: you can assume that no duplicate edges will appear in edges. Since all edges are undirected, [0,1] is the same as [1,0] and thus will not appear together in edges.

Difficulty:
Medium
Lock:
Prime
Company:
Adobe Amazon Facebook Google LinkedIn Pinterest Salesforce Zenefits


# https://leetcode.ca/all/261.html
# https://leetcode.ca/2016-08-17-261-Graph-Valid-Tree/


"""

# V0
# IDEA: UNION FIND
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BFS/GraphValidTree.java#L31
class Solution(object):
    def validTree(self, n, edges):

        # A valid tree must contain exactly n - 1 edges.
        #
        # Why?
        #
        # Too few edges:
        #   graph cannot be fully connected
        #
        # Too many edges:
        #   graph must contain a cycle
        #
        if len(edges) != n - 1:
            return False

        # Initialize Union-Find
        uf = UnionFind(n)

        # Process every edge
        for a, b in edges:

            # If union returns False,
            # a and b are already connected,
            # therefore adding this edge creates a cycle.
            if not uf.union(a, b):
                return False

        # No cycle found and edge count is n - 1
        return True


class UnionFind(object):

    def __init__(self, n):

        # Initially every node is its own parent.
        #
        # Example:
        #
        # n = 5
        #
        # parent =
        # [0, 1, 2, 3, 4]
        #
        self.parent = [i for i in range(n)]

    def find(self, x):

        # If x is not the root,
        # recursively find the root.
        if self.parent[x] != x:

            # Path Compression
            #
            # Example:
            #
            # 0 -> 1 -> 2 -> 3
            #
            # find(0)
            #
            # After compression:
            #
            # 0 -> 3
            # 1 -> 3
            # 2 -> 3
            #
            self.parent[x] = self.find(self.parent[x])

        return self.parent[x]

    def union(self, a, b):

        # Find root(parent representative)
        root_a = self.find(a)
        root_b = self.find(b)

        # If roots are equal,
        # a and b already belong to the same connected component.
        #
        # Adding another edge between them
        # would create a cycle.
        #
        # Example:
        #
        # 0 -- 1 -- 2
        # |__________|
        #
        if root_a == root_b:
            return False

        # Merge two sets.
        #
        # Simple version:
        #
        # root_a --> root_b
        #
        self.parent[root_a] = root_b

        return True



# V0-1
# IDEA: UNION FIND
class UF(object):
    def __init__(self, n):
        # Initialize each node as its own parent
        self.parents = [i for i in range(n)]

    # Find operation with optimized path compression
    def find(self, a):
        if self.parents[a] != a:
            # Recursively find the root and compress the path on the way back
            self.parents[a] = self.find(self.parents[a])
        return self.parents[a]

    # Union operation that returns False if a cycle is detected
    def union(self, a, b):
        aParent = self.find(a)
        bParent = self.find(b)
        
        # NOTE !!!
        # If find(a) == find(b), it means they are already CONNECTED.
        # Adding an extra edge between them would form a CYCLE!
        if aParent == bParent:
            return False # Cycle detected
            
        # Simple union step: hook one root to the other
        self.parents[aParent] = bParent
        return True


class Solution(object):
    def validTree(self, n, edges):
        # NOTE !!!
        # A valid tree must contain exactly `n - 1` edges.
        # If it doesn't, it is either disconnected or contains a cycle.
        if len(edges) != n - 1:
            return False
            
        # Instantiate the Union-Find data structure
        uf = UF(n)
        
        # Process every edge to connect nodes and inspect for cycles
        for u, v in edges:
            if not uf.union(u, v):
                return False # Cycle detected!
                
        # If no cycles are detected and edge count matches n-1, it's a valid tree
        return True


# V0-2
# IDEA: DFS
class Solution(object):
    def validTree(self, n, edges):
        # Math Optimization: A valid tree MUST have exactly (n - 1) edges.
        # If it doesn't, it either has a cycle or is disconnected!
        if len(edges) != n - 1:
            return False
            
        # Step 1: Build an UNDIRECTED adjacency list
        neighbors = {i: [] for i in range(n)}
        for a, b in edges:
            neighbors[a].append(b)
            neighbors[b].append(a)
            
        visited = set()
        
        # Step 2: Start a single DFS from node 0. 
        # Pass -1 as the initial dummy parent.
        if not self.helper(neighbors, visited, 0, -1):
            return False # Cycle detected!
            
        # Step 3: Check connectivity. Did we reach all nodes?
        return len(visited) == n

    def helper(self, neighbors, visited, cur, parent):
        # If we hit a node already in our visited set, we found a cycle!
        if cur in visited:
            return False
            
        # Mark the current node as visited
        visited.add(cur)
        
        # Explore all connected neighbors
        for neighbor in neighbors[cur]:
            # CRITICAL FIX: Skip the trivial edge going backward to the parent
            if neighbor == parent:
                continue
                
            # Recurse down, passing 'cur' as the parent for the next node
            if not self.helper(neighbors, visited, neighbor, cur):
                return False
                
        return True


# V0
# IDEA : DFS (NEED TO VALIDATE***)
# class Solution(object):
#     def validTree(self, n, edges):
#         ### NOTE : can use dict as well, but need to deal with "no such key" case
#         _edges = collections.defaultdict(list)
#         for i in range(len(edges)):
#             _edges[ edges[i][0] ].append( _edges[ edges[i][1]] )
#             _edges[ edges[i][1] ].append( _edges[ edges[i][0]] )
#         return self.dfs(n, edges, edges[0][0], [])
#
#     def dfs(self, n, _edges, key, visited):
#         if not _edges:
#             return
#         if not _edges && len(visited) == n:
#             return True
#         if key in visited:
#             return False
#         for key in _edges[key]:
#             self.dfs(_edges, key, visited.append(key))

# V0'
# IDEA : Quick Find
class Solution(object):
    def validTree(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: bool
        """
        root = [i for i in range(n)]
        for i in edges:
            root1 = self.find(root, i[0])
            root2 = self.find(root, i[1])
            if root1 == root2:
                return False
            else:
                root[root1] = root2
        return len(edges) == n - 1
        
    def find(self, root, e):
        if root[e] == e:
            return e
        else:
            return self.find(root, root[e])

# V0'
# IDEA : BFS
#       -> check if visited count is as same as "n"
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:        
        if len(edges) != n - 1:  # Check number of edges.
            return False
 
        # init node's neighbors in dict
        neighbors = collections.defaultdict(list)
        for u, v in edges:
            neighbors[u].append(v)
            neighbors[v].append(u)
 
        # BFS to check whether the graph is valid tree.
        visited = {}
        q = collections.deque([0])
        while q:
            curr = q.popleft()
            visited[curr] = True
            ### NOTE : bfs, but looping neighbors[curr], but NOT elements in queue
            for node in neighbors[curr]:
                if node not in visited:
                    visited[node] = True
                    q.append(node)
 
        return len(visited)==n

# V0''
# IDEA : DFS
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:        
        if len(edges) != n-1:
            return False
        memo = collections.defaultdict(list)
        for edge in edges:
            memo[edge[0]].append(edge[1])
            memo[edge[1]].append(edge[0])
        visited = [False]*n
        def helper(curr,parent):
            if visited[curr]:
                return False
            visited[curr] = True
            for val in memo[curr]:
                if val!= parent and not helper(val,curr):
                    return False
            return True
        if not helper(0,-1):
            return False
        for v in visited:
            if not v:
                return False
        return True

# V1 
# http://www.voidcn.com/article/p-hjukomuq-zo.html
# https://www.jianshu.com/p/1b954f99a497
# https://medium.com/@gxyou45/algorithm%E6%99%AE%E6%9E%97%E6%96%AF%E9%A0%93%E8%AA%B2%E7%A8%8B%E5%AD%B8%E7%BF%92%E7%AD%86%E8%A8%981-union-find-5af7911ca5ef
# IDEA : Quick-Union
class Solution(object):
    def validTree(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: bool
        """
        root = [i for i in range(n)]
        for i in edges:
            root1 = self.find(root, i[0])
            root2 = self.find(root, i[1])
            if root1 == root2:
                return False
            else:
                root[root1] = root2
        return len(edges) == n - 1
        
    def find(self, root, e):
        if root[e] == e:
            return e
        else:
            return self.find(root, root[e])

### Test case : dev

# V1'  
# http://www.voidcn.com/article/p-hjukomuq-zo.html
# https://www.jianshu.com/p/1b954f99a497
class Solution(object):
    def validTree(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: bool
        """
        if n == 1:
            return True
            
        if len(edges) == 0 or len(edges) < n - 1:
            return False    
        s = set()
        s.add(edges[0][0])
        s.add(edges[0][1])
        l = [s]
        for i in range(1, len(edges)):
            first = self.find(edges[i][0], l)
            second = self.find(edges[i][1], l)
            if first == -1 and second == -1:
                l.append(set([edges[i][0], edges[i][1]]))
            elif first == second:
                return False
            elif first != -1 and second != -1:
                if first > second:
                    temp = l[first]
                    del l[first]
                    l[second] = l[second] | temp
                else:
                    temp = l[second]
                    del l[second]
                    l[first] = l[first] | temp
            else:
                if first != -1:
                    l[first].add(edges[i][1])
                else:
                    l[second].add(edges[i][0])
            
        if len(l) == 1:
            return True
        else:
            return False
            
    def find(self, e, l):
        for i in range(len(l)):
            if e in l[i]:
                return i
        return -1

# V1''
# https://blog.csdn.net/qq_37821701/article/details/104168177
# IDEA :DFS 
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:        
        if len(edges) != n-1:
            return False
        memo = collections.defaultdict(list)
        for edge in edges:
            memo[edge[0]].append(edge[1])
            memo[edge[1]].append(edge[0])
        visited = [False]*n
        def helper(curr,parent):
            if visited[curr]:
                return False
            visited[curr] = True
            for val in memo[curr]:
                if val!= parent and not helper(val,curr):
                    return False
            return True
        if not helper(0,-1):
            return False
        for v in visited:
            if not v:
                return False
        return True

# V1'''
# https://blog.csdn.net/qq_37821701/article/details/104168177
# IDEA : BFS
class Solution:
    def validTree(self, n: int, edges: List[List[int]]) -> bool:        
        if len(edges) != n - 1:  # Check number of edges.
            return False
 
        # init node's neighbors in dict
        neighbors = collections.defaultdict(list)
        for u, v in edges:
            neighbors[u].append(v)
            neighbors[v].append(u)
 
        # BFS to check whether the graph is valid tree.
        visited = {}
        q = collections.deque([0])
        while q:
            curr = q.popleft()
            visited[curr] = True
            for node in neighbors[curr]:
                if node not in visited:
                    visited[node] = True
                    q.append(node)
 
        return len(visited)==n

# V1''''
# https://blog.csdn.net/qq_37821701/article/details/104168177
# IDEA : UNION FIND : dev

# V2 
# Time:  O(|V| + |E|)
# Space: O(|V| + |E|)
import collections
# BFS solution. Same complexity but faster version.
class Solution(object):
    # @param {integer} n
    # @param {integer[][]} edges
    # @return {boolean}
    def validTree(self, n, edges):
        if len(edges) != n - 1:  # Check number of edges.
            return False

        # init node's neighbors in dict
        neighbors = collections.defaultdict(list)
        for u, v in edges:
            neighbors[u].append(v)
            neighbors[v].append(u)

        # BFS to check whether the graph is valid tree.
        q = collections.deque([0])
        visited = set([0])
        while q:
            curr = q.popleft()
            for node in neighbors[curr]:
                if node not in visited:
                    visited.add(node)
                    q.append(node)

        return len(visited) == n


# Time:  O(|V| + |E|)
# Space: O(|V| + |E|)
# BFS solution.
class Solution2(object):
    # @param {integer} n
    # @param {integer[][]} edges
    # @return {boolean}
    def validTree(self, n, edges):
        # A structure to track each node's [visited_from, neighbors]
        visited_from = [-1] * n
        neighbors = collections.defaultdict(list)
        for u, v in edges:
            neighbors[u].append(v)
            neighbors[v].append(u)

        # BFS to check whether the graph is valid tree.
        q = collections.deque([0])
        visited = set([0])
        while q:
            i = q.popleft()
            for node in neighbors[i]:
                if node != visited_from[i]:
                    if node in visited:
                        return False
                    else:
                        visited.add(node)
                        visited_from[node] = i
                        q.append(node)
        return len(visited) == n
