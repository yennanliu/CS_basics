package LeetCodeJava.String;

// https://leetcode.com/problems/rearrange-spaces-between-words/description/

import java.util.*;

/**
 * 1592. Rearrange Spaces Between Words
 * Easy
 *
 * You are given a string text of words that are placed among some number of spaces. Each word consists of one or more lowercase English letters and are separated by at least one space. It's guaranteed that text contains at least one word.
 *
 * Rearrange the spaces so that there is an equal number of spaces between every pair of adjacent words and that number is maximized. If you cannot redistribute all the spaces equally, place the extra spaces at the end, meaning the returned string should be the same length as text.
 *
 * Return the string after rearranging the spaces.
 *
 * Example 1:
 *
 * Input: text = "  this   is  a sentence "
 * Output: "this   is   a   sentence"
 * Explanation: There are a total of 9 spaces and 4 words. We can evenly divide the 9 spaces between the words: 9 / (4-1) = 3 spaces.
 *
 * Example 2:
 *
 * Input: text = " practice   makes   perfect"
 * Output: "practice   makes   perfect "
 * Explanation: There are a total of 7 spaces and 3 words. 7 / (3-1) = 3 spaces plus 1 extra space. We place this extra space at the end of the string.
 *
 * Constraints:
 *
 * 1 <= text.length <= 100
 * text consists of lowercase English letters and ' '.
 * text contains at least one word.
 *
 */
public class RearrangeSpacesBetweenWords {

    // V0
    // IDEA: COUNT THE SPACES, THEN SPREAD THEM EVENLY
    //       with W words and S spaces the gap is S / (W - 1), and the
    //       S % (W - 1) leftovers go to the end. A single word has no gap:
    //       every space trails it (and guards the division by zero).
    /**
     * time = O(N)
     * space = O(N)
     */
    public String reorderSpaces(String text) {
        // count every space and collect the words
        int spaceCount = 0;
        List<String> words = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                spaceCount++;
                if (current.length() > 0) {
                    words.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            words.add(current.toString());
        }

        StringBuilder result = new StringBuilder();
        if (words.size() == 1) {
            result.append(words.get(0));
            appendSpaces(result, spaceCount);
            return result.toString();
        }

        int gap = spaceCount / (words.size() - 1);
        int extra = spaceCount % (words.size() - 1);
        for (int i = 0; i < words.size(); i++) {
            if (i > 0) {
                appendSpaces(result, gap);
            }
            result.append(words.get(i));
        }
        appendSpaces(result, extra);
        return result.toString();
    }

    /** Appends count spaces to sb. */
    private void appendSpaces(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
    }
}
