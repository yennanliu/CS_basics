"""

2398. Maximum Number of Robots Within Budget
Hard

You have n robots. You are given two 0-indexed integer arrays, chargeTimes and runningCosts, both of length n. The ith robot costs chargeTimes[i] units to charge and costs runningCosts[i] units to run. You are also given an integer budget.

The total cost of running k chosen robots is equal to max(chargeTimes) + k * sum(runningCosts), where max(chargeTimes) is the largest charge cost among the k robots and sum(runningCosts) is the sum of running costs among the k robots.

Return the maximum number of consecutive robots you can run such that the total cost does not exceed budget.


Example 1:

Input: chargeTimes = [3,6,1,3,4], runningCosts = [2,1,3,4,5], budget = 25
Output: 3
Explanation:
It is possible to run all individual and consecutive pairs of robots within budget.
To obtain answer 3, consider the first 3 robots. The total cost will be max(3,6,1) + 3 * sum(2,1,3) = 6 + 3 * 6 = 24 which is less than 25.
It can be shown that it is not possible to run more than 3 consecutive robots within budget, so we return 3.

Example 2:

Input: chargeTimes = [11,12,19], runningCosts = [10,8,7], budget = 19
Output: 0
Explanation: No robot can be run that does not exceed the budget, so we return 0.


Constraints:

chargeTimes.length == runningCosts.length == n
1 <= n <= 5 * 10^4
1 <= chargeTimes[i], runningCosts[i] <= 10^5
1 <= budget <= 10^15

"""

# V0
# IDEA : SLIDING WINDOW + MONOTONIC (DECREASING) DEQUE
#
#   cost(l..r) = max(chargeTimes[l..r]) + (r-l+1) * sum(runningCosts[l..r]).
#   both terms only GROW when the window grows, so the feasibility is
#   monotonic in the window -> a two-pointer window works.
#
#   the only tricky part is max() over a shrinking-from-the-left window:
#   keep a deque of indices whose chargeTimes are strictly decreasing, so
#   the front is always the window maximum.
#
#   NOTE : when we advance `l`, pop the deque front FIRST if it equals the
#          old `l`, otherwise the max would keep pointing at an evicted item.
#
# time = O(n), space = O(n)
from collections import deque
class Solution(object):
    def maximumRobots(self, chargeTimes, runningCosts, budget):
        q = deque()
        res = 0
        s = 0
        l = 0
        for r in range(len(chargeTimes)):
            s += runningCosts[r]
            while q and chargeTimes[q[-1]] <= chargeTimes[r]:
                q.pop()
            q.append(r)
            while q and chargeTimes[q[0]] + (r - l + 1) * s > budget:
                if q[0] == l:
                    q.popleft()
                s -= runningCosts[l]
                l += 1
            if r - l + 1 > res:
                res = r - l + 1
        return res
