package LeetCodeJava.Greedy;

// https://leetcode.com/problems/jump-game-ii/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 45. Jump Game II
 * Solved
 * Medium
 * Topics
 * Companies
 * You are given a 0-indexed array of integers nums of length n. You are initially positioned at nums[0].
 *
 * Each element nums[i] represents the maximum length of a forward jump from index i. In other words, if you are at nums[i], you can jump to any nums[i + j] where:
 *
 * 0 <= j <= nums[i] and
 * i + j < n
 * Return the minimum number of jumps to reach nums[n - 1]. The test cases are generated such that you can reach nums[n - 1].
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,1,1,4]
 * Output: 2
 * Explanation: The minimum number of jumps to reach the last index is 2. Jump 1 step from index 0 to 1, then 3 steps to the last index.
 * Example 2:
 *
 * Input: nums = [2,3,0,1,4]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 104
 * 0 <= nums[i] <= 1000
 * It's guaranteed that you can reach nums[n - 1].
 *
 *
 */
public class JumpGame2 {

    // V0
    // IDEA : GREEDY
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/jump-game-ii.py#L45
    public int jump(int[] nums) {

        if (nums == null || nums.length <= 1){
            return 0;
        }

        /**
         *  NOTE !!!
         *
         *   need to introduce a new val: `farthest`
         *
         *   valuables we need:
         *
         *   - jumps
         *   - current_jump_end
         *   - farthest
         */
        int jumps = 0;
        int current_jump_end = 0;
        int fartheset = 0;

        for (int i = 0; i < nums.length - 1; i++){

            fartheset = Math.max(fartheset, i + nums[i]);

            if (i == current_jump_end){
                jumps += 1;
                current_jump_end = fartheset;
            }
        }

        return jumps;
    }

    // V0-2
    // IDEA: GREEDY (GPT)
    /**
     * Explanation of the Fixes:
     *
     * 1. cur: This keeps track of the furthest index we can reach with the current number of jumps.
     *
     * 2. endOfCurrentJump: This marks the end of the current jump range. Every time we reach this point (i == endOfCurrentJump), we increase the jump count (res).
     *
     * 3. Main loop:
     *   - At each index i, we update cur to be the maximum of cur and i + nums[i]. This tells us how far we can reach from the current position.
     *   - If we reach the end of the current jump range (i == endOfCurrentJump), it means we've used up one jump, so we increment res and update endOfCurrentJump to cur (i.e., the farthest point reachable with the current number of jumps).
     *
     * 4. Early exit: If cur or endOfCurrentJump reaches or exceeds the last index, we return res, the current jump count.
     *
     *
     * 5. Example Runs:
     * - For the input [2,3,1,1,4], the method will return 2, meaning two jumps are enough to reach the end ([2,3,1,1,4] → [2, 3] → [4]).
     * - For the input [1,1,1,1,1], the method will return 4, as you'll need to jump four times to reach the end.
     *
     *
     * - Time Complexity:
     *   - O(n): We iterate through the array once, where n is the length of the array.
     * - Space Complexity:
     *   - O(1): We use a constant amount of extra space.
     */
    public int jump_0_2(int[] nums) {
        // edge case
        if (nums == null || nums.length <= 1) {
            return 0;
        }


        /**
         *  NOTE !!!
         *
         *   need to introduce a new val: `endOfCurrentJump`
         *
         *   valuables we need:
         *
         *   - cur
         *   - res
         *   - endOfCurrentJump
         */
        int cur = 0; // The farthest index that can be reached with the current number of jumps.
        int res = 0; // The number of jumps.
        int endOfCurrentJump = 0; // The end of the range that can be reached with the current jump.

        for (int i = 0; i < nums.length - 1; i++) {
            // Update the farthest reachable index
            cur = Math.max(cur, i + nums[i]);

            // If we have reached the end of the current jump range, increment the jump
            // count
            if (i == endOfCurrentJump) {
                res++;
                endOfCurrentJump = cur; // Update the range for the next jump

                // If the current jump can take us to or past the last index, return the result
                if (endOfCurrentJump >= nums.length - 1) {
                    return res;
                }
            }
        }

        return res; // This will not be reached unless the input is valid.
    }

