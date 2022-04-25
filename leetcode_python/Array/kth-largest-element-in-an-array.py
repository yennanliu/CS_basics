"""

215. Kth Largest Element in an Array
Medium

Given an integer array nums and an integer k, return the kth largest element in the array.

Note that it is the kth largest element in the sorted order, not the kth distinct element.

Example 1:

Input: nums = [3,2,1,5,6,4], k = 2
Output: 5

Example 2:

Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
Output: 4
 

Constraints:

1 <= k <= nums.length <= 104
-104 <= nums[i] <= 104

"""

# V0
# IDEA : SORTED 
class Solution:
    def findKthLargest(self, nums):
        # edge case
        if not nums:
            return
        nums.sort(key=lambda x : -x)
        print ("nums = " + str(nums))
        return nums[k-1]

# V0'
# IDEA : SORTED 
class Solution:
    def findKthLargest(self, nums, k):
        nums.sort()
        return nums[-k]

# V0''
# IDEA : bubble sort -> OK*** (reiew again!)
class Solution:
    def findKthLargest(self, nums, k):
        for i in range(k):
            ### NOTE THIS : range(len(nums) - i - 1)
            # same as bubble sort
            # https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/bubble_sort.py#L13
            for j in range(len(nums) - i - 1):
                if nums[j] > nums[j + 1]:
                    nums[j], nums[j + 1] = nums[j + 1], nums[j]
        return nums[len(nums) - k]
# class Solution:
#     def findKthLargest(self, nums, k):
#         # bubble sort
#         _len = len(nums)
#         for i in range(k):
#             for j in range(_len-1):
#                 if nums[j] > nums[j+1]:
#                     nums[j], nums[j+1] = nums[j+1], nums[j]
#         return nums[-k]

# V0'''
# IDEA : quick sort -> check again
import random
class Solution:
    def findKthLargest(self, nums, k):
        pivot = random.choice(nums)
        nums1, nums2 = [], []
        for num in nums:
            if num > pivot:
                nums1.append(num)
            elif num < pivot:
                nums2.append(num)
        if k <= len(nums1):
            return self.findKthLargest(nums1, k)
        if k > len(nums) - len(nums2):
            return self.findKthLargest(nums2, k - (len(nums) - len(nums2)))
        return pivot

# V1 
# http://bookshadow.com/weblog/2015/05/23/leetcode-kth-largest-element-array/
# IDEA : SORTED 
class Solution:
    def findKthLargest(self, nums, k):
        return sorted(nums, reverse=True)[k - 1]

# V1'  
# http://bookshadow.com/weblog/2015/05/23/leetcode-kth-largest-element-array/
# IDEA : QUICK SORT
import random
class Solution:
    # @param {integer[]} nums
    # @param {integer} k
    # @return {integer}
    def findKthLargest(self, nums, k):
        pivot = random.choice(nums)
        nums1, nums2 = [], []
        for num in nums:
            if num > pivot:
                nums1.append(num)
            elif num < pivot:
                nums2.append(num)
        if k <= len(nums1):
            return self.findKthLargest(nums1, k)
        if k > len(nums) - len(nums2):
            return self.findKthLargest(nums2, k - (len(nums) - len(nums2)))
        return pivot

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79264797
# IDEA : REMOVE MAX 
class Solution(object):
    def findKthLargest(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        for i in range(k - 1):
            nums.remove(max(nums))
        return max(nums)
        
# V2 
# Time:  O(n) ~ O(n^2)
# Space: O(1)
from random import randint
class Solution(object):
    # @param {integer[]} nums
    # @param {integer} k
    # @return {integer}
    def findKthLargest(self, nums, k):
        left, right = 0, len(nums) - 1
        while left <= right:
            pivot_idx = randint(left, right)
            new_pivot_idx = self.PartitionAroundPivot(left, right, pivot_idx, nums)
            if new_pivot_idx == k - 1:
                return nums[new_pivot_idx]
            elif new_pivot_idx > k - 1:
                right = new_pivot_idx - 1
            else:  # new_pivot_idx < k - 1.
                left = new_pivot_idx + 1

    def PartitionAroundPivot(self, left, right, pivot_idx, nums):
        pivot_value = nums[pivot_idx]
        new_pivot_idx = left
        nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
        for i in range(left, right):
            if nums[i] > pivot_value:
                nums[i], nums[new_pivot_idx] = nums[new_pivot_idx], nums[i]
                new_pivot_idx += 1

        nums[right], nums[new_pivot_idx] = nums[new_pivot_idx], nums[right]
        return new_pivot_idx