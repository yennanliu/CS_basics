package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-peak-element/
/**
 * 162. Find Peak Element
 * Solved
 * N/A
 * Topics
 * premium lock icon
 * Companies
 * A peak element is an element that is strictly greater than its neighbors.
 *
 * Given a 0-indexed integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.
 *
 * You may imagine that nums[-1] = nums[n] = -∞. In other words, an element is always considered to be strictly greater than a neighbor that is outside the array.
 *
 * You must write an algorithm that runs in O(log n) time.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1]
 * Output: 2
 * Explanation: 3 is a peak element and your function should return the index number 2.
 * Example 2:
 *
 * Input: nums = [1,2,1,3,5,6,4]
 * Output: 5
 * Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * -231 <= nums[i] <= 231 - 1
 * nums[i] != nums[i + 1] for all valid i.
 *
 */

public class FindPeakElement {

    // V0
    // IDEA: BINARY SEARCH ( r > l )
    /**
     *  NOTE !!!  binary search pattern:
     *
     *  **Pattern 2: `while (l < r)` - Boundary/Peak Finding**
     *
     *    -> so for `peak` LC, use `r > l`
     *
     *    https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/binary_search.md
     */
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int findPeakElement(int[] nums) {
        int l = 0;
        int r = nums.length - 1;
        while (l < r) {
            int mid = (l + r) / 2;
            if (nums[mid] > nums[mid + 1])
                r = mid;
            else
                l = mid + 1;
        }
        return l;
    }

    // V0-0-1
    // IDEA: ITERATIVE BINARY SEARCH
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int findPeakElement_0_0_1(int[] nums) {

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
    /**
     * time = O(log N)
     * space = O(log N)
     */
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
    /**
     * time = O(log N)
     * space = O(1)
     */
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
