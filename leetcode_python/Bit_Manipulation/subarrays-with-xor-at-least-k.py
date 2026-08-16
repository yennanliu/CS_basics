"""

3632. Subarrays with XOR at Least K
Hard

Given an array of positive integers nums of length n and a non-negative
integer k.

Return the number of contiguous subarrays whose bitwise XOR of all elements
is greater than or equal to k.


Example 1:

Input: nums = [3,1,2,3], k = 2
Output: 6
Explanation:
The valid subarrays with XOR >= 2 are [3] at index 0, [3, 1] at indices
0 - 1, [3, 1, 2, 3] at indices 0 - 3, [1, 2] at indices 1 - 2, [2] at index
2, and [3] at index 3; there are 6 in total.

Example 2:

Input: nums = [0,0,0], k = 0
Output: 6
Explanation:
Every contiguous subarray yields XOR = 0, which meets k = 0. There are 6
such subarrays in total.


Constraints:

1 <= nums.length <= 10^5
0 <= nums[i] <= 10^9
0 <= k <= 10^9

"""

# V0
# IDEA : COUNT THE COMPLEMENT, ONE BIT OF K AT A TIME
#
#   with prefix xors p[0..n] a subarray's xor is p[i] ^ p[j], so the task is
#   counting pairs of prefixes whose xor is >= k. the usual answer is a
#   binary trie; the sharper observation is that "z < k" has a UNIQUE
#   witness bit -- the highest bit where z and k differ, which must be a bit
#   b with k_b = 1 and z_b = 0, everything above agreeing.
#
#   that turns one messy inequality into a disjoint union over the set bits
#   of k. for a given such b, "z agrees with k above b and has 0 at b" is
#   exactly z >> b == (k >> b) ^ 1, i.e.
#
#       (p_i >> b) ^ (p_j >> b) == (k >> b) ^ 1
#
#   an equality! so bucket the prefixes by their top bits p >> b and add
#   cnt[u] * cnt[u ^ c] over the buckets -- no trie, no descent, just one
#   frequency table per set bit of k, and only for the set bits.
#
#   counting ordered pairs keeps the arithmetic clean (buckets pair up
#   symmetrically); at the end subtract the diagonal i == j, which
#   contributes xor 0 and so only counts when k == 0, and halve.
#
# time = O(n log C), space = O(n)
class Solution(object):
    def countXorSubarrays(self, nums, k):
        from collections import Counter

        n = len(nums)
        pref = [0] * (n + 1)
        cur = 0
        for i, x in enumerate(nums):
            cur ^= x
            pref[i + 1] = cur

        below = 0                          # ordered pairs with xor < k
        for b in range(k.bit_length()):
            if not (k >> b) & 1:
                continue
            c = (k >> b) ^ 1
            cnt = Counter([p >> b for p in pref])
            get = cnt.get
            for u, m in cnt.items():
                other = get(u ^ c)
                if other:
                    below += m * other

        m = n + 1
        atleast = m * m - below            # ordered pairs with xor >= k
        if k == 0:
            atleast -= m                   # drop the i == j diagonal
        return atleast // 2
