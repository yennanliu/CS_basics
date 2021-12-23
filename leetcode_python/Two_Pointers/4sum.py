"""

18. 4Sum
Medium

Given an array nums of n integers, return an array of all the unique quadruplets [nums[a], nums[b], nums[c], nums[d]] such that:

0 <= a, b, c, d < n
a, b, c, and d are distinct.
nums[a] + nums[b] + nums[c] + nums[d] == target
You may return the answer in any order.

 

Example 1:

Input: nums = [1,0,-1,0,-2,2], target = 0
Output: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
Example 2:

Input: nums = [2,2,2,2,2], target = 8
Output: [[2,2,2,2]]
 

Constraints:

1 <= nums.length <= 200
-109 <= nums[i] <= 109
-109 <= target <= 109

"""

# V0

# V1
# https://leetcode.com/problems/4sum/discuss/164105/Python-solution
# IDEA : BREAK DOWN + 2 SUM
# First sort the array, loop over the first two indices i & j, and the problem reduces to the 2Sum problem, i.e., finding two indices k & l such that nums[k]+nums[l] == target - nums[i] - nums[j], which takes O(N) time. The total time complexity is then O(N^3).
class Solution(object):
    def fourSum(self, nums, target):
        N = len(nums)
        if N < 4:
            return []
        res = []
        nums = sorted(nums)
        i = 0
        for i in range(N-3):
            if 0 < i < N-3 and nums[i] == nums[i-1]:
                continue        
            for j in range(i+1,N-2):
                if i+1 < j < N-2 and nums[j] == nums[j-1]:
                    continue
                k = j+1
                l = N-1
                while k < l:
                    summ = nums[i]+nums[j]+nums[k]+nums[l]
                    if summ == target:
                        res.append([nums[i],nums[j],nums[k],nums[l]])
                        while k < N-1 and nums[k] == nums[k+1]:
                            k += 1
                        while l > 0 and nums[l] == nums[l-1]:
                            l -= 1
                        k += 1
                        l -= 1
                    elif summ < target:
                        k += 1
                    else:
                        l -= 1
        return res

# V1'
# https://leetcode.com/problems/4sum/solution/
# IDEA : HASH SET
class Solution:
    def fourSum(self, nums, target):
	
        def kSum(nums, target, k):
            res = []
            
            # If we have run out of numbers to add, return res.
            if not nums:
                return res
            
            # There are k remaining values to add to the sum. The 
            # average of these values is at least target // k.
            average_value = target // k
            
            # We cannot obtain a sum of target if the smallest value
            # in nums is greater than target // k or if the largest 
            # value in nums is smaller than target // k.
            if average_value < nums[0] or nums[-1] < average_value:
                return res
            
            if k == 2:
                return twoSum(nums, target)
    
            for i in range(len(nums)):
                if i == 0 or nums[i - 1] != nums[i]:
                    for subset in kSum(nums[i + 1:], target - nums[i], k - 1):
                        res.append([nums[i]] + subset)
    
            return res

        def twoSum(nums, target):
            res = []
            s = set()
    
            for i in range(len(nums)):
                if len(res) == 0 or res[-1][1] != nums[i]:
                    if target - nums[i] in s:
                        res.append([target - nums[i], nums[i]])
                s.add(nums[i])
    
            return res

        nums.sort()
        return kSum(nums, target, 4)

# V1''
# https://leetcode.com/problems/4sum/solution/
# IDEA : Two Pointers
class Solution:
    def fourSum(self, nums, target):
	
        def kSum(nums, target, k):
            res = []
            
            # If we have run out of numbers to add, return res.
            if not nums:
                return res
            
            # There are k remaining values to add to the sum. The 
            # average of these values is at least target // k.
            average_value = target // k
            
            # We cannot obtain a sum of target if the smallest value
            # in nums is greater than target // k or if the largest 
            # value in nums is smaller than target // k.
            if average_value < nums[0] or nums[-1] < average_value:
                return res
            
            if k == 2:
                return twoSum(nums, target)
    
            for i in range(len(nums)):
                if i == 0 or nums[i - 1] != nums[i]:
                    for subset in kSum(nums[i + 1:], target - nums[i], k - 1):
                        res.append([nums[i]] + subset)
    
            return res

        def twoSum(nums, target):
            res = []
            lo, hi = 0, len(nums) - 1
    
            while (lo < hi):
                curr_sum = nums[lo] + nums[hi]
                if curr_sum < target or (lo > 0 and nums[lo] == nums[lo - 1]):
                    lo += 1
                elif curr_sum > target or (hi < len(nums) - 1 and nums[hi] == nums[hi + 1]):
                    hi -= 1
                else:
                    res.append([nums[lo], nums[hi]])
                    lo += 1
                    hi -= 1
                                                         
            return res

        nums.sort()
        return kSum(nums, target, 4)

# V1'''
# https://leetcode.com/problems/4sum/discuss/8604/Python-solution-with-detailed-explanation
# IDEA : BREAK DOWN + 3 SUM
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        nums.sort()
        N, result = len(nums), []
        for i in range(N):
            if i > 0 and nums[i] == nums[i-1]:
                continue
            for j in range(i+1, N):
                if j > i+1 and nums[j] == nums[j-1]:
                    continue
                x = target - nums[i] - nums[j]
                s,e = j+1, N-1
                while s < e:
                    if nums[s]+nums[e] == x:
                        result.append([nums[i], nums[j], nums[s], nums[e]])
                        s = s+1
                        while s < e and nums[s] == nums[s-1]:
                            s = s+1
                    elif nums[s]+nums[e] < x:
                        s = s+1
                    else:
                        e = e-1
        return result

# V2