"""

2376. Count Special Integers
Hard

We call a positive integer special if all of its digits are distinct.

Given a positive integer n, return the number of special integers that belong to the interval [1, n].


Example 1:

Input: n = 20
Output: 19
Explanation: All the integers from 1 to 20, except 11, are special. Thus, there are 19 special integers.

Example 2:

Input: n = 5
Output: 5
Explanation: All the integers from 1 to 5 are special.

Example 3:

Input: n = 135
Output: 110
Explanation: There are 110 integers from 1 to 135 that are special.
Some of the integers that are not special are: 22, 114, and 131.


Constraints:

1 <= n <= 2 * 10^9

"""

# V0
# IDEA : COUNT BY DIGIT LENGTH, THEN WALK n's DIGITS FIXING A COMMON PREFIX
#
#   part 1 — every length strictly below len(n) is unconstrained :
#       a number with L digits has 9 choices for the lead (no leading zero)
#       and then P(9, L - 1) ways to pick the rest from the unused digits.
#
#   part 2 — same length as n : walk the digits left to right keeping the set
#       already fixed. at position i, placing any UNUSED digit strictly below
#       n[i] frees the remaining L - i - 1 slots to be any arrangement of the
#       9 - i still-available digits, i.e. P(9 - i, L - i - 1).
#       then commit n[i] itself and continue — unless it repeats a digit
#       already used, in which case no longer prefix is possible and the walk
#       stops.
#
#   if the walk never breaks, n itself is special and counts as 1 more.
#
# time = O(digits^2), space = O(digits)
class Solution(object):
    def countSpecialNumbers(self, n):
        s = str(n)
        L = len(s)

        def perm(m, k):
            """m * (m-1) * ... * (m-k+1)"""
            out = 1
            for t in range(k):
                out *= m - t
            return out

        res = 0
        for length in range(1, L):
            res += 9 * perm(9, length - 1)

        used = set()
        for i, ch in enumerate(s):
            d = int(ch)
            low = 1 if i == 0 else 0
            for x in range(low, d):
                if x not in used:
                    res += perm(9 - i, L - i - 1)
            if d in used:
                break
            used.add(d)
        else:
            res += 1               # n itself has all-distinct digits
        return res
