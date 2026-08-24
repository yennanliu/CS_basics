"""

3533. Concatenated Divisibility
Hard

You are given an array of positive integers nums and a positive integer k.

A permutation of nums is said to form a divisible concatenation if, when you
concatenate the decimal representations of the numbers in the order specified by
the permutation, the resulting number is divisible by k.

Return the lexicographically smallest permutation (when considered as a list of
integers) that forms a divisible concatenation. If no such permutation exists,
return an empty list.

Example 1:

Input: nums = [3,12,45], k = 5

Output: [3,12,45]

Explanation:

Permutation  | Concatenated Value | Divisible by 5
-------------+--------------------+---------------
[3, 12, 45]  | 31245              | Yes
[3, 45, 12]  | 34512              | No
[12, 3, 45]  | 12345              | Yes
[12, 45, 3]  | 12453              | No
[45, 3, 12]  | 45312              | No
[45, 12, 3]  | 45123              | No

The lexicographically smallest permutation that forms a divisible concatenation
is [3,12,45].

Example 2:

Input: nums = [10,5], k = 10

Output: [5,10]

Explanation:

Permutation | Concatenated Value | Divisible by 10
------------+--------------------+----------------
[5, 10]     | 510                | Yes
[10, 5]     | 105                | No

The lexicographically smallest permutation that forms a divisible concatenation
is [5,10].

Example 3:

Input: nums = [1,2,3], k = 5

Output: []

Explanation:

Since no permutation of nums forms a valid divisible concatenation, return an
empty list.

Constraints:

1 <= nums.length <= 13

1 <= nums[i] <= 10^5

1 <= k <= 100

"""

# V0
# IDEA : PRECOMPUTE THE REACHABLE REMAINDERS OF EVERY SUBSET, THEN PICK GREEDILY
#
#   concatenating x in front of an already-built number Q of digit length L
#   gives x * 10^L + Q, so a subset's concatenation modulo k is determined by
#   the subset (which fixes the total digit length) and by the order inside it.
#   collect, for every subset M, the *set* of residues its permutations can
#   produce -- call it G[M].  building G[M] from G[M \ {i}] only adds the
#   constant c = nums[i] * 10^(len of M \ {i}) mod k to every residue.
#
#   adding a constant modulo k is a rotation of the residue class, so if G[M]
#   is stored as a k-bit integer the whole transition is one cyclic shift.
#   that turns the 2^n * n * k table into 2^n * n big-integer operations, which
#   is what makes n = 13 with k = 100 comfortable.
#
#   with G in hand, the lexicographically smallest answer falls out of a plain
#   greedy: keep the residue r of the prefix built so far, try the remaining
#   numbers in increasing value, and accept the first one for which the rest is
#   still completable -- the whole number is r * 10^(len of rest) + Q, so the
#   test is just "is residue (-r * 10^len) mod k present in G[rest]".
#
"""

DP def
    concatenating x in FRONT of an already-built number Q of digit length L
    gives x * 10^L + Q, so a subset's residue mod k depends on the subset
    (which fixes the digit length) plus the order inside it.

    G[mask]: the SET of residues mod k that the permutations of subset `mask`

             can produce, stored as a k-bit integer (bit r set <-> residue r)

DP eq

     G[mask] = union over i in mask of
                  rotate( G[mask ^ (1<<i)], c )

               where c = nums[i] * 10^(total digit len of mask ^ (1<<i)) mod k


    -> e.g. adding a CONSTANT mod k is a cyclic rotation of the residue
              class, so as a bitset the whole transition is one cyclic shift
              -> 2^n * n big-int ops instead of 2^n * n * k

     init: G[0] = bit 0 set (the empty concatenation is 0)

     then greedily build the lexicographically smallest number: keep the
     prefix residue r, try remaining numbers in INCREASING value, accept the
     first one where residue (-r * 10^len(rest)) mod k is present in G[rest]

"""
# time = O(2^n * n), space = O(2^n)
class Solution(object):
    def concatenatedDivisibility(self, nums, k):
        n = len(nums)
        pw = [1] * (7 * n + 1)              # 10^t mod k, t bounded by the digits
        for t in range(1, len(pw)):
            pw[t] = pw[t - 1] * 10 % k
        dlen = [len(str(v)) for v in nums]
        vmod = [v % k for v in nums]

        full = (1 << n) - 1
        tot_len = [0] * (1 << n)
        for mask in range(1, 1 << n):
            b = mask & -mask
            i = b.bit_length() - 1
            tot_len[mask] = tot_len[mask ^ b] + dlen[i]

        allbits = (1 << k) - 1
        G = [0] * (1 << n)
        G[0] = 1                            # the empty concatenation is 0
        for mask in range(1, 1 << n):
            acc = 0
            rest = mask
            while rest:
                b = rest & -rest
                rest ^= b
                i = b.bit_length() - 1
                sub = mask ^ b
                c = vmod[i] * pw[tot_len[sub]] % k      # i goes first
                g = G[sub]
                acc |= ((g << c) | (g >> (k - c))) & allbits
            G[mask] = acc

        # need residue 0 out of the whole set
        if not G[full] & 1:
            return []

        order = sorted(range(n), key=lambda i: (nums[i], i))
        used = 0
        r = 0
        out = []
        for _ in range(n):
            for i in order:
                b = 1 << i
                if used & b:
                    continue
                nr = (r * pw[dlen[i]] + vmod[i]) % k
                rest = full ^ used ^ b
                need = (-nr * pw[tot_len[rest]]) % k
                if G[rest] >> need & 1:
                    used |= b
                    r = nr
                    out.append(nums[i])
                    break
        return out
