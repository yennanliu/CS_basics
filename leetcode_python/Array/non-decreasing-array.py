"""

665. Non-decreasing Array
Medium

Given an array nums with n integers, your task is to check if it could become non-decreasing by modifying at most one element.

We define an array is non-decreasing if nums[i] <= nums[i + 1] holds for every i (0-based) such that (0 <= i <= n - 2).

Example 1:

Input: nums = [4,2,3]
Output: true
Explanation: You could modify the first 4 to 1 to get a non-decreasing array.

Example 2:

Input: nums = [4,2,1]
Output: false
Explanation: You cannot get a non-decreasing array by modifying at most one element.

Constraints:

n == nums.length
1 <= n <= 10^4
-10^5 <= nums[i] <= 10^5

"""

# V0 

# V1
# http://bookshadow.com/weblog/2017/08/30/leetcode-non-decreasing-array/
# time = O(n)
# space = O(1)
class Solution(object):
    def checkPossibility(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        chance = 1
        for x in range(len(nums)):
            if x and nums[x] < nums[x - 1]:
                if not chance:
                    return False
                chance -= 1
                if x > 1 and nums[x] <= nums[x - 2]:
                    nums[x] = nums[x - 1]
        return True

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79264475
# time = O(n)
# space = O(n)
class Solution(object):
    def checkPossibility(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        isIncrease = lambda nums: all(nums[i] <= nums[i + 1] for i in range(len(nums) - 1))
        one, two = nums[:], nums[:]
        for i in range(0, len(nums) - 1):
            if nums[i + 1] < nums[i]:
                one[i] = one[i + 1]
                two[i + 1] = two[i]
                break
        return isIncrease(one) or isIncrease(two)
            
# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def checkPossibility(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        modified, prev = False, nums[0]
        for i in range(1, len(nums)):
            if prev > nums[i]:
                if modified:
                    return False
                if i-2 < 0 or nums[i-2] <= nums[i]:
                    prev = nums[i]    # nums[i-1] = nums[i], prev = nums[i]
#               else:
#                   prev = nums[i-1]  # nums[i] = nums[i-1], prev = nums[i]
                modified = True
            else:
                prev = nums[i]
        return True
