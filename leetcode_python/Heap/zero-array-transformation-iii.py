"""

3362. Zero Array Transformation III
Medium

You are given an integer array nums of length n and a 2D array queries where queries[i] = [l_i, r_i].

Each queries[i] represents the following action on nums:

Decrement the value at each index in the range [l_i, r_i] in nums by at most 1.
The amount by which the value is decremented can be chosen independently for each index.

A Zero Array is an array with all its elements equal to 0.

Return the maximum number of elements that can be removed from queries, such that nums can still be converted to a zero array using the remaining queries. If it is not possible to convert nums to a zero array, return -1.


Example 1:

Input: nums = [2,0,2], queries = [[0,2],[0,2],[1,1]]
Output: 1
Explanation:
After removing queries[2], nums can still be converted to a zero array.
Using queries[0], decrement nums[0] and nums[2] by 1 and nums[1] by 0.
Using queries[1], decrement nums[0] and nums[2] by 1 and nums[1] by 0.

Example 2:

Input: nums = [1,1,1,1], queries = [[1,3],[0,2],[1,3],[1,2]]
Output: 2
Explanation:
We can remove queries[2] and queries[3].

Example 3:

Input: nums = [1,2,3,4], queries = [[0,3]]
Output: -1
Explanation:
nums cannot be converted to a zero array even after using all the queries.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^5
1 <= queries.length <= 10^5
queries[i].length == 2
0 <= l_i <= r_i < nums.length

"""

# V0
# IDEA : SWEEP LEFT TO RIGHT, ALWAYS SPENDING THE QUERY THAT REACHES FURTHEST
#
#   maximising the removals is the same as minimising how many queries are
#   kept. sweeping index by index, whatever demand is still unmet at i must
#   be paid by a query that starts at or before i — and among those, the one
#   with the largest right end is never worse, since it can help everything a
#   shorter one could.
#
#   so keep the available queries in a max-heap keyed by their right end,
#   adding those that start at i, and pull from it until the running coverage
#   meets nums[i]. a difference array remembers where each chosen query's
#   coverage expires.
#
#   if the heap runs dry (or its best already ended before i) the array
#   cannot be zeroed at all.
#
# time = O((n + q) log q), space = O(n + q)
import heapq


class Solution(object):
    def maxRemoval(self, nums, queries):
        n = len(nums)
        by_start = [[] for _ in range(n)]
        for l, r in queries:
            by_start[l].append(r)

        available = []                          # max-heap of right ends
        expire = [0] * (n + 1)                  # difference array of chosen coverage
        cover = 0
        used = 0

        for i in range(n):
            cover += expire[i]
            for r in by_start[i]:
                heapq.heappush(available, -r)
            while cover < nums[i]:
                if not available or -available[0] < i:
                    return -1
                r = -heapq.heappop(available)
                used += 1
                cover += 1
                expire[r + 1] -= 1
        return len(queries) - used
