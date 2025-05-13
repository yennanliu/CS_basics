package dev;

import LeetCodeJava.BFS.NetworkDelayTime;
import LeetCodeJava.BFS.WordLadder;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.DynamicProgramming.CoinChange;

import java.util.*;

public class workspace10 {


    // LC 399
    // 11.31 - 11. 47 am
    /**
     *  IDEA 1) DFS
     *
     *  IDEA 2) BFS ???
     *
     *  IDEA 3) UNION FIND ???
     *
     *
     *  ----------------------
     *
     *  IDEA: DFS
     *
     *
     * Example 1):
     *  Given a / b = 2.0, b / c = 3.0.
     *  queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ? .
     *  return [6.0, 0.5, -1.0, 1.0, -1.0 ].
     *
     *  ->
     *
     *
     */
    // 12.04 - 12.14 pm
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {

        double[] res = new double[queries.size()];

        // edge
        if(queries.isEmpty() || queries.size() == 0){
            return null;
        }
        if(equations == null || equations.size() == 0){
            Arrays.fill(res, -1.0);
            return res;
        }

        // build map
        Map<String, Double> valMap = new HashMap<>();
        for(int i = 0; i < equations.size(); i++){

            List<String> eq = equations.get(i);
            String var_1 = eq.get(0);
            String var_2 = eq.get(1);
            double val = values[i];

            valMap.put(var_1 + " / " + var_2, val);
            valMap.put(var_2 + " / " + var_1, 1 / val);
        }

        List<Double> cache = new ArrayList<>();

        // dfs
        for(List<String> q: queries){
            double tmp = 1.0;
            calHelper(q, valMap, tmp);
            cache.add(tmp);
        }

        return res;
    }

    public Double calHelper(List<String> query, Map<String, Double> valMap, double tmp){

        String var_1 = query.get(0);
        String var_2 = query.get(1);
        if(!valMap.containsKey(var_1) || !valMap.containsKey(var_2)){
            return -1.0;
        }
        if (var_1.equals(var_2)) {
            return 1.0;
        }

        // dfs call
//        if(valMap.containsKey(var_1)){
//            for(Str)
//        }

        return tmp;
    }

    // LC 310
    // 12.46 - 12.56 pm
    /**
     *  -> Given such a graph, write a function
     *   to find all the `MHTs` and return a `list of their root labels`.
     *
     *
     *   MHTs:  minimum height trees (MHTs)
     *
     *
     *  IDEA 1) DFS ?? build tree recursively
     *
     *    -> choose `every node`, build the tree per edges
     *    -> maintain the node and height
     *    ...
     *    -> return lowest height
     *
     *
     *   IDEA 2) BFS ->
     *           try every node, use node as root, and build graph, once reach the `end`, get cur height as MST,
     *           then do next node....
     *           maintain the `min height` at the same time
     *
     *           return min height as res
     */
    /**
     *  IDEA 1) BRUTE FORCE:
     *     -> loop over every node,
     *        build tree accordingly,
     *        check the `min height`
     *
     *     -> NOT efficient, time complexity O(n * n) ????
     *
     *
     *  IDEA 2)  `observation` -> `quick union`
     *      -> find the nodes has `most connection`
     *
     *      -> the `min` depth happened
     *        when choose a node as root
     *        which has `most` connection to other nodes
     *
     *        -> so it `most unlikely` to form a `height`
     *        -> so it has `lowest` height
     *
     *  IDEA 3)  topological sort
     */
    // 8.35 - 8.45 pm
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

        // edge case
        // topological sort
        MyTopo6 myTopo6 = new MyTopo6(n, edges);
        List<Integer> res = myTopo6.sort();

        return null;
    }

    public class MyTopo6{
        int n;
        int[][] edges;
        public  MyTopo6(int n, int[][] edges){
            this.n = n;
            this.edges = edges;
        }

        public List<Integer> sort(){
            return null;
        }
    }

//    // LC 310
//    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
//
//    }

    // LC 127
    // 3.46 - 3.56 pm
    /**
     *  ->  return the number of words in the shortest transformation
     *      sequence from beginWord to endWord, or 0
     *
     *  -> return `lowest` cnt that transform start -> end word
     *
     *
     * -> IDEA: BFS
     *
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
         // edge
        if(!wordList.contains(endWord)){
            return 0;
        }
        if(beginWord.equals(endWord) && wordList.contains(endWord)){
            return 0;
        }

        HashSet<String> visited = new HashSet<>();

        Queue<WordState> q = new LinkedList<>();
        StringBuilder _sb = new StringBuilder();
        _sb.append(beginWord);
        q.add(new WordState(0, _sb));

        String alpha = "abcdefghijklmnopqrstuvwxyz";

        // BFS
        while(!q.isEmpty()){

            WordState ws = q.poll();
            StringBuilder _sb2 = ws.sb;
            String word = _sb2.toString();
            int cnt = ws.cnt;

            if(word.equals(endWord)){
                return cnt;
            }
            // NOTE !!! we validate below first
            if(!wordList.contains(word) || visited.contains(word)){
                continue;
            }

            visited.add(word);


            // move on every idx, every 26 alphabet ???
            for(int i = 0; i < word.length(); i++){
                // update sb
                for(String x: alpha.split("")){
                    _sb2.replace(i, i + 1, x);
                    q.add(new WordState(cnt + 1, _sb2));
                }
            }
        }

        return 0;
    }

    public class WordState{
        int cnt;
        StringBuilder sb;

        public WordState(int cnt, StringBuilder sb){
            this.cnt = cnt;
            this.sb = sb;
        }
    }

    // LC 126
    // 4.39 pm - 4.49 pm
    /**
     * Given two words, beginWord and endWord, and a dictionary wordList,
     * return `all` the `shortest` transformation sequences from beginWord to endWord,
     * or an empty list if no such sequence exists. Each sequence
     * should be returned as a list of the words [beginWord, s1, s2, ..., sk].
     *
     *
     * -> return all `shortest` path of the start -> end transformation,
     *   return null if can't find one
     *
     *
     *   // IDEA: BFS
     *
     *   -> not `stop` when find a solution, keep finding
     *      till all `path` are processed ???
     *
     *
     */
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {

        List<List<String>> res = new ArrayList<>();

        // edge
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)){
             return res;
        }

        // LC 127
        Queue<WordState_> q = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        q.add(new WordState_(beginWord, 1, new ArrayList<>()));
        visited.add(beginWord);

        while (!q.isEmpty()) {
            WordState_ ws = q.poll();
            String word = ws.word;
            int cnt = ws.count;
            List<String> history = ws.history;

            history.add(beginWord);

            if (word.equals(endWord)){
                // update res
                res.add(new ArrayList<>(history)); // ???
                continue;
            }

            for (int i = 0; i < word.length(); i++) {
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == word.charAt(i)){
                        continue;
                    }

                    /** NOTE !!! below */
                    StringBuilder sb = new StringBuilder(word);
                    /** NOTE !!! StringBuilder can update val per idx */
                    sb.setCharAt(i, c);
                    String nextWord = sb.toString();

                    if (wordSet.contains(nextWord) && !visited.contains(nextWord)) {
                        visited.add(nextWord);
                        history.add(nextWord);
                        q.add(new WordState_(nextWord, cnt + 1, history));
                    }
                }
            }
        }

        // final check: ONLY get the `path` with shortest length

        return res;
    }


    private static class WordState_ {
        String word;
        int count;
        List<String> history;

        WordState_(String word, int count, List<String> history) {
            this.word = word;
            this.count = count;
            this.history = history;
        }
    }

    // LC 1631
    // 7.40 - 7.50 pm
    /**
     *  IDEA 3) Dijkstra
     *
     *  A route's effort is the `maximum absolute
     *  difference` in heights between two consecutive cells of the route.
     *
     *   effort = max ( abs(height_1 height_2 ) )
     */
    // Dijkstra algo
    public int minimumEffortPath(int[][] heights) {

        // edge
        if(heights == null || heights.length == 0){
            return 0;
        }

        int l = heights.length;
        int w = heights[0].length;

        // Dijkstra : min PQ + BFS

        // min PQ : { [ [ x, y, cost_till_now ] }
        // sort on `cost_till_now` (small -> big)
        PriorityQueue<int[]> min_pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[2] - o2[2];
                return diff;
            }
        });

        //HashSet<int[]> visited = new HashSet<>();
        boolean[][] visited = new boolean[l][w];

        // moves
        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        while(!min_pq.isEmpty()){
            int[] cur = min_pq.poll();
            int x_ = cur[0];
            int y_ = cur[1];
            int cost_ = cur[2];

            if(x_ == w - 1 && y_ == l - 1){
                return cost_;
            }

            // NOTE !!! we validate below
            if(x_ < 0 || x_ >= w || y_ <= 0 || y_ >= l || visited[y_][x_]){
                continue;
            }

           // visited[y_][x_] = true;

            for(int[] dir: dirs){

                int x_new = x_ + dir[1];
                int y_new = y_ + dir[1];

                if(x_new < 0 || x_new >= w || y_new <= 0 || y_new >= l){
                    continue;
                }

                int max_abs_diff = Math.max(cost_, Math.abs(  heights[y_new][x_new] -  heights[y_][x_] ));

                min_pq.add(new int[] { x_new, y_new, max_abs_diff } );
            }

        }

        return -1; // ?? if can't find a solution
    }

    // LC 743
    // 10.39 - 10.49 am
    /**
     *
     * We will send a signal from a given node k. Return the minimum time it takes for all the n nodes to receive the signal.
     * If it is impossible for all the n nodes to receive the signal, return -1.
     *
     *
     *   times[i] = (ui, vi, wi), where ui is the source node, vi is the target node,
     *   and wi is the time it takes for a signal
     *   to travel from source to target.
     *
     *
     *  -> n: total has n nodes ( 1 to n)
     *  -> k: msg sent from `k` node
     *
     *  -> times[i] = (ui, vi, wi)
     *     ui: src node
     *     vi: target node
     *     wi: processing time
     *
     * -> Return the `minimum` time it takes for all the n nodes,
     *    if not possible, return -1
     *
     *
     *  IDEA 1) Dijkstra algo
     */
    public int networkDelayTime(int[][] times, int n, int k) {

        // Edge case: If no nodes exist
        if (times == null || times.length == 0 || n == 0) {
            return -1;
        }
        if (n == 1) {
            return 0; // If there's only one node, no travel time is needed.
        }

        MyDijkstra dijkstra = new MyDijkstra(times, n);
        return dijkstra.getShortestPath(k);
    }

    public class MyDijkstra{

        int[][] times;
        int n;
        public MyDijkstra(int[][] times, int n){
            this.n = n;
            this.times = times;
        }

        // Dijkstra
        public int getShortestPath(int k){

            // build graph
            // { val : {neighbor_1, neighbor_2, .... } }
            Map<Integer, List<Integer>> pathMap = new HashMap<>();
            for(int[] t: this.times){
                int src = t[0];
                int dest = t[1];
                List<Integer> list = pathMap.getOrDefault(src, new ArrayList<>());
                list.add(dest);
            }

            // min pq : {src, dest, process_time}
            // sort on `process_time`
            PriorityQueue<List<Integer>> minPQ = new PriorityQueue<>(new Comparator<List<Integer>>() {
                @Override
                public int compare(List<Integer> o1, List<Integer> o2) {
                    int diff = o1.get(2) - o2.get(2);
                    return diff;
                }
            });

            // queue :  {src, dest, process_time}
            Queue<List<Integer>> q = new LinkedList<>();
            HashSet<Integer> visited = new HashSet<>();
            int res = -1;

            while(!q.isEmpty()){

                List<Integer> cur = q.poll();
                int _src = cur.get(0);
                int _dest = cur.get(1);
                int _process_time = cur.get(2);

                if(visited.size() == n){
                    return _process_time;
                }

                if(visited.contains(_src)){
                    continue;
                }

                visited.add(_src);
                // check node's neighbor and find the one same as min heap pop
                // e.g. move to the `next node` that has `min process time`
//                while(!pathMap.keySet().contains(minPQ.peek().get(0))){
//                    minPQ.poll();
//                }
//                // if found one
//                if(!minPQ.isEmpty()){
//                    int nextNode = minPQ.poll().get(0);
//                    q.add(pathMap.get(nextNode));
//                }
                for(Integer next: pathMap.get(_src)){
                    if(next == (minPQ.peek().get(0))){
                        q.add(pathMap.get(next));
                    }
                }

                res = _process_time;
            }

            return visited.size() == n ? res : -1;
        }
    }

