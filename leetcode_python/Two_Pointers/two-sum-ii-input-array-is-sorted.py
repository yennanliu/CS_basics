
# V1  : need to improve ***

class Solution(object):
	def twoSum(self, nums, target):
		for i in range(len(nums)):
			for j in range( i+1 ,len(nums)):
				if nums[i] + nums[j] == target:
					return [i+1, j+1]
			else:
				pass 

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