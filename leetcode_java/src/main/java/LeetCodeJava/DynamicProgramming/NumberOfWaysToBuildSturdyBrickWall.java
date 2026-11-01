package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-build-sturdy-brick-wall/description/

import java.util.*;

/**
 * 2184. Number of Ways to Build Sturdy Brick Wall
 * Medium
 *
 * You are given integers height and width which specify the dimensions of a brick wall you are building. You are also given a 0-indexed array of unique integers bricks, where the ith brick has a height of 1 and a width of bricks[i]. You have an infinite supply of each type of brick and bricks may not be rotated.
 *
 * Each row in the wall must be exactly width units long. For the wall to be sturdy, adjacent rows in the wall should not join bricks at the same location, except at the ends of the wall.
 *
 * Return the number of ways to build a sturdy wall. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Example 1:
 *
 * Input: height = 2, width = 3, bricks = [1,2]
 * Output: 2
 * Explanation:
 * The first two walls in the diagram show the only two ways to build a sturdy brick wall.
 * Note that the third wall in the diagram is not sturdy because adjacent rows join bricks 2 units from the left.
 *
 * Example 2:
 *
 * Input: height = 1, width = 1, bricks = [5]
 * Output: 0
 * Explanation:
 * There are no ways to build a sturdy wall because the only type of brick we have is longer than the width of the wall.
 *
 * Constraints:
 *
 * 1 <= height <= 100
 * 1 <= width <= 10
 * 1 <= bricks.length <= 10
 * 1 <= bricks[i] <= 10
 * All the values of bricks are unique.
 *
 */
public class NumberOfWaysToBuildSturdyBrickWall {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: BITMASK DP OVER ROW JOINT PATTERNS — TWO ROWS STACK IFF THEIR JOINT MASKS ARE DISJOINT
    /**
     * time = O(H * R^2), R = number of distinct rows (<= 2^(W-1))
     * space = O(R)
     */
    public int buildWall(int height, int width, int[] bricks) {
        // every row is described by its set of internal joint offsets 1..width-1
        List<Integer> rowMasks = new ArrayList<>();
        buildRows(0, 0, width, bricks, rowMasks);

        int rowCount = rowMasks.size();
        if (rowCount == 0) {
            return 0;
        }

        // dp[r] = number of walls of the current height whose top row is rowMasks[r]
        long[] dp = new long[rowCount];
        Arrays.fill(dp, 1);

        for (int level = 1; level < height; level++) {
            long[] next = new long[rowCount];
            for (int top = 0; top < rowCount; top++) {
                for (int below = 0; below < rowCount; below++) {
                    // sturdy: joints of adjacent rows must never line up
                    boolean disjoint = (rowMasks.get(top) & rowMasks.get(below)) == 0;
                    if (disjoint) {
                        next[top] = (next[top] + dp[below]) % MOD;
                    }
                }
            }
            dp = next;
        }

        long total = 0;
        for (long ways : dp) {
            total = (total + ways) % MOD;
        }
        return (int) total;
    }

    /** DFS over brick choices; records the joint mask of every row that fills width exactly. */
    private void buildRows(int length, int mask, int width, int[] bricks, List<Integer> rowMasks) {
        if (length == width) {
            rowMasks.add(mask);
            return;
        }
        for (int brick : bricks) {
            int newLength = length + brick;
            if (newLength > width) {
                continue;
            }
            // a joint only exists strictly inside the row, not at the right edge
            int newMask = mask;
            if (newLength < width) {
                newMask = mask | (1 << (newLength - 1));
            }
            buildRows(newLength, newMask, width, bricks, rowMasks);
        }
    }
}
