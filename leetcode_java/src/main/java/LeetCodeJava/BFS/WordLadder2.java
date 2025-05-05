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
    // IDEA: BFS, LC 127 (fixed by gpt) (TLE)
    public List<List<String>> findLadders_0_1(String beginWord, String endWord, List<String> wordList) {
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

    // V1
    // https://www.youtube.com/watch?v=rWd4wScVYxc


    // V2
    // https://leetcode.com/problems/word-ladder-ii/solutions/40477/super-fast-java-solution-two-end-bfs-by-6p645/


    // V3
    // https://leetcode.com/problems/word-ladder-ii/solutions/2424910/explanation-with-animation-accepted-with-zlxi/

}
