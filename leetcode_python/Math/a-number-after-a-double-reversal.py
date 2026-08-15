"""

2119. A Number After a Double Reversal
Easy

Reversing an integer means to reverse all its digits.

For example, reversing 2021 gives 1202. Reversing 12300 gives 321 as the leading zeros are not retained.

Given an integer num, reverse num to get reversed1, then reverse reversed1 to get reversed2. Return true if reversed2 equals num. Otherwise return false.


Example 1:

Input: num = 526
Output: true
Explanation: Reverse num to get 625, then reverse 625 to get 526, which equals num.

Example 2:

Input: num = 1800
Output: false
Explanation: Reverse num to get 81, then reverse 81 to get 18, which does not equal num.

Example 3:

Input: num = 0
Output: true
Explanation: Reverse num to get 0, then reverse 0 to get 0, which equals num.


Constraints:

0 <= num <= 10^6

"""

# V0
# IDEA : ONLY TRAILING ZEROS SURVIVE THE ROUND TRIP BADLY
#
#   the first reversal turns trailing zeros into LEADING zeros, which are
#   then dropped — that information is gone for good. every other digit
#   comes back in place.
#
#   so the double reversal is lossless exactly when num has no trailing
#   zero, with 0 itself as the one exception (it reverses to 0).
#
# time = O(1), space = O(1)
class Solution(object):
    def isSameAfterReversals(self, num):
        return num == 0 or num % 10 != 0
