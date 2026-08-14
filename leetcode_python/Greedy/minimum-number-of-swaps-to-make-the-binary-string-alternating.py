"""

1864. Minimum Number of Swaps to Make the Binary String Alternating
Medium

Given a binary string s, return the minimum number of character swaps to make it alternating, or -1 if it is impossible.

The string is called alternating if no two adjacent characters are equal. For example, the strings "010" and "1010" are alternating, while the string "0100" is not.

Any two characters may be swapped, even if they are not adjacent.


Example 1:

Input: s = "111000"
Output: 1
Explanation: Swap positions 1 and 4: "111000" -> "101010"
The string is now alternating.

Example 2:

Input: s = "010"
Output: 0
Explanation: The string is already alternating, no swaps are needed.

Example 3:

Input: s = "1110"
Output: -1


Constraints:

1 <= s.length <= 1000
s[i] is either '0' or '1'.

"""

# V0
# IDEA : GREEDY / COUNTING (only 2 target patterns exist)
#
#   an alternating string of length n is fully determined by its FIRST
#   character, so there are only two candidates : "0101..." and "1010...".
#
#   feasibility : those patterns need |#0 - #1| <= 1, else return -1.
#     #0 > #1  -> must start with '0'
#     #1 > #0  -> must start with '1'
#     #0 == #1 -> both are possible, take the cheaper one
#
#   cost : count the positions that mismatch the target. every swap fixes
#   exactly TWO mismatches (a misplaced '0' traded with a misplaced '1'),
#   so cost = mismatches / 2.
#
# time = O(n), space = O(1)
class Solution(object):
    def minSwaps(self, s):
        n = len(s)
        zeros = s.count('0')
        ones = n - zeros

        if abs(zeros - ones) > 1:
            return -1

        def cost(start):
            # target[i] = str((start + i) % 2)
            bad = 0
            for i in range(n):
                if int(s[i]) != (start + i) % 2:
                    bad += 1
            return bad // 2

        if zeros > ones:
            return cost(0)
        if ones > zeros:
            return cost(1)
        return min(cost(0), cost(1))
