package LeetCodeJava.Sort;

// https://leetcode.com/problems/count-odd-numbers-in-an-interval-range/

/**
 *  1523. Count Odd Numbers in an Interval Range
 *  Easy
 *
 *  Given two non-negative integers low and high. Return the count of odd numbers
 *  between low and high (inclusive).
 *
 *  Example 1:
 *    Input: low = 3, high = 7
 *    Output: 3
 *    Explanation: the odd numbers between 3 and 7 are [3,5,7].
 *
 *  Example 2:
 *    Input: low = 8, high = 10
 *    Output: 1
 *    Explanation: the odd number between 8 and 10 is [9].
 *
 *  Constraints:
 *    0 <= low <= high <= 10^9
 */
public class CountOddNumbersInAnIntervalRange {

    // V0
    // IDEA: MATH / PREFIX COUNT (no loop needed)
    //       let f(x) = number of odd values in [0, x] = (x + 1) / 2
    //       answer = f(high) - f(low - 1) = (high + 1) / 2 - low / 2
    //       writing it as `- low / 2` instead of `- (low - 1 + 1) / 2` also keeps
    //       low = 0 safe (no negative operand).
    /**
     * time = O(1)
     * space = O(1)
     */
    public int countOdds(int low, int high) {
        return (high + 1) / 2 - low / 2;
    }
}
