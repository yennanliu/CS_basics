package LeetCodeJava.BFS;

import java.util.*;

// https://leetcode.com/problems/word-ladder/
/**
 * 127. Word Ladder
 * Solved
 * Hard
 * Topics
 * Companies
 * A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words beginWord -> s1 -> s2 -> ... -> sk such that:
 *
 * Every adjacent pair of words differs by a single letter.
 * Every si for 1 <= i <= k is in wordList. Note that beginWord does not need to be in wordList.
 * sk == endWord
 * Given two words, beginWord and endWord, and a dictionary wordList, return the number of words in the shortest transformation sequence from beginWord to endWord, or 0 if no such sequence exists.
 *
 *
 *
 * Example 1:
 *
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log","cog"]
 * Output: 5
 * Explanation: One shortest transformation sequence is "hit" -> "hot" -> "dot" -> "dog" -> cog", which is 5 words long.
 * Example 2:
 *
 * Input: beginWord = "hit", endWord = "cog", wordList = ["hot","dot","dog","lot","log"]
 * Output: 0
 * Explanation: The endWord "cog" is not in wordList, therefore there is no valid transformation sequence.
 *
 *
 * Constraints:
 *
 * 1 <= beginWord.length <= 10
 * endWord.length == beginWord.length
 * 1 <= wordList.length <= 5000
 * wordList[i].length == beginWord.length
 * beginWord, endWord, and wordList[i] consist of lowercase English letters.
 * beginWord != endWord
 * All the words in wordList are unique.
 *
 *
 */

public class WordLadder {

    // V0
    // IDEA: BFS (fixed by gpt) (TLE)
    // TODO: optimize
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {

        // edge case: if the endWord is not in the wordList, return 0
        if (!wordList.contains(endWord)) {
            return 0;
        }

        // edge case: if the wordList is null or empty, return 0
        if (wordList == null || wordList.isEmpty()) {
            return 0;
        }

        // if beginWord is the same as endWord, return 1 since no transformation is
        // needed
        if (beginWord.equals(endWord)) {
            return 1;
        }

        // Initialize the BFS queue and visited set
        Queue<String> q = new LinkedList<>();
        q.add(beginWord);

        HashSet<String> visited = new HashSet<>();
        visited.add(beginWord);

        int pathLen = 1; // Starting from the beginWord, the first word is considered part of the path

        // Begin BFS
        while (!q.isEmpty()) {
            int levelSize = q.size(); // Number of words at the current level

            // Process all words at the current level
            for (int i = 0; i < levelSize; i++) {
                String currentWord = q.poll();

                // Try changing each character of the currentWord to any letter a-z
                for (int j = 0; j < currentWord.length(); j++) {
                    for (char c = 'a'; c <= 'z'; c++) {
                        // Skip if the character is the same as the one at position j
                        if (currentWord.charAt(j) == c)
                            continue;

                        // Generate the next possible word
                        String nextWord = currentWord.substring(0, j) + c + currentWord.substring(j + 1);

                        // If the next word is the endWord, return the path length
                        if (nextWord.equals(endWord)) {
                            return pathLen + 1;
                        }

                        // If the next word is valid, add it to the queue
                        if (wordList.contains(nextWord) && !visited.contains(nextWord)) {
                            q.add(nextWord);
                            visited.add(nextWord);
                        }
                    }
                }
            }

            // Increment path length after processing the current level
            pathLen++;
        }

        // If no transformation sequence exists, return 0
        return 0;
    }

    // V0-1
    // IDEA : BFS + pair (gpt)
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/word-ladder.py#L42
    public int ladderLength_0_1(String beginWord, String endWord, List<String> wordList) {
        // Convert wordList to a set for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordList);
        // Queue for BFS
        Queue<Pair> bfs = new LinkedList<>();
        // Add the beginWord with the initial length of 1
        bfs.offer(new Pair(beginWord, 1));

        while (!bfs.isEmpty()) {
            Pair current = bfs.poll();
            String word = current.word;
            int length = current.length;

            // If we reach the endWord, return the length of the sequence
            if (word.equals(endWord)) {
                return length;
            }

            // Try changing each character of the current word to find new words
            for (int i = 0; i < word.length(); i++) {
                char[] wordArray = word.toCharArray();
                for (char c = 'a'; c <= 'z'; c++) {
                    wordArray[i] = c;
                    String newWord = new String(wordArray);

                    // If newWord is in the wordSet and it's not the original word
                    if (wordSet.contains(newWord) && !newWord.equals(word)) {
                        wordSet.remove(newWord);
                        bfs.offer(new Pair(newWord, length + 1));
                    }
                }
            }
        }

        // If no sequence is found, return 0
        return 0;
    }

