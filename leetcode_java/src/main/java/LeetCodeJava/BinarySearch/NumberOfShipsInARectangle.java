package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/number-of-ships-in-a-rectangle/description/

/**
 * 1274. Number of Ships in a Rectangle
 * Hard
 *
 * (This problem is an interactive problem.)
 *
 * Each ship is located at an integer point on the sea represented by a cartesian
 * plane, and each integer point may contain at most 1 ship.
 *
 * You have a function Sea.hasShips(topRight, bottomLeft) which takes two points
 * as arguments and returns true If there is at least one ship in the rectangle
 * represented by the two points, including on the boundary.
 *
 * Given two points: the top right and bottom left corners of a rectangle,
 * return the number of ships present in that rectangle.
 * It is guaranteed that there are at most 10 ships in that rectangle.
 *
 * Submissions making more than 400 calls to hasShips will be judged Wrong Answer.
 * Also, any solutions that attempt to circumvent the judge will result in
 * disqualification.
 *
 *
 * Example 1:
 *
 * Input:
 * ships = [[1,1],[2,2],[3,3],[5,5]], topRight = [4,4], bottomLeft = [0,0]
 * Output: 3
 * Explanation: From [0,0] to [4,4] we can count 3 ships within the range.
 *
 * Example 2:
 *
 * Input: ans = [[1,1],[2,2],[3,3]], topRight = [1000,1000], bottomLeft = [0,0]
 * Output: 3
 *
 *
 * Constraints:
 *
 * On the input ships is only given to initialize the map internally.
 * You must solve this problem "blindfolded". In other words, you must find the
 * answer using the given hasShips API, without knowing the ships position.
 * 0 <= bottomLeft[0] <= topRight[0] <= 1000
 * 0 <= bottomLeft[1] <= topRight[1] <= 1000
 * topRight != bottomLeft
 *
 */
public class NumberOfShipsInARectangle {

    /**
     * // This is Sea's API interface.
     * // You should not implement it, or speculate about its implementation
     * class Sea {
     *     public boolean hasShips(int[] topRight, int[] bottomLeft);
     * }
     */
    public interface Sea {
        boolean hasShips(int[] topRight, int[] bottomLeft);
    }

    // V0
    // IDEA: DIVIDE AND CONQUER (QUAD-TREE SPLIT)
    /**
     *  - empty rectangle            -> 0
     *  - hasShips is false          -> 0, and the whole branch is pruned
     *    (this is what keeps the number of hasShips calls small)
     *  - a single cell with a ship  -> 1
     *  - otherwise cut into 4 quadrants around the midpoint and recurse
     */
    /**
     * time = O(S * log(max(W, H))), S = number of ships (<= 10)
     * space = O(log(max(W, H))), recursion depth
     */
    public int countShips(Sea sea, int[] topRight, int[] bottomLeft) {
        int right = topRight[0];
        int top = topRight[1];
        int leftX = bottomLeft[0];
        int bottom = bottomLeft[1];

        // empty rectangle (a split can produce one)
        if (right < leftX || top < bottom) {
            return 0;
        }
        // prune: no ship anywhere in this rectangle
        if (!sea.hasShips(topRight, bottomLeft)) {
            return 0;
        }
        // a single cell that has a ship holds exactly one
        if (right == leftX && top == bottom) {
            return 1;
        }

        int midX = leftX + (right - leftX) / 2;
        int midY = bottom + (top - bottom) / 2;

        int count = 0;
        // bottom-left quadrant
        count += countShips(sea, new int[]{midX, midY}, new int[]{leftX, bottom});
        // bottom-right quadrant
        count += countShips(sea, new int[]{right, midY}, new int[]{midX + 1, bottom});
        // top-left quadrant
        count += countShips(sea, new int[]{midX, top}, new int[]{leftX, midY + 1});
        // top-right quadrant
        count += countShips(sea, new int[]{right, top}, new int[]{midX + 1, midY + 1});
        return count;
    }
}
