package LeetCodeJava.Trie;

// https://leetcode.com/problems/extra-characters-in-a-string/

import java.util.Arrays;

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


    // V2

}
