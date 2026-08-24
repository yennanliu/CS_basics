"""

2827. Number of Beautiful Integers in the Range
Hard

You are given positive integers low, high, and k.

A number is beautiful if it meets both of the following conditions:

The count of even digits in the number is equal to the count of odd digits.
The number is divisible by k.

Return the number of beautiful integers in the range [low, high].


Example 1:

Input: low = 10, high = 20, k = 3
Output: 2
Explanation: There are 2 beautiful integers in the given range: [12,18].
- 12 is beautiful because it contains 1 odd digit and 1 even digit, and is divisible by k = 3.
- 18 is beautiful because it contains 1 odd digit and 1 even digit, and is divisible by k = 3.
Additionally we can see that:
- 16 is not beautiful because it is not divisible by k = 3.
- 15 is not beautiful because it does not contain equal counts even and odd digits.
It can be shown that there are only 2 beautiful integers in the given range.

Example 2:

Input: low = 1, high = 10, k = 1
Output: 1
Explanation: There is 1 beautiful integer in the given range: [10].
- 10 is beautiful because it contains 1 odd digit and 1 even digit, and is divisible by k = 1.
It can be shown that there is only 1 beautiful integer in the given range.

Example 3:

Input: low = 5, high = 5, k = 2
Output: 0
Explanation: There are 0 beautiful integers in the given range.
- 5 is not beautiful because it is not divisible by k = 2 and it does not contain equal even and odd digits.


Constraints:

0 < low <= high <= 10^9
0 < k <= 20

"""

# V0
# IDEA : DIGIT DP + PREFIX SUBTRACTION
#
#   count(x) = number of beautiful integers in [1, x], so the answer is
#   count(high) - count(low - 1).
#
#   Build x digit by digit, carrying 4 pieces of state:
#     pos   : index into str(x) we are about to fill
#     mod   : value built so far, modulo k  (k <= 20, so <= 20 states)
#     diff  : (#odd digits) - (#even digits) placed so far; "beautiful"
#             means diff == 0 at the end. Range is [-10, 10] for 10 digits.
#     lead  : still in the leading-zero run, i.e. no significant digit yet
#     limit : the prefix so far exactly matches str(x)'s prefix, so the next
#             digit is capped at str(x)[pos]
#
#   NOTE : leading zeros must NOT be counted as even digits — "12" has one
#          even and one odd digit, not nine extra evens. While lead is true
#          we place a 0 without touching mod/diff.
#
#   NOTE : a number that is ALL leading zeros is the integer 0, which is not
#          in [1, x]. Hence the explicit `not lead` guard at pos == n, and
#          count(x) returning 0 for x <= 0 (low - 1 can be 0).
#
#   NOTE : memoizing on the full 5-tuple is safe (not just on the
#          lead=limit=False states): for a fixed pos there is exactly one
#          prefix with limit=True and exactly one with lead=True, so those
#          keys are never shared between different prefixes.
#
#   NOTE : the memo must be rebuilt per call to count() — it is keyed by
#          position within a specific str(x).
#
"""

DP def
    (DIGIT DP + prefix subtraction)

    ans = count(high) - count(low - 1),  count(x) = beautiful integers in [1, x]

    dp(pos, mod, diff, lead, limit):

        pos   - index of the digit being filled
        mod   - the value built so far, modulo k   (k <= 20)
        diff  - (#odd digits) - (#even digits) so far; beautiful means
                diff == 0 at the end
        lead  - still in the leading-zero run (no significant digit yet)
        limit - the prefix matches str(x)'s prefix, so the digit is capped

DP eq

     dp(pos, ...) = sum over d in 0..up of

        dp( pos+1,
            (mod * 10 + d) % k,
            diff + (1 if d odd else -1),
            lead and d == 0,
            limit and d == up )


    -> e.g. NOTE !!! leading zeros must NOT count as EVEN digits - "12" has
              one even and one odd digit, not nine extra evens. while `lead`
              is on, place a 0 WITHOUT touching mod / diff

     NOTE !!! all-leading-zeros is the integer 0, not in [1, x] - hence the
              `not lead` guard at pos == n, and count(x) = 0 for x <= 0

     base: pos == n -> 1 if (not lead and mod == 0 and diff == 0) else 0

"""
# time = O(log(x) * k * d * 10) per bound, space = O(log(x) * k * d)
class Solution(object):
    def numberOfBeautifulIntegers(self, low, high, k):
        return self._count(high, k) - self._count(low - 1, k)

    def _count(self, x, k):
        if x <= 0:
            return 0
        s = str(x)
        n = len(s)
        memo = {}

        def dfs(pos, mod, diff, lead, limit):
            if pos == n:
                if lead:
                    return 0
                return 1 if (mod == 0 and diff == 0) else 0
            key = (pos, mod, diff, lead, limit)
            if key in memo:
                return memo[key]
            up = int(s[pos]) if limit else 9
            res = 0
            for d in range(up + 1):
                nxt_limit = limit and (d == up)
                if d == 0 and lead:
                    res += dfs(pos + 1, 0, 0, True, nxt_limit)
                else:
                    nxt_diff = diff + (1 if d % 2 else -1)
                    res += dfs(pos + 1, (mod * 10 + d) % k, nxt_diff, False, nxt_limit)
            memo[key] = res
            return res

        return dfs(0, 0, 0, True, True)
