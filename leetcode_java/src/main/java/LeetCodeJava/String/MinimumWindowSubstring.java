package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-window-substring/description/?envType=list&envId=xoqag3yj

import java.util.HashMap;
import java.util.Map;


public class MinimumWindowSubstring {

    // V0
    // TODO : implement
    public String minWindow(String s, String t) {

        return null;
    }

    // V1
    // IDEA: SLIDING WINDOW
    // https://leetcode.com/problems/minimum-window-substring/solutions/4674237/easy-explanation-solution/?envType=list&envId=xoqag3yj
    public String minWindow_1(String s, String t) {
        Map<Character, Integer> targetFreq = new HashMap<>();
        Map<Character, Integer> windowFreq = new HashMap<>();

        for (char c : t.toCharArray()) {
            targetFreq.put(c, targetFreq.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0, minLength = Integer.MAX_VALUE, minLeft = 0, requiredChars = targetFreq.size();

        while (right < s.length()) {
            char currentChar = s.charAt(right);

            windowFreq.put(currentChar, windowFreq.getOrDefault(currentChar, 0) + 1);

            if (targetFreq.containsKey(currentChar) && windowFreq.get(currentChar).equals(targetFreq.get(currentChar))) {
                requiredChars--;
            }

            while (requiredChars == 0) {
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    minLeft = left;
                }

                char leftChar = s.charAt(left);
                windowFreq.put(leftChar, windowFreq.get(leftChar) - 1);

                if (targetFreq.containsKey(leftChar) && windowFreq.get(leftChar) < targetFreq.get(leftChar)) {
                    requiredChars++;
                }

                left++;
            }

            right++;
        }

        if (minLength == Integer.MAX_VALUE) {
            return "";
        }

        return s.substring(minLeft, minLeft + minLength);
    }

    // V2
    // https://leetcode.com/problems/minimum-window-substring/solutions/4673541/beats-100-explained-any-language-by-prodonik/?envType=list&envId=xoqag3yj
    public String minWindow_2(String s, String t) {
        if (s == null || t == null || s.length() == 0 || t.length() == 0 ||
                s.length() < t.length()) {
            return new String();
        }
        int[] map = new int[128];
        int count = t.length();
        int start = 0, end = 0, minLen = Integer.MAX_VALUE, startIndex = 0;
        /// UPVOTE !
        for (char c : t.toCharArray()) {
            map[c]++;
        }

        char[] chS = s.toCharArray();

        while (end < chS.length) {
            if (map[chS[end++]]-- > 0) {
                count--;
            }
            while (count == 0) {
                if (end - start < minLen) {
                    startIndex = start;
                    minLen = end - start;
                }
                if (map[chS[start++]]++ == 0) {
                    count++;
                }
            }
        }

        return minLen == Integer.MAX_VALUE ? new String() :
                new String(chS, startIndex, minLen);
    }

}
