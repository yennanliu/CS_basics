"""

3405. Count the Number of Arrays with K Matching Adjacent Elements
Hard

You are given three integers n, m, k. A good array arr of size n is defined as
follows:

Each element in arr is in the inclusive range [1, m].
Exactly k indices i (where 1 <= i < n) satisfy the condition arr[i - 1] ==
arr[i].

Return the number of good arrays that can be formed.

Since the answer may be very large, return it modulo 10^9 + 7.

Example 1:

Input: n = 3, m = 2, k = 1

Output: 4

Explanation:

There are 4 good arrays. They are [1, 1, 2], [1, 2, 2], [2, 1, 1] and [2, 2, 1].
Hence, the answer is 4.

Example 2:

Input: n = 4, m = 2, k = 2

Output: 6

Explanation:

The good arrays are [1, 1, 1, 2], [1, 1, 2, 2], [1, 2, 2, 2], [2, 1, 1, 1], [2,
2, 1, 1] and [2, 2, 2, 1].
Hence, the answer is 6.

Example 3:

Input: n = 5, m = 2, k = 0

Output: 2

Explanation:

The good arrays are [1, 2, 1, 2, 1] and [2, 1, 2, 1, 2]. Hence, the answer is 2.

Constraints:

1 <= n <= 10^5
1 <= m <= 10^5
0 <= k <= n - 1

"""

# V0
# IDEA : CLOSED-FORM COUNT — CHOOSE THE MATCHING POSITIONS, THEN FILL FREELY
#
#   look at the n - 1 adjacent boundaries.  a "good" array is decided by two
#   independent choices:
#
#     1. which k of the n - 1 boundaries are the equal ones — C(n-1, k) ways;
#     2. what the values actually are.
#
#   for (2), walk the array left to right.  arr[0] is free: m choices.  at each
#   boundary marked equal the next element is forced (it copies its neighbour),
#   and at each of the remaining n - 1 - k boundaries the next element must
#   merely *differ* from its neighbour: m - 1 choices, regardless of what the
#   neighbour is.  so every marking pattern admits exactly m * (m-1)^(n-1-k)
#   arrays, and no array is produced twice because the marking is read straight
#   off the array.
#
#   answer = C(n-1, k) * m * (m-1)^(n-1-k)  (mod 1e9+7).
#
# time = O(n + log n), space = O(1)
class Solution(object):
    def countGoodArrays(self, n, m, k):
        MOD = 10 ** 9 + 7
        # C(n-1, k) mod MOD
        num = 1
        den = 1
        for i in range(k):
            num = num * ((n - 1 - i) % MOD) % MOD
            den = den * (i + 1) % MOD
        comb = num * pow(den, MOD - 2, MOD) % MOD
        return comb * m % MOD * pow(m - 1, n - 1 - k, MOD) % MOD
