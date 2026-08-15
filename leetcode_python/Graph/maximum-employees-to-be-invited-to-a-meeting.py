"""

2127. Maximum Employees to Be Invited to a Meeting
Hard

A company is organizing a meeting and has a list of n employees, waiting to be invited. They have arranged for a large circular table, capable of seating any number of employees.

The employees are numbered from 0 to n - 1. Each employee has a favorite person and they will attend the meeting only if they can sit next to their favorite person at the table. The favorite person of an employee is not themself.

Given a 0-indexed integer array favorite, where favorite[i] denotes the favorite person of the ith employee, return the maximum number of employees that can be invited to the meeting.


Example 1:

Input: favorite = [2,2,1,2]
Output: 3
Explanation:
The above figure shows how the company can invite employees 0, 1, and 2, and seat them at the round table.
All employees cannot be invited because employee 2 cannot sit beside employees 0, 1, and 3, simultaneously.
Note that the company can also invite employees 1, 2, and 3, and give them their desired seats.
The maximum number of employees that can be invited to the meeting is 3.

Example 2:

Input: favorite = [1,2,0]
Output: 3
Explanation:
Each employee is the favorite person of at least one other employee, and the only way the company can invite them is if they invite every employee.
The seating arrangement will be the same as that in the figure given in example 1:
- Employee 0 will sit between employees 2 and 1.
- Employee 1 will sit between employees 0 and 2.
- Employee 2 will sit between employees 1 and 0.
The maximum number of employees that can be invited to the meeting is 3.

Example 3:

Input: favorite = [3,0,1,4,1]
Output: 4
Explanation:
The above figure shows how the company will invite employees 0, 1, 3, and 4, and seat them at the round table.
Employee 2 cannot be invited because the two spots next to their favorite employee 1 are taken.
So the company leaves them out of the meeting.
The maximum number of employees that can be invited to the meeting is 4.


Constraints:

n == favorite.length
2 <= n <= 10^5
0 <= favorite[i] <= n - 1
favorite[i] != i

"""

# V0
# IDEA : FUNCTIONAL GRAPH — ONE BIG CYCLE, OR MANY 2-CYCLES WITH TAILS
#
#   every employee points at exactly one favorite, so the graph i -> fav[i]
#   is a functional graph : each component is one cycle with trees hanging
#   off it. only two seating shapes are possible :
#
#   (a) ONE cycle of length >= 3 fills the whole table by itself — nothing
#       else fits, because every seated person needs their favorite adjacent.
#
#   (b) any number of MUTUAL pairs (cycles of length 2) can share the table,
#       each pair extended outwards by the longest chain of admirers feeding
#       into each of its two members.
#
#   so : answer = max(longest cycle >= 3, sum over 2-cycles of the two chains)
#
#   the chains come from a Kahn peel : repeatedly drop indegree-0 nodes,
#   carrying depth[v] = longest chain ending at v. whatever survives with
#   indegree > 0 is exactly the cycles.
#
# time = O(n), space = O(n)
from collections import deque


class Solution(object):
    def maximumInvitations(self, favorite):
        n = len(favorite)
        indeg = [0] * n
        for f in favorite:
            indeg[f] += 1

        depth = [1] * n                       # longest chain ending at node
        q = deque(i for i in range(n) if indeg[i] == 0)
        removed = [False] * n
        while q:
            u = q.popleft()
            removed[u] = True
            v = favorite[u]
            depth[v] = max(depth[v], depth[u] + 1)
            indeg[v] -= 1
            if indeg[v] == 0:
                q.append(v)

        longest_cycle = 0
        pairs_total = 0
        for i in range(n):
            if removed[i]:
                continue
            # walk this cycle once, marking as we go
            length = 0
            cur = i
            while not removed[cur]:
                removed[cur] = True
                length += 1
                cur = favorite[cur]
            if length == 2:
                pairs_total += depth[i] + depth[favorite[i]]
            else:
                longest_cycle = max(longest_cycle, length)

        return max(longest_cycle, pairs_total)
