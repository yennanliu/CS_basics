"""

224. Basic Calculator
Hard

Given a string s representing a valid expression, implement a basic calculator to evaluate it, and return the result of the evaluation.

Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().

 

Example 1:

Input: s = "1 + 1"
Output: 2
Example 2:

Input: s = " 2-1 + 2 "
Output: 3
Example 3:

Input: s = "(1+(4+5+2)-3)+(6+8)"
Output: 23
 

Constraints:

1 <= s.length <= 3 * 105
s consists of digits, '+', '-', '(', ')', and ' '.
s represents a valid expression.
'+' is not used as a unary operation (i.e., "+1" and "+(2 + 3)" is invalid).
'-' could be used as a unary operation (i.e., "-1" and "-(2 + 3)" is valid).
There will be no two consecutive operators in the input.
Every number and running calculation will fit in a signed 32-bit integer.

"""

# V0

# V1
# https://leetcode.com/problems/basic-calculator/discuss/62418/Python-with-stack
# IDEA : STACK
class Solution(object):
    def calculate(self, s):
        res, num, sign, stack = 0, 0, 1, [1]
        for i in s+"+":
            if i.isdigit():
                num = 10*num + int(i)
            elif i in "+-":
                res += num * sign * stack[-1]
                sign = 1 if i=="+" else -1
                num = 0
            elif i == "(":
                stack.append(sign * stack[-1])
                sign = 1
            elif i == ")":
                res += num * sign * stack[-1]
                num = 0
                stack.pop()
        return res

# V1'
# https://leetcode.com/problems/basic-calculator/discuss/196363/Python-solution
# IDEA : STACK
class Solution:
    def calculate(self, s):
        res = 0
        stack = []
        for c in s:
            if c == " ":
                continue
            elif c == "(":
                stack.append(c)
            elif c.isdigit():
                if stack and stack[-1].isdigit():
                    tmp = stack.pop()
                    stack.append(tmp+c)
                else:
                    stack.append(c)
            elif c == ")":
                summ = 0
                tmp = stack.pop()
                while tmp != "(":
                    if tmp == "+":
                        if prev[-1].isdigit():
                            summ += int(prev)
                    elif tmp == "-":
                        if prev[-1].isdigit():
                            summ -= int(prev)
                    prev = tmp 
                    tmp = stack.pop()
                if prev.isdigit():
                    summ += int(prev)
                stack.append(str(summ))
            else:
                stack.append(c)
        res = 0
        while stack:
            tmp = stack.pop()
            if tmp == "+":
                if prev[-1].isdigit():
                    res += int(prev)
            elif tmp == "-":
                if prev[-1].isdigit():
                    res -= int(prev)
            prev = tmp
        if prev[-1].isdigit():
            res += int(prev)
        return res

# V1''
# https://leetcode.com/problems/basic-calculator/discuss/62344/Easy-18-lines-C%2B%2B-16-lines-Python
# IDEA : STACK
class Solution:
    def calculate(self, s):
        total = 0
        i, signs = 0, [1, 1]
        while i < len(s):
            c = s[i]
            if c.isdigit():
                start = i
                while i < len(s) and s[i].isdigit():
                    i += 1
                total += signs.pop() * int(s[start:i])
                continue
            if c in '+-(':
                signs += signs[-1] * (1, -1)[c == '-'],
            elif c == ')':
                signs.pop()
            i += 1
        return total

# V1''''
# https://leetcode.com/problems/basic-calculator/discuss/62483/AC-Python-Solution
# IDEA : STACK
class Solution:
    def calculate(self, s):
        s = '+(+' + s + ')'
        s = s.replace('+-', '-').replace('++', '+') # for the corner case '-5', '+5'
        stack = []
        for i in s:
            if i == ')':
                total = 0
                while stack[-1] != '(':
                    total += int(stack.pop())
                stack.pop()
                sign = 1 if stack.pop() == '+' else -1
                stack.append(sign * total)
            elif i.isdigit() and stack[-1][-1] in '+-0123456789':
                stack[-1] += i
            elif i != ' ':
                stack.append(i)
        return stack[0]

# V2