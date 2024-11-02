package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-knight-moves/description/
// https://leetcode.ca/all/1197.html

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 1197. Minimum Knight Moves
 * In an infinite chess board with coordinates from -infinity to +infinity, you have a knight at square [0, 0].
 * <p>
 * A knight has 8 possible moves it can make, as illustrated below. Each move is two squares in a cardinal direction, then one square in an orthogonal direction.
 * <p>
 * <p>
 * <p>
 * Return the minimum number of steps needed to move the knight to the square [x, y].  It is guaranteed the answer exists.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: x = 2, y = 1
 * Output: 1
 * Explanation: [0, 0] → [2, 1]
 * Example 2:
 * <p>
 * Input: x = 5, y = 5
 * Output: 4
 * Explanation: [0, 0] → [2, 1] → [4, 2] → [3, 4] → [5, 5]
 * <p>
 * <p>
 * Constraints:
 * <p>
 * |x| + |y| <= 300
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google Oracle
 * Problem Solution
 * 1197-Minimum-Knight-Moves
 */
public class MinimumKnightMoves {

    // V0
    // IDEA : BFS
    // TODO : fix and validate
//    public int minKnightMoves(int x, int y) {
//
//        if (x==0 && y==0){
//            return 0;
//        }
//
//        // init
//        int[][] moves = new int[][]{ {1,2}, {2,1}, {2,-1}, {1,-2}, {-1,-2}, {-2,-1}, {-2,1}, {-1,2} };
//        // queue : FIFO
//        Queue<List<Integer>> q = new LinkedList<>(); // Queue([x, y, step])
//        List<Integer> tmp = new ArrayList<>();
//        tmp.add(0); // x
//        tmp.add(0); // y
//        tmp.add(0); // step
//        q.add(tmp);
//
//        while(!q.isEmpty()){
//            List<Integer> cur = q.poll();
//            int cur_x = cur.get(0);
//            int cur_y = cur.get(1);
//            int cur_step = cur.get(2);
//            if (cur_x == x && cur_y == y){
//                return cur_step;
//            }
//            for (int[] move : moves){
//                List<Integer> newCoor = new ArrayList<>();
//                newCoor.add(cur_x + move[0]);
//                newCoor.add(cur_y + move[1]);
//                newCoor.add(cur_step + 1);
//                q.add(newCoor);
//            }
//        }
//
//        return -1;
//    }

    // V1
    // IDEA : BFS
    // https://leetcode.ca/2019-03-11-1197-Minimum-Knight-Moves/
    public int minKnightMoves_1(int x, int y) {
        x += 310;
        y += 310;
        int ans = 0;
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{310, 310});
        // NOTE !!! use vis (boolean 2D array) to AVOID visit same coordination again
        boolean[][] vis = new boolean[700][700];
        vis[310][310] = true;
        int[][] dirs = {{-2, 1}, {-1, 2}, {1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}};
        while (!q.isEmpty()) {
            for (int k = q.size(); k > 0; --k) {
                int[] p = q.poll();
                if (p[0] == x && p[1] == y) {
                    return ans;
                }
                for (int[] dir : dirs) {
                    int c = p[0] + dir[0];
                    int d = p[1] + dir[1];
                    if (!vis[c][d]) {
                        vis[c][d] = true;
                        q.offer(new int[]{c, d});
                    }
                }
            }
            ++ans;
        }
        return -1;
    }

    // V2
    // IDEA : BFD
    // https://www.cnblogs.com/cnoodle/p/12820573.html
    public int minKnightMoves_2(int x, int y) {
        int[][] dirs = new int[][]{{-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {-2, -1}, {-2, 1}, {2, -1}, {2, 1}};
        x = Math.abs(x);
        y = Math.abs(y);
        HashSet<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited.add("0,0");

        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                int[] cur = queue.poll();
                if (cur[0] == x && cur[1] == y) {
                    return step;
                }

                for (int[] dir : dirs) {
                    int i = cur[0] + dir[0];
                    int j = cur[1] + dir[1];
                    // (0, 0) -> (2, -1) -> (1, 1)
                    // +2的意思是多给两个格子的空间以便于骑士跳出去再跳回来的操作
                    if (!visited.contains(i + "," + j) && i >= -1 && j >= -1 && i <= x + 2 && j <= y + 2) {
                        queue.offer(new int[]{i, j});
                        visited.add(i + "," + j);
                    }
                }
            }
            step++;
        }
        return -1;
    }

}
