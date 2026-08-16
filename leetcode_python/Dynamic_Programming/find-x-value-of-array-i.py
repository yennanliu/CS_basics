"""

3524. Find X Value of Array I
Medium

You are given an array of positive integers nums, and a positive integer k.

You are allowed to perform an operation once on nums, where in each operation
you can remove any non-overlapping prefix and suffix from nums such that nums
remains non-empty.

You need to find the x-value of nums, which is the number of ways to perform
this operation so that the product of the remaining elements leaves a remainder
of x when divided by k.

Return an array result of size k where result[x] is the x-value of nums for 0 <=
x <= k - 1.

A prefix of an array is a subarray that starts from the beginning of the array
and extends to any point within it.

A suffix of an array is a subarray that starts at any point within the array and
extends to the end of the array.

Note that the prefix and suffix to be chosen for the operation can be empty.

Example 1:

Input: nums = [1,2,3,4,5], k = 3

Output: [9,2,4]

Explanation:

For x = 0, the possible operations include all possible ways to remove
non-overlapping prefix/suffix that do not remove nums[2] == 3.

For x = 1, the possible operations are:

Remove the empty prefix and the suffix [2, 3, 4, 5]. nums becomes [1].

Remove the prefix [1, 2, 3] and the suffix [5]. nums becomes [4].

For x = 2, the possible operations are:

Remove the empty prefix and the suffix [3, 4, 5]. nums becomes [1, 2].

Remove the prefix [1] and the suffix [3, 4, 5]. nums becomes [2].

Remove the prefix [1, 2, 3] and the empty suffix. nums becomes [4, 5].

Remove the prefix [1, 2, 3, 4] and the empty suffix. nums becomes [5].

Example 2:

Input: nums = [1,2,4,8,16,32], k = 4

Output: [18,1,2,0]

Explanation:

For x = 0, the only operations that do not result in x = 0 are:

Remove the empty prefix and the suffix [4, 8, 16, 32]. nums becomes [1, 2].

Remove the empty prefix and the suffix [2, 4, 8, 16, 32]. nums becomes [1].

Remove the prefix [1] and the suffix [4, 8, 16, 32]. nums becomes [2].

For x = 1, the only possible operation is:

Remove the empty prefix and the suffix [2, 4, 8, 16, 32]. nums becomes [1].

For x = 2, the possible operations are:

Remove the empty prefix and the suffix [4, 8, 16, 32]. nums becomes [1, 2].

Remove the prefix [1] and the suffix [4, 8, 16, 32]. nums becomes [2].

For x = 3, there is no possible way to perform the operation.

Example 3:

Input: nums = [1,1,2,1,1], k = 2

Output: [9,6]

Constraints:

1 <= nums[i] <= 10^9

1 <= nums.length <= 10^5

1 <= k <= 5

"""

# V0
# IDEA : ROLLING RESIDUE HISTOGRAM OF ALL SUBARRAYS ENDING AT THE CURRENT INDEX
#
#   removing a prefix and a suffix and keeping something non-empty is exactly
#   "pick a non-empty contiguous subarray", and each subarray is produced by
#   exactly one (prefix, suffix) pair -- so the answer counts subarrays by the
#   residue of their product.
#
#   walking the right end j, every subarray ending at j is either the single
#   element nums[j] or a subarray ending at j-1 extended by nums[j].  since
#   (p * nums[j]) mod k depends only on p mod k, a histogram of size k over the
#   residues at j-1 is enough to build the one at j: shift every bucket r into
#   bucket r*nums[j] mod k, then add one for the singleton.
#
#   k is at most 5, so each step is a handful of operations and the running
#   totals just accumulate into the answer.
#
# time = O(n * k), space = O(k)
class Solution(object):
    def resultArray(self, nums, k):
        res = [0] * k
        cur = [0] * k                       # subarrays ending at the current j
        for v in nums:
            v %= k
            nxt = [0] * k
            for r in range(k):
                if cur[r]:
                    nxt[r * v % k] += cur[r]
            nxt[v] += 1                     # the single-element subarray
            cur = nxt
            for r in range(k):
                res[r] += cur[r]
        return res
