"""

2104. Sum of Subarray Ranges
Medium

You are given an integer array nums. The range of a subarray of nums is the difference between the largest and smallest element in the subarray.

Return the sum of all subarray ranges of nums.

A subarray is a contiguous non-empty sequence of elements within an array.

 

Example 1:

Input: nums = [1,2,3]
Output: 4
Explanation: The 6 subarrays of nums are the following:
[1], range = largest - smallest = 1 - 1 = 0 
[2], range = 2 - 2 = 0
[3], range = 3 - 3 = 0
[1,2], range = 2 - 1 = 1
[2,3], range = 3 - 2 = 1
[1,2,3], range = 3 - 1 = 2
So the sum of all ranges is 0 + 0 + 0 + 1 + 1 + 2 = 4.
Example 2:

Input: nums = [1,3,3]
Output: 4
Explanation: The 6 subarrays of nums are the following:
[1], range = largest - smallest = 1 - 1 = 0
[3], range = 3 - 3 = 0
[3], range = 3 - 3 = 0
[1,3], range = 3 - 1 = 2
[3,3], range = 3 - 3 = 0
[1,3,3], range = 3 - 1 = 2
So the sum of all ranges is 0 + 0 + 0 + 2 + 0 + 2 = 4.
Example 3:

Input: nums = [4,-2,-3,4,1]
Output: 59
Explanation: The sum of all subarray ranges of nums is 59.
 

Constraints:

1 <= nums.length <= 1000
-109 <= nums[i] <= 109
 

Follow-up: Could you find a solution with O(n) time complexity?

"""

# V0
# IDEA : BRUTE FORCE
class Solution:
    def subArrayRanges(self, nums):
        res = 0
        for i in range(len(nums)):
            curMin = float("inf")
            curMax = -float("inf")
            for j in range(i, len(nums)):
                curMin = min(curMin, nums[j])
                curMax = max(curMax, nums[j])
                res += curMax - curMin
        return res

# V0'
# IDEA : INCREASING STACK
class Solution:
    def subArrayRanges(self, A0):
        res = 0
        inf = float('inf')
        A = [-inf] + A0 + [-inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] > x:
                j = s.pop()
                k = s[-1]
                res -= A[j] * (i - j) * (j - k)
            s.append(i)
            
        A = [inf] + A0 + [inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] < x:
                j = s.pop()
                k = s[-1]
                res += A[j] * (i - j) * (j - k)
            s.append(i)
        return res

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1624303/python-bruct-foce
class Solution:
    def subArrayRanges(self, nums):
        res = 0
        for i in range(len(nums)):
            curMin = float("inf")
            curMax = -float("inf")
            for j in range(i, len(nums)):
                curMin = min(curMin, nums[j])
                curMax = max(curMax, nums[j])
                res += curMax - curMin
        return res

# V1'
# IDEA : 2 POINTERS + stack
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1624222/JavaC%2B%2BPython-O(n)-solution-detailed-explanation
# IDEA:
# Follow the explanation in 907. Sum of Subarray Minimums
#
# Intuition
# res = sum(A[i] * f(i))
# where f(i) is the number of subarrays,
# in which A[i] is the minimum.
#
# To get f(i), we need to find out:
# left[i], the length of strict bigger numbers on the left of A[i],
# right[i], the length of bigger numbers on the right of A[i].
#
# Then,
# left[i] + 1 equals to
# the number of subarray ending with A[i],
# and A[i] is single minimum.
#
# right[i] + 1 equals to
# the number of subarray starting with A[i],
# and A[i] is the first minimum.
#
# Finally f(i) = (left[i] + 1) * (right[i] + 1)
#
# For [3,1,2,4] as example:
# left + 1 = [1,2,1,1]
# right + 1 = [1,3,2,1]
# f = [1,6,2,1]
# res = 3 * 1 + 1 * 6 + 2 * 2 + 4 * 1 = 17
#
# Explanation
# To calculate left[i] and right[i],
# we use two increasing stacks.
#
# It will be easy if you can refer to this problem and my post:
# 901. Online Stock Span
# I copy some of my codes from this solution.
class Solution:
    def subArrayRanges(self, A0):
        res = 0
        inf = float('inf')
        A = [-inf] + A0 + [-inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] > x:
                j = s.pop()
                k = s[-1]
                res -= A[j] * (i - j) * (j - k)
            s.append(i)
            
        A = [inf] + A0 + [inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] < x:
                j = s.pop()
                k = s[-1]
                res += A[j] * (i - j) * (j - k)
            s.append(i)
        return res

# V1''
# IDEA : monotonic stack
# https://zhuanlan.zhihu.com/p/444725220
class Solution:
    def subArrayRanges(self, nums):
        A, s, res = [-float('inf')] + nums + [-float('inf')], [], 0
        for i, num in enumerate(A):
            while s and num < A[s[-1]]:
                j = s.pop()
                res -= (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        A, s = [float('inf')] + nums + [float('inf')], []
        for i, num in enumerate(A):
            while s and num > A[s[-1]]:
                j = s.pop()
                res += (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        return res 

# V1'''
# IDEA : BRUTE FORCE
# https://blog.csdn.net/sinat_30403031/article/details/122082809

# V1''''
# IDEA : 2 POINTERS
# https://www.codeleading.com/article/65096149220/
class Solution:
    def subArrayRanges(self, nums):
        count = 0
        for i in range(len(nums) - 1):
            # set left index = i
            max_num = min_num = nums[i]
            # here we need to record current max, min. So can count diff sum
            for t in range(i+1, len(nums)):
                # keep moving left pointer
                if nums[t] < min_num:
                    min_num = nums[t]
                if nums[t] > max_num:
                    max_num = nums[t]
                count += max_num - min_num
        return count

# V1'''''
# IDEA : sumSubarray
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1638345/Python-0(n)
class Solution:
    def subArrayRanges(self, nums):
        return self.sumSubarray(nums, operator.gt) - self.sumSubarray(nums, operator.lt)
    
    def sumSubarray(self, arr, comp):
        n = len(arr) 
        stack = []
        res = 0
            
        for idx in range(n+1):
            while stack and (idx == n or comp(arr[idx], arr[stack[-1]])):
                curr, prev = stack[-1], stack[-2] if len(stack) > 1 else -1
                res = res + (arr[curr] * (idx - curr) * (curr - prev))
                stack.pop()
            stack.append(idx)
        
        return res

# V1'''''''
# IDEA : DP
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1624305/Python-DP-Solution
class Solution:
    def subArrayRanges(self, nums):
            dp = [[(None, None) for i in range(len(nums))] for j in range(len(nums))]
            dp[len(nums) -1][len(nums) -1] = (nums[len(nums) -1], nums[len(nums) -1])

            res = 0
            for i in range(len(nums) -2, -1, -1):
                for j in range(i, len(nums)):
                    if dp[i][j-1] != (None, None):
                        dp[i][j] = (max(dp[i][j-1][0], nums[j]), min(dp[i][j-1][1], nums[j]))

                    elif dp[i + 1][j] != (None, None):
                        dp[i][j] = (max(dp[i + 1][j][0], nums[i]), min(dp[i + 1][j][1], nums[i]))
                    else:
                        dp[i][j] = (nums[i], nums[j])

                    res += dp[i][j][0] - dp[i][j][1]
            return res

# V2