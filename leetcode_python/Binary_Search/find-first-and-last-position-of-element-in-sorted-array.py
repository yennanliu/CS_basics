# V0

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83273084
# IDEA : BINARY SEARCH 
class Solution(object):
    def searchRange(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        left = self.lowwer_bound(nums, target)
        right = self.higher_bound(nums, target)
        if left == right:
            return [-1, -1]
        return [left, right - 1]
    
    def lowwer_bound(self, nums, target):
        # find in range [left, right)
        left, right = 0, len(nums)
        while left < right:
            mid = left + (right - left) / 2
            if nums[mid] < target:
                left = mid + 1
            else:
                right = mid
        return left
    
    def higher_bound(self, nums, target):
        # find in range [left, right)
        left, right = 0, len(nums)
        while left < right:
            mid = left + (right - left) / 2
            if nums[mid] <= target:
                left = mid + 1
            else:
                right = mid
        return left

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83273084
class Solution(object):
    def searchRange(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        left = bisect.bisect_left(nums, target)
        right = bisect.bisect_right(nums, target)
        if left == right:
            return [-1, -1]
        return [left, right - 1]

# V1''
# https://www.jiuzhang.com/solution/find-first-and-last-position-of-element-in-sorted-array/#tag-other-lang-python
class Solution:
    def searchRange(self, nums, target):
        if not nums:
            return [-1, -1]
        
        left, right = -1, -1
        start, end = 0, len(nums) - 1 
        while start + 1 < end:
            mid = (start + end) // 2 
            if nums[mid] <= target:
                start = mid
            else:
                end = mid
        if nums[start] == target:
            right = start
        if nums[end] == target:
            right = end
        
        start, end = 0, len(nums) - 1 
        while start + 1 < end:
            mid = (start + end) // 2 
            if nums[mid] >= target:
                end = mid
            else:
                start = mid
        if nums[end] == target:
            left = end
        if nums[start] == target:
            left = start
        if left == -1 and right == -1:
            return [-1, -1]
        return [left, right]