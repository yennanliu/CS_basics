package LeetCodeJava.Queue;

// https://leetcode.com/problems/moving-average-from-data-stream/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Your MovingAverage object will be instantiated and called as such:
 * MovingAverage obj = new MovingAverage(size);
 * double param_1 = obj.next(val);
 */

public class MovingAverageFromDataStream {

    // V1
    // https://leetcode.com/problems/moving-average-from-data-stream/editorial/
    int size, windowSum = 0, count = 0;
    Deque queue = new ArrayDeque<Integer>();

    public void MovingAverage(int size) {
        this.size = size;
    }

    public double next(int val) {
        ++count;
        // calculate the new sum by shifting the window
        queue.add(val);
        int tail = count > size ? (int)queue.poll() : 0;
        windowSum = windowSum - tail + val;
        return windowSum * 1.0 / Math.min(size, count);
    }

    // V2
    // https://leetcode.com/problems/moving-average-from-data-stream/editorial/
    class MovingAverage {
        int size, windowSum = 0, count = 0;
        Deque queue = new ArrayDeque<Integer>();

        public MovingAverage(int size) {
            this.size = size;
        }

        public double next(int val) {
            ++count;
            // calculate the new sum by shifting the window
            queue.add(val);
            int tail = count > size ? (int)queue.poll() : 0;
            windowSum = windowSum - tail + val;
            return windowSum * 1.0 / Math.min(size, count);
        }
    }

    // V2
    // https://leetcode.com/problems/moving-average-from-data-stream/editorial/
    class MovingAverage3 {
        int size, head = 0, windowSum = 0, count = 0;
        int[] queue;
        public MovingAverage3(int size) {
            this.size = size;
            queue = new int[size];
        }

        public double next(int val) {
            ++count;
            // calculate the new sum by shifting the window
            int tail = (head + 1) % size;
            windowSum = windowSum - queue[tail] + val;
            // move on to the next head
            head = (head + 1) % size;
            queue[head] = val;
            return windowSum * 1.0 / Math.min(size, count);
        }
    }

}
