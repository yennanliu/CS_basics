"""

772. Basic Calculator III
Hard

Implement a basic calculator to evaluate a simple expression string.

The expression string may contain open ( and closing parentheses ),
the plus + or minus sign -, non-negative integers and empty spaces .

The expression string contains only non-negative integers, +, -, *, /
operators , open ( and closing parentheses ) and empty spaces .
The integer division should truncate toward zero.

You may assume that the given expression is always valid.
All intermediate results will be in the range of [-2147483648, 2147483647].

Some examples:

"1 + 1" = 2
" 6-4 / 2 " = 4
"2*(5+5*2)/3+(6/2+8)" = 21
"(2+6* 3+5- (3*14/7+2)*5)+3" = -12


Note: Do not use the eval built-in library function.

Lock: Prime

"""

# V0
# IDEA: the `Universal basic calculator` (stack + queue + recursion)
# https://github.com/yennanliu/CS_basics/issues/168
# https://yennj12.js.org/CS_basics/cheatsheets/stack_expression_parsing.html
"""
NOTE !!!

this is an universal algo that work for

LC 224, LC 227, and LC 772 (Basic Calculator III).

-> LC 772 is the `general case`: it is the ONLY one of the three where
   BOTH mechanisms fire.

   | LC  | chars present | what runs                                    |
   |-----|---------------|----------------------------------------------|
   | 224 | + - ( )       | recursion + push/negate; * / branches unused  |
   | 227 | + - * /       | never recurses; pure delay-insert precedence  |
   | 772 | + - * / ( )   | both                                          |

Two independent tricks, one loop:

 1) PRECEDENCE via `delay-insert on pre_op`
    + / -  ->  DEFER: push the signed number onto the stack
    * / /  ->  ACT NOW: pop the top and combine with it
    -> because * and / mutate stack[-1] BEFORE anything is summed,
       sum(stack) at the end respects precedence for free.
       e.g. 2 + 3 * 4 -> stack = [2, 12] -> 14  (not 20)

 2) PARENTHESES via recursion on the SAME queue
    `(` -> recurse; the child keeps popleft()-ing from the shared queue
           and breaks on its matching `)`, so the parent resumes at
           exactly the right char. The sub-total is then just a `curr_num`.

NOTE !!! we act on `pre_op` (the op BEFORE the number), not on the current
         char -- seeing an op / `)` / end-of-input is what tells us the
         number we were building is finished. `op` starts as '+' so the
         very first number is simply pushed.
"""
# time = O(n), space = O(n)  (stack + recursion depth)
import collections


class Solution(object):
    def calculate(self, s):
        """
        :type s: str
        :rtype: int
        """
        # edge
        if not s:
            return 0

        # 1. Remove spaces and convert to a queue for easy left-to-right parsing
        queue = collections.deque(s.replace(" ", ""))

        def helper(q):
            stack = []
            curr_num = 0
            op = '+'  # Default first operation

            while q:
                char = q.popleft()

                if char.isdigit():
                    curr_num = curr_num * 10 + int(char)

                elif char == '(':
                    # RECURSION: Evaluate the parentheses completely first
                    curr_num = helper(q)

                # If we hit an operator, a closing parenthesis, or the queue is empty
                if char in "+-*/" or char == ')' or not q:
                    if op == '+':
                        stack.append(curr_num)
                    elif op == '-':
                        stack.append(-curr_num)
                    elif op == '*':
                        stack.append(stack.pop() * curr_num)
                    elif op == '/':
                        # NOTE !!! truncate toward zero
                        # (python `//` FLOORS: -7 // 2 == -4, but we need -3)
                        stack.append(int(float(stack.pop()) / curr_num))

                    # Reset the current number and update the operator
                    curr_num = 0
                    op = char

                if char == ')':
                    # End of this scope, break and return the sum to the caller
                    break

            return sum(stack)

        return helper(queue)


# V0-1
# IDEA: SAME delay-insert engine, but ITERATIVE -- `(` SUSPENDS the scope
#       onto a stack of frames instead of recursing.
#
#   Justified as a 2nd solution (not a re-spelling): V0's recursion depth is
#   the parenthesis depth, so a pathological "((((...1...))))" can blow
#   python's recursion limit. Here the frames live on the heap instead.
#
#   On '(' : push (stack, op) and start a FRESH scope
#   On ')' : flush the scope, sum it -> that sum becomes the parent's
#            curr_num, and the parent's own (stack, op) is restored
#
# time = O(n), space = O(n)
class Solution2(object):
    def calculate(self, s):
        """
        :type s: str
        :rtype: int
        """
        # edge
        if not s:
            return 0

        s = s.replace(" ", "")

        stack = []       # operands of the CURRENT scope
        frames = []      # suspended (stack, op) of each open parenthesis
        curr_num = 0
        op = '+'

        def flush(stack, op, curr_num):
            if op == '+':
                stack.append(curr_num)
            elif op == '-':
                stack.append(-curr_num)
            elif op == '*':
                stack.append(stack.pop() * curr_num)
            elif op == '/':
                stack.append(int(float(stack.pop()) / curr_num))

        for i, char in enumerate(s):

            if char.isdigit():
                curr_num = curr_num * 10 + int(char)

            elif char == '(':
                # NOTE !!! suspend the scope; `op` is the op waiting on the
                #          value this parenthesis is about to produce
                frames.append((stack, op))
                stack, op, curr_num = [], '+', 0
                continue

            elif char == ')':
                flush(stack, op, curr_num)
                curr_num = sum(stack)          # the sub-total
                stack, op = frames.pop()       # resume the parent scope
                continue

            # an operator ends the number we were building
            if char in "+-*/":
                flush(stack, op, curr_num)
                curr_num = 0
                op = char

        flush(stack, op, curr_num)
        return sum(stack)
