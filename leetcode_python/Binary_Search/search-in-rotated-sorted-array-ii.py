# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83214278
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: bool
        """
        N = len(nums)
        l, r = 0, N - 1
        while l <= r:
            while l < r and nums[l] == nums[r]:
                l += 1
            mid = l + (r - l) / 2
            if nums[mid] == target:
                return True
            if nums[mid] >= nums[l]:
                if nums[l] <= target < nums[mid]:
                    r = mid - 1
                else:
                    l = mid + 1
            elif nums[mid] <= nums[r]:
                if nums[mid] < target <= nums[r]:
                    l = mid + 1
                else:
                    r = mid - 1
        return False

# V1'
# https://www.jiuzhang.com/solution/search-in-rotated-sorted-array-ii/#tag-highlight-lang-python
class Solution(object):
    def search(self, A, target):
        # write your code here
        for num in A:
            if num == target:
                return True
        return False
        
# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        left, right = 0, len(nums) - 1

        while left <= right:
            mid = left + (right - left) / 2

            if nums[mid] == target:
                return True
            elif nums[mid] == nums[left]:
                left += 1
            elif (nums[mid] > nums[left] and nums[left] <= target < nums[mid]) or \
                 (nums[mid] < nums[left] and not (nums[mid] < target <= nums[right])):
                right = mid - 1
            else:
                left = mid + 1

        return False