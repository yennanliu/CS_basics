package LeetCodeJava.BFS;

// https://leetcode.com/problems/word-ladder-ii/description/

import java.util.*;

/**
 * 126. Word Ladder II
 * Solved
 * Hard
 * Topics
 * Companies
 * A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words beginWord -> s1 -> s2 -> ... -> sk such that:
 *
 * Every adjacent pair of words differs by a single letter.
 * Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
 * sk == endWord
 * Given two words, beginWord and endWord, and a dictionary wordList, return all the shortest transformation sequences from beginWord to endWord, or an empty list if no such sequence exists. Each sequence should be returned as a list of the words [beginWord, s1, s2, ..., sk].
 *
 *
 *
 * Example 1:
 *
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: [["hit","hot","dot","dog","cog"],["hit","hot","lot","log","cog"]]
 * Explanation: There are 2 shortest transformation sequences:
 * "hit" -> "hot" -> "dot" -> "dog" -> "cog"
 * "hit" -> "hot" -> "lot" -> "log" -> "cog"
 * Example 2:
 *
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
 * Output: []
 * Explanation: The endWord "cog" is not in wordList, therefore there is no valid transformation sequence.
 *
 *
 * Constraints:
 *
 * 1 <= beginWord.length <= 5
 * endWord.length == beginWord.length
 * 1 <= wordList.length <= 500
 * wordList[i].length == beginWord.length
 * beginWord, endWord, and wordList[i] consist of lowercase English letters.
 * beginWord != endWord
 * All the words in wordList are unique.
 * The sum of all shortest transformation sequences does not exceed 105.
 *
 *
 */
public class WordLadder2 {

    // V0
//    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
//
//    }

