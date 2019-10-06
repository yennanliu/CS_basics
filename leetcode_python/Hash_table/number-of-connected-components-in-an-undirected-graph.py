# V0 

# V1 
# https://www.jiuzhang.com/solution/number-of-connected-components-in-an-undirected-graph/#tag-other-lang-python
import collections
class Solution:
    def countComponents(self, n: int, edges: List[List[int]]) -> int:
        
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
        for i in range(n):
            node = find_root(visited)
            if node != -1:
                count+=1
                visited.append(node)
                visited = bfs(node, visited)
            else:
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
