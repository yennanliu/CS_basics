package LeetCodeJava.Math;

// https://leetcode.com/problems/building-boxes/description/

/**
 * 1739. Building Boxes
 * Hard
 *
 * You have a cubic storeroom where the width, length, and height of the room are all equal to n units. You are asked to place n boxes in this room where each box is a cube of unit side length. There are however some rules to placing the boxes:
 *
 * You can place the boxes anywhere on the floor.
 * If box x is placed on top of the box y, then each side of the four vertical sides of the box y must either be adjacent to another box or to a wall.
 *
 * Given an integer n, return the minimum possible number of boxes touching the floor.
 *
 *
 * Example 1:
 *
 * Input: n = 3
 * Output: 3
 * Explanation: The figure above is for the placement of the three boxes.
 * These boxes are placed in the corner of the room, where the corner is on the left side.
 *
 * Example 2:
 *
 * Input: n = 4
 * Output: 3
 * Explanation: The figure above is for the placement of the four boxes.
 * These boxes are placed in the corner of the room, where the corner is on the left side.
 *
 * Example 3:
 *
 * Input: n = 10
 * Output: 6
 * Explanation: The figure above is for the placement of the ten boxes.
 * These boxes are placed in the corner of the room, where the corner is on the back side.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 *
 */
public class BuildingBoxes {

    // V0
    // IDEA: GREEDY STAIRCASE + MATH (build a corner pyramid, then extend its floor)
    //   with k full layers the floor is a triangle of T(k) = k(k+1)/2 boxes and the total is
    //   T(1) + ... + T(k). phase 1: grow k while the whole pyramid fits in n.
    //   phase 2: each extra floor box (the i-th one) lets i more boxes stand in total,
    //   so consume the remainder in chunks 1, 2, 3, ..., one floor box per chunk.
    /**
     * time = O(N^(1/3))
     * space = O(1)
     */
    public int minimumBoxes(int n) {
        // phase 1: as many complete pyramid layers as fit
        long used = 0;
        long layers = 1;
        while (used + layers * (layers + 1) / 2 <= n) {
            used += layers * (layers + 1) / 2;
            layers++;
        }
        layers--;

        // floor footprint of the full pyramid
        long floor = layers * (layers + 1) / 2;

        // phase 2: add floor boxes one at a time for the remainder
        long extra = 1;
        while (used < n) {
            floor++;
            used += extra;
            extra++;
        }
        return (int) floor;
    }
}
