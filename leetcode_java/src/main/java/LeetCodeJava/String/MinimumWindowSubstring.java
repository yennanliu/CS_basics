package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-window-substring/description/?envType=list&envId=xoqag3yj
// https://leetcode.cn/problems/minimum-window-substring/
/**
 * 76. Minimum Window Substring
 * Solved
 * Hard
 * Topics
 * Companies
 * Hint
 * Given two strings s and t of lengths m and n respectively, return the minimum window
 * substring
 *  of s such that every character in t (including duplicates) is included in the window. If there is no such substring, return the empty string "".
 *
 * The testcases will be generated such that the answer is unique.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "ADOBECODEBANC", t = "ABC"
 * Output: "BANC"
 * Explanation: The minimum window substring "BANC" includes 'A', 'B', and 'C' from string t.
 * Example 2:
 *
 * Input: s = "a", t = "a"
 * Output: "a"
 * Explanation: The entire string s is the minimum window.
 * Example 3:
 *
 * Input: s = "a", t = "aa"
 * Output: ""
 * Explanation: Both 'a's from t must be included in the window.
 * Since the largest window of s only has one 'a', return empty string.
 *
 *
 * Constraints:
 *
 * m == s.length
 * n == t.length
 * 1 <= m, n <= 105
 * s and t consist of uppercase and lowercase English letters.
 *
 *
 * Follow up: Could you find an algorithm that runs in O(m + n) time?
 *
 */
import java.util.HashMap;
import java.util.Map;


public class MinimumWindowSubstring {

    // V0
    // TODO : implement
//    public String minWindow(String s, String t) {
//
//        return null;
//    }

    // V1-1
    // IDEA: BRUTE FORCE (TLE)
    // https://youtu.be/jSto0O4AJbM?si=pASTto2TTl1MOI_S
    // https://neetcode.io/problems/minimum-window-with-characters
    public String minWindow_1_1(String s, String t) {
        if (t.isEmpty()) return "";

        Map<Character, Integer> countT = new HashMap<>();
        for (char c : t.toCharArray()) {
            countT.put(c, countT.getOrDefault(c, 0) + 1);
        }

        int[] res = {-1, -1};
        int resLen = Integer.MAX_VALUE;

        for (int i = 0; i < s.length(); i++) {
            Map<Character, Integer> countS = new HashMap<>();
            for (int j = i; j < s.length(); j++) {
                countS.put(s.charAt(j), countS.getOrDefault(s.charAt(j), 0) + 1);

                boolean flag = true;
                for (char c : countT.keySet()) {
                    if (countS.getOrDefault(c, 0) < countT.get(c)) {
                        flag = false;
                        break;
                    }
                }

                if (flag && (j - i + 1) < resLen) {
                    resLen = j - i + 1;
                    res[0] = i;
                    res[1] = j;
                }
            }
        }

        return resLen == Integer.MAX_VALUE ? "" : s.substring(res[0], res[1] + 1);
    }

    // V1-2
    // IDEA: SLIDING WINDOW
    // https://youtu.be/jSto0O4AJbM?si=pASTto2TTl1MOI_S
    // https://neetcode.io/problems/minimum-window-with-characters
    public String minWindow_1_2(String s, String t) {
        if (t.isEmpty())
            return "";

        Map<Character, Integer> countT = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (char c : t.toCharArray()) {
            countT.put(c, countT.getOrDefault(c, 0) + 1);
        }

        int have = 0, need = countT.size();
        int[] res = { -1, -1 };
        int resLen = Integer.MAX_VALUE;
        int l = 0;

        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            window.put(c, window.getOrDefault(c, 0) + 1);

            if (countT.containsKey(c) && window.get(c).equals(countT.get(c))) {
                have++;
            }

            while (have == need) {
                if ((r - l + 1) < resLen) {
                    resLen = r - l + 1;
                    res[0] = l;
                    res[1] = r;
                }

                char leftChar = s.charAt(l);
                window.put(leftChar, window.get(leftChar) - 1);
                if (countT.containsKey(leftChar) && window.get(leftChar) < countT.get(leftChar)) {
                    have--;
                }
                l++;
            }
        }

        return resLen == Integer.MAX_VALUE ? "" : s.substring(res[0], res[1] + 1);
    }

    // V2
    // IDEA: SLIDING WINDOW
    // https://leetcode.com/problems/minimum-window-substring/solutions/4674237/easy-explanation-solution/?envType=list&envId=xoqag3yj
    public String minWindow_2(String s, String t) {
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

    // V3
    // https://leetcode.com/problems/minimum-window-substring/solutions/4673541/beats-100-explained-any-language-by-prodonik/?envType=list&envId=xoqag3yj
    public String minWindow_3(String s, String t) {
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
