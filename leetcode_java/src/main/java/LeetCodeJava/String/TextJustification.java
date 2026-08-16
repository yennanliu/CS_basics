package LeetCodeJava.String;

// https://leetcode.com/problems/text-justification/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 68. Text Justification
 * Hard
 *
 * Given an array of strings words and a width maxWidth, format the text such that each
 * line has exactly maxWidth characters and is fully (left and right) justified.
 *
 * You should pack your words in a greedy approach; that is, pack as many words as you can
 * in each line. Pad extra spaces ' ' when necessary so that each line has exactly maxWidth
 * characters.
 *
 * Extra spaces between words should be distributed as evenly as possible. If the number of
 * spaces on a line does not divide evenly between words, the empty slots on the left will
 * be assigned more spaces than the slots on the right.
 *
 * For the last line of text, it should be left-justified, and no extra space is inserted
 * between words.
 *
 * Note:
 *
 * - A word is defined as a character sequence consisting of non-space characters only.
 * - Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
 * - The input array words contains at least one word.
 *
 *
 * Example 1:
 *
 * Input: words = ["This", "is", "an", "example", "of", "text", "justification."],
 *        maxWidth = 16
 * Output:
 * [
 *    "This    is    an",
 *    "example  of text",
 *    "justification.  "
 * ]
 *
 * Example 2:
 *
 * Input: words = ["What","must","be","acknowledgment","shall","be"], maxWidth = 16
 * Output:
 * [
 *   "What   must   be",
 *   "acknowledgment  ",
 *   "shall be        "
 * ]
 * Explanation: Note that the last line is "shall be    " instead of "shall     be",
 * because the last line must be left-justified instead of fully-justified.
 * Note that the second line is also left-justified because it contains only one word.
 *
 * Example 3:
 *
 * Input: words = ["Science","is","what","we","understand","well","enough","to","explain",
 * "to","a","computer.","Art","is","everything","else","we","do"], maxWidth = 20
 * Output:
 * [
 *   "Science  is  what we",
 *   "understand      well",
 *   "enough to explain to",
 *   "a  computer.  Art is",
 *   "everything  else  we",
 *   "do                  "
 * ]
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 300
 * 1 <= words[i].length <= 20
 * words[i] consists of only English letters and symbols.
 * 1 <= maxWidth <= 100
 * words[i].length <= maxWidth
 *
 */
public class TextJustification {

    // V0
    // IDEA: GREEDY line packing + space distribution
    /**
     *  Pack while (sum of word lengths) + (min 1 space per gap) fits in maxWidth.
     *
     *  NOTE !!! `line.size()` IS the number of gaps needed if we append one more word
     *           (k words already there -> k gaps once the new word joins),
     *           which is why the fit test reads `lineLen + line.size() + w.length()`.
     *
     *  TWO special cases must NOT be fully justified:
     *    - a line holding a SINGLE word (no gap to spread spaces into)
     *    - the LAST line (left justified by the problem statement)
     *
     *  time  = O(n * maxWidth), n = words.length
     *  space = O(maxWidth) extra (excluding output)
     */
    public List<String> fullJustify(String[] words, int maxWidth) {
        List<String> res = new ArrayList<>();

        List<String> line = new ArrayList<>(); // words collected for the current line
        int lineLen = 0;                       // sum of word lengths (spaces NOT counted)

        for (String w : words) {
            if (lineLen + line.size() + w.length() > maxWidth) {
                res.add(justify(line, lineLen, maxWidth));
                line = new ArrayList<>();
                lineLen = 0;
            }
            line.add(w);
            lineLen += w.length();
        }

        // LAST line -> left justified, single spaces, pad the tail
        StringBuilder last = new StringBuilder();
        for (int i = 0; i < line.size(); i++) {
            if (i > 0) {
                last.append(' ');
            }
            last.append(line.get(i));
        }
        while (last.length() < maxWidth) {
            last.append(' ');
        }
        res.add(last.toString());

        return res;
    }

    private String justify(List<String> line, int lineLen, int maxWidth) {
        // SINGLE word -> left justified (no gap to spread spaces into)
        if (line.size() == 1) {
            StringBuilder sb = new StringBuilder(line.get(0));
            while (sb.length() < maxWidth) {
                sb.append(' ');
            }
            return sb.toString();
        }

        int gaps = line.size() - 1;
        /** NOTE !!!
         *
         *  base spaces per gap, plus ONE extra for the `extra` LEFTMOST gaps
         *  -> that is what makes the left slots wider than the right ones
         */
        int base = (maxWidth - lineLen) / gaps;
        int extra = (maxWidth - lineLen) % gaps;

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < line.size() - 1; i++) {
            out.append(line.get(i));
            int spaces = base + (i < extra ? 1 : 0);
            for (int s = 0; s < spaces; s++) {
                out.append(' ');
            }
        }
        out.append(line.get(line.size() - 1));
        return out.toString();
    }

}
