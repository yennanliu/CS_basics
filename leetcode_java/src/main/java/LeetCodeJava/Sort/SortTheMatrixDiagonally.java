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



    // V1-1
    // IDEA:  SORT, GROUP (GPT)
    /**  NOTE !!!
     *
     *  Key observation:
     *
     *   ```
     *   elements on the same diagonal have the same value of:
     *
     *   row - col
     *   ```
     *
     *  ->
     *
     *  So we can:
     *
     *  1. group elements by (r - c)
     *  2. sort each diagonal
     *  3. put them back into the matrix
     *
     *
     */
    /**  Dry run
     *
     * Let's dry run the solution for:
     *
     * ```text id="j1m08g"
     * mat =
     * [
     *  [3, 3, 1, 1],
     *  [2, 2, 1, 2],
     *  [1, 1, 1, 2]
     * ]
     * ```
     *
     * ---
     *
     * # Step 1: Group by diagonal `(r - c)`
     *
     * Each diagonal shares the same:
     *
     * ```text id="hh1xjg"
     * r - c
     * ```
     *
     * We traverse the matrix.
     *
     * ---
     *
     * ## Matrix coordinates
     *
     * ```text id="m5s6t9"
     * (0,0)=3   (0,1)=3   (0,2)=1   (0,3)=1
     * (1,0)=2   (1,1)=2   (1,2)=1   (1,3)=2
     * (2,0)=1   (2,1)=1   (2,2)=1   (2,3)=2
     * ```
     *
     * ---
     *
     * ## Compute keys
     *
     * | Position | Value | r-c |
     * | -------- | ----- | --- |
     * | (0,0)    | 3     | 0   |
     * | (0,1)    | 3     | -1  |
     * | (0,2)    | 1     | -2  |
     * | (0,3)    | 1     | -3  |
     * | (1,0)    | 2     | 1   |
     * | (1,1)    | 2     | 0   |
     * | (1,2)    | 1     | -1  |
     * | (1,3)    | 2     | -2  |
     * | (2,0)    | 1     | 2   |
     * | (2,1)    | 1     | 1   |
     * | (2,2)    | 1     | 0   |
     * | (2,3)    | 2     | -1  |
     *
     * ---
     *
     * # Step 2: Build hashmap
     *
     * ```
     * Map<Integer, List<Integer>>
     * ```
     *
     * After collection:
     *
     * ```
     * 0   -> [3, 2, 1]
     * -1  -> [3, 1, 2]
     * -2  -> [1, 2]
     * -3  -> [1]
     * 1   -> [2, 1]
     * 2   -> [1]
     * ```
     *
     * ---
     *
     * # Step 3: Sort each diagonal descending
     *
     * We sort descending because later we remove from the end.
     *
     * ```
     * 0   -> [3, 2, 1]
     * -1  -> [3, 2, 1]
     * -2  -> [2, 1]
     * -3  -> [1]
     * 1   -> [2, 1]
     * 2   -> [1]
     * ```
     *
     * ---
     *
     * # Step 4: Refill matrix
     *
     * We traverse again row by row.
     *
     * ---
     *
     * ## Cell (0,0)
     *
     * key = 0
     *
     * list:
     *
     * ```
     * [3,2,1]
     * ```
     *
     * Take last:
     *
     * ```
     * 1
     * ```
     *
     * Matrix:
     *
     * ```
     * [
     *  [1, _, _, _],
     *  [_, _, _, _],
     *  [_, _, _, _]
     * ]
     * ```
     *
     * Remaining:
     *
     * ```
     * 0 -> [3,2]
     * ```
     *
     * ---
     *
     * ## Cell (0,1)
     *
     * key = -1
     *
     * list:
     *
     * ```
     * [3,2,1]
     * ```
     *
     * Take last → 1
     *
     * ```
     * [
     *  [1,1,_,_],
     *  [_,_,_,_],
     *  [_,_,_,_]
     * ]
     * ```
     *
     * Remaining:
     *
     * ```
     * -1 -> [3,2]
     * ```
     *
     * ---
     *
     * ## Cell (0,2)
     *
     * key = -2
     *
     * take 1
     *
     * ```
     * [
     *  [1,1,1,_],
     *  [_,_,_,_],
     *  [_,_,_,_]
     * ]
     * ```
     *
     * ---
     *
     * ## Cell (0,3)
     *
     * key = -3
     *
     * take 1
     *
     * ```
     * [
     *  [1,1,1,1],
     *  [_,_,_,_],
     *  [_,_,_,_]
     * ]
     * ```
     *
     * ---
     *
     * Continue similarly...
     *
     * Final matrix:
     *
     * ```
     * [
     *  [1,1,1,1],
     *  [1,2,2,2],
     *  [1,2,3,3]
     * ]
     * ```
     *
     * ---
     *
     * # Why sorting descending works
     *
     * Suppose diagonal values are:
     *
     * ```
     * [3,2,1]
     * ```
     *
     * We want ascending order in matrix:
     *
     * ```
     * 1 -> 2 -> 3
     * ```
     *
     * If we sort descending:
     *
     * ```
     * [3,2,1]
     * ```
     *
     * then repeatedly:
     *
     * ```
     * remove(list.size() - 1)
     * ```
     *
     * gives:
     *
     * ```
     * 1
     * 2
     * 3
     * ```
     *
     * which is ascending.
     *
     * ---
     *
     * # Core idea summary
     *
     * The entire solution depends on this invariant:
     *
     * ```
     * same diagonal => same (row - col)
     * ```
     *
     * Then:
     *
     * 1. group
     * 2. sort
     * 3. refill matrix in traversal order
     *
     *
     */
    public int[][] diagonalSort_1_1(int[][] mat) {

        int m = mat.length;
        int n = mat[0].length;

        /**
         *  map:  { diagonal id : elements }
         */
        // diagonal id -> elements
        Map<Integer, List<Integer>> map = new HashMap<>();

        // 1. collect elements
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {

                /**
                 *  the key is `r -c`,
                 *  since the same diagonal elements
                 *  have the same `r-c`  value
                 */
                int key = r - c;

                map.putIfAbsent(key, new ArrayList<>());
                map.get(key).add(mat[r][c]);
            }
        }


        /**
         *  sort each diagonal `descending`
         */
        // 2. sort each diagonal `descending`
        // so we can remove from end efficiently
        for (List<Integer> list : map.values()) {
            Collections.sort(list, Collections.reverseOrder());
        }

        // 3. write back sorted values
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {

                int key = r - c;
                List<Integer> list = map.get(key);

                mat[r][c] = list.remove(list.size() - 1);
            }
        }

        return mat;
    }



    // V1-2
    /**
     *  The trick to solving **LC 1329 (Sort the Matrix Diagonally)**
     *  relies on a beautiful property of 2D grid diagonals:
     *
     * > **Any two cells `mat[i][j]` belong to the
     *   exact same top-left to bottom-right diagonal
     *   if and only if their index
     *   difference `i - j` is identical.**
     *
     * For instance:
     *
     * * The main diagonal cells like `(0,0)`, `(1,1)`, `(2,2)` all yield `i - j = 0`.
     * * Diagonals below it yield positive integers (`1 - 0 = 1`, `2 - 1 = 1`, etc.).
     * * Diagonals above it yield negative integers (`0 - 1 = -1`, `1 - 2 = -1`, etc.).
     *
     * By leveraging a `HashMap` mapping this unique `i - j` diagonal key to a min-heap (`PriorityQueue`), we can effortlessly sort the diagonals on the fly.
     *
     *
     * ---
     *
     * ### 📊 Complexity Analysis
     *
     * * **Time Complexity:** $\mathcal{O}(M \times N \log(\min(M, N)))$ where $M$ is the number of rows and $N$ is the number of columns.
     * * We iterate over all cells ($M \times N$ steps) to insert and extract from our min-heaps.
     * * The maximum size of any single diagonal heap is capped at $\min(M, N)$, resulting in a $\log(\min(M, N))$ runtime penalty for queue operations.
     *
     *
     * * **Space Complexity:** $\mathcal{O}(M \times N)$ to store the cell values temporarily inside the hash map structure.
     *
     * ---
     *
     * ### 🎥 Recommended Walkthrough
     *
     * For a deeper visual understanding,
     * this [Sort the Matrix Diagonally Video Explanation]
     * (https://www.youtube.com/watch?v=JBqUl7avtG8)
     * dry-runs the entire diagonal mapping concept
     * with clear tracing diagrams.
     *
     */
    public int[][] diagonalSort_1_2(int[][] mat) {
        // Guard check for safely terminating on empty input boundaries
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return mat;
        }

        int rows = mat.length;
        int cols = mat[0].length;

        // Map to group elements belonging to the same diagonal.
        // Key: The mathematical identifier 'i - j'
        // Value: A min-heap (PriorityQueue) that naturally keeps elements sorted in ascending order.
        Map<Integer, Queue<Integer>> diagonalGroups = new HashMap<>();

        // PASS 1: Group all elements by their respective diagonal signature
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int diagonalKey = i - j;

                // If the diagonal key doesn't exist yet, initialize a new Min-Heap
                diagonalGroups.computeIfAbsent(diagonalKey, k -> new PriorityQueue<>());

                // Add the current cell value into its designated sorted diagonal bucket
                diagonalGroups.get(diagonalKey).add(mat[i][j]);
            }
        }

        // PASS 2: Place the sorted values back into the original matrix positions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int diagonalKey = i - j;

                // Poll the smallest remaining element from the min-heap to write it back.
                // Since we traverse row-by-row and col-by-col, we fill it from top-left to bottom-right.
                mat[i][j] = diagonalGroups.get(diagonalKey).poll();
            }
        }

        // Return the modified sorted matrix in-place
        return mat;
    }



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
    public int[][] diagonalSort_3(int[][] mat) {

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
