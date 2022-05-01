"""

150. Evaluate Reverse Polish Notation
Medium

Evaluate the value of an arithmetic expression in Reverse Polish Notation.

Valid operators are +, -, *, and /. Each operand may be an integer or another expression.

Note that division between two integers should truncate toward zero.

It is guaranteed that the given RPN expression is always valid. That means the expression would always evaluate to a result, and there will not be any division by zero operation.

 

Example 1:

Input: tokens = ["2","1","+","3","*"]
Output: 9
Explanation: ((2 + 1) * 3) = 9
Example 2:

Input: tokens = ["4","13","5","/","+"]
Output: 6
Explanation: (4 + (13 / 5)) = 6
Example 3:

Input: tokens = ["10","6","9","3","+","-11","*","/","*","17","+","5","+"]
Output: 22
Explanation: ((10 * (6 / ((9 + 3) * -11))) + 17) + 5
= ((10 * (6 / (12 * -11))) + 17) + 5
= ((10 * (6 / -132)) + 17) + 5
= ((10 * 0) + 17) + 5
= (0 + 17) + 5
= 17 + 5
= 22
 

Constraints:

1 <= tokens.length <= 104
tokens[i] is either an operator: "+", "-", "*", or "/", or an integer in the range [-200, 200].

"""

# V0
# IDEA : STACK + eval
# https://blog.csdn.net/fuxuemingzhu/article/details/79559703
class Solution(object):
    def evalRPN(self, tokens):
        stack = []
        operators = ['+', '-', '*', '/']
        for token in tokens:
            if token not in operators:
                stack.append(token)
            else:
                b = stack.pop()
                a = stack.pop()
                if token == '/' and int(a) * int(b) < 0:
                    res = eval('-' + '(' + '-' + a + '/' + b + ')')
                else:
                    res = eval(a + token + b)
                stack.append(str(res))
        return int(stack.pop())

# V0'
# IDEA : STACK 
# DEMO : call lambda via dict
# In [13]: ops = {
#     ...:     '+' : lambda y, x: x+y,
#     ...:     '-' : lambda y, x: x-y,
#     ...:     '*' : lambda y, x: x*y,
#     ...:     '/' : lambda y, x: int(x/y)
#     ...: }
#     ...: 
#     ...: a = 3 
#     ...: b = 4 
#     ...: 
#     ...: 
#     ...: for key in ops.keys():
#     ...:     print (ops[key])
#     ...:     print (ops[key](a,b))
#     ...:     
# <function <lambda> at 0x7f8c068c4730>
# 7
# <function <lambda> at 0x7f8c068c4048>
# 1
# <function <lambda> at 0x7f8c068c4a60>
# 12
# <function <lambda> at 0x7f8c068c48c8>
# 1
class Solution:
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        if len(tokens) < 1:
            return None
        ops = {
            '+' : lambda y, x: x+y,
            '-' : lambda y, x: x-y,
            '*' : lambda y, x: x*y,
            '/' : lambda y, x: int(x/y)
        }
        result = []
        for token in tokens:
            if token in ops.keys():
                result.append(ops[token](result.pop(), result.pop()))
            else:
                result.append(int(token))
        return result[0]

# V1
# IDEA : Reducing the List In-place
# https://leetcode.com/problems/evaluate-reverse-polish-notation/solution/
def evalRPN(self, tokens: List[str]) -> int:

        operations = {
            "+": lambda a, b: a + b,
            "-": lambda a, b: a - b,
            "/": lambda a, b: int(a / b),
            "*": lambda a, b: a * b
        }
        
        current_position = 0
        
        while len(tokens) > 1:
            
            # Move the current position pointer to the next operator.
            while tokens[current_position] not in "+-*/":
                current_position += 1
        
            # Extract the operator and numbers from the list.
            operator = tokens[current_position]
            number_1 = int(tokens[current_position - 2])
            number_2 = int(tokens[current_position - 1])
            
            # Calculate the result to overwrite the operator with.
            operation = operations[operator]
            tokens[current_position] = operation(number_1, number_2)
            
            # Remove the numbers and move the pointer to the position
            # after the new number we just added.
            tokens.pop(current_position - 2)
            tokens.pop(current_position - 2)
            current_position -= 1
        
        return tokens[0]

# V1
# IDEA : Reducing the List In-place + NO lambda functions.
# https://leetcode.com/problems/evaluate-reverse-polish-notation/solution/
def evalRPN(self, tokens: List[str]) -> int:

    current_position = 0

    while len(tokens) > 1:
        
        # Move the current position pointer to the next operator.
        while tokens[current_position] not in "+-*/":
            current_position += 1
    
        # Extract the operator and numbers from the list.
        operator = tokens[current_position]
        number_1 = int(tokens[current_position - 2])
        number_2 = int(tokens[current_position - 1])
        
        # Calculate the result to overwrite the operator with.
        if operator == "+":
            tokens[current_position] = number_1 + number_2
        elif operator == "-":
            tokens[current_position] = number_1 - number_2
        elif operator == "*":
            tokens[current_position] = number_1 * number_2
        else:
            tokens[current_position] = int(number_1 / number_2)
        
        # Remove the numbers and move the pointer to the position
        # after the new number we just added.
        tokens.pop(current_position - 2)
        tokens.pop(current_position - 2)
        current_position -= 1
        
    return tokens[0]

