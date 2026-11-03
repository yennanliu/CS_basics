"""

2110. Number of Smooth Descent Periods of a Stock
Medium

You are given an integer array prices representing the daily price history of a stock, where prices[i] is the stock price on the ith day.

A smooth descent period of a stock consists of one or more contiguous days such that the price on each day is lower than the price on the preceding day by exactly 1. The first day of the period is exempted from this rule.

Return the number of smooth descent periods.


Example 1:

Input: prices = [3,2,1,4]
Output: 7
Explanation: There are 7 smooth descent periods:
[3], [2], [1], [4], [3,2], [2,1], and [3,2,1]
Note that a period with one day is a smooth descent period by the definition.

Example 2:

Input: prices = [8,6,7,7]
Output: 4
Explanation: There are 4 smooth descent periods: [8], [6], [7], and [7]
Note that [8,6] is not a smooth descent period as 8 - 6 != 1.

Example 3:

Input: prices = [1]
Output: 1
Explanation: There is 1 smooth descent period: [1]


Constraints:

1 <= prices.length <= 10^5
1 <= prices[i] <= 10^5

"""

# V0
# IDEA : COUNT THE RUN LENGTH ENDING AT EACH DAY (1D DP, ROLLED TO ONE VARIABLE)
#
#   run = length of the longest smooth descent that ENDS on day i.
#       run = run + 1   if prices[i] == prices[i-1] - 1
#       run = 1         otherwise (day i starts a new period on its own)
#   every period ending on day i is a suffix of that run, so day i
#   contributes exactly `run` new periods -> answer = sum of run over all days.
#
#   e.g. prices = [3,2,1,4] -> run = 1,2,3,1 -> 1+2+3+1 = 7
#
#   (brute force : check every subarray, O(n^2) -> TLE at n = 10^5)
#
# time = O(n), space = O(1)
class Solution(object):
    def getDescentPeriods(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if not prices:
            return 0

        # day 0 alone is one period
        total = 1
        run = 1

        for i in range(1, len(prices)):
            # NOTE !!! "smooth" means a drop of EXACTLY 1, not any drop
            if prices[i] == prices[i - 1] - 1:
                run += 1
            else:
                run = 1
            # all periods ending on day i
            total += run

        return total


# V1
# IDEA : SPLIT INTO MAXIMAL RUNS, EACH RUN OF LENGTH L GIVES L * (L + 1) / 2
#
#   a maximal smooth descent of length L contains every one of its
#   contiguous sub-periods, and there are 1 + 2 + ... + L = L * (L + 1) / 2
#   of them. Periods never cross a break, so the runs add up independently.
#
#   e.g. prices = [3,2,1,4] -> runs [3,2,1], [4] -> 6 + 1 = 7
#
#   same answer as V0 — V0 just adds the triangle number one term at a time.
#
# time = O(n), space = O(1)
class Solution2(object):
    def getDescentPeriods(self, prices):
        """
        :type prices: List[int]
        :rtype: int
        """
        if not prices:
            return 0

        total = 0
        run_length = 1

        for i in range(1, len(prices)):
            if prices[i] == prices[i - 1] - 1:
                run_length += 1
            else:
                # the run ended at day i-1 : count all its sub-periods
                total += run_length * (run_length + 1) // 2
                run_length = 1

        # the last run is still open when the loop ends
        total += run_length * (run_length + 1) // 2
        return total
