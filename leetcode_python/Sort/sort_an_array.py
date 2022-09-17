"""

912. Sort an Array
Medium

Given an array of integers nums, sort the array in ascending order and return it.

You must solve the problem without using any built-in functions in O(nlog(n)) time complexity and with the smallest space complexity possible.

 

Example 1:

Input: nums = [5,2,3,1]
Output: [1,2,3,5]
Explanation: After sorting the array, the positions of some numbers are not changed (for example, 2 and 3), while the positions of other numbers are changed (for example, 1 and 5).
Example 2:

Input: nums = [5,1,1,2,0,0]
Output: [0,0,1,1,2,5]
Explanation: Note that the values of nums are not necessairly unique.
 

Constraints:

1 <= nums.length <= 5 * 104
-5 * 104 <= nums[i] <= 5 * 104

"""

# V0

# V1
# IDEA : merge sort
# https://leetcode.com/problems/sort-an-array/discuss/568255/Python-Merge-Sort
class Solution:
    def sortArray(self, nums: List[int]) -> List[int]:
        self.mergeSort(nums)
        return nums
    
    def mergeSort(self, nums: List[int]) -> None:
        if len(nums) > 1:
            mid = len(nums) // 2
            L, R = nums[:mid], nums[mid:]

            self.mergeSort(L)
            self.mergeSort(R)

            i = j = k = 0

            while i < len(L) and j < len(R):
                if L[i] < R[j]:
                    nums[k] = L[i]
                    i += 1
                else:
                    nums[k] = R[j]
                    j += 1
                k += 1

            while i < len(L):
                nums[k] = L[i]
                i += 1
                k += 1

            while j < len(R):
                nums[k] = R[j]
                j += 1
                k += 1

# V1'
# IDEA : quick sort
class Solution(object):
    def sortArray(self, nums):
        self.quickSort(nums, 0, len(nums)-1)
        return nums

    def quickSort(self, nums, start, end):
        if end <= start:
            return
        mid = (start + end) // 2
        nums[start], nums[mid] = nums[mid], nums[start]
        i = start + 1
        j = end
        while i <= j:
            while i <= j and nums[i] <= nums[start]:
                i += 1
            while i <= j and nums[j] >= nums[start]:
                j -= 1
            if i < j:
                nums[i], nums[j] = nums[j], nums[i]        
        nums[start], nums[j] = nums[j], nums[start]
        self.quickSort(nums, start, j-1)
        self.quickSort(nums, j+1, end)

# V2