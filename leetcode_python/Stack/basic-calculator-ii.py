"""

227. Basic Calculator II
Medium

Given a string s which represents an expression, evaluate this expression and return its value. 

The integer division should truncate toward zero.

You may assume that the given expression is always valid. All intermediate results will be in the range of [-231, 231 - 1].

Note: You are not allowed to use any built-in function which evaluates strings as mathematical expressions, such as eval().

 

Example 1:

Input: s = "3+2*2"
Output: 7
Example 2:

Input: s = " 3/2 "
Output: 1
Example 3:

Input: s = " 3+5 / 2 "
Output: 5
 

Constraints:

1 <= s.length <= 3 * 105
s consists of integers and operators ('+', '-', '*', '/') separated by some number of spaces.
s represents a valid expression.
All the integers in the expression are non-negative integers in the range [0, 231 - 1].
The answer is guaranteed to fit in a 32-bit integer

"""

# V0
# IDEA : STACK
class Solution:
    def calculate(self, s):
        stack = []
        # NOTE THIS !!! we init pre_op as "+"
        pre_op = '+'
        num = 0
        for i, each in enumerate(s):
            # case 1) : digit
            if each.isdigit():
                num = 10 * num + int(each)  # the way to deal with number like "100", "10"... 
            """
            ### NOTE !!!
                1) use if (instead of elif)
                2) if i == len(s) - 1 or each in '+-*/' condition
            """
            if i == len(s) - 1 or each in '+-*/':
                """
                NOTE !!! : we deal with "pre_op" (rather than current op)
                """
                # case 2) : "+"
                if pre_op == '+':
                    stack.append(num)   # since pre_op init as "+", so here we can append first num to stack
                # case 3) : "-"    
                elif pre_op == '-':
                    stack.append(-num)
                # case 3) : "*" 
                elif pre_op == '*':
                    stack.append(stack.pop() * num)
                # case 3) : "/" 
                elif pre_op == '/':
                    top = stack.pop()
                    if top < 0:
                        stack.append(int(top / num))
                    else:
                        stack.append(top // num)
                """
                NOTE this !!!!
                """
                pre_op = each  # get pre_op from each
                num = 0   # init num
        return sum(stack)

# V1
# IDEA : STACK
# https://blog.csdn.net/fuxuemingzhu/article/details/80826333
# DEMO : 
# In [51]: num = 0
#     ...: s = '100'
#     ...: for i, each in enumerate(s):
#     ...:             if each.isdigit():
#     ...:                 num = 10 * num + int(each)
#     ...:                 print (num)
#     ...: 
# 1
# 10
# 100
# python 3
class Solution:
    def calculate(self, s):
        stack = []
        # NOTE THIS !!
        pre_op = '+'
        num = 0
        for i, each in enumerate(s):
            # case 1) : digit
            if each.isdigit():
                num = 10 * num + int(each)  # the way to deal with number like "100", "10"... 
            if i == len(s) - 1 or each in '+-*/':
                """
                NOTE !!! : we deal with "pre_op" (rather than current op)
                """
                # case 2) : "+"
                if pre_op == '+':
                    stack.append(num)
                # case 3) : "-"    
                elif pre_op == '-':
                    stack.append(-num)
                # case 3) : "*" 
                elif pre_op == '*':
                    stack.append(stack.pop() * num)
                # case 3) : "/" 
                elif pre_op == '/':
                    top = stack.pop()
                    if top < 0:
                        stack.append(int(top / num))
                    else:
                        stack.append(top // num)
                # NOTE this!
                pre_op = each
                num = 0
        return sum(stack)

# V1'
# https://www.jiuzhang.com/solution/basic-calculator-ii/#tag-highlight-lang-python
class Solution:
    def calculate(self, s):
        # Write your code here
        if not s:
            return "0"
        stack, num, sign = [], 0, "+"
        for i in range(len(s)):
            if s[i].isdigit():
                num = num*10+ord(s[i])-ord("0")
            if (not s[i].isdigit() and not s[i].isspace()) or i == len(s)-1:
                if sign == "-":
                    stack.append(-num)
                elif sign == "+":
                    stack.append(num)
                elif sign == "*":
                    stack.append(stack.pop()*num)
                else:
                    tmp = stack.pop()
                    if tmp//num < 0 and tmp%num != 0:
                        stack.append(tmp//num+1)
                    else:
                        stack.append(tmp//num)
                sign = s[i]
                num = 0
        return sum(stack)

# V1''
# IDEA : STACK
# https://leetcode.com/problems/basic-calculator-ii/discuss/1492325/Python-clean-stack-solution-(easy-understanding)
class Solution:
    def calculate(self, s: str) -> int:
        num, ope, stack = 0, '+', []
        
        for cnt, i in enumerate(s):
            if i.isnumeric():
                num = num * 10 + int(i)
            if i in '+-*/' or cnt == len(s) - 1:
                if ope == '+':
                    stack.append(num)
                elif ope == '-':
                    stack.append(-num)
                elif ope == '*':
                    j = stack.pop() * num
                    stack.append(j)
                elif ope == '/':
                    j = int(stack.pop() / num)
                    stack.append(j)
            
                ope = i
                num = 0
       
        return sum(stack)

# V1'''
# IDEA : STACK
# https://leetcode.com/problems/basic-calculator-ii/discuss/63076/Python-short-solution-with-stack.
class Solution:
    def calculate(self, s):
        if not s:
            return "0"
        stack, num, sign = [], 0, "+"
        for i in range(len(s)):
            if s[i].isdigit():
                num = num*10+ord(s[i])-ord("0")
            if (not s[i].isdigit() and not s[i].isspace()) or i == len(s)-1:
                if sign == "-":
                    stack.append(-num)
                elif sign == "+":
                    stack.append(num)
                elif sign == "*":
                    stack.append(stack.pop()*num)
                else:
                    tmp = stack.pop()
                    if tmp//num < 0 and tmp%num != 0:
                        stack.append(tmp//num+1)
                    else:
                        stack.append(tmp//num)
                sign = s[i]
                num = 0
        return sum(stack)

# V1''''
# IDEA : STACK
class Solution:
    def calculate(self, s):
        num, op, stack = 0, '+', [0]
        ops = {'+':lambda x, y: y, '-':lambda x, y: -y, '*':lambda x, y: x*y, '/':lambda x, y: (int)(float(x)/float(y))}
        for i, c in enumerate(s):
            if c.isdigit():
                num = num * 10 + int(c)
            if not c.isdigit() and c != ' ' or i == len(s)-1:
                prev = 0 if op in '+-' else stack.pop()
                stack.append(ops[op](prev, num))
                num, op = 0, c
        return sum(stack)

# V1'''''
# IDEA : STACK
# https://leetcode.com/problems/basic-calculator-ii/solution/
# JAVA
# class Solution {
#     public int calculate(String s) {
#
#         if (s == null || s.isEmpty()) return 0;
#         int len = s.length();
#         Stack<Integer> stack = new Stack<Integer>();
#         int currentNumber = 0;
#         char operation = '+';
#         for (int i = 0; i < len; i++) {
#             char currentChar = s.charAt(i);
#             if (Character.isDigit(currentChar)) {
#                 currentNumber = (currentNumber * 10) + (currentChar - '0');
#             }
#             if (!Character.isDigit(currentChar) && !Character.isWhitespace(currentChar) || i == len - 1) {
#                 if (operation == '-') {
#                     stack.push(-currentNumber);
#                 }
#                 else if (operation == '+') {
#                     stack.push(currentNumber);
#                 }
#                 else if (operation == '*') {
#                     stack.push(stack.pop() * currentNumber);
#                 }
#                 else if (operation == '/') {
#                     stack.push(stack.pop() / currentNumber);
#                 }
#                 operation = currentChar;
#                 currentNumber = 0;
#             }
#         }
#         int result = 0;
#         while (!stack.isEmpty()) {
#             result += stack.pop();
#         }
#         return result;
#     }
# }

# V1''''''''
# IDEA : Optimised Approach without the stack
# https://leetcode.com/problems/basic-calculator-ii/solution/
# JAVA
# class Solution {
#     public int calculate(String s) {
#         if (s == null || s.isEmpty()) return 0;
#         int length = s.length();
#         int currentNumber = 0, lastNumber = 0, result = 0;
#         char operation = '+';
#         for (int i = 0; i < length; i++) {
#             char currentChar = s.charAt(i);
#             if (Character.isDigit(currentChar)) {
#                 currentNumber = (currentNumber * 10) + (currentChar - '0');
#             }
#             if (!Character.isDigit(currentChar) && !Character.isWhitespace(currentChar) || i == length - 1) {
#                 if (operation == '+' || operation == '-') {
#                     result += lastNumber;
#                     lastNumber = (operation == '+') ? currentNumber : -currentNumber;
#                 } else if (operation == '*') {
#                     lastNumber = lastNumber * currentNumber;
#                 } else if (operation == '/') {
#                     lastNumber = lastNumber / currentNumber;
#                 }
#                 operation = currentChar;
#                 currentNumber = 0;
#             }
#         }
#         result += lastNumber;
#         return result;
#     }
# }

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    # @param {string} s
    # @return {integer}
    def calculate(self, s):
        operands, operators = [], []
        operand = ""
        for i in reversed(range(len(s))):
            if s[i].isdigit():
                operand += s[i]
                if i == 0 or not s[i-1].isdigit():
                    operands.append(int(operand[::-1]))
                    operand = ""
            elif s[i] == ')' or s[i] == '*' or s[i] == '/':
                operators.append(s[i])
            elif s[i] == '+' or s[i] == '-':
                while operators and \
                      (operators[-1] == '*' or operators[-1] == '/'):
                    self.compute(operands, operators)
                operators.append(s[i])
            elif s[i] == '(':
                while operators[-1] != ')':
                    self.compute(operands, operators)
                operators.pop()

        while operators:
            self.compute(operands, operators)

        return operands[-1]

    def compute(self, operands, operators):
        left, right = operands.pop(), operands.pop()
        op = operators.pop()
        if op == '+':
            operands.append(left + right)
        elif op == '-':
            operands.append(left - right)
        elif op == '*':
            operands.append(left * right)
        elif op == '/':
            operands.append(left / right)