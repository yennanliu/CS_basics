package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-pairs-of-connectable-servers-in-a-weighted-tree-network/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 *  3067. Count Pairs of Connectable Servers in a Weighted Tree Network
 *  Medium
 *
 *  You are given an unrooted weighted tree with n vertices representing servers
 *  numbered from 0 to n - 1, an array edges where edges[i] = [ai, bi, weighti]
 *  represents a bidirectional edge between vertices ai and bi of weight weighti.
 *  You are also given an integer signalSpeed.
 *
 *  Two servers a and b are connectable through a server c if:
 *    a < b, a != c and b != c.
 *    The distance from c to a is divisible by signalSpeed.
 *    The distance from c to b is divisible by signalSpeed.
 *    The path from c to a and the path from c to b do not share any edges.
 *
 *  Return an integer array count of length n where count[i] is the number of server
 *  pairs that are connectable through the server i.
 *
 *  Example 1:
 *    Input: edges = [[0,1,1],[1,2,5],[2,3,13],[3,4,9],[4,5,2]], signalSpeed = 1
 *    Output: [0,4,6,6,4,0]
 *
 *  Example 2:
 *    Input: edges = [[0,6,3],[6,5,3],[0,3,1],[3,2,7],[3,1,6],[3,4,2]], signalSpeed = 3
 *    Output: [2,0,0,0,0,0,2]
 *
 *  Constraints:
 *    2 <= n <= 1000
 *    edges.length == n - 1
 *    edges[i].length == 3
 *    0 <= ai, bi < n
 *    1 <= weighti <= 10^6
 *    1 <= signalSpeed <= 10^6
 *    The input is generated such that edges represents a valid tree.
 */
public class CountPairsOfConnectableServersInAWeightedTreeNetwork {

    // V0
    // IDEA: ROOT AT EACH SERVER, COUNT PER BRANCH, MULTIPLY ACROSS BRANCHES
    //       "the two paths share no edge" means a and b must leave c through
    //       DIFFERENT neighbours - every subtree hanging off c is one branch.
    //       so for a fixed c, walk each branch and count how many vertices sit at a
    //       distance divisible by signalSpeed. If the branches yield c1..cm such
    //       vertices, the connectable pairs through c number  sum_{i<j} ci * cj,
    //       which a running total computes in one pass:
    //           res += prevTotal * ci ;  prevTotal += ci
    //       n <= 1000, so repeating the whole DFS from every c is O(n^2) - fine.
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int[] countPairsOfConnectableServers(int[][] edges, int signalSpeed) {

        int n = edges.length + 1;

        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<int[]>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(new int[]{e[1], e[2]});
            adj.get(e[1]).add(new int[]{e[0], e[2]});
        }

        int[] res = new int[n];

        for (int c = 0; c < n; c++) {
            int prevTotal = 0;
            for (int[] br : adj.get(c)) {
                int cnt = countDivisible(adj, br[0], c, br[1], signalSpeed);
                res[c] += prevTotal * cnt;
                prevTotal += cnt;
            }
        }

        return res;
    }

    // how many vertices inside the branch rooted at `start` (entered from `blocked`)
    // sit at a distance from `blocked` divisible by signalSpeed
    private int countDivisible(List<List<int[]>> adj, int start, int blocked,
                               long startDist, int signalSpeed) {

        int cnt = 0;
        // stack entries : {node, parent, distance-from-blocked}
        Deque<long[]> stack = new ArrayDeque<>();
        stack.push(new long[]{start, blocked, startDist});

        while (!stack.isEmpty()) {
            long[] cur = stack.pop();
            int node = (int) cur[0];
            int parent = (int) cur[1];
            long dist = cur[2];

            if (dist % signalSpeed == 0) {
                cnt++;
            }
            for (int[] nb : adj.get(node)) {
                if (nb[0] != parent) {
                    stack.push(new long[]{nb[0], node, dist + nb[1]});
                }
            }
        }
        return cnt;
    }
}
