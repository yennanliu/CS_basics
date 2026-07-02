# https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/description/

"""

2316. Count Unreachable Pairs of Nodes in an Undirected Graph
Solved
Medium
Topics
premium lock icon
Companies
Hint
You are given an integer n. There is an undirected graph with n nodes, numbered from 0 to n - 1. You are given a 2D integer array edges where edges[i] = [ai, bi] denotes that there exists an undirected edge connecting nodes ai and bi.

Return the number of pairs of different nodes that are unreachable from each other.

 

Example 1:


Input: n = 3, edges = [[0,1],[0,2],[1,2]]
Output: 0
Explanation: There are no pairs of nodes that are unreachable from each other. Therefore, we return 0.
Example 2:


Input: n = 7, edges = [[0,2],[0,5],[2,4],[1,6],[5,4]]
Output: 14
Explanation: There are 14 pairs of nodes that are unreachable from each other:
[[0,1],[0,3],[0,6],[1,2],[1,3],[1,4],[1,5],[2,3],[2,6],[3,4],[3,5],[3,6],[4,6],[5,6]].
Therefore, we return 14.
 

Constraints:

1 <= n <= 105
0 <= edges.length <= 2 * 105
edges[i].length == 2
0 <= ai, bi < n
ai != bi
There are no repeated edges.
 

"""


# V0
class Solution(object):
    def countPairs(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: int
        """
        

# V1-1
# IDEA: UNION FIND (gemini)
class MyUF:
    def __init__(self, n):
        # Every node starts as its own parent
        self.parents = list(range(n))
        # Keep track of the size of each cluster to easily count pairs later
        self.sizes = [1] * n

    def get_parent(self, x):
        # Find path compression
        if self.parents[x] != x:
            self.parents[x] = self.get_parent(self.parents[x])
        return self.parents[x]

    def union(self, x, y):
        root_x = self.get_parent(x)
        root_y = self.get_parent(y)
        
        if root_x != root_y:
            # Union by size: attach smaller cluster to larger cluster
            if self.sizes[root_x] < self.sizes[root_y]:
                root_x, root_y = root_y, root_x
            self.parents[root_y] = root_x
            self.sizes[root_x] += self.sizes[root_y]

class Solution(object):
    def countPairs(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: int
        """
        my_uf = MyUF(n)
        for x, y in edges:
            my_uf.union(x, y)
    
        # Step 1: Map the true cluster root to the nodes inside it
        c_map = {}
        for i in range(n):
            # FIX 2: Use get_parent(i) instead of my_uf.parents[i] directly
            root = my_uf.get_parent(i)
            if root not in c_map:
                c_map[root] = 0
            c_map[root] += 1
            
        # Step 2: Efficiently calculate pairs without a nested loop
        res = 0
        remaining_nodes = n
        
        for cluster_size in c_map.values():
            # The current cluster can pair with all other remaining nodes
            remaining_nodes -= cluster_size
            res += cluster_size * remaining_nodes
            
        return res


# V1-2
# IDEA: UNION FIND (GPT)
class MyUF:

    def __init__(self, n):
        self.parents = list(range(n))
        self.cluster_cnt = n

    def get_parent(self, x):
        if self.parents[x] != x:
            self.parents[x] = self.get_parent(self.parents[x])
        return self.parents[x]

    def union(self, x, y):
        px = self.get_parent(x)
        py = self.get_parent(y)

        if px == py:
            return

        self.parents[py] = px
        self.cluster_cnt -= 1

    def is_connected(self, x, y):
        return self.get_parent(x) == self.get_parent(y)


class Solution(object):
    def countPairs(self, n, edges):

        my_uf = MyUF(n)

        for x, y in edges:
            my_uf.union(x, y)

        # root -> component size
        size = {}

        for i in range(n):
            root = my_uf.get_parent(i)
            size[root] = size.get(root, 0) + 1

        res = 0
        remain = n

        for s in size.values():
            remain -= s
            res += s * remain

        return res


# V2
