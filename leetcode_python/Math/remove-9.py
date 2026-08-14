"""

660. Remove 9
Hard

Start from integer 1, remove any integer that contains 9 such as 9, 19, 29...

Now, you will have a new integer sequence [1, 2, 3, 4, 5, 6, 7, 8, 10, 11, ...].

Given an integer n, return the nth (1-indexed) integer in the new sequence.

Example 1:

Input: n = 9
Output: 10

Example 2:

Input: n = 10
Output: 11

Constraints:

1 <= n <= 8 * 10^8

"""

# V0
# IDEA : BASE 9 -- the sequence IS the base-9 numbers read as base-10
#
#   Removing every integer containing the digit 9 leaves exactly the numbers
#   whose decimal digits all come from {0..8} -- i.e. valid base-9 numerals.
#   Listed in increasing order they line up one-to-one with 1, 2, 3, ... in base 9.
#
#   So: write n in base 9, then reinterpret those same digits as a decimal number.
#
#     n = 9  -> base9 "10" -> 10
#     n = 10 -> base9 "11" -> 11
#
# time = O(log(n))
# space = O(log(n))
class Solution(object):
    def newInteger(self, n):
        digits = []
        while n > 0:
            digits.append(str(n % 9))
            n //= 9
        # digits were collected least-significant first
        return int(''.join(reversed(digits)))
