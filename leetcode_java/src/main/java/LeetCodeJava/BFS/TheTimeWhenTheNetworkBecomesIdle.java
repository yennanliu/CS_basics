package LeetCodeJava.BFS;

// https://leetcode.com/problems/the-time-when-the-network-becomes-idle/description/

import java.util.*;

/**
 * 2039. The Time When the Network Becomes Idle
 * Medium
 *
 * There is a network of n servers, labeled from 0 to n - 1. You are given a 2D integer array
 * edges, where edges[i] = [ui, vi] indicates there is a message channel between servers ui and
 * vi, and they can pass any number of messages to each other directly in one second. You are
 * also given a 0-indexed integer array patience of length n.
 *
 * All servers are connected, i.e., a message can be passed from one server to any other
 * server(s) directly or indirectly through the message channels.
 *
 * The server labeled 0 is the master server. The rest are data servers. Each data server needs
 * to send its message to the master server for processing and wait for a reply. Messages move
 * between servers optimally, so every message takes the least amount of time to arrive at the
 * master server. The master server will process all newly arrived messages instantly and send a
 * reply to the originating server via the reversed path the message had gone through.
 *
 * At the beginning of second 0, each data server sends its message to be processed. Starting
 * from second 1, at the beginning of every second, each data server will check if it has
 * received a reply to the message it sent (including any newly arrived replies) from the master
 * server:
 *
 * If it has not, it will resend the message periodically. The data server i will resend the
 * message every patience[i] second(s), i.e., the data server i will resend the message if
 * patience[i] second(s) have elapsed since the last time the message was sent from this server.
 * Otherwise, no more resending will occur from this server.
 *
 * The network becomes idle when there are no messages passing between servers or arriving at
 * servers.
 *
 * Return the earliest second starting from which the network becomes idle.
 *
 *
 * Example 1:
 *
 * Input: edges = [[0,1],[1,2]], patience = [0,2,1]
 * Output: 8
 * Explanation:
 * At (the beginning of) second 0,
 * - Data server 1 sends its message (denoted 1A) to the master server.
 * - Data server 2 sends its message (denoted 2A) to the master server.
 *
 * At second 1,
 * - Message 1A arrives at the master server. Master server processes message 1A instantly and
 *   sends a reply 1A back.
 * - Server 1 has not received any reply. 1 second (1 < patience[1] = 2) elapsed since this server
 *   has sent the message, therefore it does not resend the message.
 * - Server 2 has not received any reply. 1 second (1 == patience[2] = 1) elapsed since this
 *   server has sent the message, therefore it resends the message (denoted 2B).
 *
 * At second 2,
 * - The reply 1A arrives at server 1. No more resending will occur from server 1.
 * - Message 2A arrives at the master server. Master server processes message 2A instantly and
 *   sends a reply 2A back.
 * - Server 2 resends the message (denoted 2C).
 * ...
 * At second 4, the reply 2A arrives at server 2. No more resending will occur from server 2.
 * At second 7, reply 2D arrives at server 2.
 * From the beginning of the second 8, there are no messages passing between servers or arriving
 * at servers.
 * This is the time when the network becomes idle.
 *
 * Example 2:
 *
 * Input: edges = [[0,1],[0,2],[1,2]], patience = [0,10,10]
 * Output: 3
 * Explanation: 0 is the master server. Data servers 1 and 2 send their messages at second 0.
 * The messages arrive at the master server at second 1 and the replies arrive back at second 2.
 * From the beginning of second 3, the network becomes idle.
 *
 *
 * Constraints:
 *
 * n == patience.length
 * 2 <= n <= 10^5
 * patience[0] == 0
 * 1 <= patience[i] <= 10^5 for 1 <= i < n
 * 1 <= edges.length <= min(10^5, n * (n - 1) / 2)
 * edges[i].length == 2
 * 0 <= ui, vi < n
 * ui != vi
 * There are no duplicate edges.
 * Each server is directly connected to a data server.
 *
 */
public class TheTimeWhenTheNetworkBecomesIdle {

    // V0
    // IDEA: BFS SHORTEST PATH FROM THE MASTER + PER-SERVER ARITHMETIC
    //  edges are unweighted, so BFS from 0 gives dist[i]; a round trip takes 2 * dist[i].
    //  server i resends at 0, p, 2p, ... strictly before the first reply lands at
    //  roundTrip, so its last send is at p * ((roundTrip - 1) / p). That message returns
    //  at lastSend + roundTrip, and the network is idle from the following second.
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int networkBecomesIdle(int[][] edges, int[] patience) {
        int n = patience.length;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // BFS from the master server
        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        distance[0] = 0;
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int server = queue.poll();
            for (int next : graph.get(server)) {
                if (distance[next] == -1) {
                    distance[next] = distance[server] + 1;
                    queue.offer(next);
                }
            }
        }

        int idleAt = 0;
        for (int i = 1; i < n; i++) {
            int roundTrip = 2 * distance[i];
            // last resend strictly before the first reply arrives
            int lastSend = patience[i] * ((roundTrip - 1) / patience[i]);
            idleAt = Math.max(idleAt, lastSend + roundTrip + 1);
        }
        return idleAt;
    }
}
