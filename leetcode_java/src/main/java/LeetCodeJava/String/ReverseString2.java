package LeetCodeJava.String;

// https://leetcode.com/problems/reverse-string-ii/description/
// https://leetcode.cn/problems/reverse-string-ii/
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
    // https://leetcode.com/problems/reverse-string-ii/solutions/2609325/java1ms100-faster99-less-memory-usage-by-cxtc/
    public String reverseStr_2(String s, int k) {
        char[] str = s.toCharArray();
        int n = str.length;
        for (int i = 0; i <= n - 1; i += 2 * k) {
            if (i + k - 1 <= n - 1) {
                reverseK(i, i + k - 1, str);
            } else {
                //for fewer than k characters left (edge case)
                reverseK(i, n - 1, str);
            }
        }
        String ans = new String(str);
        return ans;
    }

    public void reverseK(int i, int j, char[] str) {
        while (i < j) {
            char temp = str[i];
            str[i] = str[j];
            str[j] = temp;
            i++;
            j--;
        }
    }

    // V3
    // https://leetcode.com/problems/reverse-string-ii/solutions/6998948/reverse-string-ii-on-time-o1-space-most-ppcxs/
    public void reverse(char[] arr, int i, int j) {
        while (i <= j) {
            char t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;

            i++;
            j--;
        }
    }

    public String reverseStr_3(String s, int k) {
        char[] arr = s.toCharArray();
        int n = s.length();

        int i = 0;
        while (i < n) {
            int j = Math.min(i + k - 1, n - 1);
            reverse(arr, i, j);

            i += 2 * k;
        }

        return new String(arr);
    }




}
