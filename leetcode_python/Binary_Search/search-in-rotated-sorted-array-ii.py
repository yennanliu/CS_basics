"""

81. Search in Rotated Sorted Array II
Medium

There is an integer array nums sorted in non-decreasing order (not necessarily with distinct values).

Before being passed to your function, nums is rotated at an unknown pivot index k (0 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,4,4,5,6,6,7] might be rotated at pivot index 5 and become [4,5,6,6,7,0,1,2,4,4].

Given the array nums after the rotation and an integer target, return true if target is in nums, or false if it is not in nums.

You must decrease the overall operation steps as much as possible.

 

Example 1:

Input: nums = [2,5,6,0,0,1,2], target = 0
Output: true
Example 2:

Input: nums = [2,5,6,0,0,1,2], target = 3
Output: false
 

Constraints:

1 <= nums.length <= 5000
-104 <= nums[i] <= 104
nums is guaranteed to be rotated at some pivot.
-104 <= target <= 104
 

Follow up: This problem is similar to Search in Rotated Sorted Array, but nums may contain duplicates. Would this affect the runtime complexity? How and why?

"""

# V0
# IDEA : LC 033 (binary search) + set
class Solution(object):
    def search(self, nums, target):
        """
        NOTE !!! 
            since there could be duplicated val in nums,
            but we ONLY want to know if target in nums or not
            so we can use set deduplicate nums, and use the
            same algorithm as LC 33
        """
        nums = list(set(nums))
        if not nums:
            return -1
        left, right = 0, len(nums) - 1
        while left <= right:
            mid = (left + right) // 2
            if nums[mid] == target:
                return True
            #---------------------------------------------
            # Case 1 :  nums[mid:right] is ordering
            #---------------------------------------------
            # all we need to do is : 1) check if target is within mid - right, and move the left or right pointer
            if nums[mid] < nums[right]:
                # mind NOT use (" nums[mid] < target <= nums[right]")
                # mind the "<="
                if target > nums[mid] and target <= nums[right]: # check the relationship with target, which is different from the default binary search
                    left = mid + 1
                else:
                    right = mid - 1
            #---------------------------------------------
            # Case 2 :  nums[left:mid] is ordering
            #---------------------------------------------
            # all we need to do is : 1) check if target is within left - mid, and move the left or right pointer
            else:
                # # mind NOT use (" nums[left] <= target < nums[mid]")
                # mind the "<="
                if target < nums[mid] and target >= nums[left]:  # check the relationship with target, which is different from the default binary search
                    right = mid - 1
                else:
                    left = mid + 1
        return False

# V0'
# IDEA : 
# STEP 0) NEGLECT THE "DUPLICATES" : "while l < r and nums[l] == nums[r]: l += 1"
# STEP 1) CHECK WHETHER "LEFT SUB-STRING" OR "RIGHT SUB-STRING" IS ORDERING 
#         -> IF THE SUB-STRING IS ORDERING => IT'S IS ASCENDING ORDERING  => SO WE CAN USE THE ORIGINAL "BINRARY SEARCH" FOR TARGET SEARCH
# STEP 2) CHECK THE RELATIONS : nums[l] <= target < nums[mid], nums[mid] < target <= nums[r]
#         -> THEN MODIFY l, r, with mid by cases
# STEP 3) LOOP STEP 0) -> STEP 3), if nums[m] == target, return it, if not then return False at the end.
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
            mid = l + (r - l) // 2
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
            mid = l + (r - l) // 2
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
# https://blog.csdn.net/happyaaaaaaaaaaa/article/details/51602234
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: bool
        """
        left, right = 0, len(nums) - 1
        while left <= right :
            mid = (left+right) // 2
            if nums[mid] == target : return True
            if nums[mid] == nums[left] : left += 1
            elif nums[mid] > nums[left] :
                if nums[mid] > target and nums[left] <= target :
                    right = mid - 1
                else : left = mid + 1
            else :
                if nums[mid] < target and nums[right] >= target :
                    left = mid + 1
                else : right = mid - 1
        return False

# V1''
# https://www.jiuzhang.com/solution/search-in-rotated-sorted-array-ii/#tag-highlight-lang-python
class Solution(object):
    def search(self, A, target):
        # write your code here
        for num in A:
            if num == target:
                return True
        return False

# V1
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/search-in-rotated-sorted-array-ii/solution/
# JAVA
# class Solution {
#     public boolean search(int[] nums, int target) {
#         int n = nums.length;
#         if (n == 0) return false;
#         int end = n - 1;
#         int start = 0;
#
#         while (start <= end) {
#             int mid = start + (end - start) / 2;
#
#             if (nums[mid] == target) {
#                 return true;
#             }
#
#             if (!isBinarySearchHelpful(nums, start, nums[mid])) {
#                 start++;
#                 continue;
#             }
#             // which array does pivot belong to.
#             boolean pivotArray = existsInFirst(nums, start, nums[mid]);
#
#             // which array does target belong to.
#             boolean targetArray = existsInFirst(nums, start, target);
#             if (pivotArray ^ targetArray) { // If pivot and target exist in different sorted arrays, recall that xor is true when both operands are distinct
#                 if (pivotArray) {
#                     start = mid + 1; // pivot in the first, target in the second
#                 } else {
#                     end = mid - 1; // target in the first, pivot in the second
#                 }
#             } else { // If pivot and target exist in same sorted array
#                 if (nums[mid] < target) {
#                     start = mid + 1;
#                 } else {
#                     end = mid - 1;
#                 }
#             }
#         }
#         return false;
#     }
#
#     // returns true if we can reduce the search space in current binary search space
#     private boolean isBinarySearchHelpful(int[] arr, int start, int element) {
#         return arr[start] != element;
#     }
#
#     // returns true if element exists in first array, false if it exists in second
#     private boolean existsInFirst(int[] arr, int start, int element) {
#         return arr[start] <= element;
#     }
# }

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
            mid = left + (right - left) // 2

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