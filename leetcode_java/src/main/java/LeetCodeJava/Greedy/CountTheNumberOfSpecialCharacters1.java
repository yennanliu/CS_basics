package LeetCodeJava.Greedy;

// https://leetcode.com/problems/count-the-number-of-special-characters-i/description/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  3120. Count the Number of Special Characters I
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given a string word. A letter is called special if it appears both in lowercase and uppercase in word.
 *
 * Return the number of special letters in word.
 *
 *
 *
 * Example 1:
 *
 * Input: word = "aaAbcBC"
 *
 * Output: 3
 *
 * Explanation:
 *
 * The special characters in word are 'a', 'b', and 'c'.
 *
 * Example 2:
 *
 * Input: word = "abc"
 *
 * Output: 0
 *
 * Explanation:
 *
 * No character in word appears in uppercase.
 *
 * Example 3:
 *
 * Input: word = "abBCab"
 *
 * Output: 1
 *
 * Explanation:
 *
 * The only special character in word is 'b'.
 *
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 50
 * word consists of only lowercase and uppercase English letters.
 *
 */
public class CountTheNumberOfSpecialCharacters1 {

    // V0
//    public int numberOfSpecialChars(String word) {
//
//    }

    // V0-1
    // IDEA: 2 HASHSET (fixed by gemini)
    /**
     * Counts the number of letters that appear as both lowercase and uppercase.
     * Time Complexity: O(N) where N is the length of the word.
     * Space Complexity: O(1) since the sets are capped at 26 characters each.
     */
    public int numberOfSpecialChars_0_1(String word) {
        if (word == null || word.isEmpty()) {
            return 0;
        }

        // Sets to track all unique lowercase and uppercase characters encountered
        Set<Character> lowerSeen = new HashSet<>();
        Set<Character> upperSeen = new HashSet<>();

        // 1. Traverse the word and populate the sets
        for (char ch : word.toCharArray()) {
            if (ch >= 'a' && ch <= 'z') {
                lowerSeen.add(ch);
            } else if (ch >= 'A' && ch <= 'Z') {
                upperSeen.add(ch);
            }
        }

        // 2. Count the special characters
        int specialCount = 0;

        // Iterate over all unique lowercase characters seen
        for (char lowerCh : lowerSeen) {

            // Get the corresponding uppercase character
            // The difference between 'a' and 'A' is constant (32 in ASCII)
            char upperCh = Character.toUpperCase(lowerCh);

            // Check if the corresponding uppercase character was also seen
            if (upperSeen.contains(upperCh)) {
                specialCount++;
            }
        }

        return specialCount;
    }

    // V0-2
    // IDEA: HASHMAP (fixed by gpt)
    public int numberOfSpecialChars_0_2(String word) {
        if (word == null || word.isEmpty())
            return 0;
        if (word.length() == 1)
            return 0;

        // Map each letter to [lowerSeen, upperSeen]
        Map<Character, int[]> map = new HashMap<>();
        int cnt = 0;

        for (char ch : word.toCharArray()) {
            char base = Character.toLowerCase(ch); // Normalize key
            map.putIfAbsent(base, new int[2]); // index 0 = lowercase, 1 = uppercase

            if (Character.isLowerCase(ch)) {
                /** NOTE !!!  need to update the val back to hashmap */
                map.get(base)[0] = 1;
            } else {
                /** NOTE !!!  need to update the val back to hashmap */
                map.get(base)[1] = 1;
            }
        }

        // Count special characters
        for (int[] arr : map.values()) {
            if (arr[0] == 1 && arr[1] == 1) {
                cnt++;
            }
        }

        return cnt;
    }


    // V1
    // https://leetcode.com/problems/count-the-number-of-special-characters-i/solutions/7378008/beats-100-on-time-by-hanyuuuuu-cr82/
    public int numberOfSpecialChars_1(String word) {
        boolean[] isFound = new boolean['z' + 1];
        for (char c : word.toCharArray()) {
            isFound[c] = true;
        }
        int result = 0;
        for (char uc = 'A', lc = 'a'; uc <= 'Z'; uc++, lc++) {
            if (isFound[uc] && isFound[lc]) {
                result++;
            }
        }
        return result;
    }


    // V2-1
    // IDEA: SET
    // https://leetcode.com/problems/count-the-number-of-special-characters-i/solutions/5052874/array-is-better-than-sets-detailed-compa-duu1/
    public int numberOfSpecialChars_2_1(String word) {
        Set<Character> smallSet = new HashSet<>();
        Set<Character> capitalSet = new HashSet<>();

        for (char c : word.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                smallSet.add(c);
            } else {
                capitalSet.add(c);
            }
        }

        int count = 0;
        for (char c : smallSet) {
            if (capitalSet.contains(Character.toUpperCase(c))) {
                count++;
            }
        }

        return count;
    }


    // V2-2
    // IDEA: INDEX ARRAY
    // https://leetcode.com/problems/count-the-number-of-special-characters-i/solutions/5052874/array-is-better-than-sets-detailed-compa-duu1/
    public int numberOfSpecialChars_2_2(String word) {
        int[] small = new int[26];
        int[] capital = new int[26];
        int count = 0;

        for (char c : word.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                small[c - 'a'] = 1;
            } else {
                capital[c - 'A'] = 1;
            }
        }

        for (int i = 0; i < 26; i++) {
            if (small[i] == 1 && capital[i] == 1) {
                count++;
            }
        }

        return count;
    }


    // V3

}
