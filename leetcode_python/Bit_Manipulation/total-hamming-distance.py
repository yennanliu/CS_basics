# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79494653
class Solution(object):
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        res = 0
        for pos in range(32):
            bitCount = 0
            for i in range(len(nums)):
                bitCount += (nums[i] >> pos) & 1
            res += bitCount * (len(nums) - bitCount)
        return res

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def totalHammingDistance(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result = 0
        for i in range(32):
            counts = [0] * 2
            for num in nums:
                counts[(num >> i) & 1] += 1
            result += counts[0] * counts[1]
        return result
