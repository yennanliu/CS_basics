"""

1723. Find Minimum Time to Finish All Jobs
Hard

You are given an integer array jobs, where jobs[i] is the amount of time it takes to complete the ith job.

There are k workers that you can assign jobs to. Each job should be assigned to exactly one worker. The working time of a worker is the sum of the time it takes to complete all jobs assigned to them. Your goal is to devise an optimal assignment such that the maximum working time of any worker is minimized.

Return the minimum possible maximum working time of any assignment.


Example 1:

Input: jobs = [3,2,3], k = 3
Output: 3
Explanation: By assigning each person one job, the maximum time is 3.

Example 2:

Input: jobs = [1,2,4,7,8], k = 2
Output: 11
Explanation: Assign the jobs the following way:
Worker 1: 1, 2, 8 (working time = 1 + 2 + 8 = 11)
Worker 2: 4, 7 (working time = 4 + 7 = 11)
The maximum working time is 11.


Constraints:

1 <= k <= jobs.length <= 12
1 <= jobs[i] <= 10^7

"""

# V0
# IDEA : BACKTRACKING WITH BRANCH-AND-BOUND PRUNING
#
#   assign jobs one by one to one of the k workers, tracking each worker's
#   load in `load`. two prunings turn the naive k^n search into something
#   that finishes instantly for n <= 12:
#
#   1) SORT jobs DESCENDING. big jobs first means the load array blows past
#      the incumbent best early, so bad branches die near the root.
#
#   2) SYMMETRY BREAK : after trying job i on an EMPTY worker, stop. all
#      remaining empty workers are interchangeable, so putting the job in
#      worker #3 instead of worker #2 produces a relabelled duplicate.
#
#   3) BOUND : skip a worker whose load would reach the current best answer -
#      that branch cannot possibly improve it.
#
# time = O(k^n) worst case, heavily pruned (n <= 12), space = O(n + k)
class Solution(object):
    def minimumTimeRequired(self, jobs, k):
        jobs = sorted(jobs, reverse=True)
        n = len(jobs)
        load = [0] * k
        self.res = sum(jobs)

        def dfs(i):
            if i == n:
                self.res = min(self.res, max(load))
                return
            for j in range(k):
                if load[j] + jobs[i] >= self.res:
                    continue
                load[j] += jobs[i]
                dfs(i + 1)
                load[j] -= jobs[i]
                # this worker was empty -> every later empty worker is a twin
                if load[j] == 0:
                    break

        dfs(0)
        return self.res
