package LeetCodeJava.Graph;

// https://leetcode.com/problems/check-for-contradictions-in-equations/description/

import java.util.*;

/**
 * 2307. Check for Contradictions in Equations
 * Hard
 *
 * You are given a 2D array of strings equations and an array of real numbers values, where equations[i] = [Ai, Bi] and values[i] means that Ai / Bi = values[i].
 *
 * Determine if there exists a contradiction in the equations. Return true if there is a contradiction, or false otherwise.
 *
 * Note:
 *
 * When checking if two numbers are equal, check that their absolute difference is less than 10^-5.
 * The testcases are generated such that there are no cases targeting precision, i.e. using double is enough to solve the problem.
 *
 *
 * Example 1:
 *
 * Input: equations = [["a","b"],["b","c"],["a","c"]], values = [3,0.5,1.5]
 * Output: false
 * Explanation:
 * The given equations are: a / b = 3, b / c = 0.5, a / c = 1.5
 * There are no contradictions in the equations. One possible assignment to satisfy all equations is:
 * a = 3, b = 1 and c = 2.
 *
 * Example 2:
 *
 * Input: equations = [["le","et"],["le","code"],["code","et"]], values = [2,5,0.5]
 * Output: true
 * Explanation:
 * The given equations are: le / et = 2, le / code = 5, code / et = 0.5
 * Based on the first two equations, we get code / et = 0.4.
 * Since the third equation is code / et = 0.5, we get a contradiction.
 *
 *
 * Constraints:
 *
 * 1 <= equations.length <= 100
 * equations[i].length == 2
 * 1 <= Ai.length, Bi.length <= 5
 * Ai, Bi consist of lowercase English letters.
 * equations.length == values.length
 * 0.0 < values[i] <= 10.0
 * values[i] has a maximum of 2 decimal places.
 *
 */
public class CheckForContradictionsInEquations {

    // V0
    // IDEA: WEIGHTED UNION FIND (ratio[x] = value(x) / value(root(x)))
    //  - for a / b = v:
    //    different roots -> merge, attaching root(b) under root(a) with
    //                       ratio[rootB] = v * ratio[a] / ratio[b]
    //    same root       -> the ratio is already fixed; contradiction iff
    //                       |v * ratio[a] - ratio[b]| >= 1e-5
    /**
     * time = O(m * alpha(n))
     * space = O(n)
     */
    public boolean checkContradictions(List<List<String>> equations, double[] values) {
        // give every variable name an integer id
        Map<String, Integer> ids = new HashMap<>();
        for (List<String> equation : equations) {
            for (String name : equation) {
                if (!ids.containsKey(name)) {
                    ids.put(name, ids.size());
                }
            }
        }

        int n = ids.size();
        parent = new int[n];
        ratio = new double[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            ratio[i] = 1.0;
        }

        for (int i = 0; i < equations.size(); i++) {
            int a = ids.get(equations.get(i).get(0));
            int b = ids.get(equations.get(i).get(1));
            double value = values[i];
            int rootA = find(a);
            int rootB = find(b);
            if (rootA != rootB) {
                // value(rootB) / value(rootA) chosen so that value(a) / value(b) == value
                parent[rootB] = rootA;
                ratio[rootB] = value * ratio[a] / ratio[b];
            } else if (Math.abs(value * ratio[a] - ratio[b]) >= EPS) {
                return true;
            }
        }
        return false;
    }

    private static final double EPS = 1e-5;
    private int[] parent;
    private double[] ratio;

    /** Finds the root of x, compressing the path and folding the ratios along it. */
    private int find(int x) {
        // collect the path up to the root
        List<Integer> path = new ArrayList<>();
        int root = x;
        while (parent[root] != root) {
            path.add(root);
            root = parent[root];
        }
        // rewrite from the node nearest the root downwards, so each parent's ratio is final first
        for (int i = path.size() - 1; i >= 0; i--) {
            int node = path.get(i);
            ratio[node] *= ratio[parent[node]];
            parent[node] = root;
        }
        return root;
    }
}
