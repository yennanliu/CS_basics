"""

207. Course Schedule
Medium

There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.

For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
Return true if you can finish all courses. Otherwise, return false.

 

Example 1:

Input: numCourses = 2, prerequisites = [[1,0]]
Output: true
Explanation: There are a total of 2 courses to take. 
To take course 1 you should have finished course 0. So it is possible.
Example 2:

Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: false
Explanation: There are a total of 2 courses to take. 
To take course 1 you should have finished course 0, and to take course 0 you should also have finished course 1. So it is impossible.
 

Constraints:

1 <= numCourses <= 105
0 <= prerequisites.length <= 5000
prerequisites[i].length == 2
0 <= ai, bi < numCourses
All the pairs prerequisites[i] are unique.


"""


# V0
# IDEA 1) TOPO SORT
from collections import deque

# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):

        # adjacency list: prereq -> courses that depend on it
        """
        NOTE !!!


        graph: {pre: [next_1, next_2, ....]}


        https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/BFS/CourseSchedule2.java#L107
        
        """
        graph = {i: [] for i in range(numCourses)}

        indegree = [0] * numCourses

        # build graph
        for cur, pre in prerequisites:
            graph[pre].append(cur)
            # NOTE !!!
            # we update `degree` on `next` course
            indegree[cur] += 1

        # start with all courses that have no prerequisites
        q = deque()

        for i in range(numCourses):
            if indegree[i] == 0:
                q.append(i)

        count = 0

        while q:
            # NOTE !!!
            # use `popleft()` in BFS in py
            cur = q.popleft()
            count += 1

            # unlock next courses
            for nxt in graph[cur]:
                """
                NOTE !!!

                -> we update degree on `next` course
                -> if any `next` course has `0` degree
                    -> add to queue (BFS)
                """
                indegree[nxt] -= 1
                if indegree[nxt] == 0:
                    q.append(nxt)

        return count == numCourses


# V0
# IDEA: DFS + STATUS CHECK
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # graph[course] = list of prerequisites for that course
        graph = {}

        # initialize an empty prerequisite list for every course
        # cur_pre: [course, [pre_1, pre_2, ...]]
        for course in range(numCourses):
            graph[course] = []

        # build the graph
        # [a, b] means:
        #   take b before a
        #   therefore a depends on b

        # V1
        # for course, pre in prerequisites:
        #     graph[course].append(pre)

        # V2
        for ai, bi in prerequisites:
            cur_pre[ai].append(bi)

        # status meaning:
        # 0 = unvisited
        # 1 = currently visiting (in recursion stack)
        # 2 = fully processed (no cycle found below this node)
        status = [0] * numCourses

        # run DFS from every course
        # some courses may belong to disconnected components
        for course in range(numCourses):

            # NOTE !!!
            # -> need to `prapagate` the `False`
            # if a cycle is found, impossible to finish all courses
            if not self.dfs(course, graph, status):
                return False

        # no cycle found anywhere
        return True

    # NOTE !!!
    # we DON'T need `visited` in dfs helper func
    def dfs(self, course, graph, status):

        # Case 1:
        # already completely processed before
        # no need to search again
        if status[course] == 2:
            return True

        # Case 2:
        # we reached a node already in the current DFS path
        # cycle detected
        # NOTE !!!
        # -> status[course] == 1
        # -> is enough for cycle check
        if status[course] == 1:
            return False

        # mark current node as "visiting"
        status[course] = 1

        # visit all prerequisites
        for pre in graph[course]:

            # if any prerequisite leads to a cycle
            # propagate failure upward
            if not self.dfs(pre, graph, status):
                return False

        # all prerequisites checked successfully
        # mark current node as completely processed
        status[course] = 2

        return True



