package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/
/**
 *  452. Minimum Number of Arrows to Burst Balloons
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * There are some spherical balloons taped onto a flat wall that represents the XY-plane. The balloons are represented as a 2D integer array points where points[i] = [xstart, xend] denotes a balloon whose horizontal diameter stretches between xstart and xend. You do not know the exact y-coordinates of the balloons.
 *
 * Arrows can be shot up directly vertically (in the positive y-direction) from different points along the x-axis. A balloon with xstart and xend is burst by an arrow shot at x if xstart <= x <= xend. There is no limit to the number of arrows that can be shot. A shot arrow keeps traveling up infinitely, bursting any balloons in its path.
 *
 * Given the array points, return the minimum number of arrows that must be shot to burst all balloons.
 *
 *
 *
 * Example 1:
 *
 * Input: points = [[10,16],[2,8],[1,6],[7,12]]
 * Output: 2
 * Explanation: The balloons can be burst by 2 arrows:
 * - Shoot an arrow at x = 6, bursting the balloons [2,8] and [1,6].
 * - Shoot an arrow at x = 11, bursting the balloons [10,16] and [7,12].
 * Example 2:
 *
 * Input: points = [[1,2],[3,4],[5,6],[7,8]]
 * Output: 4
 * Explanation: One arrow needs to be shot for each balloon for a total of 4 arrows.
 * Example 3:
 *
 * Input: points = [[1,2],[2,3],[3,4],[4,5]]
 * Output: 2
 * Explanation: The balloons can be burst by 2 arrows:
 * - Shoot an arrow at x = 2, bursting the balloons [1,2] and [2,3].
 * - Shoot an arrow at x = 4, bursting the balloons [3,4] and [4,5].
 *
 *
 * Constraints:
 *
 * 1 <= points.length <= 105
 * points[i].length == 2
 * -231 <= xstart < xend <= 231 - 1
 *
 *
 */

import java.util.Arrays;
import java.util.Stack;

public class MinimumNumberOfArrowsToBurstBalloons {

    // V0
    public int findMinArrowShots(int[][] points) {
        if (points == null || points.length == 0)
            return 0;

        // 1. MUST SORT: Sorting by the end point is the most efficient greedy strategy here.
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));

        Stack<int[]> st = new Stack<>();
        st.push(points[0]);

        for (int i = 1; i < points.length; i++) {
            int[] prev = st.peek();
            int[] cur = points[i];

            // 2. CHECK OVERLAP:
            // Since we sorted by end points, we only need to check if
            // the current balloon starts before (or at) the previous balloon ends.
            if (cur[0] <= prev[1]) {
                // 3. INTERSECT (Merge):
                // Pop the old range and push the intersection.
                // The intersection end is the MINIMUM of the two ends.
                // The intersection start is the MAXIMUM of the two starts.
                st.pop();
                int[] intersection = new int[] {
                        Math.max(prev[0], cur[0]),
                        Math.min(prev[1], cur[1])
                };
                st.push(intersection);
            } else {
                // No overlap, this balloon needs a new arrow.
                st.push(cur);
            }
        }

        return st.size();
    }

    // NOTE !!! below is WRONG
    // this LC is `arrow burst ballon`,
    // but NOT `interval merge` problem
