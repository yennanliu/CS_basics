package LeetCodeJava.String;

// https://leetcode.com/problems/occurrences-after-bigram/description/

import java.util.*;

/**
 * 1078. Occurrences After Bigram
 * Easy
 *
 * Given two strings first and second, consider occurrences in some text of the form
 * "first second third", where second comes immediately after first, and third comes
 * immediately after second.
 *
 * Return an array of all the words third for each occurrence of "first second third".
 *
 *
 * Example 1:
 *
 * Input: text = "alice is a good girl she is a good student", first = "a", second = "good"
 * Output: ["girl","student"]
 *
 * Example 2:
 *
 * Input: text = "we will we will rock you", first = "we", second = "will"
 * Output: ["we","rock"]
 *
 *
 * Constraints:
 *
 * 1 <= text.length <= 1000
 * text consists of lowercase English letters and spaces.
 * All the words in text are separated by a single space.
 * 1 <= first.length, second.length <= 10
 * first and second consist of lowercase English letters.
 * text will not have any leading or trailing spaces.
 *
 */
public class OccurrencesAfterBigram {

    // V0
    // IDEA: SPLIT + SLIDING WINDOW OF THREE WORDS
    /**
     * time = O(N)
     * space = O(N)
     */
    public String[] findOcurrences(String text, String first, String second) {
        String[] words = text.split(" ");
        List<String> result = new ArrayList<>();

        // words[i], words[i+1] form the bigram; words[i+2] is the "third" word
        for (int i = 0; i + 2 < words.length; i++) {
            if (words[i].equals(first) && words[i + 1].equals(second)) {
                result.add(words[i + 2]);
            }
        }
        return result.toArray(new String[0]);
    }
}
