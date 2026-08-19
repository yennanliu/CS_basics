package LeetCodeJava.Math;

// https://leetcode.com/problems/random-flip-matrix/

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *  519. Random Flip Matrix
 *  Medium
 *
 *  There is an m x n binary grid matrix with all the values set to 0 initially.
 *  Design an algorithm to randomly pick an index (i, j) where matrix[i][j] == 0 and
 *  flip it to 1. All the indices (i, j) where matrix[i][j] == 0 should be equally likely
 *  to be returned.
 *
 *  Optimize your algorithm to minimize the number of calls made to the built-in random
 *  function of your language and optimize the time and space complexity.
 *
 *  Implement the Solution class:
 *    - Solution(int m, int n) initializes the object with the size of the binary matrix
 *      m and n.
 *    - int[] flip() returns a random index [i, j] of the matrix where matrix[i][j] == 0
 *      and flips it to 1.
 *    - void reset() resets all the values of the matrix to be 0.
 *
 *  Example 1:
 *    Input: ["Solution", "flip", "flip", "flip", "reset", "flip"]
 *           [[3, 1], [], [], [], [], []]
 *    Output: [null, [1, 0], [2, 0], [0, 0], null, [2, 0]]
 *
 *  Constraints:
 *    1 <= m, n <= 10^4
 *    There will be at least one free cell for each call to flip.
 *    At most 1000 calls will be made to flip and reset.
 */
public class RandomFlipMatrix {

    private final int rows;
    private final int cols;
    private int remaining;
    private final Map<Integer, Integer> lookup; // "virtual index" -> real index (Fisher-Yates swap map)
    private final Random rand;

    public RandomFlipMatrix(int m, int n) {
        this.rows = m;
        this.cols = n;
        this.remaining = m * n;
        this.lookup = new HashMap<>();
        this.rand = new Random();
    }

    // V0
    // IDEA: virtual Fisher-Yates - pick a random idx in [0, remaining), read its (possibly
    //       remapped) value, then swap the last unused slot into it. Only the swapped
    //       entries are stored, so space is O(#flips) instead of O(m*n).
    /**
     * time = O(1)
     * space = O(number of flip calls)
     */
    public int[] flip() {
        this.remaining--;
        int target = this.rand.nextInt(this.remaining + 1);
        Integer mapped = this.lookup.get(target);
        int real = (mapped == null) ? target : mapped;

        Integer lastMapped = this.lookup.get(this.remaining);
        this.lookup.put(target, (lastMapped == null) ? this.remaining : lastMapped);

        return new int[] { real / this.cols, real % this.cols };
    }

    /**
     * time = O(number of flips since last reset)
     * space = O(1)
     */
    public void reset() {
        this.remaining = this.rows * this.cols;
        this.lookup.clear();
    }
}
