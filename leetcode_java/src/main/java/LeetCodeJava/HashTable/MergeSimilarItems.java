package LeetCodeJava.HashTable;

// https://leetcode.com/problems/merge-similar-items/description/

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 2363. Merge Similar Items
 * Easy
 *
 * You are given two 2D integer arrays, items1 and items2, representing two sets of items. Each array items has the following properties:
 *
 * items[i] = [valuei, weighti] where valuei represents the value and weighti represents the weight of the ith item.
 * The value of each item in items is unique.
 *
 * Return a 2D integer array ret where ret[i] = [valuei, weighti], with weighti being the sum of weights of all items with value valuei.
 *
 * Note: ret should be returned in ascending order by value.
 *
 *
 * Example 1:
 *
 * Input: items1 = [[1,1],[4,5],[3,8]], items2 = [[3,1],[1,5]]
 * Output: [[1,6],[3,9],[4,5]]
 * Explanation:
 * The item with value = 1 occurs in items1 with weight = 1 and in items2 with weight = 5, total weight = 1 + 5 = 6.
 * The item with value = 3 occurs in items1 with weight = 8 and in items2 with weight = 1, total weight = 8 + 1 = 9.
 * The item with value = 4 occurs in items1 with weight = 5, total weight = 5.
 * Therefore, we return [[1,6],[3,9],[4,5]].
 *
 * Example 2:
 *
 * Input: items1 = [[1,1],[3,2],[2,3]], items2 = [[2,1],[3,2],[1,3]]
 * Output: [[1,4],[2,4],[3,4]]
 * Explanation:
 * The item with value = 1 occurs in items1 with weight = 1 and in items2 with weight = 3, total weight = 1 + 3 = 4.
 * The item with value = 2 occurs in items1 with weight = 3 and in items2 with weight = 1, total weight = 3 + 1 = 4.
 * The item with value = 3 occurs in items1 with weight = 2 and in items2 with weight = 2, total weight = 2 + 2 = 4.
 * Therefore, we return [[1,4],[2,4],[3,4]].
 *
 * Example 3:
 *
 * Input: items1 = [[1,3],[2,2]], items2 = [[7,1],[2,2],[1,4]]
 * Output: [[1,7],[2,4],[7,1]]
 * Explanation:
 * The item with value = 1 occurs in items1 with weight = 3 and in items2 with weight = 4, total weight = 3 + 4 = 7.
 * The item with value = 2 occurs in items1 with weight = 2 and in items2 with weight = 2, total weight = 2 + 2 = 4.
 * The item with value = 7 occurs in items2 with weight = 1, total weight = 1.
 * Therefore, we return [[1,7],[2,4],[7,1]].
 *
 *
 * Constraints:
 *
 * 1 <= items1.length, items2.length <= 1000
 * items1[i].length == items2[i].length == 2
 * 1 <= valuei, weighti <= 1000
 * Each valuei in items1 is unique.
 * Each valuei in items2 is unique.
 *
 */
public class MergeSimilarItems {

    // V0
    // IDEA: HASH TABLE (TreeMap) SUMMING WEIGHTS BY VALUE, EMITTED IN ASCENDING VALUE ORDER
    /**
     * time = O((N + M) log(N + M))
     * space = O(N + M)
     */
    public List<List<Integer>> mergeSimilarItems(int[][] items1, int[][] items2) {
        Map<Integer, Integer> totalWeight = new TreeMap<>();
        for (int[] item : items1) {
            totalWeight.put(item[0], totalWeight.getOrDefault(item[0], 0) + item[1]);
        }
        for (int[] item : items2) {
            totalWeight.put(item[0], totalWeight.getOrDefault(item[0], 0) + item[1]);
        }

        List<List<Integer>> merged = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : totalWeight.entrySet()) {
            List<Integer> pair = new ArrayList<>();
            pair.add(entry.getKey());
            pair.add(entry.getValue());
            merged.add(pair);
        }
        return merged;
    }
}
