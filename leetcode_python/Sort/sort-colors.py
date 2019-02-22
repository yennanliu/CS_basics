

# V1 : dev 

# class Solution(object):
#     def sortColors(self, nums):
#         return [0] * nums.count(0) + [1] * nums.count(1) + [2] * nums.count(2)
    


# V2 
# https://blog.csdn.net/maymay_/article/details/80093460
class Solution(object):
    def sortColors(self, nums):
        if nums == []:
            return 
        i,l,r = 0,0,len(nums)-1    # define two pointers : l, r. and index 
        while i <= r :
            if nums[i] == 2:       # if the value of current index = 2 
                nums[i],nums[r] = nums[r],nums[i]   # then switch the index with the one next to it (right) 
                r -=1                # then move right point to left 
            elif nums[i] == 0:  # if current value of index = 0 
                nums[i],nums[l] = nums[l],nums[i]  # then switch the index with the one next to it (left)  
                l +=1
                i +=1         # then move left point  and index to right 
            else :
                i+=1       # if current value of index = 1, then move index to right, keep left point unchanged  


# V3 
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

    
# V4 
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