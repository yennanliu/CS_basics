"""

3509. Maximum Product of Subsequences With an Alternating Sum Equal to K
Hard

You are given an integer array nums and two integers, k and limit. Your task is
to find a non-empty subsequence of nums that:

Has an alternating sum equal to k.

Maximizes the product of all its numbers without the product exceeding limit.

Return the product of the numbers in such a subsequence. If no subsequence
satisfies the requirements, return -1.

The alternating sum of a 0-indexed array is defined as the sum of the elements
at even indices minus the sum of the elements at odd indices.

Example 1:

Input: nums = [1,2,3], k = 2, limit = 10

Output: 6

Explanation:

The subsequences with an alternating sum of 2 are:

[1, 2, 3]

Alternating Sum: 1 - 2 + 3 = 2

Product: 1 * 2 * 3 = 6

[2]

Alternating Sum: 2

Product: 2

The maximum product within the limit is 6.

Example 2:

Input: nums = [0,2,3], k = -5, limit = 12

Output: -1

Explanation:

A subsequence with an alternating sum of exactly -5 does not exist.

Example 3:

Input: nums = [2,2,3,3], k = 0, limit = 9

Output: 9

Explanation:

The subsequences with an alternating sum of 0 are:

[2, 2]

Alternating Sum: 2 - 2 = 0

Product: 2 * 2 = 4

[3, 3]

Alternating Sum: 3 - 3 = 0

Product: 3 * 3 = 9

[2, 2, 3, 3]

Alternating Sum: 2 - 2 + 3 - 3 = 0

Product: 2 * 2 * 3 * 3 = 36

The subsequence [2, 2, 3, 3] has the greatest product with an alternating sum
equal to k, but 36 > 9. The next greatest product is 9, which is within the
limit.

Constraints:

1 <= nums.length <= 150

0 <= nums[i] <= 12

-10^5 <= k <= 10^5

1 <= limit <= 5000

"""

# V0
# IDEA : DP KEYED ON (PARITY, PRODUCT), WITH THE SET OF SUMS AS A BITMASK
#
#   the natural state is (index, parity of the length so far, alternating sum,
#   product).  the sum axis alone spans about 1800 values, so materialising the
#   whole product x sum grid is far too much.  the escape is that appending a
#   value v does two *independent* things: it multiplies the product by v, and
#   it shifts the sum by exactly +v or -v depending on the parity.
#
#   so keep, for each (parity, product), the whole set of reachable sums packed
#   into one integer bitmask (bit OFF+s set means sum s is reachable).  then a
#   transition is a single shift: mask << v when the new element lands on an
#   even index, mask >> v when it lands on an odd one.  no per-sum loop at all.
#
#   two product states need care.  a product that has already passed limit is
#   not dead: a later 0 collapses it back to 0, which is a legal answer.  so
#   carry one extra "overflow" bucket that maps to 0 when multiplied by 0 and
#   stays overflowed otherwise.  the reachable products are all products of
#   values <= 12 bounded by limit, so the dictionary stays small.
#
#   OFF = 1000 is safe: with n <= 150 and values <= 12 at most 75 elements sit
#   on odd indices, so no partial sum can drop below -900.
#
# time = O(n * P) bigint ops with P the reachable products, space = O(P)
class Solution(object):
    def maxProduct(self, nums, k, limit):
        OFF = 1000
        OVER = -1                              # bucket for "product > limit"
        if not -OFF < k < OFF:
            return -1

        def mul(p, v):
            if v == 0 or p == 0:
                return 0
            if p == OVER:
                return OVER
            q = p * v
            return q if q <= limit else OVER

        # dp[parity][product] -> bitmask of reachable alternating sums
        dp = [{}, {}]
        for v in nums:
            nxt = [dict(dp[0]), dict(dp[1])]
            # start a brand new subsequence with v at index 0 (even)
            p0 = v if 0 < v <= limit else (0 if v == 0 else OVER)
            nxt[1][p0] = nxt[1].get(p0, 0) | (1 << (OFF + v))
            # extend an existing one: even count so far -> v goes to an even index
            for p, mask in dp[0].items():
                q = mul(p, v)
                nxt[1][q] = nxt[1].get(q, 0) | (mask << v)
            # odd count so far -> v goes to an odd index, so it is subtracted
            for p, mask in dp[1].items():
                q = mul(p, v)
                nxt[0][q] = nxt[0].get(q, 0) | (mask >> v)
            dp = nxt

        bit = 1 << (OFF + k)
        best = -1
        for side in dp:
            for p, mask in side.items():
                if p != OVER and p > best and (mask & bit):
                    best = p
        return best
