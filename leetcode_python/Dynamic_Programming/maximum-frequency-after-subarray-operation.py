"""

3434. Maximum Frequency After Subarray Operation
Medium

You are given an array nums of length n. You are also given an integer k.

You perform the following operation on nums once:

Select a subarray nums[i..j] where 0 <= i <= j <= n - 1.
Select an integer x and add x to all the elements in nums[i..j].

Find the maximum frequency of the value k after the operation.

Example 1:

Input: nums = [1,2,3,4,5,6], k = 1

Output: 2

Explanation:

After adding -5 to nums[2..5], 1 has a frequency of 2 in [1, 2, -2, -1, 0, 1].

Example 2:

Input: nums = [10,2,3,4,5,5,4,3,2,2], k = 10

Output: 4

Explanation:

After adding 8 to nums[1..9], 10 has a frequency of 4 in [10, 10, 11, 12, 13,
13, 12, 11, 10, 10].

Constraints:

1 <= n == nums.length <= 10^5
1 <= nums[i] <= 50
1 <= k <= 50

"""

# V0
# IDEA : ONE KADANE PER CANDIDATE VALUE, ALL 50 RUN AT ONCE VIA AN OFFSET
#
#   whatever x we add, inside the chosen subarray exactly one original value v
#   turns into k, and the elements that were already k stop being k.  so fix v
#   and score the array with +1 where nums[i] == v, -1 where nums[i] == k, and 0
#   elsewhere; the best subarray of that scoring is the net gain, and the answer
#   is count(k) plus the best gain over all v (the empty subarray, gain 0, is
#   always allowed, which covers "do nothing").
#
#   running 50 separate Kadanes is 50n steps.  the trick to make it linear is to
#   notice how a position updates the 50 running sums:
#
#     nums[i] == k  -> *every* candidate loses 1,
#     nums[i] == u  -> only candidate u gains 1, the rest are untouched.
#
#   a uniform -1 would be a plain offset if it were not for Kadane's clamp at 0.
#   so store a raw value per candidate and a shared offset, define the true
#   running sum as max(raw[u] - off, 0), and re-normalise a candidate only when
#   it is actually incremented: raw[u] = max(raw[u], off) + 1.  each position
#   then costs O(1), and the best is only ever refreshed on an increment because
#   a -1 step can never set a record.
#
"""

DP def
    whatever x is added, inside the chosen subarray exactly ONE original value
    v turns into k, and the elements that were already k STOP being k. so fix
    v and score the array

        +1 where nums[i] == v,  -1 where nums[i] == k,  0 elsewhere

    then the best subarray of that scoring is the net gain -> one KADANE per
    candidate v

    dp (per candidate u): the running Kadane sum, clamped at 0

DP eq

     Kadane:  run = max(run + score[i], 0);  best = max(best, run)

     all 50 candidates at once, via a shared OFFSET:

        nums[i] == k  ->  EVERY candidate loses 1   ->  off += 1
        nums[i] == u  ->  only candidate u gains 1  ->  raw[u] = max(raw[u], off) + 1

        true running sum of u  ==  max(raw[u] - off, 0)


    -> e.g. re-normalising a candidate only when it is INCREMENTED is safe -
              a -1 step can never set a record

     the empty subarray (gain 0) is always allowed -> covers "do nothing"
     ans = count(k) + max gain over all v

"""
# time = O(n), space = O(max value)
class Solution(object):
    def maxFrequency(self, nums, k):
        M = 51
        raw = [0] * M
        best = [0] * M
        off = 0
        base = 0
        for u in nums:
            if u == k:
                base += 1
                off += 1
            else:
                r = raw[u]
                if off > r:
                    r = off
                r += 1
                raw[u] = r
                if r - off > best[u]:
                    best[u] = r - off
        return base + max(best)
