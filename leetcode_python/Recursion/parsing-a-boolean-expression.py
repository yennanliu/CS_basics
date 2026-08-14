"""

1106. Parsing A Boolean Expression
Hard

A boolean expression is an expression that evaluates to either true or false.
It can be in one of the following shapes:

- 't' that evaluates to true.
- 'f' that evaluates to false.
- '!(subExpr)' that evaluates to the logical NOT of the inner expression subExpr.
- '&(subExpr1, subExpr2, ..., subExprn)' that evaluates to the logical AND of the
  inner expressions subExpr1, subExpr2, ..., subExprn where n >= 1.
- '|(subExpr1, subExpr2, ..., subExprn)' that evaluates to the logical OR of the
  inner expressions subExpr1, subExpr2, ..., subExprn where n >= 1.

Given a string expression that represents a boolean expression, return the
evaluation of that expression.

It is guaranteed that the given expression is valid and follows the given rules.


Example 1:

Input: expression = "&(|(f))"
Output: false
Explanation:
First, evaluate |(f) --> f. The expression is now "&(f)".
Then, evaluate &(f) --> f. The expression is now "f".
Finally, return false.

Example 2:

Input: expression = "|(f,f,f,t)"
Output: true
Explanation: The evaluation of (false OR false OR false OR true) is true.

Example 3:

Input: expression = "!(&(f,t))"
Output: true
Explanation:
First, evaluate &(f,t) --> (false AND true) --> false --> f.
The expression is now "!(f)".
Then, evaluate !(f) --> NOT false --> true. We return true.


Constraints:

1 <= expression.length <= 2 * 10^4
expression[i] is one following characters: '(', ')', '&', '|', '!', 't', 'f', and ','.

"""

# V0
# IDEA: STACK (evaluate the inner-most group on every ')')
#
#   push 't' / 'f' / operators onto a stack.
#   when we hit ')', pop the boolean literals of the current group,
#   count how many are true / false, pop the operator, and push
#   back the single resulting literal.
#   '(' and ',' are just separators -> ignored.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def parseBoolExpr(self, expression):
        stack = []

        for c in expression:
            if c in "tf!&|":
                stack.append(c)
            elif c == ")":
                t = 0
                f = 0
                # NOTE !!! drain all literals of this group
                while stack[-1] in "tf":
                    if stack.pop() == "t":
                        t += 1
                    else:
                        f += 1
                op = stack.pop()
                if op == "!":
                    stack.append("t" if f else "f")
                elif op == "&":
                    stack.append("f" if f else "t")
                else:  # op == '|'
                    stack.append("t" if t else "f")
            # '(' and ',' -> skip

        return stack[-1] == "t"


# V1
# IDEA: RECURSIVE DESCENT PARSER (index pointer)
#
#   parse() reads exactly ONE sub expression starting at self.i
#   and returns its boolean value.
#
# time = O(n)
# space = O(n)
#   recursion depth = nesting depth
class Solution(object):
    def parseBoolExpr(self, expression):
        self.i = 0
        self.s = expression

        def parse():
            c = self.s[self.i]
            self.i += 1

            if c == "t":
                return True
            if c == "f":
                return False

            # c is one of '!' / '&' / '|'  -> next char is '('
            self.i += 1  # skip '('
            vals = []
            while self.s[self.i] != ")":
                if self.s[self.i] == ",":
                    self.i += 1
                    continue
                vals.append(parse())
            self.i += 1  # skip ')'

            if c == "!":
                return not vals[0]
            if c == "&":
                return all(vals)
            return any(vals)

        return parse()
