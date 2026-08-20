package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-even-and-odd-indices-independently/

import java.util.Arrays;

/**
 *  2164. Sort Even and Odd Indices Independently
 *  Easy
 *
 *  You are given a 0-indexed integer array nums. Rearrange the values of nums
 *  according to the following rules:
 *    - Sort the values at odd indices of nums in non-increasing order.
 *    - Sort the values at even indices of nums in non-decreasing order.
 *
 *  Return the array formed after rearranging the values of nums.
 *
 *  Example 1:
 *    Input: nums = [4,1,2,3]
 *    Output: [2,3,4,1]
 *    Explanation: odd indices (1,3) hold {1,3} -> non-increasing {3,1};
 *                 even indices (0,2) hold {4,2} -> non-decreasing {2,4}.
 *
 *  Example 2:
 *    Input: nums = [2,1]
 *    Output: [2,1]
 *    Explanation: one odd index and one even index -> nothing moves.
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    1 <= nums[i] <= 100
 */
public class SortEvenAndOddIndicesIndependently {

    // V0
    // IDEA: SPLIT BY INDEX PARITY, SORT EACH SLICE, WRITE THEM BACK
    //       the two parities are fully independent sub-sequences. Extract them
    //       into their own arrays, sort both ascending (Arrays.sort on int[]),
    //       then read the odd one BACKWARDS while writing back to get the
    //       non-increasing order without needing a comparator.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[] sortEvenOdd(int[] nums) {
        int n = nums.length;
        int evenLen = (n + 1) / 2;
        int oddLen = n / 2;

        int[] even = new int[evenLen];
        int[] odd = new int[oddLen];
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                even[i / 2] = nums[i];
            } else {
                odd[i / 2] = nums[i];
            }
        }

        Arrays.sort(even);
        Arrays.sort(odd);

        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                res[i] = even[i / 2];                      // ascending
            } else {
                res[i] = odd[oddLen - 1 - (i / 2)];        // read back -> descending
            }
        }
        return res;
    }
}
