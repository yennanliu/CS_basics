package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/can-make-palindrome-from-substring/description/

import java.util.*;

/**
 * 1177. Can Make Palindrome from Substring
 * Medium
 *
 * You are given a string s and array queries where queries[i] = [left_i, right_i, k_i].
 * We may rearrange the substring s[left_i...right_i] for each query and then choose up to k_i of them
 * to replace with any lowercase English letter.
 *
 * If the substring is possible to be a palindrome string after the operations above, the result of the
 * query is true. Otherwise, the result is false.
 *
 * Return a boolean array answer where answer[i] is the result of the i-th query queries[i].
 *
 * Note that each letter is counted individually for replacement, so if, for example
 * s[left_i...right_i] = "aaa", and k_i = 2, we can only replace two of the letters.
 * Also, note that no query modifies the initial string s.
 *
 * Example 1:
 *
 * Input: s = "abcda", queries = [[3,3,0],[1,2,0],[0,3,1],[0,3,2],[0,4,1]]
 * Output: [true,false,false,true,true]
 * Explanation:
 * queries[0]: substring = "d", is palindrome.
 * queries[1]: substring = "bc", is not palindrome.
 * queries[2]: substring = "abcd", is not palindrome after replacing only 1 character.
 * queries[3]: substring = "abcd", could be changed to "abba" which is palindrome.
 * Also this can be changed to "baab" first rearrange it "bacd" then replace "cd" with "ab".
 * queries[4]: substring = "abcda", could be changed to "abcba" which is palindrome.
 *
 * Example 2:
 *
 * Input: s = "lyb", queries = [[0,1,0],[2,2,1]]
 * Output: [false,true]
 *
 * Constraints:
 *
 * 1 <= s.length, queries.length <= 10^5
 * 0 <= left_i <= right_i < s.length
 * 0 <= k_i <= s.length
 * s consists of lowercase English letters.
 *
 */
public class CanMakePalindromeFromSubstring {

    // V0
    // IDEA: PREFIX XOR BITMASK (PARITY OF EACH LETTER COUNT)
    //       after a free rearrange, only letters with an ODD count are a problem.
    //       with `odd` such letters, rewriting one of each pair fixes two at once, and one
    //       leftover can sit in the middle -> odd / 2 replacements are needed.
    //       answer = (odd / 2 <= k).
    //       prefix[i] = xor of the letter bits of s[0..i-1], so the parity mask of
    //       s[left..right] is prefix[right + 1] ^ prefix[left].
    /**
     * time = O(N + Q)
     * space = O(N)
     */
    public List<Boolean> canMakePaliQueries(String s, int[][] queries) {
        int n = s.length();
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) {
            // flip the bit of this letter: bit set <-> odd count so far
            prefix[i + 1] = prefix[i] ^ (1 << (s.charAt(i) - 'a'));
        }

        List<Boolean> result = new ArrayList<>();
        for (int[] query : queries) {
            int left = query[0];
            int right = query[1];
            int k = query[2];
            int parityMask = prefix[right + 1] ^ prefix[left];
            int oddLetters = Integer.bitCount(parityMask);
            result.add(oddLetters / 2 <= k);
        }
        return result;
    }

    // V1
    // IDEA: PREFIX SUM OF THE 26 LETTER COUNTS
    //       same idea, keeps the actual counts instead of only the parity.
    /**
     * time = O((N + Q) * 26)
     * space = O(N * 26)
     */
    public List<Boolean> canMakePaliQueries_1(String s, int[][] queries) {
        int n = s.length();
        // counts[i][c] = occurrences of letter c in s[0..i-1]
        int[][] counts = new int[n + 1][26];
        for (int i = 0; i < n; i++) {
            for (int c = 0; c < 26; c++) {
                counts[i + 1][c] = counts[i][c];
            }
            counts[i + 1][s.charAt(i) - 'a']++;
        }

        List<Boolean> result = new ArrayList<>();
        for (int[] query : queries) {
            int left = query[0];
            int right = query[1];
            int k = query[2];
            int oddLetters = 0;
            for (int c = 0; c < 26; c++) {
                int countInRange = counts[right + 1][c] - counts[left][c];
                if (countInRange % 2 == 1) {
                    oddLetters++;
                }
            }
            result.add(oddLetters / 2 <= k);
        }
        return result;
    }
}
