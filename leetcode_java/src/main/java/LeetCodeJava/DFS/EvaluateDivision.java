package LeetCodeJava.DFS;

// https://leetcode.com/problems/evaluate-division/description/

import dev.workspace6;

import java.util.*;

/**
 * 399. Evaluate Division
 * Solved
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an array of variable pairs equations and an array of real numbers values, where equations[i] = [Ai, Bi] and values[i] represent the equation Ai / Bi = values[i]. Each Ai or Bi is a string that represents a single variable.
 *
 * You are also given some queries, where queries[j] = [Cj, Dj] represents the jth query where you must find the answer for Cj / Dj = ?.
 *
 * Return the answers to all queries. If a single answer cannot be determined, return -1.0.
 *
 * Note: The input is always valid. You may assume that evaluating the queries will not result in division by zero and that there is no contradiction.
 *
 * Note: The variables that do not occur in the list of equations are undefined, so the answer cannot be determined for them.
 *
 *
 *
 * Example 1:
 *
 * Input: equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * Output: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * Explanation:
 * Given: a / b = 2.0, b / c = 3.0
 * queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * return: [6.0, 0.5, -1.0, 1.0, -1.0 ]
 * note: x is undefined => -1.0
 * Example 2:
 *
 * Input: equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
 * Output: [3.75000,0.40000,5.00000,0.20000]
 * Example 3:
 *
 * Input: equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
 * Output: [0.50000,2.00000,-1.00000,-1.00000]
 *
 *
 * Constraints:
 *
 * 1 <= equations.length <= 20
 * equations[i].length == 2
 * 1 <= Ai.length, Bi.length <= 5
 * values.length == equations.length
 * 0.0 < values[i] <= 20.0
 * 1 <= queries.length <= 20
 * queries[i].length == 2
 * 1 <= Cj.length, Dj.length <= 5
 * Ai, Bi, Cj, Dj consist of lower case English letters and digits.
 *
 */
public class EvaluateDivision {

    // V0
    // TODO: fix below
//    private class EquationRes{
//        // attr
//        String variable;
//        Double result;
//
//        public Double getResult() {
//            return result;
//        }
//
//        public String getVariable() {
//            return variable;
//        }
//
//        // constructor
//        EquationRes(String variable, Double result){
//            this.variable = variable;
//            this.result = result;
//        }
//    }
//
//    // init relation
//    Map<String, List<workspace6.EquationRes>> relations = new HashMap();
//    //double[] res = new double[];
//
//    public double[] calcEquation(
//
//            List<List<String>> equations, double[] values, List<List<String>> queries) {
//
//        // build
//        buildRelation(equations, values);
//        // get
//        double[] res = new double[queries.size()];
//        for(int i = 0; i < queries.size(); i++){
//            res[i] = getResult(queries.get(i), 1);
//        }
//
//        System.out.println(">>> res = " + res);
//
//        return res;
//    }
//
//    // dfs
//    private double getResult(List<String> queries, double res){
//        // check if in list
//        String firstVal = queries.get(0);
//        String secondVal = queries.get(1);
//        if (!this.relations.containsKey(firstVal) || !this.relations.containsKey(secondVal)){
//            return -1.0;
//        }
//
//        //double res = 1;
//        //List<EquationRes> x = this.relations.get(firstVal);
//        for(workspace6.EquationRes equationRes: this.relations.get(firstVal)){
//            res = res * equationRes.result;
//
//
//        }
//
//        return res;
//    }
//
//    // build relation
//    private void buildRelation(List<List<String>> equations, double[] values){
//        for(int i = 0; i < equations.size(); i++){
//            List<String> equation = equations.get(i);
//            String firstVal = equation.get(0);
//            String secondVal = equation.get(1);
//
//            workspace6.EquationRes equationRes = new workspace6.EquationRes(secondVal, values[i]);
//
//            List<workspace6.EquationRes> equationAndRes = new ArrayList<>();
//            if (this.relations.containsKey(firstVal)){
//                equationAndRes =  this.relations.get(firstVal);
//            }
//
//            this.relations.put(firstVal, equationAndRes);
//        }
//
//    }

