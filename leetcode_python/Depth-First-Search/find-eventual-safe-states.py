"""

802. Find Eventual Safe States
Medium

There is a directed graph of n nodes with each node labeled from 0 to n - 1. The graph is represented by a 0-indexed 2D integer array graph where graph[i] is an integer array of nodes adjacent to node i, meaning there is an edge from node i to each node in graph[i].

A node is a terminal node if there are no outgoing edges. A node is a safe node if every possible path starting from that node leads to a terminal node (or another safe node).

Return an array containing all the safe nodes of the graph. The answer should be sorted in ascending order.

Example 1:

Illustration of graph (https://s3-lc-upload.s3.amazonaws.com/uploads/2018/03/17/picture1.png)

Input: graph = [[1,2],[2,3],[5],[0],[5],[],[]]
Output: [2,4,5,6]
Explanation: The given graph is shown above.
Nodes 5 and 6 are terminal nodes as there are no outgoing edges from either of them.
Every path starting at nodes 2, 4, 5, and 6 all lead to either node 5 or 6.

Example 2:

Input: graph = [[1,2,3,4],[1,2],[3,4],[0,4],[]]
Output: [4]
Explanation:
Only node 4 is a terminal node, and every path starting at node 4 leads to node 4.

Constraints:

n == graph.length
1 <= n <= 10^4
0 <= graph[i].length <= n
0 <= graph[i][j] <= n - 1
graph[i] is sorted in a strictly increasing order.
The graph may contain self-loops.
The number of edges in the graph will be in the range [1, 4 * 10^4].

"""

# V0 

# V1
# http://bookshadow.com/weblog/2018/03/18/leetcode-find-eventual-safe-states/
import collections
# time = O(V + E)
# space = O(V + E)
class Solution(object):
    def eventualSafeNodes(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[int]
        """
        srcs = collections.defaultdict(set)
        tgts = collections.defaultdict(set)
        for idx in range(len(graph)):
            for v in graph[idx]:
                tgts[idx].add(v)
                srcs[v].add(idx)

        degZeros = [k for k in range(len(graph)) if not tgts[k]]
        while degZeros:
            ndegZeros = []
            for t in degZeros:
                for s in srcs[t]:
                    tgts[s].remove(t)
                    if not tgts[s]: ndegZeros.append(s)
            degZeros = ndegZeros
        return [k for k in range(len(graph)) if not tgts[k]] 

# V1'
# https://www.jiuzhang.com/solution/find-eventual-safe-states/#tag-highlight-lang-python
# time = O(V + E)
# space = O(V)
class Solution:
    """
    @param graph: a 2D integers array
    @return: return a list of integers
    """
    def eventualSafeNodes(self, graph):
        def dfs(graph, i, visited):
            for j in graph[i]:
                if j in visited:
                    return False
                if j in ans:
                    continue
                visited.add(j)
                if not dfs(graph, j, visited):
                    return False
                visited.remove(j)
            ans.add(i)
            return True
        ans = set()
        for i in range(len(graph)):
            visited = set([i])
            dfs(graph, i, visited)
        return sorted(list(ans))

# V2
# time = O(|V| + |E|)
# space = O(|V|)
import collections
class Solution(object):
    def eventualSafeNodes(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: List[int]
        """
        WHITE, GRAY, BLACK = 0, 1, 2

        def dfs(graph, node, lookup):
            if lookup[node] != WHITE:
                return lookup[node] == BLACK
            lookup[node] = GRAY
            for child in graph[node]:
                if lookup[child] == BLACK:
                    continue
                if lookup[child] == GRAY or \
                   not dfs(graph, child, lookup):
                    return False
            lookup[node] = BLACK
            return True

        lookup = collections.defaultdict(int)
        return filter(lambda node: dfs(graph, node, lookup), range(len(graph)))
