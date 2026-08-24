"""

3525. Find X Value of Array II
Hard

You are given an array of positive integers nums and a positive integer k. You
are also given a 2D array queries, where queries[i] = [index_i, value_i,
start_i, x_i].

You are allowed to perform an operation once on nums, where you can remove any
suffix from nums such that nums remains non-empty.

The x-value of nums for a given x is defined as the number of ways to perform
this operation so that the product of the remaining elements leaves a remainder
of x modulo k.

For each query in queries you need to determine the x-value of nums for x_i
after performing the following actions:

Update nums[index_i] to value_i. Only this step persists for the rest of the
queries.

Remove the prefix nums[0..(start_i - 1)] (where nums[0..(-1)] will be used to
represent the empty prefix).

Return an array result of size queries.length where result[i] is the answer for
the i^th query.

A prefix of an array is a subarray that starts from the beginning of the array
and extends to any point within it.

A suffix of an array is a subarray that starts at any point within the array and
extends to the end of the array.

Note that the prefix and suffix to be chosen for the operation can be empty.

Note that x-value has a different definition in this version.

Example 1:

Input: nums = [1,2,3,4,5], k = 3, queries = [[2,2,0,2],[3,3,3,0],[0,1,0,1]]

Output: [2,2,2]

Explanation:

For query 0, nums becomes [1, 2, 2, 4, 5], and the empty prefix must be removed.
The possible operations are:

Remove the suffix [2, 4, 5]. nums becomes [1, 2].

Remove the empty suffix. nums becomes [1, 2, 2, 4, 5] with a product 80, which
gives remainder 2 when divided by 3.

For query 1, nums becomes [1, 2, 2, 3, 5], and the prefix [1, 2, 2] must be
removed. The possible operations are:

Remove the empty suffix. nums becomes [3, 5].

Remove the suffix [5]. nums becomes [3].

For query 2, nums becomes [1, 2, 2, 3, 5], and the empty prefix must be removed.
The possible operations are:

Remove the suffix [2, 2, 3, 5]. nums becomes [1].

Remove the suffix [3, 5]. nums becomes [1, 2, 2].

Example 2:

Input: nums = [1,2,4,8,16,32], k = 4, queries = [[0,2,0,2],[0,2,0,1]]

Output: [1,0]

Explanation:

For query 0, nums becomes [2, 2, 4, 8, 16, 32]. The only possible operation is:

Remove the suffix [2, 4, 8, 16, 32].

For query 1, nums becomes [2, 2, 4, 8, 16, 32]. There is no possible way to
perform the operation.

Example 3:

Input: nums = [1,1,2,1,1], k = 2, queries = [[2,1,0,1]]

Output: [5]

Constraints:

1 <= nums[i] <= 10^9

1 <= nums.length <= 10^5

1 <= k <= 5

1 <= queries.length <= 2 * 10^4

queries[i] == [index_i, value_i, start_i, x_i]

0 <= index_i <= nums.length - 1

1 <= value_i <= 10^9

0 <= start_i <= nums.length - 1

0 <= x_i <= k - 1

"""

# V0
# IDEA : SEGMENT TREE WHOSE NODE SUMMARISES ALL PREFIXES OF ITS SEGMENT
#
#   after the forced prefix removal the array left is nums[start:], and
#   "remove a suffix, stay non-empty" simply picks one of its prefixes.  so the
#   query asks how many prefixes of nums[start:] have product == x (mod k).
#
#   store in every segment tree node two things about its own slice:
#     * prod -- the product of the whole slice mod k,
#     * cnt[r] -- how many prefixes of the slice have product r mod k.
#   these merge in O(k): the prefixes of "A then B" are the prefixes of A,
#   plus A-entire followed by a prefix of B, and in the second case the residue
#   is A.prod * (that prefix's residue).  so
#       cnt[A.prod * r % k] += B.cnt[r],  prod = A.prod * B.prod % k.
#   no k x k table is ever needed, which matters because the merge runs on
#   every update and every query.
#
#   a point update rebuilds one root-to-leaf path, and a query folds the
#   O(log n) canonical nodes covering [start, n-1] left to right -- the fold
#   must stay ordered, because this merge is not commutative.
#
"""

DP def
    after the forced prefix removal the array is nums[start:], and "remove a
    suffix, stay non-empty" simply picks one of its PREFIXES - so a query asks
    how many prefixes of nums[start:] have product == x (mod k)

    every SEGMENT TREE node stores, about its own slice:

        prod  : product of the WHOLE slice mod k

        cnt[r]: how many PREFIXES of the slice have product r mod k

DP eq (the merge of A then B)

     prod = A.prod * B.prod % k

     cnt[r]                    = A.cnt[r]                 # prefixes inside A

     cnt[A.prod * r % k]      += B.cnt[r]                 # all of A, then a
                                                          # prefix of B


    -> e.g. the merge is O(k), never a k x k table - and it is NOT
              COMMUTATIVE, so a query must fold the O(log n) covering nodes
              strictly LEFT to RIGHT

     a point update rebuilds one root-to-leaf path

"""
# time = O(n * k + q * k * log n), space = O(n * k)
class Solution(object):
    def resultArray(self, nums, k, queries):
        n = len(nums)
        size = 1
        while size < n:
            size <<= 1
        # node = [prod, cnt_0, ..., cnt_{k-1}], None marks an empty slot
        tree = [None] * (2 * size)

        def leaf(v):
            v %= k
            node = [v] + [0] * k
            node[1 + v] = 1
            return node

        def merge(a, b):
            if a is None:
                return b
            if b is None:
                return a
            pa = a[0]
            node = a[:]
            for r in range(k):
                c = b[1 + r]
                if c:
                    node[1 + pa * r % k] += c
            node[0] = pa * b[0] % k
            return node

        for i in range(n):
            tree[size + i] = leaf(nums[i])
        for i in range(size - 1, 0, -1):
            tree[i] = merge(tree[2 * i], tree[2 * i + 1])

        res = []
        for idx, val, start, x in queries:
            p = size + idx
            tree[p] = leaf(val)
            p >>= 1
            while p:
                tree[p] = merge(tree[2 * p], tree[2 * p + 1])
                p >>= 1
            lo, hi = size + start, size + n - 1
            left = right = None
            while lo <= hi:
                if lo & 1:
                    left = merge(left, tree[lo])
                    lo += 1
                if not hi & 1:
                    right = merge(tree[hi], right)
                    hi -= 1
                lo >>= 1
                hi >>= 1
            whole = merge(left, right)
            res.append(whole[1 + x] if whole else 0)
        return res
