"""

1494. Parallel Courses II
Hard

You are given an integer n, which indicates that there are n courses labeled from 1 to n. You are also given an array relations where relations[i] = [prevCourse_i, nextCourse_i], representing a prerequisite relationship between course prevCourse_i and course nextCourse_i: course prevCourse_i has to be taken before course nextCourse_i. Also, you are given the integer k.

In one semester, you can take at most k courses as long as you have taken all the prerequisites in the previous semesters for the courses you are taking.

Return the minimum number of semesters needed to take all courses. The testcases will be generated such that it is possible to take every course.


Example 1:

Input: n = 4, relations = [[2,1],[3,1],[1,4]], k = 2
Output: 3
Explanation: The figure above represents the given graph.
In the first semester, you can take courses 2 and 3.
In the second semester, you can take course 1.
In the third semester, you can take course 4.

Example 2:

Input: n = 5, relations = [[2,1],[3,1],[4,1],[1,5]], k = 2
Output: 4
Explanation: The figure above represents the given graph.
In the first semester, you can only take courses 2 and 3 since you cannot take more than two per semester.
In the second semester, you can take course 4.
In the third semester, you can take course 1.
In the fourth semester, you can take course 5.


Constraints:

1 <= n <= 15
1 <= k <= n
0 <= relations.length <= n * (n-1) / 2
relations[i].length == 2
1 <= prevCourse_i, nextCourse_i <= n
prevCourse_i != nextCourse_i
All the pairs [prevCourse_i, nextCourse_i] are unique.
The given graph is a directed acyclic graph.

"""

# V0
# IDEA : BITMASK + BFS over subsets (n <= 15 -> state = set of taken courses)
#
#   pre[i] = bitmask of course i's prerequisites.
#   state `cur` = bitmask of courses already finished. BFS by semester,
#   so the first time we reach the full mask we have the minimum count.
#
#   from `cur`, the courses becoming available are
#       avail = { i : pre[i] subset of cur } \ cur
#   if popcount(avail) <= k -> take them all (never hurts).
#   else -> enumerate every k-sized subset of avail via the classic
#       sub = (sub - 1) & avail  trick.
#
#   NOTE : greedily taking "the most-unlocking" k courses is WRONG here,
#          which is why we must brute force the subsets.
#
# time = O(3^n) worst case, space = O(2^n)
from collections import deque
def popcount(x):
    c = 0
    while x:
        x &= x - 1
        c += 1
    return c
class Solution(object):
    def minNumberOfSemesters(self, n, relations, k):
        pre = [0] * (n + 1)
        for x, y in relations:
            pre[y] |= 1 << x

        full = ((1 << (n + 1)) - 1) - 1   # bits 1..n set, bit 0 unused
        q = deque([(0, 0)])
        seen = set([0])
        while q:
            cur, t = q.popleft()
            if cur == full:
                return t

            avail = 0
            for i in range(1, n + 1):
                if not (cur >> i) & 1 and (cur & pre[i]) == pre[i]:
                    avail |= 1 << i

            if popcount(avail) <= k:
                nxt = cur | avail
                if nxt not in seen:
                    seen.add(nxt)
                    q.append((nxt, t + 1))
            else:
                sub = avail
                while sub:
                    if popcount(sub) == k:
                        nxt = cur | sub
                        if nxt not in seen:
                            seen.add(nxt)
                            q.append((nxt, t + 1))
                    sub = (sub - 1) & avail
        return 0
