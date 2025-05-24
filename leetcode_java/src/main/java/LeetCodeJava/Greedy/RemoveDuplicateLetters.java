package LeetCodeJava.Greedy;

// https://leetcode.com/problems/remove-duplicate-letters/description/

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 316. Remove Duplicate Letters
 * Medium
 * Topics
 * Companies
 * Hint
 * Given a string s, remove duplicate letters so that every letter appears once and only once. You must make sure your result is the smallest in lexicographical order among all possible results.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "bcabc"
 * Output: "abc"
 * Example 2:
 *
 * Input: s = "cbacdcbc"
 * Output: "acdb"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s consists of lowercase English letters.
 *
 *
 * Note: This question is the same as 1081: https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/
 *
 */
public class RemoveDuplicateLetters {

    // V0
//    public String removeDuplicateLetters(String s) {
//
//    }

    // V1
    // IDEA: STACK
    // https://leetcode.com/problems/remove-duplicate-letters/solutions/76762/java-on-solution-using-stack-with-detail-z3nb/
    public String removeDuplicateLetters_1(String s) {
        Stack<Character> stack = new Stack<>();
        int[] count = new int[26];
        char[] arr = s.toCharArray();
        for (char c : arr) {
            count[c - 'a']++;
        }
        boolean[] visited = new boolean[26];
        for (char c : arr) {
            count[c - 'a']--;
            if (visited[c - 'a']) {
                continue;
            }
            while (!stack.isEmpty() && stack.peek() > c && count[stack.peek() - 'a'] > 0) {
                visited[stack.peek() - 'a'] = false;
                stack.pop();
            }
            stack.push(c);
            visited[c - 'a'] = true;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : stack) {
            sb.append(c);
        }
        return sb.toString();
    }

    // V2
    // https://leetcode.com/problems/remove-duplicate-letters/solutions/76766/easy-to-understand-iterative-java-soluti-bgeo/
    public String removeDuplicateLetters_2(String s) {
        if (s == null || s.length() <= 1)
            return s;

        Map<Character, Integer> lastPosMap = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            lastPosMap.put(s.charAt(i), i);
        }

        char[] result = new char[lastPosMap.size()];
        int begin = 0, end = findMinLastPos(lastPosMap);

        for (int i = 0; i < result.length; i++) {
            char minChar = 'z' + 1;
            for (int k = begin; k <= end; k++) {
                if (lastPosMap.containsKey(s.charAt(k)) && s.charAt(k) < minChar) {
                    minChar = s.charAt(k);
                    begin = k + 1;
                }
            }

            result[i] = minChar;
            if (i == result.length - 1)
                break;

            lastPosMap.remove(minChar);
            if (s.charAt(end) == minChar)
                end = findMinLastPos(lastPosMap);
        }

        return new String(result);
    }

    private int findMinLastPos(Map<Character, Integer> lastPosMap) {
        if (lastPosMap == null || lastPosMap.isEmpty())
            return -1;
        int minLastPos = Integer.MAX_VALUE;
        for (int lastPos : lastPosMap.values()) {
            minLastPos = Math.min(minLastPos, lastPos);
        }
        return minLastPos;
    }

    // V3
    // https://leetcode.com/problems/remove-duplicate-letters/solutions/76768/a-short-on-recursive-greedy-solution-by-z4daq/
    // IDEA: RECURSIVE
    public String removeDuplicateLetters_3(String s) {
        int[] cnt = new int[26];
        int pos = 0; // the position for the smallest s[i]
        for (int i = 0; i < s.length(); i++)
            cnt[s.charAt(i) - 'a']++;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < s.charAt(pos))
                pos = i;
            if (--cnt[s.charAt(i) - 'a'] == 0)
                break;
        }
        return s.length() == 0 ? ""
                : s.charAt(pos) + removeDuplicateLetters_3(s.substring(pos + 1).replaceAll("" + s.charAt(pos), ""));
    }

    // V3


}
