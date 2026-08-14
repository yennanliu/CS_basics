"""

2071. Maximum Number of Tasks You Can Assign
Hard

You have n tasks and m workers. Each task has a strength requirement stored in a 0-indexed integer array tasks, with the ith task requiring tasks[i] strength to complete. The strength of each worker is stored in a 0-indexed integer array workers, with the jth worker having workers[j] strength. Each worker can only be assigned to a single task and must have a strength greater than or equal to the task's strength requirement (i.e., workers[j] >= tasks[i]).

Additionally, you have pills magical pills that will increase a worker's strength by strength. You can decide which workers receive the magical pills, however, you may only give each worker at most one magical pill.

Given the 0-indexed integer arrays tasks and workers and the integers pills and strength, return the maximum number of tasks that can be completed.


Example 1:

Input: tasks = [3,2,1], workers = [0,3,3], pills = 1, strength = 1
Output: 3
Explanation:
We can assign the magical pill and tasks as follows:
- Give the magical pill to worker 0.
- Assign worker 0 to task 2 (0 + 1 >= 1)
- Assign worker 1 to task 1 (3 >= 2)
- Assign worker 2 to task 0 (3 >= 3)

Example 2:

Input: tasks = [5,4], workers = [0,0,0], pills = 1, strength = 5
Output: 1
Explanation:
We can assign the magical pill and tasks as follows:
- Give the magical pill to worker 0.
- Assign worker 0 to task 0 (0 + 5 >= 5)

Example 3:

Input: tasks = [10,15,30], workers = [0,10,10,10,10], pills = 3, strength = 10
Output: 2
Explanation:
We can assign the magical pills and tasks as follows:
- Give the magical pill to worker 0 and worker 1.
- Assign worker 0 to task 0 (0 + 10 >= 10)
- Assign worker 1 to task 1 (10 + 10 >= 15)
The last pill is not given because it will not make any worker strong enough for the last task.


Constraints:

n == tasks.length
m == workers.length
1 <= n, m <= 5 * 10^4
0 <= pills <= m
0 <= tasks[i], workers[j], strength <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON THE ANSWER k + GREEDY FEASIBILITY WITH A DEQUE
#
#   if k tasks can be done, so can k - 1 -> the predicate is monotone, so
#   binary search k over [0, min(n, m)].
#
#   feasibility of k : use the k EASIEST tasks and the k STRONGEST workers
#   (both sorted). process the tasks from HARDEST down to easiest, keeping a
#   deque of the workers who could handle the current task WITH a pill :
#
#     - push every worker with  w + strength >= task  into the deque
#       (workers are scanned from strongest downward, so the deque is
#        naturally ordered strongest-first / weakest-last)
#     - if the deque is empty -> infeasible
#     - if the STRONGEST available worker already clears the task unaided,
#       use them (popleft, no pill spent)
#     - otherwise spend a pill on the WEAKEST one who can be brought up to it
#       (pop from the right) — saving the stronger workers for harder tasks
#
# time = O((n log n) + (m log m) + (n + m) log(min(n, m))), space = O(m)
from collections import deque


class Solution(object):
    def maxTaskAssign(self, tasks, workers, pills, strength):
        tasks.sort()
        workers.sort()
        n, m = len(tasks), len(workers)

        def feasible(k):
            # hardest of the k easiest tasks first
            dq = deque()
            w = m - 1                 # pointer into the k strongest workers
            left = pills
            for i in range(k - 1, -1, -1):
                t = tasks[i]
                while w >= m - k and workers[w] + strength >= t:
                    dq.append(workers[w])
                    w -= 1
                if not dq:
                    return False
                if dq[0] >= t:
                    dq.popleft()      # strongest one handles it, no pill
                else:
                    if left == 0:
                        return False
                    left -= 1
                    dq.pop()          # weakest eligible one takes the pill
            return True

        lo, hi = 0, min(n, m)
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if feasible(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo
