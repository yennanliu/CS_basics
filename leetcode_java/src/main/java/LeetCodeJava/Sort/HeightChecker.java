package LeetCodeJava.Sort;

// https://leetcode.com/problems/height-checker/description/

import java.util.*;

/**
 * 1051. Height Checker
 * Easy
 *
 * A school is trying to take an annual photo of all the students. The students are
 * asked to stand in a single file line in non-decreasing order by height.
 * Let this ordering be represented by the integer array expected where expected[i]
 * is the expected height of the ith student in line.
 *
 * You are given an integer array heights representing the current order that the
 * students are standing in. Each heights[i] is the height of the ith student in line
 * (0-indexed).
 *
 * Return the number of indices where heights[i] != expected[i].
 *
 * Example 1:
 *
 * Input: heights = [1,1,4,2,1,3]
 * Output: 3
 * Explanation:
 * heights:  [1,1,4,2,1,3]
 * expected: [1,1,1,2,3,4]
 * Indices 2, 4, and 5 do not match.
 *
 * Example 2:
 *
 * Input: heights = [5,1,2,3,4]
 * Output: 5
 * Explanation:
 * heights:  [5,1,2,3,4]
 * expected: [1,2,3,4,5]
 * All indices do not match.
 *
 * Example 3:
 *
 * Input: heights = [1,2,3,4,5]
 * Output: 0
 * Explanation:
 * heights:  [1,2,3,4,5]
 * expected: [1,2,3,4,5]
 * All indices match.
 *
 * Constraints:
 *
 * 1 <= heights.length <= 100
 * 1 <= heights[i] <= 100
 *
 */
public class HeightChecker {

    // V0
    // IDEA: SORT A COPY + COMPARE INDEX BY INDEX
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int heightChecker(int[] heights) {
        if (heights == null) {
            return 0;
        }

        // expected = the line sorted by height, without touching the input
        int[] expected = heights.clone();
        Arrays.sort(expected);

        int mismatches = 0;
        for (int i = 0; i < heights.length; i++) {
            if (heights[i] != expected[i]) {
                mismatches++;
            }
        }
        return mismatches;
    }

    // V1
    // IDEA: COUNTING SORT (heights are in [1, 100], so the expected line can be
    //       rebuilt from a frequency table in linear time)
    /**
     * time = O(N + K), K = 100 (the height range)
     * space = O(K)
     */
    public int heightChecker_1(int[] heights) {
        if (heights == null) {
            return 0;
        }
        final int maxHeight = 100;

        // count[h] = how many students have height h
        int[] count = new int[maxHeight + 1];
        for (int h : heights) {
            count[h]++;
        }

        // walk the line and the sorted heights in lockstep
        int mismatches = 0;
        int expectedHeight = 1;
        for (int h : heights) {
            // skip heights that have been fully used up
            while (count[expectedHeight] == 0) {
                expectedHeight++;
            }
            if (h != expectedHeight) {
                mismatches++;
            }
            count[expectedHeight]--;
        }
        return mismatches;
    }
}
