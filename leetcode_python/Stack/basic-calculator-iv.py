"""

770. Basic Calculator IV
Hard

Given an expression such as expression = "e + 8 - a + 5" and an evaluation map such as
{"e": 1} (given in terms of evalvars = ["e"] and evalints = [1]), return a list of
tokens representing the simplified expression, such as ["-1*a","14"]

  - An expression alternates chunks and symbols, with a space separating each chunk
    and symbol.
  - A chunk is either an expression in parentheses, a variable, or a non-negative integer.
  - A variable is a string of lowercase letters (not including digits). Note that
    variables can be multiple letters, and note that variables never have a leading
    coefficient or unary operator like "2x" or "-x".

Expressions are evaluated in the usual order: brackets first, then multiplication,
then addition and subtraction.

  - For example, expression = "1 + 2 * 3" has an answer of ["7"].

The format of the output is as follows:

  - For each term of free variables with a non-zero coefficient, we write the free
    variables within a term in sorted order lexicographically.
      - For example, we would never write a term like "b*a*c", only "a*b*c".
  - Terms have degrees equal to the number of free variables being multiplied, counting
    multiplicity. We write the largest degree terms of our answer first, breaking ties
    by lexicographic order ignoring the leading coefficient of the term.
      - For example, "a*a*b*c" has degree 4.
  - The leading coefficient of the term is placed directly to the left with an asterisk
    separating it from the variables (if they exist). A leading coefficient of 1 is
    still printed.
  - An example of a well-formatted answer is
    ["-2*a*a*a", "3*a*a*b", "3*b*b", "4*a", "5*c", "-6"].
  - Terms (including constant terms) with coefficient 0 are not included.
      - For example, an expression of "0" has an output of [].

Note: You may assume that the given expression is always valid.
All intermediate results will be in the range of [-2^31, 2^31 - 1].


Example 1:

Input: expression = "e + 8 - a + 5", evalvars = ["e"], evalints = [1]
Output: ["-1*a","14"]

Example 2:

Input: expression = "e - 8 + temperature - pressure", evalvars = ["e", "temperature"], evalints = [1, 12]
Output: ["-1*pressure","5"]

Example 3:

Input: expression = "(e + 8) * (e - 8)", evalvars = [], evalints = []
Output: ["1*e*e","-64"]


Constraints:

1 <= expression.length <= 250
expression consists of lowercase English letters, digits, '+', '-', '*', '(', ')', ' '.
expression does not contain any leading or trailing spaces.
All the tokens in expression are separated by a single space.
0 <= evalvars.length <= 100
1 <= evalvars[i].length <= 20
evalvars[i] consists of lowercase English letters.
evalints.length == evalvars.length
-100 <= evalints[i] <= 100

"""

# V0
# IDEA : RECURSIVE DESCENT PARSER + POLYNOMIAL ARITHMETIC
#
#   Represent a polynomial as a dict:
#       {(sorted tuple of variable names): coefficient}
#   e.g.  3*a*b - 5   ->   {("a","b"): 3, (): -5}
#   Terms with coefficient 0 are dropped eagerly so they never reach the output.
#
#   Grammar (standard precedence):
#       expr   := term (('+' | '-') term)*
#       term   := factor ('*' factor)*
#       factor := '(' expr ')' | variable | integer
#
#   Output order: highest degree first (degree = len(key)), ties broken by the
#   lexicographic order of the variable tuple — which is exactly sorted by
#   (-len(key), key).
#
# time = O(n * T^2), n = len(expression), T = number of distinct terms produced
# space = O(n + T)
class Solution(object):
    def basicCalculatorIV(self, expression, evalvars, evalints):
        env = dict(zip(evalvars, evalints))

        def make_const(v):
            return {(): v} if v != 0 else {}

        def make_atom(name):
            # a variable with a known value collapses to a constant
            if name in env:
                return make_const(env[name])
            return {(name,): 1}

        def poly_add(p, q, sign):
            res = dict(p)
            for key, coef in q.items():
                res[key] = res.get(key, 0) + sign * coef
                if res[key] == 0:
                    del res[key]
            return res

        def poly_mul(p, q):
            res = {}
            for k1, c1 in p.items():
                for k2, c2 in q.items():
                    key = tuple(sorted(k1 + k2))  # variables kept in sorted order
                    res[key] = res.get(key, 0) + c1 * c2
            return {k: c for k, c in res.items() if c != 0}

        # pad the parens so they become standalone tokens, then split on whitespace
        tokens = expression.replace("(", " ( ").replace(")", " ) ").split()
        pos = [0]  # single element list so nested funcs can mutate it

        def parse_expr():
            res = parse_term()
            while pos[0] < len(tokens) and tokens[pos[0]] in ("+", "-"):
                op = tokens[pos[0]]
                pos[0] += 1
                res = poly_add(res, parse_term(), 1 if op == "+" else -1)
            return res

        def parse_term():
            res = parse_factor()
            while pos[0] < len(tokens) and tokens[pos[0]] == "*":
                pos[0] += 1
                res = poly_mul(res, parse_factor())
            return res

        def parse_factor():
            tok = tokens[pos[0]]
            pos[0] += 1
            if tok == "(":
                res = parse_expr()
                pos[0] += 1  # consume the matching ')'
                return res
            if tok.isdigit():
                return make_const(int(tok))
            return make_atom(tok)

        poly = parse_expr()

        # highest degree first, then lexicographic on the variable tuple
        keys = sorted(poly.keys(), key=lambda k: (-len(k), k))
        return ["*".join([str(poly[k])] + list(k)) for k in keys]
