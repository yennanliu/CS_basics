"""

Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.

Symbol       Value
I             1
V             5
X             10
L             50
C             100
D             500
M             1000
For example, 2 is written as II in Roman numeral, just two one's added together. 12 is written as XII, which is simply X + II. The number 27 is written as XXVII, which is XX + V + II.

Roman numerals are usually written largest to smallest from left to right. However, the numeral for four is not IIII. Instead, the number four is written as IV. Because the one is before the five we subtract it making four. The same principle applies to the number nine, which is written as IX. There are six instances where subtraction is used:

I can be placed before V (5) and X (10) to make 4 and 9. 
X can be placed before L (50) and C (100) to make 40 and 90. 
C can be placed before D (500) and M (1000) to make 400 and 900.
Given a roman numeral, convert it to an integer.

 

Example 1:

Input: s = "III"
Output: 3
Example 2:

Input: s = "IV"
Output: 4
Example 3:

Input: s = "IX"
Output: 9
Example 4:

Input: s = "LVIII"
Output: 58
Explanation: L = 50, V= 5, III = 3.
Example 5:

Input: s = "MCMXCIV"
Output: 1994
Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
 

Constraints:

1 <= s.length <= 15
s contains only the characters ('I', 'V', 'X', 'L', 'C', 'D', 'M').
It is guaranteed that s is a valid roman numeral in the range [1, 3999].


"""

# V0
class Solution(object):
    def romanToInt(self, s):
        roman = {"I":1, "V":5, "X":10, "L":50, "C":100, "D":500, "M":1000}
        ### NOTE THIS TRICK (loop from last 2 element)
        res = roman[s[-1]]
        N = len(s)
        ### NOTE THIS : set -1 as stop point can always loop til the final element (inverse order)
        for i in range(N - 2, -1, -1):
            ### NOTE : if any left element < right element  -> need to minus the left element (NOT LIMIT TO 2 ELEMENT PAIR)
            if roman[s[i]] < roman[s[i + 1]]:
                res -= roman[s[i]]
            ### NOTE : if any left element > right element  -> need to add the left element (NOT LIMIT TO 2 ELEMENT PAIR)
            else:
                res += roman[s[i]]
        return res
        
# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/48208995
class Solution(object):
    def romanToInt(self, s):
        """
        :type s: str
        :rtype: int
        """
        roman = {"I":1, "V":5, "X":10, "L":50, "C":100, "D":500, "M":1000}
        res = roman[s[-1]]
        N = len(s)
        for i in range(N - 2, -1, -1):
            if roman[s[i]] < roman[s[i + 1]]:
                res -= roman[s[i]]
            else:
                res += roman[s[i]]
        return res

# V1'
# https://www.jiuzhang.com/solution/roman-to-integer/#tag-highlight-lang-python
class Solution:
    # @param {string} s
    # @return {integer}
    def romanToInt(self, s):
        ROMAN = {
            'I': 1,
            'V': 5,
            'X': 10,
            'L': 50,
            'C': 100,
            'D': 500,
            'M': 1000
        }
        
        if s == "":
            return 0
            
        index = len(s) - 2
        sum = ROMAN[s[-1]]
        while index >= 0:
            if ROMAN[s[index]] < ROMAN[s[index + 1]]:
                sum -= ROMAN[s[index]]
            else:
                sum += ROMAN[s[index]]
            index -= 1
        return sum

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/48208995
class Solution(object):
    def romanToInt(self, s):
        """
        :type s: str
        :rtype: int
        """
        roman = {"I":1, "V":5, "X":10, "L":50, "C":100, "D":500, "M":1000}
        res = roman[s[-1]]
        N = len(s)
        for i in range(N - 2, -1, -1):
            if roman[s[i]] < roman[s[i + 1]]:
                res -= roman[s[i]]
            else:
                res += roman[s[i]]
        return res

# V3
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @return an integer
    def romanToInt(self, s):
        numeral_map = {"I": 1, "V": 5, "X": 10, "L": 50, "C":100, "D": 500, "M": 1000}
        decimal = 0
        for i in range(len(s)):
            if i > 0 and numeral_map[s[i]] > numeral_map[s[i - 1]]:
                decimal += numeral_map[s[i]] - 2 * numeral_map[s[i - 1]]
            else:
                decimal += numeral_map[s[i]]
        return decimal
