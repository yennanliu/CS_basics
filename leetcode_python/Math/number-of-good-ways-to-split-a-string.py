"""

1525. Number of Good Ways to Split a String
Medium

You are given a string s.

A split is called good if you can split s into two non-empty strings s_left and s_right where their concatenation is equal to s (i.e., s_left + s_right = s) and the number of distinct letters in s_left and s_right is the same.

Return the number of good splits you can make in s.


Example 1:

Input: s = "aacaba"
Output: 2
Explanation: There are 5 ways to split "aacaba" and 2 of them are good.
("a", "acaba") Left string and right string contains 1 and 3 different letters respectively.
("aa", "caba") Left string and right string contains 1 and 3 different letters respectively.
("aac", "aba") Left string and right string contains 2 and 2 different letters respectively (good split).
("aaca", "ba") Left string and right string contains 2 and 2 different letters respectively (good split).
("aacab", "a") Left string and right string contains 3 and 1 different letters respectively.

Example 2:

Input: s = "abcd"
Output: 1
Explanation: Split the string as follows ("ab", "cd").


Constraints:

1 <= s.length <= 10^5
s consists of only lowercase English letters.

"""

# V0
# IDEA : SWEEP THE CUT, MAINTAIN DISTINCT COUNTS ON BOTH SIDES
#
#   start with everything on the right (right = Counter(s), left empty),
#   then move the cut one character at a time :
#     left gains c ; right loses one c, and if right[c] hits 0 the letter
#     disappears from the right side entirely -> drop the key.
#
#   compare len(left) vs len(right) — both are exact distinct-letter counts.
#   NOTE : iterate s[:-1] only, since the right half must stay non-empty.
#
# time = O(n), space = O(26)
from collections import Counter
class Solution(object):
    def numSplits(self, s):
        right = Counter(s)
        left = Counter()
        res = 0
        for c in s[:-1]:
            left[c] += 1
            right[c] -= 1
            if right[c] == 0:
                del right[c]
            if len(left) == len(right):
                res += 1
        return res
