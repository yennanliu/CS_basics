package dev;

import LeetCodeJava.BFS.NetworkDelayTime;
import LeetCodeJava.BFS.WordLadder;

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
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {

        return null;
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
    // 5.28 - 5.38 pm
    /**
     *  IDEA 1) DFS
     *  IDEA 2) BFS
     *  IDEA 3) Dijkstra
     *
     *
     */
    public int minimumEffortPath(int[][] heights) {

        return 0;
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

}
