"""

2092. Find All People With Secret
Hard

You are given an integer n indicating there are n people numbered from 0 to n - 1. You are also given a 0-indexed 2D integer array meetings where meetings[i] = [xi, yi, timei] indicates that person xi and person yi have a meeting at timei. A person may attend multiple meetings at the same time. Finally, you are given an integer firstPerson.

Person 0 has a secret and initially shares the secret with a person firstPerson at time 0. This secret can then be shared by any person who knows it to any other person they meet at the same time. More formally, for every meeting, if a person xi has the secret at timei, then they will share the secret with person yi, and vice versa.

The secrets are shared instantaneously. That is, a person may receive the secret and share it with people in other meetings within the same time frame.

Return a list of all the people that have the secret after all the meetings have taken place. You may return the answer in any order.


Example 1:

Input: n = 6, meetings = [[1,2,5],[2,3,8],[1,5,10]], firstPerson = 1
Output: [0,1,2,3,5]
Explanation:
At time 0, person 0 shares the secret with person 1.
At time 5, person 1 shares the secret with person 2.
At time 8, person 2 shares the secret with person 3.
At time 10, person 1 shares the secret with person 5.
Thus, people 0, 1, 2, 3, and 5 know the secret after all the meetings.

Example 2:

Input: n = 4, meetings = [[3,1,3],[1,2,2],[0,3,3]], firstPerson = 3
Output: [0,1,3]
Explanation:
At time 0, person 0 shares the secret with person 3.
At time 2, neither person 1 nor person 2 know the secret.
At time 3, person 3 shares the secret with person 0 and person 1.
Thus, people 0, 1, and 3 know the secret after all the meetings.

Example 3:

Input: n = 5, meetings = [[3,4,2],[1,2,1],[2,3,1]], firstPerson = 1
Output: [0,1,2,3,4]
Explanation:
At time 0, person 0 shares the secret with person 1.
At time 1, person 1 shares the secret with person 2, and person 2 shares the secret with person 3.
Note that person 2 can share the secret at the same time as receiving it.
At time 1, person 3 shares the secret with person 4.
Thus, people 0, 1, 2, 3, and 4 know the secret after all the meetings.


Constraints:

2 <= n <= 10^5
1 <= meetings.length <= 10^5
meetings[i].length == 3
0 <= xi, yi <= n - 1
xi != yi
1 <= timei <= 10^5
1 <= firstPerson <= n - 1

"""

# V0
# IDEA : UNION-FIND PROCESSED IN TIME GROUPS, WITH A ROLLBACK PER GROUP
#
#   meetings at the SAME timestamp all happen simultaneously, so they must be
#   unioned together before deciding who knows the secret — that is what lets
#   the secret hop 1 -> 2 -> 3 within one time frame.
#
#   per time group :
#     1. union every pair in the group
#     2. for every person touched, if their component now contains person 0,
#        they keep the secret; otherwise RESET them to their own singleton —
#        a meeting with only ignorant people must not leak into later groups
#
#   sorting by time and resetting per group keeps this O(m log m).
#
# time = O(m log m * alpha(n)), space = O(n)
from collections import defaultdict


class Solution(object):
    def findAllPeople(self, n, meetings, firstPerson):
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        def union(a, b):
            ra, rb = find(a), find(b)
            if ra != rb:
                parent[ra] = rb

        union(firstPerson, 0)     # time 0 : person 0 tells firstPerson

        groups = defaultdict(list)
        for x, y, t in meetings:
            groups[t].append((x, y))

        for t in sorted(groups):
            people = set()
            for x, y in groups[t]:
                union(x, y)
                people.add(x)
                people.add(y)
            root0 = find(0)
            for p in people:
                if find(p) != root0:
                    parent[p] = p    # never knew it — undo this group for them

        root0 = find(0)
        return [i for i in range(n) if find(i) == root0]
