"""

1979. Find Greatest Common Divisor of Array
Easy

Given an integer array nums, return the greatest common divisor of the smallest number and largest number in nums.

The greatest common divisor of two numbers is the largest positive integer that evenly divides both numbers.

 

Example 1:

Input: nums = [2,5,6,9,10]
Output: 2
Explanation:
The smallest number in nums is 2.
The largest number in nums is 10.
The greatest common divisor of 2 and 10 is 2.
Example 2:

Input: nums = [7,5,6,8,3]
Output: 1
Explanation:
The smallest number in nums is 3.
The largest number in nums is 8.
The greatest common divisor of 3 and 8 is 1.
Example 3:

Input: nums = [3,3]
Output: 3
Explanation:
The smallest number in nums is 3.
The largest number in nums is 3.
The greatest common divisor of 3 and 3 is 3.
 

Constraints:

2 <= nums.length <= 1000
1 <= nums[i] <= 1000

"""

# V0

# V1
# https://leetcode.com/problems/find-greatest-common-divisor-of-array/discuss/1459270/python-solution
class Solution:
    def findGCD(self, nums):
        min_ = min(nums)
        max_ = max(nums)
        
        for i in range(min_, 1, -1):
            if not max_ % i and not min_ % i:
                return i
            
        return 1

# V1'
# https://leetcode.com/problems/find-greatest-common-divisor-of-array/discuss/1442477/Python-Euclidean-algorithm
class Solution:
    def findGCD(self, nums):
        a, b = min(nums), max(nums)
        while a:
            a, b = b % a, a
        return b

# V1''
# https://leetcode.com/problems/find-greatest-common-divisor-of-array/discuss/1418724/python-easy-to-undetstand
class Solution:
	def findGCD(self, nums):
		maxNum = max(nums)
		minNum = min(nums)
		for i in range(minNum, 0, -1):
			if maxNum % i == 0 and minNum % i == 0:
				return i

# V2