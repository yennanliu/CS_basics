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
# IDEA : STACK
class Solution(object):
    def calculate(self, s):
        res = 0
        num = 0
        sign = 1
        stack = [1]
        s += "+"
        for i in s:
            if i.isdigit():
                num = 10*num + int(i)
            elif i in "+-":
                res += num * sign * stack[-1]
                #sign = 1 if i=="+" else -1
                if i == "+":
                    sign = 1
                else:
                    sign = -1
                num = 0
            elif i == "(":
                stack.append(sign * stack[-1])
                sign = 1
            elif i == ")":
                res += num * sign * stack[-1]
                num = 0
                stack.pop(-1)
        return res

# V0'
# IDEA : STACK
class Solution:
    def calculate(self, s):

        stack = []
        operand = 0
        res = 0 # For the on-going result
        sign = 1 # 1 means positive, -1 means negative  

        for ch in s:
            if ch.isdigit():

                # Forming operand, since it could be more than one digit
                operand = (operand * 10) + int(ch)

            elif ch == '+':

                # Evaluate the expression to the left,
                # with result, sign, operand
                res += sign * operand

                # Save the recently encountered '+' sign
                sign = 1

                # Reset operand
                operand = 0

            elif ch == '-':

                res += sign * operand
                sign = -1
                operand = 0

            elif ch == '(':

                # Push the result and sign on to the stack, for later
                # We push the result first, then sign
                stack.append(res)
                stack.append(sign)

                # Reset operand and result, as if new evaluation begins for the new sub-expression
                sign = 1
                res = 0

            elif ch == ')':

                # Evaluate the expression to the left
                # with result, sign and operand
                res += sign * operand

                # ')' marks end of expression within a set of parenthesis
                # Its result is multiplied with sign on top of stack
                # as stack.pop() is the sign before the parenthesis
                res *= stack.pop() # stack pop 1, sign

                # Then add to the next operand on the top.
                # as stack.pop() is the result calculated before this parenthesis
                # (operand on stack) + (sign on stack * (result from parenthesis))
                res += stack.pop() # stack pop 2, operand

                # Reset the operand
                operand = 0

        return res + sign * operand

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

# V1
# IDEA : Stack and String Reversal
# https://leetcode.com/problems/basic-calculator/solution/
class Solution:

    def evaluate_expr(self, stack):
        
        # If stack is empty or the expression starts with
        # a symbol, then append 0 to the stack.
        # i.e. [1, '-', 2, '-'] becomes [1, '-', 2, '-', 0]
        if not stack or type(stack[-1]) == str:
            stack.append(0)
            
        res = stack.pop()

        # Evaluate the expression till we get corresponding ')'
        while stack and stack[-1] != ')':
            sign = stack.pop()
            if sign == '+':
                res += stack.pop()
            else:
                res -= stack.pop()
        return res       

    def calculate(self, s: str) -> int:

        stack = []
        n, operand = 0, 0

        for i in range(len(s) - 1, -1, -1):
            ch = s[i]

            if ch.isdigit():

                # Forming the operand - in reverse order.
                operand = (10**n * int(ch)) + operand
                n += 1

            elif ch != " ":
                if n:
                    # Save the operand on the stack
                    # As we encounter some non-digit.
                    stack.append(operand)
                    n, operand = 0, 0

                if ch == '(':         
                    res = self.evaluate_expr(stack)
                    stack.pop()        

                    # Append the evaluated result to the stack.
                    # This result could be of a sub-expression within the parenthesis.
                    stack.append(res)

                # For other non-digits just push onto the stack.
                else:
                    stack.append(ch)

        # Push the last operand to stack, if any.
        if n:
            stack.append(operand)

        # Evaluate any left overs in the stack.
        return self.evaluate_expr(stack)

# V1
# IDEA : Stack and No String Reversal
# https://leetcode.com/problems/basic-calculator/solution/
class Solution:
    def calculate(self, s: str) -> int:

        stack = []
        operand = 0
        res = 0 # For the on-going result
        sign = 1 # 1 means positive, -1 means negative  

        for ch in s:
            if ch.isdigit():

                # Forming operand, since it could be more than one digit
                operand = (operand * 10) + int(ch)

            elif ch == '+':

                # Evaluate the expression to the left,
                # with result, sign, operand
                res += sign * operand

                # Save the recently encountered '+' sign
                sign = 1

                # Reset operand
                operand = 0

            elif ch == '-':

                res += sign * operand
                sign = -1
                operand = 0

            elif ch == '(':

                # Push the result and sign on to the stack, for later
                # We push the result first, then sign
                stack.append(res)
                stack.append(sign)

                # Reset operand and result, as if new evaluation begins for the new sub-expression
                sign = 1
                res = 0

            elif ch == ')':

                # Evaluate the expression to the left
                # with result, sign and operand
                res += sign * operand

                # ')' marks end of expression within a set of parenthesis
                # Its result is multiplied with sign on top of stack
                # as stack.pop() is the sign before the parenthesis
                res *= stack.pop() # stack pop 1, sign

                # Then add to the next operand on the top.
                # as stack.pop() is the result calculated before this parenthesis
                # (operand on stack) + (sign on stack * (result from parenthesis))
                res += stack.pop() # stack pop 2, operand

                # Reset the operand
                operand = 0

        return res + sign * operand

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