package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/delete-and-earn/description/

import java.util.Arrays;
import java.util.HashMap;

/**
 *  740. Delete and Earn
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer array nums. You want to maximize the number of points you get by performing the following operation any number of times:
 *
 * Pick any nums[i] and delete it to earn nums[i] points. Afterwards, you must delete every element equal to nums[i] - 1 and every element equal to nums[i] + 1.
 * Return the maximum number of points you can earn by applying the above operation some number of times.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [3,4,2]
 * Output: 6
 * Explanation: You can perform the following operations:
 * - Delete 4 to earn 4 points. Consequently, 3 is also deleted. nums = [2].
 * - Delete 2 to earn 2 points. nums = [].
 * You earn a total of 6 points.
 * Example 2:
 *
 * Input: nums = [2,2,3,3,3,4]
 * Output: 9
 * Explanation: You can perform the following operations:
 * - Delete a 3 to earn 3 points. All 2's and 4's are also deleted. nums = [3,3].
 * - Delete a 3 again to earn 3 points. nums = [3].
 * - Delete a 3 once more to earn 3 points. nums = [].
 * You earn a total of 9 points.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2 * 104
 * 1 <= nums[i] <= 104
 *
 */
public class DeleteAndEarn {

    // V0
//    public int deleteAndEarn(int[] nums) {
//
//    }


