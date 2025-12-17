package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-subtrees-with-max-distance-between-cities/description/

import java.util.*;

/**
 * 1617. Count Subtrees With Max Distance Between Cities
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There are n cities numbered from 1 to n. You are given an array edges of size n-1, where edges[i] = [ui, vi] represents a bidirectional edge between cities ui and vi. There exists a unique path between each pair of cities. In other words, the cities form a tree.
 *
 * A subtree is a subset of cities where every city is reachable from every other city in the subset, where the path between each pair passes through only the cities from the subset. Two subtrees are different if there is a city in one subtree that is not present in the other.
 *
 * For each d from 1 to n-1, find the number of subtrees in which the maximum distance between any two cities in the subtree is equal to d.
 *
 * Return an array of size n-1 where the dth element (1-indexed) is the number of subtrees in which the maximum distance between any two cities is equal to d.
 *
 * Notice that the distance between the two cities is the number of edges in the path between them.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: n = 4, edges = [[1,2],[2,3],[2,4]]
 * Output: [3,4,0]
 * Explanation:
 * The subtrees with subsets {1,2}, {2,3} and {2,4} have a max distance of 1.
 * The subtrees with subsets {1,2,3}, {1,2,4}, {2,3,4} and {1,2,3,4} have a max distance of 2.
 * No subtree has two nodes where the max distance between them is 3.
 * Example 2:
 *
 * Input: n = 2, edges = [[1,2]]
 * Output: [1]
 * Example 3:
 *
 * Input: n = 3, edges = [[1,2],[2,3]]
 * Output: [2,1]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 15
 * edges.length == n-1
 * edges[i].length == 2
 * 1 <= ui, vi <= n
 * All pairs (ui, vi) are distinct.
 */
public class CountSubtreesWithMaxDistanceBetweenCities {

    // V0
//    public int[] countSubgraphsForEachDiameter(int n, int[][] edges) {
//
//    }

    // V1

    // V2
    // IDEA: BACKTRACK
    // https://leetcode.com/problems/count-subtrees-with-max-distance-between-cities/solutions/3019595/java-backtracking-by-mpettina-14hu/
    List<Integer>[] tree, subTree;
    int subTreeRoot, maxPath;
    boolean[] inStack; // do not allow to travel upwards in the rooted tree
    int[] ans;

    public int[] countSubgraphsForEachDiameter_1(int n, int[][] edges) {
        buildTree(n, edges);
        ans = new int[n - 1];
        solve(0);
        return ans;
    }

    private void buildTree(int n, int[][] edges) {
        tree = new List[n];
        subTree = new List[n];
        inStack = new boolean[n];
        for (int i = 0; i < n; ++i) {
            tree[i] = new ArrayList<>();
            subTree[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            tree[e[0] - 1].add(e[1] - 1);
            tree[e[1] - 1].add(e[0] - 1);
        }
    }

    private void solve(int i) {
        subTreeRoot = i;
        inStack[i] = true;
        buildSubTree(0, 0, Collections.singletonList(i), new ArrayList<>());
        for (int e : tree[i]) {
            if (inStack[e]) {
                continue;
            }
            solve(e);
        }
    }

    private void buildSubTree(int i, int j, List<Integer> fronteer, List<Integer> next) {
        if (i >= fronteer.size()) {
            buildNextLevel(next);
            return;
        }
        int curr = fronteer.get(i);
        if (j >= tree[curr].size()) {
            buildSubTree(i + 1, 0, fronteer, next);
        } else {
            int succ = tree[curr].get(j);
            if (inStack[succ]) {
                buildSubTree(i, j + 1, fronteer, next);
                return;
            }
            inStack[succ] = true;
            next.add(succ);
            subTree[curr].add(succ);
            buildSubTree(i, j + 1, fronteer, next);
            next.remove(next.size() - 1);
            subTree[curr].remove(subTree[curr].size() - 1);
            buildSubTree(i, j + 1, fronteer, next);
            inStack[succ] = false;
        }
    }

    private void buildNextLevel(List<Integer> next) {
        if (next.isEmpty()) { // subTree is done, add it to the answer
            maxPath = 0;
            computeMaxPath(subTreeRoot);
            if (maxPath > 1) {
                ++ans[maxPath - 2];
            }
        } else {
            buildSubTree(0, 0, next, new ArrayList<>());
        }
    }

    private int computeMaxPath(int i) {
        int max = 0, prevMax = 0;
        for (int child : subTree[i]) {
            int h = computeMaxPath(child);
            if (h >= max) {
                prevMax = max;
                max = h;
            } else {
                prevMax = Integer.max(prevMax, h);
            }
        }
        maxPath = Integer.max(maxPath, prevMax + max + 1);
        return max + 1;
    }


    // V3
    // IDEA: BITMASK
    // https://leetcode.com/problems/count-subtrees-with-max-distance-between-cities/solutions/1706928/java-solution-bitmask-by-gauravkr442-xeah/
    public int[] countSubgraphsForEachDiameter_3(int n, int[][] edges) {

        ArrayList<Integer>[] graph = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();

        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];

            --u;
            --v;

            graph[u].add(v);
            graph[v].add(u);
        }

