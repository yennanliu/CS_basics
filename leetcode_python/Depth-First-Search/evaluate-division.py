"""

399. Evaluate Division
Medium

You are given an array of variable pairs equations and an array of real numbers values, where equations[i] = [Ai, Bi] and values[i] represent the equation Ai / Bi = values[i]. Each Ai or Bi is a string that represents a single variable.

You are also given some queries, where queries[j] = [Cj, Dj] represents the jth query where you must find the answer for Cj / Dj = ?.

Return the answers to all queries. If a single answer cannot be determined, return -1.0.

Note: The input is always valid. You may assume that evaluating the queries will not result in division by zero and that there is no contradiction.

 

Example 1:

Input: equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
Output: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
Explanation: 
Given: a / b = 2.0, b / c = 3.0
queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
return: [6.0, 0.5, -1.0, 1.0, -1.0 ]
Example 2:

Input: equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
Output: [3.75000,0.40000,5.00000,0.20000]
Example 3:

Input: equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
Output: [0.50000,2.00000,-1.00000,-1.00000]
 

Constraints:

1 <= equations.length <= 20
equations[i].length == 2
1 <= Ai.length, Bi.length <= 5
values.length == equations.length
0.0 < values[i] <= 20.0
1 <= queries.length <= 20
queries[i].length == 2
1 <= Cj.length, Dj.length <= 5
Ai, Bi, Cj, Dj consist of lower case English letters and digits.

"""

# V0 
# IDEA : DFS + collections.defaultdict(dict)
class Solution:
    def calcEquation(self, equations, values, queries):
        """
        :type equations: List[List[str]]
        :type values: List[float]
        :type queries: List[List[str]]
        :rtype: List[float]
        """
        table = collections.defaultdict(dict)
        for (x, y), value in zip(equations, values):
            table[x][y] = value
            table[y][x] = 1.0 / value
        ans = [self.dfs(x, y, table, set()) if x in table and y in table else -1.0 for (x, y) in queries]
        return ans
        
    def dfs(self, x, y, table, visited):
        if x == y:
            return 1.0
        visited.add(x)
        for n in table[x]:
            if n in visited: continue
            visited.add(n)
            d = self.dfs(n, y, table, visited)
            if d > 0:
                return d * table[x][n]
        return -1.0

# V0'
# IDEA : DFS + collections.defaultdict(dict)
class Solution:
    def calcEquation(self, equations, values, queries):
        from collections import defaultdict
        # build graph
        graph = defaultdict(dict)
        for (x, y), v in zip(equations, values):
            graph[x][y] = v
            graph[y][x] = 1.0/v
        ans = [self.dfs(x, y, graph, set()) for (x, y) in queries]
        return ans

    def dfs(self, x, y, graph, visited):
        if not graph:
            return
        if x not in graph or y not in graph:
            return -1
        if x == y:
            return 1
        visited.add(x)
        for n in graph[x]:
            if n in visited:
                continue
            visited.add(n)
            d = self.dfs(n, y, graph, visited)
            if d > 0:
                return d * graph[x][n]
        return -1.0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82591165
# IDEA : DFS 
# DEMO
#   ...: 
#    ...: import collections
#    ...: equations = [ ["a", "b"], ["b", "c"] ]
#    ...: values = [2.0, 3.0]
#    ...: queries = [ ["a", "c"], ["b", "a"], ["a", "e"], ["a", "a"], ["x", "x"] ]
#    ...: table = collections.defaultdict(dict)
#    ...: for (x, y), value in zip(equations, values):
#    ...:     table[x][y] = value
#    ...:     table[y][x] = 1.0 / value
#    ...: 
#    ...: print (table)
#    ...: 
# defaultdict(<class 'dict'>, {'a': {'b': 2.0}, 'b': {'a': 0.5, 'c': 3.0}, 'c': {'b': 0.3333333333333333}})
# In [5]: table 
# Out[5]: 
# defaultdict(dict,
#             {'a': {'b': 2.0},
#              'b': {'a': 0.5, 'c': 3.0},
#              'c': {'b': 0.3333333333333333}})
class Solution:
    def calcEquation(self, equations, values, queries):
        """
        :type equations: List[List[str]]
        :type values: List[float]
        :type queries: List[List[str]]
        :rtype: List[float]
        """
        table = collections.defaultdict(dict)
        for (x, y), value in zip(equations, values):
            table[x][y] = value
            table[y][x] = 1.0 / value
        ans = [self.dfs(x, y, table, set()) if x in table and y in table else -1.0 for (x, y) in queries]
        return ans
        
    def dfs(self, x, y, table, visited):
        if x == y:
            return 1.0
        visited.add(x)
        for n in table[x]:
            if n in visited: continue
            visited.add(n)
            d = self.dfs(n, y, table, visited)
            if d > 0:
                return d * table[x][n]
        return -1.0

### Test case : dev

