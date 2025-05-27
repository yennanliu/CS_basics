package dev;

import LeetCodeJava.DataStructure.ListNode;
import LeetCodeJava.DataStructure.TreeNode;
import LeetCodeJava.LinkedList.RemoveDuplicatesFromAnUnsortedLinkedList;

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
    // 10.38 - 11.07 am
    /**
     *  IDEA 1) Kadane ALGO
     *
     *  1. define 4 var
     *
     *   - global_max
     *   - global_min
     *   - local_max
     *   - local_min
     *
     *
     *
     */
    public int maxProduct(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }

//        int global_max = nums[0];
//        int global_min = nums[0];
//        int local_max = nums[0];
//        int local_min = nums[0];

        int max_prod = nums[0];
        int min_prod = nums[0];
        int res = nums[0];


        for(int i = 1; i < nums.length; i++){

            int x = nums[i];

            int cache = max_prod;

            max_prod = Math.max(
                    min_prod * x,
                    Math.max(x, x * cache)
            );

            min_prod = Math.min(
                    min_prod * x,
                    Math.min(x, x * cache)
            );

//            local_max = Math.max(x, x * local_max);
//            local_min = Math.min(x, x * local_min);
//
//            int cache = local_max;
//
//            global_min = Math.min(
//                    local_min,
//                    global_min
//            );
//
//            global_max = Math.max(
//                    global_max,
//                    local_max
//            );

            res = Math.max(max_prod, res);

        }

        return res;
    }


//    public int maxProduct_2(int[] nums) {
//
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//
//        // Kadane algo
//        /**
//         *  define 4 val ???
//         *
//         *   1. global max
//         *   3. local max
//         *   4. local min ????
//         *
//         */
//
////        int global_max = 1;
////        int local_min = 1;
////        int local_max = 1;
//
//        int max_prod = nums[0];
//        int min_prod = nums[0];
//
//        //int[] product_arr = new int[nums.length + 1];
//        int val = 1;
//
//        /**
//         *
//         *  exp 1)
//         *
//         *   input = [1,2,3,4]
//         *
//         *     1 : local_min = 1, local_max = 1, global_max = 1
//         *     2:  local_min = 1, local_max = 2, global_max = 2
//         *     ...
//         *
//         *     4: local_min = 1, local_max = 24, global_max = 24
//         *
//         *
//         *
//         *  exp 2)
//         *
//         *  input = [1,-1,2,3]
//         *    1: local_min = 1, local_max = 1, global_max = 1
//         *    -1 : local_min = -1, local_max = 1, global_max = 1
//         *    2 : local_min = -2, local_max = 2, global_max = 2
//         *
//         *
//         *
//         */
//
//        for(int i = 0; i < nums.length; i++){
//
//            // cache max_prod
//            int cache = max_prod;
//
//            max_prod = Math.max(
//                    nums[i],
//                    Math.max(min_prod * nums[i], cache * nums[i])
//            );
//
//            min_prod = Math.min(
//                    nums[i],
//                    Math.min(cache * nums[i], min_prod * nums[i])
//            );
//
//
//
//            max_prod = Math.max(max_prod, min_prod);
//        }
//
//        return max_prod;
//    }

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

        int[][] memory = new int[l][w];
        // init as `-1` value
        Arrays.fill(memory, -1);

        int max_len = 1;

        for(int i = 0; i < l; i++){
            for(int j = 0; j < w; j++){

                int cur_len = 0;

                if(memory[i][j] != -1){
                    cur_len = memory[i][j];
                }else{
                    cur_len = getMaxIncreaseLen(matrix, j, i, matrix[i][j], new boolean[l][w], memory);
                }
                max_len = Math.max(max_len, cur_len);
            }
        }

        return max_len;
    }

    public int getMaxIncreaseLen(int[][] matrix, int x, int y, int last_val, boolean[][] visited, int[][] memory){

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

//            if(memory[y_][x_] != -1){
//                res = Math.max(res, memory[y_][x_]);
//            }else{
//                int val = getMaxIncreaseLen(matrix, x_, y_, last_val, visited, memory);
//                memory[y_][x_] = val;
//                res = Math.max(res, val);
//            }

            res = Math.max(res, getMaxIncreaseLen(matrix, x_, y_, last_val, visited, memory));

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


    // LC 115
    // 11.26 - 11.36 am
    /**
     *
     * Given two strings s and t, return the number of distinct
     * subsequences of s which equals t.
     *
     */
    public int numDistinct(String s, String t) {

        // edge
        if(t == null || t.length() == 0){
            return 0;
        }
        if(t.equals(s)){
            return 1;
        }

        // ???? should not happen ???
        if(t.length() < s.length()){
            return -1;
        }

        return 0;
    }


    // LC 53
    // 11.54 - 12.06 pm
    /**
     *
     *  * Given an integer array nums, find the subarray
     *  *
     *  with the `largest` sum, and return its sum.
     *  *
     *  *
     *  *  -> A subarray is a contiguous non-empty sequence
     *    of elements within an array.
     *
     *
     *  -> return the max sum of the `contiguous` sub array
     *
     *
     */
    /**
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) K*** algo
     *
     *
     *
     *  exp 1)
     *
     *    Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
     *
     *    -> [ -2, 1, -2
     *
     *
     *
     */
    public int maxSubArray(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return -1;
        }
        if(nums.length == 1){
            return nums[0];
        }

        //int[] dp = new int[nums.length];
        //int maxSum = 0;

        int localMax = nums[0];
        int globalMax = nums[0];

        /**
         *  NOTE !!!
         *
         *   below code has actually 2 options consideration
         *
         *
         *   1. nums[i] : `give up` sum till now, init a new sub array
         *   2. nums[i] + localMax : keep `sub array`, add the new val to it
         *
         */
        for(int i = 1; i < nums.length; i++){

            localMax = Math.max(
                    nums[i],
                    nums[i] + localMax
            );

            // we update the global max at every iteration
            globalMax = Math.max(globalMax, localMax);
        }


        return globalMax;
    }

    // LC 152
    // 12.21 - 12.31 pm
    // IDEA : K*** algo
//    public int maxProduct(int[] nums) {
//        // edge
//        // ...
//
//        /**
//         *  NOTE !!!
//         *
//         *   we define 3 vars below:
//         *
//         *   1. globalMax
//         *   2. localMax
//         *   3. localMin ??
//         *
//         */
//        int globalMax = nums[0];
//        int localMax = nums[0];
//        int localMin = nums[0];
//
//        for(int i = 1; i < nums.length; i++){
//
//            // NOTE !!! we `cache` localMax first
//            int cache = localMax;
//
//            localMin = Math.min(nums[i] * localMin,
//                    Math.min(
//                            nums[i],
//                            nums[i] * localMax
//                    )
//            );
//
//            localMax = Math.max(nums[i] * cache,
//                    Math.max(
//                            nums[i],
//                            nums[i] * localMin
//                    )
//            );
//
//
//            globalMax = Math.max(
//                    Math.max(localMax, localMin),
//                    globalMax
//            );
//
//        }
//
//        return globalMax;
//    }

//    // LC 918
//    // 12.49 - 12.59 pm
//    public int maxSubarraySumCircular(int[] nums) {
//        return 0;
//    }


    // LC 72
    // 10.07 - 10.17 am
    /**
     *
     *
     *  -> return the min ops that can
     *     convert w1 to w2
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) DP ???
     *
     *
     *
     *
     *  exp 1)
     *
     *   w1 = "horse", w2 = "ros"
     *
     *   -> res = 3
     *
     *   -> rorse
     *   -> rose
     *   -> ros
     *
     *
     *
     *  exp 2)
     *
     *
     *
     */
    public int minDistance(String word1, String word2) {

        return 0;
    }


    // LC 312
    // 10.26 - 10.36 am
    /**
     *
     *  You are asked to burst all the balloons.
     *
     *
     *   If you burst the ith balloon,
     *      you will get nums[i - 1] * nums[i] * nums[i + 1] coins.
     *
     *   If i - 1 or i + 1 goes out of bounds of the array,
     *      then treat it as if there is a balloon with a 1 painted on it.
     *
     *
     *
     *  -> Return the `maximum` coins
     *     you can collect by bursting the balloons wisely.
     *
     *
     *   IDEA 1) DP
     *
     *   IDEA 2) BRUTE FORCE
     *
     *
     */
    public int maxCoins(int[] nums) {

        return 0;
    }

    // LC 10
    // 10.47 - 10.57 am
    /**
     *
     *  implement `regular expression` matching with support for '.' and '*'
     *
     *    - '.' Matches any single character.
     *    -  '*' Matches zero or more of the preceding element.
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *  IDEA 2) DP
     *
     *  IDEA 3) RECURSION ???
     *
     *
     */
    public boolean isMatch(String s, String p) {
        // edge
        if(s == null && p == null){
            return true;
        }
        if(s == null || p == null){
            return false;
        }
        if(s.equals(p)){
            return true;
        }

        // case 1) no '*' and '.' in s
        if(!p.contains("*") && !p.contains(".")){
           // return s.equals(p);
            return false;
        }

        // case 2) '.' in s
        if(!p.contains("*") && p.contains(".")){
            if(s.length() != p.length()){
                return false;
            }

            for(int i = 0; i < s.length(); i++){
                if(s.charAt(i) != p.charAt(i)){
                    if(p.charAt(i) != '.'){
                        return false;
                    }
                }
            }

        }

        // case 3) '*' in s
        if(p.contains("*") && !p.contains(".")){

        }


        return true;
    }

    // LC 978
    // 11.18 - 11. 28 am
    /**
     *  -> return the length of a `maximum` size
     *     turbulent subarray of arr.
     *
     *
     *
     *  IDEA 1) BRUTE FORCE
     *
     *
     *   -> loop over idx,
     *      check
     *        - odd idx
     *        - even idx
     *        get the local, global max len
     *
     *   -> return global max len as res
     *
     *
     *  IDEA 2) `diff` array
     *
     *  ->  arr = [9,4,2,10,7,8,8,1,9]
     *      -> diff = [0, -5, -2, 8, -3, 1, 0, -7, 8]
     *
     *      ->
     *
     *
     *
     */
    // SLIDING WINDOW
    public int maxTurbulenceSize(int[] arr) {
        if(arr == null || arr.length == 0){
            return 0;
        }
        if(arr.length == 1){
            return 1;
        }
        int maxLen = 1;

        // double loop
        for(int i = 0; i < arr.length; i++){
            int cnt = 0;
            int j = i+1;
            while(j < arr.length){
                if(cnt % 2 == 0){
                    if(arr[i] > arr[j]){
                        continue;
                    }else{
                        break;
                    }
                }else{
                    if(arr[j] > arr[i]){
                        continue;
                    }else{
                        break;
                    }
                }

            }
        }


        return maxLen;
    }

    // BRUTE FORCE
