"""

1067. Digit Count in Range
Hard

Given a single-digit integer d and two integers low and high, return the number
of times that d occurs as a digit in all integers in the inclusive range [low, high].


Example 1:

Input: d = 1, low = 1, high = 13
Output: 6
Explanation: The digit d = 1 occurs 6 times in 1, 10, 11, 12, 13.
Note that the digit d = 1 occurs twice in the number 11.

Example 2:

Input: d = 3, low = 100, high = 250
Output: 35
Explanation: The digit d = 3 occurs 35 times in 103,113,123,130,131,...,238,239,243.


Constraints:

0 <= d <= 9
1 <= low <= high <= 2 * 10^8

"""

# V0
# IDEA : DIGIT DP + prefix trick  count(high) - count(low - 1)
#
#  count(n) = how many times digit d shows up when writing out 1..n
#
#  dfs(pos, cnt, lead, limit)
#    pos   : current digit position (left -> right)
#    cnt   : how many `d` placed so far
#    lead  : still in the leading zero prefix (those zeros are NOT real digits)
#    limit : the prefix so far equals n's prefix, so this digit is capped
# time = O(len(n) * len(n) * 10)
# space = O(len(n) * len(n))
class Solution(object):
    def digitsCount(self, d, low, high):
        return self._count(high, d) - self._count(low - 1, d)

    def _count(self, n, d):
        if n <= 0:
            return 0

        s = str(n)
        m = len(s)
        memo = {}

        def dfs(pos, cnt, lead, limit):
            if pos == m:
                return cnt
            key = (pos, cnt, lead, limit)
            if key in memo:
                return memo[key]

            up = int(s[pos]) if limit else 9
            res = 0
            for i in range(up + 1):
                if i == 0 and lead:
                    # NOTE !!! a leading zero is not a real digit -> cnt unchanged
                    res += dfs(pos + 1, cnt, True, limit and i == up)
                else:
                    res += dfs(pos + 1, cnt + (1 if i == d else 0),
                               False, limit and i == up)

            memo[key] = res
            return res

        return dfs(0, 0, True, True)
