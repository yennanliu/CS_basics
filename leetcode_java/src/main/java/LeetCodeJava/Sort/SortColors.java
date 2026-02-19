package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-colors/

import java.util.Arrays;
import java.util.Comparator;

/**
 * 75. Sort Colors
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white, and blue.
 *
 * We will use the integers 0, 1, and 2 to represent the color red, white, and blue, respectively.
 *
 * You must solve this problem without using the library's sort function.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,0,2,1,1,0]
 * Output: [0,0,1,1,2,2]
 * Example 2:
 *
 * Input: nums = [2,0,1]
 * Output: [0,1,2]
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 300
 * nums[i] is either 0, 1, or 2.
 *
 *
 * Follow up: Could you come up with a one-pass algorithm using only constant extra space?
 *
 *
 */
public class SortColors {

    // V0
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public void sortColors(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return;
        }

        Integer[] nums2 = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            nums2[i] = nums[i];
        }

        // sort on nums2
        Arrays.sort(nums2, new Comparator<Integer>() {
            @Override
            /**
             * time = O(N)
             * space = O(N)
             */
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        for (int i = 0; i < nums2.length; i++) {
            nums[i] = nums2[i];
        }

        return;
    }

    // V0-1
    // IDEA: BUBBLE SORT (any sorting algo (small -> big) works!)
    public void sortColors_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return;
        }
        if (nums.length == 1) {
            return;
        }
        // ??
        int n = nums.length;
        for (int l = 0; l < n; l++) {
            for (int r = l + 1; r < n; r++) {
                int tmp = nums[r];
                if (nums[r] < nums[l]) {
                    nums[r] = nums[l];
                    nums[l] = tmp;
                }
            }
        }
    }



    // V1
    // https://leetcode.com/problems/sort-colors/solutions/6751648/6-sorting-methods-with-images-cpythonjav-phsy/
    public void sortColors_1(int[] nums) {
        int low = 0, mid = 0, high = nums.length - 1;
        while (mid <= high) {
            if (nums[mid] == 0) {
                int tmp = nums[low];
                nums[low++] = nums[mid];
                nums[mid++] = tmp;
            } else if (nums[mid] == 1) {
                mid++;
            } else {
                int tmp = nums[mid];
                nums[mid] = nums[high];
                nums[high--] = tmp;
            }
        }
    }


    // V2
    // https://leetcode.com/problems/sort-colors/solutions/7588561/lifes-first-dsa-question-by-coder_sach_i-xkha/
    public void sortColors_2(int[] nums) {

        int r = 0, w = 0, b = nums.length - 1;

        while (w <= b) {
            if (nums[w] == 0) {
                int temp = nums[r];
                nums[r] = nums[w];
                nums[w] = temp;
                r++;
                w++;
            } else if (nums[w] == 1) {
                w++;
            } else {
                int temp = nums[w];
                nums[w] = nums[b];
                nums[b] = temp;
                b--;

            }
        }
    }



}