//    public int networkDelayTime(int[][] times, int n, int k) {
//        // edge
//        if(times == null || times.length == 0){
//            return -1;
//        }
//        if(times.length == 1){
//            return times[0][2];
//        }
//
//        // Dijkstra:
//        // min heap + bfs
//
//        // min PQ: return the `min` processing time every time
//        PriorityQueue<NodeInfo> minPQ = new PriorityQueue<>(new Comparator<NodeInfo>() {
//            @Override
//            public int compare(NodeInfo o1, NodeInfo o2) {
//                int diff = o1.processTime - o2.processTime;
//                return diff;
//            }
//        });
//
//        // bfs
//        Queue<ProcessInfo> q = new LinkedList<>();
//        q.add(new ProcessInfo(k, 0));
//
//        while(!q.isEmpty()){
//            ProcessInfo info = q.poll();
//            int totalProcessTime = info.totalProcessTime;
//            int node = info.node;
//
//            // if `all nodes are visited`
//            if(node == n){
//                return totalProcessTime;
//            }
//        }
//
//
//        return 0;
//    }
//
//    public class ProcessInfo{
//        int node;
//        int totalProcessTime;
//
//        public ProcessInfo(int node, int totalProcessTime){
//            this.node = node;
//            this.totalProcessTime = totalProcessTime;
//        }
//    }
//
//    public class NodeInfo{
//        int src;
//        int dest;
//        int processTime;
//
//        public NodeInfo(int src, int dest, int processTime){
//            this.src = src;
//            this.dest = dest;
//            this.processTime = processTime;
//        }
//    }

    // LC 332
    // 11.33 - 11.43 am
    public List<String> findItinerary(List<List<String>> tickets) {

        return null;
    }

    // LC 502
    // 11.56 - 12.16 pm
    /**
     *  1. max PQ on profit
     *  2. collect all `profit` to queue
     *     than can `afford` ( capital_i <= current money)
     *
     *  3. pop cur max profit, update res
     *  4. loop above, return final res
     *
     *
     *
     *
     */
    /**
     *  IDEA:  PQ
     *
     *  -> 2 PQ
     *
     *  -> 1. small PQ : small capital PQ
     *  -> 2. big PQ : big profits PQ
     *
     *
     */
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        // edge
        if(k == 0 || profits == null || profits.length == 0 || capital == null || capital.length == 0){
            return 0;
        }

        // init PQ
        // small PQ : PQ : { [capital, profit], .... }
        // order by `capital` (small -> big)
        PriorityQueue<int[]> capital_pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[0] - o2[1];
                return diff;
            }
        });

        // big PQ : PQ : {profit_1, profit_2, ..}
        // order by `profit` (big -> small)
        PriorityQueue<Integer> profit_pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        //int res = 0;
        // NOTE !!! we all all capital - profit to capital_pq first
        for(int i = 0; i < capital.length; i++){
            capital_pq.add(new int[] { capital[i], profits[i] });
        }

        // NOTE !!! below, we ONLY operate `k` steps
        for(int i = 0; i < k; i++){

            // NOTE !!! below
            if(!capital_pq.isEmpty() && capital_pq.peek()[0] <= w){
                // add to profit PQ
                profit_pq.add(capital_pq.poll()[1]);
            }

            // ???? if `profit_pq` is empty, early quit
            if(profit_pq.isEmpty()){
                break;
            }

            // ???
            w += profit_pq.poll();
        }

        return w;
    }

//    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
//
//        // edge
//        if(profits == null || profits.length == 0){
//            return 0;
//        }
//        // can't even get the 1st capital
//        boolean foundOne = false;
//        for(int c: capital){
//            if (c <= w){
//                foundOne = true;
//                break;
//            }
//        }
//        if(!foundOne){
//            return 0;
//        }
//
//        // PQ : { [profit, capital] }
//        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
//            @Override
//            public int compare(int[] a, int[] b) {
//                int diff = b[0] - a[0];
//                return diff;
//            }
//        });
//
//        // record visited idx
//        HashSet<Integer> visited = new HashSet<>();
//
//        Queue<Integer> q = new LinkedList<>();
//        int curCaptital = w;
//
//        for(int i = 0; i < capital.length; i++){
//
//            if(!visited.contains(i)){
//                if(pq.isEmpty()){
//                    if(curCaptital >= capital[i]){
//                        q.add(profits[i]);
//                    }
//                }
//
//
//            }
//
//
//        }
//
//        int maxProfit = 0;
//
//
//        return maxProfit;
//    }

  // LC 1584
  // 8.17 - 8.27 pm
  /**
   * The cost of connecting two points [xi, yi] and [xj, yj]
   * is the manhattan distance between them: |xi - xj| + |yi - yj|,
   * where |val| denotes the absolute value of val.
   *
   * -> Return the minimum cost to make all points connected.
   * All points are connected if there is exactly
   * one simple path between any two points.
   *
   *
   *
   */
  public int minCostConnectPoints(int[][] points) {

        return 0;
    }

    // LC 778
    // 8.24 - 8.34 pm
    /**
     *
     *
     * -> Return the least time until you can reach the bottom right square (n - 1, n - 1)
     * if you start at the top left square (0, 0).
     *
     *
     *  IDEA 1) BFS
     *
     *  IDEA 2) DFS
     *
     *
     *  IDEA 3) Dijkstra
     *
     */
    // Dijkstra algo
    // 12.46 - 12.56 PM
    /**
     *
     *
     *
     */
    public int swimInWater(int[][] grid) {

        return 0;
    }
