package LCWeekly;

import java.util.*;

/**
 * LeetCode biweekly contest 345
 * https://leetcode.com/contest/weekly-contest-345/
 * 中文題目
 * https://leetcode.cn/contest/weekly-contest-345/
 */
public class Weekly345 {

    // Q1
    // LC 2682
    // https://leetcode.com/problems/find-the-losers-of-the-circular-game/
    // 10.40 - 50 am
    /**
     *
     *  -> Given the number of friends, n, and an integer k,
     *   return the `array answer`,
     *   which contains the losers of the game in the ascending order.
     *
     *
     *   -> return the arr with list of losers (small -> big)
     *
     *    - n people seat in circle, from 1 to n in clockwise order.
     *    - rules of the game
     *      - 1st person gets ball, then pass the ball to one who
     *        is k step away from the cur person
     *       - pass to 2 * k step away from the cur person
     *       - pass to 3 * k step away from the cur person
     *       .....
     *
     *   - NOTE: The game is FINISHED
     *          when some friend receives the
     *          ball for the SECOND time.
     *
     *    - The losers of the game are friends
     *      who DID NOT receive the ball in the ENTIRE game.
     *
     *
     *
     *  IDEA 1) SET + HASHMAP + math ?????
     *
     *  IDEA 2)
     *
     *
     *   exp 1)
     *   Input: n = 5, k = 2
     *   Output: [4,5]
     *
     *        1
     *     5     2
     *       4  3
     *
     *   exp 2)
     *   Input: n = 4, k = 4
     *   Output: [2,3,4]
     *
     *        1
     *     4     2
     *       3
     *
     *
     */
    public int[] circularGameLosers(int n, int k) {
        // edge
        if(n == 1){
            return new int[]{0}; // ???
        }
        // ???
        if(k == 0){
            return new int[]{}; // ???
        }

        Set<Integer> set = new HashSet<>();

        boolean isVisitTwice = false;
        int cnt = 1; // ???
        int idx = 1;
        //int ken = n;
        // ???
        while(isVisitTwice){
            int val = cnt * k;
            // adjust
            val = val % n; // /?
            idx += val;
            if(set.contains(idx)){
                isVisitTwice = true;
            }
            set.add(idx);
            cnt += 1;
        }

        List<Integer> list = new ArrayList<>();
        for(int i = 1; i < n; i++){
            if(!set.contains(i)){
                list.add(i);
            }
        }

        int[] res = new int[list.size()];
        for(int i = 0; i < list.size(); i++){
            res[i] = list.get(i);
        }

        return res; // ??? should NOT visit here???
    }



    // Q2
    // LC 2683
    // https://leetcode.com/problems/neighboring-bitwise-xor/description/
    // 11.02 - 11.12 AM
    /**
     *
     *  -> Return true if such an array exists or false otherwise.
     *
     *  - for index = i, (range [0, n - 1])
     *    - if i = n - 1
     *      - derived[i] = original[i] ⊕ original[0]
     *    - otherwise
     *      - derived[i] = original[i] ⊕ original[i + 1]
     *
     *
     */
    public boolean doesValidArrayExist(int[] derived) {
        // edge
        if(derived == null || derived.length == 0){
            return true;
        }

        //  int result = a ^ b; // Binary: 0110 (which is 6 in decimal)

        // cache as original
        int[] original = derived;

        // ??? op
        int n = derived.length;
        for(int i = 0; i < n; i++){
            // check if `can form as original`
            if(n > 0){
                if(isSameBySort(original, derived)){
                    return true;
                }
            }
            // case 1) i == n - 1
            if(i == n - 1){
                derived[i] = (original[i] ^ original[0]); // ???
            }
            // case 2) otherwise
            else{
                derived[i] = (original[i] ^ original[i + 1]); // ???
            }
        }

        return false;
    }

    private boolean isSameBySort(int[] original, int[] cur){
        // sort ??
        Arrays.sort(original);
        Arrays.sort(cur);

        //????
        if(original.length != cur.length){
            return false;
        }
        for(int i = 0; i < original.length; i++){
            if(original[i] != cur[i]){
                return false;
            }
        }

       // ???
       // return original.equals(cur);
        return true;
    }




    // Q3
    // LC 2684
    // https://leetcode.com/problems/maximum-number-of-moves-in-a-grid/description/
    // 11. 29 - 39 am
    /**
     *
     * -> Return the MAX number of MOVES that you can perform.
     *
     *  - 0-indexed m x n matrix grid
     *       - ALL positive int
     *
     *  - can start at ANY cell in the FIRST column,
     *       and traverse the grid in the following way:
     *           - from (row, col), cam move to ANY of below:
     *               - (row - 1, col + 1)
     *               - (row, col + 1)
     *               - (row + 1, col + 1)
     *               - BUT the new value be strictly bigger
     *                 than the value of the current cell.
     *
     *  -------
     *
     *    (MAX number of MOVES)
     *
     *
     *    IDEA 1) DFS ???
     *
     *    IDEA 2) BFS ???  -> always select the `min increase path` ????
     *
     *    IDEA 3) dijkstra ???
     *
     *    IDEA 4)  Floyd-Warshall ???
     *
     *    IDEA 5)  DP ?????
     *
     */

