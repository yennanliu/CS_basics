"""

2117. Abbreviating the Product of a Range
Hard

You are given two positive integers left and right with left <= right. Calculate the product of all integers in the inclusive range [left, right].

Since the product may be very large, you will abbreviate it following these steps:

Count all trailing zeros in the product and remove them. Let us denote this count as C.
For example, there are 3 trailing zeros in 1000, and there are 0 trailing zeros in 546.
Denote the remaining number of digits in the product as d. If d > 10, then express the product as <pre>...<suf> where <pre> denotes the first 5 digits of the product, and <suf> denotes the last 5 digits of the product after removing all trailing zeros. If d <= 10, we keep it unchanged.
For example, we express 1234567654321 as 12345...54321, but 1234567 is represented as 1234567.
Finally, represent the product as a string "<pre>...<suf>eC".
For example, 12345678987600000 will be represented as "12345...89876e5".

Return a string denoting the abbreviated product of all integers in the inclusive range [left, right].


Example 1:

Input: left = 1, right = 4
Output: "24e0"
Explanation: The product is 1 × 2 × 3 × 4 = 24.
There are no trailing zeros, so 24 remains the same. The abbreviation will end with "e0".
Since the number of digits is 2, which is less than 10, we do not have to abbreviate it further.
Thus, the final representation is "24e0".

Example 2:

Input: left = 2, right = 11
Output: "399168e2"
Explanation: The product is 39916800.
There are 2 trailing zeros, which we remove to get 399168. The abbreviation will end with "e2".
The number of digits after removing the trailing zeros is 6, so we do not abbreviate it further.
Hence, the abbreviated product is "399168e2".

Example 3:

Input: left = 371, right = 375
Output: "7219856259e3"
Explanation: The product is 7219856259000.
The abbreviated product is "7219856259e3".


Constraints:

1 <= left <= right <= 10^4

"""

# V0
# IDEA : TRACK THE HEAD AND THE TAIL SEPARATELY — NEVER MATERIALISE THE PRODUCT
#
#   the product of 1..10^4 runs to ~36k digits, so building it and calling
#   str() is both slow and blocked outright by CPython's 4300-digit int ->
#   str limit. all three pieces of the answer can be had without it :
#
#   C (trailing zeros) : a trailing zero is a factor 10 = 2 * 5, so
#       C = min(total exponent of 2, total exponent of 5) over the range.
#
#   TAIL : strip every 2 and 5 out of each factor while multiplying modulo
#       10^15, then multiply back the LEFTOVER powers, 2^(c2-C) * 5^(c5-C).
#       that is exactly prod / 10^C modulo 10^15 — one of the two leftover
#       exponents is 0, so no trailing zero can survive.
#
#   HEAD : keep a running `head` truncated to 20 digits, counting the digits
#       dropped in `exp`. the product is then head * 10^exp up to a relative
#       error below 10^-15, which is plenty for 5 leading digits, and the
#       total digit count len(str(head)) + exp is exact.
#
#   d = total digits - C. when d <= 10 the whole number fits in the 10^15
#   window, so the tail alone IS the answer; otherwise glue head[:5], "...",
#   and the tail's last 5 digits.
#
# time = O(right - left), space = O(1)
class Solution(object):
    def abbreviateProduct(self, left, right):
        MOD = 10 ** 15

        c2 = c5 = 0
        core = 1
        head = 1
        exp = 0
        for x in range(left, right + 1):
            y = x
            while y % 2 == 0:
                y //= 2
                c2 += 1
            while y % 5 == 0:
                y //= 5
                c5 += 1
            core = core * y % MOD

            head *= x
            while head >= 10 ** 20:
                head //= 10
                exp += 1

        C = min(c2, c5)
        core = core * pow(2, c2 - C, MOD) % MOD
        core = core * pow(5, c5 - C, MOD) % MOD

        head_str = str(head)
        d = len(head_str) + exp - C          # digits left after stripping zeros

        if d <= 10:
            return str(core) + "e" + str(C)
        return (head_str[:5] + "..." + str(core % 10 ** 5).zfill(5)
                + "e" + str(C))
