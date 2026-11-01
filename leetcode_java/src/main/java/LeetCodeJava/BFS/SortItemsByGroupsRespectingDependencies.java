package LeetCodeJava.BFS;

// https://leetcode.com/problems/sort-items-by-groups-respecting-dependencies/description/

import java.util.*;

/**
 * 1203. Sort Items by Groups Respecting Dependencies
 * Hard
 *
 * There are n items each belonging to zero or one of m groups where group[i] is
 * the group that the i-th item belongs to and it's equal to -1 if the i-th item
 * belongs to no group. The items and the groups are zero indexed. A group can have
 * no item belonging to it.
 *
 * Return a sorted list of the items such that:
 *
 * The items that belong to the same group are next to each other in the sorted
 * list.
 * There are some relations between these items where beforeItems[i] is a list
 * containing all the items that should come before the i-th item in the sorted
 * array (to the left of the i-th item).
 *
 * Return any solution if there is more than one solution and return an empty list
 * if there is no solution.
 *
 *
 * Example 1:
 *
 * Input: n = 8, m = 2, group = [-1,-1,1,0,0,1,0,-1],
 *        beforeItems = [[],[6],[5],[6],[3,6],[],[],[]]
 * Output: [6,3,4,1,5,2,0,7]
 *
 * Example 2:
 *
 * Input: n = 8, m = 2, group = [-1,-1,1,0,0,1,0,-1],
 *        beforeItems = [[],[6],[5],[6],[3],[],[4],[]]
 * Output: []
 * Explanation: This is the same as example 1 except that 4 needs to be before 6
 * in the sorted list.
 *
 *
 * Constraints:
 *
 * 1 <= m <= n <= 3 * 10^4
 * group.length == beforeItems.length == n
 * -1 <= group[i] <= m - 1
 * 0 <= beforeItems[i].length <= n - 1
 * 0 <= beforeItems[i][j] <= n - 1
 * i != beforeItems[i][j]
 * beforeItems[i] does not contain duplicates elements.
 *
 */
public class SortItemsByGroupsRespectingDependencies {

    // V0
    // IDEA: TWO-LEVEL TOPOLOGICAL SORT (KAHN BFS)
    //  1) give every ungrouped item (group == -1) a brand new group of its own
    //  2) split beforeItems into two graphs:
    //       item graph  : j -> i when i and j share a group   (order INSIDE a block)
    //       group graph : g[j] -> g[i] when they do not       (order OF the blocks)
    //  3) topo sort the groups, then topo sort the items of each group in that order
    //  a cycle in either graph means there is no valid ordering -> return []
    /**
     * time = O(N + M + E)
     * space = O(N + M + E)
     */
    public int[] sortItems(int n, int m, int[] group, List<List<Integer>> beforeItems) {
        // step 1: every item gets a real group id (copy so the input is not mutated)
        int[] groupOf = new int[n];
        int groupCount = m;
        for (int i = 0; i < n; i++) {
            if (group[i] == -1) {
                groupOf[i] = groupCount;
                groupCount++;
            } else {
                groupOf[i] = group[i];
            }
        }

        // step 2: build both graphs with their in-degrees
        List<List<Integer>> itemGraph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            itemGraph.add(new ArrayList<>());
        }
        int[] itemInDegree = new int[n];

        List<List<Integer>> groupGraph = new ArrayList<>();
        for (int g = 0; g < groupCount; g++) {
            groupGraph.add(new ArrayList<>());
        }
        int[] groupInDegree = new int[groupCount];

        for (int i = 0; i < n; i++) {
            for (int before : beforeItems.get(i)) {
                if (groupOf[before] == groupOf[i]) {
                    itemGraph.get(before).add(i);
                    itemInDegree[i]++;
                } else {
                    groupGraph.get(groupOf[before]).add(groupOf[i]);
                    groupInDegree[groupOf[i]]++;
                }
            }
        }

        // step 3a: order the groups
        List<Integer> allGroups = new ArrayList<>();
        for (int g = 0; g < groupCount; g++) {
            allGroups.add(g);
        }
        List<Integer> groupOrder = topologicalSort(allGroups, groupGraph, groupInDegree);
        if (groupOrder.isEmpty()) {
            return new int[0];
        }

        // items of each group, so each block can be sorted on its own
        List<List<Integer>> itemsInGroup = new ArrayList<>();
        for (int g = 0; g < groupCount; g++) {
            itemsInGroup.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            itemsInGroup.get(groupOf[i]).add(i);
        }

        // step 3b: order the items inside each group, block by block
        int[] result = new int[n];
        int index = 0;
        for (int g : groupOrder) {
            List<Integer> items = itemsInGroup.get(g);
            if (items.isEmpty()) {
                continue;
            }
            List<Integer> itemOrder = topologicalSort(items, itemGraph, itemInDegree);
            if (itemOrder.isEmpty()) {
                return new int[0];
            }
            for (int item : itemOrder) {
                result[index] = item;
                index++;
            }
        }
        return result;
    }

    /** Kahn's BFS over the given nodes; returns an empty list if a cycle blocks a full ordering. */
    private List<Integer> topologicalSort(List<Integer> nodes, List<List<Integer>> graph, int[] inDegree) {
        Deque<Integer> queue = new ArrayDeque<>();
        for (int node : nodes) {
            if (inDegree[node] == 0) {
                queue.offer(node);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);
            for (int next : graph.get(current)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        if (order.size() != nodes.size()) {
            return new ArrayList<>();
        }
        return order;
    }
}
