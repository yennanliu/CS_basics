"""

3183. The Number of Ways to Make the Sum
Medium
🔒 (premium)

You have an infinite number of coins with values 1, 2, and 6, and there are also two coins with value 4.

Given an integer n, return the number of ways to make the sum of n.

Since the answer may be too large, return it modulo 10^9 + 7.

Note that the order of the coins doesn't matter and 2 + 4 + 6 is the same as 4 + 6 + 2.


Example 1:

Input: n = 4
Output: 4
Explanation:
Here are the four combinations: (1, 1, 1, 1), (1, 1, 2), (2, 2), (4).

Example 2:

Input: n = 12
Output: 22
Explanation:
Here are the 22 combinations:
(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1), (1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2), (1, 1, 1, 1, 1, 1, 1, 1, 2, 2), (1, 1, 1, 1, 1, 1, 2, 2, 2), (1, 1, 1, 1, 2, 2, 2, 2), (1, 1, 2, 2, 2, 2, 2), (2, 2, 2, 2, 2, 2), (1, 1, 1, 1, 1, 1, 6), (1, 1, 1, 1, 2, 6), (1, 1, 2, 2, 6), (2, 2, 2, 6), (6, 6), (1, 1, 1, 1, 4, 4), (1, 1, 2, 4, 4), (2, 2, 4, 4), (4, 4, 4)... and so on.


Constraints:

1 <= n <= 10^5

"""

# V0
# IDEA : UNBOUNDED COIN COUNT FOR {1, 2, 6}, THEN ADD 0, 1 OR 2 FOURS BY HAND
#
#   the 4-coins are LIMITED to two, so they cannot go through the usual
#   unbounded loop. instead solve the unbounded part once
#
#       f[t] = ways to make t out of 1s, 2s and 6s
#
#   with the classic "one coin at a time, sweep upwards" recurrence, and then
#   split on how many fours are used :
#
#       answer = f[n] + f[n - 4] + f[n - 8]
#
#   which is exact because order does not matter — the fours are
#   interchangeable, so only their COUNT distinguishes the cases.
#
# time = O(n), space = O(n)
class Solution(object):
    def numberOfWays(self, n):
        MOD = 10 ** 9 + 7
        f = [0] * (n + 1)
        f[0] = 1
        for coin in (1, 2, 6):
            for t in range(coin, n + 1):
                f[t] = (f[t] + f[t - coin]) % MOD

        res = f[n]
        if n >= 4:
            res += f[n - 4]
        if n >= 8:
            res += f[n - 8]
        return res % MOD
