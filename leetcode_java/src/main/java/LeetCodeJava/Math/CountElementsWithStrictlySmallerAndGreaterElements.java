package LeetCodeJava.Math;

// https://leetcode.com/problems/count-elements-with-strictly-smaller-and-greater-elements/description/

/**
 * 2148. Count Elements With Strictly Smaller and Greater Elements
 * Easy
 *
 * Given an integer array nums, return the number of elements that have both a strictly smaller and a strictly greater element appear in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [11,7,2,15]
 * Output: 2
 * Explanation: The element 7 has the element 2 strictly smaller than it and the element 11 strictly greater than it.
 * Element 11 has element 7 strictly smaller than it and element 15 strictly greater than it.
 * In total there are 2 elements having both a strictly smaller and a strictly greater element appear in nums.
 *
 * Example 2:
 *
 * Input: nums = [-3,3,3,90]
 * Output: 2
 * Explanation: The element 3 has the element -3 strictly smaller than it and the element 90 strictly greater than it.
 * Since there are two elements with the value 3, in total there are 2 elements having both a strictly smaller and a strictly greater element appear in nums.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * -10^5 <= nums[i] <= 10^5
 *
 */
public class CountElementsWithStrictlySmallerAndGreaterElements {

    // V0
    // IDEA: ANYTHING STRICTLY BETWEEN THE GLOBAL MIN AND MAX QUALIFIES
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countElements(int[] nums) {
        int minValue = Integer.MAX_VALUE;
        int maxValue = Integer.MIN_VALUE;
        for (int num : nums) {
            minValue = Math.min(minValue, num);
            maxValue = Math.max(maxValue, num);
        }

        // when all values are equal, nothing is strictly inside (min, max)
        int count = 0;
        for (int num : nums) {
            if (num > minValue && num < maxValue) {
                count++;
            }
        }
        return count;
    }
}
