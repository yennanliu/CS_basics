"""

2426. Number of Pairs Satisfying Inequality
Hard

You are given two 0-indexed integer arrays nums1 and nums2, each of size n, and an integer diff. Find the number of pairs (i, j) such that:

0 <= i < j <= n - 1 and
nums1[i] - nums1[j] <= nums2[i] - nums2[j] + diff.

Return the number of pairs that satisfy the conditions.


Example 1:

Input: nums1 = [3,2,5], nums2 = [2,2,1], diff = 1
Output: 3
Explanation:
There are 3 pairs that satisfy the conditions:
1. i = 0, j = 1: 3 - 2 <= 2 - 2 + 1. Since i < j and 1 <= 1, this pair satisfies the conditions.
2. i = 0, j = 2: 3 - 5 <= 2 - 1 + 1. Since i < j and -2 <= 2, this pair satisfies the conditions.
3. i = 1, j = 2: 2 - 5 <= 2 - 1 + 1. Since i < j and -3 <= 2, this pair satisfies the conditions.
Therefore, we return 3.

Example 2:

Input: nums1 = [3,-1], nums2 = [-2,2], diff = -1
Output: 0
Explanation:
Since there does not exist any pair that satisfies the conditions, we return 0.


Constraints:

n == nums1.length == nums2.length
2 <= n <= 10^5
-10^4 <= nums1[i], nums2[i] <= 10^4
-10^4 <= diff <= 10^4

"""

# V0
# IDEA : REARRANGE INTO ONE ARRAY, THEN COUNT WITH A FENWICK TREE
#
#   move each index's terms to its own side :
#       nums1[i] - nums1[j] <= nums2[i] - nums2[j] + diff
#       (nums1[i] - nums2[i]) <= (nums1[j] - nums2[j]) + diff
#   so with  a[k] = nums1[k] - nums2[k]  the condition is simply
#       a[i] <= a[j] + diff       for i < j
#
#   sweep j left to right, and for each j ask "how many earlier a[i] are
#   <= a[j] + diff?" — a prefix count over the VALUE axis, which a Fenwick
#   (binary indexed) tree answers in O(log V).
#
#   values live in [-2*10^4, 2*10^4], so a fixed offset maps them to
#   1-indexed tree positions.
#
# time = O(n log V), space = O(V)
class Solution(object):
    def numberOfPairs(self, nums1, nums2, diff):
        OFFSET = 20000
        SIZE = 40002                    # values -20000 .. 20000, 1-indexed
        tree = [0] * (SIZE + 1)

        def add(pos):
            while pos <= SIZE:
                tree[pos] += 1
                pos += pos & -pos

        def query(pos):
            """how many stored values sit at index <= pos"""
            total = 0
            pos = min(pos, SIZE)
            while pos > 0:
                total += tree[pos]
                pos -= pos & -pos
            return total

        res = 0
        for x, y in zip(nums1, nums2):
            a = x - y
            bound = a + diff + OFFSET + 1     # 1-indexed position of a + diff
            if bound >= 1:
                res += query(bound)
            add(a + OFFSET + 1)
        return res
