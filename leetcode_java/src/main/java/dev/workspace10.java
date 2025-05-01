package dev;

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


}
