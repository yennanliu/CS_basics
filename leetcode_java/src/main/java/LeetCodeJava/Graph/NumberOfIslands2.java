package LeetCodeJava.Graph;

// https://leetcode.com/problems/number-of-islands-ii/description/
// https://leetcode.ca/2016-09-30-305-Number-of-Islands-II/
/**
 * 305 - Number of Islands II
 * Posted on September 30, 2016 · 9 minute read
 * Welcome to Subscribe On Youtube
 *
 * 305. Number of Islands II
 * Description
 * You are given an empty 2D binary grid grid of size m x n. The grid represents a map where 0's represent water and 1's represent land. Initially, all the cells of grid are water cells (i.e., all the cells are 0's).
 *
 * We may perform an add land operation which turns the water at position into a land. You are given an array positions where positions[i] = [ri, ci] is the position (ri, ci) at which we should operate the ith operation.
 *
 * Return an array of integers answer where answer[i] is the number of islands after turning the cell (ri, ci) into a land.
 *
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
 * Output: [1,1,2,3]
 * Explanation:
 * Initially, the 2d grid is filled with water.
 * - Operation #1: addLand(0, 0) turns the water at grid[0][0] into a land. We have 1 island.
 * - Operation #2: addLand(0, 1) turns the water at grid[0][1] into a land. We still have 1 island.
 * - Operation #3: addLand(1, 2) turns the water at grid[1][2] into a land. We have 2 islands.
 * - Operation #4: addLand(2, 1) turns the water at grid[2][1] into a land. We have 3 islands.
 * Example 2:
 *
 * Input: m = 1, n = 1, positions = [[0,0]]
 * Output: [1]
 *
 *
 * Constraints:
 *
 * 1 <= m, n, positions.length <= 104
 * 1 <= m * n <= 104
 * positions[i].length == 2
 * 0 <= ri < m
 * 0 <= ci < n
 *
 *
 * Follow up: Could you solve it in time complexity O(k log(mn)), where k == positions.length?
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberOfIslands2 {

    // V0
//    public List<Integer> numIslands2(int m, int n, int[][] positions) {
//
//    }

    // V0-1
    // IDEA: UNION FIND (gemini)
    class UnionFind_0_1 {
        // Array to store the parent of each element.
        private int[] parent;
        // Array for rank/size optimization.
        private int[] rank;
        // Current total number of connected components (islands).
        private int count;

        public UnionFind_0_1(int size) {
            // Size is M * N, representing all cells.
            parent = new int[size];
            rank = new int[size];
            // Initialize all cells as non-land (parent = -1) or a state indicating not yet used.
            Arrays.fill(parent, -1);
            count = 0;
        }

        // Find the representative (root) of the set containing element i, with path compression.
        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]);
            }
            return parent[i];
        }

        // Unite the sets containing elements i and j, using union by rank/size.
        // Returns true if a union occurred (i.e., i and j were in different sets).
        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                // Union by Rank (or size)
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++;
                }
                // A merge occurred, so the number of distinct components decreases by 1.
                count--;
                return true;
            }
            return false;
        }

        // Initialize a new cell as a distinct island.
        public void setParent(int i) {
            // Only initialize if it was previously not initialized (not yet land).
            if (parent[i] == -1) {
                parent[i] = i;
                // The new island is a new component.
                count++;
            }
        }

        public int getCount() {
            return count;
        }
    }

    /**
     * Finds the number of islands after each land operation using Union-Find.
     * @param m Number of rows.
     * @param n Number of columns.
     * @param positions A list of (r, c) coordinates where land is added.
     * @return A list of the number of islands after each operation.
     * * Time Complexity: O(K * alpha(M*N)), where K is the number of positions
     * and alpha is the inverse Ackermann function (nearly constant).
     * Space Complexity: O(M * N)
     */
    public List<Integer> numIslands2_0_1(int m, int n, int[][] positions) {

        List<Integer> result = new ArrayList<>();
        if (m <= 0 || n <= 0) return result;

        UnionFind_0_1 dsu = new UnionFind_0_1(m * n);

        // Define neighbor directions: Down, Up, Right, Left
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] pos : positions) {
            int r = pos[0];
            int c = pos[1];

            // 1D index for the current position
            int currentId = r * n + c;

            // Check if this position was already turned into land in a previous operation.
            // If it was, the state is already recorded, and the island count doesn't change.
            if (dsu.parent[currentId] == currentId) {
                result.add(dsu.getCount());
                continue;
            }

            // 1. Establish New Island:
            // Set the new cell as land and increment count inside setParent.
            dsu.setParent(currentId);

            // 2. Union with Neighbors:
            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                // Check bounds
                if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                    int neighborId = nr * n + nc;

                    // Check if the neighbor is ALREADY land (i.e., its parent is initialized)
                    if (dsu.parent[neighborId] != -1) {
                        // If the neighbor is land, union the current island with the neighbor's island.
                        dsu.union(currentId, neighborId);
                    }
                }
            }

            // 3. Record Result:
            result.add(dsu.getCount());
        }

        return result;
    }


    // V1
    // IDEA: UNION FIND
    // https://leetcode.ca/2016-09-30-305-Number-of-Islands-II/
    class UnionFind {
        private final int[] p;
        private final int[] size;

        public UnionFind(int n) {
            p = new int[n];
            size = new int[n];
            for (int i = 0; i < n; ++i) {
                p[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (p[x] != x) {
                p[x] = find(p[x]);
            }
            return p[x];
        }

        public boolean union(int a, int b) {
            int pa = find(a), pb = find(b);
            if (pa == pb) {
                return false;
            }
            if (size[pa] > size[pb]) {
                p[pb] = pa;
                size[pa] += size[pb];
            } else {
                p[pa] = pb;
                size[pb] += size[pa];
            }
            return true;
        }
    }

    public List<Integer> numIslands2_2(int m, int n, int[][] positions) {
        int[][] grid = new int[m][n];
        UnionFind uf = new UnionFind(m * n);
        int[] dirs = {-1, 0, 1, 0, -1};
        int cnt = 0;
        List<Integer> ans = new ArrayList<>();
        for (int[] p : positions) {
            int i = p[0], j = p[1];
            if (grid[i][j] == 1) {
                ans.add(cnt);
                continue;
            }
            grid[i][j] = 1;
            ++cnt;
            for (int k = 0; k < 4; ++k) {
                int x = i + dirs[k], y = j + dirs[k + 1];
                if (x >= 0 && x < m && y >= 0 && y < n && grid[x][y] == 1
                        && uf.union(i * n + j, x * n + y)) {
                    --cnt;
                }
            }
            ans.add(cnt);
        }
        return ans;
    }


    // V2

    // V3


}
