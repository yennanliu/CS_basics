package LeetCodeJava.Graph;

// https://leetcode.com/problems/graph-connectivity-with-threshold/description/

import java.util.*;

/**
 * 1627. Graph Connectivity With Threshold
 * Hard
 *
 * We have n cities labeled from 1 to n. Two different cities with labels x and y are directly connected by a
 * bidirectional road if and only if x and y share a common divisor strictly greater than some threshold.
 * More formally, cities with labels x and y have a road between them if there exists an integer z such that
 * all of the following are true:
 *
 * x % z == 0,
 * y % z == 0, and
 * z > threshold.
 *
 * Given the two integers, n and threshold, and an array of queries, you must determine for each
 * queries[i] = [ai, bi] if cities ai and bi are connected directly or indirectly. (i.e. there is some path
 * between them).
 *
 * Return an array answer, where answer.length == queries.length and answer[i] is true if for the ith query,
 * there is a path between ai and bi, or answer[i] is false if there is no path.
 *
 *
 * Example 1:
 *
 * Input: n = 6, threshold = 2, queries = [[1,4],[2,5],[3,6]]
 * Output: [false,false,true]
 *
 * Example 2:
 *
 * Input: n = 6, threshold = 0, queries = [[4,5],[3,4],[3,2],[2,6],[1,3]]
 * Output: [true,true,true,true,true]
 *
 * Example 3:
 *
 * Input: n = 5, threshold = 1, queries = [[4,5],[4,5],[3,2],[2,3],[3,4]]
 * Output: [false,false,false,false,false]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^4
 * 0 <= threshold <= n
 * 1 <= queries.length <= 10^5
 * queries[i].length == 2
 * 1 <= ai, bi <= cities
 * ai != bi
 *
 */
public class GraphConnectivityWithThreshold {

    // V0
    // IDEA: UNION FIND OVER MULTIPLES (sieve-style edge generation)
    // no need to link every pair sharing a divisor z > threshold: for each such z, union z
    // with its multiples 2z, 3z, ... <= n. Any two numbers sharing z then meet through z.
    // The sieve is n/1 + n/2 + ... = O(N log N) unions.
    /**
     * time = O(N log N * alpha(N) + Q * alpha(N))
     * space = O(N)
     */
    public List<Boolean> areConnected(int n, int threshold, int[][] queries) {
        int[] parent = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        for (int divisor = threshold + 1; divisor <= n; divisor++) {
            for (int multiple = 2 * divisor; multiple <= n; multiple += divisor) {
                int rootA = find(parent, divisor);
                int rootB = find(parent, multiple);
                if (rootA != rootB) {
                    parent[rootA] = rootB;
                }
            }
        }

        List<Boolean> answer = new ArrayList<>();
        for (int[] query : queries) {
            answer.add(find(parent, query[0]) == find(parent, query[1]));
        }
        return answer;
    }

    /** Returns the root of x, compressing the path on the way. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }
}
