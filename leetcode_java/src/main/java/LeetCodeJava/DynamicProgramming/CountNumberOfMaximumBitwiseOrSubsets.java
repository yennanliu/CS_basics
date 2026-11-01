package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-number-of-maximum-bitwise-or-subsets/description/

import java.util.*;

/**
 * 2044. Count Number of Maximum Bitwise-OR Subsets
 * Medium
 *
 * Given an integer array nums, find the maximum possible bitwise OR of a subset of nums
 * and return the number of different non-empty subsets with the maximum bitwise OR.
 *
 * An array a is a subset of an array b if a can be obtained from b by deleting some
 * (possibly zero) elements of b. Two subsets are considered different if the indices of
 * the elements chosen are different.
 *
 * The bitwise OR of an array a is equal to a[0] OR a[1] OR ... OR a[a.length - 1]
 * (0-indexed).
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1]
 * Output: 2
 * Explanation: The maximum possible bitwise OR of a subset is 3. There are 2 subsets with
 * a bitwise OR of 3:
 * - [3]
 * - [3,1]
 *
 * Example 2:
 *
 * Input: nums = [2,2,2]
 * Output: 7
 * Explanation: All non-empty subsets of [2,2,2] have a bitwise OR of 2. There are
 * 2^3 - 1 = 7 total subsets.
 *
 * Example 3:
 *
 * Input: nums = [3,2,1,5]
 * Output: 6
 * Explanation: The maximum possible bitwise OR of a subset is 7. There are 6 subsets with
 * a bitwise OR of 7:
 * - [3,5]
 * - [3,1,5]
 * - [3,2,5]
 * - [3,2,1,5]
 * - [2,1,5]
 * - [2,5]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 16
 * 1 <= nums[i] <= 10^5
 *
 */
public class CountNumberOfMaximumBitwiseOrSubsets {

    // V0
    // IDEA: DP OVER REACHABLE OR VALUES (COUNT OF SUBSETS PER OR VALUE)
    /**
     *  DP def:
     *     - waysByOr[v] = number of subsets (of the elements processed so far) whose OR is v
     *
     *  DP eq, for each element x:
     *     next[v | x] += waysByOr[v]   (take x)
     *     next[v]     += waysByOr[v]   (skip x)
     *
     *  Init: waysByOr[0] = 1 (the empty subset).
     *  The maximum OR is the OR of every element (adding an element only sets bits),
     *  and since every nums[i] >= 1 the empty subset never reaches it.
     *
     *  time = O(N * D), D = number of distinct reachable OR values
     *  space = O(D)
     */
    public int countMaxOrSubsets(int[] nums) {
        int target = 0;
        for (int num : nums) {
            target |= num;
        }

        Map<Integer, Integer> waysByOr = new HashMap<>();
        waysByOr.put(0, 1);

        for (int num : nums) {
            // start from "skip num": every existing subset stays as it is
            Map<Integer, Integer> next = new HashMap<>(waysByOr);
            // "take num": each existing subset extends to OR value (orValue | num)
            for (Map.Entry<Integer, Integer> entry : waysByOr.entrySet()) {
                int newOr = entry.getKey() | num;
                next.put(newOr, next.getOrDefault(newOr, 0) + entry.getValue());
            }
            waysByOr = next;
        }
        return waysByOr.getOrDefault(target, 0);
    }

    // V1
    // IDEA: BACKTRACKING (TAKE / SKIP EACH ELEMENT), COUNT LEAVES WHOSE OR HITS THE MAX
    /**
     *  n <= 16, so all 2^n subsets can be enumerated directly.
     *
     *  time = O(2^N)
     *  space = O(N) recursion depth
     */
    public int countMaxOrSubsets_1(int[] nums) {
        int target = 0;
        for (int num : nums) {
            target |= num;
        }
        return countSubsets(nums, 0, 0, target);
    }

    /** Number of ways to choose from nums[index..] so the final OR equals target. */
    private int countSubsets(int[] nums, int index, int currentOr, int target) {
        if (index == nums.length) {
            return currentOr == target ? 1 : 0;
        }
        int take = countSubsets(nums, index + 1, currentOr | nums[index], target);
        int skip = countSubsets(nums, index + 1, currentOr, target);
        return take + skip;
    }
}
