package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-the-number-of-k-free-subsets/description/

import java.util.*;

/**
 * 2638. Count the Number of K-Free Subsets
 * Medium
 *
 * You are given an integer array nums, which contains distinct elements and an integer k.
 *
 * A subset is called a k-Free subset if it contains no two elements with an absolute difference
 * equal to k. Notice that the empty set is a k-Free subset.
 *
 * Return the number of k-Free subsets of nums.
 *
 * A subset of an array is a selection of elements (possibly none) of the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,4,6], k = 1
 * Output: 5
 * Explanation: There are 5 valid subsets: {}, {5}, {4}, {6} and {4, 6}.
 *
 * Example 2:
 *
 * Input: nums = [2,3,5,8], k = 5
 * Output: 12
 * Explanation: There are 12 valid subsets: {}, {2}, {3}, {5}, {8}, {2, 3}, {2, 3, 5}, {2, 5}, {2, 5, 8}, {2, 8}, {3, 5} and {5, 8}.
 *
 * Example 3:
 *
 * Input: nums = [10,5,9,11], k = 20
 * Output: 16
 * Explanation: All subsets are valid. Since the total count of subsets is 2^4 = 16, so the answer is 16.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 50
 * 1 <= nums[i] <= 1000
 * 1 <= k <= 1000
 *
 */
public class CountTheNumberOfKFreeSubsets {

    // V0
    // IDEA: GROUP BY value % k, SORT, THEN HOUSE ROBBER ALONG EACH CHAIN OF VALUES THAT DIFFER BY EXACTLY k
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long countTheNumOfKFreeSubsets(int[] nums, int k) {
        // only values in the same remainder class mod k can differ by exactly k
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int num : nums) {
            groups.computeIfAbsent(num % k, key -> new ArrayList<>()).add(num);
        }

        // classes (and broken chains inside a class) are independent, so their counts multiply
        long result = 1;
        for (List<Integer> values : groups.values()) {
            Collections.sort(values);

            // notPicked / picked = ways for the chain so far, by whether the current value is chosen
            long notPicked = 1;
            long picked = 1;
            for (int i = 1; i < values.size(); i++) {
                if (values.get(i) - values.get(i - 1) == k) {
                    // adjacent in the chain: cannot pick both
                    long newNotPicked = notPicked + picked;
                    long newPicked = notPicked;
                    notPicked = newNotPicked;
                    picked = newPicked;
                } else {
                    // the chain ends here; fold it in and start a new one
                    result *= (notPicked + picked);
                    notPicked = 1;
                    picked = 1;
                }
            }
            result *= (notPicked + picked);
        }
        return result;
    }
}
