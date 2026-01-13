package LeetCodeJava.Greedy;

// https://leetcode.com/problems/wiggle-subsequence/description/
/**
 * 376. Wiggle Subsequence
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * A wiggle sequence is a sequence where the differences between successive numbers strictly alternate between positive and negative. The first difference (if one exists) may be either positive or negative. A sequence with one element and a sequence with two non-equal elements are trivially wiggle sequences.
 *
 * For example, [1, 7, 4, 9, 2, 5] is a wiggle sequence because the differences (6, -3, 5, -7, 3) alternate between positive and negative.
 * In contrast, [1, 4, 7, 2, 5] and [1, 7, 4, 5, 5] are not wiggle sequences. The first is not because its first two differences are positive, and the second is not because its last difference is zero.
 * A subsequence is obtained by deleting some elements (possibly zero) from the original sequence, leaving the remaining elements in their original order.
 *
 * Given an integer array nums, return the length of the longest wiggle subsequence of nums.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,7,4,9,2,5]
 * Output: 6
 * Explanation: The entire sequence is a wiggle sequence with differences (6, -3, 5, -7, 3).
 * Example 2:
 *
 * Input: nums = [1,17,5,10,13,15,10,5,16,8]
 * Output: 7
 * Explanation: There are several subsequences that achieve this length.
 * One is [1, 17, 10, 13, 10, 16, 8] with differences (16, -7, 3, -3, 6, -8).
 * Example 3:
 *
 * Input: nums = [1,2,3,4,5,6,7,8,9]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 *
 *
 * Follow up: Could you solve this in O(n) time?
 */
public class WiggleSubsequence {

    // V0
//    public int wiggleMaxLength(int[] nums) {
//
//    }

    // V0-1
    // IDEA: DP (GEMINI)
    public int wiggleMaxLength_0_1(int[] nums) {
        int n = nums.length;
        if (n < 2)
            return n;

        // up[i] is the max length ending with an ascending step
        int[] up = new int[n];
        // down[i] is the max length ending with a descending step
        int[] down = new int[n];

        // Base case: a single element is a wiggle sequence of length 1
        up[0] = 1;
        down[0] = 1;

        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                // We can extend a "down" sequence with an "up" step
                up[i] = down[i - 1] + 1;
                down[i] = down[i - 1];
            } else if (nums[i] < nums[i - 1]) {
                // We can extend an "up" sequence with a "down" step
                down[i] = up[i - 1] + 1;
                up[i] = up[i - 1];
            } else {
                // No change: sequence length stays the same as the previous step
                up[i] = up[i - 1];
                down[i] = down[i - 1];
            }
        }

        return Math.max(up[n - 1], down[n - 1]);
    }


    // V0-2
    // IDEA: space optimized DP (GEMINI)
    public int wiggleMaxLength_0_2(int[] nums) {
        if (nums.length < 2)
            return nums.length;

        int up = 1;
        int down = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] > nums[i - 1]) {
                up = down + 1;
            } else if (nums[i] < nums[i - 1]) {
                down = up + 1;
            }
        }

        return Math.max(up, down);
    }

    // V0-3
    // IDEA: DP (GPT)
    public int wiggleMaxLength_0_3(int[] nums) {
        if (nums == null || nums.length == 0)
            return 0;
        if (nums.length == 1)
            return 1;

        int n = nums.length;

        // dp[i][0] -> ending at i, last diff is down
        // dp[i][1] -> ending at i, last diff is up
        int[][] dp = new int[n][2];

        // base case
        dp[0][0] = 1;
        dp[0][1] = 1;

        for (int i = 1; i < n; i++) {
            if (nums[i] > nums[i - 1]) {
                dp[i][1] = dp[i - 1][0] + 1;
                dp[i][0] = dp[i - 1][0];
            } else if (nums[i] < nums[i - 1]) {
                dp[i][0] = dp[i - 1][1] + 1;
                dp[i][1] = dp[i - 1][1];
            } else {
                dp[i][0] = dp[i - 1][0];
                dp[i][1] = dp[i - 1][1];
            }
        }

        return Math.max(dp[n - 1][0], dp[n - 1][1]);
    }


    // V1
    // IDEA: DP
    // https://leetcode.ca/2016-12-10-376-Wiggle-Subsequence/
    public int wiggleMaxLength_1(int[] nums) {
        int up = 1, down = 1;
        for (int i = 1; i < nums.length; ++i) {
            if (nums[i] > nums[i - 1]) {
                up = Math.max(up, down + 1);
            } else if (nums[i] < nums[i - 1]) {
                down = Math.max(down, up + 1);
            }
        }
        return Math.max(up, down);
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/wiggle-subsequence/solutions/84849/very-simple-java-solution-with-detail-ex-10he/
    public int wiggleMaxLength_2(int[] nums) {
        if (nums.length == 0 || nums.length == 1) {
            return nums.length;
        }
        int k = 0;
        while (k < nums.length - 1 && nums[k] == nums[k + 1]) { //Skips all the same numbers from series beginning eg 5, 5, 5, 1
            k++;
        }
        if (k == nums.length - 1) {
            return 1;
        }
        int result = 2; // This will track the result of result array
        boolean smallReq = nums[k] < nums[k + 1]; //To check series starting pattern
        for (int i = k + 1; i < nums.length - 1; i++) {
            if (smallReq && nums[i + 1] < nums[i]) {
                nums[result] = nums[i + 1];
                result++;
                smallReq = !smallReq; //Toggle the requirement from small to big number
            } else {
                if (!smallReq && nums[i + 1] > nums[i]) {
                    nums[result] = nums[i + 1];
                    result++;
                    smallReq = !smallReq; //Toggle the requirement from big to small number
                }
            }
        }
        return result;
    }


    // V3
    // IDEA: DP
    // https://leetcode.com/problems/wiggle-subsequence/solutions/85001/java-on-solution-quite-simple-idea-with-e5bwa/
    public int wiggleMaxLength_3(int[] nums) {
        if (nums.length <= 1)
            return nums.length;

        int count = nums.length;
        Boolean positive = null;

        for (int i = 0; i < nums.length - 1; i++) {
            int temp = nums[i + 1] - nums[i];
            if (temp == 0)
                count--;
            else if (positive == null)
                positive = temp > 0;
            else if ((temp > 0 && positive) || (temp < 0 && !positive))
                count--;
            else
                positive = !positive;
        }
        return count;
    }




}
