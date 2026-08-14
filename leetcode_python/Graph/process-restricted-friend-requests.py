"""

2076. Process Restricted Friend Requests
Hard

You are given an integer n indicating the number of people in a network. Each person is labeled from 0 to n - 1.

You are also given a 0-indexed 2D integer array restrictions, where restrictions[i] = [xi, yi] means that person xi and person yi cannot become friends, either directly or indirectly through other people.

Initially, no one is friends with each other. You are given a list of friend requests as a 0-indexed 2D integer array requests, where requests[j] = [uj, vj] is a friend request between person uj and person vj.

A friend request is successful if uj and vj can be friends. Each friend request is processed in the given order (i.e., requests[j] occurs before requests[j + 1]), and upon a successful request, uj and vj become direct friends for all future friend requests.

Return a boolean array result, where each result[j] is true if the jth friend request is successful or false if it is not.

Note: If uj and vj are already direct friends, the request is still successful.


Example 1:

Input: n = 3, restrictions = [[0,1]], requests = [[0,2],[2,1]]
Output: [true,false]
Explanation:
Request 0: Person 0 and person 2 can be friends, so they become direct friends.
Request 1: Person 2 and person 1 cannot be friends since person 0 and person 1 would be indirect friends (1--2--0).

Example 2:

Input: n = 3, restrictions = [[0,1]], requests = [[1,2],[0,2]]
Output: [true,false]
Explanation:
Request 0: Person 1 and person 2 can be friends, so they become direct friends.
Request 1: Person 0 and person 2 cannot be friends since person 0 and person 1 would be indirect friends (0--2--1).

Example 3:

Input: n = 5, restrictions = [[0,1],[1,2],[2,3]], requests = [[0,4],[1,2],[3,1],[3,4]]
Output: [true,false,true,false]
Explanation:
Request 0: Person 0 and person 4 can be friends, so they become direct friends.
Request 1: Person 1 and person 2 cannot be friends since they are directly restricted.
Request 2: Person 3 and person 1 can be friends, so they become direct friends.
Request 3: Person 3 and person 4 cannot be friends since person 0 and person 1 would be indirect friends.


Constraints:

2 <= n <= 1000
0 <= restrictions.length <= 1000
restrictions[i].length == 2
0 <= xi, yi <= n - 1
xi != yi
1 <= requests.length <= 1000
requests[j].length == 2
0 <= uj, vj <= n - 1
uj != vj

"""

# V0
# IDEA : UNION-FIND WITH A "DRY RUN" CHECK AGAINST EVERY RESTRICTION
#
#   for a request (u, v), merging is allowed unless some restriction pair
#   (x, y) would end up in the same component afterwards, i.e.
#       (find(x) == ru and find(y) == rv) or (find(x) == rv and find(y) == ru)
#   where ru = find(u), rv = find(v).
#
#   scan all restrictions before committing; only union when none is violated.
#
#   NOTE : n and both list sizes are <= 1000, so the O(requests * restrictions)
#          check is fine (~10^6 union-find lookups).
#
# time = O(R * K * alpha(n)), space = O(n)
class Solution(object):
    def friendRequests(self, n, restrictions, requests):
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]
                x = parent[x]
            return x

        res = []
        for u, v in requests:
            ru, rv = find(u), find(v)
            if ru == rv:
                res.append(True)
                continue
            ok = True
            for x, y in restrictions:
                rx, ry = find(x), find(y)
                if (rx == ru and ry == rv) or (rx == rv and ry == ru):
                    ok = False
                    break
            res.append(ok)
            if ok:
                parent[ru] = rv
        return res
