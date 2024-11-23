package LeetCodeJava.Sort;

import java.util.*;


// https://leetcode.com/problems/meeting-rooms-ii/description/
// https://leetcode.ca/all/253.html
/**
 * 253. Meeting Rooms II
 * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), find the minimum number of conference rooms required.
 *
 * Example 1:
 *
 * Input: [[0, 30],[5, 10],[15, 20]]
 * Output: 2
 * Example 2:
 *
 * Input: [[7,10],[2,4]]
 * Output: 1
 * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Apple Atlassian Baidu Bloomberg Booking.com Cisco Citrix Drawbridge eBay Expedia Facebook GoDaddy Goldman Sachs Google Lyft Microsoft Nutanix Oracle Paypal Postmates Quora Snapchat Uber Visa Walmart Labs Yelp
 */

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
    // TODO : validate
    // IDEA : SCANNING LINE
    // ref code : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Sort/meeting-rooms-ii.py#L90
    class Meeting {
        int time;
        String status;

        Meeting(int time, String status) {
            this.time = time;
            this.status = status;
        }

        public int getTime() {
            return time;
        }

        public String getStatus() {
            return status;
        }
    }

    public class MeetingRooms_0_1 {
        public int minMeetingRooms(int[][] intervals) {
            if (intervals.length <= 1) {
                return intervals.length;
            }

            List<Meeting> meetings = new ArrayList<>();

            // Convert intervals into meeting start and end events
            for (int[] interval : intervals) {
                int start = interval[0];
                int end = interval[1];
                meetings.add(new Meeting(start, "open"));
                meetings.add(new Meeting(end, "end"));
            }

            // Sort meetings: by time ascending; if times are equal, "end" comes before "open"
            // V1
//            meetings.sort((x,y) -> {
//                if (x.getTime() < y.getTime()){
//                    return -1;
//                }
//                if (x.getTime() > y.getTime()){
//                    return 1;
//                }
//                return 0;
//            });

            // V2
            meetings.sort((x, y) -> {
                if (x.getTime() != y.getTime()) {
                    return Integer.compare(x.getTime(), y.getTime());
                }
                return x.getStatus().equals("end") ? -1 : 1;
            });

            // Track room requirements
            int maxRooms = 0;
            int currentRooms = 0;

            for (Meeting meeting : meetings) {
                if (meeting.getStatus().equals("open")) {
                    currentRooms++;
                } else {
                    currentRooms--;
                }
                maxRooms = Math.max(maxRooms, currentRooms);
            }

            return maxRooms;
        }
    }

    // V0'
    // TODO : validate
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
            // peak : Retrieves, but does not remove, the head of this queue, or returns null if this queue is empty.
            if (!minHeap.isEmpty() && interval[0] >= minHeap.peek())
                // Retrieves and removes the head of this queue,
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

    // V3
    // https://leetcode.ca/2016-08-09-253-Meeting-Rooms-II/
    public int minMeetingRooms_3(int[][] intervals) {
        int n = 1000010;
        int[] delta = new int[n];
        for (int[] e : intervals) {
            ++delta[e[0]];
            --delta[e[1]];
        }
        int res = delta[0];
        for (int i = 1; i < n; ++i) {
            delta[i] += delta[i - 1];
            res = Math.max(res, delta[i]);
        }
        return res;
    }

}
