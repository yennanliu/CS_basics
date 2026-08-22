"""

1304. Find N Unique Integers Sum up to Zero
Easy

Given an integer n, return any array containing n unique integers such that they add up to 0.


Example 1:

Input: n = 5
Output: [-7,-1,1,3,4]
Explanation: These arrays also are accepted [-5,-1,1,2,3] , [-3,-1,2,-2,4].

Example 2:

Input: n = 3
Output: [-1,0,1]

Example 3:

Input: n = 1
Output: [0]


Constraints:

1 <= n <= 1000

"""

# V0
# IDEA : MATH (emit symmetric +/- pairs, plus a lone 0 when n is odd)
#
#   pair up the output : 1 and -1, 2 and -2, ... each pair sums to 0 and all
#   values stay distinct.
#   that fills n // 2 pairs = an even count; if n is odd, one slot is left,
#   and 0 is the only value that keeps the total at 0 without repeating.
#
# time = O(n), space = O(n)
class Solution(object):
    def sumZero(self, n):
        res = []
        for i in range(1, n // 2 + 1):
            res.append(i)
            res.append(-i)
        if n % 2 == 1:
            res.append(0)
        return res


# V0-1
# IDEA : FILL 1..n-1, THEN CANCEL EVERYTHING WITH ONE COMPENSATING VALUE
#
#   any n-1 distinct numbers can be freely chosen; the last slot is then
#   forced to be -(sum of the others). picking 1, 2, ..., n-1 makes that
#   last value -(n-1)*n/2, which is <= 0 and therefore can never collide
#   with the positive prefix (for n >= 2 it is strictly negative).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def sumZero(self, n):
        res = list(range(1, n))
        res.append(-sum(res))
        return res


# V0-2
# IDEA : ARITHMETIC PROGRESSION CENTERED ON 0 (STEP 2)
#
#   instead of building pairs, emit the n-term progression
#   -(n-1), -(n-3), ..., (n-3), (n-1) i.e. range(1 - n, n, 2).
#   it is symmetric about 0, so the terms cancel term-by-term and the sum is
#   0; the step of 2 keeps every term distinct and automatically includes 0
#   exactly when n is odd (no parity branch needed).
#
# time = O(n)
# space = O(n)
class Solution(object):
    def sumZero(self, n):
        return list(range(1 - n, n, 2))
