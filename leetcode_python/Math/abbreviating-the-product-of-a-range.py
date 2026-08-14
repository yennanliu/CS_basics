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
Explanation: The product is 1 x 2 x 3 x 4 = 24.
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


Constraints:

1 <= left <= right <= 10^4

"""

# V0
# IDEA : HANDLE THE THREE PIECES SEPARATELY — ZEROS, SUFFIX, PREFIX
#
#   1) TRAILING ZEROS. a trailing zero is a factor of 10, so
#          C = min( total factors of 2, total factors of 5 )
#      over the whole range.
#
#   2) SUFFIX. multiply the range modulo 10^5, but first divide out exactly C
#      twos and C fives. after that the running product has no factor 10 left,
#      so a plain mod 10^5 gives the true last five digits.
#
#   3) PREFIX + DIGIT COUNT. keep a TRUNCATED running product : multiply, and
#      whenever it exceeds 10^15 divide by 10 and remember how many digits
#      were dropped. that is exact integer arithmetic (no floats), and
#          total digits = len(str(pre)) + dropped
#      is exact too, because truncation only ever chops the low end.
#      the relative error stays under ~10^-11, far below what 5 leading
#      digits need.
#
#   if d = total digits - C is at most 10 the answer is the exact number, so
#   recompute the product directly — in that branch it is provably small
#   (a huge C would need thousands of multiples of 5 in the range).
#
# time = O(right - left) big-int multiplications, space = O(1)
class Solution(object):
    def abbreviateProduct(self, left, right):
        # 1) count factors of 2 and 5
        c2 = c5 = 0
        for i in range(left, right + 1):
            x = i
            while x % 2 == 0:
                x //= 2
                c2 += 1
            x = i
            while x % 5 == 0:
                x //= 5
                c5 += 1
        C = min(c2, c5)

        # 3) truncated running product -> leading digits and exact digit count
        pre = 1
        dropped = 0
        LIMIT = 10 ** 15
        for i in range(left, right + 1):
            pre *= i
            while pre >= LIMIT:
                pre //= 10
                dropped += 1
        total_digits = len(str(pre)) + dropped
        d = total_digits - C

        if d <= 10:
            p = 1
            for i in range(left, right + 1):
                p *= i
            p //= 10 ** C
            return str(p) + 'e' + str(C)

        # 2) last five digits, with the C factors of 10 taken out
        suf = 1
        r2, r5 = C, C
        for i in range(left, right + 1):
            x = i
            while r2 and x % 2 == 0:
                x //= 2
                r2 -= 1
            while r5 and x % 5 == 0:
                x //= 5
                r5 -= 1
            suf = suf * x % 100000

        head = str(pre)[:5]
        return head + '...' + str(suf).zfill(5) + 'e' + str(C)
