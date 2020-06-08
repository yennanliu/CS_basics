"""
Given an array of integers A and let n to be its length.

Assume Bk to be an array obtained by rotating the array A k positions clock-wise, we define a "rotation function" F on A as follow:

F(k) = 0 * Bk[0] + 1 * Bk[1] + ... + (n-1) * Bk[n-1].

Calculate the MAX value of F(0), F(1), ..., F(n-1).

Note:
n is guaranteed to be less than 105.

Example:

A = [4, 3, 2, 6]

F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26

So the maximum value of F(0), F(1), F(2), F(3) is F(3) = 26.
"""

# V0
# IDEA : MATH
# first, we represent the F(1) op as below:

# F(0) = 0A + 1B + 2C +3D

# F(1) = 0D + 1A + 2B +3C

# F(2) = 0C + 1D + 2A +3B

# F(3) = 0B + 1C + 2D +3A

# then, by some math manipulation, we have below relation:

# set sum = 1A + 1B + 1C + 1D

# -> F(1) = F(0) + sum - 4D

# -> F(2) = F(1) + sum - 4C

# -> F(3) = F(2) + sum - 4B

# so we find the rules!

# => F(i) = F(i-1) + sum - n*A[n-i]

# https://www.cnblogs.com/grandyang/p/5869791.html
# http://bookshadow.com/weblog/2016/09/11/leetcode-rotate-function/
class Solution(object):
    def maxRotateFunction(self, A):
        size = len(A)
        sums = sum(A)
        sumn = sum(x * n for x, n in enumerate(A))
        ans = sumn
        for x in range(size - 1, 0, -1):
            sumn += sums - size * A[x]
            ans = max(ans, sumn)
        return ans

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83002609
# IDEA : MATH PATTERN 
# -> SINCE 
# F(0) = 0A + 1B + 2C +3D

# F(1) = 0D + 1A + 2B +3C

# F(2) = 0C + 1D + 2A +3B

# F(3) = 0B + 1C + 2D +3A

# -> SO 
# F(1) = F(0) + sum - 4D

# F(2) = F(1) + sum - 4C

# F(3) = F(2) + sum - 4B
# -> THEN WE KNOW THE PATTERN OF ROTATE OPERATION IS ACTUAL :
# ---> F(i) = F(i-1) + sum - n * A[n-i]
class Solution:
    def maxRotateFunction(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        _sum = 0
        N = len(A)
        f = 0
        for i, a in enumerate(A):
            _sum += a
            f += i * a
        res = f
        for i in range(N - 1, 0, -1):
            f = f + _sum - N * A[i]
            res = max(res, f)       # since we want to calculate the MAX value of F(0), F(1), ..., F(n-1).
        return res

### Test case
s=Solution()
assert s.maxRotateFunction([]) == 0
assert s.maxRotateFunction([7]) == 0
assert s.maxRotateFunction([7,2,1]) == 15
assert s.maxRotateFunction([4, 3, 2, 6]) == 26
assert s.maxRotateFunction([0,0,0,0]) == 0
assert s.maxRotateFunction([3,7,0,1]) == 28
assert s.maxRotateFunction([1,1,1,1]) == 6
assert s.maxRotateFunction([-1,-1,-1,-1]) == -6
assert s.maxRotateFunction([-1,10,-5,1]) == 29

# V1'
# http://bookshadow.com/weblog/2016/09/11/leetcode-rotate-function/
class Solution(object):
    def maxRotateFunction(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        size = len(A)
        sums = sum(A)
        sumn = sum(x * n for x, n in enumerate(A))
        ans = sumn
        for x in range(size - 1, 0, -1):
            sumn += sums - size * A[x]
            ans = max(ans, sumn)
        return ans

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxRotateFunction(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        s = sum(A)
        fi = 0
        for i in range(len(A)):
            fi += i * A[i]

        result = fi
        for i in range(1, len(A)+1):
            fi += s - len(A) * A[-i]
            result = max(result, fi)
        return result
