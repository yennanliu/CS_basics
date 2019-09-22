# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79624149
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
# Time:  O(p + r * n), p is the count of all the possible paths in graph, r is the count of the result.
# Space: O(n)
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