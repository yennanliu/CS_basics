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


    // V0
//    public int minOperations(int[] nums, int x) {
//
//    }


    // V1-1
    // IDEA: SLIDE WINDOW (gpt)
    public int minOperations_1_1(int[] nums, int x) {

        int total = 0;

        for (int num : nums) {
            total += num;
        }

        int target = total - x;

        // remove all elements
        if (target == 0) {
            return nums.length;
        }

        int n = nums.length;

        int l = 0;
        int sum = 0;
        int maxLen = -1;

        for (int r = 0; r < n; r++) {

            sum += nums[r];

            while (l <= r && sum > target) {
                sum -= nums[l];
                l++;
            }

            if (sum == target) {
                maxLen = Math.max(maxLen, r - l + 1);
            }
        }

        return maxLen == -1 ? -1 : n - maxLen;
    }



    // V1-2
    // IDEA: SLIDE WINDOW (gemini)
    public int minOperations(int[] nums, int x) {
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