//    public int swimInWater(int[][] grid) {
//
//        // edge
//        if(grid == null || grid.length == 0){
//            return 0;
//        }
//
//        // MIN HEAP + BFS
//        // min_pq : [ cost, x, y ]
//        PriorityQueue<int[]> minHeap = new PriorityQueue<>(new Comparator<int[]>() {
//            @Override
//            public int compare(int[] o1, int[] o2) {
//                int diff = o1[0] - o2[0];
//                return diff;
//            }
//        });
//
//        Queue<int[]> q = new LinkedList<>();
//        q.add(new int[] {0, 0, 0});
//
//        int res = 0;
//
//        int l = grid.length;
//        int w = grid[0].length;
//
//        // visited : [x,y]
//        //HashSet<int[]> visited = new HashSet<>();
//        boolean[][] visited = new boolean[l-1][w-1];
//
//        while(!q.isEmpty()){
//
//            int[] cur = q.poll();
//            int cost = cur[0];
//            int x = cur[1];
//            int y = cur[2];
//
//            // update res
//            res += cost;
//
//            if(x == w - 1 && y == l - 1){
//                return res;
//            }
//
//            // NOTE !!! we do below validation
//            if (x < 0 || x >= w || y < 0 || y >= l || visited[y][x]){
//                continue;
//            }
//
//            // move on 4 directions and update t
//            int[][] moves = new int[][]{ {0,1}, {0,-1}, {1,0}, {-1,0} };
//
//            for(int[] m : moves){
//                int x_ = x + m[0];
//                int y_ = y + m[1];
//                if (x_ < 0 || x_ >= w || y_ < 0 || y_ >= l || visited[y_][x_]){
//                    continue;
//                }
//                int cost_ = grid[x_][y_];
//                minHeap.add(new int[] {cost_, x_, y_});
//            }
//
//            // pop the `min` path
//            int[] next = minHeap.poll();
//            q.add(next);
//        }
//
//      return res;
//    }

    // LC 269
    // 11.43 - 11. 53 AM
    /**
     *
     *  IDEA 1) DFS
     *
     *  IDEA 2) TOPOLOGICAL SORT ???
     *
     */
    // TOPOLOGICAL sort : `degrees`, next map, + BFS
    public String alienOrder(String[] words){

        // edge
        if(words == null || words.length == 0){
            return null;
        }

        //String res = "";
        StringBuilder sb = new StringBuilder();

        HashSet<String> set = new HashSet<>();
        for(String w: words){
            for(String x: w.split("")){
                set.add(x);
            }
        }

        // unique alphabet count
        //int n = set.size();

        // degrees
        // NOTE !!! instead of using `array`, we use map
        //int[] degrees = new int[n]; // init val as 0 ???
        Map<String, Integer> degrees = new HashMap<>();

        // nextMap : { val : [next_val_1, next_val_2, ...]}
        Map<String, HashSet<String>> nextMap = new HashMap<>();
        for(int i = 0; i < words.length; i++){
            // String w: words
            String w = words[i];
            String k = String.valueOf(w.charAt(0));
            HashSet<String> _list = nextMap.getOrDefault(k, new HashSet<>());

            for(int j = 1; j < w.length(); j++){
                //set.add(x);
                _list.add(String.valueOf(w.charAt(j)));
            }

            nextMap.put(k, _list);

            // update degrees
            // since it's `next map`, so we need to update `prev` node
            //degrees[i] += 1;
            degrees.put(k, degrees.get(k) + 1);
        }

        Queue<String> q = new LinkedList<>();

        // add `zero order` to queue
        for(String k: degrees.keySet()){
            if(degrees.get(k) == 0){
                q.add(k);
            }
        }

        while(!q.isEmpty()){

            String cur = q.poll();
            sb.append(cur);

            // find its `next nodes`
            if(nextMap.containsKey(cur)){
                for(String _next : nextMap.get(cur)){
                   //degrees[_next] -= 1; // ???
                    degrees.put(_next, degrees.get(_next) - 1);
                    // ???
                    if(degrees.get(_next) == 0){
                        q.add(_next);
                    }
                }
            }

        }

        // reverse ????
        return sb.reverse().toString();
    }


    // LC 787
    // 10.13 - 10.23 am
    /**
     *
     * IDEA 1: Dijkstra
     *
     *  edges = [[0,1,100],[1,2,100],[0,2,500]]
     *    [src, dest, cost]
     *
     */

    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {

        // edge
        if(n == 0){
            return 0;
        }
        if(flights == null || flights.length == 0){
            return 0;
        }
        if(src == dst || k == 0){
            return 0;
        }

        // Dijkstra : min heap + BFS

        int res = 0;
        int step = 0;

        HashSet<Integer> visited = new HashSet<>();

        // build `neighbor` map
        // idx : idx in `flights` array
        // { val : [idx_1, ...] }
        Map<Integer, List<Integer>> neighborMap = new HashMap<>();
        for(int i = 0; i < flights.length; i++){
            int[] f = flights[i];
            List<Integer> list = neighborMap.getOrDefault(f[0], new ArrayList<>());
            list.add(i);
            neighborMap.put(f[0], list);
        }


        // min PQ : sort on `cost`
        //  pq : { [src, dest, cost], [], ... }
        PriorityQueue<List<Integer>> min_pq = new PriorityQueue<>(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                int diff = o1.get(2) - o2.get(2);
                return diff;
            }
        });

        //Queue<List<Integer>> q = new LinkedList<>();
        while(!min_pq.isEmpty()){

            List<Integer> cur = min_pq.poll(); // ???
            int _src = cur.get(0);
            int _dest = cur.get(1);
            int _cost = cur.get(2);

            if(step == k && _dest == dst){
                return _cost;
            }

//            if(visited.contains(_src)){
//                continue;
//            }

            if(neighborMap.containsKey(_src)){
                for(Integer idx: neighborMap.get(_src)){
                    int[] flight = flights[idx];
                    List<Integer> _next = new ArrayList<>();
//                    for(int j = 0; j < flight.length; j++){
//                        _next.add(flight[j]);
//                    }
                    _next.add(flight[0]);
                    _next.add(flight[1]);
                    _next.add(flight[2] + _cost);

                    min_pq.add(_next);
                }
            }

            step += 1;
        }

        return -1; // /??
    }

    // LC 1489
    // 11.11 - 11.21 AM
    public List<List<Integer>> findCriticalAndPseudoCriticalEdges(int n, int[][] edges) {

        return null;
    }

    // LC 2392
    // 11.27 - 11.37 am
    public int[][] buildMatrix(int k, int[][] rowConditions, int[][] colConditions) {

        return null;
    }

    // LC 2709
    // 9.58 - 10.08 am
    /**
     *
     *   You are given a 0-indexed integer array nums, and you are allowed
     *   to traverse between its indices. You can traverse
     *   between index i and index j, i != j,
     *   if and only if gcd(nums[i], nums[j]) > 1,
     *   where gcd is the greatest common divisor.
     *
     *   -> Return true if it is possible to traverse between all such pairs of indices,
     *   or false otherwise.
     *
     *
     */
    public boolean canTraverseAllPairs(int[] nums) {

        return false;
    }

    // LC 1137
    // 10.31 - 10.41 am
    public int tribonacci(int n) {
        if (n == 0)
            return 0;
        if (n == 1 || n == 2)
            return 1;

        // NOTE !!! below, array size is `n + 1`
//        int[] dp = new int[n + 1];
//        dp[0] = 0;
//        dp[1] = 1;
//        dp[2] = 1;
        Map<Integer, Integer> cache = new HashMap<>();
        cache.put(0, 0);
        cache.put(1, 1);
        cache.put(2, 1);

        // NOTE !!! below, we loop from i = 3 to `i <= n`
        for (int i = 3; i <= n; i++) {
            int val = cache.get(i-1) + cache.get(i-2) + cache.get(i-3);
            cache.put(i, val);
        }

        return cache.get(n);
    }

