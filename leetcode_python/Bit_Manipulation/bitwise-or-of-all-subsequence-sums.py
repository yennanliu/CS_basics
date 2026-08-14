"""

2505. Bitwise OR of All Subsequence Sums
Medium

Given an integer array nums, return the value of the bitwise OR of the sum of all possible subsequences in the array.

A subsequence is a sequence that can be derived from another sequence by removing zero or more elements without changing the order of the remaining elements.


Example 1:

Input: nums = [2,1,0,3]
Output: 7
Explanation: All possible subsequence sums that we can have are: 0, 1, 2, 3, 4, 5, 6.
And we have 0 OR 1 OR 2 OR 3 OR 4 OR 5 OR 6 = 7, so we return 7.

Example 2:

Input: nums = [0,0,0]
Output: 0
Explanation: 0 is the only possible subsequence sum we can have, so we return 0.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9

"""

# V0
# IDEA : BIT COUNTING + CARRY PROPAGATION
#
#   claim : bit i appears in SOME subsequence sum
#           <=> T(i) >= 2^i, where T(i) = sum over all nums of (v mod 2^(i+1)),
#           i.e. the total weight living in bits 0..i.
#
#   (=>) any subset's low part is at most T(i); if T(i) < 2^i no subset can
#        reach bit i at all.
#   (<=) add elements one by one and watch the running low part cross 2^i.
#        either it lands inside [2^i, 2^(i+1)) -> that prefix already has
#        bit i, or the single element that made it jump is itself > 2^i and
#        < 2^(i+1) -> that one element alone has bit i.
#
#   T(i) >= 2^i is computed cheaply with a carry sweep : cnt[i] = number of
#   nums with bit i set, then cnt[i+1] += cnt[i] // 2 makes cnt[i] equal to
#   floor(T(i) / 2^i). so the test is simply "cnt[i] != 0".
#
#   NOTE : the sweep must run WELL ABOVE the input width -- carries from
#          10^5 values keep climbing, so go to ~64 bits (sum < 10^14 < 2^47).
#   NOTE : the empty subsequence contributes 0 and never affects an OR.
#
# time = O(n * 32 + 64), space = O(64)
class Solution(object):
    def subsequenceSumOr(self, nums):
        B = 64
        cnt = [0] * (B + 1)
        for v in nums:
            i = 0
            while v:
                if v & 1:
                    cnt[i] += 1
                v >>= 1
                i += 1

        res = 0
        for i in range(B):
            if cnt[i]:
                res |= 1 << i
            cnt[i + 1] += cnt[i] // 2
        return res
