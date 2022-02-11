"""

Given an integer array nums sorted in non-decreasing order, remove some duplicates in-place such that each unique element appears at most twice. The relative order of the elements should be kept the same.

Since it is impossible to change the length of the array in some languages, you must instead have the result be placed in the first part of the array nums. More formally, if there are k elements after removing the duplicates, then the first k elements of nums should hold the final result. It does not matter what you leave beyond the first k elements.

Return k after placing the final result in the first k slots of nums.

Do not allocate extra space for another array. You must do this by modifying the input array in-place with O(1) extra memory.

Custom Judge:

The judge will test your solution with the following code:

int[] nums = [...]; // Input array
int[] expectedNums = [...]; // The expected answer with correct length

int k = removeDuplicates(nums); // Calls your implementation

assert k == expectedNums.length;
for (int i = 0; i < k; i++) {
    assert nums[i] == expectedNums[i];
}
If all assertions pass, then your solution will be accepted.

 

Example 1:

Input: nums = [1,1,1,2,2,3]
Output: 5, nums = [1,1,2,2,3,_]
Explanation: Your function should return k = 5, with the first five elements of nums being 1, 1, 2, 2 and 3 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
Example 2:

Input: nums = [0,0,1,1,1,1,2,3,3]
Output: 7, nums = [0,0,1,1,2,3,3,_,_]
Explanation: Your function should return k = 7, with the first seven elements of nums being 0, 0, 1, 1, 2, 3 and 3 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
 

Constraints:

1 <= nums.length <= 3 * 104
-104 <= nums[i] <= 104
nums is sorted in non-decreasing order.

"""

# V0
# IDEA : 2 POINTERS
#### NOTE : THE nums already ordering
# DEMO
# example 1
# nums = [1,1,1,2,2,3]
#           i j
#           i   j
#        [1,1,2,1,2,3]
#             i   j
#             i     j
#        [1,1,2,2,1,3]
#               i   j       
class Solution:
    def removeDuplicates(self, nums):
        if len(nums) < 3:
            return len(nums)

        ### NOTE : slow starts from 1
        slow = 1
        ### NOTE : fast starts from 2
        for fast in range(2, len(nums)):
            """
            NOTE : BELOW CONDITION

            1) nums[slow] != nums[fast]: for adding "1st" element
            2) nums[slow] != nums[slow-1] : for adding "2nd" element
            """
            if nums[slow] != nums[fast] or nums[slow] != nums[slow-1]:
                # both of below op are OK
                #nums[slow+1] = nums[fast]
                nums[slow+1], nums[fast] = nums[fast], nums[slow+1] 
                slow += 1
        return slow+1

# V0'
# IDEA : 2 POINTERS
class Solution:
    def removeDuplicates(self, nums: List[int]) -> int:
        if len(nums) < 3:
            return len(nums)

        slow = 1
        for fast in range(2, len(nums)):
            if nums[slow] != nums[fast] or nums[slow] != nums[slow-1]:
                nums[slow+1], nums[fast] = nums[fast], nums[slow+1]
                slow += 1
        return slow+1

# V0''
# IDEA : TWO POINTER
# TO NOTE : have to the list IN PLACE
# DEMO
# nums = [1,1,1,2,2,3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 2, 2, 2, 3]
# [1, 1, 2, 2, 2, 3]
# 5
class Solution(object):
    def removeDuplicates(self, nums):
        i = 0
        for n in nums:
            if i < 2 or n != nums[i - 2]: # i < 2 : for dealing with i < 2 cases (i.e. i=0, i=1, i=2)
                # modify the list IN PLACE
                nums[i] = n
                i += 1
        return i

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82829709
# IDEA : TWO POINTER
class Solution(object):
    def removeDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        i = 0
        for n in nums:
            if i < 2 or n != nums[i - 2]: # i < 2 : for dealing with i < 2 cases (i.e. i=0, i=1, i=2)
                nums[i] = n
                i += 1
        return i

# V1'
# https://leetcode.com/problems/remove-duplicates-from-sorted-array-ii/discuss/829476/Python-solution
class Solution:
    def removeDuplicates(self, nums):
        if len(nums) < 3:
            return len(nums)

        slow = 1
        for fast in range(2, len(nums)):
            if nums[slow] != nums[fast] or nums[slow] != nums[slow-1]:
                nums[slow+1] = nums[fast]
                slow += 1

        return slow+1

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/82829709
# NEET TO DOUBLE CHECK / FIX 
# class Solution(object):
#     def removeDuplicates(self, nums):
#         """
#         :type nums: List[int]
#         :rtype: int
#         """
#         N = len(nums)
#         if N <= 1: return N
#         left, right = 0, 1
#         while right < N:
#             while right < N and nums[right] == nums[left]:
#                 right += 1
#             left += 1
#             if right < N:
#                 nums[left] = nums[right]
#         return left

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if not A:
            return 0
        last, i, same = 0, 1, False
        while i < len(A):
            if A[last] != A[i] or not same:
                same = A[last] == A[i]
                last += 1
                A[last] = A[i]
            i += 1
        return last + 1