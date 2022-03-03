"""

1896. Minimum Cost to Change the Final Value of Expression
Hard

You are given a valid boolean expression as a string expression consisting of the characters '1','0','&' (bitwise AND operator),'|' (bitwise OR operator),'(', and ')'.

For example, "()1|1" and "(1)&()" are not valid while "1", "(((1))|(0))", and "1|(0&(1))" are valid expressions.
Return the minimum cost to change the final value of the expression.

For example, if expression = "1|1|(0&0)&1", its value is 1|1|(0&0)&1 = 1|1|0&1 = 1|0&1 = 1&1 = 1. We want to apply operations so that the new expression evaluates to 0.
The cost of changing the final value of an expression is the number of operations performed on the expression. The types of operations are described as follows:

Turn a '1' into a '0'.
Turn a '0' into a '1'.
Turn a '&' into a '|'.
Turn a '|' into a '&'.
Note: '&' does not take precedence over '|' in the order of calculation. Evaluate parentheses first, then in left-to-right order.

 

Example 1:

Input: expression = "1&(0|1)"
Output: 1
Explanation: We can turn "1&(0|1)" into "1&(0&1)" by changing the '|' to a '&' using 1 operation.
The new expression evaluates to 0. 
Example 2:

Input: expression = "(0&0)&(0&0&0)"
Output: 3
Explanation: We can turn "(0&0)&(0&0&0)" into "(0|1)|(0&0&0)" using 3 operations.
The new expression evaluates to 1.
Example 3:

Input: expression = "(0|(1|0&1))"
Output: 1
Explanation: We can turn "(0|(1|0&1))" into "(0|(0|0&1))" using 1 operation.
The new expression evaluates to 0.
 

Constraints:

1 <= expression.length <= 105
expression only contains '1','0','&','|','(', and ')'
All parentheses are properly matched.
There will be no empty parentheses (i.e: "()" is not a substring of expression).

"""

# V0

# V1
# IDEA : DFS
# https://leetcode.com/problems/minimum-cost-to-change-the-final-value-of-expression/discuss/1267304/Python-Recursion-dfs-solution-explained
class Solution:
    def minOperationsToFlip(self, E):
        def corr(s):
            stack, d = [], {}
            for i, elem in enumerate(s):
                if elem == "(":
                    stack.append(i)
                elif elem == ")":
                    last = stack.pop()
                    d[i] = last
            return d

        def dfs(beg, end):
            if beg == end: return (int(E[beg]) , 1)
            beg2 = d.get(end, end)
            if beg2 == beg: return dfs(beg + 1, end - 1)
            p1, c1 = dfs(beg, beg2 - 2)
            p2, c2 = dfs(beg2, end)
            op = E[beg2 - 1]
            
            t = {"|": lambda x, y:x|y, "&": lambda x, y:x&y}
            c3 = 1 if p1 + p2 == 1 else min(c1, c2) + (p1^(op == "&"))
            return (t[op](p1, p2), c3)
            
        d = corr(E)
        return dfs(0, len(E) - 1)[1]

# V1'
# IDEA : STACK
# https://leetcode.com/problems/minimum-cost-to-change-the-final-value-of-expression/discuss/1653211/Python-simple-stack-solution-with-explanation-or-100
class Solution:
    def minOperationsToFlip(self, expression: str) -> int:
        # This calculates expression 'left' ('&' or '|') 'right', and minimum cost to flip
        # op: None or '&' or '|'
        # left, right: (expression_value, min_flip)
        # return: (expression_value, min_flip)
        def calculate(op: Optional[str], left: Tuple[bool, int], right: Tuple[bool, int]) -> Tuple[bool, int]:
            if not op:
                return right
            if op == '|':
                if left[0] and right[0]:
                    # Change '|' to '&', and flip left or right expression value True to False
                    return (True, min(left[1], right[1]) + 1)
                if left[0] or right[0]:
                    # Change '|' to '&'
                    return (True, 1)
                # Flip left or right expression value False to True
                return (False, min(left[1], right[1]))
            
            if not left[0] and not right[0]:
                # Change '&' to '|', and flip left or right expression value False to True
                return (False, min(left[1], right[1]) + 1)
            if not left[0] or not right[0]:
                # Change '&' to '|'
                return (False, 1)
            # Flip left or right expression value False to True
            return (True, min(left[1], right[1]))

        st = []
        op, cur = None, (True, -1)
        for ch in expression:
            if ch.isdigit():
                cur = calculate(op, cur, (ch == '1', 1))
            elif ch == '&' or ch == '|':
                op = ch
            elif ch == '(':
                st.append((op, cur))
                op = None
            else:
                op, prev = st.pop()
                cur = calculate(op, prev, cur)
        return cur[1]

