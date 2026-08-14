"""

1705. Maximum Number of Eaten Apples
Medium

There is a special kind of apple tree that grows apples every day for n days. On the ith day, the tree grows apples[i] apples that will rot after days[i] days, that is on day i + days[i] the apples will be rotten and cannot be eaten. On some days, the apple tree does not grow any apples, which are denoted by apples[i] == 0 and days[i] == 0.

You decided to eat at most one apple a day (to keep the doctors away). Note that you can keep eating after the first n days.

Given two integer arrays days and apples of length n, return the maximum number of apples you can eat.


Example 1:

Input: apples = [1,2,3,5,2], days = [3,2,1,4,2]
Output: 7
Explanation: You can eat 7 apples:
- On the first day, you eat an apple that grew on the first day.
- On the second day, you eat an apple that grew on the second day.
- On the third day, you eat an apple that grew on the second day. After this day, the apples that grew on the third day rot.
- On the fourth to the seventh days, you eat apples that grew on the fourth day.

Example 2:

Input: apples = [3,0,0,0,0,2], days = [3,0,0,0,0,2]
Output: 5
Explanation: You can eat 5 apples:
- On the first to the third day you eat apples that grew on the first day.
- Do nothing on the fouth and fifth days.
- On the sixth and seventh days you eat apples that grew on the sixth day.


Constraints:

n == apples.length == days.length
1 <= n <= 2 * 10^4
0 <= apples[i], days[i] <= 2 * 10^4
days[i] = 0 if and only if apples[i] = 0.

"""

# V0
# IDEA : GREEDY + MIN HEAP (always eat the batch that rots soonest)
#
#   exchange argument : if two batches are both still fresh today, eating
#   from the one with the EARLIER expiry never loses - the later batch is
#   still available tomorrow, the earlier one may not be.
#
#   heap entry = (last_edible_day, remaining_count)
#     apples grown on day i rot ON day i + days[i]
#     -> last edible day = i + days[i] - 1
#
#   each simulated day i:
#     1) push today's batch (if any)
#     2) pop every batch whose last day < i  (already rotten)
#     3) eat one from the heap top, push the rest back if still fresh
#
#   NOTE : we keep looping past day n-1 while the heap is non-empty,
#          because leftover apples can still be eaten after the n days.
#
# time = O((n + M) * log n), M = max(days), space = O(n)
import heapq
class Solution(object):
    def eatenApples(self, apples, days):
        n = len(apples)
        h = []          # (last_edible_day, count)
        res = 0
        i = 0

        while i < n or h:
            if i < n and apples[i] > 0:
                heapq.heappush(h, (i + days[i] - 1, apples[i]))

            # drop rotten batches
            while h and h[0][0] < i:
                heapq.heappop(h)

            if h:
                last, cnt = heapq.heappop(h)
                res += 1
                cnt -= 1
                if cnt > 0 and last > i:
                    heapq.heappush(h, (last, cnt))

            i += 1

        return res
