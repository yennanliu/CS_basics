# V0 
# IDEA : DFS
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