//    public int findMinArrowShots(int[][] points) {
//        // edge
//        if(points == null || points.length == 0){
//            return 0;
//        }
//        if(points.length == 1){
//            return 1;
//        }
//        //List<Integer[]> list = new ArrayList<>();
//        Stack<int[]> st = new Stack<>();
//        st.add(points[0]);
//
//        // NOTE !!!
//        // 1. SORT by the end coordinate.
//        // This is the greedy key: finish the earliest balloon first to leave room for others.
//        // Use Integer.compare to avoid overflow with negative numbers!
//        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));
//
//        /**  cases prev and cur interval NOT overlap
//         *
//         *    1.
//         *
//         *      |---|               prev
//         *             |----|       cur
//         *
//         *  2.
//         *             |-----|      prev
//         *      |--|                cur
//         *
//         */
//        for(int i = 1; i < points.length; i++){
//            int[] prev = st.peek(); // ??
//            int[] cur = points[i];
//
//            // if `NOT` NOT overlap
//            // -> prev and cur MUST overlap
//            if( !(cur[0] > prev[1]) || !(cur[1] < prev[0]) ){
//                prev = st.pop();
//                st.add( new int[] { Math.min(prev[0], cur[0]), Math.max(prev[1], cur[1]) } );
//            }else{
//                st.add(cur);
//            }
//        }
//
//
//        return st.size();
//    }



    // V0-0-1
    // IDEA : GREEDY
    // https://www.bilibili.com/video/BV1SA41167xe/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
    public int findMinArrowShots_0_0_1(int[][] points) {

        if (points.length == 0 || points.equals(null)){
            return 0;
        }

        int ans = 1;
        // sort
//        print2DArray(points);
//        System.out.println("");

        /** NOTE !!! we sort 2D array via 1st element (idx = 0) */
        Arrays.sort(points, (a, b) -> Integer.compare(a[0], b[0]));

//        print2DArray(points);

        for (int i = 1; i < points.length; i++){
            int[] cur = points[i];
            int[] prev = points[i-1];
            // NOTE !!! don't use below logic
            // case 1) : overlap
//            if (cur[0] < prev[1]){
//                // update cur's right boundary
//                points[i][1] = Math.min(prev[1], cur[1]);
//            // case 2) : NO overlap
//            }else{
//                ans += 1;
//            }

            /** NOTE !!!
             *      we check if NOT overlap first, then use else logic handle overlap cases
             *      since there are actually 2 overlap cases (a bit complex to handle)
             *          - cur[1] > prev[1]
             *          - cur[1] < prev[1]
             *     -> so it's much easy to handle NO overlap case
             *     -> and let else handle overlap cases
             */
            // case 1) NO overlap
            if (cur[0] > prev[1]){
                ans += 1;
            // case 2) : overlap
            }else{
                // NOTE !!! we update i idx 's 2nd element (idx=1)
                points[i][1] = Math.min(prev[1], cur[1]);
            }
        }
        return ans;
    }

    private void print2DArray(int[][] input){
        for (int[] row : input){
            System.out.println(Arrays.toString(row));
        }
    }


    // V0-1
    // IDEA: INTERVAL (gemini)
    public int findMinArrowShots_0_1(int[][] points) {
        if (points == null || points.length == 0)
            return 0;

        // 1. SORT by the end coordinate.
        // This is the greedy key: finish the earliest balloon first to leave room for others.
        // Use Integer.compare to avoid overflow with negative numbers!
        Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));

        // 2. We always need at least one arrow for the first balloon
        int arrows = 1;
        int currentEnd = points[0][1];

        for (int i = 1; i < points.length; i++) {
            // 3. If the next balloon's start is GREATER than our current arrow's position
            if (points[i][0] > currentEnd) {
                // We need a new arrow
                arrows++;
                // Move the arrow to the end of the current balloon
                currentEnd = points[i][1];
            }
            // Else: The balloon overlaps with the current arrow range, so it's popped!
        }

        return arrows;
    }


    // V0-2
    // IDEA: INTERVAL (gpt)
    public int findMinArrowShots_0_2(int[][] points) {

        if (points == null || points.length == 0)
            return 0;

        // sort by end
        Arrays.sort(points, (a, b) -> a[1] == b[1] ? Integer.compare(a[0], b[0]) : Integer.compare(a[1], b[1]));

        int arrows = 1;
        int arrowPos = points[0][1];

        for (int i = 1; i < points.length; i++) {
            if (points[i][0] > arrowPos) {
                arrows++;
                arrowPos = points[i][1];
            }
        }

        return arrows;
    }



    // V1
    // IDEA : GREEDY
    // https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/editorial/
    public int findMinArrowShots_2(int[][] points) {
        if (points.length == 0) return 0;

        // sort by x_end
        Arrays.sort(points, (o1, o2) -> {
            // We can't simply use the o1[1] - o2[1] trick, as this will cause an
            // integer overflow for very large or small values.
            if (o1[1] == o2[1]) return 0;
            if (o1[1] < o2[1]) return -1;
            return 1;
        });

        int arrows = 1;
        int xStart, xEnd, firstEnd = points[0][1];
        for (int[] p: points) {
            xStart = p[0];
            xEnd = p[1];
            // if the current balloon starts after the end of another one,
            // one needs one more arrow
            if (firstEnd < xStart) {
                arrows++;
                firstEnd = xEnd;
            }
        }

        return arrows;
    }




}
