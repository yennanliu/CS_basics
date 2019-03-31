# V1 : dev 


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79403723
# ideas:
# case 1 : return nums if there is only one number
# case 2 : return nums[0]/nums[1] if there are only 2 numbers 
# case 3 : return nums[0]/(nums[1]*nums[2]*....*nums[N]) if there are N numbers 
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """ 
        nums = list(map(str, nums)) # adapt here for python 3 
        if len(nums) <= 2:
            return '/'.join(nums)
        return '{}/({})'.format(nums[0], '/'.join(nums[1:]))

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        if len(nums) == 1:
            return str(nums[0])
        if len(nums) == 2:
            return str(nums[0]) + "/" + str(nums[1])
        result = [str(nums[0]) + "/(" + str(nums[1])]
        for i in xrange(2, len(nums)):
            result += "/" + str(nums[i])
        result += ")"
        return "".join(result)
