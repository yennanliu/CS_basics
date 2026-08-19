package LeetCodeJava.String;

// https://leetcode.com/problems/goat-latin/

/**
 *  824. Goat Latin
 *  Easy
 *
 *  You are given a string sentence that consist of words separated by spaces.
 *  Each word consists of lowercase and uppercase letters only.
 *  Convert the sentence to "Goat Latin":
 *    - If a word begins with a vowel ('a','e','i','o','u' upper or lower),
 *      append "ma" to the end of the word.
 *    - Otherwise, remove the first letter, append it to the end, then add "ma".
 *    - Add one letter 'a' to the end of each word per its word index in the
 *      sentence, starting with 1.
 *  Return the final sentence.
 *
 *  Example 1:
 *    Input:  sentence = "I speak Goat Latin"
 *    Output: "Imaa peaksmaaa oatGmaaaa atinLmaaaaa"
 *
 *  Example 2:
 *    Input:  sentence = "The quick brown fox jumped over the lazy dog"
 *    Output: "heTmaa uickqmaaa rownbmaaaa oxfmaaaaa umpedjmaaaaaa
 *             overmaaaaaaa hetmaaaaaaaa azylmaaaaaaaaa ogdmaaaaaaaaaa"
 *
 *  Constraints:
 *    1 <= sentence.length <= 150
 *    sentence consists of English letters and spaces.
 *    sentence has no leading or trailing spaces.
 *    All the words in sentence are separated by a single space.
 */
public class GoatLatin {

    // V0
    // IDEA: split on space, rewrite each word, append a growing run of 'a'
    /**
     * time = O(n^2)   (the trailing 'a' run grows with the word index)
     * space = O(n^2)
     */
    public String toGoatLatin(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return sentence;
        }
        String vowels = "aeiouAEIOU";
        String[] words = sentence.split(" ");
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String w = words[i];
            if (i > 0) {
                res.append(' ');
            }
            if (vowels.indexOf(w.charAt(0)) >= 0) {
                res.append(w);
            } else {
                res.append(w.substring(1)).append(w.charAt(0));
            }
            res.append("ma");
            for (int k = 0; k <= i; k++) {
                res.append('a');
            }
        }
        return res.toString();
    }
}
