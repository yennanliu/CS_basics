package LeetCodeJava.Sort;

// https://leetcode.com/problems/sort-the-matrix-diagonally/description/

import java.util.*;

/**
 *  1329. Sort the Matrix Diagonally
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * A matrix diagonal is a diagonal line of cells starting from some cell in either the topmost row or leftmost column and going in the bottom-right direction until reaching the matrix's end. For example, the matrix diagonal starting from mat[2][0], where mat is a 6 x 3 matrix, includes cells mat[2][0], mat[3][1], and mat[4][2].
 *
 * Given an m x n matrix mat of integers, sort each matrix diagonal in ascending order and return the resulting matrix.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: mat = [[3,3,1,1],[2,2,1,2],[1,1,1,2]]
 * Output: [[1,1,1,1],[1,2,2,2],[1,2,3,3]]
 * Example 2:
 *
 * Input: mat = [[11,25,66,1,69,7],[23,55,17,45,15,52],[75,31,36,44,58,8],[22,27,33,25,68,4],[84,28,14,11,5,50]]
 * Output: [[5,17,4,1,52,7],[11,11,25,45,8,69],[14,23,25,44,58,15],[22,27,31,36,50,66],[84,28,75,33,55,68]]
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 100
 * 1 <= mat[i][j] <= 100
 *
 */
public class SortTheMatrixDiagonally {

    // V0
//    public int[][] diagonalSort(int[][] mat) {
//
//    }



    // V1


    // V2
    // IDEA: PQ + HASHMAP
    // https://leetcode.com/problems/sort-the-matrix-diagonally/solutions/489749/javacpython-sort-on-diagonal-by-lee215-e60n/
    public int[][] diagonalSort_2(int[][] A) {
        int m = A.length, n = A[0].length;
        HashMap<Integer, PriorityQueue<Integer>> d = new HashMap<>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                d.putIfAbsent(i - j, new PriorityQueue<>());
                d.get(i - j).add(A[i][j]);
            }
        }
        for (int i = 0; i < m; ++i)
            for (int j = 0; j < n; ++j)
                A[i][j] = d.get(i - j).poll();
        return A;
    }



    // V3
    // https://leetcode.com/problems/sort-the-matrix-diagonally/solutions/7771300/1329-sort-the-matrix-diagonally-by-agupt-ajgv/
    // Refer this for beter understanding:
    // https://www.youtube.com/watch?v=mNWwJQ7_z4Q
    public int[][] diagonalSort(int[][] mat) {

        /*
            <----- Key Idea ------>
            1.  Store each diagonal element in the Map.
            2.  Sort it.
            3.  Again fill it using key to the same matrix.
        */

        int row = mat.length;
        int col = mat[0].length;

        // Add each diagonal entries to Map so that we can sort and fill it again in the same matrix.
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int key = i - j;
                int val = mat[i][j];

                List<Integer> list = map.getOrDefault(key, new ArrayList<>());
                list.add(val);

                map.put(key, list);
            }
        }

        // Sort each diagonal entries.
        for (int key : map.keySet()) {
            Collections.sort(map.get(key));
        }

        // Again fill entires to matrix.
        for (int i = row - 1; i >= 0; i--) {
            for (int j = col - 1; j >= 0; j--) {
                int key = i - j;
                int val = map.get(key).removeLast();
                mat[i][j] = val;
            }
        }

        return mat;
    }






}
