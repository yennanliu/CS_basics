# Time:  O(n)
# Space: O(1)

# Given an integer array, you need to find one continuous subarray that
# if you only sort this subarray in ascending order,
# then the whole array will be sorted in ascending order, too.
#
# You need to find the shortest such subarray and output its length.
#
# Example 1:
# Input: [2, 6, 4, 8, 10, 9, 15]
# Output: 5
# Explanation: You need to sort [6, 4, 8, 10, 9] in ascending order
# to make the whole array sorted in ascending order.
#
# Note:
# Then length of the input array is in range [1, 10,000].
# The input array may contain duplicates, so ascending order here means <=.


# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79254454
# IDEA : COLLECT THE SUB-ARRAY WHICH IS NOT IN ASCENDING ORDER
# AND GET THE LENGTH OF SUCH SUB-ARRAY
# IDEA : DMEO 
# In [9]: nums = [2, 6, 4, 8, 10, 9, 15]
# In [10]: _len, _nums = len(nums), sorted(nums)
# In [11]: _nums
# Out[11]: [2, 4, 6, 8, 9, 10, 15]
# In [12]: l = min([i for i in range(_len) if nums[i] != _nums[i]])
# In [13]: r = max([i for i in range(_len) if nums[i] != _nums[i]])
# In [14]: l
# Out[14]: 1
# In [15]: r
# Out[15]: 5
# In [16]: nums 
# Out[16]: [2, 6, 4, 8, 10, 9, 15]
# In [17]: _nums
# Out[17]: [2, 4, 6, 8, 9, 10, 15]
# In [18]: [i for i in range(_len) if nums[i] != _nums[i]]
# Out[18]: [1, 2, 4, 5]
class Solution(object):
    def findUnsortedSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        _len, _nums = len(nums), sorted(nums)
        if nums == _nums:
            return 0
        l = min([i for i in range(_len) if nums[i] != _nums[i]])
        r = max([i for i in range(_len) if nums[i] != _nums[i]])
        return r - l + 1

# V1'
# http://bookshadow.com/weblog/2017/05/15/leetcode-shortest-unsorted-continuous-subarray/
class Solution(object):
    def findUnsortedSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        snums = sorted(nums)
        s = e = -1
        for i in range(len(nums)):
            if nums[i] != snums[i]:
                if s == -1: s = i
                e = i
        return e - s + 1 if e != s else 0

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findUnsortedSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        left, right = -1, -2
        min_from_right, max_from_left = nums[-1], nums[0]
        for i in range(1, n):
            max_from_left = max(max_from_left, nums[i])
            min_from_right = min(min_from_right, nums[n-1-i])
            if nums[i] < max_from_left: right = i
            if nums[n-1-i] > min_from_right: left = n-1-i


# Time:  O(nlogn)
# Space: O(n)
class Solution2(object):
    def findUnsortedSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        a = sorted(nums) #sort the list
        left, right = 0, len(nums) -1 #define left and right pointer
        while (nums[left] == a[left] or nums[right] == a[right]):
            if right - left <= 1:
                return 0
            if nums[left] == a[left]:
                left += 1
            if nums[right] == a[right]:
                right -= 1
        return right - left + 1
