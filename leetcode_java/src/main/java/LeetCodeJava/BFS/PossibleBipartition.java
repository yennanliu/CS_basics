package LeetCodeJava.BFS;

// https://leetcode.com/problems/possible-bipartition/description/

import java.util.*;

/**
 * 886. Possible Bipartition
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * We want to split a group of n people (labeled from 1 to n) into two groups of any size. Each person may dislike some other people, and they should not go into the same group.
 *
 * Given the integer n and the array dislikes where dislikes[i] = [ai, bi] indicates that the person labeled ai does not like the person labeled bi, return true if it is possible to split everyone into two groups in this way.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 4, dislikes = [[1,2],[1,3],[2,4]]
 * Output: true
 * Explanation: The first group has [1,4], and the second group has [2,3].
 * Example 2:
 *
 * Input: n = 3, dislikes = [[1,2],[1,3],[2,3]]
 * Output: false
 * Explanation: We need at least 3 groups to divide them. We cannot put them in two groups.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2000
 * 0 <= dislikes.length <= 104
 * dislikes[i].length == 2
 * 1 <= ai < bi <= n
 * All the pairs of dislikes are unique.
 *
 *
 */
public class PossibleBipartition {

    // V0


    // V1
    // https://leetcode.ca/2018-05-04-886-Possible-Bipartition/
    private int[] p;
    public boolean possibleBipartition_1(int n, int[][] dislikes) {
        p = new int[n];
        List<Integer>[] g = new List[n];
        Arrays.setAll(g, k -> new ArrayList<>());
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        for (int[] e : dislikes) {
            int a = e[0] - 1, b = e[1] - 1;
            g[a].add(b);
            g[b].add(a);
        }
        for (int i = 0; i < n; ++i) {
            for (int j : g[i]) {
                if (find(i) == find(j)) {
                    return false;
                }
                p[find(j)] = find(g[i].get(0));
            }
        }
        return true;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }

    // V2
    // IDEA: BFS
    // https://leetcode.com/problems/possible-bipartition/solutions/7317770/possible-bipartition-solutionbfs-by-ajen-ncyp/
    public boolean possibleBipartition_2(int n, int[][] dislikes) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int d[] : dislikes) {
            graph.get(d[0]).add(d[1]);
            graph.get(d[1]).add(d[0]);
        }
        int col[] = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            col[i] = -1;
        }
        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i <= n; i++) {
            if (col[i] == -1) {
                q.add(i);
                col[i] = 0;
                while (!q.isEmpty()) {
                    int curr = q.remove();
                    for (int j = 0; j < graph.get(curr).size(); j++) {
                        int e = graph.get(curr).get(j);
                        if (col[e] == -1) {
                            col[e] = 1 - col[curr];
                            q.add(e);
                        } else if (col[e] == col[curr]) {
                            return false;
                        }
                    }

                }

            }
        }

        return true;
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/possible-bipartition/solutions/654955/pythoncjava-on-by-dfs-and-coloring-w-gra-vjcq/
    public boolean possibleBipartition_3(int N, int[][] dislikes) {
        List<Integer>[] graph = new List[N + 1];

        for (int i = 1; i <= N; ++i)
            graph[i] = new ArrayList<>();

        for (int[] dislike : dislikes) {
            graph[dislike[0]].add(dislike[1]);
            graph[dislike[1]].add(dislike[0]);
        }

        Integer[] colors = new Integer[N + 1];

        for (int i = 1; i <= N; ++i) {
            // If the connected component that node i belongs to hasn't been colored yet then try coloring it.
            if (colors[i] == null && !dfs(graph, colors, i, 1))
                return false;
        }
        return true;
    }

    private boolean dfs(List<Integer>[] graph, Integer[] colors, int currNode, int currColor) {
        colors[currNode] = currColor;

        // Color all uncolored adjacent nodes.
        for (Integer adjacentNode : graph[currNode]) {

            if (colors[adjacentNode] == null) {
                if (!dfs(graph, colors, adjacentNode, currColor * -1))
                    return false;

            } else if (colors[adjacentNode] == currColor) {
                return false;
            }
        }
        return true;
    }


}
