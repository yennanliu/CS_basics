"""

3129. Find All Possible Stable Binary Arrays I
Medium

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
The two possible stable binary arrays are [1,0] and [0,1], as both arrays have a single 0 and a single 1, and no subarray has a length greater than 2.

Example 2:

Input: zero = 1, one = 2, limit = 1
Output: 1
Explanation:
The only possible stable binary array is [1,0,1].
Note that the binary arrays [1,1,0] and [0,1,1] have subarrays of length 2 with identical elements, hence, they are not stable.

Example 3:

Input: zero = 3, one = 3, limit = 2
Output: 14
Explanation:
All the possible stable binary arrays are [0,0,1,0,1,1], [0,0,1,1,0,1], [0,1,0,0,1,1], [0,1,0,1,0,1], [0,1,0,1,1,0], [0,1,1,0,0,1], [0,1,1,0,1,0], [1,0,0,1,0,1], [1,0,0,1,1,0], [1,0,1,0,0,1], [1,0,1,0,1,0], [1,0,1,1,0,0], [1,1,0,0,1,0], and [1,1,0,1,0,0].

Constraints:

1 <= zero, one, limit <= 200

"""

# V0
# IDEA : "NO SUBARRAY LONGER THAN limit IS UNIFORM" == "NO RUN EXCEEDS limit"
#
#   a subarray missing one of the two values is exactly a stretch of equal
#   elements, so the condition is simply : every maximal run has length
#   <= limit.
#
#   dp[i][j][b] = arrays using i zeros and j ones that END with the value b.
#   appending a 0 to anything that ends in 0 or 1 gives
#
#       dp[i][j][0] = dp[i-1][j][0] + dp[i-1][j][1] - dp[i-limit-1][j][1]
#
#   the subtracted term removes the arrays whose trailing 0-run was already
#   exactly `limit` : those are precisely the ones formed by a 1-ending array
#   followed by limit zeros, i.e. dp[i-limit-1][j][1]. the 1-side is the
#   mirror image.
#
#   bases : all-zero and all-one arrays exist only while they fit in `limit`.
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
        dp0 = [[0] * (one + 1) for _ in range(zero + 1)]     # ends with 0
        dp1 = [[0] * (one + 1) for _ in range(zero + 1)]     # ends with 1

        for i in range(1, zero + 1):
            dp0[i][0] = 1 if i <= limit else 0
        for j in range(1, one + 1):
            dp1[0][j] = 1 if j <= limit else 0

        for i in range(1, zero + 1):
            for j in range(1, one + 1):
                v = dp0[i - 1][j] + dp1[i - 1][j]
                if i - limit - 1 >= 0:
                    v -= dp1[i - limit - 1][j]
                dp0[i][j] = v % MOD

                v = dp1[i][j - 1] + dp0[i][j - 1]
                if j - limit - 1 >= 0:
                    v -= dp0[i][j - limit - 1]
                dp1[i][j] = v % MOD

        return (dp0[zero][one] + dp1[zero][one]) % MOD
