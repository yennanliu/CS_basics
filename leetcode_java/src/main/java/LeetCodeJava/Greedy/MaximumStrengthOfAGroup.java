package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-strength-of-a-group/description/

import java.util.*;

/**
 * 2708. Maximum Strength of a Group
 * Medium
 *
 * You are given a 0-indexed integer array nums representing the score of students in an exam. The teacher would like to form one non-empty group of students with maximal strength, where the strength of a group of students of indices i0, i1, i2, ... , ik is defined as nums[i0] * nums[i1] * nums[i2] * ... * nums[ik].
 *
 * Return the maximum strength of a group the teacher can create.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,-1,-5,2,5,-9]
 * Output: 1350
 * Explanation: One way to form a group of maximal strength is to group the students at indices [0,2,3,4,5]. Their strength is 3 * (-5) * 2 * 5 * (-9) = 1350, which we can show is optimal.
 *
 * Example 2:
 *
 * Input: nums = [-4,-5,-4]
 * Output: 20
 * Explanation: Group the students at indices [0, 1] . Then, we'll have a resulting strength of 20. We cannot achieve greater strength.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 13
 * -9 <= nums[i] <= 9
 *
 */
public class MaximumStrengthOfAGroup {

    // V0
    // IDEA: GREEDY (take every positive, pair up the negatives)
    //       positives always help, zeros never do, negatives help in pairs:
    //       take all negatives, and if their count is odd drop the one closest to 0.
    //       edge cases: a single element is its own answer; if nothing was picked, a 0 exists -> 0.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maxStrength(int[] nums) {
        // the group must be non-empty, so a lone element is the answer even if negative / zero
        if (nums.length == 1) {
            return nums[0];
        }

        List<Integer> positives = new ArrayList<>();
        List<Integer> negatives = new ArrayList<>();
        for (int x : nums) {
            if (x > 0) {
                positives.add(x);
            } else if (x < 0) {
                negatives.add(x);
            }
        }

        // ascending: the negative closest to 0 is last
        Collections.sort(negatives);
        if (negatives.size() % 2 == 1) {
            negatives.remove(negatives.size() - 1);
        }

        if (positives.isEmpty() && negatives.isEmpty()) {
            // only zeros (plus at most one dropped negative) -> best group is a single 0
            return 0;
        }

        // long: up to 13 factors of 9
        long product = 1;
        for (int x : positives) {
            product *= x;
        }
        for (int x : negatives) {
            product *= x;
        }
        return product;
    }

    // V1
    // IDEA: BRUTE FORCE OVER EVERY NON-EMPTY SUBSET (BITMASK)
    //       n <= 13 means at most 8191 subsets, so trying them all is cheap and needs no
    //       case analysis. good as a cross-check of V0; V0 is the one to explain in an interview.
    /**
     * time = O(2^N * N)
     * space = O(1)
     */
    public long maxStrength_1(int[] nums) {
        int n = nums.length;
        long best = Long.MIN_VALUE;

        // each mask from 1 to 2^n - 1 is one non-empty group; bit i set = nums[i] is in it
        for (int mask = 1; mask < (1 << n); mask++) {
            long product = 1;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    product *= nums[i];
                }
            }
            best = Math.max(best, product);
        }
        return best;
    }
}
