package LeetCodeJava.Array;

// https://leetcode.com/problems/increasing-triplet-subsequence/description/

/**
 * 334. Increasing Triplet Subsequence
 * Solved
 * Medium
 * Topics
 * Companies
 * Given an integer array nums, return true if there exists a triple of indices (i, j, k) such that i < j < k and nums[i] < nums[j] < nums[k]. If no such indices exists, return false.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [1,2,3,4,5]
 * Output: true
 * Explanation: Any triplet where i < j < k is valid.
 * Example 2:
 * <p>
 * Input: nums = [5,4,3,2,1]
 * Output: false
 * Explanation: No triplet exists.
 * Example 3:
 * <p>
 * Input: nums = [2,1,5,0,4,6]
 * Output: true
 * Explanation: The triplet (3, 4, 5) is valid because nums[3] == 0 < nums[4] == 4 < nums[5] == 6.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= nums.length <= 5 * 105
 * -231 <= nums[i] <= 231 - 1
 * <p>
 * <p>
 * Follow up: Could you implement a solution that runs in O(n) time complexity and O(1) space complexity?
 */
public class IncreasingTripletSubsequence {

    // V0
    // TODO : implement
//    public boolean increasingTriplet(int[] nums) {
//
//    }

    // V1
    // IDEA : LOOP (fixed by gpt)
    public boolean increasingTriplet_1(int[] nums) {
        if (nums.length < 3) {
            return false;
        }

        int first = Integer.MAX_VALUE;
        int second = Integer.MAX_VALUE;

        for (int num : nums) {
            if (num <= first) {
                // update first if num is smaller than or equal to first
                first = num;
            } else if (num <= second) {
                // update second if num is smaller than or equal to second
                second = num;
            } else {
                // if we find a number greater than both first and second, return true
                return true;
            }
        }

        return false;
    }

    // V2
    // (same as V1)
    // https://leetcode.com/problems/increasing-triplet-subsequence/solutions/4462160/java-c-o-n-simple-easy-solution-step-by-step-explanation/

    // V3
    // IDEA : 2 POINTERS (out of memory error)
//    public static boolean increasingTriplet_tp_3(int[] nums) {
//        if (nums == null || nums.length < 3) {
//            return false;
//        }
//
//        int len = nums.length;
//        int[] leftMin = new int[len];
//        leftMin[0] = nums[0];
//        for (int i = 1; i < len; i++) {
//            leftMin[i] = Math.min(leftMin[i - 1], nums[i]);
//        }
//
//        int[] rightMax = new int[len];
//        rightMax[len - 1] = nums[len - 1];
//        for (int i = len - 2; i >= 0; i--) {
//            rightMax[i] = Math.max(rightMax[i + 1], nums[i]);
//        }
//
//        for (int i = 0; i < len; i++) {
//            if (nums[i] > leftMin[i] && nums[i] < rightMax[i]) {
//                return true;
//            }
//        }
//
//        return false;
//    }

}
