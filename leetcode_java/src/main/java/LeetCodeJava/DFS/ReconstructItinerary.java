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

    // V0-1
    // IDEA: TOPOLOGICAL SORT
    /**  NOTE !!!  `TOPOLOGICAL SORT` is NOT WORKING for this LC problem:
     *
     *   Strictly speaking, **LC 332. Reconstruct Itinerary**
     *   CAN NOT be solved using a standard **Topological Sort** (Kahn's algorithm).
     *
     * ### Why Topological Sort doesn't work:
     *
     * 1. **Cycles:** Topological sort ONLY works on
           Directed Acyclic Graphs (DAGs).

     *     The flight itinerary problem frequently contains
     *     cycles (e.g., JFK → SFO → JFK).
     *     A standard topological sort would get stuck in
     *     a cycle and fail to process the nodes.
     *
     *
     * 2. **Edge-based vs. Node-based:**
     *
     *     Topological sort
     *     orders **nodes** based on dependencies. This problem is
     *     about using every **edge** (ticket) exactly once.
     *
     *
     *
     *
     * ### The Correct Approach: Hierholzer's Algorithm
     *
     * This problem is actually a quest for an **Eulerian Path** in a directed graph. An Eulerian Path is a trail that visits every edge exactly once.
     *
     * While it isn't "Topological Sort," the algorithm used (Hierholzer’s) looks very similar to a **Post-Order DFS** (which is how you implement Topological Sort using DFS). The main difference is that you "consume" the edges as you go to handle the cycles.
     *
     * ---
     *
     * ### 🛠️ Java Solution (Hierholzer's Algorithm)
     *
     * ```java
     * import java.util.*;
     *
     * class Solution {
     *     // We use a Map to store the adjacency list.
     *     // A PriorityQueue is used for neighbors to ensure lexical order (as required by LC).
     *     Map<String, PriorityQueue<String>> adj = new HashMap<>();
     *     LinkedList<String> result = new LinkedList<>();
     *
     *     public List<String> findItinerary(List<List<String>> tickets) {
     *         // 1. Build the graph
     *         for (List<String> ticket : tickets) {
     *             adj.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).add(ticket.get(1));
     *         }
     *
     *         // 2. Start DFS from JFK
     *         visit("JFK");
     *
     *         return result;
     *     }
     *
     *     private void visit(String airport) {
     *         PriorityQueue<String> neighbors = adj.get(airport);
     *
     *         // 3. Greedy DFS: Always pick the lexically smallest destination first.
     *         // Crucially, we POLL (remove) the edge so it's only used once.
     *         while (neighbors != null && !neighbors.isEmpty()) {
     *             String next = neighbors.poll();
     *             visit(next);
     *         }
     *
     *         // 4. Post-order: Add the airport to the front of the list
     *         // after all its outgoing edges are consumed.
     *         result.addFirst(airport);
     *     }
     * }
     *
     * ```
     *
     * ---
     *
     * ### 💡 Key Differences to Keep in Mind
     *
     * | Feature | Topological Sort (DFS) | Hierholzer's (Eulerian Path) |
     * | --- | --- | --- |
     * | **Graph Type** | Must be a DAG (No cycles). | Can have cycles. |
     * | **Usage** | Orders nodes by dependency. | Uses every **edge** exactly once. |
     * | **Mechanism** | Mark **node** as visited. | Remove/consume the **edge** after use. |
     * | **Result** | Linear ordering of tasks. | A continuous path/circuit. |
     *
     * ### Why `addFirst`?
     *
     * In Hierholzer's algorithm, if you get stuck at a node (no more outgoing edges), it means that node must be the **end** of the itinerary (or part of the end). By adding it to the front of a linked list at the end of the recursive call, you are effectively reversing the "stuck" order to produce the correct path from start to finish.
     *
     *
     */

    // V0-2
    // IDEA: Hierholzer's algorithm (gemini)
    // We use a Map to store the adjacency list.
    // A PriorityQueue is used for neighbors to ensure lexical order (as required by LC).
    Map<String, PriorityQueue<String>> adj = new HashMap<>();
    LinkedList<String> result = new LinkedList<>();

    public List<String> findItinerary_0_2(List<List<String>> tickets) {
        // 1. Build the graph
        for (List<String> ticket : tickets) {
            adj.computeIfAbsent(ticket.get(0), k -> new PriorityQueue<>()).add(ticket.get(1));
        }

        // 2. Start DFS from JFK
        visit("JFK");

        return result;
    }

    private void visit(String airport) {
        PriorityQueue<String> neighbors = adj.get(airport);

        // 3. Greedy DFS: Always pick the lexically smallest destination first.
        // Crucially, we POLL (remove) the edge so it's only used once.
        while (neighbors != null && !neighbors.isEmpty()) {
            String next = neighbors.poll();
            visit(next);
        }

        // 4. Post-order: Add the airport to the front of the list
        // after all its outgoing edges are consumed.
        result.addFirst(airport);
    }


    // V1-1
    // https://neetcode.io/problems/reconstruct-flight-path
    // IDEA: DFS
    /**
     * time = O(E log E)
     * space = O(E)
     */
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
    /**
     *
     *
     * 🔁 DFS Method
     *
     * private boolean dfs(String src, List<String> res,
     *                     Map<String, List<String>> adj, int targetLen)
     *
     * 📍 Base Case:
     *
     * if (res.size() == targetLen) return true;
     *
     * 	•	If we’ve used all tickets (res includes all nodes), return true.
     *
     * ⸻
     *
     * ⛔ Dead-end Check:
     *
     * if (!adj.containsKey(src)) return false;
     *
     * 	•	No more neighbors = invalid path
     *
     * ⸻
     *
     * 🔁 For each neighbor:
     *
     * List<String> temp = new ArrayList<>(adj.get(src));
     *
     * 	•	Iterate over a copy of the list (so we can backtrack safely).
     *
     * adj.get(src).remove(i);
     * res.add(v);
     * if (dfs(v, res, adj, targetLen)) return true;
     * adj.get(src).add(i, v);
     * res.remove(res.size() - 1);
     *
     * 	•	Try:
     * 	1.	Take ticket src → v
     * 	2.	Add to result path
     * 	3.	Recurse deeper
     * 	4.	If it fails, undo (backtrack)
     *
     * ⸻
     *
     * 🧪 Example:
     *
     * Input:
     *
     * tickets = [["MUC", "LHR"], ["JFK", "MUC"], ["LHR", "SFO"]]
     *
     * Sorted:
     *
     * JFK → MUC
     * MUC → LHR
     * LHR → SFO
     *
     * DFS builds:
     * ["JFK", "MUC", "LHR", "SFO"]
     *
     * ⸻
     *
     * ✅ Why it works:
     * 	•	Lexical sort ensures smallest path comes first.
     * 	•	Backtracking guarantees every path is tried.
     * 	•	Tickets used exactly once via in-place mutation + backtrack.

     *
     */
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
    /**
     * time = O(E log E)
     * space = O(E)
     */
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
    /**
     * time = O(E log E)
     * space = O(E)
     */
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
    /**
     * time = O(E log E)
     * space = O(E)
     */
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
    /**
     * time = O(E log E)
     * space = O(E)
     */
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
