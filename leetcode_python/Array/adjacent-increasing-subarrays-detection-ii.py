"""

3350. Adjacent Increasing Subarrays Detection II
Medium

Given an array nums of n integers, find the maximum value of k for which there exist two adjacent subarrays of length k each, such that both subarrays are strictly increasing. Specifically, check if there are two subarrays of length k starting at indices a and b (a < b), where:

Both subarrays nums[a..a + k - 1] and nums[b..b + k - 1] are strictly increasing.
The subarrays must be adjacent, meaning b = a + k.

Return the maximum possible value of k.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [2,5,7,8,9,2,3,4,3,1]
Output: 3
Explanation:
The subarray starting at index 2 is [7, 8, 9], which is strictly increasing.
The subarray starting at index 5 is [2, 3, 4], which is also strictly increasing.
These two subarrays are adjacent, and 3 is the maximum possible value of k for which two such adjacent strictly increasing subarrays exist.

Example 2:

Input: nums = [1,2,3,4,4,4,4,5,6,7]
Output: 2
Explanation:
The subarray starting at index 0 is [1, 2], which is strictly increasing.
The subarray starting at index 2 is [3, 4], which is also strictly increasing.
These two subarrays are adjacent, and 2 is the maximum possible value of k for which two such adjacent strictly increasing subarrays exist.


Constraints:

2 <= nums.length <= 2 * 10^5
-10^9 <= nums[i] <= 10^9

"""

# V0
# IDEA : SPLIT INTO MAXIMAL RISING RUNS — THE ANSWER LIVES INSIDE OR ACROSS ONE BOUNDARY
#
#   two adjacent blocks of length k are both strictly increasing in exactly
#   two situations :
#
#       both sit inside ONE maximal rising run    -> k can be run // 2
#       they straddle the boundary between two    -> k can be min(prev, cur)
#
#   so a single pass over the run lengths answers it — no per-k scanning.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxIncreasingSubarrays(self, nums):
        n = len(nums)
        best = 0
        prev_run = 0
        run = 1
        for i in range(1, n):
            if nums[i] > nums[i - 1]:
                run += 1
            else:
                best = max(best, run // 2, min(prev_run, run))
                prev_run = run
                run = 1
        best = max(best, run // 2, min(prev_run, run))
        return best


# V0-1
# IDEA : BINARY SEARCH ON k + O(n) FEASIBILITY TEST
#
#   feasibility is monotone : if two adjacent rising blocks of length k exist,
#   trimming both toward their shared boundary gives two of length k - 1.
#   so binary search k in [1, n // 2] and test with the run-length DP
#
#       end[i] = length of the rising run ending at i
#
#   where "the k items starting at b rise" is simply end[b + k - 1] >= k.
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def maxIncreasingSubarrays(self, nums):
        n = len(nums)
        end = [1] * n
        for i in range(1, n):
            if nums[i] > nums[i - 1]:
                end[i] = end[i - 1] + 1

        def feasible(k):
            return any(end[b - 1] >= k and end[b + k - 1] >= k
                       for b in range(k, n - k + 1))

        lo, hi = 1, n // 2
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if feasible(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo


# V0-2
# IDEA : DP ON THE MEETING POINT OF THE TWO BLOCKS
#
#   end[i]   = length of the rising run that ENDS at i
#   start[i] = length of the rising run that STARTS at i
#
#   the two blocks touch at exactly one index b, and the best k for that b is
#   min(end[b - 1], start[b]) : block 1 can reach back that far and block 2
#   can reach forward that far.  taking the max over every b covers both the
#   "inside one run" case (b in the middle of a run) and the "straddling a
#   break" case, so no run bookkeeping is needed at all.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def maxIncreasingSubarrays(self, nums):
        n = len(nums)
        end = [1] * n
        start = [1] * n
        for i in range(1, n):
            if nums[i] > nums[i - 1]:
                end[i] = end[i - 1] + 1
        for i in range(n - 2, -1, -1):
            if nums[i] < nums[i + 1]:
                start[i] = start[i + 1] + 1
        return max(min(end[b - 1], start[b]) for b in range(1, n))
