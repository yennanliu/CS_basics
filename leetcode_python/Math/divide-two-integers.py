

# V1 
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