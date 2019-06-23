"""
Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊ n/2 ⌋ times.

You may assume that the array is non-empty and the majority element always exist in the array.

Example 1:

Input: [3,2,3]
Output: 3
Example 2:

Input: [2,2,1,1,1,2,2]
Output: 2
"""


# V1 
class Solution:
	def majorityElement(self, nums):
		len_nums = len(nums)
		for num in list(set(nums)):
			if nums.count(num) > len_nums/2:
				return num
			else:
				pass 


# V2 

import collections


class Solution:
    def majorityElement(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        idx, cnt = 0, 1

        for i in range(1, len(nums)):
            if nums[idx] == nums[i]:
                cnt += 1
            else:
                cnt -= 1
                if cnt == 0:
                    idx = i
                    cnt = 1

        return nums[idx]

    def majorityElement2(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return sorted(list(collections.Counter(nums).items()), key=lambda a: a[1], reverse=True)[0][0]


        









