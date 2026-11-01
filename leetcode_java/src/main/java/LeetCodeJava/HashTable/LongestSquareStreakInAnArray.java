package LeetCodeJava.HashTable;

// https://leetcode.com/problems/longest-square-streak-in-an-array/description/

import java.util.*;

/**
 * 2501. Longest Square Streak in an Array
 * Medium
 *
 * You are given an integer array nums. A subsequence of nums is called a square streak if:
 *
 * The length of the subsequence is at least 2, and
 * after sorting the subsequence, each element (except the first element) is the square of the previous number.
 *
 * Return the length of the longest square streak in nums, or return -1 if there is no square streak.
 *
 * A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,3,6,16,8,2]
 * Output: 3
 * Explanation: Choose the subsequence [4,16,2]. After sorting it, it becomes [2,4,16].
 * - 4 = 2 * 2.
 * - 16 = 4 * 4.
 * Therefore, [4,16,2] is a square streak.
 * It can be shown that every subsequence of length 4 is not a square streak.
 *
 * Example 2:
 *
 * Input: nums = [2,3,5,6,7]
 * Output: -1
 * Explanation: There is no square streak in nums so return -1.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 2 <= nums[i] <= 10^5
 *
 */
public class LongestSquareStreakInAnArray {

    // V0
    // IDEA: HASH SET + FOLLOW THE SQUARING CHAIN x, x^2, x^4, ... (chains are at most 5 long)
    /**
     * time = O(N * log log M)
     * space = O(N)
     */
    public int longestSquareStreak(int[] nums) {
        // order is irrelevant: a subsequence may be sorted, so only presence matters
        Set<Long> present = new HashSet<>();
        for (int num : nums) {
            present.add((long) num);
        }

        int longest = -1;
        for (int num : nums) {
            int length = 0;
            // long, because squaring 10^5 overflows an int
            long current = num;
            while (present.contains(current)) {
                length++;
                current = current * current;
            }
            // a streak needs at least 2 elements
            if (length > 1) {
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }
}
