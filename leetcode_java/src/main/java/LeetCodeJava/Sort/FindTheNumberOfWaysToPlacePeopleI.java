package LeetCodeJava.Sort;

// https://leetcode.com/problems/find-the-number-of-ways-to-place-people-i/

import java.util.Arrays;

/**
 *  3025. Find the Number of Ways to Place People I
 *  Medium
 *
 *  You are given a 2D array points of size n x 2 representing integer coordinates
 *  of some points on a 2D-plane, where points[i] = [xi, yi].
 *
 *  You have to place n people, including Alice and Bob, at these points such that
 *  there is exactly one person at every point. Alice wants to be alone with Bob,
 *  so Alice will build a rectangular fence with Alice's position as the upper left
 *  corner and Bob's position as the lower right corner of the fence (the fence
 *  might not enclose any area, i.e. it can be a line). If any person other than
 *  Alice and Bob is either inside the fence or on the fence, Alice will be sad.
 *
 *  Return the number of pairs of points where you can place Alice and Bob, such
 *  that Alice does not become sad on building the fence.
 *
 *  Example 1:
 *    Input: points = [[1,1],[2,2],[3,3]]
 *    Output: 0
 *
 *  Example 2:
 *    Input: points = [[6,2],[4,4],[2,6]]
 *    Output: 2
 *    Explanation: Alice (4,4) + Bob (6,2), and Alice (2,6) + Bob (4,4). Alice
 *                 (2,6) + Bob (6,2) fails because (4,4) is inside the fence.
 *
 *  Constraints:
 *    2 <= n <= 50
 *    points[i].length == 2
 *    0 <= points[i][0], points[i][1] <= 50
 *    All points[i] are distinct.
 */
public class FindTheNumberOfWaysToPlacePeopleI {

    // V0
    // IDEA: SORT BY (x ASC, y DESC), THEN SWEEP KEEPING THE HIGHEST y SEEN
    //       Alice must be up-left of Bob: xA <= xB and yA >= yB. sorting by x
    //       ascending and, on ties, y descending makes every valid Bob come AFTER
    //       its Alice in the order - so a simple i < j scan covers every candidate
    //       pair exactly once.
    //       for a fixed Alice i, walk j forward and track the largest y among the
    //       points already accepted. point j is a legal Bob iff
    //         y[j] <= y[i]   (below-or-level with Alice)
    //         y[j] >  best   (no earlier point sits inside the rectangle)
    //       any point with y between best and y[i] would sit inside the fence, so
    //       raising `best` to y[j] rules out everything flatter afterwards.
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int numberOfPairs(int[][] points) {
        int[][] pts = points.clone();
        Arrays.sort(pts, (a, b) -> a[0] != b[0]
                ? Integer.compare(a[0], b[0])
                : Integer.compare(b[1], a[1]));

        int n = pts.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            int best = Integer.MIN_VALUE;
            for (int j = i + 1; j < n; j++) {
                if (pts[j][1] <= pts[i][1] && pts[j][1] > best) {
                    res++;
                    best = pts[j][1];
                }
            }
        }
        return res;
    }
}
