package LeetCodeJava.String;

// https://leetcode.com/problems/remove-vowels-from-a-string/

/**
 *  1119. Remove Vowels from a String
 *  Easy
 *
 *  Given a string s, remove the vowels 'a', 'e', 'i', 'o', and 'u'
 *  from it, and return the new string.
 *
 *  Example 1:
 *
 *  Input: s = "leetcodeisacommunityforcoders"
 *  Output: "ltcdscmmntyfrcdrs"
 *
 *  Example 2:
 *
 *  Input: s = "aeiou"
 *  Output: ""
 *
 *  Constraints:
 *
 *  1 <= s.length <= 1000
 *  s consists of only lowercase English letters.
 */
public class RemoveVowelsFromAString {

    // V0
    // IDEA: scan chars, append the non-vowel ones to a StringBuilder
    /**
     * time = O(n)
     * space = O(n)
     */
    public String removeVowels(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
