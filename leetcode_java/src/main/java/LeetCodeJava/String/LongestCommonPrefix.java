package LeetCodeJava.String;

// https://leetcode.com/problems/longest-common-prefix/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 14. Longest Common Prefix
 * Solved
 * Easy
 * Topics
 * Companies
 * Write a function to find the longest common prefix string amongst an array of strings.
 *
 * If there is no common prefix, return an empty string "".
 *
 *
 *
 * Example 1:
 *
 * Input: strs = ["flower","flow","flight"]
 * Output: "fl"
 * Example 2:
 *
 * Input: strs = ["dog","racecar","car"]
 * Output: ""
 * Explanation: There is no common prefix among the input strings.
 *
 *
 * Constraints:
 *
 * 1 <= strs.length <= 200
 * 0 <= strs[i].length <= 200
 * strs[i] consists of only lowercase English letters if it is non-empty.
 *
 */
public class LongestCommonPrefix {

    /** NOTE !!!
     *
     *  LCP: Longest Common Prefix (LCP)
     *
     */

    // V0
    // IDEA: PREFIX + STRING OP (GEMINI)
    public String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        if (strs.length == 1)
            return strs[0];

        // 1. Generate all possible prefixes from the first word
        String first = strs[0];
        List<String> prefixList = new ArrayList<>();
        String cur = "";
        for (char c : first.toCharArray()) {
            cur += c;
            prefixList.add(cur);
        }

        // 2. Search from LONGEST prefix to SHORTEST
        for (int i = prefixList.size() - 1; i >= 0; i--) {
            String prefix = prefixList.get(i);
            boolean isCommon = true;

            for (String s : strs) {
                if (!s.startsWith(prefix)) {
                    isCommon = false;
                    break; // No need to check other words for this prefix
                }
            }

            if (isCommon) {
                return prefix; // Found the longest one!
            }
        }

