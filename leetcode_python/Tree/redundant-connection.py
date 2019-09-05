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