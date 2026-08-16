"""

3520. Minimum Threshold for Inversion Pairs Count
Medium

You are given an array of integers nums and an integer k.

An inversion pair with a threshold x is defined as a pair of indices (i, j) such
that:

i < j

nums[i] > nums[j]

The difference between the two numbers is at most x (i.e. nums[i] - nums[j] <=
x).

Your task is to determine the minimum integer min_threshold such that there are
at least k inversion pairs with threshold min_threshold.

If no such integer exists, return -1.

Example 1:

Input: nums = [1,2,3,4,3,2,1], k = 7

Output: 2

Explanation:

For threshold x = 2, the pairs are:

(3, 4) where nums[3] == 4 and nums[4] == 3.

(2, 5) where nums[2] == 3 and nums[5] == 2.

(3, 5) where nums[3] == 4 and nums[5] == 2.

(4, 5) where nums[4] == 3 and nums[5] == 2.

(1, 6) where nums[1] == 2 and nums[6] == 1.

(2, 6) where nums[2] == 3 and nums[6] == 1.

(4, 6) where nums[4] == 3 and nums[6] == 1.

(5, 6) where nums[5] == 2 and nums[6] == 1.

There are less than k inversion pairs if we choose any integer less than 2 as
threshold.

Example 2:

Input: nums = [10,9,9,9,1], k = 4

Output: 8

Explanation:

For threshold x = 8, the pairs are:

(0, 1) where nums[0] == 10 and nums[1] == 9.

(0, 2) where nums[0] == 10 and nums[2] == 9.

(0, 3) where nums[0] == 10 and nums[3] == 9.

(1, 4) where nums[1] == 9 and nums[4] == 1.

(2, 4) where nums[2] == 9 and nums[4] == 1.

(3, 4) where nums[3] == 9 and nums[4] == 1.

There are less than k inversion pairs if we choose any integer less than 8 as
threshold.

Constraints:

1 <= nums.length <= 10^4

1 <= nums[i] <= 10^9

1 <= k <= 10^9

"""

# V0
# IDEA : BINARY SEARCH ON THE THRESHOLD + FENWICK COUNT OF QUALIFYING PAIRS
#
#   widening the threshold can only admit more pairs, never remove one, so
#   count(x) is monotone non-decreasing in x.  that makes the answer the
#   classic "smallest x with count(x) >= k" and lets us binary search x over
#   [1, max(nums) - min(nums)] -- and if even the widest threshold falls short
#   of k, no threshold works and the answer is -1.
#
#   for a fixed x, count(x) is a one-pass sweep: walking j from left to right,
#   the partners of j are the already-seen values lying in (nums[j],
#   nums[j] + x].  a Fenwick tree over the compressed values answers that as
#   the difference of two prefix counts, so one evaluation is O(n log n).
#
#   the values are compressed once, outside the search; inside, only the
#   Fenwick array is reset, which keeps each of the ~30 probes cheap.
#
# time = O(n * log n * log(maxV)), space = O(n)
import bisect


class Solution(object):
    def minThreshold(self, nums, k):
        n = len(nums)
        srt = sorted(set(nums))
        m = len(srt)
        rank = [bisect.bisect_left(srt, v) + 1 for v in nums]   # 1-based

        def count(x):
            tree = [0] * (m + 1)
            total = 0
            for j in range(n):
                v = nums[j]
                # values strictly above v and at most v + x, already seen
                hi = bisect.bisect_right(srt, v + x)
                lo = rank[j]
                i = hi
                while i > 0:
                    total += tree[i]
                    i -= i & -i
                i = lo
                while i > 0:
                    total -= tree[i]
                    i -= i & -i
                if total >= k:
                    return k
                i = rank[j]
                while i <= m:
                    tree[i] += 1
                    i += i & -i
            return total

        hi = srt[-1] - srt[0]
        if hi <= 0 or count(hi) < k:
            return -1
        lo = 1
        while lo < hi:
            mid = (lo + hi) // 2
            if count(mid) >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
