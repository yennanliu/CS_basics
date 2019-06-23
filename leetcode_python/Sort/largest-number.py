# V1  : DEV 
# class Solution:
# 	def largestNumber(self, num):
# 		num_ = [str(x) for x in num]
# 		x = ''.join(num_)
# 		return ''.join(sorted(x, reverse=True))

# V2 
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
# V3 
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