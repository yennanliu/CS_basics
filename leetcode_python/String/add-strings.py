# Time:  O(n)
# Space: O(1)
# Given two non-negative numbers num1 and num2 represented as string,
# return the sum of num1 and num2.
#
# Note:
#
# The length of both num1 and num2 is < 5100.
# Both num1 and num2 contains only digits 0-9.
# Both num1 and num2 does not contain any leading zero.
# You must not use any built-in BigInteger library or
# convert the inputs to integer directly.

# V0
class Solution(object):
    def addStrings(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        result = []
        carry = 0
        while num1 or num2 or carry:  # while there is still non-add digit in num1, and num2; or there is non-zero carry 
            digit = carry
            if num1:
                digit += int(num1[-1])
                num1 = num1[:-1]
            if num2:
                digit += int(num2[-1])
                num2 = num2[:-1]
            carry = digit > 9 # true if digit (e.g. 10,11...), so carry == True and will do addition to next digit. vice versa.
            result.append(str(digit % 10))
        return ''.join(result[::-1]) 

# V1 
# http://bookshadow.com/weblog/2016/10/09/leetcode-add-strings/
class Solution(object):
    def addStrings(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        result = []
        carry = 0
        idx1, idx2 = len(num1), len(num2)
        while idx1 or idx2 or carry:  # while there is still non-add digit in num1, and num2; or there is non-zero carry 
            digit = carry
            if idx1:
                idx1 -= 1 # add from rightest digit (inverse from num1 )
                digit += int(num1[idx1])
            if idx2:
                idx2 -= 1 # add from rightest digit (inverse from num1 )
                digit += int(num2[idx2])
            carry = digit > 9 # true if digit (e.g. 10,11...), so carry == True and will do addition to next digit. vice versa.
            result.append(str(digit % 10))
        return ''.join(result[::-1]) 

# V1'
# https://www.jiuzhang.com/solution/add-strings/#tag-highlight-lang-python
class Solution(object):
    def addStrings(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        res = ""
        m = len(num1)
        n = len(num2)
        i = m - 1
        j = n - 1
        flag = 0
        while i >=0 or j >= 0:
            a = int(num1[i]) if i >=0 else 0
            i = i - 1
            b = int(num2[j]) if j >=0 else 0
            j = j - 1
            sum = a + b + flag
            res = str(sum % 10 ) + res;
            flag = sum / 10
        return res if flag == 0 else (str(flag)+ res)

# V2 
class Solution:
    def addStrings(self, num1, num2):
        output = []
        carry_fix = 0 
        lenth_ = max(len(num1), len(num2))
        num1 = '0'*(lenth_ - len(num1)) + num1 
        num2 = '0'*(lenth_ - len(num2)) + num2
        print (num1)
        print (num2)
        for i in reversed(list(range(len(num1)))):
            sum_ = int(num1[i])+ int(num2[i]) + int(carry_fix)
            if (sum_ >= 10 and i != 0 ):
                sum_ = sum_ - 10 
                output.append( str(sum_))
                carry_fix = 1 
            elif (sum_ >= 10 and i ==0):
                output.append( str(sum_))        
            else:
                output.append( str(sum_))    
                carry_fix = 0 
        return ''.join(list(reversed(output)))
            
# V3 
class Solution(object):
    def addStrings(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        result = []
        i, j, carry = len(num1) - 1, len(num2) - 1, 0

        while i >= 0 or j >= 0 or carry:
            if i >= 0:
                carry += ord(num1[i]) - ord('0')
                i -= 1
            if j >= 0:
                carry += ord(num2[j]) - ord('0')
                j -= 1
            result.append(str(carry % 10))
            carry /= 10
        result.reverse()

        return "".join(result)

    def addStrings2(self, num1, num2):
        """
        :type num1: str
        :type num2: str
        :rtype: str
        """
        length = max(len(num1), len(num2))
        num1 = num1.zfill(length)[::-1]
        num2 = num2.zfill(length)[::-1]
        res, plus = '', 0
        for index, num in enumerate(num1):
            tmp = str(int(num) + int(num2[index]) + plus)
            res += tmp[-1]
            if int(tmp) > 9:
                plus = 1
            else:
                plus = 0
        if plus:
            res += '1'
        return res[::-1]