# V0-1
# IDEA: DFS + STATUS CHECK
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # Step 1: Map out the adjacency list dependencies graph
        cur_pre = {}
        for i in range(numCourses):
            cur_pre[i] = []
        for ai, bi in prerequisites:
            # CRITICAL FIX: Course ai depends on prerequisite bi
            cur_pre[ai].append(bi)
            
        # Step 2: Initialize our 3-state tracking list
        # 0: not visited, 1: visiting, 2: visited
        status = [0] * numCourses
        
        # Step 3: Run cycle detection across all courses
        for i in range(numCourses):
            # CRITICAL FIX: Pass the actual status array instead of the integer 0
            if not self.helper(i, status, cur_pre):
                return False
                
        return True

    # CRITICAL FIX: Removed the redundant visited list completely 
    def helper(self, cur, status, cur_pre):
        # If already fully processed and verified, this path is safe!
        if status[cur] == 2:
            return True 
            
        # If it's already in the "visiting" state on this path, we found a cycle!
        if status[cur] == 1:
            return False
            
        # Mark as visiting
        status[cur] = 1
        
        for pre in cur_pre[cur]:
            # CRITICAL FIX: Capture the return value! 
            # If a prerequisite contains a cycle, bubble False up immediately.
            if not self.helper(pre, status, cur_pre):
                return False
                
        # Mark as fully visited / processed
        status[cur] = 2
        return True


# V0
# IDEA : DFS, LC Course Schedule II
from collections import defaultdict
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        def dfs(res, graph, visited, x):
            # 0 : not visited
            # 1 : visiting
            # 2 : visited
            if visited[x] == 1:
                return False
            # NOTE this !!!!
            if visited[x] == 2:
                return True
            visited[x] = 1
            for k in graph[x]:
                # NOTE THIS !!!! -> use dfs, instead of "not visited[k]"
                #if not visited[k]:
                if not dfs(res, graph, visited, k):
                    #dfs(graph, visited, k)
                    return False
            # NOTE this !!!!
            visited[x] = 2
            res.insert(0, x)
            return True
        # edge case
        if not prerequisites:
            return True
        # build graph
        graph = defaultdict(list)
        for p in prerequisites:
            graph[p[0]].append(p[1])
        # dfs
        visited = [0] * numCourses
        res = []
        for c in range(numCourses-1):
            # NOTE this !!!!
            if not dfs(res, graph, visited, c):
                return False
        return len(res) > 0

# V0'
# IDEA : DFS
# https://github.com/neetcode-gh/leetcode/blob/main/python/0207-course-schedule.py
# https://www.youtube.com/watch?v=EgI5nU9etnU
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution:
    def canFinish(self, numCourses: int, prerequisites: List[List[int]]) -> bool:
        # dfs
        preMap = {i: [] for i in range(numCourses)}

        # map each course to : prereq list
        for crs, pre in prerequisites:
            preMap[crs].append(pre)

        visiting = set()

        def dfs(crs):
            if crs in visiting:
                return False
            if preMap[crs] == []:
                return True

            visiting.add(crs)
            for pre in preMap[crs]:
                if not dfs(pre):
                    return False
            visiting.remove(crs)
            preMap[crs] = []
            return True

        for c in range(numCourses):
            if not dfs(c):
                return False
        return True

# V0
# IDEA : DFS, LC Course Schedule II
from collections import defaultdict
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
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

# V0'
# IDEA : DFS
from collections import defaultdict
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        def check(course):
            # 0 : not visited yet
            # 1 : visiting
            # 2 : visited 
            """
            NOTE : we DON'T need below :
                #if visited[course] == 2:
            """
            if visited[course] == 1:
                return False
            if visited[course] == 2:
                return True
            visited[course] = 1
            for c in g[course]:
                """
                NOTE !!! : 

                 we DON'T need below :
                    # if visited[c] != 2:
                    #     visited[c] = 2
                    #     check(c)
                """
                if not check(c):
                    return False
            visited[course] = 2
            return True
        # edge case
        if not prerequisites or len(prerequisites) == 1:
            return True
        # build graph
        g = defaultdict(list)
        for a, b in prerequisites:
            g[a].append(b)
            """
            NOTE !!! we DON'T need below : 
                # g[b].append(a)
            """
        """
        NOTE we init visited, courses as below
        """
        visited = [0  for _ in range(numCourses)]
        courses = [_ for _ in range(numCourses)]
        for course in courses:
            if not check(course):
                return False
        return True

# V0'
# IDEA : DFS
from collections import defaultdict
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
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

