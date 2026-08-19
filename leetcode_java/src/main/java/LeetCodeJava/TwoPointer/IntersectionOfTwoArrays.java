package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/intersection-of-two-arrays/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *  349. Intersection of Two Arrays
 *  Easy
 *
 *  Given two integer arrays nums1 and nums2, return an array of their
 *  intersection. Each element in the result must be unique and you may return
 *  the result in any order.
 *
 *  Example 1:
 *    Input: nums1 = [1,2,2,1], nums2 = [2,2]
 *    Output: [2]
 *
 *  Example 2:
 *    Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 *    Output: [9,4]
 *    Explanation: [4,9] is also accepted.
 *
 *  Constraints:
 *    1 <= nums1.length, nums2.length <= 1000
 *    0 <= nums1[i], nums2[i] <= 1000
 */
public class IntersectionOfTwoArrays {

    // V0
    // IDEA: HASH SET
    /**
     * time = O(N + M)
     * space = O(N)
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0) {
            return new int[0];
        }

        Set<Integer> seen = new HashSet<>();
        for (int x : nums1) {
            seen.add(x);
        }

        Set<Integer> common = new HashSet<>();
        for (int x : nums2) {
            if (seen.contains(x)) {
                common.add(x);
            }
        }

        int[] res = new int[common.size()];
        int i = 0;
        for (Integer x : common) {
            res[i++] = x;
        }
        return res;
    }

    // V1
    // IDEA: SORT + 2 POINTERS (O(1) extra space beyond the output)
    /**
     * time = O(N log N + M log M)
     * space = O(1)   // ignoring the sort / output
     */
    public int[] intersection_1(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0) {
            return new int[0];
        }
        Arrays.sort(nums1);
        Arrays.sort(nums2);

        int[] buf = new int[Math.min(nums1.length, nums2.length)];
        int k = 0;
        int i = 0;
        int j = 0;
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] < nums2[j]) {
                i++;
            } else if (nums1[i] > nums2[j]) {
                j++;
            } else {
                // dedup : only append if different from last appended
                if (k == 0 || buf[k - 1] != nums1[i]) {
                    buf[k++] = nums1[i];
                }
                i++;
                j++;
            }
        }
        return Arrays.copyOf(buf, k);
    }
}
