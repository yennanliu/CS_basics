package LeetCodeJava.Array;

// https://leetcode.com/problems/make-array-elements-equal-to-zero/description/

/**
 * 3354. Make Array Elements Equal to Zero
 * Easy
 *
 * You are given an integer array nums.
 *
 * Start by selecting a starting position curr such that nums[curr] == 0, and choose a movement direction of either left or right.
 *
 * After that, you repeat the following process:
 *
 * If curr is out of the range [0, n - 1], this process ends.
 * If nums[curr] == 0, move in the current direction by incrementing curr if you are moving right, or decrementing curr if you are moving left.
 * Else if nums[curr] > 0:
 *     Decrement nums[curr] by 1.
 *     Reverse your movement direction (left becomes right and vice versa).
 *     Take a step in your new direction.
 *
 * A selection of the initial position curr and movement direction is considered valid if every element in nums becomes 0 by the end of the process.
 *
 * Return the number of possible valid selections.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,0,2,0,3]
 * Output: 2
 * Explanation:
 * The only possible valid selections are the following:
 * Choose curr = 3, and a movement direction to the left.
 *     [1,0,2,0,3] -> [1,0,2,0,3] -> [1,0,1,0,3] -> [1,0,1,0,3] -> [1,0,1,0,2] -> [1,0,1,0,2] -> [1,0,0,0,2] -> [1,0,0,0,2] -> [1,0,0,0,1] -> [1,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,0].
 * Choose curr = 3, and a movement direction to the right.
 *     [1,0,2,0,3] -> [1,0,2,0,3] -> [1,0,2,0,2] -> [1,0,2,0,2] -> [1,0,1,0,2] -> [1,0,1,0,2] -> [1,0,1,0,1] -> [1,0,1,0,1] -> [1,0,0,0,1] -> [1,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,1] -> [0,0,0,0,0].
 *
 * Example 2:
 *
 * Input: nums = [2,3,4,0,4,1,0]
 * Output: 0
 * Explanation:
 * There are no possible valid selections.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 100
 * There is at least one element i where nums[i] == 0.
 *
 */
public class MakeArrayElementsEqualToZero {

    // V0
    // IDEA: SIMULATION (n <= 100 and values <= 100, so replaying every start and direction is cheap)
    /**
     * time = O(N * (N + S)), S = sum(nums)
     * space = O(N)
     */
    public int countValidSelections(int[] nums) {
        int n = nums.length;
        int validCount = 0;

        for (int start = 0; start < n; start++) {
            if (nums[start] != 0) {
                continue;
            }
            // try moving right first (+1), then left first (-1)
            int[] directions = {1, -1};
            for (int initialDirection : directions) {
                if (endsAllZero(nums, start, initialDirection)) {
                    validCount++;
                }
            }
        }
        return validCount;
    }

    /** Runs the process on a copy of nums and reports whether every element ended at 0. */
    private boolean endsAllZero(int[] nums, int start, int initialDirection) {
        int n = nums.length;
        int[] arr = nums.clone();
        int curr = start;
        int direction = initialDirection;

        while (curr >= 0 && curr < n) {
            if (arr[curr] == 0) {
                // zero: keep walking the same way
                curr += direction;
            } else {
                // positive: decrement, reverse, and step
                arr[curr]--;
                direction = -direction;
                curr += direction;
            }
        }

        for (int value : arr) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

    // V1
    // IDEA: PREFIX SUM (the ball bounces between the two sides, draining them alternately)
    //   starting at a zero with leftSum on its left and rightSum on its right:
    //   leftSum == rightSum      -> both directions work (2)
    //   |leftSum - rightSum| == 1 -> only towards the larger side works (1)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countValidSelections_1(int[] nums) {
        int total = 0;
        for (int value : nums) {
            total += value;
        }

        int validCount = 0;
        int leftSum = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {
                int rightSum = total - leftSum;
                if (leftSum == rightSum) {
                    validCount += 2;
                } else if (Math.abs(leftSum - rightSum) == 1) {
                    validCount += 1;
                }
            }
            leftSum += nums[i];
        }
        return validCount;
    }
}
