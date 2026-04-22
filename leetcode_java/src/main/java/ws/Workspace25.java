package ws;

import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.SIPUSH;

import java.rmi.server.RemoteRef;
import java.util.*;

public class Workspace25 {

    // LC 1905
    // 8.22 - 32 am
    /**
     *   -> Return the number of islands in
     *   grid2 that are considered sub-islands.
     *
     *
     *  ------------
     *
     *  IDEA 1) 2 PASS DFS / BFS
     *     - first pass: mark `invalid island` in grid2
     *                   e.g. island in grid2, but water in grid1
     *
     *     - second pass:  count and check remaining island in grid2,
     *                     if they are BOTH island in grid2 and grid1,
     *                     maintain the distinct island cnt
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     */
    // 16.55 - 17.20
    // IDEA 1) 2 PASS DFS (color)
    public int countSubIslands(int[][] grid1, int[][] grid2) {
        // edge

        int l = grid1.length;
        int w = grid1[0].length;

        // int subIsland = 0;
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid2[y][x] == 1 && grid1[y][x] == 0){
                    colorHelper99(grid1, grid2, x, y);
                }
            }
        }

        int subIsland = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid2[y][x] == 1 && grid1[y][x] == 1){
                    colorHelper99(grid1, grid2, x, y);
                    subIsland += 1;
                }
            }
        }

        return subIsland;


    }

    private void colorHelper99(int[][] grid1, int[][] grid2, int x, int y){
        // edge ??

        int l = grid1.length;
        int w = grid1[0].length;

        // mark as new `color`
        grid2[y][x] = -1;

        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            // ???
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && grid2[y_][x_] == 1){
                colorHelper99(grid1, grid2, x_, y_);
            }
        }

    }








    // IDEA 1) 2 PASS DFS / BFS
    public int countSubIslands_99(int[][] grid1, int[][] grid2) {
        // edge

        int l = grid1.length;
        int w = grid1[0].length;

        // int subIsland = 0;

         for(int y = 0; y < l; y++){
             for(int x = 0; x < w; x++){
                 // ???
                 boolean[][] visited = new boolean[l][w];

                 if(grid2[y][x] == 1 && grid1[y][x] == 0){
                     dfsColor(grid1, grid2, visited, x, y, -1);
                 }
             }
         }

        int subIsland = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                // ???
                boolean[][] visited = new boolean[l][w];

                if(grid2[y][x] == 1 && grid1[y][x] == 1){
                    dfsColor(grid1, grid2, visited, x, y, -2);
                    subIsland += 1;
                }
            }
        }


        return subIsland;
    }

    // ????
    private void dfsColor(int[][] grid1, int[][] grid2, boolean[][] visited, int x, int y, int newColor){
        // edge

        int l = grid1.length;
        int w = grid1[0].length;

        // need validate here ???

        // mark as new color !!!
//        grid2[y][x] = newColor; // ??
//        visited[y][x] = true;

        int[][] moves = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && !visited[y_][x_] && grid2[y_][x_] == 1){
                // ???? do below (within for loop, or at beginning of this DFS fuc ??)
                grid2[y_][x_] = newColor; // ??
                visited[y_][x_] = true;

                dfsColor(grid1, grid2, visited, x_, y_, newColor);
            }
        }


    }

    // LC 827
    // 10.04 - 14 am
    /**
     *  -> Return the size of the
     *  largest island in grid after applying
     *  this operation.
     *
     *   NOTE:
     *      - You are allowed to change
     *        `at most `one` 0 to be 1.
     *
     *
     *
     *  ------------------
     *
     *   IDEA 1) DFS / BFS + brute force
     *
     *    -> find all `0`, and check one by one,
     *       get the max updated `1` area
     *
     *   IDEA 2) `reverse` way. DFS / BFS
     *
     *    -> get all `1` island area, and check there neighbor `0`
     *
     *
     *
     *  ------------------
     *
     *
     *
     */
    // IDEA: 2 PASS DFS
    List<List<Integer[]>> isLandList = new ArrayList<>(); // ???
    public int largestIsland(int[][] grid) {

        int l = grid.length;
        int w = grid[0].length;

        List<List<Integer>> collected = new ArrayList<>();
        boolean[][] visited = new boolean[l][w];

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 1){
                    List<Integer[]> tmp = new ArrayList<>();
                    dfsGetIslands(grid, x, y, visited, tmp);
                    // ???
                    isLandList.add(tmp);
                }
            }
        }


        int biggestArea = 0;

        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){
                if(grid[y][x] == 1){
                    biggestArea = Math.max(biggestArea,
                            dfsFlip(grid, x, y, 1));
                }
            }
        }



        return biggestArea;
    }

    private List<Integer[]> dfsGetIslands(int[][] grid, int x, int y, boolean[][] visited, List<Integer[]> tmp){

        int l = grid.length;
        int w = grid[0].length;
        int[][] moves = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        for(int[] m: moves){
            int x_ = x + m[0];
            int y_ = y + m[1];
            if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && !visited[y_][x_] && grid[y_][x_] == 1){

                // ???? do below (within for loop, or at beginning of this DFS fuc ??)
                visited[y][x] = true;
                tmp.add(new Integer[]{x,y});

                dfsGetIslands(grid, x_, y_, visited, tmp);
            }
        }

        return tmp;

    }

    private int dfsFlip(int[][] grid, int x, int y, int remainingOp){

        int l = grid.length;
        int w = grid[0].length;
        int[][] moves = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

        return 0;
    }



    // LC 1492
    // 7.31 - 41 am
    /**
     *
     *   ->  return the kth factor in this
     *       or return -1 if n has less than k factors
     *
     *
     *   -  a list of all factors of n
     *      sorted in `ascending` order
     *
     *
     *   n, k: positive int
     *
     *   factor of n
     *      -> an integer i where n % i == 0.
     *
     *  --------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) MATH
     */
    public int kthFactor(int n, int k) {
        // edge
        if(n <= 0){
            return -1;
        }

        for(int i = 1; i < n + 1; i++){
            if(n % i == 0){
                k -= 1;
                // ???
                if(k == 0){
                    return i;
                }
            }
        }

        return -1; // ???
    }


    // LC 1167
    // 8.04 - 14 am
    /**
     * -> Return the minimum cost of
     * connecting all the given sticks into
     * one stick in this way.
     *
     * -------------
     *
     *   ex 1)
     *
     *    sticks = [2,4,3]
     *
     *    -> pay = (2+3) = 5, [5,3]
     *    -> pay = 5 + (5+3) = 13 ??? [8]
     *
     *
     *   ex 2)
     *    sticks = [1,8,3,5]
     *
     *    -> p = 1+3 =4    [ 4,8,5]
     *    -> p = 4 + (5+4) = 13,    [9,8]
     *    -> p  = 13 + 17 = 30, [17]
     *
     *
     */
    // IDEA 1) SMALL PQ
    public int connectSticks(int[] sticks) {
        // edge

        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        int cost = 0;

        for(int x: sticks){
            pq.add(x);
        }

        while (pq.size() > 1){
            int s1 = pq.poll();
            int s2 = pq.poll();
            cost += (s1 + s2);
            pq.add(s1 + s2);
        }


        return cost;
    }



    // LC 1048
    // 8.20 - 30 am
    /**
     *  ->Return the length of the longest possible
     *  word chain with words chosen
     *  from the given list of words.
     *
     *  - wordA is a predecessor of wordB
     *    - if and only if we can `insert`
     *      exactly `one` letter anywhere in wordA
     *      `without` changing the` order` of the
     *      other characters to make it equal to wordB.
     *
     *
     *   - A word chain is a sequence of words
     *     [word1, word2, ..., wordk] with k >= 1
     *
     *      -> w1 is predecessor of w2
     *         w2 is predecessor of w3
     *         ...
     *
     *  ---------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DP
     *
     *     ???
     *
     *     DP def:
     *
     *      dp[i][j]:
     *         if `word i` is predecessor of `word j` ???
     *
     *     DP eq
     *       dp[i][j] =
     *
     *          if(word i -1 is predecessor of word i)
     *            dp[i][j] = max( dp[i-1][j] + 1, dp[i][j] )
     *          else:
     *            dp[i][j] = 1 ???
     *
     *
     *  ---------------
     *
     */
    // 15.42 - 52
    /**
     *
     *  NOTE !!
     *
     *   wordA is a predecessor of wordB
     *      if and only if we can insert
     *      exactly one letter anywhere in
     *      wordA without changing the order
     *      of the other characters to make it equal to wordB.
     *
     *
     *   -> if wordA is `predecessor` of worB
     *      -> can ONLY insert ONE letter anywhere in wordA
     *         to make wordA `equal to` wordB
     *
     *
     * ----------
     *
     *
     *
     *  IDEA 1) SORT + DP ???
     *
     *   1. sort (small -> long ??)
     *   2. dp[i][j]
     *       =
     *
     *
     *   IDEA 2) SORT + BRUTE FORCE ???
     *
     *
     */
    //  IDEA 2) SORT + BRUTE FORCE ???
    public int longestStrChain(String[] words) {
        // edge


        List<String> list = new ArrayList<>();
        for(String w: words){
            list.add(w);
        }
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = o1.length() - o2.length();
                return diff; // ??
            }
        });

        int n = words.length;
        // ???
        /**
         *   DP def:
         *      dp[i][j]:
         *         is word i is `Predecessor` of word j
         *
         *  DP eq:
         *
         *      dp[i][j]
         *         = if (isPredecessor(word_i, word_j):
         *             dp[i][j] = max(  dp[i-1][j], dp[i-1][j-1] )
         *
         *
         *
         *
         */
        int[][] dp = new int[n][n]; // ???

        int longestLen = 1; // ??

        // ??? double loop
        for(int i = 0; i < list.size(); i++){
            for(int j = i + 1; j < list.size(); j++){
                String s1 = list.get(i);
                String s2 = list.get(j);
                if(!isPredecessor(s1, s2)){
                    break; // ??
                }
                longestLen = Math.max(longestLen, j - i + 1);
            }
        }

        return longestLen;
    }


    // ???
    /**
     *   *   -> if wordA is `predecessor` of worB
     *      *      -> can ONLY insert ONE letter anywhere in wordA
     *      *         to make wordA `equal to` wordB
     */
    private boolean isPredecessor(String s1, String s2){
        if(s1.equals(s2)){
            return true;
        }
//        if(s2.length() - s1.length() == 1){
//            return true;
//        }
        if(s2.length() - s1.length() >= 2){
            return false;
        }

        int quota = 1;

        // ??
        int i = 0;
        int j = 0;

        while (i < s1.length() && j < s2.length()){
            if(s1.charAt(i) == s2.charAt(j)){
                i += 1;
                j += 1;
            }else{
                if(quota == 0){
                    return false;
                }
                i += 1;
                quota -= 1;
            }
        }

        // ???
        return  j == s2.length() - 1;
    }








    // LC 931
    // 10.27 - 37 am
    /**
     *  ->  return the minimum sum
     *  of any falling path through matrix.
     *
     *    - n x n matrix
     *
     *    - can ONLY start from `first row`
     *
     *    - can ONLY move below dirs:
     *       - (row + 1, col)
     *       - (row + 1, col - 1)
     *       - (row + 1, col + 1)
     *
     *
     *
     *
     *
     *  ----------------
     *
     *   IDEA 1) BRUTE FORCE ???
     *
     *
     *   IDEA 2) DP ???
     *
     *      2D DP
     *
     *      - DP def
     *
     *           dp[i][j] = min path sum when visit (i,j)
     *
     *      - DP eq
     *
     *          `move backward ???`
     *
     *          dp[i][j] =
     *              ( if still `in range` )
     *              min ( dp[i-1][j], dp[i-1][j-1], dp[i+1][j+1] )
     *
     *
     *
     *
     *  ----------------
     *
     * *    - can ONLY move below dirs:
     *      *       - (row + 1, col)
     *      *       - (row + 1, col - 1)
     *      *       - (row + 1, col + 1)
     *
     */
    // 16.28 - 43 pm
    // 2D DP ???
    public int minFallingPathSum(int[][] matrix) {
        // edge

        int n = matrix.length;

        /**  DP def:
         *
         *  dp[i][j] = min `sum of any falling path`
         *             to reach cell (i,j)
         */
        int[][] dp = new int[n][n]; // ??

        // init ??
        for(int x = 0; x < n; x++){
            dp[0][x] = matrix[0][x]; // /???
        }

        // NOTE !!! we DON'T below init
   /*     // /??
        for(int y = 0; y < n; y++){
            dp[y][0] += matrix[y][0];
        }*/


        int minPath = 100 * n * n; // ??


        // ???
        /**  DP eq
         *
         *  dp[i][j] = ??
         */
        for(int y = 1; y < n; y++){
            for(int x = 1; x < n; x++){

                // NOTE !!!
                // we can use up, left, right val

                dp[y][x] = Math.min(dp[y-1][x], Math.min(
                        dp[y-1][x-1],
                        x + 1 < n ? dp[y-1][x+1] : 0
                )) + matrix[y][x]; //????
            }
        }

        for(int x = 0; x < n; x++){
            minPath = Math.min(minPath, dp[0][x]);
        }

        return minPath;
    }








    public int minFallingPathSum_99(int[][] matrix) {
        // edge

        int n = matrix.length;

        // ??
        int[][] dp = new int[n + 1][n + 1]; // ??

        // init ??
       dp[0][0] = matrix[0][0];

        for(int x = 1; x < n + 1; x++){
            dp[0][x] = matrix[0][x];
        }

        for(int y = 1; y < n + 1; y++){
            dp[y][0] += matrix[y][0];
        }

        int minPath = 100 * n * n; // ??

        /**
         *      * *    - can ONLY move below dirs:
         *      *      *       - (row + 1, col)
         *      *      *       - (row + 1, col - 1)
         *      *      *       - (row + 1, col + 1)
         *      *
         */
        // ???
        for(int y = n + 1; y > 0; y--)
            for(int x = n + 1; x > 0; x--){
               // dp[0][x] = matrix[0][x];
               int val1 = 100 * n * n;
               int val2 = 100 * n * n;
               int val3 = 100 * n * n;

               // ??
               if(y - 1 >= 0){
                   val1 = matrix[y-1][x];
               }
                if(y - 1 >= 0 && x + 1 < n + 1){
                    val2 = matrix[y-1][x+1];
                }
                if(y - 1 >= 0 && x + 1 < n + 1){
                    val3 = matrix[y-1][x+1];
                }

                dp[y][x] = Math.min(dp[y][x],
                        Math.min(Math.min(val1, val2), val3));
            }

        for(int x = 0; x < n + 1; x++){
           minPath = Math.min(minPath, dp[0][x]);
        }



        return minPath;
    }




    // LC 449
    // 16.37 - 57 pm
    /**
     *  -> find out how the ball
     *     could drop into the hole by
     *     moving the shortest distance
     *
     *
     *   1 means the wall
     *   0 means the empty space
     *
     *   -------------
     *
     *   IDEA 1) BFS ??
     *
     *
     */
    // IDEA 1) BFS ??
    class MyRecord{
        int x;
        int y;
        StringBuilder sb;

        MyRecord(int x, int y){
            this.x = x;
            this.y = y;
            this.sb = new StringBuilder(); // ???
        }

        MyRecord(int x, int y, StringBuilder sb){
            this.x = x;
            this.y = y;
            this.sb = sb;
        }
    }

    public String findShortestWay(int[][] maze, int[] ball, int[] hole) {
        // edge
        if(ball[0] == hole[0] && ball[1] == hole[1]){
            return "";
        }


        int l = maze.length;
        int w = maze[0].length;

        // ???
        Deque<MyRecord> dq = new LinkedList<>();

        dq.add(new MyRecord(ball[0], ball[1])); // ???

        // ??
        String moveStr = "ludr";
        // ??
        Map<String, Integer[]> moveMap = new HashMap<>();
        moveMap.put("l", new Integer[]{0,-1});
        moveMap.put("u", new Integer[]{-1,0});
        moveMap.put("d", new Integer[]{1,0});
        moveMap.put("r", new Integer[]{0,1});

        //int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };

        while (!dq.isEmpty()){
            // ???
            int size = dq.size();
            for(int i = 0; i < size; i++){

                MyRecord record = dq.poll();
                int x = record.x;
                int y = record.y;
                StringBuilder sb = record.sb;

                // ?? if meet the `hole`
                if(x == hole[0] && y == hole[1]){
                    return sb.toString(); // /??
                }

                // mark as visit ????
                maze[y][x] = -1;

                for(char ch: moveStr.toCharArray()){
                    Integer[] m = moveMap.get(String.valueOf(ch));
                    int x_ = x + m[0];
                    int y_ = y + m[1];
                    // ??
                    if(x_ >= 0 && x_ < w && y_ >= 0 && y_ < l && maze[y_][x_] == 0){
                        sb.append(ch);
                        dq.add(new MyRecord(x_, y_, sb));
                    }
                }

            }

        }


        return "impossible";
    }


    // LC 312
    // 17.25 - 42 pm
    /**
     *  -> Return the `maximum`
     *     `coins` you can collect
     *         by bursting the balloons wisely.
     *
     *  - You are asked to burst all the balloons
     *  - if burst i ballon, can get
     *      - nums[i - 1] * nums[i] * nums[i + 1] coins
     *      - if the idx is OUT of boundary
     *         - treat as 1 (otherwise)
     *
     *
     *      -> coin = nums[i - 1] * nums[i] * nums[i + 1]
     *
     *
     *  --------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) GREEDY ????
     *     -> ALWAYS `burst smallest` ballon ????
     *
     *   IDEA 3) DP ?????
     *
     *     DP def:
     *       dp[i][j]
     *         max coin can get within [i,j] ?????
     *
     *     DP eq:
     *        dp[i][j] =
     *
     *
     *
     *
     *  --------------
     *
     */
    public int maxCoins(int[] nums) {
        // edge

        int n = nums.length;
        // /?
        int[][] dp = new int[n][n];

        // init ???
        for(int i = 0; i < n; i++){
            dp[i][i] = getCoin(nums, i);
        }

        for(int i = 1; i < n; i++){
            for(int j = 1; j < n; j++){
                // ????
            }
        }


        // ????
        return dp[0][n];
    }


    private int getCoin(int[] nums, int i){

        return 0;
    }



    // LC 1074
    // 11.52 - 12.02
    /**
     *
     *  ->  return the number
     *  of non-empty `submatrices` that `sum` to target.
     *
     *
     *  ----------------
     *
     *   IDEA 1) BRUTE FORCE ???
     *
     *   IDEA 2) PREFIX SUM ????
     *
     *
     *  ----------------
     *
     *   ex 1)
     *
     *
     *   ex 2)
     *
     *   matrix = [
     *     [1,-1],
     *     [-1,1]
     *    ],
     *
     *
     *    target = 0
     *
     *
     */
    public int numSubmatrixSumTarget(int[][] matrix, int target) {

        return 0;
    }


    // LC 1292
    // 11.03 - 30 am
    /**
     *  ->  return the `maximum` `side-length` of a
     *      square with a `sum` `less than or equal` to threshold
     *     - OR return 0 if there is NO such square.
     *
     *   ------------------
     *
     *    IDEA 1) BRUTE FORCE
     *
     *    IDEA 2) PREFIX SUM ?????
     *
     *      - sum[i,j]
     *        = prefix[j] - prefix[i-1] ???
     *
     *    IDEA 3) BINARY SEARCH ???
     *
     *   ------------------
     *
     *
     */
    // IDEA 2) PREFIX SUM
    public int maxSideLength(int[][] mat, int threshold) {
        // edge

        int l = mat.length;
        int w = mat[0].length;

        // NOTE !!!!
        // build prefix sum with padding (m+1 x n+1)
        int[][] prefix = new int[l + 1][w + 1];


        //int[][] prefix = new int[l][w]; // ???

        // NOTE !!! NO NEED INIT
//        // init
//        for(int y = 0; y < l; y++){
//            prefix[y][0] = mat[y][0];
//        }
//        for(int x = 0; x < w; x++){
//            prefix[0][x] = mat[0][x];
//        }

        int ans = 0;


        // ???
        for(int y = 1; y < l + 1; y++){
          for(int x = 1; x < w + 1; x++){
              /**
               *     1 1 1 1 1
               *     2 2 2 2 2
               *     3 3 3 3 3
               *     4 4 4 4 4
               *
               */

              /**
               *
               *        prefix[i][j] = mat[i - 1][j - 1]
               *                     + prefix[i - 1][j]
               *                     + prefix[i][j - 1]
               *                     - prefix[i - 1][j - 1];
               */
          prefix[y][x] = ( prefix[y-1][x-1]
                  + mat[y][x] );



//                // ??
//                // ?? ONLY update `square prefix sum`
//                if(x == y){
//                    prefix[y][x] = ( prefix[y-1][x] + prefix[y][x-1] );
//                }
//                // ??? get the `actual prefix sum and check if <= threshold
//                // ???
//                int prefixVal = prefix[y][x] - prefix[y-1][x-1];
//                if(prefixVal <= threshold){
//                    ans = Math.max(ans, prefixVal);
//                }
//
////                // or, use mat cell val as prefix[y][x]
////                else{
////                    prefix[y][x] = mat[y][x];
////                }
//            }
             }
        }



        return ans;
    }


    // LC 865
    // 12.39 - 59 pm
    /**
     *
     *  -> Return the smallest subtree
     *  such that it contains all the deepest
     *  nodes in the original tree.
     *
     *
     *  ----------------
     *
     *   IDEA 1)  pre-order traverse + DFS ??? + LCA ??
     *           (lowest common ancestor)
     *
     *
     *   ----------------
     *
     *
     */
    // IDEA: LCA (POST order) + custom class (fixed by gemini)
    class Result {
        TreeNode node;
        int dist;

        Result(TreeNode node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }


    // ???
    //TreeNode deepstNode = null;
    //TreeNode deepestNode = null;
    //Result res = null;
    public TreeNode subtreeWithAllDeepest(TreeNode root) {

        //TreeNode deepstNode = root;

        //Result  res = dfsDeepstNode(new Result(root, 0));
        Result  res = dfsDeepstNode(root);
        // ???
        return res.node;
    }

    // ???
    private Result dfsDeepstNode(TreeNode node){
        // /??
//        TreeNode node = result.node;
//        int dist = result.dist;

        // edge
        if(node == null){
            //return 0; // ???
            //return false; // ???
            // ???
            return new Result(node, 0);
        }

//        boolean _left = dfsDeepstNode(root.left);
//        boolean _right = dfsDeepstNode(root.right);

        Result _left = dfsDeepstNode(node.left);
        Result _right = dfsDeepstNode(node.right);


        // NOTE !!! below
        if(_left.dist > _right.dist){
            return new Result(_left.node, _left.dist + 1);
        }else if(_left.dist < _right.dist){
            //return _left;
            return new Result(_right.node, _right.dist + 1);
        }

        return new Result(node, _left.dist + 1); // ???

/*
        // ????
        if(node.left == null && node.right == null){
            return new Result(node, dist); // ???

        }else{
            if(node.left == null){
                return new Result(node.right, dist + 1); // ???
            }
            return new Result(node.left, dist + 1); // ???
        }
*/

        //return null;



    }


    // LC 486
    // 8.46 - 56 am
    /**
     *   IDEA 1) DP
     *
     *    DP def:
     *
     *        dp[i][j]: the max `relative diff score` between
     *                  play1 and play2
     *                  within idx: [i, j]
     *    DP eq
     *
     *        dp[i][j] =
     *
     *
     *  ---------------
     *
     *   ex 1)
     *
     *     nums = [1,2,4,5,6,7]
     *
     */
    public boolean predictTheWinner(int[] nums) {
        // edge??

        int n = nums.length;

        // ???
        // dp[i][j] = max score `difference`
        // `current` player can get from nums[i..j]
        int[][] dp = new int[n][n];

//        dp[0][0] = nums[0]; // ?????
        // INIT ???
        // base case
        // ???????
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }


        int maxDiffScore = 0;

        // NOTE !!!
        // LOOP ON LEN
        for(int len = 2; len <= n; len++){
            for(int i = 0; i <= n - len; i++){
                // ???
                int j = i + len - 1;

                dp[i][j] = Math.max(
                        nums[i] - dp[i+1][j],
                        nums[j] - dp[i][j-1]
                );
            }
        }


        // ???
//        for(int i = 1; i < n; i++){
//            for(int j = 1; j < n; j++){
//
//                dp[i][j] = Math.max(
//                        nums[i-1] + dp[i-1][j],
//                        nums[j - 1] + dp[i][j-1]
//                );
//
//            }
//        }

        // ???
        //return maxDiffScore >= 0;
        return dp[0][n - 1] >= 0;
    }










    // LC 1248
    // 7.40 - 50 am
    /**
     *  -> Return the number of nice sub-arrays.
     *
     *   nums: int arr
     *   k: int
     *
     *   `nice array`:
     *      if a continuous arr
     *      has `k` odd number on it
     *
     *  -----------------------
     *
     *  IDEA 1) SLIDE WINDOW
     *
     *    for (int r = 0; r < len; r++)
     *      while  condition
     *         ...
     *         l += 1
     *
     *     ...
     *
     *  IDEA 2) BRUTE FORCE ????
     *
     *  IDEA 3) HASH ???
     *
     *
     *  -----------------------
     *
     *  ex 1)
     *
     *  Input: nums = [1,1,2,1,1], k = 3
     *  Output: 2
     *
     *  -> [1,1,2,1,1]        map: {0: 1}, cnt = 0
     *      x
     *
     *     [1,1,2,1,1]        map: {0: 1, 1:2}, cnt = 0
     *        x
     *
     *     [1,1,2,1,1]        map: {0: 1, 1:2, 2:2}, cnt = 0
     *          x
     *
     *    [1,1,2,1,1]        map:  {0: 1, 1:2, 2:2, 3:3}, cnt = 1
     *           x
     *                           k-x = 3 - 3 = 0 in map
     *                           cnt += (1) ??? -> cnt = 2
     *
     *   [1,1,2,1,1]        map: {0: 1, 1:2, 2:2, 3:3, 4:4}
     *            x
     *
     */
    //IDEA 3) HASH ??? + prefix sum ???
    public int numberOfSubarrays(int[] nums, int k) {
        // edge

        int cnt = 0;

        // ???
        // { idx : odd_cnt_till_now }  // ???
        //  -> e.g. { idx : prefix_sum_of_odd_num_cnt }
        /**
         *   ??
         *
         *   x + y = k
         *   -> if meet x, check if y in map
         *   -> e.g. check if k - x in map ???
         *
         */
        Map<Integer, Integer> map = new HashMap<>();
        int curCnt = 0;


        return cnt;
    }





    //  IDEA 1) SLIDE WINDOW
    public int numberOfSubarrays_99(int[] nums, int k) {
        // edge
        int l = 0;
        int cnt = 0;

        // ???
        // { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        int curCnt = 0;

        for(int r = 0; r < nums.length; r++){
            int rightVal = nums[r];
            if(rightVal % 2 == 1){
                curCnt += 1;
            }
            while (curCnt > k && l <= r){
                // ???
                int leftVal = nums[l];
                if(leftVal % 2 == 1){
                    curCnt -= 1;
                }
                if(map.get(leftVal) - 1 == 0){
                    map.remove(leftVal);
                }else{
                    map.put(leftVal, map.get(leftVal) - 1);
                }
                l += 1;
            }

            // ???
            if(curCnt == k){
                cnt += 1;
            }

        }

        return cnt;
    }


    // LC 10
    // 10.47 - 57
    /**
     *   -> Return a boolean indicating whether
     *   the matching covers the entire input string (not partial).
     *
     *   '.' Matches any single character
     *
     *   '*' Matches zero or more of the preceding element.
     *
     *
     *   -----------------
     *
     *
     *    IDEA 1) BRUTE FORCE ???
     *
     *    IDEA 2) 2D DP ???
     *
     *      DP def:
     *         boolean dp[i][j]
     *           - can pattern match s in index [i, j ] ???
     *
     *
     *      DP eq:
     *
     *        dp[i][j]
     *          = if p == '*':
     *              dp[i][j]  =  dp[i][j-1] ???
     *
     *            if p == '.'
     *
     *               dp[i][j]  =  dp[i][j-1] ???
     *
     *
     *   -----------------
     *
     *
     *
     *
     *
     */
    public boolean isMatch(String s, String p) {

        return false;
    }



    // LC 1150
    // 7.43 - 53 am
    /**
     *
     *  IDEA 1) : HASHMAP
     *
     *  IDEA 2) : binary search ????
     *
     */
    public boolean isMajorityElement(int[] nums, int target) {
        // edge


        int firstIdx = findFirstIdx3(nums, target);
        // ??
        if(firstIdx == -1){
            return false;
        }
        int possibleLastIdx = firstIdx + nums.length / 2;

        return possibleLastIdx < nums.length && nums[possibleLastIdx] ==  target;
    }


    private int findFirstIdx3(int[] nums, int target){
        //int n = nums.length;
        int l = 0;
        int r = nums.length - 1;

        int firstIdx = -1;

        while (r >= l){
            int mid = l + (r - l) / 2;
            int midVal = nums[mid];
            // ??
            if(midVal >= target){
                // ???
                if(midVal ==  target){
                    firstIdx = mid;
                }
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }


        return firstIdx;
    }







    // 10.14 - 24 am
    //  IDEA 2) : binary search ????
    public boolean isMajorityElement_97(int[] nums, int target) {
        // edge

        int n = nums.length;
        int l = 0;
        int r = nums.length - 1;

        while (r >= l){
            int mid = l + (r - l) / 2;
            int midVal = nums[mid];

            if(midVal == target){
                // ??
                int firstIdx = findFirstIdx2(nums, mid, target);
                if(firstIdx == -1){
                    return false;
                }

                int possibleLastIdx = firstIdx +  n / 2;
                if(possibleLastIdx < n  && nums[possibleLastIdx] == target){
                    return true;
                }
                return false; // ???
            }
            else if(midVal > target){
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }


        return false;
    }

    // ???
    private int findFirstIdx2(int[] nums, int idx, int target){
        while (idx > 0 && nums[idx] == target){
            idx -= 1;
        }
        return idx + 1; // ????
    }







    //  IDEA 2) : binary search ????
    public boolean isMajorityElement_98(int[] nums, int target) {
        // edge

        int n = nums.length;


        // ???
        int l = 0;
        int r = nums.length - 1; // ???
        while (r >= l){
            int mid = l + (r - l) / 2;
            int midVal = nums[mid];
            // ???
            if(midVal == target){
                int firstIdx = findFirstIdx(nums, mid, midVal);
                // ???
                int tmp = mid;
                while (tmp < r && nums[tmp] == target){
                    tmp += 1;
                }
                return (tmp - firstIdx + 1) >= n / 2;
            }
            // ???
            else if(midVal > target){
                r = mid - 1;
            }else{
                l = mid + 1; // ??
            }
        }


        return false;
    }

    private int findFirstIdx(int[] nums, int startIdx, int val){
       // int res = -1;
        while (startIdx > 0 && nums[startIdx] == val){
            startIdx -= 1;
        }
        return startIdx;
    }







    // IDEA 1) : HASHMAP
    public boolean isMajorityElement_99(int[] nums, int target) {
        // edge

        // map: { val : cnt }
        Map<Integer, Integer> map = new HashMap<>();
        for(int x: nums){
            map.put(x, map.getOrDefault(x, 0) + 1);
        }

        int n = nums.length;
        for(int k: map.keySet()){
            if(map.get(k) >= n/2){
                return true;
            }
        }

        return false;
    }


    // LC 26
    // 8.09 - 19 am
    /**
     *
     *   IDEA 1) 2 POINTERS
     *
     *
     *  -------------
     *
     *  ex 1)
     *
     *   nums = [1,1,2]
     *
     *  -> [1,1,2]
     *      l   r
     *
     *     [1,1,2]
     *        l r
     *
     *     [1,2, _]
     *
     *
     *  ex 2)
     *
     *   [0,0,1,1,1,2,2,3,3,4]
     *
     *
     *  ->
     *
     *    [0,0,1,1,1,2,2,3,3,4]
     *     l r
     *
     *
     *    [0,1,0,1,1,2,2,3,3,4]
     *       l r
     *
     *    [0,1,0,1,1,2,2,3,3,4]
     *       l       r
     *
     *
     *  ex 3)
     *
     *   [0,0,1,1,1,2,2,3,3,4]
     *    l r
     *
     *   [0,1,0,1,1,2,2,3,3,4]
     *    l l r
     *
     *   [0,1,0,1,1,2,2,3,3,4]
     *      l   r
     *
     *   [0,1,0,1,1,2,2,3,3,4]
     *      l     r
     *
     *   [0,1,2,1,1,0,2,3,3,4]
     *      l l     r
     *
     *   [0,1,2,1,1,0,2,3,3,4]
     *        l       r
     *
     *    [0,1,2,3,1,0,2,1,3,4]
     *           l       r
     *
     *    [0,1,2,3,4,0,2,1,3,1]
     *           l l        r r
     *
     */
    // IDEA 1) 2 POINTERS
    public int removeDuplicates(int[] nums) {
        // edge

        /** logic:
         *
         *  1. 2 pointers (f, s)
         *       s starts from 0
         *       f starts from 1
         *
         *  2. if nums[l] != nums[r]
         *        l += 1
         *        // swap
         *
         *  3. .... return l
         *
         *
         */
        int l = 0;
        // ??
        for(int r = 1; r < nums.length; r++){
            if(nums[l] != nums[r]){
                l += 1;
                int tmp = nums[r];
                nums[r] = nums[l];
                nums[l] = tmp;
            }
        }

        return l;
    }





    // IDEA 1) 2 POINTERS
    public int removeDuplicates_xxx(int[] nums) {
        // edge

        int l = 0;
        //int r = 1; // ???

        // ???
        for(int r = 1; r < nums.length; r++){
            if(nums[r] != nums[l]){
                l += 1;
                int tmp = nums[r];
                nums[r] = nums[l];
                nums[l] = tmp;
            }
        }

        return l + 1;
    }



    // LC 80
    // 8.43 - 53 am
    /**
     *
     *   at most twice
     *
     *
     *   ------------
     *
     *   IDEA 1) 2 POINTERS ???
     *
     *
     *   ------------
     *
     *
     *   ex 1)
     *
     *   Input: nums = [1,1,1,2,2,3]
     *   Output: 5, nums = [1,1,2,2,3,_]
     *
     *
     *   [1,1,1,2,2,3]
     *    l r
     *
     *   [1,1,1,2,2,3]
     *    l   r
     *
     *   [1,1,2,1,2,3]
     *    l   l r
     *
     *   [1,1,2,1,2,3]
     *        l   r
     *
     *    [1,1,2,1,2,3]
     *         l l    r
     *
     *
     *
     *
     */
//    public int removeDuplicates(int[] nums) {
//
//        return 1;
//    }


    // LC 765
    // 10.45 -55 am
    /**
`     *  -> Return the `minimum` number of `swaps`
     *     so that `every couple` is sitting side by side`
     *
     *     row[i]: person id at idx = i
     *
     *     couple: (0,1), (1,2)...
     *              -> e.g. (2n-2, 2n-1) pair
     *
     *   ---------------------
     *
     *    IDEA 1) BRUTE FORCE
     *
     *    IDEA 2) GREEDY ???
     *
     *    IDEA 3) HASHMAP ???
     *      { val : idx }
     *
     *
     *   IDEA 4) DP ???
     *
     *
     *    if row size = n
     *     -> MUST be 0....n-1
     *     -> so the couple MUST be
     *         - (0,1), (2,3), .... (n-2, n-1) // ??
     *
     *
     *   ---------------------
     *
     *   ex 1)
     *    row = [1,3,0,2]
     *
     *    -> map = { 1: 0, 3: 1, 0: 2, 2: 3 }
     *
     *    -> [1,2,0,3]
     *       [1,2,3,0]
     *       ..
     *
     *       or
     *
     *       [1,0,3,2]
     *
     *       or
     *
     *       ...
     *
     *  ex 2)
     *
     *   row = [1,3,0,5,2.4]
     *
     *   ->
     *    [1,0,3,5,2,4]
     *
     *
     *
     */
    public int minSwapsCouples(int[] row) {
        // edge ???

        // map: { val : idx }
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < row.length; i++){
            map.put(row[i], i);
        }

        //???
        int swap = 0;
        int l = 1; // ??

        while (l < row.length - 1){
            // ???
            if(isPair(row[l-1], row[l])){
                l += 2; // /??
            }
            // swap
            // get min possible `pair candidate` from map
            // swap
            if(map.containsKey(row[l] - 1)){
                int tmp = row[l+1];
                // ???
                row[row[l+1]] = row[l] - 1;
                row[row[l] - 1] = tmp;
                // need to updare mp ????
                swap += 1;
            }
            l += 2; // ????
        }

        return -1;
    }

    // ???
    private boolean isPair(int x, int y){
        return Math.abs(x - y) == 1;
    }
    


    // LC 2406
    // 7.30 - 40 am
    /**
     *  -> Return the minimum number
     *  of groups you need to make.
     *
     *
     *  - intervals[i] = [lefti, righti]
     *                  the inclusive
     *                  interval [lefti, righti].
     *
     *
     *  - NO overlap for intervals in same group
     *
     *
     *
     *  ---------------
     *
     *  IDEA 1) PQ / SORTING ????
     *
     *
     *  IDEA 2) SCAN LINE ????
     *
     *
     *
     *  ---------------
     *
     *
     *
     */
    // IDEA 1) PQ / SORTING ????
    public int minGroups(int[][] intervals) {


        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o1[0] - o2[0];
//                return diff == 0 ? o1[1] - o2[1]: diff;
                return o1[1] - o2[1]; // ???
            }
        });

        // sort ???
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[0]; // ?????
                return diff;
            }
        });

        int group = 0;

        List<Integer[]> tmp = new ArrayList<>(); //??

        // ???
        for(int[] x: intervals){
            if(!pq.isEmpty() && pq.peek()[1] < x[0]){
               // pq.add(new Integer[]{x[0], x[1]});

            }
        }


        return 0;
    }






    // IDEA 1) PQ / SORTING ????
    public int minGroups_99(int[][] intervals) {
        // edge


        // ???
        // PQ: small PQ
        // sort on 1st element (small -> big)
        //    sort on 2nd element (small -> big ????
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[0] - o2[0];
//                if(diff == 0){
//                    return o1[1] - o2[1];
//                }
//                return diff;
                return diff == 0 ? o1[1] - o2[1]: diff;
            }
        });

        for(int[] x: intervals){
            pq.add(new Integer[]{x[0], x[1]});
        }

        int group = 0;

        // ???
        while (!pq.isEmpty()){
            List<Integer[]> tmp = new ArrayList<>();
            List<Integer[]> collected = new ArrayList<>();
            Integer[] cur = pq.poll();
            if(collected.isEmpty() || collected.get(collected.size() - 1)[1] < cur[0]){
                collected.add(cur);
            }else{
                tmp.add(cur);
            }
            // ??? loop all elements in pq for cur group
            // add all of the tmp element back to PQ ??
            if(!tmp.isEmpty()){
                for(Integer[] y: tmp){
                    pq.add(y);
                }
            }
        }


        return group;
    }


    // LC 1675
    // 8.27 - 39 am
    /**
     *  -> Return the minimum deviation (偏差)
     *  the array can have after
     *  performing some number of operations.
     *
     *    - nums: n positive int
     *
     *
     *     can two types of operations
     *     on any element of the array
     *     any number of times:
     *
     *   - OP:
     *      - op 1:  if element is `even` divide by 2
     *      - op 2: if  ... `odd`, multiply by 2
     *
     *
     *  NOTE:
     *   deviation:
     *      the `maximum` difference
     *      between `any two elements` in the array.
     *
     *
     *  ---------------
     *
     *   IDEA 1) PQ ????
     *     -> big, small PQ ????
     *
     *
     *  ---------------
     *
     *
     */
    // IDEA 1) PQ ????
    public int minimumDeviation(int[] nums) {
        // edge

        // ???
        PriorityQueue<Integer> bigPQ = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> smallPQ = new PriorityQueue<>();


        // ??? sort (small -> big)
        //HashSet<Integer> set = new HashSet<>();
        //Arrays.sort(nums); // default is small -> big ???

        int minDeviation = nums[nums.length - 1] - nums[0]; // ???

        // ??? add num to PQs
        int n = nums.length; // /?
        //int half = n / 2; // ???

        for(int x: nums){
            bigPQ.add(x);
            smallPQ.add(x);
        }

        // ???
        while (!bigPQ.isEmpty() && !smallPQ.isEmpty()){
            int curBig = updateWithOp(bigPQ.poll());
            int curSmall = updateWithOp(smallPQ.poll());

            // ???
            minDeviation = Math.min(minDeviation,
                    Math.abs(curBig - curSmall)
            );

            bigPQ.add(curBig);
            smallPQ.add(curSmall);
        }



        return minDeviation;
    }


    private int updateWithOp(int x){
        return x % 2 == 1 ? x * 2 : x / 2;
    }


}
