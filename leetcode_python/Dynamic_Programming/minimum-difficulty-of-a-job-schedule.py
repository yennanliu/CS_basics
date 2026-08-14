"""

1335. Minimum Difficulty of a Job Schedule
Hard

You want to schedule a list of jobs in d days. Jobs are dependent
(i.e To work on the i-th job, you have to finish all the jobs j
where 0 <= j < i).

You have to finish at least one task every day. The difficulty of a job
schedule is the sum of difficulties of each day of the d days. The difficulty
of a day is the maximum difficulty of a job done on that day.

You are given an integer array jobDifficulty and an integer d.
The difficulty of the i-th job is jobDifficulty[i].

Return the minimum difficulty of a job schedule.
If you cannot find a schedule for the jobs return -1.


Example 1:

Input: jobDifficulty = [6,5,4,3,2,1], d = 2
Output: 7
Explanation: First day you can finish the first 5 jobs, total difficulty = 6.
Second day you can finish the last job, total difficulty = 1.
The difficulty of the schedule = 6 + 1 = 7

Example 2:

Input: jobDifficulty = [9,9,9], d = 4
Output: -1
Explanation: If you finish a job per day you will still have a free day.
you cannot find a schedule for the given jobs.

Example 3:

Input: jobDifficulty = [1,1,1], d = 3
Output: 3
Explanation: The schedule is one job per day. total difficulty will be 3.


Constraints:

1 <= jobDifficulty.length <= 300
0 <= jobDifficulty[i] <= 1000
1 <= d <= 10

"""

# V0
# IDEA: 2D DP (partition array into d contiguous groups)
"""
 DP def:
    - dp[i][j] = min difficulty to finish the FIRST i jobs within EXACTLY j days

 DP eq:
    - dp[i][j] = min over k in [j, i] of
                    dp[k-1][j-1] + max(jobDifficulty[k-1 .. i-1])
      (day j does jobs k..i, 1-indexed)

 init:
    - dp[0][0] = 0, everything else = INF

 answer:
    - dp[n][d], or -1 if unreachable (n < d)
"""
# time = O(n^2 * d)
# space = O(n * d)
class Solution(object):
    def minDifficulty(self, jobDifficulty, d):
        n = len(jobDifficulty)

        # edge: not enough jobs to fill d days
        if n < d:
            return -1

        INF = float('inf')
        dp = [[INF] * (d + 1) for _ in range(n + 1)]
        dp[0][0] = 0

        for i in range(1, n + 1):
            for j in range(1, min(d, i) + 1):
                mx = 0
                # day j handles jobs k..i (1-indexed)
                for k in range(i, j - 1, -1):
                    mx = max(mx, jobDifficulty[k - 1])
                    if dp[k - 1][j - 1] + mx < dp[i][j]:
                        dp[i][j] = dp[k - 1][j - 1] + mx

        return -1 if dp[n][d] == INF else dp[n][d]


# V0-1
# IDEA: TOP DOWN DP (memoized DFS)
#  distinct trick: recursion on (start index, days left)
# time = O(n^2 * d)
# space = O(n * d)
class Solution(object):
    def minDifficulty(self, jobDifficulty, d):
        n = len(jobDifficulty)
        if n < d:
            return -1

        memo = {}

        def dfs(i, days):
            # last day -> must take all remaining jobs
            if days == 1:
                return max(jobDifficulty[i:])
            if (i, days) in memo:
                return memo[(i, days)]

            res = float('inf')
            mx = 0
            # leave at least (days - 1) jobs for the remaining days
            for j in range(i, n - days + 1):
                mx = max(mx, jobDifficulty[j])
                res = min(res, mx + dfs(j + 1, days - 1))

            memo[(i, days)] = res
            return res

        return dfs(0, d)
