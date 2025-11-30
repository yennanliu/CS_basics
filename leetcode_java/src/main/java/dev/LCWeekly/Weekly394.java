package dev.LCWeekly;

import java.util.*;

/**
 * LeetCode weekly contest 394
 * https://leetcode.com/contest/weekly-contest-394/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-394/
 *
 */
public class Weekly394 {

    // Q1
    // LC 3120
    // https://leetcode.com/problems/count-the-number-of-special-characters-i/
    // 15.10 - 20 pm
    /**
     *
     *  -> Return the `number` of `special letters` in `word.`
     *
     *   - word
     *   -  A letter is called special if
     *      it appears both in lowercase and uppercase in word.
     *
     *
     *   -------------
     *
     *  IDEA 1) HASHMAP OR SET
     *
     *
     */
    public int numberOfSpecialChars_1(String word) {
        // edge
        if(word == null || word.isEmpty()){
            return 0;
        }
        if(word.length() == 1){
            return 0;
        }
        //Set<String> set = new HashSet<>();
        // { val : [lower_idx, upper_idx]

        String lowerAlpha = "abcdefghijklmnopqrstuvwxyz";

        Map<Character, Integer[]> map = new HashMap<>();
        int cnt = 0;
        for(char ch: word.toCharArray()){
            if(!map.containsKey(ch)){
                map.put(ch, new Integer[2]);
            }
            String s = String.valueOf(ch);
            Integer[] arr = map.get(ch);
            // check if lower or upper
            if(lowerAlpha.contains(s)){
                arr[0] = 1;
            }else{
                arr[1] = 1;
            }
        }

        // check
        for(Integer[] x: map.values()){
            if(x[0] == 1 && x[1] == 1){
                cnt += 1;
            }
        }

        return cnt;
    }


    // Q2
    // LC 3121
    // https://leetcode.com/problems/count-the-number-of-special-characters-ii/
    // 15.15 - 25 pm
    /**
     *
     * -> Return the number of special letters in word.
     *
     *   - word
     *   - c: letter
     *   - is `special` if
     *      1) exists BOTH lower, upper case in `word`
     *      2) EVERY lower case c appears BEFORE
     *          the first UPPERCASE of c
     *
     *
     *  ---------------
     *
     *   IDEA 1)  HASHMAP + SLIDE WINDOW ???
     *
     *   // map: record all idx of occurrence of a letter c
     *
     *   // V1
     *   HASHMAP: { val : [idx1, idx2, ...] }
     *   -> and for each val,
     *       we check if it's special
     *
     *  // V2
     *   HASHMAP: { val : [ [lower_idx1, lower_idx2, ...], [upper_idx1, upper_idx2] ], ... }
     *
     *
     *  // V3
     *
     *  map_1: { val : [lower_idx1, lower_idx2, ...] }
     *
     *  map_2:  { val : [upper_idx1, upper_idx2, ....]  }
     *
     *
     *  -> and within build map1, map2,
     *     check if `EVERY lower case c appears BEFORE upper case`
     *     if NOT, quit the map build for that alphabet directly
     *
     *   ---------------
     *
     *
     *
     *
     */
    public int numberOfSpecialChars(String word) {
        // edge
        if(word == null || word.isEmpty()){
            return 0;
        }
        if(word.length() == 1){
            return 0;
        }

        // map_1: { val : [lower_idx1, lower_idx2, ...] }
        Map<Character, List<Integer>> map1 = new HashMap<>();
        // map_2:  { val : [upper_idx1, upper_idx2, ....]  }
        Map<Character, List<Integer>> map2 = new HashMap<>();

        String lowerAlpha = "abcdefghijklmnopqrstuvwxyz";

        // build map
        for(int i = 0; i < word.length(); i++){
            char ch = word.charAt(i);
            if(!map1.containsKey(ch)){
                map1.put(ch, new ArrayList<>());
            }

            if(!map2.containsKey(ch)){
                map2.put(ch, new ArrayList<>());
            }

            String s = String.valueOf(ch);

            // add idx
            if(lowerAlpha.contains(s)){
                map1.get(ch).add(i);
            }else{
                map2.get(ch).add(i);
            }
        }

        System.out.println(">>> map1 = " + map1 +
                ", map2 = " + map2);

        int cnt = 0;
        for(char ch: map1.keySet()){
            if(map1.get(ch).isEmpty()){
                continue;
            }
            if(!map2.containsKey(ch) || map2.get(ch).isEmpty()){
                continue;
            }
            /**  // validate if is `special` */
            // check if `EVERY lower case c appears BEFORE upper case`
            // since we go through word element in order
            // e.g. idx start from small -> big
            // the collected idx in map
            // SHOULD be in INCREASING order as well (small->big)
            // -> so ALL we need to check is:
            // check if `last idx in map1` <   `1st idx in map2`
            // ???
            int map1IdxSize =  map1.get(ch).size();
            //int map2IdxSize =  map2.get(ch).size();
            if(map1.get(ch).get(map1IdxSize - 1) < map2.get(ch).get(0)){
                cnt += 1;
            }

        }

        return cnt;
    }




