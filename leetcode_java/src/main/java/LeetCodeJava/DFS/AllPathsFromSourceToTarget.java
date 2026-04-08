package LeetCodeJava.DFS;

// https://leetcode.com/problems/all-paths-from-source-to-target/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 797. All Paths From Source to Target
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a directed acyclic graph (DAG) of n nodes labeled from 0 to n - 1, find all possible paths from node 0 to node n - 1 and return them in any order.
 *
 * The graph is given as follows: graph[i] is a list of all nodes you can visit from node i (i.e., there is a directed edge from node i to node graph[i][j]).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: graph = [[1,2],[3],[3],[]]
 * Output: [[0,1,3],[0,2,3]]
 * Explanation: There are two paths: 0 -> 1 -> 3 and 0 -> 2 -> 3.
 * Example 2:
 *
 *
 * Input: graph = [[4,3,1],[3,2,4],[3],[4],[]]
 * Output: [[0,4],[0,3,4],[0,1,3,4],[0,1,2,3,4],[0,1,4]]
 *
 *
 * Constraints:
 *
 * n == graph.length
 * 2 <= n <= 15
 * 0 <= graph[i][j] < n
 * graph[i][j] != i (i.e., there will be no self-loops).
 * All the elements of graph[i] are unique.
 * The input graph is guaranteed to be a DAG.
 *
 *
 *
 */
public class AllPathsFromSourceToTarget {

    // V0
//    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
//
//    }

    // V1
    // https://leetcode.com/problems/all-paths-from-source-to-target/solutions/7469999/90-beats-easy-simple-java-solution-by-pr-r3h7/
    public List<List<Integer>> allPathsSourceTarget_1(int[][] graph) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(0);
        dfs(0, graph, ans, list);

        return ans;

    }

    public void dfs(int vtx, int graph[][], List<List<Integer>> ans, List<Integer> list) {
        if (vtx == graph.length - 1) {
            ans.add(new ArrayList<>(list));
        }

        for (int ngbr : graph[vtx]) {
            list.add(ngbr);
            dfs(ngbr, graph, ans, list);
            list.remove(list.size() - 1);
        }
    }


    // V2
    public List<List<Integer>> allPathsSourceTarget_2(int[][] a) {
        int n = a.length;
        List<List<Integer>> ans = new ArrayList<>();
        ArrayList<Integer> curr = new ArrayList<>();
        dfs_2(0, curr, ans, a, n - 1);
        return ans;
    }

    private void dfs_2(int x, ArrayList<Integer> curr, List<List<Integer>> ans, int[][] a, int des) {
        curr.add(x);
        if (x == des) {
            ans.add(new ArrayList<>(curr));
            return;
        }
        for (int i = 0; i < a[x].length; i++) {
            dfs_2(a[x][i], curr, ans, a, des);
            // TODO: fix below
           // curr.removeLast();
        }
    }



}
