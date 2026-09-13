package LeetCodeJava.String;

// https://leetcode.com/problems/license-key-formatting/description/

/**
 * 482. License Key Formatting
 * Easy
 *
 * You are given a license key represented as a string s that consists of only alphanumeric characters and dashes. The string is separated into n + 1 groups by n dashes. You are also given an integer k.
 *
 * We want to reformat the string s such that each group contains exactly k characters, except for the first group, which could be shorter than k but still must contain at least one character. Furthermore, there must be a dash inserted between two groups, and you should convert all lowercase letters to uppercase.
 *
 * Return the reformatted license key.
 *
 * Example 1:
 *
 * Input: s = "5F3Z-2e-9-w", k = 4
 * Output: "5F3Z-2E9W"
 * Explanation: The string s has been split into two parts, each part has 4 characters.
 * Note that the two extra dashes are not needed and can be removed.
 *
 * Example 2:
 *
 * Input: s = "2-5g-3-J", k = 2
 * Output: "2-5G-3J"
 * Explanation: The string s has been split into three parts, each part has 2 characters except the first part as it could be shorter as mentioned above.
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s consists of English letters, digits, and dashes '-'.
 * 1 <= k <= 10^4
 *
 */
public class LicenseKeyFormatting {


    // V0
    // TODO : implement
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/String/license-key-formatting.py
//    public String licenseKeyFormatting(String s, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/license-key-formatting/solutions/316752/clean-and-self-explanatory-11-ms-java-solution/
    /**
     * time = O(N)
     * space = O(N)
     */
    public String licenseKeyFormatting_1(String S, int K) {

        StringBuilder sb = new StringBuilder();

        for (int i = S.length() - 1, count = 0 ; i >= 0 ; --i) {

            char c = S.charAt(i);
            if (c == '-') continue;

            // put a '-' first if we already append K characters
            if (count == K) {
                sb.append('-');
                count = 0;
            }

            sb.append(Character.toUpperCase(c));
            ++count;
        }

        return sb.reverse().toString();
    }

    // V2
    // https://leetcode.com/problems/license-key-formatting/solutions/2087766/java-solution-16ms-runtime/
    /**
     * time = O(N)
     * space = O(N)
     */
    public String licenseKeyFormatting_2(String s, int k) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '-') continue;
            if (count == k) {
                sb.append('-');
                count = 0;
            }
            sb.append(s.charAt(i));
            count++;
        }
        return sb.reverse().toString().toUpperCase();
    }

}
