"""

1191. K-Concatenation Maximum Sum
Medium

Given an integer array arr and an integer k, modify the array by repeating it
k times.

For example, if arr = [1, 2] and k = 3 then the modified array will be
[1, 2, 1, 2, 1, 2].

Return the maximum sub-array sum in the modified array. Note that the length of
the sub-array can be 0 and its sum in that case is 0.

As the answer can be very large, return the answer modulo 10^9 + 7.


Example 1:

Input: arr = [1,2], k = 3
Output: 9

Example 2:

Input: arr = [1,-2,1], k = 5
Output: 2

Example 3:

Input: arr = [-1,-2], k = 7
Output: 0


Constraints:

1 <= arr.length <= 10^5
1 <= k <= 10^5
-10^4 <= arr[i] <= 10^4

"""

# V0
# IDEA: PREFIX SUM + CASE DISCUSSION
"""

 Let:
    - s      = total sum of arr
    - mx_pre = max prefix sum  (best "head" of the array)
    - mi_pre = min prefix sum
    - mx_suf = s - mi_pre      (best "tail" of the array)
    - mx_sub = max subarray sum within ONE copy (Kadane via prefix sums)

 Cases:
    - k == 1                -> mx_sub
    - k >= 2                -> also try mx_suf + mx_pre  (subarray spanning 2 copies)
    - k >= 2 and s > 0      -> also try (k-2)*s + mx_suf + mx_pre
                               (take all the middle copies, since each adds s > 0)

 -> empty subarray is allowed, so every quantity starts at 0
"""
# time = O(n)
# space = O(1)
class Solution(object):
    def kConcatenationMaxSum(self, arr, k):
        MOD = 10 ** 9 + 7

        s = 0
        mx_pre = 0
        mi_pre = 0
        mx_sub = 0

        for x in arr:
            s += x
            mx_pre = max(mx_pre, s)
            mi_pre = min(mi_pre, s)
            mx_sub = max(mx_sub, s - mi_pre)

        res = mx_sub

        if k >= 2:
            mx_suf = s - mi_pre
            res = max(res, mx_pre + mx_suf)
            if s > 0:
                res = max(res, (k - 2) * s + mx_pre + mx_suf)

        """
        NOTE !!!

        -> take modulo ONLY at the very end,
           comparing already-modded values would break the max()
        """
        return res % MOD
