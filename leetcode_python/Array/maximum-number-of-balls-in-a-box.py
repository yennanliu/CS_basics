"""

1742. Maximum Number of Balls in a Box
Easy

You are working in a ball factory where you have n balls numbered from lowLimit up to highLimit inclusive (i.e., n == highLimit - lowLimit + 1), and an infinite number of boxes numbered from 1 to infinity.

Your job at this factory is to put each ball in the box with a number equal to the sum of digits of the ball's number. For example, the ball number 321 will be put in the box number 3 + 2 + 1 = 6 and the ball number 10 will be put in the box number 1 + 0 = 1.

Given two integers lowLimit and highLimit, return the number of balls in the box with the most balls.


Example 1:

Input: lowLimit = 1, highLimit = 10
Output: 2
Explanation:
Box Number:  1 2 3 4 5 6 7 8 9 10 11 ...
Ball Count:  2 1 1 1 1 1 1 1 1 0  0  ...
Box 1 has the most number of balls with 2 balls.

Example 2:

Input: lowLimit = 5, highLimit = 15
Output: 2
Explanation:
Box Number:  1 2 3 4 5 6 7 8 9 10 11 ...
Ball Count:  1 1 1 1 2 2 1 1 1 0  0  ...
Boxes 5 and 6 have the most number of balls with 2 balls in each.

Example 3:

Input: lowLimit = 19, highLimit = 28
Output: 2
Explanation:
Box Number:  1 2 3 4 5 6 7 8 9 10 11 12 ...
Ball Count:  0 1 1 1 1 1 1 1 1 2  0  0  ...
Box 10 has the most number of balls with 2 balls.


Constraints:

1 <= lowLimit <= highLimit <= 10^5

"""

# V0
# IDEA : COUNTING BY DIGIT SUM (the box index space is tiny)
#
#   highLimit <= 10^5, so a ball number has at most 6 digits and its digit
#   sum is at most 9 * 6 = 54. that means only ~55 boxes are ever used -
#   a fixed-size array beats a hash map here.
#
#   walk every ball, compute its digit sum, bump that bucket, and return
#   the largest bucket.
#
# time = O(n * log10(highLimit)), space = O(1) (a 55-slot array)
class Solution(object):
    def countBalls(self, lowLimit, highLimit):
        cnt = [0] * 55

        for v in range(lowLimit, highLimit + 1):
            s = 0
            x = v
            while x:
                s += x % 10
                x //= 10
            cnt[s] += 1

        return max(cnt)


# V0-1
# IDEA : STRING DIGITS + Counter (hash map instead of a fixed array)
#
#   the digit sum can be read off the DECIMAL STRING instead of being peeled
#   with % / // : str(v) hands the digits over directly, and Counter grows the
#   box space on demand, so no bound on the digit sum has to be known up front
#   (which matters the moment highLimit stops being 10^5).
#
#   the mechanism differs from V0 on both axes - string scan vs modular
#   arithmetic, hash map vs pre-sized array - but the work is still one pass
#   per ball.
#
# time = O(n * log10(highLimit))
# space = O(log10(highLimit)) distinct boxes in the map
import collections
class Solution(object):
    def countBalls(self, lowLimit, highLimit):
        boxes = collections.Counter(
            sum(int(ch) for ch in str(v))
            for v in range(lowLimit, highLimit + 1)
        )
        return max(boxes.values())


# V0-2
# IDEA : DIGIT DP - COUNT PER DIGIT SUM WITHOUT VISITING A SINGLE BALL
#
#   define f(N)[s] = how many x in [0, N] have digit sum s. then the balls of
#   box s are f(highLimit)[s] - f(lowLimit - 1)[s], and the answer is the max
#   over s. no loop over the n balls at all - the cost depends only on the
#   NUMBER OF DIGITS.
#
#   f is built the standard digit-DP way :
#     free[m][s] = # of m-digit strings (leading zeros allowed) with sum s
#                  -> free[m][s] = sum over d in 0..9 of free[m-1][s-d]
#     then walk the digits of N left to right; fixing a smaller digit dd < d at
#     position i leaves the remaining `rest` positions completely free, so it
#     contributes free[rest][*] shifted by (prefix sum so far + dd). finally add
#     1 for N itself.
#
#   with D = number of digits (D = 6 here) this is O(D^2 * 9D) ~ a few thousand
#   operations regardless of how wide the range is - it still answers
#   [1, 10^18] instantly, where V0 and V0-1 cannot even start.
#
# time = O(D^3) where D = digits of highLimit (independent of n)
# space = O(D^2)
class Solution(object):
    def countBalls(self, lowLimit, highLimit):
        def counts_up_to(n):
            digits = [int(c) for c in str(n)]
            L = len(digits)
            top = 9 * L

            free = [[0] * (top + 1) for _ in range(L + 1)]
            free[0][0] = 1
            for m in range(1, L + 1):
                for s in range(9 * m + 1):
                    free[m][s] = sum(free[m - 1][s - d]
                                     for d in range(10) if s - d >= 0)

            cnt = [0] * (top + 1)
            used = 0
            for i, d in enumerate(digits):
                rest = L - i - 1
                for dd in range(d):
                    for s in range(9 * rest + 1):
                        if free[rest][s]:
                            cnt[used + dd + s] += free[rest][s]
                used += d
            cnt[used] += 1          # n itself
            return cnt

        hi = counts_up_to(highLimit)
        lo = counts_up_to(lowLimit - 1) if lowLimit > 0 else [0]
        m = max(len(hi), len(lo))
        hi += [0] * (m - len(hi))
        lo += [0] * (m - len(lo))
        # box 0 only ever holds the number 0, which is never a ball
        return max(hi[s] - lo[s] for s in range(1, m))
