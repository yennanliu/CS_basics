"""

3683. Earliest Time to Finish One Task
Easy

You are given a 2D integer array tasks where tasks[i] = [si, ti].

Each [si, ti] in tasks represents a task with start time si that takes ti
units of time to finish.

Return the earliest time at which at least one task is finished.


Example 1:

Input: tasks = [[1,6],[2,3]]
Output: 5
Explanation:
The first task starts at time t = 1 and finishes at time 1 + 6 = 7. The
second task finishes at time 2 + 3 = 5. You can finish one task at time 5.

Example 2:

Input: tasks = [[100,100],[100,100],[100,100]]
Output: 200
Explanation:
All three tasks finish at time 100 + 100 = 200.


Constraints:

1 <= tasks.length <= 100
tasks[i] = [si, ti]
1 <= si, ti <= 100

"""

# V0
# IDEA : EACH TASK'S FINISH TIME IS INDEPENDENT -- TAKE THE MINIMUM
#
#   there is no scheduling to do here: tasks do not compete for a resource,
#   so task i always finishes at exactly s_i + t_i regardless of what the
#   others do. "the earliest time at least one task is finished" is then
#   just the smallest of those n independent finish times.
#
# time = O(n), space = O(1)
class Solution(object):
    def earliestTime(self, tasks):
        return min(s + t for s, t in tasks)
