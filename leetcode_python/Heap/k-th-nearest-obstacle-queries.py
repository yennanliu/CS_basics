"""

3275. K-th Nearest Obstacle Queries
Medium

There is an infinite 2D plane.

You are given a positive integer k. You are also given a 2D array queries, which contains the following queries:

queries[i] = [x, y]: Build an obstacle at coordinate (x, y) in the plane. It is guaranteed that there is no obstacle at this coordinate when this query is made.

After each query, you need to find the distance of the kth nearest obstacle from the origin.

Return an integer array results where results[i] denotes the kth nearest obstacle after query i, or results[i] == -1 if there are less than k obstacles.

Note that initially there are no obstacles anywhere.

The distance of an obstacle at coordinate (x, y) from the origin is given by |x| + |y|.


Example 1:

Input: k = 2, queries = [[1,2],[3,4],[2,3],[-3,0]]
Output: [-1,7,5,3]
Explanation:
Initially, there are 0 obstacles.
After queries[0], there are less than 2 obstacles.
After queries[1], there are obstacles at distances 3 and 7.
After queries[2], there are obstacles at distances 3, 5, and 7.
After queries[3], there are obstacles at distances 3, 3, 5, and 7.

Example 2:

Input: k = 1, queries = [[5,5],[4,4],[3,3]]
Output: [10,8,6]
Explanation:
After queries[0], there is an obstacle at distance 10.
After queries[1], there are obstacles at distances 8 and 10.
After queries[2], there are obstacles at distances 6, 8, and 10.


Constraints:

1 <= queries.length <= 2 * 10^5
All queries[i] are unique.
-10^9 <= queries[i][0], queries[i][1] <= 10^9
0 <= k <= 10^5

"""

# V0
# IDEA : A BOUNDED MAX-HEAP HOLDING THE k CLOSEST DISTANCES
#
#   only the k-th smallest distance is ever asked for, so keeping the k
#   smallest distances is enough — and a MAX-heap of exactly those has the
#   answer sitting on top.
#
#   python's heapq is a min-heap, so store negated distances : push, then pop
#   whenever the heap exceeds k, which discards the largest of the k+1.
#
#   before the heap fills up there are fewer than k obstacles, hence -1.
#
# time = O(q log k), space = O(k)
import heapq


class Solution(object):
    def resultsArray(self, queries, k):
        heap = []                     # max-heap of the k smallest distances
        res = []
        for x, y in queries:
            d = abs(x) + abs(y)
            heapq.heappush(heap, -d)
            if len(heap) > k:
                heapq.heappop(heap)
            res.append(-heap[0] if len(heap) == k else -1)
        return res