    // V1
    // IDEA: DFS
    // https://leetcode.com/problems/evaluate-division/solutions/3543256/image-explanation-easiest-concise-comple-okpu/
    public double[] calcEquation_1(List<List<String>> equations, double[] values, List<List<String>> queries) {
        HashMap<String, HashMap<String, Double>> gr = buildGraph(equations, values);
        double[] finalAns = new double[queries.size()];

        for (int i = 0; i < queries.size(); i++) {
            String dividend = queries.get(i).get(0);
            String divisor = queries.get(i).get(1);

            /** NOTE !!!
             *
             *  either dividend nor divisor NOT in graph, return -1.0 directly
             */
            if (!gr.containsKey(dividend) || !gr.containsKey(divisor)) {
                finalAns[i] = -1.0;
            } else {

                /** NOTE !!!
                 *
                 *  we use `vis` to check if element already visited
                 *  (to avoid repeat accessing)
                 *  `vis` init again in every loop
                 */

                HashSet<String> vis = new HashSet<>();
                /**
                 *  NOTE !!!
                 *
                 *   we init `ans` and pass it to dfs method
                 *   (but dfs method return NOTHING)
                 *   -> `ans` is init, and pass into dfs,
                 *   -> so `ans` value is updated during dfs recursion run
                 *   -> and after dfs run completed, we get the result `ans` value
                 */
                double[] ans = { -1.0 };
                double temp = 1.0;
                dfs(dividend, divisor, gr, vis, ans, temp);
                finalAns[i] = ans[0];
            }
        }

        return finalAns;
    }

    /** NOTE !!! below dfs method */
    public void dfs(String node, String dest, HashMap<String, HashMap<String, Double>> gr, HashSet<String> vis,
                    double[] ans, double temp) {

        /** NOTE !!! we use `vis` to check if element already visited */
        if (vis.contains(node))
            return;

        vis.add(node);
        if (node.equals(dest)) {
            ans[0] = temp;
            return;
        }

        for (Map.Entry<String, Double> entry : gr.get(node).entrySet()) {
            String ne = entry.getKey();
            double val = entry.getValue();
            /** NOTE !!! update temp as `temp * val` */
            dfs(ne, dest, gr, vis, ans, temp * val);
        }
    }

    public HashMap<String, HashMap<String, Double>> buildGraph(List<List<String>> equations, double[] values) {
        HashMap<String, HashMap<String, Double>> gr = new HashMap<>();

        for (int i = 0; i < equations.size(); i++) {
            String dividend = equations.get(i).get(0);
            String divisor = equations.get(i).get(1);
            double value = values[i];

            gr.putIfAbsent(dividend, new HashMap<>());
            gr.putIfAbsent(divisor, new HashMap<>());

            gr.get(dividend).put(divisor, value);
            gr.get(divisor).put(dividend, 1.0 / value);
        }

        return gr;
    }

    // V2
    // IDEA: DFS (gpt)
    public double[] calcEquation_2(List<List<String>> equations, double[] values, List<List<String>> queries) {
        // Build the graph
        Map<String, Map<String, Double>> graph = new HashMap<>();
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            double value = values[i];

            graph.putIfAbsent(a, new HashMap<>());
            graph.putIfAbsent(b, new HashMap<>());

            graph.get(a).put(b, value);
            graph.get(b).put(a, 1.0 / value);
        }

        // Process each query
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String c = queries.get(i).get(0);
            String d = queries.get(i).get(1);

