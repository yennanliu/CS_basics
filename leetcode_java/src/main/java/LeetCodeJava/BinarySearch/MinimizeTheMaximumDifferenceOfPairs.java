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
