package LeetCodeJava.String;

// https://leetcode.com/problems/find-and-replace-in-string/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 833. Find And Replace in String
 * Medium
 * Topics
 * Companies
 * You are given a 0-indexed string s that you must perform k replacement operations on. The replacement operations are given as three 0-indexed parallel arrays, indices, sources, and targets, all of length k.
 * <p>
 * To complete the ith replacement operation:
 * <p>
 * Check if the substring sources[i] occurs at index indices[i] in the original string s.
 * If it does not occur, do nothing.
 * Otherwise if it does occur, replace that substring with targets[i].
 * For example, if s = "abcd", indices[i] = 0, sources[i] = "ab", and targets[i] = "eee", then the result of this replacement will be "eeecd".
 * <p>
 * All replacement operations must occur simultaneously, meaning the replacement operations should not affect the indexing of each other. The testcases will be generated such that the replacements will not overlap.
 * <p>
 * For example, a testcase with s = "abc", indices = [0, 1], and sources = ["ab","bc"] will not be generated because the "ab" and "bc" replacements overlap.
 * Return the resulting string after performing all replacement operations on s.
 * <p>
 * A substring is a contiguous sequence of characters in a string.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * Input: s = "abcd", indices = [0, 2], sources = ["a", "cd"], targets = ["eee", "ffff"]
 * Output: "eeebffff"
 * Explanation:
 * "a" occurs at index 0 in s, so we replace it with "eee".
 * "cd" occurs at index 2 in s, so we replace it with "ffff".
 * Example 2:
 * <p>
 * <p>
 * Input: s = "abcd", indices = [0, 2], sources = ["ab","ec"], targets = ["eee","ffff"]
 * Output: "eeecd"
 * Explanation:
 * "ab" occurs at index 0 in s, so we replace it with "eee".
 * "ec" does not occur at index 2 in s, so we do nothing.
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= s.length <= 1000
 * k == indices.length == sources.length == targets.length
 * 1 <= k <= 100
 * 0 <= indexes[i] < s.length
 * 1 <= sources[i].length, targets[i].length <= 50
 * s consists of only lowercase English letters.
 * sources[i] and targets[i] consist of only lowercase English letters.
 */
public class FindAndReplaceInString {

    // V0
//    public String findReplaceString(String s, int[] indices, String[] sources, String[] targets) {
//
//    }

    // V1
    // IDEA : HASHMAP
    // https://leetcode.com/problems/find-and-replace-in-string/submissions/1454170265/
    public String findReplaceString_1(String s, int[] indices, String[] sources, String[] targets) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < indices.length; i++) {
            if (s.startsWith(sources[i], indices[i])) {
                map.put(indices[i], i);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            if (!map.containsKey(i)) {
                sb.append(s.charAt(i));
                i++;
            } else { //replace chars
                sb.append(targets[map.get(i)]);
                i += sources[map.get(i)].length();
            }
        }
        return sb.toString();
    }

    // V2
    // TODO : replacer `Pair` in code
    // https://leetcode.com/problems/find-and-replace-in-string/submissions/1454169549/
//    public String findReplaceString_2(String s, int[] indices, String[] sources, String[] targets) {
//        Map<Integer , Pair> replacements = new TreeMap<>();
//        StringBuilder res = new StringBuilder();
//
//        for(int i = 0 ; i < indices.length ; i++)
//            if(s.substring(indices[i]).startsWith(sources[i]))
//                replacements.put(indices[i] , new Pair(targets[i] , sources[i].length()-1));
//
//        for(int i = 0 ; i < s.length() ; i++){
//            if(replacements.containsKey(i)){
//                Pair p = replacements.get(i);
//                res.append(p.getKey());
//                i += (int)p.getValue();
//            }
//            else
//                res.append(s.charAt(i));
//        }
//
//        return res.toString();
//    }

}