        int[] darr = new int[n - 1];

        for (int mask = 0; mask < (1 << (n)); mask++) {
            int d = getmax(mask, graph);

            if (d > 0) {
                darr[d - 1]++;
            }
        }

        return darr;

    }

    int getmax(int mask, ArrayList<Integer>[] graph) {

        int maxd = -1;

        for (int i = 0; i < 15; i++) {
            if ((mask & (1 << i)) != 0) {
                maxd = Math.max(maxd, dfs(i, graph, mask));
            }
        }

        return maxd;
    }

    int dfs(int node, ArrayList<Integer>[] graph, int mask) {

        Queue<Integer> q = new LinkedList<>();
        q.add(node);
        int dist = -1;
        int curr = mask;

        while (!q.isEmpty()) {
            dist++;

            for (int l = q.size() - 1; l >= 0; l--) {
                int z1 = q.peek();
                curr = curr ^ (1 << z1);
                q.remove();

                for (int z : graph[z1]) {
                    if (((mask & (1 << z)) != 0) && ((curr & (1 << z)) != 0)) {
                        q.add(z);
                    }
                }
            }

        }

        if (curr != 0) {
            return -1;
        }

        return dist;

    }

    // V4
    // https://leetcode.com/problems/count-subtrees-with-max-distance-between-cities/solutions/889194/java-build-subtrees-recursively-on2-numb-t11w/
    public int[] countSubgraphsForEachDiameter_4(int n, int[][] edges) {

        ArrayList<Integer>[] graph = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++)
            graph[i] = new ArrayList<>();
        for (int[] edge : edges) {
            graph[edge[0] - 1].add(edge[1] - 1);
            graph[edge[1] - 1].add(edge[0] - 1);
        }
        List<Integer> postOrder = new ArrayList<>();
        getPostorder(graph, 0, new boolean[n], postOrder);

        int[][] distances = new int[n][n];
        for (int i = 0; i < n; i++)
            calculateDistances(graph, i, distances);

        List<List<Integer>> subtrees = buildAllSubtrees(graph, new boolean[n], postOrder, 0);
        int[] ans = new int[n - 1];
        for (List<Integer> subTree : subtrees) {
            if (subTree.size() < 2)
                continue;

            // find the maximum distance between two vertices from this subtree
            int max = -1;
            for (int i = 0; i < subTree.size(); i++)
                for (int j = i + 1; j < subTree.size(); j++)
                    max = Math.max(max, distances[subTree.get(i)][subTree.get(j)]);
            ans[max - 1]++;
        }

        return ans;
    }

    // get the postorder of vertices using DFS

    private void getPostorder(ArrayList<Integer>[] graph, int start, boolean[] visited, List<Integer> postOrder) {
        visited[start] = true;
        for (int i : graph[start]) {
            if (visited[i])
                continue;
            getPostorder(graph, i, visited, postOrder);
        }
        postOrder.add(start);
    }

    // find distances between all pairs using BFS

    private void calculateDistances(ArrayList<Integer>[] graph, int start, int[][] distances) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        int dist = 0;
        boolean[] visited = new boolean[distances.length];
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int vertex = queue.poll();
                visited[vertex] = true;
                distances[start][vertex] = dist;
                for (int neigh : graph[vertex]) {
                    if (!visited[neigh])
                        queue.add(neigh);
                }
            }
            dist++;
        }
    }

    // build the set of all subtrees by visiting vertices in the postorder
    // in this way the current vertex will always be the leaf in the subtree up to current

    private List<List<Integer>> buildAllSubtrees(ArrayList<Integer>[] graph, boolean[] visited, List<Integer> order,
                                                 int current) {
        if (current == order.size())
            return new ArrayList<>();

        int leaf = order.get(current);

        // find the unique neighbor of the leaf
        int neighbor = -1;
        for (int neigh : graph[leaf]) {
            if (!visited[neigh]) {
                neighbor = neigh;
                break;
            }
        }

        visited[leaf] = true;

        // find the subtrees if we remove the leaf
        List<List<Integer>> list = buildAllSubtrees(graph, visited, order, current + 1);

        // for each subset, if it contains the unique neighbor, add a copy of it with the leaf
        List<List<Integer>> newAns = new ArrayList<>();
        for (List<Integer> subTree : list) {
            newAns.add(subTree);
            if (subTree.contains(neighbor)) {
                List<Integer> newList = new ArrayList(subTree);
                newList.add(leaf);
                newAns.add(newList);
            }
        }

        // add the subtree that only contains leaf
        List<Integer> single = new ArrayList<>();
        single.add(leaf);
        newAns.add(single);

        return newAns;
    }


    // V5


}
