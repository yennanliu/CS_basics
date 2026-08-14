"""

2019. The Score of Students Solving Math Expression
Hard

You are given a string s that contains digits 0-9, addition symbols '+', and multiplication symbols '*' only, representing a valid math expression of single digit numbers (e.g., 3+5*2). This expression was given to n elementary school students. The students were instructed to get the answer of the expression by following this order of operations:

Compute multiplication, reading from left to right; Then,
Compute addition, reading from left to right.

You are given an integer array answers of length n, which are the submitted answers of the students in no particular order. You are asked to grade the answers, by following these rules:

If an answer equals the correct answer of the expression, this student will be rewarded 5 points;
Otherwise, if the answer could be interpreted as if the student applied the operators in the wrong order but had correct arithmetic, this student will be rewarded 2 points;
Otherwise, this student will be rewarded 0 points.

Return the sum of the points of the students.


Example 1:

Input: s = "7+3*1*2", answers = [20,13,42]
Output: 7
Explanation: The correct answer of the expression is 13, therefore one student is rewarded 5 points: [20,13,42]
A student might have applied the operators in this order: ((7+3)*1)*2 = 20. Therefore one student is rewarded 2 points: [20,13,42]
The points for the students are: [2,5,0]. The sum of the points is 2+5+0=7.

Example 2:

Input: s = "3+5*2", answers = [13,0,10,13,13,16,16]
Output: 19
Explanation: The correct answer of the expression is 13, therefore three students are rewarded 5 points each: [13,0,10,13,13,16,16]
A student might have applied the operators in this order: ((3+5)*2 = 16. Therefore two students are rewarded 2 points: [13,0,10,13,13,16,16]
The points for the students are: [5,0,0,5,5,2,2]. The sum of the points is 5+0+0+5+5+2+2=19.

Example 3:

Input: s = "6+0*1", answers = [12,9,6,4,8,6]
Output: 10
Explanation: The correct answer of the expression is 6.
If a student had incorrectly done (6+0)*1, the answer would also be 6.
By the rules of grading, the students will still be rewarded 5 points (as they got the correct answer), not 2 points.
The points for the students are: [0,0,5,0,0,5]. The sum of the points is 10.


Constraints:

3 <= s.length <= 31
s represents a valid expression that contains only digits 0-9, '+', and '*' only.
All the integer operands in the expression are in the inclusive range [0, 9].
1 <= The count of all operators ('+' and '*') in the math expression <= 15
Test data are generated such that the correct answer of the expression is in the range of [0, 1000].
answers.length == n
1 <= n <= 10^4
0 <= answers[i] <= 1000

"""

# V0
# IDEA : INTERVAL DP (all possible parenthesizations) + CORRECT EVAL
#
#   dp[i][j] = SET of values the sub-expression of operands i..j can take
#              under ANY parenthesization.
#   split on every operator between i and j :
#       dp[i][j] |= { combine(a, op, b) for a in dp[i][k] for b in dp[k+1][j] }
#
#   prune : the statement says the correct answer is <= 1000, and any value
#   above 1000 can never shrink back down, so drop values > 1000. that keeps
#   each set tiny (<= 1001 values) and the whole DP fast.
#
#   grading : 5 points for the true value (normal precedence, eval'd first),
#             else 2 points if the value lives in dp[0][last].
#
# time = O(n^3 * V^2) with V bounded by 1001 in practice, space = O(n^2 * V)
from collections import Counter


class Solution(object):
    def scoreOfStudents(self, s, answers):
        nums = [int(c) for c in s[::2]]
        ops = list(s[1::2])
        n = len(nums)

        # the "correct" answer : multiplication first, then addition
        stack = [nums[0]]
        for i, op in enumerate(ops):
            if op == '*':
                stack[-1] *= nums[i + 1]
            else:
                stack.append(nums[i + 1])
        correct = sum(stack)

        # interval DP over every parenthesization
        LIMIT = 1000
        dp = [[set() for _ in range(n)] for _ in range(n)]
        for i in range(n):
            dp[i][i].add(nums[i])
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                for k in range(i, j):
                    op = ops[k]
                    for a in dp[i][k]:
                        for b in dp[k + 1][j]:
                            v = a * b if op == '*' else a + b
                            if v <= LIMIT:
                                dp[i][j].add(v)

        possible = dp[0][n - 1]
        res = 0
        for v, cnt in Counter(answers).items():
            if v == correct:
                res += 5 * cnt
            elif v in possible:
                res += 2 * cnt
        return res
