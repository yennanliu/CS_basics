"""

3332. Maximum Points Tourist Can Earn
Medium

You are given two integers, n and k, along with two 2D integer arrays, stayScore and travelScore.

A tourist is visiting a country with n cities, where each city is directly connected to every other city. The tourist's journey consists of exactly k 0-indexed days, and they can choose any city as their starting point.

Each day, the tourist has two choices:

Stay in the current city: If the tourist stays in their current city curr during day i, they will earn stayScore[i][curr] points.
Move to another city: If the tourist moves from their current city curr to city dest, they will earn travelScore[curr][dest] points.

Return the maximum possible points the tourist can earn.


Example 1:

Input: n = 2, k = 1, stayScore = [[2,3]], travelScore = [[0,2],[1,0]]
Output: 3
Explanation:
The tourist earns the maximum number of points by starting in city 1 and staying in that city.

Example 2:

Input: n = 3, k = 2, stayScore = [[3,4,2],[2,1,2]], travelScore = [[0,2,1],[2,0,4],[3,2,0]]
Output: 8
Explanation:
The tourist earns the maximum number of points by starting in city 1, staying in that city on day 0, and traveling to city 2 on day 1.


Constraints:

1 <= n <= 200
1 <= k <= 200
n == travelScore.length == travelScore[i].length == stayScore[i].length
k == stayScore.length
1 <= stayScore[i][j] <= 100
0 <= travelScore[i][j] <= 100
travelScore[i][i] == 0

"""

# V0
# IDEA : DP OVER (DAY, CITY) — EVERY CITY IS A LEGAL START
#
#   dp[c] = the best total achievable while standing in city c at the start
#   of the current day. seeding it with 0 everywhere encodes "any city may be
#   the starting point".
#
#   a day offers two moves from c :
#       stay      -> dp[c] + stayScore[day][c], still in c
#       travel to d -> dp[c] + travelScore[c][d], now in d
#
#   so each day is an O(n^2) relaxation, and the answer is the best entry
#   after k days.
#
# time = O(k * n^2), space = O(n)
class Solution(object):
    def maxScore(self, n, k, stayScore, travelScore):
        NEG = float('-inf')
        dp = [0] * n
        for day in range(k):
            nxt = [NEG] * n
            for c in range(n):
                cur = dp[c]
                if cur == NEG:
                    continue
                stay = cur + stayScore[day][c]
                if stay > nxt[c]:
                    nxt[c] = stay
                row = travelScore[c]
                for d in range(n):
                    if d == c:
                        continue
                    v = cur + row[d]
                    if v > nxt[d]:
                        nxt[d] = v
            dp = nxt
        return max(dp)
