package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/intersection-of-two-arrays-ii/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  350. Intersection of Two Arrays II
 *  Easy
 *
 *  Given two integer arrays nums1 and nums2, return an array of their
 *  intersection. Each element in the result must appear as many times as it
 *  shows in both arrays and you may return the result in any order.
 *
 *  Example 1:
 *    Input: nums1 = [1,2,2,1], nums2 = [2,2]
 *    Output: [2,2]
 *
 *  Example 2:
 *    Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 *    Output: [4,9]
 *    Explanation: [9,4] is also accepted.
 *
 *  Constraints:
 *    1 <= nums1.length, nums2.length <= 1000
 *    0 <= nums1[i], nums2[i] <= 1000
 */
public class IntersectionOfTwoArraysII {

    // V0
    // IDEA: SORT + 2 POINTERS
    /**
     * time = O(N log N + M log M)
     * space = O(1)   // ignoring the sort / output
     */
    public int[] intersect(int[] nums1, int[] nums2) {
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
                buf[k++] = nums1[i];
                i++;
                j++;
            }
        }
        return Arrays.copyOf(buf, k);
    }

    // V1
    // IDEA: HASH MAP counting (better when one array is much smaller / unsorted stream)
    /**
     * time = O(N + M)
     * space = O(min(N, M))
     */
    public int[] intersect_1(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0) {
            return new int[0];
        }
        // always count the smaller array
        if (nums1.length > nums2.length) {
            return intersect_1(nums2, nums1);
        }

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int x : nums1) {
            cnt.put(x, cnt.getOrDefault(x, 0) + 1);
        }

        int[] buf = new int[nums1.length];
        int k = 0;
        for (int x : nums2) {
            int c = cnt.getOrDefault(x, 0);
            if (c > 0) {
                buf[k++] = x;
                cnt.put(x, c - 1);
            }
        }
        return Arrays.copyOf(buf, k);
    }
}
