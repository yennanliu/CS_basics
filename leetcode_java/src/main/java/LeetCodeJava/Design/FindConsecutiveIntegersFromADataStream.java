package LeetCodeJava.Design;

// https://leetcode.com/problems/find-consecutive-integers-from-a-data-stream/

/**
 *  2526. Find Consecutive Integers from a Data Stream
 *  Medium
 *
 *  For a stream of integers, implement a data structure that checks if the last k
 *  integers parsed in the stream are equal to value.
 *
 *  Implement the DataStream class:
 *    DataStream(int value, int k) Initializes the object with an empty integer
 *      stream and the two integers value and k.
 *    boolean consec(int num) Adds num to the stream of integers. Returns true if
 *      the last k integers are equal to value, and false otherwise. If there are
 *      less than k integers, the condition does not hold true, so returns false.
 *
 *  Example 1:
 *    Input
 *      ["DataStream", "consec", "consec", "consec", "consec"]
 *      [[4, 3], [4], [4], [4], [3]]
 *    Output
 *      [null, false, false, true, false]
 *    Explanation
 *      DataStream dataStream = new DataStream(4, 3);
 *      dataStream.consec(4); // only 1 integer parsed -> false
 *      dataStream.consec(4); // 2 < k -> false
 *      dataStream.consec(4); // the 3 parsed integers are all 4 -> true
 *      dataStream.consec(3); // last 3 are [4,4,3] -> false
 *
 *  Constraints:
 *    1 <= value, num <= 10^9
 *    1 <= k <= 10^5
 *    At most 10^5 calls will be made to consec.
 */
public class FindConsecutiveIntegersFromADataStream {

    // V0
    // IDEA: RUNNING STREAK COUNTER (no queue needed)
    //
    //       "are the last k items all == value ?" only depends on the length of
    //       the CURRENT suffix run of `value`, so keep a single counter:
    //         num == value -> streak++
    //         num != value -> streak = 0  (the run is broken; nothing before it
    //                                      can help any future window either)
    //       answer is streak >= k.
    //
    //       a deque of the last k items also works but costs O(k) memory; the
    //       counter collapses that to O(1) because a run LONGER than k still
    //       satisfies the predicate -- we never need to "forget" old matches.
    //       the "fewer than k integers seen" case needs no special handling:
    //       the streak simply cannot have reached k yet.
    /**
     * time = O(1) per consec
     * space = O(1)
     */
    private final int value;
    private final int k;
    private int streak;

    public FindConsecutiveIntegersFromADataStream(int value, int k) {
        this.value = value;
        this.k = k;
        this.streak = 0;
    }

    public boolean consec(int num) {
        if (num == value) {
            streak++;
        } else {
            streak = 0;
        }
        return streak >= k;
    }
}
