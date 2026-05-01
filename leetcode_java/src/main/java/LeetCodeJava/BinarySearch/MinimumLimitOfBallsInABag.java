package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/description/

import java.util.Arrays;

/**
 *  1760. Minimum Limit of Balls in a Bag
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer array nums where the ith bag contains nums[i] balls. You are also given an integer maxOperations.
 *
 * You can perform the following operation at most maxOperations times:
 *
 * Take any bag of balls and divide it into two new bags with a positive number of balls.
 * For example, a bag of 5 balls can become two new bags of 1 and 4 balls, or two new bags of 2 and 3 balls.
 * Your penalty is the maximum number of balls in a bag. You want to minimize your penalty after the operations.
 *
 * Return the minimum possible penalty after performing the operations.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [9], maxOperations = 2
 * Output: 3
 * Explanation:
 * - Divide the bag with 9 balls into two bags of sizes 6 and 3. [9] -> [6,3].
 * - Divide the bag with 6 balls into two bags of sizes 3 and 3. [6,3] -> [3,3,3].
 * The bag with the most number of balls has 3 balls, so your penalty is 3 and you should return 3.
 * Example 2:
 *
 * Input: nums = [2,4,8,2], maxOperations = 4
 * Output: 2
 * Explanation:
 * - Divide the bag with 8 balls into two bags of sizes 4 and 4. [2,4,8,2] -> [2,4,4,4,2].
 * - Divide the bag with 4 balls into two bags of sizes 2 and 2. [2,4,4,4,2] -> [2,2,2,4,4,2].
 * - Divide the bag with 4 balls into two bags of sizes 2 and 2. [2,2,2,4,4,2] -> [2,2,2,2,2,4,2].
 * - Divide the bag with 4 balls into two bags of sizes 2 and 2. [2,2,2,2,2,4,2] -> [2,2,2,2,2,2,2,2].
 * The bag with the most number of balls has 2 balls, so your penalty is 2, and you should return 2.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 1 <= maxOperations, nums[i] <= 109
 *
 *
 */
public class MinimumLimitOfBallsInABag {

    // V0
//    public int minimumSize(int[] nums, int maxOperations) {
//
//    }

    // V0-1
    // IDEA: BINARY SEARCH (gpt)
    public int minimumSize_0_1(int[] nums, int maxOperations) {
        int l = 1;
        int r = 0;

        for (int x : nums) {
            /** NOTE !!!
             *
             *  -> max, not min
             *
             */
            r = Math.max(r, x); // max, not min
        }

        int ans = r;

        while (l <= r) {
            /** NOTE !!!
             *
             *  binary search is finding
             *  `penalty`
             */
            int mid = l + (r - l) / 2;

            if (canSplit(nums, maxOperations, mid)) {
                ans = mid;
                /** NOTE !!!
                 *
                 *  ->  r = mid - 1; // try smaller penalty
                 */
                r = mid - 1; // try smaller penalty
            } else {
                l = mid + 1;
            }
        }

        return ans;
    }

    /** NOTE !!!
     *
     *  binary search is finding
     *  `penalty`
     *
     *  // e.g. `maxBalls` is `penalty`
     */
    private boolean canSplit(int[] nums, int maxOperations, int maxBalls) {
        int ops = 0;

        for (int x : nums) {
            /** NOTE !!!
             *
             *  1. `x-1`
             *
             *  2. ONLY track `op count`,
             *     NO NEED to deal with group cnt
             *
             *  3. why `x-1` ?
             *
             *  -> V1
             *
             *   Formula:
             *
             *   ceil(x / penalty) - 1
             *   OR
             *   (x - 1) / penalty
             *
             *
             *  -> V2
             *
             *  Why (x - 1) / maxBalls works
             *
             * If a bag has x balls and each
             * bag can have at most maxBalls,
             * the number of splits needed is:
             *
             * Example: x = 9, maxBalls = 3
             * We want groups like [3,3,3]
             * Splits needed = 2
             * (9 - 1) / 3 = 8 / 3 = 2
             *
             * This formula avoids edge cases cleanly.
             *
             *
             *
             */
            ops += (x - 1) / maxBalls; // required splits
            if (ops > maxOperations) {
                return false;
            }
        }

        return true;
    }


