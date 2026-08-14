"""

2530. Maximal Score After Applying K Operations
Medium

You are given a 0-indexed integer array nums and an integer k. You have a starting score of 0.

In one operation:

1. choose an index i such that 0 <= i < nums.length,
2. increase your score by nums[i], and
3. replace nums[i] with ceil(nums[i] / 3).

Return the maximum possible score you can attain after applying exactly k operations.

The ceiling function ceil(val) is the least integer greater than or equal to val.


Example 1:

Input: nums = [10,10,10,10,10], k = 5
Output: 50
Explanation: Apply the operation to each array element exactly once. The final score is 10 + 10 + 10 + 10 + 10 = 50.

Example 2:

Input: nums = [1,10,3,3,3], k = 3
Output: 17
Explanation: You can do the following operations:
Operation 1: Select i = 1, so nums becomes [1,4,3,3,3]. Your score increases by 10.
Operation 2: Select i = 1, so nums becomes [1,2,3,3,3]. Your score increases by 4.
Operation 3: Select i = 2, so nums becomes [1,2,1,3,3]. Your score increases by 3.
The final score is 10 + 4 + 3 = 17.


Constraints:

1 <= nums.length, k <= 10^5
1 <= nums[i] <= 10^9

"""

# V0
# IDEA : GREEDY + MAX HEAP
#
#   every operation is independent : taking the current maximum can never hurt,
#   since the value we leave behind (ceil(v / 3)) is monotonic in v. so at each
#   of the k steps just pop the largest element, add it to the score, and push
#   back ceil(v / 3).
#
#   NOTE : python's heapq is a MIN heap, so we store negated values.
#   NOTE : ceil(v / 3) must be done with INTEGER math -> (v + 2) // 3.
#          float division (math.ceil(v / 3)) loses precision for v up to 10^9.
#
# time = O(n + k * log n), space = O(n)
import heapq


class Solution(object):
    def maxKelements(self, nums, k):
        heap = [-v for v in nums]
        heapq.heapify(heap)
        score = 0
        for _ in range(k):
            v = -heapq.heappop(heap)
            score += v
            heapq.heappush(heap, -((v + 2) // 3))
        return score
