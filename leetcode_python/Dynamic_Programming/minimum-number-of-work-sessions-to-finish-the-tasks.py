"""

1986. Minimum Number of Work Sessions to Finish the Tasks
Medium

There are n tasks assigned to you. The task times are represented as an integer array tasks of length n, where the ith task takes tasks[i] hours to finish. A work session is when you work for at most sessionTime consecutive hours and then take a break.

You should finish the given tasks in a way that satisfies the following conditions:

If you start a task in a work session, you must complete it in the same work session.
You can start a new task immediately after finishing the previous one.
You may complete the tasks in any order.

Given tasks and sessionTime, return the minimum number of work sessions needed to finish all the tasks following the conditions above.

The tests are generated such that sessionTime is greater than or equal to the maximum element in tasks[i].


Example 1:

Input: tasks = [1,2,3], sessionTime = 3
Output: 2
Explanation: You can finish the tasks in two work sessions.
- First work session: finish the first and the second tasks in 1 + 2 = 3 hours.
- Second work session: finish the third task in 3 hours.

Example 2:

Input: tasks = [3,1,3,1,1], sessionTime = 8
Output: 2
Explanation: You can finish the tasks in two work sessions.
- First work session: finish all the tasks except the last one in 3 + 1 + 3 + 1 = 8 hours.
- Second work session: finish the last task in 1 hour.

Example 3:

Input: tasks = [1,2,3,4,5], sessionTime = 15
Output: 1
Explanation: You can finish all the tasks in one work session.


Constraints:

n == tasks.length
1 <= n <= 14
1 <= tasks[i] <= 10
max(tasks[i]) <= sessionTime <= 15

"""

# V0
# IDEA : BITMASK DP + SUBSET ENUMERATION
#
#   n <= 14 -> a mask over the 14 tasks is only 16384 states.
#
#   step 1 : ok[m] = True iff the tasks in mask m fit into ONE session,
#            i.e. sum(tasks in m) <= sessionTime. build the sums with
#            sum[m] = sum[m without lowest bit] + tasks[lowest bit].
#
#   step 2 : f[m] = min sessions to finish exactly the tasks in m
#            f[m] = min over sub-masks s of m with ok[s] of f[m ^ s] + 1
#
#   NOTE : the standard submask walk `s = (s - 1) & m` visits every subset of
#          m exactly once, giving the classic O(3^n) total.
#
# time = O(3^n), space = O(2^n)
class Solution(object):
    def minSessions(self, tasks, sessionTime):
        n = len(tasks)
        size = 1 << n

        # total time of each mask, built incrementally
        cost = [0] * size
        for m in range(1, size):
            low = m & -m
            i = low.bit_length() - 1
            cost[m] = cost[m ^ low] + tasks[i]

        INF = float("inf")
        f = [INF] * size
        f[0] = 0
        for m in range(1, size):
            s = m
            while s:
                if cost[s] <= sessionTime and f[m ^ s] + 1 < f[m]:
                    f[m] = f[m ^ s] + 1
                s = (s - 1) & m
        return f[size - 1]
