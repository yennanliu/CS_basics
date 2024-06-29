package LeetCodeJava.Array;

// https://leetcode.com/problems/find-the-minimum-area-to-cover-all-ones-i/description/

import java.util.Arrays;

public class findTheMinimumAreaToCoverAllOnesI {

    // V0
    // IDEA : ARRAY (modified by gpt)
    public int minimumArea(int[][] grid) {
        if (grid.length == 1 && grid[0].length == 1) {
            return 1;
        }

        // Define x, y pointers, move x, y direction respectively
        int x_start = -1;
        int x_end = -1;
        int y_start = -1;
        int y_end = -1;

        // y-direction
        for (int i = 0; i < grid.length; i++) {
            if (Arrays.stream(grid[i]).filter(x -> x == 1).count() > 0) {
                if (x_start == -1) {
                    x_start = i;
                } else {
                    x_end = i;
                }
            }
        }

        // x-direction
        for (int j = 0; j < grid[0].length; j++) {
            int[] yArray = collectYArray(grid, j);
            if (Arrays.stream(yArray).filter(x -> x == 1).count() > 0) {
                if (y_start == -1) {
                    y_start = j;
                } else {
                    y_end = j;
                }
            }
        }

        System.out.println("x_start = " + x_start +
                ", x_end = " + x_end +
                " y_start = " + y_start +
                ", y_end = " + y_end
        );


        if (x_start == -1 || y_start == -1) {
            return 0;
        }

        // NOTE !!! edge case
        /**
         * 1.
         *      Single Row with 1s (Vertical Line):
         *      If x_end remains -1, it means we only found a single row with 1s (a vertical line). In this case, we need to set x_end to be the same as x_start to ensure the calculation of the area is correct.
         */
        if (x_end == -1) {
            x_end = x_start;
        }

        /**
         *  2.
         *      Single Column with 1s (Horizontal Line):
         *      Similarly, if y_end remains -1, it means we only found a single column with 1s (a horizontal line). Here, we set y_end to be the same as y_start.
         *
         */
        if (y_end == -1) {
            y_end = y_start;
        }

        return (x_end - x_start + 1) * (y_end - y_start + 1);
    }

    public int[] collectYArray(int[][] grid, int x) {
        int[] res = new int[grid.length];
        for (int y = 0; y < grid.length; y++) {
            res[y] = grid[y][x];
        }
        return res;
    }

    // V1
    // https://leetcode.com/problems/find-the-minimum-area-to-cover-all-ones-i/solutions/5355084/easy-java-solution/
    public int minimumArea_1(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] row = new int[m];
        int[] col = new int[n];

        // System.out.println(Arrays.toString(row));

        for(int i=0; i<m; i++){
            for(int j=0; j<n; j++){
                if(grid[i][j] == 1){
                    row[i] = 1;
                    col[j] = 1;
                }
            }
        }

        int h = find(row);
        int w = find(col);

        return h * w;
    }

    private int find(int[] arr){
        int n = arr.length;
        int i = 0;
        int j = n-1;
        int len = 0;

        while(i < j){
            if(arr[i] == 1 && arr[j] == 1){
                len = Math.max(len,j - i + 1);
                i++;
                j--;
            }
            else if(arr[i] == 1 && arr[j] != 1){
                j--;
            }
            else if(arr[i] != 1 && arr[j] == 1){
                i++;
            }else{
                i++;
                j--;
            }
        }

        return len == 0 ? 1 : len;
    }

}
