"""
75. Sort Colors
Medium

6543

338

Add to List

Share
Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white, and blue.

We will use the integers 0, 1, and 2 to represent the color red, white, and blue, respectively.

You must solve this problem without using the library's sort function.

 

Example 1:

Input: nums = [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
Example 2:

Input: nums = [2,0,1]
Output: [0,1,2]
Example 3:

Input: nums = [0]
Output: [0]
Example 4:

Input: nums = [1]
Output: [1]
 

Constraints:

n == nums.length
1 <= n <= 300
nums[i] is 0, 1, or 2.
 

Follow up: Could you come up with a one-pass algorithm using only constant extra space?

"""

# V0
# NOTE : JUST IMPLEMENT WHATEVER SORTING ALGORITHM IN THIS PROBLEM
class Solution:
    def sortColors(self, A):
        left, index, right = 0, 0, len(A) - 1

        # be careful, index < right is not correct
        while index <= right:
            if A[index] == 0:
                A[left], A[index] = A[index], A[left]
                left += 1
                index += 1
            elif A[index] == 2:
                A[right], A[index] = A[index], A[right]
                right -=  1 
            else:
                index += 1
        return A 

# V0'
# IDEA : BUBBLE SORT
class Solution:
    def sortColors(self, A):
        if not A or len(A) == 1:
            return A
        for i in range(len(A)):
            for j in range(i+1, len(A)):
                if A[i] > A[j]:
                    A[i], A[j] = A[j], A[i]
        return A

# V0'' (may not validated)
class Solution:
    def sortColors(self, A):
        return A.sort()

# V1
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

# V1'
# https://www.jiuzhang.com/solution/sort-colors/#tag-highlight-lang-python
class Solution:
    """
    @param nums: A list of integer which is 0, 1 or 2 
    @return: nothing
    """
    def sortColors(self, A):
        left, index, right = 0, 0, len(A) - 1

        # be careful, index < right is not correct
        while index <= right:
            if A[index] == 0:
                A[left], A[index] = A[index], A[left]
                left += 1
                index += 1
            elif A[index] == 1:
                index += 1
            else:
                A[right], A[index] = A[index], A[right]
                right -= 1

# V1''
# https://www.jiuzhang.com/solution/sort-colors/#tag-highlight-lang-python
class Solution:
    """
    @param nums: A list of integer which is 0, 1 or 2 
    @return: nothing
    """
    def sortColors(self, A):
        index = self.sort(A, 0, 0)
        self.sort(A, 1, index)
        
    def sort(self, A, flag, index):
        start, end = index, len(A) - 1
        while start <= end:
            while start <= end and A[start] == flag:
                start += 1
            while start <= end and A[end] != flag:
                end -= 1
            if start <= end:
                A[start], A[end] = A[end], A[start]
                start += 1
                end -= 1
        return start

# V1'' 
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
 
# V2 
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