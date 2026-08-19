package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/android-unlock-patterns/

/**
 *  351. Android Unlock Patterns
 *  Medium
 *
 *  Android devices have a special lock screen with a 3 x 3 grid of dots. Users can set an
 *  "unlock pattern" by connecting the dots in a specific sequence, forming a series of joined
 *  line segments where each segment's endpoints are two consecutive dots in the sequence.
 *  A sequence of k dots is a valid unlock pattern if both of the following are true:
 *
 *   - All the dots in the sequence are distinct.
 *   - If the line segment connecting two consecutive dots in the sequence passes through the
 *     center of any other dot, the other dot must have previously appeared in the sequence.
 *     No jumps through non-selected dots are allowed.
 *
 *  Given two integers m and n, return the number of unlock patterns of the Android grid lock
 *  screen that consist of at least m keys and at most n keys.
 *
 *  Grid:
 *      | 1 | 2 | 3 |
 *      | 4 | 5 | 6 |
 *      | 7 | 8 | 9 |
 *
 *  Example 1:
 *  Input: m = 1, n = 1
 *  Output: 9
 *
 *  Example 2:
 *  Input: m = 1, n = 2
 *  Output: 65
 *
 *  Constraints:
 *
 *   1 <= m, n <= 9
 */
public class AndroidUnlockPatterns {

    // V0
    // IDEA: BACKTRACKING + SKIP MATRIX + SYMMETRY
    //       skip[a][b] = the dot that lies between a and b (0 if none); a move a -> b is legal
    //       only when skip[a][b] == 0 or that middle dot has already been visited.
    //       By symmetry: starting at 1/3/7/9 gives the same count (corner), and 2/4/6/8 too
    //       (edge), so only 3 DFS roots are needed per length.
    /**
     * time = O(9!) worst case (bounded by the number of patterns)
     * space = O(9) for the visited array + recursion depth
     */
    public int numberOfPatterns(int m, int n) {
        int[][] skip = new int[10][10];
        skip[1][3] = skip[3][1] = 2;
        skip[1][7] = skip[7][1] = 4;
        skip[3][9] = skip[9][3] = 6;
        skip[7][9] = skip[9][7] = 8;
        skip[2][8] = skip[8][2] = 5;
        skip[4][6] = skip[6][4] = 5;
        skip[1][9] = skip[9][1] = 5;
        skip[3][7] = skip[7][3] = 5;

        boolean[] visited = new boolean[10];
        int res = 0;
        for (int len = m; len <= n; len++) {
            res += dfs(visited, skip, 1, len - 1) * 4;  // corners: 1, 3, 7, 9
            res += dfs(visited, skip, 2, len - 1) * 4;  // edges:   2, 4, 6, 8
            res += dfs(visited, skip, 5, len - 1);      // center
        }
        return res;
    }

    private int dfs(boolean[] visited, int[][] skip, int cur, int remain) {
        if (remain == 0) {
            return 1;
        }
        visited[cur] = true;
        int cnt = 0;
        for (int next = 1; next <= 9; next++) {
            int mid = skip[cur][next];
            if (!visited[next] && (mid == 0 || visited[mid])) {
                cnt += dfs(visited, skip, next, remain - 1);
            }
        }
        visited[cur] = false;
        return cnt;
    }
}
