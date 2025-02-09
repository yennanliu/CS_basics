package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/word-break-ii/

import java.util.*;

/**
 * 140. Word Break II
 * Solved
 * Hard
 * Topics
 * Companies
 * Given a string s and a dictionary of strings wordDict, add spaces in s to construct a sentence where each word is a valid dictionary word. Return all such possible sentences in any order.
 *
 * Note that the same word in the dictionary may be reused multiple times in the segmentation.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "catsanddog", wordDict = ["cat","cats","and","sand","dog"]
 * Output: ["cats and dog","cat sand dog"]
 * Example 2:
 *
 * Input: s = "pineapplepenapple", wordDict = ["apple","pen","applepen","pine","pineapple"]
 * Output: ["pine apple pen apple","pineapple pen apple","pine applepen apple"]
 * Explanation: Note that you are allowed to reuse a dictionary word.
 * Example 3:
 *
 * Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
 * Output: []
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 20
 * 1 <= wordDict.length <= 1000
 * 1 <= wordDict[i].length <= 10
 * s and wordDict[i] consist of only lowercase English letters.
 * All the strings of wordDict are unique.
 * Input is generated in a way that the length of the answer doesn't exceed 105.
 *
 */
public class WordBreak2 {

    // V0
//    public List<String> wordBreak(String s, List<String> wordDict) {
//
//    }

    // V1-1
    // IDEA: Backtracking
    // https://leetcode.com/problems/word-break-ii/editorial/
    public List<String> wordBreak_1_1(String s, List<String> wordDict) {
        // Convert wordDict to a set for O(1) lookups
        Set<String> wordSet = new HashSet<>(wordDict);
        List<String> results = new ArrayList<>();
        // Start the backtracking process
        backtrack(s, wordSet, new StringBuilder(), results, 0);
        return results;
    }

    private void backtrack(
            String s,
            Set<String> wordSet,
            StringBuilder currentSentence,
            List<String> results,
            int startIndex
    ) {
        // If we've reached the end of the string, add the current sentence to results
        if (startIndex == s.length()) {
            results.add(currentSentence.toString().trim());
            return;
        }

        // Iterate over possible end indices
        for (
                int endIndex = startIndex + 1;
                endIndex <= s.length();
                endIndex++
        ) {
            String word = s.substring(startIndex, endIndex);
            // If the word is in the set, proceed with backtracking
            if (wordSet.contains(word)) {
                int currentLength = currentSentence.length();
                currentSentence.append(word).append(" ");
                // Recursively call backtrack with the new end index
                backtrack(s, wordSet, currentSentence, results, endIndex);
                // Reset currentSentence to its original length
                currentSentence.setLength(currentLength);
            }
        }
    }

    // V1-2
    // IDEA: Dynamic Programming - Memoization
    // https://leetcode.com/problems/word-break-ii/editorial/
    // Main function to break the string into words
    public List<String> wordBreak_1_2(String s, List<String> wordDict) {
        Set<String> wordSet = new HashSet<>(wordDict);
        Map<String, List<String>> memoization = new HashMap<>();
        return dfs(s, wordSet, memoization);
    }

    // Depth-first search function to find all possible word break combinations
    private List<String> dfs(
            String remainingStr,
            Set<String> wordSet,
            Map<String, List<String>> memoization
    ) {
        // Check if result for this substring is already memoized
        if (memoization.containsKey(remainingStr)) {
            return memoization.get(remainingStr);
        }

        // Base case: when the string is empty, return a list containing an empty string
        if (remainingStr.isEmpty()) return Collections.singletonList("");
        List<String> results = new ArrayList<>();
        for (int i = 1; i <= remainingStr.length(); ++i) {
            String currentWord = remainingStr.substring(0, i);
            // If the current substring is a valid word
            if (wordSet.contains(currentWord)) {
                for (String nextWord : dfs(
                        remainingStr.substring(i),
                        wordSet,
                        memoization
                )) {
                    // Append current word and next word with space in between if next word exists
                    results.add(
                            currentWord + (nextWord.isEmpty() ? "" : " ") + nextWord
                    );
                }
            }
        }
        // Memoize the results for the current substring
        memoization.put(remainingStr, results);
        return results;
    }

