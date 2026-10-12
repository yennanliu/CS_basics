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
class Solution(object):
    def allPathsSourceTarget(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[List[int]]
        """
        pass


# V0-1



# V0-2
# IDEA: DFS + BACKTRACK (GPT)
class Solution(object):
    def allPathsSourceTarget(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[List[int]]
        """
        n = len(graph)
        self.res = []

        # Start DFS from node 0.
        cache = [0]
        self.helper(graph, 0, cache, n - 1)

        return self.res

    def helper(self, graph, x, cache, target):
        # Base case: reached the target.
        if x == target:
            self.res.append(cache[:])  # Save a copy of the path.
            return

        # Explore every neighbor.
        for next_node in graph[x]:
            cache.append(next_node)

            # Recursively explore the next node.
            self.helper(graph, next_node, cache, target)

            """
            NOTE !!!

            need to `undo` below (BACKTRACK)
            """
            # Backtrack: remove the node before exploring
            # the next neighbor.
            cache.pop()


# V0-3
# IDEA: DFS + BACKTRACK (GEMINI)
class Solution(object):

  def allPathsSourceTarget(self, graph):
    """:type graph: List[List[int]]

    :rtype: List[List[int]]
    """
    if not graph:
      return []

    res = []
    n = len(graph)
    target = n - 1

    def backtrack(curr_node, path):
      # 終點條件：如果當前節點已經是目標節點 (n - 1)
      if curr_node == target:
        res.append(list(path))  # 必須複製一份當前的路徑放入結果
        return

      # 走訪當前節點的所有相鄰節點
      for neighbor in graph[curr_node]:
        path.append(neighbor)  # 1. 做選擇

        
        """
        NOTE !!!

        need to `undo` below (BACKTRACK)
        """
        backtrack(neighbor, path)  # 2. 遞迴探索
        path.pop()  # 3. 撤銷選擇 (回溯)


    # 從節點 0 開始出發，初始路徑包含 [0]
    backtrack(0, [0])
    return res


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