"""
35. Search Insert Position
Easy

Given a sorted array of distinct integers and a target value, return the index if the target is found. If not, return the index where it would be if it were inserted in order.

You must write an algorithm with O(log n) runtime complexity.

 

Example 1:

Input: nums = [1,3,5,6], target = 5
Output: 2
Example 2:

Input: nums = [1,3,5,6], target = 2
Output: 1
Example 3:

Input: nums = [1,3,5,6], target = 7
Output: 4
Example 4:

Input: nums = [1,3,5,6], target = 0
Output: 0
Example 5:

Input: nums = [1], target = 0
Output: 0
 

Constraints:

1 <= nums.length <= 104
-104 <= nums[i] <= 104
nums contains distinct values sorted in ascending order.
-104 <= target <= 104

"""

# V0
# IDEA : BINARY SEARCH + OTHER CASES HANDLING
class Solution(object):
    def searchInsert(self, nums, target):
        l = 0
        r = len(nums) -1
        mid = 0
        ### NOTE : in normal BINARY SEARCH,  we set while condition as r >= l
        while r >= l:
            mid = l + (r-l) // 2
            if nums[mid] == target:
                return mid
            elif nums[mid] > target:
                r = mid -1 
            elif nums[mid] < target:
                l = mid + 1
        
        # init result
        result = 0

        ### NOTE : other cases handling here, 2 cases
        # case 1 :  r < t < l
        # case 2 :  t < r < l
        if r < l:
            # case 1 :  r < t < l
            if nums[r] < target:
                result =  r + 1
            # case 2 :  t < r < l
            elif target < nums[r]:
                result = r - 1
        # edge case : if result < 0, we result 0         
        return result if result > 0 else 0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/70738108
# via python intrinsic func 
class Solution(object):
    def searchInsert(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        return bisect.bisect_left(nums, target)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/70738108
class Solution(object):
    def searchInsert(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        N = len(nums)
        left, right = 0, N #[left, right)
        while left < right:
            mid = left + (right - left) / 2
            if nums[mid] == target:
                return mid
            elif nums[mid] > target:
                right = mid
            else:
                left = mid + 1
        return left

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def searchInsert(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = left + (right - left) / 2
            if nums[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1

        return left
