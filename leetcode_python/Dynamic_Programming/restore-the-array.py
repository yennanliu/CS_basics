"""

1416. Restore The Array
Hard

A program was supposed to print an array of integers. The program forgot to print whitespaces and the array is printed as a string of digits s and all we know is that all integers in the array were in the range [1, k] and there are no leading zeros in the array.

Given the string s and the integer k, return the number of the possible arrays that can be printed as s using the mentioned program. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: s = "1000", k = 10000
Output: 1
Explanation: The only possible array is [1000]

Example 2:

Input: s = "1000", k = 10
Output: 0
Explanation: There cannot be an array that was printed this way and has all integer >= 1 and <= 10.

Example 3:

Input: s = "1317", k = 2000
Output: 8
Explanation: Possible arrays are [1317],[131,7],[13,17],[1,317],[13,1,7],[1,31,7],[1,3,17],[1,3,1,7]


Constraints:

1 <= s.length <= 10^5
s consists of only digits and does not contain leading zeros.
1 <= k <= 10^9

"""

# V0
# IDEA : DP OVER SUFFIXES (cut the first number, recurse on the rest)
#
#   dp[i] = number of valid splits of s[i:], dp[n] = 1 (empty tail).
#   dp[i] = sum of dp[j+1] over every j such that int(s[i..j]) is in [1, k].
#   NOTE : s[i] == '0' -> no number can start here (leading zero), dp[i] = 0.
#   NOTE : k <= 10^9 has at most 10 digits, so the inner loop runs at most
#          10 times -> the whole thing is linear, not quadratic.
#
# time = O(n * log10(k)), space = O(n)
class Solution(object):
    def numberOfArrays(self, s, k):
        MOD = 10 ** 9 + 7
        n = len(s)
        dp = [0] * (n + 1)
        dp[n] = 1

        for i in range(n - 1, -1, -1):
            if s[i] == '0':
                continue                   # leading zero -> dead end
            num = 0
            for j in range(i, n):
                num = num * 10 + (ord(s[j]) - ord('0'))
                if num > k:
                    break
                dp[i] = (dp[i] + dp[j + 1]) % MOD

        return dp[0]
