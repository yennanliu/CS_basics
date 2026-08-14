"""

1446. Consecutive Characters
Easy

The power of the string is the maximum length of a non-empty substring that contains only one unique character.

Given a string s, return the power of s.


Example 1:

Input: s = "leetcode"
Output: 2
Explanation: The substring "ee" is of length 2 with the character 'e' only.

Example 2:

Input: s = "abbcccddddeeeeedcba"
Output: 5
Explanation: The substring "eeeee" is of length 5 with the character 'e' only.


Constraints:

1 <= s.length <= 500
s consists of only lowercase English letters.

"""

# V0
# IDEA : ONE PASS RUN LENGTH (track the current run, keep the best)
#
#   walk from index 1: if s[i] == s[i-1] the current run grows, otherwise
#   the run restarts at length 1. the answer is the longest run seen.
#   NOTE : s is non-empty, so the answer is at least 1.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxPower(self, s):
        res = run = 1
        for i in range(1, len(s)):
            if s[i] == s[i - 1]:
                run += 1
                res = max(res, run)
            else:
                run = 1
        return res
