package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-additions-to-make-valid-string/
/**
 *  2645. Minimum Additions to Make Valid String
 *
 *  Given a string word to which you can insert letters "a", "b" or "c" anywhere and any number of times, return the minimum number of letters that must be inserted so that word becomes valid.
 *
 * A string is called valid if it can be formed by concatenating the string "abc" several times.
 *
 *  Example 1:
 *
 * Input: word = "b"
 * Output: 2
 * Explanation: Insert the letter "a" right before "b", and the letter "c" right next to "b" to obtain the valid string "abc".
 * Example 2:
 *
 * Input: word = "aaa"
 * Output: 6
 * Explanation: Insert letters "b" and "c" next to each "a" to obtain the valid string "abcabcabc".
 * Example 3:
 *
 * Input: word = "abc"
 * Output: 0
 * Explanation: word is already valid. No modifications are needed.
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 50
 * word consists of letters "a", "b" and "c" only.
 *
 *
 */
public class MinimumAdditionsToMakeValidString {

    // V0
//    public int addMinimum(String word) {
//
//    }

    // V1
    // https://leetcode.com/problems/minimum-additions-to-make-valid-string/solutions/3421831/javacpython-easy-and-concise-with-explan-bj9h/
    /**  IDEA:
     *
     *  ------------
     *
     *  Intuition
     *
     *  Same problem:
     *    Find out the minimum k where word is subsequence of "abc" repeated k times.
     *
     *   Since "abc" is increasing,
     *    so we can split the original work into k strict increasing subarray.
     *
     * -------------
     *
     *
     * Explanation
     *
     *  Initial the prev as a big char,
     *  then iterate each char c in word.
     *  If c <= prev, it means we need to start a new "abc",
     *  then we increase k++.
     *
     *   Finally we find k, word is subsequence of "abc" repeated k times.
     *    We return k * 3 - n.
     *
     */
    public int addMinimum_1(String word) {
        int k = 0, prev = 'z', n = word.length();
        for (int i = 0; i < n; ++i) {
            k += word.charAt(i) <= prev ? 1 : 0;
            prev = word.charAt(i);
        }
        return k * 3 - n;
    }

    // V2
    // https://leetcode.com/problems/minimum-additions-to-make-valid-string/solutions/3421709/c-java-python-on-greedy-solution-easy-to-p8dg/
    public int addMinimum_2(String word) {
        int n = word.length(), i = 0, res = 0;

        while (i < n) {
            int count = 0;

            if (word.charAt(i) == 'a') {
                count++;
                i++;
            }

            if (i < n && word.charAt(i) == 'b') {
                count++;
                i++;
            }

            if (i < n && word.charAt(i) == 'c') {
                count++;
                i++;
            }

            res += 3 - count;
        }

        return res;
    }


    // V3
    // https://leetcode.com/problems/minimum-additions-to-make-valid-string/solutions/3421989/circular-matching-constant-space-by-xxvv-yoi3/


}
