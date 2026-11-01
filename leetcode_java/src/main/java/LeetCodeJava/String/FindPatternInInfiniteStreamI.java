package LeetCodeJava.String;

// https://leetcode.com/problems/find-pattern-in-infinite-stream-i/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 3023. Find Pattern in Infinite Stream I
 * Medium
 *
 *
 * You are given a binary array pattern and an object stream of class InfiniteStream representing a 0-indexed infinite stream of bits.
 *
 * The class InfiniteStream contains the following function:
 *
 * int next(): Reads a single bit (which is either 0 or 1) from the stream and returns it.
 *
 * Return the first starting index where the pattern matches the bits read from the stream. For example, if the pattern is [1, 0], the first timestamp where the pattern matches is when the stream reads [1, 0] consecutively.
 *
 *
 * Example 1:
 *
 * Input: stream = [1,1,1,0,1,1,1,...], pattern = [0,1]
 * Output: 3
 * Explanation: The first occurrence of the pattern [0,1] is at index 3, as the stream reads 0 at index 3 and 1 at index 4.
 *
 * Example 2:
 *
 * Input: stream = [0,0,0,0,...], pattern = [0]
 * Output: 0
 * Explanation: The first occurrence of the pattern [0] is at index 0.
 *
 * Example 3:
 *
 * Input: stream = [1,0,1,1,0,1,1,0,1,...], pattern = [1,1,0,1]
 * Output: 2
 * Explanation: The first occurrence of the pattern [1,1,0,1] is at index 2.
 *
 *
 * Constraints:
 *
 * 1 <= pattern.length <= 100
 * pattern consists only of 0 and 1.
 * stream consists only of 0 and 1.
 * The input is generated such that the pattern's start index exists in the first 10^5 bits of the stream.
 *
 */
public class FindPatternInInfiniteStreamI {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    interface InfiniteStream {
        int next();
    }

    // V0
    // IDEA: ROLLING WINDOW OF THE LAST m BITS
    //       the stream is read forwards one bit at a time; keep the latest
    //       m = pattern.length bits in a deque and compare after every read.
    //       O(m) per bit is fine for m <= 100 (LC 3037 needs KMP instead).
    //       The window starts at index bitsRead - m.
    /**
     * time = O(N * M)   N = bits read, M = pattern.length
     * space = O(M)
     */
    public int findPattern(InfiniteStream infiniteStream, int[] pattern) {
        int m = pattern.length;
        Deque<Integer> window = new ArrayDeque<>();
        int bitsRead = 0;

        // the problem guarantees a match exists, so this loop terminates
        while (true) {
            window.addLast(infiniteStream.next());
            bitsRead++;
            // keep only the latest m bits
            if (window.size() > m) {
                window.removeFirst();
            }
            if (window.size() == m && windowMatches(window, pattern)) {
                return bitsRead - m;
            }
        }
    }

    /** True iff the window's bits, oldest first, equal the pattern. */
    private boolean windowMatches(Deque<Integer> window, int[] pattern) {
        int index = 0;
        for (int bit : window) {
            if (bit != pattern[index]) {
                return false;
            }
            index++;
        }
        return true;
    }
}
