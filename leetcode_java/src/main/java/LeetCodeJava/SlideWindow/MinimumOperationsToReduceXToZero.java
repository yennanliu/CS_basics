package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/description/

import java.util.HashMap;
import java.util.Map;

/**
 *   1658. Minimum Operations to Reduce X to Zero
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer array nums and an integer x. In one operation, you can either remove the leftmost or the rightmost element from the array nums and subtract its value from x. Note that this modifies the array for future operations.
 *
 * Return the minimum number of operations to reduce x to exactly 0 if it is possible, otherwise, return -1.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,4,2,3], x = 5
 * Output: 2
 * Explanation: The optimal solution is to remove the last two elements to reduce x to zero.
 * Example 2:
 *
 * Input: nums = [5,6,7,8,9], x = 4
 * Output: -1
 * Example 3:
 *
 * Input: nums = [3,2,20,1,1,3], x = 10
 * Output: 5
 * Explanation: The optimal solution is to remove the last three elements and the first two elements (5 operations in total) to reduce x to zero.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= nums[i] <= 104
 * 1 <= x <= 109
 *
 *
 *
 */
public class MinimumOperationsToReduceXToZero {

    /** NOTE !!!
     *
     *  CAN NOT use `greedy` algo for this LC
     */


    // V0
//    public int minOperations(int[] nums, int x) {
//
//    }


    // V1-1
    // IDEA: SLIDE WINDOW (gpt)
    /** CORE IDEA:
     *
     *   target = total_sum - x
     *
     *   -> We find the LONGEST (MAX) continuous subarray in the middle
     *      with sum = total_sum - x (target).
     *
     *   -> Because: Total Elements - Max Middle Elements = Min Edge Elements (Operations).
     *
     *
     *
     * -------
     *
     * MIN EDGE PIECES (Ops)            MAX MIDDLE SUBARRAY
     *      |  nums[0]  |  nums[1]  |          |  ...  |  ...  |  ...  |
     *      \_______________________/          \_______________________/
     *          Removed from Edges                 Left in the Center
     *               (Sum = x)                     (Sum = TotalSum - x)
     *
     *
     */
    /**  Dry run
     *
     *   > Example Input
     * >
     * > ```
     * > nums = [1,1,4,2,3]
     * > x = 5
     * > ```
     * >
     * > Total Sum:
     * >
     * > ```
     * > total = 1 + 1 + 4 + 2 + 3 = 11
     * > ```
     * >
     * > Target:
     * >
     * > ```
     * > target = total - x = 11 - 5 = 6
     * > ```
     * >
     * > We now find:
     * >
     * > ```
     * > longest subarray with sum = 6
     * > ```
     * >
     * > ---
     * >
     * > ## Dry Run Table
     * >
     * > | Step | r | nums[r] | Window (`l..r`) | Window Elements | sum | Action               | maxLen |
     * > | ---- | - | ------- | --------------- | --------------- | --- | -------------------- | ------ |
     * > | Init | - | -       | -               | -               | 0   | Initialize variables | -1     |
     * > | 1    | 0 | 1       | `0..0`          | `[1]`           | 1   | sum < target         | -1     |
     * > | 2    | 1 | 1       | `0..1`          | `[1,1]`         | 2   | sum < target         | -1     |
     * > | 3    | 2 | 4       | `0..2`          | `[1,1,4]`       | 6   | sum == target git a       | 3      |
     * > | 4    | 3 | 2       | `0..3`          | `[1,1,4,2]`     | 8   | sum > target         | 3      |
     * > | 5    | 3 | 2       | `1..3`          | `[1,4,2]`       | 7   | shrink window        | 3      |
     * > | 6    | 3 | 2       | `2..3`          | `[4,2]`         | 6   | sum == target        | 3      |
     * > | 7    | 4 | 3       | `2..4`          | `[4,2,3]`       | 9   | sum > target         | 3      |
     * > | 8    | 4 | 3       | `3..4`          | `[2,3]`         | 5   | shrink window        | 3      |
     *
     */
    public int minOperations_1_1(int[] nums, int x) {

        // --------------------------------------------
        // Step 1:
        // Calculate the total sum of the array
        // --------------------------------------------
        int total = 0;

        // Add every number into total
        for (int num : nums) {
            total += num;
        }

        // --------------------------------------------
        // Step 2:
        // Convert problem:
        //
        // Instead of:
        //   removing numbers from left/right = x
        //
        // We find:
        //   longest subarray with sum = total - x
        //
        // Why?
        //
        // removed_sum + remaining_sum = total
        //
        // Therefore:
        // remaining_sum = total - x
        // --------------------------------------------
        int target = total - x;

        // --------------------------------------------
        // Edge Case:
        //
        // If target == 0
        // means:
        // total == x
        //
        // So we must remove ALL elements
        // --------------------------------------------
        if (target == 0) {
            return nums.length;
        }

        // Length of array
        int n = nums.length;

        // --------------------------------------------
        // Sliding Window Variables
        // --------------------------------------------

        // Left pointer of window
        int l = 0;

        // Current window sum
        int sum = 0;

        // Store longest valid subarray length
        // Initialize with -1 meaning "not found"
        int maxLen = -1;

        // --------------------------------------------
        // Step 3:
        // Expand sliding window using right pointer
        // --------------------------------------------
        for (int r = 0; r < n; r++) {

            // Add current right element into window
            sum += nums[r];

            // ----------------------------------------
            // If window sum becomes too large,
            // shrink window from left side
            //
            // Since all numbers are positive,
            // shrinking decreases sum
            // ----------------------------------------
            while (l <= r && sum > target) {

                // Remove left element from window
                sum -= nums[l];

                // Move left pointer right
                l++;
            }

            // ----------------------------------------
            // If current window sum equals target,
            // we found a valid subarray
            // ----------------------------------------
            if (sum == target) {

                // Current window length
                int len = r - l + 1;

                // Keep the longest one
                maxLen = Math.max(maxLen, len);
            }
        }

        // --------------------------------------------
        // Step 4:
        //
        // If no valid subarray found:
        // return -1
        //
        // Otherwise:
        //
        // minimum operations =
        // total array length - longest kept subarray
        // --------------------------------------------
        return maxLen == -1 ? -1 : n - maxLen;
    }



