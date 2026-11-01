package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/intersection-of-three-sorted-arrays/description/
// https://leetcode.ca/2019-03-27-1213-Intersection-of-Three-Sorted-Arrays/

import java.util.ArrayList;
import java.util.List;

/**
 * 1213. Intersection of Three Sorted Arrays
 * Easy
 *
 * Given three integer arrays arr1, arr2 and arr3 sorted in strictly increasing
 * order, return a sorted array of only the integers that appeared in all three
 * arrays.
 *
 *
 * Example 1:
 *
 * Input: arr1 = [1,2,3,4,5], arr2 = [1,2,5,7,9], arr3 = [1,3,4,5,8]
 * Output: [1,5]
 * Explanation: Only 1 and 5 appeared in the three arrays.
 *
 * Example 2:
 *
 * Input: arr1 = [197,418,523,876,1356], arr2 = [501,880,1593,1710,1870],
 *        arr3 = [521,682,1337,1395,1764]
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= arr1.length, arr2.length, arr3.length <= 1000
 * 1 <= arr1[i], arr2[i], arr3[i] <= 2000
 *
 */
public class IntersectionOfThreeSortedArrays {

    // V0
    // IDEA: 3 POINTERS (k-way merge on sorted arrays)
    //       one pointer per array. if the 3 pointed values are equal, it is a
    //       common element -> record it and advance all 3. otherwise advance every
    //       pointer whose value is below the current max (it can never match again).
    /**
     * time = O(N1 + N2 + N3)
     * space = O(1)  (excluding the output)
     */
    public List<Integer> arraysIntersection(int[] arr1, int[] arr2, int[] arr3) {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < arr1.length && j < arr2.length && k < arr3.length) {
            if (arr1[i] == arr2[j] && arr2[j] == arr3[k]) {
                result.add(arr1[i]);
                i++;
                j++;
                k++;
            } else {
                int largest = Math.max(arr1[i], Math.max(arr2[j], arr3[k]));
                // anything smaller than the largest cannot be in all three
                if (arr1[i] < largest) {
                    i++;
                }
                if (arr2[j] < largest) {
                    j++;
                }
                if (arr3[k] < largest) {
                    k++;
                }
            }
        }
        return result;
    }

    // V1
    // IDEA: COUNTING (values are bounded by 2000 and strictly increasing per array)
    //       each value appears at most once per array, so a value common to all
    //       three is exactly one whose count reaches 3. walk the counts in order
    //       to keep the output sorted.
    /**
     * time = O(N1 + N2 + N3 + V)  where V = 2000
     * space = O(V)
     */
    public List<Integer> arraysIntersection_1(int[] arr1, int[] arr2, int[] arr3) {
        final int maxValue = 2000;
        int[] count = new int[maxValue + 1];
        for (int value : arr1) {
            count[value]++;
        }
        for (int value : arr2) {
            count[value]++;
        }
        for (int value : arr3) {
            count[value]++;
        }

        List<Integer> result = new ArrayList<>();
        for (int value = 0; value <= maxValue; value++) {
            if (count[value] == 3) {
                result.add(value);
            }
        }
        return result;
    }
}
