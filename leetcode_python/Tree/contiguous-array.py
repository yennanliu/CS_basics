# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82667054
# https://kingsfish.github.io/2017/07/13/Leetcode-525-Contiguous-Array/
class Solution:
    def findMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        print(nums)
        total_sum = 0
        index_map = dict()
        index_map[0] = -1
        res = 0        
        for i, num in enumerate(nums):
            if num == 0:
                total_sum -= 1
            else:
                total_sum += 1
            if total_sum in index_map:
                res = max(res, i - index_map[total_sum])
            else:
                index_map[total_sum] = i
        return res
        
# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def findMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result, count = 0, 0
        lookup = {0: -1}
        for i, num in enumerate(nums):
            count += 1 if num == 1 else -1
            if count in lookup:
                result = max(result, i - lookup[count])
            else:
                lookup[count] = i

        return result
