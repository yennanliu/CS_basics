"""

26. Remove Duplicates from Sorted Array
Easy

Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such that each unique element appears only once. The relative order of the elements should be kept the same.

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

Input: nums = [1,1,2]
Output: 2, nums = [1,2,_]
Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
Example 2:

Input: nums = [0,0,1,1,1,2,2,3,3,4]
Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
Explanation: Your function should return k = 5, with the first five elements of nums being 0, 1, 2, 3, and 4 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
 

Constraints:

1 <= nums.length <= 3 * 104
-100 <= nums[i] <= 100
nums is sorted in non-decreasing order.

"""

# V0
# IDEA : 2 POINTERS: i, j
class Solution(object):
    def removeDuplicates(self, nums):
        # edge case
        if not nums:
            return
        i = 0
        for j in range(1, len(nums)):
            if nums[j] != nums[i]:
                nums[i+1], nums[j] = nums[j], nums[i+1]
                i += 1

        #print ("nums = " + str(nums))
        return i+1

# V0'
# IDEA : 2 POINTERS
# HAVE A POINTER j STARTS FROM 0 AND THE OTHER POINTER i GO THROUGH nums
#  -> IF A[i] != A[j]
#     -> THEN SWITCH A[i] AND A[j+1]
#     -> and j += 1
# *** NOTE : it's swith A[j+1] (j+1) with A[i]
# DEMO 1 
# A = [1,1,1,2,3]
# s = Solution()
# s.removeDuplicates(A)
# [1, 1, 1, 2, 3]
# [1, 1, 1, 2, 3]
# [1, 1, 1, 2, 3]
# [1, 2, 1, 1, 3]
# [1, 2, 3, 1, 1]
#
# DEMO 2
# A = [1,2,2,3,4]
# s = Solution()
# s.removeDuplicates(A)
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 2, 3, 4]
# A = [1, 2, 3, 2, 4]
# -> A = [1, 2, 3, 4, 2]
class Solution:
    def removeDuplicates(self, A):
        if len(A) == 0:
            return 0
        j = 0
        for i in range(0, len(A)):
            ###  NOTE : below condition
            if A[i] != A[j]:
                A[i], A[j+1] = A[j+1], A[i]
                j = j + 1
        return j+1

# V1 
# https://www.cnblogs.com/zuoyuan/p/3779816.html
# IDEA : TWO POINTER 
# IDEA : use an index j, when i go through the array,
# if A[i] != A[j], then swap A[i] and A[j+1] and do j=j+1 (A[i], A[j+1] = A[j+1], A[i])
# then i keep going through the array 
class Solution:
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if len(A) == 0:
            return 0
        j = 0
        for i in range(0, len(A)):
            if A[i] != A[j]:
                A[i], A[j+1] = A[j+1], A[i]
                j = j + 1
        return j+1

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51589013
# IDEA : TWO POINTER 
class Solution(object):
    def removeDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums)==0:
            return 0
        cur = 0
        for i in range(1, len(nums)):
            if nums[i] != nums[cur]:
                cur += 1
                nums[cur] = nums[i]
        return cur+1

# V1''
# IDEA : 2 POINTERS + WHILE
# https://leetcode.com/problems/remove-duplicates-from-sorted-array/discuss/145847/Python-
class Solution:
    def removeDuplicates(self, nums):
        if not nums:
            return 
        slow = fast = 0
        while fast <= len(nums) - 1:
            if nums[fast] != nums[slow]:
                nums[slow+1] = nums[fast]
                slow += 1
            fast += 1
        return slow + 1

# V1'''
# https://www.jiuzhang.com/solution/remove-duplicates-from-sorted-array/#tag-highlight-lang-python
class Solution:
    """
    @param A: a list of integers
    @return an integer
    """
    def removeDuplicates(self, A):
        # write your code here
        if A == []:
            return 0
        index = 0
        for i in range(1, len(A)):
            if A[index] != A[i]:
                index += 1
                A[index] = A[i]
        return index + 1

# V1'''
# IDEA : 2 POINTERS
# JAVA
# https://leetcode.com/problems/remove-duplicates-from-sorted-array/solution/
# public int removeDuplicates(int[] nums) {
#     if (nums.length == 0) return 0;
#     int i = 0;
#     for (int j = 1; j < nums.length; j++) {
#         if (nums[j] != nums[i]) {
#             i++;
#             nums[i] = nums[j];
#         }
#     }
#     return i + 1;
# }

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if not A:
            return 0

        last = 0
        for i in range(len(A)):
            if A[last] != A[i]:
                last += 1
                A[last] = A[i]
        return last + 1