"""

171. Excel Sheet Column Number
Easy

Given a string columnTitle that represents the column title as appears in an Excel sheet, return its corresponding column number.

For example:

A -> 1
B -> 2
C -> 3
...
Z -> 26
AA -> 27
AB -> 28
...

Example 1:

Input: columnTitle = "A"
Output: 1

Example 2:

Input: columnTitle = "AB"
Output: 28

Example 3:

Input: columnTitle = "ZY"
Output: 701

Constraints:

1 <= columnTitle.length <= 7
columnTitle consists only of uppercase English letters.
columnTitle is in the range ["A", "FXSHRXW"].

"""

# V0


# V1
# https://blog.csdn.net/coder_orz/article/details/51406455
# time = O(n)  # n = len(s)
# space = O(1)
class Solution(object):
    # transform   26 Carry ->  10 Carry (Decimal)  
    def titleToNumber(self, s):
        """
        :type s: str
        :rtype: int
        """
        sum = 0
        for c in s:
            sum = sum*26 + ord(c) - 64 # 64 = ord('A') - 1
        return sum

# V2
# time = O(n)  # n = len(s)
# space = O(1)
class Solution(object):
    def titleToNumber(self, s):
        """
        :type s: str
        :rtype: int
        """
        result = 0
        for i in range(len(s)):
            result *= 26
            result += ord(s[i]) - ord('A') + 1
        return result