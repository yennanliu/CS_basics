package LeetCodeJava.Math;

// https://leetcode.com/problems/elimination-game/

/**
 *  390. Elimination Game
 *  Medium
 *
 *  You have a list arr of all integers in the range [1, n] sorted in a strictly
 *  increasing order. Apply the following algorithm on arr:
 *
 *   - Starting from left to right, remove the first number and every other number
 *     afterward until you reach the end of the list.
 *   - Repeat the previous step again, but this time from right to left, remove the
 *     rightmost number and every other number from the remaining numbers.
 *   - Keep repeating the steps again, alternating left to right and right to left,
 *     until a single number remains.
 *
 *  Given the integer n, return the last number that remains in arr.
 *
 *  Example 1:
 *
 *  Input: n = 9
 *  Output: 6
 *  Explanation:
 *  arr = [1, 2, 3, 4, 5, 6, 7, 8, 9] -> [2, 4, 6, 8] -> [2, 6] -> [6]
 *
 *  Example 2:
 *
 *  Input: n = 1
 *  Output: 1
 *
 *  Constraints:
 *
 *  1 <= n <= 10^9
 */
public class EliminationGame {

    // V0
    // IDEA: only track the head of the remaining (arithmetic) sequence.
    //       The head moves forward by `step` when we scan left-to-right, or when we
    //       scan right-to-left with an odd count of remaining elements.
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int lastRemaining(int n) {
        int head = 1;
        int step = 1;
        boolean leftToRight = true;
        int remaining = n;
        while (remaining > 1) {
            if (leftToRight || remaining % 2 == 1) {
                head += step;
            }
            remaining /= 2;
            step *= 2;
            leftToRight = !leftToRight;
        }
        return head;
    }
}
