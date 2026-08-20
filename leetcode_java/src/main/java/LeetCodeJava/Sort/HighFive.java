package LeetCodeJava.Sort;

// https://leetcode.com/problems/high-five/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

/**
 *  1086. High Five
 *  Easy
 *
 *  Given a list of the scores of different students, items, where
 *  items[i] = [ID_i, score_i] represents one score from a student with ID_i,
 *  calculate each student's top five average.
 *
 *  Return the answer as an array of pairs result, where
 *  result[j] = [ID_j, topFiveAverage_j] represents the student with ID_j and their
 *  top five average. Sort result by ID_j in increasing order.
 *
 *  A student's top five average is the sum of their top five scores divided by 5
 *  using integer division.
 *
 *  Example 1:
 *    Input: items = [[1,91],[1,92],[2,93],[2,97],[1,60],[2,77],[1,65],[1,87],
 *                    [1,100],[2,100],[2,76]]
 *    Output: [[1,87],[2,88]]
 *    Explanation: student 1 -> (100 + 92 + 91 + 87 + 65) / 5 = 87 ;
 *                 student 2 -> (100 + 97 + 93 + 77 + 76) / 5 = 88.
 *
 *  Example 2:
 *    Input: items = [[1,100],[7,100],[1,100],[7,100],[1,100],[7,100],[1,100],
 *                    [7,100],[1,100],[7,100]]
 *    Output: [[1,100],[7,100]]
 *
 *  Constraints:
 *    1 <= items.length <= 1000
 *    items[i].length == 2
 *    1 <= ID_i <= 1000
 *    0 <= score_i <= 100
 *    For each ID_i, there will be at least five scores.
 */
public class HighFive {

    // V0
    // IDEA: GROUP BY id + FIXED-SIZE MIN HEAP OF 5 SCORES PER STUDENT
    //       bucket every score under its student id in a TreeMap (so the ids come
    //       out in increasing order for free, no final sort of the output).
    //       per student keep a MIN heap capped at 5: pushing a 6th score evicts
    //       the smallest, so the heap always holds exactly the current top 5 -
    //       no full per-student sort is needed.
    //       the average is the heap's sum divided by 5 (integer division).
    /**
     * time = O(n log 5 + k log k), k = number of distinct ids
     * space = O(k)
     */
    public int[][] highFive(int[][] items) {
        TreeMap<Integer, PriorityQueue<Integer>> heaps = new TreeMap<>();
        for (int[] it : items) {
            PriorityQueue<Integer> h = heaps.get(it[0]);
            if (h == null) {
                h = new PriorityQueue<>();          // min heap
                heaps.put(it[0], h);
            }
            h.offer(it[1]);
            if (h.size() > 5) {
                h.poll();                           // drop the smallest
            }
        }

        List<int[]> res = new ArrayList<>();
        for (Map.Entry<Integer, PriorityQueue<Integer>> e : heaps.entrySet()) {
            int sum = 0;
            for (int s : e.getValue()) {
                sum += s;
            }
            res.add(new int[]{e.getKey(), sum / 5});
        }
        return res.toArray(new int[0][]);
    }
}
