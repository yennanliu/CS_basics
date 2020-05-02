# V0
from functools import cmp_to_key
class Solution:
    # @param num, a list of integers
    # @return a string
    def largestNumber(self, num):
        num = sorted([str(x) for x in num], key=cmp_to_key(self.compare))
        ans = ''.join(num).lstrip('0')
        return ans or '0'

    def compare(self, a, b):
        return [1, -1][a + b > b + a]

# V0'
class compare(str):
    # __lt__ defines ">" operator in python
    def __lt__(x, y):
        return x+y > y+x

class Solution:
    def largestNumber(self, nums):
        largest = sorted([str(v) for v in nums], key=compare) 
        largest = ''.join(largest) 
        return '0' if largest[0] == '0' else largest 

# V1
# http://bookshadow.com/weblog/2015/01/13/leetcode-largest-number/
# IDEA : SORT
# -> GO THROUGH THE nums
# -> WITH THE SORTED RULE :
# if str(x)+str(y)  > str(y) + str(x) => [x,y]
# elif str(x)+str(y)  > str(y) + str(x) => [y,x]
# 
# cmp is not working in python 3
# use cmp_to_key when in sorted instead
# https://py.checkio.org/blog/how-did-python3-lose-cmd-sorted/
#
# Note
# In [70]: x="00003243242000"
# In [71]: x
# Out[71]: '00003243242000'
# In [72]: x.lstrip("0")
# Out[72]: '3243242000'
from functools import cmp_to_key
class Solution:
    # @param num, a list of integers
    # @return a string
    def largestNumber(self, num):
        num = sorted([str(x) for x in num], key=cmp_to_key(self.compare))
        ans = ''.join(num).lstrip('0') # remove the 0 from lest beginning
        return ans or '0'

    def compare(self, a, b):
        return [1, -1][a + b > b + a]

### Tests case
s=Solution()
assert s.largestNumber([3, 30, 34, 5, 9])=="9534330"
assert s.largestNumber([7,99,101,10,3,2,5])=="99753210110"
assert s.largestNumber([0,0,0,0,1])=="10000"
assert s.largestNumber([0,0,0])=="0"
assert s.largestNumber([7,7,7,7,7])=="77777"
assert s.largestNumber([0,0,0,3,2])=="32000"
assert s.largestNumber([])=="0"

# V1'
# https://leetcode.com/problems/largest-number/discuss/53270/Python-simple-solution-in-4-lines
class Solution:
    def largestNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        nums = [str(x) for x in nums]
        longest = max([len(x) for x in nums], default=0)
        nums.sort(key=lambda x: x*(longest//len(x)+1), reverse=True)
        if nums and nums[0] == '0':
            return '0'
        return ''.join(nums)

# V1''
# https://blog.csdn.net/qian2729/article/details/50638656
class Solution(object):
    def largestNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        def compare(a,b):
            return int(b + a) - int(a + b)
        nums = sorted([str(x) for x in nums],cmp = compare)
        ans = ''.join(nums).lstrip('0')

        return ans or '0'

# V1'''
# https://www.jianshu.com/p/960cf375c40a
class Solution:
    def smaller(self,a,b):
        strA = str(a) + str(b)
        strB = str(b) + str(a)
        if strA > strB :
            return False
        else:
            return True

    def largestNumber(self, nums):
        resultStr = ""
        for i in range(0,len(nums)):
            for j in range(i,len(nums)):
                if self.smaller(nums[i],nums[j]):
                    temp = nums[i]
                    nums[i] = nums[j]
                    nums[j] = temp
        for i in nums:
            resultStr = resultStr + str(i)
        if (resultStr[0]=='0'):
            return '0'
        return resultStr

# V1'''''
# https://blog.csdn.net/XX_123_1_RJ/article/details/82632189
# sort 2 digit x, y : str(x) + str(y) > str(y) + str(x)
# e.g. x=2, y=10, xy > yx (210 >102)
class compare(str):
    def __lt__(x, y):
        return x+y > y+x

class Solution:
    def largestNumber(self, nums):
        largest = sorted([str(v) for v in nums], key=compare) 
        largest = ''.join(largest) 
        return '0' if largest[0] == '0' else largest 

# V2
class Solution(object):
    # @param num, a list of integers
    # @return a string
    def largestNumber(self, num):
        def cmp(a, b):
            return (a > b) - (a < b) 
        num = [str(x) for x in num]
        num.sort(cmp=lambda x, y: cmp(y + x, x + y))
        largest = ''.join(num)
        return largest.lstrip('0') or '0'