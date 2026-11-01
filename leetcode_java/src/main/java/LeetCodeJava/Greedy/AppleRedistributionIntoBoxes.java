package LeetCodeJava.Greedy;

// https://leetcode.com/problems/apple-redistribution-into-boxes/description/

import java.util.*;

/**
 * 3074. Apple Redistribution into Boxes
 * Easy
 *
 * You are given an array apple of size n and an array capacity of size m.
 *
 * There are n packs where the ith pack contains apple[i] apples. There are m boxes as well, and the ith box has a capacity of capacity[i] apples.
 *
 * Return the minimum number of boxes you need to select to redistribute these n packs of apples into boxes.
 *
 * Note that, apples from the same pack can be distributed into different boxes.
 *
 *
 * Example 1:
 *
 * Input: apple = [1,3,2], capacity = [4,3,1,5,2]
 * Output: 2
 * Explanation: We will use boxes with capacities 4 and 5.
 * It is possible to distribute the apples as the total capacity is greater than or equal to the total number of apples.
 *
 * Example 2:
 *
 * Input: apple = [5,5,5], capacity = [2,4,2,7]
 * Output: 4
 * Explanation: We will need to use all the boxes.
 *
 *
 * Constraints:
 *
 * 1 <= n == apple.length <= 50
 * 1 <= m == capacity.length <= 50
 * 1 <= apple[i], capacity[i] <= 50
 * The input is generated such that it's possible to redistribute packs of apples into boxes.
 *
 */
public class AppleRedistributionIntoBoxes {

    // V0
    // IDEA: PACKS CAN BE SPLIT, SO ONLY THE TOTALS MATTER - TAKE THE BIGGEST BOXES
    /**
     *  Apples from one pack may go into different boxes, so we just need the
     *  fewest boxes whose capacities sum to at least the total apples: take
     *  the largest boxes first.
     *
     *  time = O(M log M)
     *  space = O(M)
     */
    public int minimumBoxes(int[] apple, int[] capacity) {
        int need = 0;
        for (int a : apple) {
            need += a;
        }

        int[] sorted = capacity.clone();
        Arrays.sort(sorted);

        // walk from the largest box down
        int boxes = 0;
        for (int i = sorted.length - 1; i >= 0 && need > 0; i--) {
            need -= sorted[i];
            boxes++;
        }
        return boxes;
    }
}
