"""

857. Minimum Cost to Hire K Workers
Hard

There are n workers. You are given two integer arrays quality and wage where
quality[i] is the quality of the ith worker and wage[i] is the minimum wage
expectation for the ith worker.

We want to hire exactly k workers to form a paid group. To hire a group of k
workers, we must pay them according to the following rules:

1. Every worker in the paid group must be paid at least their minimum wage expectation.
2. In the group, each worker's pay must be directly proportional to their quality.
   This means if a worker's quality is double that of another worker in the group,
   then they must be paid twice as much as the other worker.

Given the integer k, return the least amount of money needed to form a paid group
satisfying the above conditions. Answers within 10^-5 of the actual answer will be
accepted.


Example 1:

Input: quality = [10,20,5], wage = [70,50,30], k = 2
Output: 105.00000
Explanation: We pay 70 to 0th worker and 35 to 2nd worker.

Example 2:

Input: quality = [3,1,10,10,1], wage = [4,8,2,2,7], k = 3
Output: 30.66667
Explanation: We pay 4 to 0th worker, 13.33333 to 2nd and 3rd workers separately.


Constraints:

n == quality.length == wage.length
1 <= k <= n <= 10^4
1 <= quality[i], wage[i] <= 10^4

"""

# V0
# IDEA : GREEDY (sort by wage/quality ratio) + MAX HEAP on quality
#
#   Pay must be proportional to quality, so the group is paid
#       rate * quality[i]   for every hired worker i
#   and rate must satisfy rate >= wage[i] / quality[i] for all of them,
#   i.e. rate = max ratio inside the group. Total cost = rate * sum(quality).
#
#   So: sort workers by ratio ascending and walk through them. When worker r
#   is the LAST one considered, its ratio is the group rate, and we want the
#   k-1 cheapest qualities among the workers before it.
#   -> keep a max-heap of size k over quality, always evicting the largest.
#
# time  = O(n * log(n))
# space = O(n)
import heapq
class Solution(object):
    def mincostToHireWorkers(self, quality, wage, k):
        # (wage/quality ratio, quality), ascending by ratio
        workers = sorted((w / float(q), q) for q, w in zip(quality, wage))

        heap = []               # max-heap of qualities (store negatives)
        total_quality = 0
        ans = float('inf')

        for ratio, q in workers:
            heapq.heappush(heap, -q)
            total_quality += q

            # drop the worst (largest) quality once we exceed k workers
            if len(heap) > k:
                total_quality += heapq.heappop(heap)   # pop gives -max_quality

            if len(heap) == k:
                # `ratio` is the largest ratio in the current group -> the rate
                ans = min(ans, ratio * total_quality)

        return ans
