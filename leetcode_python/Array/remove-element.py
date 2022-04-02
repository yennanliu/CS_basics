"""

27. Remove Element
Easy

Given an integer array nums and an integer val, remove all occurrences of val in nums in-place. The relative order of the elements may be changed.

Since it is impossible to change the length of the array in some languages, you must instead have the result be placed in the first part of the array nums. More formally, if there are k elements after removing the duplicates, then the first k elements of nums should hold the final result. It does not matter what you leave beyond the first k elements.

Return k after placing the final result in the first k slots of nums.

Do not allocate extra space for another array. You must do this by modifying the input array in-place with O(1) extra memory.

Custom Judge:

The judge will test your solution with the following code:

int[] nums = [...]; // Input array
int val = ...; // Value to remove
int[] expectedNums = [...]; // The expected answer with correct length.
                            // It is sorted with no values equaling val.

int k = removeElement(nums, val); // Calls your implementation

assert k == expectedNums.length;
sort(nums, 0, k); // Sort the first k elements of nums
for (int i = 0; i < actualLength; i++) {
    assert nums[i] == expectedNums[i];
}
If all assertions pass, then your solution will be accepted.

 

Example 1:

Input: nums = [3,2,2,3], val = 3
Output: 2, nums = [2,2,_,_]
Explanation: Your function should return k = 2, with the first two elements of nums being 2.
It does not matter what you leave beyond the returned k (hence they are underscores).
Example 2:

Input: nums = [0,1,2,2,3,0,4,2], val = 2
Output: 5, nums = [0,1,4,0,3,_,_,_]
Explanation: Your function should return k = 5, with the first five elements of nums containing 0, 0, 1, 3, and 4.
Note that the five elements can be returned in any order.
It does not matter what you leave beyond the returned k (hence they are underscores).
 

Constraints:

0 <= nums.length <= 100
0 <= nums[i] <= 50
0 <= val <= 100

"""

# V0
# IDEA : TWO POINTER
# https://leetcode.com/problems/remove-element/solution/
class Solution(object):
    def removeElement(self, nums, val):
        i = 0
        for j in range(len(nums)):
            if nums[j] != val:
                nums[i] = nums[j]
                i += 1
        return i

# V0
# IDEA : TWO POINTER
class Solution(object):
    def removeElement(self, nums, val):
        length = 0
        for i in range(len(nums)):
            if nums[i] != val:
                nums[length] = nums[i]
                length += 1
        return length

# V1
# IDEA : array
# https://blog.csdn.net/coder_orz/article/details/51578854
class Solution(object):
    def removeElement(self, nums, val):
        length = 0
        for i in range(len(nums)):
            if nums[i] != val:
                nums[length] = nums[i]
                length += 1
        return length

# test case
s = Solution()
assert s.removeElement([3,2,2,3], 2) == 2
assert s.removeElement([3,2,2,3], 3) == 2
assert s.removeElement([], 3) == 0
assert s.removeElement([1], 3) == 1
assert s.removeElement([3], 3) == 0
assert s.removeElement([_ for _ in range(100)], 3) == 99

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51578854
class Solution(object):
    def removeElement(self, nums, val):
        """
        :type nums: List[int]
        :type val: int
        :rtype: int
        """
        rm_index = []
        for i in range(len(nums)):
            if nums[i] == val:
                rm_index.append(i)
        last = len(nums) - 1
        for i in rm_index:
            while last >= 0 and nums[last] == val:
                last -= 1
            if last < 0:
                break
            nums[i] = nums[last]
            last -= 1
        return len(nums) - len(rm_index)

# V1
# IDEA : 2 POINTERS
# https://leetcode.com/problems/remove-element/solution/
# JAVA
# public int removeElement(int[] nums, int val) {
#     int i = 0;
#     for (int j = 0; j < nums.length; j++) {
#         if (nums[j] != val) {
#             nums[i] = nums[j];
#             i++;
#         }
#     }
#     return i;
# }

# V1
# IDEA : Two Pointers - when elements to remove are rare
# https://leetcode.com/problems/remove-element/solution/
# JAVA
# public int removeElement(int[] nums, int val) {
#     int i = 0;
#     int n = nums.length;
#     while (i < n) {
#         if (nums[i] == val) {
#             nums[i] = nums[n - 1];
#             // reduce array size by one
#             n--;
#         } else {
#             i++;
#         }
#     }
#     return n;
# }

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51578854
class Solution(object):
    def removeElement(self, nums, val):
        length, i = len(nums), 0 
        while i < length:
            if nums[i] == val:
                nums[i] = nums[length - 1]
                length -= 1
            else:
                i += 1
        return length

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param    A       a list of integers
    # @param    elem    an integer, value need to be removed
    # @return an integer
    def removeElement(self, A, elem):
        i, last = 0, len(A) - 1
        while i <= last:
            if A[i] == elem:
                A[i], A[last] = A[last], A[i]
                last -= 1
            else:
                i += 1
        return last + 1