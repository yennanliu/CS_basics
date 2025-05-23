package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/combination-sum-iv/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
