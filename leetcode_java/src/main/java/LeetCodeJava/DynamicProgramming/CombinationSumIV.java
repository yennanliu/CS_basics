package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/combination-sum-iv/description/

import java.util.*;

/**
 * 377. Combination Sum IV
 * Medium
 * Topics
 * Companies
 * Given an array of distinct integers nums and a target integer target, return the number of possible combinations that add up to target.
 *
 * The test cases are generated so that the answer can fit in a 32-bit integer.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3], target = 4
 * Output: 7
 * Explanation:
 * The possible combination ways are:
 * (1, 1, 1, 1)
 * (1, 1, 2)
 * (1, 2, 1)
 * (1, 3)
 * (2, 1, 1)
 * (2, 2)
 * (3, 1)
 * Note that different sequences are counted as different combinations.
 * Example 2:
 *
 * Input: nums = [9], target = 3
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 200
 * 1 <= nums[i] <= 1000
 * All the elements of nums are unique.
 * 1 <= target <= 1000
 *
 *
 * Follow up: What if negative numbers are allowed in the given array? How does it change the problem? What limitation we need to add to the question to allow negative numbers?
 *
 *
 *
 */
public class CombinationSumIV {

    // V0
//    public int combinationSum4(int[] nums, int target) {
//
//    }

    // V0-0-1
    // IDEA: DP (gemini)
    /**  IDEA:
     *
     * ### Logic & Idea Explanation: Combination Sum IV (LC 377)
     *
     * To solve this problem effectively,
     * we must distinguish between **Combinations** and **Permutations**
     * and understand how to build the solution using Dynamic Programming.
     *
     * ---
     *
     * ### 1. The Core Concept: Permutations Matter
     *
     * In this problem, the order of numbers matters.
     * For example, if the target is `4` and your numbers are `[1, 2]`:
     *
     * *
     * *
     * *
     * All three are considered **distinct** ways
     * to reach the target. Because different sequences of
     * the same numbers are counted separately, this is a **Permutation**
     * problem rather than a standard Combination problem.
     *
     * ---
     *
     * ### 2. The DP State Definition
     *
     * We define our subproblems based on the target value:
     *
     * * ****: The total number of ways (combinations)
     * to reach the sum  using the numbers provided in the input array.
     *
     *
     * * **Target**: Our final goal is to find .
     *
     * ---
     *
     * ### 3. The Recurrence Relation (The "How")
     *
     * To find the number of ways to reach a sum ,
     * we look at the last number added to the sequence.
     * If we used `num` as the last step to reach ,
     * then the previous sum must have been .
     *
     *
     * Therefore, the total ways to reach  is the **sum**
     * of ways to reach all possible previous states:
     *
     *
     * **Example Walkthrough:**
     * If `nums = [1, 2, 3]` and we want to find ways to reach :
     *
     * 1. If the last number was **1**, we need the ways to reach  ().
     * 2. If the last number was **2**, we need the ways to reach  ().
     * 3. If the last number was **3**, we need the ways to reach  ().
     * **Total:** .
     *
     * ---
     *
     * ### 4. The Base Case
     *
     * * ****: This is the most critical part of the initialization.
     * * **Mathematically:** There is exactly one way to reach a sum of 0—by picking nothing.
     * * **Practically:** If your target is 3 and you have a 3 in your array, you need , which is . To count that 3 as "one valid way,"  must be 1.
     *
     *
     *
     * ---
     *
     * ### 5. Why the Loop Order Matters
     *
     * In DP, the order of your nested loops determines if you are counting **Combinations** or **Permutations**:
     *
     * | Loop Structure | Result Type | Logic |
     * | --- | --- | --- |
     * | **Outer: Target, Inner: Nums** | **Permutations** | For every sum, we try every number. This allows  and  to be recorded separately. |
     * | **Outer: Nums, Inner: Target** | **Combinations** | We pick a number and see all sums it can form, then move to the next number. This forces a specific order (e.g., all 1s must come before all 2s). |
     *
     * ---
     *
     * ### 6. Complexity Analysis
     *
     * * **Time Complexity:** , where  is the target value and  is the number of elements in `nums`. We fill a  array of size , and for each cell, we iterate through  numbers.
     * * **Space Complexity:**  to store the `dp` array.
     *
     *
     */
    public int combinationSum4_0_0_1(int[] nums, int target) {
        /**  NOTE !!!
         *
         *  dp[x] : # of ways to sum up equals `x`
         *
         *   ->
         *      dp[0]:   number of ways to reach sum 0
         *      dp[1]:   number of ways to reach sum 1
         *      ...
         *      dp[k]:   number of ways to reach sum k
         *
         */
        // dp[i] will store the number of ways to reach sum i
        int[] dp = new int[target + 1];

        // Base case: There is exactly one way to reach sum 0 (by picking NOTHING)
        dp[0] = 1;

        // Iterate through all possible sums from 1 to target
        for (int i = 1; i <= target; i++) {
            // Check every number in the input array
            for (int num : nums) {
                // If the current sum 'i' is greater than or equal to 'num'
                if (i >= num) {
                    // Add the number of ways we could reach the remainder
                    dp[i] += dp[i - num];
                }
            }
        }

        return dp[target];
    }

