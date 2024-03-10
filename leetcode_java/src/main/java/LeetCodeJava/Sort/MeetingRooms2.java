package LeetCodeJava.Sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


// https://leetcode.com/problems/meeting-rooms-ii/description/


public class MeetingRooms2 {

    // V0
    // IDEA : ARRAY SORT + BOUNDARY OP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/meeting-rooms-ii.py#L90
    public int minMeetingRooms(int[][] intervals) {

        final int n = intervals.length;
        int ans = 0;
        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; ++i) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }

        Arrays.sort(starts);
        Arrays.sort(ends);

        // J points to ends
        for (int i = 0, j = 0; i < n; ++i)
            if (starts[i] < ends[j])
                ++ans;
            else
                ++j;

        return ans;
    }

    // V0'
    // TODO : validate below
    // IDEA : ARRAY SORT + BOUNDARY OP
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/meeting-rooms-ii.py#L90
    // IDEA : CREATE A NEW ARRAY WITH
    //        (intervals[i][0], 1) // if start point
    //        (intervals[i][1], -1) // if end point
//    public int minMeetingRooms(int[][] intervals) {
//
//        if (intervals == null || intervals.length == 0) {
//            return 0;
//        }
//
//        // Convert intervals to a list of start and end points
//        int n = intervals.length;
//        int[][] points = new int[n * 2][2];
//        for (int i = 0; i < n; i++) {
//            points[i * 2] = new int[]{intervals[i][0], 1}; // start point
//            points[i * 2 + 1] = new int[]{intervals[i][1], -1}; // end point
//        }
//
//        // Sort the points based on their time, if time is same then process end point first
//        Arrays.sort(points, new Comparator<int[]>() {
//            public int compare(int[] a, int[] b) {
//                if (a[0] != b[0]) {
//                    return a[0] - b[0];
//                } else {
//                    return a[1] - b[1];
//                }
//            }
//        });
//
//        // Use a min heap to track the end times of ongoing meetings
//        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
//        int rooms = 0;
//        int maxRooms = 0;
//
//        for (int[] point : points) {
//            if (point[1] == 1) { // start point
//                rooms++;
//            } else { // end point
//                rooms--;
//            }
//            maxRooms = Math.max(maxRooms, rooms);
//        }
//
//        return maxRooms;
//    }

    // V1
    // IDEA : HEAP (PQ) (priority queue)
    // https://walkccc.me/LeetCode/problems/253/#__tabbed_1_2
    public int minMeetingRooms_1(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> (a[0] - b[0]));

        // Store the end times of each room.
        Queue<Integer> minHeap = new PriorityQueue<>();

        for (int[] interval : intervals) {
            // There's no overlap, so we can reuse the same room.
            if (!minHeap.isEmpty() && interval[0] >= minHeap.peek())
                minHeap.poll();
            minHeap.offer(interval[1]);
        }

        return minHeap.size();
    }

    // V2
    // IDEA : SORT
    // https://walkccc.me/LeetCode/problems/253/#__tabbed_1_2
    public int minMeetingRooms_2(int[][] intervals) {

        final int n = intervals.length;
        int ans = 0;
        int[] starts = new int[n];
        int[] ends = new int[n];

        for (int i = 0; i < n; ++i) {
            starts[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }

        Arrays.sort(starts);
        Arrays.sort(ends);

        // J points to ends
        for (int i = 0, j = 0; i < n; ++i)
            if (starts[i] < ends[j])
                ++ans;
            else
                ++j;

        return ans;
    }

}
