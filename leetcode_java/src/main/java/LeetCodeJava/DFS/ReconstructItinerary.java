package LeetCodeJava.DFS;

// https://leetcode.com/problems/reconstruct-itinerary/description/

import java.util.*;

/**
 * 332. Reconstruct Itinerary
 * Hard
 * Topics
 * Companies
 * You are given a list of airline tickets where tickets[i] = [fromi, toi] represent the departure and the arrival airports of one flight. Reconstruct the itinerary in order and return it.
 *
 * All of the tickets belong to a man who departs from "JFK", thus, the itinerary must begin with "JFK". If there are multiple valid itineraries, you should return the itinerary that has the smallest lexical order when read as a single string.
 *
 * For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].
 * You may assume all tickets form at least one valid itinerary. You must use all the tickets once and only once.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: tickets = [["MUC","LHR"],["JFK","MUC"],["SFO","SJC"],["LHR","SFO"]]
 * Output: ["JFK","MUC","LHR","SFO","SJC"]
 * Example 2:
 *
 *
 * Input: tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
 * Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
 * Explanation: Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"] but it is larger in lexical order.
 *
 *
 * Constraints:
 *
 * 1 <= tickets.length <= 300
 * tickets[i].length == 2
 * fromi.length == 3
 * toi.length == 3
 * fromi and toi consist of uppercase English letters.
 * fromi != toi
 *
 */
public class ReconstructItinerary {

    // V0
//    public List<String> findItinerary(List<List<String>> tickets) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/reconstruct-flight-path
    // IDEA: DFS
    public List<String> findItinerary_1_1(List<List<String>> tickets) {
        Map<String, List<String>> adj = new HashMap<>();
        for (List<String> ticket : tickets) {
            adj.putIfAbsent(ticket.get(0), new ArrayList<>());
        }

        tickets.sort((a, b) -> a.get(1).compareTo(b.get(1)));
        for (List<String> ticket : tickets) {
            adj.get(ticket.get(0)).add(ticket.get(1));
        }

        List<String> res = new ArrayList<>();
        res.add("JFK");

        if (dfs("JFK", res, adj, tickets.size() + 1)) {
            return res;
        }
        return new ArrayList<>();
    }

    private boolean dfs(String src, List<String> res,
                        Map<String, List<String>> adj, int targetLen) {
        if (res.size() == targetLen) {
            return true;
        }

        if (!adj.containsKey(src)) {
            return false;
        }

        List<String> temp = new ArrayList<>(adj.get(src));
        for (int i = 0; i < temp.size(); i++) {
            String v = temp.get(i);
            adj.get(src).remove(i);
            res.add(v);
            if (dfs(v, res, adj, targetLen)) return true;
            adj.get(src).add(i, v);
            res.remove(res.size() - 1);
        }
        return false;
    }


    // V1-2
    // https://neetcode.io/problems/reconstruct-flight-path
    // IDEA: Hierholzer's Algorithm (Recursion)
    public List<String> findItinerary_1_2(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> adj = new HashMap<>();
        for (List<String> ticket : tickets) {
            String src = ticket.get(0);
            String dst = ticket.get(1);
            adj.computeIfAbsent(src, k -> new PriorityQueue<>()).offer(dst);
        }

        List<String> res = new ArrayList<>();
        dfs(adj, "JFK", res);

        Collections.reverse(res);
        return res;
    }

    private void dfs(Map<String, PriorityQueue<String>> adj,
                     String src, List<String> res) {
        PriorityQueue<String> queue = adj.get(src);
        while (queue != null && !queue.isEmpty()) {
            String dst = queue.poll();
            dfs(adj, dst, res);
        }
        res.add(src);
    }


    // V1-3
    // https://neetcode.io/problems/reconstruct-flight-path
    // IDEA: Hierholzer's Algorithm (Iteration)
    public List<String> findItinerary_1_3(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> adj = new HashMap<>();
        for (List<String> ticket : tickets) {
            adj.computeIfAbsent(ticket.get(0),
                    k -> new PriorityQueue<>()).add(ticket.get(1));
        }

        LinkedList<String> res = new LinkedList<>();
        Stack<String> stack = new Stack<>();
        stack.push("JFK");

        while (!stack.isEmpty()) {
            String curr = stack.peek();
            if (!adj.containsKey(curr) || adj.get(curr).isEmpty()) {
                res.addFirst(stack.pop());
            } else {
                stack.push(adj.get(curr).poll());
            }
        }

        return res;
    }


    // V2
    // https://leetcode.com/problems/reconstruct-itinerary/solutions/4041944/9576-dfs-recursive-iterative-by-vanamsen-62uy/
    // IDEA : DFS
    public List<String> findItinerary_2(List<List<String>> tickets) {
        Map<String, PriorityQueue<String>> graph = new HashMap<>();

        for (List<String> ticket : tickets) {
            graph.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).add(ticket.get(1));
        }

        LinkedList<String> stack = new LinkedList<>();
        stack.add("JFK");
        LinkedList<String> itinerary = new LinkedList<>();

        while (!stack.isEmpty()) {
            while (graph.getOrDefault(stack.peekLast(), new PriorityQueue<>()).size() > 0) {
                stack.add(graph.get(stack.peekLast()).poll());
            }
            itinerary.addFirst(stack.pollLast());
        }

        return itinerary;
    }

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/reconstruct-itinerary/solutions/6113294/simple-solution-with-diagrams-in-video-j-67os/
    public List<String> findItinerary_3(List<List<String>> tickets) {
        Map<String, List<String>> flightMap = new HashMap<>();
        List<String> result = new LinkedList<>();

        // Populate the flight map with each departure and arrival
        for (List<String> ticket : tickets) {
            String departure = ticket.get(0);
            String arrival = ticket.get(1);
            flightMap.putIfAbsent(departure, new ArrayList<>());
            flightMap.get(departure).add(arrival);
        }

        // Sort each list of destinations in reverse lexicographical order
        for (Map.Entry<String, List<String>> entry : flightMap.entrySet()) {
            Collections.sort(entry.getValue(), Collections.reverseOrder());
        }

        // Perform DFS traversal
        dfsTraversal("JFK", flightMap, result);

        return result;
    }

    private void dfsTraversal(String current, Map<String, List<String>> flightMap, List<String> result) {
        List<String> destinations = flightMap.get(current);

        // Traverse all destinations in the order of their lexicographical
        // sorting
        while (destinations != null && !destinations.isEmpty()) {
            String nextDestination = destinations.remove(destinations.size() - 1);
            dfsTraversal(nextDestination, flightMap, result);
        }

        // Append the current airport to the result after all destinations are
        // visited
        result.add(0, current);
    }

    // V4
    // https://leetcode.com/problems/reconstruct-itinerary/solutions/78768/short-ruby-python-java-c-by-stefanpochma-forx/

    // V5

}