# V0''
# IDEA : Backtracking
# https://leetcode.com/problems/course-schedule/solution/
# IDEA : -> check : if the corresponding graph is a DAG (Directed Acyclic Graph), i.e. there is no cycle existed in the graph.
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        from collections import defaultdict
        courseDict = defaultdict(list)

        for relation in prerequisites:
            nextCourse, prevCourse = relation[0], relation[1]
            courseDict[prevCourse].append(nextCourse)

        path = [False] * numCourses
        for currCourse in range(numCourses):
            if self.isCyclic(currCourse, courseDict, path):
                return False
        return True


    def isCyclic(self, currCourse, courseDict, path):
        """
        backtracking method to check that no cycle would be formed starting from currCourse
        """
        if path[currCourse]:
            # come across a previously visited node, i.e. detect the cycle
            return True

        # before backtracking, mark the node in the path
        path[currCourse] = True

        # backtracking
        ret = False
        for child in courseDict[currCourse]:
            ret = self.isCyclic(child, courseDict, path)
            if ret: break

        # after backtracking, remove the node from the path
        path[currCourse] = False
        return ret

# V0''''
# IDEA : DFS + topological sort
import collections
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
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

# V0''''''
# IDEA : BFS + topological sort
from collections import defaultdict, deque
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
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

# V0''''''''
# IDEA : DFS + topological sort
import collections
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, N, prerequisites):
        graph = collections.defaultdict(list)
        for u, v in prerequisites:
            graph[u].append(v)
        # 0 = Unknown, 1 = visiting, 2 = visited
        visited = [0] * N
        for i in range(N):
            if not self.dfs(graph, visited, i):
                return False
        return True

    # Can we add node i to visited successfully?
    def dfs(self, graph, visited, i):
        # 0 = Unknown, 1 = visiting, 2 = visited
        if visited[i] == 1: return False
        if visited[i] == 2: return True
        visited[i] = 1
        for j in graph[i]:
            if not self.dfs(graph, visited, j):
                return False
        visited[i] = 2
        return True

# V0''''''' (AGAIN!)
# IDEA : BFS + topological sort
# time = O(V^2), V = numCourses (nested scan for zero indegree each round)
# space = O(V + E)
class Solution(object):
    def canFinish(self, N, prerequisites):
        graph = collections.defaultdict(list)
        indegrees = collections.defaultdict(int)
        for u, v in prerequisites:
            graph[v].append(u)
            indegrees[u] += 1
        for i in range(N):
            zeroDegree = False
            for j in range(N):
                if indegrees[j] == 0:
                    zeroDegree = True
                    break
            if not zeroDegree: return False
            indegrees[j] = -1
            for node in graph[j]:
                indegrees[node] -= 1
        return True 

# V1
# IDEA : BFS + topological sort
# https://leetcode.com/problems/course-schedule/discuss/811500/Python-Intuitive-Solution
from collections import defaultdict, deque
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
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

# V1
# IDEA : Backtracking
# https://leetcode.com/problems/course-schedule/solution/
# IDEA : -> check : if the corresponding graph is a DAG (Directed Acyclic Graph), i.e. there is no cycle existed in the graph.
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        from collections import defaultdict
        courseDict = defaultdict(list)

        for relation in prerequisites:
            nextCourse, prevCourse = relation[0], relation[1]
            courseDict[prevCourse].append(nextCourse)

        path = [False] * numCourses
        for currCourse in range(numCourses):
            if self.isCyclic(currCourse, courseDict, path):
                return False
        return True


    def isCyclic(self, currCourse, courseDict, path):
        """
        backtracking method to check that no cycle would be formed starting from currCourse
        """
        if path[currCourse]:
            # come across a previously visited node, i.e. detect the cycle
            return True

        # before backtracking, mark the node in the path
        path[currCourse] = True

        # backtracking
        ret = False
        for child in courseDict[currCourse]:
            ret = self.isCyclic(child, courseDict, path)
            if ret: break

        # after backtracking, remove the node from the path
        path[currCourse] = False
        return ret

