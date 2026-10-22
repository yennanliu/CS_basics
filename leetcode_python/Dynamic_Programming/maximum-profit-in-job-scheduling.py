"""

1235. Maximum Profit in Job Scheduling
Hard

We have n jobs, where every job is scheduled to be done from startTime[i] to endTime[i], obtaining a profit of profit[i].

You're given the startTime, endTime and profit arrays, return the maximum profit you can take such that there are no two jobs in the subset with overlapping time range.

If you choose a job that ends at time X you will be able to start another job that starts at time X.


Example 1:

Input: startTime = [1,2,3,3], endTime = [3,4,5,6], profit = [50,10,40,70]
Output: 120
Explanation: The subset chosen is the first and fourth job.
Time range [1-3]+[3-6] , we get profit of 120 = 50 + 70.

Example 2:

Input: startTime = [1,2,3,4,6], endTime = [3,5,10,6,9], profit = [20,20,100,70,60]
Output: 150
Explanation: The subset chosen is the first, fourth and fifth job.
Profit obtained 150 = 20 + 70 + 60.

Example 3:

Input: startTime = [1,1,1], endTime = [2,3,4], profit = [5,6,4]
Output: 6


Constraints:

1 <= startTime.length == endTime.length == profit.length <= 5 * 10^4
1 <= startTime[i] < endTime[i] <= 10^9
1 <= profit[i] <= 10^4

"""

# V0
# IDEA: DP + BINARY SEARCH (weighted interval scheduling) (claude)
"""
 DP def:
    - sort jobs by END time
    - dp[i] = max profit using the first i jobs (in sorted order)

 DP eq:
    - dp[i + 1] = max(dp[i], dp[k] + profit[i])
      where k = # of jobs (among first i) whose end time <= start time of job i
      -> found by binary search, since the end times are sorted
"""
# time = O(n log n)
# space = O(n)
import bisect
class Solution(object):
    def jobScheduling(self, startTime, endTime, profit):
        # sort by end time (so `ends` below is sorted -> binary searchable)
        jobs = sorted(zip(endTime, startTime, profit))
        n = len(jobs)
        ends = [j[0] for j in jobs]

        # dp[i] = best profit using first i jobs ; dp is non-decreasing
        dp = [0] * (n + 1)
        for i in range(n):
            _, s, p = jobs[i]
            """
            NOTE !!!

                bisect_right(ends, s, 0, i)
                -> the count of jobs among jobs[0..i-1] with end <= s
                -> those are exactly the jobs compatible with job i
                (bisect_right, NOT bisect_left, since end == start is allowed)
            """
            k = bisect.bisect_right(ends, s, 0, i)
            dp[i + 1] = max(dp[i], dp[k] + p)

        return dp[n]


# V0-1
# IDEA: DP + BINARY SEARCH (weighted interval scheduling) (GEMINI)
import bisect

class Solution(object):

  def jobScheduling(self, startTime, endTime, profit):
    """:type startTime: List[int]

    :type endTime: List[int]
    :type profit: List[int]
    :rtype: int
    """
    # 1. 將工作打包並根據「結束時間」進行排序
    jobs = sorted(zip(startTime, endTime, profit), key=lambda x: x[1])

    # 提取排序後的結束時間，專門用於二分搜尋
    ends = [job[1] for job in jobs]
    n = len(jobs)

    # dp[i] 表示考慮前 i 個工作（0 到 i-1）的最大利潤
    dp = [0] * (n + 1)

    for i in range(1, n + 1):
      start, end, prof = jobs[i - 1]

      # 2. 利用二分搜尋（bisect_right）尋找「結束時間 <= 當前 start」的最晚工作
      # bisect_right 會找出第一個大於 start 的位置 idx，
      # 因此 idx - 1 就是符合條件且結束時間最接近 start 的工作索引
      idx = bisect.bisect_right(ends, start, 0, i - 1)

      # 3. 狀態轉移：不選當前工作 vs 選當前工作（加上相容前置工作的最大利潤）
      include_profit = prof + dp[idx]
      exclude_profit = dp[i - 1]

      dp[i] = max(include_profit, exclude_profit)

    return dp[n]


# V0-2
# IDEA: DP + BINARY SEARCH (weighted interval scheduling) (GPT)
from bisect import bisect_right

class Solution(object):
    def jobScheduling(self, startTime, endTime, profit):
        """
        :type startTime: List[int]
        :type endTime: List[int]
        :type profit: List[int]
        :rtype: int
        """
        n = len(startTime)

        # Sort jobs by end time.
        jobs = sorted(
            zip(startTime, endTime, profit),
            key=lambda job: job[1]
        )

        # ends[i] is the end time of the i-th sorted job.
        ends = [job[1] for job in jobs]

        # dp[i] = maximum profit using the first i jobs.
        # dp[0] = 0 because no jobs means no profit.
        dp = [0] * (n + 1)

        for i in range(n):
            start, end, p = jobs[i]

            # Find how many earlier jobs finish at or before
            # this job starts.
            #
            # Search only [0, i), because the current job
            # and later jobs must not be included.
            prev = bisect_right(ends, start, 0, i)

            # Option 1: Skip the current job.
            skip = dp[i]

            # Option 2: Take the current job.
            # Combine its profit with the best compatible jobs.
            take = dp[prev] + p

            dp[i + 1] = max(skip, take)

        return dp[n]


# V1
# IDEA: MAX-HEAP / SWEEP (sort by start, pop finished jobs)
"""

DP def
    (weighted interval scheduling - jobs sorted by END time)

    dp[i]: MAX profit using the first i jobs (in sorted order)

           -> dp is non-decreasing

DP eq

     dp[i+1] = max(
                  dp[i],                 # SKIP job i
                  dp[k] + profit[i]      # TAKE job i
               )

     where k = number of jobs among the first i whose END <= start of job i


    -> e.g. NOTE !!! k = bisect_right(ends, s, 0, i) - bisect_RIGHT, not
              left, because end == start is allowed (a job may start exactly
              when another ends)

     init: dp[0] = 0
     ans = dp[n]

"""
# time = O(n log n)
# space = O(n)
import heapq
class Solution(object):
    def jobScheduling(self, startTime, endTime, profit):
        jobs = sorted(zip(startTime, endTime, profit))
        pq = []  # min-heap of (endTime, profit_so_far)
        best = 0
        for s, e, p in jobs:
            # all jobs already finished at time s can be "cashed in"
            while pq and pq[0][0] <= s:
                best = max(best, heapq.heappop(pq)[1])
            heapq.heappush(pq, (e, best + p))
        while pq:
            best = max(best, heapq.heappop(pq)[1])
        return best
