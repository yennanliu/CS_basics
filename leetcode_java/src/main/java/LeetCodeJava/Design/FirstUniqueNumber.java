package LeetCodeJava.Design;

// https://leetcode.com/problems/first-unique-number/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

/**
 *  1429. First Unique Number
 *  Medium
 *
 *  You have a queue of integers, you need to retrieve the first unique integer in the
 *  queue.
 *
 *  Implement the FirstUnique class:
 *   - FirstUnique(int[] nums) Initializes the object with the numbers in the queue.
 *   - int showFirstUnique() Returns the value of the first unique integer of the queue,
 *     and returns -1 if there is no such integer.
 *   - void add(int value) Inserts value into the queue.
 *
 *  Example 1:
 *    Input:
 *      ["FirstUnique","showFirstUnique","add","showFirstUnique","add",
 *       "showFirstUnique","add","showFirstUnique"]
 *      [[[2,3,5]],[],[5],[],[2],[],[3],[]]
 *    Output:
 *      [null,2,null,2,null,3,null,-1]
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    1 <= nums[i] <= 10^8
 *    1 <= value <= 10^8
 *    At most 50000 calls will be made to showFirstUnique and add.
 */
public class FirstUniqueNumber {

    // V0
    // IDEA: FIFO queue of the values seen for the FIRST time + a frequency map.
    //       showFirstUnique lazily discards the head while its frequency > 1, so every
    //       value leaves the queue at most once -> amortized O(1).
    /**
     * time = O(n) init, amortized O(1) per add / showFirstUnique
     * space = O(n)
     */
    private final Deque<Integer> queue;
    private final Map<Integer, Integer> count;

    public FirstUniqueNumber(int[] nums) {
        this.queue = new ArrayDeque<>();
        this.count = new HashMap<>();
        for (int num : nums) {
            add(num);
        }
    }

    /**
     * time = O(1) amortized
     * space = O(1)
     */
    public int showFirstUnique() {
        while (!this.queue.isEmpty() && this.count.get(this.queue.peekFirst()) > 1) {
            this.queue.pollFirst();
        }
        return this.queue.isEmpty() ? -1 : this.queue.peekFirst();
    }

    /**
     * time = O(1)
     * space = O(1)
     */
    public void add(int value) {
        Integer cur = this.count.get(value);
        if (cur == null) {
            this.count.put(value, 1);
            this.queue.addLast(value); // only the first occurrence enters the queue
        } else {
            this.count.put(value, cur + 1);
        }
    }
}
