"""

2430. Maximum Deletions on a String
Hard

You are given a string s consisting of only lowercase English letters. In one operation, you can:

Delete the entire string s, or
Delete the first i letters of s if the first i letters of s are equal to the following i letters in s, for any i in the range 1 <= i <= s.length / 2.

For example, if s = "ababc", then in one operation, you could delete the first two letters of s to get "abc", since the first two letters of s and the following two letters of s are both equal to "ab".

Return the maximum number of operations needed to delete all of s.


Example 1:

Input: s = "abcabcdabc"
Output: 2
Explanation:
- Delete the first 3 letters ("abc") since the next 3 letters are equal. Now, s = "abcdabc".
- Delete all the letters.
We used 2 operations so we return 2.
It can be shown that 2 is the maximum number of operations needed.
Note that in the second operation we cannot delete "abc" again because the next occurrence of "abc" is the 4th letter.

Example 2:

Input: s = "aaabaab"
Output: 4
Explanation:
- Delete the first letter ("a") since the next letter is equal. Now, s = "aabaab".
- Delete the first 3 letters ("aab") since the next 3 letters are equal. Now, s = "aab".
- Delete the first letter ("a") since the next letter is equal. Now, s = "ab".
- Delete all the letters.
We used 4 operations so we return 4.
It can be shown that 4 is the maximum number of operations needed.

Example 3:

Input: s = "aaaaa"
Output: 5
Explanation: In each operation, we can delete the first letter of s.


Constraints:

1 <= s.length <= 4000
s consists only of lowercase English letters.

"""

# V0
# IDEA : SUFFIX DP + ROLLING HASH FOR THE O(1) BLOCK COMPARISON
#
#   dp[i] = the most operations that can delete the suffix s[i:]. one option
#   is always "delete the whole remainder" for a single operation, hence the
#   floor of 1. otherwise a prefix block of length L can be dropped when
#       s[i : i+L] == s[i+L : i+2L]
#   giving  1 + dp[i + L].
#
#   the equality test is the expensive part — an LCP table would be
#   4000 x 4000 entries, so a POLYNOMIAL ROLLING HASH is used instead : O(1)
#   per comparison with only O(n) extra memory.
#
#   filling i from the back keeps every dp[i + L] ready.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def deleteString(self, s):
        n = len(s)
        MOD = (1 << 61) - 1
        BASE = 131

        h = [0] * (n + 1)
        pw = [1] * (n + 1)
        for i, c in enumerate(s):
            h[i + 1] = (h[i] * BASE + ord(c)) % MOD
            pw[i + 1] = pw[i] * BASE % MOD

        def sub(i, length):
            """hash of s[i : i+length]"""
            return (h[i + length] - h[i] * pw[length]) % MOD

        dp = [1] * (n + 1)
        dp[n] = 0
        for i in range(n - 1, -1, -1):
            best = 1
            for L in range(1, (n - i) // 2 + 1):
                if sub(i, L) == sub(i + L, L):
                    best = max(best, 1 + dp[i + L])
            dp[i] = best
        return dp[0]
