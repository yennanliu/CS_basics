package LeetCodeJava.String;

// https://leetcode.com/problems/most-common-word/

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  819. Most Common Word
 *  Easy
 *
 *  Given a string paragraph and a string array of the banned words banned,
 *  return the most frequent word that is not banned. It is guaranteed there is
 *  at least one word that is not banned, and that the answer is unique.
 *  The words in paragraph are case-insensitive and the answer should be
 *  returned in lowercase.
 *
 *  Example 1:
 *    Input:  paragraph = "Bob hit a ball, the hit BALL flew far after it was hit.",
 *            banned = ["hit"]
 *    Output: "ball"
 *
 *  Example 2:
 *    Input:  paragraph = "a.", banned = []
 *    Output: "a"
 *
 *  Constraints:
 *    1 <= paragraph.length <= 1000
 *    paragraph consists of English letters, space ' ', or one of "!?',;.".
 *    0 <= banned.length <= 100
 *    1 <= banned[i].length <= 10
 *    banned[i] consists of only lowercase English letters.
 */
public class MostCommonWord {

    // V0
    // IDEA: normalize to lowercase, tokenize on non-letters, count, skip banned
    /**
     * time = O(n + b)
     * space = O(n + b)
     */
    public String mostCommonWord(String paragraph, String[] banned) {
        Set<String> bannedSet = new HashSet<>();
        if (banned != null) {
            for (String b : banned) {
                bannedSet.add(b.toLowerCase());
            }
        }

        Map<String, Integer> cnt = new HashMap<>();
        String res = "";
        int best = 0;

        StringBuilder cur = new StringBuilder();
        String lower = paragraph.toLowerCase();
        for (int i = 0; i <= lower.length(); i++) {
            char c = (i < lower.length()) ? lower.charAt(i) : ' ';
            if (c >= 'a' && c <= 'z') {
                cur.append(c);
                continue;
            }
            if (cur.length() == 0) {
                continue;
            }
            String w = cur.toString();
            cur.setLength(0);
            if (bannedSet.contains(w)) {
                continue;
            }
            int c2 = cnt.getOrDefault(w, 0) + 1;
            cnt.put(w, c2);
            if (c2 > best) {
                best = c2;
                res = w;
            }
        }
        return res;
    }
}