//    public int maxTurbulenceSize(int[] arr) {
//        if(arr == null || arr.length == 0){
//            return 0;
//        }
//        if(arr.length == 1){
//            return 1;
//        }
//        int maxLen = 1;
//
//        // double loop
//        for(int i = 0; i < arr.length; i++){
//            int cnt = 0;
//            for(int j = i + 1; j < arr.length; j++){
//                if(cnt % 1 == 0){
//                    if(arr[i] > arr[j]){
//                        continue;
//                    }else{
//                        break;
//                    }
//                }
//            }
//        }
//
//
//        return maxLen;
//    }


    // LC 918
    // 12.18 - 12.28 pm
    /**
     *  A subarray may only include each element of the fixed buffer
     *
     *   nums at most once.
     *
     *  -> return the maximum possible sum of a
     *     non-empty subarray of nums.
     *
     *
     *  IDEA 1) K*** Algo + `circular` handling
     *
     */
    public int maxSubarraySumCircular___(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return nums[0];
        }

        int globalMaxSum = nums[0];
        int localMaxSum = nums[0];

        // queue : {idx_1, idx_2, ...}
        Queue<Integer> q = new LinkedList<>();
        // double nums
        //int[] nums_2 = new int[nums.length * 2];
        List<Integer> nums_2 = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            for(int j = 0; j < nums.length; j ++){
                nums_2.add(nums[j]);
            }
        }

        for(int i = 0; i < nums_2.size(); i++){

            if(q.size() >= nums.length){
                q.poll();
            }

            if(!q.contains(i % nums.length)){
                localMaxSum = Math.max(
                        nums_2.get(i),
                        nums_2.get(i) + localMaxSum
                );
            }

            globalMaxSum = Math.max(
                    localMaxSum,
                    globalMaxSum
            );

            // add `visited` idx to queue
            q.add(i % nums.length);
        }

        return globalMaxSum;
    }

    // LC 55
    // 10.22 - 10.32 am
    /**
     *
     *  -> Return true if you can reach the last index,
     *    or false otherwise.
     *
     *
     *  IDEA 1) GREEDY
     *
     *   -> loop `inversely` from right to left
     *   -> check if `nums[i-1] + (i-1) >= i`
     *   -> if above not true, return false directly
     *   ...
     *
     *   return true
     *
     */
//    public boolean canJump(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return true;
//        }
//        if(nums.length == 1){
//            return true;
//        }
//        //if(nums)
//
//        int rightMost = nums.length - 1; // ???
//
//        // loop `right -> left`
//        for(int i = nums.length - 1; i >= 0; i--){
//            if(nums[i-1] + (i-1) >= rightMost){
//                //return false;
//                rightMost = i;
//            }
//        }
//
//        //return true;
//        return rightMost == 0;
//    }


    // LC 45
    // 11.04 - 11.14 am
    /**
     *
     *  -> Return the `minimum` number of jumps
     *    to reach nums[n - 1].
     *
     *
     *
     *   IDEA 1) GREEDY
     *
     *   keep track `i + nums[i]` as max_dist
     *   and if max_dist >= nums.len - 1, return such cnt
     *
     *
     *   IDEA 2) DP ????
     *
     */
    // IDEA: DP
    public int jump(int[] nums) {
        // edge
        if(nums == null || nums.length == 0){
            return 0;
        }
        if(nums.length == 1){
            return 0;
        }

        // dp[i]: the `min jump` that can reach `i` ???
        int[] dp = new int[nums.length + 1]; // ???
        // init val
        Arrays.fill(dp, -1);
        /**
         *  dp equation:
         *
         *   dp[i] = Math.min(dp[i-1], dp[i] +1)
         *
         *
         */
        dp[0] = 0;
        if(nums[0] >= dp[1]){
            dp[1] = 1;
        }
        int max_jump_so_far = nums[0];
        int max_jump_so_far_idx = 0;

        for(int i = 1; i < nums.length -1; i++){
            if(nums[i] + i > max_jump_so_far){
                max_jump_so_far = nums[i] + i;
                max_jump_so_far_idx = i;
            }

            dp[i] = Math.min(dp[i-1], dp[max_jump_so_far_idx]);
        }


        return dp[nums.length - 1];
    }

    // IDEA: GREEDY
//    public int jump(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            return 0;
//        }
//
//        int cnt = 0; // ????
//        for(int i = 0; i < nums.length; i++){
//            if(i + nums[i] >= nums.length - 1){
//                return cnt;
//            }
//        }
//
//        // if can ONLY reach the end when visit the last element
//        // means we need to jump `nums.len - 1` times
//        return nums.length - 1; // ???
//    }

    // LC 1871
    public boolean canReach(String s, int minJump, int maxJump) {

        return false;
    }

    // LC 134
    // 9.52 - 10.02 am
    /**
     *
     *
     * ->  return the starting gas station's index
     *  if you can travel around the circuit once
     *  in the `clockwise` direction,
     *  otherwise return -1
     *
     *
     *  IDEA: GREEDY
     *
     *
     *
     *
     */
    public int canCompleteCircuit(int[] gas, int[] cost) {
        // edge
        if(gas == null || gas.length == 0){
            return 0;
        }
        if(gas.length == 1){
            return 0;
        }

        // double array, to simulate `circular`
        List<Integer> gas_ = new ArrayList<>();
        List<Integer> cost_ = new ArrayList<>();

        for(int i = 0; i < 2; i++){
            for(int j = 0; j < gas.length; j++){

                gas_.add(gas[j]);
                cost_.add(gas[j]);
            }
        }

        for(int i = 0; i < gas.length; i++){

            HashSet<Integer> visited = new HashSet<>();
            int gas_val = gas[i];

            for(int j = i+1; j < gas_.size(); j++){

                gas_val = (gas_val - cost_.get(j) + gas_.get(i));

                if(gas_val < 0){
                    break;
                }

                int adjusted_idx = j % gas.length;

                if(visited.contains(adjusted_idx)){
                    return adjusted_idx;
                }

                visited.add(adjusted_idx);
            }
        }

        return -1;
    }


    // LC 846
    // 10.34 - 10.44 am
    /**
     *   ->  she wants to `rearrange`
     *   the `cards into group`s so
     *   that each group is of size groupSize,
     *   and consists of groupSize `consecutive` cards.
     *
     *
     *  consecutive cards !!!!
     *
     *  IDEA 1) BRUTE FORCE
     *
     *
     *  exp 1)
     *
     *   hand = [1,2,3,6,2,3,4,7,8], groupSize = 3
     *
     *   -> sort,
     *      [1,2,2,3,3,4,6,7,8]
     *
     *
     *   -> unique:
     *      [1,2,3,4,6,7,8]
     *
     *   -> sum = 36,
     *   -> avg = 12
     *
     *   -> group
     *       [1,2, 3]
     *       [2, 3, 4]
     *       [6,7,8]
     *
     *
     *
     */
    // 12.34 - 12.44 PM
    // IDEA: HASHSET + PQ + MAP

    public boolean isNStraightHand(int[] hand, int groupSize) {
        // edge
        if (hand.length % groupSize != 0){
            return false;
        }

        HashSet<Integer> set = new HashSet<>();

        // get element cnt
        Map<Integer, Integer> cnt_map = new HashMap<>();
        for(int x: hand){

            cnt_map.put(x, cnt_map.getOrDefault(x, 0) + 1);

            set.add(x);
        }

        // small PQ
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for(int x: set){
            pq.add(x);
        }

        while(!pq.isEmpty()){

            int val = pq.peek();

            for(int i = 0; i < groupSize; i++){
                int new_val = val + i;
                if(!cnt_map.containsKey(new_val) || cnt_map.get(new_val) <= 0){
                    return false;
                }

                cnt_map.put(cnt_map.get(new_val), cnt_map.get(new_val) - 1);

                if(cnt_map.get(new_val) == 0){
                    cnt_map.remove(new_val);
                }

                if(cnt_map.get(val) == 0){
                    pq.poll();
                }

            }
        }


        return true;
    }



