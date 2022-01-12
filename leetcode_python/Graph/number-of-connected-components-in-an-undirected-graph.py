"""

Given n nodes labeled from 0 to n - 1 and a list of undirected edges 
(each edge is a pair of nodes), 
write a function to find the number of connected components in an undirected graph.

Example 1:

Input: n = 5 and edges = [[0, 1], [1, 2], [3, 4]]

     0          3
     |          |
     1 --- 2    4 

Output: 2


Example 2:

Input: n = 5 and edges = [[0, 1], [1, 2], [2, 3], [3, 4]]

     0           4
     |           |
     1 --- 2 --- 3

Output:  1

Note:

You can assume that no duplicate edges will appear in edges. Since all edges are undirected, 
[0, 1] is the same as [1, 0] and thus will not appear together in edges.

"""

# V0
# IDEA : DFS + GRAPH
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

# V1
# IDEA : DFS
# https://blog.csdn.net/qq_37821701/article/details/104371911
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

# V1
# IDEA : BFS
# https://www.jiuzhang.com/solution/number-of-connected-components-in-an-undirected-graph/#tag-other-lang-python
import collections
class Solution:
    def countComponents(self, n, edges):
        
        if not edges:
            return n
        
        list_dict = collections.defaultdict(list)
        for i, j in edges:
            list_dict[i].append(j)
            list_dict[j].append(i)
        
        def bfs(node, visited):
            queue = [node]
            while queue:
                nd = queue.pop(0)
                for neighbor in list_dict[nd]:
                    if neighbor not in visited:
                        visited.append(neighbor)
                        queue.append(neighbor)
            return visited
        def find_root(visited):
            for i in range(n):
                if i not in visited:
                    return i
            return -1
        
        visited = []
        count = 0
        # note : Given n nodes labeled from 0 to n - 1
        for i in range(n):
            node = find_root(visited)
            if node != -1:
                count+=1
                visited.append(node)
                visited = bfs(node, visited)
            else:
                return count

# V1'
# IDEA : UNION FIND
# https://www.cnblogs.com/lightwindy/p/8487160.html
# Time:  O(nlog*n) ~= O(n), n is the length of the positions
# Space: O(n)
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
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: int
        """
        union_find = UnionFind(n)
        for i, j in edges:
            union_find.union_set(i, j)
        return union_find.count

# V1''
# IDEA : UNION FIND
# https://blog.csdn.net/qq_37821701/article/details/104371911
class Solution:
    def countComponents(self, n, edges):
        def unionfind(p1,p2):
            nonlocal count
            # find root of p1
            while root[p1]!=p1:
                p1 = root[p1]
            # find root of p2
            while root[p2]!=p2:
                p2 = root[p2]
            #if roots of p1 and p2 are identicle, meaning they have already been merged
            if root[p1]==root[p2]:
                return
            # merge them if not merged 
            else:
                root[p1] = p2
                count -= 1
        # initially, we have n connected path
        count = n 
        # store original roots
        root = [i for i in range(n)] 
        # go through each node pair
        for edge in edges:
            unionfind(edge[0],edge[1])
        return count

# V2 
# Time:  O(nlog*n) ~= O(n), n is the length of the positions
# Space: O(n)
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
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: int
        """
        union_find = UnionFind(n)
        for i, j in edges:
            union_find.union_set(i, j)
        return union_find.count
