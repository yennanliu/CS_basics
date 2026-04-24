package LeetCodeJava.Stack;

// https://leetcode.com/problems/maximal-rectangle/description/

import java.util.Stack;

/**
 *  85. Maximal Rectangle
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Given a rows x cols binary matrix filled with 0's and 1's, find the largest rectangle containing only 1's and return its area.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
 * Output: 6
 * Explanation: The maximal rectangle is shown in the above picture.
 * Example 2:
 *
 * Input: matrix = [["0"]]
 * Output: 0
 * Example 3:
 *
 * Input: matrix = [["1"]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * rows == matrix.length
 * cols == matrix[i].length
 * 1 <= rows, cols <= 200
 * matrix[i][j] is '0' or '1'.
 *
 *
 *
 */
public class MaximalRectangle {

    // V0
//    public int maximalRectangle(char[][] matrix) {
//
//    }

    // V1-1
    // IDEA: Histogram + Stack + LC 84 (GPT)
    /** Core idea:
     *
     *  -> Treat each `row` as the base of a `histogram`:
     *
     *   - Build heights[j]:
     *       number of consecutive '1's above
     *       (including current row)
     *
     *   - For each row,
     *     solve Largest Rectangle in Histogram (LC 84)
     *
     *
     * --------------------
     *
     *  V2:
     *
     *  We convert each row into a histogram:
     *
     *  - Each column stores how many consecutive
     *    '1's appear vertically up to that row
     *
     *  - Then for each row, we solve
     *    largest rectangle in histogram
     *
     */
    public int maximalRectangle_1_1(char[][] matrix) {
        if (matrix == null || matrix.length == 0)
            return 0;

        int m = matrix.length;
        int n = matrix[0].length;

        /** NOTE !!
         *
         *  This is the key structure.
         *
         *  -> heights[j] =
         *       number of `consecutive` '1's ending at
         *       current `row` i in column j
         *
         *  Example buildup:
         *
         *   ```
         *   Row 0: 1 0 1  → heights = [1,0,1]
         *   Row 1: 1 1 1  → heights = [2,1,2]
         *   Row 2: 1 1 0  → heights = [3,2,0]
         *   ```
         *
         */
        int[] heights = new int[n];

        // Tracks global maximum rectangle.
        int maxArea = 0;

        // Iterate through each row
        // (process row by row)
        for (int i = 0; i < m; i++) {
            /** NOTE !!!
             *
             *  Step 1) build `histogram`
             *
             *  ->
             *
             *  - If current cell is '1':
             *       extend the vertical height
             *
             *  - If '0':
             *       reset height to 0 (rectangle breaks)
             *
             *
             *       -> After this loop, heights[] represents
             *          a histogram for row i.
             */
            // build histogram
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') {
                    heights[j] += 1;
                } else {
                    heights[j] = 0;
                }
            }

