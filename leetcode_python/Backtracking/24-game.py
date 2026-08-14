"""

679. 24 Game
Hard

You are given an integer array cards of length 4. You have four cards, each containing a
number in the range [1, 9]. You should arrange the numbers on these cards in a
mathematical expression using the operators ['+', '-', '*', '/'] and the parentheses
'(' and ')' to get the value 24.

You are restricted with the following rules:

- The division operator '/' represents real division, not integer division.
  For example, 4 / (1 - 2 / 3) = 4 / (1 / 3) = 12.
- Every operation done is between two numbers. In particular, we cannot use '-' as a
  unary operator.
  For example, if cards = [1, 1, 1, 1], the expression "-1 - 1 - 1 - 1" is not allowed.
- You cannot concatenate numbers together.
  For example, if cards = [1, 2, 1, 2], the expression "12 + 12" is not valid.

Return true if you can get such expression that evaluates to 24, and false otherwise.

Example 1:

Input: cards = [4,1,8,7]
Output: true
Explanation: (8-4) * (7-1) = 24

Example 2:

Input: cards = [1,2,1,2]
Output: false

Constraints:

cards.length == 4
1 <= cards[i] <= 9

"""

# V0
# IDEA : DFS / BACKTRACKING -- repeatedly fold two numbers into one
#
#   Any fully parenthesized expression over 4 numbers can be built by picking
#   two of the current numbers, replacing them with the result of one operator,
#   and recursing on the shrunken multiset (4 -> 3 -> 2 -> 1).
#   This covers every parenthesization automatically, so we never build strings.
#
#   Ordered pairs (i, j) with i != j are enumerated, which covers both a-b / b-a
#   and a/b / b/a without a separate case.
#
#   Division is real division, so work in floats and compare against 24 with an
#   epsilon (e.g. 8/(3-8/3) is exactly 24 mathematically but not in binary float).
#
# time = O(1) -- bounded search: at most 12 * 4 * 6 * 4 * 2 * 4 states for 4 cards
# space = O(1)
class Solution(object):
    def judgePoint24(self, cards):
        EPS = 1e-6

        def dfs(nums):
            if len(nums) == 1:
                return abs(nums[0] - 24.0) < EPS

            size = len(nums)
            for i in range(size):
                for j in range(size):
                    if i == j:
                        continue
                    # everything except the two chosen operands
                    rest = [nums[t] for t in range(size) if t != i and t != j]
                    a, b = nums[i], nums[j]

                    candidates = [a + b, a - b, a * b]
                    if abs(b) > EPS:
                        candidates.append(a / b)

                    for val in candidates:
                        if dfs(rest + [val]):
                            return True
            return False

        return dfs([float(c) for c in cards])
