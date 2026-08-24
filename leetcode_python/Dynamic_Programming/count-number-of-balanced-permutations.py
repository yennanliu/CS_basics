"""

3343. Count Number of Balanced Permutations
Hard

You are given a string num. A string of digits is called balanced if the sum of the digits at even indices is equal to the sum of the digits at odd indices.

Return the number of distinct permutations of num that are balanced.

Since the answer may be very large, return it modulo 10^9 + 7.

A permutation is a rearrangement of all the characters of a string.


Example 1:

Input: num = "123"
Output: 2
Explanation:
The distinct permutations of num are "123", "132", "213", "231", "312" and "321".
Among them, "132" and "231" are balanced. Thus, the answer is 2.

Example 2:

Input: num = "112"
Output: 1
Explanation:
The distinct permutations of num are "112", "121", and "211".
Only "121" is balanced. Thus, the answer is 1.

Example 3:

Input: num = "12345"
Output: 0
Explanation:
None of the permutations of num are balanced, so the answer is 0.


Constraints:

2 <= num.length <= 80
num consists of digits

"""

# V0
# IDEA : SPLIT THE MULTISET BETWEEN THE TWO POSITION CLASSES, THEN COUNT ARRANGEMENTS
#
#   a permutation is fixed by deciding which digits land on even positions
#   and which on odd ones. with j_d copies of digit d sent to the even side,
#   the number of DISTINCT permutations produced is
#
#       even! / prod(j_d!)  *  odd! / prod((cnt_d - j_d)!)
#
#   and factoring out the constants turns the sum over all splits into
#
#       even! * odd! / prod(cnt_d!)  *  sum over splits of prod C(cnt_d, j_d)
#
#   so a DP over the ten digits accumulates that inner sum, tracking how many
#   even slots are filled and how much digit-sum has gone to the even side.
#   the target is half the total, so an odd total is immediately 0.
#
"""

DP def
    a permutation is fixed by deciding WHICH digits go to even positions.
    with j_d copies of digit d on the even side, the number of DISTINCT
    permutations is

        even! / prod(j_d!)  *  odd! / prod((cnt_d - j_d)!)

    factoring the constants out leaves

        even! * odd! / prod(cnt_d!)  *  SUM over splits of prod C(cnt_d, j_d)

    dp[used][s]: that inner SUM so far - `used` even slots filled,

                 `s` digit-sum sent to the even side

DP eq

     for each digit d = 0..9, for each j in 0..cnt[d]:

        dp_new[used + j][s + d*j] += dp[used][s] * C(cnt[d], j)


    -> e.g. total digit sum must be EVEN, target = total / 2
              (an odd total -> answer 0)

     ans = even! * odd! / prod(cnt_d!) * dp[even_slots][target]   mod 10^9+7

"""
# time = O(10 * slots * sum * max count), space = O(slots * sum)
from math import factorial


def comb(n, r):
    if r < 0 or r > n:
        return 0
    return factorial(n) // (factorial(r) * factorial(n - r))


class Solution(object):
    def countBalancedPermutations(self, num):
        MOD = 10 ** 9 + 7
        n = len(num)
        cnt = [0] * 10
        total = 0
        for ch in num:
            d = int(ch)
            cnt[d] += 1
            total += d

        if total % 2:
            return 0
        target = total // 2
        even_slots = (n + 1) // 2
        odd_slots = n // 2

        # dp[used][s] = sum of prod C(cnt_d, j_d) so far
        dp = [[0] * (target + 1) for _ in range(even_slots + 1)]
        dp[0][0] = 1
        for d in range(10):
            c = cnt[d]
            if c == 0:
                continue
            nxt = [[0] * (target + 1) for _ in range(even_slots + 1)]
            for used in range(even_slots + 1):
                row = dp[used]
                for s in range(target + 1):
                    v = row[s]
                    if not v:
                        continue
                    for j in range(c + 1):
                        nu = used + j
                        ns = s + j * d
                        if nu > even_slots or ns > target:
                            break
                        nxt[nu][ns] = (nxt[nu][ns] + v * comb(c, j)) % MOD
            dp = nxt

        ways = dp[even_slots][target]
        res = ways * factorial(even_slots) % MOD * factorial(odd_slots) % MOD
        denom = 1
        for c in cnt:
            denom = denom * factorial(c) % MOD
        return res * pow(denom, MOD - 2, MOD) % MOD
