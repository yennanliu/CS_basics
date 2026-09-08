"""

4039. Sum of Decoded Numbers
Medium

You are given an integer array nums, where each nums[i] is an encoded integer
that decodes into a pair (xi, yi).

To decode nums[i]:

- widthi = nums[i] % 10

- di = floor(nums[i] / 10)

- xi is the integer formed by the first widthi digits of the decimal
  representation of di.

- yi is the integer formed by all the remaining digits of the decimal
  representation of di.

The decoded value of nums[i] is (xi) ^ (yi), that is xi raised to the power yi.

Return the sum of the decoded values of all elements in nums, modulo 10^9 + 7.

Example 1:

Input: nums = [1232]

Output: 1728

Explanation:

width = 1232 % 10 = 2, d = 1232 / 10 = 123.
The first 2 digits of "123" give x = 12, the rest gives y = 3.
The decoded value is 12^3 = 1728.

Example 2:

Input: nums = [231,1232]

Output: 1736

Explanation:

231  -> width = 1, d = 23  -> x = 2,  y = 3 -> 2^3  = 8
1232 -> width = 2, d = 123 -> x = 12, y = 3 -> 12^3 = 1728
The sum is 8 + 1728 = 1736.

Constraints:

1 <= nums.length <= 10^5

The input is generated such that every nums[i] decodes into a valid pair, i.e.
0 < widthi < the number of digits of di.

"""

# V0
# IDEA : DIGIT SPLIT + MODULAR EXPONENTIATION
#
#   the encoding is only a packing trick : the last decimal digit is the width,
#   everything above it is d. so ONE `divmod` by 10 undoes it, and the split of
#   d into (x, y) is a string slice at `width` -- no arithmetic needed.
#
#   e.g. 1232 -> width = 2, d = 123 -> "123"[:2] = "12", "123"[2:] = "3"
#
#   the size is in y, not in n : y can be ~10^9, so x^y must never be built.
#   pow(x, y, MOD) squares-and-multiplies under the mod instead, which is
#   O(log y) multiplications of numbers < MOD.
#
# NOTE !!! `^` in the statement is exponentiation, NOT python's xor operator
#          (`x ^ y` in python would silently return the wrong number, and the
#          "modulo 10^9 + 7" in the statement is the tell -- xor never overflows)
#
# time = O(n * logy), space = O(1)
class Solution(object):
    def sumDecoded(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        MOD = 10 ** 9 + 7
        ans = 0

        for num in nums:

            # last digit = width, the rest = d
            d, width = divmod(num, 10)

            s = str(d)

            # first `width` digits
            x = int(s[:width])

            # the remaining digits
            y = int(s[width:])

            ans = (ans + pow(x, y, MOD)) % MOD

        return ans
