package LeetCodeJava.HashTable;

// https://leetcode.com/problems/identify-the-largest-outlier-in-an-array/description/

import java.util.*;

/**
 * 3371. Identify the Largest Outlier in an Array
 * Medium
 *
 * You are given an integer array nums. This array contains n elements, where exactly n - 2 elements are special numbers. One of the remaining two elements is the sum of these special numbers, and the other is an outlier.
 *
 * An outlier is defined as a number that is neither one of the original special numbers nor the element representing the sum of those numbers.
 *
 * Note that special numbers, the sum element, and the outlier must have distinct indices, but may share the same value.
 *
 * Return the largest potential outlier in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3,5,10]
 * Output: 10
 * Explanation:
 * The special numbers could be 2 and 3, thus making their sum 5 and the outlier 10.
 *
 * Example 2:
 *
 * Input: nums = [-2,-1,-3,-6,4]
 * Output: 4
 * Explanation:
 * The special numbers could be -2, -1, and -3, thus making their sum -6 and the outlier 4.
 *
 * Example 3:
 *
 * Input: nums = [1,1,1,1,1,5,5]
 * Output: 5
 * Explanation:
 * The special numbers could be 1, 1, 1, 1, and 1, thus making their sum 5 and the outlier 5.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * -1000 <= nums[i] <= 1000
 * The input is generated such that at least one potential outlier exists.
 *
 */
public class IdentifyTheLargestOutlierInAnArray {

    // V0
    // IDEA: TRY EACH VALUE AS THE SUM ELEMENT - THE OUTLIER FALLS OUT OF THE TOTAL
    //   if s is the sum element and o the outlier, the special numbers add up
    //   to s, so total = 2 * s + o, i.e. o = total - 2 * s. try every distinct
    //   value as s and check o is present; when o == s they must sit at
    //   different indices, so that value needs at least two copies.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int getLargestOutlier(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();
        int total = 0;
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
            total += num;
        }

        int best = Integer.MIN_VALUE;
        for (int sumElement : count.keySet()) {
            int outlier = total - 2 * sumElement;
            int needed = (outlier == sumElement) ? 2 : 1;
            if (count.getOrDefault(outlier, 0) >= needed) {
                best = Math.max(best, outlier);
            }
        }
        // the problem guarantees at least one outlier exists
        return best;
    }
}
