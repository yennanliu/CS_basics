# V0 

# V1 
# http://bookshadow.com/weblog/2018/01/21/leetcode-max-chunks-to-make-sorted-ver-1/
# IDEA : GREEDY 
class Solution(object):
    def maxChunksToSorted(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        N = len(arr)
        ans = 1
        for x in range(N - 1, 0, -1):
            if min(arr[x:]) > max(arr[:x]):
                ans += 1
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80482014
class Solution:
    def maxChunksToSorted(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        chunks = 0
        pre_max = 0
        for i, num in enumerate(arr):
            if num > pre_max:
                pre_max = num
            if pre_max == i:
                chunks += 1
        return chunks
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def maxChunksToSorted(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        result, max_i = 0, 0
        for i, v in enumerate(arr):
            max_i = max(max_i, v)
            if max_i == i:
                result += 1
        return result
