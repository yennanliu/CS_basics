package LeetCodeJava.Sort;

// https://leetcode.com/problems/special-array-with-x-elements-greater-than-or-equal-x/

import java.util.Arrays;

/**
 *  1608. Special Array With X Elements Greater Than or Equal X
 *  Easy
 *
 *  You are given an array nums of non-negative integers. nums is considered
 *  special if there exists a number x such that there are exactly x numbers in
 *  nums that are greater than or equal to x.
 *
 *  Notice that x does not have to be an element in nums.
 *
 *  Return x if the array is special, otherwise, return -1. It can be proven that
 *  if nums is special, the value for x is unique.
 *
 *  Example 1:
 *    Input: nums = [3,5]
 *    Output: 2
 *    Explanation: there are 2 values (3 and 5) that are >= 2.
 *
 *  Example 2:
 *    Input: nums = [0,0]
 *    Output: -1
 *
 *  Example 3:
 *    Input: nums = [0,4,3,0,4]
 *    Output: 3
 *
 *  Constraints:
 *    1 <= nums.length <= 100
 *    0 <= nums[i] <= 1000
 */
public class SpecialArrayWithXElementsGreaterThanOrEqualX {

    // V0
    // IDEA: SORT + BINARY SEARCH THE COUNT OF ELEMENTS >= x
    //       x can only live in [1, n]: there cannot be more than n numbers >= x,
    //       and x = 0 would need zero numbers >= 0, impossible for a non-empty
    //       array. So sort once and for each candidate x get
    //         cnt(x) = n - lowerBound(sorted, x)
    //       in O(log n), then check cnt(x) == x.
    //       NOTE: the answer is unique, so we can return on the first hit.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int specialArray(int[] nums) {
        int[] arr = nums.clone();
        Arrays.sort(arr);
        int n = arr.length;

        for (int x = 1; x <= n; x++) {
            if (n - lowerBound(arr, x) == x) {
                return x;
            }
        }
        return -1;
    }

    // first index i with arr[i] >= target (i.e. python's bisect_left)
    private int lowerBound(int[] arr, int target) {
        int l = 0;
        int r = arr.length;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }
}
