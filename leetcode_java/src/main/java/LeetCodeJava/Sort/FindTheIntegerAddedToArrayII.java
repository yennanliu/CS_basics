package LeetCodeJava.Sort;

// https://leetcode.com/problems/find-the-integer-added-to-array-ii/

import java.util.Arrays;

/**
 *  3132. Find the Integer Added to Array II
 *  Medium
 *
 *  You are given two integer arrays nums1 and nums2.
 *
 *  From nums1 two elements have been removed, and all other elements have been
 *  increased (or decreased in the case of negative) by an integer, represented by
 *  the variable x.
 *
 *  As a result, nums1 becomes equal to nums2. Two arrays are considered equal when
 *  they contain the same integers with the same frequencies.
 *
 *  Return the minimum possible integer x that achieves this equivalence.
 *
 *  Example 1:
 *    Input: nums1 = [4,20,16,12,8], nums2 = [14,18,10]
 *    Output: -2
 *    Explanation: after removing indices [0,4] and adding -2, nums1 = [18,14,10].
 *
 *  Example 2:
 *    Input: nums1 = [3,5,5,3], nums2 = [7,7]
 *    Output: 2
 *    Explanation: after removing indices [0,3] and adding 2, nums1 = [7,7].
 *
 *  Constraints:
 *    3 <= nums1.length <= 200
 *    nums2.length == nums1.length - 2
 *    0 <= nums1[i], nums2[i] <= 1000
 *    The test cases guarantee such an x exists.
 */
public class FindTheIntegerAddedToArrayII {

    // V0
    // IDEA: ONLY THREE CANDIDATE SHIFTS - THE SMALLEST SURVIVOR IS AMONG THE FIRST 3
    //       sort both arrays. exactly two elements of nums1 are dropped, so after
    //       sorting the smallest KEPT element of nums1 is one of nums1[0],
    //       nums1[1], nums1[2]. that element must line up with nums2[0], giving
    //         x = nums2[0] - nums1[i]      for i in {0, 1, 2}
    //       for each candidate a greedy two-pointer sweep checks feasibility:
    //       walk nums1 and try to match each nums2 value in order; anything that
    //       does not match is one of the two dropped elements.
    //       keep the minimum feasible x.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int minimumAddedInteger(int[] nums1, int[] nums2) {
        int[] a = nums1.clone();
        int[] b = nums2.clone();
        Arrays.sort(a);
        Arrays.sort(b);

        int best = Integer.MAX_VALUE;
        for (int i = 0; i < 3; i++) {
            int x = b[0] - a[i];
            if (x < best && feasible(a, b, x)) {
                best = x;
            }
        }
        return best;
    }

    private boolean feasible(int[] a, int[] b, int x) {
        int j = 0;
        for (int v : a) {
            if (j < b.length && v + x == b[j]) {
                j++;
            }
        }
        return j == b.length;
    }
}