    // V0-0-2
    // IDEA: DP (gpt)
    /**   Example Walkthrough:
     *
     *
     * ex 1)
     * nums = [1, 2, 3], target = 4
     *
     * sum	dp[sum]	Ways
     * dp[0]	1	{}
     * dp[1]	dp[1-1] = dp[0] = 1	(1)
     * dp[2]	dp[2-1] + dp[2-2] = dp[1] + dp[0] = 2	(1,1), (2)
     * dp[3]	dp[3-1] + dp[3-2] + dp[3-3] = dp[2] + dp[1] + dp[0] = 4	(1,1,1), (1,2), (2,1), (3)
     * dp[4]	dp[4-1] + dp[4-2] + dp[4-3] = dp[3] + dp[2] + dp[1] = 7
     *
     * -------
     *
     * ```markdown
     * ## More Example Walkthroughs (DP Table Style)
     *
     * Below are **step-by-step DP walkthroughs** to
     * build strong intuition for **LC 377 — Combination Sum IV**.
     *
     * ---
     *
     * ### Example 1:
     * **nums = [1, 2], target = 3**
     *
     * We define:
     * ```
     *
     * dp[i] = number of ordered combinations that sum to i
     *
     * ```
     *
     * Initialize:
     * ```
     *
     * dp[0] = 1   // one way to make sum 0 (empty)
     * dp[1..3] = 0
     *
     * ```
     *
     * Now fill the table:
     *
     * #### sum = 1
     * - Use `1`: dp[1] += dp[0] = 1
     * - Use `2`: skip (2 > 1)
     *
     * ```
     *
     * dp = [1, 1, 0, 0]
     *
     * ```
     *
     * Ways:
     * - (1)
     *
     * ---
     *
     * #### sum = 2
     * - Use `1`: dp[2] += dp[1] = 1  → (1,1)
     * - Use `2`: dp[2] += dp[0] = 1  → (2)
     *
     * ```
     *
     * dp = [1, 1, 2, 0]
     *
     * ```
     *
     * Ways:
     * - (1,1)
     * - (2)
     *
     * ---
     *
     * #### sum = 3
     * - Use `1`: dp[3] += dp[2] = 2  → (1,1,1), (1,2)
     * - Use `2`: dp[3] += dp[1] = 1  → (2,1)
     *
     * ```
     *
     * dp = [1, 1, 2, 3]
     *
     * ```
     *
     * ✅ **Answer = 3**
     *
     * ---
     *
     * ### Example 2:
     * **nums = [1, 3, 5], target = 4**
     *
     * Initialize:
     * ```
     *
     * dp = [1, 0, 0, 0, 0]
     *
     * ```
     *
     * #### sum = 1
     * - Use `1`: dp[1] += dp[0] = 1
     *
     * ```
     *
     * dp = [1, 1, 0, 0, 0]
     *
     * ```
     *
     * Ways:
     * - (1)
     *
     * ---
     *
     * #### sum = 2
     * - Use `1`: dp[2] += dp[1] = 1
     *
     * ```
     *
     * dp = [1, 1, 1, 0, 0]
     *
     * ```
     *
     * Ways:
     * - (1,1)
     *
     * ---
     *
     * #### sum = 3
     * - Use `1`: dp[3] += dp[2] = 1 → (1,1,1)
     * - Use `3`: dp[3] += dp[0] = 1 → (3)
     *
     * ```
     *
     * dp = [1, 1, 1, 2, 0]
     *
     * ```
     *
     * Ways:
     * - (1,1,1)
     * - (3)
     *
     * ---
     *
     * #### sum = 4
     * - Use `1`: dp[4] += dp[3] = 2 → (1,1,1,1), (1,3)
     * - Use `3`: dp[4] += dp[1] = 1 → (3,1)
     * - Use `5`: skip
     *
     * ```
     *
     * dp = [1, 1, 1, 2, 3]
     *
     * ```
     *
     * ✅ **Answer = 3**
     *
     * ---
     *
     * ### Example 3:
     * **nums = [2, 3], target = 7**
     *
     * Initialize:
     * ```
     *
     * dp = [1, 0, 0, 0, 0, 0, 0, 0]
     *
     * ```
     *
     * | sum | dp update | dp value |
     * |----|-----------|----------|
     * | 2 | dp[2] += dp[0] | 1 |
     * | 3 | dp[3] += dp[0] | 1 |
     * | 4 | dp[4] += dp[2] | 1 |
     * | 5 | dp[5] += dp[3] | 1 |
     * | 6 | dp[6] += dp[4] + dp[3] | 2 |
     * | 7 | dp[7] += dp[5] + dp[4] | 2 |
     *
     * Final:
     * ```
     *
     * dp = [1,0,1,1,1,1,2,2]
     *
     * ```
     *
     * Ways for sum = 7:
     * - (2,2,3)
     * - (2,3,2)
     *
     * ✅ **Answer = 2**
     *
     * ---
     *
     * ### Example 4 (Important Edge Case):
     * **nums = [2], target = 3**
     *
     * Initialize:
     * ```
     *
     * dp = [1,0,0,0]
     *
     * ```
     *
     * - sum = 1 → no valid nums
     * - sum = 2 → dp[2] = dp[0] = 1
     * - sum = 3 → dp[3] = dp[1] = 0
     *
     * ```
     *
     * dp = [1,0,1,0]
     *
     * ```
     *
     * ✅ **Answer = 0**
     *
     * ---
     *
     * ## Key Pattern to Remember
     *
     * At each `sum`:
     * ```
     *
     * dp[sum] = Σ dp[sum - num]  for all num ≤ sum
     *
     * ```
     *
     * - **Outer loop on sum** → order matters
     * - **Inner loop on nums** → permutations counted
     * - `dp[0] = 1` seeds all combinations
     *
     * ---
     *
     * ### Mental Model (Interview-Friendly)
     *
     * > “To build `sum`, try placing each number **last**
     * > and count how many ways we could build the remaining part.”
     *
     * ---
     *
     * If you want next:
     * - visualization with recursion tree
     * - comparison vs Combination Sum I / II
     * - overflow discussion & constraints
     *
     */
    public int combinationSum4_0_0_2(int[] nums, int target) {
        long[] dp = new long[target + 1];
        dp[0] = 1; // one way to make sum 0

        for (int sum = 1; sum <= target; sum++) {
            for (int num : nums) {
                if (sum - num >= 0) {
                    dp[sum] += dp[sum - num];
                }
            }
        }

        return (int) dp[target];
    }

