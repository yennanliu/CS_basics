package dev;

import java.util.*;

public class Workspace21 {

    // LC 62
    public int uniquePaths(int m, int n) {
        // edge
        if (m == 1 || n == 1) {
            return 1;
        }

        int[][] dp = new int[m][n];
        // init
        for(int y = 0; y < m; y++){
            dp[y][0] =  1;
        }
        for(int x = 0; x < n; x++){
            dp[0][x] =  1;
        }


        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                dp[y][x] = dp[y-1][x] + dp[y][x-1];
            }
        }

        ///  /???
        return dp[m-1][n-1];
    }


    // LC 63
    // 17.18 - 28 pm
    /**
     * -> Return the number of possible unique paths
     *   that the robot can take to reach the bottom-right corner.
     *
     *    NOTE:
     *
     *     - An obstacle and space are marked as 1 or 0 respectively in grid.
     *       A path that the robot takes cannot include any square that is an obstacle.
     *
     *  ---------------------
     *
     *   IDEA 1) MULTI SOURCE BFS  -> TLE ???
     *
     *   IDEA 2) DP
     *
     *
     *  ---------------------
     *
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if(obstacleGrid == null){
            return 0;
        }
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // edge // ???
        if (m == 0 || n == 0) {
            return 0;
        }
        if(obstacleGrid[m-1][n-1] == 1){
            return 0;
        }

        int[][] dp = new int[m][n];
        // init
        for(int y = 0; y < m; y++){
            //dp[y][0] =  1;
            if(obstacleGrid[y][0] != 1){
                dp[y][0] =  1;
            }
        }
        for(int x = 0; x < n; x++){
           // dp[0][x] =  1;
            if(obstacleGrid[0][x] != 1){
                dp[0][x] =  1;
            }
        }


        for(int y = 1; y < m; y++){
            for(int x = 1; x < n; x++){
                // case 1) (x,y) is NOT obstacle
                if(obstacleGrid[y][x] != 1){
                    if(dp[y-1][x] != -1){
                        dp[y][x] += dp[y-1][x];
                    }
                    if(dp[y][x-1] != -1){
                        dp[y][x] += dp[y][x-1];
                    }
                   // dp[y][x] = dp[y-1][x] + dp[y][x-1];
                }
                // case 1) (x,y) is obstacle
                else{
                    dp[y][x] = -1; // ??? // mark as `CAN'T visit`
                }
            }
        }

        ///  /???
        return Math.max(dp[m - 1][n - 1], 0);
    }



    // LC 980
    public int uniquePathsIII(int[][] grid) {

        return 0;
    }


    // LC 1920
    // 12.34 - 12.44 PM
    /**
     *  -> build an array ans of the same length where ans[i] = nums[nums[i]]
     *   for each 0 <= i < nums.length and return it.
     *
     *    nums: zero based arr
     *
     *
     *  --------------------
     *
     *
     *
     *  --------------------
     *
     *
     */
    public int[] buildArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return null;
        }
        int[] res = new int[nums.length];
        for(int i = 0; i < nums.length; i++){
            res[i] = nums[nums[i]];
        }

        return res;
    }


    // LC 1089
    // 12.43 - 53 PM
    /**
     *
     *  -> Given a fixed-length integer array arr,
     *     duplicate each occurrence of zero,
     *     shifting the remaining elements to the right.
     *
     *  --------------------
     *
     *   IDEA 1) 2 POINTER
     *
     *   IDEA 2) LIST ???
     *
     *
     *  --------------------
     */
    public void duplicateZeros(int[] arr) {
        // edge
        if(arr == null || arr.length == 0){
            return;
        }

        List<Integer> list = new ArrayList<>();
        for(int x: arr){
            System.out.println(">>> x = " + x +
                    ", x == 0 ?" + (x==0) );
            list.add(x);
            if(x == 0){
                list.add(0);
            }
        }

        arr = new int[list.size()]; // ???
        for(int i = 0; i < list.size(); i++){
            arr[i] = list.get(i);
        }
    }



    // LC 720
    // 13.02 - 12 pm
    /**
     *
     *  -> return the longest word in words that can
     *   be built one character at a time by other words in words.
     *
     *
     *   NOTE:
     *
     *    - If there is more than one possible answer,
     *        - return the longest word with the smallest lexicographical order.
     *
     *    - If there is no answer,
     *       - return the empty string.
     *
     *  ---------------------
     *
     *   IDEA 1) BRUTE FORCE
     *
     *     - loop over word in words,
     *     - check if the `word` can be built from remaining word
     *     - maintain the candidate on the same time:
     *        { len : [w1, w2, ...] }
     *     - get the longest word, if there is a tie
     *       , return the one with smallest lexicographical order
     *
     *   IDEA 2) TRIE + DFS ????
     *
     *
     *  ---------------------
     *
     */
    // IDEA 1) BRUTE FORCE
    public String longestWord(String[] words) {
        // edge
        if(words == null || words.length == 0){
            return ""; // ???
        }
        if(words.length == 1){
            return ""; // /??
        }

        // cache
        // map: { len : [w1, w2, .. ] }
        Map<Integer, List<String>> map = new HashMap<>();

        int maxLen = 0;

        for(String w: words){
            if(canBuilt(w, words)){
                int len = w.length();
                if(!map.containsKey(len)){
                    map.put(len, new ArrayList<>());
                }
                List<String> list = map.get(len);
                list.add(w);
                map.put(len, list);
                maxLen = Math.max(maxLen, len);
            }
        }

        List<String> list = map.get(maxLen);

        if(list.isEmpty()){
            return ""; // /??
        }


        // sort on lexicographical (small -> big)
        // ??
        // sort anyway
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // ????
                int diff = o1.compareTo(o2); // /???
                return diff;
            }
        });


        return list.get(0);
    }



    private boolean canBuilt(String word, String[] words){
        for(String w: words){
            if(!word.startsWith(w)){
                return false;
            }
        }

        return true;
    }









}