        return "";
    }


    // V0-0-1
    // IDEA: SET + BRUTE FORCE (fixed by gpt)
    public String longestCommonPrefix_0_0_1(String[] strs) {
        // edge
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }

        String first = strs[0];

        // Build prefix list from shortest → longest
        List<String> prefixes = new ArrayList<>();
        String curr = "";
        for (int i = 0; i < first.length(); i++) {
            curr += first.charAt(i);
            prefixes.add(curr);
        }

        String res = "";

        // Check common prefix for all others
        for (String prefix : prefixes) {
            boolean ok = true;

            for (int i = 1; i < strs.length; i++) {
                // if prefix longer than word → immediately fail
                if (strs[i].length() < prefix.length() ||
                        !strs[i].startsWith(prefix)) {

                    ok = false;
                    break;
                }
            }

            if (!ok)
                break; // no need checking longer prefixes
            res = prefix; // update longest match so far
        }

        return res;
    }

    // V0-0-2
    // IDEA: SET + BRUTE FORCE (fixed by gemini)
    /**
     * Finds the longest common prefix among a set of strings using the Vertical Scanning approach.
     * Time Complexity: O(S), where S is the sum of all characters in all strings (in the worst case).
     * In the best/average case, it's O(N * minLen) where N is array length and minLen is length of shortest string.
     */
    public String longestCommonPrefix_0_0_2(String[] strs) {
        // Edge case: Handle null array, empty array, or single element array.
        if (strs == null || strs.length == 0) {
            return "";
        }

        // The first string is the reference point for the maximum possible common prefix length.
        String firstStr = strs[0];

        // Iterate through each character of the first string (index 'i').
        for (int i = 0; i < firstStr.length(); i++) {
            char currentChar = firstStr.charAt(i);

            // Now, check this character against the corresponding position in all other strings.
            for (int j = 1; j < strs.length; j++) {

                String currentStr = strs[j];

                // Two break conditions:
                // 1. The current string is shorter than the current index 'i'.
                // 2. The character at index 'i' in the current string is NOT the same as the reference character.
                if (i == currentStr.length() || currentStr.charAt(i) != currentChar) {

                    // If we break, the common prefix is the substring of the first string
                    // up to the character we just checked (index i).
                    return firstStr.substring(0, i);
                }
            }
        }

        // If the entire first string is a prefix of all other strings, return the first string.
        return firstStr;
    }

    // V0-1
    // IDEA: STRING OP (fixed by gpt)
    public String longestCommonPrefix_0_1(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }

        // Find the shortest string in strs
        String shortest = strs[0];
        for (int i = 1; i < strs.length; i++) {
            if (strs[i].length() < shortest.length()) {
                shortest = strs[i];
            }
        }

        int end = shortest.length();

        // Check prefix lengths from longest to shortest
        for (int len = end; len > 0; len--) {
            String prefix = shortest.substring(0, len);
            boolean allMatch = true;
            for (String s : strs) {
                if (!s.startsWith(prefix)) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                return prefix;
            }
        }

        // No common prefix found
        return "";
    }

    // V0-2
    // IDEA: STRING OP (GEMINI)
    public String longestCommonPrefix_0_2(String[] strs) {
        if (strs.length == 0)
            return "";

        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++) {
            // While the current string doesn't start with the prefix
            while (strs[i].indexOf(prefix) != 0) {
                // Shorten the prefix by one character from the end
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty())
                    return "";
            }
        }
        return prefix;
    }


    // V0-3
    // IDEA: DP (gpt)
    public String longestCommonPrefix_0_3(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }

        // dpPrefix holds the current longest common prefix
        String dpPrefix = strs[0];

        for (int i = 1; i < strs.length; i++) {
            dpPrefix = commonPrefix_0_3(dpPrefix, strs[i]);
            if (dpPrefix.isEmpty()) {
                break;
            }
        }

        return dpPrefix;
    }

    private String commonPrefix_0_3(String s1, String s2) {
        int minLen = Math.min(s1.length(), s2.length());
        int idx = 0;

        while (idx < minLen && s1.charAt(idx) == s2.charAt(idx)) {
            idx++;
        }

        return s1.substring(0, idx);
    }


    // V1-1
    // https://leetcode.com/problems/longest-common-prefix/editorial/
    // IDEA: Horizontal scanning
    public String longestCommonPrefix_1_1(String[] strs) {
        if (strs.length == 0)
            return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty())
                    return "";
            }
        return prefix;
    }


    // V1-2
    // https://leetcode.com/problems/longest-common-prefix/editorial/
    // IDEA: Vertical scanning
    public String longestCommonPrefix_1_2(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || strs[j].charAt(i) != c)
                    return strs[0].substring(0, i);
            }
        }
        return strs[0];
    }

    // V1-3
    // https://leetcode.com/problems/longest-common-prefix/editorial/
    // IDEA: Divide and conquer
    public String longestCommonPrefix_1_3(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        return longestCommonPrefix(strs, 0, strs.length - 1);
    }

    private String longestCommonPrefix(String[] strs, int l, int r) {
        if (l == r) {
            return strs[l];
        } else {
            int mid = (l + r) / 2;
            String lcpLeft = longestCommonPrefix(strs, l, mid);
            String lcpRight = longestCommonPrefix(strs, mid + 1, r);
            return commonPrefix(lcpLeft, lcpRight);
        }
    }

    private String commonPrefix(String left, String right) {
        int min = Math.min(left.length(), right.length());
        for (int i = 0; i < min; i++) {
            if (left.charAt(i) != right.charAt(i))
                return left.substring(0, i);
        }
        return left.substring(0, min);
    }

    // V1-4
    // https://leetcode.com/problems/longest-common-prefix/editorial/
    // IDEA: BINARY SEARCH
    public String longestCommonPrefix_1_5(String[] strs) {
        if (strs == null || strs.length == 0)
            return "";
        int minLen = Integer.MAX_VALUE;
        for (String str : strs)
            minLen = Math.min(minLen, str.length());
        int low = 1;
        int high = minLen;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (isCommonPrefix(strs, middle))
                low = middle + 1;
            else
                high = middle - 1;
        }
        return strs[0].substring(0, (low + high) / 2);
    }

    private boolean isCommonPrefix(String[] strs, int len) {
        String str1 = strs[0].substring(0, len);
        for (int i = 1; i < strs.length; i++)
            if (!strs[i].startsWith(str1))
                return false;
        return true;
    }



    // V2



}
