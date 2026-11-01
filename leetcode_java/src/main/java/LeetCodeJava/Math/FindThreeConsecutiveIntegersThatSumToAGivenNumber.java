package LeetCodeJava.Math;

// https://leetcode.com/problems/find-three-consecutive-integers-that-sum-to-a-given-number/description/

/**
 * 2177. Find Three Consecutive Integers That Sum to a Given Number
 * Medium
 *
 * Given an integer num, return three consecutive integers (as a sorted array) that sum to num. If num cannot be expressed as the sum of three consecutive integers, return an empty array.
 *
 *
 * Example 1:
 *
 * Input: num = 33
 * Output: [10,11,12]
 * Explanation: 33 can be expressed as 10 + 11 + 12 = 33.
 * 10, 11, 12 are 3 consecutive integers, so we return [10, 11, 12].
 *
 * Example 2:
 *
 * Input: num = 4
 * Output: []
 * Explanation: There is no way to express 4 as the sum of 3 consecutive integers.
 *
 *
 * Constraints:
 *
 * 0 <= num <= 10^15
 *
 */
public class FindThreeConsecutiveIntegersThatSumToAGivenNumber {

    // V0
    // IDEA: MATH - THREE CONSECUTIVE INTEGERS (m-1) + m + (m+1) = 3m, SO num MUST BE DIVISIBLE BY 3
    /**
     * time = O(1)
     * space = O(1)
     */
    public long[] sumOfThree(long num) {
        // the sum of three consecutive integers is always a multiple of 3
        if (num % 3 != 0) {
            return new long[0];
        }

        // the middle term is num / 3; its neighbours are one below and one above
        long middle = num / 3;
        return new long[] { middle - 1, middle, middle + 1 };
    }
}
