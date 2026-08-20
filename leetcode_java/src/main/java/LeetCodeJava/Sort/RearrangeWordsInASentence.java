package LeetCodeJava.Sort;

// https://leetcode.com/problems/rearrange-words-in-a-sentence/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  1451. Rearrange Words in a Sentence
 *  Medium
 *
 *  Given a sentence text (A sentence is a string of space-separated words) in the
 *  following format:
 *    - First letter is in upper case.
 *    - Each word in text are separated by a single space.
 *
 *  Your task is to rearrange the words in text such that all words are rearranged
 *  in an increasing order of their lengths. If two words have the same length,
 *  arrange them in their original order.
 *
 *  Return the new text following the format shown above.
 *
 *  Example 1:
 *    Input: text = "Leetcode is cool"
 *    Output: "Is cool leetcode"
 *
 *  Example 2:
 *    Input: text = "Keep calm and code on"
 *    Output: "On and keep calm code"
 *
 *  Example 3:
 *    Input: text = "To be or not to be"
 *    Output: "To be or to be not"
 *
 *  Constraints:
 *    text begins with a capital letter and then contains lowercase letters and
 *    single space between words.
 *    1 <= text.length <= 10^5
 */
public class RearrangeWordsInASentence {

    // V0
    // IDEA: STABLE SORT BY WORD LENGTH
    //       Arrays.sort on an object array is a STABLE merge sort, so equal-length
    //       words keep their original relative order - exactly the tie-break rule.
    //
    //       de-capitalise the original first word BEFORE sorting (otherwise its
    //       casing would leak into the middle of the sentence), then capitalise
    //       whichever word ends up first.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public String arrangeWords(String text) {
        String[] words = text.split(" ");

        // de-capitalize the original first word
        words[0] = words[0].toLowerCase();

        // stable sort -> ties keep original relative order
        Arrays.sort(words, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return Integer.compare(a.length(), b.length());
            }
        });

        // capitalize the new first word
        words[0] = Character.toUpperCase(words[0].charAt(0)) + words[0].substring(1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(words[i]);
        }
        return sb.toString();
    }
}
