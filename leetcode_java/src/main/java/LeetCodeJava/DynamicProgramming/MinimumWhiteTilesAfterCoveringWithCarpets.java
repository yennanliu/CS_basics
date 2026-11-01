package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-white-tiles-after-covering-with-carpets/description/

/**
 * 2209. Minimum White Tiles After Covering With Carpets
 * Hard
 *
 * You are given a 0-indexed binary string floor, which represents the colors of tiles on a floor:
 *
 * floor[i] = '0' denotes that the ith tile of the floor is colored black.
 * On the other hand, floor[i] = '1' denotes that the ith tile of the floor is colored white.
 *
 * You are also given numCarpets and carpetLen. You have numCarpets black carpets, each of length carpetLen tiles. Cover the tiles with the given carpets such that the number of white tiles still visible is minimum. Carpets may overlap one another.
 *
 * Return the minimum number of white tiles still visible.
 *
 *
 * Example 1:
 *
 * Input: floor = "10110101", numCarpets = 2, carpetLen = 2
 * Output: 2
 * Explanation:
 * The figure above shows one way of covering the tiles with the carpets such that only 2 white tiles are visible.
 * No other way of covering the tiles with the carpets can leave less than 2 white tiles visible.
 *
 * Example 2:
 *
 * Input: floor = "11111", numCarpets = 2, carpetLen = 3
 * Output: 0
 * Explanation:
 * The figure above shows one way of covering the tiles with the carpets such that no white tiles are visible.
 * Note that the carpets are able to overlap one another.
 *
 *
 * Constraints:
 *
 * 1 <= carpetLen <= floor.length <= 1000
 * floor[i] is either '0' or '1'.
 * 1 <= numCarpets <= 1000
 *
 */
public class MinimumWhiteTilesAfterCoveringWithCarpets {

    // V0
    // IDEA: DP ON (CARPETS USED, PREFIX LENGTH) — LEAVE TILE i-1 UNCOVERED OR END A CARPET THERE
    /**
     * time = O(N * K), K = numCarpets
     * space = O(N * K)
     */
    public int minimumWhiteTiles(String floor, int numCarpets, int carpetLen) {
        int n = floor.length();

        // dp[j][i] = fewest visible white tiles among the first i tiles using at most j carpets
        int[][] dp = new int[numCarpets + 1][n + 1];

        // base case: no carpets, every white tile stays visible
        for (int i = 1; i <= n; i++) {
            int white = (floor.charAt(i - 1) == '1') ? 1 : 0;
            dp[0][i] = dp[0][i - 1] + white;
        }

        for (int j = 1; j <= numCarpets; j++) {
            for (int i = 1; i <= n; i++) {
                int white = (floor.charAt(i - 1) == '1') ? 1 : 0;

                // option 1: leave tile i-1 uncovered
                int leave = dp[j][i - 1] + white;

                // option 2: a carpet ends at tile i-1, covering [i - carpetLen, i - 1]
                int cover = dp[j - 1][Math.max(0, i - carpetLen)];

                dp[j][i] = Math.min(leave, cover);
            }
        }
        return dp[numCarpets][n];
    }
}
