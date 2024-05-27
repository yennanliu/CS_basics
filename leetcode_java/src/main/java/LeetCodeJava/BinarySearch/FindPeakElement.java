package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-peak-element/

import java.util.Arrays;
import java.util.OptionalInt;

public class FindPeakElement {

    // V0
    // IDEA: ITERATIVE BINARY SEARCH
    public int findPeakElement(int[] nums) {

        int l = 0;
        int r = nums.length - 1;

        /**
         *
         *  NOTE !!!
         *
         *  // Change from 'l <= r' to 'l < r' to avoid accessing out of bounds
         *
         *
         *
         *
         *  DON'T use below code
         *
         *  while (l <= r) {
         *     int mid = (l + r) / 2;
         *     if (nums[mid] > nums[mid + 1])
         *         r = mid;
         *     else
         *         l = mid + 1;
         * }
         *
         *  reason :
         *
         *  When l becomes equal to r, the condition l <= r is still true.
         *  Inside the loop, mid is calculated as (l + r) / 2,
         *  which will be equal to l (or r) in this case. Then,
         *  nums[mid + 1] will be accessed, which is out of bounds
         *  if mid is the last index of the array.
         *
         */
        while (l < r) {
            int mid = (l + r) / 2;
            /**
             * If nums[mid] > nums[mid + 1],
             * it indicates a potential peak in the left half (including mid),
             * so r = mid.
             */
            if (nums[mid] > nums[mid + 1])
                r = mid;
            /**
             *
             * If nums[mid] <= nums[mid + 1],
             * it indicates a potential peak in the right half (excluding mid),
             * so l = mid + 1.
             */
            else
                l = mid + 1;
        }
        //  return r is OK as well
        // e.g. return r;
        /**
         * When l equals r, the loop terminates,
         * and l (or r) is the index of a peak element.
         */
        return l;
    }


    // V1
    // IDEA: LINEAR SCAN
    // https://leetcode.com/problems/find-peak-element/editorial/

    // V2
    // IDEA: RECURSIVE BINARY SEARCH
    // https://leetcode.com/problems/find-peak-element/editorial/
    // NOTE : ONLY have to compare index i with index i + 1 (its right element)
    //        ; otherwise, i-1 already returned as answer
    public int findPeakElement_2(int[] nums) {
        return search(nums, 0, nums.length - 1);
    }
    public int search(int[] nums, int l, int r) {
        if (l == r)
            return l;
        int mid = (l + r) / 2;
        if (nums[mid] > nums[mid + 1])
            return search(nums, l, mid);
        return search(nums, mid + 1, r);
    }

    // V3
    // IDEA: ITERATIVE BINARY SEARCH
    // https://leetcode.com/problems/find-peak-element/editorial/
    public int findPeakElement_3(int[] nums) {
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[mid + 1])
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }

}
