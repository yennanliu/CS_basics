"""

2532. Time to Cross a Bridge
Hard

There are k workers who want to move n boxes from the right (old) warehouse to the left (new) warehouse. You are given the two integers n and k, and a 2D integer array time of size k x 4 where time[i] = [right_i, pick_i, left_i, put_i].

The warehouses are separated by a river and connected by a bridge. Initially, all k workers are waiting on the left side of the bridge. To move the boxes, the ith worker can do the following:

Cross the bridge to the right side in right_i minutes.
Pick a box from the right warehouse in pick_i minutes.
Cross the bridge to the left side in left_i minutes.
Put the box into the left warehouse in put_i minutes.

The ith worker is less efficient than the jth worker if either condition is met:

left_i + right_i > left_j + right_j
left_i + right_i == left_j + right_j and i > j

The following rules regulate the movement of the workers through the bridge:

Only one worker can use the bridge at a time.
When the bridge is unused prioritize the least efficient worker (who have picked up the box) on the right side to cross. If not, prioritize the least efficient worker on the left side to cross.
If enough workers have already been dispatched from the left side to pick up all the remaining boxes, no more workers will be sent from the left side.

Return the elapsed minutes at which the last box reaches the left side of the bridge.


Example 1:

Input: n = 1, k = 3, time = [[1,1,2,1],[1,1,3,1],[1,1,4,1]]
Output: 6
Explanation:
From 0 to 1 minutes: worker 2 crosses the bridge to the right.
From 1 to 2 minutes: worker 2 picks up a box from the right warehouse.
From 2 to 6 minutes: worker 2 crosses the bridge to the left.
From 6 to 7 minutes: worker 2 puts a box at the left warehouse.
The whole process ends after 7 minutes. We return 6 because the problem asks for the instance of time at which the last worker reaches the left side of the bridge.

Example 2:

Input: n = 3, k = 2, time = [[1,5,1,8],[10,10,10,10]]
Output: 37
Explanation:
The last box reaches the left side at 37 seconds. Notice, how we do not put the last boxes down, as that would take more time, and they are already on the left with the workers.


Constraints:

1 <= n, k <= 10^4
time.length == k
time[i].length == 4
1 <= left_i, pick_i, right_i, put_i <= 1000

"""

# V0
# IDEA : EVENT SIMULATION WITH 4 HEAPS
#
#   first STABLY sort the workers by (right + left) ascending. after that the
#   *array index* is exactly the efficiency rank : a bigger index means a less
#   efficient worker, so "least efficient" == "largest index".
#
#   we keep 4 priority queues :
#     wait_left  : max-heap of indices idle on the left bank
#     wait_right : max-heap of indices idle on the right bank (box in hand)
#     work_left  : min-heap of (finish_put_time, idx)  -> becomes wait_left
#     work_right : min-heap of (finish_pick_time, idx) -> becomes wait_right
#
#   each loop : release everyone whose work finished by `cur`, then give the
#   bridge to the right bank if anyone is waiting there (rule priority),
#   otherwise send someone right if boxes are still un-dispatched. if nobody
#   can move, jump `cur` forward to the next work-completion event.
#
#   NOTE : the answer is the moment a worker ARRIVES on the left, not when the
#          box is put down -> we return `cur` right after the left-crossing,
#          before scheduling the `put`.
#   NOTE : boxes are decremented when a worker STARTS crossing to the right
#          (dispatch), which encodes the "no more workers than boxes" rule.
#
# time = O((n + k) * log k), space = O(k)
import heapq


class Solution(object):
    def findCrossingTime(self, n, k, time):
        # stable sort -> ties keep the original (more efficient = smaller) order
        t = sorted(time, key=lambda w: w[0] + w[2])

        wait_left = [-i for i in range(k)]
        heapq.heapify(wait_left)
        wait_right = []
        work_left = []
        work_right = []

        cur = 0
        while True:
            while work_left and work_left[0][0] <= cur:
                _, i = heapq.heappop(work_left)
                heapq.heappush(wait_left, -i)
            while work_right and work_right[0][0] <= cur:
                _, i = heapq.heappop(work_right)
                heapq.heappush(wait_right, -i)

            left_to_go = n > 0 and wait_left
            right_to_go = bool(wait_right)

            if not left_to_go and not right_to_go:
                nxt = None
                if work_left:
                    nxt = work_left[0][0]
                if work_right and (nxt is None or work_right[0][0] < nxt):
                    nxt = work_right[0][0]
                cur = nxt
                continue

            if right_to_go:
                i = -heapq.heappop(wait_right)
                cur += t[i][2]
                if n == 0 and not wait_right and not work_right:
                    return cur
                heapq.heappush(work_left, (cur + t[i][3], i))
            else:
                i = -heapq.heappop(wait_left)
                cur += t[i][0]
                n -= 1
                heapq.heappush(work_right, (cur + t[i][1], i))
