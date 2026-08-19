package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/sentence-screen-fitting/

/**
 *  418. Sentence Screen Fitting
 *  Medium
 *
 *  Given a rows x cols screen and a sentence represented as a list of strings,
 *  return the number of times the given sentence can be fitted on the screen.
 *
 *  The order of words in the sentence must remain unchanged, and a word cannot
 *  be split into two lines. A single space must separate two consecutive words
 *  in a line.
 *
 *  Example 1:
 *
 *  Input: sentence = ["hello","world"], rows = 2, cols = 8
 *  Output: 1
 *
 *  Example 2:
 *
 *  Input: sentence = ["a", "bcd", "e"], rows = 3, cols = 6
 *  Output: 2
 *
 *  Example 3:
 *
 *  Input: sentence = ["i","had","apple","pie"], rows = 4, cols = 5
 *  Output: 1
 *
 *  Constraints:
 *
 *  1 <= sentence.length <= 100
 *  1 <= sentence[i].length <= 10
 *  sentence[i] consists of lowercase English letters.
 *  1 <= rows, cols <= 2 * 10^4
 */
public class SentenceScreenFitting {

    // V0
    // IDEA: treat the sentence as one endless string "w1 w2 ... wn ";
    //       keep a global pointer, advance it by `cols` per row, then fix up
    //       so we never break in the middle of a word.
    /**
     * time = O(rows * cols)
     * space = O(L), L = total length of sentence + spaces
     */
    public int wordsTyping(String[] sentence, int rows, int cols) {
        StringBuilder sb = new StringBuilder();
        for (String w : sentence) {
            sb.append(w).append(' ');
        }
        String s = sb.toString();
        int len = s.length();

        int start = 0; // how many chars (of the endless string) are consumed
        for (int i = 0; i < rows; i++) {
            start += cols;
            if (s.charAt(start % len) == ' ') {
                // line ends exactly at a word boundary -> skip the space
                start++;
            } else {
                // we cut a word in half -> walk back to its beginning
                while (start > 0 && s.charAt((start - 1) % len) != ' ') {
                    start--;
                }
            }
        }
        return start / len;
    }

    // V1
    // IDEA: DP - precompute, for each starting word index, how many words are
    //       consumed by one row; then just accumulate over rows.
    /**
     * time = O(n * cols + rows), n = sentence.length
     * space = O(n)
     */
    public int wordsTyping_1(String[] sentence, int rows, int cols) {
        int n = sentence.length;
        // next[i] = total number of words placed on a row that starts with word i
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            int cnt = 0;
            int remain = cols;
            int j = i;
            while (remain >= sentence[j].length()) {
                remain -= sentence[j].length();
                remain -= 1; // trailing space
                cnt++;
                j = (j + 1) % n;
            }
            next[i] = cnt;
        }

        int total = 0;
        int cur = 0;
        for (int r = 0; r < rows; r++) {
            total += next[cur];
            cur = (cur + next[cur]) % n;
        }
        return total / n;
    }
}
