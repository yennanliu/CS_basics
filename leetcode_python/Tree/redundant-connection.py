"""

684. Redundant Connection
Solved
Medium
Topics
Companies
In this problem, a tree is an undirected graph that is connected and has no cycles.

You are given a graph that started as a tree with n nodes labeled from 1 to n, with one additional edge added. The added edge has two different vertices chosen from 1 to n, and was not an edge that already existed. The graph is represented as an array edges of length n where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the graph.

Return an edge that can be removed so that the resulting graph is a tree of n nodes. If there are multiple answers, return the answer that occurs last in the input
 

Example 1:


Input: edges = [[1,2],[1,3],[2,3]]
Output: [2,3]
Example 2:


Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
Output: [1,4]
 

Constraints:

n == edges.length
3 <= n <= 1000
edges[i].length == 2
1 <= ai < bi <= edges.length
ai != bi
There are no repeated edges.
The given graph is connected.

"""

# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80487064
class Solution:
    def findRedundantConnection(self, edges):
        """
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        tree = [-1] * (len(edges) + 1)
        for edge in edges:
            a = self.findRoot(edge[0], tree)
            b = self.findRoot(edge[1], tree)
            if a != b:
                tree[a] = b
            else:
                return edge
        
        
    def findRoot(self, x, tree):
        if tree[x] == -1: return x
        else:
            root = self.findRoot(tree[x], tree)
            tree[x] = root
            return root

# V1'
# https://www.jiuzhang.com/solution/redundant-connection/#tag-highlight-lang-python
class Solution:

    def findRedundantConnection(self, edges):
        if not edges: return None
        uf = UnionFind(len(edges))
        
        for first, second in edges:
            # check if 2 trees have same "father", if not, join them 
            if not uf.query(first,second):
                uf.connect(first,second)
            # keep the process till the same element shown ; or should be a loop 
            else:
                return (first, second)
        return None

class UnionFind(object):
    def __init__(self,n):
        self.father = {}
        for i in range(1, n+1):
            self.father[i] = i
            
    def find(self, node):
        path = []
        while self.father[node]!= node:
            node = self.father[node]
            path.append(node)
        for n in path:
            self.father[n] = node
        return node
    
    def query(self, a, b):
        return self.find(a) == self.find(b)
    def connect(self, a, b):
        self.father[self.find(a)] = self.find(b)
# if __name__== "__main__":
#     t = Solution()
#     x= t.findRedundantConnection([[1,2], [2,3], [3,4], [1,4], [1,5]])
#     print(x)

# V2 
# Time:  O(nlog*n) ~= O(n), n is the length of the positions
# Space: O(n)
class UnionFind(object):
    def __init__(self, n):
        self.set = range(n)

    def find_set(self, x):
        if self.set[x] != x:
            self.set[x] = self.find_set(self.set[x])  # path compression.
        return self.set[x]

    def union_set(self, x, y):
        x_root, y_root = map(self.find_set, (x, y))
        if x_root == y_root:
            return False
        self.set[min(x_root, y_root)] = max(x_root, y_root)
        return True

class Solution(object):
    def findRedundantConnection(self, edges):
        """
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        union_find = UnionFind(len(edges)+1)
        for edge in edges:
            if not union_find.union_set(*edge):
                return edge
        return []