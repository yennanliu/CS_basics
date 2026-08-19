package LeetCodeJava.HashTable;

// https://leetcode.com/problems/shortest-word-distance-ii/

import java.util.*;

/**
 *  244. Shortest Word Distance II
 *  Medium
 *
 *  Design a data structure that will be initialized with a string array, and
 *  then it should answer queries of the shortest distance between two different
 *  strings from the array.
 *
 *  Implement the WordDistance class:
 *   - WordDistance(String[] wordsDict) initializes the object with the strings
 *     array wordsDict.
 *   - int shortest(String word1, String word2) returns the shortest distance
 *     between word1 and word2 in the array wordsDict.
 *
 *  Example 1:
 *  Input
 *  ["WordDistance", "shortest", "shortest"]
 *  [[["practice","makes","perfect","coding","makes"]], ["coding","practice"], ["makes","coding"]]
 *  Output
 *  [null, 3, 1]
 *
 *  Constraints:
 *   - 1 <= wordsDict.length <= 3 * 10^4
 *   - 1 <= wordsDict[i].length <= 10
 *   - word1 and word2 are in wordsDict, and word1 != word2
 *   - At most 5000 calls will be made to shortest.
 */
public class ShortestWordDistanceII {

    // V0
    // IDEA: HASHMAP (word -> sorted index list) + 2 POINTERS on the 2 index lists
    public static class WordDistance {

        private final Map<String, List<Integer>> wordIdx;

        /**
         * time = O(n)
         * space = O(n)
         */
        public WordDistance(String[] wordsDict) {
            this.wordIdx = new HashMap<>();
            for (int i = 0; i < wordsDict.length; i++) {
                String w = wordsDict[i];
                List<Integer> idxList = this.wordIdx.get(w);
                if (idxList == null) {
                    idxList = new ArrayList<>();
                    this.wordIdx.put(w, idxList);
                }
                idxList.add(i);
            }
        }

        /**
         * time = O(a + b)   # a, b = occurrence count of word1, word2
         * space = O(1)
         */
        public int shortest(String word1, String word2) {

            List<Integer> l1 = this.wordIdx.get(word1);
            List<Integer> l2 = this.wordIdx.get(word2);

            if (l1 == null || l2 == null) {
                return -1;
            }

            int i = 0;
            int j = 0;
            int res = Integer.MAX_VALUE;

            /**
             *  NOTE !!!
             *
             *  both index lists are already sorted (built via a left -> right scan),
             *  so we can move the pointer with the SMALLER index to shrink the gap
             */
            while (i < l1.size() && j < l2.size()) {
                int a = l1.get(i);
                int b = l2.get(j);
                res = Math.min(res, Math.abs(a - b));
                if (a < b) {
                    i++;
                } else {
                    j++;
                }
            }

            return res;
        }
    }
}
