"""

502. IPO
Hard

Suppose LeetCode will start its IPO soon. In order to sell a good price of its
shares to Venture Capital, LeetCode would like to work on some projects to
increase its capital before the IPO. Since it has limited resources, it can only
finish at most k distinct projects before the IPO. Help LeetCode design the best
way to maximize its total capital after finishing at most k distinct projects.

You are given n projects where the ith project has a pure profit profits[i] and
a minimum capital of capital[i] is needed to start it.

Initially, you have w capital. When you finish a project, you will obtain its
pure profit and the profit will be added to your total capital.

Pick a list of at most k distinct projects from given projects to maximize your
final capital, and return the final maximized capital.

The answer is guaranteed to fit in a 32-bit signed integer.


Example 1:

Input: k = 2, w = 0, profits = [1,2,3], capital = [0,1,1]

Output: 4

Explanation: Since your initial capital is 0, you can only start the project
indexed 0. After finishing it you will obtain profit 1 and your capital becomes 1.
With capital 1, you can either start the project indexed 1 or the project
indexed 2. Since you can choose at most 2 projects, you need to finish the
project indexed 2 to get the maximum capital.
Therefore, output the final maximized capital, which is 0 + 1 + 3 = 4.

Example 2:

Input: k = 3, w = 0, profits = [1,2,3], capital = [0,1,2]

Output: 6


Constraints:

1 <= k <= 10^5
0 <= w <= 10^9
n == profits.length
n == capital.length
1 <= n <= 10^5
0 <= profits[i] <= 10^4
0 <= capital[i] <= 10^9

"""

# V0
# IDEA : GREEDY + SORT BY CAPITAL + MAX HEAP OF PROFITS
#
#   profits are never negative, so capital only grows: a project affordable
#   now stays affordable forever. that makes the greedy safe -- each round,
#   take the MOST profitable project we can currently afford:
#
#   1) sort projects by required capital, and keep a pointer into that list.
#   2) each round, push the profit of every project with capital <= w onto a
#      max heap (each project is pushed exactly once, since w never shrinks).
#   3) pop the largest profit and add it to w. if the heap is empty, nothing
#      is affordable now and nothing ever will be -> stop early.
#
#   why greedy is optimal: taking the biggest affordable profit gives the
#   largest w, which unlocks a superset of what any other choice unlocks.
#
#   e.g. k=2, w=0, profits=[1,2,3], capital=[0,1,1]
#     round 1: push 1 (cap 0)            pop 1 -> w = 1
#     round 2: push 2, 3 (cap 1)         pop 3 -> w = 4
#
#   (brute force -- rescan every project each round -- is O(k * n) = 10^10, TLE)
#
# time = O(n log n + k log n), space = O(n)
import heapq


class Solution(object):
    def findMaximizedCapital(self, k, w, profits, capital):
        """
        :type k: int
        :type w: int
        :type profits: List[int]
        :type capital: List[int]
        :rtype: int
        """
        # (required capital, profit), cheapest to start first
        projects = []
        for i in range(len(profits)):
            projects.append((capital[i], profits[i]))
        projects.sort()

        # heapq is a min heap -> store negated profits to pop the largest
        max_profit_heap = []
        next_project = 0

        for _ in range(k):
            # unlock every project the current capital can afford
            while next_project < len(projects) and projects[next_project][0] <= w:
                heapq.heappush(max_profit_heap, -projects[next_project][1])
                next_project += 1

            # NOTE !!! nothing affordable now means nothing affordable ever,
            #          because w only changes by finishing a project
            if not max_profit_heap:
                break

            best_profit = -heapq.heappop(max_profit_heap)
            w += best_profit

        return w
