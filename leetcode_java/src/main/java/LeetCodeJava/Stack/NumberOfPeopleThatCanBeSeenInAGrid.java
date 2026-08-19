package LeetCodeJava.Stack;

// https://leetcode.com/problems/number-of-people-that-can-be-seen-in-a-grid/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  2282. Number of People That Can Be Seen in a Grid
 *  Medium
 *  (premium / locked problem)
 *
 *  You are given an m x n 0-indexed 2D array of positive integers heights where
 *  heights[i][j] is the height of the person standing at position (i, j).
 *
 *  A person standing at position (row1, col1) can see a person standing at
 *  position (row2, col2) if:
 *    - The person at (row2, col2) is to the right or below the person at
 *      (row1, col1). More formally, either row1 == row2 and col1 < col2, or
 *      row1 < row2 and col1 == col2.
 *    - Everyone in between them is shorter than both of them.
 *
 *  Return an m x n 2D array of integers answer where answer[i][j] is the number
 *  of people that the person at position (i, j) can see.
 *
 *  Example 1:
 *    Input: heights = [[3,1,4,2,5]]
 *    Output: [[2,1,2,1,0]]
 *
 *  Example 2:
 *    Input: heights = [[5,1],[3,1],[4,1]]
 *    Output: [[3,1],[2,1],[1,0]]
 *
 *  Constraints:
 *    1 <= heights.length <= 400
 *    1 <= heights[i].length <= 400
 *    1 <= heights[i][j] <= 10^5
 */
public class NumberOfPeopleThatCanBeSeenInAGrid {

    // V0
    // IDEA: MONOTONIC DECREASING STACK, ONCE PER ROW AND ONCE PER COLUMN
    //       the two directions (right / down) are independent, so solve one line
    //       at a time and ADD the two results.
    //       scanning a line BACKWARDS with a non-increasing stack, for the
    //       current person of height h:
    //         - every stacked person strictly SHORTER than h is visible and then
    //           gets blocked by h for anyone further back -> pop and count each
    //         - the first person who is >= h is also visible (one more), but he
    //           blocks everything beyond, so the popping stops there
    //         - if that blocker is EXACTLY equal to h, replace it: the two are
    //           interchangeable as blockers, and keeping both would let a later
    //           person count the same wall twice
    /**
     * time = O(M * N)
     * space = O(max(M, N))   // ignoring the output
     */
    public int[][] seePeople(int[][] heights) {
        int m = heights.length;
        int n = heights[0].length;
        int[][] res = new int[m][n];

        // rows -> looking right
        for (int i = 0; i < m; i++) {
            int[] counts = scan(heights[i]);
            for (int j = 0; j < n; j++) {
                res[i][j] += counts[j];
            }
        }

        // columns -> looking down
        int[] col = new int[m];
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                col[i] = heights[i][j];
            }
            int[] counts = scan(col);
            for (int i = 0; i < m; i++) {
                res[i][j] += counts[i];
            }
        }
        return res;
    }

    /** how many people each position of `line` can see, looking forward */
    private int[] scan(int[] line) {
        int len = line.length;
        int[] counts = new int[len];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int idx = len - 1; idx >= 0; idx--) {
            int h = line[idx];
            int cnt = 0;
            while (!stack.isEmpty() && stack.peek() < h) {
                stack.pop();
                cnt++;
            }
            if (!stack.isEmpty()) {
                cnt++;                       // the blocker itself is visible
                if (stack.peek() == h) {
                    stack.pop();             // equal heights block identically
                }
            }
            stack.push(h);
            counts[idx] = cnt;
        }
        return counts;
    }
}
