package LeetCodeJava.String;

// https://leetcode.com/problems/text-justification/description/

import java.util.Arrays;
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


    // V1
    // IDEA: TWO PHASES -- decide the line breaks, then render
    /**
     *  First pass computes only the BREAK POINTS (which word starts each line);
     *  the second pass renders. Neither phase has to think about the other.
     *
     *  Separating them is what makes the `last line is special` rule a single
     *  branch in the renderer rather than a condition threaded through the packer.
     *
     *  time  = O(n * maxWidth)
     *  space = O(n)
     */
    public List<String> fullJustify_1(String[] words, int maxWidth) {
        int n = words.length;

        // breaks[i] = index one past the last word of the line starting at i
        List<int[]> lines = new ArrayList<>();
        int i = 0;
        while (i < n) {
            int len = words[i].length();
            int j = i + 1;
            while (j < n && len + 1 + words[j].length() <= maxWidth) {
                len += 1 + words[j].length();
                j += 1;
            }
            lines.add(new int[] { i, j });
            i = j;
        }

        List<String> res = new ArrayList<>();
        for (int t = 0; t < lines.size(); t++) {
            int from = lines.get(t)[0];
            int to = lines.get(t)[1];
            boolean lastLine = t == lines.size() - 1;
            res.add(render(words, from, to, maxWidth, lastLine));
        }
        return res;
    }

    private String render(String[] words, int from, int to, int maxWidth, boolean lastLine) {
        int wordCount = to - from;
        int letters = 0;
        for (int k = from; k < to; k++) {
            letters += words[k].length();
        }

        StringBuilder sb = new StringBuilder();
        if (lastLine || wordCount == 1) {
            for (int k = from; k < to; k++) {
                if (k > from) {
                    sb.append(' ');
                }
                sb.append(words[k]);
            }
            while (sb.length() < maxWidth) {
                sb.append(' ');
            }
            return sb.toString();
        }

        int gaps = wordCount - 1;
        int spaces = maxWidth - letters;
        for (int k = from; k < to; k++) {
            sb.append(words[k]);
            if (k == to - 1) {
                break;
            }
            int idx = k - from;
            // CLOSED FORM: gap idx gets ceil of the remaining share
            int width = (spaces + gaps - 1 - idx) / gaps;
            for (int s = 0; s < width; s++) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

    // V2
    // IDEA: PRE-FILLED char[] CANVAS -- write words at computed offsets
    /**
     *  Allocate one char[maxWidth] per line already full of spaces and copy each
     *  word to its offset. The padding is then implicit -- nothing has to append
     *  spaces at all.
     *
     *  One allocation per line and no StringBuilder growth, which is the shape a
     *  real text layout engine uses.
     *
     *  time  = O(n * maxWidth)
     *  space = O(maxWidth)
     */
    public List<String> fullJustify_2(String[] words, int maxWidth) {
        int n = words.length;
        List<String> res = new ArrayList<>();

        int i = 0;
        while (i < n) {
            int len = words[i].length();
            int j = i + 1;
            while (j < n && len + 1 + words[j].length() <= maxWidth) {
                len += 1 + words[j].length();
                j += 1;
            }

            char[] canvas = new char[maxWidth];
            Arrays.fill(canvas, ' ');

            int wordCount = j - i;
            boolean lastLine = j == n;

            if (lastLine || wordCount == 1) {
                int at = 0;
                for (int k = i; k < j; k++) {
                    words[k].getChars(0, words[k].length(), canvas, at);
                    at += words[k].length() + 1;
                }
            } else {
                int letters = 0;
                for (int k = i; k < j; k++) {
                    letters += words[k].length();
                }
                int gaps = wordCount - 1;
                int base = (maxWidth - letters) / gaps;
                int extra = (maxWidth - letters) % gaps;

                int at = 0;
                for (int k = i; k < j; k++) {
                    words[k].getChars(0, words[k].length(), canvas, at);
                    at += words[k].length();
                    if (k < j - 1) {
                        at += base + ((k - i) < extra ? 1 : 0);
                    }
                }
            }

            res.add(new String(canvas));
            i = j;
        }
        return res;
    }

    // V3
    // IDEA: BUILD EACH LINE WITH String.join + an explicit pad helper
    /**
     *  Assemble the gap strings first (a list of `n-1` space runs), then interleave
     *  them with the words using String.join-style concatenation.
     *
     *  The most readable of the three -- the space DISTRIBUTION is computed as data
     *  before any text is produced, so it can be asserted on directly.
     *
     *  time  = O(n * maxWidth)
     *  space = O(maxWidth)
     */
    public List<String> fullJustify_3(String[] words, int maxWidth) {
        int n = words.length;
        List<String> res = new ArrayList<>();

        int i = 0;
        while (i < n) {
            int len = words[i].length();
            int j = i + 1;
            while (j < n && len + 1 + words[j].length() <= maxWidth) {
                len += 1 + words[j].length();
                j += 1;
            }

            int wordCount = j - i;
            boolean lastLine = j == n;
            StringBuilder sb = new StringBuilder();

            if (lastLine || wordCount == 1) {
                for (int k = i; k < j; k++) {
                    if (k > i) {
                        sb.append(' ');
                    }
                    sb.append(words[k]);
                }
                sb.append(spaces(maxWidth - sb.length()));
            } else {
                int letters = 0;
                for (int k = i; k < j; k++) {
                    letters += words[k].length();
                }
                int gaps = wordCount - 1;
                int total = maxWidth - letters;

                // the gap WIDTHS, computed as data before any rendering
                int[] widths = new int[gaps];
                for (int g = 0; g < gaps; g++) {
                    widths[g] = total / gaps + (g < total % gaps ? 1 : 0);
                }

                for (int k = i; k < j; k++) {
                    sb.append(words[k]);
                    if (k < j - 1) {
                        sb.append(spaces(widths[k - i]));
                    }
                }
            }

            res.add(sb.toString());
            i = j;
        }
        return res;
    }

    private String spaces(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

}