    // V0-1
    // IDEA: DP (GPT)
    /**  CORE
     *
     * 0. transform this problem to `LC 198 HOUSE RUBBER`
     *
     * 1. (Transform to House Robber)
     *   ```
     *   freq[v] = total points by taking all occurrences of value v
     *         = v * count(v)
     *   ```
     *
     *  3. DP def:
     *
     *   - dp[i] =
     *       MAX points we can earn considering values up to i
     *
     *  4. DP eq:
     *
     *    ```
     *    dp[i] = max(dp[i-1], dp[i-2] + points[i])
     *   ```
     *
     */
    /** IDEA:
     *
     * Here’s a clear, optimal Java solution for **LeetCode 740 — Delete and Earn**.
     *
     * 👉 Problem: [https://leetcode.com/problems/delete-and-earn/description/](https://leetcode.com/problems/delete-and-earn/description/)
     *
     * ---
     *
     * ## 🧠 Problem Summary
     *
     * You are given an integer array `nums`.
     * When you choose a value `x` from `nums`, you:
     *
     * * Earn `x` points
     * * Must delete all occurrences of `x - 1` and `x + 1`
     *
     * Goal:
     * Return the **maximum points** you can earn.
     *
     * ---
     *
     * ## 🔑 Key Insight (Transform to House Robber)
     *
     * Instead of thinking about deleting individual elements, we can group by **value** first.
     *
     * Let:
     *
     * ```
     * freq[v] = total points by taking all occurrences of value v
     *         = v * count(v)
     * ```
     *
     * Now the restriction:
     *
     * > Taking value `v` prevents taking `(v-1)` and `(v+1)`
     *
     * This becomes exactly the **House Robber** constraint:
     *
     * > You can’t take adjacent indices.
     *
     * So we transform into:
     *
     * * An array `points[v]`
     * * Earn `points[v]` if you “rob” house `v`
     * * Cannot rob adjacent values
     *
     * ---
     *
     * ## 🧠 Approach (DP)
     *
     * ### Step 1 — Build the frequency/points array
     *
     * ```
     * points[v] = v * (frequency of v in nums)
     * ```
     *
     * ### Step 2 — Solve “House Robber” on `points`
     *
     * Let:
     *
     * ```
     * dp[i] = max points we can earn considering values up to i
     * ```
     *
     * Transition:
     *
     * ```
     * dp[i] = max(dp[i-1], dp[i-2] + points[i])
     * ```
     *
     * ## 🧠 Explanation
     *
     * ### Why this works
     *
     * * When you take all of value `v`, you *must delete* `v-1` and `v+1`.
     * * By grouping all same values upfront into `points[v]`, this becomes a non-adjacent choice problem — just like **House Robber**.
     *
     * ---
     *
     * ## 🧪 Example
     *
     * ```
     * nums = [3, 4, 2]
     * points[2] = 2
     * points[3] = 3
     * points[4] = 4
     *
     * dp array:
     * i = 0 → dp[0] = 0
     * i = 1 → dp[1] = max(0, points[1]) = 0
     * i = 2 → dp[2] = max(0, dp[0] + 2) = 2
     * i = 3 → dp[3] = max(2, dp[1] + 3) = 3
     * i = 4 → dp[4] = max(3, dp[2] + 4) = 6
     *
     * Return = 6
     * ```
     *
     * Explanation: choose 2 and 4 → earn 2 + 4 = 6
     *
     * ---
     *
     * ## ⏱ Time & Space
     *
     * | Metric | Value          |
     * | ------ | -------------- |
     * | Time   | O(n + max_val) |
     * | Space  | O(max_val)     |
     *
     * This is efficient within constraints.
     *
     * ---
     *
     * ## 🧠 Alternate Optimization
     *
     * You can also optimize `dp[]` to **constant space**:
     *
     * ```java
     * int twoBack = points[0];
     * int oneBack = Math.max(points[0], points[1]);
     *
     * for (int i = 2; i <= maxVal; i++) {
     *     int curr = Math.max(oneBack, twoBack + points[i]);
     *     twoBack = oneBack;
     *     oneBack = curr;
     * }
     *
     * return oneBack;
     * ```
     *
     * ---
     *
     * ## 🎯 Summary
     *
     * 1. Convert the problem to a **frequency value problem**
     * 2. Build `points[v] = total points for value v`
     * 3. Run **House Robber DP**
     *
     */
    /**
     *
     * Question: why `points[n] += n;`, but NOT record the freq ? (longer version)
     *
     * Answer:
     *
     *
     * Great question — this gets to the **core modeling insight** of LC 740.
     *
     * Let’s break it down **cleanly and precisely**, step by step.
     *
     * ---
     *
     * ## Short answer
     *
     * We **add `n` directly** (instead of just counting frequency) because:
     *
     * > **Each occurrence of value `n` gives `n` points when chosen.**
     *
     * So:
     *
     * ```
     * total points for value n = n × frequency(n)
     * ```
     *
     * By doing:
     *
     * ```java
     * points[n] += n;
     * ```
     *
     * we are **implicitly multiplying by frequency**.
     *
     * ---
     *
     * ## Step 1: What happens when you “delete and earn”?
     *
     * When you pick a value `x`:
     *
     * * You earn **`x` points per occurrence**
     * * You can (and should) take **all occurrences** of `x`
     * * You lose the ability to take `x-1` and `x+1`
     *
     * So if `x` appears `k` times:
     *
     * ```
     * earn = x + x + x + ... (k times) = x × k
     * ```
     *
     * ---
     *
     * ## Step 2: Why counting frequency alone is not enough
     *
     * If you only did:
     *
     * ```java
     * freq[n]++
     * ```
     *
     * You would still need to compute:
     *
     * ```java
     * points[n] = freq[n] * n
     * ```
     *
     * So instead of storing **two arrays**, we collapse them into one:
     *
     * ```java
     * points[n] += n;
     * ```
     *
     * This directly accumulates:
     *
     * ```
     * points[n] = n × frequency(n)
     * ```
     *
     * ---
     *
     * ## Step 3: Example to make it concrete
     *
     * ### Input
     *
     * ```text
     * nums = [2, 2, 3, 3, 3, 4]
     * ```
     *
     * ### If we counted frequency
     *
     * ```text
     * freq[2] = 2
     * freq[3] = 3
     * freq[4] = 1
     * ```
     *
     * Points would be:
     *
     * ```text
     * points[2] = 2 × 2 = 4
     * points[3] = 3 × 3 = 9
     * points[4] = 4 × 1 = 4
     * ```
     *
     * ### With the code:
     *
     * ```java
     * for (int n : nums) {
     *     points[n] += n;
     * }
     * ```
     *
     * We directly get:
     *
     * ```text
     * points = [0, 0, 4, 9, 4]
     * ```
     *
     * Same result, fewer steps.
     *
     * ---
     *
     * ## Step 4: Why this grouping is essential for DP
     *
     * Once grouped, the problem becomes:
     *
     * > Choose values `v` to maximize `points[v]`,
     * > but you **cannot choose adjacent values**.
     *
     * That is exactly the **House Robber** recurrence:
     *
     * ```text
     * dp[v] = max(dp[v-1], dp[v-2] + points[v])
     * ```
     *
     * This only works if `points[v]` already represents the **total gain** from choosing `v`.
     *
     * ---
     *
     * ## Step 5: Important insight (interview-level)
     *
     * > **Once you choose a value `v`, it is never optimal to take only some of its occurrences.**
     *
     * Why?
     *
     * * All `v`s give the same reward
     * * All `v`s delete the same neighbors (`v-1`, `v+1`)
     * * There is no downside to taking them all
     *
     * So grouping by value and summing their contributions is **lossless**.
     *
     * ---
     *
     * ## TL;DR
     *
     * * Each `n` gives `n` points → add `n`, not `1`
     * * `points[n] += n` == `points[n] = n × freq(n)`
     * * Grouping converts the problem into **House Robber**
     * * Counting freq alone would just add an extra step
     *
     */
    public int deleteAndEarn_0_1(int[] nums) {
        int maxVal = 0;
        for (int n : nums) {
            maxVal = Math.max(maxVal, n);
        }


        int[] points = new int[maxVal + 1];
        /** Question: why `points[n] += n;`, but NOT record the freq ?
         *
         *   Answer (gpt):
         *
         *    We add n directly (instead of just counting frequency)
         *
         *    because:
         *       - Each occurrence of value n gives n points when chosen.
         *
         *       -> so,
         *       ```
         *       total points for value n = n × frequency(n)
         *       ```
         *
         *       -> By doing: `points[n] += n;`
         *         -> we are implicitly multiplying by frequency.
         */
        for (int n : nums) {
            points[n] += n;
        }

        int[] dp = new int[maxVal + 1];
        dp[0] = points[0];
        dp[1] = Math.max(points[0], points[1]);

        for (int i = 2; i <= maxVal; i++) {
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + points[i]);
        }

