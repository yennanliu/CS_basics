"""

504. Base 7
Easy

Given an integer num, return a string of its base 7 representation.

 

Example 1:

Input: num = 100
Output: "202"
Example 2:

Input: num = -7
Output: "-10"
 

Constraints:

-107 <= num <= 107

"""

# V0
# IDEA : MATH : 10 based -> 7 based
"""
### NOTE :
    1) for negative num : transform to positive int first, do 10 based -> 7 based op
                          then add "-" at beginning
    2) for positive num : do 10 based -> 7 based op
                        -> keep checking if num % N == 0
                        -> if not == 0, keep do  num = num % N, and append cur result to res
                        -> reverse res
                        -> make array to string
                        -> return result
    3) example:
        100 (10 based) -> "202" (7 based)

        tmp = 100
        a, b = divmod(tmp, 7)  -> a = 14, b = 2,  tmp  = 14
        a, b = divmod(tmp, 7)  -> a= 2, b = 0, tmp = 2
        a, b = divmod(tmp, 7)  -> a = 0, b = 2

        -> so res = [2,0,2]
"""
class Solution(object):
    def convertToBase7(self, num):
        # edge case
        if num == 0:
            return '0'
        tmp = abs(num)
        res = []
        while tmp > 0:
            a, b = divmod(tmp, 7)
            res.append(str(b))
            tmp = a
        res = res[::-1]
        _res = "".join(res)
        if num > 0:
            return _res
        else:
            return "-" + _res

# V1
# https://www.itread01.com/content/1544603062.html
# https://kknews.cc/code/jlv38qp.html
class Solution(object):
    def convertToBase7(self, num):
        # edge case
        if num == 0:
            return '0'
        tmp = abs(num)
        res = []
        while tmp:
            i = tmp % 7
            res.append(str(i))
            tmp = tmp // 7
        res = res[::-1]
        _res = "".join(res)
        if num > 0:
            return _res
        else:
            return "-" + _res 

# V1'
# https://kknews.cc/code/jlv38qp.html
# IDEA : JAVA
# class Solution {
#     public String convertToBase7(int num) {
#         return Integer.toString(num, 7);
#     }
# }

# V2