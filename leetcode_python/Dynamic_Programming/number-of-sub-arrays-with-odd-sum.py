"""

1524. Number of Sub-arrays With Odd Sum
Medium

Given an array of integers arr, return the number of subarrays with an odd sum.

Since the answer can be very large, return it modulo 10^9 + 7.


Example 1:

Input: arr = [1,3,5]
Output: 4
Explanation: All subarrays are [[1],[1,3],[1,3,5],[3],[3,5],[5]]
All sub-arrays sum are [1,4,9,3,8,5].
Odd sums are [1,9,3,5] so the answer is 4.

Example 2:

Input: arr = [2,4,6]
Output: 0
Explanation: All subarrays are [[2],[2,4],[2,4,6],[4],[4,6],[6]]
All sub-arrays sum are [2,6,12,4,10,6].
All sub-arrays have even sum and the answer is 0.

Example 3:

Input: arr = [1,2,3,4,5,6,7]
Output: 16


Constraints:

1 <= arr.length <= 10^5
1 <= arr[i] <= 100

"""

# V0
# IDEA : PREFIX-SUM PARITY COUNTING
#
#   sum(l..r) = P[r] - P[l-1], and that is ODD exactly when P[r] and P[l-1]
#   have DIFFERENT parity.
#
#   so scan the prefix sums keeping cnt[0], cnt[1] = how many prefixes seen
#   so far are even / odd. at each r, the subarrays ending at r with an odd
#   sum number cnt[opposite parity of P[r]].
#
#   NOTE : seed cnt[0] = 1 for the empty prefix P[-1] = 0, otherwise every
#          subarray starting at index 0 is missed.
#
# time = O(n), space = O(1)
class Solution(object):
    def numOfSubarrays(self, arr):
        MOD = 10 ** 9 + 7
        cnt = [1, 0]
        res = 0
        s = 0
        for x in arr:
            s += x
            p = s & 1
            res = (res + cnt[p ^ 1]) % MOD
            cnt[p] += 1
        return res
