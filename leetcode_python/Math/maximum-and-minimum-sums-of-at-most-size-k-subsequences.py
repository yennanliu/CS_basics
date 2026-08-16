"""

3428. Maximum and Minimum Sums of at Most Size K Subsequences
Medium

You are given an integer array nums and a positive integer k. Return the sum of
the maximum and minimum elements of all subsequences of nums with at most k
elements.

Since the answer may be very large, return it modulo 10^9 + 7.

Example 1:

Input: nums = [1,2,3], k = 2

Output: 24

Explanation:

The subsequences of nums with at most 2 elements are:

Subsequence
Minimum
Maximum
Sum

[1]
1
1
2

[2]
2
2
4

[3]
3
3
6

[1, 2]
1
2
3

[1, 3]
1
3
4

[2, 3]
2
3
5

Final Total

24

The output would be 24.

Example 2:

Input: nums = [5,0,6], k = 1

Output: 22

Explanation:

For subsequences with exactly 1 element, the minimum and maximum values are the
element itself. Therefore, the total is 5 + 5 + 0 + 0 + 6 + 6 = 22.

Example 3:

Input: nums = [1,1,1], k = 2

Output: 12

Explanation:

The subsequences [1, 1] and [1] each appear 3 times. For all of them, the
minimum and maximum are both 1. Thus, the total is 12.

Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
1 <= k <= min(70, nums.length)

"""

# V0
# IDEA : SORT, THEN COUNT HOW OFTEN EACH ELEMENT IS THE MAX / THE MIN
#
#   a subsequence is picked by index, so sorting the values and then arguing by
#   *position in the sorted order* both fixes the duplicate problem and makes
#   the counting trivial: after sorting, the element at index i is the maximum
#   of a chosen subsequence exactly when every other chosen element comes from
#   the i positions before it.  ties are handled automatically because two equal
#   values still occupy different sorted positions, so each subsequence has one
#   designated maximum.
#
#   so nums[i] is the maximum of  f(i) = sum_{j<k} C(i, j)  subsequences, where
#   k caps the size at k elements (j is how many companions we take).  by the
#   mirror argument it is the minimum of f(n - 1 - i) of them.
#
#   f is built in one pass with Pascal's identity summed over j:
#     f(i) = 2*f(i-1) - C(i-1, k-1)
#   once i >= k, and f(i) = 2^i while the cap is not yet binding.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def minMaxSums(self, nums, k):
        MOD = 10 ** 9 + 7
        n = len(nums)
        nums.sort()

        fact = [1] * (n + 1)
        for i in range(1, n + 1):
            fact[i] = fact[i - 1] * i % MOD
        inv = [1] * (n + 1)
        inv[n] = pow(fact[n], MOD - 2, MOD)
        for i in range(n, 0, -1):
            inv[i - 1] = inv[i] * i % MOD

        def comb(a, b):
            if b < 0 or b > a:
                return 0
            return fact[a] * inv[b] % MOD * inv[a - b] % MOD

        f = [0] * n
        for i in range(n):
            if i == 0:
                f[i] = 1
            elif i <= k - 1:
                f[i] = f[i - 1] * 2 % MOD
            else:
                f[i] = (f[i - 1] * 2 - comb(i - 1, k - 1)) % MOD

        ans = 0
        for i in range(n):
            ans = (ans + nums[i] * (f[i] + f[n - 1 - i])) % MOD
        return ans % MOD
