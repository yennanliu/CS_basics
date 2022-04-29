"""

396. Rotate Function
Medium

You are given an integer array nums of length n.

Assume arrk to be an array obtained by rotating nums by k positions clock-wise. We define the rotation function F on nums as follow:

F(k) = 0 * arrk[0] + 1 * arrk[1] + ... + (n - 1) * arrk[n - 1].
Return the maximum value of F(0), F(1), ..., F(n-1).

The test cases are generated so that the answer fits in a 32-bit integer.

 
Example 1:

Input: nums = [4,3,2,6]
Output: 26
Explanation:
F(0) = (0 * 4) + (1 * 3) + (2 * 2) + (3 * 6) = 0 + 3 + 4 + 18 = 25
F(1) = (0 * 6) + (1 * 4) + (2 * 3) + (3 * 2) = 0 + 4 + 6 + 6 = 16
F(2) = (0 * 2) + (1 * 6) + (2 * 4) + (3 * 3) = 0 + 6 + 8 + 9 = 23
F(3) = (0 * 3) + (1 * 2) + (2 * 6) + (3 * 4) = 0 + 2 + 12 + 12 = 26
So the maximum value of F(0), F(1), F(2), F(3) is F(3) = 26.
Example 2:

Input: nums = [100]
Output: 0
 

Constraints:

n == nums.length
1 <= n <= 105
-100 <= nums[i] <= 100

"""

# V0
# IDEA : MATH
# first, we represent the F(1) op as below:
#
# F(0) = 0A + 1B + 2C +3D
#
# F(1) = 0D + 1A + 2B +3C
#
# F(2) = 0C + 1D + 2A +3B
#
# F(3) = 0B + 1C + 2D +3A
#
# then, by some math manipulation, we have below relation:
#
# set sum = 1A + 1B + 1C + 1D
#
# -> F(1) = F(0) + sum - 4D
#
# -> F(2) = F(1) + sum - 4C
#
# -> F(3) = F(2) + sum - 4B
#
# so we find the rules!
#
# => F(i) = F(i-1) + sum - n*A[n-i]
#
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

# V0'
# IDEA : BRUTE FORCE (TLE)
class Solution(object):
    def maxRotateFunction(self, nums):
        # help func
        def help(arr):
            ans = 0
            for i in range(len(arr)):
                tmp = i * arr[i]
                ans += tmp
            return ans
        # edge case
        if not nums:
            return 0
        # rotate
        ans = -float('inf')
        for i in range(len(nums)):
            tmp = nums.pop(-1)
            nums.insert(0, tmp)
            cur = help(nums)
            ans = max(ans, cur)
            #print("nums = " + str(nums) + " cur = " + str(cur))
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