"""

3266. Final Array State After K Multiplication Operations II
Hard

You are given an integer array nums, an integer k, and an integer multiplier.

You need to perform k operations on nums. In each operation:

Find the minimum value x in nums. If there are multiple occurrences of the minimum value, select the one that appears first.
Replace the selected minimum value x with x * multiplier.

After the k operations, apply modulo 10^9 + 7 to every value in nums.

Return an integer array denoting the final state of nums after performing all k operations and then applying the modulo.


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
After modulo   [8, 4, 6, 5, 6]

Example 2:

Input: nums = [100000,2000], k = 2, multiplier = 1000000
Output: [999999307,999999993]
Explanation:

Operation      Result
After 1        [100000, 2000000000]
After 2        [100000000000, 2000000000]
After modulo   [999999307, 999999993]


Constraints:

1 <= nums.length <= 10^4
1 <= nums[i] <= 10^9
1 <= k <= 10^9
1 <= multiplier <= 10^6

"""

# V0
# IDEA : SIMULATE UNTIL THE VALUES LEVEL OFF, THEN FINISH IN BULK
#
#   while some element is still below the original maximum the heap must be
#   walked one step at a time. but that phase is short : every operation at
#   least doubles an element (multiplier >= 2), so after about
#   n * log2(max) steps every value has caught up with the maximum.
#
#   from that point the heap is "flat" — one full sweep multiplies everybody
#   once — so the remaining r operations split evenly : each element gets
#   r // n more multiplications, and the first r % n of the current heap
#   order get one extra.
#
#   multiplier == 1 changes nothing at all, so it is answered immediately.
#
#   the values explode past 64 bits, so the bulk phase uses pow(..., MOD).
#
# time = O(n log(max) * log n + n log n), space = O(n)
import heapq


class Solution(object):
    def getFinalState(self, nums, k, multiplier):
        MOD = 10 ** 9 + 7
        n = len(nums)
        if multiplier == 1:
            return [v % MOD for v in nums]

        heap = [(v, i) for i, v in enumerate(nums)]
        heapq.heapify(heap)
        limit = max(nums)

        # phase 1 : real simulation until every value reaches the initial max
        while k and heap[0][0] < limit:
            v, i = heapq.heappop(heap)
            heapq.heappush(heap, (v * multiplier, i))
            k -= 1

        # phase 2 : the heap order is now stable, so spread what is left
        heap.sort()
        res = [0] * n
        base, extra = divmod(k, n)
        for t, (v, i) in enumerate(heap):
            times = base + (1 if t < extra else 0)
            res[i] = v % MOD * pow(multiplier, times, MOD) % MOD
        return res
