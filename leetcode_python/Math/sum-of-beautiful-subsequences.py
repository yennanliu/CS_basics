"""

3671. Sum of Beautiful Subsequences
Hard

You are given an integer array nums of length n.

For every positive integer g, we define the beauty of g as the product of g
and the number of strictly increasing subsequences of nums whose greatest
common divisor (GCD) is exactly g.

Return the sum of beauty values for all positive integers g.

Since the answer could be very large, return it modulo 10^9 + 7.


Example 1:

Input: nums = [1,2,3]
Output: 10
Explanation:
All strictly increasing subsequences and their GCDs are:

Subsequence  GCD
[1]          1
[2]          2
[3]          3
[1,2]        1
[1,3]        1
[2,3]        1
[1,2,3]      1

Calculating beauty for each GCD:

GCD  Count of subsequences  Beauty (GCD x Count)
1    5                      1 x 5 = 5
2    1                      2 x 1 = 2
3    1                      3 x 1 = 3

Total beauty is 5 + 2 + 3 = 10.

Example 2:

Input: nums = [4,6]
Output: 12
Explanation:
All strictly increasing subsequences and their GCDs are:

Subsequence  GCD
[4]          4
[6]          6
[4,6]        2

Calculating beauty for each GCD:

GCD  Count of subsequences  Beauty (GCD x Count)
2    1                      2 x 1 = 2
4    1                      4 x 1 = 4
6    1                      6 x 1 = 6

Total beauty is 2 + 4 + 6 = 12.


Constraints:

1 <= n == nums.length <= 10^4
1 <= nums[i] <= 7 * 10^4

"""

# V0
# IDEA : DIVISOR SUM + EULER'S PHI COLLAPSES THE "EXACTLY g" CONDITION
#
#   "gcd exactly g" is awkward; "gcd divisible by g" is easy -- it just
#   means every chosen element is a multiple of g. so let F(h) count the
#   strictly increasing subsequences drawn from the multiples of h, and let
#   E(g) count those with gcd exactly g. then F(h) = sum over multiples of
#   h of E, and mobius inversion gives E(g) = sum_d mu(d) F(g*d).
#
#   the answer is sum_g g * E(g); swapping the order of summation regroups
#   it around each h as F(h) * sum_{g | h} g * mu(h/g), and that inner sum
#   is the dirichlet convolution Id * mu, which is exactly euler's totient.
#   so the whole inclusion-exclusion collapses to
#
#       answer = sum_h phi(h) * F(h)
#
#   with no explicit mobius pass at all -- one sieve for phi, and one count
#   per h.
#
#   F(h) itself is the classic "number of strictly increasing subsequences":
#   walk the multiples of h in ORIGINAL index order, and for each element x
#   set dp = 1 + (sum of dp over earlier elements with a smaller value),
#   which a fenwick tree keyed by value supplies in O(log). the total work
#   is bounded by the number of (element, divisor) pairs, i.e. the sum of
#   divisor counts, which stays small for values under 7 * 10^4.
#
# time = O(V log V + sum_x d(x) * log n), space = O(V + sum_x d(x))
class Solution(object):
    def totalBeauty(self, nums):
        MOD = 10 ** 9 + 7
        V = max(nums)

        present = bytearray(V + 1)
        for x in nums:
            present[x] = 1

        # divisors of every value that actually occurs
        divs = [None] * (V + 1)
        for x in range(1, V + 1):
            if present[x]:
                divs[x] = []
        for d in range(1, V + 1):
            for mul in range(d, V + 1, d):
                if present[mul]:
                    divs[mul].append(d)

        # euler totient sieve
        phi = list(range(V + 1))
        for p in range(2, V + 1):
            if phi[p] == p:                     # p is prime
                for j in range(p, V + 1, p):
                    phi[j] -= phi[j] // p

        # per divisor, the reduced values in original order
        groups = [None] * (V + 1)
        for x in nums:
            for d in divs[x]:
                g = groups[d]
                if g is None:
                    g = groups[d] = []
                g.append(x // d)

        def count_increasing(vals):
            order = sorted(set(vals))
            rank = {v: i + 1 for i, v in enumerate(order)}
            size = len(order)
            tree = [0] * (size + 1)
            total = 0
            for v in vals:
                i = rank[v]
                s = 0
                j = i - 1
                while j > 0:
                    s += tree[j]
                    j -= j & -j
                dp = (s + 1) % MOD
                total += dp
                j = i
                while j <= size:
                    tree[j] = (tree[j] + dp) % MOD
                    j += j & -j
            return total % MOD

        ans = 0
        for h in range(1, V + 1):
            vals = groups[h]
            if vals:
                ans = (ans + phi[h] * count_increasing(vals)) % MOD
        return ans
