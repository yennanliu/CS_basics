package LeetCodeJava.Sort;

// https://leetcode.com/problems/find-the-number-of-ways-to-place-people-ii/

import java.util.Arrays;

/**
 *  3027. Find the Number of Ways to Place People II
 *  Hard
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
 *    Input: points = [[3,1],[1,3],[1,1]]
 *    Output: 2
 *    Explanation: Alice (1,1) + Bob (3,1), and Alice (1,3) + Bob (1,1). Alice
 *                 (1,3) + Bob (3,1) fails because (1,1) lies on the fence.
 *
 *  Constraints:
 *    2 <= n <= 1000
 *    points[i].length == 2
 *    -10^9 <= points[i][0], points[i][1] <= 10^9
 *    All points[i] are distinct.
 */
public class FindTheNumberOfWaysToPlacePeopleII {

    // V0
    // IDEA: SAME SWEEP AS LC 3025 - IT WAS ALREADY O(n^2)
    //       sort by (x ascending, y descending) so every candidate Bob follows its
    //       Alice in the order. then for each Alice i, scan forward tracking the
    //       highest y accepted so far:
    //         point j is a valid Bob  <=>  y[j] <= y[i]  and  y[j] > best
    //       because anything with a y strictly between `best` and y[i] would land
    //       inside the fence.
    //       the only thing this sequel changes is the scale - n grows to 1000 and
    //       the coordinates to +-10^9 - so the O(n^3) "check every third point"
    //       version is out, while this sweep still runs in 10^6 steps.
    //       NOTE: coordinates reach +-10^9, so compare with Integer.compare, never
    //             with a - b (which overflows), and seed `best` with MIN_VALUE.
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
            long best = Long.MIN_VALUE;
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
