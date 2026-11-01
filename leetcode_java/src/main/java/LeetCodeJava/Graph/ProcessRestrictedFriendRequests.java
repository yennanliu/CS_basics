package LeetCodeJava.Graph;

// https://leetcode.com/problems/process-restricted-friend-requests/description/

import java.util.*;

/**
 * 2076. Process Restricted Friend Requests
 * Hard
 *
 * You are given an integer n indicating the number of people in a network. Each person is labeled from 0 to n - 1.
 *
 * You are also given a 0-indexed 2D integer array restrictions, where restrictions[i] = [xi, yi] means that
 * person xi and person yi cannot become friends, either directly or indirectly through other people.
 *
 * Initially, no one is friends with each other. You are given a list of friend requests as a 0-indexed 2D
 * integer array requests, where requests[j] = [uj, vj] is a friend request between person uj and person vj.
 *
 * A friend request is successful if uj and vj can be friends. Each friend request is processed in the given
 * order (i.e., requests[j] occurs before requests[j + 1]), and upon a successful request, uj and vj become
 * direct friends for all future friend requests.
 *
 * Return a boolean array result, where each result[j] is true if the jth friend request is successful or
 * false if it is not.
 *
 * Note: If uj and vj are already direct friends, the request is still successful.
 *
 *
 * Example 1:
 *
 * Input: n = 3, restrictions = [[0,1]], requests = [[0,2],[2,1]]
 * Output: [true,false]
 *
 * Example 2:
 *
 * Input: n = 3, restrictions = [[0,1]], requests = [[1,2],[0,2]]
 * Output: [true,false]
 *
 * Example 3:
 *
 * Input: n = 5, restrictions = [[0,1],[1,2],[2,3]], requests = [[0,4],[1,2],[3,1],[3,4]]
 * Output: [true,false,true,false]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 0 <= restrictions.length <= 1000
 * restrictions[i].length == 2
 * 0 <= xi, yi <= n - 1
 * xi != yi
 * 1 <= requests.length <= 1000
 * requests[j].length == 2
 * 0 <= uj, vj <= n - 1
 * uj != vj
 *
 */
public class ProcessRestrictedFriendRequests {

    // V0
    // IDEA: UNION FIND WITH A "DRY RUN" CHECK AGAINST EVERY RESTRICTION
    // merging the groups of u and v is allowed unless some restricted pair (x, y) would then
    // share a group, i.e. one of x, y is in u's group and the other in v's. Check every
    // restriction first; union only when none is violated.
    // All sizes are <= 1000, so the O(requests * restrictions) check is fine.
    /**
     * time = O(R * K * alpha(N)), R = requests, K = restrictions
     * space = O(N)
     */
    public boolean[] friendRequests(int n, int[][] restrictions, int[][] requests) {
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        boolean[] result = new boolean[requests.length];
        for (int j = 0; j < requests.length; j++) {
            int rootU = find(parent, requests[j][0]);
            int rootV = find(parent, requests[j][1]);
            // already in one group: nothing new is connected
            if (rootU == rootV) {
                result[j] = true;
                continue;
            }

            boolean allowed = true;
            for (int[] restriction : restrictions) {
                int rootX = find(parent, restriction[0]);
                int rootY = find(parent, restriction[1]);
                boolean joinsPair = (rootX == rootU && rootY == rootV) || (rootX == rootV && rootY == rootU);
                if (joinsPair) {
                    allowed = false;
                    break;
                }
            }

            result[j] = allowed;
            if (allowed) {
                parent[rootU] = rootV;
            }
        }
        return result;
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
