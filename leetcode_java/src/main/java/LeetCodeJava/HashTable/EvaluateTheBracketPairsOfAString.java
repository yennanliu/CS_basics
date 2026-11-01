package LeetCodeJava.HashTable;

// https://leetcode.com/problems/evaluate-the-bracket-pairs-of-a-string/description/

import java.util.*;

/**
 * 1807. Evaluate the Bracket Pairs of a String
 * Medium
 *
 * You are given a string s that contains some bracket pairs, with each pair containing a non-empty key.
 *
 * For example, in the string "(name)is(age)yearsold", there are two bracket pairs that contain the
 * keys "name" and "age".
 *
 * You know the values of a wide range of keys. This is represented by a 2D string array knowledge
 * where each knowledge[i] = [keyi, valuei] indicates that key keyi has a value of valuei.
 *
 * You are tasked to evaluate all of the bracket pairs. When you evaluate a bracket pair that contains
 * some key keyi, you will:
 *
 * Replace keyi and the bracket pair with the key's corresponding valuei.
 * If you do not know the value of the key, you will replace keyi and the bracket pair with a question
 * mark "?" (without the quotation marks).
 *
 * Each key will appear at most once in your knowledge. There will not be any nested brackets in s.
 *
 * Return the resulting string after evaluating all of the bracket pairs.
 *
 *
 * Example 1:
 *
 * Input: s = "(name)is(age)yearsold", knowledge = [["name","bob"],["age","two"]]
 * Output: "bobistwoyearsold"
 *
 * Example 2:
 *
 * Input: s = "hi(name)", knowledge = [["a","b"]]
 * Output: "hi?"
 *
 * Example 3:
 *
 * Input: s = "(a)(a)(a)aaa", knowledge = [["a","yes"]]
 * Output: "yesyesyesaaa"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * 0 <= knowledge.length <= 10^5
 * knowledge[i].length == 2
 * 1 <= keyi.length, valuei.length <= 10
 * s consists of lowercase English letters and round brackets '(' and ')'.
 * Every open bracket '(' in s will have a corresponding close bracket ')'.
 * The key in each bracket pair of s will be non-empty.
 * There will not be any nested bracket pairs in s.
 * keyi and valuei consist of lowercase English letters.
 * Each keyi in knowledge is unique.
 *
 */
public class EvaluateTheBracketPairsOfAString {

    // V0
    // IDEA: HASH TABLE (key -> value) + ONE LINEAR SCAN into a StringBuilder
    //       brackets never nest, so the next ')' after a '(' always closes it: no stack needed
    /**
     * time = O(N + M)
     * space = O(N + M)
     */
    public String evaluate(String s, List<List<String>> knowledge) {
        Map<String, String> valueOf = new HashMap<>();
        for (List<String> pair : knowledge) {
            valueOf.put(pair.get(0), pair.get(1));
        }

        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == '(') {
                int close = s.indexOf(')', i + 1);
                String key = s.substring(i + 1, close);
                result.append(valueOf.getOrDefault(key, "?"));
                // continue right after the closing bracket
                i = close + 1;
            } else {
                result.append(c);
                i++;
            }
        }
        return result.toString();
    }
}
