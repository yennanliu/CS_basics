"""

964. Least Operators to Express Number
Hard

Given a single positive integer x, we will write an expression of the form x (op1) x (op2) x (op3) x ... where each operator op1, op2, etc. is either addition, subtraction, multiplication, or division (+, -, *, or /). For example, with x = 3, we might write 3 * 3 / 3 + 3 - 3 which is a value of 3.

When writing such an expression, we adhere to the following conventions:

The division operator (/) returns rational numbers.
There are no parentheses placed anywhere.
We use the usual order of operations: multiplication and division happen before addition and subtraction.
It is not allowed to use the unary negation operator (-). For example, "x - x" is a valid expression as it only uses subtraction, but "-x + x" is not because it uses negation.

We would like to write an expression with the least number of operators such that the expression equals the given target. Return the least number of operators used.

Example 1:

Input: x = 3, target = 19
Output: 5
Explanation: 3 * 3 + 3 * 3 + 3 / 3.
The expression contains 5 operations.

Example 2:

Input: x = 5, target = 501
Output: 8
Explanation: 5 * 5 * 5 * 5 - 5 * 5 * 5 + 5 / 5.
The expression contains 8 operations.

Example 3:

Input: x = 100, target = 100000000
Output: 3
Explanation: 100 * 100 * 100 * 100.
The expression contains 3 operations.

Constraints:

2 <= x <= 100
1 <= target <= 2 * 10^8

"""

# V0
# IDEA : MEMOIZED DFS on the BASE-x REPRESENTATION
#
#  Any expression is a sum/difference of terms, each term being x^k.
#  COST convention used here (each term is charged its joining +/- too):
#     - term x^k for k >= 1 costs k        (k-1 multiplications + 1 joining op)
#     - term x^0 == x/x     costs 2        (1 division      + 1 joining op)
#  The very first term needs no joining operator, so we subtract 1 at the end.
#
#  dfs(v) = min cost to build value v out of such terms.
#     - if v <= x : either use v copies of (x/x)          -> 2 * v
#                   or start from one x and subtract      -> 1 + 2 * (x - v)
#     - else      : let k be the smallest power with x^k >= v, then
#                   go OVER  : k     + dfs(x^k - v)
#                   go UNDER : (k-1) + dfs(v - x^(k-1))
#       (the OVER branch only helps when x^k - v < v)
#
# time = O(log_x(target) ^ 2) states, each O(log_x(target)) work
# space = O(number of memoized states)
class Solution(object):
    def leastOpsExpressTarget(self, x, target):
        memo = {}

        def dfs(v):
            if v in memo:
                return memo[v]

            if x >= v:
                # cheap base case: build v directly from x's
                res = min(v * 2, (x - v) * 2 + 1)
                memo[v] = res
                return res

            # smallest k with x^k >= v  (k >= 2 here, since x < v)
            k = 2
            while x ** k < v:
                k += 1

            # go under: use x^(k-1), then build the remainder
            res = (k - 1) + dfs(v - x ** (k - 1))
            # go over: use x^k, then subtract the excess (only if it shrinks the problem)
            if x ** k - v < v:
                res = min(res, k + dfs(x ** k - v))

            memo[v] = res
            return res

        # the leading term does not need a joining operator
        return dfs(target) - 1
