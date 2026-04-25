package LeetCodeJava.Stack;

// https://leetcode.com/problems/steps-to-make-array-non-decreasing/description/

import java.util.Stack;

/**
 *  2289. Steps to Make Array Non-decreasing
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums. In one step, remove all elements nums[i] where nums[i - 1] > nums[i] for all 0 < i < nums.length.
 *
 * Return the number of steps performed until nums becomes a non-decreasing array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [5,3,4,4,7,3,6,11,8,5,11]
 * Output: 3
 * Explanation: The following are the steps performed:
 * - Step 1: [5,3,4,4,7,3,6,11,8,5,11] becomes [5,4,4,7,6,11,11]
 * - Step 2: [5,4,4,7,6,11,11] becomes [5,4,7,11,11]
 * - Step 3: [5,4,7,11,11] becomes [5,7,11,11]
 * [5,7,11,11] is a non-decreasing array. Therefore, we return 3.
 * Example 2:
 *
 * Input: nums = [4,5,7,7,13]
 * Output: 0
 * Explanation: nums is already a non-decreasing array. Therefore, we return 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 109
 *
 * Seen this question in a real interview before?
 * 1/6
 * Yes
 * No
 *
 */
public class StepsToMakeArrayNonDecreasing {

    // V0
//    public int totalSteps(int[] nums) {
//
//    }


    // V1


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/steps-to-make-array-non-decreasing/solutions/2085864/javacpython-stack-dp-explanation-poem-by-dlgo/
    public int totalSteps_2(int[] A) {
        int n = A.length, res = 0, j = -1;
        int dp[] = new int[n], stack[] = new int[n];
        for (int i = n - 1; i >= 0; --i) {
            while (j >= 0 && A[i] > A[stack[j]]) {
                dp[i] = Math.max(++dp[i], dp[stack[j--]]);
                res = Math.max(res, dp[i]);
            }
            stack[++j] = i;
        }
        return res;
    }

    // V3
    // IDEA: STACK
    // https://leetcode.com/problems/steps-to-make-array-non-decreasing/solutions/7507922/easy-simple-java-solution-by-prakhar-131-pofo/
    public int totalSteps_3(int[] nums) {
        int ans = 0;
        Stack<int[]> st = new Stack<>();
        for (int i = nums.length - 1; i >= 0; i--) {
            int count = 0;
            while (!st.isEmpty() && st.peek()[0] < nums[i]) {
                count = Math.max(count + 1, st.peek()[1]);
                st.pop();
            }
            ans = Math.max(ans, count);
            st.push(new int[] { nums[i], count });
        }

        return ans;
    }


    // V4
    // IDEA: STACK
    // https://leetcode.com/problems/steps-to-make-array-non-decreasing/solutions/7532393/find-brute-to-optimal-solution-by-anushr-jd7u/
    public int totalSteps_4(int[] nums) {
        Stack<int[]> st = new Stack<>();
        int ans = 0;
        for (int x : nums) {
            int steps = 0;
            while (!st.isEmpty() && st.peek()[0] <= x) {
                steps = Math.max(steps, st.peek()[1]);
                st.pop();
            }
            if (!st.isEmpty()) {
                steps++;
            }
            ans = Math.max(ans, steps);
            st.push(new int[] { x, steps });
        }
        return ans;
    }





}
