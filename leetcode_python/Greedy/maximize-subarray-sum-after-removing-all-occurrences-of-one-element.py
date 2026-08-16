"""

3410. Maximize Subarray Sum After Removing All Occurrences of One Element
Hard

You are given an integer array nums.

You can do the following operation on the array at most once:

Choose any integer x such that nums remains non-empty on removing all
occurrences of x.
Remove all occurrences of x from the array.

Return the maximum subarray sum across all possible resulting arrays.

Example 1:

Input: nums = [-3,2,-2,-1,3,-2,3]

Output: 7

Explanation:

We can have the following arrays after at most one operation:

The original array is nums = [-3, 2, -2, -1, 3, -2, 3]. The maximum subarray sum
is 3 + (-2) + 3 = 4.
Deleting all occurences of x = -3 results in nums = [2, -2, -1, 3, -2, 3]. The
maximum subarray sum is 3 + (-2) + 3 = 4.
Deleting all occurences of x = -2 results in nums = [-3, 2, -1, 3, 3]. The
maximum subarray sum is 2 + (-1) + 3 + 3 = 7.
Deleting all occurences of x = -1 results in nums = [-3, 2, -2, 3, -2, 3]. The
maximum subarray sum is 3 + (-2) + 3 = 4.
Deleting all occurences of x = 3 results in nums = [-3, 2, -2, -1, -2]. The
maximum subarray sum is 2.

The output is max(4, 4, 7, 4, 2) = 7.

Example 2:

Input: nums = [1,2,3,4]

Output: 10

Explanation:

It is optimal to not perform any operations.

Constraints:

1 <= nums.length <= 10^5
-10^6 <= nums[i] <= 10^6

"""

# V0
# IDEA : ZERO OUT ONE VALUE AT A TIME IN A MAX-SUBARRAY SEGMENT TREE
#
#   deleting every copy of x and then taking a subarray is the same as leaving
#   the copies in place but valuing them at 0: a contiguous range of the deleted
#   array lifts to a contiguous range of the original, with the x's contributing
#   nothing.  so we never have to rebuild the array, only re-price it.
#
#   only x < 0 is ever worth deleting (removing a non-negative value can only
#   shrink a sum), and the plain Kadane answer covers "do nothing".
#
#   the cost trick: for a fixed x we set its positions to 0, read the global
#   best subarray, then set them back.  each index is touched exactly twice
#   across *all* values of x — once when its own value is being zeroed and once
#   when it is restored — so the total number of point updates is O(n), not
#   O(n * distinct).
#
#   the segment tree node carries (total, best prefix, best suffix, best
#   anywhere); merging is the classic maximum-subarray merge, where the only
#   genuinely new candidate is "suffix of the left child + prefix of the right".
#   padding leaves get -infinity so they can never be chosen.
#
#   two guards.  if x is the only value in the array, deleting it would empty
#   the array, which the problem forbids.  and the re-pricing trick lets the
#   tree "choose" a stretch made purely of zeroed cells, which really means the
#   empty subarray — harmless as soon as some element is >= 0 (then the plain
#   Kadane answer is already >= 0), so the all-negative case is answered up
#   front: nothing can beat the single largest element there.
#
# time = O(n log n), space = O(n)
from collections import defaultdict


class Solution(object):
    def maxSubarraySum(self, nums):
        n = len(nums)
        NEG = float('-inf')

        # plain Kadane == "perform no operation"
        best = NEG
        cur = 0
        for v in nums:
            cur = v if cur < 0 else cur + v
            if cur > best:
                best = cur

        if best < 0:
            return best

        pos = defaultdict(list)
        for i, v in enumerate(nums):
            if v < 0:
                pos[v].append(i)
        if not pos:
            return best

        size = 1
        while size < n:
            size <<= 1
        tot = [0] * (2 * size)
        pre = [NEG] * (2 * size)
        suf = [NEG] * (2 * size)
        bst = [NEG] * (2 * size)
        for i, v in enumerate(nums):
            j = size + i
            tot[j] = v
            pre[j] = suf[j] = bst[j] = v

        def pull(i):
            l, r = 2 * i, 2 * i + 1
            tot[i] = tot[l] + tot[r]
            a = tot[l] + pre[r]
            pre[i] = pre[l] if pre[l] > a else a
            a = tot[r] + suf[l]
            suf[i] = suf[r] if suf[r] > a else a
            a = suf[l] + pre[r]
            if bst[l] > a:
                a = bst[l]
            if bst[r] > a:
                a = bst[r]
            bst[i] = a

        for i in range(size - 1, 0, -1):
            pull(i)

        def assign(i, v):
            j = size + i
            tot[j] = v
            pre[j] = suf[j] = bst[j] = v
            j >>= 1
            while j:
                pull(j)
                j >>= 1

        for x, idxs in pos.items():
            if len(idxs) == n:
                continue
            for i in idxs:
                assign(i, 0)
            if bst[1] > best:
                best = bst[1]
            for i in idxs:
                assign(i, x)
        return best
