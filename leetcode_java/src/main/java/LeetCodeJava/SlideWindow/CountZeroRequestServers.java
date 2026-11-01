package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/count-zero-request-servers/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 2747. Count Zero Request Servers
 * Medium
 *
 * You are given an integer n denoting the total number of servers and a 2D 0-indexed integer
 * array logs, where logs[i] = [server_id, time] denotes that the server with id server_id
 * received a request at time time.
 *
 * You are also given an integer x and a 0-indexed integer array queries.
 *
 * Return a 0-indexed integer array arr of length queries.length where arr[i] represents the
 * number of servers that did not receive any requests during the time interval
 * [queries[i] - x, queries[i]].
 *
 * Note that the time intervals are inclusive.
 *
 *
 * Example 1:
 *
 * Input: n = 3, logs = [[1,3],[2,6],[1,5]], x = 5, queries = [10,11]
 * Output: [1,2]
 *
 * Example 2:
 *
 * Input: n = 3, logs = [[2,4],[2,1],[1,2],[3,1]], x = 2, queries = [3,4]
 * Output: [0,1]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= logs.length <= 10^5
 * 1 <= queries.length <= 10^5
 * logs[i].length == 2
 * 1 <= logs[i][0] <= n
 * 1 <= logs[i][1] <= 10^6
 * 1 <= x <= 10^5
 * x < queries[i] <= 10^6
 *
 */
public class CountZeroRequestServers {

    // V0
    // IDEA: OFFLINE QUERIES SORTED BY TIME + SLIDING WINDOW OVER LOGS SORTED BY TIME
    //  every window [q - x, q] has the same width, so answering queries in increasing q
    //  moves both window ends only to the right -> one sweep over the sorted logs.
    /**
     * time = O(L log L + Q log Q)
     * space = O(L + Q)
     */
    public int[] countServers(int n, int[][] logs, int x, int[] queries) {
        // sort a copy of the logs by time
        int[][] sortedLogs = logs.clone();
        Arrays.sort(sortedLogs, (a, b) -> Integer.compare(a[1], b[1]));

        // answer queries in increasing time, but remember where each answer goes
        Integer[] order = new Integer[queries.length];
        for (int i = 0; i < queries.length; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(queries[a], queries[b]));

        int[] result = new int[queries.length];
        // server id -> number of its logs inside the current window
        Map<Integer, Integer> activeCount = new HashMap<>();
        int left = 0;
        int right = 0;

        for (int queryIndex : order) {
            int windowEnd = queries[queryIndex];
            int windowStart = windowEnd - x;

            // admit every log with time <= windowEnd (inclusive)
            while (right < sortedLogs.length && sortedLogs[right][1] <= windowEnd) {
                int server = sortedLogs[right][0];
                activeCount.put(server, activeCount.getOrDefault(server, 0) + 1);
                right++;
            }
            // evict every log with time < windowStart (time == windowStart stays)
            while (left < right && sortedLogs[left][1] < windowStart) {
                int server = sortedLogs[left][0];
                int remaining = activeCount.get(server) - 1;
                // remove the key so size() counts only servers still in the window
                if (remaining == 0) {
                    activeCount.remove(server);
                } else {
                    activeCount.put(server, remaining);
                }
                left++;
            }
            result[queryIndex] = n - activeCount.size();
        }
        return result;
    }
}
