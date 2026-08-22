"""

3349. Adjacent Increasing Subarrays Detection I
Easy

Given an array nums of n integers and an integer k, determine whether there exist two adjacent subarrays of length k such that both subarrays are strictly increasing. Specifically, check if there are two subarrays starting at indices a and b (a < b), where:

Both subarrays nums[a..a + k - 1] and nums[b..b + k - 1] are strictly increasing.
The subarrays must be adjacent, meaning b = a + k.

Return true if it is possible to find two such subarrays, and false otherwise.


Example 1:

Input: nums = [2,5,7,8,9,2,3,4,3,1], k = 3
Output: true
Explanation:
The subarray starting at index 2 is [7, 8, 9], which is strictly increasing.
The subarray starting at index 5 is [2, 3, 4], which is also strictly increasing.
These two subarrays are adjacent, so the result is true.

Example 2:

Input: nums = [1,2,3,4,4,4,4,5,6,7], k = 5
Output: false


Constraints:

2 <= nums.length <= 100
1 < 2 * k <= nums.length
-1000 <= nums[i] <= 1000

"""

# V0
# IDEA : TEST EVERY ANCHOR — THE SECOND BLOCK STARTS EXACTLY k LATER
#
#   with n <= 100 the direct check is fine : for each start a, verify that
#   both nums[a..a+k-1] and nums[a+k..a+2k-1] rise strictly.
#
#   the sequel (LC 3350) asks for the largest such k at 2*10^5 elements and
#   needs the run-length view instead.
#
# time = O(n * k), space = O(1)
class Solution(object):
    def hasIncreasingSubarrays(self, nums, k):
        n = len(nums)

        def rising(start):
            return all(nums[i] < nums[i + 1] for i in range(start, start + k - 1))

        return any(rising(a) and rising(a + k) for a in range(n - 2 * k + 1))


# V0-1
# IDEA : MAXIMAL RISING RUNS — ONE PASS, NO PER-ANCHOR RESCAN
#
#   cut nums into maximal strictly increasing runs.  two adjacent blocks of
#   length k exist iff some run is >= 2k (both blocks sit inside it) or two
#   neighbouring runs are both >= k (the blocks straddle their boundary).
#
# time = O(n)
# space = O(1)
class Solution(object):
    def hasIncreasingSubarrays(self, nums, k):
        prev_run = 0
        run = 1
        for i in range(1, len(nums)):
            if nums[i] > nums[i - 1]:
                run += 1
            else:
                if run >= 2 * k or min(prev_run, run) >= k:
                    return True
                prev_run = run
                run = 1
        return run >= 2 * k or min(prev_run, run) >= k


# V0-2
# IDEA : DP ON THE MEETING POINT OF THE TWO BLOCKS
#
#   end[i]   = length of the rising run that ENDS at i
#   start[i] = length of the rising run that STARTS at i
#
#   the two blocks touch at some index b, so the answer is True iff
#   min(end[b - 1], start[b]) >= k for at least one b — one O(1) test per
#   split point, and it needs no knowledge of where the runs begin.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def hasIncreasingSubarrays(self, nums, k):
        n = len(nums)
        end = [1] * n
        start = [1] * n
        for i in range(1, n):
            if nums[i] > nums[i - 1]:
                end[i] = end[i - 1] + 1
        for i in range(n - 2, -1, -1):
            if nums[i] < nums[i + 1]:
                start[i] = start[i + 1] + 1
        return any(min(end[b - 1], start[b]) >= k for b in range(1, n))
