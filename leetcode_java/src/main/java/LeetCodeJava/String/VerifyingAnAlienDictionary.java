package LeetCodeJava.String;

// https://leetcode.com/problems/verifying-an-alien-dictionary/description/
// https://leetcode.cn/problems/verifying-an-alien-dictionary/solutions/

import java.util.HashMap;
import java.util.Map;

/**
 * 953. Verifying an Alien Dictionary
 * Easy
 * Topics
 * Companies
 * In an alien language, surprisingly, they also use English lowercase letters, but possibly in a different order. The order of the alphabet is some permutation of lowercase letters.
 *
 * Given a sequence of words written in the alien language, and the order of the alphabet, return true if and only if the given words are sorted lexicographically in this alien language.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["hello","leetcode"], order = "hlabcdefgijkmnopqrstuvwxyz"
 * Output: true
 * Explanation: As 'h' comes before 'l' in this language, then the sequence is sorted.
 * Example 2:
 *
 * Input: words = ["word","world","row"], order = "worldabcefghijkmnpqstuvxyz"
 * Output: false
 * Explanation: As 'd' comes after 'l' in this language, then words[0] > words[1], hence the sequence is unsorted.
 * Example 3:
 *
 * Input: words = ["apple","app"], order = "abcdefghijklmnopqrstuvwxyz"
 * Output: false
 * Explanation: The first three characters "app" match, and the second string is shorter (in size.) According to lexicographical rules "apple" > "app", because 'l' > '∅', where '∅' is defined as the blank character which is less than any other character (More info).
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 100
 * 1 <= words[i].length <= 20
 * order.length == 26
 * All characters in words[i] and order are English lowercase letters.
 *
 *
 */
public class VerifyingAnAlienDictionary {

    // V0
//    public boolean isAlienSorted(String[] words, String order) {
//
//    }

    // V0-1
    // IDEA: BRUTE FORCE + COMPARE adjacent WORD (fixed by gpt)
    public boolean isAlienSorted_0_1(String[] words, String order) {
        // Edge case
        if (words == null || words.length <= 1) {
            return true;
        }

        // Build a rank map for the alien dictionary order
        int[] charRank = new int[26];
        for (int i = 0; i < order.length(); i++) {
            charRank[order.charAt(i) - 'a'] = i;
        }

        // Compare each word with the next word
        for (int i = 1; i < words.length; i++) {
            if (!isValidOrder(words[i - 1], words[i], charRank)) {
                return false;
            }
        }

        return true;
    }

    private boolean isValidOrder(String prev, String cur, int[] charRank) {
        int minLength = Math.min(prev.length(), cur.length());

        // Compare characters one by one
        for (int i = 0; i < minLength; i++) {
            char charPrev = prev.charAt(i);
            char charCur = cur.charAt(i);

            if (charRank[charPrev - 'a'] < charRank[charCur - 'a']) {
                return true; // Order is valid
            } else if (charRank[charPrev - 'a'] > charRank[charCur - 'a']) {
                return false; // Order is invalid
            }
        }

        // Handle case where one word is a prefix of the other
        return prev.length() <= cur.length();
    }


    // V1
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0953-verifying-an-alien-dictionary.java
    public boolean isAlienSorted_1(String[] words, String order) {
        Map<Character, Integer> orderInd = new HashMap<>(); {
            int ind = 0;
            for(char c: order.toCharArray())
                orderInd.put(c, ind++);
        }

        for(int i = 0; i < words.length - 1; i++) {
            String w1 = words[i], w2 = words[i + 1];

            for(int j = 0; j < w1.length(); j++)
                if(j == w2.length())
                    return false;
                else if(w1.charAt(j) != w2.charAt(j))
                    if(orderInd.get(w2.charAt(j)) < orderInd.get(w1.charAt(j)))
                        return false;
                    else
                        break;
        }

        return true;
    }


    // V2
    // https://leetcode.com/problems/verifying-an-alien-dictionary/editorial/
    // IDEA:  Compare adjacent words
    public boolean isAlienSorted_2(String[] words, String order) {
        int[] orderMap = new int[26];
        for (int i = 0; i < order.length(); i++) {
            orderMap[order.charAt(i) - 'a'] = i;
        }

        for (int i = 0; i < words.length - 1; i++) {

            for (int j = 0; j < words[i].length(); j++) {
                // If we do not find a mismatch letter between words[i] and words[i + 1],
                // we need to examine the case when words are like ("apple", "app").
                if (j >= words[i + 1].length())
                    return false;

                if (words[i].charAt(j) != words[i + 1].charAt(j)) {
                    int currentWordChar = words[i].charAt(j) - 'a';
                    int nextWordChar = words[i + 1].charAt(j) - 'a';
                    if (orderMap[currentWordChar] > orderMap[nextWordChar])
                        return false;
                        // if we find the first different letter and they are sorted,
                        // then there's no need to check remaining letters
                    else
                        break;
                }
            }
        }

        return true;
    }


}
