package LeetCodeJava.Math;

// https://leetcode.com/problems/erect-the-fence/

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *  587. Erect the Fence
 *  Hard
 *
 *  You are given an array trees where trees[i] = [xi, yi] represents the location of a
 *  tree in the garden.
 *
 *  Fence the entire garden using the minimum length of rope, as it is expensive. The garden
 *  is well-fenced only if all the trees are enclosed.
 *
 *  Return the coordinates of trees that are exactly located on the fence perimeter.
 *  You may return the answer in any order.
 *
 *  Example 1:
 *    Input: trees = [[1,1],[2,2],[2,0],[2,4],[3,3],[4,2]]
 *    Output: [[1,1],[2,0],[4,2],[3,3],[2,4]]
 *
 *  Example 2:
 *    Input: trees = [[1,2],[2,2],[4,2]]
 *    Output: [[4,2],[2,2],[1,2]]
 *    Explanation: The fence forms a line that passes through all the trees.
 *
 *  Constraints:
 *    1 <= trees.length <= 3000
 *    trees[i].length == 2
 *    0 <= xi, yi <= 100
 *    All the given positions are unique.
 */
public class ErectTheFence {

    // V0
    // IDEA: Andrew's monotone chain convex hull. We only pop on a STRICT clockwise turn
    //       (cross < 0), so collinear points stay on the hull - the problem asks for every
    //       point lying on the perimeter, not just the corners.
    /**
     * time = O(n log n)
     * space = O(n)
     */
    public int[][] outerTrees(int[][] trees) {
        int n = trees.length;
        if (n <= 3) {
            return trees;
        }

        Arrays.sort(trees, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                return a[0] == b[0] ? a[1] - b[1] : a[0] - b[0];
            }
        });

        int[] hull = new int[2 * n + 1]; // indices into trees
        int k = 0;

        // lower hull
        for (int i = 0; i < n; i++) {
            while (k >= 2 && cross(trees[hull[k - 2]], trees[hull[k - 1]], trees[i]) < 0) {
                k--;
            }
            hull[k++] = i;
        }

        // upper hull
        int lower = k + 1;
        for (int i = n - 2; i >= 0; i--) {
            while (k >= lower && cross(trees[hull[k - 2]], trees[hull[k - 1]], trees[i]) < 0) {
                k--;
            }
            hull[k++] = i;
        }

        // dedupe (start point appears twice, collinear points can repeat)
        Set<Integer> idx = new LinkedHashSet<>();
        for (int i = 0; i < k; i++) {
            idx.add(hull[i]);
        }

        int[][] res = new int[idx.size()][2];
        int j = 0;
        for (Integer i : idx) {
            res[j][0] = trees[i][0];
            res[j][1] = trees[i][1];
            j++;
        }
        return res;
    }

    // cross product of (a -> b) x (a -> c); > 0 = counter-clockwise turn
    private long cross(int[] a, int[] b, int[] c) {
        return (long) (b[0] - a[0]) * (c[1] - a[1]) - (long) (b[1] - a[1]) * (c[0] - a[0]);
    }
}
