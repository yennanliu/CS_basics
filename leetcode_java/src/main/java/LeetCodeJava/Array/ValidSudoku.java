package LeetCodeJava.Array;

// https://leetcode.com/problems/valid-sudoku/

import java.util.HashSet;
import java.util.Set;

public class ValidSudoku {

//    public boolean isValidSudoku(char[][] board) {
//
//        // TODO : check how to get matrix width, length
//        if (board.equals(null) || (board.length == 0 && board[0].length == 0)){
//            return true;
//        }
//
//        int length = board.length;
//        int width = board[0].length;
//
//        /** check row */
//        for (int i = 0; i < length; i ++){
//            char[] row = board[i];
//            if (! check(row)){
//                return false;
//            }
//        }
//
//        /** check col */
//        for (int i = 0; i < width; i ++){
//            char[] curRow = new char[length];
//            for (int j = 0; j < length; j++){
//               curRow[j] = board[j][i];
//            }
//            if (! check(curRow)){
//                return false;
//            }
//        }
//
//        /** check 3 x 3 sub matrix */
//        for (int i = 0; i < length - 2; i++){
//            for (int j = 0; j < width - 2; j ++){
//                char[] subArray = new char[9];
//                subArray[]
//            }
//        }
//
//        return true;
//    }
//
//    private Boolean check(char[] input){
//
//        if (input.length == 0){
//            return true;
//        }
//
//        Set<Integer> set = new HashSet<Integer>();
//
//        for (char val : input){
//            if (!set.contains((int) val)){
//                set.add((int) val);
//            }else{
//                if (String.valueOf(val) == "."){
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    // V1
    // IDEA : HASH SET
    // https://leetcode.com/problems/valid-sudoku/editorial/
    public boolean isValidSudoku_2(char[][] board) {
        int N = 9;

        // Use hash set to record the status
        HashSet<Character>[] rows = new HashSet[N];
        HashSet<Character>[] cols = new HashSet[N];
        HashSet<Character>[] boxes = new HashSet[N];
        for (int r = 0; r < N; r++) {
            rows[r] = new HashSet<Character>();
            cols[r] = new HashSet<Character>();
            boxes[r] = new HashSet<Character>();
        }

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                char val = board[r][c];

                // Check if the position is filled with number
                if (val == '.') {
                    continue;
                }

                // Check the row
                if (rows[r].contains(val)) {
                    return false;
                }
                rows[r].add(val);

                // Check the column
                if (cols[c].contains(val)) {
                    return false;
                }
                cols[c].add(val);

                // Check the box
                /** NOTE trick here !!!! */
                int idx = (r / 3) * 3 + c / 3;
                if (boxes[idx].contains(val)) {
                    return false;
                }
                boxes[idx].add(val);
            }
        }
        return true;
    }

    // V2
    // IDEA : Array of Fixed Length
    // https://leetcode.com/problems/valid-sudoku/editorial/
    public boolean isValidSudoku_3(char[][] board) {
        int N = 9;

        // Use an array to record the status
        int[][] rows = new int[N][N];
        int[][] cols = new int[N][N];
        int[][] boxes = new int[N][N];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // Check if the position is filled with number
                if (board[r][c] == '.') {
                    continue;
                }
                int pos = board[r][c] - '1';

                // Check the row
                if (rows[r][pos] == 1) {
                    return false;
                }
                rows[r][pos] = 1;

                // Check the column
                if (cols[c][pos] == 1) {
                    return false;
                }
                cols[c][pos] = 1;

                // Check the box
                int idx = (r / 3) * 3 + c / 3;
                if (boxes[idx][pos] == 1) {
                    return false;
                }
                boxes[idx][pos] = 1;
            }
        }
        return true;
    }

    // V3
    // IDEA : Bitmasking
    // https://leetcode.com/problems/valid-sudoku/editorial/
    public boolean isValidSudoku_4(char[][] board) {
        int N = 9;

        // Use a binary number to record previous occurrence
        int[] rows = new int[N];
        int[] cols = new int[N];
        int[] boxes = new int[N];

        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                // Check if the position is filled with number
                if (board[r][c] == '.') {
                    continue;
                }
                int val = board[r][c] - '0';
                int pos = 1 << (val - 1);

                // Check the row
                if ((rows[r] & pos) > 0) {
                    return false;
                }
                rows[r] |= pos;

                // Check the column
                if ((cols[c] & pos) > 0) {
                    return false;
                }
                cols[c] |= pos;

                // Check the box
                int idx = (r / 3) * 3 + c / 3;
                if ((boxes[idx] & pos) > 0) {
                    return false;
                }
                boxes[idx] |= pos;
            }
        }
        return true;
    }

}
