"""

3264. Final Array State After K Multiplication Operations I
Easy

You are given an integer array nums, an integer k, and an integer multiplier.

You need to perform k operations on nums. In each operation:

Find the minimum value x in nums. If there are multiple occurrences of the minimum value, select the one that appears first.
Replace the selected minimum value x with x * multiplier.

Return an integer array denoting the final state of nums after performing all k operations.


Example 1:

Input: nums = [2,1,3,5,6], k = 5, multiplier = 2
Output: [8,4,6,5,6]
Explanation:

Operation      Result
After 1        [2, 2, 3, 5, 6]
After 2        [4, 2, 3, 5, 6]
After 3        [4, 4, 3, 5, 6]
After 4        [4, 4, 6, 5, 6]
After 5        [8, 4, 6, 5, 6]

Example 2:

Input: nums = [1,2], k = 3, multiplier = 4
Output: [16,8]
Explanation:

Operation      Result
After 1        [4, 2]
After 2        [4, 8]
After 3        [16, 8]


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100
1 <= k <= 10
1 <= multiplier <= 5

"""

# V0
# IDEA : MIN-HEAP KEYED BY (VALUE, INDEX) — THE TIE-BREAK IS THE INDEX
#
#   "the first occurrence of the minimum" is exactly the tuple order
#   (value, index), so one heap reproduces the rule directly.
#
#   each operation pops that pair, multiplies, and pushes it back, so the
#   array is rebuilt from the heap at the end.
#
#   k is only 10 here; the sequel (LC 3266) takes k to 10^9 and needs the
#   bulk-multiplication shortcut.
#
# time = O((n + k) log n), space = O(n)
import heapq


class Solution(object):
    def getFinalState(self, nums, k, multiplier):
        heap = [(v, i) for i, v in enumerate(nums)]
        heapq.heapify(heap)
        for _ in range(k):
            v, i = heapq.heappop(heap)
            heapq.heappush(heap, (v * multiplier, i))

        res = [0] * len(nums)
        for v, i in heap:
            res[i] = v
        return res