//   // public boolean isNStraightHand(int[] hand, int groupSize) {
//        return false;
//    }
//    // greedy
//    public boolean isNStraightHand(int[] hand, int groupSize) {
//        // edge
//        if(hand == null){
//            return true;
//        }
//        if(hand.length == 0 && groupSize == 0){
//            return true;
//        }
//        if(hand.length == 0 && groupSize > 0){
//            return false;
//        }
//        if(hand.length % groupSize != 0){
//            return false;
//        }
//
//        // greedy
//
//        // map : {k : cnt}
//        Map<Integer, Integer> map = new HashMap<>();
//
//        Integer[] hand_ = new Integer[hand.length];
//        for(int i = 0; i < hand.length; i++){
//            hand_[i] = hand[i];
//
//            map.put(hand[i], map.getOrDefault(hand[i], 0) + 1);
//        }
//
//        // sort (small -> big)
//        Arrays.sort(hand_, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                int diff = o1 - o2;
//                return diff;
//            }
//        });
//
//        for(int i = 0; i < hand_.length; i++){
//
//            if(map.isEmpty()){
//                return true;
//            }
//
//            int cur = hand_[i];
//            // get next 2 val
//            int next_1 = cur + 1;
//            int next_2 = cur + 2;
//
//            System.out.println("cur = " + cur + ", next_1 = " + next_1 + ", !map.containsKey(next_1) || !map.containsKey(next_2) = " + (!map.containsKey(next_1) || !map.containsKey(next_2)));
//
//            if(!map.containsKey(next_1) || !map.containsKey(next_2)){
//                return false;
//            }
//
//            // update map
//            map.put(next_1, map.get(next_1) - 1);
//            map.put(next_2, map.get(next_2) - 1);
//
//            // remove key is cnt == 0
//            if(map.get(next_1) == 0){
//                map.remove(next_1);
//            }
//
//            if(map.get(next_2) == 0){
//                map.remove(next_2);
//            }
//
//        }
//
//
//        return true;
//    }

    // LC 649
    // 12.00 - 12.10 pm
    /**
     *  IDEA 1) QUEUE ??
     *
     *
     *  exp 1)
     *
     *   input = "RD"
     *    -> q = [r, d], cant_vote = []
     *    ->  q = [d], cant_vote = [d]
     *    -> q = [],
     *
     *
     *
     */
    public String predictPartyVictory(String senate) {

        // edge
        if(senate == null || senate.length() == 0){
            return null; // ???
        }
        if(senate.length() == 1){
            return String.valueOf(senate.charAt(0));
        }

        Queue<Integer> q_r = new LinkedList<>();
        Queue<Integer> q_d = new LinkedList<>();

        List<Integer> disable_list = new ArrayList<>();

        for(int i = 0; i < senate.split("").length; i++){

            String cur = senate.split("")[i];

            if(!disable_list.contains(i)){
                if(cur.equals("R")){
                    q_r.add(i);
                }else{
                    q_d.add(i);
                }
            }

        }


        return q_r.size() > q_d.size() ? "Radiant" : "Dire";
    }

    // LC 1899
    // 12.04 - 12.14 pm
    /**
     *
     * Return true if it is possible to obtain the
     * target triplet [x, y, z]
     * as an element of triplets, or false otherwise.
     */
    public boolean mergeTriplets(int[][] triplets, int[] target) {
        // edge
        if(triplets == null || triplets.length == 0){
            return false;
        }

        List<int[]> candidates = new ArrayList<>();

        // filer out the `not valid arr`
        for(int[] x: triplets){
            boolean canAdd = true;
            for(int i = 0; i < x.length; i++){
                if(x[i] > target[i]){
                    canAdd = false;
                    break;
                }
            }
            if(canAdd){
                candidates.add(x);
            }
        }

        // check if we can get the amount of elements that has same size as target
        // (hashset size)
        // NOTE : set {idx of val} ( to deal with `same val but different idx`)
        HashSet<Integer> set = new HashSet<>();

        for(int[] x: candidates){
            for(int i = 0; i < x.length; i++){
                if(x[i] == target[i]){
                    //set.add(x[i]);
                    set.add(i);
                }
            }
        }

        return set.size() == target.length;
    }


//    public boolean mergeTriplets(int[][] triplets, int[] target) {
//
//        // edge
//        if(triplets == null || triplets.length == 0){
//            return false;
//        }
//
//        List<int[]> collected = new ArrayList<>();
//
//        // only collect the `qualified` array
//        for(int[] x: triplets){
//            boolean qualified = true;
//            for(int i = 0; i < x.length; i++){
//                if(x[i] > target[i]){
//                    qualified = false;
//                    break;
//                }
//            }
//
//            if(qualified){
//                collected.add(x);
//            }
//        }
//
//        HashSet<Integer> set = new HashSet<>();
//
//        // check if `qualified` array can form target
//        for(int[] x: collected){
//            for(int i = 0; i < x.length; i++){
//                if(x[i] == target[i]){
//                    set.add(i);
//                }
//            }
//        }
//
//        return set.size() == target.length;
//    }

    // LC 763
    // 4.14 - 4.24 pm
    /**
     *  IDEA 1) GREEDY + hashmap + sliding window
     *
     *  -> use `hash map` records
     *  the `last exist idx`  for every element
     *  loop over input str, start the other sub str
     *  if `all last idx` is met for elements in current window
     *
     *
     */
    public List<Integer> partitionLabels(String s) {

        List<Integer> res = new ArrayList<>();

        // edge
        if(s == null || s.length() == 0){
            return res;
        }
        if(s.length() == 1){
            res.add(1);
            return res;
        }

        Map<String, Integer> lastIdxMap = new HashMap<>();
        String[] s_arr = s.split("");
        for(int i = 0; i < s_arr.length; i++){
            lastIdxMap.put(s_arr[i], i);
        }

        int curMaxIdx = 0; // ???
        int l = 0; // ??
        for(int r = 0; r < s_arr.length; r++){
            //int r = i;
            String val = s_arr[r];
            curMaxIdx = Math.max(lastIdxMap.get(val), curMaxIdx);
            if(curMaxIdx == r){
                res.add(r - l + 1);
                // update l
                l = r + 1; // ??
            }

        }

        return  res;
    }



