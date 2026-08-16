"""

3422. Minimum Operations to Make Subarray Elements Equal
Medium

You are given an integer array nums and an integer k. You can perform the
following operation any number of times:

Increase or decrease any element of nums by 1.

Return the minimum number of operations required to ensure that at least one
subarray of size k in nums has all elements equal.

Example 1:

Input: nums = [4,-3,2,1,-4,6], k = 3

Output: 5

Explanation:

Use 4 operations to add 4 to nums[1]. The resulting array is [4, 1, 2, 1, -4,
6].
Use 1 operation to subtract 1 from nums[2]. The resulting array is [4, 1, 1, 1,
-4, 6].
The array now contains a subarray [1, 1, 1] of size k = 3 with all elements
equal. Hence, the answer is 5.

Example 2:

Input: nums = [-2,-2,3,1,4], k = 2

Output: 0

Explanation:

The subarray [-2, -2] of size k = 2 already contains all equal elements, so no
operations are needed. Hence, the answer is 0.

Constraints:

2 <= nums.length <= 10^5
-10^6 <= nums[i] <= 10^6
2 <= k <= nums.length

"""

# V0
# IDEA : SLIDING WINDOW MEDIAN VIA TWO FENWICK TREES
#
#   for one fixed window the cost sum|a_i - t| is minimised at a median of the
#   window — the derivative is (#below t) - (#above t), which changes sign
#   exactly there.  so the problem is "for every window of size k, evaluate the
#   cost at its median", and the answer is the smallest of those.
#
#   to slide, keep the window in two Fenwick trees over the compressed values:
#   one counting elements, one summing them.  then
#     - the j-th smallest (j = (k+1)//2) is found by descending the count tree
#       with binary lifting, in O(log n) rather than O(log^2 n);
#     - the sum of the j smallest comes from the prefix of the sum tree, plus
#       however many copies of the median value are needed to reach rank j
#       (duplicates make the boundary land in the middle of a value bucket).
#
#   with the split known, cost = (m*j - sumLow) + (sumHigh - m*(k-j)), i.e. how
#   far each side has to travel to reach m.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minOperations(self, nums, k):
        vals = sorted(set(nums))
        idx = {v: i + 1 for i, v in enumerate(vals)}
        m = len(vals)
        LOG = 1
        while (1 << LOG) <= m:
            LOG += 1
        LOG -= 1
        cnt = [0] * (m + 1)
        tot = [0] * (m + 1)

        def upd(i, dc, dv):
            while i <= m:
                cnt[i] += dc
                tot[i] += dv
                i += i & (-i)

        def kth(j):
            # smallest index whose prefix count >= j; also returns prefix
            # count / sum strictly before that index
            pos = 0
            c = 0
            s = 0
            step = 1 << LOG
            while step:
                nxt = pos + step
                if nxt <= m and c + cnt[nxt] < j:
                    pos = nxt
                    c += cnt[nxt]
                    s += tot[nxt]
                step >>= 1
            return pos + 1, c, s

        n = len(nums)
        j = (k + 1) // 2
        windowSum = 0
        ans = float('inf')
        for i in range(n):
            upd(idx[nums[i]], 1, nums[i])
            windowSum += nums[i]
            if i >= k:
                out = nums[i - k]
                upd(idx[out], -1, -out)
                windowSum -= out
            if i >= k - 1:
                p, c, s = kth(j)
                med = vals[p - 1]
                sumLow = s + (j - c) * med
                cost = (med * j - sumLow) + (windowSum - sumLow - med * (k - j))
                if cost < ans:
                    ans = cost
        return ans