    // IDEA 3) dijkstra ???
    public int maxMoves(int[][] grid) {
        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid[0].length == 1 || grid.length == 1){
            return 1; // ???
        }


        int maxMove = 0;

        // PQ: {x, y, cost, moves}
        PriorityQueue<Integer[]> pq = new PriorityQueue<>(new Comparator<Integer[]>() {
            @Override
            public int compare(Integer[] o1, Integer[] o2) {
                int diff = o1[2] - o2[2];
                return diff;
            }
        });

        int l = grid.length;
        int w = grid[0].length;

        // ???
        //  can start at ANY cell in the FIRST column,
        for(int i = 0; i < l; i++){
            int x = 0;
            int y = i;
            // init cost = 0
            // init move = 0
            pq.add(new Integer[]{x, y, 0, 0});
        }

        // 2D array record cost by (x, y) ???
        // int init as 0 // /????
        int[][] costStaus = new int[l][w]; // ???
        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                // ?? init as max val
                // so we know that which grid (x,y)
                // is NOT visited yet
                costStaus[i][j] = Integer.MAX_VALUE; // ?????
            }
        }

        // (row - 1, col + 1), (row, col + 1) and (row + 1, col + 1)
        int[][] moves = new int[][]{ {-1,1}, {0,1}, {1,1} };

        // ?? dijkstra: BFS + PQ + cost status update ???
        while (!pq.isEmpty()){
            // edge: if even CAN'T move from 1st iteration ??

            Integer[] cur = pq.poll();
            int x = cur[0];
            int y = cur[1];
            int cost = cur[2];
            int movesCnt = cur[3];

            // ???
            maxMove = Math.max(maxMove, movesCnt);

            for(int[] m: moves){
                int x_ = x + m[1];
                int y_ = y + m[0];
                int cost_ = cost + m[2]; // ???
                // check 1) if still in grid boundary
                if(x_ >= 0 && x_ < w && y_ >= 0 && y < l){
                    // check 2) if next val is bigger than prev val
                    if(grid[y_][x_] > grid[y][x]){

                        //  check 3) check if `cost if less the cur status` // ???
                        if(grid[y_][x_] > grid[y][x] + cost_){
                            // add to PQ
                            pq.add(new Integer[]{x_, y_, cost_, movesCnt + 1});

                            // update cost status ???
                            // relation ???
                            grid[y_][x_] = cost_; // ???
                        }
                    }
                }
            }
        }

        return maxMove;
    }




    // Q4
    // LC 2685
    // https://leetcode.com/problems/count-the-number-of-complete-components/description/
    // 12.06 - 32 pm
    /**
     *
     * -> Return the number of complete connected
     *   components of the graph.
     *
     *   - integer n. There is an undirected graph with n vertices,
     *     numbered from 0 to n - 1
     *
     *  - 2 D array: edges[i] = [ai, bi]
     *      - n undirected edge connecting vertices ai and bi.
     *
     *   - NOTE !!!  connected component
     *       - connected component:
     *
     *          subgraph of a graph in which
     *          there exists a path between any two vertices,
     *          and NO vertex (頂點) of the subgraph shares
     *          an edge with a vertex outside of the subgraph.
     *
     *
     *   - A `connected` component is said to be COMPLETE if
     *      there exists an edge between every pair of its vertices.
     *
     *    - vertex: 頂點
     *
     *
     *
     *    IDEA 1) graph + dfs + check if cycled ????
     *      - loop over graph, and check if it's cycled
     *          - use set to check if is cycled
     *      - record the `number of NOT cycled` graph
     *      - return above as answer
     *
     *    IDEA 2) Quick Union ??? quick find ?? (graph algo)
     *
     *
     */
    public int countCompleteComponents(int n, int[][] edges) {
        // edge
        if(edges == null || edges.length == 0 || edges[0].length == 0 || n == 0){
            return 0;
        }
        if(edges.length == 1 || edges[0].length == 1){
            return 1; // ????
        }

        int completeNodeCnt = 0;

        // ??? build graph
        // { val : [neighbor_1, neighbor_2, ...] }
        Map<Integer, List<Integer>> graph = new HashMap<>();
        // init
        for(int i = 0; i < n; i++){
            graph.put(i, new ArrayList<>());
        }
        // add neighbors
        for(int[] e: edges){
            int start = e[0];
            int end = e[1];

            graph.get(start).add(end); // ???
            graph.get(end).add(start); // ???
        }

        System.out.println(">>> graph = " + graph);

        boolean[] visited = new boolean[n];

        // loop over n
        for(int i = 0; i < n; i++){
            //graph.put(i, new ArrayList<>());
            // ???
            if(isCycled(i, graph, visited, new HashSet<Integer>())){
                completeNodeCnt += 1;
            }
        }

        return completeNodeCnt;
    }

    private boolean isCycled(int node, Map<Integer, List<Integer>> graph, boolean[] visited, HashSet<Integer> set){
        // ??
        if(set.contains(node)){
            return false;
        }
        // mark as visited
        visited[node] = true;
        // update set
        set.add(node);

        // dfs call
        for(int i: graph.get(node)){
            if(!isCycled(i, graph, visited, set)){
                return false;
            }
        }

        return true;
    }





}