//    public List<Integer> partitionLabels(String s) {
//
//        List<Integer> res = new ArrayList<>();
//
//        // edge
//        if(s == null || s.length() == 0){
//            return res;
//        }
//        if(s.length() == 1){
//            res.add(1);
//            return res;
//        }
//
//        // hashmap
//        // {k : last_idx}
//        Map<String, Integer> map = new HashMap<>();
//        String[] s_arr = s.split("");
//
//
//        for(int i = 0; i < s_arr.length; i++){
//            //String cur = s.split("")[i];
//            map.put(s_arr[i], i);
//        }
//
//        System.out.println(">>> map = " + map);
//
//        int l = 0;
//        int r = 0;
//
//        while(l < s.length() && r < s.length()){
//            Map<String, Integer> tmpMap = new HashMap<>();
//            if(canSplit(map, tmpMap)){
//                //res.add()
//                res.add(r - l + 1);
//                tmpMap = new HashMap<>();
//                l = r + 1;
//            }
//
//            tmpMap.put(s_arr[r], r);
//            r += 1;
//        }
//
//        return res;
//    }
//
//    public boolean canSplit(Map<String, Integer> map, Map<String, Integer> tmpMap){
//
//        for(String k: tmpMap.keySet()){
//            if(map.get(k) > tmpMap.get(k)){
//                return false;
//            }
//        }
//
//        if(tmpMap.isEmpty()){
//            return false;
//        }
//
//        return true;
//    }

    // LC 678
    // 5.07 - 5.22 pm
    /**
     *  IDEA 1) GREEDY + QUEUE
     *
     *
     *
     *
     */
    public boolean checkValidString(String s) {
        // edge
        if(s == null || s.length() == 0){
            return true;
        }
        if(s.length() == 1){
            if(s != "*"){
                return false;
            }
        }

        Queue<String> leftParen = new LinkedList<>();
        Queue<String> rightParen = new LinkedList<>();
        Queue<String> starParen = new LinkedList<>();

        String[] s_arr = s.split("");

        for(int i = 0; i < s_arr.length; i++){
            String cur = s_arr[i];
            if(cur.equals("(")){
                leftParen.add(cur);
            }else if (cur.equals(")")){
                if(!leftParen.isEmpty()){
                    leftParen.poll();
                }else if(!starParen.isEmpty()){
                    starParen.poll();
                }else{
                    return false;
                }
            }else{
                starParen.add(cur);
            }
        }

        //return leftParen.isEmpty() && rightParen.isEmpty();
        return leftParen.isEmpty();
    }

  // LC 135
  // 3.05 - 3.15 pm
  /**
   * 1. Each child must have at least one candy.
   *
   * <p>2. Children with a higher rating get more candies than their neighbors.
   *
   * <p>-> Return the `minimum` number of candies you need to have to distribute the candies to the
   * children.
   *
   * <p>IDEA 1) GREEDY
   *
   * <p>1. init val as 1 2. loop from `right hand side`, compare current idx with its neighbor, - if
   * same, do nothing - if bigger than neighbors, val += 1 -
   */
  public int candy__(int[] ratings) {

    // edge
    if (ratings == null || ratings.length == 0) {
      return 0;
    }
    if (ratings.length == 1) {
      return 1;
    }

    int[] arr = new int[ratings.length];
    // init val as 1
    Arrays.fill(arr, 1);

    // update
    for (int i = ratings.length; i >= 1; i--) {
      if (ratings[i] == ratings[i - 1]) {
        continue;
      } else if (i < ratings.length - 1) {
        if (ratings[i] < ratings[i + 1] && ratings[i] > ratings[i - 1]) {
          continue;
        } else {

        }
      }
    }

    return getSumFromArr(arr);
    }

  public int getSumFromArr(int[] input) {
    int res = 0;
    for (int x : input) {
      res += x;
    }
    return res;
  }

  // LC 57
  // 11.50 - 12.00 pm
  public int[][] insert(int[][] intervals, int[] newInterval) {
      // edge
      if (intervals.length == 0){
          if (newInterval.length == 0){
              return new int[][]{};
          }
          return new int[][]{newInterval};
      }

      List<int[]> intervals_ = new ArrayList<>();
      for(int[] x: intervals){
          intervals_.add(x);
      }

      // sort
      // 1st element (small -> big)
      Collections.sort(intervals_, new Comparator<int[]>() {
          @Override
          public int compare(int[] o1, int[] o2) {
              int diff = o1[0] - o2[0];
              return diff;
          }
      });

      List<int[]> collected = new ArrayList<>();

      /**
       *
       *  NON OVERLAP case:
       *
       *   |-----| old
       *            |------| new
       *
       */
      for(int i = 0; i < intervals_.size(); i++){
          // case 1) idx = 0 or NOT overlap
          if(collected.isEmpty() || collected.get(collected.size() - 1)[1] < intervals_.get(i)[0]){
              collected.add(intervals_.get(i));
          }
          // case 2) overlap
          else{
              collected.get(collected.size() - 1)[0] = Math.min( collected.get(collected.size() - 1)[0],  intervals_.get(i)[0]);
              collected.get(collected.size() - 1)[1] = Math.max( collected.get(collected.size() - 1)[1],  intervals_.get(i)[1]);
          }
      }


      return collected.toArray(new int[collected.size()][]);
  }


//  public int[][] insert(int[][] intervals, int[] newInterval) {
//
//      if (intervals.length == 0){
//          if (newInterval.length == 0){
//              return new int[][]{};
//          }
//          return new int[][]{newInterval};
//      }
//
//      List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));
//      intervalList.add(newInterval);
//
//      // sort
//      Collections.sort(intervalList, new Comparator<int[]>() {
//          @Override
//          public int compare(int[] o1, int[] o2) {
//              int diff = o1[0] - o2[0];
//              return diff;
//          }
//      });
//
//      List<int[]> collected = new ArrayList<>();
//
//      // loop over intervalList
//      for(int i = 0; i < intervals.length; i++){
//          // idx = 1 or NOT overlap
//          if(collected.isEmpty() || (collected.get(collected.size() - 1)[1] < intervalList.get(i)[0]) ){
//              collected.add(intervals[i]);
//          }
//          // overlap
//          else{
//              collected.get(collected.size() - 1)[0] = Math.min(collected.get(collected.size() - 1)[0], intervalList.get(i)[0]);
//              collected.get(collected.size() - 1)[1] = Math.max(collected.get(collected.size() - 1)[1], intervalList.get(i)[1]);
//          }
//      }
//
//      // list to array
//      //int[][] test = collected.toArray(new int[collected.size()][2]);
//
//      // ??
//      return collected.toArray(new int[collected.size()][]);
//  }


