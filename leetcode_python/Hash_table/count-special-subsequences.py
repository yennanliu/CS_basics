"""

3404. Count Special Subsequences
Medium

You are given an array nums consisting of positive integers.

A special subsequence is defined as a subsequence of length 4, represented by
indices (p, q, r, s), where p < q < r < s. This subsequence must satisfy the
following conditions:

nums[p] * nums[r] == nums[q] * nums[s]
There must be at least one element between each pair of indices. In other words,
q - p > 1, r - q > 1 and s - r > 1.

Return the number of different special subsequences in nums.

Example 1:

Input: nums = [1,2,3,4,3,6,1]

Output: 1

Explanation:

There is one special subsequence in nums.

(p, q, r, s) = (0, 2, 4, 6):

This corresponds to elements (1, 3, 3, 1).
nums[p] * nums[r] = nums[0] * nums[4] = 1 * 3 = 3
nums[q] * nums[s] = nums[2] * nums[6] = 3 * 1 = 3

Example 2:

Input: nums = [3,4,3,4,3,4,3,4]

Output: 3

Explanation:

There are three special subsequences in nums.

(p, q, r, s) = (0, 2, 4, 6):

This corresponds to elements (3, 3, 3, 3).
nums[p] * nums[r] = nums[0] * nums[4] = 3 * 3 = 9
nums[q] * nums[s] = nums[2] * nums[6] = 3 * 3 = 9

(p, q, r, s) = (1, 3, 5, 7):

This corresponds to elements (4, 4, 4, 4).
nums[p] * nums[r] = nums[1] * nums[5] = 4 * 4 = 16
nums[q] * nums[s] = nums[3] * nums[7] = 4 * 4 = 16

(p, q, r, s) = (0, 2, 5, 7):

This corresponds to elements (3, 3, 4, 4).
nums[p] * nums[r] = nums[0] * nums[5] = 3 * 4 = 12
nums[q] * nums[s] = nums[2] * nums[7] = 3 * 4 = 12

Constraints:

7 <= nums.length <= 1000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : REWRITE THE PRODUCT EQUATION AS A RATIO, THEN COUNT PAIRS BY RATIO
#
#   nums[p] * nums[r] == nums[q] * nums[s]  <=>  nums[p]/nums[q] == nums[s]/nums[r].
#   the left ratio only involves the two *early* indices and the right ratio
#   only the two *late* ones, so once we fix the split point the two halves stop
#   talking to each other and we can count them separately.
#
#   sweep r from left to right.  just before handling r, every pair (p, q) with
#   q == r - 2 becomes legal (q needs r - q > 1) and is folded into a counter
#   keyed by the reduced fraction nums[p]/nums[q].  each pair is inserted once
#   and never removed, so the counter always holds exactly the pairs allowed for
#   the current r.
#
#   then every s >= r + 2 contributes cnt[nums[s]/nums[r]] quadruples.  reducing
#   both fractions by their gcd is what makes 2/4 and 3/6 hash to the same key.
#
# time = O(n^2 log(max)), space = O(n^2) worst case for the fraction counter
from collections import defaultdict
from math import gcd


class Solution(object):
    def numberOfSubsequences(self, nums):
        n = len(nums)
        cnt = defaultdict(int)
        ans = 0
        for r in range(4, n - 2):
            q = r - 2
            vq = nums[q]
            for p in range(q - 1):
                g = gcd(nums[p], vq)
                cnt[(nums[p] // g, vq // g)] += 1
            vr = nums[r]
            for s in range(r + 2, n):
                g = gcd(nums[s], vr)
                key = (nums[s] // g, vr // g)
                if key in cnt:
                    ans += cnt[key]
        return ans
