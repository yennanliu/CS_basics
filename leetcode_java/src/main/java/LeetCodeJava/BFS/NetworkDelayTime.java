package LeetCodeJava.BFS;

// https://leetcode.com/problems/network-delay-time/description/

import java.util.*;

/**
 *
 * 743. Network Delay Time
 *
 * You are given a network of n nodes, labeled from 1 to n. You are also given times, a list of travel times as directed edges times[i] = (ui, vi, wi), where ui is the source node, vi is the target node, and wi is the time it takes for a signal to travel from source to target.
 *
 * We will send a signal from a given node k. Return the minimum time it takes for all the n nodes to receive the signal. If it is impossible for all the n nodes to receive the signal, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, k = 2
 * Output: 2
 * Example 2:
 *
 * Input: times = [[1,2,1]], n = 2, k = 1
 * Output: 1
 * Example 3:
 *
 * Input: times = [[1,2,1]], n = 2, k = 2
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= k <= n <= 100
 * 1 <= times.length <= 6000
 * times[i].length == 3
 * 1 <= ui, vi <= n
 * ui != vi
 * 0 <= wi <= 100
 * All the pairs (ui, vi) are unique. (i.e., no multiple edges.)
 *
 *
 */
public class NetworkDelayTime {

    // V0
    // TODO : implement
//    public int networkDelayTime(int[][] times, int n, int k) {
//
//    }

    // V1
    // IDEA : Dijlstra's Algorithm
    // https://leetcode.com/problems/network-delay-time/solutions/2310813/dijkstra-s-algorithm-template-list-of-problems/
    /*
    Step 1: Create a Map of start and end nodes with weight
            1 -> {2,1},{3,2}
            2 -> {4,4},{5,5}
            3 -> {5,3}
            4 ->
            5 ->

    Step 2: create a result array where we will keep track the minimum distance to rech end of the node from start node

    Step 3: Create a Queue and add starting position with it's weight and add it's reachable distance with increament of own't weight plus a weight require to reach at the end node from start node.
            We keep adding and removing pairs from queue and updating result array as well.

    Step 4: find the maximum value from result array:

    */
    public int networkDelayTime_1(int[][] times, int n, int k) {

        //Step 1
        Map<Integer, Map<Integer, Integer>> map = new HashMap<>();

        for(int[] time : times) {
            int start = time[0];
            int end = time[1];
            int weight = time[2];

            map.putIfAbsent(start, new HashMap<>());
            map.get(start).put(end, weight);
        }

        // Step 2
        int[] dis = new int[n+1];
        Arrays.fill(dis, Integer.MAX_VALUE);
        dis[k] = 0;

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{k,0});

        //Step 3:
        while(!queue.isEmpty()) {
            int[] cur = queue.poll();
            int curNode = cur[0];
            int curWeight = cur[1];

            for(int next : map.getOrDefault(curNode, new HashMap<>()).keySet()) {
                int nextweight = map.get(curNode).get(next);

                if(curWeight + nextweight < dis[next]) {
                    dis[next] = curWeight + nextweight;
                    queue.add(new int[]{next, curWeight + nextweight});
                }
            }
        }

        //Step 4:
        int res = 0;
        for(int i=1; i<=n; i++) {
            if(dis[i] > res) {
                res = Math.max(res, dis[i]);
            }
        }

        return res == Integer.MAX_VALUE ? -1 : res;
    }


    // V2_1
    // IDEA : Dijlstra's Algorithm V1
    // https://leetcode.com/problems/network-delay-time/submissions/1409037231/


    // V2_2
    // IDEA : Dijlstra's Algorithm V2
    // https://leetcode.com/problems/network-delay-time/submissions/1409037231/

    // V3
    // IDEA : Dijlstra
    // https://leetcode.com/problems/network-delay-time/submissions/1409038407/
    public int networkDelayTime_3(int[][] times, int n, int K) {
        int[][] graph = new int[n][n];
        for(int i = 0; i < n ; i++) Arrays.fill(graph[i], Integer.MAX_VALUE);
        for( int[] rows : times) graph[rows[0] - 1][rows[1] - 1] =  rows[2];

        int[] distance = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[K - 1] = 0;

        boolean[] visited = new boolean[n];
        for(int i = 0; i < n ; i++){
            int v = minIndex(distance, visited);
            if(v == -1)continue;
            visited[v] = true;
            for(int j = 0; j < n; j++){
                if(graph[v][j] != Integer.MAX_VALUE){
                    int newDist = graph[v][j] + distance[v];
                    if(newDist < distance[j]) distance[j] = newDist;
                }
            }
        }
        int result = 0;
        for(int dist : distance){
            if(dist == Integer.MAX_VALUE) return -1;
            result = Math.max(result, dist);
        }
        return result;
    }

    private int minIndex(int[] distance, boolean[] visited){
        int min = Integer.MAX_VALUE, minIndex = -1;
        for(int i = 0; i < distance.length; i++){
            if(!visited[i] && distance[i] < min){
                min = distance[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

}
