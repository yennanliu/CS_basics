"""

162. Find Peak Element
Medium

A peak element is an element that is strictly greater than its neighbors.

Given an integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.

You may imagine that nums[-1] = nums[n] = -∞.

You must write an algorithm that runs in O(log n) time.

 

Example 1:

Input: nums = [1,2,3,1]
Output: 2
Explanation: 3 is a peak element and your function should return the index number 2.
Example 2:

Input: nums = [1,2,1,3,5,6,4]
Output: 5
Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
 

Constraints:

1 <= nums.length <= 1000
-231 <= nums[i] <= 231 - 1
nums[i] != nums[i + 1] for all valid i.

"""

# V0
# IDEA :  Linear Scan + problem understanding
class Solution(object):
    def findPeakElement(self, nums):
        for i in range(len(nums)-1):
            if nums[i] > nums[i+1]:
                return i
        return len(nums) - 1

# V0'
# IDEA : RECURSIVE BINARY SEARCH
class Solution(object):
    def findPeakElement(self, nums):

        def help(nums, l, r):
            if l == r:
                return l
            mid = l + (r - l) // 2
            if (nums[mid] > nums[mid+1]):
                return help(nums, l, mid)
            return help(nums, mid+1, r)
            
        return help(nums, 0, len(nums)-1)

# V1
# https://leetcode.com/problems/find-peak-element/solution/
# IDEA :  Linear Scan + problem understanding
class Solution(object):
    def findPeakElement(self, nums):
        for i in range(len(nums)-1):
            if nums[i] > nums[i+1]:
                return i
        return len(nums) - 1

# V1'
# IDEA : RECURSIVE BINARY SEARCH
# https://leetcode.com/problems/find-peak-element/solution/
class Solution(object):
    def findPeakElement(self, nums):

        def help(nums, l, r):
            if l == r:
                return l
            mid = l + (r - l) // 2
            if (nums[mid] > nums[mid+1]):
                return help(nums, l, mid)
            return help(nums, mid+1, r)

        return help(nums, 0, len(nums)-1)

# V1 
# https://blog.csdn.net/aliceyangxi1987/article/details/50484982
class Solution(object):
    def findPeakElement(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return self.helpsearch(nums,0,len(nums)-1)
    
    def helpsearch(self,nums,start,end):
        
        if start==end:
            return start
        if end-start==1:
            return [end,start][nums[start]>nums[end]]       
        mid=(start+end)/2
        if nums[mid]<nums[mid-1]:
            return self.helpsearch(nums,start,mid-1)
        if nums[mid]<nums[mid+1]:
            return self.helpsearch(nums,mid+1,end)
        return mid

# V1'
# https://www.jiuzhang.com/solution/find-peak-element/#tag-highlight-lang-python
class Solution:
    #@param A: An integers list.
    #@return: return any of peek positions.
    def findPeak(self, A):
        # write your code here
        start, end = 1, len(A) - 2
        while start + 1 <  end:
            mid = (start + end) // 2
            if A[mid] < A[mid - 1]:
                end = mid
            elif A[mid] < A[mid + 1]:
                start = mid
            else:
                end = mid
        if A[start] < A[end]:
            return end
        else:
            return start

# V2 
# Time:  O(logn)
# Space: O(1)
class Solution(object):
    def findPeakElement(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left, right = 0, len(nums) - 1

        while left < right:
            mid = left + (right - left) / 2
            if nums[mid] > nums[mid + 1]:
                right = mid
            else:
                left = mid + 1
        return left
