"""

1570. Dot Product of Two Sparse Vectors
Medium

Given two sparse vectors, compute their dot product.

Implement class SparseVector:

SparseVector(nums) Initializes the object with the vector nums
dotProduct(vec) Compute the dot product between the instance of SparseVector and vec

A sparse vector is a vector that has mostly zero values, you should store the sparse vector efficiently and compute the dot product between two SparseVector.

Follow up: What if only one of the vectors is sparse?

Example 1:

Input: nums1 = [1,0,0,2,3], nums2 = [0,3,0,4,0]
Output: 8
Explanation: v1 = SparseVector(nums1) , v2 = SparseVector(nums2)
v1.dotProduct(v2) = 1*0 + 0*3 + 0*0 + 2*4 + 3*0 = 8

Example 2:

Input: nums1 = [0,1,0,0,0], nums2 = [0,0,0,0,2]
Output: 0
Explanation: v1 = SparseVector(nums1) , v2 = SparseVector(nums2)
v1.dotProduct(v2) = 0*0 + 1*0 + 0*0 + 0*0 + 0*2 = 0

Example 3:

Input: nums1 = [0,1,0,0,2,0,0], nums2 = [1,0,0,0,3,0,4]
Output: 6

Constraints:

n == nums1.length == nums2.length
1 <= n <= 10^5
0 <= nums1[i], nums2[i] <= 100

"""

# V0
# IDEA : HASH TABLE (keep only the non-zero entries)
#
#   a sparse vector is stored as {index : value} so the memory is O(k)
#   with k = number of non-zeros.
#   the dot product only needs indices present in BOTH vectors, so walk
#   over the SMALLER dict and probe the other one.
#
# time = O(n) to build, O(min(k1, k2)) per dot product
# space = O(k), k = number of non-zero entries
class SparseVector(object):
    def __init__(self, nums):
        self.d = {}
        for i, v in enumerate(nums):
            if v:
                self.d[i] = v

    # Return the dotProduct of two sparse vectors
    def dotProduct(self, vec):
        a, b = self.d, vec.d
        if len(a) > len(b):
            a, b = b, a
        res = 0
        for i, v in a.items():
            if i in b:
                res += v * b[i]
        return res

# Your SparseVector object will be instantiated and called as such:
# v1 = SparseVector(nums1)
# v2 = SparseVector(nums2)
# ans = v1.dotProduct(v2)
