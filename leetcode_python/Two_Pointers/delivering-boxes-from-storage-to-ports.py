"""

1687. Delivering Boxes from Storage to Ports
Hard

You have the task of delivering some boxes from storage to their ports using only one ship. However,
this ship has a limit on the number of boxes and the total weight that it can carry.

You are given an array boxes, where boxes[i] = [portsi, weighti], and three integers portsCount,
maxBoxes, and maxWeight.

- portsi is the port where you need to deliver the ith box and weightsi is the weight of the ith box.
- portsCount is the number of ports.
- maxBoxes and maxWeight are the respective box and weight limits of the ship.

The boxes need to be delivered in the order they are given. The ship will follow these steps:

- The ship will take some number of boxes from the boxes queue, not violating the maxBoxes and
  maxWeight constraints.
- For each loaded box in order, the ship will make a trip to the port the box needs to be delivered
  to and deliver it. If the ship is already at the correct port, no trip is needed, and the box can
  immediately be delivered.
- The ship then makes a return trip to storage to take more boxes from the queue.

The ship must end at storage after all the boxes have been delivered.

Return the minimum number of trips the ship needs to make to deliver all boxes to their respective
ports.


Example 1:

Input: boxes = [[1,1],[2,1],[1,1]], portsCount = 2, maxBoxes = 3, maxWeight = 3
Output: 4
Explanation: The optimal strategy is as follows:
- The ship takes all the boxes in the queue, goes to port 1, then port 2, then port 1 again, then
  returns to storage. 4 trips.
So the total number of trips is 4.
Note that the first and third boxes cannot be delivered together because the boxes need to be
delivered in order (i.e. the second box needs to be delivered at port 2 before the third box).

Example 2:

Input: boxes = [[1,2],[3,3],[3,1],[3,1],[2,4]], portsCount = 3, maxBoxes = 3, maxWeight = 6
Output: 6
Explanation: The optimal strategy is as follows:
- The ship takes the first box, goes to port 1, then returns to storage. 2 trips.
- The ship takes the second, third and fourth boxes, goes to port 3, then returns to storage. 2 trips.
- The ship takes the fifth box, goes to port 2, then returns to storage. 2 trips.
So the total number of trips is 2 + 2 + 2 = 6.

Example 3:

Input: boxes = [[1,4],[1,2],[2,1],[2,1],[3,2],[3,4]], portsCount = 3, maxBoxes = 6, maxWeight = 7
Output: 6
Explanation: The optimal strategy is as follows:
- The ship takes the first and second boxes, goes to port 1, then returns to storage. 2 trips.
- The ship takes the third and fourth boxes, goes to port 2, then returns to storage. 2 trips.
- The ship takes the fifth and sixth boxes, goes to port 3, then returns to storage. 2 trips.
So the total number of trips is 2 + 2 + 2 = 6.


Constraints:

1 <= boxes.length <= 10^5
1 <= portsCount, maxBoxes, maxWeight <= 10^5
1 <= portsi <= portsCount
1 <= weightsi <= maxWeight

"""

# V0
# IDEA : DP + SLIDING-WINDOW MINIMUM (monotonic deque) -- O(n), not O(n * maxBoxes)
#
#   boxes must go in order, so a plan is just a split of the array into blocks.
#   cost of the block [j, i) :
#       1 (leave storage) + (#port changes inside the block) + 1 (return)
#     = cs[i-1] - cs[j] + 2      with cs = prefix count of ports[t] != ports[t+1]
#
#   dp[i] = min over feasible j of  dp[j] + cs[i-1] - cs[j] + 2
#         = cs[i-1] + 2 + min over feasible j of ( dp[j] - cs[j] )
#
#   feasibility of j for a given i :  j >= i - maxBoxes  AND  ws[i] - ws[j] <= maxWeight
#   both lower bounds move MONOTONICALLY right as i grows -> a monotonic deque
#   holding candidate j's by increasing key (dp[j] - cs[j]) gives the min in O(1).
#
#   NOTE : the window is never empty -- j = i-1 always qualifies because a single
#          box weighs at most maxWeight and maxBoxes >= 1.
#
# time = O(n), space = O(n)
from collections import deque
class Solution(object):
    def boxDelivering(self, boxes, portsCount, maxBoxes, maxWeight):
        n = len(boxes)

        # ws[i] = total weight of the first i boxes
        ws = [0] * (n + 1)
        for i in range(n):
            ws[i + 1] = ws[i] + boxes[i][1]

        # cs[t] = number of port changes among boxes[0..t]
        cs = [0] * n
        for t in range(1, n):
            cs[t] = cs[t - 1] + (1 if boxes[t][0] != boxes[t - 1][0] else 0)

        INF = float("inf")
        dp = [INF] * (n + 1)
        key = [0] * (n + 1)          # key[j] = dp[j] - cs[j]
        dp[0] = 0
        key[0] = 0

        dq = deque([0])
        for i in range(1, n + 1):
            while dq and (dq[0] < i - maxBoxes or ws[i] - ws[dq[0]] > maxWeight):
                dq.popleft()
            j = dq[0]
            dp[i] = key[j] + cs[i - 1] + 2

            if i < n:
                key[i] = dp[i] - cs[i]
                while dq and key[dq[-1]] >= key[i]:
                    dq.pop()
                dq.append(i)

        return dp[n]
