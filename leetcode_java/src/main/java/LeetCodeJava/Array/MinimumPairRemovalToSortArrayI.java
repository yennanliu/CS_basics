package LeetCodeJava.Array;

// https://leetcode.com/problems/minimum-pair-removal-to-sort-array-i/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3507. Minimum Pair Removal to Sort Array I
 * Easy
 *
 * Given an array nums, you can perform the following operation any number of
 * times:
 *
 * Select the adjacent pair with the minimum sum in nums. If multiple such pairs
 * exist, choose the leftmost one.
 *
 * Replace the pair with their sum.
 *
 * Return the minimum number of operations needed to make the array non-decreasing.
 *
 * An array is said to be non-decreasing if each element is greater than or equal
 * to its previous element (if it exists).
 *
 * Example 1:
 *
 * Input: nums = [5,2,3,1]
 *
 * Output: 2
 *
 * Explanation:
 *
 * The pair (3,1) has the minimum sum of 4. After replacement, nums = [5,2,4].
 *
 * The pair (2,4) has the minimum sum of 6. After replacement, nums = [5,6].
 *
 * The array nums became non-decreasing in two operations.
 *
 * Example 2:
 *
 * Input: nums = [1,2,2]
 *
 * Output: 0
 *
 * Explanation:
 *
 * The array nums is already sorted.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 50
 *
 * -1000 <= nums[i] <= 1000
 *
 */
public class MinimumPairRemovalToSortArrayI {

    // V0
    // IDEA: SIMULATION (n <= 50; at most n - 1 merges, each re-scanning for the leftmost min-sum pair)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minimumPairRemoval(int[] nums) {
        List<Integer> arr = new ArrayList<>();
        for (int value : nums) {
            arr.add(value);
        }

        int operations = 0;
        while (!isNonDecreasing(arr)) {
            // find the leftmost adjacent pair with the smallest sum (strict < keeps the leftmost)
            int bestIndex = 0;
            int bestSum = arr.get(0) + arr.get(1);
            for (int i = 1; i + 1 < arr.size(); i++) {
                int pairSum = arr.get(i) + arr.get(i + 1);
                if (pairSum < bestSum) {
                    bestSum = pairSum;
                    bestIndex = i;
                }
            }

            // replace the pair with its sum
            arr.set(bestIndex, bestSum);
            arr.remove(bestIndex + 1);
            operations++;
        }
        return operations;
    }

    /** Returns true if every element is <= the next one. */
    private boolean isNonDecreasing(List<Integer> arr) {
        for (int i = 0; i + 1 < arr.size(); i++) {
            if (arr.get(i) > arr.get(i + 1)) {
                return false;
            }
        }
        return true;
    }
}
