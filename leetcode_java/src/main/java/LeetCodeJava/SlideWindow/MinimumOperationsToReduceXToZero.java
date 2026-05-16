package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-operations-to-reduce-x-to-zero/description/
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
     * >
     * > ---
     * >
     * > ## Detailed Explanation
     * >
     * > ### Step 1
     * >
     * > Add `1`
     * >
     * > ```
     * > window = [1]
     * > sum = 1
     * > ```
     * >
     * > Smaller than target `6`.
     * >
     * > ---
     * >
     * > ### Step 2
     * >
     * > Add another `1`
     * >
     * > ```
     * > window = [1,1]
     * > sum = 2
     * > ```
     * >
     * > Still smaller than `6`.
     * >
     * > ---
     * >
     * > ### Step 3
     * >
     * > Add `4`
     * >
     * > ```
     * > window = [1,1,4]
     * > sum = 6
     * > ```
     * >
     * > Valid subarray found.
     * >
     * > Length:
     * >
     * > r-l+1 = 2-0+1 = 3
     * >
     * > ```
     * > maxLen = 3
     * > ```
     * >
     * > ---
     * >
     * > ### Step 4
     * >
     * > Add `2`
     * >
     * > ```
     * > window = [1,1,4,2]
     * > sum = 8
     * > ```
     * >
     * > Too large.
     * >
     * > Need shrinking.
     * >
     * > ---
     * >
     * > ### Step 5
     * >
     * > Remove leftmost `1`
     * >
     * > ```
     * > window = [1,4,2]
     * > sum = 7
     * > ```
     * >
     * > Still too large.
     * >
     * > ---
     * >
     * > ### Step 6
     * >
     * > Remove another `1`
     * >
     * > ```
     * > window = [4,2]
     * > sum = 6
     * > ```
     * >
     * > Valid again.
     * >
     * > Length:
     * >
     * > r-l+1 = 3-2+1 = 2
     * >
     * > `maxLen` remains `3`.
     * >
     * > ---
     * >
     * > ### Step 7
     * >
     * > Add `3`
     * >
     * > ```
     * > window = [4,2,3]
     * > sum = 9
     * > ```
     * >
     * > Too large.
     * >
     * > ---
     * >
     * > ### Step 8
     * >
     * > Shrink window:
     * >
     * > Remove `4`
     * >
     * > ```
     * > window = [2,3]
     * > sum = 5
     * > ```
     * >
     * > End of loop.
     * >
     * > ---
     * >
     * > ## Final Result
     * >
     * > Longest valid subarray:
     * >
     * > ```
     * > [1,1,4]
     * > ```
     * >
     * > Length:
     * >
     * > ```
     * > maxLen = 3
     * > ```
     * >
     * > Minimum operations:
     * >
     * > n-maxLen = 5-3 = 2
     * >
     * > Final Answer:
     * >
     * > ```
     * > 2
     * > ```
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
