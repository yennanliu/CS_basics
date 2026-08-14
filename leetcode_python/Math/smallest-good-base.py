"""

483. Smallest Good Base
Hard

Given an integer n represented as a string, return the smallest good base of n.

We call k >= 2 a good base of n, if all digits of n base k are 1's.

Example 1:

Input: n = "13"
Output: "3"
Explanation: 13 base 3 is 111.

Example 2:

Input: n = "4681"
Output: "8"
Explanation: 4681 base 8 is 11111.

Example 3:

Input: n = "1000000000000000000"
Output: "999999999999999999"
Explanation: 1000000000000000000 base 999999999999999999 is 11.

Constraints:

n is an integer in the range [3, 10^18].
n does not contain any leading zeros.

"""

# V0
# IDEA : ENUMERATE THE NUMBER OF 1's + BINARY SEARCH THE BASE
#
#  "all digits are 1" means:
#
#      num = 1 + k + k^2 + ... + k^m       (m + 1 ones in base k)
#
#  For a FIXED m the left side is strictly increasing in k, so k can be binary
#  searched. And the MORE ones we use, the SMALLER the base has to be -> try the
#  largest m first and return the first hit.
#
#  Bound on m: the smallest possible base is 2, where num = 2^(m+1) - 1, so
#  m + 1 <= num.bit_length(), i.e. m <= num.bit_length() - 1.
#
#  Fallback: every n >= 3 is "11" in base n - 1 (m = 1), so that always works.
#
# time = O(log(num)^2 * log(num))  # ~63 values of m, each a 60-step binary
#        search whose check costs O(m)
# space = O(1)
class Solution(object):
    def smallestGoodBase(self, n):
        num = int(n)

        def total(k, m):
            # 1 + k + k^2 + ... + k^m , bailing out as soon as we pass num
            s, p = 1, 1
            for _ in range(m):
                p *= k
                s += p
                if s > num:
                    return s
            return s

        for m in range(num.bit_length() - 1, 1, -1):
            lo, hi = 2, num - 1
            while lo < hi:
                mid = (lo + hi) // 2
                if total(mid, m) >= num:
                    hi = mid
                else:
                    lo = mid + 1
            if total(lo, m) == num:
                return str(lo)

        # m == 1 : num = 1 + (num - 1)  ->  "11" in base num - 1
        return str(num - 1)