    // V1-2
    // IDEA: SLIDE WINDOW (gemini)
    public int minOperations_1_2(int[] nums, int x) {
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }

        // The target sum we need to find in the middle subarray
        int target = totalSum - x;

        // Edge Case 1: If total sum is exactly x, we must remove all elements
        if (target == 0) {
            return nums.length;
        }
        // Edge Case 2: x is larger than the entire array sum, impossible to reach 0
        if (target < 0) {
            return -1;
        }

        int maxLength = -1;
        int currentSum = 0;
        int left = 0;

        // Sliding Window to find the max length of a subarray summing to 'target'
        for (int right = 0; right < nums.length; right++) {
            currentSum += nums[right];

            // Shrink the window from the left if the sum exceeds the target
            while (currentSum > target && left <= right) {
                currentSum -= nums[left];
                left++;
            }

            // If we hit the exact target, track the maximum subarray length
            if (currentSum == target) {
                maxLength = Math.max(maxLength, right - left + 1);
            }
        }

        // If maxLength remained -1, it means no valid subarray was found
        return maxLength == -1 ? -1 : nums.length - maxLength;
    }


    // V1-3
    // IDEA: PREFIX, SUFFIX SUM (GPT)
    public int minOperations_1_3(int[] nums, int x) {

        int n = nums.length;

        // ----------------------------------------
        // Map:
        // key   = suffix sum
        // value = operations used from right
        // ----------------------------------------
        Map<Integer, Integer> rightMap = new HashMap<>();

        // Base case:
        // taking nothing from right
        rightMap.put(0, 0);

        // ----------------------------------------
        // Build suffix sums from right side
        // ----------------------------------------
        int rightSum = 0;

        // r starts from n - 1
        for (int r = n - 1; r >= 0; r--) {

            // Add current right value
            rightSum += nums[r];

            // operations taken from right
            int ops = n - r;

            // Store:
            // suffix sum -> operations
            rightMap.put(rightSum, ops);
        }

        // ----------------------------------------
        // Answer initialization
        // ----------------------------------------
        int ans = Integer.MAX_VALUE;

        // ----------------------------------------
        // Case:
        // take ONLY from right
        // ----------------------------------------
        if (rightMap.containsKey(x)) {
            ans = rightMap.get(x);
        }

        // ----------------------------------------
        // Build prefix sums from left
        // ----------------------------------------
        int leftSum = 0;

        // l starts from 0
        for (int l = 0; l < n; l++) {

            // Add current left value
            leftSum += nums[l];

            // operations used from left
            int leftOps = l + 1;

            // Remaining value needed from right
            int remain = x - leftSum;

            // ------------------------------------
            // Check whether right side can provide
            // the remaining sum
            // ------------------------------------
            if (rightMap.containsKey(remain)) {

                int rightOps = rightMap.get(remain);

                // --------------------------------
                // Ensure no overlap
                //
                // total removed elements
                // must not exceed array length
                // --------------------------------
                if (leftOps + rightOps <= n) {

                    ans = Math.min(ans, leftOps + rightOps);
                }
            }
        }

        // ----------------------------------------
        // No valid answer found
        // ----------------------------------------
        return ans == Integer.MAX_VALUE ? -1 : ans;
    }


    // V1-4
    public int minOperations_1_4(int[] nums, int x) {
        int n = nums.length;
        int currentSum = 0;
        int l = 0;

        // 1. Push the left pointer as far as possible to try to hit 'x' from the left side
        while (l < n && currentSum < x) {
            currentSum += nums[l];
            l++;
        }

        // If the entire array sum is less than x, it's impossible
        if (currentSum < x) {
            return -1;
        }

        int minOps = Integer.MAX_VALUE;

        // If the prefix exactly hits x, record how many operations it took
        if (currentSum == x) {
            minOps = l;
        }

        // 2. Start the right pointer at the very end
        int r = n - 1;

        // 3. Move inward. Shrink the left prefix while expanding the right suffix.
        while (l > 0) {
            // Remove the last element included from the left side
            l--;
            currentSum -= nums[l];

            // Compensate by taking elements from the right side until currentSum >= x
            while (r > l && currentSum < x) {
                currentSum += nums[r];
                r--;
            }

            // If we hit the exact target, calculate total operations
            if (currentSum == x) {
                // Elements taken from left: l
                // Elements taken from right: n - 1 - r
                int currentOps = l + (n - 1 - r);
                minOps = Math.min(minOps, currentOps);
            }
        }

        return minOps == Integer.MAX_VALUE ? -1 : minOps;
    }




    // V2
    // https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/solutions/2136570/change-your-perspective-java-explanation-5phd/
    public int minOperations_2(int[] nums, int x) {
        int sum = 0;
        for (int num : nums)
            sum += num;

        int maxLength = -1, currSum = 0;
        for (int l = 0, r = 0; r < nums.length; r++) {
            currSum += nums[r];
            while (l <= r && currSum > sum - x)
                currSum -= nums[l++];
            if (currSum == sum - x)
                maxLength = Math.max(maxLength, r - l + 1);
        }

        return maxLength == -1 ? -1 : nums.length - maxLength;
    }



    // V3
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/solutions/4066422/9651-sliding-window-by-vanamsen-3lh5/
    public int minOperations_3(int[] nums, int x) {
        int target = -x, n = nums.length;
        for (int num : nums)
            target += num;

        if (target == 0)
            return n;

        int maxLen = 0, curSum = 0, left = 0;

        for (int right = 0; right < n; ++right) {
            curSum += nums[right];
            while (left <= right && curSum > target) {
                curSum -= nums[left];
                left++;
            }
            if (curSum == target) {
                maxLen = Math.max(maxLen, right - left + 1);
            }
        }

        return maxLen != 0 ? n - maxLen : -1;
    }




    // V4
    // IDEA: SLIDE WINDOW
    // https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/solutions/8163080/easy-and-understandable-sliding-window-a-kc62/
    public int minOperations_4(int[] nums, int x) {
        int left = 0;
        int target = 0;
        int sum = 0;
        int result = -1;
        for (int num : nums) {
            target += num;
        }

        target = target - x;
        if (target < 0)
            return -1;
        if (target == 0)
            return nums.length;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum > target) {
                sum -= nums[left];
                left++;
            }
            if (sum == target) {
                result = Math.max(result, right - left + 1);
            }
        }
        return result == -1 ? -1 : nums.length - result;
    }






}
