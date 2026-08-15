"""

3336. Find the Number of Subsequences With Equal GCD
Hard

You are given an integer array nums.

Your task is to find the number of pairs of non-empty subsequences (seq1, seq2) of nums that satisfy the following conditions:

The subsequences seq1 and seq2 are disjoint, meaning no index of nums is common between them.
The GCD of the elements of seq1 is equal to the GCD of the elements of seq2.

Return the total number of such pairs.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,2,3,4]
Output: 10
Explanation:
There are 10 disjoint pairs of non-empty subsequences whose two GCDs agree
(each of them has both GCDs equal to 1).

Example 2:

Input: nums = [10,20,30]
Output: 2
Explanation:
There are 2 such pairs, and in both of them each side has a GCD of 10.

Example 3:

Input: nums = [1,1,1,1]
Output: 50


Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= 200

"""

# V0
# IDEA : DP OVER THE PAIR OF RUNNING GCDs, WITH 0 STANDING FOR "STILL EMPTY"
#
#   process the elements one at a time; each one either goes into seq1, into
#   seq2, or into neither — and disjointness is automatic because an index is
#   offered exactly once.
#
#   the only state worth keeping is the gcd of each side so far :
#
#       dp[(g1, g2)] = number of ways to reach those two gcds
#
#   using 0 for an empty side works because gcd(0, x) = x, so joining the
#   first element needs no special case.
#
#   the reachable pairs are far fewer than 201 x 201 — every gcd divides some
#   input value — so a dict keeps the sweep small.
#
#   the answer sums the states where both sides are non-empty and equal.
#
# time = O(n * reachable states), space = O(reachable states)
from collections import defaultdict


class Solution(object):
    def subsequencePairCount(self, nums):
        MOD = 10 ** 9 + 7

        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        dp = {(0, 0): 1}
        for x in nums:
            nxt = defaultdict(int, dp)          # option : skip x
            for (g1, g2), c in dp.items():
                k1 = (gcd(g1, x), g2)
                nxt[k1] = (nxt[k1] + c) % MOD
                k2 = (g1, gcd(g2, x))
                nxt[k2] = (nxt[k2] + c) % MOD
            dp = nxt

        return sum(c for (g1, g2), c in dp.items() if g1 == g2 and g1 > 0) % MOD
