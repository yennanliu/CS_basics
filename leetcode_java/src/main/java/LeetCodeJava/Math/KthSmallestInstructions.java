package LeetCodeJava.Math;

// https://leetcode.com/problems/kth-smallest-instructions/description/

/**
 * 1643. Kth Smallest Instructions
 * Hard
 *
 * Bob is standing at cell (0, 0), and he wants to reach destination: (row, column). He can only travel right and down. You are going to help Bob by providing instructions for him to reach destination.
 *
 * The instructions are represented as a string, where each character is either:
 *
 * 'H', meaning move horizontally (go right), or
 * 'V', meaning move vertically (go down).
 *
 * Multiple instructions will lead Bob to destination. For example, if destination is (2, 3), both "HHHVV" and "HVHVH" are valid instructions.
 *
 * However, Bob is very picky. Bob has a lucky number k, and he wants the kth lexicographically smallest instructions that will lead him to destination. k is 1-indexed.
 *
 * Given an integer array destination and an integer k, return the kth lexicographically smallest instructions that will take Bob to destination.
 *
 *
 * Example 1:
 *
 * Input: destination = [2,3], k = 1
 * Output: "HHHVV"
 * Explanation: All the instructions that reach (2, 3) in lexicographic order are as follows:
 * ["HHHVV", "HHVHV", "HHVVH", "HVHHV", "HVHVH", "HVVHH", "VHHHV", "VHHVH", "VHVHH", "VVHHH"].
 *
 * Example 2:
 *
 * Input: destination = [2,3], k = 2
 * Output: "HHVHV"
 *
 * Example 3:
 *
 * Input: destination = [2,3], k = 3
 * Output: "HHVVH"
 *
 *
 * Constraints:
 *
 * destination.length == 2
 * 1 <= row, column <= 15
 * 1 <= k <= nCr(row + column, row), where nCr(a, b) denotes a choose b.
 *
 */
public class KthSmallestInstructions {

    // V0
    // IDEA: COMBINATORIAL COUNTING, BUILD THE ANSWER GREEDILY LEFT TO RIGHT
    //   'H' < 'V', so try 'H' first. Committing to 'H' leaves (h - 1) H's and v V's,
    //   i.e. C(h - 1 + v, h - 1) completions - all of them before any string with 'V' here.
    //   k <= that count -> take 'H'; otherwise take 'V' and skip the block (k -= count).
    //   when no H is left, the rest is forced to 'V'.
    /**
     * time = O((R + C)^2)
     * space = O(R + C)
     */
    public String kthSmallestPath(int[] destination, int k) {
        int vertical = destination[0];
        int horizontal = destination[1];
        StringBuilder sb = new StringBuilder();

        int steps = vertical + horizontal;
        for (int step = 0; step < steps; step++) {
            if (horizontal == 0) {
                sb.append('V');
                vertical--;
                continue;
            }
            // number of paths that start with 'H' from here
            long withH = combination(horizontal - 1 + vertical, horizontal - 1);
            if (k <= withH) {
                sb.append('H');
                horizontal--;
            } else {
                sb.append('V');
                vertical--;
                k -= (int) withH;
            }
        }
        return sb.toString();
    }

    /** n choose r, exact (n <= 30 here, so it fits in a long). */
    private long combination(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }
        r = Math.min(r, n - r);
        long result = 1;
        for (int i = 0; i < r; i++) {
            // result * (n - i) is divisible by (i + 1): it is C(n, i + 1) * (i + 1)
            result = result * (n - i) / (i + 1);
        }
        return result;
    }
}
