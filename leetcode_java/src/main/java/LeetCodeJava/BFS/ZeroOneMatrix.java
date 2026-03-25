package LeetCodeJava.BFS;

// https://leetcode.com/problems/01-matrix/description/

import java.util.*;

/**
 *  542. 01 Matrix
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an m x n binary matrix mat, return the distance of the nearest 0 for each cell.
 *
 * The distance between two cells sharing a common edge is 1.
 *
 * Example 1:
 *
 *
 * Input: mat = [[0,0,0],[0,1,0],[0,0,0]]
 * Output: [[0,0,0],[0,1,0],[0,0,0]]
 * Example 2:
 *
 *
 * Input: mat = [[0,0,0],[0,1,0],[1,1,1]]
 * Output: [[0,0,0],[0,1,0],[1,2,1]]
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 104
 * 1 <= m * n <= 104
 * mat[i][j] is either 0 or 1.
 * There is at least one 0 in mat.
 *
 *
 * Note: This question is the same as 1765: https://leetcode.com/problems/map-of-highest-peak/
 *
 *
 */
public class ZeroOneMatrix {

    // V0
    // IDEA: BFS (fixed by gemini)
    /**
     * Logic:
     * 1. Treat all 0s as sources of a BFS and add them to the queue.
     * 2. Initialize all 1s to -1 (meaning "not yet reached").
     * 3. Expand outwards. The first time we reach a -1, we know it's the shortest path.
     */
    public int[][] updateMatrix(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        Queue<int[]> queue = new LinkedList<>();

        // 1. Initialize the grid and queue
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) {
                    // Start points for BFS
                    queue.offer(new int[] { r, c });
                } else {
                    /** NOTE !!
                     *
                     *  we init non-zero val (dist in this problem) as -1
                     *  (could be MAX_VALUE as well, see V-0-0-1)
                     */
                    // Mark 1s as -1 to indicate they haven't been visited
                    mat[r][c] = -1;
                }
            }
        }

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        // 2. Perform Multi-source BFS
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0];
            int c = curr[1];

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                /** NOTE !!
                 *
                 *  ONLY when
                 *   - mat[nr][nc] == -1
                 *   - and the cell is still in boundary,
                 *
                 *   -> we add it to queue (BFS)
                 *      and update the dist
                 *
                 *  why  ?
                 *
                 *   -> with multi source BFS (without weight),
                 *      the 1st visited cell ALWAYS has
                 *      THE SMALLEST dist (see V0-0-1)
                 */
                // Check boundaries and if the cell is unvisited (-1)
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mat[nr][nc] == -1) {
                    // The distance to neighbor is current distance + 1
                    mat[nr][nc] = mat[r][c] + 1;
                    queue.offer(new int[] { nr, nc });
                }
            }
        }

        return mat;
    }

    // V0-0-1
    // IDEA:  multi-source BFS (fixed by gpt)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] updateMatrix_0_0_1(int[][] mat) {

        int l = mat.length;
        int w = mat[0].length;

        Queue<int[]> q = new LinkedList<>();

        // 1️⃣ 初始化
        for (int y = 0; y < l; y++) {
            for (int x = 0; x < w; x++) {
                if (mat[y][x] == 0) {
                    q.offer(new int[] { y, x }); // multi-source
                } else {
                    /** NOTE !!
                     *
                     *  we init non-zero val (dist in this problem) as MAX_VALUE,
                     *  so make it's easier to get the smallest dist
                     */
                    mat[y][x] = Integer.MAX_VALUE; // 當作 INF
                }
            }
        }

        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        // 2️⃣ BFS
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int y = cur[0];
            int x = cur[1];

            for (int[] d : dirs) {
                int ny = y + d[0];
                int nx = x + d[1];

                if (ny < 0 || ny >= l || nx < 0 || nx >= w)
                    continue;

                /**
                 *   NOTE !!!  ONLY update dist and `continue BFS` (enqueue) when
                 *             `new dist < cur dist`
                 *
                 *   why ?
                 *
                 *    	BFS 第一次到達某節點 ⇒ 一定是最短距離 (without weight)
                 *
                 *
                 *  -----------------
                 *
                 *  是的，你的理解方向是正確的，但要分情況說清楚。
                 *  我用「最短路 / BFS 的角度」幫你精準整理一次 👇
                 *
                 * ⸻
                 *
                 * 結論先講（重點）
                 *
                 * 當你發現某個節點目前算出的距離，沒有比「已知最短距離」更短時，
                 * 確實「不需要再從這個節點繼續 BFS」。
                 *
                 * 而你貼的這段程式碼：
                 *
                 * if (mat[ny][nx] > mat[y][x] + 1) {
                 *     mat[ny][nx] = mat[y][x] + 1;
                 *     q.offer(new int[]{ny, nx});
                 * }
                 *
                 * 👉 這是「標準且正確」的寫法，可以安全地避免不必要的 BFS。
                 *
                 * ⸻
                 *
                 * 為什麼可以這樣做？
                 *
                 * 1️⃣ BFS 的關鍵性質（無權圖）
                 *
                 * 在 每條邊權重 = 1 的情況下（上下左右走一步）：
                 * 	•	BFS 第一次到達某節點 ⇒ 一定是最短距離
                 * 	•	後續再走到同一節點，只會 距離更長或一樣
                 *
                 * ⸻
                 *
                 * 2️⃣ 你的判斷條件在做什麼？
                 *
                 * if (mat[ny][nx] > mat[y][x] + 1)
                 *
                 * 意思是：
                 * 	•	「從 (y,x) 走到 (ny,nx) 這條路」
                 * 	•	比目前記錄的最短距離還要短
                 *
                 * 只有在這種情況下：
                 * 	•	更新距離
                 * 	•	才有「繼續往外擴散 BFS」的價值
                 *
                 * ⸻
                 *
                 * 3️⃣ 如果距離沒有變短，為什麼可以剪枝？
                 *
                 * 假設：
                 *
                 * 已知 mat[ny][nx] = 3
                 * 現在從別條路走過來 mat[y][x] + 1 = 4
                 *
                 * 那麼：
                 * 	•	從這個節點繼續 BFS
                 * 	•	只會產生距離 ≥ 4 + 1 = 5 的路
                 * 	•	一定不可能比已知解更好
                 *
                 * 👉 所以 不 enqueue = 正確剪枝
                 *
                 * ⸻
                 *
                 * 與「visited[][]」的關係
                 *
                 * 你現在用的是：
                 *
                 * mat[][] 當作 distance + visited
                 *
                 * 這在以下情況是 完全 OK 的：
                 * 	•	無權圖（每步 cost = 1）
                 * 	•	要找最短距離
                 * 	•	類似題目：
                 * 	•	LC 542 – 01 Matrix
                 * 	•	LC 994 – Rotting Oranges
                 * 	•	LC 1091 – Shortest Path in Binary Matrix
                 *
                 * ⸻
                 *
                 * 什麼情況「不能」這樣剪？
                 *
                 * ⚠️ 下面這些情況就不能只靠 BFS + 剪枝
                 *
                 * ❌ 有權圖（不同 cost）
                 * 	•	例如 cost = 1 或 2
                 * 	•	需要 Dijkstra
                 * 	•	即使距離沒變短，也可能後面接到便宜的路
                 *
                 * ❌ 允許負權邊
                 * 	•	需要 Bellman-Ford
                 *
                 * ⸻
                 *
                 * 直覺版記憶法 🧠
                 *
                 * 「只有當我讓你變得更近，你才值得我再繼續走下去」
                 *
                 * 這正是你這段 code 在做的事 👍
                 *
                 * ⸻
                 *
                 * 一句話總結
                 *
                 * ✔ 是的
                 * ✔ 在 BFS 最短路中
                 * ✔ 當新距離沒有比舊距離短
                 * ✔ 不需要再從該節點繼續 BFS
                 * ✔ 你的 if (mat[ny][nx] > mat[y][x] + 1) 是「正確且高效」的寫法
                 *
                 *
                 */
                // NOTE !!!! 只在能「變更為更小距離」時才更新
                if (mat[ny][nx] > mat[y][x] + 1) {
                    mat[ny][nx] = mat[y][x] + 1;
                    q.offer(new int[] { ny, nx });
                }
            }
        }

        return mat;
    }

    // V0-1
    // IDEA:  multi-source BFS (fixed by gpt)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] updateMatrix_0_1(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0][0];
        }

        int rows = mat.length;
        int cols = mat[0].length;
        int[][] res = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        // 1️⃣ Add all 0s to the queue, set 1s as INF
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                /**
                 *  NOTE !!! below
                 *
                 *  - We use multi-source BFS starting from all 0s
                 *    to compute the minimum distance to a 0 for each 1.
                 *
                 */
                /**
                 * ## ❓ Why Does the Fixed Code Add All `0`s to the Queue (Not `1`s)?
                 *
                 * ---
                 *
                 * ## 🧠 Let's Think About the Goal
                 *
                 * The problem asks:
                 *
                 * > For each cell with a `1`, find the distance to the **nearest `0`**.
                 *
                 * There are **two basic ways** to approach this:
                 *
                 * ---
                 *
                 * ### ❌ Option A (your original code):
                 *
                 * **Start BFS from every `1`**, searching for the nearest `0`.
                 *
                 * #### Problem:
                 *
                 * * You perform a **BFS for every 1** in the matrix.
                 * * In worst case, you scan the whole matrix **once per 1**.
                 * * That’s **O(N × M × (N + M))** — very slow for large inputs.
                 *
                 * ---
                 *
                 * ### ✅ Option B (optimized):
                 *
                 * **Start BFS from every `0`**, and compute distance as you expand.
                 *
                 * #### Why this works:
                 *
                 * * You flip the problem: instead of asking *“how far is this 1 from a 0?”*, you ask *“how far can each 0 reach a 1?”*
                 * * When you expand from all 0s **at the same time**, you ensure that **each 1 gets the shortest path to a 0**, because BFS guarantees minimum-distance traversal.
                 * * Time complexity is **O(N × M)** — each cell is visited only once.
                 *
                 */
                if (mat[i][j] == 0) {
                    queue.offer(new int[] { i, j });
                    visited[i][j] = true;
                } else {
                    res[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[][] dirs = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        // 2️⃣ BFS from all 0s
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int row = curr[0];
            int col = curr[1];

            for (int[] d : dirs) {
                int newRow = row + d[0];
                int newCol = col + d[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !visited[newRow][newCol]) {
                    res[newRow][newCol] = res[row][col] + 1;
                    queue.offer(new int[] { newRow, newCol });
                    visited[newRow][newCol] = true;
                }
            }
        }

        return res;
    }

    // V1

    // V2
    // https://leetcode.com/problems/01-matrix/solutions/6750388/video-using-queue-solution-by-niits-e1z6/
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] updateMatrix_2(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;
        int[][] directions = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        Queue<int[]> queue = new ArrayDeque<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                /**
                 *  NOTE !!! below
                 *
                 */
                if (mat[i][j] == 0) {
                    queue.add(new int[] { i, j });
                } else {
                    mat[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];
            int col = cell[1];

            for (int[] direction : directions) {
                int newRow = row + direction[0];
                int newCol = col + direction[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols
                        && mat[newRow][newCol] > mat[row][col] + 1) {
                    mat[newRow][newCol] = mat[row][col] + 1;
                    queue.add(new int[] { newRow, newCol });
                }
            }
        }

        return mat;
    }


    // V3
    // https://leetcode.com/problems/01-matrix/solutions/3920110/9487-multi-source-bfs-queue-by-vanamsen-gd1q/
    // IDEA:  Multi-source BFS + Queue
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int[][] updateMatrix_3(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0)
            return new int[0][0];

        int m = mat.length, n = mat[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int MAX_VALUE = m * n;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                /**
                 *  NOTE !!! below
                 *
                 */
                if (mat[i][j] == 0) {
                    queue.offer(new int[] { i, j });
                } else {
                    mat[i][j] = MAX_VALUE;
                }
            }
        }

        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] dir : directions) {
                int r = cell[0] + dir[0], c = cell[1] + dir[1];
                if (r >= 0 && r < m && c >= 0 && c < n && mat[r][c] > mat[cell[0]][cell[1]] + 1) {
                    queue.offer(new int[] { r, c });
                    mat[r][c] = mat[cell[0]][cell[1]] + 1;
                }
            }
        }

        return mat;
    }

    // V4
    // https://leetcode.com/problems/01-matrix/solutions/6770885/java-solution-using-bfs-by-navneetksingh-qnq6/
    // IDEA: BFS
    int m, n;
    int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

    public int[][] updateMatrix_4(int[][] matrix) {
        m = matrix.length;
        n = matrix[0].length;

        int[][] result = new int[m][n];

        Queue<int[]> que = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                /**
                 *  NOTE !!! below
                 *
                 */
                if (matrix[i][j] == 0) {
                    result[i][j] = 0;
                    que.offer(new int[] { i, j });
                } else {
                    result[i][j] = -1;
                }
            }
        }

        while (!que.isEmpty()) {
            int[] cell = que.poll();
            int i = cell[0];
            int j = cell[1];

            for (int[] dir : directions) {

                int new_i = i + dir[0];
                int new_j = j + dir[1];

                if (new_i >= 0 && new_i < m && new_j >= 0 && new_j < n && result[new_i][new_j] == -1) {
                    result[new_i][new_j] = result[i][j] + 1;
                    que.add(new int[] { new_i, new_j });
                }
            }
        }

        return result;
    }






}
