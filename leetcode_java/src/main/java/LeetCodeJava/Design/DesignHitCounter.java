package LeetCodeJava.Design;

// https://leetcode.com/problems/design-hit-counter/description/
// https://leetcode.ca/all/362.html

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * 362. Design Hit Counter
 * Design a hit counter which counts the number of hits received in the past 5 minutes.
 *
 * Each function accepts a timestamp parameter (in seconds granularity) and you may assume that calls are being made to the system in chronological order (ie, the timestamp is monotonically increasing). You may assume that the earliest timestamp starts at 1.
 *
 * It is possible that several hits arrive roughly at the same time.
 *
 * Example:
 *
 * HitCounter counter = new HitCounter();
 *
 * // hit at timestamp 1.
 * counter.hit(1);
 *
 * // hit at timestamp 2.
 * counter.hit(2);
 *
 * // hit at timestamp 3.
 * counter.hit(3);
 *
 * // get hits at timestamp 4, should return 3.
 * counter.getHits(4);
 *
 * // hit at timestamp 300.
 * counter.hit(300);
 *
 * // get hits at timestamp 300, should return 4.
 * counter.getHits(300);
 *
 * // get hits at timestamp 301, should return 3.
 * counter.getHits(301);
 * Follow up:
 * What if the number of hits per second could be very large? Does your design scale?
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Adobe Amazon Apple Bloomberg Booking.com Dropbox Google Indeed LinkedIn Microsoft Oracle Pinterest Quip (Salesforce) Snapchat Uber Visa Yahoo Zillow
 * Problem Solution
 * 362-Design-Hit-Counter
 *
 */
public class DesignHitCounter {

    // V0
    // TODO: validate
    class HitCounter {

        /**  counter : {timestamp : cnt }
         */
        private Map<Integer, Integer> counter;

        /** Initialize your data structure here. */
        public HitCounter() {
            counter = new HashMap<>(); // ?? check
        }

        /**
         Record a hit.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public void hit(int timestamp) {
            // counter.put(timestamp, counter.getOrDefault(timestamp, 0) + 1);
            int curCnt = counter.getOrDefault(counter,0);
            //counter.putIfAbsent(timestamp, curCnt+1);
            counter.put(timestamp, curCnt+1);
        }

        /**
         Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public int getHits(int timestamp) {
            int val = 0;
            for(Integer key : counter.keySet()){
                if (key >= 0 && key <= timestamp - 60 * 5){
                    //val += 1; // ??
                    val += counter.get(key);
                }
            }
            return val;
        }
    }

    // V1-1
    // https://leetcode.ca/2016-11-26-362-Design-Hit-Counter/
    public class HitCounter_1_1 {
        private ArrayDeque<Integer> queue; // @note: ArrayDeque has no capacity restrictions

        // Why is ArrayDeque better than LinkedList?
        // If you need add/remove of the both ends, ArrayDeque is significantly better than a linked list
        // https://stackoverflow.com/questions/6163166/why-is-arraydeque-better-than-linkedlist

        /** Initialize your data structure here. */
        public HitCounter_1_1() {
            queue = new ArrayDeque<Integer>();
        }

        /** Record a hit.
         @param timestamp - The current timestamp (in seconds granularity). */
        public void hit(int timestamp) {
            queue.offer(timestamp);
        }

        /** Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity). */

        // Time Complexity : O(n)
        public int getHits(int timestamp) {
            int startTime = timestamp - 300;

            // remove all hits over 300 seconds old
            while(!queue.isEmpty() && queue.peek() <= startTime) {
                queue.poll();
            }
            return queue.size();
        }
    }

    public class HitCounterFollowUp {
        private int[] times;
        private int[] hits;

        /** Initialize your data structure here. */
        public HitCounterFollowUp() {
            int[] times = new int[300];
            int[] hits = new int[300];
        }

        /** Record a hit.
         @param timestamp - The current timestamp (in seconds granularity). */
        public void hit(int timestamp) {
            int idx = timestamp % 300;
            if (times[idx] != timestamp) {
                times[idx] = timestamp; // update with most recent timestamp
                hits[idx] = 1;
            } else {
                ++hits[idx];
            }
        }

        /** Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity). */

        // Time Complexity : O(n)
        public int getHits(int timestamp) {
            int res = 0;
            for (int i = 0; i < 300; ++i) {
                if (timestamp - times[i] < 300) { // values in times[] not ordered at all
                    res += hits[i];
                }
            }
            return res;
        }
    }

    // V1-2
    // https://leetcode.ca/2016-11-26-362-Design-Hit-Counter/
    class HitCounter_1_2 {

        private Map<Integer, Integer> counter;

        /** Initialize your data structure here. */
        public HitCounter_1_2() {
            counter = new HashMap<>();
        }

        /**
         Record a hit.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public void hit(int timestamp) {
            counter.put(timestamp, counter.getOrDefault(timestamp, 0) + 1);
        }

        /**
         Return the number of hits in the past 5 minutes.
         @param timestamp - The current timestamp (in seconds granularity).
         */
        public int getHits(int timestamp) {
            int hits = 0;
            for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
                if (entry.getKey() + 300 > timestamp) {
                    hits += entry.getValue();
                }
            }
            return hits;
        }
    }


    // V2
}
