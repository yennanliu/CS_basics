"""

2179. Count Good Triplets in an Array
Hard

You are given two 0-indexed arrays nums1 and nums2 of length n, both of which are permutations of [0, 1, ..., n - 1].

A good triplet is a set of 3 distinct values which are present in increasing order by position both in nums1 and nums2. In other words, if we consider pos1v as the index of the value v in nums1 and pos2v as the index of the value v in nums2, then a good triplet will be a set (x, y, z) where 0 <= x, y, z <= n - 1, such that pos1x < pos1y < pos1z and pos2x < pos2y < pos2z.

Return the total number of good triplets.

Example 1:

Input: nums1 = [2,0,1,3], nums2 = [0,1,2,3]
Output: 1
Explanation:
There are 4 triplets (x,y,z) such that pos1x < pos1y < pos1z. They are (2,0,1), (2,0,3), (2,1,3), and (0,1,3).
Out of those triplets, only the triplet (0,1,3) satisfies pos2x < pos2y < pos2z. Hence, there is only 1 good triplet.

Example 2:

Input: nums1 = [4,0,1,3,2], nums2 = [4,1,0,2,3]
Output: 4
Explanation: The 4 good triplets are (4,0,3), (4,0,2), (4,1,3), and (4,1,2).

Constraints:

n == nums1.length == nums2.length
3 <= n <= 10^5
0 <= nums1[i], nums2[i] <= n - 1
nums1 and nums2 are permutations of [0, 1, ..., n - 1].

"""

# V0
# IDEA : BIT (FENWICK) over positions in nums2, count "middle" elements
#
#   map every value to its index in nums2 : pos[v].
#   sweep nums1 left -> right, so everything already inserted is to the
#   LEFT of the current value in nums1.  fix the current value as the
#   MIDDLE y of the triplet :
#     left  = # inserted values with pos2 < pos2[y]   -> valid x
#     right = # not-yet-inserted values with pos2 > pos2[y] -> valid z
#   answer += left * right.
#   NOTE : right is (total values after y in nums2) minus (already
#          inserted ones after y) = (n - p) - (inserted - left).
#
# time = O(n log n), space = O(n)
class BIT(object):
    def __init__(self, n):
        self.n = n
        self.c = [0] * (n + 1)

    def update(self, i, delta):
        while i <= self.n:
            self.c[i] += delta
            i += i & (-i)

    def query(self, i):
        s = 0
        while i > 0:
            s += self.c[i]
            i -= i & (-i)
        return s


class Solution(object):
    def goodTriplets(self, nums1, nums2):
        n = len(nums1)
        pos = [0] * n
        for i, v in enumerate(nums2):
            pos[v] = i + 1          # 1-indexed for the BIT

        bit = BIT(n)
        res = 0
        for k, v in enumerate(nums1):
            p = pos[v]
            left = bit.query(p)          # inserted, pos2 < p
            right = (n - p) - (k - left) # not inserted yet, pos2 > p
            res += left * right
            bit.update(p, 1)
        return res