            // If either node is not in the graph, result is -1.0
            if (!graph.containsKey(c) || !graph.containsKey(d)) {
                results[i] = -1.0;
            } else if (c.equals(d)) {
                // If nodes are the same, result is 1.0
                results[i] = 1.0;
            } else {
                // Use DFS to find the result
                Set<String> visited = new HashSet<>();
                results[i] = dfs(graph, c, d, 1.0, visited);
            }
        }

        return results;
    }

    private double dfs(Map<String, Map<String, Double>> graph, String current, String target, double value,
                       Set<String> visited) {
        // If we reach the target, return the current value
        if (current.equals(target)) {
            return value;
        }

        // Mark the current node as visited
        visited.add(current);

        // Explore neighbors
        for (Map.Entry<String, Double> neighbor : graph.get(current).entrySet()) {
            String nextNode = neighbor.getKey();
            double weight = neighbor.getValue();

            if (!visited.contains(nextNode)) {
                double result = dfs(graph, nextNode, target, value * weight, visited);
                if (result != -1.0) {
                    return result;
                }
            }
        }

        // Backtrack
        visited.remove(current);
        return -1.0;
    }

    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/evaluate-division/solutions/1992891/java-dfs-solution-with-comments-evaluate-6gmn/
    private Map<String, Map<String, Double>> makeGraph(List<List<String>> e, double[] values) {
        // build a graph
        // like a -> b = values[i]
        // and b -> a = 1.0 / values[i];
        Map<String, Map<String, Double>> graph = new HashMap<>();
        String u, v;

        for (int i = 0; i < e.size(); i++) {
            u = e.get(i).get(0);
            v = e.get(i).get(1);

            graph.putIfAbsent(u, new HashMap<>());
            graph.get(u).put(v, values[i]);

            graph.putIfAbsent(v, new HashMap<>());
            graph.get(v).put(u, 1 / values[i]);

        }
        return graph;
    }

    public double[] calcEquation_3(List<List<String>> equations, double[] values, List<List<String>> queries) {
        Map<String, Map<String, Double>> graph = makeGraph(equations, values);

        double[] ans = new double[queries.size()];

        // check for every Querie
        // store it in ans array;
        for (int i = 0; i < queries.size(); i++) {
            ans[i] = dfs(queries.get(i).get(0), queries.get(i).get(1), new HashSet<>(), graph);
        }
        return ans;
    }

    public double dfs(String src, String dest, Set<String> visited, Map<String, Map<String, Double>> graph) {
        // check the terminated Case
        // if string is not present in graph return -1.0;
        // like [a, e] or [x, x] :)
        if (graph.containsKey(src) == false)
            return -1.0;

        // simply say check src and dest are equal :) then return dest
        // store it in weight varaible;
        // case like [a,a] also handle
        if (graph.get(src).containsKey(dest)) {
            return graph.get(src).get(dest);
        }

        visited.add(src);

        for (Map.Entry<String, Double> nbr : graph.get(src).entrySet()) {
            if (visited.contains(nbr.getKey()) == false) {
                double weight = dfs(nbr.getKey(), dest, visited, graph);

                // if weight is not -1.0(terminate case)
                // then mutliply it
                // like in querie a -> c => 2 * 3 = 6
                if (weight != -1.0) {
                    return nbr.getValue() * weight;
                }
            }
        }
        return -1.0;
    }

    // V4
    // IDEA: UNION FIND (gpt)
    class UnionFind {
        private Map<String, String> parent;
        private Map<String, Double> ratio;

        public UnionFind() {
            this.parent = new HashMap<>();
            this.ratio = new HashMap<>();
        }

        // Finds the root of a node and applies path compression
        public String find(String x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                ratio.put(x, 1.0);
            }

            if (!x.equals(parent.get(x))) {
                String originalParent = parent.get(x);
                parent.put(x, find(originalParent));
                ratio.put(x, ratio.get(x) * ratio.get(originalParent));
            }

            return parent.get(x);
        }

        // Union two nodes with the given value
        public void union(String x, String y, double value) {
            String rootX = find(x);
            String rootY = find(y);

            if (!rootX.equals(rootY)) {
                parent.put(rootX, rootY);
                ratio.put(rootX, value * ratio.get(y) / ratio.get(x));
            }
        }

        // Get the ratio between two nodes if they are connected
        public double isConnected(String x, String y) {
            if (!parent.containsKey(x) || !parent.containsKey(y)) {
                return -1.0;
            }

            String rootX = find(x);
            String rootY = find(y);

            if (!rootX.equals(rootY)) {
                return -1.0;
            }

            return ratio.get(x) / ratio.get(y);
        }
    }

    public double[] calcEquation_4(List<List<String>> equations, double[] values, List<List<String>> queries) {
        UnionFind uf = new UnionFind();

        // Build the union-find structure
        for (int i = 0; i < equations.size(); i++) {
            String a = equations.get(i).get(0);
            String b = equations.get(i).get(1);
            double value = values[i];
            uf.union(a, b, value);
        }

        // Process the queries
        double[] results = new double[queries.size()];
        for (int i = 0; i < queries.size(); i++) {
            String c = queries.get(i).get(0);
            String d = queries.get(i).get(1);
            results[i] = uf.isConnected(c, d);
        }

        return results;
    }

    // V5
    // IDEA: UNION FIND
    // https://leetcode.com/problems/evaluate-division/submissions/1498458088/
