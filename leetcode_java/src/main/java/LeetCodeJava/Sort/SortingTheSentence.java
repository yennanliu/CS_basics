package LeetCodeJava.Sort;

// https://leetcode.com/problems/sorting-the-sentence/

/**
 *  1859. Sorting the Sentence
 *  Easy
 *
 *  A sentence is a list of words that are separated by a single space with no
 *  leading or trailing spaces. Each word consists of lowercase and uppercase
 *  English letters.
 *
 *  A sentence can be shuffled by appending the 1-indexed word position to each
 *  word then rearranging the words in the sentence.
 *
 *  For example, the sentence "This is a sentence" can be shuffled as
 *  "sentence4 a3 is2 This1" or "is2 sentence4 This1 a3".
 *
 *  Given a shuffled sentence s containing no more than 9 words, reconstruct and
 *  return the original sentence.
 *
 *  Example 1:
 *    Input: s = "is2 sentence4 This1 a3"
 *    Output: "This is a sentence"
 *
 *  Example 2:
 *    Input: s = "Myself2 Me1 I4 and3"
 *    Output: "Me Myself and I"
 *
 *  Constraints:
 *    2 <= s.length <= 200
 *    s consists of lowercase and uppercase English letters, spaces, and digits
 *    from 1 to 9.
 *    The number of words in s is between 1 and 9.
 *    The words in s are separated by a single space, no leading/trailing spaces.
 */
public class SortingTheSentence {

    // V0
    // IDEA: BUCKET PLACEMENT — THE TRAILING DIGIT *IS* THE TARGET INDEX
    //       every token is <word><digit> and the digit is the 1-indexed position
    //       of that word, so no sorting is needed at all: drop each word
    //       straight into slot (digit - 1) of a pre-sized array.
    //       NOTE: at most 9 words, so the position is always a SINGLE char.
    /**
     * time = O(n)
     * space = O(n)
     */
    public String sortSentence(String s) {
        String[] tokens = s.split(" ");
        String[] slots = new String[tokens.length];

        for (String t : tokens) {
            int idx = (t.charAt(t.length() - 1) - '0') - 1;
            slots[idx] = t.substring(0, t.length() - 1);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < slots.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(slots[i]);
        }
        return sb.toString();
    }
}
