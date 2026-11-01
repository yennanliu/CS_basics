package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximum-employees-to-be-invited-to-a-meeting/description/

import java.util.*;

/**
 * 2127. Maximum Employees to Be Invited to a Meeting
 * Hard
 *
 * A company is organizing a meeting and has a list of n employees, waiting to be invited. They have arranged
 * for a large circular table, capable of seating any number of employees.
 *
 * The employees are numbered from 0 to n - 1. Each employee has a favorite person and they will attend the
 * meeting only if they can sit next to their favorite person at the table. The favorite person of an
 * employee is not themself.
 *
 * Given a 0-indexed integer array favorite, where favorite[i] denotes the favorite person of the ith
 * employee, return the maximum number of employees that can be invited to the meeting.
 *
 *
 * Example 1:
 *
 * Input: favorite = [2,2,1,2]
 * Output: 3
 *
 * Example 2:
 *
 * Input: favorite = [1,2,0]
 * Output: 3
 *
 * Example 3:
 *
 * Input: favorite = [3,0,1,4,1]
 * Output: 4
 *
 *
 * Constraints:
 *
 * n == favorite.length
 * 2 <= n <= 10^5
 * 0 <= favorite[i] <= n - 1
 * favorite[i] != i
 *
 */
public class MaximumEmployeesToBeInvitedToAMeeting {

    // V0
    // IDEA: FUNCTIONAL GRAPH -- EITHER ONE BIG CYCLE, OR ALL 2-CYCLES WITH THEIR TAILS
    // every employee has exactly one outgoing edge, so the graph is cycles with trees hanging
    // off them. Two seatings are possible:
    //   1) one cycle of length >= 3 around the whole table (nothing else fits) -> its length;
    //   2) every mutual pair (2-cycle) with its longest incoming chain on each side; all such
    //      pairs fit at once, so their values add up.
    // step 1: peel the non-cycle nodes with Kahn's topological pass, recording
    //         depth[v] = longest chain ending at v (v included).
    // step 2: walk the remaining cycles: max over long cycles, sum over 2-cycles.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumInvitations(int[] favorite) {
        int n = favorite.length;
        int[] inDegree = new int[n];
        for (int fav : favorite) {
            inDegree[fav]++;
        }

        int[] depth = new int[n];
        Arrays.fill(depth, 1);
        boolean[] visited = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        // step 1: remove every node that is not on a cycle
        while (!queue.isEmpty()) {
            int node = queue.poll();
            visited[node] = true;
            int next = favorite[node];
            depth[next] = Math.max(depth[next], depth[node] + 1);
            inDegree[next]--;
            if (inDegree[next] == 0) {
                queue.offer(next);
            }
        }

        // step 2: only cycle nodes are left unvisited
        int longestCycle = 0;
        int pairsTotal = 0;
        for (int i = 0; i < n; i++) {
            if (visited[i]) {
                continue;
            }
            int length = 0;
            int node = i;
            while (!visited[node]) {
                visited[node] = true;
                length++;
                node = favorite[node];
            }
            if (length == 2) {
                // the pair plus the longest chain hanging off each side
                pairsTotal += depth[i] + depth[favorite[i]];
            } else {
                longestCycle = Math.max(longestCycle, length);
            }
        }
        return Math.max(longestCycle, pairsTotal);
    }
}
