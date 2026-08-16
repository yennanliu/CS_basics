package LeetCodeJava.String;

// https://leetcode.com/problems/word-abbreviation/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 527. Word Abbreviation
 * Hard
 * Lock: Prime
 *
 * Given an array of distinct strings words, return the minimal possible abbreviations
 * for every word.
 *
 * The following are the rules for a string abbreviation:
 *
 * 1. The initial abbreviation for each word is: the first character, then the number of
 *    characters in between, followed by the last character.
 * 2. If more than one word shares the same abbreviation, then perform the following
 *    operation:
 *    - Increase the prefix (characters in the first part) of each of their abbreviations
 *      by 1.
 *      - For example, say you start with the words ["abcdef","abndef"] both initially
 *        abbreviated as "a4f". Then, a sequence of operations would be
 *        ["a4f","a4f"] -> ["ab3f","ab3f"] -> ["abc2f","abn2f"].
 *    - This operation is repeated until every abbreviation is unique.
 * 3. At the end, if an abbreviation did not make a word shorter, then keep it as the
 *    original word.
 *
 * Example 1:
 *
 * Input: words = ["like","god","internal","me","internet","interval","intension","face",
 *                 "intrusion"]
 * Output: ["l2e","god","internal","me","i6t","interval","inte4n","f2e","intr4n"]
 *
 * Example 2:
 *
 * Input: words = ["aa","aaa"]
 * Output: ["aa","aaa"]
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 400
 * 2 <= words[i].length <= 400
 * words[i] consists of lowercase English letters.
 * All the strings of words are unique.
 *
 */
public class WordAbbreviation {

    // V0
    // IDEA: GROUP + LONGEST COMMON PREFIX
    /**
     *   Two words can only COLLIDE when they have the SAME length, SAME first char
     *   and SAME last char -> GROUP by (len, first, last).
     *
     *   Inside a group, SORT the words: the longest common prefix of a word with ANY
     *   other word of the group is reached with one of its 2 SORTED NEIGHBOURS.
     *   -> a word needs prefix length (lcp + 1) to break the tie with that neighbour.
     *
     *   Finally, keep the ORIGINAL word if the abbreviation is not shorter (rule 3).
     *
     *   time  = O(n * log(n) * L)  // n = words.length, L = max word length
     *   space = O(n * L)
     */
    public List<String> wordsAbbreviation(List<String> words) {
        int n = words.size();
        String[] res = new String[n];

        Map<String, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String w = words.get(i);
            String key = w.length() + "#" + w.charAt(0) + "#" + w.charAt(w.length() - 1);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(i);
        }

        for (List<Integer> idxs : groups.values()) {
            // sort by the WORD itself -> words sharing long prefixes become NEIGHBOURS
            idxs.sort((a, b) -> words.get(a).compareTo(words.get(b)));

            int[] need = new int[idxs.size()];
            for (int a = 0; a < idxs.size(); a++) {
                need[a] = 1;
            }

            for (int a = 0; a + 1 < idxs.size(); a++) {
                String w1 = words.get(idxs.get(a));
                String w2 = words.get(idxs.get(a + 1));

                int lcp = 0;
                while (lcp < w1.length() && w1.charAt(lcp) == w2.charAt(lcp)) {
                    lcp += 1;
                }

                /** NOTE !!!
                 *
                 *  BOTH words must keep 1 char MORE than the common part,
                 *  otherwise their abbreviations would still be identical
                 */
                need[a] = Math.max(need[a], lcp + 1);
                need[a + 1] = Math.max(need[a + 1], lcp + 1);
            }

            for (int a = 0; a < idxs.size(); a++) {
                int i = idxs.get(a);
                res[i] = abbrev(words.get(i), need[a]);
            }
        }

        List<String> out = new ArrayList<>();
        for (String s : res) {
            out.add(s);
        }
        return out;
    }

    /** word[0, prefixLen) + (# of skipped chars) + last char */
    private String abbrev(String word, int prefixLen) {
        String cand = word.substring(0, prefixLen)
                + (word.length() - prefixLen - 1)
                + word.charAt(word.length() - 1);
        // rule 3 : only keep the abbreviation when it is ACTUALLY shorter
        return cand.length() < word.length() ? cand : word;
    }

}
