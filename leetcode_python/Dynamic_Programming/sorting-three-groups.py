"""

2826. Sorting Three Groups
Medium

You are given an integer array nums. Each element in nums is 1, 2 or 3. In each operation, you can remove an element from nums. Return the minimum number of operations to make nums non-decreasing.


Example 1:

Input: nums = [2,1,3,2,1]
Output: 3
Explanation:
One of the optimal solutions is to remove nums[0], nums[2] and nums[3].

Example 2:

Input: nums = [1,3,2,1,3,3]
Output: 2
Explanation:
One of the optimal solutions is to remove nums[1] and nums[2].

Example 3:

Input: nums = [2,2,2,2,3,3]
Output: 0
Explanation:
nums is already non-decreasing.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 3

Follow-up: Can you come up with an algorithm that runs in O(n) time complexity?

"""

# V0
# IDEA : DP (removals = n - longest non-decreasing subsequence)
#
#   Whatever we keep must itself be non-decreasing, and anything
#   non-decreasing can be kept, so the minimum number of removals is
#   n - LNDS(nums). Because values are only 1..3, the usual O(n log n)
#   LNDS collapses to an O(n) scan over 3 buckets.
#
#   Let f[j] = length of the longest non-decreasing subsequence built so far
#   whose LAST value is <= j + 1  (j = 0, 1, 2). f is non-decreasing by
#   construction, so max(f[:x]) is simply f[x - 1].
#
#   On seeing value x we may append it to any subsequence ending in <= x:
#       f[x - 1] = f[x - 1] + 1
#   and then restore the "<=" prefix-max invariant for the buckets above it.
#
#   NOTE : the answer is len(nums) - f[2], not f[2] itself.
#
#   NOTE : this is the O(n) / O(1) follow-up answer — no per-element loop
#          over all previous indices is needed.
#
"""

DP def
    whatever is KEPT must be non-decreasing, and anything non-decreasing can
    be kept -> minimum removals = n - LNDS(nums).
    values are only 1..3, so the usual O(n log n) LNDS collapses to 3 buckets.

    f[j]: length of the longest non-decreasing subsequence built so far whose

          LAST value is <= j + 1        (j = 0, 1, 2)

          -> f is non-decreasing by construction, so max(f[:x]) is just f[x-1]

DP eq

     on seeing value x, append it to any subsequence ending in <= x:

        f[x-1] += 1

     then restore the prefix-max invariant for the buckets above:

        f[j] = max(f[j], f[j-1])   for j = x .. 2


    -> e.g. NOTE !!! the answer is len(nums) - f[2], not f[2] itself

     this is the O(n) / O(1) follow-up - no per-element loop over all
     previous indices is needed

     ans = len(nums) - f[2]

"""
# time = O(n), space = O(1)
class Solution(object):
    def minimumOperations(self, nums):
        # f[j] : LNDS length whose last kept value is <= j + 1
        f = [0, 0, 0]
        for x in nums:
            f[x - 1] += 1
            for j in range(x, 3):
                if f[j] < f[j - 1]:
                    f[j] = f[j - 1]
        return len(nums) - f[2]
