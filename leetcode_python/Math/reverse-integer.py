# V0
class Solution(object):
    def reverse(self, x):
        if x < 0:
            x =  -int(str(x)[1:][::-1])
        else:
            x = int(str(x)[::-1])
        x = 0 if abs(x) > 0x7FFFFFFF else x
        return x
        
# test case 
s = Solution()
assert s.reverse(0) == 0
assert s.reverse(10) == 1 
assert s.reverse(1000) == 1 
assert s.reverse(-1) == -1
assert s.reverse(-10) == -1
assert s.reverse(-1) == -1
assert s.reverse(0x7FFFFFFF) == 0
assert s.reverse(-0x7FFFFFFF) == 0
# to check 
#assert s.reverse(0x7FFFFFFF - 1) == 0 

# V1 
class Solution(object):
    # x = 20010000
    # x_reverse = int(str(x)[::-1]) 
    # x_reverse = 1002 
    def reverse(self, x):
        if x < 0:
            x =  -int(str(x)[1:][::-1])
        else:
            x = int(str(x)[::-1])
        x = 0 if abs(x) > 0x7FFFFFFF else x
        return x

# V1'
# https://blog.csdn.net/coder_orz/article/details/52039990
class Solution(object):
    def reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        flag = 1 if x >= 0 else -1
        new_x, x = 0, abs(x)
        while x:
            new_x = 10 * new_x + x % 10
            x /= 10
        new_x = flag * new_x
        return new_x if new_x < 2147483648 and new_x >= -2147483648 else 0

# V1''
# https://blog.csdn.net/coder_orz/article/details/52039990
class Solution(object):
    def reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        x = int(str(x)[::-1]) if x >= 0 else - int(str(-x)[::-1])
        return x if x < 2147483648 and x >= -2147483648 else 0

# V2 
# Time:  O(logn) = O(1)
# Space: O(1)
class Solution(object):
    def reverse(self, x):
        """
        :type x: int
        :rtype: int
        """
        if x < 0:
            return -self.reverse(-x)

        result = 0
        while x:
            result = result * 10 + x % 10
            x //= 10
        return result if result <= 0x7fffffff else 0  # Handle overflow.

    def reverse2(self, x):
        """
        :type x: int
        :rtype: int
        """
        if x < 0:
            x = int(str(x)[::-1][-1] + str(x)[::-1][:-1])
        else:
            x = int(str(x)[::-1])
        x = 0 if abs(x) > 0x7FFFFFFF else x
        return x

    def reverse3(self, x):
        """
        :type x: int
        :rtype: int
        """
        def cmp(a, b):
            return (a > b) - (a < b)
        s = cmp(x, 0)
        r = int(repr(s * x)[::-1])
        return s * r * (r < 2 ** 31)
