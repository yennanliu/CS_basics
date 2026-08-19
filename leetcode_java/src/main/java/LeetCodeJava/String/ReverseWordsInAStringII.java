package LeetCodeJava.String;

// https://leetcode.com/problems/reverse-words-in-a-string-ii/

/**
 *  186. Reverse Words in a String II
 *  Medium
 *
 *  Given a character array s, reverse the order of the words.
 *
 *  A word is defined as a sequence of non-space characters. The words in s
 *  are separated by a single space. Your code must solve the problem in-place,
 *  i.e. without allocating extra space.
 *
 *  Example 1:
 *    Input:  s = ["t","h","e"," ","s","k","y"," ","i","s"," ","b","l","u","e"]
 *    Output: ["b","l","u","e"," ","i","s"," ","s","k","y"," ","t","h","e"]
 *  Example 2:
 *    Input: s = ["a"]   Output: ["a"]
 *
 *  Constraints:
 *    1 <= s.length <= 10^5
 *    s[i] is an English letter, digit, or space ' '.
 *    There is at least one word in s; no leading/trailing spaces;
 *    words are separated by a single space.
 */
public class ReverseWordsInAStringII {

    // V0
    // IDEA: reverse the whole array, then reverse each word back in place
    /**
     * time = O(n)
     * space = O(1)
     */
    public void reverseWords(char[] s) {
        if (s == null || s.length == 0) {
            return;
        }

        // 1) reverse everything
        reverse(s, 0, s.length - 1);

        // 2) reverse each word back
        int start = 0;
        for (int i = 0; i <= s.length; i++) {
            if (i == s.length || s[i] == ' ') {
                reverse(s, start, i - 1);
                start = i + 1;
            }
        }
    }

    private void reverse(char[] s, int left, int right) {
        while (left < right) {
            char tmp = s[left];
            s[left] = s[right];
            s[right] = tmp;
            left++;
            right--;
        }
    }
}
