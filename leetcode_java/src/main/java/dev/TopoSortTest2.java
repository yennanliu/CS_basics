package dev;

import java.util.*;

/** Crack coding interview problem 4.7, p.145 */

public class TopoSortTest2 {

    public static void main(String[] args) {
        List<String> projects = Arrays.asList("a", "b", "c", "d", "e", "f");
        List<List<String>> dependencies = Arrays.asList(
//                Arrays.asList("f", "e"),
//                Arrays.asList("f", "b"),
//                Arrays.asList("a", "d"),
//                Arrays.asList("d", "c"),
//                Arrays.asList("b", "a")

                Arrays.asList("a", "d"),
                Arrays.asList("f", "b"),
                Arrays.asList("b", "d"),
                Arrays.asList("f", "a"),
                Arrays.asList("d", "c")
        );

        try {
            List<String> order = findBuildOrder(projects, dependencies);
            System.out.println("Build Order: " + order);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /** TOPOLOGICAL SORT */
    public static List<String> findBuildOrder(List<String> projects, List<List<String>> dependencies) {
        // Graph setup
        Map<String, List<String>> adjList = new HashMap<>();
        Map<String, Integer> inDegree = new HashMap<>();

        // Initialize graph
        for (String project : projects) {
            adjList.put(project, new ArrayList<>());
            inDegree.put(project, 0);
        }

        // Build graph and in-degree map
        for (List<String> dep : dependencies) {
            String before = dep.get(0);
            String after = dep.get(1);
            adjList.get(before).add(after);
            inDegree.put(after, inDegree.get(after) + 1);
        }

        // Find all sources (in-degree = 0)
        Queue<String> queue = new LinkedList<>();
        for (String project : inDegree.keySet()) {
            if (inDegree.get(project) == 0) {
                queue.offer(project);
            }
        }

        // Perform topological sort
        List<String> buildOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            buildOrder.add(current);

            for (String neighbor : adjList.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Cycle detection: if not all projects are built
        if (buildOrder.size() != projects.size()) {
            throw new RuntimeException("No valid build order (cycle detected)");
        }

        return buildOrder;
    }

}
