package LeetCodeJava.Design;

// https://leetcode.com/problems/dot-product-of-two-sparse-vectors/description/
// https://leetcode.ca/2020-03-18-1570-Dot-Product-of-Two-Sparse-Vectors/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1570. Dot Product of Two Sparse Vectors
 * Medium
 *
 * Given two sparse vectors, compute their dot product.
 *
 * Implement class SparseVector:
 *
 * SparseVector(nums) Initializes the object with the vector nums
 * dotProduct(vec) Compute the dot product between the instance of SparseVector and vec
 *
 * A sparse vector is a vector that has mostly zero values, you should store the sparse
 * vector efficiently and compute the dot product between two SparseVector.
 *
 * Follow up: What if only one of the vectors is sparse?
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,0,0,2,3], nums2 = [0,3,0,4,0]
 * Output: 8
 * Explanation: v1 = SparseVector(nums1) , v2 = SparseVector(nums2)
 * v1.dotProduct(v2) = 1*0 + 0*3 + 0*0 + 2*4 + 3*0 = 8
 *
 * Example 2:
 *
 * Input: nums1 = [0,1,0,0,0], nums2 = [0,0,0,0,2]
 * Output: 0
 * Explanation: v1 = SparseVector(nums1) , v2 = SparseVector(nums2)
 * v1.dotProduct(v2) = 0*0 + 1*0 + 0*0 + 0*0 + 0*2 = 0
 *
 * Example 3:
 *
 * Input: nums1 = [0,1,0,0,2,0,0], nums2 = [1,0,0,0,3,0,4]
 * Output: 6
 *
 *
 * Constraints:
 *
 * n == nums1.length == nums2.length
 * 1 <= n <= 10^5
 * 0 <= nums1[i], nums2[i] <= 100
 *
 */
public class DotProductOfTwoSparseVectors {

    // V0
    // IDEA: HASH MAP (KEEP ONLY THE NON-ZERO ENTRIES)
    //       the vector is stored as {index -> value}, so memory is O(k) with k = number of
    //       non-zeros. a product term is non-zero only at an index present in BOTH vectors,
    //       so walk the SMALLER map and probe the other one.
    //       (also answers the follow-up: if only one vector is sparse, we iterate that one.)
    private final Map<Integer, Integer> nonZero;

    /**
     * time = O(n)
     * space = O(k), k = number of non-zero entries
     */
    public DotProductOfTwoSparseVectors(int[] nums) {
        this.nonZero = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                this.nonZero.put(i, nums[i]);
            }
        }
    }

    /**
     * time = O(min(k1, k2))
     * space = O(1)
     */
    public int dotProduct(DotProductOfTwoSparseVectors vec) {
        // iterate the smaller map, look up in the larger one
        Map<Integer, Integer> smaller = this.nonZero;
        Map<Integer, Integer> larger = vec.nonZero;
        if (smaller.size() > larger.size()) {
            smaller = vec.nonZero;
            larger = this.nonZero;
        }

        int result = 0;
        for (Map.Entry<Integer, Integer> entry : smaller.entrySet()) {
            Integer otherValue = larger.get(entry.getKey());
            // index missing in the other vector -> that term is 0
            if (otherValue != null) {
                result += entry.getValue() * otherValue;
            }
        }
        return result;
    }

    // V1
    // IDEA: LIST OF (index, value) PAIRS + TWO POINTERS
    //       the pairs are built in increasing index order, so two sorted lists can be merged
    //       like in merge sort: advance the pointer with the smaller index, and multiply when
    //       the indices match. no hashing, which is the answer interviewers usually want when
    //       they say "hash maps are slow for huge vectors".
    public static class SparseVector_1 {

        // each element is {index, value}, sorted by index
        private final List<int[]> pairs;

        /**
         * time = O(n)
         * space = O(k), k = number of non-zero entries
         */
        public SparseVector_1(int[] nums) {
            this.pairs = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != 0) {
                    this.pairs.add(new int[] {i, nums[i]});
                }
            }
        }

        /**
         * time = O(k1 + k2)
         * space = O(1)
         */
        public int dotProduct(SparseVector_1 vec) {
            List<int[]> mine = this.pairs;
            List<int[]> other = vec.pairs;

            int result = 0;
            int i = 0;
            int j = 0;
            while (i < mine.size() && j < other.size()) {
                int myIndex = mine.get(i)[0];
                int otherIndex = other.get(j)[0];
                if (myIndex == otherIndex) {
                    // both non-zero at this position -> contributes to the sum
                    result += mine.get(i)[1] * other.get(j)[1];
                    i++;
                    j++;
                } else if (myIndex < otherIndex) {
                    // the other vector is 0 at myIndex -> skip it
                    i++;
                } else {
                    j++;
                }
            }
            return result;
        }
    }
}
