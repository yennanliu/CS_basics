package LeetCodeJava.Math;

// https://leetcode.com/problems/rabbits-in-forest/

import java.util.HashMap;
import java.util.Map;

/**
 *  781. Rabbits in Forest
 *  Medium
 *
 *  There is a forest with an unknown number of rabbits. We asked n rabbits
 *  "How many rabbits have the same color as you?" and collected the answers in
 *  an integer array answers where answers[i] is the answer of the ith rabbit.
 *
 *  Given the array answers, return the minimum number of rabbits that could be
 *  in the forest.
 *
 *  Example 1:
 *    Input: answers = [1,1,2]
 *    Output: 5
 *    Explanation: the two rabbits that answered 1 could be the same color (2
 *                 rabbits). The rabbit that answered 2 implies a group of 3.
 *                 2 + 3 = 5.
 *
 *  Example 2:
 *    Input: answers = [10,10,10]
 *    Output: 11
 *
 *  Constraints:
 *   - 1 <= answers.length <= 1000
 *   - 0 <= answers[i] < 1000
 */
public class RabbitsInForest {

    // V0
    // IDEA: COUNTING / GREEDY.
    //       Rabbits answering x belong to groups of size (x + 1). If c rabbits
    //       answered x, we need ceil(c / (x + 1)) such groups, each contributing
    //       (x + 1) rabbits.
    /**
     * time = O(n), n = answers.length
     * space = O(n)
     */
    public int numRabbits(int[] answers) {

        Map<Integer, Integer> cnt = new HashMap<>();
        for (int a : answers) {
            cnt.put(a, cnt.getOrDefault(a, 0) + 1);
        }

        int res = 0;
        for (Map.Entry<Integer, Integer> e : cnt.entrySet()) {
            int groupSize = e.getKey() + 1;
            int c = e.getValue();
            int groups = (c + groupSize - 1) / groupSize;   // ceil
            res += groups * groupSize;
        }

        return res;
    }
}