//    public int tribonacci(int n) {
//        // edge
//        if(n == 0){
//            return 0;
//        }
//        if(n == 1){
//            return 1;
//        }
//        if(n == 2){
//            return 1;
//        }
//        if(n == 3){
//            return 2;
//        }
//
//        // dp
//        int[] dp = new int[n+1];
//        for(int i = 4; i < n; i++){
//            dp[n] = dp[n-3] + dp[n-2] + dp[n-1];
//        }
//
//        // Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
//        //return tribonacci(n-3) + tribonacci(n-2) + tribonacci(n-1);
//        return dp[n-1]; // ???
//    }


//    public int tribonacci(int n) {
//        // edge
//        if(n == 0){
//            return 0;
//        }
//        if(n == 1){
//            return 1;
//        }
//        if(n == 2){
//            return 1;
//        }
//        if(n == 3){
//            return 2;
//        }
//
//        // cache
//        // n : tribonacci res
//        Map<Integer, Integer> cache = new HashMap<>();
//        if(!cache.containsKey(n)){
//            cache.put(n, tribonacci(n));
//        }else{
//            return cache.get(n);
//        }
//
//        // Tn+3 = Tn + Tn+1 + Tn+2 for n >= 0.
//        return tribonacci(n-3) + tribonacci(n-2) + tribonacci(n-1);
//    }

    // LC 647
    // 10.53 - 11.07 am
    /**
     *  IDEA 1) BACKTRACK + `Palindromic check`
     *
     *  IDEA 2) DP ???
     *
     *  IDEA 3) 2 pointers ???
     *
     *
     *
     * -> IDEA 3) 2 pointers ???
     */
    // time complexity : O(N ^ 2)
//    public int countSubstrings(String s) {
//        // edge
//        if(s == null || s.length() == 0){
//            return 0;
//        }
//        if(s.length() == 1){
//            return 1;
//        }
//        if(s.length() == 2){
//            if(s.charAt(0) == s.charAt(1)){
//                return 3;
//            }
//            return 2;
//        }
//
//        int res = 0;
//
//        for(int i = 0; i < s.length(); i++){
//            for(int j = 0; j <= s.length(); j++){
//                String sub = s.substring(i, j);
//                if(isPalindromic(sub)){
//                    res += 1;
//                }
//            }
//        }
//
//        return res;
//    }

    public int countSubstrings(String s) {

        // edge
        if(s == null || s.length() == 0){
            return 0;
        }
        if(s.length() == 1){
            return 1;
        }
        if(s.length() == 2){
            if(s.charAt(0) == s.charAt(1)){
                return 3;
            }
            return 2;
        }

        int res = 0;

        // handle odd, even length case
        for(int i = 0; i < s.length(); i++){
            int v1 = isPalindromic2(s, 0, i);
            int v2 = isPalindromic2(s, 0, i);

            res += (v1 + v2);
        }

        return res;
    }

    public int isPalindromic2(String input, int l, int r){
        if(input == null || input.length() == 0){
            return 0;
        }
//        int l = 0;
//        int r = input.length() - 1;
        int res = 0;
        while(r > l){
            if(input.charAt(r) == input.charAt(l)){
                //return res ;
                res += 1;
            }

            r -= 1;
            l += 1;
        }

        return res;
    }

    public boolean isPalindromic(String input){
        if(input == null || input.length() == 0){
            return true;
        }
        int l = 0;
        int r = input.length() - 1;
        while(r > l){
            if(input.charAt(r) != input.charAt(l)){
                return false;
            }

            r -= 1;
            l += 1;
        }

        return true;
    }

    // LC 91
    // 11.47 - 11.57 am
    /**
     *
     *  IDEA 1) DP ???
     *
     *  IDEA 2) RECURSION ???
     *
     */
    public int numDecodings(String encodedString) {

        // edge
        if(encodedString.charAt(0) == '0'){
            return 0;
        }

        // build `alphabet - code ` map
        Map<Character, Integer> map = new HashMap<>();
//        //for(Character x : )
//        for(int i = 0; i < 3; i++){
//
//        }
        // ????
        for(char x = 'a'; x < 'z'; x++){
            map.put(x, 'z' - x);
        }

        // recursion ???

        return 0;
    }

    // LC 450
    // 12.14 - 12.24 pm
    /**
     *  NOTE !!! the tree is `BST`
     *
     *  so, root < right sub tree,  left sub tree < root
     *
     *   IDEA : DFS
     *
     *     case 1) root == key
     *        -> if sub left == null && sub right == null
     *        -> return null
     *
     *    case 2) root  == key
     *       -> if sub left == null, sub right != null
     *       -> replace `to delete node` with sub right node
     *         return root as res
     *
     *
     *   case 3) root == key
     *      -> if sub left != null, sub right == null
     *      -> move  to `left most`
     *         replace the cur node (new noce) with root,
     *          return root as res
     *
     *
     *   case 4) root == key
     *     -> if sub left != null, sub right != null
     *      -> move `right`. then move `left` most,
     *       replace the cur node (new noce) with root,
     *        return root as res
     *
     *
     */
    /**
     * ======================================================
     *
     *
     *  case 1) sub left, right are null
     *
     *  case 2) sub left is null
     *   -> return sub right
     *
     *  case 3) sub right is null
     *   -> return sub left
     *
     * case 4) BOTH sub left, right are NOT NULL
     *   -> move `sub left` one step
     *   -> keep moving to `sub right`, till reach the end
     *   -> replace the `to delete node` with `cur node`
     *
     */
    // 12.06 - 12.16 pm
    public TreeNode deleteNode(TreeNode root, int key) {

        // edge
        if(root == null || ( root.left == null  && root.right == null ) ){
            if(root.val == key){
                return null;
            }
            return root;
        }

        // if too small
        if(root.val < key){
            root.right = deleteNode(root.right, key);
        }
        else if(root.val > key){
            root.left = deleteNode(root.left, key);
        }

        // if found the root
        else{

            // if sub left, right are null
            if(root.left == null && root.right == null){
                return null;
            }

            // if sub left null
            if(root.left == null){
               // return deleteNode(root.right, key);
                return root.right;
            }

            // if sub right null
            if(root.right == null){
               // return deleteNode(root.right, key);
                return root.left;
            }

            // if BOTH sub left, right are NOT null
            TreeNode left = root.left;

            // move `right most`
            while(left.right != null){
                left = left.right;
            }

            // swap root and `left`
            // update `right node` via recursive call
            TreeNode tmp = left;
            root.val = tmp.val;

           // root.right = deleteNode(left, tmp.val); // ???
            root.left = deleteNode(tmp, tmp.val); // ?????
        }

        return root;
    }


//    public TreeNode deleteNode(TreeNode root, int key) {
//        // edge
//        if(root == null || ( root.left == null  && root.right == null ) ){
//            if(root.val == key){
//                return null;
//            }
//            return root;
//        }
//
//        // if root.val is too small
//        if(root.val < key){
//            root.right = deleteNode(root.right, key);
//        }
//
//        // if root.val is too big
//        else if (root.val > key){
//            root.left = deleteNode(root.left, key);
//        }
//        // if `found`
//        else{
//
//            // case 1) sub left, right are null
//            if(root.left == null && root.right == null){
//                return null; // ??
//            }
//
//            // case 2) sub left is null
//            if(root.left == null){
//                // ???
//                return deleteNode(root.right, key);
//            }
//
//            // case 3) sub right is null
//            if(root.right == null){
//                return deleteNode(root.left, key);
//            }
//
//            // case 4) BOTH sub left, right are NOT NULL
//            else{
//                TreeNode left = root.left;
//                TreeNode right = left.right;
//                while(right.right != null){
//                    //TreeNode left = left.right;
//                    right = right.right;
//                }
//                root = right;
//                root.right = deleteNode(root.left, key); /// ?????
//            }
//
//        }
//
//        return root;
//    }



