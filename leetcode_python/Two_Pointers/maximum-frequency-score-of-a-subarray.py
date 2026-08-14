"""

2524. Maximum Frequency Score of a Subarray
Hard

You are given an integer array nums and a positive integer k.

The frequency score of an array is the sum of the distinct values in the array
raised to the power of their frequencies, taking the sum modulo 10^9 + 7.

For example, the frequency score of the array [5,4,5,7,4,4] is
(4^3 + 5^2 + 7^1) modulo (10^9 + 7) = 96.

Return the maximum frequency score of a subarray of size k in nums.
You should maximize the value under the modulo and not the actual value.

A subarray is a contiguous part of an array.


Example 1:

Input: nums = [1,1,1,2,1,2], k = 3
Output: 5
Explanation: The subarray [2,1,2] has a frequency score equal to 5.
It can be shown that it is the maximum frequency score we can have.

Example 2:

Input: nums = [1,1,1,1,1,1], k = 4
Output: 1
Explanation: All the subarrays of length 4 have a frequency score equal to 1.


Constraints:

1 <= k <= nums.length <= 10^5
1 <= nums[i] <= 10^6

"""

# V0
# IDEA : SLIDING WINDOW + COUNTER + INCREMENTAL MODULAR UPDATE
#
#   the score of a window is sum over distinct v of v^cnt[v] (mod 1e9+7).
#   recomputing that from scratch for every window is O(n * k); instead slide
#   a window of size k and only patch the TWO values whose counts change:
#   subtract the old term v^old, bump the count, add back v^new.
#
#   term(v, c) = pow(v, c, MOD) if c > 0 else 0
#
#   NOTE : the c == 0 case is the gotcha -- pow(v, 0) is 1, but a value that
#          is absent from the window must contribute 0, not 1. Guard it.
#
#   NOTE : "maximize the value under the modulo" -- we compare the REDUCED
#          residues, so keep `cur` normalised into [0, MOD) after every patch
#          (Python's % already returns a non-negative result).
#
#   NOTE : when the outgoing and incoming values are equal the window's
#          multiset is unchanged, so we can skip the work entirely.
#
# time = O(n * log(max(nums))), space = O(n)   (log factor = modular pow)
from collections import Counter
class Solution(object):
    def maxFrequencyScore(self, nums, k):
        MOD = 10 ** 9 + 7

        def term(v, c):
            # a value absent from the window contributes 0 (NOT v^0 == 1)
            return pow(v, c, MOD) if c > 0 else 0

        cnt = Counter(nums[:k])
        cur = 0
        for v, c in cnt.items():
            cur = (cur + term(v, c)) % MOD
        res = cur

        for i in range(k, len(nums)):
            out, inn = nums[i - k], nums[i]
            if out != inn:
                # drop the leaving element
                cur = (cur - term(out, cnt[out])) % MOD
                cnt[out] -= 1
                cur = (cur + term(out, cnt[out])) % MOD
                # add the entering element
                cur = (cur - term(inn, cnt[inn])) % MOD
                cnt[inn] += 1
                cur = (cur + term(inn, cnt[inn])) % MOD
            if cur > res:
                res = cur
        return res
