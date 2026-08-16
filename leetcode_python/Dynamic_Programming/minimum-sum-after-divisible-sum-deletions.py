"""

3654. Minimum Sum After Divisible Sum Deletions
Medium

You are given an integer array nums and an integer k.

You may repeatedly delete any contiguous subarray of nums whose sum is divisible by k, and after each deletion the remaining elements close the gap.

Return the minimum possible sum of nums after performing any number of such deletions.


Example 1:

Input: nums = [1,1,1], k = 2
Output: 1
Explanation:
Delete the subarray [1, 1], whose sum is 2 and divisible by k = 2. The array becomes [1], whose sum is 1, and no further deletion is possible.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
1 <= k <= 10^5

"""

# V0
# IDEA : PREFIX SUMS MOD k + "BEST dp SEEN AT THIS RESIDUE"
#
#   deletions can be nested and can re-glue distant parts, but everything a
#   sequence of deletions can achieve is captured by one linear scan.
#
#   let dp[i] be the minimum achievable sum of the first i elements. either
#   nums[i-1] survives, giving dp[i-1] + nums[i-1], or nums[i-1] is inside a
#   deleted block ending at i-1 which started at some j; that block has sum
#   prefix[i] - prefix[j], so it is deletable exactly when
#       prefix[i] % k == prefix[j] % k
#   and the cost is dp[j].
#
#   grouping by residue, keeping the smallest dp[j] seen per residue makes
#   each step O(1). (a block glued across an earlier hole is already covered,
#   because the hole was itself divisible by k and so does not disturb the
#   residue of the outer block.)
#
# time = O(n), space = O(k)
class Solution(object):
    def minArraySum(self, nums, k):
        INF = float('inf')
        best = [INF] * k
        pref = 0
        dp = 0
        best[0] = 0
        for x in nums:
            pref = (pref + x) % k
            dp = dp + x
            if best[pref] < dp:
                dp = best[pref]
            if dp < best[pref]:
                best[pref] = dp
        return dp
