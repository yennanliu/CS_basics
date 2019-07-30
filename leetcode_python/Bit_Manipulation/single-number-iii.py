# python 3 
# https://leetcode.com/problems/single-number-iii/description/
# Time:  O(n)
# Space: O(1)
#
# Given an array of numbers nums, in which exactly two
# elements appear only once and all the other elements
# appear exactly twice. Find the two elements that appear only once.
#
# For example:
#
# Given nums = [1, 2, 1, 3, 2, 5], return [3, 5].
#
# Note:
# The order of the result is not important. So in the
# above example, [5, 3] is also correct.
# Your algorithm should run in linear runtime complexity.
# Could you implement it using only constant space complexity?


# V0
import collections 
class Solution(object):
    def singleNumber(self, nums):
        num_count = collections.Counter(nums)
        return [ i for i in num_count if num_count[i] == 1 ]

# V1 

# V2 
class Solution:
	def singleNumber(self, nums):
		no_repeat_array=[]
		for index, item in enumerate(nums):
			if item in no_repeat_array:
				no_repeat_array.remove(item)
			else:
				no_repeat_array.append(item)
		return no_repeat_array

# V3 
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        return [x[0] for x in sorted(list(collections.Counter(nums).items()), key=lambda i: i[1], reverse=False)[:2]]

# V4 
class Solution:
    def singleNumber(self, nums):
        x_xor_y = reduce(operator.xor, nums)
        bit =  x_xor_y & -x_xor_y
        result = [0, 0]
        for i in nums:
            result[bool(i & bit)] ^= i
        return result

# V5
class Solution2:
    # @param {integer[]} nums
    # @return {integer[]}
    def singleNumber(self, nums):
        x_xor_y = 0
        for i in nums:
            x_xor_y ^= i

        bit = x_xor_y & ~(x_xor_y - 1)

        x = 0
        for i in nums:
            if i & bit:
                x ^= i

        return [x, x ^ x_xor_y]

# V6 
# Time:  O(n)
# Space: O(1)
import operator
import collections
class Solution(object):
    # @param {integer[]} nums
    # @return {integer[]}
    def singleNumber(self, nums):
        x_xor_y = reduce(operator.xor, nums)
        bit =  x_xor_y & -x_xor_y
        result = [0, 0]
        for i in nums:
            result[bool(i & bit)] ^= i
        return result


class Solution2(object):
    # @param {integer[]} nums
    # @return {integer[]}
    def singleNumber(self, nums):
        x_xor_y = 0
        for i in nums:
            x_xor_y ^= i

        bit = x_xor_y & ~(x_xor_y - 1)

        x = 0
        for i in nums:
            if i & bit:
                x ^= i

        return [x, x ^ x_xor_y]


class Solution3(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        return [x[0] for x in sorted(collections.Counter(nums).items(), key=lambda i: i[1], reverse=False)[:2]]

