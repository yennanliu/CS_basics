package LeetCodeJava.Sort;

// https://leetcode.com/problems/closest-room/

import java.util.Arrays;
import java.util.TreeSet;

/**
 *  1847. Closest Room
 *  Hard
 *
 *  There is a hotel with n rooms. The rooms are represented by a 2D integer array
 *  rooms where rooms[i] = [roomId_i, size_i] denotes that there is a room with
 *  room number roomId_i and size equal to size_i. Each roomId_i is unique.
 *
 *  You are also given k queries in a 2D array queries where
 *  queries[j] = [preferred_j, minSize_j]. The answer to the jth query is the room
 *  number id of a room such that:
 *    - the room has a size of at least minSize_j, and
 *    - abs(id - preferred_j) is minimized.
 *  If there is a tie in the absolute difference, use the room with the smallest
 *  such id. If there is no such room, the answer is -1.
 *
 *  Return an array answer of length k where answer[j] is the answer to the jth query.
 *
 *  Example 1:
 *    Input: rooms = [[2,2],[1,2],[3,2]], queries = [[3,1],[3,3],[5,2]]
 *    Output: [3,-1,3]
 *
 *  Example 2:
 *    Input: rooms = [[1,4],[2,3],[3,5],[4,1],[5,2]], queries = [[2,3],[2,4],[2,5]]
 *    Output: [2,1,3]
 *    Explanation: for [2,4] both rooms 1 and 3 qualify, ties go to the smaller id.
 *
 *  Constraints:
 *    n == rooms.length
 *    1 <= n <= 10^5
 *    k == queries.length
 *    1 <= k <= 10^4
 *    1 <= roomId_i, preferred_j <= 10^7
 *    1 <= size_i, minSize_j <= 10^7
 */
public class ClosestRoom {

    // V0
    // IDEA: OFFLINE SORT BY SIZE (descending) + BALANCED TREE OF ACTIVE ROOM IDS
    //       the size filter is a moving threshold, so answer the queries OFFLINE:
    //       sort queries by minSize descending and rooms by size descending.
    //       sweeping the queries in that order only ever ACTIVATES rooms, never
    //       removes them, so one TreeSet of active ids suffices.
    //       for each query, floor(preferred) / ceiling(preferred) are the only two
    //       candidates; ties go to the SMALLER id, i.e. prefer the floor when the
    //       two distances are equal.
    /**
     * time = O((n + k) log n)
     * space = O(n)
     */
    public int[] closestRoom(int[][] rooms, int[][] queries) {
        int k = queries.length;

        // sort rooms by size descending
        int[][] sortedRooms = rooms.clone();
        Arrays.sort(sortedRooms, (a, b) -> Integer.compare(b[1], a[1]));

        // query order by minSize descending (keep the original index)
        Integer[] order = new Integer[k];
        for (int j = 0; j < k; j++) {
            order[j] = j;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(queries[b][1], queries[a][1]));

        TreeSet<Integer> active = new TreeSet<>();
        int[] res = new int[k];
        Arrays.fill(res, -1);

        int i = 0;
        for (int idx = 0; idx < k; idx++) {
            int j = order[idx];
            int preferred = queries[j][0];
            int minSize = queries[j][1];

            while (i < sortedRooms.length && sortedRooms[i][1] >= minSize) {
                active.add(sortedRooms[i][0]);
                i++;
            }
            if (active.isEmpty()) {
                continue;
            }

            Integer lo = active.floor(preferred);
            Integer hi = active.ceiling(preferred);
            int best = -1;
            if (lo != null) {
                best = lo;
            }
            if (hi != null && (best == -1 || (hi - preferred) < (preferred - best))) {
                best = hi;
            }
            res[j] = best;
        }
        return res;
    }
}
