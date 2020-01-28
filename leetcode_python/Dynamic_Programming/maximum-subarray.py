# V0 
# IDEA : DP 
class Solution(object):
    def maxSubArray(self, nums):
        cumsum = nums[0]
        max_ = cumsum
        if not nums: return 0 
        for i in range(1, len(nums)):
            if cumsum < 0:
                cumsum = 0
            cumsum += nums[i]
            max_ = max(max_, cumsum)
        return max_ 

# V1 
# https://blog.csdn.net/hyperbolechi/article/details/43038749
# IDEA : BRUTE FORCE
def Solution_brutef(arr):
    maxval=-10000  
    for i in range(len(arr)):
        for j in range(i,len(arr)):
            if maxval<sum(arr[i:j]):
                print((i,j))
                maxval=max(maxval,sum(arr[i:j]))
                result=arr[i:j]
    return result

# V1''
# https://blog.csdn.net/qqxx6661/article/details/78167981
# IDEA : DP
# DP EQUATION :
# -> dp[i+1] = dp[i] + s[i+1] (if dp[i] >= 0 )
# -> dp[i+1] = s[i]           (if dp[i] < 0 )
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        current = nums[0]
        m = current
        for i in range(1, len(nums)):
            if current < 0:
                current = 0
            current += nums[i]
            m = max(current, m)
        return m

# V1''
# https://blog.csdn.net/qqxx6661/article/details/78167981
# IDEA : DP
# DP STATUS EQUATION : 
# dp[i] = dp[i-1] + s[i] (dp[i-1] >= 0)
# dp[i] = s[i] (dp[i-1] < 0)
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums:
            return 0
        dp = [nums[0] for i in range(len(nums))]
        max_result = nums[0]  # if start from nums[0], then must get smaller if there is minus number; will get larger if interger
        for i in range(1, len(nums)):
            if dp[i-1] < 0:
                dp[i] = nums[i]
            else:
                dp[i] = dp[i-1] + nums[i]
            if max_result < dp[i]:
                max_result = dp[i]
        return max_result

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxSubArray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        max_nums = max(nums)
        if max_nums < 0:
            return max_nums
        global_max, local_max = 0, 0
        for x in nums:
            local_max = max(0, local_max + x)
            global_max = max(global_max, local_max)
        return global_max
