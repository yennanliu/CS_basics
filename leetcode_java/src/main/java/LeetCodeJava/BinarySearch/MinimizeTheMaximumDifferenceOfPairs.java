package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/minimize-the-maximum-difference-of-pairs/description/

import java.util.Arrays;

/**
 *  2616. Minimize the Maximum Difference of Pairs
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a 0-indexed integer array nums and an integer p. Find p pairs of indices of nums such that the maximum difference amongst all the pairs is minimized. Also, ensure no index appears more than once amongst the p pairs.
 *
 * Note that for a pair of elements at the index i and j, the difference of this pair is |nums[i] - nums[j]|, where |x| represents the absolute value of x.
 *
 * Return the minimum maximum difference among all p pairs. We define the maximum of an empty set to be zero.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [10,1,2,7,1,3], p = 2
 * Output: 1
 * Explanation: The first pair is formed from the indices 1 and 4, and the second pair is formed from the indices 2 and 5.
 * The maximum difference is max(|nums[1] - nums[4]|, |nums[2] - nums[5]|) = max(0, 1) = 1. Therefore, we return 1.
 * Example 2:
 *
 * Input: nums = [4,2,1,2], p = 1
 * Output: 0
 * Explanation: Let the indices 1 and 3 form a pair. The difference of that pair is |2 - 2| = 0, which is the minimum we can attain.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 105
 * 0 <= nums[i] <= 109
 * 0 <= p <= (nums.length)/2
 *
 */
public class MinimizeTheMaximumDifferenceOfPairs {

    // V0
//    public int minimizeMax(int[] nums, int p) {
//
//    }


    // V0-1
    // IDEA: Greedy + Binary Search (GEMINI)
    public int minimizeMax_0_1(int[] nums, int p) {
        if (p == 0)
            return 0;
        Arrays.sort(nums);

        int n = nums.length;
        // The range of possible answers is [0, max_difference]
        int low = 0, high = nums[n - 1] - nums[0];
        int res = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            // If we can find at least p pairs with diff <= mid,
            // try a smaller max difference.
            if (canFormPairs(nums, p, mid)) {
                res = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return res;
    }

    private boolean canFormPairs(int[] nums, int p, int maxDiff) {
        int count = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            // If the current pair fits the constraint, take it!
            if (nums[i + 1] - nums[i] <= maxDiff) {
                count++;
                i++; // Skip the next index because an element can only be in one pair
            }
            if (count >= p)
                return true;
        }
        return count >= p;
    }


    // V0-2
    // IDEA: Greedy + Binary Search (GPT)
    public int minimizeMax_0_2(int[] nums, int p) {

        Arrays.sort(nums);

        int left = 0;
        int right = nums[nums.length - 1] - nums[0];

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (canForm(nums, p, mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }

    private boolean canForm(int[] nums, int p, int maxDiff) {
        int count = 0;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] - nums[i - 1] <= maxDiff) {
                count++;
                i++; // skip next index (avoid reuse)
            }
        }

        return count >= p;
    }


    // V1
    // IDEA: Greedy + Binary Search
    // https://leetcode.com/problems/minimize-the-maximum-difference-of-pairs/editorial/
    // Find the number of valid pairs by greedy approach
    private int countValidPairs(int[] nums, int threshold) {
        int index = 0, count = 0;
        while (index < nums.length - 1) {
            // If a valid pair is found, skip both numbers.
            if (nums[index + 1] - nums[index] <= threshold) {
                count++;
                index++;
            }
            index++;
        }
        return count;
    }

    public int minimizeMax_1(int[] nums, int p) {
        Arrays.sort(nums);
        int n = nums.length;
        int left = 0, right = nums[n - 1] - nums[0];

        while (left < right) {
            int mid = left + (right - left) / 2;

            // If there are enough pairs, look for a smaller threshold.
            // Otherwise, look for a larger threshold.
            if (countValidPairs(nums, mid) >= p) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }


    // V2


    // V3


}
