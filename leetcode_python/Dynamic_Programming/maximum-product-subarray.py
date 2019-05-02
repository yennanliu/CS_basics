# V0 : DEV 

# V1 
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

# V2 
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