    // V0-3
    // IDEA: GREEDY (fixed by gpt)
    public int jump_0_3(int[] nums) {

        // Edge case: if the array is null or has length 1, no jump is needed
        if (nums == null || nums.length <= 1) {
            return 0;
        }


        /**
         *  NOTE !!!
         *
         *   need to introduce a new val: `curFarthest`
         *
         *   valuables we need:
         *
         *   - steps
         *   - curEnd
         *   - curFarthest
         */
        int steps = 0;
        int curEnd = 0; // The farthest index we can reach with the current jump
        int curFarthest = 0; // The farthest index we can reach with the next jump

        for (int i = 0; i < nums.length; i++) {
            // Update the farthest index we can reach from index `i`
            curFarthest = Math.max(curFarthest, i + nums[i]);

            // If we've reached the end of the current jump range
            if (i == curEnd) {
                steps++; // We need to make a new jump

                // Update the end of the current jump range
                curEnd = curFarthest;

                // If we can reach or surpass the last index, we're done
                if (curEnd >= nums.length - 1) {
                    return steps;
                }
            }
        }

        return steps;
    }

    // V1-1
    // https://neetcode.io/problems/jump-game-ii
    // IDEA: RECURSION
    public int jump_1_1(int[] nums) {
        return dfs(nums, 0);
    }

    private int dfs(int[] nums, int i) {
        if (i == nums.length - 1) {
            return 0;
        }
        if (nums[i] == 0) {
            return 1000000;
        }
        int res = 1000000;
        int end = Math.min(nums.length - 1, i + nums[i]);
        for (int j = i + 1; j <= end; j++) {
            res = Math.min(res, 1 + dfs(nums, j));
        }
        return res;
    }

    // V1-2
    // https://neetcode.io/problems/jump-game-ii
    // IDEA: DP (TOP DOWN)
    public int jump_1_2(int[] nums) {
        Map<Integer, Integer> memo = new HashMap<>();
        return dfs(nums, 0, memo);
    }

    private int dfs(int[] nums, int i, Map<Integer, Integer> memo) {
        if (memo.containsKey(i)) {
            return memo.get(i);
        }
        if (i == nums.length - 1) {
            return 0;
        }
        if (nums[i] == 0) {
            return 1000000;
        }

        int res = 1000000;
        int end = Math.min(nums.length, i + nums[i] + 1);
        for (int j = i + 1; j < end; j++) {
            res = Math.min(res, 1 + dfs(nums, j, memo));
        }
        memo.put(i, res);
        return res;
    }

    // V1-3
    // https://neetcode.io/problems/jump-game-ii
    // IDEA: DP (BOTTOM UP)
    public int jump_1_3(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1000000);
        dp[n - 1] = 0;

        for (int i = n - 2; i >= 0; i--) {
            int end = Math.min(nums.length, i + nums[i] + 1);
            for (int j = i + 1; j < end; j++) {
                dp[i] = Math.min(dp[i], 1 + dp[j]);
            }
        }
        return dp[0];
    }

    // V1-4
    // https://neetcode.io/problems/jump-game-ii
    // IDEA: BFS (GREEDY)
    public int jump_1_4(int[] nums) {
        int res = 0, l = 0, r = 0;

        while (r < nums.length - 1) {
            int farthest = 0;
            for (int i = l; i <= r; i++) {
                farthest = Math.max(farthest, i + nums[i]);
            }
            l = r + 1;
            r = farthest;
            res++;
        }
        return res;
    }


    // V2
    // IDEA : GREEDY
    // https://leetcode.com/problems/jump-game-ii/editorial/
    public int jump_2(int[] nums) {
        // The starting range of the first jump is [0, 0]
        int answer = 0, n = nums.length;
        int curEnd = 0, curFar = 0;

        for (int i = 0; i < n - 1; ++i) {
            // Update the farthest reachable index of this jump.
            curFar = Math.max(curFar, i + nums[i]);

            // If we finish the starting range of this jump,
            // Move on to the starting range of the next jump.
            if (i == curEnd) {
                answer++;
                curEnd = curFar;
            }
        }

        return answer;
    }

}
