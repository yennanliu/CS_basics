# Add `xxx` to Sum

- https://leetcode.com/problems/add-strings/solution/

`Facebook` interviewers like this question and propose it in four main variations. The choice of algorithm should be based on the input format:

1. Strings (the current problem). Use schoolbook digit-by-digit addition. Note, that to fit into constant space is not possible for languages with immutable strings, for example, for Java and Python. Here are two examples:
- LC 067 : Add Binary: sum two binary strings.
- LC 415 : Add Strings: sum two non-negative numbers in a string representation without converting them to integers directly.

2. Integers. Usually, the interviewer would ask you to implement a sum without using + and - operators. Use bit manipulation approach. Here is an example:
- LC 371 : Sum of Two Integers: Sum two integers without using + and - operators.

3. Arrays. The same textbook addition. Here is an example:
- LC 989 : Add to Array Form of Integer.

4. Linked Lists. Sentinel Head + Textbook Addition. Here are some examples:
- LC 66 : Plus One.
- LC 002 : Add Two Numbers.
- LC 445 : Add Two Numbers II.


## 0) Concept  

### 0-1) Types

### 0-2) Pattern

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Add Strings
```python
# LC 415. Add Strings
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
```