"""

1096. Brace Expansion II
Hard

Under the grammar given below, strings can represent a set of lowercase words.
Let R(expr) denote the set of words the expression represents.

The grammar can best be understood through simple examples:

- Single letters represent a singleton set containing that word.
    - R("a") = {"a"}
    - R("w") = {"w"}
- When we take a comma-delimited list of two or more expressions, we take the
  union of possibilities.
    - R("{a,b,c}") = {"a","b","c"}
    - R("{{a,b},{b,c}}") = {"a","b","c"}
      (notice the final set only contains each word at most once)
- When we concatenate two expressions, we take the set of possible concatenations
  between two words where the first word comes from the first expression and the
  second word comes from the second expression.
    - R("{a,b}{c,d}") = {"ac","ad","bc","bd"}
    - R("a{b,c}{d,e}f{g,h}") = {"abdfg","abdfh","abefg","abefh",
                                "acdfg","acdfh","acefg","acefh"}

Formally, the three rules for our grammar:

- For every lowercase letter x, we have R(x) = {x}.
- For expressions e1, e2, ... , ek with k >= 2, we have
  R({e1, e2, ...}) = R(e1) union R(e2) union ...
- For expressions e1 and e2, we have
  R(e1 + e2) = {a + b for (a, b) in R(e1) x R(e2)},
  where + denotes concatenation, and x denotes the cartesian product.

Given an expression representing a set of words under the given grammar,
return the sorted list of words that the expression represents.


Example 1:

Input: expression = "{a,b}{c,{d,e}}"
Output: ["ac","ad","ae","bc","bd","be"]

Example 2:

Input: expression = "{{a,z},a{b,c},{ab,z}}"
Output: ["a","ab","ac","z"]
Explanation: Each distinct word is written only once in the final answer.


Constraints:

1 <= expression.length <= 60
expression[i] consists of '{', '}', ',' or lowercase English letters.
The given expression represents a set of words based on the grammar given
in the description.

"""

# V0
# IDEA: STACK PARSER (union list + product set per nesting level)
#
#   per level we keep:
#     - `cur`   : set of words built by CONCATENATION so far
#     - `parts` : list of sets already closed by a ',' (to be UNIONed)
#
#   '{' -> push (parts, cur), start a fresh level
#   ',' -> flush `cur` into `parts`, restart `cur` = {""}
#   '}' -> union the level, pop the parent, cartesian-product it into parent `cur`
#   letter -> append the letter to every word in `cur`
#
# time = O(L * W)
# space = O(W)
#   L = len(expression), W = number of distinct words produced
class Solution(object):
    def braceExpansionII(self, expression):
        parts = []       # sets waiting to be UNIONed at this level
        cur = set([""])  # current CONCATENATION product
        stack = []

        for c in expression:
            if c == "{":
                stack.append((parts, cur))
                parts, cur = [], set([""])
            elif c == "}":
                parts.append(cur)
                grp = set()
                for p in parts:
                    grp |= p
                parts, cur = stack.pop()
                # NOTE !!! close the group -> product with the parent prefix
                cur = set(a + b for a in cur for b in grp)
            elif c == ",":
                parts.append(cur)
                cur = set([""])
            else:
                cur = set(a + c for a in cur)

        parts.append(cur)
        res = set()
        for p in parts:
            res |= p
        return sorted(res)


# V1
# IDEA: RECURSIVE SUBSTITUTION (expand the inner-most brace group, then recurse)
#
#   find the FIRST '}' -> its group has no nested braces,
#   replace the whole "{x,y,z}" by each of x / y / z and recurse.
#
# time = exponential in the number of brace groups
# space = O(L * W)
class Solution(object):
    def braceExpansionII(self, expression):
        res = set()

        def dfs(exp):
            j = exp.find("}")
            if j == -1:
                res.add(exp)
                return
            i = exp.rfind("{", 0, j)
            head, tail = exp[:i], exp[j + 1:]
            for opt in exp[i + 1:j].split(","):
                dfs(head + opt + tail)

        dfs(expression)
        return sorted(res)
