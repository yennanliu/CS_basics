"""

1962. Remove Stones to Minimize the Total
Medium

You are given a 0-indexed integer array piles, where piles[i] represents the number of stones in the ith pile, and an integer k. You should apply the following operation exactly k times:

Choose any piles[i] and remove floor(piles[i] / 2) stones from it.

Notice that you can apply the operation on the same pile more than once.

Return the minimum possible total number of stones remaining after applying the k operations.

floor(x) is the largest integer that is smaller than or equal to x (i.e., rounds x down).


Example 1:

Input: piles = [5,4,9], k = 2
Output: 12
Explanation: Steps of a possible scenario are:
- Apply the operation on pile 2. The resulting piles are [5,4,5].
- Apply the operation on pile 0. The resulting piles are [3,4,5].
The total number of stones in [3,4,5] is 12.

Example 2:

Input: piles = [4,3,6,7], k = 3
Output: 12
Explanation: Steps of a possible scenario are:
- Apply the operation on pile 2. The resulting piles are [4,3,3,7].
- Apply the operation on pile 3. The resulting piles are [4,3,3,4].
- Apply the operation on pile 0. The resulting piles are [2,3,3,4].
The total number of stones in [2,3,3,4] is 12.


Constraints:

1 <= piles.length <= 10^5
1 <= piles[i] <= 10^4
1 <= k <= 10^5

"""

# V0
# IDEA : GREEDY + MAX HEAP
#
#   one operation on a pile of size x removes floor(x/2) stones, so the gain
#   is monotone in x -> always hit the CURRENT largest pile.
#   exchange argument : if an optimal plan ever halves a smaller pile while a
#   larger one is available, swapping the two targets never removes fewer stones.
#
#   python's heapq is a MIN heap -> store negated values.
#   after halving, the pile keeps x - x//2 = ceil(x/2) stones.
#
# time = O(n + k*log n), space = O(n)
import heapq
class Solution(object):
    def minStoneSum(self, piles, k):
        pq = [-x for x in piles]
        heapq.heapify(pq)
        for _ in range(k):
            x = -pq[0]
            # pop + push in one shot
            heapq.heapreplace(pq, -(x - x // 2))
        return -sum(pq)
