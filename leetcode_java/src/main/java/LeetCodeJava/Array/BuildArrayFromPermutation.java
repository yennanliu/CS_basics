package LeetCodeJava.Array;

// https://leetcode.com/problems/build-array-from-permutation/description/
/**
 *  1920. Build Array from Permutation
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a zero-based permutation nums (0-indexed), build an array ans of the same length where ans[i] = nums[nums[i]] for each 0 <= i < nums.length and return it.
 *
 * A zero-based permutation nums is an array of distinct integers from 0 to nums.length - 1 (inclusive).
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [0,2,1,5,3,4]
 * Output: [0,1,2,4,5,3]
 * Explanation: The array ans is built as follows:
 * ans = [nums[nums[0]], nums[nums[1]], nums[nums[2]], nums[nums[3]], nums[nums[4]], nums[nums[5]]]
 *     = [nums[0], nums[2], nums[1], nums[5], nums[3], nums[4]]
 *     = [0,1,2,4,5,3]
 * Example 2:
 *
 * Input: nums = [5,0,1,2,3,4]
 * Output: [4,5,0,1,2,3]
 * Explanation: The array ans is built as follows:
 * ans = [nums[nums[0]], nums[nums[1]], nums[nums[2]], nums[nums[3]], nums[nums[4]], nums[nums[5]]]
 *     = [nums[5], nums[0], nums[1], nums[2], nums[3], nums[4]]
 *     = [4,5,0,1,2,3]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] < nums.length
 * The elements in nums are distinct.
 *
 *
 * Follow-up: Can you solve it without using an extra space (i.e., O(1) memory)?
 *
 *
 */
public class BuildArrayFromPermutation {

    // V0
    // IDEA: ARRAY OP
    public int[] buildArray(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return null;
        }
        int[] res = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            res[i] = nums[nums[i]];
        }

        return res;
    }

    // V1-1
    // IDEA: Build As Required
    // https://leetcode.com/problems/build-array-from-permutation/editorial/
    public int[] buildArray_1_1(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; ++i) {
            ans[i] = nums[nums[i]];
        }
        return ans;
    }

    // V1-2
    // IDEA: Build In Place
    // https://leetcode.com/problems/build-array-from-permutation/editorial/
    public int[] buildArray_1_2(int[] nums) {
        int n = nums.length;
        // Build the final value on the first iteration
        for (int i = 0; i < n; ++i) {
            nums[i] += 1000 * (nums[nums[i]] % 1000);
        }
        // Modified to final value on the second iteration
        for (int i = 0; i < n; ++i) {
            nums[i] /= 1000;
        }
        return nums;
    }

    // V2


}
