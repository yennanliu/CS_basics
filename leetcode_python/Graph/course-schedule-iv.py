"""

1462. Course Schedule IV
Medium

There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1.
You are given an array prerequisites where prerequisites[i] = [a_i, b_i] indicates that you
must take course a_i first if you want to take course b_i.

For example, the pair [0, 1] indicates that you have to take course 0 before you can take
course 1.

Prerequisites can also be indirect. If course a is a prerequisite of course b, and course b
is a prerequisite of course c, then course a is a prerequisite of course c.

You are also given an array queries where queries[j] = [u_j, v_j]. For the j-th query, you
should answer whether course u_j is a prerequisite of course v_j or not.

Return a boolean array answer, where answer[j] is the answer to the j-th query.

Example 1:

Input: numCourses = 2, prerequisites = [[1,0]], queries = [[0,1],[1,0]]

Output: [false,true]

Explanation: The pair [1, 0] indicates that you have to take course 1 before you can take
course 0. Course 0 is not a prerequisite of course 1, but the opposite is true.

Example 2:

Input: numCourses = 2, prerequisites = [], queries = [[1,0],[0,1]]

Output: [false,false]

Explanation: There are no prerequisites, and each course is independent.

Example 3:

Input: numCourses = 3, prerequisites = [[1,2],[1,0],[2,0]], queries = [[1,0],[1,2]]

Output: [true,true]

Constraints:

2 <= numCourses <= 100
0 <= prerequisites.length <= (numCourses * (numCourses - 1) / 2)
prerequisites[i].length == 2
0 <= a_i, b_i <= numCourses - 1
a_i != b_i
All the pairs [a_i, b_i] are unique.
The prerequisites graph has no cycles.
1 <= queries.length <= 10^4
0 <= u_i, v_i <= numCourses - 1
u_i != v_i

"""

# V0
# IDEA : TOPOLOGICAL SORT (KAHN'S BFS) + PROPAGATE PREREQUISITE SETS
#
#   node = a course, edge a -> b = "a must be taken before b".
#   prereqs[b] = every course (direct or indirect) that must come before b.
#
#   process courses in topological order: when we pop a course `cur`, all of
#   ITS prerequisites are already final (every edge into cur was processed).
#   so for each edge cur -> nxt:
#       prereqs[nxt] |= {cur} | prereqs[cur]
#   after the sort, each query (u, v) is just "u in prereqs[v]" -> O(1).
#
#   e.g. edges 1->2, 1->0, 2->0
#        order 1, 2, 0: prereqs[2] = {1}, prereqs[0] = {1} | {2, 1} = {1, 2}
#        query (1, 0) -> True, query (1, 2) -> True
#
# time = O(n * e + q) (each edge merges a set of up to n courses), space = O(n^2)
from collections import deque


class Solution(object):
    def checkIfPrerequisite(self, numCourses, prerequisites, queries):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :type queries: List[List[int]]
        :rtype: List[bool]
        """
        graph = [[] for _ in range(numCourses)]
        in_degree = [0] * numCourses
        for before, after in prerequisites:
            graph[before].append(after)
            in_degree[after] += 1

        # prereqs[c] = set of every course that must be taken before c
        prereqs = [set() for _ in range(numCourses)]

        # start from the courses that need nothing
        queue = deque()
        for course in range(numCourses):
            if in_degree[course] == 0:
                queue.append(course)

        while queue:
            cur = queue.popleft()
            for nxt in graph[cur]:
                # NOTE !!! pass on cur itself AND everything cur needs
                prereqs[nxt].add(cur)
                prereqs[nxt].update(prereqs[cur])

                in_degree[nxt] -= 1
                # all of nxt's prerequisites are final -> nxt is ready
                if in_degree[nxt] == 0:
                    queue.append(nxt)

        answer = []
        for u, v in queries:
            answer.append(u in prereqs[v])
        return answer


# V1
# IDEA : DFS FROM EVERY COURSE (REACHABILITY MATRIX)
#
#   u is a prerequisite of v  <=>  v is reachable from u following the edges.
#   so run one DFS from each course and mark everything it reaches:
#       reachable[u][v] = True
#   n DFS runs, each O(n + e). Simple and easy to say out loud; a good
#   fallback when topological order does not come to mind.
#
# time = O(n * (n + e) + q), space = O(n^2)
class Solution2(object):
    def checkIfPrerequisite(self, numCourses, prerequisites, queries):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :type queries: List[List[int]]
        :rtype: List[bool]
        """
        graph = [[] for _ in range(numCourses)]
        for before, after in prerequisites:
            graph[before].append(after)

        # reachable[u][v] = True when v can be reached from u
        reachable = [[False] * numCourses for _ in range(numCourses)]

        for source in range(numCourses):
            # iterative DFS: the graph can be a 100-long chain
            stack = [source]
            while stack:
                cur = stack.pop()
                for nxt in graph[cur]:
                    # already marked -> its descendants are already marked too
                    if not reachable[source][nxt]:
                        reachable[source][nxt] = True
                        stack.append(nxt)

        answer = []
        for u, v in queries:
            answer.append(reachable[u][v])
        return answer


# V2
# IDEA : FLOYD-WARSHALL (TRANSITIVE CLOSURE)
#
#   reachable[i][j] starts as "there is a direct edge i -> j".
#   for every middle course k: if i reaches k and k reaches j, then i reaches j.
#   k must be the OUTERMOST loop -- after round k, reachable[i][j] is correct
#   for every path whose middle courses all come from {0..k}.
#   Shortest code of the three; O(n^3) is fine because n <= 100.
#
# time = O(n^3 + q), space = O(n^2)
class Solution3(object):
    def checkIfPrerequisite(self, numCourses, prerequisites, queries):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :type queries: List[List[int]]
        :rtype: List[bool]
        """
        reachable = [[False] * numCourses for _ in range(numCourses)]
        for before, after in prerequisites:
            reachable[before][after] = True

        # NOTE !!! k (the middle course) is the outer loop, not the inner one
        for k in range(numCourses):
            for i in range(numCourses):
                if not reachable[i][k]:
                    continue
                for j in range(numCourses):
                    if reachable[k][j]:
                        reachable[i][j] = True

        answer = []
        for u, v in queries:
            answer.append(reachable[u][v])
        return answer
