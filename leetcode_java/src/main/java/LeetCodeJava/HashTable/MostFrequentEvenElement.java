package LeetCodeJava.HashTable;

// https://leetcode.com/problems/most-frequent-even-element/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2404. Most Frequent Even Element
 * Easy
 *
 * Given an integer array nums, return the most frequent even element.
 *
 * If there is a tie, return the smallest one. If there is no such element, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,2,2,4,4,1]
 * Output: 2
 * Explanation:
 * The even elements are 0, 2, 2, 4, and 4. Of these, 2 and 4 appear the most.
 * We return the smallest one, which is 2.
 *
 * Example 2:
 *
 * Input: nums = [4,4,4,9,2,4]
 * Output: 4
 * Explanation: 4 is the even element appears the most.
 *
 * Example 3:
 *
 * Input: nums = [29,47,21,41,13,37,25,7]
 * Output: -1
 * Explanation: There is no even element.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 2000
 * 0 <= nums[i] <= 10^5
 *
 */
public class MostFrequentEvenElement {

    // V0
    // IDEA: COUNT ONLY THE EVEN VALUES, ARGMAX WITH A SMALLEST-VALUE TIE-BREAK
    /**
     * time = O(N)
     * space = O(N)
     */
    public int mostFrequentEven(int[] nums) {
        Map<Integer, Integer> evenCount = new HashMap<>();
        for (int num : nums) {
            if (num % 2 == 0) {
                evenCount.put(num, evenCount.getOrDefault(num, 0) + 1);
            }
        }

        // -1 survives when there is no even element at all
        int bestValue = -1;
        int bestCount = 0;
        for (Map.Entry<Integer, Integer> entry : evenCount.entrySet()) {
            int value = entry.getKey();
            int count = entry.getValue();
            boolean moreFrequent = count > bestCount;
            boolean tieButSmaller = count == bestCount && value < bestValue;
            if (moreFrequent || tieButSmaller) {
                bestValue = value;
                bestCount = count;
            }
        }
        return bestValue;
    }
}
