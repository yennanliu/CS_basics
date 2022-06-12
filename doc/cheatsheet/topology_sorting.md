# Topology sorting

## 0) Concept  
- Topological Sort is a algorithm can find "ordering" on an "order dependency" graph
- Concept
	- [techbridge : topological-sort](https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/)
	- [do topological-sort via DFS](https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html)
- Code
	- [algorithm/python/topological_sort.py](https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/topological_sort.py)

### 0-1) Types
- Courses
    - LC 207, LC 210
- Alien Dictionary
    - LC 269

### 0-2) Pattern
```
# pseudo code
# https://leetcode.com/problems/course-schedule/solution/

L = Empty list that will contain the sorted elements
S = Set of all nodes with no incoming edge

while S is non-empty do
    remove a node n from S
    add n to tail of L
    for each node m with an edge e from n to m do
        remove edge e from the graph
        if m has no other incoming edges then
            insert m into S

if graph has edges then
    return error (graph has at least one cycle)
else 
    return L (a topologically sorted order)
```

## 1) General form

```python
# V0
# IDEA : implement topologicalSortUtil, topologicalSort, and addEdge methods
# step 1) maintain a stack, save "ordering" nodes in it (and return in final step)
# step 2) init visited as [False]*self.V  (all nodes are NOT visited yet)
# step 3) iterate over all vertices in graph, if not visited, then run topologicalSortUtil
# step 4) return result (stack)
from collections import defaultdict
class Graph:
    def __init__(self, vertices):
        self.graph = defaultdict(list)
        self.V = vertices

    # for build graph
    def addEdge(self, u, v):
        self.graph[u].append(v)

    def topologicalSortUtil(self, v, visited, stack):
        visited[v] = True

        ### NOTE this !!! (self.graph[v])
        for k in self.graph[v]:
            if visited[k] == False:
                self.topologicalSortUtil(k, visited, stack)
        # stack.insert(0,v) # instead of insert v to idx = 0, we can still append v to stack and reverse it and return (e.g. return stack[::-1])
        """
        ### NOTE !! stack.append(v) is wrong, we SHOULD use  stack.insert(0,v)
        """
        stack.insert(0,v)

    def topologicalSort(self):
        visited = [False] * self.V
        stack = []
        ### NOTE this !!! (range(self.V))
        for v in range(self.V):
            # call tologicalSortUtil only if visited[v] == False (the vertice is not visited yet)
            if visited[v] == False:
                self.topologicalSortUtil(v, visited, stack)
        # return the result in inverse order
        return stack[::-1]

### TEST
{"A": 0, "B":1, "C":2, "D": 3}
v = 4
g = Graph(v)
g.addEdge(0, 1)
g.addEdge(0, 2)
g.addEdge(2, 3)
g.addEdge(3, 1)

print (g.graph)

# ans should be TableB, TableD, TableC, TableA.
r = g.topologicalSort()
print (r)
```

### 1-1) Basic OP

## 2) LC Example

