package ws;

import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.IINC;
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
    // 10.17 - 34 am
    // IDEA 1) PREFIX SUM ????? + HASHMAP.
    // ??? `k odd` numbers
    //        = (k) - (k-1) cnt  ???
    public int numberOfSubarrays(int[] nums, int k) {
        // edge
        //int cnt = 0;

        /**  NOTE !!!
         *
         *  map: frequency of prefix sums
         *
         *  -> {prefix_sum: cnt}
         *
         *
         */
        // map: {odd_num_prefix_sum: 1}
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
        // NOTE !!! base case:
        map.put(0, 1);

        int oddCntTillNow = 0;
        int oddSubArr = 0;

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];

            // ???
            if(val % 2 == 1){
                oddCntTillNow += 1;
            }

            // ???
            // prefix_sum_1 - prefix_sum_2 = k
            // -> prefix_sum_1 - k = prefix_sum_2  // ????
            if(map.containsKey(oddCntTillNow - k)){
                oddSubArr += map.get(oddCntTillNow - k);
            }


            map.put(oddCntTillNow,
                    map.getOrDefault(oddCntTillNow, 0) + 1
            ); // ???


        }


        return oddSubArr;
    }








//
//    private static int getKey(int k, int oddCntTillNow) {
//        return oddCntTillNow - k;
//    }


    //IDEA 3) HASH ??? + prefix sum ???
    public int numberOfSubarrays_93(int[] nums, int k) {
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
//    // IDEA 1) 2 POINTERS
//    public int removeDuplicates(int[] nums) {
//        // edge
//
//        /** logic:
//         *
//         *  1. 2 pointers (f, s)
//         *       s starts from 0
//         *       f starts from 1
//         *
//         *  2. if nums[l] != nums[r]
//         *        l += 1
//         *        // swap
//         *
//         *  3. .... return l
//         *
//         *
//         */
//        int l = 0;
//        // ??
//        for(int r = 1; r < nums.length; r++){
//            if(nums[l] != nums[r]){
//                l += 1;
//                int tmp = nums[r];
//                nums[r] = nums[l];
//                nums[l] = tmp;
//            }
//        }
//
//        return l;
//    }





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
    // 11.08 - 18 am
    /**
     *
     *  ------------------
     *
     *  ex 1)
     *
     *     [1,1,1,2,2,3]
     *      l   r
     *
     *
     *     [1,1,2,2,1,3]
     *      l   l   r
     *
     *     [1,1,2,2,1,3]
     *          l        r
     *
     *
     *   ex 2)
     *
     *    [0,0,1,1,1,1,2,3,3]
     *     l   r
     *
     *
     */
    // IDEA: 2 POINTERS ???
    public int removeDuplicates(int[] nums) {
        // edge ??
        int n = nums.length;
        if(n <= 2){
            return n;
        }

        // NOTE !!!
        // L should init as 2
        //int l = 0;
        int l = 2;


        //int r = 2; // ???
        // ??? NOTE !!!
        // 1. r = [2, nums.len-1]
        // 2. r += 2
        for(int r = 2; r < n; r ++){
            // ???
            // swap ????
            // if(nums[r] != nums[l]){
            if(nums[r] != nums[l-2]){
                int tmp = nums[r];
                l += 2;
                nums[r] = nums[l];
                nums[l] = tmp;
            }
        }

        return l;
       // return l + 1; // ???
    }


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
     *
     */
    // 16.02 - 16 pm
    // IDEA 1) HASHMAP + GREEDY ?????
    public int minSwapsCouples(int[] row) {
        // edge

        // map: { val : idx }
        Map<Integer, Integer> map = new HashMap<>();
        for(int i = 0; i < row.length; i++){
            map.put(row[i], i);
        }

        System.out.println(">>> map = " + map);

        int swap = 0;

        // ??
        // ????
        for(int i = 1; i < row.length; i += 2){
            // ???
            int x = row[i];
            int y = row[i - 1];
            if(isPair2(x, y)){
                continue;
            }
            // NOTE !!! if not `couple`,
            //         -> swap
            else{

                /** NOTE !!!
                 *
                 *  If x is even → partner = x + 1
                 *  If x is odd → partner = x - 1
                 *
                 */


                 swap += 1;

                int partner = (x % 2 == 0) ? x + 1 : x - 1;

                // NOTE !!!
                int partnerIdx = map.get(partner);
                int tmp = partner;

                // NOTE !!!
                // need to BOTH update
                // arr and hashmap !!!
                int tmp2 = row[partnerIdx];
                row[partnerIdx] = row[i+1];
                row[i+1] = tmp2; // ???

                // ???
                map.put(partner, i+1);
                map.put(row[i+1], partnerIdx);


//                swap += 1;
//                // ???
//                // get the `even` one ??
//                // then swap for the `odd` one ???
//                int even = -1;
//                int odd = -1;
//                if(x % 2 == 0){
//                    x = even;
//                    y = odd;
//                }else{
//                    x = odd;
//                    y = even;
//                }
//                // update map ???
//                int tmp = map.get(odd);
//                map.put(odd, i);
//                map.put(i, tmp);
            }
        }

        return swap;
    }



    // ???
    private boolean isPair2(int x, int y){

        return Math.abs(x - y) == 1;
    }










    public int minSwapsCouples_98(int[] row) {
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
     *  -> Return the `minimum` number
     *     of `groups`
     *     you need to make.
     *
     *   s.t. NO overlap for intervals in same group
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
     *  ex 1)
     *
     *  Input: intervals = [[5,10],[6,8],[1,5],[2,3],[1,10]]
     *  Output: 3
     *
     *
     *  -> sort:  (1st element small -> big,
     *             then 2nd element ?? big -> small ???
     *             )
     *
     *   [[1,5], [1,10],[2,3],[5,10],[6,8]],  PQ = { 5 }
     *      X
     *
     *   [[1,10],[2,3],[5,10],[6,8]],  PQ = { 5, 10 }
     *      x
     *
     *  [[2,3],[5,10],[6,8]],  PQ = { 5, 10, 3 }
     *     x
     *
     *  [[5,10],[6,8]],  PQ = { 5, 10, 3, 10 }
     *     x
     *
     *
     *
     */
    // 16.23 - 33 PM
    // IDEA: SCAN LINE
    public int minGroups(int[][] intervals) {
        // edge

        // NOTE !!! NO need to sort !!! (FOR SCAN LINE APPROACH)
        // ????
        // 1. sort ?? ( start ??? small -> big ???
//        Arrays.sort(intervals, new Comparator<int[]>() {
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                int diff = o1[0] - o2[0]; // ???
//                return diff;
//            }
//        });


        /**
         *
         *      *
         *      *      * Time →
         *      *      * 1    2    3    4    5    6    7    8    9    10
         *      *      * ------------------------------------------------
         *      *      * [1,5]   █████
         *      *      * [1,10]  ███████████████
         *      *      * [2,3]        ██
         *      *      * [5,10]            █████████
         *      *      * [6,8]                 ███
         *      *      *
         *
         *
         */

        // ????
        // { status : idx ???? }
        TreeMap<Integer, Integer> map = new TreeMap<>();

        // list : [ [idx, status ] ]
        // start: 1, end: -1
        //List<Integer[]> list = new ArrayList<>();
        // ???
        for(int[] x: intervals){
            int start = x[0];
            int end = x[1];
            // ???
//            list.add(new Integer[]{x[0], 1});
//            list.add(new Integer[]{x[1], -1});
            // NOTE !!!
//            map.put(1, start);
//            map.put(-1, end + 1);
            map.put(start, map.getOrDefault(start, 0) + 1);
            map.put(end + 1, map.getOrDefault(end + 1, 0) - 1);

        }

        // ???
        int maxCnt = 0;
        int cur = 0;

        for(int x: map.values()){
            cur += x;
            maxCnt = Math.max(cur, maxCnt);
        }

        // ???
       // int groupCnt = 0;

        return maxCnt;
    }







    // 15.47 - 57 pm
    //  IDEA 1) SORT + big PQ ???
    // -> PQ:  { end_idx } // ???
    public int minGroups_96(int[][] intervals) {
        // edge

        // 1. sort ?? ( start ??? small -> big ???
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[0]; // ???
                return diff;
            }
        });

        // ??? big PQ ??
        // save `end` idx ?????
        /** NOTE !!!
         *
         *  -> should be SMALL PQ,
         *  sort on `end` idx.
         *  e.g. end (small -> big)
         *
         */
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        for(int i = 0; i < intervals.length; i++){
            int start = intervals[i][0];
            int end = intervals[i][1];

            // ??? while or if ???
            //while ()
            // reuse `same group`
            /**
             *   if NO overlapped, reuse same group
             *
             *  |-----|  old
             *           |-------| new
             *
             */
            if(!pq.isEmpty() && pq.peek() < start){
                pq.poll(); // ???
            }
            pq.add(end);

        }


        return pq.size();
    }









    public int minGroups_97(int[][] intervals) {
        // edge

        // note !!
        // PQ: { end_time_1, end_time_2, .. }
        // PQ to check `min end time` ?????
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });

        // ??? sort
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[0];
                return diff;
            }
        });

        //int overlap = 0;


        // ??
        for(int[] x: intervals){
//            if(pq.isEmpty() || pq.peek() < x[0]){
//                //pq.add(x);
//            }else{
//                overlap += 1; // ??
//            }
            // ???
            if(!pq.isEmpty() && pq.peek() < x[0]){
                pq.poll();
            }

            pq.offer(x[1]);
        }

        return pq.size();
    }








    // IDEA 1) PQ / SORTING ????
    public int minGroups_98(int[][] intervals) {


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


    // LC 2345
    // 14.22 - 32 pm
    /**
     *  -> Return the `number` of `visible` mountains.
     *
     *  -> A mountain is considered visible
     *     if its peak does not lie within
     *     another mountain
     *     (including the border of other mountains).
     *
     *
     *  ---------------
     *
     *
     *   IDEA 1) MATH ???
     *
     *     0. sort ?????
     *     1. find `peaks`
     *     2. check if peaks in `line`
     *        - 2 lines:
     *           - peak + left boundary
     *           - peak + right ..
     *
     *           NOTE: it's a `isosceles` triangle
     *              -> so slope MUST be -1, 1
     *
     *   IDEA 1) SORT ??
     *
     *     0. sort ?????
     *     1. find `peaks` ???
     *     2. check if there is a overlap within prev and last`boundary`
     *
     *  ---------------
     *
     */
    public int visibleMountains(int[][] peaks) {
        // edge
        if(peaks == null){
            return 0;
        }
        if(peaks.length == 1){
            return 1;
        }

        // get `2 edges`
        // list: [ [ left_x_1, right_x_1, mountain_id ], .. }
        List<Integer[]> list = new ArrayList<>();

        // ??? sort ??? ( x: small -> big)
        Arrays.sort(peaks, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[0];
                return diff;
            }
        });

        int idx = 0;
        for(int[] p: peaks){
            int val1 = getBoundaries(p)[0];
            int val2 = getBoundaries(p)[1];
            list.add(new Integer[]{val1, val2, idx});
            idx += 1;
        }

        List<Integer[]> cache = new ArrayList<>();

        // ????
        int[] overlap = new int[peaks.length]; // ????

        /** NOTE !!!
         *
         *   since x boundary already sorted (small -> big),
         *   below are the NON overlap cases:
         *
         *    |-----|              old
         *            |------|     new
         *
         */
        for(Integer[] x: list){
            if(cache.isEmpty() || cache.get(cache.size() - 1)[1] < x[0]){
                cache.add(x);
            }else{
               overlap[x[2]] = 1;
            }
        }


        // ???
        int cnt = 0;
        for(int x: overlap){
            if(x == 1){
                cnt += 1;
            }
        }
        return cnt;
    }

    private Integer[] getBoundaries(int[] peak){
        int x = peak[0];
        int y = peak[1];
        //int height =
        return new Integer[]{x - y, x + y};
    }





    private boolean isOverlap(){
        return false;
    }


    // LC 85
    // 7.46 - 56 am
    /**
     *  ->  find the largest rectangle containing
     *      only 1's
     *     -> and return its area.
     *
     *
     *   ----------------
     *
     *   IDEA 1) DFS or BFS ??
     *
     *   IDEA 2) MATH + BRUTE FORCE ???
     *
     *   IDEA 3) DP ????
     *
     *
     *   ----------------
     *
     *
     *
     */
    //  IDEA 3) DP ????
    public int maximalRectangle(char[][] matrix) {
        // edge

        int n = matrix.length;

        /**  DP def:
         *
         *  max `rectangle` area at (i, j) (idx)
         */
        int[][] dp = new int[n + 1][n + 1];

        // init ??
        dp[0][0] = matrix[0][0] == 1 ? 1: 0;

        for(int y = 1; y < n; y++){
            for(int x = 1; x < n; x++){
                // ???
                if(matrix[y-1][x] == 1
                        && matrix[y][x-1] == 1
                        && dp[y-1][x-1] == 1){
                    dp[y][x] = dp[y-1][x-1] + 2; // ???
                }
            }
        }

        return dp[n-1][n-1];
    }


    // LC 84
    // 8.25 - 35 am
    /**
     *
     *   -> return the area of the
     *     `largest rectangle` in the histogram.
     *
     *
     *   ---------
     *
     *   IDEA 1) STACK ??????  (FILO)
     *
     */
    // 16.44 - 17.21 pm
    // IDEA 1) STACK ??????  (FILO)
    public int largestRectangleArea(int[] heights) {
        // edge

        int maxArea = 0;


        // ???
        // NOTE !!!!
        // we SHOULD track `idx` instead !!!
        // e.g. { idx_1, idx_2 ,.. .}
        //Stack<Integer> st = new Stack<>();
        // NOTE !!! use `dequeue`
        Deque<Integer> st = new ArrayDeque<>(); // store indices

        // ???
        int minHeight = 10000 + 1;// ???

        int n = heights.length;
        // NOTE !!! <=
        for(int i = 0; i <= n; i++){

            // NOTE !!!
            int cur = (i == n) ? 0 : heights[i]; // sentinel 0 to flush stack

            //int cur = heights[i];
            // ???
            // NOTE !!! while loop, not if
            //  if(!st.isEmpty() && heights[st.peek()] <= cur){
            while (!st.isEmpty() && heights[st.peek()] > cur){

                minHeight = Math.min(cur, minHeight);

                // ????

                int prevIdx = st.pop(); // ???

                maxArea = Math.max(maxArea, Math.max(
                        minHeight * i,
                        (i - prevIdx) * heights[prevIdx] // ????
                )); // ???
            }


            // ???
            st.add(i);
        }

        return maxArea;
    }










    // LC 1438
    // 9.43 - 53 am
    /**
     *
     *
     *   IDEA 1) SLIDE WINDOW ????
     *
     *
     */
    public int longestSubarray(int[] nums, int limit) {

        return 0;
    }



    // LC 739
    // 15.26 - 36 pm
    /**
     *
     *  -> return an array answer such that answer[i] is the number of days
     *    you have to wait after the
     *    ith day to get a warmer temperature.
     *
     *  IDEA:  MONO STACK
     */
    // IDEA:  MONO STACK (big stack ????
    public int[] dailyTemperatures(int[] temperatures) {
        // edge
        if(temperatures == null || temperatures.length == 0){
            return null;
        }
        if(temperatures.length == 1){
            return new int[]{0};
        }



        int n = temperatures.length;
        int res[] = new int[n];
        // ???
        Arrays.fill(res, 0);

        // NOTE !! small ???? PQ
        // small PQ: { idx_1, idx_2, ... }
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                //int diff = o2 - o1;
                int diff = o1 - o2; // ???
                return diff;
            }
        });


        for(int i = 0; i < n; i++){
            int cur = temperatures[i];
            while (!pq.isEmpty() && temperatures[pq.peek()] < cur){
                int preIdx = pq.poll();
                res[preIdx] = i - preIdx; // ???
            }
            pq.add(i);
        }

        return res;
    }



    // LC 2289
    // 7.41 - 51 am
    /**
     *  ->
     *   Return the number of steps performed
     *   until nums becomes a non-decreasing array.
     *
     *
     *   0 idx arr: nums
     *
     *   -> remove all elements nums[i]
     *      where
     *         nums[i-1] > nums[i]
     *         for idx within arr
     *
     *
     *   ---------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) STACK ???
     *
     *
     *   ---------------
     *
     *        *
     *      * ### 🎬 The Setup
     *      * * **`dp[i]`**: Steps required to remove `nums[i]`.
     *      * * **`stack`**: Stores indices of elements
     *      that haven't been "eaten" yet (Monotonic Decreasing).
     *      * * **`maxSteps`**: Our global answer.
     *
     *
     */
    // IDEA: MONO STACK + DP ?????
    // 10.47 - 10.57 ???
    // IDEA: MONO STACK + DP (gemini)
    public int totalSteps(int[] nums) {
        // edge

        int n = nums.length;
        // dp /??
        // `dp[i]`**: Steps required to remove `nums[i]`.
        // for element at idx = i
        int[] dp = new int[n];

        // ???
        // Stores indices of elements
        // that haven't been "eaten"
        // yet (Monotonic Decreasing).
        Stack<Integer> st = new Stack<>();

        int maxSteps = 0; // ??

        // ???
        for(int i = 0; i < nums.length; i++){
            int cur = nums[i];

        }




        return maxSteps;
    }





    //  IDEA 2) STACK ???
    public int totalSteps_99(int[] nums) {
        // edge
//        if(isNonDecrease(nums)){
//            return 0;
//        }

        //Stack<Integer> st = new Stack<>(); // ??
        ///  ???
        int prev = -1;
        //for(int )
        // /?? reverse loop ???
        List<Integer> cache = new ArrayList<>();
        for(int x: nums){
            cache.add(x);
        }

        while (cache != null && isNonDecrease2(cache)){
            //List<Integer> cache2 = new ArrayList<>(); // ????
            Stack<Integer> st = new Stack<>(); // ??

            for(int i = nums.length - 1; i > 0; i--){
                if(nums[i-1] > nums[i]){
                    continue;
                }
                st.add(nums[i]); // ???
            }

            cache = new ArrayList<>();

            // add back to cache ???
            for(int x: st){
                cache.add(x);
            }
        }

        // ???
        return cache.size();
    }

    private boolean isNonDecrease2(List<Integer> list){
        // ??
        if(list == null || list.size() == 0){
            return true;
        }
        for(int i = 0; i < list.size() - 1; i++){
            if(list.get(i) > list.get(i+1)){
                return false;
            }
        }

        return true;
    }



    private boolean isNonDecrease(int[] nums){
        // ??
        if(nums == null || nums.length == 0){
            return true;
        }
        for(int i = 0; i < nums.length - 1; i++){
            if(nums[i] > nums[i+1]){
                return false;
            }
        }

        return true;
    }


    // LC 853
    // 8.48 - 58 am
    /**
     *  -> Return the number of car
     *  fleets that will arrive at the destination.
     *
     *
     *  ----------------
     *
     *   IDEA 1) STACK ???
     *     ( FILO )
     *
     *
     *  ----------------
     *
     *
     *
     */
    // 10.14 - 24 am
    // IDEA SORT ON PAIR, stack + `travel time`
    public int carFleet(int target, int[] position, int[] speed) {
        // edge

        List<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < position.length; i++){
            list.add(new Integer[]{position[i], speed[i]});
        }

        // ??? sort on position ???
        // big -> small
        Collections.sort(list, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o2[0] - o1[0];
                return diff;
            }
        });

        // travel time ?????
        //
        Stack<Double> st = new Stack<>(); // ???
        for(Integer[] x: list){

            // ?????
            double time = (double) (target - x[0]) / x[1];

//            while (!st.isEmpty() && st.peek() < time){
//                st.add(time);
//            }
            //st.add(time);

            if(st.peek() < time){
                st.add(time);
            }
        }


        return st.size(); // ???
    }








    // IDEA: PAIR SORT ???
    public int carFleet_999(int target, int[] position, int[] speed) {
        // edge

        List<Integer[]> list = new ArrayList<>();
        for(int i = 0; i < position.length; i++){
            list.add(new Integer[]{position[i], speed[i]});
        }

        // NOTE !!!
        //  // sort by position descending (closest to target first)

        // ??
        // sort on position ??? (small -> big
//        Collections.sort(list, new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                int diff = o1[0] - o2[0];
//                return diff;
//            }
//        });
        Collections.sort(list, (a, b) -> b[0] - a[0]);

        //int fleet = 0;
        // ???
        //int prevTime = -1;
        //float prevTime = ( (float) (target - list.get(0)[0]) / list.get(i)[1] );

        // NOTE !!!

        int fleet = 0;
        double prevTime = -1;


        // ???
        // NOTE !!! start from i = 0
        for(int i = 0; i < list.size(); i++){
            // ???
           // float prevTime = getTime(target, list.get(i-1));
            float time = getTime(target, list.get(i));
            // ???
            if(time > prevTime){
                fleet += 1; /// ???
                prevTime = time;
            }
        }

        return fleet;
    }

    private float getTime(int target, Integer[] input){
        return ( (float) (target - input[0]) / input[1] );
    }





    public int carFleet_99(int target, int[] position, int[] speed) {
        // ???
        // edge

        int fleet = 0;

        int n = position.length;

        // PQ: {  time_1, time_2, .. }
        // small PQ (small -> big) // ????
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        // ???


        // ???
        for(int i = 0; i < n; i++){

            // ??
            int time = (target - position[i]) / speed[i];
            // ???
            while (!pq.isEmpty() && pq.peek() >= time){
               pq.poll(); // ???
            }
            pq.add(time);
        }


        // ???
        return pq.size();
    }


    // LC 402
    // 9.37 - 47 AM
    /**
     *  -> return the smallest possible integer
     *  after removing k digits from num.
     *
     *
     *  --------------
     *
     *  IDEA 1) STACK ???? + `ZERO PREFIX` HANDLING
     *
     *
     *  --------------
     *
     *
     */
    // IDEA 1) STACK ???? + `ZERO PREFIX` HANDLING
    public String removeKdigits(String num, int k) {
        // edge
        if(num.length() <= k){
            return "0";
        }

        Stack<Character> st = new Stack<>();

        // ??
        for(char ch: num.toCharArray()){
            while (!st.isEmpty() && st.peek() - ch > 0 && k > 0){
                st.pop();
                k -= 1;
            }
            //st.add(ch);
            st.push(ch);
        }

        // NOTE !!! if k still > 0
        // stack: FOLO
        // if k still remains, remove from end
        while (k > 0 && !st.isEmpty()){
            st.pop();
            k -= 1;
        }

        if(st.isEmpty()){
            return "0";
        }

        int firstNonZeroIdx = 0;
        int idx = 0;

        StringBuilder sb = new StringBuilder();

        // /? find 1st non zero idx
        for(int x: st){
            if(x != '0'){
                firstNonZeroIdx = idx;
                //break;
            }
            sb.append(x);
            idx += 1;
        }

        // ???
        return sb.substring(firstNonZeroIdx);
    }









    //  IDEA 1) STACK ???? + `ZERO PREFIX` HANDLING
    public String removeKdigits_9999(String num, int k) {
        // edge
        if(num == null){
            return num;
        }
        if(k <= 0){
            return num;
        }
        if(k == num.length()){
            return "0";
        }

        Stack<Character> st = new Stack<>();
        for(char ch: num.toCharArray()){
            if(st.isEmpty()){
                st.add(ch);
                continue;
            }
            // ???? if or while ????
            if(k > 0 && st.peek() - ch > 0){
                st.pop(); // ??
                k -= 1;
                // /??
                if(k == 0){
                    break;
                }
            }
            // ???
            st.add(ch);
        }

        StringBuilder sb = new StringBuilder();
        // ???
        // find `first NON zero` idx
        int firstNonZeroIdx = 0;
        int idx = 0;
        for(int ch: st){
            if(ch != '0'){
                firstNonZeroIdx = idx;
                break;
            }
            idx += 1;
        }

        int idx2 = 0;
        for(int ch: st){
            if(idx2 >= firstNonZeroIdx){
                sb.append(ch);
            }
            idx2 += 1;
        }

        return sb.toString();
    }




    // LC 224
    /**
     *
     *  ------------------
     *
     *
     *  IDEA: STACK
     *
     *
     *  ------------------
     *
     */
    public int calculate(String s) {

        return 0;
    }





    // LC 1081
    public String smallestSubsequence(String s) {

        return null;
    }


    // LC 316
    // 7.46 - 56 am
    /**
     * ->  remove duplicate letters
     * so that every letter
     * appears `once and only once`
     *
     *
     * -----------------------
     *
     *  IDEA 1) STACK
     *
     *   1. map ? record the `last idx` of every alphabet
     *   2.
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     *  -----------------------
     *
     *
     */
    // 7.51 - 8.01 am
    /**
     *
     *  -> remove duplicate letters so that
     *  every letter appears once and only once.
     *
     *
     *  NOTE:
     *   -  result is the smallest in
     *      lexicographical order among
     *      all possible results.
     *
     *
     *  -------------
     *
     *   IDEA 1) STACK ??? + char arr
     *
     *    -> map : { string, last_occur_idx }
     *
     *
     *    and stack, check if `last < cur` cur,
     *    via char_a - char_b < 0 ?
     *
     *
     *
     *
     */
    // IDEA 1) STACK ??? + char arr
    public String removeDuplicateLetters(String s) {
        // edge
//        if (s == null || s.length() == 0){
//            return "";
//        }
        if(s.isEmpty()){
            return null;
        }
        if(s.length() == 1){
            return s; // ??
        }

        Map<Character, Integer> map = new HashMap<>();
        for(int i = 0; i < s.length(); i++){
            map.put(s.charAt(i), i); // ??
        }

        // ???
        // use below, to avoid `DUPLICATED adding to stack` ???
        boolean[] added = new boolean[26];

        System.out.println(">>> map = " + map);

        Stack<Character> st = new Stack<>();
        for(int i = 0; i < s.length(); i++){
            char cur = s.charAt(i);
            // ??
            while (!st.isEmpty()
                    && st.peek() - cur > 0
                    && map.get(st.peek()) > i
                    && !added[cur - 'a']){

                char tmp = st.pop();
                added[tmp - 'a'] = false;
            }
            if(!added[cur - 'a']){
                st.add(cur);
                added[cur - 'a'] = true;
            }
        }

        StringBuilder sb = new StringBuilder();
        for(char ch: st){
            sb.append(ch); // ????
        }


        return sb.toString();
    }











    //  IDEA 1) STACK
    public String removeDuplicateLetters_94(String s) {
        // edge

        int[] index = new int[26]; // ???
        int i = 0;
        for(char ch: s.toCharArray()){
            index[ch - 'a'] = i; // ????
            i += 1;
        }

        Stack<Character> st = new Stack<>();
        StringBuilder sb = new StringBuilder();

        // ???
        boolean[] used = new boolean[26];
        HashSet<Character> visited = new HashSet<>();


        int j = 0;
        for(char ch: s.toCharArray()){
            // ??
            // ???
            // avoid add `duplicated alpha`
            if(visited.contains(ch)){
                continue;
            }
            if(st.isEmpty()){
                st.add(ch);
                // ???
                visited.add(ch);
            }else{
                while (!st.isEmpty() &&
                        st.peek() - ch > 0 &&
                        index[st.peek() - 'a'] > j
                        ){

                    visited.add(st.pop());

                }

                st.add(ch);
                visited.add(ch);
            }

            j += 1;
        }

        // ??
        for(char ch: st){
            sb.append(ch); // ????
        }

        return sb.toString();
    }


    // LC 1099
    // 9.32 - 43 am
    // IDEA 1: sort + 2 pointers ???
    public int twoSumLessThanK(int[] nums, int k) {
        // edge

        // default: small -> big ???
        Arrays.sort(nums);

        int l = 0;
        int r = nums.length - 1;

        int maxVal = -1;

        while (r > l){
            int cur = nums[l] + nums[r];
            if(cur < k){
                maxVal = Math.max(maxVal, cur);
               // r -= 1;
                l += 1;
            }else{
                //l += 1;.
                r -= 1;
            }
        }

        return maxVal < 0 ? -1 : maxVal;
    }


    // LC 1283
    // 7.32 - 43 am
    /**
     *
     *  ->
     *
     *   Find the `smallest` divisor such that the result
     *   mentioned above is less than or equal to threshold.
     *
     *
     *   --------------
     *
     *
     *   IDEA 1) MATH + BRUTE FORCE ??
     *
     *   IDEA 2) BINARY SEARCH ?>>>
     *
     *
     *   --------------
     *
     *
     */
    // IDEA 2) BINARY SEARCH ?>>>
    public int smallestDivisor(int[] nums, int threshold) {
        // edge
//        if(threshold == 0){
//            int res = nums[0];
//            for(int x: nums){
//                res = Math.min(res, x);
//            }
//            return res;
//        }
        // ??
        if(nums == null || nums.length == 0){
            return 0; // ????
        }

        //
        // int l = nums[0]; // ??
        int l = 1; // ???? nums[0]; // ??
        int r = 0;
        // ??
        for(int x: nums){
           // l = Math.min(x, l);
           // r += x; // ??
            // ???
            r = Math.max(x, r);
        }

        //???
        int minDivisor = r;


        while (r >= l){

            int mid = l + (r - l) / 2;
            // NOTE !!!
            //int midVal = mid; //nums[mid];
            if(canDivide(nums, threshold, mid)){
                minDivisor = Math.min(minDivisor, mid);
                // NOTE !!! try smaller candidates
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        return minDivisor;
    }



    // ???
    private boolean canDivide(int[] nums, int threshold, int x){
        int tmp = 0;
        for(int val: nums){
//            // ???
//           // tmp += (int) Math.floor( ( (double) val / x) ); // ????
//            // ????
//            tmp += (int) Math.ceil( ( (double) val / x) ); // ????


            // ceil(val / d) without using double
            tmp += (val + x - 1) / x;

            if(tmp > threshold){
                return false;
            }

        }

        return tmp <= threshold;
    }










    // LC 34
    // 13.30 - 40 pm
    /**
     *   -> find the `starting and ending `
     *      position of a given target value.
     *
     *       - sorted in non-decreasing ( increasing)
     *       - If target is not found in the array, return [-1, -1].
     *
     *  ----------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) BINARY SEARCH
     *
     *
     *
     *  ----------------
     *
     *
     */
    // IDEA 2) BINARY SEARCH
    public int[] searchRange(int[] nums, int target) {
        int[] res = new int[]{-1, -1};

        int left = getLeftBoundary(nums, target, 0, nums.length - 1);
        int right = getRightBoundary(nums, target, 0, nums.length - 1);

        if(left == right && left != -1){
            return new int[] {left, left};
        }

        if(left != -1 && right != -1){
            return new int[] {left, right};
        }

        return res; // ???
    }

    // /??
    private int getLeftBoundary(int[] nums, int target, int l, int r){
        int res = -1;

        // ???
        while (r >= l){
          int mid = l + (r - l) / 2;
          int midVal = nums[mid];
          if(midVal == target){
              res = mid; /// ???
              r = mid - 1;
          }else if(midVal > target){
              r = mid  - 1;
          }else{
              l = mid + 1;
          }
        }

        return res; // ???
    }

    private int getRightBoundary(int[] nums, int target, int l, int r){
        int res = -1;

        // ???
        while (r >= l){
            int mid = l + (r - l) / 2;
            int midVal = nums[mid];
            if(midVal == target){
                res = mid; /// ???
                //r = mid - 1;
                l = mid + 1;
            }else if(midVal > target){
                r = mid  - 1;
            }else{
                l = mid + 1;
            }
        }

        return res; // ???
    }





    // IDEA 1) BRUTE FORCE
    public int[] searchRange_99(int[] nums, int target) {
        int[] res = new int[]{-1, -1};

        for(int i = 0; i < nums.length; i++){
            int val = nums[i];
            if(val == target){
                // ???
                if(res[0] == -1){
                    res[0] = i;
                }else{
                    res[0] = Math.min(i, res[0]);
                }
                res[1] = Math.max(i, res[1]);
            }
        }

        return res;
    }



    // LC 162
    // 13.51 - 14.01 pm
    /**
     *
     *  -> find a peak element, and return its index.
     *     If the array contains multiple peaks,
     *     return the index to any of the peaks.
     *
     *
     *  -------------
     *
     *
     *  IDEA 1) BINARY SEARCH ???
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     *  -------------
     *
     *
     */
    // IDEA 1) BINARY SEARCH ???
    public int findPeakElement(int[] nums) {

        // ???
        return findPeakHelper(nums, 0, nums.length - 1);
    }

    // /??
    private int findPeakHelper(int[] nums, int l, int r){
        // edge /????
        if(l > r){
            return -1;
        }

        while (r >= l){
            int mid = l + (r -l) / 2;
            int midVal = nums[mid];

            // ???
            if(mid - 1 >= 0 && mid + 1 < r){
                if(nums[mid] > nums[mid-1] && nums[mid] > nums[mid + 1]){
                    return mid;
                }
            }

            // ??
            int leftRes = findPeakHelper(nums, l, mid - 1);
            int rightRes = findPeakHelper(nums, mid + 1, r);

            if(leftRes != -1){
                return leftRes;
            }
            if(rightRes != -1){
                return rightRes;
            }

        }

        return -1;
    }



    // LC 410
    // 14.33 - 43 pm
    /**
     *  -> Return the minimized largest sum of the split.
     *
     *
     *   - split nums into `k non-empty subarrays `
     *      such that the `largest sum `
     *      of any subarray is `minimized.`
     *
     *    NOTE:
     *      - A subarray is a `contiguous`
     *        part of the array.
     *
     *   nums: int arr
     *   k: int
     *
     *
     *
     *
     *
     *  -----------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) BINARY SEARCH ????
     *
     *   IDEA 3) GREEDY ??? + MATH ???
     *
     *
     *  -----------
     *
     */

    // 10.48 - 10.58
    // IDEA: BINARY SEARCH ????
    public int splitArray(int[] nums, int k) {
        // edge

        // ??
        int l = nums[0];
        int r = 0;
        // ??
        for(int x: nums){
            r += x;

            // NOTE !!!!
            l = Math.max(l, x); // ???????
        }

        int ans = r; // /??

        while (r >= l){
            int mid = l + (r - l) / 2;
            // ??
            if(canSplit(nums, k, mid)){
                ans = Math.min(ans, mid);
                // NOTE !!! keep finding the smaller candiates
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }


        return ans;
    }


    // threshold
    private boolean canSplit(int[] nums, int k, int threshold){
        // edge
        if(k <= 1){
            return true; // ?
        }


        //  int groups = 1;   // start with one subarray
        ///  ???? start from 1 but not 0 ??? why ????
        int curGroup = 1; // ????
        int curSum = 0;

        for(int x: nums){

            // ?????????
            // early quit
//            if(curGroup >= k){
//                return false;
//            }
            // NOTE !!!
            if (x > threshold){
                return false;
            }

            // NOTE !!!!
            if(curSum > threshold){
                curSum = x;
                curGroup += 1;
            }else{
                curSum += x;
            }
        }


        // NOTE !!! should be `<=` k
        //return curGroup == k;
        return curGroup <= k;
    }










    // IDEA 3) GREEDY ??? + MATH ???
    public int splitArray_999(int[] nums, int k) {
        // edge
        if(k == 0){
            int res = 0;
            for(int x: nums){
                //sum += x;r
                res += x;
            }
            return res;
        }
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // ???
        int sum = 0;
        for(int x: nums){
            sum += x;
        }

        int n = nums.length;

        // ??
        int avg = sum / n; // ???

        // ??
        int cur = 0;
        int res = 0;

        for(int i = 0; i < n; i++){
            int val = nums[i];
            // ???
            if(cur + val >= avg){
                // ??? reset cur
                cur = val;
                res = Math.max(res, cur);
            }else{
                cur += val;
                res = Math.max(res, cur);
            }

        }

        return res;
    }


    // LC 1552
    // 8.14 -24 am
    /**
     *
     *  -> Given the integer array position and the integer m.
     *  Return the required force.
     *
     *
     *   - such that the `minimum` magnetic force
     *      between any two balls is `maximum.`
     *
     *  -------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) BINARY SEARCH ??
     *
     *
     *  -------------
     *
     *
     */
    // IDEA 2) BINARY SEARCH ??
    public int maxDistance(int[] position, int m) {
        // edge

        int l = 1; // ??
        int sumDist = 0;
        // ?
        for(int x: position){
            sumDist += x;
        }
        // ???
        int r = sumDist / m;

        int ans = r; // ???

        // ??
        while (r >= l){
            int mid = l + (r - l) / 2;
            // ??
            if(canDistribute(position, m, mid)){
                ans = Math.min(ans, mid);
                r = mid - 1;
            }else{
                l = mid + 1;
            }
        }

        return ans;
    }


    // ???
    private boolean canDistribute(int[] position, int m, int dist){

        return false;
    }



    // LC 1891
    // 7.54 - 8.04 am
    /**
     *
     *  -> Return the `maximum possible `positive integer`
     *     length that you can obtain` k ribbons` of,
     *        or `0` if you cannot obtain k ribbons of the same length.
     *
     *
     *     NOTE:
     *
     *      Your goal is to obtain` k ribbons` of all the
     *          ` same positive integer length`.
     *         You are allowed to throw away any
     *         excess ribbon as a result of cutting.
     *
     *
     *   --------------
     *
     *   IDEA 1) BRUTE FORCE ??
     *
     *   IDEA 2) BINARY SEARCH
     *
     *
     */
    // IDEA 2) BINARY SEARCH
    public int maxLength(int[] ribbons, int k) {
        // edge
        if(ribbons == null || ribbons.length == 0){
            return 0;
        }

        int l = 1; // /?
        int r = 0;
        for(int x: ribbons){


            // ?????
            r = Math.max(r, x);
           // r = Math.min(r, x);
        }

        //int ans = r;
        int ans = 0;



        boolean foundSolution = false;

        // ??
        while (r >= l){

            int mid = l + (r - l) / 2;

            if(canSplit2(ribbons, k, mid)){
                foundSolution = true;
                //ans = Math.min(ans, mid);
                ans = mid;
                // NOTE !! find bigger candiates
                l = mid + 1;
            }else{
                r = mid - 1;
            }
        }


       // return !foundSolution ?  0 : ans; // ????
        return ans;
    }


    private boolean canSplit2(int[] ribbons, int k, int len){
        // ???
        int curCnt = 0;

        for(int x: ribbons){
//            if(x < len){
//                return false;
//            }

           // curCnt += 2;
            curCnt += (x / len); // ????
        }


        // ????
        return curCnt >= k;
    }



    // LC 1761
    // 11.20 - 45 am
    /**
     *  -> Return the minimum degree of a connected trio in the graph,
     *
     *  or -1 if the graph has no connected trios.
     *
     *
     *  ------------------
     *
     *  IDEA 1) DFS ??
     *
     *     -> map:  { node :  [neigh_1, neigh_2, .. ] }
     *     -> find if there is `cycle` ??
     *         -> e.g.  move from node_1, ...
     *                  finally back to node_1
     *                  -> and ALL of node visited exactly once
     *
     *                  -> trio is found
     *
     *
     *
     *  IDEA 2) GRAPH ??
     *
     *  IDEA 3) UNION FIND ???
     *
     *
     *   ------------------
     *
     *
     *
     */
    // IDEA 1) BRUTE FORCE ????
    // 8.34 - 44 am ???
    public int minTrioDegree(int n, int[][] edges) {

        return 0;
    }


    // IDEA 1) DFS ??
    int minDegree = 0; // ???? edges.length * edges.length; // ???
    public int minTrioDegree_99(int n, int[][] edges) {

        //int minDegree = 0; // ???? edges.length * edges.length; // ???
        // -> map:  { node :  [neigh_1, neigh_2, .. ] } ???
        Map<Integer, HashSet<Integer>> map = new HashMap<>();
        // /??
        Set<Integer> nodeSet = new HashSet<>();

        for(int[] x: edges){
            int a = x[0];
            int b = x[1];

            nodeSet.add(a);
            nodeSet.add(b);

            // ???
            if(!map.containsKey(a)){
                map.put(a, new HashSet<>());
            }
            if(!map.containsKey(b)){
                map.put(b, new HashSet<>());
            }

            HashSet<Integer> set = map.get(a);
            set.add(b);
            map.put(a, set);

            set = map.get(b);
            set.add(a);
            map.put(b, set);
        }

        // /??
        for(Integer node: nodeSet){
            // ???
            dfsTrioHelper(n, edges, node, map, new HashSet<>(), 0);
        }


        return minDegree;
    }

    private void dfsTrioHelper(int n, int[][] edges, int node, Map<Integer, HashSet<Integer>> map, HashSet<Integer> visited, int move){
        // ??
        if(visited.contains(node)){
            if(move > 0){
                if(minDegree == 0){
                    minDegree = move;
                }else{
                    minDegree = Math.min(move, minDegree);
                }
            }
            return;
        }

        // ???
        for(int next: map.get(node)){
            if(!visited.contains(next)){
                visited.add(next);
                dfsTrioHelper(n, edges, next, map, visited, move + 1);
            }
        }


    }












    // LC 154
    // 8.51 - 10.25 am
    /**
     *  ->  return the minimum element of this array.
     *
     *
     *  -------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) BINARY SEARCH ???
     *
     * -------
     *
     *
     *  ex 1)
     *
     *    CORE: compare with `nums[r]`
     *
     *    [2,2,2,0,1]
     *         mid
     *
     *
     *   [0,1,2,2,2]
     *        mid
     *
     *
     */
    // IDEA 2) BINARY SEARCH ???
    public int findMin(int[] nums) {
        // edge
        int l = 0;
        int r = nums.length - 1;

        int ans = nums[0];

        while (r >= l){

            int mid = l + (r - l) / 2;
            int midVal = nums[mid];
            ans = Math.min(ans, midVal);

            // edge: if already sorted (small -> big)
            // /??
            if(nums[r] > nums[l]){
                return nums[l];
            }
            // ???
            if(midVal > nums[r]){
                l = mid + 1;
               // r = mid - 1; // ???
                ans = Math.min(ans, nums[l]);
            }else if (midVal < nums[r]){
                //l = mid + 1;
                r = mid - 1; // ???
                ans = Math.min(ans, nums[r]);
            }
            // ??? NOTE !!!
            else{
                r -= 1;
            }

        }


        return ans;
    }









    // IDEA 2) BINARY SEARCH ???
    public int findMin_999(int[] nums) {
        // edge
        if(nums.length == 1){
            return nums[0];
        }
        // already sorted (small -> big)
        if(nums[nums.length - 1] > nums[0]){
            return nums[0];
        }

        // ???
        int ans = nums[0];
        int l = 0;
        int r = nums.length - 1;
        // ??
        while (r >= l){
            int mid = l + (r - l) / 2;
            int midVal = nums[mid];

            // ?? already sorted
            if(nums[r] > nums[l]){
                ans = Math.min(ans, nums[l]); // ?????
            }

            /** NOTE !!!
             *
             *  ONLY 2 cases:
             *
             *   1. nums[mid] >= nums[l]
             *   2. nums[mid] < nums[r]
             *
             *
             *  ---------------
             *
             *   DON'T just memorize, get understand via below practical example:
             *
             *             // [1,2,3,4,5]     // already in acending order
             *             // [5,1,2,3,4]     // mid < r, right part is sorted
             *             // [4,5,1,2,3]     // mid < r, right part is sorted
             *             // [3,4,5,1,2]     // mid >= l, left part is sorted
             *             // [2,3,4,5,1]     / mid >= l, left part is sorted
             *
             *             // cycle
             *             // [1,2,3,4,5]
             *
             *
             *
             *     -> so via above, we are VERY CLEAR on
             *        why we need to check `nums[mid] >= nums[l]` and else
             */

            ans = Math.min(ans, nums[mid]); // ?????

            // ???
            // case 1) mid is left part
            // ???
            // if(midVal >= nums[0]){
            if(midVal >= nums[l]){
//                // ???
//                if(nums[0] < nums[mid+1]){
//                    r = mid - 1; // ???
//                }else{
//                    l = mid + 1;
//                }
                l = mid + 1;

            }
            // case 2) mid is right part
            else{
//                if(nums[0] < nums[mid+1]){
//                    r = mid - 1; // ???
//                }else{
//                    l = mid + 1;
//                }
                r = mid - 1; // ???
            }
        }

        return ans;
    }









    // IDEA 1) BRUTE FORCE
    public int findMin_99(int[] nums) {
        // edge

        int ans = 5001;
        for(int x: nums){
            ans = Math.min(x, ans);
        }

        return ans;
    }


    // LC 1760
    // 2.27 - 37 pm
    /**
     *  -> Return the minimum possible
     *     penalty after performing the operations.
     *
     *
     *    - nums[i]: balls at i th bag
     *    - maxOperations: int
     *
     *    - can do below op
     *      <= `most maxOperations` times
     *
     *      - Take any bag of balls and divide
     *        it into two new bags
     *        with a `positive` number
     *
     *
     *  --------------
     *
     *   IDEA 1) BINARY SEARCH ???
     *
     *   IDEA 2) BRUTE FORCE ???
     *
     *   IDEA 3) greedy ???
     *
     *
     *
     *  --------------
     *
     *
     */
    // IDEA 1) BINARY SEARCH ???
    public int minimumSize(int[] nums, int maxOperations) {
        // edge

        int l = 1; // ???
        int r = nums[0];

        // /?
        for(int x: nums){
            // NOTE !! r should be the max value in nums, not min
            //r = Math.min(x, r);
            r = Math.max(x, r);
        }
        // ???
        int ans = r; // ??

        while (r >= l){
            // mid: possible `ball cnt` in bags ??????
            int mid = l + (r - l) / 2;
            // ???
            if(canSplit5(nums, maxOperations, mid)){
                ans = mid;
                // Return the minimum possible penalty after performing the operations.

                // ???
                // NOTE !!! find the `bigger` candidate
                //l = mid + 1;
                r = mid - 1; // try smaller penalty
            }else{
                // /????
              //  r = mid - 1;
                l = mid + 1;
            }
        }

        return ans;
    }


    private boolean canSplit5(int[] nums, int maxOperations, int ballCnt){
        int opCnt = 0;
       // int group = 0; // ???
        for(int x: nums){
            // ???
            if(opCnt > maxOperations){
                return false; // ???
            }
            // /??
//            while (x > ballCnt){
//                group += 1;
//                x = x %
//            }
            int curCnt = x / ballCnt;
            int remain = x % ballCnt;
            // ???
            if(remain != 0){
                curCnt += 1;
            }
            // ???
            opCnt += curCnt;
        }

        return opCnt <= maxOperations;
    }


    // LC 1277
    // 3.26 - 48 pm
    /**
     *    ->
     *
     *    return how `many` square `submatrices` have
     *    `all ones.`
     *
     *    - M X N matrix
     *
     *   ----------------
     *
     *    IDEA 1) BRUTE FORCE
     *
     *    IDEA 2) DP ???
     *
     *     - 2D DP ???
     *
     *     - DP def:
     *
     *        dp[i][j]:
     *          - total cnt of `submatrices` at
     *            cell (i, j)
     *
     *            - NOTE:
     *              ALL ONE submatrices
     *
     *     - DP eq:
     *
     *      dp[i][j] =
     *
     *        if m[i][j] == 1 && m[i-1][j] == 1 && m[i][j-1] == 1:
     *
     *            m[i][j] = m[i-1][j-1] + 1 // ????
     *
     *
     *
     *
     *
     *   ----------------
     *
     *
     *
     */
    // 7.41 - 51 am
    // IDEA: 2D DP
    public int countSquares(int[][] matrix) {
        // edge ???

        int n = matrix.length;
        int m = matrix[0].length;

        // ???
        /**
         *  DP def:
         *
         *    dp[i][j]:
         *       the `total` square submatrices
         *       at cell (i,j)
         *
         *
         *  DP eq:
         *
         *
         *
         */
        int[][] dp = new int[n][m];
        int ans = 0;

        // ???
        for(int y = 0; y < n; y++){
            for(int x = 0; x < m; x++){
                // ??? x or y == 0
                if(x == 0 || y == 0){
                    dp[y][x] = matrix[y][x]; //?????
                }
                else{
                    if(matrix[y][x] == 1){
                        dp[y][x] = Math.min(dp[y-1][x-1],
                                Math.min(dp[y-1][x], dp[y][x-1])) + 1;
                    }
                }

                // /??
                ans += dp[y][x];
            }
        }



        return ans;
    }









    // IDEA 2) DP ???
    public int countSquares_97(int[][] matrix) {
        // edge ??

        int n = matrix.length;
        int m = matrix[0].length;

        // ???
        int[][] dp = new int[n-1][m-1];

        // init ???
        for(int y = 0; y < n; y++){
            // ???
            if(dp[y][0] == 1){
                dp[y][0] = 1;
            }
        }

        for(int x = 0; x < m; x++){
            // ???
            if(dp[0][x] == 1){
                dp[0][x] = 1;
            }
        }


        // ???
        for(int y = 1; y < n; y++){
            for(int x = 1; x < m; x++){
                // ???
                if(dp[y][x] == 1 &&
                   dp[y-1][x] == 1 &&
                   dp[y][x-1] == 1){
                    // ????
                    dp[y][x] = (dp[y-1][x-1] + dp[y][x-1] + dp[y-1][x] + 1);
                }else{
                    dp[y][x] = (dp[y][x-1] + dp[y-1][x] + 1);
                }
            }
        }


        return dp[n-1][m-1];
    }


    // LC 1231
    // 4.42 - 52 pm
    /**
     *  -> Find the `maximum` total
     *  sweetness of the piece you can
     *  get by cutting the chocolate bar optimally.
     *
     *
     *  -------------
     *
     *  IDEA 1) GREEDY ???
     *
     *  IDEA 2) BRUTE FORCE ???
     *
     *  IDEA 3) DP ???
     *
     *  IDEA 4) binary search ???
     *
     *
     *  -------------
     *
     */
    //IDEA 4) binary search ???
    public int maximizeSweetness(int[] sweetness, int k) {

        return 0;
    }


    // LC 173
    // 7.14 - 24
    /**
     *
     *
     */
// Definition for a binary tree node.
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }




    class BSTIterator {
        // attr
        TreeNode root;
       // int val; // ??
        List<Integer> vals;
        int idx;

        public BSTIterator(TreeNode root) {
            this.root = root;
            this.vals = new ArrayList<>();
            helper(this.root); // ???
            this.idx = 0;
        }

        public int next() {

//            TreeNode _left = this.root.left;
//            TreeNode _right = this.root.right;
//            if(_left != null){
//                this.root = _left; // /???
//                return _left.val;
//            }
//
//            this.root = _right; // /???
//            return _right.val;
            this.idx += 1; // ???
            return this.vals.get(this.idx);
        }

        public boolean hasNext() {
//            if(this.root == null){
//                return false;
//            }
//            TreeNode _left = this.root.left;
//            TreeNode _right = this.root.right;
//            if(_left == null && _right == null){
//                return false;
//            }
//            return true;
            return this.idx < this.vals.size();
        }

        // /??
        // BST:
        // `IN ORDER` traverse
        // -> ascending order (small -> big)
        private void helper(TreeNode root){
            // edge
            if(root == null){
                return;
            }
            helper(root.left);
            this.vals.add(root.val);
            helper(root.right);
        }



    }


    // LC 99
    // 8.01 - 11 am
    /**
     *  ->  `Recover` the tree
     *     without changing its structure.
     *
     *
     *     where the values of `exactly two nodes `
     *     of the tree were `swapped` by mistake
     *
     *
     *  -----------------
     *
     *   IDEA 1) BST property + DFS ???
     *
     *      BST property:
     *        in order:
     *          ascending order (small -> big)
     *
     *
     *  -----------------
     *
     */

    public void recoverTree(TreeNode root) {

        List<TreeNode> list = new ArrayList<>();
        dfs1(root, list);

        // NOTE !! we DON'T sort
        // which will `break` the BST info

//        // NOTE !!!  sort (small -> big
//        Collections.sort(list, new Comparator<TreeNode>() {
//            @Override
//            public int compare(TreeNode o1, TreeNode o2) {
//                int diff = o1.val - o2.val;
//                return diff;
//            }
//        });

        // ???
        int idx1 = -1;
        int idx2 = -1;
        for(int i = 1; i < list.size() - 1; i++){
            TreeNode node = list.get(i);
            TreeNode prev = list.get(i-1);
            TreeNode next = list.get(i+1);
            if(node.val <= prev.val || node.val >= next.val){
                if(idx1 == -1){
                    idx1 = i;
                }else{
                    idx2 = i;
                }
            }

        }

        // SWAP !!!
        TreeNode node1 = list.get(idx1);
        TreeNode node2 = list.get(idx2);
        int tmp = node1.val;

        node1.val = node2.val;
        node2.val = tmp;
    }


    private void dfs1(TreeNode root, List<TreeNode> list){
        if(root == null){
            return;
        }
        dfs1(root.left, list);
        //list.add(root.val);
        list.add(root);
        dfs1(root.right, list);
    }




    // ???
    TreeNode node1;
    TreeNode node2;
    public void recoverTree_99(TreeNode root) {
        // edge
        if(root == null){
            return;
        }


        // ???
        BSTHelper(root, Integer.MAX_VALUE, -1 * Integer.MAX_VALUE);


        // swap ??
        int tmp = node1.val;
        node1.val = node2.val;
        node2.val = tmp;
    }

    // ????
    private void BSTHelper(TreeNode root, int smallest, int biggest){
        // edge
        if(root == null){
            return;
        }

        BSTHelper(root.left, smallest, root.val);
        // check
        // ???
        if(root.val <= smallest || root.val >= biggest){
            // ???
            if(node1 == null){
                node1 = root;
            }else{
                node2 = root;
            }
        }

        BSTHelper(root.right, root.val, biggest);
    }



    // LC 285
    // 8.50 - 9.00 am
    /**
     *
     *
     *
     */

    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        // edge
        if(root == null){
            return null;
        }
        // ??
        Stack<TreeNode> st = new Stack<>();
        // ???
        st.add(root);

        // /??
        while (!st.isEmpty()){
            // ???
//            while (root.left != null){
//                st.add(root.left);
//            }

            TreeNode cur = st.pop();
            if(cur.val == p.val){
                return st.pop(); // ??
            }
            // ????
            while (cur.right != null){
                st.add(cur.right);
            }
        }

        return null;
    }






    public TreeNode inorderSuccessor_99(TreeNode root, TreeNode p) {
        List<TreeNode> list = new ArrayList<>();
        dfs2(root, list);
        for(TreeNode x: list){
            if(x.val > p.val){
                return x;
            }
        }
        return null;
    }


    private void dfs2(TreeNode root, List<TreeNode> list){
        if(root == null){
            return;
        }
        dfs2(root.left, list);
        //list.add(root.val);
        list.add(root);
        dfs2(root.right, list);
    }


    // LC 769
    // 7.08 - 18 am
    /**
     *  -> Return the `largest` number
     *  of chunks we can make to sort the array.
     *
     *   - split the arr to chunks
     *      - and sort every sub array individually
     *
     *      -> NOTE: after merge, the result
     *               SHOULD BE SORTED as well
     *
     *
     *   - arr: int arr
     *      - len = n
     *      - [0, n-1]
     *
     *
     *
     *  -------------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) GREEDY ????
     *
     *  IDEA 3) SLIDE WINDOW ???
     *
     *
     *  -------------
     *
     *
     *
     */
    // 7.48 - 58 am
    // idea: prefix max ?? + for loop
    // core idea:
    // if min val == n (0 bases)
    //   -> we can split as new chunk
//    public int maxChunksToSorted(int[] arr) {
//        // edge
//
//        int chunk = 0;
//        int maxTillNow = -1 * Integer.MAX_VALUE; // ??
//
//        for(int i = 0; i < arr.length; i++){
//            maxTillNow = Math.max(maxTillNow, arr[i]);
//            // ???
//            if(maxTillNow == i){
//                chunk += 1;
//            }
//        }
//
//
//        return chunk > 0 ? chunk : 1;
//    }









//    // IDEA 3) SLIDE WINDOW ???
//    public int maxChunksToSorted(int[] arr) {
//        // edge
//        if(arr == null || arr.length <= 1){
//            return arr.length; // ???
//        }
//
//        int res = 1;
//        int curChunkCnt = 0;
//
//
//        // ???
//        int l = 0;
//        for(int r = 1; r < arr.length; r++){
//            if(canChunk(arr, r, l, r)){
//                curChunkCnt += 1;
//                l = r; // ??
//            }
//        }
//
//
//        // ???
//        return Math.max(res, curChunkCnt);
//    }
//
//
//    // ???
//    // idx: the `idx` in arr
//    // val: the `EXPECTED` val in sorted arr
//    private boolean canChunk(int[] arr, int val, int start, int end){
//        // edge
//        if(end < start){
//            return false;
//        }
//
//        int[] sub = Arrays.copyOfRange(arr, start, end); // ??
//        // sort
//        // by default, it's ascending order ??? (small -> big
//        Arrays.sort(sub);
//        // ???
//        for(int x: sub){
//            if(x != val){
//                return false;
//            }
//        }
//
//
//        return true;
//    }




    // LC 768
    // 8.06 - 16 am
    /**
     *
     *  NOTE !!!
     *    -> the element in arr
     *       could be DUPLICATED (difference VS LC 769)
     *
     *
     *   -----------------
     *
     *    IDEA 1) 1D DP ????
     *
     *
     *   -----------------
     *
     */
    public int maxChunksToSorted(int[] arr) {
        //edge

        int chunks = 0;
        int maxSoFar = -1 * Integer.MAX_VALUE; // ??

        // /??
        int[] arr2 = arr.clone(); // ??
        // sorting:
        // default should be: increasing ??? (small -> big
        Arrays.sort(arr2);

        for(int i = 0; i < arr.length; i++){
            maxSoFar = Math.max(arr[i], maxSoFar);
            // ????
            /**  NOTE !!!
             *
             * maxSoFar == arr2[i] only compares one value,
             * but for LC 768 you must ensure:
             *
             *      the entire prefix [0..i] in arr
             *      has the same multiset as [0..i] in sorted array
             *
             * So we need to compare all elements,
             * not just the max.
             *
             *
             *
             *
             */
            if(maxSoFar == arr2[i]){
                chunks += 1;
            }
        }



        return chunks;
    }










    // 7.50 - 8.00 am
    /**
     *
     *  IDEA 1) SLIDE WINDOW ?????
     *
     *
     */
    // IDEA: Prefix Sum Matching
    /** CORE IDEA:
     *
     * If two prefixes have the same sum,
     *   -> they contain the same multiset → valid chunk.
     *
     */
//    public int maxChunksToSorted(int[] arr) {
//
//        return 0;
//    }





    // idea: slide window
//    public int maxChunksToSorted(int[] arr) {
//        // edge
//
//        // case 1) element are ALL unique
//        // -> LC 769
//        Set<Integer> set = new HashSet<>();
//        for(int x: arr){
//            set.add(x);
//        }
//        if(arr.length == set.size()){
//            return LC768(arr); // ???
//        }
//
//
//        // case 2) element are NOT unique
//        // -> slide window
//        int chunks = 0;
//        int l = 0;
//        for(int r = 1; r < arr.length; r++){
//
//        }
//
//
//
//
//        return chunks;
//    }
//
//
//
//    private int LC768(int[] arr){
//        int res = 1;
//        int chunks = 0;
//        int maxSoFar = 0;
//
//        // ???
////        int[] arr2 = arr; // /?
////        Arrays.sort(arr2);
//
//        int[] arr2 = arr.clone();
//        Arrays.sort(arr2);
//
//        for(int i = 0; i < arr.length; i++){
//            // /??
//            maxSoFar = Math.max(maxSoFar, arr[i]);
//            if(maxSoFar == arr2[i]){
//                chunks += 1;
//            }
//        }
//
//
//        return Math.max(chunks, res);
//    }
//
//


    // LC 1028
    public TreeNode recoverFromPreorder(String traversal) {

        return null;
    }



    // LC 926
    // 10.23 - 33 am
    /**
     *  -> Return the `minimum` number
     *  of flips to make s monotone increasing.
     *
     *   - You are given a binary string s.
     *     You can flip s[i] changing it from
     *     0 to 1 or from 1 to 0.
     *
     *
     *     NOTE:
     *        there are ONLY 1, 0
     *        val in nums
     *
     *
     *  -------------
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) GREEDY
     *
     *  IDEA 3) MONO STACK ????
     *
     *  IDEA 4) 1D DP ???
     *
     *  -------------
     *
     *
     */
    // 7.25 - 35 am
    //  IDEA 4) 1D DP ???
    // NOTE: the updated str need to be in `000111` format
    //       or `00000` or `1111` format
    public int minFlipsMonoIncr(String s) {
        // edge

        int flip = 0;
        int oneCnt = 0;

        // ???
        for(char ch: s.toCharArray()){
            if(ch == '1'){

                // We DON'T `flip` 1s immediately. We just `COUNT` them
                // as "potential" `future` costs if we find a 0.

                oneCnt += 1;
            }else{
                /**  options:
                 *
                 *  1) flip all of prev and cur to `1`
                 *  2) keep as `0` ???
                 */

                // We found a '0'. To keep it `monotone`, we have `two` `choices`:
                // ->
                // 1. Flip this current '0' to a '1' (cost: flips + 1)
                // 2. Keep this '0', which means all previous '1's MUST be '0's
                //    (cost: onesCount)

                flip = Math.min(flip + 1, oneCnt);
            }
        }

        return flip;
    }










    //  IDEA 3) MONO STACK ????
    // (mono increasing ????
    // stack: FILO
    // check from right -> left
    // 2 cases:  starts with 0 or 1
    public int minFlipsMonoIncr_999(String s) {
        // edge

        int minOp = s.length();
        int cur = -1;
        int curOp = 0;
        // ???
        int prev = Integer.MAX_VALUE; // ????

        // case 1) start with 0
        for(int i = s.length() -1; i > 0; i--){
            // ???
            if(i == s.length() - 1){
                cur = getVal(s.charAt(i));
                if(cur != 0){
                    curOp += 1;
                }
                cur = 0;
                continue;
            }
            if(cur > prev){
                curOp += 1;
                cur = prev;
            }

            prev = cur;
        }

        minOp = Math.min(minOp, curOp);

        // reset
        //minOp = s.length();
        cur = -1;
        curOp = 0;
        // ???
        prev = Integer.MAX_VALUE; // ????

        // case 2) start with 1
        for(int i = s.length() -1; i > 0; i--){
            // ???
            if(i == s.length() - 1){
                cur = getVal(s.charAt(i));
                if(cur != 1){
                    curOp += 1;
                }
                cur = 1;
                continue;
            }
            if(cur > prev){
                curOp += 1;
                cur = 1; // ???
            }

            prev = cur;
        }




        return minOp;
    }


    private int getVal(char ch){
        return ch == '0' ? 0 : -1;
    }




    // LC 523
    // 7.14 -24 am
    /**
     *  IDEA 1) HASH MAP + PREFIX SUM
     *
     *  If two prefix sums have the
     *  same remainder mod k,
     *  their difference is divisible by k.
     *
     */
    // IDEA 1) HASH MAP + PREFIX SUM
    public boolean checkSubarraySum(int[] nums, int k) {
        // edge

        // map: { prefix_sum: idx }
        // prefix_1 - prefix_2 = k * N
        //   -> prefix_2 = prefix_1 - k * N
        //   and if prefix_1 is also k multiply
        //   -> prefix_2 = k * ()
        Map<Integer, Integer> map = new HashMap<>();

        // NOTE !!! init
        //map.put(0, 1); // ???
        map.put(0, -1); // ???
        int prefix = 0;

        for(int i = 0; i < nums.length; i++){
            int x = nums[i];

            // ???
            prefix += x;
            // ???
            //prefix = prefix % k;
            if (k != 0) {
                prefix %= k;
            }



            // ???
            // prefix_1 - prefix_2 = k
            //   -> prefix_2 = prefix_1 - k
            //int diff = prefix - k;
            if( map.containsKey(prefix % k) && i - map.get(prefix % k) >= 2 ){
                return true;
            }

            // ???
            // ?? get the smaller one
            if(!map.containsKey(prefix)){
                map.put(prefix, i);
            }

        }


        return false;
    }


    // LC 1031
    // 7.52 - 8.10 am
    /**
     *  -> return the `maximum` `sum` of elements
     *     in `two NON-overlapping` subarrays
     *     with lengths `firstLen and secondLen.`
     *
     *   - arr: int arr
     *   - firstLen, secondLen: int
     *
     *   - arr with `firstLen` could come BEFORE or AFTER
     *     than arr with secondLen
     *     and NOT OVERLAPPING
     *
     *
     *
     *  --------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) PREFIX SUM ????
     *
     *     map: { prefix_sum: idx }
     *
     *     -> try firstLen, then 2nd len
     *     -> try 2nd len, firstLen
     *
     *     -> get the max res
     *
     *
     *  --------------
     *
     *
     */
    /**
     * The key issue: this problem isn’t about matching prefix sums
     *
     * —> it’s about tracking the best subarray
     * seen so far while scanning.
     *
     *
     * ---------------
     * Correct Approach (Prefix Sum + Sliding Window)
     *
     * Use prefix sum + keep track of:
     *
     * best firstLen before secondLen
     * best secondLen before firstLen
     *
     * We try both orders.
     *
     * Use prefix sum + keep track of:
     *
     * best firstLen before secondLen
     * best secondLen before firstLen
     *
     * We try both orders.
     *
     */



    // 4.45 - 55 pm
    // IDEA 2) PREFIX SUM + sum arr op >??? ????
    public int maxSumTwoNoOverlap(int[] nums, int firstLen, int secondLen) {

//        return Math.max(
//                helper96(nums, firstLen, secondLen),
//               // helper96(nums, secondLen, firstLen),
//                );

        int v1 = helper96(nums, firstLen, secondLen);
        int v2 = helper96(nums, secondLen, firstLen);
        return Math.max(v1,v2);
    }


    /** NOTE !!!
     *
     *   L: left window
     *   M: right window
     */
    private int helper96(int[] nums, int L, int M){
        // edge

        // ?? get prefix sum
        int[] prefix = new int[nums.length + 1]; //??


        //?? what're the start, end idx ???
        // for(int i = 1; i < nums.length; i++){
//        for(int i = 1; i < nums.length; i++){
//            prefix[i] += prefix[i-1];
//        }
        for(int i = 0; i < nums.length; i++){
            prefix[i + 1] = (nums[i] + prefix[i]);
        }



        /** NOTE !!!
         *
         *   we maintain 2 var within loop:
         *
         *    1. max left sum (maxL)
         *    2. ans (biggest `lSum + rSum`)
         */


        // ??
        int maxSubSum = 0;
        int maxL = 0; // ???

        // /?
        /**
         *
         *  [ L ][ M ]
         *
         */
        /**
         * i = ending position of M window
         */
        // for(int i = L + M - 1; i < nums.length; i++){
        for(int i = L + M; i < nums.length + 1; i++){
            // ???
            int curL = prefix[i + L] - prefix[i]; // ???
            // ???
            maxL = Math.max(curL, maxL);

            //  ???
            int maxM = prefix[i + L + M] - prefix[i + L]; // ???

            maxSubSum = Math.max(maxM + maxL, maxSubSum);
        }

        // ???


        return maxSubSum;
    }











    // IDEA: Prefix Sum + Sliding Window  (gemini)
    public int maxSumTwoNoOverlap_96(int[] nums, int firstLen, int secondLen) {
        // edge

//        int ans = 0;
//
//        // ??
//        int[] prefix = new int[nums.length + 1]; // ??
//        int prefixSum = 0;
//
//        for(int i = 0; i < nums.length; i++){
//            // ???
//            prefixSum += nums[i];
//            prefix[i] = prefixSum;
//        }
//
//        // ???
//        for(int i = 0; i < nums.length; i++){
//            int val1 = helper98(prefix, firstLen, secondLen);
//            int val2 = helper98(prefix, secondLen, firstLen);
//            // ???
//            ans = Math.max(val1,
//                    Math.max(val2, ans)
//            );
//        }
//
//        return ans;

        return Math.max(
                helper98(nums, firstLen, secondLen),
                helper98(nums, secondLen, firstLen)
        );

    }



    /** NOTE !!!
     *
     *  L, M are `len` of sub array
     *    - firstLen, secondLen
     *    - secondLen, firstLen
     *
     * L: length of the first subarray (must come first)
     * M: length of the second subarray (comes after L)
     *
     */
    /**
     * L subarray comes BEFORE M subarray
     */
    // private int helper(int[] prefix, int L, int M) {
    private int helper98(int[] nums, int L, int M){
        // edge ??
        // ???
        int n = nums.length;

        // prefix sum
        int[] prefix = new int[n + 1];

        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }


        int maxL = 0;
        int ans = 0;

        /**
         * i = ending position of M window
         */
        // ???
        // for(int i = L-1; i < prefix.length; i++){
        for(int i = L + M; i < n; i++){

            /**
             * Try both orders:
             *
             * 1. firstLen before secondLen
             * 2. secondLen before firstLen
             *
             * For each order:
             * - maintain best L-window seen so far
             * - combine with current M-window
             *
             */

            // 1. get L
            // ???
            /**
             * L window:
             * [i-M-L, i-M)
             */
            //int firstSum = prefix[i + 1] - prefix[i-L]; // ????
            int lSum = prefix[i - M] - prefix[i - M - L];
            maxL = Math.max(maxL, lSum);


            // 2. get M
            // ???
            /**
             * M window:
             * [i-M, i)
             */
            //int secondSum = prefix[i + M + 1] - prefix[i];
            int mSum = prefix[i] - prefix[i - M];

            //ans = Math.max(firstSum, Math.max(secondSum, ans));
            //ans = Math.max(firstSum + secondSum, ans);
            ans = Math.max(ans, maxL + mSum);
        }


        return ans;
    }















    // IDEA 2) PREFIX SUM ????
    public int maxSumTwoNoOverlap_97(int[] nums, int firstLen, int secondLen) {
        // edge

        int n = nums.length;

        // build prefix sum
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }

