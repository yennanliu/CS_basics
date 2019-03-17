

# V1 


# V2 
# https://blog.csdn.net/qian2729/article/details/50638161
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
# Time:  O(logn), where logn is the length of result strings
# Space: O(1)

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