    // V1-3
    // IDEA: Dynamic Programming - Tabulation
    // https://leetcode.com/problems/word-break-ii/editorial/
    public List<String> wordBreak_1_3(String s, List<String> wordDict) {
        // Map to store results of subproblems
        Map<Integer, List<String>> dp = new HashMap<>();

        // Iterate from the end of the string to the beginning
        for (int startIdx = s.length(); startIdx >= 0; startIdx--) {
            // List to store valid sentences starting from startIdx
            List<String> validSentences = new ArrayList<>();

            // Iterate from startIdx to the end of the string
            for (int endIdx = startIdx; endIdx < s.length(); endIdx++) {
                // Extract substring from startIdx to endIdx
                String currentWord = s.substring(startIdx, endIdx + 1);

                // Check if the current substring is a valid word
                if (isWordInDict(currentWord, wordDict)) {
                    // If it's the last word, add it as a valid sentence
                    if (endIdx == s.length() - 1) {
                        validSentences.add(currentWord);
                    } else {
                        // If it's not the last word, append it to each sentence formed by the remaining substring
                        List<String> sentencesFromNextIndex = dp.get(
                                endIdx + 1
                        );
                        for (String sentence : sentencesFromNextIndex) {
                            validSentences.add(currentWord + " " + sentence);
                        }
                    }
                }
            }
            // Store the valid sentences in dp
            dp.put(startIdx, validSentences);
        }
        // Return the sentences formed from the entire string
        return dp.getOrDefault(0, new ArrayList<>());
    }

    // Helper function to check if a word is in the word dictionary
    private boolean isWordInDict(String word, List<String> wordDict) {
        return wordDict.contains(word);
    }


    // V1-4
    // IDEA: Trie Optimization
    // https://leetcode.com/problems/word-break-ii/editorial/
    class TrieNode {

        boolean isEnd;
        TrieNode[] children;

        TrieNode() {
            isEnd = false;
            children = new TrieNode[26];
        }
    }

    class Trie {

        TrieNode root;

        Trie() {
            root = new TrieNode();
        }

        void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode();
                }
                node = node.children[index];
            }
            node.isEnd = true;
        }
    }
    public List<String> wordBreak_1_4(String s, List<String> wordDict) {
        // Build the Trie from the word dictionary
        Trie trie = new Trie();
        for (String word : wordDict) {
            trie.insert(word);
        }

        // Map to store results of subproblems
        Map<Integer, List<String>> dp = new HashMap<>();

        // Iterate from the end of the string to the beginning
        for (int startIdx = s.length(); startIdx >= 0; startIdx--) {
            // List to store valid sentences starting from startIdx
            List<String> validSentences = new ArrayList<>();

            // Initialize current node to the root of the trie
            TrieNode currentNode = trie.root;

            // Iterate from startIdx to the end of the string
            for (int endIdx = startIdx; endIdx < s.length(); endIdx++) {
                char c = s.charAt(endIdx);
                int index = c - 'a';

                // Check if the current character exists in the trie
                if (currentNode.children[index] == null) {
                    break;
                }

                // Move to the next node in the trie
                currentNode = currentNode.children[index];

                // Check if we have found a valid word
                if (currentNode.isEnd) {
                    String currentWord = s.substring(startIdx, endIdx + 1);

                    // If it's the last word, add it as a valid sentence
                    if (endIdx == s.length() - 1) {
                        validSentences.add(currentWord);
                    } else {
                        // If it's not the last word, append it to each sentence formed by the remaining substring
                        List<String> sentencesFromNextIndex = dp.get(
                                endIdx + 1
                        );
                        for (String sentence : sentencesFromNextIndex) {
                            validSentences.add(currentWord + " " + sentence);
                        }
                    }
                }
            }

            // Store the valid sentences in dp
            dp.put(startIdx, validSentences);
        }

        // Return the sentences formed from the entire string
        return dp.getOrDefault(0, new ArrayList<>());
    }

    // V2
    
}
