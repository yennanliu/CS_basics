"""

3370. Smallest Number With All Set Bits
Easy

You are given a positive number n.

Return the smallest number x greater than or equal to n, such that the binary representation of x contains only set bits.


Example 1:

Input: n = 5
Output: 7
Explanation:
The binary representation of 7 is "111".

Example 2:

Input: n = 10
Output: 15
Explanation:
The binary representation of 15 is "1111".

Example 3:

Input: n = 3
Output: 3
Explanation:
The binary representation of 3 is "11".


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : THE ALL-ONES NUMBERS ARE 2^b - 1 — TAKE THE FIRST ONE THAT REACHES n
#
#   n.bit_length() gives the width of n, and 2^width - 1 is the smallest
#   all-ones number that is at least as wide, hence at least n.
#
# time = O(1), space = O(1)
class Solution(object):
    def smallestNumber(self, n):
        return (1 << n.bit_length()) - 1
