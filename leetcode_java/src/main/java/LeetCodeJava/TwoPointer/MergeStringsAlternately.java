package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/merge-strings-alternately/description/
/**
 * 1768. Merge Strings Alternately
 * Easy
 * Topics
 * Companies
 * Hint
 * You are given two strings word1 and word2. Merge the strings by adding letters in alternating order, starting with word1. If a string is longer than the other, append the additional letters onto the end of the merged string.
 *
 * Return the merged string.
 *
 *
 *
 * Example 1:
 *
 * Input: word1 = "abc", word2 = "pqr"
 * Output: "apbqcr"
 * Explanation: The merged string will be merged as so:
 * word1:  a   b   c
 * word2:    p   q   r
 * merged: a p b q c r
 * Example 2:
 *
 * Input: word1 = "ab", word2 = "pqrs"
 * Output: "apbqrs"
 * Explanation: Notice that as word2 is longer, "rs" is appended to the end.
 * word1:  a   b
 * word2:    p   q   r   s
 * merged: a p b q   r   s
 * Example 3:
 *
 * Input: word1 = "abcd", word2 = "pq"
 * Output: "apbqcd"
 * Explanation: Notice that as word1 is longer, "cd" is appended to the end.
 * word1:  a   b   c   d
 * word2:    p   q
 * merged: a p b q c   d
 *
 *
 * Constraints:
 *
 * 1 <= word1.length, word2.length <= 100
 * word1 and word2 consist of lowercase English letters.
 *
 *
 *
 */
public class MergeStringsAlternately {

    // V0
    // IDEA: STRING OP
    public String mergeAlternately(String word1, String word2) {
        // edge
        if (word1 == null && word2 == null) {
            return null;
        }
        if (word1 == null || word2 == null) {
            if (word1 == null) {
                return word2;
            }
            return word1;
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < word1.length() && i < word2.length()) {
            sb.append(word1.charAt(i));
            sb.append(word2.charAt(i));

            i += 1;
        }

        if (i < word1.length()) {
            for (int j = i; j < word1.length(); j++) {
                sb.append(word1.charAt(j));
            }
        }

        if (i < word2.length()) {
            for (int j = i; j < word2.length(); j++) {
                sb.append(word2.charAt(j));
            }
        }

        return sb.toString();
    }

    // V1
    // https://leetcode.com/problems/merge-strings-alternately/solutions/6141609/video-simple-solution-by-niits-ip8k/
    public String mergeAlternately_1(String word1, String word2) {
        StringBuilder merged = new StringBuilder();
        int maxLength = Math.max(word1.length(), word2.length());

        for (int i = 0; i < maxLength; i++) {
            if (i < word1.length()) {
                merged.append(word1.charAt(i));
            }
            if (i < word2.length()) {
                merged.append(word2.charAt(i));
            }
        }

        return merged.toString();
    }

    // V2
    // https://leetcode.com/problems/merge-strings-alternately/solutions/3429116/easy-solutions-in-java-python-and-c-look-80lt/
    public String mergeAlternately_2(String word1, String word2) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < word1.length() || i < word2.length()) {
            if (i < word1.length()) {
                result.append(word1.charAt(i));
            }
            if (i < word2.length()) {
                result.append(word2.charAt(i));
            }
            i++;
        }
        return result.toString();
    }

}