    // Helper class to store the current word and the sequence length
    private static class Pair {
        String word;
        int length;

        Pair(String word, int length) {
            this.word = word;
            this.length = length;
        }
    }

    // V0-2
    // IDEA: BFS + CUSTOM CLASS (fixed by gpt)
    public int ladderLength_0_2(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) {
            return 0;
        }

        Queue<WordState> q = new LinkedList<>();
        q.offer(new WordState(beginWord, 1));
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);

        while (!q.isEmpty()) {
            WordState current = q.poll();
            String word = current.word;
            int steps = current.steps;

            if (word.equals(endWord)) {
                return steps;
            }

            /**
             *  NOTE !!
             *
             *  via char[], we can MODIFY string at idx
             *
             *  step 1) String -> char[]
             *  step 2) modify at idx
             *  step 3) char[] -> String
             */
            char[] wordChars = word.toCharArray();
            for (int i = 0; i < wordChars.length; i++) {
                char originalChar = wordChars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    // AVOID revisit same word at same idx
                    if (c == originalChar){
                        continue;
                    }

                    /**
                     *  NOTE !!!
                     *
                     *  via below, we can update char[] val at idx
                     *
                     */
                    wordChars[i] = c;
                    String nextWord = new String(wordChars);
                    /**
                     *  NOTE !!! below condition
                     */
                    if (wordSet.contains(nextWord) && !visited.contains(nextWord)) {
                        q.offer(new WordState(nextWord, steps + 1));
                        visited.add(nextWord);
                    }
                }
                /**
                 *  NOTE !!! need to restore original character
                 */
                wordChars[i] = originalChar; // restore original character
            }
        }

        return 0;
    }

    private static class WordState {
        String word;
        int steps;

        public WordState(String word, int steps) {
            this.word = word;
            this.steps = steps;
        }
    }

    // V1-1
    // https://neetcode.io/problems/word-ladder
    // IDEA: BFS - I
    public int ladderLength_1_1(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord) || beginWord.equals(endWord)) {
            return 0;
        }

        int n = wordList.size();
        int m = wordList.get(0).length();
        List<List<Integer>> adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }

        Map<String, Integer> mp = new HashMap<>();
        for (int i = 0; i < n; i++) {
            mp.put(wordList.get(i), i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cnt = 0;
                for (int k = 0; k < m; k++) {
                    if (wordList.get(i).charAt(k) != wordList.get(j).charAt(k)) {
                        cnt++;
                    }
                }
                if (cnt == 1) {
                    adj.get(i).add(j);
                    adj.get(j).add(i);
                }
            }
        }

        Queue<Integer> q = new LinkedList<>();
        int res = 1;
        Set<Integer> visit = new HashSet<>();

        for (int i = 0; i < m; i++) {
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == beginWord.charAt(i)) {
                    continue;
                }
                String word = beginWord.substring(0, i) + c + beginWord.substring(i + 1);
                if (mp.containsKey(word) && !visit.contains(mp.get(word))) {
                    q.add(mp.get(word));
                    visit.add(mp.get(word));
                }
            }
        }

        while (!q.isEmpty()) {
            res++;
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int node = q.poll();
                if (wordList.get(node).equals(endWord)) {
                    return res;
                }
                for (int nei : adj.get(node)) {
                    if (!visit.contains(nei)) {
                        visit.add(nei);
                        q.add(nei);
                    }
                }
            }
        }

        return 0;
    }


    // V1-2
    // https://neetcode.io/problems/word-ladder
    // IDEA: BFS - II
    public int ladderLength_1_2(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord) || beginWord.equals(endWord)) return 0;
        Set<String> words = new HashSet<>(wordList);
        int res = 0;
        Queue<String> q = new LinkedList<>();
        q.offer(beginWord);

        while (!q.isEmpty()) {
            res++;
            for (int i = q.size(); i > 0; i--) {
                String node = q.poll();
                if (node.equals(endWord)) return res;
                for (int j = 0; j < node.length(); j++) {
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == node.charAt(j)) continue;
                        String nei = node.substring(0, j) + c + node.substring(j + 1);
                        if (words.contains(nei)) {
                            q.offer(nei);
                            words.remove(nei);
                        }
                    }
                }
            }
        }
        return 0;
    }


    // V1-3
    // https://neetcode.io/problems/word-ladder
    // IDEA: BFS - III
    public int ladderLength_1_3(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord)) {
            return 0;
        }

        Map<String, List<String>> nei = new HashMap<>();
        wordList.add(beginWord);
        for (String word : wordList) {
            for (int j = 0; j < word.length(); j++) {
                String pattern = word.substring(0, j) + "*" + word.substring(j + 1);
                nei.computeIfAbsent(pattern, k -> new ArrayList<>()).add(word);
            }
        }

        Set<String> visit = new HashSet<>();
        Queue<String> q = new LinkedList<>();
        q.offer(beginWord);
        int res = 1;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                String word = q.poll();
                if (word.equals(endWord)) {
                    return res;
                }
                for (int j = 0; j < word.length(); j++) {
                    String pattern = word.substring(0, j) + "*" + word.substring(j + 1);
                    for (String neiWord : nei.getOrDefault(pattern, Collections.emptyList())) {
                        if (!visit.contains(neiWord)) {
                            visit.add(neiWord);
                            q.offer(neiWord);
                        }
                    }
                }
            }
            res++;
        }
        return 0;
    }


    // V1-4
    // https://neetcode.io/problems/word-ladder
    // IDEA: Meet In The Middle (BFS)
    public int ladderLength_1_4(String beginWord, String endWord, List<String> wordList) {
        if (!wordList.contains(endWord) || beginWord.equals(endWord))
            return 0;
        int m = wordList.get(0).length();
        Set<String> wordSet = new HashSet<>(wordList);
        Queue<String> qb = new LinkedList<>(), qe = new LinkedList<>();
        Map<String, Integer> fromBegin = new HashMap<>();
        Map<String, Integer> fromEnd = new HashMap<>();
        qb.add(beginWord);
        qe.add(endWord);
        fromBegin.put(beginWord, 1);
        fromEnd.put(endWord, 1);

        while (!qb.isEmpty() && !qe.isEmpty()) {
            if (qb.size() > qe.size()) {
                Queue<String> tempQ = qb;
                qb = qe;
                qe = tempQ;
                Map<String, Integer> tempMap = fromBegin;
                fromBegin = fromEnd;
                fromEnd = tempMap;
            }
            int size = qb.size();
            for (int k = 0; k < size; k++) {
                String word = qb.poll();
                int steps = fromBegin.get(word);
                for (int i = 0; i < m; i++) {
                    for (char c = 'a'; c <= 'z'; c++) {
                        if (c == word.charAt(i))
                            continue;
                        String nei = word.substring(0, i) +
                                c + word.substring(i + 1);
                        if (!wordSet.contains(nei))
                            continue;
                        if (fromEnd.containsKey(nei))
                            return steps + fromEnd.get(nei);
                        if (!fromBegin.containsKey(nei)) {
                            fromBegin.put(nei, steps + 1);
                            qb.add(nei);
                        }
                    }
                }
            }
        }
        return 0;
    }


    // V2
    // IDEA : BFS + PAIR
    // https://leetcode.com/problems/word-ladder/solutions/4538368/word-ladder-simple-bfs-java/
    class Pair_{
        String str;
        int count;
        Pair_(String str,int count){
            this.str = str;
            this.count = count;
        }
    }

    public int ladderLength_2(String beginWord, String endWord, List<String> wordList) {

        Queue<Pair_> q = new LinkedList<>();
        Set<String> set = new HashSet<>();

        q.offer(new Pair_(beginWord,1));

        for(String s : wordList)
            set.add(s);

        set.remove(beginWord);

        while(!q.isEmpty()){

            String currStr = q.peek().str;
            int currCount = q.peek().count;
            q.poll();
            int len = currStr.length();

            if(currStr.equals(endWord))
                return currCount;

            for( int i=0 ; i<len ; i++){
                for(char ch='a' ; ch<='z' ;ch++){
                    String temp = currStr.substring(0,i) + ch + currStr.substring(i+1);
                    if(set.contains(temp)){
                        set.remove(temp);
                        q.offer(new Pair_(temp , currCount+1));
                    }
                }
            }
        }

        return 0;
    }

    // V3


}
