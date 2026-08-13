"""

1223. Dice Roll Simulation
Hard

A die simulator generates a random number from 1 to 6 for each roll. You introduced a
constraint to the generator such that it cannot roll the number i more than rollMax[i]
(1-indexed) consecutive times.

Given an array of integers rollMax and an integer n, return the number of distinct
sequences that can be obtained with exact n rolls. Since the answer may be too large,
return it modulo 10^9 + 7.

Two sequences are considered different if at least one element differs from each other.

Example 1:

Input: n = 2, rollMax = [1,1,2,2,2,3]
Output: 34
Explanation: There will be 2 rolls of die, if there are no constraints on the die, there
are 6 * 6 = 36 possible combinations. In this case, looking at rollMax array, the numbers
1 and 2 appear at most once consecutively, therefore sequences (1,1) and (2,2) cannot
occur, so the final answer is 36-2 = 34.

Example 2:

Input: n = 2, rollMax = [1,1,1,1,1,1]
Output: 30

Example 3:

Input: n = 3, rollMax = [1,1,1,2,2,3]
Output: 181


Constraints:

1 <= n <= 5000
rollMax.length == 6
1 <= rollMax[i] <= 15

"""

# V0
# IDEA: 2D DP (last face + current run length)
"""
 DP def:
    - dp[j][x] = number of sequences built so far that END with face j
                 repeated exactly (x + 1) consecutive times
                 (x is 0-indexed, so 0 <= x < rollMax[j])

 DP eq (rolling one more die):
    - new[j][0]  = (total of ALL dp) - (sum of dp[j])   # previous roll was NOT j
    - new[j][x]  = dp[j][x - 1]                         # extend the run of j

 Answer:
    - sum of every dp[j][x] after n rolls
"""
# time = O(n * 6 * M), M = max(rollMax)
# space = O(6 * M)
class Solution(object):
    def dieSimulator(self, n, rollMax):
        MOD = 10 ** 9 + 7
        K = 6

        # after 1 roll : each face, run length 1
        dp = [[0] * rollMax[j] for j in range(K)]
        for j in range(K):
            dp[j][0] = 1

        for _ in range(n - 1):
            total = sum(sum(row) for row in dp) % MOD
            new = [[0] * rollMax[j] for j in range(K)]
            for j in range(K):
                same = sum(dp[j]) % MOD
                # previous roll was a different face -> run of j restarts at 1
                new[j][0] = (total - same) % MOD
                # previous roll was j -> extend the run (if still allowed)
                for x in range(1, rollMax[j]):
                    new[j][x] = dp[j][x - 1]
            dp = new

        return sum(sum(row) for row in dp) % MOD
