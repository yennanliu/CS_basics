package LeetCodeJava.Greedy;

// https://leetcode.com/problems/buildings-with-an-ocean-view/description/

import java.util.*;

/**
 * 1762. Buildings With an Ocean View
 * Medium
 *
 * There are n buildings in a line. You are given an integer array heights of
 * size n that represents the heights of the buildings in the line.
 *
 * The ocean is to the right of the buildings. A building has an ocean view if
 * the building can see the ocean without obstructions. Formally, a building has
 * an ocean view if all the buildings to its right have a smaller height.
 *
 * Return a list of indices (0-indexed) of buildings that have an ocean view,
 * sorted in increasing order.
 *
 *
 * Example 1:
 *
 * Input: heights = [4,2,3,1]
 * Output: [0,2,3]
 * Explanation: Building 1 (0-indexed) does not have an ocean view because building 2 is taller.
 *
 * Example 2:
 *
 * Input: heights = [4,3,2,1]
 * Output: [0,1,2,3]
 * Explanation: All the buildings have an ocean view.
 *
 * Example 3:
 *
 * Input: heights = [1,3,2,4]
 * Output: [3]
 * Explanation: Only building 3 has an ocean view.
 *
 *
 * Constraints:
 *
 * 1 <= heights.length <= 10^5
 * 1 <= heights[i] <= 10^9
 *
 */
public class BuildingsWithAnOceanView {

    // V0
    // IDEA: RIGHT TO LEFT SCAN (keep the tallest building seen so far on the right)
    /**
     * time = O(N)
     * space = O(1) extra (output excluded)
     */
    public int[] findBuildings(int[] heights) {
        if (heights == null || heights.length == 0) {
            return new int[0];
        }

        // collected right to left, so indices arrive in decreasing order
        List<Integer> viewIndices = new ArrayList<>();
        int tallestOnRight = 0;

        for (int i = heights.length - 1; i >= 0; i--) {
            // building i sees the ocean iff it is strictly taller than everything to its right
            if (heights[i] > tallestOnRight) {
                viewIndices.add(i);
                tallestOnRight = heights[i];
            }
        }

        // reverse into increasing order
        int size = viewIndices.size();
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = viewIndices.get(size - 1 - i);
        }
        return result;
    }
}
