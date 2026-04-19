package ws;

import LeetCodeJava.DataStructure.TreeNode;
//import com.sun.org.apache.bcel.internal.generic.SIPUSH;

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







}