# V1
# IDEA : DFS
# https://leetcode.com/problems/course-schedule/solution/
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        from collections import defaultdict
        courseDict = defaultdict(list)

        for relation in prerequisites:
            nextCourse, prevCourse = relation[0], relation[1]
            courseDict[prevCourse].append(nextCourse)

        checked = [False] * numCourses
        path = [False] * numCourses

        for currCourse in range(numCourses):
            if self.isCyclic(currCourse, courseDict, checked, path):
                return False
        return True


    def isCyclic(self, currCourse, courseDict, checked, path):
        """   """
        # 1). bottom-cases
        if checked[currCourse]:
            # this node has been checked, no cycle would be formed with this node.
            return False
        if path[currCourse]:
            # came across a marked node in the path, cyclic !
            return True

        # 2). postorder DFS on the children nodes
        # mark the node in the path
        path[currCourse] = True

        ret = False
        # postorder DFS, to visit all its children first.
        for child in courseDict[currCourse]:
            ret = self.isCyclic(child, courseDict, checked, path)
            if ret: break

        # 3). after the visits of children, we come back to process the node itself
        # remove the node from the path
        path[currCourse] = False

        # Now that we've visited the nodes in the downstream,
        #   we complete the check of this node.
        checked[currCourse] = True
        return ret

# V1
# IDEA : Topological Sort
# https://leetcode.com/problems/course-schedule/solution/
class GNode(object):
    """  data structure represent a vertex in the graph."""
    def __init__(self):
        self.inDegrees = 0
        self.outNodes = []

# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
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

# V1''
# IDEA : BFS + topological sort
# https://leetcode.com/problems/course-schedule/discuss/1656939/python
from collections import defaultdict, deque
# time = O(V + E), V = numCourses, E = len(prereq)
# space = O(V + E)
class Solution:
    def canFinish(self, n, prereq):
            G = [[] for _ in range(n)]
            indeg = [0] * n
            for v, u in prereq:
                G[u].append(v)
                indeg[v] += 1

            q = [u for u, d in enumerate(indeg) if not d]
            # since we only track sink nodes. we don't need track which nodes are visited or not because sink nodes are guaranteed not to be visited again
            while q:
                u = q.pop()
                n -= 1
                for v in G[u]:
                    indeg[v] -= 1
                    if not indeg[v]:
                        q.append(v)
            return n == 0

# V1'''
# IDEA : dfs + topological sort
# https://leetcode.com/problems/course-schedule/discuss/1041737/Python-DFS
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution:
    def canFinish(self, numCourses, prerequisites):
        g = [[] for _ in range(numCourses)]
        visit = [0]*numCourses
        
        for post, pre in prerequisites:
            g[post].append(pre)
            
        def dfs(node):
            if visit[node] == -1:
                return False
            if visit[node] == 1:
                return True
            
            visit[node] = -1
            for neighbor in g[node]:
                if not dfs(neighbor):
                    return False
            visit[node] = 1
            return True
        
        for i in range(numCourses):
            if not dfs(i):
                return False
        return True

