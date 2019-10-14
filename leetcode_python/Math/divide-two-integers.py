# V0 

# V1
# https://www.cnblogs.com/zuoyuan/p/3779359.html
# IDEA : BINARY SEARCH 
class Solution:
    # @return an integer
    def divide(self, dividend, divisor):
        if (dividend < 0 and divisor > 0) or (dividend > 0 and divisor < 0):
            if abs(dividend) < abs(divisor):
                return 0
        sum = 0; count = 0; res = 0
        a = abs(dividend); b = abs(divisor)
        while a >= b:
            sum = b
            count = 1
            while sum + sum <= a:
                sum += sum
                count += count
            a -= sum
            res += count
        if (dividend < 0 and divisor > 0) or (dividend > 0 and divisor < 0):
            res = 0 - res
        return res

# V1'
# https://blog.csdn.net/NXHYD/article/details/72539880
class Solution(object):
    def divide(self, dividend, divisor):
        """
        :type dividend: int
        :type divisor: int
        :rtype: int
        """
        neg = (dividend >= 0) ^ (divisor >= 0)
        dividend, divisor = abs(dividend), abs(divisor)

        pos, base = 1, divisor 
        while base <= dividend:
            pos <<= 1
            base <<= 1

        base >>= 1
        pos >>= 1

        res = 0
        while pos > 0:
            if base <= dividend:
                res += pos
                dividend -= base
            base >>= 1
            pos >>= 1
        val = -res if neg else res
        return 2 ** 31 -1 if val > 2 ** 31 -1 else val

# V1'''
# https://www.jiuzhang.com/solution/divide-two-integers/#tag-highlight-lang-python
class Solution(object):
    def divide(self, dividend, divisor):
        INT_MAX = 2147483647
        if divisor == 0:
            return INT_MAX
        neg = dividend > 0 and divisor < 0 or dividend < 0 and divisor > 0
        a, b = abs(dividend), abs(divisor)
        ans, shift = 0, 31
        while shift >= 0:
            if a >= b << shift:
                a -= b << shift
                ans += 1 << shift
            shift -= 1
        if neg:
            ans = - ans
        if ans > INT_MAX:
            return INT_MAX
        return ans

# V1''''
class Solution(object):
	def divide(self, dividend, divisor):
		"""
		Assume we are dealing with an environment 
		which could only store integers within the 32-bit
		signed integer range: [-2**31,  2**31 − 1]. 
		For the purpose of this problem, assume that your 
		function returns 2**31 − 1 when the division result overflows.

		--> so the return value should   2**31 < always < 2**31 -1 
		(2**31 -1  = 2147483647,  2**31 = 2147483648)
		"""
		if  dividend*divisor > 0:
			return min(2147483647, dividend//divisor)
		else:
			return max(-2147483648, -(abs(dividend)//abs(divisor)))
            
# V2 
# Time:  O(logn) = O(1)
# Space: O(1)
class Solution(object):
    def divide(self, dividend, divisor):
        """
        :type dividend: int
        :type divisor: int
        :rtype: int
        """
        result, dvd, dvs = 0, abs(dividend), abs(divisor)
        while dvd >= dvs:
            inc = dvs
            i = 0
            while dvd >= inc:
                dvd -= inc
                result += 1 << i
                inc <<= 1
                i += 1
        if dividend > 0 and divisor < 0 or dividend < 0 and divisor > 0:
            return -result
        else:
            return result

    def divide2(self, dividend, divisor):
        """
        :type dividend: int
        :type divisor: int
        :rtype: int
        """
        positive = (dividend < 0) is (divisor < 0)
        dividend, divisor = abs(dividend), abs(divisor)
        res = 0
        while dividend >= divisor:
            temp, i = divisor, 1
            while dividend >= temp:
                dividend -= temp
                res += i
                i <<= 1
                temp <<= 1
        if not positive:
            res = -res
        return min(max(-2147483648, res), 2147483647)