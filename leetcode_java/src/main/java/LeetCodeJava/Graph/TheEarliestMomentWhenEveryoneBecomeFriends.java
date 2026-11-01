package LeetCodeJava.Graph;

// https://leetcode.com/problems/the-earliest-moment-when-everyone-become-friends/description/

import java.util.*;

/**
 * 1101. The Earliest Moment When Everyone Become Friends
 * Medium
 *
 * There are n people in a social group labeled from 0 to n - 1. You are given an
 * array logs where logs[i] = [timestamp_i, x_i, y_i] indicates that x_i and y_i
 * will be friends at the time timestamp_i.
 *
 * Friendship is symmetric. That means if a is friends with b, then b is friends
 * with a. Also, person a is acquainted with a person b if a is friends with b,
 * or a is a friend of someone acquainted with b.
 *
 * Return the earliest time for which every person became acquainted with every
 * other person. If there is no such earliest time, return -1.
 *
 *
 * Example 1:
 *
 * Input: logs = [[20190101,0,1],[20190104,3,4],[20190107,2,3],[20190211,1,5],
 *                [20190224,2,4],[20190301,0,3],[20190312,1,2],[20190322,4,5]], n = 6
 * Output: 20190301
 *
 * Example 2:
 *
 * Input: logs = [[0,2,0],[1,0,1],[3,0,3],[4,1,2],[7,3,1]], n = 4
 * Output: 3
 * Explanation: At timestamp = 3, all the persons (i.e., 0, 1, 2, and 3) become friends.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 100
 * 1 <= logs.length <= 10^4
 * logs[i].length == 3
 * 0 <= timestamp_i <= 10^9
 * 0 <= x_i, y_i <= n - 1
 * x_i != y_i
 * All the values timestamp_i are unique.
 * All the pairs (x_i, y_i) occur at most one time in the input.
 *
 */
public class TheEarliestMomentWhenEveryoneBecomeFriends {

    // V0
    // IDEA: SORT BY TIMESTAMP + UNION FIND (count components)
    // every successful union merges 2 groups -> component count - 1;
    // the answer is the timestamp where the count first drops to 1.
    /**
     * time = O(M log M), M = logs.length
     * space = O(N)
     */
    public int earliestAcq(int[][] logs, int n) {
        // process friendships in chronological order
        int[][] sorted = logs.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        int groups = n;

        for (int[] log : sorted) {
            int rootX = find(parent, log[1]);
            int rootY = find(parent, log[2]);
            // already acquainted: nothing changes
            if (rootX == rootY) {
                continue;
            }
            parent[rootX] = rootY;
            groups--;
            // everyone is in one group now
            if (groups == 1) {
                return log[0];
            }
        }
        return -1;
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
