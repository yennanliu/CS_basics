"""

1449. Form Largest Integer With Digits That Add up to Target
Hard

Given an array of integers cost and an integer target, return the maximum integer
you can paint under the following rules:

- The cost of painting a digit (i + 1) is given by cost[i] (0-indexed).
- The total cost used must be equal to target.
- The integer does not have 0 digits.

Since the answer may be very large, return it as a string. If there is no way to
paint any integer given the condition, return "0".


Example 1:

Input: cost = [4,3,2,5,6,7,2,5,5], target = 9
Output: "7772"
Explanation: The cost to paint the digit '7' is 2, and the digit '2' is 3.
Then cost("7772") = 2*3 + 3*1 = 9. You could also paint "977", but "7772" is the
largest number.
Digit    cost
  1  ->   4
  2  ->   3
  3  ->   2
  4  ->   5
  5  ->   6
  6  ->   7
  7  ->   2
  8  ->   5
  9  ->   5

Example 2:

Input: cost = [7,6,5,5,5,6,8,7,8], target = 12
Output: "85"
Explanation: The cost to paint the digit '8' is 7, and the digit '5' is 5.
Then cost("85") = 7 + 5 = 12.

Example 3:

Input: cost = [2,4,6,2,4,6,4,4,4], target = 5
Output: "0"
Explanation: It is impossible to paint any integer with total cost equal to target.


Constraints:

cost.length == 9
1 <= cost[i], target <= 5000

"""

# V0
# IDEA: UNBOUNDED KNAPSACK (maximize digit count) + GREEDY RECONSTRUCTION
#
#  Step 1) dp[t] = max amount of digits we can paint with cost EXACTLY t
#          dp[0] = 0, dp[t] = -1 (unreachable)
#          dp[t] = max(dp[t - cost[d-1]] + 1) for d in 1..9
#
#  Step 2) more digits always beats bigger digits (a longer number is larger),
#          so once we know the max length, we rebuild the answer greedily:
#          always try the LARGEST digit d (9 -> 1) that keeps the remaining
#          budget on an optimal path, i.e. dp[t - cost[d-1]] == dp[t] - 1
#
# time = O(target * 9)
# space = O(target)
class Solution(object):
    def largestNumber(self, cost, target):
        # dp[t] = max number of digits using cost exactly t (-1 = unreachable)
        dp = [-1] * (target + 1)
        dp[0] = 0

        for t in range(1, target + 1):
            for d in range(1, 10):
                c = cost[d - 1]
                if t >= c and dp[t - c] >= 0:
                    dp[t] = max(dp[t], dp[t - c] + 1)

        if dp[target] <= 0:
            return "0"

        # greedy rebuild : prefer the biggest digit that stays on an optimal path
        res = []
        t = target
        while t > 0:
            for d in range(9, 0, -1):
                c = cost[d - 1]
                if t >= c and dp[t - c] == dp[t] - 1:
                    res.append(str(d))
                    t -= c
                    break

        return "".join(res)
