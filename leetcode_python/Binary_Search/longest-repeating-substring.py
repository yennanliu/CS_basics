"""

1062. Longest Repeating Substring
Medium

Given a string s, return the length of the longest repeating substrings.
If no repeating substring exists, return 0.


Example 1:

Input: s = "abcd"
Output: 0
Explanation: There is no repeating substring.

Example 2:

Input: s = "abbaba"
Output: 2
Explanation: The longest repeating substrings are "ab" and "ba", each of which occurs twice.

Example 3:

Input: s = "aabcaabdaab"
Output: 3
Explanation: The longest repeating substring is "aab", which occurs 3 times.


Constraints:

1 <= s.length <= 2000
s consists of lowercase English letters.

"""

# V0
# IDEA : BINARY SEARCH on the answer + HASH SET
#
#  monotonic property :
#    if a substring of length L appears twice, then so does one of length L - 1
#    (just drop its last char) -> the predicate "has a repeat of length L"
#    is TRUE for all L <= ans and FALSE after -> binary search it
# time = O(n^2 log n)
# space = O(n^2)
class Solution(object):
    def longestRepeatingSubstring(self, s):
        n = len(s)

        def has_repeat(size):
            seen = set()
            for i in range(n - size + 1):
                sub = s[i:i + size]
                if sub in seen:
                    return True
                seen.add(sub)
            return False

        # search the largest size in [1, n - 1] that still has a repeat
        lo, hi = 1, n - 1
        ans = 0
        while lo <= hi:
            mid = (lo + hi) // 2
            if has_repeat(mid):
                ans = mid
                lo = mid + 1
            else:
                hi = mid - 1
        return ans

# V1
# IDEA : 2D DP (LCS style)
#
#  dp[i][j] = length of the longest common suffix of s[..i] and s[..j], i > j
#  dp[i][j] = dp[i-1][j-1] + 1 if s[i] == s[j] else 0
#  answer = max dp[i][j]
# time = O(n^2)
# space = O(n)
class Solution(object):
    def longestRepeatingSubstring(self, s):
        n = len(s)
        ans = 0
        prev = [0] * n          # dp row for index i - 1
        for i in range(1, n):
            cur = [0] * n
            for j in range(i):
                if s[i] == s[j]:
                    cur[j] = 1 + (prev[j - 1] if j else 0)
                    if cur[j] > ans:
                        ans = cur[j]
            prev = cur
        return ans
