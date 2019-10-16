# V0 : DEV 

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

# V1' 
# https://blog.csdn.net/hyperbolechi/article/details/43038749
def Solution_findmax(arr):
    cursum=0
    maxval=arr[0]
    for index in range(len(arr)):
        if cursum<0:
            cursum=0
        cursum+=arr[index]
        maxval=max(maxval,cursum)

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

# V1''' 
# https://blog.csdn.net/qqxx6661/article/details/78167981
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