//    private Map<String, Pair<String, Double>> parents = new HashMap<>();
//
//    public double[] calcEquation(List<List<String>> equations, double[] values,
//                                 List<List<String>> queries) {
//        // Step 1: build union groups
//        for (int i = 0; i < equations.size(); i++) {
//            List<String> equation = equations.get(i);
//
//            String u = equation.get(0), v = equation.get(1);
//            double w = values[i];
//
//            union(u, v, w);
//        }
//
//        // Step 2. try to make the query
//        double[] res = new double[queries.size()];
//        for (int i = 0; i < queries.size(); i++) {
//            List<String> query = queries.get(i);
//            String u = query.get(0), v = query.get(1);
//
//            // case 1: u or v never appear before
//            if (!parents.containsKey(u) || !parents.containsKey(v)) {
//                res[i] = -1.0;
//                continue;
//            }
//
//            Pair<String, Double> uPair = find(u);
//            Pair<String, Double> vPair = find(v);
//
//            String uParent = uPair.getKey();
//            double uWeight = uPair.getValue();
//
//            String vParent = vPair.getKey();
//            double vWeight = vPair.getValue();
//
//            if (!uParent.equals(vParent))
//                // case 2: u & v NOT belong to the same group
//                res[i] = -1.0;
//            else
//                /*
//                 * case 3: u & v belong to the same group <==> uPar == vPar
//                 * Then we want to query u / v:
//                 *
//                 * Assuming we have:
//                 * 1. u = uPar * uWei
//                 * 2. v = vPar * vWei = uPar * vWei
//                 *
//                 * Thus u / v = uWei / vWei
//                 */
//                res[i] = uWeight / vWeight;
//
//        }
//        return res;
//    }
//
//    private Pair<String, Double> find(String u) {
//        if (!parents.containsKey(u)) {
//            parents.put(u, new Pair(u, 1.0));
//            return parents.get(u);
//        }
//
//        if (!parents.get(u).getKey().equals(u)) {
//            Pair<String, Double> uParentPair = parents.get(u);
//            Pair<String, Double> uGrandParentPair = find(uParentPair.getKey());
//
//            parents.put(u, new Pair(uGrandParentPair.getKey(),
//                    uParentPair.getValue() * uGrandParentPair.getValue()));
//        }
//        return parents.get(u);
//    }
//
//    private void union(String u, String v, Double w) {
//        Pair<String, Double> uPair = find(u);
//        Pair<String, Double> vPair = find(v);
//
//        String uParent = uPair.getKey();
//        double uWeight = uPair.getValue();
//
//        String vParent = vPair.getKey();
//        double vWeight = vPair.getValue();
//
//        if (!uParent.equals(vParent)) {
//            parents.put(uParent, new Pair(vParent, vWeight / uWeight * w));
//        }
//    }

    // V4
    // IDEA: BFS
    // https://leetcode.com/problems/evaluate-division/solutions/3543150/pythonjavacsimple-solutioneasy-to-unders-7uwo/
//    public double[] calcEquation_4(List<List<String>> equations, double[] values, List<List<String>> queries) {
//        Map<String, Map<String, Double>> graph = buildGraph_4(equations, values);
//        double[] results = new double[queries.size()];
//
//        for (int i = 0; i < queries.size(); i++) {
//            List<String> query = queries.get(i);
//            String dividend = query.get(0);
//            String divisor = query.get(1);
//
//            if (!graph.containsKey(dividend) || !graph.containsKey(divisor)) {
//                results[i] = -1.0;
//            } else {
//                results[i] = bfs(dividend, divisor, graph);
//            }
//        }
//
//        return results;
//    }
//
//    private Map<String, Map<String, Double>> buildGraph_4(List<List<String>> equations, double[] values) {
//        Map<String, Map<String, Double>> graph = new HashMap<>();
//
//        for (int i = 0; i < equations.size(); i++) {
//            List<String> equation = equations.get(i);
//            String dividend = equation.get(0);
//            String divisor = equation.get(1);
//            double value = values[i];
//
//            graph.putIfAbsent(dividend, new HashMap<>());
//            graph.putIfAbsent(divisor, new HashMap<>());
//            graph.get(dividend).put(divisor, value);
//            graph.get(divisor).put(dividend, 1.0 / value);
//        }
//
//        return graph;
//    }
//
//    private double bfs(String start, String end, Map<String, Map<String, Double>> graph) {
//        Queue<Pair<String, Double>> queue = new LinkedList<>();
//        Set<String> visited = new HashSet<>();
//        queue.offer(new Pair<>(start, 1.0));
//
//        while (!queue.isEmpty()) {
//            Pair<String, Double> pair = queue.poll();
//            String node = pair.getKey();
//            double value = pair.getValue();
//
//            if (node.equals(end)) {
//                return value;
//            }
//
//            visited.add(node);
//
//            for (Map.Entry<String, Double> neighbor : graph.get(node).entrySet()) {
//                String neighborNode = neighbor.getKey();
//                double neighborValue = neighbor.getValue();
//
//                if (!visited.contains(neighborNode)) {
//                    queue.offer(new Pair<>(neighborNode, value * neighborValue));
//                }
//            }
//        }
//
//        return -1.0;
//    }

}
