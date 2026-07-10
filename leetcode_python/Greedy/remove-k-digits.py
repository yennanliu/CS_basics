"""

402. Remove K Digits
Solved
Medium
Topics
premium lock icon
Companies
Given string num representing a non-negative integer num, and an integer k, return the smallest possible integer after removing k digits from num.

 
Example 1:

Input: num = "1432219", k = 3
Output: "1219"
Explanation: Remove the three digits 4, 3, and 2 to form the new number 1219 which is the smallest.
Example 2:

Input: num = "10200", k = 1
Output: "200"
Explanation: Remove the leading 1 and the number is 200. Note that the output must not contain leading zeroes.
Example 3:

Input: num = "10", k = 2
Output: "0"
Explanation: Remove all the digits from the number and it is left with nothing which is 0.
 

Constraints:

1 <= k <= num.length <= 105
num consists of only digits.
num does not have any leading zeros except for the zero itself.
 


"""


# V0
# IDEA: STACK (stack[-1] > digit) (gpt)
"""

Dry run


num = "1432219"
k = 3

Process:

1
14
143   <-- 4 > 3, pop 4

13
132   <-- 3 > 2, pop 3

122
1221  <-- 2 > 1, pop 2

1219

Answer:

1219

"""
class Solution(object):
    def removeKdigits(self, num, k):

        # Edge case: if we remove all digits, return "0"
        if len(num) == k:
            return "0"
            
        st = []
        
        for digit in num:
            # Pop from stack if the top is GREATER than the current digit
            # and we still have operations left (k > 0)
            while k > 0 and st and st[-1] > digit:
                st.pop()
                k -= 1
            st.append(digit)
            
        # If we still have k left to remove (e.g., num was "1234")
        # remove them from the END of the stack
        if k > 0:
            st = st[:-k]
            
        # Convert stack back to string and strip leading zeros
        res = "".join(st).lstrip("0")
        
        # If stripping left us with an empty string, return "0"
        return res if res else "0"

# V0-1
# IDEA: STACK (stack[-1] > digit) (gpt)
"""

Dry run


num = "1432219"
k = 3

Process:

1
14
143   <-- 4 > 3, pop 4

13
132   <-- 3 > 2, pop 3

122
1221  <-- 2 > 1, pop 2

1219

Answer:

1219

"""
class Solution(object):
    def removeKdigits(self, num, k):
        stack = []

        for digit in num:
            """
            # Pop from stack if the top is GREATER 
              than the current digit

            # and we still have operations left (k > 0)
            """
            while k > 0 and stack and stack[-1] > digit:
                stack.pop()
                k -= 1

            stack.append(digit)

        # If we still have k left to remove (e.g., num was "1234")
        # remove them from the END of the stack
        
        # If removals remain, remove from the end
        while k > 0:
            stack.pop()
            k -= 1

        # Remove leading zeros
        res = "".join(stack).lstrip('0')

        return res if res else "0"



# V0-2
# IDEA: STACK (GEMINI)
class Solution(object):
    def removeKdigits(self, num, k):
        """
        :type num: str
        :type k: int
        :rtype: str
        """
        # Edge Case: If we need to remove all digits, the result is "0"
        if len(num) == k:
            return "0"
            
        stack = []
        
        for digit in num:
            # While we still have removals left (k > 0) AND the stack is not empty
            # AND the top of the stack is GREATER than the current digit (a peak)
            while k > 0 and stack and stack[-1] > digit:
                stack.pop()  # Remove the peak digit
                k -= 1
            stack.append(digit)
            
        # If we still have removals left (e.g., for a non-decreasing number like "12345")
        # remove the remaining digits from the end
        if k > 0:
            stack = stack[:-k]
            
        # Convert the stack back to a string and strip leading zeros
        # e.g., "0200" -> "200"
        res = "".join(stack).lstrip('0')
        
        # If stripping leading zeros leaves an empty string, return "0"
        return res if res else "0"


# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81034522
# IDEA : STACK 
class Solution(object):
    def removeKdigits(self, num, k):
        """
        :type num: str
        :type k: int
        :rtype: str
        """
        if len(num) == k:
            return '0'
        stack = []
        for n in num:
            while stack and k and int(stack[-1]) > int(n):
                stack.pop()
                k -= 1
            stack.append(n)
        while k:
            stack.pop()
            k -= 1
        if not stack:
            return '0'
        return str(int("".join(stack)))
       
# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def removeKdigits(self, num, k):
        """
        :type num: str
        :type k: int
        :rtype: str
        """
        result = []
        for d in num:
            while k and result and result[-1] > d:
                result.pop()
                k -= 1
            result.append(d)
        return ''.join(result).lstrip('0')[:-k or None] or '0'
