"""

1012. Numbers With Repeated Digits
Hard

Given an integer n, return the number of positive integers in the range [1, n] that have at least one repeated digit.


Example 1:

Input: n = 20
Output: 1
Explanation: The only positive number (<= 20) with at least 1 repeated digit is 11.

Example 2:

Input: n = 100
Output: 10
Explanation: The positive numbers (<= 100) with atleast 1 repeated digit are 11, 22, 33, 44, 55, 66, 77, 88, 99, and 100.

Example 3:

Input: n = 1000
Output: 262


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : COMBINATORICS (count the COMPLEMENT)
#
#   answer = n - (count of x in [1, n] whose digits are ALL DISTINCT)
#
#   let L = number of digits of n. count the distinct-digit numbers in 2 parts:
#
#   1) numbers with FEWER than L digits (length i, 1 <= i < L)
#        first digit : 9 choices (1..9)
#        rest        : P(9, i-1) permutations of the remaining 9 digits
#
#   2) numbers with EXACTLY L digits, by fixing a common prefix with n
#        walk position by position; at position i put a digit smaller
#        than n[i] (and unused so far), then the remaining L-i-1 positions
#        are free -> P(9-i, L-i-1)
#        if n[i] itself is already used, no longer prefix can be extended -> stop
#
#   NOTE : we walk over str(n + 1) so that the "prefix equal to n" case
#          (n itself) is included in the count without a special check.
#
# time = O(L^2), L = number of digits ~ 10
# space = O(L)
class Solution(object):
    def numDupDigitsAtMostN(self, n):
        # P(m, k) = m * (m-1) * ... * (m-k+1)
        def perm(m, k):
            res = 1
            for i in range(k):
                res *= (m - i)
            return res

        digits = [int(c) for c in str(n + 1)]
        L = len(digits)

        # 1) shorter lengths
        distinct = 0
        for i in range(1, L):
            distinct += 9 * perm(9, i - 1)

        # 2) same length, prefix by prefix
        used = set()
        for i, d in enumerate(digits):
            low = 1 if i == 0 else 0
            for j in range(low, d):
                if j in used:
                    continue
                distinct += perm(9 - i, L - i - 1)
            if d in used:
                break
            used.add(d)

        return n - distinct