//    public TreeNode deleteNode(TreeNode root, int key) {
//        // edge
//        if(root == null || ( root.left == null  && root.right == null ) ){
//            if(root.val == key){
//                return null;
//            }
//            return root;
//        }
//
//        if(root.val < key){
//            // NOTE !!!
//            root.right = deleteNode(root.right, key);
//        }
//        else if (root.val > key){
//            root.left = deleteNode(root.left, key);
//        }
//        else{
//
//            // case 1) left & right are null
//            if(root.left == null && root.right == null){
//                return null;
//            }
//
//            // case 2) left is null
//            if(root.left == null){
//                //return deleteNode(root.right, key);
//                return root.right;
//            }
//
//            // case 3) right is null
//            if(root.right == null){
//                //return deleteNode(root.left, key);
//                return root.left;
//            }
//
//            else{
//                // case 4) left, right are NOT null
//                // cache cur node
////                TreeNode cache = root;
////                TreeNode right = root.right;
////                while(right != null && right.left != null){
////                    right = right.left;
////                    cache = right;
////                    right.left = deleteNode(root.left, key); // ?????
////                }
//
//                TreeNode tmp = root.left;
//                while(tmp.right != null){
//                    tmp = tmp.right;
//                }
//                root.val = tmp.val;
//                root.left = deleteNode(root.left, tmp.val);
//            }
//
//        }
//
//        // NOTE !!! DON'T forget return `root` as final result
//        return root;
//    }




