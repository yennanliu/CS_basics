"""

1136. Parallel Courses
Medium

You are given an integer n, which indicates that there are n courses labeled from 1 to n.
You are also given an array relations where relations[i] = [prevCourse_i, nextCourse_i],
representing a prerequisite relationship between course prevCourse_i and course nextCourse_i:
course prevCourse_i has to be taken before course nextCourse_i.

In one semester, you can take any number of courses as long as you have taken all the
prerequisites in the previous semester for the courses you are taking.

Return the minimum number of semesters needed to take all courses.
If there is no way to take all the courses, return -1.


Example 1:

Input: n = 3, relations = [[1,3],[2,3]]
Output: 2
Explanation: The figure above represents the given graph.
In the first semester, you can take courses 1 and 2.
In the second semester, you can take course 3.

Example 2:

Input: n = 3, relations = [[1,2],[2,3],[3,1]]
Output: -1
Explanation: No course can be studied because they are prerequisites of each other.


Constraints:

1 <= n <= 5000
1 <= relations.length <= 5000
relations[i].length == 2
1 <= prevCourse_i, nextCourse_i <= n
prevCourse_i != nextCourse_i
All the pairs [prevCourse_i, nextCourse_i] are unique.

"""

# V0
# IDEA : BFS + TOPOLOGICAL SORT (Kahn's algorithm, level by level)
#        each BFS "level" == one semester (all in-degree 0 courses
#        can be taken at the same time)
#        -> if we can't finish all n courses, there is a cycle -> return -1
# time = O(n + m), m = len(relations)
# space = O(n + m)
from collections import deque
class Solution(object):
    def minimumSemesters(self, n, relations):
        g = [[] for _ in range(n + 1)]
        indeg = [0] * (n + 1)
        for prev, nxt in relations:
            g[prev].append(nxt)
            indeg[nxt] += 1

        q = deque([i for i in range(1, n + 1) if indeg[i] == 0])
        taken = 0
        semesters = 0
        while q:
            semesters += 1
            # NOTE !!! process the WHOLE level (= 1 semester) at once
            for _ in range(len(q)):
                cur = q.popleft()
                taken += 1
                for nxt in g[cur]:
                    indeg[nxt] -= 1
                    if indeg[nxt] == 0:
                        q.append(nxt)

        return semesters if taken == n else -1