//        int ans = 0;
//        // ???
//        for(int i = 0; i < nums.length; i++){
//            // ???
//        }
//
//        return ans;
        return Math.max(
                helper99(prefix, firstLen, secondLen),
                helper99(prefix, secondLen, firstLen)
        );


    }




    private int helper99(int[] prefix, int L, int M){
        int res = 0;
        // ??
        for(int i = L; i < M; i++){
            res += prefix[i];
        }
        return res;
    }








    // IDEA 2) PREFIX SUM ????
    public int maxSumTwoNoOverlap_999(int[] nums, int firstLen, int secondLen) {
        // edge

        // map: { prefix_sum: idx }
        Map<Integer, Integer> map = new HashMap<>();
        // ???

        map.put(0, -1);
        int prefix = 0;
        int ans = 0;

        // try firstLen, then 2nd len
        for(int i = 0; i < nums.length; i++){
            int x = nums[i];
            prefix += x;

            // ???
            map.put(prefix, i);
        }




        return ans;
    }



    // LC 304
    // 10.13 - 32 am
    /**
     *
     *  IDEA 1) 2D PREFIX SUM
     *
     */
    // 10.52 - 11.02 am
    //  IDEA 1) 2D PREFIX SUM
    class NumMatrix {

        //int elementCnt;
        int n;
        int m;
        int[][] prefix;


        // Attributes
        int[][] matrix;
        int[][] preSumMatrix;


        public NumMatrix(int[][] matrix) {
            // ???
            this.n = matrix.length;
            this.m = matrix[0].length;

            this.matrix = matrix;
            int l = matrix.length;
            int w = matrix[0].length;

            // NOTE !!!
            // use (n+1) x (m+1)
            // ???
            this.prefix = new int[n + 1][m + 1];

            // ???
            this.prefix[0][0] = matrix[0][0];

            // ???

            // ???
//            for(int y = 0; y < this.n; y++){
//                for(int x = 0; x < this.m; x++){
//
//                }
            for(int y = 1; y < this.n + 1; y++){
                for(int x = 1; x < this.m + 1; x++){
//                    // ???
//                    if(x == 0 && y == 0){
//                        continue;
//                    }
//                    // ???
//                    if(y == 0){
//                        this.prefix[y][x] = this.prefix[y][x-1] + matrix[y][x];
//
//                    }else if(x == 0){
//                        // ???
//                        this.prefix[y][x] = this.prefix[y-1][x] + matrix[y][x];
//                    }else{
//                        // /??
//                        this.prefix[y][x] =
//                                        this.prefix[y][x-1]
//                                        + this.prefix[y-1][x]
//                                        + matrix[y][x]
//                                        - this.prefix[y-1][x-1];
//                    }

                    this.prefix[y][x] =
                            this.prefix[y][x-1]
                                    + this.prefix[y-1][x]
                                   // + matrix[y][x]
                                    + matrix[y - 1][x - 1]
                                    - this.prefix[y-1][x-1];

                }
            }


        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // edge

            //return 0;
//            return this.prefix[row2][col2]
//                    - this.prefix[row2-1][col2]
//                    - this.prefix[row2][col2-1]
//                    + this.prefix[row1][col1];

            return preSumMatrix[row2 + 1][col2 + 1]
                    - preSumMatrix[row1][col2 + 1]
                    - preSumMatrix[row2 + 1][col1]
                    /** NOTE !!!
                     *
                     *  need to add the overlap back
                     *   (preSumMatrix[row1][col1])
                     */
                    + preSumMatrix[row1][col1];

        }


    }










    class NumMatrix_96 {

        // ???
        //int elementCnt;
        int n;
        int m;
        int[][] prefix;

        public NumMatrix_96
                (int[][] matrix) {
            // ???
            this.n = matrix.length;
            this.m = matrix[0].length;

            // NOTE !!!
            // use (n+1) x (m+1)
            this.prefix = new int[n + 1][m + 1];

            //this.prefix = new int[n][m]; // ????

            // ??? prepare 2D prefix sum
            //int prefixSum = 0;
            for(int y = 0; y < this.n; y++){
                for(int x = 0; x < this.m; x++){
                    // ???
                    if(x == 0 || y == 0){
                        this.prefix[y][x] = matrix[y][x];
                    }
                    else{
                        this.prefix[y][x] = (
                                  this.prefix[y-1][x]
                                  + this.prefix[y][x-1]
                                  - this.prefix[y-1][x-1]
                                  + matrix[y][x]
                                );
                    }
                }
            }


        }

        public int sumRegion(int row1, int col1, int row2, int col2) {
            // edge // ???
            if(row2 + col2 < row1 + col1){
                // swap ???
                int row1Tmp = row1;
                int col1Tmp = col1;

                row1 = row2;
                col1 = col2;

                row2 = row1Tmp;
                col2 = col1Tmp;
            }

            /**  NOTE !!!
             *
             *  for 2D prefix sum
             *
             *  ->
             *
             *   // ???
             *
             *   sum[y][x] =
             *      prefix[y][x]
             *      - prefix[y-1][x]
             *      - prefix[y-1][x - 1]
             *       + prefix[y-1][x-1]
             *
             *
             */
            return this.prefix[row2][col2] -
                    this.prefix[row2-1][col2] -
                    this.prefix[row2][col2-1] +
                    this.prefix[row1][col1];
        }


    }





    // LC 1015
    // 7.42 - 52 am
    /**
     *  -> Return the length of n.
     *  If there is no such n, return -1.
     *
     *  k: positive int
     *
     *  -> find len of smallest positive int (n),
     *    s.t. n % k == 0
     *    and  n ONLY contains the digit 1
     *
     *
     *  ------------
     *
     *   IDEA 1) MATH + BRUTE FORCE ???
     *
     *
     *  ------------
     *
     *
     */
    // IDEA 1) MATH + BRUTE FORCE ???
    public int smallestRepunitDivByK(int k) {
        int val = 0;

        // ???
        for(int i = 0; i < k; i++){
            val = val * 10 + 1;

            System.out.println(">>> val = " + val
                    + ", k = " + k);
            if(val % k == 0){
                return i+1; // ???
            }
        }



        return val % k == 0 ? k : -1;
    }




    // LC 120
    // 8.22 - 32 am
    /**
     *
     *  -> Given a triangle array,
     *  return the `minimum path sum `
     *  from top to bottom.
     *
     *   -  if you are on index i on the current row,
     *     you may move to either index i
     *     or index i + 1 on the next row.
     *
     *
     *  -----------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DP
     *
     *
     *  -----------
     *
     */
    // IDEA 2) DP
    public int minimumTotal(List<List<Integer>> triangle) {
        // edge
        if (triangle == null || triangle.size() == 0) {
            return 0;
        }


        if(triangle.size() == 1){
            return triangle.get(0).get(0);
        }

        // ???
        int minPathSum = 10000 * 200; // ???

        // ??
        /**  DP def:
         *
         *    dp[i][j]: min path sum as (i.j)
         *
         *   DP eq:
         *
         *
         * ----
         *
         *   DP def:
         *
         *     dp[i]: min path sum at layer i
         *
         */

        // dp[i][j] = `minimum` path sum
        // to reach triangle[i][j]

        // ???
        int l = triangle.size();
       //int w = triangle.get(triangle.size() - 1).size(); // ???

        int[][] dp = new int[l][l];


        //int[][] dp = new int[l][w]; // ?????
        //int[] dp = new int[l]; // ???

        // init ???
        dp[0][0] = triangle.get(0).get(0);

        // ??
       // int minPathSum = 0;

        // ???
        for(int y = 1; y < l; y++){

            List<Integer> row = triangle.get(y);

            for(int x = 0; x < row.size(); x++){
                // ???
                int lastSmall = 1000; // ???

                int val = row.get(x);

                // left edge
                if(x == 0){
                    dp[y][x] = dp[y-1][x] + val; // ???
                }

                // right edge
                else if(x == row.size() - 1){
                    dp[y][x] = dp[y - 1][x - 1] + val;
                }

                // middle
                else{
                    dp[y][x] = Math.min(dp[y - 1][x - 1],
                            dp[y - 1][x]) + val;
                }

//                // ???
//                if(x - 1 >= 0){
//                    lastSmall = Math.min(lastSmall, dp[y-1][x-1]);
//                }
//                if(x + 1 < row.size()){
//                    lastSmall = Math.min(lastSmall, dp[y-1][x-1]);
//                }
//                dp[y][x] = lastSmall + row.get(x);
            }

        }

        // ??
        for(int x: dp[l-1]){
            minPathSum = Math.min(x, minPathSum);
        }

        return minPathSum;
    }










    // IDEA 1) BRUTE FORCE
    public int minimumTotal_99(List<List<Integer>> triangle) {
        // edge
        if(triangle.size() == 1){
            return triangle.get(0).get(0);
        }

        // ???
        int minPathSum = 10000 * 200; // ???

        // ??
        for(int i = 0; i < triangle.size(); i++){

            List<Integer> row = triangle.get(i);

            for(int j = 0; j < row.size(); j++){
                int left = j - 1;
                int right = j + 1;

                // ???
                int val = row.get(j);

                // ???
                if(left >= 0){
                  //  val
                }
            }
        }


        return minPathSum;
    }



    // LC 161
    // 10.10 - 20 am
    /**
     *   -> Given two strings s and t,
     *     determine if they are
     *     both `one edit distance` apart.
     *
     *     3 types OP that is acceptable:
     *
     *       -  Insert a character into s to get t
     *       - Delete a character from s to get t
     *       - Replace a character of s to get t
     *
     *  --------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) DP ???
     *
     *   IDEA 3) 2 POINTERS ???
     *
     *
     *  --------------------
     *
     *
     *  ex 1)
     *
     *  Input: s = "ab", t = "acb"
     *  Output: true
     *
     *
     *  ex 2)
     *
     *   Input: s = "cb", t = "acb"
     *   Output: true
     *
     *
     *
     *
     */
    /**  MAIN BUGS:
     *
     * Wrong swap logic
     * Possible IndexOutOfBounds on s.charAt(i)
     * Incorrect loop/break condition
     * Missing handling for extra last char
     * Final return condition is wrong for insertion case
     *
     */
    // IDEA: DP
    // 7.56 - 8.06 am
    public boolean isOneEditDistance(String s, String t) {
        // edge
        // equal strings -> 0 edits
        if (s.equals(t)) {
            return false;
        }

        // ???
        // assume: t is the longer one
        int len_s = s.length();
        int len_t = t.length();


        // length difference > 1 impossible
        if (Math.abs(len_s - len_t) > 1) {
            return false;
        }

        if(len_s > len_t){
            return isOneEditDistance(t, s);
        }

        // /??
        int[][] dp = new int[len_s + 1][len_t + 1]; // ???

        // init ???
        for(int i = 0; i < len_s + 1; i++){
            dp[i][0] = i; // len_s; // ???
        }
        for(int i = 0; i < len_t + 1; i++){
            dp[0][i] = i; //len_t; // ???
        }


        for(int i = 1; i < len_s + 1; i++){
            for(int j = 1; j < len_t + 1; j++){
                // case 1) equals
                // NOTE !!!
                // (s.charAt(i-1) == t.charAt(j-1))
                if(s.charAt(i-1) == t.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1];
                }
                // case 2) NOT equals
                else{
                    // NOTE !!
                    // 3 ops: insert, delete, replace

                    /**
                     *  insert
                     *
                     *
                     *  s = "ab", t = "acb"
                     *
                     *  -> "ab", "a"
                     *
                     */
                    int insert = dp[i][j-1] + 1;

                    /**
                     *  delete
                     *
                     *
                     *  s = "axb", t = "ab"
                     *
                     *  -> "ab", "abc"
                     *
                     */
                    int delete = dp[i-1][j] + 1;

                    int replace = dp[i-1][j-1] + 1;

                    // NOTE !!!
//                    dp[i][j] = Math.min(dp[i][j],
//                            Math.min(insert, Math.min(delete, replace)));


                    dp[i][j] = Math.min(insert,
                            Math.min(delete, replace));

                }
            }
        }



        // ???
        return dp[len_s][len_t] == 1;
    }





    // IDEA: DP
    // 10.51 - 11.01 AM
    boolean isOneEditDistance_988(String s, String t) {
        // edge
        // equal strings -> 0 edits
        if (s.equals(t)) {
            return false;
        }

        // ???
        // assume: t is the longer one
        int len_s = s.length();
        int len_t = t.length();


        // length difference > 1 impossible
        if (Math.abs(len_s - len_t) > 1) {
            return false;
        }

        if(len_s > len_t){
            return isOneEditDistance_988(t, s);
        }

        // ??
        /**  DP def:
         *    op cnt at (i, j)
         *    i is the idx of s,
         *    j is the idx of t
         *
         *   DP eq:
         *
         *
         */

        /**
         * dp[i][j]
         * =
         * min edits to convert:
         *
         * s[0...i-1] -> t[0...j-1]
         */

        // because DP includes empty-string states.
        //int[][]dp = new int[len_s][len_t];
        int[][] dp = new int[len_s + 1][len_t + 1];

//        // init ??
//        // if t is `null` ????
//        for(int i = 0; i < len_s; i++){
//            dp[i][0] = 0; // ???
//        }
//        // if s is `null` ????
//        for(int i = 0; i < len_t; i++){
//            dp[0][i] = 0; // ???
//        }

        // init
        for (int i = 0; i <= len_s; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len_t; j++) {
            dp[0][j] = j;
        }


        for(int i = 1; i <= len_s; i++){
            for(int j = 1 ; j <= len_t; j++){
                // 3 ops:

                // if same, continue
                if(s.charAt(i-1) == t.charAt(j-1)){
                    // ???
                    dp[i][j] = dp[i-1][j-1];
                }else{
                    // insert
                    // ???
//                    dp[i][j] = Math.min(dp[i][j],
//                            dp[i-1][j] + 1
//                    );

                    int insert = dp[i][j - 1] + 1;

                    // delete
                    // ???
//                    dp[i][j] = Math.min(dp[i][j],
//                            dp[i][j-1] + 1
//                    );
                    int delete = dp[i - 1][j] + 1;

                    // replace
                    // ???
//                    dp[i][j] = Math.min(dp[i][j],
//                            dp[i-1][j-1] + 1);

                    int replace = dp[i - 1][j - 1] + 1;

                    dp[i][j] = Math.min(insert,
                            Math.min(delete, replace));

                }


//                // early quit ???
//                if(dp[i][j] > 1){
//                    return false;
//                }

            }
        }


        // ???
        return dp[len_s - 1][len_t - 1] == 1;
    }















    // IDEA 3) 2 POINTERS ???
    public boolean isOneEditDistance_99(String s, String t) {
        // edge
        // equal strings -> 0 edits
        if (s.equals(t)) {
            return false;
        }

        // ???
        // assume: t is the longer one
        int len_s = s.length();
        int len_t = t.length();


        // length difference > 1 impossible
        if (Math.abs(len_s - len_t) > 1) {
            return false;
        }



        // assume:
        // t is the longer one
        //   -> if NOT, swap ?????

        // ensure s is shorter (or equal)
        if(len_t < len_s){
            String tmp = s;
            s = t;
            t = tmp;
        }



        // ???
        len_s = s.length();
        len_t = t.length();




        // /??
        boolean isLenEquals = len_s == len_t;


        int op = 0;

        // case 1) s len == t len
        // case 2) s len != t len

        // ???
        int i = 0;
        // NOTE !!! we move j anyway (idx in t)
        for(int j = 0; j < len_t; j++){
            if(s.charAt(i) == t.charAt(j)){
                i += 1;
            }else{
                op += 1;
                // ???
                if(op > 1){
                    return false;
                }

                // ???
                if(isLenEquals){
                    i += 1;
                }
            }

            // ?? if i read the end of s
            if(i == s.length() - 1){
                break;
            }
        }


        return op == 1; // ????
    }


    // LC 1943
    // 3.07 - 17 pm
    /**
     *   -> Return the 2D array painting
     *       describing the `finished painting`
     *       (excluding any parts that are not painted).
     *       You may return the segments in any order.
     *
     *   - painting[j] = [leftj, rightj, mixj]
     *
     *      - [leftj, rightj)
     *      -  mixed color sum of mixj
     *
     *
     *   -> if colors 2, 4, and 6 are mixed,
     *      then the resulting mixed color
     *      is {2,4,6}.
     *
     *      -> , you should only output
     *           the `sum` of the elements
     *           in the `set`
     *           rather than the full set.
     *
     *
     *  -----------
     *
     *   IDEA 1) SCAN LINE
     *
     *
     *  -----------
     */
    // LC 1943
    // 8.04 - 14 AM
    /**
     *
     *
     */
    //  IDEA 1) SCAN LINE
    public List<List<Long>> splitPainting(int[][] segments) {
        // edge
        //int n = hours.length;
        List<Integer[]> list = new ArrayList<>();

        for(int[] seg: segments){
            int start = seg[0], end = seg[1], color = seg[2];
            list.add(new Integer[]{start, color});   // start adds color
            list.add(new Integer[]{end, -1 * color});    // end subtracts color
        }

        // sort ??
        Collections.sort(list, new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[0] - o2[0];
                return diff; // ????
            }
        });


        List<List<Long>> res = new ArrayList<>();
        // ??
        // ???
        int start = -1;
        int end = -1;
        int colorCumSum = 0;

        for(Integer[] x: list){
            int idx = x[0];
            int color = x[1];
            // ???
            colorCumSum += color;
            // ???
            if(start == -1){
                start = idx;
            }else{
                end = idx; // ??
            }
//            if(color > 0){
//                start = idx;
//            }else{
//                end = idx;
//                // ???
//                // ???
//                start = end;  //??????
//            }
            // ??
            if(start != -1 && end != -1){
                List<Long> tmp = new ArrayList<>();
                tmp.add((long) start);
                tmp.add((long) end);
                tmp.add((long) colorCumSum);
                res.add(tmp);
                // ???
                start = end;  //??????
            }

        }


        return res;
    }








    //  IDEA 1) SCAN LINE
    public List<List<Long>> splitPainting_03(int[][] segments) {
        // edge

        List<Integer[]> list = new ArrayList<>();
        for(int[] seg: segments){
//            int start = x[0];
//            int end = x[1];
//
//            // ???
////            list.add(new Integer[]{start, 1});
////            list.add(new Integer[]{end, -1});

            int start = seg[0], end = seg[1], color = seg[2];
            list.add(new Integer[]{start, color});   // start adds color
            list.add(new Integer[]{end, -color});    // end subtracts color

        }

        // sort ???
//        Collections.sort(list, new Comparator<Integer[]>() {
//            @Override
//            public int compare(Integer[] o1, Integer[] o2) {
//                // ???
//                int diff = o1[0] - o2[0];
//                // ???
//                if(diff == 0){
//                    return o1[1] - o2[1];
//                }
//                return diff;
//            }
//        });
        Collections.sort(list,
                (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);



        List<List<Long>> res = new ArrayList<>();
        Long cur = 0L;

        // ????
        int tmpStart = -1;
        int tmpEnd = -1;

        // NOTE !!!
        long curColor = 0;
        int prevPos = -1;



        for(Integer[] x: list){
            // ???
            int idx = x[0];
            int state = x[1];
            if(state == 1){
                cur += idx;
                tmpStart = idx;
            }else{
                cur  += (-1 * idx);
                tmpEnd = idx;
            }

            // ????
            if(state == -1){
                List<Long> tmp = new ArrayList<>();
                // ???
                tmp.add((long) tmpStart);
                tmp.add((long) tmpEnd);
                tmp.add(cur);
                res.add(tmp);

                // reset start, end ???
                tmpStart = tmpEnd; // ???
                tmpEnd = -1;
            }

        }


        return res;
    }



    // LC 1124
    // 3.52 - 4.02 pm
    /**
     *  -> Return the length of the
     *     longest well-performing interval.
     *
     *
     *   - tiring day
     *      if and only if
     *      the number of hours
     *      worked is `(strictly) greater than 8.`
     *
     *
     *   NOTE:
     *
     *     A well-performing interval is an
     *     interval of days for which the
     *     number of `tiring days` is strictly
     *     `larger` than the number of `non-tiring days.`
     *
     *
     *
     *  ----------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *   IDEA 2) SLIDE WINDOW ???
     *
     *   IDEA 3) HASHMAP + PREFIX SUM
     *`
     *      -> prefix sum: cnt ` tiring day` ?
     *      -> prefix sum: cnt ` normal day` ?
     *
     *      -> hashmap:
     *        { tiring day : idx }  // ??
     *
     *       ->
     *
     *  ----------
     *
     */




    // IDEA 3) HASHMAP + PREFIX SUM
    public int longestWPI_99(int[] hours) {
        // edge

        int n = hours.length;
        // Convert hours to 1 (tiring) or -1 (non-tiring)
        int[] work = new int[n];
        for (int i = 0; i < n; i++) {
            work[i] = hours[i] > 8 ? 1 : -1;
        }



        int tiringDay = 0;
        //int n = hours.length;
        int[] prefix1 = new int[n + 1]; // ???
        int[] prefix2 = new int[n + 1]; // ???

        if(hours[0] > 8){
            tiringDay += 1;
        }


        int prefix = 0;
        int maxLen = 0;


        // ???
        // map1: { tiring_day_cum_sum: idx }
        Map<Integer, Integer> map1 = new HashMap<>();
        // map2: { normal_day_cum_sum: idx }
        Map<Integer, Integer> map2 = new HashMap<>();

        for(int i = 1; i < hours.length; i++){
            int x = hours[i];
            if(x > 8){
                tiringDay += 1;
                prefix1[i] += prefix1[i-1];
                map1.put(prefix1[i], i); // ??
            }else{
                prefix2[i] += prefix2[i-1];
                map2.put(prefix2[i], i); // ??
            }
        }
        // ??
        if(tiringDay == 0){
            return 0;
        }

        int ans = 0;

        // ???
        for(int i = 0; i < prefix1.length; i++){
           // if(prefix1[i+1] )
        }





        return ans;
    }





    // LC 71
    // 7.15 - 25 AM
    /**
     *  -> Return the simplified canonical path.
     *
     *
     *  ------------
     *
     *
     *  IDEA 1) STACK (FILO)
     *
     *
     *  ------------
     *
     *
     */
    // IDEA 1) STACK (FILO)
    public String simplifyPath(String path) {
        // edge
//        if(path == null || path.equals(".")){
//            return null; /// ???
//        }
        if(path == null){
            return null; /// ???
        }


        Stack<String> st = new Stack<>();
        StringBuilder sb = new StringBuilder();

        // ??
        String[] arr = path.split("/");

        if(arr.length == 1){
            return arr[0]; // ???
        }

        // /??
        for(int i = 0; i < arr.length; i++){
//            if(i == 0 || i == arr.length - 1){
//                continue;
//            }
            String cur = arr[i];
            // ??
            if(cur.equals("..")){
                if(!st.isEmpty()){
                    st.pop();
                }
            }
            // ???
            else if(cur.isEmpty() || cur.equals(".")){
                continue;
            }
            else{
                st.add(cur);
            }
        }

        // ???
        if(st.isEmpty()){
            return "/";
        }

        // ??? handle `first` `/`
        sb.append("/");

        // ???
        int size = st.size();
        int i = 0;
        for(String x: st){
            sb.append(x);
            if(i < size - 1){
                sb.append("/");
            }
            i += 1;
        }


        return sb.toString();
    }



    // LC 1023
    // 8.40 - 50 am
    /**
     *  -> return a boolean array answer
     *      where answer[i] is true if queries[i]
     *      matches pattern, and false otherwise.
     *
     *
     *    NOTE:
     *      - A query word queries[i] matches pattern if you can insert
     *        lowercase English letters into the
     *        pattern so that it equals the query.
     *
     *        -> You may insert a character at any position
     *            in pattern or you may choose not to
     *            insert any characters at all.
     *
     *
     *  ---------------
     *
     *  IDEA 1) 2 POINTERS
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     *  ---------------
     *
     *
     */
    // IDEA 1) 2 POINTERS
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        // edge

        List<Boolean> res = new ArrayList<>();

        for(int i = 0; i < queries.length; i++){
//            boolean resp = canGen(queries[i], pattern);
//            res.add(resp);
            res.add(canGen(queries[i], pattern));
        }


        return res;
    }


    private boolean canGen(String query, String pattern){
        // edge ??

        //char cur = query.charAt(i);

        //int i = 0;
        int j = 0;
        for(int i = 0; i < query.length(); i++){

            char cur = query.charAt(i);

            // match pattern char
            if (j < pattern.length() && cur == pattern.charAt(j)) {
                j++;
            }

//            if(cur == pattern.charAt(j)){
//                j += 1;
//            }
            else if(Character.isLowerCase(cur)){
                continue;
            }else{
                return false;
            }
        }

        return j == pattern.length() - 1;
    }


    // LC 539
    // 3.35 - 45 pm
    /**
     * IDEA 1) CUSTOM SORTING
     *
     * IDEA 2) sort (str -> int)
     *
     *
     */
    //IDEA 2) sort (str -> int)
    public int findMinDifference(List<String> timePoints) {
        // edge
        if(timePoints.size() <= 1){
            return 0;
        }

        List<Integer> list = new ArrayList<>();
        for(String x: timePoints){
            list.add(getTimeStamp(x));
        }

        // ??? sorting
        Collections.sort(list);

        int minGap = Math.abs(list.get(0) - list.get(list.size() - 1));
        for(int i = 1; i < list.size(); i++){
            // ???
            int val1 = Math.abs(list.get(i) - list.get(i-1));
            // ???
            int val2 = Math.abs(1440 - list.get(i) - list.get(i-1));

            minGap = Math.min(minGap,
                    Math.min(val1, val2)
            );
        }


        return minGap;
    }


    private int getTimeStamp(String str){
        int res = 0;
        // ???
        if(str.isEmpty()){
            return 0;
        }
        String arr[] = str.split(":");
        res += Integer.parseInt(arr[0]) * 60;
        res += Integer.parseInt(arr[1]);
        System.out.println(">>> str = " + str
        + ", res = " + res);
        return res;
    }





    // LC 1877
    // 4.12 - 22 pm
    /**
     *
     * -> Return the
     * `minimized maximum pair sum `
     * after `optimally pairing up` the elements.
     *
     *  ------
     *
     *  IDEA 1) SORT
     *
     *  IDEA 2) GREEDY
     *
     */
    //  IDEA 1) SORT + 2 pointers
    // 11.16 - 26 am
    /**
     *
     *
     */
    public int minPairSum(int[] nums) {
        // edge
        // default: small -> big ???
        Arrays.sort(nums);

        int ans = nums[nums.length - 1] - nums[0];
        int l = 0;
        int r = nums.length - 1;
        // ??
        while (r > l){
            ans = Math.max(ans, nums[r] - nums[l]);
            l += 1;
            r -= 1;
        }

        return ans;
    }








    // LC 710
    // 11.22 - 12.01 am
    /**
     *
     *
     */
    class Solution {
        // ???
        HashSet<Integer> set;
        int n;
        int[] blacklist;
        Random random;

        public Solution(int n, int[] blacklist) {
            this.set = new HashSet<>();
            this.n = n;
            this.blacklist = blacklist;
            for(int x: blacklist){
                this.set.add(x);
            }
            // ???
            this.random = new Random();
        }

        public int pick() {
            // edge
            if(this.n <= 1){
                return this.n; // :::
            }
            //int val = -1;
            int val = this.random.nextInt(n); // ???
            while (this.set.contains(val)){
                val = this.random.nextInt(n);
            }
            return val; // ??
        }


    }








    // LC 2013
    // 8.07 - 24 am
    /**
     *
     *
     */
    class DetectSquares {


        // key = "x,y"
        private Map<String, Integer> cnt;
        private List<int[]> points;


        // ???
        Map<Integer[], Integer> map;
        int cellCnt;
        // ???
        Map<Integer[], Integer> cache;

        public DetectSquares() {
            this.map = new HashMap<>();
            this.cellCnt = 0;
            this.cache = new HashMap<>();
        }

        public void add(int[] point) {
            // ???
            Integer[] key = new Integer[]{point[0], point[1]};
            map.put(key, map.getOrDefault(key, 0) + 1);
            this.cellCnt += 1;
        }

        public int count(int[] point) {
            // edge
            if(map.isEmpty() || this.cellCnt < 4){
                return 0;
            }
            // ??? if point already `existed`
            Integer[] key = new Integer[]{point[0], point[1]};
            if(map.containsKey(key)){
                return this.cache.get(key); // ???
            }

            // ???
            /**  IDEA:
             *
             *  1. loop over cells,
             *
             *     get 2 cell as a pair,
             *  2. check if there exist
             *     2 other cells that can
             *     form a valid `square`
             *
             *  3. cnt the val per above,
             *     and add to final result
             *
             *   ... repeat above
             */
            int cnt = 0;
            // ???
            for(Integer[] k: map.keySet()){
                // ??
                int x1 = k[0];
                int y1 = k[1];
                int x2 = point[0];
                int y2 = point[1];

                //???
                int tmpx1 = x1;
                int tmpy1 = y2;

                int tmpx2 = x2;
                int tmpy2 = y1;

                Integer[] cellNew = new Integer[]{x2, y2};

                Integer[] cell1 = new Integer[]{tmpx1, tmpy1};
                Integer[] cell2 = new Integer[]{tmpx2, tmpy2};

                if(map.containsKey(cell1) && map.containsKey(cell2)){
                    int val = map.get(cell1) * map.get(cell2);
                    cnt += val; // ???
                    // ??? update cache ???
                    cache.put(cellNew, val);
                }

            }


            return cnt;
        }
    }




    // LC 2126
    // 10.09 - 19 am
    /**
     *  -> Return true if all asteroids can be destroyed.
     *  Otherwise, return false..
     *
     *
     *
     *   Asteroids 小行星
     *
     *
     *
     *  ---------
     *
     *   IDEA 1) GREEDY
     *
     *   IDEA 2) SORT
     *    -> small -> big ???
     *
     *
     *  ---------
     *
     *
     */
    // IDEA 2) SORT
    public boolean asteroidsDestroyed(int mass, int[] asteroids) {
        // edge
        if(asteroids == null || asteroids.length == 0){
            return true;
        }

        // default: small -> big ???
        Arrays.sort(asteroids);
        //int cur = 0;
        Long mass2 = (long) mass; // ???
        for(int x: asteroids){
            System.out.println(">>> mass2 = " + mass2
            + ", x = " + x);
            if(mass2 < x){
                return false;
            }
            mass2 += x;
        }


        return true;
    }



    // LC 288
    // 10.44 - 54 am
    /**
     *  Abbreviation 縮寫
     *
     *   ->  find whether its abbreviation
     *   is unique in the dictionary
     *
     *     NOTE:
     *      - A word's abbreviation is unique
     *        if no other word from the dictionary
     *        has the same abbreviation.
     *
     *
     *     abbreviations:
     *        <first letter><number><last letter>.
     *
     *
     *  -----------
     *
     *   IDEA 1) HASHMAP
     *
     *
     *  -----------
     *
     */
    // IDEA 1) HASHMAP
    public boolean ValidWordAbbr(String[] dictionary) {
        // edge
        if(dictionary == null || dictionary.length <= 1){
            return true; // ??
        }

        //Map<String, Integer> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        // ??
        for(String x: dictionary){
            String abbr = getAbbr(x);
            if(set.contains(abbr)){
                return false;
            }
            set.add(getAbbr(x));
        }
        return true;
    }


//    public boolean isUnique(String word) {
//        return false;
//    }

    private String getAbbr(String str){
        if(str.length() <= 2){
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.charAt(0));
        sb.append(str.length() - 2);
        sb.append(str.charAt(str.length() - 1));

        return sb.toString();
    }










}
