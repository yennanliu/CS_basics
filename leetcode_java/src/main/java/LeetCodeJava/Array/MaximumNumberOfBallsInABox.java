package LeetCodeJava.Array;

// https://leetcode.com/problems/maximum-number-of-balls-in-a-box/description/
/**
 * 1742. Maximum Number of Balls in a Box
 * Easy
 *
 * You are working in a ball factory where you have n balls numbered from lowLimit up to highLimit
 * inclusive (i.e., n == highLimit - lowLimit + 1), and an infinite number of boxes numbered from 1
 * to infinity.
 *
 * Your job at this factory is to put each ball in the box with a number equal to the sum of digits
 * of the ball's number. For example, the ball number 321 will be put in the box number
 * 3 + 2 + 1 = 6 and the ball number 10 will be put in the box number 1 + 0 = 1.
 *
 * Given two integers lowLimit and highLimit, return the number of balls in the box with the most
 * balls.
 *
 *
 * Example 1:
 *
 * Input: lowLimit = 1, highLimit = 10
 * Output: 2
 * Explanation: Box 1 has the most number of balls with 2 balls.
 *
 * Example 2:
 *
 * Input: lowLimit = 5, highLimit = 15
 * Output: 2
 * Explanation: Boxes 5 and 6 have the most number of balls with 2 balls in each.
 *
 * Example 3:
 *
 * Input: lowLimit = 19, highLimit = 28
 * Output: 2
 * Explanation: Box 10 has the most number of balls with 2 balls.
 *
 *
 * Constraints:
 *
 * 1 <= lowLimit <= highLimit <= 10^5
 *
 */
public class MaximumNumberOfBallsInABox {

    // highLimit <= 10^5 has at most 6 digits, so a digit sum is at most 9 * 6 = 54
    private static final int MAX_DIGIT_SUM = 54;

    // V0
    // IDEA: COUNTING BY DIGIT SUM (the box index space is tiny, so a fixed array beats a map)
    /**
     * time = O(N * log10(highLimit))
     * space = O(1)
     */
    public int countBalls(int lowLimit, int highLimit) {
        // boxCount[s] = balls whose digit sum is s
        int[] boxCount = new int[MAX_DIGIT_SUM + 1];
        int most = 0;
        for (int ball = lowLimit; ball <= highLimit; ball++) {
            int box = digitSum(ball);
            boxCount[box]++;
            most = Math.max(most, boxCount[box]);
        }
        return most;
    }

    /** Sum of the decimal digits of a non-negative number. */
    private int digitSum(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }
}
