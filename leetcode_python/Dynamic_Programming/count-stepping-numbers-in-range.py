"""

2801. Count Stepping Numbers in Range
Hard

Given two positive integers low and high represented as strings, find the count of stepping numbers in the inclusive range [low, high].

A stepping number is an integer such that all of its adjacent digits have an absolute difference of exactly 1.

Return an integer denoting the count of stepping numbers in the inclusive range [low, high].

Since the answer may be very large, return it modulo 10^9 + 7.

Note: A stepping number should not have a leading zero.


Example 1:

Input: low = "1", high = "11"
Output: 10
Explanation: The stepping numbers in the range [1,11] are 1, 2, 3, 4, 5, 6, 7, 8, 9 and 10. There are a total of 10 stepping numbers in the range. Hence, the output is 10.

Example 2:

Input: low = "90", high = "101"
Output: 2
Explanation: The stepping numbers in the range [90,101] are 98 and 101. There are a total of 2 stepping numbers in the range. Hence, the output is 2.


Constraints:

1 <= int(low) <= int(high) < 10^100
1 <= low.length, high.length <= 100
low and high consist of only digits.
low and high don't have any leading zeros.

"""

# V0
# IDEA : DIGIT DP (COUNT [1, high] - COUNT [1, low - 1])
#
#   the numbers are up to 100 digits long, so we can never enumerate them.
#   but "stepping" only constrains ADJACENT digits, so a digit-by-digit dp
#   carrying just the previous digit is enough.
#
#   classic range trick : f(x) = count of stepping numbers in [1, x], and
#   the answer is f(high) - f(low - 1).
#
#   state dfs(pos, pre, lead, limit) :
#     pos   - index of the digit we are choosing
#     pre   - digit chosen at pos-1 (-1 while still in leading zeros)
#     lead  - we have not placed a non-zero digit yet
#     limit - every digit so far equals high's prefix, so this digit is
#             capped by num[pos]
#
#   NOTE : `lead` is what forbids leading zeros. while lead is on, a chosen
#          0 keeps us in the "nothing written yet" state instead of becoming
#          a real digit with a stepping constraint. reaching the end still
#          in lead means we built the empty number -> contributes 0, which
#          is exactly why 0 itself is never counted.
#
#   NOTE : memoization is only legal for states with lead=False and
#          limit=False. a limited state depends on the actual bound prefix,
#          and a leading state has no meaningful `pre`. the cache must also
#          be reset between the two bounds since `num` changes.
#
#   NOTE : low - 1 is computed with python's arbitrary-precision int, so no
#          manual borrow-subtraction on the digit string is needed. when
#          low == "1" this yields "0", and dfs correctly returns 0 for it.
#
#   NOTE : take the final difference modulo 10^9+7 - the two counts are
#          already reduced, so a - b can go negative before the mod.
#
"""

DP def
    (DIGIT DP - the numbers are up to 100 digits, so never enumerate them;
     "stepping" only constrains ADJACENT digits, so carrying the previous
     digit is enough)

    f(x) = count of stepping numbers in [1, x],  ans = f(high) - f(low - 1)

    dfs(pos, pre, lead, limit):
        pos   - index of the digit being chosen
        pre   - digit chosen at pos-1 (-1 while still in leading zeros)
        lead  - no non-zero digit placed yet
        limit - every digit so far equals the bound's prefix

DP eq

     dfs(pos, pre, lead, limit) = sum over d in 0..up of

        dfs(pos+1, d, lead and d == 0, limit and d == up)

        allowed only when  lead  or  abs(d - pre) == 1

        up = int(num[pos]) if limit else 9


    -> e.g. base: pos == n -> 1 if not lead else 0
              (still `lead` = the empty number -> that is why 0 never counts)

     NOTE !!! memoize ONLY states with lead == False and limit == False,
              and clear the cache between the two bounds

"""
# time = O(L * 10 * 10), space = O(L * 10)   L = len(high) <= 100
class Solution(object):
    def countSteppingNumbers(self, low, high):
        MOD = 10 ** 9 + 7

        def count(num):
            memo = {}

            def dfs(pos, pre, lead, limit):
                if pos == len(num):
                    # still `lead` -> we placed nothing at all, not a number
                    return 0 if lead else 1
                if not lead and not limit and (pos, pre) in memo:
                    return memo[(pos, pre)]

                up = int(num[pos]) if limit else 9
                res = 0
                for d in range(up + 1):
                    nxt_limit = limit and d == up
                    if lead:
                        if d == 0:
                            res += dfs(pos + 1, -1, True, nxt_limit)
                        else:
                            # first real digit : no stepping constraint yet
                            res += dfs(pos + 1, d, False, nxt_limit)
                    elif abs(d - pre) == 1:
                        res += dfs(pos + 1, d, False, nxt_limit)
                res %= MOD

                if not lead and not limit:
                    memo[(pos, pre)] = res
                return res

            return dfs(0, -1, True, True)

        a = count(high)
        b = count(str(int(low) - 1))
        return (a - b) % MOD
