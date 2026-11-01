package LeetCodeJava.Array;

// https://leetcode.com/problems/count-servers-that-communicate/description/

/**
 * 1267. Count Servers that Communicate
 * Medium
 *
 * You are given a map of a server center, represented as a m * n integer matrix grid,
 * where 1 means that on that cell there is a server and 0 means that it is no server.
 * Two servers are said to communicate if they are on the same row or on the same column.
 *
 * Return the number of servers that communicate with any other server.
 *
 * Example 1:
 *
 * Input: grid = [[1,0],[0,1]]
 * Output: 0
 * Explanation: No servers can communicate with others.
 *
 * Example 2:
 *
 * Input: grid = [[1,0],[1,1]]
 * Output: 3
 * Explanation: All three servers can communicate with at least one other server.
 *
 * Example 3:
 *
 * Input: grid = [[1,1,0,0],[0,0,1,0],[0,0,1,0],[0,0,0,1]]
 * Output: 4
 * Explanation: The two servers in the first row can communicate with each other. The two servers in the third column can communicate with each other. The server at right bottom corner can't communicate with any other server.
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m <= 250
 * 1 <= n <= 250
 * grid[i][j] == 0 or 1
 *
 */
public class CountServersThatCommunicate {

    // V0
    // IDEA: COUNT SERVERS PER ROW AND PER COLUMN
    //   a server communicates iff its row OR its column holds at least 2 servers
    /**
     * time = O(M * N)
     * space = O(M + N)
     */
    public int countServers(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] serversInRow = new int[m];
        int[] serversInCol = new int[n];

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 1) {
                    serversInRow[r]++;
                    serversInCol[c]++;
                }
            }
        }

        int communicating = 0;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == 1 && (serversInRow[r] > 1 || serversInCol[c] > 1)) {
                    communicating++;
                }
            }
        }
        return communicating;
    }
}