# V1
# IDEA : Evaluate with Stack
# https://leetcode.com/problems/evaluate-reverse-polish-notation/solution/
def evalRPN(self, tokens: List[str]) -> int:
        
    operations = {
        "+": lambda a, b: a + b,
        "-": lambda a, b: a - b,
        "/": lambda a, b: int(a / b),
        "*": lambda a, b: a * b
    }
    
    stack = []
    for token in tokens:
        if token in operations:
            number_2 = stack.pop()
            number_1 = stack.pop()
            operation = operations[token]
            stack.append(operation(number_1, number_2))
        else:
            stack.append(int(token))
    return stack.pop()

# V1
# https://leetcode.com/problems/evaluate-reverse-polish-notation/discuss/143004/Python-solution-O(n)-descriptive-solution
# https://leetcode.com/problems/evaluate-reverse-polish-notation/discuss/168183/Python-simple-functional-solution-(no-stack)
# IDEA : STACK
# STEPS:
# 1) lets create a dictionary hashmap of what needs to happen when an operator is encountered.
# 2) traverse through the tokens and catch all the numbers preceeding the operator.
# 3) pop the last two numbers -> perform the operation and store it in the stack.
# 4) keep repeating 2 and 3
# 5) you'll ultimately be left with the result in the stack
class Solution:
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        if len(tokens) < 1:
            return None
        ops = {
            '+' : lambda y, x: x+y,
            '-' : lambda y, x: x-y,
            '*' : lambda y, x: x*y,
            '/' : lambda y, x: int(x/y)
        }
        result = []
        for token in tokens:
            if token in ops.keys():
                result.append(ops[token](result.pop(), result.pop()))
            else:
                result.append(int(token))
        return result[0]

### Test case
s=Solution()
assert s.evalRPN([]) == None 
assert s.evalRPN(["0"]) == 0 
assert s.evalRPN(["-1"]) == -1
assert s.evalRPN(["-0"]) == 0
assert s.evalRPN(["-1","1","-"]) == -2
assert s.evalRPN(["0","1","+"]) == 1 
assert s.evalRPN(["0","1","-"]) == -1 
assert s.evalRPN(["2", "1", "+", "3", "*"]) == 9 
assert s.evalRPN(["4", "13", "5", "/", "+"]) == 6
assert s.evalRPN(["1","1", "*"]) == 1
assert s.evalRPN(["1","1", "1", "*", "+"]) == 2

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79559703
class Solution(object):
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        stack = []
        operators = ['+', '-', '*', '/']
        for token in tokens:
            if token not in operators:
                stack.append(token)
            else:
                b = stack.pop()
                a = stack.pop()
                if token == '/':
                    res = int(operator.truediv(int(a), int(b)))
                else:
                    res = eval(a + token + b)
                stack.append(str(res))
        return int(stack.pop())

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79559703
class Solution(object):
    def evalRPN(self, tokens):
        """
        :type tokens: List[str]
        :rtype: int
        """
        stack = []
        operators = ['+', '-', '*', '/']
        for token in tokens:
            if token not in operators:
                stack.append(token)
            else:
                b = stack.pop()
                a = stack.pop()
                if token == '/' and int(a) * int(b) < 0:
                    res = eval('-' + '(' + '-' + a + '/' + b + ')')
                else:
                    res = eval(a + token + b)
                stack.append(str(res))
        return int(stack.pop())

# V1'''
# https://leetcode.com/problems/evaluate-reverse-polish-notation/discuss/47537/6-7-lines-in-Python
# IDEA : RECURSIVE
class Solution(object):
    def evalRPN(self, tokens):
        t = tokens.pop()
        if t in '+-*/':
            b = self.evalRPN(tokens)
            a = self.evalRPN(tokens)
            t = eval(a+t+b+'.')
        return int(t)

# V1''''
# https://leetcode.com/problems/evaluate-reverse-polish-notation/discuss/47549/A-Python-solution-with-8-lines
class Solution(object):
    def evalRPN(self, tokens):
        stack = []
        ops = {'+':lambda x, y: x+y, '-':lambda x, y: x-y, '*':lambda x, y: x*y, '/':lambda x, y: x/y}
        for s in tokens:
            try:
                stack.append( float( s ) )
            except:
                stack.append( int( ops[s]( stack.pop(-2), stack.pop(-1) ) ) )
        return int( stack[-1] )

# V1'''''
# https://leetcode.com/problems/evaluate-reverse-polish-notation/discuss/47537/6-7-lines-in-Python
# IDEA : ITERATION
class Solution(object):
    def evalRPN(self, tokens):
        stack = []
        for t in tokens:
            if t in '+-*/':
                b, a = stack.pop(), stack.pop()
                t = int(eval(a+t+b+'.'))
            stack += t,
        return int(stack[0]) 

# V2
# Time:  O(n)
# Space: O(n)
import operator
class Solution(object):
    # @param tokens, a list of string
    # @return an integer
    def evalRPN(self, tokens):
        numerals, operators = [], {"+": operator.add, "-": operator.sub, "*": operator.mul, "/": operator.div}
        for token in tokens:
            if token not in operators:
                numerals.append(int(token))
            else:
                y, x = numerals.pop(), numerals.pop()
                numerals.append(int(operators[token](x * 1.0, y)))
        return numerals.pop()