    // V0-2
    // IDEA: BINARY SEARCH (GEMINI)
    public int minimumSize_0_2(int[] nums, int maxOperations) {
        int l = 1;
        int r = 0;
        for (int x : nums) {
            r = Math.max(r, x);
        }

        int ans = r;
        /** NOTE !!!
         *
         *  binary search is finding
         *  `penalty`
         */
        while (l <= r) {
            int mid = l + (r - l) / 2;

            if (canAchieve(nums, maxOperations, mid)) {
                ans = mid; // This penalty is possible, try to go lower
                r = mid - 1;
            } else {
                l = mid + 1; // Penalty too small, need more operations
            }
        }
        return ans;
    }

    /** NOTE !!!
     *
     *  binary search is finding
     *  `penalty`
     */
    private boolean canAchieve(int[] nums, int maxOperations, int penalty) {
        long opsNeeded = 0;
        for (int x : nums) {
            // Number of operations to split x into bags of size <= penalty
            // Formula: ceil(x / penalty) - 1  OR  (x - 1) / penalty
            opsNeeded += (x - 1) / penalty;

            if (opsNeeded > maxOperations)
                return false;
        }
        return opsNeeded <= maxOperations;
    }


    // V1
    // IDEA: BINARY SEARCH
    // https://youtu.be/MQlC8EoOdZ0?si=y3nbj-38KazGcFkd
    // https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/solutions/6121496/video-short-simple-explained-step-by-ste-rsxz/
    public int minimumSize_1(int[] nums, int maxOps) {
        int low = 1, high = Arrays.stream(nums).max().getAsInt();
        while (low < high) {
            int mid = (low + high) / 2;
            int ops = 0;
            for (int n : nums) {
                ops += (n - 1) / mid;
            }
            if (ops <= maxOps)
                high = mid;
            else
                low = mid + 1;
        }
        return high;
    }


    // V2
    // IDEA: Binary Search on The Answer
    // https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/editorial/
    public int minimumSize_2(int[] nums, int maxOperations) {
        // Binary search bounds
        int left = 1;
        int right = 0;

        for (int num : nums) {
            right = Math.max(right, num);
        }

        // Perform binary search to find the optimal maxBallsInBag
        while (left < right) {
            int middle = (left + right) / 2;

            // Check if a valid distribution is possible with the current middle value
            if (isPossible(middle, nums, maxOperations)) {
                right = middle; // If possible, try a smaller value (shift right to middle)
            } else {
                left = middle + 1; // If not possible, try a larger value (shift left to middle + 1)
            }
        }

        // Return the smallest possible value for maxBallsInBag
        return left;
    }

    // Helper function to check if a distribution is possible for a given maxBallsInBag
    private boolean isPossible(
            int maxBallsInBag,
            int[] nums,
            int maxOperations) {
        int totalOperations = 0;

        // Iterate through each bag in the array
        for (int num : nums) {
            // Calculate the number of operations needed to split this bag
            int operations = (int) Math.ceil(num / (double) maxBallsInBag) - 1;
            totalOperations += operations;

            // If total operations exceed maxOperations, return false
            if (totalOperations > maxOperations) {
                return false;
            }
        }

        // We can split the balls within the allowed operations, return true
        return true;
    }


    // V3
    // https://leetcode.com/problems/minimum-limit-of-balls-in-a-bag/solutions/6121632/simple-and-easy-solution-binary-search-b-rg48/
    public int minimumSize_3(int[] nums, int maxOperations) {
        int left = 1, right = Arrays.stream(nums).max().getAsInt();
        int ans = right;

        while (left <= right) {
            int mid = (left + right) / 2;
            int ops = 0;

            for (int n : nums) {
                ops += (n - 1) / mid;
                if (ops > maxOperations) {
                    break;
                }
            }

            if (ops <= maxOperations) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return ans;
    }



    // V4





}
