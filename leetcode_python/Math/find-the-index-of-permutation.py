"""

3109. Find the Index of Permutation
Medium
🔒 (premium)

Given an array perm of length n which is a permutation of [1, 2, ..., n], return the index of perm in the lexicographically sorted array of all of the permutations of [1, 2, ..., n].

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: perm = [1,2]
Output: 0
Explanation:
There are only two permutations in the following order:
[1,2], [2,1]
And [1,2] is at index 0.

Example 2:

Input: perm = [3,1,2]
Output: 4
Explanation:
There are only six permutations in the following order:
[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]
And [3,1,2] is at index 4.


Constraints:

1 <= n == perm.length <= 10^5
perm is a permutation of [1, 2, ..., n].

"""

# V0
# IDEA : FACTORIAL NUMBER SYSTEM — COUNT THE PERMUTATIONS SKIPPED AT EACH SLOT
#
#   fixing the first i positions, every permutation that puts a SMALLER
#   unused value at position i comes earlier, and each such choice admits
#   (n - i - 1)! completions. so
#
#       index = sum over i of  (unused values below perm[i]) * (n - i - 1)!
#
#   "unused values below x" is a prefix count over the values not yet
#   consumed, which a Fenwick tree answers in O(log n) : start with every
#   value present and remove each one as it is used.
#
#   the factorials are precomputed modulo 10^9 + 7.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def getPermutationIndex(self, perm):
        MOD = 10 ** 9 + 7
        n = len(perm)

        fact = [1] * (n + 1)
        for i in range(1, n + 1):
            fact[i] = fact[i - 1] * i % MOD

        tree = [0] * (n + 1)

        def add(i, v):
            while i <= n:
                tree[i] += v
                i += i & -i

        def query(i):                       # how many values <= i remain
            s = 0
            while i > 0:
                s += tree[i]
                i -= i & -i
            return s

        for v in range(1, n + 1):
            add(v, 1)

        res = 0
        for i, x in enumerate(perm):
            smaller = query(x - 1)          # unused values below perm[i]
            res = (res + smaller * fact[n - i - 1]) % MOD
            add(x, -1)
        return res