# V1''''
# https://blog.csdn.net/fuxuemingzhu/article/details/82951771
# diagram explaination:
# https://leetcode.com/problems/course-schedule/discuss/658379/Python-by-DFS-and-cycle-detection-w-Graph
# IDEA : DFS + topological sort
# time = O(V + E), V = N, E = len(prerequisites)
# space = O(V + E)
class Solution(object):
    def canFinish(self, N, prerequisites):
        """
        :type N,: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        graph = collections.defaultdict(list)
        for u, v in prerequisites:
            graph[u].append(v)
        # 0 = Unknown, 1 = visiting, 2 = visited
        visited = [0] * N
        for i in range(N):
            if not self.dfs(graph, visited, i):
                return False
        return True

    # Can we add node i to visited successfully?
    def dfs(self, graph, visited, i):
        # 0 = Unknown, 1 = visiting, 2 = visited
        if visited[i] == 1: return False
        if visited[i] == 2: return True
        visited[i] = 1
        for j in graph[i]:
            if not self.dfs(graph, visited, j):
                return False
        visited[i] = 2
        return True

# V1'''''
# IDEA : DFS + topological sort
# https://leetcode.com/problems/course-schedule/discuss/203028/Python-solution
# IDEA :
# We first build a directed graph from prerequisites. The nodes are 0 to n-1, and there is an edge from i to j if i is the prerequisite of j. Then the courses can be finished if and only if the directed graph can be topologically sorted (equivalently, if and only if the directed graph is acyclic).
# We start by labelling each node 0, meaning that they have not been dfs visited. Then we iterate i in range(numCourses), and if i has not been dfs visited, we dfs visit i. If any of such dfs visits return False, we return False; Else we return True. For the dfs visit procedure, we first label i to be 1, meaning that we are currently dfs visiting the descendants of i in the dfs tree. Then for each neighbor j of i, If j has label 1, then j is a predecessor of i in the dfs visit, and i -> j is a back edge, so the graph contains a cycle, we return False; Else if j has label 0, it has not been visited, and we need to do dfs(j). If dfs(j) returns False, it means that the dfs subgraph starting with j contains a cycle, and we need to return False. Finally, if no dfs(j) returns False, it means that the dfs subgraph starting with i is acyclic, we label i to be 2, meaning that we finished dfs searches all the descendants of i, and we return True.
# The time complexity is O(n+m), and the space complexity is O(n+m), where n = numCourses, and m = len(prerequisites).
# time = O(n + m), n = numCourses, m = len(prerequisites)
# space = O(n + m)
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        def dfs(i):
            color[i] = 1
            if i in graph:
                for j in graph[i]:
                    if color[j] == 0:
                        if not dfs(j):
                            return False
                    elif color[j] == 1:
                        return False
            color[i] = 2
            return True
                            
        graph = {}
        for pair in prerequisites:
            if pair[1] in graph:
                graph[pair[1]].add(pair[0])
            else:
                graph[pair[1]] = set([pair[0]])         
        color = [0]*numCourses
        for i in range(numCourses):
            if color[i] == 0:
                if not dfs(i):
                    return False
        return True

# V1''''''
# https://www.jiuzhang.com/solution/course-schedule/#tag-highlight-lang-python
from collections import deque
# time = O(V + E), V = numCourses, E = len(prerequisites)
# space = O(V + E)
class Solution:
    # @param {int} numCourses a total of n courses
    # @param {int[][]} prerequisites a list of prerequisite pairs
    # @return {boolean} true if can finish all courses or false
    def canFinish(self, numCourses, prerequisites):
        # Write your code here
        edges = {i: [] for i in range(numCourses)}
        degrees = [0 for i in range(numCourses)] 
        for i, j in prerequisites:
            edges[j].append(i)
            degrees[i] += 1
            
        queue, count = deque([]), 0
        
        for i in range(numCourses):
            if degrees[i] == 0:
                queue.append(i)

        while queue:
            node = queue.popleft()
            count += 1

            for x in edges[node]:
                degrees[x] -= 1
                if degrees[x] == 0:
                    queue.append(x)
        return count == numCourses

# V1''''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/82951771
# IDEA : BFS + topological sort
# time = O(V^2), V = N (nested scan for zero indegree each round)
# space = O(V + E)
class Solution(object):
    def canFinish(self, N, prerequisites):
        """
        :type N,: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        graph = collections.defaultdict(list)
        indegrees = collections.defaultdict(int)
        for u, v in prerequisites:
            graph[v].append(u)
            indegrees[u] += 1
        for i in range(N):
            zeroDegree = False
            for j in range(N):
                if indegrees[j] == 0:
                    zeroDegree = True
                    break
            if not zeroDegree: return False
            indegrees[j] = -1
            for node in graph[j]:
                indegrees[node] -= 1
        return True

# V2
# time = O(|V| + |E|)
# space = O(|E|)
from collections import defaultdict, deque
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        zero_in_degree_queue = deque()
        in_degree, out_degree = defaultdict(set), defaultdict(set)

        for i, j in prerequisites:
            in_degree[i].add(j)
            out_degree[j].add(i)

        for i in xrange(numCourses):
            if i not in in_degree:
                zero_in_degree_queue.append(i)

        while zero_in_degree_queue:
            prerequisite = zero_in_degree_queue.popleft()

            if prerequisite in out_degree:
                for course in out_degree[prerequisite]:
                    in_degree[course].discard(prerequisite)
                    if not in_degree[course]:
                        zero_in_degree_queue.append(course)

                del out_degree[prerequisite]

        if out_degree:
            return False

        return True