package LeetCodeJava.Greedy;

// https://leetcode.com/problems/shortest-distance-after-road-addition-queries-ii/description/

/**
 * 3244. Shortest Distance After Road Addition Queries II
 * Hard
 *
 * You are given an integer n and a 2D integer array queries.
 *
 * There are n cities numbered from 0 to n - 1. Initially, there is a unidirectional road from city i to city i + 1 for all 0 <= i < n - 1.
 *
 * queries[i] = [u_i, v_i] represents the addition of a new unidirectional road from city u_i to city v_i. After each query, you need to find the length of the shortest path from city 0 to city n - 1.
 *
 * There are no two queries such that queries[i][0] < queries[j][0] < queries[i][1] < queries[j][1].
 *
 * Return an array answer where for each i in the range [0, queries.length - 1], answer[i] is the length of the shortest path from city 0 to city n - 1.
 *
 *
 * Example 1:
 *
 * Input: n = 5, queries = [[2,4],[0,2],[0,4]]
 * Output: [3,2,1]
 * Explanation:
 * After the addition of the road from 2 to 4, the length of the shortest path from 0 to 4 is 3.
 * After the addition of the road from 0 to 2, the length of the shortest path from 0 to 4 is 2.
 * After the addition of the road from 0 to 4, the length of the shortest path from 0 to 4 is 1.
 *
 * Example 2:
 *
 * Input: n = 4, queries = [[0,3],[0,2]]
 * Output: [1,1]
 * Explanation:
 * After the addition of the road from 0 to 3, the length of the shortest path from 0 to 3 is 1.
 * After the addition of the road from 0 to 2, the length of the shortest path remains 1.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 10^5
 * 1 <= queries.length <= 10^5
 * queries[i].length == 2
 * 0 <= queries[i][0] < queries[i][1] < n
 * 1 < queries[i][1] - queries[i][0]
 * There are no two queries such that i != j and queries[i][0] < queries[j][0] < queries[i][1] < queries[j][1].
 * There are no repeated roads among the queries.
 *
 */
public class ShortestDistanceAfterRoadAdditionQueriesII {

    // V0
    // IDEA: NEXT-POINTER SPLICING (NON-CROSSING QUERIES => SHORTCUTS NEST)
    //       no two queries interleave, so any two shortcuts are disjoint or nested.
    //       keep next[i] = successor of city i on the current shortest path.
    //       a new road u -> v removes every city strictly between u and v that is
    //       still on the path (each removal shortens the path by 1), then next[u] = v.
    //       every city is removed at most once overall -> linear total work.
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public int[] shortestDistanceAfterQueries(int n, int[][] queries) {
        // next[i] = successor of city i on the current path; -1 = removed from the path
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            next[i] = i + 1;
        }
        int pathLength = n - 1;

        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int u = queries[q][0];
            int v = queries[q][1];

            // u was already swallowed by a bigger shortcut, or u already jumps to/past v
            if (next[u] == -1 || next[u] >= v) {
                answer[q] = pathLength;
                continue;
            }

            // remove every city strictly between u and v from the path
            int cur = next[u];
            while (cur < v) {
                int after = next[cur];
                next[cur] = -1;
                pathLength--;
                cur = after;
            }
            next[u] = v;
            answer[q] = pathLength;
        }
        return answer;
    }
}