        return dp[maxVal];
    }


    // V0-2
    // IDEA: DP (LC 198 HOUSE ROBBER) (GEMINI)
    public int deleteAndEarn_0_2(int[] nums) {
        // 1. Find the maximum value in nums to determine our array size
        int maxVal = 0;
        for (int num : nums) {
            maxVal = Math.max(maxVal, num);
        }

        // 2. Create a "points" array where points[i] is the total
        // sum of all occurrences of value 'i' in the input
        int[] points = new int[maxVal + 1];
        for (int num : nums) {
            points[num] += num;
        }

        // 3. Apply House Robber DP logic
        // dp[i] = max points considering values up to i
        int[] dp = new int[maxVal + 1];

        dp[0] = points[0]; // Usually 0
        if (maxVal > 0) {
            dp[1] = Math.max(points[0], points[1]);
        }

        for (int i = 2; i <= maxVal; i++) {
            // Choice: Skip current i (take dp[i-1])
            // OR Take current i (points[i] + dp[i-2])
            dp[i] = Math.max(dp[i - 1], points[i] + dp[i - 2]);
        }

        return dp[maxVal];
    }

    // V0-3
    // Space Optimization (O(1) extra space) (GEMINI)
    public int deleteAndEarn_0_3(int[] nums) {
        int maxVal = 0;
        for (int num : nums)
            maxVal = Math.max(maxVal, num);

        int[] points = new int[maxVal + 1];
        for (int num : nums)
            points[num] += num;

        int prevTwo = 0;
        int prevOne = 0;

        for (int currentPoints : points) {
            int tmp = prevOne;
            prevOne = Math.max(prevOne, currentPoints + prevTwo);
            prevTwo = tmp;
        }

        return prevOne;
    }



    // V1
    // https://leetcode.ca/2017-12-09-740-Delete-and-Earn/


    // V2
    // IDEA: TOP DOWN DP
    // top down DP solution
    // https://leetcode.com/problems/delete-and-earn/solutions/284609/for-top-down-dp-fans-java-2ms-solution-e-sthk/
    public int deleteAndEarn_2(int[] nums) {
        // if we sort the array, we do not need to delete elements smaller than nums[idx] (ie nums[idx] - 1) because they are already computed
        // and saved in memo, we only need to delete nums[idx] + 1 and we can do this simply by skipping them since the array is sorted
        Arrays.sort(nums);
        return deleteAndEarn(nums, 0, new int[nums.length]);
    }

    private int deleteAndEarn(int[] nums, int idx, int[] memo) {
        // if we reached the end of the array, we can not earn anymore, return 0
        if (idx == nums.length)
            return 0;

        if (memo[idx] == 0) {
            // delete and earn this element
            int earned = nums[idx];
            int skip = idx + 1;

            // simply add all similar values of nums[idx] to earned at once
            while (skip < nums.length && nums[skip] == nums[idx]) {
                earned += nums[idx];
                skip++;
            }

            // skip all elements = nums[idx] + 1
            // this is instead of deleting the elements = nums[idx] + 1
            // which does not alter the array and make the solution work
            while (skip < nums.length && nums[skip] == nums[idx] + 1)
                skip++;

            // recurse
            earned += deleteAndEarn(nums, skip, memo);

            // skip deleting and earning this element
            int skipped = deleteAndEarn(nums, idx + 1, memo);

            // return the max of the 2 values
            memo[idx] = Math.max(earned, skipped);
        }

        return memo[idx];
    }


    // V3
    // IDEA: DP
    // https://leetcode.com/problems/delete-and-earn/solutions/1820924/java-simple-explained-by-prashant404-nvj8/
    /** Variable Description:
     *
     *  numToCount = map of nums[i] to their count
     *  min = min number in nums
     *  max = max number in nums
     *  prevIncEarn = Earning if previous num is excluded (deleted)
     *  prevExcEarn = Earning if previous num is included (not-deleted)
     *  incEarn = Earning if this num is excluded (deleted)
     *  excEarn = Earning if this num is included (not-deleted)
     */
    public int deleteAndEarn_3(int[] nums) {
        HashMap numToCount = new HashMap<Integer, Integer>();
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
           // TODO: fix below syntax for java 8
           // numToCount.compute(num, (k, v) -> v == null ? 1 : ++v);
            min = Math.min(min, num);
            max = Math.max(max, num);
        }

        int prevIncEarn = 0;
        int prevExcEarn = 0;
        for (int i = min; i <= max; i++) {
            // TODO: fix below syntax for java 8
            //int incEarn = prevExcEarn + i * numToCount.getOrDefault(i, 0);
            int incEarn = 0;
            int excEarn = Math.max(prevIncEarn, prevExcEarn);
            prevIncEarn = incEarn;
            prevExcEarn = excEarn;
        }
        return Math.max(prevIncEarn, prevExcEarn);
    }


    // V4


}
