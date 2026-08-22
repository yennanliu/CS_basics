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


# V0-1
# IDEA : SORT THE FINISH TIMES AND TAKE THE FIRST
#
#   build the list of independent finish times s_i + t_i, sort it, read index 0.
#   strictly more work than the single min-scan of V0 (a full ordering is
#   computed when only its smallest element is used), but it is the shape you
#   want the moment the question becomes "the k-th earliest task to finish"
#   instead of "the earliest" -- then the answer is just `finish[k - 1]`.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def earliestTime(self, tasks):
        finish = sorted(s + t for s, t in tasks)
        return finish[0]


# V0-2
# IDEA : COUNTING / BUCKET SCAN OVER THE BOUNDED TIME RANGE
#
#   s_i, t_i <= 100, so every finish time falls in 0..200. Mark each achieved
#   finish time in a flat boolean table, then walk the table upward and return
#   the first marked slot -- no comparison between tasks is ever made, the
#   ordering comes from the array indices themselves (counting sort).
#
#   this is the version that stays O(1) per extra task when the same table is
#   queried repeatedly (e.g. streaming tasks, or "how many finish by time T").
#
# time = O(n + MAX_T), space = O(MAX_T)
class Solution(object):
    def earliestTime(self, tasks):
        LIM = 200           # max s_i + t_i under the constraints
        seen = [False] * (LIM + 1)
        for s, t in tasks:
            seen[s + t] = True
        for v in range(LIM + 1):
            if seen[v]:
                return v
        return -1
