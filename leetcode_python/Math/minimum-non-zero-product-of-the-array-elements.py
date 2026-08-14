"""

1969. Minimum Non-Zero Product of the Array Elements
Medium

You are given a positive integer p. Consider an array nums (1-indexed) that consists of the integers in the inclusive range [1, 2^p - 1] in their binary representations. You are allowed to do the following operation any number of times:

Choose two elements x and y from nums.
Choose a bit in x and swap it with its corresponding bit in y. Corresponding bit refers to the bit that is in the same position in the other integer.

For example, if x = 1101 and y = 0011, after swapping the 2nd bit from the right, we have x = 1111 and y = 0001.

Find the minimum non-zero product of nums after performing the above operation any number of times. Return this product modulo 10^9 + 7.

Note: The answer should be the minimum product before the modulo operation is done.


Example 1:

Input: p = 1
Output: 1
Explanation: nums = [1].
There is only one element, so the product equals that element.

Example 2:

Input: p = 2
Output: 6
Explanation: nums = [01, 10, 11].
Any swap would either make the product 0 or stay the same.
Thus, the array product of 1 * 2 * 3 = 6 is already minimized.

Example 3:

Input: p = 3
Output: 1512
Explanation: nums = [001, 010, 011, 100, 101, 110, 111]
- In the first operation we can swap the leftmost bit of the second and fifth elements.
    - The resulting array is [001, 110, 011, 100, 001, 110, 111].
- In the second operation we can swap the middle bit of the third and fourth elements.
    - The resulting array is [001, 110, 001, 110, 001, 110, 111].
The array product is 1 * 6 * 1 * 6 * 1 * 6 * 7 = 1512, which is the minimum possible product.


Constraints:

1 <= p <= 60

"""

# V0
# IDEA : GREEDY / MATH (pair x with 2^p-1-x, then FAST POWER)
#
#   a swap never changes the SUM of the array. with a fixed sum, a product is
#   minimised by pushing the values as far apart as possible.
#
#   the max element 2^p - 1 is all-ones, so it can never gain a bit -> leave it.
#   the remaining 2^p - 2 numbers pair up as (x, 2^p-1-x) : the two are bitwise
#   complements, so we can drain every bit of one into the other, turning the
#   pair into (1, 2^p - 2).
#
#   there are (2^p - 2) / 2 = 2^(p-1) - 1 such pairs, so
#     answer = (2^p - 1) * (2^p - 2)^(2^(p-1) - 1)  mod 1e9+7
#
#   NOTE : the exponent is huge (up to 2^59) -> modular fast power, and the
#          modulo is applied only at the very end conceptually.
#          p = 1 works out to 1 * 0^0 = 1.
#
# time = O(p) (fast power over a p-bit exponent), space = O(1)
class Solution(object):
    def minNonZeroProduct(self, p):
        MOD = 10 ** 9 + 7
        mx = (1 << p) - 1            # 2^p - 1
        pair = (mx - 1) % MOD        # 2^p - 2
        cnt = (1 << (p - 1)) - 1     # number of (1, 2^p-2) pairs
        return (mx % MOD) * pow(pair, cnt, MOD) % MOD
