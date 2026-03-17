package LeetCodeJava.Array;

// https://leetcode.com/problems/range-sum-query-2d-mutable/description/
// https://leetcode.ca/all/308.html
/**
 *  308. Range Sum Query 2D - Mutable
 * Given a 2D matrix matrix, find the sum of the elements inside the rectangle defined by its upper left corner (row1, col1) and lower right corner (row2, col2).
 *
 * Range Sum Query 2D
 * The above rectangle (with the red border) is defined by (row1, col1) = (2, 1) and (row2, col2) = (4, 3), which contains sum = 8.
 *
 * Example:
 *
 * Given matrix = [
 *   [3, 0, 1, 4, 2],
 *   [5, 6, 3, 2, 1],
 *   [1, 2, 0, 1, 5],
 *   [4, 1, 0, 1, 7],
 *   [1, 0, 3, 0, 5]
 * ]
 *
 * sumRegion(2, 1, 4, 3) -> 8
 * update(3, 2, 2)
 * sumRegion(2, 1, 4, 3) -> 10
 * Note:
 *
 * The matrix is only modifiable by the update function.
 * You may assume the number of calls to update and sumRegion function is distributed evenly.
 * You may assume that row1 ≤ row2 and col1 ≤ col2.
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Facebook Google Microsoft
 * Problem Solution
 * 308-Range-Sum-Query-2D-Mutable-
 * All Problems:
 *
 */
public class RangeSumQuery2DMutable {

    // V0
//    class NumMatrix {
//        private BinaryIndexedTree[] trees;
//
//        public NumMatrix(int[][] matrix) {
//        }
//
//        public void update(int row, int col, int val) {
//        }
//
//        public int sumRegion(int row1, int col1, int row2, int col2) {
//        }
//    }


    // V0-1
    // IDEA: BRUTE FORCE (TLE) (fixed by gpt)
    class NumMatrix_0_1 {

        int[][] matrix;
        int l;
        int w;

        public NumMatrix_0_1(int[][] matrix) {
            if (matrix.length == 0 || matrix[0].length == 0) {
                return;
            }

            this.matrix = matrix;
            this.l = matrix.length;
            this.w = matrix[0].length;
        }

        public void update(int row, int col, int val) {
            matrix[row][col] = val;
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {

            int res = 0;

            for (int y = row1; y <= row2; y++) {
                for (int x = col1; x <= col2; x++) {
                    res += matrix[y][x];
                }
            }

            return res;
        }
    }


    // V0-2
    // IDEA: BIT (2D BINARY INDEXED TREE) (gemini)
    class NumMatrix_0_2 {
        private int[][] tree; // 2D Binary Indexed Tree
        private int[][] nums; // Original matrix to calculate "delta"
        private int m, n;

        public NumMatrix_0_2(int[][] matrix) {
            if (matrix.length == 0 || matrix[0].length == 0) return;
            m = matrix.length;
            n = matrix[0].length;
            tree = new int[m + 1][n + 1];
            nums = new int[m][n];

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    update(i, j, matrix[i][j]);
                }
            }
        }

        public void update(int row, int col, int val) {
            int delta = val - nums[row][col];
            nums[row][col] = val;

            // Update the BIT: O(log M * log N)
            for (int i = row + 1; i <= m; i += i & -i) {
                for (int j = col + 1; j <= n; j += j & -j) {
                    tree[i][j] += delta;
                }
            }
        }

        private int query(int row, int col) {
            int sum = 0;
            // Query the BIT: O(log M * log N)
            for (int i = row + 1; i > 0; i -= i & -i) {
                for (int j = col + 1; j > 0; j -= j & -j) {
                    sum += tree[i][j];
                }
            }
            return sum;
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // 2D Inclusion-Exclusion Principle
            return query(row2, col2)
                    - query(row1 - 1, col2)
                    - query(row2, col1 - 1)
                    + query(row1 - 1, col1 - 1);
        }
    }


    // V1
    // https://leetcode.ca/2016-10-03-308-Range-Sum-Query-2D-Mutable/
    class BinaryIndexedTree {
        private int n;
        private int[] c;

        public BinaryIndexedTree(int n) {
            this.n = n;
            c = new int[n + 1];
        }

        public void update(int x, int delta) {
            while (x <= n) {
                c[x] += delta;
                x += lowbit(x);
            }
        }

        public int query(int x) {
            int s = 0;
            while (x > 0) {
                s += c[x];
                x -= lowbit(x);
            }
            return s;
        }


        // ????
        //        public static int lowbit(int x) {
        //            return x & -x;
        //        }
        public int lowbit(int x) {
            return x & -x;
        }
    }

    class NumMatrix_1 {
        private BinaryIndexedTree[] trees;

        public NumMatrix_1(int[][] matrix) {
            int m = matrix.length;
            int n = matrix[0].length;
            trees = new BinaryIndexedTree[m];
            for (int i = 0; i < m; ++i) {
                BinaryIndexedTree tree = new BinaryIndexedTree(n);
                for (int j = 0; j < n; ++j) {
                    tree.update(j + 1, matrix[i][j]);
                }
                trees[i] = tree;
            }
        }

        public void update(int row, int col, int val) {
            BinaryIndexedTree tree = trees[row];
            int prev = tree.query(col + 1) - tree.query(col);
            tree.update(col + 1, val - prev);
        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            int s = 0;
            for (int i = row1; i <= row2; ++i) {
                BinaryIndexedTree tree = trees[i];
                s += tree.query(col2 + 1) - tree.query(col1);
            }
            return s;
        }
    }



    // V2


    // V3




}
