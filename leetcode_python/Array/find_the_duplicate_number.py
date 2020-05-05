# 287. Find the Duplicate Number
# Medium
#
# Given an array nums containing n + 1 integers where each integer is between 1 and n (inclusive), prove that at least one duplicate number must exist. Assume that there is only one duplicate number, find the duplicate one.
#
# Example 1:
#
# Input: [1,3,4,2,2]
# Output: 2
# Example 2:
#
# Input: [3,1,3,4,2]
# Output: 3
# Note:
#
# You must not modify the array (assume the array is read only).
# You must use only constant, O(1) extra space.
# Your runtime complexity should be less than O(n2).
# There is only one duplicate number in the array, but it could be repeated more than once.
### Note
# The first two approaches mentioned do not satisfy the constraints given in the prompt, but they are solutions that you might be likely to come up with during a technical interview. As an interviewer, I personally would not expect someone to come up with the cycle detection solution unless they have heard it before.

# V0

# V1
# https://leetcode.com/articles/find-the-duplicate-number/
# IDEA : Sorting
class Solution:
    def findDuplicate(self, nums):
        nums.sort()
        for i in range(1, len(nums)):
            if nums[i] == nums[i-1]:
                return nums[i]

# V1'
# https://leetcode.com/articles/find-the-duplicate-number/
# IDEA : SET
class Solution:
    def findDuplicate(self, nums):
        seen = set()
        for num in nums:
            if num in seen:
                return num
            seen.add(num)

# V1''
# https://leetcode.com/articles/find-the-duplicate-number/
# IDEA : Floyd's Tortoise and Hare (Cycle Detection)
# IDEA : 
#   -> TRANSFORM THE PROBLEM INTO "142 Linked List Cycle II"
#   -> SO NOW the problem is to find the entrance of the cycle (cycle linked list)
class Solution:
    def findDuplicate(self, nums):
        # Find the intersection point of the two runners.
        tortoise = hare = nums[0]
        while True:
            tortoise = nums[tortoise]
            hare = nums[nums[hare]]
            if tortoise == hare:
                break
        
        # Find the "entrance" to the cycle.
        tortoise = nums[0]
        while tortoise != hare:
            tortoise = nums[tortoise]
            hare = nums[hare]
        
        return hare

# V1'''
# http://bookshadow.com/weblog/2015/09/28/leetcode-find-duplicate-number/
class Solution(object):
    def findDuplicate(self, nums):
        # The "tortoise and hare" step.  We start at the end of the array and try
        # to find an intersection point in the cycle.
        slow = 0
        fast = 0
    
        # Keep advancing 'slow' by one step and 'fast' by two steps until they
        # meet inside the loop.
        while True:
            slow = nums[slow]
            fast = nums[nums[fast]]
    
            if slow == fast:
                break
    
        # Start up another pointer from the end of the array and march it forward
        # until it hits the pointer inside the array.
        finder = 0
        while True:
            slow   = nums[slow]
            finder = nums[finder]
    
            # If the two hit, the intersection index is the duplicate element.
            if slow == finder:
                return slow

# V1''''
# http://bookshadow.com/weblog/2015/09/28/leetcode-find-duplicate-number/
# IDEA : Binary Search）+ Pigeonhole Principle
class Solution(object):
    def findDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        low, high = 1, len(nums) - 1
        while low <= high:
            mid = (low + high) >> 1
            cnt = sum(x <= mid for x in nums)
            if cnt > mid:
                high = mid - 1
            else:
                low = mid + 1
        return low

# V1'''''
# https://www.hrwhisper.me/leetcode-find-the-duplicate-number/
# IDEA : BINARY SEARCH
class Solution(object):
    def findDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        L, R = 1, len(nums) - 1
        while L <= R:
            mid = (L + R) >> 1
            cnt = sum([1 for num in nums if num <= mid])

            if cnt <= mid:
                L = mid + 1
            else:
                R = mid - 1
        return L

# V1''''''
# https://www.hrwhisper.me/leetcode-find-the-duplicate-number/
# IDEA : TWO POINTERS
class Solution(object):
    def findDuplicate(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        slow , fast = nums[0] , nums[nums[0]]
        while slow != fast:
            slow = nums[slow]
            fast = nums[nums[fast]]

        slow = 0
        while slow != fast:
            slow = nums[slow]
            fast = nums[fast]
        return slow

# V2
