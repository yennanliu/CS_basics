"""

797. All Paths From Source to Target
Medium

Given a directed acyclic graph (DAG) of n nodes labeled from 0 to n - 1, find all possible paths from node 0 to node n - 1 and return them in any order.

The graph is given as follows: graph[i] is a list of all nodes you can visit from node i (i.e., there is a directed edge from node i to node graph[i][j]).

Example 1:

https://assets.leetcode.com/uploads/2020/09/28/all_1.jpg

Input: graph = [[1,2],[3],[3],[]]
Output: [[0,1,3],[0,2,3]]
Explanation: There are two paths: 0 -> 1 -> 3 and 0 -> 2 -> 3.

Example 2:

https://assets.leetcode.com/uploads/2020/09/28/all_2.jpg

Input: graph = [[4,3,1],[3,2,4],[3],[4],[]]
Output: [[0,4],[0,3,4],[0,1,3,4],[0,1,2,3,4],[0,1,4]]

Constraints:

n == graph.length
2 <= n <= 15
0 <= graph[i][j] < n
graph[i][j] != i (i.e., there will be no self-loops).
All the elements of graph[i] are unique.
The input graph is guaranteed to be a DAG.

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79624149
# time = O(2^n * n)  # n = number of nodes; up to 2^n paths each of length O(n)
# space = O(2^n * n)  # storing all paths
class Solution(object):
    def allPathsSourceTarget(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[List[int]]
        """
        res = []
        self.dfs(graph, res, 0, [0])
        return res
        
    
    def dfs(self, graph, res, pos, path):
        if pos == len(graph) - 1:
            res.append(path)
            return
        else:
            for n in graph[pos]:
                self.dfs(graph, res, n, path + [n])

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79624149
# time = O(2^n * n)  # n = number of nodes; up to 2^n paths each of length O(n)
# space = O(2^n * n)  # storing all paths
class Solution(object):
    def allPathsSourceTarget(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[List[int]]
        """
        res = []
        self.dfs(graph, 0, len(graph) - 1, res, [0])
        return res

    def dfs(self, graph, start, end, res, path):
        if start == end:
            res.append(path)
        for node in graph[start]:
            self.dfs(graph, node, end, res, path + [node])

# V1''
# https://www.jiuzhang.com/solution/all-paths-from-source-to-target/#tag-highlight-lang-python
# IDEA : DFS 
# time = O(2^n * n)  # n = number of nodes; up to 2^n paths each of length O(n)
# space = O(2^n * n)  # storing all paths
class Solution:
    """
    @param graph: a 2D array
    @return: all possible paths from node 0 to node N-1
    """
    def allPathsSourceTarget(self, graph):
        N = len(graph)
        res = []
        def dfs(N, graph, start, res, path):
            if start == N-1:
                res.append(path)
            else:
                for node in graph[start]:
                    dfs(N, graph, node, res, path + [node])
        dfs(N, graph, 0, res, [0])
        return (res)

# V2 
# time = O(p + r * n), p is the count of all the possible paths in graph, r is the count of the result.
# space = O(n)
class Solution(object):
    def allPathsSourceTarget(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[List[int]]
        """
        def dfs(graph, curr, path, result):
            if curr == len(graph)-1:
                result.append(path[:])
                return
            for node in graph[curr]:
                path.append(node)
                dfs(graph, node, path, result)
                path.pop()

        result = []
        dfs(graph, 0, [0], result)
        return result