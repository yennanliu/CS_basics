"""

2589. Minimum Time to Complete All Tasks
Hard

There is a computer that can run an unlimited number of tasks at the same time. You are given a 2D integer array tasks where tasks[i] = [starti, endi, durationi] indicates that the ith task should run for a total of durationi seconds (not necessarily continuous) within the inclusive time range [starti, endi].

You may turn on the computer only when it needs to run a task. You can also turn it off if it is idle.

Return the minimum time during which the computer should be turned on to complete all tasks.


Example 1:

Input: tasks = [[2,3,1],[4,5,1],[1,5,2]]
Output: 2
Explanation:
- The first task can be run in the inclusive time range [2, 2].
- The second task can be run in the inclusive time range [5, 5].
- The third task can be run in the two inclusive time ranges [2, 2] and [5, 5].
The computer will be on for a total of 2 seconds.

Example 2:

Input: tasks = [[1,3,2],[2,5,3],[5,6,2]]
Output: 4
Explanation:
- The first task can be run in the inclusive time range [2, 3].
- The second task can be run in the inclusive time ranges [2, 3] and [5, 5].
- The third task can be run in the two inclusive time range [5, 6].
The computer will be on for a total of 4 seconds.


Constraints:

1 <= tasks.length <= 2000
tasks[i].length == 3
1 <= starti, endi <= 2000
1 <= durationi <= endi - starti + 1

"""

# V0
# IDEA : GREEDY (SORT BY END TIME, THEN TURN ON THE LATEST SECONDS)
#
#   restate the problem : pick a set S of integer seconds so that every task
#   [start, end, duration] has at least `duration` of its seconds inside S.
#   Minimize |S|.
#
#   process the tasks in increasing order of `end`. For the current task, count
#   how many of its seconds are ALREADY on (they count towards its duration for
#   free), then switch on the remaining seconds as LATE as possible, walking
#   backwards from `end`.
#
#   why "as late as possible" is optimal : every task still unprocessed has an
#   end time >= this task's end, so its window extends further right. A second
#   near the right edge is therefore contained in a superset of the future
#   windows compared to a second further left -- it can only be reused more.
#
#   NOTE : the "already on" seconds must be counted BEFORE turning any new one
#          on, otherwise the task's requirement gets double counted.
#
# time = O(n log n + n * m), space = O(m), with m = 2001 the time axis
class Solution(object):
    def findMinimumTime(self, tasks):
        MAX_T = 2001
        on = [0] * (MAX_T + 1)
        res = 0
        for start, end, duration in sorted(tasks, key=lambda t: t[1]):
            need = duration - sum(on[start:end + 1])
            t = end
            while need > 0 and t >= start:
                if not on[t]:
                    on[t] = 1
                    need -= 1
                    res += 1
                t -= 1
        return res
