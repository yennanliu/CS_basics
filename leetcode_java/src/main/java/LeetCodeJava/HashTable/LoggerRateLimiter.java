package LeetCodeJava.HashTable;

// https://leetcode.ca/2016-11-23-359-Logger-Rate-Limiter/
// https://leetcode.com/problems/logger-rate-limiter/description/

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 359. Logger Rate Limiter
 * Description
 * Design a logger system that receives a stream of messages along with their timestamps. Each unique message should only be printed at most every 10 seconds (i.e. a message printed at timestamp t will prevent other identical messages from being printed until timestamp t + 10).
 *
 * All messages will come in chronological order. Several messages may arrive at the same timestamp.
 *
 * Implement the Logger class:
 *
 * Logger() Initializes the logger object.
 * bool shouldPrintMessage(int timestamp, string message) Returns true if the message should be printed in the given timestamp, otherwise returns false.
 *
 *
 * Example 1:
 *
 * Input
 * ["Logger", "shouldPrintMessage", "shouldPrintMessage", "shouldPrintMessage", "shouldPrintMessage", "shouldPrintMessage", "shouldPrintMessage"]
 * [[], [1, "foo"], [2, "bar"], [3, "foo"], [8, "bar"], [10, "foo"], [11, "foo"]]
 * Output
 * [null, true, true, false, false, false, true]
 *
 * Explanation
 * Logger logger = new Logger();
 * logger.shouldPrintMessage(1, "foo");  // return true, next allowed timestamp for "foo" is 1 + 10 = 11
 * logger.shouldPrintMessage(2, "bar");  // return true, next allowed timestamp for "bar" is 2 + 10 = 12
 * logger.shouldPrintMessage(3, "foo");  // 3 < 11, return false
 * logger.shouldPrintMessage(8, "bar");  // 8 < 12, return false
 * logger.shouldPrintMessage(10, "foo"); // 10 < 11, return false
 * logger.shouldPrintMessage(11, "foo"); // 11 >= 11, return true, next allowed timestamp for "foo" is 11 + 10 = 21
 *
 *
 * Constraints:
 *
 * 0 <= timestamp <= 109
 * Every timestamp will be passed in non-decreasing order (chronological order).
 * 1 <= message.length <= 30
 * At most 104 calls will be made to shouldPrintMessage.
 *
 */

public class LoggerRateLimiter {

    // V0
    // IDEA : HASHMAP
    // TODO : validate below
    class Logger {

        private Map<String, Integer> limiter;

        /** Initialize your data structure here. */
        public Logger() {
            limiter = new HashMap<>();
        }
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean shouldPrintMessage(int timestamp, String message) {
            if (!this.limiter.containsKey(message)){
                this.limiter.put(message, timestamp+10);
                return true;
            }
            int canPrintTime = this.limiter.get(message);
            if (timestamp > canPrintTime){
                return true;
            }
            return false;
        }

    }

    // V0-1
    // IDEA: HASHMAP (fixed by gemini)
    class Logger_0_1 {

        // attr: Stores the next allowable timestamp for each message
        private final Map<String, Integer> limiter;

        /** Initialize your data structure here. */
        public Logger_0_1() {
            // init
            this.limiter = new HashMap<>();
        }

        /**
         * Returns true if the message should be printed in the given timestamp, otherwise returns false.
         * If the message has not been printed before OR the current timestamp is >= its next allowable print time,
         * update the map and return true.
         */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean shouldPrintMessage(int timestamp, String message) {

            // 1. Check if the message has been seen before
            if (!this.limiter.containsKey(message)) {
                // Case A: First time seeing this message

                // Allow printing and set the next allowable timestamp (current + 10)
                this.limiter.put(message, timestamp + 10);
                return true;

            } else {
                // Case B: Message has been seen, check the time limit

                int nextAllowableTime = this.limiter.get(message);

                // Check if the current timestamp is GREATER THAN OR EQUAL TO the next allowable time
                if (timestamp >= nextAllowableTime) {

                    // Allow printing and update the next allowable timestamp
                    this.limiter.put(message, timestamp + 10);
                    return true;
                } else {
                    // The message is rate-limited
                    return false;
                }
            }
        }
    }
    /**
     * Your Logger object will be instantiated and called as such:
     * Logger obj = new Logger();
     * boolean param_1 = obj.shouldPrintMessage(timestamp,message);
     */

    // V0-2
    // IDEA: HASHMAP (fixed by gpt)
    class Logger_0_2 {

        private Map<String, Integer> limiter;

        public Logger_0_2() {
            this.limiter = new HashMap<>();
        }
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean shouldPrintMessage(int timestamp, String message) {
            // if message was never logged before
            if (!this.limiter.containsKey(message)) {
                this.limiter.put(message, timestamp + 10);
                return true;
            }

            // get the next allowed time
            int nextAllowed = this.limiter.get(message);

            // if current time is >= next allowed time → we can print
            if (timestamp >= nextAllowed) {
                this.limiter.put(message, timestamp + 10);
                return true;
            }

            // else → cannot print
            return false;
        }
    }


    // V1
    class Logger_1 {

        private Map<String, Integer> limiter;

        /** Initialize your data structure here. */
        public Logger_1() {
            limiter = new HashMap<>();
        }

        /**
         Returns true if the message should be printed in the given timestamp, otherwise returns
         false. If this method returns false, the message will not be printed. The timestamp is in
         seconds granularity.
         */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    /**
     * time = O(N)
     * space = O(N)
     */
    public boolean shouldPrintMessage(int timestamp, String message) {
            int t = limiter.getOrDefault(message, 0);
            if (t > timestamp) {
                return false;
            }
            limiter.put(message, timestamp + 10);
            return true;
        }
    }


    // V2



}
