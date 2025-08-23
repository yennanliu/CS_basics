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
    // IDEA: HASHMAP
    public List<String> findAndReplacePattern_0_1(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (check_0_1(word, pattern))
                res.add(word);
        }
        return res;
    }

    boolean check_0_1(String a, String b) {
        for (int i = 0; i < a.length(); i++) {
      /**
       *  NOTE !!!
       *
       *  	- a.indexOf(c) returns the first position where character c appears in string a.
       * 	- b.indexOf(c) does the same for string b.
       *
       *   -> So at every position i, it checks:
       *
       *   ->
       *    🔍 Why it works ?
       *
       * 	 - Characters that first appear at the same position in
       *        and b must map to each other.
       *
       * 	 - If mapping is inconsistent
       *        (like b first appeared at index 1 in a,
       *         but q first appeared at index 0 in b), the check fails.
       *
       * 	 - This is essentially another way to ensure bijection mapping,
       *        but without explicit HashMaps.
       *
       */
      if (a.indexOf(a.charAt(i)) != b.indexOf(b.charAt(i))) return false;
        }
        return true;
    }

    // V0-2
    // IDEA: HASHMAP (gemini)
    public List<String> findAndReplacePattern_0_2(String[] words, String pattern) {
        List<String> result = new ArrayList<>();
        String canonicalPattern = getCanonical(pattern);

        for (String word : words) {
            if (getCanonical(word).equals(canonicalPattern)) {
                result.add(word);
            }
        }

        return result;
    }

    private String getCanonical(String s) {
        Map<Character, Character> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        char nextChar = 'a';

        for (char c : s.toCharArray()) {
            if (!map.containsKey(c)) {
                map.put(c, nextChar);
                nextChar++;
            }
            sb.append(map.get(c));
        }

        return sb.toString();
    }

    // V0-3
    // IDEA: HASH MAP (gpt)
    public List<String> findAndReplacePattern_0_3(String[] words, String pattern) {
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (matchesPattern(word, pattern)) {
                res.add(word);
            }
        }
        return res;
    }

  // Check if word matches the pattern with bijection
  /**
   * 	- Step 1: Check lengths.
   *
   * 	  - If word and pattern have different lengths,
   * 	     they cannot match, so return false.
   *
   *    - Step 2: Create two HashMaps:
   *
   * 	    1.	wToP: Maps each character in the word
   * 	        → its corresponding character in the pattern.
   * 	        ->(e.g. : { character : pattern } )
   *
   * 	    2.	pToW: Maps each character in the pattern
   * 	        → its corresponding character in the word.
   * 	        -> (e.g. : { pattern : character } )
   *
   *   NOTE !!!
   *
   *    -> Using two maps ensures a bijection:
   *
   * 	 - Each word char maps to exactly one pattern char.
   *
   * 	 - Each pattern char maps to exactly one word char.
   */
  private boolean matchesPattern(String word, String pattern) {
        if (word.length() != pattern.length())
            return false;

        Map<Character, Character> wToP = new HashMap<>();
        Map<Character, Character> pToW = new HashMap<>();

    /**
     * 	 - For each character pair (wc, pc) from word and pattern:
     * 	    - 1. Check forward map (wToP):
     * 	        - If wc already maps to a different pc,
     * 	           the mapping is inconsistent → return false.
     * 	        - Otherwise, add the mapping wc → pc.
     *
     * 	    - 2.Check backward map (pToW):
     * 	        - Similarly, if pc already maps to
     * 	          a different wc, return false.
     * 	        - Otherwise, add the mapping pc → wc.
     *
     *   -> This ensures one-to-one correspondence
     *      between letters in word and pattern.
     *
     */
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
