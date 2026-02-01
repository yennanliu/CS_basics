package LeetCodeJava.Trie;

// https://leetcode.com/problems/extra-characters-in-a-string/

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * 2707. Extra Characters in a String
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given a 0-indexed string s and a dictionary of words dictionary. You have to break s into one or more non-overlapping substrings such that each substring is present in dictionary. There may be some extra characters in s which are not present in any of the substrings.
 *
 * Return the minimum number of extra characters left over if you break up s optimally.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "leetscode", dictionary = ["leet","code","leetcode"]
 * Output: 1
 * Explanation: We can break s in two substrings: "leet" from index 0 to 3 and "code" from index 5 to 8. There is only 1 unused character (at index 4), so we return 1.
 *
 * Example 2:
 *
 * Input: s = "sayhelloworld", dictionary = ["hello","world"]
 * Output: 3
 * Explanation: We can break s in two substrings: "hello" from index 3 to 7 and "world" from index 8 to 12. The characters at indices 0, 1, 2 are not used in any substring and thus are considered as extra characters. Hence, we return 3.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 50
 * 1 <= dictionary.length <= 50
 * 1 <= dictionary[i].length <= 50
 * dictionary[i] and s consists of only lowercase English letters
 * dictionary contains distinct words
 *
 */
public class ExtraCharactersInAString {

    // V0
//    public int minExtraChar(String s, String[] dictionary) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=ONstwO1cD7c
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F2707-extra-characters-in-a-string.java
    /**
     * time = O(L)
     * space = O(1)
     */
    public int minExtraChar_1(String s, String[] dictionary) {
        int n = s.length();
        int[] dp = new int[n+1];

        Arrays.fill(dp, n);
        dp[0] = 0;

        for (int i = 1; i <= n; ++i) {
            for (int j = 0; j < dictionary.length; ++j) {
                int len = dictionary[j].length();
                if (i >= len && s.substring(i - len, i).equals(dictionary[j])) {
                    dp[i] = Math.min(dp[i], dp[i - len]);
                }
            }
            dp[i] = Math.min(dp[i], dp[i - 1] + 1);
        }

        return dp[n];
    }


    // V2-1
    // https://leetcode.com/problems/extra-characters-in-a-string/editorial/
    // IDEA: Top Down Dynamic Programming with Substring Method
    Integer[] memo;
    HashSet<String> dictionarySet;

    /**
     * time = O(L)
     * space = O(1)
     */
    public int minExtraChar_2_1(String s, String[] dictionary) {
        int n = s.length();
        memo = new Integer[n];
        dictionarySet = new HashSet<>(Arrays.asList(dictionary));
        return dp(0, n, s);
    }

    private int dp(int start, int n, String s) {
        if (start == n) {
            return 0;
        }
        if (memo[start] != null) {
            return memo[start];
        }
        // To count this character as a left over character
        // move to index 'start + 1'
        int ans = dp(start + 1, n, s) + 1;
        for (int end = start; end < n; end++) {
            String curr = s.substring(start, end + 1);
            if (dictionarySet.contains(curr)) {
                ans = Math.min(ans, dp(end + 1, n, s));
            }
        }

        return memo[start] = ans;
    }


    // V2-2
    // https://leetcode.com/problems/extra-characters-in-a-string/editorial/
    // IDEA: Bottom Up Dynamic Programming with Substring Method
    /**
     * time = O(L)
     * space = O(1)
     */
    public int minExtraChar_2_2(String s, String[] dictionary) {
        int n = s.length();
        HashSet dictionarySet = new HashSet<>(Arrays.asList(dictionary));
        int[] dp = new int[n + 1];

        for (int start = n - 1; start >= 0; start--) {
            dp[start] = dp[start + 1] + 1;
            for (int end = start; end < n; end++) {
                String curr = s.substring(start, end + 1);
                if (dictionarySet.contains(curr)) {
                    dp[start] = Math.min(dp[start], dp[end + 1]);
                }
            }
        }

        return dp[0];
    }

    // V2-3
    // https://leetcode.com/problems/extra-characters-in-a-string/editorial/
    // IDEA: Top Down Dynamic Programming with Trie
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap();
        boolean is_word = false;
    }
    TrieNode root;
    Integer[] memo_2_3;
    /**
     * time = O(L)
     * space = O(1)
     */
    public int minExtraChar_2_3(String s, String[] dictionary) {
        int n = s.length();
        root = buildTrie(dictionary);
        memo_2_3 = new Integer[n + 1];
        return dp_2_3(0, n, s);
    }

    private int dp_2_3(int start, int n, String s) {
        if (start == n) {
            return 0;
        }
        if (memo_2_3[start] != null) {
            return memo_2_3[start];
        }

        TrieNode node = root;
        // To count this character as a left over character
        // move to index 'start + 1'
        int ans = dp_2_3(start + 1, n, s) + 1;
        for (int end = start; end < n; end++) {
            char c = s.charAt(end);
            if (!node.children.containsKey(c)) {
                break;
            }
            node = node.children.get(c);
            if (node.is_word) {
                ans = Math.min(ans, dp_2_3(end + 1, n, s));
            }
        }

        return memo_2_3[start] = ans;
    }

    private TrieNode buildTrie(String[] dictionary) {
        TrieNode root = new TrieNode();
        for (String word : dictionary) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
            }
            node.is_word = true;
        }
        return root;
    }


    // V2-4
    // https://leetcode.com/problems/extra-characters-in-a-string/editorial/
    // IDEA: : Bottom Up Dynamic Programming with Trie
//    public int minExtraChar_2_4(String s, String[] dictionary) {
//        int n = s.length();
//        var root = buildTrie(dictionary);
//        var dp = new int[n + 1];
//
//        for (int start = n - 1; start >= 0; start--) {
//            dp[start] = dp[start + 1] + 1;
//            var node = root;
//            for (int end = start; end < n; end++) {
//                if (!node.children.containsKey(s.charAt(end))) {
//                    break;
//                }
//                node = node.children.get(s.charAt(end));
//                if (node.isWord) {
//                    dp[start] = Math.min(dp[start], dp[end + 1]);
//                }
//            }
//        }
//
//        return dp[0];
//    }
//
//    private TrieNode buildTrie(String[] dictionary) {
//        var root = new TrieNode();
//        for (var word : dictionary) {
//            var node = root;
//            for (var c : word.toCharArray()) {
//                node.children.putIfAbsent(c, new TrieNode());
//                node = node.children.get(c);
//            }
//            node.isWord = true;
//        }
//        return root;
//    }


}
