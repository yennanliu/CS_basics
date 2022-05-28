"""

152. Maximum Product Subarray
Medium

Given an integer array nums, find a contiguous non-empty subarray within the array that has the largest product, and return the product.

The test cases are generated so that the answer will fit in a 32-bit integer.

A subarray is a contiguous subsequence of the array.

 

Example 1:

Input: nums = [2,3,-2,4]
Output: 6
Explanation: [2,3] has the largest product 6.
Example 2:

Input: nums = [-2,0,-1]
Output: 0
Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
 

Constraints:

1 <= nums.length <= 2 * 104
-10 <= nums[i] <= 10
The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.

"""

# V0
# IDEA : brute force + product
class Solution(object):
    def maxProduct(self, A):
        global_max, local_max, local_min = float("-inf"), 1, 1
        for x in A:
            local_max = max(1, local_max)
            if x > 0:
                local_max, local_min = local_max * x, local_min * x
            else:
                local_max, local_min = local_min * x, local_max * x
            global_max = max(global_max, local_max)
        return global_max

# V1
# IDEA : BRUTE FORCE (TLE)
# https://leetcode.com/problems/maximum-product-subarray/solution/
class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if len(nums) == 0:
            return 0

        result = nums[0]

        for i in range(len(nums)):
            accu = 1
            for j in range(i, len(nums)):
                accu *= nums[j]
                result = max(result, accu)

        return result

# V1'
# IDEA : DP
# https://leetcode.com/problems/maximum-product-subarray/solution/
class Solution:
    def maxProduct(self, nums: List[int]) -> int:
        if len(nums) == 0:
            return 0

        max_so_far = nums[0]
        min_so_far = nums[0]
        result = max_so_far

        for i in range(1, len(nums)):
            curr = nums[i]
            temp_max = max(curr, max_so_far * curr, min_so_far * curr)
            min_so_far = min(curr, max_so_far * curr, min_so_far * curr)

            max_so_far = temp_max

            result = max(max_so_far, result)

        return result

# V1''
# IDEA : brute force
# https://leetcode.com/problems/maximum-product-subarray/discuss/160466/Python-solution
class Solution(object):
    def maxProduct(self, nums):
        N = len(nums)
        if N == 1:
            return nums[0]
        max_til_here = nums[0]
        min_til_here = nums[0]
        max_end_here = nums[0]
        min_end_here = nums[0]
        for i in range(1,N):
            lis = sorted([max_end_here*nums[i], min_end_here*nums[i], nums[i]])
            max_end_here = lis[-1]
            min_end_here = lis[0]
            if max_til_here < max_end_here:
                max_til_here = max_end_here
            if min_til_here > min_end_here:
                min_til_here = min_end_here
        return max_til_here

# V1'''
# https://leetcode.com/problems/maximum-product-subarray/discuss/728760/Simple-python
class Solution(object):
    def maxProduct(self, a):
        ans = max_prod = min_prod = a[0]                       
        for x in a[1:]:
            max_prod, min_prod = max(x, min_prod*x, max_prod*x), min(x, min_prod*x, max_prod*x) 
            ans = max(ans, max_prod)
        return ans

# V1'''''
# https://blog.csdn.net/XX_123_1_RJ/article/details/81321978
# idea
# dpmax[i] = max(nums[i], dpmax[i - 1] * nums[i], dpmin[i - 1] * nums[i])  # record the maximum
# dpmin[i] = min(nums[i], dpmax[i - 1] * nums[i], dpmin[i - 1] * nums[i])  # record the minimum，and consider the negative numbeer case 
# so, ->
# dpmax  = max(nums[i], dpmax * nums[i], dpmin * nums[i])
# dpmin  = min(nums[i], dpmax * nums[i], dpmin * nums[i])
# maxout = max(maxout, dpmax)
class Solution:
    def maxstrtest(self, nums):
        n = len(nums)
        if n == 1: return nums[0]
        if n < 1: None
        dpmax, dpmin = [0]*n, [0]*n  # 初始化dp
        dpmax[0] = dpmin[0] = nums[0]
        for i in range(1, n):
            dpmax[i] = max(nums[i], dpmax[i - 1] * nums[i], dpmin[i - 1] * nums[i])  # 记录最大值
            dpmin[i] = min(nums[i], dpmax[i - 1] * nums[i], dpmin[i - 1] * nums[i])  # 记录最小值，考虑负数的情况
        return max(dpmax)

    def maxstrtest1(self, nums):
        if len(nums) == 1: return nums[0]
        dpmax, dpmin, maxout= 1, 1, float('-inf')
        for xi in nums:
            dpmax, dpmin = max(xi, dpmax * xi, dpmin * xi), min(xi, dpmax * xi, dpmin * xi)
            maxout = max(maxout, dpmax)
        return maxout

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/83211451
class Solution(object):
    def maxProduct(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return 0
        N = len(nums)
        f = [0] * N
        g = [0] * N
        f[0] = g[0] = res = nums[0]
        for i in range(1, N):
            f[i] = max(f[i - 1] * nums[i], nums[i], g[i - 1] * nums[i])
            g[i] = min(f[i - 1] * nums[i], nums[i], g[i - 1] * nums[i])
            res = max(res, f[i])
        return res

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param A, a list of integers
    # @return an integer
    def maxProduct(self, A):
        global_max, local_max, local_min = float("-inf"), 1, 1
        for x in A:
            local_max, local_min = max(x, local_max * x, local_min * x), min(x, local_max * x, local_min * x)
            global_max = max(global_max, local_max)
        return global_max

class Solution2(object):
    # @param A, a list of integers
    # @return an integer
    def maxProduct(self, A):
        global_max, local_max, local_min = float("-inf"), 1, 1
        for x in A:
            local_max = max(1, local_max)
            if x > 0:
                local_max, local_min = local_max * x, local_min * x
            else:
                local_max, local_min = local_min * x, local_max * x
            global_max = max(global_max, local_max)
        return global_max