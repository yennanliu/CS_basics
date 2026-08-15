"""

3125. Maximum Number That Makes Result of Bitwise AND Zero
Medium
🔒 (premium)

Given an integer n, return the maximum integer x such that x <= n, and the bitwise AND of all the numbers in the range [x, n] is equal to 0.


Example 1:

Input: n = 7
Output: 3
Explanation:
The bitwise AND of [3, 4, 5, 6, 7] is 0.

Example 2:

Input: n = 9
Output: 7
Explanation:
The bitwise AND of [7, 8, 9] is 0.

Example 3:

Input: n = 17
Output: 15
Explanation:
The bitwise AND of [15, 16, 17] is 0.


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : THE ANSWER IS ALWAYS 2^h - 1, WHERE h IS n'S HIGHEST SET BIT
#
#   n has bit h set, so every number from 2^h up to n also has it. an AND
#   over a range that stays inside [2^h, n] therefore keeps bit h and cannot
#   be 0 — which means x must dip below 2^h, i.e. x <= 2^h - 1.
#
#   and x = 2^h - 1 already achieves it : that number has bit h clear and all
#   lower bits set, while 2^h (which is <= n, so it is in the range) has
#   exactly the opposite. their AND alone is 0.
#
#   so the largest valid x is 2^h - 1, and h = n.bit_length() - 1.
#
# time = O(1), space = O(1)
class Solution(object):
    def maxNumber(self, n):
        h = n.bit_length() - 1
        return (1 << h) - 1
