"""

1626. Best Team With No Conflicts
Medium

You are the manager of a basketball team. For the upcoming tournament, you want to choose the team with the highest overall score. The score of the team is the sum of scores of all the players in the team.

However, the basketball team is not allowed to have conflicts. A conflict exists if a younger player has a strictly higher score than an older player. A conflict does not occur between players of the same age.

Given two lists, scores and ages, where each scores[i] and ages[i] represents the score and age of the ith player, respectively, return the highest overall score of all possible basketball teams.


Example 1:

Input: scores = [1,3,5,10,15], ages = [1,2,3,4,5]
Output: 34
Explanation: You can choose all the players.

Example 2:

Input: scores = [4,5,6,5], ages = [2,1,2,1]
Output: 16
Explanation: It is best to choose the last 3 players. Notice that you are allowed to choose multiple people of the same age.

Example 3:

Input: scores = [1,2,3,5], ages = [8,9,10,1]
Output: 6
Explanation: It is best to choose the first 3 players.


Constraints:

1 <= scores.length, ages.length <= 1000
scores.length == ages.length
1 <= scores[i] <= 10^6
1 <= ages[i] <= 1000

"""

# V0
# IDEA : SORT + LONGEST-INCREASING-SUBSEQUENCE style DP (max sum variant)
#
#   sort players by (score, age). After that, a chosen set is
#   conflict-free iff its ages are non-decreasing in this order:
#   scanning left to right scores never decrease, so we just need ages to
#   never decrease either.
#
#   dp[i] = best total ending with player i
#         = scores[i] + max(dp[j]) over j < i with age[j] <= age[i]
#
#   NOTE : ties on score must be sorted by age so that equal-score pairs
#          are always allowed together (a conflict needs a STRICTLY higher
#          score on the younger player).
#
"""

DP def
    (players are SORTED by (score, age) first)

    dp[i]: max total score of a conflict-free team

           whose LAST picked player is players[i]

           -> e.g. LIS-style DP, but maximising SUM instead of length

DP eq

     dp[i] = scores[i] + max( dp[j] )   for j < i with age[j] <= age[i]

             (0 if no such j)


    -> e.g.
         dp[i] = scores[i] + max(
            0,
            dp[j] for j < i if ages[j] <= ages[i]   # ages non-decreasing
                                                    # -> no conflict
         )

     ans = max(dp)

"""
# time = O(n^2), space = O(n)
class Solution(object):
    def bestTeamScore(self, scores, ages):
        players = sorted(zip(scores, ages))
        n = len(players)
        dp = [0] * n
        res = 0
        for i in range(n):
            score, age = players[i]
            best = 0
            for j in range(i):
                if players[j][1] <= age and dp[j] > best:
                    best = dp[j]
            dp[i] = best + score
            if dp[i] > res:
                res = dp[i]
        return res
