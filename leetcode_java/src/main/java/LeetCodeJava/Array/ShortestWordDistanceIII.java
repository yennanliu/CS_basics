package LeetCodeJava.Array;

// https://leetcode.com/problems/shortest-word-distance-iii/

/**
 *  245. Shortest Word Distance III
 *  Medium
 *
 *  Given an array of strings wordsDict and two strings word1 and word2,
 *  return the shortest distance between the occurrences of these two words
 *  in the list.
 *
 *  Note that word1 and word2 may be the same. It is guaranteed that they
 *  represent two individual words in the list.
 *
 *  Example 1:
 *   Input: wordsDict = ["practice","makes","perfect","coding","makes"],
 *          word1 = "makes", word2 = "coding"
 *   Output: 1
 *
 *  Example 2:
 *   Input: wordsDict = ["practice","makes","perfect","coding","makes"],
 *          word1 = "makes", word2 = "makes"
 *   Output: 3
 *
 *  Constraints:
 *   1 <= wordsDict.length <= 10^5
 *   1 <= wordsDict[i].length <= 10
 *   word1 and word2 are in wordsDict
 */
public class ShortestWordDistanceIII {

    // V0
    // IDEA: ONE PASS, TRACK LAST SEEN IDX OF EACH WORD;
    //       IF word1 == word2, THE "PREVIOUS" HIT PLAYS THE ROLE OF THE OTHER WORD
    /**
     * time = O(n)
     * space = O(1)
     */
    public int shortestWordDistance(String[] wordsDict, String word1, String word2) {
        boolean same = word1.equals(word2);

        int i1 = -1;
        int i2 = -1;
        int res = Integer.MAX_VALUE;

        for (int i = 0; i < wordsDict.length; i++) {
            String w = wordsDict[i];

            if (w.equals(word1)) {
                if (same) {
                    // NOTE !!! shift previous idx to i2, so distance is between 2 occurrences
                    i2 = i1;
                }
                i1 = i;
            } else if (w.equals(word2)) {
                i2 = i;
            } else {
                continue;
            }

            if (i1 != -1 && i2 != -1) {
                res = Math.min(res, Math.abs(i1 - i2));
            }
        }

        return res;
    }
}