    // V0-1
    // IDEA: BFS + DFS (fixed by gpt)
    public List<List<String>> findLadders_0_1(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord))
            return res;

        Map<String, List<String>> parents = new HashMap<>();
        Queue<String> q = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        q.add(beginWord);
        visited.add(beginWord);

        boolean found = false;

        // NOTE !!! BFS below
        while (!q.isEmpty() && !found) {
            int size = q.size();
            Set<String> levelVisited = new HashSet<>();

            for (int i = 0; i < size; i++) {
                String word = q.poll();
                char[] chs = word.toCharArray();

                for (int j = 0; j < chs.length; j++) {
                    char old = chs[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == old)
                            continue;
                        chs[j] = c;
                        String next = new String(chs);

                        if (!wordSet.contains(next))
                            continue;

                        if (!visited.contains(next)) {
                            if (!parents.containsKey(next)) {
                                parents.put(next, new ArrayList<>());
                            }
                            parents.get(next).add(word);

                            if (!levelVisited.contains(next)) {
                                levelVisited.add(next);
                                q.add(next);
                            }

                            if (next.equals(endWord)) {
                                found = true;
                            }
                        }
                    }
                    chs[j] = old;
                }
            }

            visited.addAll(levelVisited);
        }

        if (found) {
            LinkedList<String> path = new LinkedList<>();
            // NOTE !!! we call DFS below
            dfs(endWord, beginWord, parents, path, res);
        }

        return res;
    }

    private void dfs(String word, String beginWord, Map<String, List<String>> parents,
                     LinkedList<String> path, List<List<String>> res) {
        path.addFirst(word);
        if (word.equals(beginWord)) {
            res.add(new ArrayList<>(path));
        } else {
            List<String> preds = parents.get(word);
            if (preds != null) {
                for (String prev : preds) {
                    dfs(prev, beginWord, parents, path, res);
                }
            }
        }
        path.removeFirst();
    }

    // V0-2
    // IDEA: BFS, LC 127 (fixed by gpt) (TLE)
    public List<List<String>> findLadders_0_2(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord))
            return res;

        Queue<WordState_> q = new LinkedList<>();
        //q.add(new WordState_(beginWord, new ArrayList<>(List.of(beginWord))));
        q.add(new WordState_(beginWord, new ArrayList<>(Arrays.asList(beginWord)))); // fixed for java 8

        Set<String> visited = new HashSet<>();
        Set<String> currentLevelVisited = new HashSet<>();
        boolean foundShortest = false;

        while (!q.isEmpty() && !foundShortest) {
            int size = q.size();
            currentLevelVisited.clear();

            for (int i = 0; i < size; i++) {
                WordState_ ws = q.poll();
                String word = ws.word;
                List<String> history = ws.history;

                if (word.equals(endWord)) {
                    res.add(new ArrayList<>(history)); // ✅ deep copy for safety
                    foundShortest = true;
                    continue;
                }

                for (int j = 0; j < word.length(); j++) {
                    char[] wordChars = word.toCharArray();

                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == wordChars[j])
                            continue;

                        wordChars[j] = c;
                        String nextWord = new String(wordChars);

                        if (wordSet.contains(nextWord) && !visited.contains(nextWord)) {
                            List<String> newHistory = new ArrayList<>(history);
                            newHistory.add(nextWord);
                            q.add(new WordState_(nextWord, newHistory));
                            currentLevelVisited.add(nextWord);
                        }
                    }
                }
            }

            visited.addAll(currentLevelVisited);
        }

        return res;
    }

    private static class WordState_ {
        String word;
        List<String> history;

        WordState_(String word, List<String> history) {
            this.word = word;
            this.history = history;
        }
    }

    // V0-3
    // IDEA: BFS + DFS + BACKTRACK (gemini)
    public List<List<String>> findLadders_0_3(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> result = new ArrayList<>();
        Set<String> dict = new HashSet<>(wordList);
        if (!dict.contains(endWord))
            return result;

        // Map to store: word -> shortest distance from beginWord
        Map<String, Integer> distances = new HashMap<>();
        // Map to store: word -> list of its "predecessors" in the shortest path
        Map<String, List<String>> adj = new HashMap<>();

        dict.add(beginWord);

        // 1. BFS to find distances and build the adjacency structure
        bfs(beginWord, endWord, dict, distances, adj);

        // 2. DFS (Backtracking) to reconstruct paths from endWord back to beginWord
        if (distances.containsKey(endWord)) {
            List<String> path = new LinkedList<>();
            path.add(endWord);
            backtrack(endWord, beginWord, adj, path, result);
        }

        return result;
    }

    private void bfs(String begin, String end, Set<String> dict,
                     Map<String, Integer> distances, Map<String, List<String>> adj) {
        Queue<String> q = new LinkedList<>();
        q.offer(begin);
        distances.put(begin, 0);

        boolean found = false;
        while (!q.isEmpty() && !found) {
            int size = q.size();
            // Process level by level to ensure we find all shortest paths
            for (int i = 0; i < size; i++) {
                String cur = q.poll();
                int curDist = distances.get(cur);

                List<String> neighbors = getNeighbors(cur, dict);
                for (String next : neighbors) {
                    // Case 1: We found this word for the first time
                    if (!distances.containsKey(next)) {
                        distances.put(next, curDist + 1);
                        q.offer(next);
                        adj.computeIfAbsent(next, k -> new ArrayList<>()).add(cur);
                    }
                    // Case 2: We found this word at the same shortest level from another path
                    else if (distances.get(next) == curDist + 1) {
                        adj.computeIfAbsent(next, k -> new ArrayList<>()).add(cur);
                    }

                    if (next.equals(end))
                        found = true;
                }
            }
        }
    }

    private List<String> getNeighbors(String word, Set<String> dict) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char old = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == old)
                    continue;
                chars[i] = c;
                String next = new String(chars);
                if (dict.contains(next))
                    neighbors.add(next);
            }
            chars[i] = old;
        }
        return neighbors;
    }

    private void backtrack(String cur, String begin, Map<String, List<String>> adj,
                           List<String> path, List<List<String>> result) {
        if (cur.equals(begin)) {
            List<String> copy = new ArrayList<>(path);
            Collections.reverse(copy);
            result.add(copy);
            return;
        }

        if (adj.containsKey(cur)) {
            for (String pre : adj.get(cur)) {
                path.add(pre);
                backtrack(pre, begin, adj, path, result);
                path.remove(path.size() - 1); // Backtrack step
            }
        }
    }


    // V1
    // https://www.youtube.com/watch?v=rWd4wScVYxc


    // V2
    // https://leetcode.com/problems/word-ladder-ii/solutions/40477/super-fast-java-solution-two-end-bfs-by-6p645/


    // V3
    // https://leetcode.com/problems/word-ladder-ii/solutions/2424910/explanation-with-animation-accepted-with-zlxi/



}
