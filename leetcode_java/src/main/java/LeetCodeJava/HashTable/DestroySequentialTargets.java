package LeetCodeJava.HashTable;

// https://leetcode.com/problems/destroy-sequential-targets/description/

import java.util.*;

/**
 * 2453. Destroy Sequential Targets
 * Medium
 *
 * You are given a 0-indexed array nums consisting of positive integers, representing targets on a number line. You are also given an integer space.
 *
 * You have a machine which can destroy targets. Seeding the machine with some nums[i] allows it to destroy all targets with values that can be represented as nums[i] + c * space, where c is any non-negative integer. You want to destroy the maximum number of targets in nums.
 *
 * Return the minimum value of nums[i] you can seed the machine with to destroy the maximum number of targets.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,7,8,1,1,5], space = 2
 * Output: 1
 * Explanation: If we seed the machine with nums[3], then we destroy all targets equal to 1,3,5,7,9,...
 * In this case, we would destroy 5 total targets (all except for nums[2]).
 * It is impossible to destroy more than 5 targets, so we return nums[3].
 *
 * Example 2:
 *
 * Input: nums = [1,3,5,2,4,6], space = 2
 * Output: 1
 * Explanation: Seeding the machine with nums[0], or nums[3], or nums[4] will destroy 3 targets.
 * It is not possible to destroy more than 3 targets.
 * Since nums[0] is the minimal integer that can destroy 3 targets, we return 1.
 *
 * Example 3:
 *
 * Input: nums = [6,2,5], space = 100
 * Output: 2
 * Explanation: Whatever initial seed we select, we can only destroy 1 target. The minimal seed is nums[1].
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= space <= 10^9
 *
 */
public class DestroySequentialTargets {

    // V0
    // IDEA: COUNT RESIDUE CLASSES MOD space; ANSWER = SMALLEST MEMBER OF A LARGEST CLASS
    /**
     * time = O(N)
     * space = O(N)
     */
    public int destroyTargets(int[] nums, int space) {
        // seeding with v destroys every target >= v with the same remainder,
        // so seeding with the smallest member destroys its whole class
        Map<Integer, Integer> classSize = new HashMap<>();
        int largest = 0;
        for (int num : nums) {
            int remainder = num % space;
            int size = classSize.getOrDefault(remainder, 0) + 1;
            classSize.put(remainder, size);
            largest = Math.max(largest, size);
        }

        // among the largest classes, the smallest seed wins the tie
        int bestSeed = Integer.MAX_VALUE;
        for (int num : nums) {
            if (classSize.get(num % space) == largest && num < bestSeed) {
                bestSeed = num;
            }
        }
        return bestSeed;
    }
}
