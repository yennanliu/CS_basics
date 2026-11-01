package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-building-height/description/

import java.util.*;

/**
 * 1840. Maximum Building Height
 * Hard
 *
 * You want to build n new buildings in a city. The new buildings will be built
 * in a line and are labeled from 1 to n.
 *
 * However, there are city restrictions on the heights of the new buildings:
 *
 * - The height of each building must be a non-negative integer.
 * - The height of the first building must be 0.
 * - The height difference between any two adjacent buildings cannot exceed 1.
 *
 * Additionally, there are city restrictions on the maximum height of specific
 * buildings. These restrictions are given as a 2D integer array restrictions
 * where restrictions[i] = [id_i, maxHeight_i] indicates that building id_i must
 * have a height less than or equal to maxHeight_i.
 *
 * It is guaranteed that each building will appear at most once in restrictions,
 * and building 1 will not be in restrictions.
 *
 * Return the maximum possible height of the tallest building.
 *
 *
 * Example 1:
 *
 * Input: n = 5, restrictions = [[2,1],[4,1]]
 * Output: 2
 * Explanation: We can build the buildings with heights [0,1,2,1,2], and the
 * tallest building has a height of 2.
 *
 * Example 2:
 *
 * Input: n = 6, restrictions = []
 * Output: 5
 * Explanation: We can build the buildings with heights [0,1,2,3,4,5], and the
 * tallest building has a height of 5.
 *
 * Example 3:
 *
 * Input: n = 10, restrictions = [[5,3],[2,5],[7,4],[10,3]]
 * Output: 5
 * Explanation: We can build the buildings with heights [0,1,2,3,3,4,4,5,4,3],
 * and the tallest building has a height of 5.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^9
 * 0 <= restrictions.length <= min(n - 1, 10^5)
 * 2 <= id_i <= n
 * id_i is unique.
 * 0 <= maxHeight_i <= 10^9
 *
 */
public class MaximumBuildingHeight {

    // V0
    // IDEA: TIGHTEN THE RESTRICTIONS BOTH WAYS, THEN FIND THE PEAK BETWEEN NEIGHBOURS
    /**
     * time = O(M log M), M = restrictions.length
     * space = O(M)
     */
    public int maxBuilding(int n, int[][] restrictions) {
        // copy into longs: heights and ids up to 10^9 are added together below
        List<long[]> marks = new ArrayList<>();
        for (int[] restriction : restrictions) {
            marks.add(new long[] {restriction[0], restriction[1]});
        }
        // building 1 is fixed at height 0
        marks.add(new long[] {1, 0});
        marks.sort((a, b) -> Long.compare(a[0], b[0]));
        // an unrestricted last building can rise by 1 per step; n - 1 is never binding
        if (marks.get(marks.size() - 1)[0] != n) {
            marks.add(new long[] {n, n - 1});
        }

        int m = marks.size();

        // 1) left -> right: a mark cannot be higher than its left neighbour plus the distance
        for (int i = 1; i < m; i++) {
            long distance = marks.get(i)[0] - marks.get(i - 1)[0];
            marks.get(i)[1] = Math.min(marks.get(i)[1], marks.get(i - 1)[1] + distance);
        }
        // 2) right -> left: same rule from the other side; now every mark is reachable
        for (int i = m - 2; i >= 0; i--) {
            long distance = marks.get(i + 1)[0] - marks.get(i)[0];
            marks.get(i)[1] = Math.min(marks.get(i)[1], marks.get(i + 1)[1] + distance);
        }

        // 3) between two marks the profile climbs from both ends and meets in the middle
        long tallest = 0;
        for (int i = 0; i + 1 < m; i++) {
            long leftHeight = marks.get(i)[1];
            long rightHeight = marks.get(i + 1)[1];
            long distance = marks.get(i + 1)[0] - marks.get(i)[0];
            long peak = (leftHeight + rightHeight + distance) / 2;
            tallest = Math.max(tallest, peak);
        }
        return (int) tallest;
    }
}
