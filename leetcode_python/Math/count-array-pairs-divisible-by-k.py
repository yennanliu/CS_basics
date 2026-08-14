"""

2183. Count Array Pairs Divisible by K
Hard

Given a 0-indexed integer array nums of length n and an integer k, return the number of pairs (i, j) such that:

0 <= i < j <= n - 1 and
nums[i] * nums[j] is divisible by k.

Example 1:

Input: nums = [1,2,3,4,5], k = 2
Output: 7
Explanation:
The 7 pairs of indices whose corresponding products are divisible by 2 are
(0, 1), (0, 3), (1, 2), (1, 3), (1, 4), (2, 3), and (3, 4).
Their products are 2, 4, 6, 8, 10, 12, and 20 respectively.
Other pairs such as (0, 2) and (2, 4) have products 3 and 15 respectively, which are not divisible by 2.

Example 2:

Input: nums = [1,2,3,4], k = 5
Output: 0
Explanation: There does not exist any pair of indices whose corresponding product is divisible by 5.

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i], k <= 10^5

"""

# V0
# IDEA : GCD BUCKETS (only gcd(nums[i], k) matters for divisibility)
#
#   nums[i] * nums[j] % k == 0  <=>  g_i * g_j % k == 0, where
#   g = gcd(num, k) : the part of num that is coprime with k can never
#   help. every g is a DIVISOR of k, and k <= 10^5 has at most 128
#   divisors, so bucket the array by g and test every pair of buckets.
#   NOTE : same-bucket pairs are counted as c*(c-1)/2, cross-bucket as
#          c1*c2, to keep i < j exactly once.
#
# time = O(n log k + d(k)^2), space = O(d(k))
from collections import Counter
class Solution(object):
    def countPairs(self, nums, k):
        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        cnt = Counter()
        for v in nums:
            cnt[gcd(v, k)] += 1

        keys = sorted(cnt)
        res = 0
        for i, a in enumerate(keys):
            if (a * a) % k == 0:
                res += cnt[a] * (cnt[a] - 1) // 2
            for b in keys[i + 1:]:
                if (a * b) % k == 0:
                    res += cnt[a] * cnt[b]
        return res
