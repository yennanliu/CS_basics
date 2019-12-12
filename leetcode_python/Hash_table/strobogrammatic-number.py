"""
A strobogrammatic number is a number that looks the same when rotated 180 degrees (looked at upside down).

Write a function to determine if a number is strobogrammatic. The number is represented as a string.

For example, the numbers "69", "88", and "818" are all strobogrammatic.
"""

# V0 
class Solution(object):
    def isStrobogrammatic(self, num):
        """
        :type num: str
        :rtype: bool
        """
        d = {'0':'0','1':'1','6':'9','8':'8','9':'6'}
        ans = ''
        for n in num:
            if n not in d:
                return False
            ans += d[n]
        return ans[::-1] == num

# V1 
# https://blog.csdn.net/danspace1/article/details/88944960
#     ...: num = '69'
#     ...: Solution().isStrobogrammatic(num)    
#     ...: 
# Out[18]: True
#     ...: num = '699'
#     ...: Solution().isStrobogrammatic(num)    
#     ...: 
# Out[19]: False
#     ...: num = '6699'
#     ...: Solution().isStrobogrammatic(num)    
#     ...: 
# Out[20]: True
class Solution(object):
    def isStrobogrammatic(self, num):
        """
        :type num: str
        :rtype: bool
        """
        d = {'0':'0','1':'1','6':'9','8':'8','9':'6'}
        ans = ''
        for n in num:
            if n not in d:
                return False
            ans += d[n]
        return ans[::-1] == num

# V1'
# https://www.jiuzhang.com/solution/strobogrammatic-number/#tag-highlight-lang-python
class Solution:
    """
    @param num: a string
    @return: true if a number is strobogrammatic or false
    """
    def isStrobogrammatic(self, num):
        # write your code here
        map = {'0':'0', '1':'1', '6':'9', '8':'8', '9':'6'}
        i, j = 0, len(num)-1
        while i<=j:
            if not num[i] in map or map[num[i]] != num[j]:
                return False
            i, j = i+1, j-1
        return True

# V2  
# need to double check 
class Solution:
    def isStrobogrammatic(self, nums):
        lookup = {'0':'0', '1':'1', '6':'9', '8':'8', '9':'6'}
        for i, num in enumerate(list(nums)):
            #print (num)
            if str(num) in lookup:
                pass
            else:
                return False
        return True 

# V3 
class Solution:
    # @param {string} num
    # @return {boolean}
    def isStrobogrammatic(self, num):
        lookup = {'0':'0', '1':'1', '6':'9', '8':'8', '9':'6'}
        n = len(num)
        for i in range((n+1) / 2):
            if num[n-1-i] not in self.lookup or \
               num[i] != self.lookup[num[n-1-i]]:
                return False
        return True