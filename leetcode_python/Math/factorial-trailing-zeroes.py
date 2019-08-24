# V0

# V1 
# https://blog.csdn.net/coder_orz/article/details/51590478
# IDEA : Iteration
class Solution(object):
    def trailingZeroes(self, n):
        """
        :type n: int
        :rtype: int
        """
        res = 0
        while n > 0:
            n = n/5
            res += n
        return res

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51590478
# IDEA : Recursion
class Solution(object):
    def trailingZeroes(self, n):
        """
        :type n: int
        :rtype: int
        """
        return 0 if n == 0 else n / 5 + self.trailingZeroes(n / 5)

# V2 
"""
* only have to consider 5, but not 2, since the amount of 2 is always enough 
for example :

135!
 
-> 135/5 = 27 : there are 27 number in [1,  135] is 5 multiplier
-> 27/5 =  5 : there are 5 number in [1, 135] is 25 multiplier
-> 5/5 =  1 : there are 1 number in [1,  135] is 125 multiplier

so the number Trailing Zeroes in 135! =  27+5 + 1 = 33

"""
# https://blog.csdn.net/coder_orz/article/details/51590478
class Solution(object):
    def trailingZeroes(self, n):
        """
        :type n: int
        :rtype: int
        """
        res = 0
        while n > 0:
            n = n/5
            res += n
        return res

# V3 
# Time:  O(logn) = O(1)
# Space: O(1)
class Solution(object):
    # @return an integer
    def trailingZeroes(self, n):
        result = 0
        while n > 0:
            result += n / 5
            n /= 5
        return result
