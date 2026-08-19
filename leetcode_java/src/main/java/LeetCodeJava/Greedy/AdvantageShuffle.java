package LeetCodeJava.Greedy;

// https://leetcode.com/problems/advantage-shuffle/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  870. Advantage Shuffle
 *  Medium
 *
 *  You are given two integer arrays nums1 and nums2 both of the same length.
 *  The advantage of nums1 with respect to nums2 is the number of indices i for
 *  which nums1[i] > nums2[i].
 *
 *  Return any permutation of nums1 that maximizes its advantage with respect to nums2.
 *
 *  Example 1:
 *    Input: nums1 = [2,7,11,15], nums2 = [1,10,4,11]
 *    Output: [2,11,7,15]
 *
 *  Example 2:
 *    Input: nums1 = [12,24,8,32], nums2 = [13,25,32,11]
 *    Output: [24,32,8,12]
 *
 *  Constraints:
 *    1 <= nums1.length <= 10^5
 *    nums2.length == nums1.length
 *    0 <= nums1[i], nums2[i] <= 10^9
 */
public class AdvantageShuffle {

    // V0
    // IDEA: "Tian Ji horse racing". Sort nums1; walk nums2 from the largest value down.
    //       If our biggest remaining horse beats it, use it; otherwise sacrifice our
    //       weakest remaining horse there.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] advantageCount(int[] nums1, final int[] nums2) {
        int n = nums1.length;

        int[] sorted = nums1.clone();
        Arrays.sort(sorted);

        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }
        // indices of nums2 ordered by value, descending
        Arrays.sort(idx, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return Integer.compare(nums2[b], nums2[a]);
            }
        });

        int[] res = new int[n];
        int lo = 0;
        int hi = n - 1;

        for (int k = 0; k < n; k++) {
            int i = idx[k];
            if (sorted[hi] > nums2[i]) {
                res[i] = sorted[hi];
                hi--;
            } else {
                res[i] = sorted[lo];
                lo++;
            }
        }
        return res;
    }
}
