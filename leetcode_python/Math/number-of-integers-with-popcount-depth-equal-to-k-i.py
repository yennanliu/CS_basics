"""

3621. Number of Integers With Popcount-Depth Equal to K I
Hard

You are given two integers n and k.

For any positive integer x, define the following sequence:

p0 = x
p(i+1) = popcount(pi) for all i >= 0, where popcount(y) is the number of set
bits (1's) in the binary representation of y.

This sequence will eventually reach the value 1.

The popcount-depth of x is defined as the smallest integer d >= 0 such that
pd = 1.

For example, if x = 7 (binary representation "111"). Then, the sequence is:
7 -> 3 -> 2 -> 1, so the popcount-depth of 7 is 3.

Your task is to determine the number of integers in the range [1, n] whose
popcount-depth is exactly equal to k.

Return the number of such integers.


Example 1:

Input: n = 4, k = 1
Output: 2
Explanation:
The following integers in the range [1, 4] have popcount-depth exactly equal
to 1:

x    Binary    Sequence
2    "10"      2 -> 1
4    "100"     4 -> 1

Thus, the answer is 2.

Example 2:

Input: n = 7, k = 2
Output: 3
Explanation:
The following integers in the range [1, 7] have popcount-depth exactly equal
to 2:

x    Binary    Sequence
3    "11"      3 -> 2 -> 1
5    "101"     5 -> 2 -> 1
6    "110"     6 -> 2 -> 1

Thus, the answer is 3.


Constraints:

1 <= n <= 10^15
0 <= k <= 5

"""

# V0
# IDEA : COLLAPSE THE DEPTH ONTO THE POPCOUNT, THEN COUNT BY POPCOUNT
#
#   the first step of the sequence throws away everything about x except how
#   many bits it has: for x > 1, depth(x) = 1 + depth(popcount(x)). and
#   popcount(x) <= 50 for n <= 10^15, so the depths of every value that can
#   ever appear after step one are precomputable from a 50-entry table.
#
#   that turns the question into "how many x in [1, n] have popcount exactly
#   c", summed over the handful of c whose own depth is k-1. x = 1 is the one
#   value the recurrence does not cover — its depth is 0, not 1 — so it must
#   be subtracted back out of the c = 1 bucket when k = 1.
#
#   counting x <= n with a given popcount is the standard "walk the bits of n
#   from the top" argument: whenever n has a 1 at position i, the numbers that
#   agree with n above i but put a 0 there are all strictly below n, and their
#   low i bits are completely free — C(i, remaining) of them have the right
#   popcount. after passing every set bit we have described every x < n
#   exactly once, so only n itself is left to check.
#
# time = O((log n)^2), space = O((log n)^2)
class Solution(object):
    def popcountDepth(self, n, k):
        if k == 0:
            return 1

        L = n.bit_length()

        # binomials up to L
        C = [[0] * (L + 1) for _ in range(L + 1)]
        for i in range(L + 1):
            C[i][0] = 1
            for j in range(1, i + 1):
                C[i][j] = C[i - 1][j - 1] + C[i - 1][j]

        # depth of every reachable popcount value
        depth = [0] * (L + 1)
        for i in range(2, L + 1):
            depth[i] = depth[bin(i).count('1')] + 1

        def count_popcount(c):
            # how many x in [1, n] have exactly c set bits
            res = 0
            used = 0
            for i in range(L - 1, -1, -1):
                if not (n >> i) & 1:
                    continue
                need = c - used
                if 0 <= need <= i:
                    res += C[i][need]
                used += 1
            if used == c:
                res += 1
            return res

        ans = 0
        for c in range(1, L + 1):
            if depth[c] == k - 1:
                ans += count_popcount(c)
        if k == 1:
            ans -= 1  # x = 1 has popcount 1 but depth 0, not 1
        return ans
