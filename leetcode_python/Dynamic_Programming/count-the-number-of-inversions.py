"""

3193. Count the Number of Inversions
Hard

You are given an integer n and a 2D array requirements, where requirements[i] = [end_i, cnt_i] represents the end index and the inversion count of each requirement.

A pair of indices (i, j) from an integer array nums is called an inversion if:

i < j and nums[i] > nums[j]

Return the number of permutations perm of [0, 1, 2, ..., n - 1] such that for all requirements[i], perm[0..end_i] has exactly cnt_i inversions.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: n = 3, requirements = [[2,2],[0,0]]
Output: 2
Explanation:
The two permutations are:
[2, 0, 1]
    Prefix [2, 0, 1] has inversions (0, 1) and (0, 2).
    Prefix [2] has 0 inversions.
[1, 2, 0]
    Prefix [1, 2, 0] has inversions (0, 2) and (1, 2).
    Prefix [1] has 0 inversions.

Example 2:

Input: n = 3, requirements = [[2,2],[1,1],[0,0]]
Output: 1
Explanation:
The only satisfying permutation is [2, 0, 1]:
Prefix [2, 0, 1] has inversions (0, 1) and (0, 2).
Prefix [2, 0] has an inversion (0, 1).
Prefix [2] has 0 inversions.

Example 3:

Input: n = 2, requirements = [[0,0],[1,0]]
Output: 1
Explanation:
The only satisfying permutation is [0, 1]:
Prefix [0] has 0 inversions.
Prefix [0, 1] has an inversion (0, 1).


Constraints:

2 <= n <= 300
1 <= requirements.length <= n
requirements[i] = [end_i, cnt_i]
0 <= end_i <= n - 1
0 <= cnt_i <= 400
The input is generated such that there is at least one i such that end_i == n - 1.
The input is generated such that all end_i are unique.

"""

# V0
# IDEA : BUILD THE PERMUTATION LEFT TO RIGHT, COUNTING INVERSIONS AS THEY APPEAR
#
#   think of the permutation as being built one position at a time, deciding
#   the RELATIVE RANK of each new element among those placed so far. dropping
#   a new element into rank r among i existing ones creates exactly (i - r)
#   new inversions, and r ranges over 0..i — so the prefix of length i+1 can
#   gain any number of inversions from 0 to i.
#
#       dp[i][k] = permutations of the first i+1 positions with k inversions
#       dp[i][k] = sum over t = 0..min(i, k) of dp[i-1][k - t]
#
#   that inner sum is a sliding window over dp[i-1], so prefix sums make each
#   row O(maxCnt).
#
#   a requirement at end = i simply erases every dp[i][k] with k != cnt.
#
#   cnt is capped at 400, and any prefix needing more inversions than that is
#   irrelevant, so the table stays 300 x 401.
#
"""

DP def
    build the permutation one position at a time, deciding the RELATIVE RANK
    of each new element among those already placed. dropping a new element
    into rank r among i existing ones creates exactly (i - r) new inversions,
    so a prefix of length i+1 can gain 0..i inversions.

    dp[i][k]: permutations of the first i+1 positions having exactly

              k inversions

DP eq

     dp[i][k] = sum over t = 0..min(i, k) of dp[i-1][k - t]


    -> e.g. that inner sum is a SLIDING WINDOW over dp[i-1], so prefix sums
              make each row O(maxCnt):

         dp[i][k] = pre[k+1] - pre[max(0, k-i)]

     a requirement (end = i, cnt) simply ERASES every dp[i][k] with k != cnt

     init: dp[0][0] = 1
     ans = dp[n-1][ cnt of the requirement at n-1 ] % (10^9 + 7)

"""
# time = O(n * maxCnt), space = O(maxCnt)
class Solution(object):
    def numberOfPermutations(self, n, requirements):
        MOD = 10 ** 9 + 7
        req = {end: cnt for end, cnt in requirements}
        cap = max(req.values())

        dp = [0] * (cap + 1)
        dp[0] = 1
        if 0 in req and req[0] != 0:
            return 0                              # a single element has 0 inversions

        for i in range(1, n):
            pre = [0] * (cap + 2)
            for k in range(cap + 1):
                pre[k + 1] = (pre[k] + dp[k]) % MOD
            nxt = [0] * (cap + 1)
            for k in range(cap + 1):
                lo = max(0, k - i)                # new inversions t <= i
                nxt[k] = (pre[k + 1] - pre[lo]) % MOD
            if i in req:
                c = req[i]
                keep = nxt[c] if c <= cap else 0
                nxt = [0] * (cap + 1)
                if c <= cap:
                    nxt[c] = keep
            dp = nxt

        return dp[req[n - 1]] % MOD
