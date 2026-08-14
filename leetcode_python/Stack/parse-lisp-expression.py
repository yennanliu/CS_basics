"""

736. Parse Lisp Expression
Hard

You are given a string expression representing a Lisp-like expression to return
the integer value of.

The syntax for these expressions is given as follows.

  - An expression is either an integer, let expression, add expression, mult expression,
    or an assigned variable. Expressions always evaluate to a single integer.
  - (An integer could be positive or negative.)
  - A let expression takes the form "(let v1 e1 v2 e2 ... vn en expr)", where let is
    always the string "let", then there are one or more pairs of alternating variables
    and expressions, meaning that the first variable v1 is assigned the value of the
    expression e1, the second variable v2 is assigned the value of the expression e2,
    and so on sequentially; and then the value of this let expression is the value of
    the expression expr.
  - An add expression takes the form "(add e1 e2)" where add is always the string "add",
    there are always two expressions e1, e2 and the result is the addition of the
    evaluation of e1 and the evaluation of e2.
  - A mult expression takes the form "(mult e1 e2)" where mult is always the string
    "mult", there are always two expressions e1, e2 and the result is the multiplication
    of the evaluation of e1 and the evaluation of e2.
  - For this question, we will use a smaller subset of variable names. A variable starts
    with a lowercase letter, then zero or more lowercase letters or digits. Additionally,
    the names "add", "let", and "mult" are protected and will never be used as
    variable names.
  - Finally, there is the concept of scope. When an expression of a variable name is
    evaluated, within the context of that evaluation, the innermost scope (in terms of
    parentheses) is checked first for the value of that variable, and then outer scopes
    are checked sequentially. It is guaranteed that every expression is legal.


Example 1:

Input: expression = "(let x 2 (mult x (let x 3 y 4 (add x y))))"
Output: 14
Explanation: In the expression (add x y), when checking for the value of the variable x,
we check from the innermost scope to the outermost in the context of the variable we are
trying to evaluate. Since x = 3 is found first, the value of x is 3.

Example 2:

Input: expression = "(let x 3 x 2 x)"
Output: 2
Explanation: Assignment in let statements is processed sequentially.

Example 3:

Input: expression = "(let x 1 y 2 x (add x y) (add x y))"
Output: 5
Explanation: The first (add x y) evaluates as 3, and is assigned to x.
The second (add x y) evaluates as 3+2 = 5.


Constraints:

1 <= expression.length <= 2000
There are no leading or trailing spaces in expression.
All tokens are separated by a single space in expression.
The answer and all intermediate calculations of that answer are guaranteed to fit
in a 32-bit integer.
The expression is guaranteed to be legal and evaluate to an integer.

"""

# V0
# IDEA : RECURSION + SCOPE STACK
#
#   scope: dict var -> stack of values. Pushing on entry to a `let` and popping on
#   exit gives exactly the "innermost scope wins" rule for free — scope[v][-1] is
#   always the value from the closest enclosing binding.
#
#   split_tokens() splits the body of a "( ... )" on TOP LEVEL spaces only
#   (tracking paren depth), so a nested sub-expression stays one single token.
#
# time = O(n^2) worst case (n = len(expression)) — each level re-slices its body
# space = O(n)
class Solution(object):
    def evaluate(self, expression):

        def split_tokens(body):
            """split on spaces at paren depth 0 -> nested exprs stay whole"""
            tokens = []
            depth = 0
            cur = []
            for ch in body:
                if ch == "(":
                    depth += 1
                elif ch == ")":
                    depth -= 1
                if ch == " " and depth == 0:
                    tokens.append("".join(cur))
                    cur = []
                else:
                    cur.append(ch)
            tokens.append("".join(cur))
            return tokens

        def evaluate_expr(expr, scope):
            # atom: an integer literal, or a variable lookup
            if expr[0] != "(":
                if expr[0] == "-" or expr[0].isdigit():
                    return int(expr)
                return scope[expr][-1]

            tokens = split_tokens(expr[1:-1])
            op = tokens[0]

            if op == "add":
                return evaluate_expr(tokens[1], scope) + evaluate_expr(tokens[2], scope)
            if op == "mult":
                return evaluate_expr(tokens[1], scope) * evaluate_expr(tokens[2], scope)

            # "let v1 e1 v2 e2 ... expr" -> bind pairs sequentially, then eval the tail
            bound = []
            i = 1
            while i + 1 < len(tokens):
                var = tokens[i]
                val = evaluate_expr(tokens[i + 1], scope)
                scope.setdefault(var, []).append(val)
                bound.append(var)
                i += 2

            res = evaluate_expr(tokens[i], scope)

            # leaving the let -> drop every binding it introduced
            for var in bound:
                scope[var].pop()
            return res

        return evaluate_expr(expression, {})
