package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/interval-list-intersections/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 986. Interval List Intersections
 * Medium
 * Topics
 * Companies
 * You are given two lists of closed intervals, firstList and secondList, where firstList[i] = [starti, endi] and secondList[j] = [startj, endj]. Each list of intervals is pairwise disjoint and in sorted order.
 *
 * Return the intersection of these two interval lists.
 *
 * A closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b.
 *
 * The intersection of two closed intervals is a set of real numbers that are either empty or represented as a closed interval. For example, the intersection of [1, 3] and [2, 4] is [2, 3].
 *
 *
 *
 * Example 1:
 *
 *
 * Input: firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
 * Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
 * Example 2:
 *
 * Input: firstList = [[1,3],[5,9]], secondList = []
 * Output: []
 *
 *
 * Constraints:
 *
 * 0 <= firstList.length, secondList.length <= 1000
 * firstList.length + secondList.length >= 1
 * 0 <= starti < endi <= 109
 * endi < starti+1
 * 0 <= startj < endj <= 109
 * endj < startj+1
 */
public class IntervalListIntersections {

    // V0
//    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
//
//    }

    // V1
    // https://leetcode.com/problems/interval-list-intersections/solutions/1593579/java-two-pointers-most-intutive-by-chait-8cfr/
    // IDEA: 2 POINTERS
    public int[][] intervalIntersection_1(int[][] firstList, int[][] secondList) {
        if (firstList.length == 0 || secondList.length == 0)
            return new int[0][0];
        int i = 0;
        int j = 0;
        int startMax = 0, endMin = 0;
        List<int[]> ans = new ArrayList<>();

        while (i < firstList.length && j < secondList.length) {
            startMax = Math.max(firstList[i][0], secondList[j][0]);
            endMin = Math.min(firstList[i][1], secondList[j][1]);

            // you have end greater than start and you already know that this interval is
            // sorrounded with startMin and endMax so this must be the intersection
            if (endMin >= startMax) {
                ans.add(new int[] { startMax, endMin });
            }

            // the interval with min end has been covered completely and have no chance to
            // intersect with any other interval so move that list's pointer
            if (endMin == firstList[i][1])
                i++;
            if (endMin == secondList[j][1])
                j++;
        }

        return ans.toArray(new int[ans.size()][2]);
    }

    // V2
    // https://leetcode.com/problems/interval-list-intersections/solutions/592550/2-ms-faster-than-9963-of-java-and-memory-0pqj/
    public int[][] intervalIntersection_2(int[][] A, int[][] B) {
        int i = 0;
        int j = 0;
        List<int[]> list = new ArrayList<>();
        while (i < A.length && j < B.length) {
            int lower = Math.max(A[i][0], B[j][0]);
            int upper = Math.min(A[i][1], B[j][1]);
            if (lower <= upper) {
                list.add(new int[] { lower, upper });
            }
            if (A[i][1] < B[j][1]) {
                i++;
            } else {
                j++;
            }

        }
        return list.toArray(new int[list.size()][]);
    }

    // V3
    // https://leetcode.com/problems/interval-list-intersections/solutions/6048050/simple-solution-with-diagrams-in-short-v-u77n/
    public int[][] intervalIntersection_3(int[][] firstList, int[][] secondList) {
        List<int[]> intersections = new ArrayList<>(); // to store all
        // intersecting intervals
        // index "i" to iterate over the length of list a and index "j"
        // to iterate over the length of list b
        int i = 0, j = 0;
        // while loop will break whenever either of the lists ends
        while (i < firstList.length && j < secondList.length) {
            // Let's check if firstList[i] intersects secondList[j]
            // 1. start - the potential startpoint of the intersection
            // 2. end - the potential endpoint of the intersection
            int start = Math.max(firstList[i][0], secondList[j][0]);
            int end = Math.min(firstList[i][1], secondList[j][1]);

            if (start <= end) // if this is an actual intersection
                intersections.add(new int[] { start, end }); // add it to the list
            // Move forward in the list whose interval ends earlier
            if (firstList[i][1] < secondList[j][1])
                i += 1;
            else
                j += 1;
        }

        return intersections.toArray(new int[0][]);
    }
    

}
