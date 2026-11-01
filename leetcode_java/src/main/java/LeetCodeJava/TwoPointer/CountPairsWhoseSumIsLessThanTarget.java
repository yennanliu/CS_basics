package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/count-pairs-whose-sum-is-less-than-target/description/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 2824. Count Pairs Whose Sum is Less than Target
 * Easy
 *
 * Given a 0-indexed integer array nums of length n and an integer target, return the number of
 * pairs (i, j) where 0 <= i < j < n and nums[i] + nums[j] < target.
 *
 *
 * Example 1:
 *
 * Input: nums = [-1,1,2,3,1], target = 2
 * Output: 3
 *
 * Example 2:
 *
 * Input: nums = [-6,2,5,-2,-7,-1,3], target = -2
 * Output: 10
 *
 *
 * Constraints:
 *
 * 1 <= nums.length == n <= 50
 * -50 <= nums[i], target <= 50
 *
 */
public class CountPairsWhoseSumIsLessThanTarget {

    // V0
    // IDEA: SORT + TWO POINTERS
    //  the count only depends on the values, so sorting is free; if sorted[left] + sorted[right]
    //  < target then left pairs with every index in (left, right], all at once.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int countPairs(List<Integer> nums, int target) {
        List<Integer> sorted = new ArrayList<>(nums);
        Collections.sort(sorted);

        int left = 0;
        int right = sorted.size() - 1;
        int count = 0;
        while (left < right) {
            if (sorted.get(left) + sorted.get(right) < target) {
                // left pairs with each of left+1 .. right (all values <= sorted[right])
                count += right - left;
                left++;
            } else {
                // sorted[right] is too big for every remaining left partner
                right--;
            }
        }
        return count;
    }

    // V1
    // IDEA: BRUTE FORCE OVER ALL PAIRS (fine here: n <= 50)
    /**
     * time = O(N^2)
     * space = O(1)
     */
    public int countPairs_1(List<Integer> nums, int target) {
        int n = nums.size();
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums.get(i) + nums.get(j) < target) {
                    count++;
                }
            }
        }
        return count;
    }
}
