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
//    public int strStr(String haystack, String needle) {
//
//    }


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
