package LeetCodeJava.String;

// https://leetcode.com/problems/reverse-words-in-a-string-iii/

/**
 *  557. Reverse Words in a String III
 *  Easy
 *
 *  Given a string s, reverse the order of characters in each word within a
 *  sentence while still preserving whitespace and initial word order.
 *
 *  Example 1:
 *    Input:  s = "Let's take LeetCode contest"
 *    Output: "s'teL ekat edoCteeL tsetnoc"
 *
 *  Example 2:
 *    Input:  s = "Mr Ding"
 *    Output: "rM gniD"
 *
 *  Constraints:
 *    1 <= s.length <= 5 * 10^4
 *    s contains printable ASCII characters.
 *    s does not contain any leading or trailing spaces.
 *    There is at least one word in s.
 *    All the words in s are separated by a single space.
 */
public class ReverseWordsInAStringIII {

    // V0
    // IDEA: in-place 2 pointer reverse over each space-delimited segment
    /**
     * time = O(n)
     * space = O(n)  (char array copy; O(1) extra beyond the output)
     */
    public String reverseWords(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        char[] arr = s.toCharArray();
        int i = 0;
        for (int j = 0; j <= arr.length; j++) {
            if (j == arr.length || arr[j] == ' ') {
                reverse(arr, i, j - 1);
                i = j + 1;
            }
        }
        return new String(arr);
    }

    private void reverse(char[] arr, int l, int r) {
        while (l < r) {
            char tmp = arr[l];
            arr[l] = arr[r];
            arr[r] = tmp;
            l++;
            r--;
        }
    }

    // V1
    // IDEA: split by space + StringBuilder.reverse()
    /**
     * time = O(n)
     * space = O(n)
     */
    public String reverseWords_1(String s) {
        if (s == null || s.length() <= 1) {
            return s;
        }
        String[] words = s.split(" ", -1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(new StringBuilder(words[i]).reverse());
        }
        return sb.toString();
    }
}
