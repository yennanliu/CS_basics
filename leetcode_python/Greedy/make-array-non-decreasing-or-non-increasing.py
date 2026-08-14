"""

2263. Make Array Non-decreasing or Non-increasing
Hard

You are given a 0-indexed integer array nums. In one operation, you can:

Choose an index i in the range 0 <= i < nums.length
Set nums[i] to nums[i] + 1 or nums[i] - 1

Return the minimum number of operations to make nums non-decreasing or non-increasing.


Example 1:

Input: nums = [3,2,4,5,0]
Output: 4
Explanation:
One possible way to turn nums into non-increasing order is to:
- Add 1 to nums[1] once so that it becomes 3.
- Subtract 1 from nums[2] once so it becomes 3.
- Subtract 1 from nums[3] twice so it becomes 3.
After doing the 4 operations, nums becomes [3,3,3,3,0] which is in non-increasing order.
Note that it is also possible to turn nums into [4,4,4,4,0] in 4 operations.
It can be proven that 4 is the minimum number of operations needed.

Example 2:

Input: nums = [2,2,3,4]
Output: 0
Explanation: nums is already in non-decreasing order, so no operations are needed and we return 0.

Example 3:

Input: nums = [0]
Output: 0
Explanation: nums is already in non-decreasing order, so no operations are needed and we return 0.


Constraints:

1 <= nums.length <= 1000
0 <= nums[i] <= 1000

Follow up: Can you solve it in O(n*log(n)) time complexity?

"""

# V0
# IDEA : SLOPE TRICK / MAX-HEAP (isotonic L1 regression)
#
#   making the array non-decreasing with minimum sum |new - old| is the classic
#   L1 isotonic regression. the slope-trick invariant:
#
#     keep a max-heap of the "kink" points of the convex cost function f_i(y)
#     (cost of the prefix when the last value is fixed to y).
#     push x; if the heap top t is > x then the optimum forces the last value
#     down to x, paying (t - x), and the top kink moves from t to x.
#
#   the non-increasing case is the same run on the reversed array.
#   answer = min(the two runs).
#
#   NOTE : this is O(n log n), which also answers the follow-up (the plain
#          O(n * maxValue) DP over 0..1000 would work too but is slower).
#
# time = O(n log n), space = O(n)
import heapq
class Solution(object):
    def convertArray(self, nums):
        def cost(arr):
            heap = []          # max-heap via negated values
            res = 0
            for x in arr:
                heapq.heappush(heap, -x)
                top = -heap[0]
                if top > x:
                    res += top - x
                    heapq.heappop(heap)
                    heapq.heappush(heap, -x)
            return res

        return min(cost(nums), cost(nums[::-1]))
