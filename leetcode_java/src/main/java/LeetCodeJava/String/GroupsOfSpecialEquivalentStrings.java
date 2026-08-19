package LeetCodeJava.String;

// https://leetcode.com/problems/groups-of-special-equivalent-strings/

import java.util.HashSet;
import java.util.Set;

/**
 *  893. Groups of Special-Equivalent Strings
 *  Medium
 *
 *  You are given an array of strings of the same length words.
 *
 *  In one move, you can swap any two even indexed characters or any two odd
 *  indexed characters of a string words[i].
 *
 *  Two strings words[i] and words[j] are special-equivalent if after any
 *  number of moves, words[i] == words[j].
 *
 *  A group of special-equivalent strings from words is a non-empty subset of
 *  words such that any string outside of the subset is not special-equivalent
 *  with any string inside the subset.
 *
 *  Return the number of groups of special-equivalent strings from words.
 *
 *  Example 1:
 *  Input: words = ["abcd","cdab","cbad","xyzz","zzxy","zzyx"]
 *  Output: 3
 *
 *  Example 2:
 *  Input: words = ["abc","acb","bac","bca","cab","cba"]
 *  Output: 3
 *
 *  Constraints:
 *   - 1 <= words.length <= 1000
 *   - 1 <= words[i].length <= 20
 */
public class GroupsOfSpecialEquivalentStrings {

    // V0
    // IDEA: CANONICAL SIGNATURE - two strings are special-equivalent iff their
    //       even-index multiset and odd-index multiset both match. Encode that
    //       as a 52-slot count vector and count distinct vectors.
    /**
     * time = O(n * l)
     * space = O(n * l)
     */
    public int numSpecialEquivGroups(String[] words) {
        Set<String> seen = new HashSet<>();
        for (String w : words) {
            int[] cnt = new int[52];
            for (int i = 0; i < w.length(); i++) {
                cnt[(w.charAt(i) - 'a') + 26 * (i % 2)]++;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 52; i++) {
                sb.append(cnt[i]).append('#');
            }
            seen.add(sb.toString());
        }
        return seen.size();
    }
}