//    public TreeNode deleteNode(TreeNode root, int key) {
//        // edge
//        if(root == null || ( root.left == null  && root.right == null ) ){
//            if(root.val == key){
//                return null;
//            }
//            return root;
//        }
//
//        // dfs call
//        //return null;
//        // ???
//        return deleteHelper(root, root, key);
//    }
//
//    public TreeNode deleteHelper(TreeNode prevNode, TreeNode node, int key){
//
//        if (node == null){
//            return null; // ???
//        }
//
//        if(node.left == null && node.right == null){
//            return node; // ???
//        }
//
//        if(node.val == key){
//
//            // case 0)
//            if(node.left != null && node.right != null){
//
//            }
//
//            // case 1)
//            if(node.left == null && node.right == null){
//                return null;
//            }
//
//            // case 2)
//            if(node.left == null && node.right != null){
//                //node = node.right;
//                node = deleteHelper(node, node.right, key); // ????
//                return node; // ???
//            }
//
//            // case 3)
//            if(node.left != null && node.right == null){
//                TreeNode newNode = deleteHelper(node, node.left, key); // ????
//                prevNode.left = newNode;
//                return node; // ???
//            }
//        }
//
//        return null;
//
//    }

    // LC 322
    /**
     *
     *  ->  Return the `fewest` number of coins that you need to make up that amount.
     *
     *
     *  IDEA 1) BACKTRACK + `check fewest number`
     *  IDEA 2) DFS ??
     *  IDEA 3) BFS
     *
     */
    //  IDEA 3) BFS
    public int coinChange(int[] coins, int amount) {
        // edge
        if(coins == null || coins.length == 0){
            return -1;
        }
        if(amount == 0){
            return 0;
        }
        if (amount < 0) {
            return -1;
        }

        // if all `coins` bigger than amount
        boolean possible = false;
        for(int c: coins){
            if(c <= amount){
                possible = true;
                break;
            }
        }
        if(!possible){
            return -1;
        }

        // sorting (big -> small)
        List<Integer> coins_ = new ArrayList<>();
        for(int c : coins){
            coins_.add(c);
        }
        Collections.sort(coins_, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        // q : [cur_val, cnt]
        Queue<int[]> q = new LinkedList<>();
        // add init val
        q.add(new int[] {0, 0});

        int res = 0;

        // optimize TLE: use cache
        // if `already computed`, use the saved val directly
        //HashSet<Integer> cache = new HashSet<>();
        // { key : computed_result}
        Map<Integer, Integer> cahce = new HashMap<>();

        // bfs
        while(!q.isEmpty()){

            int[] cur = q.poll();
            int cur_val = cur[0];
            int cnt = cur[1];

            if(cur_val == amount){
                return cnt;
            }

            if(cur_val > amount){
                continue;
            }

            if(!cahce.containsKey(cur_val)){
                cahce.put(cur_val, cnt);
            }

            // loop over coins
            for(int c: coins){
                if(!cahce.containsKey(cur_val + c)){
                    q.add(new int[] {cur_val + c, cnt + 1});
                }
            }
        }

        return -1;
    }

    // LC 152
    // 10.31 - 10.41 am
    /**
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) `Kadane` ALGO
     *     -> see `max`, `sub array` ....
     *
     */
    public int maxProduct(int[] nums) {

        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }

        // Kadane algo
        /**
         *  define 4 val ???
         *
         *   1. global max
         *   3. local max
         *   4. local min ????
         *
         */

//        int global_max = 1;
//        int local_min = 1;
//        int local_max = 1;

        int max_prod = nums[0];
        int min_prod = nums[0];

        //int[] product_arr = new int[nums.length + 1];
        int val = 1;

        /**
         *
         *  exp 1)
         *
         *   input = [1,2,3,4]
         *
         *     1 : local_min = 1, local_max = 1, global_max = 1
         *     2:  local_min = 1, local_max = 2, global_max = 2
         *     ...
         *
         *     4: local_min = 1, local_max = 24, global_max = 24
         *
         *
         *
         *  exp 2)
         *
         *  input = [1,-1,2,3]
         *    1: local_min = 1, local_max = 1, global_max = 1
         *    -1 : local_min = -1, local_max = 1, global_max = 1
         *    2 : local_min = -2, local_max = 2, global_max = 2
         *
         *
         *
         */

        for(int i = 0; i < nums.length; i++){

            // cache max_prod
            int cache = max_prod;

            max_prod = Math.max(
                    nums[i],
                    Math.max(min_prod * nums[i], cache * nums[i])
            );

            min_prod = Math.min(
                    nums[i],
                    Math.min(cache * nums[i], min_prod * nums[i])
            );



            max_prod = Math.max(max_prod, min_prod);
        }

        return max_prod;
    }

    // LC 139
    // 11.32 - 11.42 am
    // IDEA: BFS
    public boolean wordBreak(String s, List<String> wordDict) {
        // edge
        if(s == null || s.length() == 0){
            return true;
        }
        if(wordDict.isEmpty()){
            return false;
        }
        //boolean found = false;
        for(String x: wordDict){
            if(x.equals(s)){
                return true;
            }
        }

        // bfs
        // q : { idx_1, idx_2,, }
        // record the `cur idx within string building`
        Queue<Integer> q = new LinkedList<>();
        // add init val
        q.add(0);

        while(!q.isEmpty()){

            int idx = q.poll();

            // can build the `s` string
            if(idx == s.length() - 1){
                return true;
            }

            if(idx >= s.length()){
                continue;
            }

            // loop over dict
            for(String x: wordDict){

                //int start = idx;
                int end = idx + x.length();
                if(end >= s.length()){
                    continue;
                }
                // sub string of s
                String sub = s.substring(idx, end);
                // only put into queue when equals
                if(x.equals(sub)){
                    q.add(idx + x.length());
                }
            }

        }

        return false;
    }

    // LC 300
    // 10.08 - 10.18 AN
    /**
     *  IDEA 1) DP ???
     *
     *  IDEA 2) BRUTE FORCE
     *
     *  IDEA 3) sliding window ?????
     *
     *
     *
     */
    //  IDEA 1) DP
    public int lengthOfLIS(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }
        HashSet<Integer> set = new HashSet<>();
        for(int n: nums){
            set.add(n);
        }
        if(set.size() == 1){
            return 1;
        }

        int[] dp = new int[nums.length + 1];
        // init all vals in dp as 1
        Arrays.fill(dp, 1);

        // dp equatioin
        /**
         *  dp[k] =  max( dp[k-1] + 1, dp[k] ) ???
         *
         */
        dp[0] = 1;
        dp[1] = dp[1] > dp[0] ? 2 : 1;
        dp[2] = Math.max(dp[1] + 1, dp[2]);
        dp[3] = Math.max(dp[2] + 1, dp[3]);
        // if nums[i] > nums[i-1]

        //for(int i = 0; i < nums)




        return dp[nums.length - 1]; // ???
    }




    // IDEA 2) BRUTE FORCE
    public int lengthOfLIS_1(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 1;
        }
        HashSet<Integer> set = new HashSet<>();
        for(int n: nums){
            set.add(n);
        }
        if(set.size() == 1){
            return 1;
        }


        int res = 0;
        // brute force
        for(int i = 0; i < nums.length; i++){
            int tmp = 0;
            int last = nums[i];
            for(int j = i+1; j < nums.length; j++){
                if(nums[j] > last){
                    tmp += 1;
                    last = nums[j];
                }
            }
            res = Math.max(res, tmp);
        }

        return res;
    }

    // LC 334
    // 10.50 -11.00 am
    // IDEA 1) DP
    public boolean increasingTriplet(int[] nums) {
        // edge
        if(nums == null || nums.length < 3){
            return false;
        }
        if(nums.length == 3){
            return nums[2] > nums[1] && nums[1] > nums[0];
        }

        int[] dp = new int[nums.length + 1];
        Arrays.fill(dp, 1);

        int res = 0;

        // dp, return true directly if found a solution
        // NOTE !!! we move `right boundary` first, then left
        for(int i = 1; i < nums.length; i++){

            // early quit
            if(res >= 3){
                return true;
            }

            //boolean foundFirst = false;
            for(int j = 0; j < i; j++){
                if(nums[i] > nums[i]){
                    //dp[i] = Math.max(dp[i] + 1, dp[j]);
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                    res = Math.max(dp[i], res);
                }
            }
        }

        return res >= 3;
    }

    public boolean increasingTriplet_1(int[] nums) {
        // edge
        if(nums == null || nums.length < 3){
            return false;
        }
        if(nums.length == 3){
            return nums[2] > nums[1] && nums[1] > nums[0];
        }

        int[] dp = new int[nums.length + 1];

        // dp, return true directly if found a solution
        // NOTE !!! we move `right boundary` first, then left
        // `i` starts from idx=2, since it's has length=3, and it's the base case
        for(int i = 2; i < nums.length; i++){
            // ???
            // if found the `first` relation.
            // e.g. for  nums[i] > nums[j] > nums[k], found `nums[i] > nums[j]`
            boolean foundFirst = false;
            for(int j = 0; j < i; j++){
                if(nums[i] > nums[i]){
                    if(foundFirst){
                        return true;
                    }
                    foundFirst = true;
                }
            }
        }

        return false;
    }


    // LC 416
    // 11.17 - 11.27 am
    // or backtrack ????
    /**
     *  IDEA 1) DP ???
     *
     *  IDEA 2) SORT + 2 POINTERS
     *     -> we move 2 pointers from right, left boundary
     *     -> and see if can find a solution
     *
     *
     *
     *  exp 1) nums = [1,5,11,5]
     *
     *  -> sort, [1,5,5,11]
     *  -> half_val = 11
     *  -> 2 pointers
     *      [1, 5, 5, 11]
     *       l        r
     *
     *   -> move l,
     *
     *   [1, 5, 5, 11]
     *       l      r
     *
     *  [1, 5, 5, 11]
     *         l   r
     *
     *  -> return true
     *
     *
     *
     *  exp 2) nums = [1,2,3,5]
     *
     *  -> sort, [1,2,3,5]
     *  ->
     *
     */
    //  IDEA 2) SORT + hash map
    public boolean canPartition(int[] nums) {
        // edge
        if(nums == null || nums.length <= 1){
            return false;
        }
        int sum = 0;
        for(int n: nums){
            sum += n;
        }
        if(sum % 2 == 1){
            return false;
        }

        //int half_val = sum / 2;

        List<Integer> nums_ = new ArrayList<>();
        for(int n: nums){
            nums_.add(n);
        }
        // sorting (small -> big)
        Collections.sort(nums_, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        // hash map
        // { k : sum_till_now }
        Map<Integer, Integer> map = new HashMap<>();



        return false;
    }


    //  IDEA 2) SORT + 2 POINTERS
    public boolean canPartition_1(int[] nums) {
        // edge
        if(nums == null || nums.length <= 1){
            return false;
        }
        int sum = 0;
        for(int n: nums){
            sum += n;
        }
        if(sum % 2 == 1){
            return false;
        }

        //int half_val = sum / 2;

        List<Integer> nums_ = new ArrayList<>();
        for(int n: nums){
            nums_.add(n);
        }
        // sorting (small -> big)
        Collections.sort(nums_, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        // 2 pointers
        int l = 0;
        int r = nums_.size() - 1;
        int tmp_sum = 0;

        while (r > l){

            tmp_sum += (nums[l] + nums[r]);

            // case 1)
            if(nums[l] == sum / 2 || nums[r] == sum / 2){
                return true;
            }

            // case 2)
            if(tmp_sum == sum / 2){
                return true;
            }

            // case 3) tmp sum is too small
            if(tmp_sum  < sum / 2){
                l += 1;
            }

            // case 4) tmp sum is too big
            else{
                r += 1;
            }

        }


        return false;
    }

    // LC 698
    // 9.28 - 9.38 am
    /**
     *
     *  -> return true if it is possible to divide this array
     *     into k non-empty subsets whose sums are all equal.
     *
     *
     *   IDEA 1) DP
     *    - LC 416 ???
     *
     *   IDEA 2) BRUTE FORCE
     *
     *   IDEA 3) BACKTRACK
     *
     *
     */
    //  IDEA 3) BACKTRACK
    public boolean canPartitionKSubsets(int[] nums, int k) {
        // edge
        if(nums == null || nums.length == 0){
            return true;
        }
        if(k > nums.length){
            return false;
        }
        // ???
        if(k <= 1){
            return true;
        }
        int _sum = 0;
        for(int n: nums){
            _sum += n;
        }
        if(_sum % k != 0){
            return false;
        }

        List<Integer> nums_ = new ArrayList<>();
        for(int n: nums){
            nums_.add(n);
        }

        // sorting (small -> big)
        Collections.sort(nums_, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o1 - o2;
                return diff;
            }
        });

        // backtrack

        return false;
    }

    public boolean partitionSumHelper(List<Integer> input, int k, int start_idx, HashSet<Integer> visited, List<List<Integer>> cur, int subSum){

        if(cur.size() == 4){
            if(isValidFound(cur, subSum)){
                return true;
            }else{
                return false; // ????
            }
        }

        if(cur.size() > 4){
            return false;
        }

        // ???
        for(int i = start_idx; i < input.size(); i++){

        }


        return false;
    }

    public boolean isValidFound(List<List<Integer>> cur, int subSum){
        if(cur.size() != 4){
            return false;
        }

        for(List<Integer> x: cur){
            int tmp_sum = 0;
            for(int num: x){
                tmp_sum += num;
            }
            if(tmp_sum != subSum){
                return false;
            }
        }

        return true;
    }

    // LC 377
    // 10.19 - 10.29 am
    /**
     *
     * Given an array of distinct integers nums
     * and a target integer target,
     * return the number of possible combinations
     * that add up to target.
     *
     *
     * -> count that combinations sum == target
     *
     *
     *  IDEA 1) BACKTRACK ????
     *
     *  IDEA 2) DP
     *
     *  IDEA 3) BRUTE FORCE ???
     *
     */
    int cnt = 0;
    public int combinationSum4(int[] nums, int target) {

        // edge

        // backtrack
        combineHelper(nums, target, 0, new HashSet<>(), new ArrayList<>());

        return cnt;
    }

    public void combineHelper(int[] nums, int target, int start_idx, HashSet<List<Integer>> visited, List<Integer> cur){

        int sum_ = getSum(cur);

        if(sum_ == target){
            //Collections.sort(cur);
            // need to avoid duplicated ??
            if(!visited.contains(cur)){
                visited.add(cur);
                cnt += 1;
            }
        }

        if(sum_ > target){
            return;
        }

        for(int i = start_idx; i < nums.length; i++){
            if(sum_ + nums[i] > target){
                continue;
            }

            cur.add(nums[i]);
            combineHelper(nums, target, i, visited, cur);

            // undo
            cur.remove(cur.size() - 1);
        }

    }

    public int getSum(List<Integer> list){
        int res = 0;
        for(int x: list){
            res += x;
        }
        return res;
    }

    // LC 279
    // 12.02 - 12.12 pm
    /**
     * Given an integer n,
     * return the `least` number of perfect square
     * numbers that sum to n.
     *
     *
     *  IDEA 1) DP
     *
     *  IDEA 2) BRUTE FORCE
     *
     *   -> start from `biggest` perfect num
     *      check if can build the input (n)
     *      if not, find the next smaller perferct num,
     *      ... repeat
     *
     *      if can't find one, return -1
     *
     */
    // BRUTE FORCE
    public int numSquares(int n) {
        // edge
        // ???
        if(n < 0){
            return -1;
        }
        if(n <= 1){
            return n;
        }

        // brute force
        int start = n;
        while(start >= 1){

//            if(isPerfect(start)){
//                if(n % start == 0){
//                    return n / start;
//                }
//            }

//            if (numSquaresHelper(start, n - x)){
//
//            }

            start -= 1;
        }


        return -1;
    }

    public boolean numSquaresHelper(int start, int n){

        for(int x = start; x > 0; x--){

            if(n == 0){
                return true;
            }

            if(isPerfect(x)){
                numSquaresHelper(start, n - x);
            }
        }

        return false;
    }

    public boolean isPerfect(int x){
        if (x == 0){
            return false; //??
        }
//        if(x == 1){
//            return true;
//        }
        double sqrt = Math.sqrt(x);
        return sqrt * sqrt == x; /// ?????
    }

    // LC 343
    // 2.37 - 2.47 pm
    /**
     *  IDEA 1) GREEDY
     *
     *   n = 2,  2 = 1 + 1,
     *           1 * 1 = 1
     *
     *
     *   n = 3, 2 + 1,
     *
     *
     *   n = 10, 10 = 1
     *
     *   .. ??
     *
     *
     *  IDEA 2) MATH
     *
     *   (a + b) >= sqrt(a * b)
     *
     *   the `max` ( a * b ) happens
     *   when a ~= b  ???
     *
     *
     */
    public int integerBreak(int n) {
        // edge
        if(n <= 1){
            return 0; // ???
        }
        if(n == 2){
            return 1;
        }
        if(n == 3){
            return 2;
        }

       // int val_1 =
       int avgByThree = n / 3;  // ??
       int left = avgByThree - 1;
       int right = avgByThree + 1;

       while(left > 0 && right < n){
           int _sum = avgByThree + left + right;
           if(_sum == n){
               return avgByThree * left * right;
           }
           if (_sum < n){
               right += 1;
           }else{
               left += 1;
           }
       }

        return 0;
    }

    // LC 1406
    // 3.05 - 3.15 pm
    public String stoneGameIII(int[] stoneValue) {

        return null;
    }

    // LC 62
    // 3.18 - 3.28 pm
    /**
     *  IDEA 1) MATH
     *
     *  IDEA 2) DP
     *
     *   -> can choose `->` or `down` move each time
     *
     *   -> dp[i][j] =  dp[i-1][j] + dp[i][j-1]
     *
     */
    public int uniquePaths(int m, int n) {
        // edge
        if(m == 0 || n == 0){
            return 0;
        }
        if(m == 1 || n == 1){
            return 1;
        }

        // m : y - len
        // n : x - len

        int[][] dp = new int[m][n]; // init all cells val as 0

        dp[0][0] = 1;
        dp[0][1] = 1;
        dp[1][0] = 1;

        for(int i = 1; i < m; i++){
            for(int j = 1; j < n; j++){
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }

        return dp[m-1][n-1]; // /??
    }

    // LC 63
    // 2.25 - 2.35 pm
    /**
     *  IDEA 1) GREEDY
     *
     *  IDEA 2) DP (LC 64)
     *
     *  IDEA 3) DFS
     *
     *
     *
     */
    // IDEA 3) DFS
    int pathCnt;
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        if (m == 0 || n == 0)
            return 0;

        //return 0;
        //return uniquePathDFS(0, 0, new int[m][n]);
        return pathCnt;
    }

    public void uniquePathDFS(int x, int y, int[][] obstacleGrid, boolean[][] visited){

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // if found one
        if(x == n && y == m){
            pathCnt += 1;
            return;
        }
        if( x < 0  || x > n || y < 0 || y > m || visited[y][x] ){
            return;
        }

        visited[y][x] = true;

        uniquePathDFS(x+1, y, obstacleGrid, visited);
        uniquePathDFS(x-1, y, obstacleGrid, visited);
        uniquePathDFS(x, y+1, obstacleGrid, visited);
        uniquePathDFS(x, y-1, obstacleGrid, visited);

        return;
    }



    // IDEA 2) DP (LC 64)
    public int uniquePathsWithObstacles_1(int[][] obstacleGrid) {

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        if (m == 0 || n == 0)
            return 0;

        int[][] dp = new int[m][n];

        /**  NOTE !!! init val as below
         *
         *  -> First row and first column = 1 path
         *    (only one way to go right/down)
         */
        for (int i = 0; i < m; i++) {
            dp[i][0] = 1;
        }
        for (int j = 0; j < n; j++) {
            dp[0][j] = 1;
        }

        // Fill the rest of the DP table
        // NOTE !!! i, j both start from 1
        // `(0, y), (x, 0)` already been initialized
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                /**  DP equation
                 *
                 *   dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                 */
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }


