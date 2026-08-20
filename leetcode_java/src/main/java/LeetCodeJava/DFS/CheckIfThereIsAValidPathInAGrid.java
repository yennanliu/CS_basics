package LeetCodeJava.DFS;

// https://leetcode.com/problems/check-if-there-is-a-valid-path-in-a-grid/

/**
 *  1391. Check if There is a Valid Path in a Grid
 *  Medium
 *
 *  You are given an m x n grid. Each cell of grid represents a street.
 *  The street of grid[i][j] can be:
 *    1 which means a street connecting the left cell and the right cell.
 *    2 which means a street connecting the upper cell and the lower cell.
 *    3 which means a street connecting the left cell and the lower cell.
 *    4 which means a street connecting the right cell and the lower cell.
 *    5 which means a street connecting the left cell and the upper cell.
 *    6 which means a street connecting the right cell and the upper cell.
 *
 *  You will initially start at the street of the upper-left cell (0, 0). A valid path
 *  in the grid is a path that starts from the upper left cell (0, 0) and ends at the
 *  bottom-right cell (m - 1, n - 1). The path should only follow the streets.
 *  You are not allowed to change any street.
 *
 *  Return true if there is a valid path in the grid or false otherwise.
 *
 *  Example 1:
 *    Input: grid = [[2,4,3],[6,5,2]]
 *    Output: true
 *
 *  Example 2:
 *    Input: grid = [[1,2,1],[1,2,1]]
 *    Output: false
 *    Explanation: the street at cell (0, 0) is not connected with any other street.
 *
 *  Constraints:
 *    m == grid.length
 *    n == grid[i].length
 *    1 <= m, n <= 300
 *    1 <= grid[i][j] <= 6
 */
public class CheckIfThereIsAValidPathInAGrid {

    // V0
    // IDEA: UNION FIND (a street type = a fixed pair of open sides)
    //       encode each street as the set of directions it opens onto
    //         1 -> L,R   2 -> U,D   3 -> L,D   4 -> R,D   5 -> L,U   6 -> R,U
    //       two adjacent cells are really connected only when BOTH open toward each
    //       other, so union (i, j) with a neighbour ONLY when the neighbour's street
    //       contains the opposite direction.
    //       NOTE: checking only one side is the classic bug -> a "1" next to a "2"
    //             would look joined, but neither actually opens to the other.
    //       answer = cell 0 and cell m*n-1 land in the same component.
    /**
     * time = O(m * n * alpha(m * n))
     * space = O(m * n)
     */

    // direction index: 0 = U, 1 = D, 2 = L, 3 = R
    private static final int[] DR = {-1, 1, 0, 0};
    private static final int[] DC = {0, 0, -1, 1};
    private static final int[] OPP = {1, 0, 3, 2};

    // opens[type][dir] : does street `type` open toward `dir` ?
    private static final boolean[][] OPENS = {
            {false, false, false, false},   // unused (type 0)
            {false, false, true,  true},    // 1 : L, R
            {true,  true,  false, false},   // 2 : U, D
            {false, true,  true,  false},   // 3 : L, D
            {false, true,  false, true},    // 4 : R, D
            {true,  false, true,  false},   // 5 : L, U
            {true,  false, false, true}     // 6 : R, U
    };

    private int[] parent;

    public boolean hasValidPath(int[][] grid) {

        int m = grid.length;
        int n = grid[0].length;

        parent = new int[m * n];
        for (int i = 0; i < m * n; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int type = grid[i][j];
                for (int d = 0; d < 4; d++) {
                    if (!OPENS[type][d]) {
                        continue;
                    }
                    int ni = i + DR[d];
                    int nj = j + DC[d];
                    if (ni < 0 || ni >= m || nj < 0 || nj >= n) {
                        continue;
                    }
                    // the neighbour must open back toward us
                    if (!OPENS[grid[ni][nj]][OPP[d]]) {
                        continue;
                    }
                    union(i * n + j, ni * n + nj);
                }
            }
        }

        return find(0) == find(m * n - 1);
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path halving
            x = parent[x];
        }
        return x;
    }

    private void union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra != rb) {
            parent[ra] = rb;
        }
    }
}
