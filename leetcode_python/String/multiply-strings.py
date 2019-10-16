# V0
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
# Time:  O(m * n)
# Space: O(m + n)
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

# Time:  O(m * n)
# Space: O(m + n)
# Using built-in bignum solution.
class Solution2(object):
    def multiply(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        return str(int(num1) * int(num2))