# V1''
# IDEA : STACK
# https://leetcode.com/problems/minimum-cost-to-change-the-final-value-of-expression/discuss/1267651/Python3.-O(N)-iterative-with-using-stack
class Solution:
    def minOperationsToFlip(self, expression: str) -> int:                              
        stack = [[0, 0, None]]
        for e in expression:                                                    
            if e == "(":                                                        
                stack.append([0, 0, None])                                      # new nested bracket, add empty stack element
            elif e in { ")", "0", "1" }:                                        # if we have two expressions to combine
    
                if e == ")":                                                    # if ")", we can combine child with parent
                    dp0, dp1 = stack[-1][0], stack[-1][1]                       # remember nested bracket result as dp0 and dp1
                    stack.pop()                                                 # and pop it
                else:                                                           # if e is "0" or "1", consider it as nested expression
                    dp0, dp1 = int(e != "0"), int(e != "1")                     # and find dp0 and dp1

                if stack[-1][2] == "&":                                         # now combine results, look at point 3 in the list above
                    stack[-1] = [
                        min(stack[-1][0], dp0),                                 # dp0
                        min(stack[-1][1] + dp1, min(stack[-1][1], dp1) + 1),    # dp1
                        None                                                    # reset operation
                    ]
                elif stack[-1][2] == "|":
                    stack[-1] = [
                        min(stack[-1][0] + dp0, min(stack[-1][0], dp0) + 1),    # dp0
                        min(stack[-1][1], dp1),                                 # dp1
                        None                                                    # reset operation
                    ]
                else:                                                           # if operation is not set, then it's the first expression in nested brackets
                    stack[-1] = [dp0, dp1, None]                                # so just set dp0 and dp1 as last element of stack
            elif e in { "&", "|" }:                                             # if e is some of operations
                stack[-1][2] = e                                                # set the operation in the last element of stack
        return max(stack[0][0], stack[0][1])                  
        
# V1''
# IDEA : DP
# https://leetcode.com/problems/minimum-cost-to-change-the-final-value-of-expression/discuss/1267522/Python-DP-O(n)
class Solution:
    def minOperationsToFlip(self, exp: str) -> int:
        def solve(stack):
            a, op, b = stack
            if op == '&':
                if a[0] == '1' and b[0] == '1':
                    return ('1', min(a[1], b[1]))
                elif a[0] == '1' and b[0] == '0':
                    return ('0', min(b[1], 1))
                if a[0] == '0' and b[0] == '1':
                    return ('0', min(a[1], 1))
                if a[0] == '0' and b[0] == '0':
                    return ('0', min(1+a[1], 1+b[1], a[1]+b[1]))
                    
            elif op == '|':
                if a[0] == '1' and b[0] == '1':
                    return ('1', min(a[1]+b[1], 1+a[1], 1+b[1]))
                elif a[0] == '1' and b[0] == '0':
                    return ('1', min(a[1], 1))
                if a[0] == '0' and b[0] == '1':
                    return ('1', min(b[1], 1))
                if a[0] == '0' and b[0] == '0':
                    return ('0', min(a[1], b[1]))
            
        stack = [[]]
        for i, ch in enumerate(exp):
            if ch == '(':
                stack.append([])
            elif ch == ')':
                res = stack.pop()
                stack[-1].append(res[-1])
            elif ch == '0' or ch == '1':
                stack[-1].append((ch, 1))
            else:
                stack[-1].append(ch)
                
            if len(stack[-1]) == 3:
                res = solve(stack[-1])
                stack[-1].pop()
                stack[-1].pop()
                stack[-1].pop()
                stack[-1].append(res)

        return stack[-1][-1][1]

# V2