# V1'
# https://zxi.mytechroad.com/blog/graph/leetcode-399-evaluate-division/
# IDEA : DFS 
class Solution:
  def calcEquation(self, equations, values, queries):
    def divide(x, y, visited):
      if x == y: return 1.0
      visited.add(x)
      for n in g[x]:
        if n in visited: continue
        visited.add(n)
        d = divide(n, y, visited)
        if d > 0: return d * g[x][n]
      return -1.0
    
    g = collections.defaultdict(dict)
    for (x, y), v in zip(equations, values):      
      g[x][y] = v
      g[y][x] = 1.0 / v
    
    ans = [divide(x, y, set()) if x in g and y in g else -1 for x, y in queries]
    return ans

# V1''
# https://zxi.mytechroad.com/blog/graph/leetcode-399-evaluate-division/
# IDEA : UNION FIND 
class Solution:
  def calcEquation(self, equations, values, queries):
    def find(x):
      if x != U[x][0]:
        px, pv = find(U[x][0])
        U[x] = (px, U[x][1] * pv)
      return U[x]
    
    def divide(x, y):
      rx, vx = find(x)
      ry, vy = find(y)
      if rx != ry: return -1.0
      return vx / vy
    
    U = {}
    for (x, y), v in zip(equations, values):      
      if x not in U and y not in U:
        U[x] = (y, v)
        U[y] = (y, 1.0)
      elif x not in U:
        U[x] = (y, v)
      elif y not in U:
        U[y] = (x, 1.0 / v)
      else:
        rx, vx = find(x)
        ry, vy = find(y)
        U[rx] = (ry, v / vx * vy)
    
    ans = [divide(x, y) if x in U and y in U else -1 for x, y in queries]
    return ans

# V1'''
# https://www.jiuzhang.com/solution/evaluate-division/
# JAVA
# public class Solution {
#     public void addArc(Map<String, Map<String, Double>> graph, String vexStart, String vexEnd, double value) {
#         Map<String, Double> arcMap;
#         if (graph.containsKey(vexStart)) {
#             arcMap = graph.get(vexStart);
#         } else {
#             arcMap = new HashMap<>();
#         }
#         arcMap.put(vexEnd, value);
#         graph.put(vexStart, arcMap);
#     }

#     public double getValue(Map<String, Map<String, Double>> graph, String vexStart, String vexEnd) {
#         if (graph.get(vexStart) == null || graph.get(vexEnd) == null) {
#             return -1;
#         }

#         Queue<String> queue = new LinkedList<>();   //queue uesd for bfs
#         Map<String, Double> value = new HashMap<>();    //distance from vexStart
#         Set<String> validation = new HashSet<>();   //check if the vertex has been in the queue

#         //init
#         queue.add(vexStart);
#         validation.add(vexStart);
#         value.put(vexStart, 1d);

#         String currentNode, nextNode;
#         while (!queue.isEmpty()) {
#             currentNode = queue.remove();
#             for (Map.Entry<String, Double> arc : graph.get(currentNode).entrySet()) {
#                 nextNode = arc.getKey();
#                 value.put(nextNode, value.get(currentNode) * arc.getValue());
#                 if (nextNode.equals(vexEnd)) {
#                     return value.get(vexEnd);
#                 } else if (!validation.contains(nextNode)) {
#                     queue.add(nextNode);
#                     validation.add(nextNode);
#                 }
#             }
#         }
#         return -1;
#     }

#     public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {

#         Map<String, Map<String, Double>> graph = new HashMap<>();

#         for (int i = 0; i < equations.length; i++) {
#             //add arcs for both directions
#             addArc(graph, equations[i][0], equations[i][1], values[i]);
#             addArc(graph, equations[i][1], equations[i][0], 1 / values[i]);
#         }

#         double[] answer = new double[queries.length];
#         for (int i = 0; i < answer.length; i++) {
#             answer[i] = getValue(graph, queries[i][0], queries[i][1]);
#         }

#         return answer;
#     }
# }


# V2 
# Time:  O(e + q * |V|!), |V| is the number of variables
# Space: O(e)
import collections
class Solution(object):
    def calcEquation(self, equations, values, query):
        """
        :type equations: List[List[str]]
        :type values: List[float]
        :type query: List[List[str]]
        :rtype: List[float]
        """
        def check(up, down, lookup, visited):
            if up in lookup and down in lookup[up]:
                return (True, lookup[up][down])
            for k, v in lookup[up].iteritems():
                if k not in visited:
                    visited.add(k)
                    tmp = check(k, down, lookup, visited)
                    if tmp[0]:
                        return (True, v * tmp[1])
            return (False, 0)

        lookup = collections.defaultdict(dict)
        for i, e in enumerate(equations):
            lookup[e[0]][e[1]] = values[i]
            if values[i]:
                lookup[e[1]][e[0]] = 1.0 / values[i]

        result = []
        for q in query:
            visited = set()
            tmp = check(q[0], q[1], lookup, visited)
            result.append(tmp[1] if tmp[0] else -1)
        return result
