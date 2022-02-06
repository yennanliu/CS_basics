# Topology sorting

## 0) Concept  
- Topological Sort is a algorithm can find "ordering" on an "order dependency" graph
- Concept
	- https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/
	- https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html


### 0-1) Types

### 0-2) Pattern

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
        stack.append(v)

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
```

### 1-1) Basic OP

## 2) LC Example


### 2-4) Course Schedule
```python
# LC 207 Course Schedule
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

### 2-5) Course Schedule II
```python
# LC 210 Course Schedule II
```