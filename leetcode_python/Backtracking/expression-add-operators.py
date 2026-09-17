"""

282. Expression Add Operators
Hard

Given a string num that contains only digits and an integer target, return all
possibilities to insert the binary operators '+', '-', and/or '*' between the
digits of num so that the resultant expression evaluates to the target value.

Note that operands in the returned expressions should not contain leading zeros.


Example 1:

Input: num = "123", target = 6
Output: ["1*2*3","1+2+3"]
Explanation: Both "1*2*3" and "1+2+3" evaluate to 6.

Example 2:

Input: num = "232", target = 8
Output: ["2*3+2","2+3*2"]
Explanation: Both "2*3+2" and "2+3*2" evaluate to 8.

Example 3:

Input: num = "3456237490", target = 9191
Output: []
Explanation: There are no expressions that can be created from "3456237490" to evaluate to 9191.


Constraints:

1 <= num.length <= 10
num consists of only digits.
-2^31 <= target <= 2^31 - 1

"""

# V0
# IDEA : BACKTRACKING (carry the LAST operand so '*' can be undone)
#
#   '*' binds tighter than '+'/'-', so a running total is not enough : when we
#   append "*x" we have to take the previous operand back out of the total and
#   re-add it multiplied.
#
#     total, prev  --"*x"-->  total - prev + prev*x,  new prev = prev*x
#
#   e.g. "2+3*2" : after "2+3" total = 5, prev = 3
#                  then "*2"  -> 5 - 3 + 3*2 = 8, prev = 6   (NOT 5*2)
#
#   NOTE !!! a multi-digit operand may not have a leading zero, so once the
#            operand starts with '0' we stop extending it (break, not continue).
#
# time = O(4^n * n), n = len(num), space = O(n) recursion + the output
class Solution(object):
    def addOperators(self, num, target):
        """
        :type num: str
        :type target: int
        :rtype: List[str]
        """
        # edge
        if not num:
            return []

        n = len(num)
        res = []

        def dfs(i, expr, total, prev):
            if i == n:
                if total == target:
                    res.append(expr)
                return

            for j in range(i, n):
                # "05" is not a legal operand -> and neither is anything longer
                if j > i and num[i] == '0':
                    break

                part = num[i:j + 1]
                cur = int(part)

                if i == 0:
                    # the first operand carries no operator in front of it
                    dfs(j + 1, part, cur, cur)
                else:
                    dfs(j + 1, expr + '+' + part, total + cur, cur)
                    dfs(j + 1, expr + '-' + part, total - cur, -cur)
                    dfs(j + 1, expr + '*' + part, total - prev + prev * cur, prev * cur)

        dfs(0, '', 0, 0)
        return res
