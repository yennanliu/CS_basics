"""

287. Find the Duplicate Number
Medium

Given an array of integers nums containing n + 1 integers where each integer is in the range [1, n] inclusive.

There is only one repeated number in nums, return this repeated number.

You must solve the problem without modifying the array nums and uses only constant extra space.

 

Example 1:

Input: nums = [1,3,4,2,2]
Output: 2
Example 2:

Input: nums = [3,1,3,4,2]
Output: 3
 

Constraints:

1 <= n <= 105
nums.length == n + 1
1 <= nums[i] <= n
All the integers in nums appear only once except for precisely one integer which appears two or more times.
 

Follow up:

How can we prove that at least one duplicate number must exist in nums?
Can you solve the problem in linear runtime complexity?

"""

# V0
# IDEA : Counter
from collections import Counter
class Solution(object):
    def findDuplicate(self, nums):
        # edge case
        if not nums:
            return
        cnt = Counter(nums)
        for c in cnt:
            if cnt[c] > 1:
                return c

# V0'
# IDEA : Counter
from collections import Counter
class Solution(object):
    def findDuplicates(self, nums):
        return [elem for elem, count in Counter(nums).items() if count == 2]

# V1
# http://bookshadow.com/weblog/2016/10/25/leetcode-find-all-duplicates-in-an-array/
# https://blog.csdn.net/fuxuemingzhu/article/details/79275549
# IDEA : FIND ALL ELEMENTS EXIST EXACTLY 2 TIMES IN THE GIVEN ARRAY 
class Solution(object):
    def findDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        ans = []
        for n in nums:
            if nums[abs(n) - 1] < 0:
                ans.append(abs(n))
            else:
                nums[abs(n) - 1] *= -1
        return ans

# V1'
# http://bookshadow.com/weblog/2016/10/25/leetcode-find-all-duplicates-in-an-array/
# https://blog.csdn.net/fuxuemingzhu/article/details/79275549
class Solution(object):
    def findDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        ans = []
        for i in range(len(nums)):
            while nums[i] and nums[i] != i + 1:
                n = nums[i]
                if nums[i] == nums[n - 1]:
                    ans.append(n)
                    nums[i] = 0
                else:
                    nums[i], nums[n - 1] = nums[n - 1], nums[i]
        return ans

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        result = []
        for i in nums:
            if nums[abs(i)-1] < 0:
                result.append(abs(i))
            else:
                nums[abs(i)-1] *= -1
        return result


# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def findDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        result = []
        i = 0
        while i < len(nums):
            if nums[i] != nums[nums[i]-1]:
                nums[nums[i]-1], nums[i] = nums[i], nums[nums[i]-1]
            else:
                i += 1

        for i in range(len(nums)):
            if i != nums[i]-1:
                result.append(nums[i])
        return result


# Time:  O(n)
# Space: O(n), this doesn't satisfy the question
from collections import Counter
class Solution3(object):
    def findDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        return [elem for elem, count in Counter(nums).items() if count == 2]