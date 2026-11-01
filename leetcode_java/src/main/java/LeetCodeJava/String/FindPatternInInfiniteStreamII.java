package LeetCodeJava.String;

// https://leetcode.com/problems/find-pattern-in-infinite-stream-ii/description/

/**
 * 3037. Find Pattern in Infinite Stream II
 * Hard
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
 * 1 <= pattern.length <= 10^4
 * pattern consists only of 0 and 1.
 * stream consists only of 0 and 1.
 * The input is generated such that the pattern's start index exists in the first 10^7 bits of the stream.
 *
 */
public class FindPatternInInfiniteStreamII {

    // dummy API for passing java syntax check
    // offered by LC platform when submission
    interface InfiniteStream {
        int next();
    }

    // V0
    // IDEA: KMP AS A STREAMING AUTOMATON — NO NEED TO STORE THE STREAM
    //       LC 3023's O(m) window compare per bit is too slow for m up to 1e4.
    //       Precompute the failure table once, then keep one integer
    //       `matched` = how many pattern bits match the tail read so far.
    //       matched == m means the match ended at the bit just read.
    /**
     * time = O(N + M)   N = bits read, M = pattern.length
     * space = O(M)
     */
    public int findPattern(InfiniteStream infiniteStream, int[] pattern) {
        int m = pattern.length;

        // fail[i] = longest proper prefix of pattern[0..i] that is also its suffix
        int[] fail = new int[m];
        int matched = 0;
        for (int i = 1; i < m; i++) {
            while (matched > 0 && pattern[i] != pattern[matched]) {
                matched = fail[matched - 1];
            }
            if (pattern[i] == pattern[matched]) {
                matched++;
            }
            fail[i] = matched;
        }

        matched = 0;
        int bitsRead = 0;
        // the problem guarantees a match exists, so this loop terminates
        while (true) {
            int bit = infiniteStream.next();
            bitsRead++;
            while (matched > 0 && bit != pattern[matched]) {
                matched = fail[matched - 1];
            }
            if (bit == pattern[matched]) {
                matched++;
            }
            if (matched == m) {
                return bitsRead - m;
            }
        }
    }
}
