"""

3519. Count Numbers with Non-Decreasing Digits
Hard

You are given two integers, l and r, represented as strings, and an integer b.
Return the count of integers in the inclusive range [l, r] whose digits are in
non-decreasing order when represented in base b.

An integer is considered to have non-decreasing digits if, when read from left
to right (from the most significant digit to the least significant digit), each
digit is greater than or equal to the previous one.

Since the answer may be too large, return it modulo 10^9 + 7.

Example 1:

Input: l = "23", r = "28", b = 8

Output: 3

Explanation:

The numbers from 23 to 28 in base 8 are: 27, 30, 31, 32, 33, and 34.

Out of these, 27, 33, and 34 have non-decreasing digits. Hence, the output is 3.

Example 2:

Input: l = "2", r = "7", b = 2

Output: 2

Explanation:

The numbers from 2 to 7 in base 2 are: 10, 11, 100, 101, 110, and 111.

Out of these, 11 and 111 have non-decreasing digits. Hence, the output is 2.

Constraints:

1 <= l.length <= r.length <= 100

2 <= b <= 10

l and r consist only of digits.

The value represented by l is less than or equal to the value represented by r.

l and r do not contain leading zeros.

"""

# V0
# IDEA : DIGIT DP IN BASE b, COUNTED WITH STARS-AND-BARS INSTEAD OF A STATE
#
#   answer = f(r) - f(l - 1) where f(x) counts the values in [1, x] whose
#   base-b digits never decrease.  l and r have up to 100 decimal digits, but
#   Python integers swallow that, and the base-b form is only a few hundred
#   digits long.
#
#   the useful observation is that once a prefix is fixed and we drop below x,
#   the tail is unconstrained apart from "non-decreasing and >= the last digit
#   placed".  the number of non-decreasing length-t strings over an alphabet of
#   size m is the multiset count C(m + t - 1, t) -- a closed form, so no DP
#   table over the remaining positions is needed at all.
#
#   f(x) is then two pieces:
#     * strictly shorter numbers.  a non-decreasing number with no leading zero
#       has *every* digit >= 1, so the length-t count is C(b - 2 + t, t).
#     * numbers of the same length as x: walk the digits, and at position i try
#       every digit d below x[i] that still respects the previous digit; the
#       remaining L - 1 - i places may use the b - d digits >= d.  the walk
#       stops as soon as x itself stops being non-decreasing, and if it never
#       stops, x counts too.
#
# time = O(L^2 + L * b) with L the base-b length, space = O(L)
class Solution(object):
    def countNumbers(self, l, r, b):
        MOD = 10 ** 9 + 7
        lo, hi = int(l), int(r)

        def digits(x):
            if x == 0:
                return [0]
            d = []
            while x:
                x, m = divmod(x, b)
                d.append(m)
            d.reverse()
            return d

        limit = len(digits(hi)) + b + 10
        fact = [1] * (limit + 1)
        for i in range(1, limit + 1):
            fact[i] = fact[i - 1] * i % MOD
        inv = [1] * (limit + 1)
        inv[limit] = pow(fact[limit], MOD - 2, MOD)
        for i in range(limit, 0, -1):
            inv[i - 1] = inv[i] * i % MOD

        def comb(nn, kk):
            if kk < 0 or kk > nn:
                return 0
            return fact[nn] * inv[kk] % MOD * inv[nn - kk] % MOD

        def multiset(m, t):          # non-decreasing length-t words over m symbols
            return comb(m + t - 1, t)

        def f(x):                    # how many of 1..x are non-decreasing
            if x <= 0:
                return 0
            d = digits(x)
            L = len(d)
            res = 0
            for t in range(1, L):    # shorter numbers, all digits >= 1
                res += multiset(b - 1, t)
            prev = 1                 # the leading digit may not be 0
            for i in range(L):
                for v in range(prev, d[i]):
                    res += multiset(b - v, L - 1 - i)
                if d[i] < prev:
                    break
                prev = d[i]
            else:
                res += 1             # x itself survived the whole walk
            return res % MOD

        ans = (f(hi) - f(lo - 1)) % MOD
        if lo == 0:                  # 0 is a single digit, trivially sorted
            ans = (ans + 1) % MOD
        return ans
