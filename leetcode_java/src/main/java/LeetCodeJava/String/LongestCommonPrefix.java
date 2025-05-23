package LeetCodeJava.String;

// https://leetcode.com/problems/longest-common-prefix/description/
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
//    public String longestCommonPrefix(String[] strs) {
//
//    }

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