    // Q3
    // LC 3122
    // https://leetcode.com/problems/minimum-number-of-operations-to-satisfy-conditions/
    // 15.56 - 16.18 pm
    /**
     *  -> Return the `minimum` number
     *     of operations needed.
     *
     *    - m x n matrix
     *
     *
     *   - NEED to do the op if:
     *
     *   -  Equals to the cell BELOW
     *      - e.g. grid[i][j] == grid[i + 1][j]
     *          - (if existed)
     *
     *   - Different from the cell at RIGHT
     *      - e.g. grid[i][j] != grid[i][j + 1]
     *          - (if existed)
     *
     *
     * -----------------
     *
     *   IDEA 1) dp ?????,
     *
     *  IDEA 2)  brute force ???
     *
     *
     * -----------------
     *
     *  ex 1)
     *   Input: grid = [[1,1,1],[0,0,0]]
     *   Output: 3
     *
     *  ->
     *
     *  [
     *   [1,1,1],
     *  [0,0,0]
     *  ]
     *
     *  ->
     *
     *   [
     *    [1,1,1],
     *   [1,0,0]
     *   ]
     *
     *
     *
     *
     */
    // IDEA 2)  brute force ???
    public int minimumOperations(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid.length == 1 && grid[0].length == 1){
            return 0;
        }

        int cnt = 0;

        int l = grid.length;
        int w = grid[0].length;
        /**
         *      *
         *      *   - NEED to do the op if:
         *      *
         *      *   -  Equals to the cell BELOW
         *      *      - e.g. grid[i][j] == grid[i + 1][j]
         *      *          - (if existed)
         *      *
         *      *   - Different from the cell at RIGHT
         *      *      - e.g. grid[i][j] != grid[i][j + 1]
         *      *          - (if existed)
         *
         */
        for(int y = 0; y < l; y++){
            for(int x = 0; x < w; x++){

                boolean condition1 = false;
                boolean condition2 = false;

                // step 1) check `grid[i][j] == grid[i + 1][j]`
                if(y + 1 < l){
                    if(grid[y][x] == grid[y + 1][x]){
                        condition1 = true;
                    }
                }

                // step 2) check `grid[i][j] != grid[i][j + 1]`
                if(x + 1 < w){
                    if(grid[y][x] != grid[y][x + 1]){
                        condition2 = true;
                    }
                }

                // if BOTH condition1 and condition2
                // do the op
                if(condition1 && condition2){

                    // ??? TODO: check what op to do
                    //           can minimize the total op

                    cnt += 1;
                }

            }
        }


        return cnt;
    }


    // Q4
    // LC 3123
    // https://leetcode.com/problems/find-edges-in-shortest-paths/description/
    // 16.20 - 52 pm
    /**
     *   -> Return the array answer.
     *
     *   - n nodes, from 0 to n -1
     *   - m edges
     *   - edges[i] = [ai, bi, wi]
     *      - ai: start node
     *      - bi: end node
     *      - wi: weight
     *
     *   - find the `boolean arr` array
     *     - array[i] == true
     *         - edges[i] is part of the at least one shortest path
     *     - otherwise, false
     *
     *
     *
     *  ------------------------
     *
     *   IDEA 1) Dijkstra's algo ????
     *     -> find ALL OF the shortest path (node `0` -> node `n-1`)
     *     and check if the point (0 = n- 1)
     *     is in the path ???
     *
     *
     *   IDEA 2)  BFS with wight ??
     *      -> simply collect all paths (with cost)
     *      and get the mini cost
     *      and get the paths with same cost
     *      then check if node 0 - node n - 1
     *      exists AT LEAST ONCE in the paths
     *
     *   IDEA 3) Bellman-Ford algo ???
     *
     *
     *  IDEA 4) BFS
     *
     *
     *
     *
     *
     *  ------------------------
     *
     */
    // IDEA 2)  BFS with wight ?? ???
    public boolean[] findAnswer(int n, int[][] edges) {
        // edge

        // map : build neighbors
        Map<Integer, List<Integer>> neighborMap = new HashMap<>();
        for(int[] e: edges){
            int start = e[0];
            int end = e[1];
            //int cost = e[2];

            // ???
            neighborMap.get(start).add(end);
            neighborMap.get(end).add(start);
        }


        // map:
        // { path : cost }
        // { node1-node-2-....node-k: cost-1, ...}
        Map<String, Integer> pathMap = new HashMap<>();


        // bfs get paths ???
        // queue: [ [ node, cost_till_now, path] ] // ???
        // ???
        Queue<String[]> q = new LinkedList<>();
        /** NOTE !!
         *
         *  target: node `0` -> node `n-1`
         */
        q.add(new String[]{"0", "0", ""});

        while(!q.isEmpty()){
            int size = q.size();
            for(int i = 0; i < size; i++){
                // ??
                String[] cur = q.poll();
                int node = Integer.parseInt(cur[0]);
                int cost = Integer.parseInt(cur[1]);
                String path = cur[2];

                // NOTE !!! if reach the destination (e.g. `n-1` node)
                if(node == n - 1){
                    pathMap.put(path, cost);
                }

                for(int next: neighborMap.get(node)){
                    // ??? ???
                    q.add(new String[]{String.valueOf(next), "0", path + "-"});
                }
            }
        }

        int minCost = Integer.MAX_VALUE;
        for(int x: pathMap.values()){
            minCost = Math.min(minCost, x);
        }

        List<String> minCostPaths = new ArrayList<>();
        for(String path: pathMap.keySet()){
            if(pathMap.get(path) == minCost){
                minCostPaths.add(path);
            }
        }

        boolean[] res = new boolean[edges.length];
        for(String p: minCostPaths){
            String[] list = p.split("-");
            // ???
            for(String x: list){
                int node = Integer.parseInt(x);
                res[node] = true;
            }
        }

        return res;
    }


}
