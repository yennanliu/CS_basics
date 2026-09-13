"""

970. Powerful Integers
Medium

Given three integers x, y, and bound, return a list of all the powerful integers that have a value less than or equal to bound.

An integer is powerful if it can be represented as x^i + y^j for some integers i >= 0 and j >= 0.

You may return the answer in any order. In your answer, each value should occur at most once.

Example 1:

Input: x = 2, y = 3, bound = 10
Output: [2,3,4,5,7,9,10]
Explanation:
2 = 2^0 + 3^0
3 = 2^1 + 3^0
4 = 2^0 + 3^1
5 = 2^1 + 3^1
7 = 2^2 + 3^1
9 = 2^3 + 3^0
10 = 2^0 + 3^2

Example 2:

Input: x = 3, y = 5, bound = 15
Output: [2,4,6,8,10,14]

Constraints:

1 <= x, y <= 100
0 <= bound <= 10^6

"""

# V0

# V1
# https://www.twblogs.net/a/5c31e79ebd9eee35b21cb4a5
# time = O((log n)^2)  # n = bound
# space = O(r)  # r = size of result
class Solution(object):
    def powerfulIntegers(self, x, y, bound):
        """
        :type x: int
        :type y: int
        :type bound: int
        :rtype: List[int]
        """
        res = set()
        i = 0
        while x ** i <= bound and i <= bound:
            j = 0
            while j <= bound:
                target = x ** i + y ** j
                if target > bound:
                    break
                res.add(target)
                j += 1
            i += 1
        return list(res)  

# V2
# time = O((log n)^2)  # n = bound
# space = O(r)  # r = size of result
import math
class Solution(object):
    def powerfulIntegers(self, x, y, bound):
        """
        :type x: int
        :type y: int
        :type bound: int
        :rtype: List[int]
        """
        result = set()
        log_x = int(math.floor(math.log(bound) / math.log(x)))+1 if x != 1 else 1
        log_y = int(math.floor(math.log(bound) / math.log(y)))+1 if y != 1 else 1
        pow_x = 1
        for i in range(log_x):
            pow_y = 1
            for j in range(log_y):
                val = pow_x + pow_y
                if val <= bound:
                    result.add(val)
                pow_y *= y
            pow_x *= x
        return list(result)
