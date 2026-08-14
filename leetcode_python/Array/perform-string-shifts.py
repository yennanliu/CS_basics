"""

1427. Perform String Shifts
Easy

You are given a string s containing lowercase English letters, and a matrix
shift, where shift[i] = [direction_i, amount_i]:

- direction_i can be 0 (for left shift) or 1 (for right shift).
- amount_i is the amount by which string s is to be shifted.
- A left shift by 1 means remove the first character of s and append it to the end.
- Similarly, a right shift by 1 means remove the last character of s and add it
  to the beginning.

Return the final string after all operations.


Example 1:

Input: s = "abc", shift = [[0,1],[1,2]]
Output: "cab"
Explanation:
[0,1] means shift to left by 1. "abc" -> "bca"
[1,2] means shift to right by 2. "bca" -> "cab"

Example 2:

Input: s = "abcdefg", shift = [[1,1],[1,1],[0,2],[1,3]]
Output: "efgabcd"
Explanation:
[1,1] means shift to right by 1. "abcdefg" -> "gabcdef"
[1,1] means shift to right by 1. "gabcdef" -> "fgabcde"
[0,2] means shift to left by 2. "fgabcde" -> "abcdefg"
[1,3] means shift to right by 3. "abcdefg" -> "efgabcd"


Constraints:

1 <= s.length <= 100
s only contains lower case English letters.
1 <= shift.length <= 100
shift[i].length == 2
direction_i is either 0 or 1.
0 <= amount_i <= 100

"""

# V0
# IDEA : MATH (collapse all shifts into ONE net left-shift)
#
#  -> a right shift by a == a left shift by (n - a)
#  -> so accumulate a net LEFT offset, take it mod n, then slice once
#
# time = O(n + m)
# space = O(n)
# n = len(s), m = len(shift)
class Solution(object):
    def stringShift(self, s, shift):
        n = len(s)
        # NOTE !!! net LEFT offset
        offset = 0
        for d, amount in shift:
            if d == 0:
                offset += amount
            else:
                offset -= amount

        offset %= n  # python % always returns non-negative here
        return s[offset:] + s[:offset]