            /** NOTE !!!
             *
             *  Step 2) Solve histogram problem
             *
             *    -> We compute the largest rectangle area in
             *       this histogram and update the global max.
             *
             *
             *  get `maxArea`
             */
            // compute largest rectangle in histogram
            maxArea = Math.max(maxArea, largestRectangleArea_1_1(heights));
        }

        return maxArea;
    }


    /**  Help func:  // LC 84
     * 
     *  https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Stack/LargestRectangleInHistogram.java
     *
     *   Largest Rectangle in Histogram
     *
     *    -> Classic monotonic stack solution.
     *
     */
    private int largestRectangleArea_1_1(int[] heights) {

        /** NOTE !!!
         *
         *  1.  Stack stores `indices`, not heights
         *  2.  Maintains increasing height order
         *
         */
        Stack<Integer> stack = new Stack<>();
        int max = 0;

        for (int i = 0; i <= heights.length; i++) {
            int h = (i == heights.length) ? 0 : heights[i];

            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, height * width);
            }

            stack.push(i);
        }

        return max;
    }


    // V1-2
    // IDEA: Histogram + Stack + LC 84 (GEMINI)
    public int maximalRectangle_1_2(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        int rows = matrix.length;
        int cols = matrix[0].length;
        int[] heights = new int[cols];
        int maxArea = 0;

        for (int i = 0; i < rows; i++) {
            // 1. Update histogram heights for the current row
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == '1') {
                    heights[j] += 1;
                } else {
                    heights[j] = 0; // Reset if there's a gap
                }
            }

            // 2. Find the largest rectangle in the current histogram
            maxArea = Math.max(maxArea, largestRectangleInHistogram(heights));
        }

        return maxArea;
    }

    private int largestRectangleInHistogram(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            // Use a virtual '0' height at the end to flush the stack
            int h = (i == n) ? 0 : heights[i];

            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }



    // V2
    // IDEA: ROW-WISE PREFIX WIDTH + VERTICAL EXPANSION
    // https://leetcode.com/problems/maximal-rectangle/solutions/7484798/row-wise-prefix-width-vertical-expansion-ujpz/
    public int maximalRectangle_2(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        int M = matrix.length;
        int N = matrix[0].length;

        int[][] mat = new int[M][N];

        // convert char to int
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                mat[i][j] = matrix[i][j] - '0';
            }
        }

        // row-wise prefix widths
        for (int i = 0; i < M; i++) {
            for (int j = 1; j < N; j++) {
                if (mat[i][j] == 1) {
                    mat[i][j] += mat[i][j - 1];
                }
            }
        }

        int Ans = 0;

        // fix each column
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < M; i++) {
                int width = mat[i][j];
                if (width == 0)
                    continue;

                // expand downward
                int currWidth = width;
                for (int k = i; k < M && mat[k][j] > 0; k++) {
                    currWidth = Math.min(currWidth, mat[k][j]);
                    int height = k - i + 1;
                    Ans = Math.max(Ans, currWidth * height);
                }

                // expand upward
                currWidth = width;
                for (int k = i; k >= 0 && mat[k][j] > 0; k--) {
                    currWidth = Math.min(currWidth, mat[k][j]);
                    int height = i - k + 1;
                    Ans = Math.max(Ans, currWidth * height);
                }
            }
        }

        return Ans;
    }


    // V3
    // IDEA: STACK
    // https://leetcode.com/problems/maximal-rectangle/solutions/7484644/histogram-monotonic-stack-time-om-x-n-sp-fb2p/
    public int maximalRectangle_3(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length, ans = 0;
        int[] hist = new int[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] != '0')
                    hist[j] += 1;
                else
                    hist[j] = 0;
            }
            int area = area(hist);
            ans = Math.max(ans, area);
        }

        return ans;

    }

    public static int area(int[] heights) {
        int n = heights.length;
        int maxArea = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }

        return maxArea;
    }



    // V4
    // IDEA: STACK
    // https://leetcode.com/problems/maximal-rectangle/solutions/7042757/stack-histogram-trick-faang-favourite-an-n8p9/
    public int maximalRectangle_4(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        int maxArea = 0;
        int cols = matrix[0].length;
        int[] heights = new int[cols];

        for (char[] row : matrix) {
            for (int i = 0; i < cols; i++) {
                // Update heights: increment if '1', reset if '0'
                heights[i] = (row[i] == '1') ? heights[i] + 1 : 0;
            }
            maxArea = Math.max(maxArea, largestRectangleArea(heights));
        }

        return maxArea;
    }

    public int largestRectangleArea(int[] heights) {
        int n = heights.length;
        int[] left = new int[n];
        int[] right = new int[n];
        Stack<Integer> stack = new Stack<>();

        // Nearest Smaller to Left
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            left[i] = (stack.isEmpty()) ? -1 : stack.peek();
            stack.push(i);
        }

        stack.clear(); // Reuse stack

        // Nearest Smaller to Right
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }
            right[i] = (stack.isEmpty()) ? n : stack.peek();
            stack.push(i);
        }

        // Compute max area
        int maxArea = 0;
        for (int i = 0; i < n; i++) {
            int width = right[i] - left[i] - 1;
            maxArea = Math.max(maxArea, heights[i] * width);
        }

        return maxArea;
    }





}
