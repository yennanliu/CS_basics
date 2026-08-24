"""

3130. Find All Possible Stable Binary Arrays II
Hard

You are given 3 positive integers zero, one, and limit.

A binary array arr is called stable if:

The number of occurrences of 0 in arr is exactly zero.
The number of occurrences of 1 in arr is exactly one.
Each subarray of arr with a size greater than limit must contain both 0 and 1.

Return the total number of stable binary arrays.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: zero = 1, one = 1, limit = 2
Output: 2
Explanation:
The two possible stable binary arrays are [1,0] and [0,1].

Example 2:

Input: zero = 1, one = 2, limit = 1
Output: 1
Explanation:
The only possible stable binary array is [1,0,1].

Example 3:

Input: zero = 3, one = 3, limit = 2
Output: 14
Explanation:
All the possible stable binary arrays are [0,0,1,0,1,1], [0,0,1,1,0,1], [0,1,0,0,1,1], [0,1,0,1,0,1], [0,1,0,1,1,0], [0,1,1,0,0,1], [0,1,1,0,1,0], [1,0,0,1,0,1], [1,0,0,1,1,0], [1,0,1,0,0,1], [1,0,1,0,1,0], [1,0,1,1,0,0], [1,1,0,0,1,0], and [1,1,0,1,0,0].


Constraints:

1 <= zero, one, limit <= 1000

"""

# V0
# IDEA : SAME RECURRENCE AS LC 3129 — IT WAS ALREADY O(zero * one)
#
#   "no subarray longer than limit misses a value" is exactly "no maximal run
#   exceeds limit", so with
#       dp0[i][j] = arrays of i zeros and j ones ending in 0
#       dp1[i][j] = ... ending in 1
#
#       dp0[i][j] = dp0[i-1][j] + dp1[i-1][j] - dp1[i-limit-1][j]
#
#   where the subtraction drops the arrays whose trailing 0-run was already
#   exactly `limit` (those are a 1-ending array followed by limit zeros).
#
#   this sequel only raises the bounds to 1000, so the table is 10^6 cells —
#   still linear in the state count, no new idea needed. only the rolling
#   rows are kept per i to stay light on memory.
#
#   NOTE : the subtraction can drive the intermediate below 0, so the modulo
#          has to be taken as a true remainder (python's % already is).
#
"""

DP def
    "no subarray longer than limit misses a value" == "no maximal RUN exceeds
    limit" (a subarray missing one value IS a stretch of equal elements)

    dp0[i][j]: arrays using i zeros and j ones that END with 0
    dp1[i][j]: arrays using i zeros and j ones that END with 1

DP eq

     dp0[i][j] = dp0[i-1][j] + dp1[i-1][j] - dp1[i-limit-1][j]

     dp1[i][j] = dp1[i][j-1] + dp0[i][j-1] - dp0[i][j-limit-1]


    -> e.g. the SUBTRACTED term removes the arrays whose trailing 0-run was
              already exactly `limit`: those are precisely a 1-ending array
              followed by `limit` zeros -> dp1[i-limit-1][j]

     init: dp0[i][0] = 1 iff i <= limit   (all-zero array)
           dp1[0][j] = 1 iff j <= limit   (all-one array)

     ans = (dp0[zero][one] + dp1[zero][one]) % (10^9 + 7)

"""
# time = O(zero * one), space = O(zero * one)
class Solution(object):
    def numberOfStableArrays(self, zero, one, limit):
        MOD = 10 ** 9 + 7
        dp0 = [[0] * (one + 1) for _ in range(zero + 1)]
        dp1 = [[0] * (one + 1) for _ in range(zero + 1)]

        for i in range(1, zero + 1):
            dp0[i][0] = 1 if i <= limit else 0
        for j in range(1, one + 1):
            dp1[0][j] = 1 if j <= limit else 0

        for i in range(1, zero + 1):
            row0, row1 = dp0[i], dp1[i]
            up0, up1 = dp0[i - 1], dp1[i - 1]
            far1 = dp1[i - limit - 1] if i - limit - 1 >= 0 else None
            for j in range(1, one + 1):
                v = up0[j] + up1[j]
                if far1 is not None:
                    v -= far1[j]
                row0[j] = v % MOD

                v = row1[j - 1] + row0[j - 1]
                if j - limit - 1 >= 0:
                    v -= dp0[i][j - limit - 1]
                row1[j] = v % MOD

        return (dp0[zero][one] + dp1[zero][one]) % MOD
