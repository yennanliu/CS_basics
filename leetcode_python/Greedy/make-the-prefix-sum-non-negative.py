"""

2599. Make the Prefix Sum Non-negative
Medium

You are given a 0-indexed integer array nums. You can apply the following operation any number of times:

Pick any element from nums and put it at the end of nums.

The prefix sum array of nums is an array prefix of the same length as nums such that prefix[i] is the sum of all the integers nums[j] where j is in the inclusive range [0, i].

Return the minimum number of operations such that the prefix sum array does not contain negative integers. The test cases are generated such that it is always possible to make the prefix sum array non-negative.


Example 1:

Input: nums = [2,3,-5,4]
Output: 0
Explanation: we do not need to do any operations.
The array is [2,3,-5,4]. The prefix sum array is [2, 5, 0, 4].

Example 2:

Input: nums = [3,-5,-2,6]
Output: 1
Explanation: we can do one operation on index 1.
The array after the operation is [3,-2,6,-5]. The prefix sum array is [3, 1, 7, 2].

Constraints:

1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : GREEDY + MIN HEAP (undo the most damaging negative already taken)
#
#   moving an element to the END is equivalent to DELETING it from the prefix
#   we are building -- the whole array sums to a non-negative total (guaranteed
#   solvable), so anything pushed to the back is harmless there.
#
#   sweep left to right keeping the running sum s, and push every negative seen
#   so far into a min heap. Whenever s goes negative we MUST evict something;
#   evicting the most negative element already consumed repairs s by the
#   largest possible amount for one operation, so it is optimal (an exchange
#   argument: any valid solution can swap its eviction for this one and stay
#   valid).
#
#   NOTE : `while` (not `if`) -- one eviction may not be enough to lift s back
#          to >= 0.
#   NOTE : only negatives are ever worth evicting, so positives never enter the
#          heap; the heap is therefore always non-empty when s < 0.
#
# time = O(n log n), space = O(n)
import heapq
class Solution(object):
    def makePrefSumNonNegative(self, nums):
        h = []
        s = 0
        res = 0
        for x in nums:
            s += x
            if x < 0:
                heapq.heappush(h, x)
            while s < 0:
                s -= heapq.heappop(h)
                res += 1
        return res
