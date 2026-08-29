package LeetCodeJava.Array;

// https://leetcode.com/problems/find-all-numbers-disappeared-in-an-array/

import java.util.ArrayList;
import java.util.List;

/**
 *  448. Find All Numbers Disappeared in an Array
 *  Easy
 *
 *  Given an array nums of n integers where nums[i] is in the range [1, n],
 *  return an array of all the integers in the range [1, n] that do not appear in nums.
 *
 *  Example 1:
 *  Input: nums = [4,3,2,7,8,2,3,1]
 *  Output: [5,6]
 *
 *  Example 2:
 *  Input: nums = [1,1]
 *  Output: [2]
 *
 *  Constraints:
 *  n == nums.length
 *  1 <= n <= 10^5
 *  1 <= nums[i] <= n
 *
 *  Follow up: Could you do it without extra space and in O(n) runtime?
 */
public class FindAllNumbersDisappearedInAnArray {

    // V0
    // IDEA: in-place marking - use sign of nums[|v| - 1] as a "seen" flag
    /**
     * time = O(n)
     * space = O(1)  (excluding the output list)
     */
    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        for (int i = 0; i < nums.length; i++) {
            int idx = Math.abs(nums[i]) - 1;
            if (nums[idx] > 0) {
                nums[idx] = -nums[idx];
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                res.add(i + 1);
            }
        }
        // restore the input array (be a good citizen)
        for (int i = 0; i < nums.length; i++) {
            nums[i] = Math.abs(nums[i]);
        }
        return res;
    }

    // V1
    // IDEA: boolean "seen" array (simpler, uses O(n) extra space)
    /**
     * time = O(n)
     * space = O(n)
     */
    public List<Integer> findDisappearedNumbers_1(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        boolean[] seen = new boolean[nums.length + 1];
        for (int v : nums) {
            seen[v] = true;
        }
        for (int i = 1; i <= nums.length; i++) {
            if (!seen[i]) {
                res.add(i);
            }
        }
        return res;
    }

    // V2
    // IDEA: CYCLIC SORT — repeatedly swap nums[i] to its home slot (value v -> index
    //       v - 1); afterwards any index i whose value != i + 1 is a missing number.
    /**
     * time = O(n)   (each swap puts one value in its final place)
     * space = O(1)  (excluding the output list)
     */
    public List<Integer> findDisappearedNumbers_2(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // stop when the value already sits at its home slot (also breaks dup cycles)
            while (nums[i] != nums[nums[i] - 1]) {
                int j = nums[i] - 1;
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                res.add(i + 1);
            }
        }
        return res;
    }
}
