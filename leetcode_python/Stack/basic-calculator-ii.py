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
# IDEA : STACK (gemini)
"""
Steps:

1. loop over s
   maintain `current_num`

2. check the op, based on the op,
   we then calculate and append res or current_num to stack
   -> NOTE !!!
      -> we DON'T append res / current_num to stack immediately;
         but we do it `later` when meet the op

3. sum over res
"""

"""
# Dry run


````markdown
# Dry Run: `s = "3-2*2"`

## Key Idea

When we encounter an operator (`+`, `-`, `*`, `/`):

1. The current number (`num`) is complete.
2. Apply the **previous operator (`pre_op`)** to that number.
3. Store the result in the stack.
4. Update `pre_op` to the current operator.

---

## Initial State

```python
stack = []
pre_op = '+'
num = 0
```

```text
Expression: 3 - 2 * 2
            ^

stack  = []
pre_op = '+'
num    = 0
```

---

# Iteration 1

Current character:

```text
Expression: 3 - 2 * 2
            ^
            '3'
```

Digit:

```python
num = 10 * 0 + 3
```

Result:

```python
num = 3
```

State:

```text
stack  = []
pre_op = '+'
num    = 3
```

No operator encountered yet, so continue.

---

# Iteration 2

Current character:

```text
Expression: 3 - 2 * 2
              ^
              '-'
```

Operator encountered.

Current state before processing:

```python
stack  = []
pre_op = '+'
num    = 3
```

Since:

```python
pre_op == '+'
```

Execute:

```python
stack.append(num)
```

Result:

```python
stack = [3]
```

Visualization:

```text
Processed:

+3

stack = [3]
```

Update:

```python
pre_op = '-'
num = 0
```

State:

```python
stack  = [3]
pre_op = '-'
num    = 0
```

---

# Iteration 3

Current character:

```text
Expression: 3 - 2 * 2
                ^
                '2'
```

Digit:

```python
num = 2
```

State:

```python
stack  = [3]
pre_op = '-'
num    = 2
```

Continue.

---

# Iteration 4

Current character:

```text
Expression: 3 - 2 * 2
                  ^
                  '*'
```

Operator encountered.

Current state:

```python
stack  = [3]
pre_op = '-'
num    = 2
```

Since:

```python
pre_op == '-'
```

Execute:

```python
stack.append(-num)
```

Result:

```python
stack.append(-2)

stack = [3, -2]
```

Visualization:

```text
Processed:

+3
-2

stack = [3, -2]
```

Update:

```python
pre_op = '*'
num = 0
```

State:

```python
stack  = [3, -2]
pre_op = '*'
num    = 0
```

---

# Iteration 5 (Last Character)

Current character:

```text
Expression: 3 - 2 * 2
                      ^
                      '2'
```

Digit:

```python
num = 2
```

State:

```python
stack  = [3, -2]
pre_op = '*'
num    = 2
```

Since this is the last character:

```python
i == len(s) - 1
```

we must process it.

---

## Process `*`

Current state:

```python
stack  = [3, -2]
pre_op = '*'
num    = 2
```

Execute:

```python
stack.append(stack.pop() * num)
```

### Step 1

```python
stack.pop()
```

Returns:

```python
-2
```

Stack becomes:

```python
[3]
```

### Step 2

Multiply:

```python
-2 * 2
```

Result:

```python
-4
```

### Step 3

Append:

```python
stack.append(-4)
```

Stack becomes:

```python
[3, -4]
```

Visualization:

```text
Before:

[3, -2]

Compute:

(-2) * 2 = -4

After:

[3, -4]
```

---

# Final Result

```python
return sum(stack)
```

```python
sum([3, -4])
```

Result:

```python
-1
```

---

# Visual Summary

```text
Expression:

3 - 2 * 2

Step 1:
+3

stack = [3]

--------------------------------

Step 2:
-2

stack = [3, -2]

--------------------------------

Step 3:
(-2) * 2

stack = [3, -4]

--------------------------------

Final:

3 + (-4)
= -1
```

---

# Why `stack.append(-num)`?

The algorithm converts:

```text
3 - 2 + 5
```

into:

```text
[3, -2, 5]
```

Then:

```python
sum(stack)
```

naturally computes:

```text
3 + (-2) + 5
= 6
```

So:

```python
elif pre_op == '-':
    stack.append(-num)
```

means:

> "Store the next number as a negative value."

NOT:

> "Make the previous number negative."
````

"""
class Solution(object):
    def calculate(self, s):
        """
        :type s: str
        :rtype: int
        """
        if not s:
            return 0
            
        stack = []
        current_num = 0
        # Tracks the last operator seen. The first number is implicitly positive.
        operation = '+' 
        
        # We append a dummy character at the end to force the loop to process the very last number
        for i, char in enumerate(s):
            # 1. Build the multi-digit number
            if char.isdigit():
                current_num = current_num * 10 + int(char)


            """
            NOTE !!! 

            we are dealing with `prev` op,
            NOT the cur one
            """    
            # 2. If the character is an operator or 
            # we've reached the last index
            if (not char.isdigit() and char != ' ') or i == len(s) - 1:
                if operation == '+':
                    stack.append(current_num)
                elif operation == '-':
                    stack.append(-current_num)
                elif operation == '*':
                    stack.append(stack.pop() * current_num)
                elif operation == '/':
                    # CRITICAL: In Python, integer division '//' rounds down.
                    # We cast to float and then int to properly truncate toward zero (e.g., -3 // 2 is -2, but int(-3/2) is -1).
                    prev_num = stack.pop()
                    stack.append(int(float(prev_num) / current_num))
                   
                """
                NOTE !!! 

                we save cur op,
                so can be used next time when we meet the other op again
                """    
                # Save the new operator and reset the number builder
                operation = char
                current_num = 0
                
        # 3. Low priority operations (+ and -) are now just a clean sum of the stack elements
        return sum(stack)



# V0
# IDEA : STACK
# NOTE !!! 
#   -> 1) we init pre_op = '+'
#   -> 2) how we deal with cases with pre_op
#         -> for i, each in enumerate(s):
#           -> if i == len(s) - 1 or each in '+-*/':
#              ...
#              -> pre_op = each
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