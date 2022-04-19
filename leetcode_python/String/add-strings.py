"""

415. Add Strings
Easy

Given two non-negative integers, num1 and num2 represented as string, return the sum of num1 and num2 as a string.

You must solve the problem without using any built-in library for handling large integers (such as BigInteger). You must also not convert the inputs to integers directly.

Example 1:

Input: num1 = "11", num2 = "123"
Output: "134"
Example 2:

Input: num1 = "456", num2 = "77"
Output: "533"
Example 3:

Input: num1 = "0", num2 = "0"
Output: "0"
 

Constraints:

1 <= num1.length, num2.length <= 104
num1 and num2 consist of only digits.
num1 and num2 don't have any leading zeros except for the zero itself.

"""

# V0
# IDEA : string + math
class Solution(object):
    def addStrings(self, num1, num2):
        result = []
        # note : we init carry as 0
        carry = 0
        num1 = list(num1)
        num2 = list(num2)
        # while there is still non-add digit in num1, and num2; or there is non-zero carry 
        while num1 or num2 or carry:
            digit = carry
            if num1:
                tmp1 = num1.pop(-1)
                digit += int(tmp1)
            if num2:
                tmp2 = num2.pop(-1)
                digit += int(tmp2)
            """
            if digit > 9 -> we need to "carry" 1 to next digit -> carry = 1
            else -> carry = 0
            """
            if digit > 9:
                carry = 1
            else:
                carry = 0
            # NOTE !!! we get "remain" by 10 via below code
            result.append(str(digit % 10))
        return ''.join(result[::-1]) 

# V0'
# IDEA : string + math
class Solution(object):
    def addStrings(self, num1, num2):
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
# IDEA : MATH
# https://leetcode.com/problems/add-strings/solution/
"""

Facebook interviewers like this question and propose it in four main variations. The choice of algorithm should be based on the input format:

Strings (the current problem). Use schoolbook digit-by-digit addition. Note, that to fit into constant space is not possible for languages with immutable strings, for example, for Java and Python. Here are two examples:

Add Binary: sum two binary strings.

Add Strings: sum two non-negative numbers in a string representation without converting them to integers directly.

Integers. Usually, the interviewer would ask you to implement a sum without using + and - operators. Use bit manipulation approach. Here is an example:

Sum of Two Integers: Sum two integers without using + and - operators.
Arrays. The same textbook addition. Here is an example:

Add to Array Form of Integer.
Linked Lists. Sentinel Head + Textbook Addition. Here are some examples:

Plus One.

Add Two Numbers.

Add Two Numbers II.

"""
class Solution:
    def addStrings(self, num1: str, num2: str) -> str:
        res = []

        carry = 0
        p1 = len(num1) - 1
        p2 = len(num2) - 1
        while p1 >= 0 or p2 >= 0:
            x1 = ord(num1[p1]) - ord('0') if p1 >= 0 else 0
            x2 = ord(num2[p2]) - ord('0') if p2 >= 0 else 0
            value = (x1 + x2 + carry) % 10
            carry = (x1 + x2 + carry) // 10
            res.append(value)
            p1 -= 1
            p2 -= 1
        
        if carry:
            res.append(carry)
        
        return ''.join(str(x) for x in res[::-1])

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