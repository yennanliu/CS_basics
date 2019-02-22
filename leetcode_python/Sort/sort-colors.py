

# V1 

class Solution(object):
    def sortColors(self, nums):
        return [0] * nums.count(0) + [1] * nums.count(1) + [2] * nums.count(2)
    


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79392195
from collections import Counter
class Solution(object):
    def sortColors(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        count = Counter(nums)
        for i in range(len(nums)):
            if i < count[0]:
                nums[i] = 0
            elif i < count[0] + count[1]:
                nums[i] = 1
            else:
                nums[i] = 2


    
# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def sortColors(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        def triPartition(nums, target):
            i, j, n = 0, 0, len(nums) - 1

            while j <= n:
                if nums[j] < target:
                    nums[i], nums[j] = nums[j], nums[i]
                    i += 1
                    j += 1
                elif nums[j] > target:
                    nums[j], nums[n] = nums[n], nums[j]
                    n -= 1
                else:
                    j += 1

        triPartition(nums, 1)