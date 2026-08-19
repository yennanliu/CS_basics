package LeetCodeJava.Math;

// https://leetcode.com/problems/squirrel-simulation/

/**
 *  573. Squirrel Simulation
 *  Medium
 *
 *  You are given two integers height and width representing a garden of size
 *  height x width. You are also given:
 *    - an array tree where tree = [treer, treec] is the position of the tree in the garden,
 *    - an array squirrel where squirrel = [squirrelr, squirrelc] is the position of the
 *      squirrel in the garden,
 *    - and an array nuts where nuts[i] = [nutir, nutic] is the position of the ith nut.
 *
 *  The squirrel can manage at most one nut at a time and can move in four directions:
 *  up, down, left, and right, to the adjacent cell.
 *
 *  Return the minimal distance for the squirrel to collect all the nuts and put them under
 *  the tree one by one.
 *
 *  Example 1:
 *    Input: height = 5, width = 7, tree = [2,2], squirrel = [4,4], nuts = [[3,0],[2,5]]
 *    Output: 12
 *
 *  Example 2:
 *    Input: height = 1, width = 3, tree = [0,1], squirrel = [0,0], nuts = [[0,2]]
 *    Output: 3
 *
 *  Constraints:
 *    1 <= height, width <= 100
 *    tree.length == 2, squirrel.length == 2, nuts[i].length == 2
 *    1 <= nuts.length <= 5000
 *    All positions are inside the garden and are distinct.
 */
public class SquirrelSimulation {

    // V0
    // IDEA: every nut costs 2 * dist(tree, nut) except the FIRST one, which costs
    //       dist(squirrel, nut) + dist(tree, nut). So total = 2 * sum(dist(tree, nut))
    //       + min over nuts of (dist(squirrel, nut) - dist(tree, nut)).
    /**
     * time = O(n)
     * space = O(1)
     */
    public int minDistance(int height, int width, int[] tree, int[] squirrel, int[][] nuts) {
        int total = 0;
        int bestDelta = Integer.MAX_VALUE;
        for (int[] nut : nuts) {
            int toTree = dist(nut, tree);
            total += 2 * toTree;
            bestDelta = java.lang.Math.min(bestDelta, dist(nut, squirrel) - toTree);
        }
        return total + bestDelta;
    }

    private int dist(int[] a, int[] b) {
        return java.lang.Math.abs(a[0] - b[0]) + java.lang.Math.abs(a[1] - b[1]);
    }
}
