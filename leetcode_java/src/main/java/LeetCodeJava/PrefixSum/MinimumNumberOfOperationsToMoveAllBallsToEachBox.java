package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/minimum-number-of-operations-to-move-all-balls-to-each-box/description/

/**
 * 1769. Minimum Number of Operations to Move All Balls to Each Box
 * Medium
 *
 * You have n boxes. You are given a binary string boxes of length n, where
 * boxes[i] is '0' if the i^th box is empty, and '1' if it contains one ball.
 *
 * In one operation, you can move one ball from a box to an adjacent box. Box i
 * is adjacent to box j if abs(i - j) == 1. Note that after doing so, there may
 * be more than one ball in some boxes.
 *
 * Return an array answer of size n, where answer[i] is the minimum number of
 * operations needed to move all the balls to the i^th box.
 *
 * Each answer[i] is calculated considering the initial state of the boxes.
 *
 *
 * Example 1:
 *
 * Input: boxes = "110"
 * Output: [1,1,3]
 * Explanation: The answer for each box is as follows:
 * 1) First box: you will have to move one ball from the second box to the first box in one operation.
 * 2) Second box: you will have to move one ball from the first box to the second box in one operation.
 * 3) Third box: you will have to move one ball from the first box to the third box in two operations,
 *    and move one ball from the second box to the third box in one operation.
 *
 * Example 2:
 *
 * Input: boxes = "001011"
 * Output: [11,8,5,4,3,4]
 *
 *
 * Constraints:
 *
 * n == boxes.length
 * 1 <= n <= 2000
 * boxes[i] is either '0' or '1'.
 *
 */
public class MinimumNumberOfOperationsToMoveAllBallsToEachBox {

    // V0
    // IDEA: PREFIX SUM IN BOTH DIRECTIONS (cost from the left + cost from the right)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] minOperations(String boxes) {
        int n = boxes.length();
        // leftCost[i] = cost of pulling every ball left of i to box i
        int[] leftCost = new int[n];
        // rightCost[i] = cost of pulling every ball right of i to box i
        int[] rightCost = new int[n];

        // moving the target one box to the right makes every ball on the left one step further
        int ballsOnLeft = 0;
        for (int i = 1; i < n; i++) {
            if (boxes.charAt(i - 1) == '1') {
                ballsOnLeft++;
            }
            leftCost[i] = leftCost[i - 1] + ballsOnLeft;
        }

        // mirror image for the balls on the right
        int ballsOnRight = 0;
        for (int i = n - 2; i >= 0; i--) {
            if (boxes.charAt(i + 1) == '1') {
                ballsOnRight++;
            }
            rightCost[i] = rightCost[i + 1] + ballsOnRight;
        }

        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = leftCost[i] + rightCost[i];
        }
        return answer;
    }

    // V1
    // IDEA: BRUTE FORCE (sum |i - j| over every ball j) -- fine for n <= 2000
    /**
     * time = O(N^2)
     * space = O(1) extra (output excluded)
     */
    public int[] minOperations_1(String boxes) {
        int n = boxes.length();
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            int total = 0;
            for (int j = 0; j < n; j++) {
                if (boxes.charAt(j) == '1') {
                    // each ball needs |i - j| single-step moves
                    total += Math.abs(i - j);
                }
            }
            answer[i] = total;
        }
        return answer;
    }
}
