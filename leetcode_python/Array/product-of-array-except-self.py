"""
Given an integer array nums, return an array answer such that answer[i] is equal to the product of all the elements of nums except nums[i].

The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.

You must write an algorithm that runs in O(n) time and without using the division operation.

Example 1:

Input: nums = [1,2,3,4]
Output: [24,12,8,6]


Example 2:

Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]


Constraints:

2 <= nums.length <= 105
-30 <= nums[i] <= 30
The product of any prefix or suffix of nums is guaranteed to fit in a 32-bit integer.
 

Follow up: Can you solve the problem in O(1) extra space complexity? (The output array does not count as extra space for space complexity analysis.)

"""

# V0
class Solution:
    def productExceptSelf(self, nums):
        # CASE 1: has `more that one 0` -> the `Product of Array Except Self` should always equal 0
        if nums.count(0) > 1:
            return [0] * len(nums)
        # CASE 2: has `only one 0` -> we get the `Product of Array Except Self` for non 0 and 0 separately
        if nums.count(0) == 1:
            _prod_except_z = 1
            for n in nums:
                if n != 0:
                    _prod_except_z = n * _prod_except_z
        # for non 0 element, we still get the Product of Array for op later
        _prod = 1
        for n in nums:
            _prod = n * _prod
        r = []
        for i in nums:
            if i != 0:
                r.append(int(_prod / i))
            else:
                r.append(int(_prod_except_z))
        return r

# V0'
class Solution:
    # @param {integer[]} nums
    # @return {integer[]}
    def productExceptSelf(self, nums):
        size = len(nums)
        output = [1] * size
        left = 1
        for x in range(size - 1):
            left *= nums[x]
            output[x + 1] *= left
        right = 1
        for x in range(size - 1, 0, -1):
            right *= nums[x]
            output[x - 1] *= right
        return output
        
# V1 
# http://bookshadow.com/weblog/2015/07/16/leetcode-product-array-except-self/
# https://blog.csdn.net/fuxuemingzhu/article/details/79325534
# IDEA : 
# SINCE output[i] = (x0 * x1 * ... * xi-1) * (xi+1 * .... * xn-1)
# -> SO DO A 2 LOOP
# -> 1ST LOOP : GO THROGH THE ARRAY (->) : (x0 * x1 * ... * xi-1)
# -> 2ND LOOP : GO THROGH THE ARRAY (<-) : (xi+1 * .... * xn-1)
# e.g.
# given [1,2,3,4], return [24,12,8,6].
# -> output = [2*3*4, 1,1,1]  <-- 2*3*4    (right of 1: 2,3,4)
# -> output = [2*3*4, 1*3*4,1,1] <-- 1*3*4 (left of 2 :1, right of 2: 3,4)
# -> output = [2*3*4, 1*3*4,1*2*4,1] <-- 1*2*4 (left of 3: 1,2 right of 3 : 4)
# -> output = [2*3*4, 1*3*4,1*2*4,1*2*3] <-- 1*2*3 (left of 4 : 1,2,3)
# -> final output  = [2*3*4, 1*3*4,1*2*4,1*2*3] = [24,12,8,6]
class Solution:
    # @param {integer[]} nums
    # @return {integer[]}
    def productExceptSelf(self, nums):
        size = len(nums)
        output = [1] * size
        left = 1
        for x in range(size - 1):
            left *= nums[x]
            output[x + 1] *= left
        right = 1
        for x in range(size - 1, 0, -1):
            right *= nums[x]
            output[x - 1] *= right
        return output

# V1'
# https://www.jiuzhang.com/solution/product-of-array-except-self/#tag-highlight-lang-python
class Solution(object):
    def productExceptSelf(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        result = [1 for i in nums]
        nf = 1
        nb = 1
        length = len(nums)
        for i in range(length):
            result[i] *= nf
            nf *= nums[i]
            result[length-i-1] *= nb
            nb *= nums[length-i-1]
        return result

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param {integer[]} nums
    # @return {integer[]}
    def productExceptSelf(self, nums):
        if not nums:
            return []
        left_product = [1 for _ in range(len(nums))]
        for i in range(1, len(nums)):
            left_product[i] = left_product[i - 1] * nums[i - 1]

        right_product = 1
        for i in range(len(nums) - 2, -1, -1):
            right_product *= nums[i + 1]
            left_product[i] = left_product[i] * right_product
        return left_product