    // V0-0-3
    // IDEA: Memoized Recursion (gpt)
    public int combinationSum4_0_0_3(int[] nums, int target) {
        int[] memo = new int[target + 1];
        Arrays.fill(memo, -1);
        memo[0] = 1;
        return dfs(nums, target, memo);
    }

    private int dfs(int[] nums, int target, int[] memo) {
        if (memo[target] != -1)
            return memo[target];

        int count = 0;
        for (int num : nums) {
            if (target - num >= 0) {
                count += dfs(nums, target - num, memo);
            }
        }
        memo[target] = count;
        return count;
    }


    // V0-1
    // IDEA: BACKTRACK (gpt) (TLE)
    int count = 0;

    public int combinationSum4_0_1(int[] nums, int target) {
        backtrack(nums, target);
        return count;
    }

    private void backtrack(int[] nums, int remain) {
        if (remain == 0) {
            count++;
            return;
        }

        for (int num : nums) {
            if (remain - num >= 0) {
                backtrack(nums, remain - num);
            }
        }
    }

    // V0-2
    // IDEA : Top-Down DP (with Memoization) (gpt)
    public int combinationSum4_0_2(int[] nums, int target) {
        Integer[] memo = new Integer[target + 1];
        return dp(nums, target, memo);
    }

    private int dp(int[] nums, int remain, Integer[] memo) {
        if (remain == 0)
            return 1;
        if (remain < 0)
            return 0;
        if (memo[remain] != null)
            return memo[remain];

        int count = 0;
        for (int num : nums) {
            count += dp(nums, remain - num, memo);
        }

        memo[remain] = count;
        return count;
    }

