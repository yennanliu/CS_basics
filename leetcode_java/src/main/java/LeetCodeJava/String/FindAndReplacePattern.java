package LeetCodeJava.String;

// https://leetcode.com/problems/find-and-replace-pattern/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 890. Find and Replace Pattern
 * 1414
 * Topics
 * premium lock icon
 * Companies
 * Given a list of strings words and a string pattern, return a list of words[i] that match pattern. You may return the answer in any order.
 *
 * A word matches the pattern if there exists a permutation of letters p so that after replacing every letter x in the pattern with p(x), we get the desired word.
 *
 * Recall that a permutation of letters is a bijection from letters to letters: every letter maps to another letter, and no two letters map to the same letter.
 *
 *
 *
 * Example 1:
 *
 * Input: words = ["abc","deq","mee","aqq","dkd","ccc"], pattern = "abb"
 * Output: ["mee","aqq"]
 * Explanation: "mee" matches the pattern because there is a permutation {a -> m, b -> e, ...}.
 * "ccc" does not match the pattern because {a -> c, b -> c, ...} is not a permutation, since a and b map to the same letter.
 * Example 2:
 *
 * Input: words = ["a","b","c"], pattern = "a"
 * Output: ["a","b","c"]
 *
 *
 * Constraints:
 *
 * 1 <= pattern.length <= 20
 * 1 <= words.length <= 50
 * words[i].length == pattern.length
 * pattern and words[i] are lowercase English letters.
 *
 */
public class FindAndReplacePattern {

    // V0
//    public List<String> findAndReplacePattern(String[] words, String pattern) {
//
//    }

    // V0-1
    // IDEA: HASH MAP (gpt)
    public List<String> findAndReplacePattern_0_1(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (matchesPattern(word, pattern)) {
                res.add(word);
            }
        }
        return res;
    }

    // Check if word matches the pattern with bijection
    private boolean matchesPattern(String word, String pattern) {
        if (word.length() != pattern.length())
            return false;

        Map<Character, Character> wToP = new HashMap<>();
        Map<Character, Character> pToW = new HashMap<>();

        for (int i = 0; i < word.length(); i++) {
            char wc = word.charAt(i);
            char pc = pattern.charAt(i);

            if (wToP.containsKey(wc)) {
                if (wToP.get(wc) != pc)
                    return false;
            } else {
                wToP.put(wc, pc);
            }

            if (pToW.containsKey(pc)) {
                if (pToW.get(pc) != wc)
                    return false;
            } else {
                pToW.put(pc, wc);
            }
        }

        return true;
    }

    // V1
    // IDEA: HASHMAP
    // https://leetcode.com/problems/find-and-replace-pattern/solutions/2348332/java-easy-solution-100-faster-code-by-sh-px0m/
    public List<String> findAndReplacePattern_1(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (check(word, pattern))
                res.add(word);
        }
        return res;
    }

    boolean check(String a, String b) {
        for (int i = 0; i < a.length(); i++) {
            if (a.indexOf(a.charAt(i)) != b.indexOf(b.charAt(i)))
                return false;
        }
        return true;
    }

    // V2
    // IDEA: HASHMAP
    // https://leetcode.com/problems/find-and-replace-pattern/solutions/1221188/js-python-java-c-easy-maskmap-solution-w-hvc1/
    List<String> ans;
    Map<Character, Character> codex;
    char[] cipher;

    public List<String> findAndReplacePattern_2(String[] words, String pattern) {
        ans = new ArrayList<>();
        codex = new HashMap<>();
        cipher = pattern.toCharArray();
        for (int i = 0; i < pattern.length(); i++)
            cipher[i] = translate(cipher[i]);
        for (String word : words)
            compare(word);
        return ans;
    }

    private char translate(char c) {
        if (!codex.containsKey(c))
            codex.put(c, (char) (97 + codex.size()));
        return codex.get(c);
    }

    private void compare(String word) {
        codex.clear();
        for (int i = 0; i < word.length(); i++)
            if (translate(word.charAt(i)) != cipher[i])
                return;
        ans.add(word);
    }

}
