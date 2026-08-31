"""

310. Minimum Height Trees
Solved
Medium
Topics
premium lock icon
Companies
Hint
A tree is an undirected graph in which any two vertices are connected by exactly one path. In other words, any connected graph without simple cycles is a tree.

Given a tree of n nodes labelled from 0 to n - 1, and an array of n - 1 edges where edges[i] = [ai, bi] indicates that there is an undirected edge between the two nodes ai and bi in the tree, you can choose any node of the tree as the root. When you select a node x as the root, the result tree has height h. Among all possible rooted trees, those with minimum height (i.e. min(h))  are called minimum height trees (MHTs).

Return a list of all MHTs' root labels. You can return the answer in any order.

The height of a rooted tree is the number of edges on the longest downward path between the root and a leaf.

 

Example 1:


Input: n = 4, edges = [[1,0],[1,2],[1,3]]
Output: [1]
Explanation: As shown, the height of the tree is 1 when the root is the node with label 1 which is the only MHT.
Example 2:


Input: n = 6, edges = [[3,0],[3,1],[3,2],[3,4],[5,4]]
Output: [3,4]
 

Constraints:

1 <= n <= 2 * 104
edges.length == n - 1
0 <= ai, bi < n
ai != bi
All the pairs (ai, bi) are distinct.
The given input is guaranteed to be a tree and there will be no repeated edges.
 


"""


# V0 
class Solution(object):
    def findMinHeightTrees(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        pass


# V1
# http://bookshadow.com/weblog/2015/11/26/leetcode-minimum-height-trees/
# time = O(n)  # each node/edge removed once during leaf trimming
# space = O(n)
class Solution(object):
    def findMinHeightTrees(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        children = collections.defaultdict(set)
        for s, t in edges:
            children[s].add(t)
            children[t].add(s)
        vertices = set(children.keys())
        while len(vertices) > 2:
            leaves = [x for x in children if len(children[x]) == 1]
            for x in leaves:
                for y in children[x]:
                    children[y].remove(x)
                del children[x]
                vertices.remove(x)
        return list(vertices) if n != 1 else [0]

# V1'
# http://bookshadow.com/weblog/2015/11/26/leetcode-minimum-height-trees/
# time = O(n)  # each node/edge removed once during leaf trimming
# space = O(n)
class Solution(object):
    def findMinHeightTrees(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        children = [set() for x in range(n)]
        for s, t in edges:
            children[s].add(t)
            children[t].add(s)
        leaves = [x for x in range(n) if len(children[x]) <= 1]
        while n > 2:
            n -= len(leaves)
            newLeaves = []
            for x in leaves:
                for y in children[x]:
                    children[y].remove(x)
                    if len(children[y]) == 1:
                        newLeaves.append(y)
            leaves = newLeaves
        return leaves

# V1''
# http://bookshadow.com/weblog/2015/11/26/leetcode-minimum-height-trees/
# time = O(n^2)  # findHeight does a full O(n) DFS from every candidate node
# space = O(n)
class Solution(object):
    def findMinHeightTrees(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        if n == 1: return [0]
        children = collections.defaultdict(set)
        for s, t in edges:
            children[s].add(t)
            children[t].add(s)
        l = []
        for v in children:
            l += (v, self.findHeight(v, children)),
        minHeight = min(x[1] for x in l) if l else 0
        return [x[0] for x in l if x[1] == minHeight]
    def findHeight(self, root, children):
        height = 0
        for c in children[root]:
            children[c].remove(root)
            height = max(height, self.findHeight(c, children))
            children[c].add(root)
        return height + 1

# V1'''
# https://www.jiuzhang.com/solution/minimum-height-trees/#tag-highlight-lang-python
# time = O(n)  # each node/edge removed once during leaf trimming
# space = O(n)
class Solution(object):
    def findMinHeightTrees(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        if n == 1: return [0]

        
        graph = [set() for _ in xrange(n)]
        for i,j in edges:
            graph[i].add(j)
            graph[j].add(i)
            
        # the intuition is that in a connected graph,
        # if you pick a node of degree 1 as the root
        # then the resulting tree has the max ht.
        # so trim the leaves until there are at most 2
        # and at least 1 node left.
        
        leaves = [i for i in xrange(n) if len(graph[i]) == 1]
        while n > 2:
            n -= len(leaves)
            new_leaves = []
            for leaf in leaves:
                neighbor = graph[leaf].pop()
                graph[neighbor].remove(leaf)
                if len(graph[neighbor]) == 1: new_leaves.append(neighbor)
            leaves = new_leaves        
        return leaves

# V2
# time = O(n)
# space = O(n)
import collections
class Solution(object):
    def findMinHeightTrees(self, n, edges):
        """
        :type n: int
        :type edges: List[List[int]]
        :rtype: List[int]
        """
        if n == 1:
            return [0]

        neighbors = collections.defaultdict(set)
        for u, v in edges:
            neighbors[u].add(v)
            neighbors[v].add(u)

        pre_level, unvisited = [], set()
        for i in range(n):
            if len(neighbors[i]) == 1:  # A leaf.
                pre_level.append(i)
            unvisited.add(i)

        # A graph can have 2 MHTs at most.
        # BFS from the leaves until the number
        # of the unvisited nodes is less than 3.
        while len(unvisited) > 2:
            cur_level = []
            for u in pre_level:
                unvisited.remove(u)
                for v in neighbors[u]:
                    if v in unvisited:
                        neighbors[v].remove(u)
                        if len(neighbors[v]) == 1:
                            cur_level.append(v)
            pre_level = cur_level

        return list(unvisited)
