package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-operations-to-remove-adjacent-ones-in-matrix/description/

import java.util.*;

/**
 * 2123. Minimum Operations to Remove Adjacent Ones in Matrix
 * Hard
 *
 * You are given a 0-indexed binary matrix grid. In one operation, you can flip any 1 in grid to be 0.
 *
 * A binary matrix is well-isolated if there is no 1 in the matrix that is 4-directionally connected
 * (i.e., horizontal and vertical) to another 1.
 *
 * Return the minimum number of operations to make grid well-isolated.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1,0],[0,1,1],[1,1,1]]
 * Output: 3
 * Explanation: Use 3 operations to change grid[0][1], grid[1][2], and grid[2][1] to 0.
 * After, no more 1's are 4-directionally connected and grid is well-isolated.
 *
 * Example 2:
 *
 * Input: grid = [[0,0,0],[0,0,0],[0,0,0]]
 * Output: 0
 *
 * Example 3:
 *
 * Input: grid = [[0,1],[1,0]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 300
 * grid[i][j] is either 0 or 1.
 *
 */
public class MinimumOperationsToRemoveAdjacentOnesInMatrix {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: MINIMUM VERTEX COVER ON A BIPARTITE GRAPH = MAXIMUM MATCHING (KONIG)
    // node = a cell holding 1, edge = two adjacent 1s. Removing 1s until none touch means
    // deleting a set of nodes that touches every edge: a minimum vertex cover. Colouring cells
    // by (r + c) parity makes the grid bipartite, and by Konig's theorem its minimum vertex
    // cover equals its maximum matching -> run Kuhn's augmenting paths from every even 1.
    // NOTE: the augmenting-path DFS is iterative -- up to 45,000 even cells could otherwise
    // recurse deep enough to overflow the stack.
    /**
     * time = O(V * E)
     * space = O(V + E)
     */
    public int minimumOperations(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        int cellCount = rows * cols;

        // neighbours[cell] = odd-parity 1-cells adjacent to an even-parity 1-cell (id = r * cols + c)
        List<List<Integer>> neighbours = new ArrayList<>();
        List<Integer> evenCells = new ArrayList<>();
        for (int i = 0; i < cellCount; i++) {
            neighbours.add(new ArrayList<>());
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] != 1 || (r + c) % 2 != 0) {
                    continue;
                }
                int cell = r * cols + c;
                evenCells.add(cell);
                for (int[] dir : DIRECTIONS) {
                    int nr = r + dir[0];
                    int nc = c + dir[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 1) {
                        neighbours.get(cell).add(nr * cols + nc);
                    }
                }
            }
        }

        // matchOf[oddCell] = even cell it is matched with, -1 = free
        int[] matchOf = new int[cellCount];
        Arrays.fill(matchOf, -1);
        // seenStamp[oddCell] == search id -> already tried in this search (no per-search reset)
        int[] seenStamp = new int[cellCount];
        int[] nextEdge = new int[cellCount];

        int matching = 0;
        int searchId = 0;
        for (int root : evenCells) {
            searchId++;
            if (augment(root, searchId, neighbours, matchOf, seenStamp, nextEdge)) {
                matching++;
            }
        }
        return matching;
    }

    /** Iterative Kuhn search for an augmenting path from `root`; flips it if found. */
    private boolean augment(int root, int searchId, List<List<Integer>> neighbours,
                            int[] matchOf, int[] seenStamp, int[] nextEdge) {
        // path of even cells, and the odd cell used to step from pathEven[k] to pathEven[k + 1]
        List<Integer> pathEven = new ArrayList<>();
        List<Integer> viaOdd = new ArrayList<>();
        pathEven.add(root);
        nextEdge[root] = 0;

        while (!pathEven.isEmpty()) {
            int depth = pathEven.size() - 1;
            int even = pathEven.get(depth);
            List<Integer> options = neighbours.get(even);

            // this even cell has no untried neighbour: back up one step
            if (nextEdge[even] == options.size()) {
                pathEven.remove(depth);
                if (depth > 0) {
                    viaOdd.remove(depth - 1);
                }
                continue;
            }

            int odd = options.get(nextEdge[even]);
            nextEdge[even]++;
            if (seenStamp[odd] == searchId) {
                continue;
            }
            seenStamp[odd] = searchId;

            if (matchOf[odd] == -1) {
                // free odd cell: flip the whole path so every even cell takes its next odd cell
                matchOf[odd] = even;
                for (int k = depth - 1; k >= 0; k--) {
                    matchOf[viaOdd.get(k)] = pathEven.get(k);
                }
                return true;
            }

            // odd cell is taken: try to re-seat its current partner
            int partner = matchOf[odd];
            viaOdd.add(odd);
            pathEven.add(partner);
            nextEdge[partner] = 0;
        }
        return false;
    }
}
