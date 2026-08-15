"""

3352. Count K-Reducible Numbers Less Than N
Hard

You are given a binary string s representing a number n in its binary form.

You are also given an integer k.

An integer x is called k-reducible if performing the following operation at most k times reduces it to 1:

Replace x with the count of set bits in its binary representation.

For example, the binary representation of 6 is "110". Applying the operation once reduces it to 1 (since "110" has two set bits, and the binary representation of 2 is "10", which has one set bit).

Return an integer denoting the number of positive integers less than n that are k-reducible.

Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: s = "111", k = 1
Output: 3
Explanation:
n = 7. The 1-reducible integers less than 7 are 1, 2, and 4.

Example 2:

Input: s = "1000", k = 2
Output: 6
Explanation:
n = 8. The 2-reducible integers less than 8 are 1, 2, 3, 4, 5, and 6.

Example 3:

Input: s = "1", k = 3
Output: 0
Explanation:
There are no positive integers less than n = 1, so the answer is 0.


Constraints:

1 <= s.length <= 800
s has no leading zeros.
s consists only of the characters '0' and '1'.
1 <= k <= 5

"""

# V0
# IDEA : ONE STEP COLLAPSES x TO ITS POPCOUNT — SO GROUP BY POPCOUNT
#
#   the first operation sends x to popcount(x), which is at most 800. so x is
#   k-reducible exactly when that small number needs at most k-1 more steps,
#   and those step counts are cheap to precompute for 1..800.
#
#   what remains is "how many x < n have popcount exactly c", a standard
#   binary digit-count : walk the bits of n, and at each position holding a
#   1, fix it to 0 and let the remaining suffix be free — every choice of
#   `need` ones among the remaining positions gives a number below n.
#
#   summing C(remaining, need) over the good popcounts finishes it.
#
# time = O(len(s)^2), space = O(len(s)^2)
class Solution(object):
    def countKReducibleNumbers(self, s, k):
        MOD = 10 ** 9 + 7
        m = len(s)

        # steps[v] = operations needed to bring v down to 1
        steps = [0] * (m + 1)
        for v in range(2, m + 1):
            steps[v] = steps[bin(v).count('1')] + 1

        # binomials up to m
        C = [[0] * (m + 1) for _ in range(m + 1)]
        for i in range(m + 1):
            C[i][0] = 1
            for j in range(1, i + 1):
                C[i][j] = (C[i - 1][j - 1] + C[i - 1][j]) % MOD

        res = 0
        ones = 0                                # ones fixed so far in the prefix
        for i, ch in enumerate(s):
            if ch == '1':
                free = m - i - 1                # positions left after this one
                for extra in range(free + 1):
                    c = ones + extra            # total popcount of the candidate
                    if c == 0:
                        continue                # that would be the number 0
                    if steps[c] + 1 <= k:
                        res = (res + C[free][extra]) % MOD
                ones += 1
        return res % MOD
