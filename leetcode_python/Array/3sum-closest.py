# https://leetcode.com/problems/3sum-closest/description/
# Time:  O(n^2)
# Space: O(1)
# Given an array S of n integers,
# find three integers in S such that the sum is closest to a given number,
# target.
# Return the sum of the three integers.
# You may assume that each input would have exactly one solution.
#
# For example, given array S = {-1 2 1 -4}, and target = 1.
#
# The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).

# V0 
# IDEA : SORT + DOUBLE POINTER
class Solution(object):
    def threeSumClosest(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        N = len(nums)
        nums.sort()
        ### beware of it 
        res = float('inf') # sum of 3 number
        ### beware of it 
        ### go through all elements in nums
        for t in range(N):
            i, j = t + 1, N - 1
            ### beware of it 
            while i < j:
                _sum = nums[t] + nums[i] + nums[j]
                if abs(_sum - target) < abs(res - target):
                    res = _sum
                if _sum > target:
                    j -= 1
                elif _sum < target:
                    i += 1
                else:
                    return target
        return res

### test case
s = Solution()
assert s.threeSumClosest([], 1) == float('inf')
assert s.threeSumClosest([-1, 2, 1, -4], 1) == 2 
assert s.threeSumClosest([-3, 0, 1, 2] , 1) == 0
assert s.threeSumClosest([2, 0, 1, -3] , 1) == 0
assert s.threeSumClosest([ 1 for _ in range(100)] , 1) == 3
assert s.threeSumClosest([ 1 for _ in range(1000)] , 0) == 3

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83116781
# IDEA : SORT + DOUBLE POINTER
class Solution(object):
    def threeSumClosest(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        N = len(nums)
        nums.sort()
        res = float('inf') # sum of 3 number
        for t in range(N):
            i, j = t + 1, N - 1
            while i < j:
                _sum = nums[t] + nums[i] + nums[j]
                if abs(_sum - target) < abs(res - target):
                    res = _sum
                if _sum > target:
                    j -= 1
                elif _sum < target:
                    i += 1
                else:
                    return target
        return res

# V1'
# https://www.cnblogs.com/zuoyuan/p/3699449.html
class Solution:
    # @return an integer
    def threeSumClosest(self, num, target):
        num.sort()
        mindiff=100000
        res=0
        for i in range(len(num)):
            left=i+1; right=len(num)-1
            while left<right:
                sum=num[i]+num[left]+num[right]
                diff=abs(sum-target)
                if diff<mindiff: mindiff=diff; res=sum
                if sum==target: return sum
                elif sum<target: left+=1
                else: right-=1
        return res
        
# V2 
# Time:  O(n^2)
# Space: O(1)
class Solution(object):
    def threeSumClosest(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        nums, result, min_diff, i = sorted(nums), float("inf"), float("inf"), 0
        while i < len(nums) - 2:
            if i == 0 or nums[i] != nums[i - 1]:
                j, k = i + 1, len(nums) - 1
                while j < k:
                    diff = nums[i] + nums[j] + nums[k] - target
                    if abs(diff) < min_diff:
                        min_diff = abs(diff)
                        result = nums[i] + nums[j] + nums[k]
                    if diff < 0:
                        j += 1
                    elif diff > 0:
                        k -= 1
                    else:
                        return target
            i += 1
        return result