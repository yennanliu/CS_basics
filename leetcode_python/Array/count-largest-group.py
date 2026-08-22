"""

1399. Count Largest Group
Easy

You are given an integer n.

We need to group the numbers from 1 to n according to the sum of its digits. For example, the numbers 14 and 5 belong to the same group, whereas 13 and 3 belong to different groups.

Return the number of groups that have the largest size, i.e. the maximum number of elements.


Example 1:

Input: n = 13
Output: 4
Explanation: There are 9 groups in total, they are grouped according sum of its digits of numbers from 1 to 13:
[1,10], [2,11], [3,12], [4,13], [5], [6], [7], [8], [9].
There are 4 groups with largest size.

Example 2:

Input: n = 2
Output: 2
Explanation: There are 2 groups [1], [2] of size 1.


Constraints:

1 <= n <= 10^4

"""

# V0
# IDEA : COUNTING BY DIGIT SUM (bucket key = sum of digits)
#
#   n <= 10^4, so a digit sum is at most 9*4 = 36 -> a small counter suffices.
#   count how many numbers land in each bucket, take the max bucket size,
#   then count how many buckets reach it.
#   NOTE : the answer is the NUMBER of largest groups, not their size.
#
# time = O(n * log n), space = O(log n)
from collections import Counter
class Solution(object):
    def countLargestGroup(self, n):
        cnt = Counter()
        for i in range(1, n + 1):
            s, x = 0, i
            while x:
                s += x % 10
                x //= 10
            cnt[s] += 1

        mx = max(cnt.values())
        return sum(1 for v in cnt.values() if v == mx)


# V0-1
# IDEA : DP ON DIGIT SUMS -- ds[i] = ds[i // 10] + i % 10
#
#   the digit sum of i is the digit sum of "i without its last digit" plus that
#   last digit, and i // 10 < i is already solved. so one array fills in O(1)
#   per number and no inner while-loop is needed -- the log n factor of V0
#   disappears, paid for with an O(n) table.
#   the bucket tally is then kept in a plain list of size 9 * 5 + 1 (n <= 10^4
#   -> at most 5 digits) instead of a dict.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def countLargestGroup(self, n):
        ds = [0] * (n + 1)
        buckets = [0] * (9 * 5 + 1)
        for i in range(1, n + 1):
            ds[i] = ds[i // 10] + i % 10
            buckets[ds[i]] += 1

        mx = max(buckets)
        return buckets.count(mx)


# V0-2
# IDEA : DIGIT DP -- COUNT x IN [0, n] WITH A GIVEN DIGIT SUM, WITHOUT
#        ENUMERATING ANYTHING
#
#   walk the decimal digits of n left to right. state = (position, remaining
#   sum we still owe, still hugging the prefix of n?). while `tight` the digit
#   is capped by n's digit, once we go strictly below we are free (0..9).
#   leading zeros need no flag here: every target sum s >= 1 forces at least one
#   non-zero digit, so the number 0 is never counted and [0, n] == [1, n].
#   the cache is shared across all s, so the whole sweep is one small DP table
#   whose size depends on log n, NOT on n.
#
# time = O(log n * (9 log n) * 10)
# space = O(log n * (9 log n))
from functools import lru_cache
class Solution(object):
    def countLargestGroup(self, n):
        digits = [int(c) for c in str(n)]
        L = len(digits)

        @lru_cache(None)
        def dp(i, rest, tight):
            if rest < 0:
                return 0
            if i == L:
                return 1 if rest == 0 else 0
            hi = digits[i] if tight else 9
            return sum(dp(i + 1, rest - d, tight and d == hi)
                       for d in range(hi + 1))

        mx, res = 0, 0
        for s in range(1, 9 * L + 1):
            c = dp(0, s, True)
            if c > mx:
                mx, res = c, 1
            elif c == mx and c > 0:
                res += 1
        return res
