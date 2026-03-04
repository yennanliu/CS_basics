package LeetCodeJava.String;

// https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/description/
/**
 *  28. Find the Index of the First Occurrence in a String
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given two strings needle and haystack, return the index of the first occurrence of needle in haystack, or -1 if needle is not part of haystack.
 *
 *
 *
 * Example 1:
 *
 * Input: haystack = "sadbutsad", needle = "sad"
 * Output: 0
 * Explanation: "sad" occurs at index 0 and 6.
 * The first occurrence is at index 0, so we return 0.
 * Example 2:
 *
 * Input: haystack = "leetcode", needle = "leeto"
 * Output: -1
 * Explanation: "leeto" did not occur in "leetcode", so we return -1.
 *
 *
 * Constraints:
 *
 * 1 <= haystack.length, needle.length <= 104
 * haystack and needle consist of only lowercase English characters.
 *
 *
 */
public class FindTheIndexOfTheFirstOccurrenceInAString {

    // V0
    // IDEA: SUB STRING + startsWith (OK, init a new string every time, not efficient)
    public int strStr(String haystack, String needle) {
        // edge
        if (haystack.isEmpty() && !needle.isEmpty()) {
            return -1;
        }
        for (int i = 0; i < haystack.length(); i++) {
            // V1
            // get sub str
//            String sub = haystack.substring(i);
//            if (sub.startsWith(needle)) {
//                return i;
//            }

            // V2
            if (haystack.substring(i).startsWith(needle)) {
                return i;
            }
            
        }

        return -1;
    }


    // V0-1
    // IDEA: STRING OP (gemini)
    public int strStr_0_1(String haystack, String needle) {
        // 1. Edge case: if needle is empty, return 0 per LeetCode requirements
        if (needle.isEmpty()) {
            return 0;
        }

        int hLen = haystack.length();
        int nLen = needle.length();

        /** NOTE !!!  only need to loop till  (hLen - nLen) */
        // 2. Optimization: only loop up to (hLen - nLen)
        // If haystack is "hello" and needle is "ll", we don't need to check
        // index 4 because "o" is shorter than "ll".
        for (int i = 0; i <= hLen - nLen; i++) {

            // Option A: Use startsWith (cleanest)
            // We use i + nLen to ensure the substring is exactly the right size
            if (haystack.substring(i, i + nLen).equals(needle)) {
                return i;
            }

            /* Option B: Manual character check (most efficient - avoids substring creation)
            int j;
            for (j = 0; j < nLen; j++) {
            if (haystack.charAt(i + j) != needle.charAt(j)) {
                break;
            }
            }
            if (j == nLen) return i;
            */
        }

        return -1;
    }


    // V0-2
    // IDEA: SUB STRING + startsWith optimized (GPT)
    public int strStr_0_2(String haystack, String needle) {

        // edge case
        if (needle.isEmpty()) {
            return 0;
        }

        if (haystack.isEmpty()) {
            return -1;
        }

        /** NOTE !!!  only need to loop till i <= haystack.length() - needle.length() */
        for (int i = 0; i <= haystack.length() - needle.length(); i++) {
            // get substring starting at i
            String sub = haystack.substring(i);

            if (sub.startsWith(needle)) {
                return i;
            }
        }

        return -1;
    }



    // V1
    // https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/solutions/4749634/beats-100-with-this-easy-solution-in-jav-27xk/
    public int strStr_1(String haystack, String needle) {
        for (int i = 0, j = needle.length(); j <= haystack.length(); i++, j++) {
            if (haystack.substring(i, j).equals(needle)) {
                return i;
            }
        }
        return -1;
    }


    // V2
    // https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/solutions/6750826/video-slicing-approach-by-niits-7s6u/
    public int strStr_2(String haystack, String needle) {
        if (haystack.length() < needle.length()) {
            return -1;
        }

        for (int i = 0; i <= haystack.length() - needle.length(); i++) {
            if (haystack.substring(i, i + needle.length()).equals(needle)) {
                return i;
            }
        }

        return -1;
    }




}
