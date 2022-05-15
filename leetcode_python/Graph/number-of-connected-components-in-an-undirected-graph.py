"""

323. Number of Connected Components in an Undirected Graph
Medium

You have a graph of n nodes. You are given an integer n and an array edges where edges[i] = [ai, bi] indicates that there is an edge between ai and bi in the graph.

Return the number of connected components in the graph.

 

Example 1:


Input: n = 5, edges = [[0,1],[1,2],[3,4]]
Output: 2
Example 2:


Input: n = 5, edges = [[0,1],[1,2],[2,3],[3,4]]
Output: 1
 

Constraints:

1 <= n <= 2000
1 <= edges.length <= 5000
edges[i].length == 2
0 <= ai <= bi < n
ai != bi
There are no repeated edges.

"""

# V0'
# IDEA : DFS
from collections import defaultdict
class Solution:
    def countComponents(self, n, edges):
        def help(x):
            """
            NOTE !!! only execute if x is in graph's key
            """
            if x in g:
                for item in g[x]:
                    """
                    NOTE !!!
                        1) execute if item NOT in visited
                        2) add item to visited in each loop (for item in g[x])
                    """
                    if item not in visited:
                        visited.add(item)
                        help(item)
        res = 0
        visited = set()
        # build graph
        g = defaultdict(set)
        for a, b in edges:
            g[a].add(b)
            g[b].add(a)
        """
        NOTE !!! we loop over n (INSTEAD OF edges)
        """
        for i in range(n):
            # have a filter here
            if i not in visited:
                #print ("i = " + str(i) + " visited = " + str(visited))
                help(i)
                # NOTE here !!!, we plus res when call help func every time
                res += 1
        return res

# V0'
# IDEA : DFS + GRAPH
from collections import defaultdict
class Solution:
    def countComponents(self, n, edges):
        def helper(u):
            if u in pair:
                for v in pair[u]:
                    if v not in visited:
                        visited.add(v)
                        helper(v)
        # init graph
        pair = collections.defaultdict(set)
        # build graph
        for u,v in edges:
            pair[u].add(v)
            pair[v].add(u)
        count = 0
        visited = set()
        for i in range(n):
            if i not in visited:
                """
                NOTE here !!!
                """
                helper(i)
                count+=1
        return count

# V1
# IDEA : DFS
# https://blog.csdn.net/qq_37821701/article/details/104371911
class Solution:
    def countComponents(self, n, edges):
        def helper(u):
            if u in pair:
                for v in pair[u]:
                    if v not in visited:
                        visited.add(v)
                        helper(v)
            
        pair = collections.defaultdict(set)
        for u,v in edges:
            pair[u].add(v)
            pair[v].add(u)
        count = 0
        visited = set()
        for i in range(n):
            if i not in visited:
                helper(i)
                count+=1
        return count

# V1
# IDEA : BFS
# https://www.jiuzhang.com/solution/number-of-connected-components-in-an-undirected-graph/#tag-other-lang-python
import collections
class Solution:
    def countComponents(self, n, edges):
        
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
        # note : Given n nodes labeled from 0 to n - 1
        for i in range(n):
            node = find_root(visited)
            if node != -1:
                count+=1
                visited.append(node)
                visited = bfs(node, visited)
            else:
                return count

# V1'
# IDEA : UNION FIND
# https://www.cnblogs.com/lightwindy/p/8487160.html
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

# V1''
# IDEA : UNION FIND
# https://blog.csdn.net/qq_37821701/article/details/104371911
class Solution:
    def countComponents(self, n, edges):
        def unionfind(p1,p2):
            nonlocal count
            # find root of p1
            while root[p1]!=p1:
                p1 = root[p1]
            # find root of p2
            while root[p2]!=p2:
                p2 = root[p2]
            #if roots of p1 and p2 are identicle, meaning they have already been merged
            if root[p1]==root[p2]:
                return
            # merge them if not merged 
            else:
                root[p1] = p2
                count -= 1
        # initially, we have n connected path
        count = n 
        # store original roots
        root = [i for i in range(n)] 
        # go through each node pair
        for edge in edges:
            unionfind(edge[0],edge[1])
        return count

# V1
# IDEA : DFS
# https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/solution/
# JAVA
# class Solution {
#   
#      private void dfs(List<Integer>[] adjList, int[] visited, int startNode) {
#         visited[startNode] = 1;
#         
#         for (int i = 0; i < adjList[startNode].size(); i++) {
#             if (visited[adjList[startNode].get(i)] == 0) {
#                 dfs(adjList, visited, adjList[startNode].get(i));
#             }
#         }
#     }
#    
#     public int countComponents(int n, int[][] edges) {
#         int components = 0;
#         int[] visited = new int[n];
#        
#         List<Integer>[] adjList = new ArrayList[n]; 
#         for (int i = 0; i < n; i++) {
#             adjList[i] = new ArrayList<Integer>();
#         }
#        
#         for (int i = 0; i < edges.length; i++) {
#             adjList[edges[i][0]].add(edges[i][1]);
#             adjList[edges[i][1]].add(edges[i][0]);
#         }
#        
#         for (int i = 0; i < n; i++) {
#             if (visited[i] == 0) {
#                 components++;
#                 dfs(adjList, visited, i);
#             }
#         }
#         return components;
#     }
# }

# V1
# IDEA : Disjoint Set Union (DSU)
# https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/solution/
# JAVA
# public class Solution {
#
#     private int find(int[] representative, int vertex) {
#         if (vertex == representative[vertex]) {
#             return vertex;
#         }
#        
#         return representative[vertex] = find(representative, representative[vertex]);
#     }
#    
#     private int combine(int[] representative, int[] size, int vertex1, int vertex2) {
#         vertex1 = find(representative, vertex1);
#         vertex2 = find(representative, vertex2);
#        
#         if (vertex1 == vertex2) {
#             return 0;
#         } else {
#             if (size[vertex1] > size[vertex2]) {
#                 size[vertex1] += size[vertex2];
#                 representative[vertex2] = vertex1;
#             } else {
#                 size[vertex2] += size[vertex1];
#                 representative[vertex1] = vertex2;
#             }
#             return 1;
#         }
#     }
#
#     public int countComponents(int n, int[][] edges) {
#         int[] representative = new int[n];
#         int[] size = new int[n];
#        
#         for (int i = 0; i < n; i++) {
#             representative[i] = i;
#             size[i] = 1;
#         }
#        
#         int components = n;
#         for (int i = 0; i < edges.length; i++) { 
#             components -= combine(representative, size, edges[i][0], edges[i][1]);
#         }
#
#         return components;
#     }
# }

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