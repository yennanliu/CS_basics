"""

2462. Total Cost to Hire K Workers
Medium

You are given a 0-indexed integer array costs where costs[i] is the cost of hiring the ith worker.

You are also given two integers k and candidates. We want to hire exactly k workers according to the following rules:

You will run k sessions and hire exactly one worker in each session.
In each hiring session, choose the worker with the lowest cost from either the first candidates workers or the last candidates workers. Break the tie by the smallest index.
For example, if costs = [3,2,7,7,1,2] and candidates = 2, then in the first hiring session, we will choose the 4th worker because they have the lowest cost [3,2,7,7,1,2].
In the second hiring session, we will choose 1st worker because they have the same lowest cost as 4th worker but they have the smallest index [3,2,7,7,2]. Please note that the indexing may be changed in the process.
If there are fewer than candidates workers remaining, choose the worker with the lowest cost among them. Break the tie by the smallest index.
A worker can only be chosen once.

Return the total cost to hire exactly k workers.


Example 1:

Input: costs = [17,12,10,2,7,2,11,20,8], k = 3, candidates = 4
Output: 11
Explanation: We hire 3 workers in total. The total cost is initially 0.
- In the first hiring round we choose the worker from [17,12,10,2,7,2,11,20,8]. The lowest cost is 2, and we break the tie by the smallest index, which is 3. The total cost = 0 + 2 = 2.
- In the second hiring round we choose the worker from [17,12,10,7,2,11,20,8]. The lowest cost is 2 (index 4). The total cost = 2 + 2 = 4.
- In the third hiring round we choose the worker from [17,12,10,7,11,20,8]. The lowest cost is 7 (index 3). The total cost = 4 + 7 = 11. Notice that the worker with index 3 was common in the first and last four workers.
The total hiring cost is 11.

Example 2:

Input: costs = [1,2,4,1], k = 3, candidates = 3
Output: 4
Explanation: We hire 3 workers in total. The total cost is initially 0.
- In the first hiring round we choose the worker from [1,2,4,1]. The lowest cost is 1, and we break the tie by the smallest index, which is 0. The total cost = 0 + 1 = 1.
- In the second hiring round we choose the worker from [2,4,1]. The lowest cost is 1 (index 2). The total cost = 1 + 1 = 2.
- In the third hiring round there are less than three candidates. We choose the worker from the remaining workers [2,4]. The lowest cost is 2 (index 0). The total cost = 2 + 2 = 4.
The total hiring cost is 4.


Constraints:

1 <= costs.length <= 10^5
1 <= costs[i] <= 10^5
1 <= k, candidates <= costs.length

"""

# V0
# IDEA : TWO MIN-HEAPS, ONE PER END, REFILLED FROM THE SHRINKING MIDDLE
#
#   the candidate pool is always "the first `candidates` remaining" plus "the
#   last `candidates` remaining", so keep one heap for each side and two
#   pointers marking the untouched middle.
#
#   each round takes the cheaper of the two heap tops. ties go to the FRONT
#   heap, which reproduces the "smallest index" rule since the front workers
#   are the lower indices.
#
#   after a pop, that side is topped up from the middle if any workers remain
#   — this is what makes a worker who sits in BOTH windows get counted only
#   once (the pointers never cross).
#
# time = O((k + candidates) log candidates), space = O(candidates)
import heapq


class Solution(object):
    def totalCost(self, costs, k, candidates):
        n = len(costs)
        left, right = 0, n - 1

        head = []
        while left <= right and len(head) < candidates:
            heapq.heappush(head, costs[left])
            left += 1

        tail = []
        while left <= right and len(tail) < candidates:
            heapq.heappush(tail, costs[right])
            right -= 1

        total = 0
        for _ in range(k):
            if tail and (not head or tail[0] < head[0]):
                total += heapq.heappop(tail)
                if left <= right:
                    heapq.heappush(tail, costs[right])
                    right -= 1
            else:
                total += heapq.heappop(head)
                if left <= right:
                    heapq.heappush(head, costs[left])
                    left += 1
        return total
