"""

2472. Maximum Number of Non-overlapping Palindrome Substrings
Hard

You are given a string s and a positive integer k.

Select a set of non-overlapping substrings from the string s that satisfy the following conditions:

The length of each substring is at least k.
Each substring is a palindrome.

Return the maximum number of substrings in an optimal selection.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "abaccdbbd", k = 3
Output: 2
Explanation: We can select the substrings underlined in s = "abaccdbbd".
Both "aba" and "dbbd" are palindromes and have a length of at least k = 3.
It can be shown that we cannot find a selection with more than two valid substrings.

Example 2:

Input: s = "adbcda", k = 2
Output: 0
Explanation: There is no palindrome substring of length at least 2 in the string.


Constraints:

1 <= k <= s.length <= 2000
s consists of lowercase English letters.

"""

# V0
# IDEA : INTERVAL SCHEDULING — TAKE THE PALINDROME THAT ENDS EARLIEST
#
#   this is the classic "maximum non-overlapping intervals" problem, whose
#   greedy is: sweep the END positions left to right and grab an interval as
#   soon as one fits after the previous pick.
#
#   so we only need, for each end index e, the SHORTEST qualifying palindrome
#   ending there — equivalently the LARGEST start. expanding around all 2n-1
#   centers enumerates every palindrome and records that per end.
#
#   then one pass over the ends applies the greedy: if best_start[e] sits
#   strictly after the last taken end, take it.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def maxPalindromes(self, s, k):
        n = len(s)
        best_start = [-1] * n          # largest l with s[l..e] a palindrome, len >= k

        for center in range(2 * n - 1):
            l = center // 2
            r = l + center % 2
            while l >= 0 and r < n and s[l] == s[r]:
                if r - l + 1 >= k and l > best_start[r]:
                    best_start[r] = l
                l -= 1
                r += 1

        res = 0
        last_end = -1
        for e in range(n):
            if best_start[e] > last_end:
                res += 1
                last_end = e
        return res
