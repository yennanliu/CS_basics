"""

1434. Number of Ways to Wear Different Hats to Each Other
Hard

There are n people and 40 types of hats labeled from 1 to 40.

Given a 2D integer array hats, where hats[i] is a list of all hats preferred by
the ith person.

Return the number of ways that n people can wear different hats from each other.

Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: hats = [[3,4],[4,5],[5]]
Output: 1
Explanation: There is only one way to choose hats given the conditions.
First person chooses hat 3, Second person chooses hat 4 and last one hat 5.

Example 2:

Input: hats = [[3,5,1],[3,5]]
Output: 4
Explanation: There are 4 ways to choose hats:
(3,5), (5,3), (1,3) and (1,5)

Example 3:

Input: hats = [[1,2,3,4],[1,2,3,4],[1,2,3,4],[1,2,3,4]]
Output: 24
Explanation: Each person can choose hats labeled from 1 to 4.
Number of Permutations of (1,2,3,4) = 24.


Constraints:

n == hats.length
1 <= n <= 10
1 <= hats[i].length <= 40
1 <= hats[i][j] <= 40
hats[i] contains a list of unique integers.

"""

# V0
# IDEA : BITMASK DP (iterate over HATS, not people)
#
#  Why hats and not people?
#     - a person may like up to 40 hats -> 2^40 states, too many
#     - but n <= 10 people -> 2^10 = 1024 states of "who is already served"
#
#  DP def:
#     - dp[mask] = # of ways to serve exactly the set of people in `mask`
#                  using only the hats processed so far
#
#  DP eq (processing hat h):
#     - new[mask] = dp[mask]                                # hat h unused
#                 + sum over p in like[h] and p in mask:
#                       dp[mask ^ (1 << p)]                 # hat h -> person p
#
#  answer = dp[(1 << n) - 1]
#
# time = O(40 * 2^n * n)
# space = O(2^n)
from collections import defaultdict
class Solution(object):
    def numberWays(self, hats):
        MOD = 10 ** 9 + 7
        n = len(hats)
        full = (1 << n) - 1

        # like[h] = people who like hat h
        like = defaultdict(list)
        for p, hs in enumerate(hats):
            for h in hs:
                like[h].append(p)

        dp = [0] * (1 << n)
        dp[0] = 1

        for h in range(1, 41):
            if h not in like:
                continue
            # NOTE !!! copy first -> "do not use hat h" is always an option,
            #          and each hat may be given to AT MOST one person
            new = dp[:]
            for mask in range(1 << n):
                if dp[mask] == 0:
                    continue
                for p in like[h]:
                    if not (mask >> p) & 1:
                        new[mask | (1 << p)] = (new[mask | (1 << p)] + dp[mask]) % MOD
            dp = new

        return dp[full] % MOD
