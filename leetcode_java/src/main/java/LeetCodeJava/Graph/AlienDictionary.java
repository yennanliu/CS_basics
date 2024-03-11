package LeetCodeJava.Graph;

// https://leetcode.com/problems/alien-dictionary/description/
// https://leetcode.ca/all/269.html

import java.util.*;

public class AlienDictionary {

    // V0
    // TODO : implement it
//    public String alienOrder(String[] words){
//        return null;
//    }

    // V1
    // IDEA : topological sorting
    // https://leetcode.ca/all/269.html
    // dfs
    public String alienOrder_1(String[] words) {

        // Step 1: build the graph
        Map<Character, Set<Character>> graph = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            String currentWord = words[i];
            for (int j = 0; j < currentWord.length(); j++) {
                if (!graph.containsKey(currentWord.charAt(j))) {
                    graph.put(currentWord.charAt(j), new HashSet<>());
                }
            }

            if (i > 0) {
                connectGraph(graph, words[i - 1], currentWord);
            }
        }

        // Step 2: topological sorting
        StringBuffer sb = new StringBuffer();
        Map<Character, Integer> visited = new HashMap<Character, Integer>(); // mark as visited: visited.put(vertexId, -1);

        for (Map.Entry<Character, Set<Character>> entry: graph.entrySet()) {
            char vertexId = entry.getKey();
            if (!topologicalSort(vertexId, graph, sb, visited)) {
                return "";
            }
        }

        return sb.toString();
    }

    private void connectGraph(Map<Character, Set<Character>> graph, String prev, String curr) {
        if (prev == null || curr == null) {
            return;
        }

        int len = Math.min(prev.length(), curr.length());

        for (int i = 0; i < len; i++) {
            char p = prev.charAt(i);
            char q = curr.charAt(i);
            if (p != q) { // so if same duplicated work, will not reach here and not update graph
                if (!graph.get(p).contains(q)) {
                    graph.get(p).add(q);
                }
                break;
            }
        }
    }

    private boolean topologicalSort(
            char vertexId,
            Map<Character, Set<Character>> graph,
            StringBuffer sb,
            Map<Character, Integer> visited
    ) {

        if (visited.containsKey(vertexId)) {
            // visited
            if (visited.get(vertexId) == -1) { // -1 meaning visited, cycle found
                return false;
            }

            // already in the list
            if (visited.get(vertexId) == 1) {
                return true;
            }
        }

        visited.put(vertexId, -1); // mark as visited


        Set<Character> neighbors = graph.get(vertexId);
        for (char neighbor : neighbors) {
            if (!topologicalSort(neighbor, graph, sb, visited)) {
                return false;
            }
        }

        sb.insert(0, vertexId);
        visited.put(vertexId, 1); // restore visited

        return true;
     }

     // V2
     // https://github.com/Cee/Leetcode/blob/master/269%20-%20Alien%20Dictionary.java
     public String alienOrder_2(String[] words) {
         Map<Character, Set<Character>> map = new HashMap<>();
         Map<Character, Integer> degree = new HashMap<>();
         String result = "";
         if (words == null || words.length == 0) { return result; }
         // Degree char = 0
         for (String s: words) {
             for (char c: s.toCharArray()) {
                 degree.put(c, 0);
             }
         }

         for (int i = 0; i < words.length - 1; i++) {
             String curr = words[i];
             String next = words[i + 1];
             int min = Math.min(curr.length(), next.length());
             for (int j = 0; j < min; j++) {
                 char c1 = curr.charAt(j);
                 char c2 = next.charAt(j);
                 if (c1 != c2) {
                     Set<Character> set = map.getOrDefault(c1, new HashSet<>());
                     if (!set.contains(c2)) {
                         set.add(c2);
                         map.put(c1, set);
                         degree.put(c2, degree.get(c2) + 1); // update c2, c1 < c2
                     }
                     break;
                 }
             }
         }

         LinkedList<Character> q = new LinkedList<>();
         for (char c: degree.keySet()) {
             if (degree.get(c) == 0) {
                 q.add(c);
             }
         }

         while (!q.isEmpty()) {
             char c = q.poll();
             result += c;
             if (map.containsKey(c)) {
                 for (char next: map.get(c)) {
                     degree.put(next, degree.get(next) - 1);
                     if (degree.get(next) == 0) {
                         q.offer(next);
                     }
                 }
             }
         }

         return result.length() == degree.size() ? result : "";
     }

}
