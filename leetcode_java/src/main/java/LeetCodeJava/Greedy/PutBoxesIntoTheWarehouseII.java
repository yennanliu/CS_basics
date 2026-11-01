package LeetCodeJava.Greedy;

// https://leetcode.com/problems/put-boxes-into-the-warehouse-ii/description/

import java.util.Arrays;

/**
 * 1580. Put Boxes Into the Warehouse II
 * Medium
 *
 * You are given two arrays of positive integers, boxes and warehouse, representing
 * the heights of some boxes of unit width and the heights of n rooms in a warehouse
 * respectively. The warehouse's rooms are labeled from 0 to n - 1 from left to
 * right where warehouse[i] (0-indexed) is the height of the ith room.
 *
 * Boxes are put into the warehouse by the following rules:
 *
 * - Boxes cannot be stacked.
 * - You can rearrange the insertion order of the boxes.
 * - Boxes can be pushed into the warehouse from either side (left or right)
 * - If the height of some room in the warehouse is less than the height of a box,
 *   then that box and all other boxes behind it will be stopped before that room.
 *
 * Return the maximum number of boxes you can put into the warehouse.
 *
 *
 * Example 1:
 *
 * Input: boxes = [1,2,2,3,4], warehouse = [3,4,1,2]
 * Output: 4
 *
 * Example 2:
 *
 * Input: boxes = [3,5,5,2], warehouse = [2,1,3,4,5]
 * Output: 3
 * Explanation: It is not possible to put the two boxes of height 5 in the warehouse
 * since there's only 1 room of height >= 5.
 *
 *
 * Constraints:
 *
 * n == warehouse.length
 * 1 <= boxes.length, warehouse.length <= 10^5
 * 1 <= boxes[i], warehouse[i] <= 10^9
 *
 */
public class PutBoxesIntoTheWarehouseII {

    // V0
    // IDEA: PREFIX/SUFFIX MIN + GREEDY (A ROOM IS REACHABLE FROM THE BETTER SIDE)
    //       from the left room i fits boxes <= min(warehouse[0..i]), from the right
    //       <= min(warehouse[i..n-1]); capacity[i] = max of the two.
    //       sort capacities and boxes, give the smallest box to the smallest room
    //       that holds it.
    /**
     * time = O(N log N + B log B)
     * space = O(N)
     */
    public int maxBoxesInWarehouse(int[] boxes, int[] warehouse) {
        int n = warehouse.length;

        // prefixMin[i] = tallest box that can get to room i from the left
        int[] prefixMin = new int[n];
        int current = warehouse[0];
        for (int i = 0; i < n; i++) {
            current = Math.min(current, warehouse[i]);
            prefixMin[i] = current;
        }

        // suffixMin[i] = tallest box that can get to room i from the right
        int[] suffixMin = new int[n];
        current = warehouse[n - 1];
        for (int i = n - 1; i >= 0; i--) {
            current = Math.min(current, warehouse[i]);
            suffixMin[i] = current;
        }

        // the effective capacity of a room is the better of its two entrances
        int[] capacity = new int[n];
        for (int i = 0; i < n; i++) {
            capacity[i] = Math.max(prefixMin[i], suffixMin[i]);
        }

        Arrays.sort(capacity);
        int[] sortedBoxes = boxes.clone();
        Arrays.sort(sortedBoxes);

        // two pointers: each room takes the smallest unplaced box if it fits
        int placed = 0;
        for (int room = 0; room < n; room++) {
            if (placed < sortedBoxes.length && sortedBoxes[placed] <= capacity[room]) {
                placed++;
            }
        }
        return placed;
    }
}