//  public int[][] insert(int[][] intervals, int[] newInterval) {
//
//    // edge
//    if (intervals == null || intervals.length == 0) {
//      return new int[][] {newInterval};
//    }
//    if (newInterval == null || newInterval.length == 0) {
//      return intervals;
//    }
//
//    // add newInterval to intervals
//    List<int[]> cache = new ArrayList<>();
//    cache.add(newInterval);
//    for(int[] x: intervals){
//        cache.add(x);
//    }
//
//    /**
//     *  Case of `overlap`
//     *
//     *  case 1)
//     *
//     *    |-----|  old
//     *      |--------| new
//     *
//     *  case 2)
//     *
//     *    |-------|  old
//     *      |--|     new
//     *
//     *  case 3)
//     *
//     *   ..???
//     */
//    /**
//     *  NOTE !!!
//     *
//     *  since the array ALREADY sorted in 1st element (small -> big)
//     *  there is ONLY 1 case that the intervals are NOT OVERLAPPED
//     *
//     *   e.g.
//     *
//     *      |------|  old
//     *               | ------- | new
//     *
//     *
//     */
//    // sort (`first element`) small -> big
//    Collections.sort(cache, new Comparator<int[]>() {
//        @Override
//        public int compare(int[] o1, int[] o2){
//            int diff = o1[0] - o2[0];
//            return diff;
//        }
//    });
//
//    List<int[]> cache2 = new ArrayList<>();
//
//    // merge
//    for(int i = 0; i < cache.size(); i++){
//        // if i = 0, append directly
//        if(i == 0){
//            cache2.add(cache.get(i));
//        }else{
////            // if overlap, add the (min, max)
////            int[] last = cache2.get(cache2.size() - 1);
////            if( (last[1] > cache.get(i)[0] && last[1] < cache.get(i)[1]) || (last[1] < cache.get(i)[0] && last[1] > cache.get(i)[1])){
////                cache2.remove(cache2.size() - 1);
////                //cache2 (cache2.size() - 1 ) = new int[] {1,2};
////                cache2.add(new int[] { Math.min( cache.get(i)[0], last[0]),  Math.max( cache.get(i)[1], last[1]) } );
////            }else{
////                cache2.add(cache.get(i));
////            }
//
//            // if NOT overlap, append directly
//            int[] last = cache2.get(cache2.size() - 1);
//            if(last[1] < cache.get(i)[0]){
//                cache2.add(cache.get(i));
//            }
//            // if overlap
//            else{
//                cache2.add(new int[] { Math.min( cache.get(i)[0], last[0]),  Math.max( cache.get(i)[1], last[1]) } );
//            }
//        }
//    }
//
//    int[][] res = new int[2][cache2.size()];
//    for(int i = 0; i < cache2.size(); i++){
//        res[i] = cache2.get(i);
//    }
//
//    return res;
//  }

  // LC 56
  // 3.56 - 4.07
  public int[][] merge(int[][] intervals) {

      if (intervals == null || intervals.length == 0){
          return null;
      }

      List<int[]> intervalList = new ArrayList<>(Arrays.asList(intervals));

      // sort
      intervalList.sort(Comparator.comparingInt(a -> a[0]));

      List<int[]> merged = new ArrayList<>();

      for (int[] x : intervalList){


          if (merged.isEmpty() || merged.get(merged.size()-1)[1] < x[0]){
              merged.add(x);
          }
          // case 3) if overlapped, update boundary
          else{
              /**
               *  if overlap
               *   last : |-----|
               *   x :      |------|
               */
              // NOTE : we set 0 idx as SMALLER val from merged last element (0 idx), input
              merged.get(merged.size()-1)[0] = Math.min(merged.get(merged.size()-1)[0], x[0]);
              // NOTE : we set 1 idx as BIGGER val from merged last element (1 idx), input
              merged.get(merged.size()-1)[1] = Math.max(merged.get(merged.size()-1)[1], x[1]);
          }
      }

      return merged.toArray(new int[merged.size()][]);
  }


  // LC 435
  // 10.03 - 10.13 am
  /**
   *
   *  ->
   *    return the `minimum` number of intervals
   *    you need to remove to make the rest
   *       of the intervals non-overlapping.
   *
   *
   *  IDEA 1) INTERVAL OP
   *
   *
   *
   *
   * exp 1)
   *
   *  intervals = [[1,2],[2,3],[3,4],[1,3]]
   *
   *
   *  -> sort ( 1st element : small -> big), then (2nd element : big -> small)
   *
   */
  public int eraseOverlapIntervals(int[][] intervals) {
      // edge
      if(intervals == null || intervals.length == 0){
          return 0;
      }
      if(intervals.length == 1){
          return 0;
      }

      List<int[]> intervals2 = new ArrayList<>();
      for(int[] x : intervals){
          intervals2.add(x);
      }

      int cnt = 0;

      // sort
      //  -> sort ( 1st element : small -> big), then (2nd element : big -> small)
      Collections.sort(intervals2, new Comparator<int[]>() {
          @Override
          public int compare(int[] o1, int[] o2) {
              int diff = o1[0] - o2[0];
              if(diff == 0){
                  return o2[1] - o1[1];
              }
              return diff;
          }
      });

      /**
       *  since we already sort on `first element small -> big`
       *                        and `2nd element big -> small`
       *
       *  -> the ONLY possible OVERLAP case is as below:
       *
       *     |----------|   old
       *     1          3
       *
       *    |------|        new
       *    1      2
       *
       */

      List<int[]> collected = new ArrayList<>();

      for(int i = 0; i < intervals2.size(); i++){
          // case 1) idx = 0 or NOT overlap
          if(i == 0  || collected.get(collected.size() - 1)[1] >= intervals2.get(i)[1]){
              collected.add(intervals2.get(i));
          }
          // case 2) overlap
          else{
              cnt += 1;
              // append the interval with `smaller window` back to collected
              collected.get(collected.size()-1)[0] = intervals2.get(i)[0];
              collected.get(collected.size()-1)[1] = intervals2.get(i)[1];
          }
      }

      return cnt;

  }

  // LC 252
  // 10.46 - 10.56 am
  // IDEA: SORT + GREEDY
  // IDEA: SORT + SCANNING LINE
  public boolean canAttendMeetings(int[][] intervals) {
      // edge
      if(intervals == null || intervals.length == 0){
          return true;
      }
      if(intervals.length == 1){
          return true;
      }

      /**
       * intervals_ : [time, status]
       *
       *  time: `the meeting room timestamp`
       *        start, end timestamp are included
       *
       *  status : 1 (open)
       *         : -1 (closed)
       *
       */
      List<int[]> intervals_ = new ArrayList<>();
      for(int[] x: intervals){
          intervals_.add(new int[]{x[0], 1});
          intervals_.add(new int[]{x[1], -1});
      }

      // sort : 1st element (small -> big)
      Collections.sort(intervals_, new Comparator<int[]>() {
          @Override
          public int compare(int[] o1, int[] o2) {
              int diff = o1[0] - o2[0];
              if(diff == 0){
                  return o1[1] - o2[1];
              }
              return diff;
          }
      });

      int curRoomCnt = 0;
      int maxRoomCnt = 0;

      // scanning line
      for(int[] x: intervals_){
          if(x[1] == 1){
              curRoomCnt += 1;
          }else{
              curRoomCnt -= 1;
          }
          maxRoomCnt = Math.max(maxRoomCnt, curRoomCnt);
      }

      return maxRoomCnt <= 1;
  }

  // LC 2402
  //  12.31 - 12.41 pm
  /**
   *
   * You are given an integer n.
   * There are n rooms numbered from 0 to n - 1.
   *
   * -> Return the number of the room that held
   *   the `most meetings.`
   *
   *  -> If there are multiple rooms,
   *   return the room with the lowest number.
   *
   *
   *
   *  IDEA 1) PQ   +  hast_map (meeting hold cnt)
   *
   *   1st PQ : ready_to_use_pq : [ [ room_idx, meeting_end_time ] ],  storage the rooms are ready to be used
   *   2nd PQ : busy_pq ?? :  [ [ room_idx, meeting_end_time ]  ],  rooms are being used now
   *   3nd queue : to_hold_meeting :  [ meet_start_time, meet_end_time], the `to hold` meeting at the timestamp
   *
   *
   *   exp 1) Input: n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]
   *
   *    rooms : [0, 1]
   *
   *
   *   -> res = 0 (room 0 hold MOST meetings)
   *
   *    t = 0, room = 0 is used, ready_to_use_pq = [1], busy_pq = [ [0, 10] ]
   *    t = 1, room = 0 is still used, room = 1 is used, ready_to_use_pq = [], busy_pq = [ [0,10], [1, 6] ]
   *    t = 2, both room are busy, to_hold_meeting = [ [2,7] ]
   *    t = 3, both room are busy, to_hold_meeting = [ [2,7], [3,4] ]
   *    ...
   *
   *    t = 6, room = 0 is still used, room=1 is free, ready_to_use_pq  = [1], busy_pq = [ [0,10]]
   *                                  room=1 hold a new meeting, to_hold_meeting = [ [3,4] ], busy_pq = [ [0,10], [1, 11] ]
   *
   *
   *   ...
   *
   *   t=10, room=0 is free, room=1 still busy
   *                         room=0 hold a new meeting, to_hold_meeting = [], busy_pq = [ [0,11], [1, 11] ]
   *
   *
   *
   *
   *
   */
  public int mostBooked(int n, int[][] meetings) {

      // edge
      if(meetings == null || meetings.length == 0){
          return 0;
      }
      if(n == 0){
          return 0;
      }

      // init
      // cnt_map : room hold meeting cnt : { room_idx: hold_meeting_cnt }
      Map<Integer, Integer> cnt_map = new HashMap<>();

      // to_hold_meet_queue :  queue, the list of meetings to be hold
      // [ [meet_start_time, meet_end_time]  ]
      Queue<int[]> to_hold_meet_queue = new LinkedList<>();

      // ready_to_use_pq
      // [ [room_idx] ]
      PriorityQueue<Integer> ready_to_use_pq = new PriorityQueue<>(
              new Comparator<Integer>() {
                  @Override
                  public int compare(Integer o1, Integer o2) {
                      int diff = o1 - o2;
                      return diff;
                  }
              }
      );

      // busy PQ
      PriorityQueue<Integer[]> busy_pq = new PriorityQueue<>(new Comparator<Integer[]>() {
          /**
           *  sort
           *
           *  1) sort on end time (small -> big)ready_to_use_pq
           *  2) room_idx (small -> big)
           */
          @Override
          public int compare(Integer[] o1, Integer[] o2) {
              int diff = o1[1] - o2[1];
              if(diff == 0){
                  return o1[0] - o2[0];
              }
              return diff;
          }
      });

      for(int i = 0; i < n; i++){
          cnt_map.put(i, 0);
      }

      int t = 0;

      for(int[] m: meetings){

          int cur_start = m[0];
          int cur_end = m[1];

          // check if there is a meeting finished
          while (t >= busy_pq.peek()[1]){
              Integer[] room = busy_pq.poll();
              ready_to_use_pq.add(room[0]);
          }

          // if the meeting reach the `start time`
          if(t > cur_start){
              to_hold_meet_queue.add(m);
          }

          while (!ready_to_use_pq.isEmpty()){
              // ???
              // move `time faster`, till the next meeting start time
              t += to_hold_meet_queue.peek()[0];
          }

          // hold new meeting with a room
          //int[]

          int[] new_meeting = to_hold_meet_queue.poll();
          int new_room = ready_to_use_pq.poll();
          busy_pq.add(new Integer[]{new_room, t + new_meeting[1]});
          cnt_map.put(new_room, cnt_map.getOrDefault(new_room, 0) + 1);

      }

      int cnt = 0;
      int res = 0;
      for(int k : cnt_map.keySet()){
          if(cnt_map.get(k) > cnt){
              cnt = cnt_map.get(k);
              res = k;
          }
      }

      return res;
  }





  // LC 1851
  // 10.04 - 10.14 am
  /**
   *
   * The answer to the jth query
   *  is the size of the smallest interval i such that
   *   lefti <= queries[j] <= righti.
   *  If no such interval exists, the answer is -1.
   *
   *
   */
  public int[] minInterval(int[][] intervals, int[] queries) {
      return null;
  }

  // LC 168
  // 12.26 - 12.36 pm
//  public String convertToTitle(int columnNumber) {
//
//      String res = "";
//      columnNumber -= 1;
//
//      // ???
//      int cnt = 0;
//      while(columnNumber / 26 > 26){
//
//          columnNumber = (columnNumber % 26);
//
//
//      }
//
//      return res;
//    }



//  public String convertToTitle(int columnNumber) {
//      // edge
//      if(columnNumber == 0){
//          return null; // ??
//      }
//      /**
//       * transform columnNumber to
//       *
//       *  26 ^ k + 26 ^ (k-1) + ... 26
//       *
//       *  and find the val
//       *
//       */
//      List<Integer> cache = new ArrayList<>();
//      while(columnNumber >= 26){
//         // columnNumber = columnNumber /
//      }
//      return null;
//
//  }


  // LC 1071
  public String gcdOfStrings(String str1, String str2) {

      return null;
  }

  // LC 2807
  // 7.56 - 8.06 pm
  /**
   *  IDEA 1) listNode -> list, add `gcd`, then list -> listNode
   *
   *  IDEA 2) listNode, add `gcd` in place
   *
   *
   */
  // IDEA 1)