//    public int uniquePaths(int m, int n) {
//        if (m == 0 || n == 0)
//            return 0;
//
//        int[][] dp = new int[m][n];
//
//        /**  NOTE !!! init val as below
//         *
//         *  -> First row and first column = 1 path
//         *    (only one way to go right/down)
//         */
//        for (int i = 0; i < m; i++) {
//            dp[i][0] = 1;
//        }
//        for (int j = 0; j < n; j++) {
//            dp[0][j] = 1;
//        }
//
//        // Fill the rest of the DP table
//        // NOTE !!! i, j both start from 1
//        // `(0, y), (x, 0)` already been initialized
//        for (int i = 1; i < m; i++) {
//            for (int j = 1; j < n; j++) {
//                /**  DP equation
//                 *
//                 *   dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
//                 */
//                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
//            }
//        }
//
//        return dp[m - 1][n - 1];
//    }


    // LC 64
    // 3.07 - 3.17 pm
    /**
     *
     *  Given a m x n grid filled with non-negative numbers,
     *  -> find a path from `top left` to `bottom right`,
     *  which minimizes the sum of all numbers along its path.
     *
     *
     *
     *  IDEA 1) min PQ + BFS ???
     *
     *  IDEA 2) DFS
     *
     *  IDEA 3) DP
     *
     */
    public int minPathSum(int[][] grid) {

        // edge
        if(grid == null || grid.length == 0 || grid[0].length == 0){
            return 0;
        }
        if(grid.length == 1 || grid[0].length == 1){
            return 1;
        }

        int m = grid.length;
        int n = grid[0].length;

        // min PQ
        // PQ : { [x, y, cost_till_now] }
        // sort by `cost_till_now` (small -> big)
        PriorityQueue<int[]> small_pq = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                int diff = o1[2] - o2[2];
                return diff;
            }
        });

        // add init val to PQ
        small_pq.add(new int[] {0, 0, 0});
        int res = 0;

        int[][] dirs = new int[][] { {0,1}, {0,-1}, {1,0}, {-1,0} };

        while(!small_pq.isEmpty()){

            int[] cur = small_pq.poll();
            int x = cur[0];
            int y = cur[1];
            int cost_till_now = cur[2];

            if(x == n && y == m){
                return cost_till_now;
            }

            // NOTE !!! validate below
            if(x < 0 || x >= n || y < 0 || y >= m){
                continue;
            }

            for(int[] dir: dirs){
                int x_ = x + dir[0];
                int y_ = y + dir[1];
                if(x_ < 0 || x_ >= n || y_ < 0 || y_ >= m){
                    continue;
                }

                small_pq.add(new int[] {x_, y_, cost_till_now + grid[y_][x_]});
            }

        }


        return res;
    }

    // LC 1143
    // 3.56 - 4.06 pm
    /**
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) DP ???
     *
     *  IDEA 3) RECURSION ???
     *
     */
    public int longestCommonSubsequence(String text1, String text2) {

        return 0;
    }

    // LC 1049
    // 10.16 - 10.26 am
    /**
     *
     *
     * Return the smallest possible weight of the left stone.
     * If there are no stones left, return 0.
     *
     *
     *   IDEA 1) MAX PQ ???
     *   IDEA 2)  BRUTE FORCE ??
     *   IDEA 3) DP ??
     *
     */
    public int lastStoneWeightII(int[] stones) {
        // edge
        if(stones == null || stones.length == 0){
            return 0;
        }
        if(stones.length == 1){
            return stones[0];
        }
        if(stones.length == 2){
            if(stones[0] == stones[1]){
                return 0;
            }
            return Math.abs(stones[0] - stones[1]);
        }

        // MAX PQ
        PriorityQueue<Integer> max_pq = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int diff = o2 - o1;
                return diff;
            }
        });

        // add to PQ
        for(int s: stones){
            max_pq.add(s);
        }

        while(max_pq.size() >= 2){
            int s1 = max_pq.poll();
            int s2 = max_pq.poll();
            if(s1 != s2){
                max_pq.add(Math.abs(s1 - s2));
            }
        }

        return max_pq.peek();
    }

    // LC 309
    // 10.46 - 10.56 am
    /**
     *  IDEA 1) DP
     *
     *    DP equation:
     *
     *        dp[i] = max( dp[i] - dp[i-2], dp[i] - dp[i-1] )
     *
     *  IDEA 2) BRUTE FORCE
     *
     *
     */
    public int maxProfit(int[] prices) {

        return 0;
    }

    // LC 518
    // 11.18 - 11.49 am
    /**
     *
     * Return the number of combinations that make up that amount.
     *  If that amount of money
     *   cannot be made up by any combination of the coins, return 0.
     *
     *
     * -> Return the `number of combinations` that make up that amount.
     *
     *
     *
     *   IDEA 1) BACKTRACK
     *
     *   IDEA 2) DP
     *
     *   IDEA 3) BRUTE FORCE ???
     */
    //  IDEA 1) BACKTRACK
    int combinationCnt;
    List<List<Integer>> collected = new ArrayList<>();
    public int change(int amount, int[] coins) {
        // edge
        if(amount == 0){
            return 0; // ??
        }
        if(coins == null || coins.length == 0){
            return 0;
        }
        if(amount < 0){
            return 0;
        }
        if(coins.length == 1){
            return amount % coins[0] == 0 ? 1 : 0;
        }
//
//        // dedup coins
//        HashSet<Integer> set = new HashSet<>();
//        for(int c:coins){
//            set.add(c);
//        }
//
//        int[] coins2 = new int[set.size()];
//        for(int x : set.toArray().){
//            set.
//            coins2
//        }

        // backtrack
        coinHelpr(amount, coins, 0, new ArrayList<>());

        return combinationCnt;
    }

    public void coinHelpr(int amount, int[] coins, int start_idx, List<Integer> cur){

        int _sum = getListSum(cur);

        if(_sum == amount){
            //Collections.sort(cur);
            if(!collected.contains(cur)){
                collected.add(new ArrayList<>(cur));
                combinationCnt += 1;
            }
        }

        if(_sum > amount){
            return;
        }

        // NOTE !!! make `i start from start_idx`
        for(int i = start_idx; i < coins.length; i++){
            if(_sum + coins[i] <= amount){
                cur.add(coins[i]);
                coinHelpr(amount, coins, i, cur);
                // undo
                cur.remove(cur.size() - 1);
            }
        }

    }

    public int getListSum(List<Integer> input){
        int res = 0;
        for(int x: input){
            res += x;
        }
        return res;
    }

    // LC 494
    // 10.04 - 10.14 am
    /**
     *  IDEA 1) BRUTE FORCE
     *
     *   -> double loop,
     *      1st loop : + or -, move from idx
     *      2nd loop : + or -, move from idx + 1
     *
     *
     *
     *  IDEA 2) DP ???
     *
     *
     *  IDEA 3) RECURSION ???
     *
     *
     *  exp 1)
     *
     *    input = [2,1]
     *
     *    + 2 - 1 = 1
     *    - 2 + 1 = -1
     *    + 2 + 1 = 3
     *
     *    ...
     *
     *
     *  exp 2)
     *
     *    input = [1,1,1,1,1], target = 3
     *
     *     - 1 + 1 +
     *
     *     2
     *
     *
     */
    //  RECURSION
    public int findTargetSumWays(int[] nums, int target) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            if(nums[0] == target || nums[0] == -1 * target){
                return 1;
            }
            return 0;
        }

        return 0;
    }

    public class Solution {
        public int findTargetSumWays(int[] nums, int target) {
            return calculateWays(nums, 0, 0, target);
        }

        private int calculateWays(int[] nums, int index, int currentSum, int target) {
            // Base case: all numbers used
            if (index == nums.length) {
                return currentSum == target ? 1 : 0;
            }

            // Choose '+' for current number
            int add = calculateWays(nums, index + 1, currentSum + nums[index], target);

            // Choose '-' for current number
            int subtract = calculateWays(nums, index + 1, currentSum - nums[index], target);

            // Return total ways from both choices
            return add + subtract;
        }
    }



    // BRUTE FORCE
