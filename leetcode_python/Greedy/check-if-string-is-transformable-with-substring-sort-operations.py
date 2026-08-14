"""

1585. Check If String Is Transformable With Substring Sort Operations
Hard

Given two strings s and t, transform string s into string t using the following operation any number of times:

Choose a non-empty substring in s and sort it in place so the characters are in ascending order.
For example, applying the operation on the underlined substring in "14234" results in "12344".

Return true if it is possible to transform s into t. Otherwise, return false.

A substring is a contiguous sequence of characters within a string.

Example 1:

Input: s = "84532", t = "34852"
Output: true
Explanation: You can transform s into t using the following sort operations:
"84532" (from index 2 to 3) -> "84352"
"84352" (from index 0 to 2) -> "34852"

Example 2:

Input: s = "34521", t = "23415"
Output: true
Explanation: You can transform s into t using the following sort operations:
"34521" -> "23451"
"23451" -> "23415"

Example 3:

Input: s = "12345", t = "12435"
Output: false

Constraints:

s.length == t.length
1 <= s.length <= 10^5
s and t consist of only digits.

"""

# V0
# IDEA : GREEDY + QUEUE PER DIGIT (a sort = bubbling a digit left past bigger ones)
#
#   sorting substrings is exactly "swap two adjacent chars when the left
#   one is bigger", so a digit may move LEFT only across strictly bigger
#   digits, and can never jump over a smaller-or-equal one.
#   build t from the left : for t[i] = d take the earliest unused d in s,
#   at index p ; it is movable to the front iff NO unused digit smaller
#   than d sits before p.
#
# time = O(10 * n), space = O(n)
from collections import deque
class Solution(object):
    def isTransformable(self, s, t):
        pos = [deque() for _ in range(10)]
        for i, c in enumerate(s):
            pos[int(c)].append(i)

        for c in t:
            d = int(c)
            if not pos[d]:
                return False
            p = pos[d][0]
            for smaller in range(d):
                if pos[smaller] and pos[smaller][0] < p:
                    return False
            pos[d].popleft()
        return True