//  public ListNode insertGreatestCommonDivisors_1(ListNode head) {
//      // edge
//      if(head == null || head.next == null){
//          return head;
//      }
//
//      // ListNode -> list
//      List<Integer> list = new ArrayList<>();
//      while(head != null){
//          list.add(head.val);
//          head = head.next;
//      }
//
//      List<Integer> cache = new ArrayList<>();
//
//      // add `gcd`
//      for(int i = 0; i < list.size() - 1; i++){
//          cache.add(list.get(i));
//          //int gcd = getGCD(list.get(i), list.get(i+1));
//          cache.add(getGCD(list.get(i), list.get(i+1)));
//      }
//
//      cache.add(list.get(list.size() - 1));
//
//      ListNode node = new ListNode();
//      ListNode res = node;
//
//      for(int x: cache){
//          node.next = new ListNode(x);
//          node = node.next;
//      }
//
//      return res.next;
//  }

  public ListNode insertGreatestCommonDivisors(ListNode head) {
      // edge
      if(head == null || head.next == null){
          return head;
      }

//      // ListNode -> list
//      List<Integer> list = new ArrayList<>();
//      while(head != null){
//          list.add(head.val);
//          head = head.next;
//      }
//
//      List<Integer> cache = new ArrayList<>();
//
//      // add `gcd`
//      for(int i = 0; i < list.size() - 1; i++){
//          cache.add(list.get(i));
//          //int gcd = getGCD(list.get(i), list.get(i+1));
//          cache.add(getGCD(list.get(i), list.get(i+1)));
//      }
//
//      cache.add(list.get(list.size() - 1));

      //ListNode node = new ListNode();
      ListNode head2 = head;
      ListNode res = new ListNode();
      res = head2;
//
//      for(int x: cache){
//          node.next = new ListNode(x);
//          node = node.next;
//      }

      while(head2 != null && head2.next != null){

          // get gcd
          int gcd = getGCD(head2.val, head2.next.val);

          /**
           *  before:
           *
           *     2 -> 8 -> 9
           *
           *
           *  after:
           *
           *    2 -> `2` -> 8 -> 9
           *
           *    so, we need to
           *     1) cache `next` node (aka 8)
           *     2) re-connect 2 to `2` node
           *     3) connect `2` node to 8 node
           *     4) move `2` node to 8 node
           */
          // cache original `next`
          ListNode _next = head2.next;
          // reconnect to gcd
          ListNode gcdNode = new ListNode(gcd);
          head2.next = gcdNode;
          gcdNode.next = _next;
          head2 = _next; // ???
      }

      return res.next;
  }

  public int getGCD(int x, int y){
      if (x == y){
          return x;
      }
      int res = 1;
      int end = Math.min(x, y);
      //while()
      for(int i = 1; i <= end; i++){
          if(x % i == 0 && y % i == 0){
              res = Math.max(res, i);
          }
      }

      return res;
  }

  // LC 867
  // 10.20 - 10.30 am
  public int[][] transpose(int[][] matrix) {
      // edge
      if(matrix == null || matrix.length == 0 || matrix[0].length == 0){
          return null;
      }
      int l = matrix.length;
      int w = matrix[0].length;

      System.out.println(">>> before op : " + matrix);

      // NOTE !!! go `half` to the idx + double loop
      for(int i = 1; i < l; i++){
          for(int j = 0; j <= w / 2; j++){
              int cache = matrix[i][j];
              matrix[i][j] = matrix[j][i];
              matrix[j][i] = cache;
          }
      }

      System.out.println(">>> after op : " + matrix);

      return matrix;
  }


  // LC 66
  public int[] plusOne(int[] digits) {
      // edge
      if(digits == null || digits.length == 0){
          return new int[]{1};
      }

      List<Integer> cache = new ArrayList<>();
      int plus = 0;
      for(int i = digits.length - 1; i >= 0; i--){
          int val = digits[i];
          if(i == digits.length - 1){
              val += 1;
          }
          val += plus;
          if(val > 9){
              val -= 10;
              plus = 1;
          }else{
              plus = 0;
          }
          cache.add(val);
      }

      // handle `last plus one`
      if(plus > 0){
          cache.add(plus);
      }

      // reverse
      Collections.reverse(cache);

      int[] res = new int[cache.size()];
      for(int i = 0; i < cache.size(); i++){
          res[i] = cache.get(i);
      }

      return res;
  }


//  public int[] plusOne(int[] digits) {
//      // edge
//      if(digits == null || digits.length == 0){
//          return new int[]{1};
//      }
//      StringBuilder sb = new StringBuilder();
//      for(int x: digits){
//          sb.append(x);
//      }
//
//      // to int
//      Long val = Long.parseLong(sb.toString());
//     // int val = sb.length();
//
//      val += 1L;
//      String val_str = String.valueOf(val);
//      String[] val_arr = val_str.split("");
//
//      int[] res = new int[val_arr.length];
//      for(int i = 0; i < val_str.length(); i++){
//          res[i] = Integer.parseInt(val_arr[i]);
//      }
//
//      return res;
//  }

    // LC 13
    // 11.48 - 58 am
    /**
     *  -> Given a roman numeral, convert it to an integer.
     *
     *
     *  IDEA 1) MATH
     *
     *   -> key : how to `split` the input string
     *
     *   -> loop `inverse` and `split` the string
     *      if num(i-1) > num(i)
     *
     *   -> caclute the corresponding int val
     *   -> sum all of above as res
     *
     */
    public int romanToInt(String s) {

        HashMap<Character, Integer> map = new HashMap();
        map.put('I', 1);
        map.put('V' ,5);
        map.put('X' ,10);
        map.put('L' ,50);
        map.put('C' ,100);
        map.put('D' ,500);
        map.put('M' ,1000);

        Stack<String> st = new Stack<>();
        for(String x: s.split("")){
            st.add(x);
        }

        List<String> cache = new ArrayList<>();

        int cnt = 0;

        while(!st.isEmpty()){
            if(cnt == 0){
                cache.add(st.pop());
            }
            // if `i-1` < `i`
            String prev = cache.get(cache.size()-1);
            if(map.get(st.peek()) < map.get(prev)){
                cache.remove(cache.size()- 1);
                cache.add( (prev + st.pop()));
            }else{
                cache.add( st.pop());
            }
        }

        int res = 0;

        // cache -> int
        for(String x: cache){

        }

        return res;
    }

    public int romanHelper(String x){

        int res = 0;

        HashMap<Character, Integer> map = new HashMap();
        map.put('I', 1);
        map.put('V' ,5);
        map.put('X' ,10);
        map.put('L' ,50);
        map.put('C' ,100);
        map.put('D' ,500);
        map.put('M' ,1000);

        String[] x_arr = x.split("");

        for(int i = 0; i < x_arr.length - 1; i++){
            if(map.get(x_arr[i]) < map.get(x_arr[i+1])){
              //  res += (-1 * x_arr[i]);
            }
        }


        return 0;
    }


    // LC 43
    // 10.10 - 10.20 am
    /**
     *  IDEA 1) str -> int
     *   -> sum them up
     *
     *  IDEA 2) bit op ???
     *
     */
    public String multiply(String num1, String num2) {

        return null;
    }

    // LC 2013
    // 10.25 - 10.35 am

    /**
     * Your DetectSquares object will be instantiated and called as such:
     * DetectSquares obj = new DetectSquares();
     * obj.add(point);
     * int param_2 = obj.count(point);
     */
    class DetectSquares {

        // attr
        //List<int[]> collected;
        HashSet<int[]> collected;
        int cnt;
        // { x1-y1 : 1, x1-y2 : 2, x2-y1: 2, ....}
        Map<String, Integer> coorCnt;

        public DetectSquares() {
            this.collected = new HashSet<>(); //new ArrayList<>();
            this.cnt = 0;
            coorCnt = new HashMap<>();
        }

        public void add(int[] point) {

            this.collected.add(point);

            String key =  point[0] + "-" + point[1];

//            coorCnt.put(point[0], coorCnt.getOrDefault(point[0], 0) + 1);
//            coorCnt.put(point[1], coorCnt.getOrDefault(point[1], 1) + 1);

            coorCnt.put(key, coorCnt.getOrDefault(key, 0) + 1);
        }

        public int count(int[] point) {

            // edge
            if(this.collected.size() < 3){
                return 0;
            }

            for(String k: coorCnt.keySet()){
                String[] tmp = k.split("-");
                int x = Integer.parseInt(tmp[0]);
                int y = Integer.parseInt(tmp[1]);

                // avoid `compare same point`
                if(point[0] == x && point[1] == y){
                    continue;
                }

                int input_x = point[0];
                int input_y = point[1];

                // check if `same distance`
                if(Math.abs(input_x - x) == Math.abs(input_y - y)){
                    // get the `possible square cnt`
                    String key_1 = x + "-" + input_y;
                    String key_2 = input_x + "-" + y;
                    //int newCnt = this.coorCnt.get(key_1) * this.coorCnt.get(key_2);
                    if(this.coorCnt.containsKey(key_1) && this.coorCnt.containsKey(key_2)){
                        this.cnt += this.coorCnt.get(key_1) * this.coorCnt.get(key_2);
                    }

                }

            }

            return this.cnt;
        }


    }

    // LC 918
    // 11.12 - 11. 22 am
    /**
     *  IDEA 1) BRUTE FORCE + maintain a `max_till_now` val
     *
     *
     */
    public int maxSubarraySumCircular(int[] nums) {

        /**
         *  NOTE !!!  define 4 var
         *
         *   cur_max
         *   cur_min
         *   max_sum
         *   min_sum
         *
         */
        int totalSum = 0;
        int maxSum = nums[0];
        int minSum = nums[0];
        int curMax = 0;
        int curMin = 0;

        for(int x: nums){

            curMax = Math.max(curMax + x, x);
            curMin = Math.min(curMin + x, x);

            // ????
//            maxSum = Math.max(maxSum, maxSum + curMax);
//            minSum = Math.min(minSum, minSum + curMin);

            maxSum = Math.max(maxSum, curMax);
            minSum = Math.min(minSum, curMin);
        }

        if(maxSum < 0){
            return maxSum;
        }

        return 0;
    }



