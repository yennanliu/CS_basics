package LeetCodeJava.DFS;

// https://leetcode.com/problems/number-of-operations-to-make-network-connected/

/**
 *  1319. Number of Operations to Make Network Connected
 *  Medium
 *
 *  There are n computers numbered from 0 to n - 1 connected by ethernet cables
 *  connections forming a network where connections[i] = [ai, bi] represents a
 *  connection between computers ai and bi.
 *
 *  You are given an initial computer network connections. You can extract certain
 *  cables between two directly connected computers, and place them between any
 *  pair of disconnected computers to make them directly connected.
 *
 *  Return the minimum number of times you need to do this in order to make all
 *  the computers connected. If it is not possible, return -1.
 *
 *  Example 1:
 *    Input: n = 4, connections = [[0,1],[0,2],[1,2]]
 *    Output: 1
 *    Explanation: Remove the cable 1-2 and place it between computers 1 and 3.
 *
 *  Example 3:
 *    Input: n = 6, connections = [[0,1],[0,2],[0,3],[1,2]]
 *    Output: -1
 *    Explanation: There are not enough cables.
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    1 <= connections.length <= min(n * (n - 1) / 2, 10^5)
 *    connections[i].length == 2
 *    0 <= ai, bi < n
 *    ai != bi
 *    There are no repeated connections.
 */
public class NumberOfOperationsToMakeNetworkConnected {

    private int[] parent;

    // V0
    // IDEA: UNION FIND (count components; every spare cable joins two of them)
    //       connecting k components into one always needs exactly k - 1 cables.
    //       a cable is "spare" when its two endpoints are ALREADY in the same
    //       component - unplugging it breaks nothing.
    //       so run union-find, then
    //         answer = components - 1  if spare >= components - 1  else -1.
    //       (with fewer than n - 1 cables in total the network can never be
    //        connected, which is exactly the spare < components - 1 test.)
    /**
     * time = O((N + M) * alpha(N))
     * space = O(N)
     */
    public int makeConnected(int n, int[][] connections) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int components = n;
        int spare = 0;
        for (int[] c : connections) {
            int ra = find(c[0]), rb = find(c[1]);
            if (ra == rb) {
                spare++;
            } else {
                parent[ra] = rb;
                components--;
            }
        }

        int need = components - 1;
        return spare >= need ? need : -1;
    }

    private int find(int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }
}
