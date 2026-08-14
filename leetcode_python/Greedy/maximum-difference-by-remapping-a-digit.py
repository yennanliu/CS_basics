"""

2566. Maximum Difference by Remapping a Digit
Easy

You are given an integer num. You know that Bob will sneakily remap one of the 10 possible digits (0 to 9) to another digit.

Return the difference between the maximum and minimum values Bob can make by remapping exactly one digit in num.

Notes:

When Bob remaps a digit d1 to another digit d2, Bob replaces all occurrences of d1 in num with d2.
Bob can remap a digit to itself, in which case num does not change.
Bob can remap different digits for obtaining minimum and maximum values respectively.
The resulting number after remapping can contain leading zeroes.


Example 1:

Input: num = 11891
Output: 99009
Explanation:
To achieve the maximum value, Bob can remap the digit 1 to the digit 9 to yield 99899.
To achieve the minimum value, Bob can remap the digit 1 to the digit 0, yielding 890.
The difference between these two numbers is 99009.

Example 2:

Input: num = 90
Output: 99
Explanation:
The maximum value that can be returned by the function is 99 (if 0 is replaced by 9) and the minimum value that can be returned by the function is 0 (if 9 is replaced by 0).
Thus, we return 99.


Constraints:

1 <= num <= 10^8

"""

# V0
# IDEA : GREEDY ON THE MOST SIGNIFICANT DIGIT
#
#   A remap replaces EVERY copy of one digit, so the only lever we have is
#   which digit to pick. Whatever we pick, the change that matters most is the
#   one landing on the leftmost position possible.
#
#   MIN : drive the leading digit down to 0 -> pick d = s[0], map it to '0'.
#         Nothing can beat this, since any other choice leaves s[0] untouched
#         and therefore keeps a strictly larger leading digit.
#
#   MAX : we want the leftmost digit that is not already 9 turned into 9.
#         So scan left to right for the first char != '9' and map it to '9'.
#
#   NOTE : "exactly one" remap is not a constraint in practice - mapping a
#          digit to itself is allowed, so a no-op is always available. That is
#          why the all-9s case (no char != '9') can just return num - mi.
#
#   NOTE : leading zeroes are permitted, so int() on the remapped string is
#          the intended reading (e.g. "00890" -> 890).
#
# time = O(log(num)), space = O(log(num))
class Solution(object):
    def minMaxDifference(self, num):
        s = str(num)
        # minimum : blank out every copy of the leading digit
        mi = int(s.replace(s[0], '0'))
        # maximum : promote the first non-9 digit to 9
        for c in s:
            if c != '9':
                return int(s.replace(c, '9')) - mi
        # already all 9s -> nothing to raise
        return num - mi