//    public int maxSubarraySumCircular(int[] nums) {
//        // edge
//        if(nums == null || nums.length == 0){
//            return 0;
//        }
//        if(nums.length == 1){
//            return nums[0];
//        }
//
//        int max_till_now = nums[0];
//        int global_max = nums[0];
//
//        for(int i = 1; i < nums.length; i++){
//
//            int x = nums[i];
//            max_till_now = Math.max(max_till_now + x, max_till_now);
//
//            global_max = Math.max(Math.max(x, max_till_now), global_max);
//        }
//
//        return global_max;
//    }


    // LC 14
    // 12.05 - 12.15 pm
    // IDEA: STRING OP
    public String longestCommonPrefix(String[] strs) {
        // edge
        if(strs == null || strs.length == 0){
            return null;
        }
        if(strs.length == 1){
            return strs[0];
        }

        StringBuilder sb = new StringBuilder();
        //StringBuilder sb;
        // start with the str with `shortest` length

        List<String> list = new ArrayList<>();
        for(String x: strs){
            list.add(x);
        }

        // sort on str lenght (small -> big)
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int diff = o1.length() - o2.length();
                return diff;
            }
        });

        String first = list.get(0);
        int len = first.length();

        System.out.println(">>> first = "  + first);

        for(int i = 0; i < first.length(); i++){
            String tmp = String.copyValueOf(first.toCharArray(), 0, i);
            boolean isValid = true;
            System.out.println(">>> tmp = " + tmp);
            for(int j = 1; j < list.size(); j++){
                if(!list.get(j).startsWith(tmp)){
                    isValid = false;
                    break;
                }
            }
            // only append to res if `all` string has such prefix
            if(isValid){
                sb = new StringBuilder();
                sb.append(tmp);
            }
        }


        return sb.toString();
    }


    // LC 1027
    public int longestArithSeqLength(int[] nums) {

        return 0;
    }

    // LC 135
    // 7.27 - 7.37 pm
    /**
     *  IDEA: GREEDY
     *
     *  exp 1) [1,0,2]
     *
     *  -> init : [1,1,1]
     *
     *  -> (left -> right) (check left) (start_idx = 1)
     *
     *   [1, 1, 2]
     *
     *  -> (right -> left) (check right) (start_idx = ratings.len -2)
     *
     *  [2,1,2]
     *
     *
     *  exp 2) [1,2,2]
     *
     *  -> init : [1,1,1]
     *
     *  -> (left -> right) (check left) (start_idx = 1)
     *
     *  [1,2,1]
     *
     *  -> (right -> left) (check right) (start_idx = ratings.len -2)
     *
     *  [1,2,1]
     *
     *
     */
    public int candy(int[] ratings) {
        // edge
        if(ratings == null || ratings.length == 0){
            return 0;
        }
        if(ratings.length == 1){
            return 1;
        }

        // greedy
        int res = 0;

        int[] cache = new int[ratings.length];
        // init val as 1
        Arrays.fill(cache, 1);

        // step 1)  (left -> right) (check left) (start_idx = 1)
        for(int i = 1; i < ratings.length; i++){
            if(ratings[i] > ratings[i-1]){
                /**
                 *  NOTE !!! below
                 */
               // cache[i] += (cache[i] + 1);
                cache[i] = cache[i - 1] + 1;
            }
        }

        // step 2) (right -> left) (check right) (start_idx = ratings.len -2)
        for(int i = ratings.length -2; i >= 0; i--){
            if(ratings[i] > ratings[i+1]){
                // NOTE !!! below
                // ???
                int val = Math.max(cache[i], cache[i+1] + 1);
                cache[i] = val;
            }
        }


        // sum `cache` val
        for(int x: cache){
            res += x;
        }

        return res;
    }

  // LC 316
  // 1.47 - 1.57 pm
    /**
     *  NOTE
     *
     *  Lexicographically Smaller
     *
     * A string a is lexicographically smaller than a
     * string b if in the first position where a and b differ,
     * string a has a letter that appears earlier in the alphabet
     * than the corresponding letter in b.
     * If the first min(a.length, b.length) characters do not differ,
     * then the shorter string is the lexicographically smaller one.
     *
     */
  /**
   * -> Given a string s,
   * remove duplicate letters so that every letter appears once and only once.
   *
   *  (You must make sure your result Lexicographically Smaller)
   *
   *
   *  IDEA 1) GREEDY
   *
   *  IDEA 2) STACK
   *
   *   -> step 1) have a `smallest Lexicographically order` array
   *   -> step 2) map collect the element count
   *   -> step 3) loop over the ``smallest Lexicographically` array`
   *             maintain a res str
   *             if meet the element in str, then add it to res
   *             ...
   *
   *
   *   -> maintain a PQ (stack)
   *      that with
   *
   *
   */
  // 4.48 - 4.58 pm
  // IDEA: STACK
  /**
   *  IDEA 1) STACK
   *
   *  1. hashmap record `element cnt`
   *  2. boolean arr record `visited or not`
   *  3. PQ (heap) maintain the `smallest lexicographically` order
   *
   *  -> loop over s
   *  -> if 1) pq is NOT empty
   *        2) `top element` is `smaller` then current element
   *        3) `top element` cnt > 0  ????
   *
   *        -> if true, pop the element from PQ
   *    else
   *       append element (from s) to res
   *       update visited, cnt
   *
   */
  public String removeDuplicateLetters(String s) {
      // edge
      if(s == null || s.isEmpty()){
          return null;
      }
      if(s.length() == 1){
          return s;
      }


      // ???
      int[] freq = new int[26];
      for(char c: s.toCharArray()){
          // ???
          freq[c - 'a'] += 1;
      }

      // ???
      boolean[] sean = new boolean[26];

      // ???
      Stack<Character> stack = new Stack<>();



      Map<Character, Integer> cnt_map = new HashMap<>();
      boolean[] visited = new boolean[s.length()]; // ???

      for(Character c: s.toCharArray()){
          cnt_map.put(c, cnt_map.getOrDefault(c, 0) + 1);
      }

      // PQ, order in `smallest lexicographically` ???
      PriorityQueue<Character> pq = new PriorityQueue<>(new Comparator<Character>() {
          @Override
          public int compare(Character o1, Character o2) {
              int diff = o1 - o2;
              return diff;
          }
      });

      StringBuilder sb = new StringBuilder();

      String[] s_arr = s.split("");

      for(int i = 0; i < s_arr.length; i++){
          Character c = s.charAt(i);  // ??

          //if(visited)

          // ???
          while(!pq.isEmpty() &&  c < pq.peek() && cnt_map.get(c) > 0){
              Character tmp =  pq.peek();
              //cnt_map.put(c, )
              cnt_map.put(tmp, cnt_map.get(tmp) + 1);
          }

          sb.append(c);
          cnt_map.put(c, cnt_map.get(c) - 1);
          visited[i] = true;
      }

      return sb.toString();
  }




/*  public String removeDuplicateLetters(String s) {

      // edge
      if(s == null || s.length() == 0){
          return null;
      }
      if(s.length() == 1){
          return s;
      }

      // get `smallest Lexicographically order` array
      List<String> lexi_list = new ArrayList<>();
      // TODO: implement above

      StringBuilder sb = new StringBuilder();

      // loop over lexi_list, and compare with s
      // ????
      int last_idx = -1;
      for(String x: lexi_list){
          if(s.contains(x)){
              //last_idx = s.
              sb.append(x);
          }
      }

      return sb.toString();
  }*/

  // LC 543
  // 4.11 - 4.21 pm
  // IDEA: DFS
  /**
   *      *  NOTE !!!
   *      *
   *      *    the `diameter` is the `SUM Of DEPTHS` of sub left and sub right tree
   *      *
   *      *    ( This path may or may not pass through the root.)
   *
   *
   *   IDEA 1) DFS (bottom up)
   *
   *   -> we get `diameter` from bottom, then move up, get the `max diameter` in each layer
   *   -> repeat above ... till read the `root`
   *   -> then we know the `global biggest diameter` of the whole tree
   *
   *
   *
   *   exp 1)
   *         1
   *      2    3
   *    4   5
   *
   *
   *   max_dia = 0
   *
   *  -> dia(4) = 1
   *     dia(5) = 1
   *
   *     dia(2) = max(max_dia, left_h + right_h + 1) = 2 // ????
   *     dia(3) = 1
   *
   *     dia(1) = max(max_dia, left_h + right_h + 1)  = 2 + 1 = 3
   *
   */
  int maxDiameter = 0;

  public int diameterOfBinaryTree(TreeNode root) {
      // edge
      if(root == null){
          return 0;
      }
      if(root.left == null || root.right == null){
          return 0;
      }

      // dfs call
      diameterHelper(root);

      return maxDiameter;
  }

 public int diameterHelper(TreeNode root){
   if(root == null){
       return 0;
   }

//   TreeNode left = diameterHelper(root.left);
//   TreeNode right = diameterHelper(root.right);

   // NOTE !!! we get max diameter at below step
   //maxDiameter = Math.max(maxDiameter, right)

   int left_height = diameterHelper(root.left);
   int right_height = diameterHelper(root.right);

   maxDiameter = Math.max(maxDiameter, left_height + right_height);

   // NOTE !!! below
   return Math.max(left_height, right_height) + 1;
 }