//    public int findTargetSumWays(int[] nums, int target) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            if(nums[0] == target || nums[0] == -1 * target){
//                return 1;
//            }
//            return 0;
//        }
//
////        Integer[] nums_ = new Integer[nums.length];
////        for(int i = 0; i < nums.length; i++){
////            nums_[i] = nums[i];
////        }
//
//        int res = 0;
//        // brute force
//        for(int i = 0; i < nums.length; i++){
//            for(int j = i+1; j < nums.length; j++){
//                int[] sub_nums = Arrays.copyOfRange(nums, i,j);
//            }
//        }
//
//        return res;
//    }

    // LC 97
    // 10.30 - 10.40 am
    public boolean isInterleave(String s1, String s2, String s3) {

        return false;
    }

    // LC 329
    // 10.33 - 10.43 am
    /**
     *
     *  -> Given an m x n integers matrix,
     *    return the `length` of the
     *    `longest` `increasing` path in matrix.
     *
     *
     *  IDEA 1) LOOP + DFS
     *
     *  IDEA 2) DP ???
     *
     *
     *
     *
     *
     *  exp 1)
     *
     *    [[9,9,4],
     *     [6,6,8],
     *     [2,1,1]
     *     ]
     *
     *
     *
     *  exp 2)
     *
     *   [[3,4,5],
     *   [3,2,6],
     *   [2,2,1]
     *   ]
     *
     *
     *
     *
     */
    // IDEA: DFS + MEMORIZATION
    public int longestIncreasingPath(int[][] matrix) {

        // edge
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
            return 0;
        }
        if(matrix.length == 1 || matrix[0].length == 1){
            return 1;
        }

        int l = matrix.length;
        int w = matrix[0].length;

        int max_len = 1;

        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){
                int cur_len = getMaxIncreaseLen(matrix, j, i, matrix[i][j], new boolean[l][w]);
                max_len = Math.max(max_len, cur_len);
            }
        }

        return max_len;
    }

    public int getMaxIncreaseLen(int[][] matrix, int x, int y, int last_val, boolean[][] visited){

        int l = matrix.length;
        int w = matrix[0].length;

        // NOTE !!! we validate below
        if(x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || matrix[y][x] <= last_val){
            return 0;
        }

        last_val = matrix[y][x];
        visited[y][x] = true;

        // move 4 dirs
        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
        int res = 0;

        for(int[] dir: dirs){
            int x_ =  x + dir[0];
            int y_ = y + dir[1];

            res = Math.max(res, getMaxIncreaseLen(matrix, x_, y_, last_val, visited));
        }


        return res;

    }

    // IDEA: LOOP + DFS
//    public int longestIncreasingPath(int[][] matrix) {
//
//        // edge
//        if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
//            return 0;
//        }
//        if(matrix.length == 1 || matrix[0].length == 1){
//            return 1;
//        }
//
//        int l = matrix.length;
//        int w = matrix[0].length;
//
//        int max_len = 1;
//
//        for(int i = 0; i < l; i++){
//            for(int j = 0; j < w; j++){
//                int cur_len = getMaxIncreaseLen(matrix, j, i, matrix[i][j], new boolean[l][w]);
//                max_len = Math.max(max_len, cur_len);
//            }
//        }
//
//        return max_len;
//    }
//
//    public int getMaxIncreaseLen(int[][] matrix, int x, int y, int last_val, boolean[][] visited){
//
//        int l = matrix.length;
//        int w = matrix[0].length;
//
//        // NOTE !!! we validate below
//        if(x < 0 || x >= w || y < 0 || y >= l || visited[y][x] || matrix[y][x] <= last_val){
//            return 0;
//        }
//
//        last_val = matrix[y][x];
//        visited[y][x] = true;
//
//        // move 4 dirs
////        return 1 + getMaxIncreaseLen(matrix, x + 1, y, last_val, visited) +
////                getMaxIncreaseLen(matrix, x - 1, y, last_val, visited) +
////                getMaxIncreaseLen(matrix, x, y + 1, last_val, visited) +
////                getMaxIncreaseLen(matrix, x, y - 1, last_val, visited);
//
//        int[][] dirs = {{0,1}, {1,0}, {0,-1}, {-1,0}};
//        int res = 0;
//
//        for(int[] dir: dirs){
//            int x_ =  x + dir[0];
//            int y_ = y + dir[1];
//
//            res = Math.max(res, getMaxIncreaseLen(matrix, x_, y_, last_val, visited));
//        }
//
//
//        return res;
//
//    }


}
