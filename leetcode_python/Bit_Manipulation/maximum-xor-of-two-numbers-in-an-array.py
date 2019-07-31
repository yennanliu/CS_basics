# V0 

# V1 
# http://bookshadow.com/weblog/2016/10/15/leetcode-maximum-xor-of-two-numbers-in-an-array/
# https://blog.csdn.net/fuxuemingzhu/article/details/79473171
class Solution(object):
    def findMaximumXOR(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        ans = mask = 0
        for x in range(32)[::-1]:
            mask += 1 << x
            prefixSet = set([n & mask for n in nums])
            temp = ans | 1 << x
            for prefix in prefixSet:
                if temp ^ prefix in prefixSet:
                    ans = temp
                    break
        return ans
       
# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def findMaximumXOR(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result = 0

        for i in reversed(xrange(32)):
            result <<= 1
            prefixes = set()
            for n in nums:
                prefixes.add(n >> i)
            for p in prefixes:
                if (result | 1) ^ p in prefixes:
                    result += 1
                    break

        return result
