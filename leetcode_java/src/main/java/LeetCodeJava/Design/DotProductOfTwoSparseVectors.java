package LeetCodeJava.Design;

// https://leetcode.com/problems/dot-product-of-two-sparse-vectors/

import java.util.HashMap;
import java.util.Map;

/**
 *  1570. Dot Product of Two Sparse Vectors
 *  Medium
 *
 *  Given two sparse vectors, compute their dot product.
 *
 *  Implement class SparseVector:
 *   - SparseVector(int[] nums) Initializes the object with the vector nums
 *   - int dotProduct(SparseVector vec) Computes the dot product between the instance of
 *     SparseVector and vec
 *
 *  A sparse vector is a vector that has mostly zero values, you should store the sparse
 *  vector efficiently and compute the dot product between two SparseVector.
 *
 *  Follow up: What if only one of the vectors is sparse?
 *
 *  Example 1:
 *    Input: nums1 = [1,0,0,2,3], nums2 = [0,3,0,4,0]
 *    Output: 8
 *    Explanation: v1.dotProduct(v2) = 1*0 + 0*3 + 0*0 + 2*4 + 3*0 = 8
 *
 *  Example 2:
 *    Input: nums1 = [0,1,0,0,0], nums2 = [0,0,0,0,2]
 *    Output: 0
 *
 *  Constraints:
 *    n == nums1.length == nums2.length
 *    1 <= n <= 10^5
 *    0 <= nums1[i], nums2[i] <= 100
 */
public class DotProductOfTwoSparseVectors {

    // V0
    // IDEA: HASH TABLE (keep only the NON-ZERO entries)
    //
    //   a sparse vector is stored as {index : value}, so the memory is O(K) with
    //   K = number of non-zeros rather than O(N).
    //
    //   the dot product only needs indices present in BOTH vectors, so walk over the
    //   SMALLER map and probe the other one -- that also answers the follow-up ("what if
    //   only one of the vectors is sparse?"): the cost is driven by the sparser side.
    /**
     * time = O(N) to build, O(min(K1, K2)) per dotProduct
     * space = O(K), K = number of non-zero entries
     */
    public static class SparseVector {

        private final Map<Integer, Integer> d = new HashMap<>();

        public SparseVector(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != 0) {
                    this.d.put(i, nums[i]);
                }
            }
        }

        // Return the dotProduct of two sparse vectors
        public int dotProduct(SparseVector vec) {
            Map<Integer, Integer> a = this.d;
            Map<Integer, Integer> b = vec.d;
            if (a.size() > b.size()) {
                Map<Integer, Integer> tmp = a;
                a = b;
                b = tmp;
            }
            int res = 0;
            for (Map.Entry<Integer, Integer> e : a.entrySet()) {
                Integer other = b.get(e.getKey());
                if (other != null) {
                    res += e.getValue() * other;
                }
            }
            return res;
        }
    }
}
