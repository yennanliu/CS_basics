package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/

import java.util.Arrays;

public class MinimumNumberOfArrowsToBurstBalloons {

    // V0
    // IDEA : GREEDY
    // https://www.bilibili.com/video/BV1SA41167xe/?share_source=copy_web&vd_source=771d0eba9b524b4f63f92e37bde71301
    public int findMinArrowShots(int[][] points) {

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
