package LeetCodeJava.HashTable;

// https://leetcode.com/problems/odd-string-difference/description/

import java.util.*;

/**
 * 2451. Odd String Difference
 * Easy
 *
 * You are given an array of equal-length strings words. Assume that the length of each string is n.
 *
 * Each string words[i] can be converted into a difference integer array difference[i] of length n - 1 where difference[i][j] = words[i][j+1] - words[i][j] where 0 <= j <= n - 2. Note that the difference between two letters is the difference between their positions in the alphabet i.e. the position of 'a' is 0, 'b' is 1, and 'z' is 25.
 *
 * All the strings in words have the same difference integer array, except one. You should find that string.
 *
 * Return the string in words that has different difference integer array.
 *
 *
 * Example 1:
 *
 * Input: words = ["adc","wzy","abc"]
 * Output: "abc"
 * Explanation:
 * - The difference integer array of "adc" is [3 - 0, 2 - 3] = [3, -1].
 * - The difference integer array of "wzy" is [25 - 22, 24 - 25] = [3, -1].
 * - The difference integer array of "abc" is [1 - 0, 2 - 1] = [1, 1].
 * The odd array out is [1, 1], so we return the corresponding string, "abc".
 *
 * Example 2:
 *
 * Input: words = ["aaa","bob","ccc","ddd"]
 * Output: "bob"
 * Explanation: The difference integer array of "bob" is [14, -14] and the remaining arrays are all [0, 0].
 *
 *
 * Constraints:
 *
 * 3 <= words.length <= 100
 * n == words[i].length
 * 2 <= n <= 20
 * words[i] consists of lowercase English letters.
 *
 */
public class OddStringDifference {

    // V0
    // IDEA: GROUP WORDS BY THEIR DIFFERENCE SIGNATURE -> THE ODD ONE IS ALONE
    /**
     * time = O(W * N)
     * space = O(W * N)
     */
    public String oddString(String[] words) {
        // signature (consecutive letter gaps, joined as a string) -> words having it
        Map<String, List<String>> groups = new HashMap<>();
        for (String word : words) {
            String key = signature(word);
            groups.computeIfAbsent(key, x -> new ArrayList<>()).add(word);
        }

        // exactly one word differs, so its bucket is the only singleton
        for (List<String> members : groups.values()) {
            if (members.size() == 1) {
                return members.get(0);
            }
        }
        return "";
    }

    /** Builds the difference array of a word as a comma-separated key. */
    private String signature(String word) {
        StringBuilder key = new StringBuilder();
        for (int i = 0; i + 1 < word.length(); i++) {
            key.append(word.charAt(i + 1) - word.charAt(i)).append(',');
        }
        return key.toString();
    }
}
