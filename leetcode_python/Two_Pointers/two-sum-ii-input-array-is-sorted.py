# V0

# V1 
# https://blog.csdn.net/coder_orz/article/details/52388066
# IDEA : TWO POINTER
class Solution(object):
    def twoSum(self, numbers, target):
        """
        :type numbers: List[int]
        :type target: int
        :rtype: List[int]
        """
        left, right = 0, len(numbers) - 1
        while left < right:
            if numbers[left] + numbers[right] == target:
                return [left + 1, right + 1]
            elif numbers[left] + numbers[right] > target:
                right -= 1
            else:
                left += 1
                
# V1' 
# https://blog.csdn.net/coder_orz/article/details/52388066
# IDEA : DICT
class Solution(object):
    def twoSum(self, numbers, target):
        """
        :type numbers: List[int]
        :type target: int
        :rtype: List[int]
        """
        num_dict = {}
        for i, num in enumerate(numbers):
            if (target - num) in num_dict:
                return [num_dict[target - num], i + 1]
            num_dict[num] = i + 1

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def twoSum(self, nums, target):
        start, end = 0, len(nums) - 1

        while start != end:
            sum = nums[start] + nums[end]
            if sum > target:
                end -= 1
            elif sum < target:
                start += 1
            else:
                return [start + 1, end + 1]