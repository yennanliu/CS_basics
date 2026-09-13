"""

166. Fraction to Recurring Decimal
Medium

Given two integers representing the numerator and denominator of a fraction, return the fraction in string format.

If the fractional part is repeating, enclose the repeating part in parentheses

If multiple answers are possible, return any of them.

It is guaranteed that the length of the answer string is less than 10^4 for all the given inputs.

Note that if the fraction can be represented as a finite length string, you must return it.

Example 1:

Input: numerator = 1, denominator = 2
Output: "0.5"

Example 2:

Input: numerator = 2, denominator = 1
Output: "2"

Example 3:

Input: numerator = 4, denominator = 333
Output: "0.(012)"

Constraints:

-2^31 <= numerator, denominator <= 2^31 - 1
denominator != 0

"""

# V0

# V1 


# V2 
# https://blog.csdn.net/qian2729/article/details/50638161
# time = O(n)  # n = length of decimal result string
# space = O(n)
class Solution(object):
    def fractionToDecimal(self, numerator, denominator):
        """
        :type numerator: int
        :type denominator: int
        :rtype: str
        """
        negativeSign = numerator * denominator < 0
        numerator = abs(numerator)
        denominator = abs(denominator)
        numslist = []
        loopDict = {}
        cnt = 0
        loopStr = None
        while True:
            numslist.append(str(numerator / denominator))
            cnt += 1
            numerator = 10 * (numerator % denominator)
            if numerator == 0:
                break
            loc = loopDict.get(numerator)
            if loc:
                loopStr = ''.join(numslist[loc:cnt])
                break
            loopDict[numerator] = cnt
        ans = numslist[0]
        if len(numslist) > 1:
            ans += '.'
        if loopStr:
            ans += ''.join(numslist[1:len(numslist) - len(loopStr)]) + '(' + ''.join(numslist[len(numslist) - len(loopStr):]) + ')'
        else:
            ans += ''.join(numslist[1:])
        if negativeSign:
            ans = '-' + ans
        return ans

# V3
# time = O(logn), where logn is the length of result strings
# space = O(1)

class Solution(object):
    def fractionToDecimal(self, numerator, denominator):
        """
        :type numerator: int
        :type denominator: int
        :rtype: str
        """
        result = ""
        if (numerator > 0 and denominator < 0) or (numerator < 0 and denominator > 0):
            result = "-"

        dvd, dvs = abs(numerator), abs(denominator)
        result += str(dvd / dvs)
        dvd %= dvs

        if dvd > 0:
            result += "."

        lookup = {}
        while dvd and dvd not in lookup:
            lookup[dvd] = len(result)
            dvd *= 10
            result += str(dvd / dvs)
            dvd %= dvs

        if dvd in lookup:
            result = result[:lookup[dvd]] + "(" + result[lookup[dvd]:] + ")"

        return result