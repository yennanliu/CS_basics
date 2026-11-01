package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/rearrange-array-elements-by-sign/description/

/**
 * 2149. Rearrange Array Elements by Sign
 * Medium
 *
 * You are given a 0-indexed integer array nums of even length consisting of an equal number of positive and negative integers.
 *
 * You should rearrange the elements of nums such that the modified array follows the given conditions:
 *
 * Every consecutive pair of integers have opposite signs.
 * For all integers with the same sign, the order in which they were present in nums is preserved.
 * The rearranged array begins with a positive integer.
 *
 * Return the modified array after rearranging the elements to satisfy the aforementioned conditions.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,1,-2,-5,2,-4]
 * Output: [3,-2,1,-5,2,-4]
 * Explanation:
 * The positive integers in nums are [3,1,2]. The negative integers are [-2,-5,-4].
 * The only possible way to rearrange them such that they satisfy all conditions is [3,-2,1,-5,2,-4].
 * Other ways such as [1,-2,2,-5,3,-4], [3,1,2,-2,-5,-4], [-2,3,-5,1,-4,2] are incorrect because they do not satisfy one or more conditions.
 *
 * Example 2:
 *
 * Input: nums = [-1,1]
 * Output: [1,-1]
 * Explanation:
 * 1 is the only positive integer and -1 the only negative integer in nums.
 * So nums is rearranged to [1,-1].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 2 * 10^5
 * nums.length is even
 * 1 <= |nums[i]| <= 10^5
 * nums consists of equal number of positive and negative integers.
 *
 */
public class RearrangeArrayElementsBySign {

    // V0
    // IDEA: TWO WRITE POINTERS — EVEN SLOTS FOR POSITIVES, ODD SLOTS FOR NEGATIVES
    //  the layout is fixed (positive at 0, 2, 4, ...; negative at 1, 3, 5, ...) and the order
    //  within each sign is kept, so one pass drops each value into the next slot of its sign.
    /**
     * time = O(N)
     * space = O(N)   (the output)
     */
    public int[] rearrangeArray(int[] nums) {
        int[] result = new int[nums.length];
        int nextPositive = 0;
        int nextNegative = 1;
        for (int x : nums) {
            if (x > 0) {
                result[nextPositive] = x;
                nextPositive += 2;
            } else {
                result[nextNegative] = x;
                nextNegative += 2;
            }
        }
        return result;
    }
}
