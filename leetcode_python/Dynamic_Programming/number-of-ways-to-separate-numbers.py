"""

1977. Number of Ways to Separate Numbers
Hard

You wrote down many positive integers in a string called num. However, you realized that you forgot to add commas to seperate the different numbers. You remember that the list of integers was non-decreasing and that no integer had leading zeros.

Return the number of possible lists of integers that you could have written down to get the string num. Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: num = "327"
Output: 2
Explanation: You could have written down the numbers:
3, 27
327

Example 2:

Input: num = "094"
Output: 0
Explanation: No numbers can have leading zeros and all numbers must be positive.

Example 3:

Input: num = "0"
Output: 0
Explanation: No numbers can have leading zeros and all numbers must be positive.


Constraints:

1 <= num.length <= 3500
num consists of digits '0' through '9'.

"""

# V0
# IDEA : DP + PREFIX SUM OVER LAST-BLOCK LENGTH + LCP TABLE
#
#   dp[i][j] = ways to split num[0..i-1] where the LAST number has length <= j
#              (so it is already a prefix sum along j; dp[n][n] is the answer)
#
#   let v = ways where the last number has length EXACTLY j (starts at i-j):
#     - num[i-j] == '0' -> leading zero -> v = 0
#     - the previous number has length k <= j (non-decreasing needs len_prev <= j)
#         * k < j   : always fine, a shorter number is strictly smaller
#         * k == j  : only if num[i-2j .. i-j-1] <= num[i-j .. i-1]
#     so v = dp[i-j][j]                  if that same-length compare passes
#            dp[i-j][min(j-1, i-j)]      otherwise
#   and dp[i][j] = dp[i][j-1] + v.
#
#   comparing two equal-length blocks in O(1) needs lcp[a][b] = length of the
#   longest common prefix of num[a:] and num[b:], built backwards in O(n^2).
#
#   NOTE : dp[0][0] = 1 (empty prefix) is the only base cell ever read.
#
"""

DP def
    dp[i][j]: number of ways to split num[0..i-1] where the LAST number has

              length <= j     (so dp is already a PREFIX SUM along j)

    lcp[a][b]: length of the longest common prefix of num[a:] and num[b:]

               -> lets two equal-length blocks be compared in O(1)

DP eq

     let v = ways where the last number has length EXACTLY j (it starts at i-j):

        num[i-j] == '0'        ->  v = 0                 # leading zero

        the previous number must be <= this one, so its length k <= j:
           k <  j  : always fine (a shorter number is strictly smaller)
           k == j  : only if num[i-2j .. i-j-1] <= num[i-j .. i-1]

        v = dp[i-j][j]                  if the same-length compare passes
          = dp[i-j][min(j-1, i-j)]      otherwise

     dp[i][j] = dp[i][j-1] + v


    -> e.g. lcp is built BACKWARDS in O(n^2):
              lcp[i][j] = lcp[i+1][j+1] + 1 when num[i] == num[j]

     init: dp[0][0] = 1 (the empty prefix) - the only base cell ever read
     ans = dp[n][n] % (10^9 + 7)

"""
# time = O(n^2), space = O(n^2)
class Solution(object):
    def numberOfCombinations(self, num):
        MOD = 10 ** 9 + 7
        n = len(num)
        if num[0] == "0":
            return 0

        # lcp[i][j] = length of common prefix of num[i:] and num[j:]
        lcp = [[0] * (n + 1) for _ in range(n + 1)]
        for i in range(n - 1, -1, -1):
            row = lcp[i]
            nxt = lcp[i + 1]
            for j in range(n - 1, -1, -1):
                if num[i] == num[j]:
                    row[j] = nxt[j + 1] + 1

        def ge(i, j, k):
            # is num[i:i+k] >= num[j:j+k] ?
            x = lcp[i][j]
            return x >= k or num[i + x] >= num[j + x]

        dp = [[0] * (n + 1) for _ in range(n + 1)]
        dp[0][0] = 1
        for i in range(1, n + 1):
            cur = dp[i]
            for j in range(1, i + 1):
                v = 0
                if num[i - j] != "0":
                    if i - 2 * j >= 0 and ge(i - j, i - 2 * j, j):
                        v = dp[i - j][j]
                    else:
                        v = dp[i - j][min(j - 1, i - j)]
                cur[j] = (cur[j - 1] + v) % MOD
        return dp[n][n]
