package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-middle-index-in-array/description/
/**
 * 1991. Find the Middle Index in Array
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a 0-indexed integer array nums, find the leftmost middleIndex (i.e., the smallest amongst all the possible ones).
 *
 * A middleIndex is an index where nums[0] + nums[1] + ... + nums[middleIndex-1] == nums[middleIndex+1] + nums[middleIndex+2] + ... + nums[nums.length-1].
 *
 * If middleIndex == 0, the left side sum is considered to be 0. Similarly, if middleIndex == nums.length - 1, the right side sum is considered to be 0.
 *
 * Return the leftmost middleIndex that satisfies the condition, or -1 if there is no such index.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,-1,8,4]
 * Output: 3
 * Explanation: The sum of the numbers before index 3 is: 2 + 3 + -1 = 4
 * The sum of the numbers after index 3 is: 4 = 4
 * Example 2:
 *
 * Input: nums = [1,-1,4]
 * Output: 2
 * Explanation: The sum of the numbers before index 2 is: 1 + -1 = 0
 * The sum of the numbers after index 2 is: 0
 * Example 3:
 *
 * Input: nums = [2,5]
 * Output: -1
 * Explanation: There is no valid middleIndex.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * -1000 <= nums[i] <= 1000
 *
 *
 * Note: This question is the same as 724: https://leetcode.com/problems/find-pivot-index/
 *
 *
 */
public class FindTheMiddleIndexInArray {

    // V0
    // IDEA: PREFIX SUM
    public int findMiddleIndex(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return -1; // ??
        }
        if (nums.length == 1) {
            return 0;
        }

        int sum = 0;
        for (int x : nums) {
            sum += x;
        }
        int prefixSum = 0;
        for (int i = 0; i < nums.length; i++) {
            int x = nums[i];
            prefixSum += x;
            if ((prefixSum - x) * 2 + x == sum) {
                return i;
            }
        }

        // if the `middleIndex` is in the last element
        if (sum - nums[nums.length - 1] == 0) {
            return nums.length - 1;
        }

        return -1;
    }

    // V0-1
    // IDEA: LEFT, RIGHT SUM (gemini)
    public int findMiddleIndex_0_1(int[] nums) {
        if (nums == null || nums.length == 0)
            return -1;

        int totalSum = 0;
        for (int x : nums) {
            totalSum += x;
        }

        int leftSum = 0;
        for (int i = 0; i < nums.length; i++) {
            // Right sum is everything minus the left side and the current element
            int rightSum = totalSum - leftSum - nums[i];

            if (leftSum == rightSum) {
                return i;
            }

            // Update leftSum for the NEXT index
            leftSum += nums[i];
        }

        return -1;
    }


    // V1
    // https://leetcode.com/problems/find-the-middle-index-in-array/solutions/1499953/java-easy-to-understand-prefix-and-suffi-js0s/
    public int findMiddleIndex_1(int[] nums) {
        if (nums == null || nums.length == 0)
            return -1;

        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        int prefix = 0, suffix = 0;
        for (int i = 0; i < nums.length; i++) {
            suffix = sum - prefix;
            prefix += nums[i];
            if (prefix == suffix)
                return i;
        }
        return -1;
    }


    // V2
    // https://leetcode.com/problems/find-the-middle-index-in-array/solutions/3247827/simple-solution-0-ms-beats-100-two-point-h7vf/
    public int findMiddleIndex_2(int[] nums) {
        int sum = 0;
        int temp = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
        }

        for (int j = 0; j < nums.length; j++) {
            sum -= nums[j];

            if (sum == temp)
                return j;
            temp += nums[j];
        }
        return -1;
    }
    


}
