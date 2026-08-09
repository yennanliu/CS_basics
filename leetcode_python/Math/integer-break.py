"""

343. Integer Break
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given an integer n, break it into the sum of k positive integers, where k >= 2, and maximize the product of those integers.

Return the maximum product you can get.

 

Example 1:

Input: n = 2
Output: 1
Explanation: 2 = 1 + 1, 1 × 1 = 1.
Example 2:

Input: n = 10
Output: 36
Explanation: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36.
 

Constraints:

2 <= n <= 58
 

"""

# V0
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        pass


# V0-1
# IDEA: MATH (gpt)
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """

        # Edge case
        if n == 2:
            return 1

        max_product = 1

        # Try splitting n into k parts
        for k in range(2, n):
            tmp = self.get_product(n, k)
            max_product = max(max_product, tmp)

        return max_product

    def get_product(self, n, k):
        """
        Split n into k parts as evenly as possible
        and return their maximum product.
        """

        q = n // k
        r = n % k

        # r parts have value q + 1
        # k-r parts have value q
        product = 1

        for _ in range(r):
            product *= (q + 1)

        for _ in range(k - r):
            product *= q

        return product


# V0-2
# IDEA: MATH (gemini)
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        # Edge cases defined by the problem
        if n == 2: return 1
        if n == 3: return 2
        
        max_product = 1
        
        # k is the number of parts we break 'n' into. 
        # We must break it into at least 2 parts, up to n parts.
        for k in range(2, n + 1):
            tmp = self.get_product(n, k)
            max_product = max(max_product, tmp)
            
        return max_product

    def get_product(self, n, k):
        # We want to divide 'n' into 'k' pieces as evenly as possible.
        base = n // k
        remainder = n % k
        
        # To make it add up to 'n', we will have 'remainder' pieces of size (base + 1)
        # and the remaining pieces will be of size (base).
        # Example: n=10, k=3 -> base=3, remainder=1.
        # We need one piece of (4), and two pieces of (3). Product = 4 * 3 * 3 = 36.
        return (base ** (k - remainder)) * ((base + 1) ** remainder)


# V0
# time = O(n)
# space = O(n)
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n <= 3: return n - 1
        dp = [0] * (n + 1)
        dp[2], dp[3] = 2, 3
        for x in range(4, n + 1):
            dp[x] = max(3 * dp[x - 3], 2 * dp[x - 2])
        return dp[n]

# V1
# http://bookshadow.com/weblog/2016/04/19/leetcode-integer-break/
# time = O(n)
# space = O(n)
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n <= 3: return n - 1
        dp = [0] * (n + 1)
        dp[2], dp[3] = 2, 3
        for x in range(4, n + 1):
            dp[x] = max(3 * dp[x - 3], 2 * dp[x - 2])
        return dp[n]

# V1'
# http://bookshadow.com/weblog/2016/04/19/leetcode-integer-break/
from functools import reduce
import operator
# time = O(n^2)
# space = O(n)
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        return max([reduce(operator.mul, self.splitInt(n, m)) for m in range(2, n + 1)])
    
    def splitInt(self, n, m):
        quotient = n / m
        remainder = n % m
        return [quotient] * (m - remainder) + [quotient + 1] * remainder

# time = O(n^2)
# space = O(1)
class Solution2(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        return max([self.mulSplitInt(n, m) for m in range(2, n + 1)])
    
    def mulSplitInt(self, n, m):
        quotient = n / m
        remainder = n % m
        return quotient ** (m - remainder) * (quotient + 1) ** remainder

# V2'
# EXAMPLE: 
# 2  ->  1 * 1
# 3  ->  2 * 1
# 4  ->  2 * 2
# 5  ->  3 * 2
# 6  ->  3 * 3
# 7  ->  3 * 2 * 2
# 8  ->  3 * 3 * 2
# 9  ->  3 * 3 * 3
# 10 ->  3 * 3 * 2 * 2
# 11 ->  3 * 3 * 3 * 2
# 12 ->  3 * 3 * 3 * 3
# 13 ->  3 * 3 * 3 * 2 * 2


# n / 3 <= 1  : split into prodcut of 2 integers  (n*2)
# n / 3 > 1 : split into n counts 3 and 2 product (n*3*n*2)
# n % 3 == 0 : split into n counts 3 product  (n*3)
# n % 3 == 1 : split into n-1 count 3 and 2 count n product ((n-1)*3 * (n)*2)
# n % 3 == 2 : split into n count 3 and 1 count 2 product   ((n)*3*2)


# def integerBreak(self, n):
#     """
#     :type n: int
#     :rtype: int
#     """
#     div = n / 3
#     if div <= 1:
#         return (n / 2) * (n / 2 + n % 2)
#     mod = n % 3
#     if mod == 0:
#         return 3 ** div
#     elif mod == 1:
#         return 3 ** (div - 1) * 4
#     elif mod == 2:
#         return 3 ** div * 2



# V3
# time = O(log n)  # pow is O(log n)
# space = O(1)
class Solution(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n < 4:
            return n - 1

        #  Proof.
        #  1. Let n = a1 + a2 + ... + ak, product = a1 * a2 * ... * ak
        #      - For each ai >= 4, we can always maximize the product by:
        #        ai <= 2 * (ai - 2)
        #      - For each aj >= 5, we can always maximize the product by:
        #        aj <= 3 * (aj - 3)
        #
        #     Conclusion 1:
        #      - For n >= 4, the max of the product must be in the form of
        #        3^a * 2^b, s.t. 3a + 2b = n
        #
        #  2. To maximize the product = 3^a * 2^b s.t. 3a + 2b = n
        #      - For each b >= 3, we can always maximize the product by:
        #        3^a * 2^b <= 3^(a+2) * 2^(b-3) s.t. 3(a+2) + 2(b-3) = n
        #
        #     Conclusion 2:
        #      - For n >= 4, the max of the product must be in the form of
        #        3^Q * 2^R, 0 <= R < 3 s.t. 3Q + 2R = n
        #        i.e.
        #          if n = 3Q + 0,   the max of the product = 3^Q * 2^0
        #          if n = 3Q + 2,   the max of the product = 3^Q * 2^1
        #          if n = 3Q + 2*2, the max of the product = 3^Q * 2^2

        res = 0
        if n % 3 == 0:            #  n = 3Q + 0, the max is 3^Q * 2^0
            res = 3 ** (n // 3)
        elif n % 3 == 2:          #  n = 3Q + 2, the max is 3^Q * 2^1
            res = 3 ** (n // 3) * 2
        else:                     #  n = 3Q + 4, the max is 3^Q * 2^2
            res = 3 ** (n // 3 - 1) * 4
        return res
        
# V4
# time = O(n)
# space = O(1)
# DP solution.
class Solution2(object):
    def integerBreak(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n < 4:
            return n - 1

        # integerBreak(n) = max(integerBreak(n - 2) * 2, integerBreak(n - 3) * 3)
        res = [0, 1, 2, 3]
        for i in range(4, n + 1):
            res[i % 4] = max(res[(i - 2) % 4] * 2, res[(i - 3) % 4] * 3)
        return res[n % 4]
