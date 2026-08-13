"""

1088. Confusing Number II
Hard

A confusing number is a number that when rotated 180 degrees becomes a different
number with each digit valid.

We can rotate digits of a number by 180 degrees to form new digits.

When 0, 1, 6, 8, and 9 are rotated 180 degrees, they become 0, 1, 9, 8, and 6 respectively.
When 2, 3, 4, 5, and 7 are rotated 180 degrees, they become invalid.

Note that after rotating a number, we can ignore leading zeros.

For example, after rotating 8000, we have 0008 which is considered as just 8.

Given an integer n, return the number of confusing numbers in the inclusive range [1, n].


Example 1:

Input: n = 20
Output: 6
Explanation: The confusing numbers are [6,9,10,16,18,19].
6 converts to 9.
9 converts to 6.
10 converts to 01 which is just 1.
16 converts to 91.
18 converts to 81.
19 converts to 61.

Example 2:

Input: n = 100
Output: 19
Explanation: The confusing numbers are [6,9,10,16,18,19,60,61,66,68,80,81,86,89,90,91,98,99,100].


Constraints:

1 <= n <= 10^9

"""

# V0
# IDEA : DIGIT DFS (only generate numbers made of 0,1,6,8,9) + check rotation
#
#  a confusing number must be built ONLY from rotatable digits, so instead of
#  scanning 1..n we generate exactly those candidates digit by digit,
#  bounded by n (the classic `limit` flag of digit DP).
#
#  for each fully built candidate x, rotate it and keep it if rotate(x) != x.
#  rotating while reading x's digits from the least significant end and
#  re-appending them naturally reverses the number and drops leading zeros.
#
#  NOTE : no memo here -- the answer depends on the whole number, not on a
#         small state, but only ~5^10 candidates exist which is why it passes
# time = O(5^len(n))
# space = O(len(n))
class Solution(object):
    def confusingNumberII(self, n):
        # rotation map, -1 = not rotatable
        rot = [0, 1, -1, -1, -1, -1, 9, -1, 8, 6]
        s = str(n)
        m = len(s)

        def is_confusing(x):
            y, t = 0, x
            while t:
                t, v = divmod(t, 10)
                y = y * 10 + rot[v]
            return y != x

        def dfs(pos, limit, x):
            if pos == m:
                return 1 if is_confusing(x) else 0
            up = int(s[pos]) if limit else 9
            res = 0
            for i in range(up + 1):
                if rot[i] != -1:
                    res += dfs(pos + 1, limit and i == up, x * 10 + i)
            return res

        # x == 0 (all leading zeros) is not confusing, so it is safely excluded
        return dfs(0, True, 0)
