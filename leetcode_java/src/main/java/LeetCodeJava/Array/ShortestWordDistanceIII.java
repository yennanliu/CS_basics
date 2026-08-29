package LeetCodeJava.Array;


import java.util.ArrayList;
import java.util.List;
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

    // V1
    // IDEA: COLLECT THE OCCURRENCE INDEX LISTS FIRST, THEN MERGE THEM WITH
    //       TWO POINTERS (min gap between 2 sorted index lists). If the 2 words
    //       are the same, the answer is the min gap of ADJACENT indexes.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int shortestWordDistance_1(String[] wordsDict, String word1, String word2) {
        List<Integer> idx1 = new ArrayList<>();
        List<Integer> idx2 = new ArrayList<>();
        for (int i = 0; i < wordsDict.length; i++) {
            if (wordsDict[i].equals(word1)) {
                idx1.add(i);
            }
            if (wordsDict[i].equals(word2)) {
                idx2.add(i);
            }
        }

        int res = Integer.MAX_VALUE;

        if (word1.equals(word2)) {
            // same word -> min distance of 2 consecutive occurrences
            for (int i = 1; i < idx1.size(); i++) {
                res = Math.min(res, idx1.get(i) - idx1.get(i - 1));
            }
            return res;
        }

        // 2 sorted lists -> classic merge, advance the smaller pointer
        int a = 0;
        int b = 0;
        while (a < idx1.size() && b < idx2.size()) {
            int i = idx1.get(a);
            int j = idx2.get(b);
            res = Math.min(res, Math.abs(i - j));
            if (i < j) {
                a++;
            } else {
                b++;
            }
        }
        return res;
    }

    // V2
    // IDEA: brute force O(n^2) - compare every (word1 idx, word2 idx) pair.
    //       Kept as a readable correctness reference.
    /**
     * time = O(n^2)
     * space = O(1)
     */
    public int shortestWordDistance_2(String[] wordsDict, String word1, String word2) {
        int res = Integer.MAX_VALUE;
        for (int i = 0; i < wordsDict.length; i++) {
            if (!wordsDict[i].equals(word1)) {
                continue;
            }
            for (int j = 0; j < wordsDict.length; j++) {
                // when word1 == word2 the 2 picks must still be different elements
                if (i == j) {
                    continue;
                }
                if (wordsDict[j].equals(word2)) {
                    res = Math.min(res, Math.abs(i - j));
                }
            }
        }
        return res;
    }

}
