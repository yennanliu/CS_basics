package LeetCodeJava.String;

// https://leetcode.com/problems/reverse-words-in-a-string/description/

import java.util.Arrays;

/**
 * 151. Reverse Words in a String
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an input string s, reverse the order of the words.
 *
 * A word is defined as a sequence of non-space characters. The words in s will be separated by at least one space.
 *
 * Return a string of the words in reverse order concatenated by a single space.
 *
 * Note that s may contain leading or trailing spaces or multiple spaces between two words. The returned string should only have a single space separating the words. Do not include any extra spaces.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "the sky is blue"
 * Output: "blue is sky the"
 * Example 2:
 *
 * Input: s = "  hello world  "
 * Output: "world hello"
 * Explanation: Your reversed string should not contain leading or trailing spaces.
 * Example 3:
 *
 * Input: s = "a good   example"
 * Output: "example good a"
 * Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 104
 * s contains English letters (upper-case and lower-case), digits, and spaces ' '.
 * There is at least one word in s.
 *
 *
 * Follow-up: If the string data type is mutable in your language, can you solve it in-place with O(1) extra space?
 *
 *
 *
 */
public class ReverseWordsInAString {

    // V0
    public String reverseWords(String s) {
        // edge

        /**  NOTE !!!
         *
         *       // replace `multiple space` to single space
         *       // and split
         */
        String[] str = s.trim().split("\\s+");
        //System.out.println(">>> str = " + Arrays.toString(str));
        int n = str.length;

        StringBuilder sb = new StringBuilder();
        for (int i = n - 1; i >= 0; i--) {
            sb.append(str[i]);
            sb.append(" ");
        }

        // NOTE !!! via the trim(), we can remove the redundant space added in last idx
        return sb.toString().trim(); /// ????
    }

    // V1

    // V2
    // https://leetcode.com/problems/reverse-words-in-a-string/solutions/3593904/5-line-simple-solution-with-full-explana-sguj/
    public String reverseWords_2(String s) {
        // Trim the input string to remove leading and trailing spaces
        String[] str = s.trim().split("\\s+");

        // Initialize the output string
        String out = "";

        // Iterate through the words in reverse order
        for (int i = str.length - 1; i > 0; i--) {
            // Append the current word and a space to the output
            out += str[i] + " ";
        }

        // Append the first word to the output (without trailing space)
        return out + str[0];
    }


    // V3-1
    // https://leetcode.com/problems/reverse-words-in-a-string/solutions/6743954/video-2-solutions-bonus-reversed-iterati-w00n/
    public String reverseWords_3_1(String s) {
        String[] words = s.split("\\s+");
        StringBuilder res = new StringBuilder();

        for (int i = words.length - 1; i >= 0; i--) {
            res.append(words[i]);
            if (i != 0) {
                res.append(" ");
            }
        }

        return res.toString().trim();
    }


    // V3-2
    // https://leetcode.com/problems/reverse-words-in-a-string/solutions/6743954/video-2-solutions-bonus-reversed-iterati-w00n/
    public String reverseWords_3_2(String s) {
        String[] words = s.split(" ");
        int left = 0;
        int right = words.length - 1;

        while (left < right) {
            String temp = words[left];
            words[left] = words[right];
            words[right] = temp;
            left++;
            right--;
        }

        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(word);
            }
        }

        return result.toString();
    }


    // V5
    // https://leetcode.com/problems/reverse-words-in-a-string/solutions/47720/clean-java-two-pointers-solution-no-trim-8oqo/
    public String reverseWords_5(String s) {
        if (s == null)
            return null;

        char[] a = s.toCharArray();
        int n = a.length;

        // step 1. reverse the whole string
        reverse(a, 0, n - 1);
        // step 2. reverse each word
        reverseWords(a, n);
        // step 3. clean up spaces
        return cleanSpaces(a, n);
    }

    void reverseWords(char[] a, int n) {
        int i = 0, j = 0;

        while (i < n) {
            while (i < j || i < n && a[i] == ' ')
                i++; // skip spaces
            while (j < i || j < n && a[j] != ' ')
                j++; // skip non spaces
            reverse(a, i, j - 1); // reverse the word
        }
    }

    // trim leading, trailing and multiple spaces
    String cleanSpaces(char[] a, int n) {
        int i = 0, j = 0;

        while (j < n) {
            while (j < n && a[j] == ' ')
                j++; // skip spaces
            while (j < n && a[j] != ' ')
                a[i++] = a[j++]; // keep non spaces
            while (j < n && a[j] == ' ')
                j++; // skip spaces
            if (j < n)
                a[i++] = ' '; // keep only one space
        }

        return new String(a).substring(0, i);
    }

    // reverse a[] from a[i] to a[j]
    private void reverse(char[] a, int i, int j) {
        while (i < j) {
            char t = a[i];
            a[i++] = a[j];
            a[j--] = t;
        }
    }



}
