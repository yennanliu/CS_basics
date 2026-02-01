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
    // IDEA: TOPOLOGICAL SORT (neetcode, comments created by gpt)
    // TOPOLOGICAL SORT : `degrees`, map, BFS
    /**
     * time = O(C)
     * space = O(1)
     */
    public String foreignDictionary(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        // NOTE !!! we use `map` as degrees storage
        Map<Character, Integer> indegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        }

        /**
         *   NOTE !!! below
         *
         *   -> build the character `ordering`
         *
         *  Loop Over Adjacent Word Pairs
         *
         *
         *
         * for (int i = 0; i < words.length - 1; i++) {
         *     String w1 = words[i];
         *     String w2 = words[i + 1];
         *
         * We are comparing each pair of consecutive
         * words in the list (words[i] and words[i+1]).
         *
         * This is important because the alien language is
         * sorted — and order relationships only exist between adjacent words.
         *
         */
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            /**
             *  NOTE !!! below
             *
             *
             * int minLen = Math.min(w1.length(), w2.length());
             * if (w1.length() > w2.length() &&
             *     w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
             *     return "";
             * }
             *
             *
             * ->  This checks for a prefix violation:
             * If w1 is longer than w2, and w2 is a prefix of w1, that’s `invalid`.
             *
             * Example:
             *
             *   words = ["apple", "app"]
             *
             *
             * Here, app comes after apple,
             * which is wrong because in a lexicographically sorted language,
             * a shorter prefix should come before the longer word.
             *
             * -> Hence, we return "" to signal an invalid dictionary order.
             *
             */
            int minLen = Math.min(w1.length(), w2.length());
            // handle `ordering` edge case
            // e.g. words = ["apple", "app"]
            if (w1.length() > w2.length() &&
                    w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
                return "";
            }

            /**
             *  NOTE !!! below
             *
             *
             *  This loop compares characters at each position j in w1 and w2.
             *  The first place where they differ defines the ordering.
             *
             *
             *  Example :
             *
             *    w1 = "wrt"
             *    w2 = "wrf"
             *
             *
             *
             *  At index 2, 't' and 'f' differ → so we know:
             * 't' < 'f' → Add a directed edge: t → f
             *
             * adj.get(w1.charAt(j)).add(w2.charAt(j)): Adds this edge in the adjacency list.
             *
             * indegree.put(...): Increments in-degree of the target node.
             *
             *
             * NOTE !!!
             *
             * -> Then we break — we don’t look at further characters
             *     -> because they don’t affect the order.
             *
             *
             */
            // compare the `first different character within w1, w2`
            // The first place where they differ defines the ordering.
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

    // V0-1
    // IDEA: TOPOLOGICAL SORT (fixed by gpt)
    // TODO: validate below
    /**
     * time = O(C)
     * space = O(1)
     */
    public String alienOrder_0_1(String[] words) {
        if (words == null || words.length == 0) return "";

        // Step 1: Initialize graph and in-degree map
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();

        // Initialize all unique characters
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }

        // Step 2: Build graph by comparing adjacent words
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            // Edge case: prefix problem
            if (w1.length() > w2.length() && w1.startsWith(w2)) {
                return "";
            }

            for (int j = 0; j < Math.min(w1.length(), w2.length()); j++) {
                char c1 = w1.charAt(j);
                char c2 = w2.charAt(j);
                if (c1 != c2) {
                    if (!graph.get(c1).contains(c2)) {
                        graph.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    break; // Only the first differing character defines the ordering
                }
            }
        }

        // Step 3: Topological Sort (BFS)
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }

        StringBuilder result = new StringBuilder();

        while (!queue.isEmpty()) {
            char c = queue.poll();
            result.append(c);
            for (char neighbor : graph.get(c)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // Check if there was a cycle
        if (result.length() != graph.size()) return "";

        return result.toString();
    }


    // V1-1
    // https://neetcode.io/problems/foreign-dictionary
    // IDEA: DFS
    private Map<Character, Set<Character>> adj;
    private Map<Character, Boolean> visited;
    private List<Character> result;

    /**
     * time = O(C)
     * space = O(1)
     */
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

    /**
     * time = O(C)
     * space = O(1)
     */
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
    /**
     * time = O(C)
     * space = O(1)
     */
    public String foreignDictionary_1_2(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        Map<Character, Integer> indegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        }

        // build the character `ordering`
        /**
         *  Loop Over Adjacent Word Pairs

         * for (int i = 0; i < words.length - 1; i++) {
         *     String w1 = words[i];
         *     String w2 = words[i + 1];
         *
         * We are comparing each pair of consecutive
         * words in the list (words[i] and words[i+1]).
         *
         * This is important because the alien language is
         * sorted — and order relationships only exist between adjacent words.
         *
         */
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            /**
             *  NOTE !!! below
             *
             *
             * int minLen = Math.min(w1.length(), w2.length());
             * if (w1.length() > w2.length() &&
             *     w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
             *     return "";
             * }
             *
             *
             * ->  This checks for a prefix violation:
             * If w1 is longer than w2, and w2 is a prefix of w1, that’s `invalid`.
             *
             * Example:
             *
             *   words = ["apple", "app"]
             *
             *
             * Here, app comes after apple,
             * which is wrong because in a lexicographically sorted language,
             * a shorter prefix should come before the longer word.
             *
             * -> Hence, we return "" to signal an invalid dictionary order.
             *
             */
            int minLen = Math.min(w1.length(), w2.length());
            if (w1.length() > w2.length() &&
                    w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
                return "";
            }

            /**
             *  NOTE !!! below
             *
             *
             *  This loop compares characters at each position j in w1 and w2.
             *  The first place where they differ defines the ordering.
             *
             *
             *  Example :
             *
             *    w1 = "wrt"
             *    w2 = "wrf"
             *
             *
             *
             *  At index 2, 't' and 'f' differ → so we know:
             * 't' < 'f' → Add a directed edge: t → f
             *
             * adj.get(w1.charAt(j)).add(w2.charAt(j)): Adds this edge in the adjacency list.
             *
             * indegree.put(...): Increments in-degree of the target node.
             *
             * -> Then we break — we don’t look at further characters
             * because they don’t affect the order.
             *
             *
             */
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
    /**
     * time = O(C)
     * space = O(1)
     */
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

    /**
     * time = O(min(len1, len2))
     * space = O(1)
     */
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

    /**
     * time = O(V + E)
     * space = O(V)
     */
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
     /**
      * time = O(C)
      * space = O(1)
      */
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

     // V4-1
     // https://leetcode.ca/2016-08-25-269-Alien-Dictionary/
//     public String alienOrder_4_1(String[] words) {
//
//     }
     // ref: http://buttercola.blogspot.com/2015/09/leetcode-alien-dictionary.html
     public class alienOrder_4_1 {
         /**
          * @param words: a list of words
          * @return: a string which is correct order
          */
         /**
          * time = O(C)
          * space = O(1)
          */
         public String alienOrder(String[] words) {
             // Write your code here
             if (words == null || words.length == 0) {
                 return "";
             }

             // step 1: construct the graph
             //
             Map<Character, List<Character>> adjMap = new HashMap<>();
             constructGraph(words, adjMap);

             int numNodes = adjMap.size();

             StringBuilder result = new StringBuilder();

             // toplogical sorting
             //
             Map<Character, Integer> indegreeMap = new HashMap<>();
             for (Character node : adjMap.keySet()) {
                 indegreeMap.put(node, 0);
             }

             for (Character node : adjMap.keySet()) {
                 for (Character neighbor : adjMap.get(node)) {
                     int indegree = indegreeMap.get(neighbor);
                     indegree += 1;
                     indegreeMap.put(neighbor, indegree);
                 }
             }

             // start from indegree=0
             Queue<Character> queue = new PriorityQueue<>();
             for (Character node : indegreeMap.keySet()) {
                 if (indegreeMap.get(node) == 0) {
                     // starting node, can only be one, cannot be 2 starting with 0 indegree
                     queue.offer(node);
                 }
             }

             while (!queue.isEmpty()) {
                 char curNode = queue.poll();
                 result.append(curNode);

                 for (char neighbor : adjMap.get(curNode)) {
                     int indegree = indegreeMap.get(neighbor);
                     indegree -= 1;
                     indegreeMap.put(neighbor, indegree);

                     // @note: key is here.
                     // for A->B, B->C, A-C: C will not be counted until its indgree is 0

                     if (indegree == 0) {
                         queue.offer(neighbor);
                     }
                 }
             }

             if (result.length() < numNodes) {
                 return "";
             }

             return result.toString();
         }

         /**
          * time = O(C)
          * space = O(1)
          */
         private void constructGraph(String[] words, Map<Character, List<Character>> adjMap) {
             // construct nodes
             for (String word : words) {
                 for (Character c : word.toCharArray()) {
                     adjMap.put(c, new ArrayList<>()); // c to all its next
                 }
             }

             // construct edges
             for (int i = 1; i < words.length; i++) {
                 String prev = words[i - 1];
                 String curr = words[i];

                 for (int j = 0; j < prev.length() && j < curr.length(); j++) {
                     if (prev.charAt(j) != curr.charAt(j)) {
                         adjMap.get(prev.charAt(j)).add(curr.charAt(j));
                         break;
                     }
                 }
             }
         }
     }

    // V4-2
    // https://leetcode.ca/2016-08-25-269-Alien-Dictionary/
    // IDEA: DFS
    // dfs
    /**
     * time = O(C)
     * space = O(1)
     */
    public String alienOrder_4_2(String[] words) {

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
                connectGraph_4_2(graph, words[i - 1], currentWord);
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

    /**
     * time = O(min(len1, len2))
     * space = O(1)
     */
    private void connectGraph_4_2(Map<Character, Set<Character>> graph, String prev, String curr) {
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

    /**
     * time = O(V + E)
     * space = O(V)
     */
    private boolean topologicalSort_4_2(
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
            if (!topologicalSort_4_2(neighbor, graph, sb, visited)) {
                return false;
            }
        }

        sb.insert(0, vertexId);
        visited.put(vertexId, 1); // restore visited

        return true;
    }

    // V4-3
    /**
     * time = O(C)
     * space = O(1)
     */
    public String connectGraph_4_3(String[] words) {
        boolean[][] g = new boolean[26][26];
        boolean[] s = new boolean[26];
        int cnt = 0;
        int n = words.length;
        for (int i = 0; i < n - 1; ++i) {
            for (char c : words[i].toCharArray()) {
                if (cnt == 26) {
                    break;
                }
                c -= 'a';
                if (!s[c]) {
                    ++cnt;
                    s[c] = true;
                }
            }
            int m = words[i].length();
            for (int j = 0; j < m; ++j) {
                if (j >= words[i + 1].length()) {
                    return "";
                }
                char c1 = words[i].charAt(j), c2 = words[i + 1].charAt(j);
                if (c1 == c2) {
                    continue;
                }
                if (g[c2 - 'a'][c1 - 'a']) {
                    return "";
                }
                g[c1 - 'a'][c2 - 'a'] = true;
                break;
            }
        }
        for (char c : words[n - 1].toCharArray()) {
            if (cnt == 26) {
                break;
            }
            c -= 'a';
            if (!s[c]) {
                ++cnt;
                s[c] = true;
            }
        }

        int[] indegree = new int[26];
        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < 26; ++j) {
                if (i != j && s[i] && s[j] && g[i][j]) {
                    ++indegree[j];
                }
            }
        }
        Deque<Integer> q = new LinkedList<>();
        for (int i = 0; i < 26; ++i) {
            if (s[i] && indegree[i] == 0) {
                q.offerLast(i);
            }
        }
        StringBuilder ans = new StringBuilder();
        while (!q.isEmpty()) {
            int t = q.pollFirst();
            ans.append((char) (t + 'a'));
            for (int i = 0; i < 26; ++i) {
                if (i != t && s[i] && g[t][i]) {
                    if (--indegree[i] == 0) {
                        q.offerLast(i);
                    }
                }
            }
        }
        return ans.length() < cnt ? "" : ans.toString();
    }

}
