package LeetCodeJava.BFS;

// https://leetcode.com/problems/bus-routes/description/

import java.util.*;

/**
 * 815. Bus Routes
 * Solved
 * Hard
 * Topics
 * Companies
 * You are given an array routes representing bus routes where routes[i] is a bus route that the ith bus repeats forever.
 * <p>
 * For example, if routes[0] = [1, 5, 7], this means that the 0th bus travels in the sequence 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... forever.
 * You will start at the bus stop source (You are not on any bus initially), and you want to go to the bus stop target. You can travel between bus stops by buses only.
 * <p>
 * Return the least number of buses you must take to travel from source to target. Return -1 if it is not possible.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: routes = [[1,2,7],[3,6,7]], source = 1, target = 6
 * Output: 2
 * Explanation: The best strategy is take the first bus to the bus stop 7, then take the second bus to the bus stop 6.
 * Example 2:
 * <p>
 * Input: routes = [[7,12],[4,5,15],[6],[15,19],[9,12,13]], source = 15, target = 12
 * Output: -1
 * <p>
 * <p>
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= routes.length <= 500.
 * 1 <= routes[i].length <= 105
 * All the values of routes[i] are unique.
 * sum(routes[i].length) <= 105
 * 0 <= routes[i][j] < 106
 * 0 <= source, target < 106
 */
public class BusRoutes {

    // V0
    // TODO : fix below
    // IDEA : BFS
//    public int numBusesToDestination(int[][] routes, int source, int target) {
//
//        // edge case
//        if (routes.length == 1) {
//            if (source == target) {
//                return 0;
//            }
//            return 1;
//        }
//        // build graph
//        Map<Integer, List<Integer>> graph = new HashMap<>();
//        for (int[] x : routes) {
//            int start = x[0];
//            int end = x[1];
//            List<Integer> cur = graph.getOrDefault(start, new ArrayList<>());
//            cur.add(end);
//            graph.put(start, cur); // double check ???
//        }
//        // bfs
//        int cnt = 0;
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(source);
//        Set<Integer> visited = new HashSet<>();
//        while (!queue.isEmpty()) {
//            Integer curr = queue.poll();
//            // visit "neighbor"
//            if (!graph.keySet().isEmpty()) {
//                for (Integer x : graph.get(curr)) {
//                    if (!visited.contains(x)) {
//                        if (target == x) {
//                            return cnt;
//                        }
//                        cnt += 1;
//                        queue.add(x);
//                        visited.add(x);
//                    }
//                }
//            }
//        }
//
//        //return cnt > 0 ? cnt : -1;
//        return -1;
//    }

    // V1
    // IDEA : Breadth-First Search (BFS) with Bus Stops as Nodes
    // https://leetcode.com/problems/bus-routes/editorial/
    /**
     * time = O(N * M)
     * space = O(N * M)
     */
    public int numBusesToDestination_1(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }
        HashMap<Integer, ArrayList<Integer>> adjList = new HashMap<>();
        // Create a map from the bus stop to all the routes that include this stop.
        /**
         * NOTE !!!
         *
         *  the `key` of map is the `route` index
         */
        for (int r = 0; r < routes.length; r++) {
            for (int stop : routes[r]) {
                // Add all the routes that have this stop.
                ArrayList<Integer> route = adjList.getOrDefault(
                        stop,
                        new ArrayList<>()
                );
                route.add(r);
                adjList.put(stop, route);
            }
        }

        Queue<Integer> q = new LinkedList<>();
        Set<Integer> vis = new HashSet<Integer>(routes.length);
        // Insert all the routes in the queue that have the source stop.
        for (int route : adjList.get(source)) {
            q.add(route);
            vis.add(route);
        }

        int busCount = 1;
        while (!q.isEmpty()) {
            int size = q.size();

            for (int i = 0; i < size; i++) {
                int route = q.remove();

                // Iterate over the stops in the current route.
                for (int stop : routes[route]) {
                    // Return the current count if the target is found.
                    if (stop == target) {
                        return busCount;
                    }

                    // Iterate over the next possible routes from the current stop.
                    for (int nextRoute : adjList.get(stop)) {
                        if (!vis.contains(nextRoute)) {
                            vis.add(nextRoute);
                            q.add(nextRoute);
                        }
                    }
                }
            }
            busCount++;
        }
        return -1;
    }

    // V2
    // IDEA : Breadth-First Search (BFS) with Routes as Nodes (test OK)
    // https://leetcode.com/problems/bus-routes/editorial/
    List<List<Integer>> adjList = new ArrayList();
    // Iterate over each pair of routes and add an edge between them if there's a
    // common stop.
    /**
     * time = O(N * N * M)
     * space = O(N * N)
     */
    void createGraph(int[][] routes) {
        for (int i = 0; i < routes.length; i++) {
            for (int j = i + 1; j < routes.length; j++) {
                if (haveCommonNode(routes[i], routes[j])) {
                    adjList.get(i).add(j);
                    adjList.get(j).add(i);
                }
            }
        }
    }

    // Returns true if the provided routes have a common stop, false otherwise.
    /**
     * time = O(M)
     * space = O(1)
     */
    boolean haveCommonNode(int[] route1, int[] route2) {
        int i = 0, j = 0;
        while (i < route1.length && j < route2.length) {
            if (route1[i] == route2[j]) {
                return true;
            }

            if (route1[i] < route2[j]) {
                i++;
            } else {
                j++;
            }
        }
        return false;
    }

    // Add all the routes in the queue that have the source as one of the stops.
    /**
     * time = O(N * M)
     * space = O(1)
     */
    void addStartingNodes(Queue<Integer> q, int[][] routes, int source) {
        for (int i = 0; i < routes.length; i++) {
            if (isStopExist(routes[i], source)) {
                q.add(i);
            }
        }
    }

    // Returns true if the given stop is present in the route, false otherwise.
    /**
     * time = O(M)
     * space = O(1)
     */
    boolean isStopExist(int[] route, int stop) {
        for (int j = 0; j < route.length; j++) {
            if (route[j] == stop) {
                return true;
            }
        }
        return false;
    }

    /**
     * time = O(N * N * M)
     * space = O(N * N)
     */
    public int numBusesToDestination_2(int[][] routes, int source, int target) {
        if (source == target) {
            return 0;
        }

        for (int i = 0; i < routes.length; ++i) {
            Arrays.sort(routes[i]);
            adjList.add(new ArrayList());
        }

        createGraph(routes);

        Queue<Integer> q = new LinkedList<>();
        addStartingNodes(q, routes, source);

        Set<Integer> vis = new HashSet<Integer>(routes.length);
        int busCount = 1;
        while (!q.isEmpty()) {
            int sz = q.size();

            while (sz-- > 0) {
                int node = q.remove();

                // Return busCount, if the stop target is present in the current route.
                if (isStopExist(routes[node], target)) {
                    return busCount;
                }

                // Add the adjacent routes.
                for (int adj : adjList.get(node)) {
                    if (!vis.contains(adj)) {
                        vis.add(adj);
                        q.add(adj);
                    }
                }
            }

            busCount++;
        }

        return -1;
    }

}
