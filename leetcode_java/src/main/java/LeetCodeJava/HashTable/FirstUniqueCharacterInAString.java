package LeetCodeJava.HashTable;

// https://leetcode.com/problems/first-unique-character-in-a-string/description/

import java.util.HashMap;

/**
 * 387. First Unique Character in a String
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given a string s, find the first non-repeating character in it and return its index. If it does not exist, return -1.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "leetcode"
 *
 * Output: 0
 *
 * Explanation:
 *
 * The character 'l' at index 0 is the first character that does not occur at any other index.
 *
 * Example 2:
 *
 * Input: s = "loveleetcode"
 *
 * Output: 2
 *
 * Example 3:
 *
 * Input: s = "aabb"
 *
 * Output: -1
 *
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 105
 * s consists of only lowercase English letters.
 *
 */
public class FirstUniqueCharacterInAString {

    // LC 387
//    public int firstUniqChar(String s) {
//
//    }

    // V1
    // https://leetcode.com/problems/first-unique-character-in-a-string/editorial/
    public int firstUniqChar_1(String s) {
        HashMap<Character, Integer> count = new HashMap<Character, Integer>();
        int n = s.length();
        // build hash map : character and how often it appears
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        // find the index
        for (int i = 0; i < n; i++) {
            if (count.get(s.charAt(i)) == 1)
                return i;
        }
        return -1;
    }

    // V2

    // V3

}
