package LeetCodeJava.Design;

// https://leetcode.com/problems/design-neighbor-sum-service/

import java.util.HashMap;
import java.util.Map;

/**
 *  3242. Design Neighbor Sum Service
 *  Easy
 *
 *  You are given a n x n 2D array grid containing distinct elements in the range
 *  [0, n^2 - 1].
 *
 *  Implement the NeighborSum class:
 *
 *   - NeighborSum(int[][] grid) initializes the object.
 *   - int adjacentSum(int value) returns the sum of elements which are adjacent
 *     neighbors of value, that is either to the top, left, right, or bottom of value
 *     in grid.
 *   - int diagonalSum(int value) returns the sum of elements which are diagonal
 *     neighbors of value, that is either to the top-left, top-right, bottom-left, or
 *     bottom-right of value in grid.
 *
 *  Example 1:
 *    Input:
 *      ["NeighborSum","adjacentSum","adjacentSum","diagonalSum","diagonalSum"]
 *      [[[[0,1,2],[3,4,5],[6,7,8]]],[1],[4],[4],[8]]
 *    Output: [null, 6, 16, 16, 4]
 *    Explanation:
 *      the adjacent neighbors of 1 are 0, 2, and 4      -> 6
 *      the adjacent neighbors of 4 are 1, 3, 5, and 7   -> 16
 *      the diagonal neighbors of 4 are 0, 2, 6, and 8   -> 16
 *      the diagonal neighbor of 8 is 4                  -> 4
 *
 *  Example 2:
 *    Input:
 *      ["NeighborSum","adjacentSum","diagonalSum"]
 *      [[[[1,2,0,3],[4,7,15,6],[8,9,10,11],[12,13,14,5]]],[15],[9]]
 *    Output: [null, 23, 45]
 *
 *  Constraints:
 *    3 <= n == grid.length == grid[i].length <= 10
 *    0 <= grid[i][j] <= n^2 - 1
 *    All grid[i][j] are distinct.
 *    value in adjacentSum and diagonalSum will be in the range [0, n^2 - 1].
 *    At most 2 * n^2 calls will be made to adjacentSum and diagonalSum.
 */
public class DesignNeighborSumService {

    // V0
    // IDEA: INDEX THE VALUES ONCE, THEN EACH QUERY IS FOUR BOUNDED LOOKUPS
    //
    //   the values are DISTINCT, so a map from value to its (row, col) is well defined
    //   and can be built once in the constructor. after that both queries walk the same
    //   four offsets -- differing only in WHICH set of offsets -- and skip whatever
    //   falls off the board.
    /**
     * time = O(N^2) to build, O(1) per query
     * space = O(N^2)
     */
    private static final int[][] ADJACENT = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    private static final int[][] DIAGONAL = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    private final int[][] grid;
    private final int n;
    private final Map<Integer, int[]> pos = new HashMap<>();

    public DesignNeighborSumService(int[][] grid) {
        this.grid = grid;
        this.n = grid.length;
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                pos.put(grid[i][j], new int[]{i, j});
            }
        }
    }

    private int sum(int value, int[][] offsets) {
        int[] rc = pos.get(value);
        int total = 0;
        for (int[] d : offsets) {
            int a = rc[0] + d[0];
            int b = rc[1] + d[1];
            if (a >= 0 && a < this.n && b >= 0 && b < this.n) {
                total += this.grid[a][b];
            }
        }
        return total;
    }

    public int adjacentSum(int value) {
        return sum(value, ADJACENT);
    }

    public int diagonalSum(int value) {
        return sum(value, DIAGONAL);
    }
}
