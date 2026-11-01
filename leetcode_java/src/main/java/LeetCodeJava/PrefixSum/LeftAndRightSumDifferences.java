package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/left-and-right-sum-differences/description/

/**
 * 2574. Left and Right Sum Differences
 * Easy
 *
 * You are given a 0-indexed integer array nums of size n.
 *
 * Define two arrays leftSum and rightSum where:
 *
 * leftSum[i] is the sum of elements to the left of the index i in the array nums. If there is no such element, leftSum[i] = 0.
 * rightSum[i] is the sum of elements to the right of the index i in the array nums. If there is no such element, rightSum[i] = 0.
 *
 * Return an integer array answer of size n where answer[i] = |leftSum[i] - rightSum[i]|.
 *
 *
 * Example 1:
 *
 * Input: nums = [10,4,8,3]
 * Output: [15,1,11,22]
 * Explanation: The array leftSum is [0,10,14,22] and the array rightSum is [15,11,3,0].
 * The array answer is [|0 - 15|,|10 - 11|,|14 - 3|,|22 - 0|] = [15,1,11,22].
 *
 * Example 2:
 *
 * Input: nums = [1]
 * Output: [0]
 * Explanation: The array leftSum is [0] and the array rightSum is [0].
 * The array answer is [|0 - 0|] = [0].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 1 <= nums[i] <= 10^5
 *
 */
public class LeftAndRightSumDifferences {

    // V0
    // IDEA: RUNNING PREFIX / SUFFIX SUM IN ONE PASS
    //       leftSum[i] + nums[i] + rightSum[i] = total, so carry two scalars:
    //       leftSum (everything before i) and rightSum (everything after i).
    //       order matters: drop nums[i] from rightSum first, record, then add it to leftSum.
    /**
     * time = O(N)
     * space = O(1) beyond the output
     */
    public int[] leftRightDifference(int[] nums) {
        int total = 0;
        for (int value : nums) {
            total += value;
        }

        int n = nums.length;
        int[] answer = new int[n];
        int leftSum = 0;
        int rightSum = total;
        for (int i = 0; i < n; i++) {
            rightSum -= nums[i];             // rightSum now excludes index i
            answer[i] = Math.abs(leftSum - rightSum);
            leftSum += nums[i];              // only now does leftSum include index i
        }
        return answer;
    }
}
