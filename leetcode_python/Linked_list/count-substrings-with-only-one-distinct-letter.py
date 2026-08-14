"""

1180. Count Substrings with Only One Distinct Letter
Easy

Given a string s, return the number of substrings that have only one distinct letter.

Example 1:

Input: s = "aaaba"
Output: 8
Explanation: The substrings with one distinct letter are "aaa", "aa", "a", "b".
"aaa" occurs 1 time.
"aa" occurs 2 times.
"a" occurs 4 times.
"b" occurs 1 time.
So the answer is 1 + 2 + 4 + 1 = 8.

Example 2:

Input: s = "aaaaaaaaaa"
Output: 55

Constraints:

1 <= s.length <= 1000
s[i] consists of only lowercase English letters.

"""

# V0
# IDEA : TWO POINTERS (group equal-letter runs)
#
#  a run of length L contributes L * (L + 1) / 2 substrings
#
# time = O(n)
# space = O(1)
class Solution(object):
    def countLetters(self, s):
        n = len(s)
        res = 0
        i = 0
        while i < n:
            j = i
            while j < n and s[j] == s[i]:
                j += 1
            L = j - i
            res += L * (L + 1) // 2
            i = j
        return res
