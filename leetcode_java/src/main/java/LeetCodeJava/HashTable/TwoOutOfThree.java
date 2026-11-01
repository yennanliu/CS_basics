package LeetCodeJava.HashTable;

// https://leetcode.com/problems/two-out-of-three/description/

import java.util.*;

/**
 * 2032. Two Out of Three
 * Easy
 *
 * Given three integer arrays nums1, nums2, and nums3, return a distinct array containing all the
 * values that are present in at least two out of the three arrays. You may return the values in any order.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,1,3,2], nums2 = [2,3], nums3 = [3]
 * Output: [3,2]
 * Explanation: The values that are present in at least two arrays are:
 * - 3, in all three arrays.
 * - 2, in nums1 and nums2.
 *
 * Example 2:
 *
 * Input: nums1 = [3,1], nums2 = [2,3], nums3 = [1,2]
 * Output: [2,3,1]
 *
 * Example 3:
 *
 * Input: nums1 = [1,2,2], nums2 = [4,3,3], nums3 = [5]
 * Output: []
 * Explanation: No value is present in at least two arrays.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length, nums3.length <= 100
 * 1 <= nums1[i], nums2[j], nums3[k] <= 100
 *
 */
public class TwoOutOfThree {

    // V0
    // IDEA: DEDUPE EACH ARRAY INTO A SET, THEN COUNT HOW MANY ARRAYS HOLD EACH VALUE
    //       duplicates inside one array must not count twice
    /**
     * time = O(N1 + N2 + N3)
     * space = O(N1 + N2 + N3)
     */
    public List<Integer> twoOutOfThree(int[] nums1, int[] nums2, int[] nums3) {
        // value -> number of arrays it appears in
        Map<Integer, Integer> arrayCount = new HashMap<>();
        int[][] arrays = {nums1, nums2, nums3};
        for (int[] array : arrays) {
            Set<Integer> distinct = new HashSet<>();
            for (int value : array) {
                distinct.add(value);
            }
            for (int value : distinct) {
                arrayCount.put(value, arrayCount.getOrDefault(value, 0) + 1);
            }
        }

        List<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : arrayCount.entrySet()) {
            if (entry.getValue() >= 2) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
