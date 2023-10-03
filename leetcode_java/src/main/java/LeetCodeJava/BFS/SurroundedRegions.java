package LeetCodeJava.BFS;

// https://leetcode.com/problems/surrounded-regions/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SurroundedRegions {

    // TODO : fix below
    // V0
//    int[][] points = new int[][]{};
//    public void solve(char[][] board) {
//
//        if (board.length == 1 && board[0].length == 1){
//            return;
//        }
//
//        int len = board.length;
//        int width = board[0].length;
//
//        // dfs
//        // make points to "X"
//        List<List<Integer>> tmp_points = _collect(board, 0, 0, new ArrayList<>());
//        if (tmp_points != null && _is_border(tmp_points)){
//            flip(tmp_points, board);
//        }
//        return;
//    }
//
//    private List<List<Integer>> _collect(char[][] board, int x, int y, List<List<Integer>> points){
//
//        int len = board.length;
//        int width = board[0].length;
//
//        if (x < 0 || x >= width || y < 0 || y >= len || board[y][x] == 'X'){
//            return null;
//        }
//
//        if (board[y][x] == 'O'){
//            List<Integer> cur = new ArrayList<>();
//            cur.add(x);
//            cur.add(y);
//            points.add(cur);
//        }
//
//        _collect(board, x+1, y, points);
//        _collect(board, x-1, y, points);
//        _collect(board, x, y+1, points);
//        _collect(board, x, y-1, points);
//
//        return points;
//    }
//
//    private Boolean _is_border(List<List<Integer>> points){
//        for (List<Integer> x : points){
//            if (x.get(0) == 0 || x.get(1) == 0){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private void flip(List<List<Integer>> points, char[][] board){
//        for (List<Integer> x : points){
//            board[x.get(0)][x.get(1)] = 'X';
//        }
//    }

    // V1
    // IDEA : DFS
    // https://leetcode.com/problems/surrounded-regions/editorial/
    protected Integer ROWS = 0;
    protected Integer COLS = 0;

    public void solve_1(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        this.ROWS = board.length;
        this.COLS = board[0].length;

        List<Pair<Integer, Integer>> borders = new LinkedList<Pair<Integer, Integer>>();
        // Step 1). construct the list of border cells
        for (int r = 0; r < this.ROWS; ++r) {
            borders.add(new Pair(r, 0));
            borders.add(new Pair(r, this.COLS - 1));
        }
        for (int c = 0; c < this.COLS; ++c) {
            borders.add(new Pair(0, c));
            borders.add(new Pair(this.ROWS - 1, c));
        }

        // Step 2). mark the escaped cells
        for (Pair<Integer, Integer> pair : borders) {
            this.DFS(board, pair.first, pair.second);
        }

        // Step 3). flip the cells to their correct final states
        for (int r = 0; r < this.ROWS; ++r) {
            for (int c = 0; c < this.COLS; ++c) {
                if (board[r][c] == 'O')
                    board[r][c] = 'X';
                if (board[r][c] == 'E')
                    board[r][c] = 'O';
            }
        }
    }

    protected void DFS(char[][] board, int row, int col) {
        if (board[row][col] != 'O')
            return;

        board[row][col] = 'E';
        if (col < this.COLS - 1)
            this.DFS(board, row, col + 1);
        if (row < this.ROWS - 1)
            this.DFS(board, row + 1, col);
        if (col > 0)
            this.DFS(board, row, col - 1);
        if (row > 0)
            this.DFS(board, row - 1, col);
        }
    }

    class Pair<U, V> {
        public U first;
        public V second;

        public Pair(U first, V second) {
            this.first = first;
            this.second = second;
        }


    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/surrounded-regions/editorial/
    protected Integer ROWS = 0;
    protected Integer COLS = 0;

    public void solve_2(char[][] board) {
            if (board == null || board.length == 0) {
                return;
            }
            this.ROWS = board.length;
            this.COLS = board[0].length;

            List<Pair<Integer, Integer>> borders = new LinkedList<Pair<Integer, Integer>>();
            // Step 1). construct the list of border cells
            for (int r = 0; r < this.ROWS; ++r) {
                borders.add(new Pair(r, 0));
                borders.add(new Pair(r, this.COLS - 1));
            }
            for (int c = 0; c < this.COLS; ++c) {
                borders.add(new Pair(0, c));
                borders.add(new Pair(this.ROWS - 1, c));
            }

            // Step 2). mark the escaped cells
            for (Pair<Integer, Integer> pair : borders) {
                this.BFS(board, pair.first, pair.second);
            }

            // Step 3). flip the cells to their correct final states
            for (int r = 0; r < this.ROWS; ++r) {
                for (int c = 0; c < this.COLS; ++c) {
                    if (board[r][c] == 'O')
                        board[r][c] = 'X';
                    if (board[r][c] == 'E')
                        board[r][c] = 'O';
                }
            }
        }

        protected void BFS(char[][] board, int r, int c) {
            LinkedList<Pair2<Integer, Integer>> queue = new LinkedList<Pair2<Integer, Integer>>();
            queue.offer(new Pair2<>(r, c));

            while (!queue.isEmpty()) {
                Pair2<Integer, Integer> pair = queue.pollFirst();
                int row = pair.first, col = pair.second;
                if (board[row][col] != 'O')
                    continue;

                board[row][col] = 'E';
                if (col < this.COLS - 1)
                    queue.offer(new Pair2<>(row, col + 1));
                if (row < this.ROWS - 1)
                    queue.offer(new Pair2<>(row + 1, col));
                if (col > 0)
                    queue.offer(new Pair2<>(row, col - 1));
                if (row > 0)
                    queue.offer(new Pair2<>(row - 1, col));
            }
        }
    }


    class Pair2<U, V> {
        public U first;
        public V second;

        public Pair2(U first, V second) {
            this.first = first;
            this.second = second;
        }

}
