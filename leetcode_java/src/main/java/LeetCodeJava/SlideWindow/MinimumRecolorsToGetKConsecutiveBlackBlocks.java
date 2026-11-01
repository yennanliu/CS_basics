package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-recolors-to-get-k-consecutive-black-blocks/description/

/**
 * 2379. Minimum Recolors to Get K Consecutive Black Blocks
 * Easy
 *
 * You are given a 0-indexed string blocks of length n, where blocks[i] is either 'W' or 'B', representing the color of the ith block. The characters 'W' and 'B' denote the colors white and black, respectively.
 *
 * You are also given an integer k, which is the desired number of consecutive black blocks.
 *
 * In one operation, you can recolor a white block such that it becomes a black block.
 *
 * Return the minimum number of operations needed such that there is at least one occurrence of k consecutive black blocks.
 *
 *
 * Example 1:
 *
 * Input: blocks = "WBBWWBBWBW", k = 7
 * Output: 3
 * Explanation:
 * One way to achieve 7 consecutive black blocks is to recolor the 0th, 3rd, and 4th blocks
 * so that blocks = "BBBBBBBWBW".
 * It can be shown that there is no way to achieve 7 consecutive black blocks in less than 3 operations.
 * Therefore, we return 3.
 *
 * Example 2:
 *
 * Input: blocks = "WBWBBBW", k = 2
 * Output: 0
 * Explanation:
 * No changes need to be made, since 2 consecutive black blocks already exist.
 * Therefore, we return 0.
 *
 *
 * Constraints:
 *
 * n == blocks.length
 * 1 <= n <= 100
 * blocks[i] is either 'W' or 'B'.
 * 1 <= k <= n
 *
 */
public class MinimumRecolorsToGetKConsecutiveBlackBlocks {

    // V0
    // IDEA: FIXED-WIDTH SLIDING WINDOW — MINIMISE THE WHITES INSIDE
    //  any k consecutive blocks become all black by recolouring exactly the whites in them,
    //  so the answer is the fewest 'W' over all windows of width k.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minimumRecolors(String blocks, int k) {
        // whites in the first window blocks[0 .. k - 1]
        int whites = 0;
        for (int i = 0; i < k; i++) {
            if (blocks.charAt(i) == 'W') {
                whites++;
            }
        }

        int best = whites;
        for (int i = k; i < blocks.length(); i++) {
            // blocks[i - k] leaves the window, blocks[i] enters it
            if (blocks.charAt(i - k) == 'W') {
                whites--;
            }
            if (blocks.charAt(i) == 'W') {
                whites++;
            }
            best = Math.min(best, whites);
        }
        return best;
    }
}
