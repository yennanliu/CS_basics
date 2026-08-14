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
favorite[i] != favorite[favorite[i]]

"""

# V0
# IDEA : FUNCTIONAL GRAPH — EITHER ONE BIG CYCLE, OR ALL 2-CYCLES WITH TAILS
#
#   every employee has exactly one outgoing edge (their favourite), so the
#   graph is a "functional graph" : a set of cycles, each with trees hanging
#   off it. only two seating shapes are possible :
#
#   1. ONE cycle of length >= 3 seated around the whole table. nothing can be
#      attached to it, so its value is just its length.
#   2. any number of 2-CYCLES (mutual pairs), each seated with its longest
#      incoming CHAIN trailing off on both sides. all such pairs can share
#      the table at once, so their values ADD UP.
#
#   step 1 : peel the non-cycle nodes with a Kahn topological pass, recording
#            depth[v] = longest chain ending at v.
#   step 2 : walk what's left (the cycles); take the max length among the
#            long cycles, and sum depth[a] + depth[b] over the 2-cycles.
#   answer = max of the two.
#
# time = O(n), space = O(n)
from collections import deque


class Solution(object):
    def maximumInvitations(self, favorite):
        n = len(favorite)
        indeg = [0] * n
        for f in favorite:
            indeg[f] += 1

        depth = [1] * n          # longest chain ending at this node, itself included
        visited = [False] * n
        q = deque(i for i in range(n) if indeg[i] == 0)
        while q:
            u = q.popleft()
            visited[u] = True
            v = favorite[u]
            depth[v] = max(depth[v], depth[u] + 1)
            indeg[v] -= 1
            if indeg[v] == 0:
                q.append(v)

        max_cycle = 0            # best single long cycle
        pairs_total = 0          # every 2-cycle plus its two trailing chains
        for i in range(n):
            if visited[i]:
                continue
            length = 0
            j = i
            while not visited[j]:
                visited[j] = True
                length += 1
                j = favorite[j]
            if length == 2:
                pairs_total += depth[i] + depth[favorite[i]]
            else:
                max_cycle = max(max_cycle, length)

        return max(max_cycle, pairs_total)
