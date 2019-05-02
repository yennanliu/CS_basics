# V0 : DEV 

# V1 
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

# if __name__ == '__main__':
#     nums = [2, 3, -2, 4]
#     solu = Solution()
#     print(solu.maxstrtest1(nums))

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

