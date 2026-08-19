package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-product-of-word-lengths/

/**
 *  318. Maximum Product of Word Lengths
 *  Medium
 *
 *  Given a string array words, return the maximum value of
 *  length(word[i]) * length(word[j]) where the two words do not share common
 *  letters. If no such two words exist, return 0.
 *
 *  Example 1:
 *   Input: words = ["abcw","baz","foo","bar","xtfn","abcdef"]
 *   Output: 16   ("abcw" and "xtfn")
 *
 *  Example 2:
 *   Input: words = ["a","ab","abc","d","cd","bcd","abcd"]
 *   Output: 4    ("ab" and "cd")
 *
 *  Example 3:
 *   Input: words = ["a","aa","aaa","aaaa"]
 *   Output: 0
 *
 *  Constraints:
 *   2 <= words.length <= 1000
 *   1 <= words[i].length <= 1000
 *   words[i] consists only of lowercase English letters.
 */
public class MaximumProductOfWordLengths {

    // V0
    // IDEA: encode each word as a 26-bit mask of the letters it uses; two words
    //       share no letter iff (mask1 & mask2) == 0, which makes the pairwise
    //       check O(1).
    /**
     * time = O(n * L + n^2)   // n = words.length, L = avg word length
     * space = O(n)
     */
    public int maxProduct(String[] words) {
        int n = words.length;
        int[] masks = new int[n];

        for (int i = 0; i < n; i++) {
            int mask = 0;
            for (int j = 0; j < words[i].length(); j++) {
                mask |= 1 << (words[i].charAt(j) - 'a');
            }
            masks[i] = mask;
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if ((masks[i] & masks[j]) == 0) {
                    res = Math.max(res, words[i].length() * words[j].length());
                }
            }
        }
        return res;
    }
}
