#
# Given an array nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position. Return the max sliding window.
#
# Follow up:
# Could you solve it in linear time?
#
# Example:
#
# Input: nums = [1,3,-1,-3,5,3,6,7], and k = 3
# Output: [3,3,5,5,6,7] 
# Explanation: 
#
# Window position                Max
# ---------------               -----
# [1  3  -1] -3  5  3  6  7       3
#  1 [3  -1  -3] 5  3  6  7       3
#  1  3 [-1  -3  5] 3  6  7       5
#  1  3  -1 [-3  5  3] 6  7       5
#  1  3  -1  -3 [5  3  6] 7       6
#  1  3  -1  -3  5 [3  6  7]      7
# 
#
# Constraints:
#
# 1 <= nums.length <= 10^5
# -10^4 <= nums[i] <= 10^4
# 1 <= k <= nums.length

# V0

# V1
# http://bookshadow.com/weblog/2015/07/18/leetcode-sliding-window-maximum/
# IDEA : DEQUE
class Solution:
    # @param {integer[]} nums
    # @param {integer} k
    # @return {integer[]}
    def maxSlidingWindow(self, nums, k):
        dq = collections.deque()
        ans = []
        for i in range(len(nums)):
            while dq and nums[dq[-1]] <= nums[i]:
                dq.pop()
            dq.append(i)
            if dq[0] == i - k:
                dq.popleft()
            if i >= k - 1:
                ans.append(nums[dq[0]])
        return ans

### Test case : dev 

# V1'
# https://blog.csdn.net/PKU_Jade/article/details/77934644
# IDEA : DEQUE
class Solution(object):
    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        if not nums:
            return []
        from collections import deque
        dq = deque()
        n = len(nums)
        ans = []

        for i in range(k-1):
        	# push k-1 elements into queue
            while len(dq) > 0:
                if nums[dq[-1]] <= nums[i]:
                    dq.pop()
                else:
                    break
            dq.append(i)  

        for i in range(k-1, n):
            while len(dq) > 0:
                if nums[dq[-1]] <= nums[i]:
                    dq.pop()
                else:
                    break
            dq.append(i)

            while dq[0] < i-k+1:
                dq.popleft()
            ans.append(nums[dq[0]])

        return ans

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/100828798
# IDEA : DEQUE
class Solution(object):
    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        que = collections.deque() # [[i, num]]
        res = []
        for i, num in enumerate(nums):
            if que and i - que[0][0] >= k:
                que.popleft()
            while que and que[-1][1] <= num:
                que.pop()
            que.append([i, num])
            if i >= k - 1:
                res.append(que[0][1])
        return res

# V2 