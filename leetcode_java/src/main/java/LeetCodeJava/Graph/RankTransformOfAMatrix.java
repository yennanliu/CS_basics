package LeetCodeJava.Graph;

// https://leetcode.com/problems/rank-transform-of-a-matrix/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 1632. Rank Transform of a Matrix
 * Hard
 *
 * Given an m x n matrix, return a new matrix answer where answer[row][col] is the
 * rank of matrix[row][col].
 *
 * The rank is an integer that represents how large an element is compared to other
 * elements. It is calculated using the following rules:
 *
 * - The rank is an integer starting from 1.
 * - If two elements p and q are in the same row or column, then:
 *     If p < q then rank(p) < rank(q)
 *     If p == q then rank(p) == rank(q)
 *     If p > q then rank(p) > rank(q)
 * - The rank should be as small as possible.
 *
 * The test cases are generated so that answer is unique under the given rules.
 *
 *
 * Example 1:
 *
 * Input: matrix = [[1,2],[3,4]]
 * Output: [[1,2],[2,3]]
 *
 * Example 2:
 *
 * Input: matrix = [[7,7],[7,7]]
 * Output: [[1,1],[1,1]]
 *
 * Example 3:
 *
 * Input: matrix = [[20,-21,14],[-19,4,19],[22,-47,24],[-19,4,19]]
 * Output: [[4,2,3],[1,3,4],[5,1,6],[1,3,4]]
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 500
 * -10^9 <= matrix[row][col] <= 10^9
 *
 */
public class RankTransformOfAMatrix {

    // V0
    // IDEA: PROCESS VALUES IN INCREASING ORDER + UNION FIND ON ROWS/COLUMNS
    //       rowMax[i] / colMax[j] = largest rank already given in that row / column.
    //       For one value, cells sharing a row or column must get the SAME rank, and
    //       that chains transitively: union row node i with column node (m + j) for
    //       each cell. A component gets rank = 1 + max(rowMax, colMax) over its cells.
    //       Reset the touched union-find nodes before the next value.
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int[][] matrixRankTransform(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        // value -> cells holding it, iterated in ascending value order
        TreeMap<Integer, List<int[]>> cellsByValue = new TreeMap<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cellsByValue.computeIfAbsent(matrix[r][c], k -> new ArrayList<>()).add(new int[] {r, c});
            }
        }

        // nodes 0..rows-1 are rows, rows..rows+cols-1 are columns
        int[] parent = new int[rows + cols];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        int[] rowMax = new int[rows];
        int[] colMax = new int[cols];
        int[][] answer = new int[rows][cols];

        for (List<int[]> cells : cellsByValue.values()) {
            // 1) link every row/column that holds this value together
            for (int[] cell : cells) {
                union(parent, cell[0], rows + cell[1]);
            }

            // 2) a component's rank must exceed every rank already in its rows/columns
            Map<Integer, Integer> componentRank = new HashMap<>();
            for (int[] cell : cells) {
                int root = find(parent, cell[0]);
                int needed = Math.max(rowMax[cell[0]], colMax[cell[1]]);
                componentRank.put(root, Math.max(componentRank.getOrDefault(root, 0), needed));
            }

            // 3) assign the rank and update the row/column maxima
            for (int[] cell : cells) {
                int rank = componentRank.get(find(parent, cell[0])) + 1;
                answer[cell[0]][cell[1]] = rank;
                rowMax[cell[0]] = rank;
                colMax[cell[1]] = rank;
            }

            // 4) reset only the nodes we touched so groups do not leak into the next value
            for (int[] cell : cells) {
                parent[cell[0]] = cell[0];
                parent[rows + cell[1]] = rows + cell[1];
            }
        }
        return answer;
    }

    /** Root of x's set, with path halving. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    /** Merges the sets holding a and b. */
    private void union(int[] parent, int a, int b) {
        int rootA = find(parent, a);
        int rootB = find(parent, b);
        if (rootA != rootB) {
            parent[rootA] = rootB;
        }
    }
}
