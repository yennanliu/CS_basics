package LeetCodeJava.Stack;

// https://leetcode.com/problems/steps-to-make-array-non-decreasing/description/

import java.util.ArrayList;
import java.util.List;
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


    // V0-1
    // IDEA: MONO STACK + DP (gemini)
    /**  Dry run:
     *
     * Let's dry run the optimized $O(N)$
     * Monotonic Stack code using the example
     *
     *
     * `nums = [10, 1, 2, 7, 1, 3]`.
     *
     * ### 🎬 The Setup
     * * **`dp[i]`**: Steps required to remove `nums[i]`.
     * * **`stack`**: Stores indices of elements that haven't been "eaten" yet (Monotonic Decreasing).
     * * **`maxSteps`**: Our global answer.
     *
     * ---
     *
     * ### 🚶 Step-by-Step Execution
     *
     | Step | i | nums[i] | while loop (Pops) | currentSteps (Max of popped DPs) | dp[i] Logic | stack (Indices) |
     |:----:|:-:|:-------:|:------------------|:----------------------------------|:------------|:----------------|
     | **1** | 0 | **10** | *Empty* | 0 | stack empty → **0** | `[0]` |
     | **2** | 1 | **1** | *None* (1 < 10) | 0 | 1 + 0 → **1** | `[0, 1]` |
     | **3** | 2 | **2** | Pop index 1 (2 > 1) | max(0, dp[1] = 1) = **1** | 1 + 1 → **2** | `[0, 2]` |
     | **4** | 3 | **7** | Pop index 2 (7 > 2) | max(1, dp[2] = 2) = **2** | 1 + 2 → **3** | `[0, 3]` |
     | **5** | 4 | **1** | *None* (1 < 7) | 0 | 1 + 0 → **1** | `[0, 3, 4]` |
     | **6** | 5 | **3** | Pop index 4 (3 > 1) | max(0, dp[4] = 1) = **1** | 1 + 1 → **2** | `[0, 3, 5]` |

     * **Final `maxSteps` = 3.**
     *
     * ---
     *
     * ### 🔍 Deep Dive: Why did `dp[3]` (value 7) become 3?
     *
     * Looking at the array `[10, 1, 2, 7...]`:
     * 1.  **Step 1:** `10` eats `1`. Now the array is `[10, 2, 7...]`.
     * 2.  **Step 2:** `10` eats `2`. Now the array is `[10, 7...]`.
     * 3.  **Step 3:** `10` eats `7`. Now the array is `[10...]`.
     *
     * The `Math.max` in the code captured this perfectly:
     * * When we encountered **7**, we saw it was larger than **2**.
     * * We knew **2** took **2 steps** to be removed.
     * * Therefore, **7** had to wait for those **2 steps** to finish before the "Killer" (`10`) could finally reach it.
     * * Result: $2 + 1 = 3$.
     *
     * ### 📊 Key Takeaway for the Interview
     * * If `nums[i] < nums[i-1]`, it's eaten immediately in **1 step**.
     * * If `nums[i] >= nums[i-1]`, it has to "wait out" all the smaller elements to its left until it meets an even bigger "Killer." The `Math.max` keeps track of the longest wait time in that "shadow."
     *
     *
     */
    public int totalSteps_0_1(int[] nums) {
        int n = nums.length;
        int maxSteps = 0;

        /** NOTE !!!
         *
         *   dp[i] = steps required to remove nums[i]
         */
        // dp[i] = steps required to remove nums[i]
        int[] dp = new int[n];


        /** NOTE !!!
         *
         *   Stack: `indices` of elements
         *          (monotonic decreasing order)
         */
        // Stack stores indices of elements in a monotonic decreasing order
        Stack<Integer> stack = new Stack<>();


        /** NOTE !!!
         *
         *  loop from 0 to n
         */
        for (int i = 0; i < n; i++) {
            int currentSteps = 0;

            /** Demo:
             *
             * ### 📊 Step-by-Step Logic Visualization
             * Input: `[10, 9, 1, 2, 3]`
             *
             * 1. **`10`** is on stack.
             * 2. **`9`** comes. `9 < 10`. `dp[9] = 0 + 1 = 1`. (Step 1: 10 eats 9).
             * 3. **`1`** comes. `1 < 9`. `dp[1] = 0 + 1 = 1`. (Step 1: 9 eats 1).
             * 4. **`2`** comes. `2 > 1`. We enter the `while` loop:
             *    * We pop `1`. `currentSteps = max(0, dp[1]) = 1`.
             *    * Now the stack top is `9`. Since `2 < 9`, we stop.
             *    * `dp[2] = currentSteps + 1 = 2`.
             *    * **Why 2?** Because in Step 1, `9` eats `1`. Only *then* (Step 2) is `2` exposed to `9` so `9` can eat it.
             * 5. **`3`** comes. `3 > 2`. We enter the `while` loop:
             *    * We pop `2`. `currentSteps = max(0, dp[2]) = 2`.
             *    * Stack top is `9`. Since `3 < 9`, we stop.
             *    * `dp[3] = currentSteps + 1 = 3`.
             *    * **Why 3?** It has to wait for `2` to be cleared, which already had to wait for `1` to be cleared.
             *
             */
            // While current element is >= the top of the stack,
            // it means the current element will
            // 1) "survive" `LONGER` or
            // 2) eat the elements that the
            // `stack top` was supposed to `eat`.
            while (!stack.isEmpty() && nums[i] >= nums[stack.peek()]) {
                currentSteps = Math.max(currentSteps, dp[stack.pop()]);
            }

            // If stack is not empty, there is a larger element to the left
            // that will eventually "eat" nums[i].
            if (!stack.isEmpty()) {
                dp[i] = currentSteps + 1;
                maxSteps = Math.max(maxSteps, dp[i]);
            } else {
                // No larger element to the left, nums[i] will never be removed.
                dp[i] = 0;
            }

            stack.push(i);
        }

        return maxSteps;
    }



    // V1-1
    // IDEA: BRUTE FORCE + SIMULATION (gpt) (TLE)
    public int totalSteps_1_1(int[] nums) {
        List<Integer> list = new ArrayList<>();
        for (int x : nums)
            list.add(x);

        int steps = 0;

        while (true) {
            List<Integer> next = new ArrayList<>();
            next.add(list.get(0)); // first element always stays

            boolean removed = false;

            for (int i = 1; i < list.size(); i++) {
                if (list.get(i) >= list.get(i - 1)) {
                    next.add(list.get(i));
                } else {
                    removed = true; // this element is removed in this round
                }
            }

            if (!removed)
                break;

            list = next;
            steps++;
        }

        return steps;
    }


    // V1-2
    // IDEA: MONO STACK + DP (GPT)
    /**  Key insights:
     *
     *
     * 👉 Each element may “die” after a certain number of
     *    rounds depending on larger elements to its left.
     *
     *
     * 👉 This is best solved with a monotonic stack + DP,
     *     not repeated simulation.
     */
    /** Steps:
     *
     * Traverse from right → left
     * Maintain a stack of indices
     * Track how many steps each element survives (dp[i])
     * If nums[i] > nums[j], then j will be removed eventually
     *
     */
    public int totalSteps_1_2(int[] nums) {
        int n = nums.length;

        /** NOTE !!!
         *
         * dp[i] = how many `rounds` it takes for nums[i] to be `removed.`
         *
         * If dp[i] = 0, it never gets removed.
         *
         */
        int[] dp = new int[n];

        /** NOTE !!!
         *
         * Stack stores `indices` (not values).
         *
         * Maintains a `monotonic DECREASING` structure
         * (from right side perspective).
         *
         */
        Stack<Integer> stack = new Stack<>();

        // Tracks the maximum steps among all elements → final answer.
        int res = 0;

        /** NOTE !!!
         *
         *  loop from right hand side
         *
         *
         *  Why? Because each element depends on elements to its
         *  right (who might get removed first).
         *
         */
        for (int i = n - 1; i >= 0; i--) {

            /**
             * Temporary variable:
             *
             * Tracks how many rounds current element nums[i]
             * survives before being removed.
             *
             */
            int maxSteps = 0;

            /**
             * - If nums[i] > nums[j] (where j = stack.peek()):
             *     - Then nums[j] will be removed
             *       before or because of nums[i].
             *
             *
             * - So we "resolve" those smaller elements.
             *
             *
             */
            while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
                /** NOTE !!! Core logic:
                 *
                 *
                 *  This is the core logic, broken down:
                 *
                 * 1. stack.pop() → remove index j
                 *
                 * 2. Two competing timelines:
                 *       - maxSteps + 1
                 *              - Means: current element waits
                 *                       one more round than
                 *                       previous popped element chain
                 *      - dp[j]
                 *              - Means: j itself might survive multiple
                 *                       rounds before disappearing
                 *
                 *  3.
                 *
                 *   ```
                 *   Math.max(maxSteps + 1, dp[j])
                 *   ```
                 *
                 *  ->
                 *    Interpretation:
                 *
                 *     - Current element must wait until
                 *       all smaller right elements are gone
                 *
                 *     - So it inherits the maximum delay
                 *
                 *
                 */
                maxSteps = Math.max(maxSteps + 1, dp[stack.pop()]);
            }

            dp[i] = maxSteps;
            res = Math.max(res, dp[i]);

            stack.push(i);
        }

        return res;
    }


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
