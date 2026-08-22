"""

1583. Count Unhappy Friends
Medium

You are given a list of preferences for n friends, where n is always even.

For each person i, preferences[i] contains a list of friends sorted in the order of preference. In other words, a friend earlier in the list is more preferred than a friend later in the list. Friends in each list are denoted by integers from 0 to n-1.

All the friends are divided into pairs. The pairings are given in a list pairs, where pairs[i] = [xi, yi] denotes xi is paired with yi and yi is paired with xi.

However, this pairing may cause some of the friends to be unhappy. A friend x is unhappy if x is paired with y and there exists a friend u who is paired with v but:

x prefers u over y, and
u prefers x over v.

Return the number of unhappy friends.

Example 1:

Input: n = 4, preferences = [[1, 2, 3], [3, 2, 0], [3, 1, 0], [1, 2, 0]], pairs = [[0, 1], [2, 3]]
Output: 2
Explanation:
Friend 1 is unhappy because:
- 1 is paired with 0 but prefers 3 over 0, and
- 3 prefers 1 over 2.
Friend 3 is unhappy because:
- 3 is paired with 2 but prefers 1 over 2, and
- 1 prefers 3 over 0.
Friends 0 and 2 are happy.

Example 2:

Input: n = 2, preferences = [[1], [0]], pairs = [[1, 0]]
Output: 0
Explanation: Both friends 0 and 1 are happy.

Example 3:

Input: n = 4, preferences = [[1, 3, 2], [2, 3, 0], [1, 3, 0], [0, 2, 1]], pairs = [[1, 3], [0, 2]]
Output: 4

Constraints:

2 <= n <= 500
n is even.
preferences.length == n
preferences[i].length == n - 1
0 <= preferences[i][j] <= n - 1
preferences[i] does not contain i.
All values in preferences[i] are unique.
pairs.length == n/2
pairs[i].length == 2
xi != yi
0 <= xi, yi <= n - 1
Each person is contained in exactly one pair.

"""

# V0
# IDEA : RANK TABLE (O(1) "does a prefer b over c ?" lookups)
#
#   rank[i][j] = position of j inside preferences[i] (smaller = better).
#   x paired with y is unhappy iff SOME u that x likes more than y also
#   likes x more than its own partner :
#     rank[x][u] < rank[x][y]  and  rank[u][x] < rank[u][pair[u]]
#   NOTE : scanning preferences[x] left to right and stopping at y gives
#          exactly the candidates u that x prefers over y.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def unhappyFriends(self, n, preferences, pairs):
        rank = [[0] * n for _ in range(n)]
        for i in range(n):
            for pos, j in enumerate(preferences[i]):
                rank[i][j] = pos

        pair = [0] * n
        for x, y in pairs:
            pair[x] = y
            pair[y] = x

        res = 0
        for x in range(n):
            y = pair[x]
            for u in preferences[x]:
                if u == y:
                    break
                if rank[u][x] < rank[u][pair[u]]:
                    res += 1
                    break
        return res


# V0-1
# IDEA : "BETTER THAN MY PARTNER" SETS + MUTUAL-DESIRE CHECK
#
#   for every person x, the people x likes strictly more than its own partner
#   are exactly the PREFIX of preferences[x] before the partner appears — store
#   that prefix as a set `better[x]`.
#
#   the unhappiness condition then collapses to a symmetric membership test :
#       x is unhappy  <=>  some u in better[x] has x in better[u]
#   i.e. x and u would both rather be with each other than with their partners.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def unhappyFriends(self, n, preferences, pairs):
        partner = [0] * n
        for x, y in pairs:
            partner[x] = y
            partner[y] = x

        better = [set() for _ in range(n)]
        for x in range(n):
            for u in preferences[x]:
                if u == partner[x]:
                    break
                better[x].add(u)

        return sum(1 for x in range(n)
                   if any(x in better[u] for u in better[x]))


# V0-2
# IDEA : ENUMERATE PAIRS OF COUPLES (n/2 CHOOSE 2), 4 CROSS-CHECKS EACH
#
#   the definition of unhappiness always involves exactly TWO couples,
#   (x, y) and (u, v), so enumerate couple pairs instead of people. for each
#   such pair only 4 cross combinations exist :
#       (x,u) (x,v) (y,u) (y,v)
#   and each satisfied combination makes BOTH of its members unhappy at once
#   (the condition is symmetric in the two people it links).
#
#   `rank` dicts give the O(1) "does p prefer s over q ?" test. with n/2
#   couples this is (n/2)^2 * 4 = O(n^2) work but never touches a
#   person-vs-person loop.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def unhappyFriends(self, n, preferences, pairs):
        rank = [{} for _ in range(n)]
        for i in range(n):
            for pos, j in enumerate(preferences[i]):
                rank[i][j] = pos

        unhappy = [False] * n
        k = len(pairs)
        for a in range(k):
            x, y = pairs[a]
            for b in range(a + 1, k):
                u, v = pairs[b]
                for p, q in ((x, y), (y, x)):
                    for s, t in ((u, v), (v, u)):
                        if rank[p][s] < rank[p][q] and rank[s][p] < rank[s][t]:
                            unhappy[p] = True
                            unhappy[s] = True
        return sum(unhappy)
