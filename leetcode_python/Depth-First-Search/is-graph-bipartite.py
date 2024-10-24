"""

785. Is Graph Bipartite?
Medium

There is an undirected graph with n nodes, where each node is numbered between 0 and n - 1. You are given a 2D array graph, where graph[u] is an array of nodes that node u is adjacent to. More formally, for each v in graph[u], there is an undirected edge between node u and node v. The graph has the following properties:

There are no self-edges (graph[u] does not contain u).
There are no parallel edges (graph[u] does not contain duplicate values).
If v is in graph[u], then u is in graph[v] (the graph is undirected).
The graph may not be connected, meaning there may be two nodes u and v such that there is no path between them.
A graph is bipartite if the nodes can be partitioned into two independent sets A and B such that every edge in the graph connects a node in set A and a node in set B.

Return true if and only if it is bipartite.

 

Example 1:


Input: graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
Output: false
Explanation: There is no way to partition the nodes into two independent sets such that every edge connects a node in one and a node in the other.
Example 2:


Input: graph = [[1,3],[0,2],[1,3],[0,2]]
Output: true
Explanation: We can partition the nodes into two sets: {0, 2} and {1, 3}.
 

Constraints:

graph.length == n
1 <= n <= 100
0 <= graph[u].length < n
0 <= graph[u][i] <= n - 1
graph[u] does not contain u.
All the values of graph[u] are unique.
If graph[u] contains v, then graph[v] contains u.

"""

# V0
# IDEA : GRAPH + DFS
class Solution:
    
    def isBipartite(self, graph):
        n = len(graph)
        self.color = [0] * n
        for i in range(n):
            # if grpah i's color = 0, and check if it's "Bipartite" via self.colored(i, graph, 1)
            if self.color[i] == 0 and not self.colored(i, graph, 1):
                return False
        return True        
    
    def colored(self, now, graph, c):
        self.color[now] = c
        for nxt in graph[now]:
            # case 1) now color = c, next color = 0, but next's neighbor  color != -c  => this case is not going to be "Bipartite"  
            if self.color[nxt] == 0 and not self.colored(nxt, graph, -c):
                return False
            # case 2) now color == next color (next already colored) 
            elif self.color[nxt] == self.color[now]:
                return False
        return True

# V0' 
# IDEA : DFS
# CONTINUE
# In [5]: for i in range(10):
#    ...:     if i%2 ==0:
#    ...:         continue
#    ...:         #pass
#    ...:     print (i)
#    ...:     
# 1
# 3
# 5
# 7
# 9
import collections
class Solution(object):
    def isBipartite(self, graph):
        def dfs(v, color):
            ocolor = 1 - color
            for p in edges[v]:
                if p not in colors:
                    colors[p] = ocolor
                    if not dfs(p, ocolor):
                        return False
                elif colors[p] != ocolor:
                    return False
            return True
        """
        :type graph: List[List[int]]
        :rtype: bool
        """
        edges = collections.defaultdict(set)
        for idx, points in enumerate(graph):
            for p in points: edges[idx].add(p)
        colors = {}
        for k in edges:
            if k in colors: continue
            colors[k] = 0
            if not dfs(k, 0): return False
        return True

# V1
# https://www.jiuzhang.com/solution/is-graph-bipartite/#tag-highlight-lang-python
# IDEA : GRAPH + DFS OR BFS 
class Solution:
    """
    @param graph: the given undirected graph
    @return:  return true if and only if it is bipartite
    """
    def isBipartite(self, graph):
        n = len(graph)
        self.color = [0] * n
        for i in range(n):
            if self.color[i] == 0 and not self.colored(i, graph, 1):
                return False
        return True        
    
    def colored(self, now, graph, c):
        self.color[now] = c
        for nxt in graph[now]:
            if self.color[nxt] == 0 and not self.colored(nxt, graph, -c):
                return False
            elif self.color[nxt] == self.color[now]:
                return False
        return True

# V1'
# http://bookshadow.com/weblog/2018/02/18/leetcode-is-graph-bipartite/
# IDEA : DFS + GRAPH 
import collections
class Solution(object):
    def isBipartite(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: bool
        """
        edges = collections.defaultdict(set)
        for idx, points in enumerate(graph):
            for p in points: edges[idx].add(p)
        colors = {}
        def dfs(v, color):
            ocolor = 1 - color
            for p in edges[v]:
                if p not in colors:
                    colors[p] = ocolor
                    if not dfs(p, ocolor):
                        return False
                elif colors[p] != ocolor:
                    return False
            return True
        for k in edges:
            if k in colors: continue
            colors[k] = 0
            if not dfs(k, 0): return False
        return True

# V2 
# Time:  O(|V| + |E|)
# Space: O(|V|)
class Solution(object):
    def isBipartite(self, graph):
        """
        :type graph: List[List[int]]
        :rtype: bool
        """
        color = {}
        for node in range(len(graph)):
            if node in color:
                continue
            stack = [node]
            color[node] = 0
            while stack:
                curr = stack.pop()
                for neighbor in graph[curr]:
                    if neighbor not in color:
                        stack.append(neighbor)
                        color[neighbor] = color[curr] ^ 1
                    elif color[neighbor] == color[curr]:
                        return False
        return True