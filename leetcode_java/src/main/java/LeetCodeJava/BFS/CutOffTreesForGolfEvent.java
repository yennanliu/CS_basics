package LeetCodeJava.BFS;

// https://leetcode.com/problems/cut-off-trees-for-golf-event/description/

import java.util.*;

/**
 * 675. Cut Off Trees for Golf Event
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * You are asked to cut off all the trees in a forest for a golf event. The forest is represented as an m x n matrix. In this matrix:
 *
 * 0 means the cell cannot be walked through.
 * 1 represents an empty cell that can be walked through.
 * A number greater than 1 represents a tree in a cell that can be walked through, and this number is the tree's height.
 * In one step, you can walk in any of the four directions: north, east, south, and west. If you are standing in a cell with a tree, you can choose whether to cut it off.
 *
 * You must cut off the trees in order from shortest to tallest. When you cut off a tree, the value at its cell becomes 1 (an empty cell).
 *
 * Starting from the point (0, 0), return the minimum steps you need to walk to cut off all the trees. If you cannot cut off all the trees, return -1.
 *
 * Note: The input is generated such that no two trees have the same height, and there is at least one tree needs to be cut off.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: forest = [[1,2,3],[0,0,4],[7,6,5]]
 * Output: 6
 * Explanation: Following the path above allows you to cut off the trees from shortest to tallest in 6 steps.
 * Example 2:
 *
 *
 * Input: forest = [[1,2,3],[0,0,0],[7,6,5]]
 * Output: -1
 * Explanation: The trees in the bottom row cannot be accessed as the middle row is blocked.
 * Example 3:
 *
 * Input: forest = [[2,3,4],[0,0,5],[8,7,6]]
 * Output: 6
 * Explanation: You can follow the same path as Example 1 to cut off all the trees.
 * Note that you can cut off the first tree at (0, 0) before making any steps.
 *
 *
 * Constraints:
 *
 * m == forest.length
 * n == forest[i].length
 * 1 <= m, n <= 50
 * 0 <= forest[i][j] <= 109
 * Heights of all trees are distinct.
 */
public class CutOffTreesForGolfEvent {

    // V0
//    public int cutOffTree(List<List<Integer>> forest) {
//
//        return 0;
//    }

    // V1
    // https://leetcode.ca/2017-10-05-675-Cut-Off-Trees-for-Golf-Event/
    private int[] dist = new int[3600];
    private List<List<Integer>> forest;
    private int m;
    private int n;

    public int cutOffTree_1(List<List<Integer>> forest) {
        this.forest = forest;
        m = forest.size();
        n = forest.get(0).size();
        List<int[]> trees = new ArrayList<>();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (forest.get(i).get(j) > 1) {
                    trees.add(new int[] {forest.get(i).get(j), i * n + j});
                }
            }
        }
        trees.sort(Comparator.comparingInt(a -> a[0]));
        int ans = 0;
        int start = 0;
        for (int[] tree : trees) {
            int end = tree[1];
            int t = bfs(start, end);
            if (t == -1) {
                return -1;
            }
            ans += t;
            start = end;
        }
        return ans;
    }

    private int bfs(int start, int end) {
        PriorityQueue<int[]> q = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        q.offer(new int[] {f(start, end), start});
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        int[] dirs = {-1, 0, 1, 0, -1};
        while (!q.isEmpty()) {
            int state = q.poll()[1];
            if (state == end) {
                return dist[state];
            }
            for (int k = 0; k < 4; ++k) {
                int x = state / n + dirs[k];
                int y = state % n + dirs[k + 1];
                if (x >= 0 && x < m && y >= 0 && y < n && forest.get(x).get(y) > 0) {
                    if (dist[x * n + y] > dist[state] + 1) {
                        dist[x * n + y] = dist[state] + 1;
                        q.offer(new int[] {dist[x * n + y] + f(x * n + y, end), x * n + y});
                    }
                }
            }
        }
        return -1;
    }

    private int f(int start, int end) {
        int a = start / n;
        int b = start % n;
        int c = end / n;
        int d = end % n;
        return Math.abs(a - c) + Math.abs(b - d);
    }

    // V2
    // https://leetcode.com/problems/cut-off-trees-for-golf-event/solutions/7200480/cut-off-trees-for-golf-event-leetcode-67-5i1z/
    int[] dx = { 0, 0, 1, -1 };
    int[] dy = { 1, -1, 0, 0 };

    public int cutOffTree_2(List<List<Integer>> forest) {
        if (forest.get(0).get(0) == 0)
            return -1;
        int count = 0;
        int r = forest.size(), c = forest.get(0).size();

        // Collect all trees in priority queue sorted by height
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (forest.get(i).get(j) > 1) {
                    pq.offer(new int[] { forest.get(i).get(j), i, j });
                }
            }
        }

        int oldr = 0, oldc = 0;
        while (!pq.isEmpty()) {
            boolean[][] visited = new boolean[r][c];
            visited[oldr][oldc] = true;
            int[] cell = pq.poll();
            int newr = cell[1], newc = cell[2];

            Queue<int[]> queue = new LinkedList<>();
            queue.add(new int[] { oldr, oldc });
            int steps = 0;
            boolean found = false;

            loop: while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    int[] box = queue.poll();
                    int x = box[0], y = box[1];

                    if (x == newr && y == newc) {
                        count += steps;
                        found = true;
                        break loop;
                    }

                    for (int d = 0; d < 4; d++) {
                        int X = x + dx[d], Y = y + dy[d];
                        if (X < 0 || X >= r || Y < 0 || Y >= c)
                            continue;
                        if (!visited[X][Y] && forest.get(X).get(Y) > 0) {
                            visited[X][Y] = true;
                            queue.add(new int[] { X, Y });
                        }
                    }
                }
                steps++;
            }

            if (!found)
                return -1;
            oldr = newr;
            oldc = newc;
        }

        return count;
    }

    // V3
    // IDEA: BFS + MIN HEAP (TLE)
    // https://leetcode.com/problems/cut-off-trees-for-golf-event/solutions/6815292/bfs-minheap-by-nikhill04-akcg/


    // V4
    // IDEA: BFS + MIN HEAP
    // https://leetcode.com/problems/cut-off-trees-for-golf-event/solutions/1033258/python-bfs-priorityqueue-w-comments-and-qcyqs/



}
