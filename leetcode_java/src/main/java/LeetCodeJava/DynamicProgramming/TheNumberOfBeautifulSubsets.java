package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/the-number-of-beautiful-subsets/description/

import java.util.*;

/**
 * 2597. The Number of Beautiful Subsets
 * Medium
 *
 * You are given an array nums of positive integers and a positive integer k.
 *
 * A subset of nums is beautiful if it does not contain two integers with an absolute difference
 * equal to k.
 *
 * Return the number of non-empty beautiful subsets of the array nums.
 *
 * A subset of nums is an array that can be obtained by deleting some (possibly none) elements from
 * nums. Two subsets are different if and only if the chosen indices to delete are different.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,6], k = 2
 * Output: 4
 * Explanation: The beautiful subsets of the array nums are: [2], [4], [6], [2, 6].
 *
 * Example 2:
 *
 * Input: nums = [1], k = 1
 * Output: 1
 * Explanation: The beautiful subset of the array nums is [1].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 18
 * 1 <= nums[i], k <= 1000
 *
 */
public class TheNumberOfBeautifulSubsets {

    // V0
    // IDEA: GROUP BY value % k, THEN HOUSE ROBBER ALONG EACH CHAIN OF VALUES THAT DIFFER BY EXACTLY k
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int beautifulSubsets(int[] nums, int k) {
        // only values in the same remainder class mod k can conflict;
        // inside a class, keep value -> how many copies, sorted by value
        Map<Integer, TreeMap<Integer, Integer>> groups = new HashMap<>();
        for (int num : nums) {
            TreeMap<Integer, Integer> group = groups.computeIfAbsent(num % k, key -> new TreeMap<>());
            group.put(num, group.getOrDefault(num, 0) + 1);
        }

        // classes are independent, so the counts (including the empty choice) multiply
        long result = 1;
        for (TreeMap<Integer, Integer> group : groups.values()) {
            // skip = ways where the current value is not used, take = ways where it is
            long skip = 1;
            long take = 0;
            Integer previous = null;

            for (Map.Entry<Integer, Integer> entry : group.entrySet()) {
                int value = entry.getKey();
                int copies = entry.getValue();

                // the chain broke: the previous value cannot conflict with this one, start fresh
                if (previous != null && value - previous != k) {
                    result *= (skip + take);
                    skip = 1;
                    take = 0;
                }

                // taking this value needs the previous one skipped; any non-empty set of its copies
                long newTake = skip * ((1L << copies) - 1);
                long newSkip = skip + take;
                skip = newSkip;
                take = newTake;
                previous = value;
            }
            result *= (skip + take);
        }
        // drop the empty subset
        return (int) (result - 1);
    }

    // V1
    // IDEA: BACKTRACKING - INCLUDE / EXCLUDE EACH INDEX, TRACKING VALUE COUNTS (n <= 18, SO 2^18 LEAVES)
    /**
     * time = O(2^N)
     * space = O(N)
     */
    public int beautifulSubsets_1(int[] nums, int k) {
        Map<Integer, Integer> chosenCount = new HashMap<>();
        // the recursion counts the empty subset too
        return countSubsets(nums, k, 0, chosenCount) - 1;
    }

    /** Number of beautiful subsets of nums[index..] that can be added to the chosen values. */
    private int countSubsets(int[] nums, int k, int index, Map<Integer, Integer> chosenCount) {
        if (index == nums.length) {
            return 1;
        }

        // option 1: skip nums[index]
        int total = countSubsets(nums, k, index + 1, chosenCount);

        // option 2: take nums[index] if neither value - k nor value + k is already chosen
        int value = nums[index];
        boolean conflict = chosenCount.getOrDefault(value - k, 0) > 0
                || chosenCount.getOrDefault(value + k, 0) > 0;
        if (!conflict) {
            chosenCount.put(value, chosenCount.getOrDefault(value, 0) + 1);
            total += countSubsets(nums, k, index + 1, chosenCount);
            chosenCount.put(value, chosenCount.get(value) - 1);
        }
        return total;
    }
}
