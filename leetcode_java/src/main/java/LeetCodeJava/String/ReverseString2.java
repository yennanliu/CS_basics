package LeetCodeJava.String;

// https://leetcode.com/problems/reverse-string-ii/description/
/**
 *  541. Reverse String II
 * Solved
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * Given a string s and an integer k, reverse the first k characters for every 2k characters counting from the start of the string.
 *
 * If there are fewer than k characters left, reverse all of them. If there are less than 2k but greater than or equal to k characters, then reverse the first k characters and leave the other as original.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abcdefg", k = 2
 * Output: "bacdfeg"
 * Example 2:
 *
 * Input: s = "abcd", k = 2
 * Output: "bacd"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s consists of only lowercase English letters.
 * 1 <= k <= 104
 *
 */
public class ReverseString2 {

    // V0
//    public String reverseStr(String s, int k) {
//
//    }

    // V1
    // https://leetcode.com/problems/reverse-string-ii/editorial/
    public String reverseStr_1(String s, int k) {
        char[] a = s.toCharArray();
        for (int start = 0; start < a.length; start += 2 * k) {
            int i = start, j = Math.min(start + k - 1, a.length - 1);
            while (i < j) {
                char tmp = a[i];
                a[i++] = a[j];
                a[j--] = tmp;
            }
        }
        return new String(a);
    }


    // V2


}
