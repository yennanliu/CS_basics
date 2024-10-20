package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-string-chain/description/

import java.util.*;

/**
 *1048. Longest String Chain
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an array of words where each word consists of lowercase English letters.
 *
 * wordA is a predecessor of wordB if and only if we can insert exactly one letter anywhere in wordA without changing the order of the other characters to make it equal to wordB.
 *
 * For example, "abc" is a predecessor of "abac", while "cba" is not a predecessor of "bcad".
 * A word chain is a sequence of words [word1, word2, ..., wordk] with k >= 1, where word1 is a predecessor of word2, word2 is a predecessor of word3, and so on. A single word is trivially a word chain with k == 1.
 *
 * Return the length of the longest possible word chain with words chosen from the given list of words.
 *
 * Example 1:
 *
 * Input: words = ["a","b","ba","bca","bda","bdca"]
 * Output: 4
 * Explanation: One of the longest word chains is ["a","ba","bda","bdca"].
 * Example 2:
 *
 * Input: words = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
 * Output: 5
 * Explanation: All the words can be put in a word chain ["xb", "xbc", "cxbc", "pcxbc", "pcxbcf"].
 * Example 3:
 *
 * Input: words = ["abcd","dbqca"]
 * Output: 1
 * Explanation: The trivial word chain ["abcd"] is one of the longest word chains.
 * ["abcd","dbqca"] is not a valid word chain because the ordering of the letters is changed.
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 16
 * words[i] only consists of lowercase English letters.
 *
 */
public class LongestStringChain {

    // V0
    // TODO : implement
//    public int longestStrChain(String[] words) {
//    }

    // V1-1
    // IDEA : DFS + Memoization | Increasing Word Length:
    // https://leetcode.com/problems/longest-string-chain/solutions/2153004/explaining-three-approaches-java/
    private Map<Integer, List<String>> wordLengthMap;
    private Map<String, Integer> memo;

    public int longestStrChain_1(String[] words) {
        // store each word with its corresponding length
        wordLengthMap = new HashMap<>();
        for (String word: words) {
            wordLengthMap.putIfAbsent(word.length(), new ArrayList<>());
            wordLengthMap.get(word.length()).add(word);
        }

        int maxPath = 1;
        memo = new HashMap<>();
        for (String word: words)
            maxPath = Math.max(maxPath, dfs(word));

        return maxPath;
    }

    private int dfs(String word) {
        if (!wordLengthMap.containsKey(word.length() + 1)) return 1; // if there are no words of the next length, we're done with this path.
        if (memo.containsKey(word)) return memo.get(word); // if we're computed this word before, return the result.

        int maxPath = 0;
        // for each word, find all words which are 1 letter longer and see if they are valid successors.
        List<String> nextWords = wordLengthMap.get(word.length() + 1);
        for (String nextWord: nextWords)
            if (isOneOff(word, nextWord))
                maxPath = Math.max(maxPath, dfs(nextWord));

        memo.put(word, maxPath + 1); // store our result
        return memo.get(word);
    }

    // returns true if two strings differ by no more than 1 letter
    private boolean isOneOff(String a, String b) {
        int count = 0;
        for (int i=0, j=0; i<b.length() && j<a.length() && count <= 1; i++) {
            if (a.charAt(j) != b.charAt(i)) count++;
            else j++;
        }
        return count <= 1;
    }


    // V1-2
    // IDEA : DFS + Memoization | Decreasing Word Length:
    // https://leetcode.com/problems/longest-string-chain/solutions/2153004/explaining-three-approaches-java/
//    private Set<String> wordDict;
//    private Map<String, Integer> memo;
//
//    public int longestStrChain(String[] words) {
//        wordDict = new HashSet<>();
//        Collections.addAll(wordDict, words); // adding all words to a set for constant look-up
//
//        int maxPath = 1;
//        memo = new HashMap<>();
//        for (String word: words)
//            maxPath = Math.max(maxPath, dfs(word));
//
//        return maxPath;
//    }
//
//    private int dfs(String word) {
//        if (memo.containsKey(word)) return memo.get(word); // if we're computed this word before, return the result.
//
//        StringBuilder sb = new StringBuilder(word);
//        int maxPath = 0;
//        // delete each character, check if that's a valid word in the set, add the character back and continue
//        for (int i=0; i<word.length(); i++) {
//            sb.deleteCharAt(i);
//            String prevWord = sb.toString();
//            if (wordDict.contains(prevWord))
//                maxPath = Math.max(maxPath, dfs(prevWord));
//            sb.insert(i, word.charAt(i));
//        }
//
//        memo.put(word, maxPath + 1); // store the result
//        return memo.get(word);
//    }


    // V1-3
    // IDEA : DP | Decreasing Word Length:
    // https://leetcode.com/problems/longest-string-chain/solutions/2153004/explaining-three-approaches-java/
    public int longestStrChain_1_3(String[] words) {
        Arrays.sort(words, (String a, String b) -> a.length() - b.length()); // sort by length
        Map<String, Integer> dp = new HashMap<>();

        int maxPath = 1;
        // same idea behind the previous approach but performed iteratively.
        for (String word: words) {
            int currLength = 1;
            StringBuilder sb = new StringBuilder(word);
            for (int i=0; i<word.length(); i++) {
                sb.deleteCharAt(i);
                String prevWord = sb.toString();
                currLength = Math.max(currLength, dp.getOrDefault(prevWord, 0) + 1);
                sb.insert(i, word.charAt(i));
            }
            dp.put(word, currLength);
            maxPath = Math.max(maxPath, currLength);
        }

        return maxPath;
    }

    // V2

}