    // V0-3
    // IDEA:  Bottom-Up DP Version (Tabulation) (gpt)
    public int combinationSum4_0_3(int[] nums, int target) {
        int[] dp = new int[target + 1];
        dp[0] = 1; // Base case

        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i - num >= 0) {
                    dp[i] += dp[i - num];
                }
            }
        }

        return dp[target];
    }

    // V0-4
    // IDEA: BACKTRACK (gpt) (TLE)
    int cnt = 0;

    public int combinationSum4_0_4(int[] nums, int target) {
        backtrack_0_4(nums, target, new ArrayList<>());
        return cnt;
    }

    public void backtrack_0_4(int[] nums, int target, List<Integer> cur) {
        if (target == 0) {
            cnt++;
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= target) {
                cur.add(nums[i]);
                backtrack_0_4(nums, target - nums[i], cur);
                cur.remove(cur.size() - 1); // backtrack
            }
        }
    }

    // V1-1
    // https://www.youtube.com/watch?v=dw2nMCxG0ik

    // V1-2
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0377-combination-sum-iv.java
    // IDEA:
    /*       Tabulation Method
      --------------------------
      T = Target, N = nums.length
      Time complexity: O(T⋅N)
      Space complexity: O(T)
   */
    public int combinationSum4_1_2(int[] nums, int target) {
        int[] dp = new int[target+1];
        dp[0] = 1;

        for(int currSum = 1; currSum < dp.length; currSum++){
            for(int no : nums){
                if(currSum - no >= 0){
                    dp[currSum] += dp[currSum - no];
                }
            }
        }
        return dp[target];
    }


    // V1-3
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0377-combination-sum-iv.java
    // IDEA:

    /*        Memoization Method
          --------------------------
          T = Target, N = nums.length
          Time complexity: O(T⋅N)
          Space complexity: O(T) + Recursive Stack
    */
    public int combinationSum4_1_3(int[] nums, int target) {
        HashMap<Integer, Integer> memo = new HashMap<>();
        return helper(nums, target, memo);
    }

    private int helper(int[] nums, int t, HashMap<Integer, Integer> memo){
        if(t == 0)
            return 1;
        if(t < 0)
            return 0;
        if(memo.containsKey(t))
            return memo.get(t);

        int count = 0;
        for(int no : nums){
            count += helper(nums, t - no, memo); // ????
        }
        memo.put(t, count);
        return count;
    }


    // V1-4
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0377-combination-sum-iv.java
    // IDEA:
    /**
     Simple brute force, count num of combinations
     using Map ds
     Time complexity: O(n)
     Space complexity: O(n)
     */
    public int combinationSum4_1_4(int[] nums, int target) {
        Map<Integer, Integer> cache = new HashMap<>();

        cache.put(0, 1);

        for (int i = 1; i < target + 1; i++) {
            cache.put(i, 0);
            for (int n : nums) {
                int temp = cache.containsKey(i - n) ? cache.get(i - n) : 0;
                cache.put(i, cache.get(i) + temp);
            }
        }

        return cache.get(target);
    }


    // V2
    // https://leetcode.com/problems/combination-sum-iv/solutions/6726200/optimized-solution-for-combination-sum-i-vv5u/
    public int combinationSum4_2(int[] nums, int target) {
        return dp(nums, target, new HashMap<>());
    }
    private int dp(int[] nums , int target, Map<Integer, Integer> memo ) {
        if(target < 0) {
            return 0;
        }
        if(target == 0) {
            return 1;
        }
        if(memo.containsKey(target)) {
            return memo.get(target);
        }
        int res = 0;
        for(int i = 0; i<nums.length ; i++) {
            res += dp(nums, target - nums[i], memo);
        }
        memo.put(target, res);
        return res;
    }

    // V3
    // https://leetcode.com/problems/combination-sum-iv/solutions/4020218/9822-dynamic-programming-recursion-with-bw1jh/



}
