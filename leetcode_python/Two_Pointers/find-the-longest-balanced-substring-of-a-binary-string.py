"""

2609. Find the Longest Balanced Substring of a Binary String
Easy

You are given a binary string s consisting only of zeroes and ones.

A substring of s is considered balanced if all zeroes are before ones and the number of zeroes is equal to the number of ones inside the substring. Notice that the empty substring is considered a balanced substring.

Return the length of the longest balanced substring of s.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "01000111"
Output: 6
Explanation: The longest balanced substring is "000111", which has length 6.

Example 2:

Input: s = "00111"
Output: 4
Explanation: The longest balanced substring is "0011", which has length 4.

Example 3:

Input: s = "111"
Output: 0
Explanation: There is no balanced substring except the empty substring, so the answer is 0.


Constraints:

1 <= s.length <= 50
'0' <= s[i] <= '1'

"""

# V0
# IDEA : RUN-LENGTH COUNTERS (two pointers over the 0-run / 1-run boundary)
#
#   a balanced substring is always "0" * t + "1" * t, i.e. it sits inside a
#   run of zeros immediately followed by a run of ones. so we only need, for
#   each such 0-run/1-run pair, min(zeros, ones) * 2.
#
#   sweep once with two counters:
#     - on '0' : if we were in the middle of a 1-run, a new pair starts, so
#                reset both counters; then zero += 1
#     - on '1' : one += 1, and the best balanced block ending here is
#                2 * min(zero, one)
#
#   NOTE : the reset must happen only on the FIRST '0' after some '1's —
#          that is exactly what `if one: zero = one = 0` detects.
#   NOTE : the empty substring is balanced, so the floor of the answer is 0
#          (example 3).
#
# time = O(n), space = O(1)
class Solution(object):
    def findTheLongestBalancedSubstring(self, s):
        res = 0
        zero = 0
        one = 0
        for c in s:
            if c == '0':
                if one:
                    zero = 0
                    one = 0
                zero += 1
            else:
                one += 1
                if 2 * min(zero, one) > res:
                    res = 2 * min(zero, one)
        return res
