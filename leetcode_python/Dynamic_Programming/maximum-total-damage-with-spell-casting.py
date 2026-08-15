"""

3186. Maximum Total Damage With Spell Casting
Medium

A magician has various spells.

You are given an array power, where each element represents the damage of a spell. Multiple spells can have the same damage value.

It is a known fact that if a magician decides to cast a spell with a damage of power[i], they cannot cast any spell with a damage of power[i] - 2, power[i] - 1, power[i] + 1, or power[i] + 2.

Each spell can be cast only once.

Return the maximum possible total damage that a magician can cast.


Example 1:

Input: power = [1,1,3,4]
Output: 6
Explanation:
The maximum possible damage of 6 is produced by casting spells 0, 1, 3 with damage 1, 1, 4.

Example 2:

Input: power = [7,1,6,6]
Output: 13
Explanation:
The maximum possible damage of 13 is produced by casting spells 1, 2, 3 with damage 1, 6, 6.


Constraints:

1 <= power.length <= 10^5
1 <= power[i] <= 10^9

"""

# V0
# IDEA : GROUP EQUAL DAMAGES, THEN A "HOUSE ROBBER" DP OVER DISTINCT VALUES
#
#   spells of the SAME damage never block each other, so a value is taken all
#   at once — its contribution is value * count. that collapses the array to
#   the distinct values, sorted.
#
#   taking value v forbids everything in [v-2, v+2], so the DP is
#
#       dp[i] = max( dp[i-1],                     # skip this value
#                    v * cnt + dp[j] )            # take it
#
#   where j is the last index whose value is below v - 2. since the values
#   are sorted, j only moves forward — a single moving pointer finds it, no
#   binary search needed.
#
# time = O(n log n), space = O(n)
from collections import Counter


class Solution(object):
    def maximumTotalDamage(self, power):
        cnt = Counter(power)
        vals = sorted(cnt)
        m = len(vals)

        dp = [0] * (m + 1)
        j = 0                                  # first index with vals[j] >= v - 2
        for i, v in enumerate(vals):
            while vals[j] < v - 2:
                j += 1
            dp[i + 1] = max(dp[i], v * cnt[v] + dp[j])
        return dp[m]
