package LeetCodeJava.DFS;

// https://leetcode.com/problems/tree-of-coprimes/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  1766. Tree of Coprimes
 *  Hard
 *
 *  There is a tree (i.e., a connected, undirected graph that has no cycles)
 *  consisting of n nodes numbered from 0 to n - 1 and exactly n - 1 edges. Each
 *  node has a value associated with it, and the root of the tree is node 0.
 *
 *  To represent this tree, you are given an integer array nums and a 2D array
 *  edges. Each nums[i] represents the ith node's value, and each
 *  edges[j] = [uj, vj] represents an edge between nodes uj and vj.
 *
 *  Two values x and y are coprime if gcd(x, y) == 1.
 *
 *  An ancestor of a node i is any other node on the shortest path from node i to
 *  the root. A node is not considered an ancestor of itself.
 *
 *  Return an array ans of size n, where ans[i] is the closest ancestor to node i
 *  such that nums[i] and nums[ans[i]] are coprime, or -1 if there is no such
 *  ancestor.
 *
 *  Example 1:
 *    Input: nums = [2,3,3,2], edges = [[0,1],[1,2],[1,3]]
 *    Output: [-1,0,0,1]
 *
 *  Example 2:
 *    Input: nums = [5,6,10,2,3,6,15],
 *           edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[2,6]]
 *    Output: [-1,0,-1,0,0,0,-1]
 *
 *  Constraints:
 *    nums.length == n
 *    1 <= nums[i] <= 50
 *    1 <= n <= 10^5
 *    edges.length == n - 1
 *    edges[j].length == 2
 *    0 <= uj, vj < n
 *    uj != vj
 */
public class TreeOfCoprimes {

    // V0
    // IDEA: DFS + PER-VALUE ANCESTOR STACKS (values are only 1..50)
    //       for each value v in 1..50 keep a stack of the (node, depth) pairs on
    //       the CURRENT root-to-node path that carry value v; the top of stack v
    //       is therefore the DEEPEST such ancestor.
    //       for node i we only look at the <= 50 values coprime with nums[i] and
    //       pick the top with the largest depth.
    //       push i on stack nums[i] before visiting its children and pop it on
    //       the way back up, so each stack always describes the current path.
    //       the DFS is ITERATIVE (n up to 10^5, the tree may be a long chain) -
    //       an "exit" marker frame performs the pop.
    /**
     * time = O(N * 50)
     * space = O(N)
     */
    public int[] getCoprimes(int[] nums, int[][] edges) {
        int n = nums.length;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // coprime[x] = every y in 1..50 with gcd(x, y) == 1
        List<List<Integer>> coprime = new ArrayList<>();
        for (int x = 0; x <= 50; x++) {
            coprime.add(new ArrayList<Integer>());
        }
        for (int x = 1; x <= 50; x++) {
            for (int y = 1; y <= 50; y++) {
                if (gcd(x, y) == 1) {
                    coprime.get(x).add(y);
                }
            }
        }

        // stacks of {node, depth}, one per value (total size <= n at any time)
        List<List<int[]>> stks = new ArrayList<>();
        for (int v = 0; v <= 50; v++) {
            stks.add(new ArrayList<int[]>());
        }

        int[] res = new int[n];
        Arrays.fill(res, -1);

        // iterative DFS: each node is pushed as an ENTER frame then an EXIT frame
        int[] frameNode = new int[2 * n];
        int[] frameParent = new int[2 * n];
        int[] frameDepth = new int[2 * n];
        boolean[] frameExit = new boolean[2 * n];
        int sp = 0;
        frameNode[sp] = 0;
        frameParent[sp] = -1;
        frameDepth[sp] = 0;
        frameExit[sp] = false;
        sp++;

        while (sp > 0) {
            sp--;
            int u = frameNode[sp];
            int fa = frameParent[sp];
            int depth = frameDepth[sp];
            if (frameExit[sp]) {
                List<int[]> st = stks.get(nums[u]);
                st.remove(st.size() - 1);
                continue;
            }

            int best = -1, bestDepth = -1;
            for (int v : coprime.get(nums[u])) {
                List<int[]> st = stks.get(v);
                if (!st.isEmpty()) {
                    int[] top = st.get(st.size() - 1);
                    if (top[1] > bestDepth) {
                        bestDepth = top[1];
                        best = top[0];
                    }
                }
            }
            res[u] = best;

            stks.get(nums[u]).add(new int[]{u, depth});

            // the exit frame goes in first so it pops after the whole subtree
            frameNode[sp] = u;
            frameParent[sp] = fa;
            frameDepth[sp] = depth;
            frameExit[sp] = true;
            sp++;

            for (int v : adj.get(u)) {
                if (v == fa) {
                    continue;
                }
                frameNode[sp] = v;
                frameParent[sp] = u;
                frameDepth[sp] = depth + 1;
                frameExit[sp] = false;
                sp++;
            }
        }

        return res;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }
}
