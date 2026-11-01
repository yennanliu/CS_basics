package LeetCodeJava.Greedy;

// https://leetcode.com/problems/number-of-rectangles-that-can-form-the-largest-square/description/

/**
 * 1725. Number Of Rectangles That Can Form The Largest Square
 * Easy
 *
 * You are given an array rectangles where rectangles[i] = [li, wi] represents the
 * ith rectangle of length li and width wi.
 *
 * You can cut the ith rectangle to form a square with a side length of k if both
 * k <= li and k <= wi. For example, if you have a rectangle [4,6], you can cut it to
 * get a square with a side length of at most 4.
 *
 * Let maxLen be the side length of the largest square you can obtain from any of
 * the given rectangles.
 *
 * Return the number of rectangles that can make a square with a side length of
 * maxLen.
 *
 *
 * Example 1:
 *
 * Input: rectangles = [[5,8],[3,9],[5,12],[16,5]]
 * Output: 3
 *
 * Example 2:
 *
 * Input: rectangles = [[2,3],[3,7],[4,3],[3,7]]
 * Output: 3
 *
 *
 * Constraints:
 *
 * 1 <= rectangles.length <= 1000
 * rectangles[i].length == 2
 * 1 <= li, wi <= 10^9
 * li != wi
 *
 */
public class NumberOfRectanglesThatCanFormTheLargestSquare {

    // V0
    // IDEA: RUNNING MAX + COUNT (SINGLE PASS)
    //       the biggest square from [l, w] has side min(l, w); track the max side
    //       and how many rectangles reach it.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int countGoodRectangles(int[][] rectangles) {
        int maxSide = 0;
        int count = 0;
        for (int[] rectangle : rectangles) {
            int side = Math.min(rectangle[0], rectangle[1]);
            if (side > maxSide) {
                // new record
                maxSide = side;
                count = 1;
            } else if (side == maxSide) {
                count++;
            }
        }
        return count;
    }
}
