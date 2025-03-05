package LeetCodeJava.Graph;

// https://leetcode.com/problems/alien-dictionary/description/
// https://leetcode.ca/all/269.html

import java.util.*;

/**
 * 269. Alien Dictionary
 *
 * There is a new alien language which uses the latin alphabet.
 * However, the order among letters are unknown to you.
 * You receive a list of non-empty words from the dictionary,
 * where words are sorted lexicographically by the rules of this new language.
 * Derive the order of letters in this language.
 *
 * Example 1:
 *
 * Input:
 * [
 *   "wrt",
 *   "wrf",
 *   "er",
 *   "ett",
 *   "rftt"
 * ]
 *
 * Output: "wertf"
 * Example 2:
 *
 * Input:
 * [
 *   "z",
 *   "x"
 * ]
 *
 * Output: "zx"
 * Example 3:
 *
 * Input:
 * [
 *   "z",
 *   "x",
 *   "z"
 * ]
 *
 * Output: ""
 *
 * Explanation: The order is invalid, so return "".
 * Note:
 *
 * You may assume all letters are in lowercase.
 * You may assume that if a is a prefix of b, then a must appear before b in the given dictionary.
 * If the order is invalid, return an empty string.
 * There may be multiple valid order of letters, return any one of them is fine.
 *
 */
public class AlienDictionary {

    // V0
    // TODO : implement it
//    public String alienOrder(String[] words){
//        return null;
//    }

    // V1-1
    // https://neetcode.io/problems/foreign-dictionary
    // IDEA: DFS
    private Map<Character, Set<Character>> adj;
    private Map<Character, Boolean> visited;
    private List<Character> result;

    public String foreignDictionary_1_1(String[] words) {
        adj = new HashMap<>();
        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
            }
        }

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            if (w1.length() > w2.length() &&
                    w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
                return "";
            }
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    adj.get(w1.charAt(j)).add(w2.charAt(j));
                    break;
                }
            }
        }

        visited = new HashMap<>();
        result = new ArrayList<>();
        for (char c : adj.keySet()) {
            if (dfs(c)) {
                return "";
            }
        }

        Collections.reverse(result);
        StringBuilder sb = new StringBuilder();
        for (char c : result) {
            sb.append(c);
        }
        return sb.toString();
    }

    private boolean dfs(char ch) {
        if (visited.containsKey(ch)) {
            return visited.get(ch);
        }

        visited.put(ch, true);
        for (char next : adj.get(ch)) {
            if (dfs(next)) {
                return true;
            }
        }
        visited.put(ch, false);
        result.add(ch);
        return false;
    }


    // V1-2
    // https://neetcode.io/problems/foreign-dictionary
    // IDEA:  Topological Sort (Kahn's Algorithm)
    public String foreignDictionary_1_2(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        }

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];
            int minLen = Math.min(w1.length(), w2.length());
            if (w1.length() > w2.length() &&
                    w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
                return "";
            }
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    if (!adj.get(w1.charAt(j)).contains(w2.charAt(j))) {
                        adj.get(w1.charAt(j)).add(w2.charAt(j));
                        indegree.put(w2.charAt(j),
                                indegree.get(w2.charAt(j)) + 1);
                    }
                    break;
                }
            }
        }

        Queue<Character> q = new LinkedList<>();
        for (char c : indegree.keySet()) {
            if (indegree.get(c) == 0) {
                q.offer(c);
            }
        }

        StringBuilder res = new StringBuilder();
        while (!q.isEmpty()) {
            char char_ = q.poll();
            res.append(char_);
            for (char neighbor : adj.get(char_)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    q.offer(neighbor);
                }
            }
        }

        if (res.length() != indegree.size()) {
            return "";
        }

        return res.toString();
    }


    // V2
    // IDEA : topological sorting
    // https://leetcode.ca/all/269.html
    // dfs
    public String alienOrder_2(String[] words) {

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

     // V3
     // https://github.com/Cee/Leetcode/blob/master/269%20-%20Alien%20Dictionary.java
     public String alienOrder_3(String[] words) {
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
