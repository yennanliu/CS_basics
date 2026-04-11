package LeetCodeJava.Heap;

// https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/description/
/**
 * 1353. Maximum Number of Events That Can Be Attended
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an array of events where events[i] = [startDayi, endDayi]. Every event i starts at startDayi and ends at endDayi.
 *
 * You can attend an event i at any day d where startDayi <= d <= endDayi. You can only attend one event at any time d.
 *
 * Return the maximum number of events you can attend.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: events = [[1,2],[2,3],[3,4]]
 * Output: 3
 * Explanation: You can attend all the three events.
 * One way to attend them all is as shown.
 * Attend the first event on day 1.
 * Attend the second event on day 2.
 * Attend the third event on day 3.
 * Example 2:
 *
 * Input: events= [[1,2],[2,3],[3,4],[1,2]]
 * Output: 4
 *
 *
 * Constraints:
 *
 * 1 <= events.length <= 105
 * events[i].length == 2
 * 1 <= startDayi <= endDayi <= 105
 *
 * Seen this question in a real interview before?
 * 1/5
 * Yes
 * No
 *
 *
 */

import java.util.Arrays;
import java.util.PriorityQueue;

public class MaximumNumberOfEventsThatCanBeAttended {

    // V0

    // V1
    // IDEA: GREEDY
    // https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/editorial/

    public int maxEvents_1(int[][] events) {
        int n = events.length;
        int maxDay = 0;
        for (int[] event : events) {
            maxDay = Math.max(maxDay, event[1]);
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        Arrays.sort(events, (a, b) -> a[0] - b[0]);
        int ans = 0;
        for (int i = 1, j = 0; i <= maxDay; i++) {
            while (j < n && events[j][0] <= i) {
                pq.offer(events[j][1]);
                j++;
            }
            while (!pq.isEmpty() && pq.peek() < i) {
                pq.poll();
            }
            if (!pq.isEmpty()) {
                pq.poll();
                ans++;
            }
        }

        return ans;
    }


    // V4
    // IDEA : PRIORITY QUEUE (HEAP)
    // https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/solutions/2272460/explanation-of-code-java/
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxEvents_4(int[][] A) {

        PriorityQueue<Integer> pq = new PriorityQueue<Integer>();

        /*
        Sorting according to the end day of the event
        */
        Arrays.sort(A, (a,b) -> Integer.compare(a[0], b[0]));

        int i =0, res = 0, n = A.length;

        for(int d = 1; d<= 100000; d++){

            //maximum limit of the day is 100000, so we are setting according to it
            while(!pq.isEmpty() && pq.peek() < d){
                /*
                this will be the case when some event say [2,3] is in queue and our current day is 4.
				which means end-day for the event [2,3] was 3 and today is day 4 so
				we will just remove it, and it won't be attended
                */
                pq.poll();
            }

            while(i < n && A[i][0] == d)
                /*
                this will be the case when the start day of the event is d.
                for [2,3] it will be 2. and if current day is 2 then we will add this to the queue.
				because if we dont add it on day 2 then when the day 3 comes, we wont be able to add
				[2,3] (because it's start day is 2)
                */
                pq.offer(A[i++][1]);

            if(!pq.isEmpty()){
                /*
                Now we have added the events. and we will just poll one element
                and increase the day count so this event is done.
                and we will move to the next event.

                here pq is priority queue sorted according to the end day.
				so the first element in this queue will be the event that is going to end earliest.
                so we will poll it which means it happened and move to the next day.
                */
                pq.poll();
                res++;
            }
        }

        return res;
    }

    // V5
    // https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/solutions/1419827/java-sort-priorityqueue-solution/
    // IDEA :
    // Sort + PriorityQueue Solution
    // 1. Sort events by start day
    // 2. Store end days of in progress events in min heap
    // 3. Join the earliest ending in progress evetns from the earliest start event to the latest start evetn.
    //    1) Get current date
    //    2) Add just started events at current day in the min heap
    //    3) Join the earliest ending event
    //    4) Remove already ended events
    // 4. Do the loop until we explore all the events and the min heap is empty.
    // Time complexity: O(NlogN)
    // Space complexity: O(N)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxEvents_5(int[][] events) {
        if (events == null || events.length == 0) return 0;
        final int N = events.length;
        // Sort events by start day.
        Arrays.sort(events, (e1, e2) -> Integer.compare(e1[0], e2[0]));
        // Store end days of in progress events in min heap.
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        // Join the earliest ending in progress evetns from the earliest start event to the latest start event.
        int i = 0, day = 0, res = 0;
        while (i < N || !pq.isEmpty()) {
            // Get current date.
            if (pq.isEmpty()) {
                day = events[i][0];
            }
            // Add just started events at current day in the min heap.
            while (i < N && day == events[i][0]) {
                pq.add(events[i][1]);
                i++;
            }
            // Join the earliest ending event.
            pq.poll();
            res++;
            day++;
            // Remove already ended events.
            while (!pq.isEmpty() && day > pq.peek()) {
                pq.poll();
            }
        }
        return res;
    }


    // V6



}