### 2-1) Course Schedule
```python
# LC 207 Course Schedule
# NOTE : there are also bracktrack, dfs approachs for this problem
# V0
# IDEA : LC Course Schedule II 
from collections import defaultdict
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return [x for x in range(numCourses)]
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g, res):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!
                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g, res):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            """
            NOTE : the main difference between LC 207, 210
            -> we append idx to res (our ans)
            """
            res.append(idx)
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        res = []
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g, res):
                return False #[]
        return len(res) > 0

# V1
# IDEA : Topological Sort
# https://leetcode.com/problems/course-schedule/solution/
class GNode(object):
    """  data structure represent a vertex in the graph."""
    def __init__(self):
        self.inDegrees = 0
        self.outNodes = []

class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        from collections import defaultdict, deque
        # key: index of node; value: GNode
        graph = defaultdict(GNode)

        totalDeps = 0
        for relation in prerequisites:
            nextCourse, prevCourse = relation[0], relation[1]
            graph[prevCourse].outNodes.append(nextCourse)
            graph[nextCourse].inDegrees += 1
            totalDeps += 1

        # we start from courses that have no prerequisites.
        # we could use either set, stack or queue to keep track of courses with no dependence.
        nodepCourses = deque()
        for index, node in graph.items():
            if node.inDegrees == 0:
                nodepCourses.append(index)

        removedEdges = 0
        while nodepCourses:
            # pop out course without dependency
            course = nodepCourses.pop()

            # remove its outgoing edges one by one
            for nextCourse in graph[course].outNodes:
                graph[nextCourse].inDegrees -= 1
                removedEdges += 1
                # while removing edges, we might discover new courses with prerequisites removed, i.e. new courses without prerequisites.
                if graph[nextCourse].inDegrees == 0:
                    nodepCourses.append(nextCourse)

        if removedEdges == totalDeps:
            return True
        else:
            # if there are still some edges left, then there exist some cycles
            # Due to the dead-lock (dependencies), we cannot remove the cyclic edges
            return False

# V0
# IDEA : DFS + topological sort 
# dfs
from collections import defaultdict
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return True
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!

                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        #print ("g = " + str(p))
        # dfs
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g):
                return False
        return True

# V0
# IDEA : DFS + topological sort 
import collections
class Solution:
    def canFinish(self, numCourses, prerequisites):
        _graph = collections.defaultdict(list)
        for i in range(len(prerequisites)):
            _graph[prerequisites[i][0]].append(prerequisites[i][1])

        visited = [0] * numCourses
        for i in range(numCourses):
            if not self.dfs(_graph, visited, i):
                return False
        return True

    # 0 : unknown, 1 :visiting, 2 : visited    
    def dfs(self, _graph, visited, i):
        if visited[i] == 1:
            return False
        if visited[i] == 2:
            return True
        visited[i] = 1
        for item in _graph[i]:
            if not self.dfs(_graph, visited, item):
                return False
        visited[i] = 2
        return True

# V0'
# IDEA : BFS + topological sort 
from collections import defaultdict, deque
class Solution:
    def canFinish(self, numCourses, prerequisites):
        degree = defaultdict(int)   
        graph = defaultdict(set)
        q = deque()
        
        # init the courses with 0 deg
        for i in range(numCourses):
            degree[i] = 0
        
        # add 1 to degree of course that needs prereq
        # build edge from prerequisite to child course (directed graph)
        for pair in prerequisites:
            degree[pair[0]] += 1
            graph[pair[1]].add(pair[0])
        
        # start bfs queue with all classes that dont have a prerequisite
        for key, val in degree.items():
            if val == 0:
                q.append(key)
                
        stack = []
        
        while q:
            curr = q.popleft()
            stack.append(curr)
            for child in graph[curr]:
                degree[child] -= 1
                if degree[child] == 0:
                    q.append(child)
        
        return len(stack) == numCourses
```

### 2-2) Course Schedule II
```python
# LC 210 Course Schedule II
# V0
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
from collections import defaultdict
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return [x for x in range(numCourses)]
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g, res):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!

                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g, res):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            """
            NOTE : the main difference between LC 207, 210

            -> we append idx to res (our ans)
            """
            res.append(idx)
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        res = []
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g, res):
                return []
        return res

# V0'
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
import collections
class Solution:
    def findOrder(self, numCourses, prerequisites):
        # build graph
        _graph = collections.defaultdict(list)
        for i in range(len(prerequisites)):
            _graph[prerequisites[i][0]].append(prerequisites[i][1])

        visited = [0] * numCourses
        res = []
        for i in range(numCourses):
            if not self.dfs(_graph, visited, i, res):
                return []
        print ("res = " + str(res))
        return res

    # 0 : unknown, 1 :visiting, 2 : visited    
    def dfs(self, _graph, visited, i, res):
        if visited[i] == 1:
            return False
        if visited[i] == 2:
            return True
        visited[i] = 1
        for item in _graph[i]:
            if not self.dfs(_graph, visited, item, res):
                return False
        visited[i] = 2
        res.append(i)
        return True

# V0'
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: List[int]
        """
        graph = collections.defaultdict(list)
        for u, v in prerequisites:
            graph[u].append(v)
        # 0 = Unknown, 1 = visiting, 2 = visited
        visited = [0] * numCourses
        path = []
        for i in range(numCourses):
            ### NOTE : if not a valid "prerequisites", then will return NULL list
            if not self.dfs(graph, visited, i, path):
                return []
        return path
    
    def dfs(self, graph, visited, i, path):
        # 0 = Unknown, 1 = visiting, 2 = visited
        if visited[i] == 1: return False
        if visited[i] == 2: return True
        visited[i] = 1
        for j in graph[i]:
            if not self.dfs(graph, visited, j, path):
                ### NOTE : the quit condition
                return False
        visited[i] = 2
        path.append(i)
        return True
```

### 2-3) alien-dictionary
```python
# 269 alien-dictionary
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, zero_in_degree_queue, in_degree, out_degree = [], collections.deque(), {}, {}
        nodes = sets.Set()
        for word in words:
            for c in word:
                nodes.add(c)
         
        for i in range(1, len(words)):
            if len(words[i-1]) > len(words[i]) and \
                words[i-1][:len(words[i])] == words[i]:
                    return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)
         
        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)
         
        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)
             
            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)
             
                del out_degree[precedence]
         
        if out_degree:
            return ""
 
        return "".join(result)
 
 
    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in range(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = sets.Set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = sets.Set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break　　
```