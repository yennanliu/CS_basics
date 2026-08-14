"""

2569. Handling Sum Queries After Update
Hard

You are given two 0-indexed arrays nums1 and nums2 and a 2D array queries of queries. There are three types of queries:

1. For a query of type 1, queries[i] = [1, l, r]. Flip the values from 0 to 1 and from 1 to 0 in nums1 from index l to index r. Both l and r are 0-indexed.
2. For a query of type 2, queries[i] = [2, p, 0]. For every index 0 <= i < n, set nums2[i] = nums2[i] + nums1[i] * p.
3. For a query of type 3, queries[i] = [3, 0, 0]. Find the sum of the elements in nums2.

Return an array containing all the answers to the third type queries.


Example 1:

Input: nums1 = [1,0,1], nums2 = [0,0,0], queries = [[1,1,1],[2,1,0],[3,0,0]]
Output: [3]
Explanation: After the first query nums1 becomes [1,1,1]. After the second query, nums2 becomes [1,1,1], so the answer to the third query is 3. Thus, [3] is returned.

Example 2:

Input: nums1 = [1], nums2 = [5], queries = [[2,0,0],[3,0,0]]
Output: [5]
Explanation: After the first query, nums2 remains [5], so the answer to the second query is 5. Thus, [5] is returned.


Constraints:

1 <= nums1.length,nums2.length <= 10^5
nums1.length = nums2.length
1 <= queries.length <= 10^5
queries[i].length = 3
0 <= l <= r <= nums1.length - 1
0 <= p <= 10^6
0 <= nums1[i] <= 1
0 <= nums2[i] <= 10^9

"""

# V0
# IDEA : LAZY SEGMENT TREE (range flip / range count of ones) + RUNNING TOTAL
#
#   The trick is that nums2 NEVER needs to be materialised. A type-2 query
#   adds p * nums1[i] to every nums2[i], so the only thing a type-3 query ever
#   asks for is the total:
#
#       total += p * (number of ones in nums1)
#
#   So we only maintain nums1 in a segment tree and keep `total` as a scalar.
#   That collapses type 2 and type 3 to O(1) each, and leaves only the range
#   flip to be made fast.
#
#   Range flip on a 0/1 array is the classic lazy tag, because flipping a
#   segment maps its count of ones by:
#
#       ones(node) -> length(node) - ones(node)
#
#   which is computable from the node summary alone - no need to descend. The
#   lazy tag is a single XOR bit: flipping twice is the identity, so tags
#   COMBINE by ^= 1 rather than accumulating.
#
#   NOTE : the tag must be pushed down before any partial descent, otherwise a
#          child gets updated while still owing an un-applied flip from its
#          parent and the two operations get mis-ordered.
#
#   NOTE : n reaches 10^5. We pad the tree to a power of two and store lengths
#          per node, so padding leaves (length 1, value 0) flip harmlessly -
#          they contribute 1 to `length - ones` only if they were 0, so we
#          must NOT let updates reach them. Clamping the query range to
#          [l, r] (real indices only) guarantees that.
#
#   NOTE : recursion depth is only ~log2(10^5) = 17, so recursion is safe
#          here even though n is large.
#
# time = O(n + q * log(n)), space = O(n)
class Solution(object):
    def handleQuery(self, nums1, nums2, queries):
        n = len(nums1)

        # pad up to a power of two so children are always 2*i, 2*i+1
        size = 1
        while size < n:
            size <<= 1

        ones = [0] * (2 * size)     # count of 1s in the node's segment
        span = [0] * (2 * size)     # length of the node's segment
        lazy = [0] * (2 * size)     # pending flip, 0 or 1

        for i in range(size):
            span[size + i] = 1
            if i < n:
                ones[size + i] = nums1[i]
        for i in range(size - 1, 0, -1):
            ones[i] = ones[2 * i] + ones[2 * i + 1]
            span[i] = span[2 * i] + span[2 * i + 1]

        def apply_flip(node):
            ones[node] = span[node] - ones[node]
            lazy[node] ^= 1

        def push_down(node):
            if lazy[node]:
                apply_flip(2 * node)
                apply_flip(2 * node + 1)
                lazy[node] = 0

        def flip_range(node, nl, nr, l, r):
            if r < nl or nr < l:
                return
            if l <= nl and nr <= r:
                apply_flip(node)
                return
            push_down(node)
            mid = (nl + nr) // 2
            flip_range(2 * node, nl, mid, l, r)
            flip_range(2 * node + 1, mid + 1, nr, l, r)
            ones[node] = ones[2 * node] + ones[2 * node + 1]

        total = sum(nums2)
        res = []
        for op, a, b in queries:
            if op == 1:
                flip_range(1, 0, size - 1, a, b)
            elif op == 2:
                # every 1 in nums1 contributes p to the running total
                total += a * ones[1]
            else:
                res.append(total)
        return res
