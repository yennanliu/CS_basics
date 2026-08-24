"""

2031. Count Subarrays With More Ones Than Zeros
Medium
(premium / locked problem)

You are given a binary array nums containing only the integers 0 and 1. Return the number of subarrays in nums that have more 1's than 0's. Since the answer may be very large, return it modulo 10^9 + 7.

A subarray is a contiguous sequence of elements within an array.


Example 1:

Input: nums = [0,1,1,0,1]
Output: 9
Explanation:
The subarrays of size 1 that have more ones than zeros are: [1], [1], [1]
The subarrays of size 2 that have more ones than zeros are: [1,1]
The subarrays of size 3 that have more ones than zeros are: [0,1,1], [1,1,0], [1,0,1]
The subarrays of size 4 that have more ones than zeros are: [1,1,0,1]
The subarrays of size 5 that have more ones than zeros are: [0,1,1,0,1]

Example 2:

Input: nums = [0]
Output: 0
Explanation:
No subarrays have more ones than zeros.

Example 3:

Input: nums = [1]
Output: 1
Explanation:
The subarrays of size 1 that have more ones than zeros are: [1]


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 1

"""

# V0
# IDEA : MAP 0 -> -1, THEN COUNT PAIRS WITH prefix[j] > prefix[i]  (i < j)
#
#   with 0 rewritten as -1, "more ones than zeros" on nums[i..j-1] becomes
#   prefix[j] - prefix[i] > 0.
#
#   the prefix sum moves by exactly +-1 each step, which makes an O(n) DP
#   possible without any BIT :
#
#       cnt[v] = how many prefixes seen so far equal v
#       good   = how many prefixes seen so far are STRICTLY BELOW the
#                current prefix value
#
#   when the prefix goes  p -> p+1 : every prefix equal to p joins the
#   "below" set          -> good += cnt[p]
#   when it goes p -> p-1 : every prefix equal to p-1 leaves it
#                        -> good -= cnt[p-1]
#   then the number of valid subarrays ending here is exactly `good`.
#
"""

DP def
    map 0 -> -1; then "more ones than zeros" on nums[i..j-1] becomes
    prefix[j] - prefix[i] > 0

    cnt[v]: how many prefixes seen so far equal v

    good  : how many prefixes seen so far are STRICTLY BELOW the current
            prefix value

DP eq

     the prefix moves by exactly +-1 each step:

        p -> p+1 : every prefix equal to p joins the "below" set
                       good += cnt[p]

        p -> p-1 : every prefix equal to p-1 leaves it
                       good -= cnt[p-1]

     res += good          # valid subarrays ENDING here


    -> e.g. the +-1 step is what makes this O(n) with no BIT / merge sort

     init: cnt[0] = 1 (the empty prefix)
     ans = res % (10^9 + 7)

"""
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def subarraysWithMoreZerosThanOnes(self, nums):
        MOD = 10 ** 9 + 7
        cnt = defaultdict(int)
        cnt[0] = 1          # the empty prefix
        prefix = 0
        good = 0            # prefixes strictly smaller than `prefix`
        res = 0
        for x in nums:
            if x == 1:
                good += cnt[prefix]
                prefix += 1
            else:
                prefix -= 1
                good -= cnt[prefix]
            res = (res + good) % MOD
            cnt[prefix] += 1
        return res
