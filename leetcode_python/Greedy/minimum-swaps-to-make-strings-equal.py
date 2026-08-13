"""

1247. Minimum Swaps to Make Strings Equal
Medium

You are given two strings s1 and s2 of equal length consisting of letters "x" and "y" only. Your task is to make these two strings equal to each other. You can swap any two characters that belong to different strings, which means: swap s1[i] and s2[j].

Return the minimum number of swaps required to make s1 and s2 equal, or return -1 if it is impossible to do so.


Example 1:

Input: s1 = "xx", s2 = "yy"
Output: 1
Explanation: Swap s1[0] and s2[1], s1 = "yx", s2 = "yx".

Example 2:

Input: s1 = "xy", s2 = "yx"
Output: 2
Explanation: Swap s1[0] and s2[0], s1 = "yy", s2 = "xx".
Swap s1[0] and s2[1], s1 = "xy", s2 = "xy".
Note that you cannot swap s1[0] and s1[1] to make s1 equal to "yx", cause we can only swap chars in different strings.

Example 3:

Input: s1 = "xx", s2 = "xy"
Output: -1


Constraints:

1 <= s1.length, s2.length <= 1000
s1.length == s2.length
s1, s2 only contain 'x' or 'y'.

"""

# V0
# IDEA : GREEDY / COUNTING
"""
 Only mismatched columns matter. There are 2 kinds:
   xy : s1[i] = 'x', s2[i] = 'y'
   yx : s1[i] = 'y', s2[i] = 'x'

 - two "xy" columns  -> 1 swap fixes both  (same for two "yx")
 - one "xy" + one "yx" left over -> needs 2 swaps
 - a single leftover column (xy + yx is odd) -> impossible, return -1

 so answer = xy//2 + yx//2 + 2 * (xy % 2)   [xy % 2 == yx % 2 when total is even]
"""
# time = O(n)
# space = O(1)
class Solution(object):
    def minimumSwap(self, s1, s2):
        xy = yx = 0
        for a, b in zip(s1, s2):
            if a == 'x' and b == 'y':
                xy += 1
            elif a == 'y' and b == 'x':
                yx += 1

        if (xy + yx) % 2 == 1:
            return -1

        return xy // 2 + yx // 2 + (xy % 2) + (yx % 2)
