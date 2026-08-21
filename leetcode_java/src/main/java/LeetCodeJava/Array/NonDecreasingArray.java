package LeetCodeJava.Array;

// https://leetcode.com/problems/non-decreasing-array/

/**
 *  665. Non-decreasing Array
 *  Medium
 *
 *  Given an array nums with n integers, your task is to check if it could become
 *  non-decreasing by modifying at most one element.
 *
 *  We define an array is non-decreasing if nums[i] <= nums[i + 1] holds for every
 *  i (0-based) such that (0 <= i <= n - 2).
 *
 *  Example 1:
 *  Input: nums = [4,2,3]
 *  Output: true
 *  Explanation: You could modify the first 4 to 1 to get a non-decreasing array.
 *
 *  Example 2:
 *  Input: nums = [4,2,1]
 *  Output: false
 *  Explanation: You cannot get a non-decreasing array by modifying at most one element.
 *
 *  Constraints:
 *  n == nums.length
 *  1 <= n <= 10^4
 *  -10^5 <= nums[i] <= 10^5
 */
public class NonDecreasingArray {

    // V0
    // IDEA: GREEDY - on the first drop nums[i-1] > nums[i], we may fix it once.
    //       Prefer lowering nums[i-1] to nums[i] (safe when i < 2 or nums[i-2] <= nums[i]),
    //       otherwise we must raise nums[i] up to nums[i-1].
    /**
     * time = O(n)
     * space = O(1)
     */
    public boolean checkPossibility(int[] nums) {
        if (nums == null || nums.length <= 2) {
            return true;
        }
        int modified = 0;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] <= nums[i]) {
                continue;
            }
            modified++;
            if (modified > 1) {
                return false;
            }
            if (i < 2 || nums[i - 2] <= nums[i]) {
                nums[i - 1] = nums[i];
            } else {
                nums[i] = nums[i - 1];
            }
        }
        return true;
    }

    // V1
    // IDEA: LOCATE THE SINGLE DROP, then verify both candidate repairs on copies
    //       (unlike V0 this leaves the input array untouched)
    /**
     * time = O(n)
     * space = O(n)
     */
    public boolean checkPossibility_1(int[] nums) {
        if (nums == null || nums.length <= 2) {
            return true;
        }
        int bad = -1;
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > nums[i]) {
                if (bad != -1) {
                    return false; // two drops -> one edit can never be enough
                }
                bad = i;
            }
        }
        if (bad == -1) {
            return true;
        }
        int[] lower = nums.clone();
        lower[bad - 1] = lower[bad];
        if (isNonDecreasing_1(lower)) {
            return true;
        }
        int[] raise = nums.clone();
        raise[bad] = raise[bad - 1];
        return isNonDecreasing_1(raise);
    }

    private boolean isNonDecreasing_1(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }
        return true;
    }

    // V2
    // IDEA: brute force O(n^2) — rewrite each index with a neighbour value (if any
    //       legal replacement exists, a neighbour value is one) and re-check
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public boolean checkPossibility_2(int[] nums) {
        if (nums == null || nums.length <= 2) {
            return true;
        }
        if (isNonDecreasing_2(nums)) {
            return true; // zero edits needed
        }
        for (int i = 0; i < nums.length; i++) {
            int[] cand = nums.clone();
            cand[i] = (i > 0) ? nums[i - 1] : nums[i + 1];
            if (isNonDecreasing_2(cand)) {
                return true;
            }
        }
        return false;
    }

    private boolean isNonDecreasing_2(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) {
                return false;
            }
        }
        return true;
    }
}
