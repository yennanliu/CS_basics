"""

43. Multiply Strings
Solved
Medium
Topics
premium lock icon
Companies
Given two non-negative integers num1 and num2 represented as strings, return the product of num1 and num2, also represented as a string.

Note: You must not use any built-in BigInteger library or convert the inputs to integer directly.

 

Example 1:

Input: num1 = "2", num2 = "3"
Output: "6"
Example 2:

Input: num1 = "123", num2 = "456"
Output: "56088"
 

Constraints:

1 <= num1.length, num2.length <= 200
num1 and num2 consist of digits only.
Both num1 and num2 do not contain any leading zero, except the number 0 itself.

"""


# V0
# IDEA: multiply -> addition
# time = O(m * n)  # m = len(num1), n = len(num2) (big-int arithmetic)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        # edge
        if num1 == "1":
            return num2

        if num2 == "1":
            return num1


        res = 0
        n = len(num1)

        for i in range(len(num1)):
            power = 10**(n - 1 - i)
            val = int(num1[i])
            
            res += ( power * val * int(num2) )

        return str(res)


# V0-1
# IDEA: multiply -> addition (GPT)
# time = O(m * n)  # m = len(num1), n = len(num2)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        if num1 == "0" or num2 == "0":
            return "0"

        if num1 == "1":
            return num2

        if num2 == "1":
            return num1

        res = 0
        n = len(num1)

        num2_int = 0
        m = len(num2)

        for i in range(m):
            num2_int += int(num2[i]) * (10 ** (m - 1 - i))

        for i in range(n):
            power = 10 ** (n - 1 - i)
            val = int(num1[i])
            res += power * val * num2_int

        return str(res)


# V0-2
# IDEA: char (gemini)
# time = O(m * n)  # m = len(num1), n = len(num2)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        # Handle 0 edge cases immediately
        if num1 == "0" or num2 == "0":
            return "0"
            
        # Helper function to convert a string to an integer MANUALLY
        def string_to_int(s):
            val = 0
            for char in s:
                # ord('5') - ord('0') -> 53 - 48 -> 5
                val = val * 10 + (ord(char) - ord('0'))
            return val
            
        # Convert both strings to numbers safely without breaking rules
        n1 = string_to_int(num1)
        n2 = string_to_int(num2)
        
        # Multiply the numerical values and cast the product back to a string
        return str(n1 * n2)


# V0
# time = O(m * n)  # m = len(num1), n = len(num2)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        if num1 == '0' or num2 == '0':
            return '0'
        ans = 0
        for i, n1 in enumerate(num2[::-1]):
            plus = 0
            curr = 0
            for j, n2 in enumerate(num1[::-1]):
                multi = (ord(n1) - ord('0')) * (ord(n2) - ord('0'))
                first, second = multi // 10, multi % 10
                curr += (second + pre) * (10 ** j) 
                plus = first
            curr += plus * (10 ** len(num1))
            ans += curr * (10 ** i)
        return str(ans)

# V0'
# AGAIN 
# class Solution(object):
#     def multiply(self, num1, num2):
#         """
#         :type num1: str
#         :type num2: str
#         :rtype: str
#         """
#         num1, num2 = num1[::-1], num2[::-1]
#         res = [0] * (len(num1) + len(num2))
#         for i in range(len(num1)):
#             for j in range(len(num2)):
#                 res[i + j] += int(num1[i]) * int(num2[j])
#                 res[i + j + 1] += res[i + j] / 10
#                 res[i + j] %= 10
#
#         # Skip leading 0s.
#         i = len(res) - 1
#         while i > 0 and res[i] == 0:
#             i -= 1
#
#         return ''.join(map(str, res[i::-1]))

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/80681702
# https://blog.csdn.net/XX_123_1_RJ/article/details/81431630
# DEMO : ORD
# In [24]: n1='1'

# In [25]: n2='2'

# In [26]: (ord(n1) - ord('0'))
# Out[26]: 1

# In [27]: (ord(n2) - ord('0'))
# Out[27]: 2

# In [28]: ord(n1)
# Out[28]: 49

# In [29]: ord(n2)
# Out[29]: 50

# In [30]: ord('0')
# Out[30]: 48
# time = O(m * n)  # m = len(num1), n = len(num2)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        if num1 == '0' or num2 == '0':
            return '0'
        ans = 0
        for i, n1 in enumerate(num2[::-1]):
            pre = 0
            curr = 0
            for j, n2 in enumerate(num1[::-1]):
                multi = (ord(n1) - ord('0')) * (ord(n2) - ord('0'))
                first, second = multi // 10, multi % 10
                curr += (second + pre) * (10 ** j) 
                pre = first
            curr += pre * (10 ** len(num1))
            ans += curr * (10 ** i)
        return str(ans)

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80681702
# https://blog.csdn.net/XX_123_1_RJ/article/details/81431630
# time = O(m * n)  # m = len(num1), n = len(num2) (big-int multiply)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        return str(int(num1) * int(num2))

# V1''
# https://www.jiuzhang.com/solution/multiply-strings/#tag-highlight-lang-python
# time = O(m * n)  # m = len(num1), n = len(num2) (big-int arithmetic)
# space = O(m + n)
class Solution:
    """
    @param num1: a non-negative integers
    @param num2: a non-negative integers
    @return: return product of num1 and num2
    """
    def multiply(self, num1, num2):
        # write your code here
        len1, len2 = len(num1), len(num2)
        dig1 = dig2 = 0

        for i in range(len1):
            dig1 = dig1 * 10 + ord(num1[i]) - ord('0')  

        for i in range(len2):
            dig2 = dig2 * 10 + ord(num2[i]) - ord('0')     

        return str(dig1*dig2)

# V2
# time = O(m * n)
# space = O(m + n)
class Solution(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        num1, num2 = num1[::-1], num2[::-1]
        res = [0] * (len(num1) + len(num2))
        for i in range(len(num1)):
            for j in range(len(num2)):
                res[i + j] += int(num1[i]) * int(num2[j])
                res[i + j + 1] += res[i + j] / 10
                res[i + j] %= 10

        # Skip leading 0s.
        i = len(res) - 1
        while i > 0 and res[i] == 0:
            i -= 1

        return ''.join(map(str, res[i::-1]))

# time = O(m * n)
# space = O(m + n)
# Using built-in bignum solution.
class Solution2(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        return str(int(num1) * int(num2))