//  public int diameterOfBinaryTree(TreeNode root) {
//      // edge
//      if(root == null){
//          return 0;
//      }
//      if(root.left == null || root.right == null){
//          return 0;
//      }
//
//      // dfs (one move `all left move`, the other move `all right move`)
//      // then add 2 dist as final res
//      int leftDist = diameterHelper(root.left, "left");
//      int rightDist = diameterHelper(root.right, "right");
//
//      return leftDist + rightDist;
//  }
//
//  public int  diameterHelper(TreeNode root, String dir){
//      // edge
//      if(root == null){
//          return 0;
//      }
//      if(root.left == null || root.right == null){
//          return 0;
//      }
//
//      //return 1 + diameterHelper(root.left) + diameterHelper(root.right);
//      if(dir == "left"){
//          return 1 + diameterHelper(root.left, dir);
//      }
//      return 1 + diameterHelper(root.right, dir);
//  }

  // LC 356
  // 3.29 - 3.39 pm
  /**
   * Given n points on a 2D plane,
   *
   * -> find if there is such
   * a line parallel to the y-axis
   * that reflects the given points symmetrically.
   *
   *
   * -> NOTE !!! the line needs to be parallel to y-axis
   *
   *
   * IDEA 1) HASHSET,
   *
   *  collect all points, and split by (x > 0, x < 0)
   *  -> check if all x > 0, x < 0 are `symmetric`
   *  -> symmetric : (x, y) VS (-x, y)
   *
   *
   */
  public boolean isReflected(int[][] points) {
      // edge
      if(points == null || points.length == 0){
          return true; // ??
      }
      if(points.length == 1){
          return false;
      }

      HashSet<int[]> left_set = new HashSet<>();
      HashSet<int[]> right_set = new HashSet<>();
      for(int[] p: points){
          if(p[0] > 0){
              right_set.add(p);
          }else{
              left_set.add(p);
          }
      }

      if(right_set.size() != left_set.size()){
          return false;
      }

      for(int[] x: left_set){
          int[] reflect_cell = new int[]{-1 * x[0], x[1]};
          if(!right_set.contains(reflect_cell)){
              return false;
          }
      }

      return true;
  }

  // LC 1836
  // 3.51 - 4.10 pm
  // NOTE !!! below is our custom listNode for this problem
  public class ListNode2 {
      int val;
      ListNode2 next;
      ListNode2() {}
      ListNode2(int val) { this.val = val; }
      ListNode2(int val, ListNode2 next) { this.val = val; this.next = next; }
  }
  /**
   *
   *  -> Given the head of a linked list,
   *  find all the values that `appear more than once` in the list
   *  and delete the nodes that have any of those values.
   *
   *
   *  Linked list -> hash set
   */
  public ListNode2 deleteDuplicatesUnsorted(ListNode2 head) {

      // edge
      if(head == null){
          return null;
      }
      /// ??
      if(head.next == null){
          return head;
      }

      HashSet<Integer> set = new HashSet<>();
      while(head != null){
          set.add(head.val);
          head = head.next;
      }

      ListNode2 node = new ListNode2();
      ListNode2 res = node;

      while(set.iterator().hasNext()){
          res = new ListNode2(set.iterator().next());
          res = res.next;
      }

      return res.next;
  }

  // LC 498
  // 10.06 - 10.22 am
  /**  IDEA : MATRIX OP
   *
   *  -> (0,0) -> (0, 1) -> (1,1)
   *
   *   start -> move right 1 idx -> move (+1, +1) till reach the boundary
   *   -> move down 1 idx -> move (+1, -1) till reach the boundary
   *
   *   -> repeat above ...
   *
   */
  public int[] findDiagonalOrder(int[][] mat) {
      // edge
      if(mat == null || mat.length == 0 || mat[0].length == 0){
          return null;
      }

      int l = mat.length;
      int w = mat[0].length;

      int[] res = new int[l * w];

      int cnt = 0;

      // move
      int x = 0;
      int y = 0;
      while(cnt < l * w){

          // step 1) append `start` cell
          res[cnt] = mat[y][x];

          // step 2) move right 1 idx
          x += 1;
          cnt += 1;
          res[cnt] = mat[y][x];

          // step 3) move (1, -1) dir till reach the boundary
          while (x > 0){
              x -= 1;
              y += 1;
              cnt += 1;
              res[cnt] = mat[y][x];
          }

          // step 4) move down 1 idx
          y += 1;
          cnt += 1;
          res[cnt] = mat[y][x];

          // step 5) move (1,-1) dir till reach the boundary
          while(y > 0){
              x += 1;
              y -= 1;
              cnt += 1;
              res[cnt] = mat[y][x];
          }

         // cnt += 1;
      }

      return res;
  }

  // LC 206
  // 11.55 - 12.05 pm
  // IDEA: RECURSION + LIST NODE OP
  public ListNode reverseList(ListNode head) {
      // edge
      if(head == null || head.next == null){
          return head;
      }

      ListNode _prev = null;

      reverseHelper(head, _prev);


      return _prev; // ???
  }

  public ListNode reverseHelper(ListNode head, ListNode _prev){

      // edge
      if(head == null || head.next == null){
          return _prev;
      }

      ListNode _next = head.next;
      _prev.next = head;
      _prev = head;
      head = _next;

      reverseHelper(head, _prev);

      return _prev;
  }

    // IDEA: ITERATION + LIST NODE OP
//  public ListNode reverseList(ListNode head) {
//      // edge
//      if(head == null || head.next == null){
//          return head;
//      }
//
//      /**
//       *  4 steps
//       *
//       *  1. define prev
//       *  2. define next
//       *  3.  point cur to prev
//       *  4. move prev to cur
//       *  5. cur = cur.next
//       *
//       */
//
////      ListNode res = new ListNode();
////      //ListNode node = res;
////      res.next  = head;
//
//      ListNode _prev = new ListNode();
//      while(head != null){
//          ListNode _next = head.next;
//          head.next = _prev;
//          _prev = head;
//          head = _next;
//      }
//
//      //return res.next;
//      return _prev;
//  }


    // LC 852
    // 9.57 - 10.07 am
    /**  IDEA 1) BINARY SEARCH
     *
     *
     *  find the element k such that
     *
     *     nums[k] > nums[k-1]
     *      and
     *     nums[k] > nums[k+1]
     *
     *
     *   case 1) `mid` is in `increasing array`
     *
     *
     *   case 2) `mid` is in `decreasing array`
     *
     *
     */
//    public int peakIndexInMountainArray(int[] arr) {
//        // edge
//        if(arr == null || arr.length < 3){
//            return -1; // ???
//        }
//        // binary search
//        int l = 0;
//        int r = arr.length - 1;
//        // ??
//        int mid = -1;
//        while (r >= l){
//            mid = (l + r) / 2;
//            // case 1)  `mid` is the peak, (peak is found)
//            if(arr[mid] > arr[mid-1] && arr[mid] > arr[mid+1]){
//                return mid;
//            }
//            // case 2) `mid` is in increasing array
//            if(arr[mid] < arr[mid-1]){
//                // move right
//                l = mid + 1;
//            }
//            // case 3) `mid` is in decreasing array
//            else{
//                // move left
//                r = mid - 1;
//
//            }
//
//        }
//
//        // ????
//       return mid;
//    }

    public int peakIndexInMountainArray(int[] arr) {
        if (arr == null || arr.length < 3) {
            return -1; // Return -1 if the array length is less than 3
        }

        // Binary search
        int l = 1; // Start from 1 to avoid checking arr[-1]
        int r = arr.length - 1; // End at length - 2 to avoid checking arr[arr.length]

        while (r >= l) {
            int mid = l + (r - l) / 2;

            // Check if mid is the peak
            if (mid < arr.length-1 && arr[mid] > arr[mid - 1] && arr[mid] > arr[mid + 1]) {
                return mid;
            }
            // If the element at mid is smaller than the next element, peak is on the right
            else if (arr[mid] < arr[mid + 1]) {
                l = mid + 1;
            }
            // Otherwise, peak is on the left
            else {
                r = mid - 1;
            }
        }

        return -1; // Shouldn't happen in a valid mountain array
    }



}
