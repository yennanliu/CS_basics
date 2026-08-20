package LeetCodeJava.Design;

// https://leetcode.com/problems/frequency-tracker/

import java.util.HashMap;
import java.util.Map;

/**
 *  2671. Frequency Tracker
 *  Medium
 *
 *  Design a data structure that keeps track of the values in it and answers some
 *  queries regarding their frequencies.
 *
 *  Implement the FrequencyTracker class:
 *    FrequencyTracker() Initializes the FrequencyTracker object with an empty array.
 *    void add(int number) Adds number to the data structure.
 *    void deleteOne(int number) Deletes one occurrence of number from the data
 *      structure. The data structure may not contain number, and in this case
 *      nothing is deleted.
 *    boolean hasFrequency(int frequency) Returns true if there is a number in the
 *      data structure that occurs frequency number of times, otherwise false.
 *
 *  Example 1:
 *    Input
 *      ["FrequencyTracker", "add", "add", "hasFrequency"]
 *      [[], [3], [3], [2]]
 *    Output
 *      [null, null, null, true]
 *    Explanation: 3 occurs twice, so hasFrequency(2) -> true
 *
 *  Example 2:
 *    Input
 *      ["FrequencyTracker", "add", "deleteOne", "hasFrequency"]
 *      [[], [1], [1], [1]]
 *    Output
 *      [null, null, null, false]
 *    Explanation: the structure is empty again -> false
 *
 *  Constraints:
 *    1 <= number <= 10^5
 *    1 <= frequency <= 10^5
 *    At most 2 * 10^5 calls will be made to add, deleteOne and hasFrequency.
 */
public class FrequencyTracker {

    // V0
    // IDEA: TWO HASH MAPS -- "value -> its count" AND "count -> how many values
    //       currently have that count"
    //
    //       hasFrequency(f) must not scan the values, so keep the SECOND map as an
    //       inverted index. every add / deleteOne moves one value from bucket
    //       (c) to bucket (c +/- 1), which is a constant number of map writes.
    //       a bucket that drops to 0 is removed so that a stale key never answers
    //       hasFrequency as true.
    /**
     * time = O(1) per add / deleteOne / hasFrequency
     * space = O(n), n = number of distinct values inserted
     */
    private final Map<Integer, Integer> cnt;      // number -> occurrences
    private final Map<Integer, Integer> freqCnt;  // occurrences -> how many numbers

    public FrequencyTracker() {
        this.cnt = new HashMap<>();
        this.freqCnt = new HashMap<>();
    }

    public void add(int number) {
        int c = cnt.containsKey(number) ? cnt.get(number) : 0;
        if (c > 0) {
            bumpFreq(c, -1);
        }
        cnt.put(number, c + 1);
        bumpFreq(c + 1, 1);
    }

    public void deleteOne(int number) {
        Integer c = cnt.get(number);
        if (c == null || c == 0) {
            return; // nothing to delete
        }
        bumpFreq(c, -1);
        if (c == 1) {
            cnt.remove(number);
        } else {
            cnt.put(number, c - 1);
            bumpFreq(c - 1, 1);
        }
    }

    public boolean hasFrequency(int frequency) {
        Integer c = freqCnt.get(frequency);
        return c != null && c > 0;
    }

    private void bumpFreq(int f, int d) {
        Integer old = freqCnt.get(f);
        int nv = (old == null ? 0 : old) + d;
        if (nv <= 0) {
            freqCnt.remove(f);
        } else {
            freqCnt.put(f, nv);
        }
    }
}
