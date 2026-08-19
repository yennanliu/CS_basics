package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/tiling-a-rectangle-with-the-fewest-squares/

/**
 *  1240. Tiling a Rectangle with the Fewest Squares
 *  Hard
 *
 *  Given a rectangle of size n x m, return the minimum number of integer-sided
 *  squares that tile the rectangle.
 *
 *  Example 1:
 *    Input: n = 2, m = 3
 *    Output: 3
 *    Explanation: 3 squares are necessary to cover the rectangle.
 *                 2 (squares of 1x1) + 1 (square of 2x2)
 *
 *  Example 2:
 *    Input: n = 5, m = 8
 *    Output: 5
 *
 *  Example 3:
 *    Input: n = 11, m = 13
 *    Output: 6
 *
 *  Constraints:
 *    1 <= n, m <= 13
 */
public class TilingARectangleWithTheFewestSquares {

    private int n;
    private int m;
    private int[] filled; // filled[i] is a bitmask of row i ; bit j set = (i, j) covered
    private int ans;

    // V0
    // IDEA: BACKTRACKING + BITMASK (fill the first empty cell, scan row by row)
    //       - always work on the FIRST empty cell (i, j) in reading order
    //       - the square we place must have its top-left corner exactly there
    //       - enumerate the side length w, bounded by how far we can go down (r) and
    //         right (c) before hitting a filled cell
    //       - prune with `t + 1 >= ans` (can't beat the current best)
    //
    //       NOTE : we GROW the square from side 1 up to mx. going from side w-1 to
    //              side w only needs the new bottom row + right column of the square
    //              to be marked, so the marking is incremental (no re-marking), and
    //              the whole mx x mx block is undone once at the end.
    /**
     * time = exponential (heavily pruned) ; n, m <= 13
     * space = O(n + recursion depth)
     */
    public int tilingRectangle(int n, int m) {
        this.n = n;
        this.m = m;
        this.filled = new int[n];
        this.ans = n * m; // worst case: all 1x1 squares
        dfs(0, 0, 0);
        return ans;
    }

    private void dfs(int i, int j, int t) {
        // end of row -> go to next row
        if (j == m) {
            i += 1;
            j = 0;
        }
        // all rows done -> t is a complete tiling (better than ans by pruning)
        if (i == n) {
            ans = t;
            return;
        }
        // already covered -> move right
        if (((filled[i] >> j) & 1) == 1) {
            dfs(i, j + 1, t);
            return;
        }
        if (t + 1 >= ans) {
            return;
        }

        // how far down / right is free from (i, j)
        int r = 0;
        int c = 0;
        for (int k = i; k < n; k++) {
            if (((filled[k] >> j) & 1) == 1) {
                break;
            }
            r++;
        }
        for (int k = j; k < m; k++) {
            if (((filled[i] >> k) & 1) == 1) {
                break;
            }
            c++;
        }
        int mx = Math.min(r, c);

        for (int w = 1; w <= mx; w++) {
            for (int k = 0; k < w; k++) {
                filled[i + w - 1] |= 1 << (j + k);
                filled[i + k] |= 1 << (j + w - 1);
            }
            dfs(i, j + w, t + 1);
        }

        // undo : the whole mx x mx block was marked by the loop above
        for (int x = i; x < i + mx; x++) {
            for (int y = j; y < j + mx; y++) {
                filled[x] ^= 1 << y;
            }
        }
    }
}
