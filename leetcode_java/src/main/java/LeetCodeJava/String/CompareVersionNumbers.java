package LeetCodeJava.String;

// https://leetcode.com/problems/compare-version-numbers/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 165. Compare Version Numbers
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given two version strings, version1 and version2, compare them. A version string consists of revisions separated by dots '.'. The value of the revision is its integer conversion ignoring leading zeros.
 *
 * To compare version strings, compare their revision values in left-to-right order. If one of the version strings has fewer revisions, treat the missing revision values as 0.
 *
 * Return the following:
 *
 * If version1 < version2, return -1.
 * If version1 > version2, return 1.
 * Otherwise, return 0.
 *
 *
 * Example 1:
 *
 * Input: version1 = "1.2", version2 = "1.10"
 *
 * Output: -1
 *
 * Explanation:
 *
 * version1's second revision is "2" and version2's second revision is "10": 2 < 10, so version1 < version2.
 *
 * Example 2:
 *
 * Input: version1 = "1.01", version2 = "1.001"
 *
 * Output: 0
 *
 * Explanation:
 *
 * Ignoring leading zeroes, both "01" and "001" represent the same integer "1".
 *
 * Example 3:
 *
 * Input: version1 = "1.0", version2 = "1.0.0.0"
 *
 * Output: 0
 *
 * Explanation:
 *
 * version1 has less revisions, which means every missing revision are treated as "0".
 *
 *
 *
 * Constraints:
 *
 * 1 <= version1.length, version2.length <= 500
 * version1 and version2 only contain digits and '.'.
 * version1 and version2 are valid version numbers.
 * All the given revisions in version1 and version2 can be stored in a 32-bit integer.
 *
 *
 *
 */
public class CompareVersionNumbers {

    // V0
//    public int compareVersion(String version1, String version2) {
//
//        return 0;
//    }

    // V0-1
    // IDEA: STR OP (fixed by gemini)
    public int compareVersion_0_1(String version1, String version2) {
        // 1. Split by dot (regex needs \\.)
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");

        // 2. Iterate through the LONGEST array
        int maxLength = Math.max(v1.length, v2.length);

        for (int i = 0; i < maxLength; i++) {
            // 3. Get the integer value, or 0 if we ran out of parts
            // Integer.parseInt automatically handles leading zeros (e.g., "001" -> 1)
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;

            if (num1 > num2) {
                return 1;
            } else if (num1 < num2) {
                return -1;
            }
            // If they are equal, the loop continues to the next part
        }

        // 4. If the loop finishes, all compared parts were equal
        return 0;
    }


    // V0-2
    // IDEA: STR OP (gpt)
    public int compareVersion_0_2(String version1, String version2) {

        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        for (String x : version1.split("\\.")) {
            list1.add(Integer.parseInt(x)); // ✅ keep zeros
        }

        for (String x : version2.split("\\.")) {
            list2.add(Integer.parseInt(x)); // ✅ keep zeros
        }

        int size = Math.max(list1.size(), list2.size());

        for (int i = 0; i < size; i++) {
            int v1 = i < list1.size() ? list1.get(i) : 0;
            int v2 = i < list2.size() ? list2.get(i) : 0;

            if (v1 > v2)
                return 1;
            if (v1 < v2)
                return -1;
        }

        return 0;
    }


    // V1
    // https://leetcode.com/problems/compare-version-numbers/solutions/50788/my-java-solution-without-split-by-lucife-0yk4/
    public int compareVersion_1(String version1, String version2) {
        int temp1 = 0, temp2 = 0;
        int len1 = version1.length(), len2 = version2.length();
        int i = 0, j = 0;
        while (i < len1 || j < len2) {
            temp1 = 0;
            temp2 = 0;
            while (i < len1 && version1.charAt(i) != '.') {
                temp1 = temp1 * 10 + version1.charAt(i++) - '0';

            }
            while (j < len2 && version2.charAt(j) != '.') {
                temp2 = temp2 * 10 + version2.charAt(j++) - '0';

            }
            if (temp1 > temp2)
                return 1;
            else if (temp1 < temp2)
                return -1;
            else {
                i++;
                j++;

            }

        }
        return 0;
    }

    // V2-1
    // https://leetcode.com/problems/compare-version-numbers/solutions/5104397/beats-10000-of-users-with-java2-methods-2dyv2/
    public int compareVersion_2_1(String version1, String version2) {
        int i = 0, j = 0;
        while (i < version1.length() || j < version2.length()) {
            int num1 = 0, num2 = 0;
            while (i < version1.length() && version1.charAt(i) != '.') {
                num1 = num1 * 10 + (version1.charAt(i++) - '0');
            }
            while (j < version2.length() && version2.charAt(j) != '.') {
                num2 = num2 * 10 + (version2.charAt(j++) - '0');
            }
            if (num1 < num2) {
                return -1;
            }
            if (num1 > num2) {
                return 1;
            }
            i++;
            j++;
        }
        return 0;
    }

    // V2-2
    // https://leetcode.com/problems/compare-version-numbers/solutions/5104397/beats-10000-of-users-with-java2-methods-2dyv2/
    public int compareVersion_2_2(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        for (int i = 0; i < Math.max(v1.length, v2.length); i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 < num2) {
                return -1;
            }
            if (num1 > num2) {
                return 1;
            }
        }
        return 0;
    